# 1. scala概述

[3小时入门](https://mp.weixin.qq.com/s/ZQNx1bLWH3vY9i4kemIQxQ)

## 1.1. 特性

换行符：分号

scala 没有所谓的基本数据类型，一切结尾对象

用“var”声明变量，用“val”声明常量

scala中，Function和Method分别翻译为function和method

Scala产生于**瑞士的洛桑联邦理工学院（EPEL）**，是“可扩展语言”（Scalable Language）的缩写，Scala是一种**多范式**的编程语言，其设计的初衷是要集成**面向对象**编程和**function式编**程的各种特性。你可以使用Scala编写出**更加精简**的程序，也能用于构建**大型复杂系统**，还可以访问**任何Java类库**并且与**Java框架**进行交互。Spark，Kafka，Flink的兴起，带动Scala的快速发展，成为**大数据黄金语言**。

在Scala语言中，**静态类型（static typing）**是构建健壮应用系统的一个工具。

Scala修正了Java类型系统中的一些缺陷，此外通过**类型推演（typeinference）**也免除了大量冗余代码。

Scala完全支持**面向对象编程（OOP）**。引入了**特征（trait）**改进了Java的**对象模型**。trait能通过使用**混合结构（maxin composition）**简洁的实现新的类型。

在Scala中，**一切都是对象**，即使是数值类型。

Scala也完全的支持**function式编程（FP）**，**function式编程**已经被视为解决并发、大数据以及代码正确性问题的最佳工具。

使用**不可变值**、**function**、**高阶function**以及**function集合**，有助于编写出简洁、强大而又正确的代码。

## 1.2. Scala集合简介

scala同时支持**不可变集合**和**可变集合**。

**不可变集合（不能动态变化）**位于**scala.collection.immutable包**下；

**可变集合**位于 **scala.collection.mutable包**下。

scala**默认**采用**不可变集合**，

对于几乎所有的**集合**类，scala都同时提供了可变和不可变的版本。

scala的**集合**有三大类：

* 序列Seq
* 集Set
* 映射Map

所有的**集合**都扩展自**Iterable特质**。

Scala 的**集合**设计是最容易让人着迷的地方。

通过 Scala 提供的**集合**操作，基本上可以**实现 SQL 的全部功能**，

这也是为什么 Scala 能够在大数据领域独领风骚的重要原因之一。

## 1.3. Scalafunction式编程

**集合的function式编程**在大数据处理方面非常重要，

必须完全掌握和理解Scala的高阶function，

Scala的**集合类**的

:maple_leaf: map
:maple_leaf: flatMap
:maple_leaf: reduce
:maple_leaf: reduceLeft
:maple_leaf: foreach

这些function都是**高阶function**，

因为可以**接收其他function作为参数**。

**高阶function**的使用，也是Scala与Java最大的一点不同。

java 8引入了**function式编程Lambda表达式**。

# 2. 99个Scala问题

这些是瑞士伯尔尼应用科学大学的Werner Hett所写的《九十九个序言问题》的[改编](http://aperiodic.net/phil/scala/s-99/)。

- 那些标有一个星号`*`的很简单。如果你已经成功地解决了前面的问题，你应该能够在几分钟（比如15分钟）内解决它们。

- 标有两个星号`**`的问题具有中等难度。如果您是一名熟练的Scala程序员，解决这些问题不应该超过30-90分钟。

- 标有三个星号`***`的问题更难解决。您可能需要更多的时间（即几个小时或更长的时间）来找到一个好的解决方案。

找到给定问题的最优雅的解决方案。效率固然重要，但清晰更为关键。一些（简单的）问题可以使用内置函数轻松解决。然而，在这些情况下，如果您试图找到自己的解决方案，您会学到更多。

# 3. 常用集合操作(一行代码搞定)

## 3.1. 过滤出集合中所有偶数

filtermethod:

> 会将**序列**中各个**元素**依次替换到**下划线"_"所处位置**，

> 如果返回true，则保留该元素:

```js
(1 to 9).filter( _ % 2 == 0 )
```

输出：2, 4, 6, 8

## 3.2. 对序列中所有元素求和

reduceLeftmethod：

> 是一个通用的**聚集计算**method，你可以把"+"换成其它的运算符。

```js
(1 to 9).reduceLeft(_ + _)
```

输出：45

## 3.3. 统计单词出现次数

groupBymethod:

> 可以将**序列**转换成**Map**，

适合用在:

> 需要**按某个属性**进行**统计**的情况。

```js
List("one", "two", "one", "four").groupBy(w => w).mapValues(_.length)
```

输出：

scala.collection.immutable.Map[String,Int] = Map(one -> 2, four -> 1, two -> 1)

one有2个

four有1个

two有1和


## 3.4. 将序列中单词首字母大写

capitalize: 

> 变成大写

mapmethod:

> 可以把**序列转换**成**另一个序列**

mapmethod中定义:

> 各个**元素**的**转换**过程。

```js
List("one", "line", "of", "code").map(_.capitalize)
```

List(One, Line, Of, Code)

## 3.5. 将序列拼接成字符串

mkStringmethod:

> 用于将**序列拼接**成**字符串**，

- 第1个参数是**起始符号**
- 第2个参数是**分隔符**
- 第3个参数是**结束符号**

```js
(1 to 9).mkString("(", ",", ")")
```

输出：(1,2,3,4,5,6,7,8,9)

## 3.6. 最大值，最小值和求和

这在Scala中轻而易举，直接调用min，max和summethod。

```js
List(14, 35, -7, 46, 98).min
```

输出：-7 

```js
List(14, 35, -7, 46, 98).max
```

输出：98 

```js
List(14, 35, -7, 46, 98).sum
```

输出：186

## 3.7. 获取序列中最大的前3个数值和位置

zipWithIndexmethod:

> 将序列**List[Int]**转换成

> **List[(Int, Int)]**

> 即**List[Tuple2[Int, Int]]**。

Tuple2的

第1个Int是**元素**
第2个Int是**元素所处的位置**

```js
List(2, 0, 1, 4, 12, 5).zipWithIndex.sorted.reverse.take(3)
```

三步：:point_right: 转换为List[Tuple2[Int, Int]]、:point_right: 逆向排序、:point_right: 选取最大的三个

输出：List((12,4), (5,5), (4,3))

## 3.8. 读取文本文件

在Scala中读取文本文件相当轻松。

```js
val fileContent = io.Source.fromFile("myfile.txt").mkString
val fileLines = io.Source.fromFile("myfile.txt").getLines.toList
```

## 3.9. 下载URL链接

下载文件就是这么容易。

其实利用**sys.process**包，

也就是说，我们可以用Scala编写Shell脚本，是不是很酷:cool:！

```py
import sys.process._
import java.net.URL
import java.io.File
new URL("http://www.oschina.net/favicon.ico") #> new File("d:/favicon.ico") !!
```

## 3.10. 并行计算

parmethod：

将**原序列**转换成**并行序列**，可以利用**多核的优势**加快处理速度，实现**并行计算**。

Scala 的**并行集合**可以利用**多核**优势加速计算过程，

通过**集合**上的 **par method**，

我们可以将**原集合**转换成**并行集合**。

**并行集合**利用**分治算法**将计算任务分解成很多子任务，

然后，交给不同的**线程**执行，

最后，将计算结果进行**汇总**。

```js
(1 to 1000000).par.sum
```

输出：Int = 1784293664

## 3.11. 两个List相乘求和

dataList 加权 weightList 求和。

```js
dataList.zip(weightList).map{t => t._1 * t._2}.sum
```

## 3.12. 按多个字段排序List

先按学生的**年龄**排序，

如果年龄相同，则按**分数**排序：

```js
case class Student(name: String, age: Int, score: Int)
List(
Student("a", 14, 60), 
Student("b", 15, 80), 
Student("a", 15, 70)
).sortBy(s => (s.age, s.score))
```

```js
case class 学生(姓名: String, 年龄: Int, 分数: Int)
List(
学生("a", 14, 60), 
学生("b", 15, 80), 
学生("a", 15, 70)
).sortBy(s => (s.年龄, s.分数))
```

输出：List(Student(a,14,60), Student(a,15,70), Student(b,15,80))

## 3.13. 将List相邻元素分组(这个好难，看不懂)

每相邻的10个元素分成一组：

```js
val list = (0 to 20).map(_.toString)
```

输出：

Vector(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)

```js
list
.zipWithIndex // 转换为List[Tuple2[Int, Int]]
.map(t => t._1 -> ((t._2 / 10) * 10))
.groupBy(_._2) //可以将 **序列** 转换成 **Map** ，
.toList
.sortBy(_._1)
.map(_._2
.map(_._1))
```

输出：

List(
	Vector(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), 
	Vector(10, 11, 12, 13, 14, 15, 16, 17, 18, 19), 
	Vector(20)
 )

## 3.14. 取序列的第1个元素

```js
List(3, 6, 5).headOption.getOrElse(0)
```

输出：Int = 3

# 4. Scalafunction和method的区别

## 4.1. val 和 def

val语句  定义  function

def语句  定义  method

```js
class Test{
  //method
  def m(x: Int) = x + 3
  //function
  val f = (x: Int) => x + 3
  val f1:(Int)=>Int = (y) => y + 3
}
```

## 4.2. **method**转换成**function**

在Scala中，无法直接操作**method**，

如果要操作**method**，必须先将其转换成**function**。

有两种method可以将**method**转换成**function**：

> method一：

```js
val f1 = m _
```

在method名称**m后面**紧跟一个**空格**和**下划线**，

告诉编译器将methodm转换成function，而不是要调用这个method。

> method二：

也可以**显示地**告诉编译器需要将method转换成function：

```js
val f1: (Int) => Int = m
```

## 4.3. 直接调用**function**上的**method**

可以直接调用function上的method，而method却不行

```js
f.toString //编译通过
m.toString //编译失败
```

### 4.3.1. 举个栗子：Curryingfunction和Curryingmethod

**Curryingfunction**可以

只传入部分参数

返回一个**偏function**(partially applied function, 也叫部分应用function)，

**Curryingmethod**在转换成**偏function**时

需要加上**显式说明**，让**编译器**完成转换

```js
object TestCurrying {

  def invoke(f: Int => Int => Int): Int = {
    f(1)(2)
  }

  def multiply(x: Int)(y: Int): Int = x * y

  def main(args: Array[String]) {
    //编译器会自动将 “multiplymethod” 转换成 “function”
    invoke(multiply)

    val partial1 = multiply(1) //尝试将Curryingmethod转换成偏function导致编译失败
    val partial2 = multiply(1): (Int => Int) //编译通过

    //将 “multiplymethod” 转换成 “functionf” 
    val f = multiply _  
    val partial3 = f(1) //只应用第1个参数返回偏function,编译通过
  }
}
```

Scala中的method跟Java的method一样，

method是组成类的一部分。

method有：

- 名字
- 类型签名

有时method上还有：

- 注解
- method的功能

## 4.4. 总结

method不能作为**单独的表达式**而存在（参数为空的method除外），

而function可以。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.600lrnyrgds0.png)


**function**必须要有**参数列表**，

**method**可以没有**参数列表**

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3eiiig1j7520.png)

