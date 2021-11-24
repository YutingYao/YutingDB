package hr.fer.stflink.core.data_types

import hr.fer.stflink.core.common.sttuple
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.locationtech.jts.geom._

import scala.collection.mutable.Buffer

object temporal {

	def point(cell: Tuple, window: TimeWindow, events: Iterable[sttuple],
			out: Collector[ (Int, Point) ]) = {
	  	var driverId = -1
		var point: Point = null
		
		events.map(geoLife => {
			 driverId = geoLife.id
			 point = geoLife.position
		})
		
		out.collect( driverId, point )
  }

  def temporalPoint(cell: Tuple, window: TimeWindow, events: Iterable[sttuple],
			out: Collector[ temporaltuple ]) = {
	  	var i: Int = 0
	    var g: Geometry = null	    
	    var intimeObject1: IntimeObject = null
	    var intimeObject2: IntimeObject = null	    
	    var driverId = -1
	    var tempPoint: TemporalPoint = new TemporalPoint(Buffer[PointUnit](), new TimePeriod(), null)
	    
		  events.map(line => {
			i += 1
			driverId = line.id
			var coordinate = line.position.getCoordinate()
            g = new GeometryFactory().createPoint(coordinate)
//            println("R br: " + Integer.toString(i) + " | Driver: " + line.id + " | Geometry: "+ g + " | Point:" + line.position +" | Timestamp: " + line.timestamp.toString())
            intimeObject2 = new IntimeObject(line.timestamp, g)
        	var upoint: PointUnit = temporal.createUpoint(intimeObject1, intimeObject2)
        	tempPoint.addUnit(upoint)
        	intimeObject1 = intimeObject2
		})
		out.collect( new temporaltuple(driverId, tempPoint) )
  }

  def createUpoint(intime1: IntimeObject, intime2: IntimeObject): PointUnit = {
    var upoint: PointUnit = null
  
    if(intime1 != null && intime2 != null) {
      var x0: Double = (-1) * (intime2.geom.getCoordinate.x - intime1.geom.getCoordinate.x)/(intime2.tinstant.getTime / 1000 - intime1.tinstant.getTime / 1000) * intime1.tinstant.getTime / 1000 + intime1.geom.getCoordinate.x
      var x1: Double = (intime2.geom.getCoordinate.x - intime1.geom.getCoordinate.x)/(intime2.tinstant.getTime / 1000 - intime1.tinstant.getTime / 1000)
      var y0: Double = (-1) * (intime2.geom.getCoordinate.y - intime1.geom.getCoordinate.y)/(intime2.tinstant.getTime / 1000 - intime1.tinstant.getTime / 1000) * intime1.tinstant.getTime / 1000 + intime1.geom.getCoordinate.y
      var y1: Double = (intime2.geom.getCoordinate.y - intime1.geom.getCoordinate.y)/(intime2.tinstant.getTime / 1000 - intime1.tinstant.getTime / 1000)
      var tinterval: TimeInterval = new TimeInterval(intime1.tinstant, intime2.tinstant, false, true)
      
      upoint = new PointUnit(x0, x1, y0, y1, tinterval)      
    } else if (intime1 == null) {
      var x0: Double = intime2.geom.getCoordinate.x
      var y0: Double = intime2.geom.getCoordinate.y
      var x1: Double = 0.0
      var y1: Double = 0.0
      var tinterval: TimeInterval = new TimeInterval(intime2.tinstant, intime2.tinstant, true, true)
      
      upoint = new PointUnit(x0, x1, y0, y1, tinterval)
    } else if (intime2 == null) {
      var x0: Double = intime1.geom.getCoordinate.x
      var y0: Double = intime1.geom.getCoordinate.y
      var x1: Double = 0.0
      var y1: Double = 0.0
      var tinterval: TimeInterval = new TimeInterval(intime1.tinstant, intime1.tinstant, true, true) 
      
      upoint = new PointUnit(x0, x1, y0, y1, tinterval)
    }
    upoint
  }

  //http://www.rcn.montana.edu/resources/converter.aspx
  
