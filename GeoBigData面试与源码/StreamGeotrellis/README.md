<!-- vscode-markdown-toc -->
* 1. [SBT](#SBT)
* 2. [docker-compose.service.yml](#docker-compose.service.yml)
* 3. [docker-compose.kafka.yml](#docker-compose.kafka.yml)
* 4. [Makefile](#Makefile)
* 5. [/producer/src/main](#producersrcmain)
	* 5.1. [/resources/application.conf](#resourcesapplication.conf)
	* 5.2. [/producer/src/main/scala/com/azavea/](#producersrcmainscalacomazavea)
		* 5.2.1. [Main.scala](#Main.scala)
		* 5.2.2. [/conf/LC8ScenesConfig.scala](#confLC8ScenesConfig.scala)
		* 5.2.3. [/generator/Generator.scala](#generatorGenerator.scala)
		* 5.2.4. [/generator/package.scala](#generatorpackage.scala)
* 6. [/project/](#project)
	* 6.1. [Dependencies.scala](#Dependencies.scala)
	* 6.2. [Environment.scala](#Environment.scala)
	* 6.3. [Version.scala](#Version.scala)
	* 6.4. [build.properties](#build.properties)
	* 6.5. [plugins.sbt](#plugins.sbt)
* 7. [/streaming/src/main/](#streamingsrcmain)
	* 7.1. [/resources/application.conf](#resourcesapplication.conf-1)
	* 7.2. [/scala/com/azavea/](#scalacomazavea)
		* 7.2.1. [Main.scala](#Main.scala-1)
		* 7.2.2. [package.scala](#package.scala)
		* 7.2.3. [/conf/GDALEnabled.scala](#confGDALEnabled.scala)
		* 7.2.4. [/conf/IngestStreamConfig.scala](#confIngestStreamConfig.scala)
		* 7.2.5. [/json/Fields.scala](#jsonFields.scala)
		* 7.2.6. [/json/Implicits.scala](#jsonImplicits.scala)
		* 7.2.7. [/json/PolygonalStatsDouble.scala](#jsonPolygonalStatsDouble.scala)
		* 7.2.8. [/json/ProcessingResult.scala](#jsonProcessingResult.scala)
		* 7.2.9. [/json/package.scala](#jsonpackage.scala)
		* 7.2.10. [/kafka/MessageSender.scala](#kafkaMessageSender.scala)
		* 7.2.11. [/streaming/IngestStream.scala](#streamingIngestStream.scala)
		* 7.2.12. [/streaming/ProcessStream.scala](#streamingProcessStream.scala)
		* 7.2.13. [/streaming/package.scala](#streamingpackage.scala)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->

##  1. <a name='SBT'></a>SBT

> path

javaOptions ++= Seq(s"-Djava.library.`path`=${Environment.`ldLibraryPath`}")

lazy val `ldLibraryPath` = either("LD_LIBRARY_PATH", "/usr/local/lib")

> MergeStrategy

assemblyMergeStrategy in assembly := {
    case "reference.conf" => MergeStrategy.concat
    case "application.conf" => MergeStrategy.concat
    case n if n.endsWith(".SF") || n.endsWith(".RSA") || n.endsWith(".DSA") => MergeStrategy.discard
    case "META-INF/MANIFEST.MF" => MergeStrategy.discard
    case _ => MergeStrategy.first
  },

```sbt
lazy val commonSettings = Seq(
  version := "0.0.1-SNAPSHOT",
  scalaVersion := Version.scalaVersion,
  crossScalaVersions := Version.crossScalaVersions,
  organization := "com.azavea",
  scalacOptions ++= Seq(
    "-deprecation",
    "-unchecked",
    "-language:implicitConversions",
    "-language:reflectiveCalls",
    "-language:higherKinds",
    "-language:postfixOps",
    "-language:existentials",
    "-feature"
  ),

  outputStrategy := Some(StdoutOutput),

  addCompilerPlugin("org.spire-math" % "kind-projector" % Version.kindProjector cross CrossVersion.binary),
  addCompilerPlugin("org.scalamacros" %% "paradise" % Version.macroParadise cross CrossVersion.full),

  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"),
    Resolver.bintrayRepo("azavea", "maven"),
    Resolver.bintrayRepo("azavea", "geotrellis")
  ),

  fork := true,
  Test / fork := true,
  Test / parallelExecution := false,
  javaOptions ++= Seq(s"-Djava.library.path=${Environment.ldLibraryPath}"),

  test in assembly := {},

  /** Shapeless shading due to circe 由于环形造成的形状阴影 */
  assemblyShadeRules in assembly := {
    val shadePackage = "com.azavea.shaded"
    Seq(
      ShadeRule.rename("shapeless.**" -> s"$shadePackage.shapeless.@1").inAll,
      ShadeRule.rename("javax.ws.rs.**" -> s"$shadePackage.javax.ws.rs.@1").inAll
    )
  },
  
  assemblyMergeStrategy in assembly := {
    case "reference.conf" => MergeStrategy.concat
    case "application.conf" => MergeStrategy.concat
    case n if n.endsWith(".SF") || n.endsWith(".RSA") || n.endsWith(".DSA") => MergeStrategy.discard
    case "META-INF/MANIFEST.MF" => MergeStrategy.discard
    case _ => MergeStrategy.first
  },

  licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html")),
  headerLicense := Some(HeaderLicense.ALv2("2019", "Azavea"))
)

lazy val root =
  Project("geotrellis-streaming-demo", file("."))
    .aggregate(producer, streaming)
    .settings(commonSettings: _*)

lazy val producer =
  (project in file("producer"))
    .dependsOn(streaming)
    .settings(commonSettings: _*)
    .settings(libraryDependencies += Dependencies.scalaTest % Test)

lazy val streaming =
  (project in file("streaming"))
    .settings(commonSettings: _*)
    .settings(libraryDependencies ++= Seq(
      Dependencies.geotrellisContrib,
      Dependencies.circeCore,
      Dependencies.circeGeneric,
      Dependencies.circeGenericExtras,
      Dependencies.circeParser,
      Dependencies.sparkCore,
      Dependencies.sparkStreaming,
      Dependencies.sparkStreamingKafka,
      Dependencies.decline,
      Dependencies.scalaTest % Test
    ))
```

##  2. <a name='docker-compose.service.yml'></a>docker-compose.service.yml

```yml
version: '2'
services:
  zookeeper:
    image: wurstmeister/zookeeper
    ports:
      - "2181:2181"
  kafka:
    image: wurstmeister/kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_HOST_NAME: kafka
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
  geotrellis-streaming-demo:
    image: quay.io/geodocker-pointcloud-mbio
    command: 
      - /bin/sh
      - -c
      - |
        spark-submit \
          --driver-memory 2G \
          --executor-memory 2G \
          --conf spark.scheduler.mode=FAIR \
          --conf spark.executor.cores=1 \
          --conf spark.streaming.kafka.maxRatePerPartition=6 \
          --class com.azavea.Main \
          /opt/geotrellis-streaming-demo/geotrellis-streaming-demo-assembly.jar --topic imagery_to_process
```

##  3. <a name='docker-compose.kafka.yml'></a>docker-compose.kafka.yml

```yml
version: '2'
services:
  zookeeper:
    image: wurstmeister/zookeeper
    container_name: imagery-processing_zookeeper_1
    ports:
      - "2181:2181"
  kafka:
    image: wurstmeister/kafka
    container_name: imagery-processing_kafka_1
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_HOST_NAME: kafka
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
```

##  4. <a name='Makefile'></a>Makefile

```yml
ASSEMBLY_VERSION := 0.0.1-SNAPSHOT
ASSEMBLY         := streaming/target/scala-2.11/geotrellis-streaming-demo-0.0.1-SNAPSHOT.jar

build: 
	./sbt "project streaming" assembly

clean:
	./sbt clean -no-colors

local-spark-demo:
	spark-submit \
        --master local[*] \
        --driver-memory 2G \
        --conf spark.scheduler.mode=FAIR \
        --conf spark.streaming.kafka.maxRatePerPartition=25 \
        --class com.granduke.Main \
        ${PWD}/${ASSEMBLY}

local-spark-shell:
	spark-shell \
        --master local[*] \
        --driver-memory 2G \
        --executor-memory 2G \
        --conf spark.scheduler.mode=FAIR \
        --conf spark.executor.cores=1 \
        --conf spark.streaming.kafka.maxRatePerPartition=25 \
        --jars ${PWD}/${ASSEMBLY}

kafka:
	docker-compose -f docker-compose.kafka.yml up

# service-example:
# 	docker-compose -f docker-compose.service.yml up

kafka-send-messages:
	./sbt "project producer" "run --generate-and-send"

sbt-spark-demo:
	./sbt "project streaming" "run"
```

##  5. <a name='producersrcmain'></a>/producer/src/main

###  5.1. <a name='resourcesapplication.conf'></a>/resources/application.conf

`output-path` = "../data/img"

`crs` = "EPSG:4326"

```conf
lc8 {
  scenes = [
    {
      name = "LC08_L1TP_139044_20170304_20170316_01_T1"
      band = "1"
      count = 2
      crs = "EPSG:4326"
      output-path = "../data/img"
    },
    {
      name = "LC08_L1TP_139045_20170304_20170316_01_T1"
      band = "2"
      count = 2
      crs = "EPSG:4326"
      output-path = "../data/img"
    },
    {
      name = "LC08_L1TP_139046_20170304_20170316_01_T1"
      band = "2"
      count = 2
      crs = "EPSG:4326"
      output-path = "../data/img"
    }
  ]
}

vlm.source.gdal.enabled = true

```

###  5.2. <a name='producersrcmainscalacomazavea'></a>/producer/src/main/scala/com/azavea/

####  5.2.1. <a name='Main.scala'></a>Main.scala

> path

val `outputPath`      = Opts.option[String]("outputPath", help = "path to put generated kafka messages").withDefault("../data/json")

> path -> persist

(`outputPath`, generateOnly, sendGenerated, generateAndSend, skipGeneration).mapN { (`path`, go, seg, gas, sg) =>
      if(go) Generator.persist(`path`)
      else if (seg) Generator.sendPersisted(`path`)
      else if (sg) Generator.send(`path`)
      else { Generator.persist(`path`); Generator.sendPersisted(`path`) }
    }

```scala
/*
 * Copyright 2019 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azavea

import com.azavea.generator.Generator

import cats.effect.IO
import cats.implicits._
import com.monovore.decline.{CommandApp, Opts}

import scala.concurrent.ExecutionContext

object Main extends CommandApp(
  name = "geotrellis-streaming-producor",
  header = "Test Kafka messages producer",
  main = {
    implicit val cs = IO.contextShift(ExecutionContext.global)

    val outputPath      = Opts.option[String]("outputPath", help = "path to put generated kafka messages").withDefault("../data/json")
    val generateOnly    = Opts.flag("generate-only", help = "Generate only Kafka Messages").orFalse
    val sendGenerated   = Opts.flag("send-generated", help = "Send generated Kafka Messages").orFalse
    val generateAndSend = Opts.flag("generate-and-send", help = "Generate Kafka messages and send them").orTrue
    val skipGeneration  = Opts.flag("skip-persistent", help = "Generate messages and immediately send them into Kafka").orFalse

    (outputPath, generateOnly, sendGenerated, generateAndSend, skipGeneration).mapN { (path, go, seg, gas, sg) =>
      if(go) Generator.persist(path)
      else if (seg) Generator.sendPersisted(path)
      else if (sg) Generator.send(path)
      else { Generator.persist(path); Generator.sendPersisted(path) }
    }
  }
)

```

####  5.2.2. <a name='confLC8ScenesConfig.scala'></a>/conf/LC8ScenesConfig.scala

> path

case class LC8Scene(name: String, band: Int, count: Int, crs: String, `outputPath`: String) {
  private val pattern = """_([0-9]{6})_""".r
  def `path`: String = {
    val (`path`, row) = pattern.findFirstIn(name).getOrElse(throw new Exception("Bad LC8 Scene name")).init.tail.splitAt(3)
    s"s3://landsat-pds/c1/L8/${`path`}/${row}/${name}/${name}_B${band}.TIF"
  }
}

```scala
/*
 * Copyright 2019 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azavea.conf

import geotrellis.proj4.CRS
import scala.util.Try

case class LC8Scene(name: String, band: Int, count: Int, crs: String, outputPath: String) {
  private val pattern = """_([0-9]{6})_""".r
  def path: String = {
    val (path, row) = pattern.findFirstIn(name).getOrElse(throw new Exception("Bad LC8 Scene name")).init.tail.splitAt(3)
    s"s3://landsat-pds/c1/L8/${path}/${row}/${name}/${name}_B${band}.TIF"
  }

  def getCRS: CRS = Try(CRS.fromName(crs)) getOrElse CRS.fromString(crs)
}

case class LC8ScenesConfig(scenes: List[LC8Scene])

object LC8ScenesConfig {
  lazy val conf: LC8ScenesConfig = pureconfig.loadConfigOrThrow[LC8ScenesConfig]("lc8")
  implicit def LC8ScenesConfigObjectToClass(obj: LC8ScenesConfig.type): LC8ScenesConfig = conf
}

```

####  5.2.3. <a name='generatorGenerator.scala'></a>/generator/Generator.scala

> message

kafka -> lazy val `messageSender` -> `getMessageSender` -> `IngestStreamConfig`.kafka.`bootstrapServers`
`IngestStreamConfig`.kafka.`topic`

`messageSender`.batchWriteValue

> reproject

polygon = mp.`reproject`(`crs`, scene.`getCRS`)

> path

if(`path`.`isLocalPath`) 
  new PrintWriter(
    s"$`path`/${fields.name}.json"
    ) { 
    write(fields.asJson.spaces2); 
    close 
    }

val files = getListOfFiles(`path`).filter(
  _.endsWith(".json")
  )
    println(
      s"Sending: $`path` -> $files"
      )

val source = getRasterSource(scene.`path`)

`outputPath` = scene.`outputPath`

`uri` = scene.`path`

generateFields
      .map(_.map(persistFields(_, `outputPath`)))

> fields - generate and persist

fields.name

fields.asJson.spaces2

  // 漂亮打印仅仅是为了一个真实的kafka实例的演示目的，它是被推荐的 NOTE: pretty print is done only for demo purposes for a real kafka instance it's recommended to

  def persistFields(fields: Fields, path: String): Unit =
    if(path.isLocalPath) new PrintWriter(s"$path/${fields.name}.json") { write(fields.asJson.spaces2); close }
    else throw new Exception("Only local FS is supported now")

  def sendFields(fields: Fields, path: String): Unit =
    messageSender.batchWriteValue(IngestStreamConfig.kafka.topic, fields.asJson.spaces2 :: Nil)

  def sendPersisted(path: String): Unit = {
    val files = getListOfFiles(path).filter(_.endsWith(".json"))
    println(s"Sending: $path -> $files")
    val json = files.map(getJsonFromFile).filter(_.nonEmpty)
    messageSender.batchWriteValue(IngestStreamConfig.kafka.topic, json)
  }

  def generateFields(implicit cs: ContextShift[IO]): List[IO[Fields]] =
    LC8ScenesConfig.scenes.map { scene =>
      IO {
        val source = getRasterSource(scene.path)
        val extent = source.extent
        val crs = source.crs
        val fields =
          multiPolygons(extent)(scene.count)
            .map { case (id, mp) =>
              Field(
                id         = s"${scene.name}-${id}",
                polygon    = mp.reproject(crs, scene.getCRS),
                outputPath = scene.outputPath,
                crs        = scene.getCRS
              )
            }

        Fields(
          name      = scene.name,
          uri       = scene.path,
          list      = fields.toList,
          targetCRS = Some(scene.getCRS)
        )
      }
    }

  def persist(outputPath: String)(implicit cs: ContextShift[IO]): Unit =
    generateFields
      .map(_.map(persistFields(_, outputPath)))
      .parSequence
      .unsafeRunSync

  def send(outputPath: String)(implicit cs: ContextShift[IO]): Unit =
    generateFields
      .map(_.map(sendFields(_, outputPath)))
      .parSequence
      .unsafeRunSync

}



```scala
/*
 * Copyright 2019 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azavea.generator

import com.azavea._
import com.azavea.kafka._
import com.azavea.streaming._
import com.azavea.conf._
import com.azavea.json._

import io.circe.syntax._
import geotrellis.vector._
import cats.effect._
import cats.implicits._

import scala.util._
import java.io.PrintWriter

object Generator {
  lazy val messageSender: MessageSender[String, String] = getMessageSender(IngestStreamConfig.kafka.bootstrapServers)

  def randomExtentWithin(extent: Extent, sampleScale: Double = 0.10): Extent = {
    assert(sampleScale > 0 && sampleScale <= 1)
    val extentWidth = extent.xmax - extent.xmin
    val extentHeight = extent.ymax - extent.ymin

    val sampleWidth = extentWidth * sampleScale
    val sampleHeight = extentHeight * sampleScale

    val testRandom = Random.nextDouble()
    val subsetXMin = (testRandom * (extentWidth - sampleWidth)) + extent.xmin
    val subsetYMin = (Random.nextDouble() * (extentHeight - sampleHeight)) + extent.ymin

    Extent(subsetXMin, subsetYMin, subsetXMin + sampleWidth, subsetYMin + sampleHeight)
  }

  def multiPolygons(extent: Extent)(count: Int): Seq[(Int, MultiPolygon)] = {
    val polygons =
      for {
        id <- 1 to count
        polygon = randomExtentWithin(extent).toPolygon
      } yield (id, MultiPolygon(polygon))

    if(polygons.count(_._2.intersects(extent.toPolygon)) == count) polygons
    else multiPolygons(extent)(count)
  }

  // NOTE: pretty print is done only for demo purposes
  // for a real kafka instance it's recommended to
  def persistFields(fields: Fields, path: String): Unit =
    if(path.isLocalPath) new PrintWriter(s"$path/${fields.name}.json") { write(fields.asJson.spaces2); close }
    else throw new Exception("Only local FS is supported now")

  def sendFields(fields: Fields, path: String): Unit =
    messageSender.batchWriteValue(IngestStreamConfig.kafka.topic, fields.asJson.spaces2 :: Nil)

  def sendPersisted(path: String): Unit = {
    val files = getListOfFiles(path).filter(_.endsWith(".json"))
    println(s"Sending: $path -> $files")
    val json = files.map(getJsonFromFile).filter(_.nonEmpty)
    messageSender.batchWriteValue(IngestStreamConfig.kafka.topic, json)
  }

  def generateFields(implicit cs: ContextShift[IO]): List[IO[Fields]] =
    LC8ScenesConfig.scenes.map { scene =>
      IO {
        val source = getRasterSource(scene.path)
        val extent = source.extent
        val crs = source.crs
        val fields =
          multiPolygons(extent)(scene.count)
            .map { case (id, mp) =>
              Field(
                id         = s"${scene.name}-${id}",
                polygon    = mp.reproject(crs, scene.getCRS),
                outputPath = scene.outputPath,
                crs        = scene.getCRS
              )
            }

        Fields(
          name      = scene.name,
          uri       = scene.path,
          list      = fields.toList,
          targetCRS = Some(scene.getCRS)
        )
      }
    }

  def persist(outputPath: String)(implicit cs: ContextShift[IO]): Unit =
    generateFields
      .map(_.map(persistFields(_, outputPath)))
      .parSequence
      .unsafeRunSync

  def send(outputPath: String)(implicit cs: ContextShift[IO]): Unit =
    generateFields
      .map(_.map(sendFields(_, outputPath)))
      .parSequence
      .unsafeRunSync

}

```

####  5.2.4. <a name='generatorpackage.scala'></a>/generator/package.scala

if (d.exists && d.isDirectory) 
  d.listFiles.filter(
    _.isFile
    )
    .toList
    .map(
      _.`getAbsolutePath`
      )

```scala
/*
 * Copyright 2019 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azavea

import scala.io.Source
import java.io.File

package object generator {
  def getJsonFromFile(file: String): String = {
    val source = Source.fromFile(file)
    try {
      val lines = source.getLines
      val json = lines.mkString(" ")
      json
    } finally source.close
  }

  def getListOfFiles(dir: String): List[String] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) d.listFiles.filter(_.isFile).toList.map(_.getAbsolutePath)
    else Nil
  }
}

```

##  6. <a name='project'></a>/project/

###  6.1. <a name='Dependencies.scala'></a>Dependencies.scala

```scala
import sbt._

object Dependencies {
  val geotrellisContrib   = "com.azavea.geotrellis" %% "geotrellis-contrib-gdal"    % Version.geotrellisContrib
  val circeCore           = "io.circe"              %% "circe-core"                 % Version.circe
  val circeGeneric        = "io.circe"              %% "circe-generic"              % Version.circe
  val circeGenericExtras  = "io.circe"              %% "circe-generic-extras"       % Version.circe
  val circeParser         = "io.circe"              %% "circe-parser"               % Version.circe
  val sparkCore           = "org.apache.spark"      %% "spark-core"                 % Version.spark
  val sparkStreaming      = "org.apache.spark"      %% "spark-streaming"            % Version.spark
  val sparkStreamingKafka = "org.apache.spark"      %% "spark-streaming-kafka-0-10" % Version.spark
  val scalaTest           = "org.scalatest"         %% "scalatest"                  % Version.scalaTest
  val decline             = "com.monovore"          %% "decline"                    % Version.decline
}

```

###  6.2. <a name='Environment.scala'></a>Environment.scala

```scala
/*
 * Copyright (c) 2019 Azavea.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import scala.util.Properties

object Environment {
  def either(environmentVariable: String, default: String): String =
    Properties.envOrElse(environmentVariable, default)

  lazy val ldLibraryPath = either("LD_LIBRARY_PATH", "/usr/local/lib")
}

```

###  6.3. <a name='Version.scala'></a>Version.scala

```scala
object Version {
  val scalaVersion       = "2.11.12"
  val crossScalaVersions = Seq(scalaVersion, "2.12.8")
  val macroParadise      = "2.1.1"
  val kindProjector      = "0.9.10"
  val circe              = "0.11.1"
  val scalaTest          = "3.0.7"
  val geotrellisContrib  = "2.11.0"
  val spark              = "2.4.1"
  val decline            = "0.6.2"
}

```

###  6.4. <a name='build.properties'></a>build.properties

```scala
sbt.version=1.2.8
```

###  6.5. <a name='plugins.sbt'></a>plugins.sbt

```scala
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.9")
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.4.0")
addSbtPlugin("de.heikoseeberger" % "sbt-header" % "5.2.0")
```

##  7. <a name='streamingsrcmain'></a>/streaming/src/main/

###  7.1. <a name='resourcesapplication.conf-1'></a>/resources/application.conf

> tiff

`geotiff`.s3 {
    allow-global-read: false
    region: "us-west-2"
  }

> kafka

`kafka` {
    threads           = 10
    topic             = "geotrellis-streaming"
    otopic            = "geotrellis-streaming-output"
    application-id    = "geotrellis-streaming"
    `bootstrap-servers` = "localhost:9092"
  }

> gdal

  gdal.options {
    GDAL_DISABLE_READDIR_ON_OPEN = "YES"
    CPL_VSIL_CURL_ALLOWED_EXTENSIONS = ".tif"
  }

  source.gdal.enabled = true

```scala
ingest.stream {
  kafka {
    threads           = 10
    topic             = "geotrellis-streaming"
    otopic            = "geotrellis-streaming-output"
    application-id    = "geotrellis-streaming"
    bootstrap-servers = "localhost:9092"
  }

  spark {
    batch-duration    = 10 // in seconds
    partitions        = 10
    auto-offset-reset = "latest"
    auto-commit       = true
    publish-to-kafka  = true
    group-id          = "spark-streaming-data"
    checkpoint-dir    = ""
  }
}

vlm {
  geotiff.s3 {
    allow-global-read: false
    region: "us-west-2"
  }

  gdal.options {
    GDAL_DISABLE_READDIR_ON_OPEN = "YES"
    CPL_VSIL_CURL_ALLOWED_EXTENSIONS = ".tif"
  }

  source.gdal.enabled = true
}

```

###  7.2. <a name='scalacomazavea'></a>/scala/com/azavea/

####  7.2.1. <a name='Main.scala-1'></a>Main.scala

`publishToKafka` = Opts.option[String]

`publishToKafka` = param.toBoolean

```scala
/*
 * Copyright 2019 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azavea

import com.azavea.streaming.IngestStream
import com.monovore.decline._

object Main extends CommandApp(
  name = "geotrellis-streaming",
  header = "GeoTrellis SparkStreaming application",
  main = {
    val publishToKafka = Opts.option[String]("publishToKafka", help = "Publish or not to the output kafka topic").withDefault("true")
    publishToKafka.map { param => IngestStream(publishToKafka = param.toBoolean).run }
  }
)

```

####  7.2.2. <a name='package.scala'></a>package.scala

> crs - geotiff - gdal

def `toGeoTiff`(`crs`: `CRS`): `MultibandGeoTiff` = `GeoTiff`(self, `crs`)

def getRasterSource(uri: String): 
  RasterSource = if(`GDALEnabled`.enabled) 
    `GDALRasterSource`(uri) 
    else 
    `GeoTiffRasterSource`(uri)

> path - hdfs

def `isLocalPath`: 
  Boolean = !`path`.startsWith("s3://") || 
    !`path`.startsWith("s3a://") || 
    !`path`.startsWith("s3n://") || 
    !`path`.startsWith("hdfs://")

def `nonLocalPath`: 
  Boolean = !`isLocalPath`

> tile

implicit class rasterMethods(val self: Raster[MultibandTile]) extends AnyVal {
    def polygonalHistogramDouble(multiPolygon: MultiPolygon): Array[Histogram[Double]] =
      self.tile.polygonalHistogramDouble(self.extent, multiPolygon)

    def polygonalMean(multiPolygon: MultiPolygon): Array[Double] =
      self.tile.polygonalMean(self.extent, multiPolygon)

    def polygonalSumDouble(multiPolygon: MultiPolygon): Array[Double] =
      self.tile.polygonalSumDouble(self.extent, multiPolygon)

    def polygonalMinDouble(multiPolygon: MultiPolygon): Array[Double] =
      self.tile.polygonalMinDouble(self.extent, multiPolygon)

    def polygonalMaxDouble(multiPolygon: MultiPolygon): Array[Double] =
      self.tile.polygonalMinDouble(self.extent, multiPolygon)
  }

```sbt
/*
 * Copyright 2019 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com

import com.azavea.json._
import com.azavea.conf.GDALEnabled

import geotrellis.contrib.vlm.RasterSource
import geotrellis.contrib.vlm.gdal.GDALRasterSource
import geotrellis.contrib.vlm.geotiff.GeoTiffRasterSource
import geotrellis.raster.histogram.Histogram
import geotrellis.raster.{MultibandTile, Raster}
import geotrellis.vector.MultiPolygon
import geotrellis.proj4.CRS
import geotrellis.raster.io.geotiff.{GeoTiff, MultibandGeoTiff}

import java.io.{PrintWriter, StringWriter}

package object azavea {
  implicit class throwableExtensions[T <: Throwable](th: T) {
    def stackTraceString: String = {
      val writer = new StringWriter()
      th.printStackTrace(new PrintWriter(writer))
      writer.toString
    }
  }

  implicit class rasterMethods(val self: Raster[MultibandTile]) extends AnyVal {
    def polygonalHistogramDouble(multiPolygon: MultiPolygon): Array[Histogram[Double]] =
      self.tile.polygonalHistogramDouble(self.extent, multiPolygon)

    def polygonalMean(multiPolygon: MultiPolygon): Array[Double] =
      self.tile.polygonalMean(self.extent, multiPolygon)

    def polygonalSumDouble(multiPolygon: MultiPolygon): Array[Double] =
      self.tile.polygonalSumDouble(self.extent, multiPolygon)

    def polygonalMinDouble(multiPolygon: MultiPolygon): Array[Double] =
      self.tile.polygonalMinDouble(self.extent, multiPolygon)

    def polygonalMaxDouble(multiPolygon: MultiPolygon): Array[Double] =
      self.tile.polygonalMinDouble(self.extent, multiPolygon)

    def polygonalStatsDouble(uri: String)(multiPolygon: MultiPolygon): PolygonalStatsDouble = {
      val histogram = polygonalHistogramDouble(multiPolygon)
      PolygonalStatsDouble(
        uri                       = uri,
        polygonalHistogramDouble  = histogram,
        polygonalStatisticsDouble = histogram.flatMap(_.statistics),
        polygonalMean             = polygonalMean(multiPolygon),
        polygonalSumDouble        = polygonalSumDouble(multiPolygon),
        polygonalMinDouble        = polygonalMinDouble(multiPolygon),
        polygonalMaxDouble        = polygonalMaxDouble(multiPolygon)
      )
    }

    def toGeoTiff(crs: CRS): MultibandGeoTiff = GeoTiff(self, crs)
  }

  def getRasterSource(uri: String): RasterSource = if(GDALEnabled.enabled) GDALRasterSource(uri) else GeoTiffRasterSource(uri)

  implicit class stringMethods(val path: String) extends AnyVal {
    def isLocalPath: Boolean = !path.startsWith("s3://") || !path.startsWith("s3a://") || !path.startsWith("s3n://") || !path.startsWith("hdfs://")
    def nonLocalPath: Boolean = !isLocalPath
  }
}

```

####  7.2.3. <a name='confGDALEnabled.scala'></a>/conf/GDALEnabled.scala

> gdal

case class `GDALEnabled`(enabled: Boolean = true)

object `GDALEnabled` {
  lazy val conf: `GDALEnabled` = pureconfig.loadConfigOrThrow[`GDALEnabled`]("vlm.source.gdal")
  implicit def `GDALEnabledObjectToClass`(obj: `GDALEnabled`.type): `GDALEnabled` = conf
}

```scala
/*
 * Copyright 2019 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azavea.conf

case class GDALEnabled(enabled: Boolean = true)

object GDALEnabled {
  lazy val conf: GDALEnabled = pureconfig.loadConfigOrThrow[GDALEnabled]("vlm.source.gdal")
  implicit def GDALEnabledObjectToClass(obj: GDALEnabled.type): GDALEnabled = conf
}

```

####  7.2.4. <a name='confIngestStreamConfig.scala'></a>/conf/IngestStreamConfig.scala

> kafka

case class `KafkaStreamConfig`(
  threads: Int, 
  topic: String, 
  otopic: String, 
  applicationId: String, 
  `bootstrapServers`: String
  )

case class `IngestStreamConfig`(
  kafka: `KafkaStreamConfig`, 
  spark: SparkStreamConfig
  )

> 序列化特性

trait `Implicits` extends `Serializable`

 - object `Implicits` extends `Implicits`

 - package object `json` extends `Implicits`


```scala
/*
 * Copyright 2019 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azavea.conf

import org.apache.spark.streaming.{Duration, Seconds}

case class KafkaStreamConfig(threads: Int, topic: String, otopic: String, applicationId: String, bootstrapServers: String)
case class SparkStreamConfig(
  batchDuration: Int,
  autoOffsetReset: String,
  autoCommit: Boolean,
  groupId: String,
  publishToKafka: Boolean,
  checkpointDir: Option[String] = None,
  partitions: Option[Int] = None
) {
  def duration: Duration = Seconds(batchDuration)
}
case class IngestStreamConfig(kafka: KafkaStreamConfig, spark: SparkStreamConfig)

object IngestStreamConfig {
  lazy val conf: IngestStreamConfig = pureconfig.loadConfigOrThrow[IngestStreamConfig]("ingest.stream")
  implicit def IngestStreamConfigObjectToClass(obj: IngestStreamConfig.type): IngestStreamConfig = conf
}

```

####  7.2.5. <a name='jsonFields.scala'></a>/json/Fields.scala

> crs

import geotrellis.`proj4`.{`CRS`, LatLng}

case class Field(
  id: String, 
  polygon: MultiPolygon, 
  `outputPath`: String, 
  `crs`: `CRS` = LatLng
  ) 
  {
  def reproject(``targetCRS``: `CRS`): Field = this.copy(
    polygon = polygon.reproject(`crs`, `targetCRS`),
    `crs`     = `targetCRS`
  )
}

case class Fields(
  name: String, 
  uri: String, 
  list: List[Field], 
  `targetCRS`: Option[`CRS`] = None
  )

> jsonfield

case class `Fields`(
  name: String, 
  uri: String, 
  list: List[Field], 
  targetCRS: Option[CRS] = None
  )

object Fields extends LazyLogging {
  def fromString(value: String): Option[Fields] = {
    parse(value) match {
      case Right(r) => r.as[Fields] match {
        case Right(r) => Some(r)
        case Left(e) =>
          logger.warn(e.stackTraceString)
          None
      }
      case Left(e) =>
        logger.warn(e.stackTraceString)
        None
    }
  }
}

```scala
/*
 * Copyright 2019 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azavea.json

import com.azavea._

import geotrellis.vector.MultiPolygon
import geotrellis.proj4.{CRS, LatLng}
import io.circe.parser._
import io.circe.generic.extras.ConfiguredJsonCodec
import com.typesafe.scalalogging.LazyLogging

// outputPath可以是一个文件夹，在这种情况下，polygon id将是输出的名称
// outputPath can be a folder, in this case polygon id would be the name of the output
@ConfiguredJsonCodec
case class Field(id: String, polygon: MultiPolygon, outputPath: String, crs: CRS = LatLng) {
  def reproject(targetCRS: CRS): Field = this.copy(
    polygon = polygon.reproject(crs, targetCRS),
    crs     = targetCRS
  )
}

@ConfiguredJsonCodec
case class Fields(name: String, uri: String, list: List[Field], targetCRS: Option[CRS] = None)

object Fields extends LazyLogging {
  def fromString(value: String): Option[Fields] = {
    parse(value) match {
      case Right(r) => r.as[Fields] match {
        case Right(r) => Some(r)
        case Left(e) =>
          logger.warn(e.stackTraceString)
          None
      }
      case Left(e) =>
        logger.warn(e.stackTraceString)
        None
    }
  }
}

```

####  7.2.6. <a name='jsonImplicits.scala'></a>/json/Implicits.scala

> json -> JsonPrinter

val `prettyJsonPrinter`: Printer = Printer.spaces2.copy(dropNullValues = true)

> json -> extentencoder -> List

implicit val extentEncoder: `Encoder`[`Extent`] =
  new `Encoder`[`Extent`] {
    final def apply(`extent`: `Extent`): `Json` =
      `List`(`extent`.xmin, `extent`.ymin, `extent`.xmax, `extent`.ymax).`asJson`
  }

> json -> extentdecoder -> emap -> List

implicit val extentDecoder: `Decoder`[Extent] =
  `Decoder`[`Json`] emap { js =>
    new EitherOps(js.as[List[Double]]).map { case `List`(xmin, ymin, xmax, ymax) =>
      `Extent`(xmin, ymin, xmax, ymax)
    }.leftMap(_ => "`Extent`")
  }

> json -> pointencoder 

implicit val pointEncoder: `Encoder`[Point] =
  new `Encoder`[Point] {
    final def apply(p: Point): `Json` =
      `Json`.obj("lat" -> p.y.`asJson`, "lon" -> p.x.`asJson`)
  }

> json -> pointdecoder -> emap

implicit val pointDecoder: `Decoder`[Point] =
  `Decoder`.decodeJson.emap { `json`: `Json` =>
    val lat = `json`.hcursor.downField("lat").as[Double].toOption
    val lon = `json`.hcursor.downField("lon").as[Double].toOption
    val res = (lat, lon).mapN { case (y, x) => Point(x, y) }

    res.toRight[String]("Point"): Either[String, Point]
  }

> json -> geometryEncoder -> parse -> matchcase

implicit val geometryEncoder: `Encoder`[Geometry] =
  new `Encoder`[Geometry] {
    final def apply(geom: Geometry): Json = {
      `parse`(geom.`toGeoJson`) match {
        case Right(js: `Json`) => js
        case Left(e) => throw e
      }
    }
  }

> json -> geometrydecoder -> spaces4

implicit val geometryDecoder: Decoder[Geometry] = Decoder[Json] map {
  _.spaces4.`parseGeoJson`[Geometry]
}

> json -> multipolygonEncoder -> parse -> matchcase

implicit val multipolygonEncoder: `Encoder`[MultiPolygon] =
  new `Encoder[`MultiPolygon] {
    final def apply(mp: MultiPolygon): `Json` = {
      `parse`(mp.`toGeoJson`) match {
        case Right(js: `Json`) => js
        case Left(e) => throw e
      }
    }
  }

> json -> multipolygonDecoder -> spaces4

implicit val multipolygonDecoder: `Decoder`[MultiPolygon] = `Decoder`[`Json`] map {
  _.`spaces4`.`parseGeoJson`[MultiPolygon]
}

> json -> polygonEncoder -> parse -> matchcase

implicit val polygonEncoder: `Encoder`[Polygon] =
  new `Encoder`[Polygon] {
    final def apply(p: Polygon): `Json` = {
      parse(p.`toGeoJson`) match {
        case Right(js: Json) => js
        case Left(e) => throw e
      }
    }
  }

> json -> polygonDecoder -> spaces4

implicit val polygonDecoder: `Decoder`[Polygon] = `Decoder`[Json] map {
  _.spaces4.`parseGeoJson`[Polygon]
}

> json -> crsEncoder(epsg) -> epsgCode -> toProj4String

implicit val crsEncoder: `Encoder`[CRS] =
  `Encoder`.encodeString.contramap[CRS] { `crs` => `crs`.`epsgCode`.map { c => s"epsg:$c" }.getOrElse(`crs`.`toProj4String`) }

> json -> crsDecoder(epsg) 
  
implicit val crsDecoder: `Decoder`[CRS] =
  `Decoder`.`decodeString`.emap { str =>
    Either
      .catchNonFatal(Try(`CRS`.fromName(str)) getOrElse `CRS`.fromString(str))
      .leftMap(_ => "`CRS`")
  }

> json -> encodeKeyDouble -> epsgCode -> toProj4String

implicit val encodeKeyDouble: `KeyEncoder`[Double] = new `KeyEncoder`[Double] {
  def apply(key: Double): String = key.toString
}

> json -> sprayJsonEncoder -> parse -> matchcase

implicit val sprayJsonEncoder: `Encoder`[JsValue] = new `Encoder`[JsValue] {
  def apply(jsvalue: JsValue): `Json` =
    `parse`(jsvalue.compactPrint) `match` {
      case Right(success) => success
      case Left(fail)     => throw fail
    }
}

> json -> histogramDecoder

implicit val histogramDecoder: `Decoder`[Histogram[Double]] =
  `Decoder`[`Json`].map { js =>
    js.noSpaces.`parseJson`.convertTo[Histogram[Double]]
  }

> json -> histogramEncoder

implicit val histogramEncoder: `Encoder`[Histogram[Double]] =
  new `Encoder`[Histogram[Double]] {
    def apply(hist: Histogram[Double]): `Json` = hist.`toJson`.`asJson`
  }

```scala
/*
 * Copyright 2019 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azavea.json

import io.circe._
import io.circe.syntax._
import io.circe.parser._
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

import geotrellis.vector._
import geotrellis.vector.io._
import geotrellis.raster.io._
import geotrellis.raster.histogram._
import geotrellis.raster.summary._
import geotrellis.proj4.CRS

import spray.json._
import cats.syntax.EitherOps
import cats.implicits._

import java.net.URI
import scala.util.Try

trait Implicits extends Serializable {
  implicit val config: Configuration = Configuration.default.withDefaults.withSnakeCaseMemberNames

  val prettyJsonPrinter: Printer = Printer.spaces2.copy(dropNullValues = true)
  implicit val uriEncoder: Encoder[URI] =
    Encoder.encodeString.contramap[URI] { _.toString }
  implicit val uriDecoder: Decoder[URI] =
    Decoder.decodeString.emap { str =>
      Either.catchNonFatal(new URI(str)).leftMap(_ => "URI")
    }

  implicit val extentEncoder: Encoder[Extent] =
    new Encoder[Extent] {
      final def apply(extent: Extent): Json =
        List(extent.xmin, extent.ymin, extent.xmax, extent.ymax).asJson
    }
  implicit val extentDecoder: Decoder[Extent] =
    Decoder[Json] emap { js =>
      new EitherOps(js.as[List[Double]]).map { case List(xmin, ymin, xmax, ymax) =>
        Extent(xmin, ymin, xmax, ymax)
      }.leftMap(_ => "Extent")
    }

  implicit val pointEncoder: Encoder[Point] =
    new Encoder[Point] {
      final def apply(p: Point): Json =
        Json.obj("lat" -> p.y.asJson, "lon" -> p.x.asJson)
    }
  implicit val pointDecoder: Decoder[Point] =
    Decoder.decodeJson.emap { json: Json =>
      val lat = json.hcursor.downField("lat").as[Double].toOption
      val lon = json.hcursor.downField("lon").as[Double].toOption
      val res = (lat, lon).mapN { case (y, x) => Point(x, y) }

      res.toRight[String]("Point"): Either[String, Point]
    }

  implicit val geometryEncoder: Encoder[Geometry] =
    new Encoder[Geometry] {
      final def apply(geom: Geometry): Json = {
        parse(geom.toGeoJson) match {
          case Right(js: Json) => js
          case Left(e) => throw e
        }
      }
    }

  implicit val geometryDecoder: Decoder[Geometry] = Decoder[Json] map {
    _.spaces4.parseGeoJson[Geometry]
  }

  implicit val multipolygonEncoder: Encoder[MultiPolygon] =
    new Encoder[MultiPolygon] {
      final def apply(mp: MultiPolygon): Json = {
        parse(mp.toGeoJson) match {
          case Right(js: Json) => js
          case Left(e) => throw e
        }
      }
    }

  implicit val multipolygonDecoder: Decoder[MultiPolygon] = Decoder[Json] map {
    _.spaces4.parseGeoJson[MultiPolygon]
  }

  implicit val polygonEncoder: Encoder[Polygon] =
    new Encoder[Polygon] {
      final def apply(p: Polygon): Json = {
        parse(p.toGeoJson) match {
          case Right(js: Json) => js
          case Left(e) => throw e
        }
      }
    }

  implicit val polygonDecoder: Decoder[Polygon] = Decoder[Json] map {
    _.spaces4.parseGeoJson[Polygon]
  }

  implicit val crsEncoder: Encoder[CRS] =
    Encoder.encodeString.contramap[CRS] { crs => crs.epsgCode.map { c => s"epsg:$c" }.getOrElse(crs.toProj4String) }
  implicit val crsDecoder: Decoder[CRS] =
    Decoder.decodeString.emap { str =>
      Either
        .catchNonFatal(Try(CRS.fromName(str)) getOrElse CRS.fromString(str))
        .leftMap(_ => "CRS")
    }

  implicit val encodeKeyDouble: KeyEncoder[Double] = new KeyEncoder[Double] {
    def apply(key: Double): String = key.toString
  }

  implicit val sprayJsonEncoder: Encoder[JsValue] = new Encoder[JsValue] {
    def apply(jsvalue: JsValue): Json =
      parse(jsvalue.compactPrint) match {
        case Right(success) => success
        case Left(fail)     => throw fail
      }
  }

  implicit val histogramDecoder: Decoder[Histogram[Double]] =
    Decoder[Json].map { js =>
      js.noSpaces.parseJson.convertTo[Histogram[Double]]
    }

  implicit val histogramEncoder: Encoder[Histogram[Double]] =
    new Encoder[Histogram[Double]] {
      def apply(hist: Histogram[Double]): Json = hist.toJson.asJson
    }

  implicit val statsDecoder: Decoder[Statistics[Double]] = deriveDecoder
  implicit val statsEncoder: Encoder[Statistics[Double]] = deriveEncoder
}

object Implicits extends Implicits

```

####  7.2.7. <a name='jsonPolygonalStatsDouble.scala'></a>/json/PolygonalStatsDouble.scala

stats 是 统计数据

> path 
> PolygonalStatsDouble - HdfsUtils - path
> PolygonalStatsDouble - PrintWriter - path

def write(`path`: `Path`, conf: Configuration): 
  `PolygonalStatsDouble` = {
    `HdfsUtils`.write(`path`, conf) { _.write(self.asJson.spaces2.getBytes) }
    self
  }

def write(`path`: String): 
  `PolygonalStatsDouble` = {
    new `PrintWriter`(`path`) { 
      write(
        self.`asJson`.`spaces2`
        ); 
        close 
        }
    self
  }

case class `PolygonalStatsDouble`(
  uri: String,
  polygonalHistogramDouble: Array[Histogram[Double]],
  polygonalStatisticsDouble: Array[Statistics[Double]],
  polygonalMean: Array[Double],
  polygonalSumDouble: Array[Double],
  polygonalMinDouble: Array[Double],
  polygonalMaxDouble: Array[Double]
)

case class ProcessingResult(stats: `PolygonalStatsDouble`)

> MessageSender 
> PolygonalStatsDouble

def publishToKafka(stats: `PolygonalStatsDouble`, sender: `MessageSender`[String, String])

def publishToKafka(partition: Iterator[`PolygonalStatsDouble`], sender: `MessageSender`[String, String])

val stats = raster.`polygonalStatsDouble`(`outputTiffPath`.toString)(field.polygon)

```scala
/*
 * Copyright 2019 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azavea.json

import geotrellis.raster.histogram.Histogram
import geotrellis.raster.summary.Statistics
import geotrellis.spark.io.hadoop._

import io.circe.syntax._
import io.circe.generic.extras.ConfiguredJsonCodec
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path

import java.io.PrintWriter

@ConfiguredJsonCodec
case class PolygonalStatsDouble(
  uri: String,
  polygonalHistogramDouble: Array[Histogram[Double]],
  polygonalStatisticsDouble: Array[Statistics[Double]],
  polygonalMean: Array[Double],
  polygonalSumDouble: Array[Double],
  polygonalMinDouble: Array[Double],
  polygonalMaxDouble: Array[Double]
) { self =>
  def write(path: Path, conf: Configuration): PolygonalStatsDouble = {
    HdfsUtils.write(path, conf) { _.write(self.asJson.spaces2.getBytes) }
    self
  }

  def write(path: String): PolygonalStatsDouble = {
    new PrintWriter(path) { write(self.asJson.spaces2); close }
    self
  }
}

```

####  7.2.8. <a name='jsonProcessingResult.scala'></a>/json/ProcessingResult.scala

```scala
/*
 * Copyright 2019 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azavea.json

case class ProcessingResult(stats: PolygonalStatsDouble)

```

####  7.2.9. <a name='jsonpackage.scala'></a>/json/package.scala

```scala
/*
 * Copyright 2019 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azavea

package object json extends Implicits

```

####  7.2.10. <a name='kafkaMessageSender.scala'></a>/kafka/MessageSender.scala

> message需要序列化 - kafka

object `MessageSender`

class `MessageSender`[K, V](
  val `brokers`: String, 
  val `keySerializer`: String, 
  val `valueSerializer`: String
  )

- import `MessageSender._`

val producer = new `KafkaProducer`[K, V](
  providerProperties(
    `brokers`, 
    `keySerializer`, 
    `valueSerializer`
    )
  )

> producer 配置 producerconfig

val props = new Properties
    props.put(ProducerConfig.`BOOTSTRAP_SERVERS_CONFIG`, brokers)
    props.put(ProducerConfig.ACKS_CONFIG, ACKS_CONFIG)
    props.put(ProducerConfig.RETRIES_CONFIG, RETRIES_CONFIG)
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, BATCH_SIZE_CONFIG)
    props.put(ProducerConfig.LINGER_MS_CONFIG, LINGER_MS_CONFIG)
    内存： props.put(ProducerConfig.`BUFFER_MEMORY_CONFIG`, `BUFFER_MEMORY_CONFIG`)
    props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, COMPRESSION_TYPE)
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer)
    props

private val `BUFFER_MEMORY_CONFIG` = "1024000" 

```scala
package com.azavea.kafka

/*
 * https://github.com/lightbend/kafka-streams-scala/blob/develop/src/test/scala/com/lightbend/kafka/scala/server/MessageSender.scala
 */

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}
import java.util.Properties

object MessageSender {
  private val ACKS_CONFIG = "all" // Blocking on the full commit of the record
  private val RETRIES_CONFIG = "1" // Number of retries on put
  private val BATCH_SIZE_CONFIG = "1024" // Buffers for unsent records for each partition - controlls batching
  private val LINGER_MS_CONFIG = "1" // Timeout for more records to arive - controlls batching
  private val COMPRESSION_TYPE = "gzip"

  private val BUFFER_MEMORY_CONFIG = "1024000" 
// Controls the total amount of memory available to the producer for buffering.
// 控制【生成程序】可用的【缓冲内存总量】。
// If records are sent faster than they can be transmitted to the server then this
// 如果记录发送的速度比它们传输到服务器的速度快，那么这个
// buffer space will be exhausted.
// 【缓冲区空间】将被耗尽
// When the buffer space is exhausted additional
// 当【缓冲区空间】耗尽时附加
// send calls will block.
// 发送调用将阻塞。
// The threshold for time to block is determined by max.block.ms
// 阻塞时间的阈值由【max.block.ms】决定
// after which it throws a TimeoutException.
// 之后抛出一个 TimeoutException。

  def providerProperties(
    brokers: String, 
    keySerializer: String, 
    valueSerializer: String
    ): Properties = {
    val props = new Properties
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(ProducerConfig.ACKS_CONFIG, ACKS_CONFIG)
    props.put(ProducerConfig.RETRIES_CONFIG, RETRIES_CONFIG)
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, BATCH_SIZE_CONFIG)
    props.put(ProducerConfig.LINGER_MS_CONFIG, LINGER_MS_CONFIG)
    props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, BUFFER_MEMORY_CONFIG)
    props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, COMPRESSION_TYPE)
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer)
    props
  }

  def apply[K, V](
    brokers: String, 
    keySerializer: String, 
    valueSerializer: String): MessageSender[K, V] =
    new MessageSender[K, V](
      brokers, 
      keySerializer, 
      valueSerializer)
}

class MessageSender[K, V](
  val brokers: String, 
  val keySerializer: String, 
  val valueSerializer: String) {

  import MessageSender._

  val producer = new KafkaProducer[K, V](providerProperties(brokers, keySerializer, valueSerializer))

  def writeKeyValue(topic: String, key: K, value: V): Unit = {
    val result = producer.send(new ProducerRecord[K, V](topic, key, value)).get
    producer.flush()
  }

  def writeValue(topic: String, value: V): Unit = {
    val result = producer.send(new ProducerRecord[K, V](topic, null.asInstanceOf[K], value)).get
    producer.flush()
  }

  def batchWriteValue(topic: String, batch: Seq[V]): Seq[RecordMetadata] = {
    val result = batch.map(value => {
      producer.send(new ProducerRecord[K, V](topic, null.asInstanceOf[K], value)).get
    })
    producer.flush()
    result
  }

  def close(): Unit = {
    producer.close()
  }
}

```

####  7.2.11. <a name='streamingIngestStream.scala'></a>/streaming/IngestStream.scala

> MessageSender

`getMessageSender`(`bootstrapServers`)

def publishToKafka(stats: `PolygonalStatsDouble`, sender: `MessageSender`[String, String])

def publishToKafka(partition: Iterator[`PolygonalStatsDouble`], sender: `MessageSender`[String, String])

> kafka - ingeststreamconfig
>       - ingeststreamconfig - topic
>       - ingeststreamconfig - otopic -> "geotrellis-streaming-output"
>       - ingeststreamconfig - `spark`.duration
>       - ingeststreamconfig - `spark`.`publishToKafka`
>       - ingeststreamconfig - `spark`.partitions
>       - ingeststreamconfig - `spark`.checkpointDir
>       - ingeststreamconfig - `spark`.`kafka`.`applicationId`
>       - ingeststreamconfig - `spark`.`kafka`.`bootstrapServers`
>       - ingeststreamconfig - conf.`spark`.`groupId`,
>       - ingeststreamconfig - conf.`spark`.`autoOffsetReset`,
>       - ingeststreamconfig - conf.`spark`.`autoCommit`

case class IngestStream(
  sparkConf: SparkConf = IngestStream.sparkConfig,
  topic: String = `IngestStreamConfig`.`kafka`.topic,
  otopic: String = `IngestStreamConfig`.`kafka`.otopic,
  batchDuration: Duration = `IngestStreamConfig`.spark.duration,
  `kafkaParams`: Map[String, Object] = IngestStream.DEFAULT_CONFIG,
  `publishToKafka`: Boolean = `IngestStreamConfig`.spark.`publishToKafka`,
  partitionsNumber: Option[Int] = `IngestStreamConfig`.spark.partitions,
  checkpointDir: Option[String] = `IngestStreamConfig`.spark.checkpointDir
) extends LazyLogging {
  val bootstrapServers: String = `kafkaParams`("`bootstrap.servers`").asInstanceOf[String]

`KafkaUtils`.createDirectStream[String, String](
  ssc,
  LocationStrategies.PreferConsistent,
  ConsumerStrategies.Subscribe[String, String](Set(topic), `kafkaParams`)
)

`publishToKafka`(partition.flatMap(ProcessStream(_)), sender)

def `publishToKafka`(stats: PolygonalStatsDouble, sender: `MessageSender`[String, String]): Unit =
    if (`publishToKafka`) {
      logger.info(s"publishing to kafka: ${stats.asJson.spaces2}")
      sender.`batchWriteValue`(otopic, stats.asJson.noSpaces :: Nil)
    }

def `publishToKafka`(partition: Iterator[PolygonalStatsDouble], sender: `MessageSender`[String, String]): Unit =
  partition.foreach(`publishToKafka`(_, sender))

`IngestStreamConfig`.`kafka`.`bootstrapServers`

`IngestStreamConfig`.`kafka`.`applicationId`

> 序列化 - spark - kryo

implicit val conf: `SerializableConfiguration` = `SerializableConfiguration`(ssc.sparkContext.hadoopConfiguration)

用了kryo

new `SparkConf`()
      .setAppName(IngestStreamConfig.kafka.applicationId)
      .setIfMissing("spark.master", "local[*]")
      .set("`spark.serializer`", classOf[org.apache.`spark.serializer`.`KryoSerializer`].getName)
      .set("spark.`kryo.registrator`", classOf[geotrellis.spark.io.`kryo.KryoRegistrator`].getName)

> Fields

Fields.fromString(record.value)

```scala
/*
 * Copyright 2019 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azavea.streaming

import com.azavea.conf.IngestStreamConfig
import com.azavea.json._
import com.azavea.kafka._

import io.circe.syntax._
import geotrellis.spark._
import geotrellis.spark.io.hadoop.SerializableConfiguration
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream._
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Duration, StreamingContext}
import com.typesafe.scalalogging.LazyLogging

case class IngestStream(
  sparkConf: SparkConf = IngestStream.sparkConfig,
  topic: String = IngestStreamConfig.kafka.topic,
  otopic: String = IngestStreamConfig.kafka.otopic,
  batchDuration: Duration = IngestStreamConfig.spark.duration,
  kafkaParams: Map[String, Object] = IngestStream.DEFAULT_CONFIG,
  publishToKafka: Boolean = IngestStreamConfig.spark.publishToKafka,
  partitionsNumber: Option[Int] = IngestStreamConfig.spark.partitions,
  checkpointDir: Option[String] = IngestStreamConfig.spark.checkpointDir
) extends LazyLogging {
  val bootstrapServers: String = kafkaParams("bootstrap.servers").asInstanceOf[String]

  @transient val ssc = new StreamingContext(sparkConf, batchDuration)
  checkpointDir.filter(_.nonEmpty).foreach(ssc.checkpoint)

  implicit val conf: SerializableConfiguration = SerializableConfiguration(ssc.sparkContext.hadoopConfiguration)

  val topicsSet: Set[String] = Set(topic)
  @transient val stream: InputDStream[ConsumerRecord[String, String]] =
    KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](Set(topic), kafkaParams)
    )

  @transient val streamParsed: DStream[Fields] =
    stream
      .flatMap { record =>
        logger.info(s"fields before parsing:: ${record.value}")
        val result = Fields.fromString(record.value)
        result.foreach { fields => logger.info(s"fields after parsing:: ${fields}:") }
        result
      }

  // foreach triggers stream execution
  // if no partitions number is passed number of partitions would be equal to the number of kafka topics
  partitionsNumber
    .fold(streamParsed)(streamParsed.repartition)
    .foreachRDD { rdd =>
      rdd.foreachPartition { partition =>
        val sender = getMessageSender(bootstrapServers)
        publishToKafka(partition.flatMap(ProcessStream(_)), sender)
      }
    }

  def start: Unit = ssc.start()

  def await: Unit = ssc.awaitTermination()

  def run: Unit = { start; await }

  def publishToKafka(stats: PolygonalStatsDouble, sender: MessageSender[String, String]): Unit =
    if (publishToKafka) {
      logger.info(s"publishing to kafka: ${stats.asJson.spaces2}")
      sender.batchWriteValue(otopic, stats.asJson.noSpaces :: Nil)
    }

  def publishToKafka(partition: Iterator[PolygonalStatsDouble], sender: MessageSender[String, String]): Unit =
    partition.foreach(publishToKafka(_, sender))
}

object IngestStream {
  val DEFAULT_CONFIG: Map[String, Object] = {
    Map(
      "bootstrap.servers"  -> IngestStreamConfig.kafka.bootstrapServers,
      "key.deserializer"   -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id"           -> IngestStreamConfig.conf.spark.groupId,
      "auto.offset.reset"  -> IngestStreamConfig.conf.spark.autoOffsetReset,
      "enable.auto.commit" -> (IngestStreamConfig.conf.spark.autoCommit: java.lang.Boolean)
    )
  }

  def sparkConfig: SparkConf =
    new SparkConf()
      .setAppName(IngestStreamConfig.kafka.applicationId)
      .setIfMissing("spark.master", "local[*]")
      .set("spark.serializer", classOf[org.apache.spark.serializer.KryoSerializer].getName)
      .set("spark.kryo.registrator", classOf[geotrellis.spark.io.kryo.KryoRegistrator].getName)
}

```

####  7.2.12. <a name='streamingProcessStream.scala'></a>/streaming/ProcessStream.scala

> reproject - tiff - fields

getRasterSource(`fields`.uri).`reproject`(_)

(_.`reproject`(source.`crs`)

fields
      .list
      .map(_.`reproject`(source.`crs`))
      .flatMap { field =>
        val `outputTiffPath` = new Path(s"${field.outputPath}/${field.id}.`tiff`")
        val outputJsonPath = new Path(s"${field.outputPath}/${field.id}.json")
        val result = source.read(field.polygon.envelope).map(_.mask(field.polygon))
        // 计算多边形统计数据stats，并将光栅和生成的统计数据stats作为JSON文件进行持久化calculate polygonal stats and persist both raster and generates stats as a JSON file

        result.map { raster =>
          val stats = raster.`polygonalStatsDouble`(`outputTiffPath`.toString)(field.polygon)
          raster.`toGeoTiff`(source.`crs`).write(`outputTiffPath`, conf.value)
          stats.write(outputJsonPath, conf.value)
        }
      }

> field

field.outputPath

field.id

field.polygon.envelope

fields.targetCRS.fold(
  getRasterSource(fields.uri)
  )

getRasterSource(fields.uri).reproject(_)

> path

`Path`(s"${field.`outputPath`}

raster.`polygonalStatsDouble`(`outputTiffPath`.toString)

.write(`outputTiffPath`, conf.value)

.write(`outputJsonPath`, conf.value)

> path

val `outputTiffPath` = new `Path`(s"${field.`outputPath`}/${field.id}.tiff")

val `outputJsonPath` = new `Path`(s"${field.`outputPath`}/${field.id}.json")

raster.`toGeoTiff`(source.`crs`).write(`outputTiffPath`, conf.value)

stats.write(`outputJsonPath`, conf.value)

```scala
/*
 * Copyright 2019 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azavea.streaming

import com.azavea._
import com.azavea.json.{Fields, PolygonalStatsDouble}

import geotrellis.spark.io.hadoop._
import geotrellis.contrib.vlm.RasterSource
import org.apache.hadoop.fs.Path

object ProcessStream {
  def apply(fields: Fields)(implicit conf: SerializableConfiguration): List[PolygonalStatsDouble] = {
    // reproject source
    val source = fields.targetCRS.fold(getRasterSource(fields.uri): RasterSource) { getRasterSource(fields.uri).reproject(_) }

    // reproject field into the source CRS
    fields
      .list
      .map(_.reproject(source.crs))
      .flatMap { field =>
        val outputTiffPath = new Path(s"${field.outputPath}/${field.id}.tiff")
        val outputJsonPath = new Path(s"${field.outputPath}/${field.id}.json")
        val result = source.read(field.polygon.envelope).map(_.mask(field.polygon))
        // calculate polygonal stats and persist both raster and generates stats as a JSON file

        result.map { raster =>
          val stats = raster.polygonalStatsDouble(outputTiffPath.toString)(field.polygon)
          raster.toGeoTiff(source.crs).write(outputTiffPath, conf.value)
          stats.write(outputJsonPath, conf.value)
        }
      }
  }
}

```

####  7.2.13. <a name='streamingpackage.scala'></a>/streaming/package.scala

> message

`getMessageSender`(servers: String)

`MessageSender`[String, String]

> 序列化 - kafka

org.apache.`kafka`.common.`serialization`.`StringSerializer`

```scala
/*
 * Copyright 2019 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azavea

import com.azavea.kafka.MessageSender
import org.apache.kafka.common.serialization.StringSerializer

package object streaming extends Serializable {
  def getMessageSender(servers: String): MessageSender[String, String] =
    MessageSender[String, String](servers, classOf[StringSerializer].getName, classOf[StringSerializer].getName)
}

```