###147. Insertion Sort List

题目:
<https://leetcode-cn.com/problems/insertion-sort-list/>


难度:
Medium

insertion sort 也是入门必备，一个元素本身被认为是sort的，一个简单的理解是打牌，然后进入第二个元素的时候，看它是比第一个元素大还是小，做排序，进入下一个元素的时候再看再移。

伪码

```python
for i ← 1 to length(A)-1
    j ← i
    while j > 0 and A[j-1] > A[j]
        swap A[j] and A[j-1]
        j ← j - 1
    end while
end for
```

这个伪码对于list可能适用性没有那么强，则考虑，从第二个node开始，那么从开始开始看，找到这个node应该插入的位置，插入。



就是这样，就是会超时||||

```python
class Solution(object):
    def insertionSortList(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        if head == None or head.next == None:
            return head

        dummy = ListNode(-1)
        dummy.next = head

        prev = head 
        cur = head.next

        while cur:
            p = dummy
            while p.next.val <= cur.val and p != prev:
                p = p.next
            if p != prev:
                prev.next = cur.next
                cur.next = p.next
                p.next = cur
            prev = cur
            cur = cur.next

        return dummy.next
```

