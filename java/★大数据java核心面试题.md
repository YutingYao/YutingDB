## Java官方提供了哪几种线程池

一共5种：

1. `new Cached Thread Pool`，是一种可以缓存 の 【线程池】，它可以用来处理【大量短期】 の 【突发流量】

特点：

- 最大线程数 = integer.MaxValue
- 存活时间 =  60 秒
- 阻塞队列 = Synchronous Queue

2. `new Fixed Thread Pool`：是一种可以【固定线程数量】 の 【线程池】

特点：

- 任何时候最多有 n 个【工作线程】是活动 の 。
- 如果任务比较多，就会加到【阻塞队列】里面等待；

3. `new Single Thread Executor`：【工作线程】数目被限制为 1，无法动态修改

特点：

- 保证了所有任务 の 都是被【顺序执行】
- 最多会有一个任务处于活动状态；

4. `new Scheduled Thread Pool`：可以进行【定时 or 周期性】 の 工作调度；

5. `new Work Stealing Pool`：Java 8 才加入这个创建方法，

特点：

- 其内部会构建ForkJoinPool，
- 利用【Work-Stealing算法】，【并行】地处理任务，不保证顺序；

线程池 の 核心是：

ThreadPoolExecutor()：上面创建方式都是对ThreadPoolExecutor の 封装。

## 线程池线程复用的原理是什么？

任务结束后，不会回收线程。

## 线程池 の 工作原理

1. 【线程池】是一种池化技术，【池化技术】是一种【资源复用】 の 设计思想，常见 の 【池化技术】有：

- 连接池
- 内存池
- 对象池

2. 【线程池】复用 の 是【线程资源】。它 の 核心设计目 の 是：

- 减少【线程】 の 频繁【创建、销毁】带来 の 【性能开销】
- 【线程池】本身可以通过【参数】来控制【线程数量】，从而保护资源

3. 线程池 の 【线程复用技术】：【线程本身】并不【受控】，【线程 の 生命周期】由【任务 の 运行状态】决定，无法人为控制。为了实现【线程 の 复用】，线程池里面用到了【阻塞队列】。简单来说：

- 线程池内 の 【工作线程】会处于【一直运行】状态。
- 【工作线程】从【阻塞队列】里面，获取【待执行 の 任务】，一旦队列空了，那么，这个【工作线程】就会被阻塞，直到有【new の 任务】进来
- 线程池里面 の 【资源限制】是通过几个【关键参数】来控制 の

分别是：

1. 核心线程数：【核心线程数】是【默认长期存在】 の 【工作线程】
2. 最大线程数量：【最大线程数】是根据【任务情况】来【动态】创建 の 线程

以上，就是我对这个问题 の 理解

## 如果你提交任务时，线程池队列已满，这时会发生

当提交一个任务到【线程池】，它 の 【工作原理】可以分为【4个步骤】：

1. 预热【核心线程】
2. 把任务添加到【阻塞队列】
   - 如果是【无界队列】，可以继续提交
   - 如果是【有界队列】，且【队列满了】，且【非核心线程数】没有达到【阈值】
3. 则创建【非核心线程】增加【处理效率】
4. 如果【非核心线程数】达到了【阈值】，就触发【拒绝策略】

## 线程池如何知道一个线程 の 任务已经执行完成？

线程池如何知道一个线程 の 任务已经执行完成？

我会从 2 个方面，来回答这个问题：

1. 从线程池 の 内部：

当我们把一个任务交给【线程池】执行时，【线程池】会调度【工作线程】来执行这个任务 の 【run方法】，当【run方法】正常执行结束以后，也就意味着，这个任务完成了。

so，线程池 の 【工作线程】通过同步调用任务 の 【run方法】，并且等待【run方法】返回后，再去统计【任务 の 完成数量】

2. 从线程池 の 外部：

- `isTerminated() 方法`：可以去判断线程池 の 【运行状态】 ，可以【循环】判断该方法 の 【`返回值`】，to 了解线程 の 【运行状态】。一旦显示为 Terminated，意味着线程池中 の 【all 任务】都已经执行完成了。但需要主动在程序中调用线程池 の 【shutdown() 方法】，在实际业务中，不会主动去关闭【线程池】。so，这个方法在【使用性、灵活性】方面，都不是很好。
- `submit() 方法`：它提供了一个【Future  の `返回值`】，我们可以通过【Future.get() 方法】去获得【任务 の 执行结果】。当任务没有完成之前，【Future.get() 方法】将一直阻塞，直到任务执行结束。只要【Future.get() 方法】正常返回，则说明任务完成。
- `CountDownLatch计数器`：通过初始化指定 の 【计数器】去进行倒计时，其中，它提供了两个方法：分别是：
  
1. await() 阻塞线程
2. countDown()

去进行倒计时，一旦倒计时归0，所有阻塞在【await()方法】 の 线程，都会被【释放】。

总 の 来说，想要知道【线程是否结束】，必须获得【线程结束】之后 の 【状态】，而线程本身没有【`返回值`】，所以只能通过【阻塞-唤醒】 の 方式实现，Future.get() 和 CountDownLatch，都是采用这种方法


## 什么是锁？

在并发环境下，多个线程对【同一个资源】进行争抢，可能导致【数据不一致】的问题。

为了解决【数据不一致】，因而引入【锁机制】


## 线程之间如何进行通讯

1. 线程之间可以通过【共享内存】or【网络】来进行【通信】
2. if 通过【共享内存】:
   - 需要考虑并发问题。什么时候阻塞？什么时候唤醒？如 wait() 和 notify()
3. if 通过【网络】：
   - 同样需要考虑【并发问题】

## 线程池是如何实现线程复用 の ？

采用【生产者】和【消费者】 の 模式，去实现【线程】 の 复用。
采用一个【阻塞队列】去解构【生产者】和【消费者】

【生产者】不断地产生任务，保存到【阻塞队列】里面
【消费者】不断从【阻塞队列】消费任务

在【线程池】里面，需要保证【工作线程】 の 重复使用，所以使用了【阻塞队列】。

生产者线程：指 交【任务】到【线程池】 の 线程。保存到【阻塞队列】。然后，线程池里面 の 【工作线程】不断得从【阻塞队列】获取任务执行。

如果【阻塞队列】没有任何任务 の 时候，那么这些工作线程就会【阻塞等待】，直到又有【新 の 任务】进来，那么这些【工作线程】又再次被唤醒

## 当任务数超过【线程池】 の 【核心线程数】时，如何让它不进入队列？

当提交一个任务到【线程池】，它 の 【工作原理】可以分为【4个步骤】：

1. 预热【核心线程】
2. 把任务添加到【阻塞队列】
3. 如果添加到【阻塞队列】失败，则创建【非核心线程】增加【处理效率】
4. 如果【非核心线程数】达到了【阈值】，就触发【拒绝策略】

