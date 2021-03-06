###229. Majority Element II



题目:
<https://leetcode-cn.com/problems/majority-element-ii/>


难度:
Medium

思路：

majority element是两两比较扔掉不同的元素，然后最后会留下一个。

这里变成三三比较来扔东西, find all elements that appear more than ⌊ n/3 ⌋ times，所以最多可以有两个majority element ii.


最后再加一个比较来确认这些函数是majority element

```py
class Solution(object):
    def majorityElement(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        cnt1 = 0
        cnt2 = 0
        maj1 = 0
        maj2 = 0
        for num in nums:
        	if maj1 == num:
        		cnt1 += 1
        	elif maj2 == num:
        		cnt2 += 1
        	elif cnt1 == 0:
        		maj1 = num
        		cnt1 += 1
        	elif cnt2 == 0:
        		maj2 = num
        		cnt2 += 1
        	else:
        		cnt1 -= 1
        		cnt2 -= 1

        cnt1 = 0
        cnt2 = 0

        n = len(nums)
        res = []
        for num in nums:
        	if maj1 == num:
        		cnt1 += 1
        	elif maj2 == num:
        		cnt2 += 1
        if cnt1 > n/3:
        	res.append(maj1)
        if cnt2 > n/3:
        	res.append(maj2)
        return res
```