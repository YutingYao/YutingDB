###77. Combinations


题目： 
<https://leetcode-cn.com/problems/combinations/>


难度 : Medium


思路一：
python作弊法

```
import itertools
p = [4, 8, 15, 16, 23, 42]
c = itertools.combinations(p, 4)
for i in c:
  print i
 
结果：

(4, 8, 15, 16)
(4, 8, 15, 23)
(4, 8, 15, 42)
(4, 8, 16, 23)
(4, 8, 16, 42)
(4, 8, 23, 42)
(4, 15, 16, 23)
(4, 15, 16, 42)
(4, 15, 23, 42)
(4, 16, 23, 42)
(8, 15, 16, 23)
(8, 15, 16, 42)
(8, 15, 23, 42)
(8, 16, 23, 42)
(15, 16, 23, 42)
```

作弊AC代码:

```py
class Solution(object):
    def combine(self, n, k):
        """
        :type n: int
        :type k: int
        :rtype: List[List[int]]
        """
        import itertools
        return [list(i) for i in itertools.combinations(range(1,n+1), k)]
```


思路二：

标准的recursion

但是会超时


```py
class Solution(object):
    def combine(self, n, k):
        """
        :type n: int
        :type k: int
        :rtype: List[List[int]]
        """
        ans = []
        self.dfs(n, k, 1, [], ans)
        return ans

    def dfs(self, n, k ,start, lst, ans):
    	if k == 0 :
    		ans.append(lst)
    		return
    	for i in range(start, n+1):
    		self.dfs(n, k - 1, i + 1,lst +[i], ans)
```

理解方式

```

					1          2     3
			    12  13 14    23 24   34			
```

可以参照这里


<http://www.geeksforgeeks.org/print-all-possible-combinations-of-r-elements-in-a-given-array-of-size-n/>


解法三：


采用递归的方式，在n个数中选k个，如果n大于k，那么可以分类讨论，如果选了n，那么就是在1到(n-1)中选(k-1)个，否则就是在1到(n-1)中选k个。递归终止的条件是k为1，这时候1到n都符合要求。

注意一开始这里的else part花了我一点时间来理解，因为n必定大于k，所以这样递归当 n == k的时候选法就是code原作者的写法，也就是直接[range(1,k+1)]

参考这里： <https://shenjie1993.gitbooks.io/leetcode-python/content/077%20Combinations.html>


```py
class Solution(object):
    def combine(self, n, k):
        """
        :type n: int
        :type k: int
        :rtype: List[List[int]]
        """
        if k == 1:
            return [[i + 1] for i in range(n)]
        result = []
        if n > k:
            result = [r + [n] for r in self.combine(n - 1, k - 1)] + self.combine(n - 1, k)
        else: #n == k 
        	# result = [r + [n] for r in self.combine(n - 1, k - 1)]
            result = [range(1,k+1)]
        return result
```