## 标准格式

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
    ) AS MR;
```

## PATTERN (A B+ C* D)

```s
* — 0 或者多行
+ — 1 或者多行
? — 0 或者 1 行
{ n } — 严格 n 行（n > 0）
{ n, } — n 或者更多行（n ≥ 0）
{ n, m } — 在 n 到 m（包含）行之间（0 ≤ n ≤ m，0 < m）
{ , m } — 在 0 到 m（包含）行之间（m > 0）
```

例如：

```s
PATTERN (A B* C)
PATTERN (A B* C) WITHIN INTERVAL '1' HOUR
（非标准 SQL）WITHIN 子句来定义模式的时间约束。
注意：然而，WITHIN 子句不是 SQL 标准的一部分。时间约束处理的方法已被提议将来可能会改变。
（见后面）

PATTERN (A+ B)
PATTERN (A B+)
PATTERN (A B? C)
PATTERN (A B+? C) 

PATTERN (A B*? C)
将 B* 修改为 B*? 的同一查询，这意味着 B* 应该是勉强的
目前不支持可选的勉强量词（A?? 或者 A{0,1}?）
PATTERN A?? 只支持贪婪的可选量词。

PATTERN((A B | C D) E) 
这样的模式，这意味着在寻找 E 行之前必须先找到子序列 A B 或者 C D

PATTERN (PERMUTE (A, B, C)) = PATTERN (A B C | A C B | B A C | B C A | C A B | C B A)

PATTERN ({- A -} B) 
表示将查找 A，但是不会参与输出。这只适用于 ALL ROWS PER MATCH 方式。
```


- 注意1: 不支持可能产生`空匹配`的模式。此类模式的示例如 `PATTERN (A*)`，`PATTERN (A? B*)`，`PATTERN (A{0,} B{0,} C*)` 等。

- 注意2：不允许使用类似 (A B*) 的模式。模式的最后一个变量不能使用`贪婪`量词.

- - 但：通过引入条件为 B 的人工状态（例如 C），可以轻松解决此问题。

```sql
因此，你可以使用类似以下的查询：

PATTERN (A B* C)
DEFINE
    A AS condA(),
    B AS condB(),
    C AS NOT condB()
```

- 注意3：`^, $`，表示分区的开始/结束，在流上下文中没有意义，将不被支持。

- 注意4：PATTERN(A (B C)+) 不是有效的模式。量词不能应用于模式的子序列。

- 注意5：B+ 表示 1 或者多行。查询将每个传入行映射到 B 变量，因此永远不会完成。

```sql
PATTERN (A B+ C)
DEFINE
  A as A.price > 10,
  C as C.price > 20

可以纠正此查询，例如：

PATTERN (A B+ C)
DEFINE
  A as A.price > 10,
  B as B.price <= 20,
  C as C.price > 20

或者使用 reluctant quantifier：

PATTERN (A B+? C)
DEFINE
  A as A.price > 10,
  C as C.price > 20
```


## WITHIN 子句

```sql
分析以下股票数据：

symbol         rowtime         price    tax
======  ====================  ======= =======
'ACME'  '01-Apr-11 10:00:00'   20      1
'ACME'  '01-Apr-11 10:20:00'   17      2
'ACME'  '01-Apr-11 10:40:00'   18      1
'ACME'  '01-Apr-11 11:00:00'   11      3
'ACME'  '01-Apr-11 11:20:00'   14      2
'ACME'  '01-Apr-11 11:40:00'   9       1
'ACME'  '01-Apr-11 12:00:00'   15      1
'ACME'  '01-Apr-11 12:20:00'   14      2
'ACME'  '01-Apr-11 12:40:00'   24      2
---------------结果部分---------------
'ACME'  '01-Apr-11 13:00:00'   1       2
---------------结果部分---------------
'ACME'  '01-Apr-11 13:20:00'   19      1

（在 01-Apr-11 10:00:00 和 01-Apr-11 11:40:00 之间），
这两个事件之间的时间差大于 1 小时。因此，它们不会产生匹配。
```

```sql
下面的示例查询说明了 WITHIN 子句的用法：

SELECT *
FROM Ticker
    MATCH_RECOGNIZE(
        PARTITION BY symbol
        ORDER BY rowtime
        MEASURES
            C.rowtime AS dropTime,
            A.price - C.price AS dropDiff 包含价格差异。
            注意：这里输出的是c，b表示可有可无

        ONE ROW PER MATCH
        AFTER MATCH SKIP PAST LAST ROW
        PATTERN (A B* C) WITHIN INTERVAL '1' HOUR
        说明： * — 0 或者多行

        DEFINE
            B AS B.price > A.price - 10
            C AS C.price < A.price - 10
            注意：这里输出的是c，b表示可有可无
    )
```

该查询检测到在 1 小时的间隔内价格下降了 10。

```sql
查询将生成以下结果：

symbol         dropTime         dropDiff
======  ====================  =============
'ACME'  '01-Apr-11 13:00:00'      14
```

##  DEFINE 子句

与简单 SQL 查询中的 WHERE 子句具有相近的含义。
 
 中指定行模式变量的逻辑组合。

```sql
DEFINE
      A AS name = 'a',
      B AS name = 'b',
      C AS name = 'c'
```

```sql
PATTERN (START_ROW PRICE_DOWN+ PRICE_UP)
-- 尽管不存在 START_ROW 模式变量,但它具有一个始终被评估为 TRUE 隐式条件。
DEFINE
-- 指定 PRICE_DOWN 和 PRICE_UP 事件需要满足的条件。
      PRICE_DOWN AS
            -- 和第一行对比
            (LAST(PRICE_DOWN.price, 1) IS NULL AND PRICE_DOWN.price < START_ROW.price) 
            OR
            -- 和上一行的自己对比
            PRICE_DOWN.price < LAST(PRICE_DOWN.price, 1),
      PRICE_UP AS
            -- 和上一行的自己对比
            PRICE_UP.price > LAST(PRICE_DOWN.price, 1)
