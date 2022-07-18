Structured Streaming脚本建立

#!/usr/bin/env python3

import re
from functools import partial
from pyspark.sql.functions import *
from pyspark.sql import SparkSession

if __name__ == "__main__":

    spark = SparkSession \
        .builder \
        .appName("StructuredKafkaWordCount") \
        .getOrCreate()

    spark.sparkContext.setLogLevel('WARN') #只提示警示信息

    lines = spark \    #使用spark streaming则是基于KakfkaUtils包使用createDirectStream
        .readStream \
        .format("kafka") \
        .option("kafka.bootstrap.servers", "localhost:9092") \
        .option("subscribe", 'cdczztar') \   #要消费的topic
        .load().selectExpr("CAST(value AS STRING)")

    #lines.printSchema()
    #正则处理，根据实际数据处理，kafka获取后是oracle日志，在这只提取表插入的值
    pattern = 'data":(.+)}'
    fields = partial(regexp_extract, str="value", pattern=pattern)

    words = lines.select(fields(idx=1).alias("values"))

#输出模式：存入文件
    query = words \
        .writeStream \
        .outputMode("append") \
        .format("csv") \
        .option("path","file:///tmp/filesink") \   #存到服务器地址
                .option("checkpointLocation","file:///tmp/file-sink-cp") \
        .trigger(processingTime="10 seconds") \
        .start()    

    query.awaitTermination()    

#新开一个服务器窗口运行，这边已经在代码目录下
/usr/local/spark/bin/spark-submit --packages org.apache.spark:spark-sql-kafka-0-10_2.11:2.4.0  spark.py