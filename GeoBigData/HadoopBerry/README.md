😁😎这个文档主要用来记录踩过的坑，成功版可参考本目录下的`其他markdown文档`。

<!-- vscode-markdown-toc -->
* [1.1. 三步走](#)
* [1.2. 安装输入法ibus 需要重启（但这一步,貌似不需要）](#ibus)
* [1.3. 安装远程控制（但这一步,目前没有成功）](#-1)
* [2.1. docker环境](#docker)
* [ hadoop](#hadoop)
	* [ 静默警告（由于使用了32位Hadoop构建和64位操作系统）](#32Hadoop64)
* [ scala](#scala)
* [ spark](#spark)
	* [2.6.1. 下载Spark，解包并授予pi所有权](#Sparkpi)
	* [ 配置Spark环境变量](#Spark)
	* [ 配置spark-env.sh](#spark-env.sh)
	* [ 配置slaves](#slaves)
	* [2.6.5. 启动Spark集群](#Spark-1)
		* [2.6.5.1. 启动Hadoop集群](#Hadoop)
		* [ 启动Spark集群](#Spark-1)
		* [ 关闭Spark集群](#Spark-1)
		* [2.6.5.4. 启动bin目录下的spark-shell](#binspark-shell)
	* [2.6.6. 为了方便可以修改Bash环境变量配置](#Bash)
	* [2.6.7. 配置Spark作业监控](#Spark-1)
* [2.7. 2.5 pyspark](#pyspark)
	* [2.7.1. 使用Spark](#Spark-1)
	* [2.7.2. 在Spark中采用本地模式启动pyspark](#Sparkpyspark)
	* [2.7.3. pyspark独立应用程序编程](#pyspark-1)
	* [2.7.4. Spark应用程序在集群中运行](#Spark-1)
		* [2.7.4.1. 启动Hadoop集群](#Hadoop-1)
		* [2.7.4.2. Hadoop YARN管理器](#HadoopYARN)
* [2.8. geospark](#geospark)
	* [2.8.1. geospark部署](#geospark-1)
	* [2.8.2. geospark示例](#geospark-1)
	* [2.8.3. 创建SpatialRDD(SRDD)](#SpatialRDDSRDD)
	* [2.8.4. 空间范围查询(Spatial Range Query)](#SpatialRangeQuery)
* [2.9. kafka](#kafka)
	* [2.9.1. Ubuntu 系统安装Kafka](#UbuntuKafka)
	* [2.9.2. 安装成功了Kafka](#Kafka)
	* [2.9.3. Spark准备工作（jar文件）](#Sparkjar)
	* [2.9.4. 编写Spark程序使用Kafka数据源](#SparkKafka)
* [2.10. flink](#flink)
* [2.11. PostgreSQL](#PostgreSQL)
* [2.12. MongoDB](#MongoDB)
	* [2.12.1. Mongo Spark Connector 连接器](#MongoSparkConnector)
		* [2.12.1.1. 案例](#-1)
		* [2.12.1.2. 运价系统的架构图](#-1)
	* [2.12.2. Spark 任务入口程序](#Spark-1)
	* [2.12.3. Spark ＋ MongoDB演示](#SparkMongoDB)
* [2.13. 安装SBT](#SBT)
	* [2.13.1. Linux中安装SBT](#LinuxSBT)
	* [2.13.2. Spark快速入门之SBT安装](#SparkSBT)
* [3.1. 在local模式下运行](#local)
* [3.2. 在remote模式下运行](#remote)
* [3.3. yarn模式下的运行](#yarn)
* [3.4. inline configuration](#inlineconfiguration)
* [3.5. hive](#hive)
* [3.6. SQL](#SQL)
* [3.7. Streaming](#Streaming)
* [3.8. kafka](#kafka-1)
* [3.9. python](#python)
* [3.10. spark](#spark-1)
* [3.11. flink - Python env - Conda](#flink-Pythonenv-Conda)
	* [3.11.1. 准备工作](#-1)
	* [3.11.2. 搭建 PyFlink 环境](#PyFlink)
		* [3.11.2.1. Step 1. 制作 **JobManager** 上的 **PyFlink Conda** 环境](#Step1.JobManagerPyFlinkConda)
		* [3.11.2.2. Step 2. 制作 TaskManager 上的 PyFlink Conda 环境](#Step2.TaskManagerPyFlinkConda)
		* [3.11.2.3. Step 3. 在 PyFlink 中使用 Conda 环境](#Step3.PyFlinkConda)
* [3.12. Apache Sedona](#ApacheSedona)
* [3.13. oracle (貌似不太常用)](#oracle)
* [3.14. 简单介绍oracle](#oracle-1)
	* [3.14.1. 连接Oracle数据库](#Oracle)

<!-- vscode-markdown-toc-config
	numbering=false
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->

Project to Design a Hadoop/Spark [Raspberry Pi 4 Cluster](https://github.com/YutingYao/pi-cluster) for Distributed Computing.

An efficient quick-start tool to build [a Raspberry Pi (or Debian-based) Cluster](https://github.com/YutingYao/RaspPi-Cluster) with popular ecosystem like Hadoop, Spark

Repo for instructions on setting up a micro compute cluster with the [NVidia Jetson Nano boards](https://github.com/YutingYao/JetsonCluster) and potentially Ansible playbooks for configuration and setup.

Setting up a [K3s Kubernetes](https://github.com/YutingYao/jetsonnano-k3s-gpu) cluster on my Nvidia Jetson Nano

Cluster made out of [Nvidia Jetson Nano's](https://github.com/YutingYao/NanoCluster)

# 1. 烧录系统

## <a name=''></a>1.1. 三步走

1. 下载树莓派ubuntu镜像-[Ubuntu Desktop 21.04](https://ubuntu.com/download/raspberry-pi/thank-you?version=21.04&architecture=desktop-arm64+raspi)，ubuntu镜像使用desktop版本
2. [SD卡格式化](https://www.sdcard.org/downloads/formatter/sd-memory-card-formatter-for-windows-download/)
3. [烧录系统](https://www.balena.io/etcher/)

设置language

将terminal和text放到桌面上

```sh
sudo apt install vim
```

## <a name='ibus'></a>1.2. 安装输入法ibus 需要重启（但这一步,貌似不需要）

```sh
#ctrl+alt+t进入终端，输入ibus
ibus                              #检测iubs
sudo apt-get install ibus-pinyin  #安装输入法
ibus-setup     #添加输入法（pinyin）
ibus restart   #重启ibus
```

## <a name='-1'></a>1.3. 安装远程控制（但这一步,目前没有成功）(尝试n种方法最终放弃)

```sh
sudo apt-get install tightvncserver
sudo tightvncserver

sudo apt install vino
sudo apt-get install dconf-editor
```

开机自启

```sh
mkdir -p ~/.config/autostart
cp /usr/share/applications/vino-server.desktop ~/.config/autostart/.
cd /usr/lib/systemd/user/graphical-session.target.wants
sudo ln -s ../vino-server.service ./.
```

配置服务器

```sh
gsettings set org.gnome.Vino prompt-enabled false
gsettings set org.gnome.Vino require-encryption false
Set a password to access the VNC server
```

设置登录密码

```sh
gsettings set org.gnome.Vino authentication-methods "['vnc']"
gsettings set org.gnome.Vino vnc-password $(echo -n 'thepassword'|base64)
Reboot the system so that the settings take effect
sudo reboot
```


### 再试一遍：

ubuntu20.04的系统不太好搞

```sh
# 安装可能需要用到的内容
sudo apt install tightvncserver gnome-panel gnome-settings-daemon metacity nautilus gnome-terminal gnome-session-flashback gdm3
# 第一次会提示输入密码。
# 这里启动窗口是为了自动生成一份配置文件
tightvncserver :0 -geometry 1280x720 -depth 24 -dpi 96
# 先杀掉窗口
tightvncserver -kill :0
```

修改文件

此文件的权限应该是775

```sh
vncserver -kill :1
mv ~/.vnc/xstartup ~/.vnc/xstartup.bak
sudo vim ~/.vnc/xstartup
```

```sh
sudo vim /home/yaoyuting03/.vnc/xstartup
```

```sh
sudo vim ~/.vnc/xstartup
```

添加内容:

```sh
#!/bin/bash
xrdb $HOME/.Xresources
startxfce4 &
```

```sh
#!/bin/sh

xrdb $HOME/.Xresources
xsetroot -solid grey
#x-terminal-emulator -geometry 80x24+10+10 -ls -title "$VNCDESKTOP Desktop" &
#x-window-manager &

# Fix to make GNOME work
export XKL_XMODMAP_DISABLE=1
/etc/X11/Xsession

# 下面这一段是我加上去的
unset SESSION_MANAGER
unset DBUS_SESSION_BUS_ADDRESS
export XKL_XMODMAP_DISABLE=1
export XDG_CURRENT_DESKTOP="GNOME-Flashback:GNOME"
export XDG_MENU_PREFIX="gnome-flashback-"
gnome-session --session=gnome-flashback-metacity --disable-acceleration-check &
```

添加

```sh
unset DBUS_SESSION_BUS_ADDRESS
```

修改

```sh
# /etc/X11/XSession
# 改成这个
mate-session
```

输入

```sh
#!/bin/sh
unset SESSION_MANAGER
unset DBUS_SESSION_BUS_ADDRESS
startxfce4 &
```

使其可执行:

```sh
chmod +x ~/.vnc/xstartup
```


执行

```sh
sudo chmod +x ~/.vnc/xstartup
vncserver 
```


服务可以通过以下方式启动或停止:

终止当前正在运行的服务器：

```sh
vncserver -kill :1
```

```sh
sudo service vncserver@1 start/stop
```

```sh
tightvncserver
```

启动一个新的服务器

```sh
# 再次启动窗口
tightvncserver :0 -geometry 1280x720 -depth 24 -dpi 96
```

### 可能需要安装 xfce4

```sh
sudo apt install xfce4 xfce4-goodies
```

# 2. 安装大数据分析软件

[大数据架构](http://dblab.xmu.edu.cn/blog/988-2/)请参考这个链接。

## <a name='docker'></a>2.1. docker环境

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

使用以下命令设置稳定存储库。要添加夜间或测试存储库，在下面的命令中的单词后添加单词或（或两者兼有）。

```sh
 echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
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

## <a name='hadoop'></a> hadoop

### <a name='32Hadoop64'></a> 静默警告（由于使用了32位Hadoop构建和64位操作系统）

修改Hadoop环境配置:

```sh
sudo vim /opt/hadoop/etc/hadoop/hadoop-env.sh
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
sudo vim ~/.bashrc
```

```sh
export HADOOP_HOME_WARN_SUPPRESS=1
export HADOOP_ROOT_LOGGER="WARN,DRFA" 
```

来源~/.bashrc：

```sh
source ~/.bashrc
```

将.bashrc复制到群集中的其他节点：

```sh
clusterscp ~/.bashrc
```

创建Hadoop群集目录（多节点设置）。

```sh
clustercmd sudo mkdir -p /opt/hadoop_tmp/hdfs
clustercmd sudo chown pi:pi –R /opt/hadoop_tmp
clustercmd sudo mkdir -p /opt/hadoop
clustercmd sudo chown pi:pi /opt/hadoop
```

将Hadoop文件复制到其他节点。

```sh
for pi in $(otherpis); do rsync -avxP $HADOOP_HOME $pi:/opt; done
```

验证是否在其他节点上安装：

```sh
clustercmd hadoop version | grep Hadoop
Hadoop 3.2.1
Hadoop 3.2.1
Hadoop 3.2.1
Hadoop 3.2.1
```

修改用于群集设置的Hadoop配置文件。

```sh
sudo vim /opt/hadoop/etc/hadoop/core-site.xml
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
sudo vim /opt/hadoop/etc/hadoop/hdfs-site.xml
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
sudo vim /opt/hadoop/etc/hadoop/mapred-site.xml
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
sudo vim /opt/hadoop/etc/hadoop/yarn-site.xml
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
clustercmd rm -rf /opt/hadoop_tmp/hdfs/datanode/*
clustercmd rm -rf /opt/hadoop_tmp/hdfs/namenode/*
```

创建/编辑 master amd worker files.

```sh
cd $HADOOP_HOME/etc/hadoop
vim master
```

在文件中添加一行:

```sh
pi1
```

```sh
vim workers
```

将其他pi主机名添加到文件：

```sh
pi2
pi3
pi4
```

编辑主机文件。

```sh
sudo vim /etc/hosts
```

删除该行（所有节点将具有相同的主机配置）：

```sh
127.0.1.1 pi1
```

将更新后的文件复制到其他集群节点:

```sh
clusterscp /etc/hosts
```

现在重新启动集群:

```sh
clusterreboot
```

格式化并启动多节点集群。

```sh
hdfs namenode -format -force
start-dfs.sh && start-yarn.sh
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



## <a name='spark'></a> spark

我们可以使用Spark SQL来执行常规分析，

Spark Streaming 来来做流数据处理，

以及用Mlib来执行机器学习等。

Java，python，scala及R语言的支持也是其通用性的表现之一。

官方的数据表明：它可以比传统的MapReduce快上100倍。

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




## <a name='geospark'></a>2.8. geospark

[系列教程](https://www.jianshu.com/nb/37398936)

GeoSpark是基于Spark之上的分布式群集计算系统。

GeoSpark扩展了Spark Core和SparkSQL并提出了空间弹性分布式数据集（Spatial Resilient Distributed Datasets (SRDDs)）同时提供了可视化组件。

简而言之就是可以利用它在Spark上做空间运算。

能够基于经纬度等信息创建

* 点（Point）
* 线(LineString）
* 面(Polygon)。

并提供了几种空间查询:

* 空间临近查询(Spatial KNN Query)
* 空间范围查询( Spatial Range Query)
* 空间连接查询(Spatial Join Query)
* 距离连接查询(Distance Join Query)

[Spatial RDD](https://blog.csdn.net/SUDDEV/article/details/104261704)

对应的几个类为：

* 坐标：Coordinate
* 点：Point、MultiPoint
* 线：LineString、MultiLineString（多条线）、LinearRing(环线）
* 面：Polygon、MultiPolygon
* 集合：GeometryCollection

### <a name='geospark-1'></a>2.8.1. geospark部署

环境准备

* JDK 1.8
* Scala 2.11.x

```xml
<properties>
    <scala.version>2.11.8</scala.version>
    <spark.version>2.3.4</spark.version>
    <scala.binary.version>2.11</scala.binary.version>
    <geospark.version>1.3.0</geospark.version>
</properties>

<dependencies>
    <dependency>
      <groupId>org.scala-lang</groupId>
      <artifactId>scala-library</artifactId>
      <version>${scala.version}</version>
    </dependency>

    <dependency>
      <groupId>org.apache.spark</groupId>
      <artifactId>spark-core_${scala.binary.version}</artifactId>
      <version>${spark.version}</version>
    </dependency>

    <dependency>
      <groupId>org.apache.spark</groupId>
      <artifactId>spark-sql_${scala.binary.version}</artifactId>
      <version>${spark.version}</version>
    </dependency>

    <dependency>
      <groupId>org.datasyslab</groupId>
      <artifactId>geospark</artifactId>
      <version>${geospark.version}</version>
    </dependency>

    <dependency>
      <groupId>org.datasyslab</groupId>
      <artifactId>geospark-sql_2.3</artifactId>
      <version>${geospark.version}</version>
    </dependency>
  </dependencies>
```

### <a name='geospark-1'></a>2.8.2. geospark示例

尝鲜：新建一个CSV文件checkin.csv：

```c
-88.175933,32.360763,gas
-88.388954,32.357073,bar
-88.221102,32.35078,restaurant
```

Code:

```js
package com.suddev.bigdata.core

import org.apache.spark.serializer.KryoSerializer
import org.apache.spark.{SparkConf, SparkContext}
import org.datasyslab.geospark.enums.FileDataSplitter
import org.datasyslab.geospark.serde.GeoSparkKryoRegistrator
import org.datasyslab.geospark.spatialRDD.PointRDD


object DemoApp {
  def main(args: Array[String]): Unit = {
    // 创建SparkConf
    val conf = new SparkConf().
      setAppName("GeoSparkDemo1").
      setMaster("local[*]").
      set("spark.serializer", classOf[KryoSerializer].getName).
      set("spark.kryo.registrator", classOf[GeoSparkKryoRegistrator].getName)
    val sc = new SparkContext(conf)

    val pointRDDInputLocation = "data/checkin.csv"
    // 这个变量控制我们的地理经度和纬度在数据的哪两列，我们这里是第0,1列，Offset就设置为0
    val pointRDDOffset = 0
    val pointRDDSplitter = FileDataSplitter.CSV
    // 这个参数允许我们除了经纬度外还可以携带其他自定义数据
    val carryOtherAttributes = true
    val objectRDD = new PointRDD(sc, pointRDDInputLocation,pointRDDOffset, pointRDDSplitter, carryOtherAttributes)
    // 获取rawRDD进行遍历输出
    objectRDD.rawSpatialRDD.rdd.collect().foreach(println)
  }
}
```

Output:

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2xpne7kb29g0.png)

通过GeometryFactory创建地理数据：

```js
package com.suddev.bigdata.core
import com.vividsolutions.jts.geom.{Coordinate, GeometryFactory}

object GeoDemoApp {
  def main(args: Array[String]): Unit = {
    // 创建一个坐标
    val coord = new Coordinate(-84.01, 34.01)
    // 实例化Geometry工厂类
    val factory = new GeometryFactory()
    // 创建Point
    val pointObject = factory.createPoint(coord)
    // 创建Polygon
    val coordinates = new Array[Coordinate](5)
    coordinates(0) = new Coordinate(0,0)
    coordinates(1) = new Coordinate(0,4)
    coordinates(2) = new Coordinate(4,4)
    coordinates(3) = new Coordinate(4,0)
    // 多边形是闭合的，所有最后一个点就是第一个点
    coordinates(4) = coordinates(0) 
    val polygonObject = factory.createPolygon(coordinates)
    // 创建LineString
    val coordinates2 = new Array[Coordinate](4)
    coordinates2(0) = new Coordinate(0,0)
    coordinates2(1) = new Coordinate(0,4)
    coordinates2(2) = new Coordinate(4,4)
    coordinates2(3) = new Coordinate(4,0)
    val linestringObject = factory.createLineString(coordinates2)
  }
}
```

### <a name='SpatialRDDSRDD'></a>2.8.3. 创建SpatialRDD(SRDD)

GeoSpark-Core 提供了三种特殊的SpatialRDD：

* PointRDD
* PolygonRDD
* LineStringRDD

它们可以从Spark RDD，CSV，TSV，WKT，WKB，Shapefiles，GeoJSON和NetCDF / HDF格式加载。
这里给出几种常用场景示例：

step 1. 初始化SparkContext

```sql
val conf = new SparkConf().
  setAppName("GeoSparkDemo2").
  setMaster("local[*]").
  set("spark.serializer", classOf[KryoSerializer].getName).
  set("spark.kryo.registrator", classOf[GeoSparkKryoRegistrator].getName)
val sc = new SparkContext(conf)
```

step 2. 创建typed Spatial RDD - 通过已有Spark RDD创建PointRDD

```js
// 数据准备
val data = Array(
      (-88.331492,32.324142,"hotel"),
      (-88.175933,32.360763,"gas"),
      (-88.388954,32.357073,"bar"),
      (-88.221102,32.35078,"restaurant")
    )
val geometryFactory = new GeometryFactory()
// 创建Spark RDD[Point]
val pointsRowSpatialRDD = sc.parallelize(data)
      .map(x => {
       // 创建坐标
        val coord = new Coordinate(x._1, x._2)
        // 用户定义数据
        val userData = x._3
        // 创建Point
        val point = geometryFactory.createPoint(coord)
        // Point支持携带用户数据
        point.setUserData(userData)
        point
       })
// 创建PointRDD 
val pointRDD = new PointRDD(pointsRowSpatialRDD)
```

step 2. 创建typed Spatial RDD - 通过CSV/TSV创建PointRDD

创建checkin.csv在 data/checkin.csv路径下:

```js
-88.331492,32.324142,hotel
-88.175933,32.360763,gas
-88.388954,32.357073,bar
-88.221102,32.35078,restaurant
```

checkin.csv一共有三列(Column IDs) 为 0, 1, 2.
第0，1 列是坐标
第2列是用户定义数据
pointRDDOffset 控制地理坐标从第几列开始，故offset=0

```js
val pointRDDInputLocation = "data/checkin.csv"
val pointRDDOffset = 0  // The coordinates start from Column 0
val pointRDDSplitter = FileDataSplitter.CSV // or use  FileDataSplitter.TSV
val carryOtherAttributes = true // 支持携带用户定义数据 (hotel, gas, bar...)
var objectRDD = new PointRDD(sc, pointRDDInputLocation, pointRDDOffset, pointRDDSplitter, carryOtherAttributes)
```

step 2. 创建typed Spatial RDD - 通过CSV/TSV创建PolygonRDD/LineStringRDD

创建checkinshape.csv在 data/checkin.csv路径下:

```js
-88.331492,32.324142,-88.331492,32.324142,-88.331492,32.324142,-88.331492,32.324142,-88.331492,32.324142,hotel
-88.175933,32.360763,-88.175933,32.360763,-88.175933,32.360763,-88.175933,32.360763,-88.175933,32.360763,gas
-88.388954,32.357073,-88.388954,32.357073,-88.388954,32.357073,-88.388954,32.357073,-88.388954,32.357073,bar
-88.221102,32.35078,-88.221102,32.35078,-88.221102,32.35078,-88.221102,32.35078,-88.221102,32.35078,restaurant
```

checkinshape.csv一共有11列(Column IDs) 为 0~10
第0 - 9 列是5个坐标
第10列是用户定义数据
polygonRDDStartOffset 控制地理坐标从第几列开始，故StartOffset = 0
polygonRDDStartOffset 控制地理坐标从第几列结束，故EndOffset = 8

```js
val polygonRDDInputLocation = "data/checkinshape.csv"
val polygonRDDStartOffset = 0 // The coordinates start from Column 0
val polygonRDDEndOffset = 8 // The coordinates end at Column 8
val polygonRDDSplitter = FileDataSplitter.CSV // or use  FileDataSplitter.TSV
val carryOtherAttributes = true
var objectRDD = new PolygonRDD(sc, polygonRDDInputLocation, polygonRDDStartOffset, polygonRDDEndOffset, polygonRDDSplitter, carryOtherAttributes)
```

step 3. 创建通用Spatial RDD

通用SpatialRDD不同于PointRDD，PolygonRDD和LineStringRDD，

它允许输入数据文件包含混合的几何类型，能够适用更多场景。

WKT/WKB/GeoJson/Shapefile等文件类型就

可以支持保存多种地理数据如 LineString, Polygon和MultiPolygon

step 3. 创建通用Spatial RDD - 通过WKT/WKB创建 - checkin.tsv

```tsv
POINT(-88.331492 32.324142) hotel
POINT(-88.175933 32.360763) gas
POINT(-88.388954 32.357073) bar
POINT(-88.221102 32.35078) restaurant
```

代码：

```js
val inputLocation = "data/checkin.tsv"
val wktColumn = 0 // The WKT string starts from Column 0
val allowTopologyInvalidGeometries = true 
val skipSyntaxInvalidGeometries = false  
val spatialRDD = WktReader.readToGeometryRDD(sc, inputLocation, wktColumn, allowTopologyInvalidGeometries, skipSyntaxInvalidGeometries)
```

step 3. 创建通用Spatial RDD - 通过GeoJSON创建 - polygon.json

```json
{ "type": "Feature", "properties": { "STATEFP": "01", "COUNTYFP": "077", "TRACTCE": "011501", "BLKGRPCE": "5", "AFFGEOID": "1500000US010770115015", "GEOID": "010770115015", "NAME": "5", "LSAD": "BG", "ALAND": 6844991, "AWATER": 32636 }, "geometry": { "type": "Polygon", "coordinates": [ [ [ -87.621765, 34.873444 ], [ -87.617535, 34.873369 ], [ -87.6123, 34.873337 ], [ -87.604049, 34.873303 ], [ -87.604033, 34.872316 ], [ -87.60415, 34.867502 ], [ -87.604218, 34.865687 ], [ -87.604409, 34.858537 ], [ -87.604018, 34.851336 ], [ -87.603716, 34.844829 ], [ -87.603696, 34.844307 ], [ -87.603673, 34.841884 ], [ -87.60372, 34.841003 ], [ -87.603879, 34.838423 ], [ -87.603888, 34.837682 ], [ -87.603889, 34.83763 ], [ -87.613127, 34.833938 ], [ -87.616451, 34.832699 ], [ -87.621041, 34.831431 ], [ -87.621056, 34.831526 ], [ -87.62112, 34.831925 ], [ -87.621603, 34.8352 ], [ -87.62158, 34.836087 ], [ -87.621383, 34.84329 ], [ -87.621359, 34.844438 ], [ -87.62129, 34.846387 ], [ -87.62119, 34.85053 ], [ -87.62144, 34.865379 ], [ -87.621765, 34.873444 ] ] ] } },
{ "type": "Feature", "properties": { "STATEFP": "01", "COUNTYFP": "045", "TRACTCE": "021102", "BLKGRPCE": "4", "AFFGEOID": "1500000US010450211024", "GEOID": "010450211024", "NAME": "4", "LSAD": "BG", "ALAND": 11360854, "AWATER": 0 }, "geometry": { "type": "Polygon", "coordinates": [ [ [ -85.719017, 31.297901 ], [ -85.715626, 31.305203 ], [ -85.714271, 31.307096 ], [ -85.69999, 31.307552 ], [ -85.697419, 31.307951 ], [ -85.675603, 31.31218 ], [ -85.672733, 31.312876 ], [ -85.672275, 31.311977 ], [ -85.67145, 31.310988 ], [ -85.670622, 31.309524 ], [ -85.670729, 31.307622 ], [ -85.669876, 31.30666 ], [ -85.669796, 31.306224 ], [ -85.670356, 31.306178 ], [ -85.671664, 31.305583 ], [ -85.67177, 31.305299 ], [ -85.671878, 31.302764 ], [ -85.671344, 31.302123 ], [ -85.668276, 31.302076 ], [ -85.66566, 31.30093 ], [ -85.665687, 31.30022 ], [ -85.669183, 31.297677 ], [ -85.668703, 31.295638 ], [ -85.671985, 31.29314 ], [ -85.677177, 31.288211 ], [ -85.678452, 31.286376 ], [ -85.679236, 31.28285 ], [ -85.679195, 31.281426 ], [ -85.676865, 31.281049 ], [ -85.674661, 31.28008 ], [ -85.674377, 31.27935 ], [ -85.675714, 31.276882 ], [ -85.677938, 31.275168 ], [ -85.680348, 31.276814 ], [ -85.684032, 31.278848 ], [ -85.684387, 31.279082 ], [ -85.692398, 31.283499 ], [ -85.705032, 31.289718 ], [ -85.706755, 31.290476 ], [ -85.718102, 31.295204 ], [ -85.719132, 31.29689 ], [ -85.719017, 31.297901 ] ] ] } },
{ "type": "Feature", "properties": { "STATEFP": "01", "COUNTYFP": "055", "TRACTCE": "001300", "BLKGRPCE": "3", "AFFGEOID": "1500000US010550013003", "GEOID": "010550013003", "NAME": "3", "LSAD": "BG", "ALAND": 1378742, "AWATER": 247387 }, "geometry": { "type": "Polygon", "coordinates": [ [ [ -86.000685, 34.00537 ], [ -85.998837, 34.009768 ], [ -85.998012, 34.010398 ], [ -85.987865, 34.005426 ], [ -85.986656, 34.004552 ], [ -85.985, 34.002659 ], [ -85.98851, 34.001502 ], [ -85.987567, 33.999488 ], [ -85.988666, 33.99913 ], [ -85.992568, 33.999131 ], [ -85.993144, 33.999714 ], [ -85.994876, 33.995153 ], [ -85.998823, 33.989548 ], [ -85.999925, 33.994237 ], [ -86.000616, 34.000028 ], [ -86.000685, 34.00537 ] ] ] } },
{ "type": "Feature", "properties": { "STATEFP": "01", "COUNTYFP": "089", "TRACTCE": "001700", "BLKGRPCE": "2", "AFFGEOID": "1500000US010890017002", "GEOID": "010890017002", "NAME": "2", "LSAD": "BG", "ALAND": 1040641, "AWATER": 0 }, "geometry": { "type": "Polygon", "coordinates": [ [ [ -86.574172, 34.727375 ], [ -86.562684, 34.727131 ], [ -86.562797, 34.723865 ], [ -86.562957, 34.723168 ], [ -86.562336, 34.719766 ], [ -86.557381, 34.719143 ], [ -86.557352, 34.718322 ], [ -86.559921, 34.717363 ], [ -86.564827, 34.718513 ], [ -86.567582, 34.718565 ], [ -86.570572, 34.718577 ], [ -86.573618, 34.719377 ], [ -86.574172, 34.727375 ] ] ] } },
```

代码：

```js
val inputLocation = "data/polygon.json"
val allowTopologyInvalidGeometries = true 
val skipSyntaxInvalidGeometries = false
val spatialRDD = GeoJsonReader.readToGeometryRDD(sc, inputLocation, allowTopologyInvalidGeometries, skipSyntaxInvalidGeometries)
```

step 3. 创建通用Spatial RDD - 通过Shapefile创建

```js
val shapefileInputLocation="data/myshapefile"
// System.setProperty("geospark.global.charset", "utf8")
val spatialRDD = ShapefileReader.readToGeometryRDD(sc, shapefileInputLocation)
```

注意:

.shp, .shx, .dbf 文件后缀必须是小写. 并且 shapefile 文件必须命名为myShapefile, 文件夹结构如下:

```js
- shapefile1
- shapefile2
- myshapefile
    - myshapefile.shp
    - myshapefile.shx
    - myshapefile.dbf
    - myshapefile...
    - ...
```

如果出现乱码问题可以在ShapefileReader.readToGeometryRDD方法调用之前设置编码参数

```js
System.setProperty("geospark.global.charset", "utf8")
```

step 4. 坐标系转换

GeoSpark采用EPGS标准坐标系，其坐标系也可参考EPSG官网：<https://epsg.io/>

如果需要转换成其他标准的坐标系，可以通过以下方法

```js
// 源标准
val sourceCrsCode = "epsg:4326"
// 目标标准
val targetCrsCode = "epsg:3857"
objectRDD.CRSTransform(sourceCrsCode, targetCrsCode)
```

### <a name='SpatialRangeQuery'></a>2.8.4. 空间范围查询(Spatial Range Query)

空间范围查询，顾名思义我们可以给定一个范围（query window），然后查询出包含在当前范围内的地理对象。

1.1 数据准备

创建checkin1.csv在 data/checkin1.csv路径下:
注意这里故意把bar坐标修改了一下

```js
-88.331492,32.324142,hotel
-88.175933,32.360763,gas
-99.388954,32.357073,bar
-88.221102,32.35078,restaurant
```

1.2 代码示例

considerBoundaryIntersection参数可以配置查询是否包括query window边界上的地理对象。

```js
package com.suddev.bigdata.query

import com.vividsolutions.jts.geom.Envelope
import org.apache.spark.serializer.KryoSerializer
import org.apache.spark.{SparkConf, SparkContext}
import org.datasyslab.geospark.enums.FileDataSplitter
import org.datasyslab.geospark.serde.GeoSparkKryoRegistrator
import org.datasyslab.geospark.spatialOperator.RangeQuery
import org.datasyslab.geospark.spatialRDD.PointRDD

/**
 * Spatial Range Query
 * @author Rand
 * @date 2020/4/16 0016
 */
object SpatialRangeQueryApp {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().
      setAppName("SpatialRangeQueryApp").setMaster("local[*]").
      set("spark.serializer",classOf[KryoSerializer].getName).
      set("spark.kryo.registrator", classOf[GeoSparkKryoRegistrator].getName)
    implicit val sc = new SparkContext(conf)
    val objectRDD = createPointRDD
    objectRDD.rawSpatialRDD.rdd.collect().foreach(println)

    // 定义QueryWindow
    val rangeQueryWindow = new Envelope(-90.01, -80.01, 30.01, 40.01)
    // 是否考虑边界
    val considerBoundaryIntersection = false
    val usingIndex = false
    val queryResult = RangeQuery.SpatialRangeQuery(objectRDD, rangeQueryWindow, considerBoundaryIntersection, usingIndex)
    queryResult.rdd.collect().foreach(println)
  }

  def createPointRDD(implicit sc:SparkContext): PointRDD ={
    val pointRDDInputLocation = "data/checkin1.csv"
    // 这个变量控制我们的地理经度和纬度在数据的哪两列，我们这里是第0,1列，Offset就设置为0
    val pointRDDOffset = 0
    val pointRDDSplitter = FileDataSplitter.CSV
    // 这个参数允许我们除了经纬度外还可以携带其他自定义数据
    val carryOtherAttributes = true
    val objectRDD = new PointRDD(sc, pointRDDInputLocation,pointRDDOffset, pointRDDSplitter, carryOtherAttributes)
    objectRDD
  }
}
```

这里的rangeQueryWindow除了支持Envelope外还可以使用Point/Polygon/LineString

点->创建一个Point Query Window：

```js
val geometryFactory = new GeometryFactory()
val pointObject = geometryFactory.createPoint(new Coordinate(-84.01, 34.01))
```

多边形->创建一个Polygon Query Window：

```js
val geometryFactory = new GeometryFactory()
val coordinates = new Array[Coordinate](5)
coordinates(0) = new Coordinate(0,0)
coordinates(1) = new Coordinate(0,4)
coordinates(2) = new Coordinate(4,4)
coordinates(3) = new Coordinate(4,0)
coordinates(4) = coordinates(0) // The last coordinate is the same as the first coordinate in order to compose a closed ring
val polygonObject = geometryFactory.createPolygon(coordinates)
```

线->创建一个Linestring Query Window：

```js
val geometryFactory = new GeometryFactory()
val coordinates = new Array[Coordinate](5)
coordinates(0) = new Coordinate(0,0)
coordinates(1) = new Coordinate(0,4)
coordinates(2) = new Coordinate(4,4)
coordinates(3) = new Coordinate(4,0)
val linestringObject = geometryFactory.createLineString(coordinates)
```

1.3 运行效果

可以看到查询结果包含hotel,gas,restaurant不包含bar

```js
POINT (-88.331492 32.324142) hotel
POINT (-88.175933 32.360763) gas
POINT (-99.388954 32.357073) bar
POINT (-88.221102 32.35078) restaurant
-------------------------------
POINT (-88.331492 32.324142) hotel
POINT (-88.175933 32.360763) gas
POINT (-88.221102 32.35078) restaurant
-------------------------------
```

2.空间临近查询(Spatial KNN Query)

空间临近算法，我们可以给的一个中心点的坐标，然后找出该点相邻的K个地理对象

2.1 数据准备

创建checkin2.csv在 data/checkin2.csv路径下:

```js
-88.331492,32.324142,hotel
-88.175933,32.360763,gas1
-88.176033,32.360763,gas2
-88.175833,32.360763,gas3
-88.388954,32.357073,bar
-88.221102,32.35078,restaurant
```

2.2 代码示例

k参数可以设置限制查询k个结果

🙃这里吐槽一下，如果查询结果为5个，但是我们k设置的大于5就会报空指针异常hhh，不能查到多少返回多少么

🙃再吐槽一下，它这种设计一次只能查询一个点，实际生产上肯定是一批点和另外一批点做KNN匹配，而他这个不支持两个RDD查询，如果有感兴趣的两个RDD做KNN匹配的请给我留言，我单独写一篇文章

```js
package com.suddev.bigdata.query

import com.vividsolutions.jts.geom.{Coordinate, Envelope, GeometryFactory}
import org.apache.spark.serializer.KryoSerializer
import org.apache.spark.{SparkConf, SparkContext}
import org.datasyslab.geospark.enums.FileDataSplitter
import org.datasyslab.geospark.serde.GeoSparkKryoRegistrator
import org.datasyslab.geospark.spatialOperator.{KNNQuery, RangeQuery}
import org.datasyslab.geospark.spatialRDD.PointRDD
import scala.collection.JavaConversions._

/**
 * SpatialKNNQueryApp
 * @author Rand
 * @date 2020/4/16 0016
 */
object SpatialKNNQueryApp {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().
      setAppName("SpatialKNNQueryApp").setMaster("local[*]").
      set("spark.serializer",classOf[KryoSerializer].getName).
      set("spark.kryo.registrator", classOf[GeoSparkKryoRegistrator].getName)
    implicit val sc = new SparkContext(conf)
    val objectRDD = createPointRDD
    objectRDD.rawSpatialRDD.rdd.collect().foreach(println)
    val geometryFactory = new GeometryFactory()
    // 做临近查询的中心点
    val pointObject = geometryFactory.createPoint(new Coordinate(-84.01, 34.01))
    val K = 2 // K Nearest Neighbors
    val usingIndex = false
    val result = KNNQuery.SpatialKnnQuery(objectRDD, pointObject, K, usingIndex)
    println("-----------------------------------")
    // 记得import scala.collection.JavaConversions._ 否则这里报错哈
    result.foreach(println)
  }

  def createPointRDD(implicit sc:SparkContext): PointRDD ={
    val pointRDDInputLocation = "data/checkin2.csv"
    // 这个变量控制我们的地理经度和纬度在数据的哪两列，我们这里是第0,1列，Offset就设置为0
    val pointRDDOffset = 0
    val pointRDDSplitter = FileDataSplitter.CSV
    // 这个参数允许我们除了经纬度外还可以携带其他自定义数据
    val carryOtherAttributes = true
    val objectRDD = new PointRDD(sc, pointRDDInputLocation,pointRDDOffset, pointRDDSplitter, carryOtherAttributes)
    objectRDD
  }
}
```

2.3 运行效果

可以看到查询结果包含gas3，gas1两个点

```js
POINT (-88.331492 32.324142) hotel
POINT (-88.175933 32.360763) gas1
POINT (-88.176033 32.360763) gas2
POINT (-88.175833 32.360763) gas3
POINT (-88.388954 32.357073) bar
POINT (-88.221102 32.35078) restaurant
-----------------------------------
POINT (-88.175833 32.360763) gas3
POINT (-88.175933 32.360763) gas1
```

3.空间连接查询(Spatial Join Query)

空间连接查询算法，类似于数据库中的Join操作，

有Spatial RDD A and B，遍历A中的几何对象去匹配B中覆盖或相交的几何对象。

3.1 数据准备

创建checkin3.csv在 data/checkin3.csv路径下:

```js
-88.331492,32.324142,1.hotel
-88.175933,32.360763,1.gas
-88.388954,32.357073,1.bar
-88.588954,32.357073,1.spark
```

创建checkin4.csv在 data/checkin4.csv路径下:

```js
-88.175933,32.360763,2.gas
-88.388954,32.357073,2.bar
-88.221102,32.35078,2.restaurant
-88.321102,32.35078,2.bus
```

3.2 代码示例

```js
package com.suddev.bigdata.query

import org.apache.spark.serializer.KryoSerializer
import org.apache.spark.{SparkConf, SparkContext}
import org.datasyslab.geospark.enums.{FileDataSplitter, GridType}
import org.datasyslab.geospark.serde.GeoSparkKryoRegistrator
import org.datasyslab.geospark.spatialOperator.JoinQuery
import org.datasyslab.geospark.spatialRDD.PointRDD
/**
 * SpatialJoinQueryApp
 *
 * @author Rand
 * @date 2020/4/16 0016
 */
object SpatialJoinQueryApp {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().
      setAppName("SpatialJoinQueryApp").setMaster("local[*]").
      set("spark.serializer",classOf[KryoSerializer].getName).
      set("spark.kryo.registrator", classOf[GeoSparkKryoRegistrator].getName)
    implicit val sc = new SparkContext(conf)
    // 准备数据
    val objectRDD = createObjectRDDRDD
    objectRDD.rawSpatialRDD.rdd.collect().foreach(println)
    val queryWindowRDD = createQueryWindowRDD
    println("---------------------------")
    queryWindowRDD.rawSpatialRDD.rdd.collect().foreach(println)
    println("---------------------------")
    objectRDD.analyze()
    // 必须设置objectRDD和queryWindowRDD的spatialPartitioning
    // 条件有二
    // 1.objectRDD和queryWindowRDD的spatialPartitioning 必须非空相同
    // 2.objectRDD和queryWindowRDD的分区数量必须一样
    objectRDD.spatialPartitioning(GridType.KDBTREE)
    queryWindowRDD.spatialPartitioning(objectRDD.getPartitioner)
    val considerBoundaryIntersection = false
    val usingIndex = false
    val result = JoinQuery.SpatialJoinQuery(objectRDD, queryWindowRDD, usingIndex, considerBoundaryIntersection)
    result.rdd.foreach(println)
  }

  def createObjectRDDRDD(implicit sc:SparkContext): PointRDD ={
    val pointRDDInputLocation = "data/checkin3.csv"
    val pointRDDOffset = 0
    val pointRDDSplitter = FileDataSplitter.CSV
    val carryOtherAttributes = true
    val objectRDD = new PointRDD(sc, pointRDDInputLocation,pointRDDOffset, pointRDDSplitter, carryOtherAttributes)
    objectRDD
  }

  def createQueryWindowRDD(implicit sc:SparkContext): PointRDD ={
    val pointRDDInputLocation = "data/checkin4.csv"
    val pointRDDOffset = 0
    val pointRDDSplitter = FileDataSplitter.CSV
    val carryOtherAttributes = true
    val objectRDD = new PointRDD(sc, pointRDDInputLocation,pointRDDOffset, pointRDDSplitter, carryOtherAttributes)
    objectRDD
  }
}
```

3.3 运行效果

可以看到两边的gas，barJoin关联上了

```js
POINT (-88.331492 32.324142) 1.hotel
POINT (-88.175933 32.360763) 1.gas
POINT (-88.388954 32.357073) 1.bar
POINT (-88.588954 32.357073) 1.spark
---------------------------
POINT (-88.175933 32.360763) 2.gas
POINT (-88.388954 32.357073) 2.bar
POINT (-88.221102 32.35078) 2.restaurant
POINT (-88.321102 32.35078) 2.bus
---------------------------
(POINT (-88.175933 32.360763) 2.gas,[POINT (-88.175933 32.360763) 1.gas])
(POINT (-88.388954 32.357073) 2.bar,[POINT (-88.388954 32.357073) 1.bar])
```

4.距离连接查询(Distance Join Query)

距离联接查询将两个Spatial RDD A和B和一个距离作为输入。

对于A中的每个几何对象，找到B中都在给定距离之内的集合对象。

⚠️关于距离说明：

GeoSpark不会控制SpatialRDD中所有几何的坐标单位（基于度或基于米）。

GeoSpark中所有相关距离的单位与SpatialRDD中所有几何的单位（）相同。

转换参考坐标系（Coordinate Reference System）代码:

```js
val sourceCrsCode = "epsg:4326" // WGS84, the most common degree-based CRS
val targetCrsCode = "epsg:3857" // The most common meter-based CRS
objectRDD.CRSTransform(sourceCrsCode, targetCrsCode)
```

4.1 数据准备

创建checkin5.csv在 data/checkin5.csv路径下:

```js
-89.331492,32.324142,1.hotel
-88.1760,32.360763,1.gas
-88.3890,32.357073,1.bar
-89.588954,32.357073,1.spark
```

创建checkin6.csv在 data/checkin6.csv路径下:

```js
-88.175933,32.360763,2.gas
-88.388954,32.357073,2.bar
-88.221102,32.35078,2.restaurant
-88.321102,32.35078,2.bus
```

4.2 代码示例

```js
package com.suddev.bigdata.query

import org.apache.spark.serializer.KryoSerializer
import org.apache.spark.{SparkConf, SparkContext}
import org.datasyslab.geospark.enums.{FileDataSplitter, GridType}
import org.datasyslab.geospark.serde.GeoSparkKryoRegistrator
import org.datasyslab.geospark.spatialOperator.JoinQuery
import org.datasyslab.geospark.spatialRDD.{CircleRDD, PointRDD}

/**
 * DistanceJoinQueryApp
 *
 * @author Rand
 * @date 2020/4/16 0016
 */
object DistanceJoinQueryApp {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().
      setAppName("DistanceJoinQueryApp$").setMaster("local[*]").
      set("spark.serializer",classOf[KryoSerializer].getName).
      set("spark.kryo.registrator", classOf[GeoSparkKryoRegistrator].getName)
    implicit val sc = new SparkContext(conf)
    // 准备数据
    val objectRddA = createObjectRDDA
    objectRddA.rawSpatialRDD.rdd.collect().foreach(println)
    val objectRddB = createObjectRDDB
    println("---------------------------")
    objectRddB.rawSpatialRDD.rdd.collect().foreach(println)
    println("---------------------------")
    // 设置距离
    val circleRDD = new CircleRDD(objectRddA, 0.1) // Create a CircleRDD using the given distance
    circleRDD.analyze()
    circleRDD.spatialPartitioning(GridType.KDBTREE)
    objectRddB.spatialPartitioning(circleRDD.getPartitioner)

    val considerBoundaryIntersection = false // Only return gemeotries fully covered by each query window in queryWindowRDD
    val usingIndex = false

    val result = JoinQuery.DistanceJoinQueryFlat(objectRddB, circleRDD, usingIndex, considerBoundaryIntersection)
    result.rdd.foreach(println)
  }

  def createObjectRDDA(implicit sc:SparkContext): PointRDD ={
    val pointRDDInputLocation = "data/checkin5.csv"
    val pointRDDOffset = 0
    val pointRDDSplitter = FileDataSplitter.CSV
    val carryOtherAttributes = true
    val objectRDD = new PointRDD(sc, pointRDDInputLocation,pointRDDOffset, pointRDDSplitter, carryOtherAttributes)
    objectRDD
  }

  def createObjectRDDB(implicit sc:SparkContext): PointRDD ={
    val pointRDDInputLocation = "data/checkin6.csv"
    val pointRDDOffset = 0
    val pointRDDSplitter = FileDataSplitter.CSV
    val carryOtherAttributes = true
    val objectRDD = new PointRDD(sc, pointRDDInputLocation,pointRDDOffset, pointRDDSplitter, carryOtherAttributes)
    objectRDD
  }
}
```

4.3 运行效果

可以看到

1.gas匹配到了2.gas,2.restaurant两个点

1.bar匹配到了2.bar,2.bus两个点

```js
POINT (-89.331492 32.324142) 1.hotel
POINT (-88.176 32.360763) 1.gas
POINT (-88.389 32.357073) 1.bar
POINT (-89.588954 32.357073) 1.spark
---------------------------
POINT (-88.175933 32.360763) 2.gas
POINT (-88.388954 32.357073) 2.bar
POINT (-88.221102 32.35078) 2.restaurant
POINT (-88.321102 32.35078) 2.bus
---------------------------
(POINT (-88.176 32.360763) 1.gas,POINT (-88.175933 32.360763) 2.gas)
(POINT (-88.176 32.360763) 1.gas,POINT (-88.221102 32.35078) 2.restaurant)
(POINT (-88.389 32.357073) 1.bar,POINT (-88.388954 32.357073) 2.bar)
(POINT (-88.389 32.357073) 1.bar,POINT (-88.321102 32.35078) 2.bus)
```

```js

```

```js

```

```js

```




```

```sh

```

```sh

```

## <a name='flink'></a>2.10. flink

```sh
```

## <a name='PostgreSQL'></a>2.11. PostgreSQL

  Ubuntu默认包含PostgreSQL。要在Ubuntu上安装PostgreSQL，请使用apt get（或其他apt驱动）命令：

  ```s
  apt-get install postgresql-12
  ```

PostgreSQL vs MongoDB

[postgresql的速度比MongoDB更快](http://blog.chinaunix.net/uid-69999418-id-5848402.html)

[Mongodb与PostgreSQL+postgis相比，各自的优劣势是什么?](https://www.zhihu.com/question/47292026)

Spark对接

[Spark对接分析型数据库PostgreSQL版快速入门](https://help.aliyun.com/document_detail/118439.html)

[Spark jdbc postgresql数据库连接和写入操作源代码解读](https://my.oschina.net/u/4363935/blog/4026459)

[Spark jdbc postgresql数据库连接和写入操作源代码解读](https://www.cnblogs.com/zhchoutai/p/8677027.html)

[Spark读写postgresql](https://blog.csdn.net/weixin_40450867/article/details/102613275)

flink连接

[flink连接postgresql数据库](https://blog.csdn.net/weixin_43315211/article/details/88354331)

[Flink-cdc实时读postgresql](https://www.cnblogs.com/xiongmozhou/p/14817641.html)

[flink cdc捕获postgresql数据](https://blog.csdn.net/weixin_41197407/article/details/112655218)

[创建分析型数据库PostgreSQL版结果表](https://help.aliyun.com/knowledge_detail/162453.html)

## <a name='MongoDB'></a>2.12. MongoDB

[MongoDB 如何上手和避坑？](https://mp.weixin.qq.com/s/EhVsdlRQDC1VP1S1QQfnkg)

[MongoDB 不得不知的 12 个知识点](https://mp.weixin.qq.com/s/EMHKgo2R8z8uyjAksK8hIQ)

[当物流行业遇见MongoDB](https://mp.weixin.qq.com/s/SHn_YLqR0Wzu8OF_j21PIA)

[云MongoDB 优化让LBS服务性能提升十倍](https://mp.weixin.qq.com/s/mCIL100G1GGNcxNHJiSKUQ)

**HDFS vs. MongoDB**

都是基于廉价**x86服务器**的横向扩展架构，

都能支持到**TB到PB级**的数据量。数据会在**多节点自动备份**，来保证数据的**高可用和冗余**。两者都支持**非结构化数据的存储**，等等。

**但是，HDFS和MongoDB更多的是差异点：**

* 如在存储方式上 HDFS的存储是以文件为单位，每个文件64MB到128MB不等。而MongoDB则是**细颗粒化的、以文档为单位**的存储。

* HDFS不支持索引的概念，对数据的操作局限于扫描性质的读，MongoDB则支持基于**二级索引的快速检索**。

* MongoDB可以支持常见的**增删改查场景**，而HDFS一般只是一次写入后就很难进行修改。

* 从响应时间上来说，HDFS一般是分钟级别而MongoDB对手请求的响应时间通常以**毫秒作为单位**。

如果有一天你的经理告诉你：

他想知道网站上每天有多少404错误在发生，

这个时候如果你用HDFS，就还是需要通过全量扫描所有行，

而MongoDB则可以通过索引，很快地找到所有的404日志，可能花数秒钟就可以解答你经理的问题。

又比如说，如果你希望对每个日志项加一个自定义的属性，

在进行一些预处理后，MongoDB就会比较容地支持到。而一般来说，HDFS是不支持更新类型操作的。

### <a name='MongoSparkConnector'></a>2.12.1. Mongo Spark Connector 连接器

在这里我们在介绍下MongoDB官方提供的Mongo Spark连接器。

目前有3个连接器可用，包括社区第三方开发的和之前Mongo Hadoop连接器等，

这个Mong Spark是最新的，也是我们推荐的连接方案。

这个连接器是专门为Spark打造的，支持**双向数据**，读出和写入。

但是最关键的是 **条件下推**，也就是说：

如果你在**Spark端**指定了查询或者限制条件的情况下，这个**条件会被下推到MongoDB**去执行，

这样可以保证从MongoDB取出来、经过网络传输到Spark计算节点的数据确实都是用得着的。

没有**下推支持**的话，每次操作很可能**需要从MongoDB读取全量的数据，性能体验将会很糟糕**。

拿刚才的日志例子来说，如果我们只想对**404错误日志**进行分析，看那些错误都是哪些页面，以及每天错误页面数量的变化，

如果有条件下推，那么我们可以给**MongoDB一个限定条件：错误代码=404**，

这个条件会在MongoDB服务器端执行，

这样我们只需要通过**网络传输可能只是全部日志的0.1%的数据**，而不是没有**条件下推**情况下的全部数据。

另外，这个最新的连接器还支持和Spark计算节点**Co-Lo 部署**。

就是说在同一个节点上同时部署**Spark实例**和**MongoDB实例**。

这样做可以减少数据在网络上的传输带来的资源消耗及时延。

当然，这种部署方式需要注意**内存资源和CPU资源**的隔离。隔离的方式可以通过Linux的**cgroups**。

#### <a name='-1'></a>2.12.1.1. 案例

1. 法国航空是法国最大的航空公司：

为了提高客户体验，在最近施行的**360度客户视图**中，使用Spark对**已经收集在MongoDB里面的客户数据**进行分类及行为分析，并把结果（如客户的类别、标签等信息）**写回到MongoDB内每一个客户的文档结构**里。

2. Stratio是美国硅谷一家著名的金融大数据公司：

他们最近在一家在**31个国家有分支机构的跨国银行**实施了一个**实时监控平台**。该银行希望通过**对日志的监控和分析**来保证客户服务的响应时间以及**实时监测一些可能的违规或者金融欺诈行为**。在这个应用内， 他们使用了：

* Apache Flume 来**收集log**

* Spark来**处理实时的log**

* MongoDB来**存储收集的log**以及**Spark分析的结果**，如**Key Performance Indicators**等

3. 东方航空的挑战：

顾客在网站上订购机票，平均资料库查询200次就会下单订购机票，但是现在平均要查询1.2万次才会发生一次订购行为，

**同样的订单量，查询量却成长百倍。**

按照50%直销率这个目标计算，东航的运价系统要支持每天16亿的运价请求。

思路：空间换时间

当前的运价是通过**实时计算**的，

按照现在的计算能力，需要对已有系统进行100多倍的扩容。

另一个常用的思路，就是采用**空间换时间**的方式。

与其对每一次的运价请求进行耗时300ms的运算，不如事先**把所有可能的票价查询组合穷举出来并进行批量计算**，然后把结果**存入MongoDB**里面。

当需要**查询运价**时，直接按照 **出发+目的地+日期的方式** 做一个快速的DB查询，响应时间应该可以做到几十毫秒。

那为什么要用MongoDB？因为我们要处理的数据量庞大无比。按照1000多个航班，365天，26个仓位，100多渠道以及数个不同的航程类型，

我们要实时存取的运价记录有数十亿条之多。这个已经远远超出**常规RDBMS**可以承受的范围。

MongoDB基于**内存缓存的数据管理方式**决定了对**并发读写的响应**可以做到**很低延迟**，

**水平扩展**的方式可以通过**多台节点同时并发处理海量请求**。

事实上，全球最大的航空分销商，管理者全世界95%航空库存的Amadeus也正是使用MongoDB作为其1000多亿**运价缓存的存储方案**。

#### <a name='-1'></a>2.12.1.2. 运价系统的架构图

左边是发起航班查询请求的客户端，

首先会有**API服务器**进行**预处理**：一般航班请求会分为**库存查询**和**运价查询**。**库存查询**会直接到东航已有的**库存系统（Seat Inventory）**，同样是实现在MongoDB上面的。在确定库存后根据**库存结果**再从**Fare Cache系统**内查询相应的运价。

**Spark集群**则是另外一套计算集群，通过**Spark MongoDB连接套件**和**MongoDB Fare Cache集群**连接。

Spark 计算任务会**定期触发（如每天一次或者每4小时一次）**，这个任务会对所有的可能的**运价组合进行全量计算**，然后存入**MongoDB**，以供查询使用。

右半边则把原来**实时运算的集群换成了Spark+MongoDB**。Spark负责**批量计算一年内所有航班所有仓位的所有价格**，并以**高并发**的形式存储到MongoDB里面。

每秒钟处理的运价可以达到数万条。

当来自客户端的运价查询达到服务端以后，**服务端**直接就向MongoDB发出按照**日期**，**出发**，**到达机场**为条件的mongo查询。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6uytn281yhc0.png)

需要计算的任务，也就是所有**日期、航班、仓位**的组合，事先已经存放到**MongoDB**里面。

任务递交到**master**，然后**预先加载所需参考数据**，

**broadcast**就是把这些在内存里的数据复制到每一个**Spark计算节点的JVM**，

然后所有计算节点**多线程并发执行**，

从Mongodb里取出需要计算的仓位，调用东航自己的**运价逻辑**，得出结果以后，并保存回MongoDB。

### <a name='Spark-1'></a>2.12.2. Spark 任务入口程序

Spark和MongoDB的连接使用非常简单，下面就是一个代码示例：

```java
// initialization dependencies including base prices, pricing rules and some reference data
Map dependencies = MyDependencyManager.loadDependencies();
// broadcasting dependencies
javaSparkContext.broadcast(dependencies);

// create job rdd
cabinsRDD = MongoSpark.load(javaSparkContext).withPipeline(pipeline)

// for each cabin, date, airport pair, calculate the price
cabinsRDD.map(function calc_price);

// collect the result, which will cause the data to be stored into MongoDB
cabinsRDD.collect()
cabinsRDD.saveToMongo()
```

### <a name='SparkMongoDB'></a>2.12.3. Spark ＋ MongoDB演示

安装 Spark（略）

测试连接器

```sql
# cd ～／spark
# ./bin/spark-shell \
--conf "spark.mongodb.input.uri=mongodb://127.0.0.1/flights.av" \
--conf "spark.mongodb.output.uri=mongodb://127.0.0.1/flights.output" \
--packages org.mongodb.spark:mongo-spark-connector_2.10:1.0.0

import com.mongodb.spark._
import org.bson.Document

MongoSpark.load(sc).take(10).foreach(println)
```

简单分组统计
数据： 365天，所有航班库存信息，500万文档
任务： 按航班统计一年内所有余票量

```sql
MongoSpark.load(sc)
     .map(doc=>(doc.getString("flight") ,doc.getLong("seats")))
     .reduceByKey((x,y)=>(x+y))
      .take(10)
     .foreach(println)
```

简单分组统计加条件过滤
数据： 365天，所有航班库存信息，500万文档
任务： 按航班统计一年内所有库存，但是只处理昆明出发的航班

```sql
import org.bson.Document

MongoSpark.load(sc)
          .withPipeline(Seq(Document.parse("{ $match: { orig :  'KMG'  } }")))
    .map(doc=>(doc.getString("flight") ,doc.getLong("seats")))
    .reduceByKey((x,y)=>(x+y))
    .take(10)
    .foreach(println)
```

性能优化事项:

* 使用合适的**chunksize (MB)**
* Total data size / chunksize = chunks = RDD partitions = spark tasks
* 不要将**所有CPU核**分配给Spark
* 预留**1-2个core**给**操作系统**及**其他管理进程**
* 同机部署，适当情况可以**同机部署Spark+MongoDB**，利用**本地IO**提高性能

## <a name='SBT'></a>2.13. 安装SBT

### <a name='LinuxSBT'></a>2.13.1. Linux中安装SBT

1. 在官网上下载.tgz安装包

<https://www.scala-sbt.org/download.html>

2. 使用tar -zxvf 对安装包进行解压

```sh
tar -zxvf sbt-1.3.2.tgz 
```


##  Spark快速入门之SBT安装

Spark中没有自带sbt，需要手动安装sbt，

我的方法是下载`sbt-launch.jar`，然后将源改为国内源（aliyun），

我选择将sbt安装在`/opt/sbt`中。

```sh
sudo mkdir /opt/sbt
sudo chown -R hadoop /opt/sbt   #username is hadoop.
cd /opt/sbt
mkdir sbtlaunch   #store sbt-launch.jar
```

1. 下载sbt-launch.jar,并存放至/opt/sbt/sbtlaunch

```sh
cd /opt/sbt/sbtlaunch
wget https://repo.typesafe.com/typesafe/ivy-releases/org.scala-sbt/sbt-launch/0.13.9/sbt-launch.jar -O ./sbt-launch.jar   #download sbt-launch.jar
unzip -q ./sbt-launch.jar #解压
$ 
```

# 3. zeppelin常用命令

```sh
sudo vim conf/zeppelin-site.xml
bin/zeppelin-daemon.sh restart
vim logs/zeppelin-xxxxx-Pro.local.log
```

## <a name='local'></a>3.1. 在local模式下运行

```sh
tar -xvf flink-1.10.0-bin-scala_2.11.tgz
```

**minicluster** 的端口为 **8081**

查看log

```sh
cd ../zeppelin-0.9.0-SNAPSHOT
vim logs/zeppelin-自动补全？
```

tab键自动补全命令

## <a name='remote'></a>3.2. 在remote模式下运行

flink.excution.mode设置为remote
flink.excution.remote.host设置为localhost
flink.excution.remote.port设置为**8081**

## <a name='yarn'></a>3.3. yarn模式下的运行

确保hadoop已经安装

```sh
hadoop classpath
```

获得hadoop的所有jar

```sh
echo $HADOOP_CONF_DIR
```

* FLINK_HOME设置为/Users/xxx/xxx/flink-1.10.0
* flink.excution.mode设置为yarn
* flink.excution.remote.host设置为localhost
* flink.excution.remote.port设置为8081
* flink.jm.memory设置为1024
* flink.tm.memory设置为1024
* flink.tm.slot设置为2
* local.number-taskmanager设置为4
* flink.yarn.appName设置为Zeppelin Flink Session
* flink.yarn.queue设置为default
* zeppelin.flink.maxResult设置为1000
* zeppelin.pyflink.python设置为/Users/xxx/anaconda3/bin/python

```sh
ps aux | grep RemoteInterpreterServer
```

flink的classpath

## <a name='inlineconfiguration'></a>3.4. inline configuration

一定要在进程起来前跑

```sql
%flink.conf
flink.execution.mode yarn
```

## <a name='hive'></a>3.5. hive

常用命令：

```sh
bin/hive # 开启进程
```

```sh
show tables 
```

```sh
quit # 退出
```

先要copy一些jar(不同版本，要copy的jar不同)：

```sh
cp ~/flink-connector-hive-2.11-1.10.0.jar ~/Flink_Videos/flink-1.10.0/lin
cp lib/hive-exec-2.3.4.jar  ~/Flink_Videos/flink-1.10.0/lin
```

```sh
cd conf
pwd
```

把目录copy下来，放到配置页面

```sql
%flink.bsql
show tables;
select * from bank;
```

## <a name='SQL'></a>3.6. SQL

```sql
%flink.bsql
show tables;
--this is a comment
showfunctions
```

```sql
%flink.ssql
show tables;
--this is a comment
showfunctions
```

## <a name='Streaming'></a>3.7. Streaming

采用Flink Job Control Tutorial进行学习：

single模式下：指select语句只有一行

这里必须用到html的模板，

{0}指代max(rowtime)

{1}指代count(*)

默认的刷新频率是3秒

```sql
% flink.ssql(type = single, refreshInterval = 1000, template = Total count is <h1>{1}</h1> <br> {0})

select max(rowtime), count(*) from log
```

update模式下：每一次更新数据，都是对原来的数据做一次update

默认是table模式，不需要制定template

```sql
%flink.ssql(type)

select url, count(1) as c from log group by url
```

append模式下：会得到时间序列时间。第一个字段，select字段，必须是时间。

settings的设置：

keys：永远设置为时间

values：设置为PV值

groups：设置为URL(home search product)

threshold: 默认保留一个小时的数据，但也可以设置为60000，表示保留1分钟的数据

以5秒为一个窗口的单元，查看5秒以内，每一个窗口的pv：

```sql
%flink.ssql(type = append, threshold)
select TUMBEL_START(rowtime, INTERVAL '5' SECOND) as
start_time, url, count(1) as pv from log group by
TUMBLE(rowtime, INTERVAL '5' SECOND), url
```

## <a name='kafka-1'></a>3.8. kafka

## <a name='python'></a>3.9. python

## <a name='spark-1'></a>3.10. spark

首先确认Zeppelin的机器上已安装有Hadoop客户端和Spark客户端，

能通过Hadoop客户端连接HDFS，

通过Spark客户端提交任务给YARN。

```sh
cd zeppelin-0.9.0-bin-all
vi conf/zeppelin-env.sh

# 在zeppelin-env.sh文件中找到SPARK_HOME和HADOOP_CONF_DIR两项配置，修改成实际的路径
export SPARK_HOME=/opt/cloudera/parcels/CDH/lib/spark
export HADOOP_CONF_DIR=/etc/hadoop/conf
# 重启Zeppelin
./bin/zeppelin-daemon.sh restart
```

选择Interpreter

搜索spark

将spark.master配置成yarn-client，其他可以暂时保持不变。

验证测试

```sql
%spark
import org.apache.hadoop.fs.{FileSystem, Path}
val fs = FileSystem.get(sc.hadoopConfiguration)
val dirSize = fs.getContentSummary(new Path("hdfs:///user/root")).getLength
```

## <a name='flink-Pythonenv-Conda'></a>3.11. flink - Python env - Conda

### <a name='-1'></a>3.11.1. 准备工作

本文内容就是在 Zeppelin notebook 里利用 Conda 来创建 Python env 自动部署到 Yarn 集群中，无需手动在集群上去安装任何 Pyflink 的包，并且可以在一个 Yarn 集群里同时使用多个版本的 PyFlink。

下载 Flink 1.13， 需要注意的是，本文的功能只能用在 Flink 1.13 以上版本，

然后：

把 **flink-Python-*.jar** 这个 jar 包 copy 到 **Flink 的 lib 文件夹**下；

把 **opt/Python** 这个文件夹 copy 到 **Flink 的 lib 文件夹**下。

安装以下软件 (这些软件是用于创建 Conda env 的)：

* [miniconda](https://docs.conda.io/en/latest/miniconda.html)

* [conda pack](https://conda.github.io/conda-pack/)

* [mamba](https://github.com/mamba-org/mamba)

### <a name='PyFlink'></a>3.11.2. 搭建 PyFlink 环境

接下来就可以在 Zeppelin 里搭建并且使用 PyFlink 了。

#### <a name='Step1.JobManagerPyFlinkConda'></a>3.11.2.1. Step 1. 制作 **JobManager** 上的 **PyFlink Conda** 环境

因为 Zeppelin 天生支持 Shell，

所以可以在 Zeppelin 里用 Shell 来制作 PyFlink 环境。

注意这里的 **Python 第三方包**是在 **PyFlink 客户端 (JobManager)** 需要的包，

比如 Matplotlib 这些，并且确保至少安装了下面这些包：

某个版本的 **Python (这里用的是 3.7）**

**apache-flink (这里用的是 1.13.1)**

**jupyter，grpcio，protobuf** (这三个包是 Zeppelin 需要的)

剩下的包可以根据需要来指定：

```sql
%sh

# make sure you have conda and momba installed.
# install miniconda: https://docs.conda.io/en/latest/miniconda.html
# install mamba: https://github.com/mamba-org/mamba

echo "name: pyflink_env
channels:
- conda-forge
- defaults
dependencies:
- Python=3.7
- pip
- pip:
  - apache-flink==1.13.1
- jupyter
- grpcio
- protobuf
- matplotlib
- pandasql
- pandas
- scipy
- seaborn
- plotnine
" > pyflink_env.yml
   
mamba env remove -n pyflink_env
mamba env create -f pyflink_env.yml
```

运行下面的代码打包 PyFlink 的 **Conda 环境**并且**上传**到 **HDFS** (注意这里打包出来的文件格式是 tar.gz)：

```sql
%sh

rm -rf pyflink_env.tar.gz
conda pack --ignore-missing-files -n pyflink_env -o pyflink_env.tar.gz

hadoop fs -rmr /tmp/pyflink_env.tar.gz
hadoop fs -put pyflink_env.tar.gz /tmp
# The Python conda tar should be public accessible, so need to change permission here.
hadoop fs -chmod 644 /tmp/pyflink_env.tar.gz
```

#### <a name='Step2.TaskManagerPyFlinkConda'></a>3.11.2.2. Step 2. 制作 TaskManager 上的 PyFlink Conda 环境

运行下面的代码来创建 **TaskManager 上的 PyFlink Conda 环境**，

TaskManager 上的 PyFlink 环境**至少包含以下 2 个包**：

* 某个版本的 Python (这里用的是 3.7）

* apache-flink (这里用的是 1.13.1)

剩下的包是 **Python UDF** 需要依赖的包，比如这里指定了 **pandas**

```sql
%sh

echo "name: pyflink_tm_env
channels:
- conda-forge
- defaults
dependencies:
- Python=3.7
- pip
- pip:
  - apache-flink==1.13.1
- pandas
" > pyflink_tm_env.yml
   
mamba env remove -n pyflink_tm_env
mamba env create -f pyflink_tm_env.yml
```

运行下面的代码打包 PyFlink 的 Conda 环境并且上传到 HDFS (注意这里使用的是 zip 格式）：

```sql
%sh

rm -rf pyflink_tm_env.zip
conda pack --ignore-missing-files --zip-symlinks -n pyflink_tm_env -o pyflink_tm_env.zip

hadoop fs -rmr /tmp/pyflink_tm_env.zip
hadoop fs -put pyflink_tm_env.zip /tmp
# The Python conda tar should be public accessible, so need to change permission here.
hadoop fs -chmod 644 /tmp/pyflink_tm_env.zip
```

#### <a name='Step3.PyFlinkConda'></a>3.11.2.3. Step 3. 在 PyFlink 中使用 Conda 环境

接下来就可以在 Zeppelin 中使用上面创建的 Conda 环境了，

首先需要在 Zeppelin 里配置 Flink，主要**配置的选项**有：

* **flink.execution.mode** 为 **yarn-application**, 本文所讲的方法只适用于 **yarn-application 模式**；

* 指定 **yarn.ship-archives**，**zeppelin.pyflink.Python** 以及 **zeppelin.interpreter.conda.env.name** 来配置 **JobManager** 侧的 **PyFlink Conda 环境**；

* 指定 **Python.archives** 以及 **Python.executable** 来指定 **TaskManager** 侧的 **PyFlink Conda 环境**；

* 指定其他**可选的 Flink 配置**，比如这里的 **flink.jm.memory** 和 **flink.tm.memory**。

```sql
%flink.conf


flink.execution.mode yarn-application

yarn.ship-archives /mnt/disk1/jzhang/zeppelin/pyflink_env.tar.gz
zeppelin.pyflink.Python pyflink_env.tar.gz/bin/Python
zeppelin.interpreter.conda.env.name pyflink_env.tar.gz

Python.archives hdfs:///tmp/pyflink_tm_env.zip
Python.executable pyflink_tm_env.zip/bin/Python3.7

flink.jm.memory 2048
flink.tm.memory 2048
```

接下来就可以如一开始所说的那样在 Zeppelin 里使用 **PyFlink 以及指定的 Conda 环境**了。有 2 种场景:

下面的例子里，可以在 PyFlink 客户端 (JobManager 侧)

使用上面创建的 JobManager 侧的 Conda 环境，

比如下边使用了 **Matplotlib**。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.u24vlra1yzk.png)

下面的例子是在 **PyFlink UDF** 里使用上面创建的 **TaskManager 侧 Conda 环境里的库**，

比如下面在 UDF 里使用 **Pandas**。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5s0w557gjm80.png)

## <a name='ApacheSedona'></a>3.12. Apache Sedona

[Apache Sedona](https://github.com/apache/incubator-sedona)(孵化)是一个用于处理大规模空间数据的集群计算系统。

Sedona通过一组开箱使用的空间弹性分布式数据集(srdd)/

SpatialSQL扩展了Apache Spark / SparkSQL，

可以有效地跨机器加载、处理和分析大规模空间数据。

| Name  |  API |  介绍|
|---|---|---|
|Core  | RDD  | SpatialRDD 和查询运算符。 |
|SQL  | SQL/DataFrame  |Sedona 核心的 SQL 接口。|
|Viz |  RDD, SQL/DataFrame | 空间 RDD 和 DataFrame 的可视化|
|Zeppelin |  Apache Zeppelin | Apache Zeppelin 0.8.1+ 插件|

这是一个[jupyter示例](https://mybinder.org/v2/gh/apache/incubator-sedona/HEAD?filepath=binder)

可以安装在[zeppelin](https://github.com/apache/incubator-sedona/tree/master/zeppelin)上

## <a name='oracle'></a>3.13. oracle (貌似不太常用)

## <a name='oracle-1'></a>3.14. 简单介绍oracle

Oracle数据库中的空间和图形特性

Oracle数据库现在包括**机器学习**，**空间**和**图形功能**。

如果你有Oracle数据库许可证，你可以使用所有行业领先的机器学习、空间和图形功能，

在**本地**和**Oracle云数据库**服务中进行开发和部署

一些应用：

使用[oracle](https://www.jianshu.com/p/08afbdc63848/)作为数据源发布图层到[geoserver](https://docs.geoserver.org/latest/en/user/data/database/oracle.html)

### <a name='Oracle'></a>3.14.1. 连接Oracle数据库

简单来说，步骤如下：

1. 下载ojdbc8.jar
2. 创建新jdbc解释器
3. 配置jdbc参数
4. 测试新解释器

1. 进入 Interpreters page.
2. 创建 new jdbc Interpreter.
3. 配置参数。

```sql
default.driver oracle.jdbc.driver.OracleDriver
default.url  jdbc:oracle:thin:@//host:port/servicename
default.user  database_user
default.password password
artifact   /opt/oracle/ojdbc8.jar
```

用新的解释器创建新的notbook绑定。

```sql

```

```sql

```

```sql

```

# 4. 高阶技巧

[使用 Flink 前需要知道的 10 个『陷阱』](https://mp.weixin.qq.com/s/iQdYaChIftZckyXRy3tZ0g)

[我司Kafka+Flink+MySQL生产完整案例代码](https://mp.weixin.qq.com/s/enbuh3BGp1ocAlCoSyQysQ)，这个案例用的是java

[【Flink】第二十六篇：源码角度分析Task执行过程](https://mp.weixin.qq.com/s/BOxSh3YltFrrT_IupQAB6Q)，这个案例用的是java

[实时数仓 | Flink实时维表join方法总结（附项目源码）](https://mp.weixin.qq.com/s/X3YYm9psakwF-HamjCvKBg)，这个案例用的是java
