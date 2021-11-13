[kafka_2.12-3.0.0.tgz](https://dlcdn.apache.org/kafka/3.0.0/kafka_2.12-3.0.0.tgz)

[zookeeper](https://downloads.apache.org/zookeeper/)

## zookeeper

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

server.0=ubuntu01:2888:3888
server.1=node01:2888:3888
server.2=node01:2888:3888 
```

补充：(可跳过)

```s
maxClientCnxns=60
autopurge.snapRetainCount=3
autopurge.purgeInterval=1
```

补充：(可跳过)

```s
tickTime=2000
initLimit=10
syncLimit=5
```

补充：(可跳过)

```s
dataDir=/opt/zookeeper/data
dataLogDir=/opt/zookeeper/log

clientPort=2181
```

Zookeeper 默认会将`控制台信息`输出到启动路径下的 `zookeeper.out` 中，通过如下方法，可以让 Zookeeper 输出按尺寸切分的日志文件：(可跳过)

```sh
vim log4j.properties
```

```s
zookeeper.root.logger=INFO, CONSOLE
    改为
zookeeper.root.logger=INFO, ROLLINGFILE
```

```sh
cd bin
vim zkEnv.sh
```

```s
ZOO_LOG4J_PROP="INFO,CONSOLE"
    改为
ZOO_LOG4J_PROP="INFO,ROLLINGFILE"
```

```sh
cd /opt/zookeeper/data
ls
```

在 dataDir对应的目录下创建 myid文件 与 server.0  对应

```s
echo 0 > /opt/zookeeper/data/myid
```

```sh
scp -r /opt/zookeeper root@node01:/opt/
scp -r /opt/zookeeper root@node02:/opt/
```

将对应的zookeeper 文件夹发送到 node01  node02  修改对应 dataDir 对应目录下的myid

```s
echo 1 > /opt/zookeeper/data/myid
```

```s
echo 2 > /opt/zookeeper/data/myid
```

下面介绍Kafka相关概念,以便运行下面实例的同时，更好地理解Kafka.

每个节点都启动zk：启动 zookeeper 

```sh
cd /opt/zookeeper/bin     
./zkServer.sh start
./zkServer.sh stop
./zkServer.sh status
```

安装成功后显示Mode：standalone，follower，leader

可以看到xxx机器被zk选择为了leader，xxx和xxx为follower，假如follower机器down掉后，zk会再次选择集群中的一台主机作为新的的leader，如果集群down掉一半那么zk就无法选择leader了，同时无法正常工作了。

通过jps查看java进程，显示如下则表示启动成功：

```s
QuorumPeerMain
Jps
```

## kafka安装

到[Kafka官网](https://kafka.apache.org/downloads)下载安装文件时，一定要选择和自己电脑上已经安装的scala版本号一致才可以

```sh
cd ~/下载
sudo tar -zxf kafka_2.12-3.0.0.tgz -C /opt
cd /opt
sudo mv kafka_2.12-3.0.0 kafka
sudo chown -R yaoyuting kafka
```

```sh
cd /opt/kafka
ls
cd config
ls
```

```sh
vim server.properties
```

（可能还需要修改这个，可跳过）

```s
# 指定该节点的brokerId，同一集群中的brokerId需要唯一
broker.id=0
# 指定监听的地址及端口号，该配置项是指定内网ip
listeners=PLAINTEXT://192.168.99.1:9092
# 如果需要开放外网访问，则在该配置项指定外网ip
# advertised.listeners=PLAINTEXT://192.168.99.1:9092
# 指定kafka日志文件的存储目录
log.dirs=/opt/kafka/logs
# 指定zookeeper的连接地址，若有多个地址则用逗号分隔
zookeeper.connect=192.168.99.4:2181
```

修改 ：

listeners=PLAINTEXT://ubuntu01:9092

（可能还需要修改这个，可跳过）

```s
listeners=PLAINTEXT://node01:9092
```

```s
listeners=PLAINTEXT://node02:9092
```

（可能还需要修改zookeeper.connect，可跳过）

修改zookeeper.connect为三台机器的ip，用逗号隔开；

zookeeper.connect=ubuntu01:2181,node01:2181,node02:2181

（可能还需要修改这个，可跳过）

```s
broker.id=10 
port=9092  # port 当前kafka对外提供服务的端口默认是9092 生产者（producer）
host.name=ubuntu01 # 这个参数默认是关闭的
advertised.port=9092
advertised.host.name=ubuntu01
num.network.threads=3 # 这个是borker进行网络处理的线程数 
num.io.threads=8  # 这个是borker进行I/O处理的线程数，要大于log.dirs的个数
log.dirs=/opt/kafka/logs #消息存放的目录，num.io.threads要大于这个目录的个数
delete.topic.enable=true
socket.send.buffer.bytes=102400  # 发送缓冲区buffer大小，数据不是一下子就发送的，先回存储到缓冲区了到达一定的大小后在发送，能提高性能
socket.receive.buffer.bytes=102400  # kafka接收缓冲区大小，当数据到达一定大小后在序列化到磁盘
socket.request.max.bytes=104857600  # 这个参数是向kafka请求消息或者向kafka发送消息的请请求的最大数，这个值不能超过java的堆栈大小
num.partitions=1  # 默认的分区数，一个topic默认1个分区数
log.retention.hours=168  # 默认消息的最大持久化时间，168小时，7天
message.max.byte=5242880   # 消息保存的最大值5M
default.replication.factor=2   # kafka保存消息的副本数，如果一个副本失效了，另一个还可以继续提供服务
replica.fetch.max.bytes=5242880  # 取消息的最大直接数
log.segment.bytes=1073741824  # 这个参数是：因为kafka的消息是以追加的形式落地到文件，当超过这个值的时候，kafka会新起一个文件
log.retention.check.interval.ms=300000  # 每隔300000毫秒去检查上面配置的log失效时间（log.retention.hours=168 ），到目录查看是否有过期的消息如果有，删除
log.cleaner.enable=false  # 是否启用log压缩，一般不用启用，启用的话可以提高性能
zookeeper.connect=ubuntu01:2181, 192.168.66.11:2181, 192.168.66.12:2181
zookeeper.connection.timeout.ms=60000 # 修改kafka 配置 连接超时间，这个一定配置上不然在默认的时间会出现连接超时
```

```s
#broker的全局唯一编号，不能重复 
broker.id=0 
# server接受客户端连接的端口，ip配置kafka本机ip即可
advertised.listeners=PLAINTEXT://172.16.117.6:9092
#处理网络请求的线程数量 
num.network.threads=3 
#用来处理磁盘 IO的现成数量 
num.io.threads=8 
#发送套接字的缓冲区大小 
socket.send.buffer.bytes=102400 
#接收套接字的缓冲区大小 
socket.receive.buffer.bytes=102400 
#请求套接字的缓冲区大小 
socket.request.max.bytes=104857600 
#kafka运行日志存放的路径 
# 这里的log非日志，而是kafka存储的物理数据
log.dirs=/data/kafka/kafka/logs#topic在当前 broker上的分区个数 
num.partitions=1 
#用来恢复和清理 data下数据的线程数量 
num.recovery.threads.per.data.dir=1 
#segment文件保留的最长时间，超时将被删除 
log.retention.hours=168 
# 单个.log 文件的最大大小
log.segment.bytes=1073741824
#配置连接 Zookeeper集群地址 
zookeeper.connect=xiaoyunshi1:2181,xiaoyunshi2:2181,xiaoyunshi3:2181
```

```s
#broker的全局唯一编号，不能重复
broker.id=0

#用来监听链接的端口，producer或consumer将在此端口建立连接
port=9092

#处理网络请求的线程数量，也就是接收消息的线程数。
#接收线程会将接收到的消息放到内存中，然后再从内存中写入磁盘。
num.network.threads=3

#消息从内存中写入磁盘是时候使用的线程数量。
#用来处理磁盘IO的线程数量
num.io.threads=8

#发送套接字的缓冲区大小
socket.send.buffer.bytes=102400

#接受套接字的缓冲区大小
socket.receive.buffer.bytes=102400

#请求套接字的缓冲区大小
socket.request.max.bytes=104857600

#kafka运行日志存放的路径
log.dirs=/export/servers/logs/kafka

#topic在当前broker上的分片个数
num.partitions=2

#我们知道segment文件默认会被保留7天的时间，超时的话就
#会被清理，那么清理这件事情就需要有一些线程来做。这里就是
#用来设置恢复和清理data下数据的线程数量
num.recovery.threads.per.data.dir=1

#segment文件保留的最长时间，默认保留7天（168小时），
#超时将被删除，也就是说7天之前的数据将被清理掉。
log.retention.hours=168

#滚动生成新的segment文件的最大时间
log.roll.hours=168

#日志文件中每个segment的大小，默认为1G
log.segment.bytes=1073741824

#上面的参数设置了每一个segment文件的大小是1G，那么
#就需要有一个东西去定期检查segment文件有没有达到1G，
#多长时间去检查一次，就需要设置一个周期性检查文件大小
#的时间（单位是毫秒）。
log.retention.check.interval.ms=300000

#日志清理是否打开
log.cleaner.enable=true

#broker需要使用zookeeper保存meta数据
zookeeper.connect=ubuntu01:2181,node01:2181,node02:2181

#zookeeper链接超时时间
zookeeper.connection.timeout.ms=6000

#上面我们说过接收线程会将接收到的消息放到内存中，然后再从内存
#写到磁盘上，那么什么时候将消息从内存中写入磁盘，就有一个
#时间限制（时间阈值）和一个数量限制（数量阈值），这里设置的是
#数量阈值，下一个参数设置的则是时间阈值。
#partion buffer中，消息的条数达到阈值，将触发flush到磁盘。
log.flush.interval.messages=10000

#消息buffer的时间，达到阈值，将触发将消息从内存flush到磁盘，
#单位是毫秒。
log.flush.interval.ms=3000

#删除topic需要server.properties中设置delete.topic.enable=true否则只是标记删除
delete.topic.enable=true

#此处的host.name为本机IP(重要),如果不改,则客户端会抛出:
#Producer connection to localhost:9092 unsuccessful 错误!
host.name=kafka01

advertised.host.name=192.168.239.128
```

```s
# ubuntu01
broker.id=0
log.dirs=/opt/kafka/logs #消息存放的目录，num.io.threads要大于这个目录的个数
zookeeper.connect=ubuntu01:2181,node01:2181,node02:2181
```

producer.properties:生产端的配置文件

```s
#指定kafka节点列表，用于获取metadata，不必全部指定
#需要kafka的服务器地址，来获取每一个topic的分片数等元数据信息。
metadata.broker.list=kafka01:9092,kafka02:9092,kafka03:9092

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

consumer.properties:消费端的配置文件

```s
#消费者集群通过连接Zookeeper来找到broker。
#zookeeper连接服务器地址
zookeeper.connect=zk01:2181,zk02:2181,zk03:2181

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

将对应的 kafka 文件夹发送到 node01  node02  修改对应 server.properties

```sh
scp -r /opt/kafka root@node01:/opt/
scp -r /opt/kafka root@node02:/opt/
```

修改另外2节点kafka的配置文件，修改
- broker.id，
- host.name，
- advertised.host.name为各节点的IP地址最后三位数字和对应主机IP地址

（可能不需要改）

```s
# 修改brokerId
broker.id=1
# 指定监听的地址及端口号，该配置项是指定内网ip
listeners=PLAINTEXT://192.168.99.2:9092
# 如果需要开放外网访问，则在该配置项指定外网ip
# advertised.listeners=PLAINTEXT://192.168.99.2:9092
```

```s
# 修改brokerId
broker.id=2
# 指定监听的地址及端口号，该配置项是指定内网ip
listeners=PLAINTEXT://192.168.99.3:9092
# 如果需要开放外网访问，则在该配置项指定外网ip
# advertised.listeners=PLAINTEXT://192.168.99.3:9092
```

（可能不需要改）

```s
host.name=node01
advertised.host.name=node01
```

（可能不需要改）

```s
host.name=node02
advertised.host.name=node02
```

```s
# node01
broker.id=1
log.dirs=/opt/kafka/logs
zookeeper.connect=ubuntu01:2181,node01:2181,node02:2181
```

```s
# node02
broker.id=2
log.dirs=/opt/kafka/logs
zookeeper.connect=ubuntu01:2181,node01:2181,node02:2181
```

注：启动前删除server.properties中log.dirs对应目录中的kafka-logs，因为上面跑单机的时候产生的文件，不删除的话kafka集群起不来，另外防火墙要开放9092端口

必须先启动zookeeper

```sh
# 启动kafka，node01和node02可能也需要启动
./kafka-server-start.sh -daemon /opt/kafka/config/server.properties
```

查看下日志是否真的启动成功了

```
tail -200f /opt/kafka/logs/server.log
```

通过jps查看KAFKA进程

```s
#查看服务进程和端口；
ps -ef|grep -ai kafka
netstat -tnlp|grep -aiwE 9092
```

[zk: localhost:2181(CONNECTED) 6]

```s
ls /brokers/ids
```

[0, 1, 2]

如上，可查看 broker 的节点 id 信息，通过 get 查看具体某个节点的信息：

```s
get /brokers/ids/1
```

返回：

- {"listener_security_protocol_map":
  - {"PLAINTEXT":"PLAINTEXT"},
  - "endpoints":["PLAINTEXT://xiaoyunshi1:9092"],
  - "jmx_port":-1,
  - "host":"xiaoyunshi1",
  - "timestamp":"1632838948289",
  - "port":9092,
  - "version":4}

其他kafka 节点信息如下：

[zk: localhost:2181(CONNECTED) 15] 

```s
ls /
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

## 配置开机启动(可跳过)

```sh
cd /etc/rc.d/init.d/

vim zookeeper
```

```sh
#!/bin/bash
#chkconfig: 2345 10 90
#description: service zookeeper
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-arm64/
export ZOO_LOG_DIR=/opt/zookeeper/logs
ZOOKEEPER_HOME=/opt/zookeeper
su root ${ZOOKEEPER_HOME}/bin/zkServer.sh start
```

```sh
## 添加权限
chmod +x zookeeper
## 开机自启
chkconfig --add zookeeper
## 查看
chkconfig --list
```

```sh
vim startCluster.sh
```

```sh
#!/bin/sh
case $1 in
"start"){
for host in ubuntu01 node01 node02
do
  ssh $host "source /etc/profile; nohup /opt/kafka/bin/kafka-server-start.sh -daemon /opt/kafka/config/server.properties > /opt/kafka/logs/null 2>&1 &"
  echo "$host kafka is running..."
  sleep 1.5s
done
};;
"stop"){
for host in ubuntu01 node01 node02
do
  ssh $host "source /etc/profile; nohup /opt/kafka/bin/kafka-server-stop.sh >/opt/kafka/logs/null 2>&1 &"
  echo "$host kafka is stopping..."
  sleep 1.5s
done
};;
esac
```

## kafka简单实用

###  测试验证

创建Topic–test

```s
bin/kafka-topics.sh --create --zookeeper ubuntu01:2181,node01:2181,node02:2181 --replication-factor 1 --partitions 3 --topic test
```

查看已经创建的Topic列表

```s
bin/kafka-topics.sh --list --zookeeper localhost:2181
```

在master启动生产者（任意一台）

```s
bin/kafka-console-producer.sh --broker-list ubuntu01:9092,node01:9092,node02:9092 --topic test
```

在另外两台服务器其中之一启动消费者

```s
bin/kafka-console-consumer.sh --bootstrap-server ubuntu01:9092,node01:9092,node02:9092 --from-beginning --topic test
```

生产者的服务器输入内容，消费者服务器就会显示：

- 生产者

```s
bin/kafka-console-producer.sh --broker-list ubuntu01:9092,node01:9092,node02:9092 --topic test

>hello world！
```

- 消费者

```s
bin/kafka-console-consumer.sh --bootstrap-server ubuntu01:9092,node01:9092,node02:9092 --topic test

hello world!
```

### 创建topic：

```s
bin/kafka-topics.sh --bootstrap-server 主机名：9092 --create --topic topic名 --partitions 分区数量 --replication-factor 分区因子数值
eg：
bin/kafka-topics.sh --bootstrap-server ubuntu01:9092 --create --topic topic01 --partitions 3 --replication-factor 1
```

```s
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic myFirstTopic
```

查看myFirstTopic的详细信息

```s
bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic myFirstTopic
```

可以看出myFirstTopic没有副本，只有我们创建它时只有唯一的节点0。

创建一个新的topic，1个分区，3个节点

```s
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 3 --partitions 1 --topic my-replicated-topic
```

查看刚才创建的topic

```s
bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic my-replicated-topic
```

测试容错性，kill掉主节点2，再次查看my-replicated-topic

可以看出节点2挂掉后，节点1充当了新的leader。

查看topic列表

```s
bin/kafka-topics.sh --list --zookeeper localhost:2181
```

三个节点都需要写上：

```s
# 我感觉这个版本的不对
bin/kafka-topics.sh --bootstrap-server ubuntu01:9092,node01:9092,node02:9092 --list
```

集群创建topic,三个节点都需要写上：

```s
# 我感觉这个版本的不对
bin/kafka-topics.sh --bootstrap-server ubuntu01:9092,node01:9092,node02:9092 -create --topic topic02 --partitions 3 --replication-factor 3
```

### 生产者和消费者

发送消息(生产者)

```s
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic myFirstTopic
```

```s
bin/kafka-console-producer.sh --broker-list ubuntu01:9092,node01:9092,node02:9092 --topic test
```

消费消息(消费者)

```s
bin/kafka-console-consumer.sh --bootstrap-server (新版本)localhost:9092 --topic myFirstTopic --from-beginning
```

```s
bin/kafka-console-consumer.sh –zookeeper (老版本) 192.168.66.10:2181,192.168.66.11:2181,192.168.66.12:2181  --from-beginning  --topic  test
```

## Kafka的bin目录配置到环境变量~/.bashrc

```s
sudo vim ~/.bashrc
```

```s
export KAFKA_HOME=/opt/kafka
export PATH=$PATH:$KAFKA_HOME/bin
```

```sh
source ~/.bashrc
```

## 安装成功了Kafka

进入kafka所在的目录

```sh
cd /opt/kafka
./bin/zookeeper-server-start.sh config/zookeeper.properties
```

执行以上命令后，启动日志会输出到控制台，可以通过日志判断是否启动成功，也可以通过查看是否监听了9092端口来判断是否启动成功：

```s
netstat -lntp |grep 9092
```

tcp6    0     0 192.168.99.1:9092     :::*      LISTEN     31943/java

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
./bin/kafka-console-consumer.sh --zookeeper (老版本) localhost:2181 --topic dblab --from-beginning  
```

请另外打开第四个终端，输入下面命令：

```sh
cd /opt/kafka
./bin/kafka-console-consumer.sh --zookeeper (老版本)localhost:2181 --topic wordsendertest --from-beginning
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
```

## 故障排查

如果出现commit_memory(0x00000000c0000000, 1073741824, 0) failed 错误,

```s
报错内容：

内存设置异常；

原因分析：

kafka默认启动内存为1G,可以在配置文件中修改，修改/usr/local/kafka/bin/kafka-server-start.sh重新设置文件中的KAFKA_HEAP_OPTS参数。
```

UnknownTopicOrPartitionException

org.apache.kafka.common.errors.UnknownTopicOrPartitionException:

This server does not host this topic-partition

```s
报错内容：

分区数据不在

原因分析：

producer向不存在的topic发送消息，用户可以检查topic是否存在 或者设置auto.create.topics.enable参数。
```

LEADER_NOT_AVAILABLE

WARN Error while fetching metadata with correlation id 0 : {test=LEADER_NOT_AVAILABLE} (org.apache.kafka.clients.NetworkClient

```s
报错内容：

leader不可用

原因分析：

原因很多topic正在被删除 正在进行leader选举 使用kafka-topics脚本检查leader信息，进而检查broker的存活情况 尝试重启解决。
```

NotLeaderForPartitionException

org.apache.kafka.common.errors.NotLeaderForPartitionException: This server is not the leader for that topic-partition

```s
报错内容：

broker已经不是对应分区的leader了

原因分析：

发生在leader变更时 当leader从一个broker切换到另一个broker时，要分析什么原因引起了leader的切换。
```

TimeoutException

org.apache.kafka.common.errors.TimeoutException: Expiring 5 record(s) for test-0: 30040 ms has passe

```s
报错内容：

请求超时

原因分析：

观察哪里抛出的 观察网络是否能通 如果可以通 可以考虑增加request.timeout.ms的值
```

RecordTooLargeException

WARN async.DefaultEventHandler: Produce request with correlation id 92548048 failed due to [TopicName,1]: org.apache.kafka.common.errors.RecordTooLargeException

```s
报错内容：

消息过大

原因分析：

生产者端 消息处理不过来了 可以增加 request.timeout.ms 减少 batch.size。
```

Closing socket connection

Closing socket connection to/127,0,0,1.(kafka.network.Processor)

```s
报错内容：

连接关闭

原因分析：

如果javaApi producer版本高，想在客户端consumer启动低版本验证，会不停的报错，无法识别客户端消息。
```

ConcurrentModificationException

java.util.ConcurrentModificationException: KafkaConsumer is not safe for multi-threaded access

```s
报错内容：

线程不安全

原因分析：

Kafka consumer是非线程安全的。
```

NetWorkException [kafka-producer-network-thread | producer-1] o.apache.kafka.common.network.Selector : [Producer clientId=producer-1] Connection with / disconnected

```s
报错内容：

网络异常

原因分析：

网络连接中断 检查broker的网络情况。
```

ILLEGAL_GENERATIONILLEGAL_GENERATION occurred while committing offsets for group

```s
报错内容：

无效的“代”

原因分析：

consumer错过了 rebalance 原因是consumer花了大量时间处理数据。

需要适当减少 max.poll.records值 增加 max.poll.interval.ms 或者想办法增加消息处理的速度。
```

