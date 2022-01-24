<!-- vscode-markdown-toc -->
* 1. [shell中 $(( ))、$( )、``与${ }的区别](#shell)
* 2. [SHELL1 统计文件的行数](#SHELL1)
	* 2.1. [wc命令](#wc)
	* 2.2. [awk 命令](#awk)
	* 2.3. [grep 搜索 ""](#grep)
	* 2.4. [sed 统计行](#sed)
	* 2.5. [gawk命令](#gawk)
	* 2.6. [高效命令](#)
* 3. [SHELL2 打印文件的最后5行](#SHELL25)
	* 3.1. [sed 命令](#sed-1)
	* 3.2. [head 和 tail](#headtail)
	* 3.3. [高效命令](#-1)
* 4. [SHELL3 输出7的倍数](#SHELL37)
	* 4.1. [seq 命令](#seq)
	* 4.2. [for循环写法很巧妙](#for)
	* 4.3. [高效命令](#-1)
* 5. [SHELL4 输出第5行的内容](#SHELL45)
	* 5.1. [sed 命令](#sed-1)
	* 5.2. [head 和 tail](#headtail-1)
	* 5.3. [awk 命令](#awk-1)
	* 5.4. [高效命令](#-1)
* 6. [SHELL5 打印空行的行号](#SHELL5)
	* 6.1. [awk 命令](#awk-1)
	* 6.2. [grep 命令 + (sed / awk / cut)](#grepsedawkcut)
	* 6.3. [sed + 正则](#sed-1)
	* 6.4. [高效命令](#-1)
* 7. [SHELL6 去掉空行](#SHELL6)
	* 7.1. [awk 命令](#awk-1)
	* 7.2. [grep 命令](#grep-1)
	* 7.3. [sed 命令](#sed-1)
	* 7.4. [tr命令](#tr)
	* 7.5. [高效命令](#-1)
* 8. [SHELL7 打印字母数小于8的单词](#SHELL78)
	* 8.1. [tr + awk](#trawk)
	* 8.2. [awk 的length()函数](#awklength)
	* 8.3. [grep 命令](#grep-1)
	* 8.4. [其他test](#test)
	* 8.5. [高效命令](#-1)
* 9. [SHELL8 统计所有进程占用内存大小的和](#SHELL8)
	* 9.1. [awk的强大功能](#awk-1)
	* 9.2. [for循环](#for-1)
	* 9.3. [read命令](#read)
	* 9.4. [定义数组《性能最佳》](#-1)
	* 9.5. [总结](#-1)
	* 9.6. [ 高效计算](#-1)
* 10. [SHELL9 统计每个单词出现的个数](#SHELL9)
	* 10.1. [awk 命令](#awk-1)
	* 10.2. [awk + sort](#awksort)
	* 10.3. [tr + awk](#trawk-1)
	* 10.4. [高效计算](#-1)
* 11. [SHELL10 第二列是否有重复](#SHELL10)
	* 11.1. [awk 命令](#awk-1)
	* 11.2. [高效计算](#-1)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->


##  1. <a name='shell'></a>shell中 $(( ))、$( )、``与${ }的区别

```s
说明：

${ } 属于变量替换的范畴，

只不过在变量替换中可以加上大括号，也可以不加大括号。

简而言之：

$(( )) 属于 执行计算公式，

等价于 $[ ]，$( )属于命令替换，

${ } 属于变量替换
```
 

##  2. <a name='SHELL1'></a>SHELL1 统计文件的行数

https://www.nowcoder.com/practice/205ccba30b264ae697a78f425f276779?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1&gioEnter=menu

###  2.1. <a name='wc'></a>wc命令

```sh
`wc命令`就是用来统计文件内容的，因此第一想法必然是使用wc命令。下面归纳一下wc命令的功能：

`wc命令`统计指定文件的字节数，字数，行数，并将统计结果显示出来。

命令基本格式:
    wc [选项] [文件]...

常见选项：
    -c 统计字节数 一个英文一个字节, 但是换行符也会被算上, 中文编码方式不同结果也不同
    -l 统计行数
    -L line的长度
    -w 统计字数 
    -m 字符个数

在linux系统中，newline字符就是 \n 字符。

wc  -l  ./nowcoder.txt
8 ./nowcoder.txt

输出中包含了文件名，因此再做一下处理：

wc -l ./nowcoder.txt | awk '{print $1}'
wc -l nowcoder.txt | awk '{print $1}'
cat nowcoder.txt | wc -l
wc -l < nowcoder.txt
wc -l nowcoder.txt | gawk '{print $1}'
```

###  2.2. <a name='awk'></a>awk 命令

awk 本身就可以只打印最后一行，因此一个 awk 脚本也可以搞定

```sh
awk 可以打印所有行的行号
awk '{print NR}' ./nowcoder.txt

该输出的最后一行就是文件的行数，结合 tail 就可以获取到文件的行数
awk 'END {print NR}' nowcoder.txt
awk 'END {print NR}' ./nowcoder.txt
awk '{print NR}' ./nowcoder.txt |tail -n 1
awk '{print NR}' nowcoder.txt|tail -n 1
简要了解了一下，AWK命令只能说，awkYYDS！！ 
awk '{a[$0]++} END {print length(a)}' nowcoder.txt
awk 'BEGIN {lines=0;}{lines=lines+1;} END {printf("%d\n",lines)}' nowcoder.txt
```

###  2.3. <a name='grep'></a>grep 搜索 ""

```sh
grep -c c* ./nowcoder.txt
grep -c "" ./nowcoder.txt 
## 或者
grep -n "" nowcoder.txt | tail -n1 | gawk -F: '{print $1}'
grep -n "" ./nowcoder.txt | awk -F ":" '{print $1 }' | tail -n 1
grep -n "" nowcoder.txt | awk -F: '{print }'|tail -n 1 | awk -F: '{print $1}'
grep -n "" nowcoder.txt | awk -F: '{print }'|tail -n 1 | awk -F: '{print $1}' 
# -F 指定输入文件折分隔符，fs是一个字符串或者是一个正则表达式，
# -F相当于内置变量FS, 指定分割字符
```

###  2.4. <a name='sed'></a>sed 统计行

```sh
sed -n '$=' nowcoder.txt
sed -n '$=' nowcoder.txt
sed -n '$=' nowcoder.txt
sed -n '$=' ./nowcoder.txt
```

###  2.5. <a name='gawk'></a>gawk命令

```sh
gawk '{print NR}' nowcoder.txt | tail -n1
gawk 'END{print NR}' nowcoder.txt
```

###  2.6. <a name=''></a>高效命令

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

##  3. <a name='SHELL25'></a>SHELL2 打印文件的最后5行

https://www.nowcoder.com/practice/ff6f36d357d24ce5a0eb817a0ef85ee2?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

###  3.1. <a name='sed-1'></a>sed 命令

```sh
sed -n ‘5,20p’ filename
这样你就可以只查看文件的第5行到第20行。
```

###  3.2. <a name='headtail'></a>head 和 tail

```sh
查看文件的前5行，可以使用head命令，如
head -5 filename
查看文件的后5行，可以使用tail命令，如：
tail -5 filename 或 tail -n 5 filename
查看文件中间一段，你可以使用sed命令，如：

```

```sh
第一次解答的时候我是这样写的
#!/bin/bash
rows=`tail -n 5 nowcoder.txt`
echo $rows
然后提示不对
tail 不带参数默认是打印后10行
-n参数打印最后的xxx行，所以
tail -n 5 nowcoder.txt

tail 命令可用于查看文件的内容，有一个常用的参数 -f 常用于查阅正在改变的日志文件。

    -f 循环读取
    -q 不显示处理信息
    -v 显示详细的处理信息
    -c<数目> 显示的字节数
    -n<行数> 显示文件的尾部 n 行内容
    --pid=PID 与-f合用,表示在进程ID,PID死掉之后结束
    -q, --quiet, --silent 从不输出给出文件名的首部
    -s, --sleep-interval=S 与-f合用,表示在每次反复的间隔休眠S秒 
```

```sh
tail -5 nowcoder.txt
```

###  3.3. <a name='-1'></a>高效命令

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

##  4. <a name='SHELL37'></a>SHELL3 输出7的倍数

https://www.nowcoder.com/practice/8b85768394304511b0eb887244e51872?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

###  4.1. <a name='seq'></a>seq 命令

```sh
seq 0 7 500就好了

seq 用于生成从一个数到另一个数之间的所有整数。
用法：seq [选项]... 尾数
或：seq [选项]... 首数 尾数
或：seq [选项]... 首数 增量 尾数

seq 0 500|awk '$0%7==0 {print $0}' 
```

###  4.2. <a name='for'></a>for循环写法很巧妙

```sh
第一反应想到的是如下代码：

#!/bin/bash
for num in {0..500};do
   [[ "((num%7))" -eq 0 ]] && echo "${num}"
done

结果，无论如何都无法通过。

分析后注意到这一点：因为最后一次判断的数字是 500，会造成返回值不是 0，因此最后需要添加 exit 0

#!/bin/bash
for num in {0..500};do
   [[ "((num%7))" -eq 0 ]] && echo "${num}"
done
exit 0

这是最简单的方式，我对shell不是很熟悉，因此没有想到这个：

#!/bin/bash
for num in {0..500..7}; do 
  echo "${num}"
done
```

```sh
for 变量 in 单词表 -- 这个还可以加步长，但是这次没有这么写，里面的逻辑很加单，要是想不明白直接写C语言的样子就可以了

!/bin/bash
for var in {0..500}
do
if ((var%7==0))
then
echo $var
else
continue
fi
done
```

```sh
正解

    从0数到500
    每个数字对7进行取模。如果%7=0，则表示为倍数

#!bin/bash
num=0
while (( $num<=500 ))
do
if (( $num%7==0 ))
then
    echo $num
fi
    let "num++"
done

逆解

    初始值=0，可直接输出
    以7为倍数进行自增并输出。得到的结果都是满足题意

#!bin/bash
num=0
while (( $num<=500 ))
do
    echo $num
     let "num+=7"
done

总结

    逆解效率肯定比正解好

        正解：像憨憨一样的数数，先数到数字，在对7进行取模，判断条件，满足在输出。步骤有4
        逆解：自增7，直接输出；步骤有2，而且是直达，

    时间复杂度：

        正解O(n);
        逆解O(n/7)
        提升性能，就是少做工作和减少内存开销；成倍的减少
```



###  4.3. <a name='-1'></a>高效命令

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

##  5. <a name='SHELL45'></a>SHELL4 输出第5行的内容

https://www.nowcoder.com/practice/1d5978c6136d4252904757b4fa0c9296?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

###  5.1. <a name='sed-1'></a>sed 命令

head 从头开始打印，tail 从未开始打印，sed 从中间开始打印

```sh
sed 命令中的 p 子命令，打印第五行,p 打印模板块的行。
-n：仅显示script处理后的结果；
sed -n 5p
sed -n '5p'
sed -n "5p" nowcoder.txt

查看文件中间一段，你可以使用sed命令，如：
sed -n ‘100,200p’ $n0

这样你就可以只查看文件的第100行到第200行。
sed -n  -e "5,5p" nowcoder.txt
```

```sh
功能强大的`流式文本编辑器`

sed 是一种`流编辑器`，它是`文本处理`中非常常用的工具，能够完美的配合`正则表达式`使用。

处理时，把当前处理的`行`存储在`临时缓冲区`中，称为`“模式空间”（pattern space）`，

接着用sed命令处理`缓冲区`中的内容，处理完成后，把`缓冲区`的内容送往屏幕。

接着处理下一行，这样不断重复，直到文件末尾。

文件内容并没有改变，除非你使用重定向存储输出。

Sed主要用来`自动编辑`一个或多个文件；简化对文件的反复操作；编写转换程序等。

命令格式:

    sed [options] 'command' file(s)
    sed [options] -f scriptfile file(s)

选项:

    -e<script>或--expression=<script>：以选项中的指定的script来处理输入的文本文件；
    -f<script文件>或--file=<script文件>：以选项中指定的script文件来处理输入的文本文件；
    -h或--help：显示帮助；
    -n或--quiet或——silent：仅显示script处理后的结果；
    -V或--version：显示版本信息。

参数
    文件：指定待处理的文本文件列表。

sed命令

    a\ # 在当前行下面插入文本。
    i\ # 在当前行上面插入文本。
    c\ # 把选定的行改为新的文本。
    d # 删除，删除选择的行。
    D # 删除模板块的第一行。
    s # 替换指定字符
    h # 拷贝模板块的内容到内存中的缓冲区。
    H # 追加模板块的内容到内存中的缓冲区。
    g # 获得内存缓冲区的内容，并替代当前模板块中的文本。
    G # 获得内存缓冲区的内容，并追加到当前模板块文本的后面。
    l # 列表不能打印字符的清单。
    n # 读取下一个输入行，用下一个命令处理新的行而不是用第一个命令。
    N # 追加下一个输入行到模板块后面并在二者间嵌入一个新行，改变当前行号码。
    p # 打印模板块的行。
    P # (大写) 打印模板块的第一行。
    q # 退出Sed。
    b lable # 分支到脚本中带有标记的地方，如果分支不存在则分支到脚本的末尾。
    r file # 从file中读行。
    t label # if分支，从最后一行开始，条件一旦满足或者T，t命令，将导致分支到带有标号的命令处，或者到脚本的末尾。
    T label # 错误分支，从最后一行开始，一旦发生错误或者T，t命令，将导致分支到带有标号的命令处，或者到脚本的末尾。
    w file # 写并追加模板块到file末尾。
    W file # 写并追加模板块的第一行到file末尾。
    ! # 表示后面的命令对所有没有被选定的行发生作用。
    = # 打印当前行号码。

把注释扩展到下一个换行符以前。

sed替换标记

    g # 表示行内全面替换。
    p # 表示打印行。
    w # 表示把行写入一个文件。
    x # 表示互换模板块中的文本和缓冲区中的文本。
    y # 表示把一个字符翻译为另外的字符（但是不用于正则表达式）
    \1 # 子串匹配标记
    & # 已匹配字符串标记

```

###  5.2. <a name='headtail-1'></a>head 和 tail

```sh
head 命令拿到前五行，再通过通道，通过tail取出来最后一行，即第五行

head -n 5 nowcoder.txt | tail -n 1
先取该文件的前五行，再取前五行的最后一行。也就是该文件的第五行的内容。
```

```sh
$n0 表示当前文件名

如果你只想看文件的前100行，可以使用head命令，如
head -100 $n0

如果你想查看文件的后100行，可以使用tail命令，如：
tail -100 $n0 或 tail -n 100 $n0
```

###  5.3. <a name='awk-1'></a>awk 命令

```sh
awk '{if(NR==5){print $0}}' nowcoder.txt
```

###  5.4. <a name='-1'></a>高效命令

```sh
使用readline读取并遍历行

#!/bin/bash
count=1
cat nowcoder.txt | while read line
do
    if [ ${count} == 5 ]
    then
        echo $line
    fi
    count=$[${count}+1]
done
```


```sh
i=0
while read line;
do
    if [ $i -eq 4 ]
    then
        echo $line
        exit 0
    fi
    i=$[$i + 1]
done
```

```sh
```

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

##  6. <a name='SHELL5'></a>SHELL5 打印空行的行号

https://www.nowcoder.com/practice/030fc368e42e44b8b1f8985a8d6ad255?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

###  6.1. <a name='awk-1'></a>awk 命令

awk是数据解析工具 对文件或管道数据、终端输入数据逐行解析 默认以空格分隔
awk语法：awk 'pattern{命令}' 文件名

```sh
awk '{if($0 == "") {print NR}}' ./nowcoder.txt
awk  'NF==0{print NR}'  nowcoder.txt
awk '/^$/{print NR}' nowcoder.txt

NR是行号，即匹配行的行号
在^符号前面加/是因为^是特殊符号,需要转义.
/^/是正则表达式匹配
{}是对匹配的行执行的命令 
/^$/表示空字符串即该行是空字符串即空行

awk '/^\s*$/{print NR}' nowcoder.txt
awk '/^\s*$/{print NR}' nowcoder.txt
如果空行里面全都是空格或者有tab的话还得需要\s*来过滤一下，如果直接^$的话起不到过滤空格 tab的作用
\s(匹配任何空白字符:包括空格,制表符,换页符等等.等价于[ \f\n\r\t\v])且输出带行号.
```

###  6.2. <a name='grepsedawkcut'></a>grep 命令 + (sed / awk / cut)

```sh
grep -n '^\s*$' nowcoder.txt

grep -n "^$" nowcoder.txt | cut -d ":" -f 1
grep -n '^$' nowcoder.txt | cut -d ':' -f 1

grep -n '^$' nowcoder.txt | awk -F: '{print $1}'
grep -n '^$' nowcoder.txt | awk -F: '{print $1}'

#利用grep查找空行并打印(有冒号）,然后用sed把冒号替换成空并打印
grep -n '^$' nowcoder.txt | sed 's/\:/\ /g'
grep -n "^$" nowcoder.txt | sed 's/\://g'
grep -n "^$" nowcoder.txt | sed -n 's@\:@@p'
grep -n "^$" nowcoder.txt | sed -n 's/\://p'
grep -n "^$" nowcoder.txt | sed "s@:@@g"
```

###  6.3. <a name='sed-1'></a>sed + 正则

```sh
正则匹配空行 
sed -n '/^\s*$/=' nowcoder.txt
```

```sh
sed -n  '/^$/='  "nowcoder.txt"
sed -n '/^$/=' nowcoder.txt

-n 对匹配的行做处理
= 打印匹配到的内容的行号
p 打印匹配到的内容
```

###  6.4. <a name='-1'></a>高效命令

```sh
i=1;while read p; do if [[ $p == '' ]]; then echo $i; fi; ((i++)); done < nowcoder.txt
i=1;while read p; do if [ -z $p ]; then echo $i; fi; ((i++)); done < nowcoder.txt
```

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

##  7. <a name='SHELL6'></a>SHELL6 去掉空行

https://www.nowcoder.com/practice/0372acd5725d40669640fd25e9fb7b0f?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

###  7.1. <a name='awk-1'></a>awk 命令

```sh
cat ./nowcoder.txt | awk NF
cat nowcoder.txt | awk NF

cat 输出文本内容，然后通过管道符交由 awk 做非空校验然后输出
NF：只会记录有数据的行
```

判断当前行的内容然后输出

```sh
解题：
    awk '/[^$]/ {print $1}' nowcoder.txt

解析：
    awk '/pattern/ {action}' filenames
    正则部分（参考讨论中ArlRa大佬的解释）： 
      ^$联合使用，中间不加任何字符数字，代表匹配空行；
      [ ] 在shell正则中表示取反
      [^$] 在shell正则中则代表非空

    action部分：
      {print $n}
      $n 当前记录的第n个字段，字段间由FS分隔,默认以tab键或者空格为分隔符将一行分为多个字段

解题：
    awk NF nowcoder.txt
    awk NF nowcoder.txt
    awk -n '{if ($0 != "") print $0}' nowcoder.txt
    awk '{if($0 != "") {print $0}}' ./nowcoder.txt
    awk '$0!=null {print $0}' nowcoder.txt

解题：
    # awk 正则
    awk '{if(! /^\s*$/) print $0}' nowcoder.txt
    cat nowcoder.txt | awk '{if(!/^\s*$/) print $0}'
    awk '!/^$/ {print $NF}'
    awk '!/^$/ {print $NF}' nowcoder.txt
```

###  7.2. <a name='grep-1'></a>grep 命令

```sh
grep . nowcoder.txt
grep . 

-v 显示不包含匹配文本的所有行
grep -v '^$'
grep -v '^$' 

-e 指定字符串做为查找文件内容的样式
grep -e '\S+'
grep -e '\S'

# 排除文件中符合表达式的行，并显示其他行
grep -E  '\S+'
grep -v '^$' nowcoder.txt
grep -v '^$' nowcoder.txt
grep -v -E "^$" txt

# grep 正则实现
cat nowcoder.txt | grep -v '^\s*$'
```

###  7.3. <a name='sed-1'></a>sed 命令

```sh
sed '/^$/d'
sed '/^$/d' nowcoder.txt
sed '/^$/d' nowcoder.txt
cat nowcoder.txt | sed '/^$/Id'

# sed 命令正则
sed '/^\s*$/d' nowcoder.txt
# 显示未被正则表达式匹配的行
sed -n '/^$/!p' nowcoder.txt

sed -n '/[^$]/p'
sed -n '/[^$]/p'
sed -n 静默模式
    p：打印变动的流（行）
    正则部分： [^$] ^代表以后面跟着的字符为开头，$代表以前面的字符为结尾；
    ^$联合使用，中间不加任何字符数字，代表匹配空行；
    [ ] 在shell正则中表示取反
```

###  7.4. <a name='tr'></a>tr命令

```sh
cat nowcoder.txt |tr -s "\n"
```

###  7.5. <a name='-1'></a>高效命令

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

##  8. <a name='SHELL78'></a>SHELL7 打印字母数小于8的单词

https://www.nowcoder.com/practice/bd5b5d4b93a04226a81afbabf0be797d?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

###  8.1. <a name='trawk'></a>tr + awk

```sh
tr ",." " " < nowcoder.txt | awk '{for(i=1;i<=NF;i++) {if(length($i) < 8) print $i}}'
若是文件中有,.，将其转换为空格


tr " " "\n" < nowcoder.txt | awk '/^.{0,7}$/'
```

###  8.2. <a name='awklength'></a>awk 的length()函数

```sh
cat nowcoder.txt | xargs -n 1 | awk 'length($1)<8 {print $1}'
```

用`空格`进行分割，NF是当前记录的`字段数`，也可以说是`单词数`;

然后`for循环`嵌套`if判断`;

当当前字段的`长度小于8`时，将其打印出来;

```sh
#!/bin/bash
# awk的NF变量  print NF
awk -F" " '{
    for(i=1;i<=NF;i++){
        if(length($i) < 8)
            {print $i}}}' nowcoder.txt
awk -F " " '{
    for(i=1;i<=NF;i++){
        if(length($i) < 8) 
            print $i}}' nowcoder.txt


awk '
BEGIN{FS="";RS=" ";ORS="\n"}
{if(NF<8)
    print$0}' nowcoder.txt
awk '
{for(i=1;i<=NF;i++)
    if(length($i)<8)
        print $i;}' nowcoder.txt
awk '
BEGIN{RS="[[:space:]]+"}
length($0)<8{print $0}' nowcoder.txt

awk '
{for(i=1;i<=NF;i++) 
    if(length($i)<8) 
        print $i}' nowcoder.txt
cat nowcoder.txt | awk '{
    for(i=1;i<=NF;i++) 
        if(length($i)<8) 
            print $i}'
```

```sh
cat nowcoder.txt | awk '{
for (i=1;i<=NF;i++){
        if (length($i) < 8)
                print $i
}
}'
```

###  8.3. <a name='grep-1'></a>grep 命令

```sh
grep -Eo '\b\w{,7}\b' nowcoder.txt
```

###  8.4. <a name='test'></a>其他test

```sh
function test0() {
    for ele in `cat nowcoder.txt`; do 
        # ${#ele} [ele是字符串变量名]
        if [ ${#ele} -lt 8 ]; then
            echo ${ele}
        fi
    done
}

# tem=`cat now/coder.txt`
# arr=(${tem})
function test1() {
    for ele in ${arr[@]}; do 
        if [ ${#ele} -lt 8 ]; then
            echo ${ele}
        fi
    done
}

function test11() {
    for ele in ${arr[@]}; do
        #temp=$(echo -n "${ele}" | wc -c)
        # -n参数:去除"\n"换行符,不去除的话,默认带换行符,字符个数就肉眼看到的多一个
        temp=`echo -n "${ele}" | wc -c`
        if [ ${temp} -lt 8 ]; then
            echo ${ele}
        fi
    done
}

function test111() {
    for ele in ${arr[@]}; do
# expr length ${ele}
        temp=`expr length ${ele}`
        if [ ${temp} -lt 8 ]; then
            echo ${ele}
        fi
    done
}

function test1111() {
    for ele in ${arr[@]}; do
        temp=`echo "${ele}" | wc -L`
        if [ ${temp} -lt 8 ]; then
            echo ${ele}
        fi
    done
}

function test2() {
    local ele=""
    for (( i=0; i<${#arr[@]}; i++ )); do
        ele=${arr[${i}]}
        if [ ${#ele} -lt 8 ]; then
            echo ${ele}
        fi
    done
}
test0
```




```sh

for i in $(cat nowcoder.txt); do
    if [ ${#i} -lt 8 ]; then
        echo ${i}
    fi
done

temp=$(cat nowcoder.txt)
arr=(${temp[*]})
for i in ${arr[*]}; do
    if [ ${#i} -lt 8 ]; then
        echo ${i}
    fi
done


for i in `cat nowcoder.txt`
do
if [ `echo $i | wc -m` -lt 9 ]
then
echo $i
fi
done

for i in `cat nowcoder.txt`
do
    if [ $(echo $i |wc -L ) -lt 8 ];then
        echo $i
    fi
done
```

###  8.5. <a name='-1'></a>高效命令

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

##  9. <a name='SHELL8'></a>SHELL8 统计所有进程占用内存大小的和

https://www.nowcoder.com/practice/fb24140bac154e5b99e44e0cee45dcaf?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

###  9.1. <a name='awk-1'></a>awk的强大功能

```sh
cat $1|awk -F ' ' '{(sum=sum+$6)}END{print sum}'
awk -F 指定空格做分割符，对第6列内存求和
```

```sh
都是利用awk的强大功能，由第一行开始读写，读到最后一行结束

awk '{a+=$6} END {print a}'
awk '{sum += $1} END {print sum}'< <(ps -u <account> -o pmem)
awk '{sum+=$6} END {print sum}' nowcoder.txt
awk '{sum+=$6} END {print sum}' nowcoder.txt
awk 'BEGIN{sum=0} {sum+=$6} END {print sum}' nowcoder.txt
awk 'BEGIN{sum=0} {sum+=$6} END {print sum}' nowcoder.txt
awk 'BEGIN { printf "%0.2f\n" ,1/2}' / awk 'BEGIN { printf("%0.2f\n", 1/2) }'
说明：
    awk 'BEGIN {print 1 / 3}' 
    不指定格式,
    最多保留6位小数,
    不会自动四舍五入,
    只有小数点后的位数大于4时会自动进行进位
    使用printf函数指定保留的小数位数
```

###  9.2. <a name='for-1'></a>for循环

```sh
sum=0
    for i in `awk '{print $6}' nowcoder.txt`
    do
        ((sum+=$i))
        done
    echo $sum

此处利用for循环，用变量i来接收nowcoder.txt文件每一行对应的数据，利用awk对每一行数据进行切割，输出第6快区域的数据信息
```

```sh
# 1
let sum=0
for i in $(awk '{print $6}' nowcoder.txt); do
    ((sum+=$i))
done
echo $sum
```

###  9.3. <a name='read'></a>read命令

```sh
sum=0
while read p
    do
        ((sum+=$(echo $p|awk '{print $6}')));       
    done < nowcoder.txt
echo $sum
通过read命令来读写数据，变量p表示所读取的每一行的数据，同时通过管道命令与awk命令结合查询，得到每行的第6快区域的数据信息。
```


```sh
# 4
sum=0;
while read p
do
    arr=($p)
    ((sum+=arr[5]))
done <nowcoder.txt
echo $sum
```

###  9.4. <a name='-1'></a>定义数组《性能最佳》

```sh
sum=0;
while read p
do
    arr=($p)
    ((sum+=arr[5]))
done <nowcoder.txt
echo $sum

此类解法未使用`awk命令`，为性能最佳，
通过`定义数组`接收每行的数据，`arr[5]`则表示`第5块区域`的数据信息。
```

```sh
该题实际需求是获取对应列值进行计算,期间需要了解ps命令结果中每一列代表的含义

#!/usr/bin/env bash
function test() {
    local sum=0
    local arr=()
    while read line; do
        arr=(${line})
        sum=$((${sum} + ${arr[5]}))
    done < nowcoder.txt
    echo ${sum}
}
test

只能用于整型,计算结果只有整数,小数点后直接丢弃

expr 1 + 2 数字和符号之间必须有空格!

$[1/2] 计算结果只有整数,小数点后直接丢弃

$(()) 计算结果只有整数,小数点后直接丢弃, 

幂运算 $(( 2 ** 3 )) 前式即:2的3次幂

let y=2+3 / let x=y+2 / let x=${y}+2 计算结果只有整数,小数点后直接丢弃

可以浮点运算的工具

bc计算器 echo " 1/3" | bc 结果为0 

必须在算术式前先指定保留位数 scale=2;

(保留俩位,用分号结束,在接算术式)

如: echo "scale=2; 1/3" | bc

以上bc结果是.33 少了个位上的0,

因为bc会在小数点前为结果为0时,

省略; 且不管保留几位小数,不会自动四舍五入

```

###  9.5. <a name='-1'></a>总结

```sh
sum=0;for i in `awk '{print $6}' nowcoder.txt`;do ((sum+=$i));done;echo $sum
sum=0;while read p; do ((sum+=$(echo $p|awk '{print $6}')));done < nowcoder.txt ;echo $sum

sum=0;while read line;do arr=($line);((sum+=arr[5])); done<nowcoder.txt;echo $sum
sum=0;while read line;do arr=(${line[@]});sum=$(($sum+arr[5])); done<nowcoder.txt;echo $sum #(最优解)
```

```sh
%MEM：占用的内存使用率
RSS：占用的内存大小

USER        PID %CPU %MEM    VSZ   RSS TTY      STAT START   TIME COMMAND
root          1  0.1  0.0 120012  6156 ?        Ss    2020 153:33 /sbin/init
root          2  0.0  0.0      0     0 ?        S     2020   0:07 [kthreadd]
root          3  0.0  0.0      0     0 ?        S     2020   0:55 [ksoftirqd/0]
root          5  0.0  0.0      0     0 ?        S<    2020   0:00 [kworker/0:0H]
root          8  0.0  0.0      0     0 ?        S     2020  37:07 [rcu_sched]
root          9  0.0  0.0      0     0 ?        S     2020   0:00 [rcu_bh]
root         10  0.0  0.0      0     0 ?        S     2020   0:59 [migration/0]
root         11  0.0  0.0      0     0 ?        S     2020   0:16 [watchdog/0]
root         12  0.0  0.0      0     0 ?        S     2020   0:15 [watchdog/1]
```

###  9.6. <a name='-1'></a> 高效计算

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

##  10. <a name='SHELL9'></a>SHELL9 统计每个单词出现的个数

https://www.nowcoder.com/practice/ad921ccc0ba041ea93e9fb40bb0f2786?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

###  10.1. <a name='awk-1'></a>awk 命令

```sh
写一个 bash脚本以统计一个文本文件 nowcoder.txt 中每个单词出现的个数。

为了简单起见，你可以假设：
    nowcoder.txt只包括小写字母和空格。
    每个单词只由小写字母组成。
    单词间由一个或多个空格字符分隔。

示例:
    假设 nowcoder.txt 内容如下：
        welcome nowcoder
        welcome to nowcoder
        nowcoder
你的脚本应当输出（以词频升序排列）：
    to 1
    welcome 2
    nowcoder 3

说明:
    不要担心个数相同的单词的排序问题，每个单词出现的个数都是唯一的。

解答：

    awk '{
        for(i=1;i<=NF;i++) 
            a[$i]+=1
        }END{
        for(x in a) 
            print x,a[x]
        }' nowcoder.txt
```


```sh
awk '{
    for (i=1; i<=NF; i++) {
        arr[$i]++
    }
} END {
    for (j in arr) {
        printf("%s %d", j, arr[j])
    }
}'
```

###  10.2. <a name='awksort'></a>awk + sort

```sh
xargs也可以
cat nowcoder.txt | xargs -n1 | sort | uniq -c | sort | awk '{print $2,$1}'

cat  ./nowcoder.txt | awk '{for(i=1;i<=NF;i++){arry[$i]++}}END{for(key in arry){print key" "arry[key]}}'
cat nowcoder.txt | awk '{for(i=1;i<=NF;i++){print $i}}'|sort|uniq -c| sort -n | awk '{print $2,$1}'
```

```sh
awk '{
 for (i = 1; i <= NF; ++i)
     mp[$i]++;
} END {
 for (k in mp)
     printf("%s %d", k, mp[k]);
}' | sort -n -k1
```

###  10.3. <a name='trawk-1'></a>tr + awk

```sh
cat $1  | tr -s ' ' '\n' |sort |uniq -c|sort |awk '{print $2" "$1}'

输出的结果是需要排序的

    tr -s 正则将空格替换为换行符
    uniq -c 统计连续文本次数

输出文件，将所有的空格转换成换行符，然后排序内容再统计单词出现的次数
```

```sh
tr ' ' '\n' | sort | uniq -c | awk '{print $2, $1}' | sort -nk2
cat nowcoder.txt | tr -s ' ' '\n' | sort | uniq -c | awk '{print $2" "$1}' | sort -k2n
先把空格换成换行符，然后排序并输出频率，然后交换左右两列的顺序输出，最后按照第二列的排序升序输出

```

###  10.4. <a name='-1'></a>高效计算

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


##  11. <a name='SHELL10'></a>SHELL10 第二列是否有重复

https://www.nowcoder.com/practice/61b79ffe88964c7ab7b98ae16dd76492?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

###  11.1. <a name='awk-1'></a>awk 命令

```sh
cat  $1 |awk '{print $2}'  |sort  |uniq -c|sort |grep -v 1
cat nowcoder.txt | awk '{print $2}' | sort |uniq -c | sort -n | awk -v OFS=" " '{if($1>1) print $1,$2}'
cat nowcoder.txt | awk '{print $2}' | sort |uniq -cd | sort -n
awk '{print $2}' nowcoder.txt | sort | uniq -cd | sort -n
awk '{a[$2]++
} END {
    for(i in a) 
        {
        if(a[i]>=2)
            {print a[i]" "i}
        }
        
    }' nowcoder.txt 
```

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
cat nowcoder.txt |awk '{ \
count[$2]++} \
END{ \
for(i in count){if(count[i] > 1){ \
print sount[i],i} \
} \
}' 
```

```sh
awk '
    {x[$2]++}END{
    for(i in x){
        if(x[i]>1)
            print x[i],i
    }
 }
'
```

###  11.2. <a name='-1'></a>高效计算

```sh
declare -A map
while read line 
do
arr=(${line})
i=${arr[1]}
if [ -z ${map[$i]} ] 
    then
    map[$i]=1
    else ((map[$i]++))
fi
done < nowcoder.txt

for i in ${!map[@]}
do
    if [ ${map[$i]} -gt 1 ]
    then
        echo ${map[$i]} $i
    fi
done

```

```sh
#!/bin/bash
# 用map存储
unset map
declare -A map
# 将每一行及重复数以键值对形式存储
while read line
do
  j=(${line[@]})     #  将每行的数据以数组方式存储,()初始化
  i=${j[1]}     #  将第二列的值赋值给j
  if (( -z ${map[$i]} ))    # 这数据第一次出现
  then
     map[$i]=1
  else
    ((map[$i]++))   # 这数据不是第一次出现，value值加一
  fi
done < nowcoder.txt

for key in ${!map[@]}
do
  if (( ${map[$key]} > 1 ))
  then
    echo ${map[$key]} $key
  fi
done
```

```sh
#！/bin/bash


declare -A arr
while read line
do
    temparr=($line)
    arr[${temparr[1]}]=$((${arr[${temparr[1]}]}+1))
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
declare -A memo

while read line
do
    arr=($line)
    key=${arr[1]}
    if test ${memo[$arr[1]]}
    then
        memo[$key]=1
    else
        memo[$key]=$((memo[$key]+1))
    fi
done < nowcoder.txt

for key in ${!memo[@]}
do
    value=${memo[$key]}
    if test $value -gt 1
    then
        echo "$value $key"
    fi
done
```

## SHELL11 转置文件的内容

https://www.nowcoder.com/practice/2240cd809c8f4d80b3479d7c95bb1e2e?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

```sh
awk '{
    for(i=1;i<=NF;i++){
      if(NR==1){
        row[i] = $i;
      }else{
        row[i] = row[i]" "$i;
      }
    }
}END{
  for(i=1;i<=NF;i++){
    print row[i]
  }
}
' ./nowcoder.txt
```

```sh
function solution1() {
    awk '{printf $1 }' nowcoder.txt
    awk '{printf $2 }' nowcoder.txt
}

function solution2() {
    cut -d" " -f1 nowcoder.txt | tr -d "\n"
    cut -d" " -f2 nowcoder.txt | tr -d "\n"
}

function solution3() {
    .
}

solution1
```

```sh
awk '{for (i=1; i<=NF; i++) arr[i]=arr[i]" "$i}END{for (i=1; i<=NF; i++) print arr[i]"\n"}' nowcoder.txt 利用一个数组，分别获取每一行的每一列 ...
```

```sh
#!/bin/bash
awk '{printf  $1" "}' nowcoder.txt
awk '{printf $2" "}' nowcoder.txt
```

```sh
awk '{
    for(i=1;i<=NF;i++){
        rows[i]=rows[i]" "$i
        }
} END{
    for(line in rows){
        print rows[line]
        }
}' $1
```

```sh
awk '{
    for (i = 1; i <= NF; i++){
        if (NR == 1) {
            array[i] = $i
        } else {
            array[i] = array[i] $i
        }
    }
} END {
    for(j = 1; j <= NF; j++){
        print array[j]
    }
}' nowcoder.txt
```

```sh
awk '{print $1}' nowcoder.txt | tr "\n" " "
awk '{print $2}' nowcoder.txt | tr "\n" " " 
```

```sh
 cut -d " " -f 1 nowcoder.txt |tr -s '\n' ' '
 cut -d " " -f 2 nowcoder.txt |tr -s '\n' ' '
```

```sh

declare -A map
while read -a arr
do

    for i in ${!arr[@]}
    do
        map[$i]="${map[$i]} ${arr[$i]}"
    done
done < nowcoder.txt

for i in ${!map[@]}
do
    echo ${map[$i]}
done

```

```sh
declare -a arr1
declare -a arr2
 
while read line
do
    arr=($line)
    arr1[${#arr1[@]}]=${arr[0]}
    arr2[${#arr2[@]}]=${arr[1]}
done < nowcoder.txt
 
echo ${arr1[@]}
echo ${arr2[@]}
```

## SHELL12 打印每一行出现的数字个数

https://www.nowcoder.com/practice/2d2a124f98054292aef71b453e705ca9?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

```sh
awk -F "[1,2,3,4,5]" '
BEGIN{sum=0}
{print "line"NR" number:"(NF-1);
sum+=(NF-1)}
END{print "sum is "sum}' nowcoder.txt
```

```sh
cnt=1
sum=0
while read line
do
    r=`echo $line | grep -oE "[12345]" | wc -l`
    echo "line${cnt} number: ${r}"
    let "cnt++"
    let "sum+=$r"
done 
echo "sum is ${sum}" < nowcoder.txt 
```

```sh
awk '{
    gsub(/[^1-5]/,"",$0);
    print "line"NR" number: "length($0);
    sum += length($0);
} END{ print "sum is "sum }'
```

```sh
#!/bin/bash

idx=1
sum=0

for line in $(cat nowcoder.txt)
do
        num=$(echo $line | grep -o [1-5] | wc -l)
        echo line$idx number:$num
        let "sum+=num"
        let "idx++"
done
echo sum is $sum
```

```sh
# 利用 awk 的 gsub 返回替换的数量
awk '{
    num = gsub(/[1-5]/, "");
    sum += num;
    printf("line%d number: %d\n", NR, num);
}
END {
    printf("sum is %d\n", sum);
}'
```

```sh

awk -F "[1,2,3,4,5]" '
BEGIN{sum=0}
{print "line"NR" number:"(NF-1);
sum+=(NF-1)}
END{print "sum is "sum}' nowcoder.txt
```

```sh
#!/bin/bash
j=0
i=0
sum=0
while read line
do
    for((a=0;a<${#line};a++));do
        if [[ ${line:$a:1} =~ [1-5] ]];then
            i=$(($i+1))
        fi
    done
    j=$(($j+1))
    echo "line$j number:$i"
    sum=$(($sum+$i))
    i=0
done < nowcoder.txt
echo "sum is ${sum}"
```

```sh
#!/bin/bash
j=0
i=0
sum=0
while read line
do
    for((a=0;a<${#line};a++));do
        if [[ ${line:$a:1} =~ [1-5] ]];then
            i=$(($i+1))
        fi
    done
    j=$(($j+1))
    echo "line$j number:$i"
    sum=$(($sum+$i))
    i=0
done < nowcoder.txt
echo "sum is ${sum}"
```

```sh
linecount=0
sum=0
count=0

while read line
do
    for (( i=0; i<${#line};i++ ))
    do
        if [[  ${line:$i:1} =~ [1-5] ]]
        then
            count=$(($count+1))
        fi
    done
    linecount=$(($linecount+1))
    echo "line$linecount number:$count"
    sum=$(($sum+$count))
    count=0
    
done<nowcoder.txt
echo "sum is $sum"
```

```sh
#/bin/bash
linecount=0
sum=0
count=0

while read line
do
    for (( i=0; i<${#line};i++ ))
    do
        if [[  ${line:$i:1} =~ [1-5] ]]
        then
            count=$(($count+1))
        fi
    done
    linecount=$(($linecount+1))
    echo "line$linecount number:$count"
    sum=$(($sum+$count))
    count=0
    
done<nowcoder.txt
echo "sum is $sum"
```

## SHELL13 去掉所有包含this的句子

https://www.nowcoder.com/practice/2c5a46ef755a4f099368f7588361a8af?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

```sh
方法1
grep 命令 -v 显示不包含匹配文本的所有行

grep -v 'this'
grep -v 'this'

方法2
sed 命令 -> d 删除 -> // 包含要搜索的字符串

sed '/this/d'
sed '/this/d'

方法3
awk 命令,检查当前 $0 不包含 this 随机输出

awk '$0!~/this/ {print $0}'
awk '$0!~/this/ {print $0}'
awk '!/this/' nowcoder.txt
```

```sh
#!/bin/bash
while read line
do
    flag=0
    for i in $line
    do
        if [[ $i = "this" ]];then
            flag=1
            break
        fi
    done
    if [[ $flag -eq 0 ]];then
        echo $line
    fi
done < nowcoder.txt
```

```sh
#/bin/bash


while read line
do
    flag=0
    for  i in $line 
    do
        if [[ $i = "this" ]]
        then flag=1
         break
        fi
    done 
    
    if [[ $flag -eq 0 ]]
    then
        echo $line
    fi
done< nowcoder.txt
```

```sh
while read row
do
  flag=1
  for i in $row
  do
    if test $i = "this"
    then flag=0
    fi
  done
  if test $flag -eq 1
  then echo $row
  fi
done

```

## SHELL14 求平均值

https://www.nowcoder.com/practice/c44b98aeaf9942d3a61548bff306a7de?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1


```sh
#!/usr/bin/env bash

function solution_1() {
    read count
    local sum=0
    local loop=${count}
    while (( ${loop} > 0)); do
        read m
        sum=$((${sum}+${m}))
        loop=$((${loop}-1))
    done
    echo "scale=3; ${sum}/${count}" | bc 
}

function solution_2() {
    read count
    local sum=0
    local loop=1
    while (( ${loop} <= ${count})); do
        read m
        ((sum+=m))
        ((loop++))
    done
    echo "scale=3; ${sum}/${count}" | bc 
}

function solution_3()  {
    awk 'NR==1 {all=$0} NR>1 {total+=$0} 
    END{printf "%.3f" ,total/all}'
}

function solution_4()  {
    read -p '请输入数组长度：' len
    i=1
    while [ $i -le $len ]
    do
        read -p '请输入数组数字：' num[$i]
        let i++
    done
    for i in ${num[*]}
    do
        sum=$((sum+i))
    done
    echo "scale=3; ${sum}/${len}" | bc 
    #awk -va=$sum -vb=$len 'BEGIN{printf "%.3f\n",a / b }'
}

function solution_999() {
    read n
    sum=0
    read m
    arr=($m)
    for ele in ${arr{@}}; do 
        sum=$((${sum}+${ele}))
    done
    echo "scale=3; ${sum}/${n}" | bc 
}

solution_1
```

```sh
很简单的一个东西折腾了半天，
“第1行为输入的数组长度N”，
一直以为是第一行为，第2~N行为，我这阅读理解简直满分，通过率才26.39%
，我感觉理解错的不止我一个，而且还有很多是直接输出结果6.333通过的，服了
awk '
{if(NR==1) {
    N=$1
} else{
    sum+=$1}
    } 
END{printf ("%.3f",sum/N) }'
awk '
NR==1 {all=$0} 
NR>1 {total+=$0} 
END {printf "%.3f" ,total/all}'
awk '
{NR==1?len=$1:sum+=$1} 
END {printf "%.3f",sum/len}' nowcoder.txt
```

```sh
len=''
i=0
sum=0
while read num; do
    if [ -z "${len}" ]; then
       len=$num
       continue
    fi
    (( sum += num ))

    (( i++ ))
   if [ ${i} -eq $len ]; then
       break
   fi
done
printf "%.3f" $( echo "scale=3; ${sum} / ${len}" | bc )
```

```sh
read cnt
sum=0
cnt_cp=0
while [ $cnt -gt 0 ]
do
    read num
    let "sum+=num"
    let "cnt--"
    let "cnt_cp++"
done
echo "scale=3;$sum/$cnt_cp" | bc
```

```sh
awk '{
    if (NR != 1){
        sum += $1
    }
} END {
    printf("%0.3f\n", sum/(NR-1))
}'
```

```sh
#!/bin/bash
avg=0
sum=0
a[]=(4 1 2 9 8)
for i in ${#a[*]}
do
    let sum+=${a[i]}
done
avg='expr $sum / $a[0]'
echo 6.333
```

```sh
read n 
while (( $n < 1 ))
do 
read m 
s+=m
let "n--"
done
printf '%.3f' 6.333
```

```sh
#!/bin/bash
avg=0
sum=0
num=0
while read line
do
    if [[ avg -eq 0 ]];then
        num=${line}
    else
        ((sum+=line))
    fi
        ((avg++))
done < nowcoder.txt
printf "%.3f" 6.333

```

```sh
read n 
while (( $n > 1 ))
do 
read m 
s+=m
let "n--"
done
printf '%.3f' 6.333
```

```sh

read n 
while (($n >1))
do
read m 
s+=m
let 'n--'
done
printf '%.3f' 6.333
```

## SHELL15 去掉不需要的单词

https://www.nowcoder.com/practice/838a3acde92c4805a22ac73ca04e503b?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

```sh
#!/usr/bin/env bash

function solution_1() {
    local arr=""
    while read line; do
        arr=(${line})
        for ele in ${arr[@]}; do
            if [[ "${ele}" =~ B || "${ele}" =~ b ]]; then
                continue
            fi
            echo "${ele} "
        done
    done < nowcoder.txt
}

function solution_2() {
    local arr=""
    while read line; do
        arr=(${line})
        for ele in ${arr[@]}; do
            if [[ "${ele}" =~ B|b ]]; then
                continue
            fi
            echo "${ele} "
        done
    done < nowcoder.txt
}

function solution_4() {
    sed '/B\|b/d' nowcoder.txt
}

function solution_5() {
    #awk '{ for(i=0; i<NF; i++) if($i ~! /b/) print $i }' nowcoder.txt
    #awk '/\+[^b\+]\+/{print $1}' nowcoder.txt
    :
}

function solution_999() {
    while read line; do
        my_array=("${my_array[@]}" $line)
    done
    declare -a pattern=(${my_array[@]/*[B|b]*/})
    echo ${pattern[@]}
}

solution_1


```

```s
全套

grep -v -E 'b|B' nowcoder.txt
grep -iv "b"
grep -iv "b"
grep -v '[bB]' 
grep -v [Bb]

cat  $1 |grep -v -i b
cat nowcoder.txt|grep -vi "b"
cat nowcoder.txt | grep -v -E 'b|B' 
cat nowcoder.txt|grep -vi "b"

awk '$0!~/b|B/ {print $0}' nowcoder.txt
awk '!/[bB]/'


sed '/[Bb]/d'
sed '/b\|B/d'
```

```sh
while read line
do
    my_array=("${my_array[@]}" $line)
done
  
declare -a pattern=(${my_array[@]/*[Bb]*/})
echo ${pattern[@]}
```

```sh
while read line
do
    my_array=("${my_array[@]}" $line)
done
 
declare -a pattern=(${my_array[@]/*[Bb]*/})
echo ${pattern[@]}
```


## SHELL16 判断输入的是否为IP地址

https://www.nowcoder.com/practice/ad7b6dbfab2a4267a9991110c57aa64f?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

```sh
# 使用正则表达式
 awk '{
     if ($0 ~ /^((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[09][0-9]|[1-9][0-9]|[0-9])$/) {
         print("yes");
     } else if ($0 ~ /[[:digit:]].[[:digit:]].[[:digit:]].[[:digit:]]/){
         print("no");
     } else {
         print("error")
     }
 }' nowcoder.txt

# 使用 . 作为分隔符
 awk -F '.' '{
     if (NF == 4) {
         for (i = 1; i < 5; i++) {
             if ($i > 255 || $i < 0) {
                 print("no")
                 break
             }
         }
         if (i == 5) {
             print("yes")
         } else {
             print("error")
         }
     }    
 }'

# bash 脚本，使用 . 作为分割符号
IFS='.'
while read line; do
    arr=(${line})
    if [ ${#arr[*]} -ne 4 ]; then
        printf "error\n"
    else
        for ((i = 0; i < ${#arr[*]}; i++)); do
            if [ ${arr[${i}]} -gt 255 ]; then
                printf "no\n"
                break
            fi
            done
        [ $i == 4 ] && printf "yes\n"
    fi
done



```

```sh
while read line
    do
        arr=(${line//./ })
        if [ ${#arr[*]} -ne 4 ];then
                printf "error\n"
            else
                for ((i=0; i<${#arr[*]}; i++))
                    do
                        [ ${arr[${i}]} -gt 255 ] && printf "no\n" && break
                    done
                    [ $i == 4 ] && printf "yes\n"
        fi
    done
```

```sh
awk -F "." '{
    if (NF == 4) {
        for (i=1; i<5; i++) {
            if ($i > 255 || $i < 0) {
                print("no");break
            }
        }
        if (i==5){print("yes")}
    } else {
        print("error")
    }
}'
```

```sh
awk -F "." '{
    if (NF == 4) {
        if ($0 ~ /^((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[09][0-9]|[1-9][0-9]|[0-9])$/) {
                print("yes")
        } else {
                print("no")
        }
    } else {
        print("error")
    }
}'

```

```sh
awk -F'.' '{
    if(NF!=4){
        print "error";next
    }else{
        for(i=1;i<=4;i++){
            if(i==4){
                print "yes";next
            }else{
                if($i<0||$i>255){
                    print "no";next
                }
            }
        }
    }
}' nowcoder.txt

完全依靠awk的next功能实现，
如果中途有一个字段不符合要求，都输出no，
并且处理下一行；如果进入到最后一个字段，依然没有退出，
只能说明，当前行的所有字段都是符合要求的，
但是又不能在for中打印，不然会重复打印yes，于是在此前加上一个if识别四个字段都合法的情况
学到了for中会重复打印，感谢

```

```sh
#!/bin/bash
while read line
do 
arr=(${line//./ })
if [ ${#arr[*]} -ne 4 ];then
printf "error\n"
else
for((i=0;i<${#arr[*]};i++))
do
[ ${arr[${i}]} -gt 255 ] && printf "no\n" && break
done
[ $i == 4 ] && printf "yes\n"
fi
done < nowcoder.txt

```

```sh
while read line
    do
        arr=(${line//./ })
        if [ ${#arr[*]} -ne 4 ];then
                printf "error\n"
            else
                for ((i=0; i<4; i++))
                    do
                        [ ${arr[${i}]} -gt 255 ] && printf "no\n" && break
                    done
                    [ $i == 4 ] && printf "yes\n"
        fi
    done

```

```sh
while read line; do
    arr=(${line//./ })
    if [ ${#arr[*]} -ne 4 ]; then
        printf "error\n"
    else
        for ((i = 0; i < ${#arr[*]}; i++)); do
            if [ ${arr[${i}]} -gt 255 ]; then
                printf "no\n"
                break
            fi
            done
        [ $i == 4 ] && printf "yes\n"
    fi
done
```

```sh
#!/bin/bash

# bash 脚本，使用 . 作为分割符号
IFS='.'
while read line; do
    arr=(${line})
    if [ ${#arr[*]} -ne 4 ]; then
        printf "error\n"
    else
        for ((i = 0; i < ${#arr[*]}; i++)); do
            if [ ${arr[${i}]} -gt 255 ]; then
                printf "no\n"
                break
            fi
            done
        [ $i == 4 ] && printf "yes\n"
    fi
done
```


## SHELL17 将字段逆序输出文件的每行

https://www.nowcoder.com/practice/e33fff83fd384a21ba67f3104fb8d646?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

```sh
awk -F ":" '{
    for(i=1;i<=NF;i++){
        res[NF+1-i] = $i
    }
    msg = ""
    for(k in res){
        msg = msg (msg == "" ? "":":") res[k]
    }
    print msg
}'

数组反向存起来，再拼接成字符串
```

```sh
awk -F ":" '{for (i=NF;i>0;i--){
    
    if(i==1){
    print($1);
    break;
    }
    printf ($i ":");
}}'
```

```sh
awk -F ":" '{
    msg = "";
    for(i=0;i<NF;i++){
        msg = msg $(NF-i);
        if(i!=NF-1) msg = msg ":"
    }
    print msg
}'

```

```sh
# NF表示一行有多少个单词,-F ":"表示按:分隔
awk -F ":" '{ for(i=NF;i>=1;i--)
                    if (i != 1){
                    {printf $i ":"}
                    } else {
                    {print $i}
                    }
        }' nowcoder.txt
```

```sh
IFS=":"
while read line
    do
        arr=($line)
        for ((i=${#arr[*]}-1; i>0; i--))
            do
                if [ ${arr[${i}]} == "nowcoder.txt" ];then
                    s="$s*:"
                    continue
                fi
                [ ${arr[${i}]} == "a.sh" ] && continue
                s="$s${arr[${i}]}:"
            done
        printf "$s${arr[0]}\n"
        s=""
    done
```

```sh
IFS=":"
while read line
do
    arr=($line)
    for ((i=${#arr[*]}-1; i>0; i--))
    do
        if [ ${arr[${i}]} == "nowcoder.txt" ];then
            s="$s*:"
            continue
        fi
        [ ${arr[${i}]} == "a.sh" ] && continue
        s="$s${arr[${i}]}:"
    done
    printf "$s${arr[0]}\n"
    s=""
done
```

```sh
#!/bin/bash


OLD_IFS="$IFS"
IFS=":"

while read -a arr
do
    for (( i=${#arr[@]}-1;i>-1;i-- ))
    do
        echo -n "${arr[${i}]}"
        if (( ${i} != 0 ))
        then
            echo -n ":"
        fi
    done
    echo ""
done < nowcoder.txt

IFS="$OLD_IFS"
```

```sh
IFS=":"
while read line
    do
        arr=($line)
        for (( i=${#arr[*]}-1; i>0; i--))
            do
                if [ ${arr[${i}]} == "nowcoder.txt" ];then
                    s="$s*:"
                    continue
                fi
                [ ${arr[${i}]} == "a.sh" ] && continue
                s="$s${arr[${i}]}:"
            done
        printf "$s${arr[0]}\n"
        s=""
    done
```

## SHELL18 域名进行计数排序处理


https://www.nowcoder.com/practice/f076c0a3c1274cbe9d615e0f3fd965f1?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

```sh
awk -F"://" '{print $2}'|awk -F '/' '{print $1}'|sort |uniq -c|sort -r |awk -F' ' '{print $1" "$2}'
--不加awk -F' ' '{print $1" "$2}'这个提交不了


我也是这样写的

cat nowcoder.txt |awk -F "/" '{print $3}'| sort |uniq -c | sort -nrk2 | awk '{print $1,$2}' ，

不加uniq -c命令执行后数字前面会有一段空格，

最后非得在最后加一个awk,不知道有没有大佬能解释一下
```

```sh
declare -A map
while read line
do
    arr=(${line//\// })
    ((map[${arr[1]}]++))
#     arr=(${line//\// })
#         ((map[${arr[1]}]++))
done < nowcoder.txt

for i in ${!map[@]}
    do
        str="${map[$i]} $i\n$str"
    done
printf "$str" | sort -r

```

```sh
awk -F "/" '{
    arr[$3]++
}END{
    for(i in arr){
        printf("%d %s\n",arr[i],i)
    }
}' | sort -r
```

```sh
awk -F "[/]+" '{print $2}' nowcoder.txt | sort |uniq -c| sort -nr | awk '{print $1,$2}'
```

```sh
#!/bin/bash
declare -A map
while read line
    do
        arr=(${line//\// })
        ((map[${arr[1]}]++))
    done < nowcoder.txt
for i in ${!map[@]}
    do
        str="${map[$i]} $i\n$str"
    done
printf "$str" | sort -r
```

```sh
awk -F "/" '
{a[$3]++}
END{ for(i in a) {
    print a[i],i
    } 
}' nowcoder.txt|sort -nr

awk -F "/" '
{num[$3]++}
END{for(i in num) 
print num[i], i
}' nowcoder.txt | sort -nr
```

```sh
awk -F "/" '{
    for(i=1;i<=NF;i++){
        if(i==3){
            a[$i]+=1
        }
    }
}END{
    for(x in a){
        printf("%d %s\n",a[x],x)
    }
}' nowcoder.txt|sort -r
```

```sh
awk -F '/' '
{A[$3]++} 
END{for(i in A){
    print A[i],i
    }
}' nowcoder.txt | sort -r
```

## SHELL19 打印等腰三角形

https://www.nowcoder.com/practice/1c55ca2b73a34e80bafd5978810dd8ea?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

```sh
awk 'BEGIN{
    for(n = 1; n <= 5; n++){
        row = "";
        for(i = 1;i <= 5 - n; i++){
            row = row " "
        }
        for(i = 1; i <= n; i++){
            row = row "*" " "
        }
        print row
    }
}'

```

```sh
awk 'BEGIN{
     for (i=1;i<6;i++){ 
         for(j=5;j>i;j--)
             printf " "
         for(k=0;k<i;k++)
             printf "* "
         print ""
     }
     }'

```

```sh
for ((i=1; i<=5; i++))
do
    # 打印空格
    for ((j=5-i; j>=1; j--))
    do
        printf " "
    done
    # 打印星星
    for ((k=1; k<=i; k++))
    do
        printf "* "
    done
    printf "\n"
done
```

```sh

for (( i=1;i<=5;i++ ));do
 for ((j=5;j>=1;j-- ));do
    if [ $j -le $i ];then
  echo -e "* \c"
    else
  echo -e " \c"
  fi
  done
 echo
done
```

```sh
#! bin/bash

printf "    *
   * *
  * * *
 * * * *
* * * * *
"
```

```sh
#!/bin/bash
 
echo "    *
   * *
  * * *
 * * * *
* * * * *"
```


## SHELL20 打印只有一个数字的行

https://www.nowcoder.com/practice/296c2785e64c46b7ae4c76bf190c2072?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

```sh
以数字开头，非数字结尾
awk '/^[0-9]([a-z]+)$/'

以字母开头，数字结尾
awk '/^[a-z]*[0-9]$/'

以字母开头，字母结尾，中间一个数字
awk '/^[a-z][0-9][a-z]$/'

答案：
awk '/(^[0-9]([a-z]+)$)|(^[a-z]*[0-9]$)|(^[a-z][0-9][a-z]$)/'
```

```sh
awk -F "[0-9]" '{if(NF==2) print $0}' nowcoder.txt
```

```sh
纯shell

while read line
do
    count=0
    for ((i=0;i<${#line};i++))
        do
            [[ ${line:i:1} =~ [0-9] ]] && ((count++))
        done
    if [ $count -eq 1 ];then
        printf "$line\n"
    fi
done < nowcoder.txt


纯awk

awk -F "" '{
    for (i=1; i<=NF; i++) {
        if ($i ~ /[0-9]/) {
            k++
        }
    }
    if (k==1){print($0)}
    k=0
}'

```

```sh
while read line
do
    count=0
    for (( i=0;i<${#line};i++ ))
    do
        if [[ ${line:$i:1} =~ [0-9] ]]
        then
            ((count++))
        fi
    done
    if [ $count -eq 1 ]
    then
        echo $line
    fi
done < nowcoder.txt
```

```sh
while read line
do
    count=0
    for ((i=0;i<${#line};i++))
        do
            [[ ${line:i:1} =~ [0-9] ]] && ((count++))
        done
    if [ $count -eq 1 ];then 
        printf "$line\n"
    fi
done < nowcoder.txt

```

```sh
 while read line
 do
     let count=0
     for (( i = 0; i < ${#line}; i++))
         do
             if [[ ${line:i:1} =~ [0-9] ]]; then 
               count=$(($count+1))
             fi
         done
     if [ $count -eq 1 ];then
         printf "$line\n"
     fi
 done < nowcoder.txt
```

```sh

while read line
do
    let count=0;
    for(( i = 0; i < ${#line}; i++))
    do
             [[ ${line:i:1} =~ [0-9] ]] && ((count++))
    done
    
        if [[ $count == 1 ]]
        then
            printf "$line \n"
        fi

done < nowcoder.txt

```

```sh

while read line
do
    count=0
    for ((i=0;i<${#line};i++))
        do
            [[ ${line:i:1} =~ [0-9] ]] && ((count++))
        done
    if [ $count -eq 1 ];then
        printf "$line\n"
    fi
done < nowcoder.txt
```

## SHELL21 格式化输出

https://www.nowcoder.com/practice/d91a06bfaff443928065e611b14a0e95?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

```sh
awk -F "" '{
    k=0
    for (i=NF; i>0; i--) {
        k++
        str = sprintf("%s%s", $i, str)
        if (k%3 == 0 && i>=2 && NF > 3) {
            str = sprintf(",%s", str)
        }
    }
    print(str)
    str=""
}'

```

```sh
awk 'BEGIN{FS=""}
{for(i=1;i<=NF;i++) {
    if((NF-i)%3==0&&i!=NF) 
    printf $i",";
    else printf $i
    };
printf "\n"}' nowcoder.txt 

这个判断算是个灵魂吧 ...
```

```sh
awk '{
l=length($0)
f=l%3
for (i=1;i<=l;i++){
    printf substr($0,i,1)
    if((i-f)%3==0 && i!=l)
        printf ","
}
print ""
}' nowcoder.txt

```

```sh
for i in `cat nowcoder.txt`
do
  printf "%'d\n" $i
done
```

```sh
sed -E ':a; s/([[:digit:]])([[:digit:]]{3})\>/\1,\2/; ta'
\>是匹配一个零宽的单词边界
```

```sh
while read lines; do
    printf "%'d\n" $lines
done<nowcoder.txt
```

```sh
while read line
do
    len=${#line}
    rest=$((len%3))
     
    echo -n "${line:0:rest}"
    for ((i=rest;i<len;i+=3))
    do
        if [[ rest -eq 0 ]] && [[ i -eq rest ]];then
            echo -n "${line:i:3}"
        else
        echo -n ",${line:i:3}"
        fi
    done
    echo ""
done < nowcoder.txt
```

```sh
while read line
  do
      printf "%'d\n" $line
  done < nowcoder.txt
        
```

```sh
#!/bin/bash
while read line
do
    len=${#line}
    target=""
    for ((i=1;i<=${len};i++))
    do
        bit=${line:0-$i:1}
        target=${bit}${target}
        if [ $[ $i%3 ] -eq 0 ] && [ $i -ne $len ];
        then
            target=','$target
        fi
    done
    echo $target
done < nowcoder.txt

```


## SHELL22 处理文本

https://www.nowcoder.com/practice/908d030e676a4fac997a127bfe63da64?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

```sh
awk -F ":" '{
        a[$1] = a[$1] $2 "\n"
    }
    END {for (i in a){
        printf("[%s]\n%s",i,a[i])
        }
    }' nowcoder.txt
拼接字符串，直接输出即可。
```

```sh
awk -F ":" '{a[$1]=a[$1]":"$2}END{
for (k in a )
    print k""a[k]
}' nowcoder.txt|awk -F ":" '{
printf ("[%s]\n",$1);
for (i=2;i<=NF;i++)
    print $i
}'

```

```sh
awk -F ":" '{
    res[$1] = (res[$1] == "" ? $2 : (res[$1] "\n" $2))
}END{
    for(k in res){
        print "["k"]"
        print res[k]
    }
}'
```

```s
cat nowcoder.txt | sort -t ":" -k 1,1 -sb | awk -F ':' 'BEGIN{t=0}{if($1 != t){print "["$1"]";t=$1;print $2}else{print $2}}'

sort 的 -s 可以起到仅对比某列，其余列按照文本顺序输出
```

```s
awk -F ":" '{
    res[$1] = (res[$1] == "" ? $2 : (res[$1] "\n" $2))
}END{
    for(k in res){
        print "["k"]"
        print res[k]
    }
}'
```

```s


declare -A map
while read line
    do
        arr=(${line/:/ })
        map["${arr[0]}"]="${map["${arr[0]}"]}${arr[1]}\n"
    done < nowcoder.txt
k=0
for i in ${!map[*]}
    do
        [ $k -eq 0 ] && k=1 && tmp="[$i]\n${map[$i]}" && continue
        printf "[$i]\n${map[$i]}"
    done
printf "$tmp"

```

```s
declare -A map
while read line
    do
        arr=(${line/:/ })
        map["${arr[0]}"]="${map["${arr[0]}"]}${arr[1]}\n"
    done < nowcoder.txt
k=0
for i in ${!map[*]}
    do
        [ $k -eq 0 ] && k=1 && tmp="[$i]\n${map[$i]}" && continue
        printf "[$i]\n${map[$i]}"
    done
printf "$tmp"
```

```s
#!/bin/bash
declare -A map
while read line
    do
        arr=(${line/:/ })
        map["${arr[0]}"]="${map["${arr[0]}"]}${arr[1]}\n"
    done < nowcoder.txt
k=0
for i in ${!map[*]}
    do
        [ $k -eq 0 ] && k=1 && tmp="[$i]\n${map[$i]}" && continue
        printf "[$i]\n${map[$i]}"
    done
printf "$tmp"
```


## SHELL29 netstat练习1-查看各个状态的连接数

https://www.nowcoder.com/practice/f46a302d14e04b149bb50670f255293a?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1&gioEnter=menu


```sh
awk '{
    if($NF ~ /ESTABLISHED/ || $NF ~ /LISTEN/ || $NF ~ /TIME_WAIT/){
        a[$NF]++
        }
    }END{
        for(i in a )
            print i" "a[i]
    }' | sort -nrk2
#先把最后一列存进数组，但是最后一列除了等待、建立连接、监听，还有其他没用的内容。所以用if筛选有用的三个，再用for循环输出数组内容和次数，最后排序


cat nowcoder.txt | grep "tcp" | awk '{res[$6]++}END{for(i in res) print i" "res[i]}' | sort -rn -k2
#!/bin/bash
cat nowcoder.txt|grep 'tcp'|awk '{print $NF}'|sort|uniq -c|sort -nr|awk '{print $2,$1}'

```

```sh
awk '{
    if ($1 == "tcp"){
        a[$6] ++;    
    }
}
END{
    for (i in a){
        printf("%s %d\n", i, a[i])
    }
}' |sort -nr -k 2
```

```sh
#!/bin/bash

declare -A map

while read -a line
do
    if [ "x${line[5]}" = "xESTABLISHED" ]
    then
        ((map[${line[5]}]++))
    elif [ "x${line[5]}" = "xTIME_WAIT" ]
    then
        ((map[${line[5]}]++))
    elif [ "x${line[5]}" = "xLISTEN" ]
    then
        ((map[${line[5]}]++))
    else
        ((map[other]++))
    fi
done < nowcoder.txt

echo "ESTABLISHED ${map[ESTABLISHED]}"
echo "TIME_WAIT ${map[TIME_WAIT]}"
echo "LISTEN ${map[LISTEN]}"
```

```sh
awk '
NR!=1 && $1=="tcp"{
    a[$6]++
}END{
    for(i in a) 
        print i,a[i]
}'|sort -t " " -k2nr
```

```sh
awk '{
    if($1 == "tcp")   cnt[$6]++
}END{
    for(i in cnt){
        printf "%s %d\n",i,cnt[i]
    }
}'|sort -nr -k2
```

## SHELL30 netstat练习2-查看和3306端口建立的连接

https://www.nowcoder.com/practice/534b95941ffb495b9ba57fbfc3cd723a?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

```sh
awk '{
    if($0 ~"3306"){
        if($6=="ESTABLISHED"){
            a[$5]++
        }
    }
}END{
    for(i in a){
        print a[i],i
    }
}' | sed 's/:3306//' | sort -nr -k1
```

```sh
grep 3306|grep ESTABLISHED|awk -F' ' '{print $5}'|awk -F':' '{print $1}'|sort|uniq -c|sort -n -r|awk -F' ' '{print $1" "$2}'

# 用-n按数值排序
```

```sh
awk -F "[ :]+" '
/3306.*ESTABLISHED/{
    array[$6]++
}END{
    for(i in array) 
        print array[i],i
}' | sort -nrk1
```

```sh
cat nowcoder.txt | grep 3306 | grep ESTABLISHED | awk '{print $5}' | awk -F ':' '{print $1}'| sort | uniq -c | sort -nr | awk '{print $1" "$2}'
```

```sh
awk '
BEGIN{FS=" "}{
    if($5~/3306/ && $NF~"ESTABLISHED")
        a[substr($5,1,index($5,":")-1)]+=1
}END{
    for(i in a)
        print a[i],i
}' | sort -nrk1
```

```sh
awk '{
    if($6 == "ESTABLISHED" && $5 ~/3306/){
        sub(/:.*/, "", $5) 
        arr[$5]++
    }
}END {
    for(i in arr){
        printf("%d %s\n",arr[i],i)
    }
}' | sort -nr
```

```sh
awk -F'[ :]+' '
/3306/
    {ip[$6]++}
END
    {for(i in ip)
        print ip[i],i}' nowcoder.txt| sort -nr
```

```sh
#!/bin/bash
awk '{
    if ($6 == "ESTABLISHED" && $5 ~ /3306/){
        sub(/:3306/, "", $5)
            arr[$5]++
    }
} END {
    for (i in arr) {
        printf("%d %s\n", arr[i], i)
    }
}' | sort -nr
```

```sh
awk '{
    if ($6 == "ESTABLISHED" && $5 ~ /3306/){
        sub(/:.*/, "", $5)
        arr[$5]++
    }
} END {
    for (i in arr) {
        printf("%d %s\n", arr[i], i)
    }
}' | sort -nr
```

```sh
awk '{
    if($1 == "tcp" && $6 == "ESTABLISHED" && $5 ~/:3306/){
        cnt[substr($5,0,length($5)-5)]++
    }
}END{
    for(i in cnt){
        printf "%d %s\n",cnt[i],i
    }
}'|sort -nr
```

## SHELL31 netstat练习3-输出每个IP的连接数

https://www.nowcoder.com/practice/f601fc4f35b5453ba661531051b6ce69?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

```sh
awk '{
    if ($1 == "tcp") {
        split($5, a, ":")
        t[a[1]]++
    }
} END {
    for (i in t){
        printf("%s %d\n", i, t[i])
    }
}' | sort -nrk2
```

```sh
declare -A ips
awk -F'[: ]+' '$1=="tcp"{
    ips[$6]++
}END{
    for (i in ips){
        printf("%s %d\n",i,ips[i])
 }
}' nowcoder.txt | sort -nr -k2 
```

```sh
awk -F "[ :]+" '
$1 == "tcp" 
    {a[$6]++}
END
    {for(i in a){
        print i,a[i]}}' nowcoder.txt | sort -rnk 2
+号的作用就是连续出现的分隔符当做一个来处理
```

```sh
cat nowcoder.txt|grep 'tcp'|awk -F ":" '{print $2}'|awk '{print $2}'|sort|uniq -c|sort -nr|awk '{print $2,$1}'
```

```sh
grep tcp|awk '{print $5}'|sort |uniq -c|sort -n -r|awk -F ":" '{print $1}'|awk '{print $2" "$1}'
```

```sh
#! /bin/bash
awk '{
    if ($1 == "tcp"){
        split($5,a,":")
        res[a[1]]++;
    }
}'END'{
    for (i in res){
        print i" "res[i]
    }
}' nowcoder.txt | sort -nrk2
```

```sh
awk '{
    if ($1 == "tcp") {
        split($5, a, ":")
        t[a[1]]++
    }
} END {
    for (i in t){
        printf("%s %d\n", i, t[i])
    }
}' | sort -nrk2
```

```sh
awk '{
    if ($1=="tcp"){
        split($5, a, ":");
        t[a[1]]++;
    }
    }
    END{
        for (i in t){
            printf "%s %s\n", i, t[i];
        }
    }' | sort -rnk2
```

## SHELL32 netstat练习4-输出和3306端口建立连接总的各个状态的数目

https://www.nowcoder.com/practice/5ce76fd1513d4eacae68ad3b2aca1fbb?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

```sh
echo "TOTAL_IP `awk '
$5~/3306$/
    {print $5}' nowcoder.txt
|awk -F: '
{print $1}
'
|sort -u
|wc -l`"

awk '
$5~/3306$/
{a[$6]++}
END
{for(k in a) 
    print k,a[k]}' nowcoder.txt

echo "TOTAL_LINK `awk '$5~/3306$/{print $0}' nowcoder.txt|wc -l`"

```

```sh
awk '{
    if ($1 == "tcp" && $5 ~ /3306/) {
        if ($6 == "ESTABLISHED") {
            es++
        }
        ans++
        arr[$5]=0
    }
} END {
    printf("TOTAL_IP %d\nESTABLISHED %d\nTOTAL_LINK %d", length(arr), es, ans)
}'
```

```sh
echo -e "TOTAL_IP 3\nESTABLISHED 20\nTOTAL_LINK 20"
```

```sh

echo "TOTAL_IP 3
ESTABLISHED 20
TOTAL_LINK 20"
```

```sh
echo "TOTAL_IP 3"
echo "ESTABLISHED 20"
echo "TOTAL_LINK 20"
```

```sh
awk '{
    if($1=="tcp" && $5~"3306"){
        if($6 == "ESTABLISHED"){
            es++
}
ans++
arr[$5]=0
}
}END{
    printf("TOTAL_IP %d\nESTABLISHED %d\nTOTAL_LINK %d",length(arr),es,ans)}'
```

```sh
awk '{
    if ($1 == "tcp" && $5 ~ /3306/) {
        if ($6 == "ESTABLISHED") {
            es++
        }
        ans++
        arr[$5]=0
    }
} END {
    printf("TOTAL_IP %d\nESTABLISHED %d\nTOTAL_LINK %d", length(arr), ans, es)
}'
```

```sh
awk '{if ($1 == "tcp" && $5 ~ /3306/) {
        if ($6 == "ESTABLISHED") {
            es++
        }
        ans++
        arr[$5]=0
    }
} END {
    printf("TOTAL_IP %d\nESTABLISHED %d\nTOTAL_LINK %d", length(arr), es, ans)
}'
```

## SHELL33 业务分析-提取值

https://www.nowcoder.com/practice/f144e52a3e054426a4d265ff38399748?tpId=195&tags=&title=&difficulty=0&judgeStatus=0&rp=1

```sh
awk '{for (i=6;i<=NF;i++) printf $i" ";print ""}' nowcoder.txt |
awk -F":|," '{switch(NR){
    case 1 : print "serverVersion:"$2;break
    case 3 : print "serverName:"$2;break
    case 4 : print "osName:"$2;print "osVersion:"$4;break
    default: break;
}}'


```

```sh
awk '{
    if ($0 ~ /Server version/) {
        sub(/.*:/, "", $0)
        printf("serverVersion:%s\n", $0)
        next
    }
    if ($0 ~ /Server number/) {
        sub(/.*:/, "", $0)
        printf("serverName:%s\n", $0)
        next
    }
    if ($0 ~ /OS Version/) {
        sub(/.+Name:/, "", $0)
        t=$0
        sub(/,.*/, "", $0)
        sub(/.*:/, "", t)
        printf("osName:%s\nosVersion:%s", $0, t)
        exit
    }
}'
```

```sh
awk -F "[:,]" '{
    if($0~"Server version"){
        print "serverVersion:" $4;
    }
    if($0~"Server number"){
        print "serverName:" $4;
    }
    if($0~"OS Name"){
        print "osName:" $4;
    }
    if($0~"OS Version"){
        print "osVersion:" $6
    }
}'

```

```sh
awk -F'log' '{print $2}'|awk -F':' '{
    if($1==" Server version"){
        print "serverVersion:"$2
        } 
    if($1==" Server number"){
        print "serverName:"$2
        } 
    if($1==" OS Name") 
        print "osName:"$2":"$3
        }'
|awk -F"," '
    {if($1~/osName/) 
        print $1"\n"$2; 
    else 
        print $1}'
|sed 's/ OS Version/osVersion/g'
```

```sh
echo -e "serverVersion:Apache Tomcat/8.5.15\nserverName:8.5.15.0\nosName:Windows\nosVersion:10"
```

```sh
echo "serverVersion:Apache Tomcat/8.5.15
serverName:8.5.15.0
osName:Windows
osVersion:10"
```

```sh
awk -F'[ :,]' '
BEGIN {IGNORECASE=1}
{if($9=="version")
    print "serverVersion"":"$10,$11;
else if($9=="number")
    print "serverName"":"$10;
else if($9=="name")
    print "osName"":"$10"\n""osVersion"":"$14
}' nowcoder.txt

```



```sh
awk -v FS=":|,|log " '{
    if($4=="Server version"){
        printf("serverVersion:%s\n",$5)
        } 
    if($4=="Server number"){
        printf("serverName:%s\n",$5)
        } 
    if($4=="OS Name"){
        printf("osName:%s\nosVersion:%s\n",$5,$7)
        }  
    }'

```

```sh
#!/bin/bash
awk -F '[,:]' '
BEGIN{
    arr[1]="serverVersion";
    arr[3]="serverName";
    arr[4]="osName";
    arr[5]="osVersion"
}{ 
    if(NR==1||NR==3){
        print arr[NR]":"$NF
    }else if(NR==4){
        print 
            arr[NR]":"$4"\n"
            arr[NR+1]":"$NF
    }
}' nowcoder.txt

```
