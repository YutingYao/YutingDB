### 369.Plus One Linked List	

题目： 
<https://leetcode-cn.com/problems/plus-one-linked-list/>

难度 : Medium



类似题目： plus one，plus one 用递归和循环写了，对于linked list，因为most significant digit在首位，递归写起来不方便，用循环尝试，然后代码并没有实质上的区别。



```python
class Solution(object):
    def plusOne(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        lst = []
        cur = head 

        while cur:
        	lst.append(cur)
        	cur = cur.next

        carry = 1
        for i in range(len(lst)-1,-1,-1):
        	lst[i].val += carry
        	if lst[i].val < 10:
        		carry = 0
        		break
        	else:
        		lst[i].val -= 10

        if carry == 1:
        	node = ListNode(1)
        	node.next = head
        	return node
        else:
        	return head 
```



