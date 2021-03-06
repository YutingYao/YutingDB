### 33. Search in Rotated Sorted Array

题目:
<https://leetcode-cn.com/problems/search-in-rotated-sorted-array/>


难度:
Medium


思路：



下面是rotated-array图解，

![rotated-array图解](/img/Algorithm/LeetCode/rotated-array12:09:2017.jpg)


所以直接用二分，O(lg(n))
- 如果是mid，return mid
- 如果mid在绿色线上，就对绿色线进行二分
- 如果mid在红色线上，就对红色线进行二分
- 都没找到，return -1


```python
class Solution(object):
    def search(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        l, r = 0, len(nums) - 1
        while l <= r:
            mid = l + ((r - l) >> 2)
            if nums[mid] == target:
                return mid
            if nums[mid] < nums[r]:
                if nums[mid] < target <= nums[r]:
                    l = mid + 1
                else:
                    r = mid - 1
            else:
                if nums[l] <= target < nums[mid]:
                    r = mid - 1
                else:
                    l = mid + 1
        return -1
```


