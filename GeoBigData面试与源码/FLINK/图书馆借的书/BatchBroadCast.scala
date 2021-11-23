package com.kkb.flink.common.demo1

import java.util

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.configuration.Configuration

import scala.collection.mutable


/**
  * dataset当中的广播变量的实现
  */
object BatchBroadCast {

  def main(args: Array[String]): Unit = {

    //获取程序运行的环境
    val environment: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    import org.apache.flink.api.scala._

    //读取商品的数据进行缓存 缓存成为一个Map结构，key是商品id，value是一行数据
    val productSet: DataSet[String] = environment.readTextFile("D:\\开课吧课程资料\\Flink实时数仓\\订单与商品表\\product.txt")

    var myMap = new mutable.HashMap[String,String]()


    /**
      * 将我们的数据封装到map集合里面去了
      */
    val productMap: DataSet[mutable.HashMap[String, String]] = productSet.map(x => {
      val strings: Array[String] = x.split(",")
      myMap.put(strings(0), x)
      myMap
    })



    //读取商品的数据
    val orderSet: DataSet[String] = environment.readTextFile("D:\\开课吧课程资料\\Flink实时数仓\\订单与商品表\\orders.txt")


    //通过调用withBroadcastSet 这个方法，就可以将我们指定的dataSet进行广播出去，并且给我们的广播变量取了一个名字，叫做productBroadCast
    val mapResult: DataSet[String] = orderSet.map(new RichMapFunction[String, String] {
      var broadCastMap = Map[String, String]()


      /**
        * 这个open方法主要是用于做初始化的操作
        *
        * @param parameters
        */
      override def open(parameters: Configuration): Unit = {
        //获取广播变量
        val resultBroadCast: util.List[mutable.HashMap[String, String]] = getRuntimeContext.getBroadcastVariable[mutable.HashMap[String, String]]("productBroadCast")

        val iteratorBroadCast: util.Iterator[mutable.HashMap[String, String]] = resultBroadCast.iterator()
        while (iteratorBroadCast.hasNext) {
          //获取每一个map集合里面的元素
          broadCastMap = broadCastMap.++(iteratorBroadCast.next())
        }
      }
      /**
        * 覆写map方法，
        * @param in 每一行订单数据
        * @return
        */
      override def map(in: String): String = {
        val strings: Array[String] = in.split(",")
        val str: String = broadCastMap.getOrElse(strings(2), "null")
        in + "\t" + str
      }
    }).withBroadcastSet(productMap, "productBroadCast")
    mapResult.print()
    environment.execute()

  }


}
