package com.msb.cityTraffic.warning

/**
 * ClassName CarTrackInfoAnalysis
 * Description
 * Create by liudz
 * Date 2020/9/14 3:03 下午
 */
import java.sql.DriverManager
import java.util
import java.util.Properties

import com.msb.cityTraffic.utils._
import org.apache.flink.api.common.functions.{RichFilterFunction, RichMapFunction}
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.datastream.BroadcastStream
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.util.Collector
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.util.Bytes


object  CarTrackInfoAnalysis{

  def main(args: Array[String]): Unit = {
    val streamEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.streaming.api.scala._
    streamEnv.setParallelism(1)




    //    val props = new Properties()
    //    props.setProperty("bootstrap.servers","hadoop101:9092,hadoop102:9092,hadoop103:9092")
    //    props.setProperty("group.id","msb_001")


    //创建一个Kafka的Source
    //    val stream1: DataStream[TrafficInfo] = streamEnv.addSource(
    //      new FlinkKafkaConsumer[String]("t_traffic_msb", new SimpleStringSchema(), props).setStartFromEarliest() //从第一行开始读取数据
    //    )
    //    val stream1: DataStream[TrafficInfo] = streamEnv.socketTextStream("hadoop101",9999)
    val stream1: DataStream[TrackInfo] = streamEnv.readTextFile("D:\\IdeaProjects\\trafficmonitor_msb_2\\src\\main\\resources\\log_2020-06-21_0.log")
      .map(line => {
        var arr = line.split(",")
        new TrafficInfo(arr(0).toLong, arr(1), arr(2), arr(3), arr(4).toDouble, arr(5), arr(6))
      })
      .filter(new MyViolationRichFilterFunction()) //留下违法的车辆信息
      .map(info =>{
        new TrackInfo(info.car,info.actionTime,info.monitorId,info.roadId,info.areaId,info.speed)
      })

    //批量写入数据到Hbase表中，启动CountWindow 来完成批量插入，批量的条数由窗口大小决定
    //开窗
    stream1.countWindowAll(10) //全局的count窗口
      .apply(
        (win:GlobalWindow,input:Iterable[TrackInfo],out: Collector[java.util.List[Put]]) =>{
          var list = new util.ArrayList[Put]()
          for(info<-input){
            //在hbase表中为了方便查询每辆车最近的车辆轨迹，根据车辆通行的时间降序排序
            //rowkey 车牌号+ (Long.maxValue - actionTime)
            var put = new Put(Bytes.toBytes(info.car+"_"+(Long.MaxValue - info.actionTime)))
            put.add("cf1".getBytes(),"car".getBytes(),Bytes.toBytes(info.car))
            put.add("cf1".getBytes(),"actionTime".getBytes(),Bytes.toBytes(info.actionTime))
            put.add("cf1".getBytes(),"monitorId".getBytes(),Bytes.toBytes(info.monitorId))
            put.add("cf1".getBytes(),"roadId".getBytes(),Bytes.toBytes(info.roadId))
            put.add("cf1".getBytes(),"areaId".getBytes(),Bytes.toBytes(info.areaId))
            put.add("cf1".getBytes(),"speed".getBytes(),Bytes.toBytes(info.speed))
            list.add(put)
          }
          print(list.size()+",")
          out.collect(list)
        }
      )
      .addSink(new HbaseWriterDataSink)

    streamEnv.execute()

  }

  //自定义的过滤函数类，把违法车辆信息留下，其他都去掉
  class MyViolationRichFilterFunction extends RichFilterFunction[TrafficInfo]{
    //map集合违法车辆信息
    var map  = scala.collection.mutable.Map[String,ViolationInfo]()
    //一次性从数据库中读所有的违法车辆信息列表存放到Map集合中，open函数在计算程序初始化的时候调用的，
    override def open(parameters: Configuration): Unit = {
      var conn = DriverManager.getConnection("jdbc:mysql://localhost/traffic_monitor","root","123123")
      var pst = conn.prepareStatement("select car ,violation, create_time from t_violation_list")
      var set =pst.executeQuery()
      while (set.next()){
        var info =new ViolationInfo(set.getString(1),set.getString(2),set.getLong(3))
        map.put(info.car,info)
      }
      set.close()
      pst.close()
      conn.close()
    }

    //过滤
    override def filter(t: TrafficInfo): Boolean = {
      val o: Option[ViolationInfo] = map.get(t.car)
      if(o.isEmpty){
        false
      }else{
        true
      }
    }
  }
}
