### 59. Spiral Matrix II

题目:
<https://leetcode-cn.com/problems/spiral-matrix-ii/>


难度:
Medium

和Spiral Matrix的思路基本一致

也许还有待挖掘trick


```python
class Solution(object):
    def generateMatrix(self,n):
        """
        :type n: int
        :rtype: List[List[int]]
        """
        curNum = 0
        matrix = [[0 for i in range(n)] for j in range(n)]
        maxUp = maxLeft = 0 
        maxDown = maxRight = n - 1
        direction = 0                
        while True:
            if direction == 0: #go right
                for i in range(maxLeft, maxRight+1):
                    curNum += 1
                    matrix[maxUp][i] = curNum
                maxUp += 1
            elif direction == 1: # go down
                for i in range(maxUp, maxDown+1):
                    curNum += 1
                    matrix[i][maxRight] = curNum
                maxRight -= 1
            elif direction == 2: # go left
                for i in reversed(range(maxLeft, maxRight+1)):
                    curNum += 1 
                    matrix[maxDown][i] = curNum
                maxDown -= 1
            else: #go up
                for i in reversed(range(maxUp, maxDown+1)):
                    curNum += 1
                    matrix[i][maxLeft] = curNum
                maxLeft +=1
            if curNum >= n*n:
                return matrix
            direction = (direction + 1 ) % 4
```

Same idea with [spiral matrix I](https://github.com/Lisanaaa/thinking_in_lc/blob/master/054._spiral_matrix.md)
```python
class Solution(object):
    def generateMatrix(self, n):
        """
        :type n: int
        :rtype: List[List[int]]
        """
        res = []
        l = n * n + 1
        while l > 1:
            l, r = l - len(res), l
            res = [range(l, r)] + zip(*res[::-1])
        return res
```
