```py
posi.append(tmp)
而不是
posi = posi.append(tmp)
```

```py
from collections import Counter # 要大写
Counter 要转化成 list 才能排序
```

```py
sort 与 sorted 区别：

sort 是应用在 list 上的方法，sorted 可以对所有可迭代的对象进行排序操作。
```

```py
' '.join(字符串列表)
注意，一定要是 字符串
```

```py
from functools import reduce
```

```py
zip(list,list)
或者
zip(*arr)

需要转换成list(zip())
```

```py
print('{:.2f}'.format(cc))

保留小数点后2位
```

```py
dp((num>>1)-1)
位运算一定要加小括号
```

```py
注意，从collections中import，并且是三层括号
queue = collections.deque([(root, 1)])
```

```py
s.sort() 不能用于字符串

但可以

s = sorted(s)
```

```py
collections.Counter(s1)
和
collections.defaultdict(int)
的用法其实是一样的
```

```py
哈哈，想不到吧
math.factorial(0) = 1
```

```py
可以得到前缀和
list(accumulate(nums))
```