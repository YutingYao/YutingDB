package flink

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.datastream.DataStreamSource
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

import java.util.Properties

object ReadFromKafka extends App {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //--topic quickstart-events --bootstrap.servers localhost:9092 --zookeeper.connect localhost:2181 --group.id myGroup
    val properties = new Properties
    properties.setProperty("bootstrap.servers", "localhost:9092")
    properties.setProperty("zookeeper.connect", "localhost:2181")
    properties.setProperty("group.id", "myGroup")

    val messageStream: DataStreamSource[String] = env.addSource(
        new FlinkKafkaConsumer[String]("observation-events", new SimpleStringSchema,properties))

    // rebalance will repartitions the data so that all listeners can see the messages
    // Even when kafka clusters were lessThan Flink operators, then all messages will be sent
     messageStream.map(s => "Scala Flink reads from kafka event:" + s).print

    env.execute()
}
