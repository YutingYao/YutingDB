class Solution:
    def verifyPostorder(self, postorder: List[int]) -> bool:
        def isBetween(postorder: List[int], ma: int, mi: int):
            if postorder: 
                val = postorder[-1]
                if mi < val < ma:
                    postorder.pop() # 根
                    isBetween(postorder, ma, val) # 右
                    isBetween(postorder, val, mi) # 左

        isBetween(postorder, sys.maxsize, -sys.maxsize)
        '''
        如果是 postorder 的话，返回结果应该是空
        '''
        return not postorder 