package com.machinedoll.experiment.source

import java.time.Instant

import com.machinedoll.experiment.data.{TestData, TestDataNested}
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}

import scala.util.Random

/*
  Need load external schema
 */
class SlowEmitSource() extends RichSourceFunction[TestData] {
  var isRunning = true
  var currentIndex: Long = 5839478

  def generateData(id: Long): TestData = TestData(
    string = "id",
    int = id,
    bigDecimal = BigDecimal(Random.nextDouble()),
    instant = Instant.now(),
    nested = TestDataNested(1234),
    option = Some("option"),
    list = List("list"),
    map = Map("a" -> TestDataNested(0), "b" -> TestDataNested(1))
  )

  override def run(ctx: SourceFunction.SourceContext[TestData]): Unit = {
    while (isRunning) {
      ctx.collect(generateData(currentIndex))
      currentIndex = currentIndex + 1
    }
  }

  override def cancel(): Unit = isRunning = false
}
