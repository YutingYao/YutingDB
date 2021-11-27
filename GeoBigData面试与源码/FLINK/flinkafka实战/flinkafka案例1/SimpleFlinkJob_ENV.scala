package flink

import config.ObservationProcessConfig
import function.{ObservationProcessFunctionConfig, SimpleFunction}
import org.apache.flink.streaming.api.datastream.{DataStreamSource, SingleOutputStreamOperator}
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment

class SimpleFlinkJob extends App {
process()

  def process(): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //--topic quickstart-events --bootstrap.servers localhost:9092 --zookeeper.connect localhost:2181 --group.id myGrou

    //Write the data to the input topic before read/write into different topics
    //For testing the batch events only
//    val ipStream: DataStreamSource[String] = env.readTextFile("src/main/resources/obs-input")
//    ipStream.addSink(
//      new FlinkKafkaProducer[String]("observation-input", new SimpleStringSchema, ObservationProcessConfig.properties))

    val inputStream: DataStreamSource[String] = env.addSource(ObservationProcessConfig.flinkKafkaConsumer)
     // new FlinkKafkaConsumer[String]("observation-input", new SimpleStringSchema, properties))

    val outputStream: SingleOutputStreamOperator[String] = inputStream.process(new SimpleFunction())

    // write data to 2 different topics based on the overridden processElement() of ProcessFunction
    outputStream.getSideOutput(ObservationProcessFunctionConfig.eventsOutputTag).addSink(ObservationProcessConfig.flinkKafkaOutputSink)
    outputStream.getSideOutput(ObservationProcessFunctionConfig.eventsLogTag).addSink(ObservationProcessConfig.flinkKafkaLogSink)

    env.execute()

  }

}
