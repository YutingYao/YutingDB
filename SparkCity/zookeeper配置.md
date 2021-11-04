
## 其他参考

[zookeeper数据发布与订阅](https://mp.weixin.qq.com/s/aXrYuSjrkq2wYxN0mbvtLg)

## 安装zookeeper集群

### 下载与安装zookeeper

```sh
wget http://mirrors.cnnic.cn/apache/zookeeper/zookeeper-3.4.8/zookeeper-3.4.8.tar.gz -P /usr/local/src/
tar zxvf zookeeper-3.4.8.tar.gz -C /opt
cd /opt && mv zookeeper-3.4.8 zookeeper
cd zookeeper
cp conf/zoo_sample.cfg conf/zoo.cfg
[hd@master zookeeper]# pwd
/home/hd/apps/zookeeper
```

### 配置文件

```sh
sudo vim conf/zoo.cfg
```

```s
tickTime=2000
initLimit=10
syncLimit=5
dataLogDir=/home/hd/apps/zookeeper/logs
dataDir=/home/hd/apps/zookeeper/data
clientPort=2181
#(主机名, 心跳端口、数据端口)
server.1= 192.168.1.148:2888:3888
server.2= 192.168.1.149:2888:3888
server.3= 192.168.1.150:2888:3888
```

### 在三个zookeeper节点配置环境变量

```sh
sudo vim /etc/profile
```

```s
export ZOOKEEPER_HOME=/home/hd/apps/zookeeper
export PATH=$PATH:$ZOOKEEPER_HOME/bin
```

```sh
source /etc/profile
```

第1台机

```sh
echo 1 > data/myid
cat data/myid
# 1
```

第2台机

```sh
echo 2 > data/myid
cat data/myid
# 2
```

第3台机

```sh
echo 3 > data/myid
cat data/myid
# 3
```

## 分别启动与状态查看

```sh
zkServer.sh start
zkServer.sh status
```

第一台机-第二台机-第二台机

```sh
 zkServer.sh start
 zkServer.sh  status
```