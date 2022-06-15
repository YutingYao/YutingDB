package com.bigdata.traffic.distribution

import java.{lang, util}
import java.util.Properties

import com.bigdata.traffic.util.{MyBloomFilter, TrafficLog}
import com.bigdata.traffic.util.{MyBloomFilter, TrafficLog}
import org.apache.flink.api.common.JobExecutionResult
import org.apache.flink.api.common.accumulators.{Accumulator, IntCounter}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.triggers.{Trigger, TriggerResult}
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.apache.flink.util.Collector
import org.apache.kafka.common.serialization.StringDeserializer
import redis.clients.jedis.Jedis

import scala.collection.mutable

object AreaDistributionAnalysisByBloomFilter {

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

    val map = new mutable.HashMap[String,Long]()

    mainStream.keyBy(_.areaId)
//      .timeWindow(Time.seconds(30))
      .timeWindow(Time.minutes(10))
      .trigger(new MyTrigger) //因为默认情况下，窗口在没有触发的时候，会把所有数据保存到状态中，只能修改窗口触发规则
      .process(new ProcessWindowFunction[TrafficLog,String,String,TimeWindow] {
        var bloomFilter :MyBloomFilter = _
        //连接redis数据
        var jedis:Jedis =_
        var counter:IntCounter=_

        override def open(parameters: Configuration) = {
          bloomFilter = new MyBloomFilter(1<<25) //当环境中redis是单机，内存只有512M，只能使用25
          jedis = new Jedis("hadoop102",6379)
          jedis.select(7)

          //全局变量：使用累加器作为全局变量,定义一个累加器
          counter= new IntCounter()
        }

        //每一条数据都执行一次，一个数据通过布隆过滤器去重。可以使用redis数据帮我们做位图计算
        override def process(key: String, context: Context, elements: Iterable[TrafficLog], out: Collector[String]): Unit = {
          //注册之前创建的累加器，变成了全局变量
          getRuntimeContext.addAccumulator(elements.last.areaId,counter)

          //根据业务需求，每一个窗口中，每一个区域对应一个布隆过滤器 ,redis中存放布隆过滤器的Key：窗口时间+区域ID
          //还要计算去重之后，每个区域的车辆数量，使用一个Map集合负责去重之后的车辆累加，Map集合中的Key同上
          var start = context.window.getStart
          var end =context.window.getEnd
          //elements 中只有一条数据
          var car = elements.last.car
          var acc :Long = 0 //累加器
          var myKey = end+"_"+key
//          var mapKey = "map"+end+"_"+key

//          var count = jedis.hget("t_map",myKey)
          if(map.contains(myKey)){ //如果map中存在当前区域的累加器，直接得到累加器的值
            acc = map.getOrElse(myKey,0)
          }
          //根据车牌得到位
          val offset: Long = bloomFilter.getOffset(car) //使用车牌的hash值，来模二进制数组的长度 得到一个下标
          //根据redis来做位图计算 ,在二进制数组当前offset下标所对应的值是0，还是1 。如果是0，返回false，1返回True
          val isRepeated: lang.Boolean = jedis.getbit(myKey,offset)

          if(isRepeated){ //车牌是重复的 ,不用累加，但是可以打印一个结果

          }else{ //车牌不重复 ,累加器要加1，同时还要把数据写入redis中
            acc+=1
            counter.add(1)
            //把当前的车牌号码得到bit之后写入redis中
            jedis.setbit(myKey,offset,true)
            //修改map中的累加器
            map.put(myKey,acc)
//            jedis.hset("t_map",myKey,acc+"")
          }
          out.collect(s"区域id:${key},时间范围是,起始时间:${start}----结束时间:${end},目前上路的车辆为${acc}")
        }

      })




//      //Set集合去重必须采用全量窗口函数
//      .apply(
//        (key:String, win:TimeWindow, input:Iterable[TrafficLog], out: Collector[String]) =>{
//          val set: mutable.Set[String] = scala.collection.mutable.Set() //车牌号码的去重
//          for(i<-input){
//            set+=i.car
//          }
//          out.collect(s"区域id:${key},时间范围是,起始时间:${win.getStart}----结束时间:${win.getEnd},一共上路的车辆为${set.size}")
//        }
//      )
      .print()

    val result: JobExecutionResult = streamEnv.execute()
    //得到累加器的值
    result.getAccumulatorResult("01")

//    println(map.size)
//util.BitSet
  }

  class MyTrigger extends Trigger[TrafficLog,TimeWindow]{
    //当窗口中进入一条数据的回调函数；触发机制：只要有一条数据来了，直接触发窗口函数，同时不要把这条保存到状态中
    override def onElement(element: TrafficLog, timestamp: Long, window: TimeWindow, ctx: Trigger.TriggerContext): TriggerResult = TriggerResult.FIRE_AND_PURGE
    //基于运行时间的窗口触发的回调函数
    override def onProcessingTime(time: Long, window: TimeWindow, ctx: Trigger.TriggerContext): TriggerResult = TriggerResult.CONTINUE
    //基于事件时间的窗口触发的回调函数
    override def onEventTime(time: Long, window: TimeWindow, ctx: Trigger.TriggerContext): TriggerResult = TriggerResult.CONTINUE
    //当前窗口已经结束的回调函数
    override def clear(window: TimeWindow, ctx: Trigger.TriggerContext): Unit = {}
  }
}
