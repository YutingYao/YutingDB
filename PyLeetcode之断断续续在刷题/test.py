class Solution:
    def combinationSum(self, candidates: List[int], target: int) -> List[List[int]]:
        res = []

        def backtrack(firstIdx,path):
            if sum(path) == target:
                res.append(path[:]) 
                # 易错点，这里是res.append(path[:])，而不是res.append(path)
                return
            if sum(path) > target:
                return
            if sum(path) < target:
                for i in range(firstIdx,len(candidates)):
                    backtrack(i,path + [candidates[i]])
        backtrack(0,[])
        return res