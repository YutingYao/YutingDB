package com.kkb.flinkhouse.syncdatas

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.hadoop.conf
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}

class HBaseSinkFunction  extends RichSinkFunction[OrderObj]{

  var connection:Connection = _
  var table:Table = _

  //主要用于打开hbase表的连接
  override def open(parameters: Configuration): Unit = {
    val configuration: conf.Configuration = HBaseConfiguration.create()
    configuration.set("hbase.zookeeper.quorum", "node01,node02,node03")
    configuration.set("hbase.zookeeper.property.clientPort", "2181")


    connection  = ConnectionFactory.createConnection(configuration)
    table = connection.getTable(TableName.valueOf("flink:data_orders"))

  }

  override def close(): Unit = {

    if(null != table){
      table.close()
    }

    if(null != connection){
      connection.close()
    }

  }


  def insertHbase(table: Table, data: String) = {
    val orderJson: JSONObject = JSON.parseObject(data)
    val orderId: String = orderJson.get("orderId").toString
    val orderNo: String = orderJson.get("orderNo").toString
    val userId: String = orderJson.get("userId").toString
    val goodId: String = orderJson.get("goodId").toString
    val goodsMoney: String = orderJson.get("goodsMoney").toString
    val realTotalMoney: String = orderJson.get("realTotalMoney").toString
    val payFrom: String = orderJson.get("payFrom").toString
    val province: String = orderJson.get("province").toString
    val createTime: String = orderJson.get("createTime").toString

    val put = new Put(orderId.getBytes)

    put.addColumn("f1".getBytes, "orderNo".getBytes, orderNo.getBytes)
    put.addColumn("f1".getBytes, "userId".getBytes, userId.getBytes)
    put.addColumn("f1".getBytes, "goodId".getBytes, goodId.getBytes)
    put.addColumn("f1".getBytes, "goodsMoney".getBytes, goodsMoney.getBytes)
    put.addColumn("f1".getBytes, "realTotalMoney".getBytes, realTotalMoney.getBytes)
    put.addColumn("f1".getBytes, "payFrom".getBytes, payFrom.getBytes)
    put.addColumn("f1".getBytes, "province".getBytes, province.getBytes)
    put.addColumn("f1".getBytes, "createTime".getBytes, createTime.getBytes)
    table.put(put)
  }

  def deleteFromHBase(table: Table, data: String) = {
    //从hbase当中删除一条数据
    val orderJson: JSONObject = JSON.parseObject(data)
    val orderId: String = orderJson.get("orderId").toString
    val delete = new Delete(orderId.getBytes())
    table.delete(delete)
  }

  override def invoke(orderObj: OrderObj, context: SinkFunction.Context[_]): Unit = {
    val database: String = orderObj.database
    val tableStr: String = orderObj.table
    val typeStr: String = orderObj.`type`
    val data: String = orderObj.data
    //val dataObj: JSONObject = JSON.parseObject(data)
    if(database.equalsIgnoreCase("product") && tableStr.equalsIgnoreCase("kaikeba_orders")){
      //判断操作类型，如果是插入，如果是更新，如果是删除
      if(typeStr.equalsIgnoreCase("insert")){
        insertHbase(table,data)
      }else if(typeStr.equalsIgnoreCase("update")){
        insertHbase(table,data)
      }else if(typeStr.equalsIgnoreCase("delete")){
        deleteFromHBase(table,data)
      }
    }
  }
}
