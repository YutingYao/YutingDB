package hr.fer.stflink.queries.sql

import hr.fer.stflink.core.common._
import hr.fer.stflink.core.data_types.stFlink
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.table.api.TableEnvironment
import org.apache.flink.table.api.scala._

object Q3 {

  /** Q3
    *
    * For each spatio-temporal object, find its minimal distance from the point of interest
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
    val tEnv = TableEnvironment.getTableEnvironment(env)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val rawstream = env.socketTextStream(hostName, port)
    val ststream: DataStream[sttuple] = rawstream.map{ tuple => sttuple(tuple) }
      .assignAscendingTimestamps( tuple => tuple.timestamp.getTime )

    Helpers.registerSTFunctions(tEnv)

    val windowStream = stFlink
      .tPoint(ststream, TumblingWindow(Time.minutes(30)))

    tEnv.registerDataStream("stRelation", windowStream,
                            'id as 'tPointId,
                            'location as 'tPointLocation)
    val q3 =
        tEnv.sql("""
                    SELECT tPointId,
                           ST_MinDistance(tPointLocation,
                                          pointOfInterest())
                    FROM stRelation
                """)

    q3.toDataStream[(Int, Double)]
      .print

    env.execute("stFlink queries - Table API SQL: Q3")
  }
}
