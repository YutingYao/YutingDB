# 给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为1000。

# 示例 1：
# 输入: "babad"
# 输出: "bab"
# 注意: "aba"也是一个有效答案。

# 示例 2：
# 输入: "cbbd"
# 输出: "bb"

# medium

class Solution:
    def longestPalindrome(self, s: str) -> str:
        lenStr = len(s)

        if lenStr == 1:
            return s


        def getLen(l,r) -> int:
            while l>=0 and r<lenStr and s[l] == s[r]: # 注意：边界
                l -= 1
                r += 1
            return r - l - 1 # 注意：是 “-1”

        start = 0  
        end = 1 # 注意：在第一次的时候，end = 1
        maxmaxLen = maxLen = 1

        for mid in range(lenStr):
            maxLen = max(getLen(mid,mid),getLen(mid,mid+1))
            
            if maxLen > maxmaxLen:
                maxmaxLen = maxLen
                start = mid - (maxLen-1) // 2 #易错点：-1，最好背一背
                end = start + maxLen
        return s[start:end]




if __name__ == "__main__":
	s = Solution()
	nums = "acbcccc"
	print(s.longestPalindrome(nums))