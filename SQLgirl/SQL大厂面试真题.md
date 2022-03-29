
[Hive SQL面试题(附答案)](https://mp.weixin.qq.com/s/4C4wQdaLdtLuTADi16dEsA)

## 手写 HQL 第 1 题 +

```sql
表结构：uid,subject_id,score
求：找出所有科目成绩都大于某一学科平均成绩的学生
数据集如下


1001 01 90
1001 02 90
1001 03 90
1002 01 85
1002 02 85
1002 03 70
1003 01 70
1003 02 70
1003 03 85

我的答案
select
    uid
from(
    select 
        uid,
        score
        avg(score) over(partition by subject_id) avg_score
    from score
    ) t1
group by uid having sum(if(score>avg_score,1,0)) = 3


1）建表语句


create table score(
 uid string,
 subject_id string,
 score int)
row format delimited fields terminated by '\t'; 


2）求出每个学科平均成绩


select
 uid,
 score,
 avg(score) over(partition by subject_id) avg_score
from
 score;t1


3）根据是否大于平均成绩记录 flag，大于则记为 0 否则记为 1


select
 uid,
 if(score > avg_score, 0, 1) flag
from
 t1;t2


4）根据学生 id 进行分组统计 flag 的和，和为 0 则是所有学科都大于平均成绩


select
 uid
from
 t2
group by
 uid
having
 sum(flag) = 0;


5）最终 SQL


select
    uid
from(
    select
        uid,
        if(score>avg_score,0,1) flag
    from(  
        select
            uid,
            score,
            avg(score) over(partition by subject_id) avg_score
        from
            score
        )t1
    )t2
group by uid having sum(flag)=0;

```

## 手写 HQL 第 2 题 +

```sql
我们有如下的用户访问数据

userId visitDate visitCount
u01 2017/1/21 5
u02 2017/1/23 6
u03 2017/1/22 8
u04 2017/1/20 3
u01 2017/1/23 6
u01 2017/2/21 8
U02 2017/1/23 6
U01 2017/2/22 4

要求使用 SQL 统计出每个用户的累积访问次数，如下表所示：

用户 id 月份 小计 累积
u01 2017-01 11 11
u01 2017-02 12 23
u02 2017-01 12 12
u03 2017-01 8 8
u04 2017-01 3 3

数据集

u01 2017/1/21 5
u02 2017/1/23 6
u03 2017/1/22 8
u04 2017/1/20 3
u01 2017/1/23 6
u01 2017/2/21 8
u02 2017/1/23 6
u01 2017/2/22 4

select
    userId
    mn
    cnt
    sum(cnt) over(partition by userId order by mn)
from(
    select
        userId
        mn
        sum(visitCount) cnt
    from(
        select
            userId,
            date_format(regexp_replace(visitDate,'/','-'), 'yyyy-MM') mn
            visitCount
        from
            action
        )t1
    group by userId, mn
    )t2


1）创建表


create table action
    (userId string,
    visitDate string,
    visitCount int) 
row format delimited fields terminated by "\t";


2）修改数据格式


select
    userId,
    date_format(regexp_replace(visitDate, '/', '-'), 'yyyy-MM') mn,
    visitCount
from
    action; t1


3）计算每人单月访问量


select
    userId,
    mn,
    sum(visitCount) mn_count
from
    t1
group by 
    userId, mn; t2


4）按月累计访问量


select
    userId,
    mn,
    mn_count,
    sum(mn_count) over(partition by userId order by mn)
from 
    t2;


5）最终 SQL


select
    userId,
    mn,
    mn_count,
    sum(mn_count) over(partition by userId order by mn)
from 
    ( select
        userId,
        mn,
        sum(visitCount) mn_count
    from
        (select
            userId,
            date_format(regexp_replace(visitDate,'/','-'),'yyyy-MM') mn,
            visitCount
        from
        action)t1
    group by userId,mn)t2;

```

## 手写 HQL 第 3 题 + 

```sql

有 50W 个京东店铺，每个顾客访客访问任何一个店铺的任何一个商品时都会产生一条访问日志，访问日志存
储的表名为 Visit，访客的用户 id 为 user_id，被访问的店铺名称为 shop，请统计：

1）每个店铺的 UV（访客数）
2）每个店铺访问次数 top3 的访客信息。输出店铺名称、访客 id、访问次数

数据集

u1 a
u2 b
u1 b
u1 a
u3 c
u4 b
u1 a
u2 c
u5 b
u4 b
u6 c
u2 c
u1 b
u2 a
u2 a
u3 a
u5 a
u5 a
u5 a

select
    shop,
    user_id,
    cnt
from(
    select
        shop,
        user_id,
        cnt,
        rank() over(partition by shop order by cnt) rnk
    from(
        select
            user_id
            shop
            count(user_id) cnt
        from visit
        group by shop, user_id
        )t1
    )t2
where rnk <= 3

1）建表

create table visit(
    user_id string,
    shop string) 
row format delimited fields terminated by '\t';


2）每个店铺的 UV（访客数）


select 
    shop,
    count(distinct user_id) 
from visit group by shop;


3）每个店铺访问次数 top3 的访客信息。输出店铺名称、访客 id、访问次数


（1）查询每个店铺被每个用户访问次数


select 
    shop,
    user_id,
    count(*) ct
from visit
group by shop,user_id;t1


（2）计算每个店铺被用户访问次数排名


select 
    shop,
    user_id,
    ct,
    rank() over(partition by shop order by ct) rk
from t1;t2


（3）取每个店铺排名前 3 的


select 
    shop,
    user_id,
    ct
from t2
where rk<=3;


（4）最终 SQL


select 
    shop,
    user_id,
    ct
from
    (select 
        shop,
        user_id,
        ct,
        rank() over(partition by shop order by ct) rk
    from 
        (select 
            shop,
            user_id,
            count(*) ct
        from visit
        group by 
            shop,
            user_id)t1
    )t2
where rk <= 3;
```

## 手写 HQL 第 4 题

```sql
已知一个表 STG.ORDER，有如下字段:Date，Order_id，User_id，amount。

请给出 sql 进行统计:数据样例:

2017-01-01,10029028,1000003251,33.57。


1）给出 2017 年每个月的订单数、用户数、总成交金额。
2）给出 2017 年 11 月的新客数(指在 11 月才有第一笔订单)


建表


create table order_tab(
    dt string,
    order_id string,
    user_id string,
    amount decimal(10,2)) 
row format delimited fields terminated by '\t';


1）给出 2017 年每个月的订单数、用户数、总成交金额。


select
    date_format(dt,'yyyy-MM'),
    count(order_id),
    count(distinct user_id),
    sum(amount)
from
    order_tab
where
    date_format(dt,'yyyy') = '2017'
group by
    date_format(dt,'yyyy-MM');


2）给出 2017 年 11 月的新客数(指在 11 月才有第一笔订单)


select
    count(user_id)
from
    order_tab
group by
    user_id
having
    date_format(min(dt),'yyyy-MM')='2017-11';


手写 HQL 第 5 题


有日志如下，请写出代码求得所有用户和活跃用户的总数及平均年龄。
（活跃用户指连续两天都有访问记录的用户）

日期 用户 年龄
数据集
2019-02-11,test_1,23
2019-02-11,test_2,19
2019-02-11,test_3,39
2019-02-11,test_1,23
2019-02-11,test_3,39
2019-02-11,test_1,23
2019-02-12,test_2,19
2019-02-13,test_1,23
2019-02-15,test_2,19
2019-02-16,test_2,19


1）建表


create table user_age(dt string,user_id string,age int)
row format delimited fields terminated by ',';


2）按照日期以及用户分组，按照日期排序并给出排名


select
 dt,
 user_id,
 min(age) age,
 rank() over(partition by user_id order by dt) rk
from
 user_age
group by
 dt,user_id;t1


3）计算日期及排名的差值


select
 user_id,
 age,
 date_sub(dt,rk) flag
from
 t1;t2


4）过滤出差值大于等于 2 的，即为连续两天活跃的用户


select
 user_id,
 min(age) age
from
 t2
group by
 user_id,flag
having
 count(*)>=2;t3


5）对数据进行去重处理（一个用户可以在两个不同的时间点连续登录），
例如：a 用户在 1 月 10 号 1 月 11 号以及 1 月 20 号和 1 月 21 号 4 天登录。


select
 user_id,
 min(age) age
from
 t3
group by
 user_id;t4


6）计算活跃用户（两天连续有访问）的人数以及平均年龄


select
 count(*) ct,
 cast(sum(age)/count(*) as decimal(10,2))
from t4;


7）对全量数据集进行按照用户去重


select
 user_id,
 min(age) age 
from
 user_age 
group by 
 user_id;t5


8）计算所有用户的数量以及平均年龄


select
 count(*) user_count,
 cast((sum(age)/count(*)) as decimal(10,1)) 
from 
 t5;


9）将第 5 步以及第 7 步两个数据集进行 union all 操作


select
 0 user_total_count,
 0 user_total_avg_age,
 count(*) twice_count,
 cast(sum(age)/count(*) as decimal(10,2)) twice_count_avg_age
from 
(
 select
 user_id,
 min(age) age
from
 (select
 user_id,
 min(age) age
from
 (
 select
 user_id,
 age,
 date_sub(dt,rk) flag
from
 (
 select
 dt,
 user_id,
 min(age) age,
 rank() over(partition by user_id order by dt) rk
 from
 user_age
 group by
 dt,user_id
 )t1
 )t2
group by
 user_id,flag
having
 count(*)>=2)t3
group by
 user_id 
)t4
union all
select
 count(*) user_total_count,
 cast((sum(age)/count(*)) as decimal(10,1)),
 0 twice_count,
 0 twice_count_avg_age
from 
 (
 select
 user_id,
 min(age) age 
 from 
 user_age 
 group by 
 user_id
 )t5;t6


10）求和并拼接为最终 SQL


select 
 sum(user_total_count),
 sum(user_total_avg_age),
 sum(twice_count),
 sum(twice_count_avg_age)
from 
(select
 0 user_total_count,
 0 user_total_avg_age,
 count(*) twice_count,
 cast(sum(age)/count(*) as decimal(10,2)) twice_count_avg_age
from 
(
 select
 user_id,
 min(age) age
from
 (select
 user_id,
 min(age) age
from
 (
 select
 user_id,
 age,
 date_sub(dt,rk) flag
from
 (
 select
 dt,
 user_id,
 min(age) age,
 rank() over(partition by user_id order by dt) rk
 from
 user_age
 group by
 dt,user_id
 )t1
 )t2
group by
 user_id,flag
having
 count(*)>=2)t3
group by
 user_id 
)t4
union all
select
 count(*) user_total_count,
 cast((sum(age)/count(*)) as decimal(10,1)),
 0 twice_count,
 0 twice_count_avg_age
from 
 (
 select
 user_id,
 min(age) age 
 from 
 user_age 
 group by 
 user_id
 )t5)t6;

```

## 手写 HQL 第 6 题

```sql
请用 sql 写出所有用户中在今年 10 月份第一次购买商品的金额，表 ordertable 字段
（购买用户：userid，金额：money，购买时间：paymenttime(格式：2017-10-01)，订单 id：orderid）


1）建表


create table ordertable(
 userid string,
 money int,
 paymenttime string,
 orderid string)
row format delimited fields terminated by '\t';


2）查询出


select
 userid,
 min(paymenttime) paymenttime
from
 ordertable
where
 date_format(paymenttime,'yyyy-MM')='2017-10'
group by
 userid;t1
select
 t1.userid,
 t1.paymenttime,
 od.money
from
 t1
join
 ordertable od
on
 t1.userid=od.userid
 and
 t1.paymenttime=od.paymenttime;
select
 t1.userid,
 t1.paymenttime,
 od.money
from
 (select
 userid,
 min(paymenttime) paymenttime
from
 ordertable
where
 date_format(paymenttime,'yyyy-MM')='2017-10'
group by
 userid)t1
join
 ordertable od
on
 t1.userid=od.userid
 and
 t1.paymenttime=od.paymenttime;
```

## 手写 HQL 第 7 题

```sql
有一个线上服务器访问日志格式如下（用 sql 答题）


时间 接口 ip 地址


2016-11-09 11：22：05 /api/user/login 110.23.5.33
2016-11-09 11：23：10 /api/user/detail 57.3.2.16
.....
2016-11-09 23：59：40 /api/user/login 200.6.5.166


求 11 月 9 号下午 14 点（14-15 点），访问 api/user/login 接口的 top10 的 ip 地址


数据集


2016-11-09 14:22:05 /api/user/login 110.23.5.33
2016-11-09 11:23:10 /api/user/detail 57.3.2.16
2016-11-09 14:59:40 /api/user/login 200.6.5.166
2016-11-09 14:22:05 /api/user/login 110.23.5.34
2016-11-09 14:22:05 /api/user/login 110.23.5.34
2016-11-09 14:22:05 /api/user/login 110.23.5.34
2016-11-09 11:23:10 /api/user/detail 57.3.2.16
2016-11-09 23:59:40 /api/user/login 200.6.5.166
2016-11-09 14:22:05 /api/user/login 110.23.5.34
2016-11-09 11:23:10 /api/user/detail 57.3.2.16
2016-11-09 23:59:40 /api/user/login 200.6.5.166
2016-11-09 14:22:05 /api/user/login 110.23.5.35
2016-11-09 14:23:10 /api/user/detail 57.3.2.16
2016-11-09 23:59:40 /api/user/login 200.6.5.166
2016-11-09 14:59:40 /api/user/login 200.6.5.166
2016-11-09 14:59:40 /api/user/login 200.6.5.166


1）建表


create table ip(
 time string,
 interface string,
 ip string)
row format delimited fields terminated by '\t';


2）最终 SQL


select
 ip,
 interface,
 count(*) ct
from
 ip
where
 date_format(time,'yyyy-MM-dd HH')>='2016-11-09 14'
 and 
 date_format(time,'yyyy-MM-dd HH')<='2016-11-09 15'
 and
 interface='/api/user/login'
group by
ip,interface
order by
 ct desc
limit 2;t1

```

## 手写 SQL 第 8 题

```sql
有一个账号表如下，请写出 SQL 语句，查询各自区组的 money 排名前十的账号（分组取前 10）


1）建表（MySQL）


CREATE TABLE `account`
( `dist_id` int（11）DEFAULT NULL COMMENT '区组 id',
 `account` varchar（100）DEFAULT NULL COMMENT '账号',
 `gold` int（11）DEFAULT 0 COMMENT '金币'）;


2）最终 SQL


select
 *
from
 account as a
where
 (select
 count(distinct(a1.gold))
 from
 account as a1 
 where
 a1.dist_id=a.dist_id
 and
 a1.gold>a.gold)<3;
```

## 手写 HQL 第 9 题

```sql
1）有三张表分别为会员表（member）销售表（sale）退货表（regoods）
（1）会员表有字段 memberid（会员 id，主键）credits（积分）；
（2）销售表有字段 memberid（会员 id，外键）购买金额（MNAccount）；
（3）退货表中有字段 memberid（会员 id，外键）退货金额（RMNAccount）。


2）业务说明
（1）销售表中的销售记录可以是会员购买，也可以是非会员购买。（即销售表中的 memberid 可以为空）；
（2）销售表中的一个会员可以有多条购买记录；
（3）退货表中的退货记录可以是会员，也可是非会员；
（4）一个会员可以有一条或多条退货记录。


查询需求：分组查出销售表中所有会员购买金额，同时分组查出退货表中所有会员的退货金额，把会员 id 相同
的购买金额-退款金额得到的结果更新到表会员表中对应会员的积分字段（credits）


数据集


sale
1001 50.3
1002 56.5
1003 235
1001 23.6
1005 56.2
 25.6
 33.5


regoods
1001 20.1
1002 23.6
1001 10.1
 23.5
 10.2
1005 0.8


1）建表


create table member(memberid string,credits double) 
row format delimited fields terminated by '\t';
create table sale(memberid string,MNAccount double) 
row format delimited fields terminated by '\t';
create table regoods(memberid string,RMNAccount double) 
row format delimited fields terminated by '\t';


2）最终 SQL


insert into table member
select
 t1.memberid,
 MNAccount-RMNAccount
from
 (select
 memberid,
 sum(MNAccount) MNAccount
 from
 sale
 where
 memberid!=''
 group by
 memberid
 )t1
join
 (select
 memberid,
 sum(RMNAccount) RMNAccount
 from
 regoods
 where
 memberid!=''
 group by
 memberid
 )t2
on
 t1.memberid=t2.memberid;

```

## 手写 HQL 第 10 题

```sql
1.用一条 SQL 语句查询出每门课都大于 80 分的学生姓名


name kecheng fenshu
张三 语文 81
张三 数学 75
李四 语文 76
李四 数学 90
王五 语文 81
王五 数学 100
王五 英语 90


A: select distinct name from table where name not in (select distinct name from table where fenshu<=80)


B：select name from table group by name having min(fenshu)>80


2. 学生表 如下:


自动编号 学号 姓名 课程编号 课程名称 分数
1 2005001 张三 0001 数学 69
2 2005002 李四 0001 数学 89
3 2005001 张三 0001 数学 69


删除除了自动编号不同, 其他都相同的学生冗余信息


A: delete tablename where 自动编号 not in(select min(自动编号) from tablename group by 学号, 姓名, 课程编号, 课程名称, 分数)


3.一个叫 team 的表，里面只有一个字段 name,一共有 4 条纪录，分别是 a,b,c,d,对应四个球队，现在四个球队进
行比赛，用一条 sql 语句显示所有可能的比赛组合.


答：select a.name, b.name
from team a, team b
where a.name < b.name


4.面试题：怎么把这样一个


year month amount
1991 1 1.1
1991 2 1.2
1991 3 1.3
1991 4 1.4
1992 1 2.1
1992 2 2.2
1992 3 2.3
1992 4 2.4


查成这样一个结果


year m1 m2 m3 m4
1991 1.1 1.2 1.3 1.4
1992 2.1 2.2 2.3 2.4


答案


select year,
(select amount from aaa m where month=1 and m.year=aaa.year) as m1,
(select amount from aaa m where month=2 and m.year=aaa.year) as m2,
(select amount from aaa m where month=3 and m.year=aaa.year) as m3,
(select amount from aaa m where month=4 and m.year=aaa.year) as m4
from aaa group by year



*********************************************************************



5.说明：复制表(只复制结构,源表名：a 新表名：b)

SQL: select * into b from a where 1<>1 (where1=1，拷贝表结构和数据内容)


ORACLE:

create table b
As
Select * from a where 1=2


[<>（不等于）(SQL Server Compact)
比较两个表达式。 当使用此运算符比较非空表达式时，如果左操作数不等于右操作数，则结果为 TRUE。 否则，
结果为 FALSE。]


6. 原表:
courseid coursename score
-------------------------------------
1 java 70
2 oracle 90
3 xml 40
4 jsp 30
5 servlet 80
-------------------------------------


为了便于阅读,查询此表后的结果显式如下(及格分数为 60):
courseid coursename score mark
---------------------------------------------------
1 java 70 pass
2 oracle 90 pass
3 xml 40 fail
4 jsp 30 fail
5 servlet 80 pass
---------------------------------------------------


写出此查询语句


select courseid, coursename ,score ,if(score>=60, "pass","fail") as mark from course


7.表名：购物信息


购物人 商品名称 数量
A 甲 2
B 乙 4
C 丙 1
A 丁 2
B 丙 5
……


给出所有购入商品为两种或两种以上的购物人记录


答：

select * from 购物信息 where 购物人 in (select 购物人 from 购物信息 group by 购物人 having 
count(*) >= 2);


8. info 表


date result
2005-05-09 win
2005-05-09 lose 
2005-05-09 lose 
2005-05-09 lose 
2005-05-10 win 
2005-05-10 lose 
2005-05-10 lose 


如果要生成下列结果, 该如何写 sql 语句? 
 win lose
2005-05-09 2 2 
2005-05-10 1 2 


答案：


(1) 

select date, sum(case when result = "win" then 1 else 0 end) as "win", sum(case when result = "lose" 
then 1 else 0 end) as "lose" from info group by date; 


(2) 

select a.date, a.result as win, b.result as lose 
 from 
 (select date, count(result) as result from info where result = "win" group by date) as a 
 join 
 (select date, count(result) as result from info where result = "lose" group by date) as b 
on a.date = b.date;


```

## 手写 HQL 第 11 题

```sql

有一个订单表 order。

已知字段有：

order_id(订单 ID), user_id(用户 ID),amount(金额), pay_datetime(付费时间), channel_id(渠道 ID), dt(分区字段)。


1. 在 Hive 中创建这个表。
2. 查询 dt=‘2018-09-01‘里每个渠道的订单数，下单人数（去重），总金额。
3. 查询 dt=‘2018-09-01‘里每个渠道的金额最大 3 笔订单。
4. 有一天发现订单数据重复，请分析原因


create external table order(
order_id int,
user_id int,
amount double,
pay_datatime timestamp,
channel_id int
)partitioned by(dt string)
row format delimited fields terminated by '\t';
select
count(order_id),
count(distinct(user_id))
sum(amount)
from
order
where dt="2019-09-01"
select
order_id
channel_id
channel_id_amount
from(
select
order_id
channel_id,
amount,
max(amount) over(partition by channel_id)
min(amount) over(partition by channel_id)
row_number()
over(
partition by channel_id
order by amount desc
)rank
from
order
where dt="2019-09-01"
)t
where t.rank<4


订单属于业务数据，在关系型数据库中不会存在数据重复
hive 建表时也不会导致数据重复，
我推测是在数据迁移时，迁移失败导致重复迁移数据冗余了


t_order 订单表
order_id,//订单 id
item_id, //商品 id
create_time,//下单时间
amount//下单金额
t_item 商品表
item_id,//商品 id
item_name,//商品名称
category//品类
t_item 商品表
item_id,//商品 id
item_name,//名称
category_1,//一级品类
category_2,//二级品类


1. 最近一个月，销售数量最多的 10 个商品


select
item_id,
count(order_id)a
from 
t_order
where
dataediff(create_time,current_date)<=30
group by 
item_id
order by a desc;


2. 最近一个月，每个种类里销售数量最多的 10 个商品
#一个订单对应一个商品 一个商品对应一个品类


with(
select
order_id,
item_id,
item_name,
category
from
t_order
join
t_item
on
t_order.item_id = t_item.item_id
) t
select
order_id,
item_id,
item_name,
category,
count(item_id)over(
partition by category
)item_count
from
t
group by category
order by item_count desc
limit 10;


计算平台的每一个用户发过多少日记、获得多少点赞数


with t3 as(
select * from 
t1 left join t2 
on t1.log_id = t2.log_id
)
select
uid,//用户 Id
count(log_id)over(partition by uid)log_cnt,//
count(like_uid)over(partition by log_id)liked_cnt//获得多少点赞数
from
t3


处理产品版本号


1、需求 A:找出 T1 表中最大的版本号
思路：列转行 切割版本号 一列变三列
主版本号 子版本号 阶段版本号



with t2 as(//转换
select
v_id v1,//版本号
v_id v2 //主
from
t1
lateral view explode(v2) tmp as v2
)
select //第一层 找出第一个
v1,
max(v2)
from 
t2

1、需求 A:找出 T1 表中最大的版本号


select
v_id,//版本号
max(split(v_id,".")[0]) v1,//主版本不会为空
max(if(split(v_id,".")[1]="",0,split(v_id,".")[1]))v2,//取出子版本并判断是否为空，并给默认值
max(if(split(v_id,".")[2]="",0,split(v_id,".")[2]))v3//取出阶段版本并判断是否为空，并给默认
值
from
t1


2、需求 B：计算出如下格式的所有版本号排序，要求对于相同的版本号，顺序号并列：


select
v_id,
rank() over(partition by v_id order by v_id)seq
from
t1
```


## SQL1 各个视频的平均完播率

https://www.nowcoder.com/practice/96263162f69a48df9d84a93c71045753?tpId=268&tqId=2285032&ru=/exam/oj&qru=/ta/sql-factory-interview/question-ranking&sourceUrl=%2Fexam%2Foj%3Ftab%3DSQL%25E7%25AF%2587%26topicId%3D268

完播率计算：

```sql
SELECT DATEDIFF('2018-07-01','2018-07-04');
运行结果:-3
select TIMESTAMPDIFF(DAY,'2018-07-01 09:00:00','2018-07-04 12:00:00');
运行结果:3


case when timestampdiff(second, log.start_time, log.end_time) >= info.duration then 1 else 0 end
if(timestampdiff(second, log.start_time, log.end_time) >= info.duration, 1, 0)
```

```sql
avg(xxx) 等效于 
sum(xxx)/count(log.uid) 等效于 
sum(xxx)/count(log.video_id) 等效于 
sum(xxx)/count(*)

where substr(start_time, 1, 4) = 2021 等效于 
where year(log.start_time) = 2021
```

```sql
select 
    log.video_id,
    round(sum(if(TIMESTAMPDIFF(second, log.start_time, log.end_time)
                >= info.duration, 1, 0))/count(log.video_id) ,3)
as avg_comp_play_rate
from tb_user_video_log as log inner join tb_video_info as info
on log.video_id = info.video_id
where year(log.start_time)=2021
group by video_id 
order by avg_comp_play_rate desc
```

## SQL2 平均播放进度大于60%的视频类别

https://www.nowcoder.com/practice/c60242566ad94bc29959de0cdc6d95ef?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
播放进度：
case
    when TIMESTAMPDIFF(SECOND, start_time, end_time) >= a.duration then 1 
    else TIMESTAMPDIFF(SECOND, start_time, end_time) / a.duration 
end 

等效于
if(TIMESTAMPDIFF(second, start_time, end_time) >= duration, 1, TIMESTAMPDIFF(second, start_time, end_time) / duration)
```

```sql
select 
    tag,
    concat(round(avg(if(TIMESTAMPDIFF(second,start_time,end_time)>=duration,1,
    TIMESTAMPDIFF(second,start_time,end_time)/duration))*100,2),'%') as avg_play_progress
from tb_user_video_log join tb_video_info t1 using(video_id)
group by tag
having avg(if(TIMESTAMPDIFF(second,start_time,end_time)>=duration,1,TIMESTAMPDIFF(second,start_time,end_time)/duration))>0.6
-- avg_played_progress 可以再算一遍
order by avg_play_progress desc
```

```sql
SELECT tag,
    concat(round(SUM(play)/COUNT(*)*100,2),'%') AS avg_played_progress
FROM(
    SELECT a.video_id,a.tag,
      case
          when TIMESTAMPDIFF(SECOND,start_time,end_time) >= a.duration then 1 
          else TIMESTAMPDIFF(SECOND,start_time,end_time)/a.duration 
      end as play
    from tb_video_info a
    left join tb_user_video_log b
    on a.video_id = b.video_id
) AS c
GROUP BY tag
HAVING avg_played_progress > 60
-- avg_played_progress 可以直接饮用
ORDER BY avg_played_progress desc
```

如下 CAST(xxx AS CHAR) 是可有可无的

```sql
SELECT tag,
CONCAT(CAST(
    round(100*AVG(IF(TIMESTAMPDIFF(second, start_time, end_time)<=duration,TIMESTAMPDIFF(second, start_time, end_time),duration)/duration),2)
AS CHAR),'%') avg_play_progress
FROM tb_user_video_log JOIN tb_video_info ON tb_user_video_log.video_id=tb_video_info.video_id
GROUP BY tag
HAVING AVG(IF(TIMESTAMPDIFF(second, start_time, end_time)<=duration,TIMESTAMPDIFF(second, start_time, end_time),duration)/duration)>0.6
ORDER BY avg_play_progress DESC
```

## SQL3 每类视频近一个月的转发量/率

https://www.nowcoder.com/practice/a78cf92c11e0421abf93762d25c3bfad?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
近一个月

DATEDIFF(
    DATE((select max(start_time) from tb_user_video_log)) ,
    DATE(start_time)
    ) <= 29

等效于

timestampdiff(
    day,
    start_time,
    (select max(start_time) from tb_user_video_log)
    ) <= 29
```


```sql
select 
    tag,
    sum(if_retweet) as retweet_count,
    round(sum(if_retweet) / count(*), 3) as retweet_rate
from tb_user_video_log t1
join tb_video_info t2 on t1.video_id = t2.video_id
where datediff(date((select max(start_time) from tb_user_video_log)), date(start_time)) <= 29
group by tag
order by retweet_rate desc
```

## SQL4 每个创作者每月的涨粉率及截止当前的总粉丝量

```sql
每月的涨粉率
DATE_FORMAT(start_time, '%Y-%m') month
DATE_FORMAT(end_time, "%Y-%m") AS month

sum(case when if_follow = 2 then -1 else if_follow end) 新增粉丝
sum(新增粉丝) over (partition by author order by month) 总粉丝
等效于
SUM(SUM(IF(if_follow = 2, -1, if_follow))) OVER (PARTITION BY author ORDER BY DATE_FORMAT(start_time, '%Y-%m'))
```

```sql
select 
    author
    ,month
    ,round(add_fans/counts,3) fans_growth_rate
    ,sum(add_fans) over(partition by author order by month) total_fans
from (
    select 
        author
        ,DATE_FORMAT(start_time,'%Y-%m') month
        ,sum(case when if_follow = 2 then -1 else if_follow end) add_fans
        ,count(*) counts
    from tb_user_video_log t1
    join tb_video_info t2 on t1.video_id = t2.video_id
    where year(start_time) = 2021
    group by author, month
) t
ORDER BY author, total_fans
```


## SQL5 国庆期间每类视频点赞量和转发量

注意时间的表达方式：

https://www.nowcoder.com/practice/f90ce4ee521f400db741486209914a11?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
order by dt desc 
rows between current row and 6 following

等效于
order by dt 
rows 6 preceding

SUM(likes) OVER (PARTITION BY tag ORDER BY dt ROWS 6 PRECEDING) AS sum_like_cnt_7d,
            -- 每类视频每天的近一周
```

```sql
SELECT tag, dt, sum_like_cnt_7d, max_retweet_cnt_7d
FROM (
    SELECT tag, dt,
           SUM(likes) OVER (PARTITION BY tag ORDER BY dt ROWS 6 PRECEDING) AS sum_like_cnt_7d,
            -- 每类视频每天的近一周
           MAX(retweets) OVER (PARTITION BY tag ORDER BY dt ROWS 6 PRECEDING) AS max_retweet_cnt_7d
    FROM (
        SELECT i.tag, DATE(l.end_time) AS dt,
               SUM(if_like) AS likes,
               SUM(if_retweet) AS retweets
        FROM tb_user_video_log l
        JOIN tb_video_info i
        ON l.video_id = i.video_id
        WHERE YEAR(end_time) = 2021
        GROUP BY i.tag, dt
    ) AS a
) AS b
WHERE dt BETWEEN "2021-10-01" AND "2021-10-03"
-- 最后才过滤，国庆头3天
ORDER BY tag DESC, dt ASC
```

## SQL6 近一个月发布的视频中热度最高的top3视频

https://www.nowcoder.com/practice/0226c7b2541c41e59c3b8aec588b09ff?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
timestampdiff(day,xxx,xxx) <= 29

等效于

datediff(xxx,xxx) <= 29
```

```sql
注意：

date ((select max(end_time) from tb_user_video_log))

和

max(date(end_time))

group by video_id

的区别


近一个月发布的视频：
datediff(date((select max(end_time) from tb_user_video_log)), date(release_time)) <= 29
datediff(date((select max(end_time) from tb_user_video_log)), max(date(end_time))) group by video_id 最近无播放天数
```

```sql
select 
    video_id,
    round((100*fin_rate + 5*like_sum + 3*comment_sum + 2*retweet_sum)/(unfin_day_cnt+1),0) hot_index
from(
  select 
    video_id,duration,
    avg(if(timestampdiff(second,start_time,end_time) >= duration, 1, 0)) fin_rate,
    sum(if_like) like_sum,
    count(comment_id) comment_sum,
    sum(if_retweet) retweet_sum,
    datediff(date((select max(end_time) from tb_user_video_log)), max(date(end_time))) unfin_day_cnt
  from tb_video_info
  join tb_user_video_log using(video_id)
  where datediff(date((select max(end_time) from tb_user_video_log)), date(release_time)) <= 29
  group by video_id
) t
order by hot_index DESC
limit 3
```


## SQL7 2021年11月每天的人均浏览文章时长

https://www.nowcoder.com/practice/8e33da493a704d3da15432e4a0b61bb3?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
人均

sum(TIMESTAMPDIFF(second,in_time,out_time))/count(distinct uid)

date_format(in_time, '%Y-%m-%d')
等效于
date(in_time)

DATE_FORMAT(tl.in_time, '%Y-%m') = '2021-11'
等效于
year(in_time) = 2021 and month(in_time) = 11
```

```sql
SELECT
     date(in_time)as dt,
     round(sum(TIMESTAMPDIFF(SECOND,in_time,out_time))/ COUNT(DISTINCT uid),1) as avg_view_len_sec
from tb_user_log
where date_format(in_time,"%Y-%m") = "2021-11" and artical_id  != 0
group by dt
order by avg_view_len_sec
```


## SQL8 每篇文章同一时刻最大在看人数

https://www.nowcoder.com/practice/fe24c93008b84e9592b35faa15755e48?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
注意一定要按照 in_time 和 out_time 的升序排序

利用 +1 和 -1 计数，即 uv

SUM(uv) OVER (PARTITION BY artical_id ORDER BY dt, uv desc)
```

```sql
with t as ((select
                artical_id,
                in_time tm,
                1 as uv
            from
                tb_user_log t
            where
                artical_id != '0')
            union all
            (select
                artical_id,
                out_time tm,
                -1 as uv
            from
                tb_user_log t
            where
                artical_id != '0'))

select 
    artical_id, max(sum_uv) as max_uv
from
    (select 
        artical_id,
        tm,
        sum(uv) over(partition by artical_id order by tm asc, uv desc) sum_uv
    from 
        t)t1
group by
    artical_id
order by
    max_uv desc

```

## SQL9 2021年11月每天新用户的次日留存率

https://www.nowcoder.com/practice/1fc0e75f07434ef5ba4f1fb2aa83a450?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
min(date(in_time)) group by uid, -- 用户的第一次登陆
count(uid) over (partition by min(date(in_time))) as cnt -- 每天的新用户数目
count(distinct b.uid)/count(distinct a.uid) 第二天的留存率

次日留存
left join -- 这里一定要用left join
on DATE_ADD(a.dt, INTERVAL 1 DAY) = b.dt and a.uid = b.uid
等效于
left join tb_user_log t2 
on t1.uid = t2.uid and date_add(t1.dt,interval 1 day) between date(t2.in_time) and date(t2.out_time)

WHERE a.dt like "2021-11%"
等效于
where date_format(t1.dt,'%Y-%m') = '2021-11'
```

```sql
select 
    a.dt, 
    round(count(distinct b.uid)/count(distinct a.uid),2) uv_left_rate
from (
    select uid , min(date(in_time)) dt
    from tb_user_log 
    group by uid ) a
    -- a 表示第一天
    left join -- 这里一定要用left join
        (select uid,date(in_time) dt
        from tb_user_log
        union 
        select uid,date(out_time) dt
        from tb_user_log) b
        -- b 表示第二天，用了union大法
    on DATE_ADD(a.dt,INTERVAL 1 DAY) = b.dt and a.uid = b.uid
where a.dt like '2021-11%'
group by a.dt 
order by a.dt
```



```sql
select 
    t1.dt,
    round(count(distinct t2.uid)/avg(t1.cnt),2) as uv_left_rate
from
    (select 
        uid,
        min(date(in_time)) as dt, -- 用户的第一次登陆
        count(uid) over (partition by min(date(in_time))) as cnt --每天的新用户数目
    from tb_user_log
    group by uid)t1
left join tb_user_log t2 
on t1.uid = t2.uid
and date_add(t1.dt,interval 1 day) between date(t2.in_time) and date(t2.out_time)
where date_format(t1.dt,'%Y-%m') = '2021-11'
group by t1.dt
order by t1.dt
```

## SQL10 统计活跃间隔对用户分级结果

https://www.nowcoder.com/practice/6765b4a4f260455bae513a60b6eed0af?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
全部用户：
select count(distinct uid) from tb_user_log
各个等级用户占比：
count(uid)/(全部用户) group by user_grade

最近日期：
select max(in_time) from tb_user_log
max(max(date(in_time))) over() group by uid


每个用户最近一次，最远一次登陆：
datediff((最近日期), max(in_time)) group by uid
datediff((最近日期), min(in_time)) group by uid
```

```sql
count(uid) over()
group by uid

和

count(uid)
group by uid

的区别
```

```sql
select user_grade,round(count(uid)/(select count(distinct uid) from tb_user_log),2) q
from  
    (
    select 
        uid,
        (case when datediff((select max(in_time) from tb_user_log),max(in_time)) <=6 
                and  datediff((select max(in_time) from tb_user_log),min(in_time)) >6 then '忠实用户'
                when datediff((select max(in_time) from tb_user_log),max(in_time)) <=6 
                and  datediff((select max(in_time) from tb_user_log),min(in_time)) <=6 then '新晋用户'
                when datediff((select max(in_time) from tb_user_log),max(in_time)) >6 
                and  datediff((select max(in_time) from tb_user_log),min(in_time)) <=29 then '沉睡用户'
                else '流失用户' end ) user_grade
    from tb_user_log
    group by uid
    ) f1
group by user_grade
order by q desc
``` 



```sql
select 
  (case
      when datediff(today, first_active_dt)<=6 then '新晋用户'  -- 按逻辑，不会出现负值
      -- 此处按case when执行顺序可以不再判断非新晋用户，但是建议写的健壮一些
      when datediff(today, last_active_dt)<=6 and datediff(today, first_active_dt)>6 then '忠实用户' 
      when datediff(today, last_active_dt) between 7 and 29 then '沉睡用户'
      when datediff(today, last_active_dt)>=30 then '流失用户'
   end) as user_grade
, round(count(uid)/max(all_user_cnt),2) as ratio 
from (
    select 
      uid
    , min(date(in_time)) as first_active_dt
    , max(date(in_time)) as last_active_dt
    , max(max(date(in_time))) over() as today
    -- 注意这里today的写法
    , count(uid) over() as all_user_cnt
    from tb_user_log
    group by uid
) t
group by 1
order by ratio desc
;
```

```sql
select 
    user_grade,
    round(count(uid)/(select count(distinct uid) from tb_user_log),2) as ratio
from
    (select 
        uid,
        case when datediff(date(today),date(first_time))<7 then '新晋用户'
            when datediff(date(today),date(first_time))>=7 
            and datediff(date(today),date(recent_time))<7 then '忠实用户'
            when datediff(date(today),date(first_time))>=7 
            and datediff(date(today),date(recent_time)) between 7 and 29 then '沉睡用户'
            else '流失用户' 
        end as user_grade
    from
        (select 
            uid,
            min(in_time) as first_time,
            max(in_time) as recent_time
        from tb_user_log 
        group by uid) t1
        join 
        (select 
            max(in_time) as today
        from tb_user_log
        )t2
    )T
group by user_grade
order by ratio desc
```

## SQL11 每天的日活数及新用户占比

https://www.nowcoder.com/practice/dbbc9b03794a48f6b34f1131b1a903eb?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
新用户 

count(*) over (partition by uid order by dt) as times
sum(if(times = 1, 1, 0)

min(date(in_time)) min_date group by uid
count(min_date) 或 count(DISTINCT n.uid)
```

```sql
select
    dt,
    count(uid)as dau,
    round(sum(if(times=1,1,0))/count(uid),2)as nv_new_ratio
from
    (
    select 
        *,
        count(*) over (partition by uid order by dt)as times -- 对于排序为1的进行计数
    from
        (--所有登入和登出
        select 
            uid,
            date(in_time) as dt
        from tb_user_log
        union
        select
            uid,
            date(out_time) as dt
        from tb_user_log
        ) t1
    ) t2
group by dt
order by dt
```

```sql
with 
t1 as ( -- 用户第一次登陆时间
    select 
        uid,
        min(date(in_time)) min_date
    from tb_user_log
    group by uid),
t2 as ( -- 用户所有登陆时间
    select 
        uid,
        date(in_time) dt
    from tb_user_log
    union
    select 
        uid,
        date(out_time) dt
    from tb_user_log)

select 
    dt,
    count(*),
    round(count(min_date)/count(*),2)
from(
    SELECT t2.uid,dt,min_date
    from t2
    left join t1 on t2.uid=t1.uid and t2.dt=t1.min_date) t3
group by dt
order by dt

```

```sql
SELECT 
    m.dt,
    count(DISTINCT m.uid) dau,
    round(count(DISTINCT n.uid)/count(DISTINCT m.uid),2) uv_new_ratio
FROM(
    SELECT 
        uid,
        date(in_time) dt
    FROM tb_user_log 
    union ALL
    SELECT 
        uid,
        date(out_time) dt
    FROM tb_user_log 
    where date(in_time) != date(out_time)
    ) m -- 全部登陆
    left join 
        (SELECT 
            uid,
            min(date(in_time)) dt
        FROM tb_user_log
        GROUP BY uid
        ) n -- 最近一次登陆
    on m.uid=n.uid and m.dt=n.dt
GROUP BY m.dt
order by m.dt
```


## SQL12 连续签到领金币

https://www.nowcoder.com/practice/aef5adcef574468c82659e8911bb297f?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

日期：12345789

rank：12345678

日期-rank：00000111

partition排序：12345123  

```sql
ranK() 和 row_number() 和 dense_rank() 都可以
# 第一步： 先过滤掉不签到的，重复的
# 第二步： ranK() over (partition by uid order by dt) --按照签名排序 
# 第三步： date_sub (dt, interval ranK() over (partition by uid order by dt) 
#          按照签到日期减去签到排名的差值 排序，（如果是连续签到，则得到的日期相同）-- 即得到连续签到的天数，rank_day
# 第四步： rank() over (partition by uid, rank_day order by dt)，按照连续签的天数到排序，
在mod与7，即可以判断连续签到的天数是否可以达到额外加金币的条件
```

```sql
#给确定每个人每个签到阶段的起始日期
select 
    uid,
    date_format(dt,'%Y%m') as month,
    sum(case when stage_index = 2 then 3
        when stage_index = 6 then 7
        else 1 end) as coin -- 第四部
from(
    select 
        uid,
        dt,
        (row_number() over (partition by uid, init_date order by dt) -1 ) % 7 as stage_index -- 第三步
    from
    (
        select 
            uid,
            dt,
            rn,
            subdate(dt,rn) as init_date -- 第二步
        from (
            select 
                uid,
                date(in_time) as dt,
                row_number() over (partition by uid order by date(in_time)) as rn -- 第一步
            from tb_user_log
            where date(in_time) >='2021-07-07'
            and date(in_time)<'2021-11-01'
            and artical_id=0 and sign_in=1
        ) raw_t
    ) init_date_t
) a
group by uid,month
order by month,uid;
```

```sql
SELECT uid, DATE_FORMAT(dt, "%Y%m") AS month, SUM(score) AS coin
FROM (
    SELECT uid, dt,
           CASE
               WHEN ranking % 7 = 3 THEN 3
               WHEN ranking % 7 = 0 THEN 7
               ELSE 1
           END AS score -- 第三步
    FROM (
        SELECT uid, dt, DATE_SUB(dt, INTERVAL rn DAY) AS first_day,
               ROW_NUMBER() OVER (PARTITION BY uid, DATE_SUB(dt, INTERVAL rn DAY) ORDER BY dt) AS ranking -- 第二步
        FROM (
            SELECT DISTINCT uid, DATE_FORMAT(in_time, "%Y%m%d") AS dt,
                   DENSE_RANK() OVER (PARTITION BY uid ORDER BY DATE_FORMAT(in_time, "%Y%m%d")) AS rn -- 第一步
            FROM tb_user_log
            WHERE in_time >= "2021-07-07" AND 
                  in_time < "2021-11-01" AND 
                  sign_in = 1 AND 
                  artical_id = 0
        ) AS a
    ) AS b
) AS c
GROUP BY uid, month
ORDER BY month, uid
```

```sql
select
             
    uid,
    date_format(dt,'%Y%m') month,
    sum(coin) coin
from (
    select
        uid,
        dt,
        case when mod(rank()over(partition by uid,rank_day order by dt), 7)=0 then 7 -- 第二步
             when mod(rank()over(partition by uid,rank_day order by dt), 7)=3 then 3
             else 1
             end  coin 
             --   按照rank_day排序的排名，与7求mod，
    from(
        select
            uid,
            dt,
             date_sub(dt,interval ranK()over(partition by uid order by dt) day ) rank_day --  第一步
        from (
            select
                distinct uid,  -- 去重 过滤掉重复签到的情况
                date(in_time) dt -- 登入日期 
            from tb_user_log
            where artical_id=0 and sign_in=1
                and date(in_time)  between '2021-07-07' and '2021-10-31'
            ) t1  
          ) t2
     ) t3
group by uid,month
order by  month,uid
```


## SQL13 计算商城中2021年每月的GMV

https://www.nowcoder.com/practice/5005cbf5308249eda1fbf666311753bf?tpId=268&tqId=2285515&ru=/practice/dbbc9b03794a48f6b34f1131b1a903eb&qru=/ta/sql-factory-interview/question-ranking


```sql
where event_time like '2021%' and status <> 2
等效于
where `status` in (0,1) and year(event_time)=2021
等效于
WHERE status != 2 AND year(event_time)=2021
```

```sql
SELECT 
    DATE_FORMAT(event_time,'%Y-%m') month,
    sum(total_amount) GMV
FROM tb_order_overall
WHERE status != 2 AND year(event_time)=2021
GROUP BY month 
HAVING GMV >= 100000
ORDER BY GMV;
```


## SQL14 统计2021年10月每个退货率不大于0.5的商品各项指标

https://www.nowcoder.com/practice/cbf582d28b794722becfc680847327be?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
退货率

sum(if_payment) as payment
sum(if_refund) as refund
if(payment = 0, 0, round(refund/payment, 3)) as refund_rate
having refund_rate <= 0.5
```

```sql
SELECT 
    product_id,
    round(click/showtimes,3) as ctr,
    if(click=0,0,round(cart/click,3)),
    if(cart=0,0,round(payment/cart,3)),
    if(payment=0,0,round(refund/payment,3)) as refund_rate
FROM
    (select product_id,
        count(*) as showtimes,
        sum(if_click) as click,
        sum(if_cart) as cart,
        sum(if_payment) as payment,
        sum(if_refund) as refund 
    FROM tb_user_event
    where date_format(EVENT_time,"%Y-%m")="2021-10"
    group by product_id
) BASE
group by product_id
having refund_rate<=0.5
order by product_id
```

## SQL15 某店铺的各商品毛利率及店铺整体毛利率

https://www.nowcoder.com/practice/65de67f666414c0e8f9a34c08d4a8ba6?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
商品毛利率大于 24.9 % 的商品信息
字符串 操作：
replace(profit_rate, '%', '') > 24.9
TRIM   (TRAILING '%' FROM profit_rate) > 24.9
(1 - in_sum / sale_sum) > 0.249
```

```sql
TRIM (BOTH 'O' FROM 'OOHELLO') 
TRIM (LEADING 'O' FROM 'OOHELLO')
TRIM (TRAILING 'O' FROM 'OOHELLO') 
```

```sql
难点在于要求'店铺汇总'

可以用 

group by a.product_id with rollup + ifnull(product_id,'店铺汇总')
```

```sql
select 
    ifnull(product_id,'店铺汇总') product_id,
    concat(profit_rate,'%') profit_rate 
from
(
    select 
        a.product_id,
        round((1-sum(in_price*cnt)/sum(price*cnt))*100, 1) profit_rate
    from tb_order_detail as a 
    left join tb_order_overall as b on a.order_id=b.order_id
    left join tb_product_info as c on c.product_id = a.product_id
    where c.shop_id = 901 and status=1 and date_format(event_time,'%Y%m')>='202110'
    group by a.product_id with rollup
    having profit_rate > 24.9 or a.product_id is null
    ) as d
order by profit_rate
```

或者

union

```sql
select 
    '店铺汇总' as product_id,
    concat(round((1-sum(cnt*in_price)/ sum(cnt*price))*100,1),'%') profit_rate
from tb_order_detail t3
left join tb_product_info t1 on t3.product_id=t1.product_id
left join tb_order_overall t2 on t3.order_id=t2.order_id
where date_format(event_time,'%Y-%m')>='2021-10' and shop_id='901'
group by shop_id

union

SELECT 
    t3.product_id,
    CONCAT(round((1-avg(in_price)/avg(price))*100,1),'%') profit_rate
FROM tb_order_detail t3
LEFT JOIN tb_product_info t1 ON  t3.product_id=t1.product_id
LEFT JOIN tb_order_overall t2 ON t3.order_id=t2.order_id
WHERE DATE_FORMAT(event_time,'%Y-%m')>='2021-10' AND shop_id='901'
GROUP BY product_id
HAVING TRIM(TRAILING '%' FROM profit_rate) > 24.9;
```

## SQL16 零食类商品中复购率top3高的商品

https://www.nowcoder.com/practice/9c175775e7ad4d9da41602d588c5caf3?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
复购率
count(uid) cnt group by a.product_id, b.uid -- 统计同一个商品，同一个用户
sum(if (cnt >= 2, 1, 0)) / count(*)

等效于
COUNT(1) pay_cnt group by product_id, uid
sum(case when  n>= 2 then 1 else 0 end)/count(distinct uid)
```

```sql
最近90天
event_time >= DATE_SUB((SELECT max(event_time) FROM tb_order_overall), interval 89 DAY)
DATEDIFF((select max(event_time) from tb_order_overall), event_time) <= 89
```

```sql
select 
    product_id,
    round(sum(if(cnt>=2,1,0))/count(*),3) repurchase_rate 
from
(
    select 
        a.product_id,
        b.uid,
        count(uid) cnt 
    from tb_order_detail as a 
    left join tb_order_overall as b on a.order_id=b.order_id
    left join tb_product_info as c on c.product_id=a.product_id
    where datediff((select max(event_time) from tb_order_overall), event_time) < 90 and tag = '零食'
    group by a.product_id, b.uid
    ) as d
group by product_id
order by repurchase_rate desc, product_id limit 3

```

## SQL17 10月的新户客单价和获客成本

https://www.nowcoder.com/practice/d15ee0798e884f829ae8bd27e10f0d64?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
用户首单的表示方法：

RANK() OVER (PARTITION BY uid ORDER BY event_time) AS order_rank
WHERE order_rank = 1

row_number() over (partition by uid order by event_time) rk
where rk = 1

等效于
where (uid, date(event_time)) in (select uid, min(date(event_time)) from tb_order_overall group by uid)

where DATE(event_time) in (select min(date(event_time)) from tb_order_overall group by uid)
```

```sql
获客成本表示方法：

sum(price * cnt) AS firstly_amount
sum(firstly_amount - total_amount) / count(uid)

sum(price * cnt) AS firstly_amount
sum(firstly_amount - total_amount) / count(distinct too.order_id)
```

```sql
SELECT
    ROUND(SUM(total_amount) / COUNT(uid), 1) AS avg_amount,
    ROUND(SUM(firstly_amount - total_amount) / COUNT(uid), 1) AS avg_cost
FROM
    (SELECT
        uid,
        order_id,
        total_amount
    FROM 
        (SELECT
            *,
            RANK() OVER(PARTITION BY uid ORDER BY event_time) AS order_rank
        FROM tb_order_overall) AS t1
    WHERE
        order_rank = 1
        AND
        DATE_FORMAT(event_time, '%Y%m') = 202110) AS t2
JOIN
    (SELECT
        order_id,
        SUM(price * cnt) AS firstly_amount
    FROM tb_order_detail
    GROUP BY order_id) AS t3
ON t2.order_id = t3.order_id;

```

## SQL18 店铺901国庆期间的7日动销率和滞销率（区别于22题）

https://www.nowcoder.com/practice/e7837f66e8fb4b45b694d24ea61f0dc9?tpId=268&tqId=2286659&ru=/practice/5005cbf5308249eda1fbf666311753bf&qru=/ta/sql-factory-interview/question-ranking

```sql
7日动销率：
if(timestampdiff(day, lt, event_time) between 0 and 6, product_id, null)

等效于:
if(DATEDIFF(lt, event_time) <= 6 and DATEDIFF(lt, event_time) >= 0, product_id, null))
```

```sql
select 
    dt,
    round(count(distinct product_id)/(select count(product_id) from tb_product_info where shop_id = 901),3),
    round(1-count(distinct product_id)/(select count(product_id) from tb_product_info where shop_id = 901),3)
from
    (select 
        date(event_time) dt 
    from tb_order_overall
    where date(event_time) between '2021-10-01' and '2021-10-03'
    ) a -- 最近3天
left join
    (select 
        date(c.event_time) fdt,
        b.product_id product_id
    from tb_order_detail b 
    left join tb_order_overall c using(order_id)
    left join tb_product_info d using (product_id)
    where d.shop_id = 901) f 
on datediff(a.dt,f.fdt) between 0 and 6
group by dt
order by dt asc;
```

```sql
select lt dt,round(sr,3) sr,round((1-sr),3) nsr
from
    (select 
        lt,
        count(distinct if(DATEDIFF(lt,event_time)<=6 and DATEDIFF(lt,event_time)>=0, t2.product_id, null))/ -- lt 是最近3天
        count(distinct t2.product_id) sr
    from
        (SELECT 
            date_format(event_time,'%Y-%m-%d') lt 
        from 
            tb_product_info a,
            tb_order_overall b,
            tb_order_detail c 
        where 
            a.product_id=c.product_id 
            and b.order_id=c.order_id 
            and status=1 and event_time>20211001 
            and event_time<20211004
        group by date_format(event_time,'%Y-%m-%d')
        ) a, -- 最近3天
        tb_order_overall t1
    left join tb_order_detail t2 on t1.order_id=t2.order_id
    left join tb_product_info t3 on t2.product_id=t3.product_id
    where DATEDIFF(lt,release_time)>=0 and shop_id=901
    group by lt) aa
order by dt
```


## SQL19 2021年国庆在北京接单3次及以上的司机统计信息

https://www.nowcoder.com/practice/992783fd80f746d49e790d33ee537c19?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
接单3次及以上：

group by driver_id having count(*) >= 3
group by driver_id having count(order_id) >= 3
```

 ```sql
select  t.city,
        round(avg(avg_order_num),3),
        round(avg(avg_income),3)
from 
(
    select 
        driver_id,
        city,
        count(o.order_id) as avg_order_num, 
        sum(fare) as avg_income
    from tb_get_car_record as r
    join tb_get_car_order as o on r.order_id = o.order_id
    where city = '北京' and order_time between '2021-10-01' and '2021-10-07'
    group by driver_id having count(*) > 2
) as t
group by city
```


## SQL20 有取消订单记录的司机平均评分

https://www.nowcoder.com/practice/f022c9ec81044d4bb7e0711ab794531a?tpId=268&tqId=2294893&ru=/practice/e7837f66e8fb4b45b694d24ea61f0dc9&qru=/ta/sql-factory-interview/question-ranking


```sql
coalesce(driver_id, '总体')
等效于
IFNULL(driver_id, '总体')


group by tb.driver_id with rollup
```

```sql
select
    coalesce(o.driver_id,'总体') as driver_id,
    round(avg(o.grade),1) as avg_grade
from tb_get_car_order o
where driver_id in(
    select distinct driver_id 
    from tb_get_car_order 
    where 
        date_format(order_time,'%Y%m')=202110 and 
        start_time is null)
group by o.driver_id with rollup
 
```

```sql
select driver_id,round(avg(grade),1)
from tb_get_car_order
where driver_id in(
    select distinct(o.driver_id)
    from tb_get_car_order o 
    where start_time is NULL and DATE_FORMAT(finish_time,'%Y%m') = '202110')
group by driver_id

union

select '总体',round(sum(grade)/count(grade),1)
from tb_get_car_order
where driver_id in(
    select distinct(o.driver_id)
    from tb_get_car_order o 
    where start_time is NULL and DATE_FORMAT(finish_time,'%Y%m') = '202110')
```


## SQL21 每个城市中评分最高的司机信息

https://www.nowcoder.com/practice/dcc4adafd0fe41b5b2fc03ad6a4ac686?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
每个城市中评分最高
用 rank() 效果一样
dense_rank() over (partition by city order by round(avg(grade), 1) desc
round(avg(grade) GROUP BY driver_id, city
```

```sql
select city,driver_id,avg_grade,avg_order_num,avg_mileage
from
    (select 
        city,driver_id,avg_grade,avg_order_num,avg_mileage,
        dense_rank() over(partition by city order by avg_grade desc) as rank_grade
    from
        (select 
            city,driver_id,round(avg(grade),1)avg_grade,
            round(count(ord.order_id)/count(distinct date_format(order_time,"%Y-%m-%d")),1)avg_order_num,
            round(sum(mileage)/count(distinct date_format(order_time,"%Y-%m-%d")),3)avg_mileage
        from tb_get_car_order ord
        left join tb_get_car_record re
        on ord.order_id=re.order_id
        group by city,driver_id
        )a 
    )b
where rank_grade=1
order by avg_order_num;
```

## SQL22 国庆期间近7日日均取消订单量

https://www.nowcoder.com/practice/2b330aa6cc994ec2a988704a078a0703?tpId=268&tqId=2299819&ru=/practice/f022c9ec81044d4bb7e0711ab794531a&qru=/ta/sql-factory-interview/question-ranking

```sql
group by date(order_time) 日均

- order by date_format(order_time, '%Y-%m-%d') rows 6 preceding

- order by dt rows 6 preceding

- order by dates rows between 6 preceding and current row
```

```sql
SELECT *
FROM(
    SELECT 
        dt, 
        round(sum(finish_num) over (order by dt rows 6 preceding) / 7, 2) as finish_num_7d,
        round(sum(cancel_num) over (order by dt rows 6 preceding) / 7, 2) as cancel_num_7d
    FROM(
        SELECT 
            DATE(order_time) as dt,
            sum(IF(start_time is not NULL, 1, 0)) as finish_num,
            sum(IF(start_time is NULL, 1, 0)) as cancel_num
        FROM tb_get_car_order
        GROUP BY DATE(order_time)
        ORDER BY dt
        ) t 
    ) tt
WHERE dt BETWEEN '2021-10-01' and '2021-10-03'
```

## SQL23 工作日各时段叫车量、等待接单时间和调度时间

https://www.nowcoder.com/practice/34f88f6d6dc549f6bc732eb2128aa338?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

早高峰的表示方法：

```sql
RIGHT(event_time, 8) >='07:00:00' AND RIGHT(event_time, 8) < '09:00:00'

subString_index(event_time, ' ', -1) between '07:00:00' and '08:59:59'

date_format(event_time,'%T') >= '07:00:00' and date_format(event_time,'%T') < '09:00:00'

HOUR(event_time) IN (7, 8)

hour(event_time) >= 7 and hour(event_time) < 9
```

工作日的表示方法：

```sql
DATE_FORMAT(event_time, '%w') BETWEEN 1 AND 5

WEEKDAY(order_time) NOT IN(5, 6)

WEEKDAY(order_time) between 0 and 4

DAYOFWEEK(event_time) BETWEEN 2 AND 6
```

```sql
CASE
WHEN RIGHT(event_time, 8) >= '07:00:00' AND RIGHT(event_time, 8) < '09:00:00' THEN '早高峰'
WHEN RIGHT(event_time, 8) >= '09:00:00' AND RIGHT(event_time, 8) < '17:00:00' THEN '工作时间'
WHEN RIGHT(event_time, 8) >= '17:00:00' AND RIGHT(event_time, 8) < '20:00:00' THEN '晚高峰'
ELSE '休息时间'
END period,
GROUP BY period
```


```sql
SELECT period, COUNT(event_time) order_num, ROUND(AVG(wait_time), 1), ROUND(SUM(dispatch_time)/COUNT(dispatch_time), 1)
FROM(
    SELECT event_time,  
            CASE
            WHEN RIGHT(event_time, 8) >='07:00:00' AND RIGHT(event_time, 8) < '09:00:00' THEN '早高峰'
            WHEN RIGHT(event_time, 8) >='09:00:00' AND RIGHT(event_time, 8) < '17:00:00' THEN '工作时间'
            WHEN RIGHT(event_time, 8) >='17:00:00' AND RIGHT(event_time, 8) < '20:00:00' THEN '晚高峰'
            ELSE '休息时间'
            END period,
         TIMESTAMPDIFF(SECOND, event_time, end_time)/60 wait_time,
         TIMESTAMPDIFF(SECOND, order_time, start_time)/60 dispatch_time
    FROM tb_get_car_record tgcr
    LEFT JOIN tb_get_car_order
    USING(order_id)
    WHERE DATE_FORMAT(event_time, '%w') BETWEEN 1 AND 5
    ) a 
GROUP BY period
ORDER BY order_num 
```

## SQL24 各城市最大同时等车人数


https://www.nowcoder.com/practice/f301eccab83c42ab8dab80f28a1eef98?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
IFNULL(start_time, finish_time)
等效于
COALESCE(start_time, finish_time)
```

```sql
表示是否等待：

-1 as if_wait
 1 as if_wait
```

```sql
SELECT city, MAX(sum_wait_num)
FROM(
    SELECT city, time, SUM(if_wait) OVER(PARTITION BY city, left(time, 10) ORDER BY time, if_wait DESC) sum_wait_num
    FROM(
        SELECT city, event_time time, 1 if_wait
        FROM tb_get_car_record

        UNION ALL

        SELECT city, IFNULL(start_time, finish_time) time, -1 if_wait
        FROM tb_get_car_order
        JOIN tb_get_car_record
        USING(order_id)
        )a
    )b
WHERE LEFT(time, 7) = '2021-10'
GROUP BY city
ORDER BY MAX(sum_wait_num), city
```
