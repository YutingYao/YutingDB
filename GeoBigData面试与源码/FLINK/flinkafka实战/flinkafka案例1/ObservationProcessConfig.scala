package config

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.java.typeutils.TypeExtractor
import org.apache.flink.streaming.api.scala.OutputTag
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}

import java.util.Properties

object ObservationProcessConfig {
  implicit val eventTypeInfo: TypeInformation[String] = TypeExtractor.getForClass(classOf[String])

  val eventsOutputTag: OutputTag[String] = OutputTag[String]("observe-output")
  val eventsLogTag: OutputTag[String] = OutputTag[String]("observe-log")

  val properties = new Properties
  properties.setProperty("bootstrap.servers", "localhost:9092")
  properties.setProperty("zookeeper.connect", "localhost:2181")
  properties.setProperty("group.id", "myObsGroup")

  val flinkKafkaConsumer = new FlinkKafkaConsumer[String]("observation-input", new SimpleStringSchema, properties)
  val flinkKafkaOutputSink = new FlinkKafkaProducer[String]("observation-output", new SimpleStringSchema, properties)
  val flinkKafkaLogSink = new FlinkKafkaProducer[String]("observation-log", new SimpleStringSchema, properties)

}
