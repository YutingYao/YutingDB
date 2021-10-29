## 2019-javac

```s
题目描述
输入任意个字符串，将其中的小写字母变为大写，大写字母变为小写，其他字符不用处理；
输入描述: 
任意字符串：abcd12#%XYZ 
输出描述: 
输出字符串：ABCD12#%xyz
示例1：
输入： abcd12#%XYZ 
输出： ABCD12#%xy
```

```java
import java.util.Scanner;
public class Main {
 public static void main(String[] args) {
 Scanner sc = new Scanner(System.in); 
 String str1 = sc.nextLine();
 System.out.println(changeStr(str1)); 
 }
 public static String changeStr(String str){ 
 str.toUpperCase(); 
 char[] ch = str.toCharArray(); 
 int a = 'A'-'a'; //获得大小写之间差值 
 for(int i = 0; i < ch.length; i++){ 
 if('a' <= ch[i] && ch[i] <= 'z'){ 
 ch[i] = (char)(ch[i]+a); 
 }else if('A' <= ch[i] && ch[i] <= 'Z'){ 
 ch[i] = (char)(ch[i]-a); 
 } 
 } 
 String s=new String(ch);
 return s; 
 } 
}
```
## 2019-javac

```s
小偷来到了一个神秘的王宫，突然眼前一亮，发现5个宝贝，每个宝贝的价值都不一样，且重量也不一样，
但是小偷的背包携带重量
有限，所以他不得不在宝贝中做出选择，才能使偷到的财富最大，请你帮助小偷计算一下。
输入描述: 
宝贝价值：6,3,5,4,6 宝贝重量：2,2,6,5,4 
小偷背包容量：10 
输出描述: 
偷到宝贝的总价值：15
示例1 
输入
6,3,5,4,6 
2,2,6,5,4 
10

输出
15
```

```java
import java.util.Scanner;
public class Main {
public static void main(String[] args) {
 Scanner sc = new Scanner(System.in); 
 String str1 = sc.next();
 String str2 = sc.next();
 String str3 = sc.next();
 int[] value = transform(str1);
 int[] weight = transform(str2);
 int[] bag = transform(str3); 
 sc.close();
 int m = bag[0];
 int n = value.length;
 int w[] = weight;
 int p[] = value;
 int c[][] = BackPack_Solution(m, n, w, p);
 System.out.println(c[c.length-1][c[0].length-1]);
}
 public static int[][] BackPack_Solution(int m, int n, int[] w, int[] p) {
 //c[i][v]表示前i件物品恰放入一个重量为m的背包可以获得的最大价值
 int c[][] = new int[n + 1][m + 1];
 for (int i = 0; i < n + 1; i++)
 c[i][0] = 0;
 for (int j = 0; j < m + 1; j++)
 c[0][j] = 0;
 for (int i = 1; i < n + 1; i++) {
 for (int j = 1; j < m + 1; j++) {
 //当物品为i件重量为j时，如果第i件的重量(w[i-1])小于重量j时，c[i][j]为下列两种情况之
一：
 //(1)物品i不放入背包中，所以c[i][j]为c[i-1][j]的值
 //(2)物品i放入背包中，则背包剩余重量为j-w[i-1],所以c[i][j]为c[i-1][j-w[i-1]]的值加上当
前物品i的价值
 if (w[i - 1] <= j) {
 if (c[i - 1][j] < (c[i - 1][j - w[i - 1]] + p[i - 1]))
 c[i][j] = c[i - 1][j - w[i - 1]] + p[i - 1];
 else
 c[i][j] = c[i - 1][j];
 } else
 c[i][j] = c[i - 1][j];
 }
 }
 return c;
 }
private static int[] transform(String str1) {
 String[] split = str1.split("：");
 String[] split2 = split[split.length-1].split(",");
 int[] value=new int[split2.length ]; 
 for (int i = 0; i < value.length; i++) {
 value[i]=Integer.parseInt(split2[i]);
 }
 return value;
}
```

## 2018软件

```s
题目描述
输入两个字母串，将两个字母串都包含的字母用'_'替换后，输出两个字母串的剩余部分。

输入描述:输入两个字符串，字符串最大长度为100。字符串只包含字母，不可能为空串，区分大小写。
输出描述:按字符串顺序输出处理后的字符串
示例：
输入

abcd
bdef
1
2
输出

a_c_
__ef
1
2
解题思路
采用哈希思想。首先遍历字母串a，记录其中出现的字母，随后遍历字母串b，将重复出现的字母标记为100并替换成_，随后再遍历a，替换重复出现的字母。
```

