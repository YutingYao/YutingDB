## 下载 spark-3.2.0-bin-hadoop3.2.tgz

[下载Spark](https://www.apache.org/dyn/closer.lua/spark/spark-3.2.0/spark-3.2.0-bin-hadoop3.2.tgz)，解包并授予pi所有权

```bash
sudo tar zxvf spark-3.2.0-bin-hadoop3.2.tgz  -C /opt
```

进入到解压目录并重命名：

```bash
cd /opt
sudo mv spark-3.2.0-bin-hadoop3.2 spark
```

```sh
sudo chown yaoyuting03 -R /opt/spark
```

```sh
sudo chown root -R /opt/spark
```

## 配置Spark环境变量

```sh
sudo su
```

```sh
sudo vim ~/.bashrc
```

添加（在文件顶部插入）：

```sh
export SPARK_HOME=/opt/spark
export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin
```

```sh
export PYTHONPATH=$SPARK_HOME/python:$SPARK_HOME/python/lib/py4j-0.10.9.2-src.zip:$PYTHONPATH
export PYSPARK_PYTHON=python3
```

最终变成如下效果：

```sh
export SPARK_HOME=/opt/spark
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-arm64/
export HADOOP_HOME=/opt/hadoop
export PYSPARK_PYTHON=python3
export PYTHONPATH=$SPARK_HOME/python:$SPARK_HOME/python/lib/py4j-0.10.9.2-src.zip:$PYTHONPATH
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export LD_LIBRARY_PATH=$HADOOP_HOME/lib/native:$LD_LIBRARY_PATH
export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
```

PYTHONPATH环境变量主要是为了在Python3中引入pyspark库，

PYSPARK_PYTHON变量主要是设置pyspark运行的python版本。

验证Spark安装。

```sh
source ~/.bashrc
spark-shell --version
```

通过运行Spark自带的示例，验证Spark是否安装成功。、

```sh
cd /opt/spark
bin/run-example SparkPi
```

执行时会输出非常多的运行信息，输出结果不容易找到，

可以通过 grep 命令进行过滤

（命令中的 2>&1 可以将所有的信息都输出到 stdout 中，否则由于输出日志的性质，还是会输出到屏幕中）

```sh
bin/run-example SparkPi 2>&1 | grep "Pi is"
```

这里涉及到Linux Shell中管道的知识，详情可以参考[Linux Shell中的管道命令](http://dblab.xmu.edu.cn/blog/824-2/)

过滤后的运行结果如下图示，可以得到π 的 5 位小数近似值：

## 配置spark-env.sh

安装后，还需要修改Spark的配置文件spark-env.sh

```sh
cd opt/spark/conf
cp spark-env.sh.template spark-env.sh
sudo vim spark-env.sh
```

编辑spark-env.sh,添加如下内容：用于集群环境

```sh
export SPARK_DIST_CLASSPATH=$(/opt/hadoop/bin/hadoop classpath)
export HADOOP_CONF_DIR=/opt/hadoop/etc/hadoop
export SPARK_MASTER_IP=192.168.31.189
```

有了上面的配置信息以后，Spark就可以把数据存储到Hadoop分布式文件系统HDFS中，也可以从HDFS中读取数据。如果没有配置上面信息，Spark就只能读写本地数据，无法读写HDFS数据。

SPARK_MASTER_IP 指定 Spark 集群 Master node的 IP 地址；

## 配置workers

在Masternode主机上进行如下操作：

配置workers文件
将 workers.template 拷贝到 workers

```sh
cd /opt/spark/conf
cp workers.template workers
sudo vim workers
```

注：我们会发现workers文件里为localhost即本机地址，当前为伪分布式，因此不用修改

如果要变成集群环境，则需要修改：

workers文件设置Workernode。编辑workers内容,把默认内容localhost替换成如下内容：

```sh
node01
node02
```

## /opt/spark文件夹复制

配置好后，将Master主机上的/opt/spark文件夹复制到各个node上。在Master主机上执行如下命令：

```s
scp -r /opt/spark root@node01:/opt/
scp -r /opt/spark root@node02:/opt/
```

在node01,node02node上分别执行下面同样的操作：

```sh
sudo chown -R hadoop /opt/spark
```

## 启动Spark集群

### 启动Hadoop集群

启动Spark集群前，要先启动Hadoop集群。

前面已经安装了Hadoop和Spark，

如果Spark不使用HDFS和YARN，那么就不用启动Hadoop也可以正常使用Spark。

如果在使用Spark的过程中需要用到 HDFS，

就要首先启动 Hadoop（启动Hadoop的方法可以参考上面给出的Hadoop安装教程）。

在Masternode主机上运行如下命令：

```sh
start-all.sh
```

### 启动Spark集群

启动Masternode

在Masternode主机上运行如下命令：

```sh
start-master.sh
```

在Masternode上运行jps命令，可以看到多了个Master进程：

```sh
jps
```

15093 Jps
14343 SecondaryNameNode
14121 NameNode
14891 Master
14509 ResourceManager

启动所有Slavenode
在Masternode主机上运行如下命令：

```sh
start-workers.sh
```

分别在node01、mode02node上运行jps命令，可以看到多了个Worker进程

```sh
jps
```

37553 DataNode
37684 NodeManager
37876 Worker
37924 Jps

总结：

<http://ubuntu01:8099/> （单机模式）

访问<http://ubuntu01:8080>,（集群模式）

### 关闭Spark集群

关闭Masternode

```sh
stop-master.sh
```

关闭Workernode

```sh
stop-workers.sh
```

关闭Hadoop集群

```sh
stop-all.sh
```

### 配置Spark作业监控

与Hadoop类似，Spark还提供监视您部署的作业的功能。但是，使用Spark，我们必须手动配置监控选项。

生成并修改Spark默认配置文件：

```sh
cd $SPARK_HOME/conf
sudo mv spark-defaults.conf.template spark-defaults.conf
vim spark-defaults.conf
```

添加下面几行：

ubuntu01: 4B 8GB 内存 4核

node01: 3B+ 1GB 内存 4核

node02: 3B+ 1GB 内存 4核

```c
spark.master                       yarn
spark.executor.instances           3
spark.driver.memory                1024m
spark.yarn.am.memory               1024m
spark.executor.memory              1024m
spark.executor.cores               2

spark.eventLog.enabled             true
spark.eventLog.dir                 hdfs://ubuntu01:9000/spark-logs
spark.history.provider             org.apache.spark.deploy.history.FsHistoryProvider
spark.history.fs.logDirectory      hdfs://ubuntu01:9000/spark-logs
spark.history.fs.update.interval   10s
spark.history.ui.port              19090
```

> num-executors/spark.executor.instances

参数说明：该参数用于设置Spark作业总共要用多少个Executor进程来执行。Driver在向YARN集群管理器申请资源时，YARN集群管理器会尽可能按照你的设置来在集群的各个工作node上，启动相应数量的Executor进程。这个参数非常之重要，如果不设置的话，默认只会给你启动少量的Executor进程，此时你的Spark作业的运行速度是非常慢的。

参数调优建议：每个Spark作业的运行一般设置50~100个左右的Executor进程比较合适，设置太少或太多的Executor进程都不好。设置的太少，无法充分利用集群资源；设置的太多的话，大部分队列可能无法给予充分的资源。

>executor-cores/spark.executor.cores

参数说明：该参数用于设置每个Executor进程的CPU core数量。这个参数决定了每个Executor进程并行执行task线程的能力。因为每个CPU core同一时间只能执行一个task线程，因此每个Executor进程的CPU core数量越多，越能够快速地执行完分配给自己的所有task线程。

参数调优建议：Executor的CPU core数量设置为2~4个较为合适。同样得根据不同部门的资源队列来定，可以看看自己的资源队列的最大CPU core限制是多少，再依据设置的Executor数量，来决定每个Executor进程可以分配到几个CPU core。同样建议，如果是跟他人共享这个队列，那么num-executors * executor-cores不要超过队列总CPU core的1/3~1/2左右比较合适，也是避免影响其他同学的作业运行。

--------------------------------------------------------------

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3j7vejcy5wo0.png)

当在集群上执行应用时，

job => 切分成  => stages, stage  => 切分成  => task

executor有 spark.executor.cores / spark.task.cpus execution 个执行槽，

这里有个例子：

集群有12个node运行Yarn的NodeManager，

每个node有64G内存和32的cpu核，

每个node可以启动2个executor

  =>  每个executor的使用26G内存，剩下的内用系统和别的服务使用，

  =>  每个executor有12个cpu核用于执行, 这样整个集群有 12 node * 2 executor * 12个cpu核 / 1 core = 288 个task执行槽，
  
  这意味着spark集群可以同时跑288个task
  
  整个集群用户缓存数据的内存有 0.9 (spark.storage.safetyFraction) * 0.6 (spark.storage.memoryFraction) * 12 node * 2 executors * 26 GB = 336.96 GB.

在HDFS上创建日志目录。

```sh
cd
hdfs dfs -mkdir /spark-logs
```

启动Spark历史服务器。

```sh
start-history-server.sh
```

Spark历史服务器界面可以通过 <http://ubuntu01:18080> 访问

![spark历史服务器](https://github.com/YutingYao/pi-cluster/blob/master/pictures/spark-history-ui.png)

运行示例作业（计算pi）

```sh
spark-submit --deploy-mode client --class org.apache.spark.examples.SparkPi --master spark://ubuntu01:7077  $SPARK_HOME/examples/jars/spark-examples_2.12-3.2.0.jar 7  2>&1 | grep "Pi is roughly"
```

运行后，根据在Shell中得到输出的结果地址查看

复制结果地址到浏览器，点击查看Logs，再点击stdout，即可查看结果

## spark-submit 的具体用法

 | 参数名 | 参数说明 | 
 |  ----  | ----  |
 | --master | master 的地址，提交任务到哪里执行，例如 spark://host:port,  yarn,  local | 
 | --deploy-mode |  在本地 (client) 启动 driver 或在 cluster 上启动，默认是 client | 
 | --class | 应用程序的主类，仅针对 java 或 scala 应用 | 
 | --name |  应用程序的名称 | 
 | --jars |  用逗号分隔的本地 jar 包，设置后，这些 jar 将包含在 driver 和 executor 的 classpath 下 | 
 | --packages | 包含在driver 和executor 的 classpath 中的 jar 的 maven 坐标 | 
 | --exclude-packages | 为了避免冲突 而指定不包含的 package | 
 | --repositories | 远程 repository | 
 | --properties-file | 加载的配置文件，默认为 conf/spark-defaults.conf | 
 | --driver-memory |  Driver内存，默认 1G | 
 | --driver-java-options |  传给 driver 的额外的 Java 选项 | 
 | --driver-library-path | 传给 driver 的额外的库路径 | 
 | --driver-class-path | 传给 driver 的额外的类路径 | 
 | --driver-cores | Driver 的核数，默认是1。在 yarn 或者 standalone 下使用 | 
 | --executor-memory | 每个 executor 的内存，默认是1G | 
 | --total-executor-cores | 所有 executor 总共的核数。仅仅在 mesos 或者 standalone 下使用 | 
 | --num-executors | 启动的 executor 数量。默认为2。在 yarn 下使用。设定在30~100个之间为最佳。需要计算。 | 
 | --executor-core | 每个 executor 的核数。在yarn或者standalone下使用 | 


--master标记指定要连接的集群URL

 | 值 | 描述 | 
 |  ----  | ----  |
 | spark://host:port | 连接到指定端口的Spark独立集群上。默认情况下Spark独立主节点使用7007端口 | 
 | mesos://host:port | 连接到指定端口的Mesos集群上。默认情况下Mesos主节点监听5050端口 | 
 | yarn | 连接到一个Yarn集群，当在Yarn上运行时，需要设置环境变量HADOOP_CONF_DIR指向Hadoop配置目录，以获取集群信息 | 
 | local | 运行本地模式，使用单核 | 
 | local[N] | 运行本地模式，使用N个核心 | 
 | local[*] | 运行本地模式，使用尽可能多的核心 | 

 具体示例：

 ```s
 # 本地运行程序，用8核
./bin/spark-submit \
  --class org.apache.spark.examples.SparkPi \
  --master local[8] \
  /path/to/examples.jar \
  100

#提交在集群，默认是client模式
./bin/spark-submit \
  --class org.apache.spark.examples.SparkPi \
  --master spark://207.184.161.138:7077 \
  --executor-memory 20G \
  --total-executor-cores 100 \
  /path/to/examples.jar \
  1000

# 集群上运行，并设置supervise。只能独立模式和mesos下有效。当driver失败时，自动重试。

./bin/spark-submit\
  --class org.apache.spark.examples.SparkPi \
  --master spark://207.184.161.138:7077 \
  --deploy-mode cluster \
  --supervise \
  --executor-memory 20G \
  --total-executor-cores 100 \
  /path/to/examples.jar \
  1000

#yarn集群上运行
./bin/spark-submit \
  --class org.apache.spark.examples.SparkPi \
  --master yarn \
  --deploy-mode cluster \  # can be client forclient mode
  --executor-memory 20G \
  --num-executors 50 \
  /path/to/examples.jar \
  100
 ```

### 启动bin目录下的spark-shell

```sh
bin/spark-shell
```

即会出现spark scala的命令行执行环境：

在Shell中输入如下命令启动进入spark-shell：

```sh
cd /opt/spark
bin/spark-shell --master spark://ubuntu01:7077
```

可以在spark-shell中输入如下代码进行测试：

```sh
scala> val textFile = sc.textFile("hdfs://master:9000/README.md")
textFile: org.apache.spark.rdd.RDD[String] = hdfs://master:9000/README.md MapPartitionsRDD[1] at textFile at <console>:24
scala> textFile.count()
res0: Long = 99                                                                 
scala> textFile.first()
res1: String = # Apache Spark
```

用户在独立集群管理Web界面查看应用的运行情况，可以浏览器中输入地址进行查看(<http://ubuntu01:8080/>)

## pyspark独立应用程序编程

接着我们通过一个简单的应用程序来演示如何通过 Spark API 编写一个独立应用程序。

使用 Python进行spark编程比Java和Scala简单得多。

在进行Python编程前，请先确定是否已经.bashrc中添加PYTHONPATH环境变量。

接下来即可进行Python编程.

这里在新建一个test.py文件,并在test.py添加代码

```sh
cd ~
vim test.py
```

在test.py中添加如下代码,：

```py
from pyspark import SparkContext
sc = SparkContext( 'local', 'test')
logFile = "file:///opt/spark/README.md"
logData = sc.textFile(logFile, 2).cache()
numAs = logData.filter(lambda line: 'a' in line).count()
numBs = logData.filter(lambda line: 'b' in line).count()
print('Lines with a: %s, Lines with b: %s' % (numAs, numBs))
```

保存代码后，通过如下命令执行：

```sh
python3 ~/test.py
```

最终得到的结果如下：

Lines with a: 62, Lines with b: 30