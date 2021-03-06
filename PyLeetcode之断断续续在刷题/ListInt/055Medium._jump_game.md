### 55. Jump Game

题目:
<https://leetcode-cn.com/problems/jump-game/>


难度:

Medium


问题出现在一旦有0，而且这个0是不可跨过的那么无解，无法达到


看了hint，根本不用这个数组，直接用一个数来记录可达最远距离，非常巧妙


```python
class Solution(object):
    def canJump(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        if not nums:
            return True
        if len(nums) == 1:
            return True
        n = len(nums)
        idx, reach = 0, 0
        while idx < n-1 and idx <= reach:  # idx <= reach是为了处理nums[idx] == 0的情况，若idx>reach说明已经失败了
            reach = max(reach, idx+nums[idx])
            idx += 1
        return reach >= n-1
```

idx记录当前loop位置，reach记录当前可到位置

注意这里的while循环的条件是 `idx < n-1 and idx <= reach`，之所以加上 `idx <= reach` 是因为如果```idx > reach```说明```idx```层不可达，其实也可以直接terminate.