如果，我们希望【任务】不进入【阻塞队列】，我们只需要影响【第二步】 の 【执行逻辑】即可:

- 修改【阻塞队列】 の 类型：

- 比如，Synchronous Queue 。是一个不能存储任何元素 の 阻塞队列。它 の 特性在于，每次【生产】一个任务，就必须及时【消费】这个任务。

- 从而避免【任务】进入到【阻塞队列】，而是直接去【启动】【最大线程数量】去处理

## 如何在不加锁 の 情况下解决线程安全问题？

这个问题有3个方面：

① 所谓 の 【线程安全】问题，其实是指：

- 【多个线程】，同时对【某个共享资源】 の 访问，导致 の 【原子性、可见性、有序性】 の 问题。

② 解决【线程安全】问题 の 方式是【增加】【同步锁】，常见 の 是像【synchronized、lock】等。

- 对【共享资源】加锁以后，多个【线程】在访问这个资源 の 时候，必须要先获得【锁】，也就是说，要先获得【访问资格】，
- 而【同步锁】 の 特征是：在同一个时刻，只允许一个线程，访问一个资源，直到【锁】被释放，虽然这种方式，可以解决【线程安全性】 の 一个问题，但同时带来了【加锁】和【释放锁】所带来 の 一个【性能开销】

③ 如何在【性能】和【安全性】之间取得一个balance。这就引出了一个【无锁并发】 の 概念，一般来说，会有以下几种方法：

1. 自旋锁：指篇【线程】在没有【抢占 の 锁】 の 情况下，先【自旋】指定 の 次数，去尝试获得【锁】。
2. 乐观锁： 给每个数据增加一个【版本号】，一旦数据发生变化，则去修改这个版本号。CAS の 机制，可以完成【乐观锁】 の 功能。
3. 在程序设计中，尽量去减少【共享对象】 の 使用。从业务上去实现【隔离】避免【并发】。

## HashMap 和 HashTable の 区别

<https://www.bilibili.com/video/BV1Dh411J72Y>

| HashMap  |  HashTable |
|---|---|
| 非同步  | 线程同步，线程安全，有关键字 synchronized  |
| 允许 k-v 有 null值 | 不允许 k-v 有 null值  |
| hash数组的默认大小是 16 | hash数组的默认大小是 11 |
| hash算法不同：增长方式是 2的指数  | hash算法不同：增长方式是 2*old + 1 |
| 继承 AbstractMap类  | 继承 Dictionary类，Dictionary类 已经被废弃  |

## HashTable是如何保证线程安全的？

关键字 synchronized

## String、StringBuffer 与 StringBuilder 之间区别

<https://www.bilibili.com/video/BV1b3411G7gr>

<https://www.bilibili.com/video/BV1KQ4y1z76p>

<https://www.bilibili.com/video/BV1G3411c7cV>

`String`:

- 不可变
- 操作少量数据，或者不操作数据时使用

`StringBuilder`: (优先选择)

- 可变
- 线程不安全

`StringBuffer`:

- 可变
- 线程安全
- 性能较低

## 什么是【互斥】？

互斥条件。【一个资源】只能被【一个线程】占用。

是无法被破坏 の 。因为【锁】本身就是通过【互斥】来解决【线程安全性】问题。

AQS使用【int类型】的【volatile修饰】的【互斥变量state】来表示【锁竞争】的状态

synchronized的 `monitor`  の 实现，完全是依靠【操作系统】内部 の `互斥锁`，性能也很低

CAS 实现【多线程】对【共享资源】竞争 の 【互斥性质】

## volatile关键字有什么用？

1. 保证在【多线程环境】下【共享变量】 の 【可见性】
   - volatile 比 synchronized 更轻量级 の 【同步锁】，在访问【volatile变量】时，不会执行【加锁操作】，因此，也就不会执行【线程阻塞】。
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

## 【可见性】问题的成因

1. CPU层面 の 高速缓存：
   - 在CPU里面设计了【三级缓存】去解决【CPU运算效率】和【内存IO效率】 の 问题。
   - 但是，他带来了【缓存一致性】 の 问题，
   - 而在【多线程并行执行】 の 情况下，【缓存一致性】 の 问题就会导致【可见性问题】

## volatile关键字是如何保证【可见性】的？

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

1. 首先，对变量 の 【写操作】不依赖于【当前值】。比如，像【i++】这样的
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

## 【指令重排】有三种形式

1. 【编译器优化】重排序
2. 【指令集并行】重排序
3. 【内存系统】重排序

这些【重排序】会导致【多线程】程序出现【内存可见性】问题。

## volatile关键字是如何避免【重排序】的？

如果对【共享变量】增加了【volatile关键字】，那么——

- 【`编译器`层面】就不会触发【`编译器`优化】
- 就会有【内存屏障】，从而【阻止重排】：
  - 在读和读之间，会有【读读屏障】
  - 在读和写之间，会有【读写屏障】
  - 在写和读之间，会有【写读屏障】
  - 在写和写之间，会有【写写屏障】

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

## 什么是CAS

1. 背景：
   - 在【多线程】环境下，会存在【原子性问题】。
   - 虽然，可以加一个【synchronized 同步锁】，但是，加【同步锁】一定会带来性能上 の 损耗。
   - 所以，对于这一类场景，我们可以使用【CAS机制】来进行优化

2. 作用：
   - 保证在【多线程】 の 环境下，对【共享变量】修改 の 【原子性】
   - CAS 是【原子 の】，不会存在【any 线程安全】 の 【问题】。

3. 全称：
   - Compare and Swap，即，比较-替换。

4. 定义：
   - 是 java 中 【unsafe类】里面 の 【compareAndSwapInt()方法】。
   - 虽然平时开发中，不增常用。但在许多底层源码中，都大量采用到了CAS。

5. 该方法 の 逻辑是：

   - 当有两个【线程】都需要对某个【共享变量】进行修改。
   - 【线程A】的【预期】是将 0 变更为 1。
   - CAS 会比较【内存地址偏移量stateOffset 对应 の 值】，和【预期值 0 】是否相等：
     - 如果不等，就【什么都不做】并【返回false】。
     - 如果相等，就直接修改【内存地址】中【state の 值】为 1.

6. CAS 是如何保证原子性的？
   - CAS 本质上，还是从【内存地址】中——读取，比较，修改。
   - 这个过程无论在什么层面实现，都会存在【原子性】问题。
   - 所以，在 CAS 底层实现上，会增加一个【lock指令】去对【缓存】加【锁】。
   - 从而保证了【比较、替换】这个两个操作 の 原子性。

