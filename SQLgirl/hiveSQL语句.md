## DDL数据定义

```sql
# 第四章 DDL数据定义
# 4.1 创建数据库
[COMMENT database_comment]
[LOCATION hdfs_path]
[WITH DBPROPERTIES (property_name=property_value, ...)];
# 1.默认
create database db_hive;
# 2.判断是否存在
create database if not exists db_hive;
# 3.指定位置
create database db_hive2 location '/db_hive2.db';

# 4.2 查询数据库

显示数据库
show databases;

# 2.查看数据库详情
desc database extended db_hive;
# 3.切换当前数据库
use db_hive;

修改数据库

# 用户可以使用ALTER DATABASE命令为某个数据库的DBPROPERTIES设置键-值对属性值，来描述这个数据库的属性信息。
# 数据库的其他元数据信息都是不可更改的，包括数据库名和数据库所在的目录位置。
alter database db_hive set dbproperties('createtime'='20170830');

# 4.4 删除数据库

1. 删除空数据库
drop database db_hive2;
2. 删除不空数据库
drop database db_hive cascade;

# 4.5创建表
# 1.建表语法
CREATE [EXTERNAL] TABLE [IF NOT EXISTS] table_name;
[(col_name data_type [COMMENT col_comment], ...)] 
[COMMENT table_comment] 
[PARTITIONED BY (col_name data_type [COMMENT col_comment], ...)] 
[CLUSTERED BY (col_name, col_name, ...) 
[SORTED BY (col_name [ASC|DESC], ...)] INTO num_buckets BUCKETS] 
[ROW FORMAT row_format] 
[STORED AS file_format] 
[LOCATION hdfs_path]
[TBLPROPERTIES (property_name=property_value, ...)]
[AS select_statement]
# 2.管理表（内部表）
# 3.外部表
# 因为表是外部表，所以Hive并非认为其完全拥有这份数据。
# 删除该表并不会删除掉这份数据，不过描述表的元数据信息会被删除掉。
# 创建外部表-上传数据到HDFS/创建外部表
dfs -mkdir /student;
dfs -mkdir /student;
create external table stu_external(
id int, 
name string) 
row format delimited fields terminated by '\t' 
location '/student';
# 4.内部表与外部表的相互转化
alter table student2 set tblproperties('EXTERNAL'='TRUE');
alter table student2 set tblproperties('EXTERNAL'='FALSE');

# 4.6分区表
create table dept_partition(
deptno int, dname string, loc string
)
partitioned by (month string)
row format delimited fields terminated by '\t';

load data local inpath '/opt/module/datas/dept.txt' into table default.dept_partition partition(month='201709');
load data local inpath '/opt/module/datas/dept.txt' into table default.dept_partition partition(month='201708');
load data local inpath '/opt/module/datas/dept.txt' into table default.dept_partition partition(month='201707');

# 单分区查询
select * from dept_partition where month='201709';
# 多分区查询
select * from dept_partition where month='201709'
              union
              select * from dept_partition where month='201708'
              union
              select * from dept_partition where month='201707';
# 增加分区
alter table dept_partition add partition(month='201706') ;

删除分区
alter table dept_partition drop partition (month='201704');

# 创建二级分区
create table dept_partition2(
               deptno int, dname string, loc string
               )
               partitioned by (month string, day string)
               row format delimited fields terminated by '\t';

load data local inpath '/opt/module/datas/dept.txt' into table default.dept_partition2 partition(month='201709', day='13');

select * from dept_partition2 where month='201709' and day='13';
# 在现有数据情况下添加分区（3总方式）
# （1）创建文件夹后load到分区
dfs -mkdir -p /user/hive/warehouse/dept_partition2/month=201709/day=10;

load data local inpath '/opt/module/datas/dept.txt' into table dept_partition2 partition(month='201709',day='10');

# （2）上传数据后添加分区
dfs -mkdir -p /user/hive/warehouse/dept_partition2/month=201709/day=11;
alter table dept_partition2 add partition(month='201709', day='11');

# 4.7修改表
# 重命名表
ALTER TABLE table_name RENAME TO new_table_name;
# 添加列
alter table dept_partition add columns(deptdesc string);
# 更新列
alter table dept_partition change column deptdesc desc int;
# 替换列
alter table dept_partition replace columns(deptno string, dname string, loc string);


删除表 
drop table dept_partition;


```

