
## Mysql表时间列用 datetime 还是 timestamp ？

个人认为用datetime更好。

虽然 timestamp 是 `四字节`，但一旦存储毫秒就会变成 7 字节，和datetime的8字节相差无几。

虽然 timestamp 可以 `解决时区问题`，但 还是通过前后端的转换更好，可以减轻数据库的压力。在高并发情况下，可能出现`性能抖动问题`。

## MySQL架构

https://www.bilibili.com/video/BV1W34y1k7yX

## 数据库锁表 の 相关处理

## exist和in的区别？

https://www.bilibili.com/video/BV1uF41157gm

## Mysql锁有哪些，如何理解

https://www.bilibili.com/video/BV1ff4y1Z7VQ

## MySQL性能优化

https://www.bilibili.com/video/BV1C3411x7yU

## limit500000,10和limit 10速度一样快吗？

https://www.bilibili.com/video/BV1N3411V7yf

## explain sql 执行计划 の 各列参数

最重要的几个参数

| 参数  |  含义 |
|---|---|
|  id | select 查询的序列号  |
| table  |   |
|  type | 访问类型  |
|  key |   |
| rows  | 行数的预估值  |
| extra  | 其他信息   |

type 访问类型：效率由【好】到【坏】依次是

system > eq_ref > range > all

- system: 表只有一行记录，平时不出现
- eq_ref: 使用【唯一索引】进行【数据查找】
- range: 范围查询
- all: 全表扫描


https://www.bilibili.com/video/BV1mh411q7u5

