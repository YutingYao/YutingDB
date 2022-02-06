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
class Solution:
    def findKthLargest(self, nums: List[int], k: int) -> int:
        """使用小顶堆"""
        q = []
        for c in nums:
            heapq.heappush(q, c)
            while len(q) > k:
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

```py
class Solution:
    # 翻转一个子链表，并且返回新的头与尾
    def reverse(self, head: ListNode, tail: ListNode):
        prev = tail.next
        p = head
        while prev != tail:
            nex = p.next
            p.next = prev
            prev = p
            p = nex
        return tail, head

    def reverseKGroup(self, head: ListNode, k: int) -> ListNode:
        hair = ListNode(0)
        hair.next = head
        pre = hair

        while head:
            tail = pre
            # 查看剩余部分长度是否大于等于 k
            for i in range(k):
                tail = tail.next
                if not tail:
                    return hair.next
            nex = tail.next
            head, tail = self.reverse(head, tail)
            # 把子链表重新接回原链表
            pre.next = head
            tail.next = nex
            pre = tail
            head = tail.next
        
        return hair.next
```

```py
栈
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None

class Solution:
    def reverseKGroup(self, head: ListNode, k: int) -> ListNode:
        dummy = ListNode(0)
        p = dummy
        while True:
            count = k 
            stack = []
            tmp = head
            while count and tmp:
                stack.append(tmp)
                tmp = tmp.next
                count -= 1
            # 注意,目前tmp所在k+1位置
            # 说明剩下的链表不够k个,跳出循环
            if count : 
                p.next = head
                break
            # 翻转操作
            while stack:
                p.next = stack.pop()
                p = p.next
            #与剩下链表连接起来 
            p.next = tmp
            head = tmp
        
        return dummy.next
```

```py
尾插法
# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None

class Solution:
    def reverseKGroup(self, head: ListNode, k: int) -> ListNode:
        dummy = ListNode(0)
        dummy.next = head
        pre = dummy
        tail = dummy
        while True:
            count = k
            while count and tail:
                count -= 1
                tail = tail.next
            if not tail: break
            head = pre.next
            while pre.next != tail:
                cur = pre.next # 获取下一个元素
                # pre与cur.next连接起来,此时cur(孤单)掉了出来
                pre.next = cur.next 
                cur.next = tail.next # 和剩余的链表连接起来
                tail.next = cur #插在tail后面
            # 改变 pre tail 的值
            pre = head 
            tail = head
        return dummy.next
```

```py
递归

python

# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None

class Solution:
    def reverseKGroup(self, head: ListNode, k: int) -> ListNode:
        cur = head
        count = 0
        while cur and count!= k:
            cur = cur.next
            count += 1
        if count == k:
            cur = self.reverseKGroup(cur, k)
            while count:
                tmp = head.next
                head.next = cur
                cur = head
                head = tmp
                count -= 1
            head = cur   
        return head
```

```py
方法一：递归

结合了24题两两交换链表中的节点和206题反转链表

首先判断是否可以反转，如果不行说明链表到头了，返回head

然后得到该段反转后连接到的那个节点temp，和head一起反转链表

class Solution:
    def reverseKGroup(self, head: Optional[ListNode], k: int) -> Optional[ListNode]:
        if k == 1:    #处理特殊情况
            return head

        L, R = head, head   #R判断是否可以翻转，L为实际操作的节点

        #递归终止的条件为节点数量达不到k的时候
        for _ in range(k - 1):
            if R == None or R.next == None:
                return head
            R = R.next
            
        temp = self.reverseKGroup(R.next, k)  #往后遍历，得到该段反转后需要连接到的下一个节点

        #反转链表
        for _ in range(k):
            temp2 = L.next
            L.next = temp
            temp = L
            L = temp2
        return R  #返回该段最后一个节点，作为上一段的连接点
方法二：迭代

将节点进行分组，在遍历某一组时，先看该组是否可以翻转，如果不行则直接返回ans

如果可以翻转，那就需要确定翻转后连接到下一组的哪个点。再看下一组能否翻转，存在两个情况：

下一组也可以翻转，则连接点为下一组的最后一个

下一组不能翻转，则连接点为下一组的第一个

然后开始翻转链表即可

为了返回答案，需要在遍历第一次的时候让翻转后的头结点等于ans

class Solution:
    def reverseKGroup(self, head: Optional[ListNode], k: int) -> Optional[ListNode]:
        if k == 1: 
            return head
        
        cur = head
        times = 0   #在第一次遍历时确定头节点

        while True:
            times += 1
            t = cur
            goOn = True

            #首先判断当前这一组节点是否可以翻转
            for _ in range(k - 1):                
                t = t.next
                if t == None:
                    goOn = False 
            if not goOn:
                break

            if times == 1:  #确定好返回值开始的节点
                ans = t 
            
            #然后根据下一组节点来判断当前节点翻转后连接到哪个节点
            #如果没有break，则下一组节点可以翻转，连接点为下一组节点的最后一个节点
            pre = t.next
            for _ in range(k - 1):
                if pre == None or pre.next == None:  #下一组节点无法翻转，连接点为下一组节点的第一个节点
                    pre = t.next
                    goOn = False
                    break
                pre = pre.next
            
            
            #翻转当前这一组节点
            for _ in range(k):
                temp = cur.next
                cur.next = pre
                pre = cur
                cur = temp

            if not goOn:
                break

        return ans
```

