package com.kkb.flink.batch.demo4

import org.apache.flink.api.common.functions.RichFilterFunction
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.configuration.Configuration

object FlinkParameter2 {
  def main(args: Array[String]): Unit = {
    val env=ExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._
    val sourceSet: DataSet[String] = env.fromElements("hello world","abc test")
    val configuration = new Configuration()
    configuration.setString("parameterKey","test")
    val filterSet: DataSet[String] = sourceSet.filter(new MyFilter).withParameters(configuration)
    filterSet.print()
    env.execute()
  }
}
class MyFilter extends RichFilterFunction[String]{
  var value:String ="";
  override def open(parameters: Configuration): Unit = {
    value = parameters.getString("parameterKey","defaultValue")
  }
  override def filter(t: String): Boolean = {
    if(t.contains(value)){
      true
    }else{
      false
    }
  }
}
