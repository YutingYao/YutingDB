package com.machinedoll.experiment.processor

import org.apache.avro.Schema
import org.apache.avro.specific.SpecificData
import org.apache.flink.api.common.functions.MapFunction

case class SerializeDataFunction[T](schemaVersion: Schema) extends MapFunction[T, SpecificData]{
  override def map(t: T): SpecificData = ???
}
