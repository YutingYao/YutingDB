package com.kkb.flink.tableAndSQL.demo3

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.table.api.Table
import org.apache.flink.table.api.scala.BatchTableEnvironment
import org.apache.flink.table.sinks.CsvTableSink

object DataSetAPI {

  /**
    * 读取dataset.csv文件，然后将数据转换成为一张表，通过TableAPI或者SQL语法进行查询
    * 最后将table再给转换成为dataSet
    * @param args
    */
  def main(args: Array[String]): Unit = {

    //通过批量处理来读取数据
    val environment: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment


    import org.apache.flink.api.scala._

    //将dataSet转换成为一张表
    val sqlEnvironment: BatchTableEnvironment = BatchTableEnvironment.create(environment)

    //读取数据，转换成为一张表
    val sourceSet: DataSet[String] = environment.readTextFile("D:\\开课吧课程资料\\Flink实时数仓\\datas\\dataSet.csv")
    //将数据转换成为样例类
    val userSet: DataSet[User2] = sourceSet.map(x => {
      val strings: Array[String] = x.split(",")
      User2(strings(0).toInt, strings(1), strings(2).toInt)
    })

    //注意：将表名注册成为了user，
    sqlEnvironment.registerDataSet("user",userSet)

    //tableAPI
   // val resultTable: Table = sqlEnvironment.scan("user").filter(" age >  20 ")

    //sql语法来执行  注意：user 是flink保留的关键字，如果非要使用 ``
    val resultTable: Table =sqlEnvironment.sqlQuery("select * from `user` where age > 20")


    //将表转换成为dataSet
    val user2Set: DataSet[User2] = sqlEnvironment.toDataSet[User2](resultTable)
    user2Set.print()


    //将结果写入到csv里面去
    val tableSink = new CsvTableSink("D:\\开课吧课程资料\\Flink实时数仓\\datas\\userSink222.csv"
      , "===="
      , 1
      , WriteMode.OVERWRITE)
    //将数据写入到sink里面去
    resultTable.writeToSink(tableSink)

    environment.execute()
  }


}

case class User2(id:Int,name:String,age:Int)
