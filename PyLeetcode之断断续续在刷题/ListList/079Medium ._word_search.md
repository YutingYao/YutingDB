###79. Word Search



题目:
<https://leetcode-cn.com/problems/word-search/>


难度:
Medium


思路：

其实这个题和number of islands类似，是backtracking基本功的考查,但是基本功非常有待提高|||

比较核心的是dfs函数，然后这个函数有取巧的写法：如果outside of boundary就return False

loop， 如果碰到跟word开头的字母一样，把这个扔进去loop，可以考查这个char在这个board的上下左右是否可以选择，补课使用则重置used， 然后return

也还是之前摘录的，backtrack写法关键： 选择 (Options)，限制 (Restraints)，结束条件 (Termination)。




```py
class Solution(object):
    def exist(self, board, word):
        """
        :type board: List[List[str]]
        :type word: str
        :rtype: bool
        """

        def dfs(board, used, row, col, x, y, word, idx):
        	if idx == len(word) :
        		return True

        	if x < 0 or x > row -1 or y < 0 or y > col -1 :
        		return False

        	if board[x][y] == word[idx] and not used[x][y]:
        		used[x][y] = 1 
        		left = dfs(board,used,row,col,x-1,y,word,idx+1)
        		right = dfs(board,used,row,col,x+1,y,word,idx+1)
        		up = dfs(board,used,row,col,x,y-1,word,idx+1)
        		down = dfs(board,used,row,col,x,y+1,word,idx+1)

        		used[x][y] = left or right or up or down
        		return left or right or up or down
        	return False


        row = len(board)
        col = len(board[0]) if row else 0
        used = [ [0 for i in range(col)] for j in range(row)]

        for i in range(row):
        	for j in range(col):
        			if dfs(board,used,row,col,i,j,word,0):
        				return True
        return False
```