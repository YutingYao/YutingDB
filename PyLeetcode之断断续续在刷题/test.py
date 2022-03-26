class Solution:
    def isInterleave(self, string1: str, string2: str, stringtar: str) -> bool:
        n1, n2, tar = len(string1), len(string2), len(stringtar)
        if n1 + n2 != tar: return False

        dp = [False] * (n2 + 1)
        dp[0] = True
        for i1 in range(n1 + 1):
            for i2 in range(n2 + 1):
                '''
                i1 为 1 ~ n1
                i2 为 1 ~ n2
                p 为 0 ~ n1 + n2 - 1
                '''
                p = i1 + i2 - 1
                if i1: # s1 和 s3 比较
                    dp[i2] = dp[i2] and string1[i1 - 1] == stringtar[p]
                if i2: # s2 和 s3 比较
                    dp[i2] = dp[i2] or (dp[i2 - 1] and string2[i2 - 1] == stringtar[p])
        return dp[n2]