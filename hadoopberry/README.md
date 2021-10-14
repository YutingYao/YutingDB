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

修改完成以后，请重新启动Slave节点的Linux系统。

这样就完成了Master节点和Slave节点的配置，

然后，需要在各个节点上都执行如下命令，测试是否相互ping得通，

如果ping不通，后面就无法顺利配置成功：

```sh
ping Master -c 3   # 只ping 3次就会停止，否则要按Ctrl+c中断ping命令
ping Slave1 -c 3
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



# 2. 安装大数据分析软件

[大数据架构](http://dblab.xmu.edu.cn/blog/988-2/)请参考这个链接。

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

Hadoop 集群的安装配置大致包括以下步骤：

（1）步骤1：选定一台机器作为 Master；

（2）步骤2：在Master节点上创建hadoop用户、安装SSH服务端、安装Java环境；

（3）步骤3：在Master节点上安装Hadoop，并完成配置；

（4）步骤4：在其他Slave节点上创建hadoop用户、安装SSH服务端、安装Java环境；

（5）步骤5：将Master节点上的“/usr/local/hadoop”目录复制到其他Slave节点上；

（6）步骤6：在Master节点上开启Hadoop；

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

版本三：

```sh
export PATH=$PATH:/usr/local/hadoop/bin:/usr/local/hadoop/sbin
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

## 在Master节点的workers文件中指定Slave节点

版本一：

```sh
sudo vim slaves
```

chenc02
chenc03

版本二：

```sh
sudo vim workers 
```

node02

版本三：

```sh
sudo vim workers 
```

Slave1

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

版本四：

```xml
<configuration>
        <property>
                <name>fs.defaultFS</name>
                <value>hdfs://Master:9000</value>
        </property>
        <property>
                <name>hadoop.tmp.dir</name>
                <value>file:/usr/local/hadoop/tmp</value>
                <description>Abase for other temporary directories.</description>
        </property>
</configuration>
```

### hdfs-site.xml文件的配置

对于Hadoop的分布式文件系统HDFS而言，

一般都是采用冗余存储，冗余因子通常为3，

也就是说，一份数据保存三份副本。

但是，本教程只有一个Slave节点作为数据节点，

即集群中只有一个数据节点，数据只能保存一份，

所以 ，dfs.replication的值还是设置为 1。

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

版本四：

```xml
<configuration>
        <property>
                <name>dfs.namenode.secondary.http-address</name>
                <value>Master:50090</value>
        </property>
        <property>
                <name>dfs.replication</name>
                <value>1</value>
        </property>
        <property>
                <name>dfs.namenode.name.dir</name>
                <value>file:/usr/local/hadoop/tmp/dfs/name</value>
        </property>
        <property>
                <name>dfs.datanode.data.dir</name>
                <value>file:/usr/local/hadoop/tmp/dfs/data</value>
        </property>
</configuration>
```

### mapred-site.xml文件的配置：

“/usr/local/hadoop/etc/hadoop”目录下有一个mapred-site.xml.template，

需要修改文件名称，把它重命名为mapred-site.xml，

然后，把mapred-site.xml文件配置成如下内容：

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

版本四：

```xml
<configuration>
        <property>
                <name>mapreduce.framework.name</name>
                <value>yarn</value>
        </property>
        <property>
                <name>mapreduce.jobhistory.address</name>
                <value>Master:10020</value>
        </property>
        <property>
                <name>mapreduce.jobhistory.webapp.address</name>
                <value>Master:19888</value>
        </property>
        <property>
                <name>yarn.app.mapreduce.am.env</name>
                <value>HADOOP_MAPRED_HOME=/usr/local/hadoop</value>
        </property>
        <property>
                <name>mapreduce.map.env</name>
                <value>HADOOP_MAPRED_HOME=/usr/local/hadoop</value>
        </property>
        <property>
                <name>mapreduce.reduce.env</name>
                <value>HADOOP_MAPRED_HOME=/usr/local/hadoop</value>
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

版本五：

```xml
<configuration>
        <property>
                <name>yarn.resourcemanager.hostname</name>
                <value>Master</value>
        </property>
        <property>
                <name>yarn.nodemanager.aux-services</name>
                <value>mapreduce_shuffle</value>
        </property>
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

版本四：

