### 413. Arithmetic Slices



题目： 
<https://leetcode-cn.com/problems/arithmetic-slices/>



难度 : Medium



思路：

tag 是DP

数从 i 到 j 之间的这个arithmetic 数

我的方法时间复杂度比较高O(N^2)，从 i 开始数它的arithmetic slice，每个i数一遍，到 j

AC代码

```py
class Solution(object):
    def numberOfArithmeticSlices(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        n = len(A)
        if n < 3:
        	return 0
        else:
        	res = 0
        	for i in range(n-2):
        		for j in range(i+2,n):
        			if A[j] - A[j-1] == A[i+1] - A[i]:
        				res += 1
        			else:
        				break
        	return res
```



应该可以优化到O(N)

不需要每个每个开始数，可以边数边移动

可以参考<http://www.cnblogs.com/grandyang/p/5968340.html>



O(N) 代码

```
class Solution(object):
    def numberOfArithmeticSlices(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        n = len(A)
        if n < 3:
        	return 0
        else:
        	res, cnt = 0, 2
        	for i in range(2, n):
        		if A[i] - A[i-1] == A[i-1] - A[i-2]:
        			print i, i-1, i-2
        			cnt += 1
        		else:
        			if cnt > 2:
        				res += (cnt-1) * (cnt-2)  / 2
        			cnt = 2
        	if cnt > 2: res += (cnt-1) * (cnt-2)  / 2
        	return res


```



