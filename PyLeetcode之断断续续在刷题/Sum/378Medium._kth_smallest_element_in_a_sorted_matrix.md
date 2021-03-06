### 378. Kth Smallest Element in a Sorted Matrix



题目:
<https://leetcode-cn.com/problems/kth-smallest-element-in-a-sorted-matrix/>


难度:
Medium



### 思路一：暴力法

```python
class Solution(object):
    def kthSmallest(self, matrix, k):
        """
        :type matrix: List[List[int]]
        :type k: int
        :rtype: int
        """
        tmp = []
        for row in matrix:
            for column in row:
                tmp.append(column)
        tmp.sort()
        return tmp[k-1] if tmp and len(tmp)>0 else None
```
### 思路二：
两个tag ： binary search， heap

######先来heap

1. 利用heap,先对第一行所有元素加入heap,每个元素下面同一列的元素必然比他们大
2. 重复K-1次下面的过程
	- 取现在的root
	- 将root下面的元素加入heap
	
可以手写一个例子来看

参考：
<https://lefttree.gitbooks.io/leetcode/content/dataStructure/heap/kthSmallestInMatrix.html>
	
```python
class Solution(object):
    def kthSmallest(self, matrix, k):
        """
        :type matrix: List[List[int]]
        :type k: int
        :rtype: int
        """
        if not matrix:
        	return 0

        heap = []
        row = len(matrix)
        col = len(matrix[0])

        for i in range(col):
        	# heap store its value and location
        	heapq.heappush(heap, (matrix[0][i], 0, i))

        print heap

        for j in range(k-1):
        	cur = heappop(heap)
        	x = cur[1]
        	y = cur[2]
        	if x+1 < row:
        		heapq.heappush(heap, (matrix[x+1][y],x+1,y))

        return heap[0][0]
```

### 思路三： heapq一行

```python
class Solution(object):
    def kthSmallest(self, matrix, k):
        """
        :type matrix: List[List[int]]
        :type k: int
        :rtype: int
        """
        return list(heapq.merge(*matrix))[k-1]
```

### 思路四； binary search
```python
class Solution(object):
    def kthSmallest(self, matrix, k):
        """
        :type matrix: List[List[int]]
        :type k: int
        :rtype: int
        """
        l, r = matrix[0][0], matrix[-1][-1]
        while l <= r:
            mid = l + ((r - l) >> 2)
            if sum(bisect.bisect_right(row, mid) for row in matrix) < k:
                l = mid + 1
            else:
                r = mid - 1
        return l
```