**method名**是**method调用**，

而**function名**只是代表**function对象本身**

所以**function名后面加括号才是调用function**。如下：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.7fgpnj19mmo0.png)

在需要**function**的地方，如果传递一个**method**，

会自动进行**ETA展开**（把method转换为function）：

直接把一个方法赋值给变量会报错：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4dnnfa3ermk.png)

我们也可以强制把一个方法转换给函数：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2nqgbazkupw0.png)

指定变量的类型就是函数：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6gwbokjcsqo0.png)

## 4.5. 传名参数

**传名参数**本质上是个方法，一个**参数列表为空**的方法：

```js
def m1(x: =>Int)=List(x,x)
```

如上代码实际上定义了一个**方法m1**，

m1的参数是个**传名参数**（方法）。

由于对于**参数为空**的方法来说，**方法名**就是方法调用

所以`List(x,x)`实际上是进行了**两次**方法调用:

(由于`List(x,x)`是进行了两次方法调用，所以得到两个不同的值。)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.ca4au260t20.png)

```js
//使用两次‘x‘，意味着进行了两次方法调用

def m1(x: => Int)=List(x,x)
import util.Random
val r = new Random()

//因为方法被调用了两次，所以两个值不相等
m1(r.nextInt)
```

输出：

> res21: List[Int] = List(-159079888, -453797380)

