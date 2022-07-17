from kafka import KafkaProducer
import os
import time
import json
import socket
import Adafruit_DHT
import numpy
import time

KAFKA = os.getenv('KAFKA', '192.168.2.250:9092')
DELTA = float(os.getenv('DELTA', 1))
EXPERIMENT = float(os.getenv('EXP', 1))
LOGDIR = str(os.getenv('LOGDIR', '/tmp'))

gpio = 23
sensor = Adafruit_DHT.DHT11

def getTempAndHumidity():
    return Adafruit_DHT.read_retry(sensor, gpio)

def savetext(message):
    str_filename = LOGDIR+'/'+str(socket.gethostname())+'_'+str(EXPERIMENT)+'.csv'
    with open(str_filename,'ab') as f:
        numpy.savetxt(f, [str(message)], fmt='%s')

def send():
    try:
       humidity, temperature = getTempAndHumidity()
       message = {'time': time.time(), 'device': socket.gethostname(), 'temperature': temperature, 'humidity': humidity, 'experiment': EXPERIMENT}
       savetext(message)
       print 'sending', message
       producer = KafkaProducer(bootstrap_servers=KAFKA, value_serializer=lambda v: json.dumps(v).encode('utf-8'))
       producer.send('sensor-reading', message)
       producer.flush()
    except Exception as e:
       print 'where r u kafka?', e

try:
    while True:
        send()
        time.sleep(DELTA)
except KeyboardInterrupt:
    pass