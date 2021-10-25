

## [安装sdk](https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Linux.html)


```sh
sudo apt-get update
sudo apt-get install apt-transport-https curl gnupg -yqq
echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | sudo tee /etc/apt/sources.list.d/sbt.list
echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | sudo tee /etc/apt/sources.list.d/sbt_old.list
curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | sudo -H gpg --no-default-keyring --keyring gnupg-ring:/etc/apt/trusted.gpg.d/scalasbt-release.gpg --import
sudo chmod 644 /etc/apt/trusted.gpg.d/scalasbt-release.gpg
sudo apt-get update
sudo apt-get install sbt
```

sbt是一款Spark用来对scala编写程序进行打包的工具，Spark 中没有自带 sbt

## 配置环境变量

这里最好是登录超级用户，不然有时候会莫名安装失败(也可能不需要)

```sh
su root
```

如果在国内网络环境，sbt的网络依赖可能会存在下载阻碍，可以单独配置更换国内源，通过新增文件

```sh
vim ~/.sbt/repositories
```

添加如下内容后

精简一下文件，不是越多越好，直接定位到阿里云提供的源即可！！！:star:

版本一：

```s
[repositories]
local
aliyun: http://maven.aliyun.com/nexus/content/groups/public/
typesafe: http://repo.typesafe.com/typesafe/ivy-releases/, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext], bootOnly
sonatype-oss-releases
maven-central
sonatype-oss-snapshots
```

版本二：

```s
[repositories]
local
aliyun: https://maven.aliyun.com/repository/public
huaweicloud-maven: https://repo.huaweicloud.com/repository/maven/
jcenter: https://jcenter.bintray.com
maven-central: https://repo1.maven.org/maven2/
typesafe: https://repo.typesafe.com/typesafe/ivy-releases/, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext], bootOnly
sbt-plugin-repo: https://repo.scala-sbt.org/scalasbt/sbt-plugin-releases, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]
```

版本三：

```sh
[repositories]
local
aliyun: http://maven.aliyun.com/nexus/content/groups/public
typesafe-ivy-releases: http://repo.typesafe.com/typesafe/ivy-releases/, [organization]/[module]/[revision]/[type]s/[artifact](-[classifier]).[ext], bootOnly
sonatype-oss-releases
maven-central
sonatype-oss-snapshots
```

版本四：

```sh
[repositories]
local
aliyun-maven-public: https://maven.aliyun.com/repository/public
aliyun-maven-central: https://maven.aliyun.com/repository/central
huaweicloud-maven: https://repo.huaweicloud.com/repository/maven/
maven-central: https://repo1.maven.org/maven2/
huaweicloud-ivy: https://repo.huaweicloud.com/repository/ivy/, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]
sbt-plugin-repo: https://repo.scala-sbt.org/scalasbt/sbt-plugin-releases, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext]
```

版本五：

```sh
[repositories]
local
jcenter:https://maven.aliyun.com/repository/jcenter
central:https://maven.aliyun.com/repository/central
google:https://maven.aliyun.com/repository/google
releases:https://maven.aliyun.com/repository/releases
typesafe: https://repo.typesafe.com/typesafe/ivy-releases/, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext], bootOnly
sonatype-oss-releases
maven-central
sonatype-oss-snapshots
```

版本六：

```sh
alirepo1:https://maven.aliyun.com/repository/central
alirepo2:https://maven.aliyun.com/repository/jcenter
alirepo3:https://maven.aliyun.com/repository/public
```

版本七：

```sh
[repositories]
local
osc: http://maven.aliyun.com/nexus/content/groups/public/
typesafe: http://repo.typesafe.com/typesafe/ivy-releases/, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext], bootOnly
sonatype-oss-releases
maven-central
sonatype-oss-snapshots
```

版本八：

```sh
[repositories]
  local
  local-maven: file:///file/repository/maven
  aliyun-nexus: https://maven.aliyun.com/nexus/content/groups/public/
  aliyun-nexus2: https://maven.aliyun.com/nexus/content/groups/public/,[organization]/[module]/(scala_[scalaVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext], bootOnly
  maven-central

[ivy]
  ivy-home: /file/repository/sbt
```

版本九：

```sh
[repositories]
local
aliyun: http://maven.aliyun.com/nexus/content/groups/public/
typesafe: http://repo.typesafe.com/typesafe/ivy-releases/, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext], bootOnly
sonatype-oss-releases
maven-central
sonatype-oss-snapshots
```

