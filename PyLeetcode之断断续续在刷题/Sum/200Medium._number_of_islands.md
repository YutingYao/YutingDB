### 200. Number of Islands 


题目:
<https://leetcode-cn.com/problems/number-of-islands/>


难度:
Medium


思路：


一开始：
numberOfIslands = 0
islandArea = []


然后遇到（x,y） = 1的状况，更新numberOfIslands，并且把（x,y）放入islandArea，然后用BFS或者DFS查找岛屿范围，全部更如islandArea，做loop

以上就是基本思路


然后超时|||, 小改之后AC


```python

class Solution(object):
    def numIslands(self, grid):
        """
        :type grid: List[List[str]]
        :rtype: int
        """
        self.grid = grid[:]

        self.row = len(self.grid)
        self.col = len(self.grid[0]) if self.row else 0
        self.visited = [[0 for i in range(self.col)]for j in range(self.row)]


        self.numberOfIslands = 0

        for i in range(self.row):
        	for j in range(self.col):
        		if self.grid[i][j] == '1' and self.visited[i][j] == 0:
        				self.findArea(i,j)
        				self.numberOfIslands += 1

        return self.numberOfIslands

    def findArea(self, i, j):
    	s = []
    	s.append((i,j))
    	while s:
    		(x,y) = s.pop()
    		self.visited[x][y] = 1
    		if self.legal(x-1,y):
    			s.append((x-1,y))
    		if self.legal(x+1,y):
    			s.append((x+1,y))
    		if self.legal(x,y-1):
    			s.append((x,y-1))
    		if self.legal(x,y+1):
    			s.append((x,y+1))

    def legal(self,x,y):
    	return x>= 0 and x < self.row and y >= 0 and y < self.col and self.grid[x][y] == '1' and self.visited[x][y] == 0
a = Solution()
print a.numIslands(["11000","11000","00100","00011"])

```


看了别人的代码，写的真美 ╮(╯_╰)╭ 啊

```python
class Solution(object):
    def numIslands(self, grid):
        """
        :type grid: List[List[str]]
        :rtype: int
        """
        def dfs(gird, used, row, col, x, y):
            if gird[x][y] == '0' or used[x][y]:
                return 
            used[x][y] = True

            if x!= 0:
                dfs(grid, used, row,col, x-1,y)
            if x!= row -1 :
                dfs(grid, used, row,col, x+1, y)
            if y!= 0:
                dfs(grid, used, row,col, x, y-1)
            if y!= col - 1:
                dfs(grid, used, row,col, x, y+1)


        row = len(grid)
        col = len(grid[0]) if row else 0

        used = [[0 for i in xrange(col)] for i in xrange(row)]

        count = 0
        for i in xrange(row):
            for j in xrange(col):
                if grid[i][j] == '1' and not used[i][j]:
                    dfs(grid,used,row,col,i,j)
                    count += 1
        return count
```

厉害的解法：Sink and count the islands.
```python
class Solution(object):
    def numIslands(self, grid):
        """
        :type grid: List[List[str]]
        :rtype: int
        """
        def sink(i, j):
            if 0 <= i < len(grid) and 0 <= j < len(grid[0]) and grid[i][j] == '1':
                grid[i][j] = '0'
                map(sink, (i+1, i-1, i, i), (j, j, j+1, j-1))
                return 1
            return 0
        return sum(sink(i, j) for i in range(len(grid)) for j in range(len(grid[0])))
```

