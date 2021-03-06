### 48. Rotate Image


题目:
<https://leetcode-cn.com/problems/rotate-image/>


难度:

Medium




思路一：


先将矩阵上下翻转，然后将矩阵中心对称翻转，即可实现顺时针90度旋转。


- 上下翻转规律 [i][:] --> [n-1-i][:]
- 对角线变换的规律是 [i][j] --> [j][i]


例如：
```
1 1 1    3 3 3    3 2 1
2 2 2 -> 2 2 2 -> 3 2 1
3 3 3    1 1 1    3 2 1
```


```python
class Solution(object):
    def rotate(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: void Do not return anything, modify matrix in-place instead.
        """
        n = len(matrix)
        # 上下翻转
        for i in range(n/2):
            matrix[i], matrix[n-1-i] = matrix[n-1-i], matrix[i]
        # 主对角线翻转
        for i in range(n):
            for j in range(i+1,n):
                matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
```


思路二：

参考这里

<http://www.lifeincode.net/programming/leetcode-rotate-image-java/>

找规律，一次完成四个数的该有的变换

```

1 	2 	3 	4 	5 					

6 	7 	8 	9 	10 	

11 	12 	13 	14 	15 	

16 	17 	18 	19 	20 	

21 	22 	23 	24 	25 

```

在思路一的解法下观察得出，每个元素的变换是 [x][y] -> [n-1-x][y] -> [y][n-1-x] -> [n-1-y][x]


```python
class Solution(object):
    def rotate(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: void Do not return anything, modify matrix in-place instead.
        """
        n = len(matrix)
        for i in range(n/2):
            for j in range(n-n/2):
                matrix[i][j], matrix[~j][i], matrix[~i][~j], matrix[j][~i] = \
                         matrix[~j][i], matrix[~i][~j], matrix[j][~i], matrix[i][j]
```
这里的```[~i]``` 意思就是 ```[n-1-i]```

思路三：

直接用zip函数，一行, 😂

```python
class Solution:
    def rotate(self, A):
        A[:] = zip(*A[::-1])
        # A[:] = map(list, zip(*A[::-1]))
```





