package com.machinedoll.experiment.processor

import com.machinedoll.experiment.data.TestData
import org.apache.flink.api.common.functions.RichMapFunction

class ConvertPOJOToString extends RichMapFunction[TestData, String]{
  override def map(in: TestData): String = in.toString
}
