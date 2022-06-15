package util

class Average {
  private var a: Int = 0
  private var b: Int = 0
  private var average:Int = 0
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


  def setAverage(average : Int){
    this.average = average
  }
  def getAverage(): Int ={
    average
  }

  def setTyp(typ : String){
    this.typ = typ
  }
  def getTyp(): String ={
    typ
  }

  override def toString: String = "{" + "a=" + a + ", b=" + b  + ", type" + typ + ", average" + average +'}'

}
