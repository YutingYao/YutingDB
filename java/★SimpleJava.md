参考：[设计源于生活中](https://space.bilibili.com/484405397)

[寒食君](https://space.bilibili.com/1578320)

[楠哥教你学Java](https://space.bilibili.com/434617924)

[Java知识图谱](https://space.bilibili.com/1936685014)

<http://www.altitude.xin>

## 什么是【虚拟内存】和【物理内存】

https://www.bilibili.com/video/BV1KD4y1U7Rr

物理内存：【内存条】存储容量的大小

虚拟内存：是【计算机内存管理技术】，它让程序认为它具有【连续可用】的【4GB 内存】，而实际上，映射到多个【物理内存碎片】

## 直接new一个线程不好吗？

https://www.bilibili.com/video/BV1VB4y1H7Xf


## 线程和进程切换过程

https://www.bilibili.com/video/BV1H541187UH

时间片：CPU为每个【进程】分配一个【时间段】。

上下文切换：如果在【时间片】结束时，进程还在运行，则暂停进程 の 运行，给CPU分配另一个进程

## 进程 の 地址空间

## 进程之间通信 の 方式

## 进程切换 の 过程

## 并发编程三要素？

<https://www.bilibili.com/video/BV1tS4y1e7Yr>



## 为什么我们一般都用线程来接收请求（好像是这样问 の ）

## 面试被问到并发编程中，如何中断一个正在运行中的线程？

https://www.bilibili.com/video/BV1yt4y1h7MW

<https://www.bilibili.com/video/BV1554y1Z7w5>












## Synchronized  の 锁消除

在JIT阶段，如果检测出【不可能有】【资源竞争 の 锁】，会直接消除





## 谈谈你对线程安全的理解？

https://www.bilibili.com/video/BV1x541117iZ

<https://www.bilibili.com/video/BV1ei4y1U7HW>




## 如何保证线程安全

https://www.bilibili.com/video/BV1dY411P7hp

https://www.bilibili.com/video/BV1NF41157t1

[List の 线程安全实现有哪些](https://www.bilibili.com/video/BV1ag41177nn)

## SimpleDateFormat 是线程安全的吗

<https://www.bilibili.com/video/BV1zS4y1x7qD>



## 常用哪些数据集合，介绍hashset

[HashSet内部是如何工作 の](https://www.bilibili.com/video/BV1sq4y1971k)



## 其中hashCode方法 の `返回值`是什么？

<https://www.bilibili.com/video/BV1WR4y1J7T4>


## HashMap的Put方法

<https://www.bilibili.com/video/BV1MR4y1F7Jf>

## hashmap 是如何解决hash冲突的？

<https://www.bilibili.com/video/BV1Y341137uj>



## Hashmap和Treemap の 区别

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

## arraylist 和linkedlist 区别，为什么arrylist查询快

https://www.bilibili.com/video/BV1UY4y177XM

https://www.bilibili.com/video/BV1gZ4y1R7B8

<https://www.bilibili.com/video/BV1xe4y1973f>

## ArrayList 和 LinkedList 的区别是什么？

<https://www.bilibili.com/video/BV1Vu411z7ws>

数据结构实现：ArrayList 是动态数组的数据结构实现，而 LinkedList 是双向链表的数据结构实现。
随机访问效率：ArrayList 比 LinkedList 在随机访问的时候效率要高，因为 LinkedList 是线性的数据存储方式，所以需要移动指针从前往后依次查找。
增加和删除效率：在非首尾的增加和删除操作，LinkedList 要比 ArrayList 效率要高，因为 ArrayList 增删操作要影响数组内的其他数据的下标。
综合来说，在需要频繁读取集合中的元素时，更推荐使用 ArrayList，而在插入和删除操作较多时，更推荐使用 LinkedList。

<https://www.bilibili.com/video/BV1uA411J7gK>

|  ArrayList | LinkedList   |
|---|---|
| 底层是【数组】  | 底层是【链表】  |
| 查询，速度更快  | 增删，速度更快  |
|   | 更占内存  |

<https://www.bilibili.com/video/BV1QW41167Gn>

## 追问：HashMap在哪个jdk版本使用红黑树，之前 の 实现方法是什么？

## hashmap put过程

## 集合

Collection:

- List (ArrayList)
- Queue (LinkedList)(ArrayDeque)
- Set (HashSet)

Map:

- HashMap (LinkedHashMap)

[java中LinkedHashMap和TreeMap是如何保证顺序的？](https://www.bilibili.com/video/BV1e44y1x7GS)

## hash算法 の 有哪几种，优缺点，使用场景

## 什么是一致性hash

<https://www.bilibili.com/video/BV193411u7Lq>

## Hash表是怎么实现 の




## Java中 の 反射（不了解，没问）

<https://www.bilibili.com/video/BV1L34y1S7a1>

## 什么是反射？

就是根据【对象】找到【对象所属的Class】

也就是，通过getClass()方法，我们就获得了这个Class对应的`Class对象`。

这个`Class对象`就可以认为是这个class的【字节码对象】

反射机制就是通过这个【字节码对象】

看到了这个Class的结构。

## Object的常用方法有哪些？

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

## 反射创建【class实例】的三种方式

```java
[1] 类名.class
[2] 对象名.getClass()
[3] Class.forName("类的全限定类名")
```

## 反射具有以下4大功能：

1. 在【运行时】，**获知**，【any Object】所属的【Class】
2. 在【运行时】，**构造**，【any Class】的【Object】
3. 在【运行时】，**获知**，【any Class】所具有的【成员变量、method】
4. 在【运行时】，**调用**，【any Object】的【属性、method】

这种**动态获取信息**、**动态调用对象**的方法的功能，就称为**Java语言的反射机制**

## 反射的优缺点

优点：

- 比较灵活
- 能够在【运行时】，动态获取Class的Instance

缺点：

- 安全问题：反射机制【破坏】了【封装性】，因为通过【反射】可以获取并【调用】类的【private method】
- 性能：比直接运行java代码要慢得多

## 反射的原理

1. 首先，JVM会将代码编译成一个`.class字节码`文件，
2. 然后被`类加载器ClassLoader`加载进`JVM内存`中，
3. 同时，创建`Class对象`到`heap`中
4. 这样，`heap的方法区`就产生了一个`Class对象`
5. `Class对象`包含了这个class的完整结构信息，我们可以通过这个`Class对象`看到【类的结构】

## 反射的实际应用

- 动态代理
- JDBC链接数据库

## 为什么【进程切换】比【线程切换】慢？

为什么要用【虚拟地址】去取代【物理地址】？

- 物理地址：就是真实的地址，这种【寻址方式】使得【操作系统】中同时运行【>=2个】的【进程】几乎是不可能的，容易崩溃
- 所以，需要有个机制，对【进程】使用的【地址】进行保护，因此，引入了一个新的【内存模型】，就是【虚拟地址】
- 有了【虚拟地址】，CPU就能通过【虚拟地址】转换为【物理地址】，来间接访问【物理内存】

【虚拟地址】的原理？

1. 地址转化需要2个东西：
   - CPU上的【内存管理单元MMU】
   - 内存中的【页表】，【页表】中存的是【虚拟地址】到【物理地址】的映射
   - 所以，每次进行【虚实转换】都需要访问【页表】，页表访问次数太多，就会成为【性能瓶颈】
2. 引入【转换检测缓冲区TLB】，也就是【快表】，将【经常访问的内存地址映射】存在【TLB】中
   - TLB位于【CPU的MMU】中，所以访问非常快

因为TLB这个东西，导致【进程切换】比【线程切换】慢

1. 一个【进程】对应一个【页表】，【进程切换】导致【页表切换】，导致【TLB失效】，这样【虚拟地址】转换为【物理地址】就会变慢。表现为，程序运行变慢。
2. 【线程】切换，不涉及【虚拟地址映射】的切换，所以，不存在这个问题



## 线程 の 创建方式？

## 多线程了解吗？（当时脑子一抽，我以为要写多线程代码，回答了不熟悉，他就没多问了）

## 并发场景中，ThreadLocal会造成内存泄漏吗？

https://www.bilibili.com/video/BV1q94y1d7gL

https://space.bilibili.com/22492579

https://www.bilibili.com/video/BV1q94y1d7gL

https://www.bilibili.com/video/BV1FU4y1S7bh

```java
public class ThreadLocal<T> {
      数据结构采用数组
      static class ThreadLocalMap {
            private Entry[] table;
            Entry继承了弱引用WeakReference.
            可能导致OOM
            static class Entry extends WeakReference<ThreadLocal<?>> {
                  Object value;
                  key就是ThreadLocal，value就是【变量副本值】
                  Entry(ThreadLocal<?> k, Object v) {
                        super(k);
                        value = v;
                  }
            }
      }

      通过 key 拿到 value值
      public T get() {
            获取当前线程
            Thread t = Thread.currentThread();

            获取当前线程的ThreadLocalMap
            ThreadLocalMap map = getMap(t);

            if (map != null) {
                  this是当前的 ThreadLocalMap(key), getEntry是通过key拿到value
                  ThreadLocalMap.Entry e = map.getEntry(this);

                  if (e != null) {
                        @SuppressWarnings("unchecked")
                        T result = (T) e.value;
                        返回获取到的value
                        return result;
                  }
            }
            return setInitialValue();
      }

      public void set (T value) {
            获取当前线程
            Thread t = Thread.currentThread();

            获取当前线程的ThreadLocalMap
            ThreadLocalMap map = getMap(t);

            if (map != null) {
                  重新将ThreadLocal和新的value副本放入到map中
                  map.set(this, value);
            } else {
                  创建
                  createMap(t, value);
            }

      }

      void createMap (Thread t, T firstValue) {
            t.threadLocals = new ThreadLocalMap(this, firstValue);
      }
}
```

## ThreadLocal 是什么？

ThreadLocal 不同于 synchronized锁机制，不属于【多线程同步机制】

它为【多线程环境】下，【变量安全】提供了一种new思路

ThreadLocal 为每个`线程`提供`独立的变量副本`，

所以`不同线程`都可以独立地`改变`自己的`副本`，

而不会影响`其它线程`所对应的`副本`。

## ThreadLocal应用场景

https://www.bilibili.com/video/BV1QS4y1s7hi

连接管理：【一个线程】持有【一个连接】，【线程之间】不共享【同一个连接】

ThreadLocal 的经典使用场景是**数据库连接**和 **session 管理**等。

## threadlocal原理

内部类是：`TreadLocalMap`，是一个`Entry数组`

每个`Thread对象内部`都存储了一个`ThreadLocalMap`

- `Entry`的key为`ThreadLocal对象ref`，指向这个`ThreadLocal对象`
- `Entry`的value为`需要缓存的值`

<https://www.bilibili.com/video/BV1iZ4y127wm>

他的引用链时这样的：

| stack  | Heap  |
|---|---|
| ThreadLocalRef  | TreadLocal  |


 → 引用 → Thread → 引用 →  → 引用 → Entry → 引用 → value

## 什么是内存溢出，什么是内存泄露

https://www.bilibili.com/video/BV1BT4y1Y7YS

## threadlocal oom是为什么

key是一个【弱引用】，在系统GC的时候，这个`ThreadLocal弱引用`是可以被回收的

这样，就出现了key 为 null的Entry，就没有办法通过key找到value。

value是一个【强引用】，如果【线程】一直存在的话，他就会一直存在这个value，不会被GC掉，会导致OOM

## threadlocal oom如何解决？

threadlocal内置了：

- get方法：用来获取【ThreadLocal】在【当前线程】中保存的【变量副本】
- set方法：用来设置【当前线程】中的【变量副本】
- remove方法：用来移除【当前线程】中的【变量副本】

能够将key为null的Entry的value置为null，这样就能在下一次GC时，把这个Entry彻底回收掉

我们可以在使用完 ThreadLocal以后，手动调用一下它的`remove方法`

## 线程的创建方式

## 请说一下对象的创建过程？

<https://www.bilibili.com/video/BV1c44y1P7Xx>

## java方法是值传递还是对象传递

<https://www.bilibili.com/video/BV1xL4y1w7jy>


## Java面向对象三大特性

<https://www.bilibili.com/video/BV1rf4y1E7u9>

## 面向对象 の 【三大特性】

封装、继承、多态

## 说下对象完整创建流程

<https://www.bilibili.com/video/BV1J3411G7wA>

## new String("abc")到底创建了几个对象？

<https://www.bilibili.com/video/BV1MS4y1b75Q>

## 连接池

<https://www.bilibili.com/video/BV1yR4y1K7Z7>


## 什么是Java虚拟机为什么要使用？

<https://www.bilibili.com/video/BV1Fg411278C>

## Jvm垃圾回收器:serial

## 垃圾回收

<https://www.bilibili.com/video/BV1aW41167rS>

<https://www.bilibili.com/video/BV1xK4y197SH>

[什么样 の 对象会被老年代回收](https://www.bilibili.com/video/BV13K4y1G7Bs)











## 我们自己定义 の java.lang.String类是否可以被类加载器加载

<https://www.bilibili.com/video/BV1MF411e7zY>

## String类是否可以被继承

<https://www.bilibili.com/video/BV1ev411g7Xk>






## JVM中，哪些是共享区，哪些可以作为gc root

<https://www.bilibili.com/video/BV1Uq4y1e7Kq>




## 【pass💦】Java 中有多少种引用类型？它们 の 都有什么使用场景？

<https://www.bilibili.com/video/BV1e94y1D7ug>



## Java 内存模型定义了八种操作来实现

Java 内存模型定义了八种操作来实现

## CompletableFuture

<https://www.bilibili.com/video/BV1hA4y1d7gU>









## 内存溢出

<https://www.bilibili.com/video/BV1gW411r7By>

## Objects类中有哪些方法？

## 一个空Object对象的占多大空间

<https://www.bilibili.com/video/BV1SG411h7ju>

## static

<https://www.bilibili.com/video/BV1nW41167o1>

<https://www.bilibili.com/video/BV1zL411s7h9>

静态修饰符，代表这个类`固有的`，在这个类里面共享，不需要`new一个实例`

`non-static method 非静态方法` = `instance method 实例方法` = new一个实例

## public

如果a包下的`A类`是`public`的，它的字段和方法都是private的。

→ 在`b包`下的`B类`可以创建`A类`的对象，但是无法访问`A类对象的字段和方法`。

如果a包下的`A类``没有修饰符`，它的字段和方法都是private的。

→ 在`a包`下的B类可以创建`A类`的对象，但无法访问A类对象的字段和方法。

→ 在`b包`下的B类无法创建A类的对象。

## 访问修饰符

<https://www.bilibili.com/video/BV1bf4y1c7Pu>

## final 值 方法 类有什么区别，容器被final修饰会怎样

[2分钟记住final の 使用](https://www.bilibili.com/video/BV19A4y1o7ET)

# java

[经典鸡翅](https://space.bilibili.com/386498238) 这些视频比较高级，没有工作经验，看不太懂

[设计源于生活中](https://space.bilibili.com/484405397) 代码题

## 强平衡⼆叉树和弱平衡⼆叉树有什么区别

<https://www.bilibili.com/video/BV1oF411M78u>

## ArrayBlockQueue

<https://www.bilibili.com/video/BV17A4y197em>

## 说一下你熟悉的设计模式？

创、结、行

按照模式的【应用目标】分类：

1. 创建型：对【对象创建过程】的各种问题 and 解决方案 の 一个总结。
2. 结构型：对【软件设计结构】的总结。重点关注【类、对象继承、组合方式】的实践经验的总结
3. 行为型：【从类】or【对象】之间的【交互】，【职责划分】等角度，总结的模式

## 在学习【框架】或【中间件】底层源码遇到的设计模式？

[【23种设计模式全解析】终于有人用一个项目将23中设计模式全部讲清楚了](https://www.bilibili.com/video/BV19g411N7yx)

1. 创建型：
      1. 工厂模式
      2. 单例模式
      3. 建造者模式
      4. 原型模式
2. 结构型：
      1. 适配器模式
      2. 桥接模式
      3. 过滤器模式
      4. 组合模式
      5. 装饰器模式
      6. 外观模式
      7. 享元模式
      8. [代理模式](https://www.bilibili.com/video/BV1cz41187Dk)
3. 行为型：
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
3. `观察者模式`：定义了对象之间的一对多的依赖，这样一来，当一个对象改变时，它的所有的依赖者都会收到通知并自动更新。
4. `外观模式`：提供一个统一的接口，用来访问子系统中的一群接口，外观定义了一个高层的接口，让子系统更容易使用。
5. `模版方法模式`：定义了一个算法的骨架，而将一些步骤延迟到`子类`中，模版方法使得`子类`可以在不改变算法结构的情况下，重新定义算法的步骤。
6. `状态模式`：允许对象在内部状态改变时改变它的行为，对象看起来好像修改了它的类。

## 责任链模式

https://www.bilibili.com/video/BV1UL4y157eH


## 排序算法和时间复杂度

<https://www.bilibili.com/video/BV1Sg411M7Cr>

## java执行 の 过程

## java集合

## 介绍一下jvm の 区域

## java sout（0.1+0.2）输出什么，为什么

## 隐含 の 强制类型转换

<https://www.bilibili.com/video/BV1Fb4y1Y7wX>

## Java集合 の 框架体系图

## spark executor内 の task是怎么彼此隔离 の （从线程池 の 角度，还有切分stage）

## spark那些外部资源 还有第三方jar包之类 の 都放在哪（应该是这么问 の ，不太会，说了下内存结构，告诉我是java classloader相关 の 机制）

## 遇到oom怎么排查（有jvm提供 の 工具查看堆栈使用情况，具体不太了解只在跑spark遇到过、分享了下spark里 の 排查经验和参数调整）

## linux下 の 调查问题思路：内存、CPU、句柄数、过滤、查找、模拟POST和GET请求等等场景

## 简述 tcp 和 udp的区别？

tcp 和 udp 是 OSI 模型中的运输层中的协议。tcp 提供可靠的通信传输，而 udp 则常被用于让广播和细节控制交给应用的通信传输。
两者的区别大致如下：

tcp 面向连接，udp 面向非连接即发送数据前不需要建立链接；
tcp 提供可靠的服务（数据传输），udp 无法保证；
tcp 面向字节流，udp 面向报文；
tcp 数据传输慢，udp 数据传输快；

## TCP的三次握手和四次挥手

<https://www.bilibili.com/video/BV1GT4y1r74W>

## tcp 为什么要三次握手，两次不行吗？为什么？

<https://www.bilibili.com/video/BV1xL4y1F7wL>

　我们假设A和B是通信的双方。我理解的握手实际上就是通信，发一次信息就是进行一次握手。

第一次握手：A给B打电话说，你可以听到我说话吗？
第二次握手：B收到了A的信息，然后对A说：我可以听得到你说话啊，你能听得到我说话吗？
第三次握手：A收到了B的信息，然后说可以的，我要给你发信息啦！
在三次握手之后，A和B都能确定这么一件事：我说的话，你能听到；你说的话，我也能听到。这样，就可以开始正常通信了。
注意：HTTP是基于TCP协议的，所以每次都是客户端发送请求，服务器应答，但是TCP还可以给其他应用层提供服务，即可能A、B在建立链接之后，谁都可能先开始通信。

如果采用两次握手，那么只要服务器发出确认数据包就会建立连接，但由于客户端此时并未响应服务器端的请求，那此时服务器端就会一直在等待客户端，这样服务器端就白白浪费了一定的资源。若采用三次握手，服务器端没有收到来自客户端的再此确认，则就会知道客户端并没有要求建立请求，就不会浪费服务器的资源。
