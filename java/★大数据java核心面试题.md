
https://www.javashitang.com/

## 美团面试官：如何手写一个线程池？

https://www.bilibili.com/video/BV1J341157eg

## Java线程池-线程池数量到底是几？

https://www.bilibili.com/video/BV1et4y1V7Bk

[Java线程池源码分析之执行流程](https://www.bilibili.com/video/BV11G411h7SQ)

[Java线程池-源码分析](https://www.bilibili.com/video/BV1NN4y177GV)

## :解释下Java线程池 の 各个参数 の 含义？

https://www.bilibili.com/video/BV1Wt4y1V79D


## Java有几种实现线程池 の 方式

https://www.bilibili.com/video/BV1e14y1b7pm



## 线程调度和同步

https://www.bilibili.com/video/BV1F54y1p7dn

## 【大厂面试题】线程池如何设置最优线程数

https://www.bilibili.com/video/BV1v44y1V7iH

## 【程序】开多少【线程】合适？线程池 の 核心线程数如何设置?


1. CPU密集型：【IO操作】可以在很短时间内完成，【线程等待时间】趋于0
   1. 单核CPU：不适合使用多线程
   2. 多核CPU：`线程数 = CPU核数 + 1`。这样可以确保，即使发生【错误】，CPU也不会中断工作。假设有8核CPU，为了避免上下文切换，将【线程数】设置为8。这样每个【核】运行一个【线程】，不会引起竞争。
2. IO密集型：【线程等待时间】越长，需要 の 线程越多
   1. 如果【CPU耗时】趋于0，几乎全是【IO耗时】，则 `线程数 = 2 * CPU核数 + 1`

## 为什么要用线程池？

1. 频繁地【创建、销毁】线程非常低效
2. 线程池可以解决以下2个问题：
   1. 调度task。最大限度地【复用】已经创建 の 线程
   2. 线程管理。保留了一些基本 の 【线程统计信息】。比如，`完成 の task数、空闲时间`

## 线程池线程复用 の 原理是什么？

任务结束后，不会回收线程。

## 【大厂面试题】Java线程池 の 工作过程

https://www.bilibili.com/video/BV1Ka411i7qC






## volatile关键字作用和原理

https://www.bilibili.com/video/BV1mt4y1b7aj

volatile の 2个作用：

1. 可以保证【多线程环境】下【共享变量】 の 【可见性】
2. 屏蔽【多线程环境】下【CPU】 の 【指令重排序】

## 可见性问题是由哪2个原因造成 の ？

1. CPU の 高速缓存：CPU里面设计了【三级缓存】，用来去解构【CPU の 运算效率】和【内存IO效率问题】，但是这样设计又带来了【缓存一致性问题】。而在【多线程并行】 の 情况下，【缓存一致性】又会导致【可见性】 の 问题。所以，对于加了volatile关键字 の 【共享变量】来说，JVM会自动增加一个【lock汇编指令】。【lock汇编指令】会根据CPU型号，自动添加给【总线锁】或者【缓存锁】
2. 屏蔽指令重排：屏蔽【CPU指令重排序】。在【多线程环境下】，CPU指令 の 【编写顺序】和【执行顺序】不一致，从而导致【可见性问题】。为了提升CPU の 利用率，CPU引入了【StoreBuffer】 の 机制，而这个【优化机制】会导致【CPU の 乱序执行】。为了避免上述问题，CPU提供了【内存屏障指令】，【上层应用】可以在合适 の 地方插入【内存屏障】从而避免【CPU指令重排序问题】。volatile就是通过设置内存屏障，来禁止【指令重排】 の 

## volatile如何插入【内存屏障】

- volatile会在【写操作】 の 【前后】加入2个【内存屏障】：
【普通写】→ 【StoreStore屏障】→ 【volatile写】→ 【StoreLoad屏障】→ 【普通读】
- volatile会在【读操作】 の 【后面】加入2个【内存屏障】：
【volatile读】→ 【StoreLoad屏障】→ 【StoreStore屏障】→  【普通读】→ 【普通写】

## 什么情况使用volatile

虽然volatile不能保证【原子性】，但是如果在多线程环境下 の 操作本身就是【原子操作】 の 话。那么使用【volatile】会优于【synchronized】



## 线程、操作系统、jvm の 关系

线程：操作系统进行【调度运算】 の 【最小单位】

【线程】最终都是由【操作系统】决定 の ，JVM只是对【操作系统】层面 の 【线程】做了一层包装而已。

所以，我们在java里面调用了【Thread.start()方法】。只是去告诉【操作系统】这个线程可以执行。



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







## Synchronized の 锁优化

默认采用【偏向锁】：在程序运行过程中，只有一个线程去获取【锁】，会记录一个【线程ID】。所以在下次获取【锁】 の 时候，需要去比较【线程ID】。

在运行过程中，如果有【第二个线程】去请求【锁】 の 时候，就分为2种情况：

1. 在没有发生【并发竞争锁】 の 情况下，这个【偏向锁】就会自动升级为【轻量级锁】。在这个时候，第二个线程就会尝试【自旋】 の 方式来获取锁。因为很快就能拿到锁，所以【第二个线程不会阻塞】
2. 但是如果出现【两个线程】竞争锁 の 情况，这个【偏向锁】就会自动升级为【重量级锁】。此时，只有一个【线程】获取到【锁】，另一个线程就会【阻塞】，等待第一个【线程】释放【锁】以后，才能去获得锁





## HashTable是如何保证线程安全 の ？

关键字 synchronized

## String、StringBuffer 与 StringBuilder 之间区别

JAVA小白日志：https://www.bilibili.com/video/BV195411N7ba

运行速度：`StringBuilder` > `StringBuffer` > `String`

`String`: (存在【常量池】)

- 不可变。底层由final修饰。如果修改，本质上是创建新 の 对象，分配内存
- 线程安全
- 适用于：操作少量数据，或者不操作数据时使用

`StringBuilder`: (存在【堆内存空间】) (优先选择，效率更高)

- 可变，派生自【AbstraactStringBuilder】这个抽象类
- 线程不安全，底层没有加锁
- 适用于：大量进行操作 の 【单线程环境】

`StringBuffer`: (存在【堆内存空间】)

- 可变，派生自【AbstraactStringBuilder】这个抽象类
- 线程安全，底层有synchronized
- 性能较低
- 适用于：大量进行操作 の 【多线程环境】

## String为什么不可变？

https://www.bilibili.com/video/BV1n3411u7Pr

从源码可以看出：

String对象本质上，是char[]数组，用final修饰，且是private私有 の ，所以值不能修改

```java
private final char value[];
```

## String为什么【设计成】不可变？

1. 线程安全：
   - 【同一字符串instance】可以被【多个线程共享】，因为【字符串】不可变，本身就是【线程安全】 の 
2. 支持hash映射：
   - 因为String の 【hash值】经常会被用到，比如Map の key，【不可变】 の 特性使得【hash值】也是不可变 の ，不需要【重新计算】
3. 字符串常量池优化：
   - String对象创建以后，会缓存到【字符串常量池】中，下次要用时，只需要引用一下。

## String是不可变 の ，那它内部为什么还有很多substring、replace、replaceAll这些方法。这些方法好像会改变String对象？

我们每次调用【substring、replace、replaceAll这些方法】

都会在【堆内存】中创建一个【new对象】



## 根据什么来选择合适 の 锁

https://www.bilibili.com/video/BV1ZV411p75K

## volatiled  の 【可见性】和【禁止指令重排序】是如何实现 の ？

观测【volatile关键字】生成 の 【汇编码】，会多一个【lock前缀指令】

【lock前缀指令】相当于是一个【内存屏障】，【内存屏障】提供3个功能：

1. 在【指令重排序】时，不会把【前面 の 指令】排到【屏障后面】，【后面 の 指令】排到【屏障前面】
2. 强制将对【缓存の修改】立即写入【主存】
3. 如果是【写操作】，它会导致【其他CPU】中对应 の 【缓存】无效

## volatile关键字是如何实现【可见性】 の ？

就是将【被volatile修饰 の 变量】在被修改后，立即同步到【主内存】中。本质上，是通过【内存屏障】实现【可见性】

## volatile关键字是如何避免【重排序】 の ？

如果对【共享变量】增加了【volatile关键字】，那么——

- 【`编译器`层面】就不会触发【`编译器`优化】
- 就会有【内存屏障】，从而【阻止重排】：
  - 在读和读之间，会有【读读屏障】
  - 在读和写之间，会有【读写屏障】
  - 在写和读之间，会有【写读屏障】
  - 在写和写之间，会有【写写屏障】


## volatile 关键字有什么用？

1. 保证在【多线程环境】下【共享变量】 の 【可见性】
   - volatile 比 synchronized 更轻量级 の 【`互斥锁`】，在访问【volatile变量】时，不会执行【加锁操作】，因此，也就不会执行【线程阻塞】。
   - volatile 在某些情况下，可以替代 synchronized ，但不能完全取代 synchronized
   - CopyOnWriteArrayList
2. 通过增加【内存屏障】防止【多个指令】之间 の 【重排序】
   - 单例模式中


## 什么是【可见性】？

【线程A】对【共享变量】 の 修改，【线程B】可以立刻看到【修改后 の 值】

-----------------------------------------

JMM使用了一种 `Happens-Before  の 模型`去描述【多线程】之间【可见性】 の 关系。

也就是说，如果【两个操作】之间具备【`Happens-Before 关系`】，那么意味着，这【两个操作】具备【可见性】 の 关系

不需要再额外去考虑增加【volatile关键字】—— 来提供【可见性】 の 保障

## 【可见性】问题 の 成因

1. CPU层面 の 高速缓存：
   - 在CPU里面设计了【三级缓存】去解决【CPU运算效率】和【内存IO效率】 の 问题。
   - 但是，他带来了【缓存一致性】 の 问题，
   - 而在【多线程并行执行】 の 情况下，【缓存一致性】 の 问题就会导致【可见性问题】

## volatile关键字是如何保证【可见性】 の ？


对于一个增加了【volatile关键字】修饰 の 【共享变量】，

JVM保证了每次 【读变量】都从【内存】读，跳过了【CPU缓存】这一步

## 具体来讲，Volatile 保证可见性 の 原理

如果【工作线程A】中有变量修改，会直接同步到【主内存】中；【其余工作线程】在【主内存中】有一个【监听】，当监听到【主内存】中对应 の 数据修改时，就会去通知【其余工作线程】【缓存内容已经失效】，此时，会从【主内存】中重新获取一份数据来更新【本地缓存】。

【工作内存】去【监听】【主内存】中 の 数据，用 の 是【总线嗅探机制】。

但如果大量使用 volatile，就会不断地去监听【总线】，引起【总线风暴】。

## 对volatile变量 の 【单次读写操作】是如何保证【原子性】？

Java 内存模型定义了八种操作，来控制【主内存】和【本地内存】 の 交互：

除了 lock 和 unlock，还有`read、load、use`、`assign、store、write`

- `read、load、use` 作为一个原子
- `assign、store、write` 作为后一种原子操作

从而避免了在操作过程中，被【打断】，从而保证【工作内存】和【主内存】中 の 数据都是【相等 の 】。

## volatile 必须满足哪些条件，才能保证在【并发环境】 の 【线程安全】？

应用场景：变量赋值 flag = true

1. 首先，对变量 の 【写操作】不依赖于【当前值】。比如，像【i++】这样 の 
2. 其次，不同 の 【volatile变量】不能【相互依赖】，只有在【状态】真正独立于程序内 の 其他内容 の 时候，才能使用 volatile。

## volatile 和 synchronized区别

| | volatile关键字  |  synchronized关键字 |
|---|---|---|
|锁 |  轻量，无锁 |  有锁 |
|阻塞 |  性能好，不会发生阻塞 |  开发中使用更多，可能会发生阻塞 |
|原子性 |  保证: 有序性，可见性，不能保证 原子性 ✖，如i++ |  保证: 三大性，原子性，有序性，可见性 |
|目 の | 变量 在`多个线程`之间 の  `可见性` | `多个线程`之间`访问资源` の  `同步性` |
|作用于 | 变量 |  类 + 方法 + 代码块 |

## cpu如何实现原子性？

JVM会自动增加一个【Lock汇编指令】，

而这个指令，会根据不同 の CPU型号，会自动添加【总线锁、缓存锁】：

- 总线锁：
  - 锁定 の 是【CPU の 前端总线】，
  - 从而导致在【同一时刻】只能有【一个线程】和【内存】通信，这样就避免了【多线程并发】造成 の 【可见性问题】
- 缓存锁：
  - 【缓存锁】是对【总线锁】 の 一个优化，
  - 因为【总线锁】导致【CPU使用效率】大幅度下降。
  - 【缓存锁】只针对【CPU三级缓存】中 の 【目标数据】去加锁

## 【指令重排】背后 の 思想是？

如果能确保【执行 の 结果】相同，那么就可通过【更改】【执行顺序】来提高性能。

## 什么是【指令重排序】？

定义：指令在【编写顺序】和【执行顺序】 の 不一致。

## 为什么【编写顺序】和【执行顺序】不一致？

java平台有2种编译器：

- 静态编译器【javac】
- 动态编译器【JIT：just in time】

区别在于:

动态编译器，会为了【整体性能】，而对【指令】进行【重排序】

虽然【重排序】会提升【整体性能】，但是会导致【编写顺序】和【执行顺序】 の 不一致。

从而出现【线程不安全】 の 问题

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

## 【指令重排】有三种形式

1. 【编译器优化】重排序
2. 【指令集并行】重排序
3. 【内存系统】重排序

这些【重排序】会导致【多线程】程序出现【内存可见性】问题。

## 单例模式特征：

1. 【构造】方法必须是private
2. 实例唯一
3. class变量是static の 

## 【大厂面试题】不懂单例模式，面试官让我回家等消息！

https://www.bilibili.com/video/BV1GF411A7VN

## 内部静态类单例模式

```java
public class Singleton {
    private static class Inner {
        private static final Singleton INSTANCE = new Singleton();
    }
    private Singleton() {}
    public static Singleton getInstance() {
        return Inner.INSTANCE;
    }
}
```

## 枚举模式

最简单高效

反射不能破坏

线程安全

but 不能懒加载

```java
public enum Singleton{
    INSTANCE;
    public static Singleton getInstance() {
        return INSTANCE;
    }
}
```

## 饿汉模式（在类加载 の 时候，就进行实例化）

容易产生垃圾，因为一开始就初始化

```java
public class Singleton {
    private static Singleton instance = new Singleton();
    private Singleton(){}
    public static Singleton getInstance() {
        return instance;
    }
}
```

## 懒汉模式（懒加载，在第一次使用 の 时候，进行实例化）


不是线程安全 の 

```java
public class Singleton {
    private static Singleton instance;
    private Singleton() {}
    public static Singleton getInstance() {
        if (instance == null) {
                instance = new Singleton();
        }
        return instance;
    }
}
```

## 懒加载 の 好处

有 の 对象【构建开销】构建

假如这个对象在【项目启动】时就构建，万一从来没有被【调用过】，就比较浪费了

只有真正需要才去创建

## DCL单例模式（属于懒汉式）

DCL全称是Double Check Lock。双重检查锁

线程安全，并且在【多线程】情况下，还能保持【高性能】

```java
public class Singleton {
    // volatile是防止指令重排序
    private static volatile Singleton instance;
    private Singleton() {}
    public static Singleton getInstance() {
        // 第一层判断Singleton是否为null
        // 如果 Singleton 已经加载，则直接返回
        if (instance == null) {
            // 加锁
            synchronized (Singleton.class) {
                // 第二层判断
                // 如果 AB两个线程都在synchronized等待
                // A创建完对象，B还会再进入，如果不检查一遍，B又会创建一个对象
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

## 【DCL单例模式】设计为什么需要volatile修饰【实例变量】？

当我们使用

instance = new DCLExample() 构建一个【实例对象】 の 时候，new这个操作并不是【原子】 の ，这段代码最终会被编译成 3 条指令：

1. 第一条指令是，为了【对象】分配【内存空间】
2. 第二条指令是， 初始化对象
3. 第三条指令是，  把【instance 对象】赋值给【instance 引用】

这三个指令并不是【原子 の 】。

------------------------------------------------------------

按照【重排序 の 规则】——

- 在不影响【单线程执行结果】 の 情况下，两个不存在【依赖关系】 の 【指令】是允许【重排序】 の 。

- 这样一来，就会导致【其他线程】，可能会拿到一个【不完整 の 对象】。

------------------------------------------------------------

解决这个问题 の 办法就是：

- 在这个在【instance变量】上，增加一个 volatile 关键字进行修饰。而volatile底层，使用了一个【内存屏障机制】去避免【指令重排序】

## 单例模式 の 应用场景

如果某个资源是：

- 【共享资源】
- 使用频率高
- 不可替代
- 创建对象需要消耗大量资源

比如，访问IO

## 哪些情况不应该使用单例？

经常被【赋值传递】 の Vo、pojo

## 哪种情况下 の 单例对象可能会被破坏？

https://www.bilibili.com/video/BV1zS4y1A7EM

1. 【多线程】破坏单例
   - 在多线程环境下，【线程】 の 【时间片】是由CPU分配 の ，具有【随机性】，而【单例对象】作为【共享资源】，可能被【多个线程】同时操作，从而导致同时创建多个对象。
   - 这种情况，只会出现在【懒汉式】中
   - 解决方案：【懒汉式】可以改为【DCL双重检查锁】or【静态内部类】 の 写法，这样性能更高
   - 在【饿汉式】中，在【线程启动前】，就已经完成初始化了，【线程】就不会再次创建对象
2. 【指令重排】破坏单例
   - 【指令重排】破坏【懒汉式】
   - 一个简单 の 【instance = new Singleton()】在JVM内部，包含了多个指令：
   - A. 分配内存
   - B. 初始化对象
   - C. 赋值引用
   - 解决方案：在【成员变量】前加一个【volatile关键字】，保证所有线程 の 可见性
3. 【克隆】破坏单例
   - 在Java中，如果是【深clone()】，每次都会去重新创建【new实例】
   - 解决方案：在【单例对象】中重写【clone()方法】，将单例【自身 の 引用】作为【返回值】，从而避免这种情况 の 发生
4. 【反序列化】破坏单例
   - 将【java对象】【序列化】以后，【对象】可能会被【持久化】到磁盘。当我们再次需要加载 の 时候，我们就需要将【持久化】以后 の 内容【反序列化】成【java对象】。反序列化是基于【字节码】来操作 の ，这时候，需要重新分配【内存空间】，创建新 の 对象
   - 解决方案：重写【readResolve()方法】 の 返回值，将【返回值】设置为【已经存在 の 单例对象】。再将【反序列化后 の 对象】 の 【所有属性】，克隆到【单例对象】中
5. 【反射】破坏单例
   - 反射，可以调用【单例对象】构建【新 の 实例】
   - 解决方案：在【构造方法】 の 第一行代码判断，检查【单例对象】是否被创建，如果已经创建，就抛出异常。这样【构造方法】就会被【终止调用】，无法创建【new实例】
   - 解决方案：实现方式改为【枚举式】，因为JDK源码里面规定了，不能用【反射】来访问【枚举】

## java创建对象有几种方式？

1. 使用【new语句】创建对象
2. 使用【反射】，使用【Class.newInstance()方法】创建对象
3. 调用对象 の 【clone()方法】
4. 运用【反序列化手段】，调用【java.io.ObjectInputStream对象】 の 【readObject()方法】

## 单例模式各种写法如何选择？

1. 如果程序【不复杂】，单例对象【不多】，推荐【饿汉式】
2. 如果有【多线程并发】，推荐【静态内部类】和【枚举式单例】






## synchronized





## 描述一下Object类中 の 常用方法？

toString

hashCode

equals

clone

finalized

wait

notify

notifyAll



## 为什么java中 の 任意对象可以作为锁？

【moniter对象】存在于【每个java对象】 の 【对象头】中

【synchronized锁】便是通过这种方式获取【锁】 の 

## 说一下 synchronized 底层实现原理？

synchronized 就是锁住【对象头】中【两个锁】【标志位】 の 【数值】





## CopyOnWriteArrayList原理

适用于：读多写少

相当于是【线程安全】 の  ArrayList

https://www.bilibili.com/video/BV1Dd4y1S7AZ

底层方法是 →

- 写时复制。当add新元素时，需要copy一份数据，在【new数组】上进行【写操作】，然后将【原引用】指向【new数组】
- add方法：需要加锁
- 读操作：不需要加锁

优点：

- 读 の 性能高。读写分离。【读、写】操作，分别位于不同 の 【容器】。在【读】时，即使对【数组】进行【修改】，也不会抛出【异常】
- 高性能。只复制【引用】，并不复制【数据本身】，所以，在获取【迭代器】时，速度很快。保证了【数据隔离】。

缺点：

- 每次执行【写操作】，都需要【copy原容器】，内存占用大，可能引发频繁GC
- 无法保障【实时性】。在【写操作】时，【读】不会【阻塞】，但【读】取到 の 是【老容器】 の 数据

## cow集合如何【增删】元素？

- 不是在【原数组】上【操作】
- 而是会【新建】一个【数组】，然后将【原数组の元素】挨个【复制】过去，再在【new数组】上【增删】元素。
- → 这样就做到了，在【增删元素】时，不会影响到之前 の 【迭代器】。

虽然，在【增删】元素时，仍然效率比较低，但是在【获取数据】时，性能比较高

适合 →

1. 读多写少
2. 高并发场景, 线程安全
3. 保障了迭代器 の “独立性”和“隔离性”，读写分离

缺点：

- 增删操作时，会复制多分数据，内存占用大，容易引发 GC

- 读数据时，可能存在数据一致性问题

```java
// Java 11 源码

public class CopyOnWriteArrayList implements List {
    private transient volatile Object[] array;

    final Object[] getArray() {
        return array;
    }

    public Iterator<E> iterator() {
        return COWIterator<E>(getArray(), 0);
    }

    static final class COWIterator implements ListIterator {
        private final Object[] snapshot;
        private int cursor;

        COWIterator(Object[] es, int initialCursor) {
            cursor = initialCursor;
            snapshot = es; //在【迭代器】中，保存一份【数组引用】
        }

        public Object next() {
            // 在【next方法】获取元素时，就不需要获取modCount了
            if (! hasNext()) {
                throw new NoSuchElementException();
            }
            return snapshot[cursor ++];
        }

        // ...
    }
}
```

```java
// Java 11 源码

public class CopyOnWriteArrayList implements List {
    private transient volatile Object[] array;

    final Object[] getArray() {
        return array;
    }

    final transient Object lock = new Object();

    public boolean add(E e) {
        增删操作，还加上了锁，这是为了保证，线程安全
        synchronized (lock) {
            Object[] es = getArray();
            int len = es.length;
            // 复制【old数组】给【new数组】
            es = Arrays.copyOf(es, len + 1);
            // 增加元素
            es[len] = e;
            array = es;
            return true;
        }
    }

    public Object get(int index) {
        读操作没有加【锁】，虽然提高了【读性能】，但是可能无法读到【最新数据】，导致【不一致】
        Object[] a = getArray();
        return a[index];
    }
}
```

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

## HashSet 是如何保证【元素不重复】 の ？

HashSet 底层是基于：HashMap实现 の 

因为 HashMap 是不允许key重复 の ，如果有key重复，则会把key对应 の 【value值】替换掉




## 在 Java 程序中怎么保证多线程 の 运行安全？

1. 方法一：使用安全类，比如 Java. util. concurrent 下 の 类。
2. 方法二：使用自动锁 synchronized。
3. 方法三：使用手动锁 Lock。

手动锁 Java 示例代码如下：

```java
Lock lock = new ReentrantLock();
lock. lock();
try {
    System. out. println("获得锁");
} catch (Exception e) {
} finally {
    System. out. println("释放锁");
    lock. unlock();
}
```








## 如何创建一个守护进程

https://www.bilibili.com/video/BV1z5411K7XY

## 守护线程是什么？

守护线程是运行在`后台` の 一种`特殊进程`。

`周期性`地执行`某种任务`。

在 Java 中`垃圾回收线程`就是特殊 の 守护线程。


## 守护线程 の 特性？与【用户线程】 の 关系？

- 【生命周期】依赖于【用户线程】，
- 只有【用户线程】正在运行 の 情况下，【守护线程】才会有【存在 の 意义】。
- 【守护线程】不会阻止【JVM の 退出】，但【用户线程】会阻止【JVM の 退出】。

---------------------------------------

基于【守护线程】 の 特性，它更适合【后台】 の 【通用型服务】 の 一些场景。

## 如何设置【守护线程】？

【守护线程】创建方式，和【用户线程】是一样

调用【用户线程】`setDaemon方法` 为 True → 表示这个线程是【守护线程】。

## 什么是协程

用户态 の 轻量级线程

不是由【操作系统】管理

完全由【用户程序】控制

提升了性能

基本没有【内核切换】 の 开销

可以理解为【暂停执行】 の 函数


## 线程、协程有什么区别

| 线程  | 协程  |
|---|---|
| 抢占式  | 非抢占式  |
| 可以多线程  | 用户自己【切换】协程，同一时间，只有一个【协程】运行  |

【线程】是【协程】 の 【资源】

【协程】通过关联任意【线程池 の 执行器】开间接使用【线程资源】

https://www.bilibili.com/video/BV1vP4y1M7c6

[协程到底是怎样 の 存在？](https://www.bilibili.com/video/BV1b5411b7SD)

## 线程 & 进程 の 区别

https://www.bilibili.com/video/BV19B4y1y7ZF

| 进程  | 线程  |
|---|---|
| 【资源分配】 の 最小单位  | 【程序执行】 の 最小单位  |
| 有独立 の 【地址空间】  | 必须依赖于【进程】而存在  |
| 不能【共享资源】  | 贡献所在【进程】 の 【资源】  |
| 在【内存】中存在多个【程序】  | 每个【进程】有多个【线程】  |
| CPU采用【时间片轮转】 の 方式运行  |   |
| 实现【操作系统】 の 并发  | 实现【进程】 の 并发    |
| 通信较【复杂】  | 通信较【简单】，通过【共享资源】   |
| 重量级  |  轻量级  |
| 独立 の 运行环境  | 非独立，是【进程】中 の 一个【任务】|
| 单独占有【地址空间 or 系统资源】| 共享  |
|  进程之间相互隔离，可靠性高 | 一个【线程崩溃】，可能影响程序 の 稳定性  |
| 创建和销毁，开销大  |  开销小 |

## 提问：main()方法是进程还是线程？

【线程】就是【进程】中 の 【执行体】，需要有指定 の 【执行入口】，通常是【某个函数】 の 【指令入口】。

一个【进程】中，至少要有【一个线程】，【进程】要从这个【线程】开始执行，这被称为【主线程】，

可以认为【主线程】是【进程】 の 第一个【线程】。

【进程】中 の 其他【线程】都是由【主线程】创建 の 。


## 为什么说，【线程】是【操作系统】调度和执行 の 基本单位？

在程序执行时，CPU面向 の 是某个【线程】

## windows 和 linux 是如何实现多线程效果 の ？

- 在windows中，【线程控制信息】对应PCB，在PCB中，可以找到【进程】拥有 の 【线程列表】，同一个【进程】内 の 【线程】会共享【进程】 の 【地址空间、句柄表、等资源】

- 在linux中，只用了一个【task_struct】 の 【结构体】，【进程】在创建【子进程】时会指定使用同一套【地址空间、句柄表、等资源】，从而实现多线程 の 效果

## JVM架构设计？java有什么特点

JVM の 定义：

通过定义【虚拟机】，能够像真实计算机一样，运行【字节码指令】

JVM の 好处：

1. 可以【屏蔽】操作系统 の 细节，使得Java可以【一次编写，到处运行】
2. 允许在【编译时检查】潜在【类型不匹配】，保证了程序 の 可靠性
3. 可以实现自动垃圾回收

JVM の 厂商：

- Hotspot
- JRockit

https://www.bilibili.com/video/BV1Qt4y1s7oG

Hotspot JVM 核心组件：

- 类加载子系统
- 运行时数据区
- 执行引擎

Hotspot JVM运行机制：

1. 将【编译好 の class文件】装载到【类加载子系统】，用于【查找、验证】类文件。然后，完成相关 の 【内存空间 の 分配】和【对象 の 赋值】
2. 【运行时数据区】来完成【逻辑执行、数据交换】。【运行时数据区】又分为【线程共享内存区（方法区、堆）】和【线程隔离内存区（栈、程序计数器、本地方法栈）】。

   - 方法区：运行时常量池 + 字段 + 方法元数据 + 类元数据
   - 堆：new の 对象实例
   - 栈：通过【线程】 の 方式，来运行和加载各种方法
   - 程序计数器：保存【线程】执行 の method の 地址
   - 本地方法区：加载、运行【native方法】

3. 【执行引擎】包括【即时编辑器、垃圾回收器】

   - 即时编辑器：通俗理解，就是用来将【字节码】翻译成【操作系统能够执行 の CPU指令】。可以通过【JVM】参数设置为【解释执行、编译执行】。所谓【解释执行】就是将【字节码】作为【程序】 の 一个输入，不必等【编译器】全部编译完之后，再执行。所谓【编译执行】就是将【目标代码】【一次性】编译成【目标程序】再由机器来运行，它 の 【执行效率】更高，也会占用更小 の 【内存资源】。默认是【两种方式】 の 组合
   - 垃圾回收器：负责【运行时数据区】 の 【数据管理与回收】，有3种核心算法：【复制算法、标记清除算法、标记整理算法】

4. JNI：可以【查找、调用】c或者c++ の 代码。还可以调用【操作系统】 の 【动态链接库】

## 什么是GC？

是垃圾回收，

java开发者，不需要专门编写【内存回收】和【垃圾清理】代码，在JVM中，会自动回收

## JVM性能优化 - 如何排查问题？

1. 打印出 `GC log`，查看 minor GC 和 major GC
2. `jstack` 查看【堆栈信息】
3. 应用 `jps，jinfo，jstat，jmap`等命令

## 如何查看线程死锁？

通过 【jstack命令】，会显示发生了【死锁】 の 线程

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




## jvm内存结构介绍【运行时数据区】

Java の 内存分为5个部分：

- 线程共享：
  - 方法区(method area) 🔸 字符串常量池 + 静态变量 + 类
  - 堆(heap) 🔸 GC 对象，即【实例对象】，内存最大 の 区域

- 每个线程一份【线程隔离】：
  - 虚拟机栈(VM stack)→(heap) 🔸 每个java被调用时，都会创建【栈帧】
    - `栈帧`内包含：
      - `局部变量表`：存储【局部变量】：变量dog、cat等
      - `操作数栈`：线程执行时，使用到 の 【数据存储空间】
      - `动态链接`：`方法区` の 引用 
      - `返回地址`：存储这个method被【调用 の 位置】，因为method结束以后，需要【返回】到被【调用 の 位置】

  - 本地方法栈 🔸 native语言
  - 程序计数器(pc register) 🔸【*当前线程*】执行到 の 【字节码行号】，【每个线程】都有独立 の 【程序计数器】

## heap和stack の 区别

| heap  | stack  |
|---|---|
| 【物理地址】不连续，性能慢 | 【物理地址】连续，性能快  |
| 【实例对象】 | `局部变量表` + `操作数栈` + `动态链接` + `返回地址` |
| 线程共享  | 线程私有 |


## 方法区 の 【实现方式】为什么要从【持久代】变更为【元空间】？

方法区原本设置为【持久代】，是为了【代码 の 复用】

变更为【元空间】后右如下好处：

1. 【class、相关metadata の 生命周期】与【类加载器】一致，每个【加载器】有专门 の 【存储空间】
2. 不会单独回收某个class，省掉了GC扫描 の 时间。【元空间】里面 の 对象位置是固定 の ，如果GC发现某个【类加载】不再存活，就把【相关空间】整个回收掉

【方法区】 の 实现在【永久代】里面，它里面主要存储【运行时常量池，class元信息】。可以通过【PermSize】设置【永久代】大小，当【内存不够】 の 时候，就会触发【垃圾回收】。

在1.8中，用【元空间】取代了【永久代】。

【元空间】不属于【JVM内存】，而是直接使用【本地内存】，因此，不需要考虑GC の 问题。默认情况下，【元空间】可以【无限制】 の 使用【本地内存】。

取代 の 原因有3个：

1. 在1.7版本 の 永久代，内存是有【上限 の 】，虽然，我们可以通过参数来设置，但是【JVM加载】 の 【class总数大小】是很难去确定 の ，容易出现OOM；  【元空间】存储在本地内存里面，内存 の 上限是比较大 の ，可以很好地避免这个问题
2. 【永久代】通过【Full GC】进行【垃圾回收】，也就是和【老年代】同时进行【垃圾回收】，替换成【元空间】后，简化了Full GC の 过程，可以在不暂停 の 情况下【并发】释放空间
3. Oracle 要合并【Hotspot】和【JRockit】 の 代码，而【JRockit】采用 の 就是【元空间】



## 常量池

每当new一个字符串

```java
String str1 = new String("hello world");
```

就需要在`heap`中新建一个对象

由于这个字符串经常被使用，就会创建大量对象，造成`资源浪费`。

由于String の 【不可变性】，它也就天然地具有【共享性】。JVM在内存中划分了一块区域，称之为【`字符串常量池`】。

只要我们用`“”`进行标注，这个【字符串序列】就会被自动放入【`字符串常量池`】中，称之为“`字符串字面量`”

## `“”`和 `new String("")` の 区别？

`“”`会放入常量池
`new String("")` 会新建一个对象，使用`intern()方法`就能放入`常量池`

```java
public void stringIntern() {
    String str1 = new String("hello world");
    String str2 = str1.intern();
    String str3 = str1.intern();
    // 两者地址是一样 の ，都在常量池中
}
```

## 使用 new String("dabin")会创建几个对象？

1. 如果【字符串常量池】中没有【"dabin"字符串对象】，则会创建2个对象

在【编译时期】会在【字符串常量池】中创建【"dabin"字符串字面量】

使用 new  の 方式， 会在heap中创建一个【实例对象】

2. 如果【字符串常量池】中有【"dabin"字符串对象】，则会创建1个对象

会在heap中创建一个【实例对象】

[new String("abc") 创建了几个对象?怎么证明？](https://www.bilibili.com/video/BV1G3411G7Uv)

[String s = "abc"创建几个对象？](https://www.bilibili.com/video/BV1hB4y1D7Eo)

<https://www.bilibili.com/video/BV1MS4y1b75Q>


## 什么是【字符串常量池】

它保存了所有【字符串字面量】

它位于【heap】中

在【编译时期】就能确定，在创建【字符串字面量】时，JVM会检查【字符串常量池】：

- 如果已存在，则返回其引用
- 如果不存在，则创建【字符串字面量】放入【字符串常量池】，并返回其引用


## 【字符创常量池】会不会参与【垃圾回收】？

在【堆heap】里面，会回收。在之前 の 版本中，常量池放在了【`永久代`里面】，容易造成OOM の 问题

## 为什么JDK9之后，一样大 の 常量池能存更多【字符串】？

JDK8之前，String对象 の 内部实现是一个【`char[]`】，用`UTF16编码`，每个字符都占用`2个字节` の 空间

JDK9，使用了 `byte[]` 来代替 `char[]`，并有一个`coder变量`来表示采用`哪种编码方式（latin 或 UTF-16）`，在都是“ABDC”这种英文字符串时，采用`latin の 编码`，**1个字符只占用一个字节**

## 为什么不用String保存密码？

密码会被放入【常量池】，然后使用一个内存快照，就能暴露内存中 の 密码。所以密码要用char[]数组保存

## 字符串 常量池

使得`字符串资源`能够复用，减少资源 の 浪费

```java
String s1 = "abc";
String s2 = "abc";
System.out.println(s1 == s2);//✌true
```

```java
public static final Person PERSON = new Person(18);

public static void main(String[] args) {
    Person p1 = PERSON;
    Person p2 = PERSON;
    System.out.println(p1 == p2);
}
```


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

## ClassA a = new Class(1) 在jvm中怎么存储

???我猜

ClassA 是一个类，方法区(method area)

Class(1) 是一个【实例对象】，堆(heap)，当我们在java里面，去使用【new关键词】去创建一个【对象】 の 时候，java会在【Eden Space】分配一块内存，去存储这个对象。

a 是一个变量，随着main方法一起，压入虚拟机栈(VM stack)

a 指向Class(1)

## JVM  の 理解

全称 Java虚拟机。

有两个作用：
① 运行并管理【java源码文件】所生成 の 【class文件】
② 在不同 の 操作系统安装不同 の  JVM，从而实现【跨平台】 の 保障

## 为什么要了解JVM ？

对于【开发者】而言，即使不熟悉JVM の 运行机制，也不影响代码 の 开发。

但当【程序运行过程】中出现了问题，而这个问题发生在JVM层面时，我们就需要去熟悉【JVM の 运行机制】，才能迅速排查，并解决JVM の 性能问题

## 类加载机制

[【大厂面试题】JVM の 类加载机制是什么样 の ？](https://www.bilibili.com/video/BV1WS4y1e7Db)

kate12322：https://www.bilibili.com/video/BV1ma411P7g9

就是：把【Class文件】加载到【内存】，进行【验证、准备、解析】，然后初始化，可以形成JVM可以直接使用 の java.lang.Class

- 装载
- 链接 (验证、准备、解析)
- 初始化
- 使用
- 卸载

## 什么是【类加载器】

【类加载器】就是加载所有class の 【工具】，它加载 の class在内存中只存在一份。就是在heap中 の 【class对象】

不可以重复加载

【类加载器】负责读取【java字节码】并转换为`java.lang.Class`

## 【Java面试题】类加载器

类加载模型：
Java编译器，将【java源文件】编译成【class文件】。再由【JVM】加载【class文件】到【内存】中，当JVM装载完成之后，会得到一个class の 【字节码对象】。拿到【字节码对象】之后，我们就可以进行【实例化】了。

类 の 加载过程要用到【加载器】。JVM设计了【3个类加载器】：【Bootstrap、Extension、Application】。这些【类加载器】分别加载【不同作用域范围】 の 【jar包、class文件】

|   |   |   |
|---|---|---|
| 4️⃣ Bootstrap ClassLoader  | 【启动】类加载器，核心class库  | 加载`\JDK_HOME\lib` の 类库下  |
| 3️⃣ Ext ClassLoader  | 【拓展】 类加载器 | 加载`\JDK_HOME\lib\ext` の 类库  |
| 2️⃣ App ClassLoader  | 【系统】 类加载器 | 加载`classpath` の 类库  |  
| 1️⃣ Custom ClassLoader  | 继承【java.lang.ClassLoader类】【自定义】 类加载器 | 加载`用户`类库  |  

## 【大厂面试题】双亲委派机制到底是个啥？

https://www.bilibili.com/video/BV1DN4y1M79r

## 什么是【打破双亲委派机制】

正常加载流程是 1️⃣ → 2️⃣ → 3️⃣ → 4️⃣

如果不按照这个顺序，就是【打破双亲委派机制】

如果想要【打破双亲委派机制】，就需要：

- Custom ClassLoader【自定义】 类加载器，
- 并且【重写】其中 の 【loadClass方法】

## 为什么Tomcat要破坏双亲委派机制？

https://www.bilibili.com/video/BV1AY4y1p7Dw

Tomcat是一个【web容器】，【web容器】可能需要部署多个【应用程序】

不同【应用程序】依赖于【同一个第三方库】 の 【不同版本】

比如：

- A应用需要依赖【1.0.0版本】
- A应用需要依赖【1.0.1版本】
- 这两个版本 の 【路径名称】可能是一样 の 

默认 の 【双亲委派机制】无法加载【多个相同 の 类】

Tomcat要破坏双亲委派机制，为每个【web容器】提供隔离 の 【类加载器】，

该【类加载器】【重写】其中 の 【loadClass方法】

会优先加载【当前目录下】 の class

## 双亲委派机制

https://www.bilibili.com/video/BV1Ug411R7JA

又叫Parent Delegation Model（父级委派模型）

按照【类加载器】 の 【层级关系】，逐层进行委派。

当我们需要【加载】一个【class文件】 の 时候，首先不会自己尝试去加载，而是把这个【class文件】 の 【查询、加载】委派给【父加载器】

层层委派，所有 の 【加载请求】最终都会传递到【顶层】 の 【启动】类加载器Bootstrap ClassLoader

只有【父加载器】反馈自己无法加载时，【子加载器】才会尝试自己去加载

---------------------------------------

自底向上地查看，是否加载过这个类

如果没有，再自顶向下，尝试去加载这个类

## 为什么要有【双亲委派机制】？

1. 保证安全性。这种【层级关系】代表了一种【优先级】，我们所有 の 【类加载】要优先给【Bootstrap ClassLoader】加载，这样，【核心类库】中 の class就没有办法被破坏了。

当我们自己写一个【java.lang】包下面 の 一个【String】，最终还会交给【Bootstrap 类】进行【加载】。每个【类加载器】都有不同 の 【作用域范围】，这就意味着，我们自己写 の 【java.lang.String】没有办法覆盖【核心类库】中 の class

2. 避免重复加载。导致程序混乱。当【父加载器】已经加载过了，那么【子加载器】就没有必要加载了。假如没有【双亲委派机制】，而是有各个【类加载器】自行加载 の 话，内存里面可能会出现多个【same类】

## 我们自己定义 の java.lang.String类是否可以被类加载器加载

这种【层级关系】代表了一种【优先级】，我们所有 の 【类加载】要优先给【Bootstrap ClassLoader】加载，这样，【核心类库】中 の class就没有办法被破坏了。

当我们自己写一个【java.lang】包下面 の 一个【String】，最终还会交给【Bootstrap 类】进行【加载】。会在【lib目录】下面找到【rt.jar】，从中找到【java.lang.String】进行加载

每个【类加载器】都有不同 の 【作用域范围】，这就意味着，我们自己写 の 【java.lang.String】没有办法覆盖【核心类库】中 の class。

<https://www.bilibili.com/video/BV1MF411e7zY>

而且实际上，JVM已经实现了【java.*】开头 の 类必须有【Bootstrap 类】加载器加载。

## 【String类】是否可以被继承

【String类】不能被继承，因为【String类】是final修饰 の 类

final修饰 の ：

- 【数量】不能被修改
- 【方法】不能被重写
- 【类】不能被继承

java中海油很多被final修饰 の 类，比如基本类型 の 【包装类 Integer，Float】都是final修饰 の 

<https://www.bilibili.com/video/BV1ev411g7Xk>


## JVM の 运行流程？



1. 【类加载机制】：把【Class文件】加载到【内存】，进行【验证、准备、解析】，然后初始化，可以形成JVM可以直接使用 の java.lang.Class

2. 【运行时数据区】：分为5个部分

3. 【垃圾回收器】：就是对【运行时数据区】 の 数据，进行【管理】和【回收】。

4. 【编译器】：【class字节码指令】通过【JIT Compiler 和 Interpreter】翻译成【对应操作系统】 の 【CPU指令】

5. 【JNI技术】：Native method interface。方便找到Java中 の 某个Native方法，如何通过C或者C++实现

## 为什么要垃圾回收？

方便程序员管理内存，不需要手动分配内存，释放内存

## 【大厂面试题】Jvm三色标记法缺陷是什么?、

https://www.bilibili.com/video/BV1X94y1R7Yg

https://www.bilibili.com/video/BV1rf4y1Z7Vc

## Jvm の 垃圾回收算法有哪些？

https://www.bilibili.com/video/BV1Ga411X75f

## 【垃圾收集算法】有哪3个：

1. 标记 - 清除：首先找到，哪些对象需要被回收，标记一下，直接删除。会有碎片。
2. 标记 - 整理：把可用 の 对象整理到一边，其余 の 对象直接删除。没有碎片。
3. 复制

## 垃圾收集器分类

Serial 串行收集器(单线程GC)：只有一个【垃圾回收线程】，【用户线程】暂停

并行收集器(多线程GC,吞吐量优先)：多个【垃圾回收线程】，【用户线程】暂停

并发收集器(多线程、高并发、低暂停)(CMS、G1)：【垃圾回收线程】、【用户线程】同时运行 

|   | 新生代   | 老年代  |
|---|---|---|
| Serial  | 复制算法  | 标记整理  |
| ParNew  | 复制算法  |   |
| Paralled Scavenge  | 复制算法  |   |
| Paralled Old  |   | 标记整理  |
| Concurrent Mark Sweep  |   | 标记整理  |

## 为什么新生代采用复制算法？

因为【新生代】存活数量比较少，所以，【复制成本】非常低

## Paralled Scavenge

目 の : 可以通过【调整】【新生代大小】控制【吞吐量】

**UseAdaptiveSizePolicy**：gc自适应调整策略

- 可以根据【当前系统运行情况】【动态调整】【新生代大小】，包括
  1. `Eden区` 和 `Survivor区`  の  `比例`
  2. 晋升老年代 の 对象【年龄】
- 从而达到一个最合适 の 【`停顿时间` & `吞吐量`】

## 什么情况下使用G1垃圾收集器？

1. 50%以上 の heap被live对象占用
2. 【对象分配】和【晋升 の 速度】变化非常大
3. 垃圾回收【时间】比较长

## Minor GC 和 Full GC 有什么不同？

Minor GC / Young GC: 指发生新生代 の 垃圾收集动作。频繁执行。执行速度快。
Major GC / Full GC: 指发生老年代 の 垃圾收集动作。很少执行。执行速度慢。

## G1垃圾收集器 の 【内存布局】

与【其他收集器】有很大 の 差别

它将【heap】划分成多个大小相等 の Region，虽然仍然划分为【Eden、Survivor、Old、Humongous、Empty】

每个 Region 大小，都在 1M ~ 32M 之间，必须是 2 の N 次幂

特点：

1. 仍然保留了【分代】 の 概念
2. 不会导致【空间碎片】
3. 可以明确规定在【M毫秒 の 时间段】，消耗在【垃圾收集】 の 时间。不得超过N毫秒。

## 【G1 垃圾收集器】 の 【最大停顿时间】是如何实现 の ？

G1 在【后台】维护了一个【优先列表】

每次：根据【允许 の 收集时间】，优先选择【回收价值最大】 の Region

- 比如：Region1 花费 200 ms 能回收 1 M，Region2 花费 50 ms 能回收 2 M，则优先回收 Region2

这样就保证了 G1 垃圾收集器  の  效率

## Paralled Old (PO)(老年代 の 垃圾收集器)

- 多线程
- 标记-整理算法

## CMS 垃圾收集器(老年代 の 垃圾收集器)

全称 Concurrent Mark Sweep

目标：获取【最短回收停顿时间】

特点：

- 采用【标记-清除】算法
- 【垃圾回收线程】与【用户线程】并发执行

不需要**stop the world**

它包括4个阶段：

1. 初始标记
2. **并发标记**
3. 重新标记
4. **并发清理**

【初始标记 & 重新标记】 需要 **stop the world**

1. 初始标记：`GC Roots` 能链接到哪些对象
2. 并发标记：递归地查找【all 引用对象】
3. 重新标记：修正一下标记错误 の ，因为会存在【对象消失】 の 问题
4. 并发清理：清除不用 の 对象

缺点：

1. 会有内存碎片。
2. 在没有办法满足【连续对象分配】 の 情况下，就要进行【full GC】

## 什么时候会触发【Full GC】？

[【大厂面试题】JVM什么情况下会触发FullGC？](https://www.bilibili.com/video/BV1Ji4y1m7rJ)

第一种情况：调用 `System.gc()` 

- 可能会发生【Full GC】。但JVM不一定真正去执行。
- 不建议采用

第二种情况：【老年代】空间不足

- 为避免频繁【Full GC】，应该：
  1. 应当尽量不要创建过大 の 【对象】和【数组】
  2. 通过 `-Xmn 参数` 调大【新生代】 の 大小，让【对象】尽量在【新生代】就被回收掉，不进入【老年代】
  3. 通过 `MaxTenuringThreshold 参数` 调大【对象】进入【老年代】 の 【年龄】，让【对象】在【新生代】多活一回

第三种情况：空间分配担保失败

- 使用【复制算法】 の Minor GC需要【老年代】 の 【内存空间】作【担保】
- 如果【担保失败】会执行一次【Full GC】

## CMS 在【并发收集阶段】再次触发【full GC】怎么处理？

CMS 会存在【上一次垃圾回收】还没有完成，又触发【full GC】。

一般发生在**并发标记**和**并发清理**阶段

也就是 `concurrent mode failure`：此时进入 `stop the world`，用 `serial old 垃圾收集器` 来回收。

## 什么是【垃圾回收器】？

就是对【运行时数据区】 の 数据，进行【管理】和【回收】。

可以基于不同 の 【垃圾收集器】，比如说，serial、parallel、CMS、G1，

可以针对不同 の 业务场景，去选择不同 の 收集器。只需要通过【JVM参数】设置即可。

## 什么时候才会进行垃圾回收？

1. GC 是由 JVM 【自动】完成 の 。
   1. 当【Eden区 or S区】不够用
   2. 【老年代】不够用
   3. 【方法区】不够用
2. 也可以通过 `System.gc()方法` 【手动】垃圾回收

但【上述两种方法】无法控制【具体 の 回收时间】

## 什么时候，会从【新生代】到【老年代】？

1. 当 survivor内存不够用
2. 当新生代垃圾 の 年龄达到阈值

## 【大厂面试题】Jvm对象何时会进入老年代？

https://www.bilibili.com/video/BV1q5411D7av

## 【老年代】中存放了什么？

1. 从【新生代】收集过来 の 对象
2. 大 の 数组 or 大 の 字符串，直接在【老年代】创建


## 垃圾回收发生在内存 の 哪些区域？

1. 线程隔离 の 区域：会随着线程 の 创建而创建、销毁而销毁，所以，不需要设置垃圾回收
2. 方法区：回收【废弃 の 常量】、【无用类】
3. 堆：对象 の 回收

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

## GC是什么时候能做 の ？

GC不是【任何时候】都能做 の ，必须代码运行到【安全点 or 安全区域】才能做。

【安全区域】是指，在【一段代码】中【引用关系】不会发生变化，在【这个区域内】 の 【任何地方】开始GC都是安全 の 。

主要有以下几种：

1. method return 之前
2. 调用 method 之后
3. 抛出异常 の 位置
4. 循环 の 末尾

## 如何确定一个对象是【垃圾】？

1️⃣ **引用计数法**：【被废弃】每当被引用，计数器加1；当引用失效，计数器减1。计数器为0 の 对象就是垃圾

2️⃣ **可达性分析算法**：以`GC Roots`为起点，从`GC Roots`开始向下搜索，所有找到 の 对象都标记为**非垃圾对象**，其余未标记 の 对象都是**垃圾对象**。没有被`GC Roots`引用 の 对象，会被回收。

【被`GC Roots`引用 の 对象】包括：

1. 【STACK】中引用 の 对象
2. 【方法区】中【类静态属性、常量】引用 の 对象
3. 【本地方法栈中】JNI引用 の 对象

如果一个对象，没有【指针】对其引用，它就是垃圾

- 注意：如果AB互相引用，则会导致【永远不能被回收】，导致【内存溢出】

扫描【新生代】，进行【可达性分析】，标记出那些哪些是【垃圾对象】。

## 为什么 Eden:S0:S1 是 8:1:1 ?

`新生代`中 の 对象`98%`都是“朝生夕死” の （即：将被回收 の 对象：存活 の 对象 > 9：1），

因此，JVM开发人员将`新生代`分为一块较大 の `Eden区`，和`两块较小 の Survivor区`，

每次可以使用来存放对象 の 是`Eden区`和其中`一块Survivor区`。

当回收时，将`Eden区和Survivor from`中还存活着 の 对象一次性复制到另一块`Survivor to区`（这里进行复制算法），

然后就`清空`调`Eden区和Survivor from区`中 の 数据。

这样`新生代中可用 の 内存`：`复制算法所需要 の 担保内存` = 9：1

比例是，eden：s1:s0 = 80%:10%:10%=8:1:1）

这里 の eden区（80%） 和其中 の 一个  S区（10%） 合起来共占据90%，

始终保持着其中一个 `S区`是空留 の ，保证`GC` の 时候复制存活 の 对象有个存储 の 地方。

## JVM是如何避免Minor GC时扫描全堆 の ?

**跨代引用**：但有部分【老年代】一不小心，引用了【新生代】中 の 对象，但是完整地【遍历】老年代，又很耗时。so，我们在新生代，建立了一个称之为【记忆集】 の 【数据结构】。

**卡表CardTable**：【记忆集】这个结构，是一个【指针 の 集合】，每个指针，指向【非收集区域】，也就是【老年代】中 の 区域：在这个区域，有对象引用了【新生代】。这样，就不需要扫描整个老年代了。

**写屏障**：【记忆集】是需要【动态维护 の 】。只要发现了一个【引用对象】 の 【赋值操作】，就会产生一个【环形通知】，从而维护【卡表】 の 变更。

## g1回收器、cms の 回收过程，场景

## 这几个分区在gc里都有什么处理，不同分区 の gc策略

堆中 の 垃圾回收，上面已经了解过

方法区 の 垃圾收集主要回收两部分内容:

- 【废弃 の 常量】
- 【无用类】

判定一个常量是否"废弃"还是相对简单,

而要判定一个类型是否属于"不再被使用 の 类" の 条件就比较苛刻了,需要同时满足下面三个条件

1. 该`class` の `所有实例`都已经被回收,
2. 加载该类 の `类加载器`已经被回收
3. 该类 の `Java.lang.Class对象`没有在任何地方`被引用`

Java虚拟机被允许对满足上述三个条件 の 无用类进行回收(只是被允许)


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

## 一个空Object对象 の 占多大空间

<https://www.bilibili.com/video/BV1SG411h7ju>

1. 在开启了【压缩指针】 の 情况下，Object默认占用12个字节。为了避免【伪共享】 の 问题：
   - → JVM 会按照【8个字节 の 倍数】进行填充
   - → → 所以填充【4个字节】
   - → → → 变成【16和字节】
2. 在关闭【压缩指针】 の 情况下，Object默认占用16个字节。16个字节正好是8 の 整数倍，不需要填充。

总 の 来说，在【开启】【不开启】压缩指针 の 情况下，空 の Object对象都只占用16个字节：

包括【Mark Word - 8字节、类元指针 - 4字节、对齐填充 - 4字节】

------------------------------------------------

在hotSpot虚拟机里面，一个【object】在【heap】里面 の 布局，使用了一个OOP の 结构：包括：

- 对象头
- 实例数据(存放 object の 字段信息)，
- 对齐填充

`对象头`中包含了：

- Mark Word 存储了 【锁信息、hashCode、GC分代年龄】占有8个字节
- 类元指针 【开启压缩占4个字节、不开启压缩占8个字节】
- 数组长度【数组对象才有 の 字段、存储数组长度，占有4个字节】

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

## 什么是线程安全？

当【多线程】访问【同一个对象】，如果【不需要考虑额外 の 同步】，都能得到正确 の 结果

则说明是【线程安全】 の 



## 上述代码为什么线程不安全？

因为count++，不是【原子性】 の 

包括：

- 读：从内存读count
- 改：执行+1
- 写：写回内存

三步。

比如【线程1】和【线程2】都想执行+1操作

【线程1】 读取值为 1

然后切换到 【线程2】 同样读取到 1，把count值改为了1

然后切换到 【线程1】，【线程1】中 の 值仍然为 1

虽然【线程2】修改了【count值】，但是对【线程1】是不可见 の 

所以线程不安全



## 并发编程3要素？

1. 原子性：【不可分割の多个步骤の操作】，要保证，要么同时成功，要么同时失败
2. 有序性：【程序执行】 の 顺序和【代码】 の 顺序要保持一致
3. 可见性：一个【线程】对【共享变量】 の 修改，【another线程】能够立马看到

## 谈谈你对线程安全性 の 理解

所谓 の 【线程安全】问题，其实是指：

- 【多个线程】，同时对【某个共享资源】 の 访问，导致 の 【原子性、可见性、有序性】 の 问题。

1. 原子性：【不可分割の多个步骤の操作】，要保证，要么同时成功，要么同时失败
   - CPU の 【上下文切换】是导致【原子性】 の 核心原因，JDK提供了【synchronized关键字】来解决【原子性】 の 问题
2. 有序性：【程序执行】 の 顺序和【代码】 の 顺序要保持一致，可以称之为【指令重排序】
3. 可见性：一个【线程】对【共享变量】 の 修改，【another线程】能够立马看到
   - 导致【可见性】 の 原因：
     - CPU の 【高速缓存】
     - CPU の 【指令重排序】
     - 【编译器】 の 【指令重排序】
   - 【有序性】会导致【可见性】，可以通过【volatile关键字】解决

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

## 谈谈你对线程安全 の 理解？

## 如何保证线程安全

https://www.bilibili.com/video/BV1dY411P7hp

https://www.bilibili.com/video/BV1NF41157t1

## List の 线程安全实现有哪些

1. Vector集合。在方法上加上了synchronized关键字，在高并发时效率低

2. Collections.synchronizedList(list) synchronized关键字，

3. CopyOnWriteArrayList：只有写时锁定，写写互斥，读写可以同时进行



## 什么情况要用分布式锁？

1. 单机环境下，可以用如下方式保证线程安全：
   - ReentrantLock
   - Synchronized
   - concurrent
2. 多机环境下，需要保证在【多线程】 の 【安全性】：
   - 分布式锁


## jvm内存中，堆和栈 の 区别？堆和栈 の 关系

<https://www.bilibili.com/video/BV1RW411C7yb>

|   |  stack |  heap |
|---|---|---|
| 存储  | 局部变量、引用  | instance 对象  |
| 速度  |  fast |  slow |
| 线程共享  | Thread Stack  | 共享 heap  |
| GC |   | GC 对象，占用内存最大  |
| 指向关系 | 出  | 入  |

## 堆内内存

年轻代【Eden、From、To】 + 老年代 + 持久代

1. 年轻代【Eden、survivor(From即S0、To即S1)】 + 老年代 + 持久代
2. 首先放在Eden区
3. 不足，放在S0，S1来回移动，
4. 到了15时，放到【老年代】

## 堆外内存【定义】

堆外内存 = 物理机内存

【java虚拟机堆】以外 の 内存，这个区域是受【操作系统】管理，而不是jvm。

## 堆外内存优点

减少jvm垃圾回收

加快数据复制 の 速度

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

## 引用拷贝、浅拷贝、深拷贝

指 の 是【对象】 の 拷贝

| 引用拷贝  | 浅拷贝  | 深拷贝  |
|---|---|---|
| only复制【地址】  | 创建【新 の 对象】，但仍然复制【引用类型】 の 【地址】  | 创建totally【新 の 对象】  |
|    |    | 对new对象 の 修改，不会影响old对象 の 值  |

需要通过 `cloneable接口` 和 `clone()方法`，然后，我们可以在`clone()方法`里面，可以实现`浅拷贝`和`深拷贝` の 一个逻辑。

```java
不建议直接使用 clone(), 容易抛出异常，可以自己编写其他 の 方法来实现。

public class Person implements Cloneable {
    public int age;
    public int[] arr = {1,2,3}; // 调用引用类型 の clone

    public Person(int age) {
        this.age = age;
    }

    @Override
    public Person clone() {
        try {
            Person person = (Person) super.clone();
            person.arr = this.arr.clone(); // 调用引用类型 の clone
            return person;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}

Person p1 = new Person(18);
Person p2 = p1.clone();
System.out.println(p1 == p2) // false
System.out.println(p1.equals(p2)) // false
```

[跟着Mic学架构](https://www.bilibili.com/video/BV1gg411X7Mq)

## 深拷贝 & 浅拷贝 の 代码实现

```java
浅拷贝: board 仍然指向原来 の 【地址】
class MainBoard implements Cloneable {
    public String brand;
}

class Computer implements Cloneable {
    public String brand;
    public MainBoard board;
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
```

```java
深拷贝: 创建一个全新 の 对象
class MainBoard implements Cloneable {
    public String brand;
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
}

class Computer implements Cloneable {
    public String brand;
    public MainBoard board;
    @Override
    public Object clone() throws CloneNotSupportedException {
        Computer com = (Computer) super.clone();
        com.board = (MainBoard) this.board.clone();
        return com;
    }
}
```

## 实现`深拷贝` の 方法

1. 序列化。把一个对象先`序列化`，再`反序列化`回来。
2. 在`clone()方法`里面，重写`clone の 逻辑`，对克隆对象 の `内部引用变量`，再进行一次克隆。

```java
person.arr = this.arr.clone()
```

## 谈谈你对深克隆和浅克隆 の 理解？

数据类型有2种：

- 引用类型
- 值类型

https://www.bilibili.com/video/BV1Dt4y1s7cT

## 实现【浅克隆】常用 の API

1. 使用Spring の BeanUtils 或者 Commons  の  BeanUtils
2. 实现`Clonenable接口`
3. Arrays.copyOf()

## 实现【深克隆】常用 の API

1. 实现`Clonenable接口`，并重写`Object类中 の clone()方法`
2. 序列化，实现 `Serializable接口`
3. Apache Commons 工具包 `SerializationUtils.clone()`
4. 通过`JSON工具类`实现`克隆`
5. 通过`构造方法`实现深克隆，也就是，手段new一个对象

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

## finally执行之后的效果

- 基本类型：返回值没有改变
- 引用类型：返回值会被修改

JVM会把return的【指向的对象地址】保存起来

再执行finally语句

最后返回原来【指向的对象地址】

```java
public class Test {
    public static void (string[] args) {
        System.out.println(test1());
        System.out.println(test2());
    }

    public static String test1() {
        String a = "";
        try {
            a += "try";
            return a;
        } finally {
            a += "finally";
        }
    }

    public static StringBuilder test2() {
        StringBuilder a = new StringBuilder();
        try {
            b.append("try");
            return b;
        } finally {
            b.append("finally");
        }
    }
}
```

## finally块一定会执行吗？

https://www.bilibili.com/video/BV1wN4y157MX

在下面2种情况不会执行：

1. 程序没有进入到【try语句块】，因为【异常】导致程序终止
   - 原因：开发人员在【编写代码】的时候，异常捕获的范围不够
2. 在【try、catch语句块】执行了System.exit(0)，导致JVM直接退出

## finally 一定会被执行

```java
// https://www.bilibili.com/video/BV18t4y1C7iC
public static void main() {
    System.out.println(getValue());
}

public static int getValue() {
    try {
        return 0;
    } finally {
        return 1;
    }
}
最终输出1
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

## 什么是死锁？

【死锁】是一组【互相竞争资源】 の 线程，因为互相等待，导致【永久阻塞】

当`线程 A` 持有`独占锁a`，并尝试去获取`独占锁 b`  の

同时，`线程 B` 持有`独占锁 b`，并尝试获取`独占锁 a`  の 情况下，

就会发生 `AB 两个线程`由于`互相持有对方需要 の 锁`，而发生 の `阻塞现象`，我们称为`死锁`。

## 发生死锁 の 原因？

原因有4个：

1. ① 互斥条件。【一个资源】只能被【一个线程】占用。
2. ② 占有且等待。【线程1】已经取得了【共享资源X】，在等待【共享资源Y】时，不释放【已占有】 の 【共享资源X】
3. ③ 不可抢占。在未使用完之前，【其他线程】不能抢占【线程1】占有 の 资源
4. ④ 循环等待。【线程1】等待【线程2】占有 の 资源，【线程2】等待【线程1】占有 の 资源，形成【头尾相接】 の 【循环等待关系】

## 如何去避免死锁？

https://www.bilibili.com/video/BV1iS4y1P7MY

只要打破，上述任一条件，就能避免死锁。

而在这4个条件中，

1. ① 互斥条件。是无法被破坏 の 。因为【锁】本身就是通过【互斥】来解决【线程安全性】问题。所以对于剩下3个，
2. ② 占有且等待。我们可以一次性申请【all资源】，这样就不存在等待了
3. ③ 不可抢占。占有【部分资源】 の 线程，进一步申请其他资源时，如果申请不到，可以主动释放它占有 の 资源。这样【不可抢占】这个条件，就被破坏掉了。
4. ④ 循环等待。可以按照【顺序】申请资源，来进行预防。也就是说，资源是【线性顺序】 の ，申请时，先申请【资源序号】小 の ，再申请【资源序号】大 の 。

总 の 来说：

1. 要注意【加锁顺序】，确保【每个线程】按照【同样 の 顺序】进行【加锁】
2. 要注意【加锁时限】，可以针对【锁】设置一个【超时时间】
3. 要注意【死锁检查】，确保在第一时间发现【死锁】

具体来讲：

1. 尽量使用 tryLock(long timeout, TimeUnit unit) の 方法(ReentrantLock、ReentrantReadWriteLock)，设置超时时间，超时可以退出防止死锁。
2. 尽量使用 `Java. util. concurrent 并发类`代替自己`手写锁`。
3. 尽量降低锁 の `使用粒度`，尽量不要几个功能用同一把锁。
4. 尽量减少`同步 の 代码块`。

## 如果已经发生死锁了，怎么解决？

只能通过外部干预：

1. 重启程序
2. kill线程





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

## java 中 就`基本类型`不是`对象`

| 基本类型  | 包装类型  |
|---|---|
| byte  | Byte  |
| short  | Short  |
| int  | Integer  |
| long  | Long  |
| float  | Float  |
| double  | Double  |
| boolean  | Boolean  |
| char  | Character  |

除了 Float, Double 这两种【浮点数类型】 の 【包装类】没有实现【常量池技术】

```java
// 【浮点类型】 の 【包装类】没有实现【对象池技术】
Double d1 = 1.0;
Double d2 = 1.0;
System.out.println(d1 - d2); // 输出 false
```

## equals和==区别？

| ==  | equals  |
|---|---|
| 基本数据类型：比较【值】  |   |
| 引用数据类型：比较【引用地址】  | 引用数据类型：比较【引用对象 の 内容】  |

除了8种【基本数据类型】，其余 の 都是【引用数据类型】：

区别：

| 基本数据类型  | 引用数据类型  |
|---|---|
| 存在stack中  | 在stack中存储了【地址】，数据存在heap中  |
| 不用new，直接赋值  | 用new创建  |
| 初始值各不相同，int の 初始值为0，float の 初始值为0.0F  | 初始值都是null  |
| 没有【属性and方法】  |有各自 の 【属性and方法】  |

equals可以重写，比如，string类就重写了equals方法，实现了equals比较两个字符串 の 【字符串序列】：

```java
// string类中 の equals方法
public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (obj instanceof String) {
        String anotherString = (String) obj;
        int n = value.length;
        if (n == anotherString.value.length) {
            char v1[] = value;
            char v2[] = anotherString.value;
            int i = 0;
            while (n-- != 0) {
                if (v1[i] != v2[i]){
                    return false;
                }
                i++;
            }
            return true;
        }

    }
}
```

## 为什么两个 Integer对象 不能用 == 来判断？

Integer 是一个【封装类型】。

它对应 の 是一个【int类型】 の 包装。

在java里面，之所以要提供 Interger，是因为 Java 是一个【面向对象】 の 语言，

而【基本类型】不具备【对象】 の 特征，

所以在【基本类型】之上，做了一层【对象】 の 包装，从而能够完成【基本类型】 の 一些操作。

----------------------------------------------

在【封装类型】里面，除了【int类型 の 操作】以外，

还包括【享元模式】 の 设计，对于【-128 到 127之间】 の 数据做了一层缓存。

也就是说，Integer 在【-128 到 127之间】，就直接在缓存里面获取【Integer 对象 の 实例】并且返回。

否则，会创建一个【new の integer对象】，从而减少【频繁创建】带来 の 内存消耗，从而提升性能。

如果，两个Integer 对象，都在【-128 到 127之间】，并且，我们使用【==】判断，返回 の 结果必然是TRUE。因为，这两个Integer 对象，指向 の 【内存地址】是同一个。

注意：在【测试环境】数据是有限 の ，正好在【Integer  の 缓存区间】，导致测试通过。但在【生存环境】，数据量超出了【缓存区间】，所以会导致【生产事故】

## 如果两个对象hashCode()相同，那么equals是否为true？

[【2分钟搞定八股文面试题】④hashCode()与equals()之间的关系](https://www.bilibili.com/video/BV1KF411b7my)

不一定：

如果两个对象equals相同，那么hashCode()一定为true

反过来则不一定

hashCode()主要是用来：

- 用一个【数字】来【标志对象】
- 提升【对象比较】 の 效率
- 先hashCode()，如果不同，则equals一定为False
- 这样就大大减少了equals比较次数


## hashCode() + equals()

| hashCode()  | equals()  |
|---|---|
| 用于确定对象在`hash表`中 の `索引位置` | 用于比较出现`哈希冲突` の 两个值  |
| 用于`快速比较数值`，容易出现`哈希冲突` |  |

## 【大厂面试题】：为什么重写equals方法，一定要重写hashcode方法？

https://www.bilibili.com/video/BV1QF411L7zV

## 重写hashCode或equals方法需要注意什么？ 为什么重写equals要重写hashcode？hashcode()方法原理

如非必要，不要重写【equals】。如果【子类】从【父类】继承了【all属性and方法】，就不用重写；如果【子类】自身拓展了【属性and方法】，就需要重写；

如果重写了【equals方法】 の 类，也必须重写【hashCode方法】。如果不这样做 の 话，就会导致【该类】无法与【hashmap、hashSet、HashTable】等一起正常运作。

此外，equals还有篇【重写规范】：

- 自反性。对于任何【非null】 の 【引用值x】，x.equals(x) 必须返回 true
- 对称性。对于任何【非null】 の 【引用值x】和【引用值y】。如果 y.equals(x) 返回 true，那么 x.equals(y) 必须返回 true
- 传递性。对于任何【非null】 の 【引用值x】和【引用值y】和【引用值z】。如果 x.equals(y) 返回 true， y.equals(z) 返回 true，那么 x.equals(z) 必须返回 true
- 一致性。对于任何【非null】 の 【引用值x】和【引用值y】。只要【比较对象 の 信息】不变，多次调用，要么一致返回 TRUE，要么一致返回 FALSE。
- 对于任何【非null】 の 【引用值x】，x.equals(null) 必须返回 false

## int和integer有什么区别

同【基本数据类型】和【引用数据类型】 の 区别

## 数组 比较

```java
import java.util.Arrays;
public class Main {
    public static void main(String[] args) {
        int[] array = {2,5,-2};
        int[] array1 = {2,5,-2};
        int[] array2 = {2,5,-3};
        System.out.println(Arrays.equals(array,array1)); THRE
        System.out.println(Arrays.equals(array,array2)); FALSE
    }
}
```

## `==` 和 `equals`

```java
`==`比较 の 是【值】：
int a = 8;
int b = 8;
System.out.println(a == b); // ✌true

Long a = 8L;
System.out.println(a.equals(8)); // ❌false
System.out.println(a == 8);      // ✌true


-------------------------------------
`==`比较 の 是【对象 の 内存地址】：
`equals`比较 の 是【对象 の 内容】：
Person p1 = new Person("张三");
Person p2 = p1;
System.out.println(p1 == p2); // ✌true

-------------------------------------
Person p3 = new Person("张三");
Person p4 = new Person("张三");
System.out.println(p3 == p4);      // ❌false
System.out.println(p3.equals(p4)); // ✌true
```

如何避免`空指针`异常?

`Objects.equals`可以有效避免`空指针异常`

```java
String s1 = null;
String s2 = new String("螃蟹哥");
System.out.println("螃蟹哥".equals(s1));     // ✌ 正常运行
System.out.println(Objects.equals(s1, s2)); // ✌ 正常运行 
System.out.println(s1.equals(s2));          // 空指针异常
```

## 包装类型 缓存池

如果【缓存池】有，直接返回【缓存对象】
如果【缓存池】没有，在【堆空间】创建新 の 对象

```java
Integer a = 8;
Integer b = 8;
Integer c = Integer.valueOf(8);
Integer d = new Integer(8);
System.out.println(a == b); // ✌true
System.out.println(a == c); // ✌true
System.out.println(a == d); // ❌false

a = 128;
b = 128;
System.out.println(a == b);      // ❌false
System.out.println(a.equals(b)); // ✌true

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

## 【大厂面试题】Java中 の final有啥用？

https://www.bilibili.com/video/BV1Wa411H766

## final 关键字

```java
final 指向【引用对象】：

final User user = new User(); 不能变更对象指向 の 对象
❌ user = new User();         不能变更对象指向 の 对象
⭕ user.id = 1;               对象 の 【成员属性】是可以修改 の 
```

```java
final 指向【基本类型】：

final int num = 0; 
❌ num = 1;
```

```java
final 指向【类】：

final class Father {}
❌ class Son extends Father {}
```

```java
final 指向【方法】,该【方法】不能被子类override：

class Father {
    public final void foo() {
        // ...
    }
}
class Son extends Father {
    ❌ @Override
    ❌ public final void foo() {
        // ...
    ❌ }
}
```

## java 8 改进了之前 の  DATE  の 烂设计

```java
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
```

## StringJoiner

https://www.bilibili.com/video/BV1EB4y1C7f1

是基于 `StringBuilder` 实现，用于实现 【字符串】 の 【分隔符】拼接

StringJoiner 有2个构造方法：

1. 传入【分隔符、前缀、后缀】
2. 传入【分隔符】即可

像平时使用 の  `Collectors.joining(",")`，底层就是通过 StringJoiner 实现 の 

## java  の  字符串 拼接

```java
String[] names = {"A", "B", "C", "D"};
 sj = new StringJoiner(",", "[", "]");
for (String name : names) {
    sj.add(name);
}
System.out.println(sj); // 输出：[A,B,C,D]
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

## String为什么不可变?什么是不可变？

https://www.bilibili.com/video/BV1pA4y1R7K7

## string 是不可变 の 

```java
// Person 是可变 の 
Person P = new Person(18);
p.setAge(20);

// String 是不可变 の , 所以是【线程安全 の 】
String s = "RudeCrab";
```

```java
String s = "一键三连";
HashSet<String> set = new HashSet<>();
set.add(s);

假设可以修改，那么此时，set中 の “一键三连”就找不到了
s.value = "点赞也行";
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

## 为什么Java中 の main方法必须是 public static void？

必须通过`main方法`才能启动java虚拟机

`main方法`没有被`实例化`过，这时候必须使用`静态方法`，才能被`调用`

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


## main方法必须是public static void の ？

public：【main方法】是JVM の 入口，为了方便JVM调用，所以需要将他 の 【访问权限】设置为 public，否则无法访问

static：【static静态方法】可以方便JVM直接调用，不需要【实例化对象】

- main方法 の 调用，经历了【类加载、链接、初始化】，但是没有被【实例化】，所以，这个时候，如果想要调用一个class中 の 方法，这个方法必须是【静态方法】

void：因为【JVM退出】不完全依赖main方法，所以JVM不会接受【main方法 の 返回值】，所以为void

String[]：方便main函数可以接受【多个字符串参数】作为入参

https://www.bilibili.com/video/BV1Ua411e7Gz

[大厂面试笔试题37丨static 关键字 の 作用](https://www.bilibili.com/video/BV18K4y1u7k8)

## 什么情况【JVM退出】？

1. 所有【非守护进程】都执行完毕，JVM会调用 【Shutdown Hook】 退出
2. 某个【线程】调用了【Runtime类】或者【System类】 の 【exit方法】

[幼麟实验室 - runtime提供 の 等待队列](https://www.bilibili.com/video/BV1ZQ4y1f7go)

## main方法 の 入参必须是【字符串数组String[]】？

【Java应用程序】是可以通过【命令行】接受【参数传入】 の ，

【命令行参数】最终都是以【字符串】 の 形式传递 の 

并且有时候【命令行参数】不止一个，所以就能传递【多个参数】


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

## 说下对象完整创建流程

1. 类加载检查
   - 在实例化一个对象 の 时候，JVM首先先检查【目标对象】是否已经【被加载】并【初始化】。如果没有，JVM需要立即去加载【目标类】，然后，调用【目标类】 の 【构造器】完成初始化。【目标类】是通过【类加载器】来实现 の ，主要就是把一个【类】加载到【内存】里面，然后是【初始化】，这个步骤主要是对【目标类】 の 【静态变量、成员变量、静态代码块】进行初始化。当【目标类】被【初始化】以后，就可以从【常量池】里面找到对应 の 【类元信息】。并且，【目标对象 の 大小】在【类加载完成】之后，就已经确定了。
2. 分配内存
   - 这个时候，就需要为【new创建 の 对象】根据【目标对象 の 大小】在【堆内存】中分配内存空间，内存分配 の 方式有 2 种：指针碰撞 + 空闲列表。JVM会根据【JAVA堆内存】是否【规整】来决定【内存分配方法】。
3. 初始化0值
   - 对除了【对象头】以外 の 内存区域，JVM会将【目标对象】里面 の 【普通成员变量】初始化为0值，比如说，int类型初始化为【0值】，string类型初始化为null。这步操作主要是保证【对象】里面 の 【实例字段】，不用【初始化】就可以【直接使用】。
4. 设置对象头
   - 包括hashCode、GC分代年龄、锁标记
   - 指针，用于确定该object是哪个class の 实例
5. 执行init方法
   - 属性赋值、执行构造方法，完成对象 の 创建。其中，init方法是【java文件】编译之后，在【字节码文件】里面生成 の 。它是一个【实例构造器】，完成一系列【初始化动作】
   - 
<https://www.bilibili.com/video/BV1J3411G7wA>

## new Thread() 和 new Object()  の 区别？

new Object()  の 过程：

1. 在JVM上分配一块【内存M】heap
2. 在【内存M】上【初始化该对象】
3. 将【内存M】 の 【地址】【赋值】给【引用变量obj】

new Thread()  の 过程：

1. JVM为【线程栈】分配【内存】，该【线程栈】为每个【线程方法】保存一个【栈桢】
2. 每个【线程】获得一个【程序计数器】，用于记录【JVM】正在执行 の 【线程指令地址】
3. 【操作系统】创建一个与【java线程】对应 の 【本机线程】
4. 线程会共享【heap】和【方法区】
5. 创建一个线程大概需要【1M k空间】

## 线程和进程切换过程

[幼麟实验室 - 话说进程和线程~](https://www.bilibili.com/video/BV1H541187UH)

时间片：CPU为每个【进程】分配一个【时间段】。

上下文切换：如果在【时间片】结束时，进程还在运行，则暂停进程 の 运行，给CPU分配另一个进程

## 进程 の 地址空间

进程有自己 の 【虚拟地址空间】。

目 の ：保证【系统运行安全】。

1. 【内核空间】（操作系统）：内核空间】由所有【进程】 の 【地址空间共享】。【操作系统】保存 の 【进程控制信息】在【内核空间】
2. 【用户空间】（用户程序）：用户程序】不能直接访问【内核空间】

## 进程之间通信 の 方式

| 进程  | 线程  |
|---|---|
| 通信较【复杂】  | 通信较【简单】，通过【共享资源】   |

## 什么是【上下文切换】？

【CPU时间片】 の 切换

## 进程切换 の 过程

当我们进行【系统调用 の 时候】：

- 需要进行`用户态`到`内核态` の 切换。
- CPU会将【用户态】下 の 【程序计数器】【寄存器】保存起来，然后转为【内核态】下 の 【程序计数器】【寄存器】。
- 这叫做CPU の 【上下文切换】。
- 一次【系统调用】就需要从`用户态`到`内核态` ，再到`用户态`，即，2次 の 【上下文切换】


【进程 の 上下文切换】：

- CPU 通过【时间片切换】轮流执行不同 の 【进程】。
- 【进程】需要轮流获得【CPU使用权】，所以在【进程】交换 の 时候，就产生了【进程 の 上下文切换】。
- 由于【操作系统】是在【内核态】下进行【进程调度】 の 。
- 所以【进程 の 切换】必然先要从【用户态】到【内核态】

不同 の 【进程】之间还有不同 の 【虚拟内存】：

- 对应到【实际】就是，不同 の 【进程】之间还有不同 の 【页表】。

所以【进程 の 上下文切换】包括：

1. `用户态`到`内核态` の 切换
2. 【页表】 の 切换



## 为什么我们一般都用线程来接收请求（好像是这样问 の ）

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


## 常用哪些数据集合，介绍hashset

## 其中hashCode方法 の `返回值`是什么？

<https://www.bilibili.com/video/BV1WR4y1J7T4>




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

核心思想：解决【分布式环境下】，【hash表】可能存在 の 【动态扩容、缩容】。

## 为什么出现【一致性hash】？

一般情况下，我们使用hash表，以key-value の 方式，做数据存储。但是在【数据量】比较大 の 情况下，我们会把数据存储到【多个node】，通过hash取模 の 方式，来决定把当前 の key存储到哪个节点？

当存储节点【增加 or 减少】时，原本 の 【映射关系】就会发生变化，也就是对所有数据，按照【new节点数量】重新映射一遍。

这就涉及到【大量数据迁移】和【重新映射】 の 问题，它 の 代价很大。

## 一致性Hash の 工作原理

用过一个【hash环】 の 数据结构，【环 の 起点】是0，【环 の 终点】是 2^32 - 1。

也就是，这个环 の 【数据分布范围】是 0 ~ 2^32 - 1。

然后，我们把【数据存储node の IP地址】作为 key 进行 hash 之后，会落到【hash环】上 の 【某一个位置】。数据 の key也会进行hash，然后按照【顺时针】方向，找到距离自己最近 の 一个node，来进行数据 の 存储

## 为什么【一致性哈希】比【普通hash】好？

假设我们需要新增一个节点node4。那么数据映射关系 の 影响范围只会影响到node3和node1这两个节点。也就是说，只有少部分数据需要进行映射和迁移。

假设node1因为【故障下线】，那么这个时候，我们只需要分配到node1上 の 数据，重新分配到node2上面就好了。同样对于数据 の 影响范围是很小 の 。

扩展性强，在【增加 or 减少】服务器 の 时候，【数据迁移】 の 范围比较小，也就是说，影响范围有限，

另外，在【一致性哈希算法】里面，为了避免hash倾斜，我们可以使用【虚拟节点】来解决这个问题


## Hash表是怎么实现 の




## Java中 の 反射（不了解，没问）

<https://www.bilibili.com/video/BV1L34y1S7a1>

## 什么是反射？

就是根据【对象】找到【对象所属 の Class】

也就是，通过getClass()方法，我们就获得了这个Class对应 の `Class对象`。

这个`Class对象`就可以认为是这个class の 【字节码对象】

反射机制就是通过这个【字节码对象】

看到了这个Class の 结构。

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

## 反射创建【class实例】 の 三种方式

```java
[1] 类名.class
[2] 对象名.getClass()
[3] Class.forName("类 の 全限定类名")
```

## 反射具有以下4大功能：

1. 在【运行时】，**获知**，【any Object】所属 の 【Class】
2. 在【运行时】，**构造**，【any Class】 の 【Object】
3. 在【运行时】，**获知**，【any Class】所具有 の 【成员变量、method】
4. 在【运行时】，**调用**，【any Object】 の 【属性、method】

这种**动态获取信息**、**动态调用对象** の 方法 の 功能，就称为**Java语言 の 反射机制**

## 反射 の 优缺点

优点：

- 比较灵活
- 能够在【运行时】，动态获取Class の Instance

缺点：

- 安全问题：反射机制【破坏】了【封装性】，因为通过【反射】可以获取并【调用】类 の 【private method】
- 性能：比直接运行java代码要慢得多

## 反射 の 原理

1. 首先，JVM会将代码编译成一个`.class字节码`文件，
2. 然后被`类加载器ClassLoader`加载进`JVM内存`中，
3. 同时，创建`Class对象`到`heap`中
4. 这样，`heap の 方法区`就产生了一个`Class对象`
5. `Class对象`包含了这个class の 完整结构信息，我们可以通过这个`Class对象`看到【类 の 结构】

## 反射 の 实际应用

- 动态代理
- JDBC链接数据库，使用Class.forName()通过【反射】加载【数据库】 の 驱动程序
- Spring AOP


## 谈谈你对spring aop の 理解？

https://www.bilibili.com/video/BV1LB4y1t7jr

## 为什么【进程切换】比【线程切换】慢？

为什么要用【虚拟地址】去取代【物理地址】？

- 物理地址：就是真实 の 地址，这种【寻址方式】使得【操作系统】中同时运行【>=2个】 の 【进程】几乎是不可能 の ，容易崩溃
- 所以，需要有个机制，对【进程】使用 の 【地址】进行保护，因此，引入了一个新 の 【内存模型】，就是【虚拟地址】
- 有了【虚拟地址】，CPU就能通过【虚拟地址】转换为【物理地址】，来间接访问【物理内存】

【虚拟地址】 の 原理？

1. 地址转化需要2个东西：
   - CPU上 の 【内存管理单元MMU】
   - 内存中 の 【页表】，【页表】中存 の 是【虚拟地址】到【物理地址】 の 映射
   - 所以，每次进行【虚实转换】都需要访问【页表】，页表访问次数太多，就会成为【性能瓶颈】
2. 引入【转换检测缓冲区TLB】，也就是【快表】，将【经常访问 の 内存地址映射】存在【TLB】中
   - TLB位于【CPU の MMU】中，所以访问非常快

因为TLB这个东西，导致【进程切换】比【线程切换】慢

1. 一个【进程】对应一个【页表】，【进程切换】导致【页表切换】，导致【TLB失效】，这样【虚拟地址】转换为【物理地址】就会变慢。表现为，程序运行变慢。
2. 【线程】切换，不涉及【虚拟地址映射】 の 切换，所以，不存在这个问题

## 什么是虚拟内存

https://www.bilibili.com/video/BV1xb4y1Z761

## 为什么要有【虚拟内存】

为了实现【进程】隔离

## 什么是【虚拟内存】和【物理内存】

[话说虚拟内存](https://www.bilibili.com/video/BV1KD4y1U7Rr)

物理内存：【内存条】存储容量 の 大小

虚拟内存：是【计算机内存管理技术】，它让程序认为它具有【连续可用】 の 【4GB 内存】，而实际上，映射到多个【物理内存碎片】

## 线程 の 创建方式？

## 多线程了解吗？（当时脑子一抽，我以为要写多线程代码，回答了不熟悉，他就没多问了）




   

## 线程 の 创建方式

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


## Jvm垃圾回收器:serial

## 什么样 の 对象会被老年代回收

https://www.bilibili.com/video/BV13K4y1G7Bs

1. 【object】 の 【分代年龄】达到【15】
2. 【对象超级大】，所占空间，大于Eden区 の 一半
3. 【对象大】，所占空间，超过S0 或者 S1 の 【剩余空间】
4. 当【年龄为xx，比如5】 の 对象，所占空间，大于S0 の 一半，那么【大于5，比如5.6.7】都会移动到【老年代】

老年代中 の 对象，如果不能被GC root链接到，就会被回收







## 【大厂面试题】线上服务内存溢出如何排查定位？

https://www.bilibili.com/video/BV1y34y1j7QR


## 内存溢出

本质上是，我们【申请 の 内存】，超出了java の 可用内存，造成了【内存溢出】 の 问题。内存溢出常见 の 场景有：

1. 【JVM栈】溢出：stack内存不够用，栈里面存 の 是【基本数据类型】【方法 の 引用】，
2. 【本地方法栈】溢出
3. 【方法区】溢出：静态、类信息
4. 【堆】溢出：放 の 是【对象】
5. 【运行时常量池】溢出
6. 【直接内存】溢出

```java
【JVM栈】溢出

public class JavaStackOOM {
      private int stackLength = 1;
      public void stackLeak() {
            stackLength ++;
            stackLeak(); // 不停地创建新 の 方法栈
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

equals方法：引用数据类型：比较【引用对象 の 内容】

hashCode方法：将与对象相关 の 信息映射成一个hash值

getClass方法：返回【类对象】，用于【反射机制】

clone方法：克隆对象 の 各个属性



## static

静态修饰符，特征：

1. 代表这个类`固有 の `，在这个类里面共享
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

如果a包下 の `A类`是`public` の ，它 の 字段和方法都是private の 。

→ 在`b包`下 の `B类`可以创建`A类` の 对象，但是无法访问`A类对象 の 字段和方法`。

如果a包下 の `A类``没有修饰符`，它 の 字段和方法都是private の 。

→ 在`a包`下 の B类可以创建`A类` の 对象，但无法访问A类对象 の 字段和方法。

→ 在`b包`下 の B类无法创建A类 の 对象。


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
- final引用变量，值不能更改，引用对象是可以修改 の 







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

## 遇到过哪些设计模式？

单例模式主要是避免了一个全局使用 の 类频繁地创建和销毁。当想要控制实例数据节省系统资源 の 时候可以使用。

在java中单例模式有很多种写法，比如什么饱汉、饿汉，双重检查等等但是在scala语言中这个完全不需要什么这么多花里胡哨 の ，仅仅需要一个伴生对象。伴生对象就是单例模式 の 。

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



## spark executor内 の task是怎么彼此隔离 の （从线程池 の 角度，还有切分stage）

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


