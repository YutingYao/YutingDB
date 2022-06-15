package com.msb.cityTraffic.monitor

import java.util.Properties
import com.msb.cityTraffic.utils.{GlobalConstants, JdbcReadDataSource, MonitorInfo, OutOfLimitSpeedInfo, TrafficInfo, WriteDataSink}
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.datastream.BroadcastStream
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.util.Collector

/**
 * ClassName OutOfSpeedMonitorAnalysis
 * Description
 * Create by liudz
 * Date 2020/9/14 2:48 下午
 */


/**
 * 实时车辆超速监控
 */
object OutOfSpeedMonitorAnalysis {

  def main(args: Array[String]): Unit = {
    val streamEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.streaming.api.scala._

    //stream1海量的数据流，不可以存入广播状态流中
    //stream2 从Mysql数据库中读取的卡口限速信息，特点：数据量少，更新不频繁

    val stream2: BroadcastStream[MonitorInfo] = streamEnv.addSource(new JdbcReadDataSource[MonitorInfo](classOf[MonitorInfo]))
      .broadcast(GlobalConstants.MONITOR_STATE_DESCRIPTOR)

    val props = new Properties()
    props.setProperty("bootstrap.servers","hadoop03:9092,hadoop04:9092,hadoop05:9092")
    props.setProperty("group.id","msb_001")


    //创建一个Kafka的Source
    val stream1: DataStream[TrafficInfo] = streamEnv.addSource(
      new FlinkKafkaConsumer[String]("t_traffic_msb", new SimpleStringSchema(), props).setStartFromLatest() //从第一行开始读取数据
    )
      //    val stream1: DataStream[TrafficInfo] = streamEnv.socketTextStream("hadoop101",9999)
      .map(line => {
        var arr = line.split(",")
        new TrafficInfo(arr(0).toLong, arr(1), arr(2), arr(3), arr(4).toDouble, arr(5), arr(6))
      })



    //Flink中有connect ：内和外都可以  和join ：内连接
    stream1.connect(stream2)
      .process(new BroadcastProcessFunction[TrafficInfo,MonitorInfo,OutOfLimitSpeedInfo] {
        override def processElement(value: TrafficInfo, ctx: BroadcastProcessFunction[TrafficInfo, MonitorInfo, OutOfLimitSpeedInfo]#ReadOnlyContext, out: Collector[OutOfLimitSpeedInfo]) = {
          //先从状态中得到当前卡口的限速信息
          val info: MonitorInfo = ctx.getBroadcastState(GlobalConstants.MONITOR_STATE_DESCRIPTOR).get(value.monitorId)
          if(info!=null){ //表示当前这里车，经过的卡口是有限速的
            var limitSpeed= info.limitSpeed
            var realSpeed =value.speed
            if(limitSpeed * 1.1 < realSpeed){ //当前车辆超速通过卡口
              out.collect(new OutOfLimitSpeedInfo(value.car,value.monitorId,value.roadId,realSpeed,limitSpeed,value.actionTime))
            }
          }
        }

        override def processBroadcastElement(value: MonitorInfo, ctx: BroadcastProcessFunction[TrafficInfo, MonitorInfo, OutOfLimitSpeedInfo]#Context, out: Collector[OutOfLimitSpeedInfo]) = {
          //把广播流中的数据保存到状态中
          ctx.getBroadcastState(GlobalConstants.MONITOR_STATE_DESCRIPTOR).put(value.monitorId,value)
        }
      })
      .addSink(new WriteDataSink[OutOfLimitSpeedInfo](classOf[OutOfLimitSpeedInfo]))

    streamEnv.execute()

  }
}

