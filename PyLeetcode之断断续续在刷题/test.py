class Solution:
    def reorderList(self, head: ListNode) -> None:
        que = collections.deque()
        cur = head
        while cur.next: # 链表除了首元素全部加入双向队列
            que.append(cur.next)
            cur = cur.next
        cur = head
        # 一后一前加入链表
        while len(que): # 一后一前加入链表
            cur.next = que.pop()
            cur = cur.next
            if len(que):
                cur.next = que.popleft()
                cur = cur.next
        cur.next = None # 尾部置空