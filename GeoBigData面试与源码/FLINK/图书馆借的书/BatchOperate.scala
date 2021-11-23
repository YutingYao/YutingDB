package com.kkb.flink.stream.demo1

import org.apache.flink.api.scala.{AggregateDataSet, DataSet, ExecutionEnvironment}
import org.apache.flink.core.fs.FileSystem.WriteMode

object BatchOperate {
  //实现flink批量处理代码  ，读取count.txt文件，实现单词计数统计
  def main(args: Array[String]): Unit = {

    //流式处理入口类是StreamExecutionEnvironment

    //批量处理入口类是ExecutionEnvironment
    val environment: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    // 需要导入隐式转换的类
    import org.apache.flink.api.scala._
    //读取文件
    val fileDataSet: DataSet[String] = environment.readTextFile("D:\\开课吧课程资料\\Flink实时数仓\\datas\\count.txt","utf-8")
    //统计单词出现次数
    val resultDataSet: AggregateDataSet[(String, Int)] = fileDataSet.flatMap(x =>x.split(" ")).map(x =>(x,1)).groupBy(0).sum(1)
    //将结果写入到文件里面去
    resultDataSet.writeAsText("D:\\开课吧课程资料\\Flink实时数仓\\datas\\countout_result.txt",WriteMode.OVERWRITE)
    //调用execute启动程序
    environment.execute()

  }

}
