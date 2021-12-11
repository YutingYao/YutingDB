<!-- vscode-markdown-toc -->
* 1. [参考资料](#)
* 2. [递归总结](#-1)
* 3. [回溯总结](#-1)
* 4. [DFS总结](#DFS)
	* 4.1. [DFS 的递归模板](#DFS-1)
	* 4.2. [DFS 的循环模板](#DFS-1)
* 5. [bfs](#bfs)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->

##  1. <a name=''></a>参考资料 

打卡！🎨

<https://github.com/freshklauser/LeeCodeSummary/blob/b9bf4a612e61542d20813c29eeab50be4dd42994/Leecode%E5%88%B7%E9%A2%98%E7%AE%97%E6%B3%95%E6%80%BB%E7%BB%93.md>

<https://github.com/staillyd/leetcode/blob/f394f2c7d640e0248141c5a845e753e2bfb55082/leetcode/backtrack/README.md>

<https://github.com/zgkaii/CS-Study-Notes/blob/e133ac093726e355f5ec569f516323aedd36ec99/01-Data-Structures%26Algorithms/leetcode/%E6%9E%81%E5%AE%A2%E6%97%B6%E9%97%B4-%E7%AE%97%E6%B3%95%E8%AE%AD%E7%BB%83%E8%90%A5.md>

<https://github.com/mengyuqianxun/coding-interview-Python/blob/d1eb06a20eaf98ac2daaef0f94b362403dab84f6/Leetcode/Leetcode.md>

##  2. <a name='-1'></a>递归总结



递归主要是需要我们找到它的递推公式，将任务分成一个个小块往下分发，最后别忘了递归的出口。

```py
def func(参数列表):
    # 递归出口
    if 满足某个临界条件:
        return 值
    # 递推公式
    return func(参数列表1) ......
```

```py
# Python
def recursion(level, param1, param2, ...):     
    # recursion terminator     
    if level > MAX_LEVEL:    
        # process_result     
        return     
    # process logic in current level     
    process(level, data...)     
    # drill down     
    self.recursion(level + 1, p1, ...)     
    # reverse the current level status if needed
```

对于有重复计算的问题，我们解决方法是改写成循环，或者使用数组进行记录，递归之前先查询，递归之后将结果存入记录数组。

```py
# 预置一个全为0的记录数组(列表)
rec = [0 for i in range(100)]
def func(参数列表):
    # 递归出口
    if 满足某个临界条件:
        return 值
 
    # 先查询
    if 记录数组rec中已有:
        取出该值，赋给res
    else:
        # 找不到再递归
        res = func(参数列表1) # 递归调用
        res存入rec中
    return res
```

##  3. <a name='-1'></a>回溯总结



 1、路径：也就是已经做出的选择。

 2、选择列表：也就是你当前可以做的选择。

 3、结束条件：也就是到达决策树底层，无法再做选择的条件。

1. `全局变量`： 保存结果
2. 参数设计： 递归函数的参数，是将上一次操作的合法状态当作下一次操作的初始位置。这里的参数，我理解为两种参数：`状态变量`和`条件变量`。

- `状态变量（state）`就是最后结果（result）要保存的值
- `条件变量`就是决定搜索是否完毕或者合法的值。

3. `完成条件`： `完成条件`是决定 `状态变量`和`条件变量` 在取什么值时可以判定整个`搜索流程结束`。`搜索流程`结束有两种含义：

- `搜索成功`并保存结果
- `搜索失败`并返回上一次状态。
  
4. 递归过程： 传递当前状态给下一次递归进行搜索。

模板：

```python
result = []       # 总的结果集
path = []         # 路径的记录
state variables   # 一些状态变量来判断是否剪枝
# 这些状态变量同样也可以通过参数传给回溯函数, 实际题目中有些混用.

def backtracking ():
  if 结束条件:
    更新/记录结果

  for 选择 in 搜索列表:
    if 选择 not 题目条件:       # ----> 剪枝 [这个判断过程也可放在for外面]
      continue
    设置状态                    # ----> 设置状态
    backtracking()             # ----> 进入递归
    还原状态                    # ----> 还原状态

backtracking()
return result
```

```python
def problem (nums):
  # 通常回溯法的例题都使用闭包形式, 而不是直接原题函数进行递归.
  # 因为很多情况下backtracking比纯DFS题目要来的复杂, 考虑的东西更多.
  # Set up some closure variables outside the backtracking function
  result = []
  path = []
  # other helping variables for recording and pruning, such as count, ifUsed...
  state = xxx

  # Define a backtracking recursive function
  def backtracking (someStateToCarry):
    # the base case that meet the requirement of this problem
    # 回溯法的特点是, 通过剪枝, 维护的path始终是一个valid路径
    # 因此判断path是否满足条件, 不应该出现在base case当中, 而应该在剪枝过程中
    if len(path) == len(nums):
      result.append(path[:])
      return
    
    # search for all possible solutions
    # [KEY STEP] we need to set up a solution list for each problem
    # choices是总共解的空间, 代表每次在递归函数当中搜寻下一步路径的范围: 例如走迷宫就是四个方向
    for i in range(choices):
      # [KEY STEP] Pruning
      # we should avoid recursing if some scenarios meet
      # 剪枝通常发生在这里, 这个解的空间可能在某些时候已经没有搜索下去的必要了
      # 例如走迷宫的时候遇到了墙, 或者下一次搜索的点已经遍历过了
      if state condition: continue

      # [KEY STEP] Set current state
      # 包括存储路径, 记录某个点, 计数器加1, 或标记当前点为已访问
      path.append(choices[i])
      # do recursion from current node
      # we may update some state to carry out such as, starting index...
      backtracking(updateStateToCarry)
      # [KEY STEP] Restore current state: 状态重置: 好比取消当前点已访问的tag...
      path.pop()

  # Start backtracking recursion.
  # If there is some state to carry out for the next recursion we can set it here.
  # 实际上backtracking函数的的参数全部可以放在闭包里面, 看不同题目的使用习惯, 这两者是等价的
  backtracking(initialStateToCarry)
  # Return problem function
  return result
```

```python
res = []    # 定义`全局变量`保存最终结果
state = []  # 定义`状态变量`保存当前状态
p,q,r       # 定义`条件变量`（一般`条件变量`就是题目直接给的参数）
def back(状态，条件1，条件2，……):
    if # 不满足合法条件（可以说是剪枝）
        return
    elif # 状态满足最终要求
        res.append(state)   # 加入结果
        return
    # 主要递归过程，一般是带有 循环体 或者 条件体
    for # 满足执行条件
    if  # 满足执行条件
        back(状态，条件1，条件2，……)
back(状态，条件1，条件2，……)
return res
```

```py
result = []
def backtrack(路径, 选择列表):
    if 满足结束条件:
        result.add(路径)
        return

    for 选择 in 选择列表:
        做选择
        backtrack(路径, 选择列表)
        撤销选择
```

```py
result = []
def backtrack(路径, 选择列表):
    # 第一步：判断输入或者状态是否非法？
    # 第二步：判断递归是否应该结束
    if 满足结束条件:
        result.add(路径)
        return

    # 遍历所有可能出现的情况
    for 选择 in 选择列表:
        # 第三步：尝试下一步的可能性
        做选择
        # 递归
        backtrack(路径, 选择列表)
        # 第四步：回溯到上一步
        撤销选择
```

```py
res = []
path = []

def backtrack(未探索区域, res, path):
    if path 满足条件:
        res.add(path) # 深度拷贝
        # return  # 如果不用继续搜索需要 return
    for 选择 in 未探索区域当前可能的选择:
        if 当前选择符合要求:
            path.add(当前选择)
            backtrack(新的未探索区域, res, path)
            path.pop()
```

```py
digits = {'1':'abc','2':'de'}
lis = list(digits.keys())
def trackback(index):
 if index == len(digits):
  ans.append(''.join(comb))
 else:  
  digit = lis[index]
  for letter in digits[digit]:
   comb.append(letter)
   trackback(index+1)
   comb.pop()
comb = []
ans = []
trackback(0)
```

```py
def generateParenthesis(self, n: int) -> List[str]:
    ans = []
    def backtrack(S, left, right):
        if len(S) == 2 * n:
            ans.append(''.join(S))
            return
        if left < n:
            S.append('(')
            backtrack(S, left+1, right)
            S.pop()
        if right < left:
            S.append(')')
            backtrack(S, left, right+1)
            S.pop()
    backtrack([], 0, 0)
    return ans
```

##  4. <a name='DFS'></a>DFS总结



特点：

- DFS 用到了`递归`，

- 需要有一个`递归`结束的`出口`。

- 它常`与循环混合使用`，即`多路递归`，

- 而且存在有`回溯`这一步骤。

```py
def dfs(参数列表,position)(当前路径, 当前层级，选择列表，结果集): #position表示需要对temp哪个位置进行填充
    # 1. 终止条件
    if 满足条件position == len(arr):
        递归出口
        return
    # 循环
    for i from range(0, len(arr)):
        if visit[index] == True:
            temp[position] = arr[index]
     # 2. 修改
            visit[index] = False # 修改某变量的值(一般是修改数组)
     # 3. 
            dfs(参数列表position + 1)(cur,level + 1, 选择列表，结果集) # 子问题求解
     # 4.  恢复
            visit[index] = True  # 回溯。恢复之前被修改的值  
```

```py
class Solution:
    def permute(self, nums: List[int]) -> List[List[int]]:
        # return list(itertools.permutations(nums))
        res = []
        visited = set()
        self.dfs(nums, [], visited, res)
        return res

    def dfs(self, nums, path, visited, res):
        if len(path) == len(nums):
            res.append(path)
            return
        
        for i, num in enumerate(nums):
            if num in visited:
                continue
            path.append(num)
            visited.add(num)
            self.dfs(nums, path[:], visited, res)
            # 回溯
            path.pop()
            visited.remove(num)
```

###  4.1. <a name='DFS-1'></a>DFS 的递归模板

```py
def dfs(cur, target, visited):
    if cur is target:
        return True

    for next in neighbors of cur:
        if next not in visited:
            visited.add(next)
            return True if dfs(next, target, visited)
    return False
```

```py
visited = set() 
def dfs(node, visited):    
    if node in visited: # terminator     
        # already visited      
        return      
    
    visited.add(node)  
    # process current node here.  
 ... 
    for next_node in node.children():   
        if next_node not in visited:    
            dfs(next_node, visited)
```

```py
def dfs(temp):
    if len(temp) == m: 
        rt.append(temp)
        return

    for j in range(m):
        if visit[j]: continue

        visit[j] = 1
        dfs(temp+[j+1])
        visit[j] = 0
    return
```

###  4.2. <a name='DFS-1'></a>DFS 的循环模板



```py
def dfs(root, target):
    stack = [root]
    while stack:
        cur = stack.pop()
        if cur is target:
            return True

        for next in neighbors of cur:
            if next not in visited:
                stack.append(next)
                visited.add(next)
    return False
```

```py
def DFS(self, root):  
    if tree.root is None:   
        return []  
    
    visited, stack = [], [root] 
    
    while stack:   
          node = stack.pop()   
          visited.add(node)  
          process (node)         
          # 生成相关的节点  
          nodes = generate_related_nodes(node)   
          stack.push(nodes)  
          # other processing work  ...
```

##  5. <a name='bfs'></a>bfs

```py
class Solution:
    def bfs(k):
        # 使用双端队列，而不是数组。因为数组从头部删除元素的时间复杂度为 N，双端队列的底层实现其实是链表。
        queue = collections.deque([root])
        # 队列不空，生命不止！
        while queue:
            node = queue.popleft()
            # 由于没有记录 steps，因此我们肯定是不需要根据层的信息去判断的。否则就用带层的模板了。
            if (node 是我们要找到的):
                return node
            if node.right:
                queue.append(node.right)
            if node.left:
                queue.append(node.left)
        return -1
```

```py
class Solution:
    def bfs(k):
        # 使用双端队列，而不是数组。因为数组从头部删除元素的时间复杂度为 N，双端队列的底层实现其实是链表。
        queue = collections.deque([root])
        # 记录层数
        steps = 0
        # 需要返回的节点
        ans = []
        # 队列不空，生命不止！
        while queue:
            size = len(queue)
            # 遍历当前层的所有节点
            for _ in range(size):
                node = queue.popleft()
                if (step == k):
                    ans.append(node)
                if node.right:
                    queue.append(node.right)
                if node.left:
                    queue.append(node.left)
            # 遍历完当前层所有的节点后 steps + 1
            steps += 1
        return ans
```
