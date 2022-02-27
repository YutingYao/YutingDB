class Solution:
    def calculate(self, s: str) -> int:
        stack = [1]
        sign = 1
        i = 0
        res = 0
        while i < len(s):
            if s[i].isdigit():
                num = 0
                while i < len(s) and s[i].isdigit():
                    num = 10 * num + int(s[i])
                    i += 1
                res += sign * num
            else:
                if s[i] == '+':   sign = stack[-1]
                elif s[i] == '-': sign = -stack[-1]
                # -(1-(4+5+2)-3)+(6+8)
                # stack[-1] 是因为负负得正
                elif s[i] == '(': stack.append(sign)
                elif s[i] == ')': stack.pop()
                i += 1
        return res