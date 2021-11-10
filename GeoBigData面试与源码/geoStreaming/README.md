# OpenTDSB

# geotrellis

# geopyspark

# geowave

# geomesa

# hive

# spark

# springcloud

# java

# scala

# GDAL

# geotools

# mapbox

# openlayers

# HBase

# functions

```java
import org.apache.flink.table.functions.AggregateFunctionDefinition;
import org.apache.flink.table.functions.BuiltInFunctionDefinition;
import org.apache.flink.table.functions.BuiltInFunctionDefinitions;
import org.apache.flink.table.functions.AggregateFunction;
import org.apache.flink.table.functions.FunctionDefinition;
import org.apache.flink.table.functions.FunctionIdentifier;
import org.apache.flink.table.functions.ScalarFunction;
import org.apache.flink.table.functions.TableFunction;
import org.apache.flink.table.functions.SpecializedFunction.SpecializedContext;
import org.apache.flink.table.functions.python.utils.PythonFunctionUtils;
import org.apache.flink.table.functions.python.PythonFunction;
import org.apache.flink.table.functions.python.PythonFunctionInfo;
import org.apache.flink.table.functions.python.PythonEnv;
import org.apache.flink.table.functions.python.PythonFunction;

import org.apache.flink.table.functions.FunctionContext;
import org.apache.flink.table.functions.UserDefinedFunction;
import org.apache.flink.table.functions.AsyncTableFunction;
import org.apache.flink.table.runtime.generated.GeneratedFunction;

import static org.apache.flink.table.functions.FunctionKind.AGGREGATE;
import static org.apache.flink.table.functions.FunctionKind.OTHER;
import static org.apache.flink.table.functions.FunctionKind.SCALAR;
import org.apache.flink.table.annotation.FunctionHint;



import org.apache.flink.table.catalog.CatalogFunctionImpl;
import org.apache.flink.table.functions.FunctionDefinition;
import org.apache.flink.table.catalog.CatalogFunction;
import org.apache.flink.table.catalog.FunctionCatalog;
import org.apache.flink.table.catalog.FunctionLanguage;
import org.apache.flink.table.catalog.FunctionLookup;
import org.apache.flink.table.catalog.exceptions.FunctionNotExistException;
import org.apache.flink.table.planner.functions.sql.internal.SqlAuxiliaryGroupAggFunction;
import org.apache.flink.table.planner.plan.nodes.physical.stream.StreamPhysicalWindowTableFunction;
import org.apache.flink.table.factories.FunctionDefinitionFactory;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.Function;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.functions.util.FunctionUtils;
import org.apache.flink.api.common.functions.CombineFunction;
import org.apache.flink.api.common.functions.GroupCombineFunction;
import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.api.common.functions.RichGroupReduceFunction;
import org.apache.flink.api.common.functions.IterationRuntimeContext;


import org.apache.flink.api.java.operators.translation.WrappingFunction;
import org.apache.flink.streaming.api.operators.collect.utils.CollectSinkFunctionTestWrapper;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;

import org.apache.calcite.sql.SqlAggFunction;
import org.apache.calcite.sql.SqlFunction;

import org.apache.flink.util.function.TriConsumer;
import org.apache.flink.util.function.TriFunction;
import org.apache.flink.util.function.QuadFunction;
import org.apache.flink.util.function.BiFunctionWithException;
import org.apache.flink.util.function.FunctionWithException;
import org.apache.flink.util.function.RunnableWithException;

import org.apache.flink.cep.functions.PatternProcessFunction;
import org.apache.flink.cep.functions.TimedOutPartialMatchHandler;
import org.apache.flink.util.function.TriFunction;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.Consumer;
```

```python
__all__ = ['FunctionContext', 'AggregateFunction', 'ScalarFunction', 'TableFunction',
           'TableAggregateFunction', 'udf', 'udtf', 'udaf', 'udtaf']

__all__ = [
    'RuntimeContext',
    'MapFunction',
    'CoMapFunction',
    'FlatMapFunction',
    'CoFlatMapFunction',
    'ReduceFunction',
    'AggregateFunction',
    'KeySelector',
    'FilterFunction',
    'Partitioner',
    'SourceFunction',
    'SinkFunction',
    'ProcessFunction',
    'CoProcessFunction',
    'KeyedProcessFunction',
    'KeyedCoProcessFunction',
    'TimerService',
    'WindowFunction',
    'ProcessWindowFunction']

# 01
from pyflink.datastream.functions import (_get_python_env, FlatMapFunction, MapFunction, Function,
                                          FunctionWrapper, SinkFunction, FilterFunction,
                                          KeySelector, ReduceFunction, CoMapFunction,
                                          CoFlatMapFunction, Partitioner, RuntimeContext,
                                          ProcessFunction, KeyedProcessFunction,
                                          KeyedCoProcessFunction, WindowFunction,
                                          ProcessWindowFunction, InternalWindowFunction,
                                          InternalIterableWindowFunction,
                                          InternalIterableProcessWindowFunction, CoProcessFunction)

# 02
from pyflink.fn_execution.datastream.process_function import \
    InternalKeyedProcessFunctionOnTimerContext, InternalKeyedProcessFunctionContext, \
    InternalProcessFunctionContext


# 03
from pyflink.fn_execution.utils.operation_utils import extract_user_defined_aggregate_function

try:
    from pyflink.fn_execution.table.aggregate_fast import RowKeySelector, \
        SimpleAggsHandleFunction, GroupAggFunction, DistinctViewDescriptor, \
        SimpleTableAggsHandleFunction, GroupTableAggFunction
    from pyflink.fn_execution.table.window_aggregate_fast import \
        SimpleNamespaceAggsHandleFunction, GroupWindowAggFunction
    from pyflink.fn_execution.coder_impl_fast import InternalRow
    has_cython = True
except ImportError:
    from pyflink.fn_execution.table.aggregate_slow import RowKeySelector, \
        SimpleAggsHandleFunction, GroupAggFunction, DistinctViewDescriptor, \
        SimpleTableAggsHandleFunction, GroupTableAggFunction
    from pyflink.fn_execution.table.window_aggregate_slow import \
        SimpleNamespaceAggsHandleFunction, GroupWindowAggFunction
    has_cython = False

from pyflink.table import FunctionContext, Row

# 04
from pyflink.table import functions
from pyflink.table.udf import DelegationTableFunction, DelegatingScalarFunction, \
    ImperativeAggregateFunction, PandasAggregateFunctionWrapper

# 05
from pyflink.datastream.functions import KeyedProcessFunction, KeyedCoProcessFunction, \
    ProcessFunction, CoProcessFunction

# 06
from pyflink.table.udf import UserDefinedFunctionWrapper, AggregateFunction, udaf, \
    udtaf, TableAggregateFunction

# 07
from pyflink.table import AggregateFunction, MapView, ListView

# 08
from pyflink.datastream.functions import (AggregateFunction, CoMapFunction, CoFlatMapFunction,
                                          MapFunction, FilterFunction, FlatMapFunction,
                                          KeyedCoProcessFunction, KeyedProcessFunction, KeySelector,
                                          ProcessFunction, ProcessWindowFunction, ReduceFunction,
                                          WindowFunction)
from pyflink.datastream.tests.test_util import DataStreamTestSinkFunction

# 09
from pyflink.table.catalog import ObjectPath, Catalog, CatalogDatabase, CatalogBaseTable, \
    CatalogFunction, CatalogPartition, CatalogPartitionSpec
from pyflink.testing.test_case_utils import PyFlinkTestCase
from pyflink.util.exceptions import DatabaseNotExistException, FunctionNotExistException, \
    PartitionNotExistException, TableNotExistException, DatabaseAlreadyExistException, \
    FunctionAlreadyExistException, PartitionAlreadyExistsException, PartitionSpecInvalidException, \
    TableNotPartitionedException, TableAlreadyExistException, DatabaseNotEmptyException

# 10
from pyflink.fn_execution.table.window_process_function import GeneralWindowProcessFunction, \
    InternalWindowProcessFunction, PanedWindowProcessFunction, MergingWindowProcessFunction
from pyflink.fn_execution.table.window_trigger import Trigger
from pyflink.table.udf import ImperativeAggregateFunction, FunctionContext

# 11
from pyflink.table.udf import TableFunction, udtf, ScalarFunction, 

# 12
from pyflink.table import AggregateFunction, FunctionContext, TableAggregateFunction
from pyflink.table.udf import ImperativeAggregateFunction

# 13
from pyflink.table.udf import UserDefinedFunctionWrapper, UserDefinedTableFunctionWrapper

# 14
from pyflink.table.udf import UserDefinedFunctionWrapper, UserDefinedTableFunctionWrapper

# 15
from pyflink.fn_execution.utils.operation_utils import is_built_in_function, load_aggregate_function
from pyflink.table import FunctionContext

# 16
from pyflink.table.udf import AggregateFunction, udaf

# 17
from pyflink.table.udf import UserDefinedScalarFunctionWrapper, \
    UserDefinedAggregateFunctionWrapper, UserDefinedTableFunctionWrapper

# 18
from pyflink.datastream.functions import WindowFunction
from pyflink.datastream.tests.test_util import 

# 19
from pyflink.table.udf import udaf, udf, AggregateFunction

# 20
from pyflink.datastream.tests.test_util import 

# 21
from pyflink.datastream.functions import SinkFunction

# 22
from pyflink.table.udf import udf, udtf, udaf, AggregateFunction, TableAggregateFunction, 
```

