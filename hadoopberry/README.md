Project to Design a Hadoop/Spark [Raspberry Pi 4 Cluster](https://github.com/YutingYao/pi-cluster) for Distributed Computing.

An efficient quick-start tool to build [a Raspberry Pi (or Debian-based) Cluster](https://github.com/YutingYao/RaspPi-Cluster) with popular ecosystem like Hadoop, Spark

Repo for instructions on setting up a micro compute cluster with the [NVidia Jetson Nano boards](https://github.com/YutingYao/JetsonCluster) and potentially Ansible playbooks for configuration and setup.

Setting up a [K3s Kubernetes](https://github.com/YutingYao/jetsonnano-k3s-gpu) cluster on my Nvidia Jetson Nano

Cluster made out of [Nvidia Jetson Nano's](https://github.com/YutingYao/NanoCluster)

# 1.烧录系统
三步走：下载树莓派ubuntu镜像-[Ubuntu Desktop 21.04](https://ubuntu.com/download/raspberry-pi/thank-you?version=21.04&architecture=desktop-arm64+raspi)、[SD卡格式化](https://www.sdcard.org/downloads/formatter/sd-memory-card-formatter-for-windows-download/)和[烧录系统](https://www.balena.io/etcher/)

ubuntu镜像使用桌面版本

安装[拼音输入法](https://pinyin.sogou.com/linux/?r=pinyin)

安装远程控制

```sh
sudo apt-get install tightvncserver
sudo tightvncserver
```

# 2.安装大数据分析软件

## 2.0 docker环境

安装docker

更新包索引并安装封装，以便在 HTTPS 上使用存储库

```sh
sudo apt-get update
 sudo apt-get install \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg \
    lsb-release
```

添加多克的官方 GPG 密钥：

```sh
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
```

curl -fsSL https://mirrors.aliyun.com/docker-ce/linux/ubuntu/gpg | apt-key add -

sudo apt-key fingerprint 0EBFCD88

使用以下命令设置稳定存储库。要添加夜间或测试存储库，在下面的命令中的单词后添加单词或（或两者兼有）。
nightly test stable

```sh
 echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null、
```

更新apt包索引，安装Docker Engine和container最新版本，或者继续下一步安装特定版本:
```sh
sudo apt-get update
 sudo apt-get install docker-ce docker-ce-cli containerd.io
```

通过运行hello-world镜像来验证Docker引擎是否正确安装。

```sh
sudo docker run hello-world
```

## 2.1 zeppelin
[带有所有解释器的二进制包](https://dlcdn.apache.org/zeppelin/zeppelin-0.10.0/zeppelin-0.10.0-bin-all.tgz)

Supported Interpreters:
* [Spark](https://www.apache.org/dyn/closer.lua/spark/spark-3.1.2/spark-3.1.2-bin-hadoop3.2.tgz)
* [Hive](http://www.apache.org/dyn/closer.cgi/hive/)
* JDBC
* Python
* HDFS
* [Hbase](https://hbase.apache.org/downloads.html)
* Elasticsearch
* Markdown
* Shell
* Flink
* Geode 
* [PostgreSQL](https://www.postgresql.org/download/linux/ubuntu/)
  
  Ubuntu默认包含PostgreSQL。要在Ubuntu上安装PostgreSQL，请使用apt get（或其他apt驱动）命令：

  ```s
  apt-get install postgresql-12
  ```



使用此命令在容器中启动Apache Zeppelin。

```s
docker run -p 8080:8080 --rm --name zeppelin apache/zeppelin:0.10.0
```

要持久保存日志和笔记本目录，请使用docker容器的卷选项。您还可以将体积用于Spark和Flink二进制分布。

```s
docker run -u $(id -u) -p 8080:8080 --rm -v $PWD/logs:/logs -v $PWD/notebook:/notebook \
  -v /usr/lib/spark-2.4.7:/opt/spark -v /usr/lib/flink-1.12.2:/opt/flink \
  -e FLINK_HOME=/opt/flink -e SPARK_HOME=/opt/spark \
  -e ZEPPELIN_LOG_DIR='/logs' -e ZEPPELIN_NOTEBOOK_DIR='/notebook' --name zeppelin apache/zeppelin:0.10.0
```

## hadoop

[树莓派的Hadoop 3集群上的分布式TensorFlow](https://oliver-hu.medium.com/distributed-tensorflow-on-raspberry-pis-hadoop-3-cluster-603a164bb896)

在每个节点上安装 Java 8，使其成为每个节点的默认 Java。

```sh
pi@piX:~$ sudo apt-get install openjdk-8-jdk
pi@piX:~$ sudo update-alternatives --config java    // Select number corresponding to Java 8
pi@piX:~$ sudo update-alternatives --config javac   // Select number corresponding to Java 8
```

下载 Hadoop，解压并授予 pi 所有权。

```sh
pi@pi1:~$ cd && wget https://www-us.apache.org/dist/hadoop/common/hadoop-3.2.1/hadoop-3.2.1.tar.gz
pi@pi1:~$ sudo tar -xvf hadoop-3.2.1.tar.gz -C /opt/
pi@pi1:~$ rm hadoop-3.2.1.tar.gz && cd /opt
pi@pi1:/opt$ sudo mv hadoop-3.2.1 hadoop
pi@pi1:/opt$ sudo chown pi:pi -R /opt/hadoop
```

配置 Hadoop 环境变量。

```sh
pi@pi1:~$ sudo mousepad ~/.bashrc
```

*添加（在文件顶部插入）：

```sh
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-armhf/
export HADOOP_HOME=/opt/hadoop
export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export LD_LIBRARY_PATH=$HADOOP_HOME/lib/native:$LD_LIBRARY_PATH
```

为 Hadoop 环境初始化 JAVA_HOME。

```sh
pi@pi1:~$ sudo mousepad /opt/hadoop/etc/hadoop/hadoop-env.sh
```

*添加（在文件顶部插入）：

```sh
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-armhf/
```

验证 Hadoop 安装。

```sh
pi@pi1:~$ source ~/.bashrc
pi@pi1:~$ cd && hadoop version | grep Hadoop
Hadoop 3.2.1
```

设置 Hadoop 集群

```sh
pi@pi1:~$ sudo mousepad /opt/hadoop/etc/hadoop/core-site.xml
```

修改文件结尾为：

```xml
<configuration>
  <property>
    <name>fs.defaultFS</name>
    <value>hdfs://pi1:9000</value>
  </property>
</configuration>
```

```sh
pi@pi1:~$ sudo mousepad /opt/hadoop/etc/hadoop/hdfs-site.xml
```

修改文件结尾为：

```xml
<configuration>
  <property>
    <name>dfs.datanode.data.dir</name>
    <value>file:///opt/hadoop_tmp/hdfs/datanode</value>
  </property>
  <property>
    <name>dfs.namenode.name.dir</name>
    <value>file:///opt/hadoop_tmp/hdfs/namenode</value>
  </property>
  <property>
    <name>dfs.replication</name>
    <value>1</value>
  </property>
</configuration> 
```

```sh
pi@pi1:~$ sudo mousepad /opt/hadoop/etc/hadoop/mapred-site.xml
```

修改文件结尾为：

```xml
<configuration>
  <property>
    <name>mapreduce.framework.name</name>
    <value>yarn</value>
  </property>
</configuration>
```

```sh
pi@pi1:~$ sudo mousepad /opt/hadoop/etc/hadoop/yarn-site.xml
```

修改文件结尾为：

```xml
<configuration>
  <property>
    <name>yarn.nodemanager.aux-services</name>
    <value>mapreduce_shuffle</value>
  </property>
  <property>
    <name>yarn.nodemanager.auxservices.mapreduce.shuffle.class</name>  
    <value>org.apache.hadoop.mapred.ShuffleHandler</value>
  </property>
</configuration> 
```

创建 Datanode 和 Namenode 目录。

```sh
pi@pi1:~$ sudo mkdir -p /opt/hadoop_tmp/hdfs/datanode
pi@pi1:~$ sudo mkdir -p /opt/hadoop_tmp/hdfs/namenode
pi@pi1:~$ sudo chown pi:pi -R /opt/hadoop_tmp
```

格式化HDFS。

```sh
pi@pi1:~$ hdfs namenode -format -force
```

启动HDFS，验证功能。

```sh
pi@pi1:~$ start-dfs && start-yarn.sh
```

使用jps命令验证设置。

```sh
pi@pi1:~$ jps
```

创建临时目录以测试文件系统：

```sh
pi@pi1:~$ hadoop fs -mkdir /tmp
pi@pi1:~$ hadoop fs -ls /
```

使用以下命令停止单节点群集：

```sh
pi@pi1:~$ stop-dfs && stop-yarn.sh
```

静默警告（由于使用了32位Hadoop构建和64位操作系统）
修改Hadoop环境配置:

```sh
pi@pi1:~$ sudo mousepad /opt/hadoop/etc/hadoop/hadoop-env.sh
```

把这个：

```sh
# export HADOOP_OPTS="-Djava.net.preferIPv4Stack=true"
```

改变为这个：

```sh
export HADOOP_OPTS="-XX:-PrintWarnings –Djava.net.preferIPv4Stack=true"
```

现在在~/.bashrc中，添加到底部：

```sh
pi@pi1:~$ sudo mousepad ~/.bashrc
```

```sh
export HADOOP_HOME_WARN_SUPPRESS=1
export HADOOP_ROOT_LOGGER="WARN,DRFA" 
```

来源~/.bashrc：

```sh
pi@pi1:~$ source ~/.bashrc
```

将.bashrc复制到群集中的其他节点：

```sh
pi@pi1:~$ clusterscp ~/.bashrc
```

创建Hadoop群集目录（多节点设置）。

```sh
pi@pi1:~$ clustercmd sudo mkdir -p /opt/hadoop_tmp/hdfs
pi@pi1:~$ clustercmd sudo chown pi:pi –R /opt/hadoop_tmp
pi@pi1:~$ clustercmd sudo mkdir -p /opt/hadoop
pi@pi1:~$ clustercmd sudo chown pi:pi /opt/hadoop
```

将Hadoop文件复制到其他节点。

```sh
pi@pi1:~$ for pi in $(otherpis); do rsync -avxP $HADOOP_HOME $pi:/opt; done
```

验证是否在其他节点上安装：

```sh
pi@pi1:~$ clustercmd hadoop version | grep Hadoop
Hadoop 3.2.1
Hadoop 3.2.1
Hadoop 3.2.1
Hadoop 3.2.1
```

修改用于群集设置的Hadoop配置文件。

```sh
pi@pi1:~$ sudo mousepad /opt/hadoop/etc/hadoop/core-site.xml
```


修改文件结尾为：

```xml
原来的：
<configuration>
  <property>
    <name>fs.defaultFS</name>
    <value>hdfs://pi1:9000</value>
  </property>
</configuration>
现在的：
<configuration>
  <property>
    <name>fs.default.name</name>
    <value>hdfs://pi1:9000</value>
  </property>
</configuration>
```

```sh
pi@pi1:~$ sudo mousepad /opt/hadoop/etc/hadoop/hdfs-site.xml
```

修改文件结尾为：

```xml
原来的：
<configuration>
  <property>
    <name>dfs.datanode.data.dir</name>
    <value>file:///opt/hadoop_tmp/hdfs/datanode</value>
  </property>
  <property>
    <name>dfs.namenode.name.dir</name>
    <value>file:///opt/hadoop_tmp/hdfs/namenode</value>
  </property>
  <property>
    <name>dfs.replication</name>
    <value>1</value>
  </property>
</configuration> 
现在的：
<configuration>
  <property>
    <name>dfs.datanode.data.dir</name>
    <value>/opt/hadoop_tmp/hdfs/datanode</value>
  </property>
  <property>
    <name>dfs.namenode.name.dir</name>
    <value>/opt/hadoop_tmp/hdfs/namenode</value>
  </property>
  <property>
    <name>dfs.replication</name>
    <value>4</value>
  </property>
</configuration> 
```

```sh
pi@pi1:~$ sudo mousepad /opt/hadoop/etc/hadoop/mapred-site.xml
```

修改文件结尾为：

```xml
原来的：
<configuration>
  <property>
    <name>mapreduce.framework.name</name>
    <value>yarn</value>
  </property>
</configuration>
现在在：
<configuration>
  <property>
    <name>mapreduce.framework.name</name>
      <value>yarn</value>
  </property>
  <property>
    <name>yarn.app.mapreduce.am.env</name>
      <value>HADOOP_MAPRED_HOME=$HADOOP_HOME</value>
  </property>
  <property>
    <name>mapreduce.map.env</name>
      <value>HADOOP_MAPRED_HOME=$HADOOP_HOME</value>
  </property>
  <property>
    <name>mapreduce.reduce.env</name>
      <value>HADOOP_MAPRED_HOME=$HADOOP_HOME</value>
  </property>
  <property>
    <name>yarn.app.mapreduce.am.resource.mb</name>
      <value>512</value>
  </property>
  <property>
    <name>mapreduce.map.memory.mb</name>
      <value>256</value>
  </property>
  <property>
    <name>mapreduce.reduce.memory.mb</name>
      <value>256</value>
  </property>
</configuration>
```

```sh
pi@pi1:~$ sudo mousepad /opt/hadoop/etc/hadoop/yarn-site.xml
```

修改文件结尾为：

```xml
原来的：
<configuration>
  <property>
    <name>yarn.nodemanager.aux-services</name>
    <value>mapreduce_shuffle</value>
  </property>
  <property>
    <name>yarn.nodemanager.auxservices.mapreduce.shuffle.class</name>  
    <value>org.apache.hadoop.mapred.ShuffleHandler</value>
  </property>
</configuration> 
现在的：
<configuration>
  <property>
    <name>yarn.acl.enable</name>
    <value>0</value>
  </property>
  <property>
    <name>yarn.resourcemanager.hostname</name>
      <value>pi1</value>
  </property>
  <property>
    <name>yarn.nodemanager.aux-services</name>
      <value>mapreduce_shuffle</value>
  </property>
  <property>
    <name>yarn.nodemanager.resource.memory-mb</name>
      <value>1536</value>
  </property>
  <property>
    <name>yarn.scheduler.maximum-allocation-mb</name>
      <value>1536</value>
  </property>
  <property>
    <name>yarn.scheduler.minimum-allocation-mb</name>
      <value>128</value>
  </property>
  <property>
    <name>yarn.nodemanager.vmem-check-enabled</name>
      <value>false</value>
  </property>
</configuration> 
```

清理datanode和namenode目录。

```sh
pi@pi1:~$ clustercmd rm -rf /opt/hadoop_tmp/hdfs/datanode/*
pi@pi1:~$ clustercmd rm -rf /opt/hadoop_tmp/hdfs/namenode/*
```

创建/编辑 master amd worker files.

```sh
pi@pi1:~$ cd $HADOOP_HOME/etc/hadoop
pi@pi1:/opt/hadoop/etc/hadoop$ mousepad master
```

在文件中添加一行:

```sh
pi1
```

```sh
pi@pi1:/opt/hadoop/etc/hadoop$ mousepad workers
```

将其他pi主机名添加到文件：

```sh
pi2
pi3
pi4
```

编辑主机文件。

```sh
pi@pi1:~$ sudo mousepad /etc/hosts
```

删除该行（所有节点将具有相同的主机配置）：

```sh
127.0.1.1 pi1
```

将更新后的文件复制到其他集群节点:

```sh
pi@pi1:~$ clusterscp /etc/hosts
```

现在重新启动集群:

```sh
pi@pi1:~$ clusterreboot
```

格式化并启动多节点集群。

```sh
pi@pi1:~$ hdfs namenode -format -force
pi@pi1:~$ start-dfs.sh && start-yarn.sh
```

现在，由于我们已经在多节点集群上配置了Hadoop，所以当我们在主节点（pi1）上使用jps时，将只运行以下进程：

1. Namenode
2. SecondaryNamenode
3. ResourceManager
4. jps

下面的内容已经卸载到datanode，如果您ssh进入并执行jps，您将看到:

1. Datanode
2. NodeManager
3. jps



```sh
```


```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```


```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```


```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```


```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```

```sh
```


## ssh
```sh
```

## spark

[Spark SQL](https://spark.apache.org/sql/)是Apache Spark的模块，用于处理结构化数据。
[MLlib](https://spark.apache.org/mllib/)是Apache Spark的可扩展机器学习库。
[Spark Streaming](https://spark.apache.org/streaming/)使构建可伸缩的容错流应用程序变得容易。
[GraphX](https://spark.apache.org/graphx/)是Apache Spark用于图形和图形并行计算的API。
[Apache Spark示例](https://spark.apache.org/examples.html)

Apache Spark on [Google Colaboratory](https://mikestaszel.com/2018/03/07/apache-spark-on-google-colaboratory/)
使用 [Google Colaboratory](https://medium.com/@chiayinchen/%E4%BD%BF%E7%94%A8-google-colaboratory-%E8%B7%91-pyspark-625a07c75000) 跑 PySpark
如何在3分钟内安装PySpark和[Jupyter笔记本](https://www.sicara.ai/blog/2017-05-02-get-started-pyspark-jupyter-notebook-3-minutes)
使用[spark submit](https://spark.apache.org/docs/latest/submitting-applications.html)启动应用程序
[spark 案例](https://github.com/YutingYao/spark)


下载Spark，解包并授予pi所有权。

```sh
pi@pi1:~$ cd && wget https://www-us.apache.org/dist/spark/spark-2.4.4/spark-2.4.4-bin-hadoop-2.7.tgz
pi@pi1:~$ sudo tar -xvf spark-2.4.4-bin-hadoop-2.7.tgz -C /opt/
pi@pi1:~$ rm spark-2.4.4-bin-hadoop-2.7.tgz && cd /opt
pi@pi1:~$ sudo mv spark-2.4.4-bin-hadoop-2.7 spark
pi@pi1:~$ sudo chown pi:pi -R /opt/spark
```

配置Spark环境变量。

```sh
pi@pi1:~$ sudo mousepad ~/.bashrc
```

添加（在文件顶部插入）：

```sh
export SPARK_HOME=/opt/spark
export PATH=$PATH:$SPARK_HOME/bin
```

验证Spark安装。

```sh
pi@pi1:~$ source ~/.bashrc
pi@pi1:~$ spark-shell --version
Welcome to
      ____              __
     / __/__  ___ _____/ /__
    _\ \/ _ \/ _ `/ __/  '_/
   /___/ .__/\_,_/_/ /_/\_\   version 2.4.4
      /_/
                       
Using Scala version 2.11.12, OpenJDK Client VM, 1.8.0_212
Branch
Compiled by user  on 2019-08-27T21:21:38Z
Revision
Url
Type --help for more information.
```

配置Spark作业监控。

与Hadoop类似，Spark还提供监视您部署的作业的功能。但是，使用Spark，我们必须手动配置监控选项。

生成并修改Spark默认配置文件：

```sh
pi@pi1:~$ cd $SPARK_HOME/conf
pi@pi1:/opt/spark/conf$ sudo mv spark-defaults.conf.template spark-defaults.conf
pi@pi1:/opt/spark/conf$ mousepad spark-defaults.conf
```

Add the following lines:

```c
spark.master                       yarn
spark.executor.instances           4
spark.driver.memory                1024m
spark.yarn.am.memory               1024m
spark.executor.memory              1024m
spark.executor.cores               4

spark.eventLog.enabled             true
spark.eventLog.dir                 hdfs://pi1:9000/spark-logs
spark.history.provider             org.apache.spark.deploy.history.FsHistoryProvider
spark.history.fs.logDirectory      hdfs://pi1:9000/spark-logs
spark.history.fs.update.interval   10s
spark.history.ui.port              18080
```

在HDFS上创建日志目录。

```sh
pi@pi1:/opt/spark/conf$ cd
pi@pi1:~$ hdfs dfs -mkdir /spark-logs
```

启动Spark历史服务器。

```sh
pi@pi1:~$ $SPARK_HOME/sbin/start-history-server.sh
```

Spark历史服务器界面可以通过 http://pi1:18080 访问

![spark历史服务器](https://github.com/YutingYao/pi-cluster/blob/master/pictures/spark-history-ui.png)

运行示例作业（计算pi）

```sh
pi@pi1:~$ spark-submit --deploy-mode client --class org.apache.spark.examples.SparkPi $SPARK_HOME/examples/jars/spark-examples_2.11-2.4.4.jar 7
```


## setup
```sh
```

## hdfs
```sh
```

## node
```sh
```

## yarn
```sh
```

## conf
```sh
```

## bashrc
```sh
```

## host
```sh
```

## mapreduce
```sh
```

## ip
```sh
```

## pip
```sh
```

## pyspark
```sh
```

## java
```sh
```

## xml
```sh
```

## reboot
```sh
```

## server
```sh
```

## folder
```sh
```

## jupyter
```sh
```

## dir
```sh
```

## installed
```sh
```

## master
```sh
```

## password
```sh
```

## rsa
```sh
```

## distributed
```sh
```

## pdsh
```sh
```

## mb
```sh
```

## tar
```sh
```

## keys
```sh
```

## dir
```sh
```

