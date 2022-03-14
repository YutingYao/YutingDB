class Solution:
    def mergeKLists(self, lists: List[ListNode]) -> ListNode:
        queue = []  
        dummy = ListNode(0)
        
        cur = dummy # cur 就是穿针引线的针
        for i in range(len(lists)):
            if lists[i]: # lists[i]就是head
                heapq.heappush(queue, (lists[i].val, i))     # 先把第一项 push 上去
                lists[i] = lists[i].next 

        while queue: 
            val, idx = heapq.heappop(queue)
            cur.next = ListNode(val)
            cur = cur.next
            if lists[idx]: # 此时 lists[idx] 已经是 head 的下一位
                heapq.heappush(queue, (lists[idx].val, idx)) # 再把每一项 push 上去
                lists[idx] = lists[idx].next 
        return dummy.next