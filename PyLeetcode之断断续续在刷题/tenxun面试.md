## QQ3 编码

学习一下别人的代码

```py
# a000                          b000
# aa00                    ab00 
# aaa0  aab0    aaz0      aba0 
# aaaa  aaba    aaza

# aaaz  aabz    aazz

s = input()
a = (((1*25 + 1)*25+1)*25+1)*(ord(s[0]) - 97) + 1 if len(s) >= 1 else 0
b = ((1*25 + 1)*25+1)*(ord(s[1]) - 97) + 1  if len(s) >= 2 else 0
c = (1*25 + 1)*(ord(s[2]) - 97) + 1  if len(s) >= 3 else 0
d = (ord(s[3]) - 97) if len(s) >= 4 else -1
print(a+b+c+d)

```

## QQ7 拼凑硬币

```py
# 利用奇偶性
# 动态规划
import functools

@functools.lru_cache()
def dp(num):
    if num == 0 or num == 1:
        return 1
    else:
        return dp(num >> 1) if num & 1 else dp(num>>1) + dp((num>>1)-1)
        
n = int(input())
print(dp(n))
 
# 1:1     1
# 2:10    11,2
# 3:11    21
# 4:100   112,22,4
# 5:101   14,122
# 6:110   24,114,1122
# 7:111   124
# 8:1000  8,44,422,4211
# 9:1001  81,441,4221
# 10:1010 82,442,4411,42211 = 
```