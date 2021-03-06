###238. Product of Array Except Self

题目:
<https://leetcode-cn.com/problems/product-of-array-except-self/>


难度:

Medium


不使用division 并且O(n)


想到的算法 O(n^2)

会超时


```py
class Solution(object):
        def productExceptSelf(self,nums):
            """
            :type nums: List[int]
            :rtype: List[int]
            """
            lst = []
            for i in range(len(nums)):
                lst.append(self.productWithoutI(nums,i))
            return lst
        

        def productWithoutI(self,nums,i):
            product = 1
            for j in range(len(nums)):
                if j != i:
                    product *= nums[j]
            return product
```

如果用除法，也会有问题，如果有0出现也会变繁琐。

谷歌一下：


解法还是很棒的

    output[i] =  { i 前面的数的乘积}  X  { i 后面的数的乘积}


```py
class Solution(object):
        def productExceptSelf(self,nums):
            """
            :type nums: List[int]
            :rtype: List[int]
            """
            if nums == [] : return []  
            lft = [1]
            rgt = [1]
            product = 1 
            for i in range(1,len(nums)):
                product *= nums[i-1]
                lft.append(product)
            product = 1 
            for i in reversed(range(1,len(nums))):
                product *= nums[i]
                rgt.append(product)
            rgt.reverse()
            result = []
            for i in range(len(nums)):
                result.append(lft[i]*rgt[i])
            return result
            
```


空间O（n），再看到满足要求的“标准解法”


```py
class Solution(object):
        def productExceptSelf(self,nums):
            """
            :type nums: List[int]
            :rtype: List[int]
            """
            if nums == [] : return []  
            size = len(nums)
            output = [1] * size
            left = 1
            for x in range(size-1):
                left *= nums[x]
                output[x+1] *= left
            right = 1
            for x in range(size - 1, 0, -1):
                right *= nums[x]
                output[x-1] *= right
            return output
```