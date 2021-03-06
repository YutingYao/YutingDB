### 16. 3Sum Closest

题目:
<https://leetcode-cn.com/problems/3sum-closest/>


难度:

Medium 


思路：
跟3 Sum一样，固定一个元素

用两个指针来循环


```python
class Solution(object):
    def threeSumClosest(self, nums, target):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        n, res, diff = len(nums), None, float('inf')
        nums.sort()
        for i in range(n):
            if i > 0 and nums[i] == nums[i-1]:
                continue
            l, r = i+1, n-1
            while l < r:
                tmp = nums[i] + nums[l] + nums[r]
                if tmp == target:
                    return target
                elif tmp > target:
                    r -= 1
                    if abs(tmp-target) < diff:
                        diff = abs(tmp-target)
                        res = tmp
                    while l < r and nums[r] == nums[r+1]:
                        r -= 1    
                else:
                    l += 1
                    if abs(tmp-target) < diff:
                        diff = abs(tmp-target)
                        res = tmp
                    while l < r and nums[l] == nums[l-1]:
                        l += 1 
        return res

```
