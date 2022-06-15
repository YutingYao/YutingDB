package com.bigdata.traffic.warning

import java.util.Properties

import com.bigdata.traffic.util.{JdbcWriterDataSink, RepetitionCarWarningInfo, TrafficLog}
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector
import org.apache.kafka.common.serialization.StringDeserializer

/**
 * 套牌车分析
 */
object RepetitionCarAnalysis {

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
      }).assignAscendingTimestamps(_.actionTime) //

    mainStream.keyBy(_.car)
      .process(new KeyedProcessFunction[String,TrafficLog,RepetitionCarWarningInfo] {
        lazy val firstState: ValueState[TrafficLog] = getRuntimeContext.getState(new ValueStateDescriptor[TrafficLog]("first",classOf[TrafficLog]))

        override def processElement(value: TrafficLog, ctx: KeyedProcessFunction[String, TrafficLog, RepetitionCarWarningInfo]#Context, out: Collector[RepetitionCarWarningInfo]) = {
          val first = firstState.value()
          if(first==null){ //表示当前数据就是第一次经过卡口的数据
            firstState.update(value)
          }else{ //表示该车辆已经在某个卡口中出现过了，需要判断一个时间差
            var secondTime =value.actionTime
            var firstTime =first.actionTime
            var less:Long = (secondTime-firstTime).abs  /1000
            if(less<10){ //涉嫌套牌车
              val info = new RepetitionCarWarningInfo(value.car,first.monitorId,value.monitorId,ctx.timerService().currentProcessingTime(),"涉嫌套牌车")
              out.collect(info)
              firstState.clear()
            }else{ //暂时不是套牌车，但是还需要后面的数据判断
              firstState.update( if (secondTime>firstTime) value else first )
            }
          }
        }
      })
      .addSink(new JdbcWriterDataSink[RepetitionCarWarningInfo](classOf[RepetitionCarWarningInfo]))

    streamEnv.execute()

  }

}
