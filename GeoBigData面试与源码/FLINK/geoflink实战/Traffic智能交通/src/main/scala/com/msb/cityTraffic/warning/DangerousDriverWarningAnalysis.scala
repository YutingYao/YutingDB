package com.msb.cityTraffic.warning

/**
 * ClassName DangerousDriverWarningAnalysis
 * Description
 * Create by liudz
 * Date 2020/9/14 3:03 下午
 */
import java.sql.DriverManager
import java.util.Properties

import com.msb.cityTraffic.utils.{DangerousDrivingWarning, MonitorInfo, OutOfLimitSpeedInfo, TrafficInfo}
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.cep.scala.{CEP, PatternStream}
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time

import scala.collection.Map

object DangerousDriverWarningAnalysis {

  def main(args: Array[String]): Unit = {
    val streamEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.streaming.api.scala._

    streamEnv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    streamEnv.setParallelism(1)

    val props = new Properties()
    props.setProperty("bootstrap.servers","hadoop101:9092,hadoop102:9092,hadoop103:9092")
    props.setProperty("group.id","msb_001")

    //创建一个Kafka的Source
    //    val stream: DataStream[TrafficInfo] = streamEnv.addSource(
    //      new FlinkKafkaConsumer[String]("t_traffic_msb", new SimpleStringSchema(), props).setStartFromEarliest() //从第一行开始读取数据
    //    )
    val stream: DataStream[OutOfLimitSpeedInfo] = streamEnv.socketTextStream("hadoop101",9999)
      .map(line => {
        var arr = line.split(",")
        new TrafficInfo(arr(0).toLong, arr(1), arr(2), arr(3), arr(4).toDouble, arr(5), arr(6))
      }) //引入事件时间,Watermark ,数据迟到5秒
      .map(new MyRichMapFunction(60)) //把原始的数据，变成一个超速对象
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[OutOfLimitSpeedInfo](Time.seconds(5)) {
        override def extractTimestamp(t: OutOfLimitSpeedInfo) = t.actionTime
      })

    var pattern = Pattern.begin[OutOfLimitSpeedInfo]("begin")
      .where(t=>{
        t.limitSpeed*1.2 < t.realSpeed  //超速20%
      }).timesOrMore(3) //超速3次或者3次以上
      //      .followedBy("second")
      //      .where(t=>{
      //        t.limitSpeed*1.2 < t.realSpeed  //超速20%
      //      })
      //      .followedBy("third")
      //      .where(t=>{
      //        t.limitSpeed*1.2 < t.realSpeed  //超速20%
      //      })
      .greedy
      .within(Time.minutes(2)) //定义时间范围是2分钟内

    val ps: PatternStream[OutOfLimitSpeedInfo] = CEP.pattern(stream.keyBy(_.car),pattern)
    ps.select( map=>{//map只有一条
      val list: List[OutOfLimitSpeedInfo] = map.get("begin").get.toList
      var sb = new StringBuilder()
      sb.append("该车辆涉嫌危险驾驶,")
      var i =1
      for(info <- list){
        sb.append(s"第${i}个经过卡口是:${info.monitorId} -->")
        i+=1
      }
      new DangerousDrivingWarning(list(0).car,sb.toString(),System.currentTimeMillis(),0)
    })
      .print()

    streamEnv.execute()

  }

  class MyRichMapFunction(defaultLimit:Int) extends RichMapFunction[TrafficInfo,OutOfLimitSpeedInfo]{
    var map  = scala.collection.mutable.Map[String,MonitorInfo]()
    //一次性从数据库中读取所有卡口的限速
    override def open(parameters: Configuration): Unit = {
      var conn = DriverManager.getConnection("jdbc:mysql://localhost/traffic_monitor","root","123123")
      var pst = conn.prepareStatement("select monitor_id,road_id,speed_limit,area_id from t_monitor_info where speed_limit > 0")
      var set =pst.executeQuery()
      while (set.next()){
        var info =new MonitorInfo(set.getString(1),set.getString(2),set.getInt(3),set.getString(4))
        map.put(info.monitorId,info)
      }
      set.close()
      pst.close()
      conn.close()
    }

    override def map(in: TrafficInfo): OutOfLimitSpeedInfo = {
      //首先从Map集合中判断是否存在卡口的限速，如果不存在，默认限速为60
      val  info = map.getOrElse(in.monitorId,new MonitorInfo(in.monitorId,in.roadId,defaultLimit,in.areaId))
      new OutOfLimitSpeedInfo(in.car,in.monitorId,in.roadId,in.speed,info.limitSpeed,in.actionTime)
    }
  }
}
