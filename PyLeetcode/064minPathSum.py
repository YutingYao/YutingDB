# 给定一个包含非负整数的 m x n 网格，请找出一条从左上角到右下角的路径，使得路径上的数字总和为最小。

# 说明：每次只能向下或者向右移动一步。

# 示例:

# 输入:
# [
#   [1,3,1],
#   [1,5,1],
#   [4,2,1]
# ]
# 输出: 7
# 解释: 因为路径 1→3→1→1→1 的总和最小。

# 动态规划思想
class Solution(object):
    def minPathSum(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        if not grid or len(grid) == 0:
            return 0
        rowlength = len(grid)
        collength = len(grid[0]) if rowlength else 0
        Initialvalue = [[0 for j in range(collength)] for i in range(rowlength)]
        # range()包括开头，不包括结尾
        for Nrow in range(rowlength): 
            for Ncol in range(collength):
                if Nrow > 0 and Ncol > 0:
                # 到达某个网格的路径只有2个
                # 从上方，或者左侧
                    Initialvalue[Nrow][Ncol] = min(Initialvalue[Nrow-1][Ncol]+grid[Nrow][Ncol], Initialvalue[Nrow][Ncol-1]+grid[Nrow][Ncol])
                elif Nrow > 0 and Ncol == 0:
                    Initialvalue[Nrow][Ncol] = sum([grid[k][0] for k in range(Nrow+1)])
                elif Nrow == 0 and Ncol > 0:
                    Initialvalue[Nrow][Ncol] = sum([grid[0][k] for k in range(Ncol+1)])
                else:
                    Initialvalue[Nrow][Ncol] = grid[0][0]
        return Initialvalue[-1][-1]

if __name__ == "__main__":   
	s = Solution()
	grid = [
	[1,3,1],
	[1,5,1],
	[4,2,1]
	]
	print(s.minPathSum(grid))