#  富函数（Rich Functions）
[“富函数”](https://www.jianshu.com/p/b2e9cc8569f8)是DataStream API提供的一个函数类的接口，所有Flink函数类都有其Rich版本。
它与常规函数的不同在于，可以获取*运行环境的上下文*，并拥有一些*生命周期方法*，所以可以实现更复杂的功能。

* RichMapFunction
* RichFlatMapFunction
* RichFilterFunction
…​
Rich Function有一个*生命周期*的概念。典型的生命周期方法有：

*open()方法*是rich function的初始化方法，当一个算子例如map或者filter被调用之前open()会被调用。
*close()方法*是生命周期中的最后一个调用的方法，做一些清理工作。
*getRuntimeContext()方法*提供了函数的RuntimeContext的一些信息，
例如函数执行的并行度，任务的名字，以及state状态

举例，FlatMap的富函数实现：

```js
class MyFlatMap extends RichFlatMapFunction[Int, (Int, Int)] {
var subTaskIndex = 0

// open()方法
override def open(configuration: Configuration): Unit = {
subTaskIndex = getRuntimeContext.getIndexOfThisSubtask
// 以下可以做一些初始化工作，例如建立一个和HDFS的连接
}

// close()方法
override def flatMap(in: Int, out: Collector[(Int, Int)]): Unit = {
if (in % 2 == subTaskIndex) {
out.collect((subTaskIndex, in))
}
}

// getRuntimeContext()
override def close(): Unit = {
// 以下做一些清理工作，例如断开和HDFS的连接。
}
}
```



# geojson和CSV的处理函数
[采用了java语言](https://github.com/aistairc/SpatialFlink/blob/75eca4d39717f625cd8e6fafb243bd74ae8ebbb5/src/main/java/GeoFlink/spatialStreams/Deserialization.java)
```java
    public static class GeoJSONToSpatial extends RichMapFunction<ObjectNode, Point> {
        // {"key":136138,"value":{"geometry":{"coordinates":[116.44412,39.93984],"type":"Point"},
        // "properties":{"oID":"2560","timestamp":"2008-02-02 20:12:32"},"type":"Feature"}}
        // --> [ObjID: null, 116.56444, 40.07079, 0001200005, 0, 1611022449423]

        UniformGrid uGrid;

        //ctor
        public  GeoJSONToSpatial() {};
        public  GeoJSONToSpatial(UniformGrid uGrid)
        {
            this.uGrid = uGrid;
        };

        @Override
        public Point map(ObjectNode jsonObj) throws Exception {

            Point spatialPoint = new Point(jsonObj.get("value").get("geometry").get("coordinates").get(0).asDouble(), jsonObj.get("value").get("geometry").get("coordinates").get(1).asDouble(), uGrid);
            return spatialPoint;
        }
    }
```

```java
    public static class GeoJSONToTSpatial extends RichMapFunction<ObjectNode, Point> {

        UniformGrid uGrid;
        DateFormat dateFormat;

        //ctor
        public  GeoJSONToTSpatial() {};
        public  GeoJSONToTSpatial(UniformGrid uGrid, DateFormat dateFormat)
        {

            this.uGrid = uGrid;
            this.dateFormat = dateFormat;
        };

        @Override
        public Point map(ObjectNode jsonObj) throws Exception {

            Point spatialPoint;
            Date date = new Date();


            if (this.dateFormat == null) {
                spatialPoint = new Point(jsonObj.get("value").get("properties").get("oID").asText(), jsonObj.get("value").get("geometry").get("coordinates").get(0).asDouble(), jsonObj.get("value").get("geometry").get("coordinates").get(1).asDouble(), jsonObj.get("value").get("properties").get("timestamp").asLong(), uGrid);
                //spatialPoint = new Point(jsonObj.get("value").get("properties").get("oID").asText(), jsonObj.get("value").get("geometry").get("coordinates").get(0).asDouble(), jsonObj.get("value").get("geometry").get("coordinates").get(1).asDouble(), date.getTime(), uGrid);
            }
            else {
                Date dateTime = this.dateFormat.parse(jsonObj.get("value").get("properties").get("timestamp").asText());
                long timeStampMillisec = dateTime.getTime();
                spatialPoint = new Point(jsonObj.get("value").get("properties").get("oID").asText(), jsonObj.get("value").get("geometry").get("coordinates").get(0).asDouble(), jsonObj.get("value").get("geometry").get("coordinates").get(1).asDouble(), timeStampMillisec, uGrid);
            }

            return spatialPoint;
        }
    }
```

```java
    public static class GeoJSONToSpatial extends RichMapFunction<ObjectNode, Point> {

        // {"key":136138,"value":{"geometry":{"coordinates":[116.44412,39.93984],"type":"Point"},"properties":{"oID":"2560","timestamp":"2008-02-02 20:12:32"},"type":"Feature"}}
        // --> [ObjID: null, 116.56444, 40.07079, 0001200005, 0, 1611022449423]
        UniformGrid uGrid;

        //ctor
        public  GeoJSONToSpatial() {};
        public  GeoJSONToSpatial(UniformGrid uGrid)
        {
            this.uGrid = uGrid;
        };

        @Override
        public Point map(ObjectNode jsonObj) throws Exception {

            Geometry geometry;
            try {
                geometry = readGeoJSON(jsonObj.get("value").toString());
            }
            catch (Exception e) {
                // "type" が無いStringの場合はGeometryを抽出する
                String jsonGeometry = jsonObj.get("value").get("geometry").toString();
                geometry = readGeoJSON(jsonGeometry);
            }

            Point spatialPoint = new Point(geometry.getCoordinate().x, geometry.getCoordinate().y, uGrid);
            return spatialPoint;
        }
    }
```

```java
    public static class GeoJSONToTSpatial extends RichMapFunction<ObjectNode, Point> {

        UniformGrid uGrid;
        DateFormat dateFormat;
        String propertyTimeStamp;
        String propertyObjID;

        //ctor
        public  GeoJSONToTSpatial() {};
        public  GeoJSONToTSpatial(UniformGrid uGrid, DateFormat dateFormat, String propertyTimeStamp, String propertyObjID)
        {

            this.uGrid = uGrid;
            this.dateFormat = dateFormat;
            this.propertyTimeStamp = propertyTimeStamp;
            this.propertyObjID = propertyObjID;
        };

        @Override
        public Point map(ObjectNode jsonObj) throws Exception {

            Geometry geometry;
            try {
                geometry = readGeoJSON(jsonObj.get("value").toString());
            }
            catch (Exception e) {
                // "type" が無いStringの場合はGeometryを抽出する
                String jsonGeometry = jsonObj.get("value").get("geometry").toString();
                geometry = readGeoJSON(jsonGeometry);
            }

            JsonNode nodeProperties = jsonObj.get("value").get("properties");
            String strOId = null;
            long time = 0;
            if (nodeProperties != null) {
                JsonNode nodeTime = jsonObj.get("value").get("properties").get(propertyTimeStamp);
                try {
                    if (nodeTime != null && dateFormat != null) {
                        time = dateFormat.parse(nodeTime.textValue()).getTime();
                    }
                }
                catch (ParseException e) {}
                JsonNode nodeOId = jsonObj.get("value").get("properties").get(propertyObjID);
                if (nodeOId != null) {
                    //strOId = nodeOId.textValue();
                    strOId = nodeOId.toString().replaceAll("\\\"", "");

                }
            }
            Point spatialPoint;
            if (time != 0) {
                spatialPoint = new Point(strOId, geometry.getCoordinate().x, geometry.getCoordinate().y, time, uGrid);
                //spatialPoint = new Point(strOId, geometry.getCoordinate().x, geometry.getCoordinate().y, System.currentTimeMillis(), uGrid);
            }
            else {
                spatialPoint = new Point(strOId, geometry.getCoordinate().x, geometry.getCoordinate().y, 0, uGrid);
            }
            return spatialPoint;
        }
    }
```

```java
    // Assuming that csv string contains longitude and latitude at positions 0 and 1, respectively
    public static class CSVToSpatial extends RichMapFunction<ObjectNode, Point> {

        UniformGrid uGrid;

        //ctor
        public  CSVToSpatial() {};
        public  CSVToSpatial(UniformGrid uGrid)
        {
            this.uGrid = uGrid;
        };

        @Override
        public Point map(ObjectNode strTuple) throws Exception {

            List<String> strArrayList = Arrays.asList(strTuple.toString().split("\\s*,\\s*"));
            Point spatialPoint = new Point(Double.parseDouble(strArrayList.get(0)), Double.parseDouble(strArrayList.get(1)), uGrid);

            return spatialPoint;
        }
    }
```

```java
    // Assuming that csv string contains longitude and latitude at positions 0 and 1, respectively
    public static class CSVToTSpatial extends RichMapFunction<ObjectNode, Point> {

        UniformGrid uGrid;
        DateFormat dateFormat;

        //ctor
        public  CSVToTSpatial() {};
        public  CSVToTSpatial(UniformGrid uGrid, DateFormat dateFormat)
        {

            this.uGrid = uGrid;
            this.dateFormat = dateFormat;
        };

        @Override
        public Point map(ObjectNode strTuple) throws Exception {

            // customized for ATC Shopping mall data
            //A sample tuple/record: 1351039728.980,9471001,-22366,2452,1261.421,780.711,-2.415,-2.441
            // time [ms] (unixtime + milliseconds/1000), person id, position x [mm], position y [mm], position z (height) [mm], velocity [mm/s], angle of motion [rad], facing angle [rad]

            Point spatialPoint;
            Date date = new Date();
            List<String> strArrayList = Arrays.asList(strTuple.toString().split("\\s*,\\s*")); // For parsing CSV with , followed by space
            //List<String> strArrayList = Arrays.asList(strTuple.toString().split(","));

            if (this.dateFormat == null) {
                Long timeStampMillisec = Long.parseLong(strArrayList.get(0)) * 1000;
                spatialPoint = new Point(strArrayList.get(1), Double.parseDouble(strArrayList.get(2)), Double.parseDouble(strArrayList.get(3)), timeStampMillisec, uGrid);
            }
            else {
                Date dateTime = this.dateFormat.parse(strArrayList.get(0));
                long timeStampMillisec = dateTime.getTime();
                spatialPoint = new Point(strArrayList.get(1), Double.parseDouble(strArrayList.get(2)), Double.parseDouble(strArrayList.get(3)), timeStampMillisec, uGrid);
            }



            return spatialPoint;
        }
    }
```

```java
    // Assuming that csv/tsv string contains longitude and latitude at positions 0 and 1, respectively
    public static class CSVTSVToSpatial extends RichMapFunction<String, Point> {

        UniformGrid uGrid;
        String delimiter;
        List<Integer> csvTsvSchemaAttr;

        //ctor
        public CSVTSVToSpatial() {};
        public CSVTSVToSpatial(UniformGrid uGrid, String delimiter, List<Integer> csvTsvSchemaAttr)
        {
            this.uGrid = uGrid;
            this.delimiter = delimiter;
            this.csvTsvSchemaAttr = csvTsvSchemaAttr;
        };

        @Override
        public Point map(String str) throws Exception {
            List<String> strArrayList = Arrays.asList(str.replace("\"", "").split("\\s*" + delimiter + "\\s*")); // For parsing CSV with , followed by space
            double x = Double.valueOf(strArrayList.get(csvTsvSchemaAttr.get(2)));
            double y = Double.valueOf(strArrayList.get(csvTsvSchemaAttr.get(3)));;
            Point spatialPoint = new Point(x, y, uGrid);
            return spatialPoint;
        }
    }
```

```java
    // Assuming that csv/tsv string contains longitude and latitude at positions 0 and 1, respectively
    public static class CSVTSVToTSpatial extends RichMapFunction<String, Point> {

        UniformGrid uGrid;
        DateFormat dateFormat;
        String delimiter;
        List<Integer> csvTsvSchemaAttr;

        //ctor
        public CSVTSVToTSpatial() {};
        public CSVTSVToTSpatial(UniformGrid uGrid, DateFormat dateFormat, String delimiter, List<Integer> csvTsvSchemaAttr)
        {
            this.uGrid = uGrid;
            this.dateFormat = dateFormat;
            this.delimiter = delimiter;
            this.csvTsvSchemaAttr = csvTsvSchemaAttr;
        };

        @Override
        public Point map(String str) throws Exception {

            // customized for ATC Shopping mall data
            //A sample tuple/record: 1351039728.980,9471001,-22366,2452,1261.421,780.711,-2.415,-2.441
            // time [ms] (unixtime + milliseconds/1000), person id, position x [mm], position y [mm], position z (height) [mm], velocity [mm/s], angle of motion [rad], facing angle [rad]

            Point spatialPoint;
            List<String> strArrayList = Arrays.asList(str.replace("\"", "").split("\\s*" + delimiter + "\\s*")); // For parsing CSV with , followed by space
            String strOId = strArrayList.get(csvTsvSchemaAttr.get(0));
            long time = Long.valueOf(strArrayList.get(csvTsvSchemaAttr.get(1)));
            double x = Double.valueOf(strArrayList.get(csvTsvSchemaAttr.get(2)));
            double y = Double.valueOf(strArrayList.get(csvTsvSchemaAttr.get(3)));;

            spatialPoint = new Point(strOId, x, y, time, uGrid);
            return spatialPoint;
        }
    }
```

```java
    public static class GeoJSONToSpatialPolygon extends RichMapFunction<ObjectNode, Polygon> {

        UniformGrid uGrid;

        //ctor
        public  GeoJSONToSpatialPolygon() {};
        public  GeoJSONToSpatialPolygon(UniformGrid uGrid)
        {
            this.uGrid = uGrid;
        };

        //58> {"key":368387,"value":{"geometry":{"coordinates":[[[[-73.797919,40.681402],[-73.797885,40.681331],[-73.798032,40.681289],[-73.798048,40.681285],[-73.798067,40.681324],[-73.798075,40.681322],[-73.798092,40.681357],[-73.79806,40.681366],[-73.798058,40.681363],[-73.79801,40.681376],[-73.797919,40.681402]]]],"type":"MultiPolygon"},"properties":{"base_bbl":"4119880033","bin":"4259746","cnstrct_yr":"1955","doitt_id":"527355","feat_code":"2100","geomsource":"Photogramm","groundelev":"26","heightroof":"26.82","lstmoddate":"2017-08-22T00:00:00.000Z","lststatype":"Constructed","mpluto_bbl":"4119880033","name":null,"shape_area":"1375.27323008172","shape_len":"159.1112668769"},"type":"Feature"}}


        @Override
        public Polygon map(ObjectNode jsonObj) throws Exception {

            List<Coordinate> coordinates = new ArrayList<>();
            JsonNode JSONCoordinatesArray;
            //{"geometry": {"coordinates": [[[[-73.980455, 40.661994], [-73.980542, 40.661889], [-73.980559, 40.661897], [-73.98057, 40.661885], [-73.980611, 40.661904], [-73.9806, 40.661917], [-73.980513, 40.662022], [-73.980455, 40.661994]]]], "type": "MultiPolygon"}, "properties": {"base_bbl": "3011030028", "bin": "3026604", "cnstrct_yr": "1892", "doitt_id": "33583", "feat_code": "2100", "geomsource": "Photogramm", "groundelev": "153", "heightroof": "31.65", "lstmoddate": "2020-01-28T00:00:00.000Z", "lststatype": "Constructed", "mpluto_bbl": "3011030028", "name": null, "shape_area": "926.10935740605", "shape_len": "139.11922551796"}, "type": "Feature"}

            // Differentiate Polygon and MultiPolygon
            if(jsonObj.get("value").get("geometry").get("type").asText().equalsIgnoreCase("MultiPolygon")) {
                JSONCoordinatesArray = jsonObj.get("value").get("geometry").get("coordinates").get(0).get(0);
            }
            else if (jsonObj.get("value").get("geometry").get("type").asText().equalsIgnoreCase("Polygon")){ // Polygon case??
                System.out.println(jsonObj.get("value").get("geometry").get("type").asText());
                JSONCoordinatesArray = jsonObj.get("value").get("geometry").get("coordinates").get(0);
            }
            else { // Point case ??
                System.out.println("Not polygon but: " + jsonObj.get("value").get("geometry").get("type").asText());
                JSONCoordinatesArray = jsonObj.get("value").get("geometry").get("coordinates").get(0);
            }

            if (JSONCoordinatesArray.isArray()) {
                for (final JsonNode JSONCoordinate : JSONCoordinatesArray) {
                    //Coordinate(latitude, longitude)
                    coordinates.add(new Coordinate(JSONCoordinate.get(0).asDouble(), JSONCoordinate.get(1).asDouble()));
                }
            }

            Polygon spatialPolygon = new Polygon(coordinates, uGrid);
            return spatialPolygon;
        }
    }
```

```java
    public static class GeoJSONToSpatialPolygon extends RichMapFunction<ObjectNode, Polygon> {

        UniformGrid uGrid;

        //ctor
        public  GeoJSONToSpatialPolygon() {};
        public  GeoJSONToSpatialPolygon(UniformGrid uGrid)
        {
            this.uGrid = uGrid;
        };

        //58> {"key":368387,"value":{"geometry":{"coordinates":[[[[-73.797919,40.681402],[-73.797885,40.681331],[-73.798032,40.681289],[-73.798048,40.681285],[-73.798067,40.681324],[-73.798075,40.681322],[-73.798092,40.681357],[-73.79806,40.681366],[-73.798058,40.681363],[-73.79801,40.681376],[-73.797919,40.681402]]]],"type":"MultiPolygon"},"properties":{"base_bbl":"4119880033","bin":"4259746","cnstrct_yr":"1955","doitt_id":"527355","feat_code":"2100","geomsource":"Photogramm","groundelev":"26","heightroof":"26.82","lstmoddate":"2017-08-22T00:00:00.000Z","lststatype":"Constructed","mpluto_bbl":"4119880033","name":null,"shape_area":"1375.27323008172","shape_len":"159.1112668769"},"type":"Feature"}}


        @Override
        public Polygon map(ObjectNode jsonObj) throws Exception {

            //{"geometry": {"coordinates": [[[[-73.980455, 40.661994], [-73.980542, 40.661889], [-73.980559, 40.661897], [-73.98057, 40.661885], [-73.980611, 40.661904], [-73.9806, 40.661917], [-73.980513, 40.662022], [-73.980455, 40.661994]]]], "type": "MultiPolygon"}, "properties": {"base_bbl": "3011030028", "bin": "3026604", "cnstrct_yr": "1892", "doitt_id": "33583", "feat_code": "2100", "geomsource": "Photogramm", "groundelev": "153", "heightroof": "31.65", "lstmoddate": "2020-01-28T00:00:00.000Z", "lststatype": "Constructed", "mpluto_bbl": "3011030028", "name": null, "shape_area": "926.10935740605", "shape_len": "139.11922551796"}, "type": "Feature"}

            String json = jsonObj.get("value").toString();
            Geometry geometry;
            try {
                geometry = readGeoJSON(json);
            }
            catch (Exception e) {
                // "type" が無いStringの場合はGeometryを抽出する
                String jsonGeometry = jsonObj.get("value").get("geometry").toString();
                geometry = readGeoJSON(jsonGeometry);
            }

            Polygon spatialPolygon;
            if (geometry.getGeometryType().equalsIgnoreCase("MultiPolygon")) {
                List<List<List<Coordinate>>> listCoordinate = convertMultiCoordinates(
                        json, '[', ']', "],", ",", 4);
                spatialPolygon = new MultiPolygon(listCoordinate, uGrid);
            }
            else {
                List<List<Coordinate>> listCoordinate = convertCoordinates(
                        json, '[', ']', "],", ",", 3);
                spatialPolygon = new Polygon(listCoordinate, uGrid);
            }
            return spatialPolygon;
        }
    }

    public static class GeoJSONToTSpatialPolygon extends RichMapFunction<ObjectNode, Polygon> {

        UniformGrid uGrid;
        DateFormat dateFormat;
        String propertyTimeStamp;
        String propertyObjID;

        //ctor
        public  GeoJSONToTSpatialPolygon() {};
        public  GeoJSONToTSpatialPolygon(UniformGrid uGrid, DateFormat dateFormat, String propertyTimeStamp, String propertyObjID)
        {
            this.uGrid = uGrid;
            this.dateFormat = dateFormat;
            this.propertyTimeStamp = propertyTimeStamp;
            this.propertyObjID = propertyObjID;
        }


        @Override
        public Polygon map(ObjectNode jsonObj) throws Exception {

            String json = jsonObj.get("value").toString();
            Geometry geometry;
            try {
                geometry = readGeoJSON(jsonObj.get("value").toString());
            }
            catch (Exception e) {
                // "type" が無いStringの場合はGeometryを抽出する
                String jsonGeometry = jsonObj.get("value").get("geometry").toString();
                geometry = readGeoJSON(jsonGeometry);
            }

            JsonNode nodeProperties = jsonObj.get("value").get("properties");
            String oId = null;
            long time = 0;
            if (nodeProperties != null) {
                JsonNode nodeTime = jsonObj.get("value").get("properties").get(propertyTimeStamp);
                try {
                    if (nodeTime != null && dateFormat != null) {
                        time = dateFormat.parse(nodeTime.textValue()).getTime();
                    }
                }
                catch (ParseException e) {}

                JsonNode nodeOId = jsonObj.get("value").get("properties").get(propertyObjID);

                if (nodeOId != null) {
                    try {
                        //oId = nodeOId.textValue();
                        oId = nodeOId.toString().replaceAll("\\\"", "");
                    }
                    catch (NumberFormatException e) {}
                }
            }

            Polygon spatialPolygon;
            if (geometry.getGeometryType().equalsIgnoreCase("MultiPolygon")) {
                List<List<List<Coordinate>>> listCoodinate = convertMultiCoordinates(
                        json, '[', ']', "],", ",", 4);
                if (time != 0) {
                    //TODO: Fix timestamp to original timestamp 修正时间戳到原始时间戳
                    spatialPolygon = new MultiPolygon(listCoodinate, oId, time, uGrid);
                    //spatialPolygon = new MultiPolygon(listCoodinate, oId, System.currentTimeMillis(), uGrid);
                    //System.out.println("time " + time + spatialPolygon);
                }
                else {
                    spatialPolygon = new MultiPolygon(listCoodinate, oId, 0, uGrid);
                }
            }
            else {
                List<List<Coordinate>> listCoodinate = convertCoordinates(
                        json, '[', ']', "],", ",", 3);
                if (time != 0) {
                    spatialPolygon = new Polygon(oId, listCoodinate, time, uGrid);
                    //spatialPolygon = new Polygon(oId, listCoodinate, System.currentTimeMillis(), uGrid);
                    //System.out.println("print " + spatialPolygon);
                }
                else {
                    spatialPolygon = new Polygon(oId, listCoodinate, 0, uGrid);
                }
            }
            return spatialPolygon;
        }
    }
```

```java
    // Assuming that csv string contains longitude and latitude at positions 0 and 1, respectively
    public static class CSVToSpatialPolygon extends RichMapFunction<ObjectNode, Polygon> {

        UniformGrid uGrid;

        //ctor
        public  CSVToSpatialPolygon() {};
        public  CSVToSpatialPolygon(UniformGrid uGrid)
        {
            this.uGrid = uGrid;
        };

        @Override
        public Polygon map(ObjectNode strTuple) throws Exception {

            List<String> strArrayList = Arrays.asList(strTuple.toString().split("\\s*,\\s*"));
            //Polygon spatialPolygon = new Point(Double.parseDouble(strArrayList.get(0)), Double.parseDouble(strArrayList.get(1)), uGrid);
            //return spatialPolygon;

            return null;
        }
    }
```

```java
    // Assuming that csv string contains longitude and latitude at positions 0 and 1, respectively
    public static class CSVTSVToSpatialPolygon extends RichMapFunction<String, Polygon> {

        UniformGrid uGrid;

        //ctor
        public CSVTSVToSpatialPolygon() {};
        public CSVTSVToSpatialPolygon(UniformGrid uGrid)
        {
            this.uGrid = uGrid;
        };

        @Override
        public Polygon map(String str) throws Exception {
            // {"key":1,"value":"MULTIPOLYGON (((-74.15010482037168 40.62183511874645, -74.15016701565006 40.62177739783489, -74.1502116609276 40.62180593197037, -74.15015270982748 40.62185788893257, -74.15014748371995 40.62186259918266, -74.1501238625006 40.6218473986088, -74.150107093191 40.62186251414858, -74.15008804280825 40.621850243299434, -74.15010482037168 40.62183511874645)))"}
            // value = "MULTIPOLYGON (((-74.15010482037168 40.62183511874645, -74.15016701565006 40.62177739783489, -74.1502116609276 40.62180593197037, -74.15015270982748 40.62185788893257, -74.15014748371995 40.62186259918266, -74.1501238625006 40.6218473986088, -74.150107093191 40.62186251414858, -74.15008804280825 40.621850243299434, -74.15010482037168 40.62183511874645)))"

            Polygon spatialPolygon;
            if (str.contains("MULTIPOLYGON")) {
                List<List<List<Coordinate>>> listCoordinate = convertMultiCoordinates(
                        str, '(', ')', ",", " ", 3);
                spatialPolygon = new MultiPolygon(listCoordinate, uGrid);
            }
            else {
                List<List<Coordinate>> listCoordinate = convertCoordinates(
                        str, '(', ')', ",", " ", 2);
                spatialPolygon = new Polygon(listCoordinate, uGrid);
            }
            return spatialPolygon;
        }
    }
```

```java
    public static class GeoJSONToSpatialLineString extends RichMapFunction<ObjectNode, LineString> {

        UniformGrid uGrid;

        //ctor
        public  GeoJSONToSpatialLineString() {};
        public  GeoJSONToSpatialLineString(UniformGrid uGrid)
        {
            this.uGrid = uGrid;
        };

        //58> {"key":368387,"value":{"geometry":{"coordinates":[[[[-73.797919,40.681402],[-73.797885,40.681331],[-73.798032,40.681289],[-73.798048,40.681285],[-73.798067,40.681324],[-73.798075,40.681322],[-73.798092,40.681357],[-73.79806,40.681366],[-73.798058,40.681363],[-73.79801,40.681376],[-73.797919,40.681402]]]],"type":"LineString"},"properties":{"base_bbl":"4119880033","bin":"4259746","cnstrct_yr":"1955","doitt_id":"527355","feat_code":"2100","geomsource":"Photogramm","groundelev":"26","heightroof":"26.82","lstmoddate":"2017-08-22T00:00:00.000Z","lststatype":"Constructed","mpluto_bbl":"4119880033","name":null,"shape_area":"1375.27323008172","shape_len":"159.1112668769"},"type":"Feature"}}


        @Override
        public LineString map(ObjectNode jsonObj) throws Exception {
            //{"geometry": {"coordinates": [[[[-73.980455, 40.661994], [-73.980542, 40.661889], [-73.980559, 40.661897], [-73.98057, 40.661885], [-73.980611, 40.661904], [-73.9806, 40.661917], [-73.980513, 40.662022], [-73.980455, 40.661994]]]], "type": "LineString"}, "properties": {"base_bbl": "3011030028", "bin": "3026604", "cnstrct_yr": "1892", "doitt_id": "33583", "feat_code": "2100", "geomsource": "Photogramm", "groundelev": "153", "heightroof": "31.65", "lstmoddate": "2020-01-28T00:00:00.000Z", "lststatype": "Constructed", "mpluto_bbl": "3011030028", "name": null, "shape_area": "926.10935740605", "shape_len": "139.11922551796"}, "type": "Feature"}

            String json = jsonObj.get("value").toString();
            Geometry geometry;
            try {
                geometry = readGeoJSON(json);
            }
            catch (Exception e) {
                // "type" が無いStringの場合はGeometryを抽出する
                String jsonGeometry = jsonObj.get("value").get("geometry").toString();
                geometry = readGeoJSON(jsonGeometry);
            }

            LineString spatialLineString;
            if (geometry.getGeometryType().equalsIgnoreCase("MultiLineString")) {
                List<List<Coordinate>> lists = convertCoordinates(
                        json, '[', ']', "],", ",", 3);
                spatialLineString = new MultiLineString(null, lists, uGrid);
            }
            else {
                List<List<Coordinate>> parent = convertCoordinates(
                        json, '[', ']', "],", ",", 2);
                spatialLineString = new LineString(null, parent.get(0), uGrid);
            }
            return spatialLineString;
        }
    }

    public static class GeoJSONToTSpatialLineString extends RichMapFunction<ObjectNode, LineString> {

        UniformGrid uGrid;
        DateFormat dateFormat;
        String propertyTimeStamp;
        String propertyObjID;

        //ctor
        public  GeoJSONToTSpatialLineString() {};
        public  GeoJSONToTSpatialLineString(UniformGrid uGrid, DateFormat dateFormat, String propertyTimeStamp, String propertyObjID)
        {
            this.uGrid = uGrid;
            this.dateFormat = dateFormat;
            this.propertyTimeStamp = propertyTimeStamp;
            this.propertyObjID = propertyObjID;
        };

        @Override
        public LineString map(ObjectNode jsonObj) throws Exception {

            String json = jsonObj.get("value").toString();
            Geometry geometry;
            try {
                geometry = readGeoJSON(jsonObj.get("value").toString());
            }
            catch (Exception e) {
                // "type" が無いStringの場合はGeometryを抽出する
                String jsonGeometry = jsonObj.get("value").get("geometry").toString();
                geometry = readGeoJSON(jsonGeometry);
            }

            JsonNode nodeProperties = jsonObj.get("value").get("properties");
            String strOId = null;
            long time = 0;
            if (nodeProperties != null) {
                JsonNode nodeTime = jsonObj.get("value").get("properties").get(propertyTimeStamp);
                try {
                    if (nodeTime != null && dateFormat != null) {
                        time = dateFormat.parse(nodeTime.textValue()).getTime();
                    }
                }
                catch (ParseException e) {}
                JsonNode nodeOId = jsonObj.get("value").get("properties").get(propertyObjID);
                if (nodeOId != null) {
                    //strOId = nodeOId.textValue();
                    strOId = nodeOId.toString().replaceAll("\\\"", "");
                }
            }
            LineString spatialLineString;
            if (geometry.getGeometryType().equalsIgnoreCase("MultiLineString")) {
                List<List<Coordinate>> lists = convertCoordinates(
                        json, '[', ']', "],", ",", 3);
                if (time != 0) {
                    spatialLineString = new MultiLineString(strOId, lists, time, uGrid);
                }
                else {
                    spatialLineString = new MultiLineString(strOId, lists, uGrid);
                }
            }
            else {
                List<List<Coordinate>> parent = convertCoordinates(
                        json, '[', ']', "],", ",", 2);
                if (time != 0) {
                    //spatialLineString = new LineString(strOId, parent.get(0), time, uGrid);
                    //TODO: Fix timestamp to original timestamp 修正时间戳到原始时间戳
                    spatialLineString = new LineString(strOId, parent.get(0), time, uGrid);
                    //spatialLineString = new LineString(strOId, parent.get(0), System.currentTimeMillis(), uGrid);

                }
                else {
                    spatialLineString = new LineString(strOId, parent.get(0), uGrid);
                }
            }
            return spatialLineString;
        }
    }
```

```java
    public static class GeoJSONToSpatialGeometryCollection extends RichMapFunction<ObjectNode, GeometryCollection> {

        UniformGrid uGrid;

        //ctor
        public  GeoJSONToSpatialGeometryCollection() {};
        public  GeoJSONToSpatialGeometryCollection(UniformGrid uGrid)
        {
            this.uGrid = uGrid;
        };

        @Override
        public GeometryCollection map(ObjectNode jsonObj) throws Exception {
            //{"geometry": {"coordinates": [[[[-73.980455, 40.661994], [-73.980542, 40.661889], [-73.980559, 40.661897], [-73.98057, 40.661885], [-73.980611, 40.661904], [-73.9806, 40.661917], [-73.980513, 40.662022], [-73.980455, 40.661994]]]], "type": "LineString"}, "properties": {"base_bbl": "3011030028", "bin": "3026604", "cnstrct_yr": "1892", "doitt_id": "33583", "feat_code": "2100", "geomsource": "Photogramm", "groundelev": "153", "heightroof": "31.65", "lstmoddate": "2020-01-28T00:00:00.000Z", "lststatype": "Constructed", "mpluto_bbl": "3011030028", "name": null, "shape_area": "926.10935740605", "shape_len": "139.11922551796"}, "type": "Feature"}

            String json = jsonObj.get("value").toString();
            Geometry geometry;
            try {
                geometry = readGeoJSON(json);
            }
            catch (Exception e) {
                // "type" が無いStringの場合はGeometryを抽出する
                String jsonGeometry = jsonObj.get("value").get("geometry").toString();
                geometry = readGeoJSON(jsonGeometry);
            }
            List<SpatialObject> listObj = new ArrayList<SpatialObject>();
            int num = geometry.getNumGeometries();
            for (int i = 0; i < num; i++) {
                Geometry geometryN = geometry.getGeometryN(i);
                json = json.substring(json.indexOf(geometryN.getGeometryType()));
                if (geometryN.getGeometryType().equalsIgnoreCase("Point")) {
                    listObj.add(new Point(geometryN.getCoordinate().x, geometryN.getCoordinate().y, uGrid));
                }
                else if (geometryN.getGeometryType().equalsIgnoreCase("MultiPoint")) {
                    List<List<Coordinate>> parent = convertCoordinates(
                            json, '[', ']', "],", ",", 2);
                    listObj.add(new MultiPoint(null, parent.get(0), uGrid));
                }
                else if (geometryN.getGeometryType().equalsIgnoreCase("MultiPolygon")) {
                        List<List<List<Coordinate>>> listCoordinate = convertMultiCoordinates(
                                json, '[', ']', "],", ",", 4);
                        listObj.add(new MultiPolygon(listCoordinate, uGrid));
                }
                else if (geometryN.getGeometryType().equalsIgnoreCase("Polygon")) {
                    List<List<Coordinate>> listCoordinate = convertCoordinates(
                            json, '[', ']', "],", ",", 3);
                    listObj.add(new Polygon(listCoordinate, uGrid));
                }
                else if (geometryN.getGeometryType().equalsIgnoreCase("MultiLineString")) {
                    List<List<Coordinate>> listCoordinate = convertCoordinates(
                            json, '[', ']', "],", ",", 3);
                    listObj.add(new MultiLineString(null, listCoordinate, uGrid));
                }
                else if (geometryN.getGeometryType().equalsIgnoreCase("LineString")) {
                    List<List<Coordinate>> parent = convertCoordinates(
                            json, '[', ']', "],", ",", 2);
                    listObj.add(new LineString(null, parent.get(0), uGrid));
                }
            }
            GeometryCollection spatialGeometryCollection = new GeometryCollection(listObj, null);
            return spatialGeometryCollection;
        }
    }

    public static class GeoJSONToTSpatialGeometryCollection extends RichMapFunction<ObjectNode, GeometryCollection> {

        UniformGrid uGrid;
        DateFormat dateFormat;
        String propertyTimeStamp;
        String propertyObjID;

        //ctor
        public GeoJSONToTSpatialGeometryCollection() {
        }

        public GeoJSONToTSpatialGeometryCollection(UniformGrid uGrid, DateFormat dateFormat, String propertyTimeStamp, String propertyObjID) {

            this.uGrid = uGrid;
            this.dateFormat = dateFormat;
            this.propertyTimeStamp = propertyTimeStamp;
            this.propertyObjID = propertyObjID;
        }

        @Override
        public GeometryCollection map(ObjectNode jsonObj) throws Exception {

            Geometry geometry;
            String json = jsonObj.get("value").toString();
            try {
                geometry = readGeoJSON(json);
            } catch (Exception e) {
                // "type" が無いStringの場合はGeometryを抽出する
                String jsonGeometry = jsonObj.get("value").get("geometry").toString();
                geometry = readGeoJSON(jsonGeometry);
            }
            JsonNode nodeProperties = jsonObj.get("value").get("properties");
            String strOId = null;
            long time = 0;
            if (nodeProperties != null) {
                JsonNode nodeTime = jsonObj.get("value").get("properties").get(propertyTimeStamp);
                try {
                    if (nodeTime != null && dateFormat != null) {
                        time = dateFormat.parse(nodeTime.textValue()).getTime();
                    }
                } catch (ParseException e) {
                }
                JsonNode nodeOId = jsonObj.get("value").get("properties").get(propertyObjID);
                if (nodeOId != null) {
                    //strOId = nodeOId.textValue();
                    strOId = nodeOId.toString().replaceAll("\\\"", "");
                }
            }
            List<SpatialObject> listObj = new ArrayList<SpatialObject>();
            int num = geometry.getNumGeometries();
            for (int i = 0; i < num; i++) {
                Geometry geometryN = geometry.getGeometryN(i);
                json = json.substring(1);
                json = json.substring(json.indexOf("coordinates"));
                if (geometryN.getGeometryType().equalsIgnoreCase("Point")) {
                    listObj.add(new Point(geometryN.getCoordinate().x, geometryN.getCoordinate().y, uGrid));
                }
                else if (geometryN.getGeometryType().equalsIgnoreCase("MultiPoint")) {
                    List<List<Coordinate>> parent = convertCoordinates(
                            json, '[', ']', "],", ",", 2);
                    listObj.add(new MultiPoint(null, parent.get(0), uGrid));
                }
                else if (geometryN.getGeometryType().equalsIgnoreCase("MultiPolygon")) {
                    List<List<List<Coordinate>>> listCoordinate = convertMultiCoordinates(
                            json, '[', ']', "],", ",", 4);
                    listObj.add(new MultiPolygon(listCoordinate, uGrid));
                }
                else if (geometryN.getGeometryType().equalsIgnoreCase("Polygon")) {
                    List<List<Coordinate>> listCoordinate = convertCoordinates(
                            json, '[', ']', "],", ",", 3);
                    listObj.add(new Polygon(listCoordinate, uGrid));
                }
                else if (geometryN.getGeometryType().equalsIgnoreCase("MultiLineString")) {
                    List<List<Coordinate>> listCoordinate = convertCoordinates(
                            json, '[', ']', "],", ",", 3);
                    listObj.add(new MultiLineString(null, listCoordinate, uGrid));
                }
                else if (geometryN.getGeometryType().equalsIgnoreCase("LineString")) {
                    List<List<Coordinate>> parent = convertCoordinates(
                            json, '[', ']', "],", ",", 2);
                    listObj.add(new LineString(null, parent.get(0), uGrid));
                }
            }
            GeometryCollection spatialGeometryCollection = new GeometryCollection(listObj, strOId, time);
            return spatialGeometryCollection;
        }
    }
```

```java
    public static class GeoJSONToSpatialMultiPoint extends RichMapFunction<ObjectNode, MultiPoint> {

        UniformGrid uGrid;

        //ctor
        public  GeoJSONToSpatialMultiPoint() {};
        public  GeoJSONToSpatialMultiPoint(UniformGrid uGrid)
        {
            this.uGrid = uGrid;
        };

        @Override
        public MultiPoint map(ObjectNode jsonObj) throws Exception {

            String json = jsonObj.get("value").toString();
            MultiPoint spatialMultiPoint;
                List<List<Coordinate>> parent = convertCoordinates(
                        json, '[', ']', "],", ",", 2);
            spatialMultiPoint = new MultiPoint(null, parent.get(0), uGrid);
            return spatialMultiPoint;
        }
    }

    public static class GeoJSONToTSpatialMultiPoint extends RichMapFunction<ObjectNode, MultiPoint> {

        UniformGrid uGrid;
        DateFormat dateFormat;
        String propertyTimeStamp;
        String propertyObjID;

        public  GeoJSONToTSpatialMultiPoint() {};
        public  GeoJSONToTSpatialMultiPoint(UniformGrid uGrid, DateFormat dateFormat, String propertyTimeStamp, String propertyObjID)
        {
            this.uGrid = uGrid;
            this.dateFormat = dateFormat;
            this.propertyTimeStamp = propertyTimeStamp;
            this.propertyObjID = propertyObjID;
        }

        @Override
        public MultiPoint map(ObjectNode jsonObj) throws Exception {

            String json = jsonObj.get("value").toString();
            JsonNode nodeProperties = jsonObj.get("value").get("properties");
            String strOId = null;
            long time = 0;
            if (nodeProperties != null) {
                JsonNode nodeTime = jsonObj.get("value").get("properties").get(propertyTimeStamp);
                try {
                    if (nodeTime != null && dateFormat != null) {
                        time = dateFormat.parse(nodeTime.textValue()).getTime();
                    }
                }
                catch (ParseException e) {}
                JsonNode nodeOId = jsonObj.get("value").get("properties").get(propertyObjID);
                if (nodeOId != null) {
                    //strOId = nodeOId.textValue();
                    strOId = nodeOId.toString().replaceAll("\\\"", "");
                }
            }
            MultiPoint spatialMultiPoint;
            List<List<Coordinate>> parent = convertCoordinates(
                    json, '[', ']', "],", ",", 2);
            spatialMultiPoint = new MultiPoint(strOId, parent.get(0), time, uGrid);
            return spatialMultiPoint;
        }
    }
```






# UDF
您可以在sql中使用Flink scala UDF或Python UDF。批处理和流式sql的UDF是相同的。这里有两个例子。

```py
%flink

class ScalaUpper extends ScalarFunction {
  def eval(str: String) = str.toUpperCase
}
btenv.registerFunction("scala_upper", new ScalaUpper())
```

```py
%flink

class UDF的默认名称 extends ScalarFunction {
        def eval(str: String) = str.toUpperCase
    }
btenv.createTemporarySystemFunction("UDF的默认名称",new MyScalaUdfDemo)
```

```py
%flink.pyflink

class PythonUpper(ScalarFunction):
  def eval(self, s):
    return s.upper()

bt_env.register_function("python_upper", udf(PythonUpper(), DataTypes.STRING(), DataTypes.STRING()))
```

```py
%flink.pyflink

class UDF的默认名称(ScalarFunction):
  def eval(self, s):
    return s.upper()

bt_env.register_function("python_upper", udf(UDF的默认名称(), DataTypes.STRING(), DataTypes.STRING()))
```

# 去bug之“目前还不会”
导入错误: cannot import name 'Json' from 'pyflink.table.descriptors' 

```java
public class Json extends FormatDescriptor {
​
    private Boolean failOnMissingField;
    private Boolean deriveSchema;
    private String jsonSchema;
    private String schema;
​
    public Json() {
        super(FORMAT_TYPE_VALUE, 1);
    }
​
    public Json failOnMissingField(boolean failOnMissingField) {
        this.failOnMissingField = failOnMissingField;
        return this;
    }
​
    public Json jsonSchema(String jsonSchema) {
        Preconditions.checkNotNull(jsonSchema);
        this.jsonSchema = jsonSchema;
        this.schema = null;
        this.deriveSchema = null;
        return this;
    }
​
    public Json schema(TypeInformation<Row> schemaType) {
        Preconditions.checkNotNull(schemaType);
        this.schema = TypeStringUtils.writeTypeInfo(schemaType);
        this.jsonSchema = null;
        this.deriveSchema = null;
        return this;
    }
​
    public Json deriveSchema() {
        this.deriveSchema = true;
        this.schema = null;
        this.jsonSchema = null;
        return this;
    }
​
    @Override
    protected Map<String, String> toFormatProperties() {
        final DescriptorProperties properties = new DescriptorProperties();
​
        if (deriveSchema != null) {
            properties.putBoolean(FORMAT_DERIVE_SCHEMA, deriveSchema);
        }
​
        if (jsonSchema != null) {
            properties.putString(FORMAT_JSON_SCHEMA, jsonSchema);
        }
​
        if (schema != null) {
            properties.putString(FORMAT_SCHEMA, schema);
        }
​
        if (failOnMissingField != null) {
            properties.putBoolean(FORMAT_FAIL_ON_MISSING_FIELD, failOnMissingField);
        }
​
        return properties.asMap();
    }
}
```

```py
import sys
from abc import ABCMeta
from collections import OrderedDict

from py4j.java_gateway import get_method
from typing import Dict, Union

from pyflink.java_gateway import get_gateway
from pyflink.table.table_schema import TableSchema
from pyflink.table.types import _to_java_type, DataType

__all__ = [
    'Rowtime',
    'Schema'
]


class Descriptor(object, metaclass=ABCMeta):
    """
    Base class of the descriptors that adds a set of string-based, normalized properties for
    describing DDL information.
    Typical characteristics of a descriptor are:
    - descriptors have a default constructor
    - descriptors themselves contain very little logic
    - corresponding validators validate the correctness (goal: have a single point of validation)
    A descriptor is similar to a builder in a builder pattern, thus, mutable for building
    properties.
    """

    def __init__(self, j_descriptor):
        self._j_descriptor = j_descriptor

    def to_properties(self) -> Dict:
        """
        Converts this descriptor into a dict of properties.
        :return: Dict object contains all of current properties.
        """
        return dict(self._j_descriptor.toProperties())


class Rowtime(Descriptor):
    """
    Rowtime descriptor for describing an event time attribute in the schema.
    """

    def __init__(self):
        gateway = get_gateway()
        self._j_rowtime = gateway.jvm.Rowtime()
        super(Rowtime, self).__init__(self._j_rowtime)

    def timestamps_from_field(self, field_name: str):
        """
        Sets a built-in timestamp extractor that converts an existing LONG or TIMESTAMP field into
        the rowtime attribute.
        :param field_name: The field to convert into a rowtime attribute.
        :return: This rowtime descriptor.
        """
        self._j_rowtime = self._j_rowtime.timestampsFromField(field_name)
        return self

    def timestamps_from_source(self) -> 'Rowtime':
        """
        Sets a built-in timestamp extractor that converts the assigned timestamps from a DataStream
        API record into the rowtime attribute and thus preserves the assigned timestamps from the
        source.
        .. note::
            This extractor only works in streaming environments.
        :return: This rowtime descriptor.
        """
        self._j_rowtime = self._j_rowtime.timestampsFromSource()
        return self

    def timestamps_from_extractor(self, extractor: str) -> 'Rowtime':
        """
        Sets a custom timestamp extractor to be used for the rowtime attribute.
        :param extractor: The java fully-qualified class name of the TimestampExtractor to extract
                          the rowtime attribute from the physical type. The TimestampExtractor must
                          have a public no-argument constructor and can be founded by
                          in current Java classloader.
        :return: This rowtime descriptor.
        """
        gateway = get_gateway()
        self._j_rowtime = self._j_rowtime.timestampsFromExtractor(
            gateway.jvm.Thread.currentThread().getContextClassLoader().loadClass(extractor)
                   .newInstance())
        return self

    def watermarks_periodic_ascending(self) -> 'Rowtime':
        """
        Sets a built-in watermark strategy for ascending rowtime attributes.
        Emits a watermark of the maximum observed timestamp so far minus 1. Rows that have a
        timestamp equal to the max timestamp are not late.
        :return: This rowtime descriptor.
        """
        self._j_rowtime = self._j_rowtime.watermarksPeriodicAscending()
        return self

    def watermarks_periodic_bounded(self, delay: int) -> 'Rowtime':
        """
        Sets a built-in watermark strategy for rowtime attributes which are out-of-order by a
        bounded time interval.
        Emits watermarks which are the maximum observed timestamp minus the specified delay.
        :param delay: Delay in milliseconds.
        :return: This rowtime descriptor.
        """
        self._j_rowtime = self._j_rowtime.watermarksPeriodicBounded(delay)
        return self

    def watermarks_from_source(self) -> 'Rowtime':
        """
        Sets a built-in watermark strategy which indicates the watermarks should be preserved from
        the underlying DataStream API and thus preserves the assigned watermarks from the source.
        :return: This rowtime descriptor.
        """
        self._j_rowtime = self._j_rowtime.watermarksFromSource()
        return self

    def watermarks_from_strategy(self, strategy: str) -> 'Rowtime':
        """
        Sets a custom watermark strategy to be used for the rowtime attribute.
        :param strategy: The java fully-qualified class name of the WatermarkStrategy. The
                         WatermarkStrategy must have a public no-argument constructor and can be
                         founded by in current Java classloader.
        :return: This rowtime descriptor.
        """
        gateway = get_gateway()
        self._j_rowtime = self._j_rowtime.watermarksFromStrategy(
            gateway.jvm.Thread.currentThread().getContextClassLoader().loadClass(strategy)
                   .newInstance())
        return self


class Schema(Descriptor):
    """
    Describes a schema of a table.
    .. note::
        Field names are matched by the exact name by default (case sensitive).
    """

    def __init__(self, schema=None, fields=None, rowtime=None):
        """
        Constructor of Schema descriptor.
        :param schema: The :class:`TableSchema` object.
        :param fields: Dict of fields with the field name and the data type or type string stored.
        :param rowtime: A :class:`RowTime` that Specifies the previously defined field as an
                        event-time attribute.
        """
        gateway = get_gateway()
        self._j_schema = gateway.jvm.org.apache.flink.table.descriptors.Schema()
        super(Schema, self).__init__(self._j_schema)

        if schema is not None:
            self.schema(schema)

        if fields is not None:
            self.fields(fields)

        if rowtime is not None:
            self.rowtime(rowtime)

    def schema(self, table_schema: 'TableSchema') -> 'Schema':
        """
        Sets the schema with field names and the types. Required.
        This method overwrites existing fields added with
        :func:`~pyflink.table.descriptors.Schema.field`.
        :param table_schema: The :class:`TableSchema` object.
        :return: This schema object.
        """
        self._j_schema = self._j_schema.schema(table_schema._j_table_schema)
        return self

    def field(self, field_name: str, field_type: Union[DataType, str]) -> 'Schema':
        """
        Adds a field with the field name and the data type or type string. Required.
        This method can be called multiple times. The call order of this method defines
        also the order of the fields in a row. Here is a document that introduces the type strings:
        https://nightlies.apache.org/flink/flink-docs-stable/dev/table/connect.html#type-strings
        :param field_name: The field name.
        :param field_type: The data type or type string of the field.
        :return: This schema object.
        """
        if isinstance(field_type, str):
            self._j_schema = self._j_schema.field(field_name, field_type)
        else:
            self._j_schema = self._j_schema.field(field_name, _to_java_type(field_type))
        return self

    def fields(self, fields: Dict[str, Union[DataType, str]]) -> 'Schema':
        """
        Adds a set of fields with the field name and the data type or type string stored in a
        list.
        :param fields: Dict of fields with the field name and the data type or type string
                       stored.
                       E.g, [('int_field', DataTypes.INT()), ('string_field', DataTypes.STRING())].
        :return: This schema object.
        .. versionadded:: 1.11.0
        """
        if sys.version_info[:2] <= (3, 5) and not isinstance(fields, OrderedDict):
            raise TypeError("Must use OrderedDict type in python3.5 or older version to key the "
                            "schema in insert order.")
        elif sys.version_info[:2] > (3, 5) and not isinstance(fields, (OrderedDict, dict)):
            raise TypeError("fields must be stored in a dict or OrderedDict")

        for field_name, field_type in fields.items():
            self.field(field_name=field_name, field_type=field_type)
        return self

    def from_origin_field(self, origin_field_name: str) -> 'Schema':
        """
        Specifies the origin of the previously defined field. The origin field is defined by a
        connector or format.
        E.g. field("myString", Types.STRING).from_origin_field("CSV_MY_STRING")
        .. note::
            Field names are matched by the exact name by default (case sensitive).
        :param origin_field_name: The origin field name.
        :return: This schema object.
        """
        self._j_schema = get_method(self._j_schema, "from")(origin_field_name)
        return self

    def proctime(self) -> 'Schema':
        """
        Specifies the previously defined field as a processing-time attribute.
        E.g. field("proctime", Types.SQL_TIMESTAMP_LTZ).proctime()
        :return: This schema object.
        """
        self._j_schema = self._j_schema.proctime()
        return self

    def rowtime(self, rowtime: Rowtime) -> 'Schema':
        """
        Specifies the previously defined field as an event-time attribute.
        E.g. field("rowtime", Types.SQL_TIMESTAMP).rowtime(...)
        :param rowtime: A :class:`RowTime`.
        :return: This schema object.
        """
        self._j_schema = self._j_schema.rowtime(rowtime._j_rowtime)
        return self
```