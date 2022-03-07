class Solution(object):
    def shortestSubarray(self, nums, k):
        n = len(nums)
        presum = [0]
        for x in nums:
            presum.append(presum[-1] + x)

        res = n + 1 
        queI = collections.deque()  
        for i, Py in enumerate(presum):
            while queI and Py - presum[queI[-1]] <= 0:
                queI.pop()

            while queI and Py - presum[queI[0]] >= k:
                res = min(res, i - queI.popleft())

            queI.append(i)

        return res if res < n+1 else -1