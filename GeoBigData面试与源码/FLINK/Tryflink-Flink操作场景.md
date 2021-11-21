<!-- vscode-markdown-toc -->
* 1. [一些定义](#)
* 2. [场景说明](#-1)
* 3. [环境搭建](#-1)
* 4. [环境讲解](#-1)
	* 4.1. [Flink WebUI 界面](#FlinkWebUI)
	* 4.2. [日志](#-1)
		* 4.2.1. [JobManager 日志](#JobManager)
		* 4.2.2. [TaskManager 日志](#TaskManager)
	* 4.3. [Flink CLI 相关命令](#FlinkCLI)
	* 4.4. [Flink REST API](#FlinkRESTAPI)
	* 4.5. [Kafka Topics](#KafkaTopics)
* 5. [核心特性探索](#-1)
	* 5.1. [获取所有运行中的 Job](#Job)
		* 5.1.1. [CLI 命令](#CLI)
		* 5.1.2. [REST API 请求](#RESTAPI)
	* 5.2. [Job 失败与恢复](#Job-1)
		* 5.2.1. [Step 1: 观察输出](#Step1:)
		* 5.2.2. [Step 2: 模拟失败](#Step2:)
		* 5.2.3. [Step 3: 失败恢复](#Step3:)
	* 5.3. [Job 升级与扩容](#Job-1)
		* 5.3.1. [Step 1: 停止 Job](#Step1:Job)
		* 5.3.2. [Step 2a: 重启 Job (不作任何变更)](#Step2a:Job)
		* 5.3.3. [Step 2b: 重启 Job (修改并行度)](#Step2b:Job)
	* 5.4. [查询 Job 指标](#Job-1)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->##  1. <a name='Flink'></a>Flink 操作场景

[可以以多种方式在不同的环境中部署](https://nightlies.apache.org/flink/flink-docs-release-1.14/zh/docs/try-flink/flink-operations-playground/)

- 如何管理和运行 Flink 任务
- 了解如何部署和监控应用程序
- 如何从失败作业中进行恢复
- 执行一些日常操作任务，如升级和扩容

##  1. <a name=''></a>一些定义

Flink Session Cluster 定义：

- 长时间运行的 Flink Cluster，
- 它可以接受多个 Flink Job 的执行。
- 此 Flink Cluster 的生命周期不受任何 Flink Job 生命周期的约束限制。
- 以前，Flink Session Cluster 也称为：
  - session mode 的 Flink Cluster
  - 和 Flink Application Cluster 相对应。

Flink Cluster 定义：

- 一般情况下，Flink 集群是由一个 Flink JobManager 和一个或多个 Flink TaskManager 进程组成的分布式系统。

Flink JobMaster 定义：

- Flink JobManager 是 Flink Cluster 的主节点。
- JobManager 负责监督单个作业 Task 的执行。
- 它包含三个不同的组件：
  - Flink Resource Manager
  - Flink Dispatcher
  - 运行每个 Flink Job 的 Flink JobMaster。

Flink JobMaster 定义：

- JobMaster 是在 Flink JobManager 运行中的组件之一。

Flink Job 定义：

- Flink Job是 logical graph (也称为 dataflow graph)的运行时表示，
- 该逻辑图是通过在Flink应用程序中调用execute()创建和提交的。

Logical Graph 定义：

- 逻辑图是一个`有向图directed graph`，
- `节点nodes`是`运算符Operators`，
- `边`定义运算符的`输入/输出关系`，
- 并与`数据流或数据集`相对应。
- 通过从 `Flink Application` 提交作业来创建逻辑图。

Operator 定义：

- Logical Graph 的`节点`。
- 算子执行某种操作，该操作通常由 `Function` 执行。
- Source 和 Sink 是数据`输入`和数据`输出`的特殊算子。
- 大多数 Function 都由相应的 Operator 封装。Function 封装了 Flink 程序的应用程序逻辑。

Operator Chain 定义：

- 算子链由两个或多个`连续的 Operator` 组成，
- 两者之间没有任何的`重新分区`。
- 同一算子链内的算子可以`彼此直接传递 record`，
- 而无需通过`序列化`或 `Flink 的网络栈`。

Record 定义：

- Record 是数据集或数据流的组成元素。
- Operator 和 Function 接收 record 作为输入，
- 并将 record 作为输出发出。
- Event 是特殊类型的 Record。
- 通过将每个 Record 分配给一个或多个分区，来把数据流或数据集划分为多个分区。

Physical Graph 定义：

- Physical graph 是一个在分布式运行时，
- 把 `Logical Graph` 转换为`可执行的结果`。
- `节点`是 `Task`，
- `边`表示`数据流或数据集`的`输入/输出关系`或 `partition`。

Task 定义：

- 是 Physical Graph 的节点。
- 它是基本的`工作单元`，由 Flink 的 runtime 来执行。
- Task 正好封装了一个 `Operator` 或者 `Operator Chain` 的 parallel instance。Instance 常用于描述运行时的特定类型(通常是 Operator 或者 Function)的一个具体实例。
- Task 被调度到 TaskManager 上执行。TaskManager 相互通信，只为在后续的 Task 之间交换数据。

Flink Application 定义：

- Flink应用程序是一个Java应用程序，
- 它通过`main()方法`(或其他方法)提交一个或多个`Flink Jobs`。
- 提交作业通常通过在`执行环境`中调`用execute()`来完成。
  - 应用程序的作业可以提交到:
  - 长期运行的 Flink Session Cluster
  - 专用的 Flink Application Cluster
  - Flink Job Cluster

Flink Job Cluster 定义：

- Flink作业集群是只执行`单个Flink作业`的`专用Flink集群`。
- `Flink Cluster的生存期`与`Flink Job的生存期`绑定。

Flink Application Cluster 定义：

- Flink应用集群是一个专门的Flink集群，
- 它只执行来自一个`Flink Application的Flink Jobs`。
- `Flink Cluster的生存期`与`Flink Application的生存期`绑定。

Windows 定义：

- `窗口`是处理`无限流`的核心。
- Windows将`流`分成有限大小的“`桶`”，
- 我们可以在这些`桶`上进行计算。

窗口Flink程序的一般结构如下所示:

- 第一个代码段引用键控流keyed streams
  
```java
stream
       .keyBy(...)               <-  键控与非键控窗口
       .window(...)              <-  required: "assigner"
      [.trigger(...)]            <-  optional: "trigger" (else default trigger)
      [.evictor(...)]            <-  optional: "evictor" (else no evictor)
      [.allowedLateness(...)]    <-  optional: "lateness" (else zero)
      [.sideOutputLateData(...)] <-  optional: "output tag" (else no side output for late data)
       .reduce/aggregate/apply()      <-  required: "function"
      [.getSideOutput(...)]      <-  optional: "output tag"
```

- 第二个代码段引用非键控流non-keyed streams

```java
stream
       .windowAll(...)           <-  required: "assigner"
      [.trigger(...)]            <-  optional: "trigger" (else default trigger)
      [.evictor(...)]            <-  optional: "evictor" (else no evictor)
      [.allowedLateness(...)]    <-  optional: "lateness" (else zero)
      [.sideOutputLateData(...)] <-  optional: "output tag" (else no side output for late data)
       .reduce/aggregate/apply()      <-  required: "function"
      [.getSideOutput(...)]      <-  optional: "output tag"
```

在上面👆，方括号（[…]）中的命令是optional。这表明Flink允许您以多种不同的方式`自定义窗口逻辑`，以使其最适合您的需要。

可以看到，唯一的区别是:

- 键控流的keyBy(…)调用
- 非键控流的window(…)调用

##  2. <a name='-1'></a>场景说明

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.691qdrtdoqg0.png)

##  3. <a name='-1'></a>环境搭建

你需要在自己的主机上提前安装好 docker (1.12+) 和 docker-compose (2.1+)。

```sh
git clone https://github.com/apache/flink-playgrounds.git
cd flink-playgrounds/operations-playground
docker-compose build
```

接下来在开始运行之前先在 `Docker 主机`上创建`检查点`和`保存点`目录（这些卷由 `jobmanager` 和 `taskmanager` 挂载，如 `docker-compose.yaml` 中所指定的）：

```sh
mkdir -p /tmp/flink-checkpoints-directory
mkdir -p /tmp/flink-savepoints-directory
```

然后启动环境：

```sh
docker-compose up -d
```

接下来你可以执行如下命令来查看正在运行中的 Docker 容器：

```sh
docker-compose ps
```

```s
                    Name                                  Command               State                   Ports                
-----------------------------------------------------------------------------------------------------------------------------
operations-playground_clickevent-generator_1   /docker-entrypoint.sh java ...   Up       6123/tcp, 8081/tcp                  
operations-playground_client_1                 /docker-entrypoint.sh flin ...   Exit 0                                       
operations-playground_jobmanager_1             /docker-entrypoint.sh jobm ...   Up       6123/tcp, 0.0.0.0:8081->8081/tcp    
operations-playground_kafka_1                  start-kafka.sh                   Up       0.0.0.0:9094->9094/tcp              
operations-playground_taskmanager_1            /docker-entrypoint.sh task ...   Up       6123/tcp, 8081/tcp                  
operations-playground_zookeeper_1              /bin/sh -c /usr/sbin/sshd  ...   Up       2181/tcp, 22/tcp, 2888/tcp, 3888/tcp
```

从上面的信息可以看出 `client 容器`已成功提交了 `Flink Job` (Exit 0)， 同时包含`数据生成器`在内的所有集群组件都处于`运行中状态 (Up)`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3quvrh3anhm0.png)

你可以执行如下命令停止 docker 环境：

```sh
docker-compose down -v
```

##  4. <a name='-1'></a>环境讲解

###  4.1. <a name='FlinkWebUI'></a>Flink WebUI 界面

打开浏览器并访问 http://localhost:8081

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5zo01pchs300.png)

###  4.2. <a name='-1'></a>日志

####  4.2.1. <a name='JobManager'></a>JobManager 日志

可以通过 docker-compose 命令进行查看。

```sh
docker-compose logs -f jobmanager
```

JobManager 刚启动完成之时，你会看到很多关于 checkpoint completion (检查点完成)的日志。

####  4.2.2. <a name='TaskManager'></a>TaskManager 日志

```sh
docker-compose logs -f taskmanager
```

TaskManager 刚启动完成之时，你同样会看到很多关于 checkpoint completion (检查点完成)的日志。

###  4.3. <a name='FlinkCLI'></a>Flink CLI 相关命令

Flink CLI 相关命令可以在 `client 容器`内进行使用。 

比如，想查看 Flink CLI 的 help 命令，可以通过如下方式进行查看：

```sh
docker-compose run --no-deps client flink --help
```

###  4.4. <a name='FlinkRESTAPI'></a>Flink REST API

Flink REST API 可以通过`本机`的 `localhost:8081` 进行访问，

也可以在 `client 容器`中通过 `jobmanager:8081` 进行访问。 

**REST API**：可以接受 HTTP 请求并返回 JSON 格式的数据。

默认情况下，该服务器监听 8081 端口，端口号可以通过修改 flink-conf.yaml 文件的 rest.port 进行配置。

我们使用 Netty 和 Netty Router 库来处理 REST 请求和转换 URL 。

比如，通过如下命令可以获取所有正在运行中的 Job：

```sh
curl localhost:8081/jobs
```

###  4.5. <a name='KafkaTopics'></a>Kafka Topics

可以运行如下命令查看 Kafka Topics 中的记录：

```java
//input topic (1000 records/s)
docker-compose exec kafka kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 --topic input

//output topic (24 records/min)
docker-compose exec kafka kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 --topic output
```

##  5. <a name='-1'></a>核心特性探索

###  5.1. <a name='Job'></a>获取所有运行中的 Job

####  5.1.1. <a name='CLI'></a>CLI 命令

```s
docker-compose run --no-deps client flink list
```

预期输出

```s
Waiting for response...
------------------ Running/Restarting Jobs -------------------
16.07.2019 16:37:55 : <job-id> : Click Event Count (RUNNING)
--------------------------------------------------------------
No scheduled jobs.
```
####  5.1.2. <a name='RESTAPI'></a>REST API 请求

```sh
curl localhost:8081/jobs
```

预期响应 (结果已格式化)

```s
{
  "jobs": [
    {
      "id": "<job-id>",
      "status": "RUNNING"
    }
  ]
}
```

一旦 Job 提交，Flink 会默认为其生成一个 **JobID** ->

后续对该 Job 的 所有操作（无论是通过 CLI 还是 REST API）都需要带上 **JobID**。

###  5.2. <a name='Job-1'></a>Job 失败与恢复

在 `Job (部分)失败`的情况下，Flink 对事件处理依然能够提供`精确一次`的保障， 

在本节中你将会观察到并能够在某种程度上验证这种行为:

####  5.2.1. <a name='Step1:'></a>Step 1: 观察输出

事件以特定速率生成，刚好使得每个统计窗口都包含确切的 1000 条记录。

-> so 你可以实时查看 output topic 的输出，确定失败恢复后所有的窗口依然输出正确的统计数字

-> 以此来验证 Flink 在 TaskManager 失败时能够成功恢复，而且不丢失数据、不产生数据重复。

```sh
docker-compose exec kafka kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 --topic output
```

####  5.2.2. <a name='Step2:'></a>Step 2: 模拟失败

为了模拟部分失败故障，你可以 kill 掉一个 TaskManager

  这种失败行为在生产环境中就相当于

- TaskManager 进程挂掉、
- TaskManager 机器宕机、
- 从框架或用户代码中抛出的一个临时异常（例如，由于外部资源暂时不可用）
  而导致的失败。

```sh
docker-compose kill taskmanager
```

```s
几秒钟后->
JobManager 就会感知到 TaskManager 已失联
           接下来,它会 取消 Job 运行
                      并且, 立即重新提交该 Job 以进行恢复。 
当 Job 重启后-> 
所有的任务都会处于 SCHEDULED 状态，如以下截图中紫色方格所示：
```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.20a4h62tz4gw.png)

此时，由于 `TaskManager 提供的 TaskSlots 资源`不够用，Job 的所有任务都不能成功转为 RUNNING 状态，直到有新的 TaskManager 可用。

在此之前，该 Job 将经历一个`取消和重新提交 不断循环`的过程。

与此同时，数据生成器 (data generator) 一直不断地往 input topic 中生成 ClickEvent 事件，

在生产环境中也经常出现这种 `Job 挂掉`但`源头还在不断产生数据`的情况。

####  5.2.3. <a name='Step3:'></a>Step 3: 失败恢复 

一旦 TaskManager 重启成功，它将会重新连接到 JobManager。

```sh
docker-compose up -d taskmanager
```

```s
当 TaskManager 注册成功后-> 
    JobManager 就会将处于 [SCHEDULED 状态的所有任务] 
               调度到 [该 TaskManager 的可用 TaskSlots] 中运行，
                    此时所有的任务将会从失败前 [最近一次成功的 checkpoint] 进行恢复， 
                    一旦恢复成功，它们的状态将转变为 RUNNING。

接下来该 Job 将 -> 快速处理 Kafka input 事件的全部积压（在 Job 中断期间累积的数据）
               -> 并以更快的速度(>24 条记录/分钟)产生输出，
               -> 直到它追上 kafka 的 lag 延迟为止

此时观察 output topic 输出 -> 
          你会看到 -> 在[每一个时间窗口]中都有按 page 进行分组的记录
                  -> 计数刚好是 1000。 
                  由于我们使用的是 FlinkKafkaProducer [“至少一次” 模式]，
                  因此你可能会看到一些记录重复输出多次。

注意：在大部分生产环境中都需要一个资源管理器 (Kubernetes、Yarn)对 失败的 Job 进行自动重启。
```

###  5.3. <a name='Job-1'></a>Job 升级与扩容 

升级 Flink 作业一般都需要两步：

- 第一，使用 Savepoint 优雅地停止 Flink Job。 
  - Savepoint 是整个应用程序状态的一次快照
  - （类似于 checkpoint ），
  - 该快照是在一个`明确定义的、全局一致的`时间点生成的。
- 第二，从 Savepoint 恢复启动待升级的 Flink Job。 
  
在此，“升级”包含如下几种含义：

- 配置升级（比如 Job 并行度修改）
- Job 拓扑升级（比如添加或者删除算子）
- Job 的用户自定义函数升级

在开始升级之前，你可能需要实时查看 Output topic 输出， 以便观察在升级过程中没有数据丢失或损坏。

```sh
docker-compose exec kafka kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 --topic output
```

####  5.3.1. <a name='Step1:Job'></a>Step 1: 停止 Job 

方式一：CLI 命令

```s
docker-compose run --no-deps client flink stop <job-id>
```

预期输出

```s
Suspending job "<job-id>" with a savepoint.
Suspended job "<job-id>" with a savepoint.
```

```s
Savepoint 已保存在 state.savepoints.dir 指定的路径中，
          该配置在 flink-conf.yaml 中定义，
          link-conf.yaml 挂载在本机的 /tmp/flink-savepoints-directory/ 目录下
```

在下一步操作中我们会用到`这个 Savepoint 路径`:

如果我们是通过 REST API 操作的 -> 

- 那么 `Savepoint 路径`会随着`响应结果`一起返回，
- 我们可以直接`查看文件系统`来确认 Savepoint 保存情况。

命令

```sh
ls -lia /tmp/flink-savepoints-directory
```

预期输出

```s
total 0
  17 drwxr-xr-x   3 root root   60 17 jul 17:05 .
   2 drwxrwxrwt 135 root root 3420 17 jul 17:09 ..
1002 drwxr-xr-x   2 root root  140 17 jul 17:05 savepoint-<short-job-id>-<uuid>
```

方式二：REST API 请求

请求

```sh
# 停止 Job
curl -X POST localhost:8081/jobs/<job-id>/stop -d '{"drain": false}'
```

预期响应 (结果已格式化)

```s
{
  "request-id": "<trigger-id>"
}
```

请求

```sh
# 检查停止结果并获取 savepoint 路径
 curl localhost:8081/jobs/<job-id>/savepoints/<trigger-id>
```

预期响应 (结果已格式化)

```s
{
  "status": {
    "id": "COMPLETED"
  },
  "operation": {
    "location": "<savepoint-path>"
  }
}
```

####  5.3.2. <a name='Step2a:Job'></a>Step 2a: 重启 Job (不作任何变更)

现在你可以从这个 Savepoint 重新启动待升级的 Job，为了简单起见，不对该 Job 作任何变更就直接重启。

方式一：CLI 命令

命令

```sh
docker-compose run --no-deps client flink run -s <savepoint-path> \
  -d /opt/ClickCountJob.jar \
  --bootstrap.servers kafka:9092 --checkpointing --event-time
```

```s
Click Event Count 这个 Job 在启动时总是会带上 
    --checkpointing 和 --event-time 两个参数， 
    如果我们去除这两个参数，那么 Job 的行为也会随之改变。

--checkpointing 参数 -> 开启了 checkpoint 配置，
                    ->  checkpoint 是 Flink 容错机制的重要保证。 
                    -> 如果你没有开启 checkpoint，
                          -> 那么在 Job 失败与恢复这一节中，
                          -> 你将会看到数据丢失现象发生。

--event-time 参数 -> 开启了 Job 的 事件时间 机制，
                  -> 该机制会使用 ClickEvent 自带的时间戳进行统计。 
                  -> 如果不指定该参数，
                          -> Flink 将结合当前机器时间使用事件处理时间进行统计。
                          -> 如此一来，每个窗口计数将不再是准确的 1000 了。


```

预期输出

```s
Starting execution of program
Job has been submitted with JobID <job-id>
```

方式二：REST API 请求

请求

```sh
# 从客户端容器上传 JAR
docker-compose run --no-deps client curl -X POST -H "Expect:" \
  -F "jarfile=@/opt/ClickCountJob.jar" http://jobmanager:8081/jars/upload
```

预期响应 (结果已格式化)

```s
{
  "filename": "/tmp/flink-web-<uuid>/flink-web-upload/<jar-id>",
  "status": "success"
}

```

请求

```sh
# 提交 Job
curl -X POST http://localhost:8081/jars/<jar-id>/run \
  -d '{"programArgs": "--bootstrap.servers kafka:9092 --checkpointing --event-time", "savepointPath": "<savepoint-path>"}'
```

预期响应 (结果已格式化)

```s
{
  "jobid": "<job-id>"
}
```

一旦该 Job 再次处于 `RUNNING 状态`->

- 你将从 `output Topic` 中看到数据在`快速输出`， 
- 因为刚启动的 Job 正在处理`停止期间积压`的大量数据。
- 另外，你还会看到在升级期间 没有产生任何数据丢失：所有窗口都在输出 1000。

####  5.3.3. <a name='Step2b:Job'></a>Step 2b: 重启 Job (修改并行度)

在从 Savepoint 重启 Job 之前，你还可以通过修改并行度来达到扩容 Job 的目的。

方式一：CLI 命令

命令

```sh
docker-compose run --no-deps client flink run -p 3 -s <savepoint-path> \
  -d /opt/ClickCountJob.jar \
  --bootstrap.servers kafka:9092 --checkpointing --event-time
```

预期输出

```sh
Starting execution of program
Job has been submitted with JobID <job-id>
```

方式二：REST API 请求

请求

```sh
# Uploading the JAR from the Client container
docker-compose run --no-deps client curl -X POST -H "Expect:" \
  -F "jarfile=@/opt/ClickCountJob.jar" http://jobmanager:8081/jars/upload
```

预期响应 (结果已格式化)

```s
{
  "filename": "/tmp/flink-web-<uuid>/flink-web-upload/<jar-id>",
  "status": "success"
}
```

请求

```sh
# 提交 Job
curl -X POST http://localhost:8081/jars/<jar-id>/run \
  -d '{"parallelism": 3, "programArgs": "--bootstrap.servers kafka:9092 --checkpointing --event-time", "savepointPath": "<savepoint-path>"}'
```

预期响应 (结果已格式化)

```sh
{
  "jobid": "<job-id>"
}
```

现在 Job 已重新提交，但由于我们`提高了并行度`所以导致 `TaskSlots 不够用`（1 个 TaskSlot 可用，总共需要 3 个），最终 Job 会重启失败。通过如下命令：

```sh
docker-compose scale taskmanager=2
```

你可以向 Flink 集群`添加第二个 TaskManager`（为 Flink 集群提供 `2 个 TaskSlots 资源`）， 它会`自动向 JobManager 注册`，TaskManager 注册完成后，Job 会再次处于 “RUNNING” 状态。

一旦 Job 再次运行起来，从 output Topic 的输出中你会看到在扩容期间数据依然没有丢失： 所有窗口的`计数都正好是 1000`。

###  5.4. <a name='Job-1'></a>查询 Job 指标

可以通过 JobManager 提供的 REST API 来获取系统和`用户指标`

具体请求方式取决于我们想查询哪类`指标`-> 

- Job 相关的`指标分类`可通过 **jobs/<job-id>/metrics** 获得，
- 而要想查询`某类指标`的`具体值`则可以在`请求地址`后跟上 `get 参数`。

请求:

```sh
curl "localhost:8081/jobs/<jod-id>/metrics?get=lastCheckpointSize"
```

预期响应 (结果已格式化且去除了占位符):

```s
[
  {
    "id": "lastCheckpointSize",
    "value": "9378"
  }
]
```

REST API 不仅可以用于`查询指标`，

还可以用于获取`正在运行中的 Job 详细信息`。

请求:

```sh
# 可以从结果中获取感兴趣的 vertex-id
curl localhost:8081/jobs/<jod-id>
```

预期响应 (结果已格式化):

```s
{
  "jid": "<job-id>",
  "name": "Click Event Count",
  "isStoppable": false,
  "state": "RUNNING",
  "start-time": 1564467066026,
  "end-time": -1,
  "duration": 374793,
  "now": 1564467440819,
  "timestamps": {
    "CREATED": 1564467066026,
    "FINISHED": 0,
    "SUSPENDED": 0,
    "FAILING": 0,
    "CANCELLING": 0,
    "CANCELED": 0,
    "RECONCILING": 0,
    "RUNNING": 1564467066126,
    "FAILED": 0,
    "RESTARTING": 0
  },
  "vertices": [
    {
      "id": "<vertex-id>",
      "name": "ClickEvent Source",
      "parallelism": 2,
      "status": "RUNNING",
      "start-time": 1564467066423,
      "end-time": -1,
      "duration": 374396,
      "tasks": {
        "CREATED": 0,
        "FINISHED": 0,
        "DEPLOYING": 0,
        "RUNNING": 2,
        "CANCELING": 0,
        "FAILED": 0,
        "CANCELED": 0,
        "RECONCILING": 0,
        "SCHEDULED": 0
      },
      "metrics": {
        "read-bytes": 0,
        "read-bytes-complete": true,
        "write-bytes": 5033461,
        "write-bytes-complete": true,
        "read-records": 0,
        "read-records-complete": true,
        "write-records": 166351,
        "write-records-complete": true
      }
    },
    {
      "id": "<vertex-id>",
      "name": "ClickEvent Counter",
      "parallelism": 2,
      "status": "RUNNING",
      "start-time": 1564467066469,
      "end-time": -1,
      "duration": 374350,
      "tasks": {
        "CREATED": 0,
        "FINISHED": 0,
        "DEPLOYING": 0,
        "RUNNING": 2,
        "CANCELING": 0,
        "FAILED": 0,
        "CANCELED": 0,
        "RECONCILING": 0,
        "SCHEDULED": 0
      },
      "metrics": {
        "read-bytes": 5085332,
        "read-bytes-complete": true,
        "write-bytes": 316,
        "write-bytes-complete": true,
        "read-records": 166305,
        "read-records-complete": true,
        "write-records": 6,
        "write-records-complete": true
      }
    },
    {
      "id": "<vertex-id>",
      "name": "ClickEventStatistics Sink",
      "parallelism": 2,
      "status": "RUNNING",
      "start-time": 1564467066476,
      "end-time": -1,
      "duration": 374343,
      "tasks": {
        "CREATED": 0,
        "FINISHED": 0,
        "DEPLOYING": 0,
        "RUNNING": 2,
        "CANCELING": 0,
        "FAILED": 0,
        "CANCELED": 0,
        "RECONCILING": 0,
        "SCHEDULED": 0
      },
      "metrics": {
        "read-bytes": 20668,
        "read-bytes-complete": true,
        "write-bytes": 0,
        "write-bytes-complete": true,
        "read-records": 6,
        "read-records-complete": true,
        "write-records": 0,
        "write-records-complete": true
      }
    }
  ],
  "status-counts": {
    "CREATED": 0,
    "FINISHED": 0,
    "DEPLOYING": 0,
    "RUNNING": 4,
    "CANCELING": 0,
    "FAILED": 0,
    "CANCELED": 0,
    "RECONCILING": 0,
    "SCHEDULED": 0
  },
  "plan": {
    "jid": "<job-id>",
    "name": "Click Event Count",
    "type": "STREAMING",
    "nodes": [
      {
        "id": "<vertex-id>",
        "parallelism": 2,
        "operator": "",
        "operator_strategy": "",
        "description": "ClickEventStatistics Sink",
        "inputs": [
          {
            "num": 0,
            "id": "<vertex-id>",
            "ship_strategy": "FORWARD",
            "exchange": "pipelined_bounded"
          }
        ],
        "optimizer_properties": {}
      },
      {
        "id": "<vertex-id>",
        "parallelism": 2,
        "operator": "",
        "operator_strategy": "",
        "description": "ClickEvent Counter",
        "inputs": [
          {
            "num": 0,
            "id": "<vertex-id>",
            "ship_strategy": "HASH",
            "exchange": "pipelined_bounded"
          }
        ],
        "optimizer_properties": {}
      },
      {
        "id": "<vertex-id>",
        "parallelism": 2,
        "operator": "",
        "operator_strategy": "",
        "description": "ClickEvent Source",
        "optimizer_properties": {}
      }
    ]
  }
}
```