# 登录方式 - 浏览器访问

http://localhost:18080

使用yaoyuting/yyt123456登录

admin
yyt962281

一定要root

```sh
sudo su
cd /opt/zeppelin/bin
sudo ./zeppelin-daemon.sh restart
```

# 安装前的requirement：一定要逐行执行

[Build from source](https://zeppelin.apache.org/docs/latest/setup/basics/how_to_build.html#build-requirements)

```sh
sudo apt-get update
sudo apt-get install git
sudo apt-get install openjdk-8-jdk
sudo apt-get install npm
sudo apt-get install libfontconfig
sudo apt-get install r-base-dev
sudo apt-get install r-cran-evaluate
```

安装maven

```sh
wget http://www.eu.apache.org/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
sudo tar -zxf apache-maven-3.6.3-bin.tar.gz -C /usr/local/
sudo ln -s /usr/local/apache-maven-3.6.3/bin/mvn /usr/local/bin/mvn
```

# 具体安装步骤

```sh
sudo su
# 安装到/opt目录下
tar -zxf zeppelin-0.10.0-bin-all.tgz -C /opt
chown -R root:root /opt/zeppelin-0.10.0-bin-all
chown -R yaoyuting03:yaoyuting03 /opt/zeppelin-0.10.0-bin-all

cd /opt/zeppelin-0.10.0-bin-all/conf
cp zeppelin-env.sh.template zeppelin-env.sh
cp shiro.ini.template shiro.ini
 
# 修改默认端口，在zeppelin-env.sh中找到ZEPPELIN_PORT配置，修改成自己想要的
vim zeppelin-env.sh
export ZEPPELIN_PORT=18080         # port number to listen (default 8080)
 
# 配置简单登录认证，在shiro.ini中找到[users]配置，修改成自己想要的
vim shiro.ini
[users]
# List of users with their password allowed to access Zeppelin.
# To use a different strategy (LDAP / Database / ...) check the shiro doc at http://shiro.apache.org/configuration.html#Configuration-INISections
# To enable admin user, uncomment the following line and set an appropriate password.
admin = yyt123456, yaoyuting, admin
#user1 = password2, role1, role2
#user2 = password3, role3
#user3 = password4, role2
 
# 重启Zeppelin 
cd /opt/zeppelin-0.10.0-bin-all/bin
./zeppelin-daemon.sh restart
```

# Supported Interpreters:

* [Spark](https://www.apache.org/dyn/closer.lua/spark/spark-3.1.2/spark-3.1.2-bin-hadoop3.2.tgz)
* [Hive](http://www.apache.org/dyn/closer.cgi/hive/)
* JDBC
* Python
* HDFS
* [Hbase](https://hbase.apache.org/downloads.html)
* Elasticsearch
* Markdown
* Shell
* Flink
* Geode
* [PostgreSQL](https://www.postgresql.org/download/linux/ubuntu/)
  


# 在容器中启动Apache Zeppelin

```s
docker run -p 8080:8080 --rm --name zeppelin apache/zeppelin:0.10.0
```

要持久保存日志和笔记本目录，请使用docker容器的卷选项。您还可以将体积用于Spark和Flink二进制分布。

```s
docker run -u $(id -u) -p 8080:8080 --rm -v $PWD/logs:/logs -v $PWD/notebook:/notebook \
  -v /usr/lib/spark-2.4.7:/opt/spark -v /usr/lib/flink-1.12.2:/opt/flink \
  -e FLINK_HOME=/opt/flink -e SPARK_HOME=/opt/spark \
  -e ZEPPELIN_LOG_DIR='/logs' -e ZEPPELIN_NOTEBOOK_DIR='/notebook' --name zeppelin apache/zeppelin:0.10.0
```




