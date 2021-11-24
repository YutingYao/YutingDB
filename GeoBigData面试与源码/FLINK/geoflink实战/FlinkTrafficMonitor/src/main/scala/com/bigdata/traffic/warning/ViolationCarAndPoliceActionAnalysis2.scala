package com.bigdata.traffic.warning

import com.bigdata.traffic.util.ViolationInfo
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction
import org.apache.flink.util.Collector

/**
 * 需求：违法车辆处理结果情况汇报，
 * 第一种，当前的违法车辆（在5分钟内）如果已经出警了。（最后输出道主流中做删除处理）。
 * 第二种，当前违法车辆（在5分钟后）交警没有出警（发出出警的提示，在侧流中发出第一个侧流）。
 * 第三种，有交警的出警记录，但是没有找到对应的违法车辆。(第二次侧流中)
 */
object ViolationCarAndPoliceActionAnalysis2 {

  import org.apache.flink.streaming.api.scala._
  lazy val secondTag =new OutputTag[ViolationInfo]("No Police Action")
  lazy val threeTag =new OutputTag[PoliceAction]("No Violation")

  def main(args: Array[String]): Unit = {
    val streamEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    streamEnv.setParallelism(1)
    streamEnv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    //导入隐式转换

    //首先读取违法车辆的数据流
    val violationStream: DataStream[ViolationInfo] = streamEnv.socketTextStream("hadoop101",8888)
      .map(line => {
        val arr: Array[String] = line.split(",")
        new ViolationInfo(arr(0), arr(1), arr(2).toLong,0)
      })
      .assignAscendingTimestamps(_.createTime) //违法车辆报警的时间



    //读取警察出警记录数据
    val policeActionStream: DataStream[PoliceAction] = streamEnv.socketTextStream("hadoop102",8888)
      .map(line => {
        val arr: Array[String] = line.split(",")
        new PoliceAction(arr(0), arr(1),arr(2),arr(3).toLong)
      })
      .assignAscendingTimestamps(_.actionTime) //交警到达现场的时间

    //把同一个车牌号的两条不同类型的数据分到一组
    val result: DataStream[String] = violationStream.keyBy(_.car).connect(policeActionStream.keyBy(_.car))
      .process(new JoinStreamFunction)

    result.print("main")

    result.getSideOutput(secondTag).print("第二种情况")
    result.getSideOutput(threeTag).print("第三种情况")

    streamEnv.execute()

  }

  class JoinStreamFunction extends KeyedCoProcessFunction[String,ViolationInfo,PoliceAction,String]{
    //需要两个状态保存，分别保存违法数据和出警记录
    lazy val violationState:ValueState[ViolationInfo] =getRuntimeContext.getState(new ValueStateDescriptor[ViolationInfo]("violation_state",classOf[ViolationInfo]))
    lazy val policeActionState:ValueState[PoliceAction] =getRuntimeContext.getState(new ValueStateDescriptor[PoliceAction]("police_state",classOf[PoliceAction]))


    //当违法数据流中有一条Violation数据进入，则自动调用该方法
    override def processElement1(value: ViolationInfo, ctx: KeyedCoProcessFunction[String, ViolationInfo, PoliceAction, String]#Context, out: Collector[String]): Unit = {
      println(value)
      val policeAction: PoliceAction = policeActionState.value()
      if(policeAction==null){ //有违法的数据，但是没有与之对应的出警记录
        ctx.timerService().registerEventTimeTimer(value.createTime+5000)//注册一个触发器，默认在5秒后触发，除非，在5秒内删除当前触发器。
        violationState.update(value)
      }else{ //来了一条违法数据，同时也找到与之对应的出警记录
        out.collect(s"车辆${value.car},已经出警了，警号为:${policeAction.policeId},出警状态为:${policeAction.actionStatus},出警时间是:${policeAction.actionTime}")
        violationState.clear()
        policeActionState.clear()
      }
    }

    //当警察出警数据流中有一条PoliceAction数据进入，则自动调用该方法
    override def processElement2(pa: PoliceAction, ctx: KeyedCoProcessFunction[String, ViolationInfo, PoliceAction, String]#Context, out: Collector[String]): Unit = {
      println(pa)
      val v: ViolationInfo = violationState.value()
      if(v==null){//没有找对与之对应的，输出到侧流（three侧流）
        ctx.timerService().registerEventTimeTimer(pa.actionTime+5000)
        policeActionState.update(pa)
      }else{
        out.collect(s"车辆${v.car},已经出警了，警号为:${pa.policeId},出警状态为:${pa.actionStatus},出警时间是:${pa.actionTime}")
        violationState.clear()
        policeActionState.clear()
      }
    }

    override def onTimer(timestamp: Long, ctx: KeyedCoProcessFunction[String, ViolationInfo, PoliceAction, String]#OnTimerContext, out: Collector[String]): Unit = {
      val v: ViolationInfo = violationState.value()
      val pa: PoliceAction = policeActionState.value()
      println("触发器开始出发"+v+pa)
      if(v==null&&pa!=null){ //有出警的记录，但是违法信息不是平台输出
        ctx.output(threeTag,pa)
      }
      if(pa==null&& v!=null){ //有违法车辆信息，但是已经过了5秒，没有出警的记录数据
        ctx.output(secondTag,v)
      }
      if(v!=null && pa!=null){
        out.collect(s"车辆${v.car},已经出警了，警号为:${pa.policeId},出警状态为:${pa.actionStatus},出警时间是:${pa.actionTime}")
      }
      //如果存在两种数据都不为空，触发器不能执行。触发器不输出任何数据
      policeActionState.clear()
      violationState.clear()
    }
  }
}
