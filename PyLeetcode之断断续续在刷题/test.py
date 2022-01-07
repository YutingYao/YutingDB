import heapq
class Solution:
    # Dijkstra🚗+剪枝
    def findCheapestPrice(self, n: int, flights: List[List[int]], src: int, dst: int, k: int) -> int:
        if src == dst:
            return 0

        graph = collections.defaultdict(list)

        for start, end, cost in flights:
            graph[start].append((end, cost))

        disInter = {}
        que = [(0, src, -1)]
        while que:
            costmin, start, interval = heapq.heappop(visited)
            # 这个部分很重要，一定要k+1
            if interval > k:
                continue
            if start == dst:
                return costmin
            for end, cost in graph[start]:
                # 这一步剪枝很重要
                if costmin + cost < disInter.get((end,interval+1), float("inf")):
                    heapq.heappush(visited, (costmin + cost, end, interval + 1))
                    disInter[(end,interval+1)] = costmin + cost
                # print(dist)
                # {(1, 1): 100}
                # {(1, 1): 100, (2, 1): 500}
                # {(1, 1): 100, (2, 1): 500, (2, 2): 200}
        return -1 