## 2014

```s
通过键盘输入一串小写字母(a~z)组成的字符串。请编写一个字符串过滤程序，若字符串中出现多个相同的字符，将非首次出现的字符过滤掉。
比如字符串“abacacde”过滤结果为“abcde”。
要求实现函数：void stringFilter(const char *pInputStr, long lInputLen, char *pOutputStr);
【输入】 pInputStr：  输入字符串
        lInputLen：  输入字符串长度         
【输出】 pOutputStr： 输出字符串，空间已经开辟好，与输入字符串等长； 
【注意】只需要完成该函数功能算法，中间不需要有任何IO的输入输出
示例 
输入：“deefd”        输出：“def”
输入：“afafafaf”     输出：“af”
输入：“pppppppp”     输出：“p”
main函数已经隐藏，这里保留给用户的测试入口，在这里测试你的实现函数，可以调用printf打印输出
当前你可以使用其他方法测试，只要保证最终程序能正确执行即可，该函数实现可以任意修改，但是不要改变函数原型。
一定要保证编译运行不受影响
```

```c++
#include <iostream>    
#include <cassert>    
    
using namespace std;    
    
bool g_flag[26];    
void stringFilter(const char *pInputStr, long lInputLen, char *pOutputStr)    
{    
  assert(pInputStr != NULL);    
  int i = 0;    
  if (pInputStr == NULL || lInputLen <= 1)    
  {    
      return;    
  }    
  const char *p = pInputStr;    
  while(*p != '\0')    
  {    
     if (g_flag[(*p - 'a')])    
     {    
         p++;    
     }else{    
         pOutputStr[i++] = *p;    
         g_flag[*p - 'a'] = 1;    
         p++;    
     }    
  }    
  pOutputStr[i] = '\0';    
}    
int main()    
{    
    memset(g_flag,0,sizeof(g_flag));    
    char input[] = "abacacde";    
    char *output = new char[strlen(input) + 1];    
    stringFilter(input,strlen(input),output);    
    cout<<output<<endl;    
    delete output;    
    return 0;    
}    

```

## 2014

```s
通过键盘输入一串小写字母(a~z)组成的字符串。请编写一个字符串压缩程序，将字符串中连续出席的重复字母进行压缩，并输出压缩后的字符串。
压缩规则：
1、仅压缩连续重复出现的字符。比如字符串"abcbc"由于无连续重复字符，压缩后的字符串还是"abcbc"。
2、压缩字段的格式为"字符重复的次数+字符"。例如：字符串"xxxyyyyyyz"压缩后就成为"3x6yz"。
要求实现函数： 
void stringZip(const char *pInputStr, long lInputLen, char *pOutputStr);
【输入】 pInputStr：  输入字符串
            lInputLen：  输入字符串长度
【输出】 pOutputStr： 输出字符串，空间已经开辟好，与输入字符串等长；
【注意】只需要完成该函数功能算法，中间不需要有任何IO的输入输出
示例 
输入：“cccddecc”   输出：“3c2de2c”
输入：“adef”     输出：“adef”
输入：“pppppppp” 输出：“8p”
```

```c++
#include <iostream>    
#include <cassert>    
    
using namespace std;    
    
void stringZip(const char *pInputStr, long lInputLen, char *pOutputStr)    
{    
  const char *p = pInputStr;    
  int num = 1;    
  int i = 0;    
  p++;    
  while(*p != NULL)    
  {    
      while(*p == *(p-1)&& *p != NULL)    
      {    
       num++;    
       p++;    
      }    
      if (num > 1)    
      {    
           int size = 0;    
           int temp = num;    
           while(num)             //计算位数    
           {    
             size++;    
             num /= 10;    
           }    
           num = 1;    
    
           for (int j = size; j > 0; j--)    
           {    
               pOutputStr[i+j-1] = '0'+ temp%10;    
               temp /= 10;    
           }    
           i +=size;    
           pOutputStr[i++] = *(p-1);    
           p++;    
      }else{    
          pOutputStr[i++] = *(p-1);    
          p++;    
      }    
  }    
  pOutputStr[i] = '\0';    
}    
    
int main()    
{    
    char input[] = "cccddecc";    
    char *output = new char[strlen(input) + 1];    
    stringZip(input,strlen(input),output);    
    cout<<output<<endl;    
    return 0;    
}    

```

## 2014

```s
通过键盘输入100以内正整数的加、减运算式，请编写一个程序输出运算结果字符串。
输入字符串的格式为：“操作数1 运算符 操作数2”，“操作数”与“运算符”之间以一个空格隔开。
补充说明：
1、操作数为正整数，不需要考虑计算结果溢出的情况。
2、若输入算式格式错误，输出结果为“0”。
要求实现函数： 
void arithmetic(const char *pInputStr, long lInputLen, char *pOutputStr);
【输入】 pInputStr：  输入字符串
            lInputLen：  输入字符串长度         
【输出】 pOutputStr： 输出字符串，空间已经开辟好，与输入字符串等长； 
【注意】只需要完成该函数功能算法，中间不需要有任何IO的输入输出
示例 
输入：“4 + 7”  输出：“11”
输入：“4 - 7”  输出：“-3”
输入：“9 ++ 7”  输出：“0” 注：格式错误
```

```c++
#include <iostream>    
    
using namespace std;    
    
void arithmetic(const char *pInputStr, long lInputLen, char *pOutputStr)    
{    
 const char *input = pInputStr;    
       char *output = pOutputStr;    
 int sum = 0;    
 int operator1 = 0;    
 int operator2 = 0;    
 char *temp = new char[5];    
 char *ope = temp;    
 while(*input != ' ') //获得操作数1    
 {    
     sum = sum*10 + (*input++ - '0');    
 }    
 input++;    
 operator1 = sum;    
 sum = 0;    
    
 while(*input != ' ')    
 {    
     *temp++ = *input++;    
 }    
    
 input++;    
 *temp = '\0';    
    
 if (strlen(ope) > 1 )    
 {    
     *output++ = '0';    
     *output = '\0';    
     return;    
 }    
    
 while(*input != '\0') //获得操作数2    
 {    
     sum = sum*10 + (*input++ - '0');    
 }    
 operator2 = sum;    
 sum = 0;    
    
 switch (*ope)    
 {    
 case '+':itoa(operator1+operator2,pOutputStr,10);    
     break;    
 case '-':itoa(operator1-operator2,pOutputStr,10);    
    break;    
 default:    
     *output++ = '0';    
     *output = '\0';    
     return;    
 }    
}    
    
int main()    
{    
    char input[] = "4 - 7";    
    char output[] = "    ";    
    arithmetic(input,strlen(input),output);    
    cout<<output<<endl;    
    return 0;    
}    
```

## 2014

输入1--50个数字，求出最小数和最大数的和

```c++
#include<stdio.h>    
#define N 50    
void sort(int a[],int n);    
int main(void)    
{        
    char str[100];    
    int a[N]={0};    
    gets(str);      //要点1：动态的输入1--50个整数，不能确定个数，只能用字符串输入，然后分离出来    
    int i=0;    
    int j=0;    
    int sign=1;    
    while(str[i]!='\0')    
    {    
        if(str[i]!=',')  //输入时要在半角输入    
        {    
    
            if(str[i] == '-')    //要点:2：有负整数的输入    
            {    
               // i++;   //易错点1    
                sign=-1;    
            }    
            else if(str[i]!='\0') //不用else的话，负号也会减去‘0’    
           {    
               a[j]=a[j]*10 + str[i]-'0'; //要点3：输入的可以是多位数    
    
           }    
        }    
        i++;    
        if(str[i]==',' || str[i]=='\0')  //这个判断是在i自加以后    
        {    
             a[j]=a[j]*sign;  //易错点2    
             sign=1;   ////易错点3    
             j++;    //j就是a数组的个数 范围0到j-1    
        }    
    
    
    }    
    
    sort(a,j);    
    printf("Max number + Min number = %d",a[0]+a[j-1]);    
    
    return 0;    
}    
void sort(int a[],int n)  //选择排序    
{    
    int i,j;    
    int k;    
    int temp;    
    for(i=0;i<n-1;i++)    
    {    
        k=i;    
        for(j=i+1;j<n;j++)    
        {    
            if(a[k]>a[j])    
                k=j;    
        }    
        if(i!=k)    
        {    
            temp = a[k];    
            a[k] = a[i];    
            a[i] = temp;    
        }    
    }    
    for(i=0;i<n;i++)    
        printf("%-5d",a[i]);    
    puts("");    
}    

```

## 2019

```s
第一题：读取输入字符串存储在数组中，将每个字符串按照8个字符切割，如果不足8个字符则自动补全为0，存储在新的字符串数组中，最后字符串按照升序输出。

测试样例：
输入：2 123456789 abc
输出：12345678 90000000 abc00000
（通过率只有80%，不知道为啥）
```

