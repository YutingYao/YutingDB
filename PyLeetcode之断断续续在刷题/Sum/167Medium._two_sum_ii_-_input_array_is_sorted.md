### 167. Two Sum II - Input array is sorted



题目:
<https://leetcode-cn.com/problems/two-sum-ii-input-array-is-sorted/>


难度:
Medium


思路：


双指针

```python
class Solution(object):
    def twoSum(self, numbers, target):
        """
        :type numbers: List[int]
        :type target: int
        :rtype: List[int]
        """
        l, r = 0, len(numbers) - 1
        while l < r:
            if numbers[l] + numbers[r] == target:
                return [l+1, r+1]
            elif numbers[l] + numbers[r] > target:
                r -= 1
            else:
                l += 1

```
