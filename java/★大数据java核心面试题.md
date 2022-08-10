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

## 什么是AQS

英文全称：AbstractQueuedSynchronizer

是一个【队列同步器】

底层：通过【FIFO】 の 【双向链表】来实现

## 互斥

## synchronized

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