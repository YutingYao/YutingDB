## 计算曝光数和点击数

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1x8foc3f2fb4.png)

## Spark Streaming 程序的例子 

(取自 Spark Streaming Example, 删除了部分无关代码和注释)

```SCALA
object NetworkWordCount {
	def  main(args: Array[String]) { 
// 通过指定的host和ip，以socket的形式读取每一行数据，以1秒为批次，计算每批数据的word count并打印
    val host = args(0)
    val port = args(1).toInt
    val sparkConf = new SparkConf().setAppName("NetworkWordCount")
//  Spark Streaming 的入口是 StreamingContext 这个类
    val ssc = new StreamingContext(sparkConf, Seconds(1))
    val lines = ssc.socketTextStream(host, port, StorageLevel.MEMORY_AND_DISK_SER)
    val words = lines.flatMap(_.split(" "))
    val wordCounts = words.map(x => (x, 1)).reduceByKey(_ + _)

    wordCounts.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
```

下图为 StreamingContext 这个类中比较重要的两个对象:

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.28d23pazgmc.png)

1. `DStreamGraph` 会持有该 StreamingContext 的所有`输入流`，以及`输出流`。 

2. `JobScheduler` 则分别通过 `ReceiverTracker` 来管理所有的 Receiver 以及处理 Receiver 接收的数据； 通过 `JobGenerator` 定时生成每个批次的 RDD 任务，提交给 Executor 执行。



## spark streaming流式统计单词数量代码

```scala
object WordCountAll {
  // newValues当前批次的出现的单词次数， runningCount表示之前运行的单词出现的结果
 /* def updateFunction(newValues: Seq[Int], runningCount: Option[Int]): Option[Int] = {
    val newCount =  newValues.sum + runningCount.getOrElse(0)// 将历史前几个批次的值和当前批次的值进行累加返回当前批次最终的结果
    Some(newCount)
  }*/
  /**
    * String : 单词 hello
    * Seq[Int] ：单词在当前批次出现的次数
    * Option[Int] ：历史结果
    */
  val updateFunc = (iter: Iterator[(String, Seq[Int], Option[Int])]) => {
    //iter.flatMap(it=>Some(it._2.sum + it._3.getOrElse(0)).map(x=>(it._1,x)))
    iter.flatMap{case(x,y,z)=>Some(y.sum + z.getOrElse(0)).map(m=>(x, m))}
  }
  // 屏蔽日志
  Logger.getLogger("org.apache").setLevel(Level.ERROR)
  def main(args: Array[String]) {
    // 必须要开启2个以上的线程，一个线程用来接收数据，另外一个线程用来计算
    val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
      // 设置sparkjob计算时所采用的序列化方式
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.rdd.compress", "true") // 节约大量的内存内容
    // 如果你的程序出现垃圾回收时间过程，可以设置一下java的垃圾回收参数
    // 同时也会创建sparkContext对象
    // 批次时间 >= 批次处理的总时间 (批次数据量，集群的计算节点数量和配置)
    val ssc = new StreamingContext(conf, Seconds(5))

    //做checkpoint 写入共享存储中
    ssc.checkpoint("c://aaa")

    // 创建一个将要连接到 hostname:port 的 DStream，如 localhost:9999
    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("192.168.175.101", 44444)
    //updateStateByKey结果可以累加但是需要传入一个自定义的累加函数：updateFunc
    val results = lines.flatMap(_.split(" ")).map((_,1)).updateStateByKey(updateFunc, new HashPartitioner(ssc.sparkContext.defaultParallelism), true)
    //打印结果到控制台
    results.print()
    //开始计算
    ssc.start()
    //等待停止
    ssc.awaitTermination()
  }
}
```

## 每个单词出现的次数

按照需求使用spark编写以下程序，要求使用scala语言
当前文件a.txt的格式，请统计每个单词出现的次数

A,b,c

B,b,f,e

```scala

object WordCount {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
      .setAppName(this.getClass.getSimpleName)
      .setMaster("local[*]")
    val sc = new SparkContext(conf)

    var sData: RDD[String] = sc.textFile("a.txt")
    val sortData: RDD[(String, Int)] = sData.flatMap(_.split(",")).map((_,1)).reduceByKey(_+_)
    sortData.foreach(print)
  }
}
```

## 现有一文件，格式如下，请用spark统计每个单词出现的次数

18619304961，18619304064，186193008，186193009

18619304962，18619304065，186193007，186193008

18619304963，18619304066，186193006，186193010

## flatMap 源码：

Spark flatMap 源码：

```scala
/**
   *  Return a new RDD by first applying a function to all elements of this
   *  RDD, and then flattening the results.
   */
  def flatMap[U: ClassTag](f: T => TraversableOnce[U]): RDD[U] = withScope {
    val cleanF = sc.clean(f)
    new MapPartitionsRDD[U, T](this, (context, pid, iter) => iter.flatMap(cleanF))
  }
```

Scala flatMap 源码：

```scala

/** Creates a new iterator by applying a function to all values produced by this iterator
   *  and concatenating the results.
   *
   *  @param f the function to apply on each element.
   *  @return  the iterator resulting from applying the given iterator-valued function
   *           `f` to each value produced by this iterator and concatenating the results.
   *  @note    Reuse: $consumesAndProducesIterator
   */
  def flatMap[B](f: A => GenTraversableOnce[B]): Iterator[B] = new AbstractIterator[B] {
    private var cur: Iterator[B] = empty
    private def nextCur() { cur = f(self.next()).toIterator }
    def hasNext: Boolean = {
      // Equivalent to cur.hasNext || self.hasNext && { nextCur(); hasNext }
      // but slightly shorter bytecode (better JVM inlining!)
      while (!cur.hasNext) {
        if (!self.hasNext) return false
        nextCur()
      }
      true
    }
    def next(): B =<span style="color:#ffffff"> <span style="background-color:rgb(255,0,0)">(if (hasNext) cur else empty).next()</span></span>
  }
```

flatMap其实就是将RDD里的每一个元素执行`自定义函数f`，这时这个元素的结果转换成`iterator`，

最后将这些再拼接成一个新的RDD，也可以理解成原本的每个元素由`横向执行函数f`后再变为纵向。

画红部分一直在回调，当RDD内没有元素为止。