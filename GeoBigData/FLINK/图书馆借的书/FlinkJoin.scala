package com.kkb.flink.batch.demo2

import org.apache.flink.api.scala.ExecutionEnvironment

import scala.collection.mutable.ListBuffer

object FlinkJoin {

  def main(args: Array[String]): Unit = {
    //程序入口类，以及隐式转换包
    val environment: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    import org.apache.flink.api.scala._

    var firstSet =  ListBuffer[Tuple2[Int,String]]()
    firstSet.append((1,"zs"))
    firstSet.append((2,"ls"))
    firstSet.append((3,"ww"))

    var secondSet = ListBuffer[Tuple2[Int,String]]()
    secondSet.append((1,"beijing"))
    secondSet.append((2,"shanghai"))
    secondSet.append((4,"guangzhou"))

    //将两个集合申明成为两个dataset
    val data1: DataSet[(Int, String)] = environment.fromCollection(firstSet)
    val data2: DataSet[(Int, String)] = environment.fromCollection(secondSet)

    //将两个数据集进行join操作
    //先来执行左外连接
    //调用apply方法，将数据结果给取出来
  /*  data1.leftOuterJoin(data2).where(0).equalTo(0).apply((x,y) =>{
      if(y == null){
        //判断，如果右边的数据没有join上，那么就是为null值
        (x._1,x._2,"null")
      }else{
        (x._1,x._2,y._2)
      }
    }).print()
*/

    /**
      * 右外连接，以右边的表为主表，左边的表连接不上就是为空
      */
   /* data1.rightOuterJoin(data2).where(0).equalTo(0).apply((x,y)=>{
      if(x ==null){
        (y._1,"null",y._2)
      }else{
        (y._1,x._2,y._2)
      }
    }).print()*/


    /**
      * 满外连接
      */
    data1.fullOuterJoin(data2).where(0).equalTo(0).apply((x,y)=>{
      if(x ==null){
        (y._1,"null",y._2)
      }else if (y ==null){
        (x._1,x._2,"null")

      }else{
        (x._1,x._2,y._2)
      }
    }).print()
    environment.execute()
  }
}