```java
import java.util.*;

public class Main {
    static List<String> list = new ArrayList<>();
    public static boolean  CompareString(String s1,String s2){
       char[] m1 = s1.toCharArray();
       char[] m2 = s2.toCharArray();
       for(int i = 0;i<8;i++){
           if(m1[i] >= m2[i]){
               return true;
           }
           else{//小于
               return false;
           }
       }
       return false;
    }
    public static String addzero(String result,int len){
        for(int i=0;i<len;i++){
            result += "0";
        }
        return result;
    }
    public static void splitby8( String result){
        while(result.length()>8){
            //继续切割
            int l = result.length();
            list.add(result.substring(0,8));
            result = result.substring(8,l);
        }
        if(result.length()>0){
            list.add(addzero(result,8-result.length()));
        }
    }
    public static void main(String[] args) {
        //如何实现升序？
        Scanner in = new Scanner(System.in);
        int num = Integer.valueOf(in.next());
        String[] s = new String[num];

//        String[] sresult = new String[num*13];
        for(int i=0;i<num;i++){
            s[i]=in.next();
            int sl = s[i].length();
            if(sl>8){//按照8个8个切割，直到没有8个
               splitby8(s[i]);
            }
            if(sl<8){
                //自动补全0
                list.add(addzero(s[i],8-sl));
            }
        }
        String[] sresult = new String[list.size()];
        for(int i =0;i<list.size();i++){
            sresult[i] = list.get(i);
        }
        //打印
        for(int i=0;i<sresult.length;i++){
            for(int j=i;j<sresult.length;j++){
            if(CompareString(sresult[i],sresult[j]))
                //大于则交换
                {
                    String temp = sresult[j];
                    sresult[j] = sresult[i];
                    sresult[i] = temp;
                }
            }
        }
        for(int i=0;i<sresult.length;i++){
            if(i==sresult.length -1){
                System.out.println(sresult[i]);
                break;
            }
            System.out.print(sresult[i]+" ");
        }
    }
}

```

## 2019

```s
第二题题目：输入字符串只包括字母，数字和括号（大括号，中括号和小括号），数字n后括号内的字符串需要重复n遍，括号内可能有数字和括号。数字与括号一一对应，不存在2a1(b)的形式。然后逆序输出字符串
（当时未通过，后来补充）
测试用例：
输入1：abc3(A)
输出1：AAAcba
输入2：a2{b6[3(ac)]mg}h
输出2：hgmcacacacacacacacacacacacabgmcacacacacacacacacacacacaba
```

```java
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class haha {
    //逆序字符数组
    public  static char[] reverseChar( char[] ch1){
        char[] ch2 = new char[ch1.length];
        int i =0;
        for(int j = ch1.length-1; j>=0;j--){
            ch2[i++] = ch1[j];
        }
        return ch2;
    }
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        String sum = in.next();
        char[] my = sum.toCharArray();
        String result = "";
        //得到正序，括号里面可以出现括号，肯定先处理里面括号
        int i =0;
        //如果有大括号
        if(sum.contains("{")){
            while(i<my.length){
                //考虑大括号
                //只考虑小括号
                if(my[i]>='0'&& my[i]<='9'){//遇到number需要处理
                    String mynum = "";
                    int mk = i;
                    while(my[mk] != '{'){
                        mynum += my[mk++];
                    }
                    int num = Integer.valueOf(mynum);
                    int h = mk+1;
                    String temp = "";
                    while(my[h] != '}'){
                        temp += my[h];
                        h++;
                    }
                    for(int k=0;k<num;k++) {
                        result += temp;
                    }
                    i = h+1;
                }
                else if(my[i]>='a'&&my[i]<= 'z'){//a-z
                    result += my[i++];
                }
                else if(my[i]>= 'A'&&my[i]<='Z'){//A-Z
                    result += my[i++];
                }
                else if(my[i] != '{'&&my[i] != '}'){
                    result += my[i++];
                }
                else{
                    i++;
                }
            }
        }
        //如果有中括号
        i = 0;
        if(result.isEmpty()) {
            result = sum;
        }
        if(sum.contains("[")) {
            char[] my1 = result.toCharArray();
            String result1 = "";
            while (i < my1.length) {
                if (my1[i] >= '0' && my1[i] <= '9') {//遇到number需要处理
                    String my1num = "";
                    int mk = i;
                    while(my1[mk] != '['){
                        my1num += my1[mk++];
                    }
                    int num = Integer.valueOf(my1num);
                    int h = mk+1;
                    String temp = "";
                    while (my1[h] != ']') {
                        temp += my1[h];
                        h++;
                    }
                    for (int k = 0; k <num; k++) {
                        result1 += temp;
                    }
                    i = h + 1;
                } else if (my1[i] >= 'a' && my1[i] <= 'z') {//a-z
                    result1 += my1[i++];
                } else if (my1[i] >= 'A' && my1[i] <= 'Z') {//A-Z
                    result1 += my1[i++];
                } else if (my1[i] != '[' && my1[i] != ']') {
                    result1 += my1[i++];
                }
                else{
                    i++;
                }
            }
            result = result1;
        }
        i = 0;
        if(result.isEmpty()) {
            result = sum;
        }
        char[] my2 = result.toCharArray();
        String result2 = "";
        //后处理小括号
        //如何找到括号最近的数字
        while(i<my2.length){
            //考虑大括号
            //只考虑小括号
            //需要确保是小括号前的数字
            //从括号后面往前推导出数字
            if (my2[i] > '0' && my2[i] <= '9') {//遇到number需要处理
                String my2num = "";
                int mk = i;
                while (my2[mk] != '(') {
                    my2num += my2[mk++];
                }
                int num = Integer.valueOf(my2num);
                int h = mk+1;
                String temp = "";
                while (my2[h] != ')') {
                    temp += my2[h];
                    h++;
                }
                for (int k = 0; k < num; k++) {
                    result2 += temp;
                }
                i = h + 1;
            }
            else if(my2[i]>='a'&&my2[i]<= 'z'){//a-z
                result2 += my2[i];
            }
            else if(my2[i]>= 'A'&&my2[i]<='Z'){//A-Z
                result2 += my2[i];
            }
            else if(my2[i] != ')'&&my2[i] != '('){
                result2 += my2[i];
            }
            i++;
        }
        //逆序输出
        char[] ch2 = reverseChar(result2.toCharArray());
        String s = new String(ch2);
        System.out.println(s);
    }
}
```

## 2019

第三题：破解小王的加密算法（没有时间做了……）



##  冒泡排序

```s
/*void bubblesort(int* a,int n)
{
	int i,j,tmp,exchange;
	for(i=0;i<n-1;i++)
	{
		exchange=0;
		for(j=n-1;j>i;j--)
			if(a[j]<a[j-1])
			{
				tmp=a[j];
				a[j]=a[j-1];
				a[j-1]=tmp;
				exchange=1;
			}
			if(exchange==0)
				return ;
	}
}*/
int main()
{
	int a[10]={3,2,10,5,6,4,9,8,7,1};
	bubblesort(a,10);
	for(int i=0;i<10;i++)
		cout<<a[i]<<" ";
	return 0;
}


/*void selectsort(int* a,int n)  //直接选择排序
{
	int i,j,k,tmp;
	for(i=0;i<n-1;i++)
	{
		k=i;
		for(j=i+1;j<n;j++)
			if(a[j]<a[k])
				k=j;
		tmp=a[i];
		a[i]=a[k];
		a[k]=tmp;
	}
}
int main()
{
	int a[10]={3,2,1,5,6,4,9,8,7,10};
	selectsort(a,10);
	for(int i=0;i<10;i++)
		cout<<a[i]<<" ";
	return 0;
}
/*void intersort(int *a,int n)   //插入排序
{
	int i,j,temp;
	for(i=1;i<n;i++)
	{
		temp=a[i];
		j=i-1;
		while(j>=0&&temp<a[j])
		{
			a[j+1]=a[j];
			j--;
		}
		a[j+1]=temp;
	}
}
int main()
{
	int ar[10]={2,1,4,5,8,7,9,3,6,10};
	intersort(ar,10);
	for(int k=0;k<10;k++)
		cout<<ar[k]<<" ";
	return 0;
}

/*int binsearch(int a[],int n,int k)  //二分查找
{
	int i,low=0,high=n-1,mid,find=0;
	while(low<high&&!find)
	{
		mid=(low+high)/2;
		if(k<a[mid])
			high=mid-1;
		else if(k>a[mid])
			low=mid+1;
		else {find=1; i=mid;}
	}
	if(find==0)
		return(-1);
	else
	    return(i+1);
}
int main()
{
	int ar[10]={10,22,24,27,34,45,56,57,89,90};
	int b;
	b=binsearch(ar,10,58);
	if(b==-1)
		cout<<"no find";
	else cout<<"find in  "<<b;
	return  0;
}



/*bool isneed(char* str,int len)     //字符串反转，单词不变
{
	bool isn=false;
	for(int i=0;i<len;i++)
		if(str[i]==' ')
		{isn=true;break;}
	return isn;
}
void reverse(char* str,int start,int end)
{
	int sta=start,en=end;
	char temp;
	for(;sta<en;sta++,en--)
	{temp=str[sta];str[sta]=str[en];str[en]=temp;}
    
}
int main()
{
	char str[100];
	cin.getline(str,100);
	int len=strlen(str);
	int start,end,i,j;
	if(isneed(str,len))
	{
		reverse(str,0,len-1);
		j=0;
		for(i=0;i<len;i++)
		{
			if(str[i]==' ')
			{
				reverse(str,j,i-1);
				j=i+1;
			}
		}
	}
		cout<<str;
		return 0;
}
```

