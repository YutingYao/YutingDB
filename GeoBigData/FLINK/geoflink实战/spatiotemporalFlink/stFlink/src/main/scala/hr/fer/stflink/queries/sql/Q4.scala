package hr.fer.stflink.queries.sql

import hr.fer.stflink.core.common._
import hr.fer.stflink.core.data_types.{TemporalPoint, stFlink}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.table.api.TableEnvironment
import org.apache.flink.table.api.scala._

object Q4 {

  /** Q4
    *
    * Find all spatio-temporal objects (id, position and distance traveled) that have travelled
    * more than 10 km during last hour.
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
      .tPoint(ststream, TumblingWindow(Time.minutes(60)))

    tEnv.registerDataStream("stRelation", windowStream,
                            'id as 'tPointId,
                            'location as 'tPointLocation)
    val q4 =
        tEnv.sql("""
                    SELECT tPointId, tPointLocation,
                    ST_LengthAtTime(tPointLocation,
                                    ST_EndTime(tPointLocation))
                    FROM stRelation
                    WHERE ST_LengthAtTime(tPointLocation,
                                          ST_EndTime(tPointLocation)
                                          ) > 10000
                """)

    q4.toDataStream[(Int, TemporalPoint, Double)]
      .map (element => (element._1, element._2.atFinal().geom, element._3))
      .print

    env.execute("stFlink queries - Table API SQL: Q4")
  }
}