```c++
#include<iostream>
using namespace std;
const int SIZE = 100;
int main()
{
	char a[SIZE];
	char b[SIZE];
	int low_alpha[26] = { 0 };
	int high_alpha[26] = { 0 };
	cin.getline(a, SIZE);
	cin.getline(b, SIZE);
	int i = 0; int j = 0;
	while (a[i] != '\0')
	{
		if (a[i] >= 97)
			low_alpha[a[i] - 97]++;
		else
			high_alpha[a[i] - 65]++; 
		i++;
	}
	while (b[j] != '\0')
	{
		if (b[j] >= 97)
		{
			if (low_alpha[b[j] - 97] > 0)
			{
				low_alpha[b[j] - 97] = 100;
				b[j] = '_';
			}
			else
				low_alpha[b[j] - 97]++;
		}
		else
		{
			if (high_alpha[b[j] - 65] > 0)
			{
				high_alpha[b[j] - 65] = 100;
				b[j] = '_';
			}
			else
				high_alpha[b[j] - 65]++;
		}
		j++;
	}
	i = 0;
	while (a[i] != '\0')
	{
		if (a[i] >= 97)
		{
			if (low_alpha[a[i] - 97] ==100)
			{
				a[i] = '_';
			}
		}
		else
			if(high_alpha[a[i] - 65]==100)
				a[i] = '_';
		i++;
	}
	cout << a << endl;
	cout << b << endl;
	return 0;

```

## 2018软件

```s
题目描述
一本正式出版的图书都有一个ISBN号码与之对应，ISBN码包括9位数字、1位识别码和3位分隔符，其规定格式如“x-xxx-xxxxx-x”，其中符号“-”是分隔符（键盘上的减号），最后一位是识别码，例如0-670-82162-4就是一个标准的ISBN码。ISBN码的首位数字表示书籍的出版语言，例如0代表英语；第一个分隔符“-”之后的三位数字代表出版社，例如670代表维京出版社；第二个分隔之后的五位数字代表该书在出版社的编号；最后一位为识别码。识别码的计算方法如下：
首位数字乘以1加上次位数字乘以2……以此类推，用所得的结果mod 11，所得的余数即为识别码，如果余数为10，则识别码为大写字母X。
例如ISBN号码0-670-82162-4中的识别码4是这样得到的：对067082162这9个数字，从左至右，分别乘以1，2，…，9，再求和，即
0×1+6×2+……+2×9=158 0×1+6×2+……+2×9=1580×1+6×2+……+2×9=158，
然后取158 mod 11的结果4作为识别码。
你的任务是编写程序根据输入的ISBN号码的前3段，计算出识别码，并输出完整的ISBN码。

输入描述:
为一个ASCII字符串。内容为ISBN码的前三段，以上面为例，就是0-670-82162。

输出描述:
若判断输入为合法的字符串，则计算出识别码，并输出完整的ISBN码；
若输入不合法，则输出字符串”ERROR”；

示例1
输入：

0-670-82162
1
输出：

0-670-82162-4
1
解题思路
逻辑较为简单，仅给出合理输入的实现代码
```

```java
isbn_ini=input()
isbn=isbn_ini.replace("-",'')
sums=0
for i in range(len(isbn)):
    sums+=eval(isbn[i])*(i+1)
if sums%11==10:
    last="X"
else:
    last=str(sums%11)
print(isbn_ini+'-'+last)
```

## 2018软件

