## RDD编程 vs SparkSQL编程

[参考链接](https://mp.weixin.qq.com/s/QIvWpLbz-ZDjB4X9clh9Ig)

不同于`RDD编程`的`命令式编程范式`，`SparkSQL编程`是一种`声明式编程范式`

我们使用`pyspark`进行`RDD编程`时，在`Excutor`上跑的很多时候就是`Python代码`，当然，少数时候也会跑`java字节码`。

但我们使用`pyspark`进行`SparkSQL编程`时，在`Excutor`上跑的全部是`java字节码`，pyspark在`Driver端`就将相应的`Python代码`转换成了`java任务`然后放到`Excutor`上执行。

因此，使用`SparkSQL的编程范式`进行编程，我们能够取得几乎和直接使用scala/java进行编程相当的效率(忽略语法解析时间差异)。

此外SparkSQL提供了非常方便的`数据读写API`，我们可以用它和`Hive表，HDFS，mysql表，Cassandra，Hbase`等各种存储媒介进行数据交换。

美中不足的是，`SparkSQL`的灵活性会稍差一些，其默认支持的数据类型通常只有 `Int,Long,Float,Double,String,Boolean` 等这些标准SQL数据类型, `类型扩展`相对繁琐。对于一些较为SQL中不直接支持的功能，通常可以借助于`用户自定义函数(UDF)`来实现，如果功能更加复杂，则可以转成RDD来进行实现。

```py
import findspark

#指定spark_home为刚才的解压路径,指定python路径
spark_home = "/Users/liangyun/ProgramFiles/spark-3.0.1-bin-hadoop3.2"
python_path = "/Users/liangyun/anaconda3/bin/python"
findspark.init(spark_home,python_path)

import pyspark 
from pyspark.sql import SparkSession

#SparkSQL的许多功能封装在SparkSession的方法接口中

spark = SparkSession.builder \
        .appName("test") \
        .config("master","local[4]") \
        .enableHiveSupport() \
        .getOrCreate()

sc = spark.sparkContext
```

可以通过Spark-shell来操作Spark SQL，

`spark`作为SparkSession的变量名，

`sc`作为SparkContext的变量名

## RDD，DataFrame和DataSet对比

Spark的发展会逐步将DataSet`作为主要的数据抽象`，弱化RDD和DataFrame

RDD(Spark1.0) -> DataFrame(Spark1.3) -> DataSet(Spark1.6)

DataFrame就是RDD+Schema，可以认为是一张`二维表格`，劣势在于编译器不进行表格中的字段的`类型检查`，在运行期进行检查

DataFrame和DataSet都有可控的`内存管理机制`，所有数据都保存在非堆上，都使用了`catalyst进行SQL的优化`。



不同点：

1. (重要) `DataFrame`参照了Pandas的思想，在RDD基础上增加了`schma`，能够获取`列名`信息。

2. `DataSet`在DataFrame基础上进一步增加了`数据类型信息`，可以在编译时发现`类型错误`。

3. (重要) DataSet只有`Scala语言和Java语言接口`中才支持，在`Python和R语言接口`只支持DataFrame。

4. (重要) DataFrame数据结构本质上是通过RDD来实现的，但是RDD是一种`行存储`的数据结构，而DataFrame是一种`列存储`的数据结构。

相同点：

1. `DataFrame`可以看成`DataSet[Row]`，两者的`API接口`完全相同。

2. DataFrame和DataSet都支持`SQL交互式查询`，可以和 `Hive无缝衔接`。

DataFrame->RDD

```scala
dataFrame.rdd
```

## 创建DataSet

RDD->DataSet

```scala
rdd.map(para=> Person(para(0).trim(),para(1).trim().toInt)).toDS
```

DataFrame -> DataSet

```scala
dataFrame.to[Person]
```

## DataSet -> DataFrame -> RDD

DataSet->RDD

```scala
dataSet.rdd
```

DataSet -> DataFrame

```scala
dataSet.toDF
```

## Spark SQL的数据源

### 1、输入

对于Spark SQL的输入需要使用:

```scala
sparkSession.read
```

> 通用模式:

```scala
sparkSession.read.format("json").load("path")
```

支持类型：parquet、json、text、csv、orc、jdbc

> 专业模式: 直接指定类型。

```scala
sparkSession.read.json
```
```scala
sparkSession.read.csv
```


### 2、输出

对于Spark SQL的输出需要使用

```scala
sparkSession.write
```

> 通用模式:

```scala
dataFrame.write.format("json").save("path")
```

支持类型：parquet、json、text、csv、orc

> 专业模式： 直接指定类型

```scala
dataFrame.write.csv("path") 
```

如果你使用通用模式，spark默认parquet是默认格式：

- `sparkSession.read.load` 加载的默认是parquet格式

- `dataFrame.write.save` 也是默认保存成parquet格式。

如果需要保存成一个text文件，那么需要dataFrame里面只有一列（只需要一列即可）

## 创建DataFrame

### 通过toDF方法转换成DataFrame (普通方式)

```scala
//scala
rdd.map(para(para(0).trim(),para(1).trim().toInt)).toDF("name","age")
```

```scala
// 通过反射设置schema，数据集是spark自带的people.txt,路径在下面的代码中 -scala
case class Person(name:String,age:Int)
val peopleDF=spark.sparkContext.textFile("file:///root/spark/spark2.4.1/examples/src/main/resources/people.txt").map(_.split(",")).map(para=>Person(para(0).trim,para(1).trim.toInt)).toDF
peopleDF.show
```

```s
+---------+---+
|     name|age|
+---------+---+
|  Micheal| 29| 
|     Andy| 30|
|   Justin| 19| 
+---------+---+
```

```scala
// 注册成一张临时表 - scala
peopleDF.createOrReplaceTempView("persons")
val teen=spark.sql("select name,age from persons where age between 13 and 29")
teen.show
```

```s
+---------+---+
|     name|age|
+---------+---+
|  Micheal| 29| 
|   Justin| 19| 
+---------+---+
```

这时teen是一张表，每一行是一个row对象，如果需要访问Row对象中的每一个元素，可以通过下标 `row(0)`；你也可以通过列名 `row.getAs[String]（"name"）`

```scala
teen.map(row=>"name:" + row(0)).show
```

```s
+--------------+
|         value|
+--------------+
|  name:Micheal| 
|   name:Justin| 
+--------------+
```

也可以使用`getAs`方法：

```scala
teen.map(row=>"name:" + row.getAs[String]("name")).show
```

```s
+--------------+
|         value|
+--------------+
|  name:Micheal| 
|   name:Justin| 
+--------------+
```

```py
#将RDD转换成DataFrame
rdd = sc.parallelize([("LiLei",15,88),("HanMeiMei",16,90),("DaChui",17,60)])
df = rdd.toDF(["name","age","score"])
df.show()
df.printSchema()
```

```s
+---------+---+-----+
|     name|age|score|
+---------+---+-----+
|    LiLei| 15|   88|
|HanMeiMei| 16|   90|
|   DaChui| 17|   60|
+---------+---+-----+

root
 |-- name: string (nullable = true)
 |-- age: long (nullable = true)
 |-- score: long (nullable = true)
```

### 通过createDataFrame方法将Pandas.DataFrame转换成pyspark中的DataFrame

```py
import pandas as pd 

pdf = pd.DataFrame([("LiLei",18),("HanMeiMei",17)],columns = ["name","age"])
df = spark.createDataFrame(pdf)
df.show()
```

```s
 +---------+---+
|     name|age|
+---------+---+
|    LiLei| 18|
|HanMeiMei| 17|
+---------+---+
```

```py
# 也可以对列表直接转换
values = [("LiLei",18),("HanMeiMei",17)]
df = spark.createDataFrame(values,["name","age"])
df.show() 
```

```s
 +---------+---+
|     name|age|
+---------+---+
|    LiLei| 18|
|HanMeiMei| 17|
+---------+---+
```

### 通过createDataFrame方法指定schema动态创建DataFrame

通过编程的方式来设置schema，适用于编译器不能确定列的情况

```scala
// scala
val peopleRDD=spark.sparkContext.textFile("file:///root/spark/spark2.4.1/examples/src/main/resources/people.txt")
val schemaString="name age"
val filed=schemaString.split(" ").map(filename=> org.apache.spark.sql.types.StructField(filename,org.apache.spark.sql.types.StringType,nullable = true))
val schema=org.apache.spark.sql.types.StructType(filed)
peopleRDD.map(_.split(",")).map(para=>org.apache.spark.sql.Row(para(0).trim,para(1).trim))
val peopleDF=spark.createDataFrame(res6,schema)
peopleDF.show
```

```s
+---------+---+
|     name|age|
+---------+---+
|  Micheal| 29| 
|     Andy| 30|
|   Justin| 19| 
+---------+---+
```

这种方法比较繁琐，但是可以在预先不知道schema和数据类型的情况下在代码中动态创建DataFrame.

```py
from pyspark.sql.types import *
from pyspark.sql import Row
from datetime import datetime

schema = StructType([StructField("name", StringType(), nullable = False),
                     StructField("score", IntegerType(), nullable = True),
                     StructField("birthday", DateType(), nullable = True)])

rdd = sc.parallelize([Row("LiLei",87,datetime(2010,1,5)),
                      Row("HanMeiMei",90,datetime(2009,3,1)),
                      Row("DaChui",None,datetime(2008,7,2))])

dfstudent = spark.createDataFrame(rdd, schema)

dfstudent.show()
```

```s
 +---------+-----+----------+
|     name|score|  birthday|
+---------+-----+----------+
|    LiLei|   87|2010-01-05|
|HanMeiMei|   90|2009-03-01|
|   DaChui| null|2008-07-02|
+---------+-----+----------+

```

### 通过读取文件创建

可以读取json文件，csv文件，hive数据表或者mysql数据表得到DataFrame。

#### 读取json文件

```py
#读取json文件生成DataFrame
df = spark.read.json("data/people.json")
df.show()
```

```s
+----+-------+
| age|   name|
+----+-------+
|null|Michael|
|  30|   Andy|
|  19| Justin|
+----+-------+
```

#### 读取csv文件

```py
#读取csv文件
df = spark.read.option("header","true") \
 .option("inferSchema","true") \
 .option("delimiter", ",") \
 .csv("data/iris.csv")
df.show(5)
df.printSchema()
```

```s
+-----------+----------+-----------+----------+-----+
|sepallength|sepalwidth|petallength|petalwidth|label|
+-----------+----------+-----------+----------+-----+
|        5.1|       3.5|        1.4|       0.2|    0|
|        4.9|       3.0|        1.4|       0.2|    0|
|        4.7|       3.2|        1.3|       0.2|    0|
|        4.6|       3.1|        1.5|       0.2|    0|
|        5.0|       3.6|        1.4|       0.2|    0|
+-----------+----------+-----------+----------+-----+
only showing top 5 rows

root
 |-- sepallength: double (nullable = true)
 |-- sepalwidth: double (nullable = true)
 |-- petallength: double (nullable = true)
 |-- petalwidth: double (nullable = true)
 |-- label: integer (nullable = true)
```

```py
#读取csv文件
df = spark.read.format("com.databricks.spark.csv") \
 .option("header","true") \
 .option("inferSchema","true") \
 .option("delimiter", ",") \
 .load("data/iris.csv")
df.show(5)
df.printSchema()
```

```s
+-----------+----------+-----------+----------+-----+
|sepallength|sepalwidth|petallength|petalwidth|label|
+-----------+----------+-----------+----------+-----+
|        5.1|       3.5|        1.4|       0.2|    0|
|        4.9|       3.0|        1.4|       0.2|    0|
|        4.7|       3.2|        1.3|       0.2|    0|
|        4.6|       3.1|        1.5|       0.2|    0|
|        5.0|       3.6|        1.4|       0.2|    0|
+-----------+----------+-----------+----------+-----+
only showing top 5 rows

root
 |-- sepallength: double (nullable = true)
 |-- sepalwidth: double (nullable = true)
 |-- petallength: double (nullable = true)
 |-- petalwidth: double (nullable = true)
 |-- label: integer (nullable = true)
```

#### 读取parquet文件

```py
#读取parquet文件
df = spark.read.parquet("data/users.parquet")
df.show()
```

```s
+------+--------------+----------------+
|  name|favorite_color|favorite_numbers|
+------+--------------+----------------+
|Alyssa|          null|  [3, 9, 15, 20]|
|   Ben|           red|              []|
+------+--------------+----------------+
```

#### 读取hive数据表

```py
#读取hive数据表生成DataFrame

spark.sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING) USING hive")
spark.sql("LOAD DATA LOCAL INPATH 'data/kv1.txt' INTO TABLE src")
df = spark.sql("SELECT key, value FROM src WHERE key < 10 ORDER BY key")
df.show(5)

```

```s
+---+-----+
|key|value|
+---+-----+
|  0|val_0|
|  0|val_0|
|  0|val_0|
|  0|val_0|
|  0|val_0|
+---+-----+
only showing top 5 rows
```

#### 读取mysql数据表

```py
#读取mysql数据表生成DataFrame
url = "jdbc:mysql://localhost:3306/test"
df = spark.read.format("jdbc") \
 .option("url", url) \
 .option("dbtable", "runoob_tbl") \
 .option("user", "root") \
 .option("password", "0845") \
 .load()\
df.show()
```


## DataFrame保存成文件

可以保存成csv文件，json文件，parquet文件或者保存成hive数据表

```py
 #保存成csv文件
df = spark.read.format("json").load("data/people.json")
df.write.format("csv").option("header","true").save("data/people_write.csv")
```

```py
 #先转换成rdd再保存成txt文件
df.rdd.saveAsTextFile("data/people_rdd.txt")
```

```py
 #保存成json文件
df.write.json("data/people_write.json")
```

```py
 #保存成parquet文件, 压缩格式, 占用存储小, 且是spark内存中存储格式，加载最快
df.write.partitionBy("age").format("parquet").save("data/namesAndAges.parquet")
df.write.parquet("data/people_write.parquet")
```

```py
 #保存成hive数据表
df.write.bucketBy(42, "name").sortBy("age").saveAsTable("people_bucketed")
```

## DataFrame的API交互

```py
from pyspark.sql import Row
from pyspark.sql.functions import * 

df = spark.createDataFrame(
    [("LiLei",15,"male"),
     ("HanMeiMei",16,"female"),
     ("DaChui",17,"male")]).toDF("name","age","gender")

df.show()
df.printSchema()

```

```s
+---------+---+------+
|     name|age|gender|
+---------+---+------+
|    LiLei| 15|  male|
|HanMeiMei| 16|female|
|   DaChui| 17|  male|
+---------+---+------+

root
 |-- name: string (nullable = true)
 |-- age: long (nullable = true)
 |-- gender: string (nullable = true)
```

### Action操作

DataFrame的Action操作包括show,count,collect,,describe,take,head,first等操作。

```py
 #show
df.show()
```

```s
+---------+---+------+
|     name|age|gender|
+---------+---+------+
|    LiLei| 15|  male|
|HanMeiMei| 16|female|
|   DaChui| 17|  male|
+---------+---+------+
```

```py
 #show(numRows: Int, truncate: Boolean) 
#第二个参数设置是否当输出字段长度超过20时进行截取
df.show(2,False) 
```

```s
+---------+---+------+
|name     |age|gender|
+---------+---+------+
|LiLei    |15 |male  |
|HanMeiMei|16 |female|
+---------+---+------+
only showing top 2 rows
```

```py
 #count
df.count()
```

```s
3
```

```py
#collect
df.collect()
```

```s
[Row(name='LiLei', age=15, gender='male'),
 Row(name='HanMeiMei', age=16, gender='female'),
 Row(name='DaChui', age=17, gender='male')]
```

```py
#first
df.first()
```

```s
Row(name='LiLei', age=15, gender='male')
```

```py
 #take
df.take(2)
```

```s
[Row(name='LiLei', age=15, gender='male'),
 Row(name='HanMeiMei', age=16, gender='female')]
```

```py
#head
df.head(2)
```

```s
[Row(name='LiLei', age=15, gender='male'),
 Row(name='HanMeiMei', age=16, gender='female')]
```

### 类RDD操作

DataFrame支持RDD中一些诸如distinct,cache,sample,foreach,intersect,except等操作。

可以把DataFrame当做数据类型为Row的RDD来进行操作，必要时可以将其转换成RDD来操作。

```py
df = spark.createDataFrame([("Hello World",),("Hello China",),("Hello Spark",)]).toDF("value")
df.show()
```

```s
+-----------+
|      value|
+-----------+
|Hello World|
|Hello China|
|Hello Spark|
+-----------+
```

```py
#map操作，需要先转换成rdd
rdd = df.rdd.map(lambda x:Row(x[0].upper()))
dfmap = rdd.toDF(["value"]).show()
```

```s
+-----------+
|      value|
+-----------+
|HELLO WORLD|
|HELLO CHINA|
|HELLO SPARK|
+-----------+
```

```py
#flatMap，需要先转换成rdd
df_flat = df.rdd.flatMap(lambda x:x[0].split(" ")).map(lambda x:Row(x)).toDF(["value"])
df_flat.show()
```

```s
+-----+
|value|
+-----+
|Hello|
|World|
|Hello|
|China|
|Hello|
|Spark|
+-----+
```

```py
#filter过滤
df_filter = df.rdd.filter(lambda s:s[0].endswith("Spark")).toDF(["value"])

df_filter.show()
```

```s
+-----------+
|      value|
+-----------+
|Hello Spark|
+-----------+
```

```py
# filter和broadcast混合使用
broads = sc.broadcast(["Hello","World"])

df_filter_broad = df_flat.filter(~col("value").isin(broads.value))

df_filter_broad.show() 
```

```s
+-----+
|value|
+-----+
|China|
|Spark|
+-----+
```

```py
#distinct
df_distinct = df_flat.distinct()
df_distinct.show() 
```

```s
+-----+
|value|
+-----+
|World|
|China|
|Hello|
|Spark|
+-----+
```

```py
#cache缓存
df.cache()
df.unpersist()
```

```py
 #sample抽样
dfsample = df.sample(False,0.6,0)
dfsample.show()  
```

```s
+-----------+
|      value|
+-----------+
|Hello China|
|Hello Spark|
+-----------+
```

```py
df2 = spark.createDataFrame([["Hello World"],["Hello Scala"],["Hello Spark"]]).toDF("value")
df2.show()
```

```s
+-----------+
|      value|
+-----------+
|Hello World|
|Hello Scala|
|Hello Spark|
+-----------+
```

```py
#intersect交集
dfintersect = df.intersect(df2)
dfintersect.show()
```

```s
+-----------+
|      value|
+-----------+
|Hello Spark|
|Hello World|
+-----------+
```

```py
#exceptAll补集

dfexcept = df.exceptAll(df2)
dfexcept.show()
```

```s
+-----------+
|      value|
+-----------+
|Hello China|
+-----------+
```

### 类Excel操作

可以对DataFrame进行增加列，删除列，重命名列，排序等操作，去除重复行，去除空行，就跟操作Excel表格一样。

```py
df = spark.createDataFrame([
("LiLei",15,"male"),
("HanMeiMei",16,"female"),
("DaChui",17,"male"),
("RuHua",16,None)
]).toDF("name","age","gender")

df.show()
df.printSchema()
```

```s
+---------+---+------+
|     name|age|gender|
+---------+---+------+
|    LiLei| 15|  male|
|HanMeiMei| 16|female|
|   DaChui| 17|  male|
|    RuHua| 16|  null|
+---------+---+------+

root
 |-- name: string (nullable = true)
 |-- age: long (nullable = true)
 |-- gender: string (nullable = true)
```

```py
#增加列
dfnew = df.withColumn("birthyear",-df["age"]+2020)

dfnew.show() 
```

```s
+---------+---+------+---------+
|     name|age|gender|birthyear|
+---------+---+------+---------+
|    LiLei| 15|  male|     2005|
|HanMeiMei| 16|female|     2004|
|   DaChui| 17|  male|     2003|
|    RuHua| 16|  null|     2004|
+---------+---+------+---------+
```

```py
#置换列的顺序
dfupdate = dfnew.select("name","age","birthyear","gender")
dfupdate.show()
```

```py
#删除列
dfdrop = df.drop("gender")
dfdrop.show() 
```

```s
+---------+---+
|     name|age|
+---------+---+
|    LiLei| 15|
|HanMeiMei| 16|
|   DaChui| 17|
|    RuHua| 16|
+---------+---+
```

```py
#重命名列
dfrename = df.withColumnRenamed("gender","sex")
dfrename.show() 
```

```s
+---------+---+------+
|     name|age|   sex|
+---------+---+------+
|    LiLei| 15|  male|
|HanMeiMei| 16|female|
|   DaChui| 17|  male|
|    RuHua| 16|  null|
+---------+---+------+
```

```py
#排序sort，可以指定升序降序
dfsorted = df.sort(df["age"].desc())
dfsorted.show()
```

```s
+---------+---+------+
|     name|age|gender|
+---------+---+------+
|   DaChui| 17|  male|
|    RuHua| 16|  null|
|HanMeiMei| 16|female|
|    LiLei| 15|  male|
+---------+---+------+
```

```py
#排序orderby,默认为升序,可以根据多个字段
dfordered = df.orderBy(df["age"].desc(),df["gender"].desc())
dfordered.show()
```

```s
+---------+---+------+
|     name|age|gender|
+---------+---+------+
|   DaChui| 17|  male|
|HanMeiMei| 16|female|
|    RuHua| 16|  null|
|    LiLei| 15|  male|
+---------+---+------+
```

```py
#去除nan值行
dfnotnan = df.na.drop()
dfnotnan.show()
```

```s
+---------+---+------+
|     name|age|gender|
+---------+---+------+
|    LiLei| 15|  male|
|HanMeiMei| 16|female|
|   DaChui| 17|  male|
+---------+---+------+
```

```py
#填充nan值
df_fill = df.na.fill("female")
df_fill.show()
```

```s
+---------+---+------+
|     name|age|gender|
+---------+---+------+
|    LiLei| 15|  male|
|HanMeiMei| 16|female|
|   DaChui| 17|  male|
|    RuHua| 16|female|
+---------+---+------+
```

```py
#替换某些值
df_replace = df.na.replace({"":"female","RuHua":"SiYu"})
df_replace.show()
```

```s
+---------+---+------+
|     name|age|gender|
+---------+---+------+
|    LiLei| 15|  male|
|HanMeiMei| 16|female|
|   DaChui| 17|  male|
|     SiYu| 16|  null|
+---------+---+------+
```

```py
#去重，默认根据全部字段
df2 = df.unionAll(df)
df2.show()
dfunique = df2.dropDuplicates()
dfunique.show()
```

```s
+---------+---+------+
|     name|age|gender|
+---------+---+------+
|    LiLei| 15|  male|
|HanMeiMei| 16|female|
|   DaChui| 17|  male|
|    RuHua| 16|  null|
|    LiLei| 15|  male|
|HanMeiMei| 16|female|
|   DaChui| 17|  male|
|    RuHua| 16|  null|
+---------+---+------+

+---------+---+------+
|     name|age|gender|
+---------+---+------+
|    RuHua| 16|  null|
|   DaChui| 17|  male|
|HanMeiMei| 16|female|
|    LiLei| 15|  male|
+---------+---+------+
```

```py
#去重,根据部分字段
dfunique_part = df.dropDuplicates(["age"])
dfunique_part.show()
```

```s
+---------+---+------+
|     name|age|gender|
+---------+---+------+
|   DaChui| 17|  male|
|    LiLei| 15|  male|
|HanMeiMei| 16|female|
+---------+---+------+

```

```py
#简单聚合操作
dfagg = df.agg({"name":"count","age":"max"})
dfagg.show()
```

```s

+-----------+--------+
|count(name)|max(age)|
+-----------+--------+
|          4|      17|
+-----------+--------+

```

```py
#汇总信息
df_desc = df.describe()
df_desc.show()
```

```s
+-------+------+-----------------+------+
|summary|  name|              age|gender|
+-------+------+-----------------+------+
|  count|     4|                4|     3|
|   mean|  null|             16.0|  null|
| stddev|  null|0.816496580927726|  null|
|    min|DaChui|               15|female|
|    max| RuHua|               17|  male|
+-------+------+-----------------+------+
```

```py
#频率超过0.5的年龄和性别
df_freq = df.stat.freqItems(("age","gender"),0.5)
df_freq.show()
```

```s
+-------------+----------------+
|age_freqItems|gender_freqItems|
+-------------+----------------+
|         [16]|          [male]|
+-------------+----------------+
```

### 类SQL表操作

类SQL表操作主要包括

- 表查询(select,selectExpr,where),

- 表连接(join,union,unionAll),

- 表分组(groupby,agg,pivot)

等操作。

```py
df = spark.createDataFrame([
("LiLei",15,"male"),
("HanMeiMei",16,"female"),
("DaChui",17,"male"),
("RuHua",16,None)]).toDF("name","age","gender")

df.show()
```

```s
+---------+---+------+
|     name|age|gender|
+---------+---+------+
|    LiLei| 15|  male|
|HanMeiMei| 16|female|
|   DaChui| 17|  male|
|    RuHua| 16|  null|
+---------+---+------+
```

#### 表查询(select,selectExpr,where)

```py
#表查询select
dftest = df.select("name").limit(2)
dftest.show()
```

```s
+---------+
|     name|
+---------+
|    LiLei|
|HanMeiMei|
+---------+
```

```py
dftest = df.select("name",df["age"] + 1)
dftest.show()
```

```s
+---------+---------+
|     name|(age + 1)|
+---------+---------+
|    LiLei|       16|
|HanMeiMei|       17|
|   DaChui|       18|
|    RuHua|       17|
+---------+---------+
```

```py
#表查询select
dftest = df.select("name",-df["age"]+2020).toDF("name","birth_year")
dftest.show()
```

```s
+---------+----------+
|     name|birth_year|
+---------+----------+
|    LiLei|      2005|
|HanMeiMei|      2004|
|   DaChui|      2003|
|    RuHua|      2004|
+---------+----------+
```

```py
#表查询selectExpr,可以使用UDF函数，指定别名等
import datetime
spark.udf.register("getBirthYear",lambda age:datetime.datetime.now().year-age)
dftest = df.selectExpr("name", "getBirthYear(age) as birth_year" , "UPPER(gender) as gender" )
dftest.show()
```

```s
+---------+----------+------+
|     name|birth_year|gender|
+---------+----------+------+
|    LiLei|      2005|  MALE|
|HanMeiMei|      2004|FEMALE|
|   DaChui|      2003|  MALE|
|    RuHua|      2004|  null|
+---------+----------+------+
```

```py
#表查询where, 指定SQL中的where字句表达式
dftest = df.where("gender='male' and age>15")
dftest.show()
```

```s
+------+---+------+
|  name|age|gender|
+------+---+------+
|DaChui| 17|  male|
+------+---+------+
```

```py
#表查询filter
dftest = df.filter(df["age"]>16)
dftest.show()
```

```s
+------+---+------+
|  name|age|gender|
+------+---+------+
|DaChui| 17|  male|
+------+---+------+
```

```py
#表查询filter
dftest = df.filter("gender ='male'")
dftest.show()
```

```s
+------+---+------+
|  name|age|gender|
+------+---+------+
| LiLei| 15|  male|
|DaChui| 17|  male|
+------+---+------+
```

#### 表连接(join,union,unionAll)

```py
#表连接join
dfscore = spark.createDataFrame([("LiLei","male",88),("HanMeiMei","female",90),("DaChui","male",50)]) \
          .toDF("name","gender","score") 

dfscore.show()
```

```s
+---------+------+-----+
|     name|gender|score|
+---------+------+-----+
|    LiLei|  male|   88|
|HanMeiMei|female|   90|
|   DaChui|  male|   50|
+---------+------+-----+
```

```py
#表连接join,根据单个字段
dfjoin = df.join(dfscore.select("name","score"),"name")
dfjoin.show()
```

```s
+---------+---+------+-----+
|     name|age|gender|score|
+---------+---+------+-----+
|    LiLei| 15|  male|   88|
|HanMeiMei| 16|female|   90|
|   DaChui| 17|  male|   50|
+---------+---+------+-----+
```

```py
#表连接join,根据多个字段
dfjoin = df.join(dfscore,["name","gender"])
dfjoin.show()
```

```s
+---------+------+---+-----+
|     name|gender|age|score|
+---------+------+---+-----+
|HanMeiMei|female| 16|   90|
|   DaChui|  male| 17|   50|
|    LiLei|  male| 15|   88|
+---------+------+---+-----+
```

```py
#表连接join,根据多个字段
#可以指定连接方式为"inner","left","right","outer","semi","full","leftanti","anti"等多种方式
dfjoin = df.join(dfscore,["name","gender"],"right")
dfjoin.show()
```

```s
+---------+------+---+-----+
|     name|gender|age|score|
+---------+------+---+-----+
|HanMeiMei|female| 16|   90|
|   DaChui|  male| 17|   50|
|    LiLei|  male| 15|   88|
+---------+------+---+-----+
```

```py
dfjoin = df.join(dfscore,["name","gender"],"outer")
dfjoin.show()
```

```s
+---------+------+---+-----+
|     name|gender|age|score|
+---------+------+---+-----+
|HanMeiMei|female| 16|   90|
|   DaChui|  male| 17|   50|
|    LiLei|  male| 15|   88|
|    RuHua|  null| 16| null|
+---------+------+---+-----+
```

```py
#表连接，灵活指定连接关系
dfmark = dfscore.withColumnRenamed("gender","sex")
dfmark.show()
```

```s
+---------+------+-----+
|     name|   sex|score|
+---------+------+-----+
|    LiLei|  male|   88|
|HanMeiMei|female|   90|
|   DaChui|  male|   50|
+---------+------+-----+
```

```py
dfjoin = df.join(dfmark,(df["name"] == dfmark["name"]) & (df["gender"]==dfmark["sex"]),
        "inner")
dfjoin.show()
```

```s
+---------+---+------+---------+------+-----+
|     name|age|gender|     name|   sex|score|
+---------+---+------+---------+------+-----+
|HanMeiMei| 16|female|HanMeiMei|female|   90|
|   DaChui| 17|  male|   DaChui|  male|   50|
|    LiLei| 15|  male|    LiLei|  male|   88|
+---------+---+------+---------+------+-----+
```


```py
#表合并union
dfstudent = spark.createDataFrame([("Jim",18,"male"),("Lily",16,"female")]).toDF("name","age","gender")
dfstudent.show()
```

```s
+----+---+------+
|name|age|gender|
+----+---+------+
| Jim| 18|  male|
|Lily| 16|female|
+----+---+------+
```

```py
dfunion = df.union(dfstudent)
dfunion.show()
```

```s
+---------+---+------+
|     name|age|gender|
+---------+---+------+
|    LiLei| 15|  male|
|HanMeiMei| 16|female|
|   DaChui| 17|  male|
|    RuHua| 16|  null|
|      Jim| 18|  male|
|     Lily| 16|female|
+---------+---+------+
```

#### 表分组(groupby,agg,pivot)

```py
#表分组 groupBy
from pyspark.sql import functions as F 
dfgroup = df.groupBy("gender").max("age")
dfgroup.show()
```

```s
+------+--------+
|gender|max(age)|
+------+--------+
|  null|      16|
|female|      16|
|  male|      17|
+------+--------+
```

```py
#表分组后聚合，groupBy,agg
dfagg = df.groupBy("gender").agg(F.mean("age").alias("mean_age"),
   F.collect_list("name").alias("names"))
dfagg.show()
```

```s
+------+--------+---------------+
|gender|mean_age|          names|
+------+--------+---------------+
|  null|    16.0|        [RuHua]|
|female|    16.0|    [HanMeiMei]|
|  male|    16.0|[LiLei, DaChui]|
+------+--------+---------------+

```

```py
#表分组聚合，groupBy,agg
dfagg = df.groupBy("gender").agg(F.expr("avg(age)"),F.expr("collect_list(name)"))
dfagg.show()
```

```s
+------+--------+------------------+
|gender|avg(age)|collect_list(name)|
+------+--------+------------------+
|  null|    16.0|           [RuHua]|
|female|    16.0|       [HanMeiMei]|
|  male|    16.0|   [LiLei, DaChui]|
+------+--------+------------------+
```

```py
#表分组聚合，groupBy,agg
df.groupBy("gender","age").agg(F.collect_list(col("name"))).show()
```

```s
+------+---+------------------+
|gender|age|collect_list(name)|
+------+---+------------------+
|  male| 15|           [LiLei]|
|  male| 17|          [DaChui]|
|female| 16|       [HanMeiMei]|
|  null| 16|           [RuHua]|
+------+---+------------------+
```

```py
#表分组后透视，groupBy,pivot
dfstudent = spark.createDataFrame([("LiLei",18,"male",1),("HanMeiMei",16,"female",1),
                    ("Jim",17,"male",2),("DaChui",20,"male",2)]).toDF("name","age","gender","class")
dfstudent.show()
dfstudent.groupBy("class").pivot("gender").max("age").show()
```

```s
+---------+---+------+-----+
|     name|age|gender|class|
+---------+---+------+-----+
|    LiLei| 18|  male|    1|
|HanMeiMei| 16|female|    1|
|      Jim| 17|  male|    2|
|   DaChui| 20|  male|    2|
+---------+---+------+-----+

+-----+------+----+
|class|female|male|
+-----+------+----+
|    1|    16|  18|
|    2|  null|  20|
+-----+------+----+
```

```py
#窗口函数
df = spark.createDataFrame([("LiLei",78,"class1"),("HanMeiMei",87,"class1"),
                           ("DaChui",65,"class2"),("RuHua",55,"class2")]) \
    .toDF("name","score","class")

df.show()
dforder = df.selectExpr("name","score","class",
         "row_number() over (partition by class order by score desc) as order")

dforder.show()
```

```s
+---------+-----+------+
|     name|score| class|
+---------+-----+------+
|    LiLei|   78|class1|
|HanMeiMei|   87|class1|
|   DaChui|   65|class2|
|    RuHua|   55|class2|
+---------+-----+------+

+---------+-----+------+-----+
|     name|score| class|order|
+---------+-----+------+-----+
|   DaChui|   65|class2|    1|
|    RuHua|   55|class2|    2|
|HanMeiMei|   87|class1|    1|
|    LiLei|   78|class1|    2|
+---------+-----+------+-----+
```

## DataFrame的SQL交互

将DataFrame注册为临时表视图或者全局表视图后，可以使用sql语句对DataFrame进行交互。

不仅如此，还可以通过SparkSQL对Hive表直接进行增删改查等操作。

### 注册视图后进行SQL交互

```py
#注册为临时表视图, 其生命周期和SparkSession相关联
df = spark.createDataFrame([("LiLei",18,"male"),("HanMeiMei",17,"female"),("Jim",16,"male")],
                              ("name","age","gender"))

df.show()
df.createOrReplaceTempView("student")
dfmale = spark.sql("select * from student where gender='male'")
dfmale.show()
```

```s
+---------+---+------+
|     name|age|gender|
+---------+---+------+
|    LiLei| 18|  male|
|HanMeiMei| 17|female|
|      Jim| 16|  male|
+---------+---+------+

+-----+---+------+
| name|age|gender|
+-----+---+------+
|LiLei| 18|  male|
|  Jim| 16|  male|
+-----+---+------+
```

```py
#注册为全局临时表视图,其生命周期和整个Spark应用程序关联

df.createOrReplaceGlobalTempView("student")
query = """
 select t.gender
 , collect_list(t.name) as names 
 from global_temp.student t 
 group by t.gender
""".strip("\n")

spark.sql(query).show()
#可以在新的Session中访问
spark.newSession().sql("select * from global_temp.student").show()
```

```s
+------+------------+
|gender|       names|
+------+------------+
|female| [HanMeiMei]|
|  male|[LiLei, Jim]|
+------+------------+

+---------+---+------+
|     name|age|gender|
+---------+---+------+
|    LiLei| 18|  male|
|HanMeiMei| 17|female|
|      Jim| 16|  male|
+---------+---+------+
```

### 对Hive表进行增删改查操作

```py
#删除hive表

query = "DROP TABLE IF EXISTS students" 
spark.sql(query) 
```

```py
#建立hive分区表
#(注：不可以使用中文字段作为分区字段)

query = """CREATE TABLE IF NOT EXISTS `students`
(`name` STRING COMMENT '姓名',
`age` INT COMMENT '年龄'
)
PARTITIONED BY ( `class` STRING  COMMENT '班级', `gender` STRING  COMMENT '性别')
""".replace("\n"," ")
spark.sql(query) 
```

```py
##动态写入数据到hive分区表
spark.conf.set("hive.exec.dynamic.partition.mode", "nonstrict") #注意此处有一个设置操作
dfstudents = spark.createDataFrame([("LiLei",18,"class1","male"),
                                    ("HanMeimei",17,"class2","female"),
                                    ("DaChui",19,"class2","male"),
                                    ("Lily",17,"class1","female")]).toDF("name","age","class","gender")
dfstudents.show()

#动态写入分区
dfstudents.write.mode("overwrite").format("hive")\
.partitionBy("class","gender").saveAsTable("students")
```

```py
#写入到静态分区
dfstudents = spark.createDataFrame([("Jim",18,"class3","male"),
                                    ("Tom",19,"class3","male")]).toDF("name","age","class","gender")
dfstudents.createOrReplaceTempView("dfclass3")

#INSERT INTO 尾部追加, INSERT OVERWRITE TABLE 覆盖分区
query = """
INSERT OVERWRITE TABLE `students`
PARTITION(class='class3',gender='male') 
SELECT name,age from dfclass3
""".replace("\n"," ")
spark.sql(query)
```

```py
#写入到混合分区
dfstudents = spark.createDataFrame([("David",18,"class4","male"),
                                    ("Amy",17,"class4","female"),
                                    ("Jerry",19,"class4","male"),
                                    ("Ann",17,"class4","female")]).toDF("name","age","class","gender")
dfstudents.createOrReplaceTempView("dfclass4")

query = """
INSERT OVERWRITE TABLE `students`
PARTITION(class='class4',gender) 
SELECT name,age,gender from dfclass4
""".replace("\n"," ")
spark.sql(query)
```

```py
#读取全部数据

dfdata = spark.sql("select * from students")
dfdata.show()
```

```s
+---------+---+------+------+
|     name|age| class|gender|
+---------+---+------+------+
|      Ann| 17|class4|female|
|      Amy| 17|class4|female|
|HanMeimei| 17|class2|female|
|   DaChui| 19|class2|  male|
|    LiLei| 18|class1|  male|
|     Lily| 17|class1|female|
|    Jerry| 19|class4|  male|
|    David| 18|class4|  male|
|      Jim| 18|class3|  male|
|      Tom| 19|class3|  male|
+---------+---+------+------+
```
