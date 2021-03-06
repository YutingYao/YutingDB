#  116. Populating Next Right Pointers in Each Node
**<font color=red>难度: 中等</font>**

## 刷题内容

> 原题连接

* https://leetcode-cn.com/problems/populating-next-right-pointers-in-each-node

> 内容描述

```
Given a binary tree

struct TreeLinkNode {
  TreeLinkNode *left;
  TreeLinkNode *right;
  TreeLinkNode *next;
}
Populate each next pointer to point to its next right node. If there is no next right node, the next pointer should be set to NULL.

Initially, all next pointers are set to NULL.

Note:

You may only use constant extra space.
Recursive approach is fine, implicit stack space does not count as extra space for this problem.
You may assume that it is a perfect binary tree (ie, all leaves are at the same level, and every parent has two children).
Example:

Given the following perfect binary tree,

     1
   /  \
  2    3
 / \  / \
4  5  6  7
After calling your function, the tree should look like:

     1 -> NULL
   /  \
  2 -> 3 -> NULL
 / \  / \
4->5->6->7 -> NULL
```

## 解题方案

> 思路 1

递归

```python
class Solution:
    # @param root, a tree link node
    # @return nothing
    def connect(self, root):
        res = []
        self.recurHelper(root, 0, res)
        for level in res:
            for i in range(len(level)-1):
                level[i].next = level[i+1]
        
                
    def recurHelper(self, root, level, res):
        if not root:
            return 
        if len(res) < level + 1:
            res.append([])
        res[level].append(root)
        self.recurHelper(root.left, level+1, res)
        self.recurHelper(root.right, level+1, res)
```

> 思路 2

迭代，利用curLevel和nextLevel来记录，然后按层append.


```python
class Solution:
    # @param root, a tree link node
    # @return nothing
    def connect(self, root):
        if not root:
            return 
        res, cur_level = [], [root]
        while cur_level:
            next_level = []
            for node in cur_level:
                if node.left:
                    next_level.append(node.left)
                if node.right:
                    next_level.append(node.right)
            res.append(cur_level)
            cur_level = next_level
            
        for cur_level in res:
            for i in range(len(cur_level)-1):
                cur_level[i].next = cur_level[i+1]
```



