### 280. Wiggle Sort





题目： 
<https://leetcode-cn.com/problems/wiggle-sort/>



难度 : Medium



思路：



想的是比如bubble sort或者任何简单的比较sort，只是放数字的时候是按这样的大小顺序放：

1, n, 2, n-1,3, n-2….

或者每个pass其实做两个sort，找出最大的和最小的。然后分别放在头尾。



这样的写法TLE:

```py
class Solution(object):
    def wiggleSort(self, nums): # 此法超时
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        n = len(nums)
        for i in range(n):
        	# small bubble sort
        	if i % 2 == 0:
        		for j in range(n-1, i-1, -1):
        			if nums[j] > nums[j-1]:
        				nums[j], nums[j-1] = nums[j-1],nums[j]
        	else:
        		for j in range(n-1, i-1, -1):
        			if nums[j] < nums[j-1]:
        				nums[j], nums[j-1] = nums[j-1],nums[j]
```





但是貌似想复杂了，其实对于这个简单化，要求只有一个：

1. 如果i是奇数，nums[i] >= nums[i - 1]
2. 如果i是偶数，nums[i] <= nums[i - 1]

所以我们只要遍历一遍数组，把不符合的情况交换一下就行了。具体来说，如果nums[i] > nums[i - 1]， 则交换以后肯定有nums[i] <= nums[i - 1]。



AC 代码

```python
class Solution(object):
    def wiggleSort(self, nums):
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        for i in xrange(1, len(nums)):
            if ((i % 2) and nums[i] < nums[i-1]) or ((not i % 2) and nums[i] > nums[i-1]):
                nums[i], nums[i-1] = nums[i-1], nums[i]
```



