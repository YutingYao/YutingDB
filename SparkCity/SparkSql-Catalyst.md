

## Catalyst概述

[参考链接](https://mp.weixin.qq.com/s/c58NvwsvAbK0c4yllQPX5w)

本文为机器翻译，所以不太容易看懂。不过~~~ 知道Catalyst是用来优化的就够了。👀

Catalyst可扩展的设计有两个目的。

首先，希望能够`轻松地`向Spark SQL添加新的优化技术和功能，

- 特别是为了解决大数据（例如，`半结构化数据`和`高级分析`）所遇到的各种问题。

第二，我们希望使`外部开发人员`能够扩展`优化器` 

- 例如，通过添加可将`过滤或聚合`推送到`外部存储系统`的数据源特定规则，或支持新的`数据类型`。

> Catalyst支持`基于规则(rule-based)`和`基于成本(cost-based)`的优化。

其核心是:

Catalyst包含一个用于表示`树`并应用规则来操纵它们的`通用库`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.39wb1vs67l40.png)

在`框架的顶层`，构建了特定针对`关系查询处理`的库:

- 表达式(expressions)，
- 逻辑查询计划(logical query plans)

也有一系列的处理`不同层次`查询执行的`规则`。总共有四个层次：

- analysis(语法分析)
- logical optimization(逻辑优化)，
- physical planning(物理计划)，和
- 代码生成
  
  即将请求查询编译成`java字节码`。对于后者，我们使用另一个scala特性，`quasiquotes`，使得在运行的过程中从`组合表达式`产生代码更简单。最后，`Catalyst`提供一些`公共扩展点`，包括`外部数据源`和`用户自定义类`型。

## 语法树

Catalyst 的主要数据类型就是有`节点对象`组成的`树`。

每个node都有一个`node类型`和零个或者多个子节点。

Scala中新定义的`node类型`是`TreeNode类`的子类。

这些对象都是`不可改变的`，可以使用`函数转换`来操作。

举一个简单的例子，针对一个非常简单的expression我们总共有下面三种node类型:

```scala
Literal(value: Int) // 一个常量

Attribute(name: String) // 输入行的一个列属性，例如：“x”

Add(left: TreeNode, right: TreeNode) // 两个expressions求加
```

这些类可以用来构建一棵树。举例，x+(1+2)，这个表达式，在scala代码中就如下：

```scala
Add(Attribute(x), Add(Literal(1), Literal(2)))
```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3e03nil747s0.png)

## 规则

可以使用规则来操纵树，这些规则是从一颗树到另一棵树的转换函数。

虽然一个规则可以在其输入树上运行任意代码（给定这个树只是一个Scala对象），

但最常见的方法是使用一组`模式匹配函数`来查找和替换子树为特定结构。

`模式匹配`是许多函数编程语言的特征，允许从代数数据类型的`潜在嵌套结构`中提取值。

在Catalyst中，语法树提供了一种转换方法，可以在树的所有节点上递归地应用`模式匹配函数`，将匹配到的节点转换为特定结果。

例如，我们可以实现一个在常量之间进行`Add操作`的规则，如下所示：

```scala
tree.transform {
  case Add(Literal(c1), Literal(c2)) => Literal(c1+c2)
}
```

将这个规则应用到`x+(1+2)`这棵语法树上，就会产生一棵新的树，`x+3`。

`Case关键词`是scala的标准模式匹配的语法，可以用来`匹配一个节点类型`，同时将`名字`和`抽取到的值`对应。（就是c1和c2）。

`模式匹配`的表达式是部分函数，这也意味着只需要匹配到输入语法树的子集。

Catalyst将测试给定规则`适用`的树的哪些部分，自动跳过`不匹配的子树`。

这种能力意味着: 规则只需要对给定优化适用的树进行推理，而不是那些不匹配的树。

结果就是，`新的操作`类型加入到系统时`规则无需修改`。

规则（和Scala模式匹配一般）可以匹配相同转换调用中的多个模式，使其非常简洁，可以一次实现多个转换：

```scala
tree.transform {
  case Add(Literal(c1), Literal(c2)) => Literal(c1+c2)
  case Add(left, Literal(0)) => left
  case Add(Literal(0), right) => right
}
```

为了完全转换一棵树规则往往需要执行多次。

`Catalyst`会将`规则分组`，

在`达到稳定点之前`会一直执行当前组的规则，

fixed point的意思也就是在使用`当前组的规则树不会再变化`了。

将规则运行到fixed point意味着每个规则可以简单的，但仍然最终对树有更大的全局影响。

在上面的例子中，`重复应用规则`会使较大的树（例如（x + 0）+（3 + 3））达到一个稳定的状态。

另一个例子:

第一批可以分析表达式以将类型分配给所有属性，

而第二批可能使用这些类型来执行`常量折叠(合并)`。

每个批次后，开发人员还可以在新树上进行合理检查（例如，看看是否所有属性都是分配类型了），通常也通过`递归匹配`来编写。

