class Solution(object):
    def maximalRectangle(self, matrix):
        row = len(matrix)
        col = len(matrix[0])
        res = 0
        height  = [0]*(col + 2)
        for i in range(row):
            stack = [0]
            for j in range(col):
                if matrix[i][j] == '1':
                    height[j + 1] += 1
                if matrix[i][j] == '0':
                    height[j + 1] = 0
            for k in range(1, len(height)):
                while(height[k] < height[stack[-1]]):
                    h = height[stack.pop()]
                    w = k - stack[-1] - 1
                    res = max(res, h * w)
                stack.append(k)
        return  res