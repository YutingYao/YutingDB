###152. Maximum Product Subarray



题目:
<https://leetcode-cn.com/problems/maximum-product-subarray/>


难度:
Medium

思路：

粗一看， 一股浓烈的DP气息飘来，想要套用53题的思路和方程。但是这个跟sum是不一样的，因为乘积可以正负正负的跳，这样的动归方程肯定是不对的

dp[i] = max(dp[i-1] * a[i],a[i])

举个例子 ： [-2,3,-4]


用O(N^2)超时,厉害啊！

想，可不可以记录+的和-的，记录两个dp数组，我哭了，真的是这样做的

最大值可能来源于最小值 -> 哲学般的句子

```python
class Solution(object):
    def maxProduct(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        n = len(nums)
        maxdp = [ nums[0] for i in range(n)]
        mindp = [ nums[0] for i in range(n)]


        for i in range(1,n):
        	maxdp[i] = max(mindp[i-1]*nums[i], maxdp[i-1]*nums[i],nums[i])
        	mindp[i] = min(maxdp[i-1]*nums[i], mindp[i-1]*nums[i],nums[i])

        return max(maxdp)
```
