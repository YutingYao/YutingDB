### 740. Delete and Earn

题目:
<https://leetcode-cn.com/problems/delete-and-earn/>


难度:

Medium




```python
class Solution(object):
    def deleteAndEarn(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        dp = [0] * 10001
        for num in nums:
            dp[num] += num
        for i in range(2, 10001):
            dp[i] = max(dp[i]+dp[i-2], dp[i-1])
        return dp[-1]
```


