import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object SparkKafkaDemo {
  def main(args: Array[String]): Unit = {
    //1.创建StreamingContext
    //spark.master should be set as local[n], n > 1
    val conf = new SparkConf().setAppName("wc").setMaster("local[*]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val ssc = new StreamingContext(sc,Seconds(5))//5表示5秒中对数据进行切分形成一个RDD
    //准备连接Kafka的参数
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "node01:9092,node02:9092,node03:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "SparkKafkaDemo",
      //earliest:当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
      //latest:当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
      //none:topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
      //这里配置latest自动重置偏移量为最新的偏移量,即如果有偏移量从偏移量位置开始消费,没有偏移量从新来的数据开始消费
      "auto.offset.reset" -> "latest",
      //false表示关闭自动提交.由spark帮你提交到Checkpoint或程序员手动维护
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    val topics = Array("spark_kafka")
    //2.使用KafkaUtil连接Kafak获取数据
    val recordDStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](ssc,
      LocationStrategies.PreferConsistent,//位置策略,源码强烈推荐使用该策略,会让Spark的Executor和Kafka的Broker均匀对应
      ConsumerStrategies.Subscribe[String, String](topics, kafkaParams))//消费策略,源码强烈推荐使用该策略
    //3.获取VALUE数据
    val lineDStream: DStream[String] = recordDStream.map(_.value())//_指的是ConsumerRecord
    val wrodDStream: DStream[String] = lineDStream.flatMap(_.split(" ")) //_指的是发过来的value,即一行数据
    val wordAndOneDStream: DStream[(String, Int)] = wrodDStream.map((_,1))
    val result: DStream[(String, Int)] = wordAndOneDStream.reduceByKey(_+_)
    result.print()
    ssc.start()//开启
    ssc.awaitTermination()//等待优雅停止
  }
}