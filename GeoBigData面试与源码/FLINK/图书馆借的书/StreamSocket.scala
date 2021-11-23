package com.kkb.flink.stream.demo1

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
  * 接受node01服务器上面的socket里面的数据，然后实现单词计数统计
  */
object StreamSocket {
  def main(args: Array[String]): Unit = {
    //流式处理，程序入口类 StreamExecutionEnvironment

    //获取流式处理的对象
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //注意：在flink的流失处理里面，需要导入隐式转换包，不然会报错
    import org.apache.flink.api.scala._


    //获取socket里面的的数据  hello  world
    //  test  abc
    val socketStream: DataStream[String] = environment.socketTextStream("node01",9000)


    //然后对数据进行单词计数统计
    val result: DataStream[(String, Int)] = socketStream.flatMap(x => x.split(" "))
      .map(x => (x, 1)) //将每个出现的单词记做一次
      .keyBy(0) //使用keyby将我们的单词进行分组 类似于 group by
      .sum(1)//对下标为1的数据进行求和
    result.print()  //调用print触发flink的整个任务去执行
    //执行程序
    environment.execute()


  }
}
