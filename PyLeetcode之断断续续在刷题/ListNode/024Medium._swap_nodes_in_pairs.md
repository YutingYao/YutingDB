### 24. Swap Nodes in Pairs

题目： 
<https://leetcode-cn.com/problems/swap-nodes-in-pairs/>


难度 : Medium

一眼就知道这个用递归做，```beats 96.06%```
```python
class Solution(object):
    def swapPairs(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        if not head:
            return None
        if not head.next:
            return head
        tmp = head.next
        head.next = self.swapPairs(head.next.next)
        tmp.next = head
        return tmp
```

或者用```loop```做，每个```node```关系要弄清楚, 又是巧用```dummy```，```dummy```大法对于```nodeList```的题目简直无敌！！！🐂批, 但是只```beats```了```69.40%```


```python
class Solution(object):
    def swapPairs(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        if head == None or head.next == None:
            return head

        dummy = ListNode(-1)
        dummy.next = head
        
        cur = dummy

        while cur.next and cur.next.next:
            next_one, next_two, next_three = cur.next, cur.next.next, cur.next.next.next
            cur.next = next_two
            next_two.next = next_one
            next_one.next = next_three
            cur = next_one
        return dummy.next
```
