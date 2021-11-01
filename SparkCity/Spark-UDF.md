## 自定义函数大致可以分为三种

UDF(User-Defined-Function)：

即最基本的自定义函数，类似

```s
to_char
to_date
```

UDAF（User- Defined Aggregation Funcation）：

用户自定义聚合函数，类似在`group by`之后使用的

```s
sum
avg
```

UDTF(User-Defined Table-Generating Functions)：

用户自定义生成函数，有点像`stream`里面的

```s
flatMap
```

## 自定义udf函数

### 通过匿名函数注册UDF

1. 注册一个UDF函数

```scala
spark.udf.register(name,func)
```

`name`是UDF调用时的标识符

`fun`是一个函数，用于处理字段

```scala
spark.udf.register("strLen", (str: String) => str.length())
```

```scala
val squared = (s: Int) => {
  s * s
}
spark.udf.register("square", squared)
```

2. 需要将一个DF或者DS注册为一个临时表

3. 通过spark.sql去运行一个SQL语句，在SQL语句中可以通过name(列名)方式来应用UDF函数

```sql
select name,strLen(name) as name_len from user
```

```sql
spark.range(1, 20).registerTempTable("test")
%sql select id, square(id) as id_squared from test
```

```scala
spark.sql("select name,strLen(name) as name_len from user").show
```

```s
+-----+--------+
| name|name_len|
+-----+--------+
|  Leo|       3|
|Marry|       5|
| Jack|       4|
|  Tom|       3|
+-----+--------+
```

### 通过实名函数注册UDF

实名函数的注册有点不同，要在后面加 _(注意前面有个空格)

```scala
/**
 * 根据年龄大小返回是否成年 成年：true,未成年：false
*/
def isAdult(age: Int) = {
  if (age < 18) {
    false
  } else {
    true
  }

}
```

注册（仅以Spark2.x为例）

```scala
spark.udf.register("isAdult", isAdult _)
```

至于使用都是一样的

### DataFrame的udf方法

DataFrame的udf方法虽然和Spark Sql的名字一样，但是属于不同的类，它在org.apache.spark.sql.functions里，下面是它的用法

#### 注册：

```scala
import org.apache.spark.sql.functions._
//注册自定义函数（通过匿名函数）
val strLen = udf((str: String) => str.length())
//注册自定义函数（通过实名函数）
val udf_isAdult = udf(isAdult _)
```

#### 使用：

可通过`withColumn`和`select`使用

下面的代码已经实现了给`user表`添加`两列`的功能

```scala
//通过withColumn添加列
userDF.withColumn("name_len", strLen(col("name"))).withColumn("isAdult", udf_isAdult(col("age"))).show
//通过select添加列
userDF.select(col("*"), strLen(col("name")) as "name_len", udf_isAdult(col("age")) as "isAdult").show
```

```s
+-----+---+--------+-------+
| name|age|name_len|isAdult|
+-----+---+--------+-------+
|  Leo| 16|       3|  false|
|Marry| 21|       5|   true|
| Jack| 14|       4|  false|
|  Tom| 18|       3|   true|
+-----+---+--------+-------+
```

#### withColumn和select的区别

可通过`withColumn`的源码看出`withColumn`的功能是`实现增加一列`，或者`替换`一个已存在的列，他会先`判断`DataFrame里有没有这个列名，如果有的话就会替换掉原来的列，没有的话就用调用select方法`增加一列`，

所以如果我们的需求是增加一列的话，两者实现的`功能一样`，且最终都是调用select方法，见如下代码：

但是`withColumn`会提前做一些判断处理，所以`withColumn的性能`不如select好。

```scala
/**
 * Returns a new Dataset by adding a column or replacing the existing column that has
 * the same name.
 *
 * @group untypedrel
 * @since 2.0.0
*/
def withColumn(colName: String, col: Column): DataFrame = {
  val resolver = sparkSession.sessionState.analyzer.resolver
  val output = queryExecution.analyzed.output
  val shouldReplace = output.exists(f => resolver(f.name, colName))
  if (shouldReplace) {
    val columns = output.map { field =>
      if (resolver(field.name, colName)) {
        col.as(colName)
      } else {
        Column(field)
      }
    }
    select(columns : _*)
  } else {
    select(Column("*"), col.as(colName))
  }
}
```

