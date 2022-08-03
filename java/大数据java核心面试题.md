https://www.bilibili.com/video/BV1KL4y1K7Ry

## JVM性能优化 - 存在哪些问题？

- GC 频繁
- 死锁
- oom
- 线程池不够用
- CPU负载过高

## JVM性能优化 - 如何排查问题？

1. 打印出 `GC log`，查看 minor GC 和 major GC
2. `jstack` 查看【堆栈信息】
3. 应用 `jps，jinfo，jstat，jmap`等命令

## JVM性能优化 - 如何解决问题？

1. 适当增加【堆内存大小】
2. 选择合适的【垃圾收集器】
3. 使用 zk，redis 实现【分布式锁】
4. 利用 kafka 实现【异步消息】
5. 代码优化，及时释放【资源】
6. 增加集群节点数量


## jvm内存结构介绍

Java の 内存分为5个部分：

- 线程共享：
  - 方法区(method area) 🔸 常量池 + 静态变量 + 类
  - 堆(heap) 🔸 GC 对象，即【实例对象】，内存最大 の 区域


- 每个线程一份【线程隔离】：
  - 虚拟机栈(VM stack)→(heap) 🔸 每个java被调用时，都会创建【栈帧】
    - `栈帧`内包含：
      - `局部变量表`
      - `操作数栈`
      - `动态链接`
      - `返回出口`
  - 本地方法栈 🔸 native语言
  - 程序计数器(pc register) 🔸【当前线程】执行到 の 【字节码行号】

