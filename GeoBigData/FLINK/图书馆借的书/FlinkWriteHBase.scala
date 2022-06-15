package com.kkb.flink.batch.demo5

import org.apache.flink.api.common.io.OutputFormat
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.configuration.Configuration
import org.apache.hadoop.conf
import org.apache.hadoop.hbase.{HBaseConfiguration, HConstants, TableName}
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, Put, Table}

object FlinkWriteHBase {

  def main(args: Array[String]): Unit = {
    //程序入口类，以及隐式转换包
    val environment: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._
    val sourceSet: DataSet[String] = environment.fromElements("01,zhangsan,18","02,lisi,28")
    sourceSet.output(new MyOutputFormat)
    environment.execute()
  }
}

class MyOutputFormat extends OutputFormat[String]{

  var connection:Connection = _
  var  table:Table = _

  /**
    * 主要是用于我们程序的一些配置
    * @param configuration
    */
  override def configure(configuration: Configuration): Unit = {
  }

  /**
    * 主要用于打开hbase连接
    * @param i
    * @param i1
    */
  override def open(i: Int, i1: Int): Unit = {
    val configuration: conf.Configuration = HBaseConfiguration.create()
    configuration.set(HConstants.ZOOKEEPER_QUORUM,"node01")
    configuration.set(HConstants.ZOOKEEPER_CLIENT_PORT,"2181")
    //初始化连接
    connection = ConnectionFactory.createConnection(configuration)
    table = connection.getTable(TableName.valueOf("hbasesource"))

  }

  /**
    * 将数据写入到hbase里面去
    * @param it
    */
  override def writeRecord(it: String): Unit = {
    val strings: Array[String] = it.split(",")
    val put = new Put(strings(0).getBytes())
    put.addColumn("f1".getBytes(),"name".getBytes(),strings(1).getBytes())
    put.addColumn("f1".getBytes(),"age".getBytes(),strings(2).getBytes())
    //通过table将数据保存到hbase里面去
    table.put(put)



  }

  /*
  关闭hbase连接
   */
  override def close(): Unit = {
    if(null != table){
      table.close()
    }


    if(null != connection){
      connection.close()
    }


  }
}

