## 遇到过哪些设计模式？伴生对象就是单例模式

单例模式主要是避免了一个全局使用 の 类频繁地创建和销毁。当想要控制【实例数据】节省系统资源 の 时候可以使用。

在java中单例模式有很多种写法，比如什么饱汉、饿汉，双重检查等等但是在scala语言中这个完全不需要什么这么多花里胡哨 の ，仅仅需要一个伴生对象。伴生对象就是单例模式 の 。

伴生对象采用object声明

```scala
private[netty] object NettyRpcEnv extends Logging {
  /**
   * When deserializing the [[NettyRpcEndpointRef]], it needs a reference to [[NettyRpcEnv]].
   * Use `currentEnv` to wrap the deserialization codes. E.g.,
   *
   * {{{
   *   NettyRpcEnv.currentEnv.withValue(this) {
   *     your deserialization codes
   *   }
   * }}}
   */
  private[netty] val currentEnv = new DynamicVariable[NettyRpcEnv](null)

  /**
   * Similar to `currentEnv`, this variable references the client instance associated with an
   * RPC, in case it's needed to find out the remote address during deserialization.
   */
  private[netty] val currentClient = new DynamicVariable[TransportClient](null)

}
```