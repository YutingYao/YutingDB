class Solution:
    def maxheapify(self, heap, root, heap_len):
        p = root
        while p * 2 + 2 <= heap_len: # 😐 while 循环 # 当不是叶子节点 
            l, r = p * 2 + 1, p * 2 + 2 # 代表左右结点
            if r < heap_len and heap[l] < heap[r]:
                bigger = r
            else:
                bigger = l
            # 把最大的元素往上提
            if heap[p] < heap[bigger]:
                heap[p], heap[bigger] = heap[bigger], heap[p]
                p = bigger
            else:
                break
        
    def sortArray(self, nums: List[int]) -> List[int]:
        # 时间复杂度O(N)
        # 从叶子节点开始遍历
        # 如果不是从叶子开始，可能白跑一遍
        '''
        把最大值放在 0 的位置
        '''
        for i in range(len(nums) - 1, -1, -1):
            self.maxheapify(nums, i, len(nums))
            
        # 时间复杂度O(N logN)
        for i in range(len(nums) - 1, -1, -1):
            # 把最大的元素放到末尾
        '''
        把最大值 从 0 的位置，依次移到 i 的位置
        '''
            nums[i], nums[0] = nums[0], nums[i]
            self.maxheapify(nums, 0, i)
        return nums