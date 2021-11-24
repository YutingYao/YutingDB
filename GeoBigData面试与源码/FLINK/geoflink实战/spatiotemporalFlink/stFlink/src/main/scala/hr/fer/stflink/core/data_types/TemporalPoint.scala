package hr.fer.stflink.core.data_types

import java.sql.Timestamp
import java.util.Date

import org.locationtech.jts.geom._

import scala.collection.mutable.{ArrayBuffer, Buffer}
import scala.util.control._

case class TemporalPoint(var u: Buffer[PointUnit], override var temporalDomain: TimePeriod, override var linestring: LineString) extends Trajectory(temporalDomain, linestring) with TemporalObject {

  private var _units: Buffer[PointUnit] = u

  def units = _units
  def units_= (value: Buffer[PointUnit]): Unit = _units = value
 
  def addUnit(upoint: PointUnit):Unit = {
    var upArray: Buffer[PointUnit] = this.units
    val noUnits: Int = upArray.length
    if(noUnits == 0) {
      this.units += upoint
    } else {
      val up1: PointUnit = this.units(noUnits-1)
      if(up1.period.end.getTime() > upoint.period.start.getTime()) {
        println("Error, points are not sorted correctly!")
      } else {
        this.units += upoint
      }
    }
    this.temporalDomain.start = this.startTime
    this.temporalDomain.end = this.endTime
    
    val geomFactory = new GeometryFactory()
    val x = upoint.x0 + upoint.x1*upoint.period.end.getTime()/1000
  	val y = upoint.y0 + upoint.y1*upoint.period.end.getTime()/1000
  	val coord = new Coordinate(x, y)
    
    if(this.linestring != null) {
      
    	var coords = this.linestring.getCoordinates().toBuffer
	    coords.append(coord)
	    this.linestring = geomFactory.createLineString(coords.toArray)
	    
    } else {
      
      val x0 = upoint.x0 + upoint.x1*upoint.period.start.getTime()/1000
      val y0 = upoint.y0 + upoint.y1*upoint.period.start.getTime()/1000
      val coord0 = new Coordinate(x0, y0)
      this.linestring = geomFactory.createLineString(Array(coord0, coord))
      
    }
  }
  
  
  def subTrajectory(begin: Timestamp, end: Timestamp): LineString = { 
	var retLineString = this.linestring
      if(!(this.startTime.equals(begin) && this.endTime.equals(end))) {
    	  val coordinates = this.findCoordinateArray(begin, end)
		  val geomFactory = new GeometryFactory()
    	  retLineString = geomFactory.createLineString(coordinates)
      }
      retLineString
  }
  
  def findCoordinateArray(begin: Timestamp, end: Timestamp): Array[Coordinate] = {
    val loop = new Breaks
    var upArray: Buffer[PointUnit] = this.units
    val noUnits: Int = upArray.length

    var coords = ArrayBuffer[Coordinate]()

    loop.breakable {
      for (i <- 0 to noUnits-1 by 1) {
        if (upArray(i).period.start.getTime() >= begin.getTime() && upArray(i).period.end.getTime() <= end.getTime()) {
	         var x1 = upArray(i).x0 + upArray(i).x1*upArray(i).period.start.getTime()/1000
		     var y1 = upArray(i).y0 + upArray(i).y1*upArray(i).period.start.getTime()/1000
		  	 var x2 = upArray(i).x0 + upArray(i).x1*upArray(i).period.end.getTime()/1000
		  	 var y2 = upArray(i).y0 + upArray(i).y1*upArray(i).period.end.getTime()/1000
		  	 var coord1 = new Coordinate(x1,y1)
		  	 var coord2 = new Coordinate(x2,y2)
	         coords.append(coord2)
        }
         
      }
    }
    coords.toArray
  }
  
  def locations(): Buffer[Point] = {
      var buff: Buffer[Point] = Buffer[Point]();
      val loop = new Breaks
      var upArray: Buffer[PointUnit] = this.units
      val noUnits: Int = upArray.length
      loop.breakable{
        for(i <- 0 to noUnits-1 by 1) {
          var up: PointUnit = upArray(i)
          var begin: Timestamp = up.period.start
          var end: Timestamp = up.period.end
          if(up.period.leftClosed) {
            val t: Timestamp = up.period.start
            val p: Point = atInstant(t)
            buff += p
          } 
          if(up.period.rightClosed) {
            val t: Timestamp = up.period.end
            val p: Point = atInstant(t)
            buff += p
          }
        }
      }
     buff
  }
  
  
  def lengthAtTime (tinstant: Timestamp): Double = {
    var totalLength: Double = 0.0
    val loop = new Breaks
    var upArray: Buffer[PointUnit] = this.units
    val noUnits: Int = upArray.length
    var up: PointUnit = null
    var upFirst: PointUnit = upArray(0)
    var upLast: PointUnit = upArray(noUnits - 1)
    if(upFirst.period.start.getTime() <= tinstant.getTime() && upLast.period.end.getTime() >= tinstant.getTime()) {
      loop.breakable{
        for(i <- 0 to noUnits-1 by 1) {
          up = upArray(i)
          
          var begin: Timestamp = up.period.start
          var end: Timestamp = up.period.end
          
          if(end.getTime() <= tinstant.getTime()) {
            var line: LineString = up.projection()
            totalLength += line.getLength()
          } else if((begin.getTime() < tinstant.getTime() && end.getTime() > tinstant.getTime())) {
            var line: LineString = up.projectionToTime(begin, tinstant)
            totalLength += line.getLength()
          } else {
            loop.break()
          }
        }
      }  
    } 
    totalLength
  }

  /* TODO
  
  def speed(): TemporalReal = {
    var buff: Buffer[RealUnit] = Buffer[RealUnit]();
    val loop = new Breaks
    var upArray: Buffer[PointUnit] = this.units
    val noUnits: Int = upArray.length
    loop.breakable{
        for(i <- 0 to noUnits-1 by 1) {
          var up: PointUnit = upArray(i)
          val line: LineString = projectionUP(up)
          val length: Double = line.getLength()
          val tStart: Timestamp = up.period.start
          val tEnd: Timestamp = up.period.end

          var c: Double = 0.0
          
          if(length != 0.0) {
              c = length/((tEnd.getTime() - tStart.getTime()) / 1000)
          }
          buff += new RealUnit(0.0, 0.0, c, up.period)
        }
      }
    new TemporalReal(buff)
  }

  */
  
  def maxDistance(o: Geometry) : Double = {
    val loop = new Breaks
      var startT: Long = this.startTime.getTime()
      var endT: Long = this.endTime.getTime()
      var localMaxValue: Double = this.distance(o, new Timestamp(startT)).asInstanceOf[Double]
      loop.breakable{
        for(t <- startT + 1000 to endT by 1000) {
          val resultMaxValue: Double = this.distance(o, new Timestamp(t)).asInstanceOf[Double]
          if(localMaxValue < resultMaxValue) {
            localMaxValue = resultMaxValue
          }
        }
      }
      localMaxValue
  }
  
  def minDistance(o: Geometry): Double = {
    val loop = new Breaks
      var startT: Long = this.startTime.getTime()
      var endT: Long = this.endTime.getTime()
      var localMinValue: Double = this.distance(o, new Timestamp(startT)).asInstanceOf[Double]
      loop.breakable{
        for(t <- startT + 1000 to endT by 1000) {
          val resultMinValue: Double = this.distance(o, new Timestamp(t)).asInstanceOf[Double]
          if(localMinValue > resultMinValue) {
            localMinValue = resultMinValue
          }
        }
      }
      localMinValue
  }
  
  def distance(o: Geometry, tinstance: Timestamp): Any = {
    val loop = new Breaks
    var upArray: Buffer[PointUnit] = this.units
    val noUnits: Int = upArray.length
    var isFound: Boolean = false
    var foundUp: PointUnit = null
    loop.breakable{
      for(i <- 0 to noUnits-1 by 1) {
        var up: PointUnit = upArray(i)
        var begin: Timestamp = up.period.start
        var end: Timestamp = up.period.end
        if(tinstance.getTime() < begin.getTime()) {
          isFound = true
          foundUp = up
          loop.break
        }
        if(up.period.leftClosed) {
          if(tinstance.getTime() == begin.getTime()) {
            isFound = true
            foundUp = up
            loop.break
          }
        }
        if(up.period.rightClosed) {
          if(tinstance.getTime() == end.getTime()) {
            isFound = true
            foundUp = up
            loop.break
          }
        }
        if((tinstance.getTime() > begin.getTime()) && (tinstance.getTime() < end.getTime())) {
          isFound = true
          foundUp = up
          loop.break
        }
      }
    }
    var distance: Any = null
    if (isFound) {
       distance = Math.sqrt(Math.pow(foundUp.x1 * tinstance.getTime()/1000 + foundUp.x0 - o.getCoordinate().x, 2) + Math.pow(foundUp.y1 * tinstance.getTime()/1000 + foundUp.y0 - o.getCoordinate().y, 2))  
    }
    distance
  }
  
  def atInstant(t: Timestamp): Point = {
    var found: Boolean = false
    val loop = new Breaks
    var upArray: Buffer[PointUnit] = this.units
    val noUnits: Int = upArray.length
    var up: PointUnit = upArray(0)
    loop.breakable{
      for(i <- 0 to noUnits-1 by 1) {
        up = upArray(i)
        var begin: Timestamp = up.period.start
        var end: Timestamp = up.period.end
        if(t.getTime() < begin.getTime()) loop.break
        if(up.period.leftClosed) {
          if(t.getTime() == begin.getTime()) {
            found = true
            loop.break
          }
        }
        if(up.period.rightClosed) {
          if(t.getTime() == end.getTime()) {
            found = true
            loop.break
          }
        }
        if((t.getTime() > begin.getTime()) && (t.getTime() < end.getTime())) {
          found = true
          loop.break
        }
      }
    }
    var retValue: Point = null
    if(found) {
      var x: Double = up.x0 + up.x1 * t.getTime()/1000
      var y: Double = up.y0 + up.y1 * t.getTime()/1000
      retValue = (new GeometryFactory()).createPoint(new Coordinate(x,y))
    }
    return retValue
  }
  
  
  
  def startTime(): Timestamp = {
     var upArray: Buffer[PointUnit] = this.units
     var up: PointUnit = upArray(0)
     up.period.start
  }
  
  def endTime(): Timestamp = {
    var upArray: Buffer[PointUnit] = this.units
    var numberUnits: Int = upArray.length
    var up: PointUnit = upArray(numberUnits-1)
    up.period.end
  }
  
   
  def atInitial(): IntimeObject = {
     var begin: Timestamp = this.startTime()
     new IntimeObject(begin, this.atInstant(begin))
  }
  
  def atFinal(): IntimeObject = {
    var end: Timestamp = this.endTime()
    new IntimeObject(end, this.atInstant(end))
  }
  
  def present(t: Timestamp): Boolean = {  
    var found: Boolean = false
    val loop = new Breaks
    var upArray: Buffer[PointUnit] = this.units
    val noUnits: Int = upArray.length
    var up: PointUnit = upArray(0)
    loop.breakable{
      for(i <- 0 to noUnits-1 by 1) {
        up = upArray(i)
        var begin: Timestamp = up.period.start
        var end: Timestamp = up.period.end    
        
        if(t.getTime() < begin.getTime()) loop.break
        if(up.period.leftClosed) {
          if(t.getTime() == begin.getTime()) {
            found = true
            loop.break
          }
        }
        if(up.period.rightClosed) {
          if(t.getTime() == end.getTime()) {
            found = true
            loop.break
          }
        }
        if((t.getTime() > begin.getTime()) && (t.getTime() < end.getTime())) {
          found = true
          loop.break
        }
      }
    }
    found
  }
  
  def present(tintervals: Buffer[TimeInterval]): Boolean = {
    val loop = new Breaks
    var innerLoop = new Breaks
    var upArray: Buffer[PointUnit] = this.units
    val noUnits: Int = upArray.length
    val noTimeUnits: Int = tintervals.length
    var up: PointUnit = upArray(0)
    var t: TimeInterval = tintervals(0)
    loop.breakable{
      for(i <- 0 to noUnits-1 by 1) {
        up = upArray(i)
        var begin: Timestamp = up.period.start
        var end: Timestamp = up.period.end
        for (j <- 0 to noTimeUnits-1 by 1) {
          t = tintervals(j)
          if (end.getTime() < t.start.getTime() && begin.getTime() > t.end.getTime()) {
            //nothing
          } else if (begin.getTime() == t.start.getTime() && t.leftClosed == true && up.period.leftClosed == true) {
            return true
          } else if (begin.getTime() == t.end.getTime() && up.period.rightClosed == true && up.period.leftClosed == true) {
            return true
          } else if (end.getTime() == t.start.getTime() && t.leftClosed == true && up.period.rightClosed == true) {
            return true
          } else if (end.getTime() == t.end.getTime() && up.period.rightClosed == true && t.rightClosed == true) { 
            return true
          } else if (begin.getTime() > t.start.getTime() && begin.getTime() < t.end.getTime()) {
            return true
          } else if (end.getTime() > t.start.getTime() && end.getTime() < t.end.getTime()) {
            return true
          }
        }
      }
    }
    false
  }
  
  def passes(o: Geometry): Boolean = {
    val loop = new Breaks
    var upArray: Buffer[PointUnit] = this.units
    val noUnits: Int = upArray.length
    var intersectLine = false
    
    if (o == null || upArray.length == 0) return false
    
    var up: PointUnit = upArray(0)
    
    loop.breakable{
      for(i <- 0 to noUnits-1 by 1) {
        up = upArray(i)
        val line: LineString = projectionUP(up)
        if (o.intersects(line)) {
          intersectLine = true
          loop.break
        }
      }
    } 
    intersectLine
  }
  
  
  
  def deftime(): TimeInterval = {
    var ioStart: IntimeObject = this.atInitial()
    var ioEnd: IntimeObject = this.atFinal()    
    new TimeInterval(ioStart.tinstant, ioEnd.tinstant, true, true)
  }


  def pointAtTime(t: Timestamp): Point = {
    var found: Boolean = false
    val loop = new Breaks
    var upArray: Buffer[PointUnit] = this.units
    val noUnits: Int = upArray.length
    var up: PointUnit = upArray(0)
    loop.breakable{
      for(i <- 0 to noUnits-1 by 1) {
        up = upArray(i)
        var begin: Timestamp = up.period.start
        var end: Timestamp = up.period.end
        if(t.getTime() < begin.getTime()) loop.break
        if(up.period.leftClosed) {
          if(t.getTime() == begin.getTime()) {
            found = true
            loop.break
          }
        }
        if(up.period.rightClosed) {
          if(t.getTime() == end.getTime()) {
            found = true
            loop.break
          }
        }
        if((t.getTime() > begin.getTime()) && (t.getTime() < end.getTime())) {
          found = true
          loop.break
        }
      }
    }
    var retValue: Point = null
    if(found) {
      var x: Double = up.x0 + up.x1 * t.getTime() / 1000 
      var y: Double = up.y0 + up.y1 * t.getTime() / 1000
      retValue = (new GeometryFactory()).createPoint(new Coordinate(x,y))
    }
    return retValue
  }
  
  def unitpointInTime(time: Timestamp):PointUnit = {
    var upArray: Buffer[PointUnit] = this.units
    var foundUp: PointUnit = null;
    val noUnits: Int = upArray.length
    val loop = new Breaks
    loop.breakable{
      for(i <- 0 to noUnits-1 by 1) {
        var up: PointUnit = upArray(i)
        var begin: Timestamp = up.period.start
        var end: Timestamp = up.period.end
        if(time.getTime() < begin.getTime()) {
          loop.break
        }
        if(up.period.leftClosed) {
          if(time.getTime() == begin.getTime()) {
            foundUp = up
            loop.break
          }
        }
        if(up.period.rightClosed) {
          if(time.getTime() == end.getTime()) {
            foundUp = up
            loop.break
          }
        }
        if((time.getTime() > begin.getTime()) && (time.getTime() < end.getTime())) {
          foundUp = up
          loop.break
        }
      }
    }
    return foundUp
  }
    
  def projectionUP(up: PointUnit): LineString = {
      var geometryFactory = new GeometryFactory()

      var x1: Double = up.x0 + up.x1*up.period.start.getTime()/1000
      var y1: Double = up.y0 + up.y1*up.period.start.getTime()/1000
      var x2: Double = up.x0 + up.x1*up.period.end.getTime()/1000
      var y2: Double = up.y0 + up.y1*up.period.end.getTime()/1000
      val coord1: Coordinate = new Coordinate(x1,y1)
      val coord2: Coordinate = new Coordinate(x2,y2)
      
      var line: LineString = new GeometryFactory().createLineString(Array(coord1, coord2)) 
      return line
  }
  
  def timeAtPoint(p: Point): Timestamp = {
    /*
     * TO DO
     */
    new Timestamp((new Date()).getTime())
  }
  
  def timeAtLength (length: Double): Timestamp = {
    var timeAtLength: Timestamp = null
    var tempLength: Double = 0.0
    val loop = new Breaks
    var upArray: Buffer[PointUnit] = this.units
    val noUnits: Int = upArray.length
    var up: PointUnit = null
      loop.breakable{
        for(i <- 0 to noUnits-1 by 1) {
          up = upArray(i)
          
          var begin: Timestamp = up.period.start
          var end: Timestamp = up.period.end
          
          tempLength += up.projection().getLength()
          
          if(tempLength > length) {
            loop.break()
            
            //odrediti trenutak
          } 
        }
      } 
    timeAtLength   
  }
  
  def trajectory (p: Point, tinstant: Timestamp): TemporalPoint = {
      var upArray: Buffer[PointUnit] = this.units
      if (upArray.length == 0) {
        return null
      } else {
        var lineSegmentArray: Buffer[LineString] = Buffer[LineString]()
        lineSegmentArray += projectionUP(upArray(0))
        val noUnits: Int = upArray.length
        val loop = new Breaks
        loop.breakable {
          for (i <- 1 to noUnits by 1) {
            lineSegmentArray += projectionUP(upArray(i))
          }
        }
      }
      
      // fali implementacija pretvaranja u TemporalPoint
      
      this
  }
  
  def nereastApproach(o: Geometry, tinterval: TimeInterval): Buffer[Timestamp] = {
    /*
     * TO DO
     */
    var buff: Buffer[Timestamp] = Buffer[Timestamp]();
    buff
  }
  
  def intersection(o: Geometry, tinterval: TimeInterval): TemporalObject = {
    /*
     * TO DO
     */
    this
  }
  
  
  def intersects(o: Geometry, tinterval: TimeInterval): Boolean = {
    /*
     * TO DO
     */
    true
  }
  
  def at(o:Geometry): TemporalObject = {
    /*
     * TO DO
     * 
     */
      this
  }
  

}