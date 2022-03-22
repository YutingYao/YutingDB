class Solution:
    def nextGreaterElements(self, nums: List[int]) -> List[int]:
        res = [-1] * len(nums)
        stackI = []
        # 双倍nums大法好
        for idx, cur in enumerate(nums + nums):
            while stackI and nums[stackI[-1]] < cur: # 😐 while 循环 + pop + append
                res[stackI[-1]] = cur
                stackI.pop()
            if idx < len(nums): # 易错点：append(idx)是有条件的
                stackI.append(idx)
        return res