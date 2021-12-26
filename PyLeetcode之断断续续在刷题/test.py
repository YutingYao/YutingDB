import collections
class Solution:
    def findLadders(self, beginWord: str, endWord: str, wordList):
        if endWord not in wordList:
            return []
        lookup = collections.defaultdict(list)
        L = len(beginWord)
        for word in wordList:
            for i in range(L):
                lookup[word[:i] + '*' + word[i+1:]].append(word)
        
        res = []
        que = [(beginWord, 1, [[beginWord]])] # 终点，长度，path
        visited = {beginWord:[[beginWord]]}
        mindepth = len(wordList) + 1  # 剪枝
        print(visited)
        while que:
            cur, depth, paths = que.pop(0)
            if depth > mindepth: continue  # 剪枝           
            for i in range(L):
                dummyword = cur[:i] + '*' + cur[i+1:]
                for word in lookup[dummyword]:
                    if word == endWord:
                        for path in paths:
                            mindepth = depth  # 剪枝
                            res.append(path + [endWord])
                    elif word not in visited:
                        new_paths = [p+[word] for p in paths]
                        visited[cur] = new_paths
                        que.append((word, depth+1, new_paths))

        return res
    
if __name__ == "__main__":
  s = Solution()
  res = s.findLadders("hit","cog",["hot","dot","dog","lot","log","cog"])
  print('res:',res)