## 912 补充题4. 手撕快速排序（add）

```py
class Solution:
    def randomized_partition(self, nums, l, r):
        pivot = random.randint(l, r)
        nums[pivot], nums[r] = nums[r], nums[pivot]
        i = l - 1
        for j in range(l, r):
            if nums[j] < nums[r]:
                i += 1
                nums[j], nums[i] = nums[i], nums[j]
        i += 1
        nums[i], nums[r] = nums[r], nums[i]
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

作者：LeetCode-Solution
链接：https://leetcode-cn.com/problems/sort-an-array/solution/pai-xu-shu-zu-by-leetcode-solution/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```py
class Solution:
    def max_heapify(self, heap, root, heap_len):
        p = root
        while p * 2 + 1 < heap_len:
            l, r = p * 2 + 1, p * 2 + 2
            if heap_len <= r or heap[r] < heap[l]:
                nex = l
            else:
                nex = r
            if heap[p] < heap[nex]:
                heap[p], heap[nex] = heap[nex], heap[p]
                p = nex
            else:
                break
        
    def build_heap(self, heap):
        for i in range(len(heap) - 1, -1, -1):
            self.max_heapify(heap, i, len(heap))

    def heap_sort(self, nums):
        self.build_heap(nums)
        for i in range(len(nums) - 1, -1, -1):
            nums[i], nums[0] = nums[0], nums[i]
            self.max_heapify(nums, 0, i)
            
    def sortArray(self, nums: List[int]) -> List[int]:
        self.heap_sort(nums)
        return nums

作者：LeetCode-Solution
链接：https://leetcode-cn.com/problems/sort-an-array/solution/pai-xu-shu-zu-by-leetcode-solution/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
```

```py
class Solution:
    def merge_sort(self, nums, l, r):
        if l == r:
            return
        mid = (l + r) // 2
        self.merge_sort(nums, l, mid)
        self.merge_sort(nums, mid + 1, r)
        tmp = []
        i, j = l, mid + 1
        while i <= mid or j <= r:
            if i > mid or (j <= r and nums[j] < nums[i]):
                tmp.append(nums[j])
                j += 1
            else:
                tmp.append(nums[i])
                i += 1
        nums[l: r + 1] = tmp

    def sortArray(self, nums: List[int]) -> List[int]:
        self.merge_sort(nums, 0, len(nums) - 1)
        return nums

作者：LeetCode-Solution
链接：https://leetcode-cn.com/problems/sort-an-array/solution/pai-xu-shu-zu-by-leetcode-solution/
来源：力扣（LeetCode）
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
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
            if nums[i] + nums[n-2] + nums[n-1] < 0:continue
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
* chosen solution
* 1. two pointer in twoSum
* 2. result storing in hashSet to avoid duplicate pairs
* time complexity: O(N^2)
* space complexity: O(N): due to sorted list 
*/
object Solution0 {
  def threeSum(nums: Array[Int]): List[List[Int]] = {
    val l = nums.sorted
    l.indices.foldLeft(Set[List[Int]]()) {
          /* only send value less than zero and those num which was duplicated only once into twoSum */
      case (ans, idx) if l(idx) <= 0 && (idx == 0 || (idx >= 1 && l(idx) != l(idx - 1))) =>
        twoSum(-l(idx), l, idx + 1, ans)
      case (set, _) => set

    }.toList

  }

