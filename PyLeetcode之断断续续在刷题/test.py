class Solution(object):
    def isCompleteTree(self, root):
        stack = [(root, 1)]
        i = 0
        while i < len(stack):
            node, v = stack[i]
            i += 1
            if node:
                stack.append((node.left, 2 * v))
                stack.append((node.right, 2 * v + 1))

        return  stack[-1][1] == len(stack)