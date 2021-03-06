### 3. Longest Substring Without Repeating Characters


题目:
<https://leetcode-cn.com/problems/longest-substring-without-repeating-characters/>


难度:

Medium



思路

粗一看是dp，细一看是greedy

我们先从第一个字符开始，只要碰到已经出现过的字符我们就必须从之前出现该字符的index开始重新往后看。

例如‘xyzxlkjh’，当看到第二个‘x’时我们就应该从y开始重新往后看了。

那么怎么判断字符已经出现过了呢？我们使用一个hashmap，将每一个已经阅读过的字符作为键，而它的值就是它在原字符串中的index，如果我们现在的字符不在hashmap里面我们就把它加进hashmap中去，因此，只要目前的这个字符在该hashmap中的值大于等于了这一轮字符串的首字符，就说明它已经出现过了，我们就将首字符的index加1，即从后一位又重新开始读，然后比较目前的子串长度与之前的最大长度，取大者。

### 程序变量解释

- l(字母L) 代表目前最大子串的长度
- start 是这一轮未重复子串首字母的index
- maps 放置每一个字符的index，如果maps.get(s[i], -1)大于等于start的话，就说明字符重复了，此时就要重置 l(字母L)  和start的值了，



```python
class Solution(object):
    def lengthOfLongestSubstring(self, s):
        """
        :type s: str
        :rtype: int

        """
        l, start, n = 0, 0, len(s)
        maps = {}
        for i in range(n):
            start = max(start, maps.get(s[i], -1)+1)
            l = max(l, i - start+1)
            maps[s[i]] = i
        return l
```

```python
class Solution(object):
    def lengthOfLongestSubstring(self, s):
        """
        :type s: str
        :rtype: int
        """
        maps = {}
        begin, end, counter, d = 0, 0, 0, 0
        while end < len(s):
            if s[end] in maps:
                maps[s[end]] += 1
            else:
                maps[s[end]] = 1
            if maps[s[end]] > 1:
                counter += 1
            end += 1
            while counter > 0:
                if maps[s[begin]] > 1:
                    counter -= 1
                maps[s[begin]] -= 1
                begin += 1
            d = max(d, end - begin)
        return d
```






Author: Keqi Huang

If you like it, please spread your support

![Support](/img/Algorithm/LeetCode/WechatIMG17.jpeg)