```sh
cd /usr/local
sudo rm -r ./hadoop/tmp     # 删除 Hadoop 临时文件
sudo rm -r ./hadoop/logs/*   # 删除日志文件
tar -zcf ~/hadoop.master.tar.gz ./hadoop   # 先压缩再复制
cd ~
scp ./hadoop.master.tar.gz Slave1:/home/hadoop
```

然后在Slave1节点上执行如下命令：

```sh
sudo rm -r /usr/local/hadoop    # 删掉旧的（如果存在）
sudo tar -zxf ~/hadoop.master.tar.gz -C /usr/local
sudo chown -R hadoop /usr/local/hadoop
```

### 最后，在主节点hadoop222上运行命令



直接执行start-all.sh，启动 Hadoop。

此时 node02上的相关服务也会被启动：

```sh
start-all.sh
```


### 启动HDFS，验证功能。

现在就可以启动Hadoop了，启动需要在Master节点上进行，执行如下命令：、

```sh
start-dfs.sh
start-yarn.sh
mr-jobhistory-daemon.sh start historyserver
```

```sh
start-dfs && start-yarn.sh
```

使用jps命令验证设置。在每台服务器上使用 jps 命令查看服务进程，

```sh
jps
```

通过命令jps可以查看各个节点所启动的进程。

如果已经正确启动，则在Master节点上可以看到

* NameNode
* ResourceManager
* SecondrryNameNode
* JobHistoryServer

在Slave节点可以看到

* DataNode
* NodeManager

另外还需要在Master节点上通过命令：

```sh
hdfs dfsadmin -report
```

查看数据节点是否正常启动，

如果屏幕信息中的“Live datanodes”不为 0 ，则说明集群启动成功

也可以在Linux系统的浏览器中输入地址“http://master:9870/”，

通过 Web 页面看到查看名称节点和数据节点的状态。如果不成功，可以通过启动日志排查原因。

这里再次强调，伪分布式模式和分布式模式切换时需要注意以下事项：

（a）从分布式切换到伪分布式时，不要忘记修改slaves配置文件；

（b）在两者之间切换时，若遇到无法正常启动的情况，可以删除所涉及节点的临时文件夹，

这样虽然之前的数据会被删掉，但能保证集群正确启动。

所以，如果集群以前能启动，但后来启动不了，

特别是数据节点无法启动，不妨试着删除所有节点（包括Slave节点）上的“/usr/local/hadoop/tmp”文件夹，

再重新执行一次“hdfs namenode -format”，再次启动即可。

创建临时目录以测试文件系统：

```sh
hadoop fs -mkdir /tmp
hadoop fs -ls /
```

### 执行分布式实例

执行分布式实例过程与伪分布式模式一样，首先创建HDFS上的用户目录，命令如下：



```sh
hdfs dfs -mkdir -p /user/hadoop
```

然后，在HDFS中创建一个input目录，并把“/usr/local/hadoop/etc/hadoop”目录中的配置文件作为输入文件复制到input目录中，命令如下：

```sh
hdfs dfs -mkdir input
hdfs dfs -put /usr/local/hadoop/etc/hadoop/*.xml input
```

接着就可以运行 MapReduce 作业了，命令如下：

```sh
hadoop jar /usr/local/hadoop/share/hadoop/mapreduce/hadoop-mapreduce-examples-3.1.3.jar grep input output 'dfs[a-z.]+'
```

运行时的输出信息与伪分布式类似，会显示MapReduce作业的进度。

执行过程可能会有点慢，但是，如果迟迟没有进度，

比如5分钟都没看到进度变化，那么不妨重启Hadoop再次测试。

若重启还不行，则很有可能是内存不足引起，建议增大虚拟机的内存，

或者通过更改YARN的内存配置来解决。

在执行过程中，可以在Linux系统中打开浏览器，在地址栏输入“http://master:8088/cluster”，

通过Web界面查看任务进度，在Web界面点击 “Tracking UI” 这一列的History连接，可以看到任务的运行信息。



### 使用以下命令停止群集：

最后，关闭Hadoop集群，需要在Master节点执行如下命令：

```sh
stop-yarn.sh
stop-dfs.sh
mr-jobhistory-daemon.sh stop historyserver
```

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

## 2.3 scala


点击链接https://www.scala-lang.org/download/2.12.10.html，下载对应版本scala（本文选择scala 2.12.10）：

