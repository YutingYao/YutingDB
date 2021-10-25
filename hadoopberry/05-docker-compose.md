## 安装

https://docs.docker.com/compose/install/

在Linux系统上安装Compose

在Linux上，您可以从GitHub上的Compose存储库发布页面下载Docker Compose二进制文件。

按照链接中的说明操作，其中包括在终端中运行curl命令来下载二进制文件。下面也包括这些分步说明。

```sh
sudo curl -L "https://hub.fastgit.org/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
```

```sh
sudo curl -L "https://github.com.cnpmjs吹牛皮没接上.org/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
```

将可执行权限应用于二进制文件：

```sh
sudo chmod +x /usr/local/bin/docker-compose
```

如果安装后docker compose命令失败，请检查路径。您还可以创建指向/usr/bin或路径中任何其他目录的符号链接。

例如:

```sh
sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
```

### 启动

Compose 是 Docker 公司推出的一个工具软件，可以管理多个 Docker 容器组成一个应用。你需要定义一个 YAML[17] 格式的配置文件 docker-compose.yml，写好多个容器之间的调用关系。然后，只要一个命令，就能同时启动/关闭这些容器。

```sh
# 启动所有服务
docker-compose up
```

```sh
# 关闭所有服务
docker-compose stop
```

## 案例一

在 docker-demo 目录下，新建 docker-compose.yml 文件，写入下面的内容。

```yml
mysql:
    image: mysql:5.7
    environment:
     - MYSQL_ROOT_PASSWORD=123456
     - MYSQL_DATABASE=wordpress
web:
    image: wordpress
    links:
     - mysql
    environment:
     - WORDPRESS_DB_PASSWORD=123456
    ports:
     - "127.0.0.3:8080:80"
    working_dir: /var/www/html
    volumes:
     - wordpress:/var/www/html
```

上面代码中，两个顶层标签表示有两个容器 mysql 和 web。每个容器的具体设置，前面都已经讲解过了，还是挺容易理解的。

```sh
docker-compose up
```

浏览器访问 http://127.0.0.3:8080，应该就能看到 WordPress 的安装界面。

现在关闭两个容器。

```sh
docker-compose stop
```

关闭以后，这两个容器文件还是存在的，写在里面的数据不会丢失。下次启动的时候，还可以复用。下面的命令可以把这两个容器文件删除（容器必须已经停止运行）。

```sh
docker-compose rm
```

## 其他命令

```sh
docker-compose -f docker-compose-文件1.yml  up -d
```

```sh
docker-compose -f docker-compose-文件2.yml  up -d
```

```sh
docker-compose -f docker-compose-文件3.yml  up -d
```

`docker-compose up` 本质是`docker-compose logs -f`，

> 它会收集所有容器的日志输出直到退出

`docker-compose up -d` 以后台的方式运行容器

> 不会在终端上打印运行日志

