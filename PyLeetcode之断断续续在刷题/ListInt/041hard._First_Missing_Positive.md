#  41. First Missing Positive
**<font color=red>难度: 困难</font>**

## 刷题内容

> 原题连接

* https://leetcode-cn.com/problems/first-missing-positive

> 内容描述

```
Given an unsorted integer array, find the smallest missing positive integer.

Example 1:

Input: [1,2,0]
Output: 3
Example 2:

Input: [3,4,-1,1]
Output: 2
Example 3:

Input: [7,8,9,11,12]
Output: 1
Note:

Your algorithm should run in O(n) time and uses constant extra space.
```

## 解题方案

> 思路 1

题目要求O(n)时间和O(1)空间，所以我们知道先排序再循环找是不行的

因此我们可以这样，第一轮循环，找1(因为1是最小的正整数)，如果1在，立马原地开始继续找2，以此类推，一轮循环结束后，我们记录下当前正在找的值i，
并开始第二轮循环，但是这次从i开始找了，然后以此类推，直到有一次循环我们要找的值没有变过，则代表它没出现过，返回它即可

可以看一个例子
```
[3,4,-1,1]

第一轮循环：[1,1,1,2]
第二轮循环：[2,2,2,2]

然后我们发现第二轮循环2没有变过了，所以2就是我们要的结果
```

```python
class Solution(object):
    def firstMissingPositive(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        old_missing, missing = 0, 1
        while old_missing != missing:
            old_missing = missing
            for i in range(len(nums)):
                if nums[i] == missing:
                    missing += 1
        return missing
```

> 思路 2

如果不限制空间的话，我们用一个dict就可以解决, 时间是O(N)


```python
class Solution(object):
    def firstMissingPositive(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if not nums:
            return 1
        lookup = {}
        for i in nums:
            lookup[i] = 1
        for i in range(1, max(nums)+2):
            if i not in lookup:
                return i
```



