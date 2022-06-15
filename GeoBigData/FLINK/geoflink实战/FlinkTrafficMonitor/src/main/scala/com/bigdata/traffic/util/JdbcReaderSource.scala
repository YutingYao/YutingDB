package com.bigdata.traffic.util

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}

/**
 * 专门从Mysql数据库读取数据作为一个数据流，不一定MonitorLimitInfo类，
 * @param classType
 * @tparam T
 */
class JdbcReaderSource[T](classType:Class[_<:T]) extends RichSourceFunction[T]{
  var flag:Boolean = true
  var conn :Connection =_
  var pst:PreparedStatement =_
  var ret :ResultSet =_
  override def run(ctx: SourceFunction.SourceContext[T]): Unit = {
    while (flag){
      ret = pst.executeQuery()
      while (ret.next()){
        if(classType.getName.equals(classOf[MonitorLimitInfo].getName)){
          var info = new MonitorLimitInfo(ret.getString(1),ret.getString(2),ret.getInt(3),ret.getString(4))
          val o: T = info.asInstanceOf[T] //类型转换
          ctx.collect(o)
        }
        if(classType.getName.equals(classOf[ViolationInfo].getName)){
          var info = new ViolationInfo(ret.getString("car"),ret.getString("violation"),ret.getLong("create_time"),ret.getInt("out_count"))
          val o: T = info.asInstanceOf[T] //类型转换
          ctx.collect(o)
        }
      }

      //
      ret.close()
      Thread.sleep(60*60*1000) //休眠一个小时更新一下数据，从数据库中再读一次
    }
  }

  override def cancel(): Unit = {
    flag =false
  }

  override def open(parameters: Configuration): Unit = {
    conn =DriverManager.getConnection("jdbc:mysql://localhost/traffic_monitor","root","123456")
    if(classType.getName.equals(classOf[MonitorLimitInfo].getName)){
      pst = conn.prepareStatement("select * from t_monitor_info where speed_limit>0")
    }
    if(classType.getName.equals(classOf[ViolationInfo].getName)){
      pst = conn.prepareStatement("select * from t_violation_list")
    }
  }

  override def close(): Unit = {
    pst.close()
    conn.close()
  }
}
