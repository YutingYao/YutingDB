### 267. Palindrome Permutation II





题目： 
<https://leetcode-cn.com/problems/palindrome-permutation-ii/>



难度 : Medium



思路：

首先这个题目有个简单版本，那就是判断是否可以permutate 为 palindrome. 问题的关键是最多只能一个odd character.



写的这么不elegant，我也是服气！

AC代码：



```python
class Solution(object):
    def generatePalindromes(self, s):
        """
        :type s: str
        :rtype: List[str]
        """
        def permuteUnique(firstS):
            if len(firstS) == 0 : return []
            if len(firstS) == 1 : return [firstS]
            res = []
            for i in range(len(firstS)):
                if i > 0 and firstS[i] == firstS[i-1]: continue
                for j in permuteUnique(firstS[:i] + firstS[i+1:]):
                    res.append([firstS[i]] + j)
            return res

        lookup = {}
        for char in s:
            lookup[char] = lookup.get(char, 0) + 1

        res, firstS, oddChar = 0, [], ''
        for char, cnt in lookup.items():
            if cnt % 2 == 0:
                for i in range(cnt/2):
                    firstS.append(char)
                continue
            else:
                for i in range(cnt / 2):
                    firstS.append(char)
                oddChar = char
                res += 1
        if res >= 2:
            return []
        else:
            res = permuteUnique(firstS)
            if len(res) == 0 and oddChar:
                return [oddChar]
            return map(lambda x: ''.join(x) + oddChar + ''.join(x[::-1]),res)

```









