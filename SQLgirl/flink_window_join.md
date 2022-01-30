
##  7. <a name='WindowJoin'></a>Window Join

当前条件中需要`windw start`以及`window end`条件；

Windowing TVF之后进行window join需要是下面三种窗口：

- tumble window

- hop window

- Cumulate window

当前left、 right的窗口需要时相同的，目前不支持一个tumble窗口一个hop窗口等。



```sql
INNER/LEFT/RIGHT/FULL OUTER
SELECT ...
FROM L [LEFT|RIGHT|FULL OUTER] JOIN R -- L and R are relations applied windowing TVF
ON L.window_start = R.window_start AND L.window_end = R.window_end AND ...
```

###  7.1. <a name='SEMI'></a>SEMI

```sql
Flink SQL> SELECT *
           FROM (
               SELECT * FROM TABLE(TUMBLE(TABLE LeftTable, DESCRIPTOR(row_time), INTERVAL '5' MINUTES))
           ) L WHERE L.num IN (
             SELECT num FROM (   
               SELECT * FROM TABLE(TUMBLE(TABLE RightTable, DESCRIPTOR(row_time), INTERVAL '5' MINUTES))
             ) R WHERE L.window_start = R.window_start AND L.window_end = R.window_end);

+------------------+-----+----+------------------+------------------+-------------------------+
|         row_time | num | id |     window_start |       window_end |            window_time  |
+------------------+-----+----+------------------+------------------+-------------------------+
| 2020-04-15 12:03 |   3 | L3 | 2020-04-15 12:00 | 2020-04-15 12:05 | 2020-04-15 12:04:59.999 |
+------------------+-----+----+------------------+------------------+-------------------------+

Flink SQL> SELECT *
           FROM (
               SELECT * FROM TABLE(TUMBLE(TABLE LeftTable, DESCRIPTOR(row_time), INTERVAL '5' MINUTES))
           ) L WHERE EXISTS (
             SELECT * FROM (
               SELECT * FROM TABLE(TUMBLE(TABLE RightTable, DESCRIPTOR(row_time), INTERVAL '5' MINUTES))
             ) R WHERE L.num = R.num AND L.window_start = R.window_start AND L.window_end = R.window_end);

+------------------+-----+----+------------------+------------------+-------------------------+
|         row_time | num | id |     window_start |       window_end |            window_time  |
+------------------+-----+----+------------------+------------------+-------------------------+
| 2020-04-15 12:03 |   3 | L3 | 2020-04-15 12:00 | 2020-04-15 12:05 | 2020-04-15 12:04:59.999 |
+------------------+-----+----+------------------+------------------+-------------------------+
```

###  7.2. <a name='ANTI'></a>ANTI

```sql
Flink SQL> SELECT *
           FROM (
               SELECT * FROM TABLE(TUMBLE(TABLE LeftTable, DESCRIPTOR(row_time), INTERVAL '5' MINUTES))
           ) L WHERE L.num NOT IN (
             SELECT num FROM (   
               SELECT * FROM TABLE(TUMBLE(TABLE RightTable, DESCRIPTOR(row_time), INTERVAL '5' MINUTES))
             ) R WHERE L.window_start = R.window_start AND L.window_end = R.window_end);
+------------------+-----+----+------------------+------------------+-------------------------+
|         row_time | num | id |     window_start |       window_end |            window_time  |
+------------------+-----+----+------------------+------------------+-------------------------+
| 2020-04-15 12:02 |   1 | L1 | 2020-04-15 12:00 | 2020-04-15 12:05 | 2020-04-15 12:04:59.999 |
| 2020-04-15 12:06 |   2 | L2 | 2020-04-15 12:05 | 2020-04-15 12:10 | 2020-04-15 12:09:59.999 |
+------------------+-----+----+------------------+------------------+-------------------------+

Flink SQL> SELECT *
           FROM (
               SELECT * FROM TABLE(TUMBLE(TABLE LeftTable, DESCRIPTOR(row_time), INTERVAL '5' MINUTES))
           ) L WHERE NOT EXISTS (
             SELECT * FROM (
               SELECT * FROM TABLE(TUMBLE(TABLE RightTable, DESCRIPTOR(row_time), INTERVAL '5' MINUTES))
             ) R WHERE L.num = R.num AND L.window_start = R.window_start AND L.window_end = R.window_end);
+------------------+-----+----+------------------+------------------+-------------------------+
|         row_time | num | id |     window_start |       window_end |            window_time  |
+------------------+-----+----+------------------+------------------+-------------------------+
| 2020-04-15 12:02 |   1 | L1 | 2020-04-15 12:00 | 2020-04-15 12:05 | 2020-04-15 12:04:59.999 |
| 2020-04-15 12:06 |   2 | L2 | 2020-04-15 12:05 | 2020-04-15 12:10 | 2020-04-15 12:09:59.999 |
+------------------+-----+----+------------------+------------------+-------------------------+
```


