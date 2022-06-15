package com.kkb.flink.batch.demo4

import org.apache.flink.api.common.functions.FilterFunction
import org.apache.flink.api.scala.ExecutionEnvironment

object FlinkParameter {

  def main(args: Array[String]): Unit = {
    val env=ExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._
    val sourceSet: DataSet[String] = env.fromElements("hello world","abc test")
    val filterSet: DataSet[String] = sourceSet.filter(new MyFilterFunction("test"))
    filterSet.print()
    env.execute()
  }
}

class MyFilterFunction (parameter:String) extends FilterFunction[String]{
  override def filter(t: String): Boolean = {
    if(t.contains(parameter)){
      true
    }else{
      false
    }
  }
}
