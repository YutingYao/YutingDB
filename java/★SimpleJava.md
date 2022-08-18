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

异步：【主线程】不需要同步等待【另一个线程】 の 完成，也可以继续执行其他任务。在发出一个调用时，调用立即返回，不会立即得到结果，被调用者会通过通知或者回调函数来处理结果。

同步与异步的区别主要在于：调用结果是否跟随调用结束后直接返回

【同步】和【异步】一般都是针对【请求的client】和【请求的链接】：

- 比如，【client】有一个【请求】【服务器】返回一个【结果】
- 但是【服务器端】计算这个结果，过程很长
-【同步】：连接就一直【等待】
- 【异步】：我在你那挂了一个钩子，你数据准备好了通过钩子给我。发出结果就立即返回。虽然【client】没有真正拿到返回结果，大表有一个【消息回写的接口】，在结果计算完以后，再把数据给你。

【阻塞】和【非阻塞】往往针对的是【服务端】的【请求线程 + 处理线程】来说的：这些概念一般出现在【网络io模型】中，所谓的【同步阻塞】是指【等待】数据的到达，【同步非阻塞】则需要【轮询】查看数据是否到达

- 比如，【client】查询一个【大数据】。
- 【阻塞】：【请求线程】不能做其他事情，要一致处于【等待状态】
- 【非阻塞】：【请求线程】发出请求给【处理线程】以后，不用一直等待，【请求线程】可以快速释放回到【资源池】去处理其他请求。但这个【请求线程】每个10~20秒，去【轮询】一下【处理线程】，完成以后，就拿着结果到【客户端】

综上所述：

- 【同步】和【异步】是针对【客户端】的【请求连接】来说的
- 【阻塞】和【非阻塞】往往针对的是【服务端】的【请求线程 + 处理线程】来说的
- 同步和异步是一个逻辑概念。表示一次交互要的结果请求方和处理方是否时钟同源。异步的行为是增加吞吐量。并不能节省结果的处理时间。
- 阻塞和非阻塞是在线程层面的概念。阻塞和非阻塞是在线程层面说的。阻塞与非阻塞的区别：等待这个结果的过程中，程序是去做其他事情，还是傻傻地等

两组概念一组合，就出现4种场景，为了解释这4种场景，我们举个栗子：

【顾客|client】到【餐馆吃饭】，【餐馆】有【服务员】和【后端厨师】

对比：【异步】和【多线程】并不是【同等关系】，实现异步 の 方式有很多，【多线程】只是实现异步 の 一种方式

## 多线程的异步调用是怎么实现的?

https://www.bilibili.com/video/BV1Tq4y1j7kV

当classA中的client发出一个client请求，classB受到请求，并【构建数据】，【构建数据】是一个耗时的操作，因此需要放到Thread中。虽然【数据】【还没有】【构建】完成，就可以调用Future.set()方法，异步地处理数据，只要将Future返回给classA即可。当我们真正需要【数据】时，只要调用Future.get()方法，即可获取数据。

## Future中是如何异步的get和set数据的？

用flag区分是否获得数据。

当调用get方法时，如果数据没有被set进来，则需要wait一下，等到数据真正被set进来，flag就会变成true。并且通过notify来调用数据，最终把数据return回去

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

5. `new Work Stealing Pool`：Java 8 才加入这个创建方法，

特点：

- 其内部会构建 ForkJoinPool，
- 利用【Work-Stealing算法】，【并行】地处理任务，不保证顺序；

线程池 の 核心是：

ThreadPoolExecutor()：上面创建方式都是对 ThreadPoolExecutor の 封装。

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

## ArrayBlockQueue

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

当程序运行起来后：

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
6. 如果持有锁 の 【线程2】执行完程序后也会释放`monitor锁对象`，以便其他【线程】获取`monitor`。

## 线程 の 概念体系？

线程池：多线程处理多个任务（CPU时间片、线程）【阻塞】队列（生产者、消费者）

线程安全：多线程处理同一个【对象】，`synchronized修饰` の 代码 是一个个【task】

## 什么情况【线程】会释放 `monitor锁对象`？

1. 【运行 の 线程】调用*wait方法*，将释放 `monitor锁对象`
2. 【运行 の 线程】执行完程序后，将释放 `monitor锁对象`

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
   - **外部程序，通过这个【钩子】来触发对【线程】中断 の 命令。**
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
        System.out.println("线程中断了，程序到这里了")
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
2. 在程序设计中，尽量去减少【共享对象】 の 使用。从业务上去实现【隔离】避免【并发】。 -->

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

其他程序获取不到【事务连接】

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


- 实现了Future和CompletionStage两个接口
- CompletionStage：定义了【任务编排】的方法
- CompletableFuture最大的改进：提供了类似于【观察者模式】的【回调监听】功能，也就是当【上一阶段任务】执行结束之后，可以【回调】【下一阶段任务】，而不需要【阻塞，获取结果】之后，再去处理结果
-
- 可以基于CompletableFuture创建任务和链式处理多个任务，并实现按照`任务完成`的`先后顺序`获取任务的结果
- 不论`Future.get()方法`还是`CompletableFuture.get()方法`都是【阻塞】的

弥补了原本Future の 不足，使得程序可以在【非【阻塞】状态】下，完成【**异步**】【回调机制】

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
7. 最好判断hash槽的使用率，是否超过3/4，是的话，就【扩容】
8. 结束

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

## 为什么使用【红黑树】？

【红黑树】是【二叉树】 の 一种，它 の 【查找算法】就相当于是【二分查找】。

【红黑树】 の 【时间复杂度 O(logN)】在数据比较多 の 时候，

会比【链表】 の 【时间复杂度 O(N)】要快


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

## ConcurrentHashMap是如何保证线程安全？

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

## Hashmap 和 Treemap の 区别


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

他 の 引用链时这样 の ：

| stack  | Heap  |
|---|---|
| ThreadLocalRef  | TreadLocal  |

 → 引用 → Thread → 引用 →  → 引用 → Entry → 引用 → value


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
2. 把【ThreadLocal变量】尽可能地定义为static final
   - 这样，可以避免【频繁创建】【ThreadLocal实例】，这样能够保证程序一定存在【ThreadLocal强引用】也能保证，任何时候，都能通过【ThreadLocal弱引用】去访问【Entry的值】


## 【大厂面试题】【非常重要】ThreadLocal の 常用场景？

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

