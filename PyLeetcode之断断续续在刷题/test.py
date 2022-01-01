class Solution:
    def mincostTickets(self, days: List[int], costs: List[int]) -> int:
        dayset = set(days)
        N = len(days)
        durations = [1, 7, 30]

        @lru_cache(None)
        def dp(date):
            if date > N:
                return 0
            elif date in dayset:
                return min(dp(date + gap) + fee for fee, gap in zip(costs, durations))
            else:
                return dp(date + 1)

        return dp(1)