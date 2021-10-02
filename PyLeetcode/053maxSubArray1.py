class Solution(object):
    def maxSubArray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        n = len(nums)
        maxSum = [nums[0] for i in range(n)]
        print(maxSum)
        for i in range(1,n):
        	maxSum[i] = max(maxSum[i-1] + nums[i], nums[i])
            # maxSum[i] 用来保存前面一个子序列中的最大值
        return max(maxSum)
    

    


if __name__ == "__main__":   
	s = Solution()
	nums_1 = [-2,1,-3,4,-1,2,1,-5,4]
	nums_2 = [1, -1]
	print(s.maxSubArray(nums_1))
	print(s.maxSubArray(nums_2))