[Explain语句结果中各个字段分表表示什么](https://www.bilibili.com/video/BV1gt4y1a7sE)

## 索引覆盖

https://www.bilibili.com/video/BV1HF411M7gr


## 【两个线程】去操作【数据库】时，【数据库】发生了【死锁】

```sql
show OPEN TABLES where In_use > 0; -- 查询是否锁表
show processlist; -- 查询进程
select * from information_schema.Innodb_locks; --查看正在锁 の 事务
select * from information_schema.Innodb_lock_waits; --查看等待锁 の 事务

```

## 【数据库】的三种并发场景

第一种：【读的并发】，就是两个线程A和B同时进行读操作，这种情况不会产生任何并发问题。

第二种：【读写并发】，就是两个【线程】，A和B同时进行【读写操作】，就会造成如下问题：

1. 事务隔离性问题。
2. 出现【脏读、幻读、不可重复读】的问题

第三种：【写的并发】，就是两个线程A和B同时进行写操作，就会造成如下问题：

1. 数据更新的丢失。

MVCC 就是为了解决【事务操作】过程中【并发安全问题】的【无锁并发控制技术】

## mvcc知道吗

https://www.bilibili.com/video/BV1iZ4y1a7QZ

全称 Multi-Version Concurrency Control

多版本并发控制

指的是：

维持一个数据的【多个版本】，使得没有【读写冲突】

目的：

- 提高【数据库】的【并发访问】性能
- 处理【读写冲突】。即使有【读写冲突】，也能【不加锁】，【非阻塞、并发读】
- 解决【脏读、幻读、不可重复读】等【事务隔离】问题，但不能解决【更新丢失】问题

## MVCC 实现原理？

通过【数据库记录】中的【隐式字段、Undo日志、Read View】来实现的。

## MVCC主要解决3个问题？

1. 读写并发阻塞的问题。
2. 采用【乐观锁】的方式实现，降低了【死锁】的概率
3. 解决了【一致性】读的问题

## MVCC 、乐观锁、悲观锁 的区别是什么？

【MVCC】 用来解决【读写冲突】
【乐观锁 or 悲观锁】用来解决【写的冲突】

## 高并发下如何做到安全 の 修改同一行数据，乐观锁和悲观锁是什么?

## 什么是乐观锁，什么悲观锁？

https://www.bilibili.com/video/BV1nY4y1Y7G5

## INNODB の 行级锁有哪2种，解释其含义

## SQL相应的排查思路

https://www.bilibili.com/video/BV1WY411T79A

## 一条sql语句运行慢，如何做。怎么处理MySQL的慢查询？

（慢查询，看索引，看orderby和groupby）

1. 开启【慢查询】日志，准确定位到【某个sql语句】
2. 分析【sql语句】，看看是否load了【不需要的列】
3. 分析【执行计划】，看其【索引】的使用情况，然后修改【语句or索引】，使得【语句】尽可能命中【索引】
4. 如果上述优化，还是不够。看看表中【数据量】是否过大，可以【横向or纵向】分表

<https://www.bilibili.com/video/BV1N34y17715>

## 什么情况下不建索引？

https://www.bilibili.com/video/BV1KU4y1U7TZ

## MySQL索引，给我整不会了！

https://www.bilibili.com/video/BV1KP4y1A7QB

## 索引 の 设计原则

在进行索引设计的时候，应该：

1. 经常用于 where 或 join 的列
2. 小表没必要建立索引
3. 建立索引的列没必要太多
4. 频繁更新的字段不要有索引

<https://www.bilibili.com/video/BV1WB4y1s7kD>

## 为什么要有索引？

为了提高查询性能

## 什么是索引？

是一种“key”。相当于字典中的【音序表】

## 分表之后想让一个id多个表是自增 の ，效率实现

## 事物 の 四个特性，以及各自 の 特点（原子、隔离）等等，项目怎么解决这些问题

## 浏览器从url输入到返回 の 流程

## MySQL查询语句的执行流程

https://www.bilibili.com/video/BV1gF41157Rx

## 1T文件，每行是一个数字，机器128G内存，求top10数字

## 如何进行etl链路优化

## 如果链路慢，但是没有sla风险，怎么优化（类似上一道题）

## 怎么数据链运行慢定位问题，对任务进行优化

## 索引失效场景

<https://www.bilibili.com/video/BV1yr4y1E7Xu>

<https://www.bilibili.com/video/BV1pr4y1p7Ak>

1. 对于【组合索引】，不使用【组合索引】【最左边的字段】，则【索引失效】
2. 以%开头的like查询，无法使用索引：比如%abc
3. 以%结束的like查询，可以使用索引：比如abc%，相当于范围查询
4. 查询条件中，类型是【字符串类型】，没有使用【引号】，则可能会因为【类型不同】发生【隐式转换】，导致【索引失效】
5. 判断【索引列】是否【不等于】某个值
6. 对【索引列】进行运算
7. 查询条件用【or链接】


## 如何保证MySQL数据库的高可用性？

<https://www.bilibili.com/video/BV1aL4y1G7jT>

## 去重如果不用 set 还有多少种方式，有没有更高效 の 方式？

## 怎么解决幻读 具体在sql是怎么实现 の

## 幻读

https://www.bilibili.com/video/BV1964y1s7tD

## 什么是【一致性】读？

事务启动的时候，根据【某个条件】去【读取数据】，直到【事务结束】再去执行【相同的条件】，还是读到【同一份数据】，不会发生变化

## 维度建模有什么好处？如果业务需求增加一个维度，后续需要做哪些工作？

## 怎么判断一个需求能不能实现，你们 の 判断标准是什么？需求变更要做什么？

## 增加一个维度后发现查询 の 速度变得非常慢，是什么原因导致 の ？

## 数据库视图

## 数据库 の 三大范式，都有哪些区别，举例说明

https://www.bilibili.com/video/BV1dB4y117ry

## Mysql索引的优点和缺点

https://www.bilibili.com/video/BV1HY4y1r7qx

## 数据库引擎mysaim和innoDB の 区别

| mysaim  | innoDB  |
|---|---|
| OLAP  | OLTP  |
| 不支持【事务】  |  支持【事务】 |
| 支持【表锁】【全文索引】  | 支持【行锁】【外键】【自增】【MVCC模式的读写】 |
| 非聚簇索引  | 非聚簇索引 + 聚簇索引  |
|   | 读慢，写快  |
|   | 【一行一行】删除  |

## 说一下你对行锁、临键锁、间隙锁的理解

https://www.bilibili.com/video/BV1rN4y1M7vN

## innodb の 索引结构

https://www.bilibili.com/video/BV1pS4y1v7ew

4.为什么 选用mongo

7.为什么用mongo


2. 数据库有哪些类型
## SQL和NoSQL区别

https://www.bilibili.com/video/BV1HB4y1D7pL

## 分布式ID有哪些解决方案？

https://www.bilibili.com/video/BV19r4y1L7cZ

https://www.bilibili.com/video/BV1ea411H7RR

## 什么是事务

https://www.bilibili.com/video/BV1RS4y1d7kr

## Mysql事务的实现原理

https://www.bilibili.com/video/BV1sg41197Gm

## mysql索引是什么结构，索引存储int和字符串有什么区别（不会）

mysql索引设计原则

## MySQL分区

https://www.bilibili.com/video/BV1Z3411P7XM

## int(1)和int(10)有什么区别？

https://www.bilibili.com/video/BV1dZ4y117sp

## 高度为3的B+树可以存放多少数据？

https://www.bilibili.com/video/BV1aT4y1q7ME

## 索引下推

https://www.bilibili.com/video/BV1VL4y1V7XE

## 说说 MySQL  の 索引？b树与b+树 の 区别？

https://www.bilibili.com/video/BV1TF411u7f8

https://www.bilibili.com/video/BV1534y1a7bq

https://www.bilibili.com/video/BV1oP4y1M7ib

## B树和B+树的区别是什么？

https://www.bilibili.com/video/BV1gB4y1q7dQ

## Mysql数据库中，什么情况下设置了索引但⽆法使用

## 数据库会死锁吗，举一个死锁 の 例子，mysql怎么解决死锁

## 事务的隔离级别

https://www.bilibili.com/video/BV1Ja411J7Jn

## 深分页怎么优化？

https://www.bilibili.com/video/BV1D94y1U7C1

## 垮库分页的问题，有最优解么？

https://www.bilibili.com/video/BV1xS4y1u75r

## 事务 の 特性，隔离 の 级别

Mysql事务的实现原理：

MySQL里面的事务是满足【ACID特性】的。
在于InnoDB，是如何保证ACID的特性的。

A ： 需要保证多个【DML操作】的【原子性】，要么都成功，要么都失败。如果失败，那么，对原本执行成功的数据，需要进行【回滚】，所以，在innoDB里面，设计了一个【UNDO_LOG表】。

在【事务】执行的过程中，把【修改之前】的【数据快照】保存到【UNDO_LOG表】里面，一旦出现错误，就直接从【UNDO_LOG表】里面读取数据，进行【反向操作】就行了。

C：就是数据的【完整性约束】没有被破坏。而数据库本身，也提供了像【主键的唯一约束】【字段长度】【类型】的保障

I：表示事务的【隔离性】，多个【并行事务】对同一个数据的操作，互不干扰，从而避免【数据混乱】。InnoDB里面提供了4种隔离级别的实现：

1. RU (未提交读)
2. RC (已提交读)
3. RR (可重复读)（InnoDB默认）
4. Serializable (串行化)

还使用了 【MVCC机制】，解决了【脏读】和【不可重复读】的一个问题，

并且使用了【行锁、表锁】的方式，解决了【幻读】的问题

D：持久性。也就是说，只要事务【提交成功】，那么，对于这个数据的结果的影响，一定是永久的。不能因为【数据库宕机】or 其他原因，导致【数据变更】的失效。

数据发生变更后，先更新【buffer pool】，然后在【合适的时间】，持久化到【磁盘】。

在这个过程中，有可能出现【宕机】，导致数据丢失，无法满足【持久性】。因此，InnoDB引入了【Redo_log】，当我们通过【事务】进行【数据更改】时，除了修改【buffer pool】的数据，还会把修改值追加到【Redo_log】。即使宕机，重启以后，可以直接使用【redo_log】的重新日志，重写一遍，从而保证持久性。

也就是说，MySQL用过【MVCC、行锁、表锁、UNDO_LOG、REDO_LOG】保证了这个特性

## ACID 靠什么来保证？

A：由 undolog 来保证：在事务回滚时，撤销已经执行成功的SQL

C：由于其他三大特性保证

I: 由 MVCC 保证

D：由 redolog 来保证。mysql在修改数据时，会在redolog中，记录一份【日志数据】，就算【数据】没有保存成功，只有【log】保存成功了，数据仍然不会丢失

## 什么是分库分表

[全面分库分表方案](https://www.bilibili.com/video/BV1RZ4y1m7Q9)

https://www.bilibili.com/video/BV1MY4y1k7VW

## 为什么数据量大了，就一定要分库分表？

https://www.bilibili.com/video/BV1Ja411e7dG

## MySQL单表多大考虑进行分库分表？

https://www.bilibili.com/video/BV1d34y1E7zj

## 水平切分和垂直切分

https://www.bilibili.com/video/BV1Hi4y1m7SE

## 想用数据库“读写分离” 请先明白“读写分离”解决什么问题？

https://www.bilibili.com/video/BV1bA4y1D72W

## truncate、delete与drop有什么异同点？

## mysql索引

## hash索引 + B+树

https://www.bilibili.com/video/BV1DY4y147Wj

https://www.bilibili.com/video/BV17F41177RT

## mysql の 引擎

## Innodb是如何实现事务的

https://www.bilibili.com/video/BV1hT4y1v7Up

innoDB

mysql の 引擎和innodb这类数据库 の 区别

## mysql索引相关 の 问题，b+树

https://www.bilibili.com/video/BV1a341137my

[讲讲mysql の 索引及B树和B+树](https://www.bilibili.com/video/BV1Dq4y1S7GZ)

https://www.bilibili.com/video/BV18U4y197Dx

## MySQL 索引，存数据库，还是存磁盘

## count(1)、count(*)和count(字段名)的区别？

https://www.bilibili.com/video/BV1Yd4y1m72z

## 【聚簇索引】和【非聚簇索引】如何区别？

https://www.bilibili.com/video/BV1x5411m7iy

https://www.bilibili.com/video/BV1Er4y1572y

https://www.bilibili.com/video/BV1o3411J7Mh

只需要判断【数据】和【索引】是否存在一起

## 前缀索引

https://www.bilibili.com/video/BV1WF411L7Mk

## mySQL是【聚簇索引】，还是【非聚簇索引】？

与【存储引擎】相关

innoDB：都有。

- `xxx.ibd`包含【数据】 + 【索引】
- 建立【聚簇索引】占用的空间更大，一旦【聚簇索引】改变，那么【非聚簇索引】也要跟着变

myisam：只有【非聚簇索引】

- `xxx.myd`包含【数据】
- `xxx.myi`包含【索引】

## MySQL有哪些存储引擎？

https://www.bilibili.com/video/BV1zr4y1x7o7

## MyISAM和InnoDB的区别？

| InnoDB  |  MyISAM |
|---|---|
| OLTP，支持事务  | OLAP，不支持事务  |
| 支持【行锁】，支持【外键】  | 支持【表锁】和全文索引  |
| 写快  |   |
| 读慢  |   |


https://www.bilibili.com/video/BV1Ba411J7TP

## 你们 ADS 层 の 数据量每天 の 数据量有多大？ADS 层再 MySQL 中 の 表是怎么创建 の ？有什么注意事项？索引怎么创建 の ？

1.1 MySql の 存储引擎 の 不同

1.2 Mysql怎么分表，以及分表后如果想按条件分页查询怎么办(如果不是按分表字段来查询 の 话，几乎效率低下，无解)

## MySql の 主从实时备份同步 の 配置，以及原理()，读写分离

1. 当【主库】上的数据发生改变时，将其改变写入【二进制binlog】中
2. 【从库】每隔一段时间对【二进制binlog】探测，如果发生改变，则开始一个【IO线程】请求【二进制binlog】的【events】
3. 同时，【主库】为每个【从库IO线程】启动一个【主库dump线程】，用于发送【二进制Event】，并保存至【从节点 本地relay-log】
4. 【从库SQL线程】读取【本地relay-log】，使得数据和【主库】保持一致。
5. 最后【从库IO线程】和【从库SQL线程】将进入【睡眠状态】，等待下一次被唤醒

## bin log&undo log&redo log

https://www.bilibili.com/video/BV1Lr4y1x7W9

## 从库读主库 の binlog

1. 【从库】通过【手动执行】【change master to 语句】连接【主库】
2. 【主库dump线程】 & 【从库IO线程】建立连接
3. 【从库】根据【change master to 语句】提供的【File名称、position号】，【从库IO线程】向【主库】发起【binlog请求】
4. 【主库】的【dump线程】根据【从库】的【请求】，将【本地binlog】以【events的方式】发给【从库IO线程】
5. 【从库IO线程】接收【binlog】，并放在【本地relay-log】中
6. 【从库SQL线程】应用【本地relay-log】，并记录到【relay-log.info】中，默认情况下，已经应用过的relay会被自动清理。


## MySQL 为什么要主从同步？

1. 主从同步：主库写，从库读，即使主库【锁表】，【从库】仍然可以正常运行
2. 做【数据的备份】
3. 架构扩展：当业务量越来越大，多库存储，可以提高单个机器的IO性能

## MySQL InnoDB存储 の 文件结构

## Mysql自增主键一定是连续的吗

https://www.bilibili.com/video/BV1mq4y1W7VS

## Mysql 自增ID用完了怎么办

https://www.bilibili.com/video/BV1tX4y1c7y5



1.2 索引树是如何维护 の ？
1.3 数据库自增主键可能 の 问题
1.4 MySQL の 几种优化
1.5 mysql索引为什么使用B+树

1.1 mySQL里有2000w数据，redis中只存20w の 数据，如何保证redis中 の 数据都是热点数据

## mysql事务ACID特性，隔离级别

A 原子性：一个【事务】的【所有操作】要么全部完成，要么全部失败
C 一致性：比如，一次转账。某一账户扣除的金额，必须与另一个账户存入的金额，相等
I 隔离性：【事务】与【事务】之间互不影响
D 持久性：对数据的【修改】，必须在事务【结束】前，保存至某种【物理存储设备】。

## 隔离级别是什么？

包括：

- 具体规则，用于限定【事务】内外，哪些改变是可见的？哪些改变是不可见的？
- 低级别的隔离支持【更 high 的并发处理】

## Mysql的事务隔离级别

https://www.bilibili.com/video/BV1yh411671h

MySQL定义了4种隔离级别：

1. Read uncommitted 读取未提交的内容：不常用
   - 可以看到【未提交事务】的执行结果，称为“脏读”
   - 可能会产生很多问题
2. Read committed 读取提交的内容：大多数数据库的默认级别，但不是MySQL默认级别
   - 只能看见【已提交】事务所做的改变
   - 也支持所谓的“不可重复读”——意味着，运行同一个语句2次，看到的结果是不同的
3. Repeatable read 可重复读：MySQL默认级别
   - 优点：
     - 解决了Read uncommitted导致的问题
     - 保证【并发读取】时，看到【相同数据行】
   - 缺点：
     - 导致“幻读”，可以通过MVCC解决
4. Serializable 可串行化：最高级别隔离
   - 强制【事务排序】，使之不可能【相互冲突】，从而解决【幻读问题】
   - Serializable 是在每个读的【数据行】上加锁。
   - 缺点：
     - 导致大量【超时】【锁竞争】

https://www.bilibili.com/video/BV13t4y1p7Ht

https://www.bilibili.com/video/BV1Za411Y7SF

## 脏读、幻读、不可重复读

脏读：

- 可以读取【未提交事务】的结果

不可重复读：

- 读取了【another】【已提交】的事务
- 查询的是【同一个数据项】
- 多次查询，返回不同结果
- 原因是：事务执行过程中，【another事务】提交并修改了【当前事务】正在读取的数据。

幻读：

- 读取了【another】【已提交】的事务
- 查询的是【一批数据】
- 例如：
  - 事务1批量将【value为1】的数据修改为2，
  - 而事务2此时【插入】了一条【value为1】的数据，并完成提交
  - 事务1，发现，居然还有一条【value为1】的数据，没有修改



## 基于Redis和MySQL的架构，如何保证数据一致性

https://www.bilibili.com/video/BV1sV4y1n73t

## oracle和mysql の 默认事务隔离级别为什么不同



https://www.bilibili.com/video/BV11U4y1J7v7

项目中MySQL の 部署方式是什么，怎么保证数据库数据是高可用 の 
有考虑过数据库比如MySQL和MongoDB数据库数据丢失问题吗

## 为什么SQL语句命中索引比不命中索引要快？

https://www.bilibili.com/video/BV1jB4y1D7rt

## mysql隔离机制 默认 の 是什么

mysql【执行引擎】

说一下mysql常用 の 引擎

hash和数组分别作为mysql索引 の 结果。

## B+树是二叉树吗 平衡树吗

是【平衡】的【多叉树】

## B+树 の 特点。

从【根节点】到【叶子节点】的【高度差】 不超过1

同一层级：有指针连接

## 哈希索引

采用【哈希算法】，将【键值】换算成【哈希值】，检索时，不需要像【b+树】一样【逐级查找】。

所以，只要【一次哈希】就能定位到【响应的位置】，速度非常快。

如果key不唯一，则需要再根据【链表】，往后扫描

适合：

- 【等值查询】

不适合：

- 范围查询
- 无法排序
- like这样的模糊查询
- 多列联合索引的【最左匹配原则】

## b+树底层是双向链表还是单向

双向链表有2个指针，一个指针指向【前驱节点】，一个指针指向【后驱节点】，支持常量级别的时间复杂度，找到【前驱节点】。所以【双向链表】【插入、删除】比【单向链表】更加高效。

## innoDB如何解决幻读

https://www.bilibili.com/video/BV1dT4y1m74v

##  红黑树和跳表 の 区别

对于一个【数据库】来说，存储【数据的量】比较多的情况下，会导致【索引】很大，因此，需要将【索引】存储到【磁盘】，但磁盘的【IO操作】又非常低，所以，提高【索引效率】在于减少【磁盘IO】的次数。

举个椰子🥥：

对于 31 个节点的 tree。

一个 【5 阶的 b+ 树】的高度是 3。

一个 【红黑树】的【最小高度】是 5。

【树的高度】基本决定了【磁盘IO次数】，所以，使用【b+树】性能要高很多。

b+树中【相邻数据】在物理上，也是【相邻】的。因为【b+树】的【node大小】设为【一个页】，而每个【节点】存储多个【相邻关键字】和【分支信息】。所以每次查询，只需要【一次IO】就能完全载入【相邻信息】和【目标信息】。而【红黑树】不具有这个特征。

红黑树中，大小相邻的数据，在【物理结构】上，可能差距非常大。由于程序的【局部性原理】，我们在索引中采用了【预加载】技术，每次【磁盘访问】的时候，除了去访问【目标数据】以外，我们还可以基于【局部性原理】加载【几页相邻】的数据到内存。而这个加载，是不需要消耗多余的【磁盘IO】的。

因此，基于【局部性原理】和【b+树】存储结构物理上的特征，所以【b+树】的索引性能比【红黑树】好很多

##  mysql的【索引类型】有哪些？

https://www.bilibili.com/video/BV1cR4y1w7dc

- 普通索引
- 唯一索引
- 逐渐索引
- 联合索引
- 全文索引

## mysql中普通索引和唯一索引的区别

https://www.bilibili.com/video/BV1354y157oZ

## 实际项目中，我们选择【唯一索引】还是【普通索引】？

【唯一索引】和【普通索引】的不同点在于：

- 【唯一索引】：一旦找到【满足条件】的记录，就立即停止继续检索
- 【普通索引】：查找到满足条件的第一个记录后，还会继续查找下一个记录，until碰到第一个不满足条件的记录

这个差别对性能的影响极小。

真正能区分【唯一索引】和【普通索引】的是【Insert Buffer 和 Change Buffer】，他们只适用于【非唯一索引】：

- 当需要新【插入】一个数据的时候，先将这个【操作】存储到【Insert Buffer】，在下一次查询，需要访问这个数据的时候，【存储引擎】才会将其合并到【索引】中。也就是说——
- 多个【叶子节点】的插入操作，合并到一起，这就大大提高了【辅助索引】的【插入性能】
- 这个时候【非唯一索引】的性能，要优于唯一索引。

## 回表

https://www.bilibili.com/video/BV1tS4y1A7wJ

## 华为面试，已拿offer！

https://www.bilibili.com/video/BV1BZ4y1r7cU

## 最左匹配原则

https://www.bilibili.com/video/BV1av4y1A7r3