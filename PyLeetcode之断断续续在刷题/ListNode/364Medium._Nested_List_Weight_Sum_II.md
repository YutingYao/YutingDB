### 364. Nested List Weight Sum II



题目:
<https://leetcode-cn.com/problems/nested-list-weight-sum-ii/>

难度:
Medium 

思路：



跟 Nested List Weight Sum I 的区别是这个是从不是数depth，是数层的高度：



比较naive的AC代码：

```python
class Solution(object):
    def depthSumInverse(self, nestedList):
        """
        :type nestedList: List[NestedInteger]
        :rtype: int
        """
        def level(nestedList,height):
            self.level = max(height, self.level)
            for item in nestedList:
                if not item.isInteger():
                    level(item.getList(), height + 1)

        def dfs(nestedList, height):
        	for item in nestedList:
        		if item.isInteger():
        			self.res += item.getInteger() * height
        		else:
        			dfs(item.getList(),height - 1)
        
        self.level = 1
        self.res = 0
        level(nestedList,1)
        dfs(nestedList, self.level)
        return self.res
```

