class Solution:
    def findLength(self, nums1: List[int], nums2: List[int]) -> int:
        length = left = 0
        if nums1 and nums2:
            # 将数字转换为字符串
            a, b, n = ''.join(map(chr, nums1)), ''.join(map(chr, nums2)), len(nums1)
            while length + left < n: # 😐 while 循环
                # 这里使用lenth保存结果，用left跳出循环
                if a[left : left + length + 1] in b:
                    length += 1
                else:
                    left += 1
        return length 