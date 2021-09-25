class Solution(object):
    def twoSum(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: List[int]
        """
        lookup = {}
        for index, number in enumerate(nums):
            if target - number in lookup:
                return [lookup[target - number],index]
            lookup[number] = index
            print("lookup",lookup)
            print("ij",index,number)
        return []

    
if __name__ == "__main__":

    list = [11,15,2,9,7]
    target = 9
    so = Solution()
    n = so.twoSum(list, target)
    print("结果: ", n)
