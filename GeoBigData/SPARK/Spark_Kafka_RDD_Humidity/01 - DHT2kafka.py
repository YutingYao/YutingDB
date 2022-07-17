from kafka import *
import time
import random
from datetime import datetime, date

mykafka = KafkaClient("localhost:9092")

producer = SimpleProducer(mykafka)

filename = open ("cities.csv")

city_list = filename.readlines()

print len(city_list)

i = 1

while 1 == 1:

	#city_index = random.randint(0, 100)
	#city = city_list[city_index]
	#city_data = city.split(',')

	timestamp = datetime.now()

	windspeed = random.randint(0,5)
	humidity = random.uniform(0, 5)

	#current_state = city_data[1]
	#current_city = city_data[2] 
	
	timestamp = datetime.now()
	seed_temp = random.randint(-5, 5) #feed temperature data simulation
	current_temp = 40 + seed_temp


	data = str(humidity) + "," + str(windspeed) +  "," + str(timestamp) + "," + str(current_temp) 
	producer.send_messages("test", data)
	time.sleep(5)