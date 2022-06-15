package com.msb.cityTraffic.distribution

/**
 * ClassName AreaDistributionAnalysisByBloomFilter
 * Description
 * Create by liudz
 * Date 2020/9/14 3:01 下午
 */
import java.lang
import java.nio.charset.Charset
import java.util.Properties

import com.google.common.hash.Hashing
import com.msb.cityTraffic.utils._
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.triggers.{Trigger, TriggerResult}
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.util.Collector
import redis.clients.jedis.Jedis

import scala.collection.mutable
import scala.util.control.Breaks


object  AreaDistributionAnalysisByBloomFilter{

  def main(args: Array[String]): Unit = {
    val streamEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.streaming.api.scala._
    streamEnv.setParallelism(1)


    val props = new Properties()
    props.setProperty("bootstrap.servers","hadoop101:9092,hadoop102:9092,hadoop103:9092")
    props.setProperty("group.id","msb_001")

    var map = new mutable.HashMap[String,Long]()

    //创建一个Kafka的Source
    val stream: DataStream[TrafficInfo] = streamEnv.addSource(
      new FlinkKafkaConsumer[String]("t_traffic_msb", new SimpleStringSchema(), props).setStartFromEarliest() //从第一行开始读取数据
    )
      .map(line => {
        var arr = line.split(",")
        new TrafficInfo(arr(0).toLong, arr(1), arr(2), arr(3), arr(4).toDouble, arr(5), arr(6))
      })

    stream.keyBy(_.areaId)
      //模拟10分钟,数据量大概是1000万数据，Set集合去重会有内存溢出，采用布隆过滤器
      //10分钟内，默认情况：窗口没有触发，窗口中的所有数据都缓冲在状态（本地内存），也可能内存的溢出,自定义窗口的触发机制
      .timeWindow(Time.minutes(10))
      //自定义窗口触发器机制,默认窗口触发机制：窗口结束的时候触发，触发之后情况状态中的数据
      //自定义：只要有一条数据进入窗口就触发，并且不保存数据到状态中
      .trigger(new MyTrigger)
      .process(new ProcessWindowFunction[TrafficInfo,String,String,TimeWindow] {

        var jedis:Jedis= _
        var bloomfilter: MyBloomFilter = _

        //初始化
        override def open(parameters: Configuration) = {
          jedis = new Jedis("hadoop102",6379)
          jedis.select(3)
          //          bloomfilter = new MyBloomFilter(1<<27)
          bloomfilter = new MyBloomFilter(1<<22)
        }

        override def process(key: String, context: Context, elements: Iterable[TrafficInfo], out: Collector[String]): Unit = {
          //一条数据执行一次，设计：在一个窗口中，每个区域对应一个布隆过滤器 ,为了方便位图计算，把布隆过滤器存入Redis中
          //设计一个Map集合，存放一个窗口中，每个区域的车辆(去重之后)数量
          val start =context.window.getStart
          val end =context.window.getEnd
          var car = elements.last.car
          var counter:Long = 0  //累加器
          //窗口的结束时间+ 区域ID 组成一个Map集合中的key
          var mapKey =end+"_"+key
          if(map.contains(mapKey)){
            counter = map.getOrElse(mapKey,0)
          }

          //定义是否重复的变量
          var repeated :Boolean = true;
          //根据车辆和布隆过滤器得到下标
          val offsets: Array[Long] = bloomfilter.getOffsets(car)
          val loop = new Breaks
          loop.breakable{
            for(offset<-offsets){
              //有了下标需要位图计算，采用redis帮助我们做位图计算
              //如果返回true，当前车辆可能是重复的，如果是false当前车辆肯定不重复
              val isContain: lang.Boolean = jedis.getbit(mapKey,offset)
              if(!isContain){
                repeated =false
                loop.break()
              }
            }
          }
          if(!repeated){ //车票是重复的,不用累加数量
            //修改Map集合中的计数器，同时还要把车牌写入redis位图中。还可以：把计数器的值写入redis数据库中
            counter+=1
            map.put(mapKey,counter)
            for (offset<-offsets){
              jedis.setbit(mapKey,offset,true)
            }
            //把数据写入redis中
            jedis.hset("t_count",mapKey,counter+"")
          }
          out.collect(s"区域${key},在窗口其实时间${start},到窗口结束时间${end} ,一共有${counter} 辆车")
        }
      })
      .print()


    streamEnv.execute()

  }

  class MyTrigger extends Trigger[TrafficInfo,TimeWindow]{
    //当前窗口进入一条数据的回调函数
    override def onElement(t: TrafficInfo, l: Long, w: TimeWindow, triggerContext: Trigger.TriggerContext): TriggerResult = TriggerResult.FIRE_AND_PURGE
    //当前窗口已经结束的回调函数(基于运行时间)
    override def onProcessingTime(l: Long, w: TimeWindow, triggerContext: Trigger.TriggerContext): TriggerResult = TriggerResult.CONTINUE
    //当前窗口已经结束的回调函数(基于事件时间)
    override def onEventTime(l: Long, w: TimeWindow, triggerContext: Trigger.TriggerContext): TriggerResult = TriggerResult.CONTINUE
    //当前窗口对象销毁
    override def clear(w: TimeWindow, triggerContext: Trigger.TriggerContext): Unit = {}
  }

  /**
   *
   * @param lengthBits 二进制向量的长度
   */
  class MyBloomFilter(lengthBits:Long) extends Serializable{

    /**
     * 根据车牌，计算布隆过滤器(二进制向量)中对应的下标
     * 由于从用两个哈希函数(提高去重准确率)
     * @param car
     * @return
     */
    def getOffsets(car:String):Array[Long] = {
      var result  = new Array[Long](2)
      //调用谷歌的函数算法
      var hashcode1 = googleHash(car)
      if(hashcode1<0){
        hashcode1 = ~ hashcode1 //防止哈希值为负数
      }
      var bit1 =  hashcode1 % lengthBits
      result(0) = bit1

      //调用JDK的哈希算法
      var hashcode2 = car.hashCode()
      if(hashcode2<0){
        hashcode2 = ~ hashcode2 //防止哈希值为负数
      }
      result(1) = hashcode2 % lengthBits
      result
    }

    /**
     * 调用谷歌的哈希算法得到一个哈希值
     * @param car
     * @return
     */
    def googleHash(car:String):Long ={
      Hashing.murmur3_128(1).hashString(car,Charset.forName("UTF-8")).asLong()
    }

  }
}

