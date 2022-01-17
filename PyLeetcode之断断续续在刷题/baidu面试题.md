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