import java.util
import java.util.Random

import com.google.common.hash.{BloomFilter, Funnels}

object Test {

  def main(args: Array[String]): Unit = {

    var sizeOfNumberSet :Int =1<<25 //二进制向量的长度

    //谷歌的
//    val bf = BloomFilter.create(Funnels.stringFunnel(),sizeOfNumberSet)
//    println(sizeOfNumberSet)
//    Array("abc","efg","abc","hij","hello","hive","hello").foreach(word=>{
//
//      val bool = bf.mightContain(word)
//      if(bool){
//        println(s"${word}包含")
//      }else{
//        println(s"${word}不包含")
//        bf.put(word)
//      }
//    })

    //JDK
    val bitSet = new util.BitSet(sizeOfNumberSet)
    Array("abc","efg","abc","hij","hello","hive","hello").foreach(word=>{
        val bool = bitSet.get(word.hashCode)
        if(bool){
          println(s"${word}包含")
        }else{
          println(s"${word}不包含")
          bitSet.set(word.hashCode,true)
        }
    })

  }
}
