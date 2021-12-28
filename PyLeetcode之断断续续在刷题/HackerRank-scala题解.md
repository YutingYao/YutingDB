

### Hello World N Times

```scala
object Solution extends App {

  var n = scala.io.StdIn.readInt
  f(n)
}
```

```scala
  def f(n: Int) = for (i <- 0 until n) println("Hello World")
  注意：to 和 until 的区别
  def f(n: Int) = for (i <- 1 to n) println("Hello World")
```

### List Replication 复制

```s
Sample Input

3
1
2
3
4
Sample Output

1
1
1
2
2
2
3
3
3
4
4
4
```

```scala
object Solution extends App{

    def displayResult(arr:List[Int]) = println(f(arr(0).toInt, arr.drop(1)).map(_.toString).mkString("\n"))
    displayResult(io.Source.stdin.getLines.toList.map(_.trim).map(_.toInt))
}
```

```scala
    def f(repeat:Int, arr:List[Int]) = arr.flatMap(i => List.fill(repeat)(i))
    d等价于
    def f(repeat:Int, arr:List[Int]) = arr.flatMap(List.fill(repeat)(_))
```

### Filter Array

这个挑战的目的是学习如何编写自己的过滤器函数的实现。我们建议不要使用内置库函数。

```s
Sample Input

3
10
9
8
2
7
5
1
3
0
Sample Output

2
1
0
```

```scala
object Solution extends App {

  var lines = io.Source.stdin.getLines.toList
  println(f(lines(0).toInt,lines.map(_.trim).map(_.toInt)).map(_.toString).mkString("\n"))
}
```

```scala
  def f(upp:Int, arr:List[Int]):List[Int] = arr.filter( _ < upp )
  等价于：
  def f(upp:Int, arr:List[Int]):List[Int] = arr.filter( i => i < upp )

```

### Filter Positions in a List

```s
Sample Input

2
5
3
4
6
7
9
8
Sample Output 保留第二元素

5
4
7
8
```

```scala
object Solution extends App {

  println(f(io.Source.stdin.getLines.toList.map(_.trim).map(_.toInt)).mkString("\n"))
}
```

```scala

def f(arr:List[Int]):List[Int] = {
  arr.zipWithIndex.filter(i => i._2 % 2 != 0).map(i => i._1) // 过滤不是第二个元素的元素
}

def f(arr:List[Int]):List[Int] = {
  arr.zipWithIndex.filter( _._2 % 2 != 0).map( _._1)
}


def f(arr: List[Int]):List[Int] = arr.grouped(2).flatMap(_.tail) 不通过
```