如果你在方法体部分缓存了**传名参数**（函数），

那么你就缓存了值（因为**x函数**被调用了一次）

```js
//把传名参数代表的函数缓存起来

def m1(x: => Int) = {val y=x;List(y,y)}
m1(r.nextInt)
```

输出：

> res22: List[Int] = List(-1040711922, -1040711922)

能否在**函数体部分**引用**传名参数**所代表的方法呢?

是可以的(**缓存**的是传名参数所代表的方法)。

```js
def m1(x: => Int)={ val y=x _; List(y(),y()) }

m1(r.nextInt)
```

输出：
> m1: (x: => Int)List[Int]
> res23: List[Int] = List(1677134799, 180926366)


## 4.6. 有参 vs 无参

**有参方法**可以作为**表达式**的一部分出现，（调用函数并传参）

但**有参方法**不能作为**最终的表达式**出现

**无参方法**可以作为**最终表达式**出现；(其实这属于方法调用,无参方法的调用可以省略括号)

但**函数**可以作为**最终表达式**出现

```js
//定义一个方法
def m(x:Int) = 2*x
```

输出：

> m: (x: Int)Int

```js
//定义一个函数
scala> val f = (x:Int) => 2*x
```

输出：

> f: Int => Int = <function1>

```js
//方法不能作为最终表达式出现
m
```