/*#include <assert.h>   
#include <iostream>   
using namespace std;  
  
#define     KEY_CHAR            ' '            
  
  
void ReverseString(char *pszStr, int i32FirIndex, int i32EndIndex)  
{  
    assert(NULL != pszStr || i32FirIndex >= 0 || i32EndIndex >= 0);  
   if (i32EndIndex <= i32FirIndex) return;  
  
    char chTemp;  
    int i32FirIdx = i32FirIndex;  
    int i32EndIdx = i32EndIndex;  
  
    for (; i32FirIdx < i32EndIdx; i32FirIdx++, i32EndIdx--)  
    {  
        chTemp = pszStr[i32FirIdx];  
        pszStr[i32FirIdx] = pszStr[i32EndIdx];  
        pszStr[i32EndIdx] = chTemp;   
    }  
}  
  
bool IsNeedReverseString(char *pszStr, int nLen)  
{  
    assert(NULL != pszStr || nLen > 0);  
  
    bool bRet = false;  
    for (int i32I = 0; i32I < nLen; i32I++)  
    {  
        if (KEY_CHAR == pszStr[i32I])  
        {  
            bRet = true;  
            break;  
        }  
    }  
  
    return bRet;  
}  
  
  
int main()  
{  
    char aszStr[] = {"I am a boy."};  
  
    if(IsNeedReverseString(aszStr, strlen(aszStr)))  
    {  
        //Step 1:   
        ReverseString(aszStr, 0, strlen(aszStr) - 1);  
  
        //Step 2:   
        int i32CurIdx = 0;  
        for (int i32I = 0; i32I < (int)strlen(aszStr); i32I++)  
        {  
            if (KEY_CHAR == aszStr[i32I])  
            {  
                ReverseString(aszStr, i32CurIdx, i32I - 1);  
                i32CurIdx = i32I + 1;  
            }  
        }  
       // ReverseString(aszStr, i32CurIdx, (int)strlen(aszStr) - 1);  
    }  
  
    cout << aszStr << endl;  
    return 0;  
}  



/*void minnum(string str)     //统计字符串中出现最少的字符
{
	int i=0,min,num[26]={0};
	char ch;
	while(ch=str[i++])
		//if(isalpha(ch))  //判断是不是字母可去掉因为num[-?]  大写换小写 if(isupper(ch)) ch=tolower(ch) 
		num[ch-'a']++;
    for(i=0;i<26;i++)
		if(num[i]!=0)
		{min=num[i];break;} //找到第一个不是零个的字母
		int j=i;
	for(i=j;i<26;i++)
		if(num[i]!=0&&min>num[i])
		{
			min=num[i];
		//	ch=char('a'+i);        //找出最小的个数
		}
	for(i=j;i<26;i++)
        if(num[i]==min)
			cout<<char('a'+i)<<" "<<min<<endl;  //有可能有多个字母个数一样且最少
}
int main()
{
	//string str("qwe rrasddsa f123");
	char str[100];
	cout<<"input string\n";
	cin.get(str,100);      //string str;cin>>str;不能有空格
	minnum(str);
	cout<<str;
	return 0;
}




```c
void tr(int n,int base)                 // 进制转换
{ int num[20],i=0,j,m=n;
  while(m)
  { 
    num[i]=m%base;
	m/=base;
    i++;
  }
  cout<<"**"<<n<<"转换为"<<base<<"进制数是：";
  for(j=i-1;j>=0;j--)
      if(num[j]<10)  cout<<num[j];
	  else  cout<<char(num[j]+'A'-10);
  cout<<endl;
}
int main()
{int n,ch;
do{cout<<"输入一个数：(0退出)";
   cin>>n;
   if(n)
   {do{ cout<<"输入需要转换的进制数：2-9，16，32(输入0退出)";
      cin>>ch;
	  switch(ch)
	  {  case 2:tr(n,2);break;
	     case 3:tr(n,3);break;
		 case 4:tr(n,4);break;
         case 5:tr(n,5);break;
         case 6:tr(n,6);break;
         case 7:tr(n,7);break;
         case 8:tr(n,8);break;
         case 9:tr(n,9);break;
         case 16:tr(n,16);break;
         case 32:tr(n,32);break;
         case 0:break;
		 default:cout<<"输入错误！"<<endl;
	  }
  }while(ch);
   }
   else exit(0);
}while(n);
  return 0;
}


/*#include <stdio.h>                //判断回文
void main()
{
 char *p,*q,a[100];
 printf("请输入一个字符串！");
 scanf("%s",a);//输入类型为s%，参数应为字符串的首地址
 p=a;
 q=a;
 while(*q!='\0')
  q++;
 q--;//应该再减回到最后一个字符的地址
 while(p<q&&(*p)==(*q))
 {
  p++;
  q--;
 }
 if(p<q)printf("这不是一个回文字符串！");//考虑到是奇数个字符的字符串时不可以<=
 else printf("这是一个回文字符串！");
}

/*void iss(string str)
{  int i,aa=str.length();
	for(i=0;i<=aa/2;i++)
		if(str[i]!=str[aa-i-1]) break;
		if(i==aa/2+1) cout<<"yes";
		else cout<<"no";

}
int main()
{
	string str; int j;
	cout<<"input:";
	cin>>str;
    int b=str.length();
	for(j=0;str[j];j++)
		if(str[j]<'0'||str[j]>'9')
			break;
		if(j==b)
	    iss(str);
		else cout<<"input digit";
	return 0;

}



/*void isa(int a)         //输入int型 判断回文
{
	int b,newo=0,oldo=a;
	while(a)
	{
		b=a%10;
		newo=newo*10+b;
		a/=10;
	}
	if(newo==oldo) cout<<"yes";
	else cout<<"no";
}
int main()
{
	int a;
	cout<<"input n :";cin>>a;
	isa(a);
	return 0;
}
/*int main()
{   int j,i=0,nu[30];
    long num;
	cout<<"input a num"; 
	cin>>num;
	int a=num;
	while(a)
	{nu[i]=a%10;
	 a/=10;
	 i++;
	}
	j=i-1;
	for(i=0;i<=j/2;i++)
		if(nu[i]==nu[j-i]) continue;
		else {cout<<"no";break;}
	if(i==j/2+1)  cout<<"yes";
	return 0;
}*/
```

## 2012软件

### 1、删除子串，只要是原串中有相同的子串就删掉，不管有多少个，返回子串个数。

```c++
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <string.h>
int delete_sub_str(const char *str,const char *sub_str,char *result)
{
	assert(str != NULL && sub_str != NULL);
	const char *p,*q;
	char *t,*temp;
	p = str;
	q = sub_str;
	t = result;
	int n,count = 0;
	n = strlen(q);
	temp = (char *)malloc(n+1);
	memset(temp,0x00,n+1);
	while(*p)
	{
		memcpy(temp,p,n);
		if(strcmp(temp,q) == 0 )
		{
			count++;
			memset(temp,0x00,n+1);
			p = p + n;
		}
		else
		{	
			*t = *p;
			p++;
			t++;
			memset(temp,0x00,n+1);
		}	
	}
	free(temp);
	return count;
}
void main()
{
	char s[100] = {‘\0’};
	int num = delete_sub_str(“123abc12de234fg1hi34j123k”,”123”,s);
	printf(“The number of sub_str is %d\r\n”,num);
	printf(“The result string is %s\r\n”,s);
}
```

### 2、约瑟夫环是一个数学的应用问题：

已知n个人（以编号1，2，3...n分别表示）围坐在一张圆桌周围。从编号为k的人开始报数，数到m的那个人出列；他的下一个人又从1开始报数，数到m的那个人又出列；依此规律重复下去，直到圆桌周围的人全部出列。

```c++
#include<stdio.h>
#include<stdlib.h>
typedef struct Node
{	
	int num;
	struct Node *next;
}LinkList;
LinkList *creat(int n)
{
	LinkList *p,*q,*head;
	int i=1;  
	p=(LinkList *)malloc(sizeof(LinkList));
    p->num=i;
	head=p;
    for(i=2;i<=n;i++)
    {
        q=(LinkList *)malloc(sizeof(LinkList));
        q->num=i;
        p->next=q;
        p=q;	
    }
    p->next=head;          /*使链表尾指向链表头 形成循环链表*/
   return head;
}
void fun(LinkList *L,int m)
{
	int i;
	LinkList *p,*s,*q;
	p=L;
	printf("出列顺序为:");
	while(p->next!=p)
	{
		for(i=1;i<m;i++)
		{	q=p;
			p=p->next;
		}
		printf("%5d",p->num);
		s=p;
		q->next=p->next;
		p=p->next;
		free(s);
	}
	printf("%5d\n",p->num);
}
int main()
{
	LinkList *L;
	int n, m;
	n=9;
	m=5;
	L=creat(n);
	fun(L,m);	
	return 0;
}
```

### 3、比较一个数组的元素 是否为回文数组

```c++
#include <stdio.h>
#include <string.h>
int huiwen(char str[])
{
	int i,len,k=1;
	len=strlen(str);
	for(i=0;i<len/2;i++)
	{
		if(str[i]!=str[len-i-1])
		{
			k=1;
			break;
		}
	}
	if(k==0)
	    printf("%s 不是一个回文数\n",str);
	else
		printf("%s 是一个回文数\n",str);
     return 0;
}
void main()
{       
	char str[100] = {0};     
	printf("Input a string：");        /*提示输入Input a string：*/
	scanf("%s", str);                  /*scan()函数输入一个字符串：*/
	huiwen(str);
}
```

### 4、 数组比较（20分）

```s
• 问题描述： 
比较两个数组，要求从数组最后一个元素开始逐个元素向前比较，如果2个数组长度不等，则只比较较短长度数组个数元素。请编程实现上述比较，并返回比较中发现的不相等元素的个数
比如：
数组{1,3,5}和数组{77,21,1,3,5}按题述要求比较，不相等元素个数为0
数组{1,3,5}和数组{77,21,1,3,5,7}按题述要求比较，不相等元素个数为3
• 要求实现函数： 
int array_compare(int len1, int array1[], int len2, int array2[])
【输入】 int len1：输入被比较数组1的元素个数；
int array1[]：输入被比较数组1；
int len2：输入被比较数组2的元素个数；
int array2[]：输入被比较数组2；
【输出】 无 
【返回】 不相等元素的个数，类型为int
• 示例 
1） 输入：int array1[] = {1,3,5}，int len1 = 3，int array2[] = {77,21,1,3,5}，int len2 = 5
函数返回：0
2） 输入：int array1[] = {1,3,5}，int len1 = 3，int array2[] = {77,21,1,3,5,7}，int len2 = 6
函数返回：３
```

```c++
#include<stdlib.h>
#include<stdio.h>
#include<string.h>
int array_compare(int len1, int array1[], int len2, int array2[])
{
	int count=0;
	for( ;len1>=0&&len2>=0 ;len1--,len2--)
	{
			if(array1[len1-1]==array2[len2-1])
			{
	        	count++;
			}	
	}
	return count;
}
int main()
{
	int result=0;
	int array1[]={1,3,5};
	int len1=3;
	int array2[]={77,12,1,3,5};
	int len2=5;
	result=array_compare( len1, array1,  len2, array2);  ///result=array_compare( len1, array1[],  len2, array2[]);不能这样
                                                         // 函数形参中永远只是传得首地址，不能传数组                       切记切记！！！！！！
	printf("the result is %d", result);
}
```

### 5、随机数按计数输出

```s

