# 给定两个非空链表来表示两个非负整数。位数按照逆序方式存储，它们的每个节点只存储单个数字。将两数相加返回一个新的链表。

# 你可以假设除了数字 0 之外，这两个数字都不会以零开头。

# 示例：
# ```
# 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
# 输出：7 -> 0 -> 8
# 原因：342 + 465 = 807
# ```

# medium, 但我认为hard

class ListNode:
    def __init__(self, val = None, next = None):
        self.val = val
        self.next = next
    def __str__(self):
        #测试基本功能，输出字符串
        return str(self.val)

print(ListNode("text"))
#输出text


class Solution(object):
    def addTwoNumbers(self, l1, l2):
        """
        :type l1: ListNode
        :type l2: ListNode
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
 
    
if __name__ == "__main__":

    la = ListNode(2)
    la.next = ListNode(4)
    la.next.next = ListNode(3)

    lb = ListNode(5)
    lb.next = ListNode(6)
    lb.next.next = ListNode(4)

    def printList(node):
        while node:
            print(node)
            node = node.next
            


    s = Solution()
    ss = s.addTwoNumbers(la, lb)

    print(ListNode("la"))
    printList(la)
    print(ListNode("lb"))
    printList(lb)

    print("addTwoNumbers")
    print(ss.val)
    print(ss.next.val)
    print(ss.next.next.val)
    print(ss.next.next.next)