输出：

> <console>:9: error: missing arguments for method m;

```js
//函数可以作为最终表达式出现
f
```

输出：

> res9: Int => Int = <function1>

```js
 //定义无参方法，并可以作为最终表达式出现
def m1()=1+2 
m1
```

输出：

> res10: Int = 3

## 4.7. 方法可以自动(称之ETA扩展)或手动强制转换为函数

在scala中很多高级函数，如map(),filter()等，都是要求提供一个**函数**作为参数。

参数列表对于方法是可选的，但是对于函数参数列表是强制的

```js
val myList = List(3,56,1,4,72)
 // map()参数是一个函数
myList.map((x) => 2*x)
```

输出：

> res15: List[Int] = List(6, 112, 2, 8, 144)

```js
 //尝试给map()函提供一个方法作为参数
def m4(x:Int) = 3*x
 //正常执行
myList.map(m4)
```

输出：

> res17: List[Int] = List(9, 168, 3, 12, 216)

这是因为，如果期望出现函数的地方我们提供了一个方法的话，该方法就会自动被转换成函数。该行为被称为**ETA expansion**。

这样的话使用函数将会变得简单很多。你可以按照下面的代码验证该行为：

```js
//期望出现函数的地方，我们可以使用方法
 val f3:(Int)=>Int = m4
//不期望出现函数的地方，方法并不会自动转换成函数
 val v3 = m4
```

利用这种自动转换，我们可以写出很简洁的代码，如下面这样：

```js
//10.<被解释成obj.method，即整形的<的方法，所以该表达式是一个方法，会被解释成函数
 myList.filter(10.<)
```

输出：

>res18: List[Int] = List(56, 72)

## 4.8. scala中操作符

- 前缀操作符：op obj 被解释称 obj.op

- 中缀操作符：obj1 op obj2被解释称 obj1.op(obj2)

- 后缀操作符：obj op被解释称 obj.op

# 5. scala vs java

## 5.1. 文件名

java要求**文件名**和**公共类**名必须要求一致，scala不要求。

```java
test.java -> public class test{}
```

```js
test.scala -> class xxx(任意){}
```

## 5.2. 关键字

1. scala 中没有**public关键字**，默认访问权限就是public
2. scala中没有**void关键字**，因为scala是完全面向对象的语言，所以采用特殊的对象来模拟：Unit

## 5.3. method和function

1. method定义的参数顺序不同

> java把**参数类型**放在前:

```java
public String test(String param){
  
}
```

> scala把**参数类型**放在之后:

```js
def test(param:String):String = {
  
}
```

2. scala的**method**里面也可以声明**method**:

```js
object test{
  def main(args:Array[String]):Unit = {
    def test(): Unit={
      
    }
  }
}
```

3. java中包含**method重载** <br> 但scala中同一作用域不能有**同名function**，即使function参数不一致

见如下的test

```js
object Test {
  def main(arg: Array[String]): Unit = {

    def test():Unit={
    }
    
    // 会报错，不允许通过
    def test(s:String):Unit={
    }
  }
```

4. 传递多参数的时候，java用 ... scala使用*

```java
public test(String args...){
  
}
```

```js
def test(args:String*):Unit={
  
}
```

5. scala 在function定义时，可以给定默认参数。

```js
def test(param1:String = "morenzhi"):Unit={
  
}
```

> 但是**默认参数**推荐放参数列表**后面**，<br> 否则需要确保后面的无默认值参数从左到右能匹配到，<br> 另外可以使用带名参数传递参数:

```js
//声明  默认值在前
def test1(param1:String = "t1",param2:String):Unit={

}

//调用   带名参数
test1(param2="t2")

//从左到右都给了参数
test1("t1","t2")
```

## 5.4. 异常

java的异常和scala的异常大体相同，catch的实现有些小区别.

java异常:

```java
try{
  
}catch(RuntimeException e){
  
}catch(Exception e){
  
}finally{
  
}
```


scala的异常：

```js
try{
  
}catch{
  case e:RuntimeException
  case e:Exception => 
}
```

## 5.5. 类和对象

1. scala类的定义和java一致 <br> 在**属性初始化**的时候有些区别。
   
   * java的**属性初始化**时，若不指定**初始值**，**jvm**会补充上。
   
   * scala初始化用 **_** 代替，注意是**var变量**。如下：

```js
var str:String = _
var num:Int = _
```

2. scala和java的包声明方式**默认方式**一致，但是有其他使用方式。

scala: 即源码中的**类所在位置**不需要和**包路径**相同

```js
package com.lucky
package test  
class Emp{
}
//最终会组合，其中的Emp会在   com.lucky.test  中
```

