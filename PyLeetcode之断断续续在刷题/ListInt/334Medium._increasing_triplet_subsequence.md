###334. Increasing Triplet Subsequence

题目:
<https://leetcode-cn.com/problems/increasing-triplet-subsequence/>


难度:
Medium


思路：

用longest increasing subsequence来求，超时

```py
class Solution(object):
    def increasingTriplet(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        if not nums: return False
        n = len(nums)
        dp = [1 for i in range(n)]
        for i in range(1,n):
        	for j in range(i):
        		if nums[i] > nums[j] :
        			dp[i] = max(dp[i],dp[j] + 1)
        			if dp[i] >= 3:
        				return True

        return False
```

于是转而用Third Maximum Number的方法，维护一个当前最小和当前第二小，当碰到当前比较大，返回True，否则一圈走下来依旧不能满足，返回false.

想一下，如果不是求三个增长，如果是求两个的话，那么一定想到的是保存当前最小值，那么一旦后方遇到一个比较大的，就这样处理掉了。

所以对于任何一个num来说，有三种可能：

- 小于当前的最小值，那么更新当前最小值
- 小于当前第二小值，更新当前第二小值
- 如果以上两种都不是，那么是大于当前第二小值和最小值，于是这样就true

所以是求四个增长也是类似的么

AC代码

```py
class Solution(object):
    def increasingTriplet(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        # m - min, sm - second min
        m, sm = float('inf'), float('inf')

        for num in nums:
        	print m, sm
        	if m >= num:
        		m = num
        	elif sm >= num:
        		sm = num
        	else:
        		return True
        return False
```



