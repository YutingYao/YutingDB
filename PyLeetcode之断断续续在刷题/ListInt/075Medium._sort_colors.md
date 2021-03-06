#  75. Sort Colors
**<font color=red>难度: 中等</font>**

## 刷题内容

> 原题连接

* https://leetcode-cn.com/problems/sort-colors

> 内容描述

```
Given an array with n objects colored red, white or blue, sort them in-place so that objects of the same color are adjacent, with the colors in the order red, white and blue.

Here, we will use the integers 0, 1, and 2 to represent the color red, white, and blue respectively.

Note: You are not suppose to use the library's sort function for this problem.

Example:

Input: [2,0,2,1,1,0]
Output: [0,0,1,1,2,2]
Follow up:

A rather straight forward solution is a two-pass algorithm using counting sort.
First, iterate the array counting number of 0's, 1's, and 2's, then overwrite array with total number of 0's, then 1's and followed by 2's.
Could you come up with a one-pass algorithm using only constant space?
```

## 解题方案

> 思路 1 

先算一下0, 1, 2分别有多少个，然后in-place改呗，简单, beats 100%

```python
class Solution(object):
    def sortColors(self, nums):
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        red, white, blue = 0, 0, 0
        for i in nums:
            if i == 0:
                red += 1
            elif i == 1:
                white += 1
        for i in range(red):
            nums[i] = 0
        for i in range(red, red+white):
            nums[i] = 1
        for i in range(red+white, len(nums)):
            nums[i] = 2
```


> 思路 2

这个问题是 Dutch National Flag Problem， 荷兰旗问题
<https://en.wikipedia.org/wiki/Dutch_national_flag_problem>


思路其实是类似partition的，比x小的放左边，比x大的放右边。

这里是用三个指针，begin, cur, end，cur需要遍历整个数组

- cur 指向0，交换begin与cur， begin++,cur++
- cur 指向1，不做任何交换，cur++
- cur 指向2，交换end与cur，end--

之所以cur指向2，交换之后不前进是因为我们end交换过来的是0或者1，如果是0那么明显我们需要做进一步的处理，所以最终判断条件是end < cur应该就结束了

这样的three-way-partition也只是3-way好用吧？如果有4个数，那么这样则是无效的，或者如果是4-way，那么可以转换成3-way+2-way


```python
class Solution(object):
    def sortColors(self, nums):
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        begin, cur, end = 0, 0, len(nums) - 1

        while cur <= end:
        	if nums[cur] == 0:
        		nums[begin], nums[cur] = nums[cur], nums[begin]
        		cur += 1
        		begin += 1
        	elif nums[cur] == 1:
        		cur += 1
        	else: # nums[cur] == 2
        		nums[cur], nums[end] = nums[end], nums[cur]
        		end -= 1
```

> 思路 3

两个指针也可以

```python
class Solution(object):
    def sortColors(self, nums):
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        i, l, r = 0, 0, len(nums) - 1
        while i < len(nums):
            if nums[i] == 2 and i < r:
                nums[i], nums[r] = nums[r], 2
                r -= 1
            elif nums[i] == 0 and i > l:
                nums[i], nums[l] = nums[l], 0
                l += 1
            else:
                i += 1
```

> 思路 4

这个方法就很巧妙了，我们遍历整个数组，只要碰到了什么数字我们就把这个数字往右边推一下

大家可以用例子[2,0,2,1,1,0]自己推导一下过程，看看是不是有一种向右推的感觉


```python
class Solution(object):
    def sortColors(self, nums):
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        n0, n1, n2 = -1, -1, -1
        for i in range(len(nums)):
            if nums[i] == 0:
                n0, n1, n2 = n0+1, n1+1, n2+1
                nums[n2] = 2
                nums[n1] = 1
                nums[n0] = 0
            elif nums[i] == 1:
                n1, n2 = n1+1, n2+1
                nums[n2] = 2
                nums[n1] = 1
            else:
                n2 += 1
                nums[n2] = 2
```
