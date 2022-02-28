from collections import defaultdict 
class Solution:
    def canFinish(self, numCourses, prerequisites):
        indegree = defaultdict(lambda:0)  
        graph = defaultdict(list)         
        for end,stt in prerequisites:
            graph[stt].append(end)
            indegree[end] += 1
        res = []                  
        for i in range(numCourses):
            if indegree[i] == 0:
                res.append(i)    
        for i in res:
            for j in graph[i]:
                indegree[j] -= 1
                if indegree[j] == 0: res.append(j)
        return len(res) == numCourses