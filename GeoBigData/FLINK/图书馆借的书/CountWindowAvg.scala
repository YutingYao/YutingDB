package com.kkb.flink.stream.demo6

import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.datastream.DataStreamSink
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow
import org.apache.flink.util.Collector

/**
  * 求取每3条数据的平均值
  * 获取socket当中的数据，每3条数据，统计平均值
  */
object CountWindowAvg {
  def main(args: Array[String]): Unit = {
    //获取程序入口类
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //导入隐式转换的包
    import org.apache.flink.api.scala._

    val sourceStream: DataStream[String] = environment.socketTextStream("node01",9000)

    val avgResult: DataStreamSink[Double] = sourceStream.map(x => (1, x.toInt)).keyBy(0)
      .countWindow(3) //调用countWindow来进行数据的处理，如果传入一个参数，就是滚动窗口，没有重复数据
      //.countWindow(3,2)  //调用countWindow进行数据处理，传入两个参数，就是滚动窗口，窗口长度为3，滚动的条件2
      .process(new MyProcessWindow)
      .print()

    environment.execute()




  }



}

/**
  * *
  * * @tparam IN The type of the input value.
  * * @tparam OUT The type of the output value.
  * * @tparam KEY The type of the key.
  * * @tparam W The type of the window.
  */
class MyProcessWindow extends ProcessWindowFunction[(Int,Int),Double,Tuple,GlobalWindow]{
  /**
    *
    * @param key  定义我们聚合的key
    * @param context  上下文对象，用于将我们的数据进行一些上下文的获取
    * @param elements  传入的数据
    * @param out   通过collector将我们处理之后的结果收集起来
    */
  override def process(key: Tuple, context: Context, elements: Iterable[(Int, Int)], out: Collector[Double]): Unit = {

    //用于统计一共有多少条数据
    var totalNum:Int = 0;
    //用于定义我们所有数据的累加的和
    var totalResult:Int = 0;

    for(element <- elements){
      totalNum += 1
      totalResult += element._2

    }

    out.collect(totalResult/totalNum)

  }
}



