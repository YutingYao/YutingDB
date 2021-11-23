package com.kkb.flink.stream.demo6

import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time

/**
  * 实现求取最近5S钟数据的累加的和
  * 读取socke当中的数据
  */
object TimeWindowCount {
  def main(args: Array[String]): Unit = {
    //获取程序入口类
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //导入隐式转换的包
    import org.apache.flink.api.scala._
    val sourceStream: DataStream[String] = environment.socketTextStream("node01",9000)
    val resultStream: DataStream[(Int, Int)] = sourceStream.map(x => (1, x.toInt))
      .keyBy(0)
      .timeWindow(Time.seconds(5)) //如果调用timeWindow只有一个参数，那么就是滚动窗口，没有重复的数据
      //.timeWindow(Time.seconds(5),Time.seconds(2))//如果调用timeWindow有两个参数，那么就是滑动窗口，滑动窗口有重复数据
      .reduce(new ReduceFunction[(Int, Int)] {
      //覆写reduce方法，实现数据累加求和，
      //  t 代表的是我们每次输入的数据
      //  t1 代表的是我们累加求和结果
      override def reduce(t: (Int, Int), t1: (Int, Int)): (Int, Int) = {
        val result: Int = t._2 + t1._2

        (t._1, result)

      }
    })
    resultStream.print()

    environment.execute()



  }


}
