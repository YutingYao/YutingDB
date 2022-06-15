package util

  import com.fasterxml.jackson.core.`type`.TypeReference
  import com.fasterxml.jackson.databind.ObjectMapper
  import com.fasterxml.jackson.module.scala.DefaultScalaModule

  import java.lang.reflect.{ParameterizedType, Type};

  object JacksonWrapper {
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)

    def serialize(value: Any): String = {
      import java.io.StringWriter
      val writer = new StringWriter()
      mapper.writeValue(writer, value)
      writer.toString
    }

    def deserialize[T: Manifest](value: String) : T =
      mapper.readValue(value, typeReference[T])

    private [this] def typeReference[T: Manifest] = new TypeReference[T] {
      override def getType = typeFromManifest(manifest[T])
    }

    private [this] def typeFromManifest(m: Manifest[_]): Type = {
      if (m.typeArguments.isEmpty) { m.runtimeClass }
      else new ParameterizedType {
        def getRawType = m.runtimeClass
        def getActualTypeArguments = m.typeArguments.map(typeFromManifest).toArray
        def getOwnerType = null
      }
    }
  }