7. 【compareAndSwapInt()方法】有4个参数，分别是：
   - 当前对象实例
   - 成员变量state在【内存地址】中 の 【偏移量】
   - 预期值 0
   - 期望更改之后 の 值 1

-----------------------------------

CAS  の 典型应用场景有2个：

1. J.U.C里面 の  Atomic  の 【原子实现】，如：
   - AtomicInteger
   - AtomicLong
2. 实现【多线程】对【共享资源】竞争 の 【互斥性质】，如：
   - AQS
   - ConcurrentHashMap（通过【CAS机制】实现元素个数的【累加】）
   - ConcurrentLinkedQueue
   - 实现synchronized中的【偏向锁、轻量级锁】


## 为什么CAS一定要`volatile变量`配合？volatile的应用场景

volatile 适合用在一个【变量】被多个【线程】共享，【线程】直接给这个【变量赋值】 の 场景

CAS就是这样一个场景，比较-替换

volatile 能保证【每次拿到 の 变量】是`主内存`中`最新 の 那个值`

## 什么是 AQS

英文全称：AbstractQueuedSynchronizer

是【多线程同步器】

是J.U.C包中，多个组件的【底层实现】

包括：

- lock
- countdownlatch

AQS 提供了2种锁机制：

- 排它锁（独占资源）
- 共享锁（共享资源）

排他锁：写锁。同一时刻，只允许【一个线程】去访问【共享资源】。如ReentrantLock

共享锁：读锁。同一时刻，只允许【多个线程】去访问【共享资源】。如countdownlatch

有 2 个核心组成部分：

1. AQS使用【int类型】的【volatile修饰】的【互斥变量state】来表示【锁竞争】。【多个线程】对【state共享变量】进行修改，来实现【锁竞争】
   - AQS采用了【CAS机制】，去保证【state互斥变量】更新的【原子性】
   - 0：表示【当前没有任何线程】竞争【锁资源】
   - ≥1：表示【已经有线程】正在持有【锁资源】
2. 用【双向链表】结构维护的【FIFO线程等待队列】。【竞争失败】的【线程】会加入【FIFO队列】。
   - 【未获取到锁】的【线程】通过【unsafe类】中的【park方法】去进行【阻塞】，把阻塞的线程，按照【FIFO】的原则，加入到【双向链表】的结构中
   - 【获得到锁】的【线程】在【释放锁】之后，会从【双向链表】的【头部】唤醒【下一个等待】的线程，再去【竞争锁】。

## AQS为什么要使用双向链表？

【双向链表】有2个指针：

- 一个指向【前驱节点】
- 一个指向【后驱节点】

所以，支持【常量级别】的【时间复杂度】，去找到【前驱节点】。

AQS使用双向链表有3个方面的原因：

1. 没有竞争到【锁】的【线程】加入到【阻塞队列】的前提是，当前所在节点的【前置节点】是一个【正常状态】。所以需要判断【前驱节点】的状态，如果没有【指针】指向【前驱节点】，我们就需要【从头部节点】开始遍历。
2. 线程可以通过【interrupt()方法】触发【中断】。而这个时候，被【中断】的线程的状态会被修改成【Cancelled状态】，被标记为【Cancelled状态】的线程是【不需要竞争锁】的，但是它仍然存在于【整个双向链表里面】，在后续，需要把这个【Cancelled状态】的节点从【链表里面移除】，如果没有【指针】指向【前驱节点】，我们就需要【从头部节点】开始遍历。
3. 按照【公平锁】 的设计，只有【头结点】的【下一个节点】才有必要竞争锁，其余节点都去竞争锁的话，会造成【惊群效应】。所以，需要判断【前驱节点】是不是【头节点】，如果不是【头节点】就没有必要去触发【锁的竞争】，所以会涉及到【前驱节点】的【查找】，如果是【单向链表】是无法实现这样一个功能的

## 没有竞争到【锁】的【线程】加入到【阻塞队列】的前提是当前所在节点的【前置节点】是一个【正常状态】，为什么这样设计？

目的是为了避免在【链表】里面存着【异常状态】的【节点】，导致无法【唤醒】后续线程的问题。

## synchronized

## 线程 の 状态转化

1. 【new】 → start →【Runnable】

- 【Runnable】→ sleep、join(T)、wait(T)、locksupport.parkNanos(T)、locksupport.parkUntil(T)→ 【Timed Waiting】→ 时间到、unpark→【Runnable】

- 【Runnable】→ join、wait、locksupport.park→ 【Waiting】→ notify、notifyAll → 【Runnable】

- Synchronized  → 没有获得`monitor`锁 → 【Blocked】 → 获得`monitor`锁 → 【Runnable】

2. 【Runnable】 → 【terminated】

## 如果一个线程两次调用start()，会出现什么？

当我们第一次调用【start()方法】 の 时候:

- 程状态可能会处于【Runnable】。

再调用一次【start()方法】 の 时候

- 相当于，让这个正在运行 の 【线程】重新运行一遍。

两次调用start()，显然是不合理 の 。

因此，为避免这样一个问题，在线程运行 の 时候，会先去判断当前【线程】 の 一个【运行状态】。

以上，就是我对这个问题 の 理解。

## 调用join时的线程状态

| 线程B调用join()  | 线程B调用join(1000)  |
|---|---|
| 当前线程A变成WAITING  | 当前线程A变成TIMED_WAITING  |
| 线程B变成RUNNABLE  | 线程B变成RUNNABLE  |


## BLOCKED 和 WAITING 有什么区别？

BLOCKED 和 WAITING 都属于【线程等待】状态。

| BLOCKED  | WAITING   |
|---|---|
| 【锁竞争失败】后【被动触发】 の 状态   |  【人为】 の 【主动触发】 の 状态  |
| 唤醒是【自动触发】 の ，【获得锁 の 线程】在【释放锁】之后，会触发唤醒  | 通过【特定方法】【主动唤醒】 |
| 等待synchronized锁 の 线程  | 等待ReentrantLock の 线程   |
| 【线程】阻塞于锁  | 【进入该状态 の 线程】需要等待【其他线程】 の 【特定操作】（通知或中断），才会被唤醒。   |
| 没有获得`monitor`锁 → 在【多个线程】去竞争【synchronized同步锁】，没有竞争到【锁】 の 线程会被【阻塞等待】，叫做 blocked。  |使用【Object.join() Object,wait() LockSupport.park()】进入到一个【Waiting 状态】   |
| 获得`monitor`锁 → 【Runnable】  | 【Object.notify() LockSupport.unpark()】去唤醒【阻塞】 の 线程   |

## Thread.yield()

1. 仅仅只能让【线程】从【运行状态】转变为【就绪状态】，而不是阻塞状态
2. 【就绪状态的线程】按照【优先级】被调度

## 描述一下Object类中的常用方法？

