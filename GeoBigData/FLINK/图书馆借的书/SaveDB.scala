package com.kkb.flink.batch.demo2

import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.flink.api.scala.ExecutionEnvironment

object SaveDB {

  /**
    * 通过mapPartition算子，将数据保存到数据库里面去
    * @param args
    */
  def main(args: Array[String]): Unit = {
    //程序入口类，以及隐式转换包
    val environment: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    import org.apache.flink.api.scala._

    //获取一点数据，将数据写入到mysql里面去
    val sourceSet: DataSet[String] = environment.fromElements("1 zhangsan","2 lisi","3 wangwu")

    //将数据保存到mysql里面去
    //调用mapPartition这个算子，是一次性获取一个分区里面的数据，
    //一个分区里面有多条数据 part代表了一个分区的数据，是多条数据
    sourceSet.mapPartition(part =>{

      //加载驱动类
      Class.forName("com.mysql.jdbc.Driver").newInstance()
      //获取数据库连接
      val connection: Connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/flink_db?characterEncoding=utf-8","root","123456")
      //通过part调用map方法，获取到分区里面每一条数据，
      //使用x来代表分区里面的每一条数据
      part.map(x =>{
        val statement: PreparedStatement = connection.prepareStatement("insert into user(id,name) values(?,?)")
        statement.setInt(1,x.split(" ")(0).toInt)
        statement.setString(2,x.split(" ")(1))
        //触发程序真正的去运行
        statement.execute()
      })

      //将返回值申明成为一个变量
     // val closed:Unit = connection.close()


    }).print()  //需要调用print算子，触发程序真正的去执行

    environment.execute()

  }
}
