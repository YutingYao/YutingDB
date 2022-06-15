# 几个重要的目录

```S
/opt/hadoop
```

## echo $HADOOP_CONF_DIR

```S
/opt/hadoop/etc/hadoop
```


capacity-scheduler.xml            kms-log4j.properties
configuration.xsl                 kms-site.xml
container-executor.cfg            log4j.properties
core-site.xml                     mapred-env.cmd
hadoop-env.cmd                    mapred-env.sh
hadoop-env.sh                     mapred-queues.xml.template
hadoop-metrics2.properties        mapred-site.xml
hadoop-policy.xml                 shellprofile.d
hadoop-user-functions.sh.example  ssl-client.xml.example
hdfs-rbf-site.xml                 ssl-server.xml.example
hdfs-site.xml                     user_ec_policies.xml.template
httpfs-env.sh                     workers
httpfs-log4j.properties           yarn-env.cmd
httpfs-site.xml                   yarn-env.sh
kms-acls.xml                      yarnservice-log4j.properties
kms-env.sh                        yarn-site.xml

## /opt/hadoop/sbin

```S
/opt/hadoop/sbin
```

distribute-exclude.sh    start-all.sh         stop-balancer.sh
FederationStateStore     start-balancer.sh    stop-dfs.cmd
hadoop-daemon.sh         start-dfs.cmd        stop-dfs.sh
hadoop-daemons.sh        start-dfs.sh         stop-secure-dns.sh
httpfs.sh                start-secure-dns.sh  stop-yarn.cmd
kms.sh                   start-yarn.cmd       stop-yarn.sh
mr-jobhistory-daemon.sh  start-yarn.sh        workers.sh
refresh-namenodes.sh     stop-all.cmd         yarn-daemon.sh
start-all.cmd            stop-all.sh          yarn-daemons.sh

# 安装前准备 - java环境

```sh
sudo apt-get install openjdk-8-jdk
```

可以顺便安装一下这些

```s
sudo apt-get update
sudo apt-get install git
sudo apt-get install openjdk-8-jdk
sudo apt-get install npm
sudo apt-get install libfontconfig
sudo apt-get install r-base-dev
sudo apt-get install r-cran-evaluate
```

debian：pi 3b+ java环境

```S
/usr/lib/jvm/java-8-openjdk-armhf
```

ubuntu：pi 4b java环境

```S
/usr/lib/jvm/java-8-openjdk-arm64
```

# hadoop配置- sudo su

因为ubuntu的ssh只有在root下面才能成，所以hadoop最好安在root下面

下载后解压到 opt：

```sh
sudo tar -xvf hadoop-3.3.1.tar.gz -C /opt
```

* x : 从 tar 包中把文件提取出来
* z : 表示 tar 包是被 gzip 压缩过的，所以解压时需要用 gunzip 解压
* v : 显示详细信息
* f xxx.tar.gz :  指定被处理的文件是 xxx.tar.gz

mv是移动目录的意思：

```sh
sudo mv hadoop-3.3.1 hadoop
```

```s
sudo vim ~/.bashrc
```

debian：pi 3b+ java环境

```S
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-armhf/
export HADOOP_HOME=/opt/hadoop
export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export LD_LIBRARY_PATH=$HADOOP_HOME/lib/native:$LD_LIBRARY_PATH
```

ubuntu：pi 4b java环境

```S
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-arm64/
export HADOOP_HOME=/opt/hadoop
export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export LD_LIBRARY_PATH=$HADOOP_HOME/lib/native:$LD_LIBRARY_PATH
```

```s
sudo chown yaoyuting03:yaoyuting03 -R /opt/hadoop 
sudo chown pi:pi -R /opt/hadoop 
sudo chown root:root -R /opt/hadoop 
```

使配置的环境变量生效：

```sh
source ~/.bashrc
```

验证 Hadoop 安装

```sh
cd && hadoop version | grep Hadoop
```

## hadoop-env.sh

```s
sudo vim /opt/hadoop/etc/hadoop/hadoop-env.sh
```

*添加（在文件顶部插入）：

```sh
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-arm64/
```

```s
export HDFS_NAMENODE_USER=root
export HDFS_DATANODE_USER=root
export HDFS_SECONDARYNAMENODE_USER=root
export YARN_RESOURCEMANAGER_USER=root
export YARN_NODEMANAGER_USER=root
```

如果需要复制到node上：

```s
scp -r  /opt/hadoop/etc/hadoop/hadoop-env.sh root@node01:/opt/hadoop/etc/hadoop/hadoop-env.sh
scp -r  /opt/hadoop/etc/hadoop/hadoop-env.sh root@node02:/opt/hadoop/etc/hadoop/hadoop-env.sh
```

并需要修改java环境，修改arm64为armhf

## yarn-env.sh

sudo vim /opt/hadoop/etc/hadoop/yarn-env.sh

*添加（在文件顶部插入）：

```sh
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-arm64/
```

并需要修改java环境，修改arm64为armhf

## Slave节点

```sh
sudo vim workers 
```

