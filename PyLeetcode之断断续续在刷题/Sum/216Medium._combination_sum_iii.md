###216. Combination Sum III

题目:

<https://leetcode-cn.com/problems/combination-sum-iii/>


难度:

Medium

继续Combination Sum 系列


```python
class Solution(object):
    def combinationSum3(self, k, n):
        """
        :type k: int
        :type n: int
        :rtype: List[List[int]]
        """
        candidates = [1,2,3,4,5,6,7,8,9]
        self.res = []
        self.combSum(candidates, n, [], k)
        return self.res
        
        
    def combSum(self,candidates, target, valueList, k):
        if target == 0 and k == 0:
            self.res.append(valueList)
        length = len(candidates)
        if length == 0 or k < 0 :
            return 
        for i in range(length):
            if candidates[i] > target:
                return
            self.combSum(candidates[i+1:], target - candidates[i], valueList + [candidates[i]], k-1)
            
```