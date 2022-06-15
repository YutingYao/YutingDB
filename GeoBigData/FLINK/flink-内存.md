<!-- vscode-markdown-toc -->
* 1. [内存部署](#)
	* 1.1. [独立部署模式-flink.size](#-flink.size)
		* 1.1.1. [OutOfMemoryError: Direct buffer memory](#OutOfMemoryError:Directbuffermemory)
		* 1.1.2. [OutOfMemoryError: Java heap space](#OutOfMemoryError:Javaheapspace)
	* 1.2. [容器化部署模式-process.size](#-process.size)
		* 1.2.1. [容器（Container）内存超用](#Container)
	* 1.3. [托管内存 - managed.size](#-managed.size)
		* 1.3.1. [State Backend 的内存配置](#StateBackend)
		* 1.3.2. [Heap State Backend](#HeapStateBackend)
		* 1.3.3. [RocksDB State Backend](#RocksDBStateBackend)
		* 1.3.4. [批处理作业的内存配置](#-1)
		* 1.3.5. [消费者权重](#-1)
* 2. [JobManager 内存](#JobManager)
	* 2.1. [heap.size 配置 JVM 堆内存](#heap.sizeJVM)
	* 2.2. [off-heap.size 配置堆外内存](#off-heap.size)
	* 2.3. [详细配置](#-1)
* 3. [taskmanager 内存](#taskmanager)
	* 3.1. [详细配置](#-1)
	* 3.2. [异常分析](#-1)
		* 3.2.1. [IOException: Insufficient number of network buffers](#IOException:Insufficientnumberofnetworkbuffers)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->
##  1. <a name=''></a>内存部署

Flink 会根据`总内存`和`占比`计算出该内存部分的大小。

`计算得到的内存大小`将受限于相应的`最大值、最小值范围`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2dve40yew000.png)

###  1.1. <a name='-flink.size'></a>独立部署模式-flink.size

我们通常更关注 Flink 应用本身使用的内存大小。

* 建议配置 Flink 总内存（taskmanager.memory.flink.size 或者 jobmanager.memory.flink.size）或其组成部分。

此外，如果出现 Metaspace 不足的问题，可以调整 JVM Metaspace 的大小。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3knrgotat9i0.png)

####  1.1.1. <a name='OutOfMemoryError:Directbuffermemory'></a>OutOfMemoryError: Direct buffer memory

该异常通常说明 JVM 的直接内存限制过小，或者存在直接内存泄漏（Direct Memory Leak）。 请确认用户代码及外部依赖中是否使用了 JVM 直接内存，以及如果使用了直接内存，是否配置了足够的内存空间。 可以通过调整`堆外内存`来增大直接内存限制。

####  1.1.2. <a name='OutOfMemoryError:Javaheapspace'></a>OutOfMemoryError: Java heap space

该异常说明 JVM 的堆空间过小。 可以通过增大`总内存process.size`、TaskManager 的`任务堆内存`、JobManager 的 `JVM 堆内存`等方法来增大 JVM 堆空间。

提示: 也可以增大 TaskManager 的`框架堆内存`。 这是一个进阶配置，只有在确认是 `Flink 框架自身`需要更多内存时才应该去调整。

###  1.2. <a name='-process.size'></a>容器化部署模式-process.size

（Containerized Deployment）下（Kubernetes 或 Yarn）

* 建议配置进程`总内存`（taskmanager.memory.process.size 或者 jobmanager.memory.process.size）。
* 该配置参数用于指定分配给 Flink JVM 进程的总内存，也就是需要申请的容器大小。
* 如果配置了 Flink 总内存，Flink 会自动加上 JVM 相关的内存部分，根据推算出的进程总内存大小申请容器。

如果 Flink 或者用户代码分配超过`容器大小的非托管的堆外（本地）内存`，部署环境可能会杀掉`超用内存的容器`，造成作业执行失败。

####  1.2.1. <a name='Container'></a>容器（Container）内存超用

Flink 没有预留出足够的本地内存。

可以通过`外部监控系统`或者`容器被部署环境`杀掉时的`错误信息`判断是否存在容器内存超用。

对于 JobManager 进程，你还可以尝试启用 `JVM 直接内存限制（jobmanager.memory.enable-jvm-direct-memory-limit）`，以排除 JVM 直接`内存泄漏`的可能性。

###  1.3. <a name='-managed.size'></a>托管内存 - managed.size

`托管内存`是由 Flink 负责分配和管理的`本地（堆外）内存`。

以下场景需要使用`托管内存`：

* 流处理作业中用于 `RocksDB State Backend`。
* 流处理和批处理作业中用于`排序、哈希表及缓存中间结果`。
* 流处理和批处理作业中用于在 `Python 进程`中执行`用户自定义函数`。

可以通过以下两种范式指定`托管内存`的大小：

* 通过 taskmanager.memory.managed.size 明确指定其大小。
* 通过 taskmanager.memory.managed.fraction 指定在Flink 总内存中的占比。

####  1.3.1. <a name='StateBackend'></a>State Backend 的内存配置

在部署 Flink 流处理应用时，可以根据 State Backend 的类型对`集群`的配置进行优化。

####  1.3.2. <a name='HeapStateBackend'></a>Heap State Backend

执行`无状态作业`或者使用 Heap State Backend（MemoryStateBackend 或 FsStateBackend）时，建议将`托管内存`设置为 0。 这样能够最大化分配给 JVM 上用户代码的内存。

####  1.3.3. <a name='RocksDBStateBackend'></a>RocksDB State Backend

RocksDBStateBackend 使用`本地内存`。 默认情况下，RocksDB 会限制其内存用量不超过用户配置的`托管内存`。 因此，使用这种方式存储状态时，配置足够多的`托管内存`是十分重要的。 如果你关闭了 `RocksDB 的内存控制`，那么在容器化部署模式下如果 RocksDB 分配的内存超出了`申请容器的大小（进程总内存）`，可能会造成 TaskExecutor 被部署环境杀掉。 请同时参考如何调整 `RocksDB 内存`以及 `state.backend.rocksdb.memory.managed`。

####  1.3.4. <a name='-1'></a>批处理作业的内存配置

Flink 批处理算子使用`托管内存`来提高处理效率。

算子运行时，部分操作可以直接在原始数据上进行，而无需将数据反`序列化成 Java 对象`。

这意味着`托管内存`对应用的性能具有实质上的影响。

因此 Flink 会在不超过`其配置限额`的前提下，尽可能分配更多的`托管内存`。

Flink 明确知道可以使用的`内存大小`，因此可以有效避免 `OutOfMemoryError` 的发生。 当`托管内存`不足时，Flink 会优雅地将数据落盘。

####  1.3.5. <a name='-1'></a>消费者权重

对于包含不同种类的`托管内存`消费者的作业，可以进一步控制`托管内存`如何在`消费者`之间分配。

通过 taskmanager.memory.managed.consumer-weights 可以为每一种类型的`消费者`指定一个权重，

Flink 会按照权重的比例进行内存分配。

目前支持的消费者类型包括：

* OPERATOR: 用于内置算法。
* STATE_BACKEND: 用于流处理中的 RocksDB State Backend。
* PYTHON：用户 Python 进程。
  
例如，一个流处理作业同时使用到了 `RocksDB State Backend` 和 `Python UDF`

* `消费者权重`设置为 `STATE_BACKEND:70,PYTHON:30`，
* 那么 Flink 会将 70% 的`托管内存`用于`RocksDB State Backend`，`30%` 留给 `Python 进程`。

提示:

* 对于未出现在`消费者权重`中的类型，Flink 将不会为其分配`托管内存`。 

* 默认情况下，`消费者权重`中包含了所有可能的`消费者类型`。 


##  2. <a name='JobManager'></a>JobManager 内存

> 如果你是在本地运行 Flink（例如在 IDE 中）而非创建一个集群，那么 JobManager 的内存配置将不会生效。

与 TaskManager 相比，JobManager 具有相似但更加简单的内存模型。

配置 JobManager 内存最简单的方法就是进程的配置总内存。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.731d3tyftmo.png)

如果已经明确设置了 JVM 堆内存，建议不要再设置进程总内存或 Flink 总内存，否则可能会造成内存配置冲突。Flink 会根据默认值或其他配置参数自动调整剩余内存部分的大小。用户需要至少选择其中一种进行配置：

  JobManager:

* jobmanager.memory.process.size -- 进程总内存
* jobmanager.memory.flink.size -- Flink 总内存
  * 指定由 `Flink 应用本身`使用的内存大小，
  * 最好选择配置 Flink 总内存。
* jobmanager.memory.heap.size -- JVM 堆内存

如果

* 同时配置了 `Flink 总内存` 和 `JVM 堆内存`，
* 且没有配置堆外内存，
  * 那么`堆外内存的大小`将会是
  * `Flink 总内存`减去`JVM 堆内存`。

###  2.1. <a name='heap.sizeJVM'></a>heap.size 配置 JVM 堆内存

如配置总内存中所述，另一种配置 JobManager 内存的方式是明确指定 JVM 堆内存的大小（jobmanager.memory.heap.size）

Flink 需要多少 JVM 堆内存，很大程度上取决于运行的作业数量、作业的结构及上述用户代码的需求。

JVM 堆空间的实际大小不受 Flink 掌控，而是取决于本地执行进程是如何启动的。

如果希望控制 JVM 的堆空间大小，可以在启动进程时明确地指定相关的 JVM 参数，即 -Xmx 和 -Xms。

在启动 JobManager 进程时，Flink 启动脚本及客户端通过设置 JVM 参数 -Xms 和 -Xmx 来管理 JVM 堆空间的大小

###  2.2. <a name='off-heap.size'></a>off-heap.size 配置堆外内存

堆外内存 = JVM 直接内存 + 本地内存

```s
 jobmanager.memory.enable-jvm-direct-memory-limit
# 设置是否启用 JVM 直接内存限制。
# 如果该配置项设置为 true，
# Flink 会根据配置的堆外内存大小设置 JVM 参数 
# XX:MaxDirectMemorySize
 jobmanager.memory.off-heap.size 
#  “OutOfMemoryError: Direct buffer memory” 的异常，可以尝试调大这项配置
```

###  2.3. <a name='-1'></a>详细配置

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.12twqtprqa68.png)

##  3. <a name='taskmanager'></a>taskmanager 内存

###  3.1. <a name='-1'></a>详细配置

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5e1g2ktk9h00.png)

如果你是将 Flink 作为一个单独的 Java 程序运行在你的电脑本地而非创建一个集群（例如在 IDE 中），那么只有下列配置会生效，其他配置参数则不会起到任何效果：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4qnz12t4j1q0.png)

###  3.2. <a name='-1'></a>异常分析

####  3.2.1. <a name='IOException:Insufficientnumberofnetworkbuffers'></a>IOException: Insufficient number of network buffers

[网络内存调优指南(英文)](https://nightlies.apache.org/flink/flink-docs-release-1.14/zh/docs/deployment/memory/network_mem_tuning/)

该异常仅与 TaskManager 相关。

该异常通常说明网络内存过小。 可以通过调整以下配置参数增大网络内存：

```js
taskmanager.memory.network.min
taskmanager.memory.network.max
taskmanager.memory.network.fraction
```