scala中的所有语法都可以进行**嵌套**，package也可以**嵌套**。

如果有{} ，那么{}中声明的类在这个包中，之外的不在这个包中:

```js
package test1{
  package test2{
    
  }
}
```

scala中可以声明**父包**和**子包**，

父包中的**类**，**子类可以直接访问**，不需要引入，和**作用域一致**

```js
package test1{
  class Emp{
    
  }
  package test2{
    object User{
      def main(arg: Array[String]): Unit = {
        //可以访问
         val emp = new Emp
      }
    }
  }
}
```

**package**中可以声明**类**，如上：

:space_invader::space_invader::space_invader::space_invader::space_invader::space_invader::space_invader:

但是不能声明**变量**和**function(method)**

但是scala为了弥补包的不足，加入了**包对象**概念 :handbag:

如下 **package object Emp**

```js
package test1{
  package object Emp{
    val username = "object"
  }
  package test2{
    object User{
      def main(arg: Array[String]): Unit = {
        //可以访问
         println(Emp.username)
      }
    }
  }
}
```

在同一个源码文件中，可以**多次声明**。声明的**类**在**最后的那个包**中

java:

```java
package com.lucky.test
package test  //会出问题
```

## 5.6. import

scala也使用import导入类，但也有些区别，在scala中

import可以在任意地方使用

导入一个包中所有的类，采用**下划线**：

```java
//java
import java.lang.*
```

```js
//scala
import java.lang._
```

导入同一个包中部分类，用{}和, 组合：

```java
//java
import java.util.ArrayList
import java.util.HashMap
```

```js
//scala
import java.util.{ArrayList,HashMap}
```

import隐藏指定的类：

scala：

```js
//隐藏方式  {类名=>_}
import java.util.{Date=>_}
```

import 可以导包：

scala：

```js
import java.util
```

## 5.7. java和scala调用python程序

### 5.7.1. java中调用python的方法

1. 使用Jython；
2. 使用Jep；:key: 
3. 使用Runtime.getRuntime()；:key: 
4. 使用py4j 的方式。

   本文今天将介绍使用Runtime.getRuntime()的方式调用python程序。

python代码：（脚本名`JavaCallPythonDemo.py`）

```py
# coding=utf-8
if __name__ == '__main__':
   print("hello, world")
```

java代码：
```java
public static void main(String[] args) {
   String[] arguments = new String[] {"/usr/bin/python3", "src/main/python/JavaCallPythonDemo.py"};
   try {
       Process process = Runtime.getRuntime().exec(arguments);
       BufferedReader bufferReader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
       String line = null;
       while ((line = bufferReader.readLine()) != null) {
           System.out.println(line);
       }
       bufferReader.close();
       //process.waitFor()返回值为0表示我们调用python脚本成功，返回值为1表示调用python脚本失败
       int res = process.waitFor();
       System.out.println(res);
   } catch (Exception e) {
       e.printStackTrace();
   }
}
```

Java代码中Process的几种方法：

1. destroy()：杀掉子进程
2. exitValue()：返回子进程的出口值，值 0 表示正常终止
3. getErrorStream()：获取子进程的错误流
4. getInputStream()：获取子进程的输入流
5. getOutputStream()：获取子进程的输出流
6. waitFor()：导致当前线程等待，如有必要，一直要等到由该 Process 对象表示的进程已经终止。
   如果已终止该子进程，此方法立即返回。如果没有终止该子进程，调用的线程将被阻塞，直到退出子进程，根据惯例，0 表示正常终止

遇到的问题：

当python代码中有引入第三方库时，process.waitFor()会返回1，即调用失败。

解决方案：

将 String[] arguments = new String[] {"python", "src/main/python/JavaCallPythonDemo.py"}中的参数python

换成绝对路径即可。比如："/usr/bin/python3"

### 5.7.2. scala调用python的代码

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1pgejbudddkw.png)

python代码：(脚本名`ScalaCallPythonDemo.py`)

```py
# coding=utf-8
import sys
def sum(a, b):
   print(int(a) + int(b))
if __name__ == '__main__':
   sum(sys.argv[1], sys.argv[2])
```

Scala代码：

```js
def main(args: Array[String]): Unit = {
 val arguments = Array[String]("/usr/bin/python3", "src/main/python/ScalaCallPythonDemo.py", "6", "10")
 try {
   val process = Runtime.getRuntime.exec(arguments)
   val bufferReader = new BufferedReader(new InputStreamReader(process.getInputStream, StandardCharsets.UTF_8))
   var line = ""
   var flag:Boolean= true
   while (flag){
     line = bufferReader.readLine()
     //Scala在读取文件时，如果读到最后会返回一个null值，因此，此时我们将标志位改为false，以便下一次结束while循环
     if (line == null){
       flag = false
     }else{
       println(line)
     }
   }
   bufferReader.close()
   val res = process.waitFor
   System.out.println(res)
 } catch {
   case e: Exception =>
     e.printStackTrace()
 }
}
```

