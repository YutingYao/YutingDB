package hr.fer.stflink.queries.streaming_api

import java.util.concurrent.TimeUnit

import hr.fer.stflink.core.common.{sttuple, Helpers}
import hr.fer.stflink.core.data_types.temporal
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object Q5 {

  /** Q5
    *
    * Find trajectories of the mobile objects that have been less than 500 meters
    * from a point of interest within last 5 minutes.
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

    val q5 = ststream
      .assignAscendingTimestamps( tuple => tuple.timestamp.getTime )
      .keyBy(0)
      .timeWindow(Time.of(5, TimeUnit.MINUTES))
      .apply { temporal.temporalPoint _ }
      .filter( mo => mo.location.distance(pointOfInterest, mo.location.endTime).asInstanceOf[Double] < 500)
      .map( mo => (mo.id, mo.location.subTrajectory(mo.location.startTime, mo.location.endTime)) )

    q5.print

    env.execute("stFlink queries - Streaming API: Q5")
  }
}
