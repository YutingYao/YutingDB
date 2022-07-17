import paho.mqtt.client as mqtt
from argos import tbHome
from kafka import KafkaConsumer
import argparse
import json
import logging
from argos.old.kafka import toThingsboardDeserializer


parser = argparse.ArgumentParser()
parser.add_argument("--topic", dest="topic" , help="The kafka topic to consume from", required=True)
parser.add_argument("--expConf", dest="expConf", help="The path to the experiment configuration json file")
parser.add_argument("--kafkaHost", dest="kafkaHost", default="localhost", help="The kafka host in the following format - ip(:port)")
parser.add_argument("--tbHost", dest="tbHost", default="localhost", help="The thingsboard host ip")
args = parser.parse_args()


def on_disconnect(client, userdata, rc=0):
    logging.debug("DisConnected result code " + str(rc))
    client.loop_stop()


def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print("Connected to broker")
    else:
        print("Connection failed")

consumer = KafkaConsumer(args.topic,
                         bootstrap_servers=[args.kafkaHost],
                         auto_offset_reset='latest',
                         enable_auto_commit=True,
                         group_id='my-group'
                         #value_deserializer=thingsboardDeserializer
                         )

with open('/home/eden/Projects.local/2019/DesertWalls/experimentConfiguration.json') as credentialOpen:  # with open(args.expConf)
    credentialMap = json.load(credentialOpen)
tbh = tbHome(credentialMap["connection"])

deviceName = args.topic

client = mqtt.Client("Me_%s" % deviceName)
accessToken = tbh.deviceHome.createProxy(deviceName).getCredentials()
client.username_pw_set(accessToken, password=None)
client.on_disconnect = on_disconnect
client.connect(args.tbHost)
client.loop_start()

for message in consumer:
    # print('----------',pandas.Timestamp.fromtimestamp(message.timestamp/1000.0),'----------')
    try:
        client.publish('v1/devices/me/telemetry', toThingsboardDeserializer(message.value))
    except json.JSONDecodeError:
        print('exception')
        continue