### 384. Shuffle an Array



题目： 
<https://leetcode-cn.com/problems/shuffle-an-array/>



难度 : Medium



思路：



这就是洗牌算法吧，洗牌算法几种常见的：

<http://www.cnblogs.com/tudas/p/3-shuffle-algorithm.html>

http://www.matrix67.com/blog/archives/879



也是有wikipedia page的: <https://en.wikipedia.org/wiki/Fisher–Yates_shuffle>

最简单的算法是很容易想到的， O(N^2)

然后就是modern 算法：



```
-- To shuffle an array a of n elements (indices 0..n-1):
for i from n−1 downto 1 do
     j ← random integer such that 0 ≤ j ≤ i
     exchange a[j] and a[i]
```



这个感觉还是比较容易证明的，一开始生成的数字 1/n 概率

没选中，下一个 n-1 /n * 1/ n-1  =  1/n， 所以每个位置都是等概率的？



这个有很妙的点：

比如五个人顺序抽签，只要不uncover 结果，那么就是等概率的。



但是第一个人抽奖之后uncover结果，比如他没有抽中 → 那么概率就会变。







AC代码：

```py
class Solution(object):

    def __init__(self, nums):
        """
        
        :type nums: List[int]
        :type size: int
        """
        self.lst = nums
        

    def reset(self):
        """
        Resets the array to its original configuration and return it.
        :rtype: List[int]
        """
        return self.lst
        

    def shuffle(self):
        """
        Returns a random shuffling of the array.
        :rtype: List[int]
        """
        import random
        res = self.lst[:]
        n = len(res)
        for i in range(n-1,0,-1):
            j = random.randint(0,i)
            res[i], res[j] = res[j], res[i]
        return res
        
```







