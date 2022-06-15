package com.kkb.flink.common.demo2

import org.apache.flink.api.common.JobExecutionResult
import org.apache.flink.api.common.accumulators.LongCounter
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.configuration.Configuration
import org.apache.flink.api.scala._
object FlinkCounter {
  def main(args: Array[String]): Unit = {
    val environment: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    val fileSet: DataSet[String] = environment.readTextFile("D:\\开课吧课程资料\\Flink实时数仓\\datas\\catalina.out")
    fileSet.map(new RichMapFunction[String,String] {

      var counter=new LongCounter()

      //在open方法里面获取累加器
      override def open(parameters: Configuration): Unit = {
        getRuntimeContext.addAccumulator("my-accumulator",counter)
      }
      override def map(line: String): String = {

        if(line.toLowerCase().contains("exception")){
          counter.add(1L)
        }
        line
      }
    }).setParallelism(4).writeAsText("D:\\开课吧课程资料\\Flink实时数仓\\result_out.txt")
    val job: JobExecutionResult = environment.execute()

    val resultLong: Long = job.getAccumulatorResult[Long]("my-accumulator")
    //统计文件里面exception关键字出现了多少次
    println(resultLong)




  }


}