## 5.8. Scala与JAVA互操作

### 5.8.1. Java调Scala

Java可以直接操作纵Scala类，如同scala直接使用Java中的类一样，例如：

 

```js
//在Person.scala文件中定义Scala语法的Person类
package cn.scala.xtwy.scalaToJava
class Person(val name:String,val age:Int)
//伴生对象
object Person{
  def getIdentityNo()= {"test"}
}

//ScalaInJava.java文件中定义了ScalaInJava类
//直接调用Scala中的Person类

package cn.scala.xtwy.scalaToJava;
 
public class ScalaInJava {
    public static void main(String[] args) {
       Person p=new Person("摇摆少年梦", 27);
       System.out.println("name="+p.name()+" age="+p.age());
       //伴生对象的方法当做静态方法来使用
       System.out.println(Person.getIdentityNo());
    }
}

```

对！就是这么简单，Java似乎可以无缝操纵Scala语言中定义的类，

在trait那一节中我们提到，如果trait中全部是抽象成员，

则它与java中的interface是等同的，

这时候java可以把它当作接口来使用，但如果trait中定义了具体成员，

则它有着自己的内部实现，此时在java中使用的时候需要作相应的调整。


### 5.8.2. Scala调java

Scala可以直接调用Java实现的任何类，

只要符合scala语法就可以，不过某些方法在JAVA类中不存在，

但在scala中却存在操作更简便的方法，例如集合的`foreach`方法，

在java中是不存在的，但我们想用的话怎么办呢？

这时候可以通过隐式转换来实现，scala已经为我们考虑到实际应用场景了，例如：

```java
import java.util.ArrayList;

class RevokeJavaCollections {
    def getList={
      val list=new ArrayList[String]()
      list.add("摇摆少年梦")
      list.add("学途无忧网金牌讲师")
      list
    }

  def main(args: Array[String]) {
    val list=getList
    //因为list是java.util.ArrayList类型，所以下这条语句编译不会通过
    list.foreach(println)
  }

}
```

此时只要引入`scala.collection.JavaConversions._`包就可以了，

它会我们自动地进行隐式转换，从而可以使用scala中的一些非常方便的高阶函数，

如`foreach`方法，代码如下：

```js
import java.util.ArrayList;
//引入下面这条语句后便可以调用scala集合中的方法，如foreach,map等
import scala.collection.JavaConversions._
/**
 * Created by 摇摆少年梦 on 2015/8/16.
 */
object RevokeJavaCollections{
  def getList={
    val list=new ArrayList[String]()
    list.add("摇摆少年梦")
    list.add("学途无忧网金牌讲师")
    list
  }

  def main(args: Array[String]) {
    val list=getList
    //现在可以调用scala集合中的foreach等方法了
    list.foreach(println)
    val list2=list.map(x=>x*2)
    println(list2)
  }
}
```

### 5.8.3. Scala与Java集合互转摘要

对于集合而言，Scala从2.8.1开始引入`scala.collection.JavaConverters`

用于Scala与Java集合的互转。

在scala代码中如果需要集合转换，首先引入`scala.collection.JavaConverters._`，

进而显示调用`asJava`或者`asScala`方法完成转型。

与此雷同的`scala.collection.JavaConversions`已被标注为

@Deprecated（since 2.12.0），`JavaConversions`可以做到隐式转换，

即不需要`asJava`或者`asScala`的调用，但是这样可能会对阅读造成障碍，可能会让人难以知晓什么变成了什么。

以下可以通过`asScala`和`asJava`进行互转：

*    scala.collection.Iterable       <=> java.lang.Iterable
*    scala.collection.Iterator       <=> java.util.Iterator
*    scala.collection.mutable.Buffer <=> java.util.List
*    scala.collection.mutable.Set    <=> java.util.Set
*    scala.collection.mutable.Map    <=> java.util.Map
*    scala.collection.concurrent.Map <=> java.util.concurrent.ConcurrentMap

以下可以通过`asScala`将Java的转成Scala的，

通过特殊的命名（如`asJavaCollection`）将Scala的转成Java的：

*    scala.collection.Iterable    <=> java.util.Collection   (via asJavaCollection)
*    scala.collection.Iterator    <=> java.util.Enumeration  (via asJavaEnumeration)
*    scala.collection.mutable.Map <=> java.util.Dictionary   (via asJavaDictionary)
  
以下可以通过`asJava`进行Scala到Java的单向转换：