  def twoSum(target: Int, nums: Array[Int], from: Int, ans: Set[List[Int]]): Set[List[Int]] = {

    @annotation.tailrec
    def loop(i: Int, j: Int, ans: Set[List[Int]]): Set[List[Int]] = {

      if(i < j) {
        val sum = nums(i) + nums(j)
        if(sum > target) loop(i, j - 1, ans)
        else if(sum < target) loop(i + 1, j, ans)
        else loop(i + 1, j - 1, ans + List(-target, nums(i), nums(j)))
      }else {
        ans
      }
    }
    loop(from, nums.length - 1, ans)
  }
}
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

/**
* hashset in twoSum
* sorted nums and not to run duplicate num twice into twoSum
* O(N^2)
*/
object Solution1-2 {
  def threeSum(nums: Array[Int]): List[List[Int]] = {
   
    val l = nums.sorted
    val ret = for((value, index) <- l.zipWithIndex; if index >= 1 && l(index) != l(index - 1)) yield  {
      val ll = l.toBuffer
      ll.remove(index)
      twoSum(ll.toArray, -value).filter(_.nonEmpty).map(_ :+ value)
    }

    l.slice(0, 3) match {
      case Array(0, 0, 0 ) =>  ret.flatten.map(l => (l.toSet, l)).toMap.values.toList :+ List(0, 0, 0) // edge case (0, 0, 0)
      case _ => ret.flatten.map(l => (l.toSet, l)).toMap.values.toList
    }

  }

  def twoSum(nums: Array[Int], target: Int): List[List[Int]] = {
    val value2Idx = nums.zipWithIndex.toMap
    nums.zipWithIndex.collect {
      case (value, index) if value2Idx.get(target - value).exists(_ != index) =>

        List(value, target - value)
    }.map(l => (l.toSet, l)).toMap.values.toList
  }

/**
* improvement:
*   1. only call twoSum when  l(idx) under zero,  because the array was sorted, there won't be any chance the next entries sum to 0.
*   2. only send the remaining nums which were after idx into twoSum
* O(N^2)
*/

  object Solution1-3 {
    def threeSum(nums: Array[Int]): List[List[Int]] = {
        val l = nums.sorted
        l.indices.foldLeft(collection.mutable.ListBuffer.empty[List[Int]]){
        case (r, idx) if l(idx) <=0 && (idx == 0 || (idx > 0 && l(idx) != l(idx-1))) =>
            r ++= twoSum(l.slice(idx + 1, l.length), -l(idx)).map(_ :+ l(idx))
        case (r, idx)  => r

        }.toList
        
    }

    def twoSum(nums: Array[Int], target: Int): List[List[Int]] = {

        val value2Idx = nums.zipWithIndex.toMap
        nums.zipWithIndex.collect {
        case (value, index) if value2Idx.get(target - value).exists(_ != index) =>
            List(value, target - value)
        }.map(l => (l.toSet, l)).toMap.values.toList
    }
  
}


/**
*  Using a hashset to erase duplicate in twoSum
*/
object Solution1-3-2 {
  def threeSum(nums: Array[Int]): List[List[Int]] = {
    val l = nums.sorted
    l.indices.foldLeft(collection.mutable.ListBuffer.empty[List[Int]]){
      case (r, idx) if l(idx) <=0 && (idx == 0 || (idx > 0 && l(idx) != l(idx-1))) =>
        r ++= twoSum(l.slice(idx + 1, l.length), -l(idx))
      case (r, idx)  => r

    }.toList

  }

