class Solution:
    def findCheapestPrice(self, n: int, flights: List[List[int]], src: int, dst: int, k: int) -> int:
        tmp = [float("inf")] * n
        tmp[src] = 0
        res = float("inf")
        for _ in range(1, k + 2):
            dp = [float("inf")] * n
            for j, i, cost in flights:
                dp[i] = min(dp[i], tmp[j] + cost)
            tmp = dp
            res = min(res, tmp[dst])
        
        return -1 if res == float("inf") else res