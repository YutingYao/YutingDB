package com.kkb.flink.stream.demo7

import java.text.SimpleDateFormat

import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.runtime.state.memory.MemoryStateBackend
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.scala.{DataStream, OutputTag, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup

import scala.collection.mutable.ArrayBuffer
import scala.util.Sorting

/**
  * 使用waterMark机制来实现乱序以及迟到数据的问题的解决
  */
object FlinkWaterMark {
  def main(args: Array[String]): Unit = {
    //程序的入口类以及隐式转换的包
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment


  //默认checkpoint功能是disabled的，想要使用的时候需要先启用//默认checkpoint功能是disabled的，想要使用的时候需要先启用

    // 每隔1000 ms进行启动一个检查点【设置checkpoint的周期】  每隔多长时间，保存一下我们数据状态
    environment.enableCheckpointing(1000)
    // 高级选项：
    // 设置模式为exactly-once （这是默认值）
    environment.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
    // 确保检查点之间有至少500 ms的间隔【checkpoint最小间隔】
    environment.getCheckpointConfig.setMinPauseBetweenCheckpoints(500)
    // 检查点必须在一分钟内完成，或者被丢弃【checkpoint的超时时间】
    environment.getCheckpointConfig.setCheckpointTimeout(60000)
    // 同一时间只允许进行一个检查点
    environment.getCheckpointConfig.setMaxConcurrentCheckpoints(1)
    // 表示一旦Flink处理程序被cancel后，会保留Checkpoint数据，以便根据实际需要恢复到指定的Checkpoint【详细解释见备注】

    /**
      * ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION:表示一旦Flink处理程序被cancel后，会保留Checkpoint数据，以便根据实际需要恢复到指定的Checkpoint
      * ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION: 表示一旦Flink处理程序被cancel后，会删除Checkpoint数据，只有job执行失败的时候才会保存checkpoint
      */
    environment.getCheckpointConfig.enableExternalizedCheckpoints(ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)


    //1、设置checkpoint保存的地方
    //environment.setStateBackend(new MemoryStateBackend())  //将数据保存到内存里面去，实际工作当中没人用

    //2、将checkPoint保存到文件系统  将数据保存到文件系统里面去
    //environment.setStateBackend(new FsStateBackend("hdfs://node01:8020/flink_state_save"))

    //3、将数据情况保存到RocksDB 里面去
    environment.setStateBackend(new RocksDBStateBackend("hdfs://node01:8020/flink_save_checkPoint/checkDir",true))




    import org.apache.flink.api.scala._

    //设置程序的处理的时间标准为eventTime
    environment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    environment.setParallelism(1)

    //读取socket里面的数据
    val sourceStream: DataStream[String] = environment.socketTextStream("node01",9000).uid("my_source")

    val tupleStream: DataStream[(String, Long)] = sourceStream.map(x => {
      val strings: Array[String] = x.split(" ")
      (strings(0), strings(1).toLong)

    }).uid("mapuid")


    //给数据注册waterMark
    val waterMarkStream: DataStream[(String, Long)] = tupleStream.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks[(String, Long)] {
      /**
        * 定义AssignerWithPeriodicWatermarks 这个内部类，需要实现两个方法
        * 第一个方法getCurrentWatermark 获取当前的水印位置
        *
        * @return
        */
      var currentTimemillis: Long = 0L //定义的最大的时间
      var timeDiff: Long = 10000L //定义允许数据乱序的最大时间
      val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

      override def getCurrentWatermark: Watermark = {
        val watermark = new Watermark(currentTimemillis - timeDiff)
        watermark
      }

      /**
        * 抽取eventTime
        *
        * @param l
        * @return
        */
      override def extractTimestamp(element: (String, Long), l: Long): Long = {
        currentTimemillis = Math.max(currentTimemillis, element._2)
        val id = Thread.currentThread().getId
        println("currentThreadId:" + id + ",key:" + element._1 + ",eventtime:[" + element._2 + "|" + sdf.format(element._2) + "],currentMaxTimestamp:[" + currentTimemillis + "|" + sdf.format(currentTimemillis) + "],watermark:[" + this.getCurrentWatermark.getTimestamp + "|" + sdf.format(this.getCurrentWatermark.getTimestamp) + "]")
        element._2 //获取eventTime然后返回即可
      }
    })
    /*waterMarkStream
      .keyBy(0)
      .window(TumblingEventTimeWindows.of(Time.seconds(10)))  //使用滚动窗口，每隔10S钟往前滚动一次
      .allowedLateness(Time.seconds(2))  //第二种迟到数据处理策略：指定延迟2秒钟的数据都可以接着被处理
      .apply(new MyWindowFunction)
      .print()  //如果打印出  “看到这个结果，就证明窗口已经运行了” 这些字就说明窗口已经运行了
*/

    val outputTag: OutputTag[(String, Long)] = new OutputTag[(String,Long)]("late_data")
    val outputWindow: DataStream[String] = waterMarkStream
      .keyBy(0)
      .window(TumblingEventTimeWindows.of(Time.seconds(3)))
      // .allowedLateness(Time.seconds(2))//允许数据迟到2S
      .sideOutputLateData(outputTag)
      //function: (K, W, Iterable[T], Collector[R]) => Unit
      .apply(new MyWindowFunction)


    val sideOuptut: DataStream[(String, Long)] = outputWindow.getSideOutput(outputTag)

    sideOuptut.print()


    outputWindow.print()




    //执行程序
    environment.execute()
  }


}

//org.apache.flink.streaming.api.scala.function
//IN, OUT, KEY, W <: Window
class MyWindowFunction  extends WindowFunction[(String,Long),String,Tuple,TimeWindow]{
  /**
    * 自定义一个class类来继承WindowFunction
    * @param key  输入的数据类型
    * @param window  窗口
    * @param input  窗口里面所有的数据，都封装在了input里面了
    * @param out   输出的数据都是通过out进行输出
    */
  override def apply(key: Tuple, window: TimeWindow, input: Iterable[(String, Long)], out: Collector[String]): Unit = {
      window.getStart//获取窗口的起始时间
    window.getEnd  //获取窗口结束时间
    val keyStr = key.toString
    val arrBuf = ArrayBuffer[Long]()

    val ite = input.iterator

    while (ite.hasNext){
      val tup2 = ite.next()
      arrBuf.append(tup2._2)
    }
    val arr = arrBuf.toArray
    Sorting.quickSort(arr)  //对数据进行排序，按照eventTime进行排序
    val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    val result = "聚合数据的key为："+keyStr + "," + "窗口当中数据的条数为："+arr.length + "," + "窗口当中第一条数据为："+sdf.format(arr.head) + "," +"窗口当中最后一条数据为："+ sdf.format(arr.last)+ "," + "窗口起始时间为："+sdf.format(window.getStart) + "," + "窗口结束时间为："+sdf.format(window.getEnd)  + "！！！！！看到这个结果，就证明窗口已经运行了"
    out.collect(result)

  }
}

