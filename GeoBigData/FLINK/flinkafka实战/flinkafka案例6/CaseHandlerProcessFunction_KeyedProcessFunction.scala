package com.binderror.streaming

import com.binderror.core.KafkaMsg
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.util.Collector

class CaseHandlerProcessFunction extends ProcessFunction[KafkaMsg, KafkaMsg]{

  override def processElement(i: KafkaMsg, context: ProcessFunction[KafkaMsg, KafkaMsg]#Context, collector: Collector[KafkaMsg]): Unit = {
      i.value.split(" ").map(data => collector.collect(KafkaMsg("", data.toLowerCase())))
  }
}
