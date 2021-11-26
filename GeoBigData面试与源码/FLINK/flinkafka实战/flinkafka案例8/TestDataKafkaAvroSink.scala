package com.machinedoll.experiment.sink

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer

object TestDataKafkaAvroSink {
  val properties = new Properties
  properties.setProperty("bootstrap.servers", "10.8.3.102:6667")

  val simpleStringProducer =
    new FlinkKafkaProducer[String]("test", new SimpleStringSchema(), properties)

  //  def getKafkaAvroSink(topic: String): FlinkKafkaProducer[TestData] = {
  //
  //    val props = new Properties()
  //    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.108:9092")
  //    //    props.put(ProducerConfig.ACKS_CONFIG, "all")
  //    //    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer])
  //    //    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer])
  //    //    props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://192.168.0.108:8081")
  //    props.setProperty("group.id", "example-group")
  //
  //    //    public FlinkKafkaProducer (brokerList: String, topicId: String, serializationSchema: KeyedSerializationSchema[IN]) {
  //    //    new FlinkKafkaProducer("")
  //    //    public FlinkKafkaProducer (brokerList: String, topicId: String, serializationSchema: SerializationSchema[IN]) {
  //
  //    new FlinkKafkaProducer[TestData]("192.168.0.108:9092", "topic", new SerializationSchema[TestData] {
  //      override def serialize(t: TestData): Array[Byte] = {
  //        val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
  //        val oos = new ObjectOutputStream(stream)
  //        oos.writeObject(t)
  //        oos.close()
  //        stream.toByteArray
  //      }
  //    })
  //
  //
  //  }
}
