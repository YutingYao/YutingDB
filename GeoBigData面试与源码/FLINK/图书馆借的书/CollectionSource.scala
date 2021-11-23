package com.kkb.flink.batch.demo1

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.runtime.rest.messages.JobExceptionsInfo.ExecutionExceptionInfo

object CollectionSource {

  def main(args: Array[String]): Unit = {
    val environment: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._
    val sourceSet: DataSet[String] = environment.fromElements("hello world","hello flink")
    val resultSet: AggregateDataSet[(String, Int)] = sourceSet.flatMap(x => x.split(" ")).map(x =>(x,1)).groupBy(0).sum(1)
    resultSet.print()
    environment.execute()
  }
}
