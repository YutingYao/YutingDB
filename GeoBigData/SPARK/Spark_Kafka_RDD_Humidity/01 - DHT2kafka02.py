import Adafruit_DHT
from kafka import KafkaProducer
from colorama import init, Fore, Back, Style
import os
import sys
import json

# ***** Initializes Colorama to format printing
init(autoreset=True)

# ********************************************
# ***** Variables initialization - START *****
# ********************************************
producer = None
caRootLocation='certs/CARoot.pem'
password='password'
# Set sensor type : Options are DHT11, DHT22 or AM2302
sensor=Adafruit_DHT.DHT11
# Set GPIO sensor is connected to
gpio=17
# Get environment variables
kafkaBrokers = os.getenv('KAFKA_BROKER')
SSL = os.getenv('SSL')
topic = os.getenv('TOPIC')
# ********************************************
# ****** Variables initialization - END ******
# ********************************************

# **********************************
# ***** Main program execution *****
# **********************************
if __name__ == '__main__':
    ###### Initialize Kafka producer
    if (kafkaBrokers == None) :
        print(Style.BRIGHT + 'No KAFKA_BROKER environment variable set, exiting ... ')
        sys.exit(1);
    if (SSL == None) | (SSL == "false"):
        print(Style.BRIGHT + 'Connecting to Kafka Broker without SSL')
        producer = KafkaProducer(bootstrap_servers=kafkaBrokers, value_serializer=lambda v: json.dumps(v).encode('utf-8'))
    else:
        print(Style.BRIGHT + 'Connecting to Kafka Broker with SSL')
        producer = KafkaProducer(bootstrap_servers=kafkaBrokers, value_serializer=lambda v: json.dumps(v).encode('utf-8'),
                                security_protocol='SSL',
                                ssl_check_hostname=False,
                                ssl_cafile=caRootLocation,
                                ssl_password=password)

    while True:
        try:
            # Use read_retry method. This will retry up to 15 times to
            # get a sensor reading (waiting 2 seconds between each retry).
            humidity, temperature = Adafruit_DHT.read_retry(sensor, gpio)

            if humidity is not None and temperature is not None:
                print('Temp={0:0.1f}*C  Humidity={1:0.1f}%'.format(temperature, humidity))
                ###### Write messages to Kafka topic
                print(Style.BRIGHT + 'Writing message to topic : ' + Fore.GREEN + topic)
                producer.send(topic, {'temperature': temperature,'humidity':humidity})
                producer.flush()
            else:
                print(Style.BRIGHT + Fore.RED + 'Failed to get reading. Try again!')

        except RuntimeError:
                print(Style.BRIGHT + Fore.RED + "RuntimeError, trying again...")
                continue