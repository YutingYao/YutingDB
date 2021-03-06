###148. Sort List

题目:
<https://leetcode-cn.com/problems/sort-list/>


难度:
Medium

MergeSort

merge sort是必备，divide & conquer的入门之物。

merge sort做两件事， sort 和 merge。

看一看标准伪码：

```python
function mergesort(m)
   var list left, right, result
   if length(m) ≤ 1
       return m
   else
       var middle = length(m) / 2
       for each x in m up to middle - 1
           add x to left
       for each x in m at and after middle
           add x to right
       left = mergesort(left)
       right = mergesort(right)
       if last(left) ≤ first(right) 
          append right to left
          return left
       result = merge(left, right)
       return result

function merge(left,right)
   var list result
   while length(left) > 0 and length(right) > 0
       if first(left) ≤ first(right)
           append first(left) to result
           left = rest(left)
       else
           append first(right) to result
           right = rest(right)
   if length(left) > 0 
       append rest(left) to result
   if length(right) > 0 
       append rest(right) to result
   return result
```

另一处获得伪码

```python
MergeSort(arr[], l,  r)
If r > l
     1. Find the middle point to divide the array into two halves:  
             middle m = (l+r)/2
     2. Call mergeSort for first half:   
             Call mergeSort(arr, l, m)
     3. Call mergeSort for second half:
             Call mergeSort(arr, m+1, r)
     4. Merge the two halves sorted in step 2 and 3:
             Call merge(arr, l, m, r)
```


merge sort用在linked list上的好处是不用开辟空间，然后就处理node

用旧的代码拼装出来的结果

然后需要注意的一点是拆分链表，所以有设置left node 的tail为None的操作.

```python
class Solution(object):
    def sortList(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        if head == None or head.next == None:
            return head

        mid = self.findMid(head)
        # split the 
        l1 = head
        l2 = mid.next
        mid.next = None

        l1 = self.sortList(l1)
        l2 = self.sortList(l2)

        return self.mergeTwoLists(l1, l2)

    def mergeTwoLists(self, l1, l2):
        """
        :type l1: ListNode
        :type l2: ListNode
        :rtype: ListNode
        """
        if l1 == None:
            return l2
        if l2 == None:
            return l1
        
        dummy = ListNode(-1)
        cur = dummy
        
        while l1 and l2:
            if l1.val < l2.val:
                cur.next = l1
                l1 = l1.next
            else:
                cur.next = l2
                l2 = l2.next
            cur = cur.next
        
        if l1:
            cur.next = l1
        else:
            cur.next = l2
        return dummy.next

    def findMid(self,head):
        if head == None or head.next == None:
            return head

        slow = head
        fast = head

        while fast.next and fast.next.next:
            slow = slow.next
            fast = fast.next.next
        
        return slow

```