```s
题目描述
主机名由多级域名组成，自右向左，依次是顶级域名、二级域名、三级域名……以此类推

例，主机名：google.com.hk
　　hk是顶级域名
　　com是二级域名
　　google是三级域名

现在我们需要实现一个主机名的排序功能
排序规则

主机名按照域名等级排序，即先按照顶级域名排序，顶级域名相同的再按照二级域名排序，顶级和二级域名均相同的再按照三级域名排序，以此类推，直到整个主机名排序完毕
如果短主机名是由长主机名从顶级域名开始的连续一个或多个域名组成，短主机名排在长主机名前面。例：google.com 排在gmail.google.com 之前
每一级域名按照字典顺序排序，字典顺序定义见下面：
（1）两个单词(字母按照自左向右顺序)先以第一个字母作为排序的基准，如果第一个字母相同，就用第二个字母为基准，如果第二个字母相同就以第三个字母为基准。依此类推，如果到某个字母不相同，字母顺序在前的那个单词顺序在前。
例：abc 排在 abf 之前
（2）如果短单词是长单词从首字母开始连续的一部分，短单词顺序在前。例：abc 排在 abcd 之前
输入确保符合以下规则（无需检查）
主机名以字符串形式给出，非空串
主机名中仅包含小写英文字母和分隔符’.’
主机名中没有连续的’.’，不以’.’开始，也不以’.’结束
主机名不存在重复
示例1
输入

mail.huawei.com|huawei.com|teltalk.org|google.com.hk|imail.huawei.com
1
输出

huawei.com|imail.huawei.com|mail.huawei.com|google.com.hk|teltalk.org
1
解题思路
建立域名类，重写排序
```

```py
class Domain:
    def __init__(self,s):
        ls=s.split(".")
        self.top=ls[-1]
        self.num=len(ls)
        self.mid=''
        self.third=''
        if self.num==2:
            self.mid=ls[0]
        elif self.num==3:
            self.mid = ls[1]
            self.third=ls[0]
        else:
            pass

    def __gt__(self, other):
        if self.top!=other.top:
            return self.top>other.top
        else:
            if self.mid!=other.mid:
                return self.mid>other.mid
            else:
                if self.num!=other.num:
                    return self.num<other.num
                else:
                    return self.third>other.third
    def __str__(self):
        if self.num==3:
            return self.third+'.'+self.mid+'.'+self.top
        elif self.num==2:
            return self.mid+'.'+self.top
        else:
            return self.top

name=input().split("|")
domain=[Domain(x) for x in name]
domain.sort()
ans=list(map(str,domain))
print("|".join(ans))
```

## 2019软件

```s

输入描述：

1、忽略小数点，例如“A1.2”，认为包含整数1和2；

2、如果整数的左侧出现“-”，则奇数个数认为是负整数，偶数个数认为是正整数。例如AB-1CD--2EF---3“”,认为包含整数-1、2和-3。

输出描述：

输出即为字符串中所有整数数字之和。

```

```py
#coding=utf-8
import sys
if __name__ == "__main__":
    def sum_of_int(s):
        sums, num, pos = 0, 0, 1
        if s == None:
            return 0
        for i in range(len(s)):
            if 48 <= ord(s[i]) <= 57:
                num = num * 10 + int(s[i])*pos
            else:
                sums += num
                num = 0
                if s[i] == '-':
                    if i-1 > -1 and s[i-1] == '-':
                        pos = -pos
                    else:
                        pos = -1
                else:
                    pos = 1
        sums=sums+num
        return sums 
    e=sys.stdin.readline().strip()
    result=sum_of_int(e)
    print (result)

```

## 2019软件

```s
多项式卷积乘法

C(n) = A(n)*B(n)

多项式系数[b(2) b(1) b(0)] = [1 2 5]

[c(3) c(2) c(1) c(0)] = [1 3 7 5]

c(0) = a(0)b(0)

c(1) = a(0)b(1)+a(1)b(0)

c(2)=a(0)b(2)+a(1)b(1)+a(2)b(0)

c(3)=a(0)b(3)+a(1)b(2)+a(2)b(1)+a(3)b(0)

其中 ：a(3)=a(2)=b(3=0)

```

```py
#coding=utf-8
 
 
def juanji():
    a_xishu=[]
    b_xishu=[]
    for i in range(5):
        one=[]
        one.append(int(input()))
        one.append(int(input()))
        a_xishu.append(one)
 
    for i in range(5):
        two=[]
        two.append(int(input()))
        two.append(int(input()))
        b_xishu.append(two)
    a_xishu=a_xishu[::-1]
    b_xishu = b_xishu[::-1]
 
    a_len=len(a_xishu)
    b_len=len(b_xishu)
    for i in range(9)[::-1]:
        ci_s=0
        ci_x=0
        for index in range(i+1)[::-1]:
            if i-index<a_len and index<b_len:
                temp=fushumulity(a_xishu[i-index],b_xishu[index])
                ci_s+=temp[0]
                ci_x+=temp[1]
 
        print(ci_s)
        print(ci_x)
 
def fushumulity(x,y):
    s=x[0]*y[0]-x[1]*y[1]
 
    x=x[0]*y[1]+x[1]*y[0]
    return s,x
 
juanji()
 

```

