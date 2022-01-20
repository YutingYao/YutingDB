## PDD4 迷宫寻路

无法通过全部测试

参考一下：
https://leetcode-cn.com/problems/shortest-path-to-get-all-keys/

```PY
# a110000011
# 0021111110
# 11101000A0
# 1001100111
# 100B000101
# 11030001b1
from collections import deque
m,n = [int(i) for i in input().split()]
arr = []
for _ in range(m):
    arr.append(list(input()))
dirs = [(0,1),(1,0),(0,-1),(-1,0)]
# start,end,key,door = tuple(),tuple(),tuple(),tuple()
keys = set()
# canvisit = []
res = []
for i in range(m):
    for j in range(n):
        if arr[i][j] == '2':
            start = (i,j)
        elif 'a' <= arr[i][j] <= 'z' :
            keys.add(arr[i][j])
#             doors.add(chr(ord(arr[i][j]) - 32))

que = deque([(start[0],start[1],0,0)])
que2 = deque([('1','2')])
visited = {(start[0],start[1],0)}
# print(que2.popleft())
# direct = False
while que:
    x,y,t,keytime = que.popleft()
    canvisit = que2.popleft()
    for dx, dy in dirs:
        nx, ny = x+dx, y+dy
        if 0 <= nx < m and 0<= ny < n:
            if arr[nx][ny] == '3': # 直接除去
#                 print(nx,ny)
                res.append(t+1)
                break
            elif arr[nx][ny] in keys and (nx,ny,keytime) not in visited:
                
                canvisit += tuple(chr(ord(arr[nx][ny]) - 32))
                canvisit += tuple(arr[nx][ny])
                que.append((nx,ny,t+1,arr[nx][ny]))
                que2.append(canvisit)
                visited.add((nx,ny,arr[nx][ny]))
                
            elif arr[nx][ny] in canvisit and (nx,ny,keytime) not in visited:
                que.append((nx,ny,t+1,keytime))
                que2.append(canvisit)
                visited.add((nx,ny,keytime))
print(min(res))

```

## PDD6 Anniversary【不会做，再看看】

```py
limit = int(input())
city = int(input())
dic,tmp = {},0
for i in range(city-1):
    root,child,dist = list(map(int, input().split()))
    dic.setdefault(root,[]).append([child,dist])
    tmp += child
    
def dfs(node,limit):
    
    if node not in dic or limit <= 0:
        return {0}
    
    distdic = {}
    
    for child, weight in dic[node]:
        if weight <= limit:
            distdic[child] = dfs(child, limit-weight)
            distdic[child] = set(x + weight for x in distdic[child])
            
    if len(distdic)==0:
        return {0}
    
    distset = set()
    for key1 in distdic:
        for key2 in distdic:
            if key1 == key2:
                distset |= distdic[key1]
            elif key1 > key2:
                distset |= set(x+y for x in distdic[key1] for y in distdic[key2] if x+y <= limit)
                
    return distset | {0}

root = city*(city-1)/2 - tmp
print(max(dfs(root, limit)))
```

只找到一条路径版本的答案

```py
# class node():
#     def __init__(self,root = 0,nei = []:
#         self.root = root
#         self.nei = nei
#         node[0] = []        
limit = int(input())
city = int(input())
dic = {}
p = list(range(city))
for i in range(city):
    dic[i] = []
# print(dic)
while True:
    try:
        root,child,dist = [int(i) for i in input().split()]
        dic[root].append((child,dist))
        p[child]  = root
    except:
        break
# print(dic)
def find(a):
    if p[a] != a:
        p[a] = find(p[a])
    return p[a]
# def union(a,b):
#     p[find(a)] = p[find[b]]:
# print()
        
        
def dfs(node,total):
    res.append(total)
    if total > limit:
        return
    if not dic[node]:
        return
    for child,dist in dic[node]:
        
        dfs(child,total+dist)
        
distdic = {}

for child,dist in dic[find(0)]:
    res = []
    dfs(child,dist)
    distdic[child] = res
    
n = len(distdic)
upp = 0

if n > 1:
    for keyi in distdic.keys():
        for keyj in distdic.keys():
            if keyi >= keyj:
                continue
            for a in distdic[keyi]:
                for b in distdic[keyj]:
                    if upp < a+b <= limit:
                        upp = a+b
else:
    for keyi in distdic.keys():
        for a in distdic[keyi]:
            if upp < a <= limit:
                upp = a
print(upp)
    
```

```py
limit = int(input())
city = int(input())
dic = {}
p = list(range(city))
for i in range(city):
    dic[i] = []
# print(dic)
while True:
    try:
        root,child,dist = [int(i) for i in input().split()]
        dic[root].append((child,dist))
        p[child]  = root
    except:
        break
# print(dic)
def find(a):
    if p[a] != a:
        p[a] = find(p[a])
    return p[a]

root = find(0)
# print(dic) {0: [(1, 1), (2, 2), (3, 3), (4, 4)], 1: [], 2: [], 3: [], 4: []}
# print(root)

# visited = {(root,)}
# res = 0

distdic = dict()
def backtrack(node,dist):
    if dist > limit:
        return
    
#     distset.add(dist)
    if not dic[node]:
        return
    if dist <= limit and dic[node]:
        for c, d in dic[node]:
            distdic[c] = backtrack(c, dist + d)
            distdic[c] = set(d + x for x in distdic[c])
        if len(distdic) == 0:
            return {0}
        distset = set()
        for child1, dist1 in dic[node]:
            for child2, dist2 in dic[node]:
                if child1 < child2:
                    continue
                elif child1 == child2:
                    distset |= distdic[child1]
                else:
                    distset |= set(i+j for i in distdic[child1] for j in distdic[child2] if i+j<=limit)
    return distset | {0}
#             if len(que) <= 2:
#                 que.append(c)
#                 backtrack(c, dist + d)
print(backtrack(root,0))


```

## PDD10 选靓号

```py
# sort bY 金额
# 字典序
import math
# 找到需要修改的数字，并改成目标字符
n, k = [int(i) for i in input().split()]
nums = [int(i) for i in list(input())]
mean = sum(nums)/len(nums)
# sortnum = dict()
# for i,num in enumerate(nums):
#     gap = round(abs(num-mean),2)
#     if gap not in sortnum:
#         sortnum[gap] = []
#     sortnum[gap].append(i)
# sortnum = sorted(sortnum.items())
# print(sortnum)
sortnum = []
for i,num in enumerate(nums):
    sortnum.append((round(abs(num-mean),2),i))
sortnum.sort()
# print(sortnum)


candidate = []
waterlevel = sortnum[k-1][0]
sortnumbkp = sortnum.copy()
for _ in range(n-k):
    sortnumbkp.pop()
for gap,i in sortnumbkp:
#     if gap < waterlevel:
    candidate.append(nums[i])
    
# print(candidate)
canmean = sum(candidate)/len(candidate)
x = int(math.ceil(canmean) if canmean % 1 > 0.5 else math.floor(canmean))
tmp = []
res = 0
for gap,i in sortnum:
    if gap < waterlevel:
        res += abs(x-nums[i])
        nums[i] = x
        k -= 1
    if gap == waterlevel:
        tmp.append(i)
for i in range(k):
    if nums[tmp[0]] < x:
        res += abs(x-nums[tmp[-i-1]])
        nums[tmp[-i-1]] = x
    else:
        res += abs(x-nums[tmp[i]])
        nums[tmp[i]] = x
        
print(res)
print(''.join(map(str,nums)))

```

## PDD11 种树