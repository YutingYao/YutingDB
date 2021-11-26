package com.bainan.test

import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import java.util

/**
 *
 * @author Max
 * @date 2021/4/13 16:46
 */
class MyProcessor(name : String) extends ProcessWindowFunction [Message,ResultJson,String,TimeWindow]{

  val nameArray = name.split("-")
  val dataName = nameArray(1) match {
    case "Volt" => "电压"
    case "Curr" => "电流"
    case "Temp" => "温度"
    case "Ampl" => "振动幅度"
    case "Freq" => "振动频率"
  }
  val machineType = nameArray(0) match {
    case "d" => "动力机械臂"
    case "z" => "轴承电动机"
    case "a" => "AGV运载车"
  }
  override def process(key: String,
                       context: Context,
                       elements: Iterable[Message],
                       out: Collector[ResultJson]): Unit ={

    val elementList = new util.ArrayList[Element]()
    val list = elements.toList
    for( e <- list){
      elementList.add(Element(e.time, e.value))
    }

    val outJson = ResultJson(
      elements.head.machineNumber,
      elements.head.machineType,
      elements.head.name,
      "警告！"+machineType+elements.head.machineNumber+dataName+"值跳变过大，超过阈值！",
      elements.head.time,
      elementList
    )
    out.collect(outJson)
  }

}
