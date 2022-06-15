package hr.fer.stflink.core.data_types

import java.sql.Timestamp

import org.locationtech.jts.geom._

import scala.collection.mutable.Buffer

trait TemporalObject {
  
  def atInstant(tinstant: Timestamp): Geometry
  def trajectory (p: Point, tinstant: Timestamp): TemporalPoint
  def startTime(): Timestamp
  def endTime(): Timestamp
  def nereastApproach(o: Geometry, tinterval: TimeInterval): Buffer[Timestamp]
  def intersection(o: Geometry, tinterval: TimeInterval): TemporalObject
  def intersects(o: Geometry, tinterval: TimeInterval): Boolean
  def atInitial(): IntimeObject
  def atFinal(): IntimeObject
  def present(tinstant: Timestamp): Boolean
  def present(tintervals: Buffer[TimeInterval]): Boolean
  def passes(o: Geometry): Boolean
  def at(o:Geometry): TemporalObject
  def deftime(): TimeInterval

}