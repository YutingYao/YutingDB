<!-- vscode-markdown-toc -->
* 1. [启动 flink SQL 客户端](#flinkSQL)
* 2. [Flink 内置函数的完整列表](#Flink)
	* 2.1. [举个栗子--时间函数](#--)
* 3. [source 表 - 使用 CREATE TABLE 语句](#source-CREATETABLE)
	* 3.1. [举个栗子](#)
	* 3.2. [CREATE 语句](#CREATE)
		* 3.2.1. [CREATE TABLE](#CREATETABLE)
		* 3.2.2. [模式映射](#-1)
* 4. [watermark 策略](#watermark)
	* 4.1. [几种常用的 watermark 策略](#watermark-1)
	* 4.2. [一些栗子](#-1)
* 5. [LIKE 子句](#LIKE)
* 6. [连续查询](#-1)
* 7. [Sink 表 - 使用 INSERT INTO 语句](#Sink-INSERTINTO)
* 8. [Apache Kafka SQL 连接器](#ApacheKafkaSQL)
	* 8.1. [依赖](#-1)
	* 8.2. [如何创建 Kafka 表](#Kafka)
	* 8.3. [连接器 必选 参数](#-1)
	* 8.4. [连接器 可选 参数](#-1)
* 9. [Debezium Format](#DebeziumFormat)
	* 9.1. [CSV Format](#CSVFormat)
		* 9.1.1. [依赖](#-1)
		* 9.1.2. [如何创建使用 CSV 格式的表](#CSV)
	* 9.2. [Avro](#Avro)
	* 9.3. [依赖](#-1)
	* 9.4. [如何使用 Debezium Format](#DebeziumFormat-1)
	* 9.5. [消息体中包含 schema 信息](#schema)
	* 9.6. [可用 METADATA 元数据](#METADATA)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->
##  1. <a name='flinkSQL'></a>启动 flink SQL 客户端 

在安装文件夹中使用以下命令启动本地集群：

```sh
./bin/start-cluster.sh
```

SQL 客户端是一个交互式的客户端，用于向 Flink 提交 SQL 查询并将结果可视化。

```sh
./bin/sql-client.sh
```

让我们使用以下简单查询打印出 ‘Hello World’：

```sql
SELECT 'Hello World';
```

##  2. <a name='Flink'></a>Flink 内置函数的完整列表

[Flink 内置函数的完整列表](https://nightlies.apache.org/flink/flink-docs-release-1.14/zh/docs/dev/table/functions/systemfunctions/)

这些函数为用户在开发 SQL 查询时提供了一个功能强大的工具箱。 

###  2.1. <a name='--'></a>举个栗子--时间函数

例如，CURRENT_TIMESTAMP 将在执行时打印出机器的当前系统时间。

```sql
SELECT CURRENT_TIMESTAMP;
```

| SQL 函数  | Table 函数  | 描述
|---|---|---|
|LOCALTIMESTAMP|localTimestamp()|返回本地时区的当前 SQL 时间，返回类型为 TIMESTAMP(3)。在流模式下为每条记录进行取值。 但在批处理模式下，它在查询开始时计算一次，并对每一行使用相同的结果。|
|CURRENT_TIMESTAMP|currentTimestamp()|返回本地时区的当前 SQL 时间戳，返回类型为 TIMESTAMP_LTZ(3)。在`流模式`下为每条记录进行取值。 但在`批处理`模式下，它在查询开始时计算一次，并对每一行使用相同的结果。|
|CURRENT_ROW_TIMESTAMP()|不适用|返回本地时区的当前 SQL 时间戳，返回类型为 TIMESTAMP_LTZ(3)。无论是在批处理模式还是流模式下，都会为每条记录进行取值。|
|NOW()|不适用|返回本地时区的当前 SQL 时间戳，这是 CURRENT_TIMESTAMP 的同义词。|
|TIMESTAMP string|STRING.toTimestamp()|以“yyyy-MM-dd HH:mm:ss[.SSS]”的形式返回从字符串解析的 SQL 时间戳。|
|HOUR(timestamp)|不适用|从 SQL 时间戳 timestamp 返回小时单位部分的小时（0 到 23 之间的整数）数。相当于 EXTRACT(HOUR FROM timestamp)。 例如 MINUTE(TIMESTAMP '1994-09-27 13:14:15') 返回 14。|
|MINUTE(timestamp)|不适用|从 SQL 时间戳 timestamp 返回分钟单位的分钟数（0 到 59 之间的整数）。相当于 EXTRACT(MINUTE FROM timestamp)。 例如 MINUTE(TIMESTAMP '1994-09-27 13:14:15') 返回 14。|
|SECOND(timestamp)|不适用|从 SQL 时间戳 timestamp 返回秒单位部分的秒数（0 到 59 之间的整数）。相当于 EXTRACT(SECOND FROM timestamp)。 例如 SECOND(TIMESTAMP '1994-09-27 13:14:15') 返回 15。|
|DATE_FORMAT(timestamp, string)|不适用|将时间戳 timestamp 转换为日期格式字符串 string 指定格式的字符串值。格式字符串与 Java 的 SimpleDateFormat 兼容。|
|TIMESTAMPDIFF(timepointunit, timepoint1, timepoint2)| timestampDiff(TIMEPOINTUNIT, TIMEPOINT1, TIMEPOINT2)|返回 timepoint1 和 timepoint2 之间时间间隔。间隔的单位由第一个参数给出，它应该是以下值之一： SECOND，MINUTE，HOUR，DAY，MONTH 或 YEAR。|
|TO_TIMESTAMP(string1[, string2])|不适用|将 ‘UTC+0’ 时区下格式为 string2（默认为：‘yyyy-MM-dd HH:mm:ss’）的字符串 string1 转换为时间戳。|
|CURRENT_WATERMARK(rowtime)|不适用|返回给定时间列属性 rowtime 的当前水印，如果管道中的当前操作没有可用的上游操作的公共水印时则为 NULL。 函数的返回类型被推断为与提供的时间列属性匹配，但调整后的精度为 3。例如时间列属性为 TIMESTAMP_LTZ(9)，则函数将返回 TIMESTAMP_LTZ(3)。|

TIMESTAMP_LTZ 的一个例子：

```sql
CREATE TABLE 我的表 (
  `user_id` BIGINT,
  `name` STRING,
  `record_time` TIMESTAMP_LTZ(3) METADATA FROM 'timestamp'    
  -- 读取和写入Kafka记录的时间戳
  -- reads and writes a Kafka record's timestamp
) WITH (
  'connector' = 'kafka'
  ...
);
```

源数据中的时间戳数据表示为一个纪元 (epoch) 时间，通常是一个 long 值，例如 1618989564564，建议将事件时间属性定义在 TIMESTAMP_LTZ 列上：

```sql
CREATE TABLE user_actions (
 user_name STRING,
 data STRING,
 ts BIGINT,
 time_ltz AS TO_TIMESTAMP_LTZ(ts, 3),
 -- 声明time_ltz为事件时间属性，并使用5秒延迟水印策略
 -- declare time_ltz as event time attribute and use 5 seconds delayed watermark strategy
 WATERMARK FOR time_ltz AS time_ltz - INTERVAL '5' SECOND
) WITH (
 ...
);

SELECT TUMBLE_START(time_ltz, INTERVAL '10' MINUTE), COUNT(DISTINCT user_name)
FROM user_actions
GROUP BY TUMBLE(time_ltz, INTERVAL '10' MINUTE);
```

在上面的示例中，元数据列record_time成为表架构的一部分，可以像常规列一样进行转换和存储：

```sql
INSERT INTO 我的表 
SELECT 
  user_id, 
  name, 
  record_time + INTERVAL '1' SECOND FROM 我的表;
```

为方便起见，如果列名应用作标识元数据键，则可以省略FROM子句：

```sql
CREATE TABLE MyTable (
  `user_id` BIGINT,
  `name` STRING,
  `timestamp` BIGINT METADATA    
  -- 将时间戳转换为BIGINT类型
  -- cast the timestamp as BIGINT
) WITH (
  'connector' = 'kafka'
  ...
);
```

```sql
CREATE TABLE MyTable (
  `user_id` BIGINT,
  `name` STRING,
  `timestamp` BIGINT METADATA,       
  -- 查询到接收模式的一部分
  -- part of the query-to-sink schema
  `offset` BIGINT METADATA VIRTUAL,  
  -- not查询到接收模式的一部分
  -- 可以使用VIRTUAL关键字将元数据列排除在持久化之外。
) WITH (
  'connector' = 'kafka'
  ...
);
```

Computed Columns

计算列

```sql
CREATE TABLE MyTable (
  `user_id` BIGINT,
  `price` DOUBLE,
  `quantity` DOUBLE,
  `cost` AS price * quanitity,  
  -- 计算表达式并将结果提供给查询
  -- evaluate expression and supply the result to queries
) WITH (
  'connector' = 'kafka'
  ...
);
```

##  3. <a name='source-CREATETABLE'></a>source 表 - 使用 CREATE TABLE 语句

###  3.1. <a name=''></a>举个栗子

下面是一个示例，定义一个以 CSV 文件作为存储格式的 source 表，其中 emp_id，name，dept_id 作为 CREATE 表语句中的列。

```sql
CREATE TABLE 员工信息 (
    emp_id INT,
    name VARCHAR,
    部门_id INT
) WITH ( 
    'connector' = 'filesystem',
    'path' = '/path/to/something.csv',
    'format' = 'csv'
);
```

可以从该表中定义一个连续查询，当新行可用时读取并立即输出它们的结果。 例如，我们可以过滤出只在部门 1 中工作的员工。

```sql
SELECT * from 员工信息 WHERE 部门_id = 1;
```

###  3.2. <a name='CREATE'></a>CREATE 语句

目前 Flink SQL 支持下列 CREATE 语句：

- 根据指定的表名创建一个表，如果同名表已经在 catalog 中存在了，则无法注册。

  - CREATE TABLE

- 根据给定的表属性创建数据库。若数据库中已存在同名表会抛出异常。

  - CREATE DATABASE

- 根据给定的 query 语句创建一个视图。若数据库中已经存在同名视图会抛出异常.

  - CREATE VIEW

- 创建一个有 catalog 和数据库命名空间的 catalog function ，需要指定一个 identifier ，可指定 language tag。 若 catalog 中，已经有同名的函数注册了，则无法注册。

  - CREATE FUNCTION 

------------------------------------------

可以使用 TableEnvironment 中的 executeSql() 方法执行 CREATE 语句。 若 CREATE 操作执行成功，executeSql() 方法返回 ‘OK’，否则会抛出异常。

```scala
val tableEnv = TableEnvironment.create(...)

// 对已注册的表进行 SQL 查询
// 注册名为 “Orders” 的表
tableEnv.executeSql("CREATE TABLE Orders (`user` BIGINT, product STRING, amount INT) WITH (...)");
// 在表上执行 SQL 查询，并把得到的结果作为一个新的表
val result = tableEnv.sqlQuery(
  "SELECT product, amount FROM Orders WHERE product LIKE '%Rubber%'");

// 对已注册的表进行 INSERT 操作
// 注册 TableSink
tableEnv.executeSql("CREATE TABLE RubberOrders(product STRING, amount INT) WITH ('connector.path'='/path/to/file' ...)");
// 在表上执行 INSERT 语句并向 TableSink 发出结果
tableEnv.executeSql(
  "INSERT INTO RubberOrders SELECT product, amount FROM Orders WHERE product LIKE '%Rubber%'")
```

等价于

```sql
-- 对已注册的表进行 SQL 查询. 注册名为 “Orders” 的表
CREATE TABLE Orders 
(`user` BIGINT, product STRING, amount INT) 
WITH (...);

-- 在表上执行 SQL 查询，并把得到的结果作为一个新的表
SELECT product, amount 
FROM Orders 
WHERE product LIKE '%Rubber%'

```

```sql
CREATE TABLE RubberOrders 
(product STRING, amount INT) 
WITH (...);
```

```sql
-- 在表上执行 INSERT 语句并向 TableSink 发出结果
INSERT INTO RubberOrders 
SELECT product, amount 
FROM Orders 
WHERE product LIKE '%Rubber%';
```

####  3.2.1. <a name='CREATETABLE'></a>CREATE TABLE

Flink 提供了一套与表连接器（table connector）一起使用的表格式（table format）。表格式是一种存储格式，定义了如何把二进制数据映射到表的列上。

Flink 支持以下格式：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3jkdkyb4c6w0.png)

下面的语句创建一个仅包含常规列的表：

```sql
CREATE TABLE MyTable (
  `user_id` BIGINT,
  `name` STRING
) WITH (
  ...
);
```

下面的代码显示了如何连接到Kafka以读取和写入JSON记录的完整示例。

处理时间属性可以在创建表的 DDL 中用计算列的方式定义，用 PROCTIME() 就可以定义处理时间，函数 PROCTIME() 的返回类型是 TIMESTAMP_LTZ 。

```sql
CREATE TABLE user_actions (
  user_name STRING,
  data STRING,
  user_action_time AS PROCTIME() 
  -- 声明一个额外的列作为处理时间属性
) WITH (
  ...
);

SELECT 
TUMBLE_START(user_action_time, INTERVAL '10' MINUTE), 
COUNT(DISTINCT user_name)
FROM user_actions
GROUP BY TUMBLE(user_action_time, INTERVAL '10' MINUTE);
```

Flink 支持和在 TIMESTAMP 列和 TIMESTAMP_LTZ 列上定义事件时间。如果源数据中的时间戳数据表示为年-月-日-时-分-秒，则通常为不带时区信息的字符串值，例如 2020-04-15 20:13:40.564，建议将事件时间属性定义在 TIMESTAMP 列上:

```sql

CREATE TABLE user_actions (
  user_name STRING,
  data STRING,
  user_action_time TIMESTAMP(3),
  -- 声明 user_action_time 是事件时间属性，并且用 延迟 5 秒的策略来生成 watermark
  WATERMARK FOR user_action_time AS user_action_time - INTERVAL '5' SECOND
) WITH (
  ...
);

SELECT TUMBLE_START(user_action_time, INTERVAL '10' MINUTE), COUNT(DISTINCT user_name)
FROM user_actions
GROUP BY TUMBLE(user_action_time, INTERVAL '10' MINUTE);
```

以下是一个利用 Kafka 以及 JSON Format 构建表的例子。

```sql
CREATE TABLE MyUserTable (
  -- 声明表的模式 declare the schema of the table
  `user` BIGINT,
  `message` STRING,
  `rowtime` TIMESTAMP(3) METADATA FROM 'timestamp',    
  -- 使用元数据列访问 Kafka 的记录时间戳
  -- use a metadata column to access Kafka's record timestamp
  `proctime` AS PROCTIME(),    
  -- 使用计算列定义proctime属性
  -- use a computed column to define a proctime attribute
  WATERMARK FOR `rowtime` AS `rowtime` - INTERVAL '5' SECOND    
  -- 使用WATERMARK语句来定义行时间属性
  -- use a WATERMARK statement to define a rowtime attribute
) WITH (
  -- 声明要连接的外部系统
  -- declare the external system to connect to
  'connector' = 'kafka',
  'topic' = 'topic_name',
  'scan.startup.mode' = 'earliest-offset',
  'properties.bootstrap.servers' = 'localhost:9092',
  'format' = 'json'   
  -- 为这个系统声明一种格式
  -- declare a format for this system
)
```

####  3.2.2. <a name='-1'></a>模式映射

```sql
CREATE TABLE MyTable (
  MyField1 INT,
  MyField2 STRING,
  MyField3 BOOLEAN
) WITH (
  ...
)
```

```sql
CREATE TABLE MyTable (
  MyField1 INT,
  MyField2 STRING,
  MyField3 BOOLEAN,
  PRIMARY KEY (MyField1, MyField2) NOT ENFORCED  -- defines a primary key on columns
) WITH (
  ...
)
```

```sql
CREATE TABLE MyTable (
  MyField1 INT,
  MyField2 STRING,
  MyField3 BOOLEAN
  MyField4 AS PROCTIME() -- declares a proctime attribute
) WITH (
  ...
)
```

##  4. <a name='watermark'></a>watermark 策略

###  4.1. <a name='watermark-1'></a>几种常用的 watermark 策略

1. 严格递增时间戳：

```sql
WATERMARK FOR rowtime_column AS rowtime_column
```

发出到目前为止已观察到的最大时间戳的 watermark ，时间戳大于最大时间戳的行被认为没有迟到。

2. 递增时间戳：

```sql
WATERMARK FOR rowtime_column AS rowtime_column - INTERVAL '0.001' SECOND
```

发出到目前为止已观察到的最大时间戳减 1 的 watermark ，时间戳大于或等于最大时间戳的行被认为没有迟到。

3. 有界乱序时间戳：

```sql
WATERMARK FOR rowtime_column AS rowtime_column - INTERVAL 'string' timeUnit
```

发出到目前为止已观察到的最大时间戳减去指定延迟的 watermark 

例如，

```sql
WATERMARK FOR rowtime_column AS rowtime_column - INTERVAL '5' SECOND
```

是一个 5 秒延迟的 watermark 策略。

```sql
CREATE TABLE Orders (
    `user` BIGINT,
    product STRING,
    order_time TIMESTAMP(3),
    WATERMARK FOR order_time AS order_time - INTERVAL '5' SECOND
) WITH ( . . . );
```

###  4.2. <a name='-1'></a>一些栗子

```sql
-- 使用模式中现有的TIMESTAMP(3)字段作为行时间属性
-- use the existing TIMESTAMP(3) field in schema as the rowtime attribute
CREATE TABLE MyTable (
  ts_field TIMESTAMP(3),
  WATERMARK FOR ts_field AS ...
) WITH (
  ...
)

-- 使用系统函数或udf或表达式提取预期的TIMESTAMP(3)行时间字段
-- use system functions or UDFs or expressions to extract the expected TIMESTAMP(3) rowtime field
CREATE TABLE MyTable (
  log_ts STRING,
  ts_field AS TO_TIMESTAMP(log_ts),
  WATERMARK FOR ts_field AS ...
) WITH (
  ...
)
```

支持的 watermark 水印策略如下:

```sql
-- Sets a watermark strategy for strictly ascending rowtime attributes. Emits a watermark of the maximum observed timestamp so far. Rows that have a timestamp bigger to the max timestamp are not late.
-- 为严格升序的行时间属性设置水印策略。
-- 发出到目前为止观察到的最大时间戳的水印。
-- 时间戳大于最大时间戳的行不属于延迟。
CREATE TABLE MyTable (
  ts_field TIMESTAMP(3),
  WATERMARK FOR ts_field AS ts_field
) WITH (
  ...
)

-- Sets a watermark strategy for ascending rowtime attributes. Emits a watermark of the maximum observed timestamp so far minus 1. Rows that have a timestamp bigger or equal to the max timestamp are not late.
-- 设置升序行时间属性的水印策略。
-- 发出到目前为止观察到的最大时间戳减去1的水印。
-- 时间戳大于或等于最大时间戳的行不属于延迟。
CREATE TABLE MyTable (
  ts_field TIMESTAMP(3),
  WATERMARK FOR ts_field AS ts_field - INTERVAL '0.001' SECOND
) WITH (
  ...
)

-- Sets a watermark strategy for rowtime attributes which are out-of-order by a bounded time interval.
-- 为行时间属性设置水印策略，
-- 这些行时间属性在有限的时间间隔内是无序的。
-- Emits watermarks which are the maximum observed timestamp minus the specified delay, e.g. 2 seconds.
-- 发出的水印是观察到的最大时间戳减去指定的延迟，例如2秒。
CREATE TABLE MyTable (
  ts_field TIMESTAMP(3),
  WATERMARK FOR ts_field AS ts_field - INTERVAL '2' SECOND
) WITH (
  ...
)
```

##  5. <a name='LIKE'></a>LIKE 子句

LIKE 子句可以基于现有表的定义去创建新表，并且可以扩展或排除原始表中的某些部分。

LIKE 子句必须在 CREATE 语句中定义，并且是基于 CREATE 语句的更上层定义。

LIKE 子句可以用于定义表的多个部分，而不仅仅是 schema 部分。

```sql
CREATE TABLE Orders (
    `user` BIGINT,
    product STRING,
    order_time TIMESTAMP(3)
) WITH ( 
    'connector' = 'kafka',
    'scan.startup.mode' = 'earliest-offset'
);

CREATE TABLE Orders_with_watermark (
    -- 添加 watermark 定义
    WATERMARK FOR order_time AS order_time - INTERVAL '5' SECOND 
) WITH (
    -- 改写 startup-mode 属性
    'scan.startup.mode' = 'latest-offset'
)
LIKE Orders;
```

表属性的合并逻辑可以用 like options 来控制。

```sql
<like_options>:
{
   { INCLUDING | EXCLUDING } 
   { ALL | CONSTRAINTS | PARTITIONS }
 | 
 { INCLUDING - 新表包含源表（source table）所有的表属性，如果和源表的表属性重复则会直接失败，例如新表和源表存在相同 key 的属性。 
 | EXCLUDING - 新表不包含源表指定的任何表属性。
 | OVERWRITING - 新表包含源表的表属性，但如果出现重复项，则会用新表的表属性覆盖源表中的重复表属性，例如，两个表中都存在相同 key 的属性，则会使用当前语句中定义的 key 的属性值。
 } 
 { GENERATED - 计算列 
 | OPTIONS - 连接器信息、格式化方式等配置项 
 | WATERMARKS - watermark 定义 
 } 
}[, ...]
```

```sql

-- 存储在文件系统的源表
CREATE TABLE Orders_in_file (
    `user` BIGINT,
    product STRING,
    order_time_string STRING,
    order_time AS to_timestamp(order_time)
    
)
PARTITIONED BY (`user`) 
WITH ( 
    'connector' = 'filesystem',
    'path' = '...'
);

-- 对应存储在 kafka 的源表
CREATE TABLE Orders_in_kafka (
    -- 添加 watermark 定义
    WATERMARK FOR order_time AS order_time - INTERVAL '5' SECOND 
) WITH (
    'connector' = 'kafka',
    ...
)
LIKE Orders_in_file (
    -- 排除需要生成 watermark 的计算列之外的所有内容。
    -- 去除不适用于 kafka 的所有分区和文件系统的相关属性。
    EXCLUDING ALL
    INCLUDING GENERATED
);
```

##  6. <a name='-1'></a>连续查询

例如，假设你需要从传入的数据流中**计算每个部门的员工人数**。查询需要维护每个部门最新的计算总数，以便在处理新行时及时输出结果。

```sql
SELECT 
   部门_id,
   COUNT(*) as 员工人数 
FROM 员工信息 
GROUP BY 部门_id;
```

##  7. <a name='Sink-INSERTINTO'></a>Sink 表 - 使用 INSERT INTO 语句

```sql
INSERT INTO 部门_人数
SELECT 
   部门_id,
   COUNT(*) as 员工人数 
FROM 员工信息;
```

##  8. <a name='ApacheKafkaSQL'></a>Apache Kafka SQL 连接器

###  8.1. <a name='-1'></a>依赖

```xml
<dependency>
  <groupId>org.apache.flink</groupId>
  <artifactId>flink-connector-kafka_2.11</artifactId>
  <version>1.14.0</version>
</dependency>
```

###  8.2. <a name='Kafka'></a>如何创建 Kafka 表

```sql
CREATE TABLE KafkaTable (
  `user_id` BIGINT,
  `item_id` BIGINT,
  `behavior` STRING,
  `ts` TIMESTAMP(3) METADATA FROM 'timestamp'
) WITH (
  'connector' = 'kafka',
  'topic' = 'user_behavior',
  'properties.bootstrap.servers' = 'localhost:9092',
  'properties.group.id' = 'testGroup',
  'scan.startup.mode' = 'earliest-offset',
  'format' = 'csv'
)
```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4bq9ef9ry140.png)

以下扩展的 CREATE TABLE 示例展示了使用这些元数据字段的语法：

```sql
CREATE TABLE KafkaTable (
  `event_time` TIMESTAMP(3) METADATA FROM 'timestamp',
  `partition` BIGINT METADATA VIRTUAL,
  `offset` BIGINT METADATA VIRTUAL,
  `user_id` BIGINT,
  `item_id` BIGINT,
  `behavior` STRING
) WITH (
  'connector' = 'kafka',
  'topic' = 'user_behavior',
  'properties.bootstrap.servers' = 'localhost:9092',
  'properties.group.id' = 'testGroup',
  'scan.startup.mode' = 'earliest-offset',
  'format' = 'csv'
);
```

由于 Kafka 消息中消息键是可选的，以下语句将使用消息体格式读取和写入消息，但不使用消息键格式。 'format' 选项与 'value.format' 意义相同。 所有的格式配置使用格式识别符作为前缀。

```sql
CREATE TABLE KafkaTable (,
  `ts` TIMESTAMP(3) METADATA FROM 'timestamp',
  `user_id` BIGINT,
  `item_id` BIGINT,
  `behavior` STRING
) WITH (
  'connector' = 'kafka',
  ...

  'format' = 'json',
  'json.ignore-parse-errors' = 'true'
)
```

以下示例展示了如何配置和使用消息键和消息体格式。 格式配置使用 'key' 或 'value' 加上格式识别符作为前缀。

```sql
CREATE TABLE KafkaTable (
  `ts` TIMESTAMP(3) METADATA FROM 'timestamp',
  `user_id` BIGINT,
  `item_id` BIGINT,
  `behavior` STRING
) WITH (
  'connector' = 'kafka',
  ...

  'key.format' = 'json',
  'key.json.ignore-parse-errors' = 'true',
  'key.fields' = 'user_id;item_id',

  'value.format' = 'json',
  'value.json.fail-on-missing-field' = 'false',
  'value.fields-include' = 'ALL'
)
```

|参数|数据类型|描述|举例|
|---|---|---|---|
|key.format|String|用来序列化和反序列化 Kafka 消息键（Key）的格式。 请参阅 格式 页面以获取更多关于格式的细节和相关配置项。 注意：如果定义了键格式，则配置项 'key.fields' 也是必需的。 否则 Kafka 记录将使用空值作为键。|'json',|
|key.fields|List<String>|表结构中用来配置消息键（Key）格式数据类型的字段列表。默认情况下该列表为空，因此消息键没有定义。 列表格式为 'field1;field2'。|'user_id;item_id'|
|value.fields-include|枚举类型，可选值：[ALL, EXCEPT_KEY]|定义消息体（Value）格式如何处理消息键（Key）字段的策略。 默认情况下，表结构中 'ALL' 即所有的字段都会包含在消息体格式中，即消息键字段在消息键和消息体格式中都会出现。|'ALL'|


格式元数据的配置键以 'value.' 作为前缀

只读列必须声明为 VIRTUAL 以在 INSERT INTO 操作中排除它们。

```sql
-- 以下示例展示了如何获取 Kafka 和 Debezium 的元数据字段：
CREATE TABLE KafkaTable (
  `event_time` TIMESTAMP(3) METADATA FROM 'value.source.timestamp' VIRTUAL,  
  -- from Debezium format
  `origin_table` STRING METADATA FROM 'value.source.table' VIRTUAL, 
  -- from Debezium format
  `partition_id` BIGINT METADATA FROM 'partition' VIRTUAL,  
  -- from Kafka connector
  `offset` BIGINT METADATA VIRTUAL,  
  -- from Kafka connector
  `user_id` BIGINT,
  `item_id` BIGINT,
  `behavior` STRING
) WITH (
  'connector' = 'kafka',
  'topic' = 'user_behavior',
  'properties.bootstrap.servers' = 'localhost:9092',
  'properties.group.id' = 'testGroup',
  'scan.startup.mode' = 'earliest-offset',
  'value.format' = 'debezium-json'
);
```

如果消息键字段和消息体字段重名，连接器无法根据表结构信息将这些列区分开。

 'key.fields-prefix' 配置项可以在表结构中为消息键字段指定一个唯一名称，并在配置消息键格式的时候保留原名。

以下示例展示了在消息键和消息体中同时包含 version 字段的情况：

```sql
CREATE TABLE KafkaTable (
  `k_version` INT,
  `k_user_id` BIGINT,
  `k_item_id` BIGINT,
  `version` INT,
  `behavior` STRING
) WITH (
  'connector' = 'kafka',
  ...

  'key.format' = 'json',
  'key.fields-prefix' = 'k_',
  'key.fields' = 'k_version;k_user_id;k_item_id',

  'value.format' = 'json',
  'value.fields-include' = 'EXCEPT_KEY'
)
```

###  8.3. <a name='-1'></a>连接器 必选 参数

|参数|数据类型|描述|举例|
|---|---|---|---|
|connector|String|指定使用的连接器，Kafka 连接器使用 'kafka'。|'kafka'|
|properties.bootstrap.servers|String|逗号分隔的 Kafka broker 列表。|'localhost:9092'|
|'format'|String|用来序列化或反序列化 Kafka 消息的格式。 请参阅 格式 页面以获取更多关于格式的细节和相关配置项。 注意：该配置项和 'value.format' 二者必需其一。|'csv'|
|'value.format'|String|序列化和反序列化 Kafka 消息体时使用的格式。 请参阅 格式 页面以获取更多关于格式的细节和相关配置项。 注意：该配置项和 'format' 二者必需其一。|'debezium-json'|

###  8.4. <a name='-1'></a>连接器 可选 参数

|参数|数据类型|描述|举例|
|---|---|---|---|
|topic|String|当表用作 source 时读取数据的 topic 名。亦支持用分号间隔的 topic 列表，如 'topic-1;topic-2'。注意，对 source 表而言，'topic' 和 'topic-pattern' 两个选项只能使用其中一个。当表被用作 sink 时，该配置表示写入的 topic 名。注意 sink 表不支持 topic 列表。|'user_behavior'|
|properties.group.id|String|Kafka source 的消费组 id。如果未指定消费组 ID，则会使用自动生成的 "KafkaSource-{tableIdentifier}" 作为消费组 ID。|'testGroup'|
|scan.startup.mode|String|Kafka consumer 的启动模式。有效值为：'earliest-offset'，'latest-offset'，'group-offsets'，'timestamp' 和 'specific-offsets'。|'earliest-offset'|

scan.startup.mode 配置项决定了 Kafka consumer 的启动模式。有效值为：

- `group-offsets`：从 Zookeeper/Kafka 中某个指定的消费组已提交的偏移量开始。
- `earliest-offset`：从可能的最早偏移量开始。
- `latest-offset`：从最末尾偏移量开始。
- `timestamp`：从用户为每个 partition 指定的时间戳开始。
- `specific-offsets`：从用户为每个 partition 指定的偏移量开始。

##  9. <a name='DebeziumFormat'></a>Debezium Format

[Debezium Documentation](https://debezium.io/documentation/reference/1.3/connectors/mongodb.html)

Debezium 是一个 CDC（Changelog Data Capture，变更数据捕获）的工具

Debezium 为变更日志提供了统一的格式结构，并支持使用 JSON 和 Apache Avro 序列化消息。

###  9.1. <a name='CSVFormat'></a>CSV Format

####  9.1.1. <a name='-1'></a>依赖

```xml
<dependency>
  <groupId>org.apache.flink</groupId>
  <artifactId>flink-csv</artifactId>
  <version>1.14.0</version>
</dependency>
```

####  9.1.2. <a name='CSV'></a>如何创建使用 CSV 格式的表

```sql
CREATE TABLE user_behavior (
  user_id BIGINT,
  item_id BIGINT,
  category_id BIGINT,
  behavior STRING,
  ts TIMESTAMP(3)
) WITH (
 'connector' = 'kafka',
 'topic' = 'user_behavior',
 'properties.bootstrap.servers' = 'localhost:9092',
 'properties.group.id' = 'testGroup',
 'format' = 'csv',
 'csv.ignore-parse-errors' = 'true',
 'csv.allow-comments' = 'true'
)
```

|参数|数据类型|描述|举例|
|---|---|---|---|
|'csv.ignore-parse-errors'|Boolean|当解析异常时，是跳过当前字段或行，还是抛出错误失败（默认为 false，即抛出错误失败）。如果忽略字段的解析异常，则会将该字段值设置为null。|'true'|
|'csv.allow-comments'|Boolean|是否允许忽略注释行（默认不允许），注释行以 '#' 作为起始字符。 如果允许注释行，请确保 csv.ignore-parse-errors 也开启了从而允许空行。|'true'|

下面的表格列出了flink数据和CSV数据的对应关系。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.dk781s60968.png)

###  9.2. <a name='Avro'></a>Avro

[官方文档](https://avro.apache.org/docs/current/spec.html)

以下是一个使用 Kafka 连接器和 Confluent Avro 格式创建表的示例。

- 使用原始的 UTF-8 字符串作为 Kafka 的 key：

```sql
CREATE TABLE user_created (

  -- 该列映射到 Kafka 原始的 UTF-8 key
  the_kafka_key STRING,
  
  -- 映射到 Kafka value 中的 Avro 字段的一些列
  id STRING,
  name STRING,
  email STRING

) WITH (

  'connector' = 'kafka',
  'topic' = 'user_events_example1',
  'properties.bootstrap.servers' = 'localhost:9092',

  -- UTF-8 字符串作为 Kafka 的 keys，使用表中的 'the_kafka_key' 列
  'key.format' = 'raw',
  'key.fields' = 'the_kafka_key',

  'value.format' = 'avro-confluent',
  'value.avro-confluent.url' = 'http://localhost:8082',
  'value.fields-include' = 'EXCEPT_KEY'
)
```

- Kafka 的 key 和 value 在 Schema Registry 中都注册为 Avro 记录的表的示例：

```sql
CREATE TABLE user_created (
  
  -- 该列映射到 Kafka key 中的 Avro 字段 'id'
  kafka_key_id STRING,
  
  -- 映射到 Kafka value 中的 Avro 字段的一些列
  id STRING,
  name STRING, 
  email STRING
  
) WITH (

  'connector' = 'kafka',
  'topic' = 'user_events_example2',
  'properties.bootstrap.servers' = 'localhost:9092',

  -- 注意：由于哈希分区，在 Kafka key 的上下文中，schema 升级几乎从不向后也不向前兼容。
  'key.format' = 'avro-confluent',
  'key.avro-confluent.url' = 'http://localhost:8082',
  'key.fields' = 'kafka_key_id',

  -- 在本例中，我们希望 Kafka 的 key 和 value 的 Avro 类型都包含 'id' 字段
  -- => 给表中与 Kafka key 字段关联的列添加一个前缀来避免冲突
  'key.fields-prefix' = 'kafka_key_',

  'value.format' = 'avro-confluent',
  'value.avro-confluent.url' = 'http://localhost:8082',
  'value.fields-include' = 'EXCEPT_KEY',
   
  -- 自 Flink 1.13 起，subjects 具有一个默认值, 但是可以被覆盖：
  'key.avro-confluent.subject' = 'user_events_example2-key2',
  'value.avro-confluent.subject' = 'user_events_example2-value2'
)
```

```sql
INSERT INTO user_created
SELECT
  -- 将 user id 复制至映射到 kafka key 的列中
  id as the_kafka_key,

  -- 所有的 values
  id, name, email
FROM some_table
```

使用 upsert-kafka 连接器，Kafka 的 value 在 Schema Registry 中注册为 Avro 记录的表的示例：

```sql
CREATE TABLE user_created (
  
  -- 该列映射到 Kafka 原始的 UTF-8 key
  kafka_key_id STRING,
  
  -- 映射到 Kafka value 中的 Avro 字段的一些列
  id STRING, 
  name STRING, 
  email STRING, 
  
  -- upsert-kafka 连接器需要一个主键来定义 upsert 行为
  PRIMARY KEY (kafka_key_id) NOT ENFORCED

) WITH (

  'connector' = 'upsert-kafka',
  'topic' = 'user_events_example3',
  'properties.bootstrap.servers' = 'localhost:9092',

  -- UTF-8 字符串作为 Kafka 的 keys
  -- 在本例中我们不指定 'key.fields'，因为它由表的主键决定
  'key.format' = 'raw',
  
  -- 在本例中，我们希望 Kafka 的 key 和 value 的 Avro 类型都包含 'id' 字段
  -- => 给表中与 Kafka key 字段关联的列添加一个前缀来避免冲突
  'key.fields-prefix' = 'kafka_key_',

  'value.format' = 'avro-confluent',
  'value.avro-confluent.url' = 'http://localhost:8082',
  'value.fields-include' = 'EXCEPT_KEY'
)
```

这是使用 Kafka 连接器和 Avro format 创建表的示例。

```sql
CREATE TABLE user_behavior (
  user_id BIGINT,
  item_id BIGINT,
  category_id BIGINT,
  behavior STRING,
  ts TIMESTAMP(3)
) WITH (
 'connector' = 'kafka',
 'topic' = 'user_behavior',
 'properties.bootstrap.servers' = 'localhost:9092',
 'properties.group.id' = 'testGroup',
 'format' = 'avro'
)
```

Flink 类型到 Avro 类型的类型映射。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.zaozjs71zkw.png)

###  9.3. <a name='-1'></a>依赖

```xml
<!-- Debezium Avro -->
<dependency>
  <groupId>org.apache.flink</groupId>
  <artifactId>flink-avro-confluent-registry</artifactId>
  <version>1.14.0</version>
</dependency>
<!-- Debezium Json -->
<dependency>
  <groupId>org.apache.flink</groupId>
  <artifactId>flink-json</artifactId>
  <version>1.14.0</version>
</dependency>
```

下表列出了 Flink 中的数据类型与 JSON 中的数据类型的映射关系：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1ymukce9m6e8.png)

###  9.4. <a name='DebeziumFormat-1'></a>如何使用 Debezium Format

Debezium 为变更日志提供了统一的格式，这是一个 JSON 格式的从 MySQL product 表捕获的更新操作的简单示例：

MySQL 产品表有4列（id、name、description、weight）

```json
{
  "before": {
    "id": 111,
    "name": "scooter",
    "description": "Big 2-wheel scooter",
    "weight": 5.18
  },
  "after": {
    "id": 111,
    "name": "scooter",
    "description": "Big 2-wheel scooter",
    "weight": 5.15
  },
  "source": {...},
  "op": "u",
  "ts_ms": 1589362330904,
  "transaction": null
}
```

假设此消息已同步到 Kafka 主题 products_binlog，则可以使用以下 DDL 来使用此主题并解析更改事件。

```sql
CREATE TABLE topic_products (
  -- schema 与 MySQL 的 products 表完全相同
  id BIGINT,
  name STRING,
  description STRING,
  weight DECIMAL(10, 2)
) WITH (
 'connector' = 'kafka',
 'topic' = 'products_binlog',
 'properties.bootstrap.servers' = 'localhost:9092',
 'properties.group.id' = 'testGroup',
  -- 使用 'debezium-json' format 来解析 Debezium 的 JSON 消息
  -- 如果 Debezium 用 Avro 编码消息，请使用 'debezium-avro-confluent'
 'format' = 'debezium-json'  -- 如果 Debezium 用 Avro 编码消息，请使用 'debezium-avro-confluent'
)
```

```sql
-- MySQL "products" 的实时物化视图
-- 计算相同产品的最新平均重量
SELECT name, AVG(weight) 
FROM topic_products 
GROUP BY name;

-- 将 MySQL "products" 表的所有数据和增量更改同步到
-- Elasticsearch "products" 索引，供将来查找
INSERT INTO elasticsearch_products
SELECT * FROM topic_products;
```

###  9.5. <a name='schema'></a>消息体中包含 schema 信息

可能会开启 Kafka 的配置 'value.converter.schemas.enable'，用来在消息体中包含 schema 信息。

为了解析这一类信息，你需要在上述 DDL WITH 子句中添加选项 'debezium-json.schema-include' = 'true'（默认为 false）。

通常情况下，建议不要包含 schema 的描述，因为这样会使消息变得非常冗长，并降低解析性能。

然后，Debezium JSON 消息可能如下所示:

```json
{
  "schema": {...},
  "payload": {
    "before": {
      "id": 111,
      "name": "scooter",
      "description": "Big 2-wheel scooter",
      "weight": 5.18
    },
    "after": {
      "id": 111,
      "name": "scooter",
      "description": "Big 2-wheel scooter",
      "weight": 5.15
    },
    "source": {...},
    "op": "u",
    "ts_ms": 1589362330904,
    "transaction": null
  }
}
```

|参数|数据类型|描述|举例|
|---|---|---|---|
|debezium-json.schema-include|Boolean|设置 Debezium Kafka Connect 时，用户可以启用 Kafka 配置 'value.converter.schemas.enable' 以在消息中包含 schema。此选项表明 Debezium JSON 消息是否包含 schema。|'true'（默认为 false）|

###  9.6. <a name='METADATA'></a>可用 METADATA 元数据

|参数|数据类型|描述|举例|
|---|---|---|---|
|ingestion-timestamp|TIMESTAMP_LTZ(3) NULL|连接器处理事件的时间戳。对应于Debezium记录中的ts_ms字段。| origin_ts TIMESTAMP(3) |
|source.timestamp|TIMESTAMP_LTZ(3) NULL|源系统创建事件的时间戳。对应于Debezium记录中的source.ts_ms字段。|event_time TIMESTAMP(3)|
|source.database|STRING NULL|原始数据库。如果可用，则对应于Debezium记录中的source.db字段。|origin_database|
|source.schema|STRING NULL|原始数据库架构。对应于Debezium记录中的source.schema字段（如果可用）。|origin_schema|
|source.table| STRING NULL|原始数据库表。对应于Debezium记录中的source.table或source.collection字段（如果可用）。|origin_table|
|source.properties|MAP<STRING, STRING> NULL|各种源属性的映射。对应于Debezium记录中的源字段。|origin_properties|

```sql
CREATE TABLE KafkaTable (
  origin_ts TIMESTAMP(3) METADATA FROM 'value.ingestion-timestamp' VIRTUAL,
  event_time TIMESTAMP(3) METADATA FROM 'value.source.timestamp' VIRTUAL,
  origin_database STRING METADATA FROM 'value.source.database' VIRTUAL,
  origin_schema STRING METADATA FROM 'value.source.schema' VIRTUAL,
  origin_table STRING METADATA FROM 'value.source.table' VIRTUAL,
  origin_properties MAP<STRING, STRING> METADATA FROM 'value.source.properties' VIRTUAL,
  user_id BIGINT,
  item_id BIGINT,
  behavior STRING
) WITH (
  'connector' = 'kafka',
  'topic' = 'user_behavior',
  'properties.bootstrap.servers' = 'localhost:9092',
  'properties.group.id' = 'testGroup',
  'scan.startup.mode' = 'earliest-offset',
  'value.format' = 'debezium-json'
);
```
