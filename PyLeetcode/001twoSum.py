# 给定一个整数数组和一个目标值，找出数组中和为目标值的两个数。

# 你可以假设每个输入只对应一种答案，且同样的元素不能被重复利用。

# > 示例：

# 给定 nums = [2, 7, 11, 15], target = 9

# 因为 nums[0] + nums[1] = 2 + 7 = 9
# 所以返回 [0, 1]

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