## DML数据操作

```sql
# 第五章_DML数据操作
# 5.1 数据导入

# 5.1.1 数据装载（load)

load data [local] inpath '/opt/module/datas/student.txt' 
[overwrite] into table student [partition (partcol1=val1,…)];
# 有local表示从本地加载数据，否则从hdfs中加载

# 有overwrite表示覆写，如无则追加

load data local inpath '/opt/module/datas/student.txt' into table default.student;

# 5.1.2 插入数据（Insert）
insert into table  student partition(month='201709') values(1,'wangwu'),(2,’zhaoliu’);
insert overwrite table student partition(month='201708')
            select id, name from student where month='201709';
#into为追加，overwrite为覆写

# 5.1.3 查询语句中创建表并加载数据（As Select）
# 根据查询结果创建表
create table if not exists student3 as select id, name from student;

# 5.1.4 创建表时通过Location指定加载数据路径
dfs -mkdir /student;
dfs -put /opt/module/datas/student.txt /student;

create external table if not exists student5(
              id int, name string
              )
              row format delimited fields terminated by '\t'
              location '/student';
# 5.1.5 Import数据到指定的Hive中
import table student2 partition(month='201709') from '/user/hive/warehouse/export/student';

# 5.2 数据导出
# 5.2.1 Insert导出
# 查询结果格式化导出到本地
insert overwrite local directory '/opt/module/datas/export/student1'
           ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'             
           select * from student;
# 查询结果导出到HDFS
insert overwrite directory '/user/atguigu/student2'
             ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' 
             select * from student;
# 5.2.2 Hadoop导出到本地
dfs -get /user/hive/warehouse/student/month=201709/000000_0 /opt/module/datas/export/student3.txt;
# 5.2.3 Shell命令导出
bin/hive -e 'select * from default.student;' > /opt/module/datas/export/student4.txt;
# 5.2.4 Export导出到HDFS
export table default.student to '/user/hive/warehouse/export/student';

清除表数据
truncate table student;

```

## 查询

```sql
# 6.7 其他常用查询函数
# 6.7.1 空字段赋值
select comm,nvl(comm, -1) from emp;
select comm, nvl(comm,mgr) from emp;

# 6.7.2 CASE WHEN
# 1.导入数据
create table emp_sex(
name string, 
dept_id string, 
sex string) 
row format delimited fields terminated by "\t";
load data local inpath '/opt/module/datas/emp_sex.txt' into table emp_sex;

# 2.查询
select 
  dept_id,
  sum(case sex when '男' then 1 else 0 end) male_count,
  sum(case sex when '女' then 1 else 0 end) female_count
from 
  emp_sex
group by
  dept_id;

# 6.7.3 行转列
# 1.导入数据
create table person_info(
name string, 
constellation string, 
blood_type string) 
row format delimited fields terminated by "\t";
load data local inpath "/opt/module/datas/constellation.txt" into table person_info;

# 2.查询数据
select
    t1.base,
    concat_ws('|', collect_set(t1.name)) name
from
    (select
        name,
        concat(constellation, ",", blood_type) base
    from
        person_info) t1
group by
    t1.base;

# 6.7.4 列转行
# 1.导入数据
create table movie_info(
    movie string, 
    category array<string>) 
row format delimited fields terminated by "\t"
collection items terminated by ",";
load data local inpath "/opt/module/datas/movie.txt" into table movie_info;

# 2.查询
select
    movie,
    category_name
from 
    movie_info lateral view explode(category) table_tmp as category_name;

# 6.7.5 窗口函数
# 1.导入数据
create table business(
name string, 
orderdate string,
cost int
) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',';
load data local inpath "/opt/module/datas/business.txt" into table business;

# 2.查询
# (1)查询在2017年4月份购买过的顾客及总人数
select name,count(*) over () 
from business 
where substring(orderdate,1,7) = '2017-04' 
group by name;

# (2)查询顾客的购买明细及月购买总额
select name,orderdate,cost,sum(cost) over(partition by month(orderdate)) 
from business;

#（3）上述的场景, 将每个顾客的cost按照日期进行累加
select name,orderdate,cost, 
sum(cost) over(partition by name order by orderdate rows between UNBOUNDED PRECEDING and current row ) as sample4,
from business;

#（4）查看顾客上次的购买时间
select name,orderdate,cost, 
lag(orderdate,1,'1900-01-01') over(partition by name order by orderdate ) as time1, 
lag(orderdate,2) over (partition by name order by orderdate) as time2 
from business;

#（5）查询前20%时间的订单信息
select * from (
    select name,orderdate,cost, ntile(5) over(order by orderdate) sorted
    from business
) t
where sorted = 1;

# 6.7.6 Rank
# 1.导入数据
create table score(
name string,
subject string, 
score int) 
row format delimited fields terminated by "\t";
load data local inpath '/opt/module/datas/score.txt' into table score;

# 2.查询
select name,
subject,
score,
rank() over(partition by subject order by score desc) rp,
dense_rank() over(partition by subject order by score desc) drp,
row_number() over(partition by subject order by score desc) rmp
from score;







```

