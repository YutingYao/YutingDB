package com.kkb.flink.stream.demo3

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}


/**
  * 实现两个流的union功能
  * 主要用于合并两个类型相同的流
  */
object UnionStream {
  def main(args: Array[String]): Unit = {
    //获取程序入口类
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //导入隐式转换的包
    import org.apache.flink.api.scala._
    //第一个流
    val firstStream: DataStream[String] = environment.fromElements("hello,world","flink,spark")
    //第二个流
    val secondStream: DataStream[String] = environment.fromElements("second,stream","hadoop,hive")

    //合并两个流
    val resultStream: DataStream[String] = firstStream.union(secondStream)

    //直接打印合并之后的流
    resultStream.print().setParallelism(1)

    environment.execute()











  }


}
