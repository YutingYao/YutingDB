package com.kkb.flink.stream.demo2

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

object FileSource {
  def main(args: Array[String]): Unit = {
    //读取hdfs上面的文件进行处理
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //导入隐式转换的包
    import  org.apache.flink.api.scala._

    //读取hdfs文件
    val fileStream: DataStream[String] = environment.readTextFile("hdfs://node01:8020/flink_input")

    val resultStream: DataStream[(String, Int)] = fileStream.flatMap(x => x.split(" ")).map(x =>(x,1)).keyBy(0).sum(1)

    resultStream.writeAsText("hdfs://node01:8020/out_stream")

    environment.execute("FileSource")





  }


}
