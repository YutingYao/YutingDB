### 477. Total Hamming Distance



题目:
<https://leetcode-cn.com/problems/total-hamming-distance/>


难度:
Medium

思路：


第一想法就是暴力，直接超时

```python
class Solution(object): # 此法超时
    def totalHammingDistance(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        res = 0
        for i in range(len(nums)):
            for j in range(i+1, len(nums)):
                res += bin(nums[i]^nums[j]).count('1')
        return res
```


前面的解法是```O(n^2)```所以超时，所以我们想想有没有```O(n)```的解法
对于所有的数字，我们先从右数第一位开始算，如果一共有```n```个数字，其中```k```个数字的右数第一位是```‘1’```，其他```n-k```个数字的右数第一位是```‘0’```，
所以这一位对最终```res```的贡献就是```k*(n-k)```,这样我们的时间复杂度就是```O(32n)```，也就是```O(N)```了

```
for each “column” or bit position, once you count the number of set bits you can figure out the number of pairs 
that will contribute to the count using combination logic.

Consider you have 10 numbers and only one of them is a 1 the rest are zeros. How many (1, 0) pairs can you make? 
Clearly you can make 9, pair the 1 with each of the other 9 zeros. If you have 2 ones, 
you can pair each of those with the other 8 zeros giving 2*8 = 16. 
Keep going and you see that you can pair each 1 with each zero so the number of pairs is just the number of 1’s times the number of 0’s.

This would be an O(32 * n) solution which is an O(n) solution, no space used.
```

AC代码

```python
class Solution(object):
    def totalHammingDistance(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # iterate thru "column" or bit position
        # Note: you could stop at 10^9 as stated in the problem if you want to optimize
        res = 0
        for i in range(32):
            mask = 1 << i
            count_ones, count_zeros = 0, 0
            for num in nums:
                if num & mask != 0:
                    count_ones += 1
                else:
                    count_zeros += 1
            res += count_ones * count_zeros
        return res
```

上面的代码简化一下就是[stefan大神(老流氓罒ω罒)](https://leetcode.com/problems/total-hamming-distance/discuss/96229)的无敌一行了

```python
class Solution(object): # 此法超时
    def totalHammingDistance(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        return sum(b.count('0') * b.count('1') for b in zip(*map('{:032b}'.format, nums)))
```
