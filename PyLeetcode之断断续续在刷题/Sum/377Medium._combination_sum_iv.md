###377. Combination Sum IV

题目:

<https://leetcode-cn.com/problems/combination-sum-iv/>


难度:

Medium


直接用combination sum的思路： 超时

```python
class Solution(object):
    def combinationSum4(self, candidates, target):
        """
        :type candidates: List[int]
        :type target: int
        :rtype: List[List[int]]
        """
        def combSum(candidates, target, start, valueList):
            length = len(candidates)
            if target == 0 :
                res.append(valueList)
            for i in range(start, length):
                if target < candidates[i]:
                    return 
                combSum(candidates, target - candidates[i], 0, valueList + [candidates[i]])

        candidates = list(set(candidates))
        candidates.sort()
        res = []
        combSum(candidates, target, 0, [])
        return len(res)
```





说起来标签是dp,也知道是dp啊,状态转移方程：



参考:

<http://www.cnblogs.com/grandyang/p/5705750.html>

> 
>
> 我们需要一个一维数组dp，其中dp[i]表示目标数为i的解的个数，然后我们从1遍历到target，对于每一个数i，遍历nums数组，如果i>=x, dp[i] += dp[i - x]。这个也很好理解，比如说对于[1,2,3] 4，这个例子，当我们在计算dp[3]的时候，3可以拆分为1+x，而x即为dp[2]，3也可以拆分为2+x，此时x为dp[1]，3同样可以拆为3+x，此时x为dp[0]，我们把所有的情况加起来就是组成3的所有情况了



AC代码

```python
class Solution(object):
    def combinationSum4(self, candidates, target):
        """
        :type candidates: List[int]
        :type target: int
        :rtype: List[List[int]]
        """
        dp = [0 for i in range(target+1)]

        dp[0] = 1

        for i in range(target+1):
            for candidate in candidates:
                if i >= candidate:
                    dp[i] += dp[i - candidate]
        return dp[-1]
```

