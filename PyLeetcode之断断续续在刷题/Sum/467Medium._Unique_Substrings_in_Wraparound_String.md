### 467. Unique Substrings in Wraparound String

题目:
<https://leetcode-cn.com/problems/Unique-Substrings-in-Wraparound-String/>


难度:

Medium


思路:

有个无限长的字符串s，是由无数个「abcdefghijklmnopqrstuvwxyz」组成的。现在给你一个字符串p，求多少个p的非空子串在s中出现了？
　　
  
先考虑s的特性，满足条件（在s中）的p的子串只可能是abcd……z的连续序列（z后面是a）， 我们只需要处理p中连续的部分就可以了。但是 举个例子，h-k的序列出现了，a-z的序列也出现了，那么只需要计算a-z的子串个数就可以了，因为h-k已经包含在a-z里了。考虑所有包含的情况，似乎就变得复杂了，a-z还可能被包含在x-za-z中，甚至更长的序列中。
  
　　但是如果考虑以某个字母结尾的子串个数，那么p中以该字母结尾的连续序列长度，就是满足条件的子串个数。如果以字母x结尾的连续序列有多个， 我们只需要最长的一个即可，因为其他短的序列都已经被长的包含进去了，例如'bcd'和'abcd'，有了'abcd'就知道以d结尾的子串有4个，分别是‘d’,'cd','bcd','abcd'，‘bcd’已经被包含进去了。最后求和，问题就解决了。 这样思考就非常简单了，代码也可以很容易写出来。
  


```python
class Solution(object):
    def findSubstringInWraproundString(self, p):
        """
        :type p: str
        :rtype: int
        """
        letters = [0] * 26          #开始默认每个都是0
        length = 0
        for i in range(len(p)):
            curr = ord(p[i]) - ord('a')
            if i > 0 and ord(p[i-1]) != (curr-1)%26 + ord('a'):   #一旦开始不相等了就要将length重置为0
                length = 0
            length += 1                        #否则就说明继续与前面一个字符是连续的，length要加1才行
            if length > letters[curr]:     #length一直加，如果到i这个字符length比它的目前的最大连续子串长度还要长，那么肯定要更新letters
                letters[curr] = length
        return sum(letters)
```