*    scala.collection.Seq         => java.util.List
*    scala.collection.mutable.Seq => java.util.List
*    scala.collection.Set         => java.util.Set
*    scala.collection.Map         => java.util.Map

以下可以通过`asScala`进行Java到Scala的单向转换：

*    java.util.Properties => scala.collection.mutable.Map
  
在所有情形下，从原始类型转变到对侧类型之后再转变回来的话会是同一个对象，举例

```js
import scala.collection.JavaConverters._
val source = new scala.collection.mutable.ListBuffer[Int]
val target: java.util.List[Int] = source.asJava
val other: scala.collection.mutable.Buffer[Int] = target.asScala
assert(source eq other)
```

另外，转换方法也有其描述性的名称可供显示调用，举例如下：

```js
scala> val vs = java.util.Arrays.asList("hi", "bye")
vs: java.util.List[String] = [hi, bye]
scala> val ss = asScalaIterator(vs.iterator)
ss: Iterator[String] = non-empty iterator
scala> .toList
res0: List[String] = List(hi, bye)
scala> val ss = asScalaBuffer(vs)
ss: scala.collection.mutable.Buffer[String] = Buffer(hi, bye)
```


# scala 包

## java

java.time.LocalDateTime
java.time.LocalDate
java.time.format.DateTimeFormatter
java.time.temporal.TemporalAccessor
java.time.temporal.ChronoUnit
java.time.instant
java.time.Zoneld
java.time.ZoneOffset
java.time.Period
java.time.OffsetDateTime
java.time.Duration
java.time.Instant
java.time.Period

java.util.UUID
java.util.concurrent.TimeUnit
java.util.concurrent.Executors
java.util.concurrent.CopyOnWriteArrayList
java.util.concurrent.atomic.AtomicReference
java.util.concurrent.ConcurrentHashMap
java.util.concurrent.ThreadLocalRandom
java.util.Currency
java.util.LinkedHashMap
java.util.Base64
java.util.Properties
java.util.HashMap
java.util.HashSet
java.util.Locale

java.nio.ByteBuffer
java.nio.CharBuffer
java.nio.charset.StandardCharsets
java.nio.charset.Charset
java.nio.channels.ReadableByteChannel
java.nio.file.Files
java.nio.file.InvalidPathException
java.nio.file.Path
java.nio.file.Paths

java.io.Serializable
java.io.File
java.io.PrintWriter
java.io.StringWriter
java.io.Closeable
java.io.ByteArrayOutputStream
java.io.IOException
java.io.InputStreamReader
java.io._


java.lang.StringBuilder
java.lang.reflect.InvocationHandler
java.lang.reflect.Method
java.lang.reflect.Proxy
java.math.BigDecimal
java.math.BigInteger
java.net.URI
java.net.URLEncoder
java.net.URLDncoder
java.net.URISyntaxException

## akka

akka.actor.Actor
akka.actor.ActorRef
akka.actor.ActorRefFactory
akka.actor.ActorSystem
akka.actor.Props
akka.actor.PoisonPill
akka.event.Logging
akka.io.IO
akka.pattern.ask
akka.util.Timeout
akka.util.ByteString
akka.stream.Materializer
akka.stream.ActorMaterializer
akka.stream.scaladsl.Sink
akka.stream.scaladsl.Source
akka.stream.scaladsl.Framing
akka.stream.scaladsl._
akka.stream.actor.ActorPublisher
akka.stream.actor._
akka.http.scaladsl.Http
akka.http.scaladsl.HttpExt
akka.http.scaladsl.model.ContentType
akka.http.scaladsl.model.HttpEntity
akka.http.scaladsl.model.StatusCode
akka.http.scaladsl.model.HttpHeader
akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
akka.http.scaladsl.model.HttpMethod
akka.http.scaladsl.model.HttpMethods
akka.http.scaladsl.model.HttpRequest
akka.http.scaladsl.model.HttpResponse
akka.http.scaladsl.model.RequestEntity
akka.http.scaladsl.model.StatusCodes
akka.http.scaladsl.model.Uri
akka.http.scaladsl.model.ws.WebSocketRequest
akka.http.scaladsl.model.ws.Message
akka.http.scaladsl.model.ws.TextMessage
akka.http.scaladsl.model.ws.BinaryMessage
akka.http.scaladsl.model.headers.BasicHttpCredentials
akka.http.scaladsl.model.headers.Authorization
akka.http.scaladsl.model._
akka.Done

## cats

