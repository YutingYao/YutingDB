

算法实现流程思路：

迪杰斯特拉算法每次只找`离起点最近`的一个结点，并将之并入`已经访问过结点的集合`（以防重复访问，陷入死循环），

然后将刚找到的`最短路径的结点`作为`中间结点`来更新`相邻结点`的`路径长度`，这样循环找到图中一个个结点的最短路径。

```py
"""
输入
graph 输入的图
src 原点
返回
dis 记录源点到其他点的最短距离
path 路径
"""
import json
def dijkstra(graph,src):
  if graph ==None:
    return None
  # 定点集合
  nodes = [i for i in range(len(graph))] # 获取顶点列表，用邻接矩阵存储图
  # 顶点是否被访问
  visited = []
  visited.append(src)
  # 初始化dis
  dis = {src:0}# 源点到自身的距离为0
  for i in nodes:
    dis[i] = graph[src][i]
  path={src:{src:[]}} # 记录源节点到每个节点的路径
  k=pre=src
  while nodes:
    temp_k = k
    mid_distance=float('inf') # 设置中间距离无穷大
    for v in visited:
      for d in nodes:
        if graph[src][v] != float('inf') and graph[v][d] != float('inf'):# 有边
          new_distance = graph[src][v]+graph[v][d]
          if new_distance <= mid_distance:
            mid_distance=new_distance
            graph[src][d]=new_distance # 进行距离更新
            k=d
            pre=v
    if k!=src and temp_k==k:
      break
    dis[k]=mid_distance # 最短路径
    path[src][k]=[i for i in path[src][pre]]
    path[src][k].append(k)
 
    visited.append(k)
    nodes.remove(k)
    print(nodes)
  return dis,path

if __name__ == '__main__':
  # 输入的有向图,有边存储的就是边的权值，无边就是float('inf')，顶点到自身就是0
  graph = [ 
    [0, float('inf'), 10, float('inf'), 30, 100],
    [float('inf'), 0, 5, float('inf'), float('inf'), float('inf')],
    [float('inf'), float('inf'), 0, 50, float('inf'), float('inf')],
    [float('inf'), float('inf'), float('inf'), 0, float('inf'), 10],
    [float('inf'), float('inf'), float('inf'), 20, 0, 60],
    [float('inf'), float('inf'), float('inf'), float('inf'), float('inf'), 0]]
  dis,path= dijkstra(graph, 0) # 查找从源点0开始带其他节点的最短路径
  print(dis)
  print(json.dumps(path, indent=4))
```

https://blog.csdn.net/weixin_39433783/article/details/82954269

```py
# -*-coding:utf-8-*-
# 用散列表实现图的关系
# 创建节点的开销表，开销是指从"起点"到该节点的权重
graph = {}
graph["start"] = {}
graph["start"]["a"] = 6
graph["start"]["b"] = 2

graph["a"] = {}
graph["a"]["end"] = 1

graph["b"] = {}
graph["b"]["a"] = 3
graph["b"]["end"] = 5
graph["end"] = {}

# 无穷大
infinity = float("inf")
costs = {}
costs["a"] = 6
costs["b"] = 2
costs["end"] = infinity

# 父节点散列表
parents = {}
parents["a"] = "start"
parents["b"] = "start"
parents["end"] = None

# 已经处理过的节点，需要记录
processed = []


# 找到开销最小的节点
def find_lowest_cost_node(costs):
    # 初始化数据
    lowest_cost = infinity
    lowest_cost_node = None
    # 遍历所有节点
    for node in costs:
        # 该节点没有被处理
        if not node in processed:
            # 如果当前节点的开销比已经存在的开销小，则更新该节点为开销最小的节点
            if costs[node] < lowest_cost:
                lowest_cost = costs[node]
                lowest_cost_node = node
    return lowest_cost_node


# 找到最短路径
def find_shortest_path():
    node = "end"
    shortest_path = ["end"]
    while parents[node] != "start":
        shortest_path.append(parents[node])
        node = parents[node]
    shortest_path.append("start")
    return shortest_path


# 寻找加权的最短路径
def dijkstra():
    # 查询到目前开销最小的节点
    node = find_lowest_cost_node(costs)
    # 只要有开销最小的节点就循环（这个while循环在所有节点都被处理过后结束）
    while node:
        # 获取该节点当前开销
        cost = costs[node]
        # 获取该节点相邻的节点
        neighbors = graph[node]
        # 遍历当前节点的所有邻居
        for n in neighbors.keys():
            # 计算经过当前节点到达相邻结点的开销,即当前节点的开销加上当前节点到相邻节点的开销
            new_cost = cost + neighbors[n]
            # 如果经当前节点前往该邻居更近，就更新该邻居的开销
            if new_cost < costs[n]:
                costs[n] = new_cost
                #同时将该邻居的父节点设置为当前节点
                parents[n] = node
        # 将当前节点标记为处理过
        processed.append(node)
        # 找出接下来要处理的节点，并循环
        node = find_lowest_cost_node(costs)
    # 循环完毕说明所有节点都已经处理完毕
    shortest_path = find_shortest_path()
    shortest_path.reverse()
    print(shortest_path)
# 测试
dijkstra()
```

