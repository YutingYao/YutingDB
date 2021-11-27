package eu.fasten.monitor.influx

import com.influxdb.client.{InfluxDBClient, InfluxDBClientFactory}
import com.influxdb.client.write.Point
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}

class InfluxSink(host: String, port: Int, database: String) extends RichSinkFunction[Point] {

  private var influxClient: InfluxDBClient = null

  override def open(parameters: Configuration): Unit = {
    super.open(parameters)

    influxClient = InfluxDBClientFactory.createV1(s"http://${host}:${port}", "", Array[Char](), database, "autogen")
  }

  override def invoke(value: Point, context: SinkFunction.Context[_]): Unit = {
    try {
      val writeAPI = influxClient.getWriteApi
      writeAPI.writePoint(value)
    }
  }
  override def close(): Unit = influxClient.close()
}