• 问题描述： 
输入一个由随机数组成的数列（数列中每个数均是大于0的整数，长度已知），和初始计数值m。从数列首位置开始计数，计数到m后，将数列该位置数值替换计数值m，并将数列该位置数值出列，然后从下一位置从新开始计数，直到数列所有数值出列为止。如果计数到达数列尾段，则返回数列首位置继续计数。请编程实现上述计数过程，同时输出数值出列的顺序

比如： 输入的随机数列为：3,1,2,4，初始计数值m=7，从数列首位置开始计数（数值3所在位置）
第一轮计数出列数字为2，计数值更新m=2，出列后数列为3,1,4，从数值4所在位置从新开始计数
第二轮计数出列数字为3，计数值更新m=3，出列后数列为1,4，从数值1所在位置开始计数
第三轮计数出列数字为1，计数值更新m=1，出列后数列为4，从数值4所在位置开始计数
最后一轮计数出列数字为4，计数过程完成。
输出数值出列顺序为：2,3,1,4。

• 要求实现函数： 
void array_iterate(int len, int input_array[], int m, int output_array[])
【输入】 int len：输入数列的长度；
int intput_array[]：输入的初始数列
int m：初始计数值
【输出】 int output_array[]：输出的数值出列顺序
【返回】 无

• 示例 
输入：int input_array[] = {3,1,2,4}，int len = 4， m=7
输出：output_array[] = {2,3,1,4}
////////////循环链表实现//////////////////////
```

```c++
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
typedef struct Node
{
	int num;
	struct Node *next;
}node;
node *creat(int len , int input_array[])
{
	node *h,*s,*p;
	int i;
	h=(node*)malloc(sizeof(node));
	h->num=input_array[0];
	p=h;
	for(i=1;i<len;i++)
	 {
		 s=(node*)malloc(sizeof(node));
		 s->num=input_array[i];
		 p->next=s;
		 p=s;
	 }
	 p->next=h;
	 
	 return (h);
}
void array_iterate(int len, int input_array[], int m)
{
	node *q,*p,*s;
	int i=0,j=0,k;
	int output_array[4];
	p=creat(len,input_array);
	while(p->next!=p)
	{
		for(i=1;i<m;i++)
		{
			q=p;
			p=p->next;
		}
		m=p->num;
	    //printf("%5d",m);
		output_array[j++]=m;
			
		s=p;
		q->next=p->next;
		p=p->next;
		free(s);
		s=NULL;
	}
		m=p->num;
	   // printf("%5d\n",m);
		output_array[j]=p->num;
		k=j;
		for(j=0 ; j<=k; j++)
		{
			printf("%5d",output_array[j]);
		}

}
int main()
{
	int input_array[]={3,1,2,4};
	int len=4;
	int m=7;
	int output_array[4];
	array_iterate(len,  input_array, m);
}
```

### 6、 手机号码合法性判断（20分）

```s
	问题描述：
我国大陆运营商的手机号码标准格式为：国家码+手机号码，例如：8613912345678。特点如下：
1、  长度13位；
2、  以86的国家码打头；
3、  手机号码的每一位都是数字。
 
请实现手机号码合法性判断的函数要求：
1）  如果手机号码合法，返回0；
2）  如果手机号码长度不合法，返回1
3）  如果手机号码中包含非数字的字符，返回2；
4）  如果手机号码不是以86打头的，返回3；
【注】除成功的情况外，以上其他合法性判断的优先级依次降低。也就是说，如果判断出长度不合法，直接返回1即可，不需要再做其他合法性判断。
	要求实现函数：
int verifyMsisdn(char* inMsisdn)
【输入】 char* inMsisdn，表示输入的手机号码字符串。
【输出】  无
【返回】  判断的结果，类型为int。
	示例
输入：  inMsisdn = “869123456789“
输出：  无
返回：  1
输入：  inMsisdn = “88139123456789“
输出：  无
返回：  3
输入：  inMsisdn = “86139123456789“
输出：  无
返回：  0

```

```c++
#include<stdio.h>
#include<stdlib.h>
#include<assert.h>
#include<string.h>
#define LENGTH  13

int verifyMsisdn(char *inMsisdn)
{
	
	char *pchar=NULL;
	assert(inMsisdn!=NULL);
	if(LENGTH==strlen(inMsisdn))
	{
		if(('8'==*inMsisdn)&&(*(inMsisdn+1)=='6'))
		{
			while(*inMsisdn!='\0')
			{
				if((*inMsisdn>='0')&&(*inMsisdn<='9'))
				inMsisdn++;
				else 
				return 2 ;
			}
		}
		else
			return 3;
	}
	else
		return 1;
	return 0;
}

int main()
{
	char *pchar=NULL;
	unsigned char ichar=0;
	int result;
	switch(ichar)
	{
	case 0:
		pchar="8612345363789";break;
	case 1:
		pchar="861111111111111";break;
	case 2:
		pchar="86s1234536366"; break;
	default: 
		break;
	}
   result =verifyMsisdn(pchar);
    printf("result is %d\n",result);
}
```


### 7、 数组比较（20分）（和第4、12题一样)

```s
• 问题描述： 
比较两个数组，要求从数组最后一个元素开始逐个元素向前比较，如果2个数组长度不等，则只比较较短长度数组个数元素。请编程实现上述比较，并返回比较中发现的不相等元素的个数
比如：
数组{1,3,5}和数组{77,21,1,3,5}按题述要求比较，不相等元素个数为0
数组{1,3,5}和数组{77,21,1,3,5,7}按题述要求比较，不相等元素个数为3

• 要求实现函数： 
int array_compare(int len1, int array1[], int len2, int array2[])

【输入】 int len1：输入被比较数组1的元素个数；
int array1[]：输入被比较数组1；
int len2：输入被比较数组2的元素个数；
int array2[]：输入被比较数组2；
【输出】 无 
【返回】 不相等元素的个数，类型为int

• 示例 
1） 输入：int array1[] = {1,3,5}，int len1 = 3，int array2[] = {77,21,1,3,5}，int len2 = 5
函数返回：0
2） 输入：int array1[] = {1,3,5}，int len1 = 3，int array2[] = {77,21,1,3,5,7}，int len2 = 6
函数返回：3
```

```c++
#include<stdlib.h>
#include<stdio.h>
#include<string.h>

