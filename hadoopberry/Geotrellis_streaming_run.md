# GeoTrellis Streaming demo

这是一个包含通过`GeoTrellis` 渲染 tiff 过程的项目。
输入数据是一个 `Kafka` 流，输出 -一组栅格和一些 json 元数据输出。

该项目包含两个子项目：`streaming`（实际的流媒体应用程序）和
`producer`（用于测试目的的子项目，生成测试 kafka 消息）。

- [Environment](#environment)
- [Makefile commands](#makefile-commands)
- [application.conf description](#applicationconf-description)
- [Usage Example in SBT shell](#usage-example-in-sbt-shell)
- [Usage example](#usage-example)
  - [Kafka in Docker usage](#kafka-in-docker-usage)

## Kafka in Docker usage

1. 在 `/etc/hosts` 文件中添加以下别名：```127.0.0.1 localhost kafka```（绝对是 Mac OS 设置的工作变体：```127.0.0.1 localhost.localdomain localhost kafka ` ``)
2. 运行`make kafka`

## Environment

要在任何“Spark”模式下运行此应用程序，请确保您的计算机上安装了正确的“Spark”客户端。

要启动一个 `Kafka` 实例，运行 `make kafka` 命令就足够了（有关 docker 中 kafka 的更多详细信息在 [Kafka in docker](#kafka-in-docker-usage) 部分中提供）。

确保所有必要的更改都被引入到“application.conf”文件中。

## Makefile commands

提供 Makefile 以简化应用程序的启动和集成测试。

| Command                 | Description
|-------------------------|--------------------------------------------------------------------------|
|local-spark-demo         |在本地 Spark 服务器上运行 Spark 流程序集                    |
|local-spark-shell        |在本地运行带有fat jar的spark-shell                           |
|build                    |构建一个 fat jar 以在 Spark 上运行                                     |
|clean                    |清理目标                                                         |
|kafka                    |运行一个 dockerized kafka，查看 README.md 了解更多信息              |
|kafka-send-messages      |生成演示 kafka 消息                                           |
|sbt-spark-demo           |从 SBT shell 运行 Spark 流应用程序                      |

## application.conf 描述

通过资源文件夹（`streaming`）中的配置文件提供的应用程序设置。

```conf
ingest.stream {
  # kafka setting
  kafka {
    threads           = 10
    topic             = "geotrellis-streaming"
    otopic            = "geotrellis-streaming-output"
    application-id    = "geotrellis-streaming"
    bootstrap-servers = "localhost:9092"
  }
  # spark streaming settings
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

# geotrellis gdal VLM settings
vlm {
  geotiff.s3 {
    allow-global-read: false
    region: "us-west-2"
  }

  gdal.options {
    GDAL_DISABLE_READDIR_ON_OPEN = "YES"
    CPL_VSIL_CURL_ALLOWED_EXTENSIONS = ".tif"
  }

  # if true then uses GDALRasterSources, if false GeoTiffRasterSources
  source.gdal.enabled = true
}
```

通过资源文件夹（`producer`）中的配置文件提供的应用程序设置。

```conf
lc8 {
  scenes = [
    {
      name = "LC08_L1TP_139044_20170304_20170316_01_T1" # name of the LC8 scene
      band = "1" # band number
      count = 2 # number of generated polygons
      crs = "EPSG:4326" # desired generated CRS
      output-path = "../data/img" # the output path where the result output should be placed after processing
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
```

## SBT shell 中的使用示例

1. 例如，我们已经在 9092 端口本地运行了 `Kafka`。
  1.1 如果没有，可以启动【docker in Kafka】(#kafka-in-docker-usage)
2. 在两个独立的终端窗口中打开两个项目`producer`和`streaming`
3. `run` 一个流式应用程序（`project streaming`，`run`）
4. `run` 一个 procuder 应用程序（`project producer`，`run --generate-and-send`）

总结一下：

Terminal №1:

```bash
$ make kafka
```

Terminal №2:

```bash
$ cd app; ./sbt
$ project streaming
$ run
```

or

```bash
$ make sbt-spark-demo
```

Terminal №3:

```bash
$ ./sbt
$ project producer
$ run --generate-and-send
```

or

```bash
$ make kafka-send-messages
```

额外总结：

```bash
# terminal 1
make kafka
# terminal 2
make sbt-spark-demo
# terminal 3
make kafka-send-messages
```

## 使用示例

1. 例如，我们已经在 9092 端口上本地运行了 `Kafka`。如果没有，可以启动[Kafka in docker](#kafka-in-docker-usage)
2. 构建一个组装 fat jar：`make build`
3. 启动`Spark`应用程序：`make local-spark-processing`
4. 发布测试kafka消息：`make kafka-send-messages`

总结一下：

```bash
$ make build && make local-spark-processing
$ make kafka-send-messages
```

# GeoTrellis setup

GeoTrellis代码无法在Java 7或以下版本中运行。你可以在Linux或Mac终端输入以下命令来测试你的Java版本:

```sh
javac -version
```

当运行更复杂的应用程序时，spark-submit应该在你的PATH中:

GeoGrillis是一个Scala库，因此您自然必须用Scala编写应用程序。如果您是Scala新手，我们建议您好好学习scala

## GeoTrellis Spark 项目模板

GeoGrillis维护一个g8模板，用于引导依赖Spark的新GeoGrillis ingest项目。通过以下方式获得：

```sh
git clone https://github.com/geotrellis/geotrellis-spark-job.g8
```

你不需要安装sbt来编写GeoTrellis应用程序，因为这个模板包含了sbt引导脚本。它像普通的SBT一样使用，并带有一些额外的命令:

输入SBT shell: 

```sh
./sbt
```

运行测试:

```sh
./sbt test
```

强制 Scala 2.11(默认):

```sh
./sbt -211
```

Force Scala 2.12: 

```sh
./sbt -212
```

GeoTrellis实际上是一个由许多模块组成的库套件。

我们的设计使您可以根据项目需要依赖或多或少的GeoTrellis。

要依赖一个新模块，请将它添加到`build.sbt`中的`libraryDependencies`列表中:

```sh
libraryDependencies ++= Seq(
    "org.locationtech.geotrellis" %% "geotrellis-spark"    % "3.0.0",
    "org.locationtech.geotrellis" %% "geotrellis-s3-spark" % "3.0.0", // now we can use the Amazon S3 store!
    "org.apache.spark"            %% "spark-core"          % "2.4.3" % "provided",
    "org.scalatest"               %% "scalatest"           % "3.0.8" % "test"
)
```

现在您已经建立了一个简单的GeoTrellis环境，现在是时候对它的一些功能进行实践了。

## GeoTrellis模块层次结构

这是所有GeoTrellis模块的完整列表。虽然它们之间存在一定的相互依赖关系，

但您可以在您的`build.sbt`中依赖任意多(或任意少)的它们。

## geotrellis-accumulo

为 Apache Accumulo 实现了 geotrellis.store 类型。

提供：`geotrellis.store.accumulo.*`

在“Accumulo”中保存和加载图层。使用图层查询 API 高效查询大型图层。


## geotrellis-accumulo-spark

实现了 `geotrellis.spark.store` types 为了 `Apache Accumulo`, 从而拓展 `geotrellis-accumulo`.

提供： `geotrellis.spark.store.accumulo.*`

保存和加载图层 to and from `Accumulo` within a Spark Context using `RDDs`.

Supoort Accumulo backend 为了 `TileLayerRDDs`.

## geotrellis-cassandra

实现了 `geotrellis.store` types 为了 `Apache Cassandra`.

提供： `geotrellis.store.cassandra.*`

保存和加载图层 to and from `Cassandra.` Query large layers efficiently using the layer query API.

## geotrellis-cassandra-spark

实现了 geotrellis.spark.store types 为了 Apache Cassandra, 从而拓展 geotrellis-cassandra.

提供： geotrellis.spark.store.cassandra.*

保存和加载图层 to and from Cassandra within a Spark Context using RDDs.
Supoort Accumulo backend 为了 TileLayerRDDs.

## geotrellis-gdal

实现了 GeoTrellis GDAL support.

提供： geotrellis.raster.gdal.*

实现了 GDALRasterSources to read, reproject, resample, convert rasters. Performs all transformations via GDALWarp.

## geotrellis-gdal-spark

Contains geotrellis.raster.gdal.* integration tests 为了 Spark.

## geotrellis-geomesa

Experimental. GeoTrellis compatibility 为了 the distributed feature store GeoMesa.

提供： geotrellis.geomesa.geotools.* 提供： geotrellis.spark.store.geomesa.*

Save and load RDDs of features to and from GeoMesa.

## geotrellis-geotools

提供： geotrellis.geotools.*

Conversion functions between GeoTrellis, OpenGIS and GeoTools Features.

提供： geotrellis.geotools.*

## geotrellis-geowave

Experimental. GeoTrellis compatibility 为了 the distributed feature store GeoWave.

提供： geotrellis.spark.io.geowave.*

Save and load RDDs of features to and from GeoWave.

## geotrellis-hbase

实现了 geotrellis.store types 为了 Apache HBase.

提供： geotrellis.store.hbase.*

保存和加载图层 to and from HBase. Query large layers efficiently using the layer query API.

## geotrellis-hbase-spark

实现了 geotrellis.spark.store types 为了 Apache hbase, 从而拓展 geotrellis-hbase.

提供： geotrellis.spark.store.hbase.*

保存和加载图层 to and from HBase within a Spark Context using RDDs.
Supoort Accumulo backend 为了 TileLayerRDDs.

## geotrellis-layer

Datatypes to describe Layers (sets of spatially referenced rasters).

提供： geotrellis.layer.*

Generic way to represent key value Seq as layers, where the key represents a coordinate in space based on some uniform grid layout, optionally with a temporal component.

Contains data types to describe LayoutSchemes and LayoutDefinitions, KeyBounds, layer key types (SpatialKey, SpaceTimeKey) and TileLayerMetadata layer metadata type.

实现了 SpaceTimeKey collection layer projection to the SpatialKey space.

MapAlgebra (focal and local) 为了 collection layers.

Mask and Stitch operations 为了 collection layers.

实现了 tiling 为了 RasterSources.

## geotrellis-macros

The intention of this package is to keep API both performant and expressive enough.

提供： geotrellis.macros.*

Contains inline macro implementations 为了 Tile NoData, foreach, map and some type conversions.

## geotrellis-proj4

提供： geotrellis.proj4.*, org.osgeo.proj4.* (Java)

Represent a Coordinate Reference System (CRS) based on Ellipsoid, Datum, and Projection.

Translate CRSs to and from proj4 string representations.
Lookup CRS’s based on EPSG and other codes.

Transform (x, y) coordinates from one CRS to another.

## geotrellis-raster

Types and algorithms 为了 Raster processing.

提供： geotrellis.raster.*

Provides types to represent single- and multi-band rasters, supporting Bit, Byte, UByte, Short, UShort, Int, Float, and Double data, with either a constant NoData value (which improves performance) or a user defined NoData value.
Treat a tile as a collection of values, by calling “map” and “foreach”, along with floating point valued versions of those methods (separated out 为了 performance).
Combine raster data in generic ways.
Render rasters via color ramps and color maps to PNG and JPG images.
Read GeoTiffs with DEFLATE, LZW, and PackBits compression, including horizontal and floating point prediction 为了 LZW and DEFLATE.
Write GeoTiffs with DEFLATE or no compression.
Reproject rasters from one CRS to another.
Resample of raster data.
Mask and Crop rasters.
Split rasters into smaller tiles, and stitch tiles into larger rasters.
Derive histograms from rasters in order to represent the distribution of values and create quantile breaks.
Local Map Algebra operations: Abs, Acos, Add, And, Asin, Atan, Atan2, Ceil, Cos, Cosh, Defined, Divide, Equal, Floor, Greater, GreaterOrEqual, InverseMask, Less, LessOrEqual, Log, Majority, Mask, Max, MaxN, Mean, Min, MinN, Minority, Multiply, Negate, Not, Or, Pow, Round, Sin, Sinh, Sqrt, Subtract, Tan, Tanh, Undefined, Unequal, Variance, Variety, Xor, If
Focal Map Algebra operations: Hillshade, Aspect, Slope, Convolve, Conway’s Game of Life, Max, Mean, Median, Mode, Min, MoransI, StandardDeviation, Sum
Zonal Map Algebra operations: ZonalHistogram, ZonalPercentage
Polygonal Summary operations that summarize raster data intersecting polygons: Min, Mean, Max, Sum, Histogram.
Cost distance operation based on a set of starting points and a friction raster.
Hydrology operations: Accumulation, Fill, and FlowDirection.
Rasterization of geometries and the ability to iterate over cell values covered by geometries.
Vectorization of raster data.
Kriging Interpolation of point data into rasters.
Viewshed operation.
RegionGroup operation.
Kernel density estimation.
Raster histogram equalization and matching methods.
Delaunay triangulation rasterizer.
Provides an abstract, higher order API 为了 reading RasterSources from different sources. RasterSource is an abstraction over I/O implementations. Other GeoTrellis packages provide concrete RasterSource implementations, such as GDALRasterSource in a geotrellis.raster.gdal package.
实现了 lazy RasterSource transformation operations: reprojection, resampling and cellType conversion.

## geotrellis-raster-testkit

Integration tests 为了 geotrellis-raster.

Build test raster data.
Assert raster data matches Array data or other rasters in scalatest.

## geotrellis-s3

实现了 the geotrellis.store types 为了 the AWS Simple Storage Service (S3) backend.

Allows the use of Amazon S3 as a Tile layer backend.

提供： geotrellis.store.s3.*

Save/load raster layers to/from S3
Save/load Cloud Optimized GeoTiffs (COGs) to/from S3

## geotrellis-s3-spark

实现了 geotrellis.store and geotrellis.spark types 为了 interoperability between GeoTrellis, Spark and S3.

提供： geotrellis.spark.store.s3.*

Save/load Spark RDD Tile layers to/from S3
Support S3 operations on GeoTiff, COG and Slippy tiles
Use SaveToS3 to save pyramided image and vector tile layers in X/Y/Z format

## geotrellis-shapefile

提供： geotrellis.shapefile.*

Read geometry and feature data from shapefiles into GeoTrellis types using GeoTools.

## geotrellis-spark

Tile layer algorithms powered by Apache Spark.

提供： geotrellis.spark.*

Generic way to represent key value RDDs as layers, where the key represents a coordinate in space based on some uniform grid layout, optionally with a temporal component.
Represent spatial or spatiotemporal raster data as an RDD of raster tiles.
Generic architecture 为了 saving/loading layers RDD data and metadata to/from various backends, using Spark’s IO API with Space Filling Curve indexing to optimize storage retrieval (support 为了 Hilbert curve and Z order curve SFCs). HDFS and local file system are supported backends by default, S3 and Accumulo are supported backends by the geotrellis-s3 and geotrellis-accumulo projects, respectively.
Query architecture that allows 为了 simple querying of layer data by spatial or spatiotemporal bounds.
Perform map algebra operations on layers of raster data, including all supported Map Algebra operations mentioned in the geotrellis-raster feature list.
Perform seamless reprojection on raster layers, using neighboring tile information in the reprojection to avoid unwanted NoData cells.
Pyramid up layers through zoom levels using various resampling methods.
Types to reason about tiled raster layouts in various CRS’s and schemes.
Perform operations on raster RDD layers: crop, filter, join, mask, merge, partition, pyramid, render, resample, split, stitch, and tile.
Polygonal summary over raster layers: Min, Mean, Max, Sum.
Save spatially keyed RDDs of byte arrays to z/x/y files into HDFS or the local file system. Useful 为了 saving PNGs off 为了 use as map layers in web maps or 为了 accessing GeoTiffs through z/x/y tile coordinates.
Utilities around creating spark contexts 为了 applications using GeoTrellis, including a Kryo registrator that registers most types.
实现了 GeoTrellis COGLayer creation, persistence and query mechanisms.

## geotrellis-spark-pipeline

Pipelines are the operative construct in GeoTrellis, the original idea was taken from PDAL. Pipelines represent a set of instructions rather than a simple ETL process: how to read data, transform (process), write it. The result of the Pipeline should not always be writing, it can also be some intermediate transformation result, or just a raw data.

提供： geotrellis.spark.pipeline.*

Provides a JSON DSL that represents a set of instructions performed on some data source.
Provides a Scala DSL that abstracts over GeoTrellis pipeline operations. It also allows users to avoid manually writing the JSON DSL.
Allows reads (from local file system, s3, hdfs, etc), transformations (tile-to-layout, reproject, pyramid) and writes (all supported GeoTrellis stores).

## geotrellis-spark-testkit

Integration tests 为了 geotrellis-spark.

Utility code to create test RDDs of raster data.
Matching methods to test equality of RDDs of raster data in scalatest unit tests.

## geotrellis-store

Types and interfaces 为了 interacting with a number of different storage backends in an abstract way.

In older versions of GeoTrellis, store implementations were referred to as backends.

提供： geotrellis.store.*

Contains interfaces 为了 LayerReaders, LayerWriters and ValueReaders.
Avro Tile codecs.
Local file system and HDFS COG and GeoTrellis Value and Collection readers implementation.
Indexing strategies implementation: ZCurve and HilbertCurve.
GeoTrellisRasterSources that implement access to GeoTrellis layers through the new API.

## geotrellis-util

Plumbing 为了 other GeoTrellis modules.

提供： geotrellis.util.*

Constants
Data structures missing from Scala, such as BTree
Haversine implementation
Lenses
RangeReaderProvider 为了 reading contiguous subsets of data from a source
Implementations 为了 FileRangeReader and HttpRangeReader

## geotrellis-vector

Types and algorithms 为了 processing Vector data.

提供： geotrellis.vector.*

Provides idiomatic helpers 为了 the JTS types: Point, LineString, Polygon, MultiPoint, MultiLineString, MultiPolygon, GeometryCollection
Methods 为了 geometric operations supported in JTS, with results that provide a type-safe way to match over possible results of geometries.
Provides a Feature type that is the composition of an id, geometry and a generic data type.
Read and write geometries and features to and from GeoJSON.
Read and write geometries to and from WKT and WKB.
Reproject geometries between two CRSs.
Geometric operations: Convex Hull, Densification, Simplification
Perform Kriging interpolation on point values.
Perform affine transformations of geometries

## geotrellis-vector-testkit

Integration tests 为了 geotrellis-vector.

GeometryBuilder 为了 building test geometries
GeometryMatcher 为了 scalatest unit tests, which aides in testing equality in geometries with an optional threshold.

## geotrellis-vectortile

Experimental. A full Mapbox VectorTile codec.

提供： geotrellis.vectortile.*

Lazy decoding
Read/write VectorTile tile layers from any tile backend

# zeppelin docker

# zeppelin kafka

# zeppelin sbt