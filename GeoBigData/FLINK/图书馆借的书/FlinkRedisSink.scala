package com.kkb.flink.stream.demo5

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.redis.RedisSink
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig
import org.apache.flink.streaming.connectors.redis.common.mapper.{RedisCommand, RedisCommandDescription, RedisMapper}

/**
  * flink的sink算子，自己添加sink，将数据写入到redis里面去
  */
object FlinkRedisSink {
  def main(args: Array[String]): Unit = {

    //获取程序入口类
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //导入隐式转换的包
    import org.apache.flink.api.scala._

    //获取一些数据，用于写入到redis里面去
    val sourceStream: DataStream[String] = environment.fromElements("hello,world","spark,flink","key,value")

    //将数据组织成为key，vlaue对的形式
    val tupleStream: DataStream[(String, String)] = sourceStream.map(x => {
      val strings: Array[String] = x.split(",")
      (strings(0), strings(1))
    })

    val builder = new FlinkJedisPoolConfig.Builder

    val config: FlinkJedisPoolConfig = builder
      .setHost("node03")
      .setPort(6379)
      .setMaxIdle(10)
      .setMinIdle(2)
      .setTimeout(8000)
      .build()
    //获取redisSink
    val redisSink = new RedisSink[Tuple2[String,String]](config,new MyRedisMapper)
    //将redisSink添加进来，就可以将数据插入到redis里面去
    tupleStream.addSink(redisSink)
    environment.execute()
  }
}



class MyRedisMapper extends RedisMapper[Tuple2[String,String]]{
  override def getCommandDescription: RedisCommandDescription = {
    new RedisCommandDescription(RedisCommand.SET)

  }

  /**
    * 获取插入到redis当中的数据key
    * @param data
    * @return
    */
  override def getKeyFromData(data: (String, String)): String = {
    data._1
  }

  /**
    * 获取插入到redis当中的数据的value
    * @param data
    * @return
    */
  override def getValueFromData(data: (String, String)): String = {
    data._2

  }
}

