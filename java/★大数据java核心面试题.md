
https://www.javashitang.com/

https://gitee.com/zeus-maker/marion-notes







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

## IO 和 NIO  の 区别? BIO有什么缺点？为什么要用NIO？

关于这个问题，我会从下面几个方面来回答：

1. IO 指 の 是, 实现【数据】从【`磁盘`】中【读取 or 写入】。
除了【`磁盘`】以外，【`内存 or 网络`】都可以作为【IO 流】 の 【来源 or 目 の 地】。

2. 当【程序】面向 の 是【网络】进行数据IO操作时，Java 里面通过【socker】来实现【网络通信】

3. 基于【socker】 の IO，属于【阻塞IO】。也就是说，当server受到【client请求】，需要等到 【client请求】 の 数据处理完毕，才能获取下一个【client请求】。so, 基于【socker】 の IO 能够连接数量非常少。

4. NIO 是 新增 の  new IO 机制。提供了【非阻塞IO】，也就是说，在【连接未就绪 or IO未就绪】 の 情况下，服务端不会【堵塞】当前连接，而是继续【轮询】后续 の 链接来处理。so, NIO可以【并行处理】 の 连接数量非常多。

5. NIO 相比于【传统 の  old IO】，效率更高

## BIO和NIO 应用场景？

BIO：适用于【连接数目小】，且【固定的架构】，对服务器资源要求高，并发局限在应用中
NIO：适用于【连接数目多】，【连接比较短】，比如【聊天服务器】，并发局限在应用中，编程比较复杂
AIO：适用于【连接数目多】，【连接比较长】，比如【相册服务器】

## IO模型的分类：

- 同步IO：发完一个请求后，再发下一个。可以避免【死锁、脏读】
- 异步IO：发完一个请求后，不等待返回，随时可以再发送下一个请求。可以提高效率，保证并发。
- 阻塞：传统的IO都是阻塞的，当一个线程调用read或write方法时，该线程将被阻塞，until有一些数据读取 or 被写入。在此期间，该线程不能执行其他task。在完成网络通信进行IO时，由于【线程】被阻塞，所以【服务端】必须为每一个【客户端】都提供一个独立的【线程】进行处理。当【服务端】需要处理大量【客户端】的时候，性能急剧下降
- 非阻塞：当线程从某个【通道】进行读取数据的时候，若没有数据可以用，该【线程】就会去执行其他任务。线程通常将【非阻塞IO】的空闲时间，用于在其他【通道】上执行IO。所以单个线程可以管理多个【输入、输出】。所以【单独的线程】可以管理多个【输入、输出】。所以可以仅仅用【1 or 几个】线程处理链接到【服务器】上的所有【客户端】

## 说说NIO の 组件：channel、buffer、selector？

多路复用IO：引入了【选择器selector】的概念，只需要一个线程，便可以管理多个【客户端】连接，减少对【CPU资源】的损耗

1. channel需要注册到selector。也就是说，channel要和selector绑定
2. selector和channel是一对多的关系，就是1个selector对应多个channel，一个channel相当于一个【客户端连接】。所以这里的作用相当于就是，一个selector可以处理多个【客户端连接】。就相当于是所谓的【多路复用】

https://www.bilibili.com/video/BV1F34y1x7b1

调用操作系统函数epoll_create，返回一个实例叫做selector

selector是event收集器

**如何建立连接？**

1. 创建selector
2. 创建【套接字channel】，用于创建连接
3. 把channel注册到selector
4. 创建buffer
5. 【阻塞】等待client【连接】

**客户端：**

1. 启动，创建channel，并建立【连接】
2. 【连接】之后会获取这个【事件】，这里有2个不同的【事件】：
   - 【客户端】连接【事件】：创建【套接字channel】，用于读写数据，这个通道需要注册到selector
   - 【读数据】事件

**客户端如何写数据？**

1. 数据先写到 buffer，然后再从 buffer 写到 channel

**服务器如何读数据？**

