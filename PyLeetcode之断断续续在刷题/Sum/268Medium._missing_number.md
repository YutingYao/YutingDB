### 268. Missing Number

题目:
<https://leetcode-cn.com/problems/missing-number/>


难度:

Medium 



等差数列前n项和减去数组之和,一行瞬秒
```(注意题目input从0开始取值)```


```python
class Solution(object):
    def missingNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        return len(nums) * (len(nums) + 1) / 2 - sum(nums)
```



第二种解法是位运算：位运算（异或运算）



```python
class Solution(object):
    def missingNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        res = n = len(nums)
        for i in range(n):
            res ^= i
            res ^= nums[i]
        return res
```

        


