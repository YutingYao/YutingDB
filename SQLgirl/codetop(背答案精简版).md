# 1 day (得分 = 30分) 30

##  1. <a name='ReverseLinkedList'></a> reverseList

```py
输入：head = [1,2,3,4,5]
输出：[5,4,3,2,1]

class Solution:
    def reverseList(self, head: ListNode) -> ListNode:
        cur = None
        while head: 
            headnxt = head.next
            head.next = cur
            cur = head
            head = headnxt
        return cur

时间复杂度：O(n)，其中 n 是链表的节点数量。

空间复杂度：O(n)，
```


##  5. <a name='Kadd-1'></a> reverseKGroup

```py
输入：head = [1,2,3,4,5], k = 3
输出：[3,2,1,4,5]

class Solution:
    def reverseKGroup(self, head: Optional[ListNode], k: int) -> Optional[ListNode]:
        cur = head
        cnt = 0
        while cur and cnt != k: 
            # 必须用 cnt += 1
            # 而不能用 k -= 1
            cur = cur.next
            cnt += 1
        if cnt == k:
            cur = self.reverseKGroup(cur,k)
            # 不能写 while head
            while cnt: # 😐 while 循环
                headnxt = head.next
                head.next = cur
                cur = head
                head = headnxt
                cnt -= 1
            head = cur # 易错点: 这一步不能漏
        return head # head 进来，head 返回

时间复杂度：O(n)，其中 n 是链表的长度。需要遍历链表一次。

空间复杂度：O(1)。
```


##  95. <a name='SwapNodesinPairs'></a> swapPairs

```py
输入：head = [1,2,3,4]
输出：[2,1,4,3]




输入：head = []
输出：[]




输入：head = [1]
输出：[1]



class Solution:
    def swapPairs(self, head: ListNode) -> ListNode:
        if not head or not head.next:
            return head
        headnxt = head.next

        head.next = self.swapPairs(head.next.next)
        headnxt.next = head
        return headnxt


时间复杂度：O(n)，其中 n 是链表的节点数量。

空间复杂度：O(n)，其中 n 是链表的节点数量。空间复杂度主要取决于递归调用的栈空间。

``` 


##  24. <a name='ReverseLinkedListII'></a> reverseBetween


```py
输入：head = [1,2,3,4,5], left = 2, right = 4
输出：[1,4,3,2,5]



class Solution:
    def reverseBetween(self, head: ListNode, left: int, right: int) -> ListNode:
        dummy = ListNode(0, head)
        pre = dummy
        # 这里用到3个指针，pre，first，second
        for _ in range(left - 1):
            pre = pre.next
        # 因为需要保留 pre, 所以 left - 1
        NOTE: first在FOR循环外面，second在FOR循环里面
        first = pre.next
        for _ in range(right - left):
        # 易错点：顺序不能错: 2,1,2,pre
            second = first.next
            first.next = second.next
            second.next = pre.next
            pre.next = second
        
        return dummy.next



时间复杂度：O(n)，其中 n 是链表的长度。需要遍历链表一次。

空间复杂度：O(1)。
```


##  36. <a name='ReorderList'></a> reorderList

```py
输入：head = [1,2,3,4]
输出：[1,4,2,3]



输入：head = [1,2,3,4,5]
输出：[1,5,2,4,3]



# 双向队列
class Solution:
    def reorderList(self, head: ListNode) -> None:
        """
        Do not return anything, modify head in-place instead.
        """
        que = collections.deque()
        cur = head
        #  链表除了首元素全部加入双向队列
        while cur.next: 
            que.append(cur.next)
            cur = cur.next
        cur = head
        # 一后一前加入链表
        while que: 
            cur.next = que.pop()
            cur = cur.next
            if que:
                cur.next = que.popleft()
                cur = cur.next
        cur.next = None # 尾部置空
 
时间复杂度：O(N)，其中 N 是链表中的节点数。

空间复杂度：O(N)，其中 N 是链表中的节点数。主要为线性表的开销。
```


##  44. <a name='RemoveNthNodeFromEndofList'></a> removeNthFromEnd


```py
输入：head = [1,2,3,4,5], n = 2
输出：[1,2,3,5]


输入：head = [1], n = 1
输出：[]


输入：head = [1,2], n = 1
输出：[1]


class Solution:
    def removeNthFromEnd(self, head: ListNode, n: int) -> ListNode:
        dummy = ListNode(0,head)
        slow = dummy # 慢指针需要指向前一个
        fast = head

        for _ in range(n):
            fast = fast.next
        while fast: 
            fast = fast.next
            slow = slow.next

        slow.next = slow.next.next

        return dummy.next

时间复杂度：O(L)，其中 L 是链表的长度。

空间复杂度：O(1)。
```



##  40. <a name='Offer22.k'></a> getKthFromEnd

```py

给定一个链表: 1->2->3->4->5, 和 k = 2.

返回链表 4->5.

class Solution:
    def getKthFromEnd(self, head: ListNode, k: int) -> ListNode:
        # 上一问用 dummy
        slow, fast = head, head
        for i in range(k):
            fast = fast.next
        while fast: # 😐 while 循环
            slow = slow.next
            fast = fast.next
        return slow

时间复杂度：O(N)

空间复杂度：O(1)

```


##  122. <a name='RotateList'></a> rotateRight

```py
输入：head = [1,2,3,4,5], k = 2
输出：[4,5,1,2,3]


输入：head = [0,1,2], k = 4
输出：[2,0,1]



class Solution:
    def rotateRight(self, head: Optional[ListNode], k: int) -> Optional[ListNode]:
        
        if not head or not head.next:
            return head
            
        lenth = 1
        # 第一步：链接成一个环
        cur = head
        while cur.next:
            cur = cur.next
            lenth += 1
        cur.next = head

        # 第二步，cur指向的是head前一个节点
        
        steps = lenth - k % lenth
        for _ in range(steps):
            cur = cur.next

        # 第三步：断开
        res = cur.next
        cur.next = None
        return res

时间复杂度：O(n)，最坏情况下，我们需要遍历该链表两次。

空间复杂度：O(1)，我们只需要常数的空间存储若干变量。
```

##  41. <a name='IIRemoveDuplicatesfromSortedList'></a> deleteDuplicates

```py
输入：head = [1,2,3,3,4,4,5]
输出：[1,2,5]



class Solution:
    def deleteDuplicates(self, head: ListNode) -> ListNode:
        if not head or not head.next:
            return head
        dummy = ListNode(0, head)
        # 后一问，cur = head
        cur = dummy
        # 目的是删除cur的下一个节点
        while cur.next and cur.next.next:  
            if cur.next.val == cur.next.next.val:
                # 把所有等于 x 的结点全部删除
                x = cur.next.val
                # while cur.next 不要漏
                while cur.next and cur.next.val == x: 
                    cur.next = cur.next.next
            else:
                cur = cur.next
        return dummy.next



时间复杂度：O(n)，其中 n 是链表的长度。

空间复杂度：O(1)。
```

##  78. <a name='Removeduplicatesfromsortedarray'></a> deleteDuplicates

```py
输入：head = [1,1,2,3,3]
输出：[1,2,3]


class Solution:
    def deleteDuplicates(self, head: ListNode) -> ListNode:
        if not head or not head.next:
            return head
        # 前一问 cur = dummy
        cur = head
        while cur.next:  
            if cur.val == cur.next.val:
                cur.next = cur.next.next # 要么删除
            else:
                cur =  cur.next # 要么下一个
        return head
```

##  141. <a name='Removeduplicatesfromsortedarray-1'></a> removeDuplicates

```py
不要使用额外的空间，你必须在 `原地` 修改输入数组 并在使用 O(1) 额外空间的条件下完成。



输入：nums = [1,1,2]
输出：2, nums = [1,2,_]




输入：nums = [0,0,1,1,1,2,2,3,3,4]
输出：5, nums = [0,1,2,3,4]




class Solution:
    def removeDuplicates(self, nums: List[int]) -> int:
        slow = 0 # 注意：count是从0开始的
        for fast in range(len(nums)):
            if nums[fast] != nums[slow]:
                slow += 1
                nums[slow] = nums[fast]
        return slow + 1



时间复杂度：O(n)，其中 n 是数组的长度。快指针和慢指针最多各移动 n 次。

空间复杂度：O(1)。只需要使用常数的额外空间。
```

##  168. <a name='StringCompression'></a> compress


```py
输入：chars = ["a","a","b","b","c","c","c"]
输出：返回 6 ，输入数组的前 6 个字符应该是：["a","2","b","2","c","3"]




输入：chars = ["a"]
输出：返回 1 ，输入数组的前 1 个字符应该是：["a"]




输入：chars = ["a","b","b","b","b","b","b","b","b","b","b","b","b"]
输出：返回 4 ，输入数组的前 4 个字符应该是：["a","b","1","2"]。



你必须设计并实现一个只使用`常量额外空间`的算法来解决此问题。



'''
slow += 1
cnt += 1
'''


class Solution:
    def compress(self, chars: List[str]) -> int:

        n = len(chars)

        slow = 0
        cnt = 1
        for fast in range(n):
            # 在 aa，bb，ccc 的最后一位触发计算
            # 这边不是比较 chars[fast] != chars[slow]
            # fast == n - 1 不要漏
            if fast == n - 1 or chars[fast] != chars[fast+1]:

                chars[slow] = chars[fast] 
                slow += 1

                if cnt > 1: # cnt 重新置为 1 前，需要统计是几位数
                    for digit in str(cnt):
                        chars[slow] = digit
                        slow += 1

                cnt = 1 # cnt 重新置为 1
            else:
                cnt += 1
        return slow 
        # 前一问是 slow + 1


时间复杂度：O(n)，其中 n 为字符串长度，我们只需要遍历该字符串一次。

空间复杂度：O(1)。我们只需要常数的空间保存若干变量。

```


##  152. <a name='-1'></a> removeDuplicates

类似消消看

```py
输入："abbaca"
输出："ca"

class Solution(object):
    def removeDuplicates(self, S):
        stack = []
        for char in S:
            if stack and stack[-1] == char:
                stack.pop()
            else:
                stack.append(char)
        return "".join(stack)

时间复杂度：O(n)，其中 n 是字符串的长度。我们只需要遍历该字符串一次。

空间复杂度：O(n)

```

##  173. <a name='FindAllDuplicatesinanArray'></a> findDuplicates

[小明](https://www.bilibili.com/video/BV1Lh411d7AD?spm_id_from=333.999.0.0)

```py
请你找出所有出现 两次 的整数，并以数组形式返回。

输入：nums = [4,3,2,7,8,2,3,1]
输出：[2,3]

你必须设计并实现一个时间复杂度为 O(n) 且仅使用常量额外空间的算法解决此问题。

一个长度为 n 的整数数组 nums ，其中 nums 的所有整数都在范围 [1, n] 内

class Solution:
    def findDuplicates(self, nums: List[int]) -> List[int]:
        res = []
        for num in nums:
            # 取绝对值
            num = abs(num)
            # 把相应下标减1的值设为负数
            if nums[num-1] > 0:
                nums[num-1] *= -1
            # 值为负的话，说明该值已经出现过，添加到输出列表l中
            else:
                res.append(num)
                
        return res

时间复杂度： O(n) 
常量额外空间

[4, 3, 2, 7, 8, 2, 3, 1]
[4, 3, 2, -7, 8, 2, 3, 1]   
[4, 3, -2, -7, 8, 2, 3, 1]    
[4, -3, -2, -7, 8, 2, 3, 1]
[4, -3, -2, -7, 8, 2, -3, 1]
[4, -3, -2, -7, 8, 2, -3, -1]
[4, [-3], -2, -7, 8, 2, -3, -1] 
[4, [-3], [-2], -7, 8, 2, -3, -1] 
[-4, [-3], [-2], -7, 8, 2, -3, -1] 
```

##  276. <a name='RemoveDuplicateLetters'></a> removeDuplicateLetters

```py
去除字符串中重复的字母

使得每个字母只出现一次

返回结果的字典序最小（要求不能打乱其他字符的相对位置）。


输入：s = "bcabc"
输出："abc"
a  小于 stack[-1]，并且 stack[-1] c 在s[i+1:]中，弹出 c
a  小于 stack[-1]，并且 stack[-1] b 在s[i+1:]中，弹出 b




输入：s = "cbacdcbc"
输出："acdb"

b  小于 stack[-1]，并且 stack[-1] c 在s[i+1:]中，弹出 c
a  小于 stack[-1]，并且 stack[-1] b 在s[i+1:]中，弹出 b
c  in stack
c  in stack




class Solution:
    def removeDuplicateLetters(self, s: str) -> str:
        stack = []
        n = len(s)
        for i in range(n):
            if s[i] not in stack:
                while stack and stack[-1] > s[i] and stack[-1] in s[i + 1: ]: # 😐😐😐 while 循环 + pop + append
                # 如果数比栈顶小，而且栈顶在后面还有的话，
                    stack.pop() # 就弹出栈顶。
                stack.append(s[i])
            
        return "".join(stack)



时间复杂度： O(N)。代码中虽然有双重循环，但是每个字符至多只会入栈、出栈各一次。

空间复杂度： O(∣Σ∣)，其中 Σ 为字符集合，本题中字符均为小写字母，所以 ∣Σ∣= 26。

由于栈中的字符不能重复，因此栈中最多只能有 ∣Σ∣ 个字符，

另外需要维护两个数组，

分别记录每个字符是否出现在栈中以及每个字符的剩余数量。
```

##  137. <a name='FindtheDuplicateNumber'></a> findDuplicate


```py
不修改 数组 nums 且只用常量级 O(1) 的额外空间。

线性级时间复杂度 O(n)

输入：nums = [1,3,4,2,2] 0 -> 1 -> 3 -> (2 -> 4) -> 2 -> 4  循环
输出：2 


输入：nums = [3,1,3,4,2] 0 -> (3 -> 4 -> 2) -> 3 -> 4 -> 2 
输出：3

class Solution:
    def findDuplicate(self, nums: List[int]) -> int:
        # node.next = nums[node]
        # node.next.next = nums[nums[node]]
        slow = nums[0]        
        fast = nums[nums[0]] 
        while slow != fast: # 😐😐 while 循环
            slow = nums[slow]
            fast = nums[nums[fast]] 
        p = 0                    
        q = slow  
        while p != q: # 😐😐 while 循环
            p = nums[p]
            q = nums[q]
        return p           

```

##  25. <a name='LinkedListCycleII'></a>142 Linked List Cycle II

![](https://s3.bmp.ovh/imgs/2022/02/5ca7ad17ae2ceeed.png)

```py
时间复杂度： O(N)，其中 N 为链表中节点的数目。slow 指针走过的距离不会超过链表的总长度；

空间复杂度： O(1)。我们只使用了 slow,fast 三个指针。

class Solution:
    def detectCycle(self, head: ListNode) -> ListNode:
        slow, fast = head, head
        while fast and fast.next: # 😐 while 循环
            slow = slow.next
            fast = fast.next.next
            
            if slow == fast: # 如果相遇
                p = head
                q = slow
                while p != q: # 😐 while 循环
                    p = p.next
                    q = q.next
                return p    # 你也可以 return q
        return None
```

##  11. <a name='LinkedListCycle'></a> hasCycle

```py
class Solution:
    def hasCycle(self, head: ListNode) -> bool:
        fast = slow = head
        
        while fast and fast.next: # 😐 while 循环
            fast = fast.next.next
            slow = slow.next
            if fast == slow:
                return True
        return False


* 时间复杂度:O(n)
* 空间复杂度:O(1)      
```


##  114. <a name='1.'></a> sortOddEvenList

1. 按奇偶位置拆分链表，得 1->3->5->7->NULL 和 8->6->4->2->NULL  328. 奇偶链表
2. 反转偶链表，得 1->3->5->7->NULL 和 2->4->6->8->NULL         206. 反转链表
3. 合并两个有序链表，得 1->2->3->4->5->6->7->8->NULL           21. 合并两个有序链表

https://mp.weixin.qq.com/s/0WVa2wIAeG0nYnVndZiEXQ

```py
输入: 1->8->3->6->5->4->7->2->NULL
输出: 1->2->3->4->5->6->7->8->NULL



class Solution:    
    def sortOddEvenList(self,head):     
        if not head or not head.next:      
            return head 
        # 第一步：分割    
        oddList, evenList = self.partition(head)    
        # 第二步：反转 
        evenList = self.reverse(evenList)        
        # 第三步：合并
        return self.merge(oddList, evenList)    

* 时间复杂度: O(n)
* 空间复杂度: O(1)
    def partition(self, head: ListNode) -> ListNode:        
        headnxt = head.next        
        odd, even = head, headnxt        
        while even and even.next: # 😐😐 while 循环  # 🌵 while fast and fast.next:
            odd.next = even.next            
            odd = odd.next            
            even.next = odd.next            
            even = even.next        
        odd.next = None # 节点需要断开
        return [head, headnxt]    



* 时间复杂度: O(n)
* 空间复杂度: O(1)

    def reverse(self,head):    
        cur = None
        while head: # 😐 while 循环, cur
            headnxt = head.next
            head.next = res
            cur = head
            head = headnxt
        return cur    



* 时间复杂度: O(min(n1,n2))
* 空间复杂度: O(1)


    def merge(self,p,q):        
        dummy = ListNode(0)        
        cur = dummy        
        while p and q:    # 😐 while 循环        
            if p.val <= q.val:               
                cur.next = p                
                p = p.next            
            else:                
                cur.next = q                
                q = q.next            
            cur = cur.next        
        cur.next = p or q        
        return dummy.next
```

##  161. <a name='PartitionList'></a> partition

```py
小于 x 的节点都出现在 大于或等于 x 的节点之前

输入：head = [1,4,3,2,5,2], x = 3
输出：[1,2,2,4,3,5]

输入：head = [2,1], x = 2
输出：[1,2]

快慢指针 slow -> fast -> None
链表中节点的数目在范围 [0, 200] 内

* 时间复杂度: O(n)
* 空间复杂度: O(1)


class Solution:
    def partition(self, head: ListNode, x: int) -> ListNode:
        '''
        这道题只要返回 dummy1.next -> dummy2.next -> None
                      slow        -> fast        -> None
        '''
        dummy1 = ListNode(0)
        dummy2 = ListNode(0)
        slow, fast = dummy1, dummy2 

        while head:    # 😐😐😐 while 循环 # 🌵 用 cur 指针
            if head.val < x:
                slow.next = head # dummy1 指向第一个小于x的node
                slow = slow.next
            else:
                fast.next = head # dummy2 指向第一个大于x的node
                fast = fast.next
            head = head.next

        slow.next = dummy2.next
        fast.next = None
        return dummy1.next
```



##  46. <a name='SortList'></a> sortList

```py
输入：head = [4,2,1,3]
输出：[1,2,3,4]


输入：head = [-1,5,3,4,0]
输出：[-1,0,3,4,5]


输入：head = []
输出：[]

class Solution:
    def sortList(self, head: ListNode) -> ListNode:
        # 第一步：递归条件
        if not head or not head.next:
            return head
            
        # 第二步：左右切分
        mid = self.findmid(head)
        left = head # 指定左右
        right = mid.next # 指定左右
        mid.next = None # 断开链接
        '''
        归并排序，先排序，再归并
        '''
        l = self.sortList(left)
        r = self.sortList(right)
        return self.merge(l, r) 

    def findmid(self,head):
        slow, fast = head, head
        while fast.next and fast.next.next: 
            slow = slow.next
            fast = fast.next.next
        return slow

    def merge(self,l,r):
        dummy = ListNode(0)
        cur = dummy
        while l and r: 
            if l.val <= r.val:
                cur.next = l
                l = l.next 
            else:
                cur.next = r
                r = r.next 
            cur = cur.next 
        cur.next = l or r
        return dummy.next

时间复杂度： O(nlogn)，其中 n 是链表的长度。

空间复杂度： O(logn)，其中 n 是链表的长度。空间复杂度主要取决于递归调用的栈空间。

```

##  14. <a name='IntersectionofTwoLinkedLists'></a> getIntersectionNode


```py
输入：intersectVal = 8, listA = [4,1,8,4,5], listB = [5,6,1,8,4,5], skipA = 2, skipB = 3
输出：Intersected at '8'


class Solution:
    def getIntersectionNode(self, headA: ListNode, headB: ListNode) -> ListNode:
        if not headA or not headB:
            return None
        pa, pb = headA, headB
        while pa != pb: # 😐 while 循环
            pa = pa.next if pa else headB
            pb = pb.next if pb else headA
        return pa

时间复杂度 O(M+N), 空间复杂度 O(1)
```



##  277. <a name='-1'></a> sortedListToBST

当递归的是一个链表`头`时，需要切断

当递归的是一个链表`头尾`时，不需要切断

```py
输入: head = [-10,-3,0,5,9]

输出: [0,-3,9,-10,null,5]


输入: head = []

输出: []


class Solution:
    def sortedListToBST(self, head: ListNode) -> TreeNode:
        def findmid(head: ListNode, tail: ListNode) -> ListNode:
            fast = slow = head
            # 和这种写法很像：while fast and fast.next:
            '''
            while fast.next != tail and fast.next.next != tail: # 😐 while 循环
            也对
            '''

            while fast != tail and fast.next != tail: # 😐 while 循环
                fast = fast.next.next
                slow = slow.next
            return slow
        
        def buildTree(left: ListNode, right: ListNode) -> TreeNode:
            '''
            归并，必须 left < right 
            buildTree(left, mid) 和 buildTree(mid.next, right) 是连续的
            '''
            if left == right:
                return None
            mid = findmid(left, right)
            root = TreeNode(mid.val)
            root.left = buildTree(left, mid) # 从 head 到 mid-1，所以我们在 findMid 方程里面，需要对 List 进行切分
            root.right = buildTree(mid.next, right) # 从 mid+1 到 tail
            return root
        
        return buildTree(head, None)

时间复杂度：O(nlogn)，其中 n 是链表的长度。

设长度为 n 的链表构造二叉搜索树的时间为 T(n)，递推式为 T(n) = 2⋅T(n/2) + O(n)，根据主定理， T(n) = O(nlogn)。

空间复杂度：O(logn)，这里只计算除了返回答案之外的空间。平衡二叉树的高度为 O(logn)，

即为递归过程中栈的最大深度，也就是需要的空间。

```


##  189. <a name='MiddleoftheLinkedList'></a> middleNode

```py
输入：[1,2,3,4,5,6]
输出：此列表中的结点 4 (序列化形式：[4,5,6])



* 时间复杂度: O(n)
* 空间复杂度: O(1)


class Solution:
    def middleNode(self, head: ListNode) -> ListNode:
        slow = fast = head
        while fast and fast.next: # 😐 while 循环
            slow = slow.next
            fast = fast.next.next
        return slow
```

## 归并排序:

```py
class Solution:
    def sortArray(self, nums: List[int]) -> List[int]:
        def merge_sort(nums, l, r):
            if l < r:
                mid = (l + r) // 2
                # 先把子序列排序完成
                merge_sort(nums, l, mid)
                merge_sort(nums, mid + 1, r)
                tmp = []
                i1, i2 = l, mid + 1   # i1, i2 是两个起始点
                while i1 <= mid and i2 <= r: # 😐 while 循环
                    # 如果 前半部部分结束了，或者后半部分没有结束
                    if nums[i2] < nums[i1]: # 因为前面是or，所以这里必须是对i进行约束
                        tmp.append(nums[i2])
                        i2 += 1
                    else:
                        tmp.append(nums[i1])
                        i1 += 1
                tmp += nums[i1: mid + 1] or nums[i2: r + 1] # 注意，这里要+1
                nums[l: r + 1] = tmp


        merge_sort(nums, 0, len(nums) - 1)
        return nums

时间复杂度：O(n log(n))
空间复杂度：O(n)
```


##  219. <a name='8.'></a>补充题8. 计算数组的小和


https://mp.weixin.qq.com/s/rMsbcUf9ZPhvfRoyZGW6HA

```py
在一个数组中，每一个数左边比当前数小的数累加起来，叫做这个数组的小和。求一个数组的小和。

例子：

[1,3,4,2,5]

1左边比1小的数，没有；

3左边比3小的数，1；

4左边比4小的数，1、3；

2左边比2小的数，1；

5左边比5小的数，1、3、4、2；

所以小和为1+1+3+1+1+3+4+2=16

要求时间复杂度O(NlogN)，空间复杂度O(N)
```


```py


# 这里有2个目的：
# 1. 排序
# 2. 求出 [1,3,4] [2,5,6] 之间的smallsum
class Solution:
    '''
    在原地排序，不需要 return
    '''
    def mergesmallSum(nums):
        def merge(nums, l, r):
            if l == r:
                return 0
            if l < r:
                mid = (l + r) // 2
                s1 = merge(nums, l, mid)
                s2 = merge(nums, mid + 1, r)
                tmp = []
                s3 = 0
                i1, i2 = l, mid + 1
                while i1 <= mid and i2 <= r: # 😐 while 循环
                    if nums[i1] <= nums[i2]:
                        s3 += nums[i1] * (r - i2 + 1)   # j 后面的部分比 j 都要大， 所以小和有right-j+1个arr[i]
                        tmp.append(nums[i1])
                        i1 += 1
                    else:
                        tmp.append(nums[i2])   # 把小的值先往res里面填写
                        i2 += 1
                tmp += nums[i1: mid + 1] or nums[i2: r + 1]   # 全都排完之后，左半部分有剩余
                nums[l: r + 1] = tmp   # 修改原 arr 的值
                return s1 + s2 + s3
        return merge(nums, 0, n-1)
    
N = int(input())
nums = list(map(int, input().split()))
print(mergesmallSum(nums, 0, N-1))
```


##  10. <a name='-1'></a> mergeTwoLists

暴力解法：

* 时间复杂度:O(M+N)

* 时间复杂度:O(1)

```py
输入：l1 = [1,2,4], l2 = [1,3,4]
输出：[1,1,2,3,4,4]


输入：l1 = [], l2 = []
输出：[]


输入：l1 = [], l2 = [0]
输出：[0]


class Solution:
    def mergeTwoLists(self, list1: Optional[ListNode], list2: Optional[ListNode]) -> Optional[ListNode]:
        dummy = ListNode(0)
        cur = dummy # dummy是固定节点，cur是移动指针
        while list1 and list2: # 😐 while 循环 # 这里是and 
            if list1.val < list2.val: # 易错点：这里是list.val，而不是list
                cur.next = list1
                list1 = list1.next # 向后进一位
            else:
                cur.next = list2
                list2 = list2.next # 向后进一位
            cur = cur.next # 向后进一位
        cur.next = list1 or list2 # 易错点：这里是cur.next，而不是cur。这里是or
        # 等效于：
        # if list1:
        #     cur.next = list1
        # else:
        #     cur.next = list2
        return dummy.next
```



##  15. <a name='Mergesortedarray'></a> merge

```py
输入：nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
输出：[1,2,2,3,5,6]



解释：需要合并 [1,2,3] 和 [2,5,6] 。
合并结果是 [1,2,2,3,5,6] ，其中斜体加粗标注的为 nums1 中的元素。


class Solution:
    def merge(self, nums1: List[int], m: int, nums2: List[int], n: int) -> None:
        """
        Do not return anything, modify nums1 in-place instead.
        """
        # 三个指针
        cur1 = m - 1
        cur2 = n - 1
        i = m + n -1
        # 从后往前遍历
        while cur1 >= 0 and cur2 >= 0: # 😐 while 循环
            if nums1[cur1] < nums2[cur2]:
                nums1[i] = nums2[cur2]
                cur2 -= 1
            else:
                nums1[i] = nums1[cur1]
                cur1 -= 1
            i -= 1
        # 如果后面的那个n还有多余
        if cur2 >= 0:
            nums1[:cur2+1] = nums2[:cur2+1] # 易错点：不包括右边界

* 时间复杂度: O(n)
* 空间复杂度: O(1)
```



##  26. <a name='MergekSortedLists'></a> mergeKLists


优先队列：


```py
输入：lists = [[1,4,5],[1,3,4],[2,6]]
输出：[1,1,2,3,4,4,5,6]
解释：链表数组如下：
[
  1->4->5,
  1->3->4,
  2->6
]
将它们合并到一个有序链表中得到。
1->1->2->3->4->4->5->6

* 时间复杂度: O(N logk) 一共有N个结点
* 空间复杂度: O(k), 此外，新链表需要  O(N) 的空间
class Solution:
    def mergeKLists(self, lists: List[ListNode]) -> ListNode:
        queue = []  
        dummy = ListNode(0)
        
        for i in range(len(lists)):
            if lists[i]: # lists[i] 就是 head
                heapq.heappush(queue, (lists[i].val, i))     # 先把第一项 push 上去
                lists[i] = lists[i].next 

        cur = dummy # cur 就是穿针引线的针
        while queue: # 😐 while 循环
            val, idx = heapq.heappop(queue)
            cur.next = ListNode(val)
            cur = cur.next
            if lists[idx]: # 此时 lists[idx] 已经是 head 的下一位
                heapq.heappush(queue, (lists[idx].val, idx)) # 再把每一项 push 上去
                lists[idx] = lists[idx].next 
        return dummy.next
```

两两合并：

* 时间复杂度: O(N logk)

* 空间复杂度: O(logk)空间代价的栈空间。

```py
class Solution:
    def merge2Lists(self, list1, list2):
        dummy = ListNode(0)
        
        cur = dummy # dummy是固定节点，cur是移动指针
        while list1 and list2: # 😐 while 循环 # 这里是and
            if list1.val < list2.val: # 易错点：这里是list.val，而不是list
                cur.next = list1
                list1 = list1.next # 向后进一位
            else:
                cur.next = list2
                list2 = list2.next # 向后进一位
            cur = cur.next # 向后进一位
        cur.next = list1 or list2 # 易错点：这里是cur.next，而不是cur。这里是or
        return dummy.next
            # 0,1,2,3,4,5,6  7-1
            # 0, ,2, ,4, ,6  7-2
            # 0, , , ,4, ,   7-3
            # 0, , , , , ,   7-4

    def mergeKLists(self, lists: List[ListNode]) -> ListNode:     
        n = len(lists)
        interval = 1
        while n > interval: # 😐😐😐 while 循环
            for i in range(0, n - interval, 2 * interval):
                lists[i] = self.merge2Lists(lists[i], lists[i + interval]) # 易错点：方括号和小括号不要用错
            interval *= 2
        return lists[0] if n else None
```


##  39. <a name='MergeIntervals'></a> merge

```py
输入：intervals = [[1,3],[2,6],[8,10],[15,18]]
输出：[[1,6],[8,10],[15,18]]


输入：intervals = [[1,4],[4,5]]
输出：[[1,5]]


解释：区间 [1,4] 和 [4,5] 可被视为重叠区间。




时间复杂度： O(nlogn)，其中 n 为区间的数量。

除去排序的开销，我们只需要一次线性扫描，所以主要的时间开销是排序的 O(nlogn)。



空间复杂度： O(logn)，其中 n 为区间的数量。

这里计算的是存储答案之外，使用的额外空间。 O(logn) 即为排序所需要的空间复杂度。



 
class Solution:
    def merge(self, intervals: List[List[int]]) -> List[List[int]]:
        intervals.sort() # 等价于：intervals.sort(key = lambda x: x[0])
        res = []
        for interval in intervals: # res[-1] 和 interval 比较
            if res and res[-1][1] >= interval[0]:
                res[-1][1] = max(res[-1][1], interval[1])
            else:
                res.append(interval[:])
                # 易错点：不是interval[1]，而是max(res[-1][1],interval[1])
                # 比如，[[1,4],[2,3]]
        return res
```



##  4. <a name='Kadd'></a> findKthLargest

```py
输入: [3,2,3,1,2,4,5,5,6] 和 k = 4
输出: 4
[3]
[2, 3]
[2, 3, 3]
[1, 2, 3, 3]
[2, 2, 3, 3]
[2, 3, 3, 4]
[3, 3, 5, 4]
[3, 4, 5, 5]
[4, 5, 5, 6]


输入: [3,2,1,5,6,4] 和 k = 2
[3]
[2, 3]
[2, 3]
[3, 5]
[5, 6]
[5, 6]
```

```py
最小堆：时间复杂度就是nlogk
if len(q) > k: 用于限制 q 的宽度
            q:  q 里面 过滤掉了 太小的数


class Solution:
    def findKthLargest(self, nums: List[int], k: int) -> int:
        q = []
        for num in nums:
            heapq.heappush(q, num) # n * log(k + 1)
            if len(q) > k: heapq.heappop(q)   # n * log(k)
        return heapq.heappop(q)


时间复杂度： O((NlogK)) 
空间复杂度： O(K) 
```



##  115. <a name='Offer40.k'></a> getLeastNumbers

```py
输入：arr = [3,2,1], k = 2
输出：[1,2] 或者 [2,1]
hp：
[-3, -2]
[-2, -1]
用 heapq 过滤掉较大值

class Solution:
    def getLeastNumbers(self, arr: List[int], k: int) -> List[int]:
        q = []
        for num in arr:
            heapq.heappush(q, -num) # n * log(k + 1)
            if len(q) > k: heapq.heappop(q)   # n * log(k)
        return [-x for x in q]

时间复杂度： O((NlogK)) 
空间复杂度： O(K) 
```

##  165. <a name='TopKFrequentElements'></a> topKFrequent

```py
输入: nums = [1,1,1,2,2,3], k = 2
输出: [1,2]

hp 为：
[(3, 1)]
[(2, 2), (3, 1)]
[(2, 2), (3, 1)]


过滤较小值
import heapq
class Solution:
    def topKFrequent(self, nums: List[int], k: int) -> List[int]:
        dic = collections.defaultdict(int)
        for num in nums:
            dic[num] += 1
        hp = [] # 小顶堆
        for key, freq in dic.items():
            heapq.heappush(hp, (freq, key))
            if len(hp) > k: heapq.heappop(hp)
        return [x[1] for x in hp]

时间复杂度： O((NlogK)) 
空间复杂度： O(K) 
```

##  104. <a name='MoveZeros'></a> moveZeroes

```py
输入: nums = [0,1,0,3,12]
输出: [1,3,12,0,0]


输入: nums = [0]
输出: [0]


class Solution:
    def moveZeroes(self, nums: List[int]) -> None:
        slow = 0
        for fast in range(len(nums)):
            if nums[fast] != 0:
                # 把 index 的位置变成不是 0, i 的位置变成是 0
                nums[slow], nums[fast] = nums[fast], nums[slow]
                # slow 的位置不是 0, 都在前面
                slow += 1

时间复杂度： O(N) 
空间复杂度： O(1) 
```

##  111. <a name='Offer21.'></a> exchange

```py
输入：nums = [1,2,3,4]
输出：[1,3,2,4] 
注：[3,1,2,4] 也是正确的答案之一。

调整数组顺序使`奇数`位于`偶数`前面

类似前面的移动0

class Solution:
    def exchange(self, nums: List[int]) -> List[int]:
        slow = 0
        for fast in range(len(nums)):
            if nums[fast] & 1 == 1:
                # 把 [fast上的奇数] 移动到 [slow的位置] 上
                nums[slow], nums[fast] = nums[fast], nums[slow]
                slow += 1
        return nums

时间复杂度： O(N) 
空间复杂度： O(1) 
```


##  130. <a name='SortColors'></a> sortColors

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5l1bfbznzwc0.png)

```py
输入：nums = [2,0,2,1,1,0]
输出：[0,0,1,1,2,2]



输入：nums = [2,0,1]
输出：[0,1,2]


class Solution:
    def sortColors(self, nums: List[int]) -> None:
        fast, slow, right = 0, 0, len(nums) - 1
        while fast <= right: # 😐😐😐😐 while 循环
            # 交换完位置后 idx 依旧在原位
            if nums[fast] == 2 and fast < right:
                nums[fast], nums[right] = nums[right], 2
                right -= 1
            # 交换完位置后 idx 依旧在原位
            elif nums[fast] == 0 and fast > slow:
                nums[fast], nums[slow] = nums[slow], 0
                slow += 1
            else:
            # idx 为 1, 或者 idx 与 [right/left] 相交
                fast += 1


时间复杂度： O(N) 
空间复杂度： O(1) 
```

## 堆排序:

```py


     0
    / \
   1   2
  / \ / \
 3  4 5  6

class Solution:
    def maxheapify(self, heap, root, heap_len):
        p = root
        while p * 2 + 2 <= heap_len: # 😐 while 循环 # 当不是叶子节点 
            l, r = p * 2 + 1, p * 2 + 2 # 代表左右结点
            if r < heap_len and heap[l] < heap[r]:
                bigger = r
            else:
                bigger = l
            # 把最大的元素往上提
            if heap[p] < heap[bigger]:
                heap[p], heap[bigger] = heap[bigger], heap[p]
                p = bigger
            else:
                return
        
    def sortArray(self, nums: List[int]) -> List[int]:
        # 时间复杂度O(N)
        # 从叶子节点开始遍历
        # 如果不是从叶子开始，可能白跑一遍
        '''
        把最大值放在 0 的位置
        '''
        for i in range(len(nums) - 1, -1, -1):
            self.maxheapify(nums, i, len(nums))
            
        # 时间复杂度O(N logN)
        for i in range(len(nums) - 1, -1, -1):
            # 把最大的元素放到末尾
        '''
        把最大值 从 0 的位置，依次移到 i 的位置
        '''
            nums[i], nums[0] = nums[0], nums[i]
            self.maxheapify(nums, 0, i)
        return nums

时间复杂度：O(n logn)
空间复杂度：O(1)
```




##  232. <a name='K-1'></a> kthSmallest

[哈哈哈](https://www.bilibili.com/video/BV1mT4y1w7u2?spm_id_from=333.999.0.0)

[图灵](https://www.bilibili.com/video/BV1Zy4y127qr?spm_id_from=333.999.0.0)

```py
输入：matrix = [[1,5,9],[10,11,13],[12,13,15]], k = 8
输出：13


解释：矩阵中的元素为 [1,5,9,10,11,12,13,13,15], 第 8 小元素是 13
```

```py
时间复杂度：O(klogn)，归并 k 次，每次堆中插入和弹出的操作时间复杂度均为 logn。

空间复杂度：O(n)，堆的大小始终为 n。

class Solution:
    def kthSmallest(self, matrix: List[List[int]], k: int) -> int:
        n = len(matrix)
        pq = [(matrix[i][0], i, 0) for i in range(n)] # 每行的 第一个元素
        heapq.heapify(pq)

        for _ in range(k - 1): # 这里 pop k - 1 次
            num, i, j = heapq.heappop(pq)
            if j != n - 1:
                heapq.heappush(pq, (matrix[i][j + 1], i, j + 1)) # 每行的 下一个元素
        
        return heapq.heappop(pq)[0] # 这里  pop  1 次

```



##  54. <a name='SlidingWindowMaximum'></a> maxSlidingWindow

```py
输入：nums = [1,3,-1,-3,5,3,6,7], k = 3
输出：[3,3,5,5,6,7]

解释：
滑动窗口的位置                最大值
---------------               -----
[1  3  -1] -3  5  3  6  7       3
 1 [3  -1  -3] 5  3  6  7       3
 1  3 [-1  -3  5] 3  6  7       5
 1  3  -1 [-3  5  3] 6  7       5
 1  3  -1  -3 [5  3  6] 7       6
 1  3  -1  -3  5 [3  6  7]      7


[(-3, 1), (-1, 0), (1, 2), (3, 3)]
[(-5, 4), (-3, 1), (1, 2), (3, 3), (-1, 0)]
[(-5, 4), (-3, 1), (-3, 5), (3, 3), (-1, 0), (1, 2)]
[(-6, 6), (-3, 1), (-5, 4), (3, 3), (-1, 0), (1, 2), (-3, 5)]
[(-7, 7), (-6, 6), (-5, 4), (-3, 1), (-1, 0), (1, 2), (-3, 5), (3, 3)]


class Solution:
    def maxSlidingWindow(self, nums: List[int], k: int) -> List[int]:
        # 求最大值，则需要取复数
        hp = [(-nums[i], i) for i in range(k-1)]
        heapq.heapify(hp)
        res = []

        for i in range(k-1, len(nums)):
            heapq.heappush(hp, (- nums[i], i))
            while i - hp[0][1] >= k: # 😐😐😐 while 循环 + pop + append
                heapq.heappop(hp) # 把所有出界的最大值弹出，可能不小心攒了许多个
            res.append(- hp[0][0]) # 最大值永远在 q[0]
        
        return res



时间复杂度： O((n-k)logk)

空间复杂度： O(k)，即为优先队列需要使用的空间

```



## 希尔排序：

```py
输入：nums = [5,2,3,1]
输出：[1,2,3,5]
示例 2：

输入：nums = [5,1,1,2,0,0]
输出：[0,0,1,1,2,5]





设定一个

对第一轮排序后的数列再按增量重新分组

对每一个分组进行排序

以此

结果为我们想要的结果

对分组进行排序用的是插入排序算法.因此,希尔排序是插入排序的一种优化的算法

def shellSort(nums): 
  
    n = len(nums)
    gap = n//2 # 初始增量
  
    while gap > 0: 
  
        for i in range(gap,n):  # 对原始数列进行分组 对每一个分组进行排序
  
            right = nums[i] 
            j = i 
            while  j >= gap and nums[j-gap] > right: # nums[j-gap] 是 left
                nums[j] = nums[j-gap]   # 把 nums[j-gap] 这个 bigger 往后面放
                j -= gap 
            nums[j] = right  # 把 right 值 插入
        gap = int(gap/2) # 设置一个更小的增量, 直到增量为1, 再排序

最坏时间复杂度：O(n2)
空间复杂度：O(1)
```

## 选择排序：

选择排序（Selection sort）是一种简单直观的排序算法。它的工作原理如下。首先在未排序序列中找到最小（大）元素，存放到排序序列的起始位置，然后，再从剩余未排序元素中继续寻找最小（大）元素，然后放到已排序序列的末尾。以此类推，直到所有元素均排序完毕。

```py
for i in range(len(nums)): 
      
    minpos = i 
    for j in range(i + 1, len(nums)): 
        if nums[j] < nums[minpos]: 
            minpos = j 
                
    nums[i], nums[minpos] = nums[minpos], nums[i] 
```

## 冒泡排序：

```py
把最大值移到最后一位上：
def bubble_sort(nums):
    n = len(nums)

    for i in range(n):
        for j in range(1, n - i):
            if nums[j - 1] > nums[j]:
                nums[j - 1], nums[j] = nums[j], nums[j - 1]
    return nums
```

## 快速排序:

```py
class Solution:
    # 这里需要用到 pivot
    def randomized_partition(self, nums, l, r):
        pivot = random.randint(l, r)
        # 先把 nums[pivot] 靠边站
        nums[pivot], nums[r] = nums[r], nums[pivot]
        slow = l
        for fast in range(l, r):
            if nums[fast] < nums[r]: # nums[r] 就是 pivot
                nums[fast], nums[slow] = nums[slow], nums[fast] # nums[i] 存的都是较小的数字
                slow += 1
        nums[slow], nums[r] = nums[r], nums[slow] # pivot 放到中间
        return slow
    # 这里需要用到 mid
    def randomized_quicksort(self, nums, l, r):
        if l < r:
            mid = self.randomized_partition(nums, l, r)
            self.randomized_quicksort(nums, l, mid - 1)
            self.randomized_quicksort(nums, mid + 1, r)

    def sortArray(self, nums: List[int]) -> List[int]:
        self.randomized_quicksort(nums, 0, len(nums) - 1)
        return nums

时间复杂度：O(n log(n))
空间复杂度：O(log n) ~ O(n)
```

## 桶排序：

```py
排序问题各有各的招，我来说一个凑热闹的桶排序。反正所有数字在正负五万之间，你就拿100001个桶，遍历一遍把数字仍对应的桶里边，然后你就排好了。

class Solution:
    def sortArray(self, nums: List[int]) -> List[int]:
        bucket = collections.defaultdict(int)
        for num in nums:
            bucket[num] += 1
        res = []
        for i in range(-50000, 50001):
            res += [i] * bucket[i]
        return res
你一看这方法能行啊，复杂度也低！那为啥不经常用呢？你猜？你想想要有小数可咋整？
```



##  74. <a name='PalindromeLinkedList'></a>234. 【回文🌈】Palindrome Linked List

[小梦想家](https://www.bilibili.com/video/BV1Yb411H7ML?spm_id_from=333.999.0.0)

```py
输入：head = [1,2,2,1]
输出：true


输入：head = [1,2]
输出：false


class Solution:
    def isPalindrome(self, head: ListNode) -> bool:
        vals = []
        cur = head
        while cur: # 😐 while 循环, cur
            vals.append(cur.val)
            cur = cur.next
        return vals == vals[::-1]

时间复杂度：O(n)

空间复杂度：O(n)
```


##  92. <a name='-1'></a>138. 复制带随机指针的链表

```py
输入：head = [[7,null],[13,0],[11,4],[10,2],[1,0]]
输出：[[7,null],[13,0],[11,4],[10,2],[1,0]]




"""
class Node:
    def __init__(self, x: int, next: 'Node' = None, random: 'Node' = None):
        self.val = int(x)
        self.next = next
        self.random = random
"""

hash解法：

class Solution:
    def copyRandomList(self, head: 'Node') -> 'Node':
        if not head: return
        hash = {}

        cur = head
        while cur: # 😐😐😐 while 循环, cur
            hash[cur] = Node(cur.val)
            cur = cur.next
        
        cur = head 
        while cur: # 😐😐😐 while 循环, cur
            hash[cur].next = hash.setdefault(cur.next)
            # hash[cur].next = hash.get(cur.next) 这里也可以用 get
            hash[cur].random = hash.setdefault(cur.random)
            cur = cur.next
            
        return hash[head]



dict.setdefault(key, default = None)  -->  有key获取值，否则设置 default，并返回default
dict.get(key, default = None)  -->  有key获取值，否则返回 default



时间复杂度：O(n)，其中 n 是链表的长度。

对于每个节点，我们至多访问其「后继节点」和「随机指针指向的节点」各一次，均摊每个点至多被访问两次。

空间复杂度：O(n)，其中 n 是链表的长度。为哈希表的空间开销。

```


##  230. <a name='PopulatingNextRightPointersinEa'></a>117 Populating Next Right Pointers in Ea

[小明](https://www.bilibili.com/video/BV1np4y1r7fQ?spm_id_from=333.999.0.0)

看不懂，懵逼了

```py
输入：root = [1,2,3,4,5,null,7]
输出：[1,#,2,3,#,4,5,7,#]




常数空间，从顶到下，逐层连接

"""
# Definition for a Node.
class Node:
    def __init__(self, val: int = 0, left: 'Node' = None, right: 'Node' = None, next: 'Node' = None):
        self.val = val
        self.left = left
        self.right = right
        self.next = next
"""
类似 ListNode
class Solution:
    def connect(self, root: 'Node') -> 'Node':
        first = root # first 表示当前层的最左边节点
        while first: # 😐😐 while 循环 # 每次循环连接当前层的下一层
            """
            注意Note：dummy = nxtcur 必须写在一起
            """
            dummy = nxtcur = Node(0) # head表示下一层的虚拟头部

            cur = first
            while cur: # 😐😐 while 循环, cur #  cur遍历当前层，nxtcur将下一层连接 
                if cur.left :
                    nxtcur.next = cur.left
                    nxtcur = nxtcur.next
                if cur.right :
                    nxtcur.next = cur.right
                    nxtcur = nxtcur.next
                cur = cur.next
            
            first = dummy.next
        return root

时间复杂度：O(N)。我们需要遍历这棵树上所有的点。

空间复杂度：O(1)
```




##  2. <a name='LRULRUCache'></a>146. LRU缓存机制【构造🏰】LRU Cache 

https://leetcode-cn.com/problems/lru-cache/submissions/

[花花酱](https://www.bilibili.com/video/BV19b411c7ue?spm_id_from=333.999.0.0)

[花花酱](https://www.bilibili.com/video/BV1gt411Y7c6?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1vi4y1t7zj?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1ZQ4y1A74H?spm_id_from=333.999.0.0)

```py
class LRUCache:

    def __init__(self, capacity: int):
        self.capacity = capacity
        self.cache = collections.OrderedDict()


    def get(self, key: int) -> int:
        if key in self.cache:
            # 先弹出pop，再赋值，表明是最新的
            value = self.cache.pop(key)
            self.cache[key] = value
            return value
        return -1


    def put(self, key: int, value: int) -> None:
        if key in self.cache:
            self.cache.pop(key)
        if len(self.cache) == self.capacity:
            self.cache.popitem(last = False)
        self.cache[key] = value

时间复杂度：对于 put 和 get 都是 O(1)。

空间复杂度：O(capacity)，因为哈希表和双向链表最多存储 capacity+1 个元素。

```


##  31. <a name='ImplementQueueusingStacks'></a>232-【构造🏰】Implement Queue using Stacks

https://leetcode-cn.com/problems/implement-queue-using-stacks/

[哈哈哈](https://www.bilibili.com/video/BV1p741177pp?spm_id_from=333.999.0.0)

[图灵](https://www.bilibili.com/video/BV1Gf4y147Vj?spm_id_from=333.999.0.0)


```py
class MyQueue:

    def __init__(self):
        self.s1 = []
        self.tmp = []

    def push(self, x):
        # 要把新来的元素压入
        while self.s1: # 😐 while 循环 + append + pop
            self.tmp.append(self.s1.pop())
        self.tmp.append(x) # 目的是把最后进来的元素最下面
        while self.tmp: # 😐 while 循环 + append + pop
            self.s1.append(self.tmp.pop())
时间复杂度：O(n)

对于除了新元素之外的所有元素，它们都会被压入两次，弹出两次。
新元素只被压入一次，弹出一次。
这个过程产生了 4n + 2 次操作，其中 n 是队列的大小。由于 压入 操作和 弹出 操作的时间复杂度为 O(1)， 所以时间复杂度为 O(n)。

空间复杂度：O(n)
需要额外的内存来存储队列中的元素。


    def pop(self):
        # 假装最后一个元素是开头
        return self.s1.pop() if self.s1 else None
时间复杂度：O(1)

    def peek(self):
        # 假装最后一个元素是开头
        return self.s1[-1] if self.s1 else None
时间复杂度：O(1)

    def empty(self):
        return False if self.s1 else True
时间复杂度：O(1)
```

##  101. <a name='Offer09.'></a>剑指 Offer 09. 用两个栈实现队列

```py
class CQueue:

    def __init__(self):
        self.s1 = []
        self.tmp = []

    def appendTail(self, x: int) -> None:
        # 要把新来的元素压入
        while self.s1: # 😐 while 循环 + append + pop
            self.tmp.append(self.s1.pop())
        self.tmp.append(x) # 目的是把最后进来的元素最下面
        while self.tmp: # 😐 while 循环 + append + pop
            self.s1.append(self.tmp.pop())

    def deleteHead(self) -> int:
        # 假装最后一个元素是开头
        return self.s1.pop() if self.s1 else -1


# Your CQueue object will be instantiated and called as such:
# obj = CQueue()
# obj.appendTail(value)
# param_2 = obj.deleteHead()
```

##  128. <a name='ImplementStackusingQueues'></a>225-【构造🏰】Implement Stack using Queues

[哈哈哈](https://www.bilibili.com/video/BV1p741177pK?spm_id_from=333.999.0.0)

[图灵](https://www.bilibili.com/video/BV1XQ4y1h735?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1ep4y1Y77j?spm_id_from=333.999.0.0)

```py
q2当作缓存队列

class MyStack:

    def __init__(self):
        # q1和q2是两个队列
        ## 保证q1当中永远有元素
        ## 保证q2当中永远没有元素
        self.q1 = deque([])
        self.tmp = deque([])

    def push(self, x: int) -> None:
        self.q1.append(x)
        
    def pop(self) -> int:
        # 把 [-1] 用 popleft 搞定 
        '''
        tmp 中有 n-1 个元素
        '''
        while len(self.q1) > 1: # 😐😐😐 while 循环 + append + pop : 保留一个元素，将其pop掉
            self.tmp.append(self.q1.popleft())
        '''
        tmp 中有 11 个元素
        '''
        self.q1, self.tmp = self.tmp, self.q1
        '''
        tmp 中有 0 个元素
        '''
        return self.tmp.popleft()
        
    def top(self) -> int:
        return self.q1[-1]

    def empty(self) -> bool:
        return not self.q1

时间复杂度：pop 操作 O(n)，其余操作都是 O(1)，其中 n 是栈内的元素个数。

空间复杂度：O(n)，其中 n 是栈内的元素个数。需要使用一个队列存储栈内的元素。

```

##  273. <a name='BinarySearchTreeIterator'></a>173 【构造🏰】Binary Search Tree Iterator

[小明](https://www.bilibili.com/video/BV1qK41137h1?spm_id_from=333.999.0.0)

```py
# next() 和 hasNext() 操作均摊时间复杂度为 O(1) ，并使用 O(h) 内存。其中 h 是树的高度。

class BSTIterator(object):
    def __init__(self, root):
        self.stack = []
        self.appendAllLeft(root)
时间复杂度：O(1)      

    def hasNext(self):
        return self.stack != []
时间复杂度：O(1)

    def next(self):
        '''
        先 pop，再 append
        '''
        tmp = self.stack.pop()
        self.appendAllLeft(tmp.right)
        return tmp.val
时间复杂度：最坏情况下需要 O(n)
总共会遍历全部的 n 个节点，
因此总的时间复杂度为 O(n)，
因此单次调用平均下来的均摊复杂度为 O(1)

    def appendAllLeft(self, node):
        while node: # 😐 while 循环
            self.stack.append(node)
            node = node.left


空间复杂度：O(n)，其中 n 是二叉树的节点数量。空间复杂度取决于栈深度，而栈深度在二叉树为一条链的情况下会达到 O(n) 的级别。


```

递归解法不符合题目：不能用递归 应该用迭代


##  63. <a name='MinStack'></a>155-【构造🏰】Min Stack

[哈哈哈](https://www.bilibili.com/video/BV1H74118748?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1YK4y1r77W?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1ja4y1Y7vY?spm_id_from=333.999.0.0)

   
关键在于  def getMin

```py
class MinStack:

    def __init__(self):
        # 另外用一个stack，栈顶表示原栈里所有值的最小值
        self.minStack = []
        self.stack = []

    def push(self, val: int) -> None:
        self.stack.append(val)
        if not self.minStack or self.minStack[-1] >= val:
            self.minStack.append(val) # minStack 只 append 某一状态下的最小值

    def pop(self) -> None:
        if self.stack[-1] == self.minStack[-1]:
            self.minStack.pop()
        return self.stack.pop() # minStack 只 pop 某一状态下的最小值

    def top(self) -> int:
        return self.stack[-1]


    def getMin(self) -> int:
        return self.minStack[-1]     

时间复杂度：均为 O(1)。因为栈的插入、删除与读取操作都是 O(1)，我们定义的每个操作最多调用栈操作两次。

空间复杂度：O(n)，其中 n 为总操作数。最坏情况下，我们会连续插入 n 个元素，此时两个栈占用的空间为 O(n)。

 
```


##  138. <a name='-1'></a>384. 打乱数组

https://leetcode-cn.com/problems/shuffle-an-array/solution/da-luan-shu-zu-by-leetcode-solution-og5u/

```py
官方版本：
# class Solution:
#     def __init__(self, nums: List[int]):
#         self.nums = nums
#         self.original = nums.copy()

#     def reset(self) -> List[int]:
#         self.nums = self.original.copy()
#         return self.nums

#     def shuffle(self) -> List[int]:
#         for i in range(len(self.nums)):
#             j = random.randrange(i, len(self.nums))
#             self.nums[i], self.nums[j] = self.nums[j], self.nums[i]
#         return self.nums

精简版本：
from random import random
class Solution:

    def __init__(self, nums: [int]):
        self.nums = nums

    def reset(self) -> [int]:
        return self.nums

    def shuffle(self) -> [int]:
        return sorted(self.nums, key=lambda k: random())
        shuffle：O(nlogn)


空间复杂度：O(n)。记录初始状态和临时的乱序数组均需要存储 n 个元素。

class Solution:
    def __init__(self, nums: List[int]):
        self.nums = nums
        self.original = nums.copy()

    def reset(self) -> List[int]:
        self.nums = self.original.copy()
        return self.nums

    def shuffle(self) -> List[int]:
        for i in range(len(self.nums)):
            j = random.randrange(i, len(self.nums))
            self.nums[i], self.nums[j] = self.nums[j], self.nums[i]
        return self.nums

时间复杂度：初始化：O(n) reset：O(n) shuffle：O(n)
空间复杂度：O(n)。记录初始状态和临时的乱序数组均需要存储 n 个元素。
```


##  144. <a name='ImplementTriePrefixTree'></a>208. 【构造🏰】Implement Trie (Prefix Tree)

[花花酱](https://www.bilibili.com/video/BV1Ut411a74P?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Zz4y1R7j8?spm_id_from=333.999.0.0)

```py

class Trie:
    def __init__(self):
        self.root = {}

    def insert(self, word: str) -> None:
        r = self.root
        for c in word:
            if c not in r: r[c] = {}
            r = r[c]
        r['#'] = True

    # def insert(self, word: str) -> None:
    #     r = self.root
    #     for c in word:
    #         r = r.setdefault(c, {})
    #     r['#'] = True

    def search(self, word: str) -> bool:
        r = self.root
        for c in word:
            if c not in r: return False
            r = r[c]
        return '#' in r

    # def search(self, word: str) -> bool:
    #     r = self.root
    #     for c in word:
    #         if c not in r: return False
    #         r = r[c]
    #     return r.get("#", False)

    def startsWith(self, prefix: str) -> bool:
        r = self.root
        for c in prefix:
            if c not in r: return False
            r = r[c]
        return True

时间复杂度：初始化为 O(1) ，其余操作为 O(|S|) ，其中 |S| 是每次入插或查询的字符串的长度。
空间复杂度：O(|T|⋅ Σ)，其中 |T| 为所有插入字符串的长度之和，Σ 为字符集的大小，本题 Σ = 26。

```

##  158. <a name='-1'></a>295. 数据流的中位数

```py
from heapq import *
class MedianFinder:
    def __init__(self):
        self.maxhp = []
        self.minhp = []
        heapify(self.maxhp)
        heapify(self.minhp)
        
    def addNum(self, num):
        # 每次都插入到最小
        heappush(self.minhp, num)
        # 然后，将最小堆里面的栈顶元素，取出来，放到最大堆中去，这样就能保证最小堆的堆，都比最大堆的堆顶大
        heappush(self.maxhp, - heappop(self.minhp))
        '''
        minhp 的 长度 >= maxhp 的 长度 
        '''
        if len(self.minhp) < len(self.maxhp): # 如果最大堆太大了
            heappush(self.minhp, - heappop(self.maxhp))

        # self.max_h 和 self.min_h 分别为: 
        # [-1] [2]
        # [-1] [2, 3]

        # 对于如何实现大顶堆?
        # 1. 添加元素进去时，取反
        # 2. 取出元素时，也取反

        # 满足两个特性：
        # 1. `大顶堆`中最大的数值 <= `小顶堆`中的最小数, 也就是小于小顶堆的堆顶
        # 2. 两个堆中元素相差为 0, 或者为 1, 不能 > 1
时间复杂度：O(logn) 其中 n 累计添加的数的数量。

    def findMedian(self):
        max_len = len(self.maxhp)
        min_len = len(self.minhp)
        return self.minhp[0] if max_len != min_len else (- self.maxhp[0] + self.minhp[0]) / 2
时间复杂度：O(1

空间复杂度：O(n)
```



##  209. <a name='DesignCircularQueue'></a>622 Design Circular Queue

[小明](https://www.bilibili.com/video/BV1kV411n7Uk?spm_id_from=333.999.0.0)

`循环队列`是一种`线性数据结构`，

其操作表现基于 `FIFO（先进先出）原则`并且队尾

被连接在队首之后以形成一个`循环`。它也被称为`环形缓冲器`。

在一个`普通队列`里，一旦一个队列满了，我们就不能插入下一个元素，即使在队列前面仍有空间。

但是使用`循环队列`，我们能使用这些空间去存储新的值。

```py
class MyCircularQueue:


    # MyCircularQueue(k): 构造器，设置队列长度为 k 。
    def __init__(self, k: int):
        self.queue = [0] * k
        self.headIndex = 0
        self.count = 0
        self.capacity = k

    # enQueue(value): 向循环队列插入一个元素。如果成功插入则返回真。
    def enQueue(self, value: int) -> bool:
        if self.count == self.capacity: return False
        self.queue[(self.headIndex + self.count) % self.capacity] = value
        self.count += 1
        return True

    # deQueue(): 从循环队列中删除一个元素。如果成功删除则返回真。
    def deQueue(self) -> bool:
        if self.count == 0: return False
        self.headIndex = (self.headIndex + 1) % self.capacity
        self.count -= 1
        return True

    # Front: 从队首获取元素。如果队列为空，返回 -1 。
    def Front(self) -> int:
        if self.count == 0: return -1
        return self.queue[self.headIndex]

    # Rear: 获取队尾元素。如果队列为空，返回 -1 。
    def Rear(self) -> int:
        if self.count == 0: return -1
        return self.queue[(self.headIndex + self.count - 1) % self.capacity]

    # isEmpty(): 检查循环队列是否为空。
    def isEmpty(self) -> bool:
        return self.count == 0

    # isFull(): 检查循环队列是否已满。
    def isFull(self) -> bool:
        return self.count == self.capacity
时间复杂度： O(1)。该数据结构中，所有方法都具有恒定的时间复杂度。

空间复杂度： O(N)，其中 N 是队列的预分配容量。循环队列的整个生命周期中，都持有该预分配的空间。

```

##  3. <a name='LongestSubstringWithoutRepeatingCharacters'></a>3. 无重复字符的最长子串 【滑动窗口🔹】数组中重复的数字 Longest Substring Without Repeating Characters

https://leetcode-cn.com/problems/longest-substring-without-repeating-characters/

[哈哈哈](https://www.bilibili.com/video/BV1h54y1B7No?spm_id_from=333.999.0.0)

[花花酱](https://www.bilibili.com/video/BV1CJ411G7Nn?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1va4y1J7Gx?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1ob411n7mv?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV18K411M7d2?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1DK4y1b7xp?spm_id_from=333.999.0.0)

涉及 sub 的问题，可以使用 “滑动窗口”

特殊情况：

* 时间复杂度: O(n) + hashset判断是否重复O(n)
  
```py
输入: s = "abcabcbb"
输出: 3 
解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。




输入: s = "bbbbb"
输出: 1
解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。




输入: s = "pwwkew"
输出: 3
解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
     请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。






class Solution:
    def lengthOfLongestSubstring(self, s: str) -> int:
        dic = {}
        leftI = 0
        res = 0
        for rightI, char in enumerate(s):
            # 含义为"tmmzuxt", start在m，当有新的t进来时，上一个t在start的前面，所以，此时的start不需要修改
            if char in dic and leftI <= dic[char]:      # char 重复出现，并且 上一个出现 在窗口内部
                leftI = dic[char] + 1                   # 易错点: 这里的dic[char]还是前一个,且 +1
            else:
                res = max(res, rightI - leftI + 1)      # 易错点: +1
            dic[char] = rightI                          # 易错点: dic[char]滞后更新
        return res


时间复杂度：O(N)，其中 N是字符串的长度。左指针和右指针分别会遍历整个字符串一次。

空间复杂度：O(∣Σ∣)，其中 Σ 表示字符集（即字符串中可以出现的字符），
∣Σ∣ 表示字符集的大小。
默认为所有 ASCII 码在 [0, 128)内的字符，即∣Σ∣= 128。
我们需要用到哈希集合来存储出现过的字符，而字符最多有∣Σ∣ 个
```




##  49. <a name='-1'></a>105-从前序与中序遍历序列构

[哈哈哈](https://www.bilibili.com/video/BV1uv411B73D?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1x54y1d7e8?spm_id_from=333.999.0.0)

[图灵](https://www.bilibili.com/video/BV1ry4y1U7ZR?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV14A411q7Nv?spm_id_from=333.999.0.0)

> PYTHON 递归

```py
输入: preorder = [3,9,20,15,7], inorder = [9,3,15,20,7]
输出: [3,9,20,null,null,15,7]




输入: preorder = [-1], inorder = [-1]
输出: [-1]






递归:

时间复杂度：O(n)，其中 n 是树中的节点个数。

空间复杂度：O(n)，除去返回的答案需要的 O(n) 空间之外，
我们还需要使用 O(n) 的空间存储哈希映射，以及 O(h) （其中 h 是树的高度）的空间表示递归时栈空间。
这里 h < n，所以总空间复杂度为 O(n)。


class Solution:
    def buildTree(self, preorder, inorder):
        if inorder:
            root = TreeNode(preorder.pop(0)) # preorder 在这里的作用就是 pop(0)
            i = inorder.index(root.val)
            root.left = self.buildTree(preorder, inorder[: i])
            root.right = self.buildTree(preorder, inorder[i + 1:])
            return root

class Solution:
    def buildTree(self, preorder: List[int], inorder: List[int]) -> TreeNode:
        if inorder:
            root = TreeNode(preorder.pop(0)) # preorder 在这里的作用就是 pop(0)
            i = inorder.index(root.val)
            root.left = self.buildTree(preorder[:i], inorder[: i])
            root.right = self.buildTree(preorder[i:], inorder[i + 1:])
            return root
            
print('preorder:',preorder[:i],preorder[i:])
print('inorder:',inorder[: i],inorder[i + 1:])
preorder: [9] [20, 15, 7]
inorder: [9] [15, 20, 7]
preorder: [] []
inorder: [] []
preorder: [15] [7]
inorder: [15] [7]
preorder: [] []
inorder: [] []
preorder: [] []
inorder: [] []
```

106-从中序与后序遍历序列构造二叉树

[哈哈哈](https://www.bilibili.com/video/BV1r5411W7d2?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1jh411Z7y8?spm_id_from=333.999.0.0)

```py
class Solution:
    def buildTree(self, inorder: List[int], postorder: List[int]) -> TreeNode:
        if inorder:
            root = TreeNode(postorder.pop())
            i = inorder.index(root.val)
            root.left = self.buildTree(inorder[:i], postorder[:i])
            root.right = self.buildTree(inorder[i+1:], postorder[i:])
            return root

print('inorder:',inorder[:i],inorder[i+1:])
print('postorder:',postorder[:i],postorder[i:])
inorder: [9] [15, 20, 7]
postorder: [9] [15, 7, 20]
inorder: [] []
postorder: [] []
inorder: [15] [7]
postorder: [15] [7]
inorder: [] []
postorder: [] []
inorder: [] []
postorder: [] []
```



##  204. <a name='ConvertSortedArraytoBinarySearchTree'></a>108 Convert Sorted Array to Binary Search Tree 

[花花酱](https://www.bilibili.com/video/BV1F7411H7tH?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1JJ411q74U?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Wb411e7FR?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1Wa411c7tS?spm_id_from=333.999.0.0)

> python

```py
输入：nums = [-10,-3,0,5,9]
输出：[0,-3,9,-10,null,5]
解释：[0,-10,5,null,-3,null,9] 也将被视为正确答案：



输入：nums = [1,3]
输出：[3,1]
解释：[1,null,3] 和 [3,1] 都是高度平衡二叉搜索树。



时间复杂度：O(n)，其中 n 是数组的长度。每个数字只访问一次。

空间复杂度：O(logn)，其中 n 是数组的长度。空间复杂度不考虑返回值，

因此空间复杂度主要取决于递归栈的深度，递归栈的深度是 O(logn)。

class Solution:
    def sortedArrayToBST(self, nums: List[int]) -> TreeNode:
        if nums:
            mid = len(nums) // 2
            root = TreeNode(nums[mid])
            root.left = self.sortedArrayToBST(nums[:mid])
            root.right = self.sortedArrayToBST(nums[mid+1:])
            return root
```


##  163. <a name='SumClosest'></a>16. 3Sum Closest

[小梦想家](https://www.bilibili.com/video/BV11441187Rr?spm_id_from=333.999.0.0)

```py
输入：nums = [-1,2,1,-4], target = 1
输出：2
解释：与 target 最接近的和是 2 (-1 + 2 + 1 = 2) 。

输入：nums = [0,0,0], target = 1
输出：0





# 和上一题差不多
class Solution:
    def threeSumClosest(self, nums: List[int], target: int) -> int:
        nums.sort()
        minAim = sum(nums[:3]) - target
        n = len(nums)
        for i in range(n - 2):
            # 三指针：i + left + right
            if i - 1 >= 0 and nums[i] == nums[i-1]: continue # 这里不剪枝也可以
            p = i + 1
            q = n - 1
            while p < q: # 😐 while 循环
                aim = nums[i] + nums[p] + nums[q] - target
                if abs(aim) < abs(minAim): 
                    minAim = aim
                if aim == 0:  return target
                elif aim > 0:  q -= 1
                else:          p += 1
        return minAim + target



时间复杂度：O(NlogN)排序 + O(N^2)，其中 N 是数组 nums 的长度。

使用一重循环 O(N) 枚举 i，双指针 O(N) 枚举 p 和 q，故一共是 O(N^2)。

空间复杂度： O(N)。python中的sort之timsort

```

##  7. <a name=''></a>15. 三数之和

https://leetcode-cn.com/problems/3sum/

[花花酱](https://www.bilibili.com/video/BV1wp4y1W72o?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Tb411578b?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV19K4y1s7co?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV19i4y1s7VZ?spm_id_from=333.999.0.0)

暴力解法：

* 时间复杂度:O(n3)

* 时间复杂度:O(1)

双指针法：

先排序：时间复杂度:O(n log(n)) + O(n2)

```py
输入：nums = [-1,0,1,2,-1,-4]
输出：[[-1,-1,2],[-1,0,1]]




输入：nums = []
输出：[]




输入：nums = [0]
输出：[]






class Solution:
    def threeSum(self, nums: List[int]) -> List[List[int]]:
        n = len(nums)
        # nums.sort() # 另一种写法
        nums = sorted(nums)
        res = []
        for i in range(n-2):
            # 优化部分：
            # if nums[i] > 0: break
            # if nums[i] + nums[i+1] + nums[i+2] > 0: break
            # 这个写法不对：if i+1 < n-2 and nums[i] == nums[i+1]: continue
            # 这样可能直接跳过了[-1,-1,2,3]的前三个
            # 这个写法是正确的↓：
            if i - 1 >= 0 and nums[i] == nums[i-1]: continue
            # if nums[i] + nums[n-2] + nums[n-1] < 0: continue
            # 双指针部分：
            p = i + 1
            q = n - 1 
            while p < q:  # 😐 while 循环
                if nums[i] + nums[p] + nums[q] > 0:
                    q -= 1
                elif nums[i] + nums[p] + nums[q] < 0:
                    p += 1
                else:
                    res.append([nums[i],nums[p],nums[q]])
                    # 去重：
                    while nums[p] == nums[p + 1] and p + 1 < q: p += 1# 😐 while 循环 # 注意边界
                        
                    while nums[q] == nums[q - 1] and p < q - 1: q -= 1# 😐 while 循环 # 注意边界
                        
                    p +=1
                    q -=1
        return res
时间复杂度：O(NlogN)排序 + O(N^2)，其中 N 是数组 nums 的长度。

使用一重循环 O(N) 枚举 i，双指针 O(N) 枚举 p 和 q，故一共是 O(N^2)。

空间复杂度： O(N)。python中的sort之timsort
```


##  217. <a name='-1'></a>18. 四数之和

```py
输入：nums = [1,0,-1,0,-2,2], target = 0
输出：[[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]




输入：nums = [2,2,2,2,2], target = 8
输出：[[2,2,2,2]]





# 双指针法
class Solution:
    def fourSum(self, nums: List[int], target: int) -> List[List[int]]:
        nums.sort()
        n = len(nums)
        res = []
        for i in range(n):
            # 第一次 剪枝
            if i > 0 and nums[i] == nums[i - 1]: continue
            for j in range(i + 1, n):
                # 第二次 剪枝
                if j > i + 1 and nums[j] == nums[j - 1]: continue
                # 双指针
                p = j + 1
                q = n - 1

                while p < q: # 😐 while 循环
                    '''
                    == target 才需要剪枝
                    不等于 target 不需要剪枝
                    '''
                    if nums[i] + nums[j] + nums[p] + nums[q] > target: q -= 1
                    elif nums[i] + nums[j] + nums[p] + nums[q] < target: p += 1
                    else:
                        res.append([nums[i], nums[j], nums[p], nums[q]])
                        # 第3次 剪枝
                        while p + 1 < q and nums[p] == nums[p + 1]: p += 1 # 😐 while 循环
                        # 第4次 剪枝
                        while p + 1 < q and nums[q] == nums[q - 1]: q -= 1 # 😐 while 循环
                        p += 1
                        q -= 1
        return res

时间复杂度：O(NlogN)排序 + O(N^3)，其中 N 是数组 nums 的长度。



空间复杂度： O(N)。python中的sort之timsort
```

##  9. <a name='-1'></a>1. 两数之和


[哈哈哈](https://www.bilibili.com/video/BV1rE411Y7UN?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV19b411v7qp?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Zf4y1G7W4?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1rv411k7VY?spm_id_from=333.999.0.0)

暴力求解：

* 时间复杂度:O(n2)

* 空间复杂度:O(1)

```py
输入：nums = [2,7,11,15], target = 9
输出：[0,1]
解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。




输入：nums = [3,2,4], target = 6
输出：[1,2]




输入：nums = [3,3], target = 6
输出：[0,1]






class Solution:
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        for i in range(len(nums)-1):
            for j in range(i+1,len(nums)):
                if nums[i] + nums [j] == target:
                    return [i,j]
```

查找表法:

* 哈希表(不需要维护表的顺序性)

* 平衡二叉搜素树

* 时间复杂度:O(n)

* 空间复杂度:O(n)

```py
class Solution:
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        dic = {}
        for i, num in enumerate(nums):
            if num in dic:
                return [dic[num], i]
            dic[target - num] = i
```


##  208. <a name='TwoSumII-Inputarrayissorted'></a>167-Two Sum II - Input array is sorted

[哈哈哈](https://www.bilibili.com/video/BV167411h7ou?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Yb411H7id?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1VZ4y1M7eu?spm_id_from=333.999.0.0)

```py
输入：numbers = [2,7,11,15], target = 9
输出：[1,2]
解释：2 与 7 之和等于目标数 9 。因此 index1 = 1, index2 = 2 。返回 [1, 2] 。




输入：numbers = [2,3,4], target = 6
输出：[1,3]
解释：2 与 4 之和等于目标数 6 。因此 index1 = 1, index2 = 3 。返回 [1, 3] 。




输入：numbers = [-1,0], target = -1
输出：[1,2]
解释：-1 与 0 之和等于目标数 -1 。因此 index1 = 1, index2 = 2 。返回 [1, 2] 。






双指针

给你一个下标从 1 开始的整数数组 numbers

* 时间复杂度:O(n)

* 空间复杂度:O(1)

class Solution:
    def twoSum(self, numbers: List[int], target: int) -> List[int]:
        l, r = 0, len(numbers) - 1 
        while l < r: # 😐 while 循环
            if numbers[l] + numbers[r] == tart:
                return [l + 1, r + 1] # 给你一个下标从 1 开始的整数数组 numbers
            elif numbers[l] + numbers[r] < target:
                l += 1
            else:
                r -= 1
        return [-1, -1]



* 时间复杂度:O(n)

* 空间复杂度:O(n)

查表法
class Solution:
    def twoSum(self, numbers: List[int], target: int) -> List[int]:
        visited = dict()
        for i, num in enumerate(numbers):
            if num in visited:
                return [visited[num] + 1, i + 1]
            visited[target - num] = i
```



##  8. <a name='Maximumsubarray'></a>53. 最大子序和53-【贪心🧡】Maximum subarray

https://leetcode-cn.com/problems/maximum-subarray/

[哈哈哈](https://www.bilibili.com/video/BV1QJ411R75H?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Yb411i7dn?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV11A41187AR?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1Ta4y1i7Sh?spm_id_from=333.999.0.0)

贪心

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5qrso4wuc440.png)

```py
* 时间复杂度:O(n)

* 空间复杂度:O(1)




输入：nums = [-2,1,-3,4,-1,2,1,-5,4]
输出：6
解释：连续子数组 [4,-1,2,1] 的和最大，为 6 。




输入：nums = [1]
输出：1




输入：nums = [5,4,-1,7,8]
输出：23







class Solution:
    def maxSubArray(self, nums: List[int]) -> int:
        res = preSum = nums[0]
        for num in nums[1:]:
            preSum = max(preSum + num, num)
            res = max(res, preSum)
        return res
```



##  125. <a name='SubarraySumEqualsKK'></a>560. 【前缀和🎨】Subarray Sum Equals K 和为K的子数组

[花花酱](https://www.bilibili.com/video/BV1XW411d71i?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1d54y127ri?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1vK4y1k7ku?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV13t4y1y7ya?spm_id_from=333.999.0.0)

```py
输入：nums = [1,2,3], k = 3
输出：2



* 时间复杂度:O(n)
* 空间复杂度:O(n)



查表法：
class Solution:
    def subarraySum(self, nums: 'List[int]', target: 'int') -> 'int':
        '''
        presum - target 为之前的 presum
        '''
        presum, res, dic = 0, 0, {}
        dic[0] = 1 # 刚好前 n 个的和为 target
        for num in nums:
            presum += num
            if presum - target in dic:
                res += dic[presum - target]
                # sums - target 就是前缀和
            if presum not in dic:
                dic[presum] = 0
            dic[presum] += 1
        return res
        # 输入：nums = [1,2,3], k = 3
        # 输出：2
        # -1000 <= nums[i] <= 1000 注意: nums 有正负
        # {0:1, 1:1}
        # {0:1, 1:1, 3:1}
        # {0:1, 1:1, 3:1, 6:1}
```


##  183. <a name='ContiguousArray'></a>525. 【前缀和🎨】Contiguous Array

[花花酱](https://www.bilibili.com/video/BV14W411d7SD?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV185411t7tu?spm_id_from=333.999.0.0)

```py
找到含有相同数量的 0 和 1 的最长连续子数组，并返回该子数组的长度。

输入: nums = [0,1,0]
输出: 2


说明: [0, 1] (或 [1, 0]) 是具有相同数量0和1的最长连续子数组。



0 变 -1 是精髓，sum_dct = {0:-1} 是细节。



* 时间复杂度:O(n)
* 空间复杂度:O(n)



class Solution:
    def findMaxLength(self, nums: List[int]) -> int:
        presumDic = {}
        presumDic[0] = -1
        res = 0
        s = 0
        for i in range(len(nums)):
            s += 1 if nums[i] == 1 else -1
            if s in presumDic:
                res = max(res, i - presumDic[s])
            else:
                presumDic[s] = i
        return res

```

##  196. <a name='K-1'></a>862. 和至少为 K 的最短子数组

```py
输入：nums = [2, -1, 2], k = 3
输出：3



找出 nums 中和至少为 k 的 `最短非空子数组` ，

并返回该子数组的`长度`。如果不存在这样的 `子数组` ，返回 -1 。

`子数组` 是数组中 `连续` 的一部分。



class Solution:
    def shortestSubarray(self, nums: List[int], k: int) -> int:
        n = len(nums)
        presums = [0]
        for x in nums:
            presums.append(presums[-1] + x)

        res = n + 1 
        deqI = collections.deque()  
        for i, cursum in enumerate(presums):
            # -105 <= nums[i] <= 105
            # 1 <= k <= 109
            # k为正数，如果算到复数，肯定是不满足的
            while deqI and cursum - presums[deqI[-1]] <= 0: # 😐😐😐 while 循环, 排出所有的局部负值
                deqI.pop()
            # 找到 sum 至少为 k 的 `最短非空子数组`，则尽可能地缩短答案
            while deqI and cursum - presums[deqI[0]] >= k: # 😐😐😐 while 循环
                res = min(res, i - deqI.popleft())

            deqI.append(i)

        return res if res < n + 1 else -1



* 时间复杂度:O(n)
* 空间复杂度:O(n)



输入：    [84,-37,32,40,95] 167
presums:  [84,]


输出：    5
预期结果：3



pop()后 []
popleft()后 []


pre的append [0]
判断： 84  -  0 = 84
pop()后 [0]
popleft()后 [0]


pre的append [0, 84]
判断： 47  -  84 = -37
pop()后 [0]
popleft()后 [0]


pre的append [0, 47]
判断： 79  -  47 = 32
pop()后 [0, 47]
popleft()后 [0, 47]


pre的append [0, 47, 79]
判断： 119  -  79 = 40
pop()后 [0, 47, 79]
popleft()后 [0, 47, 79]


pre的append [0, 47, 79, 119]
判断： 214  -  119 = 95
pop()后 [0, 47, 79, 119]
判断：  >= k
popleft()后 [79, 119]


pre的append [79, 119, 214]
```





##  12. <a name='BinaryTreeLevelOrderTraversal'></a>102-Binary Tree Level Order Traversal

https://leetcode-cn.com/problems/binary-tree-level-order-traversal/

[哈哈哈](https://www.bilibili.com/video/BV1W54y197Lc?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV14T4y1u7Wk?spm_id_from=333.999.0.0)

> python queue

```py

输入：root = [3,9,20,null,null,15,7]
输出：[[3],[9,20],[15,7]]


* 时间复杂度:O(n)
* 空间复杂度:O(n)

class Solution:
    def levelOrder(self, root: TreeNode) -> List[List[int]]:
        
        if not root: return []

        queue = collections.deque([root]) 
        res = []
        
        while queue: # 😐 while 循环
            vals = [] 
            for _ in range(len(queue)): 
                node = queue.popleft() 
                vals.append(node.val) 
                if node.left:  queue.append(node.left) 
                if node.right: queue.append(node.right) 
            res.append(vals) 
        return res
```

levelOrderBottom

```py
* 时间复杂度:O(n)
* 空间复杂度:O(n)
class Solution:
    def levelOrderBottom(self, root: TreeNode) -> List[List[int]]:
        if not root: return []
        queue = collections.deque([root]) 
        res = []
        while queue: # 😐 while 循环
            vals = []
            for _ in range(len(queue)):
                node = queue.popleft() 
                vals.append(node.val)
                if node.left:  queue.append(node.left)
                if node.right: queue.append(node.right)
            res.append(vals)
        return res[::-1] 只需要这里变一下
```

> python 递归


```py
* 时间复杂度:O(n)
* 空间复杂度:O(n)

class Solution:
    def levelOrder(self, root: TreeNode) -> List[List[int]]:
        dic = collections.defaultdict(list)

        def bfs(node, level):
            if node:
                dic[level].append(node.val)
                bfs(node.left,  level + 1)
                bfs(node.right, level + 1)

        bfs(root, 0) 
        return [*dic.values()]
```


##  13. <a name='BestTimetoBuyandSellStock121-'></a>121. Best Time to Buy and Sell Stock  121-买卖股票的最佳时机

https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock/

[花花酱](https://www.bilibili.com/video/BV1oW411C7UB?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1cZ4y1K7HP?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1D7411s7A1?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Qb411e7by?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV16z4y1Z7jD?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1hA411t76C?spm_id_from=333.999.0.0)

```py
输入：[7,1,5,3,6,4]
输出：5


解释：在第 2 天（股票价格 = 1）的时候买入，在第 5 天（股票价格 = 6）的时候卖出，最大利润 = 6-1 = 5 。



* 时间复杂度: O(n)
* 空间复杂度: O(1)



class Solution:
    def maxProfit(self, prices: List[int]) -> int:
        maxprofit = 0
        minprice = 1e9
        for price in prices:
            maxprofit = max(maxprofit,price - minprice)
            minprice = min(minprice,price)
        return maxprofit
```


##  102. <a name='II122-BestTimetoBuyandSellStockII'></a>122-【贪心🧡】买卖股票的最佳时机 II 122-Best Time to Buy and Sell Stock II

[哈哈哈](https://www.bilibili.com/video/BV12K411A7rL?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1d7411x78d?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Qb411e7iq?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Fk4y1R7ve?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV17i4y1L7LG?spm_id_from=333.999.0.0)

```py
在每一天，你可能会决定购买和/或出售股票。
你在任何时候 最多 只能持有 `一股` 股票。你也可以购买它，然后在 `同一天` 出售。



输入: prices = [7,1,5,3,6,4]
输出: 7



解释: 

在第 2 天（股票价格 = 1）的时候买入，在第 3 天（股票价格 = 5）的时候卖出, 
这笔交易所能获得利润 = 5-1 = 4 。

随后，在第 4 天（股票价格 = 3）的时候买入，在第 5 天（股票价格 = 6）的时候卖出, 
这笔交易所能获得利润 = 6-3 = 3 。




贪心算法：一次遍历，只要`今天价格`小于`明天价格`就在今天买入然后明天卖出，时间复杂度 O(n)



* 时间复杂度: O(n)
* 空间复杂度: O(1)



class Solution:
    def maxProfit(self, prices: List[int]) -> int:
        maxprofit = 0
        preprice = 1e9
        for price in prices:
            if price > preprice:
                maxprofit += price - preprice
            preprice = price
        return maxprofit
```


##  146. <a name='III'></a>123-买卖股票的最佳时机 III

[哈哈哈](https://www.bilibili.com/video/BV1Xp4y1k7aD?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1rk4y117z8?spm_id_from=333.999.0.0)

```py
设计一个算法来计算你所能获取的最大利润。你最多可以完成 `两笔` 交易。

输入：prices = [3,3,5,0,0,3,1,4]
输出：6



解释：

在第 4 天（股票价格 = 0）的时候买入，在第 6 天（股票价格 = 3）的时候卖出，
这笔交易所能获得利润 = 3-0 = 3 。

随后，在第 7 天（股票价格 = 1）的时候买入，在第 8 天 （股票价格 = 4）的时候卖出，
这笔交易所能获得利润 = 4-1 = 3 。




* 时间复杂度: O(n)
* 空间复杂度: O(1)




# 我的写法：
class Solution:
    def maxProfit(self, prices: List[int]) -> int:
        n = len(prices)
        profit1 = profit2 = 0
        buy1 = buy2 = 10e9
        for price in prices:
            # 实际上，是从卖出那天开始算，也就是第二天
            buy1 = min(buy1, price)
            profit1 = max(profit1, price - buy1)
            buy2 = min(buy2, price - profit1)  # buy2[i]-profit1[i-1] 相当于一个虚拟的买入价格
            profit2 = max(profit2, price - buy2)
        return profit2
```


##  251. <a name='BestTimetoBuyandSellStockIV'></a>188 【动态🚀规划】Best Time to Buy and Sell Stock IV

[小明](https://www.bilibili.com/video/BV1f54y1k7cX?spm_id_from=333.999.0.0)

你最多可以完成 `k 笔` 交易。

```py
输入：k = 2, prices = [3,2,6,5,0,3]
输出：7



解释：
在第 2 天 (股票价格 = 2) 的时候买入，在第 3 天 (股票价格 = 6) 的时候卖出, 
这笔交易所能获得利润 = 6-2 = 4 。
随后，在第 5 天 (股票价格 = 0) 的时候买入，在第 6 天 (股票价格 = 3) 的时候卖出, 
这笔交易所能获得利润 = 3-0 = 3 。




0 <= k <= 100
# 背一背
class Solution:
    def maxProfit(self, k: int, prices: List[int]) -> int:
        if not prices:
            return 0
        k =  min(k,len(prices))
        buy = [10e9] * (k+1)
        sell = [0] * (k+1)
        for price in prices:
            for time in range(1,k+1): 
                # 对于每一个新来的价格，依 time 比较 and 更新
                buy[time-1] = min(buy[time-1],  price - sell[time-1]) # 相当于一个虚拟的买入价格
                sell[time]  = max(sell[time], price - buy[time-1])
                # print('价格:',price,'次数:',time,'buy:',buy)
                # print('价格:',price,'次数:',time,'sell:',sell)
                
        return sell[k]

时间复杂度： O(n min(n,k))，其中 n 是数组 prices 的大小，即我们使用二重循环进行动态规划需要的时间。
空间复杂度： O(n min(n,k)) 或 O(min(n,k))，取决于我们使用二维数组还是一维数组进行动态规划。
 
if __name__ == "__main__":   
	s = Solution()
	print(s.maxProfit(k = 2, prices = [3,2,6,5,0,3,-1,3]))

价格: 3 次数: 1 buy: [3, 3, '*']
价格: 3 次数: 1 sell: [0, 0, 0]
价格: 3 次数: 2 buy: [3, 3, '*']
价格: 3 次数: 2 sell: [0, 0, 0]
--------------------
价格: 2 次数: 1 buy: [2, 3, '*']
价格: 2 次数: 1 sell: [0, 0, 0]
价格: 2 次数: 2 buy: [2, 2, '*']
价格: 2 次数: 2 sell: [0, 0, 0]
--------------------
价格: 6 次数: 1 buy: [2, 2, '*']
价格: 6 次数: 1 sell: [0, 4, 0]
价格: 6 次数: 2 buy: [2, 2, '*']
价格: 6 次数: 2 sell: [0, 4, 4]
--------------------
价格: 5 次数: 1 buy: [2, 2, '*']
价格: 5 次数: 1 sell: [0, 4, 4]
价格: 5 次数: 2 buy: [2, 1, '*']
价格: 5 次数: 2 sell: [0, 4, 4]
--------------------
价格: 0 次数: 1 buy: [0, 1, '*']
价格: 0 次数: 1 sell: [0, 4, 4]
价格: 0 次数: 2 buy: [0, -4, '*']
价格: 0 次数: 2 sell: [0, 4, 4]
--------------------
价格: 3 次数: 1 buy: [0, -4, '*']
价格: 3 次数: 1 sell: [0, 4, 4]
价格: 3 次数: 2 buy: [0, -4, '*']
价格: 3 次数: 2 sell: [0, 4, 7]
--------------------
价格: -1 次数: 1 buy: [-1, -4, '*']
价格: -1 次数: 1 sell: [0, 4, 7]
价格: -1 次数: 2 buy: [-1, -5, '*']
价格: -1 次数: 2 sell: [0, 4, 7]
--------------------
价格: 3 次数: 1 buy: [-1, -5, '*']
价格: 3 次数: 1 sell: [0, 4, 7]
价格: 3 次数: 2 buy: [-1, -5, '*']
价格: 3 次数: 2 sell: [0, 4, 8]
--------------------
8


```




##  16. <a name='BinaryTreeZigzagLevelOrderTraversal'></a>103. Binary Tree Zigzag Level Order Traversal

[小梦想家](https://www.bilibili.com/video/BV1NE411M7Fm?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV15h411Z7h5?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1GA411W7NY?spm_id_from=333.999.0.0)

> python 队列

```py
输入：root = [3,9,20,null,null,15,7]
输出：[[3],[20,9]反转,[15,7]]


* 时间复杂度: O(n)
* 空间复杂度: O(n)


class Solution:
    def zigzagLevelOrder(self, root: TreeNode) -> List[List[int]]:
        if not root: return []

        queue = collections.deque([root])
        res = []
        indexflag = 1 

        while queue: # 😐 while 循环
            vals = []
            for _ in range(len(queue)):
                node = queue.popleft()
                vals.append(node.val)
                if node.left:  queue.append(node.left)
                if node.right: queue.append(node.right)
            indexflag += 1 
            if indexflag % 2: 
                res.append(vals[::-1])
            else: 
                res.append(vals[:])
        return res

```

递归

```py
* 时间复杂度: O(n)
* 空间复杂度: O(n)


class Solution:
    def zigzagLevelOrder(self, root: TreeNode) -> List[List[int]]:
        res = collections.defaultdict(list)
        def bfs(node, level):
            if node:
                res[level].append(node.val)
                bfs(node.left,  level + 1)
                bfs(node.right, level + 1)

        bfs(root, 0)
        for key in res: 
            if key % 2: 
                res[key] = res[key][::-1]
        return [*res.values()]  
# print(res.values()) 
# dict_values([[3], [20, 9], [15, 7]])
```

##  17. <a name='-1'></a>236-二叉树的最近公共祖先

[哈哈哈](https://www.bilibili.com/video/BV1ov411172r?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV125411p7dr?spm_id_from=333.999.0.0)

```py
# Python 超越99%执行速度的解法：而且也简短
root = [3,5,1,6,2,0,8,null,null,7,4], 

       3
     /   \
    5     1
  /  \   /  \
 6    2  0   8
     / \
    7   4

p = 5, q = 4  输出：5




* 时间复杂度: O(n)
* 空间复杂度: O(n)

class Solution:
    def lowestCommonAncestor(self, root, p, q) -> 'TreeNode':
        if root in (None, p, q):
            return root 

        L = self.lowestCommonAncestor(root.left, p, q) # 递归到 5 的时候，就直接返回了
        R = self.lowestCommonAncestor(root.right, p, q) # 递归到 right 的时候，永远是none

        return R if not L else L if not R else root

时间复杂度：O(N)，其中 N 是二叉树的节点数。

二叉树的所有节点有且只会被访问一次，因此时间复杂度为 O(N)。

空间复杂度：O(N)，其中 N 是二叉树的节点数。

递归调用的栈深度取决于二叉树的高度，二叉树最坏情况下为一条链，此时高度为 N，因此空间复杂度为 O(N)。


```

##  18. <a name='Validparentheses'></a>20-Valid parentheses

[哈哈哈](https://www.bilibili.com/video/BV1DJ41127uA?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1hb411i7ek?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Hr4y1M7Sc?spm_id_from=333.999.0.0)

[洛阳](https://www.bilibili.com/video/BV1sC4y1H7Hs?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1QA411L7y7?spm_id_from=333.999.0.0)

先进后出，所以用栈

* 时间复杂度:O(n)

* 时间复杂度:O(n)

```py
输入：s = "()"
输出：true




输入：s = "()[]{}"
输出：true




输入：s = "(]"
输出：false




输入：s = "([)]"
输出：false




输入：s = "{[]}"
输出：true






# 这道题背一背！
class Solution:
    def isValid(self, s: str) -> bool:
        dic = {'{':'}','[':']','(':')'}
        stack = [] # stack 要提前定义好
        for char in s:
            if char in dic: # 是 “key”
                stack.append(char) # 一个 char 进来，要么被 append
            elif not stack or dic[stack.pop()] != char: 
                # 如果上一步不被append就是不对的
                # 如果这一步不匹配也是不对
                return False
        return not stack # 如果append上了，但没有被完全pop也是不对的
```



##  60. <a name='GenerateParentheses'></a>22. Generate Parentheses

[小梦想家](https://www.bilibili.com/video/BV1hb411i7t7?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1vK4y1b744?spm_id_from=333.999.0.0)

回溯法：

* 时间复杂度:O($\frac{4^n}{\sqrt{n}}$)

* 时间复杂度:O($\frac{4^n}{\sqrt{n}}$)

<img src="https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.ud5vx6kpbvk.png" width="50%">

```py
输入：n = 3
输出：["((()))","(()())","(())()","()(())","()()()"]




输入：n = 1
输出：["()"]






时间复杂度：O(4^n / sqrt{n})，在回溯过程中，每个答案需要 O(n) 的时间复制到答案数组中。

空间复杂度：O(n)，除了答案数组之外，我们所需要的空间取决于递归栈的深度，

每一层递归函数需要 O(1) 的空间，最多递归 2n 层，因此空间复杂度为 O(n)。




class Solution:
    def generateParenthesis(self, n: int) -> List[str]:

        def dfs(left,right,itm):
            if left == 0 and right == 0:
                res.append(itm)
            if left > 0:
                dfs(left - 1, right, itm + '(') #   状态转移
            if right > left:
                dfs(left, right - 1, itm + ')')
        
        res = []
        dfs(n, n, '')
        return res
```



##  67. <a name='LongestValidParentheses'></a>32 Longest Valid Parentheses

[小明](https://www.bilibili.com/video/BV1RZ4y1F7nJ?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1yi4y1G74d?spm_id_from=333.999.0.0)

动态规划：

* 时间复杂度: O(n) 

* 空间复杂度: O(n)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6dkova4yjvk0.png)

```py
# 背一背吧，好难。
# class Solution:
#     def longestValidParentheses(self, s: str) -> int:
#         n = len(s)
#         dp = [0]*n
#         if n == 0: return 0
#         for i in range(n):
#             if s[i] == ')' and s[i - dp[i-1] - 1] == '(' and i - dp[i-1] - 1 >= 0:
#                 dp[i] = 2 + dp[i-1] + dp[i-dp[i-1]-2]
#         return max(dp)
```

栈：



![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1dgqk0ervhb4.png)

```py
输入：s = ")()())"
输出：4


解释：最长有效括号子串是 "()()"



* 时间复杂度: O(n) 
* 空间复杂度: O(n)


class Solution:
    def longestValidParentheses(self, s: str) -> int:
        stack = [-1]
        length = maxlength = 0
        for i, c in enumerate(s):
            if c == '(':
                stack.append(i)
            if c == ')':
                stack.pop()
                if not stack:
                    stack.append(i) # 记录一下stack[-1]为')'，断开区间
                else:
                    length = i - stack[-1] # stack[-1]为')'，断开区间
                    maxlength = max(maxlength, length)
        return maxlength

```

##  160. <a name='ValidParenthesisString'></a>678 Valid Parenthesis String

[小明](https://www.bilibili.com/video/BV1ap4y1X7nu?spm_id_from=333.999.0.0)

```py
输入: "()"
输出: True




输入: "(*)"
输出: True




输入: "(*))"
输出: True



## 方法三：左右对称

* 时间复杂度: O(n) 
* 空间复杂度: O(1)


class Solution:
    def checkValidString(self, s: str) -> bool:
        left = right = 0
        for i, c in enumerate(s):    
            # 从左向右看左括号能否有效
            left  += -1 if c == ')' else 1 
            # 从右向左看右括号能否有效
            right += -1 if s[-i-1] == '(' else 1
            # 注意：在循环过程中，只要出现一次负数，就直接返回 false
            if left < 0 or right < 0: return False
       
        return True

```


##  19. <a name='LongestPalindromicSubstring-'></a>5. 【回文🌈】Longest Palindromic Substring -最长回文🌈子串

[花花酱](https://www.bilibili.com/video/BV18J411j7Pb?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1ra4y1Y7Gx?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Yb411H7P6?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1so4y1o765?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1L54y1D7pa?spm_id_from=333.999.0.0)

暴力解法：

* 时间复杂度:O(n3),在两个for循环里面，还做了一次遍历

* 时间复杂度:O(1)

中心扩散法：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4sfvjkqc4qo0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6ur1lzo89kk0.png)

* 时间复杂度:O(n2)

* 时间复杂度:O(1)

```py
容易错
# class Solution:
#     def longestPalindrome(self, s: str) -> str:
#         lenStr = len(s)

#         if lenStr == 0:
#             return ''

#         if lenStr == 1:
#             return s


#         def palinLen(l,r) -> int:
#             while l >= 0 and r < lenStr and s[l] == s[r]: # 注意：边界
#                 l -= 1
#                 r += 1
#             return r - l - 1 # 注意：是 “-1”

#         start = 0  
#         end = 1 # 注意：在第一次的时候，end = 1
#         maxmaxLen = maxLen = 1

#         for mid in range(lenStr):
#             maxLen = max(palinLen(mid, mid), palinLen(mid, mid + 1))
            
#             if maxLen > maxmaxLen:
#                 maxmaxLen = maxLen
#                 start = mid - (maxLen - 1) // 2 #易错点：-1，最好背一背
#                 end = start + maxLen
#         return s[start:end]
```

动态规划法：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.67y5euem0vo0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.90ngy2t8j3k.png)

* 时间复杂度:O(n2)

* 时间复杂度:O(n2)

```py
输入：s = "babad"
输出："bab"


解释："aba" 同样是符合题意的答案。



class Solution:
    def longestPalindrome(self, s: str) -> str:
        lenStr = len(s)
        maxlen = maxmaxlen = 1
        start = 0

        if lenStr == 0:
            return ''

        if lenStr == 1:
            return s

        dp = [[False for _ in range(lenStr)] for _ in range(lenStr)]
        dp[1][1] = True 
            # dp[1][1]是正确写法，dp[1,1]是错误写法

        for end in range(1, lenStr): # 把三角形画出来，先j，再i，
            for stt in range(end): # 先框定结束j，再框定开始i。
                if s[stt] == s[end]:
                    if end - stt < 3:
                        dp[stt][end] = True
                    else:
                        dp[stt][end] = dp[stt + 1][end - 1]
                if dp[stt][end]:
                    maxlen = end - stt + 1
                    if maxlen > maxmaxlen:
                        maxmaxlen = maxlen
                        start = stt
        return s[start: start + maxmaxlen]
```




##  249. <a name='PalindromicSubstrings'></a>647 【动态🚀规划 + 回文🌈】Palindromic Substrings

[小明](https://www.bilibili.com/video/BV1g54y1h7uv?spm_id_from=333.999.0.0)

```py
输入：s = "abc"
输出：3


解释：3个回文子串: "a", "b", "c"




输入：s = "aaa"
输出：6


解释：6个回文子串: "a", "a", "a", "aa", "aa", "aaa"


# class Solution:
#     def countSubstrings(self, s: str) -> int:
#         n = len(s)
#         dp = [[0] * n for _ in range(n)]
#         res = 0
#         # 这个部分其实可以不写：
#         # for i in range(n):
#         #     dp[i][i]=1
#         for end in range(n):
#             for stt in range(end, -1, -1): # stt 从 end 的位置开始移动
#                 if s[end] == s[stt] and (end - stt <= 1 or dp[end - 1][stt + 1]):
#                     dp[end][stt] = 1
#                     res += 1
#         return res

```

```py

# 直接利用, 依次计数找到的所有的回文🌈子串即可；
# 无需在字符串中插入特殊字符, center中心位置从0到最后一个元素移动,
# 每次移动0.5, 表示移动到当前元素与下一个元素中间作为中心。
输入：s = "aaa"
输出：6


解释：6个回文子串: "a", "a", "a", "aa", "aa", "aaa"



* 时间复杂度: O(n2) 
* 空间复杂度: O(1)



from math import floor,ceil
class Solution:
    def countSubstrings(self, s: str) -> int:
        '''
        中心扩展法: 双😐😐while 循环, center += 0.5, 
        '''
        center = cnt = 0
        # center 用一个 while 循环
        while center < len(s): # 😐😐 while 循环
            low, high = floor(center), ceil(center)
            # low, high 用一个 while 循环
            while low >= 0 and high < len(s) and s[low] == s[high]: # 😐😐 while 循环
                low, high = low - 1, high + 1
                cnt = cnt + 1
            center += 0.5
        return cnt
```




##  134. <a name='ValidPalindrome'></a>125 【回文🌈】Valid Palindrome

[哈哈哈](https://www.bilibili.com/video/BV1d7411n7cF?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Qb411e7ML?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV17h411Z7ey?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1iC4y1a7Hz?spm_id_from=333.999.0.0)

isalnum() 方法检测字符串是否由`字母`和`数字`组成。

isalpha() 方法检测字符串是否只由`字母`组成。

```py
输入: "A man, a plan, a canal: Panama"
输出: true
解释："amanaplanacanalpanama" 是回文串




输入: "race a car"
输出: false
解释："raceacar" 不是回文串



* 时间复杂度: O(n) 
* 空间复杂度: O(1)


class Solution:
    def isPalindrome(self, s: str) -> bool:
        left = 0
        right = len(s) - 1
        while left < right: # 😐 while 循环
            # 易错点：if not s[left].isalnum() 是不对的，因为存在连续多个“非数字的情况”
            # 易错点：while left < right 不能省略
            while left < right and not s[left].isalnum(): # 😐😐 while 循环
                left += 1
            while left < right and not s[right].isalnum(): # 😐😐 while 循环
                right -= 1
            if s[left].lower() == s[right].lower():
                left += 1
                right -= 1
            else:
                return False
        return True
```

python牛逼的一行代码：

```py
class Solution:
    def isPalindrome(self, s: str) -> bool:
        sgood = "".join(ch.lower() for ch in s if ch.isalnum())
        return sgood == sgood[::-1]

class Solution:
    def isPalindrome(self, s):
        s = ''.join(filter(str.isalnum,s)).lower()
        return s==s[::-1]
```



##  143. <a name='Palindrome'></a>9-【回文🌈】Palindrome

[哈哈哈](https://www.bilibili.com/video/BV1hJ411S7kt?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Jb411i7YG?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1Af4y1m7kk?spm_id_from=333.999.0.0)

```py
# class Solution:
#     def isPalindrome(self, x: int) -> bool:
#         return True if str(x) == str(x)[::-1] else False
```

```py
输入：x = 121
输出：true




输入：x = -121
输出：false

解释：从左向右读, 为 -121 。 从右向左读, 为 121- 。因此它不是一个回文数。




输入：x = 10
输出：false

解释：从右向左读, 为 01 。因此它不是一个回文数。






* 时间复杂度: O(logn)，对于每次迭代，我们会将输入除以 10
* 空间复杂度: O(1)

class Solution:
    def isPalindrome(self, x: int) -> bool:
        # -231 <= x <= 231 - 1
        if x < 0: return False

        bkp = x
        res = 0
        while x: # 😐 while 循环
            '''
            余加除
            '''
            tmp = x % 10
            res = res * 10 + tmp
            x //= 10

        return bkp == res
```

翻转一半字符法：

* 时间复杂度:O(log10(n)), 每次迭代都会除以10



##  268. <a name='ValidPalindromeII'></a>680 【回文🌈】Valid Palindrome II

[哈哈哈](https://www.bilibili.com/video/BV167411h7x1?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV17i4y147xn?spm_id_from=333.999.0.0)

```py
输入: s = "abca"
输出: true

解释: 你可以删除c字符。



* 时间复杂度: O(n)
* 空间复杂度: O(1)


class Solution:
    def validPalindrome(self, s: str) -> bool:
        def checkPalindrome(low, high):
            i, j = low, high
            while i < j: # 😐 while 循环
                if s[i] != s[j]: return False # 当 需要 跳过 字符串
                i += 1
                j -= 1 
            return True # 当 需要 跳过 字符串

        low, high = 0, len(s) - 1
        while low < high: # 😐 while 循环
            if s[low] == s[high]: 
                low += 1
                high -= 1
            else:
                '''
                删除c字符
                '''
                
                return checkPalindrome(low + 1, high) or checkPalindrome(low, high - 1)
        return True # 当不需要 跳过 字符串

```





##  55. <a name='LongestCommonSubsequence'></a>1143 【二维动态🚀规划】Longest Common Subsequence

####  55.1. <a name='516'></a>类似题目：516最长回文🌈子序列

[小明](https://www.bilibili.com/video/BV19Z4y1W7Xi?spm_id_from=333.999.0.0)

```py
输入：text1 = "abcde", text2 = "ace" 
输出：3  

解释：最长公共子序列是 "ace" ，它的长度为 3 。

         a   b   c   d   e
         0   0   0   0   0
  a  0   1   1   1   1   1
  c  0   1   1   2   2   2
  e  0   1   1   2   2   3



* 时间复杂度: O(n2)
* 空间复杂度: O(n2)



class Solution:
    def longestCommonSubsequence(self, text1: str, text2: str) -> int:
        dp = [[0] * (len(text2) + 1) for _ in range(len(text1) + 1)]
        for i in range(1, len(text1) + 1): 
            for j in range(1, len(text2) + 1): 
                """
                如果相等的话，同时删t1和t2 + 1 
                """
                if text1[i - 1] == text2[j - 1]: 
                    dp[i][j] = dp[i - 1][j - 1] + 1 
                """
                如果 not 相等的话，继承 max(x,x)， 因为可以不连续
                """
                else: 
                    dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])
        return dp[-1][-1]

```




##  70. <a name='-1'></a>718. 最长重复子数组

```py
输入：nums1 = [1,2,3,2,1], nums2 = [3,2,1,4,7]

输出：3


解释：长度最长的公共子数组是 [3,2,1] 。


      3 2 1 4 7
      0 0 0 0 0 

1 0   0 0 1 0 0
2 0   0 1 0 0 0
3 0   1 0 0 0 0
2 0   0 2 0 0 0
1 0   0 0 3 0 0



* 时间复杂度: O(nm)
* 空间复杂度: O(nm)



class Solution:
    def findLength(self, A: List[int], B: List[int]) -> int:
        dp = [[0] * (len(B)+1) for _ in range(len(A)+1)]
        result = 0
        for i in range(1, len(A)+1):
            for j in range(1, len(B)+1):
                """
                如果相等的话， 同时删t1和t2 + 1
                """
                if A[i-1] == B[j-1]:
                    dp[i][j] = dp[i-1][j-1] + 1
                """
                如果 not 相等的话， 不能继承，因为与连续
                """
                result = max(result, dp[i][j])
        return result



这个方法时间复杂度讲不清楚了
# 这个滑动窗口，用的是 left 和 lenth
# * 时间复杂度: O(min(N,M))
# * 空间复杂度: O(1)
# class Solution:
#     def findLength(self, nums1: List[int], nums2: List[int]) -> int:
#         '''
#         0 <= nums1[i], nums2[i] <= 100
#         所以用chr，把数字转换成字符串
#         '''

#         length = left = 0
#         if nums1 and nums2:
#             # 将数字转换为字符串
#             a, b = ''.join(map(chr, nums1)), ''.join(map(chr, nums2))
#             n = len(nums1)
#             while length + left < n: # 😐😐😐 while 循环
#                 # 这里使用lenth保存结果，用left跳出循环
#                 if a[left : left + length + 1] in b:
#                     length += 1
#                 else:
#                     left += 1
#         return length 
```

##  84. <a name='LongestConsecutiveSequence'></a>128. 【🍒并查集】Longest Consecutive Sequence

[花花酱](https://www.bilibili.com/video/BV14t411Y7cg?spm_id_from=333.999.0.0)

[一俩三四五](https://www.bilibili.com/video/BV1LJ41137r2?from=search&seid=18400815010859255620&spm_id_from=333.337.0.0)

```py
输入：nums = [100,4,200,1,3,2]
输出：4

解释：最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。





输入：nums = [0,3,7,2,5,8,4,6,0,1]
输出：9





时间复杂度：O(n)，其中 n 为数组的长度。

增加了判断跳过的逻辑之后，时间复杂度是多少呢？

外层循环需要 O(n) 的时间复杂度，只有当一个数是连续序列的第一个数的情况下才会进入内层循环，

然后在内层循环中匹配连续序列中的数，因此数组中的每个数只会进入内层循环一次。

根据上述分析可知，总时间复杂度为 O(n)，符合题目要求。
 
空间复杂度：O(n)。哈希表存储数组中所有的数需要 O(n) 的空间。
 
class Solution:
    def longestConsecutive(self, nums: List[int]) -> int:
        res = 0
        numSet = set(nums)

        for num in numSet:
            if num - 1 not in numSet: # 去重，表示和前面的不连续
                cur = num
                curlen = 1

                while cur + 1 in numSet: # 😐😐 while 循环
                    cur += 1
                    curlen += 1

                res = max(res, curlen)

        return res
```



##  47. <a name='EditDistance72-'></a>72. Edit Distance 72-编辑距离

[花花酱](https://www.bilibili.com/video/BV1cb411u7uX?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1wv411P7aQ?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV13Z4y1W7UB?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1ea4y147FK?spm_id_from=333.999.0.0)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5kci5ryyi3k0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.7fq2ehol7rg0.png)

```py
输入：word1 = "intention", word2 = "execution"
输出：5


解释：
intention -> inention (删除 't')
inention  -> enention (将 'i' 替换为 'e')
enention  -> exention (将 'n' 替换为 'x')
exention  -> exection (将 'n' 替换为 'c')
exection  -> execution (插入 'u')



 
         i   n   t   e   n   t   i   o   n
     0   1   2   3   4   5   6   7   8   9
 e   1   1   n   t   3   n   t   i   o   n
 x   2   i   n   t   e   n   t   i   o   n
 e   3   i   n   t   e   n   t   i   o   n
 c   4   i   n   t   e   n   t   i   o   n
 u   5   i   n   t   e   n   t   i   o   n
 t   6   i   n   t   e   n   t   i   o   n
 i   7   i   n   t   e   n   t   i   o   n
 o   8   i   n   t   e   n   t   i   o   n
 n   9   i   n   t   e   n   t   i   o   n



* 时间复杂度: O(nm)
* 空间复杂度: O(nm)


class Solution:
    def minDistance(self, word1: str, word2: str) -> int:
        len1 = len(word1)
        len2 = len(word2)

        DP = [[0 for _ in range(len2 + 1)] for _ in range(len1 + 1)]
        
        for i in range(0, len1 + 1):
            for j in range(0, len2 + 1):
                """
                l1 和 l2 都能删干净
                """
                if i == 0:               # 初始化
                    DP[i][j] = j
                elif j == 0:             # 初始化
                    DP[i][j] = i
                """
                如果相等的话，可以直接从 左上角 “继承”
                """
                elif word1[i - 1] == word2[j - 1]:
                    DP[i][j] = DP[i - 1][j - 1]
                """
                如果 not 相等的话，一步操作min(x,x,x) + 计数
                """
                else:
                    DP[i][j] = min(DP[i - 1][j], DP[i][j - 1], DP[i - 1][j - 1]) + 1
                    
        return DP[-1][-1]
```



##  278. <a name='DistinctSubsequences'></a>115. 【动态🚀规划】Distinct Subsequences

[花花酱](https://www.bilibili.com/video/BV1EW411d7PC?spm_id_from=333.999.0.0)

[图灵](https://www.bilibili.com/video/BV185411G7F6?spm_id_from=333.999.0.0)

```py
字符串的一个 子序列 是指，通过 `删除` 一些（也可以 `不删除`）
字符且不干扰剩余字符相对位置所组成的新字符串。

输入：s = "rabbbit", t = "rabbit"

ra-bbit
rab-bit
rabb-it


输出：3


输入：s = "babgbag", t = "bag"
输出：5
ba-g---
ba----g
b----ag
--b--ag
----bag

              b    a    b    g    b    a    g  【si】
         -    1    1    1    1    1    1    1
     b   0   [1]   1   [2]   2   [3]   3    3
     a   0    0   [1]   1    1    1   [4]   4
     g   0    0    0    0   [1]   1    1   [5]
   【ti】



* 时间复杂度: O(nm)
* 空间复杂度: O(nm)


class Solution:
    def numDistinct(self, s: str, t: str) -> int:
        sN = len(s)
        tN = len(t)
        dp = [[0] * (tN + 1) for _ in range(sN + 1)]

        for si in range(sN + 1):
            for ti in range(tN + 1):
                """
                只有s能删，t不能删
                """
                if ti == 0:  
                    dp[si][ti] = 1
                elif si == 0:  
                    dp[si][ti] = 0
                """
                如果相等的话：只删 s + st 同时删除
                """
                elif s[i - 1] == t[j - 1]:
                    dp[si][ti] = dp[si - 1][ti - 1] + dp[si - 1][ti]
                """
                如果 not 相等的话：只删 s
                """
                else:
                    dp[si][ti] = dp[si - 1][ti]
        return dp[-1][-1]
```

##  150. <a name='-1'></a>10. 正则表达式匹配

https://leetcode-cn.com/problems/regular-expression-matching/solution/

```py
输入：s = "aa", p = "a"
输出：false


解释："a" 无法匹配 "aa" 整个字符串。

        """
        思路：动态规划， 定义二维dp数组，其中dp[i][j]表示s的前i个字符和p的前j个字符是否匹配，
        为了方便初始化，我们将s和p的长度均+1
        考虑到P中可能出现三种字符：普通字母(a-z)、'*'或者是'.', 则其动态转移方程分别是：
        1) 如果p[j]为普通字母，dp[i][j]==dp[i-1][j-1] and s[i]==p[j]
        2) 如果p[j]为'.', dp[i][j]==dp[i-1][j-1]
        3) 
        """



* 时间复杂度: O(nm)
* 空间复杂度: O(nm)



class Solution:
    def isMatch(self, s: str, p: str) -> bool:
        # 为了解决s="a", p="c*a"中*组合在p开头0次匹配的问题，
        # 我们需要额外初始化dp[0][:], 为此，在s前加一特殊字符，以方便操作
        s = " " + s
        p = " " + p
        dp = [[False] * len(p) for _ in range(len(s))]   # [len(s)+1, len(s)+1]
        dp[0][0] = True  # 假定s和p都从空字符开始
        
        for si in range(len(s)):  # s的空字符需要额外初始化
            for pi in range(1, len(p)):
        '''
        如果 p[j] 为 '*', 则情况比较复杂, 分以下两种情况讨论：
           A. 以 s="c", p="ca*" 为例，此时 '*' 匹配0次，dp[si][pi] = dp[si][pi-2]
           B. 以 s="caa", p="ca*", p="c.*" 为例，此时 '*' 匹配多次
        '''
                if p[pi] == '*' and dp[si][pi-2]:   # *可以出现0次或者多次
                    dp[si][pi] = dp[si][pi-2]
                elif p[pi] == '*' and p[pi-1] in ('.', s[si]):
                    dp[si][pi] = dp[si-1][pi]
                elif p[pi] in ('.', s[si]):
                    dp[si][pi] = dp[si-1][pi-1]
        return dp[-1][-1]


```




##  216. <a name='-1'></a>44. 通配符匹配

```py
输入:
s = "aa"
p = "a"
输出: false

解释: "a" 无法匹配 "aa" 整个字符串。




输入:
s = "aa"
p = "*"
输出: true

解释: '*' 可以匹配任意字符串。





输入:
s = "cb"
p = "?a"
输出: false

解释: '?' 可以匹配 'c', 但第二个 'a' 无法匹配 'b'。





输入:
s = "adceb"
p = "*a*b"
输出: true

解释: 第一个 '*' 可以匹配空字符串, 第二个 '*' 可以匹配字符串 "dce".





输入:
s = "acdcb"
p = "a*c?b"

输出: false





给定一个 `字符串 (s)` 和一个 `字符模式 (p)` ，实现一个支持 '?' 和 '*' 的通配符匹配。

'?' 可以匹配任何 `单个字符`。
'*' 可以匹配 `任意字符串`（包括 `空字符串`）。
两个字符串完全匹配才算匹配成功。



说明:

s 可能为空，且只包含从 `a-z` 的小写字母。
p 可能为空，且只包含从 `a-z` 的小写字母，以及字符 ? 和 *。



* 时间复杂度: O(nm)
* 空间复杂度: O(nm)



class Solution:
    def isMatch(self, s: str, p: str) -> bool:
        m, n = len(s), len(p)

        dp = [[False] * (n + 1) for _ in range(m + 1)]

        dp[0][0] = True # dp[0][0]:什么都没有,所以为true
        for pi in range(1, n + 1):
            if p[pi - 1] == '*': 
                dp[0][pi] = True
            else:
                break
        # 也可以这么写
        # for j in range(1, n + 1):
        #     if p[j - 1] == "*":
        #         dp[0][j] = dp[0][j - 1]
        
        for si in range(1, m + 1):
            for pi in range(1, n + 1):
                if p[pi - 1] == '*': # 如果p[j] == "*" && (dp[i-1][j] = true || dp[i][j-1] = true) 有dp[i][j] = true
                    dp[si][pi] = dp[si][pi - 1] | dp[si - 1][pi] 
                    # ​ dp[i-1][j],表示*代表是空字符,例如ab,ab*
                    # ​ dp[i][j-1],表示*代表非空任何字符,例如abcd,ab*
                elif p[pi - 1] in ('?', s[si - 1]): # 如果(s[i] == p[j] || p[j] == "?") && dp[i-1][j-1] ,有dp[i][j] = true
                    dp[si][pi] = dp[si - 1][pi - 1]
                
        return dp[m][n]

```




##  83. <a name='-1'></a>62-不同路径

[哈哈哈](https://www.bilibili.com/video/BV1mC4y1W7Je?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Sg4y1v7PM?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1cp4y167qx?spm_id_from=333.999.0.0)

二维动态规划：

时间复杂度：O(mn)

空间复杂度：O(mn)

```py
输入：m = 3, n = 7
输出：28




输入：m = 3, n = 2
输出：3

解释：
从左上角开始，总共有 3 条路径可以到达右下角。
1. 向右 -> 向下 -> 向下
2. 向下 -> 向下 -> 向右
3. 向下 -> 向右 -> 向下





输入：m = 7, n = 3
输出：28





输入：m = 3, n = 3
输出：6





class Solution:
    def uniquePaths(self, m: int, n: int) -> int:
        # 易错点：dp千万不要写错
        # 其他写法：dp = [[1 for i in range(n)] for j in range(m)]
        # 其他写法：dp = [[1]*n]*m
        dp = [[1] * n] + [[1] + [0] * (n - 1) for _ in range(m - 1)]
        for i in range(1, m): # 这里从 1 开始
            for j in range(1, n): # 这里从 1 开始
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1]
        return dp[-1][-1]
```

一维动态规划：

时间复杂度：O(mn)

空间复杂度：O(n)



##  190. <a name='UniquePathsII'></a>63 Unique Paths II

[小明](https://www.bilibili.com/video/BV1Sv411L7qe?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1Pp4y1v7KR?spm_id_from=333.999.0.0)

```py
输入：obstacleGrid = [[0,0,0],[0,1,0],[0,0,0]]
输出：2


解释：3x3 网格的正中间有一个障碍物。
从左上角到右下角一共有 2 条不同的路径：
1. 向右 -> 向右 -> 向下 -> 向下
2. 向下 -> 向下 -> 向右 -> 向右



* 时间复杂度: O(nm)
* 空间复杂度: O(nm)



class Solution:
    def uniquePathsWithObstacles(self, obstacleGrid: List[List[int]]) -> int:
        m = len(obstacleGrid)
        n = len(obstacleGrid[0])
        # 易错点：注意边界上也可能有obstacle
        # 易错点：dp = [[0]*(n+1)]*(m+1)这些写法是错误的
        dp = [[0] * (n + 1) for _ in range(m + 1)]
        # 易错点：dp[1][1] = 1,这个数字会被重新计算，所以应该写成：
        dp[0][1] = 1
        # 构建了一个大一圈的矩阵，但实际计算的时候，仍然是mn的大小
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                if not obstacleGrid[i - 1][j - 1]:
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1]
        print(dp)
        return dp[-1][-1]
```



##  89. <a name='-1'></a>221-【动态🚀规划】最大正方形

[哈哈哈](https://www.bilibili.com/video/BV1XT4y137Gq?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV16K411575r?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1mA411q7Sw?spm_id_from=333.999.0.0)

```py
输入：matrix = [["1","0","1","0","0"],["1","0","1","1","1"],["1","1","1","1","1"],["1","0","0","1","0"]]
输出：4



# 右下角的坐标为 (x, y)
# 那么 (x - 1, y - 1) 一定需要是一个 square，
# 并且该点的左边全为1，上边也为1，按照这个进行理解变长的增加。



构建dp：
* 时间复杂度: O(nm)
* 空间复杂度: O(nm)



class Solution:
    def maximalSquare(self, matrix):
        m, n = len(matrix), len(matrix[0])
        dp = [[0] * n for _ in range(m)]
        maxEdge = 0
        for i in range(m):
            for j in range(n):
                if matrix[i][j] == '1':
                    # 当 i 和 j 等于0时，i-1 为 -1
                    dp[i][j] = min(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1]) + 1
                    maxEdge = max(dp[i][j], res)
        return maxEdge * maxEdge

# 原地修改：
# class Solution:
#     def maximalSquare(self, matrix):
#         maxEdge = 0
#         for i in range(len(matrix)):
#             for j in range(len(matrix[i])):
#                 if i and j: # 这个写法妙啊，刚好就跳过了 i-1
#                     if matrix[i][j] == "1":
#                         matrix[i][j] = min(int(matrix[i-1][j-1]), int(matrix[i][j-1]), int(matrix[i-1][j])) + 1
#                     else:
#                         matrix[i][j] = 0
#                 maxEdge = max(maxEdge, int(matrix[i][j]))
#         return maxEdge**2       
```



##  127. <a name='2.'></a>补充题2. 圆环回原点问题

```s
圆环上有 10 个点，编号为 0 ~ 9。
从`0点`出发，每次可以`逆时针`和`顺时针`走一步，问走`n步`回到`0点`共有多少种走法。

输入: 2
输出: 2


解释：有 2 种方案。分别是 0->1->0 和 0->9->0
```

```py

* 时间复杂度: O(nm)
* 空间复杂度: O(nm)



# 走 n 步到 0 的方案数 = 走 n-1 步到 1 的方案数 + 走 n-1 步到 9 的方案数。
# 公式之所以取余是因为 j-1 或 j+1 可能会超过圆环 0~9 的范围
class Solution:
    def backToOrigin(self,n):
        # 点的个数为 10
        circle = 10
        # step 在外面，site 在里面
        dp = [[0 for site in range(circle)] for step in range(n + 1)]
        dp[0][0] = 1
        for step in range(1, n + 1): # 走 1 ~ n 步
            for site in range(circle):
                # dp[i][j] 表示从 0 出发，走 step 步到 site 的方案数
                # ps:公式之所以`取余`是因为 site-1 或 site+1 可能会超过圆环 0~9 的范围
                dp[step][site] = dp[step - 1][(site - 1 + circle) % circle] \
                               + dp[step - 1][(site + 1) % circle]
        return dp[n][0]
```





##  20. <a name='SearchinRotatedSortedArray'></a>33. Search in Rotated Sorted Array

[小梦想家](https://www.bilibili.com/video/BV1gJ411V7Sq?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV14t4y127hK?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV16A41147Fp?spm_id_from=333.999.0.0)

```py
输入：nums = [4,5,6,7,0,1,2], target = 0
输出：4




输入：nums = [4,5,6,7,0,1,2], target = 3
输出：-1




输入：nums = [1], target = 0
输出：-1





# 我的模仿！啊😋
时间复杂度： O(logn)，其中 n 为 nums 数组的大小。

整个算法时间复杂度即为二分查找的时间复杂度 O(logn)。

空间复杂度： O(1) 。我们只需要常数级别的空间存放变量。
 


class Solution:
    def search(self, nums: List[int], target: int) -> int:
        # 定义第一个元素和最后一个元素
        l = 0
        r = len(nums) - 1

        while l <= r: # 😐 while 循环
            mid = (l + r) // 2
            '''
            这道题 返回 mid
            '''
            if nums[mid] == target:
                return mid
            '''
            输入：nums = [4,5,6,7,0,1,2], target = 0
                  nums = [4,5,6]
                  nums = [4]
            输出：4
            '''
            # 只存在一个上升序列
            if nums[l] <= nums[mid]:
                if nums[l] <= target < nums[mid]:
                    r = mid - 1
                else: 
                    l = mid + 1
            # 只存在一个上升序列
            else:
                if nums[mid] < target <= nums[r]:
                    l = mid + 1
                else: 
                    r = mid - 1
        
        return -1
```


# 2 day (得分 = 15分) 45

##  21. <a name='-1'></a>200 【🍒并查集】岛屿数量

[哈哈哈](https://www.bilibili.com/video/BV15K411p72j?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1Cg4y1i7dZ?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1KK4y1U7Ds?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1E64y1T7Nk?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1Np4y1977S?spm_id_from=333.999.0.0)

[一俩三四五](https://www.bilibili.com/video/BV114411q7sP?from=search&seid=1135814820928819139&spm_id_from=333.337.0.0)

```py
输入：grid = [
  ["1","1","1","1","0"],
  ["1","1","0","1","0"],
  ["1","1","0","0","0"],
  ["0","0","0","0","0"]
]
输出：1


class Solution:
    def numIslands(self, grid: List[List[str]]) -> int:
        def find(x):
            parent.setdefault(x,x)
            if parent[x] != x:
                parent[x] = find(parent[x])
            return parent[x]

        def union(x,y):
            parent[find(y)] = find(x)
            
        if not grid: return 0
        parent = {}
        row, col = len(grid), len(grid[0])
        # 这里是 union
        for i in range(row):
            for j in range(col):
                if grid[i][j] == "1":
                    for dx, dy in [[-1, 0], [0, -1]]:
                        nx = i + dx
                        ny = j + dy
                        if 0 <= nx < row and 0 <= ny < col and grid[nx][ny] == "1":
                            # 把 array 翻译成 list
                            union(nx * col + ny, i * col + j)
        # 这里是 find
        res = set()
        for i in range(row):
            for j in range(col):
                if grid[i][j] == "1":
                    res.add(find(col * i + j))
        return len(res)



时间复杂度： O(MN×α(MN))，其中 M 和 N 分别为行数和列数。

实现并查集时，单次操作的时间复杂度为 α(MN)，其中 α(x) 为反阿克曼函数，

当自变量 x 的值在人类可观测的范围内（宇宙中粒子的数量）时，函数 α(x) 的值不会超过 5，

因此也可以看成是常数时间复杂度。

空间复杂度： O(MN)，这是并查集需要使用的空间。


```

```py
# dfs
class Solution:
    def numIslands(self, grid: List[List[str]]) -> int:
        # 就像是把岛屿一个个蚕食
        # !!! 这里没有 visited
        def dfs(i, j): 
            for dx, dy in [(1,0), (0,1), (-1,0), (0,-1)]:
                nx, ny = i + dx, j + dy
                if 0 <= nx < row and 0 <= ny < col and grid[nx][ny] == '1':   # 补充边界条件，防止溢出
                    grid[nx][ny] = '0' # dfs置为0
                    dfs(nx, ny)  # 遍历4个领域

        row, col = len(grid), len(grid[0]) # 行列
        res = 0
        for i in range(row): # 行列
            for j in range(col): # 行列
                if grid[i][j] == '1': # 如果grid[i][j]为1，则dfs
                    grid[i][j] = '0' # dfs置为0
                    res += 1
                    dfs(i, j)
        return res
时间复杂度： O(MN)，其中 M 和 N 分别为行数和列数。

空间复杂度： O(MN)，在最坏情况下，整个网格均为陆地，深度优先搜索的深度达到 MN。

```

```py
# 厉害的解法：Sink and count the islands.
# class Solution(object):
#     def numIslands(self, grid):
#         def sink(i, j):
#             if 0 <= i < len(grid) and 0 <= j < len(grid[0]) and grid[i][j] == '1':
#                 grid[i][j] = '0'
#                 map(sink, (i+1, i-1, i, i), (j, j, j+1, j-1))
#                 return 1
#             return 0
#         return sum(sink(i, j) for i in range(len(grid)) for j in range(len(grid[0])))

```

##  93. <a name='-1'></a>695-岛屿的最大面积

[哈哈哈](https://www.bilibili.com/video/BV1s54y1B77k?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1wz4y1R7e6?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1k64y1c798?spm_id_from=333.999.0.0)

```py
输入：grid = [
    [0,0,1,0,0,0,0,1,0,0,0,0,0],
    [0,0,0,0,0,0,0,1,1,1,0,0,0],
    [0,1,1,0,1,0,0,0,0,0,0,0,0],
    [0,1,0,0,1,1,0,0,1,0,1,0,0],
    [0,1,0,0,1,1,0,0,1,1,1,0,0],
    [0,0,0,0,0,0,0,0,0,0,1,0,0],
    [0,0,0,0,0,0,0,1,1,1,0,0,0],
    [0,0,0,0,0,0,0,1,1,0,0,0,0]
    ]
输出：6
解释：答案不应该是 11 ，因为岛屿只能包含水平或垂直这四个方向上的 1 。



输入：grid = [[0,0,0,0,0,0,0,0]]
输出：0



# 堆栈：

# class Solution:
#     def maxAreaOfIsland(self, grid: List[List[int]]) -> int:
#         res = 0
#         for i, lists in enumerate(grid):
#             for j, n in enumerate(lists):
#                 area = 0
#                 stack = [(i, j)]
#                 while stack: # 😐 while 循环
#                     x, y = stack.pop()
#                     # if 范围 + 值
#                     if x < 0 or y < 0 or x == len(grid) or y == len(grid[0]) or grid[x][y] != 1:
#                         continue
#                     area += 1
#                     grid[x][y] = 0
#                     for dx, dy in [[0, 1], [0, -1], [1, 0], [-1, 0]]:
#                         newX, newY = x + dx, y + dy
#                         stack.append((newX, newY))
#                 res = max(res, area)
#         return res

dfs
时间复杂度： O(MN)，其中 M 和 N 分别为行数和列数。
空间复杂度： O(MN)，在最坏情况下，整个网格均为陆地，深度优先搜索的深度达到 MN。


class Solution:
    def maxAreaOfIsland(self, grid: List[List[int]]) -> int:
        
        def dfs(i, j):
            grid[i][j] = 0 # 删除这个grid
            acc = 1
            for dx, dy in [(1,0), (0,1), (-1,0), (0,-1)]:
                nx, ny = i + dx, j + dy
                # if 范围 + 值
                if 0 <= nx < m and 0 <= ny < n and grid[nx][ny] == 1:
                    acc += dfs(nx, ny)
            return acc
        
        res = 0
        m, n = len(grid), len(grid[0])
        for i in range(m):
            for j in range(n):
                if grid[i][j] == 1:
                    res = max(res, dfs(i, j))
        return res




并查集

union面积 是基于 find 和 dpArea
'''
这个方法容易出错
'''
class Solution:
    def maxAreaOfIsland(self, grid: List[List[int]]) -> int:
        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])
            return parent[x]
        # 简洁的写法是：
        # def union(i, j):
        #     parent[find(i)] = find(j)
        def union(x, y):
            r1 = find(x)
            r2 = find(y)
            if r1 != r2:
                parent[r2] = r1
                dpArea[r1] += dpArea[r2] # 合并, 到r1
        
        m, n = len(grid), len(grid[0])
        parent = [i * n + j for i in range(m) for j in range(n)]
        dpArea = [0] * (m * n) # 合并
        for i in range(m):
            for j in range(n):
                if grid[i][j]:
                    cur = i * n + j
                    dpArea[cur] = 1  # 合并
                    if 0 <= i + 1 < m and grid[i + 1][j]:
                        dpArea[cur + n] = 1  # 合并
                        union(cur, cur + n)
                    if 0 <= j + 1 < n and grid[i][j + 1]:
                        dpArea[cur + 1] = 1  # 合并
                        union(cur, cur + 1)
        return max(dpArea)
```

##  212. <a name='dfs'></a>547 【🍒并查集 + dfs + 队列】朋友圈

[哈哈哈](https://www.bilibili.com/video/BV1Ta411F7rk?spm_id_from=333.999.0.0)

[郭郭](https://www.bilibili.com/video/BV1eX4y157jr?from=search&seid=13286624680279107242&spm_id_from=333.337.0.0)

🍒并查集 

```py
输入：isConnected = [
    [1,1,0],
    [1,1,0],
    [0,0,1]]

时间复杂度：

最坏情况下 O(n2 logn)，

平均情况下 O(n2 α(n))，

α 为阿克曼函数的反函数， 

α(n) 可以认为是一个很小的常数。

 n 是城市的数量。
 
需要遍历矩阵 isConnected 中的所有元素，时间复杂度是 O(n2)，

如果遇到相连关系，则需要进行 2 次查找和最多 1 次合并，

一共需要进行 2n^2 次查找和最多 n^2 次合并，

因此总时间复杂度是 O(2n2 logn2 )=O(n2 logn)。




空间复杂度： O(n)

其中 n 是城市的数量。需要使用数组 parent 记录每个城市所属的连通分量的祖先。


class Solution:
    def findCircleNum(self, isConnected: List[List[int]]) -> int:
        def find(i: int) -> int:
            if parent[i] != i:
                parent[i] = find(parent[i])
            return parent[i]
        
        def union(i: int, j: int):
            parent[find(i)] = find(j)
        
        proN = len(isConnected)
        parent = list(range(proN))
        
        for i in range(proN):
            '''
            j 大于 i
            '''
            for j in range(i + 1, proN):
                if isConnected[i][j] == 1:
                    union(i, j)
        
        res = sum(parent[i] == i for i in range(proN))
        # 求出 i 就是 parent 的总和
        return res
```

```py
时间复杂度：O(n^2)，其中 n 是城市的数量。需要遍历矩阵 n 中的每个元素。

空间复杂度：O(n)，其中 n 是城市的数量。调用栈的深度.

需要使用数组 visited 记录每个城市是否被访问过，数组长度是 n，递归调用栈的深度不会超过 n。

class Solution:
    def findCircleNum(self, isConnected: List[List[int]]) -> int:
        def dfs(stt: int):
            for end in range(proN):
                if isConnected[stt][end] == 1 and end not in visited:
                    visited.add(end)
                    dfs(end)
        
        proN = len(isConnected)
        visited = set()
        res = 0

        for stt in range(proN):
            if stt not in visited:
                dfs(stt)
                res += 1
        
        return res

# class Solution:
#     def findCircleNum(self, isConnected: List[List[int]]) -> int:
#         proN = len(isConnected)
#         visited = set()
#         res = 0
        
#         for i in range(proN):
#             if i not in visited:
#                 peopleQ = collections.deque([i])
#                 while peopleQ: # 😐 while 循环
#                     peo = peopleQ.popleft()
#                     visited.add(peo)
#                     for fri in range(proN):
#                         if isConnected[peo][fri] == 1 and fri not in visited:
#                             peopleQ.append(fri)
#                 res += 1
        
#         return res
```



##  229. <a name='SurroundedRegions130-'></a>130. 【🍒并查集】Surrounded Regions 130-被围绕的区域

[花花酱](https://www.bilibili.com/video/BV1dE411f7U4?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV18y4y1j7JH?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1pV411k7TH?spm_id_from=333.999.0.0)

并查集


```py
输入：board = [
    ["X","X","X","X"],
    ["X","O","O","X"],
    ["X","X","O","X"],
    ["X","O","X","X"]
    ]
输出：[
    ["X","X","X","X"],
    ["X","X","X","X"],
    ["X","X","X","X"],
    ["X","O","X","X"]
    ]


解释：被围绕的区间不会存在于边界上，

换句话说，任何边界上的 'O' 都不会被填充为 'X'。 

任何不在边界上，或不与边界上的 'O' 相连的 'O' 最终都会被填充为 'X'。

如果两个元素在水平或垂直方向相邻，则称它们是“相连”的。






输入：board = [["X"]]
输出：[["X"]]








class Solution:
    def solve(self, board: List[List[str]]) -> None:
        parent = {}
        def find(x):
            parent.setdefault(x,x)
            if parent[x]!=x:
                parent[x] = find(parent[x])
            return parent[x]
        def union(x,y):
            parent[find(y)] = find(x)
        if not board or not board[0]:
            return
        row, col = len(board), len(board[0])
        dummy = row * col
        for i in range(row):
            for j in range(col):
                if board[i][j] == "O":
                    if i == 0 or i == row - 1 or j == 0 or j == col - 1:
                        union(i * col + j, dummy)
                    else:
                        for x, y in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
                            if board[i + x][j + y] == "O":
                                union(i * col + j, (i + x) * col + (j + y))
                                
        for i in range(row):
            for j in range(col):
                if find(dummy) == find(i * col + j):
                    board[i][j] = "O"
                else:
                    board[i][j] = "X"
```

队列

```py
# class Solution:
#     def solve(self, board: List[List[str]]) -> None:

#         m = len(board)
#         n = len(board[0])
#         que = collections.deque()

#         for i in range(m):
#             for j in range(n):
#                 if i==0 or i==m-1 or j==0 or j==n-1: # 易错点：m 和 n 不要写反了
#                     if board[i][j] == 'O':
#                         que.append((i,j))

#         while que: # 😐 while 循环
#             x,y = que.popleft()
#             board[x][y] = 'A'
#             for dx,dy in [(1,0),(-1,0),(0,1),(0,-1)]:
#                 # 易错点：x+dx 和 x 不要写反了
#                 if 0 <= x+dx < m-1 and 0 <= y+dy < n-1 and board[x+dx][y+dy] == 'O': # 易错点：'O'不要写成0
#                     board[x+dx][y+dy] = 'A'
#                     que.append((x+dx,y+dy))

#         for i in range(m):
#             for j in range(n):
#                 # 易错点：== 和 = 不要写反了
#                 if board[i][j] == 'O':
#                     board[i][j] = 'X'
#                 elif board[i][j] == 'A':
#                     board[i][j] = 'O'

#         return board
```

dfs

```py
时间复杂度： O(MN)，其中 M 和 N 分别为行数和列数。

空间复杂度： O(MN)，在最坏情况下，整个网格均为陆地，深度优先搜索的深度达到 MN。

class Solution:
    def solve(self, board: List[List[str]]) -> None:
        if not board:
            return
        
        n, m = len(board), len(board[0])

        def dfs(x, y):
            if 0 <= x < n and 0 <= y < m and board[x][y] == 'O':
                board[x][y] = "A"
                for dx, dy in [(1,0), (0,1), (-1,0), (0,-1)]:
                    nx, ny = x + dx, y + dy
                    dfs(nx, ny)
         
        for i in range(n):
            for j in range(m):
                if i == 0 or i == n - 1 or j == 0 or j == m - 1:
                    dfs(i, j)
        
        for i in range(n):
            for j in range(m):
                if board[i][j] == "A":
                    board[i][j] = "O"
                elif board[i][j] == "O":
                    board[i][j] = "X"
```


##  109. <a name='WordSearch'></a>79. Word Search

[小梦想家](https://www.bilibili.com/video/BV1yE411g7Tb?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1iZ4y1T78D?spm_id_from=333.999.0.0)

```py
输入：board = [
    ["A","B","C","E"],
    ["S","F","C","S"],
    ["A","D","E","E"]
    ], word = "ABCCED"
输出：true

输入：board = [
    ["A","B","C","E"],
    ["S","F","C","S"],
    ["A","D","E","E"]
    ], word = "SEE"
输出：true

输入：board = [
    ["A","B","C","E"],
    ["S","F","C","S"],
    ["A","D","E","E"]
    ], word = "ABCB"
输出：false





空间复杂度：O(MN)。我们额外开辟了 O(MN) 的 visited 数组，同时栈的深度最大为 O(min(L,MN))。L 为字符串 word 的长度

时间复杂度：Θ(MN⋅3^L )

由于剪枝的存在，我们在遇到不匹配或已访问的字符时会提前退出，终止递归流程。

因此，实际的时间复杂度会远远小于 Θ(MN⋅3^L )

在每次调用函数 dfs 时，除了第一次可以进入 4 个分支以外，其余时间我们最多会进入 3 个分支

（因为每个位置只能使用一次，所以走过来的分支没法走回去）。

由于单词长为 L，故 dfs(i,j,0) 的时间复杂度为 O(3^L)

而我们要执行 O(MN) 次 dfs


class Solution:
    def exist(self, board: List[List[str]], word: str) -> bool:

        def dfs(i, j, word):
            # 单词是否出现在以i，j为起点的网格中
            # word[0] 和 word[1:] 划分
            # 结束条件：
            if len(word) == 1:
                return word[0] == board[i][j]
            # 结束条件：
            if board[i][j] != word[0]:
                return False
            
            # 设置现场
            visit[i][j] = True
            for dx, dy in [(0,1), (0, -1), (1, 0), (-1, 0)]: # 对四个方向进行搜索
                nx, ny = i + dx, j + dy
                if 0 <= nx < len(board) and 0 <= ny < len(board[0]) and not visit[nx][ny]:
                    if dfs(nx, ny, word[1:]): # dfs成功
                        return True
            # 还原现场
            visit[i][j] = False

        m = len(board)
        n = len(board[0])
        visit = [[False] * n for _ in range(m)]
        for i in range(m): # 遍历所有格子作为单词起点
            for j in range(n):
                if dfs(i, j, word): # dfs成功
                    return True
        return False
```



##  139. <a name='dfsLongestIncreasingPathinaMatrix'></a>329. 【动态🚀规划 + dfs】Longest Increasing Path in a Matrix

[花花酱](https://www.bilibili.com/video/BV1mW411d7q8?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1VK4y1K7SX?spm_id_from=333.999.0.0)

动态🚀规划


```py
输入：matrix = [
    [3,4,5],
    [3,2,6],
    [2,2,1]
    ]
输出：4 



解释：最长递增路径是 [3, 4, 5, 6]。注意不允许在对角线方向上移动。

时间复杂度： O(mn)，其中 m 和 n 分别是矩阵的行数和列数。

深度优先搜索的时间复杂度是 O(V+E)，其中 V 是节点数，E 是边数。

在矩阵中，O(V)=O(mn)， O(E)≈O(4mn)=O(mn)。

空间复杂度： O(mn)，其中 m 和 n 分别是矩阵的行数和列数。




空间复杂度主要取决于缓存和递归调用深度，缓存的空间复杂度是 O(mn)，递归调用深度不会超过 mn。
 
class Solution:
    def longestIncreasingPath(self, matrix: List[List[int]]) -> int:
        if not matrix:
            return 0
        
        @cache
        def dfs(x: int, y: int) -> int:
            ans = 1
            for dx, dy in  [(-1, 0), (1, 0), (0, -1), (0, 1)]:
                nx, ny = x + dx, y + dy
                if 0 <= nx < m and 0 <= ny < n and matrix[nx][ny] > matrix[x][y]:
                    # 从 x, y 点出发, 可以到达的最远距离
                    ans = max(ans, dfs(nx, ny) + 1)
            return ans

        res = 0
        m, n = len(matrix), len(matrix[0])
# 🌵 这里循环了 m*n 次
        for i in range(m):
            for j in range(n):
                res = max(res, dfs(i, j)) # 注意：😁这里存的是从开始位置能到达多远
        return res


```


##  186. <a name='SudokuSolver'></a>37. Sudoku Solver 解数独

[花花酱](https://www.bilibili.com/video/BV1Tt41137Xr?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1f5411h7er?spm_id_from=333.999.0.0)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3k462gpgb5k0.png)

```py
# 一句都不能少
class Solution:
    def solveSudoku(self, board: List[List[str]]) -> None:
        """
        Do not return anything, modify board in-place instead.
        """
        rows = [set() for _ in range(9)]
        cols = [set() for _ in range(9)]
        grids = [[set() for _ in range(3)] for _ in range(3)]
        for i in range(9):
            for j in range(9):
                if board[i][j] != '.':
                    if  board[i][j] not in rows[i] and \
                        board[i][j] not in cols[j] and \
                        board[i][j] not in grids[i//3][j//3]:
                        rows[i].add(board[i][j])
                        cols[j].add(board[i][j])
                        grids[i//3][j//3].add(board[i][j])

        def dfs(i,j):
            if board[i][j] != '.': # 被数字填满

                if i == 8 and j == 8:
                    self.flag = True
                    return
                if j < 8:    dfs(i, j + 1)
                if j == 8:   dfs(i + 1, 0)
                    
            else: # not 被数字填满
                for num in range(1,10):
                    item = str(num)
                    if  item not in rows[i] and \
                        item not in cols[j] and \
                        item not in grids[i//3][j//3]:
                        board[i][j] = item
                        rows[i].add(item)
                        cols[j].add(item)
                        grids[i//3][j//3].add(item)

                        # 易错点: 注意缩进关系
                        if i == 8 and j == 8:
                            self.flag = True
                            return
                        if j < 8:      dfs(i, j + 1)
                        if j == 8:     dfs(i + 1, 0)
                        '''
                        这一行至关重要
                        '''
                        if self.flag:  return
                            
                        board[i][j] = '.'
                        rows[i].remove(item)
                        cols[j].remove(item)
                        grids[i//3][j//3].remove(item)

        self.flag = False
        dfs(0,0)

```

##  22. <a name='AddStrings'></a>415-Add Strings

[哈哈哈](https://www.bilibili.com/video/BV18E411n7Cy?spm_id_from=333.999.0.0)

```py
python
输入：num1 = "456", num2 = "77"
输出："533"

class Solution:
    def addStrings(self, num1: str, num2: str) -> str:
        '''
        从后往前 <--- 
        i -= 1
        j -= 1
        '''
        i, j, carry, tail = len(num1)-1, len(num2)-1, 0, 0
        res = ''

        while i >= 0 or j >= 0 or carry: # 😐 while 循环
            val = carry

            if i >= 0:
                val += ord(num1[i]) - ord('0')
                i -= 1
            if j >= 0:
                val += ord(num2[j]) - ord('0')
                j -= 1

            carry, tail = divmod(val, 10)
            res = str(tail) + res

        return res  

时间复杂度：O(max(len1,len2))

空间复杂度： 1


警察叔叔，我没有用 int
class Solution:
    def addStrings(self, num1: str, num2: str) -> str:
        equation = num1+'+'+num2
        return str(eval(equation))

class Solution(object):
    def addStrings(self, num1, num2):
        return str((eval(num1)+eval(num2)))
```



##  68. <a name='-1'></a>43. 字符串相乘

```py
输入: num1 = "2", num2 = "3"
输出: "6"




输入: num1 = "123", num2 = "456"
输出: "56088"





时间复杂度：O(mn)， 需要计算 num1, num2的每一位的乘积。

空间复杂度：O(m+n) 需要创建一个长度为 m+n 的数组存储乘积。




class Solution:
    def multiply(self, num1: str, num2: str) -> str:
        m, n = len(num1), len(num2)
        ansArr = [0] * (m + n)
        # 从后往前
        for i in range(m - 1, -1, -1):
            x = int(num1[i])
            for j in range(n - 1, -1, -1):
                ansArr[i + j + 1] += x * int(num2[j])
        
        # 从后往前
        for i in range(m + n - 1, 0, -1):
            ansArr[i - 1] += ansArr[i] // 10
            ansArr[i] %= 10
        
        res = ''.join(str(x) for x in ansArr)
        return str(int(res))
```



##  156. <a name='9.36-415-AddStrings'></a>补充题9. 36进制加法 - 见 415 - Add Strings

```s
36进制由 0-9，a-z，共 36 个字符表示。

要求按照加法规则计算出任意两个36进制正整数的和，如 1b + 2x = 48  （解释：47 + 105 = 152）

要求：不允许使用先将36进制数字整体转为10进制，相加后再转回为36进制的做法

题目分析：此题难度倒不是很大，实际上是 LC415. 字符串相加的扩展。

LC415 是十进制的大数相加，而本题是`36进制`的大数相加。

顺便提一句，我强烈推荐415题使用以下代码的写法，优雅简洁，不容易出Bug。

如果第一次见，可能需要多反应会儿，但明白了以后就会有相见恨晚的感觉。
```


##  179. <a name='ExcelSheetColumnTitle'></a>168-Excel Sheet Column Title

[哈哈哈](https://www.bilibili.com/video/BV1Qj411f7FY?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Yb411H777?spm_id_from=333.999.0.0)

```py
输入：columnNumber = 1
输出："A"




输入：columnNumber = 28
输出："AB"




输入：columnNumber = 701
输出："ZY"




输入：columnNumber = 2147483647
输出："FXSHRXW"





时间复杂度： O(log26columnNumber)。

空间复杂度：O(1)。



class Solution(object):
    def convertToTitle(self, columnNumber):
        res = ''
        '''
        余加除
        '''
        while columnNumber: # 😐😐 while 循环
            columnNumber -= 1                       # 又想了好久才知道在哪里减一。。
            res = chr(columnNumber % 26 + 65) + res # A的ascii码为65
            columnNumber = columnNumber // 26 
        return res
```

##  218. <a name='ConvertaNumbertoHexadecimal'></a>405 【位运算😜】Convert a Number to Hexadecimal

[哈哈哈](https://www.bilibili.com/video/BV1pj411f7ds?spm_id_from=333.999.0.0)

```py
输入:
26

输出:
"1a"





输入:
-1

输出:
"ffffffff"



0xffffffff = 1111 1111 1111 1111 1111 1111 1111 1111 # (8个F的二进制形式, 一个F占4个字节 )  # 2 ^ 32 - 1



时间复杂度： O(k)，其中 k 是整数的十六进制数的位数，这道题中 k=8。

空间复杂度： O(k)，其中 k 是整数的十六进制数的位数，这道题中 k=8。

空间复杂度主要取决于中间结果的存储空间，这道题中需要存储前导零以外的全部数位。



class Solution:
    def toHex(self, num):
        num = num & 0xffffffff 
        res = ""
        lib = "0123456789abcdef"
        if num == 0: return "0"
        while num: # 😐 while 循环
            '''
            余加除
            '''
            res = lib[num % 16] + res # 一定要加在右边
            num //= 16
        return res
```

##  191. <a name='ExcelSheetColumnNumber'></a>171. Excel Sheet Column Number

[小梦想家](https://www.bilibili.com/video/BV1Yb411H7nT?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1h541187Sv?spm_id_from=333.999.0.0)

```py
输入: columnTitle = "A"
输出: 1




输入: columnTitle = "AB"
输出: 28




输入: columnTitle = "ZY"
输出: 701




时间复杂度： O(n)，其中 n 是列名称 columnTitle 的长度。需要遍历列名称一次。

空间复杂度： O(1)。



# python 从左到右遍历
        # 26进制转10进制
class Solution:
def titleToNumber(self, columnTitle: str) -> int:
        res = 0
        for char in columnTitle:
            res *= 26
            res += ord(char) - ord('A') + 1 
        return res
```




##  45. <a name='AddTwoNumbers'></a>2. Add Two Numbers

[花花酱](https://www.bilibili.com/video/BV1EJ411h72z?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1gJ411V7gJ?spm_id_from=333.999.0.0)

[小梦想](https://www.bilibili.com/video/BV1Wb411e77s?spm_id_from=333.999.0.0)

[洛阳](https://www.bilibili.com/video/BV1rZ4y1j7V3?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1DA411L7YQ?spm_id_from=333.999.0.0)

* 时间复杂度:O(max(m,n))

* 时间复杂度:O(1).注意返回值不计入空间复杂度。

特殊情况：

两个链表的长度不同。

进位

```py

输入：l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
输出：[8,9,9,9,0,0,0,1]



class Solution:
    def addTwoNumbers(self, l1: ListNode, l2: ListNode) -> ListNode:
        dummy = cur = ListNode(0) # 易错点：定义一个dummy和一个pointer，都指向ListNode(0)
        carry = 0 # 易错点：carry需要先赋值
        '''
        not 从后往前 <--- 
        l1 = l1.next if l1 else None
        l2 = l2.next if l2 else None
        '''

        while l1 or l2 or carry: # 易错点：carry要存在 # 😁 while 循环
            # 易错点：l1,l2不一定存在，所以不能写成：sumNode = l1 + l2
            # 易错点：调用listnode要有.val
            sumNode = (l1.val if l1 else 0) + (l2.val if l2 else 0) + carry
            carry, tail = divmod(sumNode,10) 

            cur.next = ListNode(tail)
            cur = cur.next
            l1 = l1.next if l1 else None # # l1,l2不一定存在，所以不能写成：l1 = l1.next
            l2 = l2.next if l2 else None
        return dummy.next
```


##  154. <a name='AddTwoNumbersII'></a>445-Add Two Numbers II

[哈哈哈](https://www.bilibili.com/video/BV1Qj411f7Qz?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1Pt4y1m78o?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV17a4y1s7BG?spm_id_from=333.999.0.0)

```py
输入：l1 = [7,2,4,3], l2 = [5,6,4]
输出：[7,8,0,7]




时间复杂度： O(max(m,n))，其中 m 和 n 分别为两个链表的长度。

我们需要遍历两个链表的全部位置，而处理每个位置只需要 O(1) 的时间。

空间复杂度： O(m+n)，其中 m 和 n 分别为两个链表的长度。

空间复杂度主要取决于我们把链表内容放入栈中所用的空间。
 



class Solution:
    def addTwoNumbers(self, l1: ListNode, l2: ListNode) -> ListNode:
        '''
        对比上一题，这里使用了stack
        '''
        stack1, stack2 = [], []
        while l1: # 😐 while 循环
            stack1.append(l1.val)
            l1 = l1.next
        while l2: # 😐 while 循环
            stack2.append(l2.val)
            l2 = l2.next
        res = None
        carry = 0
        '''
        not 从后往前 <--- 
        val1 = stack1.pop() if stack1 else 0 
        val1 = stack1.pop() if stack1 else 0 
        '''
        while stack1 or stack2 or carry: # 😐 while 循环
            val1 = stack1.pop() if stack1 else 0 
            val2 = stack2.pop() if stack2 else 0 

            sumNode = val1 + val2 + carry
            carry, tail = divmod(sumNode,10) 

            tmp = ListNode(tail)
            tmp.next = res
            res = tmp
        return res

```

##  23. <a name='-1'></a>46- ★ 全排列

类似题目：

https://leetcode-cn.com/problems/permutation-i-lcci/

```py
输入：nums = [1,2,3]
输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]




输入：nums = [0,1]
输出：[[0,1],[1,0]]




输入：nums = [1]
输出：[[1]]





时间复杂度： O(n×n!)，其中 n 为序列的长度。

算法的复杂度首先受 backtrack 的调用次数制约
backtrack 的调用次数是 O(n!) 的。
我们需要将当前答案使用 O(n) 的时间复制到答案数组中
相乘得时间复杂度为 O(n×n!)





空间复杂度：O(n)，递归函数在递归过程中需要为每一层递归函数分配栈空间，

所以这里需要额外的空间且该空间取决于递归的深度



class Solution:
    def permutation(self, S: str) -> List[str]:
        res = []
        path = ''
        def backtrack(S, path):
            if S == '':
                res.append(path) # 这里不需要：path[:]，因为这里 path 是 str
                return 

            for i in range(len(S)):
                cur = S[i]
                backtrack(S[:i] + S[i+1:], path + cur)
                
        backtrack(S, path)

        return res
```

[哈哈哈](https://www.bilibili.com/video/BV1YA411v7zF?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1hb411i7fm?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1oa4y1v7Kz?spm_id_from=333.999.0.0)

```py
# class Solution:
#     def permute(self, nums: List[int]) -> List[List[int]]:
#         res = []
#         path = []
#         def backtrack(nums):
#             if not nums: 
#                 res.append(path[:]) 
#                 return
#             else:
#                 for i in range(len(nums)):
#                     path.append(nums[i])
#                     backtrack(nums[:i]+nums[i+1:]) 
#                     path.pop()
#         backtrack(nums)
#         return res

# 另一种写法😋
class Solution:
    def permute(self, nums: List[int]) -> List[List[int]]:
        res = []
        # n = len(nums)
        def backtrack(nums, path):
            # 易错点：if len(path) == n:
            if not nums: # 判断条件应该是这个
                res.append(path[:]) # 易错点：path[:]
                return
            else:
                for i in range(len(nums)):
                    backtrack(nums[:i] + nums[i+1:], path + [nums[i]]) # 易错点：n是不断变小的
        backtrack(nums, [])
        return res
```



##  57. <a name='IP'></a>93. 复原 IP 地址

```py
输入：s = "25525511135"
输出：["255.255.11.135","255.255.111.35"]



输入：s = "101023"
输出：["1.0.10.23","1.0.102.3","10.1.0.23","10.10.2.3","101.0.2.3"]



class Solution:
    def restoreIpAddresses(self, s: str) -> List[str]:
        res = []
        def backtrack(s,path):
            if len(path) == 4 and len(s) == 0:
                res.append('.'.join(path))
                return # 注意点：一定要返回
            for i in range(len(s)):
                left, right = s[:i+1], s[i+1:]
                if 0 <= int(left) <= 255 and str(int(left)) == left:
                    backtrack(right, path + [left])  
        backtrack(s, [])    
        return res



时间复杂度： O(3^SEG_COUNT})

由于 IP 地址的每一段的位数不会超过 3，因此在递归的每一层，我们最多只会深入到下一层的 3 种情况.

由于 SEG_COUNT=4，对应着递归的最大层数，所以递归本身的时间复杂度为 O(3^SEG_COUNT}).



空间复杂度：O(SEG_COUNT) 递归使用的空间与递归的最大深度 SEG_COUNT 成正比。
```


##  85. <a name='ValidateIPAddress'></a>468 Validate IP Address

[小明](https://www.bilibili.com/video/BV1tg4y1q7Kq?spm_id_from=333.999.0.0)

```py
输入：queryIP = "172.16.254.1"
输出："IPv4"
解释：有效的 IPv4 地址，返回 "IPv4"




输入：queryIP = "2001:0db8:85a3:0:0:8A2E:0370:7334"
输出："IPv6"
解释：有效的 IPv6 地址，返回 "IPv6"




输入：queryIP = "256.256.256.256"
输出："Neither"
解释：既不是 IPv4 地址，又不是 IPv6 地址




class Solution:
    """
    第1步：分割字符串
    第2步：判断长度
    第3步：分析其中的每个元素
    """
    def validIPAddress(self, IP: str) -> str:
        if "." in IP:
            # ipv4
            ipv4 = IP.split(".")
            if len(ipv4) != 4:
                return "Neither"
            for num in ipv4:
                # 192.168.01.1 为无效IPv4地址
                # 192.168@1.1 为无效IPv4地址
                # 0 <= xi <= 255 
                if  (len(num) >= 2 and num[0] == "0")  \
                    or not num.isdigit()  \
                    or (not 0 <= int(num) <= 255):
                    return "Neither"
            return "IPv4"
        else:
            ipv6 = IP.split(":")
            if len(ipv6) != 8:
                return "Neither"
            for num in ipv6:
                # 1 <= xi.length <= 4
                # 可以包含数字、小写英文字母( 'a' 到 'f' )和大写英文字母( 'A' 到 'F' )。
                if  (not 1 <= len(num) <= 4) \
                    or not all(map(lambda x: x.lower() in "0123456789abcdef", num)):
                    return "Neither"
            return "IPv6"


```


##  188. <a name='22.IP'></a>补充题22. IP地址与整数的转换

https://mp.weixin.qq.com/s/UWCuEtNS2kuAuDY-eIbghg




##  259. <a name='PathSumIII'></a>437 【前缀和🎨】Path Sum III

[小明](https://www.bilibili.com/video/BV1tZ4y1M7JR?spm_id_from=333.999.0.0)

时间复杂度 O(n), 空间复杂度 O(n)

```py
输入：root = [10,5,-3,3,2,null,11,3,-2,null,1], targetSum = 8
输出：3

解释：和等于 8 的路径有 3 条，如图所示。


       10
     /    \
    5     -3
   / \      \
  3   2     11
 / \   \
3  -2   1
 
        O
     /    \
    5      O
   / \      \
  3   O      O
 / \   \
O   O   O

        O
     /    \
    5      O
   / \      \
  O   2      O
 / \   \
O   O   1

        O
     /    \
    O     -3
   / \      \
  O   O     11
 / \   \
O   O   O

时间复杂度： O(N)，其中 N 为二叉树中节点的个数。

利用`前缀和`只需遍历一次二叉树即可。



空间复杂度： O(N)。

class Solution:
    def pathSum(self, root: TreeNode, targetSum: int) -> int:
        dic = collections.defaultdict(int)
        dic[0] = 1
        res = 0
        def backtrack(root, preSums):
            nonlocal res
            if root:
                
                preSums += root.val
                if preSums - targetSum in dic: 
                    res += dic[preSums - targetSum]
                
                dic[preSums] += 1
                backtrack(root.left, preSums)
                backtrack(root.right, preSums)
                dic[preSums] -= 1 # Note: 回到上一层时, 需要将当前的前缀和对应的路径数目减1  
        
        backtrack(root, 0)
        return res 
```



##  71. <a name='Subsets'></a>78. Subsets 子集

[花花酱](https://www.bilibili.com/video/BV1jt411k7py?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1HD4y1Q7Te?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1YK4y1s7pq?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1154y1R72Q?spm_id_from=333.999.0.0)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.xmmpwe7mlzk.webp)

时间复杂度：O(n·2^n)

```py
# 【位运算😜】
# class Solution:
#     def subsets(self, nums: List[int]) -> List[List[int]]:
#         size = len(nums)
#         n = 1 << size
#         res = []
#         # i = 0,1,2,3,4,5,6,7
#         for i in range(n):
#             cur = []
#             # j = 0,1,2
#             for j in range(size):
#                 if i >> j & 1:
#                     cur.append(nums[j])
#             res.append(cur)
#         return res

```

```py
class Solution(object):
    def subsets(self, nums):
        res = [[]]
        for num in nums:
            res.extend([subres+[num] for subres in res])
        return res  

# bfs
class Solution:
    def subsets(self, nums: List[int]) -> List[List[int]]:
        res = [[]]
        n = len(nums)
        for num in nums:
            for subres in res[:]:
                res.append(subres+[num])
        return res

# 注意代码中res[:]是必须的，因为切片是引用新的对象，
# 此时在循环中res[:]是不更新的，而res是不断有元素push进去的，很trick
```

```py
输入：nums = [1,2,3]
输出：[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]



时间复杂度：O(n×2^n)。一共 2^n 个状态，每种状态需要 O(n)的时间来构造子集。
空间复杂度：O(n)。临时数组 t 的空间代价是 O(n)，递归时栈空间的代价为 O(n)。



class Solution:
    def subsets(self, nums: List[int]) -> List[List[int]]:
        res = []  
        def backtrack(startIndex,path):
            res.append(path[:])  # unconditional, 收集子集
            for i in range(startIndex, len(nums)):  #当startIndex已经大于数组的长度了，就终止了，for循环本来也结束了，所以不需要终止条件
                '''
                每个数字只能用一次, 所以 i + 1
                '''
                backtrack(i + 1, path + [nums[i]])  # nums[i] 一定要用中括号括起来
        backtrack(0,[])
        return res
```



##  76. <a name='CombinationSum39-'></a>39. Combination Sum 39-组合总和

[花花酱](https://www.bilibili.com/video/BV1gb411u7dy?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1Wz411e79d?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV12Z4y157nE?spm_id_from=333.999.0.0)

```py
输入：candidates = [2,3,6,7], target = 7
输出：[[2,2,3],[7]]



输入: candidates = [2,3,5], target = 8
输出: [[2,2,2,2],[2,3,3],[3,5]]




时间复杂度： O(S)，其中 S 为所有可行解的长度之和。O(n×2^n) 是一个比较松的上界.实际运行情况是远远小于这个上界的。

从分析给出的搜索树我们可以看出时间复杂度取决于搜索树所有叶子节点的深度之和.

即在这份代码中，n 个位置每次考虑选或者不选，如果符合条件，就加入答案的时间代价。



空间复杂度： O(target)。除答案数组外，空间复杂度取决于递归的栈深度，在最差情况下需要递归 O(target) 层。
 

class Solution:
    def combinationSum(self, candidates: List[int], target: int) -> List[List[int]]:
        res = []

        def backtrack(firstIdx, path):
            if sum(path) == target:
                res.append(path[:]) 
                # 易错点，这里是res.append(path[:])，而不是res.append(path)
                return
            if sum(path) > target:
                return
            if sum(path) < target:
                for i in range(firstIdx, len(candidates)):
                '''
                每个数字能用 n 次, 所以 i
                '''
                    backtrack(i, path + [candidates[i]])
        backtrack(0, [])
        return res
```



##  131. <a name='II-'></a>47 - ★ 全排列 II-剪枝版

类似题目：

https://leetcode-cn.com/problems/permutation-ii-lcci/

```py
输入：nums = [1,1,2]
输出：
[[1,1,2],
 [1,2,1],
 [2,1,1]]




输入：nums = [1,2,3]
输出：[
    [1,2,3],
    [1,3,2],
    [2,1,3],
    [2,3,1],
    [3,1,2],
    [3,2,1]
    ]




时间复杂度： O(n×n!)，其中 n 为序列的长度。

算法的复杂度首先受 backtrack 的调用次数制约
backtrack 的调用次数是 O(n!) 的。
我们需要将当前答案使用 O(n) 的时间复制到答案数组中
相乘得时间复杂度为 O(n×n!)



空间复杂度：O(n)，递归函数在递归过程中需要为每一层递归函数分配栈空间，

所以这里需要额外的空间且该空间取决于递归的深度



class Solution:
    def permutation(self, S: str) -> List[str]:
        res = []
        S = sorted(S)
        def backtrack(S,path):
            if not S:
                res.append(path)
            else:
                for i in range(len(S)):
                    if i > 0 and S[i] == S[i-1]:  # 剪枝
                        continue
                    backtrack(S[:i] + S[i+1:], path + S[i])

        backtrack(S,'')
        return res
```

[哈哈哈](https://www.bilibili.com/video/BV1Ev411672A?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1qK4y1x7Qs?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1z54y1a7rQ?spm_id_from=333.999.0.0)

```py
class Solution:
    def permuteUnique(self, nums: List[int]) -> List[List[int]]:
        res = []
        nums.sort()
        def backtrack(nums,path):
            if not nums:
                res.append(path[:])
            else:
                for i in range(len(nums)):
                    if i > 0 and nums[i] == nums[i-1]: # 剪枝
                        continue
                    backtrack(nums[:i] + nums[i+1:],path + [nums[i]])

        backtrack(nums,[])
        return res
```


##  142. <a name='CombinationSumII40-II'></a>40. Combination Sum II 40-组合总和 II

[花花酱](https://www.bilibili.com/video/BV1Pb411u7Yd?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1gT4y1J7JE?spm_id_from=333.999.0.0)

```py
输入: candidates = [10,1,2,7,6,1,5], target = 8,
输出:
[
[1,1,6],
[1,2,5],
[1,7],
[2,6]
]




输入: candidates = [2,5,2,1,2], target = 5,
输出:
[
[1,2,2],
[5]
]




时间复杂度： O(S)，其中 S 为所有可行解的长度之和。O(n×2^n) 是一个比较松的上界.实际运行情况是远远小于这个上界的。

从分析给出的搜索树我们可以看出时间复杂度取决于搜索树所有叶子节点的深度之和.

即在这份代码中，n 个位置每次考虑选或者不选，如果符合条件，就加入答案的时间代价。

每得到一个满足要求的组合，需要 O(n) 的时间将其放入答案中，因此我们将 O(2^n) 与 O(n) 相乘，即可估算出一个宽松的时间复杂度上界。




空间复杂度： O(n)。除了存储答案的数组外，我们需要 O(n) 的空间存储列表 freq、递归中存储当前选择的数的列表、以及递归需要的栈。

class Solution:
    def combinationSum2(self, candidates: List[int], target: int) -> List[List[int]]:
        res = []
        candidates.sort()
        # candidates.reverse()
        def backtrack(firstIdx, path):
            if sum(path) == target:
                res.append(path[:])
                return
            if sum(path) > target:
                return
            if sum(path) < target:
                for i in range(firstIdx, len(candidates)):
                    # 易错点：需要剪枝
                    if i > firstIdx and candidates[i] == candidates[i - 1]: continue
                    # [1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1]会超时
                    '''
                    candidates 中的每个数字在每个组合中只能使用 一次, 所以 i+1
                    '''
                    backtrack(i + 1, path + [candidates[i]])
        backtrack(0, [])
        return res
```


```py
时间复杂度：O(1) 。一共有 9216 种可能性，对于每种可能性，各项操作的时间复杂度都是 O(1)，因此总时间复杂度是 O(1)。

空间复杂度：O(1) 。空间复杂度取决于递归调用层数与存储中间状态的列表，

因为一共有 4 个数，所以递归调用的层数最多为 4，存储中间状态的列表最多包含 4 个元素，因此空间复杂度为常数。

class Solution:
    def judgePoint24(self, nums: List[int]) -> bool:
        TARGET = 24
        EPSILON = 1e-6
        ADD, MULTIPLY, SUBTRACT, DIVIDE = 0, 1, 2, 3

        def backtrack(nums: List[float]) -> bool:
            if not nums:
                return False
            if len(nums) == 1:
                return abs(nums[0] - TARGET) < EPSILON
            for i, x in enumerate(nums):
                for j, y in enumerate(nums):
                    if i != j:
                        newNums = []
                        # 把 x, y 之外的 2个nums 放到 newNums
                        for k, z in enumerate(nums):
                            if k != i and k != j:
                                newNums.append(z)
                        # 把 x y 进行加减乘除运算 
                        for op in range(4):
                            '''
                            剪枝：op < 2 and i > j，其中 + 和 * 和计算次序无关 
                            '''
                            if op < 2 and i > j: continue
                            if op == ADD:
                                newNums.append(x + y)
                            elif op == MULTIPLY:
                                newNums.append(x * y)
                            elif op == SUBTRACT:
                                newNums.append(x - y)
                            elif op == DIVIDE:
                                if abs(y) < EPSILON:
                                    continue
                                newNums.append(x / y)
                            '''
                            backtrack 4 遍
                            '''
                            if backtrack(newNums):
                                return True
                            newNums.pop()
            return False

        return backtrack(nums)


```


##  211. <a name='UniqueBinarySearchTrees'></a>96. Unique Binary Search Trees

[小梦想家](https://www.bilibili.com/video/BV1xV411Y731?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1e5411W72t?spm_id_from=333.999.0.0)

```py
输入：n = 3
输出：5




输入：n = 1
输出：1



# 基于上一问修改
class Solution:
    def numTrees(self, n: int) -> int:

        @cache
        def backtrack(n):
            if n <= 1:
                return 1
            else:
                res = 0 # res一定要写在backtrack里面
                for rootI in range(n):
                    leftTrees = backtrack(rootI)
                    rightTrees = backtrack(n - rootI - 1)
                    res += leftTrees * rightTrees # 易错点：一定是加号
                return res
                    
        return backtrack(n)

class Solution:
    def numTrees(self, n: int) -> int:
        dp = [0] * (n + 1)
        dp[0] = 1
        dp[1] = 1
        for i in range(2, n + 1):
            for subi in range(i):
                dp[i] += dp[subi] * dp[i - subi - 1]
        return dp[-1]

时间复杂度 : O(n^2) ，其中 n 表示二叉搜索树的节点个数。

G(n) 函数一共有 n 个值需要求解，每次求解需要 O(n) 的时间复杂度，因此总时间复杂度为 O(n^2)。

空间复杂度 : O(n)。我们需要 O(n) 的空间存储 G 数组。
 
```

##  234. <a name='-1'></a>77. 组合

```py
给定两个整数 n 和 k，返回范围 [1, n] 中所有可能的 k 个数的组合。

你可以按 任何顺序 返回答案。

输入：n = 4, k = 2


输出：
[
  [2,4],
  [3,4],
  [2,3],
  [1,2],
  [1,3],
  [1,4],
]


class Solution:
    def combine(self, n, k):
        res = []
        def backtrack(StartIndex,path):
            if len(path) == k:
                res.append(path[:])
                return
            for i in range(StartIndex, n + 1):
                '''
                没有重复，所以 i + 1
                '''
                backtrack(i + 1,path + [i])
        backtrack(1, [])
        return res



时间复杂度：O((kn的组合枚举)×k)，每次记录答案的复杂度为 O(k)
空间复杂度：O(n + k) = O(n) 
```









##  27. <a name='SpiralMatrix'></a>54. Spiral Matrix

https://leetcode-cn.com/problems/spiral-matrix/

[小梦想家](https://www.bilibili.com/video/BV1N7411h7i1?spm_id_from=333.999.0.0)

```py
输入：matrix = [[1,2,3],[4,5,6],[7,8,9]]
输出：[1,2,3,6,9,8,7,4,5]


输入：matrix = [[1,2,3,4],[5,6,7,8],[9,10,11,12]]
输出：[1,2,3,4,8,12,11,10,9,5,6,7]



class Solution(object):
    def spiralOrder(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: List[int]
        """
        # print(list(matrix.pop(0)))
        print(list(zip(*matrix)))
        print(list(zip(*matrix))[::-1])
        return matrix and list(matrix.pop(0)) + self.spiralOrder(list(zip(*matrix))[::-1])
        # 含义是，如果matrix为空，则返回matrix
```

```py
return a and b
 
等价于
 
return b if a else a
```

```py
class Solution:
    def spiralOrder(self, matrix: List[List[int]]) -> List[int]:
        res = []
        while matrix: # 😐 while 循环
            res += matrix.pop(0) # 易错点：注意是 +=
            '''
            matrix = [*zip(*matrix)][::-1] 等效
            '''
            matrix = list(zip(*matrix))[::-1] # 易错点：注意 [::-1] 的摆放
        return res
```



##  73. <a name='RotateImage'></a>48. 旋转图像 Rotate Image

[官方](https://www.bilibili.com/video/BV1mf4y1e7ox?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Wy4y1s7fs?spm_id_from=333.999.0.0)

<img src="https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3kl7avrsvhi0.png" width="30%">

```py
输入：matrix = [[1,2,3],[4,5,6],[7,8,9]]
输出：[[7,4,1],[8,5,2],[9,6,3]]


输入：matrix = [[5,1,9,11],[2,4,8,10],[13,3,6,7],[15,14,12,16]]
输出：[[15,13,2,5],[14,3,4,1],[12,6,8,9],[16,7,10,11]]


时间复杂度：O(N^2)，其中 N 是 matrix 的边长。 

空间复杂度：O(1)。为原地旋转。


class Solution:
    def rotate(self, matrix: List[List[int]]) -> None:
        """
        Do not return anything, modify matrix in-place instead.
        """
        n = len(matrix)
        for i in range(n//2): # n 和 下面的(n+1) 可以调换位置
            for j in range((n+1)//2):
                matrix[i][j], matrix[j][n-1-i], matrix[n-1-i][n-1-j], matrix[n-1-j][i] = \
                matrix[n-1-j][i], matrix[i][j], matrix[j][n-1-i], matrix[n-1-i][n-1-j]
        return matrix
```



##  105. <a name='-1'></a>498. 对角线遍历

```py


'''


    m, n = 7
   每层的索引和:
            0:              (00)               i: 0 ~ 0
            1:            (01)(10)             i: 0 ~ 1
            2:          (20)(11)(02)           i: 0 ~ 2
            3:        (03)(12)(21)(30)         i: 0 ~ 3
            4:      (40)(31)(22)(13)(04)       i: 0 ~ 4
            5:    (05)(14)(23)(32)(41)(50)     i: 0 ~ 5
            6:  (60)(51)................(06)   i: 0 ~ 6
            7:    (16)................(61)     i: 1 ~ 6
            8:      (62)............(26)       i: 2 ~ 6
            9:        (36)........(36)         i: 3 ~ 6
           10:          (64)....(46)           i: 4 ~ 6
           11:            (56)(65)             i: 5 ~ 6
           12:              (66)               i: 6 ~ 6

        按照“层次”遍历，依次append在索引边界内的值即可
'''



时间复杂度： O(N⋅M)
空间复杂度：O(N+M-1)



输入：mat = [[1,2,3],[4,5,6],[7,8,9]]
输出：[1,2,4,7,5,3,6,8,9]



class Solution:
    def findDiagonalOrder(self, matrix: List[List[int]]) -> List[int]:
        m, n = len(matrix), len(matrix) and len(matrix[0])
        # dic = collections.defaultdict(list), 这个方法相对没那么好，因为次序会乱掉
        dic = [[] for _ in range(m + n - 1)]
        for i in range(m):
            for j in range(n):
                dic[i + j].append(matrix[i][j])
        res = []
        for i, diag in enumerate(dic):
            '''
            extend, 而不是append
            '''
            res.extend(diag if i % 2 else diag[::-1])
        return res
```

##  112. <a name='SpiralMatrixII'></a>59. Spiral Matrix II 

[小梦想家](https://www.bilibili.com/video/BV1J741157Kt?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1q5411G7MY?spm_id_from=333.999.0.0)

```py
输入：n = 3
输出：[[1,2,3],[8,9,4],[7,6,5]]



时间复杂度： O(N⋅N)
空间复杂度：O(1)



class Solution:
    def generateMatrix(self, n: int) -> List[List[int]]:
        res    = [[0 for _ in range(n)] for _ in range(n)]
        x,  y  = 0, 0 
        dx, dy = 0, 1
        # 0,1 -> 1,0 -> 0,-1 -> -1,0
        for num in range(1, n * n + 1):
            res[x][y] = num
            nx, ny = x + dx, y + dy

            if not 0 <= nx < n or not 0 <= ny < n or res[nx][ny] != 0:
            # 易错点：or res[nx][ny] != 0 顺序很重要，一定要在最后，就像贪吃蛇
                dx, dy = dy, -dx # 调头

            x += dx
            y += dy
        return res
```

##  28. <a name='LongestIncreasingSubsequence'></a>300 【动态🚀规划 + 二分】Longest Increasing Subsequence 最长上升子序列

https://leetcode-cn.com/problems/longest-increasing-subsequence/

[花花酱](https://www.bilibili.com/video/BV1Wf4y1y7ou?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1rT4y1ujV?spm_id_from=333.999.0.0)

动态规划： 时间复杂度为 O(n2)

```py
输入：nums = [10,9,2,5,3,7,101,18]
输出：4
解释：最长递增子序列是 [2,3,7,101]，因此长度为 4 。



输入：nums = [0,1,0,3,2,3]
输出：4




输入：nums = [7,7,7,7,7,7,7]
输出：1



class Solution(object):
    def lengthOfLIS(self, nums):
        if not nums:
            return 0

        dp = [1 for i in range(len(nums))]
        # [10,9,2,5,3,7,101,18]
        # [1,1,1,1,1,1,1,1]
        # [1,1, , , , , , ]
        # [1,1,1, , , , , ]
        # [1,1,1,2, , , , ]
        # [1,1,1,2,1+1, , , ]
        # [1,1,1,2,2,1+1或2+1或2+1, , ]
        # [1,1,1,2,2,3,1+1或2+1或3+1, ]

        for end in range(1, len(nums)): # 先确定结束，再确定开始
            for stt in range(end):
                if nums[end] > nums[stt]:
                    dp[end] = max(dp[stt] + 1, dp[end])

        return max(dp)



时间复杂度：O(n^2) 

空间复杂度：O(n) ，需要额外使用长度为 n 的 dp 数组。

```


贪心 + 二分查找

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6wjfuj0uqvo0.webp)

```py
class Solution:
    def lengthOfLIS(self, nums: List[int]) -> int:
        res = []
        for num in nums:
            # bisect_left会把新的元素放在相等元素前面, 即原来值5的索引位置2
            i = bisect_left(res, num)
            if i == len(res):
                res.append(num) # 如果新元素插入在最后面
            else:
                res[i] = num # 如果新元素代替旧元素
        return len(res)
时间复杂度：O(N logN) 

空间复杂度：O(N)
```



##  198. <a name='NumberofLongestIncreasingSubse'></a>673 Number of Longest Increasing Subse

[小明](https://www.bilibili.com/video/BV1gT4y1F7y3?spm_id_from=333.999.0.0)


```py
note：这道题返回序列个数

输入: [1,3,5,4,7]
输出: 2
解释: 有两个最长递增子序列，分别是 [1, 3, 4, 7] 和[1, 3, 5, 7]。

dp:   [1, 2, 1, 1, 1]
cnt:  [1, 1, 1, 1, 1]

dp:   [1, 2, 3, 1, 1]
cnt:  [1, 1, 1, 1, 1]

dp:   [1, 2, 3, 3, 1]
cnt:  [1, 1, 1, 1, 1]

dp:   [1, 2, 3, 3, 4]
cnt:  [1, 1, 1, 1, 2]

end,stt: 3 1 此时: 2 > 1
end,stt: 5 1 此时: 2 > 1
end,stt: 5 3 此时: 3 > 2
end,stt: 4 1 此时: 2 > 1
end,stt: 4 3 此时: 3 > 2
end,stt: 7 1 此时: 2 > 1
end,stt: 7 3 此时: 3 > 2
end,stt: 7 5 此时: 4 > 3
end,stt: 7 4 此时: 4 == 4

i,j: 7 4 此时: 4 == 4




输入: [2,2,2,2,2]
输出: 5


解释: 最长递增子序列的长度是1，并且存在5个子序列的长度为1，因此输出5。



时间复杂度：O(N^2) 
空间复杂度：O(N)



class Solution:
    def findNumberOfLIS(self, nums: List[int]) -> int:
        n = len(nums)
        if n <= 1: return n

        dp = [1 for _ in range(n)] # dp[i] 表示以 nums[i] 结尾的最长的子序列的长度
        cnt = [1 for _ in range(n)]

        maxCount = 0
        for end in range(1, n):
            for stt in range(end):
                if nums[end] > nums[stt]:
                    if dp[stt] + 1 > dp[end] : # 更长，则更新最长的长度和个数
                        dp[end] = dp[stt] + 1
                        cnt[end] = cnt[stt]
                    elif dp[stt] + 1 == dp[end] : # 相等时，把个数加上去
                        cnt[end] += cnt[stt]
                '''
                输入: [2,2,2,2,2]
                这种情况，cnt的每个1都是答案
                '''
                if dp[end] > maxCount:
                    maxCount = dp[end] # 统计最长的序列的所有次数
        res = 0
        for end in range(n):
            if maxCount == dp[end]: # 长度和个数一一对应
                res += cnt[end]
        return res

```

##  29. <a name='BinarySearch'></a>704.Binary Search二分查找

https://leetcode-cn.com/problems/binary-search/

[图灵](https://www.bilibili.com/video/BV1Dh411v7yT?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1qa4y157E4?spm_id_from=333.999.0.0)

```py
输入: nums = [-1,0,3,5,9,12], target = 9
输出: 4
解释: 9 出现在 nums 中并且下标为 4




输入: nums = [-1,0,3,5,9,12], target = 2
输出: -1
解释: 2 不存在 nums 中因此返回 -1






对不起没忍住

class Solution:
    def search(self, nums: List[int], target: int) -> int:
        if target in nums :
            return nums.index(target)
        else:
            return -1

（版本一）左闭右闭区间，这个模板要记住
'''
这里会找不到target，返回-1
'''

时间复杂度：O(logN) 
空间复杂度：O(1)

class Solution:
    def search(self, nums: List[int], target: int) -> int:
        left, right = 0, len(nums) - 1
        
        while left <= right: # 😐 while 循环
            mid = (left + right) // 2

            if nums[mid] < target:
                left = mid + 1
            elif nums[mid] > target:
                right = mid - 1
            else:
                return mid
        return -1

```


##  30. <a name='TrappingRainWater'></a>42. Trapping Rain Water

https://leetcode-cn.com/problems/trapping-rain-water/

[花花酱](https://www.bilibili.com/video/BV1hJ41177gG?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1fi4y1t7BP?spm_id_from=333.999.0.0)


双指针：

* 时间复杂度: O(n)

* 空间复杂度: O(1)

```py
输入：height = [0,1,0,2,1,0,1,3,2,1,2,1]
输出：6
解释：上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的高度图，

在这种情况下，可以接 6 个单位的雨水（蓝色部分表示雨水）。 



输入：height = [4,2,0,3,2,5]
输出：9
 





#   😋我的模仿
[0,1,0,2,1,0,1,3,2,1,2,1]

class Solution:
    def trap(self, height: List[int]) -> int:
        left = 0
        right = len(height) - 1
        leftmax = 0
        rightmax = 0
        res = 0
        while left < right: # 😐 while 循环
            if height[left] < height[right]:
                # 短板效应，移动小的那个值
                '''
                更新 leftmax
                res 累加
                '''
                leftmax = max(leftmax, height[left])
                # 易错点：注意res和left的次序：先res，后left
                res += leftmax - height[left] 
                left += 1
            else:
                '''
                更新 rightmax
                res 累加
                '''
                rightmax = max(rightmax, height[right])
                # 易错点：注意res和right的次序：先res，后right
                res += rightmax - height[right]
                right -= 1
        return res
```

##  132. <a name='ContainerWithMostWater'></a>11. Container With Most Water 

[花花酱](https://www.bilibili.com/video/BV1CW41167qB?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Yb411H7Gn?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1A5411E7oM?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1TK41157jH?spm_id_from=333.999.0.0)

暴力解法：

* 时间复杂度:O(n2)

* 时间复杂度:O(1)

双指针法：

由于盛水面积由较短边控制，所以，指针放在两端，每次只移动较短边。因为，移动较长边的话。一定仍然是不变的。

* 时间复杂度:O(n)

* 时间复杂度:O(1)

```py
输入：[1,8,6,2,5,4,8,3,7]
输出：49 
解释：

图中垂直线代表输入数组 [1,8,6,2,5,4,8,3,7]。

在此情况下，容器能够容纳水（表示为蓝色部分）的最大值为 49。





输入：height = [1,1]
输出：1




# 这个写起来超级简单！
# NO BUG
class Solution:
    def maxArea(self, height: List[int]) -> int:
        left = 0
        right = len(height) - 1
        maxRes = res = 0
        while left < right: # 😐 while 循环
            res = (right - left) * min(height[left], height[right])
            if height[left] < height[right]:
                # 由于短板效应，只需要移动短板即可
                left += 1
            else:
                right -= 1
            maxRes = max(maxRes,res)
        return maxRes
```



##  32. <a name='Inorderwihstack'></a>94-Inorder wih stack

https://leetcode-cn.com/problems/binary--inorder-traversal/

[哈哈哈](https://www.bilibili.com/video/BV1uV411o78x?spm_id_from=333.999.0.0)

[洛阳](https://www.bilibili.com/video/BV1o54y1B7Z8?spm_id_from=333.999.0.0)

```py
输入：root = [1,null,2,3]
输出：[1,3,2]




输入：root = []
输出：[]




输入：root = [1]
输出：[1]





时间复杂度：O(n) ，其中 nn 为二叉树节点的个数。二叉树的遍历中每个节点会被访问一次且只会被访问一次。

空间复杂度：O(n) 。空间复杂度取决于递归的栈深度
 



最少代码递归：

class Solution:
    def inorderTraversal(self, root: Optional[TreeNode]) -> List[int]:
        return self.inorderTraversal(root.left) + [root.val] + self.inorderTraversal(root.right) if root else []

class Solution(object):
    def preorderTraversal(self, root):
        return [root.val] + self.preorderTraversal(root.left) + self.preorderTraversal(root.right) if root else []

class Solution:
    def inorderTraversal(self, root: TreeNode) -> List[int]:
        res = []
        if root:
            res += self.inorderTraversal(root.left)
            res.append(root.val)
            res += self.inorderTraversal(root.right)
        return res

class Solution:
    def inorderTraversal(self, root: TreeNode) -> List[int]:
        res = []
        if root:
            res.extend(self.inorderTraversal(root.left))
            res.append(root.val)
            res.extend(self.inorderTraversal(root.right))
        return res

# 前序遍历-递归-LC144_二叉树的前序遍历

class Solution:
    def preorderTraversal(self, root: TreeNode) -> List[int]:
        # 保存结果
        res = []
        
        def traversal(root: TreeNode):
            if root:
                res.append(root.val) # 前序
                traversal(root.left)    # 左
                traversal(root.right)   # 右

        traversal(root)
        return res

中序遍历-递归-LC94_二叉树的中序遍历

class Solution:
    def inorderTraversal(self, root: TreeNode) -> List[int]:
        res = []

        def traversal(root: TreeNode):
            if root:
                traversal(root.left)    # 左
                res.append(root.val) # 中序
                traversal(root.right)   # 右

        traversal(root)
        return res

# 后序遍历-递归-LC145_二叉树的后序遍历
class Solution:
    def postorderTraversal(self, root: TreeNode) -> List[int]:
        res = []

        def traversal(root: TreeNode):
            if root:
                traversal(root.left)    # 左
                traversal(root.right)   # 右
                res.append(root.val) # 后序

        traversal(root)
        return res

# # 中序遍历需先判断当前结点是否存在，若存在则将该节点放入栈中，再将当前结点设置为结点的左孩子，
# # 若不存在则取栈顶元素为cur，当且仅当栈空cur也为空，循环结束。

# class Solution:
#     def inorderTraversal(self, root: TreeNode) -> List[int]: 
#         stack, res = [], []
#         node = root
#         while stack or node:
#             if node:
#                 stack.append(node)
#                 node = node.left
#             else:
#                 node = stack.pop()
#                 res.append(node.val)
#                 node = node.right
#         return res

# class Solution:
#     def preorderTraversal(self, root: TreeNode) -> List[int]:
#         stack, res = [], []
#         node = root
#         while stack or node:
#             while node:
#                 res.append(node.val)
#                 stack.append(node)
#                 node = node.left
#             node = stack.pop()
#             node = node.right
#         return res

# class Solution:
#     def preorderTraversal(self, root: TreeNode) -> List[int]:
#         def addAllLeft(node):
#             while node:
#                 res.append(node.val) # append 优先
#                 stack.append(node)
#                 node = node.left

#         stack, res = [], []
#         node = root
#         while stack or node:
#             addAllLeft(node)
#             node = stack.pop()
#             node = node.right
#         return res

class Solution:
    def inorderTraversal(self, root: TreeNode) -> List[int]:
        def appendAllLeft(node):
            while node: # 😐 while 循环
                stack.append(node)
                node = node.left

        stack, res = [], []
        appendAllLeft(root)
        while stack: # 😐 while 循环
            node = stack.pop()
            res.append(node.val) # res.append 在中间
            appendAllLeft(node.right)
        return res

必须用stack，不能用queue，层序遍历可以用queue
class Solution:
    def postorderTraversal(self, root: TreeNode) -> List[int]:
        if not root: return []
        res = []
        stack = [root]
        while stack: # 😐 while 循环
            node = stack.pop()
            res.append(node.val)
            # 背一背：后左右 - 后座有
            if node.left: stack.append(node.left) # 目的是left先出：先进，后出，取反则先
            if node.right: stack.append(node.right) 
        return res[::-1]

必须用stack，不能用queue，层序遍历可以用queue
class Solution:
    def preorderTraversal(self, root: TreeNode) -> List[int]:
        if not root: return []
        res = []
        stack = [root]
        while stack: # 😐 while 循环
            node = stack.pop()
            res.append(node.val)
            # 背一背：前右左 - 钱优作
            if node.right: stack.append(node.right)
            if node.left:  stack.append(node.left) # 目的是left先出：后进，先出
        return res
```


##  64. <a name='ValidateBinarySearchTree98-'></a>98. Validate Binary Search Tree 98-验证二叉搜索树

[花花酱](https://www.bilibili.com/video/BV12t411Y7TP?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1Wz4y1R7dF?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Wb411e7FV?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Hv411478d?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1Fi4y147Ng?spm_id_from=333.999.0.0)

```py
输入：root = [2,1,3]
输出：true



输入：root = [5,1,4,null,null,3,6]
输出：false
解释：根节点的值是 5 ，但是右子节点的值是 4 。

有效 二叉搜索树定义如下：

节点的左子树只包含 < 当前节点的数。
节点的右子树只包含 > 当前节点的数。
所有左子树和右子树自身必须也是二叉搜索树。
```

中序遍历一下就行了

```py
时间复杂度：O(n) ，其中 n 为二叉树节点的个数。二叉树的遍历中每个节点会被访问一次且只会被访问一次。

空间复杂度：O(n) 。空间复杂度取决于递归的栈深度。最坏情况下空间复杂度为 O(n)

res是 list 的写法:
class Solution:
    def isValidBST(self, root: TreeNode) -> bool:
        res = [float('-inf')]
        valid = True # 必须用 valid 这个变量，不能用 return False

        def traversal(root: TreeNode):
            nonlocal valid # 这一行必不可少，不然虽然不报错，但不能ac
            if root:
                traversal(root.left)    # 左
                if res[-1] >= root.val: valid = False
                res.append(root.val) # 中序
                traversal(root.right)   # 右

        traversal(root)
        return valid

res不是 list 的写法:
class Solution:
    def isValidBST(self, root: TreeNode) -> bool:
        res = float('-inf')
        valid = True # 必须用 valid 这个变量，不能用 return False

        def traversal(root: TreeNode):
            nonlocal valid, res # 这一行必不可少，不然虽然不报错，但不能ac
            if root:
                traversal(root.left)    # 左
                if res >= root.val: valid = False
                res = root.val # 中序
                traversal(root.right)   # 右

        traversal(root)
        return valid



class Solution:
    def isValidBST(self, root: TreeNode) -> bool:
        def appendAllLeft(node):
            while node: # 😐 while 循环
                stack.append(node)
                node = node.left
        # 这里可以直接 return，不需要valid
        stack, res = [], float('-inf')
        appendAllLeft(root)
        while stack: # 😐 while 循环
            node = stack.pop()
            if res >= node.val: return False
            res = node.val # res.append 在中间
            appendAllLeft(node.right)
        return True

```

定义上下界：

```py
class Solution:
    def isValidBST(self, root):
        def isBetween(node, lower, upper):
            if not node: return True
            return lower < node.val < upper and isBetween(node.left, lower, node.val) and isBetween(node.right, node.val, upper)

        return isBetween(root, float('-inf'), float('inf'))
```


##  94. <a name='Offer36.-530.MinimumAbsoluteDifferenceinBST'></a>剑指 Offer 36. 二叉搜索树与双向链表 - 530. Minimum Absolute Difference in BST

【剑指36】. 将二叉搜索树转化为排序的双向链表【字节跳动】-

将一个 二叉搜索树 就地转化为一个 已排序的`双向循环链表` 。

对于`双向循环列表`，你可以将左右孩子指针作为`双向循环链表`的前驱和后继指针，

第一个节点的`前驱`是最后一个节点，最后一个节点的`后继`是第一个节点。

特别地，我们希望可以 就地 完成转换操作。当转化完成以后，树中节点的左指针需要指向前驱，树中节点的右指针需要指向后继。还需要返回链表中最小元素的指针。

![](https://s3.bmp.ovh/imgs/2022/01/11d8ac60b4c3deb6.png)

```py



时间复杂度：O(n) ，其中 n 为二叉树节点的个数。二叉树的遍历中每个节点会被访问一次且只会被访问一次。

空间复杂度：O(n) 。空间复杂度取决于递归的栈深度。最坏情况下空间复杂度为 O(n)

class Solution:
    def treeToDoublyList(self, root: 'Node') -> 'Node':
        if not root: return
        path = []
        def inorder(root):
            if root: 
                inorder(root.left)
                path.append(root)
                inorder(root.right)    
        inorder(root)
        for i in range(len(path)): # 构成一个环
            path[i].left = path[i-1]
            path[i].right = path[(i+1)%len(path)] 
        return path[0]
```

##  162. <a name='-1'></a>114题. 二叉树展开为链表

https://www.bilibili.com/video/BV1T7411A7S8?from=search&seid=15731266160913668837&spm_id_from=333.337.0.0

<img src="https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6tma3pncods0.png" width="80%">

<img src="https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5csg54lu3lw0.png" width="50%">

递归

```s
给你二叉树的根结点 root ，请你将它展开为一个单链表：

展开后的单链表应该同样使用 TreeNode ，其中 right 子指针指向链表中下一个结点，而左子指针始终为 null 。
展开后的单链表应该与二叉树 先序遍历 顺序相同。
```

```py
时间复杂度：O(n) ，其中 n 为二叉树节点的个数。二叉树的遍历中每个节点会被访问一次且只会被访问一次。

空间复杂度：O(n)



class Solution:
    def flatten(self, root: TreeNode) -> None:
        """
        Do not return anything, modify root in-place instead.
        """
        if not root: return
        stack = [root]
        pre = None # 穿针引线
        while stack:
            node = stack.pop()
            if pre:
                pre.left = None # 穿针引线
                pre.right = node # 穿针引线
            if node.right: stack.append(node.right)
            if node.left: stack.append(node.left) # 目的是left先出：后进，先出
            pre = node
        # return root

class Solution:
    def flatten(self, root: TreeNode) -> None:
        """
        Do not return anything, modify root in-place instead.
        """
        preorderList = []
        
        def preorder(root: TreeNode):
            if root: 
                preorderList.append(root) # 前序
                preorder(root.left)    # 左
                preorder(root.right)   # 右

        preorder(root)
        n = len(preorderList)
        for i in range(1, n):
            prev, curr = preorderList[i - 1], preorderList[i] # 穿针引线
            prev.left = None # 穿针引线
            prev.right = curr # 穿针引线
        # return preorderList and preorderList[0]
        # 等效return preorderList[0] if preorderList else []
        
```


##  164. <a name='DeleteNodeinaBST'></a>450. Delete Node in a BST

[花花酱](https://www.bilibili.com/video/BV1XW411d7yU?spm_id_from=333.999.0.0)

[图灵](https://www.bilibili.com/video/BV1YK4y1h7Mw?spm_id_from=333.999.0.0)

```py
class Solution:
    def deleteNode(self, root: Optional[TreeNode], key: int) -> Optional[TreeNode]:
        if not root: return None
        # 假如要删除的不是根节点
        if root.val > key:
            root.left = self.deleteNode(root.left, key)
        elif root.val < key:
            root.right = self.deleteNode(root.right, key)

        # 假如删除的是根节点
        elif not root.left:
            root = root.right # 删除根节点
        else:
            '''
            p = root.left
            while p.right: # 😐😐 while 循环
                p = p.right
            p.right = root.right # 找到左子树中最大的节点，链接到 root.right
            '''
            root = root.left # 删除根节点
        return root


找到left中的最大：
            p = root.left
            while p.right: # 😐 while 循环
                p = p.right


          5
        /  \
       3    6
     /  \    \
    2    4    7

    2 链接到4  ->  p.right = root.right

          5
        /  \
       3    6
     /       \
    2         7
     \
      4

    删除3 -> root = root.left 

          5
        /  \
       2    6
        \    \
         4    7
    再删除3

时间复杂度： O(logN)。在算法的执行过程中，我们一直在树上向左或向右移动。

首先先用 O(H1) 的时间找到要删除的节点，H_1 是从根节点到要删除节点的高度。

然后删除节点需要 O(H2) 的时间，H_2 是从要删除节点到替换节点的高度。

由于 O(H_1 + H_2) = O(H)，H 值得是树的高度，若树是一个平衡树则 H =  logN。

空间复杂度： O(H)，递归时堆栈使用的空间，H 是树的高度。

```



##  195. <a name='Offer33.'></a>剑指 Offer 33. 二叉搜索树的后序遍历序列

```py
输入: [1,6,3,2,5]
输出: false



输入: [1,3,2,6,5]
输出: true



大家好呀，还有一种解法。简单翻了几篇题解好像没人提到类似的思路。个人理解这题跟构造BST考察的内容是类似的，我们只需要利用数组尝试构建一棵BST就可以了，如果构建完成，最终数组为空，说明是合法的BST。若构建结束数组不为空，说明不是合法的BST。

实际代码实现不需要真的构建一棵BST，只需要判断是否符合BST结构，
符合则移除数组的最后一个元素，不符合直接返回即可。这样实现的话，不符合规则提前返回，相当于剪枝了。

最差情况下需要遍历全部节点，时间复杂度为O(N)。空间上只有2个常量，另外就是递归使用的栈空间，递归的深度和树的深度相关。常规情况下空间复杂度为O(logN)，极端情况下（拉成一条链）空间复杂度为O(N)。

对了，构造顺序是根->右->左，这个点挺关键的，因为是后续遍历序列，而我们是倒序遍历数组的。




class Solution:
    def verifyPostorder(self, postorder: List[int]) -> bool:
        def isBetween(postorder: List[int], ma: int, mi: int):
            if postorder: 
                val = postorder[-1]
                if mi < val < ma:
                    postorder.pop() # 根
                    isBetween(postorder, ma, val) # 右
                    isBetween(postorder, val, mi) # 左

        isBetween(postorder, sys.maxsize, -sys.maxsize)
        '''
        如果是 postorder 的话，返回结果应该是空
        '''
        return not postorder 




时间复杂度 O(N)
空间复杂度 O(N)
```

##  271. <a name='MinimumAbsoluteDifferenceinBST-Offer36.'></a>530. Minimum Absolute Difference in BST - 剑指 Offer 36. 二叉搜索树与双向链表

[花花酱](https://www.bilibili.com/video/BV1fW411k7eT?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1FJ41147BB?spm_id_from=333.999.0.0)

```py
输入：root = [4,2,6,1,3]
输出：1





输入：root = [1,0,48,null,null,12,49]
输出：1




用 path 的 list



时间复杂度 O(N)
空间复杂度 O(N)



class Solution:
    def getMinimumDifference(self, root: TreeNode) -> int:
        path = []
        def inorder(root):
            nonlocal path
            if root:
                inorder(root.left)
                path.append(root.val)
                inorder(root.right) 
        inorder(root)
        return min([path[i] - path[i-1] for i in range(1, len(path))])
 
 
# 用 preval 存储上一个值

# class Solution:
#     def getMinimumDifference(self, root: TreeNode) -> int:
#         res = inf
#         preval = None # 注意：这里是None
#         def inorder(root):
#             nonlocal res, preval
#             if root: 
#                 inorder(root.left)
#                 if preval != None:  # 这里篇pre 必须写成 != None，而不能写成 if pre: 因为pre可以是0
#                     res = min(root.val - preval, res)             
#                 preval = root.val
#                 inorder(root.right)                
#         inorder(root)       
#         return res


```



##  129. <a name='KthSmallestElementinaB-Offer54.k'></a>230 Kth Smallest Element in a B - 见 剑指 Offer 54. 二叉搜索树的第k大节点

[小明](https://www.bilibili.com/video/BV1ha4y1i7dZ?spm_id_from=333.999.0.0)

```py

输入：root = [3,1,4,null,2], k = 1
输出：1



输入：root = [5,3,6,2,4,null,null,1], k = 3
输出：3





时间复杂度： O(H+k)，其中 H 是树的高度。

在开始遍历之前，我们需要 O(H) 到达叶结点。
当树是平衡树时，时间复杂度取得最小值 O(logN+k)
当树是线性树（树中每个结点都只有一个子结点或没有子结点）时，时间复杂度取得最大值 O(N+k)。

空间复杂度： O(H)，栈中最多需要存储 H 个元素。
当树是`平衡树`时，空间复杂度取得`最小值` O(logN)；
当树是`线性树`时，空间复杂度取得`最大值` O(N)O(N)。



# 中序遍历
class Solution:
    def kthSmallest(self, root: Optional[TreeNode], k: int) -> int:
        def appendAllLeft(node):
            while node: # 😐 while 循环
                stack.append(node)
                node = node.left

        stack, res = [], []
        appendAllLeft(root)
        while stack: # 😐 while 循环 + pop + append
            node = stack.pop()
            k -= 1
            if k == 0:
                return node.val
            appendAllLeft(node.right)





kthLargest: 先右后左
class Solution:
    def kthLargest(self, root: TreeNode, k: int) -> int:
        def inorder(root):
            if root: 
                inorder(root.right)
                self.k -= 1
                if self.k == 0: 
                    self.res = root.val
                    return
                inorder(root.left)

        self.k = k
        inorder(root)
        return self.res





kthSmallest: 先左后右
class Solution:
    def kthSmallest(self, root, k: int) -> int:
        def inorder(root):
            if root: 
                inorder(root.left)
                self.k -= 1
                if self.k == 0: 
                    self.res = root.val
                    return
                inorder(root.right)

        self.k = k
        inorder(root)
        return self.res
```



##  33. <a name='BinaryTreePreorderTraversal'></a>144-Binary Tree Preorder Traversal

[哈哈哈](https://www.bilibili.com/video/BV1n7411D7NZ?spm_id_from=333.999.0.0)

[图灵](https://www.bilibili.com/video/BV1Ch411Q74P?spm_id_from=333.999.0.0)

[洛阳](https://www.bilibili.com/video/BV1RD4y1D7C7?spm_id_from=333.999.0.0)

##  34. <a name='BinaryTreePostorderTraversal'></a>145-Binary Tree Postorder Traversal

[哈哈哈](https://www.bilibili.com/video/BV1n7411D7ub?spm_id_from=333.999.0.0)

[图灵](https://www.bilibili.com/video/BV1uv411h7Gc?spm_id_from=333.999.0.0)

[洛阳](https://www.bilibili.com/video/BV1xZ4y1H7uS?spm_id_from=333.999.0.0)

##  35. <a name='BinaryTreeRightSideView'></a>199 Binary Tree Right Side View

[小明](https://www.bilibili.com/video/BV1854y1W7CB?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1xK4y1b7Wh?spm_id_from=333.999.0.0)

```py
输入: [1,2,3,null,5,null,4]
输出: [1,3,4]



输入: [1,null,3]
输出: [1,3]



输入: []
输出: []




时间复杂度 O(N)
空间复杂度 O(N)

class Solution:
    def rightSideView(self, root: TreeNode):
        if not root: return []
        res = []
        que = collections.deque([root])
        while que: # 😐 while 循环
            res.append(que[-1].val) # res.append 必须放置在第一行
            for _ in range(len(que)):
                node = que.popleft()
                if node.left:  que.append(node.left)
                if node.right: que.append(node.right)
        return res

# 递归
class Solution:
    def rightSideView(self, root: TreeNode):
        res = []
        def bfs(node, level):
            if node:
                if level == len(res): res.append(node.val)
                bfs(node.right, level + 1)  # 因为先右边，后左边，所以 append 的第一个数就是 rightSideView
                bfs(node.left,  level + 1)
        bfs(root, 0)
        return res
```


##  118. <a name='RemoveKDigits'></a>402 Remove K Digits

[小明](https://www.bilibili.com/video/BV1PV411C79X?spm_id_from=333.999.0.0)

形成一个新的最小的数字：

```py
输入：num = "1432219", k = 3
输出："1219"



解释：移除掉三个数字 4, 3, 和 2 形成一个新的最小的数字 1219 。




class Solution:
    def removeKdigits(self, num: str, k: int) -> str:
        '''
        长江后浪推前浪，前浪死在沙滩上
        '''
        numStack = []
        
        for digit in num:
            # 新来的数字更小，就 pop 掉
            while k and numStack and numStack[-1] > digit: # 😐 while 循环 + pop + append + 3个条件
                numStack.pop()
                k -= 1
        
            numStack.append(digit)
        
        # 如果 K > 0，删除末尾的 K 个字符
        finalStack = numStack[:-k] if k else numStack
        
        # 抹去前导零
        return "".join(finalStack).lstrip('0') or "0"




时间复杂度： O(n) 。

尽管存在嵌套循环，但内部循环最多运行 k 次。由于 0 < k ≤ n，

主循环的时间复杂度被限制在 2n 以内。对于主循环之外的逻辑，它们的时间复杂度是 O(n)，因此总时间复杂度为 O(n)。

空间复杂度： O(n)。栈存储数字需要线性的空间。
 
```

##  121. <a name='DailyTemperatures'></a>739-Daily Temperatures

[哈哈哈](https://www.bilibili.com/video/BV1Q7411L7w8?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1ov411z7rM?spm_id_from=333.999.0.0)

```py
输入: temperatures = [73,74,75,71,69,72,76,73]
输出: [1,1,4,2,1,1,0,0]




输入: temperatures = [30,40,50,60]
输出: [1,1,1,0]




输入: temperatures = [30,60,90]
输出: [1,1,0]





class Solution:
    def dailyTemperatures(self, temperatures: List[int]) -> List[int]:
        n = len(temperatures)
        res = [0] * n # 如果温度递减，那么答案都是 0
        stackI = []
        # 用 i 来触发计算
        for i in range(n):
            tmpt = temperatures[i]
            '''
            长江大浪踹小浪
            '''
            # 指在第 i 天之后，才会有更高的温度。
            # [73,74,75,71,69,72,76,73]
            # []
            # [0]
            # [1]
            # [2]
            # [2, 3]
            # [2, 3, 4]
            # [2, 5]
            # [6]
            # 如果比前一项大，则直接pop，成功
            while stackI and temperatures[stackI[-1]] < tmpt: # 😐😐 while 循环 + pop + append
                preIdx = stackI.pop()
                res[preIdx] = i - preIdx
            stackI.append(i) 
        return res



时间复杂度： O(n)，其中 n 是温度列表的长度。正向遍历温度列表一遍，对于温度列表中的每个下标，最多有一次进栈和出栈的操作。

空间复杂度： O(n)，其中 n 是温度列表的长度。需要维护一个单调栈存储温度列表中的下标。
 
```

##  202. <a name='LargestRectangleinHistogram'></a>85. 最大矩形 - 84. 柱状图中最大的矩形 Largest Rectangle in Histogram

```py
# 这一题的算法本质上和84题Largest Rectangle in Histogram一样，
# 对每一行都求出每个元素对应的高度，
# 这个高度就是对应的连续1的长度，
# 然后对每一行都更新一次最大矩形面积。
# 本质上是对矩阵中的每行，均依次执行84题算法。
输入：matrix = 
["1","0","1","0","0"],
["1","0","1","1","1"],
["1","1","1","1","1"],
["1","0","0","1","0"]

输出：6




时间复杂度： O(mn)。 对每一列应用柱状图算法需要 O(m) 的时间，一共需要 O(mn) 的时间。

空间复杂度： O(mn)，其中 m 和 n 分别是矩阵的行数和列数。

我们分配了一个与给定矩阵等大的数组，用于存储每个元素的左边连续 1 的数量。




class Solution:
    def maximalRectangle(self, matrix) -> int:
        if len(matrix) == 0:
            return 0
        res = 0
        m, n = len(matrix), len(matrix[0])
        heights = [0] * n 
        # heights = [0] * n，height需要补充一个0
        for i in range(m):
            for j in range(n):
                if matrix[i][j] == '0':
                    heights[j] = 0
                else:
                    heights[j] += 1
            # 每行求一次 self.largestRectangleArea
            heights.append(0)
            res = max(res, self.largestRectangleArea(heights))
        return res

    def largestRectangleArea(self, heights):
        # heights.append(0)
        '''
        stackI 放个 -1
        heights 要append个 0
        '''
        stackI = [-1]
        res = 0
        for i in range(len(heights)):
            # 新来的 heights[i] 更小
            while stackI and heights[i] < heights[stackI[-1]]: # 😐 while 循环 + pop + append
                # 算一下，heights[s] 上一个较大的 hight
                s = stackI.pop()
                res = max(res, heights[s] * ((i - stackI[-1] - 1)))
            stackI.append(i)
        return res

''''
s = stack.pop()后：
✨表示pop
'''

heights: [1, 0, 1, ✨0, 0, 0]
stack: [1]    res: 1 = 1 * ( 3 - 1 - 1)

heights: [2, 0, 2, ✨1, 1, ✨0, 0]
stack: [1]    res: 2 = 2 * ( 3 - 1 - 1)
stack: [1, 3] res: 2 = 1 * ( 5 - 3 - 1)
stack: [1]    res: 3 = 1 * ( 5 - 1 - 1)

heights: [3, 1, 3, ✨2, 2, ✨✨0, 0, 0]
stack: [1]    res: 3 = 3 * ( 3 - 1 -1)
stack: [1, 3] res: 3 = 2 * ( 5 - 3 -1)
stack: [1]    res: 6 = 2 * ( 5 - 1 -1)

heights: [4, 0, 0, 3, ✨0, 0, 0, 0, 0]
stack: [1, 2] res: 4 = 3 * ( 4 - 2 -1)
```



##  206. <a name='LargestRectangleinHistogram-85.'></a>84. 柱状图中最大的矩形 Largest Rectangle in Histogram - 见85. 最大矩形

[官方](https://www.bilibili.com/video/BV16D4y1D7ed?spm_id_from=333.999.0.0)

```py
单调栈


输入：heights = [2,1,5,6,2,3]
输出：10


解释：最大的矩形为图中红色区域，面积为 10




时间复杂度： O(N)。

空间复杂度： O(N)。



class Solution:
    def largestRectangleArea(self, heights: List[int]) -> int:
        stack = [-1]
        heights.append(0) # 最左边插个0，heights最后补充一个0可以很好的简化代码
        n, res = len(heights), 0
        for i in range(n):
            while heights[stack[-1]] > heights[i]: # 😐😐😐 while 循环 + pop + append
                h = heights[stack.pop()]
                w = i - stack[-1] - 1
                res = max(res, h * w)   
     
            stack.append(i)
        return res

判断： 0  >  2


pre的append： [-1, 0]
判断： 2  >  1
pop后： [-1]


pre的append： [-1, 1]
判断： 1  >  5


pre的append： [-1, 1, 2]
判断： 5  >  6


pre的append： [-1, 1, 2, 3]
判断： 6  >  2
pop后： [-1, 1, 2]
pop后： [-1, 1]


pre的append： [-1, 1, 4]
判断： 2  >  3


pre的append： [-1, 1, 4, 5]
判断： 3  >  0
pop后： [-1, 1, 4]
pop后： [-1, 1]
pop后： [-1]


pre的append： [-1, 6]

```

##  226. <a name='NextGreaterElementII'></a>503 【栈】Next Greater Element II

[哈哈哈](https://www.bilibili.com/video/BV197411L77N?spm_id_from=333.999.0.0)

[洛阳](https://www.bilibili.com/video/BV1k5411t7Pa?spm_id_from=333.999.0.0)


```py


注意：不能是 if stack and nums[stack[-1]] < cur:
输入：
[5,4,3,2,1]
输出：
[-1,-1,-1,4,5]



预期结果：
[-1,5,5,5,5]
输入：
[5,4,3,2,1][5,4,3,2,1]
4,3,2,1 存起来，到遇到5的时候，一起pop出来
[-1,5,5,5,5]



时间复杂度:  O(n)，其中 n 是序列的长度。我们需要遍历该数组中每个元素最多 2 次，每个元素出栈与入栈的总次数也不超过 4 次。

空间复杂度:  O(n)，其中 n 是序列的长度。空间复杂度主要取决于栈的大小，栈的大小至多为 2n − 1。

 

class Solution:
    def nextGreaterElements(self, nums: List[int]) -> List[int]:
        res = [-1] * len(nums)
        stackI = []
        # 双倍nums大法好
        for idx, cur in enumerate(nums + nums):
            '''
            长江大浪灭小浪
            '''
            while stackI and nums[stackI[-1]] < cur: # 😐 while 循环 + pop + append
                res[stackI[-1]] = cur
                stackI.pop()
            if idx < len(nums): # 易错点：append(idx)是有条件的
                stackI.append(idx)
        return res

输入: nums = [1,2,3,4,3]
输出: [2,3,4,-1,4]

[-1, -1, -1, -1, -1]
[2, -1, -1, -1, -1]
[2, 3, -1, -1, -1]
[2, 3, 4, -1, -1]
[2, 3, 4, -1, -1]
[2, 3, 4, -1, -1]
[2, 3, 4, -1, -1]
[2, 3, 4, -1, -1]
[2, 3, 4, -1, 4]
[2, 3, 4, -1, 4]
print(stack)
[0]
[1]
[2]
[3]
[3, 4]
[3, 4]
[3, 4]
[3, 4]
[3]
[3]

```









##  37. <a name='ClimbingStairs'></a>70. Climbing Stairs （重要）

https://leetcode-cn.com/problems/climbing-stairs/

[5:32 花花酱 DP](https://www.bilibili.com/video/BV1b34y1d7S8?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1gJ411R7X1?spm_id_from=333.999.0.0)

[哈哈哈 70(重制版)](https://www.bilibili.com/video/BV1G54y197eZ?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Wb411e7s9?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1DZ4y1H7k9?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1ki4y1u7tn?spm_id_from=333.999.0.0)

```py
输入：n = 2
输出：2
解释：有两种方法可以爬到楼顶。
1. 1 阶 + 1 阶
2. 2 阶





输入：n = 3
输出：3
解释：有三种方法可以爬到楼顶。
1. 1 阶 + 1 阶 + 1 阶
2. 1 阶 + 2 阶
3. 2 阶 + 1 阶




# 我的模仿
时间复杂度：循环执行 n 次，每次花费常数的时间代价，故渐进时间复杂度为 O(n)。
空间复杂度：这里只用了常数个变量作为辅助空间，故渐进空间复杂度为 O(1)。

 
class Solution:
    def climbStairs(self, n: int) -> int:
        dp0 = 1
        dp1 = 1
        for _ in range(n - 1):
            dp1, dp0 = dp0 + dp1, dp1 # 用2个数字分别存储
        return dp1
```


##  38. <a name='BinaryTreeMaximumPathSum'></a>124. Binary Tree Maximum Path Sum

[花花酱](https://www.bilibili.com/video/BV1ct411r7qw?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1CT4y1g7bR?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1qT4y1J71C?spm_id_from=333.999.0.0)

```py
输入：
           -10
          /   \
         9    20     
             /  \
            15   7
输出：42


解释：最优路径是 15 -> 20 -> 7 ，路径和为 15 + 20 + 7 = 42



我的思考：
        # 有两种情况：
        # node.val 往上回收, 构成递归
        return max(left, right) + node.val
        # node.val 不往上回收, 左中右
        res = max(left + right + node.val, res)



class Solution:
    def maxPathSum(self, root: Optional[TreeNode]) -> int:
        res = -1e9
        # left = right = 0
        def subsum(node) -> int:
            nonlocal res # 也可以写成 self.res
            if not node: return 0
            # if node.left:
            left = max(subsum(node.left), 0)     # 正负性：left 为负，就不回收
            # if node.right:
            right = max(subsum(node.right), 0)   # 正负性：right 为负，就不回收
            # 有两种情况：node.val 不往上回收, 左中右
            res = max(left + right + node.val, res)
            # 有两种情况：node.val 往上回收, 构成递归
            return max(left, right) + node.val # 正负性：node.val必须回收
        subsum(root)
        return res


时间复杂度： O(N)，其中 N 是二叉树中的节点个数。对每个节点访问不超过 2 次。

空间复杂度： O(N)，其中 N 是二叉树中的节点个数。

空间复杂度主要取决于递归调用层数，最大层数等于二叉树的高度，

最坏情况下，二叉树的高度等于二叉树中的节点个数。

 
```

##  221. <a name='HouseRobberIII'></a>337 House Robber III

[小明](https://www.bilibili.com/video/BV1WD4y1X7JQ?spm_id_from=333.999.0.0)

```py
输入: root = [3,2,3,null,3,null,1]
输出: 7 
解释: 小偷一晚能够盗取的最高金额 3 + 3 + 1 = 7



输入: root = [3,4,5,1,3,null,1]
输出: 9
解释: 小偷一晚能够盗取的最高金额 4 + 5 = 9



# 补充一个Python的：
时间复杂度： O(n)。

空间复杂度： O(n)。



class Solution:
    def rob(self, root: TreeNode) -> int:
        def dfs(root):
            if not root: return 0, 0
            rob_L, no_rob_L = dfs(root.left)  # 前一项表示根节点偷，后一项表示根节点不偷
            rob_R, no_rob_R = dfs(root.right) # 前一项表示根节点偷，后一项表示根节点不偷
            return root.val + no_rob_L + no_rob_R, max(rob_L, no_rob_L) + max(rob_R, no_rob_R) 
            # 前一项表示根节点偷，后一项表示根节点不偷
            # 根节点偷 + (root.left的根节点no偷     +  root.right的根节点no偷) 
            #           max(root.left的根节点all)  +  max(root.right的根节点all)
        return max(dfs(root))

```





##  98. <a name='-1'></a>209-长度最小的子数组

[哈哈哈](https://www.bilibili.com/video/BV1JZ4y1N7Rt?spm_id_from=333.999.0.0)

```py
输入：target = 7, nums = [2,3,1,2,4,3]
输出：2

解释：子数组 [4,3] 是该条件下的长度最小的子数组。




输入：target = 4, nums = [1,4,4]
输出：1




输入：target = 11, nums = [1,1,1,1,1,1,1,1]
输出：0





暴力解法；
时间复杂度：O(n^2) 
空间复杂度：O(1) 


class Solution:
    def minSubArrayLen(self, s: int, nums: List[int]) -> int:
        if not nums:
            return 0
        
        n = len(nums)
        minlen = n + 1
        for i in range(n):
            total = 0
            #  连续子数组, 所以是 i 到 j 累加
            for j in range(i, n):
                total += nums[j]
                if total >= s:
                    minlen = min(minlen, j - i + 1)
                    break
        
        return 0 if minlen == n + 1 else minlen
```


```py

'''
输入：target = 7, nums = [2,3,1,2,4,3]
输出：2
解释：子数组 [4,3] 是该条件下的长度最小的子数组。
'''
       
时间复杂度: O(n log n) ，用二分
空间复杂度: O(1)

class Solution:
    def minSubArrayLen(self, s: int, nums: List[int]) -> int:
        def isWinEnough(size):
            '''
            加上新来的 nums[i], 减去旧的 nums[i - size]
            '''
            sums = 0
            for i in range(len(nums)):
                sums += nums[i]
                # 固定大小的滑动窗口
                if i >= size: sums -= nums[i - size]
                # 然后判断是否满足要求
                if sums >= s: return True
            return False
            
        l, r = 0, len(nums)
        res = 0
        while l <= r: # 😐 while 循环
            mid = (l + r) // 2  # 滑动窗口大小
            if isWinEnough(mid):  # 如果这个大小的窗口可以那么就缩小
                res = mid
                r = mid - 1
            else:  # 否则就增大窗口
                l = mid + 1
        return res

```

##  42. <a name='SqrtxHJ107'></a>69 Sqrt(x) 见 HJ107 求解立方根

[花花酱](https://www.bilibili.com/video/BV1WW411C7YN?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1gJ411R7XR?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Yb411i7TN?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1PK411s72g?spm_id_from=333.999.0.0)

二分查找:

时间复杂度：O(logN)

空间复杂度：O(1)

```py
输入：x = 4
输出：2




输入：x = 8
输出：2

解释：8 的算术平方根是 2.82842..., 由于返回类型是整数，小数部分将被舍去。




class Solution:
    def mySqrt(self, x: int) -> int:
        l, r = 0, x
        ans = -1
        while l <= r: # 😐 while 循环
            mid = (l + r) // 2
            if mid * mid <= x: # 2 * 2 = 4
                ans = mid # ans 必须放置在这个位置
                l = mid + 1
            else:
                r = mid - 1
        return ans
```


# 3 day (得分 = 10分) 55

##  43. <a name='StringtoIntegeratoi'></a>8. String to Integer(atoi)

[小梦想家](https://www.bilibili.com/video/BV1Cb411e7pz?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1AZ4y1s7TD?spm_id_from=333.999.0.0)

* 时间复杂度:O(n)

* 时间复杂度:O(1)

|模式|描述|
|---|---|
|^|匹配字符串的开头|
|[...]|用来表示一组字符,单独列出：[amk] 匹配 'a'，'m'或'k'|
|*|匹配0个或多个的表达式。|
|?|匹配0个或1个由前面的正则表达式定义的片段，非贪婪方式|
|+|匹配1个或多个的表达式。|
|\d|匹配任意数字，等价于 [0-9]。|
|\D|匹配任意非数字，等价于 [^0-9]。|

[正则表达式中小括号、中括号、大括号的作用](https://blog.csdn.net/weixin_45621662/article/details/103921232)

```py
# import re
# class Solution(object):
#     def myAtoi(self, s):
#         return max(min(int(*re.findall('^[+-]?\d+', s.lstrip())), 2**31 - 1), -2**31)

# import re
# class Solution:
#     def myAtoi(self, s: str) -> int:
#         at_oi_re = re.compile('^[ ]*([+-]?\d+)')
#         # 易错点：要注意中括号[]和小括号()的区别
#         # 易错点：要注意小括号()的位置，小括号的作用是匹配并提取，所以+-要包括起来
#         # 易错点：不能漏掉*？

#         # 字符串的 开头 匹配 0个或多个[空格]
#         # 匹配 0个或多个[+-]
#         # 匹配 0个或多个[0-9]
#         if not at_oi_re.search(s):
#             return 0
#         res = int(at_oi_re.findall(s)[0])
#         # 易错点：findall返回一个列表，所以必须有[0]
#         # 易错点：必须有int()
#         #  在范围 [-2^31, 2^31 - 1] 内
#         return min(max(res, -(1<<31)), (1<<31) - 1) # 在两者之间，背一背
#         # 要加小括号(1<<31)


输入：s = "   -42"
输出：-42



解释：

第 1 步："   -42"（读入前导空格，但忽视掉）
            ^
第 2 步："   -42"（读入 '-' 字符，所以结果应该是负数）
             ^
第 3 步："   -42"（读入 "42"）
               ^
解析得到整数 -42 。

由于 "-42" 在范围 [-231, 231 - 1] 内，最终结果为 -42 。




class Solution(object):
    def myAtoi(self, str):
        str = str.strip()
        strNum = 0
        if len(str) == 0:
            return strNum

        flag = 1
        if str[0] == '+' or str[0] == '-':
            if str[0] == '-':
                flag = -1
            str = str[1:]
        
        for char in str:
            if '0' <= char <='9':
                strNum = strNum * 10 +  ord(char) - ord('0')
            else:
                break
        strNum *= flag
        return min(max(strNum, -(1<<31)), (1<<31) - 1) 





时间复杂度： O(n)。我们只需要依次处理所有的字符，处理每个字符需要的时间为 O(1)。

空间复杂度： O(1)。

```





##  48. <a name='MedianofTwoSortedArrays'></a>4. 寻找两个正序数组的中位数 Median of Two Sorted Arrays

[官方](https://www.bilibili.com/video/BV1Xv411z76J?spm_id_from=333.999.0.0)

```py
输入：nums1 = [1,3], nums2 = [2]
输出：2.00000
解释：合并数组 = [1,2,3] ，中位数 2




输入：nums1 = [1,2], nums2 = [3,4]
输出：2.50000
解释：合并数组 = [1,2,3,4] ，中位数 (2 + 3) / 2 = 2.5






给定两个大小分别为 m 和 n 的正序（从小到大）数组 nums1 和 nums2。请你找出并返回这两个正序数组的 中位数 。

算法的时间复杂度应该为 O(log (m+n)) 。



class Solution:
    def findMedianSortedArrays(self, A: List[int], B: List[int]) -> float:
        lenA = len(A)
        lenB = len(B) 
        n = lenA + lenB
        slow, fast = -1, -1
        i, j = 0, 0
        for _ in range(n//2 + 1) :
            slow = fast  
            # 每次循环前将 fast 的值赋给 slow
            # A移动的条件: B遍历到最后 或 当前A<B,满足一个即可
            if j >= lenB or (i < lenA and A[i] < B[j]):
                fast = A[i]
                i += 1
            else :
                fast = B[j]
                j += 1
            
        if (n & 1) == 0: # 与1交,判断奇偶数,更快速
            return (slow + fast) / 2.0
        else:
            return fast

class Solution:
    def findMedianSortedArrays(self, nums1: List[int], nums2: List[int]) -> float:
        if len(nums1) > len(nums2):
            return self.findMedianSortedArrays(nums2, nums1)

        infinty = 2**40
        m, n = len(nums1), len(nums2)
        left, right = 0, m
        # median1：前一部分的最大值
        # median2：后一部分的最小值
        median1, median2 = 0, 0

        while left <= right:
            # 前一部分包含 nums1[0 .. i-1] 和 nums2[0 .. j-1]
            # // 后一部分包含 nums1[i .. m-1] 和 nums2[j .. n-1]
            i = (left + right) // 2
            j = (m + n + 1) // 2 - i

            # nums_im1, nums_i, nums_jm1, nums_j 分别表示 nums1[i-1], nums1[i], nums2[j-1], nums2[j]
            nums_im1 = (-infinty if i == 0 else nums1[i - 1])
            nums_i = (infinty if i == m else nums1[i])
            nums_jm1 = (-infinty if j == 0 else nums2[j - 1])
            nums_j = (infinty if j == n else nums2[j])

            if nums_im1 <= nums_j:
                median1, median2 = max(nums_im1, nums_jm1), min(nums_i, nums_j)
                left = i + 1
            else:
                right = i - 1

        return (median1 + median2) / 2 if (m + n) % 2 == 0 else median1




class Solution:
    def findMedianSortedArrays(self, nums1: List[int], nums2: List[int]) -> float:
        def getKthElement(k):
            """
            - 主要思路：要找到第 k (k>1) 小的元素，那么就取 pivot1 = nums1[k/2-1] 和 pivot2 = nums2[k/2-1] 进行比较
            - 这里的 "/" 表示整除
            - nums1 中小于等于 pivot1 的元素有 nums1[0 .. k/2-2] 共计 k/2-1 个
            - nums2 中小于等于 pivot2 的元素有 nums2[0 .. k/2-2] 共计 k/2-1 个
            - 取 pivot = min(pivot1, pivot2)，两个数组中小于等于 pivot 的元素共计不会超过 (k/2-1) + (k/2-1) <= k-2 个
            - 这样 pivot 本身最大也只能是第 k-1 小的元素
            - 如果 pivot = pivot1，那么 nums1[0 .. k/2-1] 都不可能是第 k 小的元素。把这些元素全部 "删除"，剩下的作为新的 nums1 数组
            - 如果 pivot = pivot2，那么 nums2[0 .. k/2-1] 都不可能是第 k 小的元素。把这些元素全部 "删除"，剩下的作为新的 nums2 数组
            - 由于我们 "删除" 了一些元素（这些元素都比第 k 小的元素要小），因此需要修改 k 的值，减去删除的数的个数
            """
            
            index1, index2 = 0, 0
            while True:
                # 特殊情况
                if index1 == m:
                    return nums2[index2 + k - 1]
                if index2 == n:
                    return nums1[index1 + k - 1]
                if k == 1:
                    return min(nums1[index1], nums2[index2])

                # 正常情况
                newIndex1 = min(index1 + k // 2 - 1, m - 1)
                newIndex2 = min(index2 + k // 2 - 1, n - 1)
                pivot1, pivot2 = nums1[newIndex1], nums2[newIndex2]
                if pivot1 <= pivot2:
                    k -= newIndex1 - index1 + 1
                    index1 = newIndex1 + 1
                else:
                    k -= newIndex2 - index2 + 1
                    index2 = newIndex2 + 1
        
        m, n = len(nums1), len(nums2)
        totalLength = m + n
        if totalLength % 2 == 1:
            return getKthElement((totalLength + 1) // 2)
        else:
            return (getKthElement(totalLength // 2) + getKthElement(totalLength // 2 + 1)) / 2




```



##  166. <a name='ReverseString'></a>344. Reverse String

[小梦想家](https://www.bilibili.com/video/BV1Gx411o7Ha?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1nC4y1a7DR?spm_id_from=333.999.0.0)

[图灵](https://www.bilibili.com/video/BV1nQ4y1R7nH?spm_id_from=333.999.0.0)

```py
输入：s = ["h","e","l","l","o"]
输出：["o","l","l","e","h"]



输入：s = ["H","a","n","n","a","h"]
输出：["h","a","n","n","a","H"]



时间复杂度：O(N)。一共执行了 N/2 次的交换。
空间复杂度：O(1)


class Solution: 
    def reverseString(self, s: List[str]) -> None:
        l, r = 0, len(s) - 1
        while l < r: # 😐 while 循环
            s[l], s[r] = s[r], s[l]
            l += 1
            r -= 1
        return s
```


##  177. <a name='III-1'></a>557. 反转字符串中的单词 III

```py
输入：s = "Let's take LeetCode contest"
输出："s'teL ekat edoCteeL tsetnoc"



class Solution:
    def reverseWords(self, s: str) -> str:
        return ' '.join([i[::-1] for i in s.split(' ')])

一行就是快乐

class Solution:
    def reverseWords(self, s: str) -> str:
        return ' '.join(i[::-1] for i in s.split())



class Solution:
    def reverseWords(self, s: str) -> str:
        strs = s.split(' ')
        n = len(strs)
        for i in range(n):
            # 把 str 转换成 list
            strs[i] =  list(strs[i])
            l = 0
            r = len(strs[i]) - 1
            while l < r: # 😐 while 循环
                strs[i][r], strs[i][l] = strs[i][l], strs[i][r]
                l += 1
                r -= 1
            strs[i] = ''.join(strs[i])
        strs = ' '.join(strs)
        return strs



时间复杂度：O(N)。
空间复杂度：O(N)
```

##  50. <a name='ReverseWordsinaString'></a>151. Reverse Words in a String

[小梦想家](https://www.bilibili.com/video/BV1Yb411i7g4?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1rT4y1g7AJ?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Ei4y1V7yA?spm_id_from=333.999.0.0)

```py
输入：s = "the sky is blue"
输出："blue is sky the"

输入：s = "  hello world  "
输出："world hello"

输入：s = "a good   example"
输出："example good a"



class Solution:
    def reverseWords(self, s: str) -> str:
        return " ".join(reversed(s.split()))
```

```py
时间复杂度：O(N)
空间复杂度：O(N)


class Solution:
    def reverseWords(self, s: str) -> str:
        s = s.strip()
        s = s + ' ' # 根据后面的计算规则，在s后面加个空格
        left, right = 0, len(s) - 1
        que = collections.deque()
        word = []
        for char in s:
            if char == ' ' and word:
                que.appendleft(''.join(word))
                word = []
            elif char != ' ':
                word.append(char)

        return ' '.join(que)
```


##  120. <a name='ReverseInteger'></a>7 Reverse Integer

[哈哈哈](https://www.bilibili.com/video/BV1sE411e73m?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Jb411i7bM?spm_id_from=333.999.0.0)

* 时间复杂度: O(log10(n)), 每次迭代都会除以 10

* 时间复杂度: O(1)

```py
输入：x = 123
输出：321




输入：x = -123
输出：-321




输入：x = 120
输出：21




输入：x = 0
输出：0
 





# 字符串法：
# class Solution:
#     def reverse(self, x: int) -> int:
#         s = str(x)

#         if '-' in s:
#             sn = '-'
#             s = s[1:len(s)]
#         else:
#             sn = ''

#         for i in range(len(s)):
#             sn = sn + s[len(s)-1-i]
#         if int(sn) < -2**31 or int(sn) > 2**31-1:
#             return 0
#         return int(sn)

计算法：
class Solution:
    def reverse(self, x: int) -> int:
        res = 0 
        a = abs(x)

        while a: # 😐😐 while 循环
            '''
            余加除
            '''
            tmp = a % 10
            res = res * 10 + tmp
            a = a // 10
        # 要注意return和while的相对位置
        # 不要写在while循环内部

        if x > 0 and res < 1<<31:
            return res 
        elif x < 0 and res <= 1<<31:
            return -res
        else:
            return 0
```

##  222. <a name='FractiontoRecurringDecimal'></a>166. Fraction to Recurring Decimal

[小梦想家](https://www.bilibili.com/video/BV1Wb411e7PE?spm_id_from=333.999.0.0)

```py
输入：numerator = 1, denominator = 2
输出："0.5"



输入：numerator = 2, denominator = 1
输出："2"



输入：numerator = 4, denominator = 333
输出："0.(012)"





时间复杂度：O(l) 其中 l 是答案字符串的长度
空间复杂度：O(l)


class Solution:
    def fractionToDecimal(self, numerator, denominator):

        # ----------情况一：没有余数----------
        if numerator % denominator == 0:
            return str(numerator // denominator)

        # -----------情况二：有余数-----------
        s = []
        # ----------------得到负数----------------
        if (numerator < 0) != (denominator < 0):
            s.append('-')
        # ----------------得到负数----------------


        # 整数部分
        numerator = abs(numerator)
        denominator = abs(denominator)
        integerPart = numerator // denominator
        s.append(str(integerPart))
        s.append('.')

        # 小数部分
        indexMap = {}
        remainder = numerator % denominator

        while remainder and remainder not in indexMap: # 😐😐😐 while 循环
            indexMap[remainder] = len(s) 
            remainder *= 10
            '''
            余加除
            '''
            s.append(str(remainder // denominator))
            remainder %= denominator

        if remainder:  # 有循环节，跳出循环时，remainde 不是 
            insertIndex = indexMap[remainder]
            s.insert(insertIndex, '(') #左侧插入
            s.append(')')

        return ''.join(s)
        # -----------情况二：有余数-----------
        # -----------情况二：有余数-----------
```



##  51. <a name='MaximumDepthofBinary'></a>104-Maximum Depth of Binary

[哈哈哈](https://www.bilibili.com/video/BV1AJ411Q7xG?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Wb411e7eK?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1u54y1D7Nx?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1tK41137GM?spm_id_from=333.999.0.0)

```py
给定二叉树 [3,9,20,null,null,15,7]，

    3
   / \
  9  20
    /  \
   15   7


返回它的最大深度 3 。





# class Solution:
#     def maxDepth(self, root: Optional[TreeNode]) -> int:
#         return max(self.maxDepth(root.left), self.maxDepth(root.right)) + 1 if root else 0

# class Solution:
#     def maxDepth(self, root: TreeNode) -> int:
#         if not root: return 0
#         return max(self.maxDepth(root.left), self.maxDepth(root.right)) + 1

# 等效于
时间复杂度：O(n) n 为二叉树节点的个数
空间复杂度：O(height)，其中 height 表示二叉树的高度。



递归函数需要栈空间，而栈空间取决于递归的深度，因此空间复杂度等价于二叉树的高度。




class Solution:
    def maxDepth(self, root: Optional[TreeNode]) -> int:
        if root:
            L = self.maxDepth(root.left) + 1 if root.left else 1 # 注意：这里一定要用 if else 结构
            R = self.maxDepth(root.right) + 1 if root.right else 1 # 注意：这里是边的条数
            return max(L, R)
        else:
            return 0

对比


class Solution:
    def diameterOfBinaryTree(self, root: TreeNode) -> int:
        res = 0
        def depth(node):
            nonlocal res
            if node:
                """
                当 node.left, 高度为 1, 否则为 0
                当 node.right, 高度为 1, 否则为 0
                """
                L = depth(node.left) + 1 if node.left else 0 # 注意：这里一定要用 if else 结构
                R = depth(node.right) + 1 if node.right else 0 # 注意：这里是边的条数
                res = max(res, L + R)
                return max(L, R)

        depth(root)
        return res
```


##  185. <a name='MinimumDepthofBinaryTree'></a>111-Minimum Depth of Binary Tree

[哈哈哈](https://www.bilibili.com/video/BV1E7411k7KY?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Wb411e7Vi?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1XZ4y1G7xM?spm_id_from=333.999.0.0)

递归

```py
输入：root = [3,9,20,null,null,15,7]
输出：2




输入：root = [2,null,3,null,4,null,5,null,6]
输出：5




时间复杂度：O(n) n 为二叉树节点的个数
空间复杂度：O(height)，其中 height 表示二叉树的高度。

递归函数需要栈空间，而栈空间取决于递归的深度，因此空间复杂度等价于二叉树的高度。



class Solution:
    def minDepth(self, root: TreeNode) -> int:
        if root:
            L = self.minDepth(root.left) + 1 if root.left else 1 # 注意：这里一定要用 if else 结构
            R = self.minDepth(root.right) + 1 if root.right else 1 # 注意：这里是边的条数
            return L if R == 1 else R if L == 1 else min(L,R)
        else:
            return 0
            
# class Solution:
#     def minDepth(self, root: TreeNode) -> int:
#         if root:
#             if root.left and root.right:
#                 return 1 + min(self.minDepth(root.left), self.minDepth(root.right)) # 如果有2个子树，取较低一层的值
#             elif root.left:
#                 return 1 + self.minDepth(root.left)  # 如果有1个子树，取较高一层的值
#             elif root.right:
#                 return 1 + self.minDepth(root.right) # 如果有1个子树，取较高一层的值
#             else: # not root.left and not root.right
#                 return 1
#         else:
#             return 0
```

层序遍历

```py
时间复杂度： O(N)，其中 N 是树的节点数。

对每个节点访问一次。

空间复杂度： O(N)，其中 N 是树的节点数。

空间复杂度主要取决于队列的开销，队列中的元素个数不会超过树的节点数。


class Solution:
    def minDepth(self, root: TreeNode) -> int:
        if not root:
            return 0

        que = collections.deque([(root, 1)]) # 注意这个写法：[(root, 1)] 的括号
        while que: # 😐 while 循环
            node, depth = que.popleft()
            if not node.left and not node.right: return depth
            if node.left:  que.append((node.left, depth + 1)) # 注意这个写法：(node.left, depth + 1) 的括号
            if node.right: que.append((node.right, depth + 1))
        
```


##  58. <a name='BalancedBinaryTree'></a>110-Balanced Binary Tree

[哈哈哈](https://www.bilibili.com/video/BV1NJ411v7b1?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Wb411e7Lb?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1sV411b7hR?spm_id_from=333.999.0.0)

```py
输入：root = [3,9,20,null,null,15,7]
输出：true




输入：root = [1,2,2,3,3,null,null,4,4]
输出：false




输入：root = []
输出：true






class Solution:
    def isBalanced(self, root: TreeNode) -> bool:
        def maxDepth(root: TreeNode) -> int:
            return max(maxDepth(root.left), maxDepth(root.right)) + 1 if root else 0

        if not root: 
            return True
        return abs(maxDepth(root.left) - maxDepth(root.right)) <= 1 and \
            self.isBalanced(root.left) and self.isBalanced(root.right)
        # 注意：左右两个子树也必须balanced



时间复杂度：O(n^2) 

    最坏情况下，二叉树是满二叉树，需要遍历二叉树中的所有节点，时间复杂度是 O(n)。

    对于节点 p，如果它的高度是 d，则  height(p) 最多会被调用 d 次（即遍历到它的每一个祖先节点时）。

    对于平均的情况，一棵树的高度 h 满足 O(h) = O(logn)，因为 d ≤ h，所以总时间复杂度为 O(nlogn)

    对于最坏的情况，二叉树形成链式结构，高度为 O(n)，此时总时间复杂度为 O(n^2)



空间复杂度：O(n) ，其中 n 是二叉树中的节点个数。

    空间复杂度主要取决于递归调用的层数，递归调用的层数不会超过 n。




class Solution:
    def isBalanced(self, root: TreeNode) -> bool:
        def height(root: TreeNode) -> int:
            if not root:
                return 0
            leftHeight = height(root.left)
            rightHeight = height(root.right)
            if leftHeight == -1 or rightHeight == -1 or abs(leftHeight - rightHeight) > 1:
                return -1
            else:
                return max(leftHeight, rightHeight) + 1

        return height(root) >= 0



时间复杂度： O(n)，其中 n 是二叉树中的节点个数。

使用自底向上的递归，每个节点的计算高度和判断是否平衡都只需要处理一次，

最坏情况下需要遍历二叉树中的所有节点，因此时间复杂度是 O(n)。



空间复杂度： O(n)，其中 n 是二叉树中的节点个数。

空间复杂度主要取决于递归调用的层数，递归调用的层数不会超过 n。


```




##  62. <a name='DiameterofBinaryTree'></a>543 Diameter of Binary Tree

[小明](https://www.bilibili.com/video/BV12K4y1r78T?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1qA411t7LR?spm_id_from=333.999.0.0)

```py
          1
         / \
        2   3
       / \     
      4   5  
返回 3, 它的长度是路径 [4,2,1,3] 或者 [5,2,1,3]。

class Solution:
    def diameterOfBinaryTree(self, root: TreeNode) -> int:
        res = 0
        def depth(node):
            nonlocal res
            if node:
                """
                当 node.left, 高度为 1, 否则为 0
                当 node.right, 高度为 1, 否则为 0
                """
                L = depth(node.left) + 1 if node.left else 0 # 注意：这里一定要用 if else 结构
                R = depth(node.right) + 1 if node.right else 0 # 注意：这里是边的条数
                res = max(res, L + R)
                return max(L, R)

        depth(root)
        return res

时间复杂度： O(N)，其中 N 为二叉树的节点数，即遍历一棵二叉树的时间复杂度，每个结点只被访问一次。

空间复杂度： O(Height)，其中 Height 为二叉树的高度。

```


##  205. <a name='CountCompleteTreeNodes'></a>222. Count Complete Tree Nodes

[花花酱](https://www.bilibili.com/video/BV1n44y1E73D?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Qz411i7bh?spm_id_from=333.999.0.0)

```py
输入：root = [1,2,3,4,5,6]
输出：6

class Solution(object):
    def countNodes(self, root):
        if not root: 
            return 0
        if root and not root.left and not root.right: 
            return 1
        return 1 + self.countNodes(root.left) + self.countNodes(root.right) 
时间复杂度为 O(n) 
```


##  237. <a name='-1'></a>257-二叉树的所有路径

[哈哈哈](https://www.bilibili.com/video/BV1rf4y1X7He?spm_id_from=333.999.0.0)


```py
输入：root = [1,2,3,null,5]
输出：["1->2->5","1->3"]

class Solution:
    def binaryTreePaths(self, root: TreeNode) -> List[str]:
        # 结束条件：
        if not root: return []
        if not root.left and not root.right: return [str(root.val)]
        paths = []
        if root.left:
            # 预先知道 subtree 的答案
            for pt in self.binaryTreePaths(root.left):
                paths.append(str(root.val) + '->' + pt)
        if root.right:
            # 预先知道 subtree 的答案
            for pt in self.binaryTreePaths(root.right):
                paths.append(str(root.val) + '->' + pt)
        return paths  
        
时间复杂度：O(n^2) 

    最坏情况下，二叉树是满二叉树，需要遍历二叉树中的所有节点，时间复杂度是 O(n)。

    对于节点 p，如果它的高度是 d，则  height(p) 最多会被调用 d 次（即遍历到它的每一个祖先节点时）。

    对于平均的情况，一棵树的高度 h 满足 O(h) = O(logn)，因为 d ≤ h，所以总时间复杂度为 O(nlogn)

    对于最坏的情况，二叉树形成链式结构，高度为 O(n)，此时总时间复杂度为 O(n^2)

空间复杂度：O(n) ，其中 n 是二叉树中的节点个数。

    空间复杂度主要取决于递归调用的层数，递归调用的层数不会超过 n。
```


##  52. <a name='-1'></a>76-【滑动窗口🔹】最小覆盖子串

[哈哈哈](https://www.bilibili.com/video/BV1PM4y1K7p6?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1aK4y1t7Qd?spm_id_from=333.999.0.0)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1ud8tslp4vz4.png)

```py
输入：s = "ADOBECODEBANC", t = "ABC"
输出："BANC"

class Solution:
    def minWindow(self, s: str, t: str) -> str:
    
        def isContains(outerdic,innerdic):
            for key in innerdic:
                if outerdic[key] < innerdic[key]:
                    return False # 只要有一个不满足，则不满足
            return True

        inndic = defaultdict(int) # 固定的
        outdic = defaultdict(int) # 变动的
        for char in t:
            inndic[char] += 1 # 固定的

        minlen = len(s)
        l = 0
        res = ''

        for r in range(len(s)): # 扩展右边界
            if s[r] in inndic:
                outdic[s[r]] += 1  # 变动的
            '''
            等到 outdic 够大，才触发计算
            第一步：先保存答案
            第二步：收缩左边界
            '''
            while isContains(outdic, inndic): # 😐😐😐 while 循环
                # 如果是 minWindow
                if r - l + 1 <= minlen:
                    minlen = r - l + 1
                    res = s[l: r + 1]
                # 收缩左边界
                if s[l] in outdic:
                    outdic[s[l]] -= 1  # 变动的
                l += 1   
        return res

时间复杂度：
    最坏情况下左右指针对 s 的每个元素各遍历一遍
    每次检查是否可行会遍历整个 t 的哈希表
    哈希表的大小与字符集的大小有关，设字符集大小为 C, s 和 t 由英文字母组成
    则渐进时间复杂度为 O(52⋅∣s∣+∣t∣)
```


##  53. <a name='NextPermutation'></a>31 ★ Next Permutation

[小明](https://www.bilibili.com/video/BV1Uz4y1m72N?spm_id_from=333.999.0.0)

[图灵](https://www.bilibili.com/video/BV1SK4y1V7ch?spm_id_from=333.999.0.0)

```py
输入：nums = [1,2,3]
输出：[1,3,2]




输入：nums = [3,2,1]
输出：[1,2,3]




输入：nums = [1,1,5]
输出：[1,5,1]





class Solution:
    def nextPermutation(self, nums: List[int]) -> None:
        '''
        关键在于: 从后往前，找到非递减序列
        '''
        stt = len(nums) - 2
        while stt >= 0: # 😐😐 while 循环，找到非递减序列
            if nums[stt] >= nums[stt + 1]:
                stt -= 1
            else:
                # 寻找i后面比i大的数，交换位置,并且排序
                for end in range(len(nums) - 1, stt, -1): # 易错点:len(nums)-1,i的区间
                    # 12(3)5(4)
                    if nums[end] > nums[stt]:
                        nums[stt], nums[end] = nums[end], nums[stt]
                        nums[stt + 1 : ] = sorted(nums[stt + 1 : ])
                        return
        nums.reverse() # 易错点:对于[3,2,1]这种情况，i = 0

时间复杂度： O(N)，其中 N 为给定序列的长度。我们至多只需要扫描两次序列，以及进行一次反转操作。

空间复杂度： O(1)，只需要常数的空间存放若干变量。
 
```


##  184. <a name='NextGreaterElementIII-31NextPermutation'></a>556 Next Greater Element III - 类似 31 ★ Next Permutation

[小明](https://www.bilibili.com/video/BV19t4y167yb?spm_id_from=333.999.0.0)

```py
输入：n = 12
输出：21



输入：n = 21
输出：-1



    # 3 步走：
    # 1. 从后往前，非递减序列的前一个 i
    # 2. 从后往前，比 i 大的 j
    # 3. i 和 j 交换位置
    # 4. [i+1:] 排序

    # 123(4)(5)
    # 12(3)5(4)
    # 124(3)(5)
    # 12(4)(5)3
    # 125(3)(4)
    # 1(2)54(3)

class Solution:
    def nextGreaterElement(self, n: int) -> int:
        nums = list(str(n))
        # 关键在于从后往前，找到非递减序列
        stt = len(nums) - 2
        while stt >= 0: # 😐😐😐 while 循环
            if nums[stt] >= nums[stt + 1]:
                stt -= 1
            else:
                # 寻找i后面比i大的数，交换位置,并且排序
                for end in range(len(nums) - 1, stt, -1): # 易错点:len(nums)-1,i的区间
                    # 12(3)5(4)
                    if nums[end] > nums[stt]:
                        nums[stt], nums[end] = nums[end], nums[stt]
                        nums[stt + 1 : ] = sorted(nums[stt + 1 : ])
                        res =  int(''.join(nums))
                        return res if res < (1<<31) else -1
        return -1 # 易错点:对于[3,2,1]这种情况，i = 0



时间复杂度： O(n)。最坏情况下，只会扫描整个数组两遍，这里 n 是给定数字的位数。

空间复杂度： O(n)。使用了大小为 n 的数组 a，其中 n 是给定数字的位数。
 
```

##  72. <a name='PathSum'></a>112-Path Sum

[哈哈哈](https://www.bilibili.com/video/BV1T7411r7Yr?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1pb411e7r7?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1uK411T7kX?spm_id_from=333.999.0.0)


```py
输入：root = [5,4,8,11,null,13,4,7,2,null,null,null,1], targetSum = 22
输出：true

解释：等于目标和的根节点到叶节点路径如上图所示。





输入：root = [1,2,3], targetSum = 5
输出：false


解释：树中存在两条根节点到叶子节点的路径：
(1 --> 2): 和为 3
(1 --> 3): 和为 4
不存在 sum = 5 的根节点到叶子节点的路径。





dfs
class Solution:
    def hasPathSum(self, root: Optional[TreeNode], targetSum: int) -> bool:
        res = False

        def dfs(node,tsum): # node.val == tsum 结束
            nonlocal res
            if node:
                if node.val == tsum and not node.left and not node.right: # 结束条件
                    res = True
                dfs(node.left,  tsum - node.val) # 三个部分都需要状态转移
                dfs(node.right, tsum - node.val)
            
        dfs(root, targetSum)
        return res


时间复杂度： O(N)，其中 N 是树的节点数。对每个节点访问一次。



空间复杂度： O(H)，其中 H 是树的高度。

空间复杂度主要取决于递归时栈空间的开销，最坏情况下，树呈现链状，空间复杂度为 O(N)。

平均情况下树的高度与节点数的对数正相关，空间复杂度为 O(logN)。


# 递归
# class Solution:
#     def hasPathSum(self, root: TreeNode, targetSum: int) -> bool:
#         if not root: 
#             return False
#         if root.val == targetSum and not root.left and not root.right: 
#             return True
#         return self.hasPathSum(root.left,  targetSum - root.val) or \
#                self.hasPathSum(root.right, targetSum - root.val)
        # 注意：这里用or链接
```

```py
class Solution:
    def pathSum(self, root: Optional[TreeNode], targetSum: int) -> List[List[int]]:
        if not root: 
            return False
        que = collections.deque([(root, targetSum)])
        while que: # 😐 while 循环
            node, tsum = que.popleft()
            if not node.left and not node.right and node.val == tsum:
                return True
            if node.left:  que.append((node.left,  tsum - node.val))
            if node.right: que.append((node.right, tsum - node.val))
        return False

时间复杂度： O(N)，其中 N 是树的节点数。

对每个节点访问一次。



空间复杂度： O(N)，其中 N 是树的节点数。

空间复杂度主要取决于队列的开销，队列中的元素个数不会超过树的节点数。
```

##  59. <a name='-1'></a>113. 二叉树中和为某一值的路径

[哈哈哈](https://www.bilibili.com/video/BV1P54y1i73U?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1k54y177fu?spm_id_from=333.999.0.0)

```py
           5
       /      \
      4        8
    /   \    /   \
  11   null 13    4
 /  \            / \
7    2          5   1


输入： targetSum = 22
输出：[[5,4,11,2],[5,8,4,5]]



class Solution:
    def pathSum(self, root: Optional[TreeNode], targetSum: int) -> List[List[int]]:
        res = []

        def dfs(node, path, tsum): # node.val == tsum 结束
            if node:
                if node.val == tsum and not node.left and not node.right: # 结束条件
                    res.append(path[:] + [node.val])  # 需要深拷贝
                dfs(node.left,  path + [node.val], tsum - node.val) # 三个部分都需要状态转移
                dfs(node.right, path + [node.val], tsum - node.val)
            
        dfs(root, [], targetSum)
        return res



时间复杂度：O(N^2) ，其中 N 是树的节点数。

        在最坏情况下，树的上半部分为链状，下半部分为完全二叉树，

        并且从根节点到每一个叶子节点的路径都符合题目要求。

        此时，路径的数目为 O(N)，并且每一条路径的节点个数也为 O(N)，

        因此要将这些路径全部添加进答案中，时间复杂度为 O(N^2) 。



空间复杂度： O(N)，其中 N 是树的节点数。

        空间复杂度主要取决于栈空间的开销，栈中的元素个数不会超过树的节点数。




class Solution:
    def pathSum(self, root: Optional[TreeNode], targetSum: int) -> List[List[int]]:
        if not root: return []
        res = []
        que = collections.deque([(root, [], targetSum)])
        while que: # 😐 while 循环
            node, path, tsum = que.popleft()
            if not node.left and not node.right and node.val == tsum:
                res.append(path + [node.val])
            if node.left:  que.append((node.left,  path + [node.val], tsum - node.val))
            if node.right: que.append((node.right, path + [node.val], tsum - node.val))
        return res

时间复杂度：O(N^2) ，其中 N 是树的节点数。
空间复杂度： O(N)，其中 N 是树的节点数。
```



##  56. <a name='SumRoottoLeafNumbers'></a>129 Sum Root to Leaf Numbers

[小明](https://www.bilibili.com/video/BV1VK411H7o5?spm_id_from=333.999.0.0)

```py
输入：
       4
     /  \
    9    0
  /  \
 5    1
 
输出：1026

解释：
从根到叶子节点路径 4 -> 9 -> 5 代表数字 495
从根到叶子节点路径 4 -> 9 -> 1 代表数字 491
从根到叶子节点路径 4 -> 0 代表数字 40
因此，数字总和 = 495 + 491 + 40 = 1026




class Solution:
    def sumNumbers(self, root: TreeNode) -> int:
        res = 0
        
        def dfs(root, acc):
            nonlocal res
            if root:
                if not root.left and not root.right: # 结束
                    res += acc * 10 + root.val 
                dfs(root.left,  acc * 10 + root.val)
                dfs(root.right, acc * 10 + root.val)
        dfs(root, 0)
        return res # 在根节点处cur为0，而不是sums




时间复杂度： O(n)，其中 n 是二叉树的节点个数。对每个节点访问一次。

空间复杂度： O(n)，其中 n 是二叉树的节点个数。

空间复杂度主要取决于递归调用的栈空间，递归栈的深度等于二叉树的高度，

最坏情况下，二叉树的高度等于节点个数，空间复杂度为 O(n)。
 
```






##  61. <a name='FirstMissingPositive'></a>41 First Missing Positive

[小明](https://www.bilibili.com/video/BV1fy4y1k7pV?spm_id_from=333.999.0.0)

```py
给你一个未排序的整数数组 nums ，请你找出其中没有出现的最小的正整数。

输入：nums = [1,2,0]
输出：3

输入：nums = [3,4,-1,1]
输出：2

输入：nums = [7,8,9,11,12]
输出：1



置换法：


class Solution:
    def firstMissingPositive(self, nums: List[int]) -> int:
        n = len(nums)

        for i in range(n):
            while 1 <= nums[i] <= n and nums[nums[i] - 1] != nums[i]: # 😐😐😐 while 循环
                nums[nums[i] - 1], nums[i] = nums[i], nums[nums[i] - 1]
# nums[nums[i] - 1]
# [-1, 4, 3, 1] 4 在 4 的位置↓
# [-1, 1, 3, 4] 1 在 1 的位置↓
# [1, -1, 3, 4]

        for i in range(n):
            if nums[i] != i + 1:
                return i + 1

# 输入：[1] 预期结果：2
        return n + 1

时间复杂度： O(N)，其中 N 是数组的长度。

空间复杂度： O(1)。
```


##  175. <a name='MissingNumber'></a>268 【位运算😜】Missing Number [0, n] 中


[小明](https://www.bilibili.com/video/BV1LU4y1p7n7?spm_id_from=333.999.0.0)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1caw225arjj4.webp)

```py
 [0, n] 中 n 个数的数组 nums


输入：nums = [9,6,4,2,3,5,7,0,1]
输出：8


解释：n = 9，因为有 9 个数字，所以所有的数字都在范围 [0,9] 内。8 是丢失的数字，因为它没有出现在 nums 中。


# class Solution:
#     def missingNumber(self, nums: List[int]) -> int:
#         n = len(nums)
#         total = n * (n + 1) // 2
#         arrSum = sum(nums)
#         return total - arrSum
```

```py
你能否实现`线性时间复杂度`、仅使用`额外常数空间`的算法解决此问题 ?

class Solution:
    def missingNumber(self, nums: List[int]) -> int:
        xor = len(nums) # 注意这里
        for i, num in enumerate(nums):
            xor ^= i
            xor ^= num
        return xor

时间复杂度： O(n)，其中 n 是数组  nums 的长度。需要对 2n+1 个数字计算按位异或的结果。

空间复杂度： O(1)。


# class Solution:
#     def missingNumber(self, nums: List[int]) -> int:
#         xor = 0
#         for i, num in enumerate(nums):
#             xor ^= i ^ num
#         return xor ^ len(nums) # 注意这里
```



##  240. <a name='SingleNumberII'></a>137 【位运算😜】Single Number II

[小明](https://www.bilibili.com/video/BV1Hv411B7rd?spm_id_from=333.999.0.0)

方法二：依次确定每一个二进制位

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1ro27dupbn40.webp)

```py
给你一个整数数组 nums ，除某个元素仅出现 `一次` 外，其余每个元素都恰出现 `三次` 。请你找出并返回那个只出现了一次的元素。


输入：nums = [2,2,3,2]
输出：3


输入：nums = [0,1,0,1,0,1,99]
输出：99



细节：

需要注意的是，如果使用的语言对「有符号整数类型」和「无符号整数类型」没有区分，

那么可能会得到错误的答案。

这是因为「有符号整数类型」（即 int 类型）的第 31 个二进制位（即最高位）是补码意义下的符号位，对应着 -2^{31}

而「无符号整数类型」由于没有符号，第 31 个二进制位对应着 2^{31}

因此在某些语言（例如 Python ）中需要对最高位进行特殊判断。

时间复杂度：O(nlogC)，其中 n 是数组的长度，C 是元素的数据范围

空间复杂度：O(1)

class Solution:
    def singleNumber(self, nums: List[int]) -> int:
        ans = 0
        for i in range(32):
            total = sum((num >> i) & 1 for num in nums)
            if total % 3:
                # Python 这里对于最高位需要特殊判断
                if i == 31:
                    ans -= (1 << i)
                else:
                    ans |= (1 << i)
        return ans


```

##  250. <a name='III-1'></a>260-【位运算😜】只出现一次的数字 III

[哈哈哈](https://www.bilibili.com/video/BV15Z4y1H7Sw?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1QK411J7dN?spm_id_from=333.999.0.0)

难点在于只出现一次的数字不止一个，

但是刚好有且只有两个

```py
输入：nums = [1,2,1,3,2,5]
输出：[3,5]

解释：[5, 3] 也是有效的答案。




输入：nums = [-1,0]
输出：[-1,0]



输入：nums = [0,1]
输出：[1,0]





class Solution:
    def singleNumber(self, nums: List[int]) -> List[int]:
        freq = Counter(nums)
        return [num for num, occ in freq.items() if occ == 1]

```

```py

时间复杂度：O(n)，其中 n 是数组 nums 的长度。

空间复杂度：O(1)。

class Solution:
    def singleNumber(self, nums: List[int]) -> List[int]:
        xorsum = 0
        # 先全部异或一次, 得到的结果 # 找到这两个数的差异
        for num in nums:
            xorsum ^= num 
        # 找到这两个数的差异的最后一位1, 在这个位上一个为0, 一个为1
        diff = xorsum & (-xorsum) 
        type1 = type2 = 0
        for num in nums:
            # 由此可以将数组中的元素分成两部分,重新遍历, 求两个异或值
            if num & diff: 
                type1 ^= num
            else:
                type2 ^= num

        return [type1, type2]

```

##  257. <a name='PowerofTwo'></a>231. 【位运算😜】Power of Two

[小梦想家](https://www.bilibili.com/video/BV1Yb411H73f?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1rV411r7AL?spm_id_from=333.999.0.0)

```py
输入：n = 1
输出：true
解释：20 = 1




输入：n = 16
输出：true
解释：24 = 16




输入：n = 3
输出：false




输入：n = 4
输出：true




输入：n = 5
输出：false






时间复杂度： O(1)。

空间复杂度： O(1)。


class Solution:
    def isPowerOfTwo(self, n: int) -> bool:
        return n > 0 and (n & (n - 1)) == 0
```



# 4 day (得分 = 8分) 63


##  100. <a name='-1'></a>958. 二叉树的完全性检验

```py
输入：root = [1,2,3,4,5,6]
输出：true

解释：最后一层前的每一层都是满的（即，结点值为 {1} 和 {2,3} 的两层），

且最后一层中的所有结点（{4,5,6}）都尽可能地向左。





输入：root = [1,2,3,4,5,null,7]
输出：false

解释：值为 7 的结点没有尽可能靠向左侧。






2 * v 和 2 * v + 1
         1
        / \
       2   3
      / \ / \
     4  5 6  7

2 * v + 1 和 2 * v + 2
         0
        / \
       1   2
      / \ / \
     3  4 5  6



class Solution(object):
    def isCompleteTree(self, root):
        alltreepos = [(root, 1)]
        i = 0
        # 在一个 完全二叉树 中，除了最后一个关卡外，所有关卡都是完全被填满的
        while i < len(alltreepos): # 😐😐😐 while 循环, i 从 0 开始
            node, v = alltreepos[i]
            i += 1
            if node:
                alltreepos.append((node.left,  2 * v))
                alltreepos.append((node.right, 2 * v + 1))

        return alltreepos[-1][1] == len(alltreepos)



时间复杂度： O(N)，其中 N 是树节点个数。
空间复杂度： O(N)。
```



##  106. <a name='MaximumWidthofBinar'></a>662. Maximum Width of Binary Tree

[花花酱](https://www.bilibili.com/video/BV1cv411q7pb?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV16a4y1h7fG?spm_id_from=333.999.0.0)

```py
           1
         /   \
        3     2
       / \     \  
      5   3     9 

输出: 4
解释: 最大值出现在树的第 3 层，宽度为 4 (5,3,null,9)。




输入: 

          1
         /  
        3    
       / \       
      5   3     

输出: 2
解释: 最大值出现在树的第 3 层，宽度为 2 (5,3)。




输入: 

          1
         / \
        3   2 
       /        
      5      

输出: 2
解释: 最大值出现在树的第 2 层，宽度为 2 (3,2)。



输入: 

          1
         / \
        3   2
       /     \  
      5       9 
     /         \
    6           7
输出: 8
解释: 最大值出现在树的第 4 层，宽度为 8 (6,null,null,null,null,null,null,7)。






层序遍历

时间复杂度：  O(N) ，其中 N 是树中节点的数目，我们需要遍历每个节点。

空间复杂度：  O(N) ，这部分空间是因为我们 DFS 递归过程中有 N 层的栈。

 

class Solution:
    def widthOfBinaryTree(self, root: TreeNode) -> int:
        res = 0
        queue = collections.deque([(root, 1)])
        while queue: # 😐 while 循环
            res = max(res, queue[-1][1] - queue[0][1] + 1) # 只能写在这里！否则不存在
            for _ in range(len(queue)):
                node, pos = queue.popleft()
                if node.left:  queue.append((node.left,  pos * 2))
                if node.right: queue.append((node.right, pos * 2 + 1))
        return res 

其他写法

class Solution:
    def widthOfBinaryTree(self, root: TreeNode) -> int:
        res = 0
        queue = collections.deque([(root, 0)])
        while queue: # 😐 while 循环
            res = max(res, queue[-1][1] - queue[0][1] + 1) # 只能写在这里！否则不存在
            for _ in range(len(queue)):
                node, pos = queue.popleft()
                if node.left:  queue.append((node.left,  pos * 2 + 1))
                if node.right: queue.append((node.right, pos * 2 + 2))
        return res
```

##  65. <a name='ImplementRand10UsingRand7'></a>470. Implement Rand10() Using Rand7()

[花花酱](https://www.bilibili.com/video/BV1Ut411Z7KX?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1AD4y1m7Qb?spm_id_from=333.999.0.0)

```py
输入: 1
输出: [2]




输入: 2
输出: [2,8]




输入: 3
输出: [3,8,10]



"""
给定方法 rand7 可生成 [1,7] 范围内的均匀随机整数
试写一个方法 rand10 生成 [1,10] 范围内的均匀随机整数。
"""


时间复杂度：期望时间复杂度为 O(1) ，但最坏情况下会达到 O(∞)（一直被拒绝）。

空间复杂度：O(1) 。



class Solution:
    def rand10(self) -> int:
        while True: # 😐😐😐 while 循环
            row = rand7()
            col = rand7()
            idx = (row - 1) * 7 + col #（0-42） + （1-7）
            if idx <= 40: # 1-40
                return 1 + idx % 10



这样写也是对的，因为 0-9 等概率出现


class Solution:
    def rand10(self):
        while True: # 😐😐😐 while 循环
            row = rand7()
            col = rand7()
            idx = (row - 1) * 7 + col #（0-42） + （1-7）
            if idx <= 30: # 1-30
                return 1 + idx % 10
```

##  66. <a name='Symmetrictree'></a>101-Symmetric tree

[哈哈哈](https://www.bilibili.com/video/BV1VJ41197KD?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Wb411e7eb?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1xv41167z8?spm_id_from=333.999.0.0)

> Python 迭代：其实就是层序遍历，然后检查每一层是不是回文🌈数组

```py
输入：root = [1,2,2,3,4,4,3]
输出：true
示例 2：


输入：root = [1,2,2,null,3,null,3]
输出：false





class Solution:
    def isSymmetric(self, root: TreeNode) -> bool:
        dic = collections.defaultdict(list)

        def bfs(node, level):
            if node:
                dic[level].append(node.val)
                bfs(node.left, level + 1)
                bfs(node.right, level + 1)
            else:
                dic[level].append(None)
        
        bfs(root, 0) 
        for key in dic:
            if dic[key] != dic[key][::-1]:
                return False

        return True

时间复杂度：这里遍历了这棵树，渐进时间复杂度为 O(n)。
空间复杂度：这里的空间复杂度和递归使用的栈空间有关，这里递归层数不超过 n，故渐进空间复杂度为 O(n)。



class Solution:
    def isSymmetric(self, root: TreeNode) -> bool:
        # if not root:
        #     return [] 删除
        que = collections.deque([root])
        while que: # 😐 while 循环
            vals = [] # 补充
            for _ in range(len(que)):
                node = que.popleft()
                if node: # 修改，因为none节点也需要append
                    que.append(node.left) # if n.left 被删除
                    que.append(node.right) #  if n.right 被删除
                    vals.append(node.val)  # 补充
                else:
                    vals.append(None) # 修改，因为none节点也需要append
            if vals != vals[::-1]:  # 补充
                return False  # 补充
        return True

时间复杂度：  O(n)。
空间复杂度：  O(n)。
```

> Python 递归：

```py
class Solution:
    def isSymmetric(self, root: TreeNode) -> bool:
        def twoSym(node1, node2):
            if  node1 and node2 and \
                node1.val == node2.val and \
                twoSym(node1.left, node2.right) and twoSym(node1.right, node2.left): 
                return True
            elif not node1 and not node2:
                return True
            else:
                return False
        return twoSym(root.left, root.right)
时间复杂度：  O(n)。
空间复杂度：  O(n)。
```


##  69. <a name='MinimumPathSum64-'></a>64. Minimum Path Sum 64-最小路径和

[花花酱](https://www.bilibili.com/video/BV12W411679S?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1Ka4y1i7Vu?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1JC4y1x7j1?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1vi4y1u7a6?spm_id_from=333.999.0.0)

```py
输入：grid = [[1,3,1],[1,5,1],[4,2,1]]
输出：7

解释：因为路径 1→3→1→1→1 的总和最小。




输入：grid = [[1,2,3],[4,5,6]]
输出：12





# 可以直接在原数组上进行记忆，不需要额外的空间
# so easy,直接AC
class Solution:
    def minPathSum(self, grid: List[List[int]]) -> int:
        for i in range(len(grid)):
            for j in range(len(grid[0])):
                if i == j == 0:
                    continue
                if i == 0:
                    grid[i][j] += grid[i][j-1]
                if j == 0:
                    grid[i][j] += grid[i-1][j]
                if i > 0 and j > 0:
                    grid[i][j] += min(grid[i-1][j],grid[i][j-1])
        return grid[-1][-1]
```




##  75. <a name='dfsCoinChange-518CoinChange'></a>322. 【动态🚀规划 + 背包 + dfs】Coin Change - 见 518 Coin Change

https://leetcode-cn.com/problems/coin-change/

[花花酱](https://www.bilibili.com/video/BV1SW411C7d1?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1tz4y1d7XM?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1ty4y187dh?spm_id_from=333.999.0.0)

```py
输入：coins = [1, 2, 5], amount = 11
输出：3 
解释：11 = 5 + 5 + 1



动态🚀规划
'''
求硬币数量
'''
import functools
class Solution:
    def coinChange(self, coins, amount):
        dp = [10e9] * (amount + 1)
        dp[0] = 0

        for coin in coins:
            for tar in range(coin, amount + 1):
                # i 就是一个 target, 求出每个 target 的最小值
                dp[tar] = min(dp[tar], dp[tar-coin] + 1)
         # 这道题的难点在于：最后结果的输出
        return dp[-1] if dp[-1] != 10e9 else -1



时间复杂度： O(Sn)，其中 S 是金额，n 是面额数
空间复杂度： O(S)。数组 dp 需要开长度为总金额 S 的空间。



```



##  236. <a name='PerfectSquares'></a>279 【动态🚀规划 + 背包】Perfect Squares

[小明](https://www.bilibili.com/video/BV1r5411Y7MH?spm_id_from=333.999.0.0)

```py
'''
求硬币数量
'''
输入：n = 12
输出：3 
解释：12 = 4 + 4 + 4

输入：n = 13
输出：2
解释：13 = 4 + 9




# 动态🚀规划
        '''版本一，先遍历背包, 再遍历物品'''
        '''版本二， 先遍历物品, 再遍历背包'''


class Solution:
    def numSquares(self, n: int) -> int:
        dp = [10e5] * (n + 1)
        dp[0] = 0
        # 也可以 sqrt = floor(n**0.5) 
        sqrt = ceil(n ** 0.5) 
        for i in range(sqrt + 1): # 易错点：必须要 sqrt+1 比如输入：n = 1
            coin = i ** 2
            for tar in range(coin, n + 1):
                dp[tar] = min(dp[tar], dp[tar - coin] + 1) 
        return dp[-1]





时间复杂度：O(n sqrt{n}) ，其中 n 为给定的正整数。

状态转移方程的时间复杂度为 O(sqrt{n}) ，共需要计算 n 个状态，因此总时间复杂度为 O(n sqrt{n}) 

空间复杂度： O(n)。我们需要 O(n) 的空间保存状态。

。
```

##  116. <a name='CoinChange2-322.dfsCoinChange'></a>518 Coin Change 2 - 见 322. 【动态🚀规划 + 背包 + dfs】Coin Change

https://leetcode-cn.com/problems/coin-change-2/

[小明](https://www.bilibili.com/video/BV1jC4y1a7YT?spm_id_from=333.999.0.0)

```py
'''
求种类
'''
输入：amount = 5, coins = [1, 2, 5]
输出：4



解释：有四种方式可以凑成总金额：
5=5
5=2+2+1
5=2+1+1+1
5=1+1+1+1+1




时间复杂度： O(amount × n) 
空间复杂度： O(amount) 




class Solution:
    def change(self, amount: int, coins: List[int]) -> int:
        # dp[i]代表金额为i的时候能凑成总金额的硬币组合数量
        dp = [0] * (amount + 1)
        dp[0] = 1
        for coin in coins:
            # 假如只有1个硬币，假如有2个硬币，假如有3个硬币~ ~ ~
            for tar in range(coin, amount + 1):
               dp[tar] += dp[tar - coin]
        return dp[-1]
```




##  133. <a name='WordBreak'></a>139 【动态🚀规划 + 背包】Word Break

[小明](https://www.bilibili.com/video/BV1p54y1k7vf?spm_id_from=333.999.0.0)

```py
输入: s = "leetcode", wordDict = ["leet", "code"]
输出: true
解释: 返回 true 因为 "leetcode" 可以由 "leet" 和 "code" 拼接成。




输入: s = "applepenapple", wordDict = ["apple", "pen"]
输出: true
解释: 返回 true 因为 "applepenapple" 可以由 "apple" "pen" "apple" 拼接成。
     注意，你可以重复使用字典中的单词。





输入: s = "catsandog", wordDict = ["cats", "dog", "sand", "and", "cat"]
输出: false





# python 动态🚀规划

# 从 i = 0 开始分析：i = 0， 遍历 j in range(1, n+1)， 

# 即遍历所有以 s[0] 开头的组合，把第一个单词可能的情况全部找出来。

# 此时相当于把打头的单词可能的情况全部找出来了。

# 然后基于第一个单词一个单词一个单词地接上去。




class Solution:
    def wordBreak(self, s, wordDict):
        n = len(s) 
        dp = [True] + [False]*n

        for end in range(1, n + 1):
            for stt in range(end):
                # 前提是 start 为 true, end 才为 true
                if dp[stt] and s[stt: end] in wordDict:
                    dp[end] = True # 说明 s[: i] 在 wordDict 中
                    break # 优化部分：剩下的切分点 j 不用再寻找了，也可以不写，像下方一样
        return dp[-1]





时间复杂度：O(n^2)

我们一共有 O(n) 个状态需要计算，每次计算需要枚举 O(n) 个分割点，

哈希表判断一个字符串是否出现在给定的字符串列表需要 O(1) 的时间，因此总时间复杂度为 O(n^2) 




空间复杂度：O(n) 

```


##  193. <a name='dfsstartIforTargetSum'></a>494. 【动态🚀规划 + 背包 + dfs(startI) 无 for 循环】Target Sum

[花花酱](https://www.bilibili.com/video/BV1WW411C7Mp?spm_id_from=333.999.0.0)

[花花酱 下](https://www.bilibili.com/video/BV1WW411C7Mr?spm_id_from=333.999.0.0)

0 - 1 背包（二维动态规划）

```py
输入：nums = [1,1,1,1,1], target = 3
输出：5

解释：一共有 5 种方法让最终目标和为 3 。
-1 + 1 + 1 + 1 + 1 = 3
+1 - 1 + 1 + 1 + 1 = 3
+1 + 1 - 1 + 1 + 1 = 3
+1 + 1 + 1 - 1 + 1 = 3
+1 + 1 + 1 + 1 - 1 = 3





输入：nums = [1], target = 1
输出：1





'''
求种类
'''

时间复杂度： O(n × (sum − target))

    sum 是数组 nums 的元素和
    动态规划有 (n+1)×( (sum−target)/2 + 1 ) 个状态，需要计算每个状态的值

空间复杂度： O(sum−target)

    需要创建长度为 (sum−target)/2 + 1 的数组




class Solution:
    def findTargetSumWays(self, nums: List[int], target) -> int:
        # 求得新的目标
        # 注意，需要排除掉一些特殊状况
        # 也可以写成：bagSize = sums - target
        n = len(nums)
        sums = sum(nums)
        bagSize = sums + target

        if bagSize % 2 == 1 or bagSize < 0:
            return 0
        bagSize = bagSize // 2
        dp = [[0] * (bagSize + 1) for _ in range(n + 1)]  # 构建dp，numLen在外围
        dp[0][0] = 1 # 赋值，dp的第一个元素

        for i in range(1, n + 1):
            num = nums[i - 1] # 易错点: num = nums[i-1]单独提出来写，不容易出错
            for tar in range(bagSize + 1):
                if tar - num >= 0: # 易错点: 这里必需要是>=
                    dp[i][tar] = dp[i - 1][tar] + dp[i - 1][tar - num]
                else:
                    dp[i][tar] = dp[i - 1][tar]
        return dp[-1][-1]



#    [[1, 0, 0, 0, 0], 
#     [1, 1, 0, 0, 0], 
#     [1, 2, 1, 0, 0], 
#     [1, 3, 3, 1, 0], 
#     [1, 4, 6, 4, 1], 
#     [1, 5, 10, 10, 5]]
```

0-1背包（一维动态规划）

```py
'''
求种类，每个coin只能用1次
'''


class Solution:
    def findTargetSumWays(self, nums: List[int], target) -> int:
        n = len(nums)
        # 求得新的目标
        sums = sum(nums)
        # 注意，需要排除掉一些特殊状况
        bagSize = sums + target
        # 也可以写成：bagSize = sums - target
        if bagSize % 2 == 1 or bagSize < 0:
            return 0
        bagSize = bagSize // 2
        # 构建dp，numLen在外围
        dp = [0] * (bagSize+1)
        # 赋值，dp的第一个元素
        dp[0] = 1
        for coin in nums:
            for tar in range(bagSize, coin - 1, -1):
                dp[tar] += dp[tar - coin] # 对于没有当前num时的case + 有了num时bagSize-num的cas
        # 含义就是：
        # 对于1个num，bagsize的填满情况
        # 对弈2个num，bagsize的填满情况
        return dp[-1]



数字： 1 dp: [1, 0, 0, 0, 0]
数字： 1 dp: [1, 0, 0, 0, 0]
数字： 1 dp: [1, 0, 0, 0, 0]
数字： 1 dp: [1, 1, 0, 0, 0]
--------------------
数字： 1 dp: [1, 1, 0, 0, 0]
数字： 1 dp: [1, 1, 0, 0, 0]
数字： 1 dp: [1, 1, 1, 0, 0]
数字： 1 dp: [1, 2, 1, 0, 0]
--------------------
数字： 1 dp: [1, 2, 1, 0, 0]
数字： 1 dp: [1, 2, 1, 1, 0]
数字： 1 dp: [1, 2, 3, 1, 0]
数字： 1 dp: [1, 3, 3, 1, 0]
--------------------
数字： 1 dp: [1, 3, 3, 1, 1]
数字： 1 dp: [1, 3, 3, 4, 1]
数字： 1 dp: [1, 3, 6, 4, 1]
数字： 1 dp: [1, 4, 6, 4, 1]
--------------------
数字： 1 dp: [1, 4, 6, 4, 5]
数字： 1 dp: [1, 4, 6, 10, 5]
数字： 1 dp: [1, 4, 10, 10, 5]
数字： 1 dp: [1, 5, 10, 10, 5]
--------------------



# class Solution:
#     def findTargetSumWays(self, nums: List[int], target) -> int:
#         n = len(nums)
#         sums = sum(nums)
#         bagSize = sums + target
#         if bagSize % 2 == 1 or bagSize < 0:
#             return 0
#         bagSize = bagSize // 2

#         @cache
#         def dfs(startI,total):
#             # 如果要写递归，那么这个递归的结束条件一定要背出来
#             if startI == len(nums):
#                 return 1 if total == 0 else 0
#             # 如果要写递归，那么这个递归的结束条件一定要背出来
#             return dfs(startI + 1, total - nums[startI]) + dfs(startI + 1, total)

#         return dfs(0, bagSize)

```

```py
# 思路

# 每个数，只有取正、取负数，这两种处理。因此可以做深度优先遍历，为了避免重复计算，加上记忆法。

# DFS 到 nums[i] 时，取正数，则要求后面的数，处理后的 next_target = current_target - nums[i];
# 取负数的处理同上，后续要处理的 next_target = current_target + nums[i]；
# 用 python 语法糖简单演示下：

# class Solution:
#     def findTargetSumWays(self, nums: List[int], target) -> int:
#         # @lru_cache(None)
#         def dfs(startIdx, sum) -> int:
#             if startIdx == len(nums):
#                 return 1 if sum == 0 else 0
#             return dfs(startIdx + 1, sum - nums[startIdx]) + dfs(startIdx + 1, sum + nums[startIdx])

#         return dfs(0, target)

# @lru_cache(None) 好神奇，瞬间提高效率
# 如果不用@lru_cache(None)，用这种方式会超时，Java的就不会啊
# 同样的解法，Java 不超时是因为 Java 运行速度比 Python 快得多。
# 而 Python 如果不用 lru_cache 或者显式的记忆数组存储 dfs 的结果，
# 大量重复计算会导致超时，因为 Python 运行速度慢



# DFS
输入：nums = [1,1,1,1,1], target = 3
输出：5
解释：一共有 5 种方法让最终目标和为 3 。
-1 + 1 + 1 + 1 + 1 = 3
+1 - 1 + 1 + 1 + 1 = 3
+1 + 1 - 1 + 1 + 1 = 3
+1 + 1 + 1 - 1 + 1 = 3
+1 + 1 + 1 + 1 - 1 = 3



class Solution:
    def findTargetSumWays(self, nums: List[int], target) -> int:
        def dfs(startIdx, sum):
            if startIdx == len(nums):
                return 1 if sum == 0 else 0
            res = 0
            res += dfs(startIdx + 1, sum - nums[startIdx])
            res += dfs(startIdx + 1, sum + nums[startIdx])
            return res
        return dfs(0, target)

时间复杂度：O(2^n) 回溯需要遍历所有不同的表达式，共有 2^n 种不同的表达式，

每种表达式计算结果需要 O(1) 的时间 

空间复杂度： O(n) 。空间复杂度主要取决于递归调用的栈空间，栈的深度不超过 n。
 
```



##  215. <a name='SplitArrayLargestSum'></a>410. Split Array Largest Sum

[花花酱](https://www.bilibili.com/video/BV14W411d7D4?spm_id_from=333.999.0.0)

```py
输入：nums = [7,2,5,10,8], m = 2
输出：18



答案在 max(nums) 和 sum(nums) 之间，也就是在 10 ~ 32 之间



class Solution:
    def splitArray(self, nums: List[int], bagnum: int) -> int:
        def check(bagsize: int) -> bool:
            tmpsums, bagcnt = 0, 1
            for num in nums:
                # 如果超出了背包的尺寸，则 bagcnt += 1
                if tmpsums + num > bagsize: 
                    bagcnt += 1
                    tmpsums = num   # 清空
                else:
                    tmpsums += num  # 累加
            return bagcnt <= bagnum


        left = max(nums)  # 当 划分个数为 len(nums)
        right = sum(nums) # 当 划分个数为 1
        while left <= right: # 😐 while 循环
            mid = (left + right) // 2
            if check(mid): # 检查划分个数够不够
                res = mid
                right = mid - 1
            else:
                left = mid + 1

        return res



时间复杂度： O(n × log(sum−maxn))，

        其中 sum 表示数组 nums 中所有元素的和， maxn 表示数组所有元素的最大值。

        每次二分查找时，需要对数组进行一次遍历，时间复杂度为 O(n)，

空间复杂度： O(1)。

 
```


##  265. <a name='dfsstartIforPartitionEqualSubsetSum'></a>416. 【动态🚀规划 + 背包 + dfs(startI)无for循环】Partition Equal Subset Sum

####  265.1. <a name='494'></a>相似题目：494题

[花花酱](https://www.bilibili.com/video/BV1AW411y7So?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1DD4y1X7Cp?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1oZ4y1G7QY?spm_id_from=333.999.0.0)

“动态🚀规划” 的解法

```py

根据494题修改的动态规划：


输入：nums = [1,5,11,5]
输出：true



解释：数组可以分割成 [1, 5, 5] 和 [11] 。


'''
求数量，每个coin只能用1次
'''
class Solution:
    def canPartition(self, nums: List[int]) -> bool:
        n = len(nums)
        
        sums = sum(nums) 
        if sums % 2 == 1: return False # 注意，需要排除掉一些特殊状况
        bagSize = sums // 2 # 求得新的目标
        
        dp = [0] * (bagSize+1) # 构建dp，numLen在外围
        dp[0] = 1 # 赋值，dp的第一个元素
        
        for coin in nums:
            for tar in range(bagSize, coin - 1, -1):
                dp[tar] += dp[tar - coin] # 对于没有当前num时的case + 有了num时bagSize-num的cas
        # 含义就是：
        # 对于1个num，bagsize的填满情况
        # 对于2个num，bagsize的填满情况
        return dp[-1] != 0




时间复杂度： O(n × target)，其中 n 是数组的长度， target 是整个数组的元素和的一半。

需要计算出所有的状态，每个状态在进行转移时的时间复杂度为 O(1)。

空间复杂度： O(target)，其中 target 是整个数组的元素和的一半。

空间复杂度取决于 dp 数组，在不进行空间优化的情况下，空间复杂度是 O(n × target)，

在进行空间优化的情况下，空间复杂度可以降到 O(target)。

```

##  77. <a name='MajorityElement'></a>169. 【位运算😜】Majority Element

[花花酱](https://www.bilibili.com/video/BV1hb411c7bF?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Yb411H7pW?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Ff4y1U7Vn?spm_id_from=333.999.0.0)


```py
输入：nums = [3,2,3]
输出：3





输入：nums = [2,2,1,1,1,2,2]
输出：2





class Solution:
    def majorityElement(self, nums: List[int]) -> int:
        return sorted(nums)[len(nums) // 2]

class Solution:
    def majorityElement(self, nums: List[int]) -> int:
        nums.sort()
        return nums[len(nums) // 2]


class Solution:
    def majorityElement(self, nums: List[int]) -> int:
        counts = collections.Counter(nums)
        return max(counts.keys(), key=counts.get)

# 投票策略，半数以上获胜

class Solution:
    def majorityElement(self, nums: List[int]) -> int:
        count = 0
        candidate = None

        for vot in nums:
            if count == 0:
                candidate = vot
            count += (1 if vot == candidate else -1)

        return candidate




时间复杂度： O(N)
空间复杂度： O(1)

```



##  79. <a name='-1'></a>226-翻转二叉树

[哈哈哈](https://www.bilibili.com/video/BV1Sh411R7B2?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Yb411H73E?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1FK411p7Co?spm_id_from=333.999.0.0)

```py
输入：root = [4,2,7,1,3,6,9]
输出：[4,7,2,9,6,3,1]





输入：root = [2,1,3]
输出：[2,3,1]




输入：root = []
输出：[]





时间复杂度：O(N)

空间复杂度：

在平均情况下，二叉树的高度与节点个数为对数关系，即 O(logN)。

而在最坏情况下，树形成链状，空间复杂度为  O(N)。

class Solution:
    def invertTree(self, root: TreeNode) -> TreeNode:
        if not root: return root
        # 先翻转
        left = self.invertTree(root.left)
        right = self.invertTree(root.right)
        # 再交换
        root.left, root.right = right, left
        return root
```

```py
class Solution:
    def invertTree(self, root: TreeNode) -> TreeNode:
        if not root: return root
        que = [root]
        while que: # 😐 while 循环
            node = que.pop()
            if node.left or node.right:
                if node.left: que.append(node.left)
                if node.right: que.append(node.right)
                node.left, node.right = node.right, node.left
        return root
```

##  80. <a name='CompareVersionNumbers'></a>165. Compare Version Numbers

[小梦想家](https://www.bilibili.com/video/BV19K4y1C7L3?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Pk4y117dF?spm_id_from=333.999.0.0)

```py
输入：version1 = "1.01", version2 = "1.001"
输出：0

解释：忽略前导零，"01" 和 "001" 都表示相同的整数 "1"




输入：version1 = "1.0", version2 = "1.0.0"
输出：0

解释：version1 没有指定下标为 2 的修订号，即视为 "0"




输入：version1 = "0.1", version2 = "1.1"
输出：-1

解释：version1 中下标为 0 的修订号是 "0"，
version2 中下标为 0 的修订号是 "1" 。
0 < 1，所以 version1 < version2
 




时间复杂度： O(n+m)（或 O(max(n,m))，这是等价的），

其中 n 是字符串 version1 的长度，m 是字符串 version2 的长度。

空间复杂度： O(n+m)，我们需要 O(n+m) 的空间存储分割后的修订号列表。
 




class Solution:
    def compareVersion(self, version1: str, version2: str) -> int:
        v1 = version1.split(".")
        v2 = version2.split(".")

        while v1 or v2: # 😐😐 while 循环
            x = int(v1.pop(0)) if v1 else 0
            y = int(v2.pop(0)) if v2 else 0

            if x > y:
                return 1
            elif x < y:
                return -1
        return 0
```

##  81. <a name='offer53'></a>34-在排序数组中查找元素的第一个 - 类似剑指offer53

https://leetcode-cn.com/problems/zai-pai-xu-shu-zu-zhong-cha-zhao-shu-zi-lcof/

[哈哈哈](https://www.bilibili.com/video/BV1Zv411y71t?spm_id_from=333.999.0.0)

[图灵](https://www.bilibili.com/video/BV1GU4y1j7dq?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1ef4y1v7Vz?spm_id_from=333.999.0.0)

```py
输入：nums = [5,7,7,8,8,10], target = 8
输出：[3,4]



输入：nums = [5,7,7,8,8,10], target = 6
输出：[-1,-1]



输入：nums = [], target = 0
输出：[-1,-1]





# Python 二分法
时间复杂度：  O(logn) ，其中 n 为数组的长度。 一共会执行两次，因此总时间复杂度为 O(logn)。

空间复杂度： O(1) 。只需要常数空间存放若干变量。
 


class Solution:
    def searchRange(self, nums, target):
        left = 0
        right = len(nums)-1
        res = [0,0]
        if target not in nums:
            return [-1,-1]

        # 寻找左侧边界
        while left <= right: # 😐 while 循环
            mid = (right + left) // 2
            if nums[mid] >= target:
                right = mid - 1 # 结束条件, 因为保留 left，所以移动 right
            elif nums[mid] < target:
                left = mid + 1
        res[0] = left

        # 寻找右侧边界
        right = len(nums)-1 # 只移动 right 端点
        while left <= right: # 😐 while 循环
            mid = (right + left) // 2
            if nums[mid] <= target:
                left = mid + 1 # 结束条件, 因为保留 right，所以移动 left
            elif nums[mid] > target:
                right = mid - 1
        res[1] = right

        return res
```



 # 5 day (得分 = 6分) 69


##  87. <a name='FindPeakElement'></a>162. Find Peak Element

[小梦想家](https://www.bilibili.com/video/BV1Rb411n7dT?spm_id_from=333.999.0.0)

```py

输入：nums = [1,2,1,3,5,6,4]
输出：1 或 5 

解释：你的函数可以返回索引 1，其峰值元素为 2；
     或者返回索引 5， 其峰值元素为 6。


时间复杂度： O(logn)，其中 n 是数组 nums 的长度。
空间复杂度： O(1)。


class Solution:
    def findPeakElement(self, nums: List[int]) -> int:
        l, r = 0, len(nums) - 1 
        res = 0
        while l < r: # 😐😐 while 循环
            mid = (l + r) // 2
            '''
            找到最大值，不能写成 while l <= r
            '''
            if nums[mid] < nums[mid + 1]:
                res =  mid + 1
                l = mid + 1
            else:
                r = mid
        return res
# [1,2,1,3,5,6,4]
# 3 < 5, 向右移动，left 指向 5，right 指向 4，想较大值方向移动
# 6 > 4, 向左移动，left 指向 5，right 指向 6 
# 5 < 6, 向右移动，left 指向 6，right 指向 6 
```

##  180. <a name='FindinMountainArray'></a>1095. Find in Mountain Array

[花花酱](https://www.bilibili.com/video/BV1m5411V7x7?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1GK4115778?spm_id_from=333.999.0.0)

```py
输入：array = [1,2,3,4,5,3,1], target = 3
输出：2

解释：3 在数组中出现了两次，下标分别为 2 和 5，我们返回最小的下标 2。




输入：array = [0,1,2,4,2,1], target = 3
输出：-1

解释：3 在数组中没有出现，返回 -1。





注意：这里用
MountainArray.get(k) - 会返回数组中索引为k 的元素（下标从 0 开始）
MountainArray.length() - 会返回该数组的长度

"""
This is MountainArray's API interface.
You should not implement it, or speculate about its implementation
"""
class MountainArray:
   def get(self, index: int) -> int:
   def length(self) -> int:

class Solution:
    def findInMountainArray(self, target: int, mountain_arr: 'MountainArray') -> int:
        l, r = 0, mountain_arr.length() - 1
        '''
        找到山峰
        '''
        while l < r: # 😐😐 while 循环
            mid = (l + r) // 2
            if mountain_arr.get(mid) < mountain_arr.get(mid + 1):
                l = mid + 1
                peak = l
            else:
                r = mid
        
        '''
        找到左侧
        '''
        l, r = 0, peak
        while l <= r: # 😐 while 循环
            mid = (l + r) // 2
            cur = mountain_arr.get(mid)
            if cur == target:
                return mid
            elif cur < target:
                l = mid + 1
            else:
                r = mid - 1
        '''
        找到右侧
        '''
        l, r = peak + 1, mountain_arr.length() - 1
        while l <= r: # 😐 while 循环
            mid = (l + r) // 2
            cur = mountain_arr.get(mid)
            if cur == target:
                return mid
            elif cur > target:
                l = mid + 1
            else:
                r = mid - 1

        return -1

时间复杂度： O(logn)，我们进行了三次二分搜索，每次的时间复杂度都为 O(logn)。

空间复杂度： O(1)，只需要常数的空间存放若干变量。

 
```

##  82. <a name='-1'></a>153-寻找旋转排序数组中的最小值

[哈哈哈](https://www.bilibili.com/video/BV1bT4y1w7yK?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1yK411L7rp?spm_id_from=333.999.0.0)

```py
输入：nums = [3,4,5,1,2]
输出：1
解释：原数组为 [1,2,3,4,5] ，旋转 3 次得到输入数组。



输入：nums = [4,5,6,7,0,1,2]
输出：0
解释：原数组为 [0,1,2,4,5,6,7] ，旋转 4 次得到输入数组。




输入：nums = [11,13,15,17]
输出：11
解释：原数组为 [11,13,15,17] ，旋转 4 次得到输入数组。
 




时间复杂度： O(logn)，其中 n 是数组 nums 的长度。

在二分查找的过程中，每一步会忽略一半的区间，因此时间复杂度为  O(logn)。

空间复杂度： O(1)。



class Solution:
    def findMin(self, nums):
        l, r = 0, len(nums) - 1
        # [4,5,6,7,0,1,2]
        """
        用 mid 和 right 比较
        而不是用 mid 和 target 比较
        """

        while l <= r: # 😐 while 循环
            mid = (r + l) // 2       
            if  nums[mid] == nums[r]:    # 此时 left 和 right 相等，直接返回
                return nums[r]
            elif nums[mid] < nums[r]:   # 比右界小，nums[mid] 可能是最小值，不能去掉
                r = mid                 # 比如 [5,6,7,0,1,2,4]
            else:                           # 比右界大，nums[mid] 肯定不会是最小值     
                l = mid + 1              # 比如 [4,5,6,7,0,1,2]
```



##  86. <a name='SingleNumber'></a>136 【位运算😜】Single Number

[哈哈哈](https://www.bilibili.com/video/BV1g7411a7bf?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1Sp4y1D7M3?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Qb411e7PU?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1pa4y1t7tr?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1iC4y1a7Hz?spm_id_from=333.999.0.0)

```py
输入: [2,2,1]
输出: 1



输入: [4,1,2,1,2]
输出: 4



时间复杂度： O(n)，其中 n 是数组长度。只需要对数组遍历一次。
空间复杂度： O(1)。

class Solution:
    def singleNumber(self, nums):
        a = 0
        for num in nums:
            a = a ^ num
        return a

class Solution:
    def singleNumber(self, nums: List[int]) -> int:
        return reduce(lambda x, y: x ^ y, nums)
```



##  88. <a name='Searcha2DMatrix'></a>240. 二维数组的查找 - 74 Search a 2D Matrix

[哈哈哈](https://www.bilibili.com/video/BV1dz411i7jC?spm_id_from=333.999.0.0)


```py
输入：matrix = 
[
    [ 1, 4, 7,11,15],
    [ 2, 5, 8,12,19],
    [ 3, 6, 9,16,22],
    [10,13,14,17,24],
    [18,21,23,26,30]], 

target = 5
输出：true

若目标小了就左移。若目标大了就下移！
若目标小了就左移。若目标大了就下移！
若目标小了就左移。若目标大了就下移！



时间复杂度：O(m + n)
空间复杂度：O(1)
class Solution:
    def searchMatrix(self, matrix, target):
        # 从右上角开始找
        i, j = 0, len(matrix[0]) - 1
        while i <= len(matrix) - 1 and j >= 0: # 😐😐😐 while 循环
            if matrix[i][j] == target: 
                return True
            elif target < matrix[i][j]: 
                j -= 1 # 若目标小了就左移
            elif target > matrix[i][j]: 
                i += 1 # 目标大了就下移
        return False
```


##  90. <a name='Longestcommonprefix'></a>14 - Longest common prefix

[哈哈哈](https://www.bilibili.com/video/BV1cJ411D7qU?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Eb411i7QN?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1tV411k7GY?spm_id_from=333.999.0.0)

* 时间复杂度: O(mn), m 是字符串最短长度, n 是字符串数量

* 时间复杂度: O(1)

```py
输入：strs = [
    "flower",
    "flow",
    "flight"
    ]
输出："fl"

class Solution:
    def longestCommonPrefix(self, strs: List[str]) -> str:
        tmpset = set()
        res = ''
        i = 0
        minlen = min(len(string) for string in strs)
        while i < minlen: # 😐😐😐 while 循环
            tmpset = set(string[i] for string in strs)
            if len(tmpset) == 1:
                res += tmpset.pop() # set集合中用pop弹出数据
                i += 1
            else:
                break
        return res
```


##  91. <a name='LargestNumber'></a>179 Largest Number

[小明](https://www.bilibili.com/video/BV1mV411m7aN?spm_id_from=333.999.0.0)

```py
# from functools import cmp_to_key
# class Solution(object):
#     def largestNumber(self, nums):
#         # 第一步：定义比较函数，把最大的放左边
#         # 第二步：排序
#         # 第三步：返回结果
#         def compare(a, b):
#             return int(b + a) - int(a + b)
#         nums = sorted([str(x) for x in nums], key = cmp_to_key(compare))
#         # nums = sorted(map(str, nums), key = cmp_to_key(compare))
#         return str(int(''.join(nums)))
```

```py
输入：nums = [3,30,34,5,9]
输出："9534330"

时间复杂度： O(nlogn + n^2)
空间复杂度： O(logn)，排序需要  O(logn) 的栈空间。
class Solution:
    def largestNumber(self, nums: List[int]) -> str:
        # 按照字典序由大到小排序
        nums = sorted([str(num) for num in nums], reverse = True)
        for stt in range(len(nums) - 1):
            for end in range(stt, len(nums)):
        # [3,30,34,5,9] 的 3,30 不能按照字典序排序，需要交换位置
                if str(nums[stt]) + str(nums[end]) < str(nums[end]) + str(nums[stt]):
                    nums[stt], nums[end] = nums[end], nums[stt]
        return str(int(''.join(nums)))
```







##  96. <a name='DecodeString'></a>394 Decode String

[小明](https://www.bilibili.com/video/BV145411V75E?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1GZ4y1p7pE?spm_id_from=333.999.0.0)

```py
示例 1：

输入：s = "3[a]2[bc]"
输出："aaabcbc"


示例 2：

输入：s = "3[a2[c]]"
输出："accaccacc"

本题核心思路：是在`栈`里面每次存储两个信息, 

(左括号前的`字符串`, 左括号前的`数字`)

时间复杂度: O(S)
空间复杂度: O(S)

class Solution:
    def decodeString(self, s: str) -> str:
        stack = []  
        tmpstr, num = "", 0 
        for char in s:
            if char.isdigit():
                num = num * 10 + int(char) # 3
            elif char == "[":
                stack.append((tmpstr, num)) # 比如abc3[def], 当遇到第一个 "[" 的时候，压入栈中的是("abc", 3)
                '''
                遇到左括号，abc，3，都要被清空
                '''
                tmpstr, num = "", 0
            elif char == "]":
                pre, cnt = stack.pop() # 然后遍历括号里面的字符串def, 当遇到 "]" 的时候, 从栈里面弹出一个元素(s1, n1)
                tmpstr = pre + tmpstr * cnt # 得到新的字符串为 abc + def * 3
            else:
                tmpstr += char # abc def
        return tmpstr

```

##  97. <a name='HouseRobber198-'></a>198. 【动态🚀规划】House Robber 198-打家劫舍

[12:45 花花酱 DP](https://www.bilibili.com/video/BV1b34y1d7S8?spm_id_from=333.999.0.0)

[花花酱](https://www.bilibili.com/video/BV1tW411676f?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1u64y1M7PA?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Yb411H7hu?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1gZ4y1N75c?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV18g4y1i7f9?spm_id_from=333.999.0.0)

```py
示例 1：

输入：[1,2,3,1]
输出：4
解释：偷窃 1 号房屋 (金额 = 1) ，然后偷窃 3 号房屋 (金额 = 3)。
     偷窃到的最高金额 = 1 + 3 = 4 。

示例 2：

输入：[2,7,9,3,1]
输出：12
解释：偷窃 1 号房屋 (金额 = 2), 偷窃 3 号房屋 (金额 = 9)，接着偷窃 5 号房屋 (金额 = 1)。
     偷窃到的最高金额 = 2 + 9 + 1 = 12 。



时间复杂度： O(n)，其中 n 是数组长度。只需要对数组遍历一次。
空间复杂度： O(1)

动态🚀规划，典型例题：
class Solution:
    def rob(self, nums: List[int]) -> int:
        dp0 = 0
        dp1 = 0
        for num in nums:
            dp0, dp1 = dp1, max(dp0 + num, dp1) # (隔一个的和+当前, 前一个的和)
        return dp1
```



##  155. <a name='HouseRobberII213-II'></a>213.【动态🚀规划】 House Robber II 213-打家劫舍II

[18:13 花花酱 DP](https://www.bilibili.com/video/BV1b34y1d7S8?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1Ea4y147oh?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1GD4y1d7DS?spm_id_from=333.999.0.0)

```py
输入：nums = [2,3,2]
输出：3
解释：你不能先偷窃 1 号房屋（金额 = 2），然后偷窃 3 号房屋（金额 = 2）, 因为他们是相邻的。


输入：nums = [1,2,3,1]
输出：4
解释：你可以先偷窃 1 号房屋（金额 = 1），然后偷窃 3 号房屋（金额 = 3）。
     偷窃到的最高金额 = 1 + 3 = 4 。


输入：nums = [1,2,3]
输出：3
 

房屋都 围成一圈 

# dp[i] = max(dp[i-2] + nums[i], dp[i-1])
# class Solution:
#     def rob(self, nums: List[int]) -> int:
#         n = len(nums)
#         # 易错点：注意特殊情况
#         if n <= 2:
#             return max(nums)

#         dp1 = [0] * (n - 1)
#         dp1[0] = nums[0]
#         dp1[1] = max(nums[1], nums[0])
#         for i in range(2, n - 1):
#             dp1[i] = max(dp1[i-2] + nums[i], dp1[i-1])

#         dp2 = [0] * (n - 1)
#         dp2[0] = nums[1]
#         dp2[1] = max(nums[1], nums[2])
#         # 易错点：注意，根据递推式，这里不是，dp2[1] = nums[2]
#         for i in range(2, n - 1):
#             dp2[i] = max(dp2[i-2] + nums[i + 1], dp2[i-1])

#         return max(dp1[-1],dp2[-1])
时间复杂度： O(n)，其中 n 是数组长度。只需要对数组遍历2次。
空间复杂度： O(1)
降维以后：

class Solution:
    def rob(self, nums: List[int]) -> int:
        n = len(nums)
        # 易错点：注意特殊情况
        if n <= 2:
            return max(nums)

        def robrange(start,end):
            dp0 = 0
            dp1 = 0
            for num in nums[start:end]:
                dp1, dp0 = max(dp0 + num, dp1), dp1
            return dp1

        return max(robrange(0, n - 1), robrange(1, n))
```


##  99. <a name='BasicCalculatorII-224.'></a>227 Basic Calculator II - 见 224. 基本计算器

[小明](https://www.bilibili.com/video/BV1Qy4y167Ax?spm_id_from=333.999.0.0)

https://www.bilibili.com/video/BV1t4411c7m6?from=search&seid=14354850983862729610&spm_id_from=333.337.0.0

https://www.bilibili.com/video/BV1v54y1a74b?from=search&seid=14354850983862729610&spm_id_from=333.337.0.0

```py
输入：s = "3+2*2"
输出：7

输入：s = " 3/2 "
输出：1

输入：s = " 3+5 / 2 "
输出：5

时间复杂度：O(n) 
空间复杂度：O(n) 

class Solution:
    def calculate(self, s: str) -> int:
        stack = []
        num, op = 0, "+"  # 这个"+", 在最前面,是因为算法符号具有滞后性
        for i, char in enumerate(s):
            if char.isdigit():
                num = 10 * num + int(char)
            if char in "+-*/" or i == len(s)-1:
                if op == "+":
                    stack.append(num)
                elif op == "-":
                    stack.append(-num)
                elif op == "*":
                    stack.append(stack.pop()*num)
                elif op == "/":
                    stack.append(int(stack.pop()/float(num)))
                num, op = 0, char # op 的赋值放在最后面, 是因为算法符号具有滞后性
        return sum(stack)

```

```py
# 本题不含括号和符号位，所以将 '/' 替换为 '//' 就可以直接调用 eval 了。
class Solution(object):
    def calculate(self, s):
        return eval(s.replace('/', '//'))
```



##  117. <a name='BasicCalculatorII'></a>224. 基本计算器 - 见 227 Basic Calculator II 两道题完全不同

https://www.bilibili.com/video/BV1Nb4y1z7hG?from=search&seid=1882841343164929357&spm_id_from=333.337.0.0

<img src="https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.71qtf391s5w0.png" width="40%">

```py
输入：s = "1 + 1"
输出：2



输入：s = " 2-1 + 2 "
输出：3



输入：s = "(1+(4+5+2)-3)+(6+8)"
输出：23





时间复杂度：O(n) 
空间复杂度：O(n) 
class Solution:
    def calculate(self, s: str) -> int:
        stack = [1]
        sign = 1
        i = 0
        res = 0
        while i < len(s): # 😐😐😐 while 循环, i 从 0 开始
            if s[i].isdigit():
                num = 0
                while i < len(s) and s[i].isdigit(): # 😐😐😐 while 循环, 是否存在连续 num
                    num = 10 * num + int(s[i])
                    i += 1
                res += sign * num
            else:
                if s[i] == '+':   sign = stack[-1]
                elif s[i] == '-': sign = -stack[-1]
                # -(1-(4+5+2)-3) + (6+8)
                # stack[-1] 是因为负负得正
                elif s[i] == '(': stack.append(sign)
                elif s[i] == ')': stack.pop()
                i += 1
        return res
```



# 6 day (得分 = 5分) 74




##  103. <a name='Offer54.k-230KthSmallestElementinaB'></a>剑指 Offer 54. 二叉搜索树的第k大节点 - 230 Kth Smallest Element in a B

```py
输入: root = [3,1,4,null,2], k = 1
   3
  / \
 1   4
  \
   2
输出: 4

输入: root = [5,3,6,2,4,null,null,1], k = 3
       5
      / \
     3   6
    / \
   2   4
  /
 1
输出: 4



class Solution:
    def kthLargest(self, root: TreeNode, k: int) -> int:
        def inorder(root):
            if root:
                inorder(root.right)
                self.k -= 1
                if self.k == 0: 
                    self.res = root.val
                    return 注意，这里return的是【空】
                inorder(root.left)

        self.k = k
        inorder(root)
        return self.res
```



##  107. <a name='SerializeandDeserializeBinaryTree'></a>297. Serialize and Deserialize Binary Tree

[花花酱](https://www.bilibili.com/video/BV1Hb411c7cr?spm_id_from=333.999.0.0)

```py
时间复杂度：

        序列化时做了一次遍历，渐进时间复杂度为 O(n)。

        反序列化时，在解析字符串的时候 ptr 指针对字符串做了一次顺序遍历，字符串长度为 O(n)，

        故这里的渐进时间复杂度为 O(n)。

空间复杂度：

        考虑递归使用的栈空间的大小，这里栈空间的使用和递归深度有关，

        递归深度又和二叉树的深度有关，在最差情况下，二叉树退化成一条链，

        故这里的渐进空间复杂度为 O(n)。


class Codec:
    def serialize(self, root):
        # 前序遍历
        def tree2str(node):
            return str(node.val) + ',' + tree2str(node.left) + tree2str(node.right) if node else ','
        return tree2str(root)

    def deserialize(self, data):
        # 前序遍历[::-1]后就能直接pop()
        vals = data.split(',')[::-1]
        def str2tree():
            val = vals.pop()
            return TreeNode(val, str2tree(), str2tree()) if val else None
        return str2tree()

```

##  108. <a name='-1'></a>152 【动态🚀规划】乘积最大子数组

[哈哈哈](https://www.bilibili.com/video/BV12a4y1i76G?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1iK411K7yG?spm_id_from=333.999.0.0)

maxdp * num, mindp * num, num

```py
输入: nums = [2,3,-2,4]
输出: 6
解释: 子数组 [2,3] 有最大乘积 6。

输入: nums = [-2,0,-1]
输出: 0
解释: 结果不能为 2, 因为 [-2,-1] 不是子数组。




# 动态🚀规划：
# 遍历时，每次分别存储 前i个中连续数组 [最大的乘积和] 和 [最小乘积和]
时间复杂度为 O(n)
空间复杂度为 O(1)

class Solution:
    def maxProduct(self, nums: List[int]) -> int:
        if not nums: return 
        res = nums[0]
        maxdp = nums[0]
        mindp = nums[0]
        for num in nums[1:]:
            maxdp = max(maxdp * num, mindp * num, num)
            mindp = min(maxdp * num, mindp * num, num)
            res = max(res, maxdp)
        return res

# 之所有存 [最小乘积和]，是因为 [最小乘积和] 如果是负数有可能遇到负数，

# 相乘后结果更大 Python
```



##  110. <a name='Offer10-I.'></a>剑指 Offer 10- I. 斐波那契数列

```py
输入：n = 2
输出：1

输入：n = 5
输出：5
# from functools import lru_cache
# class Solution:
#     @lru_cache(None)
#     def fib(self, n: int) -> int:
#         if n < 2:
#             return n
#         return (self.fib(n - 1) + self.fib(n - 2)) % 1000000007
#         # 1 1 2 3 5
class Solution:
    def fib(self, n: int) -> int:
        a, b = 0, 1
        '''
        遍历范围是 2 ~ n，遍历 n - 1 次
        '''
        for i in range(n): 
            a, b = b, a + b # py的单行更新值
    	return a


class Solution:
    def fib(self, n: int) -> int:
        MOD = 10 ** 9 + 7
        if n < 2:
            return n
        dp0, dp1 = 0, 1
        '''
        遍历范围是 2 ~ n，遍历 n - 1 次
        '''
        for _ in range(2, n + 1): # 注意：这里的边界
            dp1, dp0 = (dp1 + dp0) % MOD, dp1
        return dp1

时间复杂度： O(n)。
空间复杂度： O(1)
```



##  157. <a name='FibonacciNumber'></a>509. 【动态🚀规划 + 递归】Fibonacci Number

[1:30 花花酱 DP](https://www.bilibili.com/video/BV1b34y1d7S8?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV15y4y147Re?spm_id_from=333.999.0.0)

```py

输入：n = 2
输出：1

输入：n = 3
输出：2

输入：n = 4
输出：3




    
class Solution:
    @cache
    def fib(self, n: int) -> int:
        if n < 2:
            return n
        return self.fib(n - 1) + self.fib(n - 2)

# 记忆化递归
class Solution:
    def fib(self, n: int) -> int:
        dic = {}
        def helper(n):
            # 这个写法是错误的：if n == 0 or 1:
            if n < 2:
                return n
            elif n in dic:
                return dic[n]
            else:
                dic[n] = helper(n - 1) + helper(n - 2)
                return dic[n]
        return helper(n)
```



##  113. <a name='OddEvenLinkedList'></a>328. 奇偶链表 (Odd Even Linked List)

[洛阳](https://www.bilibili.com/video/BV1v64y1u7AH?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1ag4y1B78z?spm_id_from=333.999.0.0)

```py

输入: head = [1,2,3,4,5]
输出: [1,3,5,2,4]


输入: head = [2,1,3,5,6,4,7]
输出: [2,3,6,7,1,5,4]


时间复杂度： O(n)。
空间复杂度： O(1)



class Solution(object):
    def oddEvenList(self, head):
        if not head or not head.next:      
            return head
        # odd 和 even 都是移动指针
        # evenHead 是固定的
    
        slow  = head
        fast = headnxt = head.next
        # 当 2 和 3 存在
        while fast and fast.next: # 😐😐 while 循环
            # 1 -> 2的后面
            slow.next = fast.next
            # 1 变成 3
            slow = slow.next
            # 2 -> 3的后面
            fast.next = slow.next 
            # 2 变成 4
            fast = fast.next
        slow.next = headnxt # 先奇数，后偶数
        return head 
```


# 7 day (得分 = 4分) 78



##  119. <a name='23.'></a>补充题23. 检测循环依赖

```s
现有n个编译项，编号为 0 ~ n-1。给定一个二维数组，

表示编译项之间有依赖关系。如[0, 1]表示1依赖于0。

若存在循环依赖则返回空；不存在依赖则返回可行的编译顺序。
```

```py
目标：是让 graph 里面，全部进入 que，最后进入 res
class Solution:
    def haveCircularDependency(self, n: int, prerequisites):
        graph = [[] for _ in range(n)] # 邻接表存储图结构
        indegree = [0 for _ in range(n)] # 每个点的入度
        # 将依赖关系加入邻接表中g，并各个点入度
        for pre in prerequisites:
            stt, end = pre[0], pre[1]
            graph[stt].append(end)
            indegree[end] += 1
            
        # 一次性将入度为0的点全部入队
        que0 = deque()
        for i in range(n):
            if indegree[i] == 0:
                que0.append(i)

        res = []
        '''
        indegree 和 que0 是变动的量
        '''
        while que0: # 😐 while 循环
            stt = que0.popleft()
            res.append(stt)
            # 删除边时，将终点的入度-1。若入度为0，果断入队
            for end in graph[stt]:
                indegree[end] -= 1
                if indegree[end] == 0:
                    que0.append(end)
        # 若存在循环依赖则返回空；不存在依赖则返回可行的编译顺序。
        return res if len(res) == n else []
```


##  148. <a name='-1'></a>207-课程表

[花花酱](https://www.bilibili.com/video/BV1Ut411a74a?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV19k4y1r76s?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1jz411B7UJ?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1Xp4y1Y7FJ?spm_id_from=333.999.0.0)


```py
时间复杂度:  O(n+m)，其中 n 为课程数，m 为先修课程的要求数。

这其实就是对图进行广度优先搜索的时间复杂度。

空间复杂度: O(n+m)。

题目中是以列表形式给出的先修课程关系，为了对图进行广度优先搜索，我们需要存储成邻接表的形式，空间复杂度为 O(n+m)。

在广度优先搜索的过程中，我们需要最多 O(n) 的队列空间（迭代）进行广度优先搜索。





输入：
numCourses = 2, 
prerequisites = [[1,0],[0,1]]
end, stt

输出：false



解释：总共有 2 门课程。学习课程 1 之前，你需要先完成​课程 0 ；并且学习课程 0 之前，你还应先完成课程 1 。这是不可能的。

# python
from collections import defaultdict 
class Solution:
    def canFinish(self, numCourses, prerequisites):
        indegree = defaultdict(lambda:0)  
        graph = defaultdict(list)         
        for end, stt in prerequisites:
            graph[stt].append(end)
            indegree[end] += 1
        que0 = []                  
        for i in range(numCourses):
            if indegree[i] == 0:
                que0.append(i)    
        for stt in que0:
            for end in graph[stt]:
                indegree[end] -= 1
                if indegree[end] == 0: 
                    que0.append(end)
        return len(que0) == numCourses
```


##  182. <a name='CourseScheduleII210-II'></a>210. Course Schedule II 210-课程表II

[花花酱](https://www.bilibili.com/video/BV1gW411y7Kb?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1Ja4y147on?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1qt4y1X7oC?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1kK411W7rL?spm_id_from=333.999.0.0)

```py
时间复杂度: O(n+m)，其中 n 为课程数，m 为先修课程的要求数。

这其实就是对图进行广度优先搜索的时间复杂度。

空间复杂度: O(n+m)。

题目中是以列表形式给出的先修课程关系，为了对图进行广度优先搜索，我们需要存储成邻接表的形式，空间复杂度为 O(n+m)。

在广度优先搜索的过程中，我们需要最多 O(n) 的队列空间（迭代）进行广度优先搜索。





输入：
numCourses = 4, 
prerequisites = [[1,0],[2,0],[3,1],[3,2]]

输出：[0,2,1,3]

解释：总共有 4 门课程。
要学习课程 3，你应该先完成课程 1 和课程 2。
并且课程 1 和课程 2 都应该排在课程 0 之后。
因此，一个正确的课程顺序是 [0,1,2,3] 。另一个正确的排序是 [0,2,1,3] 。


class Solution:
    def findOrder(self, numCourses: int, prerequisites: List[List[int]]) -> List[int]:
        indegre = [0] * numCourses
        graph = [[] for _ in range(numCourses)]
        for end, stt in prerequisites:
            indegre[end] += 1
            graph[stt].append(end)
        que0 = []                  
        for i in range(numCourses):
            if indegre[i] == 0:
                que0.append(i)    
        for stt in que0:
            for end in graph[stt]:
                indegre[end] -= 1
                if indegre[end] == 0: 
                    que0.append(end)
        return len(que0) == numCourses and que0 or []
```




##  220. <a name='AllNodesDistanceKinBinaryTree'></a>863. All Nodes Distance K in Binary Tree

[花花酱](https://www.bilibili.com/video/BV14W411d7mz?spm_id_from=333.999.0.0)

```py
输入：root = [3,5,1,6,2,0,8,null,null,7,4], target = 5, k = 2
输出：[7,4,1]
解释：所求结点为与目标结点（值为 5）距离为 2 的结点，值分别为 7，4，以及 1


这道题就是先把二叉树转化图,再用图的bfs,求得解

所以,这道题关键就是如何把树转化成图,不难直接看代码就可以理解.


class Solution:
    def distanceK(self, root: TreeNode, target: TreeNode, k: int) -> List[int]:
        from collections import defaultdict
        graph = defaultdict(set)
        # 建图
        def dfs(root):
            if root.left :
                graph[root.val].add(root.left.val)
                graph[root.left.val].add(root.val)
                dfs(root.left)
            if root.right:
                graph[root.val].add(root.right.val)
                graph[root.right.val].add(root.val)
                dfs(root.right)
        dfs(root)
        # 3: {1, 5}, 
        # 5: {2, 3, 6}, 
        # 6: {5}, 
        # 2: {4, 5, 7}, 
        # 7: {2}, 
        # 4: {2}, 
        # 1: {0, 8, 3}, 
        # 0: {1}, 
        # 8: {1}
        que = [target.val]
        visited = {target.val} # 必须用set
        while k: # 😐 while 循环
            nextnode = [] # 每一次都是临近的节点
            while que: # 😐 while 循环
                stt = que.pop()
                for end in graph[stt]:
                    if end not in visited:
                        visited.add(end)
                        nextnode.append(end) # 每一次都是临近的节点
            k -= 1 
            que = nextnode # 每一次都是临近的节点
        return que

时间复杂度： O(n)，其中 n 是二叉树的结点个数。

需要执行两次深度优先搜索，每次的时间复杂度均为 O(n)。

空间复杂度： O(n)。记录父节点需要 O(n) 的空间，深度优先搜索需要 O(n) 的栈空间。

```



##  126. <a name='Powxn'></a>50 Pow(x, n)

[小明](https://www.bilibili.com/video/BV1W54y1q7CV?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1Ai4y147kr?spm_id_from=333.999.0.0)

```py
输入：x = 2.00000, n = 10
输出：1024.00000


输入：x = 2.10000, n = 3
输出：9.26100


输入：x = 2.00000, n = -2
输出：0.25000
解释：2-2 = 1/22 = 1/4 = 0.25



class Solution:
    def myPow(self, x: float, n: int) -> float:
        res = 1

        if n < 0:
            x = 1 / x
            n = - n

        # 类似2分，速度更快
        while n: # 😐😐 while 循环
            if n % 2 == 1:
                res *= x # 注意: res 这里, 同步发生变化
            n >>= 1 # 等价于 n //= 2
            x *= x       # 注意: x 这里, 同步发生变化
        return res

时间复杂度： O(logn)，即为对 n 进行二进制拆分的时间复杂度。

空间复杂度： O(1)。
```



##  123. <a name='Offer62.'></a>剑指 Offer 62. 圆圈中最后剩下的数字

```py
输入: n = 5, m = 3
输出: 3


输入: n = 10, m = 17
输出: 2

https://leetcode-cn.com/problems/yuan-quan-zhong-zui-hou-sheng-xia-de-shu-zi-lcof/solution/huan-ge-jiao-du-ju-li-jie-jue-yue-se-fu-huan-by-as/

0个人时候游戏就不存在了， 1个人时候直接获胜， 
 
反推公式：

f(n,m) = (f(n,m) + m) % i #i为当前人数

f(8,3) = [f(7,3) + 3] % 8

约瑟夫环：

class Solution:
    def lastRemaining(self, n: int, m: int) -> int:
        res = 0
        for i in range(2, n + 1):
            res = (res + m) % i
        return res


时间复杂度： O(n)，需要求解的函数值有 n 个。

空间复杂度： O(1)，只使用常数个变量。
```

##  124. <a name='Offer51.'></a>剑指 Offer 51. 数组中的逆序对

```py
时间复杂度：O(N logN) 
空间复杂度：O(N)


如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对：


输入: [7,5,6,4]
输出: 5


class Solution:
    def reversePairs(self, nums: List[int]) -> int:
        negUpQue = []
        res = 0
        for num in nums:
            # 变负数插入，绝了-v，构成递增序列
            i = bisect.bisect_left(negUpQue,-num) # bisect_left 返回的待插入位置分别是 0，1，1，3，
            res += i # 前面有多少个比它大的，当前数就有多少个逆序对,加起来就是逆序对总数 5
            negUpQue[i:i] = [-num]
            # 这里也可以写：q.insert(i, -v)
        return res
# q[i:i] = [-v] 的效果如下，是一个排好序的数组：
# [-7]
# [-7, -5]
# [-7, -6, -5]
# [-7, -6, -5, -4]

如果不用负数，就要用 res += len(q) - i 了，

并且要改用 i = bisect.bisect(q, v)。

```




##  135. <a name='RotateArray'></a>189. Rotate Array 

[小梦想家](https://www.bilibili.com/video/BV1Yb411H7Yy?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1N541177Bk?spm_id_from=333.999.0.0)

```py
输入: nums = [1,2,3,4,5,6,7], k = 3
输出: [5,6,7,1,2,3,4]


解释:
向右轮转 1 步: [7,1,2,3,4,5,6]
向右轮转 2 步: [6,7,1,2,3,4,5]
向右轮转 3 步: [5,6,7,1,2,3,4]

class Solution:
    def rotate(self, nums: List[int], k: int) -> None:
        rotate = k % len(nums)
        if rotate:
            nums[:] = nums[::-1]
            nums[:rotate] = nums[:rotate][::-1]
            nums[rotate:] = nums[rotate:][::-1]

时间复杂度：O(N) 
空间复杂度：O(1)
```

# 8 day (得分 = 3分) 81

##  136. <a name='Searcha2DMatrix-240.'></a>74 Search a 2D Matrix - 240. 二维数组的查找

[小明](https://www.bilibili.com/video/BV1aK4y1h7Bb?spm_id_from=333.999.0.0)

```py
输入：matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 3
输出：true

输入：matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 13
输出：false
 

时间复杂度： O(logmn)，其中 m 和 n 分别是矩阵的行数和列数。
空间复杂度： O(1)。

class Solution:
    def searchMatrix(self, matrix: List[List[int]], target: int) -> bool:
        m = len(matrix)
        n = len(matrix[0])
        l = 0
        r = m * n - 1
        while l <= r: # 😐 while 循环
            mid = (l + r) // 2
            midRow = mid // n
            midCol = mid % n
            if matrix[midRow][midCol] == target:
                return True
            elif matrix[midRow][midCol] > target:
                r = mid - 1 # 易错点：+1,-1不要写反了
            else:
                l = mid + 1
        return False
```






##  140. <a name='IntersectionofTwoArrays'></a>349. Intersection of Two Arrays

https://leetcode-cn.com/problems/intersection-of-two-arrays/

[小梦想家](https://www.bilibili.com/video/BV1zx411o7i1?spm_id_from=333.999.0.0)

```py
输入：nums1 = [1,2,2,1], nums2 = [2,2]
输出：[2]

输入：nums1 = [4,9,5], nums2 = [9,4,9,8,4]
输出：[9,4]
解释：[4,9] 也是可通过的



时间复杂度： O(m+n)，其中 m 和 n  分别是两个数组的长度。

使用两个集合分别存储两个数组中的元素需要 O(m+n) 的时间，

遍历较小的集合并判断元素是否在另一个集合中需要 O(min(m,n)) 的时间，因此总时间复杂度是 O(m+n)。

空间复杂度： O(m+n)，其中 m 和 n 分别是两个数组的长度。空间复杂度主要取决于两个集合。

class Solution:
    def intersection(self, nums1: List[int], nums2: List[int]) -> List[int]:
        set1 = set(nums1)
        set2 = set(nums2)
        return [x for x in set1 if x in set2]

class Solution:
    def intersection(self, nums1, nums2):
        """
        :type nums1: List[int]
        :type nums2: List[int]
        :rtype: List[int]
        """
        return list(set(nums1) & set(nums2))

```



##  254. <a name='386.'></a> 386. 字典序排数

```py
输入：n = 13
输出：[1,10,11,12,13,2,3,4,5,6,7,8,9]


输入：n = 2
输出：[1,2]


时间复杂度： O(n)，n 为给定的数
空间复杂度： O(n)，为递归栈占用的空间

class Solution:
    def lexicalOrder(self, n):
        def dfs(num):
            if num <= n: 
                res.append(num)
                for nxt in range(num * 10, num * 10 + 10):
                    dfs(nxt)

        res = []
        for num in range(1, 10):
            dfs(num)
        return res
```

```py
# class Solution:
#     def lexicalOrder(self, n):
#         return sorted(list(range(1, n+1)), key = lambda x: str(x))
```

```py
# 输入：n = 13
# 输出：[1,10,11,12,13,2,3,4,5,6,7,8,9]
# class Solution:
#     def lexicalOrder(self, n):
#         ans = []
#         num = 1
#         while len(ans) < n: # 😐😐😐 while 循环
#             while num <= n: # 😐😐😐 while 循环 # 不断进入下一层
#                 ans.append(num)
#                 num *= 10
#             while num % 10 == 9 or num > n: # 😐😐😐 while 循环 # 不断返回上一层
#                 num //= 10
#             num += 1  # 遍历该层下一个数
#         return ans

```

##  145. <a name='K'></a>440. 字典序的第K小数字

https://leetcode-cn.com/problems/k-th-smallest-in-lexicographical-order/

```py
我们求字典序第k个就是上图`前序遍历`访问的第k节点！

https://leetcode-cn.com/problems/k-th-smallest-in-lexicographical-order/solution/yi-tu-sheng-qian-yan-by-pianpianboy/


但是不需要用`前序遍历`，如果我们能通过`数学方法`求出`节点1`和`节点2`之间需要走几步，减少很多没必要的移动。

其实只需要按`层节点个数计算`即可，图中`节点1`和`节点2`在`第二层`，因为 n = 13，`节点1`可以移动到`节点2`（同一层）所以在第二层需要移动1步。

第三层，移动个数就是 (13 - 10 + 1) = 4 （min（13 + 1， 20） - 10）

所以`节点1`到`节点2`需要移动 1 + 4 = 5 步

1. 当移动步数 <= k，说明: 

需要向`右节点`移动，图中就是`节点1`移动到`节点2`。

2. 当移动步数 > k，说明: 

目标值在`节点1`和`节点2`之间，我们要向下移动！即从`节点1`移动到`节点10`。

输入: n = 13, k = 2
输出: 10
解释: 字典序的排列是 [1, 10, 11, 12, 13, 2, 3, 4, 5, 6, 7, 8, 9]，所以第二小的数字是 10。


输入: n = 1, k = 1
输出: 1




class Solution:
    def findKthNumber(self, n: int, k: int) -> int:
        
        def calSteps(n, cur, nxt):
            step = 0
            while cur <= n: # 😐😐😐 while 循环
                step += min(nxt, n+1) - cur # 比如n是195的情况195到100有96个数
                cur *= 10
                nxt *= 10
            return step
                
        cur = 1
        k -= 1 # k - 1 就可以与步长 比较
        
        while k > 0: # 😐😐😐 while 循环
            steps = calSteps(n, cur, cur + 1)
            if steps <= k: # 第k个数不在以cur为根节点的树上
                k -= steps 
                cur += 1  # prefix在字典序数组中从左往右移动
            else:  # 在子树中
                k -= 1
                cur *= 10 # prefix 在字典序数组中从上往下移动
        
        return cur

# 当前值： 1 2
# 当前值： 10 20
# steps: 11 cur: 2 k: 3
# 当前值： 2 3
# 当前值： 20 30
# steps: 5 cur: 20 k: 2
# 当前值： 20 21
# steps: 1 cur: 21 k: 1
# 当前值： 21 22
# steps: 1 cur: 22 k: 0


ss = Solution()
print(ss.findKthNumber(23,15))

时间复杂度：O(log N)^2 ，其中 n 为 给定的 数值的大小。

每次计算子树下的节点数目的搜索深度最大为 log 10 N，最多需要搜索 log 10 N
​
每一层最多需要计算 10 次，最多需要计算  10 × (log 10 n) ^ 2 次，因此时间复杂度为 O(log N)^2。

空间复杂度：O(1) ，不需要开辟额外的空间，只需常数空间记录常量即可。
```

##  169. <a name='N'></a>400. 第N个数字

```py
输入：n = 11
输出：0
解释：第 11 位数字在序列 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, ... 里是 0 ，它是 10 的一部分。

class Solution:
    def findNthDigit(self, n: int) -> int:
        dnum, count = 1, 9
        while n > dnum * count: # 😐 while 循环
            n -= dnum * count
            dnum += 1
            count *= 10
        index = n - 1
        carry = 10 ** (dnum - 1)
        tail = index // dnum
        digitIndex = index % dnum

        num = carry + tail
        return int(str(num)[digitIndex])
# 问第 300 个数
# 1 ~ 9        9个数字 * 1位数字
# 10 ~ 99     90个数字 * 2位数字
# 100 ~ 999  900个数字 * 3位数字
# 300 - 9 - 2*90 = 111
# (111 - 1) // 3 = 36
# (111 - 1) % 3 = 2
# 100 + 36 = 136
时间复杂度： O(log 10 n)。用 d 表示第 n 位数字所在整数的位数，循环需要遍历 d 次，由于 d=O(log 10 n)，

空间复杂度：O(1)。

```


##  147. <a name='-1'></a>135. 分发糖果

https://leetcode-cn.com/problems/candy/

```py
每个孩子至少分配到 1 个糖果。
相邻两个孩子评分更高的孩子会获得更多的糖果。


输入：ratings = [1,0,2]
输出：5
解释：你可以分别给第一个、第二个、第三个孩子分发 2、1、2 颗糖果。

输入：ratings = [1,2,2]
输出：4
解释：你可以分别给第一个、第二个、第三个孩子分发 1、2、1 颗糖果。
     第三个孩子只得到 1 颗糖果，这满足题面中的两个条件。


class Solution:
    def candy(self, ratings: List[int]) -> int:
        n = len(ratings)
        dp = [1] * n
        # 从左往右
        for i in range(1, n):
            if ratings[i] > ratings[i - 1]:
                dp[i] = dp[i - 1] + 1
                # [1, 1, 2]
        # 从右往左
        for j in range(n - 2, -1, -1):
            if ratings[j] > ratings[j + 1]:
                dp[j] = max(dp[j], dp[j + 1] + 1)
                # [2, 1, 2]
        return sum(dp)
# 输入: [1,0,2]
# 输出: 5

时间复杂度： O(n)。我们需要遍历两次数组以分别计算满足左规则或右规则的最少糖果数量。

空间复杂度： O(n)。我们需要保存所有的左规则对应的糖果数量。


```

##  149. <a name='-1'></a>572-另一个树的子树

[哈哈哈](https://www.bilibili.com/video/BV1cA411t7zD?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1wt4y197aB?spm_id_from=333.999.0.0)

```py
输入：root = [3,4,5,1,2], subRoot = [4,1,2]
输出：true

输入：root = [3,4,5,1,2,null,null,null,null,0], subRoot = [4,1,2]
输出：false


这题不可能是简单题

class Solution:
    def isSubtree(self, root: TreeNode, subRoot: TreeNode) -> bool:
        def isSame(A,B): # 函数的功能要明确，用来判断当前子树是否一致
            if not B and not A: 
                return True
            if A and B and A.val == B.val and isSame(A.left, B.left) and isSame(A.right, B.right):
                return True
            else: 
                return False
        
        if not root or not subRoot:
            return False
        if isSame(root, subRoot):
            return True
        return self.isSubtree(root.left, subRoot) or self.isSubtree(root.right, subRoot)

时间复杂度为  O(∣s∣×∣t∣)。
空间复杂度为  O(max{s深度, t深度})
```

##  171. <a name='SameTree'></a>100-Same Tree 

[哈哈哈](https://www.bilibili.com/video/BV1bJ411X7xH?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1bJ411X7xH?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Wb411e7ti?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1vf4y1R7Ue?spm_id_from=333.999.0.0)

> python:

```py
输入：p = [1,2,3], q = [1,2,3]
输出：true


输入：p = [1,2], q = [1,null,2]
输出：false


输入：p = [1,2,1], q = [1,1,2]
输出：false


时间复杂度： O(min(m,n))，其中 m 和 n 分别是两个二叉树的节点数。

对两个二叉树同时进行深度优先搜索，只有当两个二叉树中的对应节点都不为空时才会访问到该节点，

因此被访问到的节点数不会超过较小的二叉树的节点数。

空间复杂度： O(min(m,n))，其中 m 和 n 分别是两个二叉树的节点数。

空间复杂度取决于递归调用的层数，递归调用的层数不会超过较小的二叉树的最大高度，

最坏情况下，二叉树的高度等于节点数。



self.isSameTree(p.left, q.left) and self.isSameTree(p.right, q.right)

class Solution:
    def isSameTree(self, p: TreeNode, q: TreeNode) -> bool:
        if not p and not q:
            return True
        elif p and q and p.val == q.val and self.isSameTree(p.left, q.left) and self.isSameTree(p.right, q.right):
            return True
        else:
            return False
```

```py
class Solution:
    def isSameTree(self, p: TreeNode, q: TreeNode) -> bool:
        return str(p) == str(q)
```





##  151. <a name='JumpGame'></a>55 Jump Game

[小明](https://www.bilibili.com/video/BV14K4y1b7Fw?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1be411s7XX?spm_id_from=333.999.0.0)

```py
输入：nums = [2,3,1,1,4]
输出：true
解释：可以先跳 1 步，从下标 0 到达下标 1, 然后再从下标 1 跳 3 步到达最后一个下标。

# 精简一下的for循环😁
class Solution:
    def canJump(self, nums: List[int]) -> bool:
        cover = 0

        n = len(nums)
        for i in range(n):
            if cover >= i: # 易错点：在判断下一个cover前，先要判断i是否能够到达
                cover = max(cover, i + nums[i])
                # if cover == i:
                    # return False # 易错点：应该考虑特殊情况[0,1,2]
                if cover >= n - 1:
                    return True
        return False

时间复杂度： O(n)，其中 n 为数组的大小。只需要访问 nums 数组一遍，共 n 个位置。

空间复杂度： O(1)，不需要额外的空间开销


```



##  201. <a name='JumpGameII'></a>45 Jump Game II

[小明](https://www.bilibili.com/video/BV1fb4y1Z77x?spm_id_from=333.999.0.0)

```py
输入: nums = [2,3,1,1,4]
输出: 2
解释: 跳到最后一个位置的最小跳跃数是 2。
     从下标为 0 跳到下标为 1 的位置，跳 1 步，然后跳 3 步到达数组的最后一个位置。
假设你`总是`可以到达数组的最后一个位置。

class Solution:
    def jump(self, nums: List[int]) -> int:
        jump = 0
        cover = stop = 0

        n = len(nums)
        for i in range(n - 1):
            cover = max(cover, i + nums[i]) #易错点：是n-1，不是n，只要调到最后一格就算成功
            '''
            代表从 stop 位置能到达的最远的地点
            '''
            if i == stop:
                jump += 1 # jump + 1 的情况：2(0),stop=2,1(2),stop=4
                stop = cover
        return jump

时间复杂度： O(n)，其中 n 为数组的大小。只需要访问 nums 数组一遍，共 n 个位置。

空间复杂度： O(1)
```




##  153. <a name='DecodeWays'></a>91. Decode Ways

[花花酱](https://www.bilibili.com/video/BV1Lb411y7ec?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Pf4y1G7M5?spm_id_from=333.999.0.0)

```py
输入：s = "226"
输出：3
解释：它可以解码为 "BZ" (2 26), "VF" (22 6), 或者 "BBF" (2 2 6) 。
```

```py
class Solution:
    def numDecodings(self, s: str) -> int:
        n = len(s)
        dp0 = 1 # 这里 dp = 0 或者是 dp = 1 都可以，因为在第一轮的循环过后会更新
        dp1 = 1
        for i in range(n):
            res = 0
            if '1' <= s[i] <= '9': # 当前项
                res = dp1
            if i > 0 and '10' <= s[i-1:i+1] <= '26': # 当前项 + 前一项
                res += dp0
            dp1, dp0 = res, dp1
        return dp1
时间复杂度： O(n)，其中 n 是字符串 s 的长度。

空间复杂度： O(n) 或 O(1)。

如果使用数组进行状态转移，空间复杂度为 O(n)；

如果仅使用三个变量，空间复杂度为 O(1)。

```


# 9 day (得分 = 2分) 83

##  159. <a name='Offer61.'></a>剑指 Offer 61. 扑克牌中的顺子

```py
从若干副扑克牌中随机抽 5 张牌，判断是不是一个顺子
输入: [1,2,3,4,5]
输出: True

输入: [0,0,1,2,5]
输出: True

而大、小王为 0 ，可以看成任意数字

class Solution:
    def isStraight(self, nums: List[int]) -> bool:
        repeat = set()
        ma, mi = 0, 14
        for num in nums:
            if num in repeat: return False # 若有重复，提前返回 false
            if num == 0: continue # 跳过大小王
            ma = max(ma, num) # 最大牌
            mi = min(mi, num) # 最小牌
            repeat.add(num) # 添加牌至 Set
        return ma - mi < 5 # 最大牌 - 最小牌 < 5 则可构成顺子 

时间复杂度 O(N) = O(5) = O(1)  ： 
        其中 N 为 nums 长度，本题中 N ≡ 5 ；遍历数组使用 O(N) 时间。
空间复杂度 O(N) = O(5) = O(1)  ： 
        用于判重的辅助 Set 使用 O(N) 额外空间。
 
```





##  167. <a name='Numberof1Bits'></a>191 Number of 1 Bits

[小明](https://www.bilibili.com/video/BV1i5411J7SA?spm_id_from=333.999.0.0)

```py
输入：00000000000000000000000000001011
输出：3
解释：输入的二进制串 00000000000000000000000000001011 中，共有三位为 '1'。


输入：00000000000000000000000010000000
输出：1
解释：输入的二进制串 00000000000000000000000010000000 中，共有一位为 '1'。


输入：11111111111111111111111111111101
输出：31
解释：输入的二进制串 11111111111111111111111111111101 中，共有 31 位为 '1'。



# class Solution:
#     def hammingWeight(self, n: int) -> int:
#         res = sum(1 for i in range(32) if n & (1 << i)) 
#         return res


1111000
1110111
& 运算后，去掉最后的1
1110000

# 从1开始，每次<<一位，与n做与运算，如果不为0，则该位为1
class Solution:
    def hammingWeight(self, n: int) -> int:
        res = 0
        while n: # 😐😐😐 while 循环
            n &= n - 1
            res += 1
        return res
时间复杂度： O(logn)。循环次数等于 n 的二进制位中 1 的个数，最坏情况下 n 的二进制位全部为 1。我们需要循环 logn 次。

空间复杂度： O(1)，我们只需要常数的空间保存若干变量。

```



# 10 day (得分 = 2分) 85




##  170. <a name='ValidAnagram'></a>242. Valid Anagram 

[小梦想家](https://www.bilibili.com/video/BV1Db411s78v?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1hV411i73u?spm_id_from=333.999.0.0)

```py
输入: s = "anagram", t = "nagaram"
输出: true


输入: s = "rat", t = "car"
输出: false




直接返回两个计数器是否相等即可

class Solution:
    def isAnagram(self, s: str, t: str) -> bool:
        return Counter(s) == Counter(t) 
```

```py
class Solution:
    def isAnagram(self, s: str, t: str) -> bool:
        from collections import defaultdict
        
        sdic = defaultdict(int)
        tdic = defaultdict(int)

        for x in s: sdic[x] += 1
        
        for x in t: tdic[x] += 1

        return sdic == tdic

时间复杂度： O(n)，其中 n 为 s 的长度。

空间复杂度： O(S)，其中 S 为字符集大小，此处 S=26。
```




##  172. <a name='RepeatedSubstringPattern'></a>459 Repeated Substring Pattern

[小明](https://www.bilibili.com/video/BV1Yt4y1S7XZ?spm_id_from=333.999.0.0)

```py
输入: s = "abab"
输出: true
解释: 可由子串 "ab" 重复两次构成。

输入: s = "aba"
输出: false

输入: s = "abcabcabcabc"
输出: true
解释: 可由子串 "abc" 重复四次构成。 (或子串 "abcabc" 重复两次构成。)

class Solution:
    def repeatedSubstringPattern(self, s: str) -> bool:
        for i in range(1, len(s) // 2 + 1):
            '''
            i 的范围 1 到 len(s) // 2
            s[:i] * (len(s) // i) 能构成一个完整的 s
            '''
            if s == s[:i] * (len(s) // i):
                return True
        return False

时间复杂度：O(n)，其中 n 是字符串 s 的长度。

空间复杂度：O(1)。
```



##  174. <a name='Triangle'></a>120 【动态🚀规划】Triangle

[小明](https://www.bilibili.com/video/BV1m54y1L7Af?spm_id_from=333.999.0.0)

```py
输入：triangle = [[2],[3,4],[6,5,7],[4,1,8,3]]
输出：11
解释：如下面简图所示：
   2
  3 4
 6 5 7
4 1 8 3
自顶向下的最小路径和为 11（即，2 + 3 + 5 + 1 = 11）。

时间复杂度：O(n^2) ，其中 n 是三角形的行数。
空间复杂度：O(n^2)。

class Solution:
    def minimumTotal(self, triangle: List[List[int]]) -> int:
        i = len(triangle) - 2
        while i >= 0: # 😐😐 while 循环
            subi = i
            while subi >= 0: # 😐😐 while 循环
                triangle[i][subi] += min(triangle[i + 1][subi], triangle[i + 1][subi + 1])
                subi -= 1
            i -= 1
        return triangle[0][0]
```



##  176. <a name='SuperEggDrop'></a>887. Super Egg Drop

[花花酱](https://www.bilibili.com/video/BV1Tv411i7cP?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1ri4y1t78d?spm_id_from=333.999.0.0)

<img src="https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3ld2gccb6ey0.webp" width="30%">

```py
输入：k = 1, n = 2
输出：2


解释：
鸡蛋从 1 楼掉落。如果它碎了，肯定能得出 f = 0 。 
否则，鸡蛋从 2 楼掉落。如果它碎了，肯定能得出 f = 1 。 
如果它没碎，那么肯定能得出 f = 2 。 
因此，在最坏的情况下我们需要移动 2 次以确定 f 是多少。 




输入：k = 2, n = 6
输出：3



输入：k = 3, n = 14
输出：4





请你计算并返回要确定 f 确切的值 的 最小操作次数 是多少？

dp[k][m] 的含义是 k 个鸡蛋 移动 m 次最多能够确定多少楼层
这个角度思考
dp[k][m] 最多能够确定的楼层数为 L

那么我选定第一个扔的楼层之后，我要么碎，要么不碎

这就是把 L 分成 3 段:
左边：是碎的那段 长度是   dp[k][m - 1]
右边：是没碎的那段 长度是 dp[k - 1][m - 1] 因为已经碎了一个了
中间：是我选定扔的楼层 是 1

所以递推公式是: dp[k][m] = dp[k - 1][m - 1] + dp[k][m - 1] + 1

根据递推公式 如果采用 k 倒着从大到小计算 就可以只存一行的 dp[k] 直接原地更新 dp[k] 不影响后续计算 

只需要 O(K) 空间复杂度 O(KlogN) 鸡蛋完全够用的时候 就是走 LogN 步 最差情况是 1 个鸡蛋走 N 步 O(KN)

时间复杂度：O(eggs*log level) 
空间复杂度：O(eggs)。

class Solution:
    def superEggDrop(self, eggs: int, level: int) -> int:
            dp = [0] * (eggs + 1)
            m = 0
            while dp[eggs] < level: # 😐😐😐 while 循环
                m += 1
                for gg in range(eggs, 0, -1): # 从 eggs ~ 1
                    # 鸡蛋碎了，剩下的鸡蛋可以遍历多少楼层
                    # 鸡蛋没碎，可以遍历的楼层数目
                    dp[gg] = dp[gg - 1] + dp[gg] + 1
            return m

# 移动次数: 1 鸡蛋 0-3 对应楼层: [0, 1, 1, 1]
# 移动次数: 2 鸡蛋 0-3 对应楼层: [0, 2, 3, 3]
# 移动次数: 3 鸡蛋 0-3 对应楼层: [0, 3, 6, 7]
# 移动次数: 4 鸡蛋 0-3 对应楼层: [0, 4, 10, 14]
```




##  178. <a name='14.'></a>补充题14. 阿拉伯数字转中文数字

```py
def money(l,r):
    dic = {'0':'零','1':'壹','2':'贰','3':'叁','4':'肆','5':'伍','6':'陆','7':'柒','8':'捌','9':'玖'}
    p = '仟,佰,拾,亿,仟,佰,拾,万,仟,佰,拾,元'.split(',')
    q = '角,分'.split(',')
    resL = ''
    resR = '人民币'
    #小数部分解决
    if int(r):
        for i, char in enumerate(r):
            resL = resL + dic[char] + q[i] if char != '0' else resL + ''
    else:
        resL += '整'
    #整数部分
    if int(l):
        for i, char in enumerate(l):
            #这种情况是几十几百万，所以在这些拾后面没必要加元等单位
            idx = i-len(l)
            resR = resR + dic[char] + p[idx] if char != '0' or (idx+1) % 4 == 0 else resR + '零'
        resR = resR.replace('零零', '零')
        resR = resR.replace('拾零', '拾')
        resR = resR.replace('仟零零', '仟')
        resR = resR.replace('壹拾', '拾')
        resR = resR.replace('人民币零', '人民币')
    return resR + resL
 
         
     
while 1:
    try:
        raw = input().split('.')
        print(money(raw[0],raw[1]))
    except:
        break
```


##  181. <a name='-1'></a>670. 最大交换

```py
'''
让最早出现的数字， string[i]
用后面 lastI[j] > i
比他大的 ，且最大的数字交换 for j in range(9, int(d), -1)
'''
输入: 2736
输出: 7236

输入: 9973
输出: 9973

时间复杂度：O(10 N) 。其中，N 是输入数字的总位数。每个数字最多只考虑一次。
空间复杂度：O(10)。last 使用的额外空间最多只有 10 个。

class Solution:
    def maximumSwap(self, num: int) -> int:
        string = list(str(num))
        lastI = [None for _ in range(10)]      # 数字 0 ~ 9, 一共 9 个数字

        for i, d in enumerate(string):
            lastI[int(d)] = i                  # 统计每个数字出现的最后的位置

        for i, d in enumerate(string):          # 从最高位开始，往后面寻找
            for j in range(9, int(d), -1):      # 从最低位开始，往前面寻找
                if lastI[j] and lastI[j] > i:   # 位置的元素交换
                    string[lastI[j]], string[i] = string[i], string[lastI[j]]
                    return int(''.join(string))
        
        return num # 已是最大值就返回原数字
```





# 11 day (得分 = 2分) 87



##  187. <a name='GasStation'></a>134. Gas Station

[小梦想家](https://www.bilibili.com/video/BV1BC4y1472f?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1754y1176F?spm_id_from=333.999.0.0)

```py
输入：
[1,2,3,4,5]
[3,4,5,1,2]

输出：
4

预期结果：
3

时间复杂度： O(N)，其中 N 为数组的长度。我们对数组进行了单次遍历。
空间复杂度： O(1)。
class Solution:
    def canCompleteCircuit(self, gas: List[int], cost: List[int]) -> int:
        start = 0
        curSum = 0
        totalSum = 0
        for i in range(len(gas)):
            curSum   += gas[i] - cost[i]
            totalSum += gas[i] - cost[i]
            if curSum < 0:
                curSum = 0 # curSum 必须置为 0
                start  = i + 1
        if totalSum < 0: return -1
        return start
```



##  192. <a name='LetterCombinationsofaPhoneNumber'></a>17. Letter Combinations of a Phone Number 

[花花酱](https://www.bilibili.com/video/BV1PW411y7r2?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Yb411H7sL?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Ti4y1A73M?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1Sp4y1r7YP?spm_id_from=333.999.0.0)

深度优先 or 广度优先

* 时间复杂度：O(3^m × 4^n), m是对应3个字母的数字, n是对应4个字母的数字
* 空间复杂度：O(m+n)

```py
输入：digits = "23"
输出：["ad","ae","af","bd","be","bf","cd","ce","cf"]

class Solution:
    def letterCombinations(self, digits: str) -> List[str]:
        if not digits: # 易错点：一定要判断判断字符串是否为空
            return [] 
        dic = {'2':'abc','3':'def','4':'ghi','5':'kjl','6':'mno','7':'pqrs','8':'tuv','9':'wxyz'}
        res = [''] # 前面的排在前面
        for num in digits:
            res = [string + char for string in res for char in dic[num]]
        return res
```

##  194. <a name='Offer03.'></a>剑指 Offer 03. 数组中重复的数字

https://leetcode-cn.com/problems/shu-zu-zhong-zhong-fu-de-shu-zi-lcof/

```py
输入：
[2, 3, 1, 0, 2, 5, 3]

输出：2 或 3 

class Solution:
    def findRepeatNumber(self, nums: List[int]) -> int:
        repeatDict = {}
        for num in nums:
            if num not in repeatDict:
                repeatDict[num] = 1
            else:
                return num


class Solution:
    def findRepeatNumber(self, nums: List[int]) -> int:
        tmp = set()
        for i in range(len(nums)):
            tmp.add(nums[i])
            if len(tmp) < i + 1:
                return nums[i]


时间复杂度： O(n)。

遍历数组一遍。使用哈希集合（HashSet），添加元素的时间复杂度为 O(1)，故总的时间复杂度是 O(n)。

空间复杂度： O(n)。不重复的每个元素都可能存入集合，因此占用 O(n) 额外空间。
 
```



##  197. <a name='-1'></a>343-整数拆分

[哈哈哈](https://www.bilibili.com/video/BV1Dp4y1U79P?spm_id_from=333.999.0.0)

```py
输入: n = 10
输出: 36
解释: 10 = 3 + 3 + 4, 3 × 3 × 4 = 36。


class Solution:
    def integerBreak(self, n: int) -> int:
        dp = [0] * (n + 1)
        for end in range(2, n + 1):
            for stt in range(end):
                dp[end] = max(dp[end], stt * (end - stt), stt * dp[end - stt])
        return dp[n]
        # 假设对正整数 i 拆分出的第一个正整数是 j（1 <= j < i），则有以下两种方案：
        # 1) 将 i 拆分成 j 和 i−j 的和，且 i−j 不再拆分成多个正整数，此时的乘积是 j * (i-j)
        # 2) 将 i 拆分成 j 和 i−j 的和，且 i−j 继续拆分成多个正整数，此时的乘积是 j * dp[i-j]



时间复杂度：O(n^2) ，其中 n 是给定的正整数。对于从 2 到 n 的每一个整数都要计算对应的 dp 值，
空间复杂度： O(n)，其中 n 是给定的正整数。创建一个数组 dp，其长度为 n + 1。


```



##  199. <a name='17.24.'></a>面试题 17.24. 最大子矩阵

https://leetcode-cn.com/problems/max-submatrix-lcci/solution/zhe-yao-cong-zui-da-zi-xu-he-shuo-qi-you-jian-dao-/

```py
输入：
[
   [-1,0],
   [0,-1]
]
输出：[0,1,0,1]
解释：输入中标粗的元素即为输出所表示的矩阵



翻译一个python版本

1.时间复杂度：O(n^2*m)
2.空间复杂度：O(m)

class Solution:
    def getMaxMatrix(self, matrix: List[List[int]]) -> List[int]:
        n = len(matrix)
        m = len(matrix[0])
        height = [0] * m
        maxArea = float('-inf')
        res = [0] * 4
        for slowA in range(n):           
            height = [0] * m
            for fastA in range(slowA, n):
                acc = 0
                for fastB in range(m):
                    '''
                    由上往下累加
                    '''
                    height[fastB] += matrix[fastA][fastB]
                    # print('sums[rightB] =', sums[fastB])
                    if acc <= 0:
                        acc = height[fastB]
                        slowB = fastB
                        # print('dp <= 0:', slowB)
                    else:
                    '''
                    由左往右累加
                    '''
                        acc += height[fastB]
                    # 把答案存下来
                    if acc > maxArea:
                        maxArea = acc
                        res[0] = slowA
                        res[1] = slowB
                        res[2] = fastA
                        res[3] = fastB
                    # print('4个顶点=', slowA, slowB, fastA, fastB)
                    # print('dp=', dp, '最大值=', maxdp, res, '\n')
        return res
    
[
[ 9,-8, 1, 3,-2],
[-3, 7, 6,-2, 4],
[ 6,-4,-4, 8,-7]
]

预期结果: 
[0,0,2,3]

从第一行开始：
sums[rightB] = 9
sums[rightB] = -8
sums[rightB] = 1
sums[rightB] = 3
sums[rightB] = -2
# 由上往下累加
sums[rightB] = 6
sums[rightB] = -1
sums[rightB] = 7
sums[rightB] = 1
sums[rightB] = 2
# 由上往下累加
sums[rightB] = 12
sums[rightB] = -5
sums[rightB] = 3
sums[rightB] = 9
sums[rightB] = -5


从第二行开始：
# 由上往下累加
sums[rightB] = -3
sums[rightB] = 7
sums[rightB] = 6
sums[rightB] = -2
sums[rightB] = 4
# 由上往下累加
sums[rightB] = 3
sums[rightB] = 3
sums[rightB] = 2
sums[rightB] = 6
sums[rightB] = -3


从第三行开始：
# 由上往下累加
sums[rightB] = 6
sums[rightB] = -4
sums[rightB] = -4
sums[rightB] = 8
sums[rightB] = -7


4个顶点= 0 0 0 0
4个顶点= 0 0 0 1
4个顶点= 0 0 0 2
4个顶点= 0 0 0 3
4个顶点= 0 0 0 4

4个顶点= 0 0 1 0
4个顶点= 0 0 1 1
4个顶点= 0 0 1 2
4个顶点= 0 0 1 3
4个顶点= 0 0 1 4

4个顶点= 0 0 2 0
4个顶点= 0 0 2 1
4个顶点= 0 0 2 2
4个顶点= 0 0 2 3
4个顶点= 0 0 2 4

4个顶点= 1 0 1 0
# slowB = fastB
4个顶点= 1 1 1 1
4个顶点= 1 1 1 2
4个顶点= 1 1 1 3
4个顶点= 1 1 1 4 

4个顶点= 1 0 2 0
4个顶点= 1 0 2 1
4个顶点= 1 0 2 2
4个顶点= 1 0 2 3
4个顶点= 1 0 2 4

4个顶点= 2 0 2 0
4个顶点= 2 0 2 1
4个顶点= 2 0 2 2
# slowB = fastB
4个顶点= 2 3 2 3
4个顶点= 2 3 2 4

dp <= 0: 0
dp <= 0: 0
dp <= 0: 0
dp <= 0: 0
dp <= 0: 1
dp <= 0: 0
dp <= 0: 0
dp <= 0: 3

dp= 9 最大值= 9 [0, 0, 0, 0]
dp= 1 最大值= 9 [0, 0, 0, 0]
dp= 2 最大值= 9 [0, 0, 0, 0]
dp= 5 最大值= 9 [0, 0, 0, 0]
dp= 3 最大值= 9 [0, 0, 0, 0]
dp= 6 最大值= 9 [0, 0, 0, 0]
dp= 5 最大值= 9 [0, 0, 0, 0]
dp= 12 最大值= 12 [0, 0, 1, 2]
dp= 13 最大值= 13 [0, 0, 1, 3]
dp= 15 最大值= 15 [0, 0, 1, 4]
dp= 12 最大值= 15 [0, 0, 1, 4]
dp= 7 最大值= 15 [0, 0, 1, 4]
dp= 10 最大值= 15 [0, 0, 1, 4]
dp= 19 最大值= 19 [0, 0, 2, 3]
dp= 14 最大值= 19 [0, 0, 2, 3]
dp= -3 最大值= 19 [0, 0, 2, 3]
dp= 7 最大值= 19 [0, 0, 2, 3]
dp= 13 最大值= 19 [0, 0, 2, 3]
dp= 11 最大值= 19 [0, 0, 2, 3]
dp= 15 最大值= 19 [0, 0, 2, 3]
dp= 3 最大值= 19 [0, 0, 2, 3]
dp= 6 最大值= 19 [0, 0, 2, 3]
dp= 8 最大值= 19 [0, 0, 2, 3]
dp= 14 最大值= 19 [0, 0, 2, 3]
dp= 11 最大值= 19 [0, 0, 2, 3]
dp= 6 最大值= 19 [0, 0, 2, 3]
dp= 2 最大值= 19 [0, 0, 2, 3]
dp= -2 最大值= 19 [0, 0, 2, 3]
dp= 8 最大值= 19 [0, 0, 2, 3]
dp= 1 最大值= 19 [0, 0, 2, 3]

```

##  200. <a name='-1'></a>611. 有效三角形的个数

```py
输入: nums = [2,2,3,4]
输出: 3
解释:有效的组合是: 
2,3,4 (使用第一个 2)
2,3,4 (使用第二个 2)
2,2,3




输入: nums = [4,2,3,4]
输出: 4






class Solution:
    def triangleNumber(self, nums: List[int]) -> int:
        nums.sort()
        res = 0
        for i3 in range(len(nums)):
            i1, i2 = 0, i3 - 1
            while i1 < i2: # 😐😐😐 while 循环
                # 如果满足条件，则 i1 到 i2 之间的，所有 i1，都满足条件
                if nums[i1] + nums[i2] > nums[i3]:
                    res += i2 - i1
                    i2 -= 1
                # 如果不满足条件，i1 才需要增大，否则 i1 可以一直躺平
                else:
                    i1 += 1
        return res
        # 2,3,4,4
        # 2,3,4,-
        # 2,-,4,4 可以，则代表 -,3,4,4 也可以
        # 2,3,-,4
        # 2 + 3 > 4
        # 2 + 4 > 4
        # 2 + 3 > 4

时间复杂度：O(n^2)，其中 n 是数组 nums 的长度。

        我们需要 O(nlogn) 的时间对数组 nums 进行排序，

        随后需要 O(n^2) 的时间使用一重循环枚举 a 的下标以及使用双指针维护 b, c 的下标。

空间复杂度： O(logn)，即为排序需要的栈空间。
 
```



# 12 day (得分 = 2分) 89

##  203. <a name='-1'></a>679. 24 点游戏


##  207. <a name='SimplifyPath'></a>71. Simplify Path

[小梦想家](https://www.bilibili.com/video/BV1V7411w7jX?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1D5411J72c?spm_id_from=333.999.0.0)

```py
输入：path = "/home/"
输出："/home"
解释：注意，最后一个目录名后面没有斜杠。 

输入：path = "/../"
输出："/"
解释：从根目录向上一级是不可行的，因为根目录是你可以到达的最高级。

输入：path = "/home//foo/"
输出："/home/foo"
解释：在规范路径中，多个连续斜杠需要用一个斜杠替换。

输入：path = "/a/./b/../../c/"
输出："/c"

一个点（.）表示当前目录本身；
两个点 （..） 表示将目录切换到上一级（指向父目录）
（例如，'...'）均被视为文件/目录名称。
任意多个连续的斜杠（即，'//'）都被视为单个斜杠 '/' 。 

始终以斜杠 '/' 开头。
两个目录名之间必须只有一个斜杠 '/' 。
最后一个目录名（如果存在）不能 以 '/' 结尾。
此外，路径仅包含从根目录到目标文件或目录的路径上的目录（即，不含 '.' 或 '..'）。

时间复杂度： O(n)，其中 n 是字符串 path 的长度。

空间复杂度： O(n)。我们需要 O(n) 的空间存储 names 中的所有字符串。


class Solution(object):
    def simplifyPath(self, path):
        stack = []
        for i in path.split('/'):
            if i not in ['', '.', '..']:
                stack.append(i)
            elif i == '..' and stack:
                stack.pop()
        return "/" + "/".join(stack)
```



##  210. <a name='ZigZagConversion'></a> convert

```py
输入：s = "PAYPALISHIRING", numRows = 3
输出："PAHNAPLSIIGYIR"



输入：s = "PAYPALISHIRING", numRows = 4
输出："PINALSIGYAHRPI"


解释：
P     I    N
A   L S  I G
Y A   H R
P     I


输入：s = "A", numRows = 1
输出："A"


建立字典， key就是行index， value就是对应行的所有字母。

遍历字符串， 对于每个字母， 计算在第几行， 加入字典。

时间复杂度： O(n)。n 为字符串 s 的长度。

空间复杂度： O(r)。其中 r = numRows


class Solution: 
    def convert(self, s: str, numRows: int) -> str:
        if numRows == 1: return s
        dic = defaultdict(str)

        cycleCount = numRows * 2 - 2 # 循环为6
        for index, char in enumerate(s):
            key = index % cycleCount
            if key >= numRows: # 如果 >= 4, 则需要反过来
                key  = cycleCount - key
            dic[key] += char

        res = ""
        for i in range(numRows):
            res += dic[i]
        return res


```



##  213. <a name='UglyNumberII'></a> nthUglyNumber

```py
输入：n = 10
输出：12
解释：[1, 2, 3, 4, 5, 6, 8, 9, 10, 12] 是由前 10 个丑数组成的序列。

输入：n = 1
输出：1
解释：1 通常被视为丑数。

# python 实习面试这道题挂了，但我就不懂了，第一次见这题目谁能想到3指针的方法？
# [1, 2, 3, 4, 5, 6, 8, 9, 10, 12]
class Solution(object):
    def nthUglyNumber(self, n):
        res = [1]
        idx2 = 0
        idx3 = 0
        idx5 = 0
        for _ in range(n-1):
            # 根据指针，求得最小值
            nxt = min(res[idx2] * 2, res[idx3] * 3, res[idx5] * 5)
            res.append(nxt)
            # 移动指针
            if nxt == res[idx2] * 2:
                idx2 += 1
            if nxt == res[idx3] * 3:
                idx3 += 1
            if nxt == res[idx5] * 5:
                idx5 += 1
        return res[-1]

时间复杂度： O(n)。需要计算数组 dp 中的 n 个元素，每个元素的计算都可以在 O(1) 的时间内完成。
空间复杂度： O(n)。空间复杂度主要取决于数组 dp 的大小。

```

##  214. <a name='-1'></a>97. 交错字符串

```py
给定三个字符串 s1、s2、s3，请你帮忙验证 s3 是否是由 s1 和 s2 交错 组成的。
输入：s1 = "aabcc", s2 = "dbbca", s3 = "aadbbcbcac"
输出：true

class Solution:
    def isInterleave(self, string1: str, string2: str, stringtar: str) -> bool:
        n1, n2, tar = len(string1), len(string2), len(stringtar)
        if n1 + n2 != tar: return False

        dp = [False] * (n2 + 1)
        dp[0] = True
        for i1 in range(n1 + 1):
            for i2 in range(n2 + 1):
                '''
                i1 为 1 ~ n1
                i2 为 1 ~ n2
                p 为 0 ~ n1 + n2 - 1
                每次遍历：
                i1 固定
                i2 变化
                '''
                p = i1 + i2 - 1
                if i1: # s1 和 s3 比较
                    dp[i2] = dp[i2] and string1[i1 - 1] == stringtar[p]
                if i2: # s2 和 s3 比较
                    dp[i2] = dp[i2] or (dp[i2 - 1] and string2[i2 - 1] == stringtar[p])
        return dp[n2]

时间复杂度： O(nm)，两重循环的时间代价为 O(nm)。
空间复杂度： O(m)，即 s2 的长度。

"aabcc"
"dbbca"
"aadbbcbcac"
i1 和 i2 分别为:  0 0
i1 和 i2 分别为:  0 1
d a [True, False, False, False, False, False]
i1 和 i2 分别为:  0 2
b a [True, False, False, False, False, False]
i1 和 i2 分别为:  0 3
b d [True, False, False, False, False, False]
i1 和 i2 分别为:  0 4
c b [True, False, False, False, False, False]
i1 和 i2 分别为:  0 5
a b [True, False, False, False, False, False]


i1 和 i2 分别为:  1 0
a a [True, False, False, False, False, False]
i1 和 i2 分别为:  1 1
a a [True, False, False, False, False, False]
d a [True, False, False, False, False, False]
i1 和 i2 分别为:  1 2
a d [True, False, False, False, False, False]
b d [True, False, False, False, False, False]
i1 和 i2 分别为:  1 3
a b [True, False, False, False, False, False]
b b [True, False, False, False, False, False]
i1 和 i2 分别为:  1 4
a b [True, False, False, False, False, False]
c b [True, False, False, False, False, False]
i1 和 i2 分别为:  1 5
a c [True, False, False, False, False, False]
a c [True, False, False, False, False, False]


i1 和 i2 分别为:  2 0
a a [True, False, False, False, False, False]
i1 和 i2 分别为:  2 1
a d [True, False, False, False, False, False]
d d [True, True, False, False, False, False]
i1 和 i2 分别为:  2 2
a b [True, True, False, False, False, False]
b b [True, True, True, False, False, False]
i1 和 i2 分别为:  2 3
a b [True, True, True, False, False, False]
b b [True, True, True, True, False, False]
i1 和 i2 分别为:  2 4
a c [True, True, True, True, False, False]
c c [True, True, True, True, True, False]
i1 和 i2 分别为:  2 5
a b [True, True, True, True, True, False]
a b [True, True, True, True, True, False]


i1 和 i2 分别为:  3 0
b d [False, True, True, True, True, False]
i1 和 i2 分别为:  3 1
b b [False, True, True, True, True, False]
d b [False, True, True, True, True, False]
i1 和 i2 分别为:  3 2
b b [False, True, True, True, True, False]
b b [False, True, True, True, True, False]
i1 和 i2 分别为:  3 3
b c [False, True, True, False, True, False]
b c [False, True, True, False, True, False]
i1 和 i2 分别为:  3 4
b b [False, True, True, False, True, False]
c b [False, True, True, False, True, False]
i1 和 i2 分别为:  3 5
b c [False, True, True, False, True, False]
a c [False, True, True, False, True, False]


i1 和 i2 分别为:  4 0
c b [False, True, True, False, True, False]
i1 和 i2 分别为:  4 1
c b [False, False, True, False, True, False]
d b [False, False, True, False, True, False]
i1 和 i2 分别为:  4 2
c c [False, False, True, False, True, False]
b c [False, False, True, False, True, False]
i1 和 i2 分别为:  4 3
c b [False, False, True, False, True, False]
b b [False, False, True, True, True, False]
i1 和 i2 分别为:  4 4
c c [False, False, True, True, True, False]
c c [False, False, True, True, True, False]
i1 和 i2 分别为:  4 5
c a [False, False, True, True, True, False]
a a [False, False, True, True, True, True]


i1 和 i2 分别为:  5 0
c b [False, False, True, True, True, True]
i1 和 i2 分别为:  5 1
c c [False, False, True, True, True, True]
d c [False, False, True, True, True, True]
i1 和 i2 分别为:  5 2
c b [False, False, False, True, True, True]
b b [False, False, False, True, True, True]
i1 和 i2 分别为:  5 3
c c [False, False, False, True, True, True]
b c [False, False, False, True, True, True]
i1 和 i2 分别为:  5 4
c a [False, False, False, True, False, True]
c a [False, False, False, True, False, True]
i1 和 i2 分别为:  5 5
c c [False, False, False, True, False, True]
a c [False, False, False, True, False, True]


```








# 13 day (得分 = 1分) 90



##  223. <a name='08.12.'></a>面试题 08.12. 八皇后

##  224. <a name='SetMatrixZeroes'></a>73. Set Matrix Zeroes

```py
输入：matrix = [[1,1,1],[1,0,1],[1,1,1]]
输出：[[1,0,1],[0,0,0],[1,0,1]]


输入：matrix = [[0,1,2,0],[3,4,5,2],[1,3,1,5]]
输出：[[0,0,0,0],[0,4,5,0],[0,3,1,0]]



时间复杂度： O(mn)，其中 m 是矩阵的行数，n 是矩阵的列数。

        我们至多只需要遍历该矩阵两次。

空间复杂度： O(m+n)，其中 m 是矩阵的行数，n 是矩阵的列数。

        我们需要分别记录每一行或每一列是否有零出现。
 
class Solution:
    def setZeroes(self, matrix: List[List[int]]) -> None:
        """
        Do not return anything, modify matrix in-place instead.
        """
        setrow = set()
        setcol = set()
        # 记录含0的位置
        for i in range(len(matrix)):
            for j in range(len(matrix[0])):
                if matrix[i][j] == 0:
                    setrow.add(i)
                    setcol.add(j)
        # 将其所在行和列的所有元素都设为 0
        for r in setrow:
            for j in range(len(matrix[0])):
                matrix[r][j] = 0
        for c in setcol:
            for i in range(len(matrix)):
                matrix[i][c] = 0
        return matrix
```


##  225. <a name='Offer46.'></a>剑指 Offer 46. 把数字翻译成字符串



##  227. <a name='PartitionLabels'></a>763 Partition Labels

[小明](https://www.bilibili.com/video/BV1Ca4y177LW?spm_id_from=333.999.0.0)

##  228. <a name='K-1'></a>340. 至多包含 K 个不同字符的最长子串




##  231. <a name='Offer07.'></a>剑指 Offer 07. 重建二叉树




##  233. <a name='Offer32-III.III'></a>剑指 Offer 32 - III. 从上到下打印二叉树 III



##  235. <a name='RandomPickwithWeight'></a>528 Random Pick with Weight

[小明](https://www.bilibili.com/video/BV1UV411r7MK?spm_id_from=333.999.0.0)




##  238. <a name='TopKFrequentWords'></a>692. Top K Frequent Words

[花花酱](https://www.bilibili.com/video/BV1Mt41137eL?spm_id_from=333.999.0.0)

##  239. <a name='Offer50.'></a>剑指 Offer 50. 第一个只出现一次的字符



##  241. <a name='21.'></a>补充题21. 字符串相减


# 14 day (得分 = 1分) 91

##  242. <a name='-1'></a>354. 俄罗斯套娃信封问题

##  243. <a name='II'></a>253. 会议室 II

##  244. <a name='-1'></a>628. 三个数的最大乘积

##  245. <a name='-1'></a>674. 最长连续递增序列

##  246. <a name='Offer57-II.s'></a>剑指 Offer 57 - II. 和为s的连续正数序列

##  247. <a name='CountofSmallerNumbersAfterSelf'></a>315. Count of Smaller Numbers After Self

[花花酱](https://www.bilibili.com/video/BV1BW411C7TM?spm_id_from=333.999.0.0)

##  248. <a name='FactorialTrailingZeroes'></a> trailingZeroes

```py
输入：n = 5
输出：1


解释：5! = 120 ，有一个尾随 0
当 n = 25 时，ans = 5 + 1
当 n = 20 时，ans = 4


class Solution:
    def trailingZeroes(self, n: int) -> int:
        ans = 0
        while n: # 😐 while 循环
            n //= 5
            ans += n
        return ans

时间复杂度： O(logn)。

空间复杂度： O(1)。
```




##  252. <a name='Offer35.'></a>剑指 Offer 35. 复杂链表的复制

##  253. <a name='IntegertoRoman'></a>12. Integer to Roman

```py
输入: num = 3
输出: "III"

输入: num = 4
输出: "IV"

输入: num = 9
输出: "IX"

输入: num = 58
输出: "LVIII"
解释: L = 50, V = 5, III = 3.

输入: num = 1994
输出: "MCMXCIV"
解释: M = 1000, CM = 900, XC = 90, IV = 4.


class Solution:
    def romanToint(self, s: str) -> int:
        dic = ["M","D","C","L","X","V","I","O"]
         = [1000,500,100,50,10,5,1,0]

        res = 0
        pre = None
        s += 'O'
        for char in s:
            if pre:
                if dic[pre] >= dic[char]:
                    res += dic[pre]
                else:
                    res += -dic[pre] 
            pre = char

        return res


class Solution:
    def intToRoman(self, num: int) -> str:
        strlist = ["M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"]
        numlist = [1000,900,500,400,100,90,50,40,10,9,5,4,1]
        res = ''
        for i in range(len(numlist)):
            while num >= numlist[i]: # 注意还有 = 号
                num -= numlist[i]
                res = res + strlist[i]
        return res



时间复杂度： O(1)。

        由于 valueSymbols 长度是固定的，且这 13 字符中的每个字符的出现次数均不会超过 3，

        因此循环次数有一个确定的上限。对于本题给出的数据范围，循环次数不会超过 15 次。

        计算量与输入数字的大小无关。

空间复杂度： O(1)。

```



##  255. <a name='OpentheLock'></a>752. Open the Lock

[花花酱](https://www.bilibili.com/video/BV1NW411y74z?spm_id_from=333.999.0.0)

##  256. <a name='LongestSubstringwithAtLeastK'></a>395 Longest Substring with At Least K

[小明](https://www.bilibili.com/video/BV1hD4y1X7rq?spm_id_from=333.999.0.0)



##  258. <a name='SquaresofaSortedArray'></a>977 Squares of a Sorted Array

[小明](https://www.bilibili.com/video/BV1EX4y1u7Mb?spm_id_from=333.999.0.0)



##  260. <a name='-1'></a>617. 合并二叉树


# 15 day (得分 = 1分) 92

##  261. <a name='k'></a>60. 第k个排列

##  262. <a name='Implementstr'></a> strStr

```py
输入：haystack = "hello", needle = "ll"
输出：2

输入：haystack = "aaaaa", needle = "bba"
输出：-1

输入：haystack = "", needle = ""
输出：0

class Solution:
    def strStr(self, haystack: str, needle: str) -> int:
        for i in range(len(haystack) - len(needle) + 1):
            if haystack[i : i + len(needle)] == needle:
                return i 
        return -1
时间复杂度： O(n×m)，其中 n 是字符串 haystack 的长度，m 是字符串 needle 的长度。

空间复杂度： O(1)。我们只需要常数的空间保存若干变量。
```


##  263. <a name='CountPrimes'></a> countPrimes

```py
输入：n = 10
输出：4


输入：n = 0
输出：0


输入：n = 1
输出：0

埃氏筛
时间复杂度： O(nloglogn)
class Solution(object):
    def countPrimes(self, n):

        isPrime = [1 for i in range(n)]
        i = 2

        while i * i < n: # 😐😐😐 while 循环
        	if isPrime[i]:
        		j = i * i    # j 永远是 i 的倍数
        		while j < n : # 😐😐😐 while 循环
        			isPrime[j] = 0
        			j += i   # j 永远是 i 的倍数
        	i += 1

        return sum(isPrime[2:])
```



##  264. <a name='Offer65.'></a>剑指 Offer 65. 不用加减乘除做加法


##  266. <a name='N-1'></a>51. N皇后

##  267. <a name='24.'></a>补充题24. 双栈排序


##  269. <a name='Offer38.'></a>剑指 Offer 38. 字符串的排列

##  270. <a name='InsertintoaBinarySearchTree'></a>701 Insert into a Binary Search Tree

[小明](https://www.bilibili.com/video/BV1q54y1k76s?spm_id_from=333.999.0.0)


##  272. <a name='k-1'></a>698. 划分为k个相等的子集





##  274. <a name='-1'></a>836. 矩形重叠

##  275. <a name='-1'></a>99. 恢复二叉搜索树


##  279. <a name='MinimumInsertionStepstoMakeaStringPalindrom'></a>1312. 【回文🌈】Minimum Insertion Steps to Make a String Palindrom

[花花酱](https://www.bilibili.com/video/BV1HJ411L7b2?spm_id_from=333.999.0.0)