> 如果只是在单个项目中修改的话,在build.sbt里添加

```sh
resolvers += "aliyun" at "http://maven.aliyun.com/nexus/content/groups/public/"
```

有可能需要修改启动jar,也可能不需要

修改/soft/sbt/bin/sbt-launch.jar包的/sbt/sbt.boot.properties

```s
[scala]
  version: ${sbt.scala.version-auto}

[app]
  org: ${sbt.organization-org.scala-sbt}
  name: sbt
  version: ${sbt.version-read(sbt.version)[1.3.4]}
  class: ${sbt.main.class-sbt.xMain}
  components: xsbti,extra
  cross-versioned: ${sbt.cross.versioned-false}
  resources: ${sbt.extraClasspath-}

[repositories]
  local
  local-maven: file:/file/repository/maven
  local-preloaded-ivy: file:///${sbt.preloaded-${sbt.global.base-${user.home}/.sbt}/preloaded/}, [organization]/[module]/[revision]/[type]s/[artifact](-[classifier]).[ext]
  local-preloaded: file:///${sbt.preloaded-${sbt.global.base-${user.home}/.sbt}/preloaded/}
  aliyun-nexus: http://maven.aliyun.com/nexus/content/groups/public/
  aliyun-nexus2: https://maven.aliyun.com/nexus/content/groups/public/,[organization]/[module]/(scala_[scalaVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext], bootOnly
  maven-central
  sbt-maven-releases: https://repo.scala-sbt.org/scalasbt/maven-releases/, bootOnly
  sbt-maven-snapshots: https://repo.scala-sbt.org/scalasbt/maven-snapshots/, bootOnly
  typesafe-ivy-releases: https://repo.typesafe.com/typesafe/ivy-releases/, [organization]/[module]/[revision]/[type]s/[artifact](-[classifier]).[ext], bootOnly
  sbt-ivy-snapshots: https://repo.scala-sbt.org/scalasbt/ivy-snapshots/, [organization]/[module]/[revision]/[type]s/[artifact](-[classifier]).[ext], bootOnly

[boot]
  directory: ${sbt.boot.directory-${sbt.global.base-${user.home}/.sbt}/boot/}
  lock: ${sbt.boot.lock-true}

[ivy]
  ivy-home: /file/repository/sbt
  checksums: ${sbt.checksums-sha1,md5}
  override-build-repos: ${sbt.override.build.repos-false}
  repository-config: ${sbt.repository.config-${sbt.global.base-${user.home}/.sbt}/repositories}
```

执行

```sh
sbt --version
```

查看是否正常

## Create the project

到一个空文件夹。

```sh
cd
```

运行以下命令

```sh
sbt new scala/hello-world.g8
```

这会从 GitHub 中提取“hello-world”模板。

它还将创建一个目标文件夹，您可以忽略它。

出现提示时，将应用程序命名为 hello-world。

这将创建一个名为“hello-world”的项目。

让我们看看刚刚生成的内容：

```s
- hello-world
    - project (sbt 使用它来安装和管理插件和依赖项)
        - build.properties
    - src
        - main
            - scala (您所有的 Scala 代码都放在这里)
                - Main.scala (程序入口点) <--这就是我们现在所需要的
    - build.sbt (sbt 的构建定义文件)
```

hello的scala文件里的代码可能如下所示

```js
object Hi{
  def main(args:Array[String]) = println("hi!")
}
```

构建项目后，sbt将为生成的文件创建更多的目标目录。你可以忽略这些。

```sh
cd hello-world
```

运行

```sh
sbt
```

这将打开sbt控制台。

输入

```sh
~run
```

~是可选的，可使sbt在每次保存文件时重新运行，从而实现快速的编辑/运行/调试周期。

sbt还将生成一个您可以忽略的目标目录。


## 修改代码


打开

```s
src/main/scala/Main.scala
``` 

将“Hello, World!”改为“Hello, New York!”

如果尚未停止sbt命令，则应在控制台上显示“Hello, New York!”。

您可以继续进行更改，并在控制台中查看结果。

稍微改变一下，让我们看看如何使用已发布的库为我们的应用程序添加额外的功能。

打开build.sbt并添加以下行：

```sbt
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"
```

在这里，`libraryDependencies`是一组依赖项，

