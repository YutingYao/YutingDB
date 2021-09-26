class Solution(object):
    def longestPalindrome(self, s):
        """
        :type s: str
        :rtype: str
        """
        # 当不存在
        if not s:
            return
	# 有了return就会停止计算
	
        # 当长度为1
        stringlenth = len(s)
        print("stringlenth:",stringlenth)
        if stringlenth == 1:
            return s
	# 有了return就会停止计算
 
	# 当长度大于1
        leftindex = 0
        rightindex = 0
        maxlength = 0
        currentlength = 0
        ifbound = True

        for index in range(0, stringlenth):
            # 奇数情况
            for buffer in range(0, min(stringlenth - index, index + 1)):
                print("---single---","index:",index,"buffer",buffer)
                if (s[index - buffer] != s[index + buffer]):
                    ifbound = False
                    break
                else:
                    currentlength = 2 * buffer + 1
            if (currentlength > maxlength):
                print("bound?:",ifbound)
                leftindex = index - buffer + 1 - ifbound
                rightindex = index + buffer + ifbound
                print("---single---","index:",index,"leftindex:",leftindex,"rightindex:",rightindex,s[leftindex:rightindex])
                maxlength = currentlength
            ifbound = True
            # 偶数情况
            for buffer in range(0, min(stringlenth - index - 1, index + 1)):
                print("---double---","index:",index,"buffer double:",buffer)
                if (s[index - buffer] != s[index + buffer + 1]):
                    ifbound = False
                    break
                else:
                    currentlength = 2 * buffer + 2
            if (currentlength > maxlength):
                print("bound?:",ifbound)
                leftindex = index - buffer + 1 - ifbound
                rightindex = index + buffer + 1 + ifbound
                maxlength = currentlength
                print("---double---","index:",index,"leftindex:",leftindex,"rightindex:",rightindex,s[leftindex:rightindex],"bound?:",ifbound)
            ifbound = True
        # 将最终的回文串返回
        return s[leftindex:rightindex]
	# index: 5 leftindex: 5 rightindex: 7 aa bound?: True
	# 从第5个字符串，到第7个字符串，但不包括第7个
    
s = Solution()
nums = "csdcxaa"
print(s.longestPalindrome(nums))
