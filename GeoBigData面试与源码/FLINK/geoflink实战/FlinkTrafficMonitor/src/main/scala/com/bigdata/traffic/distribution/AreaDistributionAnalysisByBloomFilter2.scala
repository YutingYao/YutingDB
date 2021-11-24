package com.bigdata.traffic.distribution

import java.lang
import java.util.Properties

import com.bigdata.traffic.util.{MyBloomFilter, TrafficLog}
import com.google.common.hash.{BloomFilter, Funnels}
import org.apache.flink.api.common.functions.{AggregateFunction, RichAggregateFunction}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala.function.{ProcessWindowFunction, WindowFunction}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.triggers.{Trigger, TriggerResult}
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.apache.flink.util.Collector
import org.apache.kafka.common.serialization.StringDeserializer
import redis.clients.jedis.Jedis

import scala.collection.mutable

object AreaDistributionAnalysisByBloomFilter2 {

  def main(args: Array[String]): Unit = {
    val streamEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //导入隐式转换
    import org.apache.flink.streaming.api.scala._
    streamEnv.setParallelism(1)
    //定义Kafka的连接属性
    val props = new Properties()
    props.setProperty("bootstrap.servers","hadoop101:9092,hadoop104:9092")
    props.setProperty("group.id","traffic01")
    props.setProperty("key.deserializer",classOf[StringDeserializer].getName)
    props.setProperty("value.deserializer",classOf[StringDeserializer].getName)
    props.setProperty("auto.offset.reset","latest")

    //Kafka的Source
    val mainStream: DataStream[TrafficLog] = streamEnv.addSource(new FlinkKafkaConsumer[String]("t0601_traffic", new SimpleStringSchema(), props).setStartFromEarliest())
//    val mainStream: DataStream[TrafficLog] = streamEnv.readTextFile(getClass.getResource("/new_data").getPath)
      .map(line => {
        val arr: Array[String] = line.split(",")
        TrafficLog(arr(0).toLong, arr(1), arr(2), arr(3), arr(4).toDouble, arr(5), arr(6))
      })

    //为什么布隆过滤器：解决内存受限的问题。如果对整个窗口的数据做全量计算的话，其实还存在内存受限
    //不要全量计算，可以通过改变窗口的触发机制，不让数据存在状态中

    //每个区域独立去重，所有一个区域一个bloomfilter
    val map: mutable.HashMap[String, BloomFilter[CharSequence]] = mutable.HashMap[String,BloomFilter[CharSequence]]()
//    val bf = BloomFilter.create(Funnels.stringFunnel(),1<<25)
    mainStream
      .keyBy(_.areaId)
//      .timeWindow(Time.seconds(30))
      .timeWindow(Time.minutes(1))

      .aggregate( //先增量（来一条就计算一下），后全量(最后窗口触发的时候执行一次)
        new AggregateFunction[TrafficLog,Long,Long] {

          override def createAccumulator() = 0

          override def add(value: TrafficLog, acc: Long) = {
            var newAcc = acc
            var bf:BloomFilter[CharSequence] = null
            //根据区域得到filter
            if(map.contains(value.areaId)){
              bf = map.get(value.areaId).get
            }else{
              bf =BloomFilter.create(Funnels.stringFunnel(),1<<25)
              map.put(value.areaId,bf)
            }


            //根据google来做位图计算 ,在二进制数组当前offset下标所对应的值是0，还是1 。如果是0，返回false，1返回True
            val isRepeated: lang.Boolean = bf.mightContain(value.car)

            if(isRepeated){ //车牌是重复的 ,不用累加，但是可以打印一个结果

            }else{ //车牌不重复 ,累加器要加1，同时还要把数据写入filter中
              newAcc+=1
              //把当前的车牌号码得到bit之后写入filter
              bf.put(value.car)
            }

            newAcc

          }

          override def getResult(acc: Long) = acc

          override def merge(a: Long, b: Long) = a+b
        },
        new WindowFunction[Long,String,String,TimeWindow]{
          override def apply(key: String, window: TimeWindow, input: Iterable[Long], out: Collector[String]): Unit = {
            out.collect(s"区域id:${key},时间范围是,起始时间:${window.getStart}----结束时间:${window.getEnd},目前上路的车辆为${input.last}")
          }
        }
      )
      .print()

    streamEnv.execute()
//    println(map.size)

  }


}
