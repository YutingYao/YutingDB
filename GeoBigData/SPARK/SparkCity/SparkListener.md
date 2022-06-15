spark 提供了一系列整个任务生命周期中各个阶段变化的`事件监听机制`

通过这一机制可以在任务的各个阶段做一些自定义的各种动作

SparkListener便是这些阶段的`事件监听接口类`

通过实现这个类中的各种方法便可实现`自定义的事件处理动作`

代码记录

```scala
// SparkListener 下各个事件对应的函数名非常直白，即如字面所表达意思。

// 想对哪个阶段的事件做一些自定义的动作，变继承SparkListener实现对应的函数即可

abstract class SparkListener extends SparkListenerInterface {
  //阶段完成时触发的事件
  override def onStageCompleted(stageCompleted: SparkListenerStageCompleted): Unit = { }

  //阶段提交时触发的事件
  override def onStageSubmitted(stageSubmitted: SparkListenerStageSubmitted): Unit = { }

  //任务启动时触发的事件
  override def onTaskStart(taskStart: SparkListenerTaskStart): Unit = { }

  //下载任务结果的事件
  override def onTaskGettingResult(taskGettingResult: SparkListenerTaskGettingResult): Unit = { }

  //任务结束的事件
  override def onTaskEnd(taskEnd: SparkListenerTaskEnd): Unit = { }

  //job启动的事件
  override def onJobStart(jobStart: SparkListenerJobStart): Unit = { }

  //job结束的事件
  override def onJobEnd(jobEnd: SparkListenerJobEnd): Unit = { }

  //环境变量被更新的事件
  override def onEnvironmentUpdate(environmentUpdate: SparkListenerEnvironmentUpdate): Unit = { }

  //块管理被添加的事件
  override def onBlockManagerAdded(blockManagerAdded: SparkListenerBlockManagerAdded): Unit = { }

  override def onBlockManagerRemoved(
      blockManagerRemoved: SparkListenerBlockManagerRemoved): Unit = { }

  //取消rdd缓存的事件
  override def onUnpersistRDD(unpersistRDD: SparkListenerUnpersistRDD): Unit = { }

  //app启动的事件
  override def onApplicationStart(applicationStart: SparkListenerApplicationStart): Unit = { }

  //app结束的事件 [以下各事件也如同函数名所表达各个阶段被触发的事件不在一一标注]
  override def onApplicationEnd(applicationEnd: SparkListenerApplicationEnd): Unit = { } 

  override def onExecutorMetricsUpdate(
      executorMetricsUpdate: SparkListenerExecutorMetricsUpdate): Unit = { }

  override def onExecutorAdded(executorAdded: SparkListenerExecutorAdded): Unit = { }

  override def onExecutorRemoved(executorRemoved: SparkListenerExecutorRemoved): Unit = { }

  override def onExecutorBlacklisted(
      executorBlacklisted: SparkListenerExecutorBlacklisted): Unit = { }

  override def onExecutorUnblacklisted(
      executorUnblacklisted: SparkListenerExecutorUnblacklisted): Unit = { }

  override def onNodeBlacklisted(
      nodeBlacklisted: SparkListenerNodeBlacklisted): Unit = { }

  override def onNodeUnblacklisted(
      nodeUnblacklisted: SparkListenerNodeUnblacklisted): Unit = { }

  override def onBlockUpdated(blockUpdated: SparkListenerBlockUpdated): Unit = { }

  override def onOtherEvent(event: SparkListenerEvent): Unit = { }
```

示例代码

```scala
package org.apache.spark


import org.apache.spark.util.Utils
import org.apache.spark.internal.Logging
import org.apache.spark.scheduler.{SparkListener, SparkListenerApplicationEnd, SparkListenerApplicationStart}

/**
  * Created by cloud on 18/1/19.
  */
class MySparkAppListener(val sparkConf: SparkConf) extends SparkListener with Logging{

  override def onApplicationStart(applicationStart: SparkListenerApplicationStart): Unit = {
    val appId = applicationStart.appId
    logInfo(appId)
  }

  override def onApplicationEnd(applicationEnd: SparkListenerApplicationEnd): Unit = {
    logInfo("app end time " + applicationEnd.time)
  }

}
```

示例代码使用

```scala
// 设置spark.extraListeners参数即可
// 如果在代码中设置，则一定要设置在sparkContext初始化之前
// 因为监听器是在sparkContext初始化的时候加载的
object MyObject {
    def main(args : Array[String]) : Unit = {
        val sparkConf=new SparkConf()

        sparkConf.set("spark.extraListeners","org.apache.spark.MySparkAppListener")

        val sc = new SparkContext(sparkConf)
        .....
    }
}
```