###394. Decode String


题目:

<https://leetcode-cn.com/problems/decode-string/>


难度:

Medium


思路:

感觉像用栈做运算。

s = "3[a2[c]]"

⬇️

s = 3 *( a + 2 * ( c ) )


遇到非右括号全部入栈，碰到右括号出栈直到左括号，这个就算运算符2 → op2
然后检查，直到stack空掉或者碰到下一个非数字，这个就算运算符1 → op1

算出op1 和 op2 之后把这个res继续入栈。然后接着处理


代码不是很优美




```python
class Solution(object):
    def decodeString(self, s):
        """
        :type s: str
        :rtype: str
        """

        s = list(s)
        stack = []

        while s:
        	char = s.pop(0)
        	if char != ']':
        		stack.append(char)
        	else:
        		op1, op2 = '',''
        		popChar = stack.pop()
        		while popChar != '[':
        			op2 = popChar + op2
        			popChar = stack.pop()

        		while stack and stack[-1] in ['0','1','2','3','4','5','6','7','8','9']:
        			popChar = stack.pop()
        			op1 = popChar + op1

        		res = int(op1) * op2

        		for char in res:
        			stack.append(char)

        return ''.join(stack)
```


