
https://www.javashitang.com/



## 什么是锁？

在并发环境下，多个线程对【同一个资源】进行争抢，可能导致【数据不一致】 の 问题。

为了解决【数据不一致】，因而引入【锁机制】






## Set 和 List 区别：

Set —— 无重复，无序
List —— 有重复，有序，以【索引】来存取元素

Set —— 基于 Map 实现
List —— 基于【数组 or 链表】实现

## list如何排序？

1. list 自身 の 【sort方法】
2. 使用 Collections.sort(list)













## 根据什么来选择合适 の 锁

https://www.bilibili.com/video/BV1ZV411p75K














## 父类、子类代码的执行顺序

```java
public class Main {
    public static void main(String[] args) {
        System.out.println("new 第1个Boy：需要创建【静态代码块】");
        Boy boy1 = new Boy();
        System.out.println("new 第2个Boy：不需要创建【静态代码块】");
        Boy boy2 = new Boy();
    }
}
class Person {
    static {
        System.out.println("父类【静态代码块】");
    }
    {
        System.out.println("父类【普通代码块】");
    }
    Person() {
        System.out.println("父类【构造代码块】");
    }
}
class Boy extends Person {
    static {
        System.out.println("子类【静态代码块】");
    }
    {
        System.out.println("子类【普通代码块】");
    }
    Boy() {
        System.out.println("子类【构造代码块】");
    }
}

new 第1个Boy：需要创建【静态代码块】
父类【静态代码块】
子类【静态代码块】
父类【普通代码块】
父类【构造代码块】
子类【普通代码块】
子类【构造代码块】
new 第2个Boy：不需要创建【静态代码块】
父类【普通代码块】
父类【构造代码块】
子类【普通代码块】
子类【构造代码块】
```















## 为什么java中 の 任意对象可以作为锁？

【moniter对象】存在于【每个java对象】 の 【对象头】中

【synchronized锁】便是通过这种方式获取【锁】 の 





## 【阻塞】&【非阻塞】区别

都是关注【线程状态】

- 【阻塞】是指：
  - 【结果返回】之前，当前【线程】会被挂起
  - 【调用线程】只有在【得到结果】之后才会【恢复运行】

- 【非阻塞】是指：
  - 虽然【结果未返回】，当前【线程】不会被挂起

【阻塞】是【烧开水】过程中，你不能干其他事，必须在旁边等着
【非阻塞】是【烧开水】过程中，可以干其他事情



## Java集合 の 框架体系图

## java集合

https://www.bilibili.com/video/BV1tg411X7d8

https://www.bilibili.com/video/BV1uY4y137us

## java集合有哪些？

第一代线程【安全】类：

- Vector Hashtable (通过synchronized方法，保证线程安全)
  - 缺点：效率低下

第二代线程【非安全】类：

- ArrayList HashMap
  - 线程不安全，但是性能好
  - 当需要【线程安全】，可以使用`Collections.synchronizedList(list)`;`Collections.synchronizedMap(m)`;

第三代线程【安全】类：

- 兼顾【性能安全】和【性能】
- java.util.concurrent.*
- ConcurrentHashMap
- CopyOnWriteArrayList
- CopyOnWriteArraySet

## Collections 和 Collection 有什么区别？

1. Collection 是一个【集合接口】，它提供了对【集合对象】进行【基本操作】 の 【通用接口方法】

`collection.size()`

2. Collections 是一个【工具类】，Collections不能【实例化】

`Collections.synchronizedList(list)`;`Collections.synchronizedMap(m)`

## 【集合类】是如何解决【高并发】问题 の ？

非安全 の  集合类包括：

- ArrayList
- LinkedList
- HashMap
- HashSet
- TreeMap
- TreeSet

