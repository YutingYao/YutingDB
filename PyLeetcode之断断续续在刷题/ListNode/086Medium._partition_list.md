###86. Partition List


题目： 
<https://leetcode-cn.com/problems/partition-list/>


难度 : Medium


思路一：


最简单的思路就是两个dummy head，然后一个指向 小于的node，一个指向大于的node


思路二：

不走寻常路了，使用两个指针，一个指向小于的尾巴，一个一直往后走，指向大于，然后交换node

完成比完美更重要啊，其实可以先试试用简单方法，因为我用我的不走寻常路画了比较久的图，写起来也稍显没那么美观，还在交换node的部分卡了一会



```python
class Solution(object):
    def partition(self, head, x):
        """
        :type head: ListNode
        :type x: int
        :rtype: ListNode
        """
        dummy = ListNode(-1)
        dummy.next = head

        p1 = p2 = dummy

        while p1.next and p1.next.val < x:
            p1 = p1.next

        p2 = p1.next

        while p2:
            while p2.next and p2.next.val >= x:
                p2 = p2.next

            if p2.next == None:
                break
            node = p2.next
            p2.next = node.next
            node.next = p1.next
            p1.next = node
            p1 = p1.next

        return dummy.next
```