## 软件2019-野蛮生长的牛

```py
#coding=utf-8
import sys
if __name__ == "__main__":
    # 读取第一行的n
    def f(m,n):
        if n == 0:
            return m
        if n == 1:
            return 2*m
        if n == 2:
            return 3*m
        if n == 3:
            return 4*m
        return f(m,n-1) + f(m,n-4)
    n = int(sys.stdin.readline().strip())
    for i in range(n):
        M = int(sys.stdin.readline().strip())
        N = int(sys.stdin.readline().strip())
        print(f(M,N))

```

## 2021-矩阵的旋转

```s
一个nXn阶的矩阵，第一个位置上的人喊1，延矩阵顺时针报号依次加1，若十位是奇数且各位是7，则为特种兵，请返回特种兵的位置。
思路：基于第三题的思想：螺旋打矩阵
递归解决：
螺旋矩阵用二维数组表示,坐标(x,y),即(x轴坐标，y轴坐标)
顺时针螺旋的方向是->右,下,左,上,用数值表示即是x加1格(1,0),y加1格(0,1),x减1格(-1,0),y减1格(0,-1)
坐标从(0,0)开始行走,当超出范围或遇到障碍时切换方向
```

```s
cycle(iterable)
创建一个迭代器，对iterable中的元素反复执行循环操作，内部会生成iterable中的元素的一个副本，此副本用于返回循环中的重复项。
cycle(“abc”) #重复序列的元素，既a, b, c, a, b, c …
使用for或者__next__()调用里面的元素。
```

```python
import itertools
import numpy as np
#递归解决
def spiral(m,n):
    ans=[]#存储矩阵
    result=[]#存储特种兵的索引
    _status = itertools.cycle(['right','down','left','up'])#用于状态周期性的切换
    _movemap = {
        'right':(1,0),
        'down':(0,1),
        'left':(-1,0),
        'up':(0,-1),
    }
    pos2no = dict.fromkeys([(x,y) for x in range(m) for y in range(n)])
    #{(0, 0): None,
    # (0, 1): None,
    # (1, 0): None,
    # ....
    # (m-1, n-1): None}
    _pos = (0,0)#当前位置
    _st = next(_status)#⽤next()函数来获取迭代器的下⼀条数据
    for i in range(1,m*n+1):
        _oldpos = _pos#旧位置
        #_pos=(0,0),_movemap['right']=(1,0)
        #tuple(map(sum,zip((2,3),(3,4)))):(5,7)实现两个tuple对应位置相加
        _pos = tuple(map(sum,zip(_pos,_movemap[_st])))#根据状态下一次该到的位置
        if (_pos not in pos2no) or (pos2no[_pos]):#当超出范围或遇到障碍时（此位置已有值）切换方向
            _st = next(_status)#切换下一次的状态
            _pos = tuple(map(sum,zip(_oldpos,_movemap[_st])))
        pos2no[_oldpos] = i
        #根据口号规则找出特种兵的位置
        if i//10%2!=0 and i%10==7:
            result.append(_oldpos[::-1])
    for i in range(m):
        ans1=[]
        for j in range(n):
            ans1.append(pos2no[(j,i)])
        ans.append(ans1)
    return ans, result
a,b=spiral(10,10)
print('特种兵:',b)
print('矩阵:')
```

## 2021-顺时针打印矩阵

```s
输入一个矩阵，按照从外向里以顺时针的顺序依次打印出每一个数字，例如，如果输入如下4 X 4矩阵： 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 则依次打印出数字1,2,3,4,8,12,16,15,14,13,9,5,6,7,11,10.
```

```s
思想： 可以模拟魔方逆时针旋转的方法，一直做取出第一行的操作。
在turn中，for循环(行列互换)将矩阵[[5,6,7,8],[9,10,11,12],[13,14,15,16]]变成矩阵[[5,9,13],[6,10,14],[7,11,15],[8,12,16]]，在进行reversr操作变为,[[8,12,16],[7,11,15],[6,10,14]，[5,9,13]]，第一行即为要输出的元素。
```

