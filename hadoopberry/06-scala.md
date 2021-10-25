## 下载

从这个网页：Latest [2.12.x maintenance release](https://www.scala-lang.org/download/scala2.html)

下载这个包：Scala [2.12.15](https://downloads.lightbend.com/scala/2.12.15/scala-2.12.15.deb) - Released on September 14, 2021



















点击链接<https://www.scala-lang.org/download/2.12.10.html，下载对应版本scala（本文选择scala> 2.12.10）：

下载好后解压到：/usr/local/

```bash
sudo tar zxvf ~/Downloads/scala-2.12.10.tgz -C /usr/local/
```

 删除安装包：

```bash
rm ~/Downloads/scala-2.12.10.tgz
```

  进入到解压目录并重命名：

```bash
cd /usr/local/
sudo mv scala-2.12.10 scala
```

  配置环境变量：

```bash
sudo vim /etc/profile
```

```
export SCALA_HOME = 
export JAVA_HOME = 
export PATH = $PATH:$HOME/bin:$JAVA_HOME/bin:$SCALA_HOME/bin:/bin
```

 执行source命令并测试：

```bash
source /etc/profile
scala -version
```