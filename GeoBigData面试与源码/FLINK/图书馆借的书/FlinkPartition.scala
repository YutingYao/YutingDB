package com.kkb.flink.batch.demo3

import org.apache.flink.api.scala.ExecutionEnvironment

object FlinkPartition {
  def main(args: Array[String]): Unit = {
    //程序入口类，以及隐式转换包
    val environment: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    import org.apache.flink.api.scala._

    val sourceSet: DataSet[String] = environment.fromElements("hello world","spark flink","abc test")
    val value: DataSet[(String, String)] = sourceSet.map(x =>{(x.split(" ")(0),x.split(" ")(1))})


    value.rebalance()//消除数据倾斜的问题
     // .partitionByHash(0)  //指定数据key，取hashCode码值来进行分区
     // .partitionByRange(0)
      .print()  //通过字段的range范围值来进行分区



    environment.execute()

  }

}
