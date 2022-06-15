package com.kkb.flink.tableAndSQL.demo1

import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.{Table, Types}
import org.apache.flink.table.api.scala.StreamTableEnvironment
import org.apache.flink.table.sinks.CsvTableSink
import org.apache.flink.table.sources.CsvTableSource

object TableStudy2 {
  def main(args: Array[String]): Unit = {
    //流式sql，获取运行环境
    val streamEnvironment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //流式table处理环境
    val tableEnvironment: StreamTableEnvironment = StreamTableEnvironment.create(streamEnvironment)
    //注册我们的tableSource
    val source: CsvTableSource = CsvTableSource.builder()
      .field("id", Types.INT)
      .field("name", Types.STRING)
      .field("age", Types.INT)
      .fieldDelimiter(",")
      .ignoreFirstLine()
      .ignoreParseErrors()
      .lineDelimiter("\r\n")
      .path("D:\\开课吧课程资料\\Flink实时数仓\\datas\\flinksql.csv")
      .build()
    //将tableSource注册成为我们的表
    tableEnvironment.registerTableSource("user",source)
    //查询年龄大于18岁的人
    val result: Table = tableEnvironment.scan("user").filter("age >18")
    //打印我们表的元数据信息===》也就是字段信息
    //将查询出来的结果，保存到我们的csv文件里面去
    val sink = new CsvTableSink("D:\\开课吧课程资料\\Flink实时数仓\\datas\\sinkxxxx.csv","===",1,WriteMode.OVERWRITE)
    result.writeToSink(sink)
    streamEnvironment.execute()
  }

}
