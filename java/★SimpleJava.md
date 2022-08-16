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
   - 没有创建线程的【同一标准】，比如，线程名字。
   - 每个人都可以创建线程，导致当系统运行起来，不好管控。
2. 频繁创建【开销大】

## 说下对象完整创建流程

1. 类加载检查
   - 在实例化一个对象的时候，JVM首先先检查【目标对象】是否已经【被加载】并【初始化】。如果没有，JVM需要立即去加载【目标类】，然后，调用【目标类】的【构造器】完成初始化。【目标类】是通过【类加载器】来实现的，主要就是把一个【类】加载到【内存】里面，然后是【初始化】，这个步骤主要是对【目标类】的【静态变量、成员变量、静态代码块】进行初始化。当【目标类】被【初始化】以后，就可以从【常量池】里面找到对应的【类元信息】。并且，【目标对象的大小】在【类加载完成】之后，就已经确定了。
2. 分配内存
   - 这个时候，就需要为【new创建的对象】根据【目标对象的大小】在【堆内存】中分配内存空间，内存分配的方式有 2 种：指针碰撞 + 空闲列表。JVM会根据【JAVA堆内存】是否【规整】来决定【内存分配方法】。
3. 初始化0值
   - 对除了【对象头】以外的内存区域，JVM会将【目标对象】里面的【普通成员变量】初始化为0值，比如说，int类型初始化为【0值】，string类型初始化为null。这步操作主要是保证【对象】里面的【实例字段】，不用【初始化】就可以【直接使用】。
4. 设置对象头
   - 包括hashCode、GC分代年龄、锁标记
   - 指针，用于确定该object是哪个class的实例
5. 执行init方法
   - 属性赋值、执行构造方法，完成对象的创建。其中，init方法是【java文件】编译之后，在【字节码文件】里面生成的。它是一个【实例构造器】，完成一系列【初始化动作】
   - 
<https://www.bilibili.com/video/BV1J3411G7wA>

## new Thread() 和 new Object() 的区别？

new Object() 的过程：

1. 在JVM上分配一块【内存M】heap
2. 在【内存M】上【初始化该对象】
3. 将【内存M】的【地址】【赋值】给【引用变量obj】

new Thread() 的过程：

1. JVM为【线程栈】分配【内存】，该【线程栈】为每个【线程方法】保存一个【栈桢】
2. 每个【线程】获得一个【程序计数器】，用于记录【JVM】正在执行的【线程指令地址】
3. 【操作系统】创建一个与【java线程】对应的【本机线程】
4. 线程会共享【heap】和【方法区】
5. 创建一个线程大概需要【1M k空间】

## 线程和进程切换过程

