package com.bigdata.traffic.util

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}
import java.text.SimpleDateFormat
import java.util.Date

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}

class JdbcWriterDataSink[T](classType:Class[_<:T]) extends RichSinkFunction[T]{
  var conn :Connection =_
  var pst:PreparedStatement =_

  override def open(parameters: Configuration): Unit = {
    conn =DriverManager.getConnection("jdbc:mysql://localhost/traffic_monitor","root","123123")
    if(classType.getName.equals(classOf[OutOfLimitSpeedInfo].getName)){
      pst = conn.prepareStatement("insert into t_speeding_info (car,monitor_id,road_id,real_speed,limit_speed,action_time) values (?,?,?,?,?,?)")
    }
    if(classType.getName.equals(classOf[AvgSpeedInfo].getName)){
      pst = conn.prepareStatement("insert into t_average_speed (start_time,end_time,monitor_id,avg_speed,car_count)  values (?,?,?,?,?)")
    }
    if(classType.getName.equals(classOf[RepetitionCarWarningInfo].getName)){
      pst = conn.prepareStatement("insert into t_violation_list (car,violation,create_time)  values (?,?,?)")
    }
    if(classType.getName.equals(classOf[ViolationInfo].getName)){
      pst = conn.prepareStatement("insert into t_violation_list (car,violation,create_time)  values (?,?,?)")
    }
  }

  override def close(): Unit = {
    pst.close()
    conn.close()
  }

  override def invoke(value: T, context: SinkFunction.Context[_]): Unit = {
    if(classType.getName.equals(classOf[OutOfLimitSpeedInfo].getName)){
      val info: OutOfLimitSpeedInfo = value.asInstanceOf[OutOfLimitSpeedInfo]
      pst.setString(1,info.car)
      pst.setString(2,info.monitorId)
      pst.setString(3,info.roadId)
      pst.setDouble(4,info.realSpeed)
      pst.setInt(5,info.limitSpeed)
      pst.setLong(6,info.actionTime)
      pst.executeUpdate()
    }
    if(classType.getName.equals(classOf[AvgSpeedInfo].getName)){
      val info: AvgSpeedInfo = value.asInstanceOf[AvgSpeedInfo]
      pst.setLong(1,info.start)
      pst.setLong(2,info.end)
      pst.setString(3,info.monitorId)
      pst.setDouble(4,info.avgSpeed)
      pst.setInt(5,info.carCount)
      pst.executeUpdate()
    }
    if(classType.getName.equals(classOf[RepetitionCarWarningInfo].getName)){
      val info: RepetitionCarWarningInfo = value.asInstanceOf[RepetitionCarWarningInfo]
      pst.setString(1,info.car)
      pst.setString(2,info.warnMsg)
      pst.setLong(3,info.warningTime)
      pst.executeUpdate()
    }
    if(classType.getName.equals(classOf[ViolationInfo].getName)){
      val info: ViolationInfo = value.asInstanceOf[ViolationInfo]
      //在一天内，同一个车牌如果存在危险驾驶的告警，只要往数据库输入一条
      val format = new SimpleDateFormat("yyy-MM-dd")
      var day :Date =format.parse( format.format(new Date(info.createTime)) ) //得到当前的0时0分0秒
      //先查询数据库中是否有同样车牌的数据
      var selectPst = conn.prepareStatement("select count(1),out_count from t_violation_list where car=? and create_time between ? and ?")
      selectPst.setString(1,info.car)
      selectPst.setLong(2,day.getTime)
      selectPst.setLong(3,day.getTime+(24*60*60*1000))
      var set:ResultSet = selectPst.executeQuery()
      if(set.next()){
        if(set.getInt(1) ==0){
          pst.setString(1,info.car)
          pst.setString(2,info.msg)
          pst.setLong(3,info.createTime)
          pst.executeUpdate()
        }else{
          var count = set.getInt(2)
          if(count< info.outCount){
            var updatePst = conn.prepareStatement("update t_violation_list set out_count=? where car=?")
            updatePst.setInt(1,info.outCount)
            updatePst.setString(2,info.car)
            updatePst.executeUpdate()
            updatePst.close()
          }
        }
      }
      set.close()
      selectPst.close()


    }
  }
}
