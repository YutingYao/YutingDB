### 381. Insert Delete GetRandom O(1) - Duplicates allowed



题目： 
<https://leetcode-cn.com/problems/insert-delete-getrandom-o1-duplicates-allowed/>



难度 : Hard



一开始的想法是在380上面做简单的修改，比如用一个list来存每个数对应的location，但是这样remove会退化为O(N)，然后看到：

- 用 set 这个数据结构就可以贴近O(1)
- 学了一个新的东西`defaultdict`， 相当于 D.get('key',defaultvalue)



这个defaultdict的好处就是添加的时候默认的值就是set，但是并不默认这个就存在



AC代码

```py
class RandomizedCollection(object):

    def __init__(self):
        """
        Initialize your data structure here.
        """
        import collections
        self.hashtable = collections.defaultdict(set)
        self.array = []

    def insert(self, val):
        """
        Inserts a value to the collection. Returns true if the collection did not already contain the specified element.
        :type val: int
        :rtype: bool
        """
        valin = val not in self.hashtable
        self.hashtable[val].add(len(self.array))
        self.array.append(val)
        return valin 

    def remove(self, val):
        """
        Removes a value from the collection. Returns true if the collection contained the specified element.
        :type val: int
        :rtype: bool
        """
        if val not in self.hashtable:
            return False
        else:
            if self.array[-1] == val:
                removeIdx = len(self.array) - 1
                self.hashtable[val].remove(removeIdx)
            else:
                # set pop remove arbitrary element
                removeIdx = self.hashtable[val].pop()
                self.array[removeIdx] = self.array[-1]
                self.hashtable[self.array[-1]].remove(len(self.array) - 1)
                self.hashtable[self.array[-1]].add(removeIdx)
            if len(self.hashtable[val]) == 0:
                del self.hashtable[val]
            del self.array[-1]
            return True
        

    def getRandom(self):
        """
        Get a random element from the collection.
        :rtype: int
        """
        import random
        return random.choice(self.array)
        
```

`