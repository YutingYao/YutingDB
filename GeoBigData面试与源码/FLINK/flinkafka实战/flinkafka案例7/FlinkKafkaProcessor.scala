package flinkjobs

import configurations.FlinkSampleConfiguration
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import util.FlinkKafkaConnector

class FlinkKafkaProcessor(config: FlinkSampleConfiguration, kafkaConnector: FlinkKafkaConnector) {


  def process(): Unit = {

    val flinkProduceFunction = new FlinkProcessFunction(config)


    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val stream = env.addSource(kafkaConnector.kafkaConsumer(config.jsontest))


    val ingestStream = stream

      .process(flinkProduceFunction)





    ingestStream.getSideOutput(config.sumOutputTag).addSink(kafkaConnector.kafkaProducer(config.jsontest1)).name(config.mySumProducer)


    ingestStream.getSideOutput(config.averageOutputTag).addSink(kafkaConnector.kafkaProducer(config.jsontest2)).name(config.mySumProducer)


    env.execute()
  }
}
