
## SQL1 各个视频的平均完播率

https://www.nowcoder.com/practice/96263162f69a48df9d84a93c71045753?tpId=268&tqId=2285032&ru=/exam/oj&qru=/ta/sql-factory-interview/question-ranking&sourceUrl=%2Fexam%2Foj%3Ftab%3DSQL%25E7%25AF%2587%26topicId%3D268

```sql
select a.video_id,
round(sum(case when timestampdiff(second,b.start_time,b.end_time)>=a.duration then 1 else 0 end)/count(b.uid),3)
as avg_com_play_rate
from tb_video_info a join tb_user_video_log b on a.video_id=b.video_id
where YEAR(b.start_time) = 2021
group by a.video_id
order by avg_com_play_rate desc
```

```sql
select log.video_id,
round(sum(if(TIMESTAMPDIFF(second,log.start_time,log.end_time)
             >=info.duration,1,0))/count(log.video_id) ,3)
as avg_comp_play_rate
from tb_user_video_log  as log inner join tb_video_info as info
on log.video_id=info.video_id
where year(log.start_time)=2021
group by video_id 
order by avg_comp_play_rate desc

```

```sql
select t1.video_id,
    round(avg(if(TIMESTAMPDIFF(second, start_time, end_time) >= t2.duration, 1, 0)),3) avg_comp_play_rate
from tb_user_video_log t1
join tb_video_info t2 on t1.video_id = t2.video_id
where YEAR(start_time) = 2021
group by t1.video_id
order by avg_comp_play_rate desc

```

```sql
SELECT a.video_id, 
round(sum(if((end_time-start_time-b.duration) >=0,1,0))/count(a.video_id),3) avg_comp_play_rate
FROM tb_user_video_log a
left join tb_video_info b
on a.video_id = b.video_id
where year(start_time) = 2021
GROUP BY a.video_id
order by avg_comp_play_rate desc
```

```sql
select m1.video_id
  ,round(sum(case when m1.bftime>=m1.duration then 1 else 0 end)/ count(*),3) as avg_comp_play_rate
from(
select t2.video_id
      ,(t1.end_time-t1.start_time) as bftime
      ,t2.duration
from tb_user_video_log t1
left join tb_video_info t2
on t2.video_id=t1.video_id
where substr(start_time,1,4)=2021
) as m1
group by 1
order by avg_comp_play_rate desc
```

## SQL2 平均播放进度大于60%的视频类别

https://www.nowcoder.com/practice/c60242566ad94bc29959de0cdc6d95ef?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
select
tag,
concat(
    ROUND(
        avg(
            if(
                timestampdiff(second,start_time,end_time)>=duration,
                1,timestampdiff(second,start_time,end_time)/duration
               )
            )*100
            ,2)
    ,'%') avg_play_progress
from 
tb_user_video_log a join tb_video_info b
on a.video_id=b.video_id
group by b.tag
having avg(
            if(
                timestampdiff(second,start_time,end_time)>=duration,
                1,timestampdiff(second,start_time,end_time)/duration
               )
    )>0.6
order by avg_play_progress desc
```

```sql
select tag,
concat(round(avg(if(TIMESTAMPDIFF(second,start_time,end_time)>=duration,1,TIMESTAMPDIFF(second,start_time,end_time)/duration))*100,2)
,'%') as avg_play_progress
from tb_user_video_log join tb_video_info t1 using(video_id)
group by tag
having avg(if(TIMESTAMPDIFF(second,start_time,end_time)>=duration,1,TIMESTAMPDIFF(second,start_time,end_time)/duration))>0.6
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
HAVING avg_played_progress> 60
ORDER BY avg_played_progress desc
```

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

```sql
with t as(
    select tag,
        sum(duration) sum_duration,
        sum(if(TIMESTAMPDIFF(second,start_time,end_time)>=duration,duration,TIMESTAMPDIFF(second,start_time,end_time))) sum_sec
    from tb_user_video_log vl
    left join tb_video_info vi
    on vl.video_id=vi.video_id
    group by tag
)
select tag,concat(round(sum_sec/sum_duration*100,2),'%') avg_progress
from t
where sum_sec/sum_duration>0.6
order by avg_progress desc;


```

## SQL3 每类视频近一个月的转发量/率

https://www.nowcoder.com/practice/a78cf92c11e0421abf93762d25c3bfad?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
select b.tag, sum(if_retweet) retweet_cnt,  round(sum(if_retweet)/count(1),3) retweet_rate from
tb_user_video_log a
left join  tb_video_info b
on a.video_id = b.video_id
where DATEDIFF(DATE((select max(start_time) from tb_user_video_log)) ,DATE(start_time)) <= 29
group by b.tag
order by 2 desc
```

```sql
select tag,
sum(if_retweet) as retweet_count,
round(sum(if_retweet) / count(*), 3) as retweet_rate
from tb_user_video_log t1
join tb_video_info t2
on t1.video_id = t2.video_id
where datediff(date((select max(start_time) from tb_user_video_log)), date(start_time)) <= 29
group by tag
order by retweet_rate desc
```


```sql
select 
tag,
sum(case when a.if_retweet ="1" then 1 else 0 end )as retweet_cnt,
round(sum(case when a.if_retweet ="1" then 1 else 0 end )/count(a.video_id)
,3)
from tb_user_video_log a 
left join 
tb_video_info b 
on a.video_id=b.video_id
where DATEDIFF(DATE((select max(start_time) FROM tb_user_video_log)),
             DATE(a.start_time)) <= 29
group by tag
order by  retweet_cnt desc
```

```sql
SELECT b.tag, SUM(if_retweet) retweet_cnt, ROUND(SUM(if_retweet)/COUNT(*), 3) retweet_rate
FROM tb_user_video_log a
LEFT JOIN tb_video_info b
ON a.video_id = b.video_id
WHERE DATEDIFF(DATE((select max(start_time) FROM tb_user_video_log)), DATE(a.start_time)) <= 29
GROUP BY b.tag
ORDER BY retweet_rate desc
```

```sql
select tag,
sum(if_retweet) retweet_count,
round(sum(if_retweet) / count(1), 3) retweet_rate
from tb_user_video_log a
join tb_video_info b
on a.video_id = b.video_id
where timestampdiff(day,start_time,(select max(start_time) from tb_user_video_log))<=29
group by tag
order by retweet_rate desc
```

## SQL4 每个创作者每月的涨粉率及截止当前的总粉丝量

```sql
SELECT 
  author, 
  DATE_FORMAT(start_time, '%Y-%m') month, 
  ROUND(SUM(IF(if_follow = 2, -1, if_follow)) / COUNT(*), 3) fans_growth_rate, 
  SUM(SUM(IF(if_follow = 2, -1, if_follow))) OVER (PARTITION BY author ORDER BY DATE_FORMAT(start_time, '%Y-%m')) total_fans 
FROM tb_video_info 
LEFT JOIN tb_user_video_log USING(video_id) 
WHERE YEAR(start_time) = 2021
GROUP BY 1,2
ORDER BY 1,4
```

```sql
SELECT author, month,
       ROUND((increase-decrease)/watch, 3) AS fans_growth_rate,
       SUM(increase-decrease) OVER(PARTITION BY author ORDER BY month) AS total_fans
FROM (
    SELECT i.author, DATE_FORMAT(end_time, "%Y-%m") AS month,
           SUM(IF(l.if_follow=1, 1, 0)) AS increase,
           SUM(IF(l.if_follow=2, 1, 0)) AS decrease,
           COUNT(uid) AS watch
    FROM tb_user_video_log l
    JOIN tb_video_info i
    ON l.video_id = i.video_id
    WHERE YEAR(end_time) = 2021
    GROUP BY i.author, month
) AS a
ORDER BY author, total_fans
```

```sql
select author,date_format(start_time,'%Y-%m')ym,
round((sum(case if_follow when 1 then 1 when 2 then -1 else 0 end)/count(a.id)),3)fans_growth_rate,
sum(sum(case if_follow when 1 then 1 when 2 then -1 else 0 end))over(partition by author order by
date_format(start_time,'%Y-%m'))total_fans from tb_user_video_log a
join tb_video_info b
on a.video_id=b.video_id
where year(end_time)=2021
group by author,ym
order by author,total_fans 

```

```sql
select 
    author
    ,mon
    ,round(add_fans/counts,3) fans_growth_rate
    ,sum(add_fans) over(partition by author order by mon) total_fans
from (
    select 
        author
        ,DATE_FORMAT(start_time,'%Y-%m') mon
        ,sum(case when if_follow = 2 then -1 else if_follow end) add_fans
        ,count(*) counts
    from tb_user_video_log t1
    join tb_video_info t2 on t1.video_id = t2.video_id
    where year(start_time) = 2021
    group by author, mon
) t
ORDER BY author,total_fans
```

```sql
SELECT author ,DATE_FORMAT(A.start_time,'%Y-%m') month,
       ROUND(SUM(IF(if_follow=2,-1,if_follow))/ COUNT(*),3) fans_growth_rate ,
       SUM(SUM(IF(if_follow = 2, -1, if_follow))) OVER (PARTITION BY author ORDER BY DATE_FORMAT(start_time, '%Y-%m')) total_fans
       FROM tb_user_video_log A 
LEFT JOIN tb_video_info B
ON A.video_id = B.video_id
WHERE YEAR(start_time) = 2021
GROUP BY author,month
ORDER BY author,total_fans
```

## SQL5 国庆期间每类视频点赞量和转发量

注意时间的表达方式：

https://www.nowcoder.com/practice/f90ce4ee521f400db741486209914a11?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

ORDER BY dt DESC rows BETWEEN CURRENT ROW AND 6 FOLLOWING：

```sql
SELECT * FROM
(SELECT tag, dt, 
       SUM(lick_cnt) OVER w,
       MAX(retweet_cnt) OVER w
FROM
(SELECT tag, DATE_FORMAT(start_time, '%Y-%m-%d') AS dt,
SUM(if_like) lick_cnt, SUM(if_retweet) retweet_cnt
FROM tb_user_video_log JOIN tb_video_info USING(video_id)
WHERE start_time BETWEEN '20210925' AND '20211004' GROUP BY tag, dt) t
WINDOW w AS (PARTITION BY tag ORDER BY dt DESC rows BETWEEN CURRENT ROW AND 6 FOLLOWING)
) t1
WHERE dt BETWEEN '2021-10-01' AND '2021-10-04'
ORDER BY tag DESC, dt ASC
```

rows 6 preceding

```sql
select * from (

select tag,dt,
     sum(like_cnt) over(partition by tag order by t3.dt rows 6 preceding) total_like,
     max(retweet_cnt) over(partition by tag order by t3.dt rows 6 preceding) max_retweet
from (
    select tag,date(start_time) dt, sum(if_like) like_cnt,
                                        sum(if_retweet) retweet_cnt
    from tb_user_video_log t1 inner join tb_video_info t2 on t1.video_id=t2.video_id
    where date(start_time) between '2021-09-25' and '2021-10-03' 
    group by tag,date(start_time)
    ) t3 )t4 where t4.dt between '2021-10-01' and '2021-10-03'
    order by tag desc,dt
    
```

order by dt desc rows between current row and 6 following