```

关于last的映射关系见[模式导航](https://nightlies.apache.org/flink/flink-docs-release-1.14/zh/docs/dev/table/sql/queries/match_recognize/#time-constraint)

```sql
PATTERN (A B? C)
DEFINE
  B AS B.price < 20,
  C AS LAST(price, 1) < C.price
```

## AFTER MATCH SKIP 子句

指定在找到`完全匹配`后从何处开始`新的匹配过程`。

```sql
PAST LAST ROW - 在当前匹配的最后一行之后的`下一行`继续模式匹配。
TO NEXT ROW - 继续从匹配项开始行后的`下一行`开始搜索新匹配项。
TO LAST variable - 恢复映射到指定模式变量的`最后一行`的模式匹配。
TO FIRST variable - 在映射到指定模式变量的`第一行`继续模式匹配。
```

示例 #

```sql
对于以下输入行：

 symbol   tax   price         rowtime
======== ===== ======= =====================
 XYZ      1     7   #1    2018-09-17 10:00:01
 XYZ      2     9   #2    2018-09-17 10:00:02
 XYZ      1     10  #3    2018-09-17 10:00:03
 XYZ      2     5   #4    2018-09-17 10:00:04
 XYZ      2     10  #5    2018-09-17 10:00:05
 XYZ      2     7   #6    2018-09-17 10:00:06
 XYZ      2     14  #7    2018-09-17 10:00:07
```

我们使用不同的策略评估以下查询：

```sql
SELECT *
FROM Ticker
    MATCH_RECOGNIZE(
        PARTITION BY symbol
        ORDER BY rowtime
        MEASURES
            SUM(A.price) AS sumPrice,
            FIRST(rowtime) AS startTime,
            LAST(rowtime) AS endTime
        ONE ROW PER MATCH
        [AFTER MATCH STRATEGY]
        PATTERN (A+ C)
        DEFINE
            A AS SUM(A.price) < 30
    )
```

该查询返回映射到 A 的总体匹配的`第一个`和`最后一个时间戳`所有`行的价格之和`。

查询将根据使用的 AFTER MATCH 策略产生不同的结果：

1. `AFTER MATCH SKIP PAST LAST ROW` 匹配以后下一行

```sql
 symbol   sumPrice        startTime              endTime
======== ========== ===================== =====================
 XYZ      26         2018-09-17 10:00:01   2018-09-17 10:00:04
 （A是123，c是4）
 XYZ      17         2018-09-17 10:00:05   2018-09-17 10:00:07
 （A是56，c是7）
```

2. `AFTER MATCH SKIP TO NEXT ROW` 相当于滑动窗口

```sql
 symbol   sumPrice        startTime              endTime
======== ========== ===================== =====================
 XYZ      26         2018-09-17 10:00:01   2018-09-17 10:00:04
 （A是123，c是4）
 XYZ      24         2018-09-17 10:00:02   2018-09-17 10:00:05
 （A是234，c是5）
 XYZ      25         2018-09-17 10:00:03   2018-09-17 10:00:06
 （A是345，c是6）
 XYZ      22         2018-09-17 10:00:04   2018-09-17 10:00:07
 （A是456，c是7）
 XYZ      17         2018-09-17 10:00:05   2018-09-17 10:00:07
 （A是56，c是7）
```

3. `AFTER MATCH SKIP TO LAST A`

```sql
 symbol   sumPrice        startTime              endTime
======== ========== ===================== =====================
 XYZ      26         2018-09-17 10:00:01   2018-09-17 10:00:04
 （A是123，c是4）
 XYZ      25         2018-09-17 10:00:03   2018-09-17 10:00:06
 （A是345，c是6）
 XYZ      17         2018-09-17 10:00:05   2018-09-17 10:00:07
 （A是56，c是7）
```

4. `AFTER MATCH SKIP TO FIRST A`
   
这种组合将产生一个运行时异常，因为人们总是试图在上一个开始的地方开始一个新的匹配。这将产生一个无限循环，因此是禁止的。

必须记住，在 SKIP TO FIRST/LAST variable 策略的场景下，可能没有映射到该变量的行（例如，对于模式 A*）。在这种情况下，将抛出一个运行时异常，因为标准要求一个有效的行来继续匹配。

## MEASURES 

- 定义子句的输出；类似于 SELECT 子句。

```sql
MEASURES
    START_ROW.rowtime AS start_tstamp,
    LAST(PRICE_DOWN.rowtime) AS bottom_tstamp,
    LAST(PRICE_UP.rowtime) AS end_tstamp
```

```SQL
MEASURES
    FIRST(A.rowtime) AS start_tstamp,
    LAST(A.rowtime) AS end_tstamp,
    AVG(A.price) AS avgPrice
```

```SQL
MEASURES
    C.rowtime AS dropTime,
    A.price - C.price AS dropDiff
```

```SQL
MEASURES
    SUM(A.price) AS sumPrice,
    FIRST(rowtime) AS startTime,
    LAST(rowtime) AS endTime
```

注意：

Aggregation 可以应用于`表达式`，但前提是它们引用`单个模式变量`。因此，SUM(`A`.price * `A`.tax) 是有效的，而 AVG(`A`.price * `B`.tax) 则是无效的。

## ALL ROWS PER MATCH + ONE ROW PER MATCH

输出行数由 ONE ROW PER MATCH 输出方式定义。

唯一支持的输出方式是 ONE ROW PER MATCH，它将始终为每个找到的匹配项生成一个输出摘要行。