package com.kkb.flink.stream.demo2

import org.apache.flink.shaded.netty4.io.netty.handler.codec.http2.Http2Exception.StreamException
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time

object SocketSource {

  /*(*
  从socket当中接受数据，实现单词计数统计
  统计最近5S钟每个单词出现的次数
   */
  def main(args: Array[String]): Unit = {
    //获取程序处理入口类
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //导入隐式转换包
    import  org.apache.flink.api.scala._

    //接受socket数据
    val socketStream: DataStream[String] = environment.socketTextStream("node01",9000)
    //统计最近5S钟出现的单词次数
    val resultStream: DataStream[(String, Int)] = socketStream.flatMap(_.split(" ")).map((_, 1)).keyBy(0)
      .timeWindow(Time.seconds(5), Time.seconds(5))
      .sum(1)
    resultStream.print().setParallelism(1)

    //启动程序
    environment.execute("SocketSource")

  }

}
