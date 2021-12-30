
## 简单题

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
```

```scala
def f(x: Double):Double = 1 + List(2,3,4,5,6,7,8,9.0).scan(x)((ini,cur) => x*ini/cur).sum

// 完整版：
object Solution {

def f(x: Double):Double = 1 + List(2,3,4,5,6,7,8,9.0).scan(x)((ini,cur) => x*ini/cur).sum
def main(args: Array[String]) {
       var n = 4
       (1 to n) foreach(x=> println(f(20)))
    }
}
```

```scala
def factorial(n: Int): Int = (1 to n).product
def f(x: Float):Float = (List.range(0, 10) map(p => math.pow(x,p)/fact(p))).sum.toFloat

// 完整版：
object Solution {

def factorial(n: Int): Int = (1 to n).product
def f(x: Float):Float = (List.range(0, 10) map(p => math.pow(x,p)/factorial(p))).sum.toFloat

def main(args: Array[String]) {
       var n = 4
       (1 to n) foreach(x=> println(f(20)))
    }
}
```

## 复杂题

### Area Under Curves and Volume of Revolving a Curve

<https://www.hackerrank.com/challenges/area-under-curves-and-volume-of-revolving-a-curv/problem>

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1x8qb87o7wjk.webp)

[scala里的List/Stream/View机制浅析](https://blog.csdn.net/tlxamulet/article/details/78305652)

```scala
object Solution {

    def f(coefficients: List[Int], powers: List[Int], x: Double): Double = 
         ------------------------------------------------------------------------------
        coefficients.zip(powers).map{ case (coeff, p) =>
          coeff * math.pow(x, p)
        }.reduce(_ + _)
         ------------------------------------------------------------------------------
        (coefficients.view zip powers map (e => e._1 * math.pow(x, e._2))).sum
         ------------------------------------------------------------------------------
        (coefficients zip powers).map(t => t._1 * pow(x, t._2)).sum
         ------------------------------------------------------------------------------
        ((coefficients, powers).zipped map {(coeff,p) => coeff * Math.pow(x, p)}).sum
         ------------------------------------------------------------------------------
        (coefficients zip powers) map {case (coeff, p) => coeff * math.pow(x, p)} sum
         coefficients.zip(powers).map {case (coeff, p) => coeff * math.pow(x, p)}.sum
         ------------------------------------------------------------------------------
         ------------------------------------------------------------------------------
        coefficients.zip(powers).foldLeft(0.0) { case (sum, (coeff, p)) => sum + coeff * math.pow(x, p) }
         ------------------------------------------------------------------------------
        (for (i <- 0 until coefficients.size) yield coefficients(i) * Math.pow(x, powers(i))).sum
         ------------------------------------------------------------------------------
        (0 until coefficients.size).foldLeft(0.0) { (sum: Double, i: Int) => sum + coefficients(i) * Math.pow(x, powers(i)) }
         ------------------------------------------------------------------------------
         ------------------------------------------------------------------------------
        val y = (coefficients, powers).zipped map { _ * scala.math.pow(x, _) } sum
        y * 0.001
         ------------------------------------------------------------------------------

    def area(coefficients: List[Int], powers: List[Int], x: Double): Double = 
         ------------------------------------------------------------------------------
        Pi*pow(f(coefficients, powers, x), 2)
        math.Pi * math.pow(f(coefficients, powers, x), 2)
         ------------------------------------------------------------------------------
        3.14159265359 * f(coefficients, powers, x) * f(coefficients, powers, x)
        3.14159265 * f(coefficients, powers, x) * f(coefficients, powers, x)
         ------------------------------------------------------------------------------
         ------------------------------------------------------------------------------
        math.Pi * math.pow(f(coefficients, powers, x), 2)
       math.Pi * math.pow(f(coefficients, powers, x), 2)
         ------------------------------------------------------------------------------
         ------------------------------------------------------------------------------
        def square(y: Double) = y * y
        square(f(coefficients, powers, x)) * Math.PI
         ------------------------------------------------------------------------------

