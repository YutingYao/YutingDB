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