```sql
# 第六章_查询
# 6.1.1 全表查询和特定列查询
# 1.创建表
create table if not exists dept(
deptno int,
dname string,
loc int
)
row format delimited fields terminated by '\t';

create table if not exists emp(
empno int,
ename string,
job string,
mgr int,
hiredate string, 
sal double, 
comm double,
deptno int)
row format delimited fields terminated by '\t';

load data local inpath '/opt/module/datas/dept.txt' into table dept;
load data local inpath '/opt/module/datas/emp.txt' into table emp;
# 2.查询
select * from emp;
select empno, ename from emp;

# 6.1.2 列别名
select ename AS name, deptno dn from emp;

# 6.1.3 算术运算符

# 6.1.4 常用函数
select count(*) cnt from emp;
select max(sal) max_sal from emp;
select min(sal) min_sal from emp;
select sum(sal) sum_sal from emp;
select avg(sal) avg_sal from emp;

# 6.1.5 Limit语句
select * from emp limit 5;



# 6.2 Where语句
select * from emp where sal >1000;

# 6.2.1 比较运算符（Between/In/ Is Null）
select * from emp where sal between 500 and 1000;
select * from emp where comm is null;
select * from emp where sal IN (1500, 5000);

# 6.2.2 Like和RLike
select * from emp where sal LIKE '2%';
select * from emp where sal LIKE '_2%';
select * from emp where sal RLIKE '[2]';
# RLike通过Java正则表达式来指定匹配条件

# 6.2.3 逻辑运算符（And/Or/Not)
select * from emp where sal>1000 and deptno=30;
select * from emp where sal>1000 or deptno=30;
select * from emp where deptno not IN(30, 20);

# 6.3 分组
# 6.3.1 Group By
select t.deptno, avg(t.sal) avg_sal from emp t group by t.deptno;
select t.deptno, t.job, max(t.sal) max_sal from emp t group by t.deptno, t.job;

# 6.3.2 Having语句(having只用于group by分组统计语句)
select deptno, avg(sal) avg_sal from emp group by deptno having avg_sal > 2000;

# 6.4 Join语句
# 6.4.1 等值Join
select e.empno, e.ename, d.deptno, d.dname from emp e join dept d on e.deptno = d.deptno;

# 6.4.2 内连接
select e.empno, e.ename, d.deptno from emp e join dept d on e.deptno = d.deptno;

# 6.4.3 左外连接
select e.empno, e.ename, d.deptno from emp e left join dept d on e.deptno = d.deptno;

# 6.4.4 右外连接
select e.empno, e.ename, d.deptno from emp e right join dept d on e.deptno = d.deptno;

# 6.4.5 满外链接
# 将会返回所有表中符合WHERE语句条件的所有记录。如果任一表的指定字段没有符合条件的值的话，那么就使用NULL值替代。
select e.empno, e.ename, d.deptno from emp e full join dept d on e.deptno = d.deptno;

# 6.4.6 多表链接
# 注意：连接 n个表，至少需要n-1个连接条件。例如：连接三个表，至少需要两个连接条件。
create table if not exists location(
loc int,
loc_name string
)
row format delimited fields terminated by '\t';

load data local inpath '/opt/module/datas/location.txt' into table location;

SELECT e.ename, d.dname, l.loc_name
FROM   emp e 
JOIN   dept d
ON     d.deptno = e.deptno 
JOIN   location l
ON     d.loc = l.loc;

# 6.4.7 笛卡尔积
# 1.笛卡尔集会在下面条件下产生
#（1）省略连接条件
#（2）连接条件无效
#（3）所有表中的所有行互相连接 
# 错误示范如下：
select empno, dname from emp, dept;

# 6.4.8 连接谓词中不支持or
select e.empno, e.ename, d.deptno from emp e join dept d on e.deptno = d.deptno or e.ename=d.ename;

# 6.5 排序
# 6.5.1 全局排序（Order By）——只有一个Reducer
select * from emp order by sal;
select * from emp order by sal desc;

# 6.5.2 多个列排序
select ename, deptno, sal from emp order by deptno, sal;

# 6.5.3 每个MR内部排序（Sort By）
set mapreduce.job.reduces = 3;
set mapreduce.job.reduces;
select * from emp sort by deptno desc;

# 6.5.4 分区排序（Distributed By）——类似于MR中的partition
set mapreduce.job.reduces=3;
insert overwrite local directory '/opt/module/datas/distribute-result' 
	select * from emp distribute by deptno sort by empno desc;
# 1．distribute by的分区规则是根据分区字段的hash码与reduce的个数进行模除后，余数相同的分到一个区。
# 2．Hive要求DISTRIBUTE BY语句要写在SORT BY语句之前

# 6.5.5 Cluster By
# 当distribute by和sorts by字段相同时，可以使用cluster by方式。
# 排序只能是升序排序，不能指定排序规则为ASC或者DESC。
select * from emp cluster by deptno;
select * from emp distribute by deptno sort by deptno;

分桶及抽样查询

分区针对的是数据的存储路径；分桶针对的是数据文件。
set hive.enforce.bucketing = true;
set mapreduce.job.reduces = -1;

insert into table stu_buck
select id, name from stu;

分桶抽样查询
select * from stu_buck tablesample(bucket 1 out of 4 on id);

```

