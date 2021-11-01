## 自定义udf函数

1. 注册一个UDF函数

```scala
spark.udf.register(name,func)
```

`name`是UDF调用时的标识符

`fun`是一个函数，用于处理字段

2. 需要将一个DF或者DS注册为一个临时表

3. 通过spark.sql去运行一个SQL语句，在SQL语句中可以通过name(列名)方式来应用UDF函数

## 用户自定义聚合函数

### 弱类型用户自定义聚合函数

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