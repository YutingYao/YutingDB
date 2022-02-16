## 206. 反转链表Reverse Linked List

https://leetcode-cn.com/problems/reverse-linked-list/submissions/

[哈哈哈](https://www.bilibili.com/video/BV1Q7411V7zr?spm_id_from=333.999.0.0)

[图灵](https://www.bilibili.com/video/BV1XQ4y1h735?spm_id_from=333.999.0.0)

[洛阳](https://www.bilibili.com/video/BV16Q4y1M767?spm_id_from=333.999.0.0)

```py
class Solution:
    def reverseList(self, head):
        pre, res = head, None
        while pre:
            res, res.next, pre = pre, res, pre.next
            # 易错点：必须写成一行
            # 就是多元赋值的时候，右边的值不会随着赋值而改变~
        return res

class Solution:
    def reverseList(self, head: ListNode) -> ListNode:
        res = None
        pre = head
        while pre:
            prenxt = pre.next
            pre.next = res
            res = pre
            pre = prenxt
        return res
```

```scala
/**
* time complexity: O(n)
* space complexity: O(1) 
*/
object Solution {
    def reverseList(head: ListNode): ListNode = {
        var res: ListNode = null
        var pre = head

        while (pre != null) {
            val prenxt = pre.next
            pre.next = res
            res = pre
            pre = prenxt
        }
        res
    }
}

```

## 146. LRU缓存机制【构造🏰】LRU Cache 

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
            value = self.cache.pop(key)
            self.cache[key] = value
            return value
        return -1


    def put(self, key: int, value: int) -> None:
        if key in self.cache:
            self.cache.pop(key)
        if len(self.cache) == self.capacity:
            self.cache.popitem(last=False)
        self.cache[key] = value

```

```scala

/**
* chosen solution
* build-in linkedHashMap
* time complexity: O(1)
*/
class LRUCache(_capacity: Int) {

    private val capacity = _capacity
    val cache = collection.mutable.LinkedHashMap[Int, Int]()


    def get(key: Int): Int = {
        cache.get(key) match {
            case Some(v) => 
                cache.remove(key)
                cache.put(key, v)
                v
            case None => -1
        }
    }

    def put(key: Int, value: Int): Unit = {
        cache.get(key) match {
            case Some(_) =>
                cache.remove(key)
                cache.update(key, value)

            case None =>
                if(cache.size >= capacity){
                cache.remove(cache.head._1)
                }
                cache.put(key, value)
        }   

    }
}

```

## 3. 无重复字符的最长子串 【滑动窗口🔹】数组中重复的数字 Longest Substring Without Repeating Characters

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
class Solution:
    def lengthOfLongestSubstring(self, s: str) -> int:
        dic = {}
        leftI = 0
        res = 0
        for rightI, char in enumerate(s):
            # char 重复出现，并且 上一个出现 在窗口内部
            # 含义为"tmmzuxt", start在m，当有新的t进来时，上一个t在start的前面，所以，此时的start不需要修改
            if char in dic and leftI <= dic[char]:      # 易错点: and start <= dic[char]: 
                leftI = dic[char] + 1        # 易错点: 这里的dic[char]还是前一个,且 +1
            else:
                res = max(res, rightI-leftI+1) # 易错点: +1
            dic[char] = rightI         # 易错点: dic[char]滞后更新
        return res
    
```

```scala
/**
* my first commit
* sliding windows
*  time  complexity: O(N), worst: O(2N) -> each char was visited twice
*/
object Solution1 {
    def lengthOfLongestSubstring(s: String): Int = {
        var right = 0
        var left = 0
        var current = ""
        var ret = ""
        
        while(right < s.length) {
            val char = s(right)
            if (current.contains(char)){
                current = current.drop(1)
                left += 1
                 
            }else {
                right += 1
                current += char
            }     
            if(current.length > ret.length) ret = current
        }
        ret.length
    }
}

```

## 215. 数组中的第K个最大元素（add）

https://leetcode-cn.com/problems/kth-largest-element-in-an-array/

```py
时间复杂度就是nlogn
class Solution:
    def findKthLargest(self, nums: List[int], k: int) -> int:
        q = []
        for num in nums:
            # n*log(k+1)
            heapq.heappush(q,num)
            if len(q) > k:
                # n*log(k)
                heapq.heappop(q)
        return heapq.heappop(q)

输入: [3,2,1,5,6,4] 和 k = 2
[1, 3, 2]
[2, 3, 5]
[3, 5, 6]
[4, 6, 5]
```

```py
class Solution:
    def findKthLargest(self, nums: List[int], k: int) -> int:
        return sorted(nums)[-k]
```

## 25. K 个一组翻转链表（add）

https://leetcode-cn.com/problems/reverse-nodes-in-k-group/solution/dong-hua-yan-shi-di-gui-25-kge-yi-zu-fan-y6hv/

```py
递归
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None

class Solution:
    def reverseKGroup(self, head: Optional[ListNode], k: int) -> Optional[ListNode]:
        cur = head
        cnt = 0
        while cur and cnt != k:
            cur = cur.next
            cnt += 1
        if cnt == k:
            cur = self.reverseKGroup(cur,k)
            while cnt:
                headnxt = head.next
                head.next = cur
                cur = head
                head = headnxt
                cnt -= 1
            head = cur # 易错点: 这一步不能漏
        return head # head 进来，head 返回


```

## 912 补充题4. 手撕快速排序（add）

https://leetcode-cn.com/problems/sort-an-array/submissions/

快速排序:

```py
class Solution:
    def randomized_partition(self, nums, l, r):
        pivot = random.randint(l, r)
        # 先把 nums[pivot] 靠边站
        nums[pivot], nums[r] = nums[r], nums[pivot]
        i = l - 1
        for j in range(l, r):
            if nums[j] < nums[r]: # nums[r] 就是 pivot
                i += 1
                nums[j], nums[i] = nums[i], nums[j] # nums[i] 存的都是较小的数字
        i += 1
        nums[i], nums[r] = nums[r], nums[i] # pivot 放到中间
        return i

    def randomized_quicksort(self, nums, l, r):
        if r - l <= 0:
            return
        mid = self.randomized_partition(nums, l, r)
        self.randomized_quicksort(nums, l, mid - 1)
        self.randomized_quicksort(nums, mid + 1, r)

    def sortArray(self, nums: List[int]) -> List[int]:
        self.randomized_quicksort(nums, 0, len(nums) - 1)
        return nums

时间复杂度：O(n log(n))
空间复杂度：O(log n) ~ O(n)
```

堆排序:

     0
    / \
   1   2
  / \ / \
 3  4 5  6

```py
class Solution:
    def max_heapify(self, heap, root, heap_len):
        p = root
        while p * 2 + 2 <= heap_len: # 当不是叶子节点
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
                break
        
    def sortArray(self, nums: List[int]) -> List[int]:
        # 时间复杂度O(N)
        # 从叶子节点开始遍历
        # 如果不是从叶子开始，可能白跑一遍
        for i in range(len(nums) - 1, -1, -1):
            self.max_heapify(nums, i, len(nums))
            
        # 时间复杂度O(N logN)
        for i in range(len(nums) - 1, -1, -1):
            # 把最大的元素放到末尾
            nums[i], nums[0] = nums[0], nums[i]
            self.max_heapify(nums, 0, i)
        return nums

时间复杂度：O(n log(n))
空间复杂度：O(1)
```

归并排序:

```py
class Solution:
    def merge_sort(self, nums, l, r):
        if l == r:
            return
        mid = (l + r) // 2
        # 先把子序列排序完成
        self.merge_sort(nums, l, mid)
        self.merge_sort(nums, mid + 1, r)
        tmp = []
        i, j = l, mid + 1   # i, j 是两个起始点
        while i <= mid or j <= r:
            # 如果 前半部部分结束了，或者后半部分没有结束
            if i > mid or (j <= r and nums[j] < nums[i]): # 因为前面是or，所以这里必须是对i进行约束
                tmp.append(nums[j])
                j += 1
            else:
                tmp.append(nums[i])
                i += 1

        nums[l: r + 1] = tmp

    def sortArray(self, nums: List[int]) -> List[int]:
        self.merge_sort(nums, 0, len(nums) - 1)
        return nums

时间复杂度：O(n log(n))
空间复杂度：O(n)
```

```py
排序问题各有各的招，我来说一个凑热闹的桶排序。反正所有数字在正负五万之间，你就拿100001个桶，遍历一遍把数字仍对应的桶里边，然后你就排好了。

class Solution:
    def sortArray(self, nums: List[int]) -> List[int]:
        bucket=collections.defaultdict(int)
        for n in nums:
            bucket[n]+=1
        ans=[]
        for i in range(-50000,50001):
            ans+=[i]*bucket[i]
        return ans
你一看这方法能行啊，复杂度也低！那为啥不经常用呢？你猜？你想想要有小数可咋整？
```

## 15. 三数之和

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
class Solution:
    def threeSum(self, nums: List[int]) -> List[List[int]]:
        n = len(nums)
        # nums.sort() # 另一种写法
        nums = sorted(nums)
        res = []
        for i in range(n-2):
            # 优化部分：
            if nums[i] > 0: break
            if nums[i] + nums[i+1] + nums[i+2] > 0: break
            # 这个写法不对：if i+1 < n-2 and nums[i] == nums[i+1]: continue
            # 这样可能直接跳过了[-1,-1,2,3]的前三个
            # 这个写法是正确的↓：
            if i - 1 >= 0 and nums[i] == nums[i-1]: continue
            if nums[i] + nums[n-2] + nums[n-1] < 0: continue
            # 双指针部分：
            left = i + 1
            right = n - 1
            while left < right: 
                if nums[i] + nums[left] + nums[right] > 0:
                    right -= 1
                elif nums[i] + nums[left] + nums[right] < 0:
                    left += 1
                else:
                    res.append([nums[i],nums[left],nums[right]])
                    # 去重：
                    while nums[left] == nums[left + 1] and left + 1 < right: # 注意边界
                        left += 1
                    left +=1
                    while nums[right] == nums[right - 1] and left < right - 1: # 注意边界
                        right -= 1
                    right -=1
        return res
```

```scala
/**
* my first commit
* hashset in twoSum
* a very time consuming version
* O(N^2)
*/
object Solution1 {
  def threeSum(nums: Array[Int]): List[List[Int]] = {

      val l = nums.groupBy(identity).mapValues(aa => if(aa.length >=3) aa.take(3) else aa ).values.flatten.toList

     l.zipWithIndex.flatMap {
      case (value, index) =>
        val ll = collection.mutable.ListBuffer(l: _*)
        ll.remove(index)

        twoSum(ll.toList, -value).filter(_.nonEmpty)
          .map(_ :+ value)
    }.map(pair => (pair.toSet, pair)).toMap.values.toList

  }

   def twoSum(nums: List[Int], target: Int): List[List[Int]] = {
    val valueCounter = nums.groupBy(identity).mapValues(_.length)

    nums.collect {
      case value if target - value == value && valueCounter.get(target - value).exists(_ >= 2) =>
        List(value, target - value)
      case value if target - value != value && valueCounter.contains(target - value) =>
        List(value, target - value)

    }
  }

}


```

## 53. 最大子序和53-【贪心🧡】Maximum subarray

https://leetcode-cn.com/problems/maximum-subarray/

[哈哈哈](https://www.bilibili.com/video/BV1QJ411R75H?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Yb411i7dn?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV11A41187AR?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1Ta4y1i7Sh?spm_id_from=333.999.0.0)

贪心

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5qrso4wuc440.png)

```py
class Solution:
    def maxSubArray(self, nums: List[int]) -> int:
        res = preSum = nums[0]
        for num in nums[1:]:
            preSum = max(preSum + num, num)
            res = max(res,preSum)
        return res
```

时间复杂度：O(n)
时间复杂度：O(1)

```scala
object Solution {
    def maxSubArray(nums: Array[Int]): Int = {
        for (i <- Range(1, nums.length)) {
            if (nums(i-1) > 0) {
                nums(i) += nums(i-1)
            }
        }
        nums.max
    }
}
```

## 1. 两数之和


[哈哈哈](https://www.bilibili.com/video/BV1rE411Y7UN?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV19b411v7qp?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Zf4y1G7W4?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1rv411k7VY?spm_id_from=333.999.0.0)

暴力求解：

* 时间复杂度:O(n2)

* 时间复杂度:O(1)

```py
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

* 时间复杂度:O(n)

```py
class Solution:
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        dic = {}
        for i,num in enumerate(nums):
            if num in dic:
                return [dic[num],i]
            dic[target - num] = i
```

```scala
/**
* chosen solution
* time complexity: O(N)
*/


object Solution0 {
  def twoSum(nums: Array[Int], target: Int): Array[Int] = {
    val value2Idx = nums.zipWithIndex.toMap
    nums.zipWithIndex.collectFirst {
      case (value, index) if value2Idx.get(target - value).exists(_ != index) =>
        Array(index, value2Idx(target - value))
    }.get
  }
}


/**
* more elegant
*/


object Solution1-2 {
  def twoSum(nums: Array[Int], target: Int): Array[Int] = {
    val value2Idx = nums.zipWithIndex.toMap
    nums.zipWithIndex.collectFirst {
      case (value, index) if value2Idx.get(target - value).exists(_ != index) =>
        Array(index, value2Idx(target - value))
    }.get
  }
}
```

## 21. 合并两个有序链表

https://leetcode-cn.com/problems/merge-two-sorted-lists/

[哈哈哈](https://www.bilibili.com/video/BV1rJ41127ry?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1hb411i7D7?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1my4y127bK?spm_id_from=333.999.0.0)

[洛阳](https://www.bilibili.com/video/BV1qZ4y1j7Jb?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1ck4y1k7J9?spm_id_from=333.999.0.0)

暴力解法：

* 时间复杂度:O(M+N)

* 时间复杂度:O(1)

```py
class Solution:
    def mergeTwoLists(self, list1: Optional[ListNode], list2: Optional[ListNode]) -> Optional[ListNode]:
        dummy = ListNode(0)
        cur = dummy # dummy是固定节点，cur是移动指针
        while list1 and list2: # 这里是and
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

递归解法：

* 时间复杂度:O(M+N)

* 时间复杂度:O(M+N)

```py
class Solution:
    def mergeTwoLists(self, list1: Optional[ListNode], list2: Optional[ListNode]) -> Optional[ListNode]:
        if not list1:
            return list2
        elif not list2:
            return list1
        elif list1.val < list2.val:
            list1.next = self.mergeTwoLists(list1.next,list2) # 找到较小头结点，提取出来
            return list1
        else:
            list2.next = self.mergeTwoLists(list1,list2.next) # 找到较小头结点，提取出来
            return list2
```

```scala
/**
* iterative version
* time complexity: O(N + M), N is the length of l1, M is the length of l2
*/
object Solution1 {
    def mergeTwoLists(l1: ListNode, l2: ListNode): ListNode = {
        val headNode = new ListNode(-1, null)
        var cur = headNode
        
        var no1 = l1;
        var no2 = l2;
        
        while(no1 != null && no2 != null) {
            if (no1.x >= no2.x){
                
                cur.next = no2
                no2 = no2.next
            }else {
                cur.next = no1
                no1 = no1.next
            }
            cur = cur.next
        }
        (no1, no2) match {
            case (_, null) => cur.next = no1
            case (null, _) => cur.next = no2
            case _ => throw new RuntimeException()
        }
        
        headNode.next
    }
}



/**
* recursive version
*/

object Solution1-2 {
    def mergeTwoLists(l1: ListNode, l2: ListNode): ListNode = {
        (l1, l2) match {
            case (null, _) => l2
            case (_, null) => l1
            case (a, b) => 
                if (a.x >= b.x){
                    b.next = mergeTwoLists(b.next, a)
                    b
                } else {
                    a.next = mergeTwoLists(a.next, b)
                    a   
                }
        }
    }
}

object Solution {
    def mergeTwoLists(l1: ListNode, l2: ListNode): ListNode = {
    if(l1 == null) return l2
    if(l2 == null) return l1

    if (l1.x < l2.x) {
      l1.next = mergeTwoLists(l1.next, l2)
      l1
    } else {
      l2.next = mergeTwoLists(l1, l2.next)
      l2
    }
  }
}

```

## 141-Linked List Cycle

https://leetcode-cn.com/problems/linked-list-cycle/

[哈哈哈](https://www.bilibili.com/video/BV1g7411a7ta?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1hb411H7XP?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1KX4y157vh?spm_id_from=333.999.0.0)

[洛阳](https://www.bilibili.com/video/BV1PA411b7gq?spm_id_from=333.999.0.0)

```py
方法一：集合 如果发现节点已在集合内则说明存在环

class Solution:
    def hasCycle(self, head: ListNode) -> bool:
        visited = set()
        while head:
            visited.add(head)
            head = head.next
            if head in visited:
                return True
        return False

感觉初始时把快慢指针都指向 head 反而更简洁：

class Solution:
    def hasCycle(self, head: ListNode) -> bool:
        fast = slow = head
        while fast and fast.next:
            fast = fast.next.next
            slow = slow.next
            if fast == slow:
                return True
        return False
        
```


```scala
object Solution1 {
    def hasCycle(head: ListNode): Boolean = {
        
        var cur = head
        val visited = new scala.collection.mutable.HashSet[ListNode]()
        
        var res: Boolean = false
        while (cur != null && res != true) {

            if(visited.contains(cur))  
                res = true
            else {
                visited += cur
                cur = cur.next
            }
        }
        res
    }
}


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
```


## 102-Binary Tree Level Order Traversal

https://leetcode-cn.com/problems/binary-tree-level-order-traversal/

[哈哈哈](https://www.bilibili.com/video/BV1W54y197Lc?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV14T4y1u7Wk?spm_id_from=333.999.0.0)

> python queue

```py
class Solution:
    def levelOrder(self, root: TreeNode) -> List[List[int]]:
        if not root:
            return []
        queue = [root]
        res = []
        while queue:
            level = []
            for _ in range(len(queue)): # 当前层的个数!!!
                node=queue.pop(0)
                level.append(node.val)

                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
            res.append(level)
        return res


from collections import deque
class Solution:
    def levelOrder(self, root: TreeNode) -> List[List[int]]:
        
        if not root:
            return []

        queue = deque([root]) 
        res = []
        
        while queue: 
            level = [] 
            for _ in range(len(queue)): 
                node = queue.popleft() 
                level.append(node.val) 
                if node.left:
                    queue.append(node.left) 
                if node.right:
                    queue.append(node.right) 
            res.append(level) 
        return res
```


> python 递归


```py
class Solution:
    def levelOrder(self, root: TreeNode) -> List[List[int]]:
        res = []

        def bfs(node, level):
            if node: 
                if len(res) < level + 1:
                    res.append([])
                res[level].append(node.val)
                bfs(node.left, level+1)
                bfs(node.right, level+1)

        bfs(root, 0)
        return res

class Solution:
    def levelOrder(self, root: TreeNode) -> List[List[int]]:
        dic = collections.defaultdict(list)

        def bfs(node, level):
            if node:
                dic[level].append(node.val)
                bfs(node.left, level + 1)
                bfs(node.right, level + 1)

        bfs(root, 0) 
        return [*dic.values()]
```

> scala queue

```scala
object Solution {
    def levelOrder(root: TreeNode): List[List[Int]] = {
        val buffer =  scala.collection.mutable.Queue[TreeNode]()
        val res =  scala.collection.mutable.ListBuffer[List[Int]]()

        if(root == null) return List[List[Int]]()
        buffer.enqueue(root)
	
        while(buffer.nonEmpty) {
          val cur = scala.collection.mutable.ListBuffer[Int]()
          for ( _ <- 0 until buffer.size) {
            val node = buffer.dequeue
            cur.append(node.value)
            if(node.left != null) buffer.enqueue(node.left)
            if(node.right != null) buffer.enqueue(node.right)
        }
        res += cur.toList
        }
        res.toList
    }
}
```

> scala 递归

```scala
object Solution {
    def levelOrder(root: TreeNode): List[List[Int]] = {
        val oderMap = scala.collection.mutable.Map[Int, List[Int]]()
        bfs(root, 1, oderMap)
        oderMap.values.toList
    }
    def bfs(node: TreeNode, level: Int, map: scala.collection.mutable.Map[Int, List[Int]]): Unit = {
        if (node != null) {
            val l = map.get(level)
                .map(_ :+ node.value)
                .getOrElse(List(node.value))

            map(level) = l
            bfs(node.left, level + 1, map)
            bfs(node.right, level + 1, map)
        }
    }
}
```

```scala
object Solution {
    def levelOrder(root: TreeNode): List[List[Int]] = {
        bfs(if(root == null) List() else List(root), List())
    }

    // @annotation.tailrec
    // @annotation.tailrec 告诉编译器，下面这个函数是递归的，在栈桢的管理上，希望编译器能所有优化。
    def bfs(queue: List[TreeNode], ans: List[List[Int]]): List[List[Int]] = {
        if(queue.isEmpty) ans
        else{
        bfs(queue.flatMap(n => List(n.left, n.right)).filter(_ != null), ans :+ queue.map(n => n.value))
        }
    }
}
```

## 121. Best Time to Buy and Sell Stock  121-买卖股票的最佳时机

https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock/

[花花酱](https://www.bilibili.com/video/BV1oW411C7UB?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1cZ4y1K7HP?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1D7411s7A1?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Qb411e7by?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV16z4y1Z7jD?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1hA411t76C?spm_id_from=333.999.0.0)

```py
class Solution:
    def maxProfit(self, prices: List[int]) -> int:
        maxprofit = 0
        minprice = 1e9
        for price in prices:
            maxprofit = max(maxprofit,price - minprice)
            minprice = min(minprice,price)
        return maxprofit
```

```scala
object Solution {
    def maxProfit(prices: Array[Int]): Int = {
        prices.foldLeft((Int.MaxValue, 0)){
            case ((minPriceSoFar, maxProfit), price) => (minPriceSoFar min price, maxProfit max (price - minPriceSoFar))
        }._2
    }
}
```

## 160-Intersection of Two Linked Lists

https://leetcode-cn.com/problems/intersection-of-two-linked-lists/

[哈哈哈](https://www.bilibili.com/video/BV1n741187X6?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1eb411H7uq?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV18K4y1J7wx?spm_id_from=333.999.0.0)

[洛阳](https://www.bilibili.com/video/BV1np4y1y789?spm_id_from=333.999.0.0)

```py
## 1. 哈希表

class Solution:
    def getIntersectionNode(self, headA: ListNode, headB: ListNode) -> ListNode:
        listA = set()
        while headA:
            listA.add(headA)
            headA = headA.next
        while headB:
            if headB in listA:
                return headB
            headB = headB.next
        return None

# > 时间复杂度 $O(M+N)$, 空间复杂度 $O(M)$

## 2. 双指针

class Solution:
    def getIntersectionNode(self, headA: ListNode, headB: ListNode) -> ListNode:
        if not headA or not headB:
            return None
        pa,pb = headA, headB
        while pa != pb:
            pa = pa.next if pa else headB
            pb = pb.next if pb else headA
        return pa

# > 时间复杂度 $O(M+N)$, 空间复杂度 $O(1)$
```


```scala
/**
 * Definition for singly-linked list.
 * class ListNode(var _x: Int = 0) {
 *   var next: ListNode = null
 *   var x: Int = _x
 * }
 */

object Solution {
    
    def getIntersectionNode(headA: ListNode, headB: ListNode): ListNode = {
        var ha = headA
        var hb = headB
        
        while(ha != hb){
            if(ha == null){
                ha = headB
            }else{
                ha = ha.next
            }
            
            if(hb == null){
                hb = headA
            }else{
                hb = hb.next
            }
        }
        
        ha
    }
}

```

## 88-Merge sorted array

https://leetcode-cn.com/problems/merge-sorted-array/

[哈哈哈](https://www.bilibili.com/video/BV14J411X7JE?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Wb411e7bg?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1g54y1s7ZG?spm_id_from=333.999.0.0)

直接合并后排序

```py
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
        while cur1 >= 0 and cur2 >= 0:
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

class Solution:
    def merge(self, nums1: List[int], m: int, nums2: List[int], n: int) -> None:
        """
        Do not return anything, modify nums1 in-place instead.
        """
        nums1[m:] = nums2
        nums1.sort()
```

```scala
object Solution {
    def merge(nums1: Array[Int], m: Int, nums2: Array[Int], n: Int): Unit = {
        var trail = m+n-1
        
        var t1 = m-1
        var t2 = n-1
        
        while(t1 > -1 && t2 > -1){
            val e1 = nums1(t1)
            val e2 = nums2(t2)
            
            if(e1 > e2){
                nums1(trail) = e1
                t1 -= 1
                trail -= 1
            }else{
                nums1(trail) = e2
                t2 -= 1
                trail -= 1
            }
        }
        
        if(t1 == -1){
            while(t2 > -1){
                nums1(trail) = nums2(t2)
                t2 -= 1
                trail -= 1
            }
        }else{
            while(t1 > -1){
                nums1(trail) = nums1(t1)
                t1 -= 1
                trail -= 1
            }
        }
        
    }
}

```

## 103. Binary Tree Zigzag Level Order Traversal

[小梦想家](https://www.bilibili.com/video/BV1NE411M7Fm?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV15h411Z7h5?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1GA411W7NY?spm_id_from=333.999.0.0)

> python 队列

```py
class Solution:
    def zigzagLevelOrder(self, root: TreeNode) -> List[List[int]]:
        if not root: 
            return []

        queue = [root]
        res = []
        indexflag = 1 
        while queue:
            level = []
            for _ in range(len(queue)):
                node = queue.pop(0)
                level.append(node.val)
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
            indexflag += 1 
            if not indexflag % 2: 
                res.append(level[:])
            else:
                res.append(level[::-1])
        return res

```

递归

```py
class Solution:
    def zigzagLevelOrder(self, root):
        res = []
        def bfs(node, level):
            if node:
                if level >= len(res):
                    res.append([])
                res[level].append(node.val)
                bfs(node.left, level + 1)
                bfs(node.right, level + 1)

        bfs(root, 0)
        for i in range(1, len(res), 2): # flag，各两个逆序
            res[i] = res[i][::-1]
        return res
```

## 236-二叉树的最近公共祖先

[哈哈哈](https://www.bilibili.com/video/BV1ov411172r?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV125411p7dr?spm_id_from=333.999.0.0)

```py
# Python 超越99%执行速度的解法：而且也简短

class Solution:
    def lowestCommonAncestor(self, root, p, q) -> 'TreeNode':

        if root in (None, p, q):
            return root 

        L = self.lowestCommonAncestor(root.left, p, q)
        R = self.lowestCommonAncestor(root.right, p, q)

        return R if None == L else L if None == R else root
```

```scala
/**
*  chosen solution
*  DFS with recursive
*  time complexity O(N), N is the number of node in the tree
*  space complexity O(N)
*/
object Solution0 {
  def lowestCommonAncestor(root: TreeNode, p: TreeNode, q: TreeNode): TreeNode = {
    _lowestCommonAncestor(root, p, q)
  }

  private def _lowestCommonAncestor(node: TreeNode, p: TreeNode, q: TreeNode): TreeNode = {
    if (node == null || node == p || node == q) return node
    /**
    *  1. if p and q are node 's child, return p q 's LCA 
    *  2.  if p and q are not node's child return null
    *  3. if p and q, only one of then ar node's child return that node (p or q)
    */
    val left = _lowestCommonAncestor(node.left, p, q)
    val right = _lowestCommonAncestor(node.right, p, q)

    (left, right) match {
      case (null, _) => right  // p and q are both not in left
      case (_, null) => left  // p and q are both not in right
      case (l, r) =>  node // only lowest common ancestor could return both non null node
      // p and q, one of then in left and the other one in right
    }
  }
}
```

## 20-Valid parentheses

[哈哈哈](https://www.bilibili.com/video/BV1DJ41127uA?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1hb411i7ek?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Hr4y1M7Sc?spm_id_from=333.999.0.0)

[洛阳](https://www.bilibili.com/video/BV1sC4y1H7Hs?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1QA411L7y7?spm_id_from=333.999.0.0)

先进后出，所以用栈

* 时间复杂度:O(n)

* 时间复杂度:O(n)

```py
# 这道题背一背！
class Solution:
    def isValid(self, s: str) -> bool:
        dic = {'{':'}','[':']','(':')'}
        stack = [] # stack 要提前定义好
        for char in s:
            if char in dic: # 是“key”
                stack.append(char) # 一个char进来，要么被append
            elif not stack or dic[stack.pop()] != char: 
                # 如果上一步不被append就是不对的
                # 如果这一步不匹配也是不对
                return False
        return not stack # 如果append上了，但没有被完全pop也是不对的
```

```scala
/**
* my first commitment
* using stack
* time complexity: O(N)
* space complexity: O(N)
*/
object Solution1 {
    def isValid(s: String): Boolean = {
        if(s.isEmpty || s.length % 2 != 0) return false
        val stack = scala.collection.mutable.Stack[Char]()
        
        val mapping = Map('(' -> ')', '{' -> '}', '[' -> ']')

        s.foreach{c => 
            
            if (mapping.contains(c)){
                stack push c
            }else{
                if(stack.isEmpty || mapping(stack.pop) != c) return false 
             
            }
        }
        stack.isEmpty
        
    }
}


/**
* using stack X FP
* time complexity: O(N)
* space complexity: O(N)
*/
object Solution1-3 {
    def isValid(s: String): Boolean = {
        val mapping = Map('(' -> ')', '{' -> '}', '[' -> ']')
        
        s.foldLeft(List.empty[Char]){ (stack, c) => 
            stack match {
                case pop :: stackAfterPop if  c.equals(mapping.getOrElse(pop, None)) => stackAfterPop
                case _ => c +: stack
            }
           
        }.isEmpty
        
    }
}

```

## 5. 【回文🌈】Longest Palindromic Substring -最长回文🌈子串

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
class Solution:
    def longestPalindrome(self, s: str) -> str:
        lenStr = len(s)

        if lenStr == 0:
            return ''

        if lenStr == 1:
            return s


        def getLen(l,r) -> int:
            while l>=0 and r<lenStr and s[l] == s[r]: # 注意：边界
                l -= 1
                r += 1
            return r - l - 1 # 注意：是 “-1”

        start = 0  
        end = 1 # 注意：在第一次的时候，end = 1
        maxmaxLen = maxLen = 1

        for mid in range(lenStr):
            maxLen = max(getLen(mid,mid),getLen(mid,mid+1))
            
            if maxLen > maxmaxLen:
                maxmaxLen = maxLen
                start = mid - (maxLen-1) // 2 #易错点：-1，最好背一背
                end = start + maxLen
        return s[start:end]
```

动态规划法：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.67y5euem0vo0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.90ngy2t8j3k.png)

* 时间复杂度:O(n2)

* 时间复杂度:O(n2)

```py
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
        for i in range(lenStr):
            dp[1][1] = True 
            # dp[1][1]是正确写法，dp[1,1]是错误写法

        for j in range(1,lenStr): # 把三角形画出来，先j，再i，
            for i in range(j): # 先框定结束j，再框定开始i。
                if s[i] == s[j]:
                    if j-i < 3:
                        dp[i][j] = True
                    else:
                        dp[i][j] = dp[i+1][j-1]
                if dp[i][j]:
                    maxlen = j-i+1
                    if maxlen > maxmaxlen:
                        maxmaxlen = maxlen
                        start = i
        return s[start:start+maxmaxlen]
```


```scala
/**
* chosen solution
* expand around center
* time complexity: O(N * 2 * N) = O(N^2)
*        expandLengths: O(N)
* space complexity: O(1)
*/

object Solution0 {
    def longestPalindrome(s: String): String = {
        if(s == null || s.isEmpty) return ""
        
        // 0 1 2 3 4 5 6 7
        // r a c e c a r
        // r a c e e c a r
        // b b c e c a a
        val (head, maxlen) = s.indices.foldLeft((0, 1)){
            case ((h, maxlen), i) => 
                val oddlen =  expandLengths(s, i, i)
                val evenlen = expandLengths(s, i, i + 1)
                val len = oddlen max evenlen
                if(len > maxlen)  (i -  (len - 1) / 2, len)
                else (h, maxlen)
        }
        s.slice(head, head + maxlen)
    }
    // return length
    @annotation.tailrec
    def expandLengths(s: String, left: Int, right: Int): Int = {
        if(0 <= left && right < s.length && s(left) == s(right)) expandLengths(s, left - 1, right + 1)
        else right - left - 1
    }
}

/**
* dynamic programming
*/
object Solution3 {
    def longestPalindrome(s: String): String = {
        if(s == null || s.isEmpty ) return ""
        if(s.length < 2) return s
 
        val dp = Array.ofDim[Boolean](s.length, s.length)
        var maxLen = 1
        var head = 0
 
        for(j <- 1 until s.length; i <- 0 until j){
            val currentLen = j - i + 1
            if(s(i) != s(j))  dp(i)(j) = false
            else if(currentLen < 4)  dp(i)(j) = true // currentLen - 2 < 2
            else dp(i)(j) = dp(i + 1)(j - 1)
            
            
            if(dp(i)(j) && currentLen > maxLen){
                maxLen = currentLen
                head = i

            }
        }
        
        s.slice(head, head + maxLen)
    }
}
```

## 33. Search in Rotated Sorted Array

[小梦想家](https://www.bilibili.com/video/BV1gJ411V7Sq?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV14t4y127hK?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV16A41147Fp?spm_id_from=333.999.0.0)

```py
# 我的模仿！啊😋

class Solution:
    def search(self, nums: List[int], target: int) -> int:
        # 定义第一个元素和最后一个元素
        l = 0
        r = len(nums) - 1

        while l <= r:
            m = (l+r) // 2
            if nums[m] == target:
                return m
            # 只存在一个上升序列
            if nums[l] <= nums[m]:
                if nums[l] <= target < nums[m]:
                    r = m - 1
                else: 
                    l = m + 1
            # 只存在一个上升序列
            else:
                if nums[m] < target <= nums[r]:
                    l = m + 1
                else: 
                    r = m - 1
        
        return -1
```

```py
# 这道题简直是在跟我开玩笑（狗头）

class Solution(object):
    def search(self, nums, target):
        return nums.index(target) if target in nums else -1
```

```scala

/**
* binary search - iterative version
*/
object Solution1-2 {
    def search(nums: Array[Int], target: Int): Int = {
      var left = 0
      var right = nums.length - 1
      
      var ans = -1
      while(ans == -1 && left <= right) {
        val mid = left + (right - left) / 2

        if (target == nums(mid) ){
          ans = mid

        } else if (nums(left) <= nums(mid)){ // left part is in order
          if (nums(mid) > target && target >= nums(left)) { // target is in left part
            right = mid - 1
          } else {
            left = mid + 1
          }
        } else { // right part is in order
          if (nums(mid) < target && target <= nums(right)) { // target is in right part
            left = mid + 1
          } else {
            right = mid - 1
          }
        } 
      }
      ans
    }
}
```

## 200 【🍒并查集】岛屿数量

[哈哈哈](https://www.bilibili.com/video/BV15K411p72j?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1Cg4y1i7dZ?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1KK4y1U7Ds?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1E64y1T7Nk?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1Np4y1977S?spm_id_from=333.999.0.0)

[一俩三四五](https://www.bilibili.com/video/BV114411q7sP?from=search&seid=1135814820928819139&spm_id_from=333.337.0.0)

```py
class Solution:
    def numIslands(self, grid: List[List[str]]) -> int:
        f = {}
        def find(x):
            f.setdefault(x,x)
            if f[x]!=x:
                f[x] = find(f[x])
            return f[x]
        def union(x,y):
            f[find(y)] = find(x)
            
        if not grid:
            return 0
        row,col =len(grid),len(grid[0])
        # 这里是 union
        for i in range(row):
            for j in range(col):
                if grid[i][j] == "1":
                    for x, y in [[-1, 0], [0, -1]]:
                        tmp_i = i + x
                        tmp_j = j + y
                        if 0 <= tmp_i < row and 0 <= tmp_j < col and grid[tmp_i][tmp_j] == "1":
                            # 把 array 翻译成 list
                            union(tmp_i * col + tmp_j, i * col + j)
        # 这里是 find
        res = set()
        for i in range(row):
            for j in range(col):
                if grid[i][j] == "1":
                    res.add(find(col*i+j))
        return len(res)
```

```py
# dfs
class Solution:
    def numIslands(self, grid: List[List[str]]) -> int:
        m, n = len(grid), len(grid[0]) # 行列
        ans = 0
        # 就像是把岛屿一个个蚕食
        def dfs(i, j): 
            if 0 <= i < m and 0 <= j < n and grid[i][j] == '1':   # 补充边界条件，防止溢出
                grid[i][j] = '0' # dfs置为0
                dfs(i + 1, j)  # 遍历4个领域
                dfs(i - 1, j)  # 遍历4个领域
                dfs(i, j - 1)  # 遍历4个领域
                dfs(i, j + 1)  # 遍历4个领域

        for i in range(m): # 行列
            for j in range(n): # 行列
                if grid[i][j] == '1': # 如果grid[i][j]为1，则dfs
                    ans += 1
                    dfs(i, j)
        return ans

```

```py
# 厉害的解法：Sink and count the islands.
class Solution(object):
    def numIslands(self, grid):
        def sink(i, j):
            if 0 <= i < len(grid) and 0 <= j < len(grid[0]) and grid[i][j] == '1':
                grid[i][j] = '0'
                map(sink, (i+1, i-1, i, i), (j, j, j+1, j-1))
                return 1
            return 0
        return sum(sink(i, j) for i in range(len(grid)) for j in range(len(grid[0])))

```

```scala
/**
* chosen solution
* dfs + floodfill
* time complexity: O(N * M) N is the grid length, M is the grid width
*/

object Solution0 {
    private val endLabel = '0'
    def numIslands(grid: Array[Array[Char]]): Int = {
        // val gridReplica = grid.map(_.clone).toArray
        val coords = for (i <- grid.indices; j <- grid(0).indices) yield (i, j)        
        coords.foldLeft(0){case (count, coord) => if(_dfs(grid, coord))  count + 1 else count}
        
    }
    
    def _dfs(grid: Array[Array[Char]], coord: (Int, Int)): Boolean = {
        val (row, col) = coord
        if(grid(row)(col) == endLabel) return false
        
        grid(row)(col) = endLabel
        getValidNeighbors(coord, (grid.length, grid(0).length)).foreach {
            case (nr, nc) if grid(nr)(nc) != endLabel => _dfs(grid, (nr, nc))
            case _ =>
        }
        true
    }
    
    private val getValidNeighbors = (coord: (Int, Int), shape: (Int, Int)) => {
        List(
            (coord._1 + 1, coord._2),
            (coord._1, coord._2 + 1),
            (coord._1 - 1, coord._2),
            (coord._1, coord._2 - 1)
        ).filter{case (row, col) => 0 <= row  && row < shape._1 && 0 <= col && col < shape._2}
    }
}

/**
* Union & Find 
* memo
*    1. without modify original grid's elements
* time complexity: O(N * M) both N M is the dimension of grid 
*     both union and find operation's amortized time complexity in UnionFind class are very very close to 1 but not 1
*/


object Solution {
  private val endLabel = '0'
  def numIslands(grid: Array[Array[Char]]): Int = {
    val unionFind = new UnionFind(grid)
    for(i <- grid.indices; j <- grid(0).indices)
      union((i, j), unionFind, grid)
    unionFind.counter

  }

  def union(coord: (Int, Int), unionFind: UnionFind, grid: Array[Array[Char]]): Unit = {
    val (row, col) = coord
    if(grid(row)(col) == endLabel) return

    neighbors(coord, (grid.length, grid(0).length)).foreach {
      case (nr, nc) if grid(nr)(nc) != endLabel  =>
        unionFind.union(coord, (nr, nc))
      case _ =>
    }
  }

  private val neighbors = (coord: (Int, Int), shape: (Int, Int)) => {
    val (row, col) = coord
    Seq(
      (row + 1, col),
      (row - 1, col),
      (row, col + 1),
      (row, col - 1)
    ).filter{ case (r, c) => 0 <= r && r < shape._1 && 0 <= c && c < shape._2}
  }
}

```

## 415-Add Strings

[哈哈哈](https://www.bilibili.com/video/BV18E411n7Cy?spm_id_from=333.999.0.0)

```py
python

按照加法运算，从最后一位开始相加。实际实现中，用两个指针分别指向两个字符串的末尾，然后用一个变量来保持进位。

class Solution:
    def addStrings(self, num1: str, num2: str) -> str:
        i, j, carry, res = len(num1)-1, len(num2)-1, 0, 0
        ans = ''

        while i >= 0 or j >= 0 or carry != 0:
            val = carry

            if i >= 0:
                val += ord(num1[i]) - ord('0')
                i -= 1
            if j >= 0:
                val += ord(num2[j]) - ord('0')
                j -= 1

            carry, res = divmod(val, 10)
            ans = str(res) + ans

        return ans  

时间复杂度： n

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

## 46- ★ 全排列

类似题目：

https://leetcode-cn.com/problems/permutation-i-lcci/

```py
class Solution:
    def permutation(self, S: str) -> List[str]:
        res = []
        path = ''
        def backtrack(S, path):
            if S == '':
                res.append(path) # 这里不需要：path[:]
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
        def backtrack(nums,path):
            # 易错点：if len(path) == n:
            if not nums: # 判断条件应该是这个
                res.append(path[:]) # 易错点：path[:]
                return
            else:
                for i in range(len(nums)):
                    backtrack(nums[:i]+nums[i+1:],path + [nums[i]]) # 易错点：n是不断变小的
        backtrack(nums,[])
        return res
```

```scala
object Solution {
    var output = List.empty[List[Int]]
    
    def backtrack(nums: Array[Int], l: Int, r: Int): Unit = {
        def swap(a: Int, b: Int) = {
            val temp = nums(a)
            nums(a) = nums(b)
            nums(b) = temp
        }
        
        if(l == r){
            output = output :+ nums.toList
        }else{
            (l to r).map(i => {
                swap(l, i)
                backtrack(nums, l+1, r)
                swap(l, i) //backtrack step
            })
        }
    }
    
    def permute(nums: Array[Int]): List[List[Int]] = {
        output = List.empty[List[Int]]
        var input = nums
        backtrack(input, 0, input.length - 1)
        output
    }
}

```

## 92-Reverse Linked List II

[哈哈哈](https://www.bilibili.com/video/BV1n7411G7N4?spm_id_from=333.999.0.0)

[洛阳](https://www.bilibili.com/video/BV19c411h7UE?spm_id_from=333.999.0.0)

```py
class Solution:
    def reverseBetween(self, head: ListNode, left: int, right: int) -> ListNode:
        dummy = ListNode(0,head)
        pre = dummy
        for _ in range(left-1):
            pre = pre.next

        cur = pre.next
        for _ in range(right-left):
            # 易错点：顺序不能错，中，后，前
            aft = cur.next
            cur.next = aft.next
            aft.next = pre.next
            pre.next = aft
        
        return dummy.next
```

## 142 Linked List Cycle II

https://leetcode-cn.com/problems/merge-k-sorted-lists/

[小明](https://www.bilibili.com/video/BV1W5411L7AF?spm_id_from=333.999.0.0)

[洛阳](https://www.bilibili.com/video/BV15e41147EY?spm_id_from=333.999.0.0)

![](https://s3.bmp.ovh/imgs/2022/02/5ca7ad17ae2ceeed.png)

```py
class Solution:
    def detectCycle(self, head: ListNode) -> ListNode:
        slow, fast = head, head
        while fast and fast.next:
            slow = slow.next
            fast = fast.next.next
            # 如果相遇
            if slow == fast:
                p = head
                q = slow
                while p!=q:
                    p = p.next
                    q = q.next
                #你也可以return q
                return p

        return None
```

```scala
object Solution {
    def detectCycle(head: ListNode): ListNode = {
        val visited = new scala.collection.mutable.HashSet[ListNode]()
        var cur = head
        
        var result: ListNode = null

        while (cur != null && result == null) {
            // println(result)
            if(visited.contains(cur))  
                result = cur
            else {
                visited += cur
                cur = cur.next
            }
        }
        result
        
    }
}

```

## 23. 【最小堆🌵】Merge k Sorted Lists

[花花酱](https://www.bilibili.com/video/BV1X4411u7xF?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Ty4y1178e?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1GK41157mu?spm_id_from=333.999.0.0)

暴力求解法：

* 时间复杂度: O(N) + O(N logN) + O(N)

* 空间复杂度: O(N) + O(N)

<img src="https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.65tcjjz2oy80.png" width="50%">

```py
# so easy，一遍过
class Solution:
    def mergeKLists(self, lists: List[ListNode]) -> ListNode:
        arr = []
        for listhead in lists:
            while listhead:
                arr.append(listhead.val)
                listhead = listhead.next
        arr.sort()
        dummy = ListNode(0)
        cur = dummy
        for value in arr:
            cur.next = ListNode(value)
            cur = cur.next
        return dummy.next
```

优先队列：

* 时间复杂度: O(N logk) 

* 空间复杂度: O(N) + O(1)

<img src="https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3tftyqf2g4s0.png" width="50%">

```py
class Solution:
    def mergeKLists(self, lists: List[ListNode]) -> ListNode:
        q = []  # 易错点：先要定义一个空
        dummy = ListNode(0)
        cur = dummy
        for i in range(len(lists)):
            if lists[i]:
                heapq.heappush(q, (lists[i].val, i))  # 易错点：要可以排序的
                lists[i] = lists[i].next # 易错点：注意，向后一位
        while q: # 易错点：注意这个循环条件
            val, idx = heapq.heappop(q)
            cur.next = ListNode(val)
            cur = cur.next
            if lists[idx]:
                heapq.heappush(q, (lists[idx].val, idx))
                lists[idx] = lists[idx].next # 易错点：注意，向后一位
        return dummy.next
```

两两合并：

* 时间复杂度: O(N logk) 

* 空间复杂度: O(1)

<img src="https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.60itjgowwpo0.png" width="50%">

```py
class Solution:
    def merge2Lists(self, list1, list2):
        dummy = ListNode(0)
        cur = dummy # dummy是固定节点，cur是移动指针
        while list1 and list2: # 这里是and
            if list1.val < list2.val: # 易错点：这里是list.val，而不是list
                cur.next = list1
                list1 = list1.next # 向后进一位
            else:
                cur.next = list2
                list2 = list2.next # 向后进一位
            cur = cur.next # 向后进一位
        cur.next = list1 or list2 # 易错点：这里是cur.next，而不是cur。这里是or
        return dummy.next

    def mergeKLists(self, lists: List[ListNode]) -> ListNode:     
        amount = len(lists)
        interval = 1
        while amount > interval:
            # 0,1,2,3,4,5,6  7-1
            # 0, ,2, ,4, ,6  7-2
            # 0, , , ,4, ,   7-3
            # 0, , , , , ,   7-4
            for i in range(0,amount-interval,2*interval):
                lists[i] = self.merge2Lists(lists[i], lists[i+interval]) # 易错点：方括号和小括号不要用错
            interval *= 2
        return lists[0] if amount>0 else None
```

## 54. Spiral Matrix

https://leetcode-cn.com/problems/spiral-matrix/

[小梦想家](https://www.bilibili.com/video/BV1N7411h7i1?spm_id_from=333.999.0.0)

```py
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
        while matrix:
            res += matrix.pop(0) # 易错点：注意是 +=
            matrix = list(zip(*matrix))[::-1] # 易错点：注意 [::-1] 的摆放
        return res
```

```scala
/**
* counterclockwise rotate matrix
* step:
*  1. add first line to list
*  2. counter-clockwise rotate remaining matrix: transpose + entire reverse
*  
*  remaining:
*  4 5 6
*  7 8 9
* 
* transpose:
*   4 7
*   5 8
*   6 9
* 
* reverse:
*   6 9
*   5 8
*   4 7
*/

object Solution2-1 {
    def spiralOrder(matrix: Array[Array[Int]]): List[Int] = { 
        def dfs(mx: Array[Array[Int]]): List[Int] = mx match {
            case mx if mx.isEmpty => List()
            case mx if mx.length == 1 => mx.head.toList
            case _ => mx.head.toList ::: spiralOrder(mx.tail.transpose.reverse)  // counter-clockwise
        }
        dfs(matrix)

    }    
}

```

## 300 【动态🚀规划 + 二分】Longest Increasing Subsequence 最长上升子序列

https://leetcode-cn.com/problems/longest-increasing-subsequence/

[花花酱](https://www.bilibili.com/video/BV1Wf4y1y7ou?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1rT4y1ujV?spm_id_from=333.999.0.0)

动态规划： 时间复杂度为 O(n2)

```py

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

        for i in range(1, len(nums)):
            for j in range(i):
                if nums[i] > nums[j]:
                    dp[i] = max(dp[j]+1, dp[i])

        return max(dp)

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
```

```scala
/**
* dynamic programming 
*  time complexity: O(N^2)
*  space  complexity: O(N)
*/

object Solution0 {
    def lengthOfLIS(nums: Array[Int]): Int = {
        if(nums == null || nums.isEmpty) return 0
        val dp = Array.fill[Int](nums.length)(1) // record the LIS of 0 to i sub-array in nums while select i
        for(i <- nums.indices; j <- 0 until i) {
            if(nums(i) > nums(j)) {
                dp(i) = (dp(j) + 1) max dp(i)
            }
        }
        dp.max
        
    }
}

```

## 704.Binary Search二分查找

https://leetcode-cn.com/problems/binary-search/

[图灵](https://www.bilibili.com/video/BV1Dh411v7yT?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1qa4y157E4?spm_id_from=333.999.0.0)

```py
对不起没忍住

class Solution:
    def search(self, nums: List[int], target: int) -> int:
        if target in nums :
            return nums.index(target)
        else:
            return -1

（版本一）左闭右闭区间，这个模板要记住

class Solution:
    def search(self, nums: List[int], target: int) -> int:
        left, right = 0, len(nums) - 1
        
        while left <= right:
            mid = (left + right) // 2

            if nums[mid] < target:
                left = mid + 1
            elif nums[mid] > target:
                right = mid - 1
            else:
                return mid
        return -1

（版本二）左闭右开区间，容易写错

# class Solution:
#     def search(self, nums: List[int], target: int) -> int:
#         left, right = 0, len(nums)
#         while left < right:
#             mid = (left + right) // 2
#             if nums[mid] < target:
#                 left = mid+1
#             elif nums[mid] > target:
#                 right = mid
#             else:
#                 return mid
#         return -1
```

```scala
object Solution {
    def search(nums: Array[Int], target: Int): Int = {
        nums.lastIndexOf(target)
    }
}

/**
* my first commitment:
* time complexity: O(logn)
*/

object Solution1 {
    def search(nums: Array[Int], target: Int): Int = {
      var left = 0
      var right = nums.length - 1
      var ans = -1
      while(ans == -1 && left <= right) {
        println(left, right)
        val mid: Int = left  + (right - left) / 2
        if(nums(mid) == target){
          ans = mid
        } else if(target > nums(mid)) {
          left = mid + 1
        } else {
          right = mid - 1
        }
 
      }
      ans
    }
}

```

## 42. Trapping Rain Water

https://leetcode-cn.com/problems/trapping-rain-water/

[花花酱](https://www.bilibili.com/video/BV1hJ41177gG?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1fi4y1t7BP?spm_id_from=333.999.0.0)


双指针：

* 时间复杂度: O(n)

* 空间复杂度: O(1)

```py
#   😋我的模仿
[0,1,0,2,1,0,1,3,2,1,2,1]

class Solution:
    def trap(self, height: List[int]) -> int:
        left = 0
        right = len(height)-1
        leftmax = 0
        rightmax = 0
        res = 0
        while left < right:
            if height[left] < height[right]:
                # 短板效应，移动小的那个值
                leftmax = max(leftmax,height[left])
                # 易错点：注意res和left的次序：先res，后left
                res += leftmax-height[left] 
                left += 1
            else:
                rightmax = max(rightmax,height[right])
                # 易错点：注意res和right的次序：先res，后right
                res += rightmax-height[right]
                right -= 1
        return res
```

## 232-【构造🏰】Implement Queue using Stacks

https://leetcode-cn.com/problems/implement-queue-using-stacks/

[哈哈哈](https://www.bilibili.com/video/BV1p741177pp?spm_id_from=333.999.0.0)

[图灵](https://www.bilibili.com/video/BV1Gf4y147Vj?spm_id_from=333.999.0.0)


```py
class MyQueue:

    def __init__(self):
        self.s1 = []
        self.s2 = []

    def push(self, x):
        # 要把新来的元素压入
        while self.s1:
            self.s2.append(self.s1.pop())
        self.s2.append(x)
        while self.s2:
            self.s1.append(self.s2.pop())

    def pop(self):
        # 假装最后一个元素是开头
        return self.s1.pop() if self.s1 else None
        

    def peek(self):
        # 假装最后一个元素是开头
        return self.s1[-1] if self.s1 else None

    def empty(self):
        return False if self.s1 else True
```

```scala
/**
* using two stack to implement
* one for push, the other for pop
* time complexity amortized O(1) per operation
* space complexity
*/

class MyQueue() {

  /** Initialize your data structure here. */
  private val inputStack = scala.collection.mutable.ArrayStack[Int]()
  private val outputStack = scala.collection.mutable.ArrayStack[Int]()


  /** Push element x to the back of queue. */
  def push(x: Int) {
    inputStack.push(x)

  }

  /** Removes the element from in front of queue and returns that element. */
  def pop(): Int = {
    if(outputStack.isEmpty) {
      while (inputStack.nonEmpty) {
        outputStack.push(inputStack.pop())
      }
    }
    if(outputStack.isEmpty) -1 else outputStack.pop()

  }

  /** Get the front element. */
  def peek(): Int = {
    if(outputStack.isEmpty) {
      while (inputStack.nonEmpty) {
        outputStack.push(inputStack.pop())
      }
    }
    if(outputStack.isEmpty) -1 else outputStack.head
  }

  /** Returns whether the queue is empty. */
  def empty(): Boolean = {
    outputStack.isEmpty && inputStack.isEmpty
  }

}

```

## 94-Inorder wih stack

https://leetcode-cn.com/problems/binary-tree-inorder-traversal/

[哈哈哈](https://www.bilibili.com/video/BV1uV411o78x?spm_id_from=333.999.0.0)

[洛阳](https://www.bilibili.com/video/BV1o54y1B7Z8?spm_id_from=333.999.0.0)

```py

最少代码递归：

class Solution:
    def inorderTraversal(self, root: Optional[TreeNode]) -> List[int]:
        return self.inorderTraversal(root.left) + [root.val] + self.inorderTraversal(root.right) if root else []

class Solution(object):
    def preorderTraversal(self, root):
        if not root:
            return []
        return [root.val] + self.preorderTraversal(root.left) + self.preorderTraversal(root.right)

class Solution:
    def inorderTraversal(self, root: TreeNode) -> List[int]:
        if not root:
            return []            
        return self.inorderTraversal(root.left) + [root.val] + self.inorderTraversal(root.right)

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
        if not root:
            return []
        res = []
        res.extend(self.inorderTraversal(root.left))
        res.append(root.val)
        res.extend(self.inorderTraversal(root.right))
        return res

# 前序遍历-递归-LC144_二叉树的前序遍历

class Solution:
    def preorderTraversal(self, root: TreeNode) -> List[int]:
        # 保存结果
        result = []
        
        def traversal(root: TreeNode):
            if root == None:
                return
            result.append(root.val) # 前序
            traversal(root.left)    # 左
            traversal(root.right)   # 右

        traversal(root)
        return result

中序遍历-递归-LC94_二叉树的中序遍历

class Solution:
    def inorderTraversal(self, root: TreeNode) -> List[int]:
        result = []

        def traversal(root: TreeNode):
            if root == None:
                return
            traversal(root.left)    # 左
            result.append(root.val) # 中序
            traversal(root.right)   # 右

        traversal(root)
        return result

# 后序遍历-递归-LC145_二叉树的后序遍历
class Solution:
    def postorderTraversal(self, root: TreeNode) -> List[int]:
        result = []

        def traversal(root: TreeNode):
            if root == None:
                return
            traversal(root.left)    # 左
            traversal(root.right)   # 右
            result.append(root.val) # 后序

        traversal(root)
        return result

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
            while node:
                stack.append(node)
                node = node.left

        stack, res = [], []
        appendAllLeft(root)
        while stack:
            node = stack.pop()
            res.append(node.val) # res.append 在中间
            appendAllLeft(node.right)
        return res

class Solution:
    def postorderTraversal(self, root: TreeNode) -> List[int]:
        res = []
        if not root:
            return res
        stack = [root]
        while stack:
            tmp = stack.pop()
            res.append(tmp.val)
            if tmp.left: stack.append(tmp.left)
            if tmp.right: stack.append(tmp.right)
        return res[::-1]

class Solution:
    def preorderTraversal(self, root: TreeNode) -> List[int]:
        res = []
        if not root:
            return res
        stack = [root]
        while stack:
            tmp = stack.pop()
            res.append(tmp.val)
            if tmp.right: stack.append(tmp.right)
            if tmp.left: stack.append(tmp.left)
        return res
```

## 144-Binary Tree Preorder Traversal

[哈哈哈](https://www.bilibili.com/video/BV1n7411D7NZ?spm_id_from=333.999.0.0)

[图灵](https://www.bilibili.com/video/BV1Ch411Q74P?spm_id_from=333.999.0.0)

[洛阳](https://www.bilibili.com/video/BV1RD4y1D7C7?spm_id_from=333.999.0.0)

## 145-Binary Tree Postorder Traversal

[哈哈哈](https://www.bilibili.com/video/BV1n7411D7ub?spm_id_from=333.999.0.0)

[图灵](https://www.bilibili.com/video/BV1uv411h7Gc?spm_id_from=333.999.0.0)

[洛阳](https://www.bilibili.com/video/BV1xZ4y1H7uS?spm_id_from=333.999.0.0)

## 199 Binary Tree Right Side View

[小明](https://www.bilibili.com/video/BV1854y1W7CB?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1xK4y1b7Wh?spm_id_from=333.999.0.0)

```py
class Solution:
    def rightSideView(self, root: TreeNode):
        res, level = [], root and [root]
        while level:
            res.append(level[-1].val)
            level = [nxt for n in level for nxt in (n.left, n.right) if nxt]
        return res

class Solution:
    def rightSideView(self, root: TreeNode):
        if not root:
            return []
        res = []
        level = [root]
        while level:
            tmp = []
            for n in level:
                if n.left: tmp.append(n.left)
                if n.right: tmp.append(n.right)
            res.append(level[-1].val)
            level = tmp
        return res

# 递归
class Solution:
    def rightSideView(self, root: TreeNode):
        res = []
        def traversal(node, level):
            if node:
                if level == len(res):
                    res.append(node.val)
                traversal(node.right, level + 1)
                traversal(node.left, level + 1)
        traversal(root, 0)
        return res
```

## 143 Reorder List

https://leetcode-cn.com/problems/reorder-list/

[小明](https://www.bilibili.com/video/BV1Jf4y1Q7y7?spm_id_from=333.999.0.0)

```py
class Solution:
    def reorderList(self, head: ListNode) -> None:
        que = []
        
        cur1 = head
        while cur1.next: # 链表除了首元素全部加入双向队列
            que.append(cur1.next)
            cur1 = cur1.next
        # 双指针
        cur2 = head
        i, j = 0, len(que) - 1
        while i <= j: # 一后一前加入链表
            cur2.next = que[j] # 头部连接到尾部
            cur2 = cur2.next
            j -= 1
            cur2.next = que[i] # 当i = j还是指向本身
            cur2 = cur2.next
            i += 1
        cur2.next = None # 尾部置空

# 双向队列
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
 

```

## 70. Climbing Stairs

https://leetcode-cn.com/problems/climbing-stairs/

[5:32 花花酱 DP](https://www.bilibili.com/video/BV1b34y1d7S8?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1gJ411R7X1?spm_id_from=333.999.0.0)

[哈哈哈 70(重制版)](https://www.bilibili.com/video/BV1G54y197eZ?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Wb411e7s9?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1DZ4y1H7k9?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1ki4y1u7tn?spm_id_from=333.999.0.0)

```py
# 我的模仿

class Solution:
    def climbStairs(self, n: int) -> int:
        dp0 = 1
        dp1 = 1
        for _ in range(n-1):
            dp1, dp0 = dp0 + dp1, dp1
            # 用2个数字分别存储
        return dp1
```

```scala
/**
* dynamic programming
*   1. dp(i) represent climb to i floor's distinct ways
*   2. dp(i) could be calculate from dp(i - 1) + dp(i - 2)
*           (1) taking a single step from dp(i - 1)
*           (2) taking a step of two from dp(i - 2)
* time complexity: O(N)
* space complexity: O(N)
*/
object Solution0 {
    def climbStairs(n: Int): Int = {
        val dp = Array.ofDim[Int](n + 1)
        dp(0) = 1
        dp(1) = 1
        (2 to n).foreach(i => dp(i) = dp(i - 1) + dp(i - 2))
        dp(n)
    }
}
```

```scala
object Solution {
    
    def climbStairs(n: Int): Int = {
        if(n==1){
            1
        }else if(n == 2){
            2
        }else{
            climbStairs(n-1) + climbStairs(n-2)
        }
    }
}
```

## 124. Binary Tree Maximum Path Sum

[花花酱](https://www.bilibili.com/video/BV1ct411r7qw?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1CT4y1g7bR?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1qT4y1J71C?spm_id_from=333.999.0.0)

```py
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
            if not node:
                return 0
            # if node.left:
            left = max(subsum(node.left), 0)     # 正负性：left 为负，就不回收
            # if node.right:
            right = max(subsum(node.right), 0)   # 正负性：right 为负，就不回收
            # 有两种情况：node.val 不往上回收, 左中右
            res = max(left + right + node.val, res)
            # 有两种情况：node.val 往上回收, 构成递归
            return max(left,right) + node.val # 正负性：node.val必须回收
        subsum(root)
        return res
```

```scala
object Solution1 {
    def maxPathSum(root: TreeNode): Int = {
        dfs(root)._1
    }

    def dfs(node: TreeNode): (Int, Int) = {
      if (node == null) return (Int.MinValue, 0)
      
      val (leftSoFar, leftEndingHere) = dfs(node.left)
      val (rightSoFar, rightEndingHere) = dfs(node.right)

      val maxSoFar = leftSoFar max rightSoFar max (node.value + leftEndingHere + rightEndingHere)

      val maxEndingHere = 0 max (node.value + (leftEndingHere max rightEndingHere))
      (maxSoFar, maxEndingHere)
    }
}
```

## 56. Merge Intervals

[花花酱](https://www.bilibili.com/video/BV11t411J7zV?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1w7411a7Wo?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1pV411a7t4?spm_id_from=333.999.0.0)

```py
class Solution:
    def merge(self, intervals: List[List[int]]) -> List[List[int]]:
        intervals.sort()
        # 等价于：intervals.sort(key = lambda x: x[0])
        res = []
        for interval in intervals:
            # res[-1] 和 interval 比较
            if not res or res[-1][1] < interval[0]:
                res.append(interval[:])
            else:
                res[-1][1] = max(res[-1][1],interval[1])
                # 易错点：不是interval[1]，而是max(res[-1][1],interval[1])
                # 比如，[[1,4],[2,3]]
        return res
```

```scala

/**
*  time complexity: O(nlogn) + O(n) = O(nlogn) 
*  space complexity: O(n): sorted array
*/

object Solution1-2 {
    def merge(intervals: Array[Array[Int]]): Array[Array[Int]] = {
      intervals.sortBy(_(0)).foldLeft(List.empty[Array[Int]]){
        case (last::ans, arr) =>
          if (last.last < arr.head) {
            arr::last::ans
          } else {
            Array(last.head, last.last max arr.last)::ans
          }
        case (ans, arr) => arr::ans // for empty ans list
      }.toArray
    }
}
```

## 剑指 Offer 22. 链表中倒数第k个节点

```py
栈
class Solution:
    def getKthFromEnd(self, head: ListNode, k: int) -> ListNode:
        stack = []
        while head:
            stack.append(head)
            head = head.next
        return stack[-k]

快慢指针
class Solution:
    def getKthFromEnd(self, head: ListNode, k: int) -> ListNode:
        slow, fast = head, head
        for i in range(k):
            fast = fast.next
        while fast:
            slow = slow.next
            fast = fast.next
        return slow

总长度减k
class Solution:
    def getKthFromEnd(self, head: ListNode, k: int) -> ListNode:
        node, listlen = head, 0  
        while node:
            node = node.next
            listlen += 1

        node = head
        for _ in range(listlen-k):
            node = node.next
        return node  
```

## 82. 删除排序链表中的重复元素 II(Remove Duplicates from Sorted List

https://leetcode-cn.com/problems/remove-duplicates-from-sorted-list-ii/

[洛阳](https://www.bilibili.com/video/BV1Fi4y187pj?spm_id_from=333.999.0.0)

```py
class Solution:
    def deleteDuplicates(self, head: ListNode) -> ListNode:
        if not head or not head.next:
            return head
        dummy = ListNode(0,head)
        cur = dummy
        while cur.next and cur.next.next:
            if cur.next.val == cur.next.next.val:
                # 把所有等于 x 的结点全部删除
                x = cur.next.val
                while cur.next and cur.next.val == x:
                    cur.next = cur.next.next
            else:
                cur =  cur.next
        return dummy.next
```

## 69 Sqrt(x) 见 HJ107 求解立方根

[花花酱](https://www.bilibili.com/video/BV1WW411C7YN?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1gJ411R7XR?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Yb411i7TN?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1PK411s72g?spm_id_from=333.999.0.0)

二分查找:

时间复杂度：O(logN)

空间复杂度：O(1)

```py
class Solution:
    def mySqrt(self, x: int) -> int:
        l, r = 0, x
        ans = -1
        while l <= r:
            mid = (l + r) // 2
            if mid * mid <= x: # 2*2=4
                ans = mid # ans 必须放置在这个位置
                l = mid + 1
            else:
                r = mid - 1
        return ans
```

牛顿迭代法:

时间复杂度：O(logN)

空间复杂度：O(1)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3g2xmodb40u0.png)

```py
class Solution:
    def mySqrt(self, x: int) -> int:
        if x <= 1:
            return x
        res = x # 初始值
        c = x # 牛顿迭代法中的常数
        while res > c / res:
            res = (res + c / res) // 2 # 这里必须用整除
        return int(res)
        
class Solution:
    def mySqrt(self, num: int) -> int:
        x = 1 # 背一背这个套路
        while abs(x**2 - num) > 0.001:
            x -= (x**2 - num) / (2 * x) # 注意这里是减号
        return floor(x)
```

```scala

/**
* Newton's method - iterative
* y = x^2 => f(x) = x^2 - y
* x_{k+1} = x_k - f(x_k) / f'(x_k)
* x_{k+1} = x_k - (x_k^2 - y) / (2x_k) = (x_k + y / x_k) / 2
* time complexity: O(logN)
*/

object Solution2 {
     def mySqrt(x: Int): Int = {
        val precision = math.pow(10, -5)
        
        var ans: Double = x
        while(math.abs(ans * ans - x) > precision){
            ans = (ans + x / ans) / 2
            // println(ans)
        }
        ans.toInt
    }
}


```

```scala
object Solution {
    def mySqrt(x: Int): Int = {
        if(x == 0){
            0
        }else if(x == 1){
            1
        }else{
            var start = 1
            var end = x
            var result = 0
            
            while(start <= end){
                var mid = start + (end - start)/2
                if(mid <= x/mid){
                    result = mid
                    start = mid+1
                }else{
                    end = mid-1
                }
            }
            result
        }
    }
}

```    

## 8. String to Integer(atoi)

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
import re

class Solution:
    def myAtoi(self, s: str) -> int:
        return max(min(int(*re.findall('^[\+\-]?\d+', s.lstrip())), 2**31 - 1), -2**31)

class Solution:
    def myAtoi(self, s: str) -> int:
        import re
        at_oi_re = re.compile('^[ ]*([+-]?\d+)')
        # 易错点：要注意中括号[]和小括号()的区别
        # 易错点：要注意小括号()的位置，小括号的作用是匹配并提取，所以+-要包括起来
        # 易错点：不能漏掉*？

        # 字符串的 开头 匹配 0个或多个[空格]
        # 匹配 0个或多个[+-]
        # 匹配 0个或多个[0-9]
        if not at_oi_re.search(s):
            return 0
        res = int(at_oi_re.findall(s)[0])
        # 易错点：findall返回一个列表，所以必须有[0]
        # 易错点：必须有int()
        #  在范围 [-2^31, 2^31 - 1] 内
        return min(max(res, -(1<<31)), (1<<31) - 1) # 在两者之间，背一背
        # 要加小括号(1<<31)

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
			if char < '0' or char > '9':
				break
		strNum *= flag
		return min(max(strNum, -(1<<31)), (1<<31) - 1) 
```

## 19-Remove Nth Node From End of List

[哈哈哈](https://www.bilibili.com/video/BV1Q7411V7DQ?spm_id_from=333.999.0.0)

[图灵](https://www.bilibili.com/video/BV1eL411n7KE?spm_id_from=333.999.0.0)

[洛阳](https://www.bilibili.com/video/BV1654y1R7Xe?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1KK4y1E7st?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Z5411c79y?spm_id_from=333.999.0.0)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6ccdr2kcw7c0.png)

```py
class Solution:
    def removeNthFromEnd(self, head: ListNode, n: int) -> ListNode:
        def getLength(head: ListNode) -> int:
            length = 0
            while head:
                length += 1
                head = head.next
            return length
        
        dummy = ListNode(0, head)
        length = getLength(head)
        cur = dummy
        for i in range(1, length - n + 1):
            cur = cur.next
        cur.next = cur.next.next
        return dummy.next


class Solution:
    def removeNthFromEnd(self, head: ListNode, n: int) -> ListNode:
        dummy = ListNode(0,head)
        slow = dummy
        fast = head
        for _ in range(n):
            fast = fast.next
        while fast:
            fast = fast.next
            slow = slow.next

        slow.next = slow.next.next

        return dummy.next
```

```scala
/**
* my first commitment - fast & slow pointer
* time complexity O(N + N / 2)
*   1. keep fast pointer is n + 1 ahead to slow pointer
*   2. if fast == null, slow pointer would points to the  preNode of target removing node
*           t 
*   0 1 2 3 4 5
*   s     f
*     s     f
*       s     f
*         s     f
*/
object Solution1-2 {
    def removeNthFromEnd(head: ListNode, n: Int): ListNode = {
      val dummyHead = ListNode(0, head)
      var slow = dummyHead
      var fast = dummyHead
      
      for (i <- 0 until (n + 1) if fast != null) {
        fast = fast.next
      }
      
      while(fast != null) {
        slow = slow.next
        fast = fast.next
      }
      
      slow.next = slow.next.next
      dummyHead.next
    }
  
}
```

## 2. Add Two Numbers

[花花酱](https://www.bilibili.com/video/BV1EJ411h72z?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1gJ411V7gJ?spm_id_from=333.999.0.0)

[小梦想](https://www.bilibili.com/video/BV1Wb411e77s?spm_id_from=333.999.0.0)

[洛阳](https://www.bilibili.com/video/BV1rZ4y1j7V3?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1DA411L7YQ?spm_id_from=333.999.0.0)

* 时间复杂度:O(max(m,n))

* 时间复杂度:O(max(m,n))

特殊情况：

两个链表的长度不同。

进位

```py

class Solution:
    def addTwoNumbers(self, l1: ListNode, l2: ListNode) -> ListNode:
        dummy = cur = ListNode(0) # 易错点：定义一个dummy和一个pointer，都指向ListNode(0)
        carry = 0 # 易错点：carry需要先赋值
        while l1 or l2 or carry: # 易错点：carry要存在
            # 易错点：l1,l2不一定存在，所以不能写成：sumNode = l1 + l2
            # 易错点：调用listnode要有.val
            sumNode = (l1.val if l1 else 0) + (l2.val if l2 else 0) + carry
            tail = sumNode % 10
            carry = sumNode // 10
            cur.next = ListNode(tail)
            cur = cur.next
            # # l1,l2不一定存在，所以不能写成：l1 = l1.next
            l1 = l1.next if l1 else None
            l2 = l2.next if l2 else None
        return dummy.next
```

```scala
object Solution {
    def addTwoNumbers(l1: ListNode, l2: ListNode): ListNode = {
      var cur1 = l1
      var cur2 = l2
      val dummy = ListNode(0)
      var prev=dummy
      var carry = 0
      while (cur1!=null ||  cur2!=null || carry !=0) {
        val (s1,next1) = cur1 match {
          case null => (0,null)
          case _=> (cur1.x, cur1.next)
        }
        val (s2,next2) = cur2 match {
          case null => (0,null)
          case _=> (cur2.x,cur2.next)
        }
        val s = s1+s2+carry
        val node = ListNode(s % 10)
        prev.next = node
        prev=node
        carry=s/10
        cur1 = next1
        cur2=next2
      }
      dummy.next
    }
  }
```

## 148. Sort List

[花花酱](https://www.bilibili.com/video/BV1jW411d7z7?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1VK411A7Gm?spm_id_from=333.999.0.0)

```py
class Solution:
    def sortList(self, head: ListNode) -> ListNode:
        dummy = ListNode(-1, head)
        sortlist = []
        # 先把链表断开
        while head:
            aft = head.next
            head.next = None
            sortlist.append(head)
            head = aft
        # 排序
        sortlist = sorted(sortlist, key=lambda x: x.val)
        # 把链表串联起来
        n = len(sortlist)
        if n == 0:
            return None
        dummy.next = sortlist[0]
        for i in range(n-1):
            sortlist[i].next = sortlist[i+1]
        
        return dummy.next
```

```py
# py3 归并排序，递归实现。空间复杂度主要在递归栈深度：O( log(n) )，整个递归过程有点像后序遍历

class Solution:
    def sortList(self, head: ListNode) -> ListNode:
        if not head or not head.next:
            return head
        mid = self.findmid(head)
        left = head # 指定左右
        right = mid.next # 指定左右
        mid.next = None # 断开链接
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
                l = l.next # 下一个
            else:
                cur.next = r
                r = r.next # 下一个
            cur = cur.next # 下一个
        cur.next = l or r
        return dummy.next

        # 基本用法：
        # v = p1 or p2

        # 它完成的效果等同于：
        # if p1:
        #     v = p1
        # else:
        #     v = p2
```

## 72. Edit Distance 72-编辑距离

[花花酱](https://www.bilibili.com/video/BV1cb411u7uX?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1wv411P7aQ?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV13Z4y1W7UB?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1ea4y147FK?spm_id_from=333.999.0.0)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5kci5ryyi3k0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.7fq2ehol7rg0.png)

```py
class Solution:
    def minDistance(self, word1: str, word2: str) -> int:
        len1 = len(word1)
        len2 = len(word2)

        DP = [[0 for _ in range(len2 + 1)] for _ in range(len1 + 1)]
        
        for i in range(0, len1 + 1):
            for j in range(0, len2 + 1):
                if i == 0:               # 初始化
                    DP[i][j] = j
                elif j == 0:             # 初始化
                    DP[i][j] = i
                elif word1[i - 1] == word2[j - 1]:
                    DP[i][j] = DP[i-1][j-1]
                else:
                    DP[i][j] = min(DP[i-1][j], DP[i][j-1], DP[i-1][j-1]) + 1
                    
        return DP[-1][-1]
```

```py
class Solution:
    def minDistance(self, word1: str, word2: str) -> int:
        @cache
        def dp(i, j) -> int:
            if i == -1:
                return j + 1
            if j == -1:
                return i + 1
            # 做出选择
            if word1[i] == word2[j]:
                return dp(i - 1, j - 1) # 什么都不做
            else:
                return min(
                    dp(i, j-1) + 1,  # insert
                    dp(i-1, j) + 1,  # delete
                    dp(i-1, j-1) + 1 # replace
                )
        return dp(len(word1)-1, len(word2)-1)
```

```scala
/**
* dynamic programming  - Levenshtein distance
* memo
*    1. dp(i)(j) represent the minimum edit distance from the length i substring from word1 to the length j substring from word2
*    2. dp(i)(j) is solved by its sub-optimal problem 
*         1, delete op: dp(i -1)(j)
*         2. replacement op: dp(i -1)(j - 1)
*         3. insertion op: dp(i)(j - 1)
* time complexity: O(NM) N is the length of word1, N is the length of word2
* space complexity: O(NM)
*/
object Solution1 {
  def minDistance(word1: String, word2: String): Int = {
    val m = word1.length
    val n = word2.length
    /* initial  Levenshtein distance table 
    * dp(i)(j) represent the minimum distance transforming from length i of substring word1 to length j of substring word2
    */
    val dp = Array.tabulate(m + 1, n + 1) {
      case (0, j) => j
      case (i, 0) => i
      case _ => 0
    }

    for (i <- 1 to m; j <- 1 to n) {
      /* i-1 is word1 index, j-1 is word2 index */
      if (word1(i - 1) == word2(j - 1)) {
        // do nothing case
        dp(i)(j) = dp(i - 1)(j - 1)
      } else {
        /**
        *       i-1,    i
        * j-1 replace  insertion     
        *  j   delete  dp(i)(j)
        */
        val replace = dp(i - 1)(j - 1)
        val insert = dp(i)(j - 1)
        val delete = dp(i - 1)(j)
        dp(i)(j) = (replace min insert min delete) + 1
      }
    }
    dp(m)(n)
  }
}
```

## 4. 寻找两个正序数组的中位数 Median of Two Sorted Arrays

[官方](https://www.bilibili.com/video/BV1Xv411z76J?spm_id_from=333.999.0.0)

```py
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

```

## 105-从前序与中序遍历序列构

[哈哈哈](https://www.bilibili.com/video/BV1uv411B73D?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1x54y1d7e8?spm_id_from=333.999.0.0)

[图灵](https://www.bilibili.com/video/BV1ry4y1U7ZR?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV14A411q7Nv?spm_id_from=333.999.0.0)

> PYTHON 递归

```py
class Solution:
    def buildTree(self, preorder, inorder):
        if inorder:
            root = TreeNode(preorder.pop(0)) # preorder 在这里的作用就是 pop(0)
            i = inorder.index(root.val)
            root.left = self.buildTree(preorder, inorder[: i])
            root.right = self.buildTree(preorder, inorder[i + 1:])
            return root

```

## 151. Reverse Words in a String

[小梦想家](https://www.bilibili.com/video/BV1Yb411i7g4?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1rT4y1g7AJ?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Ei4y1V7yA?spm_id_from=333.999.0.0)

```py
class Solution:
    def reverseWords(self, s: str) -> str:
        return " ".join(reversed(s.split()))
```

```py
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

## 104-Maximum Depth of Binary

[哈哈哈](https://www.bilibili.com/video/BV1AJ411Q7xG?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Wb411e7eK?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1u54y1D7Nx?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1tK41137GM?spm_id_from=333.999.0.0)

```py
class Solution:
    def maxDepth(self, root: TreeNode) -> int:
        if not root:
            return 0
        return max(self.maxDepth(root.left),self.maxDepth(root.right))+1
```

```scala
object Solution1 {
    def maxDepth(root: TreeNode): Int = {
        if (root == null) return 0
        math.max(maxDepth(root.left), maxDepth(root.right)) + 1
    }
}

object Solution {
    def maxDepth(root: TreeNode): Int = root match {
        case null => 0
        case x: TreeNode => Math.max((1 + maxDepth(x.left)), (1 + maxDepth(x.right)))
    }
}

```

## 76-【滑动窗口🔹】最小覆盖子串

[哈哈哈](https://www.bilibili.com/video/BV1PM4y1K7p6?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1aK4y1t7Qd?spm_id_from=333.999.0.0)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1ud8tslp4vz4.png)

```py
class Solution:
    def minWindow(self, s: str, t: str) -> str:
    
        def isContains(windic,targetdic):
            for key in targetdic:
                if windic[key] < targetdic[key]:
                    return False # 只要有一个不满足，则不满足
            return True

        tdic = defaultdict(int) # 固定的
        wdic = defaultdict(int) # 变动的
        for char in t:
        	tdic[char] += 1

        minlen = len(s)
        l = 0
        res = ''

        for r in range(len(s)): # 扩展右边界
            if s[r] in tdic:
                wdic[s[r]] += 1
            while isContains(wdic,tdic):
                # 如果是 minWindow
                if r-l+1 <= minlen:
                    minlen = r-l+1
                    res = s[l:r+1]
                # 收缩左边界
                if s[l] in wdic:
                    wdic[s[l]] -= 1
                l += 1   
        return res
```

```scala
/**
* chosen solution
*   time complexity: O(|S| + |T|)
*   space complexity: O(|s| + |T|)

*/
object Solution1 {
  def minWindow(s: String, t: String): String = {

    var left = 0
    val tMap = t.groupBy(identity).mapValues(_.length).toMap

    val budgetMap = scala.collection.mutable.Map() ++ tMap
    var currentString = ""
    var answer = ""

    for (char <- s) {
        budgetMap.get(char) match {

          case Some(e) => budgetMap.update(char, e - 1)
          case None =>
        }
      
      currentString += char

      while(!budgetMap.exists{case (_, v) => v > 0}) {

        val tempChar = s(left)
        if(tMap.contains(tempChar)){
          budgetMap.update(tempChar, budgetMap.getOrElse(tempChar, 0) + 1)
        }

        if(answer.length > currentString.length || answer.isEmpty) {
          answer = currentString
        }
        currentString = currentString.drop(1)
        left += 1
      }
    }

    answer
  }
}

```

## 31 ★ Next Permutation

[小明](https://www.bilibili.com/video/BV1Uz4y1m72N?spm_id_from=333.999.0.0)

[图灵](https://www.bilibili.com/video/BV1SK4y1V7ch?spm_id_from=333.999.0.0)

```py
class Solution:
    def nextPermutation(self, nums: List[int]) -> None:
        # 关键在于从后往前，找到非递减序列
        i = len(nums) - 2
        while i >= 0:
            if nums[i] >= nums[i+1]:
                i -= 1
            else:
                # 寻找i后面比i大的数，交换位置,并且排序
                for j in range(len(nums)-1,i,-1): # 易错点:len(nums)-1,i的区间
                    # 12(3)5(4)
                    if nums[j] > nums[i]:
                        nums[i],nums[j] = nums[j],nums[i]
                        nums[i+1:] = sorted(nums[i+1:])
                        return
        nums.reverse() # 易错点:对于[3,2,1]这种情况，i = 0
```

```scala
/**
* my first commitment
* memo
* 1. find the first index i which breaks the increasing order
* 2. find the last index  j which is larger than index i
* 3. swap(i, j)
* 4. sorting: reverse sequence from i + 1 to the end 
* time complexity: O(n)
*/

object Solution1 {
    def nextPermutation(nums: Array[Int]): Unit = {
        /**
        * find the first index i which breaks the increasing order
        * 0 1 2 3 4 5 6
        * 5 4 7 6 5 4 3
        *   i     j 
        */
      ((nums.length - 2) to 0 by -1).find(idx => nums(idx) < nums(idx + 1)) match {
        case Some(idx) => 
          /* 
          * find the last index  j which  is larger than index i
          */
          val j = ((idx + 1) until nums.length).findLast(i => nums(idx) < nums(i)).getOrElse(idx)
          swap(nums, idx, j)
          reverse(nums, idx + 1, nums.length - 1)
        case None => reverse(nums, 0, nums.length - 1)
      }
    }
    @annotation.tailrec
    def reverse(nums: Array[Int], from: Int, to: Int) {
      if (from < to) {
        swap(nums, from, to)
        reverse(nums, from + 1, to - 1)
      }
    }
  
    def swap(nums: Array[Int], index1: Int, index2: Int) {
      val tmp = nums(index2)
      nums(index2) = nums(index1)
      nums(index1) = tmp
    }
}


```

## 239. ★【最小堆🌵 + 滑动窗口🔹单调队列】Sliding Window Maximum

#### 不类似567，567类似187

[花花酱](https://www.bilibili.com/video/BV1WW411C763?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Bf4y1v758?spm_id_from=333.999.0.0)

```py
思路：

维护：最接近右边的最大值的pos
        
# print(winpos)
# [1,3,-1,-3,5,3,6,7]
保证窗口内的值是递减的即可
# []
# [0]
# [1]
# [1, 2]
# [1, 2, 3]
# [4]
# [4, 5]
# [6]

class Solution:
    def maxSlidingWindow(self, nums: List[int], k: int) -> List[int]:
        winQ = deque()
        res = []
        for r, v in enumerate(nums):
            # 如果新来的数字更大, 所以最右边的数字是最大的
            while winQ and nums[winQ[-1]] < v:
                winQ.pop()
            winQ.append(r)
            # 如果出界
            l = winQ[0]
            if r - k == l:
                winQ.popleft()
            # 开始写入答案
            if r >= k - 1:
                res.append(nums[winQ[0]])

        return res
```

```py
class Solution:
    def maxSlidingWindow(self, nums: List[int], k: int) -> List[int]:
        n = len(nums)
        # 注意 Python 默认的优先队列是小根堆
        q = [(-nums[i], i) for i in range(k)]
        heapq.heapify(q)

        res = [-q[0][0]]
        for i in range(k, n):
            heapq.heappush(q, (-nums[i], i))
            while q[0][1] <= i - k: # 最大值永远在 q[0]
                heapq.heappop(q) # 把所有出界的最大值弹出
            res.append(-q[0][0])
        
        return res

```

```scala

/**
* using max heap, may not AC
* pq = pq.filter{case (_v: Int, _idx: Int) => (_v >= v) && (_idx > idx - k)} : keep element's time complexity is O(K)
* time complexity: O(N log K)
*/

object Solution1 {
    def maxSlidingWindow(nums: Array[Int], k: Int): Array[Int] = {
        var pq = scala.collection.mutable.PriorityQueue.empty[(Int, Int)](Ordering.by(p  => p._1))
        val rest = scala.collection.mutable.ArrayBuffer[Int]()
        
        nums.zipWithIndex.foreach{case (v: Int, idx: Int) => {
     
            pq += ((v, idx))
            
            /* keep the elements that is only larger than newest v and the nearest k */
            pq = pq.filter{case (_v: Int, _idx: Int) => (_v >= v) && (_idx > idx - k)}       

            if (idx + 1 >= k) {
                rest += pq.head._1
            }
          
        }}        
        rest.toArray
    }
}

/**
* using scala vector, due to scala vector is immutable, any operation about add update remove is generate a new vector
* so it's not a proper substitute for deque
*/

object Solution2 {
  def maxSlidingWindow(nums: Array[Int], k: Int): Array[Int] = {
    var windows = Vector.empty[Int]
    val ret = scala.collection.mutable.ArrayBuffer.empty[Int]

    nums.zipWithIndex.foreach { case (value: Int, index: Int) =>
      if (index >= k && windows.head <= index - k)
        windows = windows.drop(1)

      while (windows.nonEmpty && nums(windows.last) <= value){
        windows = windows.dropRight(1)
      }
      windows = windows :+ index
      if (index + 1 >= k) {
        ret += nums(windows.head)
      }
    }
    ret.toArray
  }
}



```

## 1143 【二维动态🚀规划】Longest Common Subsequence

####  1.245.1. <a name='516'></a>类似题目：516最长回文🌈子序列

[小明](https://www.bilibili.com/video/BV19Z4y1W7Xi?spm_id_from=333.999.0.0)

```py
做了几个dp的题之后，总结了dp需要注意的几个要素：

1、 明确dp二维数组表示的含义

2、 base case

3、 状态的转移：对于`回文🌈/LCS`之类的问题则是考虑当前字串和已经计算过的子串之间的关系

4、 由`状态的转移`来确定 loop的边界

5、 由loop的边界`打出表格` 可得出最后一个dp的状态值，即结果。


class Solution:
    def longestCommonSubsequence(self, text1: str, text2: str) -> int:
        dp = [[0] * (len(text2)+1) for _ in range(len(text1)+1)]
        for i in range(1, len(text1)+1): 
            for j in range(1, len(text2)+1): 
                if text1[i-1] == text2[j-1]: 
                    dp[i][j] = dp[i-1][j-1] + 1 
                else: 
                    dp[i][j] = max(dp[i-1][j], dp[i][j-1])
        return dp[-1][-1]


class Solution:
    def longestCommonSubsequence(self, text1: str, text2: str) -> int:
        n1, n2 = len(text1), len(text2)
        pre = [0 for _ in range(n2 + 1)]
        dp = [0 for _ in range(n2 + 1)]
        for i in range(n1):
            for j in range(1, n2 + 1):
                if text1[i] == text2[j-1]:
                    dp[j] = pre[j-1] + 1
                else:
                    dp[j] = max(pre[j], dp[j-1])
                pre[j-1] = dp[j-1] # 注意这里的缩进关系
            pre[j] = dp[j]
        return dp[-1]
```



```scala


  object Solution {
    def longestCommonSubsequence(text1: String, text2: String): Int = {
      val m = text1.length
      val n = text2.length
      //val dp = Array.ofDim[Int](1001,1001)
      val dp = Array.fill(1001,1001)(0)
      for (i<- 1 to m) { // must have space?
        for (j<- 1 to n) {
          dp(i)(j) = if (text1(i-1)== text2(j-1)) dp(i-1)(j-1)+1 else Math.max(dp(i-1)(j),dp(i)(j-1))
        }
      }
      dp(m)(n)
    }
  }

  class Test extends BaseExtension {
    def init {
      println(Solution.longestCommonSubsequence("abcde", "ace") == 3)
    }
    val name = "1143 Longest common sequence"
  }

```

## 129 Sum Root to Leaf Numbers

[小明](https://www.bilibili.com/video/BV1VK411H7o5?spm_id_from=333.999.0.0)

```py
class Solution:
    def sumNumbers(self, root: TreeNode) -> int:
        res = 0
        
        def dfs(root, acc):
            nonlocal res
            if not root.left and not root.right: # 易错点：不要忽视了这种情况
                res += acc * 10 + root.val
                return
            if root.left:
                dfs(root.left, acc * 10 + root.val)
            if root.right:
                dfs(root.right, acc * 10 + root.val)
        dfs(root, 0)
        return res # 在根节点处cur为0，而不是sums

```

## 93. 复原 IP 地址

```py
class Solution:
    def restoreIpAddresses(self, s: str) -> List[str]:
        res = []
        def backtrack(s,path):
            if len(path) == 4 and len(s) == 0:
                res.append('.'.join(path))
                return # 注意点：一定要返回
            for i in range(len(s)):
                left,right = s[:i+1],s[i+1:]
                if 0 <= int(left) <= 255 and str(int(left)) ==  left:
                    backtrack(right,path + [left])  
        backtrack(s,[])    
        return res


```

## 110-Balanced Binary Tree

[哈哈哈](https://www.bilibili.com/video/BV1NJ411v7b1?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Wb411e7Lb?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1sV411b7hR?spm_id_from=333.999.0.0)

```py
class Solution:
    def isBalanced(self, root: TreeNode) -> bool:
        def height(root: TreeNode) -> int:
            if not root:
                return 0
            return max(height(root.left), height(root.right)) + 1

        if not root:
            return True
        return abs(height(root.left) - height(root.right)) <= 1 and self.isBalanced(root.left) and self.isBalanced(root.right)
        # 注意：左右两个子树也必须balanced
```

## 113. 二叉树中和为某一值的路径

[哈哈哈](https://www.bilibili.com/video/BV1P54y1i73U?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1k54y177fu?spm_id_from=333.999.0.0)

```py
class Solution:
    def pathSum(self, root: Optional[TreeNode], targetSum: int) -> List[List[int]]:
        res=[]

        def dfs(node,path,tsum): # node.val == tsum 结束

            if not node:
                return

            if node.val == tsum and not node.left and not node.right: # 结束条件
                res.append(path[:] + [node.val])  # 需要深拷贝

            dfs(node.left, path + [node.val], tsum - node.val) # 三个部分都需要状态转移
            dfs(node.right, path + [node.val], tsum - node.val)
            
        dfs(root, [], targetSum)
        return res
```

## 22. Generate Parentheses

[小梦想家](https://www.bilibili.com/video/BV1hb411i7t7?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1vK4y1b744?spm_id_from=333.999.0.0)

回溯法：

* 时间复杂度:O($\frac{4^n}{\sqrt{n}}$)

* 时间复杂度:O($\frac{4^n}{\sqrt{n}}$)

<img src="https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.ud5vx6kpbvk.png" width="50%">

```py
class Solution:
    def generateParenthesis(self, n: int) -> List[str]:

        def helper(left,right,itm):
            if left == 0 and right == 0:
                res.append(itm)
                return  # 这里return写不写居然都ac了，可能是因为没有循环吧
            if left > 0:
                helper(left-1,right,itm + '(') #   状态转移
            if right > left:
                helper(left,right-1,itm + ')')
        
        res = []
        helper(n,n,'')
        return res
```


```py
# 我的模仿😐
class Solution:
    def generateParenthesis(self, n: int) -> List[str]:
        # 这是我写的愚蠢的结束条件：
        # if len(res[0]) == n:
        #     return
        if n == 1:
            return ['()']

        res = set()
        for itm in self.generateParenthesis(n-1):
            for j in range(len(itm) + 1): # 如果item的长度为4，那么就有5个可以插入的位置
                # 错误写法：
                # itm = itm[:j] + '()' + itm[j:]
                # res = res.add(itm)
                # 错误写法：
                # res = res.add(itm[:j] + '()' + itm[j:])
                # 正确写法：
                res.add(itm[:j] + '()' + itm[j:])
        return list(res)

# 相当于比上一层少了一层循环。
# 不懂是不是动态规划，每新增一对括号，
# 就是在上一次的结果的各个位置插入一个"()"，用集合防止重复

class Solution:
    def generateParenthesis(self, n):
        res = {''}
        for i in range(n):
            tmp = set()
            for s in res:  # 在上一次的结果的所有字符串的各个位置上插入'()'
                for j in range(len(s) + 1):
                    tmp.add(s[:j] + '()' + s[j:])
            res = tmp
        return list(res)



```

```scala

object Solution {
    
    def generateParenthesis(n: Int): List[String] = {
        import scala.collection.mutable._
        def backtrack(acc: ListBuffer[String], curr: String, left: Int, right: Int): Unit = {
            if (left == 0 && right == 0) acc.append(curr)
            else {
                if (left > 0) backtrack(acc, curr + "(", left-1, right)
                if (right > left) backtrack(acc, curr + ")", left, right-1)
            }
        }
      
        val acc = ListBuffer[String]()
        backtrack(acc, "", n, n)
        acc.toList
    }
}

```

## 41 First Missing Positive

[小明](https://www.bilibili.com/video/BV1fy4y1k7pV?spm_id_from=333.999.0.0)

```py
置换法
class Solution:
    def firstMissingPositive(self, nums: List[int]) -> int:
        n = len(nums)
        for i in range(n):
            while 1 <= nums[i] <= n and nums[nums[i] - 1] != nums[i]:
                nums[nums[i] - 1], nums[i] = nums[i], nums[nums[i] - 1]
        for i in range(n):
            if nums[i] != i + 1:
                return i + 1
        return n + 1

```

## 543 Diameter of Binary Tree

[小明](https://www.bilibili.com/video/BV12K4y1r78T?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1qA411t7LR?spm_id_from=333.999.0.0)

```py
class Solution:
    def diameterOfBinaryTree(self, root: TreeNode) -> int:
        res = 0
        def depth(node):
            nonlocal res
            if not node:
                return 0
            L = depth(node.left) + 1 if node.left else 0 # 注意：这里一定要用 if else 结构
            R = depth(node.right) + 1 if node.right else 0
            res = max(res, L + R)
            return max(L, R)

        depth(root)
        return res
```

## 155-【构造🏰】Min Stack

[哈哈哈](https://www.bilibili.com/video/BV1H74118748?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1YK4y1r77W?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1ja4y1Y7vY?spm_id_from=333.999.0.0)

   
关键在于  def getMi

```py
class MinStack:

    def __init__(self):
        # 另外用一个stack，栈顶表示原栈里所有值的最小值
        self.minStack = []
        self.stack = []

    def push(self, val: int) -> None:
        self.stack.append(val)
        if self.minStack == [] or self.minStack[-1] >= val:
            self.minStack.append(val)

    def pop(self) -> None:
        if self.stack[-1] == self.minStack[-1]:
            self.minStack.pop()
        return self.stack.pop()

    def top(self) -> int:
        return self.stack[-1]


    def getMin(self) -> int:
        return self.minStack[-1]        
```

```py
面试的时候被问到不能用额外空间，就去网上搜了下不用额外空间的做法。思路是栈里保存差值。

class MinStack:
    def __init__(self):

        self.diffstack = []
        self.mins = -1

    def push(self, x: int) -> None:
        if not self.diffstack:
            self.diffstack.append(0)
            self.mins = x
        else:
            diff = x-self.mins
            self.diffstack.append(diff)
            self.mins = self.mins if diff > 0 else x
            # mins 是会变化的

    def pop(self) -> None:
        if self.diffstack:
            diff = self.diffstack.pop()
            if diff < 0: 
                # [3,2,1,4] [0,-1,-1, 3]
                # mins = 3, 2, 1, 1
                top = self.mins # 第一步：顺序不能错
                self.mins = self.mins - diff # 第二步：如果 diff < 0, 那就需要还原 self.mins
            else:     # 如果 diff 一直都 > 0, 那就非常好
                top = self.mins + diff
            return top

    def top(self) -> int:
        return self.mins if self.diffstack[-1] < 0 else self.diffstack[-1] + self.mins

    def getMin(self) -> int:
        return self.mins if self.diffstack else -1
```


```scala
class MinStack() {

    /** initialize your data structure here. */
    var stack = List.empty[Int]
    var min = Int.MaxValue

    def push(x: Int) {
        stack = stack :+ x
        if(x < min){
            min = x
        }
    }

    def pop() {
        stack = stack.init
        min = Int.MaxValue
        stack.map(x => {
            if(x < min) min = x
        })
    }

    def top(): Int = {
        stack.last
    }

    def getMin(): Int = {
        min
    }

}

//替代解决方案：更快
//这里我们将元素添加到列表中而不是附加
//请注意，由于List实际上是一个LinkedList，因此处理列表的“头部”要容易得多
//还有另一个列表来维护列表的最小元素
class MinStack() {

    /** initialize your data structure here. */
    var stack = List.empty[Int]
    var mins = List.empty[Int]

    def push(x: Int) {
        //如果我们将第二个条件设为 x < mins.head，则此行失败
        //with NoSuchElementException: 空列表的头部
        //为什么？？？
        if(mins.isEmpty || mins.head >= x) mins = x +: mins
        stack = x +: stack
    }

    def pop() {
        if(mins.head == stack.head) mins = mins.tail
        stack = stack.tail
    }

    def top(): Int = {
        stack.head
    }

    def getMin(): Int = {
        mins.head
    }

}

```

## 98. Validate Binary Search Tree 98-验证二叉搜索树

[花花酱](https://www.bilibili.com/video/BV12t411Y7TP?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1Wz4y1R7dF?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Wb411e7FV?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Hv411478d?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1Fi4y147Ng?spm_id_from=333.999.0.0)

```py
有效 二叉搜索树定义如下：

节点的左子树只包含 小于 当前节点的数。
节点的右子树只包含 大于 当前节点的数。
所有左子树和右子树自身必须也是二叉搜索树。
```

中序遍历一下就行了

```py
class Solution:
    def isValidBST(self, root: TreeNode) -> bool:
        result = [float('-inf')]
        valid = True # 必须用valid这个变量，不能用return False

        def traversal(root: TreeNode):
            nonlocal valid # 这一行必不可少，不然虽然不报错，但不能ac
            if root == None:
                return
            traversal(root.left)    # 左
            if result[-1] >= root.val: valid = False
            result.append(root.val) # 中序
            traversal(root.right)   # 右

        traversal(root)
        return valid


class Solution:
    def isValidBST(self, root: TreeNode) -> bool:
        def appendAllLeft(node):
            while node:
                stack.append(node)
                node = node.left

        stack, res = [], float('-inf')
        appendAllLeft(root)
        while stack:
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
        def BFS(node, lower, upper):
            if not node:
                return True
            return lower < node.val < upper and BFS(node.left, lower, node.val) and BFS(node.right, node.val, upper)

        return fun(root, float('-inf'), float('inf'))
```

```scala
/**
* chosen solution
* inorder iterative version only keep pre node
* this is also the inorder-iterative-template
* 
* time complexity: O(N)
*/

object Solution0 {
   def isValidBST(root: TreeNode): Boolean = {
    val stack = new collection.mutable.Stack[TreeNode]()
    var node = root
    var pre: TreeNode = null
    var result = true
    while ((node != null || stack.nonEmpty) && result) {
      while (node != null) {
        stack push node
        node = node.left
      }

      node = stack.pop
      if (pre != null && node.value <= pre.value) result = false
      pre = node
      node = node.right

    }
    result
  }
}

/**
* inorder recursive traversal
* memo:
*    1. recursive version with all element storing
* Time complexity O(NlogN)  there are a distinct and sorted operation
* space complexity O(N)
*/
object Solution1 {
  def isValidBST(root: TreeNode): Boolean = {
    val inorder = traversal(root)
    inorder equals inorder.distinct.sorted // why distinct here? [1, 1] is not a BST because left tree should be smaller than root. 
  }
  def traversal(node: TreeNode): List[Int] = {
    if(node == null){
      List.empty[Int]
    }else {
      // (traversal(node.left) :+ node.value) ::: traversal(node.right) 
      traversal(node.left) ::: List(node.value) ::: traversal(node.right)
    }
  }
}



/**
* giving min max range when recursive
* time complexity: O(N)
*/

object Solution4 {
  def isValidBST(root: TreeNode): Boolean = {

    def _isValidBST(node: TreeNode, min: TreeNode, max: TreeNode): Boolean = {

      if(node == null) true
      else {
        if((min != null && node.value <= min.value) || (max != null  && node.value >= max.value)) false
        else {
          _isValidBST(node.lefmt, min, node) && _isValidBST(node.right, node, max)
        }
      }
    }
    _isValidBST(root, null, null)
  }

}
```

## 470. Implement Rand10() Using Rand7()

[花花酱](https://www.bilibili.com/video/BV1Ut411Z7KX?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1AD4y1m7Qb?spm_id_from=333.999.0.0)

```py
class Solution:
    def rand10(self) -> int:
        while True:
            row = rand7()
            col = rand7()
            idx = (row - 1) * 7 + col #（0-42） + （1-7）
            if idx <= 40: # 1-40
                return 1 + (idx - 1) % 10

这样写也是对的，因为 0-9 等概率出现
class Solution:
    def rand10(self):
        while True:
            row = rand7()
            col = rand7()
            idx = (row - 1) * 7 + col #（0-42） + （1-7）
            if idx <= 30: # 1-40
                return 1 + (idx + 1) % 10

class Solution:
    def rand10(self):
        while True:
            res = (rand7()-1)*7 + rand7()#构造1~49的均匀分布
            if res <= 40: #剔除大于40的值，1-40等概率出现。
                break
        return res%10+1 #构造1-10的均匀分布
```

## 101-Symmetric tree

[哈哈哈](https://www.bilibili.com/video/BV1VJ41197KD?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Wb411e7eb?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1xv41167z8?spm_id_from=333.999.0.0)

> Python 迭代：其实就是层序遍历，然后检查每一层是不是回文🌈数组

```py
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

class Solution:
    def isSymmetric(self, root: TreeNode) -> bool:
        # if not root:
        #     return [] 删除
        level = [root]
        while level:
            tmp = []
            vals = [] # 补充
            for n in level:
                if n: # 修改，因为none节点也需要append
                    tmp.append(n.left) # if n.left 被删除
                    tmp.append(n.right) #  if n.right 被删除
                    vals.append(n.val)  # 补充
                else:
                    vals.append(None) # 修改，因为none节点也需要append
            if vals != vals[::-1]:  # 补充
                return False  # 补充
            level = tmp
        return True
```

> Python 递归：

```py
class Solution:
    def isSymmetric(self, root: TreeNode) -> bool:
        def twoSym(node1, node2):
            if node1 and node2 and node1.val == node2.val: 
                return twoSym(node1.left, node2.right) and twoSym(node1.right, node2.left)
            elif not node1 and not node2:
                return True
            else:
                return False
        return twoSym(root.left, root.right)
```

> scala:

```scala
/**
 * Definition for a binary tree node.
 * class TreeNode(_value: Int = 0, _left: TreeNode = null, _right: TreeNode = null) {
 *   var value: Int = _value
 *   var left: TreeNode = _left
 *   var right: TreeNode = _right
 * }
 */
object Solution {
    
    def symmetric(nodeA: TreeNode, nodeB: TreeNode): Boolean = {
        if(nodeA == null && nodeB == null){
            true
        }else if(nodeA !=null && nodeB != null){
            if(nodeA.value != nodeB.value){
                false
            }else{
                symmetric(nodeA.left, nodeB.right) && symmetric(nodeA.right, nodeB.left)
            }
        }else{
            false
        }
    }
    
    def isSymmetric(root: TreeNode): Boolean = {
        if(root == null){
            true
        } else{
            symmetric(root.left, root.right)
        }
    }
}

```

## 32 Longest Valid Parentheses

[小明](https://www.bilibili.com/video/BV1RZ4y1F7nJ?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1yi4y1G74d?spm_id_from=333.999.0.0)

动态规划：

* 时间复杂度: O(n) 

* 空间复杂度: O(n)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6dkova4yjvk0.png)

```py
# 背一背吧，好难。
class Solution:
    def longestValidParentheses(self, s: str) -> int:
        n = len(s)
        dp = [0]*n
        if n == 0: return 0
        for i in range(n):
            if s[i] == ')' and s[i-dp[i-1]-1] == '(' and i - dp[i-1] - 1 >= 0:
                dp[i] = 2 + dp[i-1] + dp[i-dp[i-1]-2]
        return max(dp)
```

栈：

* 时间复杂度: O(n) 

* 空间复杂度: O(n)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1dgqk0ervhb4.png)

```py
class Solution:
    def longestValidParentheses(self, s: str) -> int:
        stack = [-1]
        length = maxlength = 0
        for i,c in enumerate(s):
            if c == '(':
                stack.append(i)
            if c == ')':
                stack.pop()
                if not stack:
                    stack.append(i)
                else:
                    length = i - stack[-1] # stack[-1]为')'，断开区间
                    maxlength = max(maxlength,length)
        return maxlength

```

```scala



/**
* using stack to record the char index in oder to calculate the valid length
* memo:
* 1. always only have one invalid symbol at stack and its position index is 0
* time complexity O(n)
* space complexity O(n)
*/
object Solution1 {

  import collection.mutable

  def longestValidParentheses(s: String): Int = {
    val mapping = Map('(' -> ')')
    val stack = mutable.Stack[Int]()
    stack.push(-1)
    s.indices.foldLeft(0) {
      case (maxLength, idx) =>
        val char = s(idx)
        if (mapping.contains(char)) {
          stack push idx
          maxLength
        } else {
          stack.pop()
          if (stack.isEmpty) {
            stack push idx
            maxLength
          } else {
            (idx - stack.head) max maxLength
          }
        }
    }
  }
}
```

## 43. 字符串相乘

```py
class Solution:
    def multiply(self, num1: str, num2: str) -> str:
        m, n = len(num1), len(num2)
        ansArr = [0] * (m + n)
        for i in range(m - 1, -1, -1):
            x = int(num1[i])
            for j in range(n - 1, -1, -1):
                ansArr[i + j + 1] += x * int(num2[j])
        
        for i in range(m + n - 1, 0, -1):
            ansArr[i - 1] += ansArr[i] // 10
            ansArr[i] %= 10
        
        res = ''.join(str(x) for x in ansArr)
        return str(int(res))
```

```py
class Solution:
    def multiply(self, num1: str, num2: str) -> str:
        if num1 == "0" or num2 == "0":
            return "0"
        
        m, n = len(num1), len(num2)
        ansArr = [0] * (m + n)
        for i in range(m - 1, -1, -1):
            x = int(num1[i])
            for j in range(n - 1, -1, -1):
                ansArr[i + j + 1] += x * int(num2[j])
        
        for i in range(m + n - 1, 0, -1):
            ansArr[i - 1] += ansArr[i] // 10
            ansArr[i] %= 10
        
        index = 1 if ansArr[0] == 0 else 0
        ans = "".join(str(x) for x in ansArr[index:])
        return ans

```

## 64. Minimum Path Sum 64-最小路径和

[花花酱](https://www.bilibili.com/video/BV12W411679S?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1Ka4y1i7Vu?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1JC4y1x7j1?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1vi4y1u7a6?spm_id_from=333.999.0.0)

```py
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

## 718. 最长重复子数组

```py
# 竟然用时击败100%，惊了

class Solution:
    def findLength(self, nums1: List[int], nums2: List[int]) -> int:
        lenth = left = 0
        if nums1 and nums2:
            a, b, n = ''.join(map(chr, nums1)), ''.join(map(chr, nums2)), len(nums1)
            while lenth + left < n:
                # 这里使用lenth保存结果，用left跳出循环
                if a[left : left + lenth + 1] in b:
                    lenth += 1
                else:
                    left += 1
        return lenth 



class Solution:
    def findLength(self, A: List[int], B: List[int]) -> int:
        dp = [[0] * (len(B)+1) for _ in range(len(A)+1)]
        result = 0
        for i in range(1, len(A)+1):
            for j in range(1, len(B)+1):
                if A[i-1] == B[j-1]:
                    dp[i][j] = dp[i-1][j-1] + 1
                result = max(result, dp[i][j])
        return result


  3 2 1 4 7
1 0 0 1 0 0
2 0 1 0 0 0
3 1 0 0 0 0
2 0 2 0 0 0
1 0 0 3 0 0

```

## 78. Subsets 子集

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
class Solution:
    def subsets(self, nums: List[int]) -> List[List[int]]:
        res = []  
        def backtrack(startIndex,path):
            res.append(path[:])  # unconditional, 收集子集
            for i in range(startIndex, len(nums)):  #当startIndex已经大于数组的长度了，就终止了，for循环本来也结束了，所以不需要终止条件
                backtrack(i + 1, pat + [nums[i]])  # nums[i] 一定要用中括号括起来
        backtrack(0,[])
        return res
```

```scala
object Solution {
    //We either use or don't use the current item at the given index and continue until we are at the end of the array.
    
    def subsets(nums: Array[Int]): List[List[Int]] = {
        def backtrack(nums: List[Int], returnValue: List[Int]): List[List[Int]] = {
            nums
            .headOption
            .map(currentElem => 
                 backtrack(nums.tail, returnValue) ++ backtrack(nums.tail, currentElem +: returnValue))
            .getOrElse(List(returnValue))
        }
        
        backtrack(nums.toList, List.empty[Int])
    }
}

```

## 112-Path Sum

[哈哈哈](https://www.bilibili.com/video/BV1T7411r7Yr?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1pb411e7r7?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1uK411T7kX?spm_id_from=333.999.0.0)

递归

```py
# 正确写法
class Solution:
    def hasPathSum(self, root: TreeNode, targetSum: int) -> bool:
        if not root:
            return False
        if root.val == targetSum and not root.left and not root.right:
            return True
        return self.hasPathSum(root.left, targetSum - root.val) or self.hasPathSum(root.right, targetSum - root.val)

# 错误写法
# class Solution:
#     def hasPathSum(self, root: Optional[TreeNode], targetSum: int) -> bool:
#         if not root:
#             return False
#         if root.val == targetSum:
#             return not root.left and not root.right
#         return self.hasPathSum(root.left, targetSum - root.val) or self.hasPathSum(root.right, targetSum - root.val)

# class Solution:
#     def hasPathSum(self, root: TreeNode, targetSum: int) -> bool:
#         if not root:
#             return False
#         if root.val == targetSum:
#             return True
#         return self.hasPathSum(root.left, targetSum - root.val) or self.hasPathSum(root.right, targetSum - root.val)
```

```py
队列
class Solution:
    def hasPathSum(self, root: TreeNode, sum: int) -> bool:
        if not root:
            return False
        que = collections.deque([(root, root.val)])
        while que:
            node, tmp = que.popleft()
            if not node.left and not node.right and tmp == sum:
                return True
            if node.left:
                que.append((node.left, node.left.val + tmp))
            if node.right:
                que.append((node.right, node.right.val + tmp))
        return False
```

## 48. 旋转图像 Rotate Image

[官方](https://www.bilibili.com/video/BV1mf4y1e7ox?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Wy4y1s7fs?spm_id_from=333.999.0.0)

<img src="https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3kl7avrsvhi0.png" width="30%">

```py
class Solution:
    def rotate(self, matrix: List[List[int]]) -> None:
        """
        Do not return anything, modify matrix in-place instead.
        """
        n = len(matrix)
        for i in range(n//2): # n 和 下面的(n+1) 可以调换位置
            for j in range((n+1)//2):
                matrix[i][j],matrix[j][n-1-i],matrix[n-1-i][n-1-j],matrix[n-1-j][i] = \
                matrix[n-1-j][i],matrix[i][j],matrix[j][n-1-i],matrix[n-1-i][n-1-j]
        return matrix
```

```scala
/**
* my first commitment
* rotate 4 cell in each iteration
*
*   pattern:  (row, col) -> (col, n - 1- row)
*       1. (i, j) - > (j, n - 1 -i)
*       2. (j, n - 1 -i) -> (n - 1 - i, n - 1 - j)
*       3. (n - 1 - i, n - 1 - j) -> (n -1 -j, n - 1 - (n -1 - i) ) =  (n - 1 -j, i)
*       4. (n - 1 -j, i) -> (i, n - 1 - (n - 1 - j)) = (i, j)
*
* ((0,0) -> (0,3) -> (3,3) -> (3,0))
* ((0,1) -> (1,3) -> (3,2) -> (2,0))
* ((1,0) -> (0,2) -> (2,3) -> (3,1))
* ((1,1) -> (1,2) -> (2,2) -> (2,1))
* 
*/
object Solution1 {
    def rotate(matrix: Array[Array[Int]]): Unit = {
      val n = matrix.size
      printMatrix(n)
      
      for (i <- 0 until (n / 2).toInt + n % 2; j <- 0 until (n / 2).toInt){      
        val tmp = matrix(n - 1 -j)(i)
        matrix(n - 1 - j)(i) = matrix(n - 1 - i)(n - j - 1)
        matrix(n - 1 - i)(n - j - 1) = matrix(j)(n - 1 - i)
        matrix(j)(n - 1 - i) = matrix(i)(j)
        matrix(i)(j) = tmp
      }
    }

    /**
        (0, 0) (0, 1) (0, 2) (0, 3)  
        (1, 0) (1, 1) (1, 2) (1, 3)  
        (2, 0) (2, 1) (2, 2) (2, 3)  
        (3, 0) (3, 1) (3, 2) (3, 3)  
    */
}

```

## 234. 【回文🌈】Palindrome Linked List

[小梦想家](https://www.bilibili.com/video/BV1Yb411H7ML?spm_id_from=333.999.0.0)

```py
class Solution:
    def isPalindrome(self, head: ListNode) -> bool:
        vals = []
        cur = head
        while cur:
            vals.append(cur.val)
            cur = cur.next
        return vals == vals[::-1]



看不懂
class Solution:
    def isPalindrome(self, head: ListNode) -> bool:

        self.pre = head
        # 递归处理节点的顺序是相反的
        def recur(cur):
            if cur:
                if not recur(cur.next):
                    return False
                if self.pre.val != cur.val:
                    return False
                self.pre = self.pre.next
            return True

        return recur(head)

```

```scala
/**
* very brilliant solution
*/
object Solution2 {
    def isPalindrome(head: ListNode): Boolean = {
        if (head == null) {
            return true
        }
        var p = head
        var result = true
        def go(node: ListNode): Unit = {
            if (node.next != null) {
                go(node.next)
            }
            if (p.x != node.x) {
                result = false
            }
            p = p.next
        }
        go(head)
        result
    }
}
```


## 322. 【动态🚀规划 + 背包 + dfs】Coin Change

[花花酱](https://www.bilibili.com/video/BV1SW411C7d1?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1tz4y1d7XM?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1ty4y187dh?spm_id_from=333.999.0.0)

```py
动态🚀规划

class Solution:
    def coinChange(self, coins: List[int], amount: int) -> int:
        # 这道题的难点在于：dp数组的初始化
        dp = [10e9] * (amount + 1)
        dp[0] = 0

        for coin in coins:
            for i in range(coin, amount + 1):
                if i >= coin:
                    dp[i] = min(dp[i], dp[i-coin] + 1)
         # 这道题的难点在于：最后结果的输出
        return dp[-1] if dp[-1] != 10e9 else -1


class Solution:
    def coinChange(self, coins: List[int], amount: int) -> int:
        dp = [10e9] * (amount + 1)
        dp[0] = 0

        # 这道题 i 和 coin 倒是无所谓
        for i in range(1, amount + 1):
            for coin in coins:
                if i >= coin:
                    dp[i] = min(dp[i], dp[i-coin] + 1)
        return dp[-1] if dp[-1] != 10e9 else -1


class Solution:
    def coinChange(self, coins, amount):
        @functools.lru_cache(amount)
        def dp(remain) -> int:
            if remain < 0: return -1
            if remain == 0: return 0
            res = int(1e9)
            for coin in coins:
                tmp = dp(remain - coin) + 1
                if tmp > 0 and tmp < res:
                    res = tmp 
            return res if res < int(1e9) else -1

        if amount < 1: return 0
        return dp(amount)



class Solution:
    def coinChange(self, coins, amount):
        import functools
        @functools.lru_cache(None)
        def helper(amount):
            if amount == 0:
                return 0
            return min(helper(amount - c) if amount - c >= 0 else float("inf") for c in coins) + 1
        res = helper(amount)
        return res if res != float("inf") else -1


```

```scala
/**
* dynamic programming: bottom up
* time complexity: O(S * N), S is the amount, N is the coin denomination count
* space complexity: O(S)
*/

object Solution {
    def coinChange(coins: Array[Int], amount: Int): Int = {
         
        val dp = Array.fill[Int](amount + 1)(amount + 1) // record the minimum needed coins of each denominations

        dp(0) = 0
        for (i <- 1 to amount; denominations <- coins) {

            if(denominations <= i) {
                dp(i) = dp(i) min (dp(i - denominations) + 1)
            }        
        }
    
        if (dp.last > amount) -1 else dp.last
    }
}

```

## 39. Combination Sum 39-组合总和

[花花酱](https://www.bilibili.com/video/BV1gb411u7dy?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1Wz411e79d?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV12Z4y157nE?spm_id_from=333.999.0.0)

```py
class Solution:
    def combinationSum(self, candidates: List[int], target: int) -> List[List[int]]:
        res = []

        def backtrack(firstIdx,path):
            if sum(path) == target:
                res.append(path[:]) 
                # 易错点，这里是res.append(path[:])，而不是res.append(path)
                return
            if sum(path) > target:
                return
            if sum(path) < target:
                for i in range(firstIdx,len(candidates)):
                    backtrack(i,path + [candidates[i]])
        backtrack(0,[])
        return res
```

```scala

/**
* my first commitment: dfs - backtracking
*/

object Solution1-1 {
    import collection.mutable
    def combinationSum(candidates: Array[Int], target: Int): List[List[Int]] = {
      
      def dfs(combination: List[Int], ans: mutable.Set[List[Int]]): Unit = {
        val currentSum = combination.sum
        
        if (currentSum == target) {
          ans += combination.toList
          
        } else if (currentSum < target){
          val diff = target - currentSum
          candidates.filter(n => n <= diff).foreach{ case n => dfs(n :: combination, ans)}
        }
      }
      val ans = mutable.Set.empty[List[Int]]
      dfs(List.empty[Int], ans)
      ans.map(l => l.groupBy(identity).mapValues(_.length).toMap -> l).toMap.values.toList // distinct 
    }
}
```

## 169. 【位运算😜】Majority Element

[花花酱](https://www.bilibili.com/video/BV1hb411c7bF?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Yb411H7pW?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Ff4y1U7Vn?spm_id_from=333.999.0.0)


```py
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
```

```scala
/**
* HashMap
* time complexity: O(N)
* space complexity: O(N)
*/

object Solution2 {
    def majorityElement(nums: Array[Int]): Int = {
        nums.groupBy(identity).mapValues(_.length).maxBy(_._2)._1  
    }
}


/**
* sorting array and pick middle element
* time complexity O(NlogN)
*/

object Solution3 {
    def majorityElement(nums: Array[Int]): Int = {
        nums.sorted(Ordering.Int)(nums.length / 2)
    }
}

//Alternate solution O(n) but NO EXTRA SPACE
object Solution {
    def majorityElement(nums: Array[Int]): Int = {     
        var candidate = nums.head
        var count = 0
        nums.foreach(vot => {
            if(count == 0) { 
                candidate = vot
                count = 0
            }
            if(vot == candidate) count+=1;
            else count-=1;
        })
        
        candidate
    }
}

```

## 83-Remove duplicates from sorted array

[哈哈哈](https://www.bilibili.com/video/BV1yJ411R7FZ?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Wb411e7s7?spm_id_from=333.999.0.0)

[洛阳](https://www.bilibili.com/video/BV1zK411L7Gg?spm_id_from=333.999.0.0)

```py
class Solution:
    def deleteDuplicates(self, head: ListNode) -> ListNode:
        if not head or not head.next:
            return head
        cur = head
        while cur.next:
            if cur.val == cur.next.val:
                cur.next = cur.next.next
            else:
                cur =  cur.next
        return head
```

## 226-翻转二叉树

[哈哈哈](https://www.bilibili.com/video/BV1Sh411R7B2?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1Yb411H73E?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1FK411p7Co?spm_id_from=333.999.0.0)

```py
class Solution:
    def invertTree(self, root: TreeNode) -> TreeNode:
        if not root:
            return root
        
        left = self.invertTree(root.left)
        right = self.invertTree(root.right)
        root.left, root.right = right, left
        return root
```

```py
class Solution:
    def invertTree(self, root: TreeNode) -> TreeNode:
        if not root:
            return root
        Q = deque([root])
        while Q:
            r = Q.pop()
            if r.left or r.right:
                r.left, r.right = r.right, r.left
                if r.left: Q.append(r.left)
                if r.right: Q.append(r.right)
        return root
```

## 165. Compare Version Numbers

[小梦想家](https://www.bilibili.com/video/BV19K4y1C7L3?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1Pk4y117dF?spm_id_from=333.999.0.0)

```py
class Solution:
    def compareVersion(self, version1: str, version2: str) -> int:
        v1 = version1.split(".")
        v2 = version2.split(".")

        while v1 or v2:
            x = int(v1.pop(0)) if v1 else 0
            y = int(v2.pop(0)) if v2 else 0

            if x > y:
                return 1
            elif x < y:
                return -1
        return 0
```

## 34-在排序数组中查找元素的第一个

[哈哈哈](https://www.bilibili.com/video/BV1Zv411y71t?spm_id_from=333.999.0.0)

[图灵](https://www.bilibili.com/video/BV1GU4y1j7dq?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1ef4y1v7Vz?spm_id_from=333.999.0.0)

```py
# Python 二分法

class Solution:
    def searchRange(self, nums, target):
        left = 0
        right = len(nums)-1
        res = [0,0]
        
        if target not in nums:
            return [-1,-1]

        # 寻找左侧边界
        while left <= right:
            mid = (right + left) // 2
            if nums[mid] == target:
                right = mid - 1 # 结束条件, 因为保留 left，所以移动 right
            elif nums[mid] > target:
                right = mid - 1
            else:
                left = mid + 1
        res[0] = left

        # 寻找右侧边界
        right = len(nums)-1
        while left <= right:
            mid = (right + left) // 2
            if nums[mid] == target:
                left = mid + 1 # 结束条件, 因为保留 right，所以移动 left
            elif nums[mid] > target:
                right = mid - 1
            else:
                left = mid + 1
        res[1] = right

        return res
```


```scala
/**
* modify binary search template
* memo
*  1. search first and last the the same function
*  2. if nums(mid) == target we could move left to check if left part exists target number
*  3. finding last by target + 1,  then we could get last position of target by first position of (target + 1) - 1
* tricky:
*  1. ans = nums.length
*  2. first > last  means that target doesn't exists
*
* time complexity: O(2logN)
*/
 
 object Solution2 {
    def searchRange(nums: Array[Int], target: Int): Array[Int] = {
        val first = search(nums, target)
        val last = search(nums, target + 1) - 1
        if (first > last) Array(-1, -1) else Array(first, last)
    }

    def search(nums: Array[Int], target: Int): Int = {
      var ans = nums.length
      var left = 0
      var right = nums.length - 1
      while (left <= right) {
        val mid = left + (right - left) / 2
        if (nums(mid) >= target) {
          ans = mid
          right = mid - 1
        }else {
          left = mid + 1
        } 
      }
      ans
    }
}

```