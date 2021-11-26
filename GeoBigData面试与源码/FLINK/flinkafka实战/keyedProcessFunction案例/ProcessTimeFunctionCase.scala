package keyedProcessFunctionDemo.demo2

import java.util.Properties

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

/**
 *  以处理时间为例：
 *      需要获取用户2分钟后未浏览商品页面，需要将这个用户捞出，给这些用户推送一些优惠商品。
  *
  * 数据
  * 411,page1,1578466369000    08:11:00
  * 411,page1,1578269580000    08:13:00
  * 411,page1,1578269760000    08:16:00
  * 411,page1,1578271800000    08:50:00
  */
object ProcessTimeFunctionCase {
  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)

    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "localhost:9092")
    properties.setProperty("zookeeper.connect", "localhost:2181")
    properties.setProperty("group.id", "test-group")

    val source: DataStream[String] = env.addSource(new FlinkKafkaConsumer011[String]("test",new SimpleStringSchema(), properties))

    val data = source
                  .map(x=>{
                     val s = x.split(",")
                     view(s(0).toLong, s(1), s(2).toLong)
                  })
                  .keyBy(_.userid)
                  .process(new CountWithTimeoutProcessingTimeFunction())

    data.print()

    env.execute()

  }
}
