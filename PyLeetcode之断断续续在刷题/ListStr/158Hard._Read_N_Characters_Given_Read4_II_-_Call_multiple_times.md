# 158. Read N Characters Given Read4 II - Call multiple times

**<font color=red>难度: 困难</font>**

## 刷题内容

> 原题连接

* https://leetcode-cn.com/problems/read-n-characters-given-read4-ii-call-multiple-times

> 内容描述

```
The API: int read4(char *buf) reads 4 characters at a time from a file.

The return value is the actual number of characters read. For example, it returns 3 if there is only 3 characters left in the file.

By using the read4 API, implement the function int read(char *buf, int n) that reads n characters from the file.

Note:
The read function may be called multiple times.

Example 1: 

Given buf = "abc"
read("abc", 1) // returns "a"
read("abc", 2); // returns "bc"
read("abc", 1); // returns ""
Example 2: 

Given buf = "abc"
read("abc", 4) // returns "abc"
read("abc", 1); // returns ""
```

## 解题方案

> 思路 1

这个题目的描述就是一坨屎💩，真的，sb。

我来总结一下，跟第157题不一样的地方就是，157是就读一次，158是可以读好几次
例如：
文件是‘abcdefg’
- 157题就读一次，给一个n就行了。n给1那buf就是‘a’, n给2那buf就是‘ab’
- 但是158不一样，可以多次read，比如第一次n给1，那buf是‘a’，再read一次，n给2，那'a'已经读过了，所以现在buf是'bc'了，
如果再来个n=3的话，buf就是‘def’,

总之就是一个test case 中read函数可以调用一次和调用多次的区别

```python
class Solution(object):
    head, tail, buffer = 0, 0, [''] * 4 ## 定义全局变量
    
    def read(self, buf, n):
        """
        :type buf: Destination buffer (List[str])
        :type n: Maximum number of characters to read (int)
        :rtype: The number of characters read (int)
        """
        i = 0
        while i < n:
            if self.head == self.tail: ## read4 的缓存区为空的时候
                self.head = 0
                self.tail = read4(self.buffer) ## 开始进缓存区
                if self.tail == 0:
                    break
            while i < n and self.head < self.tail:
                buf[i] = self.buffer[self.head] ## 读出缓存区的变量
                i += 1
                self.head += 1
        return i
```


