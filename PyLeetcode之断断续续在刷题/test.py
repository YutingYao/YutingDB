MASK1 = 0x100000000  # 2^32
minInt1000 = 0x80000000  # 2^31
maxInt0111 = 0X7FFFFFFF  # 2^31-1

class Solution:
    def getSum(self, a: int, b: int) -> int:
        a %= MASK1
        b %= MASK1
        while b != 0:
            carry = ((a & b) << 1) % MASK1
            a = (a ^ b) % MASK1
            b = carry
        if a & minInt1000:  # 负数，也就是第31位有东西
            return ~((a ^ minInt1000) ^ maxInt0111)
        else:  # 正数，也就是第31位没有东西
            return a