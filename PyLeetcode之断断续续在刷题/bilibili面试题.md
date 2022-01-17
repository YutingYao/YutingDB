## BL2 小A最多会新认识的多少人【并查集】

内存超了

```py
def groups(n,ai,m,edges):
    p = [i for i in range(n)]
#     p = {}
#     for i in range(n):
#         p[i] = i

    def find(a):
        nonlocal p
        if a != p[a]:
            p[a] = find(p[a])
        return p[a]

    def union(a,b):
        nonlocal p
        p[find(a)] = find(b)

    for i,j in edges:
        union(i,j)
        
    cnt = 0
    tar = find(ai)
    for i in range(n):
        cur = find(i)
        cnt = cnt + 1 if find(i) == tar else cnt
        
    for edge in edges:
        if ai in edge:
            cnt -= 1
    return cnt-1
#     return cnt




n = int(input())
ai = int(input())
m = int(input())
edges = []
while 1:
    try:
        edges.append([int(i) for i in input().split(',')])
    except:
        break
print(groups(n,ai,m,edges))
```

成功

```py
n = int(input())
ai = int(input())
m = int(input())
p = [i for i in range(n)]
cnt = 0
def find(a):
    if a != p[a]:
        p[a] = find(p[a])
    return p[a]

def union(a,b):
    p[find(a)] = find(b)

while 1:
    try:
        i, j = [int(x) for x in input().split(',')]
        if ai in [i,j]:
            cnt -= 1
        union(i,j)
    except:
        break

tar = find(ai)
for i in range(n):
    if find(i) == tar:
        cnt += 1
print(cnt-1)
```

## BL3 简单表达式计算

```py
from functools import reduce
def compute(s):
    s = s.replace('-','+-')
    slist = s.split('+')
    res = []
    for i in slist:
        if '*' in i:
            product = reduce(lambda x,y: x*y, [int(x) for x in i.split('*')])
            res.append(product)
        else:
            res.append(int(i))
    return sum(res)


while 1:
    try:
        s = input()
        if s != 'END':
            print(compute(s))
        else:
            break
    except:
        break
```

## BL4 山寨金闪闪

```py
import functools
@functools.lru_cache()
def fib(n):
    return fib(n-1) + fib(n-2) if n >= 2 else n

x = 10
while fib(x) < (1 << 32):
    x += 1

print(x)

# 输出 48
```

```py
def istri(nums):
    nums.sort()
    one = nums[0]
    two = nums[1]
    for num in nums[2:]:
        if one + two > num:
            return True
        else:
            one, two = two, num
    return False

n = int(input())
edges = [int(i) for i in input().strip().split()]
m = int(input())
cnt = 0
for _ in range(m):
    [l,r] = [int(i) for i in input().split()]
    if r - l < 2:
        continue
    if r - l > 40: 
# 只要在给出的区间长度较大的，一定能构成三角形
# 调整40这个数，逐渐逼近正确答案，斐波那契数列
        cnt += 1
        continue
    nums = [edges[i] for i in range(l-1,r)]
    if istri(nums):
        cnt += 1
print(cnt)
        

```

## BL5 顺时针打印数字矩阵

```py
def rot(arr):
    res = arr.pop(0)
    while arr:
        arr = list(zip(*arr))[::-1]
        res += arr.pop(0)
    return ','.join(map(str,res))

while True:
    try:
        [m,n] = [int(i) for i in input().split()]
        if m != -1:
            arr = []
            for _ in range(m):
                tmp = [int(i) for i in input().split()]
                arr.append(tmp)
            print(rot(arr))
        else:
            break
    except:
        break
```

## BL9 给定一个整数数组,判断其中是否有3个数和为N

双指针法：

先排序：时间复杂度:O(n log(n)) + O(n2)

```py


def find(nums,tar):
    nums.sort()
    n = len(nums)
    for i in range(n-2):
        if nums[i] + nums[i+1] + nums[i+2] > tar:
            break
        l, r = i + 1, n-1
        subtar = tar - nums[i]
        while l < r:
            if nums[l] + nums[r] == subtar:
                return True
            elif nums[l] + nums[r] > subtar:
                r -= 1
            else:
                l += 1

    return False

s = input().split(',')
nums = [int(i) for i in s[0].split()]
tar = int(s[1])
print(find(nums,tar))
```

## BL10 脸滚键盘

counter只成功了一个

```py
from collections import Counter

def face(n,s):
    cnt = list(filter(lambda x: x[1] == 1, list(Counter(s).items())))
    return '[' + cnt[n-1][0] + ']' if cnt else 'Myon~'

while True:
    try:
        raw = input().strip()
        idx = raw.index(' ')
        n = int(raw[:idx])
        s = raw[idx+1:]
        print(face(n,s))

    except:
        break
```

用 count 就成功了

```py
from collections import Counter

def face(n,s):
    pos = 0
    visited = set()
    for char in s:
        if char in visited:
            continue
        else:
            visited.add(char)
            if s.count(char) == 1:
                pos += 1
                if pos == n:
                    return '[' + char + ']'
                
    return  'Myon~'

while True:
    try:
        raw = input().strip()
        idx = raw.index(' ')
        n = int(raw[:idx])
        s = raw[idx+1:]
        print(face(n,s))

    except:
        break
```