## 用户自定义聚合函数

### 弱类型用户自定义聚合函数

需要继承`UserDefinedAggregateFunction`类，并实现其中的8个方法

```scala
import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, StringType, StructField, StructType}

object GetDistinctCityUDF extends UserDefinedAggregateFunction{  
    /**
    * 输入的数据类型
    * */
  override def inputSchema: StructType = StructType(
    StructField("status",StringType,true) :: Nil
  )  
    /**
    * 缓存字段类型
    * */
  override def bufferSchema: StructType = {
    StructType(
      Array(
        StructField("buffer_city_info",StringType,true)
      )
    )
  }
  /**
  * 输出结果类型
  * */
  override def dataType: DataType = StringType
  /**
  * 输入类型和输出类型是否一致
  * */
  override def deterministic: Boolean = true
  /**
  * 对辅助字段进行初始化
  * */
  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    buffer.update(0,"")
  }
  /**
  *修改辅助字段的值
  * */
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {    
  //获取最后一次的值
    var last_str = buffer.getString(0)    //获取当前的值
    val current_str = input.getString(0)    //判断最后一次的值是否包含当前的值
    if(!last_str.contains(current_str)){      
    //判断是否是第一个值，是的话走if赋值，不是的话走else追加
      if(last_str.equals("")){
        last_str = current_str
      }else{
        last_str += "," + current_str
      }
    }
    buffer.update(0,last_str)

  }
  /**
  *对分区结果进行合并
  * buffer1是机器hadoop1上的结果
  * buffer2是机器Hadoop2上的结果
  * */
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {    var buf1 = buffer1.getString(0)
    val buf2 = buffer2.getString(0)    
    //将buf2里面存在的数据而buf1里面没有的数据追加到buf1    
    //buf2的数据按照，进行切分
    for(s <- buf2.split(",")){      
    if(!buf1.contains(s)){        
        if(buf1.equals("")){
          buf1 = s
        }else{
          buf1 += s
        }
      }
    }
    buffer1.update(0,buf1)
  }
  /**
  * 最终的计算结果
  * */
  override def evaluate(buffer: Row): Any = {
    buffer.getString(0)
  }
}
```

注册自定义的UDF函数为临时函数

```scala
def main(args: Array[String]): Unit = {    
      /**
      * 第一步 创建程序入口      
      */
    val conf = new SparkConf().setAppName("AralHotProductSpark")
    val sc = new SparkContext(conf)
    val hiveContext = new HiveContext(sc)
　　//注册成为临时函数
    hiveContext.udf.register("get_distinct_city",GetDistinctCityUDF)　　
    //注册成为临时函数
    hiveContext.udf.register("get_product_status",(str:String) =>{      
      var status = 0
      for(s <- str.split(",")){        
        if(s.contains("product_status")){
          status = s.split(":")(1).toInt
        }
      }
    })
}
```

1. 建一个Class 继承UserDefinedAggregateFunction ，然后复写方法：

```scala
//聚合函数需要输入参数的数据类型
override def inputSchema: StructType = ???
//可以理解为保存聚合函数业务逻辑数据的一个数据结构
override def bufferSchema: StructType = ???
// 返回值的数据类型
override def dataType: DataType = ???
// 对于相同的输入一直有相同的输出
override def deterministic: Boolean = true
//用于初始化你的数据结构
override def initialize(buffer: MutableAggregationBuffer): Unit = ???
//用于同分区内Row对聚合函数的更新操作
override def update(buffer: MutableAggregationBuffer, input: Row): Unit = ???
//用于不同分区对聚合结果的聚合。
override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = ???
//计算最终结果
override def evaluate(buffer: Row): Any = ???
```

