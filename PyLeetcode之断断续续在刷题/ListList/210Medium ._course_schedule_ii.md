###210. Course Schedule II



题目:
<https://leetcode-cn.com/problems/course-schedule-ii/>


难度:
Medium

思路：

在207的基础上加了order，进击


```py
class Solution(object):
    def findOrder(self, numCourses, prerequisites):
        """
        :type numCourses: int
        :type prerequisites: List[List[int]]
        :rtype: List[int]
        """
        degrees = [ 0 for i in range(numCourses)]
        childs = [[] for i in range(numCourses)]
        for front, tail in prerequisites:
        	degrees[front] += 1
        	childs[tail].append(front)


        courses = set(range(numCourses))
        flag = True
        order = []

        while flag and len(courses):
        	flag = False
        	removeList = []
        	for x in courses:
        		if degrees[x] == 0:
        			print x
        			for child in childs[x]:
        				degrees[child] -= 1
        			removeList.append(x)
        			order.append(x)
        			flag = True
        	for x in removeList:
        		courses.remove(x)

        if len(courses) == 0:
        	return order
        else:
        	return []

```
