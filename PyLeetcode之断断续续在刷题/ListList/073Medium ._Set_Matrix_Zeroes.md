### 73. Set Matrix Zeroes



题目： 
<https://leetcode-cn.com/problems/set-matrix-zeroes/>



难度 : Medium



思路：

Naive AC代码，一看类似那个 game of life，不用extra space，不用O(mn)，应该就是用状态转移机了（？），所以还是先naive AC把：

```python
class Solution(object):
    def setZeroes(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: void Do not return anything, modify matrix in-place instead.
        """
        def setZero(i,j):
            for m in range(col):
                matrix[i][m] = 0
            for n in range(row):
                matrix[n][j] = 0
        
        row = len(matrix)
        col = len(matrix[0]) if row else 0
        new_matrix = [matrix[i][:] for i in range(row)]
        
        for i in range(row):
            for j in range(col):
                if new_matrix[i][j] == 0:
                    setZero(i,j)
```



`正确思路`：

一边遍历，一边将相应的行和列置为0是行不通的，会影响后面元素的遍历判断，所以要记录下哪些行和哪些列是要置为0的。为了节约空间，在原矩阵中借两条边，如果该行或者列要置为0，则把左边或者上边的相应位置置为0。如果左边和上边本来就有0，那么需要额外标记一下，最后把左边或者右边也全部置为0.





