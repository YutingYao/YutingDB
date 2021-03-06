
###382. Linked List Random Node


题目:
<https://leetcode-cn.com/problems/linked-list-random-node/>


难度:

Medium



tag：reservoir sampling 水塘抽样 


思路：

n选k


这样来看，有k个元素，那么这个时候全部选中,当第k+1个元素进来的时候，生成一个随机数r，如果 r <= k，那么用它来替换第r个元素

那么r被替换掉的概率是 1 / k + 1, 不被替换掉的概率是 k / k + 1 (不生成r)

k+2来继续： 被替换掉的概率  1 / k + 2, 不被替换掉的概率  (k + 1) / (k+2)

所以最终被选中的（不被替换掉的概率是） k / n

随机 √


针对这道题目来看

- 一开始选head为choice
- 出现第二个，生成[1,2]之间的随机数，如果r = 2，则用新的来替换choice
- 出现第三个，生成[1,2,3]之间的随机数，如果r = 3，则替换

再写简单一点就是


每次以 1/i 来决定是否用新的元素来替换选中元素，那么就是 i - 1 / i 不替换，它之前被选中的概率就是  1 / i-1 ，所以最终被选中的概率是 1/i

这个对于linked list更优之处在于它不用reverse

时间复杂度 O(N)， 空间复杂度O(K)


然后AC


```py
class Solution(object):

    def __init__(self, head):
        """
        @param head The linked list's head.
        Note that the head is guaranteed to be not null, so it contains at least one node.
        :type head: ListNode
        """
        self.head = head

    def getRandom(self):
        """
        Returns a random node's value.
        :rtype: int
        """
        choice = self.head 
        cur = self.head
        i = 1
        while cur.next:
            cur = cur.next
            i += 1
            rd = random.randint(1,i)
            if rd == i:
                choice = cur
        return choice.val
```