2. 你需要通过spark.udf.resigter去注册你的UDAF函数。

3. 需要通过spark.sql去运行你的SQL语句，可以通过 `select UDAF`(列名) 来应用你的用户自定义聚合函数。

### 强类型用户自定义聚合函数

1. 新建一个class，继承Aggregator[Employee, Average, Double]，其中Employee是在应用聚合函数的时候传入的对象，Average是聚合函数在运行的时候内部需要的数据结构，Double是聚合函数最终需要输出的类型。这些可以根据自己的业务需求去调整。复写相对应的方法：

```scala
//用于定义一个聚合函数内部需要的数据结构
override def zero: Average = ???
//针对每个分区内部每一个输入来更新你的数据结构
override def reduce(b: Average, a: Employee): Average = ???
//用于对于不同分区的结构进行聚合
override def merge(b1: Average, b2: Average): Average = ???
//计算输出
override def finish(reduction: Average): Double = ???
//用于数据结构他的转换
override def bufferEncoder: Encoder[Average] = ???
//用于最终结果的转换
override def outputEncoder: Encoder[Double] = ???
```

2. 新建一个UDAF实例，通过DF或者DS的DSL风格语法去应用。

## 完整代码

### 示例一，使用UDF给user表添加两列

下面的代码的功能是使用UDF给user表添加两列:`name_len、isAdult`，每个输出结果都是一样的

```s
+-----+---+--------+-------+
| name|age|name_len|isAdult|
+-----+---+--------+-------+
|  Leo| 16|       3|  false|
|Marry| 21|       5|   true|
| Jack| 14|       4|  false|
|  Tom| 18|       3|   true|
+-----+---+--------+-------+
```

