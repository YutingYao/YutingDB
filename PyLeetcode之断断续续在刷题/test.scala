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
        
        // "/:" 是foldLeft的缩写
        import math._
        val perimeter = (0d /: convexHull(points)) {
            case (s, ((xa, ya), (xb, yb))) => s + sqrt(pow(xb - xa, 2) + pow(yb - ya, 2))
        }
        println(perimeter)
    }
}

print(0d)