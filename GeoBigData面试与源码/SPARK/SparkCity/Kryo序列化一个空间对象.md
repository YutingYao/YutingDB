## 优化方案

JTS(Java Topology Suite)空间拓扑工具包提供了空间对象与WKB（Well-known Binary）格式之间的转换，WKB是OGC标准定义的空间对象的二进制表示格式，该格式用最少的字节数表示一个空间对象的完整信息。可以将空间对象与WKB之间的转换过程对应到空间对象的序列化和反序列化过程，并将通过Kryo序列化器的形式注册到Spark的序列化过程中，以优化基于Spark的空间分析性能。

WKB的转换利用JTS提供的`WKBReader`和`WKBWriter`两个类来实现，这两个类是`非线程安全`的，

为了在Spark的并发场景中重复利用，一般使用`ThreadLocal`对其进行缓存，Scala代码实现如下。

```scala
object WKBUtils {
  private val readerPool = newThreadLocal[WKBReader]{
    override def initialValue:WKBReader =new WKBReader
  }
  private val writerPool = newThreadLocal[WKBWriter]{
    override def initialValue:WKBWriter =new WKBWriter
  }
  def read(bytes: Array[Byte]): Geometry= readerPool.get.read(bytes)
  def write(geom: Geometry): Array[Byte]= writerPool.get.write(geom)
}
```

为空间对象创建`Kryo序列化器`。序列化的实现通过调用`WKBUtils`的`read`和`write`方法来完成，Scala代码实现如下。

```scala
class GeometrySerializer extends Serializer[Geometry] {
override def write(kryo: Kryo, output:Output, geom: Geometry): Unit= {
val spatialBytes: Array[Byte] =WKBUtils.write(geom)
output.writeInt(spatialBytes.length)
output.writeBytes(spatialBytes)
}
override def read(kryo: Kryo, input:Input, clazz: Class[Geometry]):Geometry= {
   val length = input.readInt()
   WKBUtils.read(input.readBytes(length))
}
}
```

向Spark注册创建好的`Kryo序列化器`。该过程通过继承Spark的`KryoRegistrator`类实现，Scala代码实现如下。

因为`Point、LineString、Polygon、MultiPoint、MultiLineString、MultiPolygon`这六类空间数据类型都是Geometry的子类，都可以通过`WKB`的格式表示，因此共用同一个`GeometrySerializer`对象。

```scala
class GeometryKryoRegistrator extends KryoRegistrator {
  override def registerClasses(kryo:Kryo): Unit = {
    val geometrySerializer = new GeometrySerializer

    kryo.register(classOf[Point],geometrySerializer)
    kryo.register(classOf[LineString],geometrySerializer)
    kryo.register(classOf[Polygon],geometrySerializer)
    kryo.register(classOf[MultiPoint],geometrySerializer)
    kryo.register(classOf[MultiLineString],geometrySerializer)
    kryo.register(classOf[MultiPolygon],geometrySerializer)
  }
}
```

将创建好的`序列化注册器`作为`SparkConf`的参数，在创建`SparkContext`的时候传入，便可在Spark的计算中对`空间对象`使用WKB的格式进行`序列化`和`反序列化`。

```scala
val conf= new SparkConf()
 .set("spark.serializer",classOf[KryoSerializer].getName)
 .set("spark.kryo.registrator",classOf[GeometryKryoRegistrator].getName)
val spark = new SparkContext(conf)
```

实验与结论:

对比原始的Kryo序列化器和自定义的Kryo序列化器，以序列化时长和序列化字节数为指标观察其性能差异，实验结果如下表所示：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.641jkn903100.png)

由实验结果可知，原始的`Kryo序列化机`制耗时是自定义序列化器的3倍左右，序列化所得的字节数是自定义序列化器的1.7倍左右。总之，在使用Spark进行空间数据分析时，通过Kryo给Spark指定空间对象的WKB序列化机制可以提高序列化效率，并减少机器之间的数据传输量。