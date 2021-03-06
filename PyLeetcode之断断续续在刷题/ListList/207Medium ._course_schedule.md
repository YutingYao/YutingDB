###207. Course Schedule



题目:
<https://leetcode-cn.com/problems/course-schedule/>


难度:
Medium

思路：

就是考topological sort，用来判断directed graph是否有cycle

DFS 和 BFS都可以用来拓扑排序。

最简单的想法是每次取出indegree是0的node，然后把它和与之相关的edge都删了。一开始觉得这样的时间复杂度会很高，然后看到了这样写，参照：

<http://bookshadow.com/weblog/2015/05/07/leetcode-course-schedule/>

很聪明的写法

这里做了转成set以及添加removeList这样的操作是因为边list边做iterator这样的操作很危险




```py
class Solution(object):
    def canFinish(self, numCourses, prerequisites):
        """
        :type numCourses: int
        :type prerequisites: List[List[int]]
        :rtype: bool
        """
        degrees = [ 0 for i in range(numCourses)]
        childs = [[] for i in range(numCourses)]
        for front, tail in prerequisites:
        	degrees[front] += 1
        	childs[tail].append(front)

        courses = set(range(numCourses))
        flag = True

        while flag and len(courses):
        	flag = False
        	removeList = []
        	for x in courses:
        		if degrees[x] == 0:
        			for child in childs[x]:
        				degrees[child] -= 1
        			removeList.append(x)
        			flag = True
        	for x in removeList:
        		courses.remove(x)
        return len(courses) == 0 

```

因为CLRS里面明确提到涂色法来处理DFS

搞了半天，写了一个涂色法，在超时的边缘。之所以超时边缘是因为每次都要去prerequisites里看，没有删减，不高效.

```py
class Solution(object):
    def canFinish(self, numCourses, prerequisites):
        """
        :type numCourses: int
        :type prerequisites: List[List[int]]
        :rtype: bool
        """
        def dfs(i, colors, prerequisites):
        	colors[i] = 'G'
        	#print i, colors
        	for front, tail in prerequisites:
        		if tail == i:
        			if colors[front] == 'G':
        				return False
        			elif colors[front] == 'B':
        				continue
        			elif dfs(front, colors, prerequisites) == False:
        				return False
        	colors[i] = 'B'
        	return True

        colors = ['W' for i in range(numCourses)]
        for i in range(numCourses):
        	if colors[i] == 'W':
        		if dfs(i, colors, prerequisites) == False:
        			return False
        return True
```
