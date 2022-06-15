package hr.fer.stflink.core.data_types

import java.sql.Timestamp

import org.locationtech.jts.geom._

case class PointUnit(override var x0: Double, override var x1: Double, override var y0: Double, override var y1: Double, override var period: TimeInterval) extends UnitType(x0,x1,y0,y1, period) {

  def projection(): LineString = {
      var coorXend: Double = x0 + x1*period.end.getTime()/1000
      var coorYend: Double = y0 + y1*period.end.getTime()/1000

      var coorXstart: Double = x0 + x1*period.start.getTime()/1000
      var coorYstart: Double = y0 + y1*period.start.getTime()/1000

      val coord1: Coordinate = new Coordinate(coorXstart,coorYstart)
      val coord2: Coordinate = new Coordinate(coorXend,coorYend)

      var line: LineString = new GeometryFactory().createLineString(Array(coord1, coord2))

      return line
  }

  def speed(): Double = {
    var line: LineString = projection()
    return (line.getLength/((period.end.getTime()-period.start.getTime())/1000))
  }

  def direction(): Double = {

    var angle = 0.0
    var coorXstart: Double = x0 + x1*period.start.getTime()/1000
    var coorYstart: Double = y0 + y1*period.start.getTime()/1000
    
    var coorXend: Double = x0 + x1*period.end.getTime()/1000
    var coorYend: Double = y0 + y1*period.end.getTime()/1000
    
    if(coorYstart < coorYend) {
      val coorX3: Double = coorXend
      val coorY3: Double = coorYstart
      
      val coord1: Coordinate = new Coordinate(coorXstart,coorYstart)
      val coord2: Coordinate = new Coordinate(coorXend,coorYend)
      val coord3: Coordinate = new Coordinate(coorX3,coorY3)
      var line1: LineString = new GeometryFactory().createLineString(Array(coord1, coord3))
      var line2: LineString = new GeometryFactory().createLineString(Array(coord2, coord3))
      val length1: Double = line1.getLength
      val length2: Double = line2.getLength
      
      if(coorXstart < coorXend) {
          angle = Math.toDegrees(Math.atan(length2/length1))
      } else if (coorXstart > coorXend) {
          angle = 180 - Math.toDegrees(Math.atan(length2/length1))
      } else {
          angle = 90
      }     
    } else if(coorYstart > coorYend) {
      var coorX3: Double = coorXstart
      var coorY3: Double = coorYend
      
      var coord1: Coordinate = new Coordinate(coorXstart, coorYstart)
      val coord2: Coordinate = new Coordinate(coorXend,coorYend)
      val coord3: Coordinate = new Coordinate(coorX3,coorY3)
      var line1: LineString = new GeometryFactory().createLineString(Array(coord1, coord3))
      var line2: LineString = new GeometryFactory().createLineString(Array(coord2, coord3))
      val length1: Double = line1.getLength
      val length2: Double = line2.getLength
      
      if(coorXstart > coorXend) {
          angle = 180 + Math.toDegrees(Math.atan(length1/length2))
      } else if(coorXstart < coorXend) {
          angle = 360 - Math.toDegrees(Math.atan(length1/length2))
      } else {
          angle = 270
      }
      
    } else {
      if (coorXstart > coorXend) {
          angle = 180
      } else {
        angle = 0.0
      }
    }
    angle
   }
  
  def projectionToTime(begin: Timestamp, end: Timestamp): LineString = {
      var coorXend: Double = x0 + x1*end.getTime()/1000
      var coorYend: Double = y0 + y1*end.getTime()/1000

      var coorXstart: Double = x0 + x1*begin.getTime()/1000
      var coorYstart: Double = y0 + y1*begin.getTime()/1000

      val coord1: Coordinate = new Coordinate(coorXstart,coorYstart)
      val coord2: Coordinate = new Coordinate(coorXend,coorYend)
      var line: LineString = new GeometryFactory().createLineString(Array(coord1, coord2)) 

      return line
  }

}