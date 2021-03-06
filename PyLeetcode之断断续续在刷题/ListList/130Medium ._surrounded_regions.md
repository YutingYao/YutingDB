###130. Surrounded Regions


题目:

<https://leetcode-cn.com/problems/surrounded-regions>


难度:

Medium


思路:

loop，然后碰到O做DFS/BFS找出O所在区域:

- 貌似只要O没有碰壁，O就总是被X包围着？
- 所以找出O的范围，然后看它是否碰壁，没有碰壁则mark不需要修改

但是这道题折磨我了很久，因为它有毛病。。。。
它给的input例子是
["XXX","XOX","XXX"]
也怪我 input写着List[List[str]]

但实际上的输入是：
[[u'X', u'X', u'X'], [u'X', u'X', u'X'], [u'X', u'X', u'X']]


还要mark unicode

还有就是学会了新的可以函数之下定义函数，这样就不用什么self了，用起来真方便，但是这样的思路做起来会超时。



```py
class Solution(object):
    def solve(self, board):
        """
        :type board: List[List[str]]
        :rtype: void Do not return anything, modify board in-place instead.
        """


        def shouldOChange(i, j):
            """
            return x,y area and whether they shouldChange
            """
            shouldChange = True
            Oarea = []
            s = []
            s.append((i,j))
            while s:
                (x,y) = s.pop()
                if x == 0 or x == row - 1 or y == 0 or y == col -1 :
                    shouldChange = False
                visited[x][y] = 1
                Oarea.append((x,y))
                if legal(x-1,y):
                    s.append((x-1,y))
                if legal(x+1,y):
                    s.append((x+1,y))
                if legal(x,y-1):
                    s.append((x,y-1))
                if legal(x,y-1):
                    s.append((x,y+1))
                return Oarea,shouldChange

        def legal(x,y):
            return x>=0 and x < row and y>=0 and y < col and board[x][y] == 'O' and visited[x][y] == 0

        
        row = len(board)
        col = len(board[0]) if row else 0

        visited = [[0 for i in range(col)] for j in range(row)]

        for i in range(row):
            for j in range(col):
                if board[i][j] == 'O' and visited[i][j] == 0:
                    Oarea, shouldChange = shouldOChange(i,j)
                    print Oarea,shouldChange
                    if shouldChange:
                        for (x,y) in Oarea:
                            board[x][y] = u'X'

        print board
```


另一个思路就是对周围碰壁的O做BFS/DFS，碰壁的和碰壁相连的是不需要修改的。这样就时间复杂度降低很多了。

原本是O(n^2)可能做DFS/BFS。现在是O(4n)做DFS/BFS,但是发现依旧超时，最后查看了别人的解法，因为我的解法里面多了一个存储工具，相当于，把需要更换location的位置存储起来，最后做loop的时候去查，然后这样还是很耗时。

而一个简便的变法是把这些特别的碰壁的'O' mark出来，这样最后loop的时候不改变这些'O'，只改变不碰壁的'O',又可以减少工作量。同时依旧可以使用collection里面的queue



AC代码

```py
class Solution(object):
    def solve(self, board):
        """
        :type board: List[List[str]]
        :rtype: void Do not return anything, modify board in-place instead.
        """
        def legal(x,y):
            return x>=0 and x < row and y>=0 and y < col and board[x][y] == 'O' and visited[x][y] == 0

        
        row = len(board)
        col = len(board[0]) if row else 0

        visited = [[0 for i in range(col)] for j in range(row)]

        notChangeOArea = []
        queue = collections.deque()

        for j in range(col):
            if board[0][j] == 'O': queue.append((0,j))
            if board[row-1][j] == 'O': queue.append((row-1,j))
        for i in range(row):
            if board[i][0] == 'O': queue.append((i,0))
            if board[i][col-1] == 'O': queue.append((i,col-1))

        while queue:
            (x,y) = queue.popleft()
            board[x][y] = '$'
            visited[x][y] = 1
            if legal(x-1,y):
                queue.append((x-1,y))
            if legal(x+1,y):
                queue.append((x+1,y))
            if legal(x,y-1):
                queue.append((x,y-1))
            if legal(x,y+1):
                queue.append((x,y+1))

        for i in range(row):
            for j in range(col):
                if board[i][j] == '$' : board[i][j] = 'O'
                elif board[i][j] == 'O' : board[i][j] = 'X'
```


同时发现，用这种方式，无论是否使用collection里面的queue，都能AC