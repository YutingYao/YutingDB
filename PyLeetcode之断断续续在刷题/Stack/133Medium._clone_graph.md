###133. Clone Graph


题目:
<https://leetcode-cn.com/problems/clone-graph/>


难度:
Medium



思路：

DFS或者BFS把graph traverse一遍，边traverse边复制。因为nodes are labeled uniquely，这就是方便的地方，但是注意node可能重复和有self-loop.

所以先建立新的root node，然后有一个dict把node label和node一一对应。

用stack来存储原本的graph root node，对原本的graph做DFS，这个时候，如果这个node的neighbor是已经出现过，那么我们就是去修改原本的existNode，让它指向存在的neighbor，否则创建新的，再把它们联系起来，谷歌了一下，别人写的比我更简单。anyway，先AC。



`if cur.label in createdNodes:`多余。




```python
class Solution(object):
    def cloneGraph(self, node):
        """
        :type node: UndirectedGraphNode
        :rtype: UndirectedGraphNode
        """
        if node == None: return None

        root = UndirectedGraphNode(node.label)
        # must 1 to 1
        createdNodes = {}
        createdNodes[root.label] = root 

        stack = []
        stack.append(node)

        while stack:
        	cur = stack.pop()
        	if cur.label in createdNodes:
        		existNode = createdNodes[cur.label]
        		for neighbor in cur.neighbors:
        			if neighbor.label in createdNodes:
        				existNeighbor = createdNodes[neighbor.label]
        				existNode.neighbors.append(existNeighbor)
        			else:
        				newNode = UndirectedGraphNode(neighbor.label)
        				existNode.neighbors.append(newNode)
        				createdNodes[neighbor.label] = newNode
        				stack.append(neighbor)
        return root
```



看了别人的代码，貌似比我又写的简洁。



