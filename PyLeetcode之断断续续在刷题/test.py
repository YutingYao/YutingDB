class MountainArray:
   def get(self, index: int) -> int:
   def length(self) -> int:

def binary_search(mountain, target, l, r, key=lambda x: x):
    target = key(target)
    while l <= r:
        mid = (l + r) // 2
        cur = key(mountain.get(mid))
        if cur == target:
            return mid
        elif cur < target:
            l = mid + 1
        else:
            r = mid - 1
    return -1

class Solution:
    def findInMountainArray(self, target: int, mountain_arr: 'MountainArray') -> int:
        left, right = 0, mountain_arr.length() - 1

        while left <= right:
            mid = (right + left) // 2       
            if left == right : 
                peak = right # 关键在于这里
            elif mountain_arr.get(mid) < mountain_arr.get(mid + 1): # 关键在于这里，背一背吧
                left = mid + 1
            else:
                right = mid 
                
        index = binary_search(mountain_arr, target, 0, peak) # 递增序列 二分查找
        if index != -1:
            return index
        index = binary_search(mountain_arr, target, peak + 1, mountain_arr.length() - 1, lambda x: -x) # 递减序列 二分查找
        return index