  def latlonToUTM(latitude: Double, longitude: Double): Coordinate = {
    
  		val zone = Math.floor( longitude/6 + 31 )
  		
	  	var letter = "X"
	        
  		if (latitude < -72) 
  			letter = "C"
	  	else if (latitude < -64) 
	  		letter = "D"
        else if (latitude < -56)
	        letter = "E"
        else if (latitude < -48)
	        letter = "F"
        else if (latitude < -40)
	        letter = "G"
        else if (latitude < -32)
	        letter = "H"
    	else if (latitude < -24)
	        letter = "J"
        else if (latitude < -16)
	        letter = "K"
        else if (latitude < -8) 
	        letter = "L"
        else if (latitude < 0)
	        letter = "M"
        else if (latitude < 8)  
	        letter = "N"
        else if (latitude < 16) 
	        letter = "P"
        else if (latitude < 24) 
	        letter = "Q"
        else if (latitude < 32) 
	        letter = "R"
        else if (latitude < 40) 
	        letter = "S"
        else if (latitude < 48) 
	        letter = "T"
        else if (latitude < 56) 
	        letter = "U"
    	else if (latitude < 64) 
	        letter = "V"
        else if (latitude < 72) 
	        letter = "W"

        var easting = 0.5*Math.log((1+Math.cos(latitude*Math.PI/180)*Math.sin(longitude*Math.PI/180-(6*zone-183)*Math.PI/180))/(1-Math.cos(latitude*Math.PI/180)*Math.sin(longitude*Math.PI/180-(6*zone-183)*Math.PI/180)))*0.9996*6399593.62/Math.pow((1+Math.pow(0.0820944379, 2)*Math.pow(Math.cos(latitude*Math.PI/180), 2)), 0.5)*(1+ Math.pow(0.0820944379,2)/2*Math.pow((0.5*Math.log((1+Math.cos(latitude*Math.PI/180)*Math.sin(longitude*Math.PI/180-(6*zone-183)*Math.PI/180))/(1-Math.cos(latitude*Math.PI/180)*Math.sin(longitude*Math.PI/180-(6*zone-183)*Math.PI/180)))),2)*Math.pow(Math.cos(latitude*Math.PI/180),2)/3)+500000
        easting = Math.round(easting*100)*0.01
        var northing = (Math.atan(Math.tan(latitude*Math.PI/180)/Math.cos((longitude*Math.PI/180-(6*zone -183)*Math.PI/180)))-latitude*Math.PI/180)*0.9996*6399593.625/Math.sqrt(1+0.006739496742*Math.pow(Math.cos(latitude*Math.PI/180),2))*(1+0.006739496742/2*Math.pow(0.5*Math.log((1+Math.cos(latitude*Math.PI/180)*Math.sin((longitude*Math.PI/180-(6*zone -183)*Math.PI/180)))/(1-Math.cos(latitude*Math.PI/180)*Math.sin((longitude*Math.PI/180-(6*zone -183)*Math.PI/180)))),2)*Math.pow(Math.cos(latitude*Math.PI/180),2))+0.9996*6399593.625*(latitude*Math.PI/180-0.005054622556*(latitude*Math.PI/180+Math.sin(2*latitude*Math.PI/180)/2)+4.258201531e-05*(3*(latitude*Math.PI/180+Math.sin(2*latitude*Math.PI/180)/2)+Math.sin(2*latitude*Math.PI/180)*Math.pow(Math.cos(latitude*Math.PI/180),2))/4-1.674057895e-07*(5*(3*(latitude*Math.PI/180+Math.sin(2*latitude*Math.PI/180)/2)+Math.sin(2*latitude*Math.PI/180)*Math.pow(Math.cos(latitude*Math.PI/180),2))/4+Math.sin(2*latitude*Math.PI/180)*Math.pow(Math.cos(latitude*Math.PI/180),2)*Math.pow(Math.cos(latitude*Math.PI/180),2))/3)
        if (letter < "M")
            northing = northing + 10000000
        northing = Math.round(northing*100)*0.01
        var coordinate: Coordinate = new Coordinate(easting, northing)
        coordinate
        
  }


}
