class Solution(object):
    def isCompleteTree(self, root):
        alltreepos = [(root, 1)]
        i = 0
        # 在一个 完全二叉树 中，除了最后一个关卡外，所有关卡都是完全被填满的
        while i < len(alltreepos):
            node, v = alltreepos[i]
            i += 1
            if node:
                alltreepos.append((node.left,  2 * v))
                alltreepos.append((node.right, 2 * v + 1))

        return alltreepos[-1][1] == len(alltreepos)