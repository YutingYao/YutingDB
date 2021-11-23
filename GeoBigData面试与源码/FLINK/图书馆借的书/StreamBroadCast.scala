package com.kkb.flink.common.demo1

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
  * dataStream当中的广播变量
  */
object StreamBroadCast {
  def main(args: Array[String]): Unit = {
    //流式处理，程序入口类 StreamExecutionEnvironment

    //获取流式处理的对象
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //注意：在flink的流失处理里面，需要导入隐式转换包，不然会报错
    environment.setParallelism(4)
    import org.apache.flink.api.scala._
    val sourceStream: DataStream[String] = environment.fromElements("hello").setParallelism(1)

    val resultStream: DataStream[String] = sourceStream.broadcast.map(x => {
      println(x)
      x
    })
    resultStream.print()
    environment.execute()
  }
}
