package eu.fasten.monitor.influx

import java.time.Instant

import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.write.Point
import org.apache.flink.api.common.functions.{RichMapFunction, RuntimeContext}

class MapToPoint(topic: String) extends RichMapFunction[(String, Int), Point] {
  override def map(value: (String, Int)): Point = {
    Point.measurement("monitor-fasten-topics")
      .addTag("topic", topic)
      .addTag("type", value._1)
      .addField("amount", value._2)
      .time(Instant.now(), WritePrecision.MS)
  }
}
