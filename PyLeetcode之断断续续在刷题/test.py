class Solution:
    def haveCircularDependency(self, n: int, prerequisites):
        graph = [[] for _ in range(n)] # 邻接表存储图结构
        indegree = [0 for _ in range(n)] # 每个点的入度
        # 将依赖关系加入邻接表中g，并各个点入度
        for pre in prerequisites:
            a, b = pre[0], pre[1]
            graph[a].append(b)
            indegree[b] += 1
            
        res = [] # 存储结果序列
        que = deque()
        # 一次性将入度为0的点全部入队
        for i in range(n):
            if indegree[i] == 0:
                que.append(i)
        while que:
            tmp = que.popleft()
            res.append(tmp)
            # 删除边时，将终点的入度-1。若入度为0，果断入队
            for j in graph[tmp]:
                indegree[j] -= 1
                if indegree[j] == 0:
                    que.append(j)
        return res if len(res) == n else []