int array_compare(int len1, int array1[], int len2, int array2[])
{
	int count=0;

	for( ;len1>0&&len2>0 ;len1--,len2--)
	{
			if(array1[len1-1]!=array2[len2-1])
			{
	        	count++;
			}	
	}
	return count;
}

int main()
{
	int result=0;
	int array1[]={1,3,5};
	int len1=3;

	int array2[]={77,12,1,3,5,7};
	int len2=6;

	result=array_compare(len1, array1, len2, array2);  
///result=array_compare( len1, array1[],  len2, array2[]);不能这样
// 函数形参中永远只是传得首地址，不能传数组                       切记切记！！！！！！
	printf("the result is %d", result);
}
```

### 8、简单四则运算

```s
• 问题描述： 输入一个只包含个位数字的简单四则运算表达式字符串，计算该表达式的值
注： 1、表达式只含 +, -, *, / 四则运算符，不含括号
2、表达式数值只包含个位整数(0-9)，且不会出现0作为除数的情况
3、要考虑加减乘除按通常四则运算规定的计算优先级
4、除法用整数除法，即仅保留除法运算结果的整数部分。比如8/3=2。输入表达式保证无0作为除数情况发生
5、输入字符串一定是符合题意合法的表达式，其中只包括数字字符和四则运算符字符，除此之外不含其它任何字符，不会出现计算溢出情况
• 要求实现函数： 
int calculate(int len,char *expStr)
【输入】 int len: 字符串长度；
char *expStr: 表达式字符串；
【输出】 无
【返回】 计算结果

• 示例 
1） 输入：char *expStr = “1+4*5-8/3”
函数返回：19
2） 输入：char *expStr = “8/3*3”
函数返回：6 
```

```c++
#include <stdio.h>
#include <iostream>
using namespace std;
int calculate(int len,char *expStr)
{
	struct  {
		char opdata[200];
		int top;
	}opstack;
	//定义操作符栈
	opstack.top = -1;
	int i=0;//遍历字符串的下标
	int t=0;//当前后缀表达式的长度
	char ch = expStr[i];
	while (ch!='\0')
	{
		switch (ch)
		{
			case '+':
			case '-':
				while (opstack.top != -1)
				{
					expStr[t] = opstack.opdata[opstack.top];
					opstack.top--;
					t++;
				}
				opstack.top++;
				opstack.opdata[opstack.top] = ch;
				break;
			case '*':
			case '/':
				while (opstack.top != -1 && (opstack.opdata[opstack.top] =='*' || opstack.opdata[opstack.top] =='/') )
				{
					expStr[t] = opstack.opdata[opstack.top];
					opstack.top--;
					t++;
				}
				opstack.top++;
				opstack.opdata[opstack.top] = ch;
				break;
			default:
				expStr[t] = ch;
				t++;
				break;
		}
		i++;
		ch = expStr[i];
	}
	while (opstack.top != -1)//将栈中所有的剩余的运算符出栈
	{
	    expStr[t] = opstack.opdata[opstack.top];
		opstack.top--;
		t++;
	}
	expStr[t]='\0';
	struct 	{
		int numeric[200];
		int top;
	}data;
	data.top = -1;
	i=0;
	ch = expStr[i];
    while (ch!='\0')
    {
		if (ch>='0' && ch <= '9' )
		{
			data.top++;
			data.numeric[data.top] = ch-'0';
		} 
		else if('+' == ch)
		{
			int tmp = data.numeric[data.top-1]  + data.numeric[data.top];
			data.top--;
			data.numeric[data.top] = tmp;
		}
		else if('-' == ch)
		{
			int tmp = data.numeric[data.top-1]  - data.numeric[data.top];
			data.top--;
			data.numeric[data.top] = tmp;
		}
		else if('*' == ch)
		{
			int tmp = data.numeric[data.top-1]  * data.numeric[data.top];
			data.top--;
			data.numeric[data.top] = tmp;
		}
		else if('/' == ch)
		{
		    if(data.numeric[data.top] == 0)
			{
				printf("cannot be zero of the divide\n");
				exit(1);
			}
			int tmp = data.numeric[data.top-1] / data.numeric[data.top];
			data.top--;
			data.numeric[data.top] = tmp;
		}
		i++;
		ch = expStr[i];
    }
	return data.numeric[data.top];
}
void main()
{
	char expStr[] = "9/3*5";
    printf("%s\n",expStr);
	int result = calculate(strlen(expStr),expStr);
	printf("%d\n",result);
}
```

### 9、选秀节目打分，分为专家评委和大众评委，

```s
score[] 数组里面存储每个评委打的分数，judge_type[] 里存储与 score[] 数组对应的评委类别，judge_type[i] == 1，表示专家评委，judge_type[i] == 2，表示大众评委，n表示评委总数。打分规则如下：专家评委和大众评委的分数先分别取一个平均分（平均分取整），然后，总分 = 专家评委平均分  * 0.6 + 大众评委 * 0.4，总分取整。如果没有大众评委，则 总分 = 专家评委平均分，总分取整。函数最终返回选手得分。
 函数接口   int cal_score(int score[], int judge_type[], int n) 
```

```c++
#include<stdio.h>
#include<string.h>
#include<iostream.h>
#include<conio.h>
#define N 5

int cal_score(int score[], int judge_type[], int n) 
{
	int expert=0;
    int dazhong=0;
	int zongfen=0;
	int i;
	int number=0;
	
	for(i=0;i<N;i++)
	{
		if(judge_type[i]==1)
		{
			expert=expert+score[i];
			number++;
		}
		else dazhong=dazhong+score[i];
	}
	if(number==N)
	{
		zongfen=(int)(expert/N);
	}
	else
    {
		expert=(int)(expert/number);
		dazhong=(int)(dazhong/(N-number));
		zongfen=int(0.6*expert+0.4*dazhong);
	}
	return zongfen;
}
int main()
{
	int score[N];
	int judge_type[N];
	int numberlast=0;
	int i;
	printf("please input the %d score:\n",N);
	for(i=0;i<N;i++)
		scanf("%d",&score[i]);
	printf("please input the level(1:expert,2:dazhong)\n");
	for(i=0;i<N;i++)
		scanf("%d",&judge_type[i]);
	numberlast=cal_score(score,judge_type,N);
	printf("the last score is %d\n",numberlast);
	return 0;
}
```

### 10、给定一个数组input[] ，如果数组长度n为奇数，则将数组中最大的元素放到 output[] 数组最中间的位置，

```s
如果数组长度n为偶数，则将数组中最大的元素放到 output[] 数组中间两个位置偏右的那个位置上，然后再按从大到小的顺序，依次在第一个位置的两边，按照一左一右的顺序，依次存放剩下的数。 
      例如：input[] = {3, 6, 1, 9, 7}   output[] = {3, 7, 9, 6, 1};             input[] = {3, 6, 1, 9, 7, 8}    output[] = {1, 6, 8, 9, 7, 3}
```

```c++
#include<stdio.h>
#include<string.h>
#include<conio.h>
void sort(int input[], int n, int output[])
{
	int i,j;
	int k=1;
	int temp;
	int med;
	for(i=0;i<n;i++)
		for(j=0;j<n-i;j++)
			if(input[j]>input[j+1])
			{temp=input[j];input[j]=input[j+1];input[j+1]=temp;}
			if(n%2!=0)
			{
				for(i=0;i<n;i++)
				//	printf("%2d",input[i]);
			//	printf("\n");
				med=(n-1)/2;
				output[med]=input[n-1];
				for(i=1;i<=med;i++)
				{
					output[med-i]=input[n-1-k];
					output[med+i]=input[n-2-k];
					k=k+2;
					
				}
			}
			else
			{
				
				for(i=0;i<n;i++)
			//		printf("%2d",input[i]);
			//	printf("\n");
				med=n/2;
				output[med]=input[n-1];
				for(i=1;i<=med-1;i++)
				{
					output[med-i]=input[n-1-k];
					output[med+i]=input[n-2-k];
					k=k+2;	
				}
				output[0]=input[0];				
			}	
			for(i=0;i<n;i++)
					printf("%2d",output[i]);
				printf("\n");
}
int main()
{
	int a[6]={3,6,1,9,7,8};
	int b[6]={0};
	for(int i=0;i<6;i++)
	printf("%2d",a[i]);
	printf("\n");
	sort(a,6,b);
	return 0;
}
```

### 11、操作系统任务调度问题。

```s
操作系统任务分为系统任务和用户任务两种。其中，系统任务的优先级 < 50，用户任务的优先级 >= 50且 <= 255。优先级大于255的为非法任务，应予以剔除。现有一任务队列task[]，长度为n，task中的元素值表示任务的优先级，数值越小，优先级越高。函数scheduler实现如下功能，将task[] 中的任务按照系统任务、用户任务依次存放到 system_task[] 数组和 user_task[] 数组中（数组中元素的值是任务在task[] 数组中的下标），并且优先级高的任务排在前面，数组元素为-1表示结束。 
      例如：task[] = {0, 30, 155, 1, 80, 300, 170, 40, 99}    system_task[] = {0, 3, 1, 7, -1}    user_task[] = {4, 8, 2, 6, -1}
