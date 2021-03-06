###150. Evaluate Reverse Polish Notation



题目:
<https://leetcode-cn.com/problems/evaluate-reverse-polish-notation/>


难度:
Medium



AC代码

```py
class Solution(object):
    def evalRPN(self, tokens):
        """
        :type tokens: List[str]
        :rtype: int
        """
        def cal(op, op1, op2):
            if op == '*':
                return op1 * op2
            elif op == '/':
                return op1 / float(op2)
            elif op == '+':
                return op1 + op2 
            else:
                return op1 - op2

        operandStack = []

        for token in tokens:
            if token in '+-*/':
                op2 = operandStack.pop()
                op1 = operandStack.pop()
                res = cal(token, op1, op2)
                operandStack.append(int(res))
            else:
                operandStack.append(int(token)) 

        return operandStack.pop()
```


实际上这里有一个很奇（sha）怪（bi）的地方，看到了么，除法➗处，如果我不这么做，就是错的，这是python 2 和 python 3 的除法不一致导致的，所以最终我这样做了才能得到正确答案。

思路：

已经给了我们wikipedia的链接了<https://en.wikipedia.org/wiki/Reverse_Polish_notation>

- While there are input tokens left
	- Read the next token from input.
	- If the token is a value
		- Push it onto the stack.
	-Otherwise, the token is an operator (operator here includes both operators and functions).
		- It is already known that the operator takes n arguments.
		- If there are fewer than n values on the stack
			- (Error) The user has not input sufficient values in the expression.
		- Else, Pop the top n values from the stack.
		- Evaluate the operator, with the values as arguments.
		- Push the returned results, if any, back onto the stack.
- If there is only one value in the stack
	- That value is the result of the calculation.
- Otherwise, there are more values in the stack
	- (Error) The user input has too many values.



再参考这里

<http://interactivepython.org/runestone/static/pythonds/BasicDS/InfixPrefixandPostfixExpressions.html>


1. Create an empty stack called operandStack.
2. Convert the string to a list by using the string method split.
3. Scan the token list from left to right.
	- If the token is an operand, convert it from a string to an integer and push the value onto the operandStack.
	- If the token is an operator, *, /, +, or -, it will need two operands. Pop the operandStack twice. The first pop is the second operand and the second pop is the first operand. Perform the arithmetic operation. Push the result back on the operandStack.
4. When the input expression has been completely processed, the result is on the stack. Pop the operandStack and return the value.

