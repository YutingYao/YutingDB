### 23. Merge k Sorted Lists



题目:
<https://leetcode-cn.com/problems/merge-k-sorted-lists/>


难度:
Hard

思路：

看到思路有heap，similar question有ugly number|| -> 这个是用heapq来解决的

那么就用heap吧？ heapsort

最简单的做法是只要每个list里面还有node，就把他们扔到minheap里面去，然后再把minheap pop，一个一个node连起来，听起来时间复杂度和空间复杂度都蛮高的。
直接merge必然是不好的，因为没有利用有序这个点，应该做的是每次取来一个，然后再把应该的下一个放入

写到这里瞬间明白和ugly number ii像的点了，甚至感觉跟find in sorted matrix ii也像

```python
class Solution(object):
    def mergeKLists(self, lists):
        """
        :type lists: List[ListNode]
        :rtype: ListNode
        """
        import heapq
        h = []
        for lst_head in lists:
            if lst_head:
                heapq.heappush(h, (lst_head.val, lst_head))
        cur = ListNode(-1)
        dummy = cur
        while h:
            smallest_node = heapq.heappop(h)[1]
            cur.next = smallest_node
            cur = cur.next
            if smallest_node.next:
                heapq.heappush(h, (smallest_node.next.val, smallest_node.next))
        return dummy.next
```

当然还像merge two sorted list
