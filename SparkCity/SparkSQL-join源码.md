<!-- vscode-markdown-toc -->
* 1. [build table的选择](#buildtable)
* 2. [满足什么条件的表才能被广播-broadcastSide](#-broadcastSide)
* 3. [是否可构造本地HashMap](#HashMap)
	* 3.1. [Broadcast Hash Join](#BroadcastHashJoin)
	* 3.2. [Shuffle Hash Join](#ShuffleHashJoin)
	* 3.3. [Sort Merge Join](#SortMergeJoin)
	* 3.4. [Without joining keys](#Withoutjoiningkeys)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->

##  1. <a name='buildtable'></a>build table的选择

[参考链接](https://mp.weixin.qq.com/s/w9StfmI-1h4nAkpp3d-SZw)

我还没有看懂~~~~

Hash Join的第一步就是:

根据两表之中较小的那一个构建`哈希表`，

小表就叫做`build table`，

大表则称为`probe table`，

因为需要拿小表形成的哈希表来"探测"它。源码如下：

```scala
/* 左表作为build table的条件，join类型需满足：
   1. InnerLike：实现目前包括inner join和cross join
   2. RightOuter：right outer join
*/      
private def canBuildLeft(joinType: JoinType): Boolean = joinType match {
  case _: InnerLike | RightOuter => true
  case _ => false
}

/* 右表作为build table的条件，join类型需满足（第1种是在业务开发中写的SQL主要适配的）：
   1. InnerLike、LeftOuter（left outer join）、LeftSemi（left semi join）、LeftAnti（left anti join）
   2. ExistenceJoin：only used in the end of optimizer and physical plans, we will not generate SQL for this join type
*/
private def canBuildRight(joinType: JoinType): Boolean = joinType match {
  case _: InnerLike | LeftOuter | LeftSemi | LeftAnti | _: ExistenceJoin => true
  case _ => false
}
```

##  2. <a name='-broadcastSide'></a>满足什么条件的表才能被广播-broadcastSide

如果一个表的大小 `< 或 =` 参数 `spark.sql.autoBroadcastJoinThreshold`（默认10M）配置的值，那么就可以广播该表。源码如下：

```scala
private def canBroadcastBySizes(joinType: JoinType, left: LogicalPlan, right: LogicalPlan)
  : Boolean = {
  val buildLeft = canBuildLeft(joinType) && canBroadcast(left)
  val buildRight = canBuildRight(joinType) && canBroadcast(right)
  buildLeft || buildRight
}

private def canBroadcast(plan: LogicalPlan): Boolean = {
  plan.stats.sizeInBytes >= 0 && plan.stats.sizeInBytes <= conf.autoBroadcastJoinThreshold
}

private def broadcastSideBySizes(joinType: JoinType, left: LogicalPlan, right: LogicalPlan)
  : BuildSide = {
  val buildLeft = canBuildLeft(joinType) && canBroadcast(left)
  val buildRight = canBuildRight(joinType) && canBroadcast(right)

  // 最终会调用broadcastSide
  broadcastSide(buildLeft, buildRight, left, right)
}
```

除了通过上述表的大小满足一定条件之外，我们也可以通过直接在Spark SQL中显示使用`hint方式`

(/+ `BROADCAST`(small_table) /)

直接指定要广播的表，源码如下：

```scala
private def canBroadcastByHints(joinType: JoinType, left: LogicalPlan, right: LogicalPlan)
  : Boolean = {
  val buildLeft = canBuildLeft(joinType) && left.stats.hints.broadcast
  val buildRight = canBuildRight(joinType) && right.stats.hints.broadcast
  buildLeft || buildRight
}

private def broadcastSideByHints(joinType: JoinType, left: LogicalPlan, right: LogicalPlan)
  : BuildSide = {
  val buildLeft = canBuildLeft(joinType) && left.stats.hints.broadcast
  val buildRight = canBuildRight(joinType) && right.stats.hints.broadcast

  // 最终会调用broadcastSide
  broadcastSide(buildLeft, buildRight, left, right)
}
```

无论是通过`表大小`进行`广播`还是根据是否指定`hint进行表广播`，最终都会调用`broadcastSide`，来决定应该广播哪个表：

```scala
private def broadcastSide(
     canBuildLeft: Boolean,
     canBuildRight: Boolean,
     left: LogicalPlan,
     right: LogicalPlan): BuildSide = {

   def smallerSide =
     if (right.stats.sizeInBytes <= left.stats.sizeInBytes) BuildRight else BuildLeft

  if (canBuildRight && canBuildLeft) {
    // 如果左表和右表都能作为build table，则将根据表的统计信息，确定physical size较小的表作为build table（即使两个表都被指定了hint）
    smallerSide
  } else if (canBuildRight) {
     // 上述条件不满足，优先判断右表是否满足build条件，满足则广播右表。否则，接着判断左表是否满足build条件
    BuildRight
  } else if (canBuildLeft) {
    BuildLeft
  } else {
    // 如果左表和右表都不能作为build table，则将根据表的统计信息，确定physical size较小的表作为build table。目前主要用于broadcast nested loop join
    smallerSide
  }
}
```

从上述源码可知，即使用户指定了广播hint，实际执行时，不一定按照hint的表进行广播。

##  3. <a name='HashMap'></a>是否可构造本地HashMap

应用于Shuffle Hash Join中，源码如下：

```scala
// 逻辑计划的单个分区足够小到构建一个hash表
// 注意：要求分区数是固定的。如果分区数是动态的，还需满足其他条件
private def canBuildLocalHashMap(plan: LogicalPlan): Boolean = {
  // 逻辑计划的physical size小于spark.sql.autoBroadcastJoinThreshold * spark.sql.shuffle.partitions（默认200）时，即可构造本地HashMap
  plan.stats.sizeInBytes < conf.autoBroadcastJoinThreshold * conf.numShufflePartitions
}
```

SparkSQL目前主要实现了3种join：

- Broadcast Hash Join
- ShuffledHashJoin
- Sort Merge Join

那么Catalyst在处理SQL语句时，是依据什么规则进行join策略选择的呢？

###  3.1. <a name='BroadcastHashJoin'></a>Broadcast Hash Join

主要根据`hint`和`size`进行判断是否满足条件。

```scala
case ExtractEquiJoinKeys(joinType, leftKeys, rightKeys, condition, left, right)
   if canBroadcastByHints(joinType, left, right) =>
   val buildSide = broadcastSideByHints(joinType, left, right)
   Seq(joins.BroadcastHashJoinExec(
     leftKeys, rightKeys, joinType, buildSide, condition, planLater(left), planLater(right)))

// 未指定广播提示，因此需要从大小和配置中推断出它。
case ExtractEquiJoinKeys(joinType, leftKeys, rightKeys, condition, left, right)
   if canBroadcastBySizes(joinType, left, right) =>
   val buildSide = broadcastSideBySizes(joinType, left, right)
   Seq(joins.BroadcastHashJoinExec(
     leftKeys, rightKeys, joinType, buildSide, condition, planLater(left), planLater(right)))
```

###  3.2. <a name='ShuffleHashJoin'></a>Shuffle Hash Join

选择`Shuffle Hash Join`需要同时满足以下条件：

1. `spark.sql.join.preferSortMergeJoin`为false，即Shuffle Hash Join优先于Sort Merge Join

2. 右表或左表是否能够作为`build table`

3. 是否能构建本地`HashMap`

4. 以右表为例，它的`逻辑计划大小`要远小于`左表大小`（默认3倍）

上述条件优先检查右表。

```scala
case ExtractEquiJoinKeys(joinType, leftKeys, rightKeys, condition, left, right)
    if !conf.preferSortMergeJoin && canBuildRight(joinType) && canBuildLocalHashMap(right)
      && muchSmaller(right, left) ||
      !RowOrdering.isOrderable(leftKeys) =>
   Seq(joins.ShuffledHashJoinExec(
     leftKeys, rightKeys, joinType, BuildRight, condition, planLater(left), planLater(right)))

case ExtractEquiJoinKeys(joinType, leftKeys, rightKeys, condition, left, right)
     if !conf.preferSortMergeJoin && canBuildLeft(joinType) && uildLocalHashMap(left)
       && muchSmaller(left, right) ||
       !RowOrdering.isOrderable(leftKeys) =>
    Seq(joins.ShuffledHashJoinExec(
      leftKeys, rightKeys, joinType, BuildLeft, condition, planLater(left), planLater(right)))

private def muchSmaller(a: LogicalPlan, b: LogicalPlan): Boolean = {
  a.stats.sizeInBytes * 3 <= b.stats.sizeInBytes
}
```

如果不满足上述条件，但是如果参与join的表的key无法被排序，即无法使用`Sort Merge Join`，最终也会选择`Shuffle Hash Join`。

```scala
!RowOrdering.isOrderable(leftKeys)

def isOrderable(exprs: Seq[Expression]): Boolean = exprs.forall(e => isOrderable(e.dataType))
```

###  3.3. <a name='SortMergeJoin'></a>Sort Merge Join

如果上面两种join策略（`Broadcast Hash Join`和`Shuffle Hash Join`）都不符合条件，

并且`参与join的key`是`可排序的`，

就会选择`Sort Merge Join`。

```scala
case ExtractEquiJoinKeys(joinType, leftKeys, rightKeys, condition, left, right)
   if RowOrdering.isOrderable(leftKeys) =>
   joins.SortMergeJoinExec(
     leftKeys, rightKeys, joinType, condition, planLater(left), planLater(right)) :: Nil
```

###  3.4. <a name='Withoutjoiningkeys'></a>Without joining keys

`Broadcast Hash Join`、`Shuffle Hash Join`和`Sort Merge Join`都属于经典的`ExtractEquiJoinKeys（等值连接条件）`。

对于`非ExtractEquiJoinKeys`，则会优先检查表`是否可以被广播（hint或者size）`。

如果可以，则会使用`BroadcastNestedLoopJoin（简称BNLJ）`，

熟悉`Nested Loop Join`则不难理解`BNLJ`，主要却别在于`BNLJ`加上了`广播表`。

源码如下：

```scala
// 如果可以广播一侧，则选择 BroadcastNestedLoopJoin
case j @ logical.Join(left, right, joinType, condition)
    if canBroadcastByHints(joinType, left, right) =>
  val buildSide = broadcastSideByHints(joinType, left, right)
  joins.BroadcastNestedLoopJoinExec(
    planLater(left), planLater(right), buildSide, joinType, condition) :: Nil

case j @ logical.Join(left, right, joinType, condition)
    if canBroadcastBySizes(joinType, left, right) =>
  val buildSide = broadcastSideBySizes(joinType, left, right)
  joins.BroadcastNestedLoopJoinExec(
    planLater(left), planLater(right), buildSide, joinType, condition) :: Nil
```

如果表不能被`广播`，又细分为两种情况：

若join类型`InnerLike`（关于InnerLike上面已有介绍）对量表直接进行`笛卡尔积`处理

若

上述情况都不满足，最终方案是选择两个表中`physical size较小`的表`进行广播`，

join策略仍为`BNLJ`

源码如下：

```scala
// 为 InnerJoin内连接 选择 CartesianProduct笛卡尔积 
case logical.Join(left, right, _: InnerLike, condition) =>
  joins.CartesianProductExec(planLater(left), planLater(right), condition) :: Nil

case logical.Join(left, right, joinType, condition) =>
  val buildSide = broadcastSide(
    left.stats.hints.broadcast, right.stats.hints.broadcast, left, right)
  // 此加入可能非常慢或 OOM
  joins.BroadcastNestedLoopJoinExec(
    planLater(left), planLater(right), buildSide, joinType, condition) :: Nil
```