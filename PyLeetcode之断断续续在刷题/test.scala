object Solution3 {
    def hasCycle(head: ListNode): Boolean = {
        var fast = head
        var slow = head
        
        
        var result = false
        while (fast != null && fast.next != null && result != true) {
            fast = fast.next.next
            slow = slow.next
        
            if(fast == slow) result = true
        }
        result
    }
}