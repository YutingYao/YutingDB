class Solution:
    def maxSlidingWindow(self, nums: List[int], k: int) -> List[int]:
        winQ = deque()
        res = []
        for r, v in enumerate(nums):
            # 如果新来的数字更大
            while winQ and nums[winQ[-1]] < v:
                winQ.pop()
            winQ.append(r)
            # 如果出界
            l = winQ[0]
            if r - k == l:
                winQ.popleft()
            # 开始写入答案
            if r >= k - 1:
                res.append(nums[winQ[0]])

        return res