node01
node02

## core-site.xml文件


```sh
sudo vim /opt/hadoop/etc/hadoop/core-site.xml
```

这个是hadoop的核心配置，这里需要配置两属性，

fs.default.name 配置hadoop的HDFS系统命令，位置为主机的9000端口，

hadoop.tmp.dir 配置haddop的tmp目录的根位置。


```xml
  <property>
    <name>fs.defaultFS</name>
    <value>hdfs://ubuntu01:9000</value>
  </property>
```

```xml
<property>
        <name>hadoop.tmp.dir</name>
        <value>file:/opt/hadoop/tmp</value>
        <description>Abase for other tmporary directries.</description>
</property>
```

```xml
<property>
        <name>io.file.buffer.size</name>
        <value>131072</value>
</property>
```

## hdfs-site.xml文件

```sh
sudo vim /opt/hadoop/etc/hadoop/hdfs-site.xml
```

对于Hadoop的分布式文件系统HDFS而言，

一般都是采用冗余存储，冗余因子通常为3，

也就是说，一份数据保存三份副本。

但是，本教程只有一个Slave节点作为数据节点，

即集群中只有一个数据节点，数据只能保存一份，

所以 ，dfs.replication的值还是设置为 1。

HDFS主要的配置文件， dfs.http.address配置了hdfs的http的访问位置；

dfs.replication 配置文件的副本，一般不大于从机个数。

修改文件结尾为：

```xml
<property>
<name>dfs.replication</name>
<value>2</value>
</property>
```

```xml
<property>
<name>dfs.permissions</name>
<value>false</value>
</property>
```

```xml
<property>
        <name>dfs.namenode.secondary.http-address</name>
        <value>ubuntu01:9000</value>
</property>

<property>
        <name>dfs.namenode.name.dir</name>
        <value>file:/opt/hadoop/tmp/dfs/name</value>
</property>

<property>
        <name>dfs.datanode.data.dir</name>
        <value>file:/opt/hadoop/tmp/dfs/data</value>
</property>

<property>
        <name>dfs.webhdfs.enabled</name>
        <value>true</value>
</property>
```

## mapred-site.xml

mapred.map.tasks和mapred.reduce.tasks 分别为map和reduce 的任务数。

注意：默认情况下是没有mapred-site.xml文件的，
但有mapred-site.xml.template文件，
运行命令:

```s
cp mapred-site.xml.template mapred-site.xml
```

拷贝一份即可。

```sh
sudo vim /opt/hadoop/etc/hadoop/mapred-site.xml
```

修改文件结尾为：

```xml
  <property>
    <name>mapreduce.framework.name</name>
    <value>yarn</value>
  </property>
```

同时指定hadoop历史服务器hsitoryserver

我们可以通过historyserver查看mapreduce的作业记录，

```xml
<property>
        <name>mapreduce.jobhistory.address</name>
        <value>ubuntu01:10020</value>
</property>

<property>
        <name>mapreduce.jobhistory.webapp.address</name>
        <value>ubuntu01:19888</value>
</property>
```

```xml
        <property>
                <name>yarn.app.mapreduce.am.env</name>
                <value>HADOOP_MAPRED_HOME=/opt/hadoop</value>
        </property>
        <property>
                <name>mapreduce.map.env</name>
                <value>HADOOP_MAPRED_HOME=/opt/hadoop</value>
        </property>
        <property>
                <name>mapreduce.reduce.env</name>
                <value>HADOOP_MAPRED_HOME=/opt/hadoop</value>
        </property> 
```

## yarn-site.xml

yarn框架的配置，主要是一些任务的启动位置

```sh
sudo vim /opt/hadoop/etc/hadoop/yarn-site.xml
```

修改文件结尾为：

MapReduce程序的运行方式：shuffle洗牌

```xml
<property>
    <name>yarn.nodemanager.aux-services</name>
    <value>mapreduce_shuffle</value>
</property>
<property>
    <name>yarn.nodemanager.auxservices.mapreduce.shuffle.class</name>  
    <value>org.apache.hadoop.mapred.ShuffleHandler</value>
</property>
```

```xml
<property>
<name>yarn.resourcemanager.hostname</name>
<value>ubuntu01</value>
</property>
```

```xml
<proetry>
        <name>yarn.resoucemanager.address</name>
        <value>ubuntu01:8032</value>
</proetry>
<proetry>
        <name>yarn.resourcemanager.scheduler.address</name>
        <value>ubuntu01:8030</value>
</proetry>
<proetry>
        <name>yarn.resourcemanager.resource-tracker.address</name>
        <value>ubuntu01:8031</value>
</proetry>

<proetry>
        <name>yarn.resourcemanager.admin.address</name>
        <value>ubuntu01:8033</value>
</proetry>

<proetry>
        <name>yarn.resourcemanager.webapp.address</name>
        <value>ubuntu01:8088</value>
</proetry>
```




## /opt/hadoop/tmp

```S
/opt/hadoop/tmp
```

