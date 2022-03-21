class Solution:
    def triangleNumber(self, nums: List[int]) -> int:
        nums.sort()
        res = 0
        for i in range(len(nums)):
            p, q = 0, i - 1
            while p < q: # 😐😐😐 while 循环
                # 如果满足条件，则i到j之间的，所有i，都满足条件
                # j 缩小
                if nums[p] + nums[q] > nums[i]:
                    res += q - p
                    q -= 1
                # 如果不满足条件，i才需要增大，否则i可以一直躺平
                else:
                    p += 1
        return res