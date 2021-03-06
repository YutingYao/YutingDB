### 469. Convex Polygon



题目： 
<https://leetcode-cn.com/problems/convex-polygon/



难度 : Medium



思路：

凸多边形

记得讲过convex hull，然而已经不知道是啥了。。。。

看wikipedia它的性质 https://en.wikipedia.org/wiki/Convex_polygon

- The polygon is entirely contained in a closed half-plane defined by each of its edges.



http://stackoverflow.com/questions/471962/how-do-determine-if-a-polygon-is-complex-convex-nonconvex



这个算法很有意思也很make sense，叫包礼物。不过也是对付convex hull的，看到有人提这个：



> You can make things a lot easier than the Gift-Wrapping Algorithm... that's a good answer when you have a set of points w/o any particular boundary and need to find the convex hull.
>
> A polygon is a set of points in a list where the consecutive points form the boundary. It is much easier to figure out whether a polygon is convex or not (and you don't have to calculate any angles, either):
>
> 
>
> For each consecutive pair of edges of the polygon (each triplet of points), compute the z-component of the cross product of the vectors defined by the edges pointing towards the points in increasing order. Take the cross product of these vectors:
>
> The polygon is convex if the z-components of the cross products are either all positive or all negative. Otherwise the polygon is nonconvex.
>
> given p[k], p[k+1], p[k+2] each with coordinates x, y:
>
>  dx1 = x[k+1]-x[k]
>
>  dy1 = y[k+1]-y[k]
>
>  dx2 = x[k+2]-x[k+1]
>
>  dy2 = y[k+2]-y[k+1]
>
>  zcrossproduct = dx1 * dy2 - dy1 * dx2
>
> If there are N points, make sure you calculate N cross products, e.g. be sure to use the triplets (p[N-2],p[N-1],p[0]) and (p[N-1],p[0],p[1]).



所以根据这个答案AC代码

```py
class Solution(object):
    def isConvex(self, points):
        """
        :type points: List[List[int]]
        :rtype: bool
        """
        n = len(points)
        zcrossproduct = None

        for i in range(-2, n-2):
            x = [ points[i][0], points[i+1][0], points[i+2][0] ]
            y = [ points[i][1], points[i+1][1], points[i+2][1] ]

            dx1 = x[1] - x[0]
            dy1 = y[1] - y[0]

            dx2 = x[2] - x[1]
            dy2 = y[2] - y[1]

            if not zcrossproduct:
                zcrossproduct = dx1 * dy2 - dy1 * dx2
            elif ( dx1 * dy2 - dy1 * dx2 ) * zcrossproduct < 0:
                return False
        return True
```

