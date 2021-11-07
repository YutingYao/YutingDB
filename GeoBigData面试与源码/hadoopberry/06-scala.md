## 下载

从这个网页：Latest [2.12.x maintenance release](https://www.scala-lang.org/download/scala2.html)

下载这个包：Scala [2.12.15] - Released on September 14, 2021

下载好后解压到：

```bash
sudo tar zxvf ~/Downloads/scala-2.12.15.tgz -C /opt
```

  进入到解压目录并重命名：

```bash
cd /opt
sudo mv scala-2.12.15 scala
```

  配置环境变量：

```bash
sudo vim ~/.bashrc
```

```
export SCALA_HOME = /opt/scala
export PATH = $PATH:$SCALA_HOME/bin
```

 执行source命令并测试：

```bash
source  ~/.bashrc
scala -version
```