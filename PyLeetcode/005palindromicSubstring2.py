# 给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为1000。

# 示例 1：
# 输入: "babad"
# 输出: "bab"
# 注意: "aba"也是一个有效答案。

# 示例 2：
# 输入: "cbbd"
# 输出: "bb"

# medium

class Solution(object):
    def longestPalindrome(self, s):
        """
        :type s: str
        :rtype: str
        """
        stringlenth = len(s)

        maxlength,leftindex,rightindex = 0,0,0

        for index in range(stringlenth): # range(4)生成0,1,2,3
            # odd case
            buffermaxodd = min(index+1,stringlenth-index)
            for buffer in range(buffermaxodd):
                if s[index-buffer] != s[index+buffer]:
                    break
                if 2*buffer + 1 > maxlength :
                    maxlength = 2 * buffer + 1
                    leftindex = index-buffer
                    rightindex = index+buffer

            # even case
            buffermaxeven = min(index+1,stringlenth-index-1)
            if index+1 < stringlenth and s[index] == s[index+1]:
                for buffer in range(buffermaxeven):
                    if s[index-buffer] != s[index+buffer+1]:
                        break
                    if 2 * buffer + 2 > maxlength :
                        maxlength = 2*buffer +2
                        leftindex = index-buffer
                        rightindex = index+buffer+1


        return s[leftindex:rightindex+1]

if __name__ == "__main__":
	s = Solution()
	nums = "acbcccc"
	print(s.longestPalindrome(nums))