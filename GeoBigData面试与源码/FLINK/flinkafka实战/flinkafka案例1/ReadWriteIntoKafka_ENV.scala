package flink

import config.ObservationProcessConfig
import function.ObservationProcessFunction
import org.apache.flink.streaming.api.datastream.{DataStreamSource, SingleOutputStreamOperator}
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment

object ReadWriteIntoKafka extends App {
  process()

  def process(): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //--topic quickstart-events --bootstrap.servers localhost:9092 --zookeeper.connect localhost:2181 --group.id myGroup

    //Read the data from the given topic
    val inputStream: DataStreamSource[String] = env.addSource(ObservationProcessConfig.flinkKafkaConsumer)

    //Create different streams for log events and other events
    val outputStream: SingleOutputStreamOperator[String] = inputStream.process(new ObservationProcessFunction())

    // write data into 2 different topics based on the overridden processElement() of ProcessFunction
    outputStream.getSideOutput(ObservationProcessConfig.eventsOutputTag).addSink(ObservationProcessConfig.flinkKafkaOutputSink)

    outputStream.getSideOutput(ObservationProcessConfig.eventsLogTag).addSink(ObservationProcessConfig.flinkKafkaLogSink)

    env.execute()
  }
}

