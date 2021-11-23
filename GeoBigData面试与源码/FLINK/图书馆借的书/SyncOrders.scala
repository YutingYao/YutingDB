package com.kkb.flinkhouse.syncdatas

import java.util.Properties

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.environment.CheckpointConfig
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011


object SyncOrders {

  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //隐式转换
    import org.apache.flink.api.scala._
    //checkpoint配置
    environment.enableCheckpointing(100)
    environment.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
    environment.getCheckpointConfig.setMinPauseBetweenCheckpoints(500)
    environment.getCheckpointConfig.setCheckpointTimeout(60000)
    environment.getCheckpointConfig.setMaxConcurrentCheckpoints(1)
    environment.getCheckpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)
    environment.setStateBackend(new RocksDBStateBackend("hdfs://node01:8020/flink_kafka/checkpoints", true))


    val props = new Properties
    props.put("bootstrap.servers", "node01:9092")
    props.put("zookeeper.connect", "node01:2181")
    props.put("group.id", "flinkHouseGroupxxx")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("auto.offset.reset", "latest")
    props.put("flink.partition-discovery.interval-millis", "30000")


    val kafkaSource = new FlinkKafkaConsumer011[String]("flink_house",new SimpleStringSchema(),props)

    kafkaSource.setCommitOffsetsOnCheckpoints(true)


    val result: DataStream[String] = environment.addSource(kafkaSource)

    val orderStream: DataStream[OrderObj] = result.map(x => {
      val jsonObject: JSONObject = JSON.parseObject(x)
      val database: String = jsonObject.get("database").toString
      val table: String = jsonObject.get("table").toString
      val typeStr: String = jsonObject.get("type").toString
      val data: String = jsonObject.get("data").toString
      OrderObj(database, table, typeStr, data)
    })
    //将我们的数据插入到hbase里面去
    orderStream.addSink(new HBaseSinkFunction)
    environment.execute()
  }

}



case class OrderObj(database:String,table:String,`type`:String,data:String) extends Serializable