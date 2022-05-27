class Solution:
    def maxProfit(self, prices: List[int]) -> int:
        sell = 0
        buy = 1e9
        for price in prices:
            buy = min(buy, price)
            sell = max(sell, price - buy)
        return sell