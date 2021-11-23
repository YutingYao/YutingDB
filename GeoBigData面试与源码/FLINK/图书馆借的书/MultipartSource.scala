package com.kkb.flink.stream.demo2

import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

object MultipartSource {

  def main(args: Array[String]): Unit = {

    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._


    val sourceStream: DataStream[String] = environment.addSource(new MyOwnSource2)

    val resultStream: DataStream[(String, Int)] = sourceStream.flatMap(x => x.split(" ")).map(x =>(x,1)).keyBy(0).sum(1)


    resultStream.print().setParallelism(4)

    environment.execute()


  }


}


/***
  * 定义一个多并行度的souce，继承的RichSourceFunction 这个接口
  */
class MyOwnSource2 extends RichSourceFunction[String]{
  var isRunning = true
  override def run(sourceContext: SourceFunction.SourceContext[String]): Unit = {
    while (isRunning){
      sourceContext.collect("hello world")

    }
  }

  override def cancel(): Unit = {
    isRunning  = false


  }
}
