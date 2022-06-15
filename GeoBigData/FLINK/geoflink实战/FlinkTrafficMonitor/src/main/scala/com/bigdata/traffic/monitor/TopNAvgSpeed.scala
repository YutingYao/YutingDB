package com.bigdata.traffic.monitor

import java.lang
import java.util.Properties

import com.bigdata.traffic.util._
import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala.function.AllWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.apache.kafka.common.serialization.StringDeserializer

object TopNAvgSpeed {

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
      }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[TrafficLog](Time.seconds(5)) {
        override def extractTimestamp(element: TrafficLog) = element.actionTime
      })


    val stream: DataStream[AvgSpeedInfo] = mainStream.keyBy(_.monitorId) //统计每个卡口的平均车速
      .timeWindow(Time.minutes(5), Time.minutes(1))
      //首先需要统计每个卡口经过车辆的数量，和统计这些车的车速之和，可以使用增量函数
      .aggregate( //累加器类型为二元组(累加车速之和，累加车辆数量)
        new AggregateFunction[TrafficLog, (Double, Long), (Double, Long)] {
          override def createAccumulator() = (0.0, 0)

          override def add(value: TrafficLog, acc: (Double, Long)) = (acc._1 + value.speed, acc._2 + 1)

          override def getResult(acc: (Double, Long)) = acc

          override def merge(a: (Double, Long), b: (Double, Long)) = (a._1 + b._1, a._2 + b._2)
        }, //第二个是全量函数 ,计算平均车速
        (key: String, win: TimeWindow, input: Iterable[(Double, Long)], out: Collector[AvgSpeedInfo]) => { //input
          val t: (Double, Long) = input.last
          val avg: Double = (t._1 / t._2).formatted("%.2f").toDouble
          out.collect(new AvgSpeedInfo(win.getStart, win.getEnd, key, avg, t._2.toInt))
        }
      )


    stream.assignAscendingTimestamps(_.end) //数据是有序的，只要设定EventTime
      .timeWindowAll(Time.minutes(1))
      .apply(new AllWindowFunction[AvgSpeedInfo,String,TimeWindow] {
        //input里面有n条
        override def apply(window: TimeWindow, input: Iterable[AvgSpeedInfo], out: Collector[String]): Unit = {
          //存放所有卡口的平均车速
          var map :scala.collection.mutable.Map[String,AvgSpeedInfo] = scala.collection.mutable.Map[String,AvgSpeedInfo]()
          for (one<-input){ //如果前一个window中存在Watermark和AllowedLateness，造成同一个卡口有多个平均车速
            if(map.contains(one.monitorId)){ //map中已经存在有当前卡口的平均车速，留下车辆最大
              if(one.avgSpeed> map.get(one.monitorId).get.avgSpeed){
                map.put(one.monitorId,one)
              }
            }else{
              map.put(one.monitorId,one)
            }
          }
          //map 集合中没有重复的数据,开始排序
          val list: List[AvgSpeedInfo] = map.values.toList.sortBy(_.avgSpeed)(Ordering.Double.reverse).take(3)
          val sb = new StringBuilder()
          sb.append(s"在时间从${list(0).start}, 到:${list(0).end}的范围内：")
          sb.append("整个城市最通畅的前三个卡口是:")
          list.foreach(info=>{
            sb.append(s"卡口编号：${info.monitorId},平均车速是:${info.avgSpeed},经过车辆的数量:${info.carCount}")
          })
          sb.append("\n")
          out.collect(sb.toString())
        }
      })





//        .addSink(new JdbcWriterDataSink[AvgSpeedInfo](classOf[AvgSpeedInfo]))
        .print()

    streamEnv.execute()

  }
}
