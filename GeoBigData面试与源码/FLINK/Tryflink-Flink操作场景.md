## Flink 操作场景

[可以以多种方式在不同的环境中部署](https://nightlies.apache.org/flink/flink-docs-release-1.14/zh/docs/try-flink/flink-operations-playground/)

- 如何管理和运行 Flink 任务
- 了解如何部署和监控应用程序
- 如何从失败作业中进行恢复
- 执行一些日常操作任务，如升级和扩容

## 一些定义

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
