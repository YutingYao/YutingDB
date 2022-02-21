class Solution:
    def calculate(self, s: str) -> int:
        stack = []
        num, op = 0, "+"
        for i, char in enumerate(s):
            if char.isdigit():
                num = 10 * num + int(char)
            if char in "+-*/" or i == len(s)-1:
                if op == "+":
                    stack.append(num)
                elif op == "-":
                    stack.append(-num)
                elif op == "*":
                    stack.append(stack.pop()*num)
                elif op == "/":
                    stack.append(int(stack.pop()/float(num)))
                num, op = 0, char
        return sum(stack)