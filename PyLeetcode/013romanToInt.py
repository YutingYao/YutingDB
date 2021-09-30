# 难度：Easy
class Solution(object):
    def romanToInt(self, string):
        """
        :type s: str
        :rtype: int
        """
        lookup = {
            'M': 1000,
            'D': 500,
            'C': 100,
            'L': 50,
            'X': 10,
            'V': 5,
            'I': 1
        }  # 查找['I'] = 1
        res = 0
        for i in range(len(string)):
            # I 可以放在 V (5) 和 X (10) 的左边，来表示 4 和 9。
            # X 可以放在 L (50) 和 C (100) 的左边，来表示 40 和 90。 
            # C 可以放在 D (500) 和 M (1000) 的左边，来表示 400 和 900。
            if i > 0 and lookup[string[i]] > lookup[string[i-1]]: #字符串可以直接索引
                # 针对这种特别长的字符串：
                # 输入: "MCMXCIV"
                # 输出: 1994
                # 解释: M = 1000, CM = 900, XC = 90, IV = 4.
                res = res + lookup[string[i]] - 2 * lookup[string[i-1]] #多减一位，后面会补上的
            else:
                res += lookup[string[i]] # 简单相加即可
        return res

if __name__ == "__main__":    
	s = Solution()
	string = "MCMXCIV"
	print(s.romanToInt(string))