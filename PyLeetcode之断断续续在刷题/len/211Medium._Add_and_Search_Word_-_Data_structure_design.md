### 211. Add and Search Word - Data structure design

题目:

<https://leetcode-cn.com/problems/add-and-search-word-data-structure-design/>

难度： Medium

思路：

trie也是树，那么dfs/bfs同样适用。

实际上是照抄208trie的题目再加上dfs



AC代码



```py
class TrieNode(object):
    """docstring for TrieNode"""
    def __init__(self):
        self.childs = dict()
        self.isWord = False
        
class WordDictionary(object):
    def __init__(self):
        """
        initialize your data structure here.
        """
        self.root = TrieNode()
        

    def addWord(self, word):
        """
        Adds a word into the data structure.
        :type word: str
        :rtype: void
        """
        node = self.root
        for letter in word:
            child = node.childs.get(letter)
            if child is None:
                child = TrieNode()
                node.childs[letter] = child
            node = child
        node.isWord = True
        

    def search(self, word):
        """
        Returns if the word is in the data structure. A word could
        contain the dot character '.' to represent any one letter.
        :type word: str
        :rtype: bool
        """
        def dfs(root, word):
            if len(word) == 0:
                return root.isWord
            elif word[0] == '.':
                for node in root.childs:
                    if dfs(root.childs[node], word[1:]):
                        return True
                return False
            else:
                node = root.childs.get(word[0])
                if node is None:
                    return False
                return dfs(node, word[1:])

        return dfs(self.root, word)
```



