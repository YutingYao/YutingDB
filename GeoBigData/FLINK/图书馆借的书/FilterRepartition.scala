package com.kkb.flink.stream.demo4

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
  * 实现过滤数据之后，重新进行分区
  */
object FilterRepartition {
  def main(args: Array[String]): Unit = {
    //获取程序入口类
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //导入隐式转换的包
    import org.apache.flink.api.scala._

    val sourceStream: DataStream[String] = environment.fromElements("hello,world","spark,flink","hadoop,hive")

    //过滤数据里面包含hello元素的数据
    val resultStream: DataStream[(String, Int)] = sourceStream.filter(x => x.contains("hello"))
      //.shuffle  //重新分区 会有网络拷贝的情况
      //.rebalance  //重点是消除数据倾斜问题
      .rescale //在每个节点内部进行数据负载均衡，不涉及到网络拷贝情况
      .flatMap(x => x.split(","))
      .map(x => (x, 1))
      .keyBy(0)
      .sum(1)
    resultStream.print()

    environment.execute()



  }
}
