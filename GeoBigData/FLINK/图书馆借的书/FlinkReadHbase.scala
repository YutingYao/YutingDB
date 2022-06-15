package com.kkb.flink.batch.demo5

import org.apache.flink.addons.hbase.TableInputFormat
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.configuration
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{Cell, HBaseConfiguration, HConstants, TableName}
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.util.Bytes
import org.apache.flink.api.java.tuple


object FlinkReadHbase {
  def main(args: Array[String]): Unit = {
    //程序入口类，以及隐式转换包
    val environment: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    import org.apache.flink.api.scala._

    val hbaseSource: DataSet[tuple.Tuple2[String, String]] = environment.createInput(new TableInputFormat[tuple.Tuple2[String, String]] {

      /**
        * 初始化方法
        * @param parameters
        */
      override def configure(parameters: configuration.Configuration): Unit = {
        val conf: Configuration = HBaseConfiguration.create()
        conf.set(HConstants.ZOOKEEPER_QUORUM, "node01,node02,node03")
        conf.set(HConstants.ZOOKEEPER_CLIENT_PORT, "2181")
        val connection: Connection = ConnectionFactory.createConnection(conf)
        table = classOf[HTable].cast(connection.getTable(TableName.valueOf("hbasesource")))
        scan = new Scan()
        //设置其实的rowkey和结束rowkey
        /* scan.setStartRow("".getBytes())
         scan.setStopRow("stop".getBytes())*/
      }
      /**
        * 主要用于扫描hbase当中的数据
        *
        * @return
        */
      override def getScanner: Scan = {
        scan
      }

      /*
      指定我们需要读取的hbase哪张表的表名
       */
      override def getTableName: String = {
        "hbasesource"
      }

      /**
        * 将获取到的结果，封装到tuple里面去
        *
        * @param result
        * @return
        */
      /*override def mapResultToTuple(result: Result): (String, String) = {

      }*/
      override def mapResultToTuple(result: Result): tuple.Tuple2[String, String] = {
        //result封装了我们的结果数据
        //获取rowkey
        val rowkey: String = Bytes.toString(result.getRow)
        //循环遍历所有的cell，将cell值都拼接起来
        var str = ""
        for (mycell <- result.rawCells()) {
          //获取cell值
          val cellValue: String = Bytes.toString(mycell.getValue)
          str += cellValue
          str += "\t"
        }
        val tuple2 = new org.apache.flink.api.java.tuple.Tuple2[String,String]()
        tuple2.setField(rowkey,0)
        tuple2.setField(str,1)
        tuple2
      }





    })
    hbaseSource.print()

    environment.execute()


  }


}
