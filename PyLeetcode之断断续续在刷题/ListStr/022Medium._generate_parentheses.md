### 22. Generate Parentheses

题目:
<https://leetcode-cn.com/problems/generate-parentheses/>


难度:

Medium





```python
class Solution(object):
    def generateParenthesis(self,n):
        """
        :type n: int
        :rtype: List[str]
        """
        self.res = []
        self.singleStr('', 0, 0, n)
        return self.res
        
    def singleStr(self, s, left, right, n):
        if left == n and right == n:
            self.res.append(s)
        if left < n:
            self.singleStr(s + '(',left + 1, right,n)
        if right < left:
            self.singleStr(s + ')',left, right + 1, n)
                
```


非常牛逼的讲解，需要这样的人来给我们讲算法

####以Generate Parentheses为例，backtrack的题到底该怎么去思考？


所谓Backtracking都是这样的思路：在当前局面下，你有若干种选择。那么尝试每一种选择。如果已经发现某种选择肯定不行（因为违反了某些限定条件），就返回；如果某种选择试到最后发现是正确解，就将其加入解集

所以你思考递归题时，只要明确三点就行：选择 (Options)，限制 (Restraints)，结束条件 (Termination)。即“ORT原则”（这个是我自己编的）




对于这道题，在任何时刻，你都有两种选择：
1. 加左括号。
2. 加右括号。

同时有以下限制：
1. 如果左括号已经用完了，则不能再加左括号了。
2. 如果已经出现的右括号和左括号一样多，则不能再加右括号了。因为那样的话新加入的右括号一定无法匹配。

结束条件是：
左右括号都已经用完。

结束后的正确性：
左右括号用完以后，一定是正确解。因为1. 左右括号一样多，2. 每个右括号都一定有与之配对的左括号。因此一旦结束就可以加入解集（有时也可能出现结束以后不一定是正确解的情况，这时要多一步判断）。

递归函数传入参数：
限制和结束条件中有“用完”和“一样多”字样，因此你需要知道左右括号的数目。
当然你还需要知道当前局面sublist和解集res。

因此，把上面的思路拼起来就是代码：

	if (左右括号都已用完) {
	  加入解集，返回
	}
	//否则开始试各种选择
	if (还有左括号可以用) {
	  加一个左括号，继续递归
	}
	if (右括号小于左括号) {
	  加一个右括号，继续递归
	}
	
	
	
你帖的那段代码逻辑中加了一条限制：“3. 是否还有右括号剩余。如有才加右括号”。这是合理的。不过对于这道题，如果满足限制1、2时，3一定自动满足，所以可以不判断3。

这题其实是最好的backtracking初学练习之一，因为ORT三者都非常简单明显。你不妨按上述思路再梳理一遍，还有问题的话再说。



以上文字来自 1point3arces的牛人解答