```s
sudo mkdir -p /opt/hadoop/tmp/dfs/data
sudo mkdir -p /opt/hadoop/tmp/dfs/name
```

file:/opt/hadoop/tmp
file:/opt/hadoop/tmp/dfs/name
file:/opt/hadoop/tmp/dfs/data

```s
sudo chown root:root -R /opt/hadoop/tmp
sudo chown yaoyuting03:yaoyuting03 -R /opt/hadoop/tmp
```

```s
hdfs name -format
hdfs data -format
```

# master安装结束以后,copy

## 复制

```s
scp -r /opt/hadoop root@node01:/opt/
scp -r /opt/hadoop root@node02:/opt/
```

需要修改node01和node02的配置：java环境


debian：pi 3b+ java环境

```S
/usr/lib/jvm/java-8-openjdk-armhf
```

ubuntu：pi 4b java环境

```S
/usr/lib/jvm/java-8-openjdk-arm64
```

## 重复安装步骤

```s
sudo vim ~/.bashrc
```

debian：pi 3b+ java环境

```S
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-armhf/
export HADOOP_HOME=/opt/hadoop
export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export LD_LIBRARY_PATH=$HADOOP_HOME/lib/native:$LD_LIBRARY_PATH
```

ubuntu：pi 4b java环境

```S
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-arm64/
export HADOOP_HOME=/opt/hadoop
export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export LD_LIBRARY_PATH=$HADOOP_HOME/lib/native:$LD_LIBRARY_PATH
```

```s
sudo chown yaoyuting03:yaoyuting03 -R /opt/hadoop 
sudo chown pi:pi -R /opt/hadoop 
sudo chown root:root -R /opt/hadoop 
```

使配置的环境变量生效：

```sh
source ~/.bashrc
```

验证 Hadoop 安装

```sh
cd && hadoop version | grep Hadoop
```




# 启动与关闭hadoop

```s
cd /opt/hadoop/
sbin/start-all.sh
```

```s
cd /opt/hadoop/sbin/
start-dfs.sh
start-yarn.sh
mapred --daemon start
```

```s
stop-yarn.sh
stop-dfs.sh
mapred --daemon stop
```

# 查看进程

```s
jps
```

在node上：

- 2210 DataNode
- 2601 Jps
- 2316 NodeManager

在master上：

- 8199 JobHistoryServer
- 6235 ResourceManager
- 5790 NameNode
- 8366 Jps

```s
hdfs dfsadmin -report
```

查看数据节点是否正常启动，

如果屏幕信息中的“Live datanodes”不为 0 ，则说明集群启动成功

```s
Configured Capacity: 60478431232 (56.32 GB)
Present Capacity: 36467687424 (33.96 GB)
DFS Remaining: 36467630080 (33.96 GB)
DFS Used: 57344 (56 KB)
DFS Used%: 0.00%
Replicated Blocks:
	Under replicated blocks: 0
	Blocks with corrupt replicas: 0
	Missing blocks: 0
	Missing blocks (with replication factor 1): 0
	Low redundancy blocks with highest priority to recover: 0
	Pending deletion blocks: 0
Erasure Coded Block Groups: 
	Low redundancy block groups: 0
	Block groups with corrupt internal blocks: 0
	Missing block groups: 0
	Low redundancy blocks with highest priority to recover: 0
	Pending deletion blocks: 0

-------------------------------------------------
Live datanodes (2):

Name: 192.168.31.219:9866 (node01)
Hostname: raspberrypi
Decommission Status : Normal
Configured Capacity: 30348288000 (28.26 GB)
DFS Used: 28672 (28 KB)
Non DFS Used: 10677399552 (9.94 GB)
DFS Remaining: 18340032512 (17.08 GB)
DFS Used%: 0.00%
DFS Remaining%: 60.43%
Configured Cache Capacity: 0 (0 B)
Cache Used: 0 (0 B)
Cache Remaining: 0 (0 B)
Cache Used%: 100.00%
Cache Remaining%: 0.00%
Xceivers: 0
Last contact: Fri Oct 22 20:49:39 CST 2021
Last Block Report: Fri Oct 22 20:33:52 CST 2021
Num of Blocks: 0


Name: 192.168.31.6:9866 (node02)
Hostname: raspberrypi
Decommission Status : Normal
Configured Capacity: 30130143232 (28.06 GB)
DFS Used: 28672 (28 KB)
Non DFS Used: 10680582144 (9.95 GB)
DFS Remaining: 18127597568 (16.88 GB)
DFS Used%: 0.00%
DFS Remaining%: 60.16%
Configured Cache Capacity: 0 (0 B)
Cache Used: 0 (0 B)
Cache Remaining: 0 (0 B)
Cache Used%: 100.00%
Cache Remaining%: 0.00%
Xceivers: 0
Last contact: Fri Oct 22 20:49:37 CST 2021
Last Block Report: Fri Oct 22 20:33:52 CST 2021
Num of Blocks: 0
```

http://ubuntu01:8088/cluster

至此，Hadoop分布式集群搭建成功。