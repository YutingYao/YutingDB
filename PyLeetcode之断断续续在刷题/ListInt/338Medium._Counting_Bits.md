### 338. Counting Bits

题目:

<https://leetcode-cn.com/problems/counting-bits/>

难度:
Medium



**O(n\*sizeof(integer))** 算法，其实就是把count of 1 bit拿来用：

```py
class Solution(object):
    def countBits(self, num):
        """
        :type num: int
        :rtype: List[int]
        """
        def hammingWeight(n):
        	cnt = 0
        	while n != 0:
        		n &= n -1
        		cnt += 1
        	return cnt

        res = []
        for i in range(num+1):
        	res.append(hammingWeight(i))
        return res

```



DP算法 - to be done