最后，`规则`条件及其本身可以包含任意的Scala代码。

这使得Catalyst比优化器的域特定语言更强大，同时`保持简洁的简单规则`。

在经验中，对`不变树的功能转换`使得整个`优化器`非常容易`推理和调试`。

它们还可以在优化器中实现并行化，尽管目前还没有开发它。

## 在Sparksql中使用Catalyst

在四个层面，可以使用Catalyst通用树的转换框架，如下：

(1) 分析一个逻辑计划，解析引用，也即unresolved logical plan转化为logical plan。

(2) 逻辑计划优化。

(3) 物理计划。

(4) 代码生成。将query转化为java字节码。

在`物理计划层`，Catalyst也许会产生多个物理计划，

然后`根据cost`进行选择。

`其它层`都是单纯的`基于规则`的优化。

每个层使用不同的`树节点类型`。

下面开始详细介绍每个层次。

### 语法解析-Analysis

在SQL查询`SELECT col FROM sales`，`col`的类型，甚至是否是`有效的列名称`，直到我们查找sales表前都是不知道的

Spark SQL使用`Catalyst规则`和`Catalog对象`来跟踪所有数据源中的表以解析这些属性。

首先构建一个具有`未绑定属性和数据类型的树(unresolved logical plan)`。

然后应用执行以下操作的规则：

1) 通过name从catalog中查找`relations`。

2) 将命名的属性（如“`col`”）映射到给定操作符的子节点的输入中。

3) 确定哪些属性引用相同的值，以便给它们一个唯一的ID（稍后允许对表达式进行优化(如 `col = col`)

4) 在`expressions`中传播和强制类型：例如，我们不能知道1 + col的返回类型，直到我们解析col并且可能将其子表达式转换为兼容类型。

### 逻辑优化-Logical Optimizations

基于规则的优化包括`常量合并`，`谓词下推`，`列裁剪`，`null propagation`，`boolean表达式简化`，和其它的规则

比如，我们想为SparkSql增加一个固定精度的`DECIMAL类型`，我们想优化`聚合规则`，比如sum 和average均值。它只需要`12行代码`来编写一个在SUM和AVG表达式中找到这样的小数的规则，并将它们转换为`未缩放的64位长整型`，然后将聚合后的结果类型转换回来。

求和的表达式实现如下：

```scala
object DecimalAggregates extends Rule[LogicalPlan] {
  /** Long 中的最大十进制位数 */
  val MAX_LONG_DIGITS = 18
  def apply(plan: LogicalPlan): LogicalPlan = {
    plan transformAllExpressions {
      case Sum(e @ DecimalType.Expression(prec, scale))
        if prec + 10 <= MAX_LONG_DIGITS =>
        MakeDecimal(Sum(UnscaledValue(e)), prec + 10, scale) }
  }
```

### 物理计划-Physical Planning

基于cost-based的优化仅仅用于选择join算法

使用spark的提供的点对点的广播功能实现Broadcast join

支持更广泛地使用cost-based的优化

### 代码生成-Code Generation-quasiquotes

一个简单的例子，结合`Add，Attribute，Literal`树节点，这些表达式必须通过`树`来解释每行数据。这引入了`大量的分支`和`虚拟函数`调用，从而减慢了执行速度。

使用代码生成，我们可以编写一个函数来将`特定表达式树`转换为`Scala AST`，如下所示：

```scala
def compile(node: Node): AST = node match {
  case Literal(value) => q"$value"
  case Attribute(name) => q"row.get($name)"
  case Add(left, right) => q"${compile(left)} + ${compile(right)}"
}
```

Catalyst依靠Scala语言，名为`quasiquotes`的特殊功能，使代码生成更简单。

Quasiquotes允许用Scala语言编程构建`抽象语法树（AST）`，

然后可以在运行时将其提供给`Scala编译器`以生成`字节码`。

我们使用Catalyst将表示`SQL中的表达式的树`转换为`Scala代码的AST`，以评估该表达式，

然后编译并运行生成的代码。

以q开头的字符串是`quasiquote`，这意味着尽管它们看起来像字符串，但它们在编译时由`Scala编译器解析`，代表了`代码的AST`。

Quasiquotes可以将变量或其他AST引用到它们中，使用`$符号`开头。

例如，
- Literal（1）将成为1的Scala AST
- Attribute（“x”）变为row.get（“x”）
- Add（Literal（1），Attribute（“x”））变为 1 + row.get（“x”）这样的Scala表达式的AST。

`Quasiquotes`在编译时进行`类型检查`，以确保仅替换适当的AST或literals ，使其比`字符串连接`更可用，并且它们直接生成`Scala AST`，而不是在运行时运行`Scala解析器`。

此外，它们是高度可组合的，因为每个节点的代码生成规则不需要知道如何构建其子节点返回的树。

最后，`Scala编译器`进一步优化了最终的代码，以防止Catalyst错过了表达式优化。

