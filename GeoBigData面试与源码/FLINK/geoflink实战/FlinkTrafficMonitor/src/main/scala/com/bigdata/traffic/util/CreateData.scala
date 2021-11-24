package com.bigdata.traffic.util

import java.io.{FileOutputStream, OutputStreamWriter, PrintWriter}
import java.text.SimpleDateFormat
import java.util.{Date, Properties}

import org.apache.commons.math3.random.{GaussianRandomGenerator, JDKRandomGenerator}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

import scala.util.Random

object CreateData {

  def main(args: Array[String]): Unit = {
    //所有车辆的车速：必须呈现一个高斯分布的
    //每辆车经过的卡口数量也应该是呈现一个高斯分布
    //数据直接生成到Kafka中，同时生成到文件
    var out:FileOutputStream=null
    var pw:PrintWriter =null
    val r = new Random()
    var producer :KafkaProducer[Nothing,String] = null

    //定义连接Kafka的属性
    val props = new Properties()
    props.setProperty("bootstrap.servers","hadoop101:9092,hadoop102:9092")
    props.setProperty("key.serializer",classOf[StringSerializer].getName)
    props.setProperty("value.serializer",classOf[StringSerializer].getName)
    try{
      //初始化写入文件的输出流
//      print(getClass.getResource("/traffic_data").getPath)
//      out = new FileOutputStream("D:/IdeaProjects/TrafficMonitor0601/target/classes/traffic_data")
//      pw = new PrintWriter(new OutputStreamWriter(out,"UTF-8"))
      producer =new KafkaProducer[Nothing,String](props)

      val locations = Array("京","鲁","京","皖","京","京","沪","京","京","豫","湘","京","京")
      val day = new SimpleDateFormat("yyyy-MM-dd").format(new Date) //当天的年月日

      val rg = new JDKRandomGenerator()
      rg.setSeed(1) //设置随机数的种子
      var grg:GaussianRandomGenerator = new GaussianRandomGenerator(rg) //初始化高斯分布的随机数对象

      //假设一天有30万量车上路
      for(i<-1 to 300000){
        //的到车牌
        var car = locations(r.nextInt(locations.size)) + (65+r.nextInt(26)).toChar +"%05d".format(r.nextInt(100000))
        //得到一个随机的车辆向上的起始时间，精确到小时
        var startHour = "%02d".format(r.nextInt(24))

        //得到(标准)正太分布的随机数,模拟每辆车经过的卡口数据
        var g = grg.nextNormalizedDouble() //大概率 从-1 到 1 的随机数
        //大多数的车辆一天经过30个左右的卡口
        var m_count = (30+(30*g)).abs.toInt+1 //均值为30的高斯分布的随机数
        for(j<-1 to m_count){ //一辆车每经过一个卡口留下一条数据

          //m_count 很可能超过30卡口，多的话可以超过100个以上,每经过30个卡口小时加1,
          if(j %30==0){
            var newHour:Int = startHour.toInt+1
            if(newHour==24){
              newHour = 0
            }
            startHour = "%02d".format(newHour)
          }
          //得到车辆经过卡口的时间
          var actionTime =day+" "+startHour+":"+"%02d".format(r.nextInt(60))+":"+"%02d".format(r.nextInt(60))
          //得到时间戳
          var actionTime2:Date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(actionTime)

          //随机的卡口号
          var monitorId = "%04d".format(r.nextInt(100)) //模拟经过30-50个卡口
          //随机车速
          var g2 =grg.nextNormalizedDouble()
          var speed = ((50+(50*g2)).abs+1).formatted("%.1f")

          //随机道路ID
          var roadId = "%03d".format(r.nextInt(1000))
          //随机的通道ID
          var cameraId = "%05d".format(r.nextInt(100000))
          //随机的区域ID
          var areaId = "%02d".format(r.nextInt(8))

          var content =actionTime2.getTime+","+monitorId+","+cameraId+","+car+","+speed+","+roadId+","+areaId

          //写入文件
//          pw.write(content+"\n")
          //写入Kafka
          var record = new ProducerRecord("t0601_traffic",content)
          producer.send(record)
        }
//        pw.flush()
      }
//      pw.flush()
    }catch {
      case e:Exception=> e.printStackTrace()
    }finally {
      pw.close()
      out.close()
      producer.close()
    }

  }
}