[了解JVM内存区域吗？](https://www.bilibili.com/video/BV1Vt4y1V7cz)

[2分钟学会对象和引用如何存储在JVM内存](https://www.bilibili.com/video/BV1S5411Q78u)

## 【pass💦】Java 中有多少种引用类型？它们 の 都有什么使用场景？

https://www.bilibili.com/video/BV1e94y1D7ug

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

## Java 内存模型定义了八种操作来实现

Java 内存模型定义了八种操作来实现

## 【pass💦】为了更好 の 控制【主内存】和【本地内存】 の 交互，Java 内存模型定义了八种操作来实现：

lock：锁定。 主内存 の 变量，把一个变量标识为一条线程独占状态。
unlock：解锁。 主内存变量，把一个处于锁定状态 の 变量释放出来，释放后 の 变量才可以被其他线程锁定。
read：读取。 主内存变量，把一个变量值从主内存传输到线程 の 【工作内存】中，以便随后 の load动作使用
load：载入。 【工作内存】 の 变量，它把read操作从主内存中得到 の 变量值放入【工作内存】 の 变量副本中。
use：使用。 【工作内存】 の 变量，把【工作内存】中 の 一个变量值传递给【执行引擎】，每当虚拟机遇到一个需要使用变量 の 值 の 字节码指令时将会执行这个操作。
assign：赋值。 【工作内存】 の 变量，它把一个从【执行引擎】接收到 の 值赋值给【工作内存】 の 变量，每当虚拟机遇到一个给变量赋值 の 字节码指令时执行这个操作。
store：存储。 【工作内存】 の 变量，把【工作内存】中 の 一个变量 の 值传送到主内存中，以便随后 の write の 操作。
write：写入。 主内存 の 变量，它把store操作从【工作内存】中一个变量 の 值传送到主内存 の 变量中。

## jvm内存中，堆和栈 の 区别？堆和栈 の 关系

https://www.bilibili.com/video/BV1RW411C7yb

|   |  stack |  heap |
|---|---|---|
| 存储  | 局部变量、引用  | instance 对象  |
| 速度  |  fast |  slow |
| 线程共享  | Thread Stack  | 共享 heap  |
| GC |   | GC 对象，占用内存最大  |
| 指向关系 | 出  | 入  |

## 堆内内存

年轻代【Eden、From、To】 + 老年代 + 持久代

## 堆外内存【定义】：

堆外内存 = 物理机内存

【java虚拟机堆】以外的内存，这个区域是受【操作系统】管理，而不是jvm。

## 堆外内存优点

减少jvm垃圾回收

加快数据复制的速度

## Linux查看内存、CPU等状态？Linux查看进程 の 内存消耗和CPU消耗？

查看内存使用情况：`free`

显示进程信息（包括CPU、内存使用等信息）：`top、ps`

## 什么是【虚拟内存】和【物理内存】

物理内存：【内存条】存储容量的大小

虚拟内存：是【计算机内存管理技术】，它让程序认为它具有【连续可用】的【4GB 内存】，而实际上，映射到多个【物理内存碎片】

## HashMap 和 HashTable の 区别

https://www.bilibili.com/video/BV1Dh411J72Y

| HashMap  |  HashTable |
|---|---|
| 非同步  | 线程同步，线程安全，有关键字 synchronized  |
| 允许  | 不允许 k-v 有 null值  |
| hash数组的默认大小是 16 | hash数组的默认大小是 11 |
| hash算法不同：增长方式是 2的指数  | hash算法不同：增长方式是 2*old + 1 |
| 继承 AbstractMap类  | 继承 Dictionary类，Dictionary类 已经被废弃  |

## HashMap 有哪些【线程安全】的方式

方式一：通过 Collections.synchronizedMap() 返回一个新的 Map

- 优点：代码简单
- 缺点：用了【锁】的方法，性能较差

方式二：通过java.util.concurrent.ConcurrentHashMap

- 将代码拆分成独立的segment，并调用【CAS指定】保证了【原子性】和【互斥性】
- 缺点：代码繁琐
- 优点：【锁碰撞】几率低，性能较好

## HashTable是如何保证线程安全的？

HashTable 给整张表添加一把【大锁】，把整张表锁起来，大幅度降低了效率。

为了提高效率，引入 ConcurrentHashMap

## ConcurrentHashMap 是如何提高效率的？

HashTable 给整张表添加一把【大锁】

而 ConcurrentHashMap 采用【分段锁】

## 什么是【分段锁】？

就是将数据【分段】，每段加一把锁

## java8的ConcurrentHashMap放弃分段锁，采用node锁。why？

node锁，粒度更细，提高了性能，并使用 `CAS 算法`保证原子性

## 什么是CAS

CAS，全称为Compare and Swap，

即比较-替换。

假设有三个操作数：`内存值V`、旧的`预期值A`、要`修改的值B`

当且仅当`预期值A`和`内存值V`相同时，才会将`内存值V`修改为B并返回true，否则什么都不做并返回false。

只要某次CAS操作失败，永远都不可能成功

当然CAS一定要`volatile变量`配合，

这样才能保证每次拿到的变量是`主内存`中`最新的那个值`

## voliate是怎么保证可见性的

Java 内存模型定义了八种操作，来控制【主内存】和【本地内存】 の 交互：

除了 lock 和 unlock，还有read、load、use、assign、store、write

- read、load、use 作为一个原子
- assign、store、write作为后一种原子操作

从而避免了在操作过程中，被【打断】，从而保证【工作内存】和【主内存】中的数据都是【相等的】。

应用场景：变量赋值 flag = true，而不适用于 a++

## volatile和synchronized区别

| volatile关键字  |  synchronized关键字 |
|---|---|
|  轻量，无锁 |  有锁 |
|  性能好，不会发生阻塞 |  开发中使用更多，可能会发生阻塞 |
|  保证: 有序性，可见性，不能保证 原子性 ✖ |  保证: 三大性，原子性，有序性，可见性 |
|  目的: 变量 在`多个线程`之间的 可见性 | 目的: `多个线程`之间`访问资源`的 同步性 |
|  作用于: 变量 | 作用于: 方法 + 代码块 |

## lock与synchronized区别

```java
lock.lock();
// ...
lock.unlock();

sychronized{
  // ...
}
```

| lock  | synchronized  |
|---|---|
| 接口  | 关键字  |
| 【手动】获得锁，释放锁  | 【自动】获得锁，释放锁     |
| 适用于：线程方程多   |  适用于：线程少  |

## synchronized 的作用

作用在：静态方法、实例方法、this代码块、class代码块

## synchronized 的执行流程：

`start` + `acc_synchronized` + `moniter enter指令` + `计数器加一` + 执行方法 + `计数器减一` + `monitor exit指令` + end



https://www.bilibili.com/video/BV17W411S7jH

## Volatile 关键字原理

https://www.bilibili.com/video/BV1SW411U7QM

https://www.bilibili.com/video/BV1KU4y1q7yF

https://www.bilibili.com/video/BV1Ny4y1u7L6

https://www.bilibili.com/video/BV1x3411B7GE


## synchronized 关键字

https://www.bilibili.com/video/BV1q54y1G75e

https://www.bilibili.com/video/BV18y4y1V79v






## HashMap、Hashtable、ConcurrentHashMap、LinkedHashMap、TreeMap


[](https://www.bilibili.com/video/BV1pq4y157sa)

## Hashmap和Treemap の 区别

## HashMap默认大小，扩容机制？

## 追问：HashMap在哪个jdk版本使用红黑树，之前 の 实现方法是什么？

## hashmap put过程

## 如何确定一个对象是【垃圾】？

引用计数法：只要【程序】中持有【该对象的引用】，就说明【该对象不是垃圾】

如果一个对象，没有【指针】对其引用，它就是垃圾

- 注意：如果AB互相引用，则会导致【永远不能被回收】，导致【内存溢出】

## 为什么 Eden:S0:S1 是 8:1:1 ?



## 新生代、老年代

Eden From To 老年代

## Minor GC 和 Full GC 有什么不同？

Minor GC / Young GC: 指发生新生代 の 垃圾收集动作
Major GC / Full GC: 指发生新生代 の 垃圾收集动作

## JVM是如何避免Minor GC时扫描全堆的?

https://www.bilibili.com/video/BV1h64y1x7SF

## java GC算法，标记-清楚、标记-复制、标记-整理

## 4.g1回收器、cms の 回收过程，场景

[CMS比较严重 の 问题并发收集阶段再次触发Full gc怎么处理](https://www.bilibili.com/video/BV1BA4y1R7W7)

[G1垃圾收集器最大停顿时间是如何实现 の ](https://www.bilibili.com/video/BV1TB4y1s7rh)

## G1垃圾收集器的【内存布局】

与【其他收集器】有很大的差别

它将【heap】划分成多个大小相等的Region，虽然仍然划分为【Eden、Survivor、Old、Humongous、Empty】

每个 Region 大小，都在 1M ~ 32M 之间，必须是 2的N 次幂

特点：

1. 仍然保留了【分代】的概念
2. 不会导致【空间碎片】
3. 可以明确规定在【M毫秒的时间段】，消耗在【垃圾收集】的时间。不得超过N毫秒。

## 什么情况下使用G1垃圾收集器？

1. 50%以上的heap被live对象占用
2. 【对象分配】和【晋升的速度】变化非常大
3. 垃圾回收【时间】比较长

## 这几个分区在gc里都有什么处理，不同分区 の gc策略

[GC是什么时候都能做吗？知道GC安全点与安全区域是怎么回事吗？](https://www.bilibili.com/video/BV1oZ4y1q7tu)

## spark那些外部资源 还有第三方jar包之类 の 都放在哪（应该是这么问 の ，不太会，说了下内存结构，告诉我是java classloader相关 の 机制）

## java掌握到什么程度；

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


## 内存溢出

https://www.bilibili.com/video/BV1gW411r7By

## equals和==区别？

https://www.bilibili.com/video/BV1cW411Z7XB

https://www.bilibili.com/video/BV13q4y1d7kg

## 为什么重写equals要重写hashcode？

https://www.bilibili.com/video/BV1o34y127Pm

## ArrayList和LinkedList の 区别？

|  ArrayList | LinkedList   |
|---|---|
| 底层是【数组】  | 底层是【链表】  |
| 查询，速度更快  | 增删，速度更快  |
|   | 更占内存  |


https://www.bilibili.com/video/BV1QW41167Gn

## ArrayList默认大小，扩容机制？扩容 の 时候如何将旧数组转化为新数组？



## 线程 の 创建方式？

## 多线程了解吗？（当时脑子一抽，我以为要写多线程代码，回答了不熟悉，他就没多问了）



## Objects类中有哪些方法？

## equals()和hashcode()了解吗？

## 排序算法和时间复杂度

https://www.bilibili.com/video/BV1Sg411M7Cr

## 其中hashCode方法 の 返回值是什么？

https://www.bilibili.com/video/BV1WR4y1J7T4

## 类加载机制

就是：把【Class文件】加载到【内存】，进行【验证、准备、解析】，然后初始化，可以形成JVM可以直接使用的java.lang.Class

- 装载
- 链接 (验证、准备、解析)
- 初始化
- 使用
- 卸载

## 【Java面试题】类加载器

|   |   |   |
|---|---|---|
| 4️⃣ Bootstrap ClassLoader  | 【启动】类加载器  | 加载`\jre\lib`的类库  |
| 3️⃣ Ext ClassLoader  | 【拓展】 类加载器 | 加载`\jre\lib\ext`的类库  |
| 2️⃣ App ClassLoader  | 【系统】 类加载器 | 加载`classpath`的类库  |
| 1️⃣ Custom ClassLoader  | 【自定义】 类加载器 | 加载`用户`类库  |


https://www.bilibili.com/video/BV1JW411r758


## 我们自己定义 の java.lang.String类是否可以被类加载器加载

https://www.bilibili.com/video/BV1MF411e7zY

## String类是否可以被继承

https://www.bilibili.com/video/BV1ev411g7Xk

## 重写hashCode或equals方法需要注意什么？

## 如何创建一个线程？

## java执行 の 过程

## jvm存在 の 意义

## 8大基本类型
 
## string list set map数据结构 和基本类型有什么区别

## arraylist 和linkedlist 区别，为什么arrylist查询快

https://www.bilibili.com/video/BV1xe4y1973f

## 线程 & 进程 の 区别

https://www.bilibili.com/video/BV1xS4y1t7mR

## 怎么实现多线程顺序输出下面这，ABC是三个不同 の 线程

## final 值 方法 类有什么区别，容器被final修饰会怎样

[2分钟记住final の 使用](https://www.bilibili.com/video/BV19A4y1o7ET)

## 线程安全 の 单例模式

java集合

介绍一下jvm の 区域

ClassA a=new Class(1) 在jvm中怎么存储

## int和integer有什么区别

https://www.bilibili.com/video/BV1ZP4y187Tk

## java方法是值传递还是对象传递

https://www.bilibili.com/video/BV1xL4y1w7jy


## 说一下你对多线程 の 理解

threadlocal oom是为什么

java sout（0.1+0.2）输出什么，为什么

## Java面向对象三大特性

https://www.bilibili.com/video/BV1rf4y1E7u9

## 面向对象 の 【三大特性】

封装、继承、多态

## 说下对象完整创建流程

https://www.bilibili.com/video/BV1J3411G7wA

## 什么时候用多态

https://www.bilibili.com/video/BV18W411C7TC

## Java中 の 反射（不了解，没问）

https://www.bilibili.com/video/BV1L34y1S7a1

## 隐含 の 强制类型转换

https://www.bilibili.com/video/BV1Fb4y1Y7wX



Java集合 の 框架体系图



5.遇到oom怎么排查（有jvm提供 の 工具查看堆栈使用情况，具体不太了解只在跑spark遇到过、分享了下spark里 の 排查经验和参数调整）

12.用过多线程吗（了解，没具体使用过，就不问了？？八股我都准备输出了）

## java中类和对象 の 关系

https://www.bilibili.com/video/BV1GR4y1p7qw

## 访问修饰符

https://www.bilibili.com/video/BV1bf4y1c7Pu

## 常用哪些数据集合，介绍hashset

[HashSet内部是如何工作 の ](https://www.bilibili.com/video/BV1sq4y1971k)

## threadlocal原理，应用场景

## 线程池 の 工作原理

https://www.bilibili.com/video/BV1T34y1p7ih

https://www.bilibili.com/video/BV19L411n7mL

## 垃圾收集器分类

串行收集器：只有一个【垃圾回收线程】，【用户线程】暂停

并行收集器(吞吐量优先)：多个【垃圾回收线程】，【用户线程】暂停

并发收集器(停顿时间优先)(CMS、G1)：【垃圾回收线程】、【用户线程】同时

## CMS 垃圾收集器

全称 Concurrent Mark Sweep

目标：获取【最短回收停顿时间】

特点：

- 采用【标记-清除】算法
- 【内存回收过程】与【用户线程】并发执行

## 垃圾回收

https://www.bilibili.com/video/BV1aW41167rS

https://www.bilibili.com/video/BV1xK4y197SH

[什么样 の 对象会被老年代回收](https://www.bilibili.com/video/BV13K4y1G7Bs)

## 什么时候才会进行垃圾回收？

1. GC 是由 JVM 【自动】完成的。
   1. 当【Eden区 or S区】不够用
   2. 【老年代】不够用
   3. 【方法区】不够用
2. 也可以通过 `System.gc()方法` 【手动】垃圾回收

但【上述两种方法】无法控制【具体的回收时间】




## java の 锁原理

## 手写单例模式

https://www.bilibili.com/video/BV1834y1m7Eq

https://www.bilibili.com/video/BV1Fo4y127zN

https://www.bilibili.com/video/BV1iW411S76k



浏览器从url输入到返回 の 流程

1T文件，每行是一个数字，机器128G内存，求top10数字

java基本数据类型




 spark executor内 の task是怎么彼此隔离 の （从线程池 の 角度，还有切分stage）

 进程和线程区别，线程和进程切换过程

 线程进程

 线程和进程



进程 の 地址空间

说一下进程和线程

进程之间通信 の 方式

进程切换 の 过程

为什么我们一般都用线程来接收请求（好像是这样问 の ）

## 如何保证线程安全

[List の 线程安全实现有哪些](https://www.bilibili.com/video/BV1ag41177nn)

如何进行etl链路优化

如果链路慢，但是没有sla风险，怎么优化（类似上一道题）

死锁是什么

如何破坏死锁

怎么数据链运行慢定位问题，对任务进行优化

死锁 の 产生条件还有如何避免

1.1 数据库锁表 の 相关处理
1.2 索引失效场景

## Mysql锁有哪些，如何理解

https://www.bilibili.com/video/BV1ff4y1Z7VQ

## 高并发下如何做到安全 の 修改同一行数据，乐观锁和悲观锁是什么，INNODB の 行级锁有哪2种，解释其含义

[并发编程没你想得那么难学，并发学习指南|附学习资料](https://www.bilibili.com/video/BV1br4y1C72X)



1.4 数据库会死锁吗，举一个死锁 の 例子，mysql怎么解决死锁








1.1 hash算法 の 有哪几种，优缺点，使用场景
1.2 什么是一致性hash

Hash表是怎么实现 の 。

去重如果不用 set 还有多少种方式，有没有更高效 の 方式？
hashcode()方法原理
怎么解决幻读 具体在sql是怎么实现 の 

## mvcc知道吗

全称 Multi-Version Concurrency Control

多版本并发控制

指的是：

维持一个数据的【多个版本】，使得没有【读写冲突】

目的：

- 提高【数据库】的【并发访问】性能
- 处理【读写冲突】。即使有【读写冲突】，也能【不加锁】，【非阻塞、并发读】

## 1.1 维度建模有什么好处？如果业务需求增加一个维度，后续需要做哪些工作？


1.2 怎么判断一个需求能不能实现，你们 の 判断标准是什么？需求变更要做什么？

1.3 增加一个维度后发现查询 の 速度变得非常慢，是什么原因导致 の ？

## 大 の log文件中，统计异常出现 の 次数、排序，或者指定输出多少行多少列 の 内容。

https://www.bilibili.com/video/BV1sr4y127b7

## linux下 の 调查问题思路：内存、CPU、句柄数、过滤、查找、模拟POST和GET请求等等场景

1.6 分表之后想让一个id多个表是自增 の ，效率实现


1.7 事物 の 四个特性，以及各自 の 特点（原子、隔离）等等，项目怎么解决这些问题

## 一条sql语句运行慢，如何做。怎么处理MySQL的慢查询？

（慢查询，看索引，看orderby和groupby）

1. 开启【慢查询】日志，准确定位到【某个sql语句】
2. 分析【sql语句】，看看是否load了【不需要的列】
3. 分析【执行计划】，看其【索引】的使用情况，然后修改【语句or索引】，使得【语句】尽可能命中【索引】
4. 如果上述优化，还是不够。看看表中【数据量】是否过大，可以【横向or纵向】分表

https://www.bilibili.com/video/BV1N34y17715

## 索引 の 设计原则

在进行索引设计的时候，应该：

1. 经常用于 where 或 join 的列
2. 小表没必要建立索引
3. 建立索引的列没必要太多
4. 频繁更新的字段不要有索引

https://www.bilibili.com/video/BV1WB4y1s7kD


## 事务 の 特性，隔离 の 级别。

## 分布式session中用 の 是什么数据结构

# MySQL

## zk の watch机制实现原理

https://www.bilibili.com/video/BV1D3411w7wK

## 简述zk の 命名服务、配置管理、集群管理

https://www.bilibili.com/video/BV1r3411F7id

## explain sql 执行计划 の 各列参数

最重要的几个参数

| 参数  |  含义 |
|---|---|
|  id | select 查询的序列号  |
| table  |   |
|  type | 访问类型  |
|  key |   |
| rows  | 行数的预估值  |
| extra  | 其他信息   |

type 访问类型：效率由【好】到【坏】依次是

system > eq_ref > range > all

- system: 表只有一行记录，平时不出现
- eq_ref: 使用【唯一索引】进行【数据查找】
- range: 范围查询
- all: 全表扫描


https://www.bilibili.com/video/BV1mh411q7u5

[Explain语句结果中各个字段分表表示什么](https://www.bilibili.com/video/BV1gt4y1a7sE)


## 数据库视图

## 数据库 の 三大范式，都有哪些区别，举例说明


## 数据库引擎mysaim和innoDB の 区别

| mysaim  | innoDB  |
|---|---|
| OLAP  | OLTP  |
| 不支持【事务】  |  支持【事务】 |
| 支持【表锁】【全文索引】  | 支持【行锁】【外键】【自增】【MVCC模式的读写】 |
| 非聚簇索引  | 非聚簇索引 + 聚簇索引  |
|   | 读慢，写快  |
|   | 【一行一行】删除  |


## innodb の 索引结构

https://www.bilibili.com/video/BV1pS4y1v7ew

4.为什么 选用mongo

7.为什么用mongo


2. 数据库有哪些类型
- SQL和NoSQL区别

## 分布式ID有哪些解决方案？

https://www.bilibili.com/video/BV1ea411H7RR

## 什么是事务

https://www.bilibili.com/video/BV1RS4y1d7kr

## mysql索引是什么结构，索引存储int和字符串有什么区别（不会）

mysql索引设计原则

说说 MySQL  の 索引？b树与b+树 の 区别？

## mysql索引

hash索引 + B+树

## mysql の 引擎

innoDB

mysql の 引擎和innodb这类数据库 の 区别

## mysql索引相关 の 问题，b+树

[讲讲mysql の 索引及B树和B+树](https://www.bilibili.com/video/BV1Dq4y1S7GZ)

https://www.bilibili.com/video/BV18U4y197Dx

## MySQL 索引，存数据库，还是存磁盘

## 【聚簇索引】和【非聚簇索引】如何区别？

只需要判断【数据】和【索引】是否存在一起

## mySQL是【聚簇索引】，还是【非聚簇索引】？

与【存储引擎】相关

innoDB：都有。

- `xxx.ibd`包含【数据】 + 【索引】
- 建立【聚簇索引】占用的空间更大，一旦【聚簇索引】改变，那么【非聚簇索引】也要跟着变

myisam：只有【非聚簇索引】

- `xxx.myd`包含【数据】
- `xxx.myi`包含【索引】

## 你们 ADS 层 の 数据量每天 の 数据量有多大？ADS 层再 MySQL 中 の 表是怎么创建 の ？有什么注意事项？索引怎么创建 の ？

1.1 MySql の 存储引擎 の 不同

1.2 Mysql怎么分表，以及分表后如果想按条件分页查询怎么办(如果不是按分表字段来查询 の 话，几乎效率低下，无解)

## MySql の 主从实时备份同步 の 配置，以及原理(从库读主库 の binlog)，读写分离

## MySQL 为什么要主从同步？

1. 主从同步：主库写，从库读，即使主库【锁表】，【从库】仍然可以正常运行
2. 做【数据的备份】
3. 架构扩展：当业务量越来越大，多库存储，可以提高单个机器的IO性能

1.1 MySQL InnoDB存储 の 文件结构
1.2 索引树是如何维护 の ？
1.3 数据库自增主键可能 の 问题
1.4 MySQL の 几种优化
1.5 mysql索引为什么使用B+树

1.1 mySQL里有2000w数据，redis中只存20w の 数据，如何保证redis中 の 数据都是热点数据

## mysql事务ACID特性，隔离级别

A 原子性：一个【事务】的【所有操作】要么全部完成，要么全部失败
C 一致性：比如，一次转账。某一账户扣除的金额，必须与另一个账户存入的金额，相等
I 隔离性：【事务】与【事务】之间互不影响
D 持久性：对数据的【修改】，必须在事务【结束】前，保存至某种【物理存储设备】。

## oracle和mysql の 默认事务隔离级别为什么不同

https://www.bilibili.com/video/BV11U4y1J7v7

项目中MySQL の 部署方式是什么，怎么保证数据库数据是高可用 の 
有考虑过数据库比如MySQL和MongoDB数据库数据丢失问题吗

## mysql隔离机制 默认 の 是什么

mysql【执行引擎】

说一下mysql常用 の 引擎

hash和数组分别作为mysql索引 の 结果。

## B+树是二叉树吗 平衡树吗

是【平衡】的【多叉树】

## B+树 の 特点。

从【根节点】到【叶子节点】的【高度差】 不超过1

同一层级：有指针连接

## 哈希索引

采用【哈希算法】，将【键值】换算成【哈希值】，检索时，不需要像【b+树】一样【逐级查找】。

所以，只要【一次哈希】就能定位到【响应的位置】，速度非常快。

如果key不唯一，则需要再根据【链表】，往后扫描

适合：

- 【等值查询】

不适合：

- 范围查询
- 无法排序
- like这样的模糊查询
- 多列联合索引的【最左匹配原则】

## b+树底层是双向链表还是单向

##  红黑树和跳表 の 区别

##  mysql的【索引类型】有哪些？

- 普通索引
- 唯一索引
- 逐渐索引
- 联合索引
- 全文索引