  def twoSum(nums: Array[Int], target: Int): List[List[Int]] = {

    val value2Idx = nums.zipWithIndex.toMap
    nums.zipWithIndex.foldLeft(Set[List[Int]]()) {
      case (s, (value, index)) if value2Idx.get(target - value).exists(_ != index) =>
        val t_sub_v = target - value
        if(index < value2Idx(t_sub_v)) {
          s + List(-target, value, t_sub_v)
        } else {
          s + List(-target, t_sub_v, value)
        }
      case (s, _) => s

    }.toList
  }
}
/**
* more readable and simpler
*/
object Solution1-3-3 {
  def threeSum(nums: Array[Int]): List[List[Int]] = {
    val l = nums.sorted

    l.zipWithIndex.foldLeft(Set[List[Int]]()) {
      /* only send value less than zero and those num which was duplicated only once into twoSum */
      case (set, (v, idx)) if v <=0 && (idx == 0 || (idx > 0 && l(idx) != l(idx - 1)))  =>
        set ++ twoSum(-v, l.slice(idx + 1, l.length))
      case (set, _) => set
    }.toList

  }

  def twoSum(target: Int, nums: Array[Int]): List[List[Int]] = {
    val map = nums.zipWithIndex.toMap
    nums.zipWithIndex.foldLeft(Set[List[Int]]()){
      case (set, (n, idx)) =>
        val n2 = target - n
        map.get(n2) match {
          case Some(e) if e != idx =>
            /* using  n n2 order to help hashset to eliminate duplicate */
            if(n < n2)
              set + List(-target, n, n2)
            else
              set + List(-target, n2, n)
          case _ => set
        }
    }.toList
  }
}

/**
* two pointer in twoSum
* time complexity: O(N^2)
* space complexity: O(N): due to sorted list 
*/

object Solution2 {
  def threeSum(nums: Array[Int]): List[List[Int]] = {
    val l = nums.sorted
    l.indices.foldLeft(Set[List[Int]]()) {
      case (ans, idx) if l(idx) <= 0 && (idx == 0 || (idx >= 1 && l(idx) != l(idx - 1))) =>
        twoSum(-l(idx), l, idx + 1, ans)
      case (set, _) => set

    }.toList

  }