[Scala--基础--模式匹配(match case)](https://www.cnblogs.com/p---k/p/8583914.html)

```scala
def f(arr:List[Int]):List[Int] = arr match{
  case odd::even::tail => even :: f(tail)
  case _ => Nil
}

def f(arr:List[Int]):List[Int] = arr match{
  case odd::even::tail => even :: f(tail)
  case _ => List()
}

def f(arr:List[Int]):List[Int] = arr match{
  case Nil => Nil
  case _::Nil => Nil
  case odd::even::tail => even :: f(tail)
  case _ => List()
}
```


### Sum of Odd Elements 奇数求和

```s
Sample Input

3
2
4
6
5
7
8
0
1
Sample Output

16
```

```scala
保留奇数：
def f(arr: List[Int]): Int = arr.filter(_ % 2 != 0).reduce(_+_)
def f(arr: List[Int]): Int = arr.filter(_ % 2 != 0).sum
def f(arr: List[Int]): Int = arr.filter(_.abs % 2 == 1).sum
def f(arr: List[Int]): Int = arr.filter(x => (x % 2).abs == 1).sum
过滤掉偶数：
def f(arr: List[Int]): Int = arr.filterNot(_ % 2 == 0).sum
```


```scala
def f(arr: List[Int]): Int = arr match {
    case x::tail => (if (x%2 == 0) 0 else head) + f(tail)
    case Nil => 0
}
```

### List Length

```s
Sample Input

2
5
1
4
3
7
8
6
0
9
Sample Output

10
```

```scala
def f(arr: List[Int]):Int = arr.length
def f(arr: List[Int]):Int = arr.size
```

```scala
def foldLeft(B 的 start value)((B, A) => B) 返回 B
将二元运算符应用于起始值和此序列的所有元素，从左到右。

def f(arr: List[Int]):Int = arr.foldLeft(0)((count , _) => count + 1)
def f(arr: List[Int]):Int = arr.foldLeft(0)((count , b) => count + 1)
def f(arr: List[Int]):Int = arr.foldRight(0){(_ , count) => count + 1} // 中括号和小括号可以互换
def f(arr: List[Int]):Int = arr.fold(0) ((_ , count) => count + 1) // 这么写是错误的，为什么呢？🆒
def f(arr: List[Int]):Int = arr.fold(0) ((count, _) => count + 1)
```

```scala
def f(arr: List[Int]) : Int = arr match {
    case Nil => 0
    case _::xs => 1 + f(xs)
}

def f(arr: List[Int]) : Int = arr match {
    case Nil => 0
    case x::xs => 1 + f(xs)
}

def f(arr: List[Int]) : Int = arr match {
    case Nil => 0
    case x::tail => 1 + f(tail)
}

-------------------------------------------------------------------

def f(arr: List[Int]):Int = {
    if (arr.isEmpty)
        0
    else
        1 + f(arr.tail)
}

-------------------------------------------------------------------

def f(arr: List[Int]):Int = if (arr.isEmpty) 0 else 1 + f(arr.tail)
```

### Update List

```s
Sample Input

2
-4
3
-1
23
-4
-54
Sample Output

2
4
3
1
23
4
54
```

```scala
def f(arr: List[Int]):List[Int] =  arr match {
    case head::tail if head < 0 => -head::f(tail)
    case head::tail => head::f(tail)
    case Nil => Nil
}  

def f(a: List[Int]) = a.map(_.abs)
def f(a: List[Int]) = a.map(x => x.abs)
```

### Evaluating e^x

```s
Sample Input

4
20.0000
5.0000
0.5000
-0.5000
Sample Output

2423600.1887
143.6895
1.6487
0.6065
```



```scala
def scan[B >: A](z: B)(op: (B, B) => B): Seq[B]
计算集合元素的前缀扫描。

Note: The neutral element z may be applied more than once.

B
element type of the resulting collection

z
操作员 op 的中性元素

op
the associative operator for the scan

returns
a new sequence containing the prefix scan of the elements in this sequence

Definition Classes
IterableOps
```

### Reverse a List

```s
Sample Input

19
22
3
28
26
17
18
4
28
0
Sample Output

0
28
4
18
17
26
28
3
22
19
```

```scala
def f(arr: List[Int]):List[Int] = arr.reverse

def f(arr: List[Int]):List[Int] = arr.foldLeft(List[Int]()) { (left, right) => right :: left }
等价于
def f(arr: List[Int]):List[Int] = (List[Int]() /: arr) {(left, right) => right :: left}

def f(arr: List[Int]):List[Int] = if(arr.isEmpty) List() else f(arr.tail) ::: List(arr.head) 
def f(arr: List[Int]):List[Int] = if(arr.size==1) List(arr.head) else f(arr.tail):::List(arr.head)
def f(arr: List[Int]):List[Int] = (if(arr.size > 1) f(arr.tail) else arr.tail) :+ arr.head
```

[scala中:: , +:, :+, :::, +++的区别](https://segmentfault.com/a/1190000005083578)

[Scala Vector fold syntax (/: and :\ and /:\)](https://stackoverflow.com/questions/7339618/scala-vector-fold-syntax-and-and)

```scala
def f(arr:List[Int]):List[Int] = arr match{
  case Nil => Nil
  case Nil => List();
  case head::Nil => List(head)
  case head::tail => f(tail) :+ head
  case head::tail => f(tail) ++ List(head)
  case head::tail => f(tail) ::: (head::Nil)
  case head::tail => f(tail) ::: List(head) 
}
```

