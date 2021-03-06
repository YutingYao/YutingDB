### 127. Word Ladder

题目:

<https://leetcode-cn.com/problems/word-ladder/>


难度:

Medium

tag可以算BFS，其实就是求shortest path的变体

Reference from [kamyu104](https://github.com/kamyu104/LeetCode/blob/71e0ba555ee49befa01fcd9fc78c3528e2ab63a9/Python/word-ladder.py)

```python
class Solution(object):
    def ladderLength(self, beginWord, endWord, wordList):
        """
        :type beginWord: str
        :type endWord: str
        :type wordList: List[str]
        :rtype: int
        """
        distance, cur, visited, lookup = 0, [beginWord], set([beginWord]), set(wordList)
        
        while cur:
            next_queue = []

            for word in cur:
                if word == endWord:
                    return distance + 1
                for i in xrange(len(word)):
                    for j in 'abcdefghijklmnopqrstuvwxyz':
                        candidate = word[:i] + j + word[i + 1:]
                        if candidate not in visited and candidate in lookup:
                            next_queue.append(candidate)
                            visited.add(candidate)
            distance += 1
            cur = next_queue

        return 0
```
