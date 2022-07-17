## 步骤

### 01 - 启动 kafka

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

## 03 - 温湿度数据传输到 spark streaming

```sh
python consumer2spark.py
```

关键语法为：

```py
self.sc   = SparkContext(conf=self.conf)
self.ssc = StreamingContext(self.sc, batchDuration=interval)
```

04 - 接下来，运行spark rdd machine，进行预测

参考项目：

- <https://github.com/sarath96kumarh/Real-time-Weather-Prediction>

```sh
./spark­1.6.1 ­bin ­hadoop2.6/bin/spark ­submit ­­packages org.apache.spark:spark­streaming ­kafka_2.10:1.6.1 spark_consumer.py
```

05 - 将预测结果输出到 kafka

06 - 将 kafka 结果输出到 thingsboard

参考项目：

- <https://github.com/karthikv1392/IoT_Kafka_Demo/tree/71ab51564ebf01112def2a63b406a9fe8eccd31a>
- <https://github.com/tanyongnan1992/Kafka-Python/tree/9f0edb965232fe1acef30b349b617763621204b5>
- <https://github.com/argosp/pyargos/tree/fac8c968325b47f1e4bf5f9f3dc4df602fad2ba4>
