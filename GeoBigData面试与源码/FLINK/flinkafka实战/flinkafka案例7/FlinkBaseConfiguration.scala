/*
package configurations

import com.typesafe.config.Config

import java.util.Properties
class FlinkBaseConfiguration(val config:Config) extends Serializable {



  def kafkaConsumerProperties: Properties = {


    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "localhost:9092")
    properties.setProperty("zookeeper.connect", "localhost:2181")
    properties.setProperty("group.id", "consumerGroup")
    properties
}
  def kafkaProducerProperties: Properties = {

    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "localhost:9092")
    properties.setProperty("zookeeper.connect", "localhost:2181")
    properties.setProperty("group.id", "consumerGroup")
    properties
  }


}
*/
