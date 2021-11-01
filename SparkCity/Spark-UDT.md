## 从类 org.apache.spark.sql.types.UserDefinedType 继承的方法

defaultSize

jsonValue

## 从类 org.apache.spark.sql.types.DataType 继承的方法

equalsIgnoreCompatibleNullability

equalsIgnoreNullability
 
fromCaseClassString

fromJson

isPrimitive

json

prettyJson

sameType

simpleString

typeName

unapply

## Class VectorUDT

```scala
Object
1. 从类 org.apache.spark.sql.types.UserDefinedType 继承的方法
2. 从类 org.apache.spark.sql.types.DataType 继承的方法
3. Class VectorUDT
4. :: DeveloperApi ::
```

所有实现的接口：

```scala
java.io.Serializable
```

```scala
public class VectorUDT
extends UserDefinedType<Vector>
```

## :: DeveloperApi ::

Vector 的用户定义类型，允许通过 DataFrame 与 SQL 轻松交互.

注意：这目前是私有的[spark]，但一旦稳定下来就会公开。


构造函数-构造函数和描述:

```scala
VectorUDT() 
```

