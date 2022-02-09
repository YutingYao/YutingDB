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