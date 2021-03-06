#  774. Minimize Max Distance to Gas Station
**<font color=red>难度: 困难</font>**

## 刷题内容

> 原题连接

* https://leetcode-cn.com/problems/minimize-max-distance-to-gas-station

> 内容描述

```
On a horizontal number line, we have gas stations at positions stations[0], stations[1], ..., stations[N-1], where N = stations.length.

Now, we add K more gas stations so that D, the maximum distance between adjacent gas stations, is minimized.

Return the smallest possible value of D.

Example:

Input: stations = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], K = 9
Output: 0.500000
Note:

stations.length will be an integer in range [10, 2000].
stations[i] will be an integer in range [0, 10^8].
K will be an integer in range [1, 10^6].
Answers within 10^-6 of the true value will be accepted as correct.
```

## 解题方案

> 思路 1

首先明确一下题意，如果目前最大的一个距离是D的话，只要现在我们能够让最大的距离全都变小就行了，也就是说通过在最大距离的station之间再加一些station即可，
加了之后现在原来的最大距离就变小了，就可以了。明确一点，我们需要返回的是我们用最多k个station所能构造出的最小的最大距离。

于是我们判断一下需要我们额外加的stations数目是否小于等于k：
- 若是，且当前最大距离小于10^-6则返回当前最大距离
- 若不是，则继续追加stations数目，继续让最大距离变小

```python
class Solution(object):
    def minmaxGasDist(self, stations, K):
        """
        :type stations: List[int]
        :type K: int
        :rtype: float
        """
        def possible(D):
            return sum(int((stations[i+1] - stations[i]) / D)
                       for i in xrange(len(stations) - 1)) <= K

        l, r = 0, 10**8
        while r - l > 1e-6:
            mid = (l + r) / 2.0
            if possible(mid):
                r = mid
            else:
                l = mid
        return l
```
