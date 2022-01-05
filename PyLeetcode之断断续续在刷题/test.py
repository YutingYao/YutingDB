class Solution:
    def maxProduct(self, nums: List[int]) -> int:
        if not nums: return 
        res = nums[0]
        maxdp = nums[0]
        mindp = nums[0]
        for num in nums[1:]:
            maxdp, mindp = max(maxdp * num, mindp * num, num), min(maxdp * num, mindp * num, num)
            res = max(res, maxdp)
        return res