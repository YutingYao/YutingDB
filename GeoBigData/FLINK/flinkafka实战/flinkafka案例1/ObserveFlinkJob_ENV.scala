package flink

import config.ObservationProcessConfig
import function.ObservationFunction
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.datastream.{DataStreamSource, SingleOutputStreamOperator}
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}

import java.util.Properties

object ObserveFlinkJob extends App {
  val env = StreamExecutionEnvironment.getExecutionEnvironment
  //--topic quickstart-events --bootstrap.servers localhost:9092 --zookeeper.connect localhost:2181 --group.id myGroup
  val properties = new Properties
  properties.setProperty("bootstrap.servers", "localhost:9092")
  properties.setProperty("zookeeper.connect", "localhost:2181")
  properties.setProperty("group.id", "myObsGroup")

  //Write the data to the input topic before read/write into different topics
  val ipStream: DataStreamSource[String] = env.readTextFile("src/main/resources/obs-input")
  ipStream.addSink(
    new FlinkKafkaProducer[String]("observation-input", new SimpleStringSchema,properties))

  val inputStream: DataStreamSource[String] = env.addSource(
    new FlinkKafkaConsumer[String]("observation-input", new SimpleStringSchema,properties))

  val outputStream: SingleOutputStreamOperator[String] = inputStream.process(new ObservationFunction())

  // write data into different topics of Kafka
  outputStream.getSideOutput(ObservationProcessConfig.eventsOutputTag).
    addSink(new FlinkKafkaProducer[String]("observation-output",new SimpleStringSchema,properties))

  outputStream.getSideOutput(ObservationProcessConfig.eventsLogTag).
    addSink(new FlinkKafkaProducer[String]("observation-log",new SimpleStringSchema,properties))

  env.execute()
}


