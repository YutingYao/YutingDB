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
awk -F" " '{for(i=1;i<=NF;i++){if(length($i) < 8){print $i}}}' nowcoder.txt
awk -F " " '{for(i=1;i<=NF;i++){if(length($i) < 8) print $i}}' nowcoder.txt


awk 'BEGIN{FS="";RS=" ";ORS="\n"}{if(NF<8)print$0}' nowcoder.txt
awk '{for(i=1;i<=NF;i++)if(length($i)<8)print $i;}' nowcoder.txt
awk 'BEGIN{RS="[[:space:]]+"}length($0)<8{print $0}' nowcoder.txt

awk '{for(i=1;i<=NF;i++) if(length($i)<8) print $i}' nowcoder.txt
cat nowcoder.txt | awk '{for(i=1;i<=NF;i++) if(length($i)<8) print $i}'
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

###  9.1. <a name='awk-1'></a>awk的强大功能

```sh
cat $1|awk -F ' ' '{(sum=sum+$6)}END{print sum}'
awk -F 指定空格做分割符，对第6列内存求和
```

```sh
都是利用awk的强大功能，由第一行开始读写，读到最后一行结束

awk '{a+=$6}END{print a}'
awk '{sum += $1} END {print sum}'< <(ps -u <account> -o pmem)
awk '{sum+=$6} END {print sum}' nowcoder.txt
awk '{sum+=$6} END {print sum}' nowcoder.txt
awk 'BEGIN{sum=0}{sum+=$6}END{print sum}' nowcoder.txt
awk 'BEGIN{sum=0}{sum+=$6}END{print sum}' nowcoder.txt
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

    awk '{for(i=1;i<=NF;i++) a[$i]+=1}END{for(x in a) print x,a[x]}' nowcoder.txt
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
}
END {
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

###  11.1. <a name='awk-1'></a>awk 命令

```sh
cat  $1 |awk '{print $2}'  |sort  |uniq -c|sort |grep -v 1
cat nowcoder.txt | awk '{print $2}' | sort |uniq -c | sort -n | awk -v OFS=" " '{if($1>1) print $1,$2}'
cat nowcoder.txt | awk '{print $2}' | sort |uniq -cd | sort -n
awk '{print $2}' nowcoder.txt | sort | uniq -cd | sort -n
awk '{a[$2]++} END{for(i in a) {if(a[i]>=2){print a[i]" "i}}}' nowcoder.txt 
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