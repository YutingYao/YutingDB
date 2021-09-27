# easy
class Solution:
    def reverse(self, x):
        """
        :type x: int
        :rtype: int
        """
        num = 0
        flag = 1
        if x > 0:
            flag = 1
        else:
            flag = -1
        #######核心算法#######
        while x != 0:
            num = num * 10 + x % (10 * flag)
            # %	取模 - 返回除法的余数
            x = int(x / 10)
            # 使用 int() 将小数转换为整数，小数取整会采用比较暴力的截断方式
            # 如果想要让其按照人类的思维“四舍五入”，可以采用+ 0.5的方法，示例如下
            # 5.4 “四舍五入”结果为：5，int(5.4+0.5) == 5
            # 5.6 “四舍五入”结果为：6，int(5.6+0.5) == 6
        #######核心算法#######
        if num > 2**31-1 or num < -2**31:
            return 0
        else:
            return num
        
if __name__ == "__main__":
    s = Solution()
    x = 120
    y = -456
    print(s.reverse(x))
    print(s.reverse(y))