#  416. Partition Equal Subset Sum
**<font color=red>难度: 中等</font>**

## 刷题内容

> 原题连接

* https://leetcode-cn.com/problems/partition-equal-subset-sum

> 内容描述

```
Given a non-empty array containing only positive integers, find if the array can be partitioned into two subsets such that the sum of elements in both subsets is equal.

Note:
Each of the array element will not exceed 100.
The array size will not exceed 200.
Example 1:

Input: [1, 5, 11, 5]

Output: true

Explanation: The array can be partitioned as [1, 5, 5] and [11].
Example 2:

Input: [1, 2, 3, 5]

Output: false

Explanation: The array cannot be partitioned into equal sum subsets.
```

## 解题方案

> 思路 1

动态规划

dp[i]代表nums中能否找出一个subset的sum等于i，例如dp[0] = True是必然的，因为我们只要取空子集，那么其sum一定为0

```python
class Solution(object):
    def canPartition(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        if not nums or len(nums) == 0:
            return True
        if sum(nums) % 2 != 0: ## 总和必须为偶数，否则肯定无法取两个集合的sum相等
            return False
        half_sum = sum(nums)/2
        dp = [False] * (half_sum+1)
        dp[0] = True
        for i in range(len(nums)):
            for j in range(half_sum, nums[i]-1, -1):
                dp[j] = dp[j] or dp[j-nums[i]]
        print(dp)
        return dp[half_sum]
```




