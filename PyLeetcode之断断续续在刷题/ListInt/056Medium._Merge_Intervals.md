### 56. Merge Intervals

题目:
<https://leetcode-cn.com/problems/merge-intervals/>


难度:

Medium


Just go through the intervals sorted by start coordinate and 
either combine the current interval with the previous one if they overlap, or add it to the output by itself if they don’t.

```python
class Solution(object):
    def merge(self, intervals):
        """
        :type intervals: List[Interval]
        :rtype: List[Interval]
        """
        res = []
        for i in sorted(intervals, key = lambda i: i.start):
            if res and i.start <= res[-1].end:
                res[-1].end = max(i.end, res[-1].end)
            else:
                res.append(i)
        return res
```


