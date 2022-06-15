package com.kkb.flink.batch.demo1

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.configuration.Configuration

object ReadTextFile {
  def main(args: Array[String]): Unit = {
    //获取批量处理程序的入口类
    val environment: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
  //如果不设置并行度，默认的并行度，就是CPU核数
    environment.setParallelism(1)
    import org.apache.flink.api.scala._


    val configuration: Configuration = new Configuration()
    //对flink的程序进行配置，配置递归的遍历文件夹下面所有的文件
    configuration.setBoolean("recursive.file.enumeration",true)

    //读取文件
    val fileSet: DataSet[String] = environment
      .readTextFile("D:\\开课吧课程资料\\Flink实时数仓\\datas\\count.txt")
      .withParameters(configuration)

    //统计单词出现的次数
    val resultSet: AggregateDataSet[(String, Int)] = fileSet.flatMap(x =>x.split(" ")).map(x =>(x,1)).groupBy(0).sum(1)


    //打印输出结果
    resultSet.print()

    resultSet.writeAsText("D:\\开课吧课程资料\\Flink实时数仓\\datas\\count_out_result.txt")


    environment.execute()

  }


}
