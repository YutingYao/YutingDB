<!-- vscode-markdown-toc -->
* 1. [Flink 程序剖析](#Flink)
* 2. [Data Sources](#DataSources)
* 3. [DataStream Transformations 算子](#DataStreamTransformations)
	* 3.1. [数据流转换](#)
		* 3.1.1. [Map - 数据流 → 数据流](#Map-)
		* 3.1.2. [FlatMap - 数据流 → 数据流](#FlatMap-)
		* 3.1.3. [Filter - 数据流 → 数据流](#Filter-)
		* 3.1.4. [KeyBy - 数据流 → KeyedStream](#KeyBy-KeyedStream)
		* 3.1.5. [Reduce - KeyedStream → 数据流](#Reduce-KeyedStream)
		* 3.1.6. [Window - KeyedStream → WindowedStream](#Window-KeyedStreamWindowedStream)
		* 3.1.7. [WindowAll - 数据流 → AllWindowedStream](#WindowAll-AllWindowedStream)
		* 3.1.8. [Window Apply - WindowedStream/AllWindowedStream → 数据流](#WindowApply-WindowedStreamAllWindowedStream)
		* 3.1.9. [WindowReduce - WindowedStream → 数据流](#WindowReduce-WindowedStream)
		* 3.1.10. [Union - n* 数据流 → 数据流](#Union-n)
		* 3.1.11. [Interval Join - 2* KeyedStream → 数据流](#IntervalJoin-2KeyedStream)
		* 3.1.12. [Window Join - 2* 数据流 → 数据流](#WindowJoin-2)
		* 3.1.13. [Window CoGroup - 2* 数据流 → 数据流](#WindowCoGroup-2)
		* 3.1.14. [Connect - 2* 数据流 → ConnectedStream](#Connect-2ConnectedStream)
		* 3.1.15. [CoMap, CoFlatMap - ConnectedStream → 数据流](#CoMapCoFlatMap-ConnectedStream)
		* 3.1.16. [Iterate - 数据流 → IterativeStream → ConnectedStream](#Iterate-IterativeStreamConnectedStream)
	* 3.2. [物理分区](#-1)
		* 3.2.1. [Custom Partitioning - 数据流 → 数据流](#CustomPartitioning-)
		* 3.2.2. [Random Partitioning - 数据流 → 数据流](#RandomPartitioning-)
		* 3.2.3. [Rescaling - 数据流 → 数据流](#Rescaling-)
		* 3.2.4. [Broadcasting - 数据流 → 数据流](#Broadcasting-)
	* 3.3. [算子链和资源组](#-1)
		* 3.3.1. [startNewChain 开始新链](#startNewChain)
		* 3.3.2. [disableChaining 禁用链](#disableChaining)
		* 3.3.3. [slotSharingGroup 设置槽位共享组](#slotSharingGroup)
* 4. [Data Sinks](#DataSinks)
* 5. [Iterations](#Iterations)
* 6. [控制延迟-env.setBufferTimeout(timeoutMillis)](#-env.setBufferTimeouttimeoutMillis)
* 7. [测试环境](#-1)
	* 7.1. [本地执行环境](#-1)
	* 7.2. [集合 Data Sources](#DataSources-1)
	* 7.3. [迭代器 Data Sink](#DataSink)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->
##  1. <a name='Flink'></a>Flink 程序剖析 

Java DataStream API 的所有核心类都可以在 [org.apache.flink.streaming.api.scala](https://github.com/apache/flink/tree/release-1.14/flink-streaming-scala/src/main/scala/org/apache/flink/streaming/api/scala) 中找到。

如下是一个完整的、可运行的程序示例，它是基于`流窗口`的`单词统计应用程序`，计算 `5 秒窗口`内来自 `Web 套接字`的`单词数`。你可以复制并粘贴代码以在本地运行。

```scala
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object WindowWordCount {
  def main(args: Array[String]) {
    // getExecutionEnvironment()
    // createLocalEnvironment()
    // createRemoteEnvironment(host: String, port: Int, jarFiles: String*)

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    // 你可以直接逐行读取数据，像读 CSV 文件一样，
    // 或使用任何第三方提供的 source。
    val text = env.socketTextStream("localhost", 9999)

    val counts = text.flatMap { _.toLowerCase.split("\\W+") filter { _.nonEmpty } }
      // 你可以调用 DataStream 上具有转换功能的方法来应用转换。
      // 例如，一个 map 的转换如下所示：
      .map { (_, 1) }
      .keyBy(_._1)
      .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
      .sum(1)
    // 一旦你有了包含最终结果的 DataStream
    // 你就可以通过创建 sink 把它写到外部系统。下面是一些用于创建 sink 的示例方法：
    // writeAsText(path: String)
    // print()
    counts.print()
    //一旦指定了完整的程序
    //调用 StreamExecutionEnvironment 的 execute() 方法来触发程序执行
    env.execute("Window Stream WordCount")
  }
}
```

要运行示例程序，首先从终端使用 netcat 启动输入流：

```scala
nc -lk 9999
```

只需输入一些单词，然后按回车键即可传入新单词。这些将作为单词统计程序的输入。如果想查看大于 1 的计数，在 5 秒内重复输入相同的单词即可（如果无法快速输入，则可以将窗口大小从 5 秒增加 ☺）。

```scala
val env = StreamExecutionEnvironment.getExecutionEnvironment()
val env = StreamExecutionEnvironment.getExecutionEnvironment
val text: DataStream[String] = env.readTextFile("file:///path/to/file")
val input: DataSet[String] = ...
val text = env.socketTextStream("localhost", 9999)
val mapped = input.map { x => x.toInt }
val mapped = input.map { (_, 1) }
writeAsText(path: String)
mapped.print()
print()
```

##  2. <a name='DataSources'></a>Data Sources

🤞 基于文件：

```scala
readTextFile(path) 

- 读取文本文件，例如遵守 TextInputFormat 规范的文件，逐行读取并将它们作为字符串返回。

readFile(fileInputFormat, path) 

- 按照指定的文件输入格式读取（一次）文件。

readFile(fileInputFormat, path, watchType, interval, pathFilter, typeInfo) 

- 这是前两个方法内部调用的方法。它基于给定的 fileInputFormat 读取路径 path 上的文件。根据提供的 watchType 的不同，source 可能定期（每 interval 毫秒）监控路径上的新数据（watchType 为 FileProcessingMode.PROCESS_CONTINUOUSLY），或者处理一次当前路径中的数据然后退出（watchType 为 FileProcessingMode.PROCESS_ONCE)。使用 pathFilter，用户可以进一步排除正在处理的文件。


🤞 基于套接字：

socketTextStream 

- 从套接字读取。元素可以由分隔符分隔。


🤞 基于集合：

fromCollection(Collection) 

- 从 Java Java.util.Collection 创建数据流。集合中的所有元素必须属于同一类型。

fromCollection(Iterator, Class) 

- 从迭代器创建数据流。class 参数指定迭代器返回元素的数据类型。

fromElements(T ...) 

- 从给定的对象序列中创建数据流。所有的对象必须属于同一类型。

fromParallelCollection(SplittableIterator, Class) 

- 从迭代器并行创建数据流。class 参数指定迭代器返回元素的数据类型。

generateSequence(from, to) 

- 基于给定间隔内的数字序列并行生成数据流。


🤞 自定义：

addSource 

- 关联一个新的 source function。例如，你可以使用 addSource(new FlinkKafkaConsumer<>(...)) 来从 Apache Kafka 获取数据。更多详细信息见连接器。
```

##  3. <a name='DataStreamTransformations'></a>DataStream Transformations 算子

###  3.1. <a name=''></a>数据流转换

####  3.1.1. <a name='Map-'></a>Map - 数据流 → 数据流

获取一个元素并生成一个元素。

将输入流的值加倍的map函数:

```scala
dataStream.map { x => x * 2 }
```

####  3.1.2. <a name='FlatMap-'></a>FlatMap - 数据流 → 数据流

获取一个元素并生成零个、一个或多个元素。

将句子拆分为单词的flatmap函数：

```scala
dataStream.flatMap { str => str.split(" ") }
```

####  3.1.3. <a name='Filter-'></a>Filter - 数据流 → 数据流

为每个元素计算一个布尔函数，并保留那些函数返回true的元素。

过滤掉零值的过滤器:

```scala
dataStream.filter { _ != 0 }
```

####  3.1.4. <a name='KeyBy-KeyedStream'></a>KeyBy - 数据流 → KeyedStream

在逻辑上将流划分为不相连的分区。所有具有相同键的记录都被分配到相同的分区。

在内部，`keyBy()`是通过`哈希分区`实现的。

有不同的方法来指定键。

```scala
dataStream.keyBy(_.someKey)
dataStream.keyBy(_._1)
```

####  3.1.5. <a name='Reduce-KeyedStream'></a>Reduce - KeyedStream → 数据流

键控数据流上的“滚动”减少。将当前元素与上次减少的值合并，并发出新值。

创建部分和流的reduce函数：

```scala
keyedStream.reduce { _ + _ }
```

在以下情况下，类型不能是`键`：

- 它是`POJO类型`，但不重写`hashCode（）方法`，并依赖于`Object.hashCode（）实现`。

- 它是任何类型的`数组`。

####  3.1.6. <a name='Window-KeyedStreamWindowedStream'></a>Window - KeyedStream → WindowedStream

可以在已分区的KeyedStreams上定义窗口。Windows根据某些特征（例如，在最后5秒内到达的数据）对每个键中的数据进行分组

```scala
dataStream
  .keyBy(_._1)
  .window(TumblingEventTimeWindows.of(Time.seconds(5))) 
```

####  3.1.7. <a name='WindowAll-AllWindowedStream'></a>WindowAll - 数据流 → AllWindowedStream

可以在常规数据流上定义Windows。Windows根据某些特征(例如，最近5秒内到达的数据)对所有流事件进行分组。

在很多情况下，这是一个非平行变换。所有记录将被收集到一个任务中，供windowwall操作人员使用。

```scala
dataStream
  .windowAll(TumblingEventTimeWindows.of(Time.seconds(5)))
```

####  3.1.8. <a name='WindowApply-WindowedStreamAllWindowedStream'></a>Window Apply - WindowedStream/AllWindowedStream → 数据流

将一个通用函数作为一个整体应用于窗口。下面是一个手动对窗口元素求和的函数。

如果你正在使用windowwall转换，你需要使用AllWindowFunction代替。

```scala
windowedStream.apply { WindowFunction }

// applying an AllWindowFunction on non-keyed window stream
allWindowedStream.apply { AllWindowFunction }
```

####  3.1.9. <a name='WindowReduce-WindowedStream'></a>WindowReduce - WindowedStream → 数据流

对窗口应用函数reduce函数并返回减少后的值。

```scala
windowedStream.reduce { _ + _ }
```

####  3.1.10. <a name='Union-n'></a>Union - n* 数据流 → 数据流

两个或多个数据流的合并，创建一个包含所有流中所有元素的新流

注意:如果你把一个数据流和它自己联合起来，你会在结果流中得到每个元素两次。

```scala
dataStream.union(otherStream1, otherStream2, ...);
```

####  3.1.11. <a name='IntervalJoin-2KeyedStream'></a>Interval Join - 2* KeyedStream → 数据流

在给定的时间间隔内用一个公共键连接两个键流中的两个元素e1和e2，这样:

```scala
e1.timestamp + lowerBound <= e2.timestamp <= e1.timestamp + upperBound.
```

```scala
// this will join the two streams so that
// key1 == key2 && leftTs - 2 < rightTs < leftTs + 2
keyedStream.intervalJoin(otherKeyedStream)
    .between(Time.milliseconds(-2), Time.milliseconds(2)) 
    // lower and upper bound
    .upperBoundExclusive(true) // optional
    .lowerBoundExclusive(true) // optional
    .process(new IntervalJoinFunction() {...})
```

####  3.1.12. <a name='WindowJoin-2'></a>Window Join - 2* 数据流 → 数据流

在给定的键和公共窗口上连接两个数据流。

```scala
dataStream.join(otherStream)
    .where(<key selector>).equalTo(<key selector>)
    .window(TumblingEventTimeWindows.of(Time.seconds(3)))
    .apply { ... }
```

####  3.1.13. <a name='WindowCoGroup-2'></a>Window CoGroup - 2* 数据流 → 数据流

将给定键和公共窗口上的两个数据流合并。

```scala
dataStream.coGroup(otherStream)
    .where(0).equalTo(1)
    .window(TumblingEventTimeWindows.of(Time.seconds(3)))
    .apply {}
```

####  3.1.14. <a name='Connect-2ConnectedStream'></a>Connect - 2* 数据流 → ConnectedStream

“连接”两个`保留其类型`的数据流。允许两个流之间共享`状态`的连接

```scala
someStream : DataStream[Int] = ...
otherStream : DataStream[String] = ...

val connectedStreams = someStream.connect(otherStream)
```

####  3.1.15. <a name='CoMapCoFlatMap-ConnectedStream'></a>CoMap, CoFlatMap - ConnectedStream → 数据流

类似于连接数据流上的map和flatMap

```scala
connectedStreams.map(
    (_ : Int) => true,
    (_ : String) => false
)
connectedStreams.flatMap(
    (_ : Int) => true,
    (_ : String) => false
)
```

####  3.1.16. <a name='Iterate-IterativeStreamConnectedStream'></a>Iterate - 数据流 → IterativeStream → ConnectedStream

```scala
initialStream.iterate {
  iteration => {
    val iterationBody = iteration.map {/*do something*/}
    (iterationBody.filter(_ > 0), iterationBody.filter(_ <= 0))
  }
}
```

###  3.2. <a name='-1'></a>物理分区

####  3.2.1. <a name='CustomPartitioning-'></a>Custom Partitioning - 数据流 → 数据流

使用用户定义的Partitioner为每个元素选择目标任务。

```scala
dataStream.partitionCustom(partitioner, "someKey")
dataStream.partitionCustom(partitioner, 0)
```

####  3.2.2. <a name='RandomPartitioning-'></a>Random Partitioning - 数据流 → 数据流

根据均匀分布随机划分元素

```scala
dataStream.shuffle()
```

####  3.2.3. <a name='Rescaling-'></a>Rescaling - 数据流 → 数据流 

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.23suieh256g0.png)

```scala
dataStream.rescale()
```

####  3.2.4. <a name='Broadcasting-'></a>Broadcasting - 数据流 → 数据流 

将元素广播到每个分区。

```scala
dataStream.broadcast()
```

###  3.3. <a name='-1'></a>算子链和资源组

将两个算子链接在一起能使得它们在同一个线程中执行

(例如， 两个 map 转换操作)

需要注意的是: 

- 这些方法只能在 DataStream 转换操作后才能被调用，
- 因为它们只对前一次数据转换生效。
  - 例如，可以 `someStream.map(...).startNewChain() `这样调用，
  - 而不能 `someStream.startNewChain()`这样。

####  3.3.1. <a name='startNewChain'></a>startNewChain 开始新链

```scala
someStream.filter(...).map(...).startNewChain().map(...)
```

####  3.3.2. <a name='disableChaining'></a>disableChaining 禁用链

```scala
someStream.map(...).disableChaining()
```

####  3.3.3. <a name='slotSharingGroup'></a>slotSharingGroup 设置槽位共享组

```scala
someStream.filter(...).slotSharingGroup("name")
```

##  4. <a name='DataSinks'></a>Data Sinks

```scala
writeAsText() / TextOutputFormat 

- 将元素按行写成字符串。通过调用每个元素的 toString() 方法获得字符串。

writeAsCsv(...) / CsvOutputFormat 

- 将元组写成逗号分隔值文件。行和字段的分隔符是可配置的。每个字段的值来自对象的 toString() 方法。

print() / printToErr() 

- 在标准输出/标准错误流上打印每个元素的 toString() 值。 可选地，可以提供一个前缀（msg）附加到输出。这有助于区分不同的 print 调用。如果并行度大于1，输出结果将附带输出任务标识符的前缀。

writeUsingOutputFormat() / FileOutputFormat 

- 自定义文件输出的方法和基类。支持自定义 object 到 byte 的转换。

writeToSocket 

- 根据 SerializationSchema 将元素写入套接字。

addSink 

- 调用自定义 sink function。Flink 捆绑了连接到其他系统（例如 Apache Kafka）的连接器，这些连接器被实现为 sink functions。
```

##  5. <a name='Iterations'></a>Iterations

由于 DataStream 程序可能永远不会完成，因此没有最大迭代次数。

例如，下面的程序从一系列整数中连续减去 1，直到它们达到零：

```scala
val someIntegers: DataStream[Long] = env.generateSequence(0, 1000)

val iteratedStream = someIntegers.iterate(
  iteration => {
    // 你需要指定流的哪一部分反馈给迭代
    val minusOne = iteration.map( v => v - 1)
    // 哪一部分使用旁路输出或过滤器转发到下游
    val stillGreaterThanZero = minusOne.filter (_ > 0)
    val lessThanZero = minusOne.filter(_ <= 0)
    (stillGreaterThanZero, lessThanZero)
  }
)
```

其中：

```scala
val iteratedStream = someDataStream自定义名称.iterate(
  iteration => {
    val iterationBody自定义名称 = iteration.map(/* 这被执行了很多次-自定义 */)
    (iterationBody自定义名称.filter(/* one part of the stream -自定义*/), 
    iterationBody自定义名称.filter(/* some other part of the stream -自定义*/))
})
```

##  6. <a name='-env.setBufferTimeouttimeoutMillis'></a>控制延迟-env.setBufferTimeout(timeoutMillis)

超过此时间后，即使缓冲区没有未满，也会被自动发送。

超时时间的默认值: 100 毫秒。

```scala
val env: LocalStreamEnvironment = StreamExecutionEnvironment.createLocalEnvironment
env.setBufferTimeout(timeoutMillis)

env.generateSequence(1,10).map(myMap).setBufferTimeout(timeoutMillis)
```

##  7. <a name='-1'></a>测试环境

实现`数据分析程序`通常是一个`检查结果`、`调试`和`改进`的增量过程。

###  7.1. <a name='-1'></a>本地执行环境

```scala
val env = StreamExecutionEnvironment.createLocalEnvironment()

val lines = env.addSource(/* some source */)
// 构建你的程序

env.execute()
```

###  7.2. <a name='DataSources-1'></a>集合 Data Sources

Flink 提供了由 Java 集合支持的特殊 data sources 以简化测试。

可以按如下方式使用集合 Data Sources：

```scala
val env = StreamExecutionEnvironment.createLocalEnvironment()

// 从元素列表创建一个 DataStream
val myInts = env.fromElements(1, 2, 3, 4, 5)

// 从任何 Java 集合创建一个 DataStream
val data: Seq[(String, Int)] = ...
val myTuples = env.fromCollection(data)

// 从迭代器创建一个 DataStream
val longIt: Iterator[Long] = ...
val myLongs = env.fromCollection(longIt)
```

###  7.3. <a name='DataSink'></a>迭代器 Data Sink

Flink  提供了一个 sink 来收集 DataStream 的结果，它用于`测试和调试`目的

```scala
import org.apache.flink.streaming.experimental.DataStreamUtils
import scala.collection.JavaConverters.asScalaIteratorConverter

val myResult: DataStream[(String, Int)] = ...
val myOutput: Iterator[(String, Int)] = DataStreamUtils.collect(myResult.javaStream).asScala
```