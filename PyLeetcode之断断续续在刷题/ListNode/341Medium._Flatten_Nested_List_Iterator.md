### 341. Flatten Nested List Iterator



题目:
<https://leetcode-cn.com/problems/flatten-nested-list-iterator/>



难度:
Medium 



```python
class NestedIterator(object):

    def __init__(self, nestedList):
        """
        Initialize your data structure here.
        :type nestedList: List[NestedInteger]
        """
        def dfs(nestedList):
            for item in nestedList:
                if item.isInteger():
                    self.stack.append(item.getInteger())
                else:
                    dfs(item.getList())
        self.stack = []
        dfs(nestedList)
        

    def next(self):
        """
        :rtype: int
        """
        if self.hasNext():
            return self.stack.pop(0)


    def hasNext(self):
        """
        :rtype: bool
        """
        return self.stack != []
```