```py
class Solution:
    # matrix类型为二维列表，需要返回列表
    def printMatrix(self, matrix):
        # write code here
        result = []
        while(matrix):
            result+=matrix.pop(0)#抛出第一行
            if not matrix or not matrix[0]:
                break
            matrix = self.turn(matrix)
        return result
        #旋转矩阵使得第一行为所需元素
    def turn(self,matrix):
        num_r = len(matrix)
        num_c = len(matrix[0])
        newmat = []
        for i in range(num_c):
            newmat2 = []
            for j in range(num_r):
                newmat2.append(matrix[j][i])
            newmat.append(newmat2)
        newmat.reverse()#行颠倒交换
        return newmat
```

## 2021-螺旋矩阵

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.sa19xmlcykw.png)

```s
思路：
1.构建斐波那契数列
2.递归解决：
螺旋矩阵用二维数组表示,坐标(x,y),即(x轴坐标，y轴坐标)
顺时针螺旋的方向是->右,下,左,上,用数值表示即是x加1格(1,0),y加1格(0,1),x减1格(-1,0),y减1格(0,-1)
坐标从(0,0)开始行走,当超出范围或遇到障碍时切换方向
```

```py
import itertools
import numpy as np
#1.构建斐波那契数列
def Fibonacci(n):
    if n <= 0:
        return 0
    a = b = 1
    res=[1,1]
    for i in range(2,n):
        a,b=b,a+b
        res.append(b)
    return res

#2.递归解决
def spiral(n,arr):
    _status = itertools.cycle(['right','down','left','up'])#用于状态周期性的切换
    _movemap = {
        'right':(1,0),
        'down':(0,1),
        'left':(-1,0),
        'up':(0,-1),
    }
    pos2no = dict.fromkeys([(x,y) for x in range(n) for y in range(n)])
    #{(0, 0): None,
    # (0, 1): None,
    # (1, 0): None,
    # (1, 1): None}
    _pos = (0,0)
    _st = next(_status)#⽤next()函数来获取迭代器的下⼀条数据
    #每次对旧位置赋值，并判断下一个位置怎么走
    for i in range(1,n*n+1):
        _oldpos = _pos
        #_pos=(0,0),_movemap[_st]=(1,0)
        #zip():0,1;0,0，对应位置求和
        _pos = tuple(map(sum,zip(_pos,_movemap[_st])))#根据状态进行移动（1，0）
        if (_pos not in pos2no) or (pos2no[_pos]):#当超出范围或遇到障碍时(位置上已有数字)切换方向
            _st = next(_status)
            _pos = tuple(map(sum,zip(_oldpos,_movemap[_st])))
        pos2no[_oldpos] = arr[i-1]
    return pos2no
 
def display_spiral(n):
    ans=[]
    arr=Fibonacci(n)
    n1=int(np.sqrt(n))
    pos2no = spiral(n1,arr)
    for i in range(n1):
        ans1=[]
        for j in range(n1):
            ans1.append(pos2no[(j,i)])
        ans.append(ans1)
    return ans
matrix=display_spiral(9)
print('螺旋矩阵:',matrix)
```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1nniks4x0b0g.png)

## 2021-堆积木

```s
两个数字字符串a,b，a是不超过10位，b是1-9位，a代表下面已有的积木，b代表上面落下的积木，两者尽可能的抵消，请问最终留下最少的行数。
```

```s
思路：
1. 判断a,b字符串的长度
2. 通过两个字符串相加（需要移动根据1的条件），使得最大值-最小值的差最小
2.1 当a/b长度为1时，只需要找出b/a最小元素的位置+a/b，在计算新列表的最大值-最小值
2.2 当a与b的长度相等时，两者直接对应元素相加，在计算新列表的最大值-最小值
2.3 当a,b长度都大于1，且不相等时，需要来回移位，找出最小的那个
```

```py
#输入
#a=[2,2,0,2]
#b=[2]
#输出：0
aa = list(map(int, input().split()))
bb =list(map(int, input().split()))
a=[]
b=[]
for i in range(len(aa)):
    a.append(int(aa[i]))
for i in range(len(bb)):
    b.append(int(bb[i]))
def listsum(a,b):
    for i in range(len(a)):
        a[i]=a[i]+b[i]
    return a
def fun(a,b):
    sb=len(b)
    sa=len(a)
    #2.0特殊情况
    if not b:
        return max(a)
    if not a:
        return max(b)
    if not a and not b:
        return 0
    #2.1长度为1
    if sb==1:
        c=min(a)
        sb=a.index(c)
        a[sb]=c+b[0]
        return max(a)-min(a)
    if sa==1:
        c=min(b)
        sb=b.index(c)
        b[sb]=c+a[0]
        return max(b)-min(b)
    #2.2长度相等
    if sb==sa:
        a=listsum(a,b)
        return max(a)-min(a)
    #2.3长度大于1，且不相等
    if sb>1 and sa>1 and sb<sa:
        ans=[]
        for i in range(sa-sb+1):
            c=listsum(a[i:i+sb],b)
            c=c+a[(i+2):]+a[0:i]
            ans.append(max(c)-min(c))
        return min(ans) 
    if sb>1 and sa>1 and sb>sa:
        ans=[]
        for i in range(sb-sa+1):
            c=listsum(b[i:i+sa],a)
            c=c+b[(i+2):]+b[0:i]
            ans.append(max(c)-min(c))
        return min(ans) 
print(fun(a,b))
```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1reid76rb9c0.png)

