### 34. Search for a Range



题目：

 https://leetcode-cn.com/problems/search-for-a-range/



难度 : Medium



思路：

二分法，先找```target```出现的左边界，判断是否有```target```后再判断右边界

- 找左边界：二分，找到一个```index```
    - 该```index```对应的值为```target```  
    - 并且它左边```index-1```对应的值不是```target```（如果```index```为```0```则不需要判断此条件）
    - 如果存在```index```就将其```append```到```res```中
- 判断此时```res```是否为空，如果为空，说明压根不存在```target```，返回```[-1, -1]```
- 找右边界：二分，找到一个```index```（但是此时用于二分循环的```l```可以保持不变，```r```重置为```len(nums)-1```，这样程序可以更快一些）
    - 该```index```对应的值为```target```
    - 并且它右边```index+1```对应的值不是```target```（如果```index```为```len(nums)-1```则不需要判断此条件）   
    - 如果存在```index```就将其```append```到```res```中



AC 代码




```python
class Solution(object):
    def searchRange(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: List[int]
        """
        if not nums : return [-1, -1]

        res = []
        l, r = 0, len(nums)-1
        # search for left bound
        while l <= r:
            mid = l + ((r - l) >> 2)
            if nums[mid] == target and (mid == 0 or nums[mid-1] != target):
                res.append(mid)
                break
            if nums[mid] < target:
                l = mid + 1
            else:
                r = mid - 1
        if not res:
            return [-1, -1]
        # search for right bound
        r = len(nums)-1
        while l <= r:
            mid = l + ((r - l) >> 2)
            if nums[mid] == target and (mid == len(nums)-1 or nums[mid+1] != target):
                res.append(mid)
                break
            if nums[mid] > target:
                r = mid - 1
            else:
                l = mid + 1       
        return res
```