    def summation(
        func: (List[Int], List[Int], Double) => Double,
        upperLimit: Int,
        lowerLimit: Int,
        coefficients: List[Int],
        powers: List[Int]): Double = 
         ------------------------------------------------------------------------------
         最简洁的写法：
        lowerLimit.toDouble.to(upperLimit.toDouble).by(0.001).map(func(coefficients,powers,_)).sum * 0.001
         ------------------------------------------------------------------------------
        val low = lowerLimit * 1.0
        (low to (upperLimit, 0.001)) map { x => func(coefficients, powers, x) } sum
         ------------------------------------------------------------------------------
        if(upperLimit - lowerLimit < 0.001) acc
        else summation(func, upperLimit, lowerLimit + 0.001, coefs, powers, acc + func(coefs, powers, lowerLimit) * 0.001)
         ------------------------------------------------------------------------------
        def calc(sum: Double, x: Double): Double = if (x > upperLimit) sum else calc(sum + func(coefficients, powers, x) * 0.001, x + 0.001)
        round(calc(0, lowerLimit))
         ------------------------------------------------------------------------------
        // math.floor(((lowerLimit * 1000) to (upperLimit * 1000) 
        //       map (e => func(coefficients, powers, e * 0.001) * 0.001)).sum * 10) / 10
         ------------------------------------------------------------------------------
         ------------------------------------------------------------------------------
        // (lowerLimit.toDouble to upperLimit by 0.001).foldLeft(0.0) { case (sum, x) =>
        //   sum + func(coefficients, powers, x)
        // } / 1000.0
         ------------------------------------------------------------------------------
        // (0 to ((upperLimit-lowerLimit)/0.001).toInt).map(x => func(coefficients, powers, lowerLimit+0.001*x)*0.001).sum
         ------------------------------------------------------------------------------
         ------------------------------------------------------------------------------
        // math.floor ({ for (x <- lowerLimit.toDouble to upperLimit by 0.001)
        // yield func(coefficients, powers, x) * 0.001 }.sum * 10) / 10
         ------------------------------------------------------------------------------
        // (1 to 1000).map{ x => 
        //   func(coeff, p, low + x * (up - low) / 1000.0)
        // }.reduce(_ + _) * (up - low) / 1000.0
         ------------------------------------------------------------------------------

    def displayAnswers(coefficients:List[Int],powers:List[Int],limits:List[Int])
        {
        println(summation(f,limits.reverse.head,limits.head,coefficients,powers))
        println(summation(area,limits.reverse.head,limits.head,coefficients,powers))
        }

    def main(args: Array[String]) {
        /** Purely IO Section **/
       displayAnswers("1 2 3 4 5".split(" ").toList.map(_.toInt),"6 7 8 9 10".split(" ").toList.map(_.toInt),"1 4".split(" ").toList.map(_.toInt))
    }
}
```

### Compute the Perimeter of a Polygon

<https://www.hackerrank.com/challenges/lambda-march-compute-the-perimeter-of-a-polygon/problem>

```s
Sample Input

4
0 0
0 1  
1 1  
1 0
Sample Output

4
```

```scala
这个代码写得漂亮啊
object Solution extends App {
  case class Point(x: Int, y: Int) {
    // 计算直角三角形的斜边长。hypot(double x, double y);
    def dist(p: Point) = math.hypot(x-p.x, y-p.y)
  }
  // 输入字符
  val in = new java.util.Scanner(System.in)
  val pts = List.fill(in.nextInt)(Point(in.nextInt, in.nextInt))
  // 递归求面积
  def perimeter(pts: List[Point], prev: Point): Double = pts match {
    case p :: tail => prev.dist(p) + perimeter(tail, p)
    case Nil => 0
  }
  println(perimeter(pts, pts(pts.size-1)))
}

------------------------------------------------------------------------

object Solution {
 def solve(n: Int, xs: Array[Int], ys: Array[Int]): Double = {
  var result = 0.0

  for (i <- 0 until n) {
   val j = (i + 1) % n
   result += Math.hypot(xs(j) - xs(i), ys(j) - ys(i))
  }

  return result
 }

 def main(args: Array[String]): Unit = {
  // 输入字符
  val sc = new java.util.Scanner(System.in)
  val n = sc.nextInt

  val xs = new Array[Int](n)
  val ys = new Array[Int](n)

  for (i <- 0 until n) {
   // 输入字符，循环读取
   xs(i) = sc.nextInt
   ys(i) = sc.nextInt
  }

  println(solve(n, xs, ys))
 }
}
```

```scala


--------------------------------------------------------------
object Solution {

