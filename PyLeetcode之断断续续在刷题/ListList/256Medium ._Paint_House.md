### 256. Paint House

题目:
<https://leetcode-cn.com/problems/paint-house/>

难度:
Medium



其实这个题目有实际意义诶，至少我的故乡？在要申请啥东西的时候就把街上的房子全刷了。

然后这个是相邻的房子不同色。



其实我觉得paint fense更难一点



思路：

数组 dp\[x][3] 代表第x个房子paint r/g/b的值



AC代码

```py
class Solution(object):
    def minCost(self, costs):
        """
        :type costs: List[List[int]]
        :rtype: int
        """
        m = len(costs)
        if m == 0 : return 0
        elif m == 1 :  return min(costs[0][0], costs[0][1],costs[0][2])
    	else:
	        n = 3 if m else 0
	        dp = [[0 for i in range(3)] for j in range(m)]

	        dp[0] = costs[0]

	        
	        for i in range(1,m):
	        	dp[i][0] = min(dp[i-1][1],dp[i-1][2]) + costs[i][0]
	        	dp[i][1] = min(dp[i-1][0],dp[i-1][2]) + costs[i][1]
	        	dp[i][2] = min(dp[i-1][0],dp[i-1][1]) + costs[i][2]
	        return min(dp[m-1][0], dp[m-1][1], dp[m-1][2])

```





