package com.kkb.flink.batch.demo3

import org.apache.flink.api.common.functions.Partitioner
import org.apache.flink.api.scala.ExecutionEnvironment

object FlinkCustomerPartition {

  def main(args: Array[String]): Unit = {
    val environment: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //设置我们的分区数，如果不设置，默认使用CPU核数作为分区个数
    environment.setParallelism(2)
    import  org.apache.flink.api.scala._
    //获取dataset
    val sourceDataSet: DataSet[String] = environment.fromElements("hello world","spark flink","hello world","hive hadoop")
    val result: DataSet[String] = sourceDataSet.partitionCustom(new MyPartitioner2,x => x + "")
    val value: DataSet[String] = result.map(x => {
      println("数据的key为" + x + "线程为" + Thread.currentThread().getId)
      x
    })
    value.print()
    environment.execute()
  }


}


class MyPartitioner2  extends Partitioner[String]{
  override def partition(word: String, num: Int): Int = {
    println("分区个数为" +  num)
    if(word.contains("hello")){
      println("0号分区")
      0
    }else{
      println("1号分区")
      1
    }
  }
}
