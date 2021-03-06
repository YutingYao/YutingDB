# 一个机器人位于一个 m x n 网格的左上角 （起始点在下图中标记为“Start” ）。

# 机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角（在下图中标记为“Finish”）。

# 问总共有多少条不同的路径？

# 说明：m 和 n 的值均不超过 100。

# 示例 1:
# 输入: m = 3, n = 2
# 输出: 3
# 解释:
# 从左上角开始，总共有 3 条路径可以到达右下角。
# 1. 向右 -> 向右 -> 向下
# 2. 向右 -> 向下 -> 向右
# 3. 向下 -> 向右 -> 向右

# 示例 2:
# 输入: m = 7, n = 3
# 输出: 28

# 例如，下图是一个3 x 7 的网格。有多少可能的路径？

# medium, 但我 觉得easy
class Solution(object):
    def uniquePaths(self, m, n):
        """
        :type m: int
        :type n: int
        :rtype: int
        """
        # 阶乘的思想
        def factorial(num):
            res = 1
            for i in range(1, num+1):
                res *= i
            return res
        return factorial(m+n-2)/factorial(n-1)/factorial(m-1)

if __name__ == "__main__":     
    s = Solution()
    print(s.uniquePaths(7,3))