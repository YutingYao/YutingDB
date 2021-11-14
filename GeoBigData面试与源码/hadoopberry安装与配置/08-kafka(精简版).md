<!-- vscode-markdown-toc -->
* 1. [安装包下载](#)
* 2. [zookeeper（最新版的kafka已经不需要zookeeper）](#zookeeperkafkazookeeper)
* 3. [kafka安装](#kafka)
	* 3.1. [server.properties配置](#server.properties)
	* 3.2. [producer.properties:生产端的配置文件](#producer.properties:)
	* 3.3. [consumer.properties:消费端的配置文件](#consumer.properties:)
	* 3.4. [ssh复制scp到其他节点](#sshscp)
* 4. [kafka简单示例，新版本：已经不用zookeeper了,改用bootstrapserver](#kafkazookeeperbootstrapserver)
	* 4.1. [ 测试验证](#-1)
* 5. [Kafka的bin目录配置到环境变量~/.bashrc](#Kafkabin.bashrc)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->
##  1. <a name=''></a>安装包下载

[kafka_2.12-3.0.0.tgz](https://dlcdn.apache.org/kafka/3.0.0/kafka_2.12-3.0.0.tgz)

[zookeeper](https://downloads.apache.org/zookeeper/)

##  2. <a name='zookeeperkafkazookeeper'></a>zookeeper（最新版的kafka已经不需要zookeeper）

```sh
tar -zxvf apache-zookeeper-3.7.0-bin.tar.gz -C /opt
cd /opt
sudo mv apache-zookeeper-3.7.0 zookeeper
cd zookeeper/conf
cp zoo_sample.cfg zoo.cfg
```

```sh
sudo vim zoo.cfg
```

```s
dataDir=/opt/zookeeper/data

server.3=ubuntu01:2888:3888
server.1=node01:2888:3888
server.2=node01:2888:3888
```

在 dataDir对应的目录下创建 myid文件 与 server.3  对应

```s
sudo vim /opt/zookeeper/data/myid
```

输入：3

```sh
scp -r /opt/zookeeper root@node01:/opt/
scp -r /opt/zookeeper root@node02:/opt/
```

将对应的zookeeper 文件夹发送到 node01  node02  修改对应 dataDir 对应目录下的myid

```s
sudo vim /opt/zookeeper/data/myid
```

输入：1

```s
sudo vim /opt/zookeeper/data/myid
```

输入：2


每个节点都启动zk：启动 zookeeper 

```sh
cd /opt/zookeeper/bin     
./zkServer.sh start
```

```sh
./zkServer.sh restart
```

```sh
./zkServer.sh status
```

```sh
./zkServer.sh stop
```

安装成功后显示Mode：standalone，follower，leader

可以看到xxx机器被zk选择为了leader，xxx和xxx为follower，

假如follower机器down掉后，zk会再次选择集群中的一台主机作为新的的leader，

如果集群down掉一半那么zk就无法选择leader了，同时无法正常工作了。

通过jps查看java进程，显示如下则表示启动成功：

```s
QuorumPeerMain
Jps
```

##  3. <a name='kafka'></a>kafka安装

到[Kafka官网](https://kafka.apache.org/downloads)下载安装文件时，一定要选择和自己电脑上已经安装的scala版本号一致才可以

```sh
cd ~/下载
sudo tar -zxf kafka_2.12-3.0.0.tgz -C /opt
cd /opt
sudo mv kafka_2.12-3.0.0 kafka
sudo chown -R yaoyuting03 kafka
```

```sh
cd /opt/kafka
ls
cd config
ls
```

###  3.1. <a name='server.properties'></a>server.properties配置

```sh
vim server.properties
```

包括：

3台机器不同部分：

```s
# 指定该节点的brokerId，同一集群中的brokerId需要唯一，可能不能是0
broker.id=3
# 指定监听的地址及端口号，该配置项是指定内网ip
listeners=PLAINTEXT://ubuntu01:9092
# 如果需要开放外网访问，则在该配置项指定外网ip
# advertised.listeners=PLAINTEXT://192.168.99.1:9092
host.name=ubuntu01
# 这个参数默认是关闭的
```

相同部分：

```s
# 指定kafka日志文件的存储目录
# kafka运行日志存放的路径 
# 这里的log非日志，而是kafka存储的物理数据
log.dirs=/opt/kafka/logs
# 指定zookeeper的连接地址，若有多个地址则用逗号分隔
zookeeper.connect=ubuntu01:2181,node01:2181,node02:2181
```

不需要修改的部分 ：

```s
#处理网络请求的线程数量，也就是接收消息的线程数。
#接收线程会将接收到的消息放到内存中，然后再从内存中写入磁盘。
#borker处理网络请求的线程数量 
num.network.threads=3 
#消息从内存中写入磁盘是时候使用的线程数量。
#用来处理磁盘IO的线程数量
#用来处理磁盘 IO的现成数量，要大于log.dirs的个数
num.io.threads=8 
#发送套接字的缓冲区大小 
# 发送缓冲区buffer大小，数据不是一下子就发送的，先回存储到缓冲区了到达一定的大小后在发送，能提高性能
socket.send.buffer.bytes=102400 
#接收套接字的缓冲区大小 
# kafka接收缓冲区大小，当数据到达一定大小后在序列化到磁盘
socket.receive.buffer.bytes=102400 
#请求套接字的缓冲区大小 
# 这个参数是向kafka请求消息或者向kafka发送消息的请请求的最大数，这个值不能超过java的堆栈大小
socket.request.max.bytes=104857600 
#topic在当前 broker上的分区个数
num.partitions=1 
#我们知道segment文件默认会被保留7天的时间，超时的话就
#会被清理，那么清理这件事情就需要有一些线程来做。这里就是
#用来设置恢复和清理data下数据的线程数量
#用来恢复和清理 data下数据的线程数量 
num.recovery.threads.per.data.dir=1 
#segment文件保留的最长时间，超时将被删除 
# 默认消息的最大持久化时间，168小时，7天
#segment文件保留的最长时间，默认保留7天（168小时），
#超时将被删除，也就是说7天之前的数据将被清理掉。
log.retention.hours=168 
#滚动生成新的segment文件的最大时间
log.roll.hours=168
# 单个.log 文件的最大大小
 # 这个参数是：因为kafka的消息是以追加的形式落地到文件，当超过这个值的时候，kafka会新起一个文件
 #日志文件中每个segment的大小，默认为1G
log.segment.bytes=1073741824
#用来监听链接的端口，producer或consumer将在此端口建立连接
 # port 当前kafka对外提供服务的端口默认是9092 生产者（producer）
port=9092 
advertised.port=9092
#删除topic需要server.properties中设置delete.topic.enable=true否则只是标记删除
delete.topic.enable=true
message.max.byte=5242880   # 消息保存的最大值5M
default.replication.factor=2   # kafka保存消息的副本数，如果一个副本失效了，另一个还可以继续提供服务
replica.fetch.max.bytes=5242880  # 取消息的最大直接数
#zookeeper链接超时时间
# 修改kafka 配置 连接超时间，这个一定配置上不然在默认的时间会出现连接超时
zookeeper.connection.timeout.ms=60000 
#上面的参数设置了每一个segment文件的大小是1G，那么
#就需要有一个东西去定期检查segment文件有没有达到1G，
#多长时间去检查一次，就需要设置一个周期性检查文件大小
#的时间（单位是毫秒）。
# 每隔300000毫秒去检查上面配置的log失效时间（log.retention.hours=168 ），到目录查看是否有过期的消息如果有，删除
log.retention.check.interval.ms=300000  
#日志清理是否打开
 # 是否启用log压缩，一般不用启用，启用的话可以提高性能
log.cleaner.enable=false 
#上面我们说过接收线程会将接收到的消息放到内存中，然后再从内存
#写到磁盘上，那么什么时候将消息从内存中写入磁盘，就有一个
#时间限制（时间阈值）和一个数量限制（数量阈值），这里设置的是
#数量阈值，下一个参数设置的则是时间阈值。
#partion buffer中，消息的条数达到阈值，将触发flush到磁盘。
log.flush.interval.messages=10000
#消息buffer的时间，达到阈值，将触发将消息从内存flush到磁盘，
#单位是毫秒。
log.flush.interval.ms=3000
```

###  3.2. <a name='producer.properties:'></a>producer.properties:生产端的配置文件

```s
#指定kafka节点列表，用于获取metadata，不必全部指定
#需要kafka的服务器地址，来获取每一个topic的分片数等元数据信息。
metadata.broker.list=ubuntu01:9092,node01:9092,node02:9092

#生产者生产的消息被发送到哪个block，需要一个分组策略。
#指定分区处理类。默认kafka.producer.DefaultPartitioner，表通过key哈希到对应分区
#partitioner.class=kafka.producer.DefaultPartitioner

#生产者生产的消息可以通过一定的压缩策略（或者说压缩算法）来压缩。消息被压缩后发送到broker集群，
#而broker集群是不会进行解压缩的，broker集群只会把消息发送到消费者集群，然后由消费者来解压缩。
#是否压缩，默认0表示不压缩，1表示用gzip压缩，2表示用snappy压缩。
#压缩后消息中会有头来指明消息压缩类型，故在消费者端消息解压是透明的无需指定。
#文本数据会以1比10或者更高的压缩比进行压缩。
compression.codec=none

#指定序列化处理类，消息在网络上传输就需要序列化，它有String、数组等许多种实现。
serializer.class=kafka.serializer.DefaultEncoder

#如果要压缩消息，这里指定哪些topic要压缩消息，默认empty，表示不压缩。
#如果上面启用了压缩，那么这里就需要设置
#compressed.topics=
#这是消息的确认机制，默认值是0。在面试中常被问到。
#producer有个ack参数，有三个值，分别代表：
#（1）不在乎是否写入成功；
#（2）写入leader成功；
#（3）写入leader和所有副本都成功；
#要求非常可靠的话可以牺牲性能设置成最后一种。
#为了保证消息不丢失，至少要设置为1，也就
#是说至少保证leader将消息保存成功。
#设置发送数据是否需要服务端的反馈,有三个值0,1,-1，分别代表3种状态：
#0: producer不会等待broker发送ack。生产者只要把消息发送给broker之后，就认为发送成功了，这是第1种情况；
#1: 当leader接收到消息之后发送ack。生产者把消息发送到broker之后，并且消息被写入到本地文件，才认为发送成功，这是第二种情况；#-1: 当所有的follower都同步消息成功后发送ack。不仅是主的分区将消息保存成功了，
#而且其所有的分区的副本数也都同步好了，才会被认为发动成功，这是第3种情况。
request.required.acks=0

#broker必须在该时间范围之内给出反馈，否则失败。
#在向producer发送ack之前,broker允许等待的最大时间 ，如果超时,
#broker将会向producer发送一个error ACK.意味着上一次消息因为某种原因
#未能成功(比如follower未能同步成功)
request.timeout.ms=10000

#生产者将消息发送到broker，有两种方式，一种是同步，表示生产者发送一条，broker就接收一条；
#还有一种是异步，表示生产者积累到一批的消息，装到一个池子里面缓存起来，再发送给broker，
#这个池子不会无限缓存消息，在下面，它分别有一个时间限制（时间阈值）和一个数量限制（数量阈值）的参数供我们来设置。
#一般我们会选择异步。
#同步还是异步发送消息，默认“sync”表同步，"async"表异步。异步可以提高发送吞吐量,
#也意味着消息将会在本地buffer中,并适时批量发送，但是也可能导致丢失未发送过去的消息
producer.type=sync

#在async模式下,当message被缓存的时间超过此值后,将会批量发送给broker,
#默认为5000ms
#此值和batch.num.messages协同工作.
queue.buffering.max.ms = 5000

#异步情况下，缓存中允许存放消息数量的大小。
#在async模式下,producer端允许buffer的最大消息量
#无论如何,producer都无法尽快的将消息发送给broker,从而导致消息在producer端大量沉积
#此时,如果消息的条数达到阀值,将会导致producer端阻塞或者消息被抛弃，默认为10000条消息。
queue.buffering.max.messages=20000

#如果是异步，指定每次批量发送数据量，默认为200
batch.num.messages=500

#在生产端的缓冲池中，消息发送出去之后，在没有收到确认之前，该缓冲池中的消息是不能被删除的，
#但是生产者一直在生产消息，这个时候缓冲池可能会被撑爆，所以这就需要有一个处理的策略。
#有两种处理方式，一种是让生产者先别生产那么快，阻塞一下，等会再生产；另一种是将缓冲池中的消息清空。
#当消息在producer端沉积的条数达到"queue.buffering.max.meesages"后阻塞一定时间后,
#队列仍然没有enqueue(producer仍然没有发送出任何消息)
#此时producer可以继续阻塞或者将消息抛弃,此timeout值用于控制"阻塞"的时间
#-1: 不限制阻塞超时时间，让produce一直阻塞,这个时候消息就不会被抛弃
#0: 立即清空队列,消息被抛弃
queue.enqueue.timeout.ms=-1

#当producer接收到error ACK,或者没有接收到ACK时,允许消息重发的次数
#因为broker并没有完整的机制来避免消息重复,所以当网络异常时(比如ACK丢失)
#有可能导致broker接收到重复的消息,默认值为3.
message.send.max.retries=3

#producer刷新topic metada的时间间隔,producer需要知道partition leader
#的位置,以及当前topic的情况
#因此producer需要一个机制来获取最新的metadata,当producer遇到特定错误时,
#将会立即刷新
#(比如topic失效,partition丢失,leader失效等),此外也可以通过此参数来配置
#额外的刷新机制，默认值600000
topic.metadata.refresh.interval.ms=60000
```

###  3.3. <a name='consumer.properties:'></a>consumer.properties:消费端的配置文件

```s
#消费者集群通过连接Zookeeper来找到broker。
#zookeeper连接服务器地址
zookeeper.connect=ubuntu01:2181,node01:2181,node02:2181

#zookeeper的session过期时间，默认5000ms，用于检测消费者是否挂掉
zookeeper.session.timeout.ms=5000

#当消费者挂掉，其他消费者要等该指定时间才能检查到并且触发重新负载均衡
zookeeper.connection.timeout.ms=10000

#这是一个时间阈值。
#指定多久消费者更新offset到zookeeper中。
#注意offset更新时基于time而不是每次获得的消息。
#一旦在更新zookeeper发生异常并重启，将可能拿到已拿到过的消息
zookeeper.sync.time.ms=2000

#指定消费
group.id=xxxxx

#这是一个数量阈值，经测试是500条。
#当consumer消费一定量的消息之后,将会自动向zookeeper提交offset信息#注意offset信息并不是每消费一次消息就向zk提交
#一次,而是现在本地保存(内存),并定期提交,默认为true
auto.commit.enable=true

# 自动更新时间。默认60 * 1000
auto.commit.interval.ms=1000

# 当前consumer的标识,可以设定,也可以有系统生成,
#主要用来跟踪消息消费情况,便于观察
conusmer.id=xxx

# 消费者客户端编号，用于区分不同客户端，默认客户端程序自动产生
client.id=xxxx

# 最大取多少块缓存到消费者(默认10)
queued.max.message.chunks=50

# 当有新的consumer加入到group时,将会reblance,此后将会
#有partitions的消费端迁移到新 的consumer上,如果一个
#consumer获得了某个partition的消费权限,那么它将会向zk
#注册 "Partition Owner registry"节点信息,但是有可能
#此时旧的consumer尚没有释放此节点, 此值用于控制,
#注册节点的重试次数.
rebalance.max.retries=5

#每拉取一批消息的最大字节数
#获取消息的最大尺寸,broker不会像consumer输出大于
#此值的消息chunk 每次feth将得到多条消息,此值为总大小,
#提升此值,将会消耗更多的consumer端内存
fetch.min.bytes=6553600

#当消息的尺寸不足时,server阻塞的时间,如果超时,
#消息将立即发送给consumer
#数据一批一批到达，如果每一批是10条消息，如果某一批还
#不到10条，但是超时了，也会立即发送给consumer。
fetch.wait.max.ms=5000
socket.receive.buffer.bytes=655360

# 如果zookeeper没有offset值或offset值超出范围。
#那么就给个初始的offset。有smallest、largest、
#anything可选，分别表示给当前最小的offset、
#当前最大的offset、抛异常。默认largest
auto.offset.reset=smallest

# 指定序列化处理类
derializer.class=kafka.serializer.DefaultDecoder
```

###  3.4. <a name='sshscp'></a>ssh复制scp到其他节点

将对应的 kafka 文件夹发送到 node01  node02  修改对应 server.properties

```s
scp -r /opt/kafka root@node01:/opt/
scp -r /opt/kafka root@node02:/opt/
```

修改另外2节点kafka的配置文件，修改

```s
broker.id=1
listeners=PLAINTEXT://node01:9092
#此处的host.name为本机IP(重要),如果不改,则客户端会抛出:
#Producer connection to localhost:9092 unsuccessful 错误!
host.name=node01
```

```s
broker.id=2
listeners=PLAINTEXT://node02:9092
host.name=node02
```

（外网访问可能需要改：）

```s
advertised.host.name=node01
```

```s
advertised.host.name=node02
```

注：启动前删除server.properties中log.dirs对应目录中的/opt/kafka/logs，

因为上面跑单机的时候产生的文件，不删除的话kafka集群起不来

logs里面有个meta.properties, 有broker id, 可能会和server.properties对不上, 所以直接删除了logs,

```s
sudo rm -rf /opt/kafka/logs
```

以前的版本，必须先启动zookeeper，现在的版本不需要

```s
cd /opt/kafka/bin
# 启动kafka，node01和node02可能也需要启动
./kafka-server-start.sh -daemon /opt/kafka/config/server.properties
```

查看下日志是否真的启动成功了

通过jps查看java进程，显示如下则表示启动成功：

```s
QuorumPeerMain
Jps
Kafka
```

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


##  4. <a name='kafkazookeeperbootstrapserver'></a>kafka简单示例，新版本：已经不用zookeeper了,改用bootstrapserver

###  4.1. <a name='-1'></a> 测试验证

3台机器启动kafka

```s
cd /opt/kafka/bin
```

```s
./kafka-server-start.sh -daemon /opt/kafka/config/server.properties
```

查看jps

创建topic：（任意一台机器都可以）

```s
./kafka-topics.sh --create --bootstrap-server ubuntu01:9092,node01:9092,node02:9092 --replication-factor 3 --partitions 1 --topic test6
```

查看详细描述

```s
./kafka-topics.sh --describe --bootstrap-server ubuntu01:9092,node01:9092,node02:9092 --topic test6
```

创建生产者：（任意一台机器都可以，发送hello）

```s
./kafka-console-producer.sh --broker-list ubuntu01:9092,node01:9092,node02:9092 --topic test6
```

创建消费者：（任意一台机器都可以，不过可能需要新建窗口，接收hello）

```s
./kafka-console-consumer.sh --bootstrap-server ubuntu01:9092,node01:9092,node02:9092  --topic test6 --from-beginning
```

测试容错性，kill掉主节点2，再次查看my-replicated-topic

```s
kill -9 <jps的序号>
```

##  5. <a name='Kafkabin.bashrc'></a>Kafka的bin目录配置到环境变量~/.bashrc

```s
sudo vim ~/.bashrc
```

```s
export KAFKA_HOME=/opt/kafka
export PATH=$PATH:$KAFKA_HOME/bin

export ZOOKEEPER_HOME=/opt/zookeeper
export PATH=$PATH:$ZOOKEEPER_HOME/bin
```

```sh
source ~/.bashrc
```