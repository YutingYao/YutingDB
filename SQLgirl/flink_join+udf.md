<!-- vscode-markdown-toc -->
* 1. [Regular Joins](#RegularJoins)
	* 1.1. [INNER Equi-JOIN](#INNEREqui-JOIN)
	* 1.2. [OUTER Equi-JOIN](#OUTEREqui-JOIN)
	* 1.3. [Array Expansion](#ArrayExpansion)
* 2. [Interval Joins](#IntervalJoins)
* 3. [FOR SYSTEM_TIME AS OF](#FORSYSTEM_TIMEASOF)
	* 3.1. [Event Time Temporal Join](#EventTimeTemporalJoin)
	* 3.2. [Lookup Join](#LookupJoin)
* 4. [Table Funtion](#TableFuntion)
	* 4.1. [INNER JOIN](#INNERJOIN)
		* 4.1.1. [ Processing Time Temporal Join](#ProcessingTimeTemporalJoin)
		* 4.1.2. [补充：LATERAL TABLE](#LATERALTABLE)
	* 4.2. [LEFT OUTER JOIN](#LEFTOUTERJOIN)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->

前言：由于网上的官方资料还不完善


##  1. <a name='RegularJoins'></a>Regular Joins

###  1.1. <a name='INNEREqui-JOIN'></a>INNER Equi-JOIN

返回受连接条件限制的`简单笛卡尔积`。目前，只支持相等联接，即至少有一个`连接条件`和相等`谓词`的联接。不支持任意`交叉`或`θ连接`。

```sql
SELECT * FROM Orders
INNER JOIN Product
ON Orders.product_id = Product.id
```

###  1.2. <a name='OUTEREqui-JOIN'></a>OUTER Equi-JOIN

```sql
SELECT * FROM Orders
LEFT JOIN Product
ON Orders.product_id = Product.id

SELECT * FROM Orders
RIGHT JOIN Product
ON Orders.product_id = Product.id

SELECT * FROM Orders
FULL OUTER JOIN Product
ON Orders.product_id = Product.id
```

###  1.3. <a name='ArrayExpansion'></a>Array Expansion

交叉连接，计算笛卡儿积；

```sql
SELECT order_id, tag FROM Orders 
CROSS JOIN UNNEST(tags) AS t (tag)
```

##  2. <a name='IntervalJoins'></a>Interval Joins - 时间相关

Interval Joins 至少需要一个`等连接谓词`和一个`连接条件`，以限制`两侧`的时间。

以下`谓词`是`有效的区间连接条件`的示例：

```sql
ltime = rtime
ltime >= rtime AND ltime < rtime + INTERVAL '10' MINUTE
ltime BETWEEN rtime - INTERVAL '10' SECOND AND rtime + INTERVAL '5' SECOND
```

例如，如果`订单`在收到订单`四小时`后`发货`，此查询将把所有`订单`与其对应的`发货`关联起来。

```sql
SELECT * FROM Orders o, Shipments s
WHERE o.id = s.order_id
AND o.order_time BETWEEN s.ship_time - INTERVAL '4' HOUR AND s.ship_time
```

##  3. <a name='FORSYSTEM_TIMEASOF'></a>FOR SYSTEM_TIME AS OF 


**对比 WATERMARK FOR xxx AS xxx** 出现在 CREATE TABLE 语句中

```sql
WATERMARK FOR order_time AS order_time
WATERMARK FOR update_time AS update_time
WATERMARK FOR user_action_time AS user_action_time - INTERVAL '5' SECOND
-- 声明 user_action_time 是`事件时间属性`，并且用 延迟 5 秒的策略来生成 watermark

WATERMARK FOR time_ltz AS time_ltz - INTERVAL '5' SECOND
WATERMARK FOR order_time AS order_time - INTERVAL '30' SECOND
```

**FOR SYSTEM_TIME AS OF 字句** 出现在 CREATE TABLE 语句 后面

```sql
FROM orders
LEFT JOIN currency_rates FOR SYSTEM_TIME AS OF orders.order_time

FROM Orders AS o
JOIN Customers FOR SYSTEM_TIME AS OF o.proc_time AS c
```

```sql
FROM 表一
[LEFT] JOIN 表二 FOR SYSTEM_TIME AS OF 表一.{ proctime | rowtime }
```


###  3.1. <a name='EventTimeTemporalJoin'></a>Event Time Temporal Join

`时态表`是一种随时间演变的表——在Flink中也称为`动态表`。`时态表`中的行与一个或多个`时态周期`相关联，所有Flink表都是`时态(动态)`。`时态表`包含一个或多个版本化的表快照，它可以是一个变化的历史表来跟踪这些变化(例如:数据库更改日志，包含所有快照)或一个更改`维度表`，以实现更改(例如。包含最新快照的数据库表)。

`事件时间`时态连接允许根据版本控制的表进行连接。这意味着可以通过更改`元数据`来`充实表`，并在某个时间点`检索`它的值。

`时态`连接取`任意表`(`左侧输`入/探测站点)，并将每一行关联到版本化表中相应行的相关版本(`右侧输入`/构建端)。Flink使用`FOR SYSTEM_TIME AS of`的SQL语法来执行SQL:2011标准中的这个操作。时态连接的语法如下:

```sql
SELECT [column_list]
FROM 表一 [AS <alias1>]
[LEFT] JOIN 表二 FOR SYSTEM_TIME AS OF 表一.{ proctime | rowtime } [AS <alias2>]
ON 表一.column-name1 = 表二.column-name1
```

使用`事件时间属性`（即`rowtime`属性），可以像过去某个时刻一样检索键的值。这允许在`同一时间点`连接两个表。版本表将存储自上次`水印`以来的所有版本（以时间标识）。

例如，假设我们有一个`订单表`，每个订单都有不同`货币`的价格。为了正确地将该表标准化为`单一货币`，例如美元，每个订单都需要从下订单的时间点开始加入正确的`货币兑换率`。

```sql
-- 创建订单表。这是一个标准
-- 扩展动态表 append-only dynamic table
CREATE TABLE orders (
    order_id    STRING,
    price       DECIMAL(32,2),
    currency    STRING,
    order_time  TIMESTAMP(3),
    WATERMARK FOR order_time AS order_time
    注意:`事件时间`时间`连接`是由左右两侧的`水印`触发的; 请确保联接的两边已正确设置`水印`。
) WITH (/* ... */);

-- 定义一个 versioned table版本化的货币汇率表。
-- 这可能来自更改数据捕获
-- 比如Debezium，一个紧凑的卡夫卡主题，或者其他任何主题
-- 定义 versioned table 版本化表 的方法。
CREATE TABLE currency_rates (
    currency STRING,
    conversion_rate DECIMAL(32, 2),
    update_time TIMESTAMP(3) METADATA FROM `values.source.timestamp` VIRTUAL,
    WATERMARK FOR update_time AS update_time,
    注意:`事件时间`时间`连接`是由左右两侧的`水印`触发的; 请确保联接的两边已正确设置`水印`。
    PRIMARY KEY(currency) NOT ENFORCED
    注意: `事件时间`时态连接要求在时态连接条件的等价条件中包含`主键`。
) WITH (
    'connector' = 'kafka',
    'value.format' = 'debezium-json',
   /* ... */
);

SELECT 
     order_id,
     price,
     currency,
     conversion_rate,
     order_time,
FROM orders
LEFT JOIN currency_rates FOR SYSTEM_TIME AS OF orders.order_time
ON orders.currency = currency_rates.currency;
```

```s
order_id  price  currency  conversion_rate  order_time
========  =====  ========  ===============  =========
o_001     11.11  EUR       1.14             12:00:00
o_002     12.51  EUR       1.10             12:06:00
```

###  3.2. <a name='LookupJoin'></a>Lookup Join

`Temporal Tables` 是随时间变化而变化的表。 

`Temporal Table` 提供访问指定时间点的 temporal table 版本的功能。

仅支持带有处理时间的 temporal tables 的 `inner` 和 `left join`。

是我们常说的Flink中与 `维表` 关联的 `Join`，`Lookup Join` 与前面的Join原理并不相同，

这个的实现更类似于`Map Join`方式，不会涉及到`state相关存储`。

`查找连接 lookup join`通常用于用从`external system外部系统`查询来充实表的数据。

该`join 连接`要求一个表具有`处理时间属性 processing time attribute`，另一个表由`lookup source connector 查找源连接器`支持。

`lookup join 查找连接`使用上面的 `Processing Time Temporal Join 时态连接`语法，以及由`lookup source connector查找源连接器`支持的`正确表 right table`。

下面的示例显示了指定`查找联接 lookup join`的语法。

```sql
-- 客户端由JDBC连接器支持，可以用于查找连接
CREATE TEMPORARY TABLE Customers (
  id INT,
  name STRING,
  country STRING,
  zip STRING
) WITH (
  'connector' = 'jdbc',
  'url' = 'jdbc:mysql://mysqlhost:3306/customerdb',
  'table-name' = 'customers'
);

-- 用客户信息丰富每个订单
-- enrich each order with customer information
SELECT o.order_id, o.total, c.country, c.zip
FROM Orders AS o
  JOIN Customers FOR SYSTEM_TIME AS OF o.proc_time AS c
    ON o.customer_id = c.id;

    Orders表使用了来自MySQL数据库中的Customers表的数据。
```

##  4. <a name='TableFuntion'></a>Table Funtion

###  4.1. <a name='INNERJOIN'></a>INNER JOIN 

如果table function返回了一个空结果，那么左侧数据会被丢弃

```sql
SELECT order_id, res
FROM Orders,
LATERAL TABLE(table_func(order_id)) t(res)
```

####  4.1.1. <a name='ProcessingTimeTemporalJoin'></a> Processing Time Temporal Join 

`处理时间时态表联接`使用`处理时间`属性将行与`外部版本表`中键的最新版本相关联。

根据定义，通过 `处理时间` 属性，`联接` 将始终返回给定键的最新值。可以将 `查找表` 看作是一个简单的 `HashMap<K，V>`，它存储来自构建端的所有记录。这种 `连接` 的强大之处在于，当无法在Flink中将表具体化为 `动态表` 时，它允许Flink直接针对 `外部系统` 工作。

下面的 `处理时间时态表联接` 示例显示了应与表 `LatestRates联接`的仅追加表orders。`LatestRates`是以最新速率具体化的`维度表`（例如`HBase表`）。`10:15、10:30、10:52`时，迟到者的内容如下：

```sql
10:15> SELECT * FROM LatestRates;

currency   rate
======== ======
US Dollar   102
Euro        114
Yen           1

10:30> SELECT * FROM LatestRates;

currency   rate
======== ======
US Dollar   102
Euro        114
Yen           1

10:52> SELECT * FROM LatestRates;

currency   rate
======== ======
US Dollar   102
Euro        116     <==== changed from 114 to 116
Yen           1
```

```sql
SELECT * FROM Orders;

amount currency
====== =========
     2 Euro             <== arrived at time 10:15
     1 US Dollar        <== arrived at time 10:30
     2 Euro             <== arrived at time 10:52

给定这些表，我们希望计算所有转换成共同货币的Orders。

amount currency     rate   amount*rate
====== ========= ======= ============
     2 Euro          114          228    <== arrived at time 10:15
     1 US Dollar     102          102    <== arrived at time 10:30
     2 Euro          116          232    <== arrived at time 10:52
```


目前，在时态连接中使用的`FOR SYSTEM_TIME AS OF`语法与任何视图/表的最新版本`还不支持`，你可以使用`时态表函数`语法如下:

```sql
SELECT
  o_amount, r_rate
FROM
  Orders,
  LATERAL TABLE (Rates(o_proctime))
--   假设 Rates 是一个 temporal table function
-- 在 SQL 里面用 JOIN 或者 以 ON TRUE 为条件的 LEFT JOIN 来配合 LATERAL TABLE(<TableFunction>) 的使用。
WHERE
  r_currency = o_currency
```

 LATERAL TABLE (`Rates`(o_proctime)) 中 的 `Rates` 是 [自定义函数](https://nightlies.apache.org/flink/flink-docs-release-1.14/zh/docs/dev/table/functions/udfs/#table-functions)

####  4.1.2. <a name='LATERALTABLE'></a>补充：LATERAL TABLE

```sql
// 在 SQL 里调用注册好的函数- SplitFunction
env.sqlQuery(
  "SELECT myField, word, length " +
  "FROM MyTable, LATERAL TABLE(SplitFunction(myField))");
env.sqlQuery(
  "SELECT myField, word, length " +
  "FROM MyTable " +
  "LEFT JOIN LATERAL TABLE(SplitFunction(myField)) ON TRUE")

// 在 SQL 里重命名函数字段
env.sqlQuery(
  "SELECT myField, newWord, newLength " +
  "FROM MyTable " +
  "LEFT JOIN LATERAL TABLE(SplitFunction(myField)) AS T(newWord, newLength) ON TRUE")

```

```sql
// 在 Table API 里调用注册好的函数-SplitFunction
env
  .from("MyTable")
  .joinLateral(call("SplitFunction", $"myField"))
  .select($"myField", $"word", $"length")
env
  .from("MyTable")
  .leftOuterJoinLateral(call("SplitFunction", $"myField"))
  .select($"myField", $"word", $"length")
```

关于 SplitFunction 注册好的函数 extends TableFunction

```scala
import org.apache.flink.table.annotation.DataTypeHint
import org.apache.flink.table.annotation.FunctionHint
import org.apache.flink.table.api._
import org.apache.flink.table.functions.TableFunction
import org.apache.flink.types.Row

@FunctionHint(output = new DataTypeHint("ROW<word STRING, length INT>"))
class SplitFunction extends TableFunction[Row] {

  def eval(str: String): Unit = {
    // use collect(...) to emit a row
    str.split(" ").foreach(s => collect(Row.of(s, Int.box(s.length))))
  }
}

val env = TableEnvironment.create(...)
```


###  4.2. <a name='LEFTOUTERJOIN'></a>LEFT OUTER JOIN

如果table function返回了一个空结果，那么右侧数据会用null进行填充，数据会被下发

```sql
SELECT order_id, res
FROM Orders
LEFT OUTER JOIN LATERAL TABLE(table_func(order_id)) t(res)
  ON TRUE
```