下载好后解压到：/usr/local/

```bash
sudo tar zxvf ~/Downloads/scala-2.12.10.tgz -C /usr/local/
```

 删除安装包：

```bash
rm ~/Downloads/scala-2.12.10.tgz
```

  进入到解压目录并重命名：

```bash
cd /usr/local/
sudo mv scala-2.12.10 scala
```

  配置环境变量：

```bash
sudo vim /etc/profile
```

```
export SCALA_HOME = 
export JAVA_HOME = 
export PATH = $PATH:$HOME/bin:$JAVA_HOME/bin:$SCALA_HOME/bin:/bin
```


 执行source命令并测试：

```bash
source /etc/profile
scala -version
```



## 2.4 spark

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


### 下载Spark，解包并授予pi所有权。

版本一：

点击链接 http://spark.apache.org/downloads.html 进行下载（本文选择2.4.4版本）：v

下载好后解压至/usr/local/：

```bash
sudo tar zxvf ~/Downloads/spark-2.4.4-bin-hadoop2.7.tgz  -C /usr/local

rm spark-2.4.4-bin-hadoop2.7.tgz 
```

进入到解压目录并重命名：

```bash
cd /usr/local/
sudo mv spark-2.4.4-bin-hadoop2.7 spark
```


版本二：

```sh
cd && wget https://www-us.apache.org/dist/spark/spark-2.4.4/spark-2.4.4-bin-hadoop-2.7.tgz
sudo tar -xvf spark-2.4.4-bin-hadoop-2.7.tgz -C /opt/
rm spark-2.4.4-bin-hadoop-2.7.tgz && cd /opt
sudo mv spark-2.4.4-bin-hadoop-2.7 spark
sudo chown pi:pi -R /opt/spark
```

版本三：

```sh
sudo tar -zxf ~/下载/spark-2.1.0-bin-without-hadoop.tgz -C /usr/local/
cd /usr/local
sudo mv ./spark-2.1.0-bin-without-hadoop/ ./spark
sudo chown -R hadoop:hadoop ./spark          # 此处的 hadoop 为你的用户名
```

版本四：

```sh
sudo tar -zxf ~/下载/spark-2.0.2-bin-without-hadoop.tgz -C /usr/local/
cd /usr/local
sudo mv ./spark-2.0.2-bin-without-hadoop/ ./spark
sudo chown -R hadoop ./spark
```

### 配置Spark环境变量。



```bash
sudo vim /etc/profile
```


```sh
sudo vim ~/.bashrc
```

添加（在文件顶部插入）：

```sh
export SPARK_HOME=/opt/spark
export PATH=$PATH:$SPARK_HOME/bin
```

```sh
export SPARK_HOME=/usr/local/spark
export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin
```

```sh
export SCALA_HOME = 
export JAVA_HOME = 
export SPARK_HOME =
export PATH = $PATH:$HOME/bin:$JAVA_HOME/bin:$SCALA_HOME/bin:$SPARK_HOME/bin
```


```sh
export JAVA_HOME=/usr/lib/jvm/default-java
export HADOOP_HOME=/usr/local/hadoop
export SPARK_HOME=/usr/local/spark
export PYTHONPATH=$SPARK_HOME/python:$SPARK_HOME/python/lib/py4j-0.10.4-src.zip:$PYTHONPATH
export PYSPARK_PYTHON=python3
export PATH=$HADOOP_HOME/bin:$SPARK_HOME/bin:$PATH
```

PYTHONPATH环境变量主要是为了在Python3中引入pyspark库，

PYSPARK_PYTHON变量主要是设置pyspark运行的python版本。

.bashrc中必须包含JAVA_HOME,HADOOP_HOME,SPARK_HOME,PYTHONPATH,PYSPARK_PYTHON,PATH这些环境变量。

如果已经设置了这些变量则不需要重新添加设置。

另外需要注意，上面的配置项中，PYTHONPATH这一行有个py4j-0.10.4-src.zip，

这个zip文件的版本号一定要和“/usr/local/spark/python/lib”目录下的py4j-0.10.4-src.zip文件保持版本一致。

比如，如果“/usr/local/spark/python/lib”目录下是py4j-0.10.7-src.zip，

那么，PYTHONPATH这一行后面也要写py4j-0.10.7-src.zip，从而使二者版本一致。


验证Spark安装。

