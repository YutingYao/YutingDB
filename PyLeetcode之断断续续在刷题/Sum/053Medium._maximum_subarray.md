# 53. Maximum Subarray 最大子序和

**<font color=red>难度: Medium</font>**

## 刷题内容

> 原题连接

* https://leetcode.com/problems/maximum-subarray
* https://leetcode-cn.com/problems/maximum-subarray

> 内容描述

```
给定一个整数数组 nums ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。

示例:

输入: [-2,1,-3,4,-1,2,1,-5,4],
输出: 6
解释: 连续子数组 [4,-1,2,1] 的和最大，为 6。

进阶:

如果你已经实现复杂度为 O(n) 的解法，尝试使用更为精妙的分治法求解。
```

## 解题方案

> 思路 1

O(N^2)

从i开始，计算i到n，存比较大的sum，会超时

```python
class Solution(object):
	def maxSubArray(self, nums):
	    """
	    :type nums: List[int]
	    :rtype: int
	    """
	    n = len(nums)
	    m = float('-inf')
	    for i in range(n):
	    	s = 0
	    	for j in range(i,n):
	    		s = s + nums[j]
	    		m = max(m,s)
	    return m 
```

> 思路 2

* 动态规划（只关注：当然值 和 当前值+过去的状态，是变好还是变坏，一定是回看容易理解）
* ms(i) = max(ms[i-1]+ a[i],a[i])
* 到i处的最大值两个可能，一个是加上a[i], 另一个从a[i]起头，重新开始。可以AC

```python
class Solution(object):
    def maxSubArray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        n = len(nums)
        maxSum = [nums[0] for i in range(n)]
        for i in range(1,n):
        	maxSum[i] = max(maxSum[i-1] + nums[i], nums[i])
        return max(maxSum)
```

> 思路 3

Kadane’s Algorithm wikipedia可以查到,然后一般的是负的可以还回0，这里需要稍作修改，参考

<http://algorithms.tutorialhorizon.com/kadanes-algorithm-maximum-subarray-problem/>


```
start:
    max_so_far = a[0]
    max_ending_here = a[0]

loop i= 1 to n
  (i) max_end_here = Max(arrA[i], max_end_here+a[i]);
  (ii) max_so_far = Max(max_so_far,max_end_here);

return max_so_far

```

AC代码：

```python
class Solution(object):
    def maxSubArray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        n = len(nums)
        maxSum , maxEnd = nums[0], nums[0]
        
        for i in range(1,n):
        	maxEnd = max(nums[i],maxEnd + nums[i])
        	maxSum = max(maxEnd,maxSum)
        return maxSum
```

> 思路 4

参见clrs 第71页，用divide and conquer，有伪码

最大的subarray sum有三个可能，左半段或者右半段，或者跨越左右半段,

速度比较慢，AC代码，复杂度O(NlogN)

```python
class Solution(object):
	def maxSubArray(self, nums):
		"""
		:type nums: List[int]
		:rtype: int
		"""
		def find_max_crossing_subarray(nums, low, mid, high):
			left_sum = float('-inf')
			sum = 0
			for i in xrange(mid,low-1,-1):
				sum = sum + nums[i]
				if sum > left_sum:
					left_sum = sum

			right_sum = float('-inf')
			sum = 0
			for j in range(mid+1,high+1):
				sum = sum + nums[j]
				if sum > right_sum:
					right_sum = sum

			return left_sum + right_sum

		def find_max_subarray(nums,low,high):
			if low == high: 
				return nums[low]
			else:
				mid = (low + high) / 2
				left_sum = find_max_subarray(nums, low, mid)
				right_sum = find_max_subarray(nums,mid+1,high)
				cross_sum = find_max_crossing_subarray(nums,low,mid,high)
				# print left_sum, right_sum, cross_sum
				# print mid, low, high
				return max(left_sum, right_sum, cross_sum)

		return find_max_subarray(nums, 0, len(nums)-1)
```
