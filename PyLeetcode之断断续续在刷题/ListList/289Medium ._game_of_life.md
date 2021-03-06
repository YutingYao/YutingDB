### 289. Game of Life

题目： 
<https://leetcode-cn.com/problems/game-of-life/>


难度 : Medium


直接一上来就没有考虑solve it in-place,考虑的是便利，简直是born for 便利

首先我把board拓宽了，宽，高各增加了两排。

因为这样求neighbor方便，针对原来的borad，现在新的big 对于 1 -> n-1 的部分

全都有八个neighbor，用了一个2d array来记录nbrs，再根据当下的nbr来判断更新，因为不能一边在board上loop一边更新.

AC的效率还ok：

```python
class Solution(object):
    def gameOfLife(self, board):
        """
        :type board: List[List[int]]
        :rtype: void Do not return anything, modify board in-place instead.
        """
        def liveNeighbors(i,j):
            return big[i-1][j-1] + big[i-1][j] + big[i-1][j+1] + big[i][j-1] + big[i][j+1] + big[i+1][j-1] + big[i+1][j] + big[i+1][j+1]
        
        if board == [[]] : return
        row = len(board)
        col = len(board[0]) 

        nbrs = [[0 for j in range(col)] for i in range(row)]
        big = [[ 0 for j in range(col+2) ] for i in range(row+2)]
        for i in range(1,row+1):
            for j in range(1,col+1):
                big[i][j] = board[i-1][j-1]
            
        for i in range(1,row+1):
            for j in range(1,col+1):
                nbrs[i-1][j-1] = liveNeighbors(i,j)
    
        for i in range(row):
            for j in range(col):
                if board[i][j] == 1:                
                    if nbrs[i][j] < 2:
                        board[i][j] = 0
                    elif nbrs[i][j] == 2 or nbrs[i][j] == 3:
                        board[i][j] = 1
                    else:
                        board[i][j] = 0
                else:
                    if nbrs[i][j] == 3:
                        board[i][j] = 1
            
```

谷歌了一下，大家都用到了temp 2d array嘛，哼(ˉ(∞)ˉ)唧。好吧，空间复杂度比我小。



很多的解法都是一样开了一个二维数组，即使没有像我一样扩展board.因为问题在于不能一边更新board 一边来做。

看了一下这边的思路：

<https://www.hrwhisper.me/leetcode-game-of-life/>

<http://www.cnblogs.com/grandyang/p/4854466.html>



不开数组

我们可以使用状态机转换 o(╯□╰)o  感觉不知道在听什么 还是很迷茫的感觉， in-place AC代码

```python
class Solution(object):
    def gameOfLife(self, board):
        """
        :type board: List[List[int]]
        :rtype: void Do not return anything, modify board in-place instead.
        """
        row = len(board)
        col = len(board[0]) if row else 0

        dx = [-1,-1,-1,0,1,1,1,0]
        dy = [-1,0,1,1,1,0,-1,-1]
    
        for i in range(row):
            for j in range(col):
                cnt = 0
                for k in range(8):
                    x, y = i + dx[k], j + dy[k]
                    if x >=0 and x < row and y >=0 and y < col and (board[x][y] == 1  or board[x][y] == 2):
                        cnt += 1

                if board[i][j] and (cnt < 2 or cnt > 3):
                    board[i][j] = 2
                elif board[i][j] == 0 and cnt == 3:
                    board[i][j] = 3

        for i in range(row):
            for j in range(col):
                board[i][j] %= 2
                   
   
```



