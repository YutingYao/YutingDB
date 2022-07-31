
## Mysql表时间列用 datetime 还是 timestamp ？

个人认为用datetime更好。

虽然 timestamp 是 `四字节`，但一旦存储毫秒就会变成 7 字节，和datetime的8字节相差无几。

虽然 timestamp 可以 `解决时区问题`，但 还是通过前后端的转换更好，可以减轻数据库的压力。在高并发情况下，可能出现`性能抖动问题`。

