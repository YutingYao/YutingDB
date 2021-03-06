### 286. Walls and Gates



题目： 
https://leetcode-cn.com/problems/walls-and-gates/



难度 : Medium



思路：

乍一看feel like all pairs shortest path.

naive的想法是针对所有为0的点做all pairs shortest path，然后最终得到的就是把INF替换保留最小的。时间复杂度是0的个数* BFS



naive的想法AC 

```py
class Solution(object):
    def wallsAndGates(self, rooms):
        """
        :type rooms: List[List[int]]
        :rtype: void Do not return anything, modify rooms in-place instead.
        """
        def legal(x,y):
            return x >= 0 and x < row and y >= 0 and y < col and rooms[x][y] != -1

        def bfs(rooms, i, j):
            queue = []
            queue.append((i,j))

            while queue:
                (x,y) = queue.pop()

                if legal(x-1,y) and rooms[x-1][y] > rooms[x][y] + 1:
                    rooms[x-1][y] = rooms[x][y] + 1
                    queue.append((x-1,y))
                if legal(x+1,y) and rooms[x+1][y] > rooms[x][y] + 1 :
                    rooms[x+1][y] = rooms[x][y] + 1
                    queue.append((x+1,y))
                if legal(x,y-1) and rooms[x][y-1] > rooms[x][y] + 1:
                    rooms[x][y-1] = rooms[x][y] + 1
                    queue.append((x,y-1))
                if legal(x,y+1) and rooms[x][y+1] > rooms[x][y] + 1  :
                    rooms[x][y+1] = rooms[x][y] + 1
                    queue.append((x,y+1))


        row = len(rooms)
        col = len(rooms[0]) if row else 0

        for i in range(row):
            for j in range(col):
                if rooms[i][j] == 0:
                    bfs(rooms,i,j)
```



复习一下BFS的伪码

from wikipedia, 一开始有点小迷茫，那就是为什么没有keep一个visited的数据结构，但是随即反应过来，其实`n.distance == INFINITY` 已经是check它是否被visited 过了，我以上的代码并没有做这个操作，但是因为是格子状以及我仅在检查是否更小，所以也能AC.

```
Breadth-First-Search(Graph, root):
    
    for each node n in Graph:            
        n.distance = INFINITY        
        n.parent = NIL

    create empty queue Q      

    root.distance = 0
    Q.enqueue(root)                      

    while Q is not empty:        
        current = Q.dequeue()
        for each node n that is adjacent to current:
            if n.distance == INFINITY:
                n.distance = current.distance + 1
                n.parent = current
                Q.enqueue(n)
```