函数接口    void scheduler(int task[], int n, int system_task[], int user_task[])
```

```c++
#include<stdio.h>
#include<string.h>
#include<malloc.h>
#include<iostream.h>

void scheduler1(int task[], int n, int system_task[], int user_task[])
{
	int i;
	int j=0;
	int *p,*pp,*p_user,*pp_user;
	int index=0;
	int count,count2;
	int min=0;
	int k=0;
	p=(int*)malloc(sizeof(int)*n);
	for(i=0;i<n;i++)
		p[i]=0;
	pp=(int*)malloc(sizeof(int)*n);
	for(i=0;i<n;i++)
		pp[i]=0;
	p_user=(int*)malloc(sizeof(int)*n);
	for(i=0;i<n;i++)
		p_user[i]=0;
	pp_user=(int*)malloc(sizeof(int)*n);
	for(i=0;i<n;i++)
		pp_user[i]=0;
	
	for(i=0;i<n;i++)
	{
		if(task[i]<50)
		{
			{
				system_task[j]=task[i];
				pp[j]=i;
				j++;
			}
			count=j;
		}
		
		else if(task[i]<=255)
		{
		
			{
				user_task[k]=task[i];
				pp_user[k]=i;
				k++;
			}
			count2=k;
		}
		else task[i]=task[i];
	
	}
	
	for(i=0;i<count;i++)
		printf("%3d",system_task[i]);
	printf("\n");
	
	
	for(i=0;i<count;i++)
	{
		min=system_task[0];
		for(j=1;j<count;j++)
		{
			
			if(system_task[j]<min)
			{
				min=system_task[j];	
				p[i]=j;		
			}
			
		}
		system_task[p[i]]=51;
	}
	
    pp[count]=-1;
	for(i=0;i<count;i++)
		printf("%3d",pp[p[i]]);	
	printf("%3d\n",pp[count]);
	
	
	/***********************************************************/
	
	for(i=0;i<count2;i++)
		printf("%4d",user_task[i]);
	printf("\n");
	
	for(i=0;i<count2;i++)
	{
		min=user_task[0];
		for(j=1;j<count2;j++)
		{
			
			if(user_task[j]<min)
			{
				min=user_task[j];	
				p_user[i]=j;		
			}
			
		}
		user_task[p_user[i]]=256;
	}
	
    pp_user[count2]=-1;
	for(i=0;i<count2;i++)
		printf("%4d",pp_user[p_user[i]]);	
	printf("%3d\n",pp_user[count2]);
	
}

int main()
{
	int task[9]={0, 30, 155, 1, 80, 300,170, 40, 99};
	int system_task[9]={0};
	int user_task[9]={0};
	scheduler1(task,9,system_task,user_task);
	return 0;
}
```

### 12、 (和第4、7题一样)从两个数组的最后一个元素比较两个数组中不同元素的个数，

```s
如有array1[5]={77,21,1,3,5}, array2[3]={1,3,5}，从array1[4]与array2[2]比较开始，到array1[2]与array[0]比较结束。这样得出它们不同的元素个数为0，若array1[6]={77,21,1,3,5,7}，那么他们不同的元素为3。
　　函数原型为 int compare_array( int len1, int array1[], int len2, int array2[] );
　　其中，len1与len2分别为数组array1[]和array2[]的长度，函数返回值为两个数组不同元素的个数。
　　 以下是上题的函数完整实现：
```

```c++
//diff_num.cpp

#include<stdio.h>
int compare_array(int len1,int array1[],int len2,int array2[])
{
	int i,t,small,num=0;
	//把两数组倒置
	for(i=0;i<len1/2;i++)
	{
		t=array1[i];
		array1[i]=array1[len1-i-1];
		array1[len1-i-1]=t;
	}
	for(i=0;i<len2/2;i++)
	{
		t=array2[i];
		array2[i]=array2[len2-i-1];
		array2[len2-i-1]=t;
	}
	//输出倒置后的两数组
/*	for(i=0;i<len1;i++)
		printf("%d ",array1[i]);
	printf("\n");
	for(i=0;i<len2;i++)
		printf("%d ",array2[i]);
*/	printf("\n");
	if(len1>len2)
		small=len2;
	else
		small=len1;
	num=small;
	for(i=0;i<small;i++)
	{
		if(array1[i]==array2[i])
			num--;
	}
	printf("num=%d\n",num);
	return num;
}
void main()
{
	int array1[5]={77,21,1,3,5},array2[3]={1,3,5};
	int len1=5,len2=3;
	compare_array(len1,array1,len2,array2);
}

```

### 13、输入一个字符串，用指针求出字符串的长度。

答案：

```c++
#include <stdio.h>
int main()
{
   char str[20],  *p;
   int length=0;
   printf(“Please input a string: ”);
   gets(str);
   p=str;
   while(*p++)
   {
length++;
}
printf(“The length of string is %d\n”, length);
return 0;
}
```

### 14、使用C语言实现字符串中子字符串的替换

```s
描述：编写一个字符串替换函数，如函数名为 StrReplace(char* strSrc, char* strFind, char* strReplace)，strSrc为原字符串，strFind是待替换的字符串，strReplace为替换字符串。
举个直观的例子吧，如：“ABCDEFGHIJKLMNOPQRSTUVWXYZ”这个字符串，把其中的“RST”替换为“ggg”这个字符串，结果就变成了：
ABCDEFGHIJKLMNOPQgggUVWXYZ
```

答案一：

```c++
#include <stdio.h>
#include <string.h>
void StrReplace(char* strSrc, char* strFind, char* strReplace);
#define M 100;
void main()
{char s[]="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
char s1[]="RST";
char s2[]="ggg";
StrReplace(s,s1,s2);
printf("%s\n",s);
}
void StrReplace(char* strSrc, char* strFind, char* strReplace)
{
        int i=0;
        int j;
        int n=strlen(strSrc);
        int k=strlen(strFind);
        for(i=0;i<n;i++)
        {
                if(*(strSrc+i)==*strFind)
                {
                        for(j=0;j<k;j++)
                        {
                                if(*(strSrc+i+j)==*(strFind+j))
                                {
                                        *(strSrc+i+j)=*(strReplace+j);
                                }
                                else continue;
                        }
                }
        }
}
```

答案二：

```c++
#include <stdio.h>
#define MAX 100
StrReplace(char *s, char *s1, char *s2) {
    char *p;
    for(; *s; s++) {
        for(p = s1; *p && *p != *s; p++);
        if(*p) *s = *(p - s1 + s2);
    }
}
int main()
{
    char s[MAX];            //s是原字符串
    char s1[MAX], s2[MAX];  //s1是要替换的
                            //s2是替换字符串
    puts("Please input the string for s:");
    scanf("%s", s);
    puts("Please input the string for s1:");
    scanf("%s", s1);
    puts("Please input the string for s2:");
    scanf("%s", s2);
    StrReplace(s, s1, s2);
    puts("The string of s after displace is:");
    printf("%s\n", s);
    return 0;
}
```

答案三：

```c++
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define M 100
void StrReplace(char* strSrc, char* strFind, char* strReplace);
int main()
{
	char s[]="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	char s1[]="RST";
	char s2[]="gggg";
	StrReplace(s,s1,s2);
	printf("%s\n",s);
	return 0;
}
void StrReplace(char* strSrc, char* strFind, char* strReplace)
{
	while(*strSrc != '\0')
	{
		if(*strSrc == *strFind)
		{
			if(strncmp(strSrc,strFind,strlen(strFind)) == 0 )	
			{
				int i = strlen(strFind);
				int j = strlen(strReplace);
				printf("i = %d,j = %d\n",i,j);
				char *q = strSrc + i;
				printf("*q = %s\n",q);	
				while((*strSrc++ = *strReplace++) != '\0');
				printf("strSrc - 1 = %s\n",strSrc - 1);
				printf("*q = %s\n",q);	
				while((*strSrc++ = *q++) != '\0');
			}
			else
			{
				strSrc++;
			}
		}
		else
		{
			strSrc++;
		}
	}
}

