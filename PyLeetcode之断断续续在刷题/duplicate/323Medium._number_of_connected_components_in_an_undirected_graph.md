#  323. Number of Connected Components in an Undirected Graph
**<font color=red>难度: 中等</font>**

## 刷题内容

> 原题连接

* https://leetcode-cn.com/problems/number-of-connected-components-in-an-undirected-graph

> 内容描述

```
给定从 0 到 n -1 标记的 n 个节点和一个无向边列表（每条边是一对节点），编写一个函数来查找无向图中的连通分量的数量。

Example 1:

Input: n = 5 and edges = [[0, 1], [1, 2], [3, 4]]

     0          3
     |          |
     1 --- 2    4 

Output: 2
Example 2:

Input: n = 5 and edges = [[0, 1], [1, 2], [2, 3], [3, 4]]

     0           4
     |           |
     1 --- 2 --- 3

Output:  1

Note:
您可以假设边中不会出现重复的边。由于所有边都是无向的，[0, 1] 与 [1, 0] 相同，因此不会一起出现在边中。
```

## 解题方案

> 思路 1

经典并查集题目，可以去总结部分先看看并查集的概念已经优化


```python
class Solution(object):
    def countComponents(self, n, edges):
        """
        :type n: int
        :type edges: List[List[int]]
        :rtype: int
        """
        def find(x):
        # if uf[x] != x:
        # 	uf[x] = find(uf[x])
            while x != uf[x]:
                uf[x] = uf[uf[x]]
                x = uf[x]
            return uf[x]

        def union(x, y):
            x_root = find(x)
            y_root = find(y)
            uf[x_root] = y_root

        uf = [i for i in range(n)]
        
        for (node1, node2) in edges:
            union(node1, node2)

        res = set([find(i) for i in range(n)])
        return len(res)
```




