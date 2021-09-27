# easy
class Solution:
    def reverse(self, x):
        """
        :type x: int
        :rtype: int
        """
        # 如果是负数，我们需要注意转化为绝对值
        flag = 1
        x_1 = 0
        #######核心算法#######
        if x < 0:
            flag = -1
            x = int(str(abs(x))[::-1]) 
            # str()转变为字符串
            # [::-1]转变为反转字符串
            # int()转变为整数
            x_1 = x * flag
        else:
            flag = 1
            x = int(str(x)[::-1])
            x_1 = x * flag
        #######核心算法#######
        if x_1 > 2**31-1 or x_1 < -2**31:
            return 0
        else:
            return x_1
        
if __name__ == "__main__":
    s = Solution()
    x = 120
    y = -456
    print(s.reverse(x))
    print(s.reverse(y))