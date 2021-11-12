## 下载后解压

到 opt：

```sh
sudo tar -xvf flink-1.14.0-bin-scala_2.12.tgz -C /opt
```

```sh
cd /opt
```

```sh
sudo mv flink-1.14.0-bin-scala_2.12 flink
```

## 在master节点上配置Flink

所有的配置都在"conf/flink-conf.yaml"文件中。

在实际应用中，以下几个配置项是非常重要的：

* jobmanager.heap.mb：每个JobManager的可用内存量，以MB为单位。

* taskmanager.heap.mb：每个TaskManager的可用内存量，以MB为单位。

* taskmanager.numberOfTaskSlots：每台机器上可用的cpu数量，默认为1。

* parallelism.default：集群中cpu的总数。

* io.tmp.dirs：临时目录。

```sh
cd conf
```

```sh
sudo vim flink-conf.yaml
```

jobmanager.rpc.address: master   // 指向master节点

```yaml
jobmanager.rpc.address: ubuntu01
```

jobmanager.heap.size: 1024m      // 定义允许JVM在每个节点上分配的最大主内存量

```yaml
 jobmanager.heap.size: 512m
 taskmanager.memory.process.size: 512m
```

taskmanager.numberOfTaskSlots：每台机器上可用的cpu数量
parallelism.default：集群中cpu的总数

```yaml
 taskmanager.numberOfTaskSlots: 1
 parallelism.default: 3
```

```sh
sudo vim workers
```

```s
node01
node02
```

```sh
sudo vim masters
```

```s
ubuntu01:8081
```

## ssh 复制

分别开启node01 和 node02 的ssh

```sh
sudo service ssh start
```

ubuntu01试试ssh是否成功

```sh
ssh node01
```

```sh
ssh node02
```

```s
scp -r /opt/flink root@node01:/opt/
scp -r /opt/flink root@node02:/opt/
```

## 启动集群

```sh
./bin/start-cluster.sh
```

要停止Flink集群，在终端窗口输入以下命令：

```sh
./bin/stop-cluster.sh
```

注：

停止单个的Job Manager的命令:

```sh
./bin/jobmanager.sh stop cluster
```

停止单个的Task Manager命令:

```sh
./bin/taskmanager.sh stop cluster
```

这个脚本会在`本地节点`启动一个`JobManager`

并通过SSH连接到所有的`worker节点`以启动每个节点上的`TaskManager`。

注意观察启动过程中的输出信息，如下：

- Starting cluster.
- Starting `standalonesession` daemon on host ubuntu01.
- Starting `taskexecutor` daemon on host node01.
- Starting `taskexecutor` daemon on host node02.

启动以后，分别在master、worker01和worker02节点上执行

```sh
jps
```

查看各节点上的进程是否正常启动了。

## 跑个实例

```sh
 ./bin/flink run examples/streaming/SocketWindowWordCount.jar --hostname ubuntu01 --port 9000
```

```sh
./bin/flink run examples/streaming/WordCount.jar
```

```sh
start-dfs.sh

./bin/flink run ./examples/batch/WordCount.jar
   --input  hdfs://hadoop:8020/wc.txt 
   --output hdfs://hadoop:8020/result
```

```sh
hdfs dfs -cat hdfs://hadoop:8020/result
```

可以看到以下计算结果：

- day 2
- good 2
- study 1
- up 1

## 查看日志

```sh
tail log/flink-*-taskexecutor-*.out
```

## 监视集群的状态和正在运行的作业

执行以下命令查询输出结果：

```s
http://ubuntu01:8081/
```

## 查看磁盘空间

```sh
df -h
```

## 查看inode空间

```sh
df -i
```

由上可知根目录/磁盘满了 接下来需要确定是哪个文件太大导致的:

查看指定目录下所有文件大小并排序:

```sh
du -sh  /* |sort
```