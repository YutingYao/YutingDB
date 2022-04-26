class Solution:
    def largestNumber(self, nums: List[int]) -> str:
        # 按照字典序由大到小排序
        nums = sorted([str(x) for x in nums], reverse = True)
        for stt in range(len(nums) - 1):
            for end in range(stt, len(nums)):
        # [3,30,34,5,9] 的 3,30 不能按照字典序排序，需要交换位置
                if str(nums[stt]) + str(nums[end]) < str(nums[end]) + str(nums[stt]):
                    nums[stt], nums[end] = nums[end], nums[stt]
        return str(int(''.join(nums)))