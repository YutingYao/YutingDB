package com.bigdata.traffic.warning

import com.bigdata.traffic.util.ViolationInfo
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.table.api.Table
import org.apache.flink.table.api.scala.StreamTableEnvironment
import org.apache.flink.types.Row
import org.apache.flink.util.Collector

//假设有两个流
/**
 * 1、违法车辆的实时流（数据乱序2） 2、交警出警记录的实时流 （数据乱序2）
 * 第一种：找出当前违法车辆在5分钟内有对应的出警记录的数据
 */

//出警记录的对象

object ViolationCarAndPoliceActionAnalysisBySql {

  def main(args: Array[String]): Unit = {
    val streamEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //导入隐式转换
    val tableEnv: StreamTableEnvironment = StreamTableEnvironment.create(streamEnv)
    import org.apache.flink.streaming.api.scala._
    import org.apache.flink.table.api.scala._
    streamEnv.setParallelism(1)
    streamEnv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val stream1: DataStream[ViolationInfo] = streamEnv.socketTextStream("hadoop101", 8888)
      .map(line => {
        val arr: Array[String] = line.split(",")
        new ViolationInfo(arr(0), arr(1), arr(2).toLong, 0)
      }).assignAscendingTimestamps(_.createTime)

     val stream2: DataStream[PoliceAction] = streamEnv.socketTextStream("hadoop102", 8888)
      .map(line => {
        val arr: Array[String] = line.split(",")
        new PoliceAction(arr(0), arr(1), arr(2), arr(3).toLong)
      }).assignAscendingTimestamps(_.actionTime)


//    tableEnv.fromDataStream()

    tableEnv.registerDataStream("t_violation",stream1,'car,'msg,'createTime)
    tableEnv.registerDataStream("t_police",stream2,'policeId,'car,'actionStatus,'actionTime)

    val result: Table = tableEnv.sqlQuery("select t1.car,t2.policeId,t2.actionStatus,t2.actionTime from t_violation t1 inner join t_police t2 on t1.car=t2.car")

    val r: DataStream[(Boolean, Row)] = tableEnv.toRetractStream(result) //把状态中原始的数据也给你打印出来了
    r.print() //sink
    tableEnv.execute("test2")
//    stream1.keyBy(_.car)
//      .intervalJoin(stream2.keyBy(_.car)) //上下10秒中都可以关联 ,时间可以包括边界
//      .between(Time.seconds(0),Time.seconds(10)) //设置一个时间边界，在这个边界内，两个流的数据自动根据车牌号相同关联。
//      .process(new ProcessJoinFunction[ViolationInfo,PoliceAction,String] {
//        override def processElement(left: ViolationInfo, right: PoliceAction, ctx: ProcessJoinFunction[ViolationInfo, PoliceAction, String]#Context, out: Collector[String]) = {
//          //已经关联成功过了
//          out.collect(s"违法车辆:${left.car},已经有交警出警了，警号为:${right.policeId},出警的状态是:${right.actionStatus},出警的时间:${right.actionTime}")
//        }
//      })
//      .print()

//    streamEnv.execute()



  }
}
