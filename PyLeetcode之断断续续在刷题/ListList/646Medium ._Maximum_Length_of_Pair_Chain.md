#  646. Maximum Length of Pair Chain
**<font color=red>难度: 中等</font>**

## 刷题内容

> 原题连接

* https://leetcode-cn.com/problems/maximum-length-of-pair-chain

> 内容描述

```
You are given n pairs of numbers. In every pair, the first number is always smaller than the second number.

Now, we define a pair (c, d) can follow another pair (a, b) if and only if b < c. Chain of pairs can be formed in this fashion.

Given a set of pairs, find the length longest chain which can be formed. You needn't use up all the given pairs. You can select pairs in any order.

Example 1:
Input: [[1,2], [2,3], [3,4]]
Output: 2
Explanation: The longest chain is [1,2] -> [3,4]
Note:
The number of given pairs will be in the range [1, 1000].
```

## 解题方案

> 思路 1

先按照start --> end中的end排序，即```pairs = sorted(pairs, key=lambda x:x[1])```

原因在于，你想想看，如果我们已经取得了最长的那一条序列，end最小的那个pair肯定在里面对不对？
如果你说不对，那假设它不在里面，现在max_chain的第一个pair是不是至少可以被end最小的那个pair替代，因为我的end比你更小，你都可以我为什么不可以，甚至如果我的
end比你的start小的话，我还可以加在你前面呢，这样max_chain岂不是就不是最长的chain了？

综上所述，我们只要先按照start --> end中的end排序，然后从第一个慢慢向下判断就行，如果不符合就跳过，符合我们就把长度加1，这样最后肯定是对的。

AC代码如下：


```python
class Solution(object):
    def findLongestChain(self, pairs):
        """
        :type pairs: List[List[int]]
        :rtype: int
        """
        if not pairs or len(pairs) == 0:
            return 0
        pairs = sorted(pairs, key=lambda x:x[1])
        res, i = 0, -1
        while i + 1 < len(pairs):
            res += 1
            i += 1
            cur_end = pairs[i][1]
            while i + 1 < len(pairs) and pairs[i+1][0] <= cur_end:
                i += 1
        return res
```

发现有大佬比我牛p多了，代码更nice

```python
class Solution(object):
    def findLongestChain(self, pairs):
        """
        :type pairs: List[List[int]]
        :rtype: int
        """
        if not pairs or len(pairs) == 0:
            return 0
        cur, res = float('-inf'), 0
        for p in sorted(pairs, key=lambda x: x[1]):
            if cur < p[0]: cur, res = p[1], res + 1
        return res
```




> 思路 2

动态规划

思路看代码就理解了，不宜多说



```py
class Solution(object):
    def findLongestChain(self, pairs):
        """
        :type pairs: List[List[int]]
        :rtype: int
        """
        if not pairs or len(pairs) == 0:
            return 0
        pairs = sorted(pairs, key=lambda x:x[0])
        dp = [1] * len(pairs)
        for i in range(1, len(pairs)):
            for j in range(i):
                dp[i] = max(dp[i], dp[j] + 1 if pairs[i][0] > pairs[j][1] else dp[j])
        return dp[-1]
```
这样会超时，不知道为啥，然后改了下代码的写法就过了，beats 2.21% 哈哈哈哈哈, 管它呢，过了就行，代码如下：

```python
class Solution(object):
    def findLongestChain(self, pairs):
        """
        :type pairs: List[List[int]]
        :rtype: int
        """
        if not pairs or len(pairs) == 0:
            return 0
        pairs = sorted(pairs, key=lambda x:x[0])
        dp = [1] * len(pairs)
        for i in range(1, len(pairs)):
            dp[i] = max([dp[j] + 1 if pairs[i][0] > pairs[j][1] else dp[j] for j in range(i)])
        return dp[-1]
```


