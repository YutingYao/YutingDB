###82. Remove Duplicates from Sorted List II


题目:

<https://leetcode-cn.com/problems/remove-duplicates-from-sorted-list-ii/>


难度:

Medium


木有space 和 time的限制，第一想法，用dictionary存一下每个nodes的个数，这样只要看到它是大于1的，就删删删。

虽然是笨办法。但是也可以AC

```py
class Solution(object):
    def deleteDuplicates(self, head):
        """
        :type head: ListNode
        :rtype: ListNode
        """
        dummy = ListNode(-1)
        dummy.next = head

        cur = dummy.next
        nodeNumber = {}
        while cur:
        	if cur.val in nodeNumber:
        		nodeNumber[cur.val] += 1
        	else:
        		nodeNumber[cur.val] = 1
        	cur = cur.next

        cur = dummy
        while cur.next:
        	if nodeNumber[cur.next.val] > 1:
        		cur.next = cur.next.next
        	else:
        		cur = cur.next
        return dummy.next
```


谷歌一下，更省时间的方法是用一个prev 和 cur 指针，然后用一个bool来记录是否duplicate，这样loop一次即可解决问题。

to be 写出来