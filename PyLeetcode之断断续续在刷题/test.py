class Solution(object):
    def rob(self, nums):

        def RobRange(start,end):
            #前1个，前2个
            dp1=0
            dp2=0
            for i in range(start, end + 1):
                maxdp = max(dp1, dp2 + nums[i])
                dp2 = dp1
                dp1 = maxdp
            return maxdp

        n = len(nums)
        if n == 1:
            return nums[0]
        return max(RobRange(0,n-2),RobRange(1,n-1))