package com.kkb.flink.tableAndSQL.demo2

import org.apache.flink.table.api._
import org.apache.flink.api.scala._
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.table.api.scala.StreamTableEnvironment
import org.apache.flink.table.sinks.CsvTableSink


object DataStreamAndTable {
  def main(args: Array[String]): Unit = {
    val streamEnvironment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val tableEnvironment: StreamTableEnvironment = StreamTableEnvironment.create(streamEnvironment)
    //读取socket里面的数据，然后注册成为一张表
    val sourceStream: DataStream[String] = streamEnvironment.socketTextStream("node01",9000)
    //将stream转换成为一个样例类
    /**
      * 101,zhangsan,18
      * 102,lisi,20
      * 103,wangwu,25
      * 104,zhaoliu,8
      */
      //将我们的数据转换成为样例类
    val userStream: DataStream[User] = sourceStream.map(x => {
      val strings: Array[String] = x.split(",")
      User(strings(0).toInt, strings(1), strings(2).toInt)
    })
    //通过tableAPI或者sql操作表里面的数据，然后将操作完成的结果，再转换成为dataStream
    tableEnvironment.registerDataStream("userTable",userStream)
    //使用tableAPI进行开发
   // val table: Table = tableEnvironment.scan("userTable").filter(" age > 20 ")
    //使用SQL语句进行开发
    val table: Table = tableEnvironment.sqlQuery("select * from userTable where age > 20 ")

    /**将table转换成为流 有两种模式
      * AppendMode  :追加模式，仅仅只适用于，不断地往表当中追加数据，不适用于update和delete
      * RetraceMode :任何情况都适用于此模式
      */
    //
    //通过appendMode将表转换成为流
    val appendUser: DataStream[User] = tableEnvironment.toAppendStream[User](table)
    appendUser.print()
    //通过RetractMode将表转换成为流
    val retractUser: DataStream[(Boolean, User)] = tableEnvironment.toRetractStream[User](table)
    retractUser.print()
    val tableSink = new CsvTableSink("D:\\开课吧课程资料\\Flink实时数仓\\datas\\userTableSink.csv"
      , "===="
      , 1
      , WriteMode.OVERWRITE)
    table.writeToSink(tableSink)
    streamEnvironment.execute("userTable")
  }
}
case  class User(id:Int,name:String,age:Int)