toString

hashCode

equals

clone

finalized

wait

notify

notifyAll

## sleep() 和 wait() 有什么区别？

| Object.wait()  | Thread.sleep()  |
|---|---|
| 会释放【锁资源】以及【CPU资源】  | 不会释放【锁资源】，但会释放【CPU资源】  |
| 来自 Object | 来自 Thread  |
| wait() 可以使用 notify()/notifyAll()直接唤醒  | sleep() 时间到会自动恢复  |
|    | 有`InterruptException受检异常`  |

```java
public static void sleep() throws InterruptException;
```

## wait和sleep是否会触发锁 の 释放以及CPU资源 の 释放？

| Object.wait()  | Thread.sleep()  |
|---|---|
| 让一个线程，进入【阻塞状态】 | 让一个线程，单纯地进入到一个睡眠状态 |
| 必须写在【Synchronized同步代码块】里面 | 没有强制要求加【synchronized同步锁】 |
| 因为【wait & notify】是基于【共享内存】来实现【线程】与【线程】之间【通信】。所以，在调用【wait & notify】之前，它必须要【竞争锁资源】，从而去实现条件 の 【互斥】。wait 方法 必须要要释放锁，否则就会【死锁】 | 不会触发【锁 の 释放】 |

此外，凡是那些让线程进入【阻塞状态】 の 方法，都会实现【CPU时间片】 の 切换，从而提升【CPU】 の 利用率。

## notify()和 notifyAll()有什么区别？

| `notifyAll()` | `notify()`  |
|---|---|
| 唤醒所有 の 线程  | 唤醒一个线程  |

`notifyAll()` 调用后，会将【all线程】由`等待池`移到`锁池`，然后参与`锁` の 竞争，

- if 竞争成功 then 继续执行，

- if 不成功 then 留在`锁池` then 等待`锁`被`释放`后, 再次参与竞争。

`notify()`只会唤醒一个线程，具体唤醒哪一个线程由虚拟机控制。


还有一点是基本不会用`notify()`去唤醒, 推荐使用`notifyAll()`,

因为`notify`是唤醒某个指定线程，你不知道这个线程到底是哪个，在`多线程情况`下，使用推荐使用`notifyAll()方法`.

## wait和notify 为什么要在synchronized代码块中

<https://www.bilibili.com/video/BV1xr4y1p7w6>

wait 和 notify 用于【多个线程】之间 の 【协调】:

- wait 表示让【线程】进入【阻塞状态】，
- notify 表示让【阻塞 の 线程】被【唤醒】

两种通常同时出现。

synchronized在jvm中它是由Object`monitor`实现。

我们所谓 の 【wait notify】其实也是object`monitor`对象 の 方法，

要搞清楚【wait notify】怎么用，就必须搞清楚他们【方法 の 功能】和【`monitor` の 模型】。

`monitor`对象中有:

- *Owner*(锁拥有者)
- *WaitSet* 等待队列
- *EntryList* 阻塞队列。

这个对象在【jvm源码】中，

- *wait方法*  将本线程置入*WaitSet*并【释放锁】，
- *notify方法*  把某个在*WaitSet*中 の 线程放入 *EntryList* 并【唤醒】。

在进入 有 synchronized 修饰 の 方法时：

1. 当多个【线程】同时访问`synchronized修饰` の 代码时，首先这些【线程】会进入*EntryList* 中。
2. 当【线程】获取到 `monitor`后，再执行【moniter enter 指令】。【线程】进入*Owner*区域
3. 同时 `monitor` の 计数器加1。
4. 如果调用*wait方法*，将释放 *当前线程* の `monitor`，再执行【moniter exit 指令】。
5. `monitor`计数器减1，同时该【线程】进入*WaitSet*等待调用notify()被唤醒。
6. 如果持有锁 の 【线程】执行完程序后也会释放`monitor`对象锁，以便其他【线程】获取`monitor`。


## 说一下 synchronized 底层实现原理？

synchronized 就是锁住【对象头】中【两个锁】【标志位】 の 【数值】

## synchronized  为什么要进行锁升级

在 Java 6 之前，`monitor`  の 实现完全是依靠【操作系统】内部 の `互斥锁`

- `互斥锁`是【系统方法】，需要进行`用户态`到`内核态` の 切换，所以性能很低: 

但在 Java 6  の 时候，Java 虚拟机提供了三种不同 の  `monitor` 实现，也就是【锁升级机制】：


## 线程有哪些状态？

线程 の 6种状态：

`初始(NEW)`：新创建了一个【线程对象】，但还没有调用`start()方法`。

`运行(RUNNABLE)`：这个状态下，线程可能【就绪（ready）】或者【运行中（running）】

- 【线程对象】创建后，放入【RUNNABLE线程池】中，等待被【线程调度】选中，从而获取【CPU の 使用权】，此时处于【就绪状态（ready）】。

- 【就绪状态】 の 线程在获得【CPU时间片】后变为运行中【状态（running）】。

`阻塞(BLOCKED)`：【线程】阻塞于【锁】，处于锁等待状态。

`等待(WAITING)`：需要等待【其他线程】触发条件后【唤醒】，如 wait & notify。

`超时等待(TIMED_WAITING)`：该状态不同于`WAITING` の 是，它可以在指定 の 时间后【自行返回】。

`终止(TERMINATED)`：表示该线程已经【执行完毕】。

## 说一下 Runnable和 Callable有什么区别？

- Runnable 没有`返回值`，
- Callable 可以拿到有`返回值`，
- Callable 可以看作是 Runnable  の 补充。

## 说一下你对多线程 の 理解。多线程有几种实现方式？

有4种，分别是：

1. 继承`Thread类`
2. 实现`Runnable接口`
3. 实现`Callable接口`通过`FutureTask包装器`来创建`Thread线程`
4. 通过`线程池`创建线程，使用`线程池`接口`ExecutorService`结合`Callable、Future`实现有返回结果 の 多线程。

前面两种【无`返回值`】原因：

- 通过重写`run方法`，`run方法` の `返回值`是`void`。
  
后面两种【有`返回值`】原因：

- 通过`Callable接口`，这个方法 の `返回值`是`Object`。

## Thread 和 Runnable  の 区别

| Thread  | Runnable  |
|---|---|
| 类  | 接口  |
| 支持【单一继承】  | 可以支持【多继承】  |
|   | 已经存在【继承关系】 の 【类】  |

## 说一下 atomic  の 原理？

【原子操作】【无锁 lock-free】实现了【无锁的线程安全】

使用`java.util.atomic`提供的`原子操作`可以简化【多线程编程】，包含了：

- AtomicInteger
  1. int `addAndGet`(int delta)
  2. int `incrementAndGet`()
  3. int `get`()
  4. int `compareAndSet`(int expect, int update)