## 2021-软件

```s
学校有一在线学习系统，每门课程由N（0<N<=10000）个页面组成，学生从第一页开始按顺序学到最后一页提交学习记录。系统会记录每页停留的时间（单位 秒），当学生提交时会分析时间是否满足要求，分析规则如下：
每分钟（60s）之内学完的页数不能大于4页。

比如系统记录到某学生页面停留时间如下：
10 120 10 20 10 10 50
该学生第一分钟学完1页，第2分钟学完0页，第3分钟学完5页，第4分钟学完1页，则判定为不及格。
```

```py
def check(time_list):
    time_list = time_list[1:]
    all = 0
    for i in time_list:
        all += i
        
    if len(time_list)<=4:   #总的完成页数不大于4
        check = 1
        return check
    
    if all < 60 and len(time_list)>4:   #前1分钟内完成页数大于4
        check = 0
        return check
    
    if all >60 and len(time_list)>4:         #总的阅读时间大于1分钟 
        for i in range(len(time_list)):  
            for j in range((len(time_list)-i)//4):   #遍历相邻每4页的阅读时间并求和

                ervey_4page_time = time_list[4*j+i] + time_list[4*j+i+1] +time_list[4*j+i+2] + time_list[4*j+i+3]
                
                if ervey_4page_time <60:   #判断这相邻4页的阅读时间是否小于60
                    check = 0
                    return check
                else:
                    check = 1
                    
    return check
                

if __name__ == "__main__":
    #time = [8, 10, 120, 10, 10, 10, 10, 10, 10]
    time = [8, 10, 120, 10, 10,20,50]
    time = [8, 10, 120, 10, 10,20,30,50]
    time = [3,4,10,5,1,1,1,1,1]
    
    print(check(time))

```

## 2021-软件

```s
学校有一个在线学习系统，没门课程由N个页面组成（0< N<= 10000）个页面组成，学生从第一页开始按顺序学到最后一页提交学习记录。
系统会记录每页停留的时间（单位秒），当学生提交时会分析时间是否满足要求，分析规则如下：
每分钟（60秒）之内学完的页面不能大于4页。
例如系统记录到某学生页面停留时间如下：
10 120 10 20 10 10 50
该学生第一分钟学完1页，第二分钟学完0页，第三分钟学完5页，第四分钟学完1页，则判定为不满足要求。
输入描述：
输入数据包含多个学生的提交记录，第一行是整数表示记录个数，每个记录占一行，每行的第一个数是N，表示页面数，然后是每个页面的学习时间。
输出描述：
对于每组输入数据，满足输出1，不满足输出0，每组输出占一行。
输入
5
2 10 10
4 10 15 20 30
5 10 10 10 10 10
6 10 20 20 70 10 10
8 10 120 10 10 10 10 10 10
输出：
1
1
0
1
0
思路
统计每一分钟，翻过的页数，超过60秒时，将时间与60取模，且页数归0，小于60就累加页数，大于4就返回0，否则返回1
```

