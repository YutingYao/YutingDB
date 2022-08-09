
## Mysql表时间列用 datetime 还是 timestamp ？

个人认为用datetime更好。

虽然 timestamp 是 `四字节`，但一旦存储毫秒就会变成 7 字节，和datetime的8字节相差无几。

虽然 timestamp 可以 `解决时区问题`，但 还是通过前后端的转换更好，可以减轻数据库的压力。在高并发情况下，可能出现`性能抖动问题`。

## 数据库锁表 の 相关处理

## Mysql锁有哪些，如何理解

https://www.bilibili.com/video/BV1ff4y1Z7VQ

## MySQL性能优化

https://www.bilibili.com/video/BV1C3411x7yU



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


## 数据库视图

## 数据库 の 三大范式，都有哪些区别，举例说明

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
- SQL和NoSQL区别

## 分布式ID有哪些解决方案？

https://www.bilibili.com/video/BV19r4y1L7cZ

https://www.bilibili.com/video/BV1ea411H7RR

## 什么是事务

https://www.bilibili.com/video/BV1RS4y1d7kr

## Mysql事务的实现原理

https://www.bilibili.com/video/BV1sg41197Gm

## mysql索引是什么结构，索引存储int和字符串有什么区别（不会）

mysql索引设计原则

## 说说 MySQL  の 索引？b树与b+树 の 区别？

https://www.bilibili.com/video/BV1TF411u7f8

https://www.bilibili.com/video/BV1534y1a7bq

https://www.bilibili.com/video/BV1oP4y1M7ib

## Mysql数据库中，什么情况下设置了索引但⽆法使用



## mysql索引

hash索引 + B+树

## mysql の 引擎

## Innodb是如何实现事务的

https://www.bilibili.com/video/BV1hT4y1v7Up

innoDB

mysql の 引擎和innodb这类数据库 の 区别

## mysql索引相关 の 问题，b+树

[讲讲mysql の 索引及B树和B+树](https://www.bilibili.com/video/BV1Dq4y1S7GZ)

https://www.bilibili.com/video/BV18U4y197Dx

## MySQL 索引，存数据库，还是存磁盘

## 【聚簇索引】和【非聚簇索引】如何区别？

https://www.bilibili.com/video/BV1o3411J7Mh

只需要判断【数据】和【索引】是否存在一起

## mySQL是【聚簇索引】，还是【非聚簇索引】？

与【存储引擎】相关

innoDB：都有。

- `xxx.ibd`包含【数据】 + 【索引】
- 建立【聚簇索引】占用的空间更大，一旦【聚簇索引】改变，那么【非聚簇索引】也要跟着变

myisam：只有【非聚簇索引】

- `xxx.myd`包含【数据】
- `xxx.myi`包含【索引】

## 你们 ADS 层 の 数据量每天 の 数据量有多大？ADS 层再 MySQL 中 の 表是怎么创建 の ？有什么注意事项？索引怎么创建 の ？

1.1 MySql の 存储引擎 の 不同

1.2 Mysql怎么分表，以及分表后如果想按条件分页查询怎么办(如果不是按分表字段来查询 の 话，几乎效率低下，无解)

## MySql の 主从实时备份同步 の 配置，以及原理(从库读主库 の binlog)，读写分离

## MySQL 为什么要主从同步？

1. 主从同步：主库写，从库读，即使主库【锁表】，【从库】仍然可以正常运行
2. 做【数据的备份】
3. 架构扩展：当业务量越来越大，多库存储，可以提高单个机器的IO性能

1.1 MySQL InnoDB存储 の 文件结构
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

## Mysql的事务隔离级别

https://www.bilibili.com/video/BV1Za411Y7SF

## oracle和mysql の 默认事务隔离级别为什么不同



https://www.bilibili.com/video/BV11U4y1J7v7

项目中MySQL の 部署方式是什么，怎么保证数据库数据是高可用 の 
有考虑过数据库比如MySQL和MongoDB数据库数据丢失问题吗

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

- 普通索引
- 唯一索引
- 逐渐索引
- 联合索引
- 全文索引


