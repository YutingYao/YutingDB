## BD1 罪犯转移【前缀和】【动态规划】

```py
def trans(n,t,c,nums):
    for i in range(1,n):
        nums[i] += nums[i-1]
    
#     窗口第一个数字为nums[-1]
    cnt = 1 if nums[c-1] <= t else 0 
    for i in range(c,n):
        if nums[i]-nums[i-c] <= t:
            cnt += 1
    return cnt

while True:
    try:
        n,t,c = [int(i) for i in input().split()]
        nums = [int(i) for i in input().split()]
        print(trans(n,t,c,nums))
    except:
        break 
```

## BD4 蘑菇阵【动态规划，比较特殊，不能用路径来计算】

最好看一下别人的答案，最好构件(n+1,m+1)的dp

用set(tuple)表示访问过的元素

```py
while 1:
    try:
        n,m,k  = [int(i) for i in input().split()]
        dp = [[0]*m for _ in range(n)]
        dp[0][0] = 1
        dp[-1][-1] = 1
        mogu = set()
        for _ in range(k):
            tmp = tuple(int(i)-1 for i in input().split())
            mogu.add(tmp)
        for i in range(n):
            for j in range(m):
                if (i,j) not in mogu:
                    if i == 0 and j > 0 and n > 1:
                        dp[i][j] = dp[i][j-1] * 0.5
                    elif j == 0 and i > 0 and m > 1:
                        dp[i][j] = dp[i-1][j] * 0.5
                    elif i > 0 and j > 0:
                        probi = 0.5 if i < n-1 else 1
                        probj = 0.5 if j < m-1 else 1
                        dp[i][j] = dp[i][j-1] * probi + dp[i-1][j] * probj
        print('{:.2f}'.format(dp[-1][-1]))
    except:
        break

```

## BD5 字符串匹配

```py
A = input()
B = input()

dp = [[0]*len(A) for _ in range(len(B))]
# print(dp)
for i, bc in enumerate(B):
    for j, ac in enumerate(A):
        if i == 0:
            if ac == bc or bc == '?':
                dp[i][j] = 1
        elif j < i:
            continue
        elif dp[i-1][j-1] == 1 and (ac == bc or bc == '?'):
            dp[i][j] = 1
res = set()
for i in range(len(A)):
    if dp[-1][i] == 1:
        res.add(A[i-len(B)+1:i+1])
print(len(res))
```

## BD8 完成括号匹配

## BD12 最大子序列

## BD16 浇花【动态规划，多看两遍】

```py
while True:
    try:
        n,x1,y1,x2,y2 = [int(i) for i in input().split()]
        table = []
        for _ in range(n):
            x,y = [int(i) for i in input().split()]
            tmp1 = (x1-x)**2 + (y1-y)**2
            tmp2 = (x2-x)**2 + (y2-y)**2
            table.append([tmp1,tmp2])
        table.sort(key = lambda x: x[0], reverse = True)
        dp1 = table[0][0]
        dp2 = table[0][1]
        for i,j in table[1:]:
            # dp1一定能够包得住
            dp1 = min(dp1, i + dp2)
            
            # dp2不一定能够包得住
            dp2 = max(dp2, j)

        print(min(dp1,dp2))
    except:
        break 
```

```py
while True:
    try:
        dp1 = 0
        dp2 = 0
        n,x1,y1,x2,y2 = [int(i) for i in input().split()]
        for _ in range(n):
            x,y = [int(i) for i in input().split()]
            tmp1 = (x1-x)**2 + (y1-y)**2
            tmp2 = (x2-x)**2 + (y2-y)**2
            # 如果新加入的点在圈内
            if tmp1 <= dp1 or tmp2 <= dp2:
                continue
            # 如果新加入的点在圈外
            elif tmp1 + dp2 <= tmp2 + dp1:
                dp1 = max(dp1,tmp1)
            else:
                dp2 = max(dp2,tmp2)
        # 如果有一个半径特别大
        if abs(dp1**0.5 - dp2**0.5) >= ((x1-x2)**2 + (y1-y2)**2)**0.5:
            print(max(dp1,dp2))
        else:
            print(dp1 + dp2)
    except:
        break 
```

## BD18 猜数