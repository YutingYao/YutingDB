<!-- vscode-markdown-toc -->
* 1. [启动 flink SQL 客户端](#flinkSQL)
	* 1.1. [GROUP BY](#GROUPBY)
	* 1.2. [USE 语句](#USE)
	* 1.3. [初始化会话使用SQL文件](#SQL)
	* 1.4. [文件系统 SQL 连接器](#SQL-1)
		* 1.4.1. [分区文件](#)
		* 1.4.2. [分区提交策略 & 触发器](#-1)
	* 1.5. [执行SQL文件](#SQL-1)
	* 1.6. [OVER聚合](#OVER)
	* 1.7. [执行一组SQL语句](#SQL-1)
* 2. [Flink 内置函数的完整列表](#Flink)
	* 2.1. [sql 函数](#sql)
		* 2.1.1. [比较函数](#-1)
		* 2.1.2. [逻辑函数](#-1)
		* 2.1.3. [算术函数](#-1)
		* 2.1.4. [字符串函数](#-1)
		* 2.1.5. [时间函数](#-1)
		* 2.1.6. [条件函数](#-1)
		* 2.1.7. [类型转换函数](#-1)
		* 2.1.8. [集合函数](#-1)
		* 2.1.9. [集合函数](#-1)
		* 2.1.10. [值构建函数](#-1)
		* 2.1.11. [值获取函数](#-1)
		* 2.1.12. [分组函数](#-1)
		* 2.1.13. [哈希函数](#-1)
		* 2.1.14. [聚合函数](#-1)
		* 2.1.15. [ROW_NUMBER()](#ROW_NUMBER)
		* 2.1.16. [时间间隔单位和时间点单位标识符](#-1)
	* 2.2. [举个栗子--时间函数](#--)
		* 2.2.1. [Date and Time](#DateandTime)
		* 2.2.2. [时间戳](#-1)
		* 2.2.3. [间隔年到月](#-1)
		* 2.2.4. [隔 DAY TO SECOND](#DAYTOSECOND)
* 3. [source 表 - 使用 CREATE TABLE 语句](#source-CREATETABLE)
	* 3.1. [举个栗子](#-1)
	* 3.2. [CREATE 语句](#CREATE)
		* 3.2.1. [UNION and UNION ALL](#UNIONandUNIONALL)
		* 3.2.2. [Table API 和 SQL 程序的结构](#TableAPISQL)
		* 3.2.3. [SQL hints](#SQLhints)
		* 3.2.4. [模式映射](#-1)
* 4. [LIKE 子句](#LIKE)
* 5. [连续查询](#-1)
* 6. [Sink 表 - 使用 INSERT INTO 语句](#Sink-INSERTINTO)
* 7. [scala & SQL CLI](#scalaSQLCLI)
	* 7.1. [示例-OVERWRITE-PARTITION](#-OVERWRITE-PARTITION)
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
* 10. [动态表 & 连续查询(Continuous Query)](#ContinuousQuery)
* 11. [调整 Flink Table 和 SQL API 程序的配置项](#FlinkTableSQLAPI)
* 12. [数据类型](#-1)
* 13. [executeSql 和 sqlQuery](#executeSqlsqlQuery)
	* 13.1. [时间属性介绍](#-1)
* 14. [MATCH_RECOGNIZE 模式检测](#MATCH_RECOGNIZE)
	* 14.1. [安装指南](#-1)
	* 14.2. [SQL 语义](#SQL-1)
	* 14.3. [示例](#-1)
* 15. [CDC](#CDC)
	* 15.1. [mongoDB](#mongoDB)
	* 15.2. [Oracle](#Oracle)
	* 15.3. [Postgres 数据库作为 Catalog](#PostgresCatalog)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->
##  1. <a name='flinkSQL'></a>启动 flink SQL 客户端

[配置参数设置](https://nightlies.apache.org/flink/flink-docs-release-1.14/zh/docs/dev/table/config/#%E6%89%A7%E8%A1%8C%E9%85%8D%E7%BD%AE)

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

按 Q 键退出结果视图。

如果你想提前结束这个查询，那么可以直接使用 CTRL-C 按键，这个会停掉作业同时停止屏幕上的打印。

```sql
SELECT name, COUNT(*) AS cnt 
FROM (VALUES ('Bob'), ('Alice'), ('Greg'), ('Bob')) AS NameTable(name) 
GROUP BY name;
```

CLI 为维护和可视化结果提供三种模式

```sql
SET 'sql-client.execution.result-mode' = 'table';

+ Bob, 1
+ Alice, 1
+ Greg, 1
- Bob, 1
+ Bob, 2

SET 'sql-client.execution.result-mode' = 'changelog';

Bob, 2
Alice, 1
Greg, 1

SET 'sql-client.execution.result-mode' = 'tableau';

+-----+----------------------+----------------------+
| +/- |                 name |                  cnt |
+-----+----------------------+----------------------+
|   + |                  Bob |                    1 |
|   + |                Alice |                    1 |
|   + |                 Greg |                    1 |
|   - |                  Bob |                    1 |
|   + |                  Bob |                    2 |
+-----+----------------------+----------------------+
Received a total of 5 rows
```

###  1.1. <a name='GROUPBY'></a>GROUP BY

Apache Flink支持用于聚合数据的标准GROUP BY子句。

```sql
SELECT COUNT(*)
FROM Orders
GROUP BY order_id
```

分组集允许比标准GROUP by所描述的更复杂的分组操作。行按每个指定的分组集分别分组，并为每个组计算聚合，就像简单的group by子句一样。

```sql
SELECT supplier_id, rating, COUNT(*) AS total
FROM (VALUES
    ('supplier1', 'product1', 4),
    ('supplier1', 'product2', 3),
    ('supplier2', 'product3', 3),
    ('supplier2', 'product4', 4))
AS Products(supplier_id, product_id, rating)
GROUP BY GROUPING SETS ((supplier_id, rating), (supplier_id), ())
```

Results:

```s
+-------------+--------+-------+
| supplier_id | rating | total |
+-------------+--------+-------+
|   supplier1 |      4 |     1 |
|   supplier1 | (NULL) |     2 |
|      (NULL) | (NULL) |     4 |
|   supplier1 |      3 |     1 |
|   supplier2 |      3 |     1 |
|   supplier2 | (NULL) |     2 |
|   supplier2 |      4 |     1 |
+-------------+--------+-------+
```

ROLLUP

```sql
GROUP BY ROLLUP (supplier_id, rating)
```

CUBE

```sql
GROUP BY CUBE (supplier_id, rating, product_id)
```

HAVING

```sql
SELECT SUM(amount)
FROM Orders
GROUP BY users
HAVING SUM(amount) > 50
```

###  1.2. <a name='USE'></a>USE 语句

USE 语句用来设置当前的 catalog 或者 database。

```scala
val env = StreamExecutionEnvironment.getExecutionEnvironment()
val tEnv = StreamTableEnvironment.create(env)

// create a catalog
tEnv.executeSql("CREATE CATALOG cat1 WITH (...)")
tEnv.executeSql("SHOW CATALOGS").print()
// +-----------------+
// |    catalog name |
// +-----------------+
// | default_catalog |
// | cat1            |
// +-----------------+

// change default catalog
tEnv.executeSql("USE CATALOG cat1")

tEnv.executeSql("SHOW DATABASES").print()
// databases are empty
// +---------------+
// | database name |
// +---------------+
// +---------------+

// create a database
tEnv.executeSql("CREATE DATABASE db1 WITH (...)")
tEnv.executeSql("SHOW DATABASES").print()
// +---------------+
// | database name |
// +---------------+
// |        db1    |
// +---------------+

// change default database
tEnv.executeSql("USE db1")

// change module resolution order and enabled status
tEnv.executeSql("USE MODULES hive")
tEnv.executeSql("SHOW FULL MODULES").print()
// +-------------+-------+
// | module name |  used |
// +-------------+-------+
// |        hive |  true |
// |        core | false |
// +-------------+-------+
```

```sql
Flink SQL> CREATE CATALOG cat1 WITH (...);
返回：[INFO] Catalog has been created.

Flink SQL> SHOW CATALOGS;
返回：default_catalog
返回：cat1

Flink SQL> USE CATALOG cat1;

Flink SQL> SHOW DATABASES;

Flink SQL> CREATE DATABASE db1 WITH (...);
返回：[INFO] Database has been created.

Flink SQL> SHOW DATABASES;
返回：db1

Flink SQL> USE db1;

Flink SQL> USE MODULES hive;
返回：[INFO] Use modules succeeded!
返回：Flink SQL> SHOW FULL MODULES;
返回：+-------------+-------+
返回：| module name |  used |
返回：+-------------+-------+
返回：|        hive |  true |
返回：|        core | false |
返回：+-------------+-------+
返回：2 rows in set
```

###  1.3. <a name='SQL'></a>初始化会话使用SQL文件

下面给出了这样一个文件的示例。

```sql
-- 定义可用的目录
-- Define available catalogs

CREATE CATALOG MyCatalog
  WITH (
    'type' = 'hive'
  );

USE CATALOG MyCatalog;

-- 定义可用的数据库
-- Define available database

CREATE DATABASE MyDatabase;

USE MyDatabase;

-- 定义表
-- Define TABLE

CREATE TABLE MyTable(
  MyField1 INT,
  MyField2 STRING
) WITH (
  'connector' = 'filesystem',
  'path' = '/path/to/something',
  'format' = 'csv'
);

-- 定义视图
-- Define VIEW

CREATE VIEW MyCustomView AS SELECT MyField2 FROM MyTable;

-- 在这里定义用户定义的函数。
-- Define user-defined functions here.

CREATE FUNCTION foo.bar.AggregateUDF AS myUDF;

-- 更改表程序基本执行行为的属性。
-- Properties that change the fundamental execution behavior of a table program.

SET 'execution.runtime-mode' = 'streaming'; 
-- 执行模式为'batch'或'streaming'
-- execution mode either 'batch' or 'streaming'
SET 'sql-client.execution.result-mode' = 'table'; 
-- 可用的值:'table'， 'changelog'和'tableau'
-- available values: 'table', 'changelog' and 'tableau'
SET 'sql-client.execution.max-table-result.rows' = '10000'; 
-- 可选:最大维护行数
-- optional: maximum number of maintained rows
SET 'parallelism.default' = '1'; 
-- 可选:Flink的并行度(默认为1)
-- optional: Flink's parallelism (1 by default)
SET 'pipeline.auto-watermark-interval' = '200'; 
--可选:周期水印的间隔
--optional: interval for periodic watermarks
SET 'pipeline.max-parallelism' = '10'; 
-- 可选:Flink的最大并行度
-- optional: Flink's maximum parallelism
SET 'table.exec.state.ttl' = '1000'; 
-- 可选:表程序空闲状态时间
-- optional: table program's idle state time
SET 'restart-strategy' = 'fixed-delay';

-- 用于调整和调优表程序的配置选项。
-- Configuration options for adjusting and tuning table programs.

SET 'table.optimizer.join-reorder-enabled' = 'true';
SET 'table.exec.spill-compression.enabled' = 'true';
SET 'table.exec.spill-compression.block-size' = '128kb';
```

###  1.4. <a name='SQL-1'></a>文件系统 SQL 连接器

```sql
CREATE TABLE MyUserTable (
  column_name1 INT,
  column_name2 STRING,
  ...
  part_name1 INT,
  part_name2 STRING
) PARTITIONED BY (part_name1, part_name2) WITH (
  'connector' = 'filesystem',           
  -- 必选: 指定连接器类型
  'path' = 'file:///path/to/whatever',  
  -- 必选: 指向目录的路径
  'format' = '...',                     
  -- 必选: 文件系统连接器需要指定格式，请查阅表格式 部分以获取更多细节
  'partition.default-name' = '...',     
  -- 可选: 动态分区模式下分区字段值是 null 或空字符串时，默认的分区名。
  'sink.shuffle-by-partition.enable' = '...',  
  -- 可选: 该选项开启了在 sink 阶段通过动态分区字段来 shuffle 数据，
  -- 该功能可以大大减少文件系统 sink 的文件数，
  -- 但可能会导致数据倾斜，默认值是 false.
  ...
)
```



####  1.4.1. <a name=''></a>分区文件

Flink 的文件系统连接器在对分区的支持上，使用了标准的 hive 格式。 不过，它不需要预先注册分区，而是基于目录结构自动做了分区发现。比如，以下目录结构的表， 会被自动推导为包含 datetime 和 hour 分区的分区表。

Parquet: Apache Parquet. 与 Hive 兼容.

按行编码的格式支持 csv 和 json。 

按块编码的格式支持 parquet, orc 和 avro。

```sql
path
└── datetime=2019-08-25
    └── hour=11
        ├── part-0.parquet
        ├── part-1.parquet
    └── hour=12
        ├── part-0.parquet
└── datetime=2019-08-26
    └── hour=6
        ├── part-0.parquet
```

####  1.4.2. <a name='-1'></a>分区提交策略 & 触发器

|Key|举例|默认|类型|说明|
|---|---|---|---|---|
|sink.partition-commit.trigger|partition-time|process-time|String|分区提交触发器类型。 'process-time': 基于机器时间，既不需要分区时间提取器也不需要水印生成器，一旦 ”当前系统时间“ 超过了 “分区创建系统时间” 和 'sink.partition-commit.delay' 之和，就提交分区； 'partition-time': 基于从分区字段提取的时间，需要水印生成器，一旦 “水印” 超过了 ”从分区字段提取的时间“ 和 'sink.partition-commit.delay' 之和，就提交分区.|
|sink.partition-commit.watermark-time-zone|'Asia/Shanghai'|UTC|String|解析 LONG 类型的水印到 TIMESTAMP 类型时所采用的时区，解析得到的水印的 TIMESTAMP 会被用来跟分区时间进行比较以判断分区是否该被提交。 该参数只有在参数 `sink.partition-commit.trigger` 被设置为 'partition-time' 时才生效。 如果该参数设置的不正确，比如在 TIMESTAMP_LTZ 列上定义了 source rowtime, 但没有设置该参数，则用户可能在若干个小时后才看到分区的提交。 该参数的默认值是 'UTC', 代表水印是定义在 TIMESTAMP 列上或没有定义水印。 如果水印定义在 TIMESTAMP_LTZ 列上，则水印的时区是会话的时区。 该参数的可选值要么是完整的时区名比如 'America/Los_Angeles'，要么是自定义的时区 id 比如 'GMT-08:00'.|
|sink.partition-commit.delay|1 h|0 s|Duration|该延迟时间之前分区不会被提交。如果是按天的分区，应配置为 '1 d', 如果是按小时的分区，应配置为 '1 h'.|
|sink.partition-commit.policy.kind|'success-file'|(none)|String|说分区提交策略用来通知下游应用系统某个分区已经写完毕可以被读取了。 metastore: 向 metastore 中增加分区，只有 hive 支持 metastore 策略，文件系统通过目录结构管理分区； success-file: 向目录下增加 '_success' 文件； custom: 使用指定的类来创建提交策略； 支持同时指定多个提交策略，如：'metastore,success-file'.|
|partition.time-extractor.timestamp-pattern|'$dt $hour:00:00'|(none)|String|'default' 时间提取器允许用户从分区字段中提取合法的时间戳模式。默认支持从第一个字段按 'yyyy-mm-dd hh:mm:ss' 时间戳模式提取。 如果需要从一个分区字段比如 ‘dt’ 提取时间戳，可以配置为: '$dt'; 如果需要从多个分区字段，比如 'year', 'month', 'day' 和 'hour'提取时间戳，可以配置为：'$year-$month-$day $hour:00:00'; 如果需要从两字分区字段，比如 'dt' 和 'hour' 提取时间戳，可以配置为：'$dt $hour:00:00'.|

###  1.5. <a name='SQL-1'></a>执行SQL文件

下面是这样一个文件的示例。

```sql
CREATE TEMPORARY TABLE users (
  user_id BIGINT,
  user_name STRING,
  user_level STRING,
  region STRING,
  PRIMARY KEY (user_id) NOT ENFORCED
) WITH (
  'connector' = 'upsert-kafka',
  'topic' = 'users',
  'properties.bootstrap.servers' = '...',
  'key.format' = 'csv',
  'value.format' = 'avro'
);

-- 设置同步模式
-- set sync mode
SET 'table.dml-sync' = 'true';

-- 设置作业名称
-- set the job name
SET 'pipeline.name' = 'SqlJob';

-- 设置作业提交的队列
-- set the queue that the job submit to
SET 'yarn.application.queue' = 'root';

-- 设置作业并行性
-- set the job parallism
SET 'parallism.default' = '100';

-- 从特定保存点路径恢复
-- restore from the specific savepoint path
SET 'execution.savepoint.path' = '/tmp/flink-savepoints/savepoint-cca7bc-bb1e257f0dab';

INSERT INTO pageviews_enriched
SELECT *
FROM pageviews AS p
LEFT JOIN users FOR SYSTEM_TIME AS OF p.proctime AS u
ON p.user_id = u.user_id;
```

处理时间属性可以在 schema 定义的时候用 .proctime 后缀来定义。时间属性一定不能定义在一个已有字段上，所以它只能定义在 schema 定义的最后。

```scala
val stream: DataStream[(String, String)] = ...

// 声明一个额外的字段作为时间属性字段
val table = tEnv.fromDataStream(stream, $"UserActionTimestamp", $"user_name", $"data", $"user_action_time".proctime)

val windowedTable = table.window(Tumble over 10.minutes on $"user_action_time" as "userActionWindow")
```




###  1.6. <a name='OVER'></a>OVER聚合

OVER聚合计算一段有序行范围内的每个输入行的聚合值。与GROUP BY聚合相比，OVER聚合不会将每个组的结果行数减少到单个行。相反，OVER聚合为每个输入行生成聚合值。

```sql
SELECT order_id, order_time, amount,
  SUM(amount) OVER (
    PARTITION BY product
    ORDER BY order_time
    RANGE BETWEEN INTERVAL '1' HOUR PRECEDING AND CURRENT ROW
  ) AS one_hour_prod_amount_sum
FROM Orders
```

可以在SELECT子句中定义多个窗口集合。但是，对于流式查询，由于当前的限制，所有聚合的OVER-windows必须相同

WINDOW子句可用于在SELECT子句之外定义OVER WINDOW。它可以使查询更具可读性，还允许我们对多个聚合重用窗口定义。

```sql
SELECT order_id, order_time, amount,
  SUM(amount) OVER w AS sum_amount,
  AVG(amount) OVER w AS avg_amount
FROM Orders
WINDOW w AS (
  PARTITION BY product
  ORDER BY order_time
  RANGE BETWEEN INTERVAL '1' HOUR PRECEDING AND CURRENT ROW)
```



###  1.7. <a name='SQL-1'></a>执行一组SQL语句

```sql
CREATE TABLE pageviews (
  user_id BIGINT,
  page_id BIGINT,
  viewtime TIMESTAMP,
  proctime AS PROCTIME()
) WITH (
  'connector' = 'kafka',
  'topic' = 'pageviews',
  'properties.bootstrap.servers' = '...',
  'format' = 'avro'
);

CREATE TABLE pageview (
  page_id BIGINT,
  cnt BIGINT
) WITH (
  'connector' = 'jdbc',
  'url' = 'jdbc:mysql://localhost:3306/mydatabase',
  'table-name' = 'pageview'
);

CREATE TABLE uniqueview (
  page_id BIGINT,
  cnt BIGINT
) WITH (
  'connector' = 'jdbc',
  'url' = 'jdbc:mysql://localhost:3306/mydatabase',
  'table-name' = 'uniqueview'
);

BEGIN STATEMENT SET;

INSERT INTO pageviews
SELECT page_id, count(1)
FROM pageviews
GROUP BY page_id;

INSERT INTO uniqueview
SELECT page_id, count(distinct user_id)
FROM pageviews
GROUP BY page_id;

END;
```

> 同步/异步执行DML语句

默认情况下，SQL客户端异步执行DML语句。

SQL客户端将DML语句的作业提交到Flink集群，而不是等待作业完成

```sql
INSERT INTO MyTableSink SELECT * FROM MyTableSource;
```

返回

```s
[INFO] Table update statement has been successfully submitted to the cluster:
Cluster ID: StandaloneClusterId
Job ID: 6f922fe5cba87406ff23ae4a7bb79044
```

```sql
SET 'table.dml-sync' = 'true';
INSERT INTO MyTableSink SELECT * FROM MyTableSource;
```

返回

```s
[INFO] Submitting SQL update statement to the cluster...
[INFO] Execute statement in sync mode. Please wait for the execution finish...
[INFO] Complete execution of the SQL update statement.
```

> 从保存点启动SQL作业

```sql
SET 'execution.savepoint.path' = '/tmp/flink-savepoints/savepoint-cca7bc-bb1e257f0dab'
```

返回

```sql
[INFO] Session property has been set.

-- 将从指定的保存点路径恢复下列所有DML语句
Flink SQL> INSERT INTO ...
```

> 定义自定义作业名称

```sql
SET 'pipeline.name' = 'kafka-to-hive';
```

返回

```sql
[INFO] Session property has been set.

-- 下面的所有DML语句都将使用指定的作业名称。
Flink SQL> INSERT INTO ...
```

##  2. <a name='Flink'></a>Flink 内置函数的完整列表

[Flink 内置函数的完整列表](https://nightlies.apache.org/flink/flink-docs-release-1.14/zh/docs/dev/table/functions/systemfunctions/)

这些函数为用户在开发 SQL 查询时提供了一个功能强大的工具箱。

###  2.1. <a name='sql'></a>sql 函数

####  2.1.1. <a name='-1'></a>比较函数

```sql
value1 = value2
value1 <> value2
value1 > value2
value1 >= value2
value1 < value2
value1 <= value2

value IS NULL
value IS NOT NULL

value1 IS DISTINCT FROM value2
value1 IS NOT DISTINCT FROM value2
value1 BETWEEN [ ASYMMETRIC | SYMMETRIC ] value2 AND value3
value1 NOT BETWEEN [ ASYMMETRIC | SYMMETRIC ] value2 AND value3
string1 LIKE string2 [ ESCAPE char ]
string1 NOT LIKE string2 [ ESCAPE char ]
string1 SIMILAR TO string2 [ ESCAPE char ]
string1 NOT SIMILAR TO string2 [ ESCAPE char ]
value1 IN (value2 [, value3]* )
value1 NOT IN (value2 [, value3]* )
EXISTS (sub-query)
value IN (sub-query)
value NOT IN (sub-query)
```

```sql
SELECT user, amount
FROM Orders
WHERE product IN (
    SELECT product FROM NewProducts
)
```

```sql
SELECT user, amount
FROM Orders
WHERE product EXISTS (
    SELECT product FROM NewProducts
)
```

####  2.1.2. <a name='-1'></a>逻辑函数

```sql
boolean1 OR boolean2
boolean1 AND boolean2
NOT boolean
boolean IS FALSE
boolean IS NOT FALSE
boolean IS TRUE
boolean IS NOT TRUE
boolean IS UNKNOWN
boolean IS NOT UNKNOWN
```

####  2.1.3. <a name='-1'></a>算术函数

```sql
+ numeric
- numeric
numeric1 + numeric2
numeric1 - numeric2
numeric1 * numberic2
numeric1 / numeric2
numeric1 % numeric2
POWER(numeric1, numeric2)
ABS(numeric)
SQRT(numeric)
LN(numeric)
LOG10(numeric)
LOG2(numeric)
LOG(numeric2) LOG(numeric1, numeric2)
EXP(numeric)
CEIL(numeric) CEILING(numeric)
FLOOR(numeric)
SIN(numeric)
SINH(numeric)
COS(numeric)
TAN(numeric)
TANH(numeric)
COT(numeric)
ASIN(numeric)
ACOS(numeric)
ATAN(numeric)
ATAN2(numeric1, numeric2)
COSH(numeric)
-- 弧度 numeric 的度数表示
DEGREES(numeric)
-- 度数 numeric 的弧度表示
RADIANS(numeric)
SIGN(numeric)
ROUND(numeric, INT)
-- 无比接近 pi 的值。
PI()
-- 无比接近 e 的值。
E()
-- [0.0, 1.0) 范围内的伪随机双精度值。
RAND()
RAND(INT)
--  [0.0, INT) 范围内的伪随机双精度值。
RAND_INTEGER(INT)
RAND_INTEGER(INT1, INT2)
-- 根据 RFC 4122 类型 4（伪随机生成）UUID，
-- 返回 UUID（通用唯一标识符）字符串。 
-- 例如“3d3c68f7-f608-473f-b60c-b0c44ad4cc4e”，
-- UUID 是使用加密强的伪随机数生成器生成的。
UUID()
-- 二进制格式返回 INTEGER 的字符串表示形式。
-- 如果 INTEGER 为 NULL，则返回 NULL。 
-- 例如 
-- 4.bin() 返回“100”，
-- 12.bin() 返回“1100”。
BIN(INT)
-- 以十六进制格式返回整数 numeric 值或 STRING 的字符串表示形式。
-- 如果参数为 NULL，则返回 NULL。 
-- 例如
-- 数字 20 返回“14”，
-- 数字 100 返回“64”，
-- 字符串“hello,world” 返回“68656C6C6F2C776F726C64”。
HEX(numeric) HEX(string)
-- 返回截取 integer2 位小数的数字
TRUNCATE(numeric1, integer2)
```

####  2.1.4. <a name='-1'></a>字符串函数

```sql
-- STRING1 和 STRING2 的连接
string1 || string2
-- 字符串中的字符数
CHAR_LENGTH(string) 
CHARACTER_LENGTH(string)
-- 以大写形式返回字符串。
UPPER(string)
-- 以小写形式返回字符串
LOWER(string)
-- 返回 STRING2 中第一次出现 STRING1 的位置（从 1 开始）；
-- 如果在 STRING2 中找不到 STRING1 返回 0
POSITION(string1 IN string2)
-- 从 STRING1 中删除以字符串 STRING2 的字符串的结果。
-- 开头LEADING/结尾TRAILING/开头且结尾BOTH
-- 默认情况下，两边的空格都会被删除。
TRIM([ BOTH | LEADING | TRAILING ] string1 FROM string2)
-- 从 STRING 中删除左边空格的字符串
LTRIM(string)
-- 从 STRING 中删除右边空格的字符串
RTRIM(string)
-- INT 个 string 连接的字符串
REPEAT(string, int)
-- STRING1 所有与正则表达式 STRING2 匹配的子字符串被 STRING3 替换后的字符串
-- 例如 'foobar'.regexpReplace('oo|ar', '') 返回 "fb"。
REGEXP_REPLACE(string1, string2, string3)
-- 该字符串
-- 从位置 INT1 用 STRING2 替换 STRING1 的 INT2（默认为 STRING2 的长度）字符。 
-- 例如 
-- 'xxxxxtest'.overlay('xxxx', 6) 返回 "xxxxxxxxx"； 
-- 'xxxxxtest'.overlay('xxxx', 6, 2) 返回 "xxxxxxxxxst"。
OVERLAY(string1 PLACING string2 FROM integer1 [ FOR integer2 ])
-- STRING 从位置 INT1 开始，长度为 INT2（默认到结尾）的子字符串。
SUBSTRING(string FROM integer1 [ FOR integer2 ])
-- 它用 STRING1 中的 STRING3（非重叠）替换所有出现的 STRING2。 
-- 例如 
-- 'hello world'.replace('world', 'flink') 返回 'hello flink'； 
-- 'ababab'.replace('abab', 'z') 返回 'zab'。
REPLACE(string1, string2, string3)
-- 将字符串 STRING1 按照 STRING2 正则表达式的规则拆分，
-- 返回指定 INTEGER1 处位置的字符串。
-- 正则表达式匹配组索引从 1 开始， 0 表示匹配整个正则表达式。
-- 此外，正则表达式匹配组索引不应超过定义的组数。 
-- 例如 REGEXP_EXTRACT('foothebar', 'foo(.*?)(bar)', 2) 返回 "bar"。
REGEXP_EXTRACT(string1, string2[, integer])
-- 新形式的 STRING，
-- 其中每个单词的第一个字符转换为大写，
-- 其余字符转换为小写。
-- 这里的单词表示字母数字的字符序列。
INITCAP(string)
-- 连接 string1，string2， … 的字符串。
-- 如果有任一参数为 NULL，则返回 NULL。 
-- 例如 CONCAT('AA', 'BB', 'CC') 返回 "AABBCC"。
CONCAT(string1, string2, ...)
-- 将 STRING2， STRING3， … 与分隔符 STRING1 连接起来的字符串。
-- 在要连接的字符串之间添加分隔符。 
-- 如果 STRING1 为 NULL，则返回 NULL。
-- 与 concat() 相比，concat_ws() 会自动跳过 NULL 参数。 
-- 例如 concat_ws('~', 'AA', Null(STRING), 'BB', '', 'CC') 
-- 返回 "AA~BB~~CC".
CONCAT_WS(string1, string2, string3, ...)
-- 从 string1 靠左填充 string2 到 INT 长度的新字符串。
-- 如果 string1 的长度小于 INT 值，则返回 string1 缩短为整数字符。
-- 例如 
-- LPAD('hi', 4, '??') 返回 "??hi"；
-- LPAD('hi', 1, '??') 返回 `“h”。
LPAD(string1, integer, string2)
-- 从 string1 靠右边填充 string2 到 INT 长度的新字符串。
-- 如果 string1 的长度小于 INT 值，则返回 string1 缩短为长度为 INT 的新字符串。
-- 例如 
-- RPAD('hi', 4, '??') 返回 "hi??", 
-- RPAD('hi', 1, '??') 返回 "h"。
RPAD(string1, integer, string2)
-- 字符串 string1 的 base64 解码的结果；
-- 如果字符串为 NULL，则返回 NULL。 
-- 例如 FROM_BASE64('aGVsbG8gd29ybGQ=') 
-- 返回 "hello world"。
FROM_BASE64(string)
-- 字符串 string 的 base64 编码的结果；
-- 如果字符串为 NULL，则返回 NULL。 
-- 例如 TO_BASE64('hello world') 返回 "aGVsbG8gd29ybGQ="。
TO_BASE64(string)
-- 字符串 string 第一个字符的数值。
-- 如果字符串为 NULL 则返回 NULL。
-- 例如 
-- ascii('abc') 返回 97，
-- ascii(CAST(NULL AS VARCHAR)) 返回 NULL。
ASCII(string)
-- 二进制等于 integer 的 ASCII 字符。
-- 如果整数 integer 大于 255，我们先将得到整数对 255 取模数， 并返回模数的 CHR。
-- 如果整数为 NULL，则返回 NULL。
-- 例如 
-- chr(97) 返回 a，
-- chr(353) 返回 a， 
-- ascii(CAST(NULL AS VARCHAR)) 返回 NULL
CHR(integer)
-- 使用提供的字符集（‘US-ASCII’，‘ISO-8859-1’，‘UTF-8’，‘UTF-16BE’，‘UTF-16LE’，‘UTF-16’）解码。 
-- 如果任一参数为空，则结果也将为空。
DECODE(binary, string)
-- 使用提供的字符集（‘US-ASCII’，‘ISO-8859-1’，‘UTF-8’，‘UTF-16BE’，‘UTF-16LE’，‘UTF-16’）编码。 
-- 如果任一参数为空，则结果也将为空。
ENCODE(string1, string2)
-- string2 在 string1 中第一次出现的位置。
-- 如果有任一参数为 NULL，则返回 NULL。
INSTR(string1, string2)
-- 字符串中最左边的长度为 integer 值的字符串。
-- 如果 integer 为负，则返回 EMPTY 字符串。
-- 如果有任一参数 为 NULL 则返回 NULL。
LEFT(string, integer)
-- 字符串中最右边的长度为 integer 值的字符串。
-- 如果 integer 为负，则返回 EMPTY 字符串。
-- 如果有任一参数 为 NULL 则返回 NULL
RIGHT(string, integer)
-- string2 中 string1 在位置 integer 之后第一次出现的位置。
-- 未找到返回 0。
-- 如果有任一参数为 NULL 则返回 NULL。
LOCATE(string1, string2[, integer])
-- 从 URL 返回指定的部分。
-- string2 的有效值包括“HOST”，“PATH”，“QUERY”，“REF”，“PROTOCOL”，“AUTHORITY”，“FILE”和“USERINFO”。 
-- 如果有任一参数为 NULL，则返回 NULL。
-- 例如 
-- parse_url(' http://facebook.com/path1/p.php?k1=v1&k2=v2#Ref1', 'HOST') 
-- 返回 'facebook.com'。 
-- 还可以通过提供关键词 string3 作为第三个参数来提取 QUERY 中特定键的值。
-- 例如 
-- parse_url('http://facebook.com/path1/p.php?k1=v1&k2=v2#Ref1', 'QUERY', 'k1') 
-- 返回 'v1'。
PARSE_URL(string1, string2[, string3])
-- 如果 string1 的任何（可能为空）子字符串
-- 与 Java 正则表达式 string2 匹配，则返回 TRUE，否则返回 FALSE。 
-- 如果有任一参数为 NULL，则返回 NULL。
REGEXP(string1, string2)
-- 反转的字符串。
-- 如果字符串为 NULL，则返回 NULL。
REVERSE(string)
-- 通过分隔符 string2 拆分 string1，
-- 返回拆分字符串的第 integer（从零开始）个字符串。
-- 如果整数为负，则返回 NULL。 
-- 如果有任一参数为 NULL，则返回 NULL。
SPLIT_INDEX(string1, string2, integer1)
-- 使用分隔符将 string1 拆分为键值对后返回一个 map。
-- string2 是 pair 分隔符，默认为 ‘,'。
-- string3 是键值分隔符，默认为 ‘='。
STR_TO_MAP(string1[, string2, string3]])
-- 字符串的子字符串，
-- 从位置 integer1 开始，长度为 integer2（默认到末尾）
SUBSTR(string[, integer1[, integer2]])
```



####  2.1.6. <a name='-1'></a>条件函数

```sql
-- 当第一个时间值包含在 (valueX_1, valueX_2, …) 中时，返回 resultX。
-- 当没有值匹配时，如果提供则返回 result_z， 
-- 否则返回 NULL。
CASE value WHEN value1_1 [, value1_2]* 
THEN RESULT1 
(WHEN value2_1 [, value2_2 ]* THEN result_2)* 
(ELSE result_z) 
END
-- 满足第一个条件 X 时返回 resultX。
-- 当不满足任何条件时，如果提供则返回 result_z，
-- 否则返回 NULL。
CASE WHEN condition1 
THEN result1 
(WHEN condition2 THEN result2)* 
(ELSE result_z) 
END
-- 如果 value1 等于 value2 返回 NULL；否则返回 value1。
-- 例如 
-- NULLIF(5, 5) 返回 NULL；
-- NULLIF(5, 0) 返回 5
NULLIF(value1, value2)
-- 从 value1, value2, … 返回第一个不为 NULL 的值。
-- 例如 COALESCE(3, 5, 3) 返回 3。
COALESCE(value1, value2 [, value3]*)
-- 如果满足条件，则返回 true_value，否则返回 false_value。
-- 例如 IF(5 > 3, 5, 3) 返回 5。
IF(condition, true_value, false_value)
-- 如果输入为 NULL，则返回 null_replacement；否则返回输入。
IFNULL(input, null_replacement)
-- 如果字符串中的所有字符都是字母
IS_ALPHA(string)
-- 如果 string 可以解析为有效数字
IS_DECIMAL(string)
-- 如果字符串中的所有字符都是数字，
-- 则返回 true，否则返回 false。
IS_DIGIT(string)
-- 所有输入参数的最大值，
-- 如果输入参数中包含 NULL，则返回 NULL。
GREATEST(value1[, value2]*)
-- 所有输入参数的最小值，
-- 如果输入参数中包含 NULL，则返回 NULL。
LEAST(value1[, value2]*)
```

####  2.1.7. <a name='-1'></a>类型转换函数

```sql
-- 被强制转换为类型 type 的新值。
-- 例如 CAST('42' AS INT) 返回 42； 
-- CAST(NULL AS VARCHAR) 返回 VARCHAR 类型的 NULL。
CAST(value AS type)

-- 输入表达式的数据类型的字符串表示形式。
-- 默认情况下返回的字符串是一个摘要字符串
TYPEOF(input) 
-- 如果 force_serializable 设置为 TRUE，
-- 则字符串表示可以保留在目录中的完整数据类型。
TYPEOF(input, force_serializable)
```

####  2.1.8. <a name='-1'></a>集合函数

```sql
-- 数组中元素的数量。
CARDINALITY(array)
-- 数组中 INT 位置的元素。索引从 1 开始。
array '[' INT ']'
-- 数组的唯一元素（其基数应为 1）；如果数组为空，则返回 NULL。如果数组有多个元素，则抛出异常。
ELEMENT(array)
-- map 中的 entries 数量。
CARDINALITY(map)
-- map 中指定 key 对应的值。
map ‘[’ value ‘]’
```

####  2.1.9. <a name='-1'></a>集合函数

```sql
-- 确定一个JSON字符串是否满足给定的路径搜索条件。
// TRUE
SELECT JSON_EXISTS('{"a": true}', '$.a');
// FALSE
SELECT JSON_EXISTS('{"a": true}', '$.b');
// TRUE
SELECT JSON_EXISTS('{"a": [{ "b": 1 }]}',
  '$.a[0].b');

// TRUE
SELECT JSON_EXISTS('{"a": true}',
  'strict $.b' TRUE ON ERROR);
// FALSE
SELECT JSON_EXISTS('{"a": true}',
  'strict $.b' FALSE ON ERROR);

-- 从JSON字符串中提取标量。
// STRING: "true"
JSON_VALUE('{"a": true}', '$.a')

// BOOLEAN: true
JSON_VALUE('{"a": true}', '$.a' RETURNING BOOLEAN)

// STRING: "false"
JSON_VALUE('{"a": true}', 'lax $.b'
    DEFAULT FALSE ON EMPTY)

// STRING: "false"
JSON_VALUE('{"a": true}', 'strict $.b'
    DEFAULT FALSE ON ERROR)
```

####  2.1.10. <a name='-1'></a>值构建函数

```sql
ARRAY ‘[’ value1 [, value2 ]* ‘]’
MAP ‘[’ value1, value2 [, value3, value4 ]* ‘]’
```

####  2.1.11. <a name='-1'></a>值获取函数

```sql
-- 按名称从 Flink 复合类型（例如，Tuple，POJO）返回字段的值。
tableName.compositeType.field
-- 返回 Flink 复合类型（例如，Tuple，POJO）的平面表示，
-- 将其每个直接子类型转换为单独的字段。
-- 在大多数情况下，平面表示 的字段与原始字段的命名类似，
-- 但使用 $ 分隔符（例如 mypojo$mytuple$f0）
tableName.compositeType.*
```

####  2.1.12. <a name='-1'></a>分组函数

```sql
GROUP_ID()

GROUPING(expression1 [, expression2]* ) 
GROUPING_ID(expression1 [, expression2]* )
```

####  2.1.13. <a name='-1'></a>哈希函数

```sql
MD5(string)
SHA1(string)
SHA224(string)
SHA256(string)
SHA384(string)
SHA512(string)
SHA2(string, hashLength)
```

####  2.1.14. <a name='-1'></a>聚合函数

聚合函数将所有的行作为输入，并返回单个聚合值作为结果。

默认情况下或使用关键字 ALL

使用 DISTINCT 则对所有值去重后计算。

```sql
COUNT([ ALL ] expression | DISTINCT expression1 [, expression2]*)
COUNT(*) | COUNT(1)
AVG([ ALL | DISTINCT ] expression)
SUM([ ALL | DISTINCT ] expression)
MAX([ ALL | DISTINCT ] expression)
MIN([ ALL | DISTINCT ] expression )

-- 所有输入行中表达式的总体标准偏差。
STDDEV_POP([ ALL | DISTINCT ] expression)

-- 所有输入行中表达式的样本标准偏差。
STDDEV_SAMP([ ALL | DISTINCT ] expression)

-- 所有输入行中表达式的总体方差（总体标准差的平方）
VAR_POP([ ALL | DISTINCT ] expression)

-- 所有输入行中表达式的样本方差（样本标准差的平方）
VAR_SAMP([ ALL | DISTINCT ] expression)

-- 跨所有输入行的多组表达式。
COLLECT([ ALL | DISTINCT ] expression)

-- VAR_SAMP() 的同义方法。
VARIANCE([ ALL | DISTINCT ] expression)

-- 值在一组值中的排名。结果是 1 加上分区顺序中当前行之前或等于当前行的行数。排名在序列中不一定连续。
RANK()

-- 值在一组值中的排名。结果是一加先前分配的等级值。与函数 rank 不同，dense_rank 不会在排名序列中产生间隙。
DENSE_RANK()

-- ROW_NUMBER 和 RANK 相似。ROW_NUMBER 按 顺序对所有行进行编号（例如 1，2，3，4，5）。RANK 为等值 row 提供相同的序列值（例如 1，2，2，4，5）。
ROW_NUMBER()

-- 窗口中当前行之后第 offset 行处的表达式值。
LEAD(expression [, offset] [, default])

-- 窗口中当前行之前第 offset 行处的表达式值。
LAG(expression [, offset] [, default])

-- 一组有序值中的第一个值
FIRST_VALUE(expression)

-- 一组有序值中的最后一个值
LAST_VALUE(expression)

-- 连接字符串表达式的值并在它们之间放置分隔符值。字符串末尾不添加分隔符时则分隔符的默认值为“,”。
LISTAGG(expression [, separator])
```

####  2.1.15. <a name='ROW_NUMBER'></a>ROW_NUMBER()

Flink使用`ROW_NUMBER()`来删除重复项，就像Top-N查询一样。从理论上讲，重复数据删除是Top-N的一种特殊情况，N为1，并按处理时间或事件时间排序。

```sql
SELECT [column_list]
FROM (
   SELECT [column_list],
     ROW_NUMBER() OVER ([PARTITION BY col1[, col2...]]
       ORDER BY time_attr [asc|desc]) AS rownum
   FROM table_name)
WHERE rownum = 1
```

`ROW_NUMBER()`:为每一行分配一个唯一的、连续的数字，从1开始。

`PARTITION BY col1[， col2…]`:指定分区列，即去重键。

`ORDER BY time_attr [asc|desc]`:排序列，必须是时间属性。目前Flink支持处理时间属性和事件时间属性。按ASC排序意味着保留第一行，按DESC排序意味着保留最后一行。ORDER BY子句导致根据指定的表达式对结果行进行排序。在流模式下运行时，表的主排序顺序必须在时间属性上升序。所有后续订单均可自由选择。但在批处理模式中没有此限制。

```sql
SELECT *
FROM Orders
ORDER BY order_time, order_id
```

`WHERE rownum = 1`: rownum = 1是Flink识别该查询为重复数据删除的必要参数。

以下示例演示如何在流表上指定具有重复数据消除功能的SQL查询。

```sql
CREATE TABLE Orders (
  order_time  STRING,
  user        STRING,
  product     STRING,
  num         BIGINT,
  proctime AS PROCTIME()
) WITH (...);

-- 删除order_id上重复的行并保留第一个出现的行，
-- 因为不能有两个order_id相同的订单。
SELECT order_id, user, product, num
FROM (
  SELECT *,
    ROW_NUMBER() OVER (PARTITION BY order_id ORDER BY proctime ASC) AS row_num
  FROM Orders)
WHERE row_num = 1
```

如上所述，rownum字段将作为唯一键的一个字段写入结果表，这可能会导致大量记录被写入结果表。例如，当排名9的记录(比如product-1001)被更新，并且排名提升到1时，排名1 ~ 9的所有记录都会作为更新消息输出到结果表中。如果结果表接收的数据太多，就会成为SQL作业的瓶颈。

优化方法是在Top-N查询的外层SELECT子句中省略rownum字段。这是合理的，因为前N条记录的数量通常不是很大，因此消费者可以自己快速地对记录进行排序。在上面的示例中，如果没有rownum字段，只需要将更改的记录(product-1001)发送到下游，这可以减少结果表的大量IO。

以下示例显示了如何以这种方式优化上述Top-N示例：

```sql
CREATE TABLE ShopSales (
  product_id   STRING,
  category     STRING,
  product_name STRING,
  sales        BIGINT
) WITH (...);

-- omit row_num field from the output
SELECT product_id, category, product_name, sales
FROM (
  SELECT *,
    ROW_NUMBER() OVER (PARTITION BY category ORDER BY sales DESC) AS row_num
  FROM ShopSales)
WHERE row_num <= 5
```

####  2.1.16. <a name='-1'></a>时间间隔单位和时间点单位标识符

下表列出了`时间间隔单位`和`时间点单位`标识符。

对于 Table API，请使用 _ 代替空格（例如 `DAY_TO_HOUR`）。

```sql
MILLENIUM （仅适用SQL）
CENTURY （仅适用SQL）
YEAR
YEAR TO MONTH
QUARTER
MONTH
WEEK
DAY
DAY TO HOUR
DAY TO MINUTE
DAY TO SECOND
HOUR
HOUR TO MINUTE
HOUR TO SECOND
MINUTE
MINUTE TO SECOND
SECOND
DOY （仅适用SQL）
DOW （仅适用SQL）
MILLISECOND
MICROSECOND
SQL_TSI_YEAR （仅适用SQL）
SQL_TSI_QUARTER （仅适用SQL）
SQL_TSI_MONTH （仅适用SQL）
SQL_TSI_WEEK （仅适用SQL）
SQL_TSI_DAY （仅适用SQL）
SQL_TSI_HOUR （仅适用SQL）
SQL_TSI_MINUTE （仅适用SQL）
SQL_TSI_SECOND （仅适用SQL）
```

###  2.2. <a name='--'></a>举个栗子--时间函数

####  2.2.1. <a name='DateandTime'></a>Date and Time

Date的数据类型，由`年-月-日`组成，取值范围为

0000-01-01 ~ 9999 -12-31。

与 SQL 标准相比，范围从 0000 年开始。

--------------------------------------------------

不带时区的Time的数据类型，包括小时:分钟:秒[.分数]，

精度可达纳秒，取值范围为:

00:00.000000000到23:59:59.999999999。

####  2.2.2. <a name='-1'></a>时间戳

不带时区的时间戳的数据类型

* 年-月-日-小时：分钟：秒[.小数]

* year-month-day hour:minute:second[.fractional]

精度高达纳秒

数值范围为：

* 0000-01-01 00:00:00.000000000到9999-12-31 23:59:59.99999999。

与SQL标准相比，不支持闰秒(23:59:60和23:59:61)，语义更接近java.time.LocalDateTime。

####  2.2.3. <a name='-1'></a>间隔年到月

一组年-月间隔类型的数据类型。

该类型必须参数化为以下解析之一:

* 间隔年,

* 年到月的间隔，

* 或几个月的间隔。

年-月的间隔为：

* +years-months

取值范围为：

* -9999-11 ~ +9999-11。

对于所有类型的解析，值表示都是相同的。例如，50个月的间隔总是用年到月的间隔格式(默认的年精度)表示:

* +04-02

```sql
INTERVAL YEAR
INTERVAL YEAR(p)
INTERVAL YEAR(p) TO MONTH
INTERVAL MONTH

-- 其中p是年的位数（年精度）。
-- p的值必须介于1和4之间（包括1和4）。
-- 如果未指定年份精度，则p等于2
```



####  2.2.4. <a name='DAYTOSECOND'></a>隔 DAY TO SECOND

一组日时间间隔类型的数据类型。

该类型必须参数化为以下分辨率之一，精度高达纳秒：

* 每隔几天，

* 几天到几小时的间隔，

* 几天到几分钟的间隔，

* 天到秒的间隔，

* 每隔几个小时，

* 小时到分钟的间隔，

* 小时到秒的间隔，

* 每隔几分钟，

* 分到秒的间隔，

* 或秒的间隔。

日时间间隔由

* +天 小时：月：秒

* +days hours:months:seconds.fractional 

组成。

分数值范围为:

* -9999999 23:59:59.99999999到+9999999 23:59:59.99999999。
  
对于所有类型的分辨率，值表示都是相同的。例如，70秒的间隔始终以天到秒的间隔格式表示（具有默认精度）：
  
* +00 00:01:10.000000。

```sql
INTERVAL DAY
INTERVAL DAY(p1)
INTERVAL DAY(p1) TO HOUR
INTERVAL DAY(p1) TO MINUTE
INTERVAL DAY(p1) TO SECOND(p2)
INTERVAL HOUR
INTERVAL HOUR TO MINUTE
INTERVAL HOUR TO SECOND(p2)
INTERVAL MINUTE
INTERVAL MINUTE TO SECOND(p2)
INTERVAL SECOND
INTERVAL SECOND(p2)

-- 其中,
-- p1是天数（日精度），
-- p2是分数秒（分数精度）的位数。
-- p1的值必须介于1和6之间（包括1和6）。
-- p2的值必须介于0和9之间（包括0和9）。
-- 如果未指定p1，则默认情况下它等于2。
-- 如果未指定p2，则默认情况下等于6。
```





##  3. <a name='source-CREATETABLE'></a>source 表 - 使用 CREATE TABLE 语句

###  3.1. <a name='-1'></a>举个栗子

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

* 根据指定的`表名`创建一个`表`，如果同名表已经在 catalog 中存在了，则无法注册。

  * CREATE TABLE

* 根据给定的表属性创建`数据库`。若数据库中已存在同名表会抛出异常。

  * CREATE DATABASE

* 根据给定的 query 语句创建一个`视图`。若数据库中已经存在同名视图会抛出异常.

  * CREATE VIEW

* 创建一个有 catalog 和数据库命名空间的 catalog function ，需要指定一个 identifier ，可指定 language tag。 若 catalog 中，已经有同名的函数注册了，则无法注册。

  * CREATE FUNCTION

------------------------------------------

可以使用 TableEnvironment 中的 executeSql() 方法执行 CREATE 语句。 若 CREATE 操作执行成功，executeSql() 方法返回 ‘OK’，否则会抛出异常。

Table 总是与特定的 TableEnvironment 绑定。 不能在同一条查询中使用不同 TableEnvironment 中的表，例如，对它们进行 join 或 union 操作。 TableEnvironment 可以通过静态方法 TableEnvironment.create() 创建。

```scala
import org.apache.flink.table.api.{EnvironmentSettings, TableEnvironment}

val settings = EnvironmentSettings
    .newInstance()
    .inStreamingMode()
    //.inBatchMode()
    .build()

val tEnv = TableEnvironment.create(settings)
```

或者，用户可以从现有的 StreamExecutionEnvironment 创建一个 StreamTableEnvironment 与 DataStream API 互操作。

```scala
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

val env = StreamExecutionEnvironment.getExecutionEnvironment
val tEnv = StreamTableEnvironment.create(env)
```

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

####  3.2.1. <a name='UNIONandUNIONALL'></a>UNION and UNION ALL

Flink SQL> create view t1(s) as values ('c'), ('a'), ('b'), ('b'), ('c');

Flink SQL> create view t2(s) as values ('d'), ('e'), ('a'), ('b'), ('b');

Flink SQL> (SELECT s FROM t1) UNION (SELECT s FROM t2);
+---+
|  s|
+---+
|  c|
|  a|
|  b|
|  d|
|  e|
+---+

Flink SQL> (SELECT s FROM t1) UNION ALL (SELECT s FROM t2);
+---+
|  c|
+---+
|  c|
|  a|
|  b|
|  b|
|  c|
|  d|
|  e|
|  a|
|  b|
|  b|
+---+

####  3.2.2. <a name='TableAPISQL'></a>Table API 和 SQL 程序的结构

所有用于批处理和流处理的 Table API 和 SQL 程序都遵循相同的模式。下面的代码示例展示了 Table API 和 SQL 程序的通用结构。

```scala
import org.apache.flink.table.api._
import org.apache.flink.connector.datagen.table.DataGenOptions

// 创建用于批处理或流执行的TableEnvironment.
// Create a TableEnvironment for batch or streaming execution.
// 有关详细信息，请参阅“创建表环境”一节.
// See the "Create a TableEnvironment" section for details.
val tableEnv = TableEnvironment.create(/*…*/)

// 创建源表
// Create a source table
tableEnv.createTemporaryTable("SourceTable", TableDescriptor.forConnector("datagen")
  .schema(Schema.newBuilder()
    .column("f0", DataTypes.STRING())
    .build())
  .option(DataGenOptions.ROWS_PER_SECOND, 100)
  .build())

// 创建接收表(使用SQL DDL)
// Create a sink table (using SQL DDL)
tableEnv.executeSql("CREATE TEMPORARY TABLE SinkTable WITH ('connector' = 'blackhole') LIKE SourceTable");

// 从Table API查询创建Table对象
// Create a Table object from a Table API query
val table1 = tableEnv.from("SourceTable");

// 从SQL查询创建Table对象
// Create a Table object from a SQL query
val table2 = tableEnv.sqlQuery("SELECT * FROM SourceTable");

// 产生一个Table API结果Table到一个TableSink，对于SQL结果也是一样
// Emit a Table API result Table to a TableSink, same for SQL result
val tableResult = table1.executeInsert("SinkTable");
```

Table API 提供了一种机制来解释计算 Table 的逻辑和优化查询计划。

这是通过 Table.explain() 方法或者 StatementSet.explain() 方法来完成的。

- Table.explain() 返回一个 Table 的计划。

- StatementSet.explain() 返回多 sink 计划的结果。

以下代码展示了一个示例以及对给定 Table 使用 Table.explain() 方法的相应输出：

```scala
val env = StreamExecutionEnvironment.getExecutionEnvironment
val tEnv = StreamTableEnvironment.create(env)

val table1 = env.fromElements((1, "hello")).toTable(tEnv, $"count", $"word")
val table2 = env.fromElements((1, "hello")).toTable(tEnv, $"count", $"word")
val table = table1
  .where($"word".like("F%"))
  .unionAll(table2)

  // UNION和UNION都返回在任一表中找到的行。
  // UNION只接受不同的行，
  // UNION ALL不会从结果行中删除重复项。

println(table.explain())
```

以下代码展示了一个示例以及使用 StatementSet.explain() 的多 sink 计划的相应输出：

```scala
val settings = EnvironmentSettings.inStreamingMode()
val tEnv = TableEnvironment.create(settings)

val schema = Schema.newBuilder()
    .column("count", DataTypes.INT())
    .column("word", DataTypes.STRING())
    .build()

tEnv.createTemporaryTable("MySource1", TableDescriptor.forConnector("filesystem")
    .schema(schema)
    .option("path", "/source/path1")
    .format("csv")
    .build())
tEnv.createTemporaryTable("MySource2", TableDescriptor.forConnector("filesystem")
    .schema(schema)
    .option("path", "/source/path2")
    .format("csv")
    .build())
tEnv.createTemporaryTable("MySink1", TableDescriptor.forConnector("filesystem")
    .schema(schema)
    .option("path", "/sink/path1")
    .format("csv")
    .build())
tEnv.createTemporaryTable("MySink2", TableDescriptor.forConnector("filesystem")
    .schema(schema)
    .option("path", "/sink/path2")
    .format("csv")
    .build())

val stmtSet = tEnv.createStatementSet()

val table1 = tEnv.from("MySource1").where($"word".like("F%"))
stmtSet.addInsert("MySink1", table1)

val table2 = table1.unionAll(tEnv.from("MySource2"))
stmtSet.addInsert("MySink2", table2)

val explanation = stmtSet.explain()
println(explanation)
```



####  3.2.3. <a name='SQLhints'></a>SQL hints 

我们使用 Oracle 风格的 SQL hints 语法：

```s
/*+ OPTIONS(key=val [, key=val]*) */
```

示例 

```sql
CREATE TABLE kafka_table1 (id BIGINT, name STRING, age INT) WITH (...);
CREATE TABLE kafka_table2 (id BIGINT, name STRING, age INT) WITH (...);

-- 覆盖查询语句中源表的选项
select id, name from kafka_table1 /*+ OPTIONS('scan.startup.mode'='earliest-offset') */;

-- 覆盖 join 中源表的选项
select * from
    kafka_table1 /*+ OPTIONS('scan.startup.mode'='earliest-offset') */ t1
    join
    kafka_table2 /*+ OPTIONS('scan.startup.mode'='earliest-offset') */ t2
    on t1.id = t2.id;

-- 覆盖插入语句中结果表的选项
insert into kafka_table1 /*+ OPTIONS('sink.partitioner'='round-robin') */ select * from kafka_table2;

```

####  3.2.4. <a name='-1'></a>模式映射

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



##  4. <a name='LIKE'></a>LIKE 子句

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

##  5. <a name='-1'></a>连续查询

例如，假设你需要从传入的数据流中**计算每个部门的员工人数**。查询需要维护每个部门最新的计算总数，以便在处理新行时及时输出结果。

```sql
SELECT 
   部门_id,
   COUNT(*) as 员工人数 
FROM 员工信息 
GROUP BY 部门_id;
```

##  6. <a name='Sink-INSERTINTO'></a>Sink 表 - 使用 INSERT INTO 语句

```sql
INSERT INTO 部门_人数
SELECT 
   部门_id,
   COUNT(*) as 员工人数 
FROM 员工信息;
```

##  7. <a name='scalaSQLCLI'></a>scala & SQL CLI

```scala
val tEnv = TableEnvironment.create(...)

// 注册一个 "Orders" 源表，和 "RubberOrders" 结果表
tEnv.executeSql("CREATE TABLE Orders (`user` BIGINT, product STRING, amount INT) WITH (...)")
tEnv.executeSql("CREATE TABLE RubberOrders(product STRING, amount INT) WITH (...)")

// 运行一个 INSERT 语句，将源表的数据输出到结果表中
val tableResult1 = tEnv.executeSql(
  "INSERT INTO RubberOrders SELECT product, amount FROM Orders WHERE product LIKE '%Rubber%'")
// 通过 TableResult 来获取作业状态
println(tableResult1.getJobClient().get().getJobStatus())

//----------------------------------------------------------------------------
// 注册一个 "GlassOrders" 结果表用于运行多 INSERT 语句
tEnv.executeSql("CREATE TABLE GlassOrders(product VARCHAR, amount INT) WITH (...)");

// 运行多个 INSERT 语句，将原表数据输出到多个结果表中
val stmtSet = tEnv.createStatementSet()
// `addInsertSql` 方法每次只接收单条 INSERT 语句
stmtSet.addInsertSql(
  "INSERT INTO RubberOrders SELECT product, amount FROM Orders WHERE product LIKE '%Rubber%'")
stmtSet.addInsertSql(
  "INSERT INTO GlassOrders SELECT product, amount FROM Orders WHERE product LIKE '%Glass%'")
// 执行刚刚添加的所有 INSERT 语句
val tableResult2 = stmtSet.execute()
// 通过 TableResult 来获取作业状态
println(tableResult1.getJobClient().get().getJobStatus())
```

```sql
CREATE TABLE Orders (`user` BIGINT, product STRING, amount INT) WITH (...);
CREATE TABLE RubberOrders(product STRING, amount INT) WITH (...);
SHOW TABLES
INSERT INTO RubberOrders SELECT product, amount FROM Orders WHERE product LIKE '%Rubber%';
```

###  7.1. <a name='-OVERWRITE-PARTITION'></a>示例-OVERWRITE-PARTITION

通过 INSERT 语句，可以将查询的结果插入到表中，

INSERT OVERWRITE 将会覆盖表中或分区中的任何已存在的数据。否则，新数据会追加到表中或分区中。

PARTITION 语句应该包含需要插入的静态分区列与值。

```sql
-- 创建一个分区表
CREATE TABLE country_page_view (user STRING, cnt INT, date STRING, country STRING)
PARTITIONED BY (date, country)
WITH (...)

-- 追加行到该静态分区中 (date='2019-8-30', country='China')
INSERT INTO country_page_view 
PARTITION (date='2019-8-30', country='China')
  SELECT user, cnt FROM page_view_source;

-- 追加行到分区 (date, country) 中，其中 date 是静态分区 '2019-8-30'；country 是动态分区，其值由每一行动态决定
INSERT INTO country_page_view 
PARTITION (date='2019-8-30')
  SELECT user, cnt, country FROM page_view_source;

-- 覆盖行到静态分区 (date='2019-8-30', country='China')
INSERT OVERWRITE country_page_view 
PARTITION (date='2019-8-30', country='China')
  SELECT user, cnt FROM page_view_source;

-- 覆盖行到分区 (date, country) 中，其中 date 是静态分区 '2019-8-30'；country 是动态分区，其值由每一行动态决定
INSERT OVERWRITE country_page_view 
PARTITION (date='2019-8-30')
  SELECT user, cnt, country FROM page_view_source;
```

通过 INSERT 语句，也可以直接将值插入到表中，

```sql
CREATE TABLE students (name STRING, age INT, gpa DECIMAL(3, 2)) WITH (...);

INSERT INTO students
  VALUES ('fred flintstone', 35, 1.28), ('barney rubble', 32, 2.32);
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

* `group-offsets`：从 Zookeeper/Kafka 中某个指定的消费组已提交的偏移量开始。
* `earliest-offset`：从可能的最早偏移量开始。
* `latest-offset`：从最末尾偏移量开始。
* `timestamp`：从用户为每个 partition 指定的时间戳开始。
* `specific-offsets`：从用户为每个 partition 指定的偏移量开始。

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

* 使用原始的 UTF-8 字符串作为 Kafka 的 key：

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

* Kafka 的 key 和 value 在 Schema Registry 中都注册为 Avro 记录的表的示例：

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

##  10. <a name='ContinuousQuery'></a>动态表 & 连续查询(Continuous Query)

使用具有以下模式的单击事件流:

```json
[
  user:  VARCHAR,   // 用户名
  cTime: TIMESTAMP, // 访问 URL 的时间
  url:   VARCHAR    // 用户访问的 URL
]
```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5ki47h3tduw0.png)

不适用于需要计算更新的场景：

* 下面的查询就是一个例子，它根据最后一次单击的时间为每个用户计算一个 RANK。
* 一旦 click 表接收到一个新行，用户的 lastAction 就会更新，
* 并必须计算一个新的排名。
* 然而，由于两行不能具有相同的排名，所以所有较低排名的行也需要更新。

```sql
SELECT user, RANK() OVER (ORDER BY lastAction)
FROM (
  SELECT user, MAX(cTime) AS lastAction FROM clicks GROUP BY user
);
```

Flink的 Table API 和 SQL 支持三种方式来编码一个动态表的变化:

1. Append-only 流

2. Retract 流

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3yl53i0wxui0.png)

3. Upsert 流：与 retract 流的主要区别在于 UPDATE 操作是用单个 message 编码的，因此效率更高。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3vuvhdbgu2q0.png)

##  11. <a name='FlinkTableSQLAPI'></a>调整 Flink Table 和 SQL API 程序的配置项

```sql
SET 'table.exec.mini-batch.enabled' = 'true'
SET 'table.exec.mini-batch.allow-latency' = '5s';
SET 'table.exec.mini-batch.size' = '5000';
```

|Key|默认|举例|类型|说明|
|---|---|---|---|---|
|table.exec.mini-batch.allow-latency|0 ms|'5s'|Duration|MiniBatch可以使用最大延迟来缓冲输入记录。MiniBatch是一种缓冲输入记录以减少状态访问的优化。MiniBatch在允许的延迟时间间隔内以及达到最大缓冲记录数时触发。注意：如果table.exec.mini-batch.enabled设置为true，则其值必须大于零。|
|table.exec.mini-batch.enabled|false|'true'|Boolean|指定是否启用MiniBatch优化。MiniBatch是缓冲输入记录以减少状态访问的优化。这在默认情况下是禁用的。要启用此功能，用户应该将此配置设置为true。注意:如果启用了mini-batch， 'table.exec.mini-batch. 'allow-latency’和‘table.exec.mini-batch。尺寸'必须设置。|
|table.exec.mini-batch.size|-1|'5000'|Long|MiniBatch可以缓冲的最大输入记录数。MiniBatch是一种缓冲输入记录以减少状态访问的优化。MiniBatch在允许的延迟时间间隔内以及达到最大缓冲记录数时触发。注意：MiniBatch目前仅适用于非窗口聚合。如果table.exec.mini-batch.enabled设置为true，则其值必须为正值。|

##  12. <a name='-1'></a>数据类型

下表列出了无需进一步信息即可隐式映射到数据类型的类。

|Class|Data Type|
|---|---|
|java.lang.String|STRING|
|java.lang.Boolean|BOOLEAN|
|boolean|BOOLEAN NOT NULL|
|java.lang.Byte|TINYINT|
|byte|TINYINT NOT NULL|
|java.lang.Short|SMALLINT|
|short|SMALLINT NOT NULL|
|java.lang.Integer|INT|
|int|INT NOT NULL|
|java.lang.Long|BIGINT|
|long|BIGINT NOT NULL|
|java.lang.Float|FLOAT|
|float|FLOAT NOT NULL|
|java.lang.Double|DOUBLE|
|double|DOUBLE NOT NULL|
|java.sql.Date|DATE|
|java.time.LocalDate|DATE|
|java.sql.Time|TIME(0)|
|java.time.LocalTime|TIME(9)|
|java.sql.Timestamp|TIMESTAMP(9)|
|java.time.LocalDateTime|TIMESTAMP(9)|
|java.time.OffsetDateTime|TIMESTAMP(9) WITH TIME ZONE|
|java.time.Instant|TIMESTAMP_LTZ(9)|
|java.time.Duration|INVERVAL SECOND(9)|
|java.time.Period|INTERVAL YEAR(4) TO MONTH|
|byte[]|BYTES|
|T[]|ARRAY<T>|
|java.util.Map<K, V>|MAP<K, V>|
|structured type T|anonymous structured type T|

##  13. <a name='executeSqlsqlQuery'></a>executeSql 和 sqlQuery 

###  13.1. <a name='-1'></a>时间属性介绍

Table API 程序需要在 streaming environment 中指定时间属性：

* ProcessingTime
* IngestionTime
* EventTime

```scala
val env = StreamExecutionEnvironment.getExecutionEnvironment

env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime) // default

// 或者:
// env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime)
// env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
```

在 SQL 的术语中，Table API 的对象对应于视图（虚拟表）。它封装了一个逻辑查询计划。它可以通过以下方法在 catalog 中创建：

```scala
// 得到一个TableEnvironment
// get a TableEnvironment
val tableEnv = ... 
// see "Create a TableEnvironment" section

// 表是一个简单的投影查询的结果
// table is the result of a simple projection query 
val projTable: Table = tableEnv.from("X").select(...)

// 将表projTable注册为表projectedTable
// register the Table projTable as table "projectedTable"
tableEnv.createTemporaryView("projectedTable", projTable)
```

以下示例演示如何在已注册表和内联表上指定SQL查询。

```scala
val env = StreamExecutionEnvironment.getExecutionEnvironment
val tableEnv = StreamTableEnvironment.create(env)

// read a DataStream from an external source
// 从外部源读取数据流
val ds: DataStream[(Long, String, Integer)] = env.addSource(...)

// SQL query with an inlined (unregistered) table
// 带有内联(未注册)表的SQL查询
val table = ds.toTable(tableEnv, $"user", $"product", $"amount")
val result = tableEnv.sqlQuery(
  s"SELECT SUM(amount) FROM $table WHERE product LIKE '%Rubber%'")

// SQL query with a registered table
// 带有注册表的SQL查询
// register the DataStream under the name "Orders"
// 以“Orders”的名称注册DataStream
tableEnv.createTemporaryView("Orders", ds, $"user", $"product", $"amount")
// run a SQL query on the Table and retrieve the result as a new Table
// 在表上运行SQL查询，并以新表的形式检索结果
val result2 = tableEnv.sqlQuery(
  "SELECT product, amount FROM Orders WHERE product LIKE '%Rubber%'")

// create and register a TableSink
// 创建并注册 a TableSink
val schema = Schema.newBuilder()
  .column("product", DataTypes.STRING())
  .column("amount", DataTypes.INT())
  .build()

val sinkDescriptor = TableDescriptor.forConnector("filesystem")
  .schema(schema)
  .format(FormatDescriptor.forFormat("csv")
    .option("field-delimiter", ",")
    .build())
  .build()

tableEnv.createTemporaryTable("RubberOrders", sinkDescriptor)

// run an INSERT SQL on the Table and emit the result to the TableSink
// 在表上运行INSERT SQL，并将结果发送到TableSink
tableEnv.executeSql(
  "INSERT INTO RubberOrders SELECT product, amount FROM Orders WHERE product LIKE '%Rubber%'")
```

执行一个查询:

```scala
val env = StreamExecutionEnvironment.getExecutionEnvironment()
val tableEnv = StreamTableEnvironment.create(env, settings)
// enable checkpointing
// enable 检查点
tableEnv.getConfig.getConfiguration.set(
  ExecutionCheckpointingOptions.CHECKPOINTING_MODE, CheckpointingMode.EXACTLY_ONCE)
tableEnv.getConfig.getConfiguration.set(
  ExecutionCheckpointingOptions.CHECKPOINTING_INTERVAL, Duration.ofSeconds(10))

tableEnv.executeSql("CREATE TABLE Orders (`user` BIGINT, product STRING, amount INT) WITH (...)")

// execute SELECT statement
// 执行SELECT语句
val tableResult1 = tableEnv.executeSql("SELECT * FROM Orders")
val it = tableResult1.collect()
try while (it.hasNext) {
  val row = it.next
  // handle row
  // 处理行
}
finally it.close() 
// close the iterator to avoid resource leak
// 关闭迭代器以避免资源泄漏

// execute Table
// 执行表
val tableResult2 = tableEnv.sqlQuery("SELECT * FROM Orders").execute()
tableResult2.print()

```

##  14. <a name='MATCH_RECOGNIZE'></a>MATCH_RECOGNIZE 模式检测

[完整学习链接](https://nightlies.apache.org/flink/flink-docs-release-1.14/zh/docs/dev/table/sql/queries/match_recognize/)

###  14.1. <a name='-1'></a>安装指南

```xml
<dependency>
  <groupId>org.apache.flink</groupId>
  <artifactId>flink-cep_2.11</artifactId>
  <version>1.14.0</version>
</dependency>
```

###  14.2. <a name='SQL-1'></a>SQL 语义

每个 MATCH_RECOGNIZE 查询都包含以下子句：

* PARTITION BY - 定义表的逻辑分区；类似于 GROUP BY 操作。
* ORDER BY - 指定传入行的排序方式；这是必须的，因为模式依赖于顺序。
* MEASURES - 定义子句的输出；类似于 SELECT 子句。
* ONE ROW PER MATCH - 输出方式，定义每个匹配项应产生多少行。
* AFTER MATCH SKIP - 指定下一个匹配的开始位置；这也是控制单个事件可以属于多少个不同匹配项的方法。
* PATTERN - 允许使用类似于 正则表达式 的语法构造搜索的模式。
* DEFINE - 本部分定义了模式变量必须满足的条件。

它允许 Flink 使用 MATCH_RECOGNIZE 子句融合 CEP 和 SQL API

下面的示例演示了基本模式识别的语法：

```sql
SELECT T.aid, T.bid, T.cid
FROM MyTable
    MATCH_RECOGNIZE (
      PARTITION BY userid
      ORDER BY proctime
      MEASURES
        A.id AS aid,
        B.id AS bid,
        C.id AS cid
      PATTERN (A B C)
      DEFINE
        A AS name = 'a',
        B AS name = 'b',
        C AS name = 'c'
    ) AS T
```

###  14.3. <a name='-1'></a>示例 

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5lfartqtrx80.png)

现在的任务是找出一个单一股票价格不断下降的时期。为此，可以编写如下查询：

```sql
SELECT *
FROM Ticker
    MATCH_RECOGNIZE (
        PARTITION BY symbol
        ORDER BY rowtime
        MEASURES
            START_ROW.rowtime AS start_tstamp,
            LAST(PRICE_DOWN.rowtime) AS bottom_tstamp,
            LAST(PRICE_UP.rowtime) AS end_tstamp
        ONE ROW PER MATCH
        AFTER MATCH SKIP TO LAST PRICE_UP
        PATTERN (START_ROW PRICE_DOWN+ PRICE_UP)
        DEFINE
            PRICE_DOWN AS
                (LAST(PRICE_DOWN.price, 1) IS NULL AND PRICE_DOWN.price < START_ROW.price) OR
                    PRICE_DOWN.price < LAST(PRICE_DOWN.price, 1),
            PRICE_UP AS
                PRICE_UP.price > LAST(PRICE_DOWN.price, 1)
    ) MR;
```

此查询将 Ticker 表按照 symbol 列进行分区并按照 rowtime 属性进行排序。

PATTERN 子句指定我们对以下模式感兴趣：该模式具有开始事件 START_ROW，然后是一个或多个 PRICE_DOWN 事件，并以 PRICE_UP 事件结束。如果可以找到这样的模式，如 AFTER MATCH SKIP TO LAST 子句所示，则从最后一个 PRICE_UP 事件开始寻找下一个模式匹配。

DEFINE 子句指定 PRICE_DOWN 和 PRICE_UP 事件需要满足的条件。尽管不存在 START_ROW 模式变量，但它具有一个始终被评估为 TRUE 隐式条件。

模式变量 PRICE_DOWN 定义为价格小于满足 PRICE_DOWN 条件的最后一行。对于初始情况或没有满足 PRICE_DOWN 条件的最后一行时，该行的价格应小于该模式中前一行（由 START_ROW 引用）的价格。

模式变量 PRICE_UP 定义为价格大于满足 PRICE_DOWN 条件的最后一行。

此查询为股票价格持续下跌的每个期间生成摘要行。

在查询的 MEASURES 子句部分定义确切的输出行信息。输出行数由 ONE ROW PER MATCH 输出方式定义。

```s
 symbol       start_tstamp       bottom_tstamp         end_tstamp
=========  ==================  ==================  ==================
ACME       01-APR-11 10:00:04  01-APR-11 10:00:07  01-APR-11 10:00:08
```

该行结果描述了从 01-APR-11 10:00:04 开始的价格下跌期，在 01-APR-11 10:00:07 达到最低价格，到 01-APR-11 10:00:08 再次上涨。

下面这个示例的任务是找出股票平均价格没有低于某个阈值的最长时间段。它展示了 MATCH_RECOGNIZE 在 aggregation 中的可表达性。可以使用以下查询执行此任务：

```sql
SELECT *
FROM Ticker
    MATCH_RECOGNIZE (
        PARTITION BY symbol
        ORDER BY rowtime
        MEASURES
            FIRST(A.rowtime) AS start_tstamp,
            LAST(A.rowtime) AS end_tstamp,
            AVG(A.price) AS avgPrice
        ONE ROW PER MATCH
        AFTER MATCH SKIP PAST LAST ROW
        PATTERN (A+ B)
        DEFINE
            A AS AVG(A.price) < 15
    ) MR;
```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.69vnz0pg3o80.png)

##  15. <a name='CDC'></a>CDC

###  15.1. <a name='mongoDB'></a>mongoDB

```sql

-- 在 Flink SQL 中声明一张 MongoDB CDC 表：'products'
CREATE TABLE products (
    _id STRING,       -- 文档id，必要字段
    name STRING,
    weight DECIMAL(10,3),
    tags ARRAY<STRING>, -- 数组类型
    price ROW<amount DECIMAL(10,2), currency STRING>, -- 嵌董文档类型
    suppliers ARRAY<ROW<name STRING, address STRING>>,-- 嵌套文档类型
    db_name STRING METADATA FROM 'database_name' VIRTUAL,  -- 获取库名元数据
    collection_name STRING METADATA FROM 'collection_name' VIRTUAL,  -- 获取集合名元数据
    PRIMARY KEY(_id) NOT ENFORCED
) WITH (
    'connector' = 'mongodb-cdc',
    'hosts' = 'localhost:27017,localhost:27018,localhost:27019',
    'username' = 'flinkuser',
    'password' = 'flinkpw',
    'database' = 'inventory',
    'collection' = 'products'
)；

-- 从products集合中读取存量历史数据+增量变更数据

SELECT * FROM products;
```

MongoDB CDC 也支持两种启动模式：

- 默认的initial 模式是先同步表中的存量的数据，然后同步表中的增量数据；
- latest-offset 模式则是从当前时间点开始只同步表中增量数据。

###  15.2. <a name='Oracle'></a>Oracle

```sql
-- 在 Flink SQL 中声明一张 Oracle CDC 表:'products'
CREATE TABLE products (
    id INT NOT NULL,
    name STRING,
    description STRING,
    weight DECIMAL(10, 3),
    db_name STRING METADATA FROM 'database_name' VIRTUAL, -- 获取库名元数据
    schema_name STRING METADATA FROM 'schema_name' VIRTUAL,-- 获取模式名元数据
    table_name STRING METADATA FROM 'table_name' VIRTUAL, --  获取表名元数据
    PRIMARY KEY(id) NOT ENFORCED
 )WITH (
    'connector' = 'oracle-cdc',
    'hostname' = 'localhost',
    'port' = '1521',
    'username' = 'flinkuser',
    'password' = 'flinkpw',
    'database-name' = 'XE',
    'schema-name' = 'inventory',
    'table-name' = 'products'
)；

-- 从products表中读取存量历史数据+增量变更数据
SELECT * FROM products;
```

###  15.3. <a name='PostgresCatalog'></a>Postgres 数据库作为 Catalog

```sql
CREATE CATALOG mypg WITH(
    'type' = 'jdbc',
    'default-database' = '...',
    'username' = '...',
    'password' = '...',
    'base-url' = '...'
);

USE CATALOG mypg;
```

Flink 中的 Postgres 表的完整路径应该是 "<catalog>.<db>.`<schema.table>`"。

如果指定了 schema，请注意需要转义 <schema.table>。

这里提供了一些访问 Postgres 表的例子：

```sql
-- 扫描 'public' schema（即默认 schema）中的 'test_table' 表，schema 名称可以省略
SELECT * FROM mypg.mydb.test_table;
SELECT * FROM mydb.test_table;
SELECT * FROM test_table;

-- 扫描 'custom_schema' schema 中的 'test_table2' 表，
-- 自定义 schema 不能省略，并且必须与表一起转义。
SELECT * FROM mypg.mydb.`custom_schema.test_table2`
SELECT * FROM mydb.`custom_schema.test_table2`;
SELECT * FROM `custom_schema.test_table2`;
```