[toc]

# 1. scala的特性

换行符：分号

scala 没有所谓的基本数据类型，一切结尾对象

用“var”声明变量，用“val”声明常量

scala中，Function和Method分别翻译为函数和方法

# 2. scala vs java

## 2.1. 文件名

java要求**文件名**和**公共类**名必须要求一致，scala不要求。

```java
test.java -> public class test{}
```

```js
test.scala -> class xxx(任意){}
```

## 2.2. 关键字

1. scala 中没有**public关键字**，默认访问权限就是public
2. scala中没有**void关键字**，因为scala是完全面向对象的语言，所以采用特殊的对象来模拟：Unit

## 2.3. 方法和函数

1. 方法定义的参数顺序不同

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

2. scala的**方法**里面也可以声明**方法**:

```js
object test{
  def main(args:Array[String]):Unit = {
    def test(): Unit={
      
    }
  }
}
```

3. java中包含**方法重载** <br> 但scala中同一作用域不能有**同名函数**，即使函数参数不一致

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

5. scala 在函数定义时，可以给定默认参数。

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

## 2.4. 异常

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

## 2.5. 类和对象

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

但是不能声明**变量**和**函数(方法)**

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

## 2.6. import

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