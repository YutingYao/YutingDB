class Solution:
    def reverseBetween(self, head: ListNode, left: int, right: int) -> ListNode:
        dummy = ListNode(0, head)
        pre = dummy
        for _ in range(left - 1):
            pre = pre.next
            # 因为需要保留 pre, 所以 left - 1

        first = pre.next
        for _ in range(right - left):
            # 易错点：顺序不能错，中，后，前，忠厚钱
            second = first.next
            first.next = second.next
            second.next = pre.next
            pre.next = second
        
        return dummy.next