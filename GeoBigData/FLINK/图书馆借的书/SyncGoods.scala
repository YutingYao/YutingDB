package com.kkb.flinkhouse.syncdatas

import org.apache.flink.api.common.typeinfo.BasicTypeInfo
import org.apache.flink.api.java.io.jdbc.JDBCInputFormat
import org.apache.flink.api.java.typeutils.RowTypeInfo
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala.hadoop.mapreduce.HadoopOutputFormat
import org.apache.flink.types.Row
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client.{Mutation, Put}
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat
import org.apache.hadoop.hbase.{HBaseConfiguration, HConstants}
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job

object SyncGoods {
  /**
    * 读取mysql数据  ==》 插入到hbase里面去
    * @param args
    */
  def main(args: Array[String]): Unit = {

    //获取程序入口类
    val environment: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    import org.apache.flink.api.scala._

    val inputFormat: JDBCInputFormat = JDBCInputFormat.buildJDBCInputFormat()
      .setDBUrl("jdbc:mysql://node03:3306/product?characterEncoding=utf-8")
      .setDrivername("com.mysql.jdbc.Driver")
      .setFetchSize(2)
      .setPassword("123456")
      .setUsername("root")
      .setQuery("select * from kaikeba_goods")
      .setRowTypeInfo(new RowTypeInfo(BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO))
      .finish()

    //读取mysql里面的商品数据

    val productSet: DataSet[Row] = environment.createInput[Row](inputFormat)
    //将我们的数据转换成为Text，Mutation类型

    val hbaseResult: DataSet[(Text, Mutation)] = productSet.map(x => {
      //将我们的每一行数据，构造成为rowkey 以及put对象
      val goodsId: String = x.getField(0).toString
      val goodsName: String = x.getField(1).toString
      val sellingPrice: String = x.getField(2).toString
      val productPic: String = x.getField(3).toString
      val proudctBrand: String = x.getField(4).toString
      val proudctfbl: String = x.getField(5).toString
      val productNum: String = x.getField(6).toString
      val productUrl: String = x.getField(7).toString
      val productFrom: String = x.getField(8).toString
      val goodsStock: String = x.getField(9).toString
      val appraiseNum: String = x.getField(10).toString
      val rowkey = new Text(goodsId)
      val put = new Put(rowkey.getBytes)
      put.addColumn("f1".getBytes(), "goodsName".getBytes(), goodsName.getBytes())
      put.addColumn("f1".getBytes(), "sellingPrice".getBytes(), sellingPrice.getBytes())
      put.addColumn("f1".getBytes(), "productPic".getBytes(), productPic.getBytes())
      put.addColumn("f1".getBytes(), "proudctBrand".getBytes(), proudctBrand.getBytes())
      put.addColumn("f1".getBytes(), "proudctfbl".getBytes(), proudctfbl.getBytes())
      put.addColumn("f1".getBytes(), "productNum".getBytes(), productNum.getBytes())
      put.addColumn("f1".getBytes(), "productUrl".getBytes(), productUrl.getBytes())
      put.addColumn("f1".getBytes(), "productFrom".getBytes(), productFrom.getBytes())
      put.addColumn("f1".getBytes(), "goodsStock".getBytes(), goodsStock.getBytes())
      put.addColumn("f1".getBytes(), "appraiseNum".getBytes(), appraiseNum.getBytes())

      (rowkey, put.asInstanceOf[Mutation])

    })

    val configuration: Configuration = HBaseConfiguration.create()
    configuration.set(HConstants.ZOOKEEPER_QUORUM, "node01,node02,node03")
    configuration.set(HConstants.ZOOKEEPER_CLIENT_PORT, "2181")
    configuration.set(TableOutputFormat.OUTPUT_TABLE,"flink:data_goods")
    //mapreduce.output.fileoutputformat.outputdir
    configuration.set("mapred.output.dir","/tmp2")

    val job: Job = Job.getInstance(configuration)
    //设置输出的数据到hbase表里面去
    hbaseResult.output(new HadoopOutputFormat[Text,Mutation](new TableOutputFormat[Text](),job))
    //执行程序，将读取的数据，输出到hbase表里面去
    environment.execute()
  }
}
