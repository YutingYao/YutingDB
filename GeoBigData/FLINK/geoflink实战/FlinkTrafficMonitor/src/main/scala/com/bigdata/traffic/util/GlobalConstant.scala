package com.bigdata.traffic.util

import org.apache.flink.api.common.state.MapStateDescriptor

/**
 * 项目中全局的常量
 */
object GlobalConstant {
  //状态描述对象
  val MONITOR_LIMIT_STATE_DESCRIPTOR= new MapStateDescriptor[String,MonitorLimitInfo]("monitor_state",classOf[String],classOf[MonitorLimitInfo])
  val VIOLATION_STATE_DESCRIPTOR= new MapStateDescriptor[String,ViolationInfo]("violation_state",classOf[String],classOf[ViolationInfo])

}

//车辆经过卡口的日志数据
case class TrafficLog(actionTime:Long,monitorId:String,cameraId:String,car:String,speed:Double,roadId:String,areaId:String)
//卡口限速信息类
case class MonitorLimitInfo(monitorId:String,roadId:String,speedLimit:Int,areaId:String)

//超速的样例类
case class OutOfLimitSpeedInfo(car:String,monitorId:String,roadId:String,realSpeed:Double,limitSpeed:Int,actionTime:Long)
//卡口的平均车速对象
case class AvgSpeedInfo(start:Long,end:Long,monitorId:String,avgSpeed:Double,carCount:Int)
//套牌车告警信息类
case class RepetitionCarWarningInfo(car:String,firstMonitor:String,secondMonitor:String,warningTime:Long,warnMsg:String)

//车辆危险驾驶的信息
case class ViolationInfo(car:String,msg:String,createTime:Long,outCount:Int)

//车辆轨迹数据
case class TrackInfo(car:String,actionTime:Long,monitorId:String,roadId:String,areaId:String,speed:Double)

/*
* Hbase表设计 t_track_info  ,rowkey: car_(Long.max-actionTime) 自动按照时间降序排序 cf1：
 */