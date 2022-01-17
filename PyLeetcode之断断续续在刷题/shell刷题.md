## shell中 $(( ))、$( )、``与${ }的区别

```s
说明：

${ } 属于变量替换的范畴，

只不过在变量替换中可以加上大括号，也可以不加大括号。

简而言之：

$(( )) 属于 执行计算公式，

等价于 $[ ]，$( )属于命令替换，

${ } 属于变量替换
```
 

## SHELL1 统计文件的行数

```sh
line=0
while read p
do
    ((line++))
done < ./nowcoder.txt
echo $line
```

```sh
#! /bin/bash
line = 0
while read val
do
 line = $(($line+1))
 done < nowcoder.txt
 echo $line

```

```sh
#! /bin/bash
line=0
while read val
do
 line = $(($line+1))
 done < nowcoder.txt
 echo $line

```

```sh
#!bin/bash
line=0;
while read n
do
  ((line++))
done < nowcoder.txt
echo $line

```

```sh
line = 0
while read p 
do
    ((line++))
done < ./nowcoder.txt
echo $line
```

## SHELL2 打印文件的最后5行

```sh
tail -5 nowcoder.txt
```

```sh
#! /bin/bash
while read line
do
    row = $((row+1))
done < nowcoder.txt
while read line
do
    if [ $row -lt 6 -a $row -gt 0 ]
    then
        echo $line
    fi
row = $((row-1))
done < nowcoder.txt
```

```sh
row = 0
while read line
do 
    row = $((row+1))
done < nowcoder.txt

while read line
do
    if [ $row -lt 6 -a $row -gt 0 ]
    then
        echo $line
    fi
    row = $((row-1))
done < nowcoder.txt

```

```sh
row=0
while read line
do
    ((row++))
done < ./nowcoder.txt

while read line
do
    if [ $row -le 5 -a $row -ge 1 ]
    then
        echo $line
    fi
        row = $(($row-1))
done < ./nowcoder.txt
```

```sh
#! /bin/bash
while read line
do
    row = $((row+1))
done < nowcoder.txt
while read line
do
    if [ $row -lt 6 -a $row -gt 0 ]
    then
        echo $line
    fi
row = $((row-1))
done<nowcoder.txt
```

## SHELL3 输出7的倍数

```sh
#!/bin/bash
for (( i = 0; i <= 500; i = i + 7))
do echo $i
done
```

```sh
for i in {0..500..7}
do
        echo $i
done
```

```sh
for (( i = 0; i <= 500; i += 7))
do echo $i
done
```

```sh
for i in {0..500..7}
do
    echo ${i}
done
```

```sh
for i in {0..500..7}; do echo $i; done;
```

## SHELL4 输出第5行的内容

```sh
line = 1
while read value
do 
    if [ $line -eq 5 ]
    then echo $value
    fi
    ((line++))
done < nowcoder.txt
```

```sh
line = 1
while read value
do 
    if [ $line -eq 5 ]
    then 
          echo $value;
    fi
        line = $((line+1));
done < nowcoder.txt
```

```sh
a = 0
while read c
do
    a = $((a+1))
    if [ $a -eq 5 ]
        then echo $c
    fi
done < nowcoder.txt
```

```sh
num = 0

while read p
do
    if (( $num == 4))
    then
        echo $p
        exit
    else
        num=$(($num+1))
        
    fi

done < ./nowcoder.txt

```

```sh
row = 0
while read line
do
    ((row++))
    if [ $row -eq 5 ]
    then
        echo $line
    fi
done < ./nowcoder.txt 
```

## SHELL5 打印空行的行号

```sh
row = 0
while read line
do
    ((row++))
    if [ "$line" = "" ];then
        echo $row
    fi
 done < nowcoder.txt
```

```sh
i = 1
while read line;
do
    if [ -z $line ];
    then
        echo $i
    fi
    i = $[i+1]
done < nowcoder.txt
```

```sh
line = 1
while read value
do
    if [ -z $value ]
    then
        echo $line
    fi
    line = $((line + 1))
done < nowcoder.txt
```

```sh
row = 1
while read line
do
  if [ -z $line ]
  then echo $row
  fi
  ((row++))
  done < nowcoder.txt
```

```sh
row = 0
while read line
do
    ((row++))
    #if [ -z $line]; then
    #if [ ！ $line]; then
    if [ "$line" = "" ]; then
        echo $row
    fi
 done < nowcoder.txt
```

## SHELL6 去掉空行

```sh
row = 0
while read line;
do
    
    if [ ! -z $line ];then
       echo $line
    fi
done 
exit
```

```sh
while read line
do
if [ -z $line ]
then
	continue
else
	echo $line
fi
done
```

