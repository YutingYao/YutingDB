<!-- vscode-markdown-toc -->
* 1. [sql基础](#sql)
	* 1.1. [sql执行顺序](#sql-1)
	* 1.2. [执行效率](#)
	* 1.3. [ 行列转换](#-1)
* 2. [窗口函数应用](#-1)
	* 2.1. [lag和lead 分析函数](#laglead)
* 3. [面试题](#-1)
	* 3.1. [数据在数据库中如何存储?](#-1)
	* 3.2. [orical和SQL server之间如何联系](#oricalSQLserver)
	* 3.3. [如何确定数据库大小以及需要的服务器个数](#-1)
	* 3.4. [『多表间查询，分组』](#-1)
	* 3.5. [对排序的认识：说明快速派序的思路，还知道哪些排序方法?](#-1)
* 4. [笔试题](#-1)
	* 4.1. [175. 组合两个表](#-1)
	* 4.2. [176. 第二高的薪水](#-1)
	* 4.3. [177. 第N高的薪水](#N)
	* 4.4. [178. 分数排名](#-1)
	* 4.5. [180. 连续出现的数字](#-1)
	* 4.6. [181. 超过经理收入的员工](#-1)
	* 4.7. [182. 查找重复的电子邮箱](#-1)
	* 4.8. [183. 从不订购的客户](#-1)
	* 4.9. [184. 部门工资最高的员工](#-1)
	* 4.10. [185. 部门工资前三高的所有员工](#-1)
	* 4.11. [196. 删除重复的电子邮箱](#-1)
	* 4.12. [197. 上升的温度](#-1)
	* 4.13. [262. 行程和用户](#-1)
	* 4.14. [595. 大的国家](#-1)
	* 4.15. [596. 超过5名学生的课](#-1)
	* 4.16. [601. 体育馆的人流量](#-1)
	* 4.17. [620. 有趣的电影](#-1)
	* 4.18. [626. 换座位](#-1)
	* 4.19. [627. 变更性别](#-1)
	* 4.20. [1179. 重新格式化部门表](#-1)
	* 4.21. [总结1](#1)
	* 4.22. [总结2](#2)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->

##  1. <a name='sql'></a>sql基础

###  1.1. <a name='sql-1'></a>sql执行顺序

```sql
(8) SELECT (9)DISTINCT<select_list> 
(1) FROM <left_table> 
(3) <join_type> JOIN <right_table> 
(2) ON <join_condition> 
(4) WHERE <where_condition> 
(5) GROUP BY <group_by_list> 
(6) WITH {CUBE|ROLLUP} 
(7) HAVING <having_condition> 
(10) ORDER BY <order_by_list> 
(11) LIMIT <limit_number>
```

###  1.2. <a name=''></a>执行效率

```s
num id  str
1	1	A
2	3	A
3	5	A
4	8	A
1	2	B
2	7	B
1	4	C
2	6	C
```

1. 最快的方法：MySQL变量，一次全扫描搞定。文件排序是因为没有索引。

```sql
SELECT
-- 变量赋值
    @num := IF(@str = str, @num + 1, 1) num,
    id,
    @str := str str
FROM
-- 初始化
    tem, (SELECT @str := '', @num := 0) t1
ORDER BY
    str, id;
```

```s
+----+-------------+------------+--------+------+----------------+
| id | select_type | table      | type   | rows | Extra          |
+----+-------------+------------+--------+------+----------------+
|  1 | PRIMARY     | <derived2> | system |    1 |                |
|  1 | PRIMARY     | tem        | ALL    |    8 | Using filesort |
|  2 | DERIVED     | NULL       | NULL   | NULL | No tables used |
+----+-------------+------------+--------+------+----------------+
3 rows in set (0.06 sec)
```

2. 子查询：两次全扫描，而且只用到了文件排序，如果加上索引，文件排序也可以避免。

```sql
SELECT
-- 注意，COUNT(*) FROM tem t2是从t2里面的
    (SELECT COUNT(*) FROM tem t2 WHERE t1.str = t2.str AND t1.id >= t2.id) num,
    t1.*
FROM
    tem t1
ORDER BY
    t1.str, t1.id;
```

```s
+----+--------------------+-------+------+---------------+------+----------------+
| id | select_type        | table | type | possible_keys | rows | Extra          |
+----+--------------------+-------+------+---------------+------+----------------+
|  1 | PRIMARY            | t1    | ALL  | NULL          |    8 | Using filesort |
|  2 | DEPENDENT SUBQUERY | t2    | ALL  | PRIMARY       |    8 | Using where    |
+----+--------------------+-------+------+---------------+------+----------------+
2 rows in set (0.06 sec)
```

3. 连表查询：非常低效：临时表、文件排序、循环嵌套都用上了

```sql
SELECT
    count(*) num,
    t1.*
FROM
    tem t1
    INNER JOIN tem t2 ON t1.str = t2.str AND t1.id >= t2.id
GROUP BY
    t1.id
ORDER BY
    t1.str, t1.id;
```

```s
+----+-------------+-------+------+---------------+------+-------------------------------------------------+
| id | select_type | table | type | possible_keys | rows | Extra                                           |
+----+-------------+-------+------+---------------+------+-------------------------------------------------+
|  1 | SIMPLE      | t1    | ALL  | PRIMARY       |    8 | Using temporary; Using filesort                 |
|  1 | SIMPLE      | t2    | ALL  | PRIMARY       |    8 | Using where; Using join buffer (flat, BNL join) |
+----+-------------+-------+------+---------------+------+-------------------------------------------------+
2 rows in set (0.06 sec)
```

###  1.3. <a name='-1'></a> 行列转换

经常用sql中的函数`case when`、`pivot`来实现行转列；

使用`union`、`unpivot`来实现列转行。

```s
例子 ：有学生成绩表`user_score`如下所示：

user_id	subject	score
1		语文	89
1		数学	97
2		数学	90
3		英语	70
……		……		……


想要把每个科目单独作为一列展示，如下：

user_id	语文	数学	英语
1		89		97		78
……		……		……		……

```

```sql

/*第1种方式：使用case when函数*/
SELECT user_id,
  CASE subject WHEN '语文' THEN score ELSE 0 END AS '语文',
  CASE subject WHEN '数学' THEN score ELSE 0 END AS '数学',
  CASE subject WHEN '英语' THEN score ELSE 0 END AS '英语'
FROM user_score 
GROUP BY user_id

/*第2种方式：使用PIVOT函数*/ PIVOT + FOR + IN
SELECT PVT.user_id,PVT.语文,PVT.数学,PVT.英语
FROM user_score
PIVOT (score FOR subject IN('语文','数学','英语')) AS PVT

```

```sql
如若需要把上例结果表形式转化为原表形式，即列转行:

/*第1种方式：使用union函数*/
select user_id,'语文' as subject,语文 as score from user_score t1 
union
select user_id,'数学' as subject,数学 as score from user_score t2 
union
select user_id,'英语' as subject,英语 as score from user_score t3

/*第2种方式：使用unpivot函数*/ UNPIVOT + FOR + IN
SELECT * 
FROM user_score
UNPIVOT(score FOR subject IN('语文','数学','英语')) as pvt
```

##  2. <a name='-1'></a>窗口函数

### 窗口函数主要有以下几类:

专有窗口函数

```sql
row_number() over()：排序，排序为连续值且不重复，例如，1，2，3，4，5，6，7
rank() over()：排序，排序值可能不连续，例如，1，1，1，4，5，5，7
dense_rank() over()：排序，排序为连续值可能有重复，例如，1，1，1，2，3，3，4
ntile() over()：切片函数
```

聚合类窗口函数

```sql
sum() over()：累计计算，例如查询出2019-2020年每月的支付总额和当年累计支付总额
count() over()：计数，例如查询出2019-2020年每月的支付用户数和当年累计支付用户数
avg() over()：移动平均，例如查询出2021年每个月的近三月移动平均支付金额
max() over()：最大，例如查询出每四个月的最大月总支付金额
min() over()：最小，查询出每四个月的最小月总支付金额
```

偏移分析函数

```sql
lag() over()：向上偏移
lead() over() ：向下偏移
```

###  2.1. <a name='laglead'></a>lag和lead 分析函数

```sql
select * from kkk; 
```

```s
        ID NAME                                                   
---------- --------------------                                   
         1 1name                                                  
         2 2name                                                  
         3 3name                                                  
         4 4name                                                  
         5 5name                         
```

lag就是往下拽

```sql
select id, name, lag(name,1,0) over ( order by id )  from kkk; 
```

第一个参数是列名，第二个参数是偏移的offset，第三个参数是 超出记录窗口时的默认值）

```s
        ID NAME                 LAG(NAME,1,0)OVER(ORDERBYID)      
---------- -------------------- ----------------------------      
         1 1name                0                                 
         2 2name                1name                             
         3 3name                2name                             
         4 4name                3name                             
         5 5name                4name                             
```

lead就是往上拽：

```sql
select id,name,lead(name,1,0) over ( order by id )  from kkk;
```

```s
        ID NAME                 LEAD(NAME,1,0)OVER(ORDERBYID)     
---------- -------------------- -----------------------------     
         1 1name                2name                             
         2 2name                3name                             
         3 3name                4name                             
         4 4name                5name                             
         5 5name                0         
```

```sql
 select id,name,lead(name,1,'alsdfjlasdjfsaf') over ( order by id )  from kkk; 
```

```s
                                                                                  
        ID NAME                 LEAD(NAME,1,'ALSDFJLASDJFSAF')                    
---------- -------------------- ------------------------------                    
         1 1name                2name                                             
         2 2name                3name                                             
         3 3name                4name                                             
         4 4name                5name                                             
         5 5name                alsdfjlasdjfsaf 
```

```s
求用户连续登录(内容连续更新)、断登(内容断更)情况

app为了摸清用户在平台的活跃情况，常常需要统计用户的连续登陆以及登陆间隔情况。

例子：假设某公司的APP活跃表为表login_detail，字段如下：

字段	     类型
user_id	    string
login_date	date
```

step1: 出每个用户的最大登陆间隔:

```sql

select user_id, max(datediff(lead_dates,login_date)) as '最大登陆间隔'
from
    (select user_id, login_date, lead(login_date) over (partition by user_id order by login_date asc) as lead_dates from login_detail) a
    把表往上拽一点点，一个个求差
group by user_id
```

step2: 求出每个用户最大持续登陆天数：

```sql
第1种方式:

select user_id,max(num)
-- 最后对每个用户的连续登录天数取最大值即可。 
from(
    select user_id,date,count(date) as num 
    from(
        select user_id,date_sub(login_date,interval rank1 day) as date
        -- 用每个用户的`每次登录时间`减去`排序序号天数`作为date
        -- （理论上如果用户一直连续登陆，date的值为同一日期），
        from(select user_id,login_date,row_number() over(partition by user_id order by login_date asc) as rank1 from login_detail
            -- 先对每个用户的登录时间升序排序，
            )a
        )b
    group by user_id,date
    -- 然后按用户和date分组，对date不去重计数，
    )c
group by user_id

第2种方式，使用变量来统计连续值。

SELECT user_id, max(cxgx)
FROM(
    SELECT *,IF(dg = 0, @row := @row + 1, @row:=1) cxgx
    FROM (
        SELECT *, DATEDIFF(login_date,lag1)-1 as dg, @row := 1 
        from (
            SELECT user_id, login_date, lag(login_date) over (PARTITION by user_id ORDER BY login_date) lag1 FROM login_detail
            ) t1 
        )t2
    GROUP BY user_id,login_date
    ) t3
GROUP BY user_id
```

> 接下来两步采用自连接，计算效率可能比较慢

step3: 求该app当日登陆用户数，第二日留存用户数，第三日留存用户数，第八日留存用户数；

```sql
select a.login_date,
    count(distinct case when datediff(b.login_date,a.login_date) = 0 then a.user_id else null end) as '当日登陆用户数',
    count(distinct case when datediff(b.login_date,a.login_date) = 1 then a.user_id else null end) as '第二日留存用户数',
    count(distinct case when datediff(b.login_date,a.login_date) = 2 then a.user_id else null end) as '第三日留存用户数',
    count(distinct case when datediff(b.login_date,a.login_date) = 7 then a.user_id else null end) as '第八日留存用户数'
from login_detail as a
left join login_detail as b on a.user_id = b.user_id and b.login_date >= a.login_date
group by a.login_date
```

step4: 求该app当日登陆用户数，次日留存率，二日留存率，7日留存率

```sql

select t.dates, curr_nums as '当日登陆用户数', 
    concat(cast(1st_nums/curr_nums*100 as decimal(18,2)),'%') as '次日留存率', 
    concat(cast(2nd_nums/curr_nums*100 as decimal(18,2)),'%') as '二日留存率', 
    concat(cast(7th_nums/curr_nums*100 as decimal(18,2)),'%') as '七日留存率'                                      
from (
    select a.dates,
        count(distinct case when datediff(b.login_date,a.login_date)=0 then a.user_id else null end) as curr_nums,
        count(distinct case when datediff(b.login_date,a.login_date)=1 then a.user_id else null end) as 1st_nums,
        count(distinct case when datediff(b.login_date,a.login_date)=2 then a.user_id else null end) as 2nd_nums,
        count(distinct case when datediff(b.login_date,a.login_date)=7 then a.user_id else null end) as 7th_nums
    from login_detail a
    left join login_detail b on a.user_id=b.user_id and b.login_date>=a.login_date
    group by a.login_date
    ) t
```

##  3. <a name='-1'></a>面试题

###  3.1. <a name='-1'></a>数据在数据库中如何存储?

https://mp.weixin.qq.com/s/U5WtjbgXFGoij9rIDQKaRQ

https://mp.weixin.qq.com/s/h5qysXyiOpXNvJT8pbTN3g

https://mp.weixin.qq.com/s/_3SzbOKz8bxZH7PuRZ5xHw

https://mp.weixin.qq.com/s/0ejRJq78ikQjxcB8t1EQbg

https://mp.weixin.qq.com/s/DSHIVtd4CLKqiem6_3K-rw

https://mp.weixin.qq.com/s/WIgU8ExhLkqazLOSNTTY2g

https://mp.weixin.qq.com/s/PhaB7GAQW81di4Q0lqSc0w

https://mp.weixin.qq.com/s/V1sn4I2cb8GjBFfsGtpz8Q

https://mp.weixin.qq.com/s/_yBk5zai1yhX3LmOjODcIA

https://mp.weixin.qq.com/s/kYugnod1dxE9Gjjjd7ZiCg

https://mp.weixin.qq.com/s/k9VPSPPMxDA42kfGvHTdGg

###  3.2. <a name='oricalSQLserver'></a>orical和SQL server之间如何联系

全球市场中最主流的数据库分别是 `Oracle`、`SQL Server`、`Db2` 三大`商业数据库`

和 `MySQL`、`PostgreSQL` 两种`开源数据库`。

`SQL Server` 马上要超过 `Oracle` 了，主要是因为其拥抱`云的转型`。

###  3.3. <a name='-1'></a>如何确定数据库大小以及需要的服务器个数

https://mp.weixin.qq.com/s/FdhEryE1rkcAFMTw8nswkA

https://mp.weixin.qq.com/s/VKC_ORn6Pc35I2vKrcVaUA

https://mp.weixin.qq.com/s/u3RLE6knpzkp5YStCrqttA

###  3.4. <a name='-1'></a>『多表间查询，分组』

https://mp.weixin.qq.com/s/j32qmk6pLK3rM8OhBWDaxg

https://mp.weixin.qq.com/s/At70WcOIq0HXAZSh_cvMTg

https://mp.weixin.qq.com/s/A39Th9WKodHGzUQ85fqXQg

https://mp.weixin.qq.com/s/JZevS43wDTUXU11jeWb-qw

https://mp.weixin.qq.com/s/2GUHHen9T6UqqBn0oajauQ

https://mp.weixin.qq.com/s/fwm5BUeASC9479pMklv37w

https://mp.weixin.qq.com/s/KtC49EImMb1jY88_wHeJRw

https://mp.weixin.qq.com/s/oNCea6vcSsz-GsBfKz23PQ

https://mp.weixin.qq.com/s/aeUg7hkQy00WfirXU-d-Sg

https://mp.weixin.qq.com/s/LNoy5YemEW1l73wAvop0Hw

###  3.5. <a name='-1'></a>对排序的认识：说明快速派序的思路，还知道哪些排序方法?

https://mp.weixin.qq.com/s/ufViNQMGwx8KIX23x4rmOw

https://mp.weixin.qq.com/s/l-LIb_WVhNr1m4nJKpM8Tg

https://mp.weixin.qq.com/s/ACeSNB4b8CnNQ1RgLYLuSQ

https://mp.weixin.qq.com/s/GaDtsn4DDnOqP5R3ovo23Q

https://mp.weixin.qq.com/s/FfhFrHcM2uW5D8R8c9YWsw

https://mp.weixin.qq.com/s/AFTC2AL2xvn75G_gDi3g9w

##  4. <a name='-1'></a>笔试题

###  4.1. <a name='-1'></a>175. 组合两个表

```sql
由于看数据知道应该是左连接 所以直接开干

使用联表查询，由题意，无论 person 是否有地址信息，都要提供FirstName, LastName, City, State 的信息，
所以我们将 person 作左表，Address 作右表，因为要左表的全部信息，所以使用左查询，
on 的用法就是寻找两边相同的字段，

select FirstName, LastName, City, State
from Person  left join Address on Person.PersonId = Address.PersonId;
```

```sql
不就是数据嘛，多查几次就行

select 
    FirstName, 
    LastName, 
    (select City from Address a where a.PersonId=p.PersonId) as City, 
    (select State from Address a where a.PersonId=p.PersonId) as State  
from Person p
```

###  4.2. <a name='-1'></a>176. 第二高的薪水

```sql
SELECT DISTINCT Salary AS SecondHighestSalary FROM Employee
ORDER BY Salary DESC
LIMIT 1 OFFSET 1



SELECT
    (SELECT DISTINCT Salary FROM Employee
        ORDER BY Salary DESC
        LIMIT 1 OFFSET 1) AS SecondHighestSalary

SELECT IFNULL(
      (SELECT DISTINCT Salary FROM Employee 
       ORDER BY Salary DESC
        LIMIT 1 OFFSET 1), NULL) AS SecondHighestSalary

select max(Salary) as SecondHighestSalary from Employee
where Salary < (select max(Salary) from Employee)
 

select
    max(s1.salary) as SecondHighestSalary
from
    Employee s1,Employee s2
where 
    s1.salary < s2.salary
```


###  4.3. <a name='N'></a>177. 第N高的薪水

```sql
思路1：单表查询
由于本题不存在分组排序，只需返回全局第N高的一个，所以自然想到的想法是用order by排序加limit限制得到。需要注意两个细节：

同薪同名且不跳级的问题，解决办法是用group by按薪水分组后再order by
排名第N高意味着要跳过N-1个薪水，由于无法直接用limit N-1，所以需先在函数开头处理N为N=N-1。
注：这里不能直接用limit N-1是因为limit和offset字段后面只接受正整数（意味着0、负数、小数都不行）或者单一变量（意味着不能用表达式），也就是说想取一条，limit 2-1、limit 1.1这类的写法都是报错的。
注：这种解法形式最为简洁直观，但仅适用于查询全局排名问题，如果要求各分组的每个第N名，则该方法不适用；而且也不能处理存在重复值的情况。

代码1

CREATE FUNCTION getNthHighestSalary(N INT) RETURNS INT
BEGIN
    SET N := N-1;
  RETURN (
      # Write your MySQL query statement below.
      SELECT 
            salary
      FROM 
            employee
      GROUP BY 
            salary
      ORDER BY 
            salary DESC
      LIMIT N, 1
  );
END

作者：luanhz
链接：https://leetcode-cn.com/problems/nth-highest-salary/solution/mysql-zi-ding-yi-bian-liang-by-luanz/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
思路5：自定义变量
以上方法2-4中均存在两表关联的问题，表中记录数少时尚可接受，当记录数量较大且无法建立合适索引时，实测速度会比较慢，用算法复杂度来形容大概是O(n^2)量级（实际还与索引有关）。那么，用下面的自定义变量的方法可实现O(2*n)量级，速度会快得多，且与索引无关。

自定义变量实现按薪水降序后的数据排名，同薪同名不跳级，即3000、2000、2000、1000排名后为1、2、2、3；
对带有排名信息的临时表二次筛选，得到排名为N的薪水；
因为薪水排名为N的记录可能不止1个，用distinct去重
代码5

CREATE FUNCTION getNthHighestSalary(N INT) RETURNS INT
BEGIN
  RETURN (
      # Write your MySQL query statement below.
      SELECT 
          DISTINCT salary 
      FROM 
          (SELECT 
                salary, @r:=IF(@p=salary, @r, @r+1) AS rnk,  @p:= salary 
            FROM  
                employee, (SELECT @r:=0, @p:=NULL)init 
            ORDER BY 
                salary DESC) tmp
      WHERE rnk = N
  );
END

作者：luanhz
链接：https://leetcode-cn.com/problems/nth-highest-salary/solution/mysql-zi-ding-yi-bian-liang-by-luanz/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
思路6：窗口函数
实际上，在mysql8.0中有相关的内置函数，而且考虑了各种排名问题：

row_number(): 同薪不同名，相当于行号，例如3000、2000、2000、1000排名后为1、2、3、4
rank(): 同薪同名，有跳级，例如3000、2000、2000、1000排名后为1、2、2、4
dense_rank(): 同薪同名，无跳级，例如3000、2000、2000、1000排名后为1、2、2、3
ntile(): 分桶排名，即首先按桶的个数分出第一二三桶，然后各桶内从1排名，实际不是很常用
显然，本题是要用第三个函数。
另外这三个函数必须要要与其搭档over()配套使用，over()中的参数常见的有两个，分别是

partition by，按某字段切分
order by，与常规order by用法一致，也区分ASC(默认)和DESC，因为排名总得有个依据
注：下面代码仅在mysql8.0以上版本可用，最新OJ已支持。

代码6

CREATE FUNCTION getNthHighestSalary(N INT) RETURNS INT
BEGIN
  RETURN (
      # Write your MySQL query statement below.
        SELECT 
            DISTINCT salary
        FROM 
            (SELECT 
                salary, dense_rank() over(ORDER BY salary DESC) AS rnk
             FROM 
                employee) tmp
        WHERE rnk = N
  );
END

作者：luanhz
链接：https://leetcode-cn.com/problems/nth-highest-salary/solution/mysql-zi-ding-yi-bian-liang-by-luanz/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
第二高的薪水：

要想获取第二高，需要排序，使用 order by（默认是升序 asc，即从小到大），若想降序则使用关键字 desc

去重，如果有多个相同的数据，使用关键字 distinct 去重

判断临界输出，如果不存在第二高的薪水，查询应返回 null，使用 ifNull（查询，null）方法

起别名，使用关键字 as ...

因为去了重，又按顺序排序，使用 limit（）方法，查询第二大的数据，即第二高的薪水，即 limit(1,1) （因为默认从0开始，所以第一个1是查询第二大的数，第二个1是表示往后显示多少条数据，这里只需要一条）

第 N 高的薪水：

题目是 176.第二高的薪水 的变形，将查询第二名变成查询 第N名
别名中不能带参数，一开始看到测试用例表，使用的别名是getNthHighestSalary(2)，就用了getNthHighestSalary(N)做别名，一开始报错还不知道是哪，后面删去变量即可
limit()方法中不能参与运算，因为索引从0开始，所以要 - 1，最好在外面就设定好 set N = N - 1
CREATE FUNCTION getNthHighestSalary(N INT) RETURNS INT
BEGIN
set N = N - 1;
  RETURN (
      # Write your MySQL query statement below.
      select ifnull((
          select distinct Salary 
          from Employee
          order by Salary desc limit N,1),null) as getNthHighestSalary
  );
END
```

```sql
CREATE FUNCTION getNthHighestSalary(N INT) RETURNS INT
BEGIN
  SET N = N-1;
  IF N < 0 THEN
  RETURN NULL;
  ELSE
  RETURN (
      # Write your MySQL query statement below.
      SELECT IFNULL(
          (
          SELECT
          DISTINCT Salary
          FROM Employee
          ORDER BY Salary DESC
          LIMIT N, 1
          ), NULL)
      AS getNthHighestSalary
  );
  END IF;
END

```

```sql
PS：MySQL8.0以下是不支持窗口函数的，SQL Server和Hive支持窗口函数。

CREATE FUNCTION getNthHighestSalary(N INT) RETURNS INT
BEGIN
  RETURN (
      # Write your MySQL query statement below.
      select distinct Salary from 
      (select dense_rank() over(order by Salary desc) num,Salary from Employee) t 
      where t.num=N
  );
END
当然这里时间仅仅是60%，窗口函数能完成但不是最优解，如果是对多个部门的薪水进行排序那就一定是窗口函数了。在这一题中可以使用下面的方式：

CREATE FUNCTION getNthHighestSalary(N INT) RETURNS INT
BEGIN
  SET N=N-1;
  RETURN (
      # Write your MySQL query statement below.
      select distinct Salary from Employee
      order by Salary desc limit N,1
  );
END
```

```sql
这种写法好像只能用dense_rank，试了row_number和rank都不行

CREATE FUNCTION getNthHighestSalary(N INT) RETURNS INT
BEGIN
  RETURN (
      select distinct Salary 
      from(
      select Id ,Salary,dense_rank()over(order by Salary desc) as rnk 
      from Employee ) a 
      where rnk=N  
  );
END
```

```sql
CREATE FUNCTION getNthHighestSalary(N INT) RETURNS INT
BEGIN
SET N = N-1;  # 从0开始索引，一定要设置n = n-1
  RETURN (
    SELECT(
        IFNULL(
            (SELECT DISTINCT Salary FROM Employee
            ORDER BY Salary DESC
            LIMIT 1 OFFSET N),NULL)
    ) 
  );
END
```

###  4.4. <a name='-1'></a>178. 分数排名

```sql
最后的结果包含两个部分，第一部分是降序排列的分数，第二部分是每个分数对应的排名。

第一部分不难写：


select a.Score as Score
from Scores a
order by a.Score DESC
比较难的是第二部分。假设现在给你一个分数X，如何算出它的排名Rank呢？
我们可以先提取出大于等于X的所有分数集合H，将H去重后的元素个数就是X的排名。比如你考了99分，但最高的就只有99分，那么去重之后集合H里就只有99一个元素，个数为1，因此你的Rank为1。
先提取集合H：


select b.Score from Scores b where b.Score >= X;
我们要的是集合H去重之后的元素个数，因此升级为：


select count(distinct b.Score) from Scores b where b.Score >= X as Rank;
而从结果的角度来看，第二部分的Rank是对应第一部分的分数来的，所以这里的X就是上面的a.Score，把两部分结合在一起为：


select a.Score as Score,
(select count(distinct b.Score) from Scores b where b.Score >= a.Score) as Rank
from Scores a
order by a.Score DESC

作者：johnbear007
链接：https://leetcode-cn.com/problems/rank-scores/solution/fen-cheng-liang-ge-bu-fen-xie-hui-rong-yi-hen-duo-/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
这题太老了，新版本有rank函数了，命名都别想，还得加“”

SELECT Score,
dense_rank() over(order by Score desc) as 'Rank'
FROM Scores
```

```sql
select s1.Score,count(distinct(s2.score)) Rank
from
Scores s1,Scores s2
where
s1.score<=s2.score
group by s1.Id
order by Rank
```

```sql
思路： 1.从两张相同的表scores分别命名为s1，s2。 2.s1中的score与s2中的score比较大小。意思是在输出s1.score的前提下，有多少个s2.score大于等于它。比如当s1.salary=3.65的时候，s2.salary中[4.00,4.00,3.85,3.65,3.65]有5个成绩大于等于他，但是利用count(distinct s2.score)去重可得s1.salary3.65的rank为3 3.group by s1.id 不然的话只会有一条数据 4.最后根据s1.score排序desc

select s1.score,count(distinct s2.score) as rank
from scores as s1,scores as s2
where s1.score<=s2.score
group by s1.id
order by s1.score desc;
```

```sql
方法一：采取函数

select Score,
dense_rank() over(Order By Score desc) 'Rank'
FROM Scores;
方法二：直接思维

SELECT Score,
 (SELECT count(DISTINCT score) FROM Scores WHERE score >= s.score) AS 'Rank' 
FROM Scores s 
ORDER BY Score DESC;

```

```sql
select s1.Score, count(distinct(s2.Score)) Rank
from Scores s1, Scores s2
where s1.Score<=s2.Score
group by s1.Id
order by Rank;
```

```sql
有了窗口函数，世界如此简单

select
score,
dense_rank() over(order by score desc) `Rank`
from
scores
```

```sql
自连接，注意Rank是关键字，需要加上引号，即 'Rank'

select s1.Score,count(distinct(s2.score)) 'Rank' 
from Scores s1 join Scores s2 
on s1.score<=s2.score 
group by s1.Id 
order by s1.Score desc;
```

###  4.5. <a name='-1'></a>180. 连续出现的数字

```sql
算法

连续出现的意味着相同数字的 Id 是连着的，由于这题问的是至少连续出现 3 次，我们使用 Logs 并检查是否有 3 个连续的相同数字。


SELECT *
FROM
    Logs l1,
    Logs l2,
    Logs l3
WHERE
    l1.Id = l2.Id - 1
    AND l2.Id = l3.Id - 1
    AND l1.Num = l2.Num
    AND l2.Num = l3.Num
;

作者：LeetCode
链接：https://leetcode-cn.com/problems/consecutive-numbers/solution/lian-xu-chu-xian-de-shu-zi-by-leetcode/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
然后我们从上表中选择任意的 Num 获得想要的答案。同时我们需要添加关键字 DISTINCT ，因为如果一个数字连续出现超过 3 次，会返回重复元素。

MySQL


SELECT DISTINCT
    l1.Num AS ConsecutiveNums
FROM
    Logs l1,
    Logs l2,
    Logs l3
WHERE
    l1.Id = l2.Id - 1
    AND l2.Id = l3.Id - 1
    AND l1.Num = l2.Num
    AND l2.Num = l3.Num
;

作者：LeetCode
链接：https://leetcode-cn.com/problems/consecutive-numbers/solution/lian-xu-chu-xian-de-shu-zi-by-leetcode/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
MySQL解法，供大家参考：

利用用户变量实现对连续出现的值进行计数：

select distinct Num as ConsecutiveNums
from (
  select Num, 
    case 
      when @prev = Num then @count := @count + 1
      when (@prev := Num) is not null then @count := 1
    end as CNT
  from Logs, (select @prev := null,@count := null) as t
) as temp
where temp.CNT >= 3
与自关联或自连接相比，这种方法的效率更高，不受Logs表中的Id是否连续的限制，而且可以任意设定某个值连续出现的次数。

针对评论区朋友的疑问，这里做下解答：

1）逻辑：构建两个变量@prev 和@count ，前者用于与Num做比较判断，后者用于@prev和Num相等时的条件计数；

2）(select @prev := null,@count := null) as t 这句的作用是初始化两个变量，并将初始化后的变量放到一张临时表t中，:=符号在MySQL中是赋值的意思；

3）when @prev = Num then @count := @count + 1和when (@prev := Num) is not null then @count := 1 这两个语句不能交换顺序，赋值语句永远非NULL，所以一旦执行顺序来到了第二个when，@count 是一定会被赋值为1的，后者放到前面的话就达不到计数的目的；

4）(@prev := Num) is not null这部分去掉后面加的判断，SQL也能正常执行，上面SQL中case when的这种用法，when后是判断条件，赋值后又加判断，我原以为这样会好理解点；

5）case when本质是一个函数，有值时就返回内部处理得到的值，无值就返回NULL，针对每一个Num，上面SQL中的case when 都会有一个计数，并把这个计数返回给CNT。

PS：MySQL8.0以后的版本开始支持窗口函数，使用窗口函数也能很好的解决此类问题。关于MySQL的窗口函数，可以参考译文：https://blog.csdn.net/qq_41080850/article/details/86416106
```

```sql
mysql 考虑得比较直接

select distinct a.Num as ConsecutiveNums
from Logs as a,Logs as b,Logs as c
where a.Num=b.Num and b.Num=c.Num and a.id=b.id-1 and b.id=c.id-1;
```

```sql
评论区竟然没有一样的。。。这题目是中等难度么。。。

SELECT DISTINCT Num AS ConsecutiveNums FROM Logs 
WHERE (Id+1, Num) IN (SELECT * FROM Logs)
AND (Id+2, Num) IN (SELECT * FROM Logs)
```

```sql
select distinct a.Num as ConsecutiveNums
from Logs a , Logs b , Logs c
where a.id + 1 = b.id and b.id + 1 = c.id and a.Num = b.Num and b.Num = c.Num;
```

```sql
用的lead()窗口函数

SELECT distinct num as ConsecutiveNums
FROM(
SELECT id,num,lead(num,1) over(order by id) as a,lead(num,2) over(ORDER BY id) as b
FROM `logs`
) t
WHERE num=a and a=b
```

```sql
窗口函数走起，适用于不同连续次数的取值~

select distinct num as ConsecutiveNums from 
(select num,id-cast(dense_rank() over (partition by num order by id asc) as signed)  as rn from logs) t1
group by rn,num
having count(*)>=3
```

```sql
select
    distinct Num as ConsecutiveNums
from
(
    select
        *,
        Id - (row_number() over(partition by Num order by Id) - 1) as diff 
    from Logs 
)t
group by Num, diff
having count(1) >= 3
```

```sql
可以使用lag窗口函数

select distinct Num as ConsecutiveNums
from
(select Num,
lag(Num,1) over(order by Id desc) as last1,
lag(Num,2) over(order by Id desc) as last2
from Logs) as t
where Num = last1 and last1= last2
```

```sql
通用解法，用同组必定差值一定的思路构建group，再用having统计数量。注意构建的group可能存在num值不一样的情况，所以group by要加上num

with t as
(select id, num, row_number() over(order by id) - row_number() over(partition by Num order by id) as gp
from logs)

select distinct num as  ConsecutiveNums 
from t 
group by gp, num
having count(num) >= 3
```

```sql
@过期少女 ⚡ 你好 我的思路和你差不多 想请问下为啥我的通过不了呢

select
distinct num ConsecutiveNums 
from
(
select
id,num,row_number() over(partition by num order by id) rn
from `logs`
) t
group by num,id-rn
having count(num)>=3

@满脑子都是暴力破解 没有考虑id从0开始的情况，还是要自己建立一个row_number列
```

###  4.6. <a name='-1'></a>181. 超过经理收入的员工

```sql
方法 1：使用 WHERE 语句
算法

如下面表格所示，表格里存有每个雇员经理的信息，我们也许需要从这个表里获取两次信息。


SELECT *
FROM Employee AS a, Employee AS b
;

作者：LeetCode
链接：https://leetcode-cn.com/problems/employees-earning-more-than-their-managers/solution/chao-guo-jing-li-shou-ru-de-yuan-gong-by-leetcode/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。

从两个表里使用 Select 语句可能会导致产生 笛卡尔乘积 。在这种情况下，输出会产生 4*4=16 个记录。然而我们只对雇员工资高于经理的人感兴趣。所以我们应该用 WHERE 语句加 2 个判断条件。


SELECT
    *
FROM
    Employee AS a,
    Employee AS b
WHERE
    a.ManagerId = b.Id
        AND a.Salary > b.Salary
;

作者：LeetCode
链接：https://leetcode-cn.com/problems/employees-earning-more-than-their-managers/solution/chao-guo-jing-li-shou-ru-de-yuan-gong-by-leetcode/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
由于我们只需要输出雇员的名字，所以我们修改一下上面的代码，得到最终解法：

MySQL


SELECT
    a.Name AS 'Employee'
FROM
    Employee AS a,
    Employee AS b
WHERE
    a.ManagerId = b.Id
        AND a.Salary > b.Salary
;

作者：LeetCode
链接：https://leetcode-cn.com/problems/employees-earning-more-than-their-managers/solution/chao-guo-jing-li-shou-ru-de-yuan-gong-by-leetcode/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
方法 2：使用 JOIN 语句
算法

实际上， JOIN 是一个更常用也更有效的将表连起来的办法，我们使用 ON 来指明条件。


SELECT
     a.NAME AS Employee
FROM Employee AS a JOIN Employee AS b
     ON a.ManagerId = b.Id
     AND a.Salary > b.Salary
;

作者：LeetCode
链接：https://leetcode-cn.com/problems/employees-earning-more-than-their-managers/solution/chao-guo-jing-li-shou-ru-de-yuan-gong-by-leetcode/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
还有一种子查询的方式:

 select a.Name as Employee 
 from Employee a, (select Salary,Id from Employee) b
 where a.ManagerId=b.Id and a.Salary > b.Salary 
```

```sql
自链接

select e1.Name as Employee
from employee e1, employee e2
where e1.ManagerId= e2.Id
and e1.Salary > e2.Salary
子链接

select e.Name as Employee
from employee e
where salary > (select salary from employee where Id = e.ManagerId)
```

```sql
# Write your MySQL query statement below
SELECT 
    Name Employee
FROM
    Employee AS a
WHERE
    Salary > (SELECT 
            Salary
        FROM
            Employee
        WHERE
            Id = a.Managerid)
```

```sql
# Write your MySQL query statement below
select e1.Name as Employee
from Employee e1
inner join Employee e2 
on e1.ManagerId = e2.Id
where e1.Salary > e2.Salary
果然还是实操 更容易理解连接查询
```

```sql
”和自己的xxx比”这种问题基本都是自连接问题。

SELECT E1.Name AS Employee FROM Employee as E1,Employee as E2
WHERE E1.ManagerId=E2.Id AND E1.Salary>E2.Salary;
```

```sql
纪念一下 自己写出来的第二题。。

select Name as 'Employee'
from Employee e
where Salary > (
select Salary from Employee where id = e.ManagerId
)
```

```sql
比较简单，直接关联

SELECT A.NAME Employee 
FROM Employee A , Employee B 
where A.ManagerId=B.Id  AND A.Salary>B.Salary

SELECT A.NAME Employee 
FROM Employee A 
LEFT JOIN Employee B ON A.ManagerId=B.Id 
where A.Salary>B.Salary
and a.ManagerId is not null
```

```sql
574ms

SELECT a.name as Employee 
FROM Employee a 
LEFT JOIN Employee b ON a.ManagerId = b.id 
WHERE a.salary > b.salary
628ms

SELECT a.Name AS Employee 
FROM Employee AS a,Employee AS b 
WHERE a.ManagerId = b.Id 
AND a.Salary > b.Salary

882ms

SELECT Name Employee 
FROM Employee E
WHERE Salary > 
(
select Salary from Employee where Id = E.ManagerId 
)
```

###  4.7. <a name='-1'></a>182. 查找重复的电子邮箱

```sql
方法一：使用 GROUP BY 和临时表
算法

重复的电子邮箱存在多次。要计算每封电子邮件的存在次数，我们可以使用以下代码。

MySQL

select Email, count(Email) as num
from Person
group by Email;

| Email   | num |
|---------|-----|
| a@b.com | 2   |
| c@d.com | 1   |
以此作为临时表，我们可以得到下面的解决方案。

MySQL

select Email from
(
  select Email, count(Email) as num
  from Person
  group by Email
) as statistic
where num > 1
;

作者：LeetCode
链接：https://leetcode-cn.com/problems/duplicate-emails/solution/cha-zhao-zhong-fu-de-dian-zi-you-xiang-by-leetcode/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
方法二：使用 GROUP BY 和 HAVING 条件
向 GROUP BY 添加条件的一种更常用的方法是使用 HAVING 子句，该子句更为简单高效。

所以我们可以将上面的解决方案重写为：

MySQL

select Email
from Person
group by Email
having count(Email) > 1;

作者：LeetCode
链接：https://leetcode-cn.com/problems/duplicate-emails/solution/cha-zhao-zhong-fu-de-dian-zi-you-xiang-by-leetcode/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
select distinct p1.Email as Email
from Person p1
inner join Person p2
on p1.Id!=p2.Id
where p1.Email=P2.Email;
```

```sql
-- 解法1
select email from person group by email having count(email)>1

--解法2
select email from (select count(1) as t,email from person group by email)r  where r.t>1;

--解法3
select distinct(p1.Email) from Person p1  
join Person  p2 on p1.Email = p2.Email AND p1.Id!=p2.Id
```

```sql
看做两个表，进行自连接。 连接条件：邮箱相同但人不同； 找到这个邮箱，进行去重

select distinct a.Email
from Person a
inner join Person b
on a.Email=b.Email and a.id!=b.id

```

```sql
啪的一下，很快啊

SELECT Email from Person group by Email having count(Email) > 1
```

```sql
having用于分组后的过滤，放到group by 后面，常和聚合函数一起用 where 用于全表数据集的筛选，在分组动作的前面

select Email from Person group by Email having count(Email)>=2;
select distinct(p1.Email) from Person p1 inner join Person p2 on p1.Email = p2.Email and p1.Id != p2.Id;
```

```sql
提交记录
先计数，再查找
SELECT
    Email 
FROM
    ( 
        SELECT Email, count(*) num FROM Person GROUP BY Email 
        ) AS count_table 
WHERE
    num > 1;
借助HAVING语句
SELECT
    Email 
FROM
    Person 
GROUP BY
    Email 
HAVING
    COUNT( Email ) > 1;
```

```sql
比葫芦画瓢....

顺便学习变量使用....

select distinct Email from (
    select Email,
        @times:=if(@pre=Email,@times+1,1) as times,
        @pre:=Email as preval
    from (
        select * from Person order by Email desc
    ) as tmp0,
    (
        select @times:=0,
               @pre:=''
    ) as tmp1
) as tmp2
where tmp2.times>1;
```

###  4.8. <a name='-1'></a>183. 从不订购的客户

```sql
方法：使用子查询和 NOT IN 子句
算法

如果我们有一份曾经订购过的客户名单，就很容易知道谁从未订购过。

我们可以使用下面的代码来获得这样的列表。


select customerid from orders;
然后，我们可以使用 NOT IN 查询不在此列表中的客户。

MySQL

select customers.name as 'Customers'
from customers
where customers.id not in
(
    select customerid from orders
);

作者：LeetCode
链接：https://leetcode-cn.com/problems/customers-who-never-order/solution/cong-bu-ding-gou-de-ke-hu-by-leetcode/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
左连接

select c.Name as Customers 
from Customers as c
left join Orders as o on c.Id = o.CustomerId
where o.Id is null
```

```sql
感觉官方少个distinct

select  Name as Customers
from Customers 
where Customers.Id not in
(
    select distinct CustomerID
    from Orders
)
```

```sql
不喜勿喷 （1）not in

 select t1.Name AS Customers from Customers AS t1 where id not in( select distinct id from Orders AS t2 ) 
（2）not exists

select t1.Name AS Customers from Customers AS t1 where id not exists( select 1 from Orders AS t2 where t1.id = t2.CustomerId ) 
（3）left join

SELECT t1.Name AS Customers FROM Customers AS t1 LEFT JOIN Orders AS t2 ON t1.Id = t2.CustomerId WHERE t2.Id IS NULL;
```

```sql
# 498ms
select c.Name as Customers from Customers c left join Orders o on o.CustomerId = c.Id where o.Id is null;

# 532ms
select c.Name as Customers from Customers c where not exists (select 1 from Orders o where o.CustomerId = c.Id);

# 455ms
select c.Name as Customers from Customers c where c.Id not in (select distinct o.CustomerId from Orders o);
```

```sql
SELECT Name 'Customers'
FROM Customers
WHERE Id NOT IN(
    SELECT CustomerId 
    FROM Orders
)
```

```sql
# 328ms 击败100%
select c.Name as Customers
from Customers c left join Orders o on c.Id=o.CustomerId
where o.CustomerId is null
```

```sql
执行用时：590 ms, 在所有 MySQL 提交中击败了23.51%的用户
select name as customers from customers where id not in (select customerid from orders)
换一个方法性能差不多，执行用时：592 ms, 在所有 MySQL 提交中击败了22.59%的用户
select c.name as Customers from Customers c left join Orders o on c.id = o.customerId where o.customerId is null
orders是大表,不推荐这种方法，执行用时：572 ms, 在所有 MySQL 提交中击败了34.63%的用户
select c.Name as Customers from Orders o right join Customers c on c.Id = o.CustomerId where o.Id is null
```

```sql
@呱呱编程实验室 having用在分组后，可以用聚合函数，where是用在分组前的，不能使用聚合函数

@季 谢谢回复，但我的理解是group by才叫分组？那我这里也没有用到分组呀

@呱呱编程实验室 但是他两都是条件筛选关键字，只是说having应该用在分组后的筛选

@呱呱编程实验室 having只能在group by 后面用，固定用法

@呱呱编程实验室 数据存在硬盘上，执行顺序是 from where 然后才是select，where是在硬盘上进行过滤，当你取出来以后就会放在内存中，having用在内存中的过滤
```

```sql
select Name 'Customers' from customers where Id not in (select CustomerId from Orders) 
```

```sql
not in不香吗？

SELECT name as customers from customers as c where c.id not in (SELECT customerid from orders)
```

```sql
select Name as Customers from Customers left join Orders on Customers.Id = Orders.CustomersId where Orders.CustomerId is null
```

###  4.9. <a name='-1'></a>184. 部门工资最高的员工

```sql
方法：使用 JOIN 和 IN 语句
算法

因为 Employee 表包含 Salary 和 DepartmentId 字段，我们可以以此在部门内查询最高工资。


SELECT
    DepartmentId, MAX(Salary)
FROM
    Employee
GROUP BY DepartmentId;
注意：有可能有多个员工同时拥有最高工资，所以最好在这个查询中不包含雇员名字的信息。


| DepartmentId | MAX(Salary) |
|--------------|-------------|
| 1            | 90000       |
| 2            | 80000       |
然后，我们可以把表 Employee 和 Department 连接，再在这张临时表里用 IN 语句查询部门名字和工资的关系。

MySQL


SELECT
    Department.name AS 'Department',
    Employee.name AS 'Employee',
    Salary
FROM
    Employee
        JOIN
    Department ON Employee.DepartmentId = Department.Id
WHERE
    (Employee.DepartmentId , Salary) IN
    (   SELECT
            DepartmentId, MAX(Salary)
        FROM
            Employee
        GROUP BY DepartmentId
	)
;

| Department | Employee | Salary |
|------------|----------|--------|
| Sales      | Henry    | 80000  |
| IT         | Max      | 90000  |

作者：LeetCode
链接：https://leetcode-cn.com/problems/department-highest-salary/solution/bu-men-gong-zi-zui-gao-de-yuan-gong-by-leetcode/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
in 这种类似元组的用法第一次见到。。。

我的解法比较原始：

select bb.Name as Department, aa.Employee, aa.Salary
from (
select 
       a.Id,
       a.Name as Employee,
       a.Salary,
       a.DepartmentId
  from Employee a
  join (
        select DepartmentId, max(Salary) as max_sal
          from Employee
         group by DepartmentId
       ) b on a.DepartmentId = b.DepartmentId and a.Salary = b.max_sal
) aa
join Department bb on aa.DepartmentId = bb.Id;
```

```sql
使用dense_rank() 的解决方案：

select aa.Department,
       aa.Employee,
       aa.Salary
  from (
        select 
               b.Name as Department,
               a.Name as Employee,
               a.Salary,
               dense_rank() over(partition by a.DepartmentId order by a.Salary desc) RN
          from Employee a
          join Department b on a.DepartmentId = b.Id
) aa
where aa.RN = 1;
```

```sql
为什么不可以直接对合并后的表格进行分组,然后选出最大值呢?这样做最大值只能显示一个

SELECT Department.name AS 'Department', 
Employee.name AS 'Employee', 
MAX( Employee.Salary) AS 'Salary' 
FROM Employee JOIN Department ON Employee.DepartmentId = Department.Id 
GROUP BY Department.Id ;
```

```sql
# Write your MySQL query statement below
select 
    d.Name as Department,
    e.Name as Employee,
    e.Salary 
from 
    Employee e,Department d 
where
    e.DepartmentId=d.id 
    and
    (e.Salary,e.DepartmentId) in (select max(Salary),DepartmentId from Employee group by DepartmentId);
```

```sql
利用开窗函数可以取每个部门最高，也可以取前二高，前三高，也可以只取第一第三，都OK的

-- 每个部门最高
SELECT S.NAME, S.EMPLOYEE, S.SALARY
  FROM (SELECT D.NAME,
               T.NAME EMPLOYEE,
               T.SALARY,
               ROW_NUMBER() OVER(PARTITION BY T.DEPARTMENTID ORDER BY T.SALARY DESC) RN
          FROM EMPLOYEE T
          LEFT JOIN DEPARTMENT D
            ON T.DEPARTMENTID = D.ID) S
 WHERE S.RN = 1
-- 每个部门前2高
SELECT S.NAME, S.EMPLOYEE, S.SALARY
  FROM (SELECT D.NAME,
               T.NAME EMPLOYEE,
               T.SALARY,
               ROW_NUMBER() OVER(PARTITION BY T.DEPARTMENTID ORDER BY T.SALARY DESC) RN
          FROM EMPLOYEE T
          LEFT JOIN DEPARTMENT D
            ON T.DEPARTMENTID = D.ID) S
 WHERE S.RN <= 2
-- 每个部门第一第三高
SELECT S.NAME, S.EMPLOYEE, S.SALARY
  FROM (SELECT D.NAME,
               T.NAME EMPLOYEE,
               T.SALARY,
               ROW_NUMBER() OVER(PARTITION BY T.DEPARTMENTID ORDER BY T.SALARY DESC) RN
          FROM EMPLOYEE T
          LEFT JOIN DEPARTMENT D
            ON T.DEPARTMENTID = D.ID) S
 WHERE S.RN = 1 OR S.RN = 3
```

```sql
很多人在用窗口函数，其实用传统方法更能锻炼思路，加深印象，本题是递归集合的又一经典用法。

select D.Name as Department,E.name as Employee,E.Salary
from Employee as E cross join Department as D
where E.DepartmentId=D.id
and exists(select null
            from Employee as P
            where P.DepartmentId=E.DepartmentId
            and E.Salary<=P.Salary
            having count(distinct P.Salary)=1)
```

```sql
使用left join，用时640 ms，击败了98.36%的用户

select t3.name department, t2.name Employee, salary
from (
         select departmentid, max(salary) max_salary
         from Employee
         group by departmentid
     ) t1
         left join Employee t2
                   on t1.departmentid = t2.departmentid and t1.max_salary = t2.salary
         left join department t3
                   on t1.departmentid = t3.id

为什么要用left join呢，子查询不是已经筛选出部门最高薪水了吗，全连接和left join感觉没区别呀
```

```sql
效率：99%

select
d.Name as Department,
m.Name as Employee,
Salary
from (select
Name,
DepartmentId,
Salary,
rank() over( partition by DepartmentId order by Salary desc) as rk 
from Employee
) as m left join Department d on m.DepartmentId = d.Id
where m.rk = 1
```

```sql
开窗函数真好用,已经用了连续三道了,自从学了开窗,都快把基础忘了.....

with temp as(select 
                d.Name Department,
                e.Name Employee,
                Salary,
                rank() over(partition by d.Id order by Salary desc) as `rank`
            from Employee e join Department d on e.DepartmentId=d.Id
            )
select Department,
       Employee,
       Salary
from temp
where `rank`=1;
```

```sql
看题目：就这，结果进来写了半天

select d.name as Department, e.name as Employee, e.salary
from 
Employee e,
Department d,
(select max(salary) as max,DepartmentId from Employee group by DepartmentId) as t
where e.salary = t.max and e.DepartmentId=t.DepartmentId and e.DepartmentId=d.Id;
```

```sql
窗口函数

select
    t.Department,t.Employee,t.Salary
from
    (select d.Name as Department, 
        e.Name as Employee,
        e.Salary as Salary,
        dense_rank() over(partition by e.DepartmentId order by e.Salary desc) ranking
        from Employee e,Department d 
        where e.DepartmentId=d.Id) as t
    where t.ranking=1;
```

```sql
一个平平无奇的窗口函数rank() over()解法，简洁易懂的一比，同时适用于查找其他位数的工资 自己写完觉得自己好牛批啊居然独立写了个窗口函数！！！【菜鸡的激动‘


select c.Department, c.Employee, c.Salary as Salary 
from (
    select e.Name as Employee, e.Salary, de.name as Department, rank() over(partition by DepartmentId order by Salary DESC) as Rk 
    from Employee as e 
    inner join Department as de on e.DepartmentId=de.Id) as c
    where Rk=1
```

```sql
将最大工资表查询出来加入连接，共连接三表，在我这里运行最快

# Write your MySQL query statement below
SELECT D.Name AS Department, E.Name AS Employee, E.Salary
FROM Employee E INNER JOIN Department D ON E.DepartmentId = D.Id
     INNER JOIN (SELECT DepartmentId, MAX(Salary) AS MaxSalary
                 FROM Employee
                 GROUP BY DepartmentId ) S ON E.DepartmentId = S.DepartmentId
WHERE E.Salary = MaxSalary
```

###  4.10. <a name='-1'></a>185. 部门工资前三高的所有员工

```sql
方法：使用 JOIN 和子查询
算法

公司里前 3 高的薪水意味着有不超过 3 个工资比这些值大。


select e1.Name as 'Employee', e1.Salary
from Employee e1
where 3 >
(
    select count(distinct e2.Salary)
    from Employee e2
    where e2.Salary > e1.Salary
)
;
在这个代码里，我们统计了有多少人的工资比 e1.Salary 高，所以样例的输出应该如下所示。


| Employee | Salary |
|----------|--------|
| Henry    | 80000  |
| Max      | 90000  |
| Randy    | 85000  |
然后，我们需要把表 Employee 和表 Department 连接来获得部门信息。

MySQL


SELECT
    d.Name AS 'Department', e1.Name AS 'Employee', e1.Salary
FROM
    Employee e1
        JOIN
    Department d ON e1.DepartmentId = d.Id
WHERE
    3 > (SELECT
            COUNT(DISTINCT e2.Salary)
        FROM
            Employee e2
        WHERE
            e2.Salary > e1.Salary
                AND e1.DepartmentId = e2.DepartmentId
        )
;

| Department | Employee | Salary |
|------------|----------|--------|
| IT         | Joe      | 70000  |
| Sales      | Henry    | 80000  |
| Sales      | Sam      | 60000  |
| IT         | Max      | 90000  |
| IT         | Randy    | 85000  |

作者：LeetCode
链接：https://leetcode-cn.com/problems/department-top-three-salaries/solution/bu-men-gong-zi-qian-san-gao-de-yuan-gong-by-leetco/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
/**
解题思路:先对Employee表进行部门分组工资排名，再关联Department表查询部门名称，再使用WHERE筛选出排名小于等于3的数据（也就是每个部门排名前3的工资）。
**/
SELECT 
B.Name AS Department,
A.Name AS Employee,
A.Salary
FROM (SELECT DENSE_RANK() OVER (partition by DepartmentId order by Salary desc) AS ranking,DepartmentId,Name,Salary
      FROM Employee) AS A
JOIN Department AS B ON A.DepartmentId=B.id
WHERE A.ranking<=3
```

```sql
这题题意差点把我搞蒙，差点以为是各部门获得工资前三的员工，难度为极难。感觉应该改为各部门工资级别前三的员工，可能更好理解一点。 解答也是差点给搞蒙了，不超过 3 个工资比这些值大，即小于等于，结果给我小于3； 原解答的意思是，工资级别大于第三级别工资的数量小于2。 写成以下这样可能好理解点。

SELECT
d.Name AS 'Department', e1.Name AS 'Employee', e1.Salary
FROM
 Employee e1
JOIN
Department d ON e1.DepartmentId = d.Id
WHERE
#工资级别数量小于等于3，即最多只有3个工资级别，也就是前三高
 3 >= (
SELECT 
COUNT(DISTINCT e2.Salary)
 FROM
Employee e2
  WHERE
#e2的工资级别大于等于e1的工资级别
 e2.Salary >= e1.Salary
 AND e1.DepartmentId = e2.DepartmentId
 )
ORDER BY e1.DepartmentId,e1.Salary DESC;
```

```sql
select
    base.Department,
    base.Employee,
    base.Salary
from (
    select
        Department.Name as 'Department',
        Employee.Name as 'Employee',
        Employee.Salary,
        # 同分不同名-连续：row_number()
        # 同分不同名-不连续：你猜？
        # 同分同名-连续：dense_rank()
        # 同分同名-不连续：rank()
        dense_rank() over(partition by Employee.DepartmentId order by Employee.Salary desc) as 'Ranking'
    from Employee
    inner join Department
    on Employee.DepartmentId = Department.Id
) as base
where base.Ranking <= 3
```

```sql
开窗函数解法，可以通过，但是实际上MySQL并不支持开窗函数的使用……

select tt.department,tt.employee,tt.salary from(
select  d.`name` department, e.`name` employee,e.salary salary,dense_rank() over (partition by e.departmentid order by e.salary desc) a
from employee e 
join department d 
on e.departmentid=d.id
) tt
where tt.a<4
```

```sql
1030 ms, 在所有MySQL提交中击败了91.48%的用户。 个人觉得刷SQL题，能不用函数就不要用函数，基本的语法明明能做的。受之前某一题的某个大佬启发，对于这种分组内取前几名的问题，可以先group by然后用having count()来筛选，比如这题，找每个部门的工资前三名，那么先在子查询中用Employee和自己做连接，连接条件是【部门相同但是工资比我高】，那么接下来按照having count(Salary) <= 2来筛选的原理是：如果【跟我一个部门而且工资比我高的人数】不超过2个，那么我一定是部门工资前三，这样内层查询可以查询出所有符合要求的员工ID，接下来外层查询就简单了。

select d.Name as Department,e.Name as Employee,e.Salary as Salary
from Employee as e left join Department as d 
on e.DepartmentId = d.Id
where e.Id in
(
    select e1.Id
    from Employee as e1 left join Employee as e2
    on e1.DepartmentId = e2.DepartmentId and e1.Salary < e2.Salary
    group by e1.Id
    having count(distinct e2.Salary) <= 2
)
and e.DepartmentId in (select Id from Department)
order by d.Id asc,e.Salary desc
```

```sql
SELECT P2.Name AS Department,P3.Name AS Employee,P3.Salary AS Salary
FROM Employee AS P3
INNER JOIN Department AS P2
ON P2.Id = P3.DepartmentId 
WHERE (
    SELECT COUNT(DISTINCT Salary)
    FROM Employee AS P4
    WHERE P3.DepartmentId = P4.DepartmentId
    AND P4.Salary >= P3.Salary
) <= 3
ORDER BY DepartmentId,Salary DESC
```

```sql
哈哈，比不过大佬

能用函数就用函数

select Department, Employee, Salary
from (
    select d.Name as Department, e.Name as Employee, e.Salary as Salary, 
dense_rank() over ( partition by DepartmentId order by Salary desc) as rk
    from Employee as e, Department as d
    where e.DepartmentId = d.Id
) m
where rk <= 3;
```

```sql
效率什么的不存在的，只会这样写 T T

# Write your MySQL query statement below
SELECT Department, Employee, salary
FROM(
SELECT b.Name as 'Department',a.Name as 'Employee', a.salary,
Dense_rank() over(partition by b.Name ORDER by a.salary DESC) as 'rank'
FROM Employee a left join Department b ON a.DepartmentId = b.Id
) temp
WHERE temp.rank <= 3
```

```sql
无需子查询

select 
        d.name Department,
        e1.name Employee,
        e1.salary
from employee e1, employee e2, Department d
where e1.departmentid = e2.departmentid and 
           e1.salary <= e2.salary and 
           e1.departmentid = d.id
group by e1.id, e1.name, e1.departmentid
having count(distinct e2.salary) <= 3
```

```sql
#解法1：窗口函数
SELECT
	d. NAME AS Department,
	e. NAME AS Employee,
	Salary
FROM
	Employee e,
	Department d,
	(
		SELECT
			Id,
			dense_rank()over(PARTITION BY DepartmentId ORDER BY Salary DESC) AS r
		FROM
			Employee
	) AS n
WHERE
	n.Id = e.Id
AND e.DepartmentId = d.Id
AND r < 4
ORDER BY
	d. NAME,
	Salary DESC


#解法2：自连接
SELECT
	d. NAME AS Department,
	e2. NAME AS Employee,
	e2.Salary
FROM
	Employee e1,
	Employee e2,
	Department d
WHERE
	e1.DepartmentId = e2.DepartmentId
AND e1.Salary >= e2.Salary
AND e2.DepartmentId = d.id
GROUP BY
	e2.Salary,
	e2. NAME,
	Department
HAVING
	count(DISTINCT e1.Salary) <= 3
ORDER BY
	Department,
	e2.Salary DESC


#解法3：变量
SELECT 
    d.name as Department,
    t.Employee,
    t.salary
FROM Department d
JOIN
(
SELECT 
	DepartmentId,
    name as Employee,
    Salary,
    @rk:=(@pre_id<>DepartmentId)+
        (@pre_id=(@pre_id:=DepartmentId))*
            (@rk+(@pre_salary<>(@pre_salary:=Salary))) as rk
from Employee,(SELECT @pre_id:=0,@pre_salary:=0,@rk:=0)as v
order by DepartmentId,Salary desc
)as t
ON d.id=t.DepartmentId
WHERE rk<=3

```

```sql
# Write your MySQL query statement below
select d.name Department,e.name Employee,e.salary Salary
from employee e left join Department d on e.Departmentid = d.id
where 3>(
    select count(distinct e2.salary)
    from employee e2
    where e2.salary>e.salary
    and e2.Departmentid = d.id
)
```

```sql
select department,employee,salary
from(
	select 
		d.name as department,
		e.name as employee,
		e.salary as salary,
		rank() over(partition by d.name order by e.salary desc) as rank1
	from employee as e
	left join department as d on e.departmentid=d.id
) as t
where rank1 in (1,2,3)
order by salary desc,employee desc;
为什么我这种做法显示错误？我在我自己的workbench上运行的结果和答案一模一样啊
@奥斯特洛夫斯基 应该是其他测试用例没通过，Rank()函数换成DENSE_RANK()即可，两个还是有区别的，你可以去百度一下
```

```sql
495 ms，击败了99.52%的用户 内存消耗：0 B，击败了100.00%的用户

select d.Name as Department, e2.Name as Employee, Salary 
from (
    select Name, Salary, DepartmentId
    from (
        select *, dense_rank() over(partition by DepartmentId order by Salary desc) as ranking
        from Employee
    ) as temp
    where ranking < 4
) as e2
left join Department d
on e2.DepartmentId = d.Id
where d.Id IS NOT NULL;
这里可以把left join 换成 inner join, 这样就可以不用where d.Id is not null了
```

```sql
select  d.name as Department,e.name as Employee,Salary 
from Employee e left join Department d on e.DepartmentId=d.id,
(select name, dense_rank() over (partition by DepartmentId order by salary) as salrank from Employee) rank
where e.name=rank.name and salrank between 1 and 3;
有没有大神帮俺看看，在这上面运行报错，用自己的软件却可以，呜呜呜
关键词rank重名了，改名就好
```

```sql
我的不成熟想法 第一步，分组排序并得到后续需要的元素

select DepartmentId , Name , Salary,
dense_rank()over (partition by DepartmentId order by Salary desc) n 
from Employee
第二步，求出结果

select
t1.DepartmentId Department,t1.Name Employee,t1.Salary
from 
t1 left join Department on t1.DepartmentId = t2.Id
where t1.n < 4;
两步合并，最后代码

select
t2.Name Department,t1.Name Employee,t1.Salary
from 
(select DepartmentId , Name , Salary,
dense_rank()over (partition by DepartmentId order by Salary desc) n 
from Employee )t1 
left join Department t2 on t1.DepartmentId = t2.Id
where t1.n < 4;
```

###  4.11. <a name='-1'></a>196. 删除重复的电子邮箱

```sql
方法：使用 DELETE 和 WHERE 子句
算法

我们可以使用以下代码，将此表与它自身在电子邮箱列中连接起来。

MySQL

SELECT p1.*
FROM Person p1,
    Person p2
WHERE
    p1.Email = p2.Email
;
然后我们需要找到其他记录中具有相同电子邮件地址的更大 ID。所以我们可以像这样给 WHERE 子句添加一个新的条件。

MySQL

SELECT p1.*
FROM Person p1,
    Person p2
WHERE
    p1.Email = p2.Email AND p1.Id > p2.Id
;
因为我们已经得到了要删除的记录，所以我们最终可以将该语句更改为 DELETE。

MySQL

DELETE p1 FROM Person p1,
    Person p2
WHERE
    p1.Email = p2.Email AND p1.Id > p2.Id

作者：LeetCode
链接：https://leetcode-cn.com/problems/delete-duplicate-emails/solution/shan-chu-zhong-fu-de-dian-zi-you-xiang-by-leetcode/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
delete from Person
where Id not in (select * from (select min(Id) from Person group by Email) as x )

@KyrieX 请问这里为什么要使用两个select呢？外面一层的select不是把里面一层完全重复一次吗

@wwwwwwj 去掉外层select 会报错 You can't specify target table 'Person' for update in FROM clause

@KyrieX sql 小白問一下,是不是这个代码只适合用于删除几条数据中最大id的行，那如果在几百上千条数据中想要找重复值再删除最大id的是不是不行..
```

```sql
delete p1 from Person p1 ,Person p2
where p1.Email =p2.Email and p1.Id > p2.Id 多表删除，把p1大于p2的ID删除了
```

```sql
delete p1
from person p1
inner join person p2
on p1.Email=p2.Email And p1.Id>p2.Id
```

```sql
DELETE from Person 
Where Id not in (
    Select Id 
    From(
    Select MIN(Id) as id
    From Person 
    Group by Email
   ) t
)
```

```sql
注意审题 执行 SQL 之后，输出是整个 Person 表。 使用 delete 语句。

不是查重！

#子链接
delete p1
from Person p1 ,Person s1 
where p1.email = s1.email and p1.id > s1.id
```

```sql
MySQL环境下：

delete from Person 
where Id not in (
    select * from(
        select min(Id)
        from Person
        group by Email) t);
注意：在MYSQL中，不能先Select一个表的记录，再按此条件Update和Delete同一个表的记录，否则会出错：You can't specify target table 'xxx' for update in FROM clause.
```

```sql
解决方法：使用嵌套Select——将Select得到的查询结果作为中间表，再Select一遍中间表作为结果集，即可规避错误。

另外，请注意本题题意，提示要用Delete删除重复项，所以以下傻白甜方法不能使用 T-T

select min(Id), Email
from Person
group by Email;
```

```sql
思路：先根据email进行分组，然后找出Id最小的保留。删除表时只需要让Id 不在上述范围内即可

DELETE from Person 
Where Id not in 
(
    select t.id from   
    --加上这个外层筛选可以避免You can't specify target table for update in FROM clause错误
    (
        Select MIN(Id) as id
        From Person 
        Group by Email
    ) t
)
```

```sql
执行用时： 1107 ms, 在所有 MySQL 提交中击败了90.39%的用户


delete from Person where Id in (
    select Id
    from(
        select *, row_number() over (partition by Email order by Id) rk
        from Person
    ) t1
    where rk>1
)

```

```sql
执行用时： 1107 ms, 在所有 MySQL 提交中击败了90.39%的用户


delete from Person where Id in (
    select Id
    from(
        select *, row_number() over (partition by Email order by Id) rk
        from Person
    ) t1
    where rk>1
)

```

```sql
感谢启发，没想到delete还有这种用法：就是删除两个连接以后的表其中的一个表。

按照这个思路，我换了一种连接办法：使用join进行两个表的自连接，连接条件是【邮箱相同的条件下，表1的所有Id号码都大于表2的Id】，从而得到的结果就是:P2表保留了Id最小的邮箱。因此，只需要删除P1表即满足题意。

delete  p1 
from Person p1 join Person p2 
on p1.email = p2.email and p1.id>p2.id
```

```sql
group by 只是查询时去重，并没有真正删除表中数据

Delete p1.* from Person p1,Person p2 where p1.id>p2.id and p1.Email=p2.Email
```

```sql
之前提交的一直错 报错：
-- You can't specify target table 'Person' for update in FROM clause

delete from Person 
where Id not in
    (
    select min(p2.Id)
    from  Person p2  
    group by p2.Email
    )
修改成以下写法就行，意思就是变个方向，让数据库认为你不是查同一表的数据作为同一表的更新数据： 答案：

delete from Person 
where Id not in
    (
    select min(p2.Id)
    from  (select * from Person) p2  
    group by p2.Email
    )
或者

delete from Person 
where Id not in
    (
        select *
        from (
            select min(p2.Id)
            from  Person p2  
            group by p2.Email
        ) p3
    )
```

```sql
delete p1
from person p1 join person p2
on p1.email=p2.email and p1.id>p2.id
```

###  4.12. <a name='-1'></a>197. 上升的温度

```sql
方法：使用 JOIN 和 DATEDIFF() 子句
算法

MySQL 使用 DATEDIFF 来比较两个日期类型的值。

因此，我们可以通过将 weather 与自身相结合，并使用 DATEDIFF() 函数。

MySQL

SELECT
    weather.id AS 'Id'
FROM
    weather
        JOIN
    weather w ON DATEDIFF(weather.date, w.date) = 1
        AND weather.Temperature > w.Temperature
;

作者：LeetCode
链接：https://leetcode-cn.com/problems/rising-temperature/solution/shang-sheng-de-wen-du-by-leetcode/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
不需要join

SELECT w2.Id
FROM Weather w1, Weather w2
WHERE DATEDIFF(w2.RecordDate, w1.RecordDate) = 1
AND w1.Temperature < w2.Temperature
```

```sql
使用Lead函数，

执行用时：302 ms, 在所有 MySQL 提交中击败了91.26%的用户；
内存消耗：0 B, 在所有 MySQL 提交中击败了100.00%的用户；
select Id
from 
(
	select 
		temperature,
		recordDate,
		lead(id,1) over (order by recordDate) as 'Id',
		lead(recordDate,1) over (order by recordDate) as 'nextDate',
		lead(temperature,1) over (order by recordDate) as 'nextTemp'
	from weather 
)t 
where nextTemp > temperature and DATEDIFF(nextDate, recordDate) = 1
```

```sql
select w1.id id from Weather w1 
left join Weather w2
on w1.recordDate = date_add(w2.recordDate, interval 1 day) 
where w1.Temperature > w2.Temperature 
```

```sql
与题解类似,不同实现

SELECT w1.Id FROM Weather w1, Weather w2 WHERE w1.RecordDate = DATE_ADD(w2.RecordDate,INTERVAL 1 DAY) AND w1.Temperature > w2.Tem
```

```sql
463 ms, 在所有 MySQL 提交中击败了98.58% 的用户 连接查询，然后组合两个条件
select a.Id from  Weather as a join Weather as b on a.Temperature > b.Temperature and dateDiff(a.RecordDate,b.RecordDate) = 1 
```

```sql
执行用时 : 339 ms, 在Rising Temperature的MySQL提交中击败了99.48% 的用户

# Write your MySQL query statement below
select
    Id
from
    (select w.*,
     @curd := w.RecordDate,
     @curt := w.Temperature,
     @isH := if(datediff(@curd,@pred) = 1 and @curt > @pret,1,0) as r,
     @pret := @curt,
     @pred := @curd
     from
        Weather w,
        (select 
            @curd := null,
            @pred := null,
            @curt := 0,
            @pret := 0,
            @isH := 0
        ) init
     order by w.RecordDate
    ) t

where
    t.r = 1
```

```sql
看了下大家写的我觉得我写得好蠢.........

SELECT id
FROM
(
    SELECT id, temperature, recordDate,
    lag(temperature, 1) over(order by recordDate) as 'temperature_yesterday',
    lag(recordDate, 1) over(order by recordDate) as 'date_yesterday'
    FROM Weather
) temp
WHERE temperature > temperature_yesterday
and datediff(recordDate, date_yesterday) = 1
```

```sql
大佬们咨询一个问题，我知道datediff是可以实现的，但是用日期+1的方式也可以通过大部分的测试用例，但是最后一个用例没通过，想问一下用函数和直接加数值有什么区别？

select a.id
from
Weather a join Weather b
on
a.recordDate      = b.recordDate     + 1
where a.Temperature > b.Temperature


我觉得是时间问题，就切换时间格式试了下，发现果真是

select a.Id
from
Weather a join Weather b
on date_format(a.recordDate,'%Y-%m-%d %H') = date_add(b.recordDate,interval 1 day)
where a.Temperature > b.Temperature



 create database test; use test; create table date_time( t1 date, t2 date ); insert into date_time values('2022-4-13','2021-4-13'); select t1-t2 from date_time; -- mysql的时间相减做了一个隐式转换操作， -- 将时间转换为整数，仅仅是直接将年月日拼起来 然后计算 这边结果输出是10000 啊这，我不会粘图片啊
```

```sql
select a.id
from Weather a,Weather b
Where a.Temperature>b.Temperature and datediff(a.recordDate,b.recordDate)=1

```

```sql
select w1.id
from weather w1,weather w2
where datediff(w1.recorddate,w2.recorddate)=1 and w1.temperature>w2.temperature;
--select w1.id --from Weather w1, Weather w2 --where w1.id = (w2.id)+1 and w1.temperature>w2.temperature 我觉得逻辑是一样的，但是为什么我的会报错呢
id大的不一定日期就大
```

```sql
datediff(date1, date2) = date1 - date2 少用full join 不要用lead和lag - 时间可能不连续

select w1.id
from weather w1
left join weather w2
on datediff(w1.recorddate, w2.recorddate) = 1
where w1.temperature > w2.temperature
```

```sql
select b.id
  from Weather a, Weather b
 where DATE_ADD(a.recordDate, INTERVAL 1 day) = b.recordDate
   and b.temperature > a.temperature
```

```sql
使用lag函数也是可以的

select id 
from (SELECT *,LAG(Temperature,1) OVER(ORDER BY recordDate ) lg_tp ,LAG(recordDate) OVER(ORDER BY recordDate) lg_rd
FROM Weather) as temp
where lg_tp<Temperature and datediff(recordDate,lg_rd) = 1
```

```sql
为什么258ms只能打败91%，看前面339ms的兄弟打败了99.48%

select a.id from weather a,weather b 
where DATE_SUB(a.recordDate, INTERVAL 1 DAY)=b.recordDate 
and a.temperature>b.temperature;
```

###  4.13. <a name='-1'></a>262. 行程和用户

```sql
SELECT T.request_at AS `Day`, 
	ROUND(
			SUM(
				IF(T.STATUS = 'completed',0,1)
			)
			/ 
			COUNT(T.STATUS),
			2
	) AS `Cancellation Rate`
FROM Trips AS T
JOIN Users AS U1 ON (T.client_id = U1.users_id AND U1.banned ='No')
JOIN Users AS U2 ON (T.driver_id = U2.users_id AND U2.banned ='No')
WHERE T.request_at BETWEEN '2013-10-01' AND '2013-10-03'
GROUP BY T.request_at

作者：jason-2
链接：https://leetcode-cn.com/problems/trips-and-users/solution/san-chong-jie-fa-cong-nan-dao-yi-zong-you-gua-he-n/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
SELECT T.request_at AS `Day`, 
	ROUND(
			SUM(
				IF(T.STATUS = 'completed',0,1)
			)
			/ 
			COUNT(T.STATUS),
			2
	) AS `Cancellation Rate`
FROM trips AS T,
(
	SELECT users_id
	FROM users
	WHERE banned = 'Yes'
) AS A
WHERE (T.Client_Id != A.users_id AND T.Driver_Id != A.users_id) AND T.request_at BETWEEN '2013-10-01' AND '2013-10-03'
GROUP BY T.request_at

作者：jason-2
链接：https://leetcode-cn.com/problems/trips-and-users/solution/san-chong-jie-fa-cong-nan-dao-yi-zong-you-gua-he-n/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
SELECT T.request_at AS `Day`, 
	ROUND(
			SUM(
				IF(T.STATUS = 'completed',0,1)
			)
			/ 
			COUNT(T.STATUS),
			2
	) AS `Cancellation Rate`
FROM trips AS T LEFT JOIN 
(
	SELECT users_id
	FROM users
	WHERE banned = 'Yes'
) AS A ON (T.Client_Id = A.users_id)
LEFT JOIN (
	SELECT users_id
	FROM users
	WHERE banned = 'Yes'
) AS A1
ON (T.Driver_Id = A1.users_id)
WHERE A.users_id IS NULL AND A1.users_id IS NULL AND T.request_at BETWEEN '2013-10-01' AND '2013-10-03'
GROUP BY T.request_at

作者：jason-2
链接：https://leetcode-cn.com/problems/trips-and-users/solution/san-chong-jie-fa-cong-nan-dao-yi-zong-you-gua-he-n/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
SELECT T.request_at AS `Day`, 
	ROUND(
			SUM(
				IF(T.STATUS = 'completed',0,1)
			)
			/ 
			COUNT(T.STATUS),
			2
	) AS `Cancellation Rate`
FROM trips AS T
WHERE 
T.Client_Id NOT IN (
	SELECT users_id
	FROM users
	WHERE banned = 'Yes'
)
AND
T.Driver_Id NOT IN (
	SELECT users_id
	FROM users
	WHERE banned = 'Yes'
)
AND T.request_at BETWEEN '2013-10-01' AND '2013-10-03'
GROUP BY T.request_at

作者：jason-2
链接：https://leetcode-cn.com/problems/trips-and-users/solution/san-chong-jie-fa-cong-nan-dao-yi-zong-you-gua-he-n/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
# Write your MySQL query statement below

#对Trips表和Users表连接，连接条件是行程对应的乘客非禁止且司机非禁止
#筛选订单日期在目标日期之间（BETWEEN AND）
#用日期进行分组（GROUP BY）
#分别统计所有订单数和被取消的订单数，其中取消订单数用一个bool条件来得到0或1，再用avg求均值
#对订单取消率保留两位小数，对输出列名改名。（round）

SELECT
    Request_at 'Day', round(avg(Status!='completed'), 2) 'Cancellation Rate'
FROM Trips t 
    JOIN Users u1 ON (t.Client_id = u1.Users_id AND u1.Banned = 'No')
    JOIN Users u2 ON (t.Driver_id = u2.Users_id AND u2.Banned = 'No')
WHERE	
    Request_at BETWEEN '2013-10-01' AND '2013-10-03'
GROUP BY 
    Request_at;
```

```sql
# Write your MySQL query statement below
select
    t.request_at Day, 
    (
        round(count(if(status != 'completed', status, null)) / count(status), 2)
    ) as 'Cancellation Rate'
from
    Users u inner join Trips t
on
    u.Users_id = t.Client_Id
and
    u.banned != 'Yes'
where
    t.Request_at >= '2013-10-01'
and
    t.Request_at <= '2013-10-03'
group by
    t.Request_at
```

```sql
SELECT a.request_at as day,
ROUND(COUNT(IF(status != 'completed',status,null))/COUNT(status),2) as `cancellation rate`
FROM trips a
JOIN users b
ON b.users_id = a.driver_id 
JOIN users c
ON c.users_id = a.client_id
WHERE b.banned = 'No'
AND c.banned = 'No'
AND a.request_at between '2013-10-01' AND '2013-10-03'
GROUP BY a.request_at
```

```sql
with t as (
select * from Trips
where Client_id not in (select Users_id from users where Banned = 'Yes')
and   Driver_id not in (select Users_id from users where Banned = 'Yes')
and   Request_at between "2013-10-01" and "2013-10-03")
select Request_at  as Day,
round(sum(case when Status like 'cancel%' then 1 else 0 end)/count(ID),2) as "Cancellation Rate"
from t
Group by Request_at
```

```sql
test case有严重的bug，我研究了一天，把和driver相关的条件删掉就没问题了，I was like exfkingcuseme？？？ 题目已经很明确的说了： “Write a SQL query to find the cancellation rate of requests with unbanned users 【【【both client and driver must not be banned】】】 each day between "2013-10-01" and "2013-10-03"”

这是我提交的代码，fail了，然后我根据test case看了一下，根据题意，我这么写绝对是正确的；反而后面如果删掉第二个left join（关于“driver_id”的限定），就成功了。

select request_at as Day,
       round(sum(case when t.status like "cancelled%" then 1 else 0 end) / count(*),2) as 'Cancellation Rate'
from trips t
join users u1
on (t.client_id = u1.users_id and u1.banned = "No")
join users u2
on (t.driver_id = u2.users_id and u2.banned = "No")
where request_at between "2013-10-01" and "2013-10-03"
group by request_at
order by 1
输入

{"headers": {"Trips": ["Id", "Client_Id", "Driver_Id", "City_Id", "Status", "Request_at"], "Users": ["Users_Id", "Banned", "Role"]}, "rows": {"Trips": [["1", "1", "10", "1", "completed", "2013-10-01"], ["2", "2", "11", "1", "cancelled_by_driver", "2013-10-01"], ["3", "3", "12", "6", "completed", "2013-10-01"], ["4", "4", "13", "6", "cancelled_by_client", "2013-10-01"], ["5", "1", "10", "1", "completed", "2013-10-02"], ["6", "2", "11", "6", "completed", "2013-10-02"], ["7", "3", "12", "6", "completed", "2013-10-02"], ["8", "2", "12", "12", "completed", "2013-10-03"], ["9", "3", "10", "12", "completed", "2013-10-03"], ["10", "4", "13", "12", "cancelled_by_driver", "2013-10-03"]], "Users": [["1", "No", "client"], ["2", "Yes", "client"], ["3", "No", "client"], ["4", "No", "client"], ["10", "Yes", "driver"], ["11", "Yes", "driver"], ["12", "Yes", "driver"], ["13", "No", "driver"]]}}
如果按照题目所说，根据以上这个这个test case -> user里面：client【2】是banned的，driver【10、11、12】都是banned的，整个trips table里面会被take out的有效row只有两行，分别是2013-10-01的【client4 - driver13】组合和2013-10-03的【client4 - driver13】组合，而这两个都是cancelled的行程，which means cancellation rate是100%... 以下是我根据我“错误的”代码我的输出⬇️

输出

{"headers": ["Day", "Cancellation Rate"], "values": [["2013-10-03", 1.00], ["2013-10-01", 1.00]]}
然而题目的预期结果是 ⬇️

预期结果

{"headers": ["Day", "Cancellation Rate"], "values": [["2013-10-01", 0.33], ["2013-10-02", 0.00], ["2013-10-03", 0.50]]}
如何给这边团队报错？这题真的搞得我贼上火。
```

###  4.14. <a name='-1'></a>595. 大的国家

```sql
方法一：使用 WHERE 子句和 OR【通过】
思路

使用 WHERE 子句过滤所有记录，获得满足条件的国家。

算法

根据定义，大国家至少满足以下两个条件中的一个：

面积超过 300 万平方公里。

人口超过 2500 万。

使用下面语句获得满足条件 1 的大国家。


SELECT name, population, area FROM world WHERE area > 3000000
使用下面语句获得满足条件 2 的大国家。


SELECT name, population, area FROM world WHERE population > 25000000
使用 OR 将两个子查询合并在一起。

MySQL


SELECT
    name, population, area
FROM
    world
WHERE
    area >= 3000000 OR population >= 25000000
;

作者：LeetCode
链接：https://leetcode-cn.com/problems/big-countries/solution/da-de-guo-jia-by-leetcode/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
方法二：使用 WHERE 子句和 UNION【通过】
算法

该方法思路与 方法一 一样，但是使用 UNION 连接子查询。

MySQL


SELECT
    name, population, area
FROM
    world
WHERE
    area >= 3000000

UNION

SELECT
    name, population, area
FROM
    world
WHERE
    population >= 25000000
;
注：方法二 比 方法一 运行速度更快，但是它们没有太大差别。

作者：LeetCode
链接：https://leetcode-cn.com/problems/big-countries/solution/da-de-guo-jia-by-leetcode/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
说个鼓舞人心的话吧 一个月前我刚学且刚开始刷leetcode的sql， 可能就只有这道题我是一遍过的，当时觉得天啊我真的能学会sql吗 特别崩溃 然后慢慢的一边看大家的评论和题解，10天刷完了整个sql题库 今天我开始第二次刷leetcode的sql题库（换了个号），目前基本上没遇到不会的题 共勉

select name, population, area
from world
where area > 3000000 or population > 25000000
```



```sql
这样才是对的

select name, population, area
from world
where area >= 3000000 or population >= 25000000
```

```sql
原来我还是会点mysql的。

select 
    name, population, area
from
    World
where 
    population > 25000000 or area > 3000000
```

###  4.15. <a name='-1'></a>596. 超过5名学生的课

```sql
方法一：使用 GROUP BY 子句和子查询【通过】
思路

先统计每门课程的学生数量，再从中选择超过 5 名学生的课程。

算法

使用 GROUP BY 和 COUNT 获得每门课程的学生数量。

MySQL

SELECT
    class, COUNT(DISTINCT student)
FROM
    courses
GROUP BY class
;
注：使用 DISTINCT 防止在同一门课中学生被重复计算。


| class    | COUNT(student) |
|----------|----------------|
| Biology  | 1              |
| Computer | 1              |
| English  | 1              |
| Math     | 6              |
使用上面查询结果的临时表进行子查询，筛选学生数量超过 5 的课程。

MySQL

SELECT
    class
FROM
    (SELECT
        class, COUNT(DISTINCT student) AS num
    FROM
        courses
    GROUP BY class) AS temp_table
WHERE
    num >= 5
;
注：COUNT(student) 不能直接在 WHERE 子句中使用，这里将其重命名为 num。

作者：LeetCode
链接：https://leetcode-cn.com/problems/classes-more-than-5-students/solution/chao-guo-5ming-xue-sheng-de-ke-by-leetcode/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
方法二：使用 GROUP BY 和 HAVING 条件【通过】
算法

在 GROUP BY 子句后使用 HAVING 条件是实现子查询的一种更加简单直接的方法。

MySQL

MySQL

SELECT
    class
FROM
    courses
GROUP BY class
HAVING COUNT(DISTINCT student) >= 5
;

作者：LeetCode
链接：https://leetcode-cn.com/problems/classes-more-than-5-students/solution/chao-guo-5ming-xue-sheng-de-ke-by-leetcode/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
排名没啥参考意义，击败 30% ~ 80% 不等。

#共三种写法
#最朴实的写法，共三层查询，先利用 DISTINCT 去掉重复记录得到表 A，再利用 GROUP BY 为 CLASS 分组，然
#后用 COUNT() 统计每组个数得到表 B，最后在最外层限定数量 >=5 查到结果
SELECT B.CLASS								#最外层
	FROM (SELECT A.CLASS,COUNT(A.CLASS) C          #第二层查询，得到具有 CLASS、COUNT(CLASS) 的表 B
		FROM (SELECT DISTINCT *				#第三层查询，去重得到表 A
			FROM COURSES) A
	GROUP BY A.CLASS) B						#分组
	WHERE B.C >= 5;							#条件

#稍微优化，两层查询，主要是因为用了 HAVING 省了一层查询
SELECT A.CLASS					#最外层
	FROM (SELECT DISTINCT *		#第二层查询，去重得到表 a
		FROM COURSES) A
	GROUP BY A.CLASS			        #分组
	HAVING COUNT(A.CLASS) >= 5;	#利用 COUNT() 计算每组个数并筛选

#极致优化，一层查询，利用 GROUP BY 为 CLASS 分组后，直接用 COUNT() 统计每组学生个数，在统计前先用
#DISTINCT 去掉重复学生
SELECT CLASS
	FROM COURSES
	GROUP BY CLASS							#分组
	HAVING COUNT(DISTINCT STUDENT) >= 5;          #利用 COUNT() 统计每门课 STUDENT 的个数，同时利
                                                                                        #用 DISTINCT 去掉重复
```

```sql
想问一下我能不能直接这样写，跟加上一个 from (select distinct * from courses) 有什么区别呢

select class from courses
group by class 
having count(class) >= 5
```

```sql
always remember to use distinct

select class
from courses
group by class
having count(distinct student) >= 5
```

```sql
select
    class 
from courses 
group by class 
having(count(distinct(student))>=5)
```

```sql
SELECT class
FROM courses
Group By class
HAVING COUNT(DISTINCT(student)) >= 5;
```

```sql
select
    class
from courses
group by class
having count(student)>=5;
```

```sql
执行用时：288 ms,在所有 MySQL 提交中击败了70.01%的用户 内存消耗：0 B,在所有 MySQL 提交中击败了100.00%的用户

select class
from courses
group by 1
having count(*) >=5;
```

```sql
select class from courses group by class having count(distinct student)>=5
```

###  4.16. <a name='-1'></a>601. 体育馆的人流量

```sql
方法：使用 JOIN 和 WHERE 子句【通过】
思路

在表 stadium 中查询人流量超过 100 的记录，将查询结果与其自身的临时表连接，再使用 WHERE 子句获得满足条件的记录。

算法

第一步：查询人流量超过 100 的记录，然后将结果与其自身的临时表连接。

MySQL

select distinct t1.*
from stadium t1, stadium t2, stadium t3
where t1.people >= 100 and t2.people >= 100 and t3.people >= 100
;

作者：LeetCode
链接：https://leetcode-cn.com/problems/human-traffic-of-stadium/solution/ti-yu-guan-de-ren-liu-liang-by-leetcode/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。

共有 6 天人流量超过 100 人，笛卡尔积 后有 216（666） 条记录。
前 3 列来自表 t1，中间 3 列来自表 t2，最后 3 列来自表 t3。
表 t1，t2 和 t3 相同，需要考虑添加哪些条件能够得到想要的结果。以 t1 为例，它有可能是高峰期的第 1 天，第 2 天，或第 3 天。

t1 是高峰期第 1 天：(t1.id - t2.id = 1 and t1.id - t3.id = 2 and t2.id - t3.id =1) -- t1, t2, t3
t1 是高峰期第 2 天：(t2.id - t1.id = 1 and t2.id - t3.id = 2 and t1.id - t3.id =1) -- t2, t1, t3
t1 是高峰期第 3 天：(t3.id - t2.id = 1 and t2.id - t1.id =1 and t3.id - t1.id = 2) -- t3, t2, t1
MySQL

select t1.*
from stadium t1, stadium t2, stadium t3
where t1.people >= 100 and t2.people >= 100 and t3.people >= 100
and
(
	  (t1.id - t2.id = 1 and t1.id - t3.id = 2 and t2.id - t3.id =1)  -- t1, t2, t3
    or
    (t2.id - t1.id = 1 and t2.id - t3.id = 2 and t1.id - t3.id =1) -- t2, t1, t3
    or
    (t3.id - t2.id = 1 and t2.id - t1.id =1 and t3.id - t1.id = 2) -- t3, t2, t1
)
;

| id | date       | people |
|----|------------|--------|
| 7  | 2017-01-07 | 199    |
| 6  | 2017-01-06 | 1455   |
| 8  | 2017-01-08 | 188    |
| 7  | 2017-01-07 | 199    |
| 5  | 2017-01-05 | 145    |
| 6  | 2017-01-06 | 1455   |
可以看到查询结果中存在重复的记录，再使用 DISTINCT 去重。

MySQL

MySQL

select distinct t1.*
from stadium t1, stadium t2, stadium t3
where t1.people >= 100 and t2.people >= 100 and t3.people >= 100
and
(
	  (t1.id - t2.id = 1 and t1.id - t3.id = 2 and t2.id - t3.id =1)  -- t1, t2, t3
    or
    (t2.id - t1.id = 1 and t2.id - t3.id = 2 and t1.id - t3.id =1) -- t2, t1, t3
    or
    (t3.id - t2.id = 1 and t2.id - t1.id =1 and t3.id - t1.id = 2) -- t3, t2, t1
)
order by t1.id
;

作者：LeetCode
链接：https://leetcode-cn.com/problems/human-traffic-of-stadium/solution/ti-yu-guan-de-ren-liu-liang-by-leetcode/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
Lag/Lead大法好

with people as
(
    select id, visit_date, people,
    Lag(people,2) over(order by id) as pprvPeople,
    Lag(people,1) over(order by id) as prvPeople,
    Lead(people,1) over(order by id) as nextPeople,
    Lead(people,2) over(order by id) as nnextPeople
    from stadium
)
select id, visit_date, people from people
where 
(people >= 100 and prvPeople>=100 and pprvPeople>=100) ||
(people >= 100 and nextPeople>=100 and nnextPeople>=100) ||
(people >= 100 and nextPeople>=100 and prvPeople>=100) 
我也想问 很困惑 这样是不是只能找出连续的三行 而不是连续的3个id啊 如果原本id就不联系了的话就不对了吧😂

他一开始连续的行就是已经id排过序了，order by Id, 所以结果也是连续的id
```

```sql
select id, visit_date, people from 
(select *, count(*) over(partition by oder) as oder2 from 
(
	select *, id - rank() over(order by id asc) as oder from
	(select * from stadium where people >= 100 ) as tempable
) as temptable2) as temptable3 where oder2 >=3;
```

```sql
mysql 8.0版本可以使用rank()函数 和 with语法，8.0之前的版本可以通过用户变量语法和临时表进行模拟（以下sql用于解题就没有使用临时表，性能有所损耗）

select tt.id,tt.visit_date,tt.people
from (select ((@rrk:= @rrk+1)-id) as dif,id,visit_date,people
from stadium,(select @rrk:=0) init 
where people >= 100 
order by id asc) tt
where tt.dif in (
select t.dif
from 
(select ((@rk:= @rk+1)-id) as dif
from stadium,(select @rk:=0) as aaa
where people >= 100 
order by id asc) t
GROUP BY t.dif
having count(t.dif) >= 3)
order by tt.visit_date asc;
```

```sql
select
    base.id,
    base.visit_date,
    base.people
from (
    select
        *,
        # 连续counts条记录
        count(*) over(partition by diff) as 'counts'
    from (
        select
            *,
            # 连续的记录diff都一样
            id - row_number() over(order by id) as 'diff'
        from Stadium
        where people >= 100
    ) as base
) as base
where counts >= 3
```

```sql
利用多表联合查询，分为三种情况顺序

select distinct a.* from stadium a,stadium b,stadium c
where a.people>=100 and b.people>=100 and c.people>=100
and (
     (a.id = b.id-1 and b.id = c.id -1) or
     (a.id = b.id-1 and a.id = c.id +1) or
     (a.id = b.id+1 and b.id = c.id +1)
) order by a.id

@Derrick 大佬的思维很是独特,这个distinct不是太明白,想请教一下大佬.......

@LanwerJ distinct是查询中不含重复，因为如果连续4个或以上都满足条件，有些行会在查询结果中重复出现

@Derrick 这个思维真的是~~ 大佬！大佬！！！

@Derrick 首先膜拜下大佬的思路。我的理解是，这是将a.id在a b c中的位置分为三种情况，第一位 第二位和第三位，但是你说的如果有4个连续，会出现重复的问题，这个我还是不太明白呢，请指示，谢谢了。

@monkey 这个就是前面加distinct的原因，distinct是sql中去重操作。也就是有四个连续，2、3位每个会统计两次，但是select出来去重了

@LanwerJ 写一下笛卡尔积的结果差不多就懂了应该

@Derrick 你这要是联系4天的呢？5天的呢 是不是 and里面就更多了

@monkey 比如，id为，1，2，3，4的满足。我们讨论id 为2的这一天， id为2 满足是因为，1，2，3； 也可以是因为2，3，4。

每种情况都记录一次，那是不是这两种情况就记录了两次，重复了。
```

```sql
select id,visit_date,people from
(select id,visit_date,people,count(*)over(partition by k1) k2  #3.获得同一连续组内记录数
from 
(select id,visit_date,people,id-row_number()over(order by visit_date) k1 from stadium   #2.按日期排序 用id-row_number 的方式判断是否连续
where people>=100   #1.挑选出人流超过100的天数
) a    
) b
where k2>=3

@buling 大佬这个 id-row_number() 的这个操作我从来没有进过， 平常都是row_number() 找了好久没有找到相应的资料

@足球彬阿森纳 是row_number()呀 ，这里是用id减去row_number()得到一个值

@buling 这个row_number不应该是对id进行排序标号吗？id连续三个以上

@足球彬阿森纳 这个就是做减法，id减去row_number()

@buling 不考虑兼容，窗口函数真香啊

@宅宅不宅 你是对的，楼主错了

@宅宅不宅 row_number对id排序，跳过了people<100的进行排序的，所以 id - row_num 会出现差值，每次连续断了差值都不一样，反而连续的差值是一样的

@buling 这个想法太聪明了

@keepkeep 我想到里层的id-row_number()，但一直想不到外层的partition by k1，哈哈，看了大佬的明白了
```

```sql
比较好懂

# Write your MySQL query statement below
select * from stadium where people>=100 and
(
(id in (select id from stadium where people>=100 ) and id+1 in (select id from stadium where people>=100 ) and id+2 in (select id from stadium where people>=100 ))
or    
(id in (select id from stadium where people>=100 ) and id+1 in (select id from stadium where people>=100 ) and id-1 in (select id from stadium where people>=100 ))
or
(id in (select id from stadium where people>=100 ) and id-1 in (select id from stadium where people>=100 ) and id-2 in (select id from stadium where people>=100 ))
)
```

```sql
id	visit_date	people	row_number 	id-row_number 
1	2017/1/1	10		
2	2017/1/2	109	1	1
3	2017/1/3	150	2	1
4	2017/1/4	99		
5	2017/1/5	145	3	2
6	2017/1/6	1455	4	2
7	2017/1/7	199	5	2
8	2017/1/9	188	6	2
with temp as(
   select *, id - row_number() over (order by id asc) as ranking
    from Stadium
    where people >= 100
)

select id, visit_date, people
from temp
where ranking in (
    select ranking 
    from temp
    group by 1
    having count(1) >= 3
)
order by visit_date asc
@梦里花落知多少 nice 这是我见过的最巧妙的方法
@梦里花落知多少 牛
@梦里花落知多少 请问id - row_number这个排序是啥
```

```sql
lead+lag获取上下5行

select id,visit_date,people
from(
    select *, 
        lead(people,1) over(order by visit_date) as next1,
        lead(people,2) over(order by visit_date) as next2,
        lag(people,1) over(order by visit_date) as pre1,
        lag(people,2) over(order by visit_date) as pre2
    from Stadium
)t
WHERE people>=100 and (    pre1>=100 and pre2>=100 
                        or pre1>=100 and next1>=100
                        or next1>=100 and next2>=100)
@Mcq. 牛逼，这用法太强了
@Mcq. 终于有想法一致的了，先后都取2次就好了~

@Emily huang 换成id 排序会不会好一点

@Emily huang 审题：visit_date是主键，且ID随着visit_date唯一增长。

@Emily huang id不好判断
```

```sql
先找出人数大于等于100的 创建一列序号参考列 用原表id与序号列相减，连续id会得到相同的值，在聚合计算个数大于3的

select id,visit_date,people    
from 
(
select *,count(id) over(partition by id-rnk) as gt3_c
from (
    select id,visit_date,people,row_number()over(order by id) as rnk
    from Stadium 
    where people>=100)a ) b 
where gt3_c>=3
```

```sql
使用双层窗口函数，内层窗口函数求分组编号，外层对分组结果记录数求和

SELECT  id
        ,visit_date
        ,people
FROM    (
            SELECT  id
                    ,visit_date
                    ,people
                    ,COUNT(*) OVER(PARTITION BY rn) AS cnt
            FROM    (
                        SELECT  *
                                ,(id - row_number() OVER(ORDER BY id)) AS rn
                        FROM    stadium
                        WHERE   people >= 100
                    ) a
        ) d
WHERE   cnt >= 3
;
@whisperloli 一开始我想着用lag解决的，但是发现lag只能发现3条连续记录的，如果连续有5、6甚至100次就不好解决

@whisperloli 这个思路目前最好的，效率也比自连接高

@whisperloli 为什么要id-rownum

@whisperloli 厉害，赞

@十里八乡俊后生 为了看id是不是连续的，所以先用id-rownum 做差，如果做差结果都是相同的，那么久说明相邻的id是连续的，如果还不明白，你可以举个例子写写就知道了
```

```sql
以前用变量，现在发现窗口函数真想qwq

select distinct t2.*    
from(
    select *, 
    lead(people,1)over(order by visit_date  ) as p2,
    lead(people,2)over(order by visit_date ) as p3
    from Stadium 
)t, Stadium t2
where t.people >=100 and p2>=100 and p3>=100 and 
t2.id>=t.id and t2.id-2<=t.id

@fable 请问最后一行t2.id>=t.id and t2.id-2<=t.id怎么理解

@Buzzy t2.id>t1.id就是t2的id在t1后面。t2.id-2<=t1.id 就是t2 id是在ti id的两个包括两个以内。比如t1是1，那第一个条件就是23456....第二个条件就是从无穷小到0,1,2,3，所以t2就是2，3


```

```sql
with temp as(select *,
                    id-(row_number() over(order by id)) as base_id
             from Stadium
             where people >= 100)
select id,
       visit_date,
       people
from temp
where base_id in(
                select base_id
                from temp
                group by base_id
                having count(1) > 2
                )
总结：连续登陆N天问题

辅助列：
date_sub(dete,
         intervel (row_number() over(partition by uid order by date)) day
         ) as base_date
根据连续日期的特点: base_date 相同
select base_line
group by base_line 
having count(1) > = N
与原表内连接查询出需求字段
```

```sql
select distinct s.id, s.visit_date, s.people
from (
    select id, visit_date, people,
    case
    when people >= 100 then @con := @con + 1
    else @con := 0
    end
    as consec
    from stadium, (select @con := 0) as init
) as c, stadium as s
where s.id <= c.id and s.id + 3 > c.id and c.consec >= 3

@Raniac 不错的方法。多表联结方法的缺点是，如果题目中“连续n天”的条件中n比较大，写起来会很麻烦。变量的方法能够克服这一点，随着n的不同改变一些参数即可。

@Raniac 学到了，这就是人和人的区别吗

@Raniac

s.id <= c.id and s.id + 3 > c.id
请问这句话是什么意思啊

@萤火 遇到consec为3的记录 除了输出这条记录之后的 还要把这条记录之前的两天也输出
```

###  4.17. <a name='-1'></a>620. 有趣的电影


```sql
方法：使用 MOD() 函数
算法

我们可以使用 mod(id,2)=1 来确定奇数 id，然后添加 description != 'boring' 来解决问题。

MySQL

select *
from cinema
where mod(id, 2) = 1 and description != 'boring'
order by rating DESC
;

作者：LeetCode
链接：https://leetcode-cn.com/problems/not-boring-movies/solution/you-qu-de-dian-ying-by-leetcode/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。

建议尽量使用<> ,sql2000中是不支持 != 的
MOD(x,y) 返回除法操作的余数。我感觉用%就够了

```

```sql
MySQL:

select * from cinema where id&1 and description <> 'boring' order by rating desc;
```

```sql
执行用时 : 204 ms, 在Not Boring Movies的MySQL提交中击败了98.24% 的用户

SELECT
	id,
	movie,
	description,
	rating
FROM
	cinema
WHERE
	id & 1
AND description <> 'boring'
ORDER BY
	rating DESC

@Max. select * from cinema where not description = 'boring' and id % 2 = 1 order by rating desc 执行用时99ms,击败了99.57%的用户。 使用not的效率比<>要快5ms,select * 的查找效率同样比逐字段查找快.

逐字段查找方便加注释，这个算是个小优点。

想问一下这里id&1中的&应该怎么解释？

@萌孟 位运算

@Max. 位运算好想法，快很多

@墨尘原来如此

@萌孟 位运算 ，奇数和1做&操作得到1，偶数和1做&操作得到0。

明白了，二进制中，偶数最后一位是0，奇数是1，按位运算的话，0&1就是0，1&1就是1

@Max. 这个&运算很灵性

@Max. id & 1为什么不用写成 id&1<>0 啊？
```

```sql
sql竟然有id %2 =1,哈哈，果然语言都是相通的

# Write your MySQL query statement below
select * from cinema where description != 'boring' and id % 2 = 1 order by rating desc
```

```sql
select id, movie, description, rating
from cinema
where description <> 'boring' and id%2 <>0
order by rating desc;
```

```sql
余数用% 或者mod(id,2)=1

select id, movie, description, rating
from cinema
where id % 2 = 1 and description <> 'boring'
order by 4 desc
```

```sql
SELECT
    *
FROM
    cinema
WHERE description NOT LIKE "%boring%"
    AND id & 1
ORDER BY rating DESC;
```

```sql
select
    id,
    movie,
    description,
    rating
from cinema
where description <> "boring"
and mod(id,2)=1
order by rating desc;

```

```sql
执行用时 :100 ms, 在所有 mysql 提交中击败了94.80%的用户

select * from cinema where description!="boring" and id%2=1 order by rating desc;
```

```sql
select * from cinema
where description != 'boring'
and id % 2 != 0
order by rating desc
@user7795y 用id%2不等于0避免了 id=1？ 那mod(id，2)=1能否包含id=1呢？
```

###  4.18. <a name='-1'></a>626. 换座位

```sql
方法一：使用 CASE【通过】
算法

对于所有座位 id 是奇数的学生，修改其 id 为 id+1，如果最后一个座位 id 也是奇数，则最后一个座位 id 不修改。对于所有座位 id 是偶数的学生，修改其 id 为 id-1。

首先查询座位的数量。

MySQL

SELECT
    COUNT(*) AS counts
FROM
    seat
然后使用 CASE 条件和 MOD 函数修改每个学生的座位 id。

MySQL

MySQL

SELECT
    (CASE
        WHEN MOD(id, 2) != 0 AND counts != id THEN id + 1
        WHEN MOD(id, 2) != 0 AND counts = id THEN id
        ELSE id - 1
    END) AS id,
    student
FROM
    seat,
    (SELECT
        COUNT(*) AS counts
    FROM
        seat) AS seat_counts
ORDER BY id ASC;
方法二：使用位操作和 COALESCE()【通过】
算法

使用 (id+1)^1-1 计算交换后每个学生的座位 id。

MySQL

SELECT id, (id+1)^1-1, student FROM seat;

| id | (id+1)^1-1 | student |
|----|------------|---------|
| 1  | 2          | Abbot   |
| 2  | 1          | Doris   |
| 3  | 4          | Emerson |
| 4  | 3          | Green   |
| 5  | 6          | Jeames  |
然后连接原来的座位表和更新 id 后的座位表。

MySQL

SELECT
    *
FROM
    seat s1
        LEFT JOIN
    seat s2 ON (s1.id+1)^1-1 = s2.id
ORDER BY s1.id;

| id | student | id | student |
|----|---------|----|---------|
| 1  | Abbot   | 2  | Doris   |
| 2  | Doris   | 1  | Abbot   |
| 3  | Emerson | 4  | Green   |
| 4  | Green   | 3  | Emerson |
| 5  | Jeames  |    |         |
注：前两列来自表 s1，后两列来自表 s2。

最后输出 s1.id 和 s2.student。但是 id=5 的学生，s1.student 正确，s2.student 为 NULL。因此使用 COALESCE() 函数为最后一行记录生成正确的输出。

MySQL

MySQL

SELECT
    s1.id, COALESCE(s2.student, s1.student) AS student
FROM
    seat s1
        LEFT JOIN
    seat s2 ON ((s1.id + 1) ^ 1) - 1 = s2.id
ORDER BY s1.id;

作者：LeetCode
链接：https://leetcode-cn.com/problems/exchange-seats/solution/huan-zuo-wei-by-leetcode/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
我居然交换学生

select id, 
case when id=c.count and mod(id,2)=1 then (select student from seat where id =c.count) 
when mod(id,2)=1 then (select student from seat where id = a.id+1)
else (select student from seat where id = a.id-1) end as  student
from seat a,
(select count(*) as count from seat) c
```

```sql
窗口函数

select id, if(t.id%2=1,t.back,t.ahead) as 'student' from
(select id,student,LAG(student, 1,student) over() ahead, LEAD(student, 1,student) over() back
from seat) t
```

```sql
lag,lead昨天看别的题学的，真香
窗口函数，搜一下就知道了
select id, if(id % 2 = 1,lead(student,1,student) over (order by id), lag(student,1)over (order by id)) student
from seat
```

```sql
SELECT
    (
        CASE
            WHEN id % 2 = 0 THEN id - 1
            WHEN id % 2 = 1 AND id != (SELECT COUNT(*) FROM seat) THEN id + 1
            ELSE id
        END
    ) id,
    student
FROM seat
ORDER BY id;
```

```sql
反正你们很厉害，我是直接拿菜刀一点一点砍， 献丑

select id,student from seat where id = (select max(id) from seat) and id %2=1
union 
select a.id,b.student from seat a,seat b
where a.id%2=1 and a.id=b.id-1 
union
select a.id,b.student from seat a,seat b
where a.id%2=0 and a.id=b.id+1 
order by id
```

```sql
## if()函数

select if(mod(s.id,2)=0,s.id-1,if(
    s.id=tmp.cnt,s.id,s.id+1
)) id,
s.student
from seat s,(select count(*) cnt from seat) tmp
order by id; -- 根据别名排序
mysql 是select 完后才 order by的
## case when

select (
    case 
        when mod(s.id,2)=0 then s.id-1 -- 偶数
        when mod(s.id,2)=1 and cnt=s.id then s.id -- 奇数且为最后一位
        else s.id+1 -- 奇数 不为最后一位
    end
) id,
s.student
from seat s,(select count(*) cnt from seat) tmp
order by id; -- 根据别名排序 默认从小到大
```

```sql
执行用时 : 259 ms, 在Exchange Seats的MySQL提交中击败了100.00% 的用户

SELECT (CASE 
            WHEN MOD(id,2) = 1 AND id = (SELECT COUNT(*) FROM seat) THEN id
            WHEN MOD(id,2) = 1 THEN id+1
            ElSE id-1
        END) AS id, student
FROM seat
ORDER BY id;

@blacksheep 卧槽，大佬啊，这思路好。我刚开始想着是把字段student调换，你这个语句是直接调换id，然后再进行排序，666

@blacksheep 大佬写的sql通俗易懂，佩服。

@monkey 我一开始也是想换student，后来一看答案都是对id动手，我就知道我too young 了

@blacksheep as id这个真是巧妙，骚的很


```

```sql
为什么你们都写得那么麻烦？按(id-1)^1排序下不就行？

select rank() over(order by (id-1)^1) as id,student from seat

@comiee lc的MySQL版本不够新，sql sever倒是可以运行

@comiee (id-1)^1) 这个是什么意思？

@comiee 太厉害了。用与1 异或，将奇数变偶数，偶数变奇数。又用-1对最后一行为奇数行的情况进行处理。太巧妙了。

@哎呀 乖 不做夜猫子 是这样吗？ 这妙在利用异或只把偶数减2，奇数不变，从而调位。（单数-1后，最后一位是0，异或把最后一位变回1，等于不变；偶数-1后，最后一位是1，异或把最后一位变成0，等于再减去1）

@ycchanau 不能同意更多。

@拉格朗日后人 ^是异或运算符。

@comiee 你要知道，不是每个人都像你一样聪明的

@comiee 您好请问一下为什么我用 select (id-1)^1 as id,student from seat ->{"headers": ["id", "student"], "values": [[1, "Abbot"], [0, "Doris"], [3, "Emerson"], [2, "Green"], [5, "Jeames"]]} 如果是3的话，2^1 要如何运算呢

@comiee 请问能不能再具体解释下啊, (id-1)^1具体对于每个id应该是怎样的呢，不是很了解异或

@comiee 我想了半天的位运算，都得不到理想的结果，谢谢你的答案。
```

```sql
select 
if((mod(id,2)=1 and id = (select max(id) from seat)),id,if(mod(id,2)=1,id+1,id-1)) as id,student from seat order by id

```

```sql
这应该是最简洁的方法了吧？

select row_number() over(order by if(id % 2 = 0 , id - 1 , id + 1)) as id
       ,student
from seat
```

```sql
/* Write your T-SQL query statement below */

SELECT (
    CASE WHEN id%2 = 0 THEN id - 1
         WHEN id = (SELECT COUNT(*) FROM seat) THEN id
         WHEN id%2 = 1 THEN id + 1
    END) AS id, student
FROM seat
ORDER BY id
@ReallyHappy 请问为什么THEN那里不可以用id=id-1啊，我这么写发现结果是不对的

@两包烟 不能用面向过程的思维去想SQL, then后面直接输出结果, 写计算式就行了
```

```sql
使用窗口函数直接起飞

select 
row_number() over(order by if(id%2=0,id-1,id+1)) as `id`, student
from seat;

@码博士淘宝 你可以去看一下row_number()的用法，这里代码大致意思就是先用一个表达式对id做个处理，根据处理的结果进行排序，再依次添上新序号

@TYTaO 谢谢 这个写的很clean 但是当id=5的时候为odd 然后id5 就变成6了

@Kelvin 并不会,因为外层的row_number对数据重新排了一次序

@Kelvin row_number取得是行号，所以行号还是12345不会出现6的
```

```sql
评论区学到的异或！利用异或只把偶数减2，奇数不变，从而调位。（单数-1后，最后一位是0，异或把最后一位变回1，等于不变；偶数-1后，最后一位是1，异或把最后一位变成0，等于再减去1）

select rank() over(order by (id-1)^1) as id,student
from seat
由异或得到的思路：

select row_number() over() as id, student
from seat
order by if(id%2=0, id-2, id)
```

```sql
oracle高低版本的两种写法 高版本

select 
row_number() over(order by DECODE(mod(id,2),0,id-2,id)) as id, student
from seat;
低版本

select (case
    when mod(id,2) = 1 and id = (select max(id) from seat) then id
    when mod(id,2) = 1 then id + 1 else id - 1 end) as id,student from 
    seat order by id
```

```sql
91.4%

select id, if(mod(id,2)=1, lead(student,1,student)over(), lag(student,1)over()) student
from seat
```

```sql
select row_number() over(order by if(mod(id,2)=1, id+1, id-1)) as "id", student from seat
```

```sql
搞了好久，我果然是菜鸟

select if(id%2 =0,id-1,if(id=(select max(id) from seat),id,id+1))as id,student from seat
order by id asc;
@会飞的鱼 你的这段if嵌套我都第一次看，比你还菜的鸟很多

@会飞的鱼 你太谦虚了，写出这个代码就很棒啦。 看了你的代码更简洁，棒棒的啊
```

###  4.19. <a name='-1'></a>627. 变更性别

```sql
方法：使用 UPDATE 和 CASE...WHEN
算法

要想动态地将值设置成列，我们可以在使用 CASE...WHEN... 流程控制语句的同时使用 UPDATE 语句。

MySQL

UPDATE salary
SET
    sex = CASE sex
        WHEN 'm' THEN 'f'
        ELSE 'm'
    END;

作者：LeetCode
链接：https://leetcode-cn.com/problems/swap-salary/solution/jiao-huan-gong-zi-by-leetcode/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
使用 if 判断，当 sex 为 m 的时候更改为 f，否则就更改为 m（f 改为 m），使用 if(sex="m","f","m") 就可以直接搞定了，实现 SQL 如下。

# Write your MySQL query statement below

update salary set sex=if(sex="m","f","m");
```

```sql
update salary
set
     sex = case sex when "m" then "f" else "m" end;
```

```sql
# 1.
update salary
set sex = (
    case sex when 'm' then 'f' else 'm' end
);

# 2.
update salary set sex = if(sex='m','f','m');

# 3.
update salary set sex = char(ascii('m') + ascii('f') - ascii(sex));
@扬子居 第三种方法妙啊
```

```sql
思路 颠倒男女的性别 if 男 则是女 但是会替换 所以 辅助列 sexadd

ALTER TABLE salary 
ADD COLUMN sexadd TEXT
UPDATE salary 
SET sexadd = sex
SET sex = "f"
WHERE sex = "m"
SET sex = "m"
WHERE sexadd = "f";

原来 ql 里面也支持if语句

UPDATE salary 

SET sex = if(sex = "m","f","m");
除此之外CASE WHEN也发挥同样的作用

UPDATE salary 

SET 
   sex = CASE sex 
        WHEN "m" THEN "f" 
        ELSE "m" 
    END;
```

```sql
case用法： case a when cond1 then exp1 else cond2 then exp2 else exp3

当a满足条件cond1时， 返回exp1 当a满足条件cond2时， 返回exp2 否则 返回exp3

@Carl Marx 兄弟，写错了，when .. then ..when .. then.. else

@Carl Marx 朋友,要在case语句后面加个end哦
```

```sql
case when 条件1 then 取值1 else 不满足条件的取值 end 执行用时： 330 ms , 在所有 MySQL 提交中击败了 5.06% 的用户 内存消耗： 0 B , 在所有 MySQL 提交中击败了 100.00% 的用户

update Salary set sex = case when sex='f' then 'm' else 'f' end;
@丶逗比 update salary set sex = if (sex = "m", "f", "m"); ===》评论区yyds
```

```sql
if函数真的可以，本来就是试试的

update Salary
set sex=if(sex='m','f','m')
```

```sql
简单好理解

update salary set sex = if (sex = "m", "f", "m");
```

```sql
两个相等的数异或的结果为 0，而 0 与任何一个数异或的结果为这个数

UPDATE salary
SET sex = CHAR ( ASCII(sex) ^ ASCII( 'm' ) ^ ASCII( 'f' ) );
```

```sql
oracle 两种方式，用函数和不用函数

用函数

update salary set sex = decode(sex,'m','f','m')
不用函数

update salary set sex = (case when sex = 'm' then 'f' else 'm' end)
@         decode 只支持oracle
```

```sql
击败100%，狠起来把自己都要击败

UPDATE
salary 
SET sex = IF(sex = 'm','f','m')
```

```sql

# 考察 if 或 case 的用法

# 1, 用if
update salary
set sex = if(sex='m', 'f', 'm')

# 2, 用case
update salary
set sex = (case sex
                when 'm' then 'f'
                else 'm'
            end
            )
@as IF(条件，条件true，条件false)
```

```sql
update salary set sex = char ( ASCII(sex) ^ ASCII('m') ^ ASCII('f'))
@zhaohan 我也想用异或 ，但没有成功，原来要这么用
```

###  4.20. <a name='-1'></a>1179. 重新格式化部门表

```sql
SQL

select id,
	sum(case month when 'Jan' then revenue end) as 'Jan_Revenue',
	sum(case month when 'Feb' then revenue end) as 'Feb_Revenue',
	sum(case month when 'Mar' then revenue end) as 'Mar_Revenue',
	sum(case month when 'Apr' then revenue end) as 'Apr_Revenue',
	sum(case month when 'May' then revenue end) as 'May_Revenue',
	sum(case month when 'Jun' then revenue end) as 'Jun_Revenue',
	sum(case month when 'Jul' then revenue end) as 'Jul_Revenue',
	sum(case month when 'Aug' then revenue end) as 'Aug_Revenue',
	sum(case month when 'Sep' then revenue end) as 'Sep_Revenue',
	sum(case month when 'Oct' then revenue end) as 'Oct_Revenue',
	sum(case month when 'Nov' then revenue end) as 'Nov_Revenue',
	sum(case month when 'Dec' then revenue end) as 'Dec_Revenue'
from department group by id
解析
department 表中存储这所有人所有月的收入，这里的需求是将 department 的 month 列拆成具体的月份。具体实现：

将 department 按照 id 进行分组
使用 case month when 'Jan' then revenue end 计算出一月份的收入
也可以使用 if(month = 'Jan', revenue, null)
每个以此类推，直到 12 个月都计算完
因为使用 group by 需要使用聚合函数，这里的聚合函数可以用 max 、 min 、 sum 等

作者：uccs
链接：https://leetcode-cn.com/problems/reformat-department-table/solution/zhong-xin-ge-shi-hua-bu-men-biao-by-uccs-olmz/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```sql
select 
     id
    , sum(case `month` when 'Jan' then revenue else null end) as Jan_Revenue
    , sum(case `month` when 'Feb' then revenue else null end) as Feb_Revenue
    , sum(case `month` when 'Mar' then revenue else null end) as Mar_Revenue
    , sum(case `month` when 'Apr' then revenue else null end) as Apr_Revenue
    , sum(case `month` when 'May' then revenue else null end) as May_Revenue
    , sum(case `month` when 'Jun' then revenue else null end) as Jun_Revenue
    , sum(case `month` when 'Jul' then revenue else null end) as Jul_Revenue
    , sum(case `month` when 'Aug' then revenue else null end) as Aug_Revenue
    , sum(case `month` when 'Sep' then revenue else null end) as Sep_Revenue
    , sum(case `month` when 'Oct' then revenue else null end) as Oct_Revenue
    , sum(case `month` when 'Nov' then revenue else null end) as Nov_Revenue
    , sum(case `month` when 'Dec' then revenue else null end) as Dec_Revenue
from Department group by id
@msidolphin 运行成功，不过有些不理解

@msidolphin 这里的聚合函数SUM可以随意替换成其他聚合函数是吗？ 只是为了单纯的使group by成立对吗？ 小白求解

@machi case when 就是在原表的基础上加上12列， 比如id = 1有两个数据， 然后再group by ID 再聚合sum 把两行变成一行

@machi 并不是，而且楼主的理解也有偏差。 具体原理可以参考我的题解：https://leetcode-cn.com/problems/reformat-department-table/solution/group-byben-zhi-lun-by-loverxp-7mgy/

@msidolphin 你这个查询比起上面那个连的好多了

@msidolphin 跟你一模一样

@machi 我感觉max也可以

@msidolphin 不理解这里的month为啥要加‘’

@SQ_闭嘴做事 因为month是关键字
```

```sql
select distinct id,
    sum(IF(month="Jan",revenue,null)) as Jan_Revenue,
    sum(IF(month="Feb",revenue,null)) as Feb_Revenue,
    sum(IF(month="Mar",revenue,null)) as Mar_Revenue,
    sum(IF(month="Apr",revenue,null)) as Apr_Revenue,
    sum(IF(month="May",revenue,null)) as May_Revenue,
    sum(IF(month="Jun",revenue,null)) as Jun_Revenue,
    sum(IF(month="Jul",revenue,null)) as Jul_Revenue,
    sum(IF(month="Aug",revenue,null)) as Aug_Revenue,
    sum(IF(month="Sep",revenue,null)) as Sep_Revenue,
    sum(IF(month="Oct",revenue,null)) as Oct_Revenue,
    sum(IF(month="Nov",revenue,null)) as Nov_Revenue,
    sum(IF(month="Dec",revenue,null)) as Dec_Revenue
    from Department group by id order by id;
@后场投篮 您好，为什么没有使用group by，也需要用聚合函数呀？去掉就不通过

@么么惠 不是很明白，你的意思是说不使用group by 吗

@后场投篮 不好意思，看走眼了，仔细看了您的答案末尾是有group by的...

@么么惠 哈哈好的

@后场投篮 为什么不能将sum加到if条件里revenue的前面？

@后场投篮 我直接用你这个无法执行，将if换成iif就是行了，百度也有if用法，看起来两个是一个东西 为什么if在leetcode会报错呢

@后场投篮 我和你一样写法， 但是错误， 我不懂为什么
```

```sql
select
    id,
    sum(case when month = "Jan" then revenue else null end) as Jan_Revenue,
    sum(case when month = "Feb" then revenue else null end) as Feb_Revenue,
    sum(case when month = "Mar" then revenue else null end) as Mar_Revenue,
    sum(case when month = "Apr" then revenue else null end) as Apr_Revenue,
    sum(case when month = "May" then revenue else null end) as May_Revenue,
    sum(case when month = "Jun" then revenue else null end) as Jun_Revenue,
    sum(case when month = "Jul" then revenue else null end) as Jul_Revenue,
    sum(case when month = "Aug" then revenue else null end) as Aug_Revenue,
    sum(case when month = "Sep" then revenue else null end) as Sep_Revenue,
    sum(case when month = "Oct" then revenue else null end) as Oct_Revenue,
    sum(case when month = "Nov" then revenue else null end) as Nov_Revenue,
    sum(case when month = "Dec" then revenue else null end) as Dec_Revenue
from department
group by id;
```

```sql
#注意，month字段需要用`` ，不能用' ' 
#``是 MySQL 的转义符，避免和 mysql 的本身的关键字冲突

select  id,
sum(case `month` when 'Jan' then revenue else null  end) as 'Jan_Revenue',
sum(case `month` when 'Feb' then revenue else null  end) as 'Feb_Revenue',
sum(case `month` when 'Mar' then revenue else null  end) as 'Mar_Revenue',
sum(case `month` when 'Apr' then revenue else null  end) as 'Apr_Revenue',
sum(case `month` when 'May' then revenue else null  end) as 'May_Revenue',
sum(case `month` when 'Jun' then revenue else null  end) as 'Jun_Revenue',
sum(case `month` when 'Jul' then revenue else null  end) as 'Jul_Revenue',
sum(case `month` when 'Aug' then revenue else null  end) as 'Aug_Revenue',
sum(case `month` when 'Sep' then revenue else null  end) as 'Sep_Revenue',
sum(case `month` when 'Oct' then revenue else null  end) as 'Oct_Revenue',
sum(case `month` when 'Nov' then revenue else null  end) as 'Nov_Revenue',
sum(case `month` when 'Dec' then revenue else null  end) as 'Dec_Revenue'
 from Department 
 group by id
```

###  4.21. <a name='1'></a>总结1

```sql
  本篇文章介绍平时自己使用次数非常多的SQL查询语句，总结起来方便以后复习查看
1. DDL(Data Definition Language)数据定义语言
（1）操作数据库
-- 创建库

create database db1;

-- 创建库是否存在，不存在则创建

create database if not exists db1;

-- 查看所有数据库

show databases;

-- 查看某个数据库的定义信息

show create database db1;

-- 修改数据库字符信息

alter database db1 character set utf8;

-- 删除数据库

drop database db1;

（2）操作表
--创建表

create table student(

    id int,

    name varchar(32),

    age int ,

    score double(4,1),

    birthday date,

    insert_time timestamp

);

 

-- 查看表结构

desc 表名;

-- 查看创建表的SQL语句

show create table 表名;

-- 修改表名

alter table 表名 rename to 新的表名;

-- 添加一列

alter table 表名 add 列名 数据类型;

-- 删除列

alter table 表名 drop 列名;

-- 删除表

drop table 表名;

drop table  if exists 表名 ;

2. DML(Data Manipulation Language)数据操作语言
（1）插入insert into
-- 写全所有列名

insert into 表名(列名1,列名2,...列名n) values(值1,值2,...值n);

-- 不写列名（所有列全部添加）

insert into 表名 values(值1,值2,...值n);

-- 插入部分数据

insert into 表名(列名1,列名2) values(值1,值2);

（2）删除delete
-- 删除表中数据

delete from 表名 where 列名  = 值;

-- 删除表中所有数据

delete from 表名;

-- 删除表中所有数据（高效 先删除表，然后再创建一张一样的表。）

truncate table 表名;

 

（3）修改update
-- 不带条件的修改(会修改所有行)

update 表名 set 列名 = 值;

-- 带条件的修改

updata 表名 set 列名 = 值 where 列名=值;

（4）选择select
select * from table1 where 范围

1.  DQL(Data Query Language)数据查询语言
（1）基础关键字
① BETWEEN...AND （在什么之间）和  IN( 集合)
-- 查询年龄大于等于20 小于等于30

SELECT * FROM student WHERE age >= 20 &&  age <=30;

SELECT * FROM student WHERE age >= 20 AND  age <=30;

SELECT * FROM student WHERE age BETWEEN 20 AND 30;

-- 查询年龄22岁，18岁，25岁的信息

SELECT * FROM student WHERE age = 22 OR age = 18 OR age = 25

SELECT * FROM student WHERE age IN (22,18,25);

 

② is null(不为null值) 与 like（模糊查询）、distinct（去除重复值）
-- 查询英语成绩不为null

SELECT * FROM student WHERE english  IS NOT NULL;



 _:单个任意字符

 %：多个任意字符

-- 查询姓马的有哪些？like

SELECT * FROM student WHERE NAME LIKE '马%';

-- 查询姓名第二个字是化的人

SELECT * FROM student WHERE NAME LIKE "_化%";

-- 查询姓名是3个字的人

SELECT * FROM student WHERE NAME LIKE '___';

-- 查询姓名中包含德的人

SELECT * FROM student WHERE NAME LIKE '%德%';

 

-- 关键词 DISTINCT 用于返回唯一不同的值。

-- 语法：SELECT DISTINCT 列名称 FROM 表名称

SELECT DISTINCT NAME FROM  student ;

 

（2）排序查询 order by
-- 例子

SELECT * FROM person ORDER BY math; --默认升序

SELECT * FROM person ORDER BY math desc; --降序

（3）聚合函数
将一列数据作为一个整体，进行纵向的计算。

--总数：

  select count as totalcount from table1

 

--求和：

select sum(field1) as sumvalue from table1

 

--平均：      

 select avg(field1) as avgvalue from table1

 

 --最大：       

select max(field1) as maxvalue from table1

 

--最小：

select min(field1) as minvalue from table1        

（4）分组查询 group by
-- 按照性别分组。分别查询男、女同学的平均分

SELECT sex , AVG(math) FROM student GROUP BY sex;

 

-- 按照性别分组。分别查询男、女同学的平均分,人数

 SELECT sex , AVG(math),COUNT(id) FROM student GROUP BY sex;

 

--  按照性别分组。分别查询男、女同学的平均分,人数 要求：分数低于70分的人，不参与分组

SELECT sex , AVG(math),COUNT(id) FROM student WHERE math > 70 GROUP BY sex;

 

 --  按照性别分组。分别查询男、女同学的平均分,人数 要求：分数低于70分的人，不参与分组,分组之后。人数要大于2个人

SELECT sex , AVG(math),COUNT(id) FROM student WHERE math > 70 GROUP BY sex HAVING COUNT(id) > 2;

SELECT sex , AVG(math),COUNT(id) 人数 FROM student WHERE math > 70 GROUP BY sex HAVING 人数 > 2;

（5）分页查询limit
语法：limit 开始的索引,每页查询的条数; 

公式：开始的索引 = （当前的页码 - 1） * 每页显示的条数

-- 每页显示3条记录

 

SELECT * FROM student LIMIT 0,3; -- 第1页

 

SELECT * FROM student LIMIT 3,3; -- 第2页

 

SELECT * FROM student LIMIT 6,3; -- 第3页

 

（6）外连接查询
① 左外连接 -- 查询的是左表所有数据以及其交集部分
-- 语法：select 字段列表 from 表1 left [outer] join 表2 on 条件；

-- 例子：

-- 查询所有员工信息，如果员工有部门，则查询部门名称，没有部门，则不显示部门名称

SELECT  t1.*,t2.`name` FROM emp t1 LEFT JOIN dept t2 ON t1.`dept_id` = t2.`id`;

② 右外连接 -- 查询的是右表所有数据以及其交集部分
-- 语法：

select 字段列表 from 表1 right [outer] join 表2 on 条件；

-- 例子：

SELECT  * FROM dept t2 RIGHT JOIN emp t1 ON t1.`dept_id` = t2.`id`;

（7）子查询：查询中嵌套查询
-- 查询工资最高的员工信息

-- 1 查询最高的工资是多少 9000

SELECT MAX(salary) FROM emp;

 

-- 2 查询员工信息，并且工资等于9000的

SELECT * FROM emp WHERE emp.`salary` = 9000;

 

 -- 一条sql就完成这个操作。这就是子查询

SELECT * FROM emp WHERE emp.`salary` = (SELECT MAX(salary) FROM emp);


```

###  4.22. <a name='2'></a>总结2

```sql
MYSQL:

select 字段1,字段2,  if(条件,条件满足返回值，条件不满足返回值)  as  字段3  from 表  where  ……group  by ……

字段3的AS，可省略



若涉及子查询，需要自定义名

select   字段1,字段2,字段3 from

       (select   字段1,字段2, if(条件,条件满足返回值，条件不满足返回值)  as  字段3  

from 表            
where  ……
group  by …… )  as  b
group by  ……

子查询的as不可省略





oracle:

  select  字段1,字段2，decode(条件,条件满足返回值，条件不满足返回值) 字段3  from 表 where ……group by ……

字段3的AS，可省略



子查询可不用自定义名，子查询可以多表直接交叉，示例

select sl.loc 位置, sku.prod_code 货号, sku.descr 说明, sl.qty 现有量,

      sl.qtyallocated 分配量,ceil(sysdate-sl.editdate+1/3) 未动销天数,sku.category1 品类

from skuxloc sl

join sku on sku.storerkey = sl.storerkey and sl.sku = sku.sku

where qty > 0 and sl.locationtype='PICK' and  sku.category1 in ('皮具','童装') and 

      exists (select 1 from

                      ( select storerkey,sku from skuxloc t where qty > 0 and  t.locationtype='PICK' group by storerkey,sku having count(1) > 1 ) a

                        where a.sku = sku.sku and a.storerkey = sku.storerkey and sl.qtyallocated<>sl.qty)

order by sku.sku









VBA_SQL:



select   字段1,字段2,iif(条件,条件满足返回值，条件不满足返回值)  as 字段3   from [表$]  group by ……

字段3的AS，不可省略



子查询可不用自定义名，但每次次只能套一个子查询，交叉表一次仅一个，示例：



      asql = "select a.分公司,a.物料编号,a.单位,b.体积*sum(a.开单量) as 体积,sum(a.开单量) as 数量 from [SAP元数据$] as a    " _

         & "left join (select DISTINCT 货号,单位,max(体积) as 体积 from [货号体积$] WHERE 体积>=1  GROUP BY 货号,单位 ) as b on b.货号=a.物料编号 and b.单位=a.单位    " _

         & "where  a.开单量>=1  " _

         & "group by a.分公司,a.物料编号,a.单位,b.体积  "

         

      asql = "select 分公司,sum(数量) as 总数量,round(sum(体积)/1000000,2) as 总体积M3 from (" & asql & ")   where 数量>=1 group by 分公司  "

        bsql = "select a.分公司,left(right(a.物料描述,2),1) & '鞋' as 类别,b.参照季节 as 季节,a.单位 as 型号,sum(a.开单量) as 数量,a.开单日期 from [SAP元数据$]   as a  " _

                    & "left join  [季节$] as b on b.季节代码=a.季节       " _

                    & "group by a.分公司,left(right(a.物料描述,2),1),b.参照季节,a.单位,a.开单日期  " _

                    & "order by  a.分公司,left(right(a.物料描述,2),1),b.参照季节,a.单位         "

        

        csql = "select aa.分公司,count(aa.分公司) as 订单数量,bb.总数量,bb.总体积M3 from (" & bsql & ")  as aa    " _

            & "left join (" & asql & ") as bb on bb.分公司=aa.分公司 " _

            & " group by aa.分公司,bb.总数量,bb.总体积M3 "

        

        

        bsql = "select a.分公司,b.分公司代码,b.品牌,b.承运商,b.承运商代码,iif(b.计划单时间='原数据里的开单日期','原数据里的开单日期',format(b.计划单时间,'yyyy.mm.dd')) as 计划单时间, " _

                & "      b.提货点,b.提货时间,a.类别,a.季节,a.型号,a.数量,format(a.开单日期,'yyyy.mm.dd') as 开单日期 from (" & bsql & " ) as a  " _

               & "left join [承运对照$] as b on b.备注=a.分公司  "

        ssql = "select a.分公司,a.分公司代码,a.品牌,a.承运商,a.承运商代码,iif(a.计划单时间='原数据里的开单日期',a.开单日期,a.计划单时间) as  计划单时间,   " _

               & "     a.提货点,format(a.提货时间,'hh:mm') as 提货时间,b.订单数量,b.总数量,b.总体积M3,a.类别,a.季节,a.型号,a.数量 from (" & bsql & " ) as a  " _

               & "left join (" & csql & " ) as b on b.分公司=a.分公司 "



注，在VBA开发工具中，跨行需要拼接符&进行拼接，而MYSQL与ORACLE语句直接回车跨行即可；

但如若MYSQL与ORACLE在VBA编辑开发工具中，引用，则需要用同样的方法进行&字符拼接；



以上为，三种SQL语句应用区别的简述，更多的需要实践中，不断去应用才能发现各自的区别，以及各自互相切换着应用时，如果左右手互换着来。
```

```sql
7) mysql的group by 语句可以select 没有被分组的字段，如 
select id,name,age from A group by age 这样 
但是在orcale和sqlserver中是会报错的。这个取出的id,name所在的行是每个分组中的第一行数据。 

8)orcale用decode()来转换数据，mysql,sqlserver用case when: 
case t.DETECT_RESULT when '2402' then t.SAMPLEID end (必须有end) 

9)mysql: 两个select 出的数据相减： 
(COUNT(distinct(t.SAMPLEID))- 
CONVERT((COUNT(distinct(case t.DETECT_RESULT when '2402' then t.SAMPLEID end))), SIGNED)) AS NEGATIVE 
FROM `view_sims_for_report` t 
10）convert,cast用法 
mysql将varchar转为int 
convert(字段名, SIGNED) 
字符集转换 : CONVERT(xxx USING gb2312) 
类型转换和SQL Server一样,就是类型参数有点不同 : CAST(xxx AS 类型) , CONVERT(xxx,类型) 
可用的类型　 
二进制,同带binary前缀的效果 : BINARY 
字符型,可带参数 : CHAR() 
日期 : DATE 
时间: TIME 
日期时间型 : DATETIME 
浮点数 : DECIMAL 
整数 : SIGNED 
无符号整数 : UNSIGNED 
```

```sql
数据库，顾名思义就是存储数据的仓库。目前常用的数据库如Oracle、Sybase等各有其千秋，但是如果挑出一款最为流行的数据库软件，非MySql莫属。SQL，Structured Query Language结构化查询语言，是MySql的使用语言，也是我们在MySql数据库中对数据进行数据操作、查询、修改等操作的工具。

创建数据库
一列数据是一个字段，多个字段构成数据表，而数据库就是大量带有相关数据的表的集合。所以，在使用MySql的时候，我们首先要做的就是创建数据库。

create database test；

在此段语句中，create database是关键词，test是创建的数据库的名称。对于数据库的操作，常用的还有以下语句：

查看创建好的数据库test：show create database test；

查看所有数据库：show databases；

使用数据库test：use test

删除数据库test：drop database test；

注意，在每一次新打开数据库的时候，必须先运行use test语句，才能对数据库test进行后续操作，否则就会报错。

创建数据表
创建好数据库以后，我们需要把数据表导入数据库。下面语句是一个表的导入的实例：

use test；

create table student（

ID char（3） primary key，

Name varchar（20）not null，

Credit int not null）；

上面是导入一个学生学分的表格。一共有三个字段：学生ID、名字以及学分。在穿件表格的时候，我们需要对每一个字段作两个限定。第一个是字段类型限定，字段类型有字符串（即文本类型）、数字、日期等，我们需要对此做说明。在此例中，char（3）代表固定长度为3的字符串型数据，varchar（20）代表可变长度的最大长度为20的字符串型数据，int代表整数型数据。第二是约束条件限定，我们对数据的约束有：非空约束（not null）、唯一约束（unique）、主键约束（primary key）等。其中主键约束最为重要，因为不同数据表进行联系的时候都是通过主键来进行，主键必须同时满足非空和唯一。

导入数据
创建好表格的框架以后，我们需要将数据导入进去。按照数据来源区分一般有两种：手工输入或者导入本地文件，手工输入费时耗力，在这里只说明本地文件的导入。

load data local infil ‘文件路径.txt’

into table student

fields terminated by ‘\t’

ignore 1 lines；

这一段语句里面我们需要注意的是后两行，txt文件格式对应‘\t’，excel的文件对应‘，’。另外当本地文件第一行不是每一列数据的字段名而是数据的时候，第一行数据不能忽视，所以代码的最后一行可以不写。

修改语句
alter table student rename st;将数据表student改名为st

alter table student modify name varchar(30);将字段name的数据类型改为varchar（30）

alter table student change name nm varchar(30);字段name改名为nm

alter table student add sex varchar(20) not null; 增加字段sex数据类型varchar（20）且非空
```

```sql
1， 对查询进行优化，应尽量避免全表扫描，首先应考虑在 where 及 order by 涉及的列上建立索引。
2，应尽量避免在 where 子句中对字段进行 null 值判断，创建表时NULL是默认值，但大多数时候应该使用NOT NULL，或者使用一个特殊的值，如0，-1作为默 认值。
3，应尽量避免在 where 子句中使用!=或<>操作符， MySQL只有对以下操作符才使用索引：<，<=，=，>，>=，BETWEEN，IN，以及某些时候的LIKE。
4，应尽量避免在 where 子句中使用 or 来连接条件， 否则将导致引擎放弃使用索引而进行全表扫描， 可以 使用UNION合并查询：select id from t where num=10 union all select id from t where num=20
5，in 和 not in 也要慎用，否则会导致全表扫描，对于连续的数值，能用 between 就不要用 in 了：Select id from t where num between 1 and 3
6，下面的查询也将导致全表扫描：select id from t where name like ‘%abc%’ 或者select id from t where name like ‘%abc’若要提高效率，可以考虑全文检索。而select id from t where name like ‘abc%’ 才用到索引
7， 如果在 where 子句中使用参数，也会导致全表扫描。
8，应尽量避免在 where 子句中对字段进行表达式操作，应尽量避免在where子句中对字段进行函数操作
9,很多时候用 exists 代替 in 是一个好的选择：select num from a where num in(select num from b).用下面的语句替换：select num from a where exists(select 1 from b where num=a.num)
10,索引固然可以提高相应的 select 的效率，但同时也降低了 insert 及 update 的效率，因为 insert 或 update 时有可能会重建索引，所以怎样建索引需要慎重考虑，视具体情况而定。一个表的索引数最好不要超过6个，若太多则应考虑一些不常使用到的列上建的索引是否有必要。
11,应尽可能的避免更新 clustered 索引数据列， 因为 clustered 索引数据列的顺序就是表记录的物理存储顺序，一旦该列值改变将导致整个表记录的顺序的调整，会耗费相当大的资源。若应用系统需要频繁更新 clustered 索引数据列，那么需要考虑是否应将该索引建为 clustered 索引。
12，尽量使用数字型字段，若只含数值信息的字段尽量不要设计为字符型，这会降低查询和连接的性能，并会增加存储开销。
13，尽可能的使用 varchar/nvarchar 代替 char/nchar ， 因为首先变长字段存储空间小，可以节省存储空间，其次对于查询来说，在一个相对较小的字段内搜索效率显然要高些。
14，最好不要使用”“返回所有：select from t ，用具体的字段列表代替“*”，不要返回用不到的任何字段。
15，尽量避免向客户端返回大数据量，若数据量过大，应该考虑相应需求是否合理。
16，使用表的别名(Alias)：当在SQL语句中连接多个表时,请使用表的别名并把别名前缀于每个Column上.这样一来,就可以减少解析的时间并减少那些由Column歧义引起的语法错误。
17，使用“临时表”暂存中间结果 
简化SQL语句的重要方法就是采用临时表暂存中间结果，但是，临时表的好处远远不止这些，将临时结果暂存在临时表，后面的查询就在tempdb中了，这可以避免程序中多次扫描主表，也大大减少了程序执行中“共享锁”阻塞“更新锁”，减少了阻塞，提高了并发性能。
18，一些SQL查询语句应加上nolock，读、写是会相互阻塞的，为了提高并发性能，对于一些查询，可以加上nolock，这样读的时候可以允许写，但缺点是可能读到未提交的脏数据。使用 nolock有3条原则。查询的结果用于“插、删、改”的不能加nolock ！查询的表属于频繁发生页分裂的，慎用nolock ！使用临时表一样可以保存“数据前影”，起到类似Oracle的undo表空间的功能，能采用临时表提高并发性能的，不要用nolock 。
19，常见的简化规则如下：不要有超过5个以上的表连接（JOIN），考虑使用临时表或表变量存放中间结果。少用子查询，视图嵌套不要过深,一般视图嵌套不要超过2个为宜。
20，将需要查询的结果预先计算好放在表中，查询的时候再Select。这在SQL7.0以前是最重要的手段。例如医院的住院费计算。
21，用OR的字句可以分解成多个查询，并且通过UNION 连接多个查询。他们的速度只同是否使用索引有关,如果查询需要用到联合索引，用UNION all执行的效率更高.多个OR的字句没有用到索引，改写成UNION的形式再试图与索引匹配。一个关键的问题是否用到索引。
22，在IN后面值的列表中，将出现最频繁的值放在最前面，出现得最少的放在最后面，减少判断的次数。
23，尽量将数据的处理工作放在服务器上，减少网络的开销，如使用存储过程。存储过程是编译好、优化过、并且被组织到一个执行规划里、且存储在数据库中的SQL语句，是控制流语言的集合，速度当然快。反复执行的动态SQL,可以使用临时存储过程，该过程（临时表）被放在Tempdb中。
24，当服务器的内存够多时，配制线程数量 = 最大连接数+5，这样能发挥最大的效率；否则使用 配制线程数量<最大连接数启用SQL SERVER的线程池来解决,如果还是数量 = 最大连接数+5，严重的损害服务器的性能。
25，查询的关联同写的顺序 
select a.personMemberID, * from chineseresume a,personmember b where personMemberID = b.referenceid and a.personMemberID = 'JCNPRH39681' (A = B ,B = '号码') 
select a.personMemberID, * from chineseresume a,personmember b where a.personMemberID = b.referenceid and a.personMemberID = 'JCNPRH39681' and b.referenceid = 'JCNPRH39681' (A = B ,B = '号码'， A = '号码') 
select a.personMemberID, * from chineseresume a,personmember b where b.referenceid = 'JCNPRH39681' and a.personMemberID = 'JCNPRH39681' (B = '号码'， A = '号码')
26，尽量使用exists代替select count(1)来判断是否存在记录，count函数只有在统计表中所有行数时使用，而且count(1)比count(*)更有效率。
27，尽量使用“>=”，不要使用“>”。
28，索引的使用规范：索引的创建要与应用结合考虑，建议大的OLTP表不要超过6个索引；尽可能的使用索引字段作为查询条件，尤其是聚簇索引，必要时可以通过index index_name来强制指定索引；避免对大表查询时进行table scan，必要时考虑新建索引；在使用索引字段作为条件时，如果该索引是联合索引，那么必须使用到该索引中的第一个字段作为条件时才能保证系统使用该索引，否则该索引将不会被使用；要注意索引的维护，周期性重建索引，重新编译存储过程。　　
29，下列SQL条件语句中的列都建有恰当的索引，但执行速度却非常慢： 
SELECT * FROM record WHERE substrINg(card_no,1,4)='5378' (13秒) 
SELECT * FROM record WHERE amount/30< 1000 （11秒） 
SELECT * FROM record WHERE convert(char(10),date,112)='19991201' （10秒）
分析： 
WHERE子句中对列的任何操作结果都是在SQL运行时逐列计算得到的，因此它不得不进行表搜索，而没有使用该列上面的索引；如果这些结果在查询编译时就能得到，那么就可以被SQL优化器优化，使用索引，避免表搜索，因此将SQL重写成下面这样： 
SELECT * FROM record WHERE card_no like '5378%' （< 1秒） 
SELECT * FROM record WHERE amount< 1000*30 （< 1秒） 
SELECT * FROM record WHERE date= '1999/12/01' （< 1秒）
30，当有一批处理的插入或更新时，用批量插入或批量更新，绝不会一条条记录的去更新!
31，在所有的存储过程中，能够用SQL语句的，我绝不会用循环去实现! 
(例如：列出上个月的每一天，我会用connect by去递归查询一下，绝不会去用循环从上个月第一天到最后一天)
32，选择最有效率的表名顺序(只在基于规则的优化器中有效)： 
oracle 的解析器按照从右到左的顺序处理FROM子句中的表名，FROM子句中写在最后的表(基础表 driving table)将被最先处理，在FROM子句中包含多个表的情况下,你必须选择记录条数最少的表作为基础表。如果有3个以上的表连接查询, 那就需要选择交叉表(intersection table)作为基础表, 交叉表是指那个被其他表所引用的表.
33，提高GROUP BY语句的效率, 可以通过将不需要的记录在GROUP BY 之前过滤掉.下面两个查询返回相同结果，但第二个明显就快了许多. 
低效: 
SELECT JOB , AVG(SAL) 
FROM EMP 
GROUP BY JOB 
HAVING JOB ='PRESIDENT'
OR JOB ='MANAGER'
高效: 
SELECT JOB , AVG(SAL) 
FROM EMP 
WHERE JOB ='PRESIDENT'
OR JOB ='MANAGER'
GROUP BY JOB
34，sql语句用大写，因为oracle 总是先解析sql语句，把小写的字母转换成大写的再执行。
35，别名的使用，别名是大型数据库的应用技巧，就是表名、列名在查询中以一个字母为别名，查询速度要比建连接表快1.5倍。
36，避免死锁，在你的存储过程和触发器中访问同一个表时总是以相同的顺序;事务应经可能地缩短，在一个事务中应尽可能减少涉及到的数据量;永远不要在事务中等待用户输入。
37，避免使用临时表，除非却有需要，否则应尽量避免使用临时表，相反，可以使用表变量代替;大多数时候(99%)，表变量驻扎在内存中，因此速度比临时表更快，临时表驻扎在TempDb数据库中，因此临时表上的操作需要跨数据库通信，速度自然慢。
38，最好不要使用触发器，触发一个触发器，执行一个触发器事件本身就是一个耗费资源的过程;如果能够使用约束实现的，尽量不要使用触发器;不要为不同的触发事件(Insert，Update和Delete)使用相同的触发器;不要在触发器中使用事务型代码。
39，索引创建规则： 
表的主键、外键必须有索引； 
数据量超过300的表应该有索引； 
经常与其他表进行连接的表，在连接字段上应该建立索引； 
经常出现在Where子句中的字段，特别是大表的字段，应该建立索引； 
索引应该建在选择性高的字段上； 
索引应该建在小字段上，对于大的文本字段甚至超长字段，不要建索引； 
复合索引的建立需要进行仔细分析，尽量考虑用单字段索引代替； 
正确选择复合索引中的主列字段，一般是选择性较好的字段； 
复合索引的几个字段是否经常同时以AND方式出现在Where子句中？单字段查询是否极少甚至没有？如果是，则可以建立复合索引；否则考虑单字段索引； 
如果复合索引中包含的字段经常单独出现在Where子句中，则分解为多个单字段索引； 
如果复合索引所包含的字段超过3个，那么仔细考虑其必要性，考虑减少复合的字段； 
如果既有单字段索引，又有这几个字段上的复合索引，一般可以删除复合索引； 
频繁进行数据操作的表，不要建立太多的索引； 
删除无用的索引，避免对执行计划造成负面影响； 
表上建立的每个索引都会增加存储开销，索引对于插入、删除、更新操作也会增加处理上的开销。另外，过多的复合索引，在有单字段索引的情况下，一般都是没有存在价值的；相反，还会降低数据增加删除时的性能，特别是对频繁更新的表来说，负面影响更大。 
尽量不要对数据库中某个含有大量重复的值的字段建立索引。
40，mysql查询优化总结：使用慢查询日志去发现慢查询，使用执行计划去判断查询是否正常运行，总是去测试你的查询看看是否他们运行在最佳状态下。久而久之性能总会变化，避免在整个表上使用count(*),它可能锁住整张表，使查询保持一致以便后续相似的查询可以使用查询缓存 
，在适当的情形下使用GROUP BY而不是DISTINCT，在WHERE, GROUP BY和ORDER BY子句中使用有索引的列，保持索引简单,不在多个索引中包含同一个列，有时候MySQL会使用错误的索引,对于这种情况使用USE INDEX，检查使用SQL_MODE=STRICT的问题，对于记录数小于5的索引字段，在UNION的时候使用LIMIT不是是用OR。 
为了 避免在更新前SELECT，使用INSERT ON DUPLICATE KEY或者INSERT IGNORE ,不要用UPDATE去实现，不要使用 MAX,使用索引字段和ORDER BY子句，LIMIT M，N实际上可以减缓查询在某些情况下，有节制地使用，在WHERE子句中使用UNION代替子查询，在重新启动的MySQL，记得来温暖你的数据库，以确保您的数据在内存和查询速度快，考虑持久连接，而不是多个连接，以减少开销，基准查询，包括使用服务器上的负载，有时一个简单的查询可以影响其他查询，当负载增加您的服务器上，使用SHOW PROCESSLIST查看慢的和有问题的查询，在开发环境中产生的镜像数据中 测试的所有可疑的查询。
41，MySQL 备份过程: 
从二级复制服务器上进行备份。在进行备份期间停止复制，以避免在数据依赖和外键约束上出现不一致。彻底停止MySQL，从数据库文件进行备份。 
如果使用 MySQL dump进行备份，请同时备份二进制日志文件 – 确保复制没有中断。不要信任LVM 快照，这很可能产生数据不一致，将来会给你带来麻烦。为了更容易进行单表恢复，以表为单位导出数据 – 如果数据是与其他表隔离的。 
当使用mysqldump时请使用 –opt。在备份之前检查和优化表。为了更快的进行导入，在导入时临时禁用外键约束。 
为了更快的进行导入，在导入时临时禁用唯一性检测。在每一次备份后计算数据库，表以及索引的尺寸，以便更够监控数据尺寸的增长。 
通过自动调度脚本监控复制实例的错误和延迟。定期执行备份。
42，查询缓冲并不自动处理空格，因此，在写SQL语句时，应尽量减少空格的使用，尤其是在SQL首和尾的空格(因为，查询缓冲并不自动截取首尾空格)。
43，member用mid做標準進行分表方便查询么？一般的业务需求中基本上都是以username为查询依据，正常应当是username做hash取模来分表吧。分表的话 mysql 的partition功能就是干这个的，对代码是透明的； 
在代码层面去实现貌似是不合理的。
44，我们应该为数据库里的每张表都设置一个ID做为其主键，而且最好的是一个INT型的（推荐使用UNSIGNED），并设置上自动增加的AUTO_INCREMENT标志。
45，在所有的存储过程和触发器的开始处设置 SET NOCOUNT ON ，在结束时设置 SET NOCOUNT OFF 。 
无需在执行存储过程和触发器的每个语句后向客户端发送 DONE_IN_PROC 消息。
46，MySQL查询可以启用高速查询缓存。这是提高数据库性能的有效Mysql优化方法之一。当同一个查询被执行多次时，从缓存中提取数据和直接从数据库中返回数据快很多。
47，EXPLAIN SELECT 查询用来跟踪查看效果 
使用 EXPLAIN 关键字可以让你知道MySQL是如何处理你的SQL语句的。这可以帮你分析你的查询语句或是表结构的性能瓶颈。EXPLAIN 的查询结果还会告诉你你的索引主键被如何利用的，你的数据表是如何被搜索和排序的……等等，等等。
48，当只要一行数据时使用 LIMIT 1 
当你查询表的有些时候，你已经知道结果只会有一条结果，但因为你可能需要去fetch游标，或是你也许会去检查返回的记录数。在这种情况下，加上 LIMIT 1 可以增加性能。这样一样，MySQL数据库引擎会在找到一条数据后停止搜索，而不是继续往后查少下一条符合记录的数据。
49,选择表合适存储引擎： 
myisam: 应用时以读和插入操作为主，只有少量的更新和删除，并且对事务的完整性，并发性要求不是很高的。 
Innodb：事务处理，以及并发条件下要求数据的一致性。除了插入和查询外，包括很多的更新和删除。（Innodb有效地降低删除和更新导致的锁定）。对于支持事务的InnoDB类型的表来说，影响速度的主要原因是AUTOCOMMIT默认设置是打开的，而且程序没有显式调用BEGIN 开始事务，导致每插入一条都自动提交，严重影响了速度。可以在执行sql前调用begin，多条sql形成一个事物（即使autocommit打开也可以），将大大提高性能。
50,优化表的数据类型,选择合适的数据类型： 
原则：更小通常更好，简单就好，所有字段都得有默认值,尽量避免null。 
例如：数据库表设计时候更小的占磁盘空间尽可能使用更小的整数类型.(mediumint就比int更合适) 
比如时间字段：datetime和timestamp, datetime占用8个字节，而timestamp占用4个字节，只用了一半，而timestamp表示的范围是1970—2037适合做更新时间 
MySQL可以很好的支持大数据量的存取，但是一般说来，数据库中的表越小，在它上面执行的查询也就会越快。 
因此，在创建表的时候，为了获得更好的性能，我们可以将表中字段的宽度设得尽可能小。例如， 
在定义邮政编码这个字段时，如果将其设置为CHAR(255),显然给数据库增加了不必要的空间， 
甚至使用VARCHAR这种类型也是多余的，因为CHAR(6)就可以很好的完成任务了。同样的，如果可以的话， 
我们应该使用MEDIUMINT而不是BIGIN来定义整型字段。 
应该尽量把字段设置为NOT NULL，这样在将来执行查询的时候，数据库不用去比较NULL值。 
对于某些文本字段，例如“省份”或者“性别”，我们可以将它们定义为ENUM类型。因为在MySQL中，ENUM类型被当作数值型数据来处理， 
而数值型数据被处理起来的速度要比文本类型快得多。这样，我们又可以提高数据库的性能。
51， 字符串数据类型：char，varchar，text选择区别
52，任何对列的操作都将导致表扫描，它包括数据库函数、计算表达式等等，查询时要尽可能将操作移至等号右边。
```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```

```sql

```