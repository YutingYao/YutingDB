###131. Palindrome Partitioning

题目:
<https://leetcode-cn.com/problems/palindrome-partitioning/>


难度:
Medium

知道一定是用递归做，但是在怎么拆的部分疑惑了，然后看了hint

key部分长这样，拆法是类似于combination，然后这个len(s) == 0是确保能被拆为palindrome，因为这样剩下的string才是空的


这个recursion tree是这样的，感觉时间复杂度是O(n!)，因为每次树都branch n个分支

```py

class Solution(object):
    def partition(self, s):
        """
        :type s: str
        :rtype: List[List[str]]
        """
        self.res = []
        self.dfs(s,[])
        return self.res


    def dfs(self, s, stringList):
        if len(s) == 0:
            self.res.append(stringList)
        for i in range(1,len(s)+1):
            if self.isPalindrome(s[:i]):
                self.dfs(s[i:],stringList + [s[:i]])

    def isPalindrome(self, s):
        if len(s) <= 1:
            return True
        return s[0] == s[-1] and self.isPalindrome(s[1:-1])

a = Solution()
print a.partition("aab")

# [['a', 'a', 'b'], ['aa', 'b']]
```

输出是每次必定从单个char的list开始，然后单个char 配 palindrome word，然后palindrome word再来配char...