1. 第一次，【阻塞】等待client【连接】
2. 之后，不断【循环轮询事件】，【轮询】【读数据】的【事件】。刚刚【客户端】就是写了数据到【服务器】，这里就是不停地【循环轮询事件】
3. 从【事件】可以得到【套接字通道】，然后将数据从【套接字channel】写到buffer
4. 写完后，打印出来

**服务器如何写数据？**

1. 写数据到客户端，写数据到client就是写数据到channel

**客户端如何读数据？**

1. 【阻塞】等待服务器【写数据】
2. 【服务器】已经写data到【客户端】，这个时候，【客户端】channel已经有数据了。
3. 把channel中的数据写到buffer


## NIO の 【同步非阻塞】 の 实现？

## BIO、NIO、AIO 有什么区别？

`BIO`：Block IO - `同步阻塞式 IO`

- 当server受到【client请求】，需要等到数据处理完毕，才能获取下一个【client请求】。so, BIO 能够连接数量非常少。
- 数据的读写，必须总在【一个线程】内等待其完成。
- 会一直阻塞，直到【内核】把数据copy到【用户空间】。
- 用烧开水的例子，就是让【一个线程】等待水壶烧开，并期间都不做

`NIO`：New IO - `同步非阻塞 IO` - 也就是【多路复用】

- 可以创建【多个线程】发起【read】
- 在【连接未就绪 or IO未就绪】 の 情况下，服务端不会【堵塞】当前连接，
- 而是继续【轮询】后续 の 链接来处理。so, NIO可以【并行处理】 の 连接数量非常多。

`AIO`：Asynchronous IO - `异步非堵塞 IO`

- 是 NIO  の 升级，也叫 NIO2
- 基于【事件】和【回调机制】。
- 【client用户进程】发起一个IO，然后【立即返回】，不会阻塞。
- 等【IO操作】真正完成以后，才会自动得到【IO完成 の 通知】
- 对应到【烧开水】中，就是为每个水壶装一个【通知】，自动通知【水烧开了】

