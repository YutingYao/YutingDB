package com.bigdata.traffic.monitor

import java.util.Properties

import com.bigdata.traffic.util.{GlobalConstant, JdbcReaderSource, JdbcWriterDataSink, MonitorLimitInfo, OutOfLimitSpeedInfo, TrafficLog}
import org.apache.flink.api.common.state.BroadcastState
import org.apache.flink.streaming.api.datastream.BroadcastStream
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.util.Collector
import org.apache.kafka.common.serialization.StringDeserializer

object OutOfSpeedMonitor {

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

    val broadStream: BroadcastStream[MonitorLimitInfo] = streamEnv.addSource(new JdbcReaderSource[MonitorLimitInfo](classOf[MonitorLimitInfo]))
      .broadcast(GlobalConstant.MONITOR_LIMIT_STATE_DESCRIPTOR)
      //状态中的数据结构： ValueState,ListState,MapState

//    Thread.sleep(2000)

    //Kafka的Source
//    val mainStream: DataStream[TrafficLog] = streamEnv.addSource(new FlinkKafkaConsumer[String]("t0601_traffic", new SimpleStringSchema(), props).setStartFromEarliest())
    val mainStream: DataStream[TrafficLog] = streamEnv.readTextFile(getClass.getResource("/traffic_data").getPath)
      .map(line => {
        val arr: Array[String] = line.split(",")
        TrafficLog(arr(0).toLong, arr(1), arr(2), arr(3), arr(4).toDouble, arr(5), arr(6))
      })

    mainStream.connect(broadStream) //connect解决类型不一致
      .process(new BroadcastProcessFunction[TrafficLog,MonitorLimitInfo,OutOfLimitSpeedInfo] {
        //处理普通的流
        override def processElement(value: TrafficLog, ctx: BroadcastProcessFunction[TrafficLog, MonitorLimitInfo, OutOfLimitSpeedInfo]#ReadOnlyContext, out: Collector[OutOfLimitSpeedInfo]) = {
          //首先从状态中得到卡口的限速信息
          val info: MonitorLimitInfo = ctx.getBroadcastState(GlobalConstant.MONITOR_LIMIT_STATE_DESCRIPTOR).get(value.monitorId)
          if(info!=null){ //当前车辆经过卡口的时候，该卡口有限速，需要判断是否超速
            var limitSpeed = info.speedLimit
            var realSpeed = value.speed
            if(limitSpeed*1.1 < realSpeed){ //如果超速通过卡口，需要输出一条超速的信息
              out.collect(new OutOfLimitSpeedInfo(value.car,value.monitorId,value.roadId,realSpeed,limitSpeed,value.actionTime))
            }
          }
        }
        //处理广播状态流
        override def processBroadcastElement(value: MonitorLimitInfo, ctx: BroadcastProcessFunction[TrafficLog, MonitorLimitInfo, OutOfLimitSpeedInfo]#Context, out: Collector[OutOfLimitSpeedInfo]) = {
          //把流里面数据保存到状态
          val state: BroadcastState[String, MonitorLimitInfo] = ctx.getBroadcastState(GlobalConstant.MONITOR_LIMIT_STATE_DESCRIPTOR)
          state.put(value.monitorId,value)
        }
      })
      .addSink(new JdbcWriterDataSink[OutOfLimitSpeedInfo](classOf[OutOfLimitSpeedInfo]))
//        .print()
    streamEnv.execute()

  }
}
