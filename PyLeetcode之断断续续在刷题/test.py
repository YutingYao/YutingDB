class Solution(object):
    def subarraysWithKDistinct(self, nums, K):
        return self.atMostK(nums, K) - self.atMostK(nums, K - 1)
    
    def atMostK(self, A, K):
        N = len(A)
        left, right = 0, 0
        counter = collections.Counter()
        distinct = 0
        res = 0
        while right < N:
            if counter[A[right]] == 0:
                distinct += 1
            counter[A[right]] += 1
            while distinct > K:
                counter[A[left]] -= 1
                if counter[A[left]] == 0:
                    distinct -= 1
                left += 1
            res += right - left + 1
            print(left, right, res)
            right += 1
        return res
