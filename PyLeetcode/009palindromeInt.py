# SO EASY
class Solution:
    def isPalindrome(self, x):
        """
        :type x: int
        :rtype: bool
        """
        # 处理特殊情况：
        # 1、负数一定不是回文数
        # 2、如果数字的最后一位是 0， 为了让数字是回文数字，则第一位数字也应该是 0，只有 0 满足这种情况
        if x < 0 or (x % 10 == 0 and x is not 0):
            return False
        revertNumber = 0
        while x > revertNumber:
            # x不断变小，而revertNumber不断变大，只比较一半即可
            revertNumber = revertNumber * 10 + x % 10
            x /= 10
        # 当数字长度为奇数时，我们可以通过 revertedNumber/10 去除处于中位的数字。
        # 例如，当输入为 12321 时，在 while 循环的末尾我们可以得到 x = 12，revertedNumber = 123，
        # 由于处于中位的数字不影响回文（它总是与自己相等），所以我们可以简单地将其去除。
        return x == revertNumber or x == revertNumber/10

s = Solution()
x = 2345
y = 121
print(s.isPalindrome(x))
print(s.isPalindrome(y))