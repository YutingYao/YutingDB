package com.bigdata.traffic.util

import java.util

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.{HConnection, HConnectionManager, HTableInterface, Put}

/**
 * HBase优化：批读写
 * 使用批量的方式写入Hbase，一次写入20（100）条
 */
class HBaseWriterDataSink extends RichSinkFunction[java.util.List[Put]]{
  var conf:org.apache.hadoop.conf.Configuration =_
  var conn :HConnection = _
  override def open(parameters: Configuration): Unit = {
    conf = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.quorum","hadoop102:2181")
    conn = HConnectionManager.createConnection(conf)
  }

  override def close(): Unit = conn.close()

  override def invoke(value: util.List[Put], context: SinkFunction.Context[_]): Unit = {
    val table: HTableInterface = conn.getTable("t_track_info")
    table.put(value)
    table.flushCommits()
    table.close()
  }
}
