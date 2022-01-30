<!-- vscode-markdown-toc -->
* 1. [flink-kafka连接器](#flink-kafka)
	* 1.1. [依赖](#)
	* 1.2. [Kafka Consumer](#KafkaConsumer)
		* 1.2.1. [配置 Kafka Consumer 开始消费的位置](#KafkaConsumer-1)
		* 1.2.2. [Kafka Consumer 和容错](#KafkaConsumer-1)
		* 1.2.3. [Kafka Consumer Topic 和分区发现](#KafkaConsumerTopic)
		* 1.2.4. [Kafka Consumer 提交 Offset 的行为配置](#KafkaConsumerOffset)
		* 1.2.5. [Kafka Consumer 和 时间戳抽取以及 watermark 发送](#KafkaConsumerwatermark)
	* 1.3. [Kafka Producer](#KafkaProducer)
		* 1.3.1. [SerializationSchema](#SerializationSchema)
		* 1.3.2. [Kafka Producer 和容错](#KafkaProducer-1)
	* 1.4. [Kafka 连接器指标](#Kafka)
	* 1.5. [启用 Kerberos 身份验证](#Kerberos)
	* 1.6. [升级到最近的连接器版本](#-1)
	* 1.7. [问题排查](#-1)
		* 1.7.1. [数据丢失](#-1)
		* 1.7.2. [UnknownTopicOrPartitionException](#UnknownTopicOrPartitionException)
		* 1.7.3. [ProducerFencedException](#ProducerFencedException)
* 2. [flink-kafka的发展](#flink-kafka-1)
	* 2.1. [Flink 1.7 版本](#Flink1.7)
	* 2.2. [Flink 1.8 版本](#Flink1.8)
	* 2.3. [Flink 1.10 版本](#Flink1.10)
	* 2.4. [Flink 1.11 版本](#Flink1.11)
	* 2.5. [Flink 1.12 版本](#Flink1.12)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->


##  1. <a name='flink-kafka'></a>flink-kafka连接器

[Flink 提供了 Apache Kafka 连接器](https://nightlies.apache.org/flink/flink-docs-release-1.14/zh/docs/connectors/datastream/kafka/)，用于从 Kafka topic 中读取或者向其中写入数据，可提供精确一次的处理语义。

###  1.1. <a name=''></a>依赖

```xml
<dependency>
    <groupId>org.apache.flink</groupId>
    <artifactId>flink-connector-kafka_2.11</artifactId>
    <version>1.14.0</version>
</dependency>
```

###  1.2. <a name='KafkaConsumer'></a>Kafka Consumer

####  1.2.1. <a name='KafkaConsumer-1'></a>配置 Kafka Consumer 开始消费的位置

####  1.2.2. <a name='KafkaConsumer-1'></a>Kafka Consumer 和容错

####  1.2.3. <a name='KafkaConsumerTopic'></a>Kafka Consumer Topic 和分区发现

####  1.2.4. <a name='KafkaConsumerOffset'></a>Kafka Consumer 提交 Offset 的行为配置

####  1.2.5. <a name='KafkaConsumerwatermark'></a>Kafka Consumer 和 时间戳抽取以及 watermark 发送

###  1.3. <a name='KafkaProducer'></a>Kafka Producer

####  1.3.1. <a name='SerializationSchema'></a>SerializationSchema

####  1.3.2. <a name='KafkaProducer-1'></a>Kafka Producer 和容错

###  1.4. <a name='Kafka'></a>Kafka 连接器指标

###  1.5. <a name='Kerberos'></a>启用 Kerberos 身份验证 

###  1.6. <a name='-1'></a>升级到最近的连接器版本 

###  1.7. <a name='-1'></a>问题排查

####  1.7.1. <a name='-1'></a>数据丢失

####  1.7.2. <a name='UnknownTopicOrPartitionException'></a>UnknownTopicOrPartitionException

####  1.7.3. <a name='ProducerFencedException'></a>ProducerFencedException

##  2. <a name='flink-kafka-1'></a>flink-kafka的发展

###  2.1. <a name='Flink1.7'></a>Flink 1.7 版本

社区添加了 Kafka 2.0 连接器，可以从 Kafka 2.0 读写数据时保证 Exactly-Once 语义。

###  2.2. <a name='Flink1.8'></a>Flink 1.8 版本

`FlinkKafkaConsumer` 现在将根据 topic 规范过滤已恢复的分区

更改为表API连接器jar的命名: `Kafka/elasticsearch6 sql-jars`的命名方案已经更改。在maven术语中，它们不再具有`sql-jar限定符`，而artifactId现在以前缀为例，`flink-sql`而不是flink。例如`flink-sql-connector-kafka`。

connector变动: 

对于`FlinkKafkaConsumer`，我们推出了一个新的`KafkaDeserializationSchema`，可以直接访问`KafkaConsumerRecord`。这包含了该 `KeyedSerializationSchema`功能，该功能已弃用但目前仍可以使用。

`FlinkKafkaConsumer`现在将根据`topic 规范`过滤恢复的分区

在以前的版本中不存在`FlinkKafkaConsumer`。

如果您想保留以前的行为。请使用上面的

`disableFilterRestoredPartitionsWithSubscribedTopics()`

配置方法`FlinkKafkaConsumer`。

考虑这个例子：如果你有一个正在消耗topic的Kafka Consumer A，你做了一个`保存点`，然后改变你的Kafka消费者而不是从topic消费 B，然后从`保存点`重新启动你的工作。在此更改之前，您的消费者现在将使用这`两个主题A，B`因为它存储在消费者正在使用topic消费的`状态A`。通过此更改，您的使用者将仅B在还原后使用topic，因为我们使用`配置的topic`过滤状态中`存储的topic`。

###  2.3. <a name='Flink1.10'></a>Flink 1.10 版本

Kafka 0.8 和 0.9 的 connector 已被标记为废弃并不再主动支持。

###  2.4. <a name='Flink1.11'></a>Flink 1.11 版本

Table & SQL 支持 `Change Data Capture（CDC）`

`CDC` 被广泛使用在`复制数据`、`更新缓存`、`微服务`间`同步数据`、`审计日志`等场景，很多公司都在使用开源的 `CDC 工具`，如 `MySQL CDC`。通过 Flink 支持在 `Table & SQL` 中接入和解析 CDC 是一个强需求，在过往的很多讨论中都被提及过，可以帮助用户以实时的方式处理 `changelog 流`，进一步扩展 Flink 的应用场景，例如把 MySQL 中的数据同步到 `PG` 或 `ElasticSearch` 中，低延时的 `temporal join` 一个 `changelog` 等。

除了考虑到上面的真实需求，Flink 中定义的“`Dynamic Table`”概念在流上有两种模型：`append 模式`和 `update 模式`。通过 `append 模式`把流转化为“`Dynamic Table`”在之前的版本中已经支持，因此在 1.11.0 中进一步支持 `update 模式`也从概念层面完整的实现了“`Dynamic Table`”。

在公开的 `CDC` 调研报告中，Debezium 和 Canal 是用户中最流行使用的 `CDC` 工具，这两种工具用来同步 changelog 到其它的系统中，如`消息队列`。据此，首先支持了 Debezium 和 Canal 这两种格式，而且 `Kafka source` 也已经可以支持解析上述格式并输出`更新事件`，在后续的版本中会进一步支持 `Avro（Debezium）` 和 `Protobuf（Canal）`。

```sql
CREATE TABLE my_table (  
...) WITH (  
'connector'='...', -- e.g. 'kafka'  
'format'='debezium-json',  
'debezium-json.schema-include'='true' -- default: false (Debezium can be configured to include or exclude the message schema)  
'debezium-json.ignore-parse-errors'='true' -- default: false
);
```

全新 Source API:

- source 和 sink 是 Flink 对接外部系统的一个桥梁
- split 的`发现`逻辑和数据`消费`都耦合在 source function 的实现中，在实现 Kafka 或 Kinesis 类型的 source 时增加了复杂性。
- 首先在 Job Manager 和 Task Manager 中分别引入两种不同的组件 Split Enumerator 和 Source reader，解耦 split `发现`和对应的`消费`处理，同时方便随意组合不同的策略。比如现有的 `Kafka connector` 中有多种不同的 partition `发现`策略和`消费`耦合在一起，在新的架构下，我们只需要实现一种 source reader，就可以适配多种 split enumerator 的实现来对应不同的 partition 发现策略。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.40xiwpalwxg0.png)

###  2.5. <a name='Flink1.12'></a>Flink 1.12 版本

扩展了 `Kafka SQL connector`，使其可以在 `upsert 模式`下工作，并且支持在 `SQL DDL` 中处理 `connector 的 metadata`。现在，`时态表 Join` 可以完全用 `SQL` 来表示，不再依赖于 `Table API` 了。

Table API/SQL 变更:

1.  SQL Connectors 中的 Metadata 处理
   如果可以将某些 source（和 format）的 `Metadata` 作为`额外字段`暴露给用户，对于需要将`Metadata`与 `记录数据` 一起处理的用户来说很有意义。一个常见的例子是 `Kafka`，用户可能需要访问 `offset`、`partition` 或 `topic 信息`、读写 kafka 消息中的 `key` 或 使用消息 `metadata` 中的`时间戳`进行`时间`相关的操作。在 Flink 1.12 中，Flink SQL 支持了 `Metadata` 列用来读取和写入每行数据中 `connector` 或 `format` 相关的列。这些列在 `CREATE TABLE` 语句中使用 `METADATA（保留）关键字`来声明

```sql
CREATE TABLE kafka_table (
id BIGINT,
name STRING,
event_time TIMESTAMP(3) METADATA FROM 'timestamp', -- access Kafka 'timestamp' metadata
headers MAP METADATA -- access Kafka 'headers' metadata
) WITH (
'connector' = 'kafka',
'topic' = 'test-topic',
'format' = 'avro'
);
```

在 Flink 1.12 中，已经支持 `Kafka` 和 `Kinesis connector` 的`元数据`，并且 F`ileSystem connector` 上的相关工作也已经在计划中。由于 `Kafka record` 的结构比较复杂，社区还专门为 `Kafka connector` 实现了新的属性，以控制如何处理`键／值对`。

1.  Upsert Kafka Connector
   在某些场景中，例如读取 `compacted topic` 或者输出（更新）聚合结果的时候，需要将 Kafka 消息记录的 key 当成主键处理，用来确定一条数据是应该作为插入、删除还是更新记录来处理。为了实现该功能，社区为 Kafka 专门新增了一个 `upsert connector（upsert-kafka）`，该 connector 扩展自现有的 `Kafka connector`，工作在 `upsert 模式`下。新的 `upsert-kafka connector` 既可以作为 `source` 使用，也可以作为 `sink` 使用，并且提供了与现有的 `kafka connector` 相同的`基本功能`和`持久性保证`，因为两者之间复用了大部分代码。要使用 u`psert-kafka connector`，必须在`创建表`时`定义主键`，并为`键（key.format）`和`值（value.format）`指定`序列化` `反序列化`格式。
3.  SQL 中 支持 Temporal Table Join
   在之前的版本中，用户需要通过创建`时态表函数（temporal table function）` 来支持`时态表 join（temporal table join）` ，而在 Flink 1.12 中，用户可以使用`标准的 SQL 语句` FOR SYSTEM_TIME AS OF来支持 join。此外，现在任意包含`时间列`和`主键`的表，都可以作为`时态表`，而不仅仅是 `append-only` 表。这带来了一些新的应用场景，比如将 `Kafka compacted topic` 或`数据库变更日志`（来自 Debezium 等）作为`时态表`。

```sql
CREATE TABLE orders (
    order_id STRING,
    currency STRING,
    amount INT,              
    order_time TIMESTAMP(3),                
    WATERMARK FOR order_time AS order_time - INTERVAL '30' SECOND
) WITH (
  …
);

-- Table backed by a Kafka compacted topic
CREATE TABLE latest_rates ( 
    currency STRING,
    rate DECIMAL(38, 10),
    currency_time TIMESTAMP(3),
    WATERMARK FOR currency_time AS currency_time - INTERVAL ‘5’ SECOND,
    PRIMARY KEY (currency) NOT ENFORCED      
) WITH (
  'connector' = 'upsert-kafka',
  …
);

-- Event-time temporal table join
SELECT 
  o.order_id,
  o.order_time,
  o.amount * r.rate AS amount,
  r.currency
FROM orders AS o, latest_rates FOR SYSTEM_TIME AS OF o.order_time r
ON o.currency = r.currency;
```

上面的示例同时也展示了如何在 temporal table join 中使用 Flink 1.12 中新增的 `upsert-kafka connector`。

`Kafka Connector` 支持 `Watermark` 下推 :

为了确保使用 Kafka 的作业的结果的正确性，通常来说，最好基于`分区`来生成 `watermark`，因为`分区`内数据的乱序程度通常来说比`分区`之间数据的乱序程度要低很多。Flink 现在允许将 `watermark` 策略下推到 `Kafka connector` 里面，从而支持在 `Kafka connector 内部构造`基于分区的 `watermark`。一个 `Kafka source` 节点最终所产生的 `watermark` 由该节点所读取的`所有分区中的 watermark` 的最小值决定，从而使整个系统可以获得更好的（即更接近真实情况）的 `watermark`。该功能也允许用户配置基于分区的`空闲检测策略`，以防止`空闲分区`阻碍整个作业的 `event time 增长`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3lwuxrj8m040.png)


