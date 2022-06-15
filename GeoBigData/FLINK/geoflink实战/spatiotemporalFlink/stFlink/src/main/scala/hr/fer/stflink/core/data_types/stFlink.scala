package hr.fer.stflink.core.data_types

import hr.fer.stflink.core.common.{sttuple, SlidingWindow, TumblingWindow}
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.DataStream

object stFlink {

  def tPoint(stream: DataStream[sttuple], window: SlidingWindow): DataStream[temporaltuple] = {
    stream
      .keyBy(0)
      .timeWindow(window.size, window.slide)
      .apply { temporal.temporalPoint _ }
  }

  def tPoint(stream: DataStream[sttuple], window: TumblingWindow): DataStream[temporaltuple] = {
    stream
      .keyBy(0)
      .timeWindow(window.size)
      .apply { temporal.temporalPoint _ }
  }

}