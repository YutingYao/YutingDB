#  435. Non-overlapping Intervals
**<font color=red>难度: 中等</font>**

## 刷题内容

> 原题连接

* https://leetcode-cn.com/problems/non-overlapping-intervals

> 内容描述

```
Given a collection of intervals, find the minimum number of intervals you need to remove to make the rest of the intervals non-overlapping.

Note:
You may assume the interval's end point is always bigger than its start point.
Intervals like [1,2] and [2,3] have borders "touching" but they don't overlap each other.
Example 1:
Input: [ [1,2], [2,3], [3,4], [1,3] ]

Output: 1

Explanation: [1,3] can be removed and the rest of intervals are non-overlapping.
Example 2:
Input: [ [1,2], [1,2], [1,2] ]

Output: 2

Explanation: You need to remove two [1,2] to make the rest of intervals non-overlapping.
Example 3:
Input: [ [1,2], [2,3] ]

Output: 0

Explanation: You don't need to remove any of the intervals since they're already non-overlapping.
```

## 解题方案

> 思路 1

先按照start排序，然后每次如果下一个的start小于前一个的end的时候意味着我们需要删掉一个了，但是我们尽量留下end比较小的那个Interval，具体看代码会比较清晰

```python
# Definition for an interval.
# class Interval(object):
#     def __init__(self, s=0, e=0):
#         self.start = s
#         self.end = e

class Solution(object):
    def eraseOverlapIntervals(self, intervals):
        """
        :type intervals: List[Interval]
        :rtype: int
        """
        if not intervals or len(intervals) == 0:
            return 0
        res = 0
        intervals = sorted(intervals, key=lambda x:x.start)
        cur_end = intervals[0].end
        for i in range(1, len(intervals)):
            if intervals[i].start < cur_end: #overlap
                res += 1   
                cur_end = min(intervals[i].end, cur_end) ## 尽量留下end小的Interval
            else:
                cur_end = intervals[i].end
        return res
```

> 思路 2

又发现有大佬比我牛p多了，代码更nice，跟646题比较像

首先按照end排序，我们可以知道的，一旦后面一个Interval的start比前一个的end小的话，这个时候我们就需要删除掉当前的这个Interval, 
反之则前面的那些Interval已经成立了，我们只需要更新cur_end为当前Interval的end


```python
class Solution(object):
    def eraseOverlapIntervals(self, intervals):
        """
        :type intervals: List[Interval]
        :rtype: int
        """
        intervals.sort(key = lambda x: x.end)
        res, cur_end = 0, -float("inf")
        for i in intervals:
            if cur_end > i.start: res += 1
            else: cur_end = i.end
        return res
```



