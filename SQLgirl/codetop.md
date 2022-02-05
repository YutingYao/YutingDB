## 206. 反转链表Reverse Linked List

[哈哈哈](https://www.bilibili.com/video/BV1Q7411V7zr?spm_id_from=333.999.0.0)

[图灵](https://www.bilibili.com/video/BV1XQ4y1h735?spm_id_from=333.999.0.0)

[洛阳](https://www.bilibili.com/video/BV16Q4y1M767?spm_id_from=333.999.0.0)

```py
前置条件：迭代指针：p = head、结果指针：res = none

以1->2->3->4->5为例：

过程：

res:None

第一层循环

res:1->2->3->4->5 res = p

res:1->None res.next = res

p:2->3->4->5 p = p.next

第二层循环

res:2->3->4->5 res = p

res:2->1->None res.next = res

p:3->4->5 p = p.next

第三层循环

res:3->4->5 res = p

res:3->2->1->None res.next = res

p:4->5 p = p.next

第四层循环

res:4->5 res = p

res:4->3->2->1->None res.next = res

p:5 p = p.next

第五层循环

res:5 res = p

res:5->4->3->2->1->None res.next = res

p:None p = p.next

end...

class Solution:
    def reverseList(self, head):
        pre, res = head, None
        while pre:
            res = pre
            res.next = res
            pre = pre.next
        return res
```

```py
class Solution:
    def reverseList(self, head: ListNode) -> ListNode:
        """ 还有一种方法，不需要使用首元结点 
            1 -> 2 -> 3 -> 4    可以依次逆序每个指针
            1 <- 2 <- 3 <- 4    4变为了新的表头
            和头插法一样， 需要注意改变节点指针的时候，不能影响到 遍历下一个元素
        """
        # 1. 首先需要一个指针p顺序遍历节点， 还需要pre 和 cur 指针用于反转
        pre = None
        tmp = cur = head
        while tmp:
            # 1. 更新cur为当前节点
            cur = tmp
            # 2. p指针后移
            tmp = tmp.next
            # 3. 做反转
            cur.next = pre
            # 4. 更新pre为当前节点
            pre = cur

        # 5. 重新定义 head指向链表末尾
        head = cur
        return head
```


```py

递归解法， 先写出 while循环的迭代解法，再推导到 迭代写法。好像容易理解一些
# 还可以 以递归的形式解决问题
class Solution:
    def reverseList(self, head: ListNode) -> ListNode:
        """ 
            迭代解法中，每一步都是 重新指向next指针， 可以分治法，使用递归求解。找到最小子问题及终止条件
            需要调用递归栈， 空间效率要低很多。
        """
        # 1. 首先需要一个指针p顺序遍历节点， 还需要pre 和 cur 指针用于反转
        def reverse(pre, cur):
            # 当cur为None了， 说明pre指向最后的节点，返回作为新的头结点
            if not cur: 
                return pre
            next = cur.next
            cur.next = pre
            return reverse(cur, next)
        

        head = reverse(None, head)
        return head


class Solution:
    def reverseList(self, head: ListNode) -> ListNode:
        
        def reverse(pre,cur):
            if not cur:
                return pre
                
            tmp = cur.next
            cur.next = pre

            return reverse(cur,tmp)
        
        return reverse(None,head)
```




```scala
object Solution {
    def reverseList(head: ListNode): ListNode = {
        if(head == null || head.next == null){
            head
        } else{
            var p = reverseList(head.next)
            head.next.next = head
            head.next = null
            p
        }
    }
}

```
```scala
/**
* time complexity: O(n)
* space complexity: O(1) 
*/
object Solution0 {
    def reverseList(head: ListNode): ListNode = {        
        var prev: ListNode = null
        var curr = head

        while (curr != null) {
            val hold = curr.next
            curr.next = prev
            prev = curr
            curr = hold
        }
        prev
    }
}

 /**
 * time complexity: O(n)
 * space complexity: O(1)
 */
object Solution1 {
    def reverseList(head: ListNode): ListNode = {
        
        var prev: ListNode = null
        var curr = head

        while (curr != null) {
            val hold = curr.next
            curr.next = prev
            prev = curr
            curr = hold
        }
        prev
    }
    
    def printNode(node: ListNode) {
        var n = node
        while(n != null) {
            print(s"${n.x} ")
            n = n.next
        }
    }
}


/** recursive version */

object Solution2 {
    def reverseList(head: ListNode): ListNode = {
        
        val curr:ListNode = null
        
        _reverseList(curr, head)
        
    }
    
    @annotation.tailrec
    def _reverseList(curr: ListNode, next: ListNode): ListNode = {
        if(next == null) {
            curr
        }else{
            val tmpNode = next.next
            next.next = curr
            _reverseList(next, tmpNode)
        }
    }
}

object Solution2-1 {
    def reverseList(head: ListNode): ListNode = {
        if(head == null) head
        else _reverseList(head)
        
    }
    
    def _reverseList(node: ListNode): ListNode = {
        if (node == null || node.next == null) {
            node
        }else {
            val newHead = _reverseList(node.next)
              // reversedHead 是返回原本的尾巴，若一開始輸入是 1 -> 2 -> 3 -> 4 -> 5  -> null , 那 reversedHead 就是 5
            // 每次 iteration 返回都是同一個 reversedHead 也就是 5
            node.next.next = node
            node.next = null
             // 每次迭代 改變的就是送進每個 function 的 listnode 的 next 與 next.next 指向
            newHead
        }
        
        
    }
}

```

## 146. LRU缓存机制【构造🏰】LRU Cache 

[花花酱](https://www.bilibili.com/video/BV19b411c7ue?spm_id_from=333.999.0.0)

[花花酱](https://www.bilibili.com/video/BV1gt411Y7c6?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV1vi4y1t7zj?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1ZQ4y1A74H?spm_id_from=333.999.0.0)

这个functools.lru_cache(None)的底层是怎么做的呀？ 

```py
def lru(f):
    d={}
    def wrapper(*args):
        if args not in d:
            d[args]=f(*args)
        return d[args]
    return wrapper
```

加个前缀和预处理，时间减少一半：

```py 
# 利用 super().__init__()
class LRUCache(collections.OrderedDict):

    def __init__(self, capacity: int):
        # super() 继承 collections.OrderedDict
        super().__init__()
        self.capacity = capacity


    def get(self, key: int) -> int:
        # 如果关键字 key 存在于缓存中，则返回关键字的值，否则返回 -1 
        if key not in self:
            return -1
        self.move_to_end(key)
        return self[key]

    def put(self, key: int, value: int) -> None:
        # 如果关键字 key 已经存在，则变更其数据值 value
        if key in self:
            self.move_to_end(key)
        # 如果不存在，则向缓存中插入该组 key-value
        self[key] = value
        # 如果插入操作导致关键字数量超过 capacity ，则应该 逐出 最久未使用的关键字。
        if len(self) > self.capacity:
            self.popitem(last=False)
```


```py
# 利用 self.cache = collections.OrderedDict()
class LRUCache:
    def __init__(self, capacity):
        self.capacity = capacity
        self.cache = collections.OrderedDict()

    写法 1：无 move_to_end
    def get(self, key):
        # 如果关键字 key 存在于缓存中，则返回关键字的值，否则返回 -1 
        if key in self.cache:
            value = self.cache.pop(key)
            self.cache[key] = value
            return value
        return -1

    写法 2：有 move_to_end
    def get(self, key):
        if key in self.cache:
            self.cache.move_to_end(key)
            return self.cache[key]
        return -1

    写法 1：无 move_to_end
    def put(self, key, value):
        # 如果关键字 key 已经存在，则变更其数据值 value
        if key in self.cache:
            self.cache.pop(key)
        # 如果插入操作导致关键字数量超过 capacity ，则应该 逐出 最久未使用的关键字。
        if len(self.cache) == self.capacity:
            self.cache.popitem(last=False)
                
        # 如果不存在，则向缓存中插入该组 key-value
        self.cache[key] = value

    写法 2：有 move_to_end
    def put(self, key, value):
        # 如果关键字 key 已经存在，则变更其数据值 value
        if key in self.cache:
            self.cache.move_to_end(key)
        # 如果不存在，则向缓存中插入该组 key-value
        self.cache[key] = value
        # 如果插入操作导致关键字数量超过 capacity ，则应该 逐出 最久未使用的关键字。
        if len(self.cache) > self.capacity:
            self.cache.popitem(last=False)

```

```scala

/**
* chosen solution
* build-in linkedHashMap
* time complexity: O(1)
*/
class LRUCache0(_capacity: Int) {

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



/**
* build-in linkedHashMap
*/
class LRUCache3(_capacity: Int) {

  private val capacity = _capacity
  val cache = collection.mutable.LinkedHashMap[Int, Int]()

  def get(key: Int): Int = {
  /**
   *cache.get(key).map{
   *   value =>
   *     cache.remove(key)
   *     cache.update(key, value)
   *     value
   * }.getOrElse(-1)
   */
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

```scala
import scala.collection.mutable._

  class LRUCache(_capacity: Int) {

    val hm = HashMap[Int, Int]()
    val lb = ListBuffer.empty[Int]
    val c = _capacity

    def get(key: Int): Int = {
      if (hm.contains(key)) {
        val i = lb.indexOf(key)  // could be slow? O(N)?
        lb.remove(i)
        lb += key
        hm(key)
      } else {
        -1
      }

    }

    def put(key: Int, value: Int) {
      if (hm.contains(key)) {
        val i = lb.indexOf(key)  // could be slow? O(N)?
        lb.remove(i)
        lb += key
        hm(key) = value
      } else {
        if (hm.size == c) {
          val lk = lb.head
          hm.remove(lk)
          lb.remove(0)
        }
        hm(key) = value
        lb += key
      }
    }
  }


// test case
//  ["LRUCache","put","put","put","put","put","get","put","get","get","put","get","put","put","put","get","put","get","get","get","get","put","put","get","get","get","put","put","get","put","get","put","get","get","get","put","put","put","get","put","get","get","put","put","get","put","put","put","put","get","put","put","get","put","put","get","put","put","put","put","put","get","put","put","get","put","get","get","get","put","get","get","put","put","put","put","get","put","put","put","put","get","get","get","put","put","put","get","put","put","put","get","put","put","put","get","get","get","put","put","put","put","get","put","put","put","put","put","put","put"]
//  [[10],[10,13],[3,17],[6,11],[10,5],[9,10],[13],[2,19],[2],[3],[5,25],[8],[9,22],[5,5],[1,30],[11],[9,12],[7],[5],[8],[9],[4,30],[9,3],[9],[10],[10],[6,14],[3,1],[3],[10,11],[8],[2,14],[1],[5],[4],[11,4],[12,24],[5,18],[13],[7,23],[8],[12],[3,27],[2,12],[5],[2,9],[13,4],[8,18],[1,7],[6],[9,29],[8,21],[5],[6,30],[1,12],[10],[4,15],[7,22],[11,26],[8,17],[9,29],[5],[3,4],[11,30],[12],[4,29],[3],[9],[6],[3,4],[1],[10],[3,29],[10,28],[1,20],[11,13],[3],[3,12],[3,8],[10,9],[3,26],[8],[7],[5],[13,17],[2,27],[11,15],[12],[9,19],[2,15],[3,16],[1],[12,17],[9,1],[6,19],[4],[5],[5],[8,1],[11,7],[5,2],[9,28],[1],[2,2],[7,4],[4,22],[7,24],[9,26],[13,28],[11,26]]

//  [null,null,null,null,null,null,-1,null,19,17,null,-1,null,null,null,-1,null,-1,5,-1,12,null,null,3,5,5,null,null,1,null,-1,null,30,5,30,null,null,null,-1,null,-1,24,null,null,18,null,null,null,null,-1,null,null,18,null,null,-1,null,null,null,null,null,18,null,null,-1,null,4,29,30,null,12,-1,null,null,null,null,29,null,null,null,null,17,22,18,null,null,null,-1,null,null,null,20,null,null,null,-1,18,18,null,null,null,null,20,null,null,null,null,null,null,null]
  class LRUCache3(_capacity: Int) {
    val hm = HashMap[Int, Node]()
    val dl = new DoublyLinkedList()
    val c = _capacity

    def get(key: Int): Int = {
      if (hm.contains(key)) {
        val node = hm(key)
        dl.erase(node)
        dl.push_front(node)
        node.v.v
      } else { // not found
        -1
      }
    }

    def put(key: Int, value: Int) {
      if (hm.contains(key)) {
        val node = hm(key)
        dl.erase(node)
        dl.push_front(node)
        node.v.v = value
      } else {
        if (hm.size == c) {
          val old = dl.tail
          if (old!=null) {
            dl.erase(old)
            hm.remove(old.v.k)
          }
        }
        val node = Node(KV(key,value),null,null)
        hm(key) = node
        dl.push_front(node)
      }
    }
  }
```

```scala
  class Test extends BaseExtension {
    def init {
      val lru = new LRUCache(2)
      lru.put(1,1)
      lru.put(2,2)
      println(lru.get(1) == 1)
    }

    val name = "146 LRU chache"
  }

//  ["LRUCache","put","put","get","put","get","put","get","get","get"]
//  [[2],[1,1],[2,2],[1],[3,3],[2],[4,4],[1],[3],[4]]
  class Test2 extends BaseExtension {
    def init {
      val lru = new LRUCache2(2)
      lru.put(2,1)
      lru.put(1,1)
      lru.put(2,3)
      lru.put(4,1)
      println(lru.get(1) == -1)
      println(lru.get(2) == 3)
    }
    val name = "146 LRU chache xxxx"
  }

  //  ["LRUCache","put","put","put","put","put","get","put","get","get","put","get","put","put","put","get","put","get","get","get","get","put","put","get","get","get","put","put","get","put","get","put","get","get","get","put","put","put","get","put","get","get","put","put","get","put","put","put","put","get","put","put","get","put","put","get","put","put","put","put","put","get","put","put","get","put","get","get","get","put","get","get","put","put","put","put","get","put","put","put","put","get","get","get","put","put","put","get","put","put","put","get","put","put","put","get","get","get","put","put","put","put","get","put","put","put","put","put","put","put"]
  //  [[10],[10,13],[3,17],[6,11],[10,5],[9,10],[13],[2,19],[2],[3],[5,25],[8],[9,22],[5,5],[1,30],[11],[9,12],[7],[5],[8],[9],[4,30],[9,3],[9],[10],[10],[6,14],[3,1],[3],[10,11],[8],[2,14],[1],[5],[4],[11,4],[12,24],[5,18],[13],[7,23],[8],[12],[3,27],[2,12],[5],[2,9],[13,4],[8,18],[1,7],[6],[9,29],[8,21],[5],[6,30],[1,12],[10],[4,15],[7,22],[11,26],[8,17],[9,29],[5],[3,4],[11,30],[12],[4,29],[3],[9],[6],[3,4],[1],[10],[3,29],[10,28],[1,20],[11,13],[3],[3,12],[3,8],[10,9],[3,26],[8],[7],[5],[13,17],[2,27],[11,15],[12],[9,19],[2,15],[3,16],[1],[12,17],[9,1],[6,19],[4],[5],[5],[8,1],[11,7],[5,2],[9,28],[1],[2,2],[7,4],[4,22],[7,24],[9,26],[13,28],[11,26]]
  //  [null,null,null,null,null,null,-1,null,19,17,null,-1,null,null,null,-1,null,-1,5,-1,12,null,null,3,5,5,null,null,1,null,-1,null,30,5,30,null,null,null,-1,null,-1,24,null,null,18,null,null,null,null,-1,null,null,18,null,null,-1,null,null,null,null,null,18,null,null,-1,null,4,29,30,null,12,-1,null,null,null,null,29,null,null,null,null,17,22,18,null,null,null,-1,null,null,null,20,null,null,null,-1,18,18,null,null,null,null,20,null,null,null,null,null,null,null]

  class Test3 extends BaseExtension {
    def init {
      val lru = new LRUCache3(10)
      lru.put(10,13)
      lru.put(3,17)
      lru.put(6,11)
      lru.put(10,5)
      lru.put(9,10)

      println(lru.get(1) == -1)
      println(lru.get(2) == 3)
    }
    val name = "146 LRU chache xxxx"
  }
```
 
## 3. 无重复字符的最长子串 【滑动窗口🔹】数组中重复的数字 Longest Substring Without Repeating Characters

[哈哈哈](https://www.bilibili.com/video/BV1h54y1B7No?spm_id_from=333.999.0.0)

[花花酱](https://www.bilibili.com/video/BV1CJ411G7Nn?spm_id_from=333.999.0.0)

[哈哈哈](https://www.bilibili.com/video/BV1va4y1J7Gx?spm_id_from=333.999.0.0)

[小梦想家](https://www.bilibili.com/video/BV1ob411n7mv?spm_id_from=333.999.0.0)

[小明](https://www.bilibili.com/video/BV18K411M7d2?spm_id_from=333.999.0.0)

[官方](https://www.bilibili.com/video/BV1DK4y1b7xp?spm_id_from=333.999.0.0)

方法一：暴力解法

* 时间复杂度: 2个指针遍历字符串O(n2) + hashset判断是否重复O(n) = O(n3)

* 时间复杂度: O(m), m 为所有可能出现的情况

方法二：涉及 sub 的问题，可以使用 “滑动窗口”

特殊情况：

* 字符串为空
  
* 字符串均为重复字符串

* 时间复杂度: O(n) + hashset判断是否重复O(n) = O(n3)

* 时间复杂度: O(m), m 为所有可能出现的情况

```py
class Solution:
    def lengthOfLongestSubstring(self, s: str) -> int:
        dic = {}
        start = 0
        res = 0
        for i, char in enumerate(s):
            if char in dic and start <= dic[char]:
                # 易错点: and start <= dic[char]: 
                # 含义为"tmmzuxt",
                # start在m，当有新的t进来时，上一个t在start的前面，所以，此时的start不需要修改
                start = dic[char] + 1 # 易错点: 这里的dic[char]还是前一个,且 +1
            else:
                res = max(res,i-start+1) # 易错点: +1
            dic[char] = i # 易错点: dic[char]滞后更新
        return res
```

```scala
/**
* chosen solution
* two pointer to control sliding window
*   1. two pointer: left and right to control substring window
*   2. counter and hashmap to record whether current window is valid or not
* time  complexity: O(N), worst: O(2N) -> each char was visited twice
*/

object Solution0 {
  def lengthOfLongestSubstring(s: String): Int = {
    val sMap = scala.collection.mutable.Map[Char, Int]() ++ s.distinct.map(c => (c, 0)).toMap
    var left = 0
    var right = 0
    var counter = 0
    var length = 0
    while (right < s.length) {
      val rightChar = s(right)
      sMap.get(rightChar) match {
        case Some(v) if v >= 1 =>
          sMap.update(rightChar, v + 1)
          counter += 1
        case Some(v) =>
          sMap.update(rightChar, v + 1)
      }
      right += 1
      while (counter > 0) {
        val leftChar = s(left)
        sMap.get(leftChar) match {
          case Some(v) if v > 1 =>
            sMap.update(leftChar, v - 1)
            counter -= 1
          case Some(v) =>
            sMap.update(leftChar, v - 1)
        }

        left += 1

      }
      length = length max (right - left)
    }
    length
  }
}


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


/**
* sliding windows, slower than solution1
*   memo
*     1. using hashmap to record whether the current right char is duplicated or not
*/
object Solution1-2 {
    def lengthOfLongestSubstring(s: String): Int = {
        val map = scala.collection.mutable.Map[Char, Int]() ++ s.distinct.map(c => (c, 0))
        var left = 0
        var right = 0
        var length = 0
        
        while(right < s.length){
            val rightChar = s(right)
        
            map.update(rightChar, map(rightChar) + 1)
            right += 1
            
            /* iterate until meet condition */
            while(map(rightChar) > 1){
                val leftChar = s(left)
                
                map.get(leftChar) match {
                    case Some(v) if v > 0 =>  map.update(leftChar,  v - 1)
                    case _ =>
                }
                
                left += 1
            }

            length = length max (right - left)  // update minimum
               
        }
        length
    }
}

/**
* using substring problem template
*   1. two pointer: left and right to control substring window
*   2. counter and hashmap to record whether current window is valid or not
*/
object Solution1-3 {
  def lengthOfLongestSubstring(s: String): Int = {
    val sMap = scala.collection.mutable.Map[Char, Int]() ++ s.distinct.map(c => (c, 0)).toMap
    var left = 0
    var right = 0
    var counter = 0
    var length = 0
    while (right < s.length) {
      val rightChar = s(right)
      sMap.get(rightChar) match {
        case Some(v) if v >= 1 =>
          sMap.update(rightChar, v + 1)
          counter += 1
        case Some(v) =>
          sMap.update(rightChar, v + 1)
      }
      right += 1
      while (counter > 0) {
        val leftChar = s(left)
        sMap.get(leftChar) match {
          case Some(v) if v > 1 =>
            sMap.update(leftChar, v - 1)
            counter -= 1
          case Some(v) =>
            sMap.update(leftChar, v - 1)
        }

        left += 1

      }
      length = length max (right - left)
    }
    length
  }
}

object Solution {
    //s.zipWithIndex.foreach(println) // =>tuple
    //   def foldLeft[B](z: B)(op: (B, A) => B): B = {
    // 解释 z: 初始值,
    // op (B,A) => B前一个结果，A本次输入,返回作为下一个输入
    def lengthOfLongestSubstring(s: String): Int = {
      s.zipWithIndex.foldLeft((0, -1, Map[Char, Int]())) {
        case ((len, start_pos, map), (char, i)) => {
          // 初始值len=0,start_pos=-1,map为空; case A,B; 前者为累加值，后者为index
          // 如果char不存在,last_pos=-1,更新map中的idx,len=i-start_pos
          // 如果last_pos已存在,例如abca,第一个a为0,第二个a为3,则len=3-0,跟新start_pos
          val last_pos = map.getOrElse(char, -1)
          if (last_pos >= start_pos) (len.max(i - last_pos), last_pos, map + (char -> i))
          else (len.max(i - start_pos), start_pos, map + (char -> i))
        }
      }._1
    }
  }

  class Test extends BaseExtension {
    def init {
      println(Solution.lengthOfLongestSubstring("abcabcbb")==3)
    }
    val name = "003 Longest Non repeat str"
  }
```

## 215. 数组中的第K个最大元素（add）

```py
python版-大根堆法，希望可以帮到你

class Solution:
    def findKthLargest(self, nums: List[int], k: int) -> int:

        def adju_max_heap(nums_list, in_node):  # 从当前内部节点处修正大根堆
            """"in_node是内部节点的索引"""
            l, r, large_idx= 2*in_node+1, 2*in_node+2, in_node  # 最大值的索引默认为该内部节点

            if l < len(nums_list) and nums_list[large_idx] < nums[l]:  
                # 如果左孩子值大于该内部节点的值，则最大值索引指向左孩子
                large_idx = l
            if r < len(nums_list) and nums_list[large_idx] < nums[r]:
                # 如果执行了上一个if语句，此时最大值索引指向左孩子，否则还是指向该内部节点
                # 然后最大值索引指向的值和右孩子的值比较
                large_idx = r

            # 上述两个if就是得到(内部节点，左孩子，右孩子)中最大值的索引
            if large_idx != in_node: # 如果最大值在左孩子和右孩子中，则和内部节点交换
                nums_list[large_idx], nums_list[in_node] = nums_list[in_node], nums_list[large_idx]
                # 如何内部节点是和左孩子交换，那就递归修正它的左子树，否则递归修正它的右子树
                adju_max_heap(nums_list, large_idx)

        def build_max_heap(nums_list):  # 由列表建立大根堆
            """"从后往前遍历所有内部节点，其中最后一个内部节点的公式为len(nums_list)//2 - 1"""
            for in_node in range(len(nums_list)//2 - 1, -1, -1):
                adju_max_heap(nums_list, in_node)
        
        def find_kth_max(nums_list, k):  # 从列表中找到第k个最大的
            build_max_heap(nums_list)  # 先建立大根堆
            for _ in range(k-1):
                nums_list[0], nums_list[-1] = nums_list[-1], nums_list[0]  # 堆头和堆尾交换
                nums_list.pop()  # 删除堆尾
                adju_max_heap(nums_list, 0)  # 从堆头处开始修正大根堆
            return nums_list[0]
        return find_kth_max(nums, k)  
                
```

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
```

```py
# 基于快速排序
import random


class Solution:
    def findKthLargest(self, nums: List[int], k: int) -> int:
        return self.quickSelect(nums, 0, len(nums)-1, len(nums)-k)

    def quickSelect(self, nums, start, end, k):
        nums, idx = self.partition(nums, start, end)
        if idx == k:
            return nums[idx]
        elif idx < k:
            return self.quickSelect(nums, idx+1, end, k)
        else:
            return self.quickSelect(nums, start, idx-1, k)

    def partition(self, nums, start, end):
        t = random.randint(start, end)
        nums[start], nums[t] = nums[t], nums[start]
        pivot = nums[start]
        left, right = start + 1, end
        while True:
            while left <= right and nums[left] <= pivot:
                left += 1
            while left <= right and nums[right] >= pivot:
                right -= 1
            if left <= right:
                nums[left], nums[right] = nums[right], nums[left]
            else:
                break
        nums[start], nums[right] = nums[right], nums[start]
        return nums, right
"""

"""
# 基于最大堆（调库）
import heapq


class Solution:
    def findKthLargest(self, nums: List[int], k: int) -> int:
        q = []
        for i in range(k):
            heapq.heappush(q, nums[i])
        for i in range(k, len(nums)):
            if nums[i] > q[0]:
                heapq.heapreplace(q, nums[i])
        return q[0]
"""

# 基于最大堆（手动）
class Solution:
    def findKthLargest(self, nums: List[int], k: int) -> int:
        heapList = self.buildHeap(nums)
        for _ in range(k - 1):
            heapList = self.delMax(heapList)
        return heapList[1]

    def buildHeap(self, nums):
        heapList = [0] + nums
        size = len(nums)
        i = size // 2
        while i >= 1:
            heapList = self.percDown(i, size, heapList)
            # print(heapList)
            i -= 1
        return heapList

    def percDown(self, i, size, heapList):
        while i * 2 <= size:
            mc = self.maxChild(i, size, heapList)
            if heapList[i] < heapList[mc]:
                heapList[i], heapList[mc] = heapList[mc], heapList[i]
                i = mc
            else:
                break
        return heapList

    def maxChild(self, i, size, heapList):
        if i * 2 + 1 > size:
            return i * 2
        else:
            if heapList[i * 2] >= heapList[i * 2 + 1]:
                return i * 2
            else:
                return i * 2 + 1

    def delMax(self, heapList):
        heapList[1] = heapList[-1]
        heapList.pop()
        size = len(heapList) - 1
        heapList = self.percDown(1, size, heapList)
        return heapList

# 基于最小堆
class Solution:
    def findKthLargest(self, nums: List[int], k: int) -> int:
        heap = [0]
        for i in range(k):
            heap.append(nums[i])
            heap = self.up(heap, len(heap)-1)
        # print(heap)
        for i in range(k, len(nums)):
            if nums[i] > heap[1]:
                heap[1] = nums[i]
                heap = self.down(heap, 1)
            # print(heap)
        return heap[1]

    def up(self, heap, i):
        while i > 1:
            if heap[i] < heap[i // 2]:
                heap[i], heap[i // 2] = heap[i // 2], heap[i]
                i = i // 2
            else:
                break
        return heap

    def down(self, heap, i):
        while i * 2 < len(heap):
            mc = self.minChild(heap, i)
            if heap[i] > heap[mc]:
                heap[i], heap[mc] = heap[mc], heap[i]
                i = mc
            else:
                break
        return heap

    def minChild(self, heap, i):
        if i * 2 + 1 > len(heap) - 1:
            return i * 2
        else:
            if heap[i * 2] <= heap[i * 2 + 1]:
                return i * 2
            else:
                return i * 2 + 1

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
