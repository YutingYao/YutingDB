### 54. Spiral Matrix

题目:
<https://leetcode-cn.com/problems/spiral-matrix/>


难度:
Medium


参考别人的代码，一开始觉得很有递归性，根据奇偶不同来写，递归太难写。

然后想到了loop，再想，可能有更优trick，事实证明并没有。

用四个变量来控制边界，然后因为方向总是：→↓←↑ 左右下上




```python
class Solution(object):
    def spiralOrder(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: List[int]
        """
        if matrix == [] : return []
        res = []
        maxUp = maxLeft = 0
        maxDown = len(matrix) - 1
        maxRight = len(matrix[0]) - 1 
        direction = 0 # 0 go right, 1 go down, 2 go left, 3 up
        while True:
            if direction == 0: #go right
                for i in range(maxLeft, maxRight+1):
                    res.append(matrix[maxUp][i])
                maxUp += 1
            elif direction == 1: # go down
                for i in range(maxUp, maxDown+1):
                    res.append(matrix[i][maxRight])
                maxRight -= 1
            elif direction == 2: # go left
                for i in reversed(range(maxLeft, maxRight+1)):
                    res.append(matrix[maxDown][i])
                maxDown -= 1
            else: #go up
                for i in reversed(range(maxUp, maxDown+1)):
                    res.append(matrix[i][maxLeft])
                maxLeft +=1
            if maxUp > maxDown or maxLeft > maxRight:
                return res
            direction = (direction + 1 ) % 4 
```

以上的写法非常精妙，看看我自己用同样的思路写的||||

```python
class Solution(object):
    def spiralOrder(self, matrix):
		"""
		:type matrix: List[List[int]]
		:rtype: List[int]
		"""
		if len(matrix) == 0 : return []

		left = 0
		up = 0
		down = len(matrix) - 1
		right = len(matrix[0]) -1

		# 0 -> right, 1 -> down, 2-> left, 3 -> up
		direction = 0

		# start location
		x, y = 0,0 
		res = []

		while True:
			if left > right or up > down:
				return res

			if direction == 0 :
				while y <= right:
					res.append(matrix[up][y])
					y += 1
				up += 1
				x = up
				direction = 1
				continue

			if direction == 1:
				while x <= down:
					res.append(matrix[x][right])
					x += 1
				right -= 1
				y = right
				direction = 2
				continue

			if direction == 2:
				while y >= left:
					res.append(matrix[down][y])
					y -= 1
				down -= 1
				x = down
				direction = 3
				continue

			if direction == 3:
				while x >= up:
					res.append(matrix[x][left])
					x -= 1
				left += 1
				y = left
				direction = 0
				continue

```

明显别人的代码写的更精妙，因为这里两个boundary都很明确，所以用for in range就能很好的解决问题了.





-----------

最后放一个无敌一行，怕你看完不想看上面的代码了
```python
class Solution(object):
    def spiralOrder(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: List[int]
        """
        return matrix and list(matrix.pop(0)) + self.spiralOrder(zip(*matrix)[::-1])
```

oh, my god!
