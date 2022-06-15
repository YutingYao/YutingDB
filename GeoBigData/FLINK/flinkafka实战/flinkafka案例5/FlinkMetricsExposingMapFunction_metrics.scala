package com.dg.boilerplate.metrics

import com.dg.boilerplate.core.{AppConfiguration, KafkaMsg}
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.configuration.Configuration
import org.apache.flink.metrics.{Counter, Histogram, Meter, MeterView,Gauge}
import org.apache.flink.runtime.metrics.DescriptiveStatisticsHistogram


class FlinkMetricsExposingMapFunction extends RichMapFunction[KafkaMsg, KafkaMsg] {
  val config = AppConfiguration.config
  val suffix = config.getString("metrics.suffix")
  @transient private var eventCounter: Counter = _
  @transient private var event10kHistogram: Histogram = _
  @transient private var eventThroughPut: Meter = _
  @transient private var totalProcessingTime: SimpleGauge[Long] =_
  @transient private var startTime: Long = _

  override def open(parameters: Configuration): Unit = {

    eventCounter = getRuntimeContext().getMetricGroup().counter("events_count" + suffix)
    eventThroughPut = getRuntimeContext().getMetricGroup().meter("events_throughput" + suffix, new MeterView(5))
    totalProcessingTime= getRuntimeContext().getMetricGroup().gauge[Long,SimpleGauge[Long]]("events_processing_time_ms"+ suffix,new SimpleGauge[Long]())
    startTime = System.currentTimeMillis()

    event10kHistogram =
      getRuntimeContext()
        .getMetricGroup()
        .histogram("events_histogram_10k" + suffix, new DescriptiveStatisticsHistogram(10000))
  }

  override def map(in: KafkaMsg): KafkaMsg = {
    eventCounter.inc()
    event10kHistogram.update(eventCounter.getCount)
    eventThroughPut.markEvent()
    val timeLapsed = (System.currentTimeMillis() - startTime)
    totalProcessingTime.setValue(timeLapsed)
    in
  }
}