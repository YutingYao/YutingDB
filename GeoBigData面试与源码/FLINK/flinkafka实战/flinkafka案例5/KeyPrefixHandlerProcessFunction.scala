package com.dg.boilerplate.streaming

import com.dg.boilerplate.core.KafkaMsg
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.configuration.Configuration
import org.apache.flink.metrics.Counter
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.util.Collector

class KeyPrefixHandlerProcessFunction extends ProcessFunction[KafkaMsg, KafkaMsg]
{
  override def processElement(input: KafkaMsg, context: ProcessFunction[KafkaMsg, KafkaMsg]#Context, collector: Collector[KafkaMsg]): Unit = {
    input.value.split(" ").map(data => {
      println("KeyPrefixHandlerProcessFunction::processElement Key->"+data.substring(2)+" Value->"+data.toLowerCase())
      collector.collect(KafkaMsg(data.substring(0,1), data.toLowerCase()))
    })
  }
}