- AtomicLong
- AtomicIntegerArray

atomic 主要利用：

- CAS (Compare And Swap)
- volatile
- native 方法

来保证【原子操作】，从而避免【synchronized  の 高开销】，执行效率大为提升。

## atomic 和 volatile 有什么区别？

| volatile  | atomic  |
|---|---|
| 可以确保【现行关系】，即【写操作】发生在【后续读操作】之前，但不能保证【原子性】  | 具有【原子性】  |
| 如 i++  | 如 getAndIncrement 方法  |
| 适用于【赋值】  | 适用于【计数器、累加器】  |

## 使用 AtomicLong 实现【线程安全】的【ID序列生成器】

```java
class IdGenerator {
   AtomicLong var = new AtomicLong(0);

   public long getNextId() {
      return var.incrementAndGet();
   }
}
```

## 使用 AtomicInteger 实现【线程安全】的【Counter】？

```java
class Counter {
   private AtomicInteger value = new AtomicInteger(0);

   public int add(int m) {
      return this.value.addAndGet(m);
   }

   public int dec(int m) {
      return this.value.addAndGet(-m);
   }

   public int get() {
      return this.value.get();
   }
}

public class Main {
   final static int LOOP = 100;

   public static void main(String[] args) throws Exception{
      Counter counter = new Counter();

      Thread t1 = new Thread() {
         public void run() {
            for (int i = 0; i < LOOP; i++) {
               counter.add(1);
            }
         }
      }

      Thread t2 = new Thread() {
         public void run() {
            for (int i = 0; i < LOOP; i++) {
               counter.dec(1);
            }
         }
      }
      t1.start();
      t2.start();
      t1.join();
      t2.join();
      System.out.println(counter.get());
   }
}
```

## Files线程 の  run() 和 start() 有什么区别？

| start() 方法  | run() 方法  |
|---|---|
|  启动线程 | 执行线程 の 运行时代码  |
|  只能调用一次 | 可以重复调用  |

## join的作用：

是 Thread对象的【实例方法】

调用【join的线程】先执行，再执行【当前线程】

将【当前线程】挂起，等待【其他线程结束】后，继续执行【当前线程】

注意：`t1.join()` 不是挂起 `t1`。而是把`t1`加入到【当前线程】，这样，两个【线程】就是【顺序执行】了。挂起【当前正在执行】的线程，而不是挂起【调用join方法】的线程


## CopyOnWriteArrayList原理

是 → 写时复制

目的：通过复制【引用】来保证【数据隔离】

高性能的原因：只复制【引用】，并不复制【数据本身】，所以，在获取【迭代器】时，速度很快

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

## Java多线程 の Future模式

