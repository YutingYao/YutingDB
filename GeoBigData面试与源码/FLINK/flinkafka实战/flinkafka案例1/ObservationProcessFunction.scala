package function

import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.java.typeutils.TypeExtractor
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala.OutputTag
import org.apache.flink.util.Collector

class ObservationProcessFunction extends ProcessFunction[String,String]{

  override def processElement(event: String, context: ProcessFunction[String,String]#Context,
                              collector: Collector[String]): Unit = {
    if (event.contains("LOG")) {
      context.output(ObservationProcessFunctionConfig.eventsLogTag, event)
   }
    context.output(ObservationProcessFunctionConfig.eventsOutputTag, event)
  }
}

object ObservationProcessFunctionConfig {
  implicit val eventTypeInfo: TypeInformation[String] = TypeExtractor.getForClass(classOf[String])

  val eventsOutputTag: OutputTag[String] = OutputTag[String]("obs-output")
  val eventsLogTag: OutputTag[String] = OutputTag[String]("obs-log")

}
