### 15. 3Sum

题目:
<https://leetcode-cn.com/problems/3sum/>


难度:

Medium 


第一想法，先把nums排序，用三个loop，无法AC

```python
class Solution(object):
    def threeSum(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        n = len(nums)
        res = []
        nums.sort()
        for i in range(n):
            for j in range(i,n):
                for k in range(j,n):
                    if nums[i] + nums[j] + nums[k] == 0 and j != i and k != j and k != i: 
                        curRes = [nums[i],nums[j],nums[k]]
                        if curRes not in res:
                            res.append(curRes)
    
        return res
```


然后查了一下2sum，用2sum的花样，因为要排除重复以及输出是按照从小到大的输出:但是还是超时


```python
class Solution(object):  # 此法也超时
    def threeSum(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        def twoSum(nums, target):
            """
            :type nums: List[int]
            :type target: int
            :rtype: List[int]
            """
            lookup = {}
            for num in nums:
                if target - num in lookup:
                    if (-target ,target - num, num) not in res:
                        res.append((-target ,target - num, num))
                lookup[num] = target - num

        n = len(nums)
        nums.sort()
        res = []
        for i in range(n):
            twoSum(nums[i+1:], 0-nums[i])
        return [list(i) for i in res]
```


谷歌看别人的代码，思路非常清晰的,运行起来比直接调用 Two Sum快.

清晰的思路：

- 排序
- 固定左边，如果左边重复，继续
- 左右弄边界，去重，针对不同的左右边界情况处理


```python
class Solution(object):
    def threeSum(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        n, res = len(nums), []
        nums.sort()
        for i in range(n):
            if i > 0 and nums[i] == nums[i-1]:   # 因为i=0这个元素会直接往下执行
                continue
            l, r = i+1, n-1
            while l < r:
                tmp = nums[i] + nums[l] + nums[r]
                if tmp == 0:
                    res.append([nums[i], nums[l], nums[r]])
                    l += 1
                    r -= 1
                    while l < r and nums[l] == nums[l-1]: 
                        l += 1
                    while l < r and nums[r] == nums[r+1]: 
                        r -= 1
                elif tmp > 0:
                    r -= 1
                else:
                    l += 1
        return res
```

