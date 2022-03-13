class Solution:
    def binaryTreePaths(self, root: TreeNode) -> List[str]:
        if not root:
            return []
        if not root.left and not root.right:
            return [str(root.val)]
        paths = []
        if root.left:
            for pt in self.binaryTreePaths(root.left):
                paths.append(str(root.val) + '->' + pt)
        if root.right:
            for pt in self.binaryTreePaths(root.right):
                paths.append(str(root.val) + '->' + pt)
        return paths  