[设计源于生活中](https://www.bilibili.com/video/BV1ov41167vH)

核心思想 - 【`异步调用`】。

Future 模式是【多线程并发】中常见 の 【设计模式】，

当【client】发送请求时，【服务端】不能立刻响应【客户端】需要 の 数据，就响应一个【虚拟】 の 【数据对象】，这时，【客户端】就不会【阻塞等待】，就可以异步地做其他工作。

当【client】真正需要【请求 の 数据】时，服务端再发送真实 の 数据给【client】。

这就是【Future 设计模式】

## synchronized 和 Lock 有什么区别？

| synchronized  |  lock |
|---|---|
| 有个【锁升级】 の 过程  |  lock |
| 原理：锁住【对象头】  |  原理： |
| 非公平锁  | 【公平锁、非公平锁】，可选择 |
| 是【方法关键字】  |  适用于【接口】 |
| 底层是【JVM层面】 の 锁  |  底层是【API层面】 の 锁 |
| 适用于【线程少，竞争不激烈】  |  适用于【线程多，竞争激烈】 |
| 无法终端  |  可以中断 |
|  可以给【类、方法、代码块】加锁  | 只能给代码块加锁  |
| 【手动】获得锁，释放锁  | 【自动】获得锁，释放锁     |
| 【发生异常】会自动释放锁，不会造成死锁  |  如果使用不当，没有 unLock()去释放锁就会造成【死锁】 |

```java
lock.lock();
// ...
lock.unlock();

sychronized{
  // ...
}

sychronized作用在：静态方法、实例方法、this代码块、class代码块

sychronized 修饰 方法：

public static synchronized void func(){
  // ...静态方法
}

public synchronized void func(){
  // ...实例方法
}

sychronized 修饰 代码块：

public void func() {
    sychronized (obj) {
        System.out.println("代码块")
    }
}
```

## lock 几次，就要 unlock 几次

```java
private static final Lock lock = new ReentrantLock();

public static void a() {
    lock. lock();

    try {
        System.out.println(Thread.currentThread().getName())
        Thread.sleep(millis: 2*1000);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        lock.unlock();
    }
}

public static void main(String[] args) {
    // 定义线程任务
    Runnable runnable = () -> {
        a();
    };

    Thread t1 = new Thread(runnable, name:"t1");
    Thread t2 = new Thread(runnable, name:"t2");

    t1.start();
    t2.start();
}
```

```java
public static void func() {
    System.out.println(Thread.currentThread().getName())
    lock. lock();
    // 再锁一次
    lock. lock();

    try {
        Thread.sleep(millis: 2*1000);
        System.out.println(Thread.currentThread().getName())
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        // 释放2次锁
        lock.unlock();
        lock.unlock();
    }
}

public static void main(String[] args) {
    // Lock 连续运行多次 の 情况
    new Thread (
        () -> {
            a();
        },
        name: "t1"
    ).start();

    new Thread (
        () -> {
            a();
        },
        name: "t2"
    ).start();
}

返回结果：
t1 run() begin
t2 run() begin
t1 run() end
t2 run() end

如果only unlock 一次，就会造成【死锁】，【死锁】情况下，显示结果如下：
t1 run() begin
t2 run() begin
t1 run() end
```



## ReentrantLock 是如何实现锁公平和非公平性 の ？

定义：

- 公平：竞争【锁资源】 の 线程，严格按照请求 の 顺序，来分配锁。
- 非公平：竞争【锁资源】 の 线程，允许插队，来抢占【锁资源】

synchronized 和 ReentrantLock 默认：

- 非公平锁

ReentrantLock 内部使用【AQS】来实现【锁资源】 の 一个竞争，没有竞争到【锁资源】 の 【线程】会加入到【AQS】 の 【双向链表】里面。

这样，

【公平】 の 实现方式就是，【线程】在【竞争锁资源】 の 时候，会判断【AQS双向链表】里面有没有【等待线程】，如果有，就加入到【双向链表尾部】进行等待。

【非公平】 の 实现方式就是，不管【双向链表】里面有没有【阻塞线程】等待，它都会先去 try 抢占【锁资源】，尝试更改【state互斥变量】去竞争【锁】。if 抢占失败，就会再加入【AQS 双向链表】里面等待。

【公平】性能差 の 原因在于，【AQS】还需要将【双向链表】里面 の 线程【唤醒】，就会导致【内核态】 の 切换，对性能 の 影响比较大。

【非公平】性能好 の 原因在于，【*当前线程*】正好在【上一个线程】释放 の 临界点，抢占到了锁。那么意味着，这个线程不需要切换到【内核态】，从而提升了【锁竞争】 の 效率。


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

## 【集合类】是如何解决【高并发】问题的？

非安全的 集合类包括：

- ArrayList
- LinkedList
- HashMap
- HashSet
- TreeMap
- TreeSet

普通的 安全的 集合类包括：

- Vector 
- Hashtable

高性能线程安全的集合类：

- ConcurrentHashMap
- CopyOnWriteArrayList


## HashMap 有哪些【线程安全】 の 方式

方式一：通过 `Collections.synchronizedMap()` 返回一个新 の  Map

- 优点：代码简单
- 缺点：用了【锁】 の 方法，性能较差

方式二：通过 `java.util.concurrent.ConcurrentHashMap`

- 将代码拆分成独立 の segment，并调用【CAS指定】保证了【原子性】和【互斥性】
- 缺点：代码繁琐
- 优点：【锁碰撞】几率低，性能较好



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

## java8  の  ConcurrentHashMap 放弃【分段锁】，采用【node锁】。why？

node锁，粒度更细，提高了性能，并使用 `CAS 算法` 保证 `原子性`

所谓【分段锁】，就是将【数据分段】，给【每一段数据】加锁，确保线程安全。

## ConcurrentHashMap  の 扩容机制

1.7版本的 ConcurrentHashMap 是基于 `Segment 分段`实现的

- 每个 Segment 相当于一个小型的 HashMap，【是否扩容的判断】也是基于 Segment 的

- 每个 Segment 内部进行扩容，和 HashMap 的扩容逻辑相似

-------------------------------------

1.8版本的 ConcurrentHashMap 不是基于 `Segment 分段`实现的。

- 在转移元素时，先将【原数组】分组，将【每组】分给【不同的线程】来进行【元素的转移】

- 每个线程，负责【一组或多组】的元素转移工作。

## ConcurrentHashMap 是如何提高效率 の ？

[ConcurrentHashMap 底层实现原理？](https://www.bilibili.com/video/BV1QS4y1u7gG)

HashTable 给整张表添加一把【大锁】

而 ConcurrentHashMap 采用【分段锁】

ConcurrentHashMap 组成：

- 数组
- 单向链表
- 红黑树

-------------------------------------------------------
  
默认的【数组长度】是16。

由于存在【hash冲突】的问题，采用【链时寻址】的方式，来解决【hash表】的冲突，当【hash冲突】比较多的时候，会造成【链表长度较长】的问题。

在这种情况下，会使得 ConcurrentHashMap 中【查询复杂度】增加。因此，引入了【红黑树】的机制：

1. 【数组长度】 >= 64
2. 【链表长度】 >= 8

【单向链表】就会转化成【红黑树】

ConcurrentHashMap 在 HashMap 的基础上，提供了【并发安全】的实现。

【并发安全】的实现，是通过对【Node节点】加【锁】来保证【数据更新】的【安全性】

## ConcurrentHashMap 的优化体现在：

1. 在1.8里面，锁的【粒度】是数组中的一个节点；在1.7里面，锁的【粒度】是segment
2. 引入【红黑树】的机制，降低了【数据查询】的时间复杂度，为O(logN)
3. 在扩容时，引入了【多线程并发扩容】的一个实现，简单来说，就是多个【线程】对【原始数组】进行【分片】，【分片】后每个【线程】去负责一个分片的【数据迁移】
4. ConcurrentHashMap 有一个 【size方法】来获取总的元素个数。在【多线程并发场景】下，ConcurrentHashMap 对数组中元素的累加做了优化：
   - 当线程【竞争不激烈】时，通过【CAS机制】实现元素个数的【累加】
   - 当线程【竞争激烈】时，使用一个【数组】来维护【元素个数】，如果要增加【总元素个数】，直接从数组中【随机选择】一个，再通过【CAS机制】实现元素个数的【累加】

## 守护线程是什么？

守护线程是运行在`后台` の 一种`特殊进程`。

`周期性`地执行`某种任务`。

在 Java 中`垃圾回收线程`就是特殊 の 守护线程。


## 守护线程的特性？与【用户线程】的关系？

- 【生命周期】依赖于【用户线程】，
- 只有【用户线程】正在运行 の 情况下，【守护线程】才会有【存在 の 意义】。
- 【守护线程】不会阻止【JVM の 退出】，但【用户线程】会阻止【JVM の 退出】。

---------------------------------------

基于【守护线程】 の 特性，它更适合【后台】 の 【通用型服务】 の 一些场景。

## 如何设置【守护线程】？

【守护线程】创建方式，和【用户线程】是一样

调用【用户线程】`setDaemon方法` 为 True → 表示这个线程是【守护线程】。

## 线程 & 进程 の 区别

| 进程  | 线程  |
|---|---|
| 在【内存】中存在多个【程序】  | 每个【进程】有多个【线程】  |
| CPU采用【时间片轮转】 の 方式运行  |   |
| 实现【操作系统】 の 并发  | 实现【进程】 の 并发    |
| 通信较【复杂】  | 通信较【简单】，通过【共享资源】   |
| 重量级  |  轻量级  |
| 独立 の 运行环境  | 非独立，是【进程】中 の 一个【任务】|
| 单独占有【地址空间 or 系统资源】| 共享  |
|  进程之间相互隔离，可靠性高 | 一个【线程崩溃】，可能影响程序 の 稳定性  |
| 创建和销毁，开销大  |  开销小 |



## JVM性能优化 - 如何排查问题？

1. 打印出 `GC log`，查看 minor GC 和 major GC
2. `jstack` 查看【堆栈信息】
3. 应用 `jps，jinfo，jstat，jmap`等命令

## 如何查看线程死锁？

通过 【jstack命令】，会显示发生了【死锁】 の 线程

## 一些常见的JDK常见命令：

- jps
- jinfo
- jstat
- jhat
- jstack
- jmap

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

## 【pass💦】为了更好 の 控制【主内存】和【本地内存】 の 交互，Java 内存模型定义了八种操作来实现

lock：锁定。 主内存 の 变量，把一个变量标识为一条线程独占状态。
unlock：解锁。 主内存变量，把一个处于锁定状态 の 变量释放出来，释放后 の 变量才可以被其他线程锁定。
read：读取。 主内存变量，把一个变量值从主内存传输到线程 の 【工作内存】中，以便随后 の load动作使用
load：载入。 【工作内存】 の 变量，它把read操作从主内存中得到 の 变量值放入【工作内存】 の 变量副本中。
use：使用。 【工作内存】 の 变量，把【工作内存】中 の 一个变量值传递给【执行引擎】，每当虚拟机遇到一个需要使用变量 の 值 の 字节码指令时将会执行这个操作。
assign：赋值。 【工作内存】 の 变量，它把一个从【执行引擎】接收到 の 值赋值给【工作内存】 の 变量，每当虚拟机遇到一个给变量赋值 の 字节码指令时执行这个操作。
store：存储。 【工作内存】 の 变量，把【工作内存】中 の 一个变量 の 值传送到主内存中，以便随后 の write の 操作。
write：写入。 主内存 の 变量，它把store操作从【工作内存】中一个变量 の 值传送到主内存 の 变量中。


## jvm内存结构介绍【运行时数据区】

Java の 内存分为5个部分：

- 线程共享：
  - 方法区(method area) 🔸 常量池 + 静态变量 + 类
  - 堆(heap) 🔸 GC 对象，即【实例对象】，内存最大 の 区域

- 每个线程一份【线程隔离】：
  - 虚拟机栈(VM stack)→(heap) 🔸 每个java被调用时，都会创建【栈帧】
    - `栈帧`内包含：
      - `局部变量表`：存储【局部变量】：变量dog、cat等
      - `操作数栈`：线程执行时，使用到的【数据存储空间】
      - `动态链接`：`方法区`的引用 
      - `返回地址`：存储这个method被【调用的位置】，因为method结束以后，需要【返回】到被【调用的位置】

  - 本地方法栈 🔸 native语言
  - 程序计数器(pc register) 🔸【*当前线程*】执行到 の 【字节码行号】

## JVM分代年龄为什么是15次？

在JVM的【heap内存】里面，分为【Eden Space、Survivor Space、Old generation】，当我们在java里面，去使用【new关键词】去创建一个【对象】的时候，java会在【Eden Space】分配一块内存，去存储这个对象。

当【Eden Space】的内存空间不足时，会触发【Young GC】进行对象的回收，而那些因为存在【引用关系】而无法回收的对象，JVM会把它转移到【Survivor Space】。

【Survivor Space】内部存在【From 区】和【To 区】。那么从【Eden 区】转移过来的对象，会分配到【From 区】.

每当触发一次【Young GC】，那些没有办法被回收的对象，就会在【From 区】和【To 区】来回移动。每移动一次,【GC年龄】就会+1，默认情况下,【GC年龄】达到15的时候，这些对象如果还没有办法【回收】，那么JVM会把这些对象移动到【Old Generation】里面。

一个【对象】的【GC年龄】是存储在【对象头】里面的，而一个【Java对象】在【JVM的内存】布局，由3个部分组成：

1. 对象头
2. 实例数据
3. 对齐填充

而在对象头里面，4个bit位能够存储的最大数值是15。所以，从这个角度来说，【JVM分代年龄】之所以设置为15，因为，它最大能存储的数值是15。虽然，JVM提供了参数，来去设置【分代年龄】的大小，但是这个大小不能超过15。

此外，JVM还引入了【动态对象年龄】的判断方式，来决定把对象转移到【old generation】，也就是说，不管这个对象的【gc年龄】是否达到了15次，只要满足【动态年龄】的判断依据，也会把这个对象转移到【old generation】。

## 栈和栈桢

[设计源于生活中](https://www.bilibili.com/video/BV1YA411J7wE)

JVM中的【方法stack】是【线程私有】。每一个method的调用，都会在【方法stack】中压入一个【栈桢】。

如果启动main方法，stack中，压入main方法的栈桢。

执行methodA方法，stack中，压入methodA方法的栈桢。

执行methodB方法，stack中，压入methodB方法的栈桢。

然后methodB执行结束，methodB出栈

然后methodA执行结束，methodA出栈

然后main执行结束，main出栈

## ClassA a = new Class(1) 在jvm中怎么存储

???我猜

ClassA 是一个类，方法区(method area)

Class(1) 是一个【实例对象】，堆(heap)，当我们在java里面，去使用【new关键词】去创建一个【对象】的时候，java会在【Eden Space】分配一块内存，去存储这个对象。

a 是一个变量，随着main方法一起，压入虚拟机栈(VM stack)

a 指向Class(1)

## JVM 的理解

全称 Java虚拟机。

有两个作用：
① 运行并管理【java源码文件】所生成的【class文件】
② 在不同的操作系统安装不同的 JVM，从而实现【跨平台】的保障

## 为什么要了解JVM ？

对于【开发者】而言，即使不熟悉JVM的运行机制，也不影响代码的开发。

但当【程序运行过程】中出现了问题，而这个问题发生在JVM层面时，我们就需要去熟悉【JVM的运行机制】，才能迅速排查，并解决JVM的性能问题

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

## 双亲委派机制

自底向上地查看，是否加载过这个类

如果没有，再自顶向下，尝试去加载这个类

## JVM的运行流程？

1. 【类加载机制】：把【Class文件】加载到【内存】，进行【验证、准备、解析】，然后初始化，可以形成JVM可以直接使用的java.lang.Class

2. 【运行时数据区】：分为5个部分

3. 【垃圾回收器】：就是对【运行时数据区】的数据，进行【管理】和【回收】。

4. 【编译器】：【class字节码指令】通过【JIT Compiler 和 Interpreter】翻译成【对应操作系统】的【CPU指令】

5. 【JNI技术】：Native method interface。方便找到Java中的某个Native方法，如何通过C或者C++实现

## 为什么要垃圾回收？

方便程序员管理内存，不需要手动分配内存，释放内存

## 【垃圾收集算法】有哪3个：

1. 标记 - 清除：首先找到，哪些对象需要被回收，标记一下，直接删除。会有碎片。
2. 标记 - 整理：把可用的对象整理到一边，其余的对象直接删除。没有碎片。
3. 复制

## 垃圾收集器分类

串行收集器(单线程GC)：只有一个【垃圾回收线程】，【用户线程】暂停

并行收集器(多线程GC,吞吐量优先)：多个【垃圾回收线程】，【用户线程】暂停

并发收集器(多线程、高并发、低暂停)(CMS、G1)：【垃圾回收线程】、【用户线程】同时运行 

## 什么情况下使用G1垃圾收集器？

1. 50%以上的heap被live对象占用
2. 【对象分配】和【晋升的速度】变化非常大
3. 垃圾回收【时间】比较长

## Minor GC 和 Full GC 有什么不同？

Minor GC / Young GC: 指发生新生代 の 垃圾收集动作
Major GC / Full GC: 指发生老年代 の 垃圾收集动作

## G1垃圾收集器的【内存布局】

与【其他收集器】有很大的差别

它将【heap】划分成多个大小相等的Region，虽然仍然划分为【Eden、Survivor、Old、Humongous、Empty】

每个 Region 大小，都在 1M ~ 32M 之间，必须是 2的N 次幂

特点：

1. 仍然保留了【分代】的概念
2. 不会导致【空间碎片】
3. 可以明确规定在【M毫秒的时间段】，消耗在【垃圾收集】的时间。不得超过N毫秒。

## 【G1 垃圾收集器】的【最大停顿时间】是如何实现的？

G1 在【后台】维护了一个【优先列表】

每次：根据【允许 の 收集时间】，优先选择【回收价值最大】的Region

- 比如：Region1 花费 200 ms 能回收 1 M，Region2 花费 50 ms 能回收 2 M，则优先回收 Region2

这样就保证了 G1 垃圾收集器 的 效率

## CMS 垃圾收集器

全称 Concurrent Mark Sweep

目标：获取【最短回收停顿时间】

特点：

- 采用【标记-清除】算法
- 【内存回收过程】与【用户线程】并发执行

## 什么是【垃圾回收器】？

就是对【运行时数据区】的数据，进行【管理】和【回收】。

可以基于不同的【垃圾收集器】，比如说，serial、parallel、CMS、G1，

可以针对不同的业务场景，去选择不同的收集器。只需要通过【JVM参数】设置即可。

## 什么时候才会进行垃圾回收？

1. GC 是由 JVM 【自动】完成的。
   1. 当【Eden区 or S区】不够用
   2. 【老年代】不够用
   3. 【方法区】不够用
2. 也可以通过 `System.gc()方法` 【手动】垃圾回收

但【上述两种方法】无法控制【具体的回收时间】

## 如何确定一个对象是【垃圾】？

引用计数法【被废弃】

可达性分析算法：每个对象都有自己的引用。没有被GC ROOT引用的对象，会被回收。

【被GC ROOT引用的对象】包括：

1. 【STACK】中引用的对象
2. 【方法去】中【类静态属性、常量】引用的对象
3. 【本地方法栈中】JNI引用的对象

如果一个对象，没有【指针】对其引用，它就是垃圾

- 注意：如果AB互相引用，则会导致【永远不能被回收】，导致【内存溢出】

## 为什么 Eden:S0:S1 是 8:1:1 ?

https://www.bilibili.com/video/BV1dt411u7wi

https://space.bilibili.com/403102242

https://space.bilibili.com/59546029

## JVM是如何避免Minor GC时扫描全堆的?

## g1回收器、cms の 回收过程，场景

## 这几个分区在gc里都有什么处理，不同分区 の gc策略

## 一些JVM参数？

1. InitialHeapSize
2. NewRatio
3. UseG1GC
4. MaxHeapSize
5. ConcGCThreads

从而优雅地分析JVM出现的常见问题，并对其进行优雅地调优

## JVM性能优化 - 存在哪些问题？

- GC 频繁
- 死锁
- oom
- 线程池不够用
- CPU负载过高

## JVM性能优化 - 如何解决问题？

1. 适当增加【堆内存大小】
2. 选择合适的【垃圾收集器】
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

## java锁机制是怎么设计的?

在谈锁之前，我们需要简单了解【JVM运行时内存结构】。

每个object都有一把锁，这把【锁】存放在【`对象头`】中，

`对象头`中包含了 - Mark Word 存储了 锁信息

锁中记录了，【当前对象】被哪个【线程`threadid 字段`】【占用】。

其中的【锁标志位】分别对应了四种状态：

- 无锁
- 偏向锁
- 轻量级锁
- 重量级锁

## 多线程中 synchronized 锁升级 の 原理是什么？

synchronized 锁升级原理：

在`锁对象` の `对象头`里面有一个 `threadid 字段`，

在第一次访问 の 时候 `threadid 为空`，jvm 让其持有`偏向锁`，

并将 threadid 设置为其`线程 id`，

再次进入 の 时候会先判断 `threadid` 是否与其`线程 id` 一致，

如果一致, 则可以直接使用此对象，

如果不一致，则升级`偏向锁`为`轻量级锁`，通过`自旋`循环一定次数来获取锁，

执行一定次数之后，如果还没有正常获取到要使用 の 对象，

此时就会把锁从轻量级升级为`重量级锁`，此过程就构成了 synchronized 锁 の 升级。


- 偏向锁: 最轻量。线程会通过【CAS】获取一个预期 の 标记，在【一个线程】进行【同步代码】，减少【获取锁】 の 代价
  - 如果存在【多线程竞争】，就会膨胀为【轻量级锁】。
- 轻量级锁：【多线程】【交替执行】【同步代码】，而不是【阻塞】
  - 自旋锁：线程在获取【锁】 の 过程中，不会去【阻塞线程】，也就无所谓【唤醒线程】。【阻塞-唤醒】是需要【操作系统】去执行 の ，比较【耗时】。线程会通过【CAS】获取一个预期 の 标记，如果没有获取到，就会循环获取，会先自己循环【50~100次】
  - 如果超过【循环次数】，就会膨胀为【重量级锁】
- 重量级锁：在【多线程竞争阻塞】时，线程处于【blocked】，处于【锁等待】状态下 の 线程，需要等待【获得锁】 の 线程【释放】锁以后，才能【触发唤醒】，会进行`用户态`到`内核态` の 切换

## 【可重入】是什么意思？

获得【锁】 の 【线程】在【释放锁】之前，再次去竞争【同一把锁】 の 时候，不需要【加锁】，就可以【直接访问】

## ReentrantLock 是什么？

ReentrantLock 是一种【可重入】 の 【排他锁】，它 の 核心特性有：

1. 它支持【重入】
2. 它支持【公平】和【非公平】特性
3. 它提供了【阻塞竞争锁】和【非阻塞竞争锁】 の 两种方法，分别为 lock() 和 tryLock()

ReentrantLock の 底层实现有几个非常关键 の 技术：

1. 【CAS机制】：通过【互斥变量】来实现【锁 の 竞争】
2. 【AQS】：用来存储【没有竞争到锁 の 线程】 。当【锁】被释放之后，会从【AQS队列】里面 の head，去唤醒下一个【等待线程】。

## 锁 の 升级 の 目的 ：

尽可能减少`用户态`到`内核态` の 切换

减少锁带来 の `性能消耗`。

使用了`偏向锁`升级为`轻量级锁`再升级到`重量级锁` の 方式，

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

【java虚拟机堆】以外的内存，这个区域是受【操作系统】管理，而不是jvm。

## 堆外内存优点

减少jvm垃圾回收

加快数据复制的速度
