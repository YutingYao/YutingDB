package hr.fer.stflink.queries.table_api

import hr.fer.stflink.core.common.{sttuple, TumblingWindow, ST_EndTime, ST_LengthAtTime}
import hr.fer.stflink.core.data_types.{TemporalPoint, stFlink}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.table.api.TableEnvironment
import org.apache.flink.table.api.scala._

object Q4 {

  /** Q4
    *
    * Find all mobile objects (id, position and distance traveled) that have travelled
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
      .assignAscendingTimestamps(tuple => tuple.timestamp.getTime)

    val q5 = stFlink
      .tPoint(ststream, TumblingWindow(Time.minutes(60)))
      .toTable(tEnv, 'id, 'tempPoint)
      .select('id, 'tempPoint, ST_LengthAtTime('tempPoint, ST_EndTime('tempPoint)) as 'distanceTraveled)
      .where('distanceTraveled > 10000 )

    q5.toDataStream[(Int, TemporalPoint, Double)]
      .map (element => (element._1, element._2.atFinal().geom, element._3))
      .print

    env.execute("stFlink queries: Table API: Q4")
  }
}
