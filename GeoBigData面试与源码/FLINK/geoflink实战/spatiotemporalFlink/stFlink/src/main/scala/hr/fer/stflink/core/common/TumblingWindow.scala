package hr.fer.stflink.core.common

import org.apache.flink.streaming.api.windowing.time.Time

case class TumblingWindow (size: Time, i:Int)

object TumblingWindow {
  def apply(size: Time) : TumblingWindow = {
    new TumblingWindow(size, 0)
  }
}