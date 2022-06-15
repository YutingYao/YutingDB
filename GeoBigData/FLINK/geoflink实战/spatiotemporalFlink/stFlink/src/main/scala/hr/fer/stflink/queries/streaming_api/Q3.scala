package hr.fer.stflink.queries.streaming_api

import java.util.concurrent.TimeUnit

import hr.fer.stflink.core.common.{sttuple, Helpers}
import hr.fer.stflink.core.data_types.temporal
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object Q3 {

  /** Q3
    *
    * For each mobile object, find its minimal distance from the point of interest
    * during the last half an hour.
    *
    */
  def main(args: Array[String]) {
    if (args.length != 2) {
      System.err.println("USAGE:\n<JAR name> <hostname> <port>")
      return
    }
    val hostName = args(0)
    val port = args(1).toInt

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val rawstream = env.socketTextStream(hostName, port)
    val ststream: DataStream[sttuple] = rawstream.map{ tuple => sttuple(tuple) }

    val pointOfInterest = Helpers.createPointOfInterest

    val q3 = ststream
      .assignAscendingTimestamps( tuple => tuple.timestamp.getTime )
      .keyBy(0)
      .timeWindow(Time.of(30, TimeUnit.MINUTES))
      .apply { temporal.temporalPoint _ }
      .map( mo => (mo.id, mo.location.minDistance(pointOfInterest)) )

    q3.print

    env.execute("stFlink queries - Streaming API: Q3")
  }
}
