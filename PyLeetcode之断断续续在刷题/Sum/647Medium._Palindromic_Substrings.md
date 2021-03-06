### 647. Palindromic Substrings

题目:
<https://leetcode-cn.com/problems/Palindromic-Substrings/>


难度:

Medium


思路

这道题要求给定一个字符串中的所有回文子串的个数，所以我想到了Manacher算法，
[Manacher算法](https://www.felix021.com/blog/read.php?2040) 

Manacher算法增加两个辅助变量id和mx，其中id表示最大回文子串中心的位置，mx则为id+P[id]，也就是最大回文子串的边界。得到一个很重要的结论：

- 如果mx > i，那么P[i] >= Min(P[2 * id - i], mx - i) . 为什么这样说呢，下面解释

下面，令j = 2*id - i，也就是说j是i关于id的对称点。

- 当 mx - i > P[j] 的时候，以S[j]为中心的回文子串包含在以S[id]为中心的回文子串中，由于i和j对称，以S[i]为中心的回文子串必然包含在以S[id]为中心的回文子串中，所以必有P[i] = P[j]；
![](/img/Algorithm/LeetCode/manacher1.png)

- 当 P[j] >= mx - i 的时候，以S[j]为中心的回文子串不一定完全包含于以S[id]为中心的回文子串中，但是基于对称性可知，下图中两个绿框所包围的部分是相同的，也就是说以S[i]为中心的回文子串，其向右至少会扩张到mx的位置，也就是说 P[i] >= mx - i。至于mx之后的部分是否对称，再具体匹配。
![](/img/Algorithm/LeetCode/manacher2.png)
所以P[i] >= Min(P[2 * id - i], mx - i)，因为以j为中心的绘回文子串的左边界可能会比mx关于id的对称点要大，此时只能证明P[i]=P[2 * id - i]
- 此外，对于 mx <= i 的情况，因为无法对 P[i]做更多的假设，只能让P[i] = 1，然后再去匹配。
此题还可以借鉴我leetcode第5题的解析，
[thining-in-lc-5](https://github.com/Lisanaaa/thinking_in_lc/blob/master/005._longest_palindromic_substring.md)

这道题的基本思想是将以每一个字符为中心的回文子串个数相加，还是用一个小例子来解释
![](/img/Algorithm/LeetCode/manacher3.jpg)
其实，以‘#’为中心的回文子串就代表这个子串的长度是偶数，类似于'abba'这种
但是其实这个字符本身也是一个回文子串，所以叠加的形式是count += (P[i]+1)/2，为什么呢，以下是解释：
- 对于每一个以字符‘#’为中心的回文子串，其P值绝对是偶数，所以```(P[i]+1)/2 = P[i]/2```，并不影响
- 对于每一个以非字符‘#’为中心的回文子串，其P值绝对是奇数，这就保证了单个字母的回文子串(```例如'a'也算一个回文子串```)也被加起来了，因为```(P[i]+1)/2 = P[i]/2+1```


```python
class Solution(object):
    def countSubstrings(self, s):
        """
        :type s: str
        :rtype: str
        """
        def preProcess(s):
            if not s:
                return ['^', '$']
            T = ['^']
            for c in s:
                T += ['#', c]
            T += ['#', '$']
            return T
        T = preProcess(s)
        P = [0] * len(T)
        id, mx, count = 0, 0, 0
        for i in range(1,len(T) - 1):
            j = 2*id - i
            if mx > i:
                P[i] = min(mx - i, P[j])
            else:
                P[i] = 0
            while T[i+P[i]+1] == T[i-P[i]-1]:
                P[i] += 1
            if (i + P[i]) > mx:
                id, mx = i, i + P[i]
        for i in range(len(P)):
            count += (P[i]+1)/2
        return count
```
python无敌啊！！！有没有天理啊，手动滑稽😏😏😏😏！一行解法：
```python
class Solution(object):
    def countSubstrings(self, s):
        """
        :type s: str
        :rtype: int
        """
        return sum(len(os.path.commonprefix((s[:i][::-1], s[i:]))) 
                   + len(os.path.commonprefix((s[:i][::-1], s[i + 1:]))) + 1 
                        for i in range(len(s)))
```
解释下为啥要加两次，因为回文串有以下两种形式：
- ‘abcba’
- 'abba'

那为啥要加那个1呢，上面解释过了，单个字符也算是一个回文子串呀，嘻嘻😁