```scala
package com.dkl.leanring.spark.sql

import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
 * Spark Sql 用户自定义函数示例
 */
object UdfDemo {

  def main(args: Array[String]): Unit = {
    oldUdf
    newUdf
    newDfUdf
    oldDfUdf
  }

  /**
   * 根据年龄大小返回是否成年 成年：true,未成年：false
   */
  def isAdult(age: Int) = {
    if (age < 18) {
      false
    } else {
      true
    }

  }

  /**
   * 旧版本(Spark1.x)Spark Sql udf示例
   */
  def oldUdf() {

    //spark 初始化
    val conf = new SparkConf()
      .setMaster("local")
      .setAppName("oldUdf")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    import sqlContext.implicits._

    // 构造测试数据，有两个字段、名字和年龄
    val userData = Array(("Leo", 16), ("Marry", 21), ("Jack", 14), ("Tom", 18))
    //创建测试df
    val userDF = sc.parallelize(userData).toDF("name", "age")
    // 注册一张user表
    userDF.registerTempTable("user")

    // 注册自定义函数（通过匿名函数）
    sqlContext.udf.register("strLen", (str: String) => str.length())

    sqlContext.udf.register("isAdult", isAdult _)
    // 使用自定义函数
    sqlContext.sql("select *,strLen(name)as name_len,isAdult(age) as isAdult from user").show
    //关闭
    sc.stop()

  }

  /**
   * 新版本(Spark2.x)Spark Sql udf示例
   */
  def newUdf() {
    //spark初始化
    val spark = SparkSession.builder().appName("newUdf").master("local").getOrCreate()

    // 构造测试数据，有两个字段、名字和年龄
    val userData = Array(("Leo", 16), ("Marry", 21), ("Jack", 14), ("Tom", 18))

    //创建测试df
    val userDF = spark.createDataFrame(userData).toDF("name", "age")

    // 注册一张user表
    userDF.createOrReplaceTempView("user")

    //注册自定义函数（通过匿名函数）
    spark.udf.register("strLen", (str: String) => str.length())
    //注册自定义函数（通过实名函数）
    spark.udf.register("isAdult", isAdult _)
    spark.sql("select *,strLen(name) as name_len,isAdult(age) as isAdult from user").show

    //关闭
    spark.stop()

  }

  /**
   * 新版本(Spark2.x)DataFrame udf示例
   */
  def newDfUdf() {
    val spark = SparkSession.builder().appName("newDfUdf").master("local").getOrCreate()

    // 构造测试数据，有两个字段、名字和年龄
    val userData = Array(("Leo", 16), ("Marry", 21), ("Jack", 14), ("Tom", 18))

    //创建测试df
    val userDF = spark.createDataFrame(userData).toDF("name", "age")
    import org.apache.spark.sql.functions._
    //注册自定义函数（通过匿名函数）
    val strLen = udf((str: String) => str.length())
    //注册自定义函数（通过实名函数）
    val udf_isAdult = udf(isAdult _)

    //通过withColumn添加列
    userDF.withColumn("name_len", strLen(col("name"))).withColumn("isAdult", udf_isAdult(col("age"))).show
    //通过select添加列
    userDF.select(col("*"), strLen(col("name")) as "name_len", udf_isAdult(col("age")) as "isAdult").show

    //关闭
    spark.stop()
  }
  /**
   * 旧版本(Spark1.x)DataFrame udf示例
   * 注意，这里只是用的Spark1.x创建sc的和df的语法，其中注册udf在Spark1.x也是可以使用的的
   * 但是withColumn和select方法Spark2.0.0之后才有的，关于spark1.xDataFrame怎么使用注册好的UDF没有研究
   */
  def oldDfUdf() {
    //spark 初始化
    val conf = new SparkConf()
      .setMaster("local")
      .setAppName("oldDfUdf")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    import sqlContext.implicits._

    // 构造测试数据，有两个字段、名字和年龄
    val userData = Array(("Leo", 16), ("Marry", 21), ("Jack", 14), ("Tom", 18))
    //创建测试df
    val userDF = sc.parallelize(userData).toDF("name", "age")
    import org.apache.spark.sql.functions._
    //注册自定义函数（通过匿名函数）
    val strLen = udf((str: String) => str.length())
    //注册自定义函数（通过实名函数）
    val udf_isAdult = udf(isAdult _)

    //通过withColumn添加列
    userDF.withColumn("name_len", strLen(col("name"))).withColumn("isAdult", udf_isAdult(col("age"))).show
    //通过select添加列
    userDF.select(col("*"), strLen(col("name")) as "name_len", udf_isAdult(col("age")) as "isAdult").show

    //关闭
    sc.stop()
  }

}

```

### 实例二：温度转化

```scala
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf

object ScalaUDFExample {
  def main(args: Array[String]) {
    val conf       = new SparkConf().setAppName("Scala UDF Example")
    val spark      = SparkSession.builder().enableHiveSupport().config(conf).getOrCreate() 

    val ds = spark.read.json("temperatures.json")
    ds.createOrReplaceTempView("citytemps")

    // Register the UDF with our SparkSession 
    spark.udf.register("CTOF", (degreesCelcius: Double) => ((degreesCelcius * 9.0 / 5.0) + 32.0))

    spark.sql("SELECT city, CTOF(avgLow) AS avgLowF, CTOF(avgHigh) AS avgHighF FROM citytemps").show()
  }
}
```

我们将定义一个 UDF 来将以下 JSON 数据中的温度从摄氏度（degrees Celsius）转换为华氏度（degrees Fahrenheit）：

```json
{"city":"St. John's","avgHigh":8.7,"avgLow":0.6}
{"city":"Charlottetown","avgHigh":9.7,"avgLow":0.9}
{"city":"Halifax","avgHigh":11.0,"avgLow":1.6}
{"city":"Fredericton","avgHigh":11.2,"avgLow":-0.5}
{"city":"Quebec","avgHigh":9.0,"avgLow":-1.0}
{"city":"Montreal","avgHigh":11.1,"avgLow":1.4}
```

### 实例三：时间转化

