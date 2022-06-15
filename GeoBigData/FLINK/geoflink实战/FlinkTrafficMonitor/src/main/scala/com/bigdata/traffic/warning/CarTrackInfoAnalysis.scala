package com.bigdata.traffic.warning

import java.util
import java.util.Properties

import com.bigdata.traffic.util.{GlobalConstant, HBaseWriterDataSink, JdbcReaderSource, MonitorLimitInfo, TrackInfo, TrafficLog, ViolationInfo}
import org.apache.flink.streaming.api.datastream.BroadcastStream
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow
import org.apache.flink.util.Collector
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.util.Bytes
import org.apache.kafka.common.serialization.StringDeserializer

object CarTrackInfoAnalysis {

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

    //广播状态流 (数据量少，更新频率不高)
    //1、读取一个Source得到一个流（DataStream）
    //2、通过JobManager把流广播到所有的TaskManager，（存放在状态）
    //3、调用connect算子和主流中的数据连接计算

    val broadStream: BroadcastStream[ViolationInfo] = streamEnv.addSource(new JdbcReaderSource[ViolationInfo](classOf[ViolationInfo]))
      .broadcast(GlobalConstant.VIOLATION_STATE_DESCRIPTOR)
    //状态中的数据结构： ValueState,ListState,MapState

    //    Thread.sleep(2000)

    //Kafka的Source
    //    val mainStream: DataStream[TrafficLog] = streamEnv.addSource(new FlinkKafkaConsumer[String]("t0601_traffic", new SimpleStringSchema(), props).setStartFromEarliest())
    val mainStream: DataStream[TrafficLog] = streamEnv.readTextFile(getClass.getResource("/traffic_data").getPath)
      .map(line => {
        val arr: Array[String] = line.split(",")
        TrafficLog(arr(0).toLong, arr(1), arr(2), arr(3), arr(4).toDouble, arr(5), arr(6))
      })

    mainStream.connect(broadStream)
      .process(new BroadcastProcessFunction[TrafficLog,ViolationInfo,TrackInfo] {
        override def processElement(value: TrafficLog, ctx: BroadcastProcessFunction[TrafficLog, ViolationInfo, TrackInfo]#ReadOnlyContext, out: Collector[TrackInfo]) = {
          val info: ViolationInfo = ctx.getBroadcastState(GlobalConstant.VIOLATION_STATE_DESCRIPTOR).get(value.car)
          if(info!=null){
            out.collect(new TrackInfo(value.car,value.actionTime,value.monitorId,value.roadId,value.areaId,value.speed))
          }
        }

        override def processBroadcastElement(value: ViolationInfo, ctx: BroadcastProcessFunction[TrafficLog, ViolationInfo, TrackInfo]#Context, out: Collector[TrackInfo]) = {
          //把广播流的数据存入状态中
          ctx.getBroadcastState(GlobalConstant.VIOLATION_STATE_DESCRIPTOR).put(value.car,value)
        }
      })

    //把数据写入Hbase，使用批量写入，每次写入20，在Flink存在一个countWindow（只要得到20条数据开启一个window）
        .countWindowAll(20)
        .apply(
          (win:GlobalWindow,input:Iterable[TrackInfo],out:Collector[java.util.List[Put]]) =>{
            val list = new util.ArrayList[Put]()
            for(info<- input){
              val put = new Put( Bytes.toBytes( info.car+"_"+ (Long.MaxValue - info.actionTime )))
              put.add("cf1".getBytes,"actionTime".getBytes,Bytes.toBytes(info.actionTime))
              put.add("cf1".getBytes,"monitorId".getBytes,Bytes.toBytes(info.monitorId))
              put.add("cf1".getBytes,"roadId".getBytes,Bytes.toBytes(info.roadId))
              put.add("cf1".getBytes,"areaId".getBytes,Bytes.toBytes(info.areaId))
              put.add("cf1".getBytes,"speed".getBytes,Bytes.toBytes(info.speed))
              put.add("cf1".getBytes,"car".getBytes,Bytes.toBytes(info.car))
              list.add(put)
            }
            println(list.size())
            out.collect(list)
    }
    )

      .addSink(new HBaseWriterDataSink)
//      .print()
    streamEnv.execute()
  }
}
