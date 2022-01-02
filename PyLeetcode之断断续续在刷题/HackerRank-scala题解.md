
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

### Compute the Area of a Polygon

```s
Sample Input

4
0 0
0 1  
1 1  
1 0
Sample Output

1
```

```scala
object Solution {

    def dist(x1: Int, y1: Int, x2: Int, y2: Int): Double = {
        scala.math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
    
    def prod(x1: Int, y1: Int, x2: Int, y2: Int): Double = {
        x1 * y2 - x2 * y1;
    }
    
    def area(x1: Int, y1: Int, x2: Int, y2: Int, x3: Int, y3: Int): Double = {
        prod(x2-x1, y2-y1, x3-x1, y3-y1) / 2.0;
    }
    
    def main(args: Array[String]) {
        val N = scala.io.StdIn.readInt();
        var sum = 0.0;
        var x: Array[Int] = Array.fill[Int](N+1)(0)
        var y: Array[Int] = Array.fill[Int](N+1)(0)
        for (i <- 1 to N) {
            val s = scala.io.StdIn.readLine().split(" ").map(_.toInt);
            x.update(i, s(0));
            y.update(i, s(1)); 
            // update是赋值
        }
        for (i <- 3 to N) {
            sum += area(x(1), y(1), x(i-1), y(i-1), x(i), y(i));
        }
        println(scala.math.abs(sum))
    }
}
```

java.util.Scanner😁

```scala
object Solution extends App {
  case class Point(x: Long, y: Long) {
    def dist(p: Point) = math.hypot(x-p.x, y-p.y)
  }
  val in = new java.util.Scanner(System.in)
  val pts = List.fill(in.nextInt)(Point(in.nextInt, in.nextInt))
  def det(p1: Point, p2: Point) = p1.x*p2.y-p1.y*p2.x
  def area(pts: List[Point], prev: Point): Long = pts match {
    case p :: tail => det(p, prev) + area(tail, p) // 递归调用
    case Nil => 0
  }
  println(-0.5*area(pts, pts(pts.size-1)))
}

object Solution {
  def cross(x1: Double, y1: Double, x2: Double, y2: Double): Double = x1 * y2 - y1 * x2

  def solve(n: Int, xs: Array[Int], ys: Array[Int]): Double = {
    var result = 0.0

    for (i <- 0 until n) {
      val j = (i + 1) % n // 这样，i = n 时，j = 1
      result += cross(xs(i), ys(i), xs(j), ys(j))
    }

    return Math.abs(result) / 2.0
  }

  def main(args: Array[String]): Unit = {
    val sc = new java.util.Scanner(System.in)
    val n = sc.nextInt

    val xs = new Array[Int](n)
    val ys = new Array[Int](n)

    for (i <- 0 until n) {
      xs(i) = sc.nextInt
      ys(i) = sc.nextInt
    }

    println(solve(n, xs, ys))
  }
}
```

滑动窗口

```scala
import scala.math._
object Solution {

    def distance(x1: Int, y1: Int, x2: Int, y2: Int) = x1 * y2 - y1 * x2
    def main(args: Array[String]) {
        
        val pairs = io.Source.stdin.getLines.toList.tail
        val completePairs = (pairs :+ pairs.head).map(x => x.split(" ").map(_.toInt))
        // 把 pairs.head 加到 pairs 的尾部
        val ans = completePairs.sliding(2).map{points =>
            distance(points(0)(0), points(0)(1), points(1)(0), points(1)(1))
        }.sum/2.0
        println(abs(ans))
    }
}
```

```scala
object Solution {
  private def readLine: String = {
    scala.io.StdIn.readLine()
  }
  def main(args: Array[String]) {
    val N = readLine.toInt
    val coords = Array.ofDim[(Int,Int)](N)
    for (i <- 0 until N) {
      val arr = readLine.trim.split(' ').map(s => s.toInt)
      coords(i) = (arr(0), arr(1))
    }

    val shiftedCoords = coords ++ Seq(coords(0))
    // ++ 用于连接两个集合

    def area(i:Int) : Double = {
      0.5*(shiftedCoords(i)._1 * shiftedCoords(i+1)._2 - shiftedCoords(i+1)._1 * shiftedCoords(i)._2)
    }

    val areaTotal = (0 until N).map(i => area(i)).sum
    println(areaTotal)
  }


}
```

case class Point 😁

```scala
object Solution {
  import scala.io.StdIn._

  case class Point(x: Double, y: Double) {
    def det(q: Point) = x * q.y - y * q.x
  }
  def main(args: Array[String]) {
    val Array(n) = readArray()
    val ps = Seq.fill(n){readPoint()}
    println(f"${run(ps)}%.3f") // %.3f 取小数点后三位
  }
  def run(ps: Seq[Point]) = {
    val qs = ps.tail :+ ps.head
    val ds = (ps zip qs) map { case (p, q) => p det q}
    math.abs(ds.sum / 2.0)
  }
  def readArray() = readLine().split("\\s+").map(_.toInt)
  def readPoint() = readArray() match {
    case Array(x, y) => Point(x, y)
  }
}
```

滑动窗口

