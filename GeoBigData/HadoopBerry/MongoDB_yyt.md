
# 1. 面试

## 1.1. mongodb 与mysql 的区别？

与mysql的区别在于它不会**遵循一些约束**，

比如：

:maple_leaf: **sql**标准

:maple_leaf: **ACID**属性

:maple_leaf: **表结构**

其主要特性如下：

:maple_leaf: 面向**集合文档**的存储：适合存储**Bson（json的扩展）形式**的数据；

:maple_leaf: 格式**自由**，数据格式**不固定**，**生产环境下修改结构**都可以不影响程序运行；

:maple_leaf: **强大的查询**语句，面向对象的查询语言，基本覆盖sql语言所有能力；

:maple_leaf: 完整的**索引支持**，支持查询计划；

:maple_leaf: 支持**复制**和**自动故障转移**；

:maple_leaf: 支持**二进制数据**及**大型对象（文件）**的高效存储；

:maple_leaf: 使用**分片集群**提升系统**扩展性**；

:maple_leaf: 使用**内存映射**存储引擎，把磁盘的**IO操作**转换成为内存的操作；

## 1.2. mongoDB 主要使用在什么应用场景？

MongoDB 的应用已经渗透到各个领域，比如游戏、物流、电商、内容管理、社交、物联网、视频直播等，以下是几个实际的应用案例：

:maple_leaf: 游戏场景，使用 MongoDB 存储游戏用户信息，用户的装备、积分等直接以内嵌文档的形式存储，方便查询、更新

:maple_leaf: **物流场景**，使用 **MongoDB 存储订单信息**，订单状态在运送过程中会不断更新，以MongoDB **内嵌数组**的形式来存储，一次查询就能将**订单所有的变更**读取出来。

:maple_leaf: 社交场景，使用 MongoDB 存储存储用户信息，以及用户发表的**朋友圈信息**，通过**地理位置**索引实现**附近的人、地点**等功能

:maple_leaf: **物联网场景**，使用 MongoDB 存储所有接入的**智能设备信息**，以及设备汇报的**日志信息**，并对这些信息进行多维度的分析

:maple_leaf: 视频直播，使用 MongoDB 存储用户信息、礼物信息等

## 1.3. 怎么样做 mongodb 查询优化

### 1.3.1. :maple_leaf: 第一步 **找出**慢速查询

开启内置的查询分析器,记录读写操作效率:

```js
db.setProfilingLevel(n,{m})
```
 
n的取值：可选0,1,2

:ghost: 0是**默认值**表示**不记录**；

:ghost: 1表示**记录慢速操作**,

> 如果值为1,**m**必须**赋值单位**为**ms**,用于定义**慢速查询时间的阈值**；

例如:

```js
db.setProfilingLevel(1,300)
```

:ghost: 2表示**记录所有的读写操作**；

查询**监控结果**:

**监控结果**保存在一个特殊的**盖子集合system.profile**里,

这个集合分配了**128kb的空间**,

要确保**监控分析数据**不会消耗太多的**系统性资源**；

**盖子集合**维护了自然的**插入顺序**,

可以使用

```js
$natural
```

操作符进行排序,如:

```js
db.system.profile.find().sort({'$natural':-1}).limit(5)
```

### 1.3.2. :maple_leaf: 第二步 **分析**慢速查询

找出**慢速查询**的原因比较棘手,原因可能有多个:

> 应用**程序设计**不合理
> 不正确的**数据模型**
> **硬件配置**问题
> 缺少**索引**

接下来对于缺少索引的情况进行分析:

使用`explain`分析慢速查询

例如:

```js
db.orders.find({'price':{'$lt':2000}}).explain('executionStats')
```

explain的入参可选值为:

- "queryPlanner" 是**默认值**
  
  表示仅仅展示**执行计划信息**；

- "executionStats"
  
  表示展示**执行计划信息**
  
  同时展示**被选中的执行计划**的执行情况信息；

- "allPlansExecution"
  
  表示展示**执行计划信息**
  
  并展示**被选中的执行计划**的执行情况信息
  
  还展示**备选**的执行计划的执行情况信息；

### 1.3.3. :maple_leaf: 第三步 **解读**explain结果

queryPlanner（执行计划描述）：

- winningPlan（**被选中的执行计划**）
  - stage（可选项:COLLSCAN 没有走索引；IXSCAN使用了索引）
- rejectedPlans(**候选**的执行计划)
  
executionStats(执行情况描述)：

- nReturned （返回的文档个数）
- executionTimeMillis（执行时间ms）
- totalKeysExamined （检查的**索引键值**个数）
- totalDocsExamined （检查的**文档**个数）

备注:

1. 根据需求建立**索引**
2. 每个查询都要使用**索引**以提高查询效率
   winningPlan
   stage 必须为IXSCAN
3. 追求totalDocsExamined = nReturned

## 1.4. mongodb 的**索引**注意事项？

- **索引**很有用,但是它也是有成本的
  ——它占内存,让写入变慢；
  
- mongoDB通常在一次查询里使用一个**索引**,
  所以多个字段的查询或者排序需要**复合索引**才能更加高效；

- **复合索引**的
  顺序非常重要

- 在生成环境构建**索引**往往开销很大,时间也不可以接受,
  在数据量庞大之前尽量进行**查询优化**和构建**索引**；

- 避免昂贵的查询,
  使用**查询分析器**记录那些开销很大的查询便于问题排查

- 通过减少扫描**文档数量**来优化查询,
  使用explain对开销大的查询进行分析并优化；

- **索引**是用来查询**小范围数据**的，
  
## 1.5. 不适合使用**索引**的情况：

- 每次查询都需要返回大部分数据的**文档**，避免使用**索引**
  
- 写比读多
