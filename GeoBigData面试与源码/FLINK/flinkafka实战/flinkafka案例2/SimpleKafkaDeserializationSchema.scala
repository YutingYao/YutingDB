package eu.fasten.monitor.util

package eu.fasten.synchronization.util

import com.google.gson.JsonParseException
import com.typesafe.scalalogging.Logger
import org.apache.flink.runtime.JobException
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonParser
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.{
  JsonNode,
  ObjectMapper
}
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema
import org.apache.flink.util.Collector
import org.apache.kafka.clients.consumer.ConsumerRecord

class SimpleKafkaDeserializationSchema(includeMetadata: Boolean,
                                       maxRecords: Int = -1)
    extends JSONKeyValueDeserializationSchema(includeMetadata) {

  var counter: Int = 0
  private var objectMapper: ObjectMapper = null
  private lazy val logger = Logger("Deserializer")

  override def deserialize(
      record: ConsumerRecord[Array[Byte], Array[Byte]]): ObjectNode = {

    if (objectMapper == null) {
      objectMapper = new ObjectMapper()
      objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
    }

    if (maxRecords != -1)
      counter += 1

    objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)

    val node: ObjectNode = objectMapper.createObjectNode();
    try {

      if (record.key() != null) {
        node.set("key",
                 objectMapper.readValue(record.key(), classOf[JsonNode]));
      }
      if (record.value() != null) {
        node.set("value",
                 objectMapper.readValue(record.value(), classOf[JsonNode]));
      }

      if (includeMetadata) {
        node
          .putObject("metadata")
          .put("offset", record.offset())
          .put("topic", record.topic())
          .put("partition", record.partition())
          .put("timestamp", record.timestamp())
      }

      return node
    } catch {
      case e: Exception =>
        logger.warn(s"Can't deserialize message ${record.value().toString}", e)
    }

    this.objectMapper.createObjectNode()
  }

  override def isEndOfStream(nextElement: ObjectNode): Boolean = {
    if (maxRecords == -1)
      return false

    if (counter > maxRecords) {
      // Wait for 5 more seconds and then throw an exception.
      // A hacky way to get it working in tests.
      Thread.sleep(5000)

      throw new JobException(
        s"Stop execution after receiving more than ${counter} records.")
    } else {
      false
    }
  }
}
