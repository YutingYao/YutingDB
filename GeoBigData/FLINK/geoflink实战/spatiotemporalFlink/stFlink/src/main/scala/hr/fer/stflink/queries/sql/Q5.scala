package hr.fer.stflink.queries.sql

import hr.fer.stflink.core.common._
import hr.fer.stflink.core.data_types.{TemporalPoint, stFlink}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.table.api.TableEnvironment
import org.apache.flink.table.api.scala._
import org.locationtech.jts.geom.LineString

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
    val tEnv = TableEnvironment.getTableEnvironment(env)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val rawstream = env.socketTextStream(hostName, port)
    val ststream: DataStream[sttuple] = rawstream.map{ tuple => sttuple(tuple) }
      .assignAscendingTimestamps( tuple => tuple.timestamp.getTime )

    Helpers.registerSTFunctions(tEnv)

    val windowStream = stFlink
      .tPoint(ststream, TumblingWindow(Time.minutes(5)))

    tEnv.registerDataStream("stRelation", windowStream,
                            'id as 'tPointId,
                            'location as 'tPointLocation)
    val q5 =
        tEnv.sql("""
                    SELECT tPointId, tPointLocation,
                           ST_SubTrajectory(tPointLocation,
                                            ST_StartTime(tPointLocation),
                                            ST_EndTime(tPointLocation))
                           FROM stRelation
                           WHERE ST_Distance(tPointLocation, pointOfInterest(),
                                             ST_EndTime(tPointLocation)
                                             ) < 500
                """)

    q5.toDataStream[(Int, TemporalPoint, LineString)]
      .map (element => (element._1, element._3))
      .print

    env.execute("stFlink queries - Table API SQL: Q5")
  }
}
