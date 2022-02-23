        dummy = ListNode(-1)        
        p = head        
        while p:            
            tmp = p.next            
            p.next = dummy.next            
            dummy.next = p            
            p = tmp        
        return dummy.next 