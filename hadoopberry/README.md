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

### 1.4.1 打开ssh服务端

```bash
sudo apt-get update
```

A、B分别安装ssh：

★

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

打开"终端窗口"，输入"sudo ps -e |grep ssh"-->回车-->有sshd,说明ssh服务已经启动，如果没有启动，输入"sudo service ssh start"-->回车-->ssh服务就会启动

开启Openssh服务：

```bash
sudo service ssh start
```

查看SSH服务运行状态：

```bash
service ssh status
```

### 1.4.2 免密登录-配置密钥对

A、B分别生成公钥和私钥，输入命令，提示直接按enter即可：

配置本机SSH无密码登录：

每台主机上执行 ssh-keygen -t rsa

生成自己的公钥私钥

★

```bash
ssh-keygen -t rsa
```

在运行完以上命令了以后，我们需要回答一系列的问题。首先选择保存密钥的路径，按回车将会选择默认路径即家目录的一个隐藏的.ssh文件夹。下一个提示是请输入口令提醒。我个人将此留空（直接回车）。之后密钥对就会创建，大功告成。

生成之后会在用户的根目录生成一个 “.ssh”的文件夹

查看私钥id_rsa 和公钥id_rsa.pub：

★

```bash
cd ~/.ssh
```

```bash
ll
```

进入“.ssh”会生成以下几个文件:

* authorized_keys:存放远程免密登录的公钥,主要通过这个文件记录多台机器的公钥

* id_rsa : 生成的私钥文件

* id_rsa.pub ： 生成的公钥文件

* know_hosts : 已知的主机公钥清单

有机器A(192.168.1.155)，B(192.168.1.181)。现想A通过ssh免密码登录到B。

### 1.4.3 设置允许root远程登录

因为scp是基于ssh的拷贝服务，

ssh在没有密钥登录的情况下，禁用了密码登录，

所以会出现无法拷贝文件，我们需要打开密码登录。

将/etc/ssh/sshd_config文件中的PermitRootLogin prohibit-password 改为yes。

在slave1和slave2上设置允许root远程登录：

```bash
vim /etc/ssh/sshd_config
```

设置PermitRootLogin为yes

可选操作:禁用密码登陆

在公钥上传之后，我们现在可以禁用通过密码登陆SSH的方式了。为此，我们需要通过以下命令用文本编辑器打开/etc/ssh/ssh_config。

主要找到下面的三行，修改成下面的样子:

```s
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

### 1.4.4 本地主机认证

将公钥添加到本地主机认证中，执行下面的命令：

```bash
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
chmod 644 ~/.ssh/authorized_keys 
```

然后测试：ssh localhost 无需密码即可登录则成功。


id_rsa.pub是公钥，id_rsa是私钥

之后scp传输到其他机器上

scp ./id_rsa.pub zkx@master:/home/zkx

在master机器上将id_rsa.pub的内容写入.ssh目录下的authorized_keys

cat ./id_rsa.pub >> ./.ssh/authorized_keys

之后删除id_rsa.pub



1，把A机下的id_rsa.pub复制到B机下，在B机的.ssh/authorized_keys文件里，我用scp复制。

```bash
 scp .ssh/id_rsa.pub chenlb@192.168.1.181:/home/chenlb/id_rsa.pub
```

B机把从A机复制的id_rsa.pub添加到.ssh/authorzied_keys文件里。

```bash
cat id_rsa.pub >> .ssh/authorized_keys
```

　　authorized_keys的权限要是600。

```bash
 chmod 600 .ssh/authorized_keys
```

小结：登录的机子可有私钥，被登录的机子要有登录机子的公钥。这个公钥/私钥对一般在私钥宿主机产生。上面是用rsa算法的公钥/私钥对，当然也可以用dsa(对应的文件是id_dsa，id_dsa.pub)

想让A，B机无密码互登录，那B机以上面同样的方式配置即可。



```bash
ssh-keygen
```

（2）在node1上执行 cat ~/.ssh/id_rsa.pub >>~/.ssh/authorized_keys

将自己公钥加入授权文件

按四次回车

```bash
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
```


配置ssh免密登录。进入当前用户（最好不要用root）的home目录，生成本机秘钥。命令：

```bash
cd ;ssh-keygen -t rsa -P
```

上面的命令需要在master，slave1和slave2上执行，在不同节点上执行时，需要修改ssh命令后的xx@xx.xx为其他的两个节点。
修改完之后，在master上使用下面的命令测试是否配置成功

```bash
ssh ambari_slave2
```

然后一路回车就可以.

将公钥追加到 authorized_keys 文件中。命令：

```bash
ll
```

如果在ssh目录下：

```bash
cat ./id_rsa.pub >> ./authorized_keys
```

如果不在SSH目录下：

```bash
cat .ssh/id_rsa.pub >> .ssh/authorized_keys  
```

然后赋予authorized_keys 文件权限。命令：

```bash
chmod 600 .ssh/authorized_keys
```

输入命令 ssh localhost查看ssh是否配置成功。此时因为是第一次使用ssh登录本机，所以需要输入yes确认.
配置完成后，验证本机SSH无密码登录：

```bash
 ssh localhost
```

输入命令 exit 退出ssh当前登录，再输入命令 ssh localhost 发现不用命令也可以使用ssh登录本机了，大功告成

```bash
exit
```

配置两台主机之间SSH无密码登录 

在两台主机完成ssh server安装和本地ssh无密码登录之后，

以Master Host（heron01：192.168.201.136）

和Slave Host（heron02：192.168.201.135）为例，


可能需要自己修改文件的路径

```bash
chmod 700 ~/.ssh/ chmod 640 ~/.ssh/authorized_keys
```

（3）分别将node2和node3上id_rsa.pub内容拷贝至node1的authorized_keys文件中

（4）将node1的authorized_keys分别拷贝至node2和node3对应位置

完成免密登录

```bash
ssh root@ambari_slave1 'mkdir -p .ssh && cat >> .ssh/authorized_keys' < ~/.ssh/id_rsa.pub
```

```bash
ssh root@ambari_slave2 'mkdir -p .ssh && cat >> .ssh/authorized_keys' < ~/.ssh/id_rsa.pub
```

```bash
ssh root@ambari_slave1 'chmod 600 .ssh/authorized_keys'
```

```bash
ssh root@ambari_slave2 'chmod 600 .ssh/authorized_keys'
```

使用yitian用户，并完成配置完成两台主机之间的ssh无密码登录。

Master（heron01）无密码登陆Slave（heron02）：

在运行完以上命令了以后，我们需要回答一系列的问题。首先选择保存密钥的路径，按回车将会选择默认路径即家目录的一个隐藏的.ssh文件夹。下一个提示是请输入口令提醒。我个人将此留空（直接回车）。之后密钥对就会创建，大功告成。




```bash
ssh-copy-id user@ip_address #user换成自己的用户名，ip_address换成对应的主机ip地址
```

```bash
ssh-copy-id yitian@heron02
```

```bash
ssh heron02
```

```bash
 exit
```

Slave（heron02）无密码登陆Master（heron01）步骤同上：


```bash
ssh-copy-id yitian@heron01
```

```bash
ssh heron01
```

```bash
exit
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

