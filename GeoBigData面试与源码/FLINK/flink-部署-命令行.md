<!-- vscode-markdown-toc -->
* 1. [命令行接口(CLI) bin/ Flink](#CLIbinFlink)
	* 1.1. [Submitting a Job 提交一份工作](#SubmittingaJob)
	* 1.2. [Job Monitoring 工作监控](#JobMonitoring)
	* 1.3. [Creating a Savepoint 创建一个保存点](#CreatingaSavepoint)
	* 1.4. [Disposing a Savepoint 处理一个保存点](#DisposingaSavepoint)
	* 1.5. [优雅地停止作业,创建最终保存点](#)
	* 1.6. [不礼貌地取消工作](#-1)
	* 1.7. [从保存点开始一个Job](#Job)
	* 1.8. [选择部署目标](#-1)
	* 1.9. [提交PyFlink工作](#PyFlink)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->

##  1. <a name='CLIbinFlink'></a>命令行接口(CLI) bin/ Flink

通过 `bin/flink --help` 查看帮助

作用：用于运行`打包为JAR文件`的程序并控制它们的执行。

它连接到conf/flink-conf.yaml中指定的正在运行的JobManager。

###  1.1. <a name='SubmittingaJob'></a>Submitting a Job 提交一份工作

```s
./bin/flink run \
      --detached \
      ./examples/streaming/StateMachineExample.jar
```

`run`和`run-application命令`支持通过`-D参数`传递`额外的配置参数`。

- 例如，可以通过设置`-D pipeline.max-parallelism=120`来设置`作业的最大并行度`。
- 这个参数对于配置每个作业或应用程序模式的集群非常有用，因为您可以将任何配置参数传递给集群，而`不需要更改配置文件`。

```s
Usage with built-in data generator: StateMachineExample [--error-rate <probability-of-invalid-transition>] [--sleep <sleep-per-record-in-ms>]
Usage with Kafka: StateMachineExample --kafka-topic <topic> [--brokers <brokers>]
Options for both the above setups:
        [--backend <file|rocks>]
        [--checkpoint-dir <filepath>]
        [--async-checkpoints <true|false>]
        [--incremental-checkpoints <true|false>]
        [--output <filepath> OR null for stdout]
打印的使用信息列出了与作业相关的参数，如有必要，可以将这些参数添加到作业提交命令的末尾。
Using standalone source with error rate 0.000000 and sleep delay 1 millis

Job has been submitted with JobID cca7bc1061d61cf15238e92312c2fc20
```

返回的JobID存储在变量JOB_ID中，用于以下命令：

```s
export JOB_ID="cca7bc1061d61cf15238e92312c2fc20"
```

###  1.2. <a name='JobMonitoring'></a>Job Monitoring 工作监控

您可以使用`列表操作`监视任何正在运行的作业:

```s
./bin/flink list
```

```s
Waiting for response...
------------------ Running/Restarting Jobs -------------------
30.11.2020 16:02:29 : cca7bc1061d61cf15238e92312c2fc20 : State machine job (RUNNING)
--------------------------------------------------------------
No scheduled jobs.
```

已提交但尚未启动的作业将列在“计划作业”下。

###  1.3. <a name='CreatingaSavepoint'></a>Creating a Savepoint 创建一个保存点

可以创建保存点以保存作业的当前状态。所需的只是JobID：

```s
./bin/flink savepoint \
      $JOB_ID \ 
      /tmp/flink-savepoints
```

```s
Triggering savepoint for job cca7bc1061d61cf15238e92312c2fc20.
Waiting for response...
Savepoint completed. Path: file:/tmp/flink-savepoints/savepoint-cca7bc-bb1e257f0dab
You can resume your program from this savepoint with the run command.
```

保存点文件夹是可选的，如果没有设置state.savepoints.dir，则需要指定保存点文件夹。

稍后可以使用保存点的路径重新启动Flink作业。

###  1.4. <a name='DisposingaSavepoint'></a>Disposing a Savepoint 处理一个保存点

保存点操作也可用于删除保存点--需要用`--dispose`添加相应的保存点路径：

```s
./bin/flink savepoint \ 
      --dispose \
      /tmp/flink-savepoints/savepoint-cca7bc-bb1e257f0dab \ 
      $JOB_ID
```

```s
Disposing savepoint '/tmp/flink-savepoints/savepoint-cca7bc-bb1e257f0dab'.
Waiting for response...
Savepoint '/tmp/flink-savepoints/savepoint-cca7bc-bb1e257f0dab' disposed.
```

如果使用`custom state instances自定义状态实例`（例如`自定义还原状态custom reducing state`或RocksDB状态），则必须指定触发保存点的`程序JAR`的路径。否则，您将遇到ClassNotFoundException：

```s
./bin/flink savepoint \
      --dispose <savepointPath> \ 
      --jarfile <jarFile>
```

###  1.5. <a name=''></a>优雅地停止作业,创建最终保存点

```s
./bin/flink stop \
      --savepointPath /tmp/flink-savepoints \
      $JOB_ID
```

如果未设置state.savepoints.dir，则必须使用--savepointPath指定保存点文件夹。

如果要永久终止作业 ->

- 请使用`--drain标志`。
- but 如果要在以后某个时间点恢复作业，请不要排空管道，因为在恢复作业时可能会导致不正确的结果。

- 如果指定了`--drain标志`，那么将在最后一个`检查点屏障checkpoint barrier`之前发出`MAX_WATERMARK`。
- 这将触发所有已注册的`事件时间计时器 event-time timers`，从而清除等待`特定水印specific watermark`的任何状态，例如windows。
- 作业将一直运行，直到所有`源sources`正确关闭。
- 这允许作业完成对所有`飞行中数据in-flight data`的处理，从而在停止时获取`保存点savepoint`后生成一些要处理的记录。

```s
Suspending job "cca7bc1061d61cf15238e92312c2fc20" with a savepoint.
Savepoint completed. Path: file:/tmp/flink-savepoints/savepoint-cca7bc-bb1e257f0dab
```

###  1.6. <a name='-1'></a>不礼貌地取消工作

```s
./bin/flink cancel $JOB_ID
```

--withSavepoint 已经弃用

```s
Cancelling job cca7bc1061d61cf15238e92312c2fc20.
Cancelled job cca7bc1061d61cf15238e92312c2fc20.
```

相应的作业的状态将从“运行”转换为“已取消”。任何计算都将停止。

###  1.7. <a name='Job'></a>从保存点开始一个Job

可以使用run(和run-application)操作从保存点启动作业。

```s
./bin/flink run \
      --detached \ 
      --fromSavepoint /tmp/flink-savepoints/savepoint-cca7bc-bb1e257f0dab \
      ./examples/streaming/StateMachineExample.jar
```

该参数用于`引用`先前停止的作业的状态。将生成可用于维护作业的`新作业ID`。

```s
Usage with built-in data generator: StateMachineExample [--error-rate <probability-of-invalid-transition>] [--sleep <sleep-per-record-in-ms>]
Usage with Kafka: StateMachineExample --kafka-topic <topic> [--brokers <brokers>]
Options for both the above setups:
        [--backend <file|rocks>]
        [--checkpoint-dir <filepath>]
        [--async-checkpoints <true|false>]
        [--incremental-checkpoints <true|false>]
        [--output <filepath> OR null for stdout]

Using standalone source with error rate 0.000000 and sleep delay 1 millis

Job has been submitted with JobID 97b20a0a8ffd5c1d656328b0cd6436a6
```

如果在触发保存点时从程序中删除了作为程序一部分的运算符，并且仍要使用该保存点，则需要允许此操作: 如果您的程序删除了保存点的一部分操作符，这将非常有用。

```s
./bin/flink run \
      --fromSavepoint <savepointPath> \
      --allowNonRestoredState ...
```

###  1.8. <a name='-1'></a>选择部署目标

Flink兼容多个集群管理框架，如Kubernetes或YARN

bin/flink 提供了一个参数 `--target` 来处理不同的选项

`--target` 将覆盖 `conf/flink-conf.yaml` 中指定的 `execution.target`.

- yarn

```s
./bin/flink run --target yarn-session 
# : 提交到YARN集群中已经运行的flink
./bin/flink run --target yarn-per-job 
# : 在YARN集群上以Per-Job模式提交flink
./bin/flink run-application --target yarn-application
# : 在YARN集群上以应用模式提交flink

```

- Kubernetes

```s
./bin/flink run --target kubernetes-session
# : 提交到Kubernetes集群上已经运行的flink
./bin/flink run-application --target kubernetes-application
# : 提交在kubernetes集群上以应用程序模式旋转flink
```

- Standalone

```s
./bin/flink run --target local
: 在会话模式下使用小型集群进行本地提交
./bin/flink run --target remote
: 提交到已在运行的flink群集
```

###  1.9. <a name='PyFlink'></a>提交PyFlink工作

```s
python --version
# the version printed here must be 3.6+
```

运行PyFlink作业:

```s
./bin/flink run --python examples/python/table/word_count.py
```

使用其他source和资源文件运行PyFlink作业。在--pyFiles中指定的文件将添加到PYTHONPATH中，因此在Python代码中可用:

```s
./bin/flink run \
      --python examples/python/table/word_count.py \
      --pyFiles file:///user.txt,hdfs:///$namenode_address/username.txt
```

运行PyFlink作业，该作业将引用Java UDF或外部连接器。--jarfile中指定的JAR文件将上载到集群。

```s
./bin/flink run \
      --python examples/python/table/word_count.py \
      --jarfile <jarFile>
```

使用pyFiles和--pyModule中指定的主入口模块运行PyFlink作业:

```s
./bin/flink run \
      --pyModule table.word_count \
      --pyFiles examples/python/table
```

在主机<jobmanagerHost>上运行的特定JobManager上提交PyFlink作业（相应地调整命令）：

```s
./bin/flink run \
      --jobmanager <jobmanagerHost>:8081 \
      --python examples/python/table/word_count.py
```

在Per-Job模式下使用YARN集群运行PyFlink作业:

```s
./bin/flink run \
      --target yarn-per-job
      --python examples/python/table/word_count.py
```

在群集ID为<ClusterId>的本机Kubernetes群集上运行PyFlink应用程序，

需要安装PyFlink的docker映像：

```s
./bin/flink run-application \
      --target kubernetes-application \
      --parallelism 8 \
      -Dkubernetes.cluster-id=<ClusterId> \
      -Dtaskmanager.memory.process.size=4096m \
      -Dkubernetes.taskmanager.cpu=2 \
      -Dtaskmanager.numberOfTaskSlots=4 \
      -Dkubernetes.container.image=<PyFlinkImageName> \
      --pyModule word_count \
      --pyFiles /opt/flink/examples/python/table/word_count.py
```

除了上面提到的 --pyFiles, --pyModule 和 --python之外，还有一些其他与python相关的选项

- -py,--python
- -pym,--pyModule
- -pyfs,--pyFiles
- -pyarch,--pyArchives
- -pyclientexec,--pyClientExecutable
- -pyexec,--pyExecutable
- -pyreq,--pyRequirements