```scala
case class Purchase(customer_id: Int, purchase_id: Int, date: String, time: String, tz: String, amount:Double)

 3val x = sc.parallelize(Array(
  Purchase(123, 234, "2007-12-12", "20:50", "UTC", 500.99),
  Purchase(123, 247, "2007-12-12", "15:30", "PST", 300.22),
  Purchase(189, 254, "2007-12-13", "00:50", "EST", 122.19),
  Purchase(187, 299, "2007-12-12", "07:30", "UTC", 524.37)
))

val df = sqlContext.createDataFrame(x)
df.registerTempTable("df")
```

自定义函数

```scala
def makeDT(date: String, time: String, tz: String) = s"$date $time $tz"
2sqlContext.udf.register("makeDt", makeDT(_:String,_:String,_:String))

// 现在我们可以直接在 Spark SQL 中使用我们的函数。
sqlContext.sql("SELECT amount, makeDt(date, time, tz) from df").take(2)
// but not outside
df.select($"customer_id", makeDt($"date", $"time", $"tz"), $"amount").take(2)
// fails
```

如果想要在SQL外面使用，必须通过spark.sql.function.udf来创建UDF

```scala
import org.apache.spark.sql.functions.udf
val makeDt = udf(makeDT(_:String,_:String,_:String))
// now this works
df.select($"customer_id", makeDt($"date", $"time", $"tz"), $"amount").take(2)
```

## 实践操作四：写一个UDF来将一些Int数字分类

```scala
val formatDistribution = (view: Int) => {
  if (view < 10) {
    "<10"
  } else if (view <= 100) {
    "10~100"
  } else if (view <= 1000) {
    "100~1K"
  } else if (view <= 10000) {
    "1K~10K"
  } else if (view <= 100000) {
    "10K~100K"
  } else {
    ">100K"
  }
}

```

注册：

```scala
session.udf.register("formatDistribution", UDF.formatDistribution)
```

SQL：

```scala
session.sql("select user_id, formatDistribution(variance_digg_count) as variance from video")
```

### 案例五：SparkSQL解析多层嵌套Json时

解决思路：

通过自定义函数将`JsonObject类型的字符串`转换为`Map`，

将`JsonArray类型的字符串`转换为`Array数组`，

再结合`爆破函数 lateral  view` 对`嵌套Json字符串数据`进行解析落库。

自定义函数strToMap:

```scala
 val strToMap:String=>mutable.Map[String,String]=(jsonstr:String) =>{
    val jSONObject = JSON.parseObject(jsonstr)
    val map = mutable.Map[String,String]()
    val keys = jSONObject.keySet().iterator()
    while (keys.hasNext) {
      val key = keys.next()
      val obj = jSONObject.get(key)
      map.put(key,if(obj!=null)obj.toString else null)
    }
    map
  }
```

自定义函数jsonArrayToArray:

```scala
val jsonArrayToArray:String=>mutable.ArrayBuffer[String]=(jsonArrayStr:String)=>{
    val data_Array = mutable.ArrayBuffer[String]()
    if(!StringUtils.equals("{}",jsonArrayStr) && !StringUtils.isEmpty(jsonArrayStr)){
      val jsonArray = JSON.parseArray(jsonArrayStr)
      for (i <- 0 until  jsonArray.size) {
        val nObject = jsonArray.getJSONObject(i)
        val _keys = nObject.keySet().iterator()
        val _map = mutable.Map[String, String]()
        while (_keys.hasNext) {
          val _key = _keys.next()
          _map.put(_key, if (nObject.get(_key) == null) null else nObject.get(_key).toString)
        }
        import org.json4s.DefaultFormats
        data_Array += Json(DefaultFormats).write(_map)
      }
    }
    data_Array
  }

```

注册函数

```scala
spark.udf.register("strToMap",strToMap)
spark.udf.register("jsonArrayToArray",jsonArrayToArray)
```

结合爆破函数解析数据-JsonObject类型的字符串

```scala
lateral view explode(strToMap(nvl(map_str,null))) as map_key,map_value
```

结合爆破函数解析数据-JsonArray类型的字符串

```scala
lateral view explode(jsonArrayToArray(nvl(array_str,null))) as item
```