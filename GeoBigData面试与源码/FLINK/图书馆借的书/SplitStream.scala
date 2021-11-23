package com.kkb.flink.stream.demo3

import java.{lang, util}

import org.apache.flink.streaming.api.collector.selector.OutputSelector
import org.apache.flink.streaming.api.scala.{DataStream, SplitStream, StreamExecutionEnvironment}

/**
  * 使用split将我们的流给切开
  */
object SplitStream {
  def main(args: Array[String]): Unit = {
    //获取程序入口类
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //导入隐式转换的包
    import org.apache.flink.api.scala._

    //初始的流  ， 将元素当中包含hello的作为一个流，不包含hello的作为另外一个流
    val sourceStream: DataStream[String] = environment.fromElements("hello,world","spark,flink","hadoop,hive")

    val splitedStream: SplitStream[String] = sourceStream.split(new OutputSelector[String] {
      override def select(out: String): lang.Iterable[String] = {
        val strings = new util.ArrayList[String]()
        if (out.contains("hello")) {
          //如果元素当中包含hello，就放入到一个叫做hello的流里面去
          strings.add("hello")
        } else {
          //如果元素当中不包含hello，那么久放入到一个叫做other的流里面去
          strings.add("other")
        }
        strings
      }
    })
    //通过我们定义的名字调用select来获取对应的流
    val helloStream: DataStream[String] = splitedStream.select("hello")

    helloStream.print().setParallelism(1)


    environment.execute()


  }


}
