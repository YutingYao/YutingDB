package com.kkb.flink.tableAndSQL.demo1

import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.{Table, Types}
import org.apache.flink.table.api.scala.StreamTableEnvironment
import org.apache.flink.table.sinks.CsvTableSink
import org.apache.flink.table.sources.CsvTableSource

object CSVTable {
  /**
    * 通过读取csv文件，然后注册成为一张表，查询年龄大于18岁的人
    * @param args
    */
  def main(args: Array[String]): Unit = {
    //读取文件 可以使用dataStream或者dataSet去读取文件

    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //注意：读取处理的文件内容需要注册成为一张表，需要靠StreamTableEnvironment 来进行注册
    val tableEnvironment: StreamTableEnvironment = StreamTableEnvironment.create(environment)

    val sourceTable: CsvTableSource = CsvTableSource.builder()
      .path("D:\\开课吧课程资料\\Flink实时数仓\\datas\\flinksql.csv")
      .lineDelimiter("\r\n")
      .ignoreParseErrors()
      .ignoreFirstLine()
      .fieldDelimiter(",")
      .field("id", Types.INT)
      .field("name", Types.STRING)
      .field("age", Types.INT)
      .build()

    //将CSV文件注册成为一张表
    tableEnvironment.registerTableSource("user",sourceTable)

    /**
      * 通过tableAPI进行查询
      */
    val result: Table = tableEnvironment.scan("user").filter( " age > 20 ")

    /**
      * 通过sql进行查询
      */
     //tableEnvironment.sqlQuery(" select * from  user where age > 18 ")

    //将结果写出去
    /**
      * path: String,
      * fieldDelim: Option[String],
      * numFiles: Option[Int],
      * writeMode: Option[WriteMode]
      */
    val tableSink = new CsvTableSink("D:\\开课吧课程资料\\Flink实时数仓\\datas\\flink_table_result.csv"
      , "===="
      , 1
      , WriteMode.OVERWRITE)


    result.writeToSink(tableSink)

    environment.execute()


  }


}
