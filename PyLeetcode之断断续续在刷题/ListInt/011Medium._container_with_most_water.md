### 11. Container With Most Water



题目:
<https://leetcode-cn.com/problems/container-with-most-water/>


难度:
Medium

思路：


首先理解花了我一点时间，因为一开始写出来，给了一个例子：

```

height = [3,2,1,3]
解是 9


|			|
|	|		|
|	|	|	|
1       2       3       4
    
 一开始我的理解走偏的地方是这个9是如何得到的，因为根据最短板原理，明显不可能得到9啊，后来发现是·Find two lines, which together with x-axis forms a container, such that the container contains the most water.
```

所以代码写起来就简单了,AC无能，超时，时间复杂度O(N^2)


```py
class Solution(object):  # 此法超时
    def maxArea(self, height):
        """
        :type height: List[int]
        :rtype: int
        """
        n = len(height)
        most_water = 0
        for i in range(n-1):
            for j in range(i, n):
                water = (j-i) * min(height[i], height[j])
                most_water = max(water, most_water)
        return most_water

```

题目给的tag是 two pointer，所以上边的策略肯定可以改进，改进的地方就不能是这个一次走一边，而可能是两边都要走。



参考 <http://bangbingsyb.blogspot.com/2014/11/leetcode-container-with-most-water.html>


思路：

由于ai和aj (i<j) 组成的container的面积：S(i,j) = min(ai, aj) * (j-i)

所以对于任何```S(i'>=i, j'<=j) >= S(i,j)```，由于```j'-i' <= j-i```，必然要有```min(ai',aj')>=min(ai,aj)```才行。同样可以采用头尾双指针向中间移动：

当```a(left) < a(right)```时，对任何```j<right```来说

1. ```min(a(left),aj) <= a(left) = min(a(left), a(right))```
2. ```j-left < right-left```

所以S(left, right) > S(left, j<right)。

这就排除了所有以left为左边界的组合，因此需要右移left。`这里证明的非常好。` a[left] < a[right]，需要右移left.

`同理，当a(left) > a(right)时，需要左移right`。

`而当a(left) = a(right)时，需要同时移动left和right。`

思路整理：
left = 0, right = n-1
1. a[left] < a[right], left++
2. a[left] > a[right], right--
3. a[left] = a[right], left++, right--
终止条件：left >= right

这个证明大快人心


这样写也能过:


```python
class Solution(object):
    def maxArea(self, height):
        """
        :type height: List[int]
        :rtype: int
        """
        n = len(height)
        left, right = 0, n-1
        most_water = 0
        while left <= right:
            water = (right - left) * min(height[left], height[right])
            most_water = max(water, most_water)
            if height[left] < height[right]:
                left += 1
            elif height[left] > height[right]:
                right -= 1
            else:
                left += 1
                right -= 1
        return most_water
        
```
