class Solution:
    def uniquePaths(self, m, n):
        """
        :type m: int
        :type n: int
        :rtype: int
        """
        # 构建一个m*n的矩阵，后一个是前几个累加
        if m < 1 or n < 1:
            return 0
        dp = [0] *n #第一行
        dp[0] = 1    #第一列
        for i in range(0,m):
            for j in range(1,n):
                dp[j] += dp[j-1]
        return dp[n-1]

if __name__ == "__main__":      
	s = Solution()
	print(s.uniquePaths(7,3))