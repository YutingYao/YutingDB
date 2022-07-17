## 步骤

![](https://i.bmp.ovh/imgs/2022/07/17/9bead9060f16faf9.png)

### 01 - 启动 kafka

浏览器中输入地址进行查看(http://ubuntu01:8080/)

首先启动 Hadoop（启动Hadoop的方法可以参考上面给出的Hadoop安装教程）。

在Masternode主机上运行如下命令：

```sh
start-all.sh
```

启动Spark集群：

```sh
# 在Masternode主机上运行如下命令：

start-master.sh
start-workers.sh
```



```sh
./bin/kafka-­server-­start.sh
```

### 02 - 温湿度数据读入 kafka

```sh
python DHT2producer.py
```

读入数据的格式为

```py
message = {
 'time': time.time(), 
 'device': socket.gethostname(), 
 'temperature': temperature, 
 'humidity': humidity, 
 'experiment': EXPERIMENT
 }

简化版：

message = {
 'temperature': temperature,
 'humidity':humidity}
```

关键语法为：

```py
producer.send(topic, message)
producer.flush()
```

### 03 - 温湿度数据传输到 spark streaming

```sh
python consumer2spark.py
```

关键语法为：

```py
import json
from pyspark import SparkConf, SparkContext
from pyspark.streaming import StreamingContext
from pyspark.streaming.kafka import KafkaUtils

self.sc   = SparkContext(conf=self.conf)
self.ssc = StreamingContext(self.sc, batchDuration=interval)

messages = KafkaUtils.createStream(self.ssc, self.zookeeper, "spark-streaming-consumer", {self.topic: 1})
lines = messages.map(lambda x: x[1])

rows = lines.map(lambda x: { 
 "temperature": json.loads(x)['temperature'],
 "humidity": json.loads(x)['humidity']
})

def check_and_write(self, x):
    try:
        x.toDF().write.format("org.apache.spark.sql.cassandra").options(table="test1", keyspace = "mykeyspace").save(mode ="append") 
    except ValueError:
        print "No rdd found!"

rows.foreachRDD(lambda x: {
 self.check_and_write(x)
})

self.ssc.start()
self.ssc.awaitTermination()
```

### 04 - 接下来，运行spark rdd machine，进行预测

参考项目：

- <https://github.com/sarath96kumarh/Real-time-Weather-Prediction>

```sh
./spark­1.6.1 ­bin ­hadoop2.6/bin/spark ­submit ­­packages org.apache.spark:spark­streaming ­kafka_2.10:1.6.1 spark_consumer.py
```

将2天的温度、湿度data分别在两台executor机器上计算

```py
from pyspark.ml import Pipeline
df.show(2)
df.printSchema()
train, test = df.randomSplit([0.93,0.07])

```

构建 pipeline：

case1:

```py
stage_1 = RegexTokenizer(inputCol= 'tweet' , outputCol= 'tokens', pattern= '\\W')
stage_2 = StopWordsRemover(inputCol= 'tokens', outputCol= 'filtered_words')
stage_3 = Word2Vec(inputCol= 'filtered_words', outputCol= 'vector', vectorSize= 100)
model = LogisticRegression(featuresCol= 'vector', labelCol= 'label') 

pipeline = Pipeline(stages= [stage_1, stage_2, stage_3, model])
```

case2:

```py
indexers = []
target_indexer = StringIndexer().fit(df_new)
assembler = VectorAssembler()
rf = RandomForestClassifier()
pipeline = Pipeline(stages=indexers+[target_indexer,assembler,rf])
model = pipeline.fit(train)
model.save("")
```

```py
# Make predictions on test documents and print columns of interest.
prediction = model.transform(test)
selected = prediction.select("id", "text", "probability", "prediction")
for row in selected.collect():
rid, text, prob, prediction = row
print("(%d, %s) --> prob=%s, prediction=%f" % (rid, text, str(prob),
                                    prediction))
```

```py
    sc = SparkContext(appName="PythonLinearRegressionWithSGDExample")

    # $example on$
    # Load and parse the data
    def parsePoint(line):
        values = [float(x) for x in line.replace(',', ' ').split(' ')]
        return LabeledPoint(values[0], values[1:])

    data = sc.textFile("data/mllib/ridge-data/lpsa.data")
    parsedData = data.map(parsePoint)

    # Build the model
    model = LinearRegressionWithSGD.train(parsedData, iterations=100, step=0.00000001)

    # Evaluate the model on training data
    valuesAndPreds = parsedData.map(lambda p: (p.label, model.predict(p.features)))
    MSE = valuesAndPreds \
        .map(lambda vp: (vp[0] - vp[1])**2) \
        .reduce(lambda x, y: x + y) / valuesAndPreds.count()
    print("Mean Squared Error = " + str(MSE))

    # Save and load model
    model.save(sc, "target/tmp/pythonLinearRegressionWithSGDModel")
    sameModel = LinearRegressionModel.load(sc, "target/tmp/pythonLinearRegressionWithSGDModel")
```

```py
def predict(df: DataFrame) -> DataFrame:
    # necessario per i dati che arrivano da kafka
    df = df.selectExpr("CAST(value AS STRING)") \
           .select(from_json("value", schema=schema).alias("data")) \
           .select("data.*")
    df = pipelineFit.transform(df)  # passo spark df perchè transform vuole un spark df
    # aggiunge la colonna timestamp e sistema la colonna probability. Poi seleziona solo le colonne utili
    df = df.withColumn("@timestamp", current_timestamp()) \
            .withColumn("probability", vector_to_array("probability")) \
            .select("@timestamp", "title", "polarity", "prediction", "probability")
    return df


dataStream = spark\
            .readStream \
            .format('kafka') \
            .option('kafka.bootstrap.servers', 'kafkaserver:9092') \
            .option('subscribe', 'news_topic') \
            .option('startingOffsets', 'earliest')\
            .load()

dataStream = predict(dataStream)

dataStream.writeStream\
          .option("checkpointLocation", "./checkpoints") \
          .format("es") \
          .start(elastic_index + "/_doc")\
          .awaitTermination()
```

### 05 - 将预测结果输出到 kafka

```py
def kafkaSink(matched_stats, sink_driver):
    # producer = KafkaProducer(bootstrap_servers="localhost:9092")
    producer = KafkaProducer(bootstrap_servers = "10.0.0.4:9092, 10.0.0.5:9092, 10.0.0.10:9092")
    # print(matched_stats)
    producer.send('hash_matched_stats', matched_stats.encode('utf8'))

    for i in range (0, len(sink_driver)):
        producer.send("driver_topic",sink_driver[i].encode('utf8'))
    producer.flush()
```

```py
message = {
 'temperature': temperature,
 'humidity':humidity}
```

### 06 - 将 kafka 结果输出到 thingsboard

```py
camera_access_token = "<insert the access token here for thingsboard device camera>"
counter_access_token = "<insert the access token here for thingsboard device counter>"
parking_access_token = "<insert the access token here for thingsboard device counter>"

consumer = KafkaConsumer(auto_offset_reset='latest',
                        bootstrap_servers=[init_object.kafka_host], 
                        api_version=(0, 10), 
                        consumer_timeout_ms=1000)
consumer.subscribe(pattern='')    # Subscribe to a pattern
while True:
      for message in consumer:
      #print (message.topic)
      if message.topic == "predicData":
            string_val = str(message.value)
          
            string_val = string_val.strip("b'").strip("\n")
            row = string_val.split(";")

            data = float(row[2])
            data_json = {} # To sent to Thingsboard
            access_token = ""
            sensor_id = row[0]
            if sensor_id == "S1":
                data_json["camera_count"] = data
                access_token = camera_access_token
            elif sensor_id == "S11":
                data_json["counter"] = data
                access_token = counter_access_token
            elif sensor_id == "S12":
                data_json["parking_space"] = data
                access_token =  parking_access_token

            thingsboard_host = init_object.things_host
            post_url = thingsboard_host.replace("$ACCESS_TOKEN", access_token)
            response = requests.post(post_url, json.dumps(data_json), headers={'Content-type': 'application/json'})
```

参考项目：

- <https://github.com/karthikv1392/IoT_Kafka_Demo/tree/71ab51564ebf01112def2a63b406a9fe8eccd31a>
- <https://github.com/tanyongnan1992/Kafka-Python/tree/9f0edb965232fe1acef30b349b617763621204b5>
- <https://github.com/argosp/pyargos/tree/fac8c968325b47f1e4bf5f9f3dc4df602fad2ba4>
