package com.msb.cityTraffic.warning

/**
 * ClassName RepetitionCarAnalysis
 * Description
 * Create by liudz
 * Date 2020/9/14 3:03 下午
 */
import java.util.Properties

import com.msb.cityTraffic.utils.{RepetitionCarWarning, TrafficInfo, WriteDataSink}
import org.apache.flink.api.common.state.ValueStateDescriptor
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

/**
 * 套牌车分析，从数据流中找出存在套牌嫌疑的车辆
 */
object RepetitionCarAnalysis {

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
    val stream: DataStream[TrafficInfo] = streamEnv.socketTextStream("hadoop101",9999)
      .map(line => {
        var arr = line.split(",")
        new TrafficInfo(arr(0).toLong, arr(1), arr(2), arr(3), arr(4).toDouble, arr(5), arr(6))
      }) //引入时间时间
      .assignAscendingTimestamps(_.actionTime)

    stream.keyBy(_.car) //根据车牌号分
      .process(new KeyedProcessFunction[String,TrafficInfo,RepetitionCarWarning] {
        //状态中保存第一辆出现的信息对象
        lazy val firstState = getRuntimeContext.getState(new ValueStateDescriptor[TrafficInfo]("first",classOf[TrafficInfo]))

        override def processElement(i: TrafficInfo, context: KeyedProcessFunction[String, TrafficInfo, RepetitionCarWarning]#Context, collector: Collector[RepetitionCarWarning]) = {
          val first :TrafficInfo =firstState.value()
          if(first==null){ //当前这量车就是第一辆车
            firstState.update(i)
          }else{ //有两辆车出现了，但是时间已经超过了10秒，另外一种情况是时间没有超过10秒(涉嫌套牌)
            val nowTime = i.actionTime
            val firstTime = first.actionTime
            var less:Long = (nowTime - firstTime).abs /1000
            if(less<=10){ //涉嫌
              var warn =new RepetitionCarWarning(i.car, if(nowTime>firstTime) first.monitorId else i.monitorId,
                if(nowTime<firstTime) first.monitorId else i.monitorId,
                "涉嫌套牌车",
                context.timerService().currentProcessingTime()
              )
              collector.collect(warn)
              firstState.clear()
            }else{ //不是套牌车，把第二次经过卡口的数据保存到状态中，以便下次判断
              if(nowTime>firstTime) firstState.update(i)
            }
          }
        }
      } )
      .addSink(new WriteDataSink[RepetitionCarWarning](classOf[RepetitionCarWarning]))

    streamEnv.execute()
  }
}