```sql
select *
from (
    select tag,dt,sum(like_cnt) over(partition by tag order by dt desc rows between current row and 6 following )
    as sum_like_cnt_7d,max(retwwet_cnt) over(partition by tag order by dt desc rows between current row and 6 following)
    as max_retweet_cnt_7d
    from (
        select tag,date(start_time) as dt,sum(if_like) as like_cnt,sum(if_retweet) as retwwet_cnt
        from tb_user_video_log as uvl
        left join tb_video_info as vi
        on uvl.video_id = vi.video_id
        where date(start_time) between date_sub('2021-10-01',interval 6 day) and '2021-10-03'
        group by tag,dt
        order by tag,dt DESC
        ) as temp
    ) as t
where dt between '2021-10-01' and '2021-10-03'
order by tag desc,dt 

```

rows 6 preceding

```sql
select t2.*
from (select t1.tag,t1.d
      ,sum(t1.if_like_sum)over(partition by t1.tag order by t1.d rows 6 preceding)
      ,max(t1.if_retweet_sum)over(partition by t1.tag order by t1.d rows 6 preceding)
      from (select tag,date(start_time) d
            ,sum(if_like) if_like_sum
            ,sum(if_retweet) if_retweet_sum
            from tb_user_video_log tvl,tb_video_info tvi
            where tvl.video_id=tvi.video_id
            group by tag,d) as t1
     ) as t2
where t2.d between '2021-10-01' and '2021-10-03'
order by t2.tag desc,t2.d

```

ROWS 6 PRECEDIN 

```sql
SELECT tag, dt, sum_like_cnt_7d, max_retweet_cnt_7d
FROM (
    SELECT tag, dt,
           SUM(likes) OVER(PARTITION BY tag ORDER BY dt ROWS 6 PRECEDING) AS sum_like_cnt_7d,
           MAX(retweets) OVER(PARTITION BY tag ORDER BY dt ROWS 6 PRECEDING) AS max_retweet_cnt_7d
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
ORDER BY tag DESC, dt ASC
```

## SQL6 近一个月发布的视频中热度最高的top3视频

https://www.nowcoder.com/practice/0226c7b2541c41e59c3b8aec588b09ff?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

注意：

date((select max(end_time) from tb_user_video_log))

和

max(date(end_time))

group by video_id

的区别

```sql
select video_id,
round((100*fin_rate + 5*like_sum + 3*comment_sum + 2*retweet_sum)
      /(unfin_day_cnt+1),0) hot_index
from(
  select 
    video_id,duration,
    avg(if(timestampdiff(second,start_time,end_time)>=duration,1,0)) fin_rate,
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

```sql
select video_id,round((100*sc+5*like1+3*comment1+retweet1*2)/(dt+1),0) a1
from(select video_id,duration,sum(if_like) like1,
        sum(if_retweet) retweet1,count(comment_id) comment1
        ,sum(TIMESTAMPDIFF(second,start_time,end_time)>=duration)/count(*) sc,
        datediff(date((select max(end_time) from tb_user_video_log)),max(date(end_time))) dt
        from tb_user_video_log t1
        left join tb_video_info t2
        using(video_id)
        where datediff(date((select max(end_time) from tb_user_video_log))
        ,date(release_time))<=29
        group by video_id
)t
order by a1 desc
limit 3
```

timestampdiff(day,xxx,xxx) <= 29

等效于

datediff(xxx,xxx)<=29

```sql
select vid,round((a_c+s_l+s_c+s_r)/
                 (datediff(
                     date((select max(end_time) from tb_user_video_log)),
                     date(p_t))
                  +1),0) h
from(
    SELECT AVG(IF(TIMESTAMPDIFF(
                SECOND,start_time,end_time
                )>=duration,1,0)
              )*100 a_c,
     SUM(if_like)*5 s_l,
     SUM((comment_id IS NOT NULL))*3 s_c,
     SUM(if_retweet)*2 s_r,
     MAX(end_time) p_t,
     tuvl.video_id vid,
     max(tvi.release_time) r_t

    FROM tb_user_video_log tuvl
    JOIN tb_video_info tvi
    ON tuvl.video_id=tvi.video_id
    
    GROUP BY tuvl.video_id
    having timestampdiff(day,
                         DATE(r_t),
                         date((select max(end_time) from tb_user_video_log))
                        ) < 30
) t1
ORDER BY h DESC
LIMIT 3;

```

```sql
select video_id,
       round((100*completion_rate+5*sum_if_like+3*count_comment_id+2*sum_if_retweet)*(1/(day_new+1)),0) of_heat
from
(select t1.video_id,
       datediff((select max(end_time)from tb_user_video_log),max(tuvl.end_time)) day_new,  -- 最近无播放天数
       avg(case
                when timestampdiff(second, start_time, end_time) >= duration  then  1
                else 0
            end) completion_rate,
       sum(if_like) sum_if_like,
       count(comment_id) count_comment_id,
       sum(if_retweet) sum_if_retweet
from tb_video_info t1
join tb_user_video_log tuvl on t1.video_id = tuvl.video_id
where datediff(date((select max(end_time)from tb_user_video_log)), date(t1.release_time))<=29
group by t1.video_id) a
order by of_heat desc limit 3;
```

```sql
SELECT
  video_id,
  ROUND((100 * finished_rate 
   + 5 * like_cnt 
   + 3 * comment_count 
   + 2 * retweet_cnt) / (unfinished_day_cnt + 1)) hot_index
FROM (
  SELECT
    i.video_id,
    SUM(TIMESTAMPDIFF(second, start_time, end_time) >= duration) / COUNT(*) finished_rate,
    SUM(if_like = 1) like_cnt,
    SUM(IF(comment_id IS NOT NULL, 1, 0)) comment_count,
    SUM(if_retweet = 1) retweet_cnt,
    DATEDIFF(DATE((SELECT MAX(end_time) FROM tb_user_video_log)), MAX(DATE(end_time))) unfinished_day_cnt
  FROM tb_video_info i
  JOIN tb_user_video_log USING(video_id)
  WHERE DATEDIFF(DATE((SELECT MAX(end_time) FROM tb_user_video_log)), DATE(release_time)) <= 29 
  GROUP BY 1
) t
ORDER BY 2 DESC LIMIT 3

```

## SQL7 2021年11月每天的人均浏览文章时长

https://www.nowcoder.com/practice/8e33da493a704d3da15432e4a0b61bb3?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
select DATE_FORMAT(in_time,'%Y-%m-%d') as a,
round(sum(TIMESTAMPDIFF(second,in_time,out_time))/count(distinct uid),1) as b
from tb_user_log
where year(in_time)=2021 and month(in_time)=11 and artical_id!=0
group by a
order by b ;
```

```sql
select dt,round(sum(sc)/count(distinct uid),1) avg_view_len_sec
from 
(select uid,date(out_time) dt,TIMESTAMPDIFF(SECOND,in_time,out_time) sc,artical_id from tb_user_log) t 
where year(dt) = "2021" and month(dt) = "11" and artical_id != 0
group by dt
order by avg_view_len_sec ;
```

```sql
select date(in_time) dt
,round(sum(timestampdiff(second,in_time,out_time))/count(distinct uid),1) sec
from tb_user_log
where year(in_time)=2021 and month(in_time)=11 and artical_id != 0
group by dt
order by sec
```

```sql
SELECT date_format(in_time, '%Y-%m-%d') dt, 
      round(sum(TIMESTAMPDIFF(SECOND,tl.in_time,tl.out_time))/count(DISTINCT tl.uid),1)avg_view_len_sec
FROM tb_user_log AS tl
where tl.artical_id != 0
and DATE_FORMAT(tl.in_time,'%Y-%m')='2021-11'
group by date_format(tl.in_time, '%Y-%m-%d')
order by avg_view_len_sec
```

date_format(in_time, '%Y-%m-%d')

等效于

date(in_time)

```sql
SELECT
     date(in_time)as dt,
     round(sum(TIMESTAMPDIFF(SECOND,in_time,out_time))/ COUNT(DISTINCT uid),1) as avg_view_len_sec
from tb_user_log
where date_format(in_time,"%Y-%m") = "2021-11" and artical_id  != 0
group by dt
order by avg_view_len_sec
```

```sql
select DATE_FORMAT(in_time,'%Y-%m-%d') as a,
round(sum(TIMESTAMPDIFF(second,in_time,out_time))/count(distinct uid),1) as b
from tb_user_log
where year(in_time)=2021 and month(in_time)=11 and artical_id!=0
group by a
order by b ;
```

## SQL8 每篇文章同一时刻最大在看人数

https://www.nowcoder.com/practice/fe24c93008b84e9592b35faa15755e48?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

注意一定要按照 in_time 和 out_time 的升序排序

```sql
with t as
(SELECT artical_id, in_time dt, 1 diff #开始等待，人数+1
FROM tb_user_log
where artical_id !=0
union all
SELECT artical_id, out_time dt, -1 diff #开始等待，人数+1
FROM tb_user_log
where artical_id !=0
)

select artical_id,max(cnt)ca
from
(SELECT artical_id,dt,SUM(diff) OVER(PARTITION BY artical_id ORDER BY dt,diff desc) cnt
from t)t1
group by artical_id
order by ca desc
```

```sql
SELECT artical_id, max(t1) as t2
FROM(
    SELECT artical_id,t,
    sum(m)over(partition by artical_id order by t,m desc) as t1
    from(
        select artical_id,in_time as t,1 as m
        from tb_user_log
        where artical_id<>0
        UNION ALL
        select artical_id,out_time as t,-1 as m
        from tb_user_log
        where artical_id<>0
    )a
)b
group by artical_id
order by t2 desc


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

```sql
with t2 as
(
    select artical_id,sum(x) over(partition by artical_id order by time,x desc) sum_x
    from
    (
        select artical_id,in_time time,1 x from tb_user_log
        where artical_id like '9%'
        union all 
        select artical_id,out_time time,-1 x from tb_user_log
        where artical_id like '9%'
        order by time,x desc
    ) t1
)
select distinct artical_id,sum_x
from t2 t
where sum_x >= all(select sum_x from t2 where t2.artical_id=t.artical_id)
order by sum_x desc
```

```sql
SELECT artical_id, MAX(current_max) as max_uv
FROM (
    SELECT artical_id, at_time,
        SUM(uv) over(PARTITION BY artical_id ORDER BY at_time, uv DESC) as current_max
    FROM (
        SELECT artical_id, in_time as at_time, 1 as uv FROM tb_user_log
        UNION ALL
        SELECT artical_id, out_time as at_time, -1 as uv FROM tb_user_log
        ORDER BY at_time
    ) as t_uv_at_time
    WHERE artical_id != 0
) as t_artical_cur_max
GROUP BY artical_id
ORDER BY max_uv DESC;

```

## SQL9 2021年11月每天新用户的次日留存率

https://www.nowcoder.com/practice/1fc0e75f07434ef5ba4f1fb2aa83a450?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
select a.dt, 
    round(count(distinct b.uid)/count(distinct a.uid),2) uv_left_rate
from (
    select uid , min(date(in_time)) dt
    from tb_user_log 
    group by uid ) a
left join 
     (select uid,date(in_time) dt
    from tb_user_log
    union 
    select uid,date(out_time) dt
    from tb_user_log) b
on DATE_ADD(a.dt,INTERVAL 1 DAY) = b.dt and a.uid=b.uid
where a.dt like '2021-11%'
group by a.dt 
order by a.dt
```