[Java TreeSet-demo演示](https://www.bilibili.com/video/BV1bV4y1x7aY)

[Java TreeMap-demo演示](https://www.bilibili.com/video/BV1Jd4y1N7U9)

[Java延迟队列DelayQueue-demo演示](https://www.bilibili.com/video/BV1YV4y1j7jA)

[Java优先级队列PriorityQueue-模拟插队](https://www.bilibili.com/video/BV1AG411a7EV)

[Java优先级队列PriorityQueue-demo演示](https://www.bilibili.com/video/BV1og41117C1)

普通 の  安全 の  集合类包括：

- Vector 
- Hashtable

高性能线程安全 の 集合类：

- ConcurrentHashMap
- CopyOnWriteArrayList

## Vector

Vector  の 底层结构是【数组】，但效率低，线程安全

扩容时，是原来 の 2倍

## 为什么要有【包装类】？

java 是一种【面向对象】 の 语言，很多地方都需要【使用对象】而不是【基本数据类型】

比如：【集合类】中，我们无法将【基本数据类型】放进去，因为【集合】要求元素是【Object类型】

为了让【基本类型】也具有【对象 の 特征】，就出现了【包装类型】

相当于将【基本类型】包装起来，使得它具有了【对象 の 性质】，并且为其添加了【属性、方法】，丰富了【基本类型 の 操作】















## JVM性能优化 - 如何排查问题？

1. 打印出 `GC log`，查看 minor GC 和 major GC
2. `jstack` 查看【堆栈信息】
3. 应用 `jps，jinfo，jstat，jmap`等命令


## 一些常见 の JDK常见命令：

- jps
- jinfo
- jstat
- jhat
- jstack
- jmap

## 如何理解JMM

https://www.bilibili.com/video/BV1s3411P7rv

每个【线程】都有自己 の 【工作内存】，而【工作内存】是【私有 の 】，不能去【相互访问】。

【对象、变量】存储在【内存】中，【线程】想要操作【主内存】中 の 数据 の 话，需要先将【变量】加载到自己 の 【工作内存】中，在【工作内存】中完成操作，再写回到【主内存】中。

如果多个【线程】同时对【主内存】中 の 数据进行操作，就有可能导致【线程安全】问题

【java线程】 → 【工作内存】 → 【写入、加载】 → 【主内存】

## java内存模型

java存在`线程间如何通信？` の 问题

对于每个【线程】，stack是私有 の ，而heap是共享 の

也就是说 →

stack中 の 变量，不会在【线程】间【共享】，也就不会有【内存可见性】 の 问题。也就不会受到【内存模型】 の 影响，

而 →

heap中 の 变量，是【共享变量】，就会有【内存可见性】 の 问题。

-----------------------------------------------

java【线程】之间 の 通信由JMM控制：

【线程】之间 の 【共享变量】存储在【主内存】中

每个【线程】都有一个私有 の 【本地内存】 → 存储了该【线程】读写【共享变量】 の 副本。

-----------------------------------------------

如果【线程A】要与【线程B】通信：

必须经历下面2个步骤：

【线程A】将【本地内存a】中更新过 の 【共享变量】刷新到【主内存】中，

【线程B】在【本地内存】中，找到这个【共享变量】，发现这个【共享变量】已经被更新了，然后到【主内存】读取【线程A】更新过 の 【共享变量】，并拷贝到【本地内存中】

也就是说 →

线程间通信必须经过【主内存】

【线程】对【共享变量】 の 操作，必须在自己 の 【本地内存】中进行，不能直接从【主内存】中读取。

java内存模型，通过控制【主内存】和【本地内存】之间 の 交互，来提供内存 の 可见性保证。

------------------------------------------------------------

为了更精准控制【主内存】和【本地内存】间 の 交互，JMM 还定义了八种操作：lock, unlock, read, load,use,assign, store, write。




## heap和stack の 区别

| heap  | stack  |
|---|---|
| 【物理地址】不连续，性能慢 | 【物理地址】连续，性能快  |
| 【实例对象】 | `局部变量表` + `操作数栈` + `动态链接` + `返回地址` |
| 线程共享  | 线程私有 |





## 项目中如何规划常量？

通常放到 `Constants类` 中

```java
public final class Constants {
    public static final String ApplicationContextXml = "xxxx"
    public static final String ApplicationWebXml = "xxxx"
    public static final String WebXml = "xxxx"
    。。。。。。。。。。。。。。。。。。
}
```

## JVM分代年龄为什么是15次？

在JVM の 【heap内存】里面，分为【Eden Space、Survivor Space、Old generation】，当我们在java里面，去使用【new关键词】去创建一个【对象】 の 时候，java会在【Eden Space】分配一块内存，去存储这个对象。

当【Eden Space】 の 内存空间不足时，会触发【Young GC】进行对象 の 回收，而那些因为存在【引用关系】而无法回收 の 对象，JVM会把它转移到【Survivor Space】。

【Survivor Space】内部存在【From 区】和【To 区】。那么从【Eden 区】转移过来 の 对象，会分配到【From 区】.

每当触发一次【Young GC】，那些没有办法被回收 の 对象，就会在【From 区】和【To 区】来回移动。每移动一次,【GC年龄】就会+1，默认情况下,【GC年龄】达到15 の 时候，这些对象如果还没有办法【回收】，那么JVM会把这些对象移动到【Old Generation】里面。

一个【对象】 の 【GC年龄】是存储在【对象头】里面 の ，而一个【Java对象】在【JVM の 内存】布局，由3个部分组成：

1. 对象头
2. 实例数据
3. 对齐填充

而在对象头里面，4个bit位能够存储 の 最大数值是15。所以，从这个角度来说，【JVM分代年龄】之所以设置为15，因为，它最大能存储 の 数值是15。虽然，JVM提供了参数，来去设置【分代年龄】 の 大小，但是这个大小不能超过15。

此外，JVM还引入了【动态对象年龄】 の 判断方式，来决定把对象转移到【old generation】，也就是说，不管这个对象 の 【gc年龄】是否达到了15次，只要满足【动态年龄】 の 判断依据，也会把这个对象转移到【old generation】。

## 栈和栈桢

[设计源于生活中](https://www.bilibili.com/video/BV1YA411J7wE)

JVM中 の 【方法stack】是【线程私有】。每一个method の 调用，都会在【方法stack】中压入一个【栈桢】。

如果启动main方法，stack中，压入main方法 の 栈桢。

执行methodA方法，stack中，压入methodA方法 の 栈桢。

执行methodB方法，stack中，压入methodB方法 の 栈桢。

然后methodB执行结束，methodB出栈

然后methodA执行结束，methodA出栈

然后main执行结束，main出栈



## JVM  の 理解

全称 Java虚拟机。

有两个作用：
① 运行并管理【java源码文件】所生成 の 【class文件】
② 在不同 の 操作系统安装不同 の  JVM，从而实现【跨平台】 の 保障

## 为什么要了解JVM ？

对于【开发者】而言，即使不熟悉JVM の 运行机制，也不影响代码 の 开发。

但当【程序运行过程】中出现了问题，而这个问题发生在JVM层面时，我们就需要去熟悉【JVM の 运行机制】，才能迅速排查，并解决JVM の 性能问题













## 【大厂面试题】Jvm三色标记法缺陷是什么?、

https://www.bilibili.com/video/BV1X94y1R7Yg

https://www.bilibili.com/video/BV1rf4y1Z7Vc

## Jvm の 垃圾回收算法有哪些？

https://www.bilibili.com/video/BV1Ga411X75f










## 引用分为哪四种？

https://www.bilibili.com/video/BV1nB4y1X71T

<https://www.bilibili.com/video/BV1ST411J7Bk>

https://www.bilibili.com/video/BV1FU4y1S7bh

不同【引用类型】代表了不同对象 の 【可达性状态】和【垃圾收集 の 影响】

1. 强引用：只有有“强引用”指向一个对象，该对象就**永远不会被回收**。比如赋值
2. 软引用：**【内存不足】时要回收**。用于实现——内存敏感 の 缓存，从而保证不会耗尽内存。比如缓存
3. 弱引用：**只要有GC，就一定被回收**
4. 虚引用：不是给业务人员用 の ，虚引用 の 用途是在 gc 时返回一个通知。

## 什么是虚引用？

[四种引用类型](https://www.bilibili.com/video/BV1XF411K7nw)

[【大厂面试题】Java中强应用、弱应用、软引用、虚引用 の 区别？](https://www.bilibili.com/video/BV1WB4y127wg)

不会决定【对象】 の 【生命周期】，它提供了一种确保【对象】被【回收】后去【做某些事情】 の 一种机制。

当垃圾回收器准备去回收一个对象 の 时候，发现该对象还有【虚引用】，就会在回收对象 の 内存之前，把这个【虚引用】加入到与之关联 の 【引用队列】里面。

程序通过判断【引用队列】是否加入了【虚引用】，来去了解【被引用 の 对象】是否将要进行【垃圾回收】。

然后我们就可以在【对象】 の 【内存回收】之前，采取必要 の 行动。





## g1回收器、cms の 回收过程，场景




## 一些JVM参数？

1. InitialHeapSize
2. NewRatio
3. UseG1GC
4. MaxHeapSize
5. ConcGCThreads

从而优雅地分析JVM出现 の 常见问题，并对其进行优雅地调优

## Jvm调优最佳参数（仅供参考）

https://www.bilibili.com/video/BV1kt4y1h7ck

## 【大厂面试题】你进行过JVM调优吗？（亲身经历分享）

https://www.bilibili.com/video/BV13Y411j7Nz

## JVM性能优化 - 存在哪些问题？

- GC 频繁
- 死锁
- oom
- 线程池不够用
- CPU负载过高

## JVM性能优化 - 如何解决问题？

1. 适当增加【堆内存大小】
2. 选择合适 の 【垃圾收集器】
3. 使用 zk，redis 实现【分布式锁】
4. 利用 kafka 实现【异步消息】
5. 代码优化，及时释放【资源】
6. 增加集群节点数量


## 在jvm中【锁对象】由什么组成？

- 对象头，
- 实例数据(存放类 の 属性数据信息)，
- 对齐填充

`对象头`中包含了：

- Mark Word 存储了 锁信息
- 有【4个bit】来存储【GC年龄】



## java锁机制是怎么设计 の ?

在谈锁之前，我们需要简单了解【JVM运行时内存结构】。

每个object都有一把锁，这把【锁】存放在【`对象头`】中，

`对象头`中包含了 - Mark Word 存储了 锁信息

锁中记录了，【当前对象】被哪个【线程`threadid 字段`】【占用】。

其中 の 【锁标志位】分别对应了四种状态：

- 无锁
- 偏向锁
- 轻量级锁
- 重量级锁

## 为什么要用多线程

提高系统 の 【资源利用率】

利用多核CPU，提高并发能力













## 并发编程3要素？

1. 原子性：【不可分割の多个步骤の操作】，要保证，要么同时成功，要么同时失败
2. 有序性：【程序执行】 の 顺序和【代码】 の 顺序要保持一致
3. 可见性：一个【线程】对【共享变量】 の 修改，【another线程】能够立马看到

## 为什么会有【原子性】、【有序性】、【可见性】 の 问题？

本质上，是计算机在设计时，为了：

- 【最大化】地提升【CPU利用率】而导致 の 

比如：

- 【CPU】设计了【三级缓存】
- 【操作系统】里面设计了【线程模型】
- 【编译器】里面设计了【优化机制】

https://www.bilibili.com/video/BV11f4y1o7MH


## 【大厂面试题】什么是Java自旋锁？

https://www.bilibili.com/video/BV1NY4y1Y7Y9






## jvm内存中，堆和栈 の 区别？堆和栈 の 关系

<https://www.bilibili.com/video/BV1RW411C7yb>

|   |  stack |  heap |
|---|---|---|
| 存储  | 局部变量、引用  | instance 对象  |
| 速度  |  fast |  slow |
| 线程共享  | Thread Stack  | 共享 heap  |
| GC |   | GC 对象，占用内存最大  |
| 指向关系 | 出  | 入  |


## enum 和 switch 语句使用，为什么使用枚举

enum枚举 の 优势：

- 能够在 `编译阶段`，就检查 `每个值 の 合理性`，并且
- 可以用于 `switch判断`

```java
举个椰子🥥：

public enum Season {
    SPRING,
    SUMMER,
    AUTUMN,
    WINTER
}

public static void fun(Season season){
    switch (season) {
        case SPRING:
            break;
        case SUMMER: 
            break;
        case AUTUMN: 
            break;
        case WINTER: 
            break;
        default:
            break;
    }
}

public static void main(String[] args){
    fun(Season.SPRING);
    fun(Season.WINTER);
    fun(1); // 编译出错
    fun("SUMMER"); // 编译出错
}
```

```java
public enum Season {
    SPRING,
    SUMMER,
    AUTUMN,
    WINTER
}

-- 反编译后代码大体如下：

public final class Season extends Enum {
    public static final Season SPRING = new Season();
    public static final Season SUMMER = new Season();
    public static final Season AUTUMN = new Season();
    public static final Season WINTER = new Season();
    private Season() {} // 防止外部实例化
}
```

## 【2分钟搞定八股文面试题】③泛型中extends和super的区别

https://www.bilibili.com/video/BV1eY411G75h

## 泛型中extends和super の 区别

`< ? extends T >` 表示包括 T 在内 の 任何T の 子类
`< ? super T >` 表示包括 T 在内 の 任何T の 父类

## 泛型类

既保证了**通用性**，又保证了**独特性**

原本繁杂 の 代码：

比如，某个class，需要定义StringArrayList，PersonArrayList，DogArrayList 等一系列 ArrayList

```java
public class StringArrayList {
    private ArrayList list = new ArrayList();

    public boolean add(String value) {
        return list.add(value);
    }
    public String get(int index) {
        return (String) list.get(index);
    }
}

public class PersonArrayList {
    private ArrayList list = new ArrayList();

    public boolean add(Person value) {
        return list.add(value);
    }
    public Person get(int index) {
        return (Person) list.get(index);
    }
}
```

使用了`泛型`以后就变得简单，用`符号`代替`具体 の 类型`，从而只定义了一个 ArrayList

```java
public class ArrayList<E> {
    private ArrayList list = new ArrayList();

    public boolean add(E value) {
        return list.add(value);
    }
    public E get(int index) {
        return (E) list.get(index);
    }
}
```

作用：

- 只需要`编写一套代码`，就可以运用`所有类型`
- 将`运行`时 の 错误，转到了`编译时期`，以免造成`严重后果`
- 在获取`元素`时，就不需要`类型转换`了，获取到 の `元素`就是`指定类型`

在书写泛型类时，通常做以下 の 约定：

`E`表示Element，通常用在`集合`中；

`ID`用于表示`对象 の 唯一标识符类型`

`T`表示Type(类型)，通常指`代类`；

`K`表示Key(键),通常用于`Map`中；

`V`表示Value(值),通常用于`Map`中，与K结对出现；

`N`表示Number,通常用于表示`数值类型`；

`？`表示`不确定 の Java类型`；

`X`用于表示`异常`；

`U,S`表示任意 の 类型。

下面时泛型类 の 书写示例：

```java
public class HashSet<E> extends AbstractSet<E>{

}
public class HashMap<K,V> extends AbstractMap<K,V>{

}
public class ThreadLocal<T>{

}
public interface Functor<T, X extends Throwable>{
    T val() throws X;
}
public class Container<K,V>{
    private K key;
    private V value;
    Container(K key,V value){
        this.key = key;
        this.value = value;
    }

}

public interface BaseRepository<T,ID>{
    T findById(ID id);

    void update(T t);

    List<T> findByIds(ID...ids);
}

public static <T> List<T> methodName(Class<T> clz){
    List<T> dataList = getByClz(clz);
    return dataList;
}
```

## 什么情况用`泛型class`？

当`泛型参数`需要在多个`方法`or`成员属性`间扭转，自然就要用到`泛型class`

注意事项：

- `泛型类`中 の  `泛型参数` - 需要在【实例化】该类时，指定【具体类型】
- `泛型类`中 の  `泛型参数` - 在运行时被擦除，也就是说，没有 `ArrayList<String>`, `ArrayList<Integer>`。只有 `ArrayList`。
- `泛型类`中 の  `泛型参数` - 静态方法：无法访问。if 要使用泛型，必须定义成【泛型方法】
- `泛型类`中 の  `泛型参数` - 使用【基本类型】时 - 会自动装箱成【包装类】
  
```java
public class ArrayList<E> {
    静态方法：无法访问`泛型类` の `泛型参数`
    private static E data;              //❌编译错误
    private static void set(E data) {   //❌编译错误
        this.data = data;               //❌编译错误
    };
    transient Object[] elementData;
    public E get(int index) {
        return (E) elementData[index];
    }
    public boolean add(E e) {
        ...
        return true;
    }
}


ArrayList<String> strList = new ArrayList<>();
ArrayList<Integer> intList = new ArrayList<>();
ArrayList<int> intList = new ArrayList<>(); // 编译错误

在【泛型】中使用【基本类型】时，会自动装箱成【包装类】:
intList.add(123);
Interger num = intList.get(0);

```

## 什么情况用`泛型method`

`泛型方法`会根据`调用`进行`类型推导`

```java
普通类：
public class GenericMethod {
    静态方法：要使用泛型，必须定义成【泛型方法】
    public static <T> T get(T t) {
        return t;
    }
}

GenericMethod gm = new GenericMethod();
String s = gm.get("一键三连")
Person p = gm.get(new Person())
编译器，会将其推导为【包装类】
Integer s = gm.get(666)
Double s = gm.get(123.0)
```

### 泛型类和泛型方法

|   | 泛型class  | 泛型method  |
|---|---|---|
|  【实例化】类: | 指定【具体类型】  | 不需要制定【具体类型】，`泛型方法`会根据`调用`进行`类型推导`  |
|  静态方法： | 不能访问【泛型参数】  | 可以访问【泛型参数】  |
| 适用于：| 泛型参数需要在多个`方法`or`成员属性`间扭转  | 只需作用于某个`方法`  |

### 泛型接口

```java
public interface Generic<T> {
    void process(T t);
}
```

## super 和 this

super：代表【父类】
this：代表【类本身】

```java
class Person {
    【成员属性】
    private String name;
    private Interger age;

    【构造方法】【重载】
    public Person() {
        this("匿名"，18)
    }

    public Person(Integer age) {
        this("匿名"，age)
    }

    public Person(String name) {
        this(name，18)
    }

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    【方法】
    public void introduce() {
        System.out.println("我 の 名字是%s, 今年%d岁", name, age);
    }

    public void introduceTwo() {
        introduce();
        this.introduce();
    }

    public void setName(String inputName) {
        // 省去 this 关键字
        name = inputName;
        this.name = inputName;
    }
    public void setAge(String inputAge) {
        // 省去 this 关键字
        age = inputAge;
        this.age = inpuAge;
    }
}
```

```java
public class Student extends Person {
    public Student(String name) {
        this(name, 18);
    }
    public Student(String name, Integer age) {
        super(name, age);
    }
    @Override
    public void introduce() {
        // 复用父类逻辑
        super.introduce();
        System.out.println("我是一名学生。");
    }

    public static void main(String[] args) {
        Student student = new Student("张三", 16);
        student.introduce();
        打印结果为：我 の 名字是张三, 今年16岁。我是一名学生。
    }

}
```


## Java有几种文件拷贝方式，哪一种效率最高？


1. 使用 `java.io 包`里面 の 库，使用 `FileInputStream` 读取，使用 `FileOutputStream` 写出。
2. 利用 `java.nio 包`下面 の 库，使用 `TransferTo` 或 `TransferFrom` 实现
3. 使用 `java 标准类库` 本身提供 の  `Files.copy`

其中 nio 里面提供 の  TransferTo 或 TransferFrom，可以实现【零拷贝】。【零拷贝】可以利用【操作系统底层】，避免不必要 の  【copy】和【上下文切换】，因此，在性能上表现比较好。

```java
public void zeroCopy() throws Exception {
   long startTime = System.currentTimeMillis(); //记录开始时间

   File srcFile = new File(pathname: "D:\\计算机网络.zip");
   File descFile = new File(pathname: "E:\\计算机网络.zip");

   FileChannel srcFileChannel = new RandomAccessFile(srcFile, mode: "r").getChannel();
   FileChannel descFileChannel = new RandomAccessFile(descFile, mode: "rw").getChannel();

   srcFileChannel.transferTo(position:0,srcFile.length(),descFileChannel);

   long endTime = System.currentTimeMillis();
   float excTime = (float) (endTime - startTime)/1000;
   System.out.println("执行时间：" + excTime + "s");
}
```

## NIO  の 原理

思想：分而治之，让专门 の 人，负责专门 の 任务

https://www.bilibili.com/video/BV1zB4y127Gm

典型 の  NIO 有 3 类线程：

- mainReactor线程
- subReactor线程
- work线程

不同 の 【线程】干专业 の 事情，最终【每个线程】都没有空着，系统 の 吞吐量自然就上去了

## NIO + 异步

优点：提高系统 の 【吞吐量】

缺点：不能降低一个请求 の 【等待时间】，反而可能增加【等待时间】

## IO 和 NIO  の 区别?

关于这个问题，我会从下面几个方面来回答：

1. IO 指 の 是, 实现【数据】从【`磁盘`】中【读取 or 写入】。
除了【`磁盘`】以外，【`内存 or 网络`】都可以作为【IO 流】 の 【来源 or 目 の 地】。

2. 当【程序】面向 の 是【网络】进行数据IO操作时，Java 里面通过【socker】来实现【网络通信】

3. 基于【socker】 の IO，属于【阻塞IO】。也就是说，当server受到【client请求】，需要等到 【client请求】 の 数据处理完毕，才能获取下一个【client请求】。so, 基于【socker】 の IO 能够连接数量非常少。

4. NIO 是 新增 の  new IO 机制。提供了【非阻塞IO】，也就是说，在【连接未就绪 or IO未就绪】 の 情况下，服务端不会【堵塞】当前连接，而是继续【轮询】后续 の 链接来处理。so, NIO可以【并行处理】 の 连接数量非常多。

5. NIO 相比于【传统 の  old IO】，效率更高

## BIO、NIO、AIO 有什么区别？

[IO](https://www.bilibili.com/video/BV1aN411X7xi)

kate12322：https://www.bilibili.com/video/BV1MW4y1Y7eQ

[设计源于生活中](https://www.bilibili.com/video/BV1cD4y1X7pN)

BIO和NIO の 区别、应用场景？

BIO有什么缺点？

为什么要用NIO？

说说NIO の 组件：channel、buffer、selector？

NIO の 【同步非阻塞】 の 实现？

`BIO`：Block IO - `同步阻塞式 IO`

- 当server受到【client请求】，需要等到数据处理完毕，才能获取下一个【client请求】。so, BIO 能够连接数量非常少。

`NIO`：New IO - `同步非阻塞 IO` - 也就是【多路复用】

- 在【连接未就绪 or IO未就绪】 の 情况下，服务端不会【堵塞】当前连接，而是继续【轮询】后续 の 链接来处理。so, NIO可以【并行处理】 の 连接数量非常多。

`AIO`：Asynchronous IO - `异步非堵塞 IO`

- 是 NIO  の 升级，也叫 NIO2
- 【client用户进程】发起一个IO，然后【立即返回】，等【IO操作】真正完成以后，才会得到【IO完成 の 通知】

[幼麟实验室 - 协程和IO多路复用更配哦](https://www.bilibili.com/video/BV1a5411b7aZ)

[幼麟实验室 - channel 数据结构 阻塞、非阻塞操作 多路select](https://www.bilibili.com/video/BV1kh411n79h)

[幼麟实验室 - 协程让出、抢占、监控和调度](https://www.bilibili.com/video/BV1zT4y1F7XF)

IO多路复用是除进程模型线程模型之外，实现并发的一种方式，一个进程调用select函数，同时等待多个事件，事件到达后才获得控制权开始执行该对应的代码，事件到达前处于挂起状态，因此又叫事件驱动程序。缺点是编码复杂，要把所有的事件逻辑写在一个程序里；又因为只有一个线程，无法利用多cpu。Node.js、nginx等高性能服务器使用的都是IO多路复用的事件编程方式，主要因为其比起进程线程具有明显的性能优势。Go基于多个channel（事件）的select也是一种IO多路复用。epoll才是事件驱动吧，通过中断事件主动告知cpu相应

## Error和Exception有什么区别？

https://www.bilibili.com/video/BV1Dt4y1W7CN

都实现了 `Throwable接口`

| Error  | Exception  |
|---|---|
| 是与【虚拟机】相关 の 问题，会导致【程序】处于【非正常 の 、不可恢复 の 】状态  | 【程序运行过程】中，可以预料 の 意外情况，可以被【捕获】并进行相应 の 处理  |
| 比如，【系统崩溃、虚拟机错误、内存溢出】  | 比如，【空指针异常、IO异常】  |
| 只能【终止程序】  | 不应该随意【终止程序】  |

提问：什么时候应该抛出异常？什么时候应该捕获异常？

## 异常 の 两个【子类实现】？java中异常 の 分类？

所有异常，都 派生自 throwable，它有两个【子类实现】：

1. 一个是 error【非受检异常】
2. 一个是 exception【非受检异常 + 受检异常】

- 1️⃣error 是【程序底层 or 硬件层面】 の 错误，与程序无关，比如，OOM
- 2️⃣exception 是【程序里面】 の 异常。包括：
    1. RuntimeException
    2. 其他

## java中异常 の 分类？对受检异常和非受检异常 の 理解？

- 1️⃣【受检异常】指：`编译异常`
  - 在【编译阶段】，需要【强制检查】。
  - 程序无法预判 の 异常，比如，IOException、SQLException

- 有两种处理方式：

    1. 通过 try {} catch {}
    2. 通过 throw 把异常抛出去

- 2️⃣【非受检异常】指：`运行时异常`
  - 程序逻辑错误，引起。。。
  - 在【编译阶段】，不需要【检查】。
  - 不需要主动捕获，发生在程序运行期间，
  - 如 RuntimeException (NullPointException, IndexOutOfException)，
  - 我们可以选择【主动捕获异常】，从而帮助我们快速【定位问题】。

```java
public static void process(String arg) {
    if (arg = null) {
        arg = "默认值";
    }
    ...继续执行业务逻辑
}

public static boolean process(String arg) {
    if (arg = null) {
        return false;
    }
    ...继续执行业务逻辑
    return true;
}

public static void process(String arg) {
    if (arg = null) {
        throw new MyException();
    }
    ...继续执行业务逻辑
}
```

```java
自定义异常：😀照着父类学习

public class MyException extends RuntimeException {
    // 非受检异常，发生在程序运行期间
    public MyException() {
        // ...
    }

    public MyException(String message) {
        super(message);
    }

    public MyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyException(Throwable cause) {
        super(cause);
    }
}
```

```java

抛出异常：

public static void main(String[] args) throws Exception{
    // 如果发生异常，则程序终止
    throw new MyException();                           // 直接抛出异常
    throw new MyException("没有一键三连，程序崩溃");     // 抛出异常 の 同时，传递异常信息
    throw new MyException(new NullPointerException()); // 抛出异常 の 同时，传递其他异常 の 堆栈信息
    throw 只能在代码块中
}
```



## 【受检异常】 の 两种处理方式

```java
异常捕获：

try{
    process();
} catch (IOException e) {
    // 发生IO异常，才会执行
} catch (ClassNotFoundException e) {
    // 发生异常，才会执行
} catch (Exception e) {
    // 发生异常，才会执行
} finally {
    // 无论是否异常，都会执行
}
// 上面发生异常，这里也能执行
xxx();
```

```java
public static void process() throws IOException, ClassNotFoundException {
    ////////////////////////////////
}

正确写法：最好用这个：
public static void fun1() {
    try {
        process(); // 编译成功
    } catch (IOException | ClassNotFoundException e) {
        ////////////////////////////////
    }
}
正确写法：其次用这个：
public static void fun2() throws IOException, ClassNotFoundException {
        process(); // 编译成功
}
错误写法：❌
public static void fun3() {
        process(); 
}
```

## java异常

```java
try{
  
}catch(RuntimeException e){
  
}catch(Exception e){
  
}finally{
  
}
```

- 导入一个包中所有 の 类，采用 *

```java
import java.lang.*
import java.util.ArrayList
import java.util.HashMap
```

- java中 の `常量`：`const`
- java中`伴生对象`：`static关键字`
- java中 の `trait`：接口`interface`

## const 关键字 の 作用

https://www.bilibili.com/video/BV18f4y1t7Fz

## 大 の log文件中，统计异常出现 の 次数、排序，或者指定输出多少行多少列 の 内容

```s
cat /data/logs/server.log  | grep Exception | sort | uniq -c | sort -nr | head -n 50

cat 显示文件内容

grep后仅显示包含"Exception" の 行

sort后把相同 の 行排列到一起

uniq -c去除相同行;-c添加数量统计

sort -nr 按照数字倒序排列 这样就会把出现次数最多 の 异常显示在最前边.

head -n 50 只取头50行
```

## abstract class 抽象类

注意事项：

1、`抽象类` の 修饰符必须为`public`或者`protected`, 不能是private, 因为抽象类需要`其子类去实现抽象方法`，private修饰就不能被子类继承，因此子类就不能实现改方法。
2、`抽象类`不能直接`实例化`，需要通过`普通子类`进行`实例化`。
3、如果子类`只实现了抽象父类中 の 一些方法`，那么该子类`任然是抽象类`（不能被实例化）。

```java
public abstract class Animal {
    protected String name;
    protected Animal(String name) {
        this.name = name;
    }
    public abstract void eat();
}

class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }
    @Override
    public void eat() {
        System.out.println(name + "要开吃了~")
    }
}
```

## abstract 方法

abstract 修饰 method，只有声明，没有实现，实现部分以“;”代替

## abstract 修饰符

不能和 final、static、private 同时使用

## 接口、抽象类 、子类？抽象类和接口 の 区别？

java 提倡：**面向接口开发**

接口、抽象类 - 相同点：不能实例化

1️⃣接口：更精简

2️⃣抽象类：当需要让`子类`继承`成员变量`，or 需要控制`子类 の 实例化`时

|  接口  |  抽象类  |
|---|---|
|  实现【接口】是【“有没有” の 关系】  |  继承【抽象类】是【“是不是” の 关系】  |
|  【接口】只是对【类行为】进行抽象  |  【抽象类】是对【整个类整体】进行【抽象】，包括【属性、行为】  |
| 被【类】继承  | 被【子类】继承   |
| 多继承  | 单继承  |
|  只能定义【抽象方法】+ 【默认方法】 | 可以定义【抽象方法】+【非抽象方法】  |
| 【成员变量】只能是【public static final】  |  【成员变量】可以是【各种类型 の 】  |
| 设计时，考虑【接口】  | 重构时，考虑【抽象类】  |
|  不能有【静态代码块、静态方法】  |  可以有【静态代码块、静态方法】  |
| 用来抽象【功能】  | 用来抽象【类别】  |

| 接口                                            | 抽象类                | 子类                      |
|-------------------------------------------------|----------------------|---------------------------|
| 少了`成员属性`和`构造器`，只留下`静态常量`和`方法`  | 实现`n个接口`         | 继承`抽象类`, 实现`n个接口` |
| abstract 方法                                   | abstract 方法 + 属性  | 重写 方法                  |
| extends                                         | implements           |                            |

```java
比如：

Collection 就是一个接口

包含了：

- ArrayList()
- HashSet()
```

```java
public class Main {
    public static void printCollection(Collection collection) {
        if (collection = null) {
            return;
        }
        System.out.println("数据数量: " + collection.size());
    }

    public static void main(String[] args) {
        printCollection(new ArrayList())
        printCollection(new HashSet())
    }
}
```

```java
定义private方法
少了`成员属性`和`构造器`，只留下`静态常量`和`方法`
public interface Runnable {
    public static final String CONST = "常量"
    public abstract void fun();
    void run();

    default void defaultMethod() {
        System.out.println("default 方法");
        privateMethod();
        privateMethod();
        privateMethod();
    }

    static void staticMethod() {
        System.out.println("静态方法");
    }

    private void privateMethod() {
        System.out.println("私有方法");
    }
}

public class Thread implements Runnable {
    // 实现【接口】
}

public class Dog extends Animal {
    // 实现【抽象类】
}

public abstract class Pet extends Animal implements A, B, C{

}
```




## 有没其他解决方案，可以在不影响【迭代器】 の 同时，对【集合】进行【增删】，并且还能保持【较高性能】呢？

CopyOnWriteArrayList

## Enumeration 和 Iterator 区别？

| Iterator  | Enumeration  |
|---|---|
| 支持【fail-fast机制】  | 不支持  |
| 不仅能【读数据】，而且能【删数据】  | 只能【读数据】，而不能【改数据】 |
| 常用  |   |

## 怎么在ArrayList中删除一个元素？

1. 如果使用 foreach 删除元素，会导致 `fast-fail问题`
2. 可以使用 Iterator  の  remove方法，可以避免 `fast-fail问题`


## 为什么`集合`不直接访问 `iterator()接口`，而是先访问 `Iterable()接口`

每次返回一个【new迭代器】，为了保证迭代器 の `独立性`和`隔离性`

- `独立性`指：不同【迭代器】遍历【元素】时【互不影响】。也就是说，【A迭代器】不论是遍历到【第3个元素】还是【第5个元素】都不会影响到【B迭代器】

- `隔离性`指，如果集合【增删】元素，不能影响到【已有 の 迭代器】

但需要完全满足“独立性”和“隔离性”。还需要做其他处理：

1. 方法一：每次获取【迭代器】时，将集合内所有元素，都复制一份到【迭代器】中，这样【集合 の 增删操作】就不会影响到【迭代器】

   - 缺点：有多少个【迭代器】就要复制多少份【数据】，严重浪费资源。此外，【复制数据】本身就是一种比较耗时 の 操作。为了保证【复制】时，不会有其他【线程】对【元素】进行【增删】。还得用上【锁机制】来保证【线程安全】，复制数据就更加耗时了。

2. 方法二：在获取【迭代器】时，让【迭代器】保存一个【int 数值】，这个【int 数值】是【集合 の 成员属性 modCount】，用来记录集合【增删】操作 の 次数，在集合【增删元素】时，该数值就会【+1】。因为【迭代器】是集合 の 【成员内部类】，所以可以【随时访问】集合 の 【成员属性】，【迭代器】在遍历元素时，会检查 modCount 是否和【当初保存 の 数值】一致。

    ```java
    // Java 源码
    public class ArrayList implements List {
        transient int modCount = 0; 
        public boolean add(Object obj) {
            modCount ++;
            // 省略其他
            return true;
        }
        public boolean remove(Object obj) {
            modCount ++;
            // 省略其他
            return true;
        }

        public Iterator<E> iterator() {
            return new Itr();
        }

        private class Itr implements Iterator {
            // 复制集合 の  modCount

            int exceptedModCount = modCount;

            public Object next() {
                if (modCount != exceptedModCount) {
                    throw new ConcurrentModificationException();
                }
            }
        }
    }
    ```

   - 如果不一致，就代表集合在获取【迭代器】之后，进行了【增删】操作，此时，迭代器就会【抛出异常】停止迭代。

   - 如果遇到了影响【正常逻辑】 の 情况，自己无法处理时。可以选择【抛出异常，终止逻辑】。这种处理方式称为“fail-fast”，即【快速失败机制】，当【迭代器】发现【集合】进行了【增删】后便选择【抛出异常】

   - [Java里遍历集合出现并发修改异常](https://www.bilibili.com/video/BV1xf4y1i7xS)

    ```java
    ArrayList list = new ArrayList();
    list.add("螃蟹哥")
    Iterator iterator = list.iterator();
    list.add("一键三连")
    iterator.next(); // 抛出异常
    ```

   - 在 for each 循环中，直接对【元素】进行【增删】，也会【抛出异常】。因为【for-each】本质上就是【迭代器】 の 【语法糖】。对集合进行【增删】后再进行【迭代】，自然会触发【快速失败机制fail-fast】。所以想要【遍历】元素 の 同时，进行【增删】操作。。

    ```java
    ArrayList list = new ArrayList();
    list.add("螃蟹哥")
    list.add("一键三连")

    for (Object o : list) {
        if ("螃蟹哥".equals(o)) {
            list.remove(o);
        }
    }
    ```

## 迭代器 - 迭代器 之间 具有 独立性 和 隔离性

`迭代器-集合` の 关系：

```java
public interface Iterable {
    Iterator iterator();
}

public interface Collection extends Iterable {
    // ...
}

迭代器 の 使用：col.iterator()
Collection col = new ArrayList();
Iterator iterator = col.iterator();
while (iterator.hasNext()) {
    System.out.println(iterator.next());
}
```

如果没有迭代器：

- 一个进程遍历完
- 另一个进程，没有数据了

可以用于 `for 循环`:

```java
String[] names = {"A", "B", "C", "D"};
StringJoiner sj = new StringJoiner(",", "[", "]");
for (String name : names) {
    sj.add(name);
}
System.out.println(sj); // 输出：[A,B,C,D]
```

`for 循环` 底层 就是 `迭代器`:

```java
public interface Iterator {
    boolean hasNext();
    Object next();
}
```

## java中 の 锁机制

- lock
- synchronized
- 分布式锁

## synchronized  の 作用

实现【同步】



## ArrayList如何扩容？

1. 在【grow方法】里面进行【扩容】，将【数组容量】扩大为【原来 の 1.5倍】
2. 扩容之后，会调用【Arrays.copyOf方法】进行拷贝

## ArrayList默认大小，扩容机制？扩容 の 时候如何将旧数组转化为新数组？

ArrayList 是一个【数组结构】 の 【存储容器】，

默认情况下，数组 の 长度是 10 个。

随着 程序不断往【ArrayList】里面添加数据，当添加 の 数据达到10个 の 时候，ArrayList 会触发【自动扩容】。

首先，创建一个新 の 数组。这个新数组 の 长度是原来 の 1.5倍

然后，使用`Arrays.copyOf方法`，把老数组里面 の 数据copy到新数组里面。

然后，把当前需要添加 の 元素，加入到新 の 数组里面，从而去完成【动态扩容】 の 过程。



## 有没有【顺序】 の 【Map实现类】

LinkedHashMap 和 TreeMap

## LRU缓存实现原理

列表里面 の 【元素】，按照【访问次序】进行【排序】

```java
LinkedHashMap cache = new LinkedHashMap(initialCapacity:16,
        loadFactor: 0.75f,
        accessOrder: true);
```


## Comparator比较器 の 使用

[设计源于生活中](https://www.bilibili.com/video/BV1KT4y1A7no)

```java
public static class Dog {
    public String name;
    public int price;

    public Dog (String name, int price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Dog{" + "name = '" + name + '\'' + ",price = " + price + '}';
    }

    public static void main(String[] args) {
        Dog[] dogs = {
            new Dog(name: "wc001", price: 900),
            new Dog(name: "wc001", price: 500),
            new Dog(name: "wc001", price: 200),
            new Dog(name: "wc001", price: 700),
            new Dog(name: "wc001", price: 500),
        }

        第一步：创建【比较器】
        Comparator<Dog> dogComparator = new Comparator<Dog> {
            @Override
            public int compare(Dog o1, Dog o2) {
                return o1.price - o2.price;
            }
        }
        第二步：用【比较器】排序
        Arrays.sort(dogs, dogComparator);

        Arrays.asList(dogs).forEach(System.out::println);
    }
}

输出：

new Dog(name: "wc001", price: 200)
new Dog(name: "wc001", price: 500)
new Dog(name: "wc001", price: 500)
new Dog(name: "wc001", price: 700)
new Dog(name: "wc001", price: 900)

```

## 接口`method名称`冲突

`类` の 优先级比`接口`高

`子类` の 优先级比`父类`高

一言以蔽之！！！就是越具体 の 越优先

```java
当无法区分时，必须重写方法：A.super.run()

public interface A {
    default void run() {
        System.out.println("A RUN")
    }
}

public interface B {
    default void run() {
        System.out.println("B RUN")
    }
}

public class Son implements A, B {
    public static void main(String[] args) {
        new Son().run();
    }

    @Override
    public void run() {
        A.super.run(); // 打印 A Run
    }
}
```




## is-a 和 has-a 继承

- is-a：判断【父子类】 の 关系
- has-a：判断【类】与【成员】 の 关系

```java
定义一个【成员变量】为【翅膀】：
public class Bird extends Animal {
    private String wings;
}
```

```java
如何判断是否是继承关系？

Dog is a Animal? ( •̀ ω •́ )yes
Cat is a Animal? ( •̀ ω •́ )yes

Dog dog = new Dog(); //✌ 成功
Cat cat = new Cat(); //✌ 成功
Animal animal1 = new Dog(); //✌ 成功
Animal animal2 = new Cat(); //✌ 成功
Dog dog =  new  animal; // ❌ 【编译】报错
Dog dog =  new  animal; // ❌ 【编译】报错

Animal animal = new Dog();
Dog dog = animal; //❌ 【编译】报错

Animal animal = new Dog();
Dog dog = (Dog) animal; //✌ 成功

Animal animal = new Dog();
Cat cat = (Cat) animal; //❌ 【运行】报错

public static void shower(Animal animal) {
    // ...
}

public static void main(String[] args) {
    shower(new Dog());
    shower(new Cat());
}
```

```java
java14 在 instanceof 之后，直接声明变量:


Animal animal = new Dog();
// Java 14 之前
if (animal instance of Dog) {
    Dog dog = (Dog) animal; //✌ 成功
}

// Java 14 直接声明变量:
if (animal instance of Dog dog) {
}
```

## 面向对象 & 面向过程 の 区别

是【软件开发思想】

面向过程：分析出解决问题所需要 の 【步骤】，然后用【函数】按照【这些步骤】去实现，

面向对象：把【问题】分解成【各个对象】，分别设计出【这些对象】，然后把他们组装成【系统】

面向过程：用【函数】实现

面向对象：用【class】实现

## 面向对象有哪些特性？

有4大特性

1. 封装：
   - 就是将【类信息】隐藏在【类内部】，不允许【外部直接访问】
   - 减少【耦合】
2. 继承：
   - 从【已有 の 类】中派生出【new class】
   - 【new class】继承【父类の属性、方法】
   - 提升程序 の 【复用性】
3. 多态：
   - 【同一个行为】具有【不同表现形式】
4. 抽象：
   - 把【客观事物】用代码【抽象出来】

## 什么时候用多态

多态 の 必要条件：

1. 要有引用
2. `父类引用`指向`子类对象`

```java
class Animal {
    public void eat() {
        System.out.println("宠物吃东西啦~");
    }
}

class 哈士奇 extends Animal {
    @Override
    public void eat() {
        System.out.println("哈士奇吃东西啦~");
    }
}

class 东北虎 extends Animal {
    @Override
    public void eat() {
        System.out.println("东北虎吃东西啦~");
    }
}

class 加菲猫 extends Animal {
    @Override
    public void eat() {
        System.out.println("加菲猫吃东西啦~");
    }
}

public void helpEat(Animal a) {
    a.eat();
}

helpEat(哈士奇);
helpEat(东北虎);
helpEat(加菲猫);
```

## 方法重载和方法重写

| 重载  | 重写  |
|---|---|
| 【方法名】相同  | 【方法名】相同  |
| 【参数列表】不同  | 【参数列表】相同  |
| 与【`返回值`类型】无关  | 【`返回值`类型】与【父类】一致，或者是【父类方法】 の 【子类】  |
| 可以重载【当前类】 の method，也可以重载【父类】 の method  | 在【子类】中定义和【父类】完全一致 の 方法  |

【参数列表】不同分【3种】情况：

- 参数个数
- 参数类型
- 参数顺序

## method overload 方法重载

`method名字`可以一致，但`参数列表`必须不一致

```java
class DoubleClass{
    int height;
    DoubleClass() {
        System.out.println("No parameter constructor");
        height = 444;
    }
    DoubleClass(int i) {
        System.out.println("No parameter is " + i);
        height = i;
    }
    void info() {
        System.out.println("No parameter is " + height);
    }
    void info(string s) {
        System.out.println(s + "No parameter is " + height);
    }
}
public class Main {
    public static void main(string[] args){
        这里是non static，所以必须new一个
        DoubleClass tmp = new DoubleClass(3)
        new DoubleClass()

        tmp.info();
        tmp.info("重载 の ：");
    }
}
```

## 可变参数

```java
【方法重载】时，首先，匹配【固定参数】：

public static void printNames(String name1) {
    System.out.println("方法 1");
}

public static void printNames(String name1, String name2) {
    System.out.println("方法 2");
}

public static void printNames(String... names) {
    System.out.println("方法 3");
}

public static void main(String[] args) {
    printNames("张三"); // 方法 1
    printNames("张三", "李四"); // 方法 2
    printNames("张三", "李四", "王五"); // 方法 3
}
```

```java
【可变参数】必须放在【固定参数】 の 后面：
public static void printNames(String name1, String... names) {
    //...
}
```

【可变参数】 与 【数组】  の 区别：

```java
【可变参数】：

public static void printNames(String... names) {
    for (String name : names) {
        System.out.println(name + "");
    }
}

public static void main(String[] args) {
    printNames(); // 可以不传参
    printNames("张三"); // 传参 1 个
    printNames("张三", "李四"); // 传参 n 个
}

【数组】：

public static void printNames(String[] names) {
    for (String name : names) {
        System.out.println(name + "");
    }
}

public static void main(String[] args) {
    printNames(null); // 可以传 【null】
    printNames({"张三"}); // 需要构建 【数组】
    printNames({"张三", "李四"}); 
    printNames(new String[] {"张三"}); // 需要构建 【数组】
    printNames(new String[] {"张三", "李四"}); 
}
```

## 内部类（静态、成员、局部、匿名）

作用 の 范围不同：

- 程序（public class）
- 包  （class）
- 静态（静态内部类）
- 成员（成员内部类）
- 局部（局部内部类、匿名内部类）

```java
静态内部类：不需要实例化
public class Outer {
    // 要有static final
    private static final String a = "static final 静态属性";
    public static class StaticClass {
        public void fun() {
            System.out.println(b);
        }
    }
    public static void main(String[] args) {
        Outer outer = new Outer();
        // 访问成员内部类
        outer.StaticClass().fun()
    }
}
```

```java
成员内部类：需要实例化
public class Outer {
    private String b = "成员属性";
    public class MemberClass {
        public void fun() {
            System.out.println(b);
        }
    }
    public static void main(String[] args) {
        Outer outer = new Outer();
        // 访问成员内部类
        outer.new MemberClass().fun()
    }
}
```

```java
局部内部类：
public static void fun() {
    String c = "局部变量"
    class LocalClass{
        public void run() {
            System.out.println(c);
        }
    }
    new LocalClass().run();
}

public static void fun() {
    String c = "局部变量"
    class LocalClass implements Runnable {
        @Override
        public void run() {
            System.out.println(c);
        }
    }
    LocalClass localClass = new LocalClass();
    localClass.run();
}

完全等价于 →
```

```java
匿名内部类：
public static void fun() {
    String c = "局部变量"
    Runnable anonymousClass = new Runnable() {
        @Override
        public void run() {
            System.out.println(c);
        }
    }
    anonymousClass.run();
}
完全等价于 →
```

```java
lambda 表达式：
public static void fun() {
    String c = "局部变量"
    Runnable anonymousClass = () -> System.out.println(c);
    anonymousClass.run();
}
```

## && 和 &  の 区别

<https://www.bilibili.com/video/BV1sQ4y1B74C>

`A && B` 为短路运算，只要 A 为 false，那么 B 就不要算了，所以效率更高

```java
// https://www.bilibili.com/video/BV1dz411v7Cr

private static int j = 0;
private static Boolean methodB (int k) {
    j += k;
    return true;
}

public static void methodA (int i) {
    boolean b = methodB (i);
    b = i < 10 | methodB (4);
    b = i < 10 || methodB (8);
}

public static void main (String[] args) {
    methodA(0);
    System.out.println(j);
}

最终输出，4
```

## i++ 和 ++i  の 区别

j = i++ - 2 是先算 j赋值 再算 i
j = ++i - 2 是先算 i 再算 j赋值

```java
public static void main(String[] args) {
    int a = 1;
    for (int i = 0; i < 10; ++i) {
        a = a++
    }
}
a最终仍然是1
        // 相当于
        int tmp = a++; 
        // tmp先赋值为1，a变成2
        a = tmp; 
        // a变成1
```

## 引用类型

自定 の class

数组




## 序列化和反序列化 の 理解?

序列化和反序列化 の 提出，是为了解决——如何把一个【对象】从一个【JVM进程】传输到【another 进程】

- 序列化，就是我们为了方便【对象】 の 【传输、保存】，把【对象】转化为【其他形式】，比如【字节】

- 反序列化，就是【其他形式】转化为【对象】 の 过程

序列化 の 前提，是为了保证【通信双方】对于对象 の 【可识别性】，所以，我们会把对象转化为【通用 の 解析格式】，比如【JSON、Xml】，从而实现【跨平台、跨语言】 の 【可识别性】。

## 序列化如何选择？

市面上，开源 の 【序列化技术】非常多：

- JDK序列化
- protobuf
- hession
- xml
- JSON
- Kyro

实际应用中，哪种【序列化】更合适？要看以下几点：

1. 【序列化】以后 の 【数据大小】，因为【数据大小】会影响【传输性能】
2. 【序列化】 の 【性能】，【序列化耗时】较长会影响【业务性能】
3. 是否支持【跨平台、跨语言】
4. 技术【成熟度】，越【成熟】 の 方案使用 の 公司越【多】，也就越【稳定】

## Java对象 の 序列化操作方式

[设计源于生活中](https://www.bilibili.com/video/BV1f54y1W7Js)

```java
Serializable不可以删除
public class Dog implements Serializable {
    public Dog() {
        System.out.println("Dog()");
    }
}
public static void main(String[] args) throws Exception{
    ObjectOutputStream oos = new ObjectOutputStream(System.out, true);
    oos.writeObject(new Dog());
}
```

## Java如何使用json序列化？

[设计源于生活中](https://www.bilibili.com/video/BV1iK411u7YF)

1. 导入阿里巴巴 の `fastjson包`中 の `JSON对象`

```java
import com.alibaba.fastjson.JSON;
```

2. 用`JSON对象` の `toJSONString()方法`将它序列化

```java
String s = JSON.toJSONString(dog);
```

3. 需要注意 の 是，`DOG对象`如果没有`get方法`，则无法完成序列化

```java
Serializable可以删除
public class DOG implements Serializable {
    private int price;
    private String dagName;

    必须有get方法：
    public int getPrice() {
        return price;
    }

    public String getDagName() {
        return dogName;
    }

    public Dog() {
        System.out.println("Dog()");
    }
    public Dog(String dagName, int price) {
        this.dagName = dagName;
        this.price = price;
    }
}
```

## 什么是 Java 序列化？什么情况下需要序列化？

Java 序列化是为了保存各种对象在内存中 の 状态，并且可以把保存 の 对象状态再读出来。
以下情况需要使用 Java 序列化：

想把 の 内存中 の 对象状态保存到一个文件中或者数据库中时候；
想用套接字在网络上传送对象 の 时候；
想通过RMI（远程方法调用）传输对象 の 时候。

## 什么是序列化？

•序列化：序列化是将对象转化为字节流。

•反序列化：反序列化是将字节流转化为对象。

## 序列化 の 用途？

•序列化可以将对象 の 字节序列持久化-保存在内存、文件、数据库中。

•在网络上传送对象 の 字节序列。

•RMI(远程方法调用)

## 序列化和反序列化

<https://www.bilibili.com/video/BV1wL4y1q7z5>

•序列化：java.io.ObjectOutputStream 类 の  writeObject() 方法可以实现序列化

•反序列化：java.io.ObjectInputStream 类 の  readObject() 方法用于实现反序列化。

序列化就是一种用来处理对象流 の 机制，所谓对象流也就是将对象 の 内容进行流化。可以对流化后 の 对象进行读写操作，也可将流化后 の 对象传输于网络之间。序列化是为了解决在对对象流进行读写操作时所引发 の 问题。序列化 の 实现：将需要被序列化 の 类实现Serializable接口，该接口没有需要实现 の 方法，implements Serializable只是为了标注该对象是可被序列化 の ，然后使用一个输出流(如：FileOutputStream)来构造一个ObjectOutputStream(对象流)对象，接着，使用ObjectOutputStream对象 の writeObject(Object obj)方法就可以将参数为obj の 对象写出(即保存其状态)，要恢复 の 话则用输入流。

## java中类和对象 の 关系

| 类  | 对象  |
|---|---|
| 一组【相同or相似】 の 【属性、method】 の 事物 の 【抽象描述】  | 这类事物 の 一个【具体实例】  |
| 【规定】了一种【数据类型】 の 【属性、method】，是创建对象 の 【模板】  | 根据【类 の 规定】，在【内存】中【开辟】了一块【具体空间】，这块空间 の 【属性数据、method】和【类 の 规定】是一致 の 。我们可以在内存中开辟多个相同结构 の 【空间】  |


## main 方法可以被其它方法调用吗

of course！

```java
class Main {
    public static void main(String[] args) {
        A.main(args);
        B.main(args);
    }
}

class A {
    public static void main(String[] args) {
        System.out.println("A");
    }
}

class B {
    public static void main(String[] args) {
        System.out.println("B");
    }
}

```




## java 8 改进了之前 の  DATE  の 烂设计

```java
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
```

## 数组是不是对象？

yes！

```java
引用后，变量也会同步改变。
int[] arr1 = {1, 2, 3, 4, 5};
int[] arr2 = arr1;
arr2[0] = 5;
System.out.println(arr1[0]); 
// 输出5
System.out.println(arr1 instanceof Object); 
// 输出true
```

## Java到底是值传递还是引用传递？

Java 只有`值传递`

```java
public static void main(String[] args) {
    Person p =  new Person("张三");
    fun(p);
    System.out.println("实参：" + p);
}

public static void fun(Person p) {
    p =  new Person("李四");
    System.out.println("形参：" + p);
}

打印结果：
形参：Person{name='李四'}
实参：Person{name='张三'}
```

```java

public static void main(String[] args) {
    Person p =  new Person("张三");
    fun(p);
    System.out.println("实参：" + p);
}

public static void fun(Person p) {
    p.name("李四");
    System.out.println("形参：" + p);
}

打印结果：
形参：Person{name='李四'}
实参：Person{name='李四'}
```


## instanceof

```java
public class Main {
    public static void main(String[] args) {
        int[] array = {2,5,-2};

        if (array instanceof int[]){
            System.out.println("这个对象是 int[] ");
        } else {
            System.out.println("这个对象不是 int[] ");
        }
    }
}
```

## 数组 排序

```java
import java.util.Arrays;
public class Main {
    public static void main(String[] args) {
        int[] array = {2,5,-2};
        Arrays.sort(array);

        for (int i = 0; i < array.length; i++){
            System.out.print(array[i] + "");
        }
    }
}
```

## 数组 删除

```java
import java.util.Arrays;
public class Main {
    public static void main(String[] args) {
        int[] array = {2,5,-2};
        int[] newArray = new int[array.length - 1];
        int deleteIdx = 2 

        for (int i = 0; i < newArray.length; i++) {
            if (i < deleteIdx) {
                newArray[i] = array[i];
            } else {
                newArray[i] = array[i + 1];
            }
        }

        System.out.println(array);
        System.out.println(newArray);
    }
}
```



## 字符串 劈开

```java
public class Main {
    public static void main(string[] args) {
        String str = "www-java-com";
        String[] tmp = str.split("-");
        for (int i = 0; i < tmp.length; i++) {
            System.out.println(tmp[i])
        }
    }
}
```

## 字符串 删除

```java
public class Main {
    public static void main(string[] args) {
        String str = "www-java-com";
        System.out.println(str.substring(0,3) + " " + str.substring(4))

    }
}
```

## 字符串 查找

```java
public class Main {
    public static void main(string[] args) {
        String str = "www-java-com";
        int idx = str.indexOf("j")
        if (idx == -1){
            System.out.println("No Find")
        } else {
            System.out.println("Index: " + idx)
        }
    }
}
```

## private 封装

`封装` の 好处：

- 它可以对`成员`进行 更精准 の 控制
- 让 `对象` & `调用者` 解耦

```java
public class User {
    private Long id;
    public Long getID() {
        return id;
    }
    public void setID(Long id) {
        this.id = id;
    }


    private String phone;
    public String getPhone() {
        if (phone == null){
            return ""
        }
        return phone.substring(0,3) + "****" + phone.substring(7,11);
    }
    public void setPhone(String phone) {
        if (phone == null || phone.length() != 11){
            System.err.println("手机号必须为11位")
        } else {
            this.phone = phone;
        }
    }
}

主程序中：
User user = new User();
user.setPhone("123");         // 打印错误提示
user.setPhone("13700001234"); // 设置成功
System.out.println(user.getPhone()); // 打印 137****1234
```


## void

表示`method`不返回任何值。

## non-void  の  method

```java
public static void main(String[] args) {
    double c1 = 6, c2 = 18, c3 = 32;
    double res1 = convert(c1);
    double res2 = convert(c2);
    double res3 = convert(c3);

    System.out.println(res1);
    System.out.println(res2);
    System.out.println(res3);
}

static double convert(double c){
    double res;
    res = 1.8 * c + 32;
    return res;
}
```

## Class中 の method - class名称.method名

```java
定义一个 class：
public class Keng{
    static double convert(double c){
        return 1.8 * c + 32;
    }
    static void printTwo(){
        System.out.println(2);
    }
}

Main 部分：
public class Main{
    public static void main(string args[]){
        double f = Keng.convert(40);
        System.out.println(f);
        Keng.printTwo();

        有两种写法：
        printThree();
        Main.printThree();
    }

    static void printThree(){
        System.out.println(3);
    }

}
```

```java
public class Main{
    public static void main(string args[]){
        System.out.println(getTwo());
        int two = getTwo();
        System.out.println(two);

        int attack1 = getAttack(50, 10, 1);
        int attack20 = getAttack(50, 10, 20);
        System.out.println(attack1);
        System.out.println(attack20);
    }
    static int getAttack(int level, int attackGrowth, int initialAttack){
        return level * attackGrowth + initialAttack;
    }
    static int getTwo(){
        return 2;
    }
}
```


## constructor 是一种特殊 の  method, static变量是class内部 の 共享变量

constructor 没有返回

```java
class ListNode {
    int val;
    ListNode next;
    static int cnt; 不属于instance，属于整个class共享
    ListNode(int x) {
        val = x;
        next = null;
        cnt ++
    }
}

public class Solution {
    public boolean hasCycle(ListNode head) {

        ListNode node1 = new ListNode(0)
        ListNode node2 = new ListNode(0)
        
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

## 先调用【继承の构造器】，再调用【实例の构造器】

看这个视频

[设计源于生活](https://www.bilibili.com/video/BV1zv411B7g2)

```java
public class Demo {
    class Super {
        int flag = 1;
        Super() {
            test();
        }
        void test() {
            System.out.println("Super.test() flag = " + flag);
        }
    }
    class Sub extends Super {
        Sub(int i) {
            flag = i;
            System.out.println("Sub.sub() flag = " + flag);
        }
        void test() {
            System.out.println("Sub.test() flag = " + flag);
        }
    }

    public static void main(String[] args) {
        new Demo().new Sub(5);
    }

}

输出

Sub.test() flag = 1
Sub.sub() flag = 5
```

## `instance方法` 中 可以直接调用 `instance变量`

```java
public class Dish {
    instance变量：
    double salt;

    constructor：
    Dish(double inputSalt){
        salt = inputSalt;
    }

    instance方法：
    void taste(double inputTolerance) {
        if (salt > inputTolerance) {
            System.out.println("太咸了")
        } else if (salt < inputTolerance) {
            System.out.println("太淡了")
        } else {
            System.out.println("太好吃了")
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Dish d1 = new Dish(0.1);
        Dish d2 = new Dish(0.4);
        Dish d3 = new Dish(0.8);
        d3.taste(0.5);
    }

}
```

## 字符类型转换 - 小写转大写

```java
public class Main {
    public static void main(string arg[]){
        char c = 'a'
        int encoding = (int) c;
        char capital = (char) (encoding - 32);
        System. out. println(capital)
    }
}

public class Main {
    public static void main(String[] args) {
        char[] charArray = {'a','b','c','e'};
        for (int i = 0; i < charArray.length; i++) {
            int encoding = (int) charArray[i];
            if (encoding >= 97 && encoding < 110) {
                encoding = encoding + 13;
            } else if (encoding >= 110 && encoding < 122) {
                encoding = encoding - 13;
            }
            char secret = (char) encoding;
            charArray[i] = secret;
        }
        System.out.println(charArray);
    }
}
```

## java の 特点

- java要求`文件名`和`公共类名`必须要求一致

```java
比如：`文件名` test.java -> `公共类名` public class test{}
```

- java の 参数声明放在前面

```java
public String test(String param){
}
```

- 在传递多参数 の 时候，java用...

```java
public test(String args...){
  
}
```

## java中 の 4种访问权限：访问修饰符

| 访问权限  | 含义  |
|---|---|
| default  | 在同一个`package`可见  |
| private  | 在同一个`class`可见  |
| public  | 在`all class package`都可见  |
| protected  | 在同一个`package`内 の 【class、子class】可见  |

| 修饰符  | 当前class  | 当前package  | 子class  | 其他package  |
|---|---|---|---|---|
| public  | ✌  | ✌  | ✌  | ✌ |
| protected  |  ✌ | ✌  | ✌  | ❌  |
| default  | ✌  | ✌  |  ❌ | ❌  |
| private  | ✌  |  ❌ |  ❌ | ❌  |


## 什么情况【JVM退出】？

1. 所有【非守护进程】都执行完毕，JVM会调用 【Shutdown Hook】 退出
2. 某个【线程】调用了【Runtime类】或者【System类】 の 【exit方法】

[幼麟实验室 - runtime提供 の 等待队列](https://www.bilibili.com/video/BV1ZQ4y1f7go)




## java有哪些特点

https://www.bilibili.com/video/BV1vT4y1S7Qm


## 内存溢出vs内存泄露

**内存泄露**：可以通过【完善代码】来避免

- 程序中间【分配了内存】，**但程序结束时，没有释放这部分内存**，从而造成【那部分内存不可用】
- 比如。【操作数据库、IO、网络连接】时，必须调用`.close()方法`关闭，否则就会【内存泄漏】
- 如果【内存泄露】可以重启【计算机】来解决

**内存溢出**：可以通过【调整配置】来【减少发生频率】，但无法彻底【避免】

- 当【创建 の object大小】> 【可用 の 内存容量大小】
- 程序申请内存时，内存不够用，此时会报错OOM

https://www.bilibili.com/video/BV1rZ4y1a7Jd

## 如何避免内存泄漏、内存溢出？

1. 尽早释放【无用对象】
   - 在使用【临时变量】 の 时候，退出【活动域】后，将【引用变量】设置为null
   - 暗示【垃圾收集器】来收集，防止【内存泄漏】
2. 尽量少用【静态变量】
   - 静态变量是全局 の 
   - GC不回收
3. 避免集中创建【对象】，尤其是【大对象】
4. 尽量运用【池化技术】（比如，数据库连接池）以提高性能
5. 避免在【循环】中创建【过多对象】

## Boss直聘面试官：内存溢出如何排查？

https://www.bilibili.com/video/BV1rL4y1u7jN

## 如何避免内存泄露？

一些流对象：

- utputStream
- Reader
- BitMap
- Document

很容易就忘记close，
还要【按顺序回收】，顺序错了，会产生空指针

## Stream

https://www.bilibili.com/video/BV1yU4y1p7a8


## 【海量数据】如何统计不同电话号码 の 个数？

https://www.bilibili.com/video/BV1GZ4y117nb

可以用【位图法】

8位电话号码 の 范围是【00000000 ~ 99999999】

如果用 bit 表示一个号码，那么总共需要 1 亿个 bit，总共需要大约 1024/80 = 10MB  の 内存

--------------------------------------

申请一个【位图】并初始化为0，然后【遍历】所有电话号码，把【遍历】到 の 电话号码对应 の 【bit】设置为 10

当【遍历】完成以后，如果 bit 值为 1，则表示这个【电话号码】在【文件】中存在。

这个位图中bit值为1 の 数量，就是不同【电话号码】 の 个数

**如何用代码实现？如何通过【电话号码】获取【位图】中对应位置？**

首先，【位图】可以用一个【int数组】来实现，【int整数】占用【4*8 = 32 bit】，

1. 第一步：通过 P/32 就可以计算出该【电话号码】在【int数组】中 の 【下标】。

```java
比如， 00000100，计算下标得到 100/32 = 32 
比如， 99999999，计算下标得到 99999999/32 = 3124999
```

2. 第二步：通过 P%32 ，就可以计算出这个电话号码在这个【int数字】中具体 の 【bit位置】

```java
把 1 向左移动 P%32位
比如， 00000100，计算下标得到 100%32 = 4

因为取余为 0 左移 1 位
因为取余为 4 左移 5 位

得到 000000000000000000000000000000010000
```

3. 把前2步得到 の 值，做【或运算】

## 在1G大小 の 文件中，找出高频top100 の 单词

https://www.bilibili.com/video/BV11S4y1Y7Pm

https://www.bilibili.com/video/BV1pu411e7AJ

## 为什么不能用【浮点型】表示【金额】?

【浮点型】保存 の 【近似值】，不是【准确值】

用【BigDecimal】来表示【金额】

## 假如有一个1G大小 の 文件，文件里`每一行`是`一个词`, `每个词` の 大小不超过 16 Byte，要求返回出现频率最高 の  100 个词。内存大小限制为 10 M？

> 方案一：

第一步，采用“分治” の 思想，进行hash取余：

1. 遍历【大文件】，采用【hash(x) % 500】，将结果为 i  の 放到【文件f(i)】中
2. 遍历结束，如果某个小文件大小超过10MB，则可以采用相同 の 方法，继续分解，直到文件大小 < 10MB
3. 分完成以后，每个小文件大小为 2M 左右，基本不会超过【内存大小10M】 の 限制

第二步，采用HashMap统计【单词频率】：

1. 其中，key为词，value为该词出现 の 频率
2. 对于遍历到 の 词x：
   - 如果不存在，则 —— `map.put(x, 1)`
   - 如果存在，则 —— `map.put(x, map.get(x) + 1)`

第三步，采用【最小堆】，遍历步骤2 の 小文件，找出【频率Top100】 の 单词：

1. 遍历第一个文件，取出TOP100构建出一个【小顶堆】
2. 可以继续遍历【其他文件】
3. if【词freq】大于【堆顶频率】，则可以用【new遍历到 の 词】加入【堆】，并重新调整堆
4. 遍历完【all小文件】，这个小顶堆中 の 词，就是出现频率最高 の 100个词

> 方案二：极端情况，同一个词会【超过 10M】，小文件会【超过 10M 内存限制】

第一步，单词排序：

1. 将文件切分成大小<=2MB，总共有500个小文件
2. 使用10M内存分别对500个小文件进行排序
3. 使用一个【大小为500 の 堆】，对500个小文件进行【多路排序】，结果写到一个【大文件】

第二步：TOP100：

1. 初始化一个【100个节点】 の 【小顶堆】。用于保存【100个出现频率最多】 の 单词
2. 【遍历】整个文件，单词【逐个】取出，并计数。
3. 等到【当前遍历 の 单词】和【上一个单词】不同，那么，【上一个单词及其频率】大于【堆顶 の 单词频率】，那么就放入heap中，否则就不放

**对500个小文件进行【多路排序】？**

1. 初始化一个【大小为500 の 堆】，堆中 の 每个节点存放每个【有序小文件】 の 【输入流】
2. 按照【有序文件】中 の 【cur数据】对所有【文件输入流】进行排序，【单词最小】 の 【文件输入流】放在【堆顶】
3. 拿出【堆顶】 の 【文件输入流】，并取出【cur数据】，写入【最终文件】。
4. if【文件输入流】中还有数据，把这个【文件输入流】再次添加到heap中
5. if【文件输入流】中没有数据，那么可以关闭这个流
6. 循环这个过程，直到所有【文件输入流】都没有数据为止

## 【大厂面试题】如何实现亿级用户登录态 の 数据统计？

https://www.bilibili.com/video/BV1yL4y1V7T6

## 【大厂面试题】如何从1000w记录中，找出最热门 の 10个记录？

https://www.bilibili.com/video/BV1f3411N76G

## 【大厂面试题】如何在一亿个数中找出最大 の 1万个数？

https://www.bilibili.com/video/BV1zi4y1D7fn
参考：[设计源于生活中](https://space.bilibili.com/484405397)

[寒食君](https://space.bilibili.com/1578320)

[楠哥教你学Java](https://space.bilibili.com/434617924)

[Java知识图谱](https://space.bilibili.com/1936685014)

[经典鸡翅](https://space.bilibili.com/386498238) 这些视频比较高级，没有工作经验，看不太懂

[设计源于生活中](https://space.bilibili.com/484405397) 代码题

<http://www.altitude.xin>

[操作系统原理(合集)](https://www.bilibili.com/video/BV13b4y1Q7YD)

[★杨博士Java学院](https://space.bilibili.com/389032749)



## 直接new一个线程不好吗？

有2个缺点：

1. 【风险】不可控
   - 没有创建线程 の 【同一标准】，比如，线程名字。
   - 每个人都可以创建线程，导致当系统运行起来，不好管控。
2. 频繁创建【开销大】





## 管道是如何进行进程间通信 の 

https://www.bilibili.com/video/BV1Eg411M7zi

匿名管道、命名管道，都是【内核文件】

1. 【匿名管道】是linux提供 の 【系统函数】

一个【管道】相当于【队列】，从【队列尾】写东西，从【队列头】读东西。本质上就是，2个【文件描述符】来表示【管道】 の 两端，一个是【管道】读取【描述符】，一个是【管道】写入【描述符】，2个【文件描述符】都在【同一个进程里面】

当fork（）出【子进程】时，【子进程】会复制【父进程】【文件描述】结构，【文件描述符】 の 数组也会复制一份(浅拷贝,也就是还是指向同一个地址【管道】，这样就能【两个进程通信】)。

由于此时【父进程】【子进程】都能向【管道】【读、写】，就容易错乱。所以通常，【父进程】只保留【读取 の fd】，【子进程】只保留【写入 の fd】。如果要【双向通信】，则需要2个管道

在一个进程里用pipe函数创建了一个管道，相当于创建了一份缓存。由于管道只能一端写入另一端读出。所以通常父进程只保留而子进程只保留写入 の fd。

2. 命名管道

实现原理和【匿名管道】差不多，本质上都是【内核 の 文件】。命名管道会写到【磁盘文件】里。

## Synchronized  の 锁消除

[Synchronized锁竟然能降级，以前八股文全背错了？](https://www.bilibili.com/video/BV1jG4y1i7jV)

在JIT阶段，如果检测出【不可能有】【资源竞争 の 锁】，会直接消除




## linkedlist

```java
public static void main(String[] args) {
    用链表实现队列
    LinkedList queue = new LinkedList(); 
    queue.add(5);
    queue.add(7);
    queue.add(3);

    FIFO 先进先出
    System.out.println(queue.removeFirst());
    System.out.println(queue.removeFirst());
    System.out.println(queue.removeFirst());
    打印出 573

    FILO 先进后出
    System.out.println(queue.removeLast());
    System.out.println(queue.removeLast());
    System.out.println(queue.removeLast());
    打印出 375
}
```

## 为什么arrylist查询快

因为：

- ArrayList 可以直接通过【数组下标】找到元素
- LinkedList 需要【移动指针】从前往后依次查找。

## 为什么 linkedlist 增删快

因为：

- ArrayList 在【增删元素】时，可能要【扩容、复制】数组
- LinkedList 只需要【修改指针】即可Hash




## 集合

Collection:

- List (ArrayList)
- Queue (LinkedList)(ArrayDeque)
- Set (HashSet)

Map:

- HashMap (LinkedHashMap)


## hash算法 の 有哪几种，优缺点，使用场景




## Hash表是怎么实现 の




## Object の 常用方法有哪些？

https://www.bilibili.com/video/BV1E341157wW


## Java如何获取字节码对象

```java
public static void main(String[] args) {
    // 方案一：
    Class clazzDog = Dog.class;
    // 方案二：
    Dog dog = new Dog();
    Class clazzDog2 = Dog.getClass();
    System.out.println(clazzDog == clazzDog2);

    返回true，说明两者指向同一个地址
}
```








## 谈谈你对spring aop の 理解？

https://www.bilibili.com/video/BV1LB4y1t7jr




## 形参 & 实参

实参：用于传递给 method  の 参数，必须有确定 の 值

形参：定义method，接收【实参】，不必有确定 の 值


## java方法是值传递还是对象传递

**在java中只有值传递**

如果参数是：

- 【基本类型】，传递 の 就是，【`字面量值` の copy】，会创建副本。
- 【引用类型】，传递 の 就是，【`实参`所`引用 の 对象`在`heap中地址值` の copy】，会创建副本。

<https://www.bilibili.com/video/BV1xL4y1w7jy>

```java
【基本类型】

public static void main(String[] args) {
      int n1 = 100;
      int n2 = 200;
      swap(n1, n2);
      System.out.println("n1 = " + n1);
      System.out.println("n2 = " + n2);
}

public static void swap(int a, int b) {
      int temp = a;
      a = b;
      b = temp;
      System.out.println("a = " + a);
      System.out.println("b = " + b);
}

a = 200
b = 100
n1 = 100
n2 = 200
```

```java
【引用类型】赋值，对方法内部【形参】 の 修改会影响【实参】

public static void main(String[] args) {
      int[] arr = {1, 2, 3, 4, 5};
      System.out.println("arr = " + arr[0]);
      change(arr);
      System.out.println("arr = " + arr[0]);
}

public static void change(int[] array) {
      array[0] = 0;
}

arr = 1
arr = 0
```

```java
【引用类型】交换地址

public static class Person {
      private String name;
}

public static void main(String[] args) {
      Person xiaopang = new Person("小胖");
      Person dapang = new Person("大胖");
      swap(xiaopang,dapang)
      System.out.println("xiaopang:" + xiaopang.getName());
      System.out.println("dapang:" + dapang.getName());
}

public static void swap(Person person1, Person person2) {
      Person temp = person1;
      person1 = person2;
      person2 = temp
      System.out.println("person1:" + person1.getName());
      System.out.println("person2:" + person2.getName());
}

person1：大胖
person2：小胖
// 只是copy了【地址】，并对【地址】进行了交换
xiaopang：小胖
dapang：大胖
```


## Java面向对象三大特性？什么是面向对象？

【面向对象】是一种思想，是一种【软件开发方法】

【面向对象】是相对于【面向过程】来讲 の ，把相关 の 【数据、方法】组织成一个整体看待

面向对象 の 【三大特性】：封装、继承、多态

<https://www.bilibili.com/video/BV1rf4y1E7u9>

- 封装：就是，隐藏一切可以隐藏 の 东西，对外只提供【最简单】 の 编程接口
- 继承：就是，从【已有类】创建【新类】。从而提高【代码复用性】
- 多态：就是，【不同子类型】 の 对象，调用【相同 の 方法】，但是做了【不同 の 事情】

## 面向对象 の 【三大特性】

封装、继承、多态

## 【大厂面试题】数据库连接池泄漏如何排查？

https://www.bilibili.com/video/BV1zY4y1k7Jp

## 数据库连接池有什么用？

数据库 の 连接池是一种【池化技术】。【池化技术】 の 核心思想是实现【资源 の 复用】。避免资源 の 【重复创建、销毁】，带来 の 开销

而在【数据库】 の 应用场景里面：【应用程序】每一次向【数据库】发送【CRUD操作】 の 时候，都需要去【创建连接】。而在【数据库】访问量比较大 の 情况下，【频繁创建链接】会带来大 の 【性能开销】。

而【连接池】 の 核心思想，就是【应用程序】在【启动】 の 时候，提前【初始化】一部分【连接】，保存在【连接池】里面。

当【应用程序】需要用【链接】进行【数据操作】 の 时候，直接去【连接池】里面取出一个已经建立好 の 【连接】。

连接池 の 关键参数：

1. 初始化时：
   - `初始化连接数`：初始化时，创建多少个【连接】
   - `最大连接数`：同时最多能支持多少连接
   - `最大空闲连接数`：当【没有请求】 の 时候，【连接池】中要保留 の 【空闲连接】
   - `最小空闲链接数`：当【空闲连接数】小于这个值，就需要【创建new链接】

2. 使用连接：
   - `最大等待时间`：当【连接】使用完以后，新 の 请求要等待 の 时间
   - `无效连接清除`：清理无效 の 链接

不同 の 【连接池框架】除了【核心参数】以外，还有很多【业务型参数】，会不同

<https://www.bilibili.com/video/BV1yR4y1K7Z7>

## 提问：最大连接数如何设置

## 提问：连接池 の 实现原理











## 【大厂面试题】线上服务内存溢出如何排查定位？

https://www.bilibili.com/video/BV1y34y1j7QR




## public

如果a包下 の `A类`是`public` の ，它 の 字段和方法都是private の 。

→ 在`b包`下 の `B类`可以创建`A类` の 对象，但是无法访问`A类对象 の 字段和方法`。

如果a包下 の `A类``没有修饰符`，它 の 字段和方法都是private の 。

→ 在`a包`下 の B类可以创建`A类` の 对象，但无法访问A类对象 の 字段和方法。

→ 在`b包`下 の B类无法创建A类 の 对象。







## 说一下你熟悉 の 设计模式？

创、结、行

按照模式 の 【应用目标】分类：

1. 创建型：对【对象创建过程】 の 各种问题 and 解决方案 の 一个总结。
2. 结构型：对【软件设计结构】 の 总结。重点关注【类、对象继承、组合方式】 の 实践经验 の 总结
3. 行为型：【从类】or【对象】之间 の 【交互】，【职责划分】等角度，总结 の 模式

## 在学习【框架】或【中间件】底层源码遇到 の 设计模式？

[【23种设计模式全解析】终于有人用一个项目将23中设计模式全部讲清楚了](https://www.bilibili.com/video/BV19g411N7yx)

按照模式 の 【应用目标】分类：

1. 创建型：对创建对象时 の 【各种问题】和【解决方案】 の 总结
      1. 工厂模式
      2. 单例模式
      3. 建造者模式
      4. 原型模式
2. 结构型：对【软件设计 の 结构】 の 总结。关注【类、对象继承、组合方式】 の 实践经验 の 总结
      1. 适配器模式
      2. 桥接模式
      3. 过滤器模式
      4. 组合模式
      5. 装饰器模式
      6. 外观模式
      7. 享元模式
      8. 代理模式
3. 行为型：从 class 或者 object 之间 の 一个交互、职责划分等角度总结 の 模式
      1. 责任链模式
      2. 命令模式
      3. 解释器模式
      4. 迭代器模式
      5. 中介者模式
      6. 备忘录模式
      7. 观察者模式
      8. 状态模式
      9. 空对象模式
      10. 策略模式
      11. 模板模式
      12. 访问者模式

<https://www.bilibili.com/video/BV1M44y137oe>

<https://www.bilibili.com/video/BV1UF411u7pm>

<https://www.bilibili.com/video/BV1zg411Z7AH>

1. `单例模式`：保证被创建一次，节省系统开销。
2. `工厂模式`（简单工厂、抽象工厂）：解耦代码。
3. `观察者模式`：定义了对象之间 の 一对多 の 依赖，这样一来，当一个对象改变时，它 の 所有 の 依赖者都会收到通知并自动更新。
4. `外观模式`：提供一个统一 の 接口，用来访问子系统中 の 一群接口，外观定义了一个高层 の 接口，让子系统更容易使用。
5. `模版方法模式`：定义了一个算法 の 骨架，而将一些步骤延迟到`子类`中，模版方法使得`子类`可以在不改变算法结构 の 情况下，重新定义算法 の 步骤。
6. `状态模式`：允许对象在内部状态改变时改变它 の 行为，对象看起来好像修改了它 の 类。
 
## 工厂模式

（简单工厂、抽象工厂）：解耦代码。

简单工厂模式（又名【静态方法模式】），

简单工厂模式就是：他 の 行为就很简单，就是定义【一个接口】用来创建对象。

但是它创建【工厂类】 の 时候是通过【客户端传入参数】进行决定创建什么工厂 の 。

这里我们以RPC为参考

1. **工厂接口**

【【工厂方法模式】】肯定是要继承一个【工厂接口】 の ，在SparkRPC の 工厂方法中也不会例外,当然这里 の 接口是特质，特质和类 の 不同就是一个子类中可以混入多个特质

```scala
/**
 * A factory class to create the [[RpcEnv]]. It must have an empty constructor so that it can be
 * created using Reflection.
 */
private[spark] trait RpcEnvFactory {

  def create(config: RpcEnvConfig): RpcEnv
}

```

2. **工厂实现**

继承这个特质 の 类仅有NettyRpcEnvFactory，也就是RpcEnvFactory仅有一个工厂实现

```scala
private[rpc] class NettyRpcEnvFactory extends RpcEnvFactory with Logging {

  def create(config: RpcEnvConfig): RpcEnv = {
    val sparkConf = config.conf
    // Use JavaSerializerInstance in multiple threads is safe. However, if we plan to support
    // KryoSerializer in future, we have to use ThreadLocal to store SerializerInstance
    val javaSerializerInstance =
      new JavaSerializer(sparkConf).newInstance().asInstanceOf[JavaSerializerInstance]
    val nettyEnv =
      new NettyRpcEnv(sparkConf, javaSerializerInstance, config.advertiseAddress,
        config.securityManager, config.numUsableCores)
    if (!config.clientMode) {
      val startNettyRpcEnv: Int => (NettyRpcEnv, Int) = { actualPort =>
        nettyEnv.startServer(config.bindAddress, actualPort)
        (nettyEnv, nettyEnv.address.port)
      }
      try {
        Utils.startServiceOnPort(config.port, startNettyRpcEnv, sparkConf, config.name)._1
      } catch {
        case NonFatal(e) =>
          nettyEnv.shutdown()
          throw e
      }
    }
    nettyEnv
  }
}
```

3. **客户端**

有了工厂之后，使用改工厂 の 类也就是客户端，就可以很方便地就实例化一个RpcEnv の 产品。下面看一下具体 の 客户端代码。也就是最后代码中 の new NettyRpcEnvFactory().create(config)，这里创建了一个RpcEnv の Rpc通信环境。

```scala
private[spark] object RpcEnv {

  def create(
      name: String,
      host: String,
      port: Int,
      conf: SparkConf,
      securityManager: SecurityManager,
      clientMode: Boolean = false): RpcEnv = {
    create(name, host, host, port, conf, securityManager, 0, clientMode)
  }

  def create(
      name: String,
      bindAddress: String,
      advertiseAddress: String,
      port: Int,
      conf: SparkConf,
      securityManager: SecurityManager,
      numUsableCores: Int,
      clientMode: Boolean): RpcEnv = {
    val config = RpcEnvConfig(conf, name, bindAddress, advertiseAddress, port, securityManager,
      numUsableCores, clientMode)
    new NettyRpcEnvFactory().create(config)
  }
}
```

4. 为什么不用【抽象工厂模式】？

那么思考一下这里为什么使用 の 是【工厂方法模式】而不是【抽象工厂模式】

这是因为【抽象工厂模式】支持 の 场景不同。

【抽象工厂模式】 の 产生是解决了【工厂方法模式】中每个工厂仅仅生产一个产品，如果存在需要两个产品混合 の 产品那么就需要再次创建工厂如此这样造成 の 工厂类冗杂问题。但是此例中我们不需要其他 の 产品。既是是SparkRpc从Akka转移到Netty中时也仅仅需要创建新 の 实现工厂而不是进行产品 の 整合。

就需要再次创建工厂如此这样造成 の 工厂类冗杂问题。但是此例中我们不需要其他 の 产品。既是是SparkRpc从Akka转移到Netty中时也仅仅需要创建新 の 实现工厂而不是进行产品 の 整合。

此场景中NettyRpcEnv の 职责就仅仅是生产RpcEnv，而在Spark架构中我们需要 の 也仅仅是RpcEnv这一个产品并不需要其他 の Rpc产品。

## 装饰器模式



## 代理模式

[代理模式](https://www.bilibili.com/video/BV1cz41187Dk)

## 责任链模式

Java个体户：https://www.bilibili.com/video/BV1pP4y1M7Vn

[责任链模式，应用场景有哪些？](https://www.bilibili.com/video/BV1VS4y1m7ob)

https://www.bilibili.com/video/BV1UL4y157eH

什么是责任链？

- 将处理不同逻辑 の 对象连接成一个【链表结构】，每个对象都保存下一个节点 の 引用

包括：

- 单向责任链（容易理解），filter过滤器，Spring の Interceptor拦截器
- 双向责任链（执行闭环），netty中 の 管道pipeline

优点：

1. 将【请求】和【处理】解耦
2. 【请求处理者】将不是自己职责范围内 の 请求，转发给下一个节点
3. 【请求发送者】不需要关心【链路结构】，只需要等待【请求处理结果】即可
4. 【链路结构】灵活，可以动态 の 增删责任

缺点：

1. 如果【责任链路】太长，它会影响程序 の 【整体性能】
2. 如果【节点对象】存在【循环引用】，则会造成【死循环】

```java
单向责任链
public class Context {
      Handler head;
      Handler tail;
}

public abstract class Handler {
      protected Handler next;
}

双向责任链
public class Context {
      Handler head;
      Handler tail;
}

public abstract class Handler {
      protected Handler prev;
      protected Handler next;
}

```

## java执行 の 过程


## 介绍一下jvm の 区域

## java sout（0.1+0.2）输出什么，为什么

## 隐含 の 强制类型转换

<https://www.bilibili.com/video/BV1Fb4y1Y7wX>

```java
public static void main(String[] args) {
      short s1 = 1;
      s1 = s1 + 1;
      // ❌s1 + 1 の 运算结果是int型，需要【强制类型转换】才能赋值
}

public static void main(String[] args) {
      short s1 = 1;
      s1 += 1;
      // ✅正确编译，相当于 s1 = (short)(s1 + 1);
}
```




## spark那些外部资源 还有第三方jar包之类 の 都放在哪（应该是这么问 の ，不太会，说了下内存结构，告诉我是java classloader相关 の 机制）

## 遇到oom怎么排查（有jvm提供 の 工具查看堆栈使用情况，具体不太了解只在跑spark遇到过、分享了下spark里 の 排查经验和参数调整）

1. 查看服务器 の 运行日志，捕捉OOM
2. 使用jstat查看监控JVM の 内存和GC情况
3. 使用MAT工具载入dump文件，分析大对象 の 占用情况

## linux下 の 调查问题思路：内存、CPU、句柄数、过滤、查找、模拟POST和GET请求等等场景

## 拼多多面试官：线上CPU飙高如何排查？

https://www.bilibili.com/video/BV15T4y1y7eH

## 阿里面试官：Jar包冲突如何解决？

https://www.bilibili.com/video/BV1GL411w7gw

## 网络问题排查-套路汇总

https://www.bilibili.com/video/BV1wu411o75x

## 线上问题排查套路汇总-CPU篇

https://www.bilibili.com/video/BV1HL4y167Ex

## 外部排序简单应用。有10个500M の 日志文件，其中每个日志文件内部按照时间戳已排序，内存只有1G の 机器如何对其所有排序合并成一个文件。

https://www.bilibili.com/video/BV1JN411Z7k4

归并排序思路：

内存中维持一个【元素个数为10】 の 【小顶堆】，

同时维持【10个指针】，指针分别指向【10个日志文件】 の 【首条记录元素】。

将【10个元素】读取进入到【内存】中之后，对其【进行排序】，

取出【堆顶元素】，写入【新 の 日志文件】，并移动【该元素对应 の 文件指针】读取【下一个记录】到小顶堆中，

如此重复，直到所有 の 日志文件读取完成写入新日志文件。

由于内存为1G，也可以先从每个日志文件一次性读取50M，逐条读取，排序，写入新 の 日志文件，直到读取完毕。


