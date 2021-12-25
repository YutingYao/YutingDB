class Solution:
    def pathSum(self, root, targetSum: int):
        res = []
        path = []
        
        def dfs(node, tsum):
            if not node:
                return
            
            path.append(node.val)
            tsum -= node.val # 对于每一个node，当前node的sum = 总和sum - root的值
            
            if not node.left and not node.right and tsum == 0: # 结束条件
                res.append(path[:])
            dfs(node.left, tsum)
            dfs(node.right, tsum)
            
            path.pop()
        
        dfs(root, targetSum)
        return res