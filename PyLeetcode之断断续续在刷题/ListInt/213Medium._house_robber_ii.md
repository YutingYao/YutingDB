###213. House Robber II


题目:
<https://leetcode-cn.com/problems/house-robber-ii/>


难度:
Medium

思路：

跟house robber 1 类似，但是加了一些限制，抢到第 n-1 家最大两种可能，抢第 n-1 家和不抢第 n-1 家。

	0，	1， 2， 3， 4， 5， 6 ... n-1


所以状态转移方程写成二维的更好来求，从第i家抢到第j家的状态转移方程


				nums[j] ,j = i
	dp[i][j] = max(nums[i], nums[i+1]) , j = i +1
				max(dp[i][j-2] + nums[j], dp[i][j-1]), j > i+1
				
				

Show me the code 


AC代码

```py
class Solution(object):
    def rob(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        n = len(nums)
        if n == 0 : return 0
        if n == 1 : return nums[0]
        if n == 2 : return max(nums[0],nums[1])
        
        dp = [[0 for i in range(n)] for j in range(n)]

        for i in range(n):
            for j in range(i,n):
                if j == i:
                    dp[i][j] = nums[j]
                elif j == i + 1:
                    dp[i][j] = max(nums[i],nums[i+1])
                else:
                    dp[i][j] = max(dp[i][j-2] + nums[j], dp[i][j-1])

        # print dp 
        # rob without n-1, or rob with  n-1
        val = max(dp[0][n-2], dp[1][n-3] + nums[n-1])

        return val

```