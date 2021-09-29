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