###227. Basic Calculator II



题目:
<https://leetcode-cn.com/problems/basic-calculator-ii/>


难度:
Medium

思路：

瞄了一眼，基本上infix(中缀表达式)都是表达成postfix(后缀表达式)再来求值的。
比如 A + B * C 写成 A B C * +


| Infix Expression | Prefix Expression | Postfix Expression |
| ---------------- | ----------------- | ------------------ |
| A + B            | + A B             | A B +              |
| A + B * C        | + A * B C         | A B C * +          |


infix 中缀转postfix 后缀还有专门的算法：<https://en.wikipedia.org/wiki/Shunting-yard_algorithm>



1. Create an empty stack called opstack for keeping operators. Create an empty list for output.

2. Convert the input infix string to a list by using the string method split.

3. Scan the token list from left to right.

4. - If the token is an operand, append it to the end of the output list.
   - If the token is a left parenthesis, push it on the opstack.
   - If the token is a right parenthesis, pop the opstack until the corresponding left parenthesis is removed. Append each operator to the end of the output list.
   - If the token is an operator, *, /, +, or -, push it on the opstack. However, first remove any operators already on the opstack that have higher or equal precedence and append them to the output list.

5. When the input expression has been completely processed, check the opstack. Any operators still on the stack can be removed and appended to the end of the output list.



可以看到中缀转后缀一个重要的点是： 当我们把operator +-*/ 放到opstack上时候，我们需要考虑/看是否有之前的operator有更高或者相等的precedence，这个时候我们需要优先（计算）把它放到output list.




参考

<http://interactivepython.org/runestone/static/pythonds/BasicDS/InfixPrefixandPostfixExpressions.html>



AC代码

```python
class Solution(object):
    def calculate(self, s):
        """
        :type s: str
        :rtype: int
        """
        def precedence(op):
            if op == '*' or op == '/':
                return 2
            else:
                return 1

        def cal(op, op1, op2):
            if op == '*':
                return op1 * op2
            elif op == '/':
                return op1 / float(op2)
            elif op == '+':
                return op1 + op2 
            else:
                return op1 - op2


        opstack = []
        operands = []

        # remove empty space and put operands and 
        idx = 0
        for i in range(idx, len(s)):
            if s[i] in '+-*/':
                operands.append(s[idx:i])
                while len(opstack) > 0 and precedence(s[i]) <= precedence(opstack[-1]) and len(operands) >= 2:
                    op = opstack.pop()
                    op2 = int(operands.pop())
                    op1 = int(operands.pop())
                    res = cal(op, op1, op2)
                    operands.append(res)
                opstack.append(s[i])
                idx = i + 1
        operands.append(s[idx:])

        while opstack:
             op = opstack.pop()
             op2 = int(operands.pop())
             op1 = int(operands.pop())
             res = cal(op, op1, op2)
             operands.append(res)

        return int(operands[0])

```

