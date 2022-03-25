class Solution:
    def findNumberOfLIS(self, nums: List[int]) -> int:
        n = len(nums)
        if n <= 1: return n

        dp = [1 for _ in range(n)] # dp[i] 表示以 nums[i] 结尾的最长的子序列的长度
        cnt = [1 for _ in range(n)]

        maxCount = 0
        for end in range(1, n):
            for stt in range(end):
                
                if nums[end] > nums[stt]:
                    if dp[stt] + 1 > dp[end] : # 更长，则更新最长的长度和个数
                        dp[end] = dp[stt] + 1
                        cnt[end] = cnt[stt]
                    elif dp[stt] + 1 == dp[end] : # 相等时，把个数加上去
                        cnt[end] += cnt[stt]
                if dp[end] > maxCount:
                    maxCount = dp[end] # 统计最长的序列的所有次数
        res = 0
        for end in range(n):
            if maxCount == dp[end]: # 长度和个数一一对应
                res += cnt[end]
        return res