[Scala集合Seq](https://blog.csdn.net/wenthkim/article/details/86550209): ([官方资料](https://www.scala-lang.org/api/2.12.5/scala/collection/Seq.html))([用法实例](https://www.codercto.com/a/90989.html))

- Seq是`列表`，适合存`有序``重复`数据，进行快速`插入/删除`元素等场景

- [数组Array与序列Seq是兼容的](http://bcxw.net/book/38.html)

Seq 同样分为可变和不可变两大类，此外还派生出 IndexedSeq 和 LinearSeq 两个重要的子特质：

- IndexedSeq ：代表`索引序列`，对于基于`索引`的操作来说效率较高，一般底层依赖于数组实现。
- LinearSeq ：代表`线性序列`，对于 `head、tail，以及 isEmpty` 一类的方法效率较高，一般底层依赖于链表实现。

```scala
object Solution {

  def main(args: Array[String]) {
    var polygons: Seq[(Int, Int)] = Seq()
    for (i <- 1 to readInt) {
      val poly = readLine.split(" ").map(_.toInt)
      polygons = polygons :+ (poly(0), poly(1)) // 点一个个连上去
    }
    polygons = polygons :+ polygons.head // 再把头部接上
    println(math.abs(polygons.sliding(2).map(p => calc(p(0), p(1))).sum) / 2.0)

  }

  def calc(a: (Int, Int), b: (Int, Int)): Int = {
    a._1 * b._2 - a._2 * b._1
  }

}
```

match case 递归调用

```scala
object Solution {

  def area(vertices: List[(Int, Int)], start: (Int, Int), a: Double): Double = {
    vertices match {
      case Nil => a
      case x :: Nil => math.abs((a + x._1 * start._2 - x._2 * start._1) / 2)
      case x :: y :: xs => area(y :: xs, start, a + x._1 * y._2 - x._2 * y._1)
    }
  }

  def toTuple(arr: Array[String]): (Int, Int) = {
    (arr(0).toInt, arr(1).toInt)
  }

  def main(args: Array[String]) {
    var vertices: List[(Int, Int)] = Nil
    for (i <- 1 to io.StdIn.readLine().trim.toInt) vertices = toTuple(io.StdIn.readLine().split(" ")) :: vertices
    vertices = vertices.reverse
    printf("%.1f", area(vertices, vertices.head, 0))
  }
}
```

明明可以用for循环解决的问题,scala偏偏要用函数

```scala
import scala.annotation.tailrec
import scala.io.StdIn

object Solution {
  def main(args: Array[String]) {
    val n = StdIn.readLine().trim().toInt
    val points = (1 to n)
      .map(i => StdIn.readLine().trim().split(" ").map(_.toInt))

    @tailrec
    def loop (area:Double, counter:Int):Double = {
      if (counter >= n) return area
      val currentPoint = points(counter)
      val nextPoint = if (counter == n - 1) points(0) else points(counter + 1)
      val areaDelta = nextPoint(0)*currentPoint(1) - nextPoint(1)*currentPoint(0)
      loop (area + areaDelta, counter + 1)
    }
    println(math.abs(loop(0, 0))/2)
  }
}
```

Source.fromInputStream.getLines().next() 😁

```scala
import java.text.DecimalFormat
import scala.io.Source;

object Solution {
  def area(p: (Int, Int), points: List[(Int, Int)], accum: Double): Double = {
    if (points.isEmpty) accum
    else {
      val next = points.head
      val part1 = p._1 * next._2
      val part2 = p._2 * next._1
      val dist = 0.5 * (part1 - part2)
      area(next, points.tail, accum + dist)
    }
  }
  
  def main(args: Array[String]) {
    val input = Source.fromInputStream(System.in);
    val lines = input.getLines()
    val N = lines.next().trim().toInt
    val points = List.range(0, N).map { _ => {
      val L = lines.next().split(' ').map { _.trim().toInt }
      (L(0), L(1))
    }
    }
    val formatter = new DecimalFormat("#.#")
    val p = Math.abs(area(points.head, points.tail :+ points.head, 0.0))
    println(formatter.format(p))
  }
}
```

Scanner(System.in).nextDouble

```scala
import java.io.FileInputStream
import java.util.Scanner

object Solution {
    def area(list: List[(Double, Double)]): Double = {
      def crossProduct(x: (Double, Double), y: (Double, Double)): Double = {
        return ((x._1 * y._2) - (y._1 * x._2))/2.0
      }
      // 递归调用
      def calculateArea(list: List[(Double, Double)], sum: Double): Double = list match {
        case Nil => sum
        case x :: y :: xs => calculateArea(y :: xs, sum + crossProduct(x, y))
        case x :: Nil => sum
      }
      calculateArea(list, 0)
    }
    
    def main(args: Array[String]) {
      val in = new Scanner(System.in)
      val n = in.nextInt
      val list = (for(j <- 0 until n) yield (in.nextDouble, in.nextDouble)).toList
      val center1 = list.reduce((x,y) => ((x._1 + y._1), (x._2 + y._2)))
      val center = (center1._1/n, center1._2/n)
      println(area((list :+ list.head).map(x => (x._1 - center._1, x._2 - center._2))))
      // list :+ list.head首尾相连,center是什么鬼
    }
}
```


```scala
object Solution {
    def det(p1: (Int, Int), p2: (Int, Int)): Double = {
        val (x1, y1) = p1
        val (x2, y2) = p2
        x1 * y2 - x2 * y1
    }
    def perimeter(points: List[(Int, Int)]): Double = {
        val wrapped_points = points.last +: points
        val adj_points = wrapped_points zip wrapped_points.tail
        // points 加到队首以后,进行拉链操作
        adj_points.map{case (p1, p2) => det(p1, p2)}.sum / 2
    }
    
    def main(args: Array[String]) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution
*/
        val num_points = readInt
        var points = List.empty[(Int, Int)]
        for (_ <- 1 to num_points) {
            val point = readLine.split(' ').map(_.toInt)
            points = (point(0), point(1)) +: points
        }
        println(perimeter(points.reverse))
    }
}
```

### Fibonacci Numbers

https://www.hackerrank.com/challenges/functional-programming-warmups-in-recursion---fibonacci-numbers/problem

```s
Sample Input and Output Values for the Fibonacci Series

fibonacci(3) = (0+1) = 1  
fibonacci(4) = (1+1) = 2  
fibonacci(5) = (1+2) = 3  
```

```scala
object Solution {
    
     def fibonacci(x:Int):Int = {
          if (x <= 2) x - 1
          else fibonacci(x - 1) + fibonacci(x - 2)

     }

    def main(args: Array[String]) {
         println(fibonacci(readInt()))

    }
}
```

```scala
object Solution {
    
     def fibonacci(x:Int):Int = x match {
          case 0 | 1 => 0
          case 2 => 1
          case _ => fibonacci(x-1) + fibonacci(x-2)
     }

    def main(args: Array[String]) {
         println(fibonacci(readInt()))
    }
}
```

```scala
object Solution {
    
     def fibonacci(x:Int):Int = {
        if(x == 1) 0
        else if(x == 2) 1
        else fibonacci(x-1) + fibonacci(x-2)
     }

    def main(args: Array[String]) {
         println(fibonacci(readInt()))
    }
}
```

### Pascal's Triangle

https://www.hackerrank.com/challenges/pascals-triangle/problem

```s
Sample Input

4  

Sample Output

1  
1 1  
1 2 1  
1 3 3 1   
```

用col, row两个参数：

```scala
object Solution {
    def pascal(c: Int, r: Int): Int = {
        if (c == 0 || c == r) 1
        else pascal(c - 1, r - 1) + pascal(c, r - 1)
      }

    def main(args: Array[String]) {
          for (row <- 0 to readInt()-1) {
              for (col <- 0 to row)
                print(pascal(col, row) + " ")
              println()
          }
    }
}
```

```scala
object Solution
{
  def main(args: Array[String])
  {
    def pascal(c: Int, r: Int): Int =
    {
      if(c == 0 || c == r) 1 else pascal(c, r - 1) + pascal(c - 1, r - 1)
    }

    for (row <- 0 to readLine.toInt - 1)
    {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }
}
```

用滑动窗口：

```scala
object Solution {
  // 将dp写成递归的形式
  def printRows(curRow: Array[Int], n: Int) {
    if (n > 0) {
      println(curRow.mkString(" "))
      printRows((0 +: curRow :+ 0).sliding(2).map { case Array(a, b) => a + b }.toArray, n - 1)
    }
  }

  def main(args: Array[String]): Unit = {
    printRows(Array(1), readInt())
  }
}
```

### String Mingling

https://www.hackerrank.com/challenges/string-mingling/problem

一行代码

```s
Sample Input #00

abcde
pqrst

Sample Output #00

apbqcrdset

Sample Input #01

hacker
ranker

Sample Output #01

hraacnkkeerr
```

```scala
精简版：

object Solution {
  def main(args: Array[String]): Unit = {
    println(readLine.zip(readLine).map(e => e._1 + "" + e._2).mkString(""))
  }
}

详细版：

object Solution{
    def main(args: Array[String]): Unit = {
        println(readLine.toCharArray.toList.zip(readLine.toCharArray.toList).map(e => e._1 + "" + e._2).mkString(""));
    }
}

object Solution {
    def main(args: Array[String]) {
        println((readLine() zip readLine()).toList map {case (e1, e2) => e1.toString + e2.toString} mkString)
    }
}
```

```scala
object Solution {
    
  def mingle(ps: String, qs: String): String = {
    (ps, qs).zipped.flatMap{case x => Array(x._1, x._2)}.mkString("")
  }

  def main(args: Array[String]) {
    val ps = io.StdIn.readLine
    val qs = io.StdIn.readLine
    print(mingle(ps, qs))
  }
}

object Solution extends App {
  val a = readLine.toList
  val b = readLine.toList
  println(a.zip(b).flatMap(pair => List(pair._1, pair._2)).mkString)
}
```

charAt 的用法：

```scala
object Solution {
    def mingle(p:String, q:String) = {
        for (i <- 0 until p.length()) {
            print(p.charAt(i));
            print(q.charAt(i));
        }
    }
    def main(args: Array[String]) {
        mingle(readLine(), readLine())
    }
}

object Solution {
  def main(args: Array[String]) {
    var p = scala.io.StdIn.readLine
    var q = scala.io.StdIn.readLine
    var out = new StringBuilder("")
    for (i <- 0 to p.length-1) {
      out += p.charAt(i)
      out += q.charAt(i)
    }
    println(out)
  }
}

object Solution {
  def main(args: Array[String]) {
    var P:String = readLine()
    var Q:String = readLine()
    var i:Int = 0
    for(i <- 0 until P.length()){
      print(P.charAt(i)+""+Q.charAt(i))
    }
  }
}
```

flatten 的用法：

```scala
import scala.io.Source

object Solution {
  
  def solve(s: String, t: String) =
    (s zip t) map {i => List(i._1,i._2)} flatten

  def main(args: Array[String]): Unit = {
    val lines = Source.stdin.getLines
    val s = lines.next
    val t = lines.next
    println(solve(s,t) mkString "")
  }
}
```

直接交替打印：

```scala
object Solution {
  val in = {
    io.Source.stdin.getLines()
  }
  def main(args: Array[String]): Unit = {
    val a, b = in.next
    val n = a.length()
    for (i <- 0 until n) {
      print(a(i))
      print(b(i)) 
    }
  }
}


import scala.io._

object Solution 
{
    def main(args:Array[String]):Unit=
    {
        var A=Console.readLine()
        var B=Console.readLine()
        
        for(i<-0 until A.length())
        {
            print(A(i))
            print(B(i))
        }
    }
}
```

transpose 的用法：

```scala
import java.io.PrintWriter
import java.util.Scanner
import scala.annotation.tailrec

object Solution extends App {
  val sc = new Scanner(System.in)
  val out = new PrintWriter(System.out)
  
  def solve(): String = {
    val P, Q = sc.nextLine
    List(P, Q).transpose.map(_.mkString).mkString
  }
  
  out.println(solve)
  out.flush
}
```

递归：

```scala
object Solution {
  
  def main(args: Array[String]) {
    val s1 = readLine.toList
    val s2 = readLine.toList
    val result = mingle(s1, s2, Nil).reverse.mkString
    println(result)
  }
  
  def mingle(first: List[Char], second: List[Char], res: List[Char]): List[Char] = first match {
    case Nil => res
    case head :: t => mingle(second, t, head :: res)
    // 交替将头部元素附着到结果上
  }
  
}
```

IndexedSeq的用法

```scala
import scala.collection.immutable.IndexedSeq

object Solution {
  def main(args: Array[String]): Unit = {
    val p: String = readLine()
    val q: String = readLine()
    val zip: IndexedSeq[(Char, Char)] = p.zip(q)
    zip.foreach(x => {
      print(x._1); print(x._2);
    })
    println()
  }
}
```

### String-o-Permute

https://www.hackerrank.com/challenges/string-o-permute/problem

```s
Sample Input

2
abcdpqrs
az

Sample Output

badcqpsr
za
```

利用二进制运算符：将奇数和偶数位置调换

```scala
object Solution {
    def main(args: Array[String]) {
      val lines = scala.io.Source.stdin.getLines
        val T = lines.next.toInt
        for (_ <- 1 to T)
        {
          val s = lines.next
            val ans = for (i <- 0 until s.length) yield s(i^1)  
            println(ans.mkString)
        }
    }
}
```

利用 charAt

```scala
object Solution {
  def main(args: Array[String]): Unit = {
    var t = Console.readInt
    for( tc <- 0 until t) {  
      var a = Console.readLine
      val n = a.length()
      var i:Int = 0
      for( i <- 0 until n/2) {
        print(a.charAt(2*i+1))
        print(a.charAt(2*i))
      }
      println()
    }
  }
}

/**
 * Created by hama_du on 2014/03/21.
 */
object Solution extends App {
  val T = readLine().toInt
  (0 until T).foreach(_ => {
    println(solve(readLine()))
  })

  def solve(line: String): String = {
    String.valueOf((0 until line.length).map(i => {
      if (i % 2 == 0) {
        line.charAt(i+1)
      } else {
        line.charAt(i-1)
      }
    }).toArray)
  }
}

import scala.annotation.tailrec;

object Solution {

    def main(args: Array[String]) {
        val t = readLine.toInt;
        for(i <- 1 to t) f(readLine);
    }
  
    def f(t:String):Unit = {
        var i = 0;
        val tLen = t.length;
        while(i < tLen-1){
            print(t.charAt(i+1) +""+ t.charAt(i));
            i+=2;
        }
        println();
    }
  
}

object Solution {

    def main(args: Array[String]) {
        var num = readInt;
        for (i <- 1 to num){
          var word:String = readLine;
          for(i <- 0 to (word.length/2)-1)
            print(word.charAt(2*i+1).asInstanceOf[Char] + "" + word.charAt(2*i).asInstanceOf[Char])
          println("")
        }
    }
}
```

利用 grouped

```scala
object Solution {
  
    def main(args: Array[String]) {
      def solve: Unit = {
        val in = readLine()
        println(in.grouped(2).toList.map(x => x.reverse).mkString)
      }
      val numInputs = Integer.parseInt(readLine())
      (0 until numInputs).foreach(_ => solve)
      
    }
}

object Solution {

  def solve(s: String): String = {
    s.grouped(2).map(_.reverse).mkString
  }

  def main(args: Array[String]) {
    val t = readLine().toInt
    for (_ <- 1 to t) {
      val s = readLine()
      val result = solve(s)
      println(result)
    }
  }
}

object Solution {

    def main(args: Array[String]) {
      val input = io.Source.stdin.getLines
      val t: Int = input.next.toInt
      val lines: Iterator[String] = input.take(t)
      
      for (line <- lines) {
        line.grouped(2).foreach {
          pair => {
            print(pair(1))
            print(pair(0))
          }
        }
        println
      }
    }
}

利用了隐式函数

object Solution {

  implicit def intWithTimes(n: Int) = new {        
    def times(f: => Unit) = 1 to n foreach {_ => f}
  }

  def main(args: Array[String]): Unit = {
    val lines = io.Source.stdin.getLines()
    val ntimes = lines.next().toInt;
    ntimes times {
      lines.next.toList.grouped(2).foreach{ case List(a,b) => print(b); print(a)}
      println
    }
  }

}
```

利用 match case 递归

```scala
object Solution {

  def main(args: Array[String]) {
      val lines = io.Source.stdin.getLines.drop(1).toList.foreach { line =>
        println(swap(line))
      }
    
  }
  
  def swap(str: String): String = {
    def helper(res: List[Char], input: List[Char]): List[Char] = input match {
      case even :: odd :: xs => helper(even :: odd :: res, xs) // 不太明白😁
      case _ => res.reverse
    }
    helper(List(), str.toList).mkString
  }
  
}
```

### String Compression

https://www.hackerrank.com/challenges/string-compression/problem

```s
Sample Input #00

abcaaabbb

Sample Output #00

abca3b3

Sample Input #01

abcd

Sample Output #01

abcd

Sample Input #02

aaabaaaaccaaaaba

Sample Output #02

a3ba4c2a4ba
```

利用charAt

```scala
import java.io.BufferedReader
import java.io.InputStreamReader
//remove if not needed
import scala.collection.JavaConversions._

object Solution {

  def main(args: Array[String]) {
    val br = new BufferedReader(new InputStreamReader(System.in))
    val line1 = br.readLine()
    var temp = 1
    var lastch = line1.charAt(0)
    for (i <- 1 until line1.length) {
      if (line1.charAt(i) == lastch) {
        temp += 1
      } else {
        System.out.print(lastch)
        if (temp > 1) {
          System.out.print(temp)
        }
        lastch = line1.charAt(i)
        temp = 1
      }
    }
    System.out.print(lastch)
    if (temp > 1) System.out.print(temp)
    println()
  }
}


object Solution {
    def main(args: Array[String]) {
        var m = readLine();
        var cur = m.charAt(0);
        var c = 1;
        for (i <- 1 until m.length()) {
            if (m.charAt(i) == cur) {
                c = c + 1;
            } else {
                print(cur);
                if (c > 1)
                    print(c);
                cur = m.charAt(i);
                c = 1;
            }
        }
        print(cur);
        if (c > 1)
            print(c);
    }
}


object Solution {
  def main(args: Array[String]) {
    var S:String = readLine()
    var check:Int = 0
    for(i <- 0 until S.length()){
      if(i==0 || S.charAt(i)!=S.charAt(i-1)){
        if(check>1) print(check)
        print(S.charAt(i))
        check=1
      }
      else check=check+1
    }
    if(check>1) print(check)
  }
}
```

利用StringBuffer

```scala
object Solution extends App {
  val s = readLine
  val buffer = new StringBuffer
  var i = 0
  while(i < s.length) {
    var j = i
    var cnt = 0
    while(j < s.length && s(i) == s(j)) {
      cnt += 1
      j += 1
    }
    if (cnt == 1) buffer.append(s(i))
    else buffer.append(s(i) + "" + cnt)
    i += cnt
  }
  println(buffer.toString)
}
```

利用takeWhile和dropWhile

```scala
object Solution {
    
    def apeCompress(str: String):Unit = {
        if (str.length != 0) {
            val char = str(0)
            val count = str.takeWhile(_ == char).length
            if (count != 1)
                print(char + "" + count)
            else
                print(char)

            apeCompress(str.dropWhile(_ == char))
        }
    }

    def main(args: Array[String]) {
        apeCompress(readLine())
    }
}
```

利用match case

```scala
object Solution {
    def main(args: Array[String]) {
        val s = io.Source.stdin.getLines().take(1).toList.head + "0"
            val res = s.foldLeft(('a',0)) { (acc, ch) =>
  (acc, ch) match {
    case ((_, 0), ch) => (ch, 1)
    case ((prev, n), cur) if prev == cur => (cur, n+1)
    case ((prev, 1), cur) => print(prev); (cur, 1)
    case ((prev, n), cur) => print(prev + n.toString); (cur, 1)
  }}
    }
}

```

### Convex Hull

```s
Sample Input

6    
1 1    
2 5    
3 3    
5 3    
3 2    
2 2

Sample Output

12.2   
```

- 计算欧氏距离
- 需要删除：交叉乘积 <= 0

```scala
import scala.collection.mutable.ArrayBuffer

object Solution {

    type Coordinate = (Int, Int)

    def euclid(x: Coordinate, y: Coordinate): Double = 
        Math.sqrt(Math.pow(x._1 - y._1, 2) + Math.pow(x._2 - y._2, 2))

    def array2Tuple(a: Array[Int]): Coordinate = (a(0), a(1))

    def crossProduct(a: Coordinate, b: Coordinate, c: Coordinate): Int =
        (b._1 - a._1) * (c._2 - b._2) - (c._1 - b._1) * (b._2 - a._2)

    def inBetween(a: Coordinate, b: Coordinate, c: Coordinate): Boolean =
        crossProduct(a, b, c) == 0 && (b._1 - a._1) * (b._1 - c._1) <= 0 && (b._2 - a._2) * (b._2 - c._2) <= 0

    def shouldRemove(a: Coordinate, b: Coordinate, c: Coordinate): Boolean =
        crossProduct(a, b, c) < 0 || inBetween(b, a, c)

    def convexHull(a: Array[Coordinate]): Double = {
        // 找到最小的点
        val p0 = a.minBy(_.swap) // 你可以使用 Tuple.swap 方法来交换元组的元素
        // 按照与x轴的角度排序
        val b = a.filter(_ != p0).sortBy(x => (p0._1 - x._1) / euclid(p0, x)) :+ p0
        // 把内部的点都删掉
        val h = b.tail.foldLeft(ArrayBuffer(p0, b.head)) {
            (z, x) => {
                while (z.length >= 2 && shouldRemove(z(z.length - 2), z.last, x))
                    z.remove(z.length - 1)
                if (z.length < 2 || !inBetween(z(z.length - 2), x, z.last))
                    z += x
                z
            }
        }
        // 计算外部的点的周长
        h.sliding(2).toArray.map(t => euclid(t(0), t(1))).sum
    }

    def main(args: Array[String]) {
        println(convexHull((1 to readInt()).map(_ => array2Tuple(readLine.trim.split(' ').map(_.toInt))).toArray))
    }
}
```

```scala
object Solution {

    def input = io.Source.stdin.getLines().toList
    def intLists: List[List[Int]] = input.map(_.split(" ").toList.filter(_.size>0).map(_.toInt))
    def pairs: List[(Int, Int)] = intLists.tail.filter(_.size>0).map(x => (x.head, x.tail.head))

    type Point = (Int,Int)
    type Points = List[Point]

    // 距离    
    def Distance(A:Point, B:Point): Double = {
        val dx = (A._1 - B._1)
        val dy = (A._2 - B._2)
        math.sqrt(dx*dx + dy*dy)        
    }

    // 交叉乘积
    def ChordDistance(A:Point, B:Point)(C:Point): (Int,Point) = {
        val ABx = B._1-A._1;
        val ABy = B._2-A._2;
        val num = ABx*(A._2-C._2)-ABy*(A._1-C._1);
        ((if (num < 0) -num else num),C)
    }

    // 判断是否在外边缘
    def isAbove(A:Point, B:Point)(P:Point): Boolean =
      (B._1-A._1)*(P._2-A._2) - (B._2-A._2)*(P._1-A._1) > 0
         
    // 判断是否在外边缘
    def insideTriangle( A:Point,B:Point,C:Point )(p:Point): Boolean =
        isAbove( A,B)(p) && isAbove(B,C)(p) && isAbove(C,A)(p)
        
    def quickHull(p:Points):Points = {
        def quickHullRecurs(p:Points, A:Point, B:Point):Points = {
            if ( p.isEmpty ) List(B)
            else
            {
                val C = p.map(ChordDistance(A,B) ).maxBy(_._1)._2
                val newP = p filterNot(_==C)
                val (s0,s1) = newP partition isAbove(A,C)
                val s2 = s1 filter isAbove(C,B)
                A :: quickHullRecurs(s0,A,C) ::: quickHullRecurs(s2,C,B) 
            }
        }

        if (p.length == 3) p.last :: p
        else {
            val A = p.minBy(_._1)
            val B = p.maxBy(_._1)
            val initHull = List(A,B)
            val initSet = p.filterNot(_==initHull.head).filterNot(_==initHull.tail.head)
            val (s0,s1) = initSet partition isAbove(A,B)
            quickHullRecurs(s0, A, B) :::
            quickHullRecurs(s1, B, A).tail
        }
    }
    def main(args: Array[String]) {
        val points:Points = pairs
        val hull = quickHull(points)
        val perim= (hull zip (hull.tail)).map(x=>Distance(x._1,x._2)).sum
        println(perim)
    }
}
```

```scala
object Solution {
    
    case class Point(x: Int, y: Int)
   
    def cross(o: Point, a: Point, b: Point) = {
        (a.x-o.x)*(b.y-o.y)-(a.y-o.y)*(b.x-o.x)
    } 
    
    def dist(p1: Point, p2: Point) = {
        scala.math.sqrt(scala.math.pow(p1.x-p2.x,2) + scala.math.pow(p1.y-p2.y,2));
    }
    
    def halfConvexHull(points : scala.collection.mutable.ListBuffer[Point]) = {
        var hull = new scala.collection.mutable.ArrayStack[Point]();
        for (point <- points){
            while (hull.length>=2 && (cross(hull(1),hull(0),point)<=0)){
                hull.pop();
            }
            hull.push(point);
        }
        hull;
    }
    
    def convexHull(argPoints : scala.collection.mutable.ListBuffer[Point]) = {
        var points = argPoints.sortBy(p => (p.x,p.y));
        var lowerHull = halfConvexHull(points);
        var upperHull = halfConvexHull(points.reverse);
        lowerHull.pop();
        upperHull.pop();
        upperHull ++ lowerHull;
    }
    
    def main(args: Array[String]) {
        var n = readInt();
        var points = new scala.collection.mutable.ListBuffer[Point]()
            
        for (i <- 1 to n){
            var Array(x,y) = readLine().split(" ").map(_.toInt)
            points += (Point(x,y));
        }
        var hull = convexHull(points);
        hull.push(hull.last);
        
        var perimeter:Double = 0.0;
        for (i<-0 to hull.length-2){
            perimeter+=dist(hull(i),hull(i+1));
        }
        print(perimeter);
    }
}
```

```scala
object Solution {
    type Y = Int
    type X = Int
    type Point = (X, Y)
        
    def length(p1: Point, p2: Point): Double = 
        math.sqrt(math.pow(p2._1 - p1._1, 2) + math.pow(p2._2 - p1._2, 2))    
        
    def perimeter(xs: List[Point]): Double =
        if (xs.nonEmpty)
          ((xs.head, xs.last) :: xs.zip(xs.tail)).foldLeft(0d) {
            case (acc, (p1, p2)) => acc + length(p1, p2)
          }
        else 0

    def ccw(o: Point, a: Point, b: Point) = (a._1 - o._1) * (b._2 - o._2) - (a._2 - o._2) * (b._1 - o._1)        
            
    def convexMonotone(xs: List[Point]): List[Point] = {
        val sorted = xs.sorted

        @scala.annotation.tailrec
        def loop(path: List[Point], p: Point): List[Point] = path match {
          case p1 :: p2 :: tail if ccw(p2, p1, p) <= 0 => loop(p2 :: tail, p)
          case _ => p :: path
        }

        val lower = sorted.foldLeft(Nil: List[Point])((path, p) => loop(path, p))
        val upper = sorted.reverse.foldLeft(Nil: List[Point])((path, p) => loop(path, p))

        upper.tail ::: lower.tail
    }            
            
    def main(args: Array[String]) {
        val N = io.StdIn.readInt()
        val points = (0 until N).map(_ => io.StdIn.readLine().split("\\s+") match {
            case Array(x, y) => (x.toInt, y.toInt)
          }).toList
        println(perimeter(convexMonotone(points)))
    }
}
```


```scala
object Solution extends App {
  case class Point(x: Int, y: Int)

  // 计算距离
  def sqDist(p1: Point, p2: Point) = {
    def sq(i: Int) = i*i
    sq(p1.x - p2.x) + sq(p1.y - p2.y)
  }

  // 计算是否为凸
  def hull(points: Seq[Point]) = {
    def cross(p0: Point, p1: Point, p2: Point) = (p1.x - p0.x)*(p2.y - p0.y) - (p1.y - p0.y)*(p2.x - p0.x)

    val p0 = points.minBy{case Point(x, y) => (x, y)}
    val sortedPoints = points.filter(_ != p0).sortWith{ (p1, p2) =>
      val c = cross(p0, p1, p2)
      c > 0 || (c == 0 && sqDist(p0, p1) < sqDist(p0, p2))
    }

    // 保留凸的点
    def loop(acc: List[Point], points: List[Point]): List[Point] = (acc, points) match {
      // 递归结束
      case (_, Nil) => acc
      // 继续递归：只有1个点，就继续
      case (_ :: Nil, pn :: ps) => loop(pn :: acc, ps)
      // 继续递归：只有2个点，并且，凸，就继续，
      case (pnm :: pnmm :: _, pn :: ps) if cross(pnmm, pnm, pn) > 0 => loop(pn :: acc, ps)
      // 否则，删去第一个元素
      case (_ :: acct, _) => loop(acct, points)
    }
    loop(List(p0), (sortedPoints :+ p0).toList)
  }

  def perimeter(hull: Seq[Point]) = {
    def dist(p1: Point, p2: Point) = math.sqrt(sqDist(p1, p2))
    // 滑动窗口
    hull.sliding(2).map{case List(p1, p2) => dist(p1, p2)}.sum
  }  

  val in = io.Source.stdin.getLines
  val N = in.next().toInt
  val points = for (_ <- 1 to N) yield {
    val x = in.next().split(" ").map(_.toInt)
    Point(x(0), x(1))
  }

  println(perimeter(hull(points)))
}

import scala.collection.mutable.ListBuffer

object Solution {
  
  case class Point(x: Double, y: Double)
  // 计算是否为凸
  def convexHull(ps: Array[Point]): Array[Point] = {
    val points = ps.sortWith((p, q) => p.x < q.x || (p.x == q.x && p.y < q.y))
    val upper = halfHull(points)
    val lower = halfHull(points.reverse)
    upper.tail ++ lower.tail
  }
  
  def halfHull(ps: Array[Point]): Array[Point] = {
    val hull = new ListBuffer[Point]()
    for (p <- ps) {
      while (hull.size >= 2 && ccw(p, hull(0), hull(1))) {
        hull.remove(0)
      }
    hull.prepend(p)
    }
    hull.toArray
  }
  
  def ccw(p1: Point, p2: Point, p3: Point) = {
    val exteriorProduct = (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x)
    val collinear: Boolean = math.abs(exteriorProduct) <= 1e-9
    val ccw: Boolean = exteriorProduct < 0.0
    collinear || ccw
  }
  // 计算周长
  def length(p: Point, q: Point): Double = math.sqrt((p.x - q.x) * (p.x - q.x) + (p.y - q.y) * (p.y - q.y))
    
  def perimeter(ps: Array[Point]): Double = {
    (1 until ps.length).foldLeft(length(ps(0), ps.last))((acc, i) => acc + length(ps(i), ps(i - 1)))
  }
  
  def solve(ps: Array[Point]): Double = perimeter(convexHull(ps))
    
  def main(args: Array[String]) {
    val n = io.StdIn.readInt
    val points = Array.range(0, n) map {line =>
      val coordinates = io.StdIn.readLine.split(" ").map(_.toDouble)
      Point(coordinates(0), coordinates(1))
    }
    println(solve(points))
  }
}
```

case class + 伴生对象

```scala
case class Point(x: Int, y: Int) {
  def angleWith(p: Point): Double = Math.atan2(p.y - y, p.x - x)
  def distance(p: Point): Double = Math.sqrt(Math.pow(p.x - x, 2) + Math.pow(p.y - y, 2))
}

object Point {
  def apply(a: Array[Int]): Point = Point(a(0), a(1))
}

object Solution {
  def ccw(p1: Point, p2: Point, p3: Point): Boolean = {
    (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x) >= 0
  }

  // 可以看到两个函数名一样的，
  // 他的参数是不一样的，
  // 这两个函数的功能一样，
  // 但是接受的参数不一样，所以这才需要去定义两个函数。
  def convexHull(sortedPoints: List[Point]): List[Point] = {
    convexHull(sortedPoints.take(3).reverse, sortedPoints.drop(3) :+ sortedPoints.head)
  }

  def convexHull(hullPoints: List[Point], restPoints: List[Point]): List[Point] = {
    if (restPoints == Nil) hullPoints
    else if (ccw(hullPoints(1), hullPoints(0), restPoints(0))) {
      convexHull(restPoints.head :: hullPoints, restPoints.tail)
    } else {
      convexHull(hullPoints.tail, restPoints)
    }
  }

  def lowestPoint(pts: Array[Point]): Point = pts.minBy(p => p.y * 10001 + p.x)

  def sortedPoints(pts: Array[Point]): List[Point] = {
    ((lowest: Point) =>
      pts.sortWith { (p1, p2) => 
        lowest.angleWith(p1).compare(lowest.angleWith(p2)) match {
          case 0 => lowest.distance(p1).compare(lowest.distance(p2)) < 0
          case n => n < 0
        }
      }.toList)(lowestPoint(pts))
  }

  def readPoints: Array[Point] = Array.fill(readLine().trim.toInt)(Point(readLine().split(" ").map(_.toInt)))

  def main(args: Array[String]): Unit = {
    printf("%.1f\n", convexHull(sortedPoints(readPoints)).sliding(2).map { case List(p1, p2) => p1.distance(p2) }.sum)
  }
}
```

```scala
object Solution {

  def grahamScan(points: Vector[(Int, Int)]): Double = {

    def theta(p0: (Int, Int), p1: (Int, Int)): Double = {
      val dx = p1._1 - p0._1
      val dy = p1._2 - p0._2
      val ax = math.abs(dx)
      val ay = math.abs(dy)
      val t = if (ax + ay == 0) 0.0 else dy.toFloat / (ax + ay)
      90.0 * (if (dx < 0) 2.0 - t else if (dy < 0) 4.0 + t else t)
    }

    def distance(p1: (Int, Int), p2: (Int, Int)) =
      math.sqrt(math.pow(p2._1 - p1._1, 2) + math.pow(p2._2 - p1._2, 2))

    def perimeter(ps: List[(Int, Int)]) = {
      def perimeterAcc(ps: List[(Int, Int)], acc: Double): Double =
        ps match {
          case p1 :: (p2 :: pt) => perimeterAcc(p2 :: pt, acc + distance(p1, p2))
          case _ => acc
        }
        // 每次递归，删除队首元素
      perimeterAcc(ps, 0.0)
    }

    def scan(points: List[(Int, Int)], acc: List[(Int, Int)]): List[(Int, Int)] = {
      def ccw(p0: (Int, Int), p1: (Int, Int), p2: (Int, Int)): Integer =
        (p1._1 - p0._1) * (p2._2 - p0._2) - (p1._2 - p0._2) * (p2._1 - p0._1)
      (points, acc) match {
        case (Nil, _) => acc
        case (p :: ps, p2 :: p1 :: pps) => if (ccw(p1, p2, p) >= 0) scan(ps, p :: acc) // 如果p不在内部，则p :: acc
        else scan(points, p1 :: pps) // 否则，删除p2
        case (p :: ps, xs) => scan(ps, p :: xs) // 把p挪过来
      }
    }

    val start = points.min(Ordering[(Int, Int)].on { x: (Int, Int) => (x._2, x._1) })
    val thetas = points.map(p => (p, theta(start, p), distance(p, start)))
    perimeter(start::scan(thetas.sortBy(t => (t._2, t._3)).map { case (p, t, d) => p }.toList, Nil))
  }

  def main(args: Array[String]): Unit = {
    val n = readLine.toInt
    val input = (for (i <- 1 to n)
      yield (readLine split ("""\s""")).map(_.toInt)).map { case Array(a, b) => (a, b) }.toVector
    println("%.1f".format(grahamScan(input)))
  }
}
`
```scala
object Solution {

    type Point = (Int, Int)
    type Line = (Point, Point)

    def dist(point: Point, line: Line): Double = {
        val vy = line._2._1 - line._1._1
        val vx = line._1._2 - line._2._2
        vx * (point._1 - line._1._1) + vy * (point._2 - line._1._2)
    }

    def mostDistant(line: Line, points: List[Point]): (List[Point], List[Point]) =
        points.filter(dist(_, line) > 0) match {
            case newPoints: List[Point] if newPoints.nonEmpty => (List(newPoints.maxBy(dist(_, line))), newPoints)
            case _ => (List(), List())
        }

    def buildConvexHull(line: Line, points: List[Point]): List[Line] = mostDistant(line, points) match {
        case (maxPoint, newPoints) if maxPoint.nonEmpty =>
            buildConvexHull((line._1, maxPoint.head), newPoints) ::: buildConvexHull((maxPoint.head,line._2), newPoints)
        case _ => List(line)
    }

    def convexHull(points: List[Point]): List[Line] = (points.minBy(_._1), points.maxBy(_._1)) match {
        case (minPoint, maxPoint) => buildConvexHull((minPoint, maxPoint), points) ::: buildConvexHull((maxPoint, minPoint), points)
        case _ => List()
    }
    
    def main(args: Array[String]) {
        val n = readInt
        val points = (for (_ <- 1 to n) yield readLine).map(_.split(" ")).map {
            case Array(a, b) => (a.toInt, b.toInt)
        }.toList
        
        import math._
        val perimeter = (0d /: convexHull(points)) {
            case (s, ((xa, ya), (xb, yb))) => s + sqrt(pow(xb - xa, 2) + pow(yb - ya, 2))
        }
        println(perimeter)
    }
}
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

```scala

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

```scala

```

```scala

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

```scala

```

```scala

```