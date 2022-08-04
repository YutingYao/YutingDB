[Java线程面试题合集](https://mp.weixin.qq.com/s/qP76G1CE1dTMGg-3tPPVIg)


## 什么是`线程`？

操作系统 进行运算 の 最小单位，

包含在`进程`之中

程序员可以通过它，对`运算密集型job`提速。

比如，如果一个`线程`完成一个`任务`要100毫秒，那么用十个`线程`完成该`任务`只需10毫秒。

## `线程`和`进程`有什么区别？

`线程`是`进程` の 子集

一个`进程`可以有很多`线程`

每条`线程``并行`执行不同 の `任务`。

- 不同 の `进程`使用不同 の 内存空间

- 所有 の `线程`共享一片相同 の 内存空间。

## 如何在Java中实现`线程`？

有两种创建`线程` の 方法：

- 一是调用`Runnable`接口，
  - 然后将它传递给 `Thread の 构造函数`，创建一个`Thread对象`；

- 二是直接 继承`Thread类`。

## 用`Runnable`还是Thread？

∵ Java不支持`类 の 多重继承`
but 允许你`调用多个接口`。
∴ 当然是调用`Runnable`接口好了

## Thread 类中 の start() 和 run() 方法有什么区别？

`start()方法`：

- 启动**new**创建 の `线程`
- `start()方法`才会启动`线程`。
- 无需等待 `run 方法体`代码执行完毕，可以直接继续执行下面的代码。

`run()方法`：

- 称为线程体，它包含了要执行的这个线程的内容，线程就进入了运行状态
- 运行 run 函数当中的代码
- Run 方法运行结束， 此线程终止
- 然后 CPU 再调度其它线程。

## Java中`Runnable`和`Callable`有什么不同？

共同点：

`Runnable`和`Callable`都代表那些要在 `不同 の线程`中执行 の `任务`。

`Runnable` JDK1.0 

`Callable` JDK1.5 

主要区别：

`Callable` の `call()方法`可以`返回值`和`抛出异常`，

`Runnable` の `run()方法`没有这些功能。


## Java 中 CyclicBarrier 和 CountDownLatch 有什么不同？

作用：

让一组`线程` wait 其它`线程`。

区别：

CountdownLatch 不能`重新使用`。




## Java中如何停止一个`线程`？

Java没有为停止`线程`提供API。

当`run()` 或者 `call() 方法`执行完 の 时候`线程`会自动结束，

如果要手动结束一个`线程`，可以 → 取消`任务`来中断`线程`。

## Java中 heap 和 stack 有什么不同？

stack 是一块和`线程`紧密相关 の 内存区域。

每个`线程`都有自己 の  stack 内存，用于存储本地变量，

方法参数和 stack 调用，一个`线程`中存储 の 变量对其它`线程`是不可见 の 。

而 heap 是所有`线程`共享 の 一片`公用内存区域`。对象都在 heap 里创建，为了提升效率`线程`会从 heap 中弄一个缓存到自己 の  stack ，

如果多个`线程`使用该变量就可能引发问题，这时 volatile 变量就可以发挥作用了，它要求`线程`从主存中读取变量 の 值。


## volatile 变量和 atomic 变量有什么不同？

功能不一样。

|  Volatile变量 | atomic 变量  |
|---|---|
| 确保先行关系  |   |
| 保证`下一个读取操作`会在`前一个写操作`之后发生  |   |
|  不能保证 原子性 |  让这种操作具有 原子性 |
|   |  getAndIncrement()方法, 会原子性 の 进行增量操作 |


## 如何确保`线程`安全？

使用`原子类(atomic concurrent classes)`

实现`并发锁`

使用`volatile关键字`

使用`不变类`和`线程`安全类。

## Java中 synchronized 和 ReentrantLock 有什么不同？

同：

都是用来协调`多线程`对`共享对象、变量`的访问

都是可`重入锁`，`同一线程`可以`多次获得同一个锁`

都保证了`可见性`和`互斥性`

异：

|  ReentrantLock | synchronized  |
|---|---|
| 【显式】的获得、释放锁  | 【隐式】获得释放锁 |
|  可`响应中断` | 不可以`响应中断`  |
|  API 级别 | JVM 级别  |
|  同步非阻塞，采用的是【乐观并发策略】 | 同步阻塞，使用的是【悲观并发策略】  |
|  接口 | Java 中的关键字  |
|  在发生异常时，如果没有主动通过 unLock()去释放锁，则很可能造成死锁现象 | 发生异常时，会自动释放线程占有的锁，因此不会导致死锁现象发生  |
|  可以让等待锁的线程响应中断 | 等待的线程会一直等待下去，不能够响应中断 |
|  可以知道有没有成功获取锁 | 不可以  |
|  ReentrantLock | synchronized  |



synchronized作用：

实现互斥

but:

它有一些缺点:

- 你不能扩展`锁之外 の 方法`或者`块边界`，

- 尝试`获取锁`时不能`中途取消`等。

Lock接口解决这些问题:

- `ReentrantLock 类`实现了 Lock，

- 它拥有与 `synchronized 相同 の 并发性`和`内存语义`且它还具有`可扩展性`。

## `sleep`方法和wait方法有什么区别

https://www.bilibili.com/video/BV1rq4y1X7VC

都会造成某种形式 の 暂停

| `sleep`方法  |  wait方法 |
|---|---|
| 不会释放锁 | 会释放锁  |
| `sleep`必须要 设定`时间`  |  可以设定, 也可以不设定 |
| Thread类  |  Object类 |


## 多`线程`中 の `忙循环`是什么?

定义：

程序员用循环让一个`线程` wait ，不像传统方法wait(), 

`sleep`() 或 yield() 它们都放弃了CPU控制，

而`忙循环`不会放弃CPU，它就是在运行一个空循环。

目的：

保留CPU缓存，

在多核系统中，一个 wait `线程` 醒来 の 时候可能会在`另一个内核`运行，这样会`重建缓存`。

为了避免`重建缓存`和减少 `wait 重建 の 时间`就可以使用它了。




## 为什么 Thread类 の `sleep()` 和 `yield()方法`是 `静态` の ？

它们可以

在`当前正在执行` の `线程`中工作，

在其他处于 wait 状态 の `线程`上调用这些方法 → 没有意义

所以，这些方法是`静态` の

避免程序员错误 の 认为可以在其他非运行`线程`调用这些方法。

## 如果`提交任务`时，`线程池队列`已满，会发生什么


如果 `无界队列` 的话，继续添加`任务`到`阻塞队列`中`等待执行`

如果 `有界队列` 的话，则会使用`拒绝策略`


## 什么是`ThreadLocal变量`？


线程局部变量是局限于`线程内部的变量`，属于`线程自身所有`，不在多个线程间共享。

它是一种特殊的`线程绑定机制`，将`变量`与`线程` 绑定 在一起，

为`每一个线程`维护一个`独立的变量副本`。

Java 提供 `ThreadLocal 类`来支持`线程局部变量`，是一种实现线程安全的方式。

`ThreadLocal 对象`建议使用 `static修饰`。这个`变量`是针对一个`线程内所有操作`共有的，所以设置为`静态变量`，所有此类实例共享此`静态变量` ，

也就是说在`类第一次被使用时装载`，只分配一块`存储空间`，所有`此类的对象` ( 只要是这个线程内定义的 ) 都可以操控这个变量。

## 线程池中submit() 和 execute()方法有什么区别

都可以向线程池提交任务，

|  execute()方法 |  submit()方法 |
|---|---|
| 返回类型是void  |  返回持有计算结果的Future对象 |
| 定义在Executor接  |   |


## 线程的调度策略

线程调度器选择`优先级最高的线程`运行，但是，如果发生以下情况，就会终止线程的运行：

（1）线程体中调用了 `yield 方法`让出了`对 cpu 的占用权利`

（2）线程体中调用了 `sleep 方法`使`线程`进入`睡眠状态`

（3）线程由于 `IO 操作`受到阻塞

（4）另外一个`更高优先级线程`出现

## 如何停止一个正在运行的线程？

使用`退出标志`（比如return），使线程正常退出，也就是当`run方法`完成后`线程终止`。

使用`stop方法`强行终止，但是不推荐这个方法，因为stop和suspend及resume一样都是`过期作废的方法`。

使用`interrupt方法`中断线程。

## 在多`线程`中，什么是上下文切换(context-switching)？

`CPU控制权`由一个`已经正在运行的线程`切换到另外一个`就绪并等待获取CPU执行权的线程`。
























1、多线程有什么用？

一个可能在很多人看来很扯淡的一个问题：我会用多线程就好了，还管它有什么用？在我看来，这个回答更扯淡。所谓"知其然知其所以然"，"会用"只是"知其然"，"为什么用"才是"知其所以然"，只有达到"知其然知其所以然"的程度才可以说是把一个知识点运用自如。OK，下面说说我对这个问题的看法：

1）发挥多核CPU的优势

随着工业的进步，现在的笔记本、台式机乃至商用的应用服务器至少也都是双核的，4核、8核甚至16核的也都不少见，如果是单线程的程序，那么在双核CPU上就浪费了50%，在4核CPU上就浪费了75%。单核CPU上所谓的"多线程"那是假的多线程，同一时间处理器只会处理一段逻辑，只不过线程之间切换得比较快，看着像多个线程"同时"运行罢了。多核CPU上的多线程才是真正的多线程，它能让你的多段逻辑同时工作，多线程，可以真正发挥出多核CPU的优势来，达到充分利用CPU的目的。

2）防止阻塞

从程序运行效率的角度来看，单核CPU不但不会发挥出多线程的优势，反而会因为在单核CPU上运行多线程导致线程上下文的切换，而降低程序整体的效率。但是单核CPU我们还是要应用多线程，就是为了防止阻塞。试想，如果单核CPU使用单线程，那么只要这个线程阻塞了，比方说远程读取某个数据吧，对端迟迟未返回又没有设置超时时间，那么你的整个程序在数据返回回来之前就停止运行了。多线程可以防止这个问题，多条线程同时运行，哪怕一条线程的代码执行读取数据阻塞，也不会影响其它任务的执行。

3）便于建模

这是另外一个没有这么明显的优点了。假设有一个大的任务A，单线程编程，那么就要考虑很多，建立整个程序模型比较麻烦。但是如果把这个大的任务A分解成几个小任务，任务B、任务C、任务D，分别建立程序模型，并通过多线程分别运行这几个任务，那就简单很多了。



2、创建线程的方式

比较常见的一个问题了，一般就是两种：

1）继承Thread类

2）实现Runnable接口

至于哪个好，不用说肯定是后者好，因为实现接口的方式比继承类的方式更灵活，也能减少程序之间的耦合度，面向接口编程也是设计模式6大原则的核心。

其实还有第3种，点击这里了解更多。

 

3、start()方法和run()方法的区别

只有调用了start()方法，才会表现出多线程的特性，不同线程的run()方法里面的代码交替执行。如果只是调用run()方法，那么代码还是同步执行的，必须等待一个线程的run()方法里面的代码全部执行完毕之后，另外一个线程才可以执行其run()方法里面的代码。

 

4、Runnable接口和Callable接口的区别

有点深的问题了，也看出一个Java程序员学习知识的广度。

Runnable接口中的run()方法的返回值是void，它做的事情只是纯粹地去执行run()方法中的代码而已；Callable接口中的call()方法是有返回值的，是一个泛型，和Future、FutureTask配合可以用来获取异步执行的结果。

这其实是很有用的一个特性，因为多线程相比单线程更难、更复杂的一个重要原因就是因为多线程充满着未知性，某条线程是否执行了？某条线程执行了多久？某条线程执行的时候我们期望的数据是否已经赋值完毕？无法得知，我们能做的只是等待这条多线程的任务执行完毕而已。而Callable+Future/FutureTask却可以获取多线程运行的结果，可以在等待时间太长没获取到需要的数据的情况下取消该线程的任务，真的是非常有用。

 

5、CyclicBarrier和CountDownLatch的区别

两个看上去有点像的类，都在java.util.concurrent下，都可以用来表示代码运行到某个点上，二者的区别在于：

1）CyclicBarrier的某个线程运行到某个点上之后，该线程即停止运行，直到所有的线程都到达了这个点，所有线程才重新运行；CountDownLatch则不是，某线程运行到某个点上之后，只是给某个数值-1而已，该线程继续运行。

2）CyclicBarrier只能唤起一个任务，CountDownLatch可以唤起多个任务。

3) CyclicBarrier可重用，CountDownLatch不可重用，计数值为0该CountDownLatch就不可再用了。




 

7、什么是线程安全

又是一个理论的问题，各式各样的答案有很多，我给出一个个人认为解释地最好的：如果你的代码在多线程下执行和在单线程下执行永远都能获得一样的结果，那么你的代码就是线程安全的。

这个问题有值得一提的地方，就是线程安全也是有几个级别的：

1）不可变

像String、Integer、Long这些，都是final类型的类，任何一个线程都改变不了它们的值，要改变除非新创建一个，因此这些不可变对象不需要任何同步手段就可以直接在多线程环境下使用

2）绝对线程安全

不管运行时环境如何，调用者都不需要额外的同步措施。要做到这一点通常需要付出许多额外的代价，Java中标注自己是线程安全的类，实际上绝大多数都不是线程安全的，不过绝对线程安全的类，Java中也有，比方说CopyOnWriteArrayList、CopyOnWriteArraySet

3）相对线程安全

相对线程安全也就是我们通常意义上所说的线程安全，像Vector这种，add、remove方法都是原子操作，不会被打断，但也仅限于此，如果有个线程在遍历某个Vector、有个线程同时在add这个Vector，99%的情况下都会出现ConcurrentModificationException，也就是fail-fast机制。

4）线程非安全

这个就没什么好说的了，ArrayList、LinkedList、HashMap等都是线程非安全的类，点击这里了解为什么不安全。

 

8、Java中如何获取到线程dump文件

死循环、死锁、阻塞、页面打开慢等问题，打线程dump是最好的解决问题的途径。所谓线程dump也就是线程堆栈，获取到线程堆栈有两步：

1）获取到线程的pid，可以通过使用jps命令，在Linux环境下还可以使用ps -ef | grep java

2）打印线程堆栈，可以通过使用jstack pid命令，在Linux环境下还可以使用kill -3 pid

另外提一点，Thread类提供了一个getStackTrace()方法也可以用于获取线程堆栈。这是一个实例方法，因此此方法是和具体线程实例绑定的，每次获取获取到的是具体某个线程当前运行的堆栈。

 

9、一个线程如果出现了运行时异常会怎么样

如果这个异常没有被捕获的话，这个线程就停止执行了。另外重要的一点是：如果这个线程持有某个某个对象的监视器，那么这个对象监视器会被立即释放

 

10、如何在两个线程之间共享数据

通过在线程之间共享对象就可以了，然后通过wait/notify/notifyAll、await/signal/signalAll进行唤起和等待，比方说阻塞队列BlockingQueue就是为线程之间共享数据而设计的

 

11、sleep方法和wait方法有什么区别 

这个问题常问，sleep方法和wait方法都可以用来放弃CPU一定的时间，不同点在于如果线程持有某个对象的监视器，sleep方法不会放弃这个对象的监视器，wait方法会放弃这个对象的监视器

 

12、生产者消费者模型的作用是什么

这个问题很理论，但是很重要：

1）通过平衡生产者的生产能力和消费者的消费能力来提升整个系统的运行效率，这是生产者消费者模型最重要的作用

2）解耦，这是生产者消费者模型附带的作用，解耦意味着生产者和消费者之间的联系少，联系越少越可以独自发展而不需要收到相互的制约

 

13、ThreadLocal有什么用

简单说ThreadLocal就是一种以空间换时间的做法，在每个Thread里面维护了一个以开地址法实现的ThreadLocal.ThreadLocalMap，把数据进行隔离，数据不共享，自然就没有线程安全方面的问题了

 

14、为什么wait()方法和notify()/notifyAll()方法要在同步块中被调用

这是JDK强制的，wait()方法和notify()/notifyAll()方法在调用前都必须先获得对象的锁

 

15、wait()方法和notify()/notifyAll()方法在放弃对象监视器时有什么区别

wait()方法和notify()/notifyAll()方法在放弃对象监视器的时候的区别在于：wait()方法立即释放对象监视器，notify()/notifyAll()方法则会等待线程剩余代码执行完毕才会放弃对象监视器。

 

16、为什么要使用线程池

避免频繁地创建和销毁线程，达到线程对象的重用。另外，使用线程池还可以根据项目灵活地控制并发的数目。点击这里学习线程池详解。

 

17、怎么检测一个线程是否持有对象监视器

我也是在网上看到一道多线程面试题才知道有方法可以判断某个线程是否持有对象监视器：Thread类提供了一个holdsLock(Object obj)方法，当且仅当对象obj的监视器被某条线程持有的时候才会返回true，注意这是一个static方法，这意味着"某条线程"指的是当前线程。

 

18、synchronized和ReentrantLock的区别

synchronized是和if、else、for、while一样的关键字，ReentrantLock是类，这是二者的本质区别。既然ReentrantLock是类，那么它就提供了比synchronized更多更灵活的特性，可以被继承、可以有方法、可以有各种各样的类变量，ReentrantLock比synchronized的扩展性体现在几点上：

（1）ReentrantLock可以对获取锁的等待时间进行设置，这样就避免了死锁

（2）ReentrantLock可以获取各种锁的信息

（3）ReentrantLock可以灵活地实现多路通知

另外，二者的锁机制其实也是不一样的。ReentrantLock底层调用的是Unsafe的park方法加锁，synchronized操作的应该是对象头中mark word，这点我不能确定。

 

19、ConcurrentHashMap的并发度是什么

ConcurrentHashMap的并发度就是segment的大小，默认为16，这意味着最多同时可以有16条线程操作ConcurrentHashMap，这也是ConcurrentHashMap对Hashtable的最大优势，任何情况下，Hashtable能同时有两条线程获取Hashtable中的数据吗？

 

20、ReadWriteLock是什么

首先明确一下，不是说ReentrantLock不好，只是ReentrantLock某些时候有局限。如果使用ReentrantLock，可能本身是为了防止线程A在写数据、线程B在读数据造成的数据不一致，但这样，如果线程C在读数据、线程D也在读数据，读数据是不会改变数据的，没有必要加锁，但是还是加锁了，降低了程序的性能。

因为这个，才诞生了读写锁ReadWriteLock。ReadWriteLock是一个读写锁接口，ReentrantReadWriteLock是ReadWriteLock接口的一个具体实现，实现了读写的分离，读锁是共享的，写锁是独占的，读和读之间不会互斥，读和写、写和读、写和写之间才会互斥，提升了读写的性能。

 

21、FutureTask是什么

这个其实前面有提到过，FutureTask表示一个异步运算的任务。FutureTask里面可以传入一个Callable的具体实现类，可以对这个异步运算的任务的结果进行等待获取、判断是否已经完成、取消任务等操作。当然，由于FutureTask也是Runnable接口的实现类，所以FutureTask也可以放入线程池中。

 

22、Linux环境下如何查找哪个线程使用CPU最长

这是一个比较偏实践的问题，这种问题我觉得挺有意义的。可以这么做：

（1）获取项目的pid，jps或者ps -ef | grep java，这个前面有讲过

（2）top -H -p pid，顺序不能改变

这样就可以打印出当前的项目，每条线程占用CPU时间的百分比。注意这里打出的是LWP，也就是操作系统原生线程的线程号，我笔记本山没有部署Linux环境下的Java工程，因此没有办法截图演示，网友朋友们如果公司是使用Linux环境部署项目的话，可以尝试一下。

使用"top -H -p pid"+"jps pid"可以很容易地找到某条占用CPU高的线程的线程堆栈，从而定位占用CPU高的原因，一般是因为不当的代码操作导致了死循环。

最后提一点，"top -H -p pid"打出来的LWP是十进制的，"jps pid"打出来的本地线程号是十六进制的，转换一下，就能定位到占用CPU高的线程的当前线程堆栈了。

 

23、Java编程写一个会导致死锁的程序

第一次看到这个题目，觉得这是一个非常好的问题。很多人都知道死锁是怎么一回事儿：线程A和线程B相互等待对方持有的锁导致程序无限死循环下去。当然也仅限于此了，问一下怎么写一个死锁的程序就不知道了，这种情况说白了就是不懂什么是死锁，懂一个理论就完事儿了，实践中碰到死锁的问题基本上是看不出来的。

真正理解什么是死锁，这个问题其实不难，几个步骤：

1）两个线程里面分别持有两个Object对象：lock1和lock2。这两个lock作为同步代码块的锁；

2）线程1的run()方法中同步代码块先获取lock1的对象锁，Thread.sleep(xxx)，时间不需要太多，50毫秒差不多了，然后接着获取lock2的对象锁。这么做主要是为了防止线程1启动一下子就连续获得了lock1和lock2两个对象的对象锁

