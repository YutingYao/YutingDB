### 439. Ternary Expression Parser





题目:
<https://leetcode-cn.com/problems/ternary-expression-parser/>



难度:
Medium 

思路：

其实这个和算术运算蛮像，但是不同于运算，有operator precedence差别，这个是三目运算，并且需要检查是否符合运算规则。



运用stack 然后每次查看是否形成运算式再来做处理

AC代码：

```python
class Solution(object):
    def parseTernary(self, expression):
        """
        :type expression: str
        :rtype: str
        """
        n = len(expression)

        stack = []

        for i in range(n-1, -1, -1):
            char = expression[i]
            stack.append(char)

            if len(stack) >= 5:
                op0 = stack.pop()
                op1 = stack.pop()
                op2 = stack.pop()
                op3 = stack.pop()
                op4 = stack.pop()

                if op1 == '?' and op3 == ':':
                    res = op2 if op0 == 'T' else op4
                    stack.append(res)
                else:
                    stack.append(op4)
                    stack.append(op3)
                    stack.append(op2)
                    stack.append(op1)
                    stack.append(op0)
        return stack[0]
```

