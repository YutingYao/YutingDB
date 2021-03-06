### 324. Wiggle Sort II



题目:
<https://leetcode-cn.com/problems/wiggle-sort-ii/>


难度:
Medium

思路：

首先这道题和[Wiggle Sort](https://github.com/Lisanaaa/thinking_in_lc/blob/master/280._Wiggle_Sort.md)要求不一样，不能有等于，
所以如果碰到一串```‘1,1,1,1,1,1’```，当调换顺序时候还是不会满足。

因此我们用新方法，首先将原数组排序，然后大的那一半数字降序插在奇数```index```上，小的那一半数字降序插在偶数```index```上


```python
class Solution(object):
    def wiggleSort(self, nums):
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        nums.sort()
        half = len(nums[::2])
        nums[::2], nums[1::2] = nums[:half][::-1], nums[half:][::-1]
```


### Follow up
O(n) time, O(1) space

思路：
首先想到的是将我们上面的排序方法用堆排序实现即可，建堆O(n)，调整堆O(lgN)


```python

```
