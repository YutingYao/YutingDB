package com.binderror.streaming

import com.binderror.core.{AppConfiguration, BaseStreaming, KafkaMsg}
import org.apache.flink.streaming.api.scala._

/**
 * Streaming job that reads from a kafka stream, converts the value to lowercase and then streams the data to another kafka sink topic
 * All configuration for the job is in application.conf and can be accessed through the AppConfiguration object
 */
object StreamingJob extends BaseStreaming {

  def main(args: Array[String]) {
    val config = AppConfiguration.config
    val jobName = "stream1"

    //Setting up the streaming environment
    implicit val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(config.getInt("flink.parallelism"))
    env.enableCheckpointing(config.getInt("flink.checkpointing.interval"))

    //Creating the kafka consumer and kafka producer with configurations
    val kafkaConsumer = createStreamConsumer(config.getString(jobName + ".input.topic"), config.getString(jobName + ".groupId"))
    //kafkaConsumer.setStartFromEarliest()
    val kafkaProducer = createStreamProducer(config.getString(jobName + ".output.success.topic"))

    //Attaching the kafka consumer as a source. The data stream object represents the stream of events from the source.
    val dataStream: DataStream[KafkaMsg] = env.addSource(kafkaConsumer).name("rawdata")

    /* Simple map function to lowercase the data in the stream. This can be used in place of the
       process function if there is no state/timer involved */
    //val lowerCaseDs: DataStream[KafkaMsg] = dataStream.flatMap(data => data.value.split(" ")).map(data => KafkaMsg("", data.toLowerCase()))

    val lowerCaseDs: DataStream[KafkaMsg] = dataStream.process(new CaseHandlerProcessFunction).name("tolowercase")

    //Attaching the kafka producer as a sink
    lowerCaseDs.addSink(kafkaProducer).name("tovalid")

    env.execute(jobName)
  }
}