cats.syntax.show._
cats.syntax.eq._
cats.syntax.either._
cats.syntax.contravariant._
cats.syntax.eq._
cats.syntax._
cats.syntax.functor._
cats.syntax.invariant._
cats.syntax.traverse._
cats.syntax.all._
cats.syntax.applicative._
cats.syntax.apply._
cats.syntax.foldable._
cats.syntax.list._
cats.syntax.semigroup._
cats.syntax.EitherOps
cats.syntax.option._
cats.syntax.flatMap._
cats.syntax.nested._
cats.kernel.Eq
cats.kernel.instances.all._
cats.kernel.instances.long._
cats.kernel.instances.float._
cats.kernel.instances.int._
cats.kernel.instances.list._
cats.kernel.instances.map._
cats.kernel.instances.string._
cats.kernel.instances.tuple._
cats.kernel.instances.vector._
cats.kernel.Semigroup
cats.kernel.laws.SerializableLaws
cats.kernel.laws.IsEq
cats.kernel.laws.discipline.SemigroupTests
cats.kernel.Order

cats.instances.all._
cats.instances.AllInstances
cats.instances.either.catsStdInstancesForEither
cats.instances.either.catsStdSemigroupKForEither
cats.instances.either._
cats.instances.int.catsKernelStdOrderForInt
cats.instances.string.catsKernelStdOrderForString
cats.instances.list._
cats.instances.string._
cats.instances.option._


cats.data
cats.effect
cats.laws
cats.Eq
cats.applicative
cats.defer
cats.functor
cats.implicits
cats.invariant
cats.syntax.eq._
cats.kernel.Eq
cats.Invariant
cats.data.Validated

cats.Show

cats.instances.float._

cats.instances.map._

cats.instances.tuple._
cats.instances.vector._

## com

com.azavea.franklin
com.azavea.stac
com.astrolabsoftware.graflink
com.astrolabsoftware.spark
com.monovore.decline
com.sparkcorr.geometry
com.typesafe.sslconfig
com.lightbend
com.amazonaws
com.github
com.zaxxer

## io

io.circe.generic
io.circe.syntax
io.circe.tests
io.circe.encoder
io.circe.decoder
io.circe.testing
io.circe.decodingfailure
io.circe.codec
io.circe.numbers
io.circe.parser
io.circe.export
io.circe.jsonnumber


io.chrisdavenport.log

## org

org.apache.spark
org.apache.log
org.scalacheck.prop
org.scalacheck.arbitrary
org.http
org.scalatest.matchers
org.scalatest._
org.scalatest.flatspec
org.scalatest.funsuite
org.scalatest.beforeandafterall
org.specs2.excecute
org.specs2.scalacheck
org.typelevel.discipline
org.reactivestreams.publisher
org.openjdk.
org.scalatestplus.scalacheck


## scala

scala.collection.immutable
scala.collection.mutable
scala.collection.mutable.builder
scala.collection.immutable.treemap
scala.collection.immutable.arrayseq
scala.collection.javaconverters
scala.collection.generic
scala.concurrent.duration
scala.concurrent.future
scala.concurrent.duration
scala.concurrent.executioncontext
scala.concurrent.promise

scala.util.try
scala.util.random
scala.util.failure
scala.util.success

scala.math.pi
scala.math.sin
scala.math.toradians
scala.math.log
scala.math.sqrt
scala.math.ceil
scala.math.exp
scala.math.pow

scala.language.experimental.macros

scala.annotation.tailrec

scala.reflect.macros
scala.deriving.mirror

## zio

zio.test.assertion
zio.test.defaultrunnablespec


zio.logging
zio.blocking.blocking
zio._



## doobie

doobie.util.transactor.transactor
doobie.util.transactor.strategy
doobie.util.read
doobie.util.write
doobie.implicits
doobie.free.connection.rollback
doobie.free.connection.setautocommit
doobie.free.connection.unit
doobie.postgres

## sttp

sttp.tapir.server
sttp.tapir.generic.auto
sttp.tapir.json.circe
sttp.tapir.server.http4s


sttp.client.nothingt
sttp.client.sttpbackend

sttp.model.statuscode
sttp.capabilities.fs2.fs2streams

## shaded

shaded.ahc.org.asynchttpclient

## shapeless

shapeless.tag
shapeless.labelledgeneric
shapeless.labelled.fieldtype
shapeless.test.illtyped
shapeless.witness

## eu

eu.timepit.refined.types.numeric
eu.timepit.refined.auto
eu.timepit.refined.types.numeric.nonnegint
eu.timepit.refined.types.string.nonemptystring

## geotrellis

geotrellis.vector
geotrellis.raster

## pureconfig

pureconfig.generic.auto
pureconfig.generic.producthint
pureconfig.generic.semiauto

## sbt

sbt._

## spark

spark.implicits

## autoderivedsuite

autoderivedsuite._

## gigahorse

gigahorse.support