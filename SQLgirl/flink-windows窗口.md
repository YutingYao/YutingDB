<!-- vscode-markdown-toc -->
* 1. [WindowAssigner窗口分配器，用来确定哪些数据被分配到哪些窗口。](#WindowAssigner)
	* 1.1. [窗口处理的 Flink 程序一般结构 Keyed Windows + Non-Keyed Windows](#FlinkKeyedWindowsNon-KeyedWindows)
	* 1.2. [Allowed Lateness 允许迟到](#AllowedLateness)
	* 1.3. [获取 late data 作为 side output](#latedatasideoutput)
	* 1.4. [连续窗口操作](#)
	* 1.5. [窗口生命周期](#-1)
	* 1.6. [窗口划分的源码](#-1)
	* 1.7. [内置 WindowAssigner](#WindowAssigner-1)
	* 1.8. [一张经典图](#-1)
	* 1.9. [通过size和interval组合可以得出四种基本窗口](#sizeinterval)
	* 1.10. [滚动窗口 Tumbling Time Window](#TumblingTimeWindow)
	* 1.11. [滚动窗口 Tumbling Count Window](#TumblingCountWindow)
	* 1.12. [滑动窗口 Sliding Time Window](#SlidingTimeWindow)
	* 1.13. [会话窗口Session Window](#SessionWindow)
* 2. [WindowFunction窗口函数，用来对窗口内的数据做计算](#WindowFunction)
	* 2.1. [ReduceFunction 对输入的两个相同类型的元素按照指定的计算逻辑进行集合](#ReduceFunction)
	* 2.2. [AggregateFunction 增量聚合，用于 Window 的增量计算，减轻 Window 内 State 的存储压力。](#AggregateFunctionWindowWindowState)
	* 2.3. [ProcessWindowFunction 全量聚合，可以与 AggregateFunction 结合起来使用](#ProcessWindowFunctionAggregateFunction)
	* 2.4. [ReduceFunction 或 AggregateFunction 与 ProcessWindowFunction 组合](#ReduceFunctionAggregateFunctionProcessWindowFunction)
		* 2.4.1. [使用ReduceFunction进行增量聚合](#ReduceFunction-1)
		* 2.4.2. [ 使用AggregateFunction进行增量聚合](#AggregateFunction)
* 3. [Trigger 触发器,用来确定何时触发窗口的计算](#Trigger)
	* 3.1. [Trigger 示例：](#Trigger-1)
	* 3.2. [PurgingTrigger 的应用：](#PurgingTrigger)
	* 3.3. [DeltaTrigger 的应用：](#DeltaTrigger)
	* 3.4. [GlobalWindow + 触发 = 自定义 WindowAssigner](#GlobalWindowWindowAssigner)
	* 3.5. [自定义Trigger](#Trigger-1)
	* 3.6. [TriggerResult 有如下几种取值：](#TriggerResult)
	* 3.7. [内置触发器](#-1)
	* 3.8. [自定义触发器 - 继承并实现 Trigger 抽象类](#-Trigger)
* 4. [Evictor 清除器,对满足驱逐条件的数据做过滤](#Evictor)
	* 4.1. [内置 evictor](#evictor)
	* 4.2. [TimeEvictor 的应用1](#TimeEvictor1)
	* 4.3. [TimeEvictor 的应用2](#TimeEvictor2)
	* 4.4. [CountEvictor 的应用](#CountEvictor)
	* 4.5. [DeltaEvictor 的应用](#DeltaEvictor)
* 5. [时间语义](#-1)
	* 5.1. [水位线watermarks](#watermarks)
		* 5.1.1. [为什么要引入watermark](#watermark)
		* 5.1.2. [watermark策略](#watermark-1)
	* 5.2. [语法格式样例](#-1)
	* 5.3. [选择时间特性](#-1)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->



##  1. <a name='WindowAssigner'></a>WindowAssigner窗口分配器，用来确定哪些数据被分配到哪些窗口。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.8vj588z5eko.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.55dbcxtx6x40.png)

```scala
stream      
// 先指定时间戳和 Watermark 如何生成
  .assignTimestampsAndWatermarks(…)     <-    TimestampAssigner
//   选择需要聚合的维度的 Key
  .keyBy(...)                           <-    KeySelector  
//   选择一个窗口和选择用什么样的触发器来触发窗口计算     
  .window(...)                          <-    WindowAssigner        
  [.trigger(...)]                       <-    Trigger         
//   选择驱逐器做什么样的过滤
  [.evictor(...)]                       <-    Evictor
//   确定窗口应该做什么样计算。
  .reduce/aggregate/process()           <-    Aggregate/Window function
```

###  1.1. <a name='FlinkKeyedWindowsNon-KeyedWindows'></a>窗口处理的 Flink 程序一般结构 Keyed Windows + Non-Keyed Windows

(1) Keyed Windows：

```java
stream
       .keyBy(...)          <-  keyed versus non-keyed windows
       .window(...)         <-  required: "assigner"
      [.trigger(...)]       <-  optional: "trigger" (else default trigger)
      [.evictor(...)]       <-  optional: "evictor" (else no evictor)
      [.allowedLateness()]  <-  optional, else zero
       .reduce/fold/apply() <-  required: "function"
```

使用窗口我们要做的第一件事就是为你的数据流指定 key，必须在定义窗口之前完成。需要调用 keyBy() 方法将无限数据流拆分成 Keyed 数据流。

事件的任何属性都可以用作 key

通过多个并发任务来执行窗口计算，因为每个逻辑 Keyed 数据流可以独立于其它进行。有相同 key 的所有元素将被发送到相同的并发任务上。

(2) Non-Keyed Windows：

```java
stream
       .windowAll(...)      <-  required: "assigner"
      [.trigger(...)]       <-  optional: "trigger" (else default trigger)
      [.evictor(...)]       <-  optional: "evictor" (else no evictor)
      [.allowedLateness()]  <-  optional, else zero
       .reduce/fold/apply() <-  required: "function"
```

在非 Keyed 数据流中，原始数据流`不会被拆分`成多个逻辑 Keyd 数据流，并且所有窗口逻辑将由`单个任务执行`，即`并行度为1`。

区别是：

- Keyed Windows 在 Keyed 数据流上使用，Non-Keyed Windows 在非 Keyed 数据流上使用。
- 是否调用了 keyBy() 方法
- Keyed Windows 使用 window() 方法，Non-Keyed Windows 使用 windowAll() 方法。

###  1.2. <a name='AllowedLateness'></a>Allowed Lateness 允许迟到

默认情况下，允许的延迟设置为0。也就是说，到达水印后面的元素将被丢弃。

您可以这样指定允许的延迟时间：

> scala

```scala
val input: DataStream[T] = ...

input
    .keyBy(<key selector>)
    .window(<window assigner>)
    .allowedLateness(<time>)
    .<windowed transformation>(<window function>)
```

> java

```java
DataStream<T> input = ...;

input
    .keyBy(<key selector>)
    .window(<window assigner>)
    .allowedLateness(<time>)
    .<windowed transformation>(<window function>);
```

###  1.3. <a name='latedatasideoutput'></a>获取 late data 作为 side output

首先需要指定要使用窗口流上的sideOutputLateData（OutputTag）获取最新数据。然后，您可以根据窗口操作的结果获得侧面输出流：

> scala

```scala
val lateOutputTag = OutputTag[T]("late-data")

val input: DataStream[T] = ...

val result = input
    .keyBy(<key selector>)
    .window(<window assigner>)
    .allowedLateness(<time>)
    .sideOutputLateData(lateOutputTag)
    .<windowed transformation>(<window function>)

val lateStream = result.getSideOutput(lateOutputTag)
```

> java

```java
final OutputTag<T> lateOutputTag = new OutputTag<T>("late-data"){};

DataStream<T> input = ...;

SingleOutputStreamOperator<T> result = input
    .keyBy(<key selector>)
    .window(<window assigner>)
    .allowedLateness(<time>)
    .sideOutputLateData(lateOutputTag)
    .<windowed transformation>(<window function>);

DataStream<T> lateStream = result.getSideOutput(lateOutputTag);
```

###  1.4. <a name=''></a>连续窗口操作

允许将`连续加窗操作串`在一起。当你想要执行两个`连续的窗口操作`，你想使用`不同的键`，但仍然希望来自相同的`上游窗口`的元素最终在相同的`下游窗口`时，这是很有用的。考虑一下这个例子:

> scala

```scala
val input: DataStream[Int] = ...

val resultsPerKey = input
    .keyBy(<key selector>)
    .window(TumblingEventTimeWindows.of(Time.seconds(5)))
    .reduce(new Summer())

val globalResults = resultsPerKey
    .windowAll(TumblingEventTimeWindows.of(Time.seconds(5)))
    .process(new TopKWindowFunction())
```

> java

```java
DataStream<Integer> input = ...;

DataStream<Integer> resultsPerKey = input
    .keyBy(<key selector>)
    .window(TumblingEventTimeWindows.of(Time.seconds(5)))
    .reduce(new Summer());

DataStream<Integer> globalResults = resultsPerKey
    .windowAll(TumblingEventTimeWindows.of(Time.seconds(5)))
    .process(new TopKWindowFunction());
```

###  1.5. <a name='-1'></a>窗口生命周期

创建：

- 一旦属于这个窗口的第一个元素到达，就会创建该窗口

删除：

- 当时间(事件时间或处理时间)到达规定结束时间加上用户指定的可允许延迟的时间后，窗口将会被删除。

举个例子:

- 使用基于`事件时间`的`窗口策略`，每隔5分钟创建一个滚动窗口，并且允许可以有1分钟的延迟时间。

- 当`第一个带有时间戳的元素`位于 12:00 至 12:05 之间时，Flink `创建`一个 12:00 至 12:05 的新窗口，
  
- 当时间戳到达 12:06 时，窗口将被`删除`。Flink 仅保证对基于时间的窗口进行删除，**并不适用于其他类型的窗口，例如，全局窗口**。

每个窗口都有:

```s
一个触发器(Trigger)
    * 触发器决定了窗口什么时候调用该函数
    * 触发器还可以决定在什么时候清除窗口内容
         - 在这里，清除仅指清除窗口中的元素，
         - 而不是窗口（窗口元数据）。
         - 这意味着新数据仍然可以添加到窗口中。
    * 触发策略可能类似于
         - ”当窗口中元素个数大于4时” 
         - “当 watermark 到达窗口末尾时”
一个函数(例如 WindowFunction， ReduceFunction 或 FoldFunction)
    * 函数用于窗口的计算
```



###  1.6. <a name='-1'></a>窗口划分的源码

```java
/**
  * Method to get the window start for a timestamp.
  *
  * @param timestamp 进来的时间 (event_time)epoch millisecond to get the window start.
  * @param offset 窗口启动的偏移量 The offset which window start would be shifted by.
  * @param windowSize 设定的窗口大小 The size of the generated windows.
  * @return window start
*/
public static long getWindowStartWithOffset(long timestamp, long offset, long windowSize) {
 return timestamp - (timestamp - offset + windowSize) % windowSize;
}
```

例：

```s
第一次进来的时间为 

2021-11-06 20:13:00

按3分钟为窗口大小，offset为0，所以：

window_start = 13-（13-0+3）%3 =12

所以这条数据落到 

[2021-11-06 20:12:00 2021-11-06 20:15:00)

这个窗口内。
```

计算逻辑：

```java
window_start = 
   timestamp - (timestamp - offset + windowSize) % windowSize

window_end = window_start + windowSize

以左闭右开计算

[window_start,window_end)
```

窗口触发计算时机：

```s
watermark(水位线，包含延迟) > 窗口结束时间
```

###  1.7. <a name='WindowAssigner-1'></a>内置 WindowAssigner

- GlobalWindows
  
> Java版本:
  
```java
DataStream<T> input = ...;

input
    .keyBy(<key selector>)
    .window(GlobalWindows.create())
    .<windowed transformation>(<window function>);
```

> Scala版本:

```scala
val input: DataStream[T] = ...

input
    .keyBy(<key selector>)
    .window(GlobalWindows.create())
    .<windowed transformation>(<window function>)
```


- TumblingProcessingTimeWindows

- TumblingEventTimeWindows
  
> Java版本:
  
```java
DataStream<T> input = ...;

// 基于事件时间的滚动窗口
input
    .keyBy(<key selector>)
    .window(TumblingEventTimeWindows.of(Time.seconds(5)))
    .<windowed transformation>(<window function>);

// 基于处理时间的滚动窗口
input
    .keyBy(<key selector>)
    .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
    .<windowed transformation>(<window function>);

// 基于事件时间的每日滚动窗口会-8小时的偏移。
input
    .keyBy(<key selector>)
    .window(TumblingEventTimeWindows.of(Time.days(1), Time.hours(-8)))
    .<windowed transformation>(<window function>);
```

> Scala版本:

```scala
val input: DataStream[T] = ...

// tumbling event-time windows
input
    .keyBy(<key selector>)
    .window(TumblingEventTimeWindows.of(Time.seconds(5)))
    .<windowed transformation>(<window function>)

// tumbling processing-time windows
input
    .keyBy(<key selector>)
    .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
    .<windowed transformation>(<window function>)

// daily tumbling event-time windows offset by -8 hours.
input
    .keyBy(<key selector>)
    .window(TumblingEventTimeWindows.of(Time.days(1), Time.hours(-8)))
    .<windowed transformation>(<window function>)
```

- SlidingEventTimeWindows

- SlidingProcessingTimeWindows
  
> Java版本:
  
```java
DataStream<T> input = ...;

// 基于事件时间的滑动窗口
input
    .keyBy(<key selector>)
    .window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))
    .<windowed transformation>(<window function>);

// 基于处理时间的滑动窗口
input
    .keyBy(<key selector>)
    .window(SlidingProcessingTimeWindows.of(Time.seconds(10), Time.seconds(5)))
    .<windowed transformation>(<window function>);

// 基于处理时间的滑动窗口 偏移量-8
input
    .keyBy(<key selector>)
    .window(SlidingProcessingTimeWindows.of(Time.hours(12), Time.hours(1), Time.hours(-8)))
    .<windowed transformation>(<window function>);
```

> Scala版本:

```scala
val input: DataStream[T] = ...

// sliding event-time windows
input
    .keyBy(<key selector>)
    .window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))
    .<windowed transformation>(<window function>)

// sliding processing-time windows
input
    .keyBy(<key selector>)
    .window(SlidingProcessingTimeWindows.of(Time.seconds(10), Time.seconds(5)))
    .<windowed transformation>(<window function>)

// sliding processing-time windows offset by -8 hours
input
    .keyBy(<key selector>)
    .window(SlidingProcessingTimeWindows.of(Time.hours(12), Time.hours(1), Time.hours(-8)))
    .<windowed transformation>(<window function>)
```


- EventTimeSessionWindows: 基于事件时间可Merge的窗口分配处理

- ProcessingTimeSessionWindows: 基于处理时间可Merge的窗口分配处理
  
> Java版本:
  
```java
DataStream<T> input = ...;

// 基于事件时间的会话窗口
input
    .keyBy(<key selector>)
    .window(EventTimeSessionWindows.withGap(Time.minutes(10)))
    .<windowed transformation>(<window function>);

// 基于处理时间的会话窗口
input
    .keyBy(<key selector>)
    .window(ProcessingTimeSessionWindows.withGap(Time.minutes(10)))
    .<windowed transformation>(<window function>);
```

> Scala版本:

```scala
val input: DataStream[T] = ...

// event-time session windows
input
    .keyBy(<key selector>)
    .window(EventTimeSessionWindows.withGap(Time.minutes(10)))
    .<windowed transformation>(<window function>)

// processing-time session windows
input
    .keyBy(<key selector>)
    .window(ProcessingTimeSessionWindows.withGap(Time.minutes(10)))
    .<windowed transformation>(<window function>)
```


- MergingWindowAssigner: 内部定义了Window可以Merge的特性。一个抽象类，本身是一个WindowAssigner。

###  1.8. <a name='-1'></a>一张经典图

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1aqrko750oow.png)

###  1.9. <a name='sizeinterval'></a>通过size和interval组合可以得出四种基本窗口

- time-tumbling-window 无重叠数据的时间窗口，设置方式举例：

```scala
timeWindow(Time.seconds(5))
```

- time-sliding-window 有重叠数据的时间窗口，设置方式举例：

```scala
timeWindow(Time.seconds(5), Time.seconds(3))
```

- count-tumbling-window无重叠数据的数量窗口，设置方式举例：

```scala
countWindow(5)
```

- count-sliding-window 有重叠数据的数量窗口，设置方式举例：

```scala
countWindow(5,3)
```

###  1.10. <a name='TumblingTimeWindow'></a>滚动窗口 Tumbling Time Window

应用场景：👀 

- 假如我们需要统计`每一分钟`中用户购买的商品的总数，
需要将用户的行为事件按`每一分钟`进行切分，

- `翻滚窗口`能将数据流切分成不重叠的窗口，
- 每一个事件只能属于一个窗口。

```scala
// 用户id和购买数量 stream
val counts: DataStream[(Int, Int)] = ...
val tumblingCnts: DataStream[(Int, Int)] = counts
  // 用userId分组
  .keyBy(0) 
  // 1分钟的翻滚窗口宽度
  .timeWindow(Time.minutes(1))
  // 计算购买数量
  .sum(1) 
```

###  1.11. <a name='TumblingCountWindow'></a>滚动窗口 Tumbling Count Window

应用场景：👀

当我们想要每100个用户购买行为事件统计购买总数，那么每当窗口中`填满100个元素`了，就会对`窗口进行计算`

```scala
// Stream of (userId, buyCnts)
val buyCnts: DataStream[(Int, Int)] = ...

val tumblingCnts: DataStream[(Int, Int)] = buyCnts
  // key stream by sensorId
  .keyBy(0)
  // tumbling count window of 100 elements size
  .countWindow(100)
  // compute the buyCnt sum 
  .sum(1)
```

- 每当窗口中填满100个元素了，就会对窗口进行计算

###  1.12. <a name='SlidingTimeWindow'></a>滑动窗口 Sliding Time Window

link支持窗口的两个重要属性-（size和interval）：✨

- 如果size = interval,那么就会形成tumbling-window(无重叠数据)
- 如果size > interval,那么就会形成sliding-window(有重叠数据)
- 如果size < interval,那么这种窗口将会丢失数据。比如每5秒钟，统计过去3秒的通过路口汽车的数据，将会漏掉2秒钟的数据。

应用场景：👀

- 我们可以`每30秒`计算一次`最近一分钟`用户购买的商品总数。

- 在滑窗中，一个元素可以对应多个窗口。

```scala
val slidingCnts: DataStream[(Int, Int)] = buyCnts
  .keyBy(0) 
  .timeWindow(Time.minutes(1), Time.seconds(30))
  .sum(1)
```

###  1.13. <a name='SessionWindow'></a>会话窗口Session Window

应用场景：👀需要计算每个用户在`活跃期间`总共购买的商品数量，如果用户`30秒没有活动`则视为`会话断开`（假设raw data stream是单个用户的购买行为流）

```scala
// Stream of (userId, buyCnts)
val buyCnts: DataStream[(Int, Int)] = ...

val sessionCnts: DataStream[(Int, Int)] = vehicleCnts
    .keyBy(0)
    // 如果用户30秒没有活动则视为会话断开
    .window(ProcessingTimeSessionWindows.withGap(Time.seconds(30)))
    .sum(1)
****
```

##  2. <a name='WindowFunction'></a>WindowFunction窗口函数，用来对窗口内的数据做计算

###  2.1. <a name='ReduceFunction'></a>ReduceFunction 对输入的两个相同类型的元素按照指定的计算逻辑进行集合

ReduceFunction可以这样定义和使用:

> scala

```scala
val input: DataStream[(String, Long)] = ...

input
    .keyBy(<key selector>)
    .window(<window assigner>)
    .reduce { (v1, v2) => (v1._1, v1._2 + v2._2) }
```

> java

```java
DataStream<Tuple2<String, Long>> input = ...;

input
    .keyBy(<key selector>)
    .window(<window assigner>)
    .reduce(new ReduceFunction<Tuple2<String, Long>>() {
      public Tuple2<String, Long> reduce(Tuple2<String, Long> v1, Tuple2<String, Long> v2) {
        return new Tuple2<>(v1.f0, v1.f1 + v2.f1);
      }
    });
```

它接受两个相同类型的输入，生成一个输出，即两两合一地进行汇总操作，生成一个同类型的新元素。

多了一个窗口状态数据，这个状态数据的数据类型和输入的数据类型是一致的，是之前两两计算的中间结果数据。

Flink 使用 ReduceFunction 增量聚合窗口的元素。

当数据流中的新元素流入后，ReduceFunction将中间结果和新流入数据两两合一，生成新的数据替换之前的状态数据。

```scala
case class StockPrice(symbol: String, price: Double)
val input: DataStream[StockPrice] = ...
senv.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
// reduce的返回类型必须和输入类型StockPrice一致
val sum = input
      // 对symbol字段进行了keyBy
      // 相同symbol的数据都分组到了一起
      .keyBy(s => s.symbol)
      .timeWindow(Time.seconds(10))
      // 上面的代码使用Lambda表达式对两个元组进行操作
      // 接着我们将price加和，返回的结果必须也是StockPrice类型
      // 使用reduce的好处是窗口的状态数据量非常小
      .reduce((s1, s2) => StockPrice(s1.symbol, s1.price + s2.price))
```

如下代码所示，创建好 Window Assigner 之后通过在 reduce() 函数中指定 ReduceFunction 逻辑：

```java
DataStream<Tuple2<String, Integer>> wordsCount = ...;
DataStream<Tuple2<String, Integer>> result = wordsCount
        // 根据输入单词分组
        .keyBy(0)
        // 窗口大小为1秒的滚动窗口
        .timeWindow(Time.seconds(1))
        // ReduceFunction
        .reduce(new ReduceFunction<Tuple2<String, Integer>> (){
            @Override
            public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2) throws Exception {
                return new Tuple2(value1.f0, value1.f1 + value2.f1);
            }
        });
```

###  2.2. <a name='AggregateFunctionWindowWindowState'></a>AggregateFunction 增量聚合，用于 Window 的增量计算，减轻 Window 内 State 的存储压力。

AggregateFunction可以这样定义和使用：

> scala

```scala

/**
 * The accumulator is used to keep a running sum and a count. The [getResult] method
 * computes the average.
 */
class AverageAggregate extends AggregateFunction[(String, Long), (Long, Long), Double] {
  override def createAccumulator() = (0L, 0L)

  override def add(value: (String, Long), accumulator: (Long, Long)) =
    (accumulator._1 + value._2, accumulator._2 + 1L)

  override def getResult(accumulator: (Long, Long)) = accumulator._1 / accumulator._2

  override def merge(a: (Long, Long), b: (Long, Long)) =
    (a._1 + b._1, a._2 + b._2)
}

val input: DataStream[(String, Long)] = ...

input
    .keyBy(<key selector>)
    .window(<window assigner>)
    .aggregate(new AverageAggregate)
```

> java

```java
/**
 * The accumulator is used to keep a running sum and a count. The {@code getResult} method
 * computes the average.
 */
private static class AverageAggregate
    implements AggregateFunction<Tuple2<String, Long>, Tuple2<Long, Long>, Double> {
  @Override
  public Tuple2<Long, Long> createAccumulator() {
    return new Tuple2<>(0L, 0L);
  }

  @Override
  public Tuple2<Long, Long> add(Tuple2<String, Long> value, Tuple2<Long, Long> accumulator) {
    return new Tuple2<>(accumulator.f0 + value.f1, accumulator.f1 + 1L);
  }

  @Override
  public Double getResult(Tuple2<Long, Long> accumulator) {
    return ((double) accumulator.f0) / accumulator.f1;
  }

  @Override
  public Tuple2<Long, Long> merge(Tuple2<Long, Long> a, Tuple2<Long, Long> b) {
    return new Tuple2<>(a.f0 + b.f0, a.f1 + b.f1);
  }
}

DataStream<Tuple2<String, Long>> input = ...;

input
    .keyBy(<key selector>)
    .window(<window assigner>)
    .aggregate(new AverageAggregate());
```

它是高级别的抽象，主要用来做`增量聚合`，**每来一条元素都做一次聚合**，

这样`状态`里只需要存`最新的聚合值`。

优点：`增量聚合`，实现简单。
缺点：输出`只有一个聚合值`，使用场景比较局限。

如果是`增量计算`，使用的是 `AggregatingState`，`每条元素进来`会触发 `AggregateTransformation` 的计算。

AggregateTransformation 的实现: 

- 它会调用我们定义的 AgregateFunction 中的 `createAccumulator 方法`和 `add 方法`并将 `add 的结果返回`，所以 `State 中存储`的就是 `accumulator 的值`，所以比较`轻量级`。

AggregateFunction 接口中有三个参数：

- 输入元素类型（IN）
- 累加器类型（ACC）
- 输出元素类型（OUT）
- 这样复杂的设计主要是为了解决输入类型、中间状态和输出类型不一致的问题，同时ACC可以自定义，我们可以在ACC里构建我们想要的数据结构。

我们看一下它的源码定义：

```java
public interface AggregateFunction<IN, ACC, OUT> extends Function, Serializable {
   // 在一次新的aggregate发起时，创建一个新的Accumulator，Accumulator是我们所说的中间状态数据，简称ACC
   // 这个函数一般在初始化时调用
   ACC createAccumulator();
   // 当一个新元素流入时，将新元素与状态数据ACC合并，返回状态数据ACC
   ACC add(IN value, ACC accumulator);
  
   // 将两个ACC合并
   ACC merge(ACC a, ACC b);
   // 将中间数据转成结果数据
   OUT getResult(ACC accumulator);
}
```

比如我们要计算一个窗口内某个字段的平均值，那么ACC中要保存总和以及个数，下面是一个平均值的示例：

```scala
case class StockPrice(symbol: String, price: Double)
// IN: StockPrice
// ACC：(String, Double, Int) - (symbol, sum, count)
// OUT: (String, Double) - (symbol, average)
class AverageAggregate extends AggregateFunction[StockPrice, (String, Double, Int), (String, Double)] {
  override def createAccumulator() = ("", 0, 0)
  override def add(item: StockPrice, accumulator: (String, Double, Int)) =
  (item.symbol, accumulator._2 + item.price, accumulator._3 + 1)
  override def getResult(accumulator:(String, Double, Int)) = (accumulator._1 ,accumulator._2 / accumulator._3)
  override def merge(a: (String, Double, Int), b: (String, Double, Int)) =
  (a._1 ,a._2 + b._2, a._3 + b._3)
}
senv.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
val input: DataStream[StockPrice] = ...
val average = input
      .keyBy(s => s.symbol)
      .timeWindow(Time.seconds(10))
      .aggregate(new AverageAggregate)
```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3vv9nsvylfy0.png)

定义了四个需要重写的方法，其中:
- createAccumulator() -> 创建 accumulator，
- add()  -> 定义数据的添加逻辑，
- getResult  -> 根据 accumulator 计算结果的逻辑，
- merge()  -> 合并 accumulator 的逻辑

```java
public interface AggregateFunction<IN, ACC, OUT> extends Function, Serializable {
  // 创建 accumulator
  ACC createAccumulator();
  // 定义数据的添加逻辑
	ACC add(IN value, ACC accumulator);
  // 定义了根据 accumulator 计算结果的逻辑
	OUT getResult(ACC accumulator);
  // 定义了合并 accumulator 的逻辑
	ACC merge(ACC a, ACC b);
}
```

如下代码所示，自定义实现 AggregateFunction 函数实现分组求平均值的功能：

```java
DataStream<Tuple2<String, Double>> result = stream
        // 提取时间戳与设置Watermark
        .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<Tuple3<String, Long, Integer>>(Time.minutes(10)) {
            @Override
            public long extractTimestamp(Tuple3<String, Long, Integer> element) {
                return element.f1;
            }
        })
        // 格式转换
        .map(new MapFunction<Tuple3<String,Long,Integer>, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(Tuple3<String, Long, Integer> value) throws Exception {
                return new Tuple2<String, Integer>(value.f0, value.f2);
            }
        })
        // 分组
        .keyBy(new KeySelector<Tuple2<String, Integer>, String>() {
            @Override
            public String getKey(Tuple2<String, Integer> value) throws Exception {
                return value.f0;
            }
        })
        // 窗口大小为10分钟、滑动步长为5分钟的滑动窗口
        .timeWindow(Time.minutes(10), Time.minutes(5))
        .aggregate(new AverageAggregateFunction());

/**
 * 自定义AggregateFunction
 */
private static class AverageAggregateFunction implements AggregateFunction<Tuple2<String, Integer>, Tuple3<String, Long, Long>, Tuple2<String, Double>> {

    // IN：Tuple2<String, Long>
    // ACC：Tuple3<String, Long, Long> -> <Key, Sum, Count>
    // OUT：Tuple2<String, Double>

    @Override
    public Tuple3<String, Long, Long> createAccumulator() {
        return new Tuple3<String, Long, Long>("", 0L, 0L);
    }

    @Override
    public Tuple3<String, Long, Long> add(Tuple2<String, Integer> value, Tuple3<String, Long, Long> accumulator) {
        return new Tuple3<String, Long, Long>(value.f0, accumulator.f1 + value.f1, accumulator.f2 + 1L);
    }

    @Override
    public Tuple2<String, Double> getResult(Tuple3<String, Long, Long> accumulator) {
        return new Tuple2<String, Double>(accumulator.f0, ((double) accumulator.f1) / accumulator.f2);
    }

    @Override
    public Tuple3<String, Long, Long> merge(Tuple3<String, Long, Long> a, Tuple3<String, Long, Long> b) {
        return new Tuple3<String, Long, Long>(a.f0, a.f1 + b.f1, a.f2 + b.f2);
    }
}

```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3jgximd8vmg0.png)

###  2.3. <a name='ProcessWindowFunctionAggregateFunction'></a>ProcessWindowFunction 全量聚合，可以与 AggregateFunction 结合起来使用

ProcessWindowFunction可以这样定义和使用:

> scala

```scala
val input: DataStream[(String, Long)] = ...

input
  .keyBy(_._1)
  .window(TumblingEventTimeWindows.of(Time.minutes(5)))
  .process(new MyProcessWindowFunction())

/* ... */

class MyProcessWindowFunction extends ProcessWindowFunction[(String, Long), String, String, TimeWindow] {

  def process(key: String, context: Context, input: Iterable[(String, Long)], out: Collector[String]) = {
    var count = 0L
    for (in <- input) {
      count = count + 1
    }
    out.collect(s"Window ${context.window} count: $count")
  }
}
```

> java

```java
DataStream<Tuple2<String, Long>> input = ...;

input
  .keyBy(t -> t.f0)
  .window(TumblingEventTimeWindows.of(Time.minutes(5)))
  .process(new MyProcessWindowFunction());

/* ... */

public class MyProcessWindowFunction 
    extends ProcessWindowFunction<Tuple2<String, Long>, String, String, TimeWindow> {

  @Override
  public void process(String key, Context context, Iterable<Tuple2<String, Long>> input, Collector<String> out) {
    long count = 0;
    for (Tuple2<String, Long> in: input) {
      count++;
    }
    out.collect("Window: " + context.window() + "count: " + count);
  }
}
```

它是低级别的抽象用,来做`全量聚合`，**每来一条元素都存在状态里面**，

只有当窗口触发计算时才会调用这个函数。

优点：

- 可以获取到窗口内所有数据的`迭代器`，实现起来`比较灵活`；

- 可以获取到`聚合的 Key` 以及可以从`上下文 Context` 中获取窗口的相关信息。

缺点：

- 需要存储窗口内的`全量数据`，State 的压力较大。

如果是全量聚合，元素会添加到 `ListState` 当中，当触发`窗口计算`时，再把 `ListState 中所有元素`传递给`窗口函数`。

`统计更复杂的指标`可能还是需要依赖于窗口中的`所有的数据元素`，或者需要操作窗口中的`状态`和窗口`元数据`，这时就需要使用到 ProcessWindowFunction。

ProcessWindowFunction 会获得窗口内所有元素的 Iterable 以及一个可以访问时间和`状态`信息的 Context 对象

在实现 ProcessWindowFunction 接口中，如果不操作`状态`数据，那么只需要实现 `process() 方法`即可，该方法中定义了`评估窗口`和`具体数据输出`的逻辑。

如下代码所示，通过`自定义`实现 ProcessWindowFunction 完成基于窗口上的`分组统计`的功能，并输出`窗口结束时间`等`元数据`信息：

```java
DataStream result = stream
      // 提取时间戳与设置Watermark
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<Tuple3<String, Long, Integer>>(Time.minutes(10)) {
          @Override
          public long extractTimestamp(Tuple3<String, Long, Integer> element) {
              return element.f1;
          }
      })
      // 分组
      .keyBy(new KeySelector<Tuple3<String, Long, Integer>, String>() {

          @Override
          public String getKey(Tuple3<String, Long, Integer> value) throws Exception {
              return value.f0;
          }
      })
      // 窗口大小为10分钟、滑动步长为5分钟的滑动窗口
      .timeWindow(Time.minutes(10), Time.minutes(5))
      // 窗口函数
      .process(new MyProcessWindowFunction());

/**
 * 自定义实现 ProcessWindowFunction
 */
private static class MyProcessWindowFunction extends ProcessWindowFunction<Tuple3<String, Long, Integer>, String, String, TimeWindow> {
    @Override
    public void process(String key, Context context, Iterable<Tuple3<String, Long, Integer>> elements, Collector<String> out) throws Exception {
        long count = 0;
        List<String> list = Lists.newArrayList();
        for (Tuple3<String, Long, Integer> element : elements) {
            list.add(element.f0 + "|" + element.f1 + "|" + DateUtil.timeStamp2Date(element.f1, "yyyy-MM-dd HH:mm:ss"));
            Integer value = element.f2;
            count += value;
        }
        TimeWindow window = context.window();
        long start = window.getStart();
        long end = window.getEnd();
        String startTime = DateUtil.timeStamp2Date(start, "yyyy-MM-dd HH:mm:ss");
        String endTime = DateUtil.timeStamp2Date(end, "yyyy-MM-dd HH:mm:ss");
        long currentWatermark = context.currentWatermark();
        String currentWatermarkTime = DateUtil.timeStamp2Date(currentWatermark, "yyyy-MM-dd HH:mm:ss");
        long currentProcessingTimeStamp = context.currentProcessingTime();
        String currentProcessingTime = DateUtil.timeStamp2Date(currentProcessingTimeStamp, "yyyy-MM-dd HH:mm:ss");

        StringBuilder sb = new StringBuilder();
        sb.append("Key: " + list.toString());
        sb.append(", Window[" + startTime + ", " + endTime + "]");
        sb.append(", Count: " + count);
        sb.append(", CurrentWatermarkTime: " + currentWatermarkTime);
        sb.append(", CurrentProcessingTime: " + currentProcessingTime);
        LOG.info(sb.toString());
        out.collect(sb.toString());
    }
}
```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4g1cb584wwi0.png)

需要注意的是使用 ProcessWindowFunction 进行简单聚合（如count）的效率非常低

使用ProcessWindowFunction时，

- Flink将某个Key下某个窗口的所有元素都缓存在Iterable<IN>中
- 我们需要对其进行处理，然后用Collector<OUT>收集输出。
- 我们可以使用Context获取窗口内更多的信息，包括时间、状态、迟到数据发送位置等。

下面的代码是一个ProcessWindowFunction的简单应用，我们对`价格出现的次数做了统计`，选出出现次数最多的输出出来:

```scala
case class StockPrice(symbol: String, price: Double)
class FrequencyProcessFunction extends ProcessWindowFunction[StockPrice, (String, Double), String, TimeWindow] {
  override def process(key: String, context: Context, elements: Iterable[StockPrice], out: Collector[(String, Double)]): Unit = {
    // 股票价格和该价格出现的次数
    var countMap = scala.collection.mutable.Map[Double, Int]()
    for(element <- elements) {
      val count = countMap.getOrElse(element.price, 0)
      countMap(element.price) = count + 1
    }
    // 按照出现次数从高到低排序
    val sortedMap = countMap.toSeq.sortWith(_._2 > _._2)
    // 选出出现次数最高的输出到Collector
    if (sortedMap.size > 0) {
      out.collect((key, sortedMap(0)._1))
    }
  }
}
senv.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
val input: DataStream[StockPrice] = ...
val frequency = input
      .keyBy(s => s.symbol)
      .timeWindow(Time.seconds(10))
      .process(new FrequencyProcessFunction)
```

Context中有两种状态:

- 一种是针对Key的`全局状态`，
  - 它是跨多个窗口的，多个窗口都可以访问；
- 另一种是该Key下`单窗口的状态`，
  - `单窗口的状态`只保存该窗口的数据，
  - 主要是针对`process函数`多次被调用的场景，
  - 比如处理`迟到数据`或`自定义Trigger`等场景。
  - 当使用`单个窗口`的状态时，要在`clear函数`中清理状态。

###  2.4. <a name='ReduceFunctionAggregateFunctionProcessWindowFunction'></a>ReduceFunction 或 AggregateFunction 与 ProcessWindowFunction 组合

可以使用 ProcessWindowFunction 与 ReduceFunction 或者 AggregateFunction 等增量函数组合使用，以充分利用两种函数各自的优势。

元素`到达窗口`时对其使用 `ReduceFunction 或者 AggregateFunction 增量函数`进行`增量聚合`，

当`关闭窗口`时向 `ProcessWindowFunction` 提供`聚合结果`。这样我们就可以增量的`计算窗口`，同时还可以访问窗口的`元数据信息`。

下面的代码中，Lambda函数对所有内容进行最大值和最小值的处理，这一步是增量计算。计算的结果以数据类型(String, Double, Double)传递给WindowEndProcessFunction，WindowEndProcessFunction只需要将窗口结束的时间戳添加到结果MaxMinPrice中即可:

```scala
case class StockPrice(symbol: String, price: Double)
case class MaxMinPrice(symbol: String, max: Double, min: Double, windowEndTs: Long)
class WindowEndProcessFunction extends ProcessWindowFunction[(String, Double, Double), MaxMinPrice, String, TimeWindow] {
  override def process(key: String,
                       context: Context,
                       elements: Iterable[(String, Double, Double)],
                       out: Collector[MaxMinPrice]): Unit = {
    val maxMinItem = elements.head
    val windowEndTs = context.window.getEnd
    out.collect(MaxMinPrice(key, maxMinItem._2, maxMinItem._3, windowEndTs))
  }
}
senv.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
val input: DataStream[StockPrice] = ...
// reduce的返回类型必须和输入类型相同
// 为此我们将StockPrice拆成一个三元组 (股票代号，最大值、最小值)
val maxMin = input
.map(s => (s.symbol, s.price, s.price))
.keyBy(s => s._1)
.timeWindow(Time.seconds(10))
.reduce(
  ((s1: (String, Double, Double), s2: (String, Double, Double)) => (s1._1, Math.max(s1._2, s2._2), Math.min(s1._3, s2._3))),
  new WindowEndProcessFunction
)
```



####  2.4.1. <a name='ReduceFunction-1'></a>使用ReduceFunction进行增量聚合

下面的示例展示了如何将递增的ReduceFunction与ProcessWindowFunction组合起来，以返回窗口中最小的事件以及窗口的开始时间。

> scala

```scala

val input: DataStream[SensorReading] = ...

input
  .keyBy(<key selector>)
  .window(<window assigner>)
  .reduce(
    (r1: SensorReading, r2: SensorReading) => { if (r1.value > r2.value) r2 else r1 },
    ( key: String,
      context: ProcessWindowFunction[_, _, _, TimeWindow] # Context,
      minReadings: Iterable[SensorReading], 
      out: Collector[(Long, SensorReading)] ) =>
      {
        val min = minReadings.iterator.next()
        out.collect((context.window.getStart, min))
      }
  )

```

> java

```java
DataStream<SensorReading> input = ...;

input
  .keyBy(<key selector>)
  .window(<window assigner>)
  .reduce(new MyReduceFunction(), new MyProcessWindowFunction());

// Function definitions

private static class MyReduceFunction implements ReduceFunction<SensorReading> {

  public SensorReading reduce(SensorReading r1, SensorReading r2) {
      return r1.value() > r2.value() ? r2 : r1;
  }
}

private static class MyProcessWindowFunction
    extends ProcessWindowFunction<SensorReading, Tuple2<Long, SensorReading>, String, TimeWindow> {

  public void process(String key,
                    Context context,
                    Iterable<SensorReading> minReadings,
                    Collector<Tuple2<Long, SensorReading>> out) {
      SensorReading min = minReadings.iterator().next();
      out.collect(new Tuple2<Long, SensorReading>(context.window().getStart(), min));
  }
}

```

如下代码示例展示了如何将 ReduceFunction 增量函数与 ProcessWindowFunction 组合使用 ->

以返回 ->

- 窗口中的不同Key的求和

- 该窗口的开始时间

等窗口元信息：

```java
DataStream<Tuple2<ContextInfo, Tuple2<String, Integer>>> result = stream
      // 提取时间戳与设置Watermark
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<Tuple3<String, Long, Integer>>(Time.minutes(10)) {
          @Override
          public long extractTimestamp(Tuple3<String, Long, Integer> element) {
              return element.f1;
          }
      })
      // 格式转换
      .map(new MapFunction<Tuple3<String,Long,Integer>, Tuple2<String, Integer>>() {
          @Override
          public Tuple2<String, Integer> map(Tuple3<String, Long, Integer> value) throws Exception {
              return new Tuple2<String, Integer>(value.f0, value.f2);
          }
      })
      // 分组
      .keyBy(new KeySelector<Tuple2<String, Integer>, String>() {
          @Override
          public String getKey(Tuple2<String, Integer> value) throws Exception {
              return value.f0;
          }
      })
      // 窗口大小为10分钟、滑动步长为5分钟的滑动窗口
      .timeWindow(Time.minutes(10), Time.minutes(5))
      // ReduceFunction 相同单词将第二个字段求和
      .reduce(new MyReduceFunction(), new MyProcessWindowFunction());

/**
 * 自定义ReduceFunction：根据Key实现SUM
 */
private static class MyReduceFunction implements ReduceFunction<Tuple2<String, Integer>> {
    public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2) {
        return new Tuple2(value1.f0, value1.f1 + value2.f1);
    }
}

/**
 * 自定义ProcessWindowFunction：获取窗口元信息
 */
private static class MyProcessWindowFunction extends ProcessWindowFunction<Tuple2<String, Integer>, Tuple2<ContextInfo, Tuple2<String, Integer>>, String, TimeWindow> {
    @Override
    public void process(String key, Context context, Iterable<Tuple2<String, Integer>> elements, Collector<Tuple2<ContextInfo, Tuple2<String, Integer>>> out) throws Exception {
        Tuple2<String, Integer> tuple = elements.iterator().next();
        // 窗口元信息
        TimeWindow window = context.window();
        long start = window.getStart();
        long end = window.getEnd();
        String startTime = DateUtil.timeStamp2Date(start, "yyyy-MM-dd HH:mm:ss");
        String endTime = DateUtil.timeStamp2Date(end, "yyyy-MM-dd HH:mm:ss");
        long currentWatermark = context.currentWatermark();
        String currentWatermarkTime = DateUtil.timeStamp2Date(currentWatermark, "yyyy-MM-dd HH:mm:ss");
        long currentProcessingTimeStamp = context.currentProcessingTime();
        String currentProcessingTime = DateUtil.timeStamp2Date(currentProcessingTimeStamp, "yyyy-MM-dd HH:mm:ss");

        ContextInfo contextInfo = new ContextInfo();
        contextInfo.setKey(tuple.f0);
        contextInfo.setSum(tuple.f1);
        contextInfo.setWindowStartTime(startTime);
        contextInfo.setWindowEndTime(endTime);
        contextInfo.setCurrentWatermark(currentWatermarkTime);
        contextInfo.setCurrentProcessingTime(currentProcessingTime);
        LOG.info("[WINDOW] " + contextInfo.toString());
        // 输出
        out.collect(new Tuple2<ContextInfo, Tuple2<String, Integer>>(contextInfo, tuple));
    }
}
```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1jn2hvwkuv34.png)

####  2.4.2. <a name='AggregateFunction'></a> 使用AggregateFunction进行增量聚合

下面的示例展示了如何将增量AggregateFunction与ProcessWindowFunction组合起来计算平均值，并同时发出键和窗口。

> scala

```scala

val input: DataStream[(String, Long)] = ...

input
  .keyBy(<key selector>)
  .window(<window assigner>)
  .aggregate(new AverageAggregate(), new MyProcessWindowFunction())

// Function definitions

/**
 * The accumulator is used to keep a running sum and a count. The [getResult] method
 * computes the average.
 */
class AverageAggregate extends AggregateFunction[(String, Long), (Long, Long), Double] {
  override def createAccumulator() = (0L, 0L)

  override def add(value: (String, Long), accumulator: (Long, Long)) =
    (accumulator._1 + value._2, accumulator._2 + 1L)

  override def getResult(accumulator: (Long, Long)) = accumulator._1 / accumulator._2

  override def merge(a: (Long, Long), b: (Long, Long)) =
    (a._1 + b._1, a._2 + b._2)
}

class MyProcessWindowFunction extends ProcessWindowFunction[Double, (String, Double), String, TimeWindow] {

  def process(key: String, context: Context, averages: Iterable[Double], out: Collector[(String, Double)]) = {
    val average = averages.iterator.next()
    out.collect((key, average))
  }
}

```

> java

```java
DataStream<Tuple2<String, Long>> input = ...;

input
  .keyBy(<key selector>)
  .window(<window assigner>)
  .aggregate(new AverageAggregate(), new MyProcessWindowFunction());

// Function definitions

/**
 * The accumulator is used to keep a running sum and a count. The {@code getResult} method
 * computes the average.
 */
private static class AverageAggregate
    implements AggregateFunction<Tuple2<String, Long>, Tuple2<Long, Long>, Double> {
  @Override
  public Tuple2<Long, Long> createAccumulator() {
    return new Tuple2<>(0L, 0L);
  }

  @Override
  public Tuple2<Long, Long> add(Tuple2<String, Long> value, Tuple2<Long, Long> accumulator) {
    return new Tuple2<>(accumulator.f0 + value.f1, accumulator.f1 + 1L);
  }

  @Override
  public Double getResult(Tuple2<Long, Long> accumulator) {
    return ((double) accumulator.f0) / accumulator.f1;
  }

  @Override
  public Tuple2<Long, Long> merge(Tuple2<Long, Long> a, Tuple2<Long, Long> b) {
    return new Tuple2<>(a.f0 + b.f0, a.f1 + b.f1);
  }
}

private static class MyProcessWindowFunction
    extends ProcessWindowFunction<Double, Tuple2<String, Double>, String, TimeWindow> {

  public void process(String key,
                    Context context,
                    Iterable<Double> averages,
                    Collector<Tuple2<String, Double>> out) {
      Double average = averages.iterator().next();
      out.collect(new Tuple2<>(key, average));
  }
}
```

如下代码示例展示了如何将 AggregateFunction 增量函数与 ProcessWindowFunction 组合使用 ->

以返回 ->

- 窗口中的不同Key的平均值
- 该窗口的开始时间

等窗口元信息：

```java
DataStream<Tuple2<ContextInfo, Tuple3<Long, Long, Double>>> result = stream
          // 提取时间戳与设置Watermark
          .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<Tuple3<String, Long, Integer>>(Time.minutes(10)) {
              @Override
              public long extractTimestamp(Tuple3<String, Long, Integer> element) {
                  return element.f1;
              }
          })
          // 格式转换
          .map(new MapFunction<Tuple3<String,Long,Integer>, Tuple2<String, Integer>>() {
              @Override
              public Tuple2<String, Integer> map(Tuple3<String, Long, Integer> value) throws Exception {
                  return new Tuple2<String, Integer>(value.f0, value.f2);
              }
          })
          // 分组
          .keyBy(new KeySelector<Tuple2<String, Integer>, String>() {
              @Override
              public String getKey(Tuple2<String, Integer> value) throws Exception {
                  return value.f0;
              }
          })
          // 窗口大小为10分钟、滑动步长为5分钟的滑动窗口
          .timeWindow(Time.minutes(10), Time.minutes(5))
          // 分组求平均值
          .aggregate(new MyAggregateFunction(), new MyProcessWindowFunction());

/**
 * 自定义ReduceFunction：根据Key实现求平均数
 */
private static class MyAggregateFunction implements AggregateFunction<Tuple2<String, Integer>, Tuple2<Long, Long>, Tuple3<Long, Long, Double>> {

    // IN：Tuple2<String, Integer>
    // ACC：Tuple2<Long, Long> -> <Sum, Count>
    // OUT：Tuple3<Long, Long, Double>

    @Override
    public Tuple2<Long, Long> createAccumulator() {
        return new Tuple2<Long, Long>(0L, 0L);
    }

    @Override
    public Tuple2<Long, Long> add(Tuple2<String, Integer> value, Tuple2<Long, Long> accumulator) {
        return new Tuple2<Long, Long>(accumulator.f0 + value.f1, accumulator.f1 + 1L);
    }

    @Override
    public Tuple3<Long, Long, Double> getResult(Tuple2<Long, Long> accumulator) {
        return new Tuple3<>(accumulator.f0, accumulator.f1, ((double) accumulator.f0) / accumulator.f1);
    }

    @Override
    public Tuple2<Long, Long> merge(Tuple2<Long, Long> a, Tuple2<Long, Long> b) {
        return new Tuple2<Long, Long>(a.f0 + b.f0, a.f1 + b.f1);
    }
}

/**
 * 自定义ProcessWindowFunction：获取窗口元信息
 */
private static class MyProcessWindowFunction extends ProcessWindowFunction<Tuple3<Long, Long, Double>, Tuple2<ContextInfo, Tuple3<Long, Long, Double>>, String, TimeWindow> {
    @Override
    public void process(String key, Context context, Iterable<Tuple3<Long, Long, Double>> elements, Collector<Tuple2<ContextInfo, Tuple3<Long, Long, Double>>> out) throws Exception {
        Tuple3<Long, Long, Double> tuple = elements.iterator().next();
        // 窗口元信息
        TimeWindow window = context.window();
        long start = window.getStart();
        long end = window.getEnd();
        String startTime = DateUtil.timeStamp2Date(start, "yyyy-MM-dd HH:mm:ss");
        String endTime = DateUtil.timeStamp2Date(end, "yyyy-MM-dd HH:mm:ss");
        long currentWatermark = context.currentWatermark();
        String currentWatermarkTime = DateUtil.timeStamp2Date(currentWatermark, "yyyy-MM-dd HH:mm:ss");
        long currentProcessingTimeStamp = context.currentProcessingTime();
        String currentProcessingTime = DateUtil.timeStamp2Date(currentProcessingTimeStamp, "yyyy-MM-dd HH:mm:ss");

        ContextInfo contextInfo = new ContextInfo();
        contextInfo.setKey(key);
        contextInfo.setResult("SUM: " + tuple.f0 + ", Count: " + tuple.f1 + ", Average: " + tuple.f2);
        contextInfo.setWindowStartTime(startTime);
        contextInfo.setWindowEndTime(endTime);
        contextInfo.setCurrentWatermark(currentWatermarkTime);
        contextInfo.setCurrentProcessingTime(currentProcessingTime);
        LOG.info("[WINDOW] " + contextInfo.toString());
        // 输出
        out.collect(new Tuple2<ContextInfo, Tuple3<Long, Long, Double>>(contextInfo, tuple));
    }
}
```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.29i57w7e48u8.png)

##  3. <a name='Trigger'></a>Trigger 触发器,用来确定何时触发窗口的计算

Trigger 接口有 6 个方法，可以允许 Trigger 对不同的事件做出反应：

- onElement
- onProcessingTime
- onEventTime
- canMerge
- onMerge
- clear

```java
public abstract class Trigger<T, W extends Window> implements Serializable {
    // 每个元素到达窗口时都会调用该方法，来决定是否触发窗口计算并输出窗口结果。
    public abstract TriggerResult onElement(T element, long timestamp, W window, TriggerContext ctx) throws Exception;
    // 当使用 TriggerContext 注册的处理时间 Timer 触发时会调用该方法。
    public abstract TriggerResult onProcessingTime(long time, W window, TriggerContext ctx) throws Exception;
    // 当使用 TriggerContext 注册的事件时间 Timer 触发时会调用该方法。
    public abstract TriggerResult onEventTime(long time, W window, TriggerContext ctx) throws Exception;
    // 如果 Trigger 支持对 Trigger 状态进行 Merge 并因此可以与 MergingWindowAssigner 一起使用，则返回 true。如果返回 true，必须实现 onMerge 方法。
    public boolean canMerge() {
        return false;
    }
    // 当 WindowAssigner 将多个窗口合并为一个窗口时会调用该方法，同时会进行状态的合并。
    public void onMerge(W window, OnMergeContext ctx) throws Exception {
        throw new UnsupportedOperationException("This trigger does not support merging.");
    }
    // 执行窗口以及状态数据的清除。
    public abstract void clear(W window, TriggerContext ctx) throws Exception;
}
```

###  3.1. <a name='Trigger-1'></a>Trigger 示例：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.164nnp6u07j4.png)

###  3.2. <a name='PurgingTrigger'></a>PurgingTrigger 的应用：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6axdle7s19c0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2kvnn6fj8hg0.png)

###  3.3. <a name='DeltaTrigger'></a>DeltaTrigger 的应用：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2ytbwtb1a0k0.png)

首先需要考虑的是如何来划分窗口，它不是一个时间的窗口，也不是一个基于数量的窗口。

用传统的窗口实现比较困难，这种情况下我们考虑使用 `DeltaTrigger` 来实现。

下面是简单的代码实现：

这个例子中我们通过 `GlobalWindow` 和 `DeltaTrigger` 来实现了

-> 自定义的 `Window Assigner` 的功能

```java
DataStream<Tuple4<Integer, Integer, Double, Long>> topSpeeds = carData
        // 提取时间戳和生成水印
        .assignTimestampsAndWatermarks(new CarTimestamp())
        // 选择聚合维度
        .keyBy( ...fields: 0)
        // 不是传统意义上的时间窗口或数量窗口，可以创建一个 GlobalWindow
        .window(GlobalWindows.create())
        // 通过定义一个 DeltaTrigger，并设定一个阈值
        // 这里是10000（米）
        // 每个元素和上次触发计算的元素比较是否达到设定的阈值
        .trigger(DeltaTrigger.of( threshold: 10000,
                new DeltaFunction<Tuple4<Integer, Integer, Double, Long>>() {
                    private static final long serialVersionUID = lL;

                    @Override
                    public double getDelta(
                    Tuple4<Integer, Integer, Double, Long> oldDataPoint,
                    Tuple4<Integer, Integer, Double, Long> newDataPoint) {
                    // 这里比较的是每个元素上报的位置
                    return newDataPoint.f2 - oldDataPoint.f2;
                    }
                // 如果达到了10000（米），那么当前元素carData和上一个触发计算的元素carData之间的所有元素落在同一个窗口里计算
                }, carData.getType().createSerializer(env.getConfig())))
        // 可以通过 Max 聚合计算出最大的车速。
        .max( positionToMax: 1);
```

###  3.4. <a name='GlobalWindowWindowAssigner'></a>GlobalWindow + 触发 = 自定义 WindowAssigner

对于一些复杂的窗口，我们还可以自定义 `WindowAssigner`，但实现起来不一定简单，倒不如利用 GlobalWindow 和自定义 Trigger 来达到同样的效果。

下面这个是Flink内置的`CountWindow`的实现,也是基于`GlobalWindow`和`触发`来实现的。

```java

public WindowedStream<T, KEY, GlobalWindow> countWindow(long size) {
    return window(GlobalWindows.create()).trigger(PurgingTrigger.of(CountTrigger.of(size)
}

public WindowedStream<T, KEY, GlobalWindow> countWindow(long size, long slide) {
    return window(GlobalWindows.create())
             .evictor(CountEvictor.of(size))
             .trigger(CountTrigger.of(slide)); 
```

--------------------------------------------------------------------

定时`何时聚合数据`。每一个默认窗口都有`触发器`。

触发器定义了`3个触发动作`，并且定义了`触发动作`处理完毕后的`返回结果`。

- `返回结果`交给`Window Operator`后,由`Window Operator`决定后续操作。

```java
// 返回类型及说明
public enum TriggerResult {
    CONTINUE,FIRE,PURGE,FIRE_AND_PURGE;
}
```

###  3.5. <a name='Trigger-1'></a>自定义Trigger

接下来我们以一个提前计算的案例来解释如何使用自定义的Trigger。在股票或任何交易场景中，我们比较关注价格急跌的情况，默认窗口长度是60秒，如果价格跌幅超过5%，则立即执行Window Function，如果价格跌幅在1%到5%之内，那么10秒后触发Window Function:

```scala
class MyTrigger extends Trigger[StockPrice, TimeWindow] {
  override def onElement(element: StockPrice,
                         time: Long,
                         window: TimeWindow,
                         triggerContext: Trigger.TriggerContext): TriggerResult = {
    val lastPriceState: ValueState[Double] = triggerContext.getPartitionedState(new ValueStateDescriptor[Double]("lastPriceState", classOf[Double]))
    // 设置返回默认值为CONTINUE
    var triggerResult: TriggerResult = TriggerResult.CONTINUE
    // 第一次使用lastPriceState时状态是空的,需要先进行判断
    // 状态数据由Java端生成，如果是空，返回一个null
    // 如果直接使用Scala的Double，需要使用下面的方法判断是否为空
    if (Option(lastPriceState.value()).isDefined) {
      if ((lastPriceState.value() - element.price) > lastPriceState.value() * 0.05) {
        // 如果价格跌幅大于5%，直接FIRE_AND_PURGE
        triggerResult = TriggerResult.FIRE_AND_PURGE
      } else if ((lastPriceState.value() - element.price) > lastPriceState.value() * 0.01) {
        val t = triggerContext.getCurrentProcessingTime + (10 * 1000 - (triggerContext.getCurrentProcessingTime % 10 * 1000))
        // 给10秒后注册一个Timer
        triggerContext.registerProcessingTimeTimer(t)
      }
    }
    lastPriceState.update(element.price)
    triggerResult
  }
  // 我们不用EventTime，直接返回一个CONTINUE
  override def onEventTime(time: Long, window: TimeWindow, triggerContext: Trigger.TriggerContext): TriggerResult = {
    TriggerResult.CONTINUE
  }
  override def onProcessingTime(time: Long, window: TimeWindow, triggerContext: Trigger.TriggerContext): TriggerResult = {
    TriggerResult.FIRE_AND_PURGE
  }
  override def clear(window: TimeWindow, triggerContext: Trigger.TriggerContext): Unit = {
    val lastPrice: ValueState[Double] = triggerContext.getPartitionedState(new ValueStateDescriptor[Double]("lastPrice", classOf[Double]))
    lastPrice.clear()
  }
}
senv.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
val input: DataStream[StockPrice] = ...
val average = input
      .keyBy(s => s.symbol)
      .timeWindow(Time.seconds(60))
      .trigger(new MyTrigger)
      .aggregate(new AverageAggregate)
```

在自定义Trigger时，如果使用了状态，一定要使用clear方法将状态数据清理，否则随着窗口越来越多，状态数据会越积越多。

###  3.6. <a name='TriggerResult'></a>TriggerResult 有如下几种取值：

决定窗口是否应该被`处理`、被`清除`、被`处理+清除`、还是`什么都不做`。

CONTINUE: 继续，不做任何操作。

- 不做任何操作
- 表示当前不触发计算。

FIRE: 触发计算，处理窗口数据。

- `处理`窗口数据，
- 窗口计算后不做清理。
- 这意味着下次FIRE时候可以再次用来计算（比如`滑动计数窗口`）。
- 表示触发`窗口计算`，但是`数据继续保留`。如果窗口算子具有 `ProcessWindowFunction`，则调用该函数并输出计算结果。如果窗口只有一个`增量聚合函数（ReduceFunction 或 AggregateFunction）`，则输出当前`聚合结果`。窗口状态(State)没有任何改变。

PURGE: 触发清理，移除窗口和窗口中的数据。

- 移除`窗口`和`窗口中的数据`
- 表示清除`窗口内部数据`，但不触发`窗口计算`。窗口所有`元素被清除`，窗口也被销毁(包括所有`元数据`)。此外，会调用`ProcessWindowFunction.clear() 方法`来清除所有`自定义窗口状态`。
  
FIRE_AND_PURGE: 触发计算+清理，处理数据并移除窗口和窗口中的数据。

- FIRE+PURGE的组合处理，即`处理`并`移除`窗口中的数据
- 表示触发`窗口计算`并`清除数据`。首先触发`窗口计算(FIRE)`，然后清除`所有状态和元数据(PURGE)`。

###  3.7. <a name='-1'></a>内置触发器

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3u7lkjb609m0.png)

EventTimeTrigger: 通过对比 Watermark 和窗口结束时间戳确定是否触发窗口，如果 Watermark 的时间大于窗口结束时间戳则触发计算，反之不触发计算。

ProcessingTimeTrigger: 通过对比 ProcessTime 和窗口结束时间戳确定是否触发窗口，如果 ProcessTime 的时间大于窗口结束时间戳则触发计算，反之不触发计算。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2jx3gaov8qs0.png)

ContinuousEventTimeTrigger: 根据间隔时间周期性触发窗口或者当前 EventTime 大于窗口结束时间戳会触发窗口计算。

ContinuousProcessingTimeTrigger: 根据间隔时间周期性触发窗口或者当前 ProcessTime 大于窗口结束时间戳会触发窗口计算。

CountTrigger: 根据接入数据量是否超过设定的阈值来决定是否触发窗口计算。

DeltaTrigger: 计算上一个触发点与当前点，达到给定阈值触发.根据接入数据量计算出来的 Delta 指标是否超过设定的阈值来决定是否触发窗口计算。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1ybix9io9v34.png)

PurgingTrigger: 为触发器增加 purging 清理。可以将任意 Trigger 作为参数转为为 Purge 类型大的 Trigger，计算完成后数据将被清除。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.7l1oe8trerg0.png)

NeverTrigger

ProcessingTimeoutTrigger：可以将任意 Trigger 作为参数转为为 ProcessingTimeout 类型的 Trigger。在第一个元素到达后设置一个超时处理时间。还可以通过指定 resetTimerOnNewRecord 为每个到达的元素重新更新计时器，也可以指定是否应通过 shouldClearOnTimeout 在超时时清理窗口所有数据。（ProcessingTimeoutTrigger 于 1.12.0 版本引入。）

###  3.8. <a name='-Trigger'></a>自定义触发器 - 继承并实现 Trigger 抽象类

假设我们有如下场景：

当每个`文章`点击`用户`达到 `N 个用户`时，计算每个文章的的`总点击次数`以及`平均点击次数`。

比如如下输入流：

```sql
contentId(文章Id)、uid(用户Id)、clickCnt(点击次数)

c10,ua,1
c11,ua,2
c10,ub,1
c11,ub,4
c10,uc,3
c12,ua,5
c11,uc,1
c12,ub,2
c12,uc,4

第一条记录表示用户 ua 对文章 c10 点击了 1 次。
等到达第 5 条记录时，文章 c10 被 ua、ub、uc 三个用户点击。
此时，该文章总共被点击了 5(1 + 1 + 3) 次。
```

因为默认 Trigger 不符合我们的需要 -> 

- 要么根据处理时间、事件时间来触发，
- 要么根据到达的元素个数来触发，
- 没有默认 Trigger 能实现根据到达的用户个数触发的
  
  所以我们需要实现如下自定义 Trigger：

```java
// 自定义 Trigger: CustomCountTrigger
private static class CustomCountTrigger <W extends Window> extends Trigger<Object, W> {
    private Logger LOG = LoggerFactory.getLogger(CustomWindowTriggerExample.class);
    private Long maxCount;
    private ListStateDescriptor<String> stateDescriptor = new ListStateDescriptor<>("UidState", String.class);
    
    // 自定义 Trigger: CustomCountTrigger
    public CustomCountTrigger(Long maxCount) {
        this.maxCount = maxCount;
    }

    @Override
    // 自定义 Trigger 的核心逻辑都在 onElement 方法中。
    public TriggerResult onElement(Object element, long timestamp, W window, TriggerContext ctx) throws Exception {
        // 获取uid信息
        Tuple3<String, String, Long> uidTuple = (Tuple3<String, String, Long>) element;
        String key = uidTuple.f0;
        String uid = uidTuple.f1;
        // 获取状态
        ListState<String> uidState = ctx.getPartitionedState(stateDescriptor);
        // 更新状态
        Iterable<String> iterable = uidState.get();
        List<String> uidList = Lists.newArrayList();
        
        if (!Objects.equals(iterable, null)) {
            uidList = Lists.newArrayList(iterable);
        }
        boolean isContains = uidList.contains(uid);
        // 每次元素到达窗口时都要判断元素对应的用户是否在状态中，如果不在状态中，则更新状态。
        if (!isContains) {
            uidList.add(uid);
            uidState.update(uidList);
        }
        // 更新状态之后判断状态中的用户个数，
        // 如果达到指定的阈值则触发窗口计算，否则什么都不做。
        // 大于等于3个用户触发计算
        if (uidList.size() >= maxCount) {
            LOG.info("[Trigger] Key: {} 触发计算并清除状态", key);
            uidState.clear();
            return TriggerResult.FIRE;
        }
        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onProcessingTime(long time, W window, TriggerContext ctx) throws Exception {
        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onEventTime(long time, W window, TriggerContext ctx) throws Exception {
        return TriggerResult.CONTINUE;
    }

    @Override
    public void clear(W window, TriggerContext ctx) throws Exception {
        ctx.getPartitionedState(stateDescriptor).clear();
    }
    
    // 自定义 Trigger: CustomCountTrigger
    public static <W extends Window> CustomCountTrigger<W> of(long maxCount) {
        return new CustomCountTrigger<>(maxCount);
    }
}
```

```java
DataStream<String> result = stream
    // 根据contentId分组
    .keyBy(new KeySelector<Tuple3<String, String, Long>, String>() {
        @Override
        public String getKey(Tuple3<String, String, Long> value) throws Exception {
            return value.f0;
        }
    })
    // 每3个用户一个窗口
    .window(GlobalWindows.create())
    // 自定义 Trigger: CustomCountTrigger
    // 通过调用 trigger 方法来指定我们的自定义 Trigger：
    .trigger(CustomCountTrigger.of(3))
    // 求和以及平均值
    .aggregate(new AverageAggregateFunction());
```

##  4. <a name='Evictor'></a>Evictor 清除器,对满足驱逐条件的数据做过滤

Evictor 提供了在使用 WindowFunction 之前或者之后从窗口中删除元素的能力。

如果定义了Evictor，当执行窗口处理前会`删除窗口内指定数据`再交给`窗口处理`，或等`窗口执行处理`后再`删除窗口中指定数据`。

为此，Evictor 接口提供了两个方法：evictAfter + evictBefore

evictBefore和evictAfter分别在Window Function之前和之后被调用.

窗口的所有元素被放在了Iterable<TimestampedValue<T>>，我们要实现自己的清除逻辑。

当然，对于增量计算的ReduceFunction和AggregateFunction，我们没必要使用Evictor。

Flink提供了几个实现好的Evictor：

- CountEvictor保留一定数目的元素，多余的元素按照从前到后的顺序先后清理。

- TimeEvictor保留一个时间段的元素，早于这个时间段的元素会被清理。

```java
public interface Evictor<T, W extends Window> extends Serializable {
  // 可选的删除元素，在窗口函数之前调用
  void evictBefore(Iterable<TimestampedValue<T>> elements,
      int size, W window, EvictorContext evictorContext);

  // 可选的删除元素，在窗口函数之后调用
  void evictAfter(Iterable<TimestampedValue<T>> elements,
      int size, W window, EvictorContext evictorContext);

  interface EvictorContext {
      // 当前处理时间
      long getCurrentProcessingTime();
      MetricGroup getMetricGroup();
      // 当前Watermark
      long getCurrentWatermark();
  }
}
```

evictBefore() 用于在使用窗口函数之前从窗口中删除元素，

而 evictAfter() 用于在使用窗口函数之后从窗口中删除元素。

###  4.1. <a name='evictor'></a>内置 evictor

默认情况下，所有内置的 Evictors 都是在触发窗口函数之前使用。

- **CountEvictor**

```java
private final boolean doEvictAfter;
@Override
public void evictBefore(
        Iterable<TimestampedValue<Object>> elements, int size, W window, EvictorContext ctx) {
    if (!doEvictAfter) {
        evict(elements, size, ctx);
    }
}

@Override
public void evictAfter(
        Iterable<TimestampedValue<Object>> elements, int size, W window, EvictorContext ctx) {
    if (doEvictAfter) {
        evict(elements, size, ctx);
    }
}
```

- **TimeEvictor**

基于时间的窗口会有开始时间戳(闭区间)和结束时间戳(开区间)，它们共同描述了窗口的大小。在代码中，Flink 在使用基于时间的窗口时使用 TimeWindow，该窗口具有用于查询开始和结束时间戳的方法，以及用于返回给定窗口的最大允许时间戳的 maxTimestamp() 方法。

```java
// TimeEvictor 与 DeltaEvictor、CountEvictor 一样，都需要实现 Evictor 接口的 evictBefore 和 evictAfter 方法，只是最终调用的 evict() 函数的内部实现逻辑不一样：
private void evict(Iterable<TimestampedValue<Object>> elements, int size, EvictorContext ctx) {
    if (!hasTimestamp(elements)) {
        return;
    }
    // 最大时间戳
    long currentTime = getMaxTimestamp(elements);
    // windowSize 保留元素的时间间隔
    long evictCutoff = currentTime - windowSize;
    for (Iterator<TimestampedValue<Object>> iterator = elements.iterator();
            iterator.hasNext(); ) {
        TimestampedValue<Object> record = iterator.next();
        if (record.getTimestamp() <= evictCutoff) {
            iterator.remove();
        }
    }
}
// 第一个元素是否有时间戳
private boolean hasTimestamp(Iterable<TimestampedValue<Object>> elements) {
    Iterator<TimestampedValue<Object>> it = elements.iterator();
    if (it.hasNext()) {
        return it.next().hasTimestamp();
    }
    return false;
}
// 窗口中最大时间戳
private long getMaxTimestamp(Iterable<TimestampedValue<Object>> elements) {
    long currentTime = Long.MIN_VALUE;
    for (Iterator<TimestampedValue<Object>> iterator = elements.iterator();
            iterator.hasNext(); ) {
        TimestampedValue<Object> record = iterator.next();
        currentTime = Math.max(currentTime, record.getTimestamp());
    }
    return currentTime;
}
```

- **DeltaEvictor**: 根据DeltaFunction实现和阀值决定如何清理数据

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5z0v1i7xxlo0.png)

```java
private void evict(Iterable<TimestampedValue<T>> elements, int size, EvictorContext ctx) {
    // 窗口最后一个元素
    TimestampedValue<T> lastElement = Iterables.getLast(elements);
    // 遍历整个窗口，与每一个元素进行比较
    for (Iterator<TimestampedValue<T>> iterator = elements.iterator(); iterator.hasNext(); ) {
        TimestampedValue<T> element = iterator.next();
        if (deltaFunction.getDelta(element.getValue(), lastElement.getValue())
                >= this.threshold) {
            iterator.remove();
        }
    }
}
```

###  4.2. <a name='TimeEvictor1'></a>TimeEvictor 的应用1

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6n1rq150dn40.png)

```java
DataStream<Tuple4<Integer, Integer, Double, Long>> topSpeeds = carData
        .assignTimestampsAndWatermarks(new CarTimestamp())
        .keyBy( ...fields: 0)
        .window(GlobalWindows.create())
        // 实现上只是在前面基础上增加了 Evictor 的使用，过滤掉窗口最后15分钟之前的数据。
        .evictor(TimeEvictor.of(Time.minutes(15)))
        .trigger(DeltaTrigger.of( threshold: 10000,
                new DeltaFunction<Tuple4<Integer, Integer, Double, Long>>() {
                    private static final long serialVersionUID = lL;

                    @Override
                    public double getDelta(
                    Tuple4<Integer, Integer, Double, Long> oldDataPoint,
                    Tuple4<Integer, Integer, Double, Long> newDataPoint) {
                    return newDataPoint.f2 - oldDataPoint.f2;
                    }
                }, carData.getType().createSerializer(env.getConfig())))
        .max( positionToMax: 1);
```

###  4.3. <a name='TimeEvictor2'></a>TimeEvictor 的应用2

如下代码所示，在触发窗口函数计算之前只保留最近10s内的元素：

```scala
DataStream<Tuple2<String, Long>> result = stream
    // 格式转换
    .map(tuple -> Tuple2.of(tuple.f0, tuple.f1)).returns(Types.TUPLE(Types.STRING, Types.LONG))
    // 根据key分组
    .keyBy(new KeySelector<Tuple2<String, Long>, String>() {
        @Override
        public String getKey(Tuple2<String, Long> value) throws Exception {
            return value.f0;
        }
    })
    // 处理时间滚动窗口 滚动大小60s
    .window(TumblingEventTimeWindows.of(Time.minutes(1)))
    // 保留窗口中最近10s内的元素
    .evictor(TimeEvictor.of(Time.seconds(10)))
    // 窗口函数
    .process(new ProcessWindowFunction<Tuple2<String, Long>, Tuple2<String, Long>, String, TimeWindow>() {
        @Override
        public void process(String key, Context context, Iterable<Tuple2<String, Long>> elements, Collector<Tuple2<String, Long>> out) throws Exception {
            // Watermark
            long watermark = context.currentWatermark();
            String watermarkTime = DateUtil.timeStamp2Date(watermark);
            // 窗口开始与结束时间
            TimeWindow window = context.window();
            String start = DateUtil.timeStamp2Date(window.getStart());
            String end = DateUtil.timeStamp2Date(window.getEnd());
            // 窗口中元素
            List<Long> values = Lists.newArrayList();
            for (Tuple2<String, Long> element : elements) {
                values.add(element.f1);
            }
            LOG.info("[Process] Key: {}, Watermark: [{}|{}], Window: [{}|{}, {}|{}], Values: {}",
                    key, watermarkTime, watermark, start, window.getStart(), end, window.getEnd(), values
            );
        }
    });
```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6pxgaod6u7k0.png)

###  4.4. <a name='CountEvictor'></a>CountEvictor 的应用


```scala
DataStream<Tuple2<String, Long>> result = stream
    // 格式转换
    .map(tuple -> Tuple2.of(tuple.f0, tuple.f1)).returns(Types.TUPLE(Types.STRING, Types.LONG))
    // 根据key分组
    .keyBy(new KeySelector<Tuple2<String, Long>, String>() {
        @Override
        public String getKey(Tuple2<String, Long> value) throws Exception {
            return value.f0;
        }
    })
    // 处理时间滚动窗口 滚动大小60s
    .window(TumblingEventTimeWindows.of(Time.minutes(1)))
    // 在触发使用窗口函数之前保留2个元素
    .evictor(CountEvictor.of(2))
    // 窗口函数
    .process(new ProcessWindowFunction<Tuple2<String, Long>, Tuple2<String, Long>, String, TimeWindow>() {
        @Override
        public void process(String key, Context context, Iterable<Tuple2<String, Long>> elements, Collector<Tuple2<String, Long>> out) throws Exception {
            // ---------------------Watermark---------------------
            long watermark = context.currentWatermark();
            String watermarkTime = DateUtil.timeStamp2Date(watermark);
            // ---------------------Watermark---------------------
            // 窗口开始与结束时间
            TimeWindow window = context.window();
            String start = DateUtil.timeStamp2Date(window.getStart());
            String end = DateUtil.timeStamp2Date(window.getEnd());
            // 窗口中元素
            List<Long> values = Lists.newArrayList();
            for (Tuple2<String, Long> element : elements) {
                values.add(element.f1);
            }
            // ---------------------Watermark---------------------
            LOG.info("[Process] Key: {}, Watermark: [{}|{}], Window: [{}|{}, {}|{}], Values: {}",
                    key, watermarkTime, watermark, start, window.getStart(), end, window.getEnd(), values
            // ---------------------Watermark---------------------
            );
        }
    });
```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.7jgv3nfh7uk0.png)

###  4.5. <a name='DeltaEvictor'></a>DeltaEvictor 的应用

根据用户自定的 DeltaFunction 函数来计算窗口中最后一个元素与其余每个元素之间的差值，如果差值大于等于用户指定的阈值就会删除该元素。

在触发窗口函数计算之前剔除与最后一个元素值差大于等于1的元素：如下代码所示

```scala
DataStream<Tuple2<String, Long>> result = stream
    // 格式转换
    .map(tuple -> Tuple2.of(tuple.f0, tuple.f1)).returns(Types.TUPLE(Types.STRING, Types.LONG))
    // 根据key分组
    .keyBy(new KeySelector<Tuple2<String, Long>, String>() {
        @Override
        public String getKey(Tuple2<String, Long> value) throws Exception {
            return value.f0;
        }
    })
    // 处理时间滚动窗口 滚动大小60s
    .window(TumblingEventTimeWindows.of(Time.minutes(1)))
    // 剔除与最后一个元素值差大于1的元素
    .evictor(DeltaEvictor.of(1, new DeltaFunction<Tuple2<String, Long>>() {
        @Override
        public double getDelta(Tuple2<String, Long> oldDataPoint, Tuple2<String, Long> newDataPoint) {
            return oldDataPoint.f1 - newDataPoint.f1;
        }
    }))
    // 窗口函数
    .process(new ProcessWindowFunction<Tuple2<String, Long>, Tuple2<String, Long>, String, TimeWindow>() {
        @Override
        public void process(String key, Context context, Iterable<Tuple2<String, Long>> elements, Collector<Tuple2<String, Long>> out) throws Exception {
            // Watermark
            // ---------------------Watermark---------------------
            long watermark = context.currentWatermark();
            String watermarkTime = DateUtil.timeStamp2Date(watermark);
            // ---------------------Watermark---------------------
            // 窗口开始与结束时间
            TimeWindow window = context.window();
            String start = DateUtil.timeStamp2Date(window.getStart());
            String end = DateUtil.timeStamp2Date(window.getEnd());
            // 窗口中元素
            List<Long> values = Lists.newArrayList();
            for (Tuple2<String, Long> element : elements) {
                values.add(element.f1);
            }
            // ---------------------Watermark---------------------
            LOG.info("[Process] Key: {}, Watermark: [{}|{}], Window: [{}|{}, {}|{}], Values: {}",
                    key, watermarkTime, watermark, start, window.getStart(), end, window.getEnd(), values
            // ---------------------Watermark---------------------
            );
        }
    });
```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3jvzuunwrdi0.png)

##  5. <a name='-1'></a>时间语义

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4rnpr9qv7rq0.png)

处理时间(process time)

摄入时间(ingestion time)

事件时间(event time)

###  5.1. <a name='watermarks'></a>水位线watermarks

Watermark机制（解决乱序问题）

setAutoWatermarkInterval(long milliseconds)：设置自动发送 watermark 的时间间隔。

你可以使用 long getAutoWatermarkInterval() 获取当前配置值。

####  5.1.1. <a name='watermark'></a>为什么要引入watermark

由于`实时计算的输入数据`是持续不断的，因此我们需要一个有效的`进度指标`，来帮助我们确定`关闭时间窗口`的正确时间点，保证`关闭窗口`后不会再有数据进入该窗口，可以安全输出这个`窗口的聚合结果`。

而Watermark就是一种`衡量Event Time进展`的`有效机制`。随着时间的推移，`最早流入实时计算`的数据会被处理完成，`之后流入`的数据处于`正在处理状态`。处于`正在处理部分`的和`已处理部分`的`交界的时间戳`，可以被定义为Watermark，代表在此之前的事件已经被处理完成并输出。


支持`事件时间`的流处理引擎需要一种度量`事件时间`进度的方式

在Flink计算引擎中度量`事件时间`进度的机制被称为`水位线（Watermarks）`，有的也翻译成`水印`。

Watermark(t)定义了在一个流中事件时间已到达时间t，同时这也意味着所有的带有时间戳`t’（t’<t）`的事件应该已经发生并已被系统处理（这里说应该，是因为实际业务场景中可能还存在已发生但还没被处理的迟到元素，后面会具体介绍如何处理）

一个全局进度指标

表示我们确信不会再有`迟到的事件`到来的某个时间点

对于处理`事件窗口`和`乱序事件`都很关键

允许我们在`结果的准确性`和`延迟`之间做出取舍

激进的水位线策略保证了 -> 低延迟。延迟事件可能会在水位线之后到来。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4qdfqvk8pv20.png)

针对`乱序的流`，Watermark也至关重要，即使`部分事件延迟到达`，也不会影响`窗口计算的正确性`。此外，`并行数据流`中，当`算子（Operator）`有多个`输入流`时，`算子的Event Time`以`最小流Event Time`为准。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1gfv6y3umagw.png)

####  5.1.2. <a name='watermark-1'></a>watermark策略

严格意义上`递增的时间戳`,发出到目前为止已观察到的`最大时间戳`的水印。时间戳小于`最大时间戳`的行不会迟到。

```scala
watermark for rowtime_column as rowtime_column
```

`递增的时间戳`,发出到目前为止已观察到的`最大时间戳`为`负1`的水印。时间戳等于或小于`最大时间戳`的行不会迟到。

```scala
watermark for rowtime_column as rowtime_column - INTERVAL '1' SECOND.
```

有界`时间戳(乱序)`发出`水印`，它是观察到的`最大时间戳`减去`指定的延迟`。

```scala
watermark for rowtime_column as rowtime_column - INTERVAL'5'SECOND

// 是5秒的延迟水印策略。

watermark for rowtime_column as rowtime_column - INTERVAL 'string' timeUnit
```

###  5.2. <a name='-1'></a>语法格式样例

```sql
CREATE TABLE Orders (
    `user` BIGINT,
    product STRING,
    order_time TIMESTAMP(3),
    WATERMARK FOR order_time AS order_time - INTERVAL '5' SECOND
) WITH ( . . . );
```

###  5.3. <a name='-1'></a>选择时间特性

以下示例展示了一个聚合每小时时间窗口内的事件的 Flink 程序:

> java

```java
final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

// alternatively:
// env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime);
// env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

DataStream<MyEvent> stream = env.addSource(new FlinkKafkaConsumer09<MyEvent>(topic, schema, props));

stream
    .keyBy( (event) -> event.getUser() )
    .timeWindow(Time.hours(1))
    .reduce( (a, b) -> a.add(b) )
    .addSink(...);
```

> scala

```scala
val env = StreamExecutionEnvironment.getExecutionEnvironment

env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)

// alternatively:
// env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime)
// env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

val stream: DataStream[MyEvent] = env.addSource(new FlinkKafkaConsumer09[MyEvent](topic, schema, props))

stream
    .keyBy( _.getUser )
    .timeWindow(Time.hours(1))
    .reduce( (a, b) => a.add(b) )
    .addSink(...)
```

注：为了以`事件时间`运行此示例，程序需要使用定义了`事件时间`并自动产生`watermarks的 Source`，或者程序必须在 Source 之后设置`时间戳分配器`和 `watermarks 生成器`。