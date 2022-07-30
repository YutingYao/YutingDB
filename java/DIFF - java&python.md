[常用的 jdk 包.md](https://github.com/YutingYao/YutingDB/blob/14ceb9fec6a746fc503fa92fe195aefcc055beb6/GeoBigData/JVM/%E5%B8%B8%E7%94%A8%E7%9A%84%20jdk%20%E5%8C%85.md)

[Java命名规范.md](https://github.com/YutingYao/YutingDB/blob/14ceb9fec6a746fc503fa92fe195aefcc055beb6/GeoBigData/JVM/Java%E5%91%BD%E5%90%8D%E8%A7%84%E8%8C%83.md)

[Java培训课程](https://space.bilibili.com/473751473)

[经典鸡翅](https://space.bilibili.com/386498238)

[码农小g](https://space.bilibili.com/567670)

[RudeCrab](https://space.bilibili.com/1924876999)

[杨博士Java学院](https://space.bilibili.com/389032749)

## public

如果a包下的`A类`是`public`的，它的字段和方法都是private的。

→ 在`b包`下的`B类`可以创建`A类`的对象，但是无法访问`A类对象的字段和方法`。

如果a包下的`A类``没有修饰符`，它的字段和方法都是private的。

→ 在`a包`下的B类可以创建`A类`的对象，但无法访问A类对象的字段和方法。

→ 在`b包`下的B类无法创建A类的对象。

## abstract

1、抽象类的修饰符必须为`public`或者`protected`, 不能是private, 因为抽象类需要`其子类去实现抽象方法`，private修饰就不能被子类继承，因此子类就不能实现改方法。
2、抽象类不能直接实例化，需要通过普通子类进行实例化。
3、如果子类`只实现了抽象父类中的一些方法`，那么该子类`任然是抽象类`（不能被实例化）。


## enum 和 switch 语句使用

[Java实例-enum和switch语句使用](https://www.bilibili.com/video/BV1yB4y1U7UE?spm_id_from=333.337.search-card.all.click&vd_source=9e1811a735189863812ffa0a3139abc6)

## void 

表示`method`不返回任何值。

## 为什么Java中的main方法必须是 public static void？

必须通过`main方法`才能启动java虚拟机

`main方法`没有被`实例化`过，这时候必须使用`静态方法`，才能被`调用`

## static

静态修饰符

静态方法独立于该类的any对象。 


```java
class ListNode {
    int val;
    ListNode next;
    ListNode(int x) {
        val = x;
        next = null;
    }
}

public class Solution {
    public boolean hasCycle(ListNode head) {
        
    }
}
```

```py
class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None

class Solution:
    def hasCycle(self, head: Optional[ListNode]) -> bool:
```

java的特点：

- java要求`文件名`和`公共类名`必须要求一致

```java
比如：`文件名` test.java -> `公共类名` public class test{}
```

- java的参数声明放在前面

```java
public String test(String param){
}
```

- 在传递多参数的时候，java用...

```java
public test(String args...){
  
}
```

- java异常:

```java
try{
  
}catch(RuntimeException e){
  
}catch(Exception e){
  
}finally{
  
}
```

- 导入一个包中所有的类，采用 *

```java
import java.lang.*
import java.util.ArrayList
import java.util.HashMap
```

- java中的`常量`：`const`
- java中`伴生对象`：`static关键字`
- java中的`trait`：接口`interface`

## synchronized 关键字

## String、StringBuffer 与 StringBuilder 之间区别

## HashMap、Hashtable、ConcurrentHashMap、LinkedHashMap、TreeMap

## 线程的创建方式

## 线程的状态转化

## 接口的幂等性