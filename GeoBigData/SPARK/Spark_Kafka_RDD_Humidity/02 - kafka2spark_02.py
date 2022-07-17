import requests, datetime, time, json, os
from pyspark import SparkContext
from pyspark.streaming import StreamingContext
from pyspark.sql import SQLContext, SparkSession
from kafka import KafkaConsumer
# Config
KAFKA_HOST, KAFKA_PORT = 'localhost', '9092'

#os.environ['PYSPARK_SUBMIT_ARGS'] = '--packages org.apache.spark:spark-sql-kafka-0-10_2.11:3.2.1'

sc = SparkContext()
ssc = StreamingContext(sc, 10)
sql_context = SQLContext(sc)

print("Start -----------------")

if __name__ == "__main__":
    '''
    consumer = KafkaConsumer(
        'messages1',
        bootstrap_servers=['localhost:9092'],
        auto_offset_reset='latest' # 'earliest'
    )
    for message in consumer:
        print(message)
        #print(str(json.loads(message)))'''

    spark = SparkSession \
        .builder \
        .appName("streaming-test") \
        .master("local") \
        .getOrCreate()

    #spark.sparkContext.setLogLevel("ERROR")

    df = spark \
        .readStream \
        .format("kafka") \
        .option("kafka.bootstrap.servers", f"{KAFKA_HOST}:{KAFKA_PORT}") \
        .option("subscribe", "messages1") \
        .option("startingOffsets", "earliest") \
        .load()
    print(df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)"))

    df.show(5)