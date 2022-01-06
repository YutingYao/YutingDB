class Solution:
    def minimumEffortPath(self, heights) -> int:
        m, n = len(heights), len(heights[0])
        distances = defaultdict(lambda: float('inf'))
        dirs = [(0, 1), (1, 0), (0, -1), (-1, 0)]
        que = []
        heappush(que, (0, 0, 0))
        while que:
            effort, x, y = heappop(que)
            if (x, y) == (m - 1, n - 1):
                return effort
            for dx, dy in dirs:
                nx, ny = x + dx, y + dy
                if 0 <= nx < m and 0 <= ny < n:
                    tmp = max(effort, abs(heights[nx][ny] - heights[x][y]))
                    if distances[(nx, ny)] > tmp:
                        distances[(nx, ny)] = tmp
                        heappush(que, (tmp, nx, ny))