<!-- vscode-markdown-toc -->
* 1. [数据说明](#)
* 2. [任务](#-1)
* 3. [步骤](#-1)
	* 3.1. [加载数据](#-1)
		* 3.1.1. [tbStock.txt](#tbStock.txt)
		* 3.1.2. [tbStockDetail.txt](#tbStockDetail.txt)
		* 3.1.3. [tbDate.txt](#tbDate.txt)
	* 3.2. [注册表](#-1)
	* 3.3. [解析表](#-1)
		* 3.3.1. [计算所有订单中每年的销售单数、销售总额](#-1)
		* 3.3.2. [计算所有订单每年最大金额订单的销售额](#-1)
		* 3.3.3. [计算所有订单中每年最畅销货品](#-1)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->

##  1. <a name=''></a>数据说明

这里有三个数据集，合起来大概有`几十万条`数据，是关于`货品交易`的数据集。

```s
tbDate.txt -- tbStockDetail.txt -- tbStock.txt
```

##  2. <a name='-1'></a>任务

这里有三个需求：

- 计算所有订单中每年的`销售单数`、`销售总额`
- 计算所有订单每年`最大金额订单的销售额`
- 计算所有订单中每年`最畅销货品`

##  3. <a name='-1'></a>步骤

###  3.1. <a name='-1'></a>加载数据

####  3.1.1. <a name='tbStock.txt'></a>tbStock.txt

```scala
case class tbStock(ordernumber:String,locationid:String,dateid:String) extends Serializable
val tbStockRdd=spark.sparkContext.textFile("file:///root/dataset/tbStock.txt")
val tbStockDS=tbStockRdd.map(_.split(",")).map(attr=>tbStock(attr(0),attr(1),attr(2))).toDS
tbStockDS.show()
```

```s
+-----------+----------+----------+
|ordernumber|locationid|    dataid|
+-----------+----------+----------+
|BYSL0000893|      ZHAO|2007-08-23|
|BYSL0000897|      ZHAO|2007-08-24|
|BYSL0000898|      ZHAO|2007-08-25|
省略号······
+-----------+----------+----------+
只显示20行
```

####  3.1.2. <a name='tbStockDetail.txt'></a>tbStockDetail.txt

```scala
case class tbStockDetail(ordernumber:String,rownum:Int,itemid:String,number:Int,price:Double,amount:Double) extends Serializable
val tbStockDetailRdd=spark.sparkContext.textFile("file:///root/dataset/tbStockDetail.txt")
val tbStockDetailDS=tbStockDetailRdd.map(_.split(",")).map(attr=>tbStockDetail(attr(0),attr(1).trim().toInt,attr(2),attr(3).trim().toInt,attr(4).trim().toDouble,attr(5).trim().toDouble)).toDS
tbStockDetailDS.show()
```

```s
+-----------+------+----------+------+-----+------+
|ordernumber|rownum|    itemid|number|price| amout|
+-----------+------+----------+------+-----+------+
|BYSL0000893|     0|FS52725501|    -1|268.0|-268.0|
|BYSL0000894|     1|FS52725502|     1|268.0| 268.0|
|BYSL0000895|     2|FS52725503|     1|198.0| 198.0|
省略号······
+-----------+------+----------+------+-----+------+
只显示20行
```

####  3.1.3. <a name='tbDate.txt'></a>tbDate.txt

```scala
case class tbDate(dateid:String,years:Int,theyear:Int,month:Int,day:Int,weekday:Int,week:Int,quarter:Int,period:Int,halfmonth:Int) extends Serializable
val tbDateRdd=spark.sparkContext.textFile("file:///root/dataset/tbDate.txt")
val tbDateDS=tbDateRdd.map(_.split(",")).map(attr=>tbDate(attr(0),attr(1).trim().toInt,attr(2).trim().toInt,attr(3).trim().toInt,attr(4).trim().toInt,attr(5).trim().toInt,attr(6).trim().toInt,attr(7).trim().toInt,attr(8).trim().toInt,attr(9).trim().toInt)).toDS
tbDateDS.show()
```

```s
+-----------+------+-------+-----+---+-------+-------+------+---------+
|     dataid| years|theyear|month|day|weekday|quarter|period|halfmonth|
+-----------+------+-------+-----+---+-------+-------+------+---------+
|   2003-1-1|200301|   2003|    1|  1|      1|      1|     1|        1| 
|   2003-1-2|200301|   2003|    1|  2|      1|      1|     1|        1| 
|   2003-1-3|200301|   2003|    1|  3|      1|      1|     1|        1| 
省略号······
+-----------+------+-------+-----+---+-------+-------+------+---------+
只显示20行
```

###  3.2. <a name='-1'></a>注册表

```sql
tbStockDS.createOrReplaceTempView("tbStock")
tbDateDS.createOrReplaceTempView("tbDate")
tbStockDetailDS.createOrReplaceTempView("tbStockDetail")
```

###  3.3. <a name='-1'></a>解析表

####  3.3.1. <a name='-1'></a>计算所有订单中每年的销售单数、销售总额

```sql
-- sql语句
select c.theyear,count(distinct a.ordernumber),sum(b.amount)
from tbStock a
join tbStockDetail b on a.ordernumber=b.ordernumber
join tbDate c on a.dateid=c.dateid
group by c.theyear
order by c.theyear
```

```scala
spark.sql("select c.theyear,count(distinct a.ordernumber),sum(b.amount)
from tbStock a
join tbStockDetail b on a.ordernumber=b.ordernumber
join tbDate c on a.dateid=c.dateid
group by c.theyear
order by c.theyear").show
```

```s
+--------+----------------------------+------------------+
| theyear| count(DISTINCT ordernumber)|       sum(amount)|
+--------+----------------------------+------------------+
|    2004|                        1094|      3268115.4999| 
|    2005|                        3828|      1268115.2399| 
|    2006|                        3772|       368115.1999| 
省略号······
+--------+----------------------------+------------------+
只显示20行
```

####  3.3.2. <a name='-1'></a>计算所有订单每年最大金额订单的销售额

a、先统计每年每个订单的销售额

```sql
select a.dateid,a.ordernumber,sum(b.amount) as SumOfAmount
from tbStock a
join tbStockDetail b on a.ordernumber=b.ordernumber
group by a.dateid,a.ordernumber
```

```scala
spark.sql("select a.dateid,a.ordernumber,sum(b.amount) as SumOfAmount
from tbStock a
join tbStockDetail b on a.ordernumber=b.ordernumber
group by a.dateid,a.ordernumber").show
```

```s
+--------+----------------------------+------------------+
|  dateid|                 ordernumber|       SumOfAmount|
+--------+----------------------------+------------------+
|2009-4-9|                BYSL00001175|             350.0| 
|2009-5-6|                BYSL00001214|             590.0| 
|2009-7-5|                BYSL00011545|            2040.0| 
省略号······
+--------+----------------------------+------------------+
只显示20行
```

b、计算最大金额订单的销售额

```sql
select d.theyear,c.SumOfAmount as SumOfAmount 
from 
(select a.dateid,a.ordernumber,sum(b.amount) as SumOfAmount 
from tbStock a
join tbStockDetail b on a.ordernumber=b.ordernumber  
group by a.dateid,a.ordernumber) c  
join tbDate d on c.dateid=d.dateid  
group by d.theyear
order by theyear desc
```

```scala
spark.sql("select d.theyear,c.SumOfAmount as SumOfAmount 
from 
(select a.dateid,a.ordernumber,sum(b.amount) as SumOfAmount 
from tbStock a
join tbStockDetail b on a.ordernumber=b.ordernumber  
group by a.dateid,a.ordernumber) c  
join tbDate d on c.dateid=d.dateid  
group by d.theyear
order by theyear desc").show
```

```s
+--------+------------------+
| theyear|  max(SumOfAmount)|
+--------+------------------+
|    2010|        13065.4999| 
|    2009|        25813.2399| 
|    2008|        55828.1999| 
省略号······
+--------+------------------+
只显示20行
```

####  3.3.3. <a name='-1'></a>计算所有订单中每年最畅销货品

a、求出每年每个货品的销售额

```sql
select c.theyear,b.itemid,sum(b.amount) as SumOfAmount 
from tbStock a 
join tbStockDetail b on a.ordernumber=b.ordernumber 
join tbDate c on a.dateid=c.dateid 
group by c.theyear,b.itemid
```

```scala
spark.sql("select c.theyear,b.itemid,sum(b.amount) as SumOfAmount 
from tbStock a 
join tbStockDetail b on a.ordernumber=b.ordernumber 
join tbDate c on a.dateid=c.dateid 
group by c.theyear,b.itemid").show
```

```s
+--------+---------------------+-----------------+
| theyear|               itemid|      SumOfAmount|
+--------+---------------------+-----------------+
|    2004|        4382440110202|          4474.72| 
|    2005|        YA01102023828|            556.0| 
|    2006|        BT11020238772|            360.0| 
省略号······
+--------+---------------------+-----------------+
只显示20行
```

b、在a的基础上，统计每年单个货品的最大金额

```sql
select d.theyear,max(d.SumOfAmount) as MaxOfAmount 
from 
(select c.theyear,b.itemid,sum(b.amount) as SumOfAmount 
from tbStock a 
join tbStockDetail b on a.ordernumber=b.ordernumber 
join tbDate c on a.dateid=c.dateid 
group by c.theyear,b.itemid) d 
group by theyear
```

```scala
spark.sql("select d.theyear,max(d.SumOfAmount) as MaxOfAmount 
from 
(select c.theyear,b.itemid,sum(b.amount) as SumOfAmount 
from tbStock a 
join tbStockDetail b on a.ordernumber=b.ordernumber 
join tbDate c on a.dateid=c.dateid 
group by c.theyear,b.itemid) d 
group by theyear").show
```

```s
+--------+----------------+
| theyear|     MaxOfAmount|
+--------+----------------+
|    2007|        70444.72| 
|    2006|       111356.20| 
|    2004|        53460.00| 
省略号······
+--------+----------------+
只显示20行
```

c、用最大销售额和统计好的每个货品的销售额join，以及用年join，集合得到最畅销货品那一行信息

```sql
select distinct e.theyear,e.itemid,f.maxofamount 
from 
(select c.theyear,b.itemid,sum(b.amount) as sumofamount 
from tbStock a 
join tbStockDetail b on a.ordernumber=b.ordernumber 
join tbDate c on a.dateid=c.dateid 
group by c.theyear,b.itemid) e 
join 
(select d.theyear,max(d.sumofamount) as maxofamount 
from 
(select c.theyear,b.itemid,sum(b.amount) as sumofamount 
from tbStock a 
join tbStockDetail b on a.ordernumber=b.ordernumber 
join tbDate c on a.dateid=c.dateid 
group by c.theyear,b.itemid) d 
group by d.theyear) f on e.theyear=f.theyear 
and e.sumofamount=f.maxofamount order by e.theyear
```

```scala
spark.sql("select distinct e.theyear,e.itemid,f.maxofamount 
from 
(select c.theyear,b.itemid,sum(b.amount) as sumofamount 
from tbStock a 
join tbStockDetail b on a.ordernumber=b.ordernumber 
join tbDate c on a.dateid=c.dateid 
group by c.theyear,b.itemid) e 
join 
(select d.theyear,max(d.sumofamount) as maxofamount 
from 
(select c.theyear,b.itemid,sum(b.amount) as sumofamount 
from tbStock a 
join tbStockDetail b on a.ordernumber=b.ordernumber 
join tbDate c on a.dateid=c.dateid 
group by c.theyear,b.itemid) d 
group by d.theyear) f on e.theyear=f.theyear 
and e.sumofamount=f.maxofamount order by e.theyear").show
```

```s
+--------+---------------------+-----------------+
| theyear|               itemid|      MaxOfAmount|
+--------+---------------------+-----------------+
|    2004|        4382440110202|         53401.72| 
|    2005|        YA01102023828|          55627.0| 
|    2006|        BT11020238772|         113720.0| 
省略号······
+--------+---------------------+-----------------+
只显示20行