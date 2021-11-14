## 🎈 Flink 1.10 版本

PyFlink: 支持原生`用户自定义函数（UDF）`

让用户在 Table API/SQL 中注册并使用自定义函数（UDF，另 UDTF / UDAF 规划中）

这些数据结构为支持 Pandas 以及今后将 PyFlink 引入到 DataStream API 奠定了基础。

从 Flink 1.10 开始，用户只要执行以下命令就可以轻松地通过 pip 安装 PyFlink：

```s
pip install apache-flink
```

## 🎈 Flink 1.11 版本

之前普通的 Python UDF 每次调用只能处理`一条数据`，而且在 Java 端和 Python 端都需要序列化/反序列化，开销很大。

🥝🥝🥝🥝🥝🥝🥝🥝🥝🥝🥝🥝🥝🥝🥝🥝

PyFlink 的多项性能优化，包括:

- 在`Table & SQL`中`自定义`和使用`向量化 Python UDF`
  
- 优化 `Python UDF` 的性能，对比 1.10.0 `可以提升 30 倍`。
  
- Python UDF 中用户`自定义 metric`，方便`监控和调试` UDF 的执行
  
- 在`Table & SQL`中可以定义和使用 `Python UDTF`
  
- 方便 Python 用户基于 Numpy 和 Pandas 等数据分析领域常用的 Python 库，开发高性能的 Python UDF。
  
- `PyFlink table` 和 `Pandas DataFrame` 之间无缝切换
  
- `SQL DDL/Client` 的集成
  
- 用户只需要在 UDF 修饰中额外增加一个参数 `udf_type=“pandas”` 即可

- 每次调用可以处理 `N 条数据`。

- 数据格式基于 `Apache Arrow`，大大降低了 Java、Python 进程之间的`序列化/反序列化`开销。

## 🎈Flink 1.12 版本

PyFlink 的多项性能优化，包括:

- PyFlink 中添加了对于 `DataStream API` 的支持

- 支持了`无状态类型`的操作（例如 `Map，FlatMap，Filter，KeyBy` 等）

- 将 PyFlink 扩展到了`更复杂的场景`，比如需要对`状态`或者`定时器 timer` 进行`细粒度控制`的场景。

- 现在原生支持将 PyFlink 作业部署到 Kubernetes上。最新的文档中详细描述了如何在 Kubernetes 上启动 `session` 或 `application 集群`。

- 用户自定义`聚合函数 (UDAFs)`。普通的 UDF（`标量函数`）每次只能处理`一行数据`，而 `UDAF（聚合函数）`则可以处理`多行数据`，用于计算`多行数据`的`聚合值`。也可以使用 `Pandas UDAF`，来进行向量化计算（通常来说，比`普通 Python UDAF` 快10倍以上）。注意: 普通 Python UDAF，当前仅支持在 `group aggregations` 以及`流模式`下使用。如果需要在`批模式`或者`窗口聚合`中使用，建议使用 `Pandas UDAF`。

🥝🥝🥝🥝🥝🥝🥝🥝🥝🥝🥝🥝🥝🥝🥝🥝

```py
from pyflink.common.typeinfo import Types
from pyflink.datastream import MapFunction, StreamExecutionEnvironment
class MyMapFunction(MapFunction):
    def map(self, value):
        return value + 1
env = StreamExecutionEnvironment.get_execution_environment()
data_stream = env.from_collection([1, 2, 3, 4, 5], type_info=Types.INT())
mapped_stream = data_stream.map(MyMapFunction(), output_type=Types.INT())
mapped_stream.print()
env.execute("datastream job")
```

## Flink 1.13 版本

在以前的版本中，这些 `udaf函数` 只支持无界的 `Group-by聚合`。

让Python DataStream API和Table API在特性上更接近Java/Scala API。

- PyFlink DataStream API增加了对`用户定义的窗口`的支持。 Flink程序现在可以在`标准窗口定义`之外使用窗口。因为`窗口`是所有处理`无限流`的程序的核心(通过将`无限流`分割成有大小的“桶”)，这大大提高了API的表达能力。PyFlink DataStream API现在也支持`有界流`的批处理执行模式，这是在Flink 1.12中为`Java DataStream API`引入的。批处理执行模式通过利用`有界流`的特性，绕过状态后端和检查点，简化了`有界流`上的操作并提高了程序的性能。

- Python Table API现在支持基于`行`的操作，例如，自定义的`行转换函数`。 这些函数是在内置函数之外对表应用数据转换的一种简单方法。除了`map()`之外，该API还支持`flat_map()`、`aggregate()`、`flat_aggregate()`和其他基于`行`操作的函数。 这使得Python Table API与Java Table API在特性上更相近了。支持用户在Group Windows`自定义聚合函数`。PyFlink的Table API中的组窗口现在同时支持一般的`Python用户定义聚合函数(udaf)`和`Pandas udaf`。 这些功能对于许多分析和ML训练程序是至关重要的。

```py
@udf(result_type=DataTypes.ROW(
  [DataTypes.FIELD("c1", DataTypes.BIGINT()),
   DataTypes.FIELD("c2", DataTypes.STRING())]))
def increment_column(r: Row) -> Row:
  return Row(r[0] + 1, r[1])

table = ...  # type: Table
mapped_result = table.map(increment_column)
```