## 函数

```sql
# 第七章_函数
# 7.1 系统自带函数

show functions;

desc function extended upper;

# 7.2 自定义函数
# 1.三类UDF（user-defined function）
# （1）UDF——一进一出
# （2）UDAF（User-Defined Aggregation Function)——多进一出，类似于(count/max/min)
# （3）UDTF（User-Defined Table-Generating Functions)——一进多出，（lateral view explore()）

# 2.编程步骤
#（1）继承org.apache.hadoop.hive.ql.exec.UDF
#（2）需要实现evaluate函数；evaluate函数支持重载；
#（3）在hive的命令行窗口创建函数
# 1）添加jar
add jar /opt/module/datas/udf.jar
# 2）创建function
create [temporary] function [dbname.]function_name AS class_name;
create temporary function mylower as "com.atguigu.hive.Lower";
# drop [temporary] function [if exists] [dbname.]function_name;
select ename, mylower(ename) lowername from emp;
# UDF必须要有返回类型，可以返回null，但是返回类型不能为void;

```

## 压缩和存储

```sql
# 第八章_压缩和存储
# 8.1 Hadoop源码编译支持Snappy压缩（详参Hive文档）
# 8.2 Hadoop压缩配置
# 8.3 开启Map输出阶段压缩
# 开启map输出阶段压缩可以减少job中map和Reduce task间数据传输量。
set hive.exec.compress.intermediate=true;
set mapreduce.map.output.compress=true;
set mapreduce.map.output.compress.codec=org.apache.hadoop.io.compress.SnappyCodec;

# 8.4 开启Reduce输出阶段压缩
set hive.exec.compress.output=true;
set mapreduce.output.fileoutputformat.compress=true;
set mapreduce.output.fileoutputformat.compress.codec =org.apache.hadoop.io.compress.SnappyCodec;
set mapreduce.output.fileoutputformat.compress.type=BLOCK;

# 8.5 文件存储格式
# Hive支持的存储数据的格式主要有：TEXTFILE 、SEQUENCEFILE、ORC、PARQUET
# 8.5.1 列式存储和行式存储



```

## 企业级调优