  def twoSum(target: Int, nums: Array[Int], from: Int, ans: Set[List[Int]]): Set[List[Int]] = {

    @annotation.tailrec
    def loop(i: Int, j: Int, ans: Set[List[Int]]): Set[List[Int]] = {

      if(i < j) {
        val sum = nums(i) + nums(j)
        if(sum > target) loop(i, j - 1, ans)
        else if(sum < target) loop(i + 1, j, ans)
        else loop(i + 1, j - 1, ans + List(-target, nums(i), nums(j)))
      }else {
        ans
      }
    }
    loop(from, nums.length - 1, ans)
  }
}
```

## 53. 最大子序和53-【贪心🧡】Maximum subarray

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
        
        // IDEA:
        // Go through the whole Array,
        // and change each element into the possible maximum sum of the subarray ENDING at its index 
        
        // During each iteration, the element at i-th index will be updated into the possible maximum sum of subarray ENDING at i-th index
        // then for (i+1)th index, if updated i-th value is positive, it can be used to update (i+1)th value as well.
        
        for (i <- Range(1, nums.length)) {
            if (nums(i-1) > 0) {
                nums(i) += nums(i-1)
            }
        }
        
        nums.max
    }
}
```

```scala
/**
* chosen solution
* dynamic programming
*    dp[i] defined as the sum of subarray that ending with ith element and must contains i-th element number   *
* actually, we don't need storing all previous status of nums.length
* we just need two status: one for maximum so far, the other one for the maximum accumulated value which containing with nums[i]
*
* time complexity: O(N)
* space complexity: O(1)
*/
object Solution0{
    def maxSubArray(nums: Array[Int]): Int = {
        if (nums == null || nums.isEmpty) return 0
        var maxSoFar = nums(0)
        var maxEndingHere = nums(0)

        for(i <- 1 until nums.length) {
           maxEndingHere = (maxEndingHere +  nums(i))  max nums(i)
           maxSoFar = maxEndingHere max  maxSoFar
        }
        maxSoFar
        
    }
}

/**
* my first commit version
* time complexity: O(N^2)
* space complexity: O(N)
*/

object Solution1 {
    def maxSubArray(nums: Array[Int]): Int = {
     
        (1 to nums.length).map(n => _maxSubArray(nums, nums(n - 1), n)).max
        
    }
    
    def _maxSubArray(nums: Array[Int], preSum: Int, currentIdx: Int): Int = {
        if(nums.length == currentIdx) return preSum
        
        val currentSum = preSum + nums(currentIdx)
        val nexLevelSum = _maxSubArray(nums, currentSum, currentIdx + 1)
        preSum max currentSum max nexLevelSum
    }
    
}

/**
* dynamic programming
* memo:
*    1. dp[i] defined as the sum of subarray that ending with ith element and must contains i-th element number   
* time complexity: O(N)
* space complexity: O(N)  due to dp array
*/

object Solution2 {
    def maxSubArray(nums: Array[Int]): Int = {
        if(nums == null || nums.isEmpty) return 0
        val dp = Array.ofDim[Int](nums.length, 2)  // dp(0) ... dp(i) storing each status corresponding to  nums' index, means max subarray sum ending with nums[i]
        dp(0)(0) = nums(0)  // dim0: accumulate calculator which reset while new element is larger value inside,
        dp(0)(1) = nums(0) // dim1: maximum so far
        
        for(i <- 1 until nums.length) {
            
            dp(i)(0) = (dp(i - 1)(0) + nums(i))  max nums(i)
            dp(i)(1) = dp(i)(0) max dp(i - 1)(1) 
        }
        dp.last.last
    }
}

/**
* dynamic programming
* memo
*   1. one dimension array
* time complexity O(N)
* space complexity O(N)
*/
object Solution2-1 {
    def maxSubArray(nums: Array[Int]): Int = {
      val dp  = Array.ofDim[Int](nums.length)
      dp(0) = nums(0)
      for (i <- 1 until nums.size) {
        dp(i) = nums(i) max (nums(i) + dp(i - 1))
      }
      
      dp.max
    }
}

/**
* dynamic programming
* actually, we don't need storing all previous status of nums.length
* we just need two status: one for maximum so far, the other one for the maximum accumulated value which containing with nums[i]
*
* time complexity: O(N)
* space complexity: O(1)
*/

object Solution2-2 {
    def maxSubArray(nums: Array[Int]): Int = {
        if (nums == null || nums.isEmpty) return 0
        var maxSoFar = nums(0)
        var maxEndingHere = nums(0)

        for(i <- 1 until nums.length) {
           maxEndingHere = (maxEndingHere +  nums(i))  max nums(i)
           maxSoFar = maxEndingHere max  maxSoFar
        }
        maxSoFar
        
    }
}
/**
*  functional programming: foldLeft
*/
object Solution2-3 {
    def maxSubArray(nums: Array[Int]): Int = {
      if(nums == null || nums.isEmpty) return 0
      (1 until nums.length).foldLeft((nums(0), nums(0))){
          case ((maxEndingI, maxSofar), i) => 
            val maxEndingT = nums(i) max (nums(i) + maxEndingI)
            (maxEndingT, maxSofar max maxEndingT )
      }._2
    }
}
```

```scala
object Solution {
    def maxSubArray(nums: Array[Int]): Int = {
        if(nums.length == 1){
            nums(0)
        }else{
            var sum = nums(0)
            var max = nums(0)
            var i = 1
            while (i < nums.length){
                val elem = nums(i)
                sum = sum + elem
                if(sum > max){
                    max = sum
                    i += 1
                }else if(sum < elem){
                    sum = elem
                    i += 1
                }else{
                    i += 1
                }
                
                if(elem > max){
                    max = elem
                    sum = elem
                }
            }
            max
        }
    }
}

```

```scala
package com.zhourui.leetcode

import scala.math.{abs, max}
import com.zhourui.codech.BaseExtension

package lc0053_maxsubarr {




  object Solution {
    def maxSubArray(nums: Array[Int]): Int = {
      var maxsum:Int=Int.MinValue
      nums.foldLeft(0) {
        case (a,b) => { // 第一次进入时,a=0
          val cursum = max(a+b,b)
          maxsum = max(maxsum, cursum)
          cursum
        }
      }
      return maxsum
    }
  }

  class Test extends BaseExtension {
    def init {
      val arr = Array(-2, 1, -3, 4, -1, 2, 1, -5,4)
      println(Solution.maxSubArray(arr) == 6)

    }
    val name = "053 max sub array"
  }
}



/*
[-2,1,-3,4,-1,2,1,-5,4]
class Solution {
public:
    int maxSubArray(vector<int>& nums) {
        int cursum = nums[0];
        int maxsum = cursum;

        for (int i=1;i<nums.size();i++) {
            cursum = max(cursum+nums[i],nums[i]);
            maxsum = max(maxsum, cursum);
        }
        return maxsum;
    }
};
 */
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
        for i,n in enumerate(nums):
            if n in dic:
                return [dic[n],i]
            dic[target - n] = i
```

```scala
object Solution {
    def twoSum(nums: Array[Int], target: Int): Array[Int] = {
        val nums_map = scala.collection.mutable.HashMap[Int, Int]()
        var result: Array[Int] = Array(0,0)
        var i = 0
        while(result.sum == 0) {
            val complement = target - nums(i)
            if (nums_map.contains(complement)) {
                result(0) = i
                result(1) = nums_map(complement)
            } else {
                nums_map(nums(i)) = i
            }
            i += 1
        }
        result     
    }
}



// Brute-force method, which takes more than two times of running time than the method above
object Solution {
    def twoSum(nums: Array[Int], target: Int): Array[Int] = {
        
        val result = for {i <- 0 until (nums.length - 1);
            j <- (i+1) until nums.length
            if nums(i) + nums(j) == target} yield Array(i, j)
        
        result(0)
        
    }
}

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
* HashTable
* time complexity: O(N)
*/

object Solution1 {
  def twoSum(nums: Array[Int], target: Int): Array[Int] = {
    val value2Idx = nums.zipWithIndex.toMap
    val ret = collection.mutable.ArrayBuffer[Int]()

    for ((n, idx) <- nums.zipWithIndex; if ret.length < 2) {
      val v2 = target - n
      value2Idx.get(v2) match {
        case Some(v2Idx) if v2Idx != idx =>
          ret ++= Array(idx, v2Idx)
        case _ =>
      }
    }
    ret.toArray
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

```scala
object leetcode01_two_sum extends App {
  def twoSum(nums: Array[Int], target: Int): Array[Int] = {
    val sorted = nums.zipWithIndex.sortWith(_._1 < _._1)
    var left = 0
    var right = sorted.length - 1
    while(left < right) {
      val cal = sorted(left)._1 + sorted(right)._1
      if(cal > target) {
        right = right - 1
      } else if (cal < target) {
        left = left + 1
      } else {
        return Array(sorted(left)._2, sorted(right)._2)
      }
    }
    return Array.emptyIntArray
  }

