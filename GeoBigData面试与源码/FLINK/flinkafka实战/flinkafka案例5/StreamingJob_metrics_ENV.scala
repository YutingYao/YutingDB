package com.dg.boilerplate.streaming

import com.dg.boilerplate.core.{AppConfiguration, BaseStreaming, KafkaMsg}
import com.dg.boilerplate.metrics.{FlinkMetricsExposingMapFunction}
import org.apache.flink.api.common.JobExecutionResult
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

import java.util.concurrent.TimeUnit

/**
 * Streaming job that reads from a kafka stream, converts the value to lowercase and then streams the data to another kafka sink topic
 * All configuration for the job is in application.conf and can be accessed through the AppConfiguration object
 */
object StreamingJob extends BaseStreaming {

  def plainDatapipelineJob(): Unit ={
    val config = AppConfiguration.config
    val jobName = "plain-datapipeline-job"

    //Setting up the streaming environment
    implicit val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(config.getInt("flink.parallelism"))
    env.enableCheckpointing(config.getInt("flink.checkpointing.interval"))

    //Creating the kafka consumer and kafka producer with configurations
    val kafkaConsumer = createStreamConsumer(config.getString(jobName + ".input.topic"), config.getString(jobName + ".groupId"))
    //kafkaConsumer.setStartFromEarliest()
    val kafkaProducer = createStreamProducer(config.getString(jobName + ".output.success.topic"))
    //val kafkaDownStreamProducer = createStreamProducer(config.getString(jobName + ".output.downstream.topic"))

    //Attaching the kafka consumer as a source. The data stream object represents the stream of events from the source.
    val dataStream: DataStream[KafkaMsg] = env.addSource(kafkaConsumer).name("rawdata")


    /* Simple map function to lowercase the data in the stream. This can be used in place of the
       process function if there is no state/timer involved */
    //val lowerCaseDs: DataStream[KafkaMsg] = dataStream.flatMap(data => data.value.split(" ")).map(data => KafkaMsg("", data.toLowerCase()))

    val lowerCaseDs: DataStream[KafkaMsg] = dataStream.process(new CaseHandlerProcessFunction).name("tolowercase")
    //dataStream.windowAll(TumblingEventTimeWindows.of(Time.seconds(60)))
    //Attaching the kafka producer as a sink
    lowerCaseDs.addSink(kafkaProducer).name("tovalid")

    // Attaching flink Metrics function
    dataStream.map(new FlinkMetricsExposingMapFunction())

    val jobResults =env.execute(jobName)
    System.out.println("The job took " + jobResults.getNetRuntime(TimeUnit.SECONDS) + " to execute")

  }

  def keyedWindowDatapipelineJob(): Unit ={
    val config = AppConfiguration.config
    val jobName = "keyed-window-datapipeline-job"

    //Setting up the streaming environment
    implicit val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(config.getInt("flink.parallelism"))
    env.enableCheckpointing(config.getInt("flink.checkpointing.interval"))

    println(" Consumer Group>> "+config.getString(jobName + ".groupId"))
    //Creating the kafka consumer and kafka producer with configurations
    val kafkaConsumer = createStreamConsumer(config.getString(jobName + ".input.topic"), config.getString(jobName + ".groupId"))
    //kafkaConsumer.setStartFromEarliest()
    val kafkaProducer = createStreamProducer(config.getString(jobName + ".output.success.topic"))


    //Attaching the kafka consumer as a source. The data stream object represents the stream of events from the source.
    val dataStream: DataStream[KafkaMsg] = env.addSource(kafkaConsumer).name("rawdata")

    dataStream
      .keyBy(data=>{
      data.key
    })
      .window(TumblingEventTimeWindows.of(Time.seconds(60)))


    /* Simple map function to lowercase the data in the stream. This can be used in place of the
       process function if there is no state/timer involved */
    //val lowerCaseDs: DataStream[KafkaMsg] = dataStream.flatMap(data => data.value.split(" ")).map(data => KafkaMsg("", data.toLowerCase()))

    val lowerCaseDs: DataStream[KafkaMsg] = dataStream.process(new KeyPrefixHandlerProcessFunction).name("tolowercase")


    //Attaching the kafka producer as a sink
    lowerCaseDs.addSink(kafkaProducer).name("tovalid")

    // Attaching flink Metrics function
    dataStream.map(new FlinkMetricsExposingMapFunction())

    val jobResults =env.execute(jobName)
    System.out.println("The job took " + jobResults.getNetRuntime(TimeUnit.SECONDS) + " to execute")
  }

  def main(args: Array[String]) {
    keyedWindowDatapipelineJob
  }
}
