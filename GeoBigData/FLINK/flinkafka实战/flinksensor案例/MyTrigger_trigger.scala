package com.bainan.test

import org.apache.flink.api.common.state.{ListState, ListStateDescriptor, MapState, MapStateDescriptor, ValueState, ValueStateDescriptor}
import org.apache.flink.streaming.api.windowing.triggers.{Trigger, TriggerResult}
import org.apache.flink.streaming.api.windowing.windows.TimeWindow

/**
 * 时间窗口触发器
 * dataStream中数值存在max和min中
 * onProcessingTime变化超过 thresholdValue 触发
 * @author Max
 * @date 2021/4/1 12:26
 */
class MyTrigger(thresholdValue : Double) extends Trigger[Message, TimeWindow] {
  //需要保持状态，传入一个数值状态Descriptor
  val maxStateDesc = new ValueStateDescriptor[Double]("maxValue", classOf[Double])
  val minStateDesc = new ValueStateDescriptor[Double]("minValue", classOf[Double])

  //当某窗口增加一个元素时调用onElement方法，返回一个TriggerResult
  override def onElement(element: Message,
                         time: Long,
                         window: TimeWindow,
                         triggerContext: Trigger.TriggerContext): TriggerResult = {
    //保存数值状态
    val maxValueState: ValueState[Double] = triggerContext.getPartitionedState(maxStateDesc)
    val minValueState: ValueState[Double] = triggerContext.getPartitionedState(minStateDesc)

    //给最小记录值一个初始大数
    if(Option(minValueState.value()).isEmpty){
      minValueState.update(thresholdValue)
    }
    //跟新最大和最小值记录
    if(element.value > maxValueState.value()){
      maxValueState.update(element.value)
    }else if(element.value < minValueState.value()){
      minValueState.update(element.value)
    }

    //只做记录，全部continue
    TriggerResult.CONTINUE
  }

  // 我们用EventTime
  override def onEventTime(time: Long, window: TimeWindow, triggerContext: Trigger.TriggerContext): TriggerResult = {
    //查看数值状态
    val maxValueState: ValueState[Double] = triggerContext.getPartitionedState(maxStateDesc)
    val minValueState: ValueState[Double] = triggerContext.getPartitionedState(minStateDesc)

    if((maxValueState.value() - minValueState.value()) > thresholdValue){
      println("onProcessingTime start " + (maxValueState.value() - minValueState.value()))

      return TriggerResult.FIRE_AND_PURGE
    }
    TriggerResult.PURGE

  }

  override def onProcessingTime(time: Long, window: TimeWindow, triggerContext: Trigger.TriggerContext): TriggerResult = {
    TriggerResult.CONTINUE
  }

  override def clear(window: TimeWindow, triggerContext: Trigger.TriggerContext): Unit = {
    val maxValueState: ValueState[Double] = triggerContext.getPartitionedState(maxStateDesc)
    val minValueState: ValueState[Double] = triggerContext.getPartitionedState(minStateDesc)
    maxValueState.clear()
    minValueState.clear()
  }
}


