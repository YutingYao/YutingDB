###89. Gray Code



题目:
<https://leetcode-cn.com/problems/gray-code/>


难度:
Medium

思路：

首先不是从任何一个数开始都是有效的，所以naive的想法是从任何一个开始，然后如果能到2^n位，那么说明是有效的，问题解决.

A gray code sequence must begin with 0. ->简化了一点

先写了一段代码：

```py
def nextCode(curCode, res, n):
        if curCode not in res:
		res.append(curCode)
	else:
		return
	if len(res) == pow(2,n):
		return res
	for i in range(n):
		nCode = curCode[:]
		nCode[i] = 1 if curCode[i] == 0 else 0
		nextCode(nCode,res,n)

res = []
nextCode([0,0,0],res,3)
print res
#[[0, 0, 0], [1, 0, 0], [1, 1, 0], [0, 1, 0], [0, 1, 1], [1, 1, 1], [1, 0, 1], [0, 0, 1]]
```
实际上问题是这段代码的时间复杂度感觉很高，但是试试


不失所望，到11就超时

```py

class Solution(object):
    def grayCode(self, n):
        """
        :type n: int
        :rtype: List[int]
        """
        def nextCode(curCode, res, n):
                if curCode not in res:
                        res.append(curCode)
                else:
                        return
                if len(res) == pow(2,n):
                        return res
                for i in xrange(n):
                        nCode = curCode[:]
                        nCode[i] = 1 if curCode[i] == 0 else 0
                        nextCode(nCode, res, n)

        def listoVal(curCode,n):
                val = 0
                for i in range(n-1,-1,-1):
                        val += pow(2,i) * curCode[i]
                return val


        res = []
        nextCode([0]*n, res, n)
        # print res

        val = []
        for i in res:
                val.append(listoVal(i,n))
        return val
```

然后居然有这个东西：
Gray code，要用位运算！瞑目


<https://en.wikipedia.org/wiki/Gray_code>

服气，这个待研究
```py
class Solution(object):
    def grayCode(self, n):
        """
        :type n: int
        :rtype: List[int]
        """
        result = [(i>>1)^i for i in range(pow(2,n))]
        return results
```





