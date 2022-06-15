## 命令

### 一个曾经用过的案例dockerfile

```scala
docker build -f  /home/yyt/pytfa/docker/Dockerfile -t pytfa_docker .

docker build -t etfl_docker .
docker build -t pytfa_docker .
sudo chmod -R 777 /home/yyt/pytfa/docker
```

![Snipaste_2021-06-06_20-58-25](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/Snipaste_2021-06-06_20-58-25.2vxjsjvuo9s0.png)

```s
docker run -it  pytfa_docker /bin/bash
docker run -it  etfl_docker /bin/bash
```

```s
ls -l
```

![Snipaste_2021-06-06_20-59-31](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/Snipaste_2021-06-06_20-59-31.183y0v09sjwg.png)

WSL

```scala
sudo cp -r /mnt/c/Users/dell/Downloads/pytfa /home/yyt
cd pytfa/docker
sudo cp -r /mnt/c/Users/dell/Downloads/etfl /home/yyt
cd etfl/docker
docker cp /home/yyt/etfl/tutorials/test_small.py a4ec8a1b4a4e:/home/pytfa
```

### 查看镜像

```s
docker images
```

### 列出运行的容器

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6atxj263i1k0.png)

```s
docker ps
```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3cxb1fute140.png)

```s
docker ps -a
```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6d6t2bi1o340.png)

### 创建容器

```s
docker run -it centos /bin/bash
```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3abo0y628aw0.png)

### 启动容器&停止容器

```s
docker start 容器id
docker restart 容器id
docker stop 容器id
docker kill 容器id
```

### 删除镜像

```s
docker rmi
```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6czbo0bk3i00.png)

### 帮助命令

```s
docker version
docker info 
docker 命令 --help
```

### 查看所有本地的主机上的镜像

```s
docker images
```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5j4hocm3mtw0.png)

### 搜索镜像

```s
docker search mysql 
```

### 下载镜像

```s
docker pull mysql
```

### 退出容器

容器停止：

```s
exit 
```

容器不停止：

`ctrl`+`P`+`Q`  