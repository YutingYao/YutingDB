<!-- vscode-markdown-toc -->
* 1. [Flink 1.9 版本](#Flink1.9)
* 2. [Flink 1.10 版本](#Flink1.10)
* 3. [Flink 1.11 版本](#Flink1.11)
* 4. [Flink 1.12 版本](#Flink1.12)
* 5. [Flink 1.13 版本](#Flink1.13)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->##  1. <a name='Flink1.9'></a>Flink 1.9 版本 

Flink 1.9 推出了预览版的 Hive 集成。

该版本允许用户使用 `SQL DDL` 将 Flink 特有的`元数据`持久化到 `Hive Metastore`、调用 `Hive 中定义的 UDF` 以及读、写 `Hive 中的表`。Flink 只支持写入`未分区的 Hive 表`。

##  2. <a name='Flink1.10'></a>Flink 1.10 版本 

在 Flink 1.10 中，Flink SQL 扩展支持了 `INSERT` `OVERWRITE` 和 `PARTITION` 的语法，允许用户写入 Hive 中的`静态`和`动态分区`。

写入静态分区:

```sql
INSERT { INTO | OVERWRITE } TABLE tablename1 [PARTITION (partcol1=val1, partcol2=val2 …)] select_statement1 FROM from_statement;
```

写入动态分区:

```sql
INSERT { INTO | OVERWRITE } TABLE tablename1 select_statement1 FROM from_statement;
```

对`分区表`的全面支持，使得用户在读取数据时能够受益于`分区剪枝`，减少了需要`扫描的数据量`，从而大幅提升了这些操作的性能。

投影下推：

Flink 采用了`投影下推技术`，通过在`扫描表`时忽略不必要的`域`，最小化 Flink 和 Hive 表之间的`数据传输量`。这一优化在`表的列数较多`时尤为有效。

LIMIT 下推：

对于包含 `LIMIT 语句`的查询，Flink 在所有可能的地方限制返回的`数据条数`，以降低通过网络传输的`数据量`。读取数据时的 `ORC 向量化`：为了提高读取 `ORC 文件`的性能，对于 Hive 2.0.0 及以上版本以及`非复合数据类型`的列，Flink 现在默认使用`原生的 ORC 向量化读取器`。

可插拔模块机制:

Flink 1.10 在 `Flink table 核心`引入了通用的`可插拔模块`机制，目前主要应用于`系统内置函数`。通过模块，用户可以扩展 Flink 的`系统对象`，例如像使用 `Flink 系统函数`一样使用 `Hive 内置函数`。新版本中包含一个预先实现好的 `HiveModule`，能够支持多个 Hive 版本，当然用户也可以选择编写自己的`可插拔模块`。

##  3. <a name='Flink1.11'></a>Flink 1.11 版本 

`FileSystem 连接器`还扩展了 `Table API/SQL` 支持的`用例`和`格式集`，从而实现了直接启用从 `Kafka` 到 `Hive 的 streaming` 数据传输等方案。

1.11.0 在 Hive 生态中重点实现了`实时数仓`方案，改善了`端到端` `流式 ETL` 的用户体验，达到了`批流一体 Hive 数仓`的目标。同时在兼容性、性能、易用性方面也进一步进行了加强。

在`实时数仓`的解决方案中，凭借 Flink 的`流式`处理优势做到`实时读写 Hive`：

- Hive 写入：
  - 完善扩展了 `FileSystem connector` 的`基础能力`和`实现`
  - `Table/SQL 层`的 `sink` 可以支持各种格式（`CSV、Json、Avro、Parquet、ORC`），而且支持 Hive table 的所有格式。

- Partition 支持：
  - 数据导入 Hive 引入 `partition 提交机制`来控制`可见性`，
  - 通过`sink.partition-commit.trigger` 控制 `partition 提交`的时机，
  - 通过 `sink.partition-commit.policy.kind` 选择`提交策略`，
  - 支持 `SUCCESS 文件`和 `metastore 提交`。

- Hive 读取：
  - `实时化`的流式读取 Hive，
  - 通过`监控 partition 生成增量`读取`新 partition`，
  - 或者监控文件夹内`新文件`生成来增量读取`新文件`。
  
- 在 Hive 可用性方面的提升：
  - 通过 `Hive Dialect` 为用户提供`语法兼容`，这样用户无需在 F`link 和 Hive 的 CLI` 之间切换，可以`直接迁移 Hive 脚本`到 Flink 中执行。
  - 提供 Hive 相关依赖的`内置支持`，避免用户自己下载所需的相关依赖。现在只需要`单独下载`一个包，配置 `HADOOP_CLASSPATH` 就可以运行。
  - 在 Hive 性能方面，1.10.0 中已经支持了 `ORC（Hive 2+）的向量化读取`，1.11.0 中我们补全了所有版本的 `Parquet 和 ORC 向量化`支持来提升性能。

##  4. <a name='Flink1.12'></a>Flink 1.12 版本

使用 `Hive 表`进行 `Temporal Table Join`:

- 用户也可以将 `Hive 表`作为`时态表`来使用，
- Flink 既支持自动读取` Hive 表`的`最新分区`作为`时态表`，
- 也支持在`作业执行时`追踪整个 `Hive 表`的`最新版本`作为`时态表`。
- 请参阅文档，了解更多关于如何在 `temporal table join` 中使用 `Hive 表`的示例。

在 FileSystem/Hive connector 的流式写入中支持小文件合并.

##  5. <a name='Flink1.13'></a>Flink 1.13 版本

Hive查询语法兼容性:

- 现在可以使用`Hive SQL语法`编写针对Flink的SQL查询。
- 除了Hive的`DDL方言`，
- Flink现在也接受常用的`Hive DML`和`DQL方言`。
- 要使用`Hive SQL方言`，设置 `table.sql-dialect` 为 `hive`并`加载 HiveModule`。
- `HiveModule 的加载`很重要，因为`Hive的内置函数`需要适当的语法和语义兼容性。 
  
下面的例子说明了这一点:

```sql
CREATE CATALOG myhive WITH ('type' = 'hive'); -- setup HiveCatalog
USE CATALOG myhive;
LOAD MODULE hive; -- setup HiveModule
USE MODULES hive,core;
SET table.sql-dialect = hive; -- enable Hive dialect
SELECT key, value FROM src CLUSTER BY key; -- run some Hive queries
```