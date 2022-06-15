package com.kkb.flink.stream.demo2

import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
  * 并行度为1的数据源
  */
object SingleSource {
  def main(args: Array[String]): Unit = {
    //获取程序入口类
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //导入隐式转换包
    import org.apache.flink.api.scala._

    val sourceStream: DataStream[String] = environment.addSource(new MyOwnSource)

    val resultStream: DataStream[(String, Int)] = sourceStream.flatMap(x =>x.split(" ")).map((_,1)).keyBy(0).sum(1)

    //打印结果
    resultStream.print().setParallelism(2)


    environment.execute("SingleSource")







  }
}


/***
  * 自定义单并行度source
  */
class MyOwnSource extends SourceFunction[String]{
  var isRunning = true

  /**
    * 主要就是用于接收数据
    * @param sourceContext
    */
  override def run(sourceContext: SourceFunction.SourceContext[String]): Unit = {
    while (isRunning){
      //通过SourceContext 上下文对象，调用collet方法可以实现数据的收集
      sourceContext.collect("hello world")
    }
  }

  /**
    * 如果程序取消，就会调用cancel方法
    */
  override def cancel(): Unit = {
    //如果调用取消了，就不用再生产数据
    isRunning = false


  }
}

