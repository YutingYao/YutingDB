package configurations

import com.typesafe.config.Config
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.OutputTag

import java.util.Properties

class FlinkSampleConfiguration(val config: Config ) extends Serializable {

// Kafka topics

  val jsontest:String = config.getString("json.test")
  val jsontest1:String = config.getString("json.test1")
  val jsontest2:String = config.getString("json.test2")

  val sumOutputTag = OutputTag[String]("sum-output")
  val averageOutputTag = OutputTag[String]("average-output")

  val mySumProducer = "my-sum-sink"
  val myAverageProducer = "my-average-sink"

  val flinkKafkaConsumer = "flink-kafka-consumer"

  def flinkKafkaProperties:Properties = {
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "localhost:9092")
    properties.setProperty("zookeeper.connect", "localhost:2181")
    properties.setProperty("group.id", "consumerGroup")
    properties
  }


}
