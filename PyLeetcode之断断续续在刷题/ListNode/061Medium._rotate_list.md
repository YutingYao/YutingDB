
###61. Rotate List


题目:
<https://leetcode-cn.com/problems/rotate-list/>


难度:

Medium

- k可能比list的size大，需要做一个取余准备
- 计算list size的同时把tail也记录下来，方便之后把tail的next指向原本的head
- 利用之前的到末端的kth node


AC 代码

```python
class Solution(object):
    def rotateRight(self, head, k):
    	if head == None or k == 0 :
    		return head

    	cur = head
    	size = 1
    	while cur.next:
    		size += 1
    		cur = cur.next

    	tail = cur

    	k = k % size

        p = self.findKth(head,k)

        tail.next = head
        head = p.next
        p.next = None
        return head

    def findKth(self,head, k):
        dummy = ListNode(-1)
        dummy.next = head
        p = dummy
        q = dummy
        
        for i in range(k):
            q = q.next
            
        while q.next:
            p = p.next
            q = q.next
        return p
```


