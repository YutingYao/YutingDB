### 17. Letter Combinations of a Phone Number

题目:

<https://leetcode-cn.com/problems/letter-combinations-of-a-phone-number/>


难度:

Medium


思路：

 - hash table一个，用来对应digit -> letter
 - s用来记录结果，每次从digits里面去一个，然后寻找其可能的char，加到s中，digits长度减小
 - digits长度为0时候，把它加入结果



```python
class Solution(object):
    def letterCombinations(self, digits):
        """
        :type digits: str
        :rtype: List[str]
        """
        if digits == '':
            return []
        self.res = []
        self.singleResult('', digits)
        return self.res
        
    def singleResult(self, s, digits):
        if len(digits) == 0:
            self.res.append(s)
        else:
            mapx = {'2':['a','b','c'],
                '3':['d','e','f'],
                '4':['g','h','i'],
                '5':['j','k','l'],
                '6':['m','n','o'],
                '7':['p','q','r','s'],
                '8':['t','u','v'],
                '9':['w','x','y','z']}
            cur_digit = digits[0]
            for c in mapx[cur_digit]:
                self.singleResult(s+c, digits[1:])
```


