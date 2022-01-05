class Solution:
    def possibleBipartition(self, n: int, dislikes: List[List[int]]) -> bool:
        teams = [0] * (n + 1)
        graph = collections.defaultdict(list)
        for a, b in dislikes:
            graph[a].append(b)
            graph[b].append(a)
        # print(graph):
        # defaultdict(<class 'list'>, {1: [2, 3], 2: [1, 4], 3: [1], 4: [2]})

        parent = list(range(n+1))
        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])
            return parent[x]
        
        def union(x, y):
            parent[find(x)] = parent[find(y)]

        for AA in range(1, n+1):
            for BB in graph[AA]: # AA 不能和 BB 在一起
                if find(AA) == find(BB): return False
                union(BB, graph[AA][0])
        return True