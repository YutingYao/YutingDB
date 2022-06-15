package com.dg.boilerplate.client
import java.io._
import java.util.UUID

object UUIDMessageGenerator {
  def main(args: Array[String]): Unit = {
    val fileObject = new File("data.txt" )     // Creating a file
    val printWriter = new PrintWriter(fileObject)       // Passing reference of file to the printwriter
    for( a <- 1 to 10000000 ){ // 10 million
//    for( a <- 1 to 100000 ){
      val uuid=UUID.randomUUID().toString()
      printWriter.write(uuid+",")
      println("writing "+uuid)
      println("key "+uuid.substring(0,1))
    }
    printWriter.close()             // Closing printwriter
  }
}
