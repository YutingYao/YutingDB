# 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串，判断字符串是否有效。

# 有效字符串需满足：

# 1、左括号必须用相同类型的右括号闭合。
# 2、左括号必须以正确的顺序闭合。

# 注意空字符串可被认为是有效字符串。

# 示例1：
# 输入: "()"
# 输出: true

# 示例2：
# 输入: "()[]{}"
# 输出: true

# 示例3：
# 输入: "(]"
# 输出: false

# 示例4：
# 输入: "([)]"
# 输出: false

# 示例5：
# 输入: "{[]}"
# 输出: true


# 难度：Easy
# 先创建一个堆栈
# 将左括号放进堆栈
# 如堆栈非空，且newBrackets能与堆栈匹配，则true，并清空
class Solution:
    def isValid(self, string):
        """
        :type s: str
        :rtype: bool
        """
        LEFT = {'(', '[', '{'}  # 左括号
        RIGHT = {')', ']', '}'}  # 右括号
        stack = []  # 创建一个栈
        for brackets in string:  # 迭代传过来的所有字符串，也可以 for i in range(len(string)):
            if brackets in LEFT:  # 如果当前字符在左括号内
                stack.append(brackets)  # 把当前左括号入栈
            elif brackets in RIGHT:  # 如果是右括号
                if not stack or not 1 <= ord(brackets) - ord(stack[-1]) <= 2: 
                    return False  # 返回False
                    # ord 以一个字符（长度为1的字符串）作为参数，返回对应的 ASCII 数值，或者 Unicode 数值
                    # 如果当前栈为空，()]
                    # 如果右括号减去左括号的值不是小于等于2大于等于1
                stack.pop()  # 删除左括号，函数用于移除列表中的一个元素（默认最后一个元素）
        return not stack  # 如果栈内没有值则返回True，否则返回False。False的情况为有一个左括号，但匹配不上

if __name__ == "__main__":   
	s = Solution()
	print(s.isValid("([[])[]{}"))
	print(s.isValid("([])[]{}"))