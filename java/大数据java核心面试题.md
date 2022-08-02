[程序员小邱](https://space.bilibili.com/474720451)

[起点编程](https://space.bilibili.com/507861978)

[大黄奔跑](https://space.bilibili.com/35654218)

[andjiawei](https://space.bilibili.com/396770061)

[IT简明](https://space.bilibili.com/1400081125)

[jave面试帮手先森](https://space.bilibili.com/1192365718)

[Tim数据工程师](https://space.bilibili.com/368778383)



## jvm内存结构介绍

Java的内存分为5个部分：

- 线程共享：
  - 方法区(method area) 🔸 常量池 + 静态变量 + 类
  - 堆(heap) 🔸 GC 对象，即【实例对象】，内存最大的区域


- 每个线程一份【线程隔离】：
  - 虚拟机栈(VM stack)→(heap) 🔸 每个java被调用时，都会创建【栈帧】
    - `栈帧`内包含：
      - `局部变量表`
      - `操作数栈`
      - `动态链接`
      - `返回出口`
  - 本地方法栈 🔸 native语言
  - 程序计数器(pc register) 🔸【当前线程】执行到的【字节码行号】

https://www.bilibili.com/video/BV1Vt4y1V7cz

[2分钟学会对象和引用如何存储在JVM内存](https://www.bilibili.com/video/BV1S5411Q78u)

## java内存模型



## Java 中有多少种引用类型？它们的都有什么使用场景？

https://www.bilibili.com/video/BV1e94y1D7ug



并发编程2个问题：线程间如何通信？消息如何同步？

有2种并发模型，可以解决这两个问题。

【消息传递】并发模型、【共享内存】并发模型


对于每个【线程】，stack是私有的，而heap是共享的，也就是说，stack中的变量，不会在【线程】间【共享】，也就不会有【内存可见性】的问题。也就不会受到【内存模型】的影响，

而堆中的变量是【共享变量】，就会有【内存可见性】的问题。

但为什么【堆】中会有内存不可见的问题呢？

现代计算机为了【计算高效】，往往会在【高速缓存区】缓存【共享变量】，因为【CPU】访问【缓存区】要比访问【内存】要快得多。

【线程】之间的【共享变量】存储在【主内存】中，每个【线程】都有一个私有的【本地内存】，存储了该【线程】读写【共享变量】的副本。【本地内存】是【java内存模型】的一个抽象概念，并不是真实存在的。

java【线程】之间的通信由JMM控制，从抽象的角度来说，JMM定义了【线程】与【主内存】之间的【抽象关系】

如果【线程A】要与【线程B】通信：

必须经历下面2个步骤：

【线程A】将【本地内存a】中更新过的【共享变量】刷新到【主内存】中，

【线程B】在【本地内存】中，找到这个【共享变量】，发现这个【共享变量】已经被更新了，然后到【主内存】读取【线程A】更新过的【共享变量】，并拷贝到【本地内存中】

也就是说，不能直接访问

线程间通信必须经过【主内存】

【线程】对【共享变量】的操作，必须在自己的【本地内存】中进行，不能直接从【主内存】中读取。

java内存模型，通过控制【主内存】和【本地内存】之间的交互，来提供内存的可见性保证。

------------------------------------------------------------

由于CPU 和主内存间存在数量级的速率差，想到了引入了多级高速缓存的传统硬件内存架构来解决，多级高速缓存作为 CPU 和主内间的缓冲提升了整体性能。解决了速率差的问题，却又带来了缓存一致性问题。

数据同时存在于高速缓存和主内存中，如果不加以规范势必造成灾难，因此在传统机器上又抽象出了内存模型。

Java 语言在遵循内存模型的基础上推出了 JMM 规范，目的是解决由于多线程通过共享内存进行通信时，存在的本地内存数据不一致、编译器会对代码指令重排序、处理器会对代码乱序执行等带来的问题。

为了更精准控制工作内存和主内存间的交互，JMM 还定义了八种操作：lock, unlock, read, load,use,assign, store, write。

## 项目中哪些位置用到了多线程呢？

https://www.bilibili.com/video/BV1nr4y1L7Pj

## 为了更好的控制主内存和本地内存的交互，Java 内存模型定义了八种操作来实现：

lock：锁定。作用于主内存的变量，把一个变量标识为一条线程独占状态。
unlock：解锁。作用于主内存变量，把一个处于锁定状态的变量释放出来，释放后的变量才可以被其他线程锁定。
read：读取。作用于主内存变量，把一个变量值从主内存传输到线程的工作内存中，以便随后的load动作使用
load：载入。作用于工作内存的变量，它把read操作从主内存中得到的变量值放入工作内存的变量副本中。
use：使用。作用于工作内存的变量，把工作内存中的一个变量值传递给执行引擎，每当虚拟机遇到一个需要使用变量的值的字节码指令时将会执行这个操作。
assign：赋值。作用于工作内存的变量，它把一个从执行引擎接收到的值赋值给工作内存的变量，每当虚拟机遇到一个给变量赋值的字节码指令时执行这个操作。
store：存储。作用于工作内存的变量，把工作内存中的一个变量的值传送到主内存中，以便随后的write的操作。
write：写入。作用于主内存的变量，它把store操作从工作内存中一个变量的值传送到主内存的变量中。

## jvm内存中，堆和栈的区别？

既然提到了内存结构，那就谈谈内存结构，堆外内存有什么优势（我脑子里面想的全都是劣势...）

Linux查看内存、CPU等状态？查看进程的内存消耗和CPU消耗？

4.内存优化做过吗，怎么处理

虚拟内存和物理内存

什么是虚拟内存


## 新生代、老年代

Eden From To 老年代

## Minor GC 和 Full GC 有什么不同？

Minor GC / Young GC: 指发生新生代的垃圾收集动作
Major GC / Full GC: 指发生新生代的垃圾收集动作

## spark那些外部资源 还有第三方jar包之类的都放在哪（应该是这么问的，不太会，说了下内存结构，告诉我是java classloader相关的机制）

## java掌握到什么程度；

## java集合有哪些？

## 内存溢出

https://www.bilibili.com/video/BV1gW411r7By

## equals和==区别？

https://www.bilibili.com/video/BV1cW411Z7XB

https://www.bilibili.com/video/BV13q4y1d7kg

## 为什么重写equals要重写hashcode？

https://www.bilibili.com/video/BV1o34y127Pm

## ArrayList和LinkedList的区别？

https://www.bilibili.com/video/BV1QW41167Gn

## ArrayList默认大小，扩容机制？扩容的时候如何将旧数组转化为新数组？

## HashMap默认大小，扩容机制？

## 追问：HashMap在哪个jdk版本使用红黑树，之前的实现方法是什么？

## 线程的创建方式？

## 多线程了解吗？（当时脑子一抽，我以为要写多线程代码，回答了不熟悉，他就没多问了）


## java GC算法，标记-清楚、标记-复制、标记-整理

## Objects类中有哪些方法？

## equals()和hashcode()了解吗？

## 排序算法和时间复杂度

https://www.bilibili.com/video/BV1Sg411M7Cr

## 其中hashCode方法的返回值是什么？

https://www.bilibili.com/video/BV1WR4y1J7T4

## 【Java面试题】类加载器

https://www.bilibili.com/video/BV1JW411r758


## 我们自己定义的java.lang.String类是否可以被类加载器加载

https://www.bilibili.com/video/BV1MF411e7zY

## String类是否可以被继承

https://www.bilibili.com/video/BV1ev411g7Xk

## 重写hashCode或equals方法需要注意什么？

## 如何创建一个线程？

## java执行的过程

## jvm存在的意义

## 8大基本类型
 
## string list set map数据结构 和基本类型有什么区别

## arraylist 和linkedlist 区别，为什么arrylist查询快

https://www.bilibili.com/video/BV1xe4y1973f

## 线程 & 进程的区别

https://www.bilibili.com/video/BV1xS4y1t7mR

## 怎么实现多线程顺序输出下面这，ABC是三个不同的线程

## final 值 方法 类有什么区别，容器被final修饰会怎样

[2分钟记住final的使用](https://www.bilibili.com/video/BV19A4y1o7ET)

## 线程安全的单例模式

java集合

介绍一下jvm的区域

ClassA a=new Class(1) 在jvm中怎么存储

## int和integer有什么区别

https://www.bilibili.com/video/BV1ZP4y187Tk

## java方法是值传递还是对象传递

https://www.bilibili.com/video/BV1xL4y1w7jy


## 说一下你对多线程的理解

threadlocal oom是为什么

java sout（0.1+0.2）输出什么，为什么

## Java面向对象三大特性

https://www.bilibili.com/video/BV1rf4y1E7u9

## 面向对象的【三大特性】

封装、继承、多态

## 说下对象完整创建流程

https://www.bilibili.com/video/BV1J3411G7wA

## 什么时候用多态

https://www.bilibili.com/video/BV18W411C7TC

## Java中的反射（不了解，没问）

https://www.bilibili.com/video/BV1L34y1S7a1

## 隐含的强制类型转换

https://www.bilibili.com/video/BV1Fb4y1Y7wX



Java集合的框架体系图
Hashmap和Treemap的区别

2.
## 4.g1回收器、cms的回收过程，场景

[CMS比较严重的问题并发收集阶段再次触发Full gc怎么处理](https://www.bilibili.com/video/BV1BA4y1R7W7)

[G1垃圾收集器最大停顿时间是如何实现的](https://www.bilibili.com/video/BV1TB4y1s7rh)


5.遇到oom怎么排查（有jvm提供的工具查看堆栈使用情况，具体不太了解只在跑spark遇到过、分享了下spark里的排查经验和参数调整）

12.用过多线程吗（了解，没具体使用过，就不问了？？八股我都准备输出了）

## java中类和对象的关系

https://www.bilibili.com/video/BV1GR4y1p7qw

## 访问修饰符

https://www.bilibili.com/video/BV1bf4y1c7Pu

## 常用哪些数据集合，介绍hashset

[HashSet内部是如何工作的](https://www.bilibili.com/video/BV1sq4y1971k)

## threadlocal原理，应用场景

## 线程池的工作原理

https://www.bilibili.com/video/BV1T34y1p7ih

https://www.bilibili.com/video/BV19L411n7mL

## 垃圾回收

https://www.bilibili.com/video/BV1aW41167rS

https://www.bilibili.com/video/BV1xK4y197SH

[什么样的对象会被老年代回收](https://www.bilibili.com/video/BV13K4y1G7Bs)

## Volatile 关键字原理

https://www.bilibili.com/video/BV1SW411U7QM

https://www.bilibili.com/video/BV1KU4y1q7yF

https://www.bilibili.com/video/BV1Ny4y1u7L6

https://www.bilibili.com/video/BV1x3411B7GE

## java的锁原理

## 手写单例模式

https://www.bilibili.com/video/BV1834y1m7Eq

https://www.bilibili.com/video/BV1Fo4y127zN

https://www.bilibili.com/video/BV1iW411S76k

## 这几个分区在gc里都有什么处理，不同分区的gc策略

[GC是什么时候都能做吗？知道GC安全点与安全区域是怎么回事吗？](https://www.bilibili.com/video/BV1oZ4y1q7tu)

hashmap put过程

浏览器从url输入到返回的流程

1T文件，每行是一个数字，机器128G内存，求top10数字

java基本数据类型




 spark executor内的task是怎么彼此隔离的（从线程池的角度，还有切分stage）

 进程和线程区别，线程和进程切换过程

 线程进程

 线程和进程



进程的地址空间

说一下进程和线程

进程之间通信的方式

进程切换的过程

为什么我们一般都用线程来接收请求（好像是这样问的）

## 如何保证线程安全

[List的线程安全实现有哪些](https://www.bilibili.com/video/BV1ag41177nn)

如何进行etl链路优化

如果链路慢，但是没有sla风险，怎么优化（类似上一道题）

死锁是什么

如何破坏死锁

怎么数据链运行慢定位问题，对任务进行优化

死锁的产生条件还有如何避免

1.1 数据库锁表的相关处理
1.2 索引失效场景

## Mysql锁有哪些，如何理解

https://www.bilibili.com/video/BV1ff4y1Z7VQ

## 高并发下如何做到安全的修改同一行数据，乐观锁和悲观锁是什么，INNODB的行级锁有哪2种，解释其含义

[并发编程没你想得那么难学，并发学习指南|附学习资料](https://www.bilibili.com/video/BV1br4y1C72X)



1.4 数据库会死锁吗，举一个死锁的例子，mysql怎么解决死锁




## 堆和栈的关系

https://www.bilibili.com/video/BV1RW411C7yb



1.1 hash算法的有哪几种，优缺点，使用场景
1.2 什么是一致性hash

Hash表是怎么实现的。

去重如果不用 set 还有多少种方式，有没有更高效的方式？
hashcode()方法原理
怎么解决幻读 具体在sql是怎么实现的

mvcc知道吗

1.1 维度建模有什么好处？如果业务需求增加一个维度，后续需要做哪些工作？


1.2 怎么判断一个需求能不能实现，你们的判断标准是什么？需求变更要做什么？

1.3 增加一个维度后发现查询的速度变得非常慢，是什么原因导致的？

## 大的log文件中，统计异常出现的次数、排序，或者指定输出多少行多少列的内容。

https://www.bilibili.com/video/BV1sr4y127b7

## linux下的调查问题思路：内存、CPU、句柄数、过滤、查找、模拟POST和GET请求等等场景

1.6 分表之后想让一个id多个表是自增的，效率实现


1.7 事物的四个特性，以及各自的特点（原子、隔离）等等，项目怎么解决这些问题

## 一条sql语句运行慢，如何做。（慢查询，看索引，看orderby和groupby）

https://www.bilibili.com/video/BV1N34y17715

## 索引的设计原则

https://www.bilibili.com/video/BV1WB4y1s7kD


innodb和myisam区别。
事务的特性，隔离的级别。

分布式session中用的是什么数据结构

# MySQL

## zk的watch机制实现原理

https://www.bilibili.com/video/BV1D3411w7wK

## 简述zk的命名服务、配置管理、集群管理

https://www.bilibili.com/video/BV1r3411F7id

## explain sql 执行计划的各列参数

https://www.bilibili.com/video/BV1mh411q7u5

[Explain语句结果中各个字段分表表示什么](https://www.bilibili.com/video/BV1gt4y1a7sE)


数据库视图

数据库的三大范式，都有哪些区别，举例说明


数据库引擎mysaim和innoDB的区别

## innodb的索引结构

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

说说 MySQL 的索引？b树与b+树的区别？

6.mysql索引

10.mysql索引

mysql的引擎

mysql的引擎和innodb这类数据库的区别

## mysql索引相关的问题，b+树

[讲讲mysql的索引及B树和B+树](https://www.bilibili.com/video/BV1Dq4y1S7GZ)

https://www.bilibili.com/video/BV18U4y197Dx

## 聚簇索引和非聚簇索引

1.1 你们 ADS 层的数据量每天的数据量有多大？ADS 层再 MySQL 中的表是怎么创建的？有什么注意事项？索引怎么创建的？

1.1 MySql的存储引擎的不同

1.2 Mysql怎么分表，以及分表后如果想按条件分页查询怎么办(如果不是按分表字段来查询的话，几乎效率低下，无解)

1.1 MySql的主从实时备份同步的配置，以及原理(从库读主库的binlog)，读写分离

1.1 MySQL InnoDB存储的文件结构
1.2 索引树是如何维护的？
1.3 数据库自增主键可能的问题
1.4 MySQL的几种优化
1.5 mysql索引为什么使用B+树

1.1 mySQL里有2000w数据，redis中只存20w的数据，如何保证redis中的数据都是热点数据

## mysql事务ACID特性，隔离级别

## oracle和mysql的默认事务隔离级别为什么不同

https://www.bilibili.com/video/BV11U4y1J7v7

项目中MySQL的部署方式是什么，怎么保证数据库数据是高可用的
有考虑过数据库比如MySQL和MongoDB数据库数据丢失问题吗
## mysql隔离机制 默认的是什么
mysql执行引擎

说一下mysql常用的引擎

hash和数组分别作为mysql索引的结果。

B+树是二叉树吗 平衡树吗

B+树的特点。



b+树底层是双向链表还是单向

 红黑树和跳表的区别

