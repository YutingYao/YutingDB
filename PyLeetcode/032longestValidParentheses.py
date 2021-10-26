# 给定一个只包含 '(' 和 ')' 的字符串，找出最长的包含有效括号的子串的长度。

# 示例 1:
# 输入: "(()"
# 输出: 2
# 解释: 最长有效括号子串为 "()"

# 示例 2:
# 输入: ")()())"
# 输出: 4
# 解释: 最长有效括号子串为 "()()"

# very hard
# dynamic programming 
# 动态规划，就是创建一个字典的子序列
class Solution(object):
    def longestValidParentheses(self, string):
        """
        :type s: str
        :rtype: int
        """
        if len(string) == 0:
            return 0
        dp = [0 for i in range(len(string))] # 输出 [0, 0, 0, 0, 0, 0]

        for index in range(len(string)-1):
                
	    
            if string[index+1] == ')':
                # 关键在于知道left的位置
                left = index - dp[index] 
                print("left bracket:",left)
                print("index-dp[index]:",index,"-",dp[index])
                print("?? right bracket = true")
                if left >= 0 and string[left] == '(':
                    dp[index+1] = dp[index] + 2
                    print("?? left bracket = true")
                    print(dp)
                    print("left:",left)
                    if left > 0: # 这个是判断 left 前面是否能与后面继续连起来
                        dp[index+1] += dp[left-1]
                        print("base: dp[index+1]=",dp[index+1])
                        print("add: dp[left-1]=",dp[left-1])
                        print(dp)
            print("when index =",index)
            print("-"*50)
        return max(dp)

if __name__ == "__main__":   
    s = Solution()
    print(s.longestValidParentheses("()()(()())"))
    # print(s.longestValidParentheses("()()(()"))

