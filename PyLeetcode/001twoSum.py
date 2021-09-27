# EASY
class Solution(object):
    def twoSum(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: List[int]
        """
        lookup = {} # lookup用于存储已经扫描过的列表，减少计算量
        for index, number in enumerate(nums): # enumerate获得带有索引的列表
            if target - number in lookup:
                return [lookup[target - number],index]
            lookup[number] = index # 查找[数字] = 索引
            print("lookup",lookup)
            print("ij",index,number)
        return []

    
if __name__ == "__main__":

    numlist = [11,15,2,9,7]
    target = 9
    so = Solution()
    n = so.twoSum(numlist, target)
    print("结果: ", n)
