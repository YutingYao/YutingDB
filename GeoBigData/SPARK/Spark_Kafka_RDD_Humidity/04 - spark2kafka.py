from __future__ import print_function
from pyspark import SparkContext
from pyspark.streaming import StreamingContext
from kafka import KafkaProducer
from pyspark.streaming.kafka import KafkaUtils
import sys
from datetime import datetime
from math import radians, cos, sin, asin, sqrt
import pygeohash as gh


# Function to generate geo location hash based on longitude and lattitude. Precision is 5 decimal points
def geohash(x):

    # Precision of Geo Hash
    # #   km
    # 1   ± 2500
    # 2   ± 630
    # 3   ± 78
    # 4   ± 20
    # 5   ± 2.4
    # 6   ± 0.61
    # 7   ± 0.076
    # 8   ± 0.019
    # 9   ± 0.0024
    # 10  ± 0.00060
    # 11  ± 0.000074

    array = x.strip("()").split(",")
    lon = array[0]
    lat = array[1]
    return gh.encode(float(lon), float(lat), 5)


# function to Sink the computation statestics to Kafka
def kafkaSink(matched_stats, sink_driver):
    # producer = KafkaProducer(bootstrap_servers="localhost:9092")
    producer = KafkaProducer(bootstrap_servers = "10.0.0.4:9092, 10.0.0.5:9092, 10.0.0.10:9092")
    # print(matched_stats)
    producer.send('hash_matched_stats', matched_stats.encode('utf8'))

    for i in range (0, len(sink_driver)):
        producer.send("driver_topic",sink_driver[i].encode('utf8'))
    producer.flush()

# Function to compute Herversine Distance for given two location points


def haversine(lon1, lat1, lon2, lat2):
    """
    Calculate the great circle distance between two points
    on the earth (specified in decimal degrees)
    """
    lon1 = float(lon1)
    lat1 = float(lat1)
    lon2 = float(lon2)
    lat2 = float(lat2)
    # convert decimal degrees to radians
    lon1, lat1, lon2, lat2 = map(radians, [lon1, lat1, lon2, lat2])

    # haversine formula
    dlon = lon2 - lon1
    dlat = lat2 - lat1
    a = sin(dlat/2)**2 + cos(lat1) * cos(lat2) * sin(dlon/2)**2
    c = 2 * asin(sqrt(a))

    # Radius of earth in kilometers. Use 3956 for miles
    r = 6371
    return c * r

# function to format the message into semicolan seperated stacked values into a single variable
# Helper function to sink the matched stats to kafka
def format_match_stats(total_ride_req, matched_riders, total_drivers, timestamp):
    str_fmt = "{};{};{};{}"
    message = str_fmt.format(total_ride_req, matched_riders,
                             total_drivers, timestamp)
    return message


# function to format the message into semicolan seperated stacked values into a single variable
# Helper function to sink the updated driver locations to kafka
def format_message_driver(driver_id, driver_loc, timestamp):
    str_fmt = "{};{};{}"
    message = str_fmt.format(driver_id, driver_loc, timestamp)
    # print(message)
    return message


# Process the RDD and compute Greedy bipartite match
def process_union(rdd):

    # Empty data structures to keep track of Rider and Driver ids who are matched. Avoid duplicates
    matched_list = dict()
    matched_driver = dict()

    total_ride_req = dict()
    total_driver = dict()

    sink_driver = []

    # for each record in RDD
    for line in rdd:

        # Since RDD record is a tuple, seperating it into two values rider and driver
        rider, driver = line[1]

        # When doing left outer join, if the hash values doen't match, the driver's tuple will contain none.
        # I define them as outliers. The if condition will ignore the outliers.
        if(driver is None or rider is None):
            continue

        # Seperating out all the values from tuple into single variables.
        ride_pickup = rider[1].strip("()").split(",")
        driver_loc = driver[1].strip("()").split(",")
        ride_long = ride_pickup[0]
        ride_lat = ride_pickup[1]
        driver_long = driver_loc[0]
        driver_lat = driver_loc[1]

        # keeping the dictionary of Total riders
        if rider[0] not in total_ride_req:
            total_ride_req[rider[0]] = "new"

        # keeping the dictionary of total drivers
        if driver[0] not in total_driver:
            total_driver[driver[0]] = "new"

        # if the driver id and rider id is not in the matched dictionary
        if rider[0] not in matched_list and driver[0] not in matched_driver:
            # print("not yet matched")

            # then compute the distance between
            distance = haversine(ride_long, ride_lat, driver_long, driver_lat)
            if distance < 3:

                # keeping Track of matched riders and drivers in a dictionary for 0(1) lookup
                matched_list[rider[0]] = driver[0]
                matched_driver[driver[0]] = "new"
                kafka_driver_sink = format_message_driver(
                    driver[0], rider[2], str(datetime.now()))
                sink_driver.append(kafka_driver_sink)

    # Formatting the matched statestics {}
    matched_stats = format_match_stats(len(total_ride_req), len(
        matched_list), len(total_driver), str(datetime.now()))

    # sending it to kafka Sink
    kafkaSink(matched_stats, sink_driver)


def main():

    # initializing the Spark Context
    sparkContext = SparkContext(appName='Bipartite-Matching')
    sparkContext.setLogLevel('ERROR')
    sparkStreamingContext = StreamingContext(sparkContext, 1)

    # create DStream that reads the Driver Stream
    spark_kafka_driver_Stream = KafkaUtils.createDirectStream(sparkStreamingContext, ['driver_topic'],
                                                                {'metadata.broker.list': '10.0.0.4:9092, 10.0.0.5:9092, 10.0.0.10:9092'})
                                                            #   {'metadata.broker.list': 'localhost:9092'})

    # creating DStream that reads the Rider stream
    spark_kafka_rider_stream = KafkaUtils.createDirectStream(sparkStreamingContext, ['rider_topic'],
                                                               {'metadata.broker.list': '10.0.0.4:9092, 10.0.0.5:9092, 10.0.0.10:9092'})
                                                            #  {'metadata.broker.list': 'localhost:9092'})

    # defining the Window size
    driver_window = spark_kafka_driver_Stream.window(3)
    rider_window = spark_kafka_rider_stream.window(3)

    # creating location hash - precision 0.5 on driver RDD.
    driver_window = driver_window.map(lambda x: x[1])\
        .map(lambda x: x.split(";"))\
        .map(lambda x: (geohash(x[1]), x))
    # print(driver_window.count())

    # optional debugging: checking the counts of driver RDD
    # driver_count = driver_window.foreachRDD(
    #     lambda x: print('driver:', x.count()))

    # creating location hash - precision 0.5 on rider RDD.
    rider_window = rider_window.map(lambda x: x[1])\
        .map(lambda x: x.split(";"))\
        .map(lambda x: (geohash(x[1]), x))

    # optional debugging: checking the counts of Rider RDD
    # rider_count = rider_window.foreachRDD(lambda x: print('rider:', x.count()))

    # Geerdy match - left outer join on Driver RDD and Rider RDD on key location hash.
    union_rdd = rider_window.leftOuterJoin(driver_window)

    # optional debugging: mantaining the count for Joined RDD
    # join_count = union_rdd.foreachRDD(lambda x: print('join:', x.count()))

    union_rdd = union_rdd.foreachRDD(
        lambda rdd: rdd.foreachPartition(process_union))

    # optional Debugging: running without partitioning RDDs.
    # union_rdd = union_rdd.foreachRDD(process_union)

    sparkStreamingContext.start()
    sparkStreamingContext.awaitTermination()


if __name__ == '__main__':
    main()