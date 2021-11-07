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
        maxSum , maxEnd = nums[0], nums[0]
        print("nums[i]:",nums[0],"maxEnd:",maxEnd)
        
        for i in range(1,n):
            maxEnd = max(nums[i],maxEnd + nums[i]) 
            # 如果永远是正数的话，后一个加前一个一定更大的，
            # 但存在负数，所以，求“我的值”和“前面最大序列”
            # 一旦我是一个负数，那么前面相加的努力就全部凉透了
            maxSum = max(maxEnd,maxSum)
            print("nums[i]:",nums[i],"maxEnd:",maxEnd)
        return maxSum
    
if __name__ == "__main__":   
	s = Solution()
	nums_1 = [-2,1,-3,4,-1,2,1,-5,4]
	nums_2 = [1, -1]
	print(s.maxSubArray(nums_1))
	print(s.maxSubArray(nums_2))