# 示例 1:
# 输入: word1 = "horse", word2 = "ros"
# 输出: 3
# 解释: 
# horse -> rorse (将 'h' 替换为 'r')
# rorse -> rose (删除 'r')
# rose -> ros (删除 'e')

# 示例 2:
# 输入: word1 = "intention", word2 = "execution"
# 输出: 5
# 解释: 
# intention -> inention (删除 't')
# inention -> enention (将 'i' 替换为 'e')
# enention -> exention (将 'n' 替换为 'x')
# exention -> exection (将 'n' 替换为 'c')
# exection -> execution (插入 'u')
class Solution:
    def minDistance(self, word1, word2):
        """
        :type word1: str
        :type word2: str
        :rtype: int
        """
        # 初始化一个 len(word1)+1 * len(word2)+1 的矩阵
        matrix = [[i+j for j in range(len(word2) + 1)] for i in range(len(word1) + 1)]
        # 辅助理解，matrix 矩阵的样子
        # print(matrix)
        for i in range(1, len(word1)+1):
            for j in range(1,len(word2)+1):
                if word1[i-1] == word2[j-1]:
                    d = 0
                else:
                    d = 1
                matrix[i][j] = min(matrix[i-1][j]+1, matrix[i][j-1]+1, matrix[i-1][j-1]+d)

        return matrix[len(word1)][len(word2)]
    
s = Solution()
word1 = 'horse'
word2 = 'ros'
print(s.minDistance(word1, word2))