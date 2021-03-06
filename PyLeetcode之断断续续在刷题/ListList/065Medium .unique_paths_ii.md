 ###65.Unique Paths II

题目： 
<https://leetcode-cn.com/problems/unique-paths-ii/>



tag : DP

难度 : Medium





```
BASE CASE（ i = 0 , j = 0）: 
//第一排和第一列，如果没有obstacle， 则走法为1， 一旦有了obstacle，则之后的格子走法都为0

非BASE CASE ：
//一旦有obstacle，则dp为0
dp(i, j) = dp（i,j-1) + dp(i-1,j)

```

Python代码

```py
class Solution(object):
    def uniquePathsWithObstacles(self, obstacleGrid):
        """
        :type obstacleGrid: List[List[int]]
        :rtype: int
        """
        row = len(obstacleGrid)
        col = len(obstacleGrid[0])
        dp  = [[0 for i in range(col)] for j in range(row)]

        dp[0][0] = int(obstacleGrid[0][0] == 0)

        #first row    
        for j in range(1,col):
            if obstacleGrid[0][j] == 1:
                dp[0][j] = 0
            else:
                dp[0][j] = dp[0][j-1]
        #first col
        for i in range(1,row):
            if obstacleGrid[i][0] == 1:
                dp[i][0] = 0
            else:
                dp[i][0] = dp[i-1][0]

        for i in range(1,row):
            for j in range(1,col):
                if obstacleGrid[i][j] == 1:
                    dp[i][j] = 0
                else:
                    dp[i][j] = dp[i-1][j] + dp[i][j-1]
        return dp[row-1][col-1]
            
```

犯了一个错，简直觉得不可思议。一开始初始化dp用的代码是 

```
dp  = [[0] * col] * row
```

问题在此：


```
>>> x = [[]] * 3
>>> x[1].append(0)
>>> x
[[0], [0], [0]]
```

这样初始化是做了三个一样的object.

The problem is that they're all the same exact list in memory. When you use the [x]*n syntax, what you get is a list of n many x objects, but they're all references to the same object. They're not distinct instances, rather, just n references to the same instance.

参见stackoverflow : <http://stackoverflow.com/questions/12791501/python-initializing-a-list-of-lists>
