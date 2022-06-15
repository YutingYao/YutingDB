
## sbt实例

7.1 hello world的目录结构是：

```sh
find
```

代码如下：

```sh
mkdir ~/simpleapp
mkdir ~/simpleapp/src
mkdir ~/simpleapp/src/main
mkdir ~/simpleapp/src/main/scala
vim SimpleApp.scala   #内容如下
```

SimpleApp.scala

```js
object SimpleApp{
        def main(args: Array[String]){
                println("Hello World!")
        } 
}
```

7.2 创建simple.sbt

```sh
cd ~/simpleapp
vim ./simple.sbt      #内容如下
```

simple.sbt内容如下：

每一行之间空一行。

scala version 和 spark版本信息根据所安装的spark所写。

笔者安装的是spark2.1.0和scala 2.11.8。

```sh
name :="Simple Project"

version :="1.0"

scalaVersion := "2.11.8"

libraryDependencies +="org.apache.spark" %% "spark-core" % "2.1.0"
```

7.3配置环境变量，编译和运行。

```sh
vim ~/.bashrc 		#在开头添加如下内容：export PATH=/opt/sbt:$PATH
source ~/.bashrc         #使之生效
cd ~/simpleapp
sbt compile           #编译，等待很久，天朝龟速
sbt package           #打包
/opt/spark/bin/spark-submit --class "SimpleApp" ~/sparkapp/target/scala-2.11/simple-project_2.11-1.0.jar   #将生成的jar包通过spark-summit提交到spark中执行
```

8. 接下来进行spark第二个应用程序的打包

1）首先进入用户的主文件夹

```sh
cd ~
```

创建应用程序根目录

```sh
mkdir sparkapp
```

创建所需的文件夹结构

```sh
mkdir -p ./sparkapp/src/main/scala
```

2）在./sparkapp/src/main/scala下建立一个SimpleApp.scala的文件

```sh
vim ./sparkapp/src/main/scala/SimpleApp.scala
```

内容如下：

```s
 /* SimpleApp.scala */
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
  
object SimpleApp {
  def main(args: Array[String]) {
  val logFile = "file:///opt/spark-2.4.3/README.md"
  val conf = new SparkConf().setAppName("Simple Application")
  val sc = new SparkContext(conf)
  val logData = sc.textFile(logFile, 2).cache()
  val numAs = logData.filter(line => line.contains("a")).count()
  val numBs = logData.filter(line => line.contains("b")).count()
  println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))
}
}
```

该程序计算 `/opt/spark/README` 文件中包含 “a” 的行数 和包含 “b” 的行数。

代码第8行的 `/opt/spark` 为 Spark 的安装目录，

如果不是该目录请自行修改。

不同于 `Spark shell`，

独立应用程序需要通过 `val sc = new SparkContext(conf)` 初始化 `SparkContext`，

`SparkContext` 的参数 `SparkConf` 包含了应用程序的信息。


该程序依赖 `Spark API`，因此我们需要通过`sbt` 进行编译打包。

 `./sparkapp` 中新建文件 `simple.sbt`，

```sh
vim ./sparkapp/simple.sbt
```
 
添加内容如下，声明该独立应用程序的信息以及与 Spark 的依赖关系：

```sh
name := "Simple Project"
version := "1.0"
scalaVersion := "2.11.12"
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.3"
```

文件 `simpale.sbt` 需要指明 `Spark` 和 `Scla` 的版本。

在上面的配置信息中，`scalaVersion`用来指定scala的版本，

`sparkcore`用来指定spark的版本，

这两个版本信息都可以在之前的启动 Spark shell 的过程中，

从屏幕的显示信息中找到。

下面就是笔者在启动过程当中，看到的相关版本信息

（备注：屏幕显示信息会很长，需要往回滚动屏幕仔细寻找信息）


3）使用 sbt 打包 Scala 程序

```sh
cd ~/sparkapp
find .
```

接着，我们就可以通过如下代码将整个应用程序打包成 JAR（首次运行同样需要下载依赖包 ）：

```sh
/opt/sbt/sbt package
```

生成的jar包的位置为：

```sh
~/sparkapp/target/scala-2.11/simple-project_2.11-1.0.jar
```

4）通过spark-submit运行程序，将生成的jar包通过是spark-submit提交到spark中运行

```sh
/opt/spark-2.4.3/bin/spark-submit --class "SimpleApp" ~/sparkapp/target/scala-2.11/simple-project_2.11-1.0.jar
```

上面的命令输出的东西会特别多，所以也可以通过管道化来筛选跟输出指定的信息

```sh
/opt/spark-2.4.3/bin/spark-submit --class "SimpleApp" ~/sparkapp/target/scala-2.11/simple-project_2.11-1.0.jar 2>&1 | grep "Lines with a:"
```