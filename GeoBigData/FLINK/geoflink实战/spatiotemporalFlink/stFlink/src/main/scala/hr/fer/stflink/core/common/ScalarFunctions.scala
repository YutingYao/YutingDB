package hr.fer.stflink.core.common

import java.sql.Timestamp

import hr.fer.stflink.core.data_types.TemporalPoint
import org.apache.flink.table.functions.ScalarFunction
import org.locationtech.jts.geom._

object ST_StartTime extends ScalarFunction {
  def eval(tempPoint: TemporalPoint): Timestamp = {
    tempPoint.startTime
  }
}

object ST_EndTime extends ScalarFunction {
  def eval(tempPoint: TemporalPoint): Timestamp = {
    tempPoint.endTime
  }
}

object ST_LengthAtTime extends ScalarFunction {
  def eval(tempPoint: TemporalPoint, tinstant: Timestamp): Double = {
    tempPoint.lengthAtTime(tinstant)
  }
}

object ST_Within extends ScalarFunction {
  def eval(sourceGeom: Point, destGeom: Geometry) : Boolean = {
    sourceGeom.within(destGeom)
  }
}

object ST_MinDistance extends ScalarFunction {
  def eval(temporalPoint: TemporalPoint, geom : Geometry) : Double = {
    temporalPoint.minDistance(geom)
  }
}

object ST_Distance extends ScalarFunction {
  def eval(sourceGeom: TemporalPoint, destGeom: Geometry, tinstance: Timestamp) : Double = {
    sourceGeom.distance(destGeom, tinstance).asInstanceOf[Double]
  }
}

object ST_SubTrajectory extends ScalarFunction {
  def eval(temporalPoint: TemporalPoint, begin : Timestamp, end: Timestamp) : LineString = {
    temporalPoint.subTrajectory(begin, end)
  }
}

object areaOfInterest extends ScalarFunction {
  def eval(): Geometry = {
    var aoi = Helpers.createAreaOfInterest
    aoi.asInstanceOf[Geometry]
  }
}

object pointOfInterest extends ScalarFunction {
  def eval(): Geometry = {
    var poi = Helpers.createPointOfInterest
    poi.asInstanceOf[Geometry]
  }
}






