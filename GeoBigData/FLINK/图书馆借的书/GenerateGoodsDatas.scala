package com.kkb.flinkhouse.generatedatas

import org.apache.flink.api.java.io.jdbc.JDBCOutputFormat
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.types.Row

object GenerateGoodsDatas {


  def main(args: Array[String]): Unit = {
    //程序入口类
    val environment: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._
    //读取csv文件
    val fileSource: DataSet[String] = environment.readTextFile("file:///D:\\开课吧课程资料\\Flink实时数仓\\实时数仓建表以及数据\\kaikeba_goods.csv")
    //将文本数据按照换行符进行切割，得到每一行数据
    val line: DataSet[String] = fileSource.flatMap(x => {
      x.split("\r\n")
    })
    //对每一行数据，安装  === 进行切割，成为一个个的字段
    val productSet: DataSet[Row] = line.map(x => {
      val pro: Array[String] = x.split("===")
      Row.of(null, pro(1), pro(2), pro(3), pro(4), pro(5), pro(6), pro(7), pro(8),
        pro(9), pro(10))
    })
    //通过dataSet调用output方法，使用JDBCOutputFormat 将数据保存到mysql数据库里面去
    productSet.output(JDBCOutputFormat.buildJDBCOutputFormat()
      .setBatchInterval(2)
      .setDBUrl("jdbc:mysql://node03:3306/product?characterEncoding=utf-8")
      .setDrivername("com.mysql.jdbc.Driver")
      .setPassword("123456")
      .setUsername("root")
      .setQuery("insert into kaikeba_goods(goodsId ,goodsName ,sellingPrice,productPic ,productBrand  ,productfbl  ,productNum ,productUrl ,productFrom,goodsStock ,appraiseNum   ) values(?,?,?,?,?,?,?,?,?,?,?)")
      .finish())
    environment.execute()
  }




}