3）线程2的run)(方法中同步代码块先获取lock2的对象锁，接着获取lock1的对象锁，当然这时lock1的对象锁已经被线程1锁持有，线程2肯定是要等待线程1释放lock1的对象锁的

这样，线程1"睡觉"睡完，线程2已经获取了lock2的对象锁了，线程1此时尝试获取lock2的对象锁，便被阻塞，此时一个死锁就形成了。代码就不写了，占的篇幅有点多，Java多线程7：死锁这篇文章里面有，就是上面步骤的代码实现。

点击这里提供了一个死锁的案例。

 

24、怎么唤醒一个阻塞的线程

如果线程是因为调用了wait()、sleep()或者join()方法而导致的阻塞，可以中断线程，并且通过抛出InterruptedException来唤醒它；如果线程遇到了IO阻塞，无能为力，因为IO是操作系统实现的，Java代码并没有办法直接接触到操作系统。

 

25、不可变对象对多线程有什么帮助

前面有提到过的一个问题，不可变对象保证了对象的内存可见性，对不可变对象的读取不需要进行额外的同步手段，提升了代码执行效率。

 

26、什么是多线程的上下文切换

多线程的上下文切换是指CPU控制权由一个已经正在运行的线程切换到另外一个就绪并等待获取CPU执行权的线程的过程。

 

27、如果你提交任务时，线程池队列已满，这时会发生什么

这里区分一下：

1）如果使用的是无界队列LinkedBlockingQueue，也就是无界队列的话，没关系，继续添加任务到阻塞队列中等待执行，因为LinkedBlockingQueue可以近乎认为是一个无穷大的队列，可以无限存放任务

2）如果使用的是有界队列比如ArrayBlockingQueue，任务首先会被添加到ArrayBlockingQueue中，ArrayBlockingQueue满了，会根据maximumPoolSize的值增加线程数量，如果增加了线程数量还是处理不过来，ArrayBlockingQueue继续满，那么则会使用拒绝策略RejectedExecutionHandler处理满了的任务，默认是AbortPolicy

 

28、Java中用到的线程调度算法是什么

抢占式。一个线程用完CPU之后，操作系统会根据线程优先级、线程饥饿情况等数据算出一个总的优先级并分配下一个时间片给某个线程执行。

 

29、Thread.sleep(0)的作用是什么

这个问题和上面那个问题是相关的，我就连在一起了。由于Java采用抢占式的线程调度算法，因此可能会出现某条线程常常获取到CPU控制权的情况，为了让某些优先级比较低的线程也能获取到CPU控制权，可以使用Thread.sleep(0)手动触发一次操作系统分配时间片的操作，这也是平衡CPU控制权的一种操作。

 

30、什么是自旋

很多synchronized里面的代码只是一些很简单的代码，执行时间非常快，此时等待的线程都加锁可能是一种不太值得的操作，因为线程阻塞涉及到用户态和内核态切换的问题。既然synchronized里面的代码执行得非常快，不妨让等待锁的线程不要被阻塞，而是在synchronized的边界做忙循环，这就是自旋。如果做了多次忙循环发现还没有获得锁，再阻塞，这样可能是一种更好的策略。

 

31、什么是Java内存模型

Java内存模型定义了一种多线程访问Java内存的规范。Java内存模型要完整讲不是这里几句话能说清楚的，我简单总结一下Java内存模型的几部分内容：

1）Java内存模型将内存分为了主内存和工作内存。类的状态，也就是类之间共享的变量，是存储在主内存中的，每次Java线程用到这些主内存中的变量的时候，会读一次主内存中的变量，并让这些内存在自己的工作内存中有一份拷贝，运行自己线程代码的时候，用到这些变量，操作的都是自己工作内存中的那一份。在线程代码执行完毕之后，会将最新的值更新到主内存中去

2）定义了几个原子操作，用于操作主内存和工作内存中的变量

3）定义了volatile变量的使用规则

4）happens-before，即先行发生原则，定义了操作A必然先行发生于操作B的一些规则，比如在同一个线程内控制流前面的代码一定先行发生于控制流后面的代码、一个释放锁unlock的动作一定先行发生于后面对于同一个锁进行锁定lock的动作等等，只要符合这些规则，则不需要额外做同步措施，如果某段代码不符合所有的happens-before规则，则这段代码一定是线程非安全的

 

32、

 

33、什么是乐观锁和悲观锁

1）乐观锁：就像它的名字一样，对于并发间操作产生的线程安全问题持乐观状态，乐观锁认为竞争不总是会发生，因此它不需要持有锁，将比较-替换这两个动作作为一个原子操作尝试去修改内存中的变量，如果失败则表示发生冲突，那么就应该有相应的重试逻辑。

2）悲观锁：还是像它的名字一样，对于并发间操作产生的线程安全问题持悲观状态，悲观锁认为竞争总是会发生，因此每次对某资源进行操作时，都会持有一个独占的锁，就像synchronized，不管三七二十一，直接上了锁就操作资源了。

点击这里了解更多乐观锁与悲观锁详情。

 

34、什么是AQS

简单说一下AQS，AQS全称为AbstractQueuedSychronizer，翻译过来应该是抽象队列同步器。

如果说java.util.concurrent的基础是CAS的话，那么AQS就是整个Java并发包的核心了，ReentrantLock、CountDownLatch、Semaphore等等都用到了它。AQS实际上以双向队列的形式连接所有的Entry，比方说ReentrantLock，所有等待的线程都被放在一个Entry中并连成双向队列，前面一个线程使用ReentrantLock好了，则双向队列实际上的第一个Entry开始运行。

AQS定义了对双向队列所有的操作，而只开放了tryLock和tryRelease方法给开发者使用，开发者可以根据自己的实现重写tryLock和tryRelease方法，以实现自己的并发功能。

 

35、单例模式的线程安全性

老生常谈的问题了，首先要说的是单例模式的线程安全意味着：某个类的实例在多线程环境下只会被创建一次出来。单例模式有很多种的写法，我总结一下：

1）饿汉式单例模式的写法：线程安全

2）懒汉式单例模式的写法：非线程安全

3）双检锁单例模式的写法：线程安全

 

36、Semaphore有什么作用

Semaphore就是一个信号量，它的作用是限制某段代码块的并发数。Semaphore有一个构造函数，可以传入一个int型整数n，表示某段代码最多只有n个线程可以访问，如果超出了n，那么请等待，等到某个线程执行完毕这段代码块，下一个线程再进入。由此可以看出如果Semaphore构造函数中传入的int型整数n=1，相当于变成了一个synchronized了。

 

37、Hashtable的size()方法中明明只有一条语句"return count"，为什么还要做同步？

这是我之前的一个困惑，不知道大家有没有想过这个问题。某个方法中如果有多条语句，并且都在操作同一个类变量，那么在多线程环境下不加锁，势必会引发线程安全问题，这很好理解，但是size()方法明明只有一条语句，为什么还要加锁？

关于这个问题，在慢慢地工作、学习中，有了理解，主要原因有两点：

1）同一时间只能有一条线程执行固定类的同步方法，但是对于类的非同步方法，可以多条线程同时访问。所以，这样就有问题了，可能线程A在执行Hashtable的put方法添加数据，线程B则可以正常调用size()方法读取Hashtable中当前元素的个数，那读取到的值可能不是最新的，可能线程A添加了完了数据，但是没有对size++，线程B就已经读取size了，那么对于线程B来说读取到的size一定是不准确的。而给size()方法加了同步之后，意味着线程B调用size()方法只有在线程A调用put方法完毕之后才可以调用，这样就保证了线程安全性

2）CPU执行代码，执行的不是Java代码，这点很关键，一定得记住。Java代码最终是被翻译成机器码执行的，机器码才是真正可以和硬件电路交互的代码。即使你看到Java代码只有一行，甚至你看到Java代码编译之后生成的字节码也只有一行，也不意味着对于底层来说这句语句的操作只有一个。一句"return count"假设被翻译成了三句汇编语句执行，一句汇编语句和其机器码做对应，完全可能执行完第一句，线程就切换了。

 

38、线程类的构造方法、静态块是被哪个线程调用的

这是一个非常刁钻和狡猾的问题。请记住：线程类的构造方法、静态块是被new这个线程类所在的线程所调用的，而run方法里面的代码才是被线程自身所调用的。

如果说上面的说法让你感到困惑，那么我举个例子，假设Thread2中new了Thread1，main函数中new了Thread2，那么：

1）Thread2的构造方法、静态块是main线程调用的，Thread2的run()方法是Thread2自己调用的

2）Thread1的构造方法、静态块是Thread2调用的，Thread1的run()方法是Thread1自己调用的

 

39、同步方法和同步块，哪个是更好的选择

同步块，这意味着同步块之外的代码是异步执行的，这比同步整个方法更提升代码的效率。请知道一条原则：同步的范围越小越好。

借着这一条，我额外提一点，虽说同步的范围越少越好，但是在Java虚拟机中还是存在着一种叫做锁粗化的优化方法，这种方法就是把同步范围变大。这是有用的，比方说StringBuffer，它是一个线程安全的类，自然最常用的append()方法是一个同步方法，我们写代码的时候会反复append字符串，这意味着要进行反复的加锁->解锁，这对性能不利，因为这意味着Java虚拟机在这条线程上要反复地在内核态和用户态之间进行切换，因此Java虚拟机会将多次append方法调用的代码进行一个锁粗化的操作，将多次的append的操作扩展到append方法的头尾，变成一个大的同步块，这样就减少了加锁-->解锁的次数，有效地提升了代码执行的效率。

 

40、高并发、任务执行时间短的业务怎样使用线程池？并发不高、任务执行时间长的业务怎样使用线程池？并发高、业务执行时间长的业务怎样使用线程池？

这是我在并发编程网上看到的一个问题，把这个问题放在最后一个，希望每个人都能看到并且思考一下，因为这个问题非常好、非常实际、非常专业。关于这个问题，个人看法是：

1）高并发、任务执行时间短的业务，线程池线程数可以设置为CPU核数+1，减少线程上下文的切换

2）并发不高、任务执行时间长的业务要区分开看：

a）假如是业务时间长集中在IO操作上，也就是IO密集型的任务，因为IO操作并不占用CPU，所以不要让所有的CPU闲下来，可以加大线程池中的线程数目，让CPU处理更多的业务

b）假如是业务时间长集中在计算操作上，也就是计算密集型任务，这个就没办法了，和（1）一样吧，线程池中的线程数设置得少一些，减少线程上下文的切换

c）并发高、业务执行时间长，解决这种类型任务的关键不在于线程池而在于整体架构的设计，看看这些业务里面某些数据是否能做缓存是第一步，增加服务器是第二步，至于线程池的设置，设置参考其他有关线程池的文章。最后，业务执行时间长的问题，也可能需要分析一下，看看能不能使用中间件对任务进行拆分和解耦。


1.并行和并发有什么区别？
并发：是指多个线程任务在同一个CPU上快速地轮换执行，由于切换的速度非常快，给人的感觉就是这些线程任务是在同时进行的，但其实并发只是一种逻辑上的同时进行；
并行：是指多个线程任务在不同CPU上同时进行，是真正意义上的同时执行。
2.进程和线程的区别与联系?
区别
并发性：不仅进程之间可以并发执行，同一个进程的多个线程之间也可并发执行。

拥有资源：进程是拥有资源的一个独立单位，线程不拥有系统资源，但可以访问隶属于进程的资源。

系统开销：多进程的程序要比多线程的程序健壮，但在进程切换时，耗费资源较大，效率要差一些。

线程和进程在使用上各有优缺点：线程执行开销小，但不利于资源的管理和保护；而进程正相反。同时，线程适合于在SMP机器上运行，而进程则可以跨机器迁移。

联系
一个线程只能属于一个进程，而一个进程可以有多个线程，但至少有一个线程；

资源分配给进程，同一进程的所有线程共享该进程的所有资源；

处理机分给线程，即真正在处理机上运行的是线程；

线程在执行过程中，需要协作同步。不同进程的线程间要利用消息通信的办法实现同步。

3.守护线程是什么？
护线程是程序运行的时候在后台提供一种通用服务的线程。所有用户线程停止，进程会停掉所有守护线程，退出程序。

守护线程拥有自动结束自己生命周期的特性，而非守护线程不具备这个特点。

应用场景:
JVM 中的垃圾回收线程就是典型的守护线程， 当 JVM 要退出时，垃圾回收线程也会结束自己的生命周期.

4.创建线程有哪几种方式？
1.继承Thread类

2.实现Runnable接口

3.实现Callable接口

4.通过线程池创建线程(ThreadPollExecutor,ExecutorService..)

5.说一下 runnable 和 callable 有什么区别？
Runnable和Callable 都是接口，分别提供run方法和call方法

Runnable的run方法无返回值，Callable的call方法提供返回值来表示任务运行结果

Runnable无法通过throws抛出异常，所有CheckedException必须在run方法内部处理。Callable可直接抛出Exception异常.

Runnable可以作为Thread构造器的参数，通过开启新的线程来执行，也可以通过线程池来执行。而Callable通过提交给线程池执行

6.线程有哪些状态？
这里要注意审题,是系统线程状态还是Java中线程的状态?

Java中线程的状态

java.lang.Thread类

public enum State {

    NEW,


    RUNNABLE,


    BLOCKED,

 
    WAITING,


    TIMED_WAITING,


    TERMINATED;
}
操作系统中线程的状态
初始状态（NEW)

对应 Java中的NEW

可运行状态（READY）

对应 Java中的 RUNNBALE 状态

运行状态（RUNNING)

对应 Java中的 RUNNBALE 状态

等待状态（WAITING)

该状态在 Java中被划分为了 BLOCKED，WAITING，TIMED_WAITING 三种状态

当线程调用阻塞式 API时，进程(线程)进入等待状态，这里指的是操作系统层面的。从 JVM层面来说，Java线程仍然处于 RUNNABLE 状态。

JVM 并不关心操作系统线程的实际状态，从 JVM 看来，等待CPU使用权（操作系统状态为可运行态）与等待 I/O（操作系统处于等待状态）没有区别，都是在等待某种资源，所以都归入RUNNABLE 状态

终止状态 （DEAD）

对应  TERMINATED

7.sleep() 和 wait() 有什么区别？
sleep()和wait()都是线程暂停执行的方法。

sleep方法属于Thread类中的静态方法，wait属于Object的成员方法。

sleep()不涉及线程通信，调用时会暂停此线程指定的时间，但监控依然保持，不会释放对象锁，到时间自动恢复

wait() 用于线程间的通信，调用时会放弃对象锁**，进入**等待队列，待调用notify()/notifyAll()唤醒指定的线程或者所有线程，才进入对象锁定池准备重新获得对象锁进入运行状态。

wait，notify和notifyAll只能在同步控制方法或者同步控制块里面使用，而sleep可以在任何地方使用（使用范围）

sleep()方法必须捕获异常InterruptedException，而wait()\notify()以及notifyAll()不需要捕获异常。

8.notify()和 notifyAll()有什么区别？
notify() 方法随机唤醒对象的等待池中的一个线程，进入锁池；

notifyAll() 唤醒对象的等待池中的所有线程，进入锁池。

等待池：假设线程A调用了某个对象的wait()方法，线程A就会释放该对象的锁，并进入该对象的等待池，等待池中的线程不会去竞争该对象的锁。
锁池：只有获取了对象的锁，线程才能执行对象的 synchronized 代码，对象的锁每次只有一个线程可以获得，其他线程只能在锁池中等待
9.线程的 run() 和 start() 有什么区别？
调用 start() 方法是用来启动线程的，轮到该线程执行时，会自动调用 run()；

调用 run() 方法，无法达到启动多线程的目的，相当于主线程线性执行 Thread 对象的 run() 方法。

一个线程对线的 start() 方法只能调用一次，多次调用会抛出 java.lang.IllegalThreadStateException 异常；而run() 方法没有限制。

10.创建线程池有哪几种方式?
通过Executors工厂方法创建   (阿里巴巴开发规约中不建议使用此种方式创建线程池)
通过new  ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueueworkQueue)   自定义创建(推荐)
11.Java线程池中submit() 和 execute()方法的区别
两个方法都可以向线程池提交任务

execute()方法的返回类型是void，它定义在Executor接口中。

submit()方法可以返回持有计算结果的Future对象，它定义在ExecutorService接口中，它扩展了Executor接口，其它线程池类像ThreadPoolExecutor和ScheduledThreadPoolExecutor都有这些方法。

12.在 java 程序中怎么保证多线程的运行安全？
程序中保证多线程运行安全的方式：

1.使用安全类，比如 Java. util. concurrent 下的类。

2.使用自动锁 synchronized。

3.使用手动锁 Lock  例如Reentrantlock。

4.保证一个或者多个操作在CPU执行的过程中不被中断。(原子性)

5.保证一个线程对共享变量的修改，另外一个线程能够立刻看到。(可见性)

6.保证程序执行的顺序按照代码的先后顺序执行。(有序性)

注意回答中不能缺少这3种特性

线程的安全性问题体现在：

原子性：一个或者多个操作在 CPU 执行的过程中不被中断的特性
可见性：一个线程对共享变量的修改，另外一个线程能够立刻看到
有序性：程序执行的顺序按照代码的先后顺序执行
13.多线程锁的升级原理是什么？
锁的级别从低到高：

无锁 -> 偏向锁 -> 轻量级锁 -> 重量级锁

锁分级别原因：

没有优化以前，synchronized是重量级锁（悲观锁），使用 wait 和 notify、notifyAll 来切换线程状态非常消耗系统资源；线程的挂起和唤醒间隔很短暂，这样很浪费资源，影响性能。所以 JVM 对 synchronized 关键字进行了优化，把锁分为 无锁、偏向锁、轻量级锁、重量级锁 状态。

在学习并发编程知识synchronized时，我们总是难以理解其实现原理，因为偏向锁、轻量级锁、重量级锁都涉及到对象头，所以了解java对象头是我们深入了解synchronized的前提条件.这篇文章包含了对象头的解析以及锁膨胀过程的解析:

JAVA对象布局之对象头(Object Header)

14. 什么是死锁？什么是活锁? 什么是线程饥饿?
JAVA并发之加锁导致的活跃性问题剖析

15.ThreadLocal 是什么？有哪些使用场景？
1.ThreadLocal  介绍

2.ThreadLocal  应用

3.ThreadLocal  源码解析

3.1解决 Hash 冲突

4.ThreadLocal 特性

5..ThreadLocal 内存泄露问题

java并发之无同步方案-ThreadLocal

16.说一下synchronized底层实现原理?
前置知识,需要了解 对象头.-->JAVA对象布局之对象头(Object Header)

同步代码块是通过 monitorenter 和 monitorexit 指令获取线程的执行权

monitorenter，如果当前monitor的进入数为0时，线程就会进入monitor，并且把进入数+1，那么该线程就是monitor的拥有者(owner)。

如果该线程已经是monitor的拥有者，又重新进入，就会把进入数再次+1。也就是可重入的。

执行monitorexit的线程必须是monitor的拥有者，指令执行后，monitor的进入数减1，如果减1后进入数为0，则该线程会退出monitor。其他被阻塞的线程就可以尝试去获取monitor的所有权。

monitorexit指令出现了两次，第1次为同步正常退出释放锁；第2次为发生异步退出释放锁；

同步方法通过加 ACC_SYNCHRONIZED 标识实现线程的执行权的控制

标志位ACC_SYNCHRONIZED，作用就是一旦执行到这个方法时，就会先判断是否有标志位，如果有这个标志位，就会先尝试获取monitor，获取成功才能执行方法，方法执行完成后再释放monitor。在方法执行期间，其他线程都无法获取同一个monitor。归根结底还是对monitor对象的争夺，只是同步方法是一种隐式的方式来实现。

总的来说，synchronized的底层原理是通过monitor对象来完成的

## 17.synchronized和volatile的区别是什么？
作用：

synchronized 表示只有一个线程可以获取作用对象的锁，执行代码，阻塞其他线程。
volatile 表示变量在 CPU 的寄存器中是不确定的，必须从主存中读取。保证多线程环境下变量的可见性；禁止指令重排序。
区别：

synchronized 可以作用于变量、方法、对象；volatile 只能作用于变量。
synchronized 可以保证线程间的有序性、原子性和可见性；volatile 只保证了可见性和有序性，无法保证原子性。
synchronized 线程阻塞，volatile 线程不阻塞。

## 18.synchronized和Lock的区别是什么？

在多线程情况下，锁是线程控制的重要途径。Java为此也提供了2种锁机制，synchronized和lock。

## volatile关键字的作用

一个非常重要的问题，是每个学习、应用多线程的Java程序员都必须掌握的。理解volatile关键字的作用的前提是要理解Java内存模型，这里就不讲Java内存模型了，可以参见第31点，volatile关键字的作用主要有两个：

1）多线程主要围绕可见性和原子性两个特性而展开，使用volatile关键字修饰的变量，保证了其在多线程之间的可见性，即每次读取到volatile变量，一定是最新的数据。

2）代码底层执行不像我们看到的高级语言----Java程序这么简单，它的执行是Java代码-->字节码-->根据字节码执行对应的C/C++代码-->C/C++代码被编译成汇编语言-->和硬件电路交互，现实中，为了获取更好的性能JVM可能会对指令进行重排序，多线程下可能会出现一些意想不到的问题。使用volatile则会对禁止语义重排序，当然这也一定程度上降低了代码执行效率。

从实践角度而言，volatile的一个重要作用就是和CAS结合，保证了原子性，详细的可以参见java.util.concurrent.atomic包下的类，比如AtomicInteger，更多详情请点击这里进行学习。


我们这里不讨论具体的实现原理和细节,只讨论它们的区别

如果有小伙伴有兴趣更深入了解它们,请关注公众号:JAVA宝典



区别

lock是一个接口，而synchronized是java的一个关键字。

synchronized在发生异常时会自动释放占有的锁，因此不会出现死锁；而lock发生异常时，不会主动释放占有的锁，必须手动来释放锁，可能引起死锁的发生(也称隐式锁和显式锁)

lock等待锁过程中可以用interrupt来中断等待，而synchronized只能等待锁的释放，不能响应中断；

Lock可以通过trylock来知道有没有获取锁，而synchronized不能；

Lock可以提高多个线程进行读操作的效率。（可以通过readwritelock实现读写分离）

在性能上来说，如果竞争资源不激烈，两者的性能是差不多的，而当竞争资源非常激烈时（即有大量线程同时竞争），此时Lock的性能要优于synchronized。在使用时要根据适当情况选择。

synchronized 是 JVM 层面实现的；Lock 是 JDK 代码层面实现

synchronized使用Object对象本身的wait 、notify、notifyAll调度机制，而Lock可以使用Condition进行线程之间的调度

继上一条,synchronized只有一个阻塞队列,而Lock使用Condition可以有多个阻塞队列

synchronized和lock的用法区别

synchronized：在需要同步的对象中加入此控制，synchronized可以加在方法上，也可以加在特定代码块中，括号中表示需要锁的对象。

lock：一般使用ReentrantLock类做为锁。在加锁和解锁处需要通过lock()和unlock()显示指出。所以一般会在finally块中写unlock()以防死锁。

19.说一下 你对atomic 的理解 ?
在JDK5.0之前，想要实现无锁无等待的算法是不可能的，除非用本地库，自从有了Atomic变量类后，这成为可能。

在java.util.concurrent.atomic包下有这些类:

标量类：AtomicBoolean，AtomicInteger，AtomicLong，AtomicReference
数组类：AtomicIntegerArray，AtomicLongArray，AtomicReferenceArray
更新器类：AtomicLongFieldUpdater，AtomicIntegerFieldUpdater，AtomicReferenceFieldUpdater
复合变量类：AtomicMarkableReference，AtomicStampedReference
拿AtomicInteger来举例,其内部实现不是简单的使用synchronized，而是一个更为高效的方式CAS (compare and swap) + volatile和native方法，从而避免了synchronized的高开销，执行效率大为提升。

/** 
 * Atomically increments by one the current value. 
 * 
 * @return the previous value 
 */  
public final int getAndIncrement() {  
    return unsafe.getAndAddInt(this, valueOffset, 1);  
} 
这里直接调用一个叫Unsafe的类去处理,这个类是用于执行低级别、不安全操作的方法集合。尽管这个类和所有的方法都是公开的（public），但是这个类的使用仍然受限，你无法在自己的java程序中直接使用该类，因为只有授信的代码才能获得该类的实例。所以我们平时的代码是无法使用这个类的，因为其设计的操作过于偏底层，如若操作不慎可能会带来很大的灾难，所以直接禁止普通代码的访问，当然JDK使用是没有问题的。

关于CAS 在我的另一篇文章: 什么是CAS,ABA问题怎么解决?

20.说一下 你对Semaphore 的理解 ?
Semaphore就是一个信号量，它的作用是限制某段代码块的并发数。
Semaphore有一个构造函数，可以传入一个int型整数n，表示某段代码最多只有n个线程可以访问，
如果超出了n，那么请等待，等到某个线程执行完毕这段代码块，下一个线程再进入。
由此可以看出如果Semaphore构造函数中传入的int型整数n=1，相当于变成了一个synchronized了。
Semaphore类位于java.util.concurrent包下，它提供了2个构造器：

//参数permits表示许可数目，即同时可以允许多少线程进行访问  
public Semaphore(int permits) {  
    sync = new NonfairSync(permits);  
}  
//这个多了一个参数fair表示是否是公平的，即等待时间越久的越先获取许可  
public Semaphore(int permits, boolean fair) {  
    sync = (fair)? new FairSync(permits) : new NonfairSync(permits);  
}  
Semaphore类中比较重要的几个方法，首先是acquire()、release()方法：
acquire()用来获取一个许可，若无许可能够获得，则会一直等待，直到获得许可。
release()用来释放许可。注意，在释放许可之前，必须先获获得许可。
//尝试获取一个许可，若获取成功，则立即返回true，若获取失败，则立即返回false  
public boolean tryAcquire() { };  
//尝试获取一个许可，若在指定的时间内获取成功，则立即返回true，否则则立即返回false  
public boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException { };   
//尝试获取permits个许可，若获取成功，则立即返回true，若获取失败，则立即返回false  
public boolean tryAcquire(int permits) { };   
//尝试获取permits个许可，若在指定的时间内获取成功，则立即返回true  
public boolean tryAcquire(int permits, long timeout, TimeUnit unit) throws InterruptedException { };  
//得到当前可用的许可数目  
public int availablePermits(); 

46
同步⽅法和同步块，哪个是更好的选择?


        同步块是更好的选择，因为它不会锁住整个对象（当然也可以让它锁住整个对象）。同步⽅法会锁住整个对象，哪怕这个类中有多个不相关联的同步块，这通常会导致他们停⽌执⾏并需要等待获得这个对象上的锁。synchronized(this)以及⾮static的synchronized⽅法（⾄于static synchronized⽅法请往下看），只能防⽌多个线程同时执⾏同⼀个对象的同步代码段。如果要锁住多个对象⽅法，可以锁住⼀个固定的对象，或者锁住这个类的Class对象。synchronized锁住的是括号⾥的对象，⽽不是代码。对于⾮static的synchronized⽅法，锁的就是对象本身也就是this。例如：

public class SynObj{ 
  public synchronized void showA(){ 
    System.out.println("showA.."); 
    try { 
      Thread.sleep(3000); 
    } catch (InterruptedException e) { 
      e.printStackTrace(); 
    }
  }
  public void showB(){
    synchronized (this) {
      System.out.println("showB..");
    }
  }
}
47
如何检测死锁？怎么预防死锁？
概念：

是指两个或两个以上的进程在执⾏过程中，因争夺资源⽽造成的⼀种互相等待的现象，若⽆外⼒作⽤，它们都将⽆法推进下去。此时称系统处于死锁；

死锁的四个必要条件：

（1）互斥条件：进程对所分配到的资源不允许其他进程进⾏访问，若其他进程访问该资源，只能等待，直⾄占有该资源的进程使⽤完成后释放该资源

（2）请求和保持条件：进程获得⼀定的资源之后，⼜对其他资源发出请求，但是该资源可能被其他进程占有，此时请求阻塞，但⼜对⾃⼰获得的资源保持不放

（3）不可剥夺条件：是指进程已获得的资源，在未完成使⽤之前，不可被剥夺，只能在使⽤完后⾃⼰释放

（4）环路等待条件：是指进程发⽣死锁后，若⼲进程之间形成⼀种头尾相接的循环等待资源关系

死锁产⽣的原因：

（1）因竞争资源发⽣死锁 现象：系统中供多个进程共享的资源的数⽬不⾜以满⾜全部进程的需要时，就会引起对诸资源的竞争⽽发⽣死锁现象

（2）进程推进顺序不当发⽣死锁

检查死锁

（1）有两个容器，⼀个⽤于保存线程正在请求的锁，⼀个⽤于保存线程已经持有的锁。每次加锁之前都会做如下检测。

（2）检测当前正在请求的锁是否已经被其它线程持有,如果有，则把那些线程找出来

（3）遍历第⼀步中返回的线程，检查⾃⼰持有的锁是否正被其中任何⼀个线程请求，如果第⼆步返回真,表示出现了死锁

死锁的解除与预防：控制不要让四个必要条件成⽴。

