### 18. 4Sum

题目:
<https://leetcode-cn.com/problems/4sum/>


难度:

Medium 


思路：

用3sum改

固定两个数，活动别的


```python
class Solution(object):
    def fourSum(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: List[List[int]]
        """
        n = len(nums)
        nums.sort()
        res = []
        for i in range(n):
        	for j in range(i+1,n):
        		l, r = j+1, n-1
        		while l < r:
        			temp = nums[i] + nums[j] + nums[l] + nums[r]
        			if temp == target:
        				if [nums[i],nums[j],nums[l],nums[r]] not in ans:
        					ans.append([nums[i],nums[j],nums[l],nums[r]])
        				l += 1
        				r -= 1
        			elif temp > target:
        				r -= 1
        			else:
        				l+=1
        return ans
```

可以通过加判断条件，前后数字相等可以直接跳过，使得算法更快


```python
class Solution(object):
    def fourSum(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: List[List[int]]
        """
        n, res = len(nums), []
        nums.sort()
        for i in range(n):
            if i > 0 and nums[i] == nums[i-1]:   # 因为i=0这个元素会直接往下执行
                continue
            for j in range(i+1, n):
                if j > i+1 and nums[j] == nums[j-1]:   # 因为j=i+1这个元素会直接往下执行
                    continue
                l, r = j+1, n-1
                while l < r:
                    tmp = nums[i] + nums[j] + nums[l] + nums[r]
                    if tmp == target:
                        res.append([nums[i], nums[j], nums[l], nums[r]])
                        l += 1
                        r -= 1
                        while l < r and nums[l] == nums[l-1]: 
                            l += 1
                        while l < r and nums[r] == nums[r+1]: 
                            r -= 1
                    elif tmp > target:
                        r -= 1
                    else:
                        l += 1
        return res

```

还可以再用一些判断来加速，比如枚举第一个数的时候

- nums[i] + nums[i + 1] + nums[i + 2] + nums[i + 3] > target: break
这是当前能凑齐的最小的4个数，比target后面都不用做了
- nums[i] + nums[n – 3] + nums[n – 2] + nums[n – 1] < target: continue
这是当前凑齐的最大的4个数，比target小，说明第一个数不够大

参考

<https://www.hrwhisper.me/leetcode-2-sum-3-sum-4-sum-3-sum-closest-k-sum/>


```python
class Solution(object):
    def fourSum(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: List[List[int]]
        """
        n, res = len(nums), []
        nums.sort()
        for i in range(n):
            if i > 0 and nums[i] == nums[i-1]:   # 因为i=0这个元素会直接往下执行
                continue
            if i+3 <= n-1:
                if nums[i] + nums[i+1] + nums[i+2] + nums[i+3] > target:
                    break
            if i < n-3:
                if nums[i] + nums[n-3] + nums[n-2] + nums[n-1] < target:
                    continue
            for j in range(i+1, n):
                if j > i+1 and nums[j] == nums[j-1]:   # 因为j=i+1这个元素会直接往下执行
                    continue
                l, r = j+1, n-1
                while l < r:
                    tmp = nums[i] + nums[j] + nums[l] + nums[r]
                    if tmp == target:
                        res.append([nums[i], nums[j], nums[l], nums[r]])
                        l += 1
                        r -= 1
                        while l < r and nums[l] == nums[l-1]: 
                            l += 1
                        while l < r and nums[r] == nums[r+1]: 
                            r -= 1
                    elif tmp > target:
                        r -= 1
                    else:
                        l += 1
        return res
        
```
