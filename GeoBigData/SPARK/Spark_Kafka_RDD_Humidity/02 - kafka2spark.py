from pyspark import SparkContext
import time
import os
from datetime import datetime

class TempData:
	def parse(self, line):
		fields = line.split(',')
		self.humidity = fields[0]
		self.windspeed = fields[1]
		self.timestamp = fields[2]
		self.temperature= fields[3]
		return self
	def __repr__(self):
		return 'humidity = %s, windspeed = %s, timestamp = %d, temperature = %s' % (self.humidity, self.windspeed, self.timestamp, self.temperature)
	def to_json(self):
		return '{ "humidity":"%s", "windspeed":"%s", "timestamp":%d, "temperature":"%s"}'%(self.humidity, self.windspeed, self.timestamp, self.temperature)



sc = SparkContext(appName="spark_temperature_processor")
avgJson = open("avgJson.txt", "a+", 0)
#seed with data
avgJson.write("[{ \"humidity\":\"3.08680013442\", \"windspeed\":\"1\", \"timestamp\":\"2016-05-12 20:24:57.945850\", \"temperature\":\"44\" } ]")
#avgJson.write("[{ \"timestamp\" : \"2016-05-12 22:41:16.575296\", \"avg_temperature\":\"3.08680013442\", \"avg_windspeed\":\"1\", \"avg_humidity\":\"44\" }]")
while 1==1:
	stationData = sc.textFile("output.txt")
	data = stationData.map(lambda x: x.split('\n')) # [u'3.88901292697,3,2016-05-12 19:28:33.875937,36']
	split_data = data.map(lambda x: x[0].split(',')) #[u'3.88901292697', u'3', u'2016-05-12 19:28:33.875937', u'36']
	joint_data_class = split_data.map(lambda x: str(x[0]) + "," + str(x[1]) + "," + str(x[2]) + ","  + str(x[3]))
	print "data ="
	print data.first()
	print "all temps:"
	
	#Map operations - transform
	all_humidity = data.map(lambda x: x[0].split(',')).map(lambda x: float(x[0]))
	all_windspeed = data.map(lambda x: x[0].split(',')).map(lambda x: float(x[1]))
	all_temp = data.map(lambda x: x[0].split(',')).map(lambda x: float(x[3]))
	#reduce operations (sum ) - actions
	sum_temp = float(all_temp.sum())
	sum_humidity = float(all_humidity.sum())
	sum_windspeed = float(all_windspeed.sum())
	print "sum of temperatures = ", sum_temp 
	number_of_entries = data.count()
	print "count = " + str(number_of_entries)
	avg_temp = sum_temp / number_of_entries
	avg_humidity = sum_humidity / number_of_entries
	avg_windspeed = sum_windspeed / number_of_entries
	print "avg temperature = " + str(avg_temp) 
	print "avg windspeed = " + str(avg_windspeed) 
	print "avg humidity = " + str(avg_humidity) 
	timestamp = datetime.now()
	jsonline = "{ \"timestamp\" : \"" + str(timestamp) + "\" ,"  + " \"avg_temperature\" :\"" + str(avg_temp) + "\" ," + " \"avg_windspeed\" :\"" + str(avg_windspeed) + "\" ," + " \"avg_humidity\" :\"" + str(avg_windspeed) + "\"" + " }"
	avgJson.seek(-1, os.SEEK_END) #remove the last ]
	avgJson.truncate()
	avgJson.write(",\n")
	avgJson.write(jsonline)
	avgJson.write("]")
	time.sleep(5)