```

### 15、编写一个程序实现功能：将字符串”Computer Secience”赋给一个字符数组，

然后从第一个字母开始间隔的输出该串，用指针完成。
答案：

```c++
#include <stdio.h>
#include <string.h>
int main()
{
	char str[]=”Computer Science”;
    int flag=1;
	char *p=str;
	while(*p)
{
   if ( flag )
   { 
    	  printf(“%c”,*p);
       }
       flag = (flag + 1) % 2;
       p++;
    }
	printf(“\n”);
    return 0;
}
```

### 16、（和第14题一样）使用C语言实现字符串中子字符串的替换

```
描述：编写一个字符串替换函数，如函数名为 StrReplace(char* strSrc, char* strFind, char* strReplace)，strSrc为原字符串，strFind是待替换的字符串，strReplace为替换字符串。
举个直观的例子吧，如：“ABCDEFGHIJKLMNOPQRSTUVWXYZ”这个字符串，把其中的“RST”替换为“ggg”这个字符串，结果就变成了：
ABCDEFGHIJKLMNOPQgggUVWXYZ
```

答案一：

```c++
#include <stdio.h>
#include <string.h>
void StrReplace(char* strSrc, char* strFind, char* strReplace);
#define M 100;
void main()
{char s[]="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
char s1[]="RST";
char s2[]="ggg";
StrReplace(s,s1,s2);
printf("%s\n",s);
}
void StrReplace(char* strSrc, char* strFind, char* strReplace)
{
        int i=0;
        int j;
        int n=strlen(strSrc);
        int k=strlen(strFind);
        for(i=0;i<n;i++)
        {
                if(*(strSrc+i)==*strFind)
                {
                        for(j=0;j<k;j++)
                        {
                                if(*(strSrc+i+j)==*(strFind+j))
                                {
                                        *(strSrc+i+j)=*(strReplace+j);
                                }
                                else continue;
                        }
                }
        }
}
```

答案二：

```c++
#include <stdio.h>
#define MAX 100
StrReplace(char *s, char *s1, char *s2) {
    char *p;
    for(; *s; s++) {
        for(p = s1; *p && *p != *s; p++);
        if(*p) *s = *(p - s1 + s2);
    }
}
int main()
{
    char s[MAX];            //s是原字符串
    char s1[MAX], s2[MAX];  //s1是要替换的
                            //s2是替换字符串
    puts("Please input the string for s:");
    scanf("%s", s);
    puts("Please input the string for s1:");
    scanf("%s", s1);
    puts("Please input the string for s2:");
    scanf("%s", s2);
    StrReplace(s, s1, s2);
    puts("The string of s after displace is:");
    printf("%s\n", s);
    return 0;
}
```

答案三：

```c++
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define M 100
void StrReplace(char* strSrc, char* strFind, char* strReplace);
int main()
{
	char s[]="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	char s1[]="RST";
	char s2[]="gggg";

	StrReplace(s,s1,s2);
	printf("%s\n",s);

	return 0;
}
void StrReplace(char* strSrc, char* strFind, char* strReplace)
{
	while(*strSrc != '\0')
	{
		if(*strSrc == *strFind)
		{
			if(strncmp(strSrc,strFind,strlen(strFind)) == 0 )	
			{
				int i = strlen(strFind);
				int j = strlen(strReplace);
				printf("i = %d,j = %d\n",i,j);
				char *q = strSrc + i;
				printf("*q = %s\n",q);	
				while((*strSrc++ = *strReplace++) != '\0');
				printf("strSrc - 1 = %s\n",strSrc - 1);
				printf("*q = %s\n",q);	
				while((*strSrc++ = *q++) != '\0');
			}
			else
			{
				strSrc++;
			}
		}
		else
		{
			strSrc++;
		}
	}
}
```

### 20.判断身份证号码的合法性

```s
/*我国公民的身份证号码特点如下：
1、     长度为18位；
2、     第1～17位只能为数字；
3、     第18位可以是数字或者小写英文字母x。
4、     身份证号码的第7~14位表示持有人生日的年、月、日信息。
例如：511 002 1988 08 08 0111或51100219880808011x。
请实现身份证号码合法性判断的函数。除满足以上要求外，需要对持有人生日的年、月、日信息进行校验。年份大于等于1900年，
小于等于2100年。需要考虑闰年、大小月的情况。
所谓闰年，能被4整除且不能被100整除 或 能被400整除的年份，闰年的2月份为29天，非闰年的2月份为28天。其他情况的合法性校验，考生不用考虑。
函数返回值：
1）  如果身份证号合法，返回0；
2）  如果身份证号长度不合法，返回1；
3）  如果身份证号第1~17位含有非数字的字符，返回2；
4）  如果身份证号第18位既不是数字也不是英文小写字母x，返回3；
5）  如果身份证号的年信息非法，返回4；6-9
6）  如果身份证号的月信息非法，返回5；10-11
7）  如果身份证号的日信息非法，返回6（请注意闰年的情况）；12-13
【注】除成功的情况外，以上其他合法性判断的优先级依次降低。也就是说，如果判断出长度不合法，直接返回1即可，不需要再做其他合法性判断。
```

```c++
#include <stdio.h>
#include <windows.h>
int verifyID(char* inID)
{
	int len = strlen(inID);
	int i;
	if( len !=18)
		return 1;
	for(i=0;i<17;i++)
	{
		if( *(inID+i)<'0'|| *(inID+i)>'9')
			return 2;
	}
	if( *(inID+17) <'0'||*(inID+17) >'9')
		if( *(inID+17)!='x' )
			return 3;

	char temp1[10]={0};
	char temp2[10]={0};
	char temp3[10]={0};

	memcpy(temp1,inID+6,4);
	int year=atoi(temp1);
	if( year<1900||year>2100)
		return 4;

	memcpy(temp2,inID+10,2);
	int mon=atoi(temp2);
	if( mon<1||mon>12)
		return 5;
	
	memcpy(temp3,inID+12,2);
	int day=atoi(temp3);

	if(1==mon)
	{
		if( day<0 ||day>31)
			return 6;
	}
	if(2==mon)//能被4整除且不能被100整除 或 能被400整除的年份，闰年的2月份为29天，非闰年的2月份为28天。
	{
		if( ((year%4==0)&&(year%100!=0)) || (year%400==0) )
		{
			if( day<0 ||day>29)
			return 6;
		}
		else
		{
			if( day<0 ||day>28)
			return 6;
		}
	}
	if(3==mon)
	{
		if( day<0 ||day>31)
			return 6;
	}
	if(4==mon)
	{
		if( day<0 ||day>30)
			return 6;
	}
	if(5==mon)
	{
		if( day<0 ||day>31)
			return 6;
	}
	if(6==mon)
	{
		if( day<0 ||day>30)
			return 6;
	}
	if(7==mon)
	{
		if( day<0 ||day>31)
			return 6;
	}
	if(8==mon)
	{
		if( day<0 ||day>31)
			return 6;
	}
	if(9==mon)
	{
		if( day<0 ||day>30)
			return 6;
	}
	if(10==mon)
	{
		if( day<0 ||day>31)
			return 6;
	}
	if(11==mon)
	{
		if( day<0 ||day>30)
			return 6;
	}
	if(12==mon)
	{
		if( day<0 ||day>31)
			return 6;
	}

	return 0;

}
void main()
{
	char buf[20]="500106198701295410";
	int re=verifyID(buf);
	printf("re = %d\n",re);
	printf("\n");

}
```

### 21.手机号码合法性问题的另一个版本

```s
/*1. 手机号码合法性判断（20分）
问题描述： 
我国大陆运营商的手机号码标准格式为：国家码+手机号码，例如：8613912345678。特点如下：
1、  长度13位；
2、  以86的国家码打头；
3、  手机号码的每一位都是数字。
请实现手机号码合法性判断的函数（注：考生无需关注手机号码的真实性，
也就是说诸如86123123456789这样的手机号码，我们也认为是合法的），要求： 
1）  如果手机号码合法，返回0；
2）  如果手机号码长度不合法，返回1
3）  如果手机号码中包含非数字的字符，返回2；
4）  如果手机号码不是以86打头的，返回3；
【注】除成功的情况外，以上其他合法性判断的优先级依次降低。也就是说，如果判断出长度不合法，直接返回1即可，不需要再做其他合法性判断。
要求实现函数： 
int verifyMsisdn(char* inMsisdn)
【输入】 char* inMsisdn，表示输入的手机号码字符串【输出】  无 【返回】  判断的结果，类型为int。
示例 
输入：  inMsisdn = “869123456789“
输出：  无
返回：  1
输入：  inMsisdn = “88139123456789“
输出：  无
返回：  3
输入：  inMsisdn = “86139123456789“
输出：  无
返回：  0
*/
```

```c++
#include <stdio.h>
#include <windows.h>
int verifyMsisdn(char* inMsisdn)
{
	int len = strlen(inMsisdn);
	int i=0;
	if(len!=13)
		return 1;
	for(i=0;i<13;i++)
	{
		if( *(inMsisdn+i)<'0'|| *(inMsisdn+i)>'9')
			return 2;
	}
	if( *(inMsisdn+0)=='8' && *(inMsisdn+1)=='6' )
		return 0;
	else
		return 3;

}
void main()
{
	int re = verifyMsisdn("861A640503228");
	printf("re = %d\n",re);
}

