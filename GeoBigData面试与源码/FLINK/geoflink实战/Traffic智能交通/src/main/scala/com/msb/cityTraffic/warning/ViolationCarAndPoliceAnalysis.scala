package com.msb.cityTraffic.warning

/**
 * ClassName ViolationCarAndPoliceAnalysis
 * Description
 * Create by liudz
 * Date 2020/9/14 3:05 下午
 */
import com.msb.cityTraffic.utils.ViolationInfo
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

//出警记录对象
case class PoliceAction(policeId:String,car:String,actionStatus:String,actionTime:Long)

/**
 * 违法车辆和交警出警分析
 * 第一种，当前的违法车辆（在5分钟内）如果已经出警了。（最后输出道主流中做删除处理）。
 * 第二种，当前违法车辆（在5分钟后）交警没有出警（发出出警的提示，在侧流中发出）。
 * 第三种，有交警的出警记录，但是不是由监控平台报的警。
 * 需要两种数据流：
 * 1、系统的实时违法车辆的数据流
 * 2、交警实时出警记录数据
 */
object ViolationCarAndPoliceActionAnalysis {

  def main(args: Array[String]): Unit = {
    val streamEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.streaming.api.scala._
    streamEnv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    streamEnv.setParallelism(1)

    val stream1: DataStream[ViolationInfo] =  streamEnv.socketTextStream("hadoop101",9999)
      .map(line=>{
        val arr: Array[String] = line.split(",")
        new ViolationInfo(arr(0),arr(1),arr(2).toLong)
      })
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[ViolationInfo](Time.seconds(2)) {
        override def extractTimestamp(element: ViolationInfo) = element.createTime
      })

    val stream2: DataStream[PoliceAction] =  streamEnv.socketTextStream("hadoop101",8888)
      .map(line=>{
        val arr: Array[String] = line.split(",")
        new PoliceAction(arr(0),arr(1),arr(2),arr(3).toLong)
      }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[PoliceAction](Time.seconds(2)) {
      override def extractTimestamp(element: PoliceAction) = element.actionTime
    })

    //需要两个数据流的连接，而且这两个中数据流有连接条件：PoliceAction.car =ViolationInfo.car
    //这个连接相当于数据库中的内连接

    stream1.keyBy(_.car)
      .intervalJoin(stream2.keyBy(_.car)) //内连接 intervalJoin只能应用在keyedStream，普通的DataStream流连接只能使用connect
      .between(Time.seconds(-5),Time.seconds(5)) //设置一个时间边界，在这个边界中，两个流的数据自动关联,为了便于测试，使用5秒替代5分钟 ,不和Watermark的延迟有关系
      .process(new ProcessJoinFunction[ViolationInfo,PoliceAction,String] {
        //在时间边界内，存在车辆号码相同的两中数据，
        override def processElement(left: ViolationInfo, right: PoliceAction, ctx: ProcessJoinFunction[ViolationInfo, PoliceAction, String]#Context, out: Collector[String]) = {
          out.collect(s"车辆${left.car},已经有交警出警了，警号为:${right.policeId},出警的状态是：${right.actionStatus},出警的时间:${right.actionTime}")
        }
      })
      .print()

    streamEnv.execute()







  }
}
