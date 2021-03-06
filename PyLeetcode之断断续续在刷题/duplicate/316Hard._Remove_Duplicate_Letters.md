### 316. Remove Duplicate Letters

题目:
<https://leetcode-cn.com/problems/remove-duplicate-letters/>


难度:

Hard


思路

这道题让我们移除重复字母，使得每个字符只能出现一次，而且结果要按最优的字母顺序排列，前提是不能打乱其原本的相对位置。
- 先用remaining统计所有出现字母出现过的次数；
- res就是输出结果的字母顺序(list)，最后用join连接起来作为返回值(str)；
- 在stack(set)中的元素意味着其已经出现在最终结果中；

对s中每个字母c，首先看它在stack中有没有出现过：
1. 如果没有那么只要res最后一个字母的ASCII值大于c，且其剩余次数大于0，就将其在res和stack中删去，不停做此操作
2. 如果有了那么说明已经出现在最终结果中，只需要将其统计次数减去1以防后面挪动位置要做判断

做完这些后必须要把c加入到stack和res中去，代表c已经加入到最终结果中的目前应该处于的位置

参考[python的colloections之defaultdict模块](https://docs.python.org/2/library/collections.html)

```python
class Solution(object):
    def removeDuplicateLetters(self, s):
        """
        :type s: str
        :rtype: str
        """
        remaining = collections.defaultdict(int)
        for c in s:
            remaining[c] += 1
        res, stack = [], set()
        for c in s:
            if c not in stack:
                while res and res[-1] > c and remaining[res[-1]] > 0:
                    stack.remove(res.pop())
                res.append(c)
                stack.add(c)
            remaining[c] -= 1
        return ''.join(res)
```
还有别的一些优美的解法，参考[stefan的回答](https://leetcode.com/problems/remove-duplicate-letters/discuss/76787)



```python
递归贪心版本
class Solution(object):
    def removeDuplicateLetters(self, s):
        """
        :type s: str
        :rtype: str
        """
        for c in sorted(set(s)):
            suffix = s[s.index(c):]
            if set(suffix) == set(s):
                return c + self.removeDuplicateLetters(suffix.replace(c, ''))
        return ''
```
```python
class Solution(object):
    def removeDuplicateLetters(self, s):
        """
        :type s: str
        :rtype: str
        """
        result = ''
        while s:
            i = min(map(s.rindex, set(s)))
            c = min(s[:i+1])
            result += c
            s = s[s.index(c):].replace(c, '')
        return result
```

```python
class Solution(object):
    def removeDuplicateLetters(self, s):
        """
        :type s: str
        :rtype: str
        """
        rindex = {c: i for i, c in enumerate(s)}
        result = ''
        for i, c in enumerate(s):
            if c not in result:
                while c < result[-1:] and i < rindex[result[-1]]:
                    result = result[:-1]
                result += c
        return result
```












