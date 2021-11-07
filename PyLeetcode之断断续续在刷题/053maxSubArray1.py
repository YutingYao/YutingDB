# 给定一个整数数组 nums ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。

# 示例：

# 输入: [-2,1,-3,4,-1,2,1,-5,4],
# 输出: 6
# 解释: 连续子数组 [4,-1,2,1] 的和最大，为 6。

# 进阶：
# 如果你已经实现复杂度为 O(n) 的解法，尝试使用更为精妙的分治法求解。

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