```sh
source ~/.bashrc
spark-shell --version
```


```sh
source /etc/profile
```

通过运行Spark自带的示例，验证Spark是否安装成功。、

```sh
cd /usr/local/spark
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

### 配置spark-env.sh：

安装后，还需要修改Spark的配置文件spark-env.sh

```sh
cd spark/conf
cp spark-env.sh.template spark-env.sh
vim spark-env.sh
```

添加以下内容：（版本一）


```sh
export JAVA_HOME=/usr/local/java/jdk1.8.0_241
export HADOOP_HOME=/usr/local/hadoop
export HADOOP_CONF_DIR=/usr/local/hadoop/etc/hadoop
export SCALA_HOME=/usr/local/scala
export SPARK_HOME=/usr/local/spark
export SPARK_MASTER_IP=127.0.0.1
export SPARK_MASTER_PORT=7077
export SPARK_MASTER_WEBUI_PORT=8099
export SPARK_WORKER_CORES=3
export SPARK_WORKER_INSTANCES=1
export SPARK_WORKER_MEMORY=5G
export SPARK_WORKER_WEBUI_PORT=8081
export SPARK_EXECUTOR_CORES=1
export SPARK_EXECUTOR_MEMORY=1G
export LD_LIBRARY_PATH=${LD_LIBRARY_PATH}:$HADOOP_HOME/lib/native
```

在第一行添加以下配置信息:（版本二）

```sh
export SPARK_DIST_CLASSPATH=$(/usr/local/hadoop/bin/hadoop classpath)
```

编辑spark-env.sh,添加如下内容：（版本三）用于集群环境

```sh
export SPARK_DIST_CLASSPATH=$(/usr/local/hadoop/bin/hadoop classpath)
export HADOOP_CONF_DIR=/usr/local/hadoop/etc/hadoop
export SPARK_MASTER_IP=192.168.1.104
```

有了上面的配置信息以后，Spark就可以把数据存储到Hadoop分布式文件系统HDFS中，也可以从HDFS中读取数据。如果没有配置上面信息，Spark就只能读写本地数据，无法读写HDFS数据。



SPARK_MASTER_IP 指定 Spark 集群 Master 节点的 IP 地址；

配置好后，将Master主机上的/usr/local/spark文件夹复制到各个节点上。在Master主机上执行如下命令：

```sh
cd /usr/local/
tar -zcf ~/spark.master.tar.gz ./spark
cd ~
scp ./spark.master.tar.gz slave01:/home/hadoop
scp ./spark.master.tar.gz slave02:/home/hadoop
```

在slave01,slave02节点上分别执行下面同样的操作：

```sh
sudo rm -rf /usr/local/spark/
sudo tar -zxf ~/spark.master.tar.gz -C /usr/local
sudo chown -R hadoop /usr/local/spark
```

### 配置slaves

在Master节点主机上进行如下操作：

配置slaves文件
将 slaves.template 拷贝到 slaves

```sh
cd /usr/local/spark/
cp ./conf/slaves.template ./conf/slaves
vim slaves
```

```sh
cp slaves.template  slaves
vim slaves
```

注：我们会发现slaves文件里为localhost即本机地址，当前为伪分布式，因此不用修改

如果要变成集群环境，则需要修改：

slaves文件设置Worker节点。编辑slaves内容,把默认内容localhost替换成如下内容：


```sh
slave01
slave02
```

### 启动Spark集群

#### 启动Hadoop集群

启动Spark集群前，要先启动Hadoop集群。

在Master节点主机上运行如下命令：

```sh
cd /usr/local/hadoop/
sbin/start-all.sh
```

#### 启动Spark集群

启动Master节点

在Master节点主机上运行如下命令：

```sh
d /usr/local/spark/
sbin/start-master.sh
```

在Master节点上运行jps命令，可以看到多了个Master进程：

```sh
jps
```

15093 Jps
14343 SecondaryNameNode
14121 NameNode
14891 Master
14509 ResourceManager

启动所有Slave节点
在Master节点主机上运行如下命令：

```sh
sbin/start-slaves.sh
```

分别在slave01、slave02节点上运行jps命令，可以看到多了个Worker进程

```sh
jps
```

37553 DataNode
37684 NodeManager
37876 Worker
37924 Jps

总结：


```sh
cd /usr/local/spark/
./sbin/start-master.sh
./sbin/start-slaves.sh
```

通过jps命令会发现多出worker一项

通过spark的web界面 http://127.0.0.1:8099/ 可以查看spark集群当前概况（单机模式）

在浏览器上查看Spark独立集群管理器的集群信息
在master主机上打开浏览器，访问http://master:8080,（集群模式）

#### 关闭Spark集群

关闭Master节点

```sh
sbin/stop-master.sh
```

关闭Worker节点

```sh
sbin/stop-slaves.sh
```

关闭Hadoop集群

```sh
cd /usr/local/hadoop/
sbin/stop-all.sh
```

#### 启动bin目录下的spark-shell


```sh
./bin/spark-shell
```

即会出现spark scala的命令行执行环境：

### 为了方便可以修改Bash环境变量配置：


```sh
vim /etc/bash.bashrc
```

添加相应环境变量：

 
```sh
export SPARK_HOME=/usr/local/spark
export PATH=${JAVA_HOME}/bin:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin:${SPARK_HOME}/bin:${SPARK_HOME}/sbin:$PATH
```

执行source更新命令：

```sh
source /etc/bash.bashrc
```

### 配置Spark作业监控。

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

## 2.5 pyspark

前面已经安装了Hadoop和Spark，

如果Spark不使用HDFS和YARN，那么就不用启动Hadoop也可以正常使用Spark。

如果在使用Spark的过程中需要用到 HDFS，

就要首先启动 Hadoop（启动Hadoop的方法可以参考上面给出的Hadoop安装教程）。

这里假设不需要用到HDFS，

### 使用Spark

因此，就没有启动Hadoop。现在我们直接开始使用Spark。

```sh
bin/pyspark
```

如果没有设置PYSPARK_PYTHON环境变量，则使用如下命令启动pyspark

```sh
PYSPARK_PYTHON=python3
./bin/pyspark
```

pyspark命令及其常用的参数如下：

```sh
./bin/pyspark --master <master-url>
```

Spark的运行模式取决于传递给SparkContext的Master URL的值。Master URL可以是以下任一种形式：
* local 使用一个Worker线程本地化运行SPARK(完全不并行)
* local[*] 使用逻辑CPU个数数量的线程来本地化运行Spark
* local[K] 使用K个Worker线程本地化运行Spark（理想情况下，K应该根据运行机器的CPU核数设定）
* spark://HOST:PORT 连接到指定的Spark standalone master。默认端口是7077.
* yarn-client 以客户端模式连接YARN集群。集群的位置可以在HADOOP_CONF_DIR 环境变量中找到。
* yarn-cluster 以集群模式连接YARN集群。集群的位置可以在HADOOP_CONF_DIR 环境变量中找到。
* mesos://HOST:PORT 连接到指定的Mesos集群。默认接口是5050。

### 在Spark中采用本地模式启动pyspark

–master：

这个参数表示当前的pyspark要连接到哪个master，

如果是local[*]，就是使用本地模式启动pyspark，

其中，中括号内的星号表示需要使用几个CPU核心(core)；

–jars： 

这个参数用于把相关的JAR包添加到CLASSPATH中；

如果有多个jar包，可以使用逗号分隔符连接它们；

比如，要采用本地模式，在4个CPU核心上运行pyspark：

```sh
cd /usr/local/spark
./bin/pyspark --master local[4]
```

或者，可以在CLASSPATH中添加code.jar，命令如下：

```sh
cd /usr/local/spark
./bin/pyspark --master local[4] --jars code.jar 
```

可以执行“pyspark –help”命令，获取完整的选项列表，具体如下

```sh
cd /usr/local/spark
./bin/pyspark --help
```

上面是命令使用方法介绍，下面正式使用命令进入pyspark环境，可以通过下面命令启动pyspark环境：

```sh
bin/pyspark
```

现在，你就可以在里面输入python代码进行调试了。
比如，下面在命令提示符后面输入一个表达式“8 * 2 + 5”，然后回车，就会立即得到结果：

```py
>>> 8 * 2 + 5
```

最后，可以使用命令“exit()”退出pyspark，如下所示：

或者，也可以直接使用“Ctrl+D”组合键，退出pyspark。

```py
>>> exit()
```

### pyspark独立应用程序编程

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
logFile = "file:///usr/local/spark/README.md"
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

### Spark应用程序在集群中运行

需要借助于集群管理器（包括本地集群管理器、YARN、Mesos）来为其实现资源管理调度服务，

实现对集群中各个机器的访问。

这里通过简单的示例介绍其中两种：

独立集群管理器和Hadoop Yarn集群管理器。

通过介绍，我们可以了解到如何在这两种集群管理器上运行Spark应用程序。

请登录Linux系统，打开一个终端。

#### 启动Hadoop集群

```sh
cd /usr/local/hadoop/
sbin/start-all.sh
```

启动Spark的Master节点和所有slaves节点

```sh
cd /usr/local/spark/
sbin/start-master.sh
sbin/start-slaves.sh
```

独立集群管理器

（1）在集群中运行应用程序JAR包

向独立集群管理器提交应用，

需要把spark：//master:7077作为主节点参数递给spark-submit。

下面我们可以运行Spark安装好以后自带的样例程序SparkPi，

它的功能是计算得到pi的值（3.1415926）。

在Shell中输入如下命令：

```sh
bin/spark-submit --class org.apache.spark.examples.SparkPi --master spark://master:7077 examples/jars/spark-examples_2.11-2.0.2.jar 100 2>&1 | grep "Pi is roughly"
```

（2）在集群中运行spark-shell

也可以用spark-shell连接到独立集群管理器上。

首先做一点准备工作，把一个README.md文件拷贝到HDFS上，用于后面的测试。

```sh
cd /usr/local/hadoop/
# 下面这条命令中，我们把spark安装目录下的README.md文件上传到分布式文件系统HDFS的根目录下
bin/hadoop fs -put hadoop fs -put /usr/local/spark/README.md /
```

在Shell中输入如下命令启动进入spark-shell：

```sh
cd /usr/local/spark/
bin/spark-shell --master spark://master:7077
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

