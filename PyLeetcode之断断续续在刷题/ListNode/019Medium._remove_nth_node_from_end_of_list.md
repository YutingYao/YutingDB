### 19. Remove Nth Node From End of List

题目： 

<https://leetcode-cn.com/problems/remove-nth-node-from-end-of-list/>


难度: Medium


AC击败了95.80%的Python用户，技巧 dummy head 和双指针。

切记最后要返回```dummy.next```而不是```head```，因为有这样一种情况，删掉节点后```linked list```空了，那返回```head```的话结果显然不同。如：
输入链表为```[1]```, ```n = 1```, 应该返回```None```而不是```[1]```

```python
class Solution(object):
    def removeNthFromEnd(self, head, n):
        """
        :type head: ListNode
        :type n: int
        :rtype: ListNode
        """
        dummy = ListNode(-1)
        dummy.next = head
        p, q = dummy, dummy
        
        for i in range(n):
            q = q.next
            
        while q.next:
            p = p.next
            q = q.next
        
        p.next = p.next.next
        return dummy.next
            
```




