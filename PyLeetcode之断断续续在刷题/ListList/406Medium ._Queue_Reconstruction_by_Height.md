### 406. Queue Reconstruction by Height

题目:
<https://leetcode-cn.com/problems/queue-reconstruction-by-height/>


难度:

Medium


思路：
People are only counting (in their k-value) taller or equal-height others standing in front of them. 
So a smallest person is completely irrelevant for all taller ones. And of all smallest people, 
the one standing most in the back is even completely irrelevant for everybody else. Nobody is counting that person. 
So we can first arrange everybody else, ignoring that one person. And then just insert that person appropriately. 
Now note that while this person is irrelevant for everybody else, everybody else is relevant for this person - 
this person counts exactly everybody in front of them. So their count-value tells you exactly the index they must be standing.

So you can first solve the sub-problem with all but that one person and then just insert that person appropriately. 
And you can solve that sub-problem the same way, first solving the sub-sub-problem with all 
but the last-smallest person of the subproblem. And so on. The base case is when you have the sub-…-sub-problem of zero people. 
You’re then inserting the people in the reverse order, i.e., that overall last-smallest person in the very end 
and thus the first-tallest person in the very beginning. That’s what the above solution does, 
Sorting the people from the first-tallest to the last-smallest, and inserting them one by one as appropriate.

参考[stefan](https://leetcode.com/problems/queue-reconstruction-by-height/discuss/89359)

```python
class Solution(object):
    def reconstructQueue(self, people):
        """
        :type people: List[List[int]]
        :rtype: List[List[int]]
        """
        people.sort(key=lambda (h, k): (-h, k))
        queue = []
        for p in people:
            queue.insert(p[1], p)
        return queue
```