  twoSum(Array(3,2,4), 6)
}


object Solution {
    def twoSum(nums: Array[Int], target: Int): Array[Int] = {
        var map = Map.empty[Int, Int]
        var result = Array.empty[Int]
        (0 until nums.length) foreach { i =>
            val v = nums(i)
            map.get(target - v) match {
                case Some(x)  =>
                    if (x != i){
                    result = Array(x, i)
                    }
                case _ => map += v -> i
            }
        }
        result
    }
}

```


## 21. 合并两个有序链表


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
* chosen solution
* time complexity: O(N + M), N is the length of l1, M is the length of l2
*/

object Solution0 {
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
```

```scala
/**
 * Definition for singly-linked list.
 * class ListNode(_x: Int = 0, _next: ListNode = null) {
 *   var next: ListNode = _next
 *   var x: Int = _x
 * }
 */
object Solution {
    def mergeTwoLists(l1: ListNode, l2: ListNode): ListNode = {
        if(l1 == null){
            l2
        } else if(l2 == null){
            l1
        }else{
            var (ll1, ll2) = (l1, l2)
            var firstNext = if(ll1.x < ll2.x) ll1 else ll2
            var head = ListNode(0, firstNext)
            var curr = head
            
            
            while(ll1 != null && ll2 != null){
                if(ll1.x < ll2.x){
                    curr.next = ll1
                    curr = ll1
                    ll1 = ll1.next
                } else{
                    curr.next = ll2
                    curr = ll2
                    ll2 = ll2.next
                }
            }
            
            if(ll1 == null){
                curr.next = ll2
            }else{
                curr.next = ll1
            }
            
            head.next
        }
    }
}


//Alternate & Simpler solution
/**
 * Definition for singly-linked list.
 * class ListNode(_x: Int = 0, _next: ListNode = null) {
 *   var next: ListNode = _next
 *   var x: Int = _x
 * }
 */
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