```java
import java.util.Scanner;
import java.util.ArrayList;
public class Main {

    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        ArrayList<Integer> ans = new ArrayList<>();//定义一个数组，存放所有行的结果值
        int input=in.nextInt();//第一行数字为结果值的行数
        for(int i=0;i<input;i++){
            int n;
            ArrayList<Integer> numbers = new ArrayList<>();//存放该行的所有页面的学习时间
            n=in.nextInt();//表示该行的所学页数
            for(int j=0;j<n;j++){
                numbers.add(in.nextInt());
            }
            ans.add(process(numbers));
        }
        for(Object it:ans){
            System.out.println((Integer)it);
        }
    }

    public static int process(ArrayList arrayList){
        int pages = 0, sum = 0;
        for (Object it:arrayList) {//遍历学习时间数组
            sum += (Integer) it;//时间相加
            if (sum > 60) {//当学习时间和大于60
                sum %= 60;//求余
                pages = 0;//所学页数归零
            }
            ++pages;//页数加一
            if (pages > 4) {//大于4页返回0
                return 0;
            }
        }
        return 1;
    }
}

```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3gu3yspmaj80.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2fcd5u2n1eo0.png)

## 2021-软件

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5oq58a9y1ig0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3hxeaxenq620.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2apgvzkj93pc.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5j8mjxhw4l00.png)

## 华为2017

```s
输入一个正整数X，在下面的等式左边的数字之间添加+号或者-号，使得等式成立。
1 2 3 4 5 6 7 8 9 = X
比如：
12-34+5-67+89 = 5
1+23+4-5+6-7-8-9 = 5
请编写程序，统计满足输入整数的所有整数个数。
输入： 正整数，等式右边的数字
输出： 使该等式成立的个数
样例输入：5
样例输出：21
```

```c++
#include<iostream>
#include<cmath>
using namespace std;
//动态规划：每个符号位分为三种q情况：加号，减号，无(合成数字)
int dp(int before,int des,int res,int ex)
{
	if(before==0)
	{//当前符号前数字个数==0
		if(des==res)
		{
			return 1;
		}
		else
			return 0;
	}
	return dp(before-1,before,res-des,1)+dp(before-1,before,res+des,1)+dp(before-1,before*pow(10,ex)+des,res,ex+1);
}
	
int main()
{
	int n;
	cin>>n;
	int count=dp(8,9,n,1);
	cout<<count<<endl;
	return 0;
}

```

## 华为2017

```s
简要描述：
给定一个M行N列的矩阵（M*N个格子），每个格子中放着一定数量的平安果。
你从左上角的各自开始，只能向下或者向右走，目的地是右下角的格子。
每走过一个格子，就把格子上的平安果都收集起来。求你最多能收集到多少平安果。
注意：当经过一个格子时，需要一次性把格子里的平安果都拿走。
限制条件：1<N,M<=50；每个格子里的平安果数量是0到1000（包含0和1000）.

输入描述：
输入包含两部分：
第一行M, N
接下来M行，包含N个平安果数量

输出描述：
一个整数
最多拿走的平安果的数量

示例：

输入
2 4
1 2 3 40
6 7 8 90

输出
136
```

```s
思路：动态规划
动态方程：当前位置能够获得的最大苹果数=max(从上面走能够获得最大苹果+从左边走能获得最大苹果）
dp(0,0)=app[0][0]
```

```c++
#include<iostream>
using namespace std;
int Map[50][50];
int maxnum=0;
void dp(int r,int c,int cost)
{
	if(r==0&&c==0)
	{
		maxnum=cost+Map[0][0];
		return;
	}
    if(r-1>=0&&c-1>=0)
	{
		if(Map[r][c-1]>Map[r-1][c])
			dp(r,c-1,cost+Map[r][c]);
		else
			dp(r-1,c,cost+Map[r][c]);
	}
	if(r==0&&c>0)
	{
		dp(r,c-1,cost+Map[r][c]);
	}
	if(c==0&&r>0)
	{
      	dp(r-1,c,cost+Map[r][c]);
	}
}



        
int main()
{
	int m,n;
	cin>>m>>n;
	for(int i=0;i<m;i++)
	{
		for(int j=0;j<n;j++)
		{
			 cin>>Map[i][j];
		}
	}
	dp(m-1,n-1,0);
	cout<<maxnum<<endl;
	return 0;
}
```

## 华为od外包

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5rue6xz857w0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1kxqop44ulhc.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.18lrm52q5utc.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4j19ayzlhfu0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.27lm3rm73hc0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.38ef4d7oegu.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.19ze5941a540.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.429rt9ouga20.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2vmwyqa4t3s0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3qlx7042o860.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.58kwuan92rk0.png)

## 2020机试逻辑

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1pkcbpt3m3c0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.101rs3gc1o40.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6ujgo0pn3x80.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.78j3tubh9ns0.png)

## 2020软件

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4ph52zmuw2k0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.45hgu966shu0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1kol110h7hnk.png)

