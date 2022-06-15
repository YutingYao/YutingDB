package com.dg.boilerplate.metrics

class SimpleGauge[T] extends org.apache.flink.metrics.Gauge[T] {
  private var mValue: T = _

  override def getValue: T = {
    mValue
  }

  def setValue(value:T) ={
    mValue = value
  }
}
