package com.kkb.flink.batch.demo4

import org.apache.flink.api.common.ExecutionConfig
import org.apache.flink.api.common.functions.RichFilterFunction
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.configuration.Configuration

object FlinkParameter3 {
  def main(args: Array[String]): Unit = {
    val configuration = new Configuration()
    configuration.setString("parameterKey","test")

    val env=ExecutionEnvironment.getExecutionEnvironment
    //设置全局的参数传递
    env.getConfig.setGlobalJobParameters(configuration)
    import org.apache.flink.api.scala._
    val sourceSet: DataSet[String] = env.fromElements("hello world","abc test")

    val filterSet: DataSet[String] = sourceSet.filter(new MyFilter2)
    filterSet.print()
    env.execute()
  }
}
class MyFilter2 extends RichFilterFunction[String]{
  var value:String ="";
  override def open(parameters: Configuration): Unit = {
    //获取全局的参数传递
    val parameters: ExecutionConfig.GlobalJobParameters = getRuntimeContext.getExecutionConfig.getGlobalJobParameters

    val globalConf:Configuration =  parameters.asInstanceOf[Configuration]
    value = globalConf.getString("parameterKey","test")
  }
  override def filter(t: String): Boolean = {
    if(t.contains(value)){
      true
    }else{
      false
    }
  }
}

