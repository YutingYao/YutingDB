package com.kkb.flinkhouse.generatedatas

import java.text.{DecimalFormat, SimpleDateFormat}
import java.util.{Date, UUID}

import org.apache.flink.api.common.typeinfo.BasicTypeInfo
import org.apache.flink.api.java.io.jdbc.JDBCAppendTableSink
import org.apache.flink.streaming.api.datastream.DataStreamSource
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}
import org.apache.flink.types.Row

import scala.util.Random

object GenerateOrderDatas {
  def main(args: Array[String]): Unit = {
    //获取执行环境
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    environment.setParallelism(1)

    //使用JDBCAppendTableSink 这个sink，将数据发送到表里面去
    val sink: JDBCAppendTableSink = JDBCAppendTableSink.builder()
      .setDrivername("com.mysql.jdbc.Driver")
      .setDBUrl("jdbc:mysql://node03:3306/product?characterEncoding=utf-8")
      .setUsername("root")
      .setPassword("123456")
      .setBatchSize(2)
      .setQuery("insert into kaikeba_orders (orderNo,userId ,goodId ,goodsMoney ,realTotalMoney ,payFrom ,province) values (?,?,?,?,?,?,?)")
      .setParameterTypes(BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO)
      .build()

    /* //定义字段的名字
      val FIELD_NAMES: Array[String] =Array[String]("orderNo", "userId", "goodId", "goodsMoney", "realTotalMoney", "payFrom", "province")

     //定义字段的类型
      val FIELD_TYPES: Array[TypeInformation[_]] = Array[TypeInformation[_]](BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO)
 */
    //types: Array[TypeInformation[_]], fieldNames: Array[String]
    //  val info = new RowTypeInfo(FIELD_TYPES,FIELD_NAMES)

    val sourceStream: DataStreamSource[Row] = environment.addSource(
      //使用自定义source，来生成订单数据
      new RichParallelSourceFunction[Row] {
        var isRunning = true
        override def run(sc: SourceFunction.SourceContext[Row]): Unit = {
          while (isRunning) {
            val order: Order = generateOrder
            sc.collect(Row.of(order.orderNo, order.userId, order.goodId, order.goodsMoney
              , order.realTotalMoney, order.payFrom, order.province))
            Thread.sleep(1000)
          }
        }
        override def cancel(): Unit = {
          isRunning = false
        }
      }
      //,info  这里可以不用指定字段的名称以及字段的类型，也可以同样将数据插入到表
    )
    //将数据插入到表当中去
    sink.emitDataStream(sourceStream)
    //执行我们的程序
    environment.execute()

  }

  //随机生成订单
  def generateOrder:Order={
    val province: Array[String] = Array[String]("北京市", "天津市", "上海市", "重庆市", "河北省", "山西省", "辽宁省", "吉林省", "黑龙江省", "江苏省", "浙江省", "安徽省", "福建省", "江西省", "山东省", "河南省", "湖北省", "湖南省", "广东省", "海南省", "四川省", "贵州省", "云南省", "陕西省", "甘肃省", "青海省")
    val random = new Random()
    //订单号
    val orderNo: String = UUID.randomUUID.toString
    //用户 userId
    val userId: Int = random.nextInt(10000)
    //商品id
    val goodsId: Int = random.nextInt(1360)
    var goodsMoney: Double = 100 + random.nextDouble * 100
    //商品金额
    goodsMoney = formatDecimal(goodsMoney, 2).toDouble
    var realTotalMoney: Double = 150 + random.nextDouble * 100
    //订单付出金额
    realTotalMoney = formatDecimal(goodsMoney, 2).toDouble

    val payFrom: Int = random.nextInt(5)
    //省份id
    val provinceName: String = province(random.nextInt(province.length))
    val date = new Date
    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val dateStr: String = format.format(date)

    Order(orderNo,userId+"",goodsId+"",goodsMoney+"",realTotalMoney+"",payFrom+"",provinceName)
  }

  //生成金额
  def formatDecimal(d: Double, newScale: Int): String = {
    var pattern = "#."
    var i = 0
    while ( {
      i < newScale
    }) {
      pattern += "#"

      {
        i += 1; i - 1
      }
    }
    val df = new DecimalFormat(pattern)
    df.format(d)
  }


}

//定义样例类，用于封装数据
case class Order(orderNo:String
                 ,userId:String
                 ,goodId:String
                 ,goodsMoney:String
                 ,realTotalMoney:String
                 ,payFrom:String
                 ,province:String
                ) extends Serializable

