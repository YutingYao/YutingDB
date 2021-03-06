###264. Ugly Number II



题目:
<https://leetcode-cn.com/problems/ugly-number-ii/>


难度:
Medium


思路：

暴力算法一定会超时




先看一个非常🐂的帖子，当我知道python 除了有list之外还有collections就已经被刷新了一次了，然后数据结构 x 数据结构，就展示了数据结构的魅力

<http://stackoverflow.com/questions/4098179/anyone-know-this-python-data-structure>


看一下deque + rotate：


```python
import collections
a = collections.deque()
a.append(2)
a.append(5)
a.append(7)
a.append(9)  #a  deque([2, 5, 7, 9])

import bisect
idx = bisect.bisect_left(a,3) #1
a.rotate(-idx) #deque([5, 7, 9, 2])

a.appendleft(3) #deque([3, 5, 7, 9, 2])

a.rotate(idx) # deque([2, 3, 5, 7, 9])
```

这个rotate -是往左边rotate，看官网的介绍.

>Rotate the deque n steps to the right. If n is negative, rotate to the left. Rotating one step to the right is equivalent to: d.appendleft(d.pop()).

所以这样造成可以🐂的数据结构

用这个微调之后的fasttable来解决问题


```python
import collections
import bisect

class FastTable:

    def __init__(self):
        self.__deque = collections.deque()

    def __len__(self):
        return len(self.__deque)

    def head(self):
        return self.__deque.popleft()

    def tail(self):
        return self.__deque.pop()

    def peek(self):
        return self.__deque[-1]

    def insert(self, obj):
        if obj in self.__deque:
            return
        index = bisect.bisect_left(self.__deque, obj)
        self.__deque.rotate(-index)
        self.__deque.appendleft(obj)
        self.__deque.rotate(index)

class Solution(object):
   
    def nthUglyNumber(self, n):
        """
        :type n: int
        :rtype: int
        """
        q = FastTable()
        q.insert(1)
        while n > 0:
        	x = q.head()
        	q.insert(2*x)
        	q.insert(3*x)
        	q.insert(5*x)
        	n -= 1
        return x
```


还可以优化：
根据页面hint 来做的


```python
class Solution(object):
    def nthUglyNumber(self, n):
        """
        :type n: int
        :rtype: int
        """
        if n == 1:
            return 1
        else:
            import collections
            q2 = collections.deque()
            q3 = collections.deque()
            q5 = collections.deque()
            q2.append(2)
            q3.append(3)
            q5.append(5)
            while n > 1:
                    x = min(q2[0],q3[0],q5[0])
                    if x == q2[0]:
                            x = q2.popleft()
                            q2.append(2*x)
                            q3.append(3*x)
                            q5.append(5*x)
                    elif x == q3[0]:
                            x = q3.popleft()
                            q3.append(3*x)
                            q5.append(5*x)
                    else:
                            x = q5.popleft()
                            q5.append(5*x)
                    n -= 1
            return x
```

