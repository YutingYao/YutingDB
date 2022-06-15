

## 注意点

累加器使用时，只有`使用一次action操作`时才能保证结果正确。

更好办法是， 将任务之间的`依赖关系切断` 。

什么方法有这种功能呢？

你们肯定都想到了，`cache，persist`。

调用这个方法的时候会将之前的依赖切除，后续的累加器就不会再被之前的transfrom操作影响了。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.7jiqg7o35b80.png)

```scala
val accum= sc.accumulator(0, "Error Accumulator")
val data = sc.parallelize(1 to 10)

//代码和上方相同
val newData = data.map{x => {...}}
//使用cache缓存数据，切断依赖。
newData.cache.count
//此时accum的值为5
accum.value

newData.foreach(println)
//此时的accum依旧是5
accum.value
```

总结:

使用Accumulator时，保证准确性，有几种方式：

- transform中执行的累加器更新，`只使用一次action操作`。
- transform中执行的累加器更新，被使用多次时，则使用`cache或persist操作切断依赖`。
- action中执行的累加器更新，Spark保证每个任务对累加器的更新`只应用一次`，即重新启动的任务不会更新该值。

## 系统累加器

### 案例一

针对一个输入的`日志文件`，如果我们想计算文件中`所有空行的数量`，我们可以编写以下程序：

2.0.0之前

```scala
//在driver中定义
val accum = sc.accumulator(0, "Example Accumulator")
//在task中进行累加
sc.parallelize(1 to 10).foreach(x=> accum += 1)

//在driver中输出
accum.value
//结果将返回10
res: 10
```

```scala
scala> val notice = sc.textFile("./NOTICE")
scala> val blanklines = sc.accumulator(0)
// 调用SparkContext.accumulator(initialValue)方法
// 创建出存有初始值的累加器
scala> val tmp = notice.flatMap(line => {
     |    if (line == "") {
     |       blanklines += 1
     |    }
     |    line.split(" ")
     | })
// Spark闭包里的执行器代码可以使用累加器的 += 方法
scala> tmp.count()
scala> blanklines.value
// 驱动器程序可以调用累加器的value属性(在Java中使用value()或setValue())来访问累加器的值。
```

```scala
scala> val notice = sc.textFile("./NOTICE")
notice: org.apache.spark.rdd.RDD[String] = ./NOTICE MapPartitionsRDD[40] at textFile at <console>:32

scala> val blanklines = sc.accumulator(0)
warning: there were two deprecation warnings; re-run with -deprecation for details
blanklines: org.apache.spark.Accumulator[Int] = 0
// 返回值为org.apache.spark.Accumulator[T] 对象
// 其中 T 是初始值 initialValue 的类型。
scala> val tmp = notice.flatMap(line => {
     |    if (line == "") {
     |       blanklines += 1
     |    }
     |    line.split(" ")
     | })
tmp: org.apache.spark.rdd.RDD[String] = MapPartitionsRDD[41] at flatMap at <console>:36

scala> tmp.count()
res31: Long = 3213

scala> blanklines.value
res32: Int = 171
```

对于要在 action 操作中使用的累加器，

Spark只会把每个 task 对各累加器的修改应用一次。

因此，如果想要一个无论在失败还是重复计算时都绝对可靠的累加器，

我们必须把它放在 `foreach()` 这样的行动操作中。

transformation操作中累加器可能会发生不止一次更新。

2.0.0之后

```scala
scala> val accum = sc.longAccumulator("My Accumulator")
accum: org.apache.spark.util.LongAccumulator = LongAccumulator(id: 0, name: Some(My Accumulator), value: 0)

scala> sc.parallelize(Array(1, 2, 3, 4)).foreach(x => accum.add(x))

scala> accum.value
res2: Long = 10
```

### 案例二：系统累加器：longAccumulator

案例：实现求1,2,3,4,5的累加之和。

```scala
package com.zxl.spark.core

import org.apache.spark.{SparkConf, SparkContext}

/*
* 累加器
* */
object AccumulatorDemo {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[4]").setAppName("AccumulatorDemo")
    val sc: SparkContext = new SparkContext(conf)
    val rdd = sc.makeRDD(List(1,2,3,4,5))
    // 声明累加器
    var sum = sc.longAccumulator("sum")
    rdd.foreach(
      num => {
        // 使用累加器
        sum.add(num)
      } )
    // 获取累加器的值
    println("sum = " + sum.value)
  }
}
```

运行结果

```s
sum = 15
```

## 自定义累加器

AccumulatorV2来提供更加友好的自定义类型累加器的实现方式。