通过使用`+=`，我们将[scala解析器combinators](https://github.com/scala/scala-parser-combinators)赖项添加到sbt启动时将去获取的依赖项集中。

现在，在任何Scala文件中，您都可以通过常规导入从Scala解析器组合器导入类、对象等。

您可以在`Scaladex`（[Scala库索引](https://index.scala-lang.org/)）上找到更多已发布的库，您还可以将上述依赖项信息复制到`build.sbt`文件中。

## 在命令行中用SBT和scalatest测试scala

Scala有多个库和测试方法，但在本教程中，我们将演示一个来自ScalaTest框架的流行选项，名为[AnyFunSuite](https://www.scalatest.org/getting_started_with_fun_suite)。

我们假设您知道如何使用sbt创建一个Scala项目。

在命令行上，在某处创建一个新目录。

```sh
cd 
```

进入目录并运行 

sbt new scala/scalatest-example.g8

将项目命名为`ScalaTestTutorial`。
该项目附带 `ScalaTest` 作为 `build.sbt` 文件中的依赖项。

```sh
cd 
```

进入目录并运行 

```sh
sbt test
```

这将使用名为 `CubeCalculator.cube` 的单个测试运行测试套件 `CubeCalculatorTest`。

```s
sbt test
[info] Loading global plugins from /Users/username/.sbt/0.13/plugins
[info] Loading project definition from /Users/username/workspace/sandbox/my-something-project/project
[info] Set current project to scalatest-example (in build file:/Users/username/workspace/sandbox/my-something-project/)
[info] CubeCalculatorTest:
[info] - CubeCalculator.cube
[info] Run completed in 267 milliseconds.
[info] Total number of tests run: 1
[info] Suites: completed 1, aborted 0
[info] Tests: succeeded 1, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[success] Total time: 1 s, completed Feb 2, 2017 7:37:31 PM
```

了解测试:

在文本编辑器中打开两个文件：

```s
src/main/scala/CubeCalculator.scala

src/test/scala/CubeCalculatorTest.scala
```

在 `CubeCalculator.scala` 文件中，您将看到我们如何定义`函数立方体`。

在文件 `CubeCalculatorTest.scala` 中，您将看到我们有一个以我们正在测试的`对象命名`的`类`。

```js
  import org.scalatest.funsuite.AnyFunSuite

// CubeCalculatorTest类意味着我们正在测试CubeCalculator对象
// 扩展AnyFunSuite让我们可以使用ScalaTest的AnyFunSuite类的功能，比如测试函数

  class CubeCalculatorTest extends AnyFunSuite { 
// test是来自AnyFunSuite的函数，它收集函数体中断言的结果。
// “CubeCalculator.Cube”是这个测试的名称。
// 你可以叫它任何名字，但只有一个约定是" ClassName.methodName "。
      test("CubeCalculator.cube") {
// Assert接受一个布尔条件并确定测试是否通过。
// CubeCalculator.cube(3) === 27检查立方体函数的输出是否确实是27。
// ===是ScalaTest的一部分，提供干净的错误消息。
          assert(CubeCalculator.cube(3) === 27)
      }
  }
```

添加另一个测试块

使用它自己的assert语句检查数据集是否为0。

```js
   import org.scalatest.funsuite.AnyFunSuite
    
   class CubeCalculatorTest extends AnyFunSuite {
       test("CubeCalculator.cube 3 should be 27") {
           assert(CubeCalculator.cube(3) === 27)
       }

       test("CubeCalculator.cube 0 should be 0") {
           assert(CubeCalculator.cube(0) === 0)
       }
   }
```

再次执行sbt测试以查看结果。

```s
 sbt test
 [info] Loading project definition from C:\projects\scalaPlayground\scalatestpractice\project
 [info] Loading settings for project root from build.sbt ...
 [info] Set current project to scalatest-example (in build file:/C:/projects/scalaPlayground/scalatestpractice/)
 [info] Compiling 1 Scala source to C:\projects\scalaPlayground\scalatestpractice\target\scala-2.13\test-classes ...
 [info] CubeCalculatorTest:
 [info] - CubeCalculator.cube 3 should be 27
 [info] - CubeCalculator.cube 0 should be 0
 [info] Run completed in 257 milliseconds.
 [info] Total number of tests run: 2
 [info] Suites: completed 1, aborted 0
 [info] Tests: succeeded 2, failed 0, canceled 0, ignored 0, pending 0
 [info] All tests passed.
 [success] Total time: 3 s, completed Dec 4, 2019 10:34:04 PM
```