```sh
while read line
do
    if [ ! -z $line ];then
        echo $line
    fi
done < nowcoder.txt
```

```sh
while read i
do
    if [ $i -z ]
    then 
        continue
    else
        echo $i
    fi
done
```

```sh
while read line
do
    if [ -z $line ]
    then
        continue
    fi
    echo $line
done
# 备注：判断字符串是否为空-z
# -z string 测试指定字符是否为空，空着真，非空为假
# -n string 测试指定字符串是否为不空，空为假 非空为真
```

## SHELL7 打印字母数小于8的单词

`#` 用于统计参数的个数

```sh
read line < nowcoder.txt
for n in $line
do
if [ "${#n}" -lt 8 ] 
then
    echo "${n}"
fi
done
```

```sh
read line < nowcoder.txt
for i in $line;
do
    if [ ${#i} -lt 8 ];
    then
        echo $i
    fi
done 
```

## SHELL8 统计所有进程占用内存大小的和

```sh
sum = 0
while read -a a
do
    sum = $(($sum + ${a[5]}))

done < nowcoder.txt
    echo $sum
```

```sh
sum=0
while read line; 
do
    arr = (${line[@]}) #创建一个数组，记录每一行中的元素
    sum = $(($sum + arr[5]))
done < nowcoder.txt
echo $sum
```

```sh
sum = 0
while read p
do
    arr = ($p)
    ((sum += arr[5]))
done
echo $sum
```

```sh
sum = 0;
while read p
do
    arr = ($p)
    ((sum += arr[5]))
done < nowcoder.txt
echo $sum
```

## SHELL9 统计每个单词出现的个数

[菜鸟教程](https://www.runoob.com/linux/linux-shell-array.html)

```sh
declare -A map # 声明数组变量
# 统计出现的次数
while read line
do
    arr = ($line) # 数组
    for i in ${arr[@]}
    do
        if [ -z ${map[$i]}]; then
            map[$i] = 1
        else
            ((map[$i]++))
        fi
    done
done < nowcoder.txt

mm = ()
for value in ${map[@]}
do
    mm[${#mm[@]}] = ${value}
done

for ((i = 0; i<${#mm[*]}; i++))
do
    for ((j = $i+1; j<${#mm[*]}; j++))
    do
        if [ ${mm[$i]} -gt ${mm[$j]} ]; then
            qq = ${mm[$i]}
            mm[$i] = ${mm[$j]}
            mm[$j] = $qq
        fi
    done
done

for ((k = 0; k < ${#mm[*]}; k++))
do
    for key in ${!map[@]}
    do
        if [ ${map[$key]} -eq ${mm[$k]} ];then
            echo $key ${map[$key]}
        fi
    done
done


```

```sh
declare -A map

while read line
do
    arr = ($line)
    for i in ${arr[@]}
    do
        if [ -z ${map[$i]} ];then
            map[$i]=1
        else
            ((map[$i]++))
        fi
    done
done < nowcoder.txt

mm=()
for value in ${map[@]}
do
    mm[${#mm[@]}] = ${value}
done

for ((i=0;i<${#mm[*]};i++))
do
    for ((j=$i+1;j<${#mm[*]};j++))
    do
        if [ ${mm[$i]} -gt ${mm[$j]} ]; then
            qq = ${mm[$i]}
            mm[$i] = ${mm[$j]}
            mm[$j] =$ qq
        fi
    done
done
# 排序
for ((k=0;k<${#mm[*]};k++))
do
    for key in ${!map[@]}
    do
        if [ ${map[$key]} -eq ${mm[$k]} ];then
            echo $key ${map[$key]}
        fi
    done
done


```

```sh
declare -A map
while read line #记录每个单词出现的次数
do
    for ch in $line;
    do
        map[$ch] =$ ((map[$ch]+1))
    done
done < nowcoder.txt
declare -a arr
# ${!map[@]}获取map的所有索引
for key in ${!map[@]};
do
    arr[${map[$key]}]=$key
done

for index in ${!arr[@]};
do
    echo "${arr[$index]} $index"
done

```


## SHELL10 第二列是否有重复

```sh
declare -A arr
while read line
do
    temparr = ($line)
    arr[${temparr[1]}] = $((${arr[${temparr[1]}]} + 1))
done
for key in ${!arr[@]}
do
    if [[ ${arr[$key]} -gt 1 ]]
    then
        echo ${arr[$key]}" "$key
    fi
done
```

```sh

```

```sh

```

```sh

```

```sh

```

## 

```sh

```

```sh

```

```sh

```

```sh

```

```sh

```

## 

```sh

```

```sh

```

```sh

```

```sh

```

```sh

```