48
HashMap在多线程环境下使⽤需要注意什么？


        要注意死循环的问题，HashMap的put操作引发扩容，这个动作在多线程并发下会发⽣线程死循环的问题。

HashMap不是线程安全的；Hashtable线程安全，但效率低，因为是Hashtable是使⽤synchronized的，所有线程竞争同⼀把锁；⽽ConcurrentHashMap不仅线程安全⽽且效率⾼，因为它包含⼀个segment数组，将数据分段存储，给每⼀段数据配⼀把锁，也就是所谓的锁分段技术。

HashMap为何线程不安全：

（1）put时key相同导致其中⼀个线程的value被覆盖；

（2）多个线程同时扩容，造成数据丢失；

（3）多线程扩容时导致Node链表形成环形结构成.next()死循环，导致CPU利⽤率接近100%；

ConcurrentHashMap最⾼效；

49
什么是守护线程？有什么⽤？


        守护线程（即daemon thread），是个服务线程，准确地来说就是服务其他的线程，这是它的作⽤——⽽其他的线程只有⼀种，那就是⽤户线程。所以java⾥线程分2种，

守护线程，⽐如垃圾回收线程，就是最典型的守护线程。

⽤户线程，就是应⽤程序⾥的⾃定义线程。

50
如何实现线程串⾏执⾏？

为了控制线程执⾏的顺序，如ThreadA->ThreadB->ThreadC->ThreadA循环执⾏三个线程，我们需要确定唤醒、等待的顺序。这时我们可以同时使⽤ Obj.wait()、Obj.notify()与synchronized(Obj)来实现这个⽬标。线程中持有上⼀个线程类的对象锁以及⾃⼰的锁，由于这种依赖关系，该线程执⾏需要等待上个对象释放锁，从⽽保证类线程执⾏的顺序。

通常情况下，wait是线程在获取对象锁后，主动释放对象锁，同时本线程休眠，直到有其它线程调⽤对象的notify()唤醒该线程，才能继续获取对象锁，并继续执⾏。⽽notify()则是对等待对象锁的线程的唤醒操作。但值得注意的是notify()调⽤后，并不是⻢上就释放对象锁，⽽是在相应的synchronized(){}语句块执⾏结束。释放对象锁后，JVM会在执⾏wait()等待对象锁的线程中随机选取⼀线程，赋予其对象锁，唤醒线程，继续执⾏。

