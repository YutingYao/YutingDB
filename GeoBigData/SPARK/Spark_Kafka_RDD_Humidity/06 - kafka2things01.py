_Author_ = "Karthik Vaidhyanathan"



from kafka import KafkaConsumer, KafkaProducer
from Initializer import Initialize
from Custom_Logger import logger
import requests
import json


init_object = Initialize()


camera_access_token = "<insert the access token here for thingsboard device camera>"
counter_access_token = "<insert the access token here for thingsboard device counter>"
parking_access_token = "<insert the access token here for thingsboard device counter>"




class Data_Consumer():
    # Class that will perform the prediction in near-real time
    def process_sensor_data(self):
        # This will process the data from the sensor and then perform the management of the data
        print ("processing")
    def gather_data(self):
        global prev_vals
        global main_energy_forecast
        global main_traffic_forecast
        consumer = KafkaConsumer(auto_offset_reset='latest',
                                  bootstrap_servers=[init_object.kafka_host], api_version=(0, 10), consumer_timeout_ms=1000)

        consumer.subscribe(pattern='^sensor.*')    # Subscribe to a pattern
        main_list = []
        main_list_traffic = []
        while True:
            for message in consumer:
                #print (message.topic)
                if message.topic == "sensorData":
                    string_val = str(message.value)
                    string_val = string_val.strip("b'").strip("\n")
                    row = string_val.split(";")
                    sensor_id = row[0]
                    data = float(row[2])
                    data_json = {} # To sent to Thingsboard
                    access_token = ""
                    if sensor_id == "S1":
                        # This is a camera
                        data_json["camera_count"] = data
                        access_token = camera_access_token
                    elif sensor_id == "S11":
                        # This is the Counter
                        data_json["counter"] = data
                        access_token = counter_access_token
                    elif sensor_id == "S12":
                        # This is the parking mat
                        data_json["parking_space"] = data
                        print ("Parrking ")
                        access_token =  parking_access_token

                    # If you are not using thingsboard you can comment out the below lines till response =
                    thingsboard_host = init_object.things_host
                    post_url = thingsboard_host.replace("$ACCESS_TOKEN", access_token)
                    print(post_url)
                    headers = {'Content-type': 'application/json'}
                    response = requests.post(post_url, json.dumps(data_json), headers=headers)



if __name__ == '__main__':
    data_consumer =  Data_Consumer()
    data_consumer.gather_data()