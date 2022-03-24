class Solution:
    def reversePairs(self, nums: List[int]) -> int:
        negUpQue = []
        res = 0
        for num in nums:
            # 变负数插入，绝了-v，构成递增序列
            i = bisect.bisect_left(negUpQue,-num) # bisect_left 返回的待插入位置分别是 0，1，1，3，
            res += i # 前面有多少个比它大的，当前数就有多少个逆序对,加起来就是逆序对总数 5
            negUpQue[i:i] = [-num]
            # 这里也可以写：q.insert(i, -v)
        return res