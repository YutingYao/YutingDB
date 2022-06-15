package com.kkb.flink.stream.demo4

import org.apache.flink.api.common.functions.Partitioner
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

object MyPartition {
  def main(args: Array[String]): Unit = {
    //获取程序入口类
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //导入隐式转换的包
    import org.apache.flink.api.scala._

    //设置程序的并行度为2
    environment.setParallelism(2)

    val sourceStream: DataStream[String] = environment.fromElements("hello,world","abc,test","flink,spark")

    //分区之后的数据流
    val afterPartition: DataStream[String] = sourceStream.partitionCustom(new MyPartitioner,x => x )

    val resultMap: DataStream[String] = afterPartition.map(x => {
      //获取打印的线程的id
      val threadId: Long = Thread.currentThread().getId

      println(x + "线程的id为" + threadId)
      x
    })
    resultMap.print()

    environment.execute()





  }


}

//自定义一个类，继承Partitioner 接口，实现一个方法，partition方法
class MyPartitioner extends Partitioner[String]{
  /**
    *
    * @param line   每一行数据
    * @param num  分区的个数
    * @return
    */
  override def partition(line: String, num: Int): Int = {
    //如果数据包含hello，发送到0号分区里面去
    //如果数据不包含hello，发送到1号分区里面去
    if(line.contains("hello")){
      0
    }else{
      1
    }
  }
}