```



## 写一个程序, 要求功能：求出用1，2，5这三个数不同个数组合的和为100的组合个数。

```s
如：100个1是一个组合，5个1加19个5是一个组合。。。。 请用C++语言写。
答案：最容易想到的算法是：
设x是1的个数，y是2的个数，z是5的个数，number是组合数
注意到0<=x<=100，0<=y<=50，0<=z=20，所以可以编程为：
```

```c++
number=0;
for (x=0; x<=100; x++)
for (y=0; y<=50; y++)
for (z=0; z<=20; z++)
if ((x+2*y+5*z)==100)
number++;
cout<<number<<endl;
```

```s
上面这个程序一共要循环100*50*20次，效率实在是太低了
事实上，这个题目是一道明显的数学问题，而不是单纯的编程问题。我的解法如下：
因为x+2y+5z=100
所以x+2y=100-5z，且z<=20 x<=100 y<=50
所以(x+2y)<=100，且(x+5z)是偶数
对z作循环，求x的可能值如下：
```

```s
z=0, x=100, 98, 96, ... 0
z=1, x=95, 93, ..., 1
z=2, x=90, 88, ..., 0
z=3, x=85, 83, ..., 1
z=4, x=80, 78, ..., 0
......
z=19, x=5, 3, 1
z=20, x=0
```

```s
因此，组合总数为100以内的偶数+95以内的奇数+90以内的偶数+...+5以内的奇数+1，
即为： (51+48)+(46+43)+(41+38)+(36+33)+(31+28)+(26+23)+(21+18)+(16+13)+(11+8)+(6+3)+1
某个偶数m以内的偶数个数（包括0）可以表示为m/2+1=(m+2)/2
某个奇数m以内的奇数个数也可以表示为(m+2)/2
所以，求总的组合次数可以编程为：
```

```s
number=0;
for (int m=0;m<=100;m+=5)
{
number+=(m+2)/2;
}
cout<<number<<endl;
```

```s
这个程序,只需要循环21次, 两个变量，就可以得到答案,比上面的那个程序高效了许多
倍----只是因为作了一些简单的数学分析

这再一次证明了：计算机程序=数据结构+算法，而且算法是程序的灵魂，对任何工程问
题，当用软件来实现时，必须选取满足当前的资源限制，用户需求限制，开发时间限制等种
种限制条件下的最优算法。而绝不能一拿到手，就立刻用最容易想到的算法编出一个程序了
事——这不是一个专业的研发人员的行为。
那么，那种最容易想到的算法就完全没有用吗？不，这种算法正好可以用来验证新算法
的正确性，在调试阶段，这非常有用。在很多大公司，例如微软，都采用了这种方法：在调
试阶段，对一些重要的需要好的算法来实现的程序，而这种好的算法又比较复杂时，同时用
容易想到的算法来验证这段程序，如果两种算法得出的结果不一致（而最容易想到的算法保
证是正确的），那么说明优化的算法出了问题，需要修改。
可以举例表示为：
```

```c++
#ifdef DEBUG
int simple();
#end if
int optimize();
......
in a function:
{
result=optimize();
ASSERT(result==simple());
}
```

```s
这样,在调试阶段,如果简单算法和优化算法的结果不一致,就会打出断言。同时，在程
序的发布版本，却不会包含笨重的simple()函数。——任何大型工程软件都需要预先设计良
好的调试手段，而这里提到的就是一种有用的方法。
一个学生的信息是：姓名，学号，性别，年龄等信息，用一个链表，把这些学生信息连在一起， 给出一个age, 在些链表中删除学生年龄等于age的学生信息。
```

```c++
#include "stdio.h"
#include "conio.h"

struct stu{
char name[20];
char sex;
int no;
int age;
struct stu * next;
}*linklist;
struct stu *creatlist(int n)
{
int i;
//h为头结点，p为前一结点，s为当前结点
struct stu *h,*p,*s;
h = (struct stu *)malloc(sizeof(struct stu));
h->next = NULL;
p=h;
for(i=0;i<n;i++)
{ 
s = (struct stu *)malloc(sizeof(struct stu));
p->next = s;
printf("Please input the information of the student: name sex no age \n");
scanf("%s %c %d %d",s->name,&s->sex,&s->no,&s->age);
s->next = NULL;
p = s;
}
printf("Create successful!");
return(h);
}
void deletelist(struct stu *s,int a)
{
struct stu *p;
while(s->age!=a)
{
p = s;
s = s->next;
}
if(s==NULL)
printf("The record is not exist.");
else
{
p->next = s->next;
printf("Delete successful!");
}
}
void display(struct stu *s)
{
s = s->next;
while(s!=NULL)
{
printf("%s %c %d %d\n",s->name,s->sex,s->no,s->age);
s = s->next;
}
}
int main()
{
struct stu *s;
int n,age;
printf("Please input the length of seqlist:\n");
scanf("%d",&n);
s = creatlist(n);
display(s);
printf("Please input the age:\n");
scanf("%d",&age);
deletelist(s,age);
display(s);
return 0;
}
```

## 实现一个函数，把一个字符串中的字符从小写转为大写。

```c++
#include "stdio.h"
#include "conio.h"

void uppers(char *s,char *us)
{
for(;*s!='\0';s++,us++)
{
if(*s>='a'&&*s<='z')
*us = *s-32;
else
*us = *s;
}
*us = '\0';
}
int main()
{
char *s,*us;
char ss[20];
printf("Please input a string:\n");
scanf("%s",ss);
s = ss;
uppers(s,us);
printf("The result is:\n%s\n",us);
getch();
}

```

## 随机输入一个数，判断它是不是对称数（回文数）（如3，121，12321，45254）。不能用字符串库函数 

```s
函数名称：Symmetry 
功能： 判断一个数时候为回文数(121,35653) 
输入： 长整型的数 
输出： 若为回文数返回值为1 esle 0 
```

```c++
unsigned char Symmetry (long n)
{
long i,temp;
i=n; temp=0;
while(i) //不用出现长度问题,将数按高低位掉换
{
temp=temp*10+i%10;
i/=10;
}
return(temp==n);
} 

```

方法一：

```s
功能： 
判断字符串是否为回文数字 
实现： 
先将字符串转换为正整数，再将正整数逆序组合为新的正整数，两数相同则为回文数字 
输入： 
char *s：待判断的字符串 
输出： 
无 
返回： 
0：正确；1：待判断的字符串为空；2：待判断的字符串不为数字； 
3：字符串不为回文数字；4：待判断的字符串溢出 
```

```c
unsigned IsSymmetry(char *s) 
{ 
char *p = s; 
long nNumber = 0; 
long n = 0; 
long nTemp = 0; 

/*判断输入是否为空*/ 
if (*s == \'\\0\') 
return 1; 

/*将字符串转换为正整数*/ 
while (*p != \'\\0\') 
{ 
/*判断字符是否为数字*/ 
if (*p<\'0\' || *p>\'9\') 
return 2; 

/*判断正整数是否溢出*/ 
if ((*p-\'0\') > (4294967295-(nNumber*10))) 
return 4; 

nNumber = (*p-\'0\') + (nNumber * 10); 

p++; 
} 

/*将数字逆序组合，直接抄楼上高手的代码，莫怪，呵呵*/ 
n = nNumber; 
while(n) 
{ 
/*判断正整数是否溢出*/ 
if ((n%10) > (4294967295-(nTemp*10))) 
return 3; 

nTemp = nTemp*10 + n%10; 
n /= 10; 
} 

/*比较逆序数和原序数是否相等*/ 
if (nNumber != nTemp) 
return 3; 

return 0; 
} 

```

方法二：

```s
功能： 
判断字符串是否为回文数字 
实现： 
先得到字符串的长度，再依次比较字符串的对应位字符是否相同 
输入： 
char *s：待判断的字符串 
输出： 
无 
返回： 
0：正确；1：待判断的字符串为空；2：待判断的字符串不为数字； 
3：字符串不为回文数字 
```

```c
unsigned IsSymmetry_2(char *s) 
{ 
char *p = s; 
int nLen = 0; 
int i = 0; 

/*判断输入是否为空*/ 
if (*s == \'\\0\') 
return 1; 

/*得到字符串长度*/ 
while (*p != \'\\0\') 
{ 
/*判断字符是否为数字*/ 
if (*p<\'0\' || *p>\'9\') 
return 2; 

nLen++; 
p++; 
} 

/*长度不为奇数，不为回文数字*/ 
if (nLen%2 == 0) 
return 4; 

/*长度为1，即为回文数字*/ 
if (nLen == 1) 
return 0; 

/*依次比较对应字符是否相同*/ 
p = s; 
i = nLen/2 - 1; 
while (i) 
{ 
if (*(p+i) != *(p+nLen-i-1)) 
return 3; 

i--; 
} 

return 0; 
} 

```

## 求2~2000的所有素数.有足够的内存,要求尽量快

```c++
int findvalue[2000]={2};
static int find=1;
bool adjust(int value)
{
assert(value>=2);
if(value==2) return true;
for(int i=0;i<=find;i++)
{
if(value%findvalue[i]==0)
return false;
}
findvalue[find++];
return true;
}
```

## A,B,C,D四个进程

```s
A向buf里面写数据，B,C,D向buf里面读数据，
当A写完，且B，C，D都读一次后，A才能再写。用P，V操作实现。
```

## 将单向链表reverse，如ABCD变成DCBA，只能搜索链表一次

## 将二叉树的两个孩子换位置，即左变右，右变左。不能用递规（变态！）