[幼麟实验室 - 话说进程和线程~](https://www.bilibili.com/video/BV1H541187UH)

时间片：CPU为每个【进程】分配一个【时间段】。

上下文切换：如果在【时间片】结束时，进程还在运行，则暂停进程 の 运行，给CPU分配另一个进程

## 进程 の 地址空间

进程有自己的【虚拟地址空间】。

目的：保证【系统运行安全】。

1. 【内核空间】（操作系统）：内核空间】由所有【进程】的【地址空间共享】。【操作系统】保存的【进程控制信息】在【内核空间】
2. 【用户空间】（用户程序）：用户程序】不能直接访问【内核空间】

## 进程之间通信 の 方式

| 进程  | 线程  |
|---|---|
| 通信较【复杂】  | 通信较【简单】，通过【共享资源】   |

## 什么是【上下文切换】？

【CPU时间片】 の 切换

## 进程切换 の 过程

当我们进行【系统调用的时候】：

- 需要进行`用户态`到`内核态` の 切换。
- CPU会将【用户态】下的【程序计数器】【寄存器】保存起来，然后转为【内核态】下的【程序计数器】【寄存器】。
- 这叫做CPU的【上下文切换】。
- 一次【系统调用】就需要从`用户态`到`内核态` ，再到`用户态`，即，2次的【上下文切换】


【进程的上下文切换】：

- CPU 通过【时间片切换】轮流执行不同的【进程】。
- 【进程】需要轮流获得【CPU使用权】，所以在【进程】交换的时候，就产生了【进程的上下文切换】。
- 由于【操作系统】是在【内核态】下进行【进程调度】的。
- 所以【进程的切换】必然先要从【用户态】到【内核态】

不同的【进程】之间还有不同的【虚拟内存】：

- 对应到【实际】就是，不同的【进程】之间还有不同的【页表】。

所以【进程的上下文切换】包括：

1. `用户态`到`内核态` の 切换
2. 【页表】的切换

## 进程间通信方式

https://www.bilibili.com/video/BV1tv411p7WX

进程之间的通信方式：

1. 管道模型：（常用）
分为2类：
   - 匿名管道：将前一个【命令的输出】作为后一个【命令的输入】，通常【没有名字】，用完就销毁了
   - 命名管道：mkffo，【显式】的【创建管道】，【管道】以【文件】的形式存在
2. 消息队列模式：生产者-消费者模型
   - 【系统级别】的消息队列 → 不太用、
   - 【用户级别】的消息队列 → 常用
3. 共享内存 + 信号模型（组合使用）：
   - 【线程】是可以访问同一块内存空间的，但是【进程】不可以
   - 共享内存： 开辟一片连续的存储空间，性能会更好，速度快，copy少
   - 共享内存： 通常和【信号量】一起使用
   - 信号量：解决【互斥访问共享区】的问题，使得同一个【共享内存】在同一时间，只能被【同一进程】访问。就是【串行】的
4. 信号，不是信号量
   - 上面将的都是【常规状态】下的【工作模式】，对于一些比较异常的状态。
   - 信号：就是【系统】【24小时不间断】地进行【告警】，一旦出现问题，就立即【通知】到【特定系统】
5. socket
   - 用于【网络之间】的【消息传输】

## 为什么我们一般都用线程来接收请求（好像是这样问 の ）

## 管道是如何进行进程间通信的

https://www.bilibili.com/video/BV1Eg411M7zi

匿名管道、命名管道，都是【内核文件】

1. 【匿名管道】是linux提供的【系统函数】

一个【管道】相当于【队列】，从【队列尾】写东西，从【队列头】读东西。本质上就是，2个【文件描述符】来表示【管道】的两端，一个是【管道】读取【描述符】，一个是【管道】写入【描述符】，2个【文件描述符】都在【同一个进程里面】

当fork（）出【子进程】时，【子进程】会复制【父进程】【文件描述】结构，【文件描述符】的数组也会复制一份(浅拷贝,也就是还是指向同一个地址【管道】，这样就能【两个进程通信】)。

由于此时【父进程】【子进程】都能向【管道】【读、写】，就容易错乱。所以通常，【父进程】只保留【读取的fd】，【子进程】只保留【写入的fd】。如果要【双向通信】，则需要2个管道

在一个进程里用pipe函数创建了一个管道，相当于创建了一份缓存。由于管道只能一端写入另一端读出。所以通常父进程只保留而子进程只保留写入的fd。

2. 命名管道

实现原理和【匿名管道】差不多，本质上都是【内核的文件】。命名管道会写到【磁盘文件】里。

## Synchronized  の 锁消除

[Synchronized锁竟然能降级，以前八股文全背错了？](https://www.bilibili.com/video/BV1jG4y1i7jV)

在JIT阶段，如果检测出【不可能有】【资源竞争 の 锁】，会直接消除


## 常用哪些数据集合，介绍hashset

## 其中hashCode方法 の `返回值`是什么？

<https://www.bilibili.com/video/BV1WR4y1J7T4>


## hashmap 是如何解决hash冲突的？

链式寻址法：尾插法

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

## 为什么arrylist查询快

因为：

- ArrayList 可以直接通过【数组下标】找到元素
- LinkedList 需要【移动指针】从前往后依次查找。

## 为什么 linkedlist 增删快

因为：

- ArrayList 在【增删元素】时，可能要【扩容、复制】数组
- LinkedList 只需要【修改指针】即可

## ArrayList 与 Vector 的区别？

ArrayList 线程不安全

Vector 线程安全

## arraylist 和 linkedlist 区别，

|  ArrayList | LinkedList   |
|---|---|
| 底层是【动态数组】  | 底层是【双向链表】  |
| 查询 fast  | 增删 fast  |
| 用作【列表】  | 用作【列表 + 队列】  |
| 初始大小是10，容量不足会扩容  | 将元素添加到末位，无需扩容  |



## 追问：HashMap在哪个jdk版本使用红黑树，之前 の 实现方法是什么？

## 集合

Collection:

- List (ArrayList)
- Queue (LinkedList)(ArrayDeque)
- Set (HashSet)

Map:

- HashMap (LinkedHashMap)


## hash算法 の 有哪几种，优缺点，使用场景

## 什么是一致性hash

<https://www.bilibili.com/video/BV193411u7Lq>

核心思想：解决【分布式环境下】，【hash表】可能存在的【动态扩容、缩容】。

## 为什么出现【一致性hash】？

一般情况下，我们使用hash表，以key-value的方式，做数据存储。但是在【数据量】比较大的情况下，我们会把数据存储到【多个node】，通过hash取模的方式，来决定把当前的key存储到哪个节点？

当存储节点【增加 or 减少】时，原本的【映射关系】就会发生变化，也就是对所有数据，按照【new节点数量】重新映射一遍。

这就涉及到【大量数据迁移】和【重新映射】的问题，它的代价很大。

## 一致性Hash的工作原理

用过一个【hash环】的数据结构，【环的起点】是0，【环的终点】是 2^32 - 1。

也就是，这个环的【数据分布范围】是 0 ~ 2^32 - 1。

然后，我们把【数据存储node的IP地址】作为 key 进行 hash 之后，会落到【hash环】上的【某一个位置】。数据的key也会进行hash，然后按照【顺时针】方向，找到距离自己最近的一个node，来进行数据的存储

## 为什么【一致性哈希】比【普通hash】好？

假设我们需要新增一个节点node4。那么数据映射关系的影响范围只会影响到node3和node1这两个节点。也就是说，只有少部分数据需要进行映射和迁移。

假设node1因为【故障下线】，那么这个时候，我们只需要分配到node1上的数据，重新分配到node2上面就好了。同样对于数据的影响范围是很小的。

扩展性强，在【增加 or 减少】服务器的时候，【数据迁移】的范围比较小，也就是说，影响范围有限，

另外，在【一致性哈希算法】里面，为了避免hash倾斜，我们可以使用【虚拟节点】来解决这个问题


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
- JDBC链接数据库，使用Class.forName()通过【反射】加载【数据库】的驱动程序
- Spring AOP


## 谈谈你对spring aop的理解？

https://www.bilibili.com/video/BV1LB4y1t7jr

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

## 什么是虚拟内存

https://www.bilibili.com/video/BV1xb4y1Z761

## 为什么要有【虚拟内存】

为了实现【进程】隔离

## 什么是【虚拟内存】和【物理内存】

[话说虚拟内存](https://www.bilibili.com/video/BV1KD4y1U7Rr)

物理内存：【内存条】存储容量的大小

虚拟内存：是【计算机内存管理技术】，它让程序认为它具有【连续可用】的【4GB 内存】，而实际上，映射到多个【物理内存碎片】

## 线程 の 创建方式？

## 多线程了解吗？（当时脑子一抽，我以为要写多线程代码，回答了不熟悉，他就没多问了）

## 阿里ThreadLocal-如何在父子线程传递数据？

https://www.bilibili.com/video/BV1MY4y1s7ij

## 并发场景中，ThreadLocal会造成内存泄漏吗？

https://www.bilibili.com/video/BV1q94y1d7gL

https://space.bilibili.com/22492579

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

## 【大厂面试题】ThreadLocal的常用场景？

https://www.bilibili.com/video/BV1KG411x7F2

## ThreadLocal应用场景

https://www.bilibili.com/video/BV1QS4y1s7hi

连接管理：【一个线程】持有【一个连接】，【线程之间】不共享【同一个连接】

ThreadLocal 的经典使用场景是**数据库连接**和 **session 管理**等。

## threadlocal原理

每个线程的【读写操作】都是基于线程本身的一个【私有副本】，线程之前的数据是【相互隔离的】，互相不影响。这样一来，基于ThreadLocal  就不存【线程安全问题】了。相当于采用了【空间换时间】的思路

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


## threadlocal oom是为什么

key是一个【弱引用】，在系统GC的时候，这个`ThreadLocal弱引用`是可以被回收的

这样，就出现了key 为 null的Entry，就没有办法通过key找到value。

value是一个【强引用】，如果【线程】一直存在的话，他就会一直存在这个value，

即不能【被访问】，也不会【被GC掉】，会导致OOM

-------------------------------------

如果【线程本身】的【生命周期】很短，那么【内存泄漏】能很快得到解决。

只要【线程】被销毁，value也就随之被回收，

但【线程】一般通过【线程池】来使用，很少去频繁的创建和销毁

这就将【线程】的【生命周期大大拉长】，而【内存泄漏】带来的影响也就越来越大

## threadlocal oom 内部如何优化？

threadlocal内置了：

- get方法：用来获取【ThreadLocal】在【当前线程】中保存的【变量副本】
  - **没有【直接命中】，【向后环形查找】时，会进行清理**
- set方法：用来设置【当前线程】中的【变量副本】
  - **会【采样清理、全量清理】，扩容时，还会继续检查**
- remove方法：用来移除【当前线程】中的【变量副本】
  - **除了清理当前Entry，还会向后继续清理**

## threadlocal oom 如何解决？

1. 我们可以在使用完 ThreadLocal 以后，手动调用一下它的`remove方法`
   - 能够将key为null的Entry的value置为null，这样就能在下一次GC时，把这个Entry彻底回收掉
2. 把【ThreadLocal变量】尽可能地定义为static final
   - 这样，可以避免【频繁创建】【ThreadLocal实例】，这样能够保证程序一定存在【ThreadLocal强引用】
   

## 线程的创建方式

## 形参 & 实参

实参：用于传递给 method 的参数，必须有确定的值

形参：定义method，接收【实参】，不必有确定的值


## java方法是值传递还是对象传递

**在java中只有值传递**

如果参数是：

- 【基本类型】，传递的就是，【`字面量值`的copy】，会创建副本。
- 【引用类型】，传递的就是，【`实参`所`引用的对象`在`heap中地址值`的copy】，会创建副本。

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
【引用类型】赋值，对方法内部【形参】的修改会影响【实参】

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

【面向对象】是相对于【面向过程】来讲的，把相关的【数据、方法】组织成一个整体看待

面向对象 の 【三大特性】：封装、继承、多态

<https://www.bilibili.com/video/BV1rf4y1E7u9>

- 封装：就是，隐藏一切可以隐藏的东西，对外只提供【最简单】的编程接口
- 继承：就是，从【已有类】创建【新类】。从而提高【代码复用性】
- 多态：就是，【不同子类型】的对象，调用【相同的方法】，但是做了【不同的事情】

## 面向对象 の 【三大特性】

封装、继承、多态

## 【大厂面试题】数据库连接池泄漏如何排查？

https://www.bilibili.com/video/BV1zY4y1k7Jp

## 数据库连接池有什么用？

数据库的连接池是一种【池化技术】。【池化技术】的核心思想是实现【资源的复用】。避免资源的【重复创建、销毁】，带来的开销

而在【数据库】的应用场景里面：【应用程序】每一次向【数据库】发送【CRUD操作】的时候，都需要去【创建连接】。而在【数据库】访问量比较大的情况下，【频繁创建链接】会带来大的【性能开销】。

而【连接池】的核心思想，就是【应用程序】在【启动】的时候，提前【初始化】一部分【连接】，保存在【连接池】里面。

当【应用程序】需要用【链接】进行【数据操作】的时候，直接去【连接池】里面取出一个已经建立好的【连接】。

连接池的关键参数：

1. 初始化时：
   - `初始化连接数`：初始化时，创建多少个【连接】
   - `最大连接数`：同时最多能支持多少连接
   - `最大空闲连接数`：当【没有请求】的时候，【连接池】中要保留的【空闲连接】
   - `最小空闲链接数`：当【空闲连接数】小于这个值，就需要【创建new链接】

2. 使用连接：
   - `最大等待时间`：当【连接】使用完以后，新的请求要等待的时间
   - `无效连接清除`：清理无效的链接

不同的【连接池框架】除了【核心参数】以外，还有很多【业务型参数】，会不同

<https://www.bilibili.com/video/BV1yR4y1K7Z7>

## 提问：最大连接数如何设置

## 提问：连接池的实现原理


## Jvm垃圾回收器:serial

## 什么样 の 对象会被老年代回收

https://www.bilibili.com/video/BV13K4y1G7Bs

1. 【object】的【分代年龄】达到【15】
2. 【对象超级大】，所占空间，大于Eden区的一半
3. 【对象大】，所占空间，超过S0 或者 S1的【剩余空间】
4. 当【年龄为xx，比如5】的对象，所占空间，大于S0的一半，那么【大于5，比如5.6.7】都会移动到【老年代】

老年代中的对象，如果不能被GC root链接到，就会被回收


## CompletableFuture的理解

弥补了原本Future的不足，使得程序可以在【非阻塞状态】下，完成【异步】【回调机制】

<https://www.bilibili.com/video/BV1hA4y1d7gU>

是基于【事件驱动】的【异步回调类】。

当前使用【异步线程】去执行任务时，我们希望在这个【任务结束】以后，还能触发一个【后续操作】

它提供了5种方式，把多个【异步任务】组成一个具有【先后关系】的【处理链路】

1. 第一种是：thenCombine
   - task1 和 task2 并行执行，两个任务执行结束后，触发回调
2. 第二种是：thenCompose
   - task1 结果作为 task2 参数
   - 串行执行
3. 第三种是：thenAccept
   - task1 结果作为 task2 参数
   - 无返回值
4. 第四种是：thenApply
   - task1 结果作为 task2 参数
   - 有返回值
5. 第五种是：thenRun
   - task1 完成后，触发【实现 Runnable 接口】的 task
   - 无返回值




## 【大厂面试题】线上服务内存溢出如何排查定位？

https://www.bilibili.com/video/BV1y34y1j7QR


## 内存溢出

本质上是，我们【申请的内存】，超出了java的可用内存，造成了【内存溢出】的问题。内存溢出常见的场景有：

1. 【JVM栈】溢出：stack内存不够用，栈里面存的是【基本数据类型】【方法的引用】，
2. 【本地方法栈】溢出
3. 【方法区】溢出：静态、类信息
4. 【堆】溢出：放的是【对象】
5. 【运行时常量池】溢出
6. 【直接内存】溢出

```java
【JVM栈】溢出

public class JavaStackOOM {
      private int stackLength = 1;
      public void stackLeak() {
            stackLength ++;
            stackLeak(); // 不停地创建新的方法栈
      }
      public static void main(String[] args) throws Throwable {
            JavaStackOOM oom = new JavaStackOOM();
            try {
                  oom.stackLeak(); 
            } catch (Throwable e) {
                  System.out.println("stack length:" + oom.stackLength);
            }
      }

}
```

```java
【堆】溢出
public class HeapOOM {
      static class OOMObject{}
      public static void main(String[] args) {
            List<OOMObject> list = new ArrayList<HeapOOM.OOMObject>();
            while (true) {
                  list.add(new OOMObject()); // 不断添加【实例对象】
            }
      }
}
```

```java
【运行时常量池】溢出

public class RuntimeConstantPoolOOM {
      public static void main(String[] args) {
            List<String> list = new ArrayList<String>();
            int i = 0;
            while (true) {
                  list.add(String.valueOf(i++).intern()); //使用`intern()方法`就能放入`常量池`
            }
      }

```

<https://www.bilibili.com/video/BV1gW411r7By>

## Objects类中有哪些方法？

toString方法：默认输出【对象地址】。

equals方法：引用数据类型：比较【引用对象的内容】

hashCode方法：将与对象相关的信息映射成一个hash值

getClass方法：返回【类对象】，用于【反射机制】

clone方法：克隆对象的各个属性



## static

静态修饰符，特征：

1. 代表这个类`固有的`，在这个类里面共享
2. 不需要`new一个实例`，可以直接调用
3. 可以被继承，但不能被重写
4. 【static 方法、static 代码块】中不能引用【其他 非static 方法、代码块】

`non-static method 非静态方法` = `instance method 实例方法` = new一个实例

可以修饰：

- static 变量
- static 常量
- static 方法
- static 代码块
- static 成员内部类

## 为什么【static 方法、static 代码块】中不能引用【其他 非static 方法、代码块】？

因为当执行【static 方法、static 代码块】时，【实例对象】尚未创建

【非static 方法、代码块】则必须有【实例对象】才能调用

## public

如果a包下的`A类`是`public`的，它的字段和方法都是private的。

→ 在`b包`下的`B类`可以创建`A类`的对象，但是无法访问`A类对象的字段和方法`。

如果a包下的`A类``没有修饰符`，它的字段和方法都是private的。

→ 在`a包`下的B类可以创建`A类`的对象，但无法访问A类对象的字段和方法。

→ 在`b包`下的B类无法创建A类的对象。


## final 值 方法 类有什么区别，容器被final修饰会怎样

[2分钟记住final の 使用](https://www.bilibili.com/video/BV19A4y1o7ET)

- final 放在 class 前面， 该 class 不能被【继承】
- final 放在 method 前面， 该 method 不能被【重写】，但可以被【重载】
- final 放在 【变量】前面：分成3种情况：
  1. 如果 final 放到 【静态变量static final】前面，有2次赋值机会：要么在【static final 声明】赋值，要么在【static {} 静态代码块】赋值
  2. 如果 final 放到 【成员变量|实例变量 final】前面，有3次赋值机会：要么在【final 声明】赋值，要么在【{} 非静态代码块】赋值，要么在【构造器 {}】赋值
  3. 如果 final 放到 【局部变量 func (①final xxxx) {②final xxxx}】前面:
     - ①【方法参数前】，有1次赋值机会：在调用method时，赋值
     - ②【方法里面局部变量前】，有2次赋值机会：要么在【声明】赋值，要么在【后面代码】赋值

- final基本类型变量，值不能更改
- final引用变量，值不能更改，引用对象是可以修改的







## 说一下你熟悉的设计模式？

创、结、行

按照模式的【应用目标】分类：

1. 创建型：对【对象创建过程】的各种问题 and 解决方案 の 一个总结。
2. 结构型：对【软件设计结构】的总结。重点关注【类、对象继承、组合方式】的实践经验的总结
3. 行为型：【从类】or【对象】之间的【交互】，【职责划分】等角度，总结的模式

## 在学习【框架】或【中间件】底层源码遇到的设计模式？

[【23种设计模式全解析】终于有人用一个项目将23中设计模式全部讲清楚了](https://www.bilibili.com/video/BV19g411N7yx)

按照模式的【应用目标】分类：

1. 创建型：对创建对象时的【各种问题】和【解决方案】的总结
      1. 工厂模式
      2. 单例模式
      3. 建造者模式
      4. 原型模式
2. 结构型：对【软件设计的结构】的总结。关注【类、对象继承、组合方式】的实践经验的总结
      1. 适配器模式
      2. 桥接模式
      3. 过滤器模式
      4. 组合模式
      5. 装饰器模式
      6. 外观模式
      7. 享元模式
      8. 代理模式
3. 行为型：从 class 或者 object 之间的一个交互、职责划分等角度总结的模式
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

## 遇到过哪些设计模式？

单例模式主要是避免了一个全局使用的类频繁地创建和销毁。当想要控制实例数据节省系统资源的时候可以使用。

在java中单例模式有很多种写法，比如什么饱汉、饿汉，双重检查等等但是在scala语言中这个完全不需要什么这么多花里胡哨的，仅仅需要一个伴生对象。伴生对象就是单例模式的。

伴生对象采用object声明

```scala
private[netty] object NettyRpcEnv extends Logging {
  /**
   * When deserializing the [[NettyRpcEndpointRef]], it needs a reference to [[NettyRpcEnv]].
   * Use `currentEnv` to wrap the deserialization codes. E.g.,
   *
   * {{{
   *   NettyRpcEnv.currentEnv.withValue(this) {
   *     your deserialization codes
   *   }
   * }}}
   */
  private[netty] val currentEnv = new DynamicVariable[NettyRpcEnv](null)

  /**
   * Similar to `currentEnv`, this variable references the client instance associated with an
   * RPC, in case it's needed to find out the remote address during deserialization.
   */
  private[netty] val currentClient = new DynamicVariable[TransportClient](null)

}
```

## 工厂模式

（简单工厂、抽象工厂）：解耦代码。

简单工厂模式（又名【静态方法模式】），

简单工厂模式就是：他的行为就很简单，就是定义【一个接口】用来创建对象。

但是它创建【工厂类】的时候是通过【客户端传入参数】进行决定创建什么工厂的。

这里我们以RPC为参考

1. **工厂接口**

【【工厂方法模式】】肯定是要继承一个【工厂接口】的，在SparkRPC的工厂方法中也不会例外,当然这里的接口是特质，特质和类的不同就是一个子类中可以混入多个特质

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

继承这个特质的类仅有NettyRpcEnvFactory，也就是RpcEnvFactory仅有一个工厂实现

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

有了工厂之后，使用改工厂的类也就是客户端，就可以很方便地就实例化一个RpcEnv的产品。下面看一下具体的客户端代码。也就是最后代码中的new NettyRpcEnvFactory().create(config)，这里创建了一个RpcEnv的Rpc通信环境。

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

那么思考一下这里为什么使用的是【工厂方法模式】而不是【抽象工厂模式】

这是因为【抽象工厂模式】支持的场景不同。

【抽象工厂模式】的产生是解决了【工厂方法模式】中每个工厂仅仅生产一个产品，如果存在需要两个产品混合的产品那么就需要再次创建工厂如此这样造成的工厂类冗杂问题。但是此例中我们不需要其他的产品。既是是SparkRpc从Akka转移到Netty中时也仅仅需要创建新的实现工厂而不是进行产品的整合。

就需要再次创建工厂如此这样造成的工厂类冗杂问题。但是此例中我们不需要其他的产品。既是是SparkRpc从Akka转移到Netty中时也仅仅需要创建新的实现工厂而不是进行产品的整合。

此场景中NettyRpcEnv的职责就仅仅是生产RpcEnv，而在Spark架构中我们需要的也仅仅是RpcEnv这一个产品并不需要其他的Rpc产品。

## 装饰器模式



## 代理模式

[代理模式](https://www.bilibili.com/video/BV1cz41187Dk)

## 责任链模式

Java个体户：https://www.bilibili.com/video/BV1pP4y1M7Vn

[责任链模式，应用场景有哪些？](https://www.bilibili.com/video/BV1VS4y1m7ob)

https://www.bilibili.com/video/BV1UL4y157eH

什么是责任链？

- 将处理不同逻辑的对象连接成一个【链表结构】，每个对象都保存下一个节点的引用

包括：

- 单向责任链（容易理解），filter过滤器，Spring的Interceptor拦截器
- 双向责任链（执行闭环），netty中的管道pipeline

优点：

1. 将【请求】和【处理】解耦
2. 【请求处理者】将不是自己职责范围内的请求，转发给下一个节点
3. 【请求发送者】不需要关心【链路结构】，只需要等待【请求处理结果】即可
4. 【链路结构】灵活，可以动态的增删责任

缺点：

1. 如果【责任链路】太长，它会影响程序的【整体性能】
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

## java集合

## 介绍一下jvm の 区域

## java sout（0.1+0.2）输出什么，为什么

## 隐含 の 强制类型转换

<https://www.bilibili.com/video/BV1Fb4y1Y7wX>

```java
public static void main(String[] args) {
      short s1 = 1;
      s1 = s1 + 1;
      // ❌s1 + 1的运算结果是int型，需要【强制类型转换】才能赋值
}

public static void main(String[] args) {
      short s1 = 1;
      s1 += 1;
      // ✅正确编译，相当于 s1 = (short)(s1 + 1);
}
```

## Java集合 の 框架体系图

## spark executor内 の task是怎么彼此隔离 の （从线程池 の 角度，还有切分stage）

## spark那些外部资源 还有第三方jar包之类 の 都放在哪（应该是这么问 の ，不太会，说了下内存结构，告诉我是java classloader相关 の 机制）

## 遇到oom怎么排查（有jvm提供 の 工具查看堆栈使用情况，具体不太了解只在跑spark遇到过、分享了下spark里 の 排查经验和参数调整）

1. 查看服务器的运行日志，捕捉OOM
2. 使用jstat查看监控JVM的内存和GC情况
3. 使用MAT工具载入dump文件，分析大对象的占用情况

## linux下 の 调查问题思路：内存、CPU、句柄数、过滤、查找、模拟POST和GET请求等等场景

## 拼多多面试官：线上CPU飙高如何排查？

https://www.bilibili.com/video/BV15T4y1y7eH

## 阿里面试官：Jar包冲突如何解决？

https://www.bilibili.com/video/BV1GL411w7gw

## 网络问题排查-套路汇总

https://www.bilibili.com/video/BV1wu411o75x

## 线上问题排查套路汇总-CPU篇

https://www.bilibili.com/video/BV1HL4y167Ex

## 外部排序简单应用。有10个500M的日志文件，其中每个日志文件内部按照时间戳已排序，内存只有1G的机器如何对其所有排序合并成一个文件。

https://www.bilibili.com/video/BV1JN411Z7k4

归并排序思路：

内存中维持一个【元素个数为10】的【小顶堆】，

同时维持【10个指针】，指针分别指向【10个日志文件】的【首条记录元素】。

将【10个元素】读取进入到【内存】中之后，对其【进行排序】，

取出【堆顶元素】，写入【新的日志文件】，并移动【该元素对应的文件指针】读取【下一个记录】到小顶堆中，

如此重复，直到所有的日志文件读取完成写入新日志文件。

由于内存为1G，也可以先从每个日志文件一次性读取50M，逐条读取，排序，写入新的日志文件，直到读取完毕。

## 内存中的Buffer和Cache是一个东西么

https://www.bilibili.com/video/BV1Jh41167Lo

两者的目的都是解决速度匹配的问题，做法也相似，只是思想上略有不同。

| cache  | buffer  |
|---|---|
| 为了解决【高速设备】和【低速设备】读写速度之间的【速度差】而引入的，主要利用的是局部性原理  | 缓冲数据的冲击。把小规模的IO整理成平稳的较大规模的IO，较少磁盘的随机读写次数。比如从下载大资料，不可能一次下了一点几兆就写入硬盘，而是积攒到一定的数据量再写入，上传文件也是类似，而buffer的作用就是积攒这个数据。把多次操作进行合并。  |
| 数据读取具有随机性  | 有顺序的  |
| 读缓存的时候，如果缓存中没有，则读取实际数据，然后将数据加入到cache，先进入cache中的数据不一定先被读取  | 先进入buffer中的数据一定会被先读取出来，具有顺序访问的特点。  |
| read cache，提高读性能  | write buffer，提高写性能  |
