class Solution:
    def minCut(self, s: str) -> int:
        n = len(s)
        isPalinDP = [[True] * n for _ in range(n)]
        
        for start in range(n - 1, -1, -1): # start 指向 倒数第二位, start 向前扫描
            for end in range(start + 1, n): # end 指向 倒数第一位, end 向后扫描
                isPalinDP[start][end] = (s[start] == s[end]) and isPalinDP[start + 1][end - 1] 

        cutDP = [float("inf")] * n
        for end in range(n):
            # 如果前一小段是回文
            if isPalinDP[0][end]:
                cutDP[end] = 0
            # 如果前一小段不是回文，则从start开始继续拆分
            else:
                for start in range(end):
                    if isPalinDP[start + 1][end]:
                        cutDP[end] = min(cutDP[end], cutDP[start] + 1)
        
        return cutDP[n - 1]