实现`自定义类型累加器`需要继承`AccumulatorV2`并至少覆写下例中出现的方法

### 源码

```scala

private def updateAccumulators(event: CompletionEvent): Unit = {
    val task = event.task
    val stage = stageIdToStage(task.stageId)

    event.accumUpdates.foreach { updates =>
      val id = updates.id
      try {
        // 在驱动上找到对应的累加器并更新
        val acc: AccumulatorV2[Any, Any] = AccumulatorContext.get(id) match {
          case Some(accum) => accum.asInstanceOf[AccumulatorV2[Any, Any]]
          case None =>
            throw new SparkException(s"attempted to access non-existent accumulator $id")
        }
        acc.merge(updates.asInstanceOf[AccumulatorV2[Any, Any]])
        // 为避免 UI 混乱，请忽略值未更新的情况
        if (acc.name.isDefined && !updates.isZero) {
          stage.latestInfo.accumulables(id) = acc.toInfo(None, Some(acc.value))
          event.taskInfo.setAccumulables(
            acc.toInfo(Some(updates.value), Some(acc.value)) +: event.taskInfo.accumulables)
        }
      } catch {
        case NonFatal(e) =>
          // 记录类名以方便查找错误的实现
          val accumClassName = AccumulatorContext.get(id) match {
            case Some(accum) => accum.getClass.getName
            case None => "Unknown class"
          }
          logError(
            s"Failed to update accumulator $id ($accumClassName) for task ${task.partitionId}",
            e)
      }
    }
  }
```

### 案例一：收集一些文本类信息

下面这个累加器可以用于在程序运行过程中收集一些文本类信息，最终以`Set[String]`的形式返回

```scala

import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}
import scala.collection.JavaConversions._

class LogAccumulator extends org.apache.spark.util.AccumulatorV2[String, java.util.Set[String]] {
  private val _logArray: java.util.Set[String] = new java.util.HashSet[String]()

  override def isZero: Boolean = {
    _logArray.isEmpty
  }

  override def reset(): Unit = {
    _logArray.clear()
  }

  override def add(v: String): Unit = {
    _logArray.add(v)
  }

  override def merge(other: org.apache.spark.util.AccumulatorV2[String, java.util.Set[String]]): Unit = {
    other match {
      case o: LogAccumulator => _logArray.addAll(o.value)
    }

  }

  override def value: java.util.Set[String] = {
    java.util.Collections.unmodifiableSet(_logArray)
  }

  override def copy():org.apache.spark.util.AccumulatorV2[String, java.util.Set[String]] = {
    val newAcc = new LogAccumulator()
    _logArray.synchronized{
      newAcc._logArray.addAll(_logArray)
    }
    newAcc
  }
}

// 过滤掉带字母的
object LogAccumulator {
  def main(args: Array[String]) {
    val conf=new SparkConf().setAppName("LogAccumulator")
    val sc=new SparkContext(conf)

    val accum = new LogAccumulator
    sc.register(accum, "logAccum")
    val sum = sc.parallelize(Array("1", "2a", "3", "4b", "5", "6", "7cd", "8", "9"), 2).filter(line => {
      val pattern = """^-?(\d+)"""
      val flag = line.matches(pattern)
      if (!flag) {
        accum.add(line)
      }
      flag
    }).map(_.toInt).reduce(_ + _)

    println("sum: " + sum)
    for (v <- accum.value) print(v + "")
    println()
    sc.stop()
  }
}
```

### 案例二：统计一批csv文件中的记录数

```scala

package spark

import org.apache.spark.util.AccumulatorV2

import scala.collection.mutable

class FileAccumulator extends AccumulatorV2[String,mutable.HashMap[String,Long]]{
  var result = new mutable.HashMap[String,mutable.HashMap[String,Long]]()
  override def isZero: Boolean = result.isEmpty

  override def copy(): AccumulatorV2[String, mutable.HashMap[String, Long]] = {
    val copy = new FileAccumulator
    copy.merge(this)
    copy
  }

  override def reset(): Unit = result.clear()

  override def add(v: String): Unit = {
    result.synchronized({
      result.get(v) match{
        case Some(value) => result.get(v).get.put(v,value.get(v).get+1)
        case _ => {
          var tmp = new mutable.HashMap[String,Long]()
          tmp.put(v,1L)
          result.put(v,tmp)
        }
      }
    })
  }

  override def merge(other: AccumulatorV2[String, mutable.HashMap[String, Long]]): Unit = {
    other match {
      case o:FileAccumulator => {
        o.result.foreach(e=>{
          if(result.contains(e._1)){
            result
          }else{
            result.put(e._1,e._2)
          }
        })
      }
      case _ =>{
        throw new RuntimeException("no match accumulator")
      }
    }
  }

  override def value: mutable.HashMap[String, Long] = {
    if(result.isEmpty){
      null
    }else{
      result.iterator.next()._2
    }
  }
}
```

