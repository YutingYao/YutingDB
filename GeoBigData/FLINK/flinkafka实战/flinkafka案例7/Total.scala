package util


class Total{
  private var a: Int = 0
  private var b: Int = 0
  private var sum: Int = a + b
  private var average:Int = (a + b)/2
  private var typ: String = null

  def getA(): Int ={
    a
  }

  def setA(a : Int){
    this.a = a
  }


  def getB(): Int ={
    b
  }

  def setB(b : Int){
    this.b = b
  }
  def getSum(): Int ={
    sum
  }

  def setSum(sum : Int){
    this.sum = sum
  }


  def setTyp(typ : String){
    this.typ = typ
  }
  def getTyp(): String ={
    typ
  }

  override def toString: String = "{" + "a=" + a + ", b=" + b + ", sum=" + sum + ", type" + typ + '}'
}


