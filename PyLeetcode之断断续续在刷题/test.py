class Solution:
    def countRestrictedPaths(self, n: int, edges: List[List[int]]) -> int:
        mod = 10 ** 9 + 7
        costdic = {}
        graph = collections.defaultdict(list)
        for start,end,c in edges:
            graph[start - 1].append(end - 1)
            graph[end - 1].append(start - 1)
            costdic[(start - 1,end - 1)] = c
            costdic[(end - 1,start - 1)] = c

        costsum = {n - 1:0}
        queue = [[0,n - 1]]
        while queue:
            costmin, start = heapq.heappop(queue)
            for end in graph[start]:
                if end not in costsum or costsum[end] > costmin + costdic[(start,end)]:
                    heapq.heappush(queue,[costmin + costdic[(start,end)],end])
                    costsum[end] = costmin + costdic[(start,end)]
        
        @lru_cache(None)
        def dfs(start):
            nonlocal costsum
            if start == n - 1:
                return 1
            res = 0
            for end in graph[start]:
                if costsum[end] < costsum[start]:
                    res += dfs(end) % mod
            return res
        
        return dfs(0) % mod