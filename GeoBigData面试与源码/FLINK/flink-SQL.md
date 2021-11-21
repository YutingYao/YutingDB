<!-- vscode-markdown-toc -->
* 1. [启动 flink SQL 客户端](#flinkSQL)
* 2. [Flink 内置函数的完整列表](#Flink)
	* 2.1. [举个栗子--时间函数](#--)
		* 2.1.1. [Date and Time](#DateandTime)
		* 2.1.2. [时间戳](#)
		* 2.1.3. [间隔年到月](#-1)
		* 2.1.4. [隔 DAY TO SECOND](#DAYTOSECOND)
		* 2.1.5. [当前系统时间-CURRENT_TIMESTAMP](#-CURRENT_TIMESTAMP)
* 3. [source 表 - 使用 CREATE TABLE 语句](#source-CREATETABLE)
	* 3.1. [举个栗子](#-1)
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
* 10. [动态表 & 连续查询(Continuous Query)](#ContinuousQuery)
* 11. [调整 Flink Table 和 SQL API 程序的配置项](#FlinkTableSQLAPI)
* 12. [数据类型](#-1)

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

### sql 函数

#### 比较函数

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

#### 逻辑函数

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

#### 算术函数

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

#### 字符串函数

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

#### 时间函数

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

#### 条件函数

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

#### 类型转换函数

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

#### 集合函数

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

#### 集合函数

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

#### 值构建函数

```sql
ARRAY ‘[’ value1 [, value2 ]* ‘]’
MAP ‘[’ value1, value2 [, value3, value4 ]* ‘]’
```

#### 值获取函数

```sql
-- 按名称从 Flink 复合类型（例如，Tuple，POJO）返回字段的值。
tableName.compositeType.field
-- 返回 Flink 复合类型（例如，Tuple，POJO）的平面表示，
-- 将其每个直接子类型转换为单独的字段。
-- 在大多数情况下，平面表示 的字段与原始字段的命名类似，
-- 但使用 $ 分隔符（例如 mypojo$mytuple$f0）
tableName.compositeType.*
```

#### 分组函数

```sql
GROUP_ID()

GROUPING(expression1 [, expression2]* ) 
GROUPING_ID(expression1 [, expression2]* )
```

#### 哈希函数

```sql
MD5(string)
SHA1(string)
SHA224(string)
SHA256(string)
SHA384(string)
SHA512(string)
SHA2(string, hashLength)
```

#### 聚合函数

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

#### 时间间隔单位和时间点单位标识符

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

###  2.1. <a name='--'></a>举个栗子--时间函数

####  2.1.1. <a name='DateandTime'></a>Date and Time

Date的数据类型，由年-月-日组成，取值范围为

0000-01-01 ~ 9999 -12-31。

与SQL标准相比，范围从0000年开始。

--------------------------------------------------

不带时区的Time的数据类型，包括小时:分钟:秒[.分数]，

精度可达纳秒，取值范围为:

00:00.000000000到23:59:59.999999999。

####  2.1.2. <a name=''></a>时间戳

不带时区的时间戳的数据类型

* 年-月-日-小时：分钟：秒[.小数]

* year-month-day hour:minute:second[.fractional]

精度高达纳秒

数值范围为：

* 0000-01-01 00:00:00.000000000到9999-12-31 23:59:59.99999999。

与SQL标准相比，不支持闰秒(23:59:60和23:59:61)，语义更接近java.time.LocalDateTime。

####  2.1.3. <a name='-1'></a>间隔年到月

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



####  2.1.4. <a name='DAYTOSECOND'></a>隔 DAY TO SECOND

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



####  2.1.5. <a name='-CURRENT_TIMESTAMP'></a>当前系统时间-CURRENT_TIMESTAMP

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

* 根据指定的表名创建一个表，如果同名表已经在 catalog 中存在了，则无法注册。

  * CREATE TABLE

* 根据给定的表属性创建数据库。若数据库中已存在同名表会抛出异常。

  * CREATE DATABASE

* 根据给定的 query 语句创建一个视图。若数据库中已经存在同名视图会抛出异常.

  * CREATE VIEW

* 创建一个有 catalog 和数据库命名空间的 catalog function ，需要指定一个 identifier ，可指定 language tag。 若 catalog 中，已经有同名的函数注册了，则无法注册。

  * CREATE FUNCTION

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

## executeSql 和 sqlQuery 

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