package com.kkb.flink.stream.demo8

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011

object FlinkKafkaSourceObj {
  def main(args: Array[String]): Unit = {
    //获取程序的入口类以及隐式转换包
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment


    import org.apache.flink.streaming.api.CheckpointingMode
    import org.apache.flink.streaming.api.environment.CheckpointConfig
    //checkpoint配置
    environment.enableCheckpointing(100)
    environment.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
    environment.getCheckpointConfig.setMinPauseBetweenCheckpoints(500)
    environment.getCheckpointConfig.setCheckpointTimeout(60000)
    environment.getCheckpointConfig.setMaxConcurrentCheckpoints(1)
    environment.getCheckpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)

    //2、将checkPoint保存到文件系统  将数据保存到文件系统里面去
    //environment.setStateBackend(new FsStateBackend("hdfs://node01:8020/flink_state_save"))

    //3、将数据情况保存到RocksDB 里面去
    environment.setStateBackend(new RocksDBStateBackend("hdfs://node01:8020/flink_save_checkPoint/checkDir",true))
    import org.apache.flink.api.scala._
    val  kafaTopic:String = "test"

    val prop = new Properties()
    prop.setProperty("bootstrap.servers", "node01:9092")
    prop.setProperty("group.id", "con1")
    prop.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    prop.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")


    //创建kafka的source
    val kafkaSource = new FlinkKafkaConsumer011[String](kafaTopic,new SimpleStringSchema(),prop)

    //获取到了kafka里面的数据
    val sourceStream: DataStream[String] = environment.addSource(kafkaSource)
    sourceStream.print()

    environment.execute()





  }


}
