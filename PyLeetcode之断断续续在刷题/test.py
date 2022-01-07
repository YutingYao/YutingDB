class Solution:
    def findTheCity(self, n: int, edges: List[List[int]], distanceThreshold: int) -> int:
        graph = [[0 if i == j else float('inf') for j in range(n)] for i in range(n)]
        for start, end, w in edges:
            graph[start][end] = w
            graph[end][start] = w

        for k in range(n):
            for start in range(n):
                for end in range(n):
                    graph[start][end] = min(graph[start][end], graph[start][k]+graph[k][end])

        neighbors = [sum(graph[i][j] <= distanceThreshold for j in range(n)) for i in range(n)]
        return min(list(range(n)), key=lambda i: (neighbors[i], -i))
