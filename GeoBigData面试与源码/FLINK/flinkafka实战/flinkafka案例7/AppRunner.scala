import com.typesafe.config.ConfigFactory
import configurations.FlinkSampleConfiguration
import flinkjobs.FlinkKafkaProcessor
import util.FlinkKafkaConnector

object AppRunner {

  def main(args: Array[String]): Unit ={
    val config = ConfigFactory.load("application.conf").getConfig("com.ram.batch")
    //val jsonConfig = config.getConfig("json")
    /*val jsonconfig = jsonConfig.getString("test")
    print(jsonconfig)*/


    val flinkSampleConfiguration = new FlinkSampleConfiguration(config)
    val kafkaConnector = new FlinkKafkaConnector(flinkSampleConfiguration)
    val  flinkConsume = new FlinkKafkaProcessor(flinkSampleConfiguration,kafkaConnector )

    flinkConsume.process()
  }

}
