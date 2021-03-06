### 228. Summary Ranges

题目:
<https://leetcode-cn.com/problems/summary-ranges/>


难度:

Medium


Just collect the ranges, then format and return them.

```python
class Solution(object):
    def summaryRanges(self, nums):
        """
        :type nums: List[int]
        :rtype: List[str]
        """
        ranges = []
        for i in nums:
            if not ranges or i > ranges[-1][-1] + 1:
                ranges += [],
            ranges[-1][1:] = i,
        return ['->'.join(map(str, r)) for r in ranges]                           
```
About the commas :-)

```python
ranges += [],
r[1:] = n,
```
Why the trailing commas? Because it turns the right hand side into a tuple and I get the same effects as these more common alternatives:
```
ranges += [[]]
or
ranges.append([])

r[1:] = [n]
```
Without the comma, …

- ranges += [] wouldn’t add [] itself but only its elements, i.e., nothing.
- r[1:] = n wouldn’t work, because my n is not an iterable.

Why do it this way instead of the more common alternatives I showed above? Because it’s shorter and faster (according to tests I did a while back).

写到这里可能又有疑问了🤔️，为什么不可以直接写```ranges[-1][1] = i```呢，当然是会报```IndexError: list assignment index out of range```错误啦，那为什么```ranges[-1][1:] = i,```可以呢？

简单来说

L1=L 与 L1=L[:] 
- L1和L  都是对同一个对象的引用（所谓绑定的意思）。
- L[:]  是生成了一个和L不同的新的对象，L1 变为了L[:] 这个对象的引用。


参考[stefan](https://leetcode.com/problems/summary-ranges/discuss/63193)