用户在独立集群管理Web界面查看应用的运行情况，可以浏览器中输入地址进行查看(http://master:8080/)

#### Hadoop YARN管理器

（1）在集群中运行应用程序JAR包

向Hadoop YARN集群管理器提交应用，

需要把yarn-cluster作为主节点参数递给spark-submit。

请登录Linux系统，打开一个终端，在Shell中输入如下命令：

```sh
bin/spark-submit --class org.apache.spark.examples.SparkPi --master yarn-cluster examples/jars/spark-examples_2.11-2.0.2.jar
```

运行后，根据在Shell中得到输出的结果地址查看

复制结果地址到浏览器，点击查看Logs，再点击stdout，即可查看结果

（2）在集群中运行spark-shell

也可以用spark-shell连接到独立集群管理器上。

请登录Linux系统，打开一个终端，在Shell中输入如下命令启动进入spark-shell：

```sh
bin/spark-shell --master yarn
```

在spark-shell中输入如下代码进行测试：

```sh
scala> val textFile = sc.textFile("hdfs://master:9000/README.md")
textFile: org.apache.spark.rdd.RDD[String] = hdfs://master:9000/README.md MapPartitionsRDD[3] at textFile at <console>:24
 
scala> textFile.count()
res2: Long = 99                                                                 
 
scala> textFile.first()
res3: String = # Apache Spark
```

用户在Hadoop Yarn集群管理Web界面查看所有应用的运行情况，

可以在浏览器中输入地址进行查看(http://master:8088/cluster) 

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

## kafka

到[Kafka官网](https://kafka.apache.org/downloads)下载安装文件时，一定要选择和自己电脑上已经安装的scala版本号一致才可以，

本教程安装的Spark版本号是2.1.0，scala版本号是2.11，

所以，一定要选择Kafka版本号是2.11开头的。

比如，到Kafka官网中，可以下载安装文件Kafka_2.11-0.10.2.0.tgz，

前面的2.11就是支持的scala版本号，后面的0.10.2.0是Kafka自身的版本号。

### Ubuntu 系统安装Kafka

访问Kafka官方下载页面,下载稳定版本0.10.1.0的kafka.此安装包内已经附带zookeeper,不需要额外安装zookeeper.按顺序执行如下步骤:

```sh
cd ~/下载
sudo tar -zxf kafka_2.11-0.10.1.0.tgz -C /usr/local
cd /usr/local
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




### 安装成功了Kafka。

进入kafka所在的目录

```sh
cd /usr/local/kafka
./bin/zookeeper-server-start.sh config/zookeeper.properties
```

注意，执行上面命令以后，终端窗口会返回一堆信息，然后就停住不动了，

没有回到shell命令提示符状态，这时，千万不要错误认为死机了，

而是Zookeeper服务器启动了，正在处于服务状态。

所以，千万不要关闭这个终端窗口，一旦关闭，zookeeper服务就停止了，

所以，不能关闭这个终端窗口。

请另外打开第二个终端，然后输入下面命令启动Kafka服务：

```sh
cd /usr/local/kafka
bin/kafka-server-start.sh config/server.properties
```

这样，Kafka就会在后台运行，即使你关闭了这个终端，Kafka也会一直在后台运行。

不过，这样做，有时候我们往往就忘记了还有Kafa在后台运行，所以，建议暂时不要用

下面先测试一下Kafka是否可以正常使用。

再另外打开第三个终端,输入如下命令:

```sh
cd /usr/local/kafka
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic dblab
```
topic是发布消息发布的category,

以单节点的配置创建了一个叫dblab的topic.

可以用list列出所有创建的topics,来查看刚才创建的主题是否存在。

或者：输入下面命令创建一个自定义名称为“wordsendertest”的topic

```sh
cd /usr/local/kafka
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
cd /usr/local/kafka
./bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic dblab --from-beginning  
```

请另外打开第四个终端，输入下面命令：

```sh
cd /usr/local/kafka
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

### Spark准备工作（jar文件）

按照我们前面安装好的Spark版本，这些jar包都不在里面，

为了证明这一点，我们现在可以测试一下。请打开一个新的终端，然后启动pyspark：

```sh
cd /usr/local/spark
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
cd /usr/local/spark/jars
mkdir kafka
cd ~
cd 下载
cp ./spark-streaming-kafka-0-8_2.11-2.1.0.jar /usr/local/spark/jars/kafka
```

这样，我们就把spark-streaming-kafka-0-8_2.11-2.1.0.jar文件

拷贝到了“/usr/local/spark/jars/kafka”目录下。

同时，我们还要修改spark目录下conf/spark-env.sh文件,

```sh
cd spark/conf
vim spark-env.sh
```

修改该文件下面的SPARK_DIST_CLASSPATH变量

编辑spark-env.sh：（版本三）用于集群环境

原本的：

```sh
export SPARK_DIST_CLASSPATH=$(/usr/local/hadoop/bin/hadoop classpath)
```

新的：

```sh
export SPARK_DIST_CLASSPATH=$(/usr/local/hadoop/bin/hadoop classpath):$(/usr/local/hbase/bin/hbase classpath):/usr/local/spark/examples/jars/*:/usr/local/spark/jars/kafka/*:/usr/local/kafka/libs/*`
```

纠正：

这个环境变量里最后一个字符不要复制进去，还有这个环境变量里有hBase的变量配置，没安hbase的会出错，如按以上步骤搭建环境变量应为：

```sh
export SPARK_DIST_CLASSPATH=$(/usr/local/hadoop/bin/hadoop classpath):/usr/local/spark/examples/jars/*:/usr/local/spark/jars/kafka/*:/usr/local/kafka/libs/*
```

### 编写Spark程序使用Kafka数据源

下面，我们就可以进行程序编写了。

请新打开一个终端，然后，执行命令创建代码目录：

```sh
cd /usr/local/spark/mycode
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

```sh

```

```sh

```


## flink

```sh
```

## postgresql

```sh
```

## MongoDB

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

### Mongo Spark Connector 连接器


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

#### 案例

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


#### 运价系统的架构图

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

### Spark 任务入口程序

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


### Spark ＋ MongoDB演示

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



# 3. zeppelin常用命令：

```sh
sudo vim conf/zeppelin-site.xml
bin/zeppelin-daemon.sh restart
vim logs/zeppelin-xxxxx-Pro.local.log
```

## 在local模式下运行：

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

## 在remote模式下运行：

flink.excution.mode设置为remote
flink.excution.remote.host设置为localhost
flink.excution.remote.port设置为**8081**

## yarn模式下的运行：

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

## inline configuration

一定要在进程起来前跑

```sql
%flink.conf
flink.execution.mode yarn
```

## hive：


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

## SQL

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


## Streaming

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


## kafka

## python