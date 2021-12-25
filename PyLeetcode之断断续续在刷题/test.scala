object Solution {
    def minDepth(root: TreeNode): Int = {
        if(root == null) return 0
        val que = scala.collection.mutable.Queue[TreeNode]()
        var depth = 0
        var flag = true
        que.enqueue(root)
        
        while(que.nonEmpty && flag){
            depth += 1
            for(_ <- 0 until que.size; if flag){
                val node = que.dequeue
                if(node.left == null && node.right == null) flag = false
                else {
                    if(node.left != null) que.enqueue(node.left)
                    if(node.right != null) que.enqueue(node.right)
                } 
            } 
        }
        depth
        
    }
} 