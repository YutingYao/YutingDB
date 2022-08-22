Java面试题解惑-老杨：https://space.bilibili.com/505328909

## 【并行】和【并发】有什么区别？

|   | CPU个数  | 资源占用  |
|---|---|---|
| 并发 - | 【一个CPU】同时处理 多个任务  | CPU 通过【时间片切换】轮流执行不同 の 任务  |
| 并行 = | 【多个CPU or 多核处理器】 同时处理 多个任务  | 两个【线程】互不抢占【CPU资源】，可以同时执行任务  |

## 异步和多线程 の 关系？同步、异步 の 区别？

| 异步  | 同步  |
|---|---|
| 立即响应  | 执行完响应  |
| 优点：解耦  | 优点：实时结果  |
| 缺点：结果延时  | 缺点：等待  |

https://www.bilibili.com/video/BV1C3411A7Dw

[一个视频告诉你“并发、并行、异步、同步”的区别](https://www.bilibili.com/video/BV17V411e7Ua)

同步：【线程】必须先【执行完一个】再执行【下一个】。在发出一个调用时，在没有得到结果时，调用不返回；当调用返回时，立即得到结果。

异步：【主线程】不需要同步等待【另一个线程】 の 完成，也可以继续执行其他任务。在发出一个调用时，调用立即返回，不会立即得到结果，被调用者会通过通知或者【回调函数】来处理结果。

同步与异步的区别主要在于：调用结果是否跟随调用结束后直接返回

【同步】和【异步】一般都是针对【请求的client】和【请求的链接】：

- 比如，【client】有一个【请求】【服务器】返回一个【结果】
- 但是【服务器端】计算这个结果，过程很长
-【同步】：连接就一直【等待】
- 【异步】：我在你那挂了一个钩子，你数据准备好了通过钩子给我。发出结果就立即返回。虽然【client】没有真正拿到返回结果，大表有一个【消息回写的接口】，在结果计算完以后，再把数据给你。

【阻塞】和【非阻塞】往往针对的是【服务端】的【请求线程 + 处理线程】来说的：

这些概念一般出现在【网络io模型】中，

所谓的【同步阻塞】是指【等待】数据的到达，

【同步非阻塞】则需要【轮询】查看数据是否到达

- 比如，【client】查询一个【大数据】。
- 【阻塞】：【请求线程】不能做其他事情，要一致处于【等待状态】
- 【非阻塞】：【请求线程】发出请求给【处理线程】以后，不用一直等待，【请求线程】可以快速释放回到【资源池】去处理其他请求。但这个【请求线程】每个10~20秒，去【轮询】一下【处理线程】，完成以后，就拿着结果到【客户端】

综上所述：

- 【同步】和【异步】是针对【客户端】的【请求连接】来说的
- 【阻塞】和【非阻塞】往往针对的是【服务端】的【请求线程 + 处理线程】来说的
- 同步和异步是一个逻辑概念。表示一次交互要的结果请求方和处理方是否时钟同源。异步的行为是增加吞吐量。并不能节省结果的处理时间。
- 阻塞和非阻塞是在【线程层面】的概念。阻塞和非阻塞是在【线程层面】说的。阻塞与非阻塞的区别：等待这个结果的过程中，【程序】是去做其他事情，还是傻傻地等

两组概念一组合，就出现4种场景，为了解释这4种场景，我们举个栗子：

【顾客|client】到【餐馆吃饭】，【餐馆】有【服务员】和【后端厨师】

对比：【异步】和【多线程】并不是【同等关系】，实现异步 の 方式有很多，【多线程】只是实现异步 の 一种方式

## 【阻塞】&【非阻塞】区别

都是关注【线程状态】

1. 【阻塞】是指：
   - 【结果返回】之前，当前【线程】会被挂起
   - 【调用线程】只有在【得到结果】之后才会【恢复运行】
2. 【非阻塞】是指：
   - 虽然【结果未返回】，当前【线程】不会被挂起

【同步】是【烧开水】过程中，要自己来看，有没有开
【异步】是【烧开水】过程中，水壶响了

【阻塞】是【烧开水】过程中，你不能干其他事，必须在旁边等着
【非阻塞】是【烧开水】过程中，可以干其他事情

## 多线程的异步调用是怎么实现的?

https://www.bilibili.com/video/BV1Tq4y1j7kV

当classA中的client发出一个client请求，classB受到请求，并【构建数据】，【构建数据】是一个耗时的操作，因此需要放到Thread中。虽然【数据】【还没有】【构建】完成，就可以调用Future.set()方法，异步地处理数据，只要将Future返回给classA即可。当我们真正需要【数据】时，只要调用Future.get()方法，即可获取数据。


## Future中是如何异步的get和set数据的？

用`flag`区分`是否获得数据`。

当调用`get方法`时，如果数据没有被`set`进来，则需要`wait一下`，

等到数据真正被`set`进来，flag就会变成`true`。

并且通过`notify`来调用数据，最终把数据`return回去`

## 为什么大厂禁用Java内置线程池

[浅谈线程池](https://www.bilibili.com/video/BV1qr4y1A73N)

https://www.bilibili.com/video/BV1Cd4y1m7aD

**首先，了解一下什么是内置 の 线程池？**

我们常用 の 线程池就是5种，参数方程简单：

```java
public static void main(String[] args) {
      // 固定长度
      Executors.newFixedThreadPool(nThreads:10);
      // 单线程
      Executors.newSingleThreadPool();
      // 可缓存
      Executors.newCachedThreadPool();
      // 用于定时任务
      Executors.newScheduledThreadPool(corePoolSize:10);
      // 工作偷窃算法
      Executors.newWorkStealingThreadPool();
}
```

**这些内置线程池有什么坏处呢？**

newFixedThreadPool 和 newSingleThreadPool

【阻塞】队列】长度是：Integer.MaxValue → 可能积压【大量请求】，造成OOM

newCachedThreadPool 和 newScheduledThreadPool

可以创建 の 【最大线程数】：Integer.MaxValue → 可能创建【大量线程】， **→ 导致**OOM

**自己声明线程池**

```java
public ThreadPoolExecutor (int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
      this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, Executors.defaultThreadFactory(), handler);
}

corePoolSize: pool中 の 【线程数量】
maximumPoolSize: pool中 の 【max线程数量】
keepAliveTime: 多余线程 の 【存活时间】
unit: 时间单位
workQueue: 任务 の 【阻塞】队列】，有个【queueCapacity】：代表【阻塞】队列长度】
ThreadFactory(): 线程工厂，用于【创建线程】，默认即可
handler: 拒绝策略，当任务太多来不及处理，如何【拒绝任务】
```

[解释下Java线程池的各个参数的含义？](https://www.bilibili.com/video/BV1Wt4y1V79D)

## 【线程池】 の 【工作原理】？线程池执行流程？

当提交一个任务到【线程池】，它 の 【工作原理】可以分为【4个步骤】：

1. 【创建ThreadFactory()】【核心线程】corePoolSize
2. 把任务添加到【阻塞】队列workQueue】
   - 如果是【无界队列】，可以继续提交
   - 如果是【有界队列】，且【阻塞】队列workQueue】满了，且【非核心线程数maxPoolSize】没有达到【阈值】
3. 则【创建ThreadFactory()】【非核心线程】增加【处理效率】
4. 如果【非核心线程数maxPoolSize】超时keepAliveTime * unit，则销毁
5. 如果【非核心线程数maxPoolSize】达到了【阈值】，就触发【拒绝策略】

## 如果你提交任务时，线程池队列已满，这时会发生

- 如果是【无界队列】，可以继续提交
- 如果是【有界队列】，且【阻塞】队列workQueue】满了，
  - 【非核心线程数maxPoolSize】没有达到【阈值】，则【创建ThreadFactory()】【非核心线程】增加【处理效率】
  - 【非核心线程数maxPoolSize】达到【阈值】，就触发【拒绝策略】

## 当任务数超过【线程池】 の 【核心线程数】时，如何让它不进入队列？

- 修改【阻塞】队列】 の 类型：
- 比如，Synchronous Queue 。是一个不能存储任何元素 の 【阻塞】队列。
- 它 の 特性在于，每次【生产】一个任务，就必须及时【消费】这个任务。
- 从而避免【任务】进入到【阻塞】队列】，而是直接去【启动】【最大线程数量】去处理

## 我们的线程池中，核心线程是5个，最大线程是10个，这个时候，来了11个任务，线程池是怎么做的？这个时候，来了2个任务，线程池是怎么做的？

再补充几个条件：

- 假设 keepAliveTime 为60秒
- 工作队列为“ArrayBlockingQueue”，有界阻塞队列大小为 4
- handler为默认策略。抛出一个 `threadRejection异常`

---------------------------------

第1时刻：

有一个poolSize维护当前【线程数量】，此时poolSize = 0

第2时刻：

此时来了6个任务，需要创建线程，而 poolSize(0) 小于 corePoolSize(5)，那么【直接创建5个线程】

第3时刻：

此时，6个任务排队，【queue】还没有满，就将任务丢到队列里面去去，队列里面只能放4个任务

第4时刻：

如果队列满了，并且 poolSize < maximumPoolSize，则继续创建新的线程，new创建2个线程，一共7个线程

第5时刻：

如果 poolSize == maximumPoolSize，那么此时再提交一个任务，执行 handler，默认就是抛一个【异常】

第6时刻：

此时线程池里面有7个线程，假设都处于空闲状态，那么根据PoolSize - corePoolSize = 2，则这2个线程在【空闲】超过60秒以后，就会被回收掉





## Java官方提供了哪几种线程池

一共5种：

1. `new Cached Thread Pool`，是一种可以缓存 の 【线程池】，它可以用来处理【大量短期】 の 【突发流量】

特点：

- 最大线程数 = integer.MaxValue
- 存活时间 =  60 秒
- 【阻塞】队列 = Synchronous Queue

2. `new Fixed Thread Pool`：是一种可以【固定线程数量】 の 【线程池】

特点：

- 任何时候最多有 n 个【工作线程】是活动 の 。
- 如果任务比较多，就会加到【阻塞】队列】里面等待；

3. `new Single Thread Executor`：【工作线程】数目被限制为 1，无法动态修改

特点：

- 保证了所有任务 の 都是被【顺序执行】
- 最多会有一个任务处于活动状态；

4. `new Scheduled Thread Pool`：可以进行【定时 or 周期性】 の 工作调度；

5. `new Work Stealing Pool`：Java 8 才加入这个创建方法，可以并行执行

特点：

- 其内部会构建 `fork`JoinPool，
- 利用【Work-Stealing算法】，【并行】地处理任务，不保证顺序；

线程池 の 核心是：

ThreadPoolExecutor()：上面创建方式都是对 ThreadPoolExecutor の 封装。

https://www.bilibili.com/video/BV1e14y1b7pm

## 【大厂面试题】Java线程池 の 拒绝策略有哪些？

当【线程池】满了，【阻塞】队列】也满了，就需要触发解决策略

https://www.bilibili.com/video/BV1oY4y1z7Yy

`AbortPolicy`：（不推荐）(默认)

- 直接抛出异常，阻止系统正常运行

`CallerRunsPolicy`：（可以使用，但性能可能急剧下降）

- 将任务提交给【调用者线程】。

`DiscardOldestPolicy`：

丢弃最老 の 一个请求，也就是，即将被执行 の 任务

`DiscardPolicy`：

丢弃无法处理 の 任务

`自定义`：

拓展`RejectedExecutionHandler接口`

## 为什么要有线程池？

- 【线程池】是一种池化技术，【池化技术】是一种【资源复用】 の 设计思想
- 减少【线程】 の 频繁【创建、销毁】带来 の 【性能开销】
- 【线程池】本身可以通过【参数】来控制【线程数量】，从而保护资源

此外，常见 の 【池化技术】有：

- 省略。。。。。


## 【线程 の 生命周期】由什么？决定

如果是【核心线程】，则生命周期非常长，只要【线程池】存在

如果是【非核心线程】，则由【keepAliveTime】和【任务 の 运行状态】决定

## 【线程池】是如何实现线程复用 の ？

采用【生产者】和【消费者】 の 模式，去实现【线程】 の 复用。

采用一个【阻塞】队列】去解构【生产者】和【消费者】

- 【生产者】不断地产生任务，保存到【阻塞】队列】里面
- 【消费者】不断从【阻塞】队列】消费任务

为了实现【线程 の 复用】：

- 【工作线程】会处于【一直运行】状态。
- 【工作线程】从【阻塞】队列】里面，获取【待执行 の 任务】
  - 一旦队列空了，那么，这个【工作线程】就会被【阻塞】
  - 直到有【new の 任务】进来，【工作线程】又再次被唤醒

## 【阻塞】队列】被【**异步**消费】怎么【保持顺序】吗？

【阻塞】队列是一个符合【FIFO特性】 の 队列。

在【阻塞】队列里面，使用了一个【condition条件等待】，维护了2个【等待队列】：

1. 在【队列为null】时，【存储】被【阻塞】 の 【消费者】
2. 在【队列为full】时，【存储】被【阻塞】 の 【生产者】

子烁爱学习：[生产者/消费者模型](https://www.bilibili.com/video/BV1op4y1S7KK)

【阻塞】队列】 の 【消费过程】有2种情况：

1. 【阻塞】队列】里面已经包含了很多 task，这时候，启动多个【消费者线程】去【消费】
   - 它 の 【有序性保证】是通过【加锁】来实现 の 。
   - 也就是说 ——
   - 每个【消费者线程】去【阻塞】队列】里面获取任务 の 时候，必须先要获得【排他锁 ReentrantLock】

2. 【阻塞】队列为null】时。
   - 这些【消费者线程】按照【FIFO】存入【condition条件等待队列】，
   - 当【阻塞】队列】里面有【任务】时，这些【消费者】会严格按照FIFO の 顺序被唤醒。
   - 从而保证了【消费者】对于task の 处理顺序

## 线程池的阻塞队列，用哪个？ArrayBlockQueue

```java
import java.util.concurrent.ArrayBlockingQueue;
```

<https://www.bilibili.com/video/BV17A4y197em>

用到2个关键技术：

1. 【队列元素】 の 存储：
   - 是【数组结构】
   - 为了达到【循环生产】【循环消费】 の 目 の ，用到了一个【循环数组】
2. 线程 の 【阻塞】 & 唤醒】
   - 用到了J.U.C包里面 の ：
   - ① ReentrantLock
   - ② Condition 条件等待队列：相当于 wait/notify 在J.U.C包里面 の 实现

在原本队列 の 基础上，增加了2个【附加操作】：

1. 在队列为【null】 の 时候，【获取元素 の 线程 Thread2.take()】会等待【队列】变为【非null】
2. 在队列为【Full】 の 时候，【存储元素 の 线程 Thread1.put()】会等待【队列】变为【可用】

由于这样一个特性【阻塞】队列】非常容易实现【生产者、消费者】模型

## 【阻塞】队列】 の 代码实现

```java
class MyBlockQueue<T> {
    private Queue<T> queue = new LinkedList<>();
    public synchronized void put(T obj) {
      //   这里应该要判断一下，队列是否满了
        queue.add(obj);
        notify();
    }

    public synchronized T get() {
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }
        return queue.remove();
    }
}

new Thread(
    () -> {
        while (true) {
            try {
                blockQueue.put(1);
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
        }
    }
).start();
```

## 常见 の 【池化技术】有：

- 线程池
- `字符串常量池`
- 内存池
- 对象池（部分包装类型）

## `静态常量池` & `运行时常量池` & `字符串常量池`区别

https://www.bilibili.com/video/BV1SU4y197LG

`静态常量池`：每个class文件，都有一个对应 の 【class常量池】，【`静态常量池`】也称为【class常量池】，包含2个部分：

- **符号引用**：
- **字面量**：数值型 + 字符型

----------------------------------------------------------------------------------

当【程序】运行起来后：

class就会被【加载】到【运行时内存】里面。

加载以后 の 【内存】会把class里面 の 数据【一分为二】：

`运行时常量池` + `字符串常量池`：

----------------------------------------------------------------------------------

→ 方法区 ：`运行时常量池`：【**直接引用** java/lang/Object@0x000007c00082828】 + 【数值型 **字面量**】

→ heap：【字符型 **字面量**】

- 【数值型 **字面量**】会保存到【`运行时常量池`】
- 【字符型 **字面量**】会保存到【`字符串常量池`】

## 为什么【字符型 **字面量**】会单独保存到【`字符串常量池`】？

【字符型 **字面量**】会在不同 の  class 之间【复用】

## 符号引用 + 直接引用  の 区别

- 【**符号引用** java/lang/Object】

- 【**直接引用** java/lang/Object@0x000007c00082828】

在于，它多了一个【具体 の 内存地址】

## 进程间通信方式

https://www.bilibili.com/video/BV1tv411p7WX

进程之间 の 通信方式：

1. 管道模型：（常用）
分为2类：
   - 匿名管道：将前一个【命令 の 输出】作为后一个【命令 の 输入】，通常【没有名字】，用完就销毁了
   - 命名管道：mkffo，【显式】 の 【创建管道】，【管道】以【文件】 の 形式存在
2. 消息队列模式：生产者-消费者模型
   - 【系统级别】 の 消息队列 → 不太用
   - 【用户级别】 の 消息队列 → 常用
3. 共享内存 + 信号模型（组合使用）：
   - 【线程】是可以访问同一块内存空间 の ，但是【进程】不可以
   - 共享内存： 开辟一片连续 の 存储空间，性能会更好，速度快，copy少
   - 共享内存： 通常和【信号量】一起使用
   - 信号量：解决【互斥访问共享区】 の 问题，使得同一个【共享内存】在同一时间，只能被【同一进程】访问。
4. 信号，不是信号量
   - 上面将 の 都是【常规状态】下 の 【工作模式】，对于一些比较异常 の 状态。
   - 信号：就是【系统】【24小时不间断】地进行【告警】，一旦出现问题，就立即【通知】到【特定系统】
5. socket
   - 用于【网络之间】 の 【消息传输】


## 线程之间如何进行通讯

1. 线程之间可以通过【共享内存】or【网络】来进行【通信】
2. if 通过【共享内存】:
   - 需要考虑并发问题。什么时候【阻塞】？什么时候唤醒？如 wait() 和 notify()
3. if 通过【网络】：
   - 同样需要考虑【并发问题】


## synchronized の 【同步代码块】是如何实现 の ？

它 の 实现是通过：

- 【moniterenter指令】和【monitorexit指令】
- 【moniterenter指令】指向【同步代码块】开始 の 位置，【线程】试图去获取【锁】，也就是获取【monitor持有权】
- 【monitorexit指令】指向【同步代码块】结束 の 位置
- 其内部有一个【计数器】：
  1. 当计数器为0，则可以获取【锁】。获取后，【计数器】加一
  2. 在【monitorexit指令】后，【计数器】减一，代表【锁释放】
- 如果获取【monitor对象锁】失败，那么【当前线程】就要【blocked【阻塞】等待】，直到【锁】被【另一个线程】释放

## wait和notify 为什么要在synchronized代码块中

wait 和 notify 用于【多个线程】之间 の 【协调】:

- wait 表示让【线程】进入【阻塞】状态】，
- notify 表示让【阻塞】 の 线程】被【唤醒】

两种通常同时出现。

synchronized 在 jvm 中它是由 `monitor锁对象` 实现。

我们所谓 の 【wait notify】其实也是 `monitor锁对象` の 方法，

要搞清楚【wait notify】怎么用，就必须搞清楚【`monitor锁对象` の 模型】。

`monitor锁对象`中有:

- *Owner*(锁拥有者)
- *WaitSet* 等待队列
- *EntryList* 【阻塞】队列。

这个对象在【jvm源码】中，

- *wait方法*  将【工作线程】置入*WaitSet*并【释放锁】，
- *notify方法*  把某个在*WaitSet*中 の 【线程】放入 *EntryList* 并【唤醒】。

在进入 有 synchronized 修饰 の 方法时：

1. 当多个【线程】同时访问`synchronized修饰` の 代码时，首先这些【线程】会进入*EntryList* 中。
2. 当【线程1】获取到 `monitor锁对象`后，再执行【moniter enter 指令】。【线程1】进入*Owner*区域
3. 同时 `monitor锁对象 の 计数器`加1。
4. 如果调用*wait方法*，将释放 `monitor锁对象`，再执行【moniter exit 指令】。
5. `monitor锁对象 の 计数器`减1，同时该【线程1】进入*WaitSet*等待调用*notify()*被唤醒，会进入*EntryList* 中。
6. 如果持有锁 の 【线程2】执行完【程序】后也会释放`monitor锁对象`，以便其他【线程】获取`monitor`。

## 线程 の 概念体系？

线程池：多线程处理多个任务（CPU时间片、线程）【阻塞】队列（生产者、消费者）

线程安全：多线程处理同一个【对象】，`synchronized修饰` の 代码 是一个个【task】

## 什么情况【线程】会释放 `monitor锁对象`？

1. 【运行 の 线程】调用*wait方法*，将释放 `monitor锁对象`
2. 【运行 の 线程】执行完【程序】后，将释放 `monitor锁对象`

## 什么情况【线程】会进入*EntryList* 中？

1. 当多个【线程】同时访问`synchronized修饰` の 代码时，这些【线程】会进入*EntryList* 中。
2. *notify方法*  可以把某个在*WaitSet*中 の 【线程】放入 *EntryList* 

## 什么是【互斥】？

互斥条件。【一个资源】只能被【一个线程】占用。

实现互斥可以通过

1. 【锁】：
   - 【锁】就是通过【互斥】来解决【线程安全性】问题。
   - 比如，synchronized の  `monitor锁对象` の 实现，完全是依靠【操作系统】内部 の `互斥锁`，性能很低
2. 【无锁】：
   - AQS使用【int类型】 の 【volatile修饰】 の 【互斥变量state】来表示【锁竞争】 の 状态
   - CAS 实现【多线程】对【共享资源】竞争 の 【互斥性质】

## 【可重入】是什么意思？什么是【可重入锁】能解决什么问题？

可重入锁定义：

https://www.bilibili.com/video/BV1GW4y1z7VH

- 如果【一个线程】抢占了【互斥锁】资源，
- 而在【锁释放】之前，再去竞争【同一把锁】 の 时候 ——→
- 不需要等待。只需要去记录【重入次数】就可以了。

**在【多线程】并发里面，绝大多数 の 锁都是【可重入 の 】**

可重入：

- Synchronized
- ReentrantLock

不可重入：

- 读写锁StampedLock

## 【可重入锁】 の 应用场景：

- 用于避免线程【死锁】。
- 对于已经获得【`互斥锁`X】 の 【线程】，
- 在释放【锁X】之前，再去竞争【锁X】 の 时候，
- 会出现“自己等待自己【释放锁】”，显然是无法成立 の 

## BLOCKED 和 WAITING 有什么区别？

BLOCKED 和 WAITING 都属于【线程等待】状态。

| BLOCKED  | WAITING   |
|---|---|
| 【锁竞争失败】后【**被动触发**】 の 状态   |  【人为】 の 【**主动触发**】 の 状态  |
| 唤醒是【**自动触发**】 の ，【获得锁 の 线程】在【释放锁】之后，会触发唤醒  | 通过【特定方法】【**主动唤醒**】 |
| 没有获得`monitor锁对象` → 在【多个线程】去竞争【synchronized`互斥锁`】，没有竞争到【锁】 の 线程会被【阻塞】等待】，叫做 blocked。  |使用【Object.join() Object,wait() LockSupport.park()】进入到一个【Waiting 状态】   |
| 【线程】【阻塞】于synchronized锁，获得`monitor锁对象` → 【Runnable】  |【进入该状态 の 线程】需要等待【其他线程】 の 【特定操作】 【Object.notify() LockSupport.unpark()】才会被唤醒。   |


## synchronized【同步代码块】和【方法】分布是如何实现 の ？

synchronized  の 实现使用 の 是 monitorenter 和 monitorexit 指令，

- monitorenter 指令指向【同步代码块】 の 开始位置，
- monitorexit 指令则指明【同步代码块】 の 结束位置。

synchronized 修饰 の 方法并没有【moniterenter指令】和【monitorexit指令】，

取得代之 の 是 `ACC_SYNCHRONIZED标识`”，

JVM 通过该 `ACC_SYNCHRONIZED标识` 来辨别一个method是否声明为【同步方法】


## synchronized  为什么要进行锁升级

在 Java 6 之前，`monitor锁对象`  の 实现完全是依靠【操作系统】内部 の `互斥锁`

- `互斥锁`是【系统方法】，需要进行`用户态`到`内核态` の 切换，所以性能很低: 

但在 Java 6  の 时候，Java 虚拟机提供了三种不同 の  `monitor锁对象` 实现，也就是【锁升级机制】：


## 多线程中 synchronized 锁升级 の 原理是什么？

synchronized 锁升级原理：

在`锁对象` の `对象头`里面有一个 `threadid 字段`，

在第一次访问 の 时候 `threadid 为空`，jvm 让其持有`偏向锁`，

并将 `threadid 字段` 设置为其`线程 id`，

再次进入 の 时候会先判断 `threadid 字段` 是否与其`线程 id` 一致，

如果一致, 则可以直接使用此对象，

如果不一致，则升级`偏向锁`为`轻量级锁`，通过`自旋`一定次数来获取锁，

执行一定次数【50~100次】之后，如果还没有正常获取到要使用 の 对象，

此时就会把锁从`轻量级锁`升级为`重量级锁`，此过程就构成了 synchronized 锁 の 升级。

| 锁  | 特征  |
|---|---|
| 偏向锁  | 最轻量。在`锁对象` の `对象头`里面有一个 `threadid 字段`，在第一次访问 の 时候 `threadid 为空`，jvm 让其持有`偏向锁`。 如果存在【多线程竞争】，就会膨胀为【轻量级锁】。 CAS |
| 轻量级锁（自旋锁）  | 【多线程】【交替执行】【同步代码】，而不是【阻塞】。线程在获取【锁】 の 过程中，不会去【阻塞】线程】，也就无所谓【唤醒线程】。【阻塞】-唤醒】是需要【操作系统】去执行 の ，比较【耗时】。通过`自旋CAS`一定次数来获取锁，执行一定次数【50~100次】之后，如果还没有正常获取到要使用 の 对象，此时就会把锁从`轻量级锁`升级为`重量级锁`  |
| 重量级锁  | 在【多线程竞争【阻塞】】时，线程处于【blocked】，处于【锁等待】状态下 の 线程，需要等待【获得锁】 の 线程【释放】锁以后，才能【触发唤醒】，会进行`用户态`到`内核态` の 切换  |

[幼麟实验室 - 实现自旋锁](https://www.bilibili.com/video/BV1Sf4y1s7Np)

## Synchronized の 锁升级

默认采用【偏向锁】：在程序运行过程中，只有一个线程去获取【锁】，会记录一个【线程ID】。所以在下次获取【锁】 の 时候，需要去比较【线程ID】。

在运行过程中，如果有【第二个线程】去请求【锁】 の 时候，就分为2种情况：

1. 在没有发生【并发竞争锁】 の 情况下，这个【偏向锁】就会自动升级为【轻量级锁】。在这个时候，第二个线程就会尝试【自旋】 の 方式来获取锁。因为很快就能拿到锁，所以【第二个线程不会阻塞】
2. 但是如果出现【两个线程】竞争锁 の 情况，这个【偏向锁】就会自动升级为【重量级锁】。此时，只有一个【线程】获取到【锁】，另一个线程就会【阻塞】，等待第一个【线程】释放【锁】以后，才能去获得锁


## 说一下 synchronized 底层实现原理？

synchronized 就是锁住【对象头】中【两个锁】【标志位】 の 【数值】


## 锁 の 升级 の 目 の  ：

尽可能减少`用户态`到`内核态` の 切换，从而减少锁带来 の `性能消耗`。

使用了`偏向锁`升级为`轻量级锁`再升级到`重量级锁` の 方式，

## sleep() 和 wait() 有什么区别？wait和sleep是否会触发锁 の 释放以及CPU资源 の 释放？

1. 都能使当前线程暂停
2. 【any线程】在调用【wait和sleep】之后，在等待期间被中断都会抛出InterruptedException

```java
public static void sleep() throws InterruptedException;
```

https://www.bilibili.com/video/BV1PS4y1q7T1

| Object.wait()  | Thread.sleep()  |
|---|---|
| 让一个线程，进入【阻塞】状态】 | 让一个线程，单纯地进入到一个睡眠状态 |
| 会释放【锁资源】以及【CPU资源】  | 不会释放【锁资源】，但会释放【CPU资源】  |
| 来自 Object | 来自 Thread  |
| wait() 可以使用 notify()/notifyAll()直接唤醒  | sleep() 时间到会自动恢复  |
| 需要先获取【对象】   |  直接Thread.sleep()  |
| 必须写在【Synchronized同步代码块】里面 | 没有强制要求加【synchronized`互斥锁`】 |
| 因为【wait & notify】是基于【共享内存】来实现【线程】与【线程】之间【通信】。所以，在调用【wait & notify】之前，它必须要【竞争锁资源】，从而去实现条件 の 【互斥】。wait 方法 必须要要释放锁，否则就会【死锁】 | 不会触发【锁 の 释放】 |




## notify()和 notifyAll()有什么区别？

| `notifyAll()` | `notify()`  |
|---|---|
| 唤醒所有 の 线程  | 唤醒一个线程  |

`notifyAll()` 调用后，会将【all线程】由`等待池`*WaitSet*移到`锁池`*EntryList*，然后参与`锁` の 竞争，

- if 竞争成功 then 继续执行，

- if 不成功 then 留在`锁池`*EntryList* then 等待`锁`被`释放`后, 再次参与竞争。

`notify()`只会唤醒一个线程，具体唤醒哪一个线程由虚拟机控制。


还有一点是基本不会用`notify()`去唤醒, 推荐使用`notifyAll()`,

因为`notify`是唤醒某个指定线程，你不知道这个线程到底是哪个，在`多线程情况`下，使用推荐使用`notifyAll()方法`.



## 线程 の 状态转化

1. 【new】 → start →【Runnable】

- 【Runnable】→ sleep、join(T)、wait(T)、locksupport.parkNanos(T)、locksupport.parkUntil(T)→ 【Timed Waiting】→ 时间到、unpark→【Runnable】

- 【Runnable】→ join、wait、locksupport.park → 【Waiting】→ notify、notifyAll → 【Runnable】

- Synchronized  → 没有获得`monitor锁` → 【Blocked】 → 获得`monitor锁` → 【Runnable】

2. 【Runnable】 → 【terminated】

## 线程有哪些状态？

[Java中线程 の 状态包括那哪一些？](https://www.bilibili.com/video/BV1Lr4y1575y)

[【大厂面试题】Java线程有哪六种状态](https://www.bilibili.com/video/BV1V44y137yD)

线程 の 6种状态：

`初始(NEW)`：新创建了一个【线程对象】，但还没有调用`start()方法`。

`运行(RUNNABLE)`：这个状态下，线程可能【就绪（ready）】或者【运行中（running）】

- 【线程对象】创建后，放入【RUNNABLE线程池】中，等待被【线程调度】选中，从而获取【CPU の 使用权】，此时处于【就绪状态（ready）】。

- 【就绪状态】 の 线程在获得【CPU时间片】后变为运行中【状态（running）】。

`【阻塞】(BLOCKED)`：【线程】【阻塞】于【锁】，处于锁等待状态。

`等待(WAITING)`：需要等待【其他线程】触发条件后【唤醒】，如 wait & notify。

`超时等待(TIMED_WAITING)`：该状态不同于`WAITING` の 是，它可以在指定 の 时间后【自行返回】。

`终止(TERMINATED)`：表示该线程已经【执行完毕】。


## 举个【线程不安全】 の 椰子🥥？

```java
public class Test {
    private static int count;
    private static class Thread1 extends Thread {
        public void run() {
            for (int i = 0; i < 1000; i++) {
                count ++;
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread1 t1 = new Thread1(); `初始(NEW)`
        Thread1 t2 = new Thread1();
        t1.start(); `运行(RUNNABLE)`
        t2.start();
        t1.join(); `等待(WAITING)`
        t2.join();
        System.out.println(count);
    }
}

输出结果不是2000，而是一个比2000略小 の 数字，而且每次运行结果都不同

这就是【线程不安全】 **→ 导致** の 
```

[Thread.join()的使用](https://www.bilibili.com/video/BV1W5411K7YC)

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


## 【很重要】如何停止一个正在运行 の 线程？

1. 用`stop方法`，不过该方法已经被废弃，不推荐使用。（不安全 の ）
   - 因为使用`Stop方法`，会一直向上传播ThreadDeath异常，
   - 从而使得【目标线程】【释放】掉所有 の 【锁】，使
   - 可能造成【数据不一致】
   - **总 の 来说，有可能【线程 の 任务】还没有完成，突然中断，会 **→ 导致**运行结果不一致。**
   - 比如，HashMap，扩容到一半，停了，所以数据结构错乱
   - 比如，读写IO，没有close，就一直占用内存， **→ 导致**【内存泄漏】
2. 用`interrupt方法`：
   - **【安全中断】 の 话，只能在线程内部埋下一个【钩子】：**
   - **外部【程序】，通过这个【钩子】来触发对【线程】中断 の 命令。**
   - **不是强制中断，而是告诉【正在运行 の 线程】，你可以停止了**
   - **interrupt()方法 + isInterrupted()方法 一起使用**
   - `Thread.currentThread().isInterrupted()方法`可以用来判断【当前线程】是否被终止
   - 通常【`isInterrupted()方法`】返回True の 话，会抛出一个【中断异常】，然后通过【try-catch捕获】
3. 设置【标志位stopFlag】：
   - 当【标志位】为【某个值】时，使【线程】正常退出
   - 【标志位】可以使用【volatile修饰】or【原子类】，保证【共享变量】在内存中 の 可见性
   - 但是使用【volatile修饰】or【原子类】，在【线程【阻塞】】时，是无法响应 の 。
   - 比如，调用【Thread.sleep()方法】之后，线程处于【不可运行状态】。
   - 即使【主线程】修改了【共享变量】 の 值，【阻塞】 の 线程】此时根本无法检测出【标志位】，所以也就无法实现【线程中断】

综上所述，【interrupt + 手动抛出异常】 の 方式，是目前中断一个正在运行 の 线程最正确 の 方式

## 当一个线程，调用interrupt方法是， の 两种情况：

1. 如果线程处于【阻塞】BLOCKED|WAITING状态】，例如，处于sleep、wait、join。那么【线程】立即退出【阻塞】BLOCKED|WAITING状态】，并且抛出【InterruptedException异常】
2. 如果线程处于【RUNNABLE状态】，那么会将【线程】 の 【中断标志】设置为 True，仅此而已，该线程仍然可以继续正常运行。

## volatile 中断一个正在运行中 の 线程？

```java
private static volatile boolean stopFlag = false;

public static void main(String[] args) {
      Thread t1 = new Thread (
            () -> {
                  while (true) {
                        if (stopFlag) {
                              System.out.println("线程被中断了")
                              break;
                        }
                        System.out.println("线程没有被中断")
                  }
            },
      name:"t1") ;

      t1.start();

      try {
            TimeUnit.SECONDS.sleep(timeout: 1);
      } catch (InterruptedException e) {
            e.printStackTrace();
      }

      new Thread (
            () -> {
                  stopFlag = true;
            },
      name:"t2").start();
}
```

## AtomicBoolean 中断一个正在运行中 の 线程？

```java
private static AtomicBoolean atomicBoolean = new AtomicBoolean(initialValue: false);

public static void main(String[] args) {
      Thread t1 = new Thread (
            () -> {
                  while (true) {
                        if (atomicBoolean.get()) {
                              System.out.println("线程被中断了")
                              break;
                        }
                        System.out.println("线程没有被中断")
                  }
            },
      name:"t1") ;

      t1.start();

      try {
            TimeUnit.SECONDS.sleep(timeout: 1);
      } catch (InterruptedException e) {
            e.printStackTrace();
      }

      new Thread (
            () -> {
                  atomicBoolean.set(true);
            },
      name:"t2").start();
}
```

##  interrupt + isInterrupted 中断一个正在运行中 の 线程？

```java
public static void main(String[] args) {
      Thread t1 = new Thread (
            () -> {
                  while (true) {
                        if (Thread.currentThread().isInterrupted()) {
                              System.out.println("线程被中断了")
                              break;
                        };
                        System.out.println("线程没有被中断")
                  }
            },
      name:"t1") ;

      t1.start();

      try {
            TimeUnit.SECONDS.sleep(timeout: 1);
      } catch (InterruptedException e) {
            e.printStackTrace();
      }

      new Thread (
            () -> {
                  t1.interrupt();
            },
      name:"t2").start();
}
```

## interrupt + isInterrupted 中断一个正在运行中 の 线程？

```java
class Task extends Thread {
    @Override
    public void run() {
        while (true) {
            if (this.isInterrupted()) { // 中断信号判断
                System.out.println(Thread.currentThread().getName() + "线程被中断了");
                break; // 跳出循环，run方法执行结束，从而终止线程
            } else {
                System.out.println("线程没有被中断");
            }
        }
    }
}

public class InterruptExample {
    public static void main (String[] args) throws InterruptedException {
        Task t = new Task();
        t.start();
        Thread.sleep(500);
        t.interrupt(); // 调用interrupt，给thread发送一个中断信号
        System.out.println("线程中断了，【程序】到这里了")
    }
}
```

## 如何处理【线程安全】问题？

1. 如果涉及到【操作 の 原子性】，则可以使用【atomic包】下面 の 【原子类】
2. 如果涉及到【操作 の 原子性】，则可以使用【自旋】保证【原子操作】
3. 如果涉及到【线程 の 控制】，则可以使用【线程工具类】下面 の 【CountDownLatch | Semaphore】
4. 如果涉及到【线程 の 控制】，则可以使用【集合类J.U.C】下面 の 【concurrent包】
5. 如果涉及到【线程 の 控制】，则可以使用【Synchronized关键词、ReentrantLock接口】，使线程互斥同步
6. 如果涉及到【线程 の 控制】，则可以使用【volatile关键字】，性能更好，只适用于赋值场景
7. 如果涉及到【多机环境】，则可以使用【分布式锁】

## 说一下 atomic  の 原理？

【原子操作】【无锁 lock-free】实现了【无锁 の 线程安全】

使用`java.util.atomic`提供 の `原子操作`可以简化【多线程编程】，包含了：

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
| 更底层  | 基于 **CAS(原子性) + volatile(有序性) + native** 方法  |
| 可以确保【有序性】，即【写操作】发生在【后续读操作】之前，但不能保证【原子性】  | 具有【原子性】  |
| 如 i++  | 如 getAndIncrement 方法  |
| 适用于【赋值】  | 适用于【计数器、累加器】  |

## 使用 AtomicLong 实现【线程安全】 の 【ID序列生成器】

```java
class IdGenerator {
   AtomicLong val = new AtomicLong(0);

   public long getNextId() {
      return val.incrementAndGet();
   }
}
```

## 使用 AtomicInteger 实现【线程安全】 の 【Counter】？

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

## 什么是CAS（无锁同步）（乐观锁 の 实现）

https://www.bilibili.com/video/BV1EB4y1D7P2

1. 背景：
   - 在【多线程】环境下，会存在【原子性问题】。
   - 虽然，可以加一个【synchronized `互斥锁`】，但是，加【`互斥锁`】一定会带来性能上 の 损耗。
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
   - 【线程A】 の 【预期】是将 0 变更为 1。
   - CAS 会比较【内存地址偏移量stateOffset 对应 の 值】，和【预期值 0 】是否相等：
     - 如果不等，就【什么都不做】并【返回false】。
     - 如果相等，就直接修改【内存地址】中【state の 值】为 1.

6. CAS 是如何保证原子性 の ？
   - CAS 本质上，还是从【内存地址】中——读取，比较，修改。
   - 这个过程无论在什么层面实现，都会存在【原子性】问题。
   - 所以，**在 CAS 底层实现上，会增加一个【lock指令】去对【缓存】加【锁】**。
   - 从而保证了【比较、替换】这个两个操作 の 原子性。

7. 【compareAndSwapInt()方法】有4个参数，分别是：
   - 当前对象实例
   - 成员变量state 在【内存地址】中 の 【偏移量】
   - 预期值 0
   - 期望更改之后 の 值 1

-----------------------------------

CAS  の 典型应用场景有2个：

1. 原子类：J.U.C里面 の  Atomic  の 【原子实现】，如：
   - AtomicInteger  の getAndIncrement 方法，就是通过CAS实现 の 
   - AtomicLong
2. 实现【多线程】对【共享资源】竞争 の 【互斥性质】，如：
   - ReentrantLock内部 の AQS
   - ConcurrentHashMap（通过【CAS机制】实现元素个数 の 【累加】）
   - ConcurrentLinkedQueue
   - 实现synchronized中 の 【偏向锁、轻量级锁】

## 【pass💦】为了更好 の 控制【主内存】和【本地内存】 の 交互，Java 内存模型定义了八种操作来实现

- lock：锁定。 主内存 の 变量，把一个变量标识为一条线程独占状态。
- unlock：解锁。 主内存变量，把一个处于锁定状态 の 变量释放出来，释放后 の 变量才可以被其他线程锁定。
- read：读取。 主内存变量，把一个变量值从主内存传输到线程 の 【工作内存】中，以便随后 の load动作使用
- load：载入。 【工作内存】 の 变量，它把read操作从主内存中得到 の 变量值放入【工作内存】 の 变量副本中。
- use：使用。 【工作内存】 の 变量，把【工作内存】中 の 一个变量值传递给【执行引擎】，每当虚拟机遇到一个需要使用变量 の 值 の 字节码指令时将会执行这个操作。
- assign：赋值。 【工作内存】 の 变量，它把一个从【执行引擎】接收到 の 值赋值给【工作内存】 の 变量，每当虚拟机遇到一个给变量赋值 の 字节码指令时执行这个操作。
- store：存储。 【工作内存】 の 变量，把【工作内存】中 の 一个变量 の 值传送到主内存中，以便随后 の write の 操作。
- write：写入。 主内存 の 变量，它把store操作从【工作内存】中一个变量 の 值传送到主内存 の 变量中。

## 【很重要】如何在不加锁 の 情况下解决线程安全问题？

这个问题有3个方面：

① 所谓 の 【线程安全】问题，其实是指：

- 【多个线程】同时对【某个共享资源】 の 访问 ， **→ 导致** 【原子性、可见性、有序性】 の 问题。

② 解决【线程安全】问题 の 方式是【增加】`互斥锁`，常见 の 是像【synchronized、lock】等。

- 对【共享资源】加锁以后，多个【线程】在访问这个资源 の 时候，必须要先获得【锁】
- 而【`互斥锁`】 の 特征是：
- 在同一个时刻，只允许一个线程，访问一个资源，直到【锁】被释放。
- `互斥锁`是【系统方法】，需要进行`用户态`到`内核态` の 切换，所以性能很低。

③ 如何在【性能】和【安全性】之间取得一个balance。这就引出了一个【无锁并发】 の 概念，一般来说，会有以下几种方法：

1. 乐观锁：
   - 认为【并发写】的可能性低，所以不会上锁，而是在【写】的时候【判断一下】有没有被更新过。
   - 基本都是采用 CAS 实现的。CAS 是一种原子操作，本质上还是对【缓存】的【lock指令】
   - 常用的就是 J.U.C里面 の  Atomic  の 【原子实现】
   - 在【数据库】中，采用 version 的形式。在更新的时候，将【查出的version】与【数据库的version】进行比较，如果version一致则更新，如果不一致则重试，
   - 乐观锁适用于：冲突记录小
2. volatile - 内存屏障

<!-- 1. 自旋锁：指篇【线程】在没有【抢占 の 锁】 の 情况下，先【自旋】指定 の 次数，去尝试获得【锁】。
1. 乐观锁： 给每个数据增加一个【版本号】，一旦数据发生变化，则去修改这个版本号。CAS の 机制，可以完成【乐观锁】 の 功能。
2. 在【程序】设计中，尽量去减少【共享对象】 の 使用。从业务上去实现【隔离】避免【并发】。 -->


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


## CAS的ABA问题如何解决？

线程1 和 线程2 同时取出A

线程1 将 A 变成 B，又将 B 变成 A

线程2 操作时，发现内存中，仍然是 A，所以操作成功

--------------------------------------------

可以用【version版本号】来解决ABA的问题。

乐观锁在执行【数据修改】时，会带上一个【version版本号】，

一旦【version版本号】一致，则可以进行修改，并对【version版本号 + 1】

否则就执行失败

## 【大厂面试题】乐观锁会 **→ 导致**线上崩盘吗？

https://www.bilibili.com/video/BV1JW4y1S7SL

乐观锁，如果变更失败，就会while循环，来保证成功。

如果同时有【大量变更】，就会产生【大量争抢】，以及【不断重试】，

从而达到【数据库事务】链接上限

其他【程序】获取不到【事务连接】

→ 可以改为【消息驱动 + 分布式锁】，来缓解压力

## synchronized 和 Lock 有什么区别？

https://www.bilibili.com/video/BV1sB4y1R7Lw

1. 性能区别
| synchronized  |  lock |
|---|---|
| 是【方法关键字】  |  J.U.C包中 の 【接口】 |
| 底层是【JVM层面】 の 锁  |  底层是【API层面】 の 锁 |

2. 用法区别
| synchronized  |  lock |
|---|---|
| 锁升级  |  可以设置【超时时间】来获取锁 |
| 无法实现【非【阻塞】竞争锁】  |  提供了【非【阻塞】竞争锁】tryLock()。如果获得【锁】则返回【True】，否则返回【False】 |
|  可以给【类、方法、代码块】加锁  | 只能给代码块加锁  |
| 【手动】获得锁，释放锁  | 【自动】获得锁，释放锁     |
| 【发生异常】会自动释放锁，不会造成死锁  |  如果使用不当，没有 unLock()去释放锁就会造成【死锁】 |

3. 原理区别
| synchronized  |  lock |
|---|---|
| 原理：悲观锁机制，锁住【对象头】，有个【锁升级】 の 过程  |  原理：乐观锁机制，CAS自旋锁 |

4. 用途区别
| synchronized  |  lock |
|---|---|
| 非公平锁  | 【公平锁、非公平锁】，可选择 |
| 适用于【线程少，竞争不激烈】  |  适用于【线程多，竞争激烈】 |
| 无法中断  |  可以中断 |


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


## 为什么CAS一定要`volatile变量`配合？volatile の 应用场景

volatile 适合用在一个【变量】被多个【线程】共享，【线程】直接给这个【变量赋值】 の 场景

CAS就是这样一个场景，比较-替换

volatile 能保证【每次拿到 の 变量】是`主内存`中`最新 の 那个值`

## 怎么判断【线程池 の 任务】是不是执行完了？

1. 使用【线程池】 の 【原生函数 isTerminated()】
   - executor 提供了一个【原生函数 isTerminated()】来判断【线程池】中 の 任务是否完成
   - if 全部完成返回 True，否则返回 false

2. submit向【线程池】提交任务，使用Future判断【任务执行状态】
   - 使用【submit】与【execute】不同，submit会有【Future类型】 の 【返回值】
   - 【Future.isDone()方法】可以知道【任务是否执行完成】

下面两种方法缺点在于——【需要提前知道任务数量】：

1. 使用【重入锁】，维持一个【公共计数】
   - 所有任务维持一个【计数器】。当任务完成时，【计数器】加一（这里要加锁）
   - 当【计数器 の 值】等于【任务数】时，任务执行完毕
2. 使用CountDownLatch
   - 它 の 原理类似【方法2】
   - 给CountDownLatch一个【计数值】，任务执行完毕后，调用【countDown()】执行计数值减一


## Future接口：

- 用于描述一个**异步**计算的结果
- 虽然 Future 以及相关使用方法提供了**异步**执行任务的能力，
- 但是对于结果的获取却是很不方便，只能通过【阻塞】或者【轮询】的方式得到任务的结果。

**Future模式：**

- 是【多线程设计】常用的一种【设计模式】。

**Future模式可以理解成：**

- 我有一个任务，提交给了Future，Future替我完成这个任务。期间我自己可以去做任何想做的事情。一段时间之后，我就便可以从Future那儿取出结果。

**Future提供了三种功能：**

- 判断任务【是否完成】
- 能够【中断任务】
- 能够【获取】【任务执行的结果】

**【阻塞】**

向线程池中提交任务的`submit方法`不是【阻塞】方法

而`Future.get方法`是一个【阻塞】方法

当`submit`提交多个任务时，只有`所有任务都完成`后，才能`使用get`按照任务的提交顺序得到返回结果

所以一般需要使用`future.isDone`先判断任务是否全部执行完成，

完成后再使用`future.get`得到结果。

but使用`isDone()`轮询地判断Future是否完成，这样会耗费CPU的资源。

## 【大厂面试题】为什么线程池 の submit不抛出异常？

`submit() 方法`：它提供了一个【Future  の `返回值`】，

我们可以通过【Future.get() 方法】去获得【任务 の 执行结果】。

当任务没有完成之前，【Future.get() 方法】将一直【阻塞】，直到任务执行结束。

只要【Future.get() 方法】正常返回，则说明任务完成。

根本原因：FutureTask执行不会直接抛出异常，这个异常会被【吞掉】

解决方法：调用futuretask的get方法，获取异常信息

https://www.bilibili.com/video/BV1y44y1A7mr

`testThread方法`

- 是一个callable
- 对于callable:
  1. 如果【不需要】返回值，应该使用execute。
  2. 如果【需要】返回值，那一定是submit，而且你后面也一定会调用future.get，错误也应该在这个时候返回给调用者。

**正确的调用方式**也应该是这样:

- 无所谓runnable还是callable：
  1. 【不需要】返回值，就execute
  2. 【需要】就submit。


## 什么是**异步**调用：

- 可无需等待被调函数的返回值
- 让操作继续运行

## Java多线程 の Future模式

核心思想 - 【`异步调用`】。

Future 模式是【多线程并发】中常见 の 【设计模式】，

当【client】发送请求时，【服务端】不能立刻响应【客户端】需要 の 数据，就响应一个【虚拟】 の 【数据对象】，这时，【客户端】就不会【阻塞等待】，就可以【异步】地做其他工作。异步

当【client】真正需要【请求 の 数据】时，服务端再发送真实 の 数据给【client】。

这就是【Future 设计模式】

## 什么场景使用【多线程】？什么场景使用【异步】？

一些【并发大】 の 场景，比如，读入大量文件到【数据库】，使用【多线程】可以极大地提高效率

【耗时比较长】 の 任务，比如，发送邮件，可以使用【异步线程】处理



## CompletableFuture の 理解

https://www.bilibili.com/video/BV1nA411g7d2

- 实现了Future和CompletionStage两个接口
- CompletionStage：定义了【任务编排】的方法
- CompletableFuture最大的改进：提供了类似于【观察者模式】的【回调监听】功能，也就是当【上一阶段任务】执行结束之后，可以【回调】【下一阶段任务】，而不需要【阻塞，获取结果】之后，再去处理结果
- 可以基于CompletableFuture创建任务和链式处理多个任务，并实现按照`任务完成`的`先后顺序`获取任务的结果
- 不论`Future.get()方法`还是`CompletableFuture.get()方法`都是【阻塞】的

弥补了原本Future の 不足，使得【程序】可以在【非【阻塞】状态】下，完成【**异步**】【回调机制】

<https://www.bilibili.com/video/BV1hA4y1d7gU>

是基于【事件驱动】 の 【**异步**回调类】。

当前使用【**异步**线程】去执行任务时，我们希望在这个【任务结束】以后，还能触发一个【后续操作】

它提供了5种方式，把多个【**异步**任务】组成一个具有【先后关系】 の 【处理链路】

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
   - task1 完成后，触发【实现 Runnable 接口】 の  task
   - 无返回值

CompletableFuture有诸多方法，总结来讲就有4种关系：

1. 串行关系
2. 并行关系
3. 聚合关系
4. 异常关系

可以应用在【用户注册 → 发送红包 → 发送短信】的业务场景

用【2个线程】去查询【股票价格】，只要有一个线程查询到结果，就立即返回

用【n个线程】去查询【数据库】，只要有一个线程查询到结果，就立即返回

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

```java
public static void main (String[] args) throws Exception { 
    ExecutorService es = Executors.newSingleThreadExecutor();
    Runnable rnb = new Runnable() {
        @Override
        public void run() { 逻辑 };
    }
    // 1️⃣4️⃣ Runnable + Future 有返回值
    Future<?> submit1 = es.submit(rnb);
    submit.get();

    // 2️⃣ Runnable 无返回值
    es.execute(rnb);

    // 3️⃣ Callable + Future 有返回值
    Callable<String> sc = new Callable() {
        @Override
        public Object call() throws Exception {
            逻辑
            return null;
        }
    };
    Future<String> submit2 = es.submit(sc);
    submit2.get();
}

```

## Thread 和 Runnable  の 区别

| Thread  | Runnable  |
|---|---|
| 类  | 接口  |
| 支持【单一继承】  | 可以支持【多继承】  |
|   | 已经存在【继承关系】 の 【类】  |




## CycliBarriar和CountdownLatch有什么区别？

都能实现【线程】之间 の 【等待】

侧重点不同：

| CycliBarriar   | CountdownLatch  |
|---|---|
| 可以重复使用  | 不可重复使用  |

## CountDownLatch

可以看做是【计数器】：

1. 这个【计数器】的操作都是【原子操作】
2. 同时，只能有一个线程去操作这个【计数器】
3. CountDownLatch 无法被重置

方法：

1. `await()方法`：可以带有【超时时间】，也可以没有【超时时间】
2. `countDown()方法`：减1
3. `getCount()方法`：获得计数器的值

工作原理：

1. 向 `CountDownLatch对象` 设置一个【`初始值`】
2. 任何调用这个对象上的`await()方法`都会【阻塞】，
3. 其他线程可以调用`countDown()方法`对`CountDownLatch对象`中的数字减1
4. 直到这个`计数值`被其他线程减为 0。会唤醒所有`await等待的线程`
5. 然后`被唤醒的线程`就可以继续往下执行。


**await()方法**工作原理：

1. 调用`await()方法`的线程会利用**AQS排队**，一旦数字被减为0，则会将**AQS中排队**的线程依次唤醒

## CountDownLatch使用场景

1. 等待N个线程完成各自的任务后，进行【汇总合并】
2. 同时启动多个线程
   - 创建一个计数值为1的`CountDownLatch对象`
   - 【N个线程】在开始执行任务前，都`await()方法`【阻塞】
   - 【主线程】调用`countDown()方法`
   - 【N个线程】同时恢复执行

## Semaphore 信号量

表示，最多允许【多少个线程】使用该信号量

- aquire()【阻塞】竞争：获取许可 `许可AVAILABLE -= 1`，如果没有获取许可，则可以通过AQS来排队
- release()：释放许可 `许可AVAILABLE += 1`，AQS中正在排队的线程，依次唤醒
- tryAquire()非【阻塞】竞争：尝试获取许可，若获取成功，则立即返回true，否则返回false
- availablePermits()：获取【信号量】中当前可用的【许可数目】

有【公平】和【非公平】

## Semaphore 信号量 の 使用场景

可以用来做限流：

10辆车【10个线程】进入停车场，有5个停车位【5个许可】

每个【线程】需要先获取到【许可】才能执行，最多只允许【n个线程】同时执行

【其他线程】【阻塞】在那里，等待【获取令牌】

当线程执行完以后，把【许可】还回去，【阻塞】线程】才能获取【许可】

```java

import java.util.concurrent.Semaphore;

public class Main {
    private static final int MAX_AVAILABLE = 5;
    public static void main(String[] args) {
        // 有5个停车位
        Semaphore semaphore = new Semaphore(MAX_AVAILABLE);
        // 模拟10辆车进入停车场
        for (int i = 1; i <= 10; i++) {
            new ParkingLot(semaphore, i).start();
        }
    }
}
class ParkingLot extends Thread {
    private Semaphore semaphore;
    private int num;

    public ParkingLot(Semaphore semaphore, int num) {
        this.semaphore = semaphore;
        this.num = num;
    }

    @Override
    public void run() {
        try {
            // 汽车进入停车场，需要获得一个许可
            semaphore.acquire();
            System.out.println("第" + this.num + "辆车in停车场");
            Thread.sleep(1000);
            System.out.println("第" + this.num + "辆车out停车场");
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

第1辆车in停车场
第3辆车in停车场
第4辆车in停车场
第5辆车in停车场
第2辆车in停车场
第4辆车out停车场
第5辆车out停车场
第10辆车in停车场
第2辆车out停车场
第9辆车in停车场
第6辆车in停车场
第3辆车out停车场
第1辆车out停车场
第7辆车in停车场
第8辆车in停车场
第7辆车out停车场
第10辆车out停车场
第9辆车out停车场
第6辆车out停车场
第8辆车out停车场
```


## AQS为什么要使用双向链表？

https://www.bilibili.com/video/BV1DW4y127DE

【双向链表】有2个指针：

- 一个指向【前驱节点】
- 一个指向【后驱节点】

所以，支持【常量级别】 の 【时间复杂度】，去找到【前驱节点】。

AQS使用双向链表有3个方面 の 原因：

1. 没有竞争到【锁】 の 【线程】加入到【阻塞】队列】 の 前提是，当前所在节点 の 【前置节点】是一个【正常状态】。所以需要判断【前驱节点】 の 状态，如果没有【指针】指向【前驱节点】，我们就需要【从头部节点】开始遍历。
2. 线程可以通过【`interrupt()方法`】触发【`中断`】。而这个时候，被【`中断`】 の 线程 の 状态会被修改成【`Cancelled状态`】，被标记为【`Cancelled状态`】 の 线程是【不需要竞争锁】 の ，但是它仍然存在于【整个`双向链表`里面】，在后续，需要把这个【Cancelled状态】 の 节点从【链表里面`移除`】，如果没有【指针】指向【`前驱节点`】，我们就需要【从头部节点】开始遍历。
3. 按照【公平锁】  の 设计，只有【头结点】 の 【下一个节点】才有必要竞争锁，其余节点都去竞争锁 の 话，会造成【惊群效应】。所以，需要判断【前驱节点】是不是【头节点】，如果不是【头节点】就没有必要去触发【锁 の 竞争】，所以会涉及到【前驱节点】 の 【查找】，如果是【单向链表】是无法实现这样一个功能 の 

## 【生产者】和【消费者】 の 模式

实现【多线程】并发协作

【生产者】只需要生产数据

【消费者】只需要消费数据

## 什么是 AQS

英文全称：AbstractQueuedSynchronizer

是【多线程同步器】

是J.U.C包中，多个组件 の 【底层实现】

包括：

- lock
- countdownlatch

AQS 提供了2种锁机制：

- 排它锁（独占资源）
- 共享锁（共享资源）

排他锁：写锁。同一时刻，只允许【一个线程】去访问【共享资源】。如ReentrantLock

共享锁：读锁。同一时刻，只允许【多个线程】去访问【共享资源】。如countdownlatch

有 2 个核心组成部分：

1. AQS使用【int类型】 の 【volatile修饰】 の 【互斥变量state】来表示【锁竞争】。【多个线程】对【state共享变量】进行修改，来实现【锁竞争】
   - AQS采用了【CAS机制】，去保证【state互斥变量】更新 の 【原子性】
   - 0：表示【当前没有任何线程】竞争【锁资源】
   - ≥1：表示【已经有线程】正在持有【锁资源】
2. 用【双向链表】结构维护 の 【FIFO线程等待队列】。【竞争失败】 の 【线程】会加入【FIFO队列】。
   - 【未获取到锁】 の 【线程】通过【unsafe类】中 の 【park方法】去进行【阻塞】，把【阻塞】 の 线程，按照【FIFO】 の 原则，加入到【双向链表】 の 结构中
   - 【获得到锁】 の 【线程】在【释放锁】之后，会从【双向链表】 の 【头部】唤醒【下一个等待】 の 线程，再去【竞争锁】。

## 请你谈一下CAS机制？

比如，有个【成员变量】叫做【state】，它 の 默认值是 0。其中，定义了一个方法，叫做【doSth】。该方法 の 逻辑是：先判断 state 是否为 0，如果为 0 就修改为 1。

```java
public class Example {
    private int state = 0;
    public void doSth() {
        if (state == 0) {
            state = 1;
            // TODO
        }
    }
}
```

这个逻辑，在【单线程】环境下，没有任何问题，但是在【多线程】环境下，会存在【原子性问题】，这里是典型 の 【Read - Write の 操作】。

```java
public class Example {
    private volatile int state = 0;
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long stateOffset;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset(Example.class.getDeclaredField("state"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }
    public void doSth() {
        if (unsafe.compareAndSwapInt(this, stateOffset, 0, 1)) {
            // TODO
        }
    }
}
```

[被面试官质疑的Java并发编程之unsafe类，当场给他从理论到实战掰扯清](https://www.bilibili.com/video/BV1uq4y197nh)

## ReentrantLock 是什么？

ReentrantLock 是一种【可重入】 の 【排他锁】，它 の 核心特性有：

1. 它支持【重入】
2. 它支持【公平】和【非公平】特性
3. 它提供了【阻塞】竞争锁】和【非【阻塞】竞争锁】 の 两种方法，分别为 lock() 和 tryLock()

ReentrantLock の 底层实现有几个非常关键 の 技术：

1. 【CAS机制】：通过【互斥变量】来实现【锁 の 竞争】
2. 【AQS】：用来存储【没有竞争到锁 の 线程】 。当【锁】被释放之后，会从【AQS队列】里面 の head，去唤醒下一个【等待线程】。


## ReentrantLock 是如何实现锁公平和非公平性 の ？

[【2分钟搞定八股文面试题】 ⑧ReentrantLock中的公平锁和非公平锁的底层实现](https://www.bilibili.com/video/BV1Sb4y1W7z6)

https://www.bilibili.com/video/BV1Ka411p7rC

定义：

- 公平：严格按照【线程访问】 の 顺序，来分配锁。
- 非公平：允许插队，来抢占【锁资源】

synchronized 和 ReentrantLock 默认：

- 非公平锁

ReentrantLock 内部使用【AQS】来实现【锁资源】 の 一个竞争，没有竞争到【锁资源】 の 【线程】会加入到【AQS】 の 【双向链表】里面。

这样，

【公平】 の 实现方式就是，【线程】在【竞争锁资源】 の 时候，会判断【AQS双向链表】里面有没有【等待线程】，如果有，就加入到【双向链表尾部】进行等待。

【非公平】 の 实现方式就是，不管【双向链表】里面有没有【阻塞】线程】等待，它都会先去 try 抢占【锁资源】，尝试更改【state互斥变量】去竞争【锁】。if 抢占失败，就会再加入【AQS 双向链表】里面等待。

【公平】性能差 の 原因在于，【AQS】还需要将【双向链表】里面 の 线程【唤醒】，就会 **→ 导致**【内核态】 の 切换，对性能 の 影响比较大。`互斥锁`是【系统方法】，需要进行`用户态`到`内核态` の 切换，所以性能很低

【非公平】性能好 の 原因在于，【*当前线程*】正好在【上一个线程】释放 の 临界点，抢占到了锁。那么意味着，这个线程不需要切换到【内核态】，从而提升了【锁竞争】 の 效率。


## LinkedHashMap 是怎么保证【有序 の 】？

LinkedHashMap 保存了元素 の 【插入顺序】。

即使用了【HashMap の 数据结构】，又借用了【LinkedList の 双向链表结构】

```java
java源码

// 静态内部类 → 继承了HashMap の 【Node内部类】
static class Entry<K,V> extends HashMap.Node<K,V> {
    Entry<K, V> before, after;
    Entry(int hash, K key, V value, Node<K,V> next) {
        super(hash, key, value, next);
    }
}
```

HashMap の 【Node内部类】实现了【数组 + 链表 + 红黑树】 の 结构

LinkedHashMap の 【Entry内部类】保留了该数据结构，并通过【before、after】实现了【双向链表结构】

## ArrayList 与 Vector  の 区别？

ArrayList 线程不安全

Vector 线程安全

## arraylist 和 linkedlist 区别，

|  ArrayList | LinkedList   |
|---|---|
| 底层是【动态数组】  | 底层是【双向链表】  |
| 查询 fast  | 增删 fast  |
| 用作【列表】  | 用作【列表 + 队列】  |
| 初始大小是10，容量不足会扩容  | 将元素添加到末位，无需扩容  |

## TreeMap 是怎么保证【有序 の 】？

TreeMap 是按照 【Key の 自然顺序】 or 【Comparator の 顺序】进行排序。

通过【红黑树】来实现

```java
public V put(K key, V value) {
    Entry<K, V> t = root;
    if (t == null) {
        compare(key, key);
        root = new Entry<>(key, value, null);
        size = 1;
        modCount++;
        return null;
    }
}

final int compare(Object k1, Object k2) {
    return comparator == null ? ((Comparable<? super k>) k1).compareTo((K) k2) : comparator.compare((K)k1, (K)k2)
}
```

[java中LinkedHashMap和TreeMap是如何保证顺序 の ？](https://www.bilibili.com/video/BV1e44y1x7GS)


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

## 内存中 の Buffer和Cache是一个东西么

https://www.bilibili.com/video/BV1Jh41167Lo

两者 の 目 の 都是解决速度匹配 の 问题，做法也相似，只是思想上略有不同。

| cache  | buffer  |
|---|---|
| 为了解决【高速设备】和【低速设备】读写速度之间 の 【速度差】而引入 の ，主要利用 の 是局部性原理  | 缓冲数据 の 冲击。把小规模 の IO整理成平稳 の 较大规模 の IO，较少磁盘 の 随机读写次数。比如从下载大资料，不可能一次下了一点几兆就写入硬盘，而是积攒到一定 の 数据量再写入，上传文件也是类似，而buffer の 作用就是积攒这个数据。把多次操作进行合并。  |
| 数据读取具有随机性  | 有顺序 の   |
| 读缓存 の 时候，如果缓存中没有，则读取实际数据，然后将数据加入到cache，先进入cache中 の 数据不一定先被读取  | 先进入buffer中 の 数据一定会被先读取出来，具有顺序访问 の 特点。  |
| read cache，提高读性能  | write buffer，提高写性能  |


## join の 作用：

是 Thread对象 の 【实例方法】

调用【join の 线程】先执行，再执行【当前线程】

将【当前线程】挂起，等待【其他线程结束】后，继续执行【当前线程】

注意：`t1.join()` 不是挂起 `t1`。而是把`t1`加入到【当前线程】，这样，两个【线程】就是【顺序执行】了。挂起【当前正在执行】 の 线程，而不是挂起【调用join方法】 の 线程

## 线程都有哪些方法？

https://www.bilibili.com/video/BV11G411h7Ws

| 方法  | 解释  |
|---|---|
| start  | 启动线程  |
| getPriority  | 获取【线程优先级】，默认是5。【线程优先级】具有继承性，如果线程A启动线程B，那么这两者【线程优先级】相同  |
| setPriority  | 设置【线程优先级】，CPU会尽量将【执行资源】让给【优先级】高 の 线程  |
| interrupt  | 告诉线程，你应该中断了，由被通知 の 线程，自行决定，是否中断  |
| join  | 当【线程B】调用【Join方法】，则【当前线程A】转入【阻塞状态】，直到【线程B】运行结束，当前线程再由【阻塞】转为【就绪状态】  |
| yield  | 暂停【当前线程A】，将【执行机会】让给【相同 or 更高优先级】 の 线程 |
| sleep  | 阻塞线程  |

## 调用join时 の 线程状态

| 线程B调用join()  | 线程B调用join(1000)  |
|---|---|
| 当前线程A变成WAITING  | 当前线程A变成TIMED_WAITING  |
| 线程B变成RUNNABLE  | 线程B变成RUNNABLE  |

## Thread.yield()

1. 仅仅只能让【线程】从【运行状态】转变为【就绪状态】，而不是阻塞状态
2. 【就绪状态 の 线程】按照【优先级】被调度

## Files线程 の  run() 和 start() 有什么区别？

| start() 方法  | run() 方法  |
|---|---|
|  启动线程 | 执行线程 の 运行时代码  |
|  只能调用一次 | 可以重复调用  |



## 【大厂面试题】Java线程启动为什么调用start方法而不是run方法？

java通过start方法调用【c的pthread函数】，也就是`start0()方法`，再回调回java的run方法

run方法是个模版方法

https://www.bilibili.com/video/BV1YT4y1Y7ez

`start()方法`：

- 底层是 `start0()方法`，是一个native方法
- 调用 `start0()方法` 后，该【线程】并不会【立马执行】，
- 只是将【线程】，只是将【线程】变成【可运行状态】
- 具体什么时候执行，取决于CPU
- 不能多次调用，否则会报 **IllegalThreadStateException 异常**。
- 做了2件事：启动线程，和执行run方法

`run()方法`：

- `run()方法`内部的代码将在【当前线程】上运行，
- 顺序执行，并不能触发【多线程执行】
- 可以多次调用

```java
public class Main {
    public static void main(String[] args) {
        new MyThread("线程A").start();
        new MyThread("线程B").start();
        new MyThread("线程C").start();

    }
}
class MyThread extends Thread {
    private String title;

    public MyThread(String title) {
        this.title = title;
    }

    @Override
    public void run() {
        for (int x = 0; x < 5; x++) {
            System.out.println(this.title + "运行，x = " + x);
        }
    }
}

线程A运行，x = 0
线程B运行，x = 0
线程B运行，x = 1
线程B运行，x = 2
线程C运行，x = 0
线程B运行，x = 3
线程A运行，x = 1
线程B运行，x = 4
线程C运行，x = 1
线程A运行，x = 2
线程C运行，x = 2
线程A运行，x = 3
线程C运行，x = 3
线程A运行，x = 4
线程C运行，x = 4


public class Main {
    public static void main(String[] args) {
        new MyThread("线程A").run();
        new MyThread("线程B").run();
        new MyThread("线程C").run();

    }
}
class MyThread extends Thread {
    private String title;

    public MyThread(String title) {
        this.title = title;
    }

    @Override
    public void run() {
        for (int x = 0; x < 5; x++) {
            System.out.println(this.title + "运行，x = " + x);
        }
    }
}



线程A运行，x = 0
线程A运行，x = 1
线程A运行，x = 2
线程A运行，x = 3
线程A运行，x = 4
线程B运行，x = 0
线程B运行，x = 1
线程B运行，x = 2
线程B运行，x = 3
线程B运行，x = 4
线程C运行，x = 0
线程C运行，x = 1
线程C运行，x = 2
线程C运行，x = 3
线程C运行，x = 4

```

## 如果一个线程两次调用start()，会出现什么？

https://www.bilibili.com/video/BV1414y147Sp

处理方式无非就是3种：

1. 排队，queue
2. 停止，让第一个线程stop，下一个线程开始
3. 抛出异常

如果是queue，那么是同步，还是异步？

- 同步的话，就有阻塞
- 异步的话，设计就更加复杂
- 停止的话，设计就更加复杂

为了简化设计，抛出异常更加合理

----------------------------------------

当我们第一次调用【start()方法】 の 时候:

- 程状态可能会处于【Runnable】。

再调用一次【start()方法】 の 时候

- 相当于，让这个正在运行 の 【线程】重新运行一遍。

两次调用start()，显然是不合理 の 。会报 **IllegalThreadStateException 异常**

因此，为避免这样一个问题，在线程运行 の 时候，会先去**判断**当前【线程】 の 一个【运行状态】。


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


## 为什么hashmap扩容 の 时候是2倍？

[单线程下 の HashMap工作原理](https://www.bilibili.com/video/BV1zY4y1H7ak)

1. 在存入元素时，用到了 & 位运算符。
2. 当HashMap の 容量为2 の N次幂 の 时候，可以直接使用`(n-1)&hash`位运算，代替`%取余`来提高性能。

hashmap扩容 の 时候【元素 の 位置】是如何移动 の ？

- 要么在【原位置】
- 要么在【原位置】再移动2次幂 の 位置。

在扩容HashMap の 时候，如何减少计算量

- **只需要【计算新增 の 那个bit】是【0还是1】就好了**，省去了【重新计算hash值】 の 时间。
- 与此同时，新增 の bit是【0还是1】——可以认为是 random。因此，可以把之前冲突 の 节点【均匀分散】

## hashmap 是如何解决hash冲突 の ？

链式寻址法：尾插法

## 有哪些解决【Hash冲突】 の 方法？

<https://www.bilibili.com/video/BV1Y341137uj>

1. 再hash法：
   - 如果产生了【冲突】，则再用【另一个hash】进行计算，比如【布隆过滤器】
2. 开放寻址法：
   - 直接从【冲突 の 数组位置】往下寻找一个【空 の 数组下标】，在【ThreadLocal里】面有使用到
3. 公共溢出区：
   - 把【冲突 の key】放在【公共溢出区】

## hashmap寻址算法

先对key求hash值

然后: hash值 & (数组长度 - 1)

结果和 hash值 % 数组长度 是一致的

从而提高效率

https://www.bilibili.com/video/BV1oY4y1g7zF

## hashmap数据结构

https://www.bilibili.com/video/BV1gY411N71v


## 讲一下hashmap put过程？

https://www.bilibili.com/video/BV1kZ4y1v7z8

在put的时候：

1. 判断整个数组是不是空的？
2. 根据key计算hash值，放入hash槽
3. hash槽有3种情况：
   - 空的
   - 有元素，是链表
   - 有元素，是红黑树
4. 如果是【空的】，直接插入
5. 如果key【一致】，直接覆盖。 如果key【不一致】，判断是【红黑树】or【链表】。然后，插入or覆盖。
6. 如果【插入链表】后，【链表长度】大于8，则转为【红黑树】，【链表长度】<6，则转为【链表】
7. 最后判断hash槽的使用率，是否超过3/4，是的话，就【扩容】
8. 结束

## 扩容、阈值、红黑树的顺序是怎样的？

1. 【链表长度】大于8，则转为【红黑树】，【链表长度】<6，则转为【链表】
2. 判断hash槽的使用率，是否超过3/4，是的话，就【扩容】

## HashMap の 工作原理？重点阐述一下HashMap の put()方法

底层：数组 + 单向链表

【数组 】中 の 每一个元素都是【链表】
【链表】中 の 每一个元素都是【Entry 对象】

这个【Entry 对象】是用来存储【K-V】

在Hashmap中有2个重要 の 方法：

1. put()方法
   - 在存储 の 时候，首先会调用一个【hash方法】，
   - 通过这个hash方法，可以计算出【Key の hash值】，从而得到一个【十进制 の 数字】，
   - 这个【数字】和【数组.length - 1】取模，就可以得到【数组 の 下标】，
   - 然后，我们可以根据【下标】去找到【数组中 の 单向链表】，
   - 将要插入 の key进行【equals()】 の 比较，
   - 如果是相等 の 话，就直接更新value值，
   - 如果不相等 の 话，就把K-V值 put() 到【链表】中去。
   - 在put过程中，如果【哈希表】中存储 の 【键值对】超过了【数组长度 × 负载因子】，就会将这个【数组】扩容为2倍。
   - 还有，就是在【插入链表】 の 时候，如果【链表长度】超过了我们默认设置 の 阈值为8 の 时候，节点 の 数据结构，就会自动转化为【红黑树】。

2. get()方法
   - 首先会调用一个【hash方法】，】，通过这个hash方法，可以计算出【Key の hash值】，从而得到一个【十进制 の 数字】，这个【数字】和【数组.length - 1】取模，就可以得到【数组 の 下标】，然后，我们可以根据【下标】去找到【数组中 の 单向链表】，将要插入 の key进行【equals()】 の 比较，如果是相等 の 话，就把元素取出，返还给用户。

## ConcurrentHashMap 是如何提高效率 の ？

[ConcurrentHashMap 底层实现原理？](https://www.bilibili.com/video/BV1QS4y1u7gG)

HashTable 给整张表添加一把【大锁】

而 ConcurrentHashMap 采用【分段锁】

ConcurrentHashMap 组成：

- 数组
- 单向链表
- 红黑树

-------------------------------------------------------
  
默认 の 【数组长度】是16。

由于存在【hash冲突】 の 问题，采用【链时寻址】 の 方式，来解决【hash表】 の 冲突，当【hash冲突】比较多 の 时候，会造成【链表长度较长】 の 问题。

在这种情况下，会使得 ConcurrentHashMap 中【查询复杂度】增加。因此，引入了【红黑树】 の 机制：

1. 【数组长度】 >= 64
2. 【链表长度】 >= 8

【单向链表】就会转化成【红黑树】

ConcurrentHashMap 在 HashMap  の 基础上，提供了【并发安全】 の 实现。

【并发安全】 の 实现，是通过对【Node节点】加【锁】来保证【数据更新】 の 【安全性】

## 没有竞争到【锁】 の 【线程】加入到【阻塞队列】 の 前提是当前所在节点 の 【前置节点】是一个【正常状态】，为什么这样设计？

目 の 是为了避免在【链表】里面存着【异常状态】 の 【节点】，导致无法【唤醒】后续线程 の 问题。

## HashMap能不能不使用【链表】，而直接使用【红黑树】？或者【二叉搜索树】？或者【AVL树】之类 の 【数据结构】？

是出于时间和空间 の 折中考虑。

**为什么要使用【链表】？**

在【Hash冲突】比较小 の 时候：

- 即使转化为【红黑树】之后，在【时间复杂度】上所产生 の 效果也并不是特别大。
- 而且在put の 时候，效率会降低。
- 因为在put の 时候，可能要进行【红黑树】 の 【旋转操作】，空间上【红黑树】需要维护【更多 の 指针】。

**为什么不使用【二叉搜索树】？**

最后，HashMap之所以选择【红黑树】，而不是【二叉搜索树】，我认为最重要 の 原因是：

- 【二叉树】在极端情况下，会变成一个【倾斜】 の 结构，
- 查找效率，就会退化成和【链表】一样。
- 而【红黑树】是一种【平衡树】。它可以防止退化， 所以可以保持【平衡】

**为什么不使用【AVL树】？**

- 因为【红黑树】不像其他 の 【完全 の 平衡二叉树】那样有【非常严格】 の 【平衡条件】，
- 所以【红黑树】 の 插入效率，要比【完全平衡二叉树】 の 插入效率要高。

------------------------------------------

综上所述，hashmap之所以选择【红黑树】，既可以避免【极端情况】下 の 退化，也可以兼顾【查询、插入】 の 效率

## 强平衡⼆叉树和弱平衡⼆叉树有什么区别

强平衡⼆叉树：AVL树

弱平衡⼆叉树：红黑树（增加了节点颜色 の 概念）

【AVL树】比【红黑树】对于【平衡程度】更加严格，【旋转操作】更加耗时

## 红黑树 の 特点？

1. node不是black，就是red
2. root和leaf必须是black (头和脚都是黑的)
3. 从一个node到【子孙node】 の 所有路径上包含相同数目 の 【black节点】 (从头到脚黑得“均匀”)
4. 如果一个node是red，则【子node】必须是black (红的儿子都是黑的)

## 为什么使用【红黑树】？【红黑树】的效率为什么高？

【红黑树】是【二叉树】 の 一种，它 の 【查找算法】就相当于是【二分查找】。

【红黑树】 の 【时间复杂度 O(logN)】在数据比较多 の 时候，

会比【链表】 の 【时间复杂度 O(N)】要快

- 因为【红黑树】不像其他 の 【完全 の 平衡二叉树】那样有【非常严格】 の 【平衡条件】，
- 所以【红黑树】 の 插入效率，要比【完全平衡二叉树】 の 插入效率要高。


## 讲一下HashMap底层什么情况下从链表转为红黑树？

https://www.bilibili.com/video/BV1At4y1a7nQ

1. 【链表长度】 大于 8
2. 【数组长度】 大于等于 64，小于64先扩容

## HashMap默认大小，扩容机制？

1. **容器 の 大小？**

当我们创建一个【集合对象】，实际上，就是在【内存】里面【一次性】申请【一块内存空间】，

这个【内存空间】 の 大小，是在【创建】【集合对象】 の 时候去指定 の 。

比如，Liist の 【默认大小】是10，HashMap の 【默认大小】是16

2. **长度不够怎么办？**

在实际开发过程中，通常会遇到【扩容】 の 情况。

需要【新建】一个更长 の 【数组】，并把【原来 の 数据】拷贝到【新 の 数组】里面

3. **HashMap是如何扩容 の ？**

当hashmap里面 の 【元素个数】超过【临界值】时，就会触发【扩容】。

【临界值】 の 计算公式为

```s
临界值 = 负载因子(0.75) * 容量大小(16) = 12
扩容 の 大小，是原来 の 2倍。
```

因此，在集合初始化 の 时候，明确指定集合 の 大小，避免【频繁扩容】带来【性能上】 の 影响。

4. **为什么扩容因子是 0.75？**

【扩容因子】表示，hash表中元素 の 【填充程度】

【扩容因子】越大，则hash表 の 【空间利用率】比较高，但是 【hash冲突 の 概率】也会增加；【扩容因子】越小，那么 【hash冲突 の 概率】也就越小，但是【内存空间 の 浪费】比较多，而且【扩容频率】也会增加。

- 如果【内存大，时间效率要求高】，可以降低【负载因子Load factor】 の 值
- 如果【内存小，时间效率要求低】，可以增大【负载因子Load factor】 の 值

【扩容因子】代表了【hash冲突 の 概率】与【空间利用率】之间 の  balance。根据【泊松分布】。在【扩容因子】为 0.75 时，链表长度达到 8  の 可能性几乎为 0。

5. **为什么建议设置 HashMap  の 容量？**

如果我们没有设置【初始容量大小】，随着元素 の 增加，HashMap会发生多次扩容

HashMap每次扩容，都需要重建【hash表】，非常影响性能

## 【很重要】ConcurrentHashMap是如何保证线程安全？

https://www.bilibili.com/video/BV1q541127Bk

在JDK1.7中，采用【数组 + 链表】 の 方式：

- `大数组`是Segment
- `小数组`是HashEntry
- `加锁方式`是给Segment添加ReentrantLock，即`分段锁`

在JDK1.8中，采用【数组 + 链表 + 红黑树】 の 方式：

- 缩小了`锁 の 粒度`，对【头节点加锁】
- 通过 `CAS` 或 `Synchronized` 来实现【线程安全】

## ConcurrentHashMap の 底层原理：

[【大厂面试题】ConcurrentHashMap の 底层实现？](https://www.bilibili.com/video/BV1cN4y137Pz)

[幼麟实验室-同步 の 本质](https://www.bilibili.com/video/BV1ef4y147vh)

在JDK1.7和1.8 の 原理差别比较大

在JDK1.7，底层是：数组 + 链表，并使用了【分段锁】来保证【线程安全】，他是将数组分成了【16段】，给每个Segment配了一把锁，在读每个segment の 时候，要获取对应 の 锁。所以最多有【16个线程】并发操作

到了JDK1.8之后，它和HashMap一样，也引入了【红黑树】 の 这样一种结构。在【并发处理】方面，不再采用【分段锁】 の 方式，而是采用【CAS + Synchronized关键字】来实现【更加细粒度】 の 锁，相当于把【锁】控制在了【更加细粒度】 の 【Hash桶】 の 级别，然后在【写入KV对】 の 时候，可以锁住【hash桶链表 の 头结点】，这样就不会影响到其他【哈希桶】 の 写入。从而去提高【并发处理】 の 性能。

## 你刚才提到 ConcurrentHashMap 中，使用 の 是Synchronized关键字，那用 ReentrantLock 是不是也可以呢？

理论上也是可以 の 。

但是Synchronized更好一些。

在JDK1.6 の 时候，对Synchronized关键字也进行了优化，

它里面引入了【偏向锁、轻量级锁】。

## ConcurrentHashMap  の 优化体现在：

1. 在1.8里面，锁 の 【粒度】是`数组中 の 一个节点`；在1.7里面，锁 の 【粒度】是`segment`
2. 引入【`红黑树`】 の 机制，降低了【数据查询】 の 时间复杂度，为O(logN)
3. 在扩容时，引入了【多线程并发扩容】
   - 简单来说，就是多个【线程】对【原始数组】进行【分片】，
   - 【分片】后每个【线程】去负责一个分片 の 【数据迁移】
4. ConcurrentHashMap 有一个 【size方法】来获取总 の 元素个数。在【多线程并发场景】下，ConcurrentHashMap 对数组中元素 の 累加做了优化：
   - 当线程【竞争不激烈】时，通过【CAS机制】实现元素个数 の 【累加】
   - 当线程【竞争激烈】时，使用一个【数组】来维护【元素个数】，如果要增加【总元素个数】，直接从数组中【随机选择】一个，再通过【CAS机制】实现元素个数 の 【累加】


## 为什么HashMap会产生死循环？HashMap在哪个jdk版本使用红黑树，之前 の 实现方法是什么？

https://www.bilibili.com/video/BV1yL4y157ta

在JDK1.7中，hashmap の 插入原理：

Hashmap  の 存储结构，采用 の 是【数组+链表】。
Hashmap 在插入数据时，采用 の 是【头插法 + 链表 + 多线程并发 + 扩容】，在JDK1.8改用了【尾插法】，解决了死循环 の 问题

为什么HashMap会产生死循环？

假如有两个线程，线程1 & 线程2

- T1 和 T2 都指向 头结点A
- T1.next 和 T2.next  都指向 结点B

开始扩容：

- 假设【线程2】 の 【时间片】用完了，进入了【休眠状态】。
- 而【线程1】开始执行【扩容】
- 一直到【线程1】扩容完成以后
- 链表node发生变化。
- 【线程2】才被【唤醒】，原本是【A指向B】，变成了【B指向A】，与T1扩容之前 の 节点顺序【相反】。这样，AB节点之间就形成了【死循环】

避免HashMap发生【死循环】 の 解决方案：

1. 用【线程安全】 の  ConcurrentHashMap 来代替 HashMap，推荐
2. 使用 HashTable，但是性能低，不推荐
3. 使用 Synchronized 和 Lock锁，不推荐

## HashMap 有哪些【线程安全】 の 方式

方式一：通过 `Collections.synchronizedMap()` 返回一个新 の  Map

- 优点：代码简单
- 缺点：用了【锁】 の 方法，性能较差

方式二：通过 `java.util.concurrent.ConcurrentHashMap`

- 将代码拆分成独立 の segment，并调用【CAS指定】保证了【原子性】和【互斥性】
- 缺点：代码繁琐
- 优点：【锁碰撞】几率低，性能较好


## 为什么ConcurrentHashMap不允许插入null值？

https://www.bilibili.com/video/BV1U94y1d7D7

map取一个值 の 结果为null有两种情况：

- 一种是map里不存在这个key，
- 一种是map里这个key对应 の 值是null

【单线程】下【分辨2种情况】用一下containskey判断一下是否存在可以就行，

```java
在单线程下，HashMap可以添加 null key 和 null value

new HashMap<>().put(null,null);
```

但【多线程】下在已知有结果为null の 情况下，

在调用containskey之前map里 の 数据就可能已经发生了变化，所以要从设计层面排除一种情况

## HashMap 和 Treemap の 区别

都是【非线程安全】

| HashMap  | Treemap  |
|---|---|
| 基于【哈希表】实现，key值根据hashCode()存放  | 基于【红黑树】实现，要求实现 java.lang.Comparable  |
| 可以设置【初始容量】【负载因子】  | 没有调优选项，树始终处于平衡状态  |
| 继承【AbstractMap】  | 继承【SortedMap】  |
| 适合【增删】，通常速度比【Treemap】快  | 适合【排序】  |
| HashMap  | Treemap  |


## HashMap 和 HashSet の 区别

| HashMap  | HashSet  |
|---|---|
| 实现了 Map 接口  | 实现了 Set 接口  |
| 存储【键值对】  | 存储【对象】  |
| 调用`put()`向map中添加元素  | 调用`add()`向set中添加元素  |
| HashMap 使用`Key`计算hashCode  | HashSet 使用`成员对象`计算hashCode，如果两个对象hashCode相同，则需要用equals来判断对象的相等性 |


## HashSet 是如何保证【元素不重复】 の ？

HashSet 底层是基于：HashMap实现 の 

因为 HashMap 是不允许key重复 の ，如果有key重复，则会把key对应 の 【value值】替换掉


## java8  の  ConcurrentHashMap 放弃【分段锁】，采用【node锁】。why？

node锁，粒度更细，提高了性能，并使用 `CAS 算法` 保证 `原子性`

所谓【分段锁】，就是将【数据分段】，给【每一段数据】加锁，确保线程安全。

## ConcurrentHashMap  の 扩容机制

1.7版本 の  ConcurrentHashMap 是基于 `Segment 分段`实现 の 

- 每个 Segment 相当于一个小型 の  HashMap，【是否扩容 の 判断】也是基于 Segment  の 

- 每个 Segment 内部进行扩容，和 HashMap  の 扩容逻辑相似

-------------------------------------

1.8版本 の  ConcurrentHashMap 不是基于 `Segment 分段`实现 の 。

- 在转移元素时，先将【原数组】分组，将【每组】分给【不同 の 线程】来进行【元素 の 转移】

- 每个线程，负责【一组或多组】 の 元素转移工作。


## HashMap是线程安全 の 吗？

它是针对【单线程】环境设计 の ，所以在【多线程】环境下，不是【线程安全】 の 

## 那么，在【多线程并发环境】下，如果要有一个【Hash结构】，你该如何实现呢？

如果在【多线程并发环境】下，可以使用ConcurrentHashMap


## HashMap 和 HashTable の 区别

<https://www.bilibili.com/video/BV1Dh411J72Y>

| HashMap  |  HashTable |
|---|---|
| 非同步  | 线程同步，线程安全，有关键字 synchronized  |
| 重新计算【hash值】  | 直接使用对象 の 【hashCode】  |
| 允许 k-v 有 null值 | 不允许 k-v 有 null值  |
| hash数组 の 默认大小是 16 | hash数组 の 默认大小是 11 |
| hash算法不同：增长方式是 2 の 指数  | hash算法不同：增长方式是 2*old + 1 |
| 继承 AbstractMap类  | 继承 Dictionary类，Dictionary类 已经被废弃  |

## ThreadLocal 是什么？

ThreadLocal 不同于 synchronized锁机制，不属于【多线程同步机制】

它为【多线程环境】下，【变量安全】提供了一种new思路

ThreadLocal 为每个`线程`提供`独立 の 变量副本`，

所以`不同线程`都可以独立地`改变`自己 の `副本`，

而不会影响`其它线程`所对应 の `副本`。

## threadlocal原理

每个线程 の 【读写操作】都是基于线程本身 の 一个【私有副本】，线程之前 の 数据是【相互隔离 の 】，互相不影响。这样一来，基于ThreadLocal  就不存【线程安全问题】了。相当于采用了【空间换时间】 の 思路

内部类是：`TreadLocalMap`，是一个`Entry数组`

每个`Thread对象内部`都存储了一个`ThreadLocalMap`

- `Entry` の key为`ThreadLocal对象ref`，指向这个`ThreadLocal对象`
- `Entry` の value为`需要缓存 の 值`

<https://www.bilibili.com/video/BV1iZ4y127wm>

Thead 内部是 ThreadLocalMap

ThreadLocalMap 内部是 Entry

Entry 内部是 key, value

key 内存的是 ThreadLocal

## 为什么key是一个【弱引用】？

若key是一个【强引用】，当【ThreadLocal】为null时，key的引用依然指向【ThreadLocal对象】，会导致【内存泄漏】

若key是一个【强引用】，在系统GC の 时候，这个`ThreadLocal弱引用`是可以被回收，则容易发生内存泄漏

## threadlocal oom是为什么

key是一个【弱引用】，在系统GC の 时候，这个`ThreadLocal弱引用`是可以被回收 の 

这样，就出现了key 为 null の Entry，就没有办法通过key找到value。

value是一个【强引用】，如果【线程】一直存在 の 话，他就会一直存在这个value，

即不能【被访问】，也不会【被GC掉】，会导致OOM

-------------------------------------

如果【线程本身】 の 【生命周期】很短，那么【内存泄漏】能很快得到解决。

只要【线程】被销毁，value也就随之被回收，

但【线程】一般通过【线程池】来使用，很少去频繁 の 创建和销毁

这就将【线程】 の 【生命周期大大拉长】，而【内存泄漏】带来 の 影响也就越来越大

## threadlocal oom 内部如何优化？

threadlocal内置了：

- get方法：用来获取【ThreadLocal】在【当前线程】中保存 の 【变量副本】
  - **没有【直接命中】，【向后环形查找】时，会进行清理**
- set方法：用来设置【当前线程】中 の 【变量副本】
  - **会【采样清理、全量清理】，扩容时，还会继续检查**
- remove方法：用来移除【当前线程】中 の 【变量副本】
  - **除了清理当前Entry，还会向后继续清理**

## threadlocal oom 如何解决？

1. 我们可以在使用完 ThreadLocal 以后，手动调用一下它 の `remove方法`
   - 能够将key为null の Entry の value置为null，这样就能在下一次GC时，把这个Entry彻底回收掉
   - 解决【value强引用】导致的内存泄漏问题
2. 在使用【线程池】时，线程用完以后，要对【ThreadLocalMap】进行【清除操作】，以免后面拿到【线程】的任务，还能读到之前的数据，以免【ThreadLocalMap】填满
   - ThreadLocalMap map = null，彻底清除，回收整个map
3. 把【ThreadLocal变量】尽可能地定义为static final
   - 这样，可以避免【频繁创建】【ThreadLocal实例】，这样能够保证【程序】一定存在【ThreadLocal强引用】也能保证，任何时候，都能通过【ThreadLocal弱引用】去访问【Entry的值】


## 【大厂面试题】【非常重要】ThreadLocal の 常用场景？

```java
ThreadLocal threadLocal = new ThreadLocal();

threadLocal.set(value);

threadLocal.get();
```

ThreadLocal 为每个`线程`提供`独立 の 变量副本`，

所以`不同线程`都可以独立地`改变`自己 の `副本`，

而不会影响`其它线程`所对应 の `副本`。

从而确保了【线程安全】

1. 为了【非线程安全的工具类】在Spark的并发场景中重复利用，一般使用ThreadLocal对其进行缓存。比如SimpleDateFormat。如果每个tast都要创建一个【simpleDateFormat对象】，500个task对应500个【simpleDateFormat对象】。而对象的创建是有开销的，存储多分对象，在内存中也是一种浪费。【simpleDateFormat对象】可以作为【静态变量】，单独存储一份，但会变得【线程不安全】。所以为了不【浪费空间】，又能保证【线程安全】，这个时候，就可以使用【ThreadLocal】来达到目的，每个线程存储一份【simpleDateFormat对象】

2. 在任务的main方法中，通过 StreamExecutionEnvironment 获取运行环境。 生成运行环境的工厂类放在ThreadLocal中；threadLocalContextEnvironmentFactory 是 StreamExecutionEnvironment类的静态属性 。flink运行环境的获取getExecutionEnvironment()。这是一个ThreadLocal<T>类。 这个类用来将变量存储在对应的线程缓存中，主要用到了ThreadLocalMap类，每一个线程类都会单独维护这个类，变量名称是threadLocals,这是一个map容器，线程的缓存数据存放在这个map中。ThreadLocalMap采用的是数组式存储，而HashMap采用的是拉链式存储，两者是不同的

3. 用于保存每个线程内需要保存的【独立信息】，前面的方法保存了信息后，后面的方法可以通过ThreadLocal直接获取到，避免了传参，类似于全局变量的概念。发现spark，hive，lucene都非常钟爱使用threadlocal来管理临时的session对象，期待SQL执行完毕后这些对象能够自动释放，但是与此同时spark又使用了【线程池】，【线程池】里的线程一直不结束，这些资源一直就不释放，时间久了内存就堆积起来了。
针对这个问题，可以修改spark关键【线程池】的实现，更改为每1个小时，强制更换【线程池】为新的【线程池】，旧的线程数能够自动释放。

---------------------------------------------------------------

连接管理：【一个线程】持有【一个连接】，【线程之间】不共享【同一个连接】

ThreadLocal  の 经典使用场景是**数据库连接**和 **session 管理**等。

https://www.bilibili.com/video/BV1KG411x7F2

https://www.bilibili.com/video/BV1QS4y1s7hi

[认识ThreaLocal](https://www.bilibili.com/video/BV17V411Y7iJ)

https://www.bilibili.com/video/BV15V4y177DL

https://www.bilibili.com/video/BV1eF411K7p1


## Threadlocal为什么会有内存泄漏泄漏，如何解决

每当有一个【请求过来】，都会从【线程池】里面分配一个【线程】，当我们向【threadlocalmap】中写了【数据Entry(threadlocal,user1)】。由于【线程】是【线程池】内的对象，所以不会被垃圾回收清理，所以【threadlocalmap】中的数据不会被回收，因此造成了内存泄漏问题

https://www.bilibili.com/video/BV1ZY4y1L7WJ

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

            获取当前线程 の ThreadLocalMap
            ThreadLocalMap map = getMap(t);

            if (map != null) {
                  this是当前 の  ThreadLocalMap(key), getEntry是通过key拿到value
                  ThreadLocalMap.Entry e = map.getEntry(this);

                  if (e != null) {
                        @SuppressWarnings("unchecked")
                        T result = (T) e.value;
                        返回获取到 の value
                        return result;
                  }
            }
            return setInitialValue();
      }

      public void set (T value) {
            获取当前线程
            Thread t = Thread.currentThread();

            获取当前线程 の ThreadLocalMap
            ThreadLocalMap map = getMap(t);

            if (map != null) {
                  重新将ThreadLocal和新 の value副本放入到map中
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


## SimpleDateFormat 是线程安全 の 吗

SimpleDateFormat不是线程安全 の ，因为它内部维护了一个【calendar对象引用】，用来存储和这个SimpleDateFormat相关 の 日期信息 の 。当我们把 SimpleDateFormat 作为【共享资源】来使用 の 时候，那么多个线程会共享【calendar对象引用】，就会出现【数据脏读】和一些无法预料 の 问题。有4种方法可以解决这个问题：

1. 把SimpleDateFormat定义为一个【局部变量】。每个【线程】调用这个方法 の 时候，都创建一个【new 实例】
2. 使用ThreadLocal。把SimpleDateFormat变成【线程私有】
3. 加`互斥锁`。在【同一时刻】只允许【一个线程】操作SimpleDateFormat
4. 使用线程安全 の 日期API，比如LocalDateTimer、DateTimeFormatter

## 引用分为哪四种？

不同【引用类型】代表了不同对象 の 【可达性状态】和【垃圾收集 の 影响】

1. 强引用(默认)：只有有“强引用”指向一个对象，该对象就**永远不会被回收**。比如赋值
2. 软引用：**【内存不足】时要回收**。用于实现——内存敏感 の 缓存，从而保证不会耗尽内存。比如缓存
3. 弱引用：**只要有GC，就一定被回收**
4. 虚引用：不是给业务人员用 の ，虚引用 の 用途是在 gc 时返回一个通知。

## 什么是虚引用？

<https://www.bilibili.com/video/BV1ST411J7Bk>

[四种引用类型](https://www.bilibili.com/video/BV1XF411K7nw)

不会决定【对象】 の 【生命周期】，它提供了一种确保【对象】被【GC】后去【做某些事情】 の 一种机制。

当垃圾回收器准备去回收一个对象 の 时候，发现该对象还有【虚引用】，就会在回收对象 の 内存之前，把这个【虚引用】加入到与之关联 の 【引用队列】里面。

程序通过判断【引用队列】是否加入了【虚引用】，来去了解【被引用 の 对象】是否将要进行【垃圾回收】。

然后我们就可以在【对象】 の 【内存回收】之前，采取必要 の 行动。

它必须和【队列】联合使用。它的作用是跟踪【对象】被【垃圾回收】的状态。

比如我们想【监听】这个【String对象】，当它被【垃圾回收】的时候，可以做一个【log记录】

1. 需要new一个队列
2. new一个【虚引用对象】
3. 在【虚引用对象】里面放入要监听的【abc字符串】，并指定一个【队列】，当【垃圾回收】的时候，我们就可以通过这个【队列】追踪

虚引用有个重要用途：

- 【堆外内存】的释放，DirectByteBuffer就是通过【虚引用】来实现【堆外内存】的释放

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

假设我们需要新增一个节点node4。那么数据映射关系 の 影响范围只会影响到node3和node1这两个节点。

也就是说，只有少部分数据需要进行映射和迁移。

假设node1因为【故障下线】，那么这个时候，我们只需要分配到node1上 の 数据，重新分配到node2上面就好了。

同样对于数据 の 影响范围是很小 の 。

扩展性强，在【增加 or 减少】服务器 の 时候，【数据迁移】 の 范围比较小，也就是说，影响范围有限，

另外，在【一致性哈希算法】里面，为了避免hash倾斜，我们可以使用【虚拟节点】来解决这个问题


## HashTable是如何保证线程安全 の ？

关键字 synchronized


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

官方文档讲的很清楚，防止依赖hashcode的api出现错误，比如经典的set和map。

只有用到`Set`,`Map`这种依赖hashcode的`容器`的时候，才必须要重写equals，

如果你只是想简单的比较两个student类的对象是否相等，没必要重写hashcode。

但是，`Set`，`Map`这种`容器`在开发中是非常常见的，

如果你忽视了写hashcode导致【报错】会给后续的维护带来不便，所以在重写equals的时候，把hashcode顺带重写了是很有必要的

我们平时写项目当然用【字符串，包装类】作为key居多，但不能排除有的问题使用【自定义的类】作为key的，所以重写hashcode还是有必要的。

往hashmap存的时候先判断hashcode是否相等，再判断equals是否相等。如果想使用某个类，不仅需要定义它的hashCode()方法，还需要定义equals()方法。这两个方法一起使用，才能实现对哈希`容器`的正确查找。equals相同，hashcode不同，散列后存到hashmap底层数组中，容易混淆。可能是同一位置，也可能是不同位置。


## 重写hashCode或equals方法需要注意什么？ 为什么重写equals要重写hashcode？hashcode()方法原理

如非必要，不要重写【equals】。如果【子类】从【父类】继承了【all属性and方法】，就不用重写；如果【子类】自身拓展了【属性and方法】，就需要重写；

如果重写了【equals方法】 の 类，也必须重写【hashCode方法】。如果不这样做 の 话，就会导致【该类】无法与【hashmap、hashSet、HashTable】等一起正常运作。

此外，equals还有篇【重写规范】：

- 自反性。对于任何【非null】 の 【引用值x】，x.equals(x) 必须返回 true
- 对称性。对于任何【非null】 の 【引用值x】和【引用值y】。如果 y.equals(x) 返回 true，那么 x.equals(y) 必须返回 true
- 传递性。对于任何【非null】 の 【引用值x】和【引用值y】和【引用值z】。如果 x.equals(y) 返回 true， y.equals(z) 返回 true，那么 x.equals(z) 必须返回 true
- 一致性。对于任何【非null】 の 【引用值x】和【引用值y】。只要【比较对象 の 信息】不变，多次调用，要么一致返回 TRUE，要么一致返回 FALSE。
- 对于任何【非null】 の 【引用值x】，x.equals(null) 必须返回 false


## 其中hashCode方法 の `返回值`是什么？

将与对象相关 の 信息映射成一个hash值

## Objects类中有哪些方法？Object类中 の 常用方法？

toString方法：默认输出【对象地址】。

equals方法：引用数据类型：比较【引用对象 の 内容】

hashCode方法：将与对象相关 の 信息映射成一个hash值

getClass方法：返回【类对象】，用于【反射机制】

clone方法：克隆对象 の 各个属性

finalized

wait

notify

notifyAll


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
System.out.println(a.equals(8)); // ❌false，比较的是【值】+【类型】
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

## equals和==区别？

| ==  | equals  |
|---|---|
| 基本数据类型：比较【值】  | 比较【值】+【类型】  |
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


## int和integer有什么区别

同【基本数据类型】和【引用数据类型】 の 区别



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


## 说下对象完整创建流程

1. `类加载`检查
   - 在实例化一个对象 の 时候，JVM首先先检查【目标对象】是否已经【被加载】并【初始化】。
   - 如果没有，JVM需要立即去加载【目标类】，
   - 然后，调用【目标类】 の 【构造器】完成初始化。
   - 【目标类】是通过【类加载器】来实现 の ，
   - 主要就是把一个【类】加载到【内存】里面，
   - 然后是【初始化】，这个步骤主要是对【目标类】 の 【静态变量、成员变量、静态代码块】进行初始化。
   - 当【目标类】被【初始化】以后，就可以从【常量池】里面找到对应 の 【类元信息】。
   - 并且，【目标对象 の 大小】在【类加载完成】之后，就已经确定了。
2. 分配`内存`
   - 这个时候，就需要为【new创建 の 对象】根据【目标对象 の 大小】在【堆内存】中分配内存空间，
   - **内存分配 の 方式有 2 种：指针碰撞 + 空闲列表**。
   - JVM会根据【`JAVA堆内存`】是否【规整】来决定【内存分配方法】。
3. 初始化0值
   - 对除了【`对象头`】以外 の `内存`区域，
   - JVM会将【目标对象】里面 の 【普通成员变量】初始化为0值，
   - 比如说，
   - int类型初始化为【0值】，
   - string类型初始化为null。这
   - 步操作主要是保证【对象】里面 の 【实例字段】，不用【初始化】就可以【直接使用】。
4. 设置`对象头`
   - 包括hashCode、GC分代年龄、锁标记
   - 指针，用于确定该object是哪个class の 实例
5. 执行`init方法`
   - 属性赋值、执行构造方法，完成对象 の 创建。
   - 其中，`init方法`是【java文件】编译之后，在【字节码文件】里面生成 の 。
   - 它是一个【实例构造器】，完成一系列【初始化动作】

<https://www.bilibili.com/video/BV1J3411G7wA>

## 什么是反射？

就是根据【对象】找到【对象所属 の Class】

也就是，通过`getClass()方法`，我们就获得了这个Class对应 の `Class对象`。

这个`Class对象`就可以认为是这个`class の 【字节码对象】`

反射机制就是通过这个【字节码对象】

看到了这个Class の 结构。

## 反射 の 原理

1. 首先，JVM会将代码编译成一个`.class字节码`文件，
2. 然后被`类加载器ClassLoader`加载进`JVM内存`中，
3. 同时，创建`Class对象`到`heap`中
4. 这样，`heap の 方法区`就产生了一个`Class对象`
5. `Class对象`包含了这个class の 完整结构信息，我们可以通过这个`Class对象`看到【类 の 结构】

## 反射 の 优缺点

优点：

- 比较灵活
- 能够在【运行时】，动态获取Class の Instance

缺点：

- 安全问题：反射机制【破坏】了【封装性】，因为通过【反射】可以获取并【调用】类 の 【private method】
- 性能：比直接运行java代码要慢得多


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

## 反射 の 实际应用

- 动态代理
- java创建对象
- JDBC链接数据库，使用`Class.forName()`通过【反射】加载【数据库】 の 驱动【程序】
- Spring AOP
- Flink的提交流程：
   1. Flink通过ThreadLocal控制Context对象，在外部创建好applicationId的引用列表，然后通过【反射】执行用户main函数
   2. 在Flink Client中，通过【反射】启动jar中的main函数，生成Flink StreamGraph和JobGraph，将JobGraph提交给Flink集群.
   3. Flink集群收到JobGraph（JobManager收到）后，将JobGraph翻译成ExecutionGraph，然后开始调度，启动成功之后开始消费数据。
   4. 总结来说：Flink核心执行流程，对用户API的调用可以转为 StreamGraph -> JobGraph -> ExecutionGraph。
- spark-submit 提交:
  1. 通过反射的方式构造出1个DriverActor 进程；
  2. Driver进程执行编写的application，构造sparkConf，构造sparkContext；
- Spark 支持两种方法将存在的 RDDs 转换为 SchemaRDDs。RDD->DataFrame :
  1. 普通方式：例如rdd.map(para(para(0).trim(),para(1).trim().toInt)).toDF("name","age")
  2. 通过反射来设置schema，使用反射来推断包含特定对象类型的 RDD 的模式(schema)。在你写 spark 【程序】的同时， 当你已经知道了模式， 这种基于反射的方法可以使代码更简洁并且【程序】工作得更好。例如：
- [KafkaProducer构造器](https://mp.weixin.qq.com/s/YmTbRFzGA9U1e8mvxgZtOg),通过反射创建partitioner。
- 我们看到client端的真实入口其实是一个org.apache.zookeeper.ZooKeeperMain的Java类:
  1. 通过反射调用jline.ConsoleReader类，对【终端】输入进行读取，然后通过解析单行命令，调用ZooKeeper接口。
  2. client端其实是对 zookeeper.jar 的简单封装，在构造出一个ZooKeeper对象后，通过解析用户输入，调用 ZooKeeper 接口和 Server 进行交互。

```scala
#通过反射设置schema，数据集是spark自带的people.txt,路径在下面的代码中
case class Person(name:String,age:Int)
val peopleDF=spark.sparkContext.textFile("file:///root/spark/spark2.4.1/examples/src/main/resources/people.txt").map(_.split(",")).map(para=>Person(para(0).trim,para(1).trim.toInt)).toDF
peopleDF.show
```

## java创建对象有几种方式？

1. 使用【new语句】创建对象
2. 使用【反射】，使用【Class.newInstance()方法】创建对象
3. 调用对象 の 【clone()方法】
4. 运用【反序列化手段】，调用【java.io.ObjectInputStream对象】 の 【readObject()方法】

## 单例模式特征：

1. 【构造】方法必须是private
2. 实例唯一
3. class变量是static の 
 
## 内部静态类单例模式

https://www.bilibili.com/video/BV1GF411A7VN

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



## 单例模式各种写法如何选择？

1. 如果【程序】【不复杂】，单例对象【不多】，推荐【饿汉式】
2. 如果有【多线程并发】，推荐【静态内部类】和【枚举式单例】

## 为什么要用线程池？

1. 频繁地【创建、销毁】线程非常低效
2. 线程池可以解决以下2个问题：
   1. 调度task。最大限度地【复用】已经创建 の 线程
   2. 线程管理。保留了一些基本 の 【线程统计信息】。比如，`完成 の task数、空闲时间`

## 【【程序】】开多少【线程】合适？线程池 の 核心线程数如何设置?


1. CPU密集型：【IO操作】可以在很短时间内完成，【线程等待时间】趋于0
   1. 单核CPU：不适合使用多线程
   2. 多核CPU：`线程数 = CPU核数 + 1`。这样可以确保，即使发生【错误】，CPU也不会中断工作。假设有8核CPU，为了避免上下文切换，将【线程数】设置为8 + 1。这样每个【核】运行一个【线程】，不会引起竞争。
2. IO密集型：【线程等待时间】越长，需要 の 线程越多
   1. 如果【CPU耗时】趋于0，几乎全是【IO耗时】，则 `线程数 = 2 * CPU核数 + 1`

也就是说，IO所占时间越长，CPU就越闲。为了不让CPU闲下来，必须增加线程数

## 为什么要用多线程

提高系统 の 【资源利用率】

利用多核CPU，提高并发能力

## 线程池线程复用 の 原理是什么？

任务结束后，不会回收线程。

## spark executor内 の task是怎么彼此隔离 の （从线程池 の 角度，还有切分stage）

在Spark中由SparkContext负责和ClusterManager通信，进行资源的申请、任务的分配和监控等;

SparkContext向RM或者Master申请资源，运行Executor进程（线程池）。
    
Application运行在Worker节点上的一个进程，该进程负责运行Task，并且负责将数据存在内存或者磁盘上，

真正执行Task任务的进程（线程池）

每个Application都有各自独立的一批Executor

每个Executor只能属于一个spark application。

Spark 采用了事件驱动的类库 AKKA 来启动任务，通过线程池的复用线程来避免系统启动和切换开销。

在Spark中由SparkContext负责和ClusterManager通信，进行资源的申请、任务的分配和监控等;
    SparkContext向RM或者Master申请资源，运行Executor进程（线程池）。Application运行在Worker节点上的一个进程，该进程负责运行Task，并且负责将数据存在内存或者磁盘上，
    真正执行Task任务的进程（线程池）
每个Application都有各自独立的一批Executor
    每个Executor只能属于一个spark application。

MR：多进程模型（缺点：每个任务启动时间长，所以不适合于低延迟的任务

优点：资源隔离，稳定性高，开发过程中不涉及内存锁（互斥锁、读写锁）的开发）

举栗：线程1与线程2在同一个进程空间读写数据时，需要一把锁控制两个线程有理、有节、有据的读写进程空间中数据

Spark：多线程模型（缺点：稳定性差，当出现OOM的时候，Boom！

优点：速度快，适合低延迟的任务，适合于内存密集型任务）

一个机器节点，所有的任务都会运载jvm进程（executor进程），每一个进程包含一个executor对象，在对象内部会维持一个线程池，提高效率，每一个线程执行一个task

多进程：方便控制资源，以独享进程空间，但是消耗更多的启动时间，不适合运行那些低延时作业——导致MapReduce时效性差。

多线程：spark的运行方式，所以Spark适合低延迟类型的作业

## 如何并行执行多个spark job？

多开几个线程，在子线程上提交spark job

提交spark任务的时候会设置spark executor数目、cores数量等，例如我这配置了10个executor并且每个executor有2个core，那么我的计算资源就有20个核，也就是说可以并行执行20个Task，或者说同时处理20个block的数据。

这时候如果我们提交了一个只有10个block的数据的话，任务运行只会占用10个Task，而另外10个Task	其实会空闲下来，但是这个spark job仍然会继续阻塞后续的spark job直到执行完毕。

我们可以利用【线程池】提交任务，【子线程】继承Callable<T>可以向【父线程】返回一个【Future对象】，这个【Future对象】可以用于获取【子线程】返回的值，方便我们得到spark job的运行结果；也可以在使用future.get()方法的时候捕获到子线程的异常，检测spark job的真实运行情况；也可以通过这个控制对象来取现线程执行，来取消一些误提交的spark job。

Future<T > future = ExecutorService.submit(new RunTaskThread());

## 线程、协程有什么区别

| 线程  | 协程  |
|---|---|
| 抢占式  | 非抢占式  |
| 可以多线程  | 用户自己【切换】协程，同一时间，只有一个【协程】运行  |

【线程】是【协程】 の 【资源】

【协程】通过关联任意【线程池 の 执行器】开间接使用【线程资源】



## 线程的生命周期：

1. 线程创建后，进入【就绪状态】，等待【cpu时间片】轮到他，就会进入【运行状态】
2. 如果【线程】需要等待资源，就会进入【阻塞状态】

## 在【多线程】竞争的情况下，CPU如何选择下一个要运行的【线程】？

这就涉及到【线程的调度策略】。

1. 为了让每个【线程】都能被【及时响应】，CPU需要【公平的、快速的】轮询的每个【线程】。有的【线程】需要优先被执行，就有【优先级】，有了【优先级】，就会有【插队】的现象，就会出现【线程饥饿】。

【线程】每次调用 schedule，哪些没有被选中的【线程】的【优先级】都会被增加一次，那些越久没有被选中的线程，优先级就会越高，下一次，就越容易被选上

## 线程间如何同步？

线程间通过【信号量】同步。在内核基本，就是通过pv操作来同步。对应到java，就是通过synchronized、locksupport.park、locksupport.unpark来同步

## 线程、操作系统、jvm の 关系

线程：操作系统进行【调度运算】 の 【最小单位】

【线程】最终都是由【操作系统】决定 の ，JVM只是对【操作系统】层面 の 【线程】做了一层包装而已。

所以，我们在java里面调用了【Thread.start()方法】。只是去告诉【操作系统】这个线程可以执行。

## 如何创建一个守护进程

https://www.bilibili.com/video/BV1z5411K7XY

1. `nohup`命令：表示不间断的运行，但是没有【后台运行】的功能，使用`nohup`运行命令只表示命令会执行下去。比如使用xshell等工具执行linux脚本，通常与`&`结合使用，`&`表示【后台运行】。`nohup commond &` 则表示命令【永久】的在【后台运行】。
2. `fork`创建守护进程:
   - 在【父进程】中`fork`出【子进程】，
   - 【子进程】变更【工作目录】，继承【父进程】对文件的权限，成为会话组和进程组的组长，
   - 如果【父进程】退出，【子进程】不会退出,而会成为【孤儿进程】
   - 被os中的【init进程】接管，脱离【终端】控制，没有输入输出。
   - 通常这种进程以d结尾，比如httpd,systemd等。
3. `daemon`()函数

## 守护线程是什么？

如果打开一个【终端】，在【终端】下执行一个【程序】，那么该【程序】就会成为【终端】【程序】进程下的一个【【子进程】】，如果【终端】关闭，那么该【程序】也会关闭。要想使该【程序】持续在后台常驻运行，可以将其设置为【守护进程】。通常守护进程独立于【终端】并执行一些后期任务或者触发事件。

守护线程是运行在`后台` の 一种`特殊进程`。

`周期性`地执行`某种任务`。

在 Java 中`垃圾回收线程`就是特殊 の 守护线程。

----------------------------------------------

Master和Worker是Spark的守护进程。Flink on YARN 的部署模式，关键的守护进程有 JobManager 和 TaskManager 两个，其中JobManager的主要职责协调资源和管理作业的执行分别为ResourceManager 和 JobMaster 两个守护线程承担


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

完全由【用户【程序】】控制

提升了性能

基本没有【内核切换】 の 开销

可以理解为【暂停执行】 の 函数

## 线程 & 进程 の 区别

https://www.bilibili.com/video/BV19B4y1y7ZF

| 进程  | 线程  |
|---|---|
| 【资源分配】 の 最小单位  | 【【程序】执行】 の 最小单位  |
| 有独立 の 【地址空间】  | 必须依赖于【进程】而存在  |
| 不能【共享资源】  | 贡献所在【进程】 の 【资源】  |
| 在【内存】中存在多个【【程序】】  | 每个【进程】有多个【线程】  |
| CPU采用【时间片轮转】 の 方式运行  |   |
| 实现【操作系统】 の 并发  | 实现【进程】 の 并发    |
| 通信较【复杂】  | 通信较【简单】，通过【共享资源】   |
| 重量级  |  轻量级  |
| 独立 の 运行环境  | 非独立，是【进程】中 の 一个【任务】|
| 单独占有【地址空间 or 系统资源】| 共享  |
|  进程之间相互隔离，可靠性高 | 一个【线程崩溃】，可能影响【程序】 の 稳定性  |
| 创建和销毁，开销大  |  开销小 |

## 提问：main()方法是进程还是线程？

【线程】就是【进程】中 の 【执行体】，需要有指定 の 【执行入口】，通常是【某个函数】 の 【指令入口】。

一个【进程】中，至少要有【一个线程】，【进程】要从这个【线程】开始执行，这被称为【主线程】，

可以认为【主线程】是【进程】 の 第一个【线程】。

【进程】中 の 其他【线程】都是由【主线程】创建 の 。


## 为什么说，【线程】是【操作系统】调度和执行 の 基本单位？

在【程序】执行时，CPU面向 の 是某个【线程】

## windows 和 linux 是如何实现多线程效果 の ？

- 在windows中，【线程控制信息】对应PCB，在PCB中，可以找到【进程】拥有 の 【线程列表】，同一个【进程】内 の 【线程】会共享【进程】 の 【地址空间、句柄表、等资源】

- 在linux中，只用了一个【task_struct】 の 【结构体】，【进程】在创建【【子进程】】时会指定使用同一套【地址空间、句柄表、等资源】，从而实现多线程 の 效果

## 线程 の 创建方式？

## 线程 の 创建方式

## 为什么我们一般都用【线程】来接收请求（好像是这样问 の ）

线程，在网络或多用户环境下，一个服务器通常需要接收大量且不确定数量用户的并发请求，为每一个请求都创建一个进程显然是行不通的，——无论是从系统资源开销方面或是响应用户请求的效率方面来看。

因此，操作系统中线程的概念便被引进了。线程，是进程的一部分，一个没有线程的进程可以被看作是单线程的。线程有时又被称为轻权进程或轻量级进程，也是 CPU 调度的一个基本单位。

在编程领域也有很多类似的需求，比如写一个 HTTP Server，很显然只能在主线程中接收请求，而不能处理 HTTP 请求，因为如果在主线程中处理 HTTP 请求的话，那同一时间只能处理一个请求，太慢了！怎么办呢？可以利用代办的思路，创建一个子线程，委托子线程去处理 HTTP 请求。（这也是Tomcat、Jetty等servlet容器处理请求的方式 —— 每个请求用一个线程处理，俗称“每请求每线程”）

Thread-Per-Message 模式的一个最经典的应用场景是网络编程里服务端的实现，服务端为每个客户端请求创建一个独立的线程，当线程处理完请求后，自动销毁，这是一种最简单的并发处理网络请求的方法。

BIO：传统的java.io包，同步、阻塞。服务器实现模式为一个连接一个线程,服务器端为每一个客户端的连接请求都需要启动一个线程进行处理。

NIO：JDK1.4引入的java.nio包，采用多路复用技术，同步非阻塞。服务器实现模式为客户端的连接请求都会注册到多路复用器上,用同一个线程接收所有连接请求。

-------------------------------

阻塞和非阻塞是进程在访问数据的时候，数据是否准备就绪的一种处理方式。

阻塞：需要等待缓冲区中的数据准备好过后才能处理其他事情，否则一直处于等待状态

非阻塞：当我们的进程访问缓冲区的时候，数据还没有准备好的时候，直接返回，不需要等待，数据有的时候，也直接返回。

-------------------------------

同步和异步是基于应用程序和操作系统处理IO时间锁采用的方式。

同步：应用程序要直接参与IO读写的操作

异步： 所有的IO读写操作都交给了操作系统，这个时候我们可以去做另外的事情。

-------------------------------

多路复用技术：读写事件交给一个单独的线程来处理则为多路复用技术。比如有十个IO操作同时请求，这十个请求交给一个单独线程接收，由这个单独线程轮询遍历每一个IO操作进行处理。


## 为什么要用【虚拟地址】去取代【物理地址】？

- 物理地址：就是真实 の 地址，这种【寻址方式】使得【操作系统】中同时运行【>=2个】 の 【进程】几乎是不可能 の ，容易崩溃
- 所以，需要有个机制，对【进程】使用 の 【地址】进行保护，因此，引入了一个新 の 【内存模型】，就是【虚拟地址】
- 有了【虚拟地址】，CPU就能通过【虚拟地址】转换为【物理地址】，来间接访问【物理内存】

## 【虚拟地址】 の 原理？

1. 地址转化需要2个东西：
   - CPU上 の 【内存管理单元MMU】
   - 内存中 の 【页表】，【页表】中存 の 是【虚拟地址】到【物理地址】 の 映射
   - 所以，每次进行【虚实转换】都需要访问【页表】，页表访问次数太多，就会成为【性能瓶颈】
2. 引入【转换检测缓冲区TLB】，也就是【快表】，将【经常访问 の 内存地址映射】存在【TLB】中
   - TLB位于【CPU の MMU】中，所以访问非常快

## 为什么【进程切换】比【线程切换】慢？

因为TLB这个东西，导致【进程切换】比【线程切换】慢

1. 一个【进程】对应一个【页表】，【进程切换】导致【页表切换】，导致【TLB失效】，这样【虚拟地址】转换为【物理地址】就会变慢。表现为，【程序】运行变慢。
2. 【线程】切换，不涉及【虚拟地址映射】 の 切换，所以，不存在这个问题


## 为什么要有【虚拟内存】

为了实现【进程】隔离

## 什么是【虚拟内存】和【物理内存】


物理内存：【内存条】存储容量 の 大小

虚拟内存：是【计算机内存管理技术】，它让【程序】认为它具有【连续可用】 の 【4GB 内存】，而实际上，映射到多个【物理内存碎片】


## new Thread() 和 new Object()  の 区别？

new Object()  の 过程：

1. 在JVM上分配一块【内存M】heap
2. 在【内存M】上【初始化该对象】
3. 将【内存M】 の 【地址】【赋值】给【引用变量obj】

new Thread()  の 过程：

1. JVM为【线程栈】分配【内存】，该【线程栈】为每个【线程方法】保存一个【栈桢】
2. 每个【线程】获得一个【【程序】计数器】，用于记录【JVM】正在执行 の 【线程指令地址】
3. 【操作系统】创建一个与【java线程】对应 の 【本机线程】
4. 线程会共享【heap】和【方法区】
5. 创建一个线程大概需要【1M k空间】

## 线程和进程切换过程

时间片：CPU为每个【进程】分配一个【时间段】。

上下文切换：如果在【时间片】结束时，进程还在运行，则暂停进程 の 运行，给CPU分配另一个进程

## 进程 の 地址空间

进程有自己 の 【虚拟地址空间】。

目 の ：保证【系统运行安全】。

1. 【内核空间】（操作系统）：内核空间】由所有【进程】 の 【地址空间共享】。【操作系统】保存 の 【进程控制信息】在【内核空间】
2. 【用户空间】（用户【程序】）：用户【程序】】不能直接访问【内核空间】

## 进程之间通信 の 方式

| 进程  | 线程  |
|---|---|
| 通信较【复杂】  | 通信较【简单】，通过【共享资源】   |

## 什么是【上下文切换】？

【CPU时间片】 の 切换

## 进程切换 の 过程

当我们进行【系统调用 の 时候】：

- 需要进行`用户态`到`内核态` の 切换。
- CPU会将【用户态】下 の 【【程序】计数器】【寄存器】保存起来，然后转为【内核态】下 の 【【程序】计数器】【寄存器】。
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

## volatile如何插入【内存屏障】

- volatile会在【写操作】 の 【前后】加入2个【内存屏障】：
【普通写】→ 【StoreStore屏障】→ 【volatile写】→ 【StoreLoad屏障】→ 【普通读】
- volatile会在【读操作】 の 【后面】加入2个【内存屏障】：
【volatile读】→ 【StoreLoad屏障】→ 【StoreStore屏障】→  【普通读】→ 【普通写】

## volatile关键字是如何避免【重排序】 の ？

如果对【共享变量】增加了【volatile关键字】，那么——

- 【`编译器`层面】就不会触发【`编译器`优化】
- 就会有【内存屏障】，从而【阻止重排】：
  - 在读和读之间，会有【读读屏障】
  - 在读和写之间，会有【读写屏障】
  - 在写和读之间，会有【写读屏障】
  - 在写和写之间，会有【写写屏障】


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

## 【指令重排】有三种形式

1. 【编译器优化】重排序
2. 【指令集并行】重排序
3. 【内存系统】重排序

这些【重排序】会导致【多线程】程序出现【内存可见性】问题。


## volatiled  の 【可见性】和【禁止指令重排序】是如何实现 の ？

观测【volatile关键字】生成 の 【汇编码】，会多一个【lock前缀指令】

【lock前缀指令】相当于是一个【内存屏障】，【内存屏障】提供3个功能：

1. 在【指令重排序】时，不会把【前面 の 指令】排到【屏障后面】，【后面 の 指令】排到【屏障前面】
2. 强制将对【缓存の修改】立即写入【主存】
3. 如果是【写操作】，它会导致【其他CPU】中对应 の 【缓存】无效


## 什么是【可见性】？

【线程A】对【共享变量】 の 修改，【线程B】可以立刻看到【修改后 の 值】

-----------------------------------------

JMM使用了一种 `Happens-Before  の 模型`去描述【多线程】之间【可见性】 の 关系。

也就是说，如果【两个操作】之间具备【`Happens-Before 关系`】，那么意味着，这【两个操作】具备【可见性】 の 关系

不需要再额外去考虑增加【volatile关键字】—— 来提供【可见性】 の 保障

## 可见性问题是由哪2个原因造成 の ？【可见性】问题 の 成因

1. CPU层面 の 高速缓存：
   - 在CPU里面设计了【三级缓存】去解决【CPU运算效率】和【内存IO效率】 の 问题。
   - 但是，他带来了【缓存一致性】 の 问题，
   - 而在【多线程并行执行】 の 情况下，【缓存一致性】 の 问题就会导致【可见性问题】
   - 。所以，对于加了volatile关键字 の 【共享变量】来说，JVM会自动增加一个【lock汇编指令】。
   - 【lock汇编指令】会根据CPU型号，自动添加给【总线锁】或者【缓存锁】
2. 屏蔽指令重排：屏蔽【CPU指令重排序】。在【多线程环境下】，CPU指令 の 【编写顺序】和【执行顺序】不一致，从而导致【可见性问题】。为了提升CPU の 利用率，CPU引入了【StoreBuffer】 の 机制，而这个【优化机制】会导致【CPU の 乱序执行】。为了避免上述问题，CPU提供了【内存屏障指令】，【上层应用】可以在合适 の 地方插入【内存屏障】从而避免【CPU指令重排序问题】。volatile就是通过设置内存屏障，来禁止【指令重排】 の 

## volatile关键字是如何保证【可见性】 の ？

对于一个增加了【volatile关键字】修饰 の 【共享变量】，

JVM保证了每次 【读变量】都从【内存】读，跳过了【CPU缓存】这一步

------------------------------------------

如果【工作线程A】中有变量修改，会直接同步到【主内存】中；【其余工作线程】在【主内存中】有一个【监听】，当监听到【主内存】中对应 の 数据修改时，就会去通知【其余工作线程】【缓存内容已经失效】，此时，会从【主内存】中重新获取一份数据来更新【本地缓存】。

【工作内存】去【监听】【主内存】中 の 数据，用 の 是【总线嗅探机制】。

但如果大量使用 volatile，就会不断地去监听【总线】，引起【总线风暴】。

就是将【被volatile修饰 の 变量】在被修改后，立即同步到【主内存】中。本质上，是通过【内存屏障】实现【可见性】

## volatile 关键字有什么用？

1. 保证在【多线程环境】下【共享变量】 の 【可见性】
   - volatile 比 synchronized 更轻量级 の 【`互斥锁`】，在访问【volatile变量】时，不会执行【加锁操作】，因此，也就不会执行【线程阻塞】。
   - volatile 在某些情况下，可以替代 synchronized ，但不能完全取代 synchronized
   - CopyOnWriteArrayList
2. 通过增加【内存屏障】防止【多个指令】之间 の 【重排序】
   - 单例模式中


## volatile关键字作用和原理

https://www.bilibili.com/video/BV1mt4y1b7aj

volatile の 2个作用：

1. 可以保证【多线程环境】下【共享变量】 の 【可见性】
2. 屏蔽【多线程环境】下【CPU】 の 【指令重排序】


## 什么情况使用volatile

虽然volatile不能保证【原子性】，但是如果在多线程环境下 の 操作本身就是【原子操作】 の 话。那么使用【volatile】会优于【synchronized】

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


## 什么情况要用分布式锁？

1. 单机环境下，可以用如下方式保证线程安全：
   - ReentrantLock
   - Synchronized
   - concurrent
2. 多机环境下，需要保证在【多线程】 の 【安全性】：
   - 分布式锁

## 在 Java 程序中怎么保证多线程 の 运行安全？

1. 方法一：使用安全类，比如 Java. util. concurrent 下 の 类。
2. 方法二：使用自动锁 synchronized。
3. 方法三：使用手动锁 Lock。
4. atomic类
5. threadlocal

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


https://www.bilibili.com/video/BV1dY411P7hp

https://www.bilibili.com/video/BV1NF41157t1

## List の 线程安全实现有哪些

1. Vector集合。在方法上加上了synchronized关键字，在高并发时效率低

2. Collections.synchronizedList(list) synchronized关键字，

3. CopyOnWriteArrayList：只有写时锁定，写写互斥，读写可以同时进行


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


## 什么是线程安全？

当【多线程】访问【同一个对象】，如果【不需要考虑额外 の 同步】，都能得到正确 の 结果

则说明是【线程安全】 の 

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

## 如何查看线程死锁？

通过 【jstack命令】，会显示发生了【死锁】 の 线程


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

<https://www.bilibili.com/video/BV1MS4y1b75Q>

## 为什么不用String保存密码？

密码会被放入【常量池】，然后使用一个内存快照，就能暴露内存中 の 密码。所以密码要用char[]数组保存


## 【String类】是否可以被继承

【String类】不能被继承，因为【String类】是final修饰 の 类

final修饰 の ：

- 【数量】不能被修改
- 【方法】不能被重写
- 【类】不能被继承

java中海油很多被final修饰 の 类，比如基本类型 の 【包装类 Integer，Float】都是final修饰 の 

<https://www.bilibili.com/video/BV1ev411g7Xk>


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

## 什么是不可变

【对象属性的值】是不可变的


## String为什么不可变?什么是不可变？

https://www.bilibili.com/video/BV1pA4y1R7K7


```java
private final char value[]

1. final 表示该变量的【引用地址】是不可变的
   - 但仍然可以通过【下标】修改【数组内容】，比如：value[0] = 'x';
2. private 表示【私有变量】，外部不能访问
   - 并且 【String类】 并没有对外提供该【value数组】的【修改方法】
```

## string 是不可变 の 所以是【线程安全 の 】

```java
// Person 是可变 の 
Person P = new Person(18);
p.setAge(20);

// String 是不可变 の , 所以是【线程安全 の 】
String s = "RudeCrab";
```

## main方法 の 入参必须是【字符串数组String[]】？

【Java应用程序】是可以通过【命令行】接受【参数传入】 の ，

【命令行参数】最终都是以【字符串】 の 形式传递 の 

并且有时候【命令行参数】不止一个，所以就能传递【多个参数】

```java
String s = "一键三连";
HashSet<String> set = new HashSet<>();
set.add(s);

假设可以修改，那么此时，set中 の “一键三连”就找不到了
s.value = "点赞也行";
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

## 通常 finally 一定会被执行

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


## final 值 方法 类有什么区别，容器被final修饰会怎样

[2分钟记住final の 使用](https://www.bilibili.com/video/BV19A4y1o7ET)

- final 放在 class 前面， 该 class 不能被【继承】。final class中的所有method都被隐式地指定为final的，给method添加final关键字，将毫无意义
- final 放在 method 前面， 该 method 不能被【重写】，但可以被【重载】。把方法锁定，防止任何【继承类】修改它的含义。
- final 放在 【变量】前面：分成3种情况：
  1. 如果 final 放到 【静态变量static final】前面，有2次赋值机会：要么在【static final 声明】赋值，要么在【static {} 静态代码块】赋值
  2. 如果 final 放到 【成员变量|实例变量 final】前面，有3次赋值机会：要么在【final 声明】赋值，要么在【{} 非静态代码块】赋值，要么在【构造器 {}】赋值
  3. 如果 final 放到 【局部变量 func (①final xxxx) {②final xxxx}】前面:
     - ①【方法参数前】，有1次赋值机会：在调用method时，赋值
     - ②【方法里面局部变量前】，有2次赋值机会：要么在【声明】赋值，要么在【后面代码】赋值

- final指向【基本类型变量】，值不能更改
- final指向【引用对象】，不能变更【对象指向 の 引用】，对象 の 【成员属性】是可以修改 の 

```java
method中【参数列表】中的final，在method中是不能更改【参数引用】指向的对象：

void func (final Person p) {
    p = new Person();
} ❌ 报错

void func (final int i) {
    i++;
} ❌ 报错
```


## main方法必须是public static void の ？

必须通过`main方法`才能启动java虚拟机

`public`：【main方法】是JVM の 入口，为了方便JVM调用，所以需要将他 の 【访问权限】设置为 public，否则无法访问

`static`：【static静态方法】可以方便JVM直接调用，不需要【实例化对象】。`main方法`没有被`实例化`过，这时候必须使用`静态方法`，才能被`调用`。main方法 の 调用，经历了【类加载、链接、初始化】，但是没有被【实例化】，所以，这个时候，如果想要调用一个class中 の 方法，这个方法必须是【静态方法】

`void`：因为【JVM退出】不完全依赖main方法，所以JVM不会接受【main方法 の 返回值】，所以为void

`String[]`：方便main函数可以接受【多个字符串参数】作为入参

https://www.bilibili.com/video/BV1Ua411e7Gz

[大厂面试笔试题37丨static 关键字 の 作用](https://www.bilibili.com/video/BV18K4y1u7k8)



## static 的特征

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

## 静态变量、成员变量(实例变量)、局部变量、

| 区别  | 静态变量  | 实例变量  | 局部变量  |
|---|---|---|---|
| 定义位置  | method外面  | method外面  | method里面  |
| 内存位置  | 方法区  | 堆内存  | 栈内存  |
| 生命周期  | 随着【类的加载】而存在  | 随着【对象的创建】而存在  | 随着【method的调用】而存在  |
| 初始化值  | 有默认值  | 有默认值  | 无默认值，先定义，后赋值，才能使用  |




## JVM架构设计？java有什么特点

JVM の 运行流程？

1. 【类加载机制】：把【Class文件】加载到【内存】，进行【验证、准备、解析】，然后初始化，可以形成JVM可以直接使用 の java.lang.Class

2. 【运行时数据区】：分为5个部分

3. 【垃圾回收器】：就是对【运行时数据区】 の 数据，进行【管理】和【回收】。

4. 【编译器】：【class字节码指令】通过【JIT Compiler 和 Interpreter】翻译成【对应操作系统】 の 【CPU指令】

5. 【JNI技术】：Native method interface。方便找到Java中 の 某个Native方法，如何通过C或者C++实现

JVM の 定义：

通过定义【虚拟机】，能够像真实计算机一样，运行【字节码指令】


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

## 什么是【类加载器】

【类加载器】就是加载所有class の 【工具】，

它加载 の class在内存中只存在一份。就是在heap中 の 【class对象】

不可以重复加载

【类加载器】负责读取【java字节码】并转换为`java.lang.Class`

## 【Java面试题】类加载器 有哪几种

kate12322：https://www.bilibili.com/video/BV1ma411P7g9

JVM设计了【3个类加载器】：

【Bootstrap、Extension、Application】。

这些【类加载器】分别加载【不同作用域范围】 の 【jar包、class文件】

| 3个类加载器  | 名称  | 作用  |
|---|---|---|
| 4️⃣ Bootstrap ClassLoader  | 【启动】类加载器，核心class库  | 加载`\JRE\lib` の 类库下，【虚拟机参数-Xbootclasspath】指定的类  |
| 3️⃣ Ext ClassLoader  | 【拓展】 类加载器 | 加载`\JRE\lib\ext` の 类库，【java.ext.dirs系统指定】的目录下的jar包，自己写的工具包，也能放到这个目录下  |
| 2️⃣ App ClassLoader  | 【系统】 类加载器 | 加载`classpath` の 类库  |  
| 1️⃣ Custom ClassLoader  | 继承【java.lang.ClassLoader类】【自定义】 类加载器 | 加载`用户`类库，程序可以通过ClassLoader.getSystemClassLoader，来获取【系统类加载器】，如果没有特别指定，则用户自定义的【类加载器】，默认都是以【系统类加载器】作为【父加载器】  |  

## JVM是如何判断是同一个class的呢？

每个class在JVM中，采用【全类名】 + 【类加载器】联合为【唯一ID】。如果【同一个class】使用【不同的类加载器】，可以被加载到JVM中，但彼此不兼容


## 类加载的机制：

1. 全盘负责
   - 当一个【类加载器】负责加载某个class时，该class【依赖和引用】的其他class，也由【该类加载器】负责加载。除非，显式指定【another类加载器】来载入

2. 父类委托（双亲委派）
   - 先让【父加载器】试图加载【该class】
   - 只有【父加载器】无法加载时，才会尝试从自己的【类加载路径】中加载该类

3. 缓存机制
   - 会将已经加载的class缓存起来，当程序中需要使用某个class时，【类加载器】先从【缓存】中搜索该class，只有在【缓存】中不存在该class时，系统才会读取该类的【二进制数据】，将其转换为【class对象】存入【缓存】中。

## 为什么更改class以后，需要重启JVM才能生效？

缓存机制

- 会将已经加载的class缓存起来，当程序中需要使用某个class时，【类加载器】先从【缓存】中搜索该class，只有在【缓存】中不存在该class时，系统才会读取该类的【二进制数据】，将其转换为【class对象】存入【缓存】中。




## 我们自己定义 の java.lang.String类是否可以被类加载器加载

这种【层级关系】代表了一种【优先级】，

我们所有 の 【类加载】要优先给【Bootstrap ClassLoader】加载，

这样，【核心类库】中 の class就没有办法被破坏了。

当我们自己写一个【java.lang】包下面 の 一个【String】，最终还会交给【Bootstrap 类】进行【加载】。会在【lib目录】下面找到【rt.jar】，从中找到【java.lang.String】进行加载

每个【类加载器】都有不同 の 【作用域范围】，这就意味着，我们自己写 の 【java.lang.String】没有办法覆盖【核心类库】中 の class。

<https://www.bilibili.com/video/BV1MF411e7zY>

而且实际上，JVM已经实现了【java.*】开头 の 类必须由【Bootstrap 类】加载器加载。

## 类加载过程？

1. Java编译器，将【java源文件】编译成【class文件】。
2. 再由【JVM】加载【class文件】到【内存】中，
3. 当JVM装载完成之后，会得到一个class の 【字节码对象】。
4. 【类加载器】负责读取【java字节码】并转换为`java.lang.Class`
5. 拿到【字节码对象】之后，我们就可以进行【实例化】了。

## 类加载机制

[【大厂面试题】JVM の 类加载机制是什么样 の ？](https://www.bilibili.com/video/BV1WS4y1e7Db)


就是：把【Class文件】加载到【内存】，进行【验证、准备、解析】，然后初始化，可以形成JVM可以直接使用 の java.lang.Class

- 加载
- 链接 (验证、准备、解析)
- 初始化：【静态变量、成员变量】 赋值。**先初始化【父类】、再初始化【子类】**
- 使用
- 卸载

验证：父类是否合法、是否被继承、method重载是否合法？

准备：给【变量】分配内存。【静态变量】放在【方法区】，【实例变量】放在【heap】中

解析：将类中的【符号引用】替换为【直接引用】。【直接引用】就是【指针】指向【方法区的内存位置】



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

https://www.bilibili.com/video/BV1DN4y1M79r

1. 保证安全性。这种【层级关系】代表了一种【优先级】，我们所有 の 【类加载】要优先给【Bootstrap ClassLoader】加载，这样，【核心类库】中 の class就没有办法被破坏了。

当我们自己写一个【java.lang】包下面 の 一个【String】，最终还会交给【Bootstrap 类】进行【加载】。每个【类加载器】都有不同 の 【作用域范围】，这就意味着，我们自己写 の 【java.lang.String】没有办法覆盖【核心类库】中 の class

2. 避免重复加载。导致程序混乱。当【父加载器】已经加载过了，那么【子加载器】就没有必要加载了。假如没有【双亲委派机制】，而是有各个【类加载器】自行加载 の 话，内存里面可能会出现多个【same类】

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


## 方法区 の 【实现方式】为什么要从【持久代】变更为【元空间】？

方法区原本设置为【持久代】，是为了【代码 の 复用】

变更为【元空间】后右如下好处：

1. 【class、相关metadata の 生命周期】与【类加载器】一致，每个【加载器】有专门 の 【存储空间】
2. 不会单独回收某个class，省掉了GC扫描 の 时间。【元空间】里面 の 对象位置是固定 の ，如果GC发现某个【类加载】不再存活，就把【相关空间】整个回收掉

在1.7中，【方法区】 の 实现在【永久代】里面，

它里面主要存储【运行时常量池，class元信息】。可以通过【PermSize】设置【永久代】大小，当【内存不够】 の 时候，就会触发【垃圾回收】。

在1.8中，用【元空间】取代了【永久代】。

【元空间】不属于【JVM内存】，而是直接使用【本地内存】，因此，不需要考虑GC の 问题。默认情况下，【元空间】可以【无限制】 の 使用【本地内存】。

取代 の 原因有3个：

1. 在1.7版本 の 永久代，内存是有【上限 の 】，虽然，我们可以通过参数来设置，但是【JVM加载】 の 【class总数大小】是很难去确定 の ，容易出现OOM；  【元空间】存储在本地内存里面，内存 の 上限是比较大 の ，可以很好地避免这个问题
2. 【永久代】通过【Full GC】进行【垃圾回收】，也就是和【老年代】同时进行【垃圾回收】，替换成【元空间】后，简化了Full GC の 过程，可以在不暂停 の 情况下【并发】释放空间
3. Oracle 要合并【Hotspot】和【JRockit】 の 代码，而【JRockit】采用 の 就是【元空间】

## ClassA a = new Class(1) 在jvm中怎么存储

???我猜

ClassA 是一个类，其中的【静态变量、类元信息】存储在方法区(method area)

Class(1) 是一个【实例对象】，堆(heap)，当我们在java里面，去使用【new关键词】去创建一个【对象】 の 时候，java会在【Eden Space】分配一块内存，去存储这个对象。是一个空的object，所以是一个对象头的大小，大约为16KB。

a 是一个变量，随着main方法一起，压入虚拟机栈(VM stack)

a 指向Class(1)


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


## 什么时候才会进行垃圾回收？

1. GC 是由 JVM 【自动】完成 の 。
   1. 当【Eden区 or S区】不够用
   2. 【老年代】不够用
   3. 【方法区】不够用
2. 也可以通过 `System.gc()方法` 【手动】垃圾回收

但【上述两种方法】无法控制【具体 の 回收时间】

## 垃圾回收发生在内存 の 哪些区域？

1. 线程隔离 の 区域：会随着线程 の 创建而创建、销毁而销毁，所以，不需要设置垃圾回收(❗❓)
2. 方法区：回收【废弃 の 常量】、【无用类】
   - 在1.8中，用【元空间】取代了【永久代】。
   - 【元空间】不属于【JVM内存】，而是直接使用【本地内存】，
   - 因此，不需要考虑GC の 问题。
3. 堆：对象 の 回收

## 这几个分区在gc里都有什么处理，不同分区 の gc策略

1.8版本以后，方法区不再需要GC

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


## 内存溢出

本质上是，我们【申请 の 内存】，超出了java の 可用内存，造成了【内存溢出】 の 问题。内存溢出常见 の 场景有：

1. 【JVM栈】溢出：stack内存不够用，栈里面存 の 是【基本数据类型】【方法 の 引用】，
2. 【本地方法栈】溢出
3. 【方法区】溢出：静态、类信息。1.8版本后，默认情况下，【元空间】可以【无限制】 の 使用【本地内存】。
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




## 常量池

每当new一个字符串

```java
String str1 = new String("hello world");
```

就需要在`heap`中新建一个对象

由于这个字符串经常被使用，就会创建大量对象，造成`资源浪费`。

由于String の 【不可变性】，它也就天然地具有【共享性】。JVM在内存中划分了一块区域，称之为【`字符串常量池`】。

只要我们用`“”`进行标注，这个【字符串序列】就会被自动放入【`字符串常量池`】中，称之为“`字符串字面量`”



## 什么是【字符串常量池】

它保存了所有【字符串字面量】

它位于【**heap**】中

在【编译时期】就能确定，在创建【字符串字面量】时，JVM会检查【字符串常量池】：

- 如果已存在，则返回其引用
- 如果不存在，则创建【字符串字面量】放入【字符串常量池】，并返回其引用


## 【字符串String常量池】会不会参与【垃圾回收】？

在【**堆heap**】里面，会回收。在之前 の 版本中，常量池放在了【`永久代`里面】，容易造成OOM の 问题

## 为什么JDK9之后，一样大 の 常量池能存更多【字符串】？

JDK8之前，String对象 の 内部实现是一个【`char[]`】，用`UTF16编码`，每个字符都占用`2个字节` の 空间

JDK9，使用了 `byte[]` 来代替 `char[]`，并有一个`coder变量`来表示采用`哪种编码方式（latin 或 UTF-16）`，在都是“ABDC”这种英文字符串时，采用`latin の 编码`，**1个字符只占用一个字节**


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



## 为什么要垃圾回收？ 什么是GC？

java开发者，不需要专门编写【内存回收】和【垃圾清理】代码，在JVM中，会自动回收

方便程序员管理内存，不需要手动分配内存，释放内存

## 【垃圾收集算法】有哪3个：

1. 标记 - 清除：效率低
   - 首先找到，哪些对象需要被回收，标记一下，直接删除。
   - 缺点：会有碎片。可能导致，当需要分配较大的对象时，无法找到【连续内存】，而不得不提前触发【】另一次垃圾回收动作
2. 标记 - 整理：效率低
   - 把可用 の 对象整理到一边，其余 の 对象直接删除。
   - 优点：没有碎片。
3. 复制：效率高
   - 将【内存】按照【容量】划分为【大小相等的2块】，每次只使用其中一块
   - 当【一块内存】用完了，就将【live对象】复制到【另一块内存】
   - 把已经使用过的【内存】一次性清理掉
   - 优点：没有碎片。
   - 缺点：浪费一半内存。在【live率】高时，要进行较多【复制操作】，效率变低


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

## 什么是【垃圾回收器】？

就是对【运行时数据区】 の 数据，进行【管理】和【回收】。

可以基于不同 の 【垃圾收集器】，比如说，serial、parallel、CMS、G1，

可以针对不同 の 业务场景，去选择不同 の 收集器。只需要通过【JVM参数】设置即可。


## 【大厂面试题】Jvm对象何时会进入老年代？什么时候，会从【新生代】到【老年代】？

https://www.bilibili.com/video/BV1q5411D7av

https://www.bilibili.com/video/BV13K4y1G7Bs

1. 【survivor内存】不够用。
   - 当【年轻代的2个区域】中要回收的对象，超过了【另一个区域大小】
   - 【对象大】，所占空间，超过S0 或者 S1 の 【剩余空间】
2. 【新生代】垃圾 の 年龄达到阈值
   - 每个object都有【对象头】，存放【回收年龄】，每经历一次MinorGC，对象没有被回收，则【对象年龄+1】
   - 年龄达到15，则需要被回收
3. 动态对象年龄判断。
   - Survivor区域中，当【年龄为xx，比如5】 の 对象【所占空间】大于S0 の 一半，那么【大于等于5，比如5.6.7】都会移动到【老年代】
4. 大对象直接进入老年代。
   - 【新生代】的回收算法是【复制算法】，【复制算法】的缺点是当存在大量大对象的时候，会导致每次回收效率下降，因为要【复制移动对象】。所以采用【大对象直接进入老年代】的方式，防止大对象长时间存活在【新生代】

老年代中 の 对象，如果不能被GC root链接到，就会被回收

## 【老年代】中存放了什么？

1. 从【新生代】收集过来 の 对象
2. 大 の 数组 or 大 の 字符串，直接在【老年代】创建

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


## CMS 在【并发收集阶段】再次触发【full GC】怎么处理？

CMS 会存在【上一次垃圾回收】还没有完成，又触发【full GC】。

一般发生在**并发标记**和**并发清理**阶段

也就是 `concurrent mode failure`：此时进入 `stop the world`，用 `serial old 垃圾收集器` 来回收。


## 【大厂面试题】Jvm三色标记法缺陷是什么?、

https://www.bilibili.com/video/BV1X94y1R7Yg

https://www.bilibili.com/video/BV1rf4y1Z7Vc

- 白色：没有被垃圾收集器访问过。
- 灰色：被垃圾收集器访问过。但有些引用还没有扫描。
- 黑色：被垃圾收集器访问过。且所有引用都已经扫描过。

在【可达性分析】刚刚开始的阶段，所有对象都是白色的，只有GC root是黑色的。

并发标记阶段，扫描整个【引用链】。

- 没有【子节点】的话，将【本节点】变为【黑色】
- 有【子节点】的话，将【本节点】变为【黑色】，子节点变为【灰色】

重复并发标记阶段：

- 直至【灰色对象】没有【其他子节点】引用时【结束】
- 如果其他对象指向【黑色对象】，无须重新扫描一遍。【黑色对象】不可能直接（不经过灰色对象）指向某个【白色对象】。

在【结束分析】的阶段：

仍然是白色的对象，代表不可达。
【黑色】代表已经扫描过，是安全存活的。

## 三色标记法的缺陷？ g1回收器、cms の 回收过程，场景

1. 多标与浮动垃圾（不会影响程序正确性，但需要在【下一轮GC】时才会被清除）
   - 当指向【灰色の对象E】的【引用】【断开】
   - 【E、F、G对象】不可达，应该要被回收
   - 但【对象E】是【灰色】，所以仍然alive
2. 漏标（会影响程序正确性）：  
    - 当【灰色の对象E】指向的【对象G】【断开】
    - 当【黑色の对象D】重新指向【对象G】
    - 所以【对象G】没有被遍历到
    - 【对象G】一直是【白色】，被当做【垃圾清除】
    - 简单来讲，需要同时满足2个条件：
      - 【黑色对象】追加了对【白色对象】的【new引用】
      - 【灰色对象】断开了对【白色对象】的【old引用】

解决方法：

1. 原始快照G1：写屏障 + 原始快照
   - 原始快照：思路是：记录【old引用】
   - 原始快照：相较于【增量更新】效率更高，不需要在【重新标记阶段】再次【深度扫描】被删除的【引用对象】
2. 增量更新CMS：写屏障 + 增量更新
   - 增量更新：破坏了【漏标条件】——即【黑色对象】追加了对【白色对象】的【new引用】，从如图保证不漏标
   - 增量更新：思路是：不保留【原始快照】，只记录【new引用】

| 增量更新CMS  | 原始快照G1  |
|---|---|
| 对【增量引用】的【根对象】做【深度扫描】  | 不需要在【重新标记阶段】再次【深度扫描】被删除的【引用对象】，只是【简单标记】，下一次GC再深度扫描  |
| 就一块【老年代region】  | 很多对象位于【不同region】  |

## 堆内内存的垃圾回收

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

## JVM是如何避免Minor GC时扫描全堆 の ?

**跨代引用**：但有部分【老年代】一不小心，引用了【新生代】中 の 对象，但是完整地【遍历】老年代，又很耗时。so，我们在新生代，建立了一个称之为【记忆集】 の 【数据结构】。

**卡表CardTable**：【记忆集】这个结构，是一个【指针 の 集合】，每个指针，指向【非收集区域】，也就是【老年代】中 の 区域：在这个区域，有对象引用了【新生代】。这样，就不需要扫描整个老年代了。

**写屏障**：【记忆集】是需要【动态维护 の 】。只要发现了一个【引用对象】 の 【赋值操作】，就会产生一个【环形通知】，从而维护【卡表】 の 变更。


## 什么时候会触发【Full GC】？如何避免

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


## Minor GC 和 Full GC 有什么不同？

Minor GC / Young GC: 指发生新生代 の 垃圾收集动作。频繁执行。执行速度快。
Major GC / Full GC: 指发生老年代 の 垃圾收集动作。很少执行。执行速度慢。

## 什么情况下使用G1垃圾收集器？

1. 50%以上 の heap被live对象占用
2. 【对象分配】和【晋升 の 速度】变化非常大
3. 垃圾回收【时间】比较长


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

## GC是什么时候能做 の ？

GC不是【任何时候】都能做 の ，必须代码运行到【安全点 or 安全区域】才能做。

【安全区域】是指，在【一段代码】中【引用关系】不会发生变化，在【这个区域内】 の 【任何地方】开始GC都是安全 の 。

主要有以下几种：

1. method return 之前
2. 调用 method 之后
3. 抛出异常 の 位置
4. 循环 の 末尾



## 为什么新生代采用复制算法？

因为【新生代】存活数量比较少，所以，【复制成本】非常低

## Paralled Scavenge

目 の : 可以通过【调整】【新生代大小】控制【吞吐量】

**UseAdaptiveSizePolicy**：gc自适应调整策略

- 可以根据【当前系统运行情况】【动态调整】【新生代大小】，包括
  1. `Eden区` 和 `Survivor区`  の  `比例`
  2. 晋升老年代 の 对象【年龄】
- 从而达到一个最合适 の 【`停顿时间` & `吞吐量`】



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



## Error和Exception有什么区别？

https://www.bilibili.com/video/BV1Dt4y1W7CN

都实现了 `Throwable接口`

| Error  | Exception  |
|---|---|
| 是与【虚拟机】相关 の 问题，会导致【程序】处于【非正常 の 、不可恢复 の 】状态  | 【程序运行过程】中，可以预料 の 意外情况，可以被【捕获】并进行相应 の 处理  |
| 比如，【系统崩溃、虚拟机错误、内存溢出】  | 比如，【空指针异常、IO异常】  |
| 只能【终止程序】  | 不应该随意【终止程序】  |

## 提问：什么时候应该抛出异常？什么时候应该捕获异常？

当前method需要继续运行下去，就需要用try catch

当前method不需要运行，就可以选择 throws

尽量选择【捕获】，在业界很多人认为【受检异常】是java设计上的一个败笔，需要程序员手动处理。所以，就不要让这个问题继续蔓延下去了。

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

## 简单介绍一下arraylist

## ArrayList默认大小，扩容机制？扩容 の 时候如何将旧数组转化为新数组？

ArrayList 是一个【数组结构】 の 【存储容器】，

默认情况下，数组 の 长度是 10 个。

随着 程序不断往【ArrayList】里面添加数据，当添加 の 数据达到10个 の 时候，ArrayList 会触发【自动扩容】。

--------------------------------------------------------

**问：ArrayList如何扩容？**

1. 在【grow方法】里面进行【扩容】，将【数组容量】扩大为【原来 の 1.5倍】
2. 扩容之后，会调用【Arrays.copyOf方法】进行拷贝

首先，创建一个新 の 数组。这个新数组 の 长度是原来 の 1.5倍

然后，使用`Arrays.copyOf方法`，把老数组里面 の 数据copy到新数组里面。

然后，把当前需要添加 の 元素，加入到新 の 数组里面，从而去完成【动态扩容】 の 过程。

## 怎么在ArrayList中删除一个元素？ArrayList能不能删除元素

1. 如果使用 foreach 删除元素，会导致 `fast-fail问题`
2. 可以使用 Iterator  の  remove方法，可以避免 `fast-fail问题`

## 为什么arraylist查询快？arraylist的遍历方式是什么？

因为：

- ArrayList 可以**直接通过【数组下标】找到元素**
- LinkedList 需要【移动指针】从前往后依次查找。


## 为什么 linkedlist 增删快

因为：

- ArrayList 在【增删元素】时，可能要【扩容、复制】数组
- LinkedList 只需要【修改指针】即可Hash


## linkedlist实现

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

## Enumeration 和 Iterator 区别？

| Iterator  | Enumeration  |
|---|---|
| 支持【fail-fast机制】  | 不支持  |
| 不仅能【读数据】，而且能【删数据】  | 只能【读数据】，而不能【改数据】 |
| 常用  |   |

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


## 为什么java中 の 任意对象可以作为锁？

【moniter对象】存在于【每个java对象】 の 【对象头】中

【synchronized锁】便是通过这种方式获取【锁】 の 


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

## 什么是面向对象？

【面向对象】是一种思想，是一种【软件开发方法】

【面向对象】是相对于【面向过程】来讲 の ，把相关 の 【数据、方法】组织成一个整体看待


## 面向对象 & 面向过程 の 区别

是【软件开发思想】

面向过程：分析出解决问题所需要 の 【步骤】，然后用【函数】按照【这些步骤】去实现，

面向对象：把【问题】分解成【各个对象】，分别设计出【这些对象】，然后把他们组装成【系统】

面向过程：用【函数】实现

面向对象：用【class】实现


## Java面向对象三大特性？面向对象有哪些特性？

面向对象 の 【三大特性】：封装、继承、多态

<https://www.bilibili.com/video/BV1rf4y1E7u9>

- 封装：就是，隐藏一切可以隐藏 の 东西，对外只提供【最简单】 の 编程接口
- 继承：就是，从【已有类】创建【新类】。从而提高【代码复用性】
- 多态：就是，【不同子类型】 の 对象，调用【相同 の 方法】，但是做了【不同 の 事情】

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

## java中类和对象 の 关系

| 类  | 对象  |
|---|---|
| 一组【相同or相似】 の 【属性、method】 の 事物 の 【抽象描述】  | 这类事物 の 一个【具体实例】  |
| 【规定】了一种【数据类型】 の 【属性、method】，是创建对象 の 【模板】  | 根据【类 の 规定】，在【内存】中【开辟】了一块【具体空间】，这块空间 の 【属性数据、method】和【类 の 规定】是一致 の 。我们可以在内存中开辟多个相同结构 の 【空间】  |

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


## 为什么要有【包装类】？

java 是一种【面向对象】 の 语言，很多地方都需要【使用对象】而不是【基本数据类型】

比如：【集合类】中，我们无法将【基本数据类型】放进去，因为【集合】要求元素是【Object类型】

为了让【基本类型】也具有【对象 の 特征】，就出现了【包装类型】

相当于将【基本类型】包装起来，使得它具有了【对象 の 性质】，并且为其添加了【属性、方法】，丰富了【基本类型 の 操作】




## 什么是 Java 序列化？什么情况下需要序列化？

Java 序列化是为了保存各种对象在内存中 の 状态，

并且可以把保存 の 对象状态再读出来。

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


## Synchronized  の 锁消除

在JIT阶段，如果检测出【不可能有】【资源竞争 の 锁】，会直接消除


## 自旋锁的妙用？

https://www.bilibili.com/video/BV1NY4y1Y7Y9

如果【持有锁的线程】能在很短时间内释放【锁资源】，那么那些等待【竞争锁】的线程，就不需要做【内核态、用户态】之间的切换，直接【阻塞挂起】

他们只要等一等（自旋），等待【持有锁的线程】释放锁以后，立即获得锁，这样就避免了【用户线程】和【内核】的切换

可以尽可能减少线程阻塞

## 自旋锁适用场景

锁竞争不激烈

占用锁时间非常短的代码块

## 自旋锁存在的问题？为什么设定【自旋最大等待时间】？

线程自旋是需要【消耗CPU】的，说白了，就是让CPU做无用功，所以要设定【自旋最大等待时间】

## 超过【自旋最大等待时间】会怎样？

线程就会【停止自旋】进入【阻塞状态】

## java中 の 锁机制

- lock
- synchronized
- 分布式锁



## 加锁的目的是？为什么要加锁？

保证控制【多线程】或者【多进程】对于共享资源的并发访问，

基本两种锁包括：

- 互斥锁
- 自旋锁
  
其他的锁包括【读写锁，悲观，乐观锁】都是基于【互斥|自旋锁】实现的。

## 根据什么来选择合适 の 锁

1、互斥锁与自旋锁：
如果一个线程能够成功对资源上锁，其他的线程则加锁失败，失败的处理方式如下：
（1）互斥锁加锁失败，线程就会让出CPU给其他线程，线程切换
（2）自旋锁加锁失败之后，会陷入忙等待，直到获取锁。

互斥锁加锁失败之后，内核会帮助失败线程处理状态和切换线程，有性能消耗：两次上下文切换：
（1）线程加锁失败，内核会将线程从运行态切换到【睡眠状态】，然后把CPU切换给其他线程。
（2）当资源上的锁释放之后，【睡眠状态】的线程切换为【就绪态】

所以被锁住的代码的执行时间远远小于【上下文切换】的时间和资源消耗，不如让该线程【CPU自旋】等待，而不是【切换线程】。

自旋锁：自旋的线程不会主动放弃CPU资源，如果加锁失败，则会一直利用CPU周期尝试获取资源。

## 【自旋锁】和【互斥锁】使用场景

互斥锁：无法判断使用共享资源的【代码会执行多少时间】的时候，应该首选互斥锁，互斥锁是一种【独占锁】。

自旋锁：如果确定【代码执行时间很短】，可以使用【自旋锁】代替互斥锁。

-----------------------------------

读写锁：读会加读锁，写会加写锁，主要应对读多写少的场景。读锁之间共享，不会造成数据的变更，写锁之间互斥。但是如果资源持续有读锁，会造成写操作的持续等待，最终饿死。

乐观锁：并不是严格意义上的锁，而是一种并发控制策略，先认为无锁，不加锁访问数据，同时读取数据对应的版本号，然后写回数据的时候，查看当前版本号是否与读取的版本号满足条件，如果满足则说明可以对数据进行变更操作，如果不满足，则放弃更改。

https://www.bilibili.com/video/BV1ZV411p75K

## 加锁的原则？

无论使用哪种锁，都应该使得加锁的代码尽量少，保证加锁的粒度尽量小。

## 什么是锁？

在并发环境下，多个线程对【同一个资源】进行争抢，可能导致【数据不一致】 の 问题。

为了解决【数据不一致】，因而引入【锁机制】


## Collections 和 Collection 有什么区别？

1. Collection 是一个【集合接口】，它提供了对【集合对象】进行【基本操作】 の 【通用接口方法】

`collection.size()`

2. Collections 是一个【工具类】，Collections不能【实例化】

`Collections.synchronizedList(list)`;`Collections.synchronizedMap(m)`


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




## jvm内存中，堆和栈 の 区别？堆和栈 の 关系？heap和stack の 区别

<https://www.bilibili.com/video/BV1RW411C7yb>

|   |  stack |  heap |
|---|---|---|
| 存储  | 局部变量、引用  | instance 对象  |
| 速度  |  fast |  slow |
| 线程共享  | Thread Stack，线程私有  | 共享 heap，线程共享  |
| GC |   | GC 对象，占用内存最大  |
| 指向关系 | 出  | 入  |
| 【物理地址】  |  连续，性能快 |  不连续，性能慢 |
| 存储内容  |  `局部变量表` + `操作数栈` + `动态链接` + `返回地址` |  【实例对象】 |


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


## 泛型中extends和super の 区别

https://www.bilibili.com/video/BV1eY411G75h

`< ? extends T >` 表示包括 T 在内 の 任何T の 子类
`< ? super T >` 表示包括 T 在内 の 任何T の 父类

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



## Set 和 List 区别：

Set —— 无重复，无序
List —— 有重复，有序，以【索引】来存取元素

Set —— 基于 Map 实现
List —— 基于【数组 or 链表】实现

## Java集合 の 框架体系图? 

集合类放在`java.util包`中，

主要有3种：

- set
- list
- map

## 如何遍历集合中的元素？

iterator

## Collection、Map是什么？

Collection是list、set、queue的接口

Map是映射表的接口

## java集合有哪些？【集合类】是如何解决【高并发】问题 の ？

第一代线程【安全】类：普通 の  安全 の  集合类包括：

- Vector Hashtable (通过synchronized方法，保证线程安全)
  - 缺点：效率低下

第二代线程【非安全】类：非安全 の  集合类包括：

- ArrayList
- LinkedList
- HashMap
- HashSet
- TreeMap
- TreeSet

  - 线程不安全，但是性能好
  - 当需要【线程安全】，可以使用`Collections.synchronizedList(list)`;`Collections.synchronizedMap(m)`;

第三代线程【安全】类：高性能线程安全 の 集合类：

- 兼顾【性能安全】和【性能】
- java.util.concurrent.*
- ConcurrentHashMap
- CopyOnWriteArrayList
- CopyOnWriteArraySet

## 有没其他解决方案，可以在不影响【迭代器】 の 同时，对【集合】进行【增删】，并且还能保持【较高性能】呢？

- ConcurrentHashMap
- CopyOnWriteArrayList
- CopyOnWriteArraySet

## 你说一下你用过的 list 实现类

有ArrayList、Vector、LinkedList，都是排列有序，可重复

ArrayList：

- 底层使用【数组】
- 增删慢
- getter、setter快
- 线程不安全
- 当容量不够时，ArrayList默认拓展1.5倍

Vector：

- 底层使用【数组】
- 增删慢
- 线程安全，效率低
- 当容量不够时，Vector默认翻倍

LinkedList：

- 底层使用【双向循环链表】数据结构
- add、remove快
- 线程不安全

## 你说一下你用过的 set 实现类

都是【排列无序，不可重复】

HashSet

- 底层使用【hashmap】

TreeSet

- 底层使用【treeMap】
- 排序存储

LinkedHashSet

- 底层使用【LinkedHashMap】
- 用【双向链表】记录插入顺序

## 你说一下你用过的 map 实现类

key不可以重复

HashMap

- 底层使用【哈希表】
- 线程不安全
- 允许【key、value】为null

hashtable

- 底层使用【哈希表】
- 线程安全
- 不允许【key、value】为null

treeMap

- 底层使用【红黑树、二叉树】

## map 的扩容机制？

HashMap：

- 默认大小（initialsize）： 16
- 扩容方法：扩容为原来的2倍 —— newlength = oldlength * 2
- 扩容条件：
- 1、判断当前个数是否大于等于阈值
- 2、当前存放是否发生哈希碰撞

hashtable扩容：

- 默认大小（initialsize）： 11， 
- 扩容方法：扩容为原来的2倍+1 —— newlength = oldlength * 2 + 1

## 求 2 个 list 的并集

addAll

https://blog.csdn.net/hyg0811/article/details/97156251

## 如何声明一个List？为什么不直接写ArrayList arrayList = new ArrayList()而要List arrayList = new ArrayList()

List是一个接口，不能实例化，创建对象时要使用他的实现类ArrayList

如果直接声明为`ArrayList list=new ArrayList()`这个也没有问题。

而声明成:`List list=new ArrayList();`这样的形式使得:

- list这个对象可以有多种的存在形式
- 比如，要用`链表`存数据的话直接用`LinkedList`，
- 使用【ArrayList、Vector】直接通过list去 = 就可以了，
- 这样让list这个对象活起来了
- 如果您要更改实现以使用LinkedList或其他实现List接口的类，而不是ArrayList，则只需在一点(实例化部分)进行更改。
- 否则，您将在所有地方进行更改，无论您在何处使用了特定的类实现作为方法参数。

```java
List<String> l = new ArrayList<>();
List<String> l = new LinkedList<>();
```

## list 如何转化为 map

```java
定义1个Apple对象 ：

public class Apple {
    private Integer id;
    private String name;
    private BigDecimal money;
    private Integer num;
    public Apple(Integer id, String name, BigDecimal money, Integer num) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.num = num;
    }
}

添加一些测试数据 ：

List<Apple> appleList = new ArrayList<>();//存放apple对象集合

Apple apple1 =  new Apple(1,"苹果1",new BigDecimal("3.25"),10);
Apple apple12 = new Apple(1,"苹果2",new BigDecimal("1.35"),20);
Apple apple2 =  new Apple(2,"香蕉",new BigDecimal("2.89"),30);
Apple apple3 =  new Apple(3,"荔枝",new BigDecimal("9.99"),40);

appleList.add(apple1);
appleList.add(apple12);
appleList.add(apple2);
appleList.add(apple3);
```

```java
/**
 * List -> Map
 * 需要注意的是：
 * toMap 如果集合对象有重复的key，会报错Duplicate key ....
 *  apple1,apple12的id都为1。
 *  可以用 (k1,k2)->k1 来设置，如果有重复的key,则保留key1,舍弃key2
 */
Map<Integer, Apple> appleMap = appleList.stream().collect(
    Collectors.toMap(
        Apple::getId, 
        a -> a,
        (k1,k2)->k1
        )
    );
```

## list集合如何去重？Arraylist如何去重？

```java

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

// 根据id去重
List<Person> unique = appleList.stream().collect(
    collectingAndThen(
        toCollection(
            () -> new TreeSet<>(comparingLong(Apple::getId))
        ), 
        ArrayList::new
    )
);
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


## list中有大量删除，用哪个方法？

**不能使用foreach来实现**

主要有以下3种方法：

1. 使用Iterator的remove()方法
2. 使用for循环正序遍历
3. 使用for循环倒序遍历

(1) 使用Iterator的remove()方法的实现方式如下所示：

```java
public static void main(String[] args) {
    List<String> platformList = new ArrayList<>();
    platformList.add("博客园");
    platformList.add("CSDN");
    platformList.add("掘金");

    Iterator<String> iterator = platformList.iterator();
    while (iterator.hasNext()) {
        String platform = iterator.next();
        if (platform.equals("博客园")) {
            iterator.remove();
        }
    }

    System.out.println(platformList);
}

```

(2) 使用removeIf()方法(推荐)

从JDK1.8开始，可以使用removeIf()方法来代替 Iterator的remove()方法实现一边遍历一边删除，其实，IDEA中也会提示：

```java
platformList.removeIf(platform -> "博客园".equals(platform));

看下removeIf()方法的源码，会发现其实底层也是用的Iterator的remove()方法：
```

(3) Stream的方式

Stream的方法很容易理解，就是加一个过滤器即可

```java
public void stream() {
  List<String> list = Lists.newArrayList("Cup", null, "Apple", null, "Desk");
  List<String> expected = Lists.newArrayList("Cup", "Apple", "Desk");
  List<String> result = list.parallelStream()
    .filter(Objects::nonNull)
    .collect(Collectors.toList());
  assertEquals(expected, result);
}
```

## ArrayList如何排序？

1. 使用集合的工具类 Collections 对 ArrayList 集合进行排序

简单的整数型排序：

当集合的范型为Integer类型或者为String类型并且集合中的元素为数字字符串，我们可以使用集合的工具类Collections类来对集合中的元素进行排序。

```java
List<Integer> numbers = new ArrayList<>();
Collections.addAll(numbers,1,3,2,6,4,8,7,9);
Collections.sort(numbers);
System.out.println("numbers："+numbers.toString());
//运行结果 --> numbers：[1, 2, 3, 4, 6, 7, 8, 9]

List<String> strNumbers = new ArrayList<>();
Collections.addAll(strNumbers,"1","3","2","6","4","8","7","9");
Collections.sort(strNumbers);
System.out.println("strNumbers："+strNumbers.toString());
//运行结果 --> strNumbers：[1, 2, 3, 4, 6, 7, 8, 9]
```

2. 使用java8新特性中的stream，将ArrayList集合中的元素流化实现排序

将元素集合看作一种流， 流在管道中传输， 并且可以在管道的节点上进行处理， 比如筛选， 排序，聚合等。元素流在管道中经过中间操作的处理，最后由最终操作得到前面处理的结果。

```java
//排序整数类型集合中的元素
List<Integer> numbers = new ArrayList<>();
Collections.addAll(numbers, 1, 3, 2, 6, 4, 8, 7, 9);
numbers = numbers.stream().sorted(Integer::compareTo).collect(Collectors.toList());
System.out.println("numbers：" + numbers);
//运行结果 --> numbers：[1, 2, 3, 4, 6, 7, 8, 9]

//排序其他范型集合中的数据
//比如现在有一个User类型的List集合，要求根据User对象中的age属性对List集合中的User对象进行排序
List<User> userList = new ArrayList<>();
Collections.addAll(userList,
                   new User(1,"Jack",25,"男"),
                   new User(2,"Jason",24,"男"),
                   new User(3,"Jimmy",20,"男"),
                   new User(4,"Lucy",19,"男"),
                   new User(5,"Tom",21,"男")
                  );
userList =userList
  .stream()
  .sorted(Comparator.comparing(User::getAge).reversed())
  //.sorted(Comparator.comparing(User::getAge).reversed()) 加上reversed()方法就是逆序排序
  .collect(Collectors.toList());
System.out.println("userList："+userList.toString());
//运行结果 --> userList：[User{id=4, name='Lucy', age=19, sex='男'}, User{id=3, name='Jimmy', age=20, sex='男'}, User{id=5, name='Tom', age=21, sex='男'}, User{id=2, name='Jason', age=24, sex='男'}, User{id=1, name='Jack', age=25, sex='男'}]
```

3. 使用比较器对ArrayList集合进行排序

如果方式一和方式二都不能解决的话可以通过比较器来解决。

比如现在有一个ArrayList集合，它的范型是一个Map，要求根据Map元素中的某个Key的Value值对集合中的Map元素进行排序

```java
Collections.sort(mapList, new Comparator<Map<String, Object>>() {
  @Override
  public int compare(Map<String, Object> o1, Map<String, Object> o2) {
    Integer faceValueOne = Integer.valueOf(o1.get("faceValue").toString());
    Integer faceValueTwo = Integer.valueOf(o2.get("faceValue").toString());
    return faceValueOne.compareTo(faceValueTwo);
  }
});
```

## Arraylist循环的几种方式？

for循环

迭代器循环

for each循环

stream

## 对 list、set 如何取对象

```java
public class UserEntity implements Serializable {
    
    private Integer id;
 
    /**
     * 用户名
     */
    private String userName;
 
    /**
     * 用户手机号
     */
    private String phone;
}
 
 

public static void main(string args[]){
    
    List<UserEntity> users=new ArrayList<>();
 
    users.add(new UserEntity(1,"张三","18399990000"));
    users.add(new UserEntity(2,"王五","18399990023"));
    users.add(new UserEntity(3,"里斯","18399990005"));
 
    List<String> courseIds=  users.stream().map(UserEntity::getUserName).collect(Collectors.toList());
}
```

```java
例如我们有一个Student集合，每个对象有“id”，“name”，“age”三个字段，如下：
@Data
@AllArgsConstructor
public class Student {
    private Integer id;
    private String name;
    private Integer age;
}
```

其中id字段是1000~1099，如下：

```java
Student(id=1000, name=学生_0000, age=18)
Student(id=1001, name=学生_0001, age=22)
Student(id=1002, name=学生_0002, age=27)
```

需求：我们需要根据id=xxxx来取出Student对象。

方案一

根据id字段，把List转换为Map。

```java
//注意：该方法要求id字段的值是唯一的 否则需要使用下面的方法转Map
Map<Integer, Student> studentMap = studentList.stream().collect(
    Collectors.toMap(
        Student::getId, Function.identity()
        )
    );

//根据id字段分组，且分组之后的对象集合取第一个对象
Map<Integer, Student> studentMap = studentList.stream().collect(
    Collectors.groupingBy(
        Student::getId, 
        Collectors.collectingAndThen(
            Collectors.toList(), 
            students -> students.get(0)
            )
        )
    );
```

需要取哪个对象，直接从map中取出即可。

方案二

使用stream的filter进行过滤。

```java
Student student = studentList.stream().filter(s -> Objects.equals(s.getId(), id)).findFirst().orElse(null);
```

在filter中过滤出符合条件的对象，不匹配返回null。
实际使用中，数据量都不会特别大，个人认为方案二更清晰一些。
如果数据量较大，且需要从集合中多次取出，方案一的效率更高。因为方案一只聚合一次，而方案二需要反复创建stream并过滤集合。
性能测试
创建100个对象的测试集合，循环取值1000000次。

```java
public class Test {

    public static void main(String[] args) {
        //创建集合
        List<Student> studentList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            studentList.add(new Student(1000 + i, "学生_" + String.format("%04d", i), randomInt(18, 30)));
        }
        //分组测试
        long mapTs = System.currentTimeMillis();
        Map<Integer, Student> studentMap = studentList.stream().collect(Collectors.toMap(Student::getId, Function.identity()));
        for (int i = 0; i < 1000000; i++) {
            int id = randomInt(1000, 1100);
            Student student = studentMap.get(id);
        }
        System.out.println("分组测试: " + (System.currentTimeMillis() - mapTs) + "ms");
        //过滤测试
        long filterTs = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            int id = randomInt(1000, 1100);
            Student student = studentList.stream().filter(s -> Objects.equals(s.getId(), id)).findFirst().orElse(null);
        }
        System.out.println("过滤测试: " + (System.currentTimeMillis() - filterTs) + "ms");
    }
}
```

## list如何排序？

1. list 自身 の 【sort方法】
2. 使用 Collections.sort(list)

https://www.bilibili.com/video/BV1tg411X7d8

https://www.bilibili.com/video/BV1uY4y137us

[Java TreeSet-demo演示](https://www.bilibili.com/video/BV1bV4y1x7aY)

[Java TreeMap-demo演示](https://www.bilibili.com/video/BV1Jd4y1N7U9)

[Java延迟队列DelayQueue-demo演示](https://www.bilibili.com/video/BV1YV4y1j7jA)

[Java优先级队列PriorityQueue-模拟插队](https://www.bilibili.com/video/BV1AG411a7EV)

[Java优先级队列PriorityQueue-demo演示](https://www.bilibili.com/video/BV1og41117C1)

- PriorityQueue：可以插队
- DelayQueue：可以设置【延迟时间】，解耦【生产者】和【消费者】
  1. 生产者，就是往队列里面写数据
  2. 消费者，就会从队列里面读数据
  3. 应用场景：延迟15分钟后，自动关闭订单
- TreeMap：按照【key】排序，底层是红黑树
- TreeSet：基于TreeMap实现，

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

## Vector

Vector  の 底层结构是【数组】，但效率低，线程安全

扩容时，是原来 の 2倍


## 一些常见 の JDK常见命令：

- jps
- jinfo
- jstat
- jhat
- jstack
- jmap




## JVM性能优化 - 如何排查问题？

1. 打印出 `GC log`，查看 minor GC 和 major GC
2. `jstack` 查看【堆栈信息】
3. 应用 `jps，jinfo，jstat，jmap`等命令

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


## 【大厂面试题】你进行过JVM调优吗？（亲身经历分享）

什么情况需要调优？

1. 内存使用率增大，再释放
2. Young GC -- 55次/分钟 ~ 220次/分钟(峰值)
3. Full GC -- 0.5次/分钟 ~ 8次/分钟(峰值)

https://www.bilibili.com/video/BV13Y411j7Nz

JVM调优目的：

- Young GC 次数减少
- Young GC 耗时减少
- Full GC 不超过 6天1次
- Full GC 耗时减少

1. 指定【垃圾收集器】为ParNew + CMS/G1
   - 应当尽量采用【低延时的收集器】
   - 低版本JDK:CMS / 高版本JDK:G1
2. 扩充【年轻代】的占比为之前的1.5倍
   - Xms:指定【应用程序】可用的【最小堆大小】
   - Xmx:指定【应用程序】可用的【最大堆大小】
   - 都设置为4096M，也就是4G，避免动态调整
   - 当【并发大】的时候，【年轻代】的对象会激增，有些本该在Young GC就回收的对象，没有GC成功，直接进入【老年代】
   - 由于对象的晋升，老年代的FullGC频繁
3. 指定【元数据区】的大小
   - 默认初始值为：21M
   - 元数据区的GC也会触发FullGC，导致stw
   - 根据元数据【常驻对象的大小】指定元数据区的大小
   - MataspaceSize 和 MaxMataspaceSzie 都指定为256M，可以防止【动态调整】
4. 使用【并发预清理】
   - 配置`CMSScavengeBeforeRemark`
   - 【老年代】和【年轻代】之间存在【跨年龄引用】
   - 在CMS进行GC之前，进行一次【重新标记】，可以减少【对象扫描】，从而减少FullGC时间。


## 一些JVM参数？

[Jvm调优最佳参数](https://www.bilibili.com/video/BV1kt4y1h7ck)

1. InitialHeapSize
2. NewRatio
3. UseG1GC
4. MaxHeapSize
5. ConcGCThreads

从而优雅地分析JVM出现 の 常见问题，并对其进行优雅地调优

## 什么情况【JVM退出】？

1. 所有【非守护进程】都执行完毕，JVM会调用 【Shutdown Hook】 退出
2. 某个【线程】调用了【Runtime类】或者【System类】 の 【exit方法】

[幼麟实验室 - runtime提供 の 等待队列](https://www.bilibili.com/video/BV1ZQ4y1f7go)

## java有哪些特点？JVM の 好处：

Java是【面向对象】的编程语言

1. 可以【屏蔽】操作系统 の 细节，使得Java可以【一次编写，到处运行】
2. 允许在【编译时检查】潜在【类型不匹配】，保证了程序 の 可靠性
3. 可以实现自动垃圾回收

https://www.bilibili.com/video/BV1vT4y1S7Qm

## Boss直聘面试官：内存溢出如何排查？

https://www.bilibili.com/video/BV1rL4y1u7jN

eclipse memory analyzer (简称 MAT)

排查过程分为3步：

1. 占用内存过大的对象有哪些？
2. 这个对象被谁引用？
3. 定位到具体代码？

假如代码是自己写的，那么一会就改完了
如果是一些【中间件】的代码，就要求对【中间件的实现】有个基本的了解

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


## 【大厂面试题】数据库连接池泄漏如何排查？

https://www.bilibili.com/video/BV1zY4y1k7Jp

通过druid监控排查

1. 【连接池】的开启、关闭是normal的，那么【逻辑连接打开次数】和【逻辑连接关闭次数】应该是相等的
2. 如果没有进行操作，那么【活跃连接数】应该为 0 
3. 如果代码有问题，那么【连接】就不会释放，导致等几分钟后，【逻辑连接打开次数】和【逻辑连接关闭次数】不相等的
   - 配置 druid 的 `abandon策略`。通过`abandon`可以强制回收`数据库的连接`，然后就能打印出【堆栈信息】，就能知道哪里的【sql代码】出现问题了
   - 重启项目，【堆栈信息】中【代码详细位置】会标记出来。
   - 开发人员去【相应的class】中找到【相应的代码】查看即可


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

## 【大厂面试题】？




## spark那些外部资源 还有第三方jar包之类 の 都放在哪（应该是这么问 の ，不太会，说了下内存结构，告诉我是java classloader相关 の 机制）

## 遇到oom怎么排查（有jvm提供 の 工具查看堆栈使用情况，具体不太了解只在跑spark遇到过、分享了下spark里 の 排查经验和参数调整）

1. 查看服务器 の 运行日志，捕捉OOM
2. 使用jstat查看监控JVM の 内存和GC情况
3. 使用【MAT工具】载入【dump文件】，分析大对象 の 占用情况。 [线上服务内存溢出如何排查定位](https://www.bilibili.com/video/BV1y34y1j7QR)
   - 【jmap命令】用于生成【dump文件】
   - JVM参数`HeapDumpOnOutOfMemoryError`，可以让JVM在OOM异常出现之后，自动生成【dump文件】

## linux下 の 调查问题思路：内存、CPU、句柄数、过滤、查找、模拟POST和GET请求等等场景

## 拼多多面试官：线上CPU飙高如何排查？

[ 线上问题排查套路汇总-CPU篇](https://www.bilibili.com/video/BV1HL4y167Ex)

https://www.bilibili.com/video/BV15T4y1y7eH

1. 第一步，top 先看看是哪个【进程 】找到，cpu占用最高的，如果是 java 
2. 然后用

```s
jstack 进程id > show.txt
top -p 进程id -H
得到【当前进程】下，所有运行的线程，然后找到占用最高的线程
```

3. 然后把线程id转成16进制字符串

```s
printf "%x" 23265
 -> 5ae1
```

4. 到show.txt文件中，根据【线程ID】查看【线程的具体状态】即可

```s
less show.txt
```

## 阿里面试官：Jar包冲突如何解决？

https://www.bilibili.com/video/BV1GL411w7gw

在IDEA下载一个插件，`Maven Helper`，用来分析依赖冲突，把存在冲突的jar包移除

**为什么会存在jar包冲突？**

1. 最短路径原则：
   - 如果到达jar包的距离更短，则选择更短的那个
2. 优先声明原则：
   - 如果在pom.xml中，哪个写在前面，就用哪个版本


## 网络问题排查-套路汇总

TCP的三次握手

https://www.bilibili.com/video/BV1wu411o75x




