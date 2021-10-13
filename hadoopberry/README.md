Project to Design a Hadoop/Spark [Raspberry Pi 4 Cluster](https://github.com/YutingYao/pi-cluster) for Distributed Computing.

An efficient quick-start tool to build [a Raspberry Pi (or Debian-based) Cluster](https://github.com/YutingYao/RaspPi-Cluster) with popular ecosystem like Hadoop, Spark

Repo for instructions on setting up a micro compute cluster with the [NVidia Jetson Nano boards](https://github.com/YutingYao/JetsonCluster) and potentially Ansible playbooks for configuration and setup.

Setting up a [K3s Kubernetes](https://github.com/YutingYao/jetsonnano-k3s-gpu) cluster on my Nvidia Jetson Nano

Cluster made out of [Nvidia Jetson Nano's](https://github.com/YutingYao/NanoCluster)

# 1.烧录系统

## 1.1 三步走：

1. 下载树莓派ubuntu镜像-[Ubuntu Desktop 21.04](https://ubuntu.com/download/raspberry-pi/thank-you?version=21.04&architecture=desktop-arm64+raspi)，ubuntu镜像使用desktop版本
2. [SD卡格式化](https://www.sdcard.org/downloads/formatter/sd-memory-card-formatter-for-windows-download/)
3. [烧录系统](https://www.balena.io/etcher/)

设置language

将terminal和text放到桌面上

```sh
sudo apt install vim
```

## 1.2 安装输入法ibus 需要重启（但这一步,貌似不需要）

```sh
#ctrl+alt+t进入终端，输入ibus
ibus                              #检测iubs
sudo apt-get install ibus-pinyin  #安装输入法
ibus-setup     #添加输入法（pinyin）
ibus restart   #重启ibus
```

## 1.3 安装远程控制（但这一步,目前没有成功）

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

## 1.4 ubuntu免密SSH登录

准备工作：关闭防火墙（但这一步,貌似不需要）

```sh
systemctl statusfirewalld.service
```

```sh
systemctl stopfirewalld.service
```

```sh
systemctl disablefirewalld.service
```

配置主机名

为了方便，经常使用主机名替代IP机制，因此需要配置主机名和IP地址的对应关系。运行命令vi /etc/hosts，进行编辑配置，在文件末尾添加如下一行文字即可。

```sh
sudo vim /etc/hosts
```

据说，这一步需要ipv6 的字段删除

```sh
192.168.31.219 node01
192.168.31.6 node02
```

Debian貌似不需要net-tools，ubuntu需要net-tools

```sh
sudo apt install net-tools
```

```sh
ifconfig
```

### 1.4.1 打开ssh服务端（每一台计算机都需要）


A、B分别安装ssh：这一步可能不需要。


```sh
sudo apt-get install ssh
```

Ubuntu默认安装SSH server，这一步可能不需要。

```bash
sudo apt-get install openssh-server
```

Ubuntu默认安装SSH Client，这一步可能不需要。

```bash
sudo apt-get install openssh-client
```



开启Openssh服务：

```bash
sudo service ssh start
```

查看SSH服务运行状态：（也可以不查看）

```bash
service ssh status
```

### 1.4.2 免密登录-配置密钥对（每一条计算机都需要）

A、B分别生成公钥和私钥，输入命令，提示直接按enter即可：


生成自己的公钥私钥

★
```bash
sudo su
```

```bash
ssh-keygen -t rsa
```


生成之后会在用户的根目录生成一个 “.ssh”的文件夹

查看私钥id_rsa 和公钥id_rsa.pub：

★

```bash
cd ~/.ssh
```


生成以下几个文件:

* authorized_keys:存放远程免密登录的公钥,主要通过这个文件记录多台机器的公钥

* id_rsa : 生成的私钥文件

* id_rsa.pub ： 生成的公钥文件

* know_hosts : 已知的主机公钥清单



### 1.4.3 设置允许root远程登录（每一台计算机都需要）

因为scp是基于ssh的拷贝服务，

ssh在没有密钥登录的情况下，禁用了密码登录，

所以会出现无法拷贝文件，我们需要打开密码登录。

将/etc/ssh/sshd_config文件中。

在slave1和slave2上设置允许root远程登录：


```bash
sudo vim /etc/ssh/sshd_config
```

设置PermitRootLogin为yes

可选操作:禁用密码登陆

在公钥上传之后，我们现在可以禁用通过密码登陆SSH的方式了。为此，我们需要通过以下命令用文本编辑器打开/etc/ssh/ssh_config。

主要找到下面的几行，修改成下面的样子:

```s
PermitRootLogin prohibit-password 改为yes。
RSAAuthentication yes #这个貌似没找到
PubkeyAuthentication yes
AuthorizedKeysFile %h/.ssh/authorized_keys
```

配置完成后重启： 

```sh
 sudo service ssh restart
```

或

```bash
sudo /etc/init.d/ssh restart
```

由于本人多次操作失败，所以还修改了如下文件：

```sh
sudo find / -name ssh*config
sudo vim /etc/ssh/sshd_config
sudo vim /etc/ssh/ssh_config
sudo vim /usr/share/openssh/sshd_config
```


### 1.4.4 本地主机认证（其实，不认证本地主机也没有太大关系，主要是认证其他主机）

将公钥添加到本地主机认证中，执行下面的命令：

```bash
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
chmod 644 ~/.ssh/authorized_keys 
```
id_rsa.pub是公钥，id_rsa是私钥

然后测试：

```bash
ssh localhost 
```

先要输入yes，再来一遍：

```bash
ssh localhost 
```

无需密码即可登录则成功。


输入命令 exit 可退出ssh当前登录

```bash
exit
```





### 1.4.5 scp传输到其他机器上（由于本人多次操作失败，改用U盘拷贝）


**在 node01 上进行配置：ssh-copy-id**

```bash
sudo su
```

```bash
ssh-keygen -t rsa
```

```bash
ssh-copy-id -i node01 # 对自己也做一次
```

```bash
ssh-copy-id -i node02
```

```bash
ssh-copy-id -i node03
```

```bash
ssh-copy-id -i node04
```


**在 node02 上进行配置：ssh-copy-id**

```bash
sudo su
```

```bash
ssh-keygen -t rsa
```

```bash
ssh-copy-id -i node01 # 对自己也做一次
```

```bash
ssh-copy-id -i node02
```

```bash
ssh-copy-id -i node03
```

```bash
ssh-copy-id -i node04
```


**在 node03 上进行配置：ssh-copy-id**

```bash
sudo su
```

```bash
ssh-keygen -t rsa
```

```bash
ssh-copy-id -i node01 # 对自己也做一次
```

```bash
ssh-copy-id -i node02
```

```bash
ssh-copy-id -i node03
```

```bash
ssh-copy-id -i node04
```


**在 node04 上进行配置：ssh-copy-id**

```bash
sudo su
```

```bash
ssh-keygen -t rsa
```

```bash
ssh-copy-id -i node01 # 对自己也做一次
```

```bash
ssh-copy-id -i node02
```

```bash
ssh-copy-id -i node03
```

```bash
ssh-copy-id -i node04
```

**貌似SCP也可以：**

scp到合适的位置：

./就是当前目录下的意思，去掉也是可以的

```bash
scp ./id_rsa.pub root@node01:/home/pi
```

```bash
scp .ssh/id_rsa.pub chenlb@192.168.1.181:/home/chenlb/id_rsa.pub
```

```bash
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
```


**分别将node2和node3上id_rsa.pub内容拷贝至node1的authorized_keys文件中（这个还没试过）**

将node1的authorized_keys分别拷贝至node2和node3对应位置



```bash
ssh root@node01 'mkdir -p .ssh && cat >> .ssh/authorized_keys' < ~/.ssh/id_rsa.pub
```

```bash
ssh root@node02 'mkdir -p .ssh && cat >> .ssh/authorized_keys' < ~/.ssh/id_rsa.pub
```

```bash
ssh root@node01 'chmod 600 .ssh/authorized_keys'
```

```bash
ssh root@node02 'chmod 600 .ssh/authorized_keys'
```


**authorized_keys的权限要是600。**

```bash
 chmod 600 .ssh/authorized_keys
```


```bash
chmod 700 ~/.ssh/ 
chmod 640 ~/.ssh/authorized_keys
```

```bash
chmod 644 ~/.ssh/authorized_keys 
```

也就是是说，700更加高级耶
-rw------- (600)    只有拥有者有读写权限。
-rw-r--r-- (644)    只有拥有者有读写权限；而属组用户和其他用户只有读权限。
-rwx------ (700)    只有拥有者有读、写、执行权限。

**然后，就可以随意ssh啦~~~~**

```bash
ssh node01
```

```bash
ssh node02
```

```bash
ssh node03
```

```bash
ssh node04
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

## 2.1 zeppelin
下载[带有所有解释器的二进制包](https://dlcdn.apache.org/zeppelin/zeppelin-0.10.0/zeppelin-0.10.0-bin-all.tgz)

```sh
sudo su
# 安装到/opt目录下
tar -zxf zeppelin-0.10.0-bin-all.tgz -C /opt
chown -R root:root /opt/zeppelin-0.10.0-bin-all

cd /opt/zeppelin-0.10.0-bin-all/conf
cp zeppelin-env.sh.template zeppelin-env.sh
cp shiro.ini.template shiro.ini
 
# 修改默认端口，在zeppelin-env.sh中找到ZEPPELIN_PORT配置，修改成自己想要的
vim zeppelin-env.sh
export ZEPPELIN_PORT=18080         # port number to listen (default 8080)
 
# 配置简单登录认证，在shiro.ini中找到[users]配置，修改成自己想要的
vim shiro.ini
[users]
# List of users with their password allowed to access Zeppelin.
# To use a different strategy (LDAP / Database / ...) check the shiro doc at http://shiro.apache.org/configuration.html#Configuration-INISections
# To enable admin user, uncomment the following line and set an appropriate password.
admin = yyt123456, yaoyuting
#user1 = password2, role1, role2
#user2 = password3, role3
#user3 = password4, role2
 
# 重启Zeppelin 
cd /opt/zeppelin-0.9.0-bin-all/bin
./zeppelin-daemon.sh restart
 
# 浏览器访问
http://localhost:18080
使用yaoyuting/yyt123456登录
```

[Build from source](https://zeppelin.apache.org/docs/latest/setup/basics/how_to_build.html#build-requirements)

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

## 2.2 hadoop

[树莓派的Hadoop 3集群上的分布式TensorFlow](https://oliver-hu.medium.com/distributed-tensorflow-on-raspberry-pis-hadoop-3-cluster-603a164bb896)

### 2.2.1 在每个节点上安装 Java 8，使其成为每个节点的默认 Java。

```sh
sudo apt-get install openjdk-8-jdk
sudo update-alternatives --config java    // Select number corresponding to Java 8
sudo update-alternatives --config javac   // Select number corresponding to Java 8
```

### 2.2.2 下载 Hadoop，解压并授予 pi 所有权。

下载：

```sh
cd && wget https://www-us.apache.org/dist/hadoop/common/hadoop-3.2.1/hadoop-3.2.1.tar.gz
```

解压：

```sh
sudo tar -zxvf hadoop-2.7.3.tar.gz -C /root/training/
```

```sh
sudo tar -zxvf hadoop-3.1.4.tar.gz -C module/hadoop/
```

```sh
sudo tar -xvf hadoop-3.2.1.tar.gz -C /opt/
```

```sh
rm hadoop-3.2.1.tar.gz && cd /opt
```

mv是移动目录的意思：

```sh
sudo mv hadoop-3.2.1 hadoop
```

chown是change own的缩写：

```sh
sudo chown pi:pi -R /opt/hadoop 
```

x : 从 tar 包中把文件提取出来
z : 表示 tar 包是被 gzip 压缩过的，所以解压时需要用 gunzip 解压
v : 显示详细信息
f xxx.tar.gz :  指定被处理的文件是 xxx.tar.gz

### 2.2.3 配置 Hadoop 环境变量-bash

版本一：

```sh
sudo vim ~/.bashrc
```


*添加（在文件顶部插入）：

```sh
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-armhf/
export HADOOP_HOME=/opt/hadoop
export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export LD_LIBRARY_PATH=$HADOOP_HOME/lib/native:$LD_LIBRARY_PATH
```

版本二：

```sh
sudo vim /root/.bash_profile
```


*添加（在文件顶部插入）：


```sh
#设置Hadoop的家目录
HADOOP_HOME=/root/training/hadoop-2.7.3
export HADOOP_HOME

#将Hadoop的家目录添加到环境变量中
PATH=$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$PATH
export PATH
```

保存退出后，运行命令

```sh
source /root/.bash_profile
```

使配置的环境变量生效。


### 为 Hadoop 环境初始化 JAVA_HOME

```sh
sudo vim /opt/hadoop/etc/hadoop/hadoop-env.sh
```

*添加（在文件顶部插入）：

版本一：

```sh
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-armhf/
```

版本二：

```sh
export JAVA_HOME=/root/training/jdk1.8.0_144
```

版本三：

```sh
export JAVA_HOME=/usr/java/jdk1.8.0_13
```

版本四：

```sh
export  JAVA_HOME=/usr/java/jdk1.8.0_281/
```


**此外，可能还需要修改**

```sh
sudo vim yarn-env.sh
```

JAVA_HOME=/usr/java/jdk1.8.0_131

JAVA_HOME=/usr/java/jdk1.8.0_231

在Master节点的workers文件中指定Slave节点，也就是node02

```sh
sudo vim slaves
```

chenc02
chenc03

```sh
sudo vim workers 
```

node02

### 验证 Hadoop 安装。

```sh
source ~/.bashrc
cd && hadoop version | grep Hadoop
Hadoop 3.2.1
```

设置 Hadoop 集群
hadoop-env.sh           # java的环境变量
yarn-env.sh             # 制定yarn框架的Java运行环境
slaves                  # 指定datanode数据存储服务器
core-site.xml           # hadoop-web界面路径
hdfs-site.xml           # 文件系统的配置文件
mapred-site.xml         # mapreducer 任务配置文件
yarn-site.xml           # yarn框架配置，主要一些任务的启动位置


### core-site.xml文件的配置

这个是hadoop的核心配置，这里需要配置两属性， 

fs.default.name 配置hadoop的HDFS系统命令，位置为主机的9000端口， 

hadoop.tmp.dir 配置haddop的tmp目录的根位置。

```sh
sudo vim /opt/hadoop/etc/hadoop/core-site.xml
```

修改文件结尾为：
版本一：

```xml
<configuration>
  <property>
    <name>fs.defaultFS</name>
    <value>hdfs://pi1:9000</value>
  </property>
</configuration>
```

版本二：

配置NameNode的地址，9000是进行RPC通信的端口号

```xml
<property>
<name>fs.defaultFS</name>
<value>hdfs://hadoop222:9000</value>
</property>
```

注意：该参数一定要进行配置，Linux的tmp目录是临时目录，当系统重启后数据会丢失

HDFS数据保存在Linux的哪个目录，默认路径是Linux的tmp目录
```xml
<property>
<name>hadoop.tmp.dir</name>
<value>/root/training/hadoop-2.7.3/tmp</value>
</property>
```

版本三：


```xml
<configuration>
<property>
        <name>fs.defaultFS</name>
        <value>hdfs://chenc01:9000</value>
</property>

<property>
        <name>io.file.buffer.size</name>
        <value>131072</value>
</property>

<property>
        <name>hadoop.tmp.dir</name>
        <value>file:/home/hadoop/tmp</value>
        <description>Abase for other tmporary directries.</description>
</property>
</configuration>
```

### hdfs-site.xml文件的配置

HDFS主要的配置文件， dfs.http.address配置了hdfs的http的访问位置；

dfs.replication 配置文件的副本，一般不大于从机个数。

```sh
pi@pi1:~$ sudo mousepad /opt/hadoop/etc/hadoop/hdfs-site.xml
```

修改文件结尾为：

版本一：

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

版本二：

注意：dfs.replication参数配置的原则，

一般情况下，数据块的冗余度跟数据节点（DataNode）的个数保持一致，最大不超过3

数据块的冗余度，默认为3，伪分布模式仅有一台机器，就只能设置为1

```xml
<property>
<name>dfs.replication</name>
<value>3</value>
</property>
```

是否开启HDFS的权限检查，默认为true

```xml
<property>
<name>dfs.permissions</name>
<value>false</value>
</property>
```

版本三：

```xml
<configuration>
<property>
<configuration>
<property>
        <name>dfs.namenode.secondary.http-address</name>
        <value>chenc01:9000</value>
</property>

<property>
        <name>dfs.namenode.name.dir</name>
        <value>file:/home/hadoop/dfs/name</value>
</property>

<property>
        <name>dfs.datanode.data.dir</name>
        <value>file:/home/hadoop/dfs/data</value>
</property>

<property>
        <name>dfs.replication</name>
        <value>2</value>
</property>

<property>
        <name>dfs.webhdfs.enabled</name>
        <value>true</value>
</property>
</configuration>
```

### mapred-site.xml文件的配置：

这个是mapreduce任务配置文件，

mapreduce.framework.name 属性下配置yarn,

mapred.map.tasks和mapred.reduce.tasks 分别为map和reduce 的任务数。

同时指定hadoop历史服务器hsitoryserver

我们可以通过historyserver查看mapreduce的作业记录，

比如用了多少个map,用了多少个reduce，

作业启动时间，作业完成时间。

默认清空下，hadoop历史服务器是没有启动的，我们需要通过命令来启动:

```sh
/home/hadoop/hadoop-3.1.3/sbin/mr-jobhistory-daemon.sh  start historyserver
```


注意：默认情况下是没有mapred-site.xml文件的，
但有mapred-site.xml.template文件，
运行命令cp mapred-site.xml.templatemapred-site.xml拷贝一份即可。

```sh
sudo vim /opt/hadoop/etc/hadoop/mapred-site.xml
```

修改文件结尾为：

版本一：

```xml
<configuration>
  <property>
    <name>mapreduce.framework.name</name>
    <value>yarn</value>
  </property>
</configuration>
```

版本二：

MapReduce程序运行使用的框架

```xml
<property>
<name>mapreduce.framework.name</name>
<value>yarn</value>
</property>
```

版本三：

```xml
<configuration>
<property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
</property>

<property>
        <name>mapreduce.jobhistory.address</name>
        <value>chenc01:10020</value>
</property>

<property>
        <name>mapreduce.jobhistory.webapp.address</name>
        <value>chenc01:19888</value>
</property>
</configuration>
```

### yarn-site.xml文件的配置：

yarn框架的配置，主要是一些任务的启动位置

```sh
sudo vim /opt/hadoop/etc/hadoop/yarn-site.xml
```

修改文件结尾为：

版本一：

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

版本二：

Yarn的主节点ResourceManager的位置

MapReduce程序的运行方式：shuffle洗牌
```xml
<property>
<name>yarn.nodemanager.aux-services</name>
<value>mapreduce_shuffle</value>
</property>
```

```xml
<property>
<name>yarn.resourcemanager.hostname</name>
<value>hadoop221</value>
</property>
```

版本三：

```xml
<configuration>
<!-- Site specific YARN configuration properties -->
<proetry>
        <name>yarn.nodemanager.aux-service</name>
        <value>mapreduce_shuffle</value>
</proetry>

<proetry>
        <name>yarn.nodemanager.uax-service.mapreduce.shuffle.class</name>
        <value>org.apache.hadoop.mapreduced.ShuffleHandle</value>
</proetry>
<proetry>
        <name>yarn.resoucemanager.address</name>
        <value>chenc01:8032</value>
</proetry>
<proetry>
        <name>yarn.resourcemanager.shceduler.address</name>
        <value>chenc01:8030</value>
</proetry>
<proetry>
        <name>yarn.resourcemanager.resource-tracker.address</name>
        <value>chenc01:8031</value>
</proetry>

<proetry>
        <name>yarn.resourcemanager.admin.address</name>
        <value>chenc01:8033</value>
</proetry>

<proetry>
        <name>yarn.resourcemanager.webapp.address</name>
        <value>chenc01:8088</value>
</proetry>
</configuration>
```


### 创建 Datanode 和 Namenode 目录。

```sh
sudo mkdir -p /opt/hadoop_tmp/hdfs/datanode
sudo mkdir -p /opt/hadoop_tmp/hdfs/namenode
sudo chown pi:pi -R /opt/hadoop_tmp
```


### 格式化NameNode-格式化HDFS

配置完成后，运行命令，一般第一次的时候需要初始化，之后就不需要了

版本一：

```sh
hdfs namenode –format
hdfs namenode -format
```

可能出现创建文件夹失败的问题，这个权限问题，

使用 root 账号使用命令sudo chmod -R a+w /绝对路径。

初始化HDFS失败都要把之前创建的文件夹给删除。

版本二：

```sh
hdfs namenode -format -force
```

版本三：

```sh
cd /home/hadoop/hadoop-3.1.3/bin/
./hdfs namenode -format
# 查看是否生成相应的内容

cd /home/hadoop/dfs/
ls
tree
```

### 把主节点上配置好的hadoop目录复制到从节点上：

版本一：

```sh
scp -r hadoop-2.7.3/ root@hadoop223:/root/training
```

```sh
scp -r hadoop-2.7.3/ root@hadoop224:/root/training
```

版本三：

```sh
scp -r /home/hadoop/hadoop-3.13 hadoop@chenc02:~/
scp -r /home/hadoop/hadoop-3.13 hadoop@chenc03:~/
```

### 最后，在主节点hadoop222上运行命令

直接执行start-all.sh，启动 Hadoop。

此时 node02上的相关服务也会被启动：

```sh
start-all.sh
```


### 启动HDFS，验证功能。

```sh
start-dfs && start-yarn.sh
```

使用jps命令验证设置。在每台服务器上使用 jps 命令查看服务进程，

```sh
jps
```

创建临时目录以测试文件系统：

```sh
hadoop fs -mkdir /tmp
hadoop fs -ls /
```

使用以下命令停止单节点群集：

```sh
stop-dfs && stop-yarn.sh
```

### Web查看集群状态

或直接进入 Web-UI 界面进行查看，端口为 9870。可以看到此时有一个可用的 Datanode：

接着可以查看 Yarn 的情况，端口号为 8088 ：

浏览器输入http://10.0.0.61:8088/cluster

至此，Hadoop分布式集群搭建成功。

### 静默警告（由于使用了32位Hadoop构建和64位操作系统）

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
cd && wget https://www-us.apache.org/dist/spark/spark-2.4.4/spark-2.4.4-bin-hadoop-2.7.tgz
sudo tar -xvf spark-2.4.4-bin-hadoop-2.7.tgz -C /opt/
rm spark-2.4.4-bin-hadoop-2.7.tgz && cd /opt
sudo mv spark-2.4.4-bin-hadoop-2.7 spark
sudo chown pi:pi -R /opt/spark
```

配置Spark环境变量。

```sh
sudo vim ~/.bashrc
```

添加（在文件顶部插入）：

```sh
export SPARK_HOME=/opt/spark
export PATH=$PATH:$SPARK_HOME/bin
```

验证Spark安装。

```sh
source ~/.bashrc
spark-shell --version
```

配置Spark作业监控。

与Hadoop类似，Spark还提供监视您部署的作业的功能。但是，使用Spark，我们必须手动配置监控选项。

生成并修改Spark默认配置文件：

```sh
cd $SPARK_HOME/conf
sudo mv spark-defaults.conf.template spark-defaults.conf
vim spark-defaults.conf
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
/opt/spark/conf$ cd
hdfs dfs -mkdir /spark-logs
```

启动Spark历史服务器。

```sh
$SPARK_HOME/sbin/start-history-server.sh
```

Spark历史服务器界面可以通过 http://pi1:18080 访问

![spark历史服务器](https://github.com/YutingYao/pi-cluster/blob/master/pictures/spark-history-ui.png)

运行示例作业（计算pi）

```sh
spark-submit --deploy-mode client --class org.apache.spark.examples.SparkPi $SPARK_HOME/examples/jars/spark-examples_2.11-2.4.4.jar 7
```

## pyspark

```sh
```

## flink

```sh
```

## postgresql

```sh
```

# zeppelin常用命令：

sudo vim conf/zeppelin-site.xml
bin/zeppelin-daemon.sh restart
cat logs/zeppelin-xxxxx-Pro.local.log

## 在local模式下运行：

tar -xvf flink-1.10.0-bin-scala_2.11.tgz
minicluster 的端口为 8081

查看log
cd ../zeppelin-0.9.0-SNAPSHOT
cat logs/zeppelin-自动补全？

## 在remote模式下运行：

## yarn模式下的运行：

 确保hadoop已经安装

hadoop classpath
获得hadoop的所有jar

echo $HADOOP_CONF_DIR


FLINK_HOME设置为/Users/xxx/xxx/flink-1.10.0
flink.excution.mode设置为yarn
flink.excution.remote.host设置为localhost
flink.excution.remote.port设置为8081
flink.jm.memory设置为1024
flink.tm.memory设置为1024
flink.tm.slot设置为2
local.number-taskmanager设置为4
flink.yarn.appName设置为Zeppelin Flink Session
flink.yarn.queue设置为default
zeppelin.flink.maxResult设置为1000
zeppelin.pyflink.python设置为/Users/xxx/anaconda3/bin/python


ps aux | grep RemoteInterpreterServer
flink的classpath

## hive：

## SQL

## Streaming

## kafka

## python