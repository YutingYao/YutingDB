package hr.fer.stflink.core.data_types

import java.sql.Timestamp
import org.locationtech.jts.geom._


case class IntimeObject(t: Timestamp, g: Geometry) {
  
   private var _tinstant: Timestamp = t
   private var _geom: Geometry = g
   
   def tinstant = _tinstant
   def geom = _geom
   
   def tinstant_= (value: Timestamp): Unit = _tinstant = value
   def geom_= (value: Geometry): Unit = _geom = value
   
//   def this() {
//     this(new Timestamp((new Date()).getTime()), (new GeometryFactory()).createPoint(new Coordinate(1,1)))
//   }
 
}