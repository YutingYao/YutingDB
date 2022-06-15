package com.bigdata.traffic.distribution

import java.util.Properties

import com.bigdata.traffic.util.{GlobalConstant, JdbcReaderSource, MonitorLimitInfo, TrafficLog}
import org.apache.flink.streaming.api.datastream.BroadcastStream
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.apache.flink.util.Collector
import org.apache.kafka.common.serialization.StringDeserializer

import scala.collection.mutable

object AreaDistributionAnalysis {

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

    //Kafka的Source
//    val mainStream: DataStream[TrafficLog] = streamEnv.addSource(new FlinkKafkaConsumer[String]("t0601_traffic", new SimpleStringSchema(), props).setStartFromEarliest())
    val mainStream: DataStream[TrafficLog] = streamEnv.readTextFile(getClass.getResource("/new_data").getPath)
      .map(line => {
        val arr: Array[String] = line.split(",")
        TrafficLog(arr(0).toLong, arr(1), arr(2), arr(3), arr(4).toDouble, arr(5), arr(6))
      })
    mainStream.print()

    mainStream.keyBy(_.areaId)
      .timeWindow(Time.seconds(1))
      //Set集合去重必须采用全量窗口函数
      .apply(
        (key:String, win:TimeWindow, input:Iterable[TrafficLog], out: Collector[String]) =>{
          val set: mutable.Set[String] = scala.collection.mutable.Set() //车牌号码的去重
          for(i<-input){
            set+=i.car
          }
          out.collect(s"区域id:${key},时间范围是,起始时间:${win.getStart}----结束时间:${win.getEnd},一共上路的车辆为${set.size}")
        }
      )
      .print()

    streamEnv.execute()

  }
}
