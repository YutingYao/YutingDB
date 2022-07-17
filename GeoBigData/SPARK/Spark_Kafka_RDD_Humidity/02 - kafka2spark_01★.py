import threading, sys, json
from kafka.client import KafkaClient
from kafka.consumer import SimpleConsumer
from pyspark.sql import SQLContext
from pyspark import SparkConf, SparkContext
from pyspark.streaming import StreamingContext
from pyspark.streaming.kafka import KafkaUtils

class Consumer:
	'Simple spark kafka streaming consumer'

	def __init__(self, casshost, interval, zookeeper, topic):
		self.conf = SparkConf().setAppName("KafkaSpark").set("spark.cassandra.connection.host", casshost)
		self.sqlContext = SQLContext(sparkContext=self.sc)
		self.sc   = SparkContext(conf=self.conf)
		self.ssc = StreamingContext(self.sc, batchDuration=interval)
		self.zookeeper = zookeeper
		self.topic = topic

	def check_and_write(self, x):
		try:
			x.toDF().write.format("org.apache.spark.sql.cassandra").options(table="test1", keyspace = "mykeyspace").save(mode ="append") 
		except ValueError:
			print "No rdd found!"

	def consume(self):
		messages = KafkaUtils.createStream(self.ssc, self.zookeeper, "spark-streaming-consumer", {self.topic: 1})
		lines = messages.map(lambda x: x[1])

		rows = lines.map(lambda x: { 
			"data": json.loads(x)['data'],
			"time": json.loads(x)['time']
		})

		rows.foreachRDD(lambda x: {
			self.check_and_write(x)
		})

		self.ssc.start()
		self.ssc.awaitTermination()

	def stop(self):
		if self.sqlContext != None:
			self.sqlContext.stop()
		if self.ssc != None:
			self.ssc.stop()
		if self.sc != None:
			self.sc.stop()

if __name__ == '__main__':
	topic = sys.argv[1]
	casshost = sys.argv[2]
	interval_batch = int(sys.argv[3])
	zookeeper = sys.argv[4]

	consumer = Consumer(casshost, interval_batch, zookeeper, topic)

	consumer.consume()
