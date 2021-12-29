object Solution {

def f(coefficients: List[Int], powers: List[Int], x: Double): Double = 
    (coefficients.view zip powers map (e => e._1 * math.pow(x, e._2))).sum

def area(coefficients: List[Int], powers: List[Int], x: Double): Double = 
     math.Pi * math.pow(f(coefficients, powers, x), 2)

def summation(
    func: (List[Int], List[Int], Double) => Double,
    upperLimit: Int,
    lowerLimit: Int,
    coefficients: List[Int],
    powers: List[Int]): Double = 
    math.floor(((lowerLimit * 1000) to (upperLimit * 1000) 
             map (e => func(coefficients, powers, e * 0.001) * 0.001)).sum * 10) / 10
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