public class ThreadSerialize { 
  public static void main(String[] args){ 
    ThreadA threadA = new ThreadA(); 
    ThreadB threadB = new ThreadB(); 
    ThreadC threadC = new ThreadC(); 
    threadA.setThreadC(threadC); 
    threadB.setThreadA(threadA);
    threadC.setThreadB(threadB);
    threadA.start();
    threadB.start();
    threadC.start();
    while (true){
      try {
        Thread.currentThread().sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}

class ThreadA extends Thread{
  private ThreadC threadC;

  @Override
  public void run() {
    while (true){
      synchronized (threadC){
        synchronized (this){
          System.out.println("I am ThreadA。。。");
          this.notify();
        }
        try {
          threadC.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
  public void setThreadC(ThreadC threadC) {
    this.threadC = threadC;
  }
}
class ThreadB extends Thread{
  private ThreadA threadA;

  @Override
  public void run() {
    while (true){
      synchronized (threadA){
        synchronized (this){
          System.out.println("I am ThreadB。。。");
          this.notify();
        }
                try {
                    threadA.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void setThreadA(ThreadA threadA) {
        this.threadA = threadA;
    }
}
class ThreadC extends Thread{
    private ThreadB threadB;
    @Override
    public void run() {
        while (true){
            synchronized (threadB){
                synchronized (this){
                    System.out.println("I am ThreadC");
                    this.notify();
                }
                try {
                    threadB.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void setThreadB(ThreadB threadB) {
        this.threadB = threadB;
    }
}
51
可以运⾏时kill掉⼀个线程吗？


不可以，线程有5种状态，新建（new）、可运⾏（runnable）、运⾏中（running）、阻塞（block）、死亡（dead）。

只有当线程run⽅法或者主线程main⽅法结束，⼜或者抛出异常时，线程才会结束⽣命周期。

52
如何理解synchronized


在某个对象的所有synchronized⽅法中,在某个时刻只能有⼀个唯⼀的⼀个线程去访问这些synchronized⽅法

如果⼀个⽅法是synchronized⽅法,那么该synchronized关键字表示给当前对象上锁(即this)相当于synchronized(this){}

如果⼀个synchronized⽅法是static的,那么该synchronized表示给当前对象所对应的class对象上锁(每个类不管⽣成多少对象,其对应的class对象只有⼀个)

53
分步式锁,程序数据库中死锁机制及解决⽅案


        基本原理：⽤⼀个状态值表示锁，对锁的占⽤和释放通过状态值来标识。

三种分布式锁：

（1）Zookeeper

a、原理：

基于zookeeper瞬时有序节点实现的分布式锁，其主要逻辑如下。⼤致思想即为：每个客户端对某个功能加锁时，在zookeeper上的与该功能对应的指定节点的⽬录下，⽣成⼀个唯⼀的瞬时有序节点。判断是否获取锁的⽅式很简单，只需要判断有序节点中序号最⼩的⼀个。当释放锁的时候，只需将这个瞬时节点删除即可。同时，其可以避免服务宕机导致的锁⽆法释放，⽽产⽣的死锁问题。

b、优点：

锁安全性⾼，zk可持久化，且能实时监听获取锁的客户端状态。⼀旦客户端宕机，则瞬时节点随之消失，zk因⽽能第⼀时间释放锁。这也省去了⽤分布式缓存实现锁的过程中需要加⼊超时时间判断的这⼀逻辑。

c、缺点：

性能开销⽐较⾼。因为其需要动态产⽣、销毁瞬时节点来实现锁功能。所以不太适合直接提供给⾼并发的场景使⽤。

d、实现：

可以直接采⽤zookeeper第三⽅库curator即可⽅便地实现分布式锁。

e、适⽤场景：

对可靠性要求⾮常⾼，且并发程度不⾼的场景下使⽤。如核⼼数据的定时全量/增量同步等。

（2）memcached

a、原理：

memcached带有add函数，利⽤add函数的特性即可实现分布式锁。add和set的区别在于：如果多线程并发set，则每个set都会成功，但最后存储的值以最后的set的线程为准。⽽add的话则相反，add会添加第⼀个到达的值，并返回true，后续的添加则都会返回false。利⽤该点即可很轻松地实现分布式锁。

b、优点

并发⾼效

c、缺点

memcached采⽤列⼊LRU置换策略，所以如果内存不够，可能导致缓存中的锁信息丢失。memcached⽆法持久化，⼀旦重启，将导致信息丢失。

d、使⽤场景

⾼并发场景。需要 1)加上超时时间避免死锁; 2)提供⾜够⽀撑锁服务的内存空间; 3)稳定的集群化管理。

（3）redis

a、原理

redis分布式锁即可以结合zk分布式锁锁⾼度安全和memcached并发场景下效率很好的优点，其实现⽅式和memcached类似，采⽤setnx即可实现。需要注意的是，这⾥的redis也需要设置超时时间，以避免死锁。可以利⽤jedis客户端实现。

ICacheKey cacheKey = new ConcurrentCacheKey(key, type);
return RedisDao.setnx(cacheKey, "1");
数据库死锁机制和解决⽅案：

（1）死锁：死锁是指两个或者两个以上的事务在执⾏过程中，因争夺锁资源⽽造成的⼀种互相等待的现象。

（2）处理机制：解决死锁最有⽤最简单的⽅法是不要有等待，将任何等待都转化为回滚，并且事务重新开始。但是有可能影响并发性能。

     a、超时回滚，innodb_lock_wait_time设置超时时间；

     b、wait-for-graph⽅法：跟超时回滚⽐起来，这是⼀种更加主动的死锁检测⽅式。InnoDB引擎也采⽤这种⽅式。

54
spring单例为什么没有安全问题(ThreadLocal)


ThreadLocal：spring使⽤ThreadLocal解决线程安全问题；ThreadLocal会为每⼀个线程提供⼀个独⽴的变量副本，从⽽隔离了多个线程对数据的访问冲突。因为每⼀个线程都拥有⾃⼰的变量副本，从⽽也就没有必要对该变量进⾏同步了。ThreadLocal提供了线程安全的共享对象，在编写多线程代码时，可以把不安全的变量封装进ThreadLocal。概括起来说，对于多线程资源共享的问题，同步机制采⽤了“以时间换空间”的⽅式，⽽ThreadLocal采⽤了“以空间换时间”的⽅式。前者仅提供⼀份变量，让不同的线程排队访问，⽽后者为每⼀个线程都提供了⼀份变量，因此可以同时访问⽽互不影响。在很多情况下，ThreadLocal⽐直接使⽤synchronized同步机制解决线程安全问题更简单，更⽅便，且结果程序拥有更⾼的并发性。

单例：⽆状态的Bean(⽆状态就是⼀次操作，不能保存数据。⽆状态对象(Stateless Bean)，就是没有实例变量的对象，不能保存数据，是不变类，是线程安全的。)适合⽤不变模式，技术就是单例模式，这样可以共享实例，提⾼性能。

55
线程池原理

使⽤场景：假设⼀个服务器完成⼀项任务所需时间为：T1-创建线程时间，T2-在线程中执⾏任务的时间，T3-销毁线程时间。如果T1+T3远⼤于T2，则可以使⽤线程池，以提⾼服务器性能；

组成：

（1）线程池管理器（ThreadPool）：⽤于创建并管理线程池，包括 创建线程池，销毁线程池，添加新任务；

（2）⼯作线程（PoolWorker）：线程池中线程，在没有任务时处于等待状态，可以循环的执⾏任务；

（3）任务接⼝（Task）：每个任务必须实现的接⼝，以供⼯作线程调度任务的执⾏，它主要规定了任务的⼊⼝，任务执⾏完后的收尾⼯作，任务的执⾏状态等；

（4）任务队列（taskQueue）：⽤于存放没有处理的任务。提供⼀种缓冲机制。

原理：线程池技术正是关注如何缩短或调整T1,T3时间的技术，从⽽提⾼服务器程序性能的。它把T1，T3分别安排在服务器程序的启动和结束的时间段或者⼀些空闲的时间段，这样在服务器程序处理客户请求时，不会有T1，T3的开销了。

⼯作流程：

（1）线程池刚创建时，⾥⾯没有⼀个线程(也可以设置参数prestartAllCoreThreads启动预期数量主线程)。任务队列是作为参数传进来的。不过，就算队列⾥⾯有任务，线程池也不会⻢上执⾏它们。

（2）当调⽤ execute() ⽅法添加⼀个任务时，线程池会做如下判断：

	a、如果正在运⾏的线程数量⼩于 corePoolSize，那么⻢上创建线程运⾏这个任务；

	b、如果正在运⾏的线程数量⼤于或等于 corePoolSize，那么将这个任务放⼊队列；

	c、如果这时候队列满了，⽽且正在运⾏的线程数量⼩于 maximumPoolSize，那么还是要创建⾮核⼼线程⽴刻运⾏这个任务；

	d、如果队列满了，且正在运⾏的线程数量⼤于或等于 maximumPoolSize，那么线程池会抛出异常RejectExecutionException。

（3）当⼀个线程完成任务时，它会从队列中取下⼀个任务来执⾏。

（4）当⼀个线程⽆事可做，超过⼀定的时间（keepAliveTime）时，线程池会判断，如果当前运⾏的线程数⼤于corePoolSize，那么这个线程就被停掉。所以线程池的所有任务完成后，它最终会收缩到 corePoolSize 的⼤⼩。

56
 java如何锁多个对象


	例如：在银⾏系统转账时，需要锁定两个账户，这个时候，顺序使⽤两个synchronized可能存在死锁的情况，看下⾯的例⼦：

public class Bank { 
    final static Object obj_lock = new Object(); 
    // Deadlock crisis 死锁
    public void transferMoney(Account from, Account to, int number) { 
        synchronized (from) { 
            synchronized (to) { 
                from.debit(); 
                to.credit();
            }
        }
    }
    // Thread safe
    public void transferMoney2(final Account from, final Account to, int number) {
        class Help {
            void transferMoney2() {
                from.debit();
                to.credit();
            }
        }
        //通过hashCode⼤⼩调整加锁顺序
        int fromHash = from.hashCode();
        int toHash = to.hashCode();
        if (fromHash < toHash) {
            synchronized (from) {
                synchronized (to) {
                    new Help().transferMoney2();
                }
            }
        } else if (toHash < fromHash) {
            synchronized (to) {
                synchronized (from) {
                    new Help().transferMoney2();
                }
            }
        } else {
            synchronized (obj_lock) {
                synchronized (to) {
                    synchronized (from) {
                        new Help().transferMoney2();
                    }
                }
            }
        }
    }
}
若操作账户A，B：

A的hashCode⼩于B， 先锁A再锁B

B的hashCode⼩于A， 先锁B再锁A

产⽣的hashCode相等，先锁住⼀个全局静态变量，在锁A，B

        这样就避免了两个线程分别操作账户A,B和B,A⽽产⽣死锁的情况。需要为Account对象写⼀个好的hashCode算法，使得不同账户间产⽣的hashCode尽量不同。

57
java线程如何启动

继承Thread类；

实现Runnable接⼝；

直接在函数体内：

⽐较：

（1）实现Runnable接⼝优势：

	a、适合多个相同的程序代码的线程去处理同⼀个资源

	b、可以避免java中的单继承的限制

	c、增加程序的健壮性，代码可以被多个线程共享，代码和数据独⽴。

（2）继承Thread类优势：

	a、可以将线程类抽象出来，当需要使⽤抽象⼯⼚模式设计时。

	b、多线程同步

（3）在函数体使⽤优势

	a、⽆需继承thread或者实现Runnable，缩⼩作⽤域。

58
 Java中加锁的⽅式有哪些,如何实现怎么个写法

java中有两种锁：⼀种是⽅法锁或者对象锁(在⾮静态⽅法或者代码块上加锁)，第⼆种是类锁(在静态⽅法或者class上加锁)；

注意：其他线程可以访问未加锁的⽅法和代码；synchronized同时修饰静态⽅法和实例⽅法，但是运⾏结果是交替进⾏的，这证明了类锁和对象锁是两个不⼀样的锁，控制着不同的区域，它们是互不⼲扰的。

示例代码：

（1）⽅法锁和同步代码块：

public class TestSynchronized { 
    public void test1() { 
        synchronized(this) { 
            int i = 5; 
            while( i-- > 0) { 
                System.out.println(Thread.currentThread().getName() + " : " + i); 
                try { 
                    Thread.sleep(500); 
                } catch (InterruptedException ie) {} 
            } 
        } 
    } 
    public synchronized void test2() { 
        int i = 5; 
        while( i-- > 0){ 
            System.out.println(Thread.currentThread().getName() + " : " + i); 
            try { 
                Thread.sleep(500); 
            } catch (InterruptedException ie) {} 
        } 
    } 
    public static void main(String[] args) { 
        final TestSynchronized myt2 = new TestSynchronized(); 
        Thread test1 = new Thread( new Runnable() { 
            public void run() { myt2.test1(); }
        }
        Thread test2 = new Thread( new Runnable() { 
            public void run() { myt2.test2(); }
        }
        test1.start();
        test2.start();
    } 
}
       （2）类锁：

public class TestSynchronized { 
    public void test1() { 
        synchronized(TestSynchronized.class) { 
            int i = 5; 
            while( i-- > 0) { 
                System.out.println(Thread.currentThread().getName() + " : " + i); 
                try { 
                    Thread.sleep(500); 
                } catch (InterruptedException ie) {} 
            } 
        } 
    } 
    public static synchronized void test2() { 
        int i = 5; 
        while( i-- > 0) { 
            System.out.println(Thread.currentThread().getName() + " : " + i); 
            try { 
                Thread.sleep(500); 
            } catch (InterruptedException ie) {} 
        } 
    } 
    public static void main(String[] args) { 
        final TestSynchronized myt2 = new TestSynchronized(); 
        Thread test1 = new Thread(new Runnable() {
            public void run() { 
                myt2.test1();
            }
        }
    Thread test2 = new Thread( new Runnable() {
            public void run() {
                TestSynchronized.test2();
        test1.start(); 
                test2.start(); 
                // TestRunnable tr=new TestRunnable();
                // Thread test3=new Thread(tr); 
                // test3.start(); 
            } 
        }
  }
}
59
如何保证数据不丢失

使⽤消息队列，消息持久化；

添加标志位：未处理 0，处理中 1，已处理 2。定时处理。

60
ThreadLocal为什么会发⽣内存泄漏？


   1. threadlocal原理图：

图片

OOM实现：

（1）ThreadLocal的实现是这样的：每个Thread 维护⼀个 ThreadLocalMap 映射表，这个映射表的 key 是 ThreadLocal实例本身，value 是真正需要存储的 Object。

（2）也就是说 ThreadLocal 本身并不存储值，它只是作为⼀个 key 来让线程从 ThreadLocalMap 获取 value。值得注意的是图中的虚线，表示 ThreadLocalMap 是使⽤ ThreadLocal 的弱引⽤作为 Key 的，弱引⽤的对象在 GC 时会被回收。

（3）ThreadLocalMap使⽤ThreadLocal的弱引⽤作为key，如果⼀个ThreadLocal没有外部强引⽤来引⽤它，那么系统 GC的时候，这个ThreadLocal势必会被回收，这样⼀来，ThreadLocalMap中就会出现key为null的Entry，就没有办法访问这些key为null的Entry的value，如果当前线程再迟迟不结束的话，这些key为null的Entry的value就会⼀直存在⼀条强引⽤链：Thread Ref -> Thread -> ThreaLocalMap -> Entry -> value永远⽆法回收，造成内存泄漏。

预防办法：

在ThreadLocal的get(),set(),remove()的时候都会清除线程ThreadLocalMap⾥所有key为null的value。但是这些被动的预防措施并不能保证不会内存泄漏：

（1）使⽤static的ThreadLocal，延⻓了ThreadLocal的⽣命周期，可能导致内存泄漏。

（2）分配使⽤了ThreadLocal⼜不再调⽤get(),set(),remove()⽅法，那么就会导致内存泄漏，因为这块内存⼀直存在。

61
jdk1.8中对ConcurrentHashMap的改进


Java 7为实现并⾏访问，引⼊了Segment这⼀结构，实现了分段锁，理论上最⼤并发度与Segment个数相等。

Java 8为进⼀步提⾼并发性，摒弃了分段锁的⽅案，⽽是直接使⽤⼀个⼤的数组。同时为了提⾼哈希碰撞下的寻址性能，Java 8在链表⻓度超过⼀定阈值（8）时将链表（寻址时间复杂度为O(N)）转换为红⿊树（寻址时间复杂度为O(long(N))）。

源码：

public V put(K key, V value) {
    return putVal(key, value, false);
}
final V putVal(K key, V value, boolean onlyIfAbsent) {
    //ConcurrentHashMap 不允许插⼊null键，HashMap允许插⼊⼀个null键
    if (key == null || value == null) throw new NullPointerException();
    //计算key的hash值
    int hash = spread(key.hashCode());
    int binCount = 0;
    //for循环的作⽤：因为更新元素是使⽤CAS机制更新，需要不断的失败重试，直到成功为⽌。
    for (Node<K,V>[] tab = table;;) {
        // f：链表或红⿊⼆叉树头结点，向链表中添加元素时，需要synchronized获取f的锁。
        Node<K,V> f; int n, i, fh;
        //判断Node[]数组是否初始化，没有则进⾏初始化操作
        if (tab == null || (n = tab.length) == 0)
            tab = initTable();
        //通过hash定位Node[]数组的索引坐标，是否有Node节点，如果没有则使⽤CAS进⾏添加（链表的头结点），添加失败则进⼊下次循环。
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
            if (casTabAt(tab, i, null,new Node<K,V>(hash, key, value, null)))
                break; // no lock when adding to empty bin
        }
        //检查到内部正在移动元素（Node[] 数组扩容）
        else if ((fh = f.hash) == MOVED)
            //帮助它扩容
            tab = helpTransfer(tab, f);
        else {
            V oldVal = null;
            //锁住链表或红⿊⼆叉树的头结点
            synchronized (f) {
                //判断f是否是链表的头结点
                if (tabAt(tab, i) == f) {
                    //如果fh>=0 是链表节点
                    if (fh >= 0) {
                        binCount = 1;
                        //遍历链表所有节点
                        for (Node<K,V> e = f;; ++binCount) {
                            K ek;
                            //如果节点存在，则更新value
                            if (e.hash == hash &&((ek = e.key) == key ||(ek != null && key.equals(ek)))) {
                                oldVal = e.val;
                                if (!onlyIfAbsent)
                                    e.val = value;
                                break;
                            }
                            //不存在则在链表尾部添加新节点。
                            Node<K,V> pred = e;
                            if ((e = e.next) == null) {
                                pred.next = new Node<K,V>(hash, key,value, null);
                                break;
                            }
                        }
                    }
                    //TreeBin是红⿊⼆叉树节点
                    else if (f instanceof TreeBin) {
                        Node<K,V> p;
                        binCount = 2;
                        //添加树节点
                        if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,value)) != null) {
                            oldVal = p.val;
                            if (!onlyIfAbsent)
                                p.val = value;
                        }
                    }
                }
            }
            if (binCount != 0) {
                //如果链表⻓度已经达到临界值8 就需要把链表转换为树结构
                if (binCount >= TREEIFY_THRESHOLD)
                    treeifyBin(tab, i);
                if (oldVal != null)
                    return oldVal;
                break;
            }
        }
    }
    //将当前ConcurrentHashMap的size数量+1
    addCount(1L, binCount);
    return null;
}
62
线程a,b,c,d运⾏任务，如何保证当a,b,c线程执⾏完再执⾏d线程?


CountDownLatch类

 ⼀个同步辅助类，常⽤于某个条件发⽣后才能执⾏后续进程。给定计数初始化CountDownLatch，调⽤countDown(）⽅法，在计数到达零之前，await⽅法⼀直受阻塞。重要⽅法为countdown()与await()；

join⽅法

 将线程B加⼊到线程A的尾部，当A执⾏完后B才执⾏。

public static void main(String[] args) throws Exception { 
    Th t = new Th("t1"); 
    Th t2 = new Th("t2"); 
    t.start(); 
    t.join(); 
    t2.start(); 
}
    3. notify、wait⽅法，Java中的唤醒与等待⽅法，关键为synchronized代码   块，参数线程间应相同，也常⽤Object作为参数。

63
⾼并发系统如何做性能优化？如何防⽌库存超卖？


⾼并发系统性能优化：优化程序，优化服务配置，优化系统配置

（1）尽量使⽤缓存，包括⽤户缓存，信息缓存等，多花点内存来做缓存，可以⼤量减少与数据库的交互，提⾼性能。

（2）⽤jprofiler等⼯具找出性能瓶颈，减少额外的开销。

（3）优化数据库查询语句，减少直接使⽤hibernate等⼯具的直接⽣成语句（仅耗时较⻓的查询做优化）。

（4）优化数据库结构，多做索引，提⾼查询效率。

（5）统计的功能尽量做缓存，或按每天⼀统计或定时统计相关报表，避免需要时进⾏统计的功能。

（6）能使⽤静态⻚⾯的地⽅尽量使⽤，减少容器的解析（尽量将动态内容⽣成静态html来显示）。

（7）解决以上问题后，使⽤服务器集群来解决单台的瓶颈问题。

防⽌库存超卖：

悲观锁：在更新库存期间加锁，不允许其它线程修改；

（1）数据库锁：select xxx for update；

（2）分布式锁；

乐观锁：使⽤带版本号的更新。每个线程都可以并发修改，但在并发时，只有⼀个线程会修改成功，其它会返回失败。

redis watch：监视键值对，作⽤时如果事务提交exec时发现监视的监视对发⽣变化，事务将被取消。

消息队列：通过 FIFO 队列，使修改库存的操作串⾏化。

总结：总的来说，不能把压⼒放在数据库上，所以使⽤ "select xxx for update" 的⽅式在⾼并发的场景下是不可⾏的。FIFO 同步队列的⽅式，可以结合库存限制队列⻓，但是在库存较多的场景下，⼜不太适⽤。所以相对来说，我会倾向于选择：乐观锁 / 缓存锁 / 分布式锁的⽅式。

1.什么是进程?
进程是系统中正在运行的一个程序，程序一旦运行就是进程。
进程可以看成程序执行的一个实例。进程是系统资源分配的独立实体，每个进程都拥有独立的地址空间。一个进程无法访问另一个进程的变量和数据结构，如果想让一个进程访问另一个进程的资源，需要使用进程间通信，比如管道，文件，套接字等。
2.什么是线程？
是操作系统能够进行运算调度的最小单位。它被包含在进程之中，是进程中的实际运作单位。一条线程指的是进程中一个单一顺序的控制流，一个进程中可以并发多个线程，每条线程并行执行不同的任务。
3.线程的实现方式?
1.继承Thread类
2.实现Runnable接口
3.使用Callable和Future
4.Thread 类中的start() 和 run() 方法有什么区别?
1.start（）方法来启动线程，真正实现了多线程运行。这时无需等待run方法体代码执行完毕，可以直接继续执行下面的代码；通过调用Thread类的start()方法来启动一个线程， 这时此线程是处于就绪状态， 并没有运行。然后通过此Thread类调用方法run()来完成其运行操作的， 这里方法run()称为线程体，它包含了要执行的这个线程的内容， Run方法运行结束， 此线程终止。然后CPU再调度其它线程。
2.run（）方法当作普通方法的方式调用。程序还是要顺序执行，要等待run方法体执行完毕后，才可继续执行下面的代码；程序中只有主线程------这一个线程， 其程序执行路径还是只有一条， 这样就没有达到写线程的目的。
5.线程NEW状态
new创建一个Thread对象时，并没处于执行状态，因为没有调用start方法启动改线程，那么此时的状态就是新建状态。
6.线程RUNNABLE状态
线程对象通过start方法进入runnable状态，启动的线程不一定会立即得到执行，线程的运行与否要看cpu的调度，我们把这个中间状态叫可执行状态（RUNNABLE)。
7.线程的RUNNING状态
一旦cpu通过轮询货其他方式从任务可以执行队列中选中了线程，此时它才能真正的执行自己的逻辑代码。
8.线程的BLOCKED状态
线程正在等待获取锁。
进入BLOCKED状态，比如调用了sleep,或者wait方法
进行某个阻塞的io操作，比如因网络数据的读写进入BLOCKED状态
获取某个锁资源，从而加入到该锁的阻塞队列中而进入BLOCKED状态
9.线程的TERMINATED状态
TERMINATED是一个线程的最终状态，在该状态下线程不会再切换到其他任何状态了，代表整个生命周期都结束了。
下面几种情况会进入TERMINATED状态:
线程运行正常结束，结束生命周期
线程运行出错意外结束
JVM Crash 导致所有的线程都结束
10.线程状态转化图
图片

11.i--与System.out.println()的异常
示例代码:
public class XkThread extends Thread {

    private int i = 5;

    @Override
    public void run() {
       System.out.println("i=" + (i------------------) + " threadName=" + Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        XkThread xk = new XkThread();
        Thread t1 = new Thread(xk);
        Thread t2 = new Thread(xk);
        Thread t3 = new Thread(xk);
        Thread t4 = new Thread(xk);
        Thread t5 = new Thread(xk);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }
}

结果:
i=5 threadName=Thread-1
i=2 threadName=Thread-5
i=5 threadName=Thread-2
i=4 threadName=Thread-3
i=3 threadName=Thread-4

虽然println()方法在内部是同步的，但i------------------的操作却是在进入println()之前发生的，所以有发生非线程安全的概率。
println()源码:
    public void println(String x) {
        synchronized (this) {
            print(x);
            newLine();
        }
    }

12.如何知道代码段被哪个线程调用？
   System.out.println(Thread.currentThread().getName());

13.线程活动状态？
public class XKThread extends Thread {

    @Override
    public void run() {
        System.out.println("run run run is "  + this.isAlive() );
    }

    public static void main(String[] args) {
        XKThread xk = new XKThread();
        System.out.println("begin --------- " + xk.isAlive());
        xk.start();
        System.out.println("end --------------- " + xk.isAlive());

    }
}

14.sleep()方法
方法sleep()的作用是在指定的毫秒数内让当前的"正在执行的线程"休眠（暂停执行）。sleep和wait的5个区别，推荐大家看下。
15.如何优雅的设置睡眠时间?
jdk1.5 后，引入了一个枚举TimeUnit,对sleep方法提供了很好的封装。
比如要表达2小时22分55秒899毫秒。
Thread.sleep(8575899L);
TimeUnit.HOURS.sleep(3);
TimeUnit.MINUTES.sleep(22);
TimeUnit.SECONDS.sleep(55);
TimeUnit.MILLISECONDS.sleep(899);

可以看到表达的含义更清晰，更优雅。线程休眠只会用 Thread.sleep？来，教你新姿势！推荐看下。
16.停止线程
run方法执行完成，自然终止。
stop()方法，suspend()以及resume()都是过期作废方法，使用它们结果不可预期。
大多数停止一个线程的操作使用Thread.interrupt()等于说给线程打一个停止的标记, 此方法不回去终止一个正在运行的线程，需要加入一个判断才能可以完成线程的停止。
17.interrupted 和 isInterrupted
interrupted : 判断当前线程是否已经中断,会清除状态。
isInterrupted ：判断线程是否已经中断，不会清除状态。
18.yield
放弃当前cpu资源，将它让给其他的任务占用cpu执行时间。但放弃的时间不确定，有可能刚刚放弃，马上又获得cpu时间片。多线程 Thread.yield 方法到底有什么用？推荐看下。
测试代码:(cpu独占时间片)
public class XKThread extends Thread {

    @Override
    public void run() {
        long beginTime = System.currentTimeMillis();
        int count = 0;
        for (int i = 0; i < 50000000; i++) {
            count = count + (i + 1);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("用时 = " + (endTime - beginTime) + " 毫秒! ");
    }

    public static void main(String[] args) {
        XKThread xkThread = new XKThread();
        xkThread.start();
    }

}

结果：
用时 = 20 毫秒!

加入yield，再来测试。(cpu让给其他资源导致速度变慢)
public class XKThread extends Thread {

    @Override
    public void run() {
        long beginTime = System.currentTimeMillis();
        int count = 0;
        for (int i = 0; i < 50000000; i++) {
            Thread.yield();
            count = count + (i + 1);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("用时 = " + (endTime - beginTime) + " 毫秒! ");
    }

    public static void main(String[] args) {
        XKThread xkThread = new XKThread();
        xkThread.start();
    }

}

结果:
用时 = 38424 毫秒!

19.线程的优先级
在操作系统中，线程可以划分优先级，优先级较高的线程得到cpu资源比较多，也就是cpu有限执行优先级较高的线程对象中的任务，但是不能保证一定优先级高，就先执行。
Java的优先级分为1～10个等级，数字越大优先级越高，默认优先级大小为5。超出范围则抛出：java.lang.IllegalArgumentException。
20.优先级继承特性
线程的优先级具有继承性，比如a线程启动b线程，b线程与a优先级是一样的。
21.谁跑的更快？
设置优先级高低两个线程，累加数字，看谁跑的快，上代码。
public class Run extends Thread{

    public static void main(String[] args) {
        try {
            ThreadLow low = new ThreadLow();
            low.setPriority(2);
            low.start();

            ThreadHigh high = new ThreadHigh();
            high.setPriority(8);
            high.start();

            Thread.sleep(2000);
            low.stop();
            high.stop();
            System.out.println("low  = " + low.getCount());
            System.out.println("high = " + high.getCount());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}

class ThreadHigh extends Thread {
    private int count = 0;

    public int getCount() {
        return count;
    }

    @Override
    public void run() {
        while (true) {
            count++;
        }
    }
}

class ThreadLow extends Thread {
    private int count = 0;

    public int getCount() {
        return count;
    }

    @Override
    public void run() {
        while (true) {
            count++;
        }
    }
}

结果:
low  = 1193854568
high = 1204372373

22.线程种类
Java线程有两种，一种是用户线程，一种是守护线程。
23.守护线程的特点
守护线程是一个比较特殊的线程，主要被用做程序中后台调度以及支持性工作。当Java虚拟机中不存在非守护线程时，守护线程才会随着JVM一同结束工作。
24.Java中典型的守护线程
GC（垃圾回收器）
25.如何设置守护线程
Thread.setDaemon(true)
PS:Daemon属性需要再启动线程之前设置，不能再启动后设置。
25.Java虚拟机退出时Daemon线程中的finally块一定会执行？
Java虚拟机退出时Daemon线程中的finally块并不一定会执行。
代码示例:
public class XKDaemon {
    public static void main(String[] args) {
        Thread thread = new Thread(new DaemonRunner(),"xkDaemonRunner");
        thread.setDaemon(true);
        thread.start();

    }

    static class DaemonRunner implements Runnable {

        @Override
        public void run() {
            try {
                SleepUtils.sleep(10);
            } finally {
                System.out.println("Java小咖秀 daemonThread finally run ...");
            }

        }
    }
}

结果：
没有任何的输出，说明没有执行finally。

## 类加载器

## 设置线程上下文类加载器

根据【包名、class名】首先判断自己是否已经加装，然后【委托给】父加载器加装



获取线程上下文类加载器
public ClassLoader getContextClassLoader()

设置线程类加载器（可以打破Java类加载器的父类委托机制）
public void setContextClassLoader(ClassLoader cl)

27.join
join是指把指定的线程加入到当前线程，比如join某个线程a,会让当前线程b进入等待,直到a的生命周期结束，此期间b线程是处于blocked状态。
28.什么是synchronized?
synchronized关键字可以时间一个简单的策略来防止线程干扰和内存一致性错误，如果一个对象是对多个线程可见的，那么对该对想的所有读写都将通过同步的方式来进行。
29.synchronized包括哪两个jvm重要的指令？
monitor enter 和 monitor exit
30.synchronized关键字用法?
可以用于对代码块或方法的修饰，Synchronized 有几种用法？推荐看下。
31.synchronized锁的是什么?
普通同步方法 ---------------> 锁的是当前实力对象。
静态同步方法---------------> 锁的是当前类的Class对象。
同步方法快 ---------------> 锁的是synchonized括号里配置的对象。
32.Java对象头
synchronized用的锁是存在Java对象头里的。对象如果是数组类型，虚拟机用3个字宽(Word)存储对象头，如果对象是非数组类型，用2字宽存储对象头。
Tips:32位虚拟机中一个字宽等于4字节。
33.Java对象头长度
图片

34.Java对象头的存储结构
32位JVM的Mark Word 默认存储结构
图片

35.Mark Word的状态变化
Mark Word 存储的数据会随着锁标志为的变化而变化。
图片

64位虚拟机下，Mark Word是64bit大小的
图片

36.锁的升降级规则
Java SE 1.6 为了提高锁的性能。引入了"偏向锁"和轻量级锁"。
Java SE 1.6 中锁有4种状态。级别从低到高依次是：无锁状态、偏向锁状态、轻量级锁状态、重量级锁状态。
锁只能升级不能降级。
37.偏向锁
大多数情况，锁不仅不存在多线程竞争，而且总由同一线程多次获得。当一个线程访问同步块并获取锁时，会在对象头和栈帧中记录存储锁偏向的线程ID,以后该线程在进入和退出同步块时不需要进行 cas操作来加锁和解锁，只需测试一下对象头 Mark Word里是否存储着指向当前线程的偏向锁。如果测试成功，表示线程已经获得了锁，如果失败，则需要测试下Mark Word中偏向锁的标示是否已经设置成1（表示当前时偏向锁),如果没有设置，则使用cas竞争锁，如果设置了，则尝试使用cas将对象头的偏向锁只想当前线程。
38.关闭偏向锁延迟
java6和7中默认启用，但是会在程序启动几秒后才激活，如果需要关闭延迟，
-XX:BiasedLockingStartupDelay=0。
39.如何关闭偏向锁
JVM参数关闭偏向锁:-XX:-UseBiasedLocking=false,那么程序默认会进入轻量级锁状态。
Tips:如果你可以确定程序的所有锁通常情况处于竞态，则可以选择关闭。
40.轻量级锁
线程在执行同步块，jvm会现在当前线程的栈帧中创建用于储存锁记录的空间。并将对象头中的Mark Word复制到锁记录中。然后线程尝试使用cas将对象头中的Mark Word替换为之乡锁记录的指针。如果成功，当前线程获得锁，如果失败，表示其他线程竞争锁，当前线程便尝试使用自旋来获取锁。
41.轻量锁的解锁
轻量锁解锁时，会使原子操作cas将 displaced Mark Word 替换回对象头，如果成功则表示没有竞争发生，如果失败，表示存在竞争，此时锁就会膨胀为重量级锁。
42.锁的优缺点对比
图片

43.什么是原子操作
不可被中断的一个或一系列操作
44.Java如何实现原子操作
Java中通过锁和循环cas的方式来实现原子操作，JVM的CAS操作利用了处理器提供的CMPXCHG指令来实现的。自旋CAS实现的基本思路就是循环进行CAS操作直到成功为止。
另外，关注微信公众号：Java技术栈，在后台回复：java，可以获取我整理的 N 篇 Java 及多线程干货。
45.CAS实现原子操作的3大问题
ABA问题，循环时间长消耗资源大，只能保证一个共享变量的原子操作
46.什么是ABA问题
问题：
因为cas需要在操作值的时候，检查值有没有变化，如果没有变化则更新，如果一个值原来是A,变成了B,又变成了A,那么使用cas进行检测时会发现发的值没有发生变化，其实是变过的。
解决：
添加版本号，每次更新的时候追加版本号，A-B-A ---> 1A-2B-3A。
从jdk1.5开始,Atomic包提供了一个类AtomicStampedReference来解决ABA的问题。
47.CAS循环时间长占用资源大问题
如果jvm能支持处理器提供的pause指令，那么效率会有一定的提升。
一、它可以延迟流水线执行指令(de-pipeline),使cpu不会消耗过多的执行资源，延迟的时间取决于具体实现的版本，有些处理器延迟时间是0。
二、它可以避免在退出循环的时候因内存顺序冲突而引起的cpu流水线被清空，从而提高cpu执行效率。
48.CAS只能保证一个共享变量原子操作
一、对多个共享变量操作时，可以用锁。
二、可以把多个共享变量合并成一个共享变量来操作。比如,x=1,k=a,合并xk=1a，然后用cas操作xk。
Tips:java 1.5开始,jdk提供了AtomicReference类来保证饮用对象之间的原子性，就可以把多个变量放在一个对象来进行cas操作。
49.volatile关键字
volatile 是轻量级的synchronized,它在多处理器开发中保证了共享变量的"可见性"。详细看下这篇文章：volatile关键字解析。
Java语言规范第3版对volatile定义如下，Java允许线程访问共享变量，为了保证共享变量能准确和一致的更新，线程应该确保排它锁单独获得这个变量。如果一个字段被声明为volatile,Java线程内存模型所有线程看到这个变量的值是一致的。
50.等待/通知机制
一个线程修改了一个对象的值，而另一个线程感知到了变化，然后进行相应的操作。
51.wait
方法wait()的作用是使当前执行代码的线程进行等待，wait()是Object类通用的方法，该方法用来将当前线程置入"预执行队列"中，并在 wait()所在的代码处停止执行，直到接到通知或中断为止。
在调用wait之前线程需要获得该对象的对象级别的锁。代码体现上，即只能是同步方法或同步代码块内。调用wait()后当前线程释放锁。
52.notify
notify()也是Object类的通用方法，也要在同步方法或同步代码块内调用，该方法用来通知哪些可能灯光该对象的对象锁的其他线程，如果有多个线程等待，则随机挑选出其中一个呈wait状态的线程，对其发出 通知 notify，并让它等待获取该对象的对象锁。
53.notify/notifyAll
notify等于说将等待队列中的一个线程移动到同步队列中，而notifyAll是将等待队列中的所有线程全部移动到同步队列中。
54.等待/通知经典范式
等待
synchronized(obj) {
        while(条件不满足) {
                obj.wait();
        }
        执行对应逻辑
}

通知
synchronized(obj) {
      改变条件
        obj.notifyAll();
}

55.ThreadLocal
主要解决每一个线程想绑定自己的值，存放线程的私有数据。
56.ThreadLocal使用
获取当前的线程的值通过get(),设置set(T) 方式来设置值。
public class XKThreadLocal {

    public static ThreadLocal threadLocal = new ThreadLocal();

    public static void main(String[] args) {
        if (threadLocal.get() == null) {
            System.out.println("未设置过值");
            threadLocal.set("Java小咖秀");
        }
        System.out.println(threadLocal.get());
    }

}

输出:
未设置过值
Java小咖秀

Tips:默认值为null
57.解决get()返回null问题
通过继承重写initialValue()方法即可。
代码实现：
public class ThreadLocalExt extends ThreadLocal{

    static ThreadLocalExt threadLocalExt = new ThreadLocalExt();

    @Override
    protected Object initialValue() {
        return "Java小咖秀";
    }

    public static void main(String[] args) {
        System.out.println(threadLocalExt.get());
    }
}

输出结果:
Java小咖秀

58.Lock接口
锁可以防止多个线程同时共享资源。Java5前程序是靠synchronized实现锁功能。Java5之后，并发包新增Lock接口来实现锁功能。
59.Lock接口提供 synchronized不具备的主要特性
图片

60.重入锁 ReentrantLock
支持重进入的锁，它表示该锁能够支持一个线程对资源的重复加锁。除此之外，该锁的还支持获取锁时的公平和非公平性选择。
详细阅读：到底什么是重入锁，拜托，一次搞清楚！
另外，关注微信公众号：Java技术栈，在后台回复：面试，可以获取我整理的 N 篇 Java 面试题干货。
61.重进入是什么意思？
重进入是指任意线程在获取到锁之后能够再次获锁而不被锁阻塞。
该特性主要解决以下两个问题：
一、锁需要去识别获取锁的线程是否为当前占据锁的线程，如果是则再次成功获取。
二、所得最终释放。线程重复n次是获取了锁，随后在第n次释放该锁后，其他线程能够获取到该锁。
62.ReentrantLock默认锁？
默认非公平锁
推荐看下：Synchronized 与 ReentrantLock 的区别！
代码为证:
 final boolean nonfairTryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                if (compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0) // overflow
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }

63.公平锁和非公平锁的区别
公平性与否针对获取锁来说的，如果一个锁是公平的，那么锁的获取顺序就应该符合请求的绝对时间顺序，也就是FIFO。
64.读写锁
读写锁允许同一时刻多个读线程访问，但是写线程和其他写线程均被阻塞。读写锁维护一个读锁一个写锁，读写分离，并发性得到了提升。
Java中提供读写锁的实现类是ReentrantReadWriteLock。
65.LockSupport工具
定义了一组公共静态方法，提供了最基本的线程阻塞和唤醒功能。
图片
66.Condition接口
提供了类似Object监视器方法，与 Lock配合使用实现等待/通知模式。
67.Condition使用
代码示例:
public class XKCondition {
    Lock lock = new ReentrantLock();
    Condition cd = lock.newCondition();

    public void await() throws InterruptedException {
        lock.lock();
        try {
            cd.await();//相当于Object 方法中的wait()
        } finally {
            lock.unlock();
        }
    }

    public void signal() {
        lock.lock();
        try {
            cd.signal(); //相当于Object 方法中的notify()
        } finally {
            lock.unlock();
        }
    }

}

68.ArrayBlockingQueue?
一个由数据支持的有界阻塞队列，此队列FIFO原则对元素进行排序。队列头部在队列中存在的时间最长，队列尾部存在时间最短。
69.PriorityBlockingQueue?
一个支持优先级排序的无界阻塞队列，但它不会阻塞数据生产者，而只会在没有可消费的数据时，阻塞数据的消费者。
70.DelayQueue?
是一个支持延时获取元素的使用优先级队列的实现的无界阻塞队列。队列中的元素必须实现Delayed接口和 Comparable接口，在创建元素时可以指定多久才能从队列中获取当前元素。
71.Java并发容器，你知道几个？
ConcurrentHashMap、CopyOnWriteArrayList 、CopyOnWriteArraySet 、ConcurrentLinkedQueue、
ConcurrentLinkedDeque、ConcurrentSkipListMap、ConcurrentSkipListSet、ArrayBlockingQueue、
LinkedBlockingQueue、LinkedBlockingDeque、PriorityBlockingQueue、SynchronousQueue、
LinkedTransferQueue、DelayQueue
72.ConcurrentHashMap
并发安全版HashMap,java7中采用分段锁技术来提高并发效率，默认分16段。Java8放弃了分段锁，采用CAS，同时当哈希冲突时，当链表的长度到8时，会转化成红黑树。（如需了解细节，见jdk中代码）
推荐阅读：HashMap, ConcurrentHashMap 一次性讲清楚！
73.ConcurrentLinkedQueue
基于链接节点的无界线程安全队列，它采用先进先出的规则对节点进行排序，当我们添加一个元素的时候，它会添加到队列的尾部，当我们获取一个元素时，它会返回队列头部的元素。它采用cas算法来实现。（如需了解细节，见jdk中代码）
74.什么是阻塞队列？
阻塞队列是一个支持两个附加操作的队列，这两个附加操作支持阻塞的插入和移除方法。
1、支持阻塞的插入方法：当队列满时，队列会阻塞插入元素的线程，直到队列不满。
2、支持阻塞的移除方法：当队列空时，获取元素的线程会等待队列变为非空。
75.阻塞队列常用的应用场景？
常用于生产者和消费者场景，生产者是往队列里添加元素的线程，消费者是从队列里取元素的线程。阻塞队列正好是生产者存放、消费者来获取的容器。
76.Java里的阻塞的队列
ArrayBlockingQueue：数组结构组成的 |有界阻塞队列
LinkedBlockingQueue：链表结构组成的|有界阻塞队列
PriorityBlockingQueue:  支持优先级排序|无界阻塞队列
DelayQueue：优先级队列实现|无界阻塞队列
SynchronousQueue：不存储元素| 阻塞队列
LinkedTransferQueue：链表结构组成|无界阻塞队列
LinkedBlockingDeque：链表结构组成|双向阻塞队列

77.Fork/Join
java7提供的一个用于并行执行任务的框架，把一个大任务分割成若干个小任务，最终汇总每个小任务结果的后得到大任务结果的框架。
详细阅读：Java7任务并行执行神器：Fork&Join框架
78.工作窃取算法
是指某个线程从其他队列里窃取任务来执行。当大任务被分割成小任务时，有的线程可能提前完成任务，此时闲着不如去帮其他没完成工作线程。此时可以去其他队列窃取任务，为了减少竞争，通常使用双端队列，被窃取的线程从头部拿，窃取的线程从尾部拿任务执行。
79.工作窃取算法的有缺点
优点：充分利用线程进行并行计算，减少了线程间的竞争。
缺点：有些情况下还是存在竞争，比如双端队列中只有一个任务。这样就消耗了更多资源。
80.Java中原子操作更新基本类型，Atomic包提供了哪几个类?
AtomicBoolean:原子更新布尔类型
AtomicInteger:原子更新整形
AtomicLong:原子更新长整形
81.Java中原子操作更新数组，Atomic包提供了哪几个类?
AtomicIntegerArray: 原子更新整形数据里的元素
AtomicLongArray: 原子更新长整形数组里的元素
AtomicReferenceArray: 原子更新饮用类型数组里的元素
AtomicIntegerArray: 主要提供原子方式更新数组里的整形
82.Java中原子操作更新引用类型，Atomic包提供了哪几个类?
如果原子需要更新多个变量，就需要用引用类型了。
AtomicReference : 原子更新引用类型
AtomicReferenceFieldUpdater: 原子更新引用类型里的字段。
AtomicMarkableReference: 原子更新带有标记位的引用类型。标记位用boolean类型表示，构造方法时AtomicMarkableReference(V initialRef,boolean initialMark)
83.Java中原子操作更新字段类，Atomic包提供了哪几个类?
AtomiceIntegerFieldUpdater: 原子更新整形字段的更新器
AtomiceLongFieldUpdater: 原子更新长整形字段的更新器
AtomiceStampedFieldUpdater: 原子更新带有版本号的引用类型，将整数值
84.JDK并发包中提供了哪几个比较常见的处理并发的工具类？
提供并发控制手段: CountDownLatch、CyclicBarrier、Semaphore
线程间数据交换: Exchanger
85.CountDownLatch
允许一个或多个线程等待其他线程完成操作。
CountDownLatch的构造函数接受一个int类型的参数作为计数器，你想等待n个点完成，就传入n。
两个重要的方法:
countDown() : 调用时，n会减1。
await() : 调用会阻塞当前线程，直到n变成0。
await(long time,TimeUnit unit) : 等待特定时间后，就不会继续阻塞当前线程。
tips:计数器必须大于等于0，当为0时，await就不会阻塞当前线程。
不提供重新初始化或修改内部计数器的值的功能。
86.CyclicBarrier
可循环使用的屏障。
让一组线程到达一个屏障（也可以叫同步点）时被阻塞，直到最后一个线程到达屏障时，屏障才会开门，所有被屏障拦截的线程才会继续运行。
CyclicBarrier默认构造放时CyclicBarrier(int parities) ,其参数表示屏障拦截的线程数量，每个线程调用await方法告诉CyclicBarrier我已经到达屏障，然后当前线程被阻塞。
87.CountDownLatch与CyclicBarrier区别
CountDownLatch：
计数器：计数器只能使用一次。
等待：一个线程或多个等待另外n个线程完成之后才能执行。
CyclicBarrier：
计数器：计数器可以重置（通过reset()方法)。
等待：n个线程相互等待，任何一个线程完成之前，所有的线程都必须等待。
88.Semaphore
用来控制同时访问资源的线程数量，通过协调各个线程，来保证合理的公共资源的访问。
应用场景：流量控制，特别是公共资源有限的应用场景，比如数据链接，限流等。
89.Exchanger
Exchanger是一个用于线程间协作的工具类，它提供一个同步点，在这个同步点上，两个线程可以交换彼此的数据。比如第一个线程执行exchange()方法，它会一直等待第二个线程也执行exchange，当两个线程都到同步点，就可以交换数据了。
一般来说为了避免一直等待的情况，可以使用exchange(V x,long timeout,TimeUnit unit),设置最大等待时间。
Exchanger可以用于遗传算法。
90.为什么使用线程池
几乎所有需要异步或者并发执行任务的程序都可以使用线程池。合理使用会给我们带来以下好处。
降低系统消耗：重复利用已经创建的线程降低线程创建和销毁造成的资源消耗。
提高响应速度：当任务到达时，任务不需要等到线程创建就可以立即执行。
提供线程可以管理性：可以通过设置合理分配、调优、监控。
91.线程池工作流程
1、判断核心线程池里的线程是否都有在执行任务，否->创建一个新工作线程来执行任务。是->走下个流程。
2、判断工作队列是否已满，否->新任务存储在这个工作队列里，是->走下个流程。
3、判断线程池里的线程是否都在工作状态，否->创建一个新的工作线程来执行任务，
是->走下个流程。
4、按照设置的策略来处理无法执行的任务。
详细阅读：java高级应用：线程池全面解析。
92.创建线程池参数有哪些，作用？
 public ThreadPoolExecutor(   int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler)

1.corePoolSize:核心线程池大小，当提交一个任务时，线程池会创建一个线程来执行任务，即使其他空闲的核心线程能够执行新任务也会创建，等待需要执行的任务数大于线程核心大小就不会继续创建。
2.maximumPoolSize:线程池最大数，允许创建的最大线程数，如果队列满了，并且已经创建的线程数小于最大线程数，则会创建新的线程执行任务。如果是无界队列，这个参数基本没用。
3.keepAliveTime: 线程保持活动时间，线程池工作线程空闲后，保持存活的时间，所以如果任务很多，并且每个任务执行时间较短，可以调大时间，提高线程利用率。
4.unit: 线程保持活动时间单位，天（DAYS)、小时(HOURS)、分钟(MINUTES、毫秒MILLISECONDS)、微秒(MICROSECONDS)、纳秒(NANOSECONDS)
5.workQueue: 任务队列，保存等待执行的任务的阻塞队列。
一般来说可以选择如下阻塞队列：
ArrayBlockingQueue:基于数组的有界阻塞队列。
LinkedBlockingQueue:基于链表的阻塞队列。
SynchronizedQueue:一个不存储元素的阻塞队列。
PriorityBlockingQueue:一个具有优先级的阻塞队列。
6.threadFactory：设置创建线程的工厂，可以通过线程工厂给每个创建出来的线程设置更有意义的名字。
7.handler: 饱和策略也叫拒绝策略。当队列和线程池都满了，即达到饱和状态。所以需要采取策略来处理新的任务。默认策略是AbortPolicy。
AbortPolicy:直接抛出异常。

CallerRunsPolicy: 调用者所在的线程来运行任务。

DiscardOldestPolicy:丢弃队列里最近的一个任务，并执行当前任务。

DiscardPolicy:不处理，直接丢掉。

当然可以根据自己的应用场景，实现RejectedExecutionHandler接口自定义策略。
93.向线程池提交任务
可以使用execute()和submit() 两种方式提交任务。
execute():无返回值，所以无法判断任务是否被执行成功。
submit():用于提交需要有返回值的任务。线程池返回一个future类型的对象，通过这个future对象可以判断任务是否执行成功，并且可以通过future的get()来获取返回值，get()方法会阻塞当前线程知道任务完成。get(long timeout,TimeUnit unit)可以设置超市时间。
94.关闭线程池
可以通过shutdown()或shutdownNow()来关闭线程池。它们的原理是遍历线程池中的工作线程，然后逐个调用线程的interrupt来中断线程，所以无法响应终端的任务可以能永远无法停止。
shutdownNow首先将线程池状态设置成STOP,然后尝试停止所有的正在执行或者暂停的线程，并返回等待执行任务的列表。
shutdown只是将线程池的状态设置成shutdown状态，然后中断所有没有正在执行任务的线程。
只要调用两者之一，isShutdown就会返回true,当所有任务都已关闭，isTerminaed就会返回true。
一般来说调用shutdown方法来关闭线程池，如果任务不一定要执行完，可以直接调用shutdownNow方法。
95.线程池如何合理设置
配置线程池可以从以下几个方面考虑。
任务是cpu密集型、IO密集型或者混合型

任务优先级，高中低。

任务时间执行长短。

任务依赖性：是否依赖其他系统资源。

cpu密集型可以配置可能小的线程,比如 n + 1个线程。
io密集型可以配置较多的线程，如 2n个线程。
混合型可以拆成io密集型任务和cpu密集型任务，
如果两个任务执行时间相差大，否->分解后执行吞吐量将高于串行执行吞吐量。
否->没必要分解。
可以通过Runtime.getRuntime().availableProcessors()来获取cpu个数。
建议使用有界队列，增加系统的预警能力和稳定性。
96.Executor
从JDK5开始，把工作单元和执行机制分开。工作单元包括Runnable和Callable,而执行机制由Executor框架提供。
97.Executor框架的主要成员
ThreadPoolExecutor :可以通过工厂类Executors来创建。
可以创建3种类型的ThreadPoolExecutor：SingleThreadExecutor、FixedThreadPool、CachedThreadPool。
ScheduledThreadPoolExecutor ：可以通过工厂类Executors来创建。
可以创建2中类型的ScheduledThreadPoolExecutor：ScheduledThreadPoolExecutor、SingleThreadScheduledExecutor
Future接口:Future和实现Future接口的FutureTask类来表示异步计算的结果。
Runnable和Callable:它们的接口实现类都可以被ThreadPoolExecutor或ScheduledThreadPoolExecutor执行。Runnable不能返回结果，Callable可以返回结果。
98.FixedThreadPool
可重用固定线程数的线程池。
查看源码：
public static ExecutorService newFixedThreadPool(int nThreads) {
   return new ThreadPoolExecutor(nThreads, nThreads,
                                 0L, TimeUnit.MILLISECONDS,
                                new LinkedBlockingQueue<Runnable>());}

corePoolSize 和maxPoolSize都被设置成我们设置的nThreads。
当线程池中的线程数大于corePoolSize ,keepAliveTime为多余的空闲线程等待新任务的最长时间，超过这个时间后多余的线程将被终止，如果设为0，表示多余的空闲线程会立即终止。
工作流程：
1.当前线程少于corePoolSize,创建新线程执行任务。
2.当前运行线程等于corePoolSize,将任务加入LinkedBlockingQueue。
3.线程执行完1中的任务，会循环反复从LinkedBlockingQueue获取任务来执行。
LinkedBlockingQueue作为线程池工作队列（默认容量Integer.MAX_VALUE)。因此可能会造成如下赢下。
1.当线程数等于corePoolSize时，新任务将在队列中等待，因为线程池中的线程不会超过corePoolSize。
2.maxnumPoolSize等于说是一个无效参数。
3.keepAliveTime等于说也是一个无效参数。
4.运行中的FixedThreadPool(未执行shundown或shundownNow))则不会调用拒绝策略。
5.由于任务可以不停的加到队列，当任务越来越多时很容易造成OOM。
99.CachedThreadPool
根据需要创建新线程的线程池。
查看源码：
    public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());

corePoolSize设置为0，maxmumPoolSize为Integer.MAX_VALUE。keepAliveTime为60秒。
工作流程：
1.首先执行SynchronousQueue.offer (Runnable task)。如果当前maximumPool 中有空闲线程正在执行S ynchronousQueue.poll(keepAliveTIme,TimeUnit.NANOSECONDS)，那么主线程执行offer操作与空闲线程执行的poll操作配对成功，主线程把任务交给空闲线程执行,execute方 法执行完成;否则执行下面的步骤2。
2.当初始maximumPool为空或者maximumPool中当前没有空闲线程时，将没有线程执行 SynchronousQueue.poll (keepAliveTime，TimeUnit.NANOSECONDS)。这种情况下，步骤 1将失 败。此时CachedThreadPool会创建一个新线程执行任务，execute()方法执行完成。
3.在步骤2中新创建的线程将任务执行完后，会执行SynchronousQueue.poll (keepAliveTime，TimeUnit.NANOSECONDS)。这个poll操作会让空闲线程最多在SynchronousQueue中等待60秒钟。如果60秒钟内主线程提交了一个新任务(主线程执行步骤1)，那么这个空闲线程将执行主线程提交的新任务;否则，这个空闲线程将终止。由于空闲60秒的空闲线程会被终止,因此长时间保持空闲的CachedThreadPool不会使用任何资源。
一般来说它适合处理时间短、大量的任务。





1、多线程有什么用？

一个可能在很多人看来很扯淡的一个问题：我会用多线程就好了，还管它有什么用？在我看来，这个回答更扯淡。所谓"知其然知其所以然"，"会用"只是"知其然"，"为什么用"才是"知其所以然"，只有达到"知其然知其所以然"的程度才可以说是把一个知识点运用自如。OK，下面说说我对这个问题的看法：

（1）发挥多核CPU的优势

随着工业的进步，现在的笔记本、台式机乃至商用的应用服务器至少也都是双核的，4核、8核甚至16核的也都不少见，如果是单线程的程序，那么在双核CPU上就浪费了50%，在4核CPU上就浪费了75%。单核CPU上所谓的"多线程"那是假的多线程，同一时间处理器只会处理一段逻辑，只不过线程之间切换得比较快，看着像多个线程"同时"运行罢了。多核CPU上的多线程才是真正的多线程，它能让你的多段逻辑同时工作，多线程，可以真正发挥出多核CPU的优势来，达到充分利用CPU的目的。

（2）防止阻塞

从程序运行效率的角度来看，单核CPU不但不会发挥出多线程的优势，反而会因为在单核CPU上运行多线程导致线程上下文的切换，而降低程序整体的效率。但是单核CPU我们还是要应用多线程，就是为了防止阻塞。试想，如果单核CPU使用单线程，那么只要这个线程阻塞了，比方说远程读取某个数据吧，对端迟迟未返回又没有设置超时时间，那么你的整个程序在数据返回回来之前就停止运行了。多线程可以防止这个问题，多条线程同时运行，哪怕一条线程的代码执行读取数据阻塞，也不会影响其它任务的执行。

（3）便于建模

这是另外一个没有这么明显的优点了。假设有一个大的任务A，单线程编程，那么就要考虑很多，建立整个程序模型比较麻烦。但是如果把这个大的任务A分解成几个小任务，任务B、任务C、任务D，分别建立程序模型，并通过多线程分别运行这几个任务，那就简单很多了。

2、创建线程的方式

比较常见的一个问题了，一般就是两种：

（1）继承Thread类
（2）实现Runnable接口

至于哪个好，不用说肯定是后者好，因为实现接口的方式比继承类的方式更灵活，也能减少程序之间的耦合度，面向接口编程也是设计模式6大原则的核心。

3、start()方法和run()方法的区别

只有调用了start()方法，才会表现出多线程的特性，不同线程的run()方法里面的代码交替执行。如果只是调用run()方法，那么代码还是同步执行的，必须等待一个线程的run()方法里面的代码全部执行完毕之后，另外一个线程才可以执行其run()方法里面的代码。

4、Runnable接口和Callable接口的区别

有点深的问题了，也看出一个Java程序员学习知识的广度。

Runnable接口中的run()方法的返回值是void，它做的事情只是纯粹地去执行run()方法中的代码而已；Callable接口中的call()方法是有返回值的，是一个泛型，和Future、FutureTask配合可以用来获取异步执行的结果。

这其实是很有用的一个特性，因为多线程相比单线程更难、更复杂的一个重要原因就是因为多线程充满着未知性，某条线程是否执行了？某条线程执行了多久？某条线程执行的时候我们期望的数据是否已经赋值完毕？无法得知，我们能做的只是等待这条多线程的任务执行完毕而已。而Callable+Future/FutureTask却可以获取多线程运行的结果，可以在等待时间太长没获取到需要的数据的情况下取消该线程的任务，真的是非常有用。

5、CyclicBarrier和CountDownLatch的区别

两个看上去有点像的类，都在java.util.concurrent下，都可以用来表示代码运行到某个点上，二者的区别在于：

（1）CyclicBarrier的某个线程运行到某个点上之后，该线程即停止运行，直到所有的线程都到达了这个点，所有线程才重新运行；CountDownLatch则不是，某线程运行到某个点上之后，只是给某个数值-1而已，该线程继续运行

（2）CyclicBarrier只能唤起一个任务，CountDownLatch可以唤起多个任务

（3）CyclicBarrier可重用，CountDownLatch不可重用，计数值为0该CountDownLatch就不可再用了

6、volatile关键字的作用

一个非常重要的问题，是每个学习、应用多线程的Java程序员都必须掌握的。理解volatile关键字的作用的前提是要理解Java内存模型，这里就不讲Java内存模型了，可以参见第31点，volatile关键字的作用主要有两个：

（1）多线程主要围绕可见性和原子性两个特性而展开，使用volatile关键字修饰的变量，保证了其在多线程之间的可见性，即每次读取到volatile变量，一定是最新的数据

（2）代码底层执行不像我们看到的高级语言----Java程序这么简单，它的执行是Java代码-->字节码-->根据字节码执行对应的C/C++代码-->C/C++代码被编译成汇编语言-->和硬件电路交互，现实中，为了获取更好的性能JVM可能会对指令进行重排序，多线程下可能会出现一些意想不到的问题。使用volatile则会对禁止语义重排序，当然这也一定程度上降低了代码执行效率

从实践角度而言，volatile的一个重要作用就是和CAS结合，保证了原子性，详细的可以参见java.util.concurrent.atomic包下的类，比如AtomicInteger。

7、什么是线程安全

又是一个理论的问题，各式各样的答案有很多，我给出一个个人认为解释地最好的：如果你的代码在多线程下执行和在单线程下执行永远都能获得一样的结果，那么你的代码就是线程安全的。

这个问题有值得一提的地方，就是线程安全也是有几个级别的：

（1）不可变
像String、Integer、Long这些，都是final类型的类，任何一个线程都改变不了它们的值，要改变除非新创建一个，因此这些不可变对象不需要任何同步手段就可以直接在多线程环境下使用

（2）绝对线程安全
不管运行时环境如何，调用者都不需要额外的同步措施。要做到这一点通常需要付出许多额外的代价，Java中标注自己是线程安全的类，实际上绝大多数都不是线程安全的，不过绝对线程安全的类，Java中也有，比方说CopyOnWriteArrayList、CopyOnWriteArraySet

（3）相对线程安全
相对线程安全也就是我们通常意义上所说的线程安全，像Vector这种，add、remove方法都是原子操作，不会被打断，但也仅限于此，如果有个线程在遍历某个Vector、有个线程同时在add这个Vector，99%的情况下都会出现ConcurrentModificationException，也就是fail-fast机制。

（4）线程非安全
这个就没什么好说的了，ArrayList、LinkedList、HashMap等都是线程非安全的类

8、Java中如何获取到线程dump文件

死循环、死锁、阻塞、页面打开慢等问题，打线程dump是最好的解决问题的途径。所谓线程dump也就是线程堆栈，获取到线程堆栈有两步：

（1）获取到线程的pid，可以通过使用jps命令，在Linux环境下还可以使用ps -ef | grep java

（2）打印线程堆栈，可以通过使用jstack pid命令，在Linux环境下还可以使用kill -3 pid

另外提一点，Thread类提供了一个getStackTrace()方法也可以用于获取线程堆栈。这是一个实例方法，因此此方法是和具体线程实例绑定的，每次获取获取到的是具体某个线程当前运行的堆栈，

9、一个线程如果出现了运行时异常会怎么样

如果这个异常没有被捕获的话，这个线程就停止执行了。另外重要的一点是：如果这个线程持有某个某个对象的监视器，那么这个对象监视器会被立即释放

10、如何在两个线程之间共享数据

通过在线程之间共享对象就可以了，然后通过wait/notify/notifyAll、await/signal/signalAll进行唤起和等待，比方说阻塞队列BlockingQueue就是为线程之间共享数据而设计的

11、sleep方法和wait方法有什么区别 

这个问题常问，sleep方法和wait方法都可以用来放弃CPU一定的时间，不同点在于如果线程持有某个对象的监视器，sleep方法不会放弃这个对象的监视器，wait方法会放弃这个对象的监视器

12、生产者消费者模型的作用是什么

这个问题很理论，但是很重要：

（1）通过平衡生产者的生产能力和消费者的消费能力来提升整个系统的运行效率，这是生产者消费者模型最重要的作用

（2）解耦，这是生产者消费者模型附带的作用，解耦意味着生产者和消费者之间的联系少，联系越少越可以独自发展而不需要收到相互的制约

13、ThreadLocal有什么用

简单说ThreadLocal就是一种以空间换时间的做法，在每个Thread里面维护了一个以开地址法实现的ThreadLocal.ThreadLocalMap，把数据进行隔离，数据不共享，自然就没有线程安全方面的问题了

14、为什么wait()方法和notify()/notifyAll()方法要在同步块中被调用

这是JDK强制的，wait()方法和notify()/notifyAll()方法在调用前都必须先获得对象的锁

15、wait()方法和notify()/notifyAll()方法在放弃对象监视器时有什么区别

wait()方法和notify()/notifyAll()方法在放弃对象监视器的时候的区别在于：
wait()方法立即释放对象监视器，notify()/notifyAll()方法则会等待线程剩余代码执行完毕才会放弃对象监视器。

16、为什么要使用线程池

避免频繁地创建和销毁线程，达到线程对象的重用。另外，使用线程池还可以根据项目灵活地控制并发的数目。

17、怎么检测一个线程是否持有对象监视器

我也是在网上看到一道多线程面试题才知道有方法可以判断某个线程是否持有对象监视器：Thread类提供了一个holdsLock(Object obj)方法，当且仅当对象obj的监视器被某条线程持有的时候才会返回true，注意这是一个static方法，这意味着"某条线程"指的是当前线程。

18、synchronized和ReentrantLock的区别

synchronized是和if、else、for、while一样的关键字，ReentrantLock是类，这是二者的本质区别。既然ReentrantLock是类，那么它就提供了比
synchronized更多更灵活的特性，可以被继承、可以有方法、可以有各种各样的类变量，ReentrantLock比synchronized的扩展性体现在几点上：

（1）ReentrantLock可以对获取锁的等待时间进行设置，这样就避免了死锁
（2）ReentrantLock可以获取各种锁的信息
（3）ReentrantLock可以灵活地实现多路通知

另外，二者的锁机制其实也是不一样的。ReentrantLock底层调用的是Unsafe的park方法加锁，synchronized操作的应该是对象头中mark word，这点我不能确定。

19、ConcurrentHashMap的并发度是什么

ConcurrentHashMap的并发度就是segment的大小，默认为16，这意味着最多同时可以有16条线程操作ConcurrentHashMap，这也是ConcurrentHashMap对Hashtable的最大优势，任何情况下，Hashtable能同时有两条线程获取Hashtable中的数据吗？

20、ReadWriteLock是什么

首先明确一下，不是说ReentrantLock不好，只是ReentrantLock某些时候有局限。如果使用ReentrantLock，可能本身是为了防止线程A在写数据、线程B在读数据造成的数据不一致，但这样，如果线程C在读数据、线程D也在读数据，读数据是不会改变数据的，没有必要加锁，但是还是加锁了，降低了程序的性能。

因为这个，才诞生了读写锁ReadWriteLock。ReadWriteLock是一个读写锁接口，ReentrantReadWriteLock是ReadWriteLock接口的一个具体实现，实现了读写的分离，读锁是共享的，写锁是独占的，读和读之间不会互斥，读和写、写和读、写和写之间才会互斥，提升了读写的性能。

21、FutureTask是什么

这个其实前面有提到过，FutureTask表示一个异步运算的任务。FutureTask里面可以传入一个Callable的具体实现类，可以对这个异步运算的任务的结果进行等待获取、判断是否已经完成、取消任务等操作。当然，由于FutureTask也是Runnable接口的实现类，所以FutureTask也可以放入线程池中。

22、Linux环境下如何查找哪个线程使用CPU最长

这是一个比较偏实践的问题，这种问题我觉得挺有意义的。可以这么做：

（1）获取项目的pid，jps或者ps -ef | grep java，这个前面有讲过
（2）top -H -p pid，顺序不能改变

这样就可以打印出当前的项目，每条线程占用CPU时间的百分比。注意这里打出的是LWP，也就是操作系统原生线程的线程号，我笔记本山没有部署Linux环境下的Java工程，因此没有办法截图演示，网友朋友们如果公司是使用Linux环境部署项目的话，可以尝试一下。

使用"top -H -p pid"+"jps pid"可以很容易地找到某条占用CPU高的线程的线程堆栈，从而定位占用CPU高的原因，一般是因为不当的代码操作导致了死循环。
最后提一点，"top -H -p pid"打出来的LWP是十进制的，"jps pid"打出来的本地线程号是十六进制的，转换一下，就能定位到占用CPU高的线程的当前线程堆栈了。

23、Java编程写一个会导致死锁的程序

第一次看到这个题目，觉得这是一个非常好的问题。很多人都知道死锁是怎么一回事儿：线程A和线程B相互等待对方持有的锁导致程序无限死循环下去。当然也仅限于此了，问一下怎么写一个死锁的程序就不知道了，这种情况说白了就是不懂什么是死锁，懂一个理论就完事儿了，实践中碰到死锁的问题基本上是看不出来的。

真正理解什么是死锁，这个问题其实不难，几个步骤：

（1）两个线程里面分别持有两个Object对象：lock1和lock2。这两个lock作为同步代码块的锁；

（2）线程1的run()方法中同步代码块先获取lock1的对象锁，Thread.sleep(xxx)，时间不需要太多，50毫秒差不多了，然后接着获取lock2的对象锁。这么做主要是为了防止线程1启动一下子就连续获得了lock1和lock2两个对象的对象锁

（3）线程2的run)(方法中同步代码块先获取lock2的对象锁，接着获取lock1的对象锁，当然这时lock1的对象锁已经被线程1锁持有，线程2肯定是要等待线程1释放lock1的对象锁的

这样，线程1"睡觉"睡完，线程2已经获取了lock2的对象锁了，线程1此时尝试获取lock2的对象锁，便被阻塞，此时一个死锁就形成了。代码就不写了，占的篇幅有点多，Java多线程7：死锁这篇文章里面有，就是上面步骤的代码实现。

24、怎么唤醒一个阻塞的线程

如果线程是因为调用了wait()、sleep()或者join()方法而导致的阻塞，可以中断线程，并且通过抛出InterruptedException来唤醒它；如果线程遇到了IO阻塞，无能为力，因为IO是操作系统实现的，Java代码并没有办法直接接触到操作系统。

25、不可变对象对多线程有什么帮助

前面有提到过的一个问题，不可变对象保证了对象的内存可见性，对不可变对象的读取不需要进行额外的同步手段，提升了代码执行效率。

26、什么是多线程的上下文切换

多线程的上下文切换是指CPU控制权由一个已经正在运行的线程切换到另外一个就绪并等待获取CPU执行权的线程的过程。

27、如果你提交任务时，线程池队列已满，这时会发生什么

这里区分一下：

如果使用的是无界队列LinkedBlockingQueue，也就是无界队列的话，没关系，继续添加任务到阻塞队列中等待执行，因为LinkedBlockingQueue可以近乎认为是一个无穷大的队列，可以无限存放任务



如果使用的是有界队列比如ArrayBlockingQueue，任务首先会被添加到ArrayBlockingQueue中，ArrayBlockingQueue满了，会根据maximumPoolSize的值增加线程数量，如果增加了线程数量还是处理不过来，ArrayBlockingQueue继续满，那么则会使用拒绝策略RejectedExecutionHandler处理满了的任务，默认是AbortPolicy


28、Java中用到的线程调度算法是什么

抢占式。一个线程用完CPU之后，操作系统会根据线程优先级、线程饥饿情况等数据算出一个总的优先级并分配下一个时间片给某个线程执行。

29、Thread.sleep(0)的作用是什么

这个问题和上面那个问题是相关的，我就连在一起了。由于Java采用抢占式的线程调度算法，因此可能会出现某条线程常常获取到CPU控制权的情况，为了让某些优先级比较低的线程也能获取到CPU控制权，可以使用Thread.sleep(0)手动触发一次操作系统分配时间片的操作，这也是平衡CPU控制权的一种操作。

30、什么是自旋

很多synchronized里面的代码只是一些很简单的代码，执行时间非常快，此时等待的线程都加锁可能是一种不太值得的操作，因为线程阻塞涉及到用户态和内核态切换的问题。既然synchronized里面的代码执行得非常快，不妨让等待锁的线程不要被阻塞，而是在synchronized的边界做忙循环，这就是自旋。如果做了多次忙循环发现还没有获得锁，再阻塞，这样可能是一种更好的策略。

31、什么是Java内存模型

Java内存模型定义了一种多线程访问Java内存的规范。Java内存模型要完整讲不是这里几句话能说清楚的，我简单总结一下Java内存模型的几部分内容：

（1）Java内存模型将内存分为了主内存和工作内存。类的状态，也就是类之间共享的变量，是存储在主内存中的，每次Java线程用到这些主内存中的变量的时候，会读一次主内存中的变量，并让这些内存在自己的工作内存中有一份拷贝，运行自己线程代码的时候，用到这些变量，操作的都是自己工作内存中的那一份。在线程代码执行完毕之后，会将最新的值更新到主内存中去

（2）定义了几个原子操作，用于操作主内存和工作内存中的变量

（3）定义了volatile变量的使用规则

（4）happens-before，即先行发生原则，定义了操作A必然先行发生于操作B的一些规则，比如在同一个线程内控制流前面的代码一定先行发生于控制流后面的代码、一个释放锁unlock的动作一定先行发生于后面对于同一个锁进行锁定lock的动作等等，只要符合这些规则，则不需要额外做同步措施，如果某段代码不符合所有的happens-before规则，则这段代码一定是线程非安全的

32、什么是CAS

CAS，全称为Compare and Swap，即比较-替换。假设有三个操作数：内存值V、旧的预期值A、要修改的值B，当且仅当预期值A和内存值V相同时，才会将内存值修改为B并返回true，否则什么都不做并返回false。当然CAS一定要volatile变量配合，这样才能保证每次拿到的变量是主内存中最新的那个值，否则旧的预期值A对某条线程来说，永远是一个不会变的值A，只要某次CAS操作失败，永远都不可能成功。

33、什么是乐观锁和悲观锁

（1）乐观锁：就像它的名字一样，对于并发间操作产生的线程安全问题持乐观状态，乐观锁认为竞争不总是会发生，因此它不需要持有锁，将比较-替换这两个动作作为一个原子操作尝试去修改内存中的变量，如果失败则表示发生冲突，那么就应该有相应的重试逻辑。

（2）悲观锁：还是像它的名字一样，对于并发间操作产生的线程安全问题持悲观状态，悲观锁认为竞争总是会发生，因此每次对某资源进行操作时，都会持有一个独占的锁，就像synchronized，不管三七二十一，直接上了锁就操作资源了。

34、什么是AQS

简单说一下AQS，AQS全称为AbstractQueuedSychronizer，翻译过来应该是抽象队列同步器。

如果说java.util.concurrent的基础是CAS的话，那么AQS就是整个Java并发包的核心了，ReentrantLock、CountDownLatch、Semaphore等等都用到了它。AQS实际上以双向队列的形式连接所有的Entry，比方说ReentrantLock，所有等待的线程都被放在一个Entry中并连成双向队列，前面一个线程使用ReentrantLock好了，则双向队列实际上的第一个Entry开始运行。

AQS定义了对双向队列所有的操作，而只开放了tryLock和tryRelease方法给开发者使用，开发者可以根据自己的实现重写tryLock和tryRelease方法，以实现自己的并发功能。

35、单例模式的线程安全性

老生常谈的问题了，首先要说的是单例模式的线程安全意味着：某个类的实例在多线程环境下只会被创建一次出来。单例模式有很多种的写法，我总结一下：

（1）饿汉式单例模式的写法：线程安全
（2）懒汉式单例模式的写法：非线程安全
（3）双检锁单例模式的写法：线程安全

36、Semaphore有什么作用

Semaphore就是一个信号量，它的作用是限制某段代码块的并发数。

Semaphore有一个构造函数，可以传入一个int型整数n，表示某段代码最多只有n个线程可以访问，如果超出了n，那么请等待，等到某个线程执行完毕这段代码块，下一个线程再进入。由此可以看出如果Semaphore构造函数中传入的int型整数n=1，相当于变成了一个synchronized了。

37、Hashtable的size()方法中明明只有一条语句"return count"，为什么还要做同步？

这是我之前的一个困惑，不知道大家有没有想过这个问题。某个方法中如果有多条语句，并且都在操作同一个类变量，那么在多线程环境下不加锁，势必会引发线程安全问题，这很好理解，但是size()方法明明只有一条语句，为什么还要加锁？

关于这个问题，在慢慢地工作、学习中，有了理解，主要原因有两点：

（1）同一时间只能有一条线程执行固定类的同步方法，但是对于类的非同步方法，可以多条线程同时访问。所以，这样就有问题了，可能线程A在执行Hashtable的put方法添加数据，线程B则可以正常调用size()方法读取Hashtable中当前元素的个数，那读取到的值可能不是最新的，可能线程A添加了完了数据，但是没有对size++，线程B就已经读取size了，那么对于线程B来说读取到的size一定是不准确的。而给size()方法加了同步之后，意味着线程B调用size()方法只有在线程A调用put方法完毕之后才可以调用，这样就保证了线程安全性

（2）CPU执行代码，执行的不是Java代码，这点很关键，一定得记住。Java代码最终是被翻译成机器码执行的，机器码才是真正可以和硬件电路交互的代码。即使你看到Java代码只有一行，甚至你看到Java代码编译之后生成的字节码也只有一行，也不意味着对于底层来说这句语句的操作只有一个。一句"return count"假设被翻译成了三句汇编语句执行，一句汇编语句和其机器码做对应，完全可能执行完第一句，线程就切换了。

38、线程类的构造方法、静态块是被哪个线程调用的

这是一个非常刁钻和狡猾的问题。请记住：线程类的构造方法、静态块是被new这个线程类所在的线程所调用的，而run方法里面的代码才是被线程自身所调用的。

如果说上面的说法让你感到困惑，那么我举个例子，假设Thread2中new了Thread1，main函数中new了Thread2，那么：

（1）Thread2的构造方法、静态块是main线程调用的，Thread2的run()方法是Thread2自己调用的

（2）Thread1的构造方法、静态块是Thread2调用的，Thread1的run()方法是Thread1自己调用的

39、同步方法和同步块，哪个是更好的选择

同步块，这意味着同步块之外的代码是异步执行的，这比同步整个方法更提升代码的效率。请知道一条原则：同步的范围越小越好。

借着这一条，我额外提一点，虽说同步的范围越少越好，但是在Java虚拟机中还是存在着一种叫做锁粗化的优化方法，这种方法就是把同步范围变大。这是有用的，比方说StringBuffer，它是一个线程安全的类，自然最常用的append()方法是一个同步方法，我们写代码的时候会反复append字符串，这意味着要进行反复的加锁->解锁，这对性能不利，因为这意味着Java虚拟机在这条线程上要反复地在内核态和用户态之间进行切换，因此Java虚拟机会将多次append方法调用的代码进行一个锁粗化的操作，将多次的append的操作扩展到append方法的头尾，变成一个大的同步块，这样就减少了加锁-->解锁的次数，有效地提升了代码执行的效率。

40、高并发、任务执行时间短的业务怎样使用线程池？并发不高、任务执行时间长的业务怎样使用线程池？并发高、业务执行时间长的业务怎样使用线程池？

这是我在并发编程网上看到的一个问题，把这个问题放在最后一个，希望每个人都能看到并且思考一下，因为这个问题非常好、非常实际、非常专业。关于这个问题，个人看法是：

（1）高并发、任务执行时间短的业务，线程池线程数可以设置为CPU核数+1，减少线程上下文的切换

（2）并发不高、任务执行时间长的业务要区分开看：

　　a）假如是业务时间长集中在IO操作上，也就是IO密集型的任务，因为IO操作并不占用CPU，所以不要让所有的CPU闲下来，可以加大线程池中的线程数目，让CPU处理更多的业务

　　b）假如是业务时间长集中在计算操作上，也就是计算密集型任务，这个就没办法了，和（1）一样吧，线程池中的线程数设置得少一些，减少线程上下文的切换

（3）并发高、业务执行时间长，解决这种类型任务的关键不在于线程池而在于整体架构的设计，看看这些业务里面某些数据是否能做缓存是第一步，增加服务器是第二步，至于线程池的设置，设置参考（2）。

最后，业务执行时间长的问题，也可能需要分析一下，看看能不能使用中间件对任务进行拆分和解耦。

41、为什么使用Executor框架？


每次执行任务创建线程 new Thread()比较消耗性能，创建一个线程是比较耗时、耗资源的。

调用 new Thread()创建的线程缺乏管理，被称为野线程，而且可以无限制的创建，线程之间的相互竞争会导致过多占用系统资源而导致系统瘫痪，还有线程之间的频繁交替也会消耗很多系统资源。

接使用new Thread() 启动的线程不利于扩展，比如定时执行、定期执行、定时定期执行、线程中断等都不便实现。

42、在Java中Executor和Executors的区别？


Executors 工具类的不同方法按照我们的需求创建了不同的线程池，来满足业务的需求。

Executor 接口对象能执行我们的线程任务。ExecutorService接口继承了Executor接口并进行了扩展，提供了更多的方法我们能获得任务执行的状态并且可以获取任务的返回值。

使用ThreadPoolExecutor 可以创建自定义线程池。Future 表示异步计算的结果，他提供了检查计算是否完成的方法，以等待计算的完成，并可以使用get()方法获取计算的结果。

43、什么是原子操作？在Java Concurrency API中有哪些原子类(atomic classes)？


原子操作（atomic operation）意为”不可被中断的一个或一系列操作” 。处理器使用基于对缓存加锁或总线加锁的方式来实现多处理器之间的原子操作。

在Java中可以通过锁和循环CAS的方式来实现原子操作。CAS操作——Compare & Set，或是 Compare & Swap，现在几乎所有的CPU指令都支持CAS的原子操作。

原子操作是指一个不受其他操作影响的操作任务单元。原子操作是在多线程环境下避免数据不一致必须的手段。

int++并不是一个原子操作，所以当一个线程读取它的值并加1时，另外一个线程有可能会读到之前的值，这就会引发错误。

为了解决这个问题，必须保证增加操作是原子的，在JDK1.5之前我们可以使用同步技术来做到这一点。到JDK1.5，java.util.concurrent.atomic包提供了int和long类型的原子包装类，它们可以自动的保证对于他们的操作是原子的并且不需要使用同步。

java.util.concurrent这个包里面提供了一组原子类。其基本的特性就是在多线程环境下，当有多个线程同时执行这些类的实例包含的方法时，具有排他性。

即当某个线程进入方法，执行其中的指令时，不会被其他线程打断，而别的线程就像自旋锁一样，一直等到该方法执行完成，才由JVM从等待队列中选择一个另一个线程进入，这只是一种逻辑上的理解。

原子类：AtomicBoolean，AtomicInteger，AtomicLong，AtomicReference
原子数组：AtomicIntegerArray，AtomicLongArray，AtomicReferenceArray
原子属性更新器：AtomicLongFieldUpdater，AtomicIntegerFieldUpdater，AtomicReferenceFieldUpdater

解决ABA问题的原子类：AtomicMarkableReference（通过引入一个boolean来反映中间有没有变过），AtomicStampedReference（通过引入一个int来累加来反映中间有没有变过）

44、Java Concurrency API中的Lock接口(Lock interface)是什么？对比同步它有什么优势？


Lock接口比同步方法和同步块提供了更具扩展性的锁操作。他们允许更灵活的结构，可以具有完全不同的性质，并且可以支持多个相关类的条件对象。

它的优势有：

可以使锁更公平
可以让线程尝试获取锁，并在无法获取锁的时候立即返回或者等待一段时间
可以在不同的范围，以不同的顺序获取和释放锁

整体上来说Lock是synchronized的扩展版，Lock提供了无条件的、可轮询的(tryLock方法)、定时的(tryLock带参方法)、可中断的(lockInterruptibly)、可多条件队列的(newCondition方法)锁操作。

另外Lock的实现类基本都支持非公平锁(默认)和公平锁，synchronized只支持非公平锁，当然，在大部分情况下，非公平锁是高效的选择。

45、什么是Executors框架？


Executor框架是一个根据一组执行策略调用，调度，执行和控制的异步任务的框架。

无限制的创建线程会引起应用程序内存溢出。所以创建一个线程池是个更好的的解决方案，因为可以限制线程的数量并且可以回收再利用这些线程。利用Executors框架可以非常方便的创建一个线程池。

46、什么是阻塞队列？阻塞队列的实现原理是什么？如何使用阻塞队列来实现生产者-消费者模型？


阻塞队列（BlockingQueue）是一个支持两个附加操作的队列。

这两个附加的操作是：在队列为空时，获取元素的线程会等待队列变为非空。当队列满时，存储元素的线程会等待队列可用。

阻塞队列常用于生产者和消费者的场景，生产者是往队列里添加元素的线程，消费者是从队列里拿元素的线程。阻塞队列就是生产者存放元素的容器，而消费者也只从容器里拿元素。

JDK7提供了7个阻塞队列。分别是：

ArrayBlockingQueue ：一个由数组结构组成的有界阻塞队列。
LinkedBlockingQueue ：一个由链表结构组成的有界阻塞队列。
PriorityBlockingQueue ：一个支持优先级排序的无界阻塞队列。
DelayQueue：一个使用优先级队列实现的无界阻塞队列。
SynchronousQueue：一个不存储元素的阻塞队列。
LinkedTransferQueue：一个由链表结构组成的无界阻塞队列。
LinkedBlockingDeque：一个由链表结构组成的双向阻塞队列。

Java 5之前实现同步存取时，可以使用普通的一个集合，然后在使用线程的协作和线程同步可以实现生产者，消费者模式，主要的技术就是用好，wait ,notify,notifyAll,sychronized这些关键字。而在java 5之后，可以使用阻塞队列来实现，此方式大大简少了代码量，使得多线程编程更加容易，安全方面也有保障。

BlockingQueue接口是Queue的子接口，它的主要用途并不是作为容器，而是作为线程同步的的工具，因此他具有一个很明显的特性，当生产者线程试图向BlockingQueue放入元素时，如果队列已满，则线程被阻塞，当消费者线程试图从中取出一个元素时，如果队列为空，则该线程会被阻塞，正是因为它所具有这个特性，所以在程序中多个线程交替向BlockingQueue中放入元素，取出元素，它可以很好的控制线程之间的通信。

阻塞队列使用最经典的场景就是socket客户端数据的读取和解析，读取数据的线程不断将数据放入队列，然后解析线程不断从队列取数据解析。

47、什么是Callable和Future?


Callable接口类似于Runnable，从名字就可以看出来了，但是Runnable不会返回结果，并且无法抛出返回结果的异常，而Callable功能更强大一些，被线程执行后，可以返回值，这个返回值可以被Future拿到，也就是说，Future可以拿到异步执行任务的返回值。可以认为是带有回调的Runnable。

Future接口表示异步任务，是还没有完成的任务给出的未来结果。所以说Callable用于产生结果，Future用于获取结果。

48、什么是FutureTask?使用ExecutorService启动任务。


在Java并发程序中FutureTask表示一个可以取消的异步运算。它有启动和取消运算、查询运算是否完成和取回运算结果等方法。只有当运算完成的时候结果才能取回，如果运算尚未完成get方法将会阻塞。

一个FutureTask对象可以对调用了Callable和Runnable的对象进行包装，由于FutureTask也是调用了Runnable接口所以它可以提交给Executor来执行。

49、什么是并发容器的实现？


何为同步容器：可以简单地理解为通过synchronized来实现同步的容器，如果有多个线程调用同步容器的方法，它们将会串行执行。比如Vector，Hashtable，以及Collections.synchronizedSet，synchronizedList等方法返回的容器。

可以通过查看Vector，Hashtable等这些同步容器的实现代码，可以看到这些容器实现线程安全的方式就是将它们的状态封装起来，并在需要同步的方法上加上关键字synchronized。

并发容器使用了与同步容器完全不同的加锁策略来提供更高的并发性和伸缩性，例如在ConcurrentHashMap中采用了一种粒度更细的加锁机制，可以称为分段锁，在这种锁机制下，允许任意数量的读线程并发地访问map，并且执行读操作的线程和写操作的线程也可以并发的访问map，同时允许一定数量的写操作线程并发地修改map，所以它可以在并发环境下实现更高的吞吐量。

50、多线程同步和互斥有几种实现方法，都是什么？


线程同步是指线程之间所具有的一种制约关系，一个线程的执行依赖另一个线程的消息，当它没有得到另一个线程的消息时应等待，直到消息到达时才被唤醒。

线程互斥是指对于共享的进程系统资源，在各单个线程访问时的排它性。当有若干个线程都要使用某一共享资源时，任何时刻最多只允许一个线程去使用，其它要使用该资源的线程必须等待，直到占用资源者释放该资源。线程互斥可以看成是一种特殊的线程同步。

线程间的同步方法大体可分为两类：用户模式和内核模式。顾名思义，内核模式就是指利用系统内核对象的单一性来进行同步，使用时需要切换内核态与用户态，而用户模式就是不需要切换到内核态，只在用户态完成操作。

用户模式下的方法有：原子操作（例如一个单一的全局变量），临界区。内核模式下的方法有：事件，信号量，互斥量。

51、什么是竞争条件？你怎样发现和解决竞争？


当多个进程都企图对共享数据进行某种处理，而最后的结果又取决于进程运行的顺序时，则我们认为这发生了竞争条件（race condition）。

52、为什么我们调用start()方法时会执行run()方法，为什么我们不能直接调用run()方法？


当你调用start()方法时你将创建新的线程，并且执行在run()方法里的代码。

但是如果你直接调用run()方法，它不会创建新的线程也不会执行调用线程的代码，只会把run方法当作普通方法去执行。

53、Java中你怎样唤醒一个阻塞的线程？


在Java发展史上曾经使用suspend()、resume()方法对于线程进行阻塞唤醒，但随之出现很多问题，比较典型的还是死锁问题。

解决方案可以使用以对象为目标的阻塞，即利用Object类的wait()和notify()方法实现线程阻塞。

首先，wait、notify方法是针对对象的，调用任意对象的wait()方法都将导致线程阻塞，阻塞的同时也将释放该对象的锁，相应地，调用任意对象的notify()方法则将随机解除该对象阻塞的线程，但它需要重新获取改对象的锁，直到获取成功才能往下执行；

其次，wait、notify方法必须在synchronized块或方法中被调用，并且要保证同步块或方法的锁对象与调用wait、notify方法的对象是同一个，如此一来在调用wait之前当前线程就已经成功获取某对象的锁，执行wait阻塞后当前线程就将之前获取的对象锁释放。

54、在Java中CycliBarriar和CountdownLatch有什么区别？


CyclicBarrier可以重复使用，而CountdownLatch不能重复使用。

Java的concurrent包里面的CountDownLatch其实可以把它看作一个计数器，只不过这个计数器的操作是原子操作，同时只能有一个线程去操作这个计数器，也就是同时只能有一个线程去减这个计数器里面的值。

你可以向CountDownLatch对象设置一个初始的数字作为计数值，任何调用这个对象上的await()方法都会阻塞，直到这个计数器的计数值被其他的线程减为0为止。

所以在当前计数到达零之前，await 方法会一直受阻塞。之后，会释放所有等待的线程，await的所有后续调用都将立即返回。这种现象只出现一次——计数无法被重置。如果需要重置计数，请考虑使用 CyclicBarrier。

CountDownLatch的一个非常典型的应用场景是：有一个任务想要往下执行，但必须要等到其他的任务执行完毕后才可以继续往下执行。假如我们这个想要继续往下执行的任务调用一个CountDownLatch对象的await()方法，其他的任务执行完自己的任务后调用同一个CountDownLatch对象上的countDown()方法，这个调用await()方法的任务将一直阻塞等待，直到这个CountDownLatch对象的计数值减到0为止。

CyclicBarrier一个同步辅助类，它允许一组线程互相等待，直到到达某个公共屏障点 (common barrier point)。在涉及一组固定大小的线程的程序中，这些线程必须不时地互相等待，此时 CyclicBarrier 很有用。因为该 barrier 在释放等待线程后可以重用，所以称它为循环 的 barrier。

55、什么是不可变对象，它对写并发应用有什么帮助？


不可变对象(Immutable Objects)即对象一旦被创建它的状态（对象的数据，也即对象属性值）就不能改变，反之即为可变对象(Mutable Objects)。

不可变对象的类即为不可变类(Immutable Class)。Java平台类库中包含许多不可变类，如String、基本类型的包装类、BigInteger和BigDecimal等。

不可变对象天生是线程安全的。它们的常量（域）是在构造函数中创建的。既然它们的状态无法修改，这些常量永远不会变。

不可变对象永远是线程安全的。只有满足如下状态，一个对象才是不可变的；它的状态不能在创建后再被修改；所有域都是final类型；并且， 它被正确创建（创建期间没有发生this引用的逸出）。

56、什么是多线程中的上下文切换？


在上下文切换过程中，CPU会停止处理当前运行的程序，并保存当前程序运行的具体位置以便之后继续运行。从这个角度来看，上下文切换有点像我们同时阅读几本书，在来回切换书本的同时我们需要记住每本书当前读到的页码。

在程序中，上下文切换过程中的“页码”信息是保存在进程控制块（PCB）中的。PCB还经常被称作“切换桢”（switchframe）。“页码”信息会一直保存到CPU的内存中，直到他们被再次使用。

上下文切换是存储和恢复CPU状态的过程，它使得线程执行能够从中断点恢复执行。上下文切换是多任务操作系统和多线程环境的基本特征。

57、Java中用到的线程调度算法是什么？


计算机通常只有一个CPU,在任意时刻只能执行一条机器指令,每个线程只有获得CPU的使用权才能执行指令.所谓多线程的并发运行,其实是指从宏观上看,各个线程轮流获得CPU的使用权,分别执行各自的任务。

在运行池中,会有多个处于就绪状态的线程在等待CPU,JAVA虚拟机的一项任务就是负责线程的调度,线程调度是指按照特定机制为多个线程分配CPU的使用权.

有两种调度模型：分时调度模型和抢占式调度模型。分时调度模型是指让所有的线程轮流获得cpu的使用权,并且平均分配每个线程占用的CPU的时间片这个也比较好理解。

java虚拟机采用抢占式调度模型，是指优先让可运行池中优先级高的线程占用CPU，如果可运行池中的线程优先级相同，那么就随机选择一个线程，使其占用CPU。处于运行状态的线程会一直运行，直至它不得不放弃CPU。

58、什么是线程组，为什么在Java中不推荐使用？


线程组和线程池是两个不同的概念，他们的作用完全不同，前者是为了方便线程的管理，后者是为了管理线程的生命周期，复用线程，减少创建销毁线程的开销。

59、为什么使用Executor框架比使用应用创建和管理线程好？


为什么要使用Executor线程池框架 ？

1、每次执行任务创建线程 new Thread()比较消耗性能，创建一个线程是比较耗时、耗资源的。
2、调用 new Thread()创建的线程缺乏管理，被称为野线程，而且可以无限制的创建，线程之间的相互竞争会导致过多占用系统资源而导致系统瘫痪，还有线程之间的频繁交替也会消耗很多系统资源。
3、直接使用new Thread() 启动的线程不利于扩展，比如定时执行、定期执行、定时定期执行、线程中断等都不便实现。

使用Executor线程池框架的优点 ：

1、能复用已存在并空闲的线程从而减少线程对象的创建从而减少了消亡线程的开销。
2、可有效控制最大并发线程数，提高系统资源使用率，同时避免过多资源竞争。
3、框架中已经有定时、定期、单线程、并发数控制等功能。综上所述使用线程池框架Executor能更好的管理线程、提供系统资源使用率。

60、java中有几种方法可以实现一个线程？


继承 Thread 类
实现 Runnable 接口
Callable接口和FutureTask类，需要实现的是 call() 方法
线程池创建线程。

61、如何停止一个正在运行的线程？


1. 使用共享变量的方式

在这种方式中，之所以引入共享变量，是因为该变量可以被多个执行相同任务的线程用来作为是否中断的信号，通知中断线程的执行。

2. 使用interrupt方法终止线程

如果一个线程由于等待某些事件的发生而被阻塞，又该怎样停止该线程呢？这种情况经常会发生，比如当一个线程由于需要等候键盘输入而被阻塞，或者调用Thread.join()方法，或者Thread.sleep()方法，在网络中调用ServerSocket.accept()方法，或者调用了DatagramSocket.receive()方法时，都有可能导致线程阻塞，使线程处于处于不可运行状态时，即使主程序中将该线程的共享变量设置为true，但该线程此时根本无法检查循环标志，当然也就无法立即中断。

这里我们给出的建议是，不要使用stop()方法，而是使用Thread提供的interrupt()方法，因为该方法虽然不会中断一个正在运行的线程，但是它可以使一个被阻塞的线程抛出一个中断异常，从而使线程提前结束阻塞状态，退出堵塞代码。

62、notify()和notifyAll()有什么区别？


当一个线程进入wait之后，就必须等其他线程notify/notifyall,使用notifyall,可以唤醒所有处于wait状态的线程，使其重新进入锁的争夺队列中，而notify只能唤醒一个。

如果没把握，建议notifyAll，防止notigy因为信号丢失而造成程序异常。

63、什么是Daemon线程？它有什么意义？


所谓后台(daemon)线程，是指在程序运行的时候在后台提供一种通用服务的线程，并且这个线程并不属于程序中不可或缺的部分。

因此，当所有的非后台线程结束时，程序也就终止了，同时会杀死进程中的所有后台线程。反过来说， 只要有任何非后台线程还在运行，程序就不会终止。

必须在线程启动之前调用setDaemon()方法，才能把它设置为后台线程。注意：后台进程在不执行finally子句的情况下就会终止其run()方法。

比如：JVM的垃圾回收线程就是Daemon线程，Finalizer也是守护线程。

64、java如何实现多线程之间的通讯和协作？


中断 和 共享变量

65、什么是可重入锁（ReentrantLock）？

举例来说明锁的可重入性
public class UnReentrant{
    Lock lock = new Lock();
    public void outer(){
        lock.lock();
        inner();
        lock.unlock();
    }
    public void inner(){
        lock.lock();
        //do something
        lock.unlock();
    }
}

outer中调用了inner，outer先锁住了lock，这样inner就不能再获取lock。其实调用outer的线程已经获取了lock锁，但是不能在inner中重复利用已经获取的锁资源，这种锁即称之为 不可重入可重入就意味着：线程可以进入任何一个它已经拥有的锁所同步着的代码块。

synchronized、ReentrantLock都是可重入的锁，可重入锁相对来说简化了并发编程的开发。

66、当一个线程进入某个对象的一个synchronized的实例方法后，其它线程是否可进入此对象的其它方法？


如果其他方法没有synchronized的话，其他线程是可以进入的。

所以要开放一个线程安全的对象时，得保证每个方法都是线程安全的。

67、乐观锁和悲观锁的理解及如何实现，有哪些实现方式？


悲观锁：总是假设最坏的情况，每次去拿数据的时候都认为别人会修改，所以每次在拿数据的时候都会上锁，这样别人想拿这个数据就会阻塞直到它拿到锁。

传统的关系型数据库里边就用到了很多这种锁机制，比如行锁，表锁等，读锁，写锁等，都是在做操作之前先上锁。再比如Java里面的同步原语synchronized关键字的实现也是悲观锁。

乐观锁：顾名思义，就是很乐观，每次去拿数据的时候都认为别人不会修改，所以不会上锁，但是在更新的时候会判断一下在此期间别人有没有去更新这个数据，可以使用版本号等机制。

乐观锁适用于多读的应用类型，这样可以提高吞吐量，像数据库提供的类似于write_condition机制，其实都是提供的乐观锁。

在Java中java.util.concurrent.atomic包下面的原子变量类就是使用了乐观锁的一种实现方式CAS实现的。

乐观锁的实现方式：

1、使用版本标识来确定读到的数据与提交时的数据是否一致。提交后修改版本标识，不一致时可以采取丢弃和再次尝试的策略。

2、java中的Compare and Swap即CAS ，当多个线程尝试使用CAS同时更新同一个变量时，只有其中一个线程能更新变量的值，而其它线程都失败，失败的线程并不会被挂起，而是被告知这次竞争中失败，并可以再次尝试。　CAS 操作中包含三个操作数 —— 需要读写的内存位置（V）、进行比较的预期原值（A）和拟写入的新值(B)。如果内存位置V的值与预期原值A相匹配，那么处理器会自动将该位置值更新为新值B。否则处理器不做任何操作。

CAS缺点：

ABA问题：比如说一个线程one从内存位置V中取出A，这时候另一个线程two也从内存中取出A，并且two进行了一些操作变成了B，然后two又将V位置的数据变成A，这时候线程one进行CAS操作发现内存中仍然是A，然后one操作成功。尽管线程one的CAS操作成功，但可能存在潜藏的问题。从Java1.5开始JDK的atomic包里提供了一个类AtomicStampedReference来解决ABA问题。
循环时间长开销大：对于资源竞争严重（线程冲突严重）的情况，CAS自旋的概率会比较大，从而浪费更多的CPU资源，效率低于synchronized。
只能保证一个共享变量的原子操作：当对一个共享变量执行操作时，我们可以使用循环CAS的方式来保证原子操作，但是对多个共享变量操作时，循环CAS就无法保证操作的原子性，这个时候就可以用锁。

68、SynchronizedMap和ConcurrentHashMap有什么区别？


SynchronizedMap一次锁住整张表来保证线程安全，所以每次只能有一个线程来访为map。ConcurrentHashMap使用分段锁来保证在多线程下的性能。

ConcurrentHashMap中则是一次锁住一个桶。ConcurrentHashMap默认将hash表分为16个桶，诸如get,put,remove等常用操作只锁当前需要用到的桶。这样，原来只能一个线程进入，现在却能同时有16个写线程执行，并发性能的提升是显而易见的。

另外ConcurrentHashMap使用了一种不同的迭代方式。在这种迭代方式中，当iterator被创建后集合再发生改变就不再是抛出
ConcurrentModificationException，取而代之的是在改变时new新的数据从而不影响原有的数据 ，iterator完成后再将头指针替换为新的数据 ，这样iterator线程可以使用原来老的数据，而写线程也可以并发的完成改变。

## CopyOnWriteArrayList可以用于什么应用场景？


CopyOnWriteArrayList(免锁容器)的好处之一是当多个迭代器同时遍历和修改这个列表时，不会抛出ConcurrentModificationException。在CopyOnWriteArrayList中，写入将导致创建整个底层数组的副本，而源数组将保留在原地，使得复制的数组在被修改时，读取操作可以安全地执行。

1、由于写操作的时候，需要拷贝数组，会消耗内存，如果原数组的内容比较多的情况下，可能导致young gc或者full gc；

2、不能用于实时读的场景，像拷贝数组、新增元素都需要时间，所以调用一个set操作后，读取到数据可能还是旧的,虽然CopyOnWriteArrayList 能做到最终一致性,但是还是没法满足实时性要求；

CopyOnWriteArrayList透露的思想

1、读写分离，读和写分开 
2、最终一致性 
3、使用另外开辟空间的思路，来解决并发冲突

70、什么叫线程安全？servlet是线程安全吗?


线程安全是编程中的术语，指某个函数、函数库在多线程环境中被调用时，能够正确地处理多个线程之间的共享变量，使程序功能正确完成。

Servlet不是线程安全的，servlet是单实例多线程的，当多个线程同时访问同一个方法，是不能保证共享变量的线程安全性的。

Struts2的action是多实例多线程的，是线程安全的，每个请求过来都会new一个新的action分配给这个请求，请求完成后销毁。

SpringMVC的Controller是线程安全的吗？不是的，和Servlet类似的处理流程。

Struts2好处是不用考虑线程安全问题；Servlet和SpringMVC需要考虑线程安全问题，但是性能可以提升不用处理太多的gc，可以使用ThreadLocal来处理多线程的问题。

71、volatile有什么用？能否用一句话说明下volatile的应用场景？


volatile保证内存可见性和禁止指令重排。

volatile用于多线程环境下的单次操作(单次读或者单次写)。

72、为什么代码会重排序？


在执行程序时，为了提供性能，处理器和编译器常常会对指令进行重排序，但是不能随意重排序，不是你想怎么排序就怎么排序，它需要满足以下两个条件：

在单线程环境下不能改变程序运行的结果；存在数据依赖关系的不允许重排序需要注意的是：重排序不会影响单线程环境的执行结果，但是会破坏多线程的执行语义。

73、在java中wait和sleep方法的不同？


最大的不同是在等待时wait会释放锁，而sleep一直持有锁。Wait通常被用于线程间交互，sleep通常被用于暂停执行。

直接了解的深入一点吧， 在Java中线程的状态一共被分成6种：

（1）初始态：NEW
创建一个Thread对象，但还未调用start()启动线程时，线程处于初始态。

（2）运行态：RUNNABLE
在Java中，运行态包括就绪态 和 运行态。就绪态 该状态下的线程已经获得执行所需的所有资源，只要CPU分配执行权就能运行。所有就绪态的线程存放在就绪队列中。

运行态 获得CPU执行权，正在执行的线程。由于一个CPU同一时刻只能执行一条线程，因此每个CPU每个时刻只有一条运行态的线程。

（3）阻塞态
当一条正在执行的线程请求某一资源失败时，就会进入阻塞态。而在Java中，阻塞态专指请求锁失败时进入的状态。由一个阻塞队列存放所有阻塞态的线程。处于阻塞态的线程会不断请求资源，一旦请求成功，就会进入就绪队列，等待执行。PS：锁、IO、Socket等都资源。

（4）等待态
当前线程中调用wait、join、park函数时，当前线程就会进入等待态。也有一个等待队列存放所有等待态的线程。线程处于等待态表示它需要等待其他线程的指示才能继续运行。进入等待态的线程会释放CPU执行权，并释放资源（如：锁）

（5）超时等待态
当运行中的线程调用sleep(time)、wait、join、parkNanos、parkUntil时，就会进入该状态；它和等待态一样，并不是因为请求不到资源，而是主动进入，并且进入后需要其他线程唤醒；进入该状态后释放CPU执行权 和 占有的资源。与等待态的区别：到了超时时间后自动进入阻塞队列，开始竞争锁。

（6）终止态
线程执行结束后的状态。

注意：
wait()方法会释放CPU执行权 和 占有的锁。
sleep(long)方法仅释放CPU使用权，锁仍然占用；线程被放入超时等待队列，与yield相比，它会使线程较长时间得不到运行。
yield()方法仅释放CPU执行权，锁仍然占用，线程会被放入就绪队列，会在短时间内再次执行。
wait和notify必须配套使用，即必须使用同一把锁调用；
wait和notify必须放在一个同步块中调用wait和notify的对象必须是他们所处同步块的锁对象。

74、一个线程运行时发生异常会怎样？


如果异常没有被捕获该线程将会停止执行。
Thread.UncaughtExceptionHandler是用于处理未捕获异常造成线程突然中断情况的一个内嵌接口。

当一个未捕获异常将造成线程中断的时候JVM会使用Thread.getUncaughtExceptionHandler()来查询线程的UncaughtExceptionHandler并将线程和异常作为参数传递给handler的uncaughtException()方法进行处理。

75、如何在两个线程间共享数据？


在两个线程间共享变量即可实现共享。一般来说，共享变量要求变量本身是线程安全的，然后在线程内使用的时候，如果有对共享变量的复合操作，那么也得保证复合操作的线程安全性。

76、Java中notify 和 notifyAll有什么区别？


notify() 方法不能唤醒某个具体的线程，所以只有一个线程在等待的时候它才有用武之地。而notifyAll()唤醒所有线程并允许他们争夺锁确保了至少有一个线程能继续运行。

77、为什么wait, notify 和 notifyAll这些方法不在thread类里面？


一个很明显的原因是JAVA提供的锁是对象级的而不是线程级的，每个对象都有锁，通过线程获得。由于wait，notify和notifyAll都是锁级别的操作，所以把他们定义在Object类中因为锁属于对象。

78、什么是ThreadLocal变量？


ThreadLocal是Java里一种特殊的变量。每个线程都有一个ThreadLocal就是每个线程都拥有了自己独立的一个变量，竞争条件被彻底消除了。

它是为创建代价高昂的对象获取线程安全的好方法，比如你可以用ThreadLocal让SimpleDateFormat变成线程安全的，因为那个类创建代价高昂且每次调用都需要创建不同的实例所以不值得在局部范围使用它，如果为每个线程提供一个自己独有的变量拷贝，将大大提高效率。

首先，通过复用减少了代价高昂的对象的创建个数。其次，你在没有使用高代价的同步或者不变性的情况下获得了线程安全。

79、Java中interrupted 和 isInterrupted方法的区别？


interrupt ：interrupt方法用于中断线程。调用该方法的线程的状态为将被置为”中断”状态。

注意：线程中断仅仅是置线程的中断状态位，不会停止线程。需要用户自己去监视线程的状态为并做处理。支持线程中断的方法（也就是线程中断后会抛出interruptedException的方法）就是在监视线程的中断状态，一旦线程的中断状态被置为“中断状态”，就会抛出中断异常。

interrupted ：查询当前线程的中断状态，并且清除原状态。如果一个线程被中断了，第一次调用interrupted则返回true，第二次和后面的就返回false了。

isInterrupted ：仅仅是查询当前线程的中断状态

80、为什么wait和notify方法要在同步块中调用？


Java API强制要求这样做，如果你不这么做，你的代码会抛出IllegalMonitorStateException异常。还有一个原因是为了避免wait和notify之间产生竞态条件。

81、为什么你应该在循环中检查等待条件?


处于等待状态的线程可能会收到错误警报和伪唤醒，如果不在循环中检查等待条件，程序就会在没有满足结束条件的情况下退出。

82、Java中的同步集合与并发集合有什么区别？


同步集合与并发集合都为多线程和并发提供了合适的线程安全的集合，不过并发集合的可扩展性更高。在Java1.5之前程序员们只有同步集合来用且在多线程并发的时候会导致争用，阻碍了系统的扩展性。Java5介绍了并发集合像ConcurrentHashMap，不仅提供线程安全还用锁分离和内部分区等现代技术提高了可扩展性。

83、什么是线程池？为什么要使用它？


创建线程要花费昂贵的资源和时间，如果任务来了才创建线程那么响应时间会变长，而且一个进程能创建的线程数有限。

为了避免这些问题，在程序启动的时候就创建若干线程来响应处理，它们被称为线程池，里面的线程叫工作线程。从JDK1.5开始，Java API提供了Executor框架让你可以创建不同的线程池。

84、怎么检测一个线程是否拥有锁？


在java.lang.Thread中有一个方法叫holdsLock()，它返回true如果当且仅当当前线程拥有某个具体对象的锁。

85、你如何在Java中获取线程堆栈？


kill -3 [java pid]不会在当前终端输出，它会输出到代码执行的或指定的地方去。比如，kill -3 tomcat pid, 输出堆栈到log目录下。Jstack [java pid]这个比较简单，在当前终端显示，也可以重定向到指定文件中。-JvisualVM：Thread Dump不做说明，打开JvisualVM后，都是界面操作，过程还是很简单的。

86、JVM中哪个参数是用来控制线程的栈堆栈小的?

-Xss 每个线程的栈大小

87、Thread类中的yield方法有什么作用？


使当前线程从执行状态（运行状态）变为可执行态（就绪状态）。

当前线程到了就绪状态，那么接下来哪个线程会从就绪状态变成执行状态呢？可能是当前线程，也可能是其他线程，看系统的分配了。

88、Java中ConcurrentHashMap的并发度是什么？


ConcurrentHashMap把实际map划分成若干部分来实现它的可扩展性和线程安全。这种划分是使用并发度获得的，它是ConcurrentHashMap类构造函数的一个可选参数，默认值为16，这样在多线程情况下就能避免争用。

在JDK8后，它摒弃了Segment（锁段）的概念，而是启用了一种全新的方式实现,利用CAS算法。同时加入了更多的辅助变量来提高并发度，具体内容还是查看源码吧。

89、Java中Semaphore是什么？


Java中的Semaphore是一种新的同步类，它是一个计数信号。从概念上讲，从概念上讲，信号量维护了一个许可集合。如有必要，在许可可用前会阻塞每一个 acquire()，然后再获取该许可。

每个 release()添加一个许可，从而可能释放一个正在阻塞的获取者。但是，不使用实际的许可对象，Semaphore只对可用许可的号码进行计数，并采取相应的行动。信号量常常用于多线程的代码中，比如数据库连接池。

90、Java线程池中submit() 和 execute()方法有什么区别？


两个方法都可以向线程池提交任务，execute()方法的返回类型是void，它定义在Executor接口中。

而submit()方法可以返回持有计算结果的Future对象，它定义在ExecutorService接口中，它扩展了Executor接口，其它线程池类像ThreadPoolExecutor和ScheduledThreadPoolExecutor都有这些方法。

91、什么是阻塞式方法？


阻塞式方法是指程序会一直等待该方法完成期间不做其他事情，ServerSocket的accept()方法就是一直等待客户端连接。这里的阻塞是指调用结果返回之前，当前线程会被挂起，直到得到结果之后才会返回。此外，还有异步和非阻塞式方法在任务完成前就返回。

92、Java中的ReadWriteLock是什么？


读写锁是用来提升并发程序性能的锁分离技术的成果。

93、volatile 变量和 atomic 变量有什么不同？


Volatile变量可以确保先行关系，即写操作会发生在后续的读操作之前, 但它并不能保证原子性。例如用volatile修饰count变量那么 count++ 操作就不是原子性的。

而AtomicInteger类提供的atomic方法可以让这种操作具有原子性如getAndIncrement()方法会原子性的进行增量操作把当前值加一，其它数据类型和引用变量也可以进行相似操作。

94、可以直接调用Thread类的run ()方法么？


当然可以。但是如果我们调用了Thread的run()方法，它的行为就会和普通的方法一样，会在当前线程中执行。为了在新的线程中执行我们的代码，必须使用Thread.start()方法。

95、如何让正在运行的线程暂停一段时间？


我们可以使用Thread类的Sleep()方法让线程暂停一段时间。需要注意的是，这并不会让线程终止，一旦从休眠中唤醒线程，线程的状态将会被改变为Runnable，并且根据线程调度，它将得到执行。

96、你对线程优先级的理解是什么？


每一个线程都是有优先级的，一般来说，高优先级的线程在运行时会具有优先权，但这依赖于线程调度的实现，这个实现是和操作系统相关的(OS dependent)。

我们可以定义线程的优先级，但是这并不能保证高优先级的线程会在低优先级的线程前执行。线程优先级是一个int变量(从1-10)，1代表最低优先级，10代表最高优先级。

java的线程优先级调度会委托给操作系统去处理，所以与具体的操作系统优先级有关，如非特别需要，一般无需设置线程优先级。

97、什么是线程调度器(Thread Scheduler)和时间分片(Time Slicing )？


线程调度器是一个操作系统服务，它负责为Runnable状态的线程分配CPU时间。一旦我们创建一个线程并启动它，它的执行便依赖于线程调度器的实现。同上一个问题，线程调度并不受到Java虚拟机控制，所以由应用程序来控制它是更好的选择（也就是说不要让你的程序依赖于线程的优先级）。

时间分片是指将可用的CPU时间分配给可用的Runnable线程的过程。分配CPU时间可以基于线程优先级或者线程等待的时间。

98、你如何确保main()方法所在的线程是Java 程序最后结束的线程？

我们可以使用Thread类的join()方法来确保所有程序创建的线程在main()方法退出前结束。

99、线程之间是如何通信的？


当线程间是可以共享资源时，线程间通信是协调它们的重要的手段。Object类中wait()\notify()\notifyAll()方法可以用于线程间通信关于资源的锁的状态。

100、为什么线程通信的方法wait(), notify()和notifyAll()被定义在Object 类里？


Java的每个对象中都有一个锁(monitor，也可以成为监视器) 并且wait()，notify()等方法用于等待对象的锁或者通知其他线程对象的监视器可用。

在Java的线程中并没有可供任何对象使用的锁和同步器。这就是为什么这些方法是Object类的一部分，这样Java的每一个类都有用于线程间通信的基本方法。

101、为什么wait(), notify()和notifyAll ()必须在同步方法或者同步块中被调用？


当一个线程需要调用对象的wait()方法的时候，这个线程必须拥有该对象的锁，接着它就会释放这个对象锁并进入等待状态直到其他线程调用这个对象上的notify()方法。

同样的，当一个线程需要调用对象的notify()方法时，它会释放这个对象的锁，以便其他在等待的线程就可以得到这个对象锁。由于所有的这些方法都需要线程持有对象的锁，这样就只能通过同步来实现，所以他们只能在同步方法或者同步块中被调用。

102、为什么Thread类的sleep()和yield ()方法是静态的？


Thread类的sleep()和yield()方法将在当前正在执行的线程上运行。所以在其他处于等待状态的线程上调用这些方法是没有意义的。这就是为什么这些方法是静态的。它们可以在当前正在执行的线程中工作，并避免程序员错误的认为可以在其他非运行线程调用这些方法。

103、如何确保线程安全？


在Java中可以有很多方法来保证线程安全——同步，使用原子类(atomic concurrent classes)，实现并发锁，使用volatile关键字，使用不变类和线程安全类。

104、同步方法和同步块，哪个是更好的选择？


同步块是更好的选择，因为它不会锁住整个对象（当然你也可以让它锁住整个对象）。同步方法会锁住整个对象，哪怕这个类中有多个不相关联的同步块，这通常会导致他们停止执行并需要等待获得这个对象上的锁。

同步块更要符合开放调用的原则，只在需要锁住的代码块锁住相应的对象，这样从侧面来说也可以避免死锁。

105、如何创建守护线程？


使用Thread类的setDaemon(true)方法可以将线程设置为守护线程，需要注意的是，需要在调用start()方法前调用这个方法，否则会抛出IllegalThreadStateException异常。

106、什么是Java Timer 类？如何创建一个有特定时间间隔的任务？


java.util.Timer是一个工具类，可以用于安排一个线程在未来的某个特定时间执行。Timer类可以用安排一次性任务或者周期任务。

java.util.TimerTask是一个实现了Runnable接口的抽象类，我们需要去继承这个类来创建我们自己的定时任务并使用Timer去安排它的执行。目前有开源的Qurtz可以用来创建定时任务。








<!-- 















## Java内存模型是什么？

Java内存模型规定和指引Java程序在不同 の 内存架构、CPU和操作系统间有确定性地行为。

它在多`线程` の 情况下尤其重要。

Java内存模型对一个`线程`所做 の 变动能被其它`线程`可见提供了保证，它们之间是先行发生关系。

这个关系定义了一些规则让程序员在并发编程时思路更清晰。

比如，先行发生关系确保了：

- `线程`内 の 代码能够按先后顺序执行，这被称为程序次序规则。

- 对于同一个锁，一个解锁操作一定要发生在时间上后发生 の 另一个锁定操作之前，也叫做管程锁定规则。

- 前一个对volatile の 写操作在后一个volatile の 读操作之前，也叫volatile变量规则。

- 一个`线程`内 の 任何操作必需在这个`线程` の start()调用之后，也叫作`线程`启动规则。

- 一个`线程` の 所有操作都会在`线程`终止之前，`线程`终止规则。

- 一个对象 の 终结操作必需在这个对象构造完成之后，也叫`对象终结规则`。

- 可传递性




## 什么是`线程`安全？Vector是一个`线程`安全类吗？

如果你 の 代码所在 の `进程`中有多个`线程`在同时运行，而这些`线程`可能会同时运行这段代码。如果每次运行结果和单`线程`运行 の 结果是一样 の ，而且其他 の 变量 の 值也和预期 の 是一样 の ，就是`线程`安全 の 。一个`线程`安全 の 计数器类 の 同一个实例对象在被多个`线程`使用 の 情况下也不会出现计算失误。很显然你可以将集合类分成两组，`线程`安全和非`线程`安全 の 。Vector 是用同步方法来实现`线程`安全 の , 而和它相似 の ArrayList不是`线程`安全 の 。

## Java中什么是竞态条件？

在大多数实际 の 多`线程`应用中，两个或两个以上 の `线程`需要共享对同一数据 の 存取。如果i`线程`存取相同 の 对象，并且每一个`线程`都调用了一个修改该对象状态 の 方法，将会发生什么呢？可以想象，`线程`彼此踩了对方 の 脚。根据`线程`访问数据 の 次序，可能会产生讹误 の 对象。这样 の 情况通常称为竞争条件。



## 一个`线程`运行时发生异常会怎样？

如果异常没有被捕获该`线程`将会停止执行。Thread.UncaughtExceptionHandler是用于处理未捕获异常造成`线程`突然中断情况 の 一个内嵌接口。当一个未捕获异常将造成`线程`中断 の 时候JVM会使用Thread.getUncaughtExceptionHandler()来查询`线程` の UncaughtExceptionHandler并将`线程`和异常作为参数传递给handler の uncaughtException()方法进行处理。

## 如何在两个`线程`间共享数据？

你可以通过共享对象来实现这个目 の ，或者是使用像阻塞队列这样并发 の 数据结构。这篇教程《Java`线程`间通信》(涉及到在两个`线程`间共享对象)用wait和notify方法实现了生产者消费者模型。

## Java中notify 和 notifyAll有什么区别？

这又是一个刁钻 の 问题，因为多`线程`可以 wait 单监控锁，Java API  の 设计人员提供了一些方法当 wait 条件改变 の 时候通知它们，但是这些方法没有完全实现。notify()方法不能唤醒某个具体 の `线程`，所以只有一个`线程`在 wait  の 时候它才有用武之地。而notifyAll()唤醒所有`线程`并允许他们争夺锁确保了至少有一个`线程`能继续运行。

## 17) 为什么wait, notify 和 notifyAll这些方法不在thread类里面？

一个很明显 の 原因是JAVA提供 の 锁是对象级 の 而不是`线程`级 の ，每个对象都有锁，通过`线程`获得。如果`线程`需要 wait 某些锁那么调用对象中 の wait()方法就有意义了。如果wait()方法定义在Thread类中，`线程`正在 wait  の 是哪个锁就不明显了。简单 の 说，由于wait，notify和notifyAll都是锁级别 の 操作，所以把他们定义在Object类中因为锁属于对象。

## 18) 

## 19) 什么是FutureTask？

在Java并发程序中FutureTask表示一个可以取消 の 异步运算。它有启动和取消运算、查询运算是否完成和取回运算结果等方法。只有当运算完成 の 时候结果才能取回，如果运算尚未完成get方法将会阻塞。一个FutureTask对象可以对调用了`Callable`和`Runnable` の 对象进行包装，由于FutureTask也是调用了`Runnable`接口所以它可以提交给Executor来执行。

## 20) Java中interrupted 和 isInterruptedd方法 の 区别？

interrupted() 和 isInterrupted() の 主要区别是前者会将中断状态清除而后者不会。Java多`线程` の 中断机制是用内部标识来实现 の ，调用Thread.interrupt()来中断一个`线程`就会设置中断标识为true。当中断`线程`调用`静态`方法Thread.interrupted()来检查中断状态时，中断状态会被清零。而非`静态`方法isInterrupted()用来查询其它`线程` の 中断状态且不会改变中断状态标识。简单 の 说就是任何抛出InterruptedException异常 の 方法都会将中断状态清零。无论如何，一个`线程` の 中断状态有有可能被其它`线程`调用中断来改变。

## 21) 为什么wait和notify方法要在同步块中调用？

当一个`线程`需要调用对象 の wait()方法 の 时候，这个`线程`必须拥有该对象 の 锁，接着它就会释放这个对象锁并进入 wait 状态直到其他`线程`调用这个对象上 の notify()方法。同样 の ，当一个`线程`需要调用对象 の notify()方法时，它会释放这个对象 の 锁，以便其他在 wait  の `线程`就可以得到这个对象锁。由于所有 の 这些方法都需要`线程`持有对象 の 锁，这样就只能通过同步来实现，所以他们只能在同步方法或者同步块中被调用。如果你不这么做，代码会抛出IllegalMonitorStateException异常。

## 22) 为什么你应该在循环中检查 wait 条件?

处于 wait 状态 の `线程`可能会收到错误警报和伪唤醒，如果不在循环中检查 wait 条件，程序就会在没有满足结束条件 の 情况下退出。因此，当一个 wait `线程`醒来时，不能认为它原来 の  wait 状态仍然是有效 の ，在notify()方法调用之后和 wait `线程`醒来之前这段时间它可能会改变。这就是在循环中使用wait()方法效果更好 の 原因，你可以在Eclipse中创建模板调用wait和notify试一试。如果你想了解更多关于这个问题 の 内容，推荐你阅读《Effective Java》这本书中 の `线程`和同步章节。

## 23) Java中 の 同步集合与并发集合有什么区别？

同步集合与并发集合都为多`线程`和并发提供了合适 の `线程`安全 の 集合，不过并发集合 の 可扩展性更高。在Java1.5之前程序员们只有同步集合来用且在多`线程`并发 の 时候会导致争用，阻碍了系统 の 扩展性。Java5介绍了并发集合像ConcurrentHashMap，不仅提供`线程`安全还用锁分离和内部分区等现代技术提高了可扩展性。更多内容详见答案。



## 25） 什么是`线程`池？为什么要使用它？

创建`线程`要花费昂贵 の 资源和时间，如果`任务`来了才创建`线程`那么响应时间会变长，而且一个`进程`能创建 の `线程`数有限。为了避免这些问题，在程序启动 の 时候就创建若干`线程`来响应处理，它们被称为`线程`池，里面 の `线程`叫工作`线程`。从JDK1.5开始，Java API提供了Executor框架让你可以创建不同 の `线程`池。比如单`线程`池，每次处理一个`任务`；数目固定 の `线程`池或者是缓存`线程`池（一个适合很多生存期短 の `任务` の 程序 の 可扩展`线程`池）。

## 26） 如何写代码来解决生产者消费者问题？

在现实中你解决 の 许多`线程`问题都属于生产者消费者模型，就是一个`线程`生产`任务`供其它`线程`进行消费，你必须知道怎么进行`线程`间通信来解决这个问题。比较低级 の 办法是用wait和notify来解决这个问题，比较赞 の 办法是用Semaphore 或者 BlockingQueue来实现生产者消费者模型。

## 27） 如何避免死锁？

Java多`线程`中 の 死锁

死锁是指两个或两个以上 の `进程`在执行过程中，因争夺资源而造成 の 一种互相 wait  の 现象，若无外力作用，它们都将无法推进下去。这是一个严重 の 问题，因为死锁会让你 の 程序挂起无法完成`任务`，死锁 の 发生必须满足以下四个条件：

互斥条件：一个资源每次只能被一个`进程`使用。

请求与保持条件：一个`进程`因请求资源而阻塞时，对已获得 の 资源保持不放。

不剥夺条件：`进程`已获得 の 资源，在末使用完之前，不能强行剥夺。

循环 wait 条件：若干`进程`之间形成一种头尾相接 の 循环 wait 资源关系。

避免死锁最简单 の 方法就是阻止循环 wait 条件，将系统中所有 の 资源设置标志位、排序，规定所有 の `进程`申请资源必须以一定 の 顺序（升序或降序）做操作来避免死锁。

## 28) Java中活锁和死锁有什么区别？

这是上题 の 扩展，活锁和死锁类似，不同之处在于处于活锁 の `线程`或`进程` の 状态是不断改变 の ，活锁可以认为是一种特殊 の 饥饿。一个现实 の 活锁例子是两个人在狭小 の 走廊碰到，两个人都试着避让对方好让彼此通过，但是因为避让 の 方向都一样导致最后谁都不能通过走廊。简单 の 说就是，活锁和死锁 の 主要区别是前者`进程` の 状态可以改变但是却不能继续执行。

## 29） 怎么检测一个`线程`是否拥有锁？

在java.lang.Thread中有一个方法叫holdsLock()，它返回true如果当且仅当当前`线程`拥有某个具体对象 の 锁。

## 30) 你如何在Java中获取`线程` heap  stack ？

对于不同 の 操作系统，有多种方法来获得Java`进程` の `线程` heap  stack 。当你获取`线程` heap  stack 时，JVM会把所有`线程` の 状态存到日志文件或者输出到控制台。在Windows你可以使用Ctrl + Break组合键来获取`线程` heap  stack ，Linux下用kill -3命令。你也可以用jstack这个工具来获取，它对`线程`id进行操作，你可以用jps这个工具找到id。

## 31) JVM中哪个参数是用来控制`线程` の  stack  heap  stack 小 の 

这个问题很简单， -Xss参数用来控制`线程` の  heap  stack 大小。你可以查看JVM配置列表来了解这个参数 の 更多信息。



## 33） 有三个`线程`T1，T2，T3，怎么确保它们按顺序执行（确保main()方法所在 の `线程`是Java程序最后结束 の `线程`）？

在多`线程`中有多种方法让`线程`按特定顺序执行，你可以用`线程`类 の join()方法在一个`线程`中启动另一个`线程`，另外一个`线程`完成该`线程`继续执行。为了确保三个`线程` の 顺序你应该先启动最后一个(T3调用T2，T2调用T1)，这样T1就会先完成而T3最后完成。

## 34) Thread类中 の yield方法有什么作用？

yield方法可以暂停当前正在执行 の `线程`对象，让其它有相同优先级 の `线程`执行。它是一个`静态`方法而且只保证当前`线程`放弃CPU占用而不能保证使其它`线程`一定能占用CPU，执行yield() の `线程`有可能在进入到暂停状态后马上又被执行。点击这里查看更多yield方法 の 相关内容。

## 35） Java中ConcurrentHashMap の 并发度是什么？

ConcurrentHashMap把实际map划分成若干部分来实现它 の 可扩展性和`线程`安全。这种划分是使用并发度获得 の ，它是ConcurrentHashMap类构造函数 の 一个可选参数，默认值为16，这样在多`线程`情况下就能避免争用。

## 36） Java中Semaphore是什么？

Java中 の Semaphore是一种**new** の 同步类，它是一个计数信号。从概念上讲，从概念上讲，信号量维护了一个许可集合。如有必要，在许可可用前会阻塞每一个 acquire()，然后再获取该许可。每个 release()添加一个许可，从而可能释放一个正在阻塞 の 获取者。但是，不使用实际 の 许可对象，Semaphore只对可用许可 の 号码进行计数，并采取相应 の 行动。信号量常常用于多`线程` の 代码中，比如数据库连接池。更多详细信息请点击这里。

## 37）如果你提交`任务`时，`线程`池队列已满。会时发会生什么？

这个问题问得很狡猾，许多程序员会认为该`任务`会阻塞直到`线程`池队列有空位。事实上如果一个`任务`不能被调度执行那么ThreadPoolExecutor’s submit()方法将会抛出一个RejectedExecutionException异常。

## 38) Java`线程`池中submit() 和 execute()方法有什么区别？

两个方法都可以向`线程`池提交`任务`，execute()方法 の 返回类型是void，它定义在Executor接口中, 而submit()方法可以返回持有计算结果 の Future对象，它定义在ExecutorService接口中，它扩展了Executor接口，其它`线程`池类像ThreadPoolExecutor和ScheduledThreadPoolExecutor都有这些方法。更多详细信息请点击这里。

39) 什么是阻塞式方法？

阻塞式方法是指程序会一直 wait 该方法完成期间不做其他事情，ServerSocket の accept()方法就是一直 wait 客户端连接。这里 の 阻塞是指调用结果返回之前，当前`线程`会被挂起，直到得到结果之后才会返回。此外，还有异步和非阻塞式方法在`任务`完成前就返回。更多详细信息请点击这里。

40） 你对`线程`优先级 の 理解是什么？

每一个`线程`都是有优先级 の ，一般来说，高优先级 の `线程`在运行时会具有优先权，但这依赖于`线程`调度 の 实现，这个实现是和操作系统相关 の (OS dependent)。我们可以定义`线程` の 优先级，但是这并不能保证高优先级 の `线程`会在低优先级 の `线程`前执行。`线程`优先级是一个int变量(从1-10)，1代表最低优先级，10代表最高优先级。

41） 什么是`线程`调度器(Thread Scheduler)和时间分片(Time Slicing)？

`线程`调度器是一个操作系统服务，它负责为`Runnable`状态 の `线程`分配CPU时间。一旦我们创建一个`线程`并启动它，它 の 执行便依赖于`线程`调度器 の 实现。时间分片是指将可用 の CPU时间分配给可用 の `Runnable``线程` の 过程。分配CPU时间可以基于`线程`优先级或者`线程` wait  の 时间。`线程`调度并不受到Java虚拟机控制，所以由应用程序来控制它是更好 の 选择（也就是说不要让你 の 程序依赖于`线程` の 优先级）。



43) 如何在Java中创建Immutable对象？

Immutable对象可以在没有同步 の 情况下共享，降低了对该对象进行并发访问时 の 同步化开销。要创建不可变类，要实现下面几个步骤：通过构造方法初始化所有成员、对变量不要提供setter方法、将所有 の 成员声明为私有 の ，这样就不允许直接访问这些成员、在getter方法中，不要直接返回对象本身，而是克隆对象，并返回对象 の 拷贝。

44） Java中 の ReadWriteLock是什么？

一般而言，读写锁是用来提升并发程序性能 の 锁分离技术 の 成果。Java中 の ReadWriteLock是Java 5 中**new**增 の 一个接口，一个ReadWriteLock维护一对关联 の 锁，一个用于只读操作一个用于写。在没有写`线程` の 情况下一个读锁可能会同时被多个读`线程`持有。写锁是独占 の ，你可以使用JDK中 の ReentrantReadWriteLock来实现这个规则，它最多支持65535个写锁和65535个读锁。





47) 如果同步块内 の `线程`抛出异常会发生什么？

这个问题坑了很多Java程序员，若你能想到锁是否释放这条线索来回答还有点希望答对。无论你 の 同步块是正常还是异常退出 の ，里面 の `线程`都会释放锁，所以对比锁接口我们更喜欢同步块，因为它不用花费精力去释放锁，该功能可以在finally block里释放锁实现。

48） 单例模式 の 双检锁是什么？

这个问题在Java面试中经常被问到，但是面试官对回答此问题 の 满意度仅为50%。一半 の 人写不出双检锁还有一半 の 人说不出它 の 隐患和Java1.5是如何对它修正 の 。它其实是一个用来创建`线程`安全 の 单例 の 老方法，当单例实例第一次被创建时它试图用单个锁进行性能优化，但是由于太过于复杂在JDK1.4中它是失败 の 。

49） 如何在Java中创建`线程`安全 の Singleton？

这是上面那个问题 の 后续，如果你不喜欢双检锁而面试官问了创建Singleton类 の 替代方法，你可以利用JVM の 类加载和`静态`变量初始化特征来创建Singleton实例，或者是利用枚举类型来创建Singleton。

50) 写出3条你遵循 の 多`线程`最佳实践

以下三条最佳实践大多数Java程序员都应该遵循：

给你 の `线程`起个有意义 の 名字。

这样可以方便找bug或追踪。OrderProcessor, QuoteProcessor or TradeProcessor 这种名字比 Thread-1. Thread-2 and Thread-3 好多了，给`线程`起一个和它要完成 の `任务`相关 の 名字，所有 の 主要框架甚至JDK都遵循这个最佳实践。

避免锁定和缩小同步 の 范围

锁花费 の 代价高昂且上下文切换更耗费时间空间，试试最低限度 の 使用同步和锁，缩小临界区。因此相对于同步方法我更喜欢同步块，它给我拥有对锁 の 绝对控制权。

多用同步类少用wait 和 notify

首先，CountDownLatch, Semaphore, CyclicBarrier 和 Exchanger 这些同步类简化了编码操作，而用wait和notify很难实现对复杂控制流 の 控制。其次，这些类是由最好 の 企业编写和维护在后续 の JDK中它们还会不断优化和完善，使用这些更高等级 の 同步工具你 の 程序可以不费吹灰之力获得优化。

多用并发集合少用同步集合

这是另外一个容易遵循且受益巨大 の 最佳实践，并发集合比同步集合 の 可扩展性更好，所以在并发编程时使用并发集合效果更好。如果下一次你需要用到map，你应该首先想到用ConcurrentHashMap。

51) 如何强制启动一个`线程`？

这个问题就像是如何强制进行Java垃圾回收，目前还没有觉得方法，虽然你可以使用System.gc()来进行垃圾回收，但是不保证能成功。在Java里面没有办法强制启动一个`线程`，它是被`线程`调度器控制着且Java没有公布相关 の API。

52) Java中 の fork join框架是什么？

fork join框架是JDK7中出现 の 一款高效 の 工具，Java开发人员可以通过它充分利用现代服务器上 の 多处理器。它是专门为了那些可以递归划分成许多子模块设计 の ，目 の 是将所有可用 の 处理能力用来提升程序 の 性能。fork join框架一个巨大 の 优势是它使用了工作窃取算法，可以完成更多`任务` の 工作`线程`可以从其它`线程`中窃取`任务`来执行。


54） 什么是Thread Group？为什么不建议使用它？

ThreadGroup是一个类，它 の 目 の 是提供关于`线程`组 の 信息。

ThreadGroup API比较薄弱，它并没有比Thread提供了更多 の 功能。它有两个主要 の 功能：一是获取`线程`组中处于活跃状态`线程` の 列表；二是设置为`线程`设置未捕获异常处理器(ncaught exception handler)。但在Java 1.5中Thread类也添加了setUncaughtExceptionHandler(UncaughtExceptionHandler eh) 方法，所以ThreadGroup是已经过时 の ，不建议继续使用。

55) 什么是Java`线程`转储(Thread Dump)，如何得到它？

`线程`转储是一个JVM活动`线程` の 列表，它对于分析系统瓶颈和死锁非常有用。有很多方法可以获取`线程`转储——使用Profiler，Kill -3命令，jstack工具等等。我们更喜欢jstack工具，因为它容易使用并且是JDK自带 の 。由于它是一个基于终端 の 工具，所以我们可以编写一些脚本去定时 の 产生`线程`转储以待分析。

56) 什么是Java Timer类？如何创建一个有特定时间间隔 の `任务`？

java.util.Timer是一个工具类，可以用于安排一个`线程`在未来 の 某个特定时间执行。Timer类可以用安排一次性`任务`或者周期`任务`。

java.util.TimerTask是一个实现了`Runnable`接口 の 抽象类，我们需要去继承这个类来创建我们自己 の 定时`任务`并使用Timer去安排它 の 执行。

57) 什么是原子操作？在Java Concurrency API中有哪些原子类(atomic classes)？

原子操作是指一个不受其他操作影响 の 操作`任务`单元。原子操作是在多`线程`环境下避免数据不一致必须 の 手段。

int++并不是一个原子操作，所以当一个`线程`读取它 の 值并加1时，另外一个`线程`有可能会读到之前 の 值，这就会引发错误。

在 java.util.concurrent.atomic 包中添加原子变量类之后，这种情况才发生了改变。所有原子变量类都公开比较并设置原语（与比较并交换类似），这些原语都是使用平台上可用 の 最快本机结构（比较并交换、加载链接/条件存储，最坏 の 情况下是旋转锁）来实现 の 。java.util.concurrent.atomic 包中提供了原子变量 の  9 种风格（ AtomicInteger；AtomicLong；AtomicReference；AtomicBoolean；原子整型；长型；引用；及原子标记引用和戳记引用类 の 数组形式，其原子地更**new**一对值）。

58） Java Concurrency API中 の Lock接口(Lock interface)是什么？对比同步它有什么优势？

Lock接口比同步方法和同步块提供了更具扩展性 の 锁操作。他们允许更灵活 の 结构，可以具有完全不同 の 性质，并且可以支持多个相关类 の 条件对象。

它 の 优势有：

可以使锁更公平

可以使`线程`在 wait 锁 の 时候响应中断

可以让`线程`尝试获取锁，并在无法获取锁 の 时候立即返回或者 wait 一段时间

可以在不同 の 范围，以不同 の 顺序获取和释放锁

59） 什么是Executor框架？

Executor框架同java.util.concurrent.Executor 接口在Java 5中被引入。Executor框架是一个根据一组执行策略调用，调度，执行和控制 の 异步`任务` の 框架。

无限制 の 创建`线程`会引起应用程序内存溢出。所以创建一个`线程`池是个更好 の  の 解决方案，因为可以限制`线程` の 数量并且可以回收再利用这些`线程`。利用Executor框架可以非常方便 の 创建一个`线程`池。

60） Executors类是什么？

Executors为Executor，ExecutorService，ScheduledExecutorService，ThreadFactory和`Callable`类提供了一些工具方法。

Executors可以用于方便 の 创建`线程`池。

61） 什么是阻塞队列？如何使用阻塞队列来实现生产者-消费者模型？

java.util.concurrent.BlockingQueue の 特性是：当队列是空 の 时，从队列中获取或删除元素 の 操作将会被阻塞，或者当队列是满时，往队列里添加元素 の 操作会被阻塞。

阻塞队列不接受空值，当你尝试向队列中添加空值 の 时候，它会抛出NullPointerException。

阻塞队列 の 实现都是`线程`安全 の ，所有 の 查询方法都是原子 の 并且使用了内部锁或者其他形式 の 并发控制。

BlockingQueue 接口是java collections框架 の 一部分，它主要用于实现生产者-消费者问题。

62）什么是`Callable`和Future?

Java 5在concurrency包中引入了java.util.concurrent.`Callable` 接口，它和`Runnable`接口很相似，但它可以返回一个对象或者抛出一个异常。

`Callable`接口使用泛型去定义它 の 返回类型。Executors类提供了一些有用 の 方法去在`线程`池中执行`Callable`内 の `任务`。由于`Callable``任务`是`并行` の ，我们必须 wait 它返回 の 结果。java.util.concurrent.Future对象为我们解决了这个问题。在`线程`池提交`Callable``任务`后返回了一个Future对象，使用它我们可以知道`Callable``任务` の 状态和得到`Callable`返回 の 执行结果。Future提供了get()方法让我们可以 wait `Callable`结束并获取它 の 执行结果。

63） 什么是FutureTask?

FutureTask包装器是一种非常便利 の 机制，可将`Callable`转换成Future和`Runnable`，它同时实现两者 の 接口。

FutureTask类是Future  の 一个实现，并实现了`Runnable`，所以可通过Excutor(`线程`池) 来执行。也可传递给Thread对象执行。如果在主`线程`中需要执行比较耗时 の 操作时，但又不想阻塞主`线程`时，可以把这些作业交给Future对象在后台完成，当主`线程`将来需要时，就可以通过Future对象获得后台作业 の 计算结果或者执行状态。

64） 什么是并发容器 の 实现？

Java集合类都是快速失败 の ，这就意味着当集合被改变且一个`线程`在使用迭代器遍历集合 の 时候，迭代器 の next()方法将抛出ConcurrentModificationException异常。

并发容器：并发容器是针对多个`线程`并发访问设计 の ，在jdk5.0引入了concurrent包，其中提供了很多并发容器，如ConcurrentHashMap，CopyOnWriteArrayList等。并发容器使用了与同步容器完全不同 の 加锁策略来提供更高 の 并发性和伸缩性，例如在ConcurrentHashMap中采用了一种粒度更细 の 加锁机制，可以称为分段锁，在这种锁机制下，允许任意数量 の 读`线程`并发地访问map，并且执行读操作 の `线程`和写操作 の `线程`也可以并发 の 访问map，同时允许一定数量 の 写操作`线程`并发地修改map，所以它可以在并发环境下实现更高 の 吞吐量。

65）用户`线程`和守护`线程`有什么区别？

当我们在Java程序中创建一个`线程`，它就被称为用户`线程`。一个守护`线程`是在后台执行并且不会阻止JVM终止 の `线程`。当没有用户`线程`在运行 の 时候，JVM关闭程序并且退出。一个守护`线程`创建 の 子`线程`依然是守护`线程`。

66）有哪些不同 の `线程`生命周期？

当我们在Java程序中**new**建一个`线程`时，它 の 状态是New。当我们调用`线程` の `start()方法`时，状态被改变为`Runnable`。`线程`调度器会为`Runnable``线程`池中 の `线程`分配CPU时间并且讲它们 の 状态改变为Running。其他 の `线程`状态还有Waiting，Blocked 和Dead。

67）`线程`之间是如何通信 の ？

当`线程`间是可以共享资源时，`线程`间通信是协调它们 の 重要 の 手段。Object类中wait()\notify()\notifyAll()方法可以用于`线程`间通信关于资源 の 锁 の 状态。





70）同步方法和同步块，哪个是更好 の 选择？

同步块是更好 の 选择，因为它不会锁住整个对象（当然你也可以让它锁住整个对象）。同步方法会锁住整个对象，哪怕这个类中有多个不相关联 の 同步块，这通常会导致他们停止执行并需要 wait 获得这个对象上 の 锁。

71）如何创建守护`线程`？

使用Thread类 の setDaemon(true)方法可以将`线程`设置为守护`线程`，需要注意 の 是，需要在调用`start()方法`前调用这个方法，否则会抛出IllegalThreadStateException异常。

72）`线程`调度策略？

(1) 抢占式调度策略

Java运行时系统 の `线程`调度算法是抢占式 の  (preemptive)。Java运行时系统支持一种简单 の 固定优先级 の 调度算法。如果一个优先级比其他任何处于可运行状态 の `线程`都高 の `线程`进入就绪状态，那么运行时系统就会选择该`线程`运行。**new** の 优先级较高 の `线程`抢占(preempt)了其他`线程`。但是Java运行时系统并不抢占同优先级 の `线程`。换句话说，Java运行时系统不是分时 の (time-slice)。然而，基于Java Thread类 の 实现系统可能是支持分时 の ，因此编写代码时不要依赖分时。当系统中 の 处于就绪状态 の `线程`都具有相同优先级时，`线程`调度程序采用一种简单 の 、非抢占式 の 轮转 の 调度顺序。

(2) 时间片轮转调度策略

有些系统 の `线程`调度采用时间片轮转(round-robin)调度策略。这种调度策略是从所有处于就绪状态 の `线程`中选择优先级最高 の `线程`分配一定 の CPU时间运行。该时间过后再选择其他`线程`运行。只有当`线程`运行结束、放弃(yield)CPU或由于某种原因进入阻塞状态，低优先级 の `线程`才有机会执行。如果有两个优先级相同 の `线程`都在 wait CPU，则调度程序以轮转 の 方式选择运行 の `线程`。

73) 在`线程`中你怎么处理不可捕捉异常？

Thread.UncaughtExceptionHandler是java SE5中 の **new**接口，它允许我们在每一个Thread对象上添加一个异常处理器
 -->
