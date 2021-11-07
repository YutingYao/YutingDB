# 给定一个字符串 (s) 和一个字符模式 (p)。实现支持 '.' 和 '*' 的正则表达式匹配。

# '.' 匹配任意单个字符。
# '*' 匹配零个或多个前面的元素。
# 匹配应该覆盖整个字符串 (s) ，而不是部分字符串。

# 说明:
#  - s 可能为空，且只包含从 a-z 的小写字母。
#  - p 可能为空，且只包含从 a-z 的小写字母，以及字符 . 和 *。

# 示例 1:
# 输入:
# s = "aa"
# p = "a"
# 输出: false
# 解释: "a" 无法匹配 "aa" 整个字符串。

# 示例 2:
# 输入:
# s = "aa"
# p = "a*"
# 输出: true
# 解释: '*' 代表可匹配零个或多个前面的元素, 即可以匹配 'a' 。因此, 重复 'a' 一次, 字符串可变为 "aa"。

# 示例 3:
# 输入:
# s = "ab"
# p = ".*"
# 输出: true
# 解释: ".*" 表示可匹配零个或多个('*')任意字符('.')。

# 示例 4:
# 输入:
# s = "aab"
# p = "c*a*b"
# 输出: true
# 解释: 'c' 可以不被重复, 'a' 可以被重复一次。因此可以匹配字符串 "aab"。

# 示例 5:
# 输入:
# s = "mississippi"
# p = "mis*is*p*."
# 输出: false

# hard
# runoo+b，可以匹配 runoob、runooob、runoooooob 等，
# + 号代表前面的字符必须至少出现一次（1次或多次）。
# runoo*b，可以匹配 runob、runoob、runoooooob 等，
# 代表前面的字符可以不出现，也可以出现一次或者多次（0次、或1次、或多次）。
# colou?r 可以匹配 color 或者 colour，? 
# 问号代表前面的字符最多只可以出现一次（0次、或1次）。
class Solution(object):
    def isMatch(self, string, pattern):
        """
        :type s: str
        :type p: str
        :rtype: bool
        """
        def helper(string, str_len, pattern, pat_len):
            
            # 当收敛至最后一位时，则true
            if pat_len == 0:
                return str_len == 0
            # s = "aa"
            # p = "a"
            # 轮回结束
        
        ##### 关键在于：类似消消乐    
        ##### 关键在于：从末位开始    
        ##### 关键在于：有匹配的string会被消掉    
        ##### 关键在于：没有有匹配的string，但有*，可以直接消除2两位。   
        #######核心算法#######
            if str_len == 0:
                if pattern[pat_len-1] != '*':
                    return False
                return helper(string, str_len, pattern, pat_len-2) # pattern可以匹配0个字符，如“a*”与none
        #######核心算法#######
                      

                    
        #######核心算法#######
            if pattern[pat_len-1] == '*':
                #######核心算法#######
                if pattern[pat_len-2] == '.' or pattern[pat_len-2] == string[str_len-1]:
                    if helper(string, str_len-1, pattern, pat_len): # 如果出现ccc这种重复字符串，就把这些消灭干净
                        return True
                    # s = "aa"
                    # p = "a*"
                    # 从这里路过
                    # s = "ab"
                    # p = ".*"
                    # 从这里路过
                #######核心算法#######
                    
                return helper(string, str_len, pattern, pat_len-2) # 如果最后无法匹配，那么就消掉
        #######核心算法#######
        
            # 从最后一位开始逐渐退一位
            if pattern[pat_len-1] == '.' or pattern[pat_len-1] == string[str_len-1]:
                return helper(string, str_len-1, pattern, pat_len-1)

            
            return False

        return helper(string, len(string), pattern, len(pattern))

if __name__ == "__main__":    
    s = 'abc'
    p = 'a*abc'
    ss = Solution()
    print(ss.isMatch(s, p))