###328. Odd Even Linked List

题目:
<https://leetcode-cn.com/problems/odd-even-linked-list/>


难度:

Medium 


想法：因为相对顺序保持不变，所以可以拆list，然后再组合在一起？这样是满足题目要求的，因为linked list不像array，我们操作的时候只是用指向，没有分配新的空间。

```python

class Solution(object):
    def oddEvenList(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        if head == None or head.next == None or head.next.next == None:
        	return head

        oddDummy = ListNode(-1)
        oddDummy.next = head

        evenDummy = ListNode(-1)
        evenDummy.next = head.next

        oddCur = oddDummy.next
        evenCur = evenDummy.next

        cur = head.next.next
        while cur:
        	oddCur.next = cur
        	oddCur = oddCur.next
        	evenCur.next = cur.next
        	evenCur = evenCur.next
        	if cur.next:
        		cur = cur.next.next 
        	else:
        		cur = cur.next
        oddCur.next = evenDummy.next
        # print oddDummy.next.val
        return oddDummy.next

```




看别人的优雅代码

```python
class Solution(object):
    def oddEvenList(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        if head == None:
            return head

        # odd used to keep track of the tail of odd nodes
        odd = oddHead = head
        # record how many swaps happend
        even = evenHead = head.next
        while even and even.next:
            odd.next = even.next
            odd = odd.next
            even.next = odd.next 
            even = even.next
        odd.next = evenHead
        return head 
```

intuitive and concise


```
1 → 2 → 3 → 4 → 5 → NULL

一开始

 1    → 2     → 3 →    4    → 5    → NULL
odd   even     even.next


1 → 3  →    4    → 5    → NULL
    odd     ↑
    2   -   
    
 
1 → 3  →    4    → 5    → NULL
    odd     
    2   -   even
    

再loop一次：

            |   -----------  
    |   ---------  ↓       ↓
1 → 3       4      5    → NULL
                 odd       ↑
    2   -   ↑              even
    

最后一步，再将两个odd的最后一个和evenHead连接起来，完工
```