```sql
select 
    a.dt,
    ifnull(round(count(distinct b.uid)/count(a.uid),2),0)uv_left_rate
from
    (SELECT uid,date(min(in_time)) dt
    FROM tb_user_log
    GROUP BY uid) a 
left join
    (SELECT uid,DATE(in_time) dt
    FROM tb_user_log
    union
    SELECT uid,DATE(out_time) dt
    FROM tb_user_log) b 
ON a.uid = b.uid
and b.dt = DATE_ADD(a.dt,INTERVAL 1 DAY)
WHERE a.dt like "2021-11%"
GROUP BY a.dt
ORDER BY a.dt;
```

```sql
select t1.dt,round(count(distinct t2.uid)/avg(t1.cnt),2) as uv_left_rate
from
(select uid,min(date(in_time)) as dt,
 count(uid) over(partition by min(date(in_time))) as cnt 
 from tb_user_log
group by uid)t1
left join tb_user_log t2 on t1.uid = t2.uid
and date_add(t1.dt,interval 1 day) between date(t2.in_time) and date(t2.out_time)
where date_format(t1.dt,'%Y-%m') = '2021-11'
group by t1.dt
order by t1.dt
```

```sql
select 
    a.dt,
    ifnull(round(count(distinct b.uid)/count(a.uid),2),0) uv_left_rate
from
(SELECT uid,date(min(in_time)) dt
    FROM tb_user_log
    GROUP BY uid)a
left join
    (SELECT uid,DATE(in_time) dt
    FROM tb_user_log
    union
    SELECT uid,DATE(out_time) dt
    FROM tb_user_log) b 
ON a.uid = b.uid
and b.dt = DATE_ADD(a.dt,INTERVAL 1 DAY)
WHERE a.dt like "2021-11%"
GROUP BY a.dt
ORDER BY a.dt
```

```sql
select 
    a.dt,
    ifnull(round(count(distinct b.uid)/count(a.uid),2),0)uv_left_rate
from
    # 每个用户最初注册的日期
    (SELECT uid,date(min(in_time)) dt
    FROM tb_user_log
    GROUP BY uid) a 
# 把日期拆分成登陆和退出日期，再通过uid联立表
# 查找每个用户注册日期增加1天后的日期是否在登陆和退出日期当中
left join
    (
    SELECT uid,DATE(in_time) dt
    FROM tb_user_log
    union
    SELECT uid,DATE(out_time) dt
    FROM tb_user_log
    ) b 
ON a.uid = b.uid
# 每个uid在初次登陆日期的第二天
and b.dt = DATE_ADD(a.dt,INTERVAL 1 DAY)
# WHERE的时机，在全部表联合完之后进行筛选
WHERE a.dt like "2021-11%"
GROUP BY a.dt
ORDER BY a.dt;
```

```sql
select dt,ifnull(round(next_num/new_cnt,2),0)
from 
(select dt,count(distinct case when rk = 1 then uid end) new_cnt,
           count(distinct case when rk = 1 and ( datediff(next_date,dt)=1 or datediff(dt1,dt)=1 ) then uid end) next_num
from (SELECT uid,DATE(in_time) dt,lead(DATE(in_time),1)over(partition by uid order by in_time) next_date,
        DENSE_RANK()over(partition by uid order by DATE(in_time) ) as rk,
        date(out_time) dt1
      FROM tb_user_log
     ) t
 where year(dt)=2021 and month(dt)=11 
group by dt) t1
where new_cnt > 0
order by dt

```

## SQL10 统计活跃间隔对用户分级结果

