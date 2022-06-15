package com.kkb.flink.tableAndSQL.demo4

import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.{Table, Types}
import org.apache.flink.table.api.scala.StreamTableEnvironment
import org.apache.flink.table.descriptors.{Json, Kafka, Schema}
import org.apache.flink.table.sinks.CsvTableSink

object FlinkKafkaJSON {
  def main(args: Array[String]): Unit = {
    //通过flink的流式处理程序，一直读取kafka的数据

    //获取程序的入口类
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val tableEnvironment: StreamTableEnvironment = StreamTableEnvironment.create(environment)

    val kafka: Kafka = new Kafka().startFromLatest()
      .topic("kafka_source_table")
      .version("0.11")
      .property("group.id", "test_group")
      .property("bootstrap.servers", "node01:9092,node02:9092,node03:9092")

//{"userId":1123,"day":"2017-03-02","begintime":1488326400000,"endtime":1488327000000}
    val schema = new Schema()
      .field("userId",Types.LONG)
        .field("day",Types.STRING)
        .field("begintime",Types.LONG)
        .field("endtime",Types.LONG)


    val json: Json = new Json().failOnMissingField(true).deriveSchema()

    //连接kafka，解析kafka当中的json格式的数据，并且将数据转换成为一张表
    tableEnvironment.connect(kafka)
      .inAppendMode()
      .withSchema(schema)
      .withFormat(json)
      .registerTableSource("user_log")
    val resultTable: Table = tableEnvironment.sqlQuery("select userId,`day`,begintime,endtime from user_log where userId > 1120")
    //将结果写入到csv里面去
    val tableSink = new CsvTableSink("D:\\开课吧课程资料\\Flink实时数仓\\datas\\flink_kafka_json.csv"
      , "===="
      , 1
      , WriteMode.OVERWRITE)

    /**
      * name: String,
      * fieldNames: Array[String],
      * fieldTypes: Array[TypeInformation[_]],
      * tableSink: TableSink[_]
      */
    //注册我们的输出的文件
    tableEnvironment.registerTableSink("csvSink",Array[String]("f0","f1","f2","f3"),
      Array[TypeInformation[_]](Types.LONG,Types.STRING,Types.LONG,Types.LONG),tableSink)
    //将结果保存到文件里面去
    resultTable.insertInto("csvSink")
    //启动程序，开始执行
    environment.execute()


  }


}
