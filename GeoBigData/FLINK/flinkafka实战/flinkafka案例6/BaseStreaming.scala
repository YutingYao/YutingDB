package com.binderror.core
import java.nio.charset.StandardCharsets
import java.util.Properties

import com.binderror.core.AppConfiguration.config
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.java.typeutils.TypeExtractor
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer.Semantic
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer, KafkaDeserializationSchema, KafkaSerializationSchema}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.{ProducerConfig, ProducerRecord}

/**
 * Base streaming class for all the jobs. This holds a custom string serializer and de-serializer for Kafka.
 * It also has helper methods for jobs to create kakfa consumers and producers.
 */
abstract class BaseStreaming extends Serializable {

  def createStreamConsumer(kafkaTopic: String, kafkaConsumerGroup: String): FlinkKafkaConsumer[KafkaMsg] = {
    val properties = kafkaConsumerProperties
    properties.setProperty("group.id", kafkaConsumerGroup)
    new FlinkKafkaConsumer[KafkaMsg](kafkaTopic, new ConsumerStringDeserializationSchema, properties)
  }

  def createStreamProducer(kafkaTopic: String): FlinkKafkaProducer[KafkaMsg] = {
    new FlinkKafkaProducer(kafkaTopic,
      new ProducerStringSerializationSchema(kafkaTopic), kafkaProducerProperties, Semantic.AT_LEAST_ONCE)
  }

  def kafkaConsumerProperties: Properties = {
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", config.getString("kafka.broker-servers"))
    properties
  }

  def kafkaProducerProperties: Properties = {
    val properties = new Properties()
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getString("kafka.broker-servers"))
    properties.put(ProducerConfig.LINGER_MS_CONFIG, new Integer(10))
    properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, new Integer(67108864))
    properties.put(ProducerConfig.BATCH_SIZE_CONFIG, new Integer(16384 * 4))
    properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy")
    properties
  }


}

case class KafkaMsg(key: String, value: String)

class ConsumerStringDeserializationSchema extends KafkaDeserializationSchema[KafkaMsg] {
  private val serialVersionUID = -3224825136576915426L

  override def isEndOfStream(nextElement: KafkaMsg): Boolean = false

  override def deserialize(record: ConsumerRecord[Array[Byte], Array[Byte]]): KafkaMsg = {
    KafkaMsg("", new String(record.value(), StandardCharsets.UTF_8))
  }

  override def getProducedType: TypeInformation[KafkaMsg] = TypeExtractor.getForClass(classOf[KafkaMsg])
}

class ProducerStringSerializationSchema(topic: String) extends KafkaSerializationSchema[KafkaMsg] {
  override def serialize(element: KafkaMsg, timestamp: java.lang.Long): ProducerRecord[Array[Byte], Array[Byte]] = {
    new ProducerRecord[Array[Byte], Array[Byte]](
      topic, element.key.getBytes(StandardCharsets.UTF_8), element.value.getBytes(StandardCharsets.UTF_8))
  }
}

