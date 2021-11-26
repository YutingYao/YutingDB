package util

import configurations.FlinkSampleConfiguration
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

class FlinkKafkaConnector(config: FlinkSampleConfiguration) {

  def kafkaProducer(topic: String) :SinkFunction[String] = {
    new FlinkKafkaProducer[String](topic, new SimpleStringSchema(), config.flinkKafkaProperties)
  }

  def kafkaConsumer(topic:String):SourceFunction[String] ={
    new FlinkKafkaConsumer[String](topic, new SimpleStringSchema(), config.flinkKafkaProperties)
  }


}
