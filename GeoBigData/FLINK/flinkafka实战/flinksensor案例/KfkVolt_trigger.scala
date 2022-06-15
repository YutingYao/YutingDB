package test

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.triggers.{Trigger, TriggerResult}
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer011, FlinkKafkaProducer011}
import org.json4s.NoTypeHints
import org.json4s.jackson.Serialization
import test.KfkVolt.Message

import java.util.Properties

/**
 * ./bin/flink run -m yarn-cluster  -yjm 1024 -ytm 1024 -c com.bainan.test.KfkVolt /home/bigdata/flinkApp/flinkAlert/flink-alert-1.0-SNAPSHOT-jar-with-dependencies.jar
 * @author Max
 */
object KfkVolt {
  implicit val formats = Serialization.formats(NoTypeHints)

  case class Message(time : String , name : String , value : Double)
  case class ResultJson(info : String, name : String, value: Double, warningTime : String)

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val kafkaBrokers = "222.25.172.64:9092,n001:9092,n002:9092,n003:9092"
    val consumerGroup = "readData"
    val listenerTopic = "DataGenerationTopic"
    val voltListenerTopic = "VoltDataTopic"
    val currListenerTopic = "CurrDataTopic"
    val tempListenerTopic = "TempDataTopic"
    val amplListenerTopic = "AmplDataTopic"
    val frepListenerTopic = "FreqDataTopic"
    val targetTopic = "caow02"
    //检测阈值
    val thresholdValue = 5.00

    val properties = new Properties()
    properties.setProperty("bootstrap.servers", kafkaBrokers)
    properties.setProperty("group.id", consumerGroup)

    // Source
    val consumer = new FlinkKafkaConsumer011[String](listenerTopic, new SimpleStringSchema(), properties)
    val voltConsumer = new FlinkKafkaConsumer011[String](voltListenerTopic, new SimpleStringSchema(), properties)
    val currConsumer = new FlinkKafkaConsumer011[String](currListenerTopic, new SimpleStringSchema(), properties)
    val tempConsumer = new FlinkKafkaConsumer011[String](tempListenerTopic, new SimpleStringSchema(), properties)
    val amplConsumer = new FlinkKafkaConsumer011[String](amplListenerTopic, new SimpleStringSchema(), properties)
    val freqConsumer = new FlinkKafkaConsumer011[String](frepListenerTopic, new SimpleStringSchema(), properties)
    //sink
    val sink = new FlinkKafkaProducer011[String](kafkaBrokers, targetTopic, new SimpleStringSchema())
    val dataStream = env.addSource(consumer)
    val name = "volt"

    //分割stream 转换成 Message 格式Stream
    val messageStream = dataStream.map(data =>{
      val dataArray = data.split("\\+")
      Message(dataArray(0), dataArray(1), dataArray(2).toDouble)
    })

    //分流，进行判断后分流
    val splitStream = messageStream.split( data =>{
      if(data.name == "volt" )
        Seq("voltStream")
      else
        Seq("othersStream")
    })

    //分离出来的流
    //    val othersStream = splitStream.select("othersStream")
    val voltStream = splitStream.select("voltStream")

    //开窗
    val alertStream = voltStream
      .keyBy(s => s.name)
      .timeWindow(Time.seconds(30))  //定义一个30秒的翻滚窗口
      .trigger(new MyTrigger(thresholdValue))  //检测阈值为5
      .reduce((_, y) => Message(y.time, y.name, y.value)) //聚合 ,value为差值

    alertStream.print()

    //变成JsonString流
    val jsonStrStream = alertStream
      .map(data => ResultJson("警告！"+data.name+"值跳变过大，超过阈值！", data.name, data.value, data.time)) //修改格式
      .map(data =>Serialization.write(data))
    jsonStrStream.print()
    jsonStrStream.addSink(sink)


    val processName = "kafka_read_" + name
    env.execute(processName)
  }

  /**
   * 逻辑代码函数
   * @param name
   * @param consumer source
   * @param env 环境
   * @param thresholdValue 检测临界值变量
   * @param sink  sink
   * @author Max
   */
  def dataStreamProcessor(name : String,
                          consumer : FlinkKafkaConsumer011[String],
                          env :  StreamExecutionEnvironment,
                          thresholdValue : Double,
                          sink : FlinkKafkaProducer011[String]) : Unit= {

  }

}


/**
 * 自定义时间窗口触发器
 * dataStream中数值存在max和min中
 * onProcessingTime变化超过 thresholdValue 触发
 * @author Max
 */
class MyTrigger(thresholdValue : Double) extends Trigger[Message, TimeWindow] {

  //需要保持状态，传入一个数值状态Descriptor
  val maxStateDesc = new ValueStateDescriptor[Double]("maxValue", classOf[Double])
  val minStateDesc = new ValueStateDescriptor[Double]("minValue", classOf[Double])

  //当某窗口增加一个元素时调用onElement方法，返回一个TriggerResult
  override def onElement(element: Message,
                         time: Long,
                         window: TimeWindow,
                         triggerContext: Trigger.TriggerContext): TriggerResult = {
    println("onElement start")
    //保存数值状态
    val maxValueState: ValueState[Double] = triggerContext.getPartitionedState(maxStateDesc)
    val minValueState: ValueState[Double] = triggerContext.getPartitionedState(minStateDesc)
    //给最小记录值一个初始大数
    if(Option(minValueState.value()).isEmpty){
      minValueState.update(thresholdValue)
    }
    //跟新最大和最小值记录
    if(element.value > maxValueState.value()){
      maxValueState.update(element.value)
    }else if(element.value < minValueState.value()){
      minValueState.update(element.value)
    }
    println("max "+maxValueState.value())
    println("min "+minValueState.value())
    println()

    //只做记录，全部continue
    TriggerResult.CONTINUE
  }

  // 我们不用EventTime，直接返回一个CONTINUE
  override def onEventTime(time: Long, window: TimeWindow, triggerContext: Trigger.TriggerContext): TriggerResult = {
    TriggerResult.CONTINUE
  }

  override def onProcessingTime(time: Long, window: TimeWindow, triggerContext: Trigger.TriggerContext): TriggerResult = {
    //查看数值状态
    val maxValueState: ValueState[Double] = triggerContext.getPartitionedState(maxStateDesc)
    val minValueState: ValueState[Double] = triggerContext.getPartitionedState(minStateDesc)
    if((maxValueState.value() - minValueState.value()) > thresholdValue){
      println("onProcessingTime start " + (maxValueState.value() - minValueState.value()))
      println()
      return TriggerResult.FIRE_AND_PURGE
    }
    TriggerResult.PURGE
  }

  override def clear(window: TimeWindow, triggerContext: Trigger.TriggerContext): Unit = {
    println("clear start")
    val maxValueState: ValueState[Double] = triggerContext.getPartitionedState(maxStateDesc)
    val minValueState: ValueState[Double] = triggerContext.getPartitionedState(minStateDesc)
    maxValueState.clear()
    minValueState.clear()
  }
}

