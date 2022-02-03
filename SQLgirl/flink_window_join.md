<!-- vscode-markdown-toc -->
* 1. [概述](#)
* 2. [hop](#hop)
* 3. [cumulate](#cumulate)
* 4. [TUMBLE](#TUMBLE)
	* 4.1. [SEMI](#SEMI)
	* 4.2. [ANTI](#ANTI)
* 5. [Window Functions 之前的版本](#WindowFunctions)
* 6. [Window Top-N](#WindowTop-N)
* 7. [表格式（table format）](#tableformat)
* 8. [当前系统时间-CURRENT_TIMESTAMP](#-CURRENT_TIMESTAMP)
* 9. [watermark 策略](#watermark)
	* 9.1. [几种常用的 watermark 策略](#watermark-1)
	* 9.2. [一些栗子](#-1)
* 10. [流式 Sink](#Sink)
* 11. [时间函数](#-1)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->
##  1. <a name=''></a>概述

当前条件中需要`windw start`以及`window end`条件；

Windowing TVF之后进行window join需要是下面三种窗口：

- tumble window

- hop window

- Cumulate window

当前left、 right的窗口需要时相同的，目前不支持一个tumble窗口一个hop窗口等。



```sql
INNER/LEFT/RIGHT/FULL OUTER
SELECT ...
FROM L [LEFT|RIGHT|FULL OUTER] JOIN R -- L and R are relations applied windowing TVF
ON L.window_start = R.window_start AND L.window_end = R.window_end AND ...
```

##  2. <a name='hop'></a>hop 

其中 HOP windows 对应 Table API 中的 Sliding Window, 同时每种窗口分别有相应的使用场景和方法.


```sql
HOP(TABLE data, DESCRIPTOR(timecol), slide, size [, offset ])
```

```scala
//这里需要注意的是 如果采用了EventTime, 那么 对应字段后面加 .rowtime, 否则加 .proctime
Table logT = tEnv.fromDataStream(logWithTime, "t.rowtime, name, v");

// HOP(time_attr, interval1, interval2)
// interval1 滑动长度
// interval2 窗口长度
// HOP_START(t, INTERVAL '5' SECOND, INTERVAL '10' SECOND) 表示窗口开始时间
// HOP_END(t, INTERVAL '5' SECOND, INTERVAL '10' SECOND) 表示窗口结束时间
Table result = tEnv.sqlQuery(
  "SELECT HOP_START(t, INTERVAL '5' SECOND, INTERVAL '10' SECOND) AS window_start," 
        + "HOP_END(t, INTERVAL '5' SECOND, INTERVAL '10' SECOND) AS window_end, SUM(v) FROM "
        + logT + " GROUP BY HOP(t, INTERVAL '5' SECOND, INTERVAL '10' SECOND)");
```


下面是对Bid表的一个示例调用:

```sql
-- NOTE: 目前，Flink不支持对单个窗口`table-valued function表值函数`进行求值
--  window 表值函数 should be used with aggregate operation,
--  this example is just used for explaining the syntax and the data produced by 表值函数.
> SELECT * FROM TABLE(
    HOP(TABLE Bid, DESCRIPTOR(bidtime), INTERVAL '5' MINUTES, INTERVAL '10' MINUTES));
-- or with the named params
-- note: the DATA param must be the first
> SELECT * FROM TABLE(
    HOP(
      DATA => TABLE Bid,
      TIMECOL => DESCRIPTOR(bidtime),
      SLIDE => INTERVAL '5' MINUTES,
      SIZE => INTERVAL '10' MINUTES));
+------------------+-------+------+------------------+------------------+-------------------------+
|          bidtime | price | item |     window_start |       window_end |           window_time   |
+------------------+-------+------+------------------+------------------+-------------------------+
| 2020-04-15 08:05 |  4.00 | C    | 2020-04-15 08:00 | 2020-04-15 08:10 | 2020-04-15 08:09:59.999 |
| 2020-04-15 08:05 |  4.00 | C    | 2020-04-15 08:05 | 2020-04-15 08:15 | 2020-04-15 08:14:59.999 |
| 2020-04-15 08:07 |  2.00 | A    | 2020-04-15 08:00 | 2020-04-15 08:10 | 2020-04-15 08:09:59.999 |
| 2020-04-15 08:07 |  2.00 | A    | 2020-04-15 08:05 | 2020-04-15 08:15 | 2020-04-15 08:14:59.999 |
| 2020-04-15 08:09 |  5.00 | D    | 2020-04-15 08:00 | 2020-04-15 08:10 | 2020-04-15 08:09:59.999 |
| 2020-04-15 08:09 |  5.00 | D    | 2020-04-15 08:05 | 2020-04-15 08:15 | 2020-04-15 08:14:59.999 |
| 2020-04-15 08:11 |  3.00 | B    | 2020-04-15 08:05 | 2020-04-15 08:15 | 2020-04-15 08:14:59.999 |
| 2020-04-15 08:11 |  3.00 | B    | 2020-04-15 08:10 | 2020-04-15 08:20 | 2020-04-15 08:19:59.999 |
| 2020-04-15 08:13 |  1.00 | E    | 2020-04-15 08:05 | 2020-04-15 08:15 | 2020-04-15 08:14:59.999 |
| 2020-04-15 08:13 |  1.00 | E    | 2020-04-15 08:10 | 2020-04-15 08:20 | 2020-04-15 08:19:59.999 |
| 2020-04-15 08:17 |  6.00 | F    | 2020-04-15 08:10 | 2020-04-15 08:20 | 2020-04-15 08:19:59.999 |
| 2020-04-15 08:17 |  6.00 | F    | 2020-04-15 08:15 | 2020-04-15 08:25 | 2020-04-15 08:24:59.999 |
+------------------+-------+------+------------------+------------------+-------------------------+

-- apply aggregation on the hopping windowed table
> SELECT window_start, window_end, SUM(price)
  FROM TABLE(
    HOP(TABLE Bid, DESCRIPTOR(bidtime), INTERVAL '5' MINUTES, INTERVAL '10' MINUTES))
  GROUP BY window_start, window_end;
+------------------+------------------+-------+
|     window_start |       window_end | price |
+------------------+------------------+-------+
| 2020-04-15 08:00 | 2020-04-15 08:10 | 11.00 |
| 2020-04-15 08:05 | 2020-04-15 08:15 | 15.00 |
| 2020-04-15 08:10 | 2020-04-15 08:20 | 10.00 |
| 2020-04-15 08:15 | 2020-04-15 08:25 |  6.00 |
+------------------+------------------+-------+
```

##  3. <a name='cumulate'></a>cumulate



CUMULATE有四个必需参数，一个可选参数:

```sql
CUMULATE(TABLE data, DESCRIPTOR(timecol), step, size)
```

```sql
-- NOTE: Currently Flink doesn't support evaluating individual window table-valued function,
--  window table-valued function should be used with aggregate operation,
--  this example is just used for explaining the syntax and the data produced by table-valued function.
> SELECT * FROM TABLE(
    CUMULATE(TABLE Bid, DESCRIPTOR(bidtime), INTERVAL '2' MINUTES, INTERVAL '10' MINUTES));
-- or with the named params
-- note: the DATA param must be the first
> SELECT * FROM TABLE(
    CUMULATE(
      DATA => TABLE Bid,
      TIMECOL => DESCRIPTOR(bidtime),
      STEP => INTERVAL '2' MINUTES,
      SIZE => INTERVAL '10' MINUTES));
+------------------+-------+------+------------------+------------------+-------------------------+
|          bidtime | price | item |     window_start |       window_end |            window_time  |
+------------------+-------+------+------------------+------------------+-------------------------+
| 2020-04-15 08:05 |  4.00 | C    | 2020-04-15 08:00 | 2020-04-15 08:06 | 2020-04-15 08:05:59.999 |
| 2020-04-15 08:05 |  4.00 | C    | 2020-04-15 08:00 | 2020-04-15 08:08 | 2020-04-15 08:07:59.999 |
| 2020-04-15 08:05 |  4.00 | C    | 2020-04-15 08:00 | 2020-04-15 08:10 | 2020-04-15 08:09:59.999 |
| 2020-04-15 08:07 |  2.00 | A    | 2020-04-15 08:00 | 2020-04-15 08:08 | 2020-04-15 08:07:59.999 |
| 2020-04-15 08:07 |  2.00 | A    | 2020-04-15 08:00 | 2020-04-15 08:10 | 2020-04-15 08:09:59.999 |
| 2020-04-15 08:09 |  5.00 | D    | 2020-04-15 08:00 | 2020-04-15 08:10 | 2020-04-15 08:09:59.999 |
| 2020-04-15 08:11 |  3.00 | B    | 2020-04-15 08:10 | 2020-04-15 08:12 | 2020-04-15 08:11:59.999 |
| 2020-04-15 08:11 |  3.00 | B    | 2020-04-15 08:10 | 2020-04-15 08:14 | 2020-04-15 08:13:59.999 |
| 2020-04-15 08:11 |  3.00 | B    | 2020-04-15 08:10 | 2020-04-15 08:16 | 2020-04-15 08:15:59.999 |
| 2020-04-15 08:11 |  3.00 | B    | 2020-04-15 08:10 | 2020-04-15 08:18 | 2020-04-15 08:17:59.999 |
| 2020-04-15 08:11 |  3.00 | B    | 2020-04-15 08:10 | 2020-04-15 08:20 | 2020-04-15 08:19:59.999 |
| 2020-04-15 08:13 |  1.00 | E    | 2020-04-15 08:10 | 2020-04-15 08:14 | 2020-04-15 08:13:59.999 |
| 2020-04-15 08:13 |  1.00 | E    | 2020-04-15 08:10 | 2020-04-15 08:16 | 2020-04-15 08:15:59.999 |
| 2020-04-15 08:13 |  1.00 | E    | 2020-04-15 08:10 | 2020-04-15 08:18 | 2020-04-15 08:17:59.999 |
| 2020-04-15 08:13 |  1.00 | E    | 2020-04-15 08:10 | 2020-04-15 08:20 | 2020-04-15 08:19:59.999 |
| 2020-04-15 08:17 |  6.00 | F    | 2020-04-15 08:10 | 2020-04-15 08:18 | 2020-04-15 08:17:59.999 |
| 2020-04-15 08:17 |  6.00 | F    | 2020-04-15 08:10 | 2020-04-15 08:20 | 2020-04-15 08:19:59.999 |
+------------------+-------+------+------------------+------------------+-------------------------+

-- apply aggregation on the cumulating windowed table
> SELECT window_start, window_end, SUM(price)
  FROM TABLE(
    CUMULATE(TABLE Bid, DESCRIPTOR(bidtime), INTERVAL '2' MINUTES, INTERVAL '10' MINUTES))
  GROUP BY window_start, window_end;
+------------------+------------------+-------+
|     window_start |       window_end | price |
+------------------+------------------+-------+
| 2020-04-15 08:00 | 2020-04-15 08:06 |  4.00 |
| 2020-04-15 08:00 | 2020-04-15 08:08 |  6.00 |
| 2020-04-15 08:00 | 2020-04-15 08:10 | 11.00 |
| 2020-04-15 08:10 | 2020-04-15 08:12 |  3.00 |
| 2020-04-15 08:10 | 2020-04-15 08:14 |  4.00 |
| 2020-04-15 08:10 | 2020-04-15 08:16 |  4.00 |
| 2020-04-15 08:10 | 2020-04-15 08:18 | 10.00 |
| 2020-04-15 08:10 | 2020-04-15 08:20 | 10.00 |
+------------------+------------------+-------+

size 是 10 分钟，step 是 2 分钟
```

##  4. <a name='TUMBLE'></a>TUMBLE

TUMBLE函数有三个必需参数，一个可选参数:

```sql
SELECT * FROM TABLE(
   TUMBLE(TABLE Bid, DESCRIPTOR(bidtime), INTERVAL '10' MINUTES));
-- or with the named params
-- note: the DATA param must be the first
```

```sql
SELECT * FROM TABLE(
   TUMBLE(
     DATA => TABLE Bid,
     TIMECOL => DESCRIPTOR(bidtime),
     SIZE => INTERVAL '10' MINUTES));
```

```s
+------------------+-------+------+------------------+------------------+-------------------------+
|          bidtime | price | item |     window_start |       window_end |            window_time  |
+------------------+-------+------+------------------+------------------+-------------------------+
| 2020-04-15 08:05 |  4.00 | C    | 2020-04-15 08:00 | 2020-04-15 08:10 | 2020-04-15 08:09:59.999 |
| 2020-04-15 08:07 |  2.00 | A    | 2020-04-15 08:00 | 2020-04-15 08:10 | 2020-04-15 08:09:59.999 |
| 2020-04-15 08:09 |  5.00 | D    | 2020-04-15 08:00 | 2020-04-15 08:10 | 2020-04-15 08:09:59.999 |
| 2020-04-15 08:11 |  3.00 | B    | 2020-04-15 08:10 | 2020-04-15 08:20 | 2020-04-15 08:19:59.999 |
| 2020-04-15 08:13 |  1.00 | E    | 2020-04-15 08:10 | 2020-04-15 08:20 | 2020-04-15 08:19:59.999 |
| 2020-04-15 08:17 |  6.00 | F    | 2020-04-15 08:10 | 2020-04-15 08:20 | 2020-04-15 08:19:59.999 |
+------------------+-------+------+------------------+------------------+-------------------------+
```

```sql
SELECT window_start, window_end, SUM(price)
  FROM TABLE(
    TUMBLE(TABLE Bid, DESCRIPTOR(bidtime), INTERVAL '10' MINUTES))
  GROUP BY window_start, window_end;
```

```s
+------------------+------------------+-------+
|     window_start |       window_end | price |
+------------------+------------------+-------+
| 2020-04-15 08:00 | 2020-04-15 08:10 | 11.00 |
| 2020-04-15 08:10 | 2020-04-15 08:20 | 10.00 |
+------------------+------------------+-------+
```

###  4.1. <a name='SEMI'></a>SEMI

```sql
SELECT * FROM 
(SELECT * FROM TABLE(TUMBLE(TABLE LeftTable, DESCRIPTOR(row_time), INTERVAL '5' MINUTES))) AS L 
WHERE L.num IN 
-- 注意，这里是 in
    (
        SELECT num FROM 
        (SELECT * FROM TABLE(TUMBLE(TABLE RightTable, DESCRIPTOR(row_time), INTERVAL '5' MINUTES))) AS R 
        WHERE L.window_start = R.window_start AND L.window_end = R.window_end
    );

+------------------+-----+----+------------------+------------------+-------------------------+
|         row_time | num | id |     window_start |       window_end |            window_time  |
+------------------+-----+----+------------------+------------------+-------------------------+
| 2020-04-15 12:03 |   3 | L3 | 2020-04-15 12:00 | 2020-04-15 12:05 | 2020-04-15 12:04:59.999 |
+------------------+-----+----+------------------+------------------+-------------------------+

SELECT * FROM 
(SELECT * FROM TABLE(TUMBLE(TABLE LeftTable, DESCRIPTOR(row_time), INTERVAL '5' MINUTES))) AS L 
WHERE EXISTS 
    (
        SELECT * FROM 
        (SELECT * FROM TABLE(TUMBLE(TABLE RightTable, DESCRIPTOR(row_time), INTERVAL '5' MINUTES))) AS R 
        WHERE L.num = R.num AND L.window_start = R.window_start AND L.window_end = R.window_end
    );

+------------------+-----+----+------------------+------------------+-------------------------+
|         row_time | num | id |     window_start |       window_end |            window_time  |
+------------------+-----+----+------------------+------------------+-------------------------+
| 2020-04-15 12:03 |   3 | L3 | 2020-04-15 12:00 | 2020-04-15 12:05 | 2020-04-15 12:04:59.999 |
+------------------+-----+----+------------------+------------------+-------------------------+
```

###  4.2. <a name='ANTI'></a>ANTI

```sql
SELECT * FROM 
(SELECT * FROM TABLE(TUMBLE(TABLE LeftTable, DESCRIPTOR(row_time), INTERVAL '5' MINUTES))) AS L 
WHERE L.num NOT IN 
-- 注意，这里是 not in
    (
        SELECT num FROM 
        (SELECT * FROM TABLE(TUMBLE(TABLE RightTable, DESCRIPTOR(row_time), INTERVAL '5' MINUTES))) AS R 
        WHERE L.window_start = R.window_start AND L.window_end = R.window_end
    );

+------------------+-----+----+------------------+------------------+-------------------------+
|         row_time | num | id |     window_start |       window_end |            window_time  |
+------------------+-----+----+------------------+------------------+-------------------------+
| 2020-04-15 12:02 |   1 | L1 | 2020-04-15 12:00 | 2020-04-15 12:05 | 2020-04-15 12:04:59.999 |
| 2020-04-15 12:06 |   2 | L2 | 2020-04-15 12:05 | 2020-04-15 12:10 | 2020-04-15 12:09:59.999 |
+------------------+-----+----+------------------+------------------+-------------------------+

SELECT * FROM 
(SELECT * FROM TABLE(TUMBLE(TABLE LeftTable, DESCRIPTOR(row_time), INTERVAL '5' MINUTES))) AS L 
WHERE NOT EXISTS (
        SELECT * FROM 
        (SELECT * FROM TABLE(TUMBLE(TABLE RightTable, DESCRIPTOR(row_time), INTERVAL '5' MINUTES))) AS R 
        WHERE L.num = R.num AND L.window_start = R.window_start AND L.window_end = R.window_end);

+------------------+-----+----+------------------+------------------+-------------------------+
|         row_time | num | id |     window_start |       window_end |            window_time  |
+------------------+-----+----+------------------+------------------+-------------------------+
| 2020-04-15 12:02 |   1 | L1 | 2020-04-15 12:00 | 2020-04-15 12:05 | 2020-04-15 12:04:59.999 |
| 2020-04-15 12:06 |   2 | L2 | 2020-04-15 12:05 | 2020-04-15 12:10 | 2020-04-15 12:09:59.999 |
+------------------+-----+----+------------------+------------------+-------------------------+
```

##  5. <a name='WindowFunctions'></a>Window Functions 之前的版本

Session Windows

```scala
// SESSION(time_attr, interval)
// interval 表示两条数据触发session的最大间隔
Table result = tEnv.sqlQuery("SELECT SESSION_START(t, INTERVAL '5' SECOND) AS window_start," 
                             +"SESSION_END(t, INTERVAL '5' SECOND) AS window_end, SUM(v) FROM "
                             + logT + " GROUP BY SESSION(t, INTERVAL '5' SECOND)");
```

Tumble Windows = TUMBLE_START + TUMBLE_END

```scala
// GROUP BY TUMBLE(t, INTERVAL '10' SECOND) 相当于根据10s的时间来划分窗口
// TUMBLE_START(t, INTERVAL '10' SECOND) 获取窗口的开始时间
// TUMBLE_END(t, INTERVAL '10' SECOND) 获取窗口的结束时间
tEnv.sqlQuery("SELECT TUMBLE_START(t, INTERVAL '10' SECOND) AS window_start," +
                "TUMBLE_END(t, INTERVAL '10' SECOND) AS window_end, SUM(v) FROM "
                + logT + " GROUP BY TUMBLE(t, INTERVAL '10' SECOND)");
```




##  6. <a name='WindowTop-N'></a>Window Top-N 

```sql
-- tables must have time attribute, e.g. `bidtime` in this table
Flink SQL> desc Bid;
+-------------+------------------------+------+-----+--------+---------------------------------+
|        name |                   type | null | key | extras |                       watermark |
+-------------+------------------------+------+-----+--------+---------------------------------+
|     bidtime | TIMESTAMP(3) *ROWTIME* | true |     |        | `bidtime` - INTERVAL '1' SECOND |
|       price |         DECIMAL(10, 2) | true |     |        |                                 |
|        item |                 STRING | true |     |        |                                 |
| supplier_id |                 STRING | true |     |        |                                 |
+-------------+------------------------+------+-----+--------+---------------------------------+

Flink SQL> SELECT * FROM Bid;
+------------------+-------+------+-------------+
|          bidtime | price | item | supplier_id |
+------------------+-------+------+-------------+
| 2020-04-15 08:05 |  4.00 |    A |   supplier1 |
| 2020-04-15 08:06 |  4.00 |    C |   supplier2 |
| 2020-04-15 08:07 |  2.00 |    G |   supplier1 |
| 2020-04-15 08:08 |  2.00 |    B |   supplier3 |
| 2020-04-15 08:09 |  5.00 |    D |   supplier4 |
| 2020-04-15 08:11 |  2.00 |    B |   supplier3 |
| 2020-04-15 08:13 |  1.00 |    E |   supplier1 |
| 2020-04-15 08:15 |  3.00 |    H |   supplier2 |
| 2020-04-15 08:17 |  6.00 |    F |   supplier5 |
+------------------+-------+------+-------------+

SELECT * FROM 
(
    SELECT *, ROW_NUMBER() OVER (PARTITION BY window_start, window_end ORDER BY price DESC) as rownum
    FROM 
    (
        SELECT window_start, window_end, supplier_id, SUM(price) as price, COUNT(*) as cnt
        FROM TABLE
        (
            TUMBLE(TABLE Bid, DESCRIPTOR(bidtime), INTERVAL '10' MINUTES)
        )
        GROUP BY window_start, window_end, supplier_id
    )
) WHERE rownum <= 3;

+------------------+------------------+-------------+-------+-----+--------+
|     window_start |       window_end | supplier_id | price | cnt | rownum |
+------------------+------------------+-------------+-------+-----+--------+
| 2020-04-15 08:00 | 2020-04-15 08:10 |   supplier1 |  6.00 |   2 |      1 |
| 2020-04-15 08:00 | 2020-04-15 08:10 |   supplier4 |  5.00 |   1 |      2 |
| 2020-04-15 08:00 | 2020-04-15 08:10 |   supplier2 |  4.00 |   1 |      3 |
| 2020-04-15 08:10 | 2020-04-15 08:20 |   supplier5 |  6.00 |   1 |      1 |
| 2020-04-15 08:10 | 2020-04-15 08:20 |   supplier2 |  3.00 |   1 |      2 |
| 2020-04-15 08:10 | 2020-04-15 08:20 |   supplier3 |  2.00 |   1 |      3 |
+------------------+------------------+-------------+-------+-----+--------+
```

下面的例子展示了如何计算每10分钟滚动窗口价格最高的前3项物品。

```sql
SELECT * FROM 
(
    SELECT *, ROW_NUMBER() OVER (PARTITION BY window_start, window_end ORDER BY price DESC) as rownum
    FROM TABLE
        (
            TUMBLE(TABLE Bid, DESCRIPTOR(bidtime), INTERVAL '10' MINUTES)
        )
) WHERE rownum <= 3;

+------------------+-------+------+-------------+------------------+------------------+--------+
|          bidtime | price | item | supplier_id |     window_start |       window_end | rownum |
+------------------+-------+------+-------------+------------------+------------------+--------+
| 2020-04-15 08:05 |  4.00 |    A |   supplier1 | 2020-04-15 08:00 | 2020-04-15 08:10 |      2 |
| 2020-04-15 08:06 |  4.00 |    C |   supplier2 | 2020-04-15 08:00 | 2020-04-15 08:10 |      3 |
| 2020-04-15 08:09 |  5.00 |    D |   supplier4 | 2020-04-15 08:00 | 2020-04-15 08:10 |      1 |
| 2020-04-15 08:11 |  2.00 |    B |   supplier3 | 2020-04-15 08:10 | 2020-04-15 08:20 |      3 |
| 2020-04-15 08:15 |  3.00 |    H |   supplier2 | 2020-04-15 08:10 | 2020-04-15 08:20 |      2 |
| 2020-04-15 08:17 |  6.00 |    F |   supplier5 | 2020-04-15 08:10 | 2020-04-15 08:20 |      1 |
+------------------+-------+------+-------------+------------------+------------------+--------+
```

##  7. <a name='tableformat'></a>表格式（table format）

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

Flink 支持和在 `TIMESTAMP 列` 和 `TIMESTAMP_LTZ 列` 上定义`事件时间`。

如果源数据中的时间戳数据表示为`年-月-日-时-分-秒`，则通常为不带时区信息的字符串值，

例如 2020-04-15 20:13:40.564，建议将`事件时间`属性定义在 `TIMESTAMP 列` 上:

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

##  8. <a name='-CURRENT_TIMESTAMP'></a>当前系统时间-CURRENT_TIMESTAMP

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

##  9. <a name='watermark'></a>watermark 策略

###  9.1. <a name='watermark-1'></a>几种常用的 watermark 策略

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

###  9.2. <a name='-1'></a>一些栗子

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

##  10. <a name='Sink'></a>流式 Sink

如下示例演示了如何使用文件系统连接器编写流查询语句查询 kafka 中的数据并写入到文件系统中，以及通过批查询把结果数据读取出来.

```sql
CREATE TABLE kafka_table (
  user_id STRING,
  order_amount DOUBLE,
  log_ts TIMESTAMP(3),
  WATERMARK FOR log_ts AS log_ts - INTERVAL '5' SECOND -- 在 TIMESTAMP 列上定义水印
) WITH (...);

CREATE TABLE fs_table (
  user_id STRING,
  order_amount DOUBLE,
  dt STRING,
  `hour` STRING
) PARTITIONED BY (dt, `hour`) WITH (
  'connector'='filesystem',
  'path'='...',
  'format'='parquet',
  'sink.partition-commit.delay'='1 h',
  'sink.partition-commit.policy.kind'='success-file'
);

-- streaming sql, 插入数据到文件系统表中
INSERT INTO fs_table 
SELECT 
    user_id, 
    order_amount, 
    DATE_FORMAT(log_ts, 'yyyy-MM-dd'),
    DATE_FORMAT(log_ts, 'HH') 
FROM kafka_table;

-- batch sql, 分区裁剪查询
SELECT * FROM fs_table WHERE dt='2020-05-20' and `hour`='12';
```

SELECT 语句的常见语法格式如下所示：

```sql
SELECT select_list FROM table_expression [ WHERE boolean_expression ]
```

```sql
SELECT * FROM Orders
SELECT order_id, price + tax FROM Orders
SELECT order_id, price FROM (VALUES (1, 2.0), (2, 3.1))  AS t (order_id, price)
SELECT price + tax FROM Orders WHERE id = 10
SELECT PRETTY_PRINT(order_id) FROM Orders
```

如果水印是定义在 TIMESTAMP_LTZ 列上，且使用了 partition-time 来提交分区, 则参数 sink.partition-commit.watermark-time-zone 需要被设置为会话的时区，否则分区会在若干小时后才会被提交。

```sql
CREATE TABLE kafka_table (
  user_id STRING,
  order_amount DOUBLE,
  ts BIGINT, -- epoch 毫秒时间
  ts_ltz AS TO_TIMESTAMP_LTZ(ts, 3),
  WATERMARK FOR ts_ltz AS ts_ltz - INTERVAL '5' SECOND -- 在 TIMESTAMP_LTZ 列上定义水印
) WITH (...);

CREATE TABLE fs_table (
  user_id STRING,
  order_amount DOUBLE,
  dt STRING,
  `hour` STRING
) PARTITIONED BY (dt, `hour`) WITH (
  'connector'='filesystem',
  'path'='...',
  'format'='parquet',
  'partition.time-extractor.timestamp-pattern'='$dt $hour:00:00',
  'sink.partition-commit.delay'='1 h',
  'sink.partition-commit.trigger'='partition-time',
  'sink.partition-commit.watermark-time-zone'='Asia/Shanghai', -- 假定用户配置的时区是 'Asia/Shanghai'
  'sink.partition-commit.policy.kind'='success-file'
);

-- streaming sql, 插入数据到文件系统表中
INSERT INTO fs_table 
SELECT 
    user_id, 
    order_amount, 
    DATE_FORMAT(ts_ltz, 'yyyy-MM-dd'),
    DATE_FORMAT(ts_ltz, 'HH') 
FROM kafka_table;

-- batch sql, 分区裁剪查询
SELECT * FROM fs_table WHERE dt='2020-05-20' and `hour`='12';
```

##  11. <a name='-1'></a>时间函数

```sql
DATE string
TIME string
TIMESTAMP string
INTERVAL string range
YEAR(date)
LOCALTIME
LOCALTIMESTAMP
CURRENT_TIME
CURRENT_DATE
CURRENT_TIMESTAMP
NOW()
CURRENT_ROW_TIMESTAMP()
EXTRACT(timeinteravlunit FROM temporal)
YEAR(date)
QUARTER(date)
MONTH(date)
WEEK(date)
DAYOFYEAR(date)
DAYOFMONTH
HOUR(timestamp)
MINUTE(timestamp)
SECOND(timestamp)
FLOOR(timepoint TO timeintervalunit)
CEIL(timespoint TO timeintervaluntit)
(timepoint1, temporal1) OVERLAPS (timepoint2, temporal2)
DATE_FORMAT(timestamp, string)
TIMESTAMPADD(timeintervalunit, interval, timepoint)
TIMESTAMPDIFF(timepointunit, timepoint1, timepoint2)
CONVERT_TZ(string1, string2, string3)
FROM_UNIXTIME(numeric[, string])
UNIX_TIMESTAMP()
UNIX_TIMESTAMP(string1[, string2])
TO_DATE(string1[, string2])
TO_TIMESTAMP_LTZ(numeric, precision)
TO_TIMESTAMP(string1[, string2])
CURRENT_WATERMARK(rowtime)
```