### 案例二：测试

```scala

package spark
import org.apache.spark.sql._
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, SparkSession}

case class Family(id: Long, name: String, age: Int)

object SparkAccumulator {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder
      .master("local[2]")
      .appName("Word Count")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()
    val fileAccumulator = new FileAccumulator
    sparkSession.sparkContext.register(fileAccumulator)
    import sparkSession.implicits._
    val dataFrame: DataFrame = sparkSession.read.csv("/Users/chenyuedong/Desktop/IT/源码/myspark/test_data")
    val schema: StructType = StructType(
      StructField("fileName", StringType, true)
      ::StructField("id", StringType, true)
        :: StructField("name", StringType, true)
        :: StructField("age", StringType, true)
        :: Nil
    )
    val encoder = RowEncoder.apply(schema)
    var dataset = dataFrame.map(record => {
      Row(record.getString(0),record.getString(1),record.getString(2),record.getString(3))
    })(encoder)
    dataset = dataset.map(row=>{
      fileAccumulator.add(row.getString(0))
      row
    })(encoder)
    dataset.show()
    dataset.count()
    var sum = 0L
    for(e<-fileAccumulator.result){
      sum=sum+e._2.get(e._1).get
    }
    System.out.println(sum)
  }
}
```

### 案例三：自定义累加器实现wordcount

```scala
package com.zxl.spark.core

import org.apache.spark.rdd.RDD
import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

/**
 * 自定义累加器
 * */
object AccumulatorDemo2 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[4]").setAppName("AccumulatorDemo2")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[String] = sc.makeRDD(List("hello", "zxl", "spark", "hello"))
    val wordCountAccumulator: WordCountAccumulator = new WordCountAccumulator()
    sc.register(wordCountAccumulator,"wcAcc")
    rdd.foreach(
      word=>{
        wordCountAccumulator.add(word)
      }
    )
    println(wordCountAccumulator.value)
  }

}

// 自定义累加器
// 1. 继承 AccumulatorV2，并设定泛型
// 2. 重写累加器的抽象方法
class WordCountAccumulator extends AccumulatorV2[String, mutable.Map[String,Long]]{
  var map : mutable.Map[String, Long] = mutable.Map()
  // 累加器是否为初始状态
  override def isZero: Boolean = {
    map.isEmpty
  }
  // 复制累加器
  override def copy(): AccumulatorV2[String, mutable.Map[String, Long]] = {
    new WordCountAccumulator
  }
  // 重置累加器
  override def reset(): Unit = {
    map.clear()
  }
  // 向累加器中增加数据 (In)
  override def add(word: String): Unit = {
    // 查询 map 中是否存在相同的单词
    // 如果有相同的单词，那么单词的数量加 1
    // 如果没有相同的单词，那么在 map 中增加这个单词
    map(word) = map.getOrElse(word, 0L) + 1L
  }
  // 合并累加器
  override def merge(other: AccumulatorV2[String, mutable.Map[String, Long]]):
  Unit = {
    val map1 = map
    val map2 = other.value
    // 两个 Map 的合并
    map = map1.foldLeft(map2)(
      ( innerMap, kv ) => {
        innerMap(kv._1) = innerMap.getOrElse(kv._1, 0L) + kv._2
        innerMap
      }
    ) }
  // 返回累加器的结果 （Out）
  override def value: mutable.Map[String, Long] = map
}
```

运行结果

```s
Map(spark -> 1, zxl -> 1, hello -> 2)
```

## 广播变量（调优策略）

```scala
// 通过对一个类型 T 的对象调用 SparkContext.broadcast 
// 创建出一个 Broadcast[T] 对象。任何可序列化的类型都可以这么实现。
scala> val broadcastVar = sc.broadcast(Array(1, 2, 3))
// 通过 value 属性访问该对象的值(在 Java 中为 value() 方法)。
scala> broadcastVar.value
// 变量只会被发到各个节点一次，
// 应作为只读值处理(修改这个值不会影响到别的节点)。
```

```scala
scala> val broadcastVar = sc.broadcast(Array(1, 2, 3))
broadcastVar: org.apache.spark.broadcast.Broadcast[Array[Int]] = Broadcast(35)

scala> broadcastVar.value
res: Array[Int] = Array(1, 2, 3)
```
