package com.kkb.flink.stream.demo3

import org.apache.flink.streaming.api.scala.{ConnectedStreams, DataStream, StreamExecutionEnvironment}

/**
  * 使用connect来合并不同类型的流
  */
object ConnectStream {

  def main(args: Array[String]): Unit = {
    //获取程序入口类
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //导入隐式转换的包
    import org.apache.flink.api.scala._
    //第一个流，是string类型
    val firstStream: DataStream[String] = environment.fromElements("hello,world","spark,flink")

    //第二个流，是int类型
    val secondStream: DataStream[Int] = environment.fromElements(1,2)
    //通过connect合并两个不同类型的流
    val resultStream: ConnectedStreams[String, Int] = firstStream.connect(secondStream)

    //两个流connect之后做以下处理，第一个流每个元素添加一个字符串abc  第二个流每个元素乘以10
    val finalStream: DataStream[Any] = resultStream.map(x => {x +"abc"},y=>{y * 10})

    finalStream.print().setParallelism(1)
    //执行程序
    environment.execute()









  }

}
