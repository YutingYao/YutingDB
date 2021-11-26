package function

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import config.ObservationProcessConfig
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.util.Collector

class SimpleFunction extends ProcessFunction[String, String] {
    val mapper = new ObjectMapper()

    override def processElement(value: String, context: ProcessFunction[String, String]#Context, collector: Collector[String]):
    Unit = {

      mapper.registerModule(DefaultScalaModule)
      val batchEvent = mapper.readValue(value, classOf[Map[String, AnyRef]])
      val events = batchEvent.get("events").get.asInstanceOf[List[Map[String, AnyRef]]]
      events.foreach(event => {
        val eid = event.get("eid").get.asInstanceOf[String]
        if (eid.equalsIgnoreCase("LOG")) {
          context.output(ObservationProcessConfig.eventsLogTag, mapper.writeValueAsString(event))
        }
        context.output(ObservationProcessConfig.eventsOutputTag, mapper.writeValueAsString(event))
      })
    }
  }