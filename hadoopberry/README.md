Project to Design a Hadoop/Spark [Raspberry Pi 4 Cluster](https://github.com/YutingYao/pi-cluster) for Distributed Computing.

An efficient quick-start tool to build [a Raspberry Pi (or Debian-based) Cluster](https://github.com/YutingYao/RaspPi-Cluster) with popular ecosystem like Hadoop, Spark

Repo for instructions on setting up a micro compute cluster with the [NVidia Jetson Nano boards](https://github.com/YutingYao/JetsonCluster) and potentially Ansible playbooks for configuration and setup.

Setting up a [K3s Kubernetes](https://github.com/YutingYao/jetsonnano-k3s-gpu) cluster on my Nvidia Jetson Nano

Cluster made out of [Nvidia Jetson Nano's](https://github.com/YutingYao/NanoCluster)

## hadoop

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


## ssh
```sh
```

## apark
```sh
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

