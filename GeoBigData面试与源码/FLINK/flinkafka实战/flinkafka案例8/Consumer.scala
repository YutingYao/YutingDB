package com.machinedoll.experiment

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

object Consumer {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val properties = new Properties
    properties.setProperty("bootstrap.servers", "10.8.3.102:6667")
    properties.setProperty("group.id", "test")

    val kafkaSource = new FlinkKafkaConsumer[String]("test", new SimpleStringSchema(), properties)
//    kafkaSource.setStartFromEarliest()

    env.addSource(kafkaSource)
      .map((message) => {
        message.toLowerCase
      })
      .print()

    env.execute("Demo Consumer: Load Schema From External Schema Register and Send to Kafka")
  }
}
