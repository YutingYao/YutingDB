class Solution:
    def isMatch(self, s: str, p: str) -> bool:
        m, n = len(s), len(p)

        dp = [[False] * (n + 1) for _ in range(m + 1)]

        dp[0][0] = True # dp[0][0]:什么都没有,所以为true
        for pi in range(1, n + 1):
            if p[pi - 1] == '*': 
                dp[0][pi] = True
            else:
                break
        # 也可以这么写
        # for j in range(1, n + 1):
        #     if p[j - 1] == "*":
        #         dp[0][j] = dp[0][j - 1]
        
        for si in range(1, m + 1):
            for pi in range(1, n + 1):
                if p[pi - 1] == '*': # 如果p[j] == "*" && (dp[i-1][j] = true || dp[i][j-1] = true) 有dp[i][j] = true
                    dp[si][pi] = dp[si][pi - 1] | dp[si - 1][pi] 
                    # ​ dp[i-1][j],表示*代表是空字符,例如ab,ab*
                    # ​ dp[i][j-1],表示*代表非空任何字符,例如abcd,ab*
                elif p[pi - 1] == '?' or s[si - 1] == p[pi - 1]: # 如果(s[i] == p[j] || p[j] == "?") && dp[i-1][j-1] ,有dp[i][j] = true
                    dp[si][pi] = dp[si - 1][pi - 1]
                
        return dp[m][n]