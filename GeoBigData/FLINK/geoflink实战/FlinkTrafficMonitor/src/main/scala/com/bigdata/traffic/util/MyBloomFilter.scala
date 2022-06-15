package com.bigdata.traffic.util

import java.nio.charset.Charset

import com.google.common.hash.Hashing

/**
 *
 * @param numBits 二进制数组的长度
 */
class MyBloomFilter(numBits:Long) extends Serializable {

  /**
   * 根据车牌，来计算布隆过滤器中对应的位（下标）
   * @param car
   * @return
   */
  def getOffset(car:String):Long ={
    var hashValue = googleHash(car)
    if(hashValue<0){ //hash值可能是负数,补码
      hashValue = ~ hashValue
    }
    var bit:Long = hashValue % numBits
    bit
  }

  /**
   * 谷歌提供的函数算法，函数重复的可能性很低
   * @param car
   * @return
   */
  def  googleHash(car:String) :Long ={
    Hashing.murmur3_128(1).hashString(car,Charset.forName("UTF-8")).asLong()
  }
}
