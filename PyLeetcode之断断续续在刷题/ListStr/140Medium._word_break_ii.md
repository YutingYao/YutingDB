
### 140. Word Break II

题目:
<https://leetcode-cn.com/problems/word-break-ii/>


难度:

Medium



还是backtracking，会超时

```py
class Solution(object):  # 此法超时
    def wordBreak(self, s, wordDict):
        """
        :type s: str
        :type wordDict: Set[str]
        :rtype: bool
        """
        self.res = []
        self.wordBreakLst("",s,wordDict)
        return self.res
                
        
    def wordBreakLst(self, lst, rest, wordDict):
        if rest == '':
            self.res.append(lst.rstrip())
            # print lst
        for i in range(1+len(rest)):
            if rest[:i] in wordDict:
                self.wordBreakLst(lst + rest[:i] + " ",rest[i:],wordDict)

```




然后看到有把word break i 结合起来减少时间复杂度的作法。


做法如下，聪明：

就是对于每一个s，我们来check它是否可以break，如果不可以，就不用做相应的操作了
 
 
```python
class Solution(object):
    def wordBreak(self, s, wordDict):
        """
        :type s: str
        :type wordDict: List[str]
        :rtype: List[str]
        """
        self.res = []
        self.wordBreakLst(s, wordDict, '')
        return self.res
    
    def check(self, s, wordDict):
        ok = [True]
        for i in range(1, len(s) + 1):
            ok += any(ok[j] and s[j:i] in wordDict for j in range(i)),
        return ok[-1]

        
    def wordBreakLst(self, s, wordDict, stringLst):
        if self.check(s, wordDict):
            if len(s) == 0 : self.res.append(stringLst[1:])  # 因为最开始也加了一个空格
            for i in range(1,len(s)+1):
                if s[:i] in wordDict:
                    self.wordBreakLst(s[i:], wordDict, stringLst + ' ' + s[:i])
```


但是其实

```
s = "aaaaaa"
wordDict = ["a","aa","aaa"]
print a.wordBreak(s,wordDict)还是会loop很多次

不过像
s = "aabbb"
wordDict = ["a","abbb"]
就会极其的减少loop次数
```


看看stefan大神的做法：

```sentences(i)``` returns a list of all sentences that can be built from the suffix ```s[i:]```.

```python
class Solution(object):
    def wordBreak(self, s, wordDict):
        """
        :type s: str
        :type wordDict: List[str]
        :rtype: List[str]
        """
        memo = {len(s): ['']}
        def sentences(i):
            if i not in memo:
                memo[i] = [s[i:j] + (tail and ' ' + tail)
                           for j in range(i+1, len(s)+1)
                           if s[i:j] in wordDict
                           for tail in sentences(j)]
            return memo[i]
        return sentences(0)
```
