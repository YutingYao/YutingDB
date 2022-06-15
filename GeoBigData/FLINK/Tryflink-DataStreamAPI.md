<!-- vscode-markdown-toc -->
* 1. [Flink Maven Archetype](#FlinkMavenArchetype)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->
##  1. <a name='FlinkMavenArchetype'></a>Flink Maven Archetype

完整版见：这里只是最简单的V1版本

[基于 DataStream API 实现欺诈检测](https://nightlies.apache.org/flink/flink-docs-release-1.14/zh/docs/try-flink/datastream/)

```s
 mvn archetype:generate \
    -DarchetypeGroupId=org.apache.flink \
    -DarchetypeArtifactId=flink-walkthrough-datastream-scala \
    -DarchetypeVersion=1.14.0 \
    -DgroupId=frauddetection \ #可以根据自己的情况修改
    -DartifactId=frauddetection \ #可以根据自己的情况修改
    -Dversion=0.1 \
    -Dpackage=spendreport \ #可以根据自己的情况修改
    -DinteractiveMode=false
```

通过这三个参数， Maven 将会创建一个名为 `frauddetection` 的文件夹

包含了所有`依赖`的整个工程项目将会位于该文件夹下

将工程目录导入到你的开发环境之后，你可以找到:

- FraudDetectionJob.scala 代码文件

文件中的代码如下所示: FraudDetectionJob.scala

```scala
package spendreport

import org.apache.flink.streaming.api.scala._
import org.apache.flink.walkthrough.common.sink.AlertSink
import org.apache.flink.walkthrough.common.entity.Alert
import org.apache.flink.walkthrough.common.entity.Transaction
import org.apache.flink.walkthrough.common.source.TransactionSource
-------------------------------------------------------------------
object FraudDetectionJob { 
// FraudDetectionJob 类定义了程序的数据【流】
-------------------------------------------------------------------

  @throws[Exception]
  def main(args: Array[String]): Unit = { 
  // 整个 Job 组装到 FraudDetectionJob 类的 main 函数中
-------------------------------------------------------------------
//////////////////////////////// 执行环境 ////////////////////////////////
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 第一行的 StreamExecutionEnvironment 用于设置你的执行环境。
    // 任务执行环境 -> 用于 -> 定义任务的属性
    //                     -> 创建数据源
    //                     -> 最终启动任务的执行
-------------------------------------------------------------------
//////////////////////////////// 创建数据源 ////////////////////////////////
    val transactions: DataStream[Transaction] = env
      .addSource(new TransactionSource)
      // 数据源从外部系统例如 Apache Kafka 接收数据
      .name("transactions")
      // 绑定到数据源上的 name 属性是为了调试方便，如果发生一些异常，我们能够通过它快速定位问题发生在哪里。
-------------------------------------------------------------------
/////////////////////////// 对事件分区 & 欺诈检测 //////////////////////////
// 背景: transactions 这个数据流包含了大量的【用户交易数据】，
//       需要被划分到多个并发上进行欺诈检测处理。
//       由于欺诈行为的发生是基于【某一个账户】的，
//       所以，必须要要保证【同一个账户】的所有交易行为数据
//       要被【同一个并发的task】进行处理。
    val alerts: DataStream[Alert] = transactions
      .keyBy(transaction => transaction.getAccountId)
      // 为了保证同一个 task 处理同一个 key 的所有数据
      // 你可以使用 DataStream#keyBy 对【流】进行分区。
      .process(new FraudDetector)
      // process() 函数对【流】绑定了一个【操作】，
      // 这个操作将会对【流】上的每一个消息调用所【定义好的函数FraudDetector】。
      // FraudDetector 是在一个 keyed context 上执行的
      .name("fraud-detector")
-------------------------------------------------------------------
//////////////////////////////// 输出结果 ////////////////////////////////
// sink 会将 DataStream 写出到外部系统，例如 Apache Kafka
    alerts
      .addSink(new AlertSink)
      // AlertSink: 使用 INFO 的日志级别打印每一个 Alert 的数据记录，
      //            而不是将其写入持久存储，
      //            以便你可以方便地查看结果。
      .name("send-alerts")
-------------------------------------------------------------------
//////////////////////////////// 运行作业 ////////////////////////////////
// Flink 程序是懒加载的，并且只有在完全搭建好之后，才能够发布到集群上执行。 
// 调用 StreamExecutionEnvironment#execute 时给任务传递一个任务名参数，就可以开始运行任务。
    env.execute("Fraud Detection")
  }
}
```

FraudDetector.scala

```scala
package spendreport

import org.apache.flink.streaming.api.functions.KeyedProcessFunction
// 欺诈检查类 FraudDetector 是 KeyedProcessFunction 接口的一个实现
import org.apache.flink.util.Collector
import org.apache.flink.walkthrough.common.entity.Alert
import org.apache.flink.walkthrough.common.entity.Transaction

object FraudDetector { 
// FraudDetector 类定义了欺诈交易检测的业务逻辑。
  val SMALL_AMOUNT: Double = 1.00
  val LARGE_AMOUNT: Double = 500.00
  val ONE_MINUTE: Long     = 60 * 1000L
}

@SerialVersionUID(1L)
class FraudDetector extends KeyedProcessFunction[Long, Transaction, Alert] {

  @throws[Exception]
  def processElement(
      // 欺诈检查类 FraudDetector 的方法 KeyedProcessFunction#processElement 
      // 将会在每个交易事件上被调用。↓↓↓↓
      // 这个程序里边会对每笔交易发出警报，有人可能会说这做报过于保守了。
      transaction: Transaction,
      context: KeyedProcessFunction[Long, Transaction, Alert]#Context,
      collector: Collector[Alert]): Unit = {

    val alert = new Alert
    alert.setId(transaction.getAccountId)

    collector.collect(alert)
  }
}
```