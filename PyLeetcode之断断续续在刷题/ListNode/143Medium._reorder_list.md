###143. Reorder List

题目:

<https://leetcode-cn.com/problems/reorder-list/>


难度:

Medium

超时


```python

class Solution(object):
    def reorderList(self, head):
        """
        :type head: ListNode
        :rtype: void Do not return anything, modify head in-place instead.
        """
        head = self.reorder(head)

        
    def reorder(self, head):
        if head == None or head.next == None or head.next.next == None:
            return head
    
        l0 = head
        l1 = head.next
        ln_1 = self.oneNodeTail(head)
        ln =ln_1.next
    
        l0.next = ln
        ln_1.next = None
        ln.next = self.reorder(l1)
        return l0
        

    def oneNodeTail(self, head):
        if head == None or head.next == None or head.next.next == None:
            return head
        cur = head 
        while cur.next:
            if cur.next.next:
                cur = cur.next
            else:
                break
        return cur
                
```


取巧的办法是：

找到中间节点，断开，把后半截linked list reverse，然后合并 √

看了AC指南

```python
class Solution(object):
    def reorderList(self, head):
        """
        :type head: ListNode
        :rtype: void Do not return anything, modify head in-place instead.
        """
        if head == None or head.next == None or head.next.next == None:
            return
        
        slow = head
        fast = head
        prev = None
        
        while fast and fast.next:
            prev = slow
            slow = slow.next
            fast = fast.next.next
            
        prev.next = None


        slow = self.reverseList(slow)
        
        cur = head
        while cur.next:
            tmp = cur.next
            cur.next = slow
            slow = slow.next
            cur.next.next = tmp
            cur = tmp
        cur.next = slow
            
        
    
    def reverseList(self,head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        prev = None 
        cur = head
        while(cur):
            nxt = cur.next
            cur.next = prev
            prev = cur
            cur = nxt
        return prev
        
        
```

