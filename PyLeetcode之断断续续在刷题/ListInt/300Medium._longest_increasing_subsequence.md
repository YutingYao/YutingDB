### 300. Longest Increasing Subsequence

题目:
<https://leetcode-cn.com/problems/longest-increasing-subsequence/>


难度:
Medium


思路：

典型DP

递推关系式：

对于以num[i]结束的longest increasing subsequence的长度

dp[i] = dp[j] + 1 if num[i] > num[j] else 1

最后loop一圈，求出最长的 

AC 代码

```python
class Solution(object):
    def lengthOfLIS(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if not nums:
            return 0
        dp = [1 for i in range(len(nums))]
        for i in range(1, len(nums)):
            for j in range(i):
                if nums[i] > nums[j]:
                    dp[i] = max(dp[j]+1, dp[i])
        return max(dp)
```

