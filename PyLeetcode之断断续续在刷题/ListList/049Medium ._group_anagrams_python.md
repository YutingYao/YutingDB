### 49. Group Anagrams python

题目： 
<https://leetcode-cn.com/problems/anagrams/>


难度 : Medium

python大法好


```python
class Solution(object):
    def groupAnagrams(self, strs):
        """
        :type strs: List[str]
        :rtype: List[List[str]]
        """
        mapx = {}
        for i in strs:
            x = ''.join(sorted(list(i)))
            if x in mapx:
                mapx[x].append(i)
            else:
                mapx[x] = [i]
        return mapx.values()

```
