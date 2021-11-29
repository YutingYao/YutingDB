# -*- coding:utf-8 -*-
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None

class Solution:
    def FindKthToTail(self, head, k):
        # write code here
        if head == None or k <= 0:
            return None
        
        node = head
        dummy = None
        
        for i in range(k-1):
            if node.next != None:
                node = node.next
            else:
                return None
            
        dummy = head
        while node.next != None:
            node = node.next
            dummy = dummy.next
        return dummy