[kafka_2.12-3.0.0.tgz](https://dlcdn.apache.org/kafka/3.0.0/kafka_2.12-3.0.0.tgz)

## <a name='kafka'></a>2.9. kafka

到[Kafka官网](https://kafka.apache.org/downloads)下载安装文件时，一定要选择和自己电脑上已经安装的scala版本号一致才可以，

本教程安装的Spark版本号是2.1.0，scala版本号是2.11，

所以，一定要选择Kafka版本号是2.11开头的。

比如，到Kafka官网中，可以下载安装文件Kafka_2.11-0.10.2.0.tgz，

前面的2.11就是支持的scala版本号，后面的0.10.2.0是Kafka自身的版本号。

### <a name='UbuntuKafka'></a>2.9.1. Ubuntu 系统安装Kafka

访问Kafka官方下载页面,下载稳定版本0.10.1.0的kafka.此安装包内已经附带zookeeper,不需要额外安装zookeeper.按顺序执行如下步骤:

```sh
cd ~/下载
sudo tar -zxf kafka_2.11-0.10.1.0.tgz -C /opt
cd /opt
sudo mv kafka_2.11-0.10.1.0/ ./kafka
sudo chown -R hadoop ./kafka
```

下面介绍Kafka相关概念,以便运行下面实例的同时，更好地理解Kafka.

1. Broker
Kafka集群包含一个或多个服务器，这种服务器被称为broker
2. Topic
每条发布到Kafka集群的消息都有一个类别，这个类别被称为Topic。（物理上不同Topic的消息分开存储，逻辑上一个Topic的消息虽然保存于一个或多个broker上但用户只需指定消息的Topic即可生产或消费数据而不必关心数据存于何处）
3. Partition
Partition是物理上的概念，每个Topic包含一个或多个Partition.
4. Producer
负责发布消息到Kafka broker
5. Consumer
消息消费者，向Kafka broker读取消息的客户端。
6. Consumer Group
每个Consumer属于一个特定的Consumer Group（可为每个Consumer指定group name，若不指定group name则属于默认的group）

### <a name='Kafka'></a>2.9.2. 安装成功了Kafka

进入kafka所在的目录

```sh
cd /opt/kafka
./bin/zookeeper-server-start.sh config/zookeeper.properties
```

注意，执行上面命令以后，终端窗口会返回一堆信息，然后就停住不动了，

没有回到shell命令提示符状态，这时，千万不要错误认为死机了，

而是Zookeeper服务器启动了，正在处于服务状态。

所以，千万不要关闭这个终端窗口，一旦关闭，zookeeper服务就停止了，

所以，不能关闭这个终端窗口。

请另外打开第二个终端，然后输入下面命令启动Kafka服务：

```sh
cd /opt/kafka
bin/kafka-server-start.sh config/server.properties
```

这样，Kafka就会在后台运行，即使你关闭了这个终端，Kafka也会一直在后台运行。

不过，这样做，有时候我们往往就忘记了还有Kafa在后台运行，所以，建议暂时不要用

下面先测试一下Kafka是否可以正常使用。

再另外打开第三个终端,输入如下命令:

```sh
cd /opt/kafka
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic dblab
```

topic是发布消息发布的category,

以单节点的配置创建了一个叫dblab的topic.

可以用list列出所有创建的topics,来查看刚才创建的主题是否存在。

或者：输入下面命令创建一个自定义名称为“wordsendertest”的topic

```sh
cd /opt/kafka
./bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic wordsendertest
```

这个topic叫wordsendertest，

**2181**是zookeeper默认的端口号，

partition是topic里面的分区数，

replication-factor是备份的数量，

在kafka集群中使用，这里单机版就不用备份了

可以用list列出所有创建的topics,来查看上面创建的topic是否存在：

```sh
./bin/kafka-topics.sh --list --zookeeper localhost:2181
```

这个名称为“wordsendertest”的topic，就是专门负责采集发送一些**单词**的。
下面，我们需要用**producer**来产生一些数据，请在当前终端内继续输入下面命令：

```sh
./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic dblab
```

```sh
./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic wordsendertest
```

上面命令执行后，你就可以在当前终端内用键盘输入一些英文单词，比如我们可以输入

```sh
hello hadoop
hello spark
```

这些单词就是数据源，这些单词会被Kafka捕捉到以后发送给消费者。

我们现在可以启动一个消费者，来查看刚才producer产生的数据。

然后再次开启新的终端或者直接按CTRL+C退出。然后使用consumer来接收数据,输入如下命令：

```sh
cd /opt/kafka
./bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic dblab --from-beginning  
```

请另外打开第四个终端，输入下面命令：

```sh
cd /opt/kafka
./bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic wordsendertest --from-beginning
```

可以看到，屏幕上会显示出如下结果，也就是刚才你在另外一个终端里面输入的内容：

hello hadoop

hello spark

到这里，与Kafka相关的准备工作就顺利结束了。

注意，现在可以把第四个终端关闭掉，

第一个终端（正在运行Zookeeper服务）、

第二个终端（正在运行Kafka服务）和

第三个终端不要关闭，继续留着后面使用。

如果记不住是哪个终端，那么所有这些终端窗口都不要关闭，要继续留着后面使用。

###  Spark准备工作（jar文件）

按照我们前面安装好的Spark版本，这些jar包都不在里面，

为了证明这一点，我们现在可以测试一下。请打开一个新的终端，然后启动pyspark：

```sh
cd /opt/spark
./bin/pyspark
```

根据Spark官网的说明，对于Spark2.1.0版本，

如果要使用Kafka，则需要下载spark-streaming-kafka-0-8_2.11相关jar包。

现在请在Linux系统中，打开一个火狐浏览器，

请点击这里访问[Maven Repository](https://mvnrepository.com/artifact/org.apache.spark/spark-streaming-kafka-0-8_2.11/2.1.0)，

里面有提供spark-streaming-kafka-0-8_2.11-2.1.0.jar文件的下载，

补充说明：这个spark-streaming-kafka-0-8_2.11-2.1.0.jar一定要和你之前的spark和scala版本对上，不然会报错

其中，2.11表示scala的版本，2.1.0表示Spark版本号。

下载后的文件会被默认保存在当前Linux登录用户的下载目录下，

本教程统一使用hadoop用户名登录Linux系统，

所以，文件下载后会被保存到“/home/hadoop/下载”目录下面。

现在，我们就把这个文件复制到Spark目录的jars目录下。请新打开一个终端，输入下面命令：

```sh
cd /opt/spark/jars
mkdir kafka
cd ~
cd 下载
cp ./spark-streaming-kafka-0-8_2.11-2.1.0.jar /opt/spark/jars/kafka
```

这样，我们就把spark-streaming-kafka-0-8_2.11-2.1.0.jar文件

拷贝到了“/opt/spark/jars/kafka”目录下。

同时，我们还要修改spark目录下conf/spark-env.sh文件,

```sh
cd spark/conf
vim spark-env.sh
```

修改该文件下面的SPARK_DIST_CLASSPATH变量

编辑spark-env.sh：（版本三）用于集群环境

原本的：

```sh
export SPARK_DIST_CLASSPATH=$(/opt/hadoop/bin/hadoop classpath)
```

新的：

```sh
export SPARK_DIST_CLASSPATH=$(/opt/hadoop/bin/hadoop classpath):$(/opt/hbase/bin/hbase classpath):/opt/spark/examples/jars/*:/opt/spark/jars/kafka/*:/opt/kafka/libs/*`
```

纠正：

这个环境变量里最后一个字符不要复制进去，还有这个环境变量里有hBase的变量配置，没安hbase的会出错，如按以上步骤搭建环境变量应为：

```sh
export SPARK_DIST_CLASSPATH=$(/opt/hadoop/bin/hadoop classpath):/opt/spark/examples/jars/*:/opt/spark/jars/kafka/*:/opt/kafka/libs/*
```

### <a name='SparkKafka'></a>2.9.4. 编写Spark程序使用Kafka数据源

下面，我们就可以进行程序编写了。

请新打开一个终端，然后，执行命令创建代码目录：

```sh
cd /opt/spark/mycode
mkdir kafka && cd kafka
vim KafkaWordCount.py
```

使用vim编辑器新建了KafkaWordCount.py，让它去进行词频统计。

请在KafkaWordCount.py中输入以下代码：

```py
from __future__ import print_function
 
import sys
 
from pyspark import SparkContext
from pyspark.streaming import StreamingContext
from pyspark.streaming.kafka import KafkaUtils
 
if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: kafka_wordcount.py <zk> <topic>", file=sys.stderr)
        exit(-1)   
        conf = SparkConf()
        conf.setAppName('PythonStreamingKafkaWordCount')
        conf.setMaster('local[2]')
        sc = SparkContext(conf = conf)
    ssc = StreamingContext(sc, 1)
 
    zkQuorum, topic = sys.argv[1:]
    kvs = KafkaUtils.createStream(ssc, zkQuorum, "spark-streaming-consumer", {topic: 1})
    lines = kvs.map(lambda x: x[1])
    counts = lines.flatMap(lambda line: line.split(" ")) \
        .map(lambda word: (word, 1)) \
        .reduceByKey(lambda a, b: a+b)
    counts.pprint()
 
    ssc.start()
    ssc.awaitTermination()
```

然后执行如下命令：

```sh
python3 ./KafkaWordCount.py localhost:2181 wordsendertest
```

这里我们继续使用上面第三个终端的topic。请继续在第三个终端上输入信息，就能看到当前python执行终端下显示你刚才新输入的结果。
运行上面命令以后，就启动了词频统计功能，屏幕上就会显示如下类似信息：

```s
-------------------------------------------
Time: 2017-12-12 10:57:46
-------------------------------------------
('ts', 1)

-------------------------------------------
Time: 2017-12-12 10:57:47
-------------------------------------------