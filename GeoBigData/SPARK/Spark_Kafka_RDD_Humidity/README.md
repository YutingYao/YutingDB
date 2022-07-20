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

启动kafka：

```sh
./kafka-server-start.sh -daemon /opt/kafka/config/server.properties
```

创建topics

```sh
./kafka-topics.sh --create --bootstrap-server ubuntu01:9092, node01:9092, node02:9092 --replication-factor 3 --partitions 3 --topic DHT11
```

### 02 - 温湿度数据读入 kafka

把DHT的数据读入producer

```sh
python DHT2.py
```

读入数据的格式为

```py
message = {
 'time': time.time(), 
 'temperature': temperature, 
 'humidity': humidity, 
 }

```

关键语法为：

```py
producer.send(topic, message)
producer.flush()
```

### 03 - 温湿度数据传输到 spark streaming


```sh
./spark­1.6.1 ­bin ­hadoop2.6/bin/spark ­submit ­­packages org.apache.spark:spark­streaming ­kafka_2.10:1.6.1 spark_consumer.py
```

每隔半小时，对之前半小时的数据求平均值

保留60个最近的数据

关键语法为：

```py
import json
from pyspark import SparkConf, SparkContext
from pyspark.streaming import StreamingContext
from pyspark.streaming.kafka import KafkaUtils, TopicAndPartition

self.sc   = SparkContext(conf=self.conf)
# creat the Streaming Context from the above spark context with window size n seconds
interval = 60*30
self.ssc = StreamingContext(self.sc, batchDuration=interval)

topics = ["DHT11"]
offsets = {TopicAndPartition(topic, 0): 0 for topic in topics}

kafkaParams = {"metadata.broker.list": "ubuntu01:9092,node01:9092,node02:9092"}

messages = KafkaUtils.createDirectStream(self.ssc, topics, kafkaParams, offsets)

lines = messages.map(lambda x: x[1])

rows = lines.map(lambda x: { 
 'time': json.loads(x)['time'], 
 "temperature": json.loads(x)['temperature'],
 "humidity": json.loads(x)['humidity']
})

model = tf.keras.models.load_model("trained_lstm_mod.h5")
xs = getpre_data(sonsers, prev_day, target_min=30, seq_len=30, feats=['Close', 'Volume'])




rows.foreachRDD(lambda x: {
 self.predictTemperature(x)
})

self.ssc.start()
self.ssc.awaitTermination()
```

```py
# this method gets the previous day data of the stocks, as we need this to construct the data sequence for the LSTM model prediction
def getpre_data(tickers, start_date, target_min=5, seq_len=60, feats=['Close', 'Volume']):
    # gets the last 60min of the previous day. Additional minutes taken as we are taking several minutes ahead
    last_60min = data_all.iloc[-67:-1,:]
    # initialize xs as empty dictionary
    xs = {}
    for t in tickers:
        if len(tickers) > 1:
            data_sub = last_60min[t]
        else:
            data_sub = last_60min
        # generate the past day's data as a data sequence in the same dimensions required by the trained LSTM model
        x_seq = generate_sequences(data_sub, target_min=target_min, seq_len=seq_len, feats=feats)
        x_seq = x_seq.tolist()
        xs[t] = x_seq
    return xs

# this method generates the data sequence in the dimensions needed by the LSTM model
def generate_sequences(data, target_min=30, seq_len=30, feats=['Close', 'Volume']):
    if ((len(data)-seq_len) - target_min ) > 0:
        for i, v in enumerate(range(target_min, len(data)-seq_len)):
            if i == 0:
                x = np.expand_dims(data[feats].values[i:i+seq_len, :], axis=0)
            else:
                z = np.expand_dims(data[feats].values[i:i+seq_len, :], axis=0)
                x = np.concatenate((x, z), axis=0)
    else:
        x = np.ones((1, seq_len, len(feats)))
    
    #print(x.shape)
    return x

def predictTemperature(self, rdd):
    rddDic = rdd.collectAsMap()
    for key, values in rddDic.items():
        for main_key, main_values in past_data_seq.items():
            if key == main_key:
                # 将最近一分钟的更新值从字符串转换为 dict 类型
                dic = eval(rddDic[key])
                # 重新打包为列表项以附加到股票代码的 past_data_seq
                newVal = [dic['Close'], dic['Volume']]
                # 删除代码的 past_data_seq 中最旧的元素
                pastSeq = past_data_seq[main_key][0][1:]
                # 将来自 spark rdd 流的最新分钟更新附加到股票代码的 past_data_seq
                pastSeq.append(newVal)
                past_data_seq[main_key][0] = pastSeq
                # change the type of the single ticker sequence into a numpy array
                pastSeq = (np.array(pastSeq) - x_min)/(x_max - x_min)
                pastSeq = np.reshape(pastSeq, (1,60,2))
                pred = (model.predict(pastSeq))*(y_max - y_min) + y_min
                print('The predicted '+  key + ' is '+ str(pred[0][0]))
        self.kafkaSink({key : pred})


def kafkaSink(self, message):
    producer = KafkaProducer(bootstrap_servers = "ubuntu01:9092,node01:9092,node02:9092")

    for i in range (0, len(sink_driver)):
        producer.send("predicData",message[i].encode('utf8'))
    producer.flush()
```



### 06 - 将 kafka 结果输出到 thingsboard

```py
access_token = "DHT_DEMO_TOKEN"

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
            key = row[0]
            if key == "temperature":
                data_json["temperature"] = data
                access_token = access_token
            elif key == "humidity":
                data_json["humidity"] = data
                access_token = access_token

            thingsboard_host = init_object.things_host
            post_url = thingsboard_host.replace("$ACCESS_TOKEN", access_token)
            response = requests.post(post_url, json.dumps(data_json), headers={'Content-type': 'application/json'})
```

参考项目：

- <https://github.com/karthikv1392/IoT_Kafka_Demo/tree/71ab51564ebf01112def2a63b406a9fe8eccd31a>
- <https://github.com/tanyongnan1992/Kafka-Python/tree/9f0edb965232fe1acef30b349b617763621204b5>
- <https://github.com/argosp/pyargos/tree/fac8c968325b47f1e4bf5f9f3dc4df602fad2ba4>
