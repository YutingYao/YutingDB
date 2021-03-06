### 120. Triangle

题目:

<https://leetcode-cn.com/problems/triangle/>

难度:
Medium

思路：

先是要注意下这句话：**Each step you may move to adjacent numbers on the row below**

在考虑adjacent number的定义，并不是角标的adjacent，而是真的形态上的adjacent

比如

```
		-1								-1
	2		1			最小			1		0
-2		2		0				-1		2		0
```

最小是-1， 而并不能从第二排的0跳到第三排的第一个造成-2.

 so AC代码

感觉关于dp，我可能还需要补一些东西，因为我不能做到O(n) space

```py
class Solution(object):
    def minimumTotal(self, triangle):
        """
        :type triangle: List[List[int]]
        :rtype: int
        """
        # n total rows of triangle
        n = len(triangle)
        if n == 1: return triangle[0][0]
        elif n == 2 : return min(triangle[0][0] + triangle[1][0], triangle[0][0] + triangle[1][1])
        else:
        	res = []
        	for i in range(n):
        		res.append(triangle[i])

        	res[0] = [triangle[0][0]]
        	res[1] = [triangle[0][0] + triangle[1][0], triangle[0][0] + triangle[1][1]]

        	for i in range(2,n):
        		for j in range(i+1):
        			if j == 0:
        				res[i][j] = res[i-1][j] + triangle[i][j]
        			elif j == i:
        				res[i][j] = res[i-1][-1] + triangle[i][j]
        			else:
        				res[i][j] = min(res[i-1][j-1],res[i-1][j]) + triangle[i][j]

        	return min(res[-1])
```