https://www.nowcoder.com/practice/6765b4a4f260455bae513a60b6eed0af?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
select user_grade,round(count(uid)/(select count(distinct uid) from tb_user_log),2) q
from  
(
select uid,(case when datediff((select max(in_time) from tb_user_log),max(in_time)) <=6 
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
select user_grade,
       round(count(uid)/(select count(distinct uid) from tb_user_log),2) ratio
from ( SELECT uid,
              case
              when (max(date(in_time)) BETWEEN '2021-10-29' and '2021-11-04')
                   and (min(date(in_time)) <'2021-10-29' ) then '忠实用户'
              when min(date(in_time)) BETWEEN '2021-10-29'
                   and '2021-11-04' then '新晋用户'
              when (max(date(in_time)) <= '2021-10-28')
                    and (max(date(in_time)) >= '2021-10-06') then '沉睡用户'
              else '流失用户' end as user_grade
              FROM tb_user_log
              GROUP BY uid ) a
GROUP BY user_grade
ORDER BY ratio desc
```

count(uid) over()

group by uid

和

count(uid)

group by uid

的区别

```sql
-- 2021年12月13日

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
    , count(uid) over() as all_user_cnt
    from tb_user_log
    group by uid
) t
group by 1
order by ratio desc
;
```

```sql
select grade,round(count(uid)/(select count(distinct uid) from tb_user_log),2) as ratio
from (
      select uid,
                (case when datediff((select date(max(out_time)) from tb_user_log),max(date(in_time)))<=6 and datediff((select date(max(out_time))  from tb_user_log),date(min(in_time)))>=7 then '忠实用户'
                      when datediff((select date(max(out_time))  from tb_user_log),date(min(in_time)))<=6 then '新晋用户'
                      when datediff((select date(max(out_time))  from tb_user_log),date(max(in_time))) between 7 and 29 then '沉睡用户'
                      when datediff((select date(max(out_time))  from tb_user_log),date(max(in_time))) >=30 then '流失用户'
                end) as grade
      from tb_user_log
      group by uid
      ) a
group by grade
order by ratio desc
```

```sql
select user_grade,
round(count(uid)/(select count(distinct uid) from tb_user_log),2) as ratio
#计算总人数的方法
from
(select uid,
case when datediff(date(today),date(first_time))<7 then '新晋用户'
     when datediff(date(today),date(first_time))>=7 
     and datediff(date(today),date(recent_time))<7
     then '忠实用户'
     when datediff(date(today),date(first_time))>=7 
     and datediff(date(today),date(recent_time)) between 7 and 29
     then '沉睡用户'
     else '流失用户' end
     as user_grade
from
(select uid,
min(in_time) as first_time,
max(in_time) as recent_time
from tb_user_log 
group by uid)t1
 join 
(select max(in_time) as today
from tb_user_log
)t2
 )T
group by user_grade
order by ratio desc
```

```sql
with t1 as
(select t.uid, datediff('2021-11-04',min_it) date_min,datediff('2021-11-04',max_it) date_max
from tb_user_log t join (select uid,min(in_time) min_it ,max(in_time) max_it 
                         from tb_user_log group by uid )  last_date 
                   using(uid)
group by t.uid)
 
select user_grade,round(count(*)/ ( select count(*) from t1),2) ratio
from (select t1.uid,case when date_min <7 and date_max <7 then '新晋用户' 
              when date_min >=7 and date_max >=7 and date_max < 30 then '沉睡用户' 
              when  date_min >=7 and date_max >=30 then '流失用户'
              else '忠实用户' end as user_grade
        from t1) t2
group by user_grade 
order by ratio desc


```

## SQL11 每天的日活数及新用户占比

https://www.nowcoder.com/practice/dbbc9b03794a48f6b34f1131b1a903eb?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
select
	dt,
	count(uid)as dau,
	round(sum(if(times=1,1,0))/count(uid),2)as nv_new_ratio
from
(
select 
	*,
	count(*) over (partition by uid order by dt)as times
from
(
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
with t1 as (
    select uid,min(date(in_time)) min_date
    from tb_user_log
    group by uid),
t2 as(
    select uid,date(in_time) dt
    from tb_user_log
    union
    select uid,date(out_time) dt
    from tb_user_log)

select dt,count(*),round(count(min_date)/count(*),2)
from(
    SELECT t2.uid,dt,min_date
    from t2
    left join t1 on t2.uid=t1.uid and t2.dt=t1.min_date) t3
group by dt
order by dt

```

```sql
SELECT m.dt,count(DISTINCT m.uid) dau,
				round(count(DISTINCT n.uid)/count(DISTINCT m.uid),2) uv_new_ratio
FROM(
SELECT uid,date(in_time) dt
FROM tb_user_log 
union ALL
SELECT uid,date(out_time) dt
FROM tb_user_log 
where date(in_time)!=date(out_time)) m
left join 
(SELECT uid,min(date(in_time)) dt
FROM tb_user_log
GROUP BY uid) n 
on m.uid=n.uid and m.dt=n.dt
GROUP BY m.dt
order by m.dt
```

```sql
select a.time dt,count(distinct a.uid) dau,
round(sum(case when a.time=b.min_time then 1 else 0 end)/count(distinct a.uid),2) uv_new_ratio from
(select uid,date(in_time) time from tb_user_log
union
select uid,date(out_time) time from tb_user_log) a
left join
(select uid,min(date(in_time)) min_time from tb_user_log group by uid) b
on a.uid = b.uid
group by dt order by dt asc;
```

```sql
/*建立临时表，存储uid,以及活跃的日期dt*/
with r as 
(select uid,date(in_time) dt
from tb_user_log
union        /*union 和 union all的区别：union会自动去重*/
select uid,date(out_time) dt
from tb_user_log
)


select t1.dt,dau,ifnull(round(cnt/dau,2),0) as uv_new_ratio
from 
(select dt, count( uid) dau
from r
group by dt) t1    /* t1:日期及每天活跃的人数*/
left join     /*t1中所有日期，t2中只是有新用户登陆的日期，有可能某天没有新用户，但仍然有活跃用户*/
(select min_dt as dt,count( uid) cnt
from 
(select uid,min(date(in_time)) as min_dt
from tb_user_log
group by uid) a     /*a:每个用户第一次登陆的日期，即他成为新用户的日期*/
group by dt) t2    /*t2:日期及每天的新用户人数*/
on t1.dt=t2.dt

/*
每天新用户的个数：每个人及其成为新用户的日期，然后按日期分组计数
*/

```

```sql
select a.dt,count(distinct a.uid) dau,
      round(count(distinct b.uid)/count(distinct a.uid),2) uv_new_ratio
from(select uid,date_format(in_time,"%Y-%m-%d") dt
     from tb_user_log
     union
     select uid,date_format(out_time,"%Y-%m-%d") dt
     from tb_user_log) a 
left JOIN (select uid,date_format(min(in_time),"%Y-%m-%d") ct 
                from tb_user_log
                group by uid) b 
       on a.uid = b.uid 
       and a.dt = b.ct
group by a.dt
order by a.dt asc
```

## SQL12 连续签到领金币

https://www.nowcoder.com/practice/aef5adcef574468c82659e8911bb297f?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

日期：12345789

rank：12345678

日期-rank：00000111

partition排序：12345123  

```sql
#给确定每个人每个签到阶段的起始日期
select uid,date_format(dt,'%Y%m') as month
,sum(case when stage_index=2 then 3
   when stage_index=6 then 7
   else 1 end) as coin
from(
    select uid,dt
    ,(row_number()over(partition by uid,init_date order by dt)-1)%7 as stage_index
    from
    (
        select uid,dt,rn,subdate(dt,rn) as init_date
        from (
            #给符合条件的每个人的日期编号
            select uid,date(in_time) as dt
            ,row_number()over(partition by uid order by date(in_time)) as rn
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
           END AS score
    FROM (
        SELECT uid, dt, DATE_SUB(dt, INTERVAL rn DAY) AS first_day,
               ROW_NUMBER() OVER(PARTITION BY uid, DATE_SUB(dt, INTERVAL rn DAY) ORDER BY dt) AS ranking
        FROM (
            SELECT DISTINCT uid, DATE_FORMAT(in_time, "%Y%m%d") AS dt,
                   DENSE_RANK() OVER(PARTITION BY uid ORDER BY DATE_FORMAT(in_time, "%Y%m%d")) AS rn
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
with t as(
SELECT uid,date(in_time) date_t,sign_in,ROW_NUMBER() over(partition by uid order by date(in_time)) as rank_t
from tb_user_log
where date(in_time) between '2021-07-07' and '2021-10-31' and artical_id = 0 and sign_in =1)

select uid,date_format(date_t1,"%Y%m") month,
    sum(if(cnt/7>=1,floor(cnt/7)*6+(floor(cnt/7)+if(cnt%7>=3,1,0))*2+cnt,cnt+if(cnt/3>=1,1,0)*2))
from(
    select uid,(date_t-rank_t) date_t1,count(date_t-rank_t) cnt
    from t
    group by uid,date_t1) t2
group by uid,month
```

```sql
SELECT uid,date_format(in_time,'%Y%m') month,sum(case when rk_2 % 7 = 3 then 3 when rk_2 % 7 = 0 then 7 else 1 end) coins
FROM (SELECT uid,in_time,rk,DATE_SUB(in_time,INTERVAL rk day) in_time_0,
      row_number()over(partition by uid,DATE_SUB(in_time,INTERVAL rk day) order by in_time) rk_2
      FROM (SELECT DISTINCT uid,DATE_FORMAT(in_time,'%Y-%m-%d') in_time,
            DENSE_RANK()over(partition by uid order by DATE_FORMAT(in_time,'%Y-%m-%d')) rk
            FROM tb_user_log
            WHERE (artical_id = 0 and sign_in = 1) and 
            (DATE_FORMAT(in_time,'%Y-%m-%d') >= '2021-07-07' and DATE_FORMAT(in_time,'%Y-%m-%d') <= '2021-10-31')) t1)t2
GROUP BY month,uid
ORDER BY month,uid
```

```sql
# 第一步：先过滤掉不签到的，重复的
# 第二步： ranK()over(partition by uid order by dt) --按照签名排序 
# 第三步：date_sub(dt,interval ranK()over(partition by uid order by dt) 
#          按照签到日期减去签到排名的差值 排序，（如果是连续签到，则得到的日期相同）-- 即得到连续签到的天数，rank_day
# 第四步：rank()over(partition by uid,rank_day order by dt)，按照连续签的天数到排序，在mod与7，即可以判断连续签到的天数是否可以达到额外加金币的条件


select
             
    uid,
    date_format(dt,'%Y%m') month,
    sum(coin) coin
from (
    select
        uid,
        dt,
        case when mod(rank()over(partition by uid,rank_day order by dt), 7)=0 then 7
             when mod(rank()over(partition by uid,rank_day order by dt), 7)=3 then 3
             else 1
             end  coin 
             --   按照rank_day排序的排名，与7求mod，
    from(
        select
            uid,
            dt,
             date_sub(dt,interval ranK()over(partition by uid order by dt) day ) rank_day--  签到日期减去签到排名，（如果是连续签到，则得到的日期相同），
        from (
            select
                distinct uid,  -- 去重 过滤掉重复签到的情况
                date(in_time) dt-- 登入日期 
            from tb_user_log
            where artical_id=0 and sign_in=1
                and date(in_time)  between '2021-07-07' and '2021-10-31'
            ) t1  
          ) t2
     ) t3
group by uid,month
order by  month,uid
```

```sql
with s1 as
(select uid,month,
case when mod_num<=2 then mod_num+15*rou_num
when mod_num >=3 then mod_num+15*rou_num+2 end coin
from (
    select uid,date_format(min_dt,'%Y%m') month,
    mod(cnt,7) mod_num,round(cnt/7,0) rou_num
    from(
    select uid,min(dt) min_dt,max(dt) max_dt,count(*) cnt
        from(
            select uid, dt, 
            datediff(dt,'1970-01-01')-rank()over(partition by uid order by dt) infor
            from(
                select uid,date(in_time) dt
                from tb_user_log
                where artical_id=0 and sign_in=1 
                and date(in_time)>='2021-07-07' and date(in_time)<='2021-10-31'
                order by dt
                )t1
            )t2
        group by uid,infor
        )t3
    )t4
)
select uid,month,sum(coin) coin
from s1
group by uid,month;
```

```sql
with act_log as (
      select uid, date(in_time) dt FROM tb_user_log 
      where artical_id=0 and sign_in=1 and date(in_time) between '2021-07-07' and '2021-10-31' 
      group by uid, date(in_time)
      ),
      tb_if_con as (
      select * , ROW_NUMBER() over w, datediff(dt,lag(dt,1,dt) over w) if_con from act_log 
      window w as (partition by uid order by dt)
      ),
      con_num as (
      SELECT * , 
      case if_con when 1 then @n:=@n+1 else @n:=1 end 
      mod 7 as con_num, 
      case case if_con when 1 then @m:=@m+1 else @m:=1 end 
      mod 7 
      when 3 then 3 
      when 0 then 7 
      else 1 end 
      as bin_num  
      FROM tb_if_con
      )
SELECT uid, DATE_FORMAT(dt,'%Y%m')as month ,sum(bin_num)coin FROM con_num GROUP BY uid,DATE_FORMAT(dt,'%Y%m') ORDER BY month, uid
```

## SQL13 计算商城中2021年每月的GMV

https://www.nowcoder.com/practice/5005cbf5308249eda1fbf666311753bf?tpId=268&tqId=2285515&ru=/practice/dbbc9b03794a48f6b34f1131b1a903eb&qru=/ta/sql-factory-interview/question-ranking

```sql
select date_format(event_time,'%Y-%m') as month,round(sum(total_amount),0) as GMV
from tb_order_overall
where event_time like '2021%'
and status <> 2
group by date_format(event_time,'%Y-%m')
having round(sum(total_amount),0)>100000
order by GMV asc;


```

```sql
-- 2021年12月11日

-- GMV为已付款订单和未付款订单两者之和。结果按GMV升序排序。

select 
  date_format(event_time,'%Y-%m') as month
, sum(total_amount) as GMV
from tb_order_overall 
where `status` in (0,1) and year(event_time)=2021
group by date_format(event_time,'%Y-%m')
having sum(total_amount) > 100000
order by GMV 
;
```

```sql
SELECT
    DATE_FORMAT(event_time,'%Y-%m') as month,
    round(sum(total_amount),0) as GMV
FROM (SELECT *
    from tb_order_overall
     where total_amount > 0) as result
where YEAR(event_time)=2021
group by month
HAVING GMV >100000
order by GMV
```

```sql
SELECT DATE_FORMAT(event_time,'%Y-%m') month,sum(total_amount) GMV
FROM tb_order_overall
WHERE status != 2 AND year(event_time)=2021
GROUP BY month 
HAVING GMV >= 100000
ORDER BY GMV;
```

```sql
SELECT
    LEFT(event_time,7) AS month,
    ROUND(SUM(total_amount),0) AS GMV
FROM tb_order_overall
WHERE status IN (0,1) AND YEAR(event_time)=2021
GROUP BY MONTH
HAVING GMV>100000
ORDER BY GMV
```

```sql
with tmp as (
    select DATE_FORMAT(event_time,"%Y-%m") month,sum(total_amount)gmv
    from tb_order_overall
    where status != 2 and DATE_FORMAT(event_time,"%Y-%m") >= '2021-01'
    group by month
    having sum(total_amount) > 100000
    order by gmv
)
select * from tmp
```

## SQL14 统计2021年10月每个退货率不大于0.5的商品各项指标

https://www.nowcoder.com/practice/cbf582d28b794722becfc680847327be?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
select product_id, round(click_cnt/show_cnt, 3) as ctr,
    round(IF(click_cnt>0, cart_cnt/click_cnt, 0), 3) as cart_rate,
    round(IF(cart_cnt>0, payment_cnt/cart_cnt, 0), 3) as payment_rate,
    round(IF(payment_cnt>0, refund_cnt/payment_cnt, 0), 3) as refund_rate
from (
    select product_id, COUNT(1) as show_cnt,
        sum(if_click) as click_cnt,
        sum(if_cart) as cart_cnt,
        sum(if_payment) as payment_cnt,
        sum(if_refund) as refund_cnt
    from tb_user_event
    where DATE_FORMAT(event_time, '%Y%m') = '202110'
    group by product_id
) as t_product_index_cnt
where payment_cnt = 0 or refund_cnt/payment_cnt <= 0.5
order by product_id;
```

```sql






-- ------------------------------
select product_id,
round(sum(if_click)/count(id),3) ctr,

round( case sum(if_click) when
0 then 0 else sum(if_cart)/sum(if_click) end ,3) cart_rate,
 
round( case sum(if_cart) when 
0 then 0 else sum(if_payment)/sum(if_cart) end ,3) payment_rate,

round( case sum(if_payment) when 
0 then 0 else sum(if_refund)/sum(if_payment) end ,3) refund_rate

from tb_user_event
where event_time like'2021-10%'
group by product_id having refund_rate<=0.5
order by product_id
```

```sql
select
product_id,
round(
sum(if_click)/count(id),3),
round(
sum(if_cart)/sum(if_click),3),
round(
sum(if_payment)/sum(if_cart),3),
round(
sum(if_refund)/sum(if_payment),3)
from tb_user_event
WHERE
left(date(event_time),7)='2021-10'
group by product_id
order by product_id
```

```sql
###请统计2021年10月每个有展示记录的退货率不大于0.5的商品各项指标
#窗口函数-product_id,计算各个指标
#prodct_id---ctr,cart_rate,payment_rate,refund_rate
SELECT product_id,round(click/showtimes,3) as ctr,
       if(click=0,0,round(cart/click,3)),
       if(cart=0,0,round(payment/cart,3)),
       if(payment=0,0,round(refund/payment,3)) as refund_rate
FROM
(select product_id,
       count(*) as showtimes,
       sum(if_click) as click,
       sum(if_cart) as cart,sum(if_payment) as payment,
       sum(if_refund) as refund 
FROM tb_user_event
where date_format(EVENT_time,"%Y-%m")="2021-10"
group by product_id) BASE
group by product_id
having refund_rate<=0.5
order by product_id

```

```sql
select product_id,
    round(sum(if_click) / count(1),3) as ctr,
    round(sum(if_cart) / sum(if_click),3) as cart_rate,
    round(sum(if_payment) / sum(if_cart),3) as payment_rate,
    round(sum(if_refund) / sum(if_payment),3) as refund_rate
from tb_user_event
where DATE_FORMAT(event_time,'%Y-%m')='2021-10'
GROUP BY product_id
HAVING refund_rate <= 0.5
ORDER BY product_id
```

```sql
SELECT product_id,
ROUND(SUM(IF(if_click=1,1,0))/COUNT(uid),3) AS ctr,
ROUND(SUM(IF(if_cart=1,1,0))/SUM(IF(if_click=1,1,0)),3) AS cart_rate,
ROUND(SUM(IF(if_payment=1,1,0))/SUM(IF(if_cart=1,1,0)),3) AS payment_rate,
ROUND(SUM(IF(if_refund=1,1,0))/SUM(IF(if_payment=1,1,0)),3) AS refund_rate
FROM tb_user_event
WHERE DATE_FORMAT(event_time,'%Y-%m')='2021-10'
GROUP BY product_id
HAVING refund_rate<=0.5
ORDER BY   product_id
```

```sql
select product_id,if(count(product_id)=0,0,round(sum(if_click)/count(product_id),3)) as ctr,
if(sum(if_click)=0,0,round(sum(if_cart)/sum(if_click),3)) as cart_rate,
if(sum(if_cart)=0,0,round(sum(if_payment)/sum(if_cart),3)) as payment_rate,
if(sum(if_payment)=0,0,round(sum(if_refund)/sum(if_payment),3)) as refund_rate
from 
tb_user_event
where year(event_time)=2021 and month(event_time)=10
group by product_id
having if(sum(if_payment)=0,0,round(sum(if_refund)/sum(if_payment),3)) <=0.5
order by product_id asc
```

## SQL15 某店铺的各商品毛利率及店铺整体毛利率

https://www.nowcoder.com/practice/65de67f666414c0e8f9a34c08d4a8ba6?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
replace(profit_rate,'%','') > 24.9
TRIM   (TRAILING '%' FROM profit_rate)>24.9
(1-in_sum/sale_sum) > 0.249
```

```sql
TRIM(BOTH 'O' FROM 'OOHELLO') 
TRIM(LEADING 'O' FROM 'OOHELLO')
TRIM(TRAILING 'O' FROM 'OOHELLO') 
```

难点在于要求'店铺汇总'

可以用 

group by a.product_id with rollup + ifnull(product_id,'店铺汇总')

```sql
select ifnull(product_id,'店铺汇总') product_id,concat(profit_rate,'%') profit_rate from
(
    select a.product_id,round((1-sum(in_price*cnt)/sum(price*cnt))*100,1) profit_rate
    from tb_order_detail as a left join tb_order_overall as b on a.order_id=b.order_id
    left join tb_product_info as c on c.product_id=a.product_id
    where c.shop_id=901 and status=1
    and date_format(event_time,'%Y%m')>='202110'
    group by a.product_id with rollup
    having profit_rate>24.9 or a.product_id is null) as d
order by profit_rate
```

或者

union all

```sql
(select "店铺汇总" as "product_id",concat(round((1-sum(in_price*cnt)/sum(price*cnt))*100,1),"%") as "profit_rate"
from 
tb_order_detail c left join tb_order_overall d on c.order_id=d.order_id
                  LEFT JOIN tb_product_info e on c.product_id=e.product_id
where event_time>"2021-10-01" and status=1 and shop_id=901)
union all
(select a.product_id as "product_id",concat(round((1-avg(in_price)/avg(price))*100,1),"%") as "profit_rate"
from 
tb_order_detail a left join tb_product_info b on a.product_id=b.product_id
where  shop_id="901" 
and order_id in (select order_id from tb_order_overall where status=1 and
                 event_time>"2021-10-01")
group by a.product_id having 1-avg(in_price)/avg(price)>0.249
order by a.product_id)
```

UNION ALL

```sql
with one as(
SELECT
a.product_id,
1-avg(in_price) / avg(price) as rate
from tb_product_info  as a , tb_order_detail  as c,tb_order_overall as b
where  a.product_id=c.product_id and c.order_id=b.order_id  
and DATE_FORMAT(event_time,"%Y-%m")>="2021-10"
and a.shop_id = 901 and b.status=1
group by a.product_id
having rate>0.249
order by product_id),

two as(
select
'店铺汇总' product_id,
1-sum(a.in_price*c.cnt)/ sum(c.price*c.cnt) as rate
from tb_product_info as a,tb_order_detail as c ,tb_order_overall as b
where a.product_id=c.product_id and c.order_id=b.order_id
and a.shop_id = 901 and b.status=1
and DATE_FORMAT(event_time,"%Y-%m")>="2021-10"),

result as(
SELECT * from two
UNION ALL
SELECT * from one)
SELECT
product_id,
CONCAT(round(rate*100,1),'%')
from result


# select '店铺汇总' product_id, concat(round((1-sum(in_price*cnt)/sum(price*cnt))*100,1),'%') profit_rate
# from tb_order_overall right join tb_order_detail using(order_id) left join tb_product_info using(product_id)
# where shop_id = '901' and year(event_time)=2021 and month(event_time)>=10
 
# union all
# select product_id, concat(round((1-avg(in_price)*sum(cnt)/sum(price*cnt))*100,1),'%') profit_rate
# from tb_order_overall right join tb_order_detail using(order_id) left join tb_product_info using(product_id)
# where shop_id = '901' and year(event_time)=2021 and month(event_time)>=10
# group by product_id
# having (1-avg(in_price)/avg(price))*100>24.9

```

GROUP by t1.product_id  with rollup + ifnull(t1.product_id,'店铺汇总')

```sql
select * from (select ifnull(t1.product_id,'店铺汇总') product_id,
concat(ROUND((1-sum(t2.in_price*t1.cnt)/sum(t1.price*t1.cnt))*100,1),'%') profit_rate 
 FROM tb_order_detail t1
left join (SELECT * FROM tb_product_info WHERE shop_id=901) t2 USING (product_id)
where t1.order_id in (SELECT order_id FROM tb_order_overall 
                  where date_format(event_time,'%y-%m')>='21-10' and status=1) and t2.in_price is not null 
GROUP by t1.product_id  with rollup 
ORDER BY field(product_id,'店铺汇总'),product_id)t where product_id='店铺汇总' or replace(profit_rate,'%','') >24.9
```

union

```sql
select '店铺汇总' as product_id,
        concat(round((1-sum(cnt*in_price)/ sum(cnt*price))*100,1),'%') profit_rate
from tb_order_detail t3
left join tb_product_info t1 on t3.product_id=t1.product_id
left join tb_order_overall t2 on t3.order_id=t2.order_id
where date_format(event_time,'%Y-%m')>='2021-10' and shop_id='901'
group by shop_id
union
SELECT t3.product_id,
    CONCAT(round((1-avg(in_price)/avg(price))*100,1),'%') profit_rate
FROM tb_order_detail t3
LEFT JOIN tb_product_info t1 ON  t3.product_id=t1.product_id
LEFT JOIN tb_order_overall t2 ON t3.order_id=t2.order_id
WHERE DATE_FORMAT(event_time,'%Y-%m')>='2021-10' AND shop_id='901'
GROUP BY product_id
HAVING TRIM(TRAILING '%' FROM profit_rate)>24.9;
```

union

```sql
#select '店铺汇总' product_id,
#concat(round((1-sum(pi.in_price*od.cnt)/sum(od.price*od.cnt))*100,1),'%') pro_rate
#from tb_order_overall oo
#left join tb_order_detail od
#on oo.order_id=od.order_id
#left join tb_product_info pi
#on od.product_id=pi.product_id
#where date_format(oo.event_time,'%Y-%m')='2021-10' and pi.shop_id='901'
#union all
#select pi.product_id,
#concat(round((1-pi.in_price/avg(od.price))*100,1),'%') pro_rate
#from tb_order_overall oo
#left join tb_order_detail od
#on oo.order_id=od.order_id
#left join tb_product_info pi
#on od.product_id=pi.product_id
#where date_format(oo.event_time,'%Y-%m')='2021-10' and pi.shop_id='901' and oo.status=1
#group by pi.product_id
#having pro_rate>24.9;
with t as(
select 
od.product_id,sum(price*cnt) sale_sum,sum(in_price*cnt) in_sum,
sum(cnt) sum_cnt
from tb_order_detail od
left join tb_order_overall oo
on od.order_id=oo.order_id
left join tb_product_info pi
on od.product_id=pi.product_id
where shop_id='901' and date_format(event_time,'%Y%m')>='202110' and status!=0
group by od.product_id
)
select '店铺汇总',
concat(round((1-sum(in_sum)/sum(sale_sum))*100,1),'%') profit_rate
from t
union
(
select product_id,
concat(round((1-in_sum/sale_sum)*100,1),'%') profit_rate
from t
where (1-in_sum/sale_sum)>0.249
order by product_id);
```



UNION

```sql
select '店铺汇总' as product_id,
concat(round((1-sum(in_price*cnt)/sum(price*cnt))*100,1),'%') profit_rate 
from tb_order_detail detail 
left join tb_order_overall overall on  overall.order_id=detail.order_id
left join tb_product_info info on info.product_id=detail.product_id
where info.shop_id='901' 
and date(overall.event_time)>=('2021,10,1') 
and overall.status=1
group by shop_id
UNION
select detail.product_id,
# concat(round((1-in_price/((sum(price*cnt))/cnt))*100,1),'%') profit_rate
    CONCAT(round((1-avg(in_price)/avg(price))*100,1),'%') profit_rate
from tb_order_detail detail 
left join tb_order_overall overall on  overall.order_id=detail.order_id
left join tb_product_info info on info.product_id=detail.product_id
where info.shop_id='901' 
and date(overall.event_time)>=('2021,10,1') 
and overall.status=1
GROUP by detail.product_id 
having TRIM(TRAILING '%' FROM profit_rate)>24.9
```

union all

```sql
(
select '店铺汇总' as product_id,
concat(round((1 - (sum(in_price * cnt) / sum(price * cnt)))*100 , 1) ,'%') as profit_rate
from tb_order_detail as od
left join tb_order_overall as oo using(order_id)
left join tb_product_info as pi on pi.product_id = od.product_id
where year(event_time) = 2021 and month(event_time) >= 10
and shop_id = 901
and oo.status in (0,1)
)

# union all前后两段代码需加括号
union all

(
select od.product_id,
# wrong: concat((1-(pi.in_price/avg(od.price)))*100,'%') as profit_rate
concat((round((1-(pi.in_price / (sum(od.price*od.cnt) / sum(od.cnt))))*100 , 1)) , '%') as profit_rate
from tb_order_detail as od
left join tb_order_overall as oo using(order_id)
left join tb_product_info as pi on pi.product_id = od.product_id
where year(event_time) = 2021 and month(event_time) >= 10
and shop_id = 901
and oo.status in (0,1)
group by product_id
# replace函数
having replace(profit_rate,'%','') > 24.9
order by product_id
);
```

## SQL16 零食类商品中复购率top3高的商品

https://www.nowcoder.com/practice/9c175775e7ad4d9da41602d588c5caf3?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

```sql
select product_id,round(sum(if(cnt>=2,1,0))/count(*),3) repurchase_rate from
(
    select a.product_id,b.uid,count(uid) cnt 
    from tb_order_detail as a left join tb_order_overall as b on a.order_id=b.order_id
    left join tb_product_info as c on c.product_id=a.product_id
    where datediff((select max(event_time) from tb_order_overall),event_time)<90 and tag='零食'
    group by a.product_id,b.uid
    ) as d
group by product_id
order by repurchase_rate desc,product_id limit 3

```

```sql
SELECT product_id,
       ROUND(SUM(case when pay_cnt>=2 then 1 else 0 end)/COUNT(uid), 3) rate
FROM (SELECT product_id,
             uid,
             COUNT(1) pay_cnt
      FROM tb_product_info pi
      JOIN tb_order_detail od USING(product_id)
      JOIN tb_order_overall oo USING(order_id)
      WHERE oo.event_time >= DATE_SUB((SELECT max(event_time) FROM tb_order_overall), interval 89 DAY)
            AND tag='零食' AND status=1
      group by product_id, uid) tmp
group by product_id
order by rate desc, product_id
LIMIT 3
```

```sql
# select t1.product_id,
# # count(if(count(uid)>=2,uid,NULL)) / count(distinct uid) as repurchase_rate
# sum(if(t1.a>=2,1,0)) / t1.b as repurchase_rate
# from
# (
# select uid, od.product_id, count(uid) as a, count(distinct uid) as b
# from tb_order_detail as od
# left join tb_order_overall as oo using(order_id)
# left join tb_product_info as pi on pi.product_id = od.product_id
# where oo.status in (0,1)
# and pi.tag = '零食'
# # and date(date_format(oo.event_time,'%Y%m%d')) >= DATE_ADD(dd,-90,max(date_format(oo.event_time,'%Y%m%d')))
# and DATEDIFF((select max(event_time) from tb_order_overall),event_time)<=89
# ) as t1

# group by t1.product_id
# order by repurchase_rate desc
# limit 3;

select product_id,
round(sum(case when n>=2 then 1 else 0 end)/count(distinct uid),3) as repurchase_rate
from
(
    # 表格aa：近90天内，每个用户购买每个零食类商品的次数
    select b.product_id,uid,count(*) n
    from tb_product_info a
    left join tb_order_detail b on a.product_id=b.product_id
    left join tb_order_overall c on b.order_id=c.order_id
    where status in (0,1)
    and DATEDIFF((select max(event_time) from tb_order_overall),event_time)<=89
    and tag='零食'
    group by b.product_id,uid
) as aa
group by product_id
order by repurchase_rate desc,product_id
limit 3;

```

```sql
select product_id,round(count(*)/count(distinct uid)-1,3) as repurchase_rate
from tb_product_info p
join tb_order_detail d using(product_id)
join tb_order_overall o using(order_id)
where tag='零食' and 
date(event_time)>=date_sub(date((select max(event_time) from tb_order_overall)),interval 89 day)
group by product_id
order by repurchase_rate desc,product_id
limit 3;
```

```sql

select product_id,round(sum(repurchase_cnt)/count(product_id),3) as repurchase_rate
from(
select tpi.product_id,uid,if(count(uid)>1,1,0) as repurchase_cnt-- count(distinct uid) as cus_cnt
from tb_product_info tpi
join tb_order_detail tod
using(product_id)
join tb_order_overall too
using(order_id)
where datediff(date((select max(event_time)from tb_order_overall)),date(event_time))<90
    and tag='零食'
group by product_id,uid
    )t1
group by product_id
order by repurchase_rate desc,product_id asc
limit 3
```

## SQL17 10月的新户客单价和获客成本

https://www.nowcoder.com/practice/d15ee0798e884f829ae8bd27e10f0d64?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

用户首单的表示方法：

RANK() OVER(PARTITION BY uid ORDER BY event_time) AS order_rank

WHERE order_rank = 1

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

where (uid,date(event_time)) in (select uid,min(date(event_time)) from tb_order_overall group by uid)

```sql
# select round(avg(avg_a),1) avg_amount,round(avg(avg_in-avg_a),1) as avg_cost from
# (select uid,avg(total_amount) avg_a,sum(cnt*price) avg_in from
# (select uid,event_time,tod.order_id,total_amount,cnt,price,
# dense_rank() over(partition by uid order by event_time) timerank
# from tb_order_detail tod,tb_product_info tpi,tb_order_overall too
# where tod.product_id=tpi.product_id and tod.order_id=too.order_id
# and left(event_time,7) like '2021-10')a
# where timerank=1
# group by uid
# order by uid)b

select round(sum(total_amount)/count(distinct too.order_id),1) avg_amount
,round(sum(price-total_amount)/count(distinct  too.order_id),1) avg_cost
from tb_order_overall too
inner join (
  select order_id,sum(price*cnt) price
  from tb_order_detail
  group by order_id
  ) as t
on too.order_id=t.order_id
where date_format(event_time,'%Y-%m')='2021-10'
and (uid,date(event_time)) in (select uid,min(date(event_time)) from tb_order_overall group by uid)
```

row_number() over(partition by uid order by event_time) rk

where rk = 1

```sql

select round(avg(total_amount),1) avg_amount,round(avg(sum_price-total_amount),1) avg_cost
from
(
        select 
        uid,event_time
        ,total_amount
        ,row_number() over(partition by uid order by event_time) rk
        ,sum(price*cnt) over(partition by order_id) sum_price
        ,status
        from tb_order_detail tod
        join tb_order_overall too
        using(order_id)
        where status = 1 
) t
where rk = 1
and date_format(event_time,'%Y-%m')='2021-10'

```

where DATE(event_time) in (select min(date(event_time)) from tb_order_overall group by uid)

```sql
#将订单总表中符合10月新用户的信息挑出
#select ov.order_id,ov.uid,date(ov.event_time) as dt ,ov.total_amount
#from tb_order_overall as ov
#where date(event_time) like '2021-10%'
#and DATE(event_time) in (select min(date(event_time)) from tb_order_overall group by uid)

#将订单明细表里每个订单的消费金额汇总
#select de.order_id,sum(de.price*de.cnt) as sum_p
#from tb_order_detail as de
#group by de.order_id

select round(sum(total_amount)/count(*),1) as avg_amount
,round(sum((b.sum_p-a.total_amount) )/count(*),1) as avg_cost
from (
select ov.order_id,ov.uid,date(ov.event_time) as dt ,ov.total_amount
from tb_order_overall as ov
where date(event_time) like '2021-10%'
and DATE(event_time) in (select min(date(event_time)) from tb_order_overall group by uid) ) as a
inner join (
select de.order_id,sum(de.price*de.cnt) as sum_p
from tb_order_detail as de
group by de.order_id )as b
on a.order_id=b.order_id;
```

(uid,date(event_time)) in (select uid,min(date(event_time)) from tb_order_overall group by uid)

```sql
# SELECT ROUND(SUM(total_amount) / COUNT(distinct order_id), 1) AS avg_amount,
#        ROUND(SUM(t1.cost- total_amount) / COUNT(distinct order_id)， 1） AS avg_cost
# FROM tb_order_overall too
# INNER JOIN (
#     SELECT order_id, price * cnt cost
#     FROM tb_order_detail
#     GROUP BY order_id) t1 USING (order_id)
# where DATE_FORMAT(too.event_time,'%y-%m') = '21-10' 
#     AND (uid, event_time) IN (SELECT uid, min(event_time) FROM tb_order_oveR

select round(sum(total_amount)/count(distinct too.order_id),1) avg_amount
,round(sum(price-total_amount)/count(distinct  too.order_id),1) avg_cost
from tb_order_overall too
inner join (
  select order_id,sum(price*cnt) price
  from tb_order_detail
  group by order_id
  ) as t
on too.order_id=t.order_id
where date_format(event_time,'%Y-%m')='2021-10'
and (uid,date(event_time)) in (select uid,min(date(event_time)) from tb_order_overall group by uid)
```

## SQL18 店铺901国庆期间的7日动销率和滞销率

https://www.nowcoder.com/practice/e7837f66e8fb4b45b694d24ea61f0dc9?tpId=268&tqId=2286659&ru=/practice/5005cbf5308249eda1fbf666311753bf&qru=/ta/sql-factory-interview/question-ranking

```sql
select dt1,
round(count(distinct if(timestampdiff(day,dt,dt1) between 0 and 6, tb1.product_id,null))/count(distinct if(dt1>=date(release_time),
tb3.product_id,null)),3) sale_rate,
1-round(count(distinct if(timestampdiff(day,dt,dt1) between 0 and 6, tb1.product_id,null))/count(distinct if(dt1>=date(release_time),
tb3.product_id,null)),3) unsale_rate
from (
    select date(event_time) dt1 
    from tb_order_overall 
    having dt1 between '2021-10-01' and '2021-10-03'
    ) tb2,
    (
    select b.product_id,date(event_time) dt 
    from tb_order_overall a 
    left join tb_order_detail b on a.order_id=b.order_id 
    left join tb_product_info c on b.product_id=c.product_id
    where shop_id=901
    ) tb1 
left join tb_product_info tb3 on tb1.product_id=tb3.product_id 
where shop_id=901
group by dt1 

```

```sql
SELECT dt, sale_rate, 1 - sale_rate as unsale_rate
FROM (
    SELECT dt, ROUND(MIN(sale_pid_cnt) / COUNT(all_pid), 3) as sale_rate
    FROM (
        SELECT dt, COUNT(DISTINCT IF(shop_id!=901, NULL, product_id)) as sale_pid_cnt
        FROM (
            SELECT DISTINCT DATE(event_time) as dt
            FROM tb_order_overall
            WHERE DATE(event_time) BETWEEN '2021-10-01' AND '2021-10-03'
            ) as t_dates
            LEFT JOIN (
                    SELECT DISTINCT DATE(event_time) as event_dt, product_id
                    FROM tb_order_overall
                    JOIN tb_order_detail USING(order_id)
                    ) as t_dt_pid 
                ON DATEDIFF(dt,event_dt) BETWEEN 0 AND 6
                LEFT JOIN tb_product_info USING(product_id)
                group by dt
        ) as t_dt_901_pid_cnt
        LEFT JOIN (
            -- 店铺901每个商品上架日期
                SELECT DATE(release_time) as release_dt, product_id as all_pid
                FROM tb_product_info
                WHERE shop_id=901
                ) as t_release_dt 
            ON dt >= release_dt # 当天店铺901已上架在售的商品
            GROUP BY dt
    ) as t_dt_sr
```

```sql
select dt,
round(count(distinct product_id)/(select count(product_id) from tb_product_info where shop_id = 901),3),
round(1-count(distinct product_id)/(select count(product_id) from tb_product_info where shop_id = 901),3)
from
    (select date(event_time) dt from tb_order_overall
    where date(event_time) between '2021-10-01' and '2021-10-03') a
left join
    (select date(c.event_time) fdt,b.product_id product_id
    from tb_order_detail b left join tb_order_overall c using(order_id)
left join tb_product_info d using(product_id)
where d.shop_id = 901) f 
on datediff(a.dt,f.fdt) between 0 and 6
group by dt
order by dt asc;
```

```sql
SELECT dt, sale_rate, (1 - sale_rate) unsale_rate
FROM (SELECT dt, ROUND(MIN(sale_pid_cnt)/COUNT(all_pid), 3) sale_rate
      FROM (SELECT dt, COUNT(DISTINCT IF(shop_id!=901, NULL, product_id)) sale_pid_cnt
            FROM (SELECT DISTINCT DATE(event_time) dt
                  FROM tb_order_overall
                  WHERE DATE(event_time) BETWEEN '2021-10-01' AND '2021-10-03') t_dates
            LEFT JOIN (SELECT DISTINCT DATE(event_time) event_dt, product_id
                       FROM tb_order_overall
                       JOIN tb_order_detail USING(order_id)) t_dt_pid 
            ON DATEDIFF(dt,event_dt) BETWEEN 0 AND 6
        LEFT JOIN tb_product_info USING(product_id)
        GROUP BY dt) t_dt_901_pid_cnt
    LEFT JOIN (
        -- 店铺901每个商品上架日期
        SELECT DATE(release_time) release_dt, product_id all_pid
        FROM tb_product_info
        WHERE shop_id=901) t_release_dt ON dt >= release_dt # 当天店铺901已上架在售的商品
    GROUP BY dt
) as t_dt_sr;

```

```sql
select lt dt,round(sr,3) sr,round((1-sr),3) nsr
from
    (select lt,count(distinct if(DATEDIFF(lt,event_time)<=6 and DATEDIFF(lt,event_time)>=0,t2.product_id,null))/
    count(distinct t2.product_id) sr
from
    (SELECT date_format(event_time,'%Y-%m-%d') lt 
    from tb_product_info a,tb_order_overall b,tb_order_detail c 
    where a.product_id=c.product_id and b.order_id=c.order_id and status=1 and event_time>20211001 and event_time<20211004
    group by date_format(event_time,'%Y-%m-%d')
    having count(date_format(event_time,'%Y-%m-%d'))>=1
    ) a,
    tb_order_overall t1
left join tb_order_detail t2 on t1.order_id=t2.order_id
left join tb_product_info t3 on t2.product_id=t3.product_id
where DATEDIFF(lt,release_time)>=0 and shop_id=901
group by lt) aa
order by dt
```

```sql
select
    lt dt,round(sr,3) sr,round((1-sr),3) nsr
from(
    select 
        lt,
        count(distinct if(DATEDIFF(lt,event_time)<=6 and DATEDIFF(lt,event_time)>=0,t2.product_id,null))
        /count(distinct t2.product_id) sr
    from(
        SELECT date_format(event_time,'%Y-%m-%d') lt
        from tb_product_info a,tb_order_overall b,tb_order_detail c
        where a.product_id=c.product_id
        and b.order_id=c.order_id
        and status=1 and event_time>20211001
        and event_time<20211004
        group by date_format(event_time,'%Y-%m-%d')
        having count(date_format(event_time,'%Y-%m-%d'))>=1
        ) a,
        tb_order_overall t1
    left join tb_order_detail t2
        on t1.order_id=t2.order_id
    left join tb_product_info t3
        on t2.product_id=t3.product_id
    where DATEDIFF(lt,release_time)>=0
        and shop_id=901
    group by lt
    ) aa
order by dt asc;
```

## SQL19 2021年国庆在北京接单3次及以上的司机统计信息

https://www.nowcoder.com/practice/992783fd80f746d49e790d33ee537c19?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0


```sql
select max(city), 
       round(avg(order_num), 3) as avg_order_num,
       round(avg(total_fare), 3) as avg_income
from (select city, driver_id, count(tb_get_car_order.order_id) as order_num, sum(fare) as total_fare
      from tb_get_car_record inner join tb_get_car_order
      on tb_get_car_record.order_id = tb_get_car_order.order_id
      where city = "北京" and date(event_time) between date("2021-10-01") and date("2021-10-07")
      group by city, driver_id
      having count(tb_get_car_order.order_id) >= 3
      ) as t1

```

```sql
select 
    "北京" as city,
    round(avg(b.cnt),3) as avg_order_num,
    round(avg(b.income),3) as asavg_income
from(
    select b.driver_id, count(b.order_id) as cnt, sum(b.fare) as income
    from tb_get_car_record a
    inner join 
        tb_get_car_order b
    on a.order_id=b.order_id
    where a.city="北京" and date(b.order_time) between '2021-10-01' and '2021-10-07'
    group by b.driver_id
    having count(b.order_id)>=3
    ) b
```

```sql
select city,
    round(avg(order_num),3) avg_order_num,
    round(avg(income),3) avg_income
FROM
    (SELECT city,driver_id,
        COUNT(driver_id) order_num,
        SUM(fare) income
    FROM tb_get_car_order o JOIN tb_get_car_record r 
    on o.order_id = r.order_id
    where order_time BETWEEN "2021-10-01" and "2021-10-07" and city = "北京"
    group by driver_id
    HAVING order_num >= 3
    ) b 
GROUP BY city
```

```sql

# select
#     city,
#     ROUND(avg(cnt),3) avg_order_num,
#     ROUND(avg(total),3) avg_income
# from
# (
#     SELECT
#         b.city,
#         a.driver_id,
#         count(a.order_id) cnt,
#         sum(fare) total
#     FROM
#         tb_get_car_record b
#     left join
#         tb_get_car_order a
#     on
#         a.order_id = b.order_id
#     where
#         city = '北京'
#     and
#         date_format(order_time,'%y-%m-%d') BETWEEN '2021-10-01' and '2021-10-07'
#     group by 
#         driver_id
#     HAVING
#         cnt > 2
# ) a


select  t.city,
        round(avg(avg_order_num),3),
        round(avg(avg_income),3)
from 
(
    select driver_id,city,count(o.order_id) as avg_order_num, sum(fare) as avg_income
    from tb_get_car_record as r
    join tb_get_car_order as o
    on r.order_id = o.order_id
    where city = '北京' and order_time between '2021-10-01' and '2021-10-07'
    group by driver_id
    having count(*) > 2
) as t
group by city


```

```sql
select t.city,round(avg(avg_order_num),3),round(avg(avg_income),3)
from (
    select driver_id,city,count(o.order_id) as avg_order_num, sum(fare) as avg_income
    from tb_get_car_record as r
    join tb_get_car_order as o
    on r.order_id = o.order_id
    where city = '北京' and event_time between '2021-10-01' and '2021-10-07'
    group by driver_id
    having count(*) > 2
    ) as t
group by city


```

## SQL20 有取消订单记录的司机平均评分

https://www.nowcoder.com/practice/f022c9ec81044d4bb7e0711ab794531a?tpId=268&tqId=2294893&ru=/practice/e7837f66e8fb4b45b694d24ea61f0dc9&qru=/ta/sql-factory-interview/question-ranking

```sql
select
    coalesce(o.driver_id,'总体') as driver_id,
    round(avg(o.grade),1) as avg_grade
from tb_get_car_order o
where driver_id in(
             select distinct driver_id 
             from tb_get_car_order 
             where date_format(order_time,'%Y%m')=202110 and start_time is null)
group by o.driver_id with rollup


```

```sql
SELECT IFNULL(driver_id, "总体") as driver_id,
    ROUND(AVG(grade), 1) as avg_grade
FROM tb_get_car_order
WHERE driver_id in (
    SELECT driver_id
    FROM tb_get_car_order
    WHERE DATE_FORMAT(order_time, "%Y-%m")='2021-10' AND ISNULL(fare)
    ) 
    AND NOT ISNULL(grade)
GROUP BY driver_id
WITH ROLLUP;
```

```sql
select driver_id,round(avg(grade),1)
from tb_get_car_order
where 
driver_id in(
    select distinct(o.driver_id)
    from tb_get_car_order o 
    where start_time is NULL and finish_time is not null
    and DATE_FORMAT(finish_time,'%Y%m')='202110')
group by driver_id

union ALL

select '总体',round(sum(grade)/count(grade),1)
from tb_get_car_order
where  driver_id in(
    select distinct(o.driver_id)
    from tb_get_car_order o 
    where 
        start_time is NULL and 
        finish_time is not null and 
        DATE_FORMAT(finish_time,'%Y%m')='202110'
    )


```

```sql
select ifnull(driver_id,'总体') driver_id,
       round(avg(grade),1) avg_grade
from tb_get_car_order 
where 
    start_time is not null and 
    driver_id in (select driver_id
                  from tb_get_car_order b
                  where start_time is null )
group by driver_id with ROLLUP
```

```sql
with driverid as(
        select driver_id
        from tb_get_car_order
        where month(finish_time)=10 and start_time is null
        )

select 
    coalesce(tb.driver_id, '总体') as driver_id, 
    round(avg(grade),1) as avg_grade
from tb_get_car_order tb 
inner join 
    driverid
    on tb.driver_id=driverid.driver_id
where grade is not null
group by tb.driver_id with rollup
 
```

```sql
(SELECT driver_id,round(avg(grade),1) as avg_grade FROM tb_get_car_order
where driver_id in
    (SELECT driver_id FROM tb_get_car_order
    where start_time is null and grade is NULL)
group by driver_id 
order by driver_id)

union 

(SELECT '总体' as driver_id,round(avg(grade),1) as avg_grade FROM tb_get_car_order
where driver_id in
    (SELECT driver_id FROM tb_get_car_order
    where start_time is null and grade is NULL));
```

## SQL21 每个城市中评分最高的司机信息

https://www.nowcoder.com/practice/dcc4adafd0fe41b5b2fc03ad6a4ac686?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

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

```sql
select city,driver_id,avg_grade,avg_order_num,avg_mileage from
    (select r.city,o.driver_id,
    round(avg(o.grade),1) avg_grade,
    round(count(o.order_id)/(count(distinct date(order_time))),1) avg_order_num,
    round(sum(o.mileage)/(count(distinct date(order_time))),3) avg_mileage,
    dense_rank() over (partition by r.city order by round(avg(o.grade),1) desc) t_rank
    from tb_get_car_order o 
    inner join tb_get_car_record r on r.order_id=o.order_id
    group by r.city,o.driver_id
    ) as a
where t_rank = 1
order by avg_order_num
```

```sql
SELECT city, driver_id, avg_grade, avg_order_num, avg_mileage
FROM (
    SELECT city, driver_id, ROUND(avg_grade, 1) as avg_grade,
        ROUND(order_num / work_days, 1) as avg_order_num,
        ROUND(toal_mileage / work_days, 3) as avg_mileage,
        RANK() over(PARTITION BY city ORDER BY avg_grade DESC) as rk
    FROM (
        SELECT driver_id, city, AVG(grade) as avg_grade,
            COUNT(DISTINCT DATE(order_time)) as work_days,
            COUNT(order_time) as order_num,
            SUM(mileage) as toal_mileage
        FROM tb_get_car_record
        JOIN tb_get_car_order USING(order_id)
        GROUP BY driver_id, city
    ) as t_driver_info
) as t_driver_rk
WHERE rk = 1
ORDER BY avg_order_num;


```

```sql
select 
    t.city,
    t.driver_id,
    t.avg_grade,
    t.avg_order_num,
    t.avg_mileage
from
    (select 
    r.city as city,
    o.driver_id as driver_id,
    round(avg(o.grade) ,1) as avg_grade,
    round(count(*)/count(distinct date(finish_time)),1)as avg_order_num,
    round(sum(o.mileage)/count(distinct date(finish_time)),3)as avg_mileage,
    rank() over (partition by r.city order by round(avg(o.grade) ,1) desc) as ranking
    from tb_get_car_order o
    join tb_get_car_record r using(order_id)
    group by r.city, o.driver_id
    ) as t
where t.ranking=1
order by t.avg_order_num
```

```sql
select 
    city,
    driver_id,
    avg_grade,
    avg_order_num,
    avg_mileage
 from
    (select 
        *,
        dense_rank()over(partition by city order by avg_grade desc) rank_no
    from 
        (select 
            city,
            driver_id,
            round(avg(grade),1) avg_grade,
            round(count(*)/count(distinct date(order_time)),1) avg_order_num,
            round(sum(mileage)/count(distinct date(order_time)),3) avg_mileage
        from 
            tb_get_car_order a 
        left join 
            tb_get_car_record b 
        on 
            a.order_id = b.order_id 
        group by 
            city,
            driver_id) a ) b
where 
    rank_no = 1
order by 
    avg_order_num
```

## SQL22 国庆期间近7日日均取消订单量

https://www.nowcoder.com/practice/2b330aa6cc994ec2a988704a078a0703?tpId=268&tqId=2299819&ru=/practice/f022c9ec81044d4bb7e0711ab794531a&qru=/ta/sql-factory-interview/question-ranking

```sql
select dt, finish_num_7d, cancel_num_7d
from (
    select 
        DATE_FORMAT(order_time,'%Y-%m-%d') dt,
        round(avg(count(start_time)) over (order by DATE_FORMAT(order_time, '%Y-%m-%d') rows 6 preceding), 2) finish_num_7d,
        round(avg(sum(case when start_time is null then 1 else 0 end)) over (order by date_format(order_time, '%Y-%m-%d') rows 6 preceding), 2) cancel_num_7d
    from tb_get_car_order
    group by dt
    ) as a
where dt between '2021-10-01' and '2021-10-03'
order by dt
```


```sql
select *
from(select 
        dt,
        round(sum(finish_num)over(order by dt rows 6 preceding)/7,2) as finish_num_7d,
        round(sum(cancel_num)over(order by dt rows 6 preceding)/7,2) as cancel_num_7d
    from(
        select 
            date(order_time) dt,
            sum(case when start_time is not null then 1 else 0 end) as finish_num,
            sum(case when start_time is null then 1 else 0 end) as cancel_num
        from tb_get_car_order
        group by date(order_time)
        order by dt) t 
    ) a 
where dt between '2021-10-01' and '2021-10-03'

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

```sql
#select dt,
#finish_num_7d,
#cancel_num_7d
#from (select dt,
#             round(sum(finish_num)over (order by dt rows 6 proceding)/7,2) as finish_num_7d,
#             round(sum(cancel_num)over (order by dt rows 6 proceding)/7,2) as cancel_num_7d
#      from   
#     )
#order by dt asc

select * from 
    (select 
        date_format(order_time,'%Y-%m-%d') dt,
        round(avg(count(start_time)) over (order by date_format(order_time,'%Y-%m-%d')  rows 6 preceding),2) finish_num_7d,
        round(avg(sum(case when start_time is null then 1 else 0 end)) over (order by date_format(order_time,'%Y-%m-%d') rows 6 preceding),2) cancel_num_7d
    from tb_get_car_order
    group by dt
    ) as a 
where dt between '2021-10-01' and '2021-10-03'
order by dt
```

```sql
select * from (
    select
        dates,
        round(avg(finish_cnt) over (order by dates rows between 6 preceding and current row),2),
        round(avg(cancel_cnt) over (order by dates rows between 6 preceding and current row),2)
    from(
        select
            date(order_time) dates,
            count(start_time) finish_cnt,
            sum(if(start_time is null,1,0)) cancel_cnt
        from tb_get_car_order
        group by date(order_time)
        )a
    )b
where dates in ('2021-10-01','2021-10-02','2021-10-03')

```

## SQL23 工作日各时段叫车量、等待接单时间和调度时间

https://www.nowcoder.com/practice/34f88f6d6dc549f6bc732eb2128aa338?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0


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

```sql


select
    case
        when subString_index(event_time, ' ', -1) between '07:00:00'
        and '08:59:59' then '早高峰'
        when subString_index(event_time, ' ', -1) between '09:00:00'
        and '16:59:59' then '工作时间'
        when subString_index(event_time, ' ', -1) between '17:00:00'
        and '19:59:59' then '晚高峰'
        else '休息时间'
    end period,
    count(1) AS get_car_num,
    round(
        avg(TIMESTAMPdiff(second, event_time, order_time))/60,
        1
        ) AS avg_wait_time,
    round(
        avg(Timestampdiff(second, order_time, start_time))/60,
        1
        ) AS avg_dispatch_time
from
    tb_get_car_record a
    JOIN tb_get_car_order b ON a.order_id = b.order_id
WHERE
     WEEKDAY(order_time) NOT IN(5, 6)
GROUP BY period
ORDER BY get_car_num
```

```sql
select
    period, count(period)as get_car_num,
    round(avg(wait_time),1)as avg_wait_time,
    round(avg(dispatch_time),1)as avg_dispatch_time
from
    (SELECT 
        (case when date_format(event_time,'%T')>='07:00:00'and date_format(event_time,'%T')<'09:00:00' then'早高峰'
            when date_format(event_time,'%T')>='09:00:00'and date_format(event_time,'%T')<'17:00:00' then'工作时间'
            when date_format(event_time,'%T')>='17:00:00'and date_format(event_time,'%T')<'20:00:00' then'晚高峰'
            else '休息时间' end)as period,
        round(TIMESTAMPDIFF(second,event_time,order_time)/60,1)as wait_time,
        round(TIMESTAMPDIFF(second,order_time,start_time)/60,1)as dispatch_time
    FROM tb_get_car_order o left join tb_get_car_record r on o.order_id=r.order_id
    where (WEEKDAY(order_time) between 0 and 4) and event_time is not null
    )t
GROUP by period
order by get_car_num
```

```sql


SELECT period, COUNT(1) as get_car_num,
    ROUND(AVG(wait_time/60), 1) as avg_wait_time,
    ROUND(AVG(dispatch_time/60), 1) as avg_dispatch_time
FROM (
    SELECT event_time,
        CASE
            WHEN HOUR(event_time) IN (7, 8) THEN '早高峰'
            WHEN HOUR(event_time) BETWEEN 9 AND 16 THEN '工作时间'
            WHEN HOUR(event_time) IN (17, 18, 19) THEN '晚高峰'
            ELSE '休息时间'
        END as period,
        TIMESTAMPDIFF(SECOND, event_time, end_time) as wait_time,
        TIMESTAMPDIFF(SECOND, order_time, start_time) as dispatch_time
    FROM tb_get_car_record
    JOIN tb_get_car_order USING(order_id)
    WHERE DAYOFWEEK(event_time) BETWEEN 2 AND 6
    ) as t_wait_dispatch_time
GROUP BY period
ORDER BY get_car_num;


```

```sql
select 
    (case when hour(event_time) >= 7 and hour(event_time) < 9  then '早高峰'
         when hour(event_time) >= 9 and hour(event_time) < 17  then '工作时间'
         when hour(event_time) >= 17 and hour(event_time) < 20  then '晚高峰'
         else '休息时间' end) period,
    count(b.event_time) get_car_num,
    round(avg(timestampdiff(second,event_time,order_time))/60,1) avg_wait_time, 
    round(avg(timestampdiff(second,order_time,start_time))/60,1) avg_dispatch_time
from 
    tb_get_car_order a
left join 
    tb_get_car_record b 
on 
    a.order_id = b.order_id
where 
    WEEKDAY(event_time) not in (5,6)
group by 
    period
order by 
    get_car_num
```

## SQL24 各城市最大同时等车人数


https://www.nowcoder.com/practice/f301eccab83c42ab8dab80f28a1eef98?tpId=268&tags=&title=&difficulty=0&judgeStatus=0&rp=0

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

```sql
select city,left(max(sumd),1) mdd
from (
    select city,date(a.dt) dtt,
    sum(diff) over (partition by city,date(a.dt) order by a.dt,diff desc) sumd
    from (
        (select city,event_time dt,'1' as diff
        from tb_get_car_record
        where date_format(event_time,'%Y-%m') = '2021-10')

        union all

        (select city,start_time dt,'-1' as diff
        from tb_get_car_record t1 left join tb_get_car_order t2 on t1.order_id = t2.order_id
        where date_format(start_time,'%Y-%m') = '2021-10')
        ) a
    ) b
group by city,b.dtt
order by mdd,city
```

```sql
select 
    city, 
    max(wait_uv)
from(
    select 
        city, t, sum(num) over(partition by city, date(t) order by t asc, num desc) wait_uv
    from
        (
        select city, event_time as t, 1 num
        from tb_get_car_record
        UNION all
        select city, COALESCE(start_time, finish_time) t, -1 num
        from tb_get_car_order
        left join tb_get_car_record using(order_id)
        ) a
    where LEFT(t, 7) = '2021-10'
    order by 2
    ) b
group by 1
order by 2, 1
```

```sql
with tmp as 
    (select city, event_time,
            case 
            when r.order_id is null then end_time
            when start_time is null then finish_time
            when start_time is not null then start_time 
            end out_time
     from tb_get_car_record r left join tb_get_car_order o using(order_id)
     where date_format(event_time,'%Y%m')='202110')

select city, max(uv_num) max_wait_uv
from 
    (select city, 
            sum(num) over(partition by city,date(dt) order by dt, num desc) uv_num
     from 
        (select city, event_time dt, 1 num from tmp
        union all 
        select city, out_time dt, -1 num from tmp
        )tmp2
    ) tmp3
group by city
order by max_wait_uv, city


```

```sql
/*select city,max(wait_uv) max_wait_uv
from
(select city,
sum(if_wait) over (partition by city order by time1,if_wait desc) wait_uv
from 
(select city,uid,event_time time1,1 if_wait from tb_get_car_record
where date_format(event_time,'%Y-%m')='2021-10'
union all 
select r.city,r.uid,
(case when o.start_time is null then o.finish_time else o.start_time end) time1,-1 if_wait
from tb_get_car_order o left join tb_get_car_record r on r.order_id=o.order_id
where date_format(o.start_time,'%Y-%m')='2021-10'
and date_format(o.finish_time,'%Y-%m')='2021-10') as a) as b
group by city
order by max_wait_uv,city*/


select city,max(wait_uv) max_wait_uv from
    (select 
        city,
        date_format(dt,'%Y-%m-%d') dt1,
        sum(if_wait) over (partition by city,date_format(dt,'%Y-%m-%d') order by dt asc,if_wait desc) wait_uv
    from
        (select city,event_time dt,1 if_wait from tb_get_car_record
        where date_format(event_time,'%Y-%m')='2021-10'

        union all

        select 
            r.city,
            (
            case when o.start_time is null then o.finish_time 
            else o.start_time end
            ) dt,
            -1 if_wait
        from tb_get_car_record r left join tb_get_car_order o on o.order_id=r.order_id
        where date_format(o.finish_time,'%Y-%m')='2021-10'
        ) as a
    ) as b
group by city
order by max_wait_uv,city
```
