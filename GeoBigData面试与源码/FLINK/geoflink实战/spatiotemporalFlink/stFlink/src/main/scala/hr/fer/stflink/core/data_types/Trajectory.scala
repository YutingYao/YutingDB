package hr.fer.stflink.core.data_types

import org.locationtech.jts.geom.{Coordinate, GeometryFactory, LineString}

class Trajectory(var tDomain: TimePeriod, var lineS: LineString) {
    var _temporalDomain: TimePeriod = tDomain
    var _linestring: LineString = lineS
    
    def temporalDomain = {
      _temporalDomain
    }
    
    def temporalDomain_= (value: TimePeriod): Unit = {
		_temporalDomain = value
    }
    
    def linestring = {
      _linestring
    }
    
    def linestring_= (value: LineString): Unit = {
		_linestring = value
    }
    
    def this() {
      this(new TimePeriod(), new GeometryFactory().createLineString(Array(new Coordinate(1,1))))
    }

}
