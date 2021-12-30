object Solution {

    def dist(x1: Int, y1: Int, x2: Int, y2: Int): Double = {
        scala.math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
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
            if (i > 1) sum += dist(x(i), y(i), x(i-1), y(i-1));
        }
        sum += dist(x(1), y(1), x(N), y(N))
        println(sum)
    }
}