class Solution:
    def lengthOfLIS(self, nums):
        res = []
        for num in nums:
            if not res or num > res[-1]:
                res.append(num)
            else:
                l, r = 0, len(res) - 1
                idx = r
                while l <= r:
                    mid = (l + r) // 2
                    if res[mid] >= num:
                        idx = mid
                        r = mid - 1
                    else:
                        l = mid + 1
                res[idx] = num
        return len(res)