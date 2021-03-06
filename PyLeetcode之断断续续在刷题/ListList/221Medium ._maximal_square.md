###221. Maximal Square


题目:
<https://leetcode-cn.com/problems/maximal-square/>


难度:
Medium

tag： DP


递推公式，一开始想的很简单：

dp[i][j] = dp[i-1][j-1] + 1 #如果dp[i-1][j-1]为1，dp[i-1][j]为1，dp[i][j-1]为1

很明显的错误，一旦遇到更大的方块就会有问题

然后看了hint，其实递推方程式是很有技巧的，左上角，左边，上面，相邻的三个部分最小的+1,当然，前提也是要这里dp[i][j] 为1，然后我们再会去看其他的部分。

看个例子

```
原本的matrix                     DP

1 0 1 0 0                     1 0 1 0 0
1 0 1 1 1            →        1 0 1 1 1 
1 1 1 1 1                     1 1 1 2 2
1 0 0 1 0                     1 0 0 1 0

```

是非常make sense的，因为最小的必定包括了周边的1，然后再加1，否则如果是0的话那么就为0.

而naïve的错误的递推公式是因为一个square考虑的部分是k * k的部分， k * k 部分都必定为1.

而正确的递推公式


	dp[i][j] = min(dp[i-1][j-1],dp[i-1][j],dp[i][j-1]) + 1

则完美的考虑了这一情况


```py
class Solution(object):
	def maximalSquare(self, matrix):
		"""
		:type matrix: List[List[str]]
		:rtype: int
		"""
		dp = []
		for i in matrix:
			tmp = []
			for j in i:
				tmp.append(int(j))
			dp.append(tmp)

		row = len(dp)
		col = len(dp[0]) if row else 0


		for i in range(1,row):
			for j in range(1,col):
				if dp[i][j] == 1:
					dp[i][j] = min(dp[i-1][j-1],dp[i-1][j],dp[i][j-1]) + 1


		maxv = 0
		for i in range(row):
			for j in range(col):
				if dp[i][j] > maxv:
					maxv = dp[i][j]
		return maxv * maxv
```


