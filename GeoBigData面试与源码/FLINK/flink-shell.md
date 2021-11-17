
## SQL 客户端

[SQL 客户端](https://nightlies.apache.org/flink/flink-docs-release-1.14/zh/docs/dev/table/sqlclient/)命令行界面（CLI） 能够在命令行中检索和可视化分布式应用中实时产生的结果。

## Python REPL

你可以通过PyPi安装PyFlink，然后使用[Python Shell](https://nightlies.apache.org/flink/flink-docs-release-1.14/zh/docs/deployment/repls/python_shell/):

## Scala REPL

Flink附带了一个集成的交互式Scala Shell。既可以在本地设置中使用，也可以在集群设置中使用。

要使用一个集成的Flink集群的shell，只需执行:

```scala
bin/start-scala-shell.sh local
```

### DataSet API 

下面的例子将在Scala shell中执行wordcount程序:

```scala
Scala-Flink> 

val text = benv.fromElements(
  "To be, or not to be,--that is the question:--",
  "Whether 'tis nobler in the mind to suffer",
  "The slings and arrows of outrageous fortune",
  "Or to take arms against a sea of troubles,")

Scala-Flink> 

val counts = text
    .flatMap { _.toLowerCase.split("\\W+") }
    .map { (_, 1) }.groupBy(0).sum(1)

Scala-Flink> 

counts.print()

Scala-Flink> 

benv.execute("MyProgram")
```

print()命令将自动将指定的任务发送到JobManager执行，并将在终端中显示计算的结果。

### DataStream API

类似于上面的批处理程序，我们可以通过DataStream API来执行流程序:

```scala
Scala-Flink> 

val textStreaming = senv.fromElements(
  "To be, or not to be,--that is the question:--",
  "Whether 'tis nobler in the mind to suffer",
  "The slings and arrows of outrageous fortune",
  "Or to take arms against a sea of troubles,")

Scala-Flink> 

val countsStreaming = textStreaming
    .flatMap { _.toLowerCase.split("\\W+") }
    .map { (_, 1) }.keyBy(_._1).sum(1)

Scala-Flink> 

countsStreaming.print()

Scala-Flink> 

senv.execute("Streaming Wordcount")
```

注意，在流式的情况下，打印操作不会直接触发执行。

Flink Shell带有命令历史和自动补全功能。

### Table APi

#### stream

```scala
Scala-Flink> 
// --------------变化--------------
val stenv = StreamTableEnvironment.create(senv)
Scala-Flink> 
// --------------变化--------------

val textSource = stenv.fromDataStream(
  senv.fromElements(
    "To be, or not to be,--that is the question:--",
    "Whether 'tis nobler in the mind to suffer",
    "The slings and arrows of outrageous fortune",
    "Or to take arms against a sea of troubles,"),
  'text)

Scala-Flink> 

import org.apache.flink.table.functions.TableFunction

Scala-Flink> 

class $Split extends TableFunction[String] {
    def eval(s: String): Unit = {
      s.toLowerCase.split("\\W+").foreach(collect)
    }
  }

Scala-Flink> 

val split = new $Split

Scala-Flink> 

textSource.join(split('text) as 'word).
    groupBy('word).select('word, 'word.count as 'count).toChangelogStream().print

Scala-Flink> 

// --------------变化--------------
senv.execute("Table Wordcount")
// --------------变化--------------
```

#### batch

```scala
Scala-Flink> 

// --------------变化--------------
val btenv = StreamTableEnvironment.create(senv, EnvironmentSettings.inBatchMode())
// --------------变化--------------

Scala-Flink> 

val textSource = btenv.fromDataStream(
  senv.fromElements(
    "To be, or not to be,--that is the question:--",
    "Whether 'tis nobler in the mind to suffer",
    "The slings and arrows of outrageous fortune",
    "Or to take arms against a sea of troubles,"), 
  'text)

Scala-Flink> 

import org.apache.flink.table.functions.TableFunction

Scala-Flink> 

class $Split extends TableFunction[String] {
    def eval(s: String): Unit = {
      s.toLowerCase.split("\\W+").foreach(collect)
    }
  }

Scala-Flink> 

val split = new $Split

Scala-Flink> 

textSource.join(split('text) as 'word).
    groupBy('word).select('word, 'word.count as 'count).toDataStream().print
```

### SQL

#### stream

```scala
Scala-Flink> 

// --------------变化--------------
val stenv = StreamTableEnvironment.create(senv)
// --------------变化--------------

Scala-Flink> 

val textSource = stenv.fromDataStream(
  senv.fromElements(
    "To be, or not to be,--that is the question:--",
    "Whether 'tis nobler in the mind to suffer",
    "The slings and arrows of outrageous fortune",
    "Or to take arms against a sea of troubles,"), 
  'text)

Scala-Flink> 

stenv.createTemporaryView("text_source", textSource)

Scala-Flink> 

import org.apache.flink.table.functions.TableFunction

Scala-Flink> 

class $Split extends TableFunction[String] {
    def eval(s: String): Unit = {
      s.toLowerCase.split("\\W+").foreach(collect)
    }
  }

Scala-Flink> 

stenv.createTemporarySystemFunction("split", new $Split)

Scala-Flink> 

val result = stenv.sqlQuery("""SELECT T.word, count(T.word) AS `count` 
    FROM text_source 
    JOIN LATERAL table(split(text)) AS T(word) 
    ON TRUE 
    GROUP BY T.word""")

Scala-Flink> 

// --------------变化--------------
result.toChangelogStream().print
// --------------变化--------------

Scala-Flink> 

// --------------变化--------------
senv.execute("SQL Wordcount")
// --------------变化--------------
```

#### batch

```scala
Scala-Flink> 

// --------------变化--------------
val btenv = StreamTableEnvironment.create(senv, EnvironmentSettings.inBatchMode())
// --------------变化--------------

Scala-Flink> 

val textSource = btenv.fromDataStream(
  senv.fromElements(
    "To be, or not to be,--that is the question:--",
    "Whether 'tis nobler in the mind to suffer",
    "The slings and arrows of outrageous fortune",
    "Or to take arms against a sea of troubles,"), 
  'text)

Scala-Flink> 

btenv.createTemporaryView("text_source", textSource)

Scala-Flink> 

import org.apache.flink.table.functions.TableFunction

Scala-Flink> 

class $Split extends TableFunction[String] {
    def eval(s: String): Unit = {
      s.toLowerCase.split("\\W+").foreach(collect)
    }
  }

Scala-Flink> 

btenv.createTemporarySystemFunction("split", new $Split)

Scala-Flink> 

val result = btenv.sqlQuery("""SELECT T.word, count(T.word) AS `count` 
    FROM text_source 
    JOIN LATERAL table(split(text)) AS T(word) 
    ON TRUE 
    GROUP BY T.word""")

Scala-Flink> 

// --------------变化--------------
result.toDataStream().print
// --------------变化--------------
```

### Adding external dependencies

可以向Scala-shell添加外部类路径。

当调用execute时，它们将与shell程序一起自动发送到Jobmanager。

使用参数

```scala
 -a <path/to/jar.jar> 
 
 or 
 
 --addclasspath <path/to/jar.jar> 
```

来加载额外的类。

```scala
bin/start-scala-shell.sh [local | remote <host> <port> | yarn] --addclasspath <path/to/jar.jar>
```

### 安装程序

```s
bin/start-scala-shell.sh --help

# Local
bin/start-scala-shell.sh local

# Remote
bin/start-scala-shell.sh remote <hostname> <portnumber>

# Yarn Scala Shell cluster
# 使用 2个taskmanager 来启动 Yarn集群:
bin/start-scala-shell.sh yarn -n 2

# 如果你之前已经使用Flink Yarn Session部署了一个Flink集群，那么Scala shell可以使用以下命令连接它:

bin/start-scala-shell.sh yarn
```