### 421. Maximum XOR of Two Numbers in an Array

题目:
<https://leetcode-cn.com/problems/maximum-xor-of-two-numbers-in-an-array/>


难度:

Medium

题目要求O(N)时间

看了半天的解法居然超时，
```python
class Solution(object): # 此法超时
    def findMaximumXOR(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        '''The maxResult is a record of the largest XOR we got so far. if it's 11100 at i = 2, it means 
            before we reach the last two bits, 11100 is the biggest XOR we have, and we're going to explore
            whether we can get another two '1's and put them into maxResult'''
        max_res, mask = 0, 0
        
        '''This is a greedy part, since we're looking for the largest XOR, we start 
            from the very begining, aka, the 31st postition of bits.'''
        for i in range(32)[::-1]:
            
            '''The mask will grow like  100..000 , 110..000, 111..000,  then 1111...111
                for each iteration, we only care about the left parts'''
            mask |= (1 << i)
            tmp = []
            for num in nums:
                '''we only care about the left parts, for example, if i = 2, then we have
                    {1100, 1000, 0100, 0000} from {1110, 1011, 0111, 0010}'''
                tmp.append(num & mask)
            
                '''if i = 1 and before this iteration, the maxResult we have now is 1100, 
                    my wish is the maxResult will grow to 1110, so I will try to find a candidate
                    which can give me the greedyTry;'''
            greedy_try = max_res | (1 << i)
                
            for i in tmp:
                '''This is the most tricky part, coming from a fact that if a ^ b = c, then a ^ c = b;
                now we have the 'c', which is greedyTry, and we have the 'a', which is leftPartOfNum
                If we hope the formula a ^ b = c to be valid, then we need the b, 
                and to get b, we need a ^ c, if a ^ c exisited in our set, then we're good to go'''
                if i ^ greedy_try in tmp:
                    max_res = greedy_try
            '''If unfortunately, we didn't get the greedyTry, we still have our max, 
                So after this iteration, the max will stay at 1100.'''
        return max_res
```


只好想别的办法


参考[stefan](https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/discuss/91050?page=3)
```python
class Solution(object):
    def findMaximumXOR(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        answer = 0
        for i in range(32)[::-1]:
            answer <<= 1
            prefixes = {num >> i for num in nums}
            answer += any(answer^1 ^ p in prefixes for p in prefixes)
        return answer
```
