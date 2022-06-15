package com.kkb.flink.stream.demo8

import java.util.Properties

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011
import org.apache.flink.streaming.connectors.kafka.internals.KeyedSerializationSchemaWrapper
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

object FlinkKafkaSinkObj {
  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment


    import org.apache.flink.contrib.streaming.state.RocksDBStateBackend
    import org.apache.flink.streaming.api.CheckpointingMode
    import org.apache.flink.streaming.api.environment.CheckpointConfig
    //checkpoint配置
    environment.enableCheckpointing(5000)
    environment.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
    environment.getCheckpointConfig.setMinPauseBetweenCheckpoints(500)
    environment.getCheckpointConfig.setCheckpointTimeout(60000)
    environment.getCheckpointConfig.setMaxConcurrentCheckpoints(1)
    environment.getCheckpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)
    //设置statebackend
    environment.setStateBackend(new RocksDBStateBackend("hdfs://node01:8020/flink_kafka_sink/checkpoints", true))

    import org.apache.flink.api.scala._

    val sourceStream: DataStream[String] = environment.socketTextStream("node01",9000)

    /**
      * topicId: String, serializationSchema: SerializationSchema[IN], producerConfig: Properties
      */
    val prop = new Properties()
    prop.setProperty("bootstrap.servers", "node01:9092")
    prop.setProperty("group.id", "kafka_group1")


    //第一种解决方案，设置FlinkKafkaProducer011里面的事务超时时间
    //设置事务超时时间
    prop.setProperty("transaction.timeout.ms", 60000 * 15 + "")

    val kafkaSink = new FlinkKafkaProducer011[String]("test",new KeyedSerializationSchemaWrapper(new SimpleStringSchema()),prop)


    //将数据写入到kafka里面去
    sourceStream.addSink(kafkaSink)



    environment.execute()



  }


}
