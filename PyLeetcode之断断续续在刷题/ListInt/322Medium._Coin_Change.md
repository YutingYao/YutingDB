### 322. Coin Change



题目:
<https://leetcode-cn.com/problems/coin-change/>

难度:

Medium 

DP入门

递推方程式: dp[i] = min(dp[i-vj]+1)， vj 是硬币的面额

伪码：

```s
Set Min[i] equal to Infinity for all of i
Min[0]=0

For i = 1 to S
	For j = 0 to N - 1
	If (Vj<=i AND Min[i-Vj]+1<Min[i]) 
		Then Min[i]=Min[i-Vj]+1
Output Min[S]
```



AC代码

```py
class Solution(object):
    def coinChange(self, coins, amount):
        """
        :type coins: List[int]
        :type amount: int
        :rtype: int
        """
        dp = [ float('inf') for i in range(amount+1)]

        dp[0] = 0

        for i in range(amount+1):
        	for coin in coins:
        		if coin <= i and dp[i-coin] + 1 < dp[i]:
        			dp[i] = dp[i-coin] + 1

        return dp[-1] if dp[-1] != float('inf') else -1
```