```sql
# 第九章_企业级调优
# 9.1 Fetch抓取
# Fetch抓取是指，Hive中对某些情况的查询可以不必使用MapReduce计算。
# 例如：SELECT * FROM employees;
# 在这种情况下，Hive可以简单地读取employee对应的存储目录下的文件
# 然后输出查询结果到控制台。
set hive.fetch.task.conversion=more;
select * from emp;
select ename from emp;
select ename from emp limit 3;

# 9.2 本地模式
# Hive可以通过本地模式在单台机器上处理所有的任务。
# 对于小数据集，执行时间可以明显被缩短。
set hive.exec.mode.local.auto=true;
set hive.exec.mode.local.auto.inputbytes.max=50000000;
set hive.exec.mode.local.auto.input.files.max=10;

# 9.3 表的优化
# 9.3.1 小表、大表 Join
# 最好小表在左边，大表在右，新版Hive没有明显区别了

# 9.3.2 大表 Join 大表
# （1）空Key过滤
# 1）配置mapred-site.xml以启用历史服务器
# <property>
# <name>mapreduce.jobhistory.address</name>
# <value>hadoop102:10020</value>
# </property>
# <property>
#     <name>mapreduce.jobhistory.webapp.address</name>
#     <value>hadoop102:19888</value>
# </property>
# sbin/mr-jobhistory-daemon.sh start historyserver

insert overwrite table jointable select n.* from (select * from nullidtable where id is not null ) n  left join ori o on n.id = o.id;

#（2）空Key转换
set mapreduce.job.reduces = 5;
insert overwrite table jointable
select n.* from nullidtable n full join ori o on 
case when n.id is null then concat('hive', rand()) else n.id end = o.id;

# 9.3.3 MapJoin(小表join大表)
set hive.auto.convert.join = true;
set hive.mapjoin.smalltable.filesize=25000000;

# 9.3.4 Group By
set hive.map.aggr = true;
set hive.groupby.mapaggr.checkinterval = 100000;
set hive.groupby.skewindata = true;

# 9.3.5 Count(Distinct) 去重统计
set mapreduce.job.reduces = 5;
select count(id) from (select id from bigtable group by id) a;

# 9.3.6 笛卡尔积

# 9.3.7 行列过滤

# 9.3.8 动态分区调整（Dynamic Partition）
hive.exec.dynamic.partition=true
hive.exec.dynamic.partition.mode=nonstrict
insert into table dept_partition partition(location) select deptno, dname, loc from dept;

# 9.3.9 分桶
# 9.3.10 分区

# 9.4 合理设置 Map 及 Reduce 数
# 9.4.1 复杂文件增加 Map 数
computeSliteSize(Math.max(minSize,Math.min(maxSize,blocksize)))=blocksize=128M
# maxSize最大值低于blocksize即可增加map个数

# 9.4.2 小文件进行合并
set hive.input.format= org.apache.hadoop.hive.ql.io.CombineHiveInputFormat;
SET hive.merge.mapfiles = true;
SET hive.merge.mapredfiles = true;
SET hive.merge.size.per.task = 268435456;
SET hive.merge.smallfiles.avgsize = 16777216;

# 9.4.3 合理设置Reduce数
set mapreduce.job.reduces = 15;
# 详细算法见MR理论文件

# 9.5 并行执行
set hive.exec.parallel=true;

# 9.6 严格模式
<property>
    <name>hive.mapred.mode</name>
    <value>strict</value>
    <description>
      The mode in which the Hive operations are being performed. 
      In strict mode, some risky queries are not allowed to run. They include:
        Cartesian Product.
        No partition being picked up for a query.
        Comparing bigints and strings.
        Comparing bigints and doubles.
        Orderby without limit.
</description>
</property>

# 9.7 JVM重用
# mapred-site.xml
<property>
  <name>mapreduce.job.jvm.numtasks</name>
  <value>10</value>
  <description>How many tasks to run per jvm. If set to -1, there is
  no limit. 
  </description>
</property>

# 9.8 推测执行（Speculative Execution）
# mapred-site.xml
<property>
  <name>mapreduce.map.speculative</name>
  <value>true</value>
  <description>If true, then multiple instances of some map tasks 
               may be executed in parallel.</description>
</property>

<property>
  <name>mapreduce.reduce.speculative</name>
  <value>true</value>
  <description>If true, then multiple instances of some reduce tasks 
               may be executed in parallel.</description>
</property>

# 9.9 执行计划(Explain)
explain extended select * from emp;

```