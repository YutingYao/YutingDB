package com.kkb.flink.common.demo3

import org.apache.commons.io.FileUtils
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.configuration.Configuration

object FlinkDistributedCache {
  def main(args: Array[String]): Unit = {
    //将缓存文件，拿到每台服务器的本地磁盘进行存储，然后需要获取的时候，直接从本地磁盘文件进行获取
    val env = ExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._
    //1:注册分布式缓存文件 flink会将我们的分布式缓存文件发送到每一个分区里面去
    env.registerCachedFile("D:\\开课吧课程资料\\Flink实时数仓\\datas\\catalina.out","advert")
    val data = env.fromElements("hello","flink","spark","dataset")
    val result = data.map(new RichMapFunction[String,String] {

      //获取到了分布式缓存的文件，然后将文件内容给打印出来了
      override def open(parameters: Configuration): Unit = {
        super.open(parameters)
        val myFile = getRuntimeContext.getDistributedCache.getFile("advert")
        val lines = FileUtils.readLines(myFile)
        val it = lines.iterator()
        while (it.hasNext){
          val line = it.next();
          println("line:"+line)
        }
      }
      override def map(value: String) = {
        value
      }
    }).setParallelism(2) //并行度设置的是2，会将分布式缓存文件发送到每一个分区里面去
    result.print()
    env.execute()
  }
}
