package com.bigdata.traffic.warning

import java.sql.DriverManager
import java.util.Properties

import com.bigdata.traffic.util.{JdbcWriterDataSink, MonitorLimitInfo, OutOfLimitSpeedInfo, RepetitionCarWarningInfo, TrafficLog, ViolationInfo}
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.cep.scala.{CEP, PatternStream}
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector
import org.apache.kafka.common.serialization.StringDeserializer

import scala.collection.Map

/**
 * 套牌车分析
 */
object DangeroursDriverAnalysis {

  def main(args: Array[String]): Unit = {

    val streamEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //导入隐式转换
    import org.apache.flink.streaming.api.scala._
    streamEnv.setParallelism(1)
    //定义Kafka的连接属性
    val props = new Properties()
    props.setProperty("bootstrap.servers","hadoop101:9092,hadoop104:9092")
    props.setProperty("group.id","traffic01")
    props.setProperty("key.deserializer",classOf[StringDeserializer].getName)
    props.setProperty("value.deserializer",classOf[StringDeserializer].getName)
    props.setProperty("auto.offset.reset","latest")

    //假设，数据出现乱序，一般不超过5秒
    streamEnv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    //Kafka的Source
    //    val mainStream: DataStream[TrafficLog] = streamEnv.addSource(new FlinkKafkaConsumer[String]("t0601_traffic", new SimpleStringSchema(), props).setStartFromEarliest())
    //    val mainStream: DataStream[TrafficLog] = streamEnv.readTextFile(getClass.getResource("/traffic_data").getPath)
    val mainStream: DataStream[TrafficLog] = streamEnv.socketTextStream("hadoop102",8888)
      .map(line => {
        val arr: Array[String] = line.split(",")
        TrafficLog(arr(0).toLong, arr(1), arr(2), arr(3), arr(4).toDouble, arr(5), arr(6))
      })

    //使用广播状态流(直接把流中的数据存放到状态)，现在使用富函数类
    //1、准备数据流
    val stream: DataStream[OutOfLimitSpeedInfo] = mainStream.map( new OutOfSpeedFunction(80) )
        .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[OutOfLimitSpeedInfo](Time.seconds(5)) {
          override def extractTimestamp(element: OutOfLimitSpeedInfo) = element.actionTime
        })

    //2、定义模式
    val pattern: Pattern[OutOfLimitSpeedInfo, OutOfLimitSpeedInfo] = Pattern.begin[OutOfLimitSpeedInfo]("start")
      .where(t => {
        t.limitSpeed * 1.2 < t.realSpeed //超速20%
      })
      .timesOrMore(3) //超速三次以上，包括三次则就是危险假设
      .greedy //在两分钟内，使用贪婪模式： 在2分钟内尽可能匹配更多次超速
      .within(Time.minutes(2))

    //3、在数据流中匹配得到检测流
    val ps: PatternStream[OutOfLimitSpeedInfo] = CEP.pattern(stream.keyBy(_.car),pattern)


    //4、选择结果
    ps.select(
      //map代表两分钟匹配的所有结果,根据模式定义的里面的名字来确定map集合的size
      // 当前代码中，map集合只有一条数据，key:start value:迭代器中会有多条数据
      (map: Map[String, Iterable[OutOfLimitSpeedInfo]] )=> {
          val list: List[OutOfLimitSpeedInfo] = map.get("start").get.toList
        //既然是危险假设，这辆车的平均速度肯定很高，需要计算平均车速
        var sum:Double = 0.0
        var count = list.size
        var sb = new StringBuilder
        sb.append(s"当前车辆${list(0).car},涉嫌危险驾驶，它在两分钟内经过的卡口数是:${list.size}")
        for (i<- 0 until list.size){
          var info  =list(i)
          sum +=info.realSpeed
          sb.append(s"第${i+1}个卡口${info.monitorId},车速为:${info.realSpeed},该卡口的限速为:${info.limitSpeed}--->")
        }
        var avg:Double = (sum/count).formatted("%.2f").toDouble
        sb.append(s" 它在两分钟内的平均车速为:${avg}")
        new ViolationInfo(list(0).car,sb.toString(),System.currentTimeMillis(),list.size)
      }
    )
      .addSink(new JdbcWriterDataSink[ViolationInfo](classOf[ViolationInfo]))



    streamEnv.execute()

  }

  class OutOfSpeedFunction(baseLimitSpeed:Int) extends RichMapFunction[TrafficLog,OutOfLimitSpeedInfo]{
    var map =scala.collection.mutable.Map[String,MonitorLimitInfo]() //map只用于判断是否超速的临时变量

    override def map(value: TrafficLog): OutOfLimitSpeedInfo = {
      //如果集合中有当前卡口的限速，判断是否超速20%，如果集合中没有当前卡口的限速，城市道路中有一个基本限速80
      val info: MonitorLimitInfo = map.getOrElse(value.monitorId,new MonitorLimitInfo(value.monitorId,value.roadId,baseLimitSpeed,value.areaId))
      new OutOfLimitSpeedInfo(value.car,value.monitorId,value.roadId,value.speed,info.speedLimit,value.actionTime)
    }

    override def open(parameters: Configuration): Unit = {
      //把数据库所有的限速信息读取到Map集合中
      var conn =DriverManager.getConnection("jdbc:mysql://localhost/traffic_monitor","root","123123")
      var pst = conn.prepareStatement("select * from t_monitor_info where speed_limit>0")
      var ret = pst.executeQuery()
      while (ret.next()){
        var info = new MonitorLimitInfo(ret.getString(1),ret.getString(2),ret.getInt(3),ret.getString(4))
        map.put(info.monitorId,info)
      }
      ret.close()
      pst.close()
      conn.close()
    }
  }
}