    def dist(x1: Int, y1: Int, x2: Int, y2: Int): Double = {
        scala.math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
    
    def main(args: Array[String]) {
        // 读取字符的个数
        val N = scala.io.StdIn.readInt();
        var sum = 0.0;
        // 读取点的坐标，多加一个字符，因为有x(i-1), y(i-1)
        var x: Array[Int] = Array.fill[Int](N+1)(0)
        var y: Array[Int] = Array.fill[Int](N+1)(0)
        for (i <- 1 to N) {
            val s = scala.io.StdIn.readLine().split(" ").map(_.toInt);
            x.update(i, s(0));
            y.update(i, s(1));
            if (i > 1) sum += dist(x(i), y(i), x(i-1), y(i-1));
        }
        // 计算面积
        sum += dist(x(1), y(1), x(N), y(N))
        println(sum)
    }
}
```

Scala列表有三个基本操作：

- head 返回列表第一个元素
- tail 返回一个列表，包含除了第一元素之外的其他元素
- isEmpty 在列表为空时返回true

[Scala中sliding与grouped的区别](https://www.jianshu.com/p/9db2b4fbb03c)

[Scala中拆分操作partition、grouped、groupBy和sliding函数](https://blog.csdn.net/weixin_42078760/article/details/106982271)

```scala
import scala.math._

object Solution {

    def distance(x1: Int, y1: Int, x2: Int, y2: Int) = sqrt(pow(x2 - x1, 2) + pow(y2 - y1, 2))
    
    def main(args: Array[String]) {
        // tail 返回一个列表，包含除了第一元素之外的其他元素
        val pairs = io.Source.stdin.getLines.toList.tail
        val completePairs = (pairs :+ pairs.head).map(x => x.split(" ").map(_.toInt))
        val ans = completePairs.sliding(2).map{points =>
            // 感觉这里漏了几个点
            distance(points(0)(0), points(0)(1), points(1)(0), points(1)(1))
        }.sum
        println(ans)
    }
}

-------------------------------------------------------------------------------------------------

object Solution {

  def main(args: Array[String]) {
    var polygons = Seq((0, 0)).drop(1)
    for (i <- 1 to readLine.toInt) {
      val poly = readLine.split(" ").map(_.toInt)
      polygons = polygons :+ (poly(0), poly(1))
    }
    // 前后两个数的相加的和，再把头尾相加
    println(polygons.sliding(2).map(p => distanceBetween(p(0), p(1))).sum + distanceBetween(polygons.head, polygons.last))
  }

  def distanceBetween(first:(Int,Int), second:(Int,Int)): Double = {
    val dx = second._1 - first._1
    val dy = second._2 - first._2
    math.sqrt(dx*dx + dy*dy)
  }
}
```

```scala
object Solution {

  def dist(a: (Int, Int), b: (Int, Int)): Double = {
    math.sqrt(math.abs(a._1 - b._1) * math.abs(a._1 - b._1) + math.abs(a._2 - b._2) * math.abs(a._2 - b._2))
  }

  def computePerimeter(vertices: List[(Int, Int)], start: (Int, Int), perimeter: Double): Double = {
    vertices match {
      case Nil => perimeter
      // 计算最后一个元素，与第一个元素
      case x :: Nil => perimeter + dist(x, start)
      // 计算第一个元素和第二个元素，再删除第一个元素
      case x :: y :: xs => computePerimeter(y :: xs, start, perimeter + dist(x, y))
    }
  }

  def toTuple(arr: Array[String]): (Int, Int) = {
    (arr(0).toInt, arr(1).toInt)
  }

  def main(args: Array[String]) {
    var vertices: List[(Int, Int)] = Nil
    for (i <- 1 to io.StdIn.readLine().trim.toInt) vertices = toTuple(io.StdIn.readLine().split(" ")) :: vertices
    vertices = vertices.reverse
    printf("%.1f", computePerimeter(vertices, vertices.head, 0))
  }
}
```

```scala
import scala.io.StdIn._
import scala.math._
object Solution {

    def main(args: Array[String]) {

        val n = readInt()
        // 读取元素
        val(startX,startY) =readTuple()
        def compute(num:Int,prevX:Int,prevY:Int):Double={
            // 返回第一个元素和最后一个元素的结果
            if(num==0) return calc(prevX,prevY,startX,startY)
            // 读取元素
            val(curX,curY) = readTuple()
            // prevX,prevY来自于递归，curX,curY来自于读取
            return calc(prevX,prevY,curX,curY) + compute(num-1,curX,curY)
        }       
        def calc(x1:Int,y1:Int,x2:Int,y2:Int):Double = sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2))
        println(compute(n-1,startX,startY))
    }
    
       def readTuple():(Int,Int) = readLine().split(" ").toList match{
            case List(s1:String,s2:String) =>(s1.toInt,s2.toInt)
        }
     
}
```

###

```s

```

```scala

```

```scala

```

```scala

```

```scala

```

```scala

```

###

```s

```

```scala

```

###

```s

```

```scala

```

###

```s

```

```scala

```

###

```s

```

```scala

```
