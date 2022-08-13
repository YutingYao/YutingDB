#  redis

[Redis学到什么程度，面试不虚心](https://www.bilibili.com/video/BV1Jm4y197xG)

## Redis为什么这么快？

https://www.bilibili.com/video/BV1va411a7pc

https://www.bilibili.com/video/BV1WY411V7yd

## Redis 单线程为什么还能那么快？

1. 基于【内存】
2. 单线程，没有【线程切换】开销
3. 基于【IO多路复用机制】，提高了【IO利用率】
4. 高效的【数据存储结构】，【全局 hash表】 & 【跳表等】

## 为什么要使用Redis？

https://www.bilibili.com/video/BV1sS4y1w7Cf

## 什么是Redis

https://www.bilibili.com/video/BV1rY4y1G77P

https://www.bilibili.com/video/BV1Th411b7fN

## Redis和Mysql如何保证数据一致性，如何高分回答？

https://www.bilibili.com/video/BV1y44y1N7X3

## 缓存与数据库数据一致性怎么保证？

https://www.bilibili.com/video/BV1Z3411u7Bh

## 项目中为什么要用redis，他和其他数据库的比较，和mongodb的比较

redis value类型，日常使用是命令行还是封装的

## redis的持久化方式，以及项目中用的哪种，为什么？aof和rdb的区别，可以共存吗？redis两种持久化方式分别有什么问题？

https://www.bilibili.com/video/BV1La411E7fZ

https://www.bilibili.com/video/BV1eY411u7F2

[AOF重写](https://www.bilibili.com/video/BV1eY411u7F2)

https://www.bilibili.com/video/BV1W64y1q7uA

| RDB  |  AOF |
|---|---|
|  Redis DataBase |  Append Only File |
|  持久化【内存数据】到 → 磁盘(dump.rdb) |  client 发起 → 持久化【修改数据】的命令(appendonly.aof) |
|  可能丢数据 | 混合持久化：RDB全量 + AOF增量 → 写入AOF文件 |

## Redis集群策略

https://www.bilibili.com/video/BV1hZ4y1U7iC

## redis持久化策略如何设置？

如果对性能要求比较高：

- master: 最好 not 持久化
- 某个slave: 开启AOF, 每秒1次

## 【非常重要】redis有哪些数据结构？

https://www.bilibili.com/video/BV1mS4y1175L

String 💚 简单动态字符串

List 💚 双向链表、压缩列表

Hash 💚 压缩列表、哈希表

Set 💚 哈希表、整数数组

zset、Sorted Set 💚 压缩列表、跳表

其他：

GeoHash：坐标，借助 Sorted Set 实现

HyperLogLog：统计【不重复数据】

Stream：内存版本的kafka


## 跳表底层数据结构？

跳表：将【有序链表】改造为【近似“折半查找”】算法，从而加快增删改查操作

增加了【多级索引】，通过【多级索引位置】的跳转，实现了【快速查找】

## redis 中 hashtable进行rehash的触发机制

## redis的zset是怎么实现的？底层数据结构？

https://www.bilibili.com/video/BV1kh411x7Jc

（哈希表＋跳表）

## 【压缩列表】的结构？

列表长度 + 尾部偏移量 + 列表元素个数

`元素1、元素2 。。。元素N`

列表结束标识

## 什么时候用压缩列表？

1. 【有序集合】保存的【元素数量】小于 128 个
2. 【有序集合】保存的【all 元素长度】小于 64 字节

## 什么时候采用跳表？

## 跳表的时间复杂度

## 跳表如何查找某个元素？描述一下

## zset 为什么用【跳表】而不用【二叉树】or【红黑树】？

1. 【跳表】的实现更【简单】
2. 便于【范围查找】

## 跳表的节点有什么内容？

## 跳表的查询过程是什么样的？

## 复杂度是多少？

## redis的跳表是双向的，这样设计是为什么？


## Redis集群为什么至少需要3个master节点

因为master的选举需要【大于半数节点同意】才能选举成功。

如果只有2个master，其中1个挂了，则无法满足选举条件。

## Redis的缓存淘汰策略

https://www.bilibili.com/video/BV18S4y1t7a2

## 为什么用缓存，用过哪些缓存，redis和memcache的区别

## redis 缓存污染导致的生产事故，又是一个惨痛的教训

https://www.bilibili.com/video/BV16T411J7sW

## 就因为下单的时候我用了更新缓存，导致公司损失了100多个W

https://www.bilibili.com/video/BV1k3411u7ks

## hash 分片算法

redis集群 将【所有数据】划分为【16384 个 slots】，每个节点负责一部分 slots。

当 client 来连接集群时，它会得到【slots 配置信息】，并将其缓存在 【client 本地】。

这样，当 client 要查找【某个key】时，可以根据【slots 定位算法】定位到 【目标 node】

**【slots 定位算法】：**

```java
对key使用【CRC16算法】进行hash，再对16384取模

HASH_SLOT = CRC16(key) mod 16384

master1 (0-5000)       master2 (5001-10000)       master3 (10001-16383)
```

## redis集群的理解，怎么动态增加或者删除一个节点，而保证数据不丢失。

（一致性哈希问题）

## 什么是【主从复制风暴】？

如果Redis有很多【从节点】，【all从节点】在同一时刻，同时连接【主节点】

那么【主节点】会同时把【内存快照RDB】发给多个【从节点】，

这样【主节点压力】very big ！！！

## 如何解决【主从复制风暴】？

【主从架构】优化一下，比如【树形结构】

## 缓存雪崩和缓存穿透的理解以及如何避免？

https://www.bilibili.com/video/BV1tS4y1B7cf

https://www.bilibili.com/video/BV1nL4y1K7VK

https://www.bilibili.com/video/BV1gB4y127eT

https://www.bilibili.com/video/BV1SS4y1a7rf

https://www.bilibili.com/video/BV1P3411H77i

## Redis主从切换 & 缓存雪崩

https://www.bilibili.com/video/BV1xN4y1T7TJ

要确保【主从 Redis】【机器时钟的一致性】

如果主从切换时，slave 与 master 机器时钟不一致。

从 slave 看来，master 中【没过期的数据】，已经过期了。

slave 开始大量清理【过期的数据】

导致：

1. 【主线程】发生【阻塞】，无法及时处理【client】请求

## Redis集群【网络抖动】导致【频繁主从切换】怎么处理？

网络抖动非常常见。导致【频繁主从切换】会影响集群的性能。

修改`cluster-node-timeout`。可以减少【网络抖动】导致【频繁主从切换】

## 删除 key 命令会阻塞 Redis 吗？

可能会。

1. 删除【单个字符串】的 key，时间复杂度为 O(1)
2. 删除【list、set、有序set、HashTable类型】的 key，时间复杂度为 O(M)

可能会阻塞 Redis

```java
DEL key
```

## Redis keys命令有什么问题？

https://www.bilibili.com/video/BV1oY4y1V7oU

## Redis的并发竞争问题如何解决了解Redis事务的CAS操作吗

1.1 缓存机器增删如何对系统影响最小，一致性哈希的实现
1.2 Redis持久化的几种方式，优缺点是什么，怎么实现的
1.3 Redis的缓存失效策略

## 缓存击穿？？

https://www.bilibili.com/video/BV1xS4y1B7Sa

[看高手如何教科书回答，怎么防止缓存击穿的问题？](https://www.bilibili.com/video/BV16S4y1J7LJ)

https://www.bilibili.com/video/BV1FS4y1J7S5

https://www.bilibili.com/video/BV1T44y1E7EM

## Redis 遇到数据访问倾斜了怎么办？

https://www.bilibili.com/video/BV19a411q7uq

## 缓存穿透的解决办法

[Redis缓存穿透是什么？](https://www.bilibili.com/video/BV1YF41157g9)

https://www.bilibili.com/video/BV1cB4y1v7uH

1.5 redis集群，高可用，原理
1.6 用Redis和任意语言实现一段恶意登录保护的代码，限制1小时内每用户Id最多只能登录5次

## `Redis应用场景`有哪些？

1. 利用 Redis 的【自增操作】，实现【计数器】的功能，如【用户点赞数、用户访问数】
2. 限速器，可用于限制用户访问【某个接口】的频率，比如，【秒杀场景】防止用户快速点击
3. 缓存热点数据，缓解数据库压力
4. 作为简单的消息队列，实现异步操作

## Redis大key怎么处理？

https://www.bilibili.com/video/BV1xF411V75c

## Redis key过期了，为什么内存没释放？

https://www.bilibili.com/video/BV1WT411E7HQ

## Redis Key 过期了，为什么内存没有释放？redis怎么设置过期的？

`SET`除了设置`key-value`之外，还可以设置key的`过期时间`，就像下面这样:

```java
SET tuling zhuge EX 120
```

而如果，在修改时，没有设置`过期时间`的参数

```java
SET tuling zhuge666
```

那么这个key的`过期时间`就会被`擦除`。

```java
TTL tuling // key 永不过时
```

如果发现 ① Redis 的内存持续增长；② 很多 key 的过期时间丢失

很有可能就是上述原因导致的。


## Redis Key 过期了，为什么内存没有释放？怎么检查？

[LRU缓存实现原理](https://www.bilibili.com/video/BV1fZ4y1M7qm)

（周期检查，惰性检查）。

Redis 对于过期 key 的处理一般有`惰性删除`和`定时删除`两种策略

1. 惰性删除：当【读写】一个过期的【key】，发现这个key【已经过期】，就直接删除掉这个key。
   - Redis 淘汰 Key 的算法：
   - (1) LRU: 淘汰【很久】没有访问的数据 💨 适用于：绝大多数情况
   - (2) LFU: 淘汰【访问最少】的数据 💨 适用于：存在大量的缓存热点数据
2. 定时删除：【惰性删除】无法保障【冷数据】被及时地删掉，所以，Redis会定期【默认100ms】删掉【部分过期的key】，
3. 仍然有【部分过期的key】没有被清理掉。。。。

## redis【过期键】的【删除策略】

1. 【主动删除】。定时清理 key, 每次清理会依次遍历【所有DB】。每次随机取 20 个 key，如果其中有 5 个key过期，就继续清理这个 DB.
2. 【被动删除】。在访问 key 时，如果发现 key 已经过期，那么会将 key 删除。
3. 内存不够时清理。maxmemory设置了最大内存，当【使用内存】超过了【最大内存】，就要进行【内存释放】，按照【淘汰策略】，如下：

## redis的数据【淘汰策略】

一共8种：

(1) 针对【设置了过期时间】的key做处理：

   1. volatile-ttl：根据【过期时间】的先后
   2. volatile-random：随机删除
   3. volatile-lru：LRU算法
   4. volatile-lfu：LFU算法

(2) 针对【所有】的key做处理：

   1. allkeys-random：随机删除
   2. allkeys-lru：LRU算法
   3. allkeys-lfu：LFU算法

(3) not 处理：

   1. noeviction：不删除任何数据：只读，不写



## 什么是【IO多路复用机制】

https://www.bilibili.com/video/BV1194y1Q7Rn

## IO的多路复用 select/poll/epoll 的区别

https://www.bilibili.com/video/BV1vT4y1f7T2

## 分布式session中用 の 是什么数据结构

## Redis 单线程 or 多线程？

https://www.bilibili.com/video/BV1Qi4y1m7Vk

Redis 6.0 是指的是：

- 【网络请求】采用了【多线程】
- 【键值对-读写】仍然是【单线程】
- so 是【并发安全】的

## MySQL和Redis的双写一致性？

https://www.bilibili.com/video/BV1CD4y1X75a

## bind到底绑定的是什么

https://www.bilibili.com/video/BV1eD4y1o7np

## redis是单线程的，为什么这么设计呢？

1. 基于内存，瓶颈不在cpu，加锁反而会带来性能损失
2. 实现简单，方便维护
3. 如果采用【多线程】，那么所有底层的【数据结构设计】必须考虑【线程安全问题】，会很复杂
4. 如果采用【多线程】，那么必然会引入【同步机制】，导致带来更多的开销

## 单线程一定比多线程好吗？为什么新版本的redis还要变成多线程的？

（答 读多写少的时候，加共享锁多线程性能更好）， 

## 学习下分布式锁 mysql redis zookeeper

[Redis实现分布式锁](https://www.bilibili.com/video/BV1da4y1p7my)

https://www.bilibili.com/video/BV1YZ4y1k7pC

## 那不考虑这种场景，读写差不多的情况下呢？

## 结合项目，问我elasticsearch和redis，然后redis会问到一些实现的经过，redis缓存到期后的问题。





##  redis使用的是集群还是单机

## redis为什么可以承载那么高的并发量