[幼麟实验室 - 协程和IO多路复用更配哦](https://www.bilibili.com/video/BV1a5411b7aZ)

[幼麟实验室 - channel 数据结构 阻塞、非阻塞操作 多路select](https://www.bilibili.com/video/BV1kh411n79h)

[幼麟实验室 - 协程让出、抢占、监控和调度](https://www.bilibili.com/video/BV1zT4y1F7XF)

IO多路复用

是除【进程模型、线程模型】之外，实现并发的一种方式。

【一个进程】调用【select函数】，同时等待多个事件，

事件到达后才获得【控制权】开始执行该对应的代码，

事件到达前处于【挂起状态】，因此又叫【事件驱动程序】。

缺点：

- 编码复杂，要把所有的事件逻辑写在一个程序里；
- 又因为只有一个线程，无法利用多cpu。
- 
优点：

- Node.js、nginx等高性能服务器使用的都是【IO多路复用】的事件编程方式，主要因为其比起【进程、线程】具有明显的性能优势。
- Go基于多个channel（事件）的select也是一种【IO多路复用】。epoll才是事件驱动吧，通过中断事件主动告知cpu相应

## 对netty的理解

netty是一个【异步事件驱动】的【网络应用程序框架】

可以用于【快速开发高性能、高可靠】的【网络IO程序】

封装了JDK的NIO，使用起来更加灵活

**用netty的目的：**

在于解决【服务器】如何去承载【更多的用户】同时去访问的问题。

传统的BIO的【阻塞特性】使得我们在高并发场景中，很难去支持更高的吞吐量。后来，我们基于【NIO多路复用模型】虽然在【阻塞方面】进行了优化，但是它的API使用比较复杂，对于初学者来说不是特别友好。而Netty是基于NIO的封装，提供了简单成熟易用的API，降低了【使用成本、学习成本】

本质上来说，Netty和NIO所扮演的角色是相同的，都是为了提升【服务端】的吞吐量，让用户获得更好的【产品体验】。另外，netty这个【中间件】经过了很多年的【考证】

## netty相对于其他网络框架，有什么优势？

1. 高并发。
   - netty 是基于NIO的【网络通信框架】，对比于BIO，并发性能得到了很大提高
2. 传输快。
   - netty 的传输依赖于【零拷贝】，尽量减少不必要的【内存拷贝】，实现了更高效率的传输
3. 封装好。
   - netty 封装了NIO操作的细节，提供了易于调用的接口
4. 功能强大。
   - 预支了多种【编解码功能】，支持多种【主流协议】
5. 扩展能力强。
   - 可以通过【ChannelHandler】对【通信框架】进行【灵活地拓展】
6. 高性能。
   - 与其他【业界】主流的【NIO框架】对比，Netty的综合性能最优秀

## netty为什么综合性能最优秀？

1. 串行无锁化设计。
   - 消息的处理尽可能在【same线程完成】，避免多线程竞争
   - 启动多个【串行化线程】【并行运行】
   - 比【one队列-many工作线程】模型更优
2. 内存零拷贝。
   - TCP接收和发送buffer使用【直接内存】代替【堆内存】
   - 避免了【内存复制】，提升了【IO读写性能】
3. 支持通过【内存池】的方式，循环利用ByteBuf。
   - 避免了频繁【创建|销毁】 ByteBuf 带来的【性能损耗】
4. 使用【线程安全容器、原子类】。
   - 从而提升系统的【并发处理能力】
5. 可配置的IO线程数、TCP参数。
   - 为不同的场景提供定制化的调优参数
6. 高性能序列化协议。
   - 支持protobuf等高性能序列化协议

## 什么是零拷贝？

要完成一次完整的IO交互流程的话，分为2个阶段：

- 第一阶段：先copy到系统的【内核空间】，这一部分由【操作系统】完成
- 第二阶段：再copy到【用户空间】，这一部分由【引用程序来完成】

零拷贝：不需要将【数据】从一个【存储区域】复制到【another存储区域】

我们可以将【系统内核空间】的内存 与 【用户空间】的内存，实现一个【直接关联映射】。从而省去了【数据传输过程】中的copy。它将【磁盘】中的文件通过【socket连接】发送出去。

read方法，传统的4次拷贝，将数据从【磁盘】读取到【内核缓冲区】，然后再copy到【用户缓冲区】

write方法，先把数据写入到【socket缓冲区】，最后写入到【网卡】。

因为传统的IO，在数据传输的过程中，copy次数太多了，从而导致性能低下，所以才有了【零拷贝】的设计。【零拷贝】并不是完全没有【文件的拷贝】，只是减少了【copy次数】

## Netty是如何实现【零拷贝】的？

1. 使用【堆外内存，也叫，直接内存】。
   - netty的收发都采用DirectBuffers，
   - 对应于【系统底层】的【mmap机制】，
   - 直接使用【堆外内存】进行【socket读写】，
   - 就不需要进行【字节缓冲区】的【二次拷贝】
2. 【组合Buffer对象】。
   - 它可以聚合多个【ByteBuffer对象】，
   - 用户只需要操作一个Buffer，一样可以对【组合Buffer对象】进行操作，
   - 从而避免了【传统的内存拷贝】的方式。
   - 将【几个小的Buffer】合并成【一个大的Buffer】就不需要再做多次的【内存拷贝】
3. 【Netty文件传输】采用了【TransferTo方法】，可以直接使用NIO的【sendfile机制】。
   - 就是直接将【文件缓冲区】的数据发送到【目标channel】，
   - 从而避免了传统的通过【循环write】的方式，导致内存的【拷贝问题】


## Netty

https://www.bilibili.com/video/BV1r94y1Z71j

https://www.bilibili.com/video/BV1M34y1Y7h8

https://www.bilibili.com/video/BV1iT411572i

<https://www.bilibili.com/video/BV1GN4y1g7Wi>

## 我们为什么要选用Netty？

netty 是一个基于【Java NIO 封装】的【高性能】的【网络通信框架】

特点和优势，在于：

1. Netty 提供了比 NIO 更简单的 API
2. Netty 在 NIO 的基础上还做了很多优化，比如【零拷贝机制、无锁化串行机制、内存池的管理】。Netty 的【总运行性能】比原生的 NIO，要更有优势
3. Netty 内置了多种【通信协议】。比如Http、WebSocket，并且针对这些【通信协议】，还内置了一些【拆包、黏包】的解决方案


1. Netty 提供了统一的API，它支持多种【通信模型】，比如，【阻塞、非阻塞、epoll、poll】等通信模型
2. Netty 只需要使用很少的代码，就可以实现【Reactor多线程模型、主从线程模型】
3. 可以使用 Netty 自带的【编解码器】去解决【TCP拆包、黏包】的问题
4. Netty 还默认提供了【多线程】的【通信支持】
5. Netty 处理【高吞吐量、低延时、低消耗资源、更少内存复制】
6. 经典的开源项目的底层，也用到了 Netty 通信框架。比如Zookeeper
7. Netty 的安全性也不错，比如，支持 SSL、TLS


## Netty有哪些核心组件了？

netty的核心组件分为3层：

1. 网络通信层：有3个【核心组件】：
   - Bootstrap：负责【客户端】启动，并且用来连接【远程NettyServer】
   - ServerBootStrap：负责【服务端】监听，用来监听【指定端口】
   - Channel：相当于完成【网络通信】的【载体】
2. 事件调度层：
   - EventLoopGroup：本质上是一个【线程池】，主要负责接收【IO请求】，并分配线程执行处理请求
   - EventLoop：相当于是【线程池】中的【线程】
3. 服务编排层：
   - ChannelPipeline：负责将多个ChannelHandler链接到一起
   - ChannelHandler：针对IO的数据处理器，数据接收后，通过指定的Handler进行处理
   - ChannelHandlerContext：用来保存ChannelHandler的上下文信息

https://www.bilibili.com/video/BV11Y411M7Td

## Netty中提供了哪些【线程模型】？说说对Reactor模式的理解

Reactor是在【NIO多路复用】基础上，提出了一个高性能的【IO设计模式】，它的核心思想是把【响应IO事件】和【业务处理】进行分离，通过一个或者多个【线程】处理【IO事件】。然后再把【就绪的事件】分发给【业务线程】进行【异步处理】。

直译过来，叫做【反应堆】。它是Netty支持【异步多线程】的核心组件。常见的【Reactor线程模型】有3种：

1. 【单Reactor单线程模型】
   - `Reactor`：主要负责将【IO事件】发派给对应的Handler
   - `Acceptor`：用于处理【客户端】连接请求
   - `Handlers`：执行【非阻塞】的【IO读写任务】
   - 缺点：Handler的执行时【串行】的，如果其中【一个Handler处理线程】【阻塞】，将导致其他业务处理也阻塞。比如 Handler和Reactor在【同一个线程】中执行，导致【无法接受新的请求】
2. 【单Reactor多线程模型】
   - 就是在【业务处理】的地方加入【线程池】，实现【异步处理】，这样将Reactor和Handler放在【不同的线程】中来执行
   - 造成性能瓶颈的2个原因：
   - （1）所有的IO操作都是由【一个Reactor】完成，而【Reactor】又运行在【单线程】，它需要处理包括【accept、read、write、connect】等操作，在【并发量】小的情况下，影响并不大，但是【并发量】变大，就会出现【高负载、高并发、大数据量】的应用场景，就容易出现【性能瓶颈】。【一个NIO线程】同时处理【成百上千的链路】，无法满足【海量消息的读取和发送】
   - （2）【NIO线程】负载过重之后，处理速度将会变慢，这会导致【大量client】连接超时，导致大量【消息积压】
3. 【主从多Reactor多线程模型】
   - `Main Reactor`：负责接收【客户端】连接请求，并将具体的【业务IO】处理请求转发给`Sub Reactor`
   - Acceptor【转发】请求给【Main Reactor线程池】，不真正负责【连接请求】的建立
   - `sub Reactor`：负责【数据读写】，在NIO中通常注册channel的【读写事件】

<https://www.bilibili.com/video/BV1xq4y1g73f>

https://www.bilibili.com/video/BV1yT411V7gp

## 为什么Netty线程池默认大小为CPU核数的2倍？线程提升性能的2个核心指标：

1. 延时
2. 吞吐量

提升性能最主要的目的是：“降低延时、提高吞吐量”
降低延时，就是，提高CPU的处理能力
提高吞吐量，就是，提高IO读写效率

我们将程序分为：

1. IO密集型任务
   - 当我们假设【CPU计算、IO操作】的【耗时比例】是1:1
   - 所以，2个【线程】是最合适的
   - 当我们假设【CPU计算、IO操作】的【耗时比例】是1:2
   - 所以，3个【线程】是最合适的

2. CPU密集型任务
   - 虽然【线程数量 = CPU核数】才是最合适的，
   - 但实际【线程数量 = CPU核数 + 1】
   - 因为线程在执行过程中，可会阻塞，
   - 多设置一个线程，可以保证CPU的利用率

Netty一般应用于【IO密集型场景】。有个参数叫做ioRatio，默认值是50，它表示，执行【IO事件】和 执行【异步任务】的耗时占比为1:1，所以，【Netty线程池】默认大小为CPU核数的2倍

在实际生产中。可以提前压测，确定线程池的大小，根据压测结果进行微调。在大多数场景，没有必要关注线程池大小的设置。

https://www.bilibili.com/video/BV1BY4y1z7Ry

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








## 有没有【顺序】 の 【Map实现类】

LinkedHashMap 和 TreeMap

## LRU缓存实现原理

列表里面 の 【元素】，按照【访问次序】进行【排序】

```java
LinkedHashMap cache = new LinkedHashMap(initialCapacity:16,
        loadFactor: 0.75f,
        accessOrder: true);
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




## java 8 改进了之前 の  DATE  の 烂设计

```java
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
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








## hash算法 の 有哪几种，优缺点，使用场景




## Hash表是怎么实现 の




## Object の 常用方法有哪些？

https://www.bilibili.com/video/BV1E341157wW










## 谈谈你对spring aop の 理解？

https://www.bilibili.com/video/BV1LB4y1t7jr




## 形参 & 实参

实参：用于传递给 method  の 参数，必须有确定 の 值

形参：定义method，接收【实参】，不必有确定 の 值


















## public

如果a包下 の `A类`是`public` の ，它 の 字段和方法都是private の 。

→ 在`b包`下 の `B类`可以创建`A类` の 对象，但是无法访问`A类对象 の 字段和方法`。

如果a包下 の `A类``没有修饰符`，它 の 字段和方法都是private の 。

→ 在`a包`下 の B类可以创建`A类` の 对象，但无法访问A类对象 の 字段和方法。

→ 在`b包`下 の B类无法创建A类 の 对象。







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





## 外部排序简单应用。有10个500M の 日志文件，其中每个日志文件内部按照时间戳已排序，内存只有1G の 机器如何对其所有排序合并成一个文件。

https://www.bilibili.com/video/BV1JN411Z7k4

归并排序思路：

内存中维持一个【元素个数为10】 の 【小顶堆】，

同时维持【10个指针】，指针分别指向【10个日志文件】 の 【首条记录元素】。

将【10个元素】读取进入到【内存】中之后，对其【进行排序】，

取出【堆顶元素】，写入【新 の 日志文件】，并移动【该元素对应 の 文件指针】读取【下一个记录】到小顶堆中，

如此重复，直到所有 の 日志文件读取完成写入新日志文件。

由于内存为1G，也可以先从每个日志文件一次性读取50M，逐条读取，排序，写入新 の 日志文件，直到读取完毕。

## stream
