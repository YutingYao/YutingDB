_Author_ = "Karthik Vaidhyanathan"


# Producer module to keep sending the sensor data from cupCarbon to Streamer

import subprocess

import csv
import sys
import time
from kafka import KafkaConsumer, KafkaProducer
from Initializer import Initialize
from Custom_Logger import logger
from datetime import datetime,timedelta


init_object = Initialize()

class kafka_producer():
    def publish_message(self,producer_instance, topic_name, key, value):
        try:
            key_bytes = bytearray(key,'utf8')
            value_bytes = bytearray(value,'utf8')
            producer_instance.send(topic_name, key=key_bytes, value=value_bytes)
            producer_instance.flush()
            print('Message published successfully.')
        except Exception as ex:
            print('Exception in publishing message')
            print(str(ex))

    def connect_kafka_producer(self):
        _producer = None
        try:
            _producer = KafkaProducer(bootstrap_servers=[init_object.kafka_host], api_version=(0, 10))
            #_producer = KafkaProducer(bootstrap_servers=['192.168.1.41:9092'], api_version=(0, 10))
        except Exception as ex:
            print('Exception while connecting Kafka')
            print(str(ex))
        finally:
            return _producer


producer_object = kafka_producer()



class Data_Streamer():

    def __init__(self):
        self.start_time = datetime.now() - timedelta(days=20)
        self.sensor_list = ["S1","S11","S12"]


    def stream_data(self):
        # Stream data to Kafka
        producer_instance = producer_object.connect_kafka_producer()
        sensor_id_seconds = {}
        sensor_id_value = {}
        for id in self.sensor_list:
            sensor_id_seconds[id] = 0
            sensor_id_value[id] = 0
        seconds  = 0
        with open(init_object.data_path + init_object.data_file) as csv_file:
            csv_reader = csv.reader(csv_file, delimiter=';')
            count = 0
            total_seconds = 0
            while (True):
                #print (start_timestamp)
                for row in csv.reader(iter(csv_file.readline, '')):
                    if len(row) > 0:
                        line = row[0].strip("\n")
                        #print (line)
                        # print (line_data.split(";"))

                        if "Time" in line:
                            if ":" in line and " " in line:
                                time_list = line.split(":")[1].split(" ")
                                if len(time_list) > 1:
                                    if "." in time_list[1]:
                                        try:
                                           current_time = float(line.split(":")[1].split(" ")[1])
                                           #start_timestamp = self.start_time + timedelta(seconds=float(current_time))
                                        except:
                                            pass

                            # print (current_time)

                    if ("is writing the message") in line:
                        if any(sensor in line for sensor in self.sensor_list):
                        #res = list(filter(lambda x: x in line, self.sensor_list))
                            #print (start_timestamp)
                            #print (line)
                            sensor_id = line.split(" ")[0]
                            print (sensor_id)
                            if sensor_id in self.sensor_list:
                                value_text = line.split(" ")[6]
                                value = value_text.split("#")[-1].strip("\"")
                                sensor_id_seconds[sensor_id] += 0
                                sensor_id_value[sensor_id] = value
                                line_data = sensor_id + ";" +str(seconds) + ";" + str(value)
                                print (line_data)
                                producer_object.publish_message(producer_instance, "sensorData", "data", line_data)
                                logger.info("published message ", line_data)



if __name__ == '__main__':
    data_producer_object = Data_Streamer()
    data_producer_object.stream_data()