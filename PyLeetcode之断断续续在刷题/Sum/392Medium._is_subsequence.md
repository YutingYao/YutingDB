#  392. Is Subsequence
**<font color=red>难度: 中等</font>**

## 刷题内容

> 原题连接

* https://leetcode-cn.com/problems/is-subsequence

> 内容描述

```

Given a string s and a string t, check if s is subsequence of t.

You may assume that there is only lower case English letters in both s and t. t is potentially a very long (length ~= 500,000) string, and s is a short string (<=100).

A subsequence of a string is a new string which is formed from the original string by deleting some (can be none) of the characters without disturbing the relative positions of the remaining characters. (ie, "ace" is a subsequence of "abcde" while "aec" is not).

Example 1:
s = "abc", t = "ahbgdc"

Return true.

Example 2:
s = "axc", t = "ahbgdc"

Return false.

Follow up:
If there are lots of incoming S, say S1, S2, ... , Sk where k >= 1B, and you want to check one by one to see if T has its subsequence. In this scenario, how would you change your code?
```

## 解题方案

> 思路 1


follow up question很有意思


最naive的思路表现形式如下：

beats 53.74%

```python
class Solution(object):
    def isSubsequence(self, s, t):
        """
        :type s: str
        :type t: str
        :rtype: bool
        """
        if s == '': return True
        for i in xrange(len(t)):
        	if t[i] == s[0]:
        		return self.isSubsequence(s[1:],t[i+1:])

        return False
```

> 思路 2

递归操作以及对字符串的操作太过于昂贵，所以用index来处理，节省了时间和空间


```python
class Solution(object):
    def isSubsequence(self, s, t):
        """
        :type s: str
        :type t: str
        :rtype: bool
        """
        if s == '': return True
        ps, pt = 0, 0
        while ps < len(s) and pt < len(t):
            if s[ps] == t[pt]:
                ps += 1
            pt += 1
        return ps >= len(s)
```

精妙绝伦！！


