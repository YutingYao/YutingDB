#  2. Add Two Numbers
**<font color=red>难度: 中等</font>**

## 刷题内容

> 原题连接

* https://leetcode-cn.com/problems/add-two-numbers

> 内容描述

```
You are given two non-empty linked lists representing two non-negative integers. The digits are stored in reverse order and each of their nodes contain a single digit. Add the two numbers and return it as a linked list.

You may assume the two numbers do not contain any leading zero, except the number 0 itself.

Example:

Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
Output: 7 -> 0 -> 8
Explanation: 342 + 465 = 807.
```

## 解题方案

> 思路 1

全部变成数字做加法再换回去呗，这多暴力，爽！

```python
class Solution(object):
    def addTwoNumbers(self, l1, l2):
        """
        :type l1: ListNode
        :type l2: ListNode
        :rtype: ListNode
        """
        if not l1:
            return l2
        if not l2:
            return l1
        
        val1, val2 = [l1.val], [l2.val]
        while l1.next:
            val1.append(l1.next.val)
            l1 = l1.next
        while l2.next:
            val2.append(l2.next.val)
            l2 = l2.next
            
        num1 = ''.join([str(i) for i in val1[::-1]])
        num2 = ''.join([str(i) for i in val2[::-1]])
        
        tmp = str(int(num1) + int(num2))[::-1]
        res = ListNode(tmp[0])
        run_res = res
        for i in range(1, len(tmp)):
            run_res.next = ListNode(tmp[i])
            run_res = run_res.next
        return res
```
> 思路 2

可以使用递归，每次算一位的相加


```python
class Solution(object):
    def addTwoNumbers(self, l1, l2):
        """
        :type l1: ListNode
        :type l2: ListNode
        :rtype: ListNode
        """
        if not l1:
            return l2
        if not l2:
            return l1
        
        if l1.val + l2.val < 10:
            l3 = ListNode(l1.val + l2.val)
            l3.next = self.addTwoNumbers(l1.next, l2.next)

        else:
            l3 = ListNode(l1.val + l2.val - 10)
            tmp = ListNode(1)
            tmp.next = None
            l3.next = self.addTwoNumbers(l1.next, self.addTwoNumbers(l2.next ,tmp))
        return l3
```
