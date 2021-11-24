package hr.fer.stflink.core.common

import hr.fer.stflink.core.data_types.temporal
import org.apache.flink.table.api.TableEnvironment
import org.locationtech.jts.geom._

object Helpers {

  def createAreaOfInterest : Polygon = {

    val lat1 = 39.9916666666667
    val lon1 = 116.326533333333
    val lat2 = 40.0013130
    val lon2 = 116.9992424400

    var coordinate1 = temporal.latlonToUTM(lat1, lon1)
    var coordinate2 = temporal.latlonToUTM(lat1, lon2)
    var coordinate3 = temporal.latlonToUTM(lat2, lon1)
    var coordinate4 = temporal.latlonToUTM(lat2, lon2)

    var coords: Array[Coordinate] = Array(coordinate1, coordinate2, coordinate3, coordinate4, coordinate1)
    val factory = new GeometryFactory()

    var ring = factory.createLinearRing(coords)
    var holes: Array[LinearRing] = null
    var areaOfInterest = factory.createPolygon(ring, holes)

    areaOfInterest
  }

  def createPointOfInterest : Point = {

    val factory = new GeometryFactory()
    val pointOfInterest: Point = factory.createPoint(new Coordinate(442414.25, 4426973.53))

    pointOfInterest
  }

  def registerSTFunctions(tEnv: TableEnvironment) {
    tEnv.registerFunction("areaOfInterest", areaOfInterest)
    tEnv.registerFunction("pointOfInterest", pointOfInterest)
    tEnv.registerFunction("ST_Distance", ST_Distance)
    tEnv.registerFunction("ST_EndTime", ST_EndTime)
    tEnv.registerFunction("ST_LengthAtTime", ST_LengthAtTime)
    tEnv.registerFunction("ST_MinDistance", ST_MinDistance)
    tEnv.registerFunction("ST_StartTime", ST_StartTime)
    tEnv.registerFunction("ST_SubTrajectory", ST_SubTrajectory)
    tEnv.registerFunction("ST_Within", ST_Within)
  }
}
