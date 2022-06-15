package hr.fer.stflink.queries.sql

import hr.fer.stflink.core.common.{Helpers, sttuple}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.TableEnvironment
import org.apache.flink.table.api.scala._
import org.locationtech.jts.geom.Point

object Q1 {

  /** Q1
    *
    * Continuously report mobile objects (id and position) within the area of interest.
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

    tEnv.registerDataStream("Points", ststream,
                            'id as 'pointId,
                            'position as 'pointPosition,
                            'timestamp as 'pointTimestamp)
    val q1 =
        tEnv.sql("""
                    SELECT pointId, pointPosition
                    FROM Points
                    WHERE ST_Within(pointPosition, areaOfInterest()) IS TRUE
                """)

    q1.toDataStream[(Int, Point)]
      .print

    env.execute("stFlink queries - Table API SQL: Q1")
  }
}
