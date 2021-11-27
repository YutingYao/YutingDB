package flink

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer

import java.util.Properties

class WriteToKafka extends App {
  val env = StreamExecutionEnvironment.getExecutionEnvironment
  //--topic quickstart-events --bootstrap.servers localhost:9092 --zookeeper.connect localhost:2181 --group.id myGroup
  val properties = new Properties
  properties.setProperty("bootstrap.servers", "localhost:9092")
  properties.setProperty("zookeeper.connect", "localhost:2181")
  properties.setProperty("group.id", "myObsGroup")

  // add a simple source which is writing some strings
  val messageStream = env.addSource(new SimpleStringGenerator)

  // write stream to Kafka
  messageStream.addSink(new FlinkKafkaProducer[String]("observation-output",new SimpleStringSchema,properties))

    env.execute
}

@SerialVersionUID(2174904787118597072L)
class SimpleStringGenerator extends SourceFunction[String] {
   var running = true
   var i = 0

  @throws[Exception]
  override def run(ctx: SourceFunction.SourceContext[String]): Unit = {
    while ( {
      running
    }) {
      ctx.collect("Write Data -" + {
        i += 1; i - 1
      })
      Thread.sleep(10)
    }
  }

  override def cancel(): Unit = {
    running = false
  }
}