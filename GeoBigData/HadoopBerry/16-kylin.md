
## 软件硬件要求

· Hadoop: 2.7+, 3.1+ (since v2.5)

· Hive: 0.13 - 1.2.1+

· HBase: 1.1+, 2.0 (since v2.5)

· Spark (可选) 2.3.0+

· Kafka (可选) 1.0.0+ (since v2.5)

· JDK: 1.8+ (since v2.5)

· OS: Linux only, CentOS 6.5+ or Ubuntu 16.0.4+

运行 Kylin 的服务器的最低配置为 4 core CPU，16 GB 内存和 100 GB 磁盘。对于高负载的场景，建议使用 24 core CPU，64 GB 内存或更高的配置。

Kylin 依赖于 Hadoop 集群处理大量的数据集。您需要准备一个配置好 HDFS，YARN，MapReduce，Hive， HBase，Zookeeper 和其他服务的 Hadoop 集群供 Kylin 运行。

Kylin 可以在 Hadoop 集群的任意节点上启动。方便起见，您可以在 master 节点上运行 Kylin。但为了更好的稳定性，建议您将 Kylin 部署在一个干净的 Hadoop client 节点上，该节点上 Hive，HBase，HDFS 等命令行已安装好且 client 配置（如 core-site.xml，hive-site.xml，hbase-site.xml及其他）也已经合理的配置且其可以自动和其它节点同步。

运行 Kylin 的 Linux 账户要有访问 Hadoop 集群的权限，包括创建/写入 HDFS 文件夹，Hive 表， HBase 表和提交 MapReduce 任务的权限。

## kylin 安装与下载

[官网下载地址](http://kylin.apache.org/cn/download/)

[历史所有包下载地址](https://archive.apache.org/dist/kylin)

将下载的安装包上传到 /opt 目录

解压gz包（使用root用户操作）

```sh
## 解压
tar -zxvf apache-kylin-3.1.2-bin-cdh60.tar.gz
## 解压后，可以看见一个 apache-kylin-3.1.0-bin-cdh60 目录
```

```sh
mv apache-kylin-3.1.2-bin-cdh60 kylin
```

```sh
chown -R yaoyuting /opt/kylin
```

修改 ${KYLIN_HOME}/conf/kylin.properties 配置

```sh
cd /opt/kylin

cd conf
vim kylin.properties ## 修改配置
## 再普及下shell常用指令
## 1、 vim file_name 进入文件预览 
## 2、按 i 进入编辑  
## 3、 完成 按 esc键退出编辑 
## 4、shift+zz 快捷键保存
```

kylin.properties 对应配置的内容如下：

```s
# 改成东八区, 否则会出现时间显示不准的问题
kylin.web.timezone=GMT+8
```

```s
## Working folder in HDFS, better be qualified absolute path, make sure user has the right permission to this directory
## kylin在hdfs中的工作目录
kylin.env.hdfs-working-dir=/kylin
#
## DEV|QA|PROD. DEV will turn on some dev features, QA and PROD has no difference in terms of functions.
## 当前kylin作为什么环境存在.QA测试环境
kylin.env=QA


## Kylin server mode, valid value [all, query, job]
## kylin节点类型(all表示:可开发、可查询)
kylin.server.mode=all
#
## List of web servers in use, this enables one web server instance to sync up with other servers.
## kylin服务的IP:端口
kylin.server.cluster-servers=10.100.0.1:7070
#
## Display timezone on UI,format like[GMT+N or GMT-N]
## 时区
kylin.web.timezone=GMT+8

## Hive client, valid value [cli, beeline]
## kylin 连接hive的方式: cli 表示客户端, beeline 表示通过jdbc连hive。(推荐beeline)
kylin.source.hive.client=beeline

## Parameters for beeline client, only necessary if hive client is beeline
## beeline 连接串. 如果集群用了ldap 加个参数 -p 密码. (用于刷cube连接hive)
kylin.source.hive.beeline-params=-n kylin -p passwd_1 --hiveconf hive.security.authorization.sqlstd.confwhitelist.append='mapreduce.job.*|dfs.*' -u jdbc:hive2://101.10.0.2:10000

## Hive database name for putting the intermediate flat tables
## hive中临时工作库
kylin.source.hive.database-for-flat-table=dc_tmp

## 生成的hbase块数量(最小使用2个,提升kylin结果查询效率)
kylin.storage.hbase.min-region-count=2
kylin.storage.hbase.max-region-count=500


## Max count of concurrent jobs running
## 最大可同时运行cube构建数量
kylin.job.max-concurrent-jobs=1000

## If true, will send email notification on job complete
## 邮箱配置信息
kylin.job.notification-enabled=true
kylin.job.notification-mail-enable-starttls=true
kylin.job.notification-mail-host=mail.test.com
kylin.job.notification-mail-port=25
kylin.job.notification-mail-username=userice
kylin.job.notification-mail-password=passa@2018
kylin.job.notification-mail-sender=tcjfdc_service@lt.com

## 如果集群集成了LDAP,可配置下。
## 如果集群没有集成Ldap，可忽略下面的配置
###========= ldap 
## with "testing" profile, user can use pre-defined name/pwd like KYLIN/ADMIN to login
## 默认是testing,使用ldap的话需要改成ldap
kylin.security.profile=ldap
#
## Admin roles in LDAP, for ldap and saml
## 用来做管理员的账号.
kylin.security.acl.admin-role=kylin
#
## LDAP authentication configuration
## ldap的服务ip和端口号，端口号默认389
kylin.security.ldap.connection-server=ldap://xxx.xxx.xxx.xx:389
## LDAP的登陆账号，cn=xxx,dc=xxx,dc=xxx
kylin.security.ldap.connection-username=cn=Manager,dc=xx,dc=com
## 密码kylin是加密的，使用kylin工具来生成：如下是生成方式
## cd $KYLIN_HOME/tomcat/webapps/kylin/WEB-INF/lib
## java -classpath kylin-server-base-3.1.0-SNAPSHOT.jar:kylin-core-common-3.1.0-SNAPSHOT.jar:spring-beans-4.3.20.RELEASE.jar:spring-core-4.3.20.RELEASE.jar:commons-codec-1.6.jar org.apache.kylin.rest.security.PasswordPlaceholderConfigurer AES 你的密码写这
kylin.security.ldap.connection-password=RY/z8cv5DXzPbAB3o2250g==
#
## LDAP user account directory;
## 具体用户的ou
kylin.security.ldap.user-search-base=ou=People,dc=xx,dc=com
## 搜索匹配模式，可以是(&(uid={0})),就是dn的
kylin.security.ldap.user-search-pattern=(&(cn={0}))
## 具体分组的ou
kylin.security.ldap.user-group-search-base=ou=Group,dc=xx,dc=com
#kylin.security.ldap.user-group-search-filter=(|(member={0})(memberUid={1}))
#
## LDAP service account directory 如上
kylin.security.ldap.service-search-base=ou=People,dc=xx,dc=com
kylin.security.ldap.service-search-pattern=(&(cn={0}))
kylin.security.ldap.service-group-search-base=ou=Group,dc=xx,dc=com
###========= ldap end
```

说明：

① 如果集群没有用ldap，忽略ldap相关配置

② 如果启用ldap，注意密码需要额外生成一下。

修改日志目录 kylin-server-log4j.properties 配置中.
让查找日志更方便。

```sh
vim /opt/kylin/conf/kylin-server-log4j.properties
 ## 后续日志就会输出到指定的位置
```

```s
# 也可以不修改
log4j.appender.file.File=/data/log/kylin/kylin.log
log4j.appender.realtime.File=/data/log/kylin/streaming_coordinator.log
```

检测环境是否正常

```sh
sh /opt/kylin/bin/check-env.sh
```

说明：

① 如果不全为pass得一个个[问题解决](https://mp.weixin.qq.com/s/Fsz0WYR-kzwxdREYqJEfuA)。

```sh
sudo vim ~/.bashrc
```

```sh
export KYLIN_HOME=/opt/software/kylin-3.0.1
export PATH=${JAVA_HOME}/bin:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin:${ZOOKEEPER_HOME}/bin:${HIVE_HOME}/bin:${HBASE_HOME}/bin:${KYLIN_HOME}/bin:$PATH
```

使得配置的环境变量立即生效：

```sh
source ~/.bashrc
```

由于我们这里不采用Spark作为计算引擎，所以需要将bin目录下的kylin.sh脚本进行修改

```sh
 if [[ -z $reload_dependency && `ls -1 ${dir}/cached-* 2>/dev/null | wc -l` -eq 5 ]]
    then
        echo "Using cached dependency..."
        source ${dir}/cached-hive-dependency.sh
        source ${dir}/cached-hbase-dependency.sh
        source ${dir}/cached-hadoop-conf-dir.sh
        source ${dir}/cached-kafka-dependency.sh
        #source ${dir}/cached-spark-dependency.sh
    else
        source ${dir}/find-hive-dependency.sh
        source ${dir}/find-hbase-dependency.sh
        source ${dir}/find-hadoop-conf-dir.sh
        source ${dir}/find-kafka-dependency.sh
        #source ${dir}/find-spark-dependency.sh
    fi
```

```sh

## 如果创建了kylin，先su 到kylin用户下。再启动
sh /opt/kylin/bin/kylin start ## 启动
./bin/kylin.sh start 
## 启动完成 ，到 步骤8 配置的log目录下查看日志
## sh /opt/kylin-3.1.0/bin/kylin stop ## 停止
```

## 登录kylin界面

登录地址：<http://ip:7070/kylin/login>

账号密码:  默认 admin/KYLIN

初始用户名和密码是 ADMIN/KYLIN。

选择容器自带的工程 登陆后，在“Choose Project”下拉菜单下选择“learn_kylin”。

Models - 数据模型

Models，即模型，用于建立Kylin中的对象和已有的Hive表的对应关系。

查看数据模型

第1步 选择Models下面的“kylin_sales_model”

第2步 选择Data Model可以查看事实表和维度表的清单和连接关系

另外，点击“3 Dimensions”和“4 Measures”分别可以查看配置的每张表中的维度和度量。

在datasource中可以查看Kylin已经对接的表对象，包含来自Hive的静态表和来自Kafka的动态表

Cube - 多维立方体

从模型中选择需要的维度和度量即可组成一个立方体，

从维度角度上看Cube是Model的一个自己，

从度量角度看Cube的度量在Model中度量字段上做汇总，计数和TopN操作。

查看Cube配置信息

添加维度

维度可以分为普通维度（Normal）和衍生维度（Derived），

通常可以把维度表中的字段设置成衍生维度，

这样只有维度表所在的FK参与生产Cuboid，从

而降低Cubiod数量。

那对衍生维度的查询会查询维度FK的查询，然后再进行衍生维度的关联查询。

添加度量

目前可以支持的度量包括SUM，MIN，MAX，COUNT，COUNT_DISTINCT，TOP_N， EXTENDED_COLUMN，PERCENTILE。

高级设置

在高级设置通过强制维度（Mandatory），层级维度（Hierarchy），联合维度（Joint）等方式对Cube进行剪枝，通过业务逻辑减少Cuboid的数量。

## 执行运算

查看事实表的时间跨度

```sql
hive> select min(part_dt), max(part_dt) from kylin_sales;
```

```s
MapReduce Jobs Launched:
Stage-Stage-1: Map: 1  Reduce: 1   Cumulative CPU: 4.55 sec   HDFS Read: 807367 HDFS Write: 22 SUCCESS
Total MapReduce CPU Time Spent: 4 seconds 550 msec
OK
2012-01-01  2014-01-01
Time taken: 22.709 seconds, Fetched: 1 row(s)
```

在web页面执行运算，点击Action -> Build

选择事实表计算时间段

提交后在Monitor下可以查看执行状态

执行完成

查看HBase中的结果

kylin将维度组合的结果存储在HBase中

```sh
# hbase shell
hbase(main):002:0> list
```

```s

TABLE                                                                                                                                                
KYLIN_5UOHDXWLVU                                                                                                                                      
kylin_metadata                                                                                                                                        
2 row(s) in 0.2740 seconds
hbase(main):005:0> scan 'KYLIN_5UOHDXWLVU' ,{LIMIT=>5}
ROW                                    COLUMN+CELL                                                                                                    
 \x00\x00\x00\x00\x00\x00\x00\x00@\x00 column=F1:M, timestamp=0, value=\x04\x03G>m\x12$\x0C                                                          
 \x0B6\x96
 \x00\x00\x00\x00\x00\x00\x00\x00@\x00 column=F2:M, timestamp=0, value=\x00\x0C\x1E\x00\x022\x00\x01\xF2\x00\x03\x1A\x01\x04$\x01\x01\x8B\x01\x03\xB6\
```

因为在Cube配置中包含了数据的精准统计和HLL统计，所以在HBase中有两个列簇存放不同方式的统计量。

根据模型中的配置维度进行聚合查询

```sql
select YEAR_BEG_DT, sum(price), count(1) from KYLIN_SALES
inner join KYLIN_CAL_DT
    on KYLIN_SALES.PART_DT = KYLIN_CAL_DT.CAL_DT
where trans_id > 3000
group by YEAR_BEG_DT;
```

Top N排序

```sql
select seller_id, sum(price) from
kylin_sales
group by seller_id
order by sum(price)
limit 10;
```

以上能命中预计算的查询返回结果的时间都在1秒内，远远快于在Hive中查询所需的时间。

## 基本使用

### 准备数据

 ```sql
--  # 建表 dept
create external table if not exists dept(
    deptno int,
    dname string,
    loc int
)
row format delimited fields terminated by '\t';

-- # 建表 emp
create external table if not exists emp(
    empno int,
    ename string,
    job string,
    mgr int,
    hiredate string, 
    sal double, 
    comm double,
    deptno int)
row format delimited fields terminated by '\t';

-- # 分别向两张表导入数据
load data local inpath '/home/xiaokang/dept.txt' into table xiaokang.dept;
load data local inpath '/home/xiaokang/emp.txt' into table xiaokang.emp;
 ```

### 创建Project

> 登录系统

账号：ADMIN    密码：KYLIN

> 创建Project

点击+号，输入项目名称和描述，然后点击submit

> 选择数据源

点击Data Source，然后点击Load Table

输入需要加载的Hive中的表

查看数据源

### 创建数据模型（Model）

回到Models，点击New->New Model

填写相关信息，如下图所示

### 创建Cube

回到Models，点击New->New Cube

Cube创建完成后，需要进行Build

查看Build详细进度

### 查询

在Kylin中用时7秒多，第二次查询的话仅仅0.0几秒（缓存）

在Hive中用时27秒左右

