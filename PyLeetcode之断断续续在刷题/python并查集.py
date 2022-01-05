class UnionFind:
    def __init__(self, nums):
        # 初始时元素是自己的大boss
        self.pre = list(range(nums))
        self.size = [1] * nums
    
    def union(self, x, y):
        px = self.find(x)
        py = self.find(y)

        # 是的px这组的size比较小
        if self.size[px] > self.size[py]:
            px, py = py, px
        # 小的团队交给大的
        self.pre[px] = py
        self.size[py] += self.size[px]

    def find_recur(self, x):
        # 递归写法并没有执行路径压缩~
        if self.pre[x] != x:
            self.pre[x] = self.find(x)
        return self.pre[x]
        
    def find(self, x):
        r = x
        while self.pre[r] != r:
            r = self.pre[r]
            i = x
            while i != r:
                tmp = self.pre[i]
                self.pre[i] = r
                i = tmp
        return r

# Driver code
uf = UnionFind(10)
print(uf.pre)
# 
uf.find(1)
print("执行find之后：", uf.pre)
uf.union(1,2)
print("执行Union之后：", uf.pre)
uf.union(2,3)
print("执行Union之后：", uf.pre)
uf.union(3, 4)
print("执行Union之后：", uf.pre)

# 查看元素的父亲
# print(uf.find(1)) # 4
# print(uf.find(2)) # 4
# print(uf.find(3)) # 4
# print(uf.find(4)) # 4

for i in uf.pre:
    print(uf.find(i))

class Solution:

    def __init__(self):
        """
        初始化
        """
        self.n = 1005
        self.father = [i for i in range(self.n)]


    def find(self, u):
        """
        🍒并查集里寻根的过程
        """
        if u == self.father[u]:
            return u
        self.father[u] = self.find(self.father[u])
        return self.father[u]

    def join(self, u, v):
        """
        将v->u 这条边加入🍒并查集
        """
        u = self.find(u)
        v = self.find(v)
        if u == v : return
        self.father[v] = u
        pass


    def same(self, u, v ):
        """
        判断 u 和 v是否找到同一个根，本题用不上
        """
        u = self.find(u)
        v = self.find(v)
        return u == v

    def findRedundantConnection(self, edges: List[List[int]]) -> List[int]:
        for i in range(len(edges)):
            if self.same(edges[i][0], edges[i][1]) :
                return edges[i]
            else :
                self.join(edges[i][0], edges[i][1])
        return []
    
    class UnionFind:
        def __init__(self, n):
            self.parents = {}
            self.total = [1 for _ in range(n)]
            for i in range(n):
                self.parents[i] = i

        
        def find(self, x):
            original = self.parents[x]
            if original != x:
                original = self.find(original)
                self.parents[x] = original
            return original


        def union(self, x, y):
            rootX, rootY = self.find(x), self.find(y)
            if rootX != rootY:
                self.parents[rootX] = self.parents[rootY]
                self.total[rootY] += self.total[rootX]

        
        def getSize(self, idx):
            return self.total[self.find(idx)]
class UF:
    def __init__(self, n): 
        self.p = list(range(n))
    def union(self, x, y): 
        self.p[self.find(x)] = self.find(y)
    def find(self, x):
        if x != self.p[x]: 
            self.p[x] = self.find(self.p[x])
        return self.p[x]
    
class UnionFind:
    def __init__(self, n: int):
        self.parent = list(range(n))
        self.size = [1] * n
        self.n = n
        # 当前连通分量数目
        self.setCount = n
    
    def findset(self, x: int) -> int:
        if self.parent[x] == x:
            return x
        self.parent[x] = self.findset(self.parent[x])
        return self.parent[x]
    
    def unite(self, x: int, y: int) -> bool:
        x, y = self.findset(x), self.findset(y)
        if x == y:
            return False
        if self.size[x] < self.size[y]:
            x, y = y, x

        self.parent[y] = x
        self.size[x] += self.size[y]
        self.setCount -= 1
        return True
    
    def connected(self, x: int, y: int) -> bool:
        x, y = self.findset(x), self.findset(y)
        return x == y
    
class UF():
    def __init__(self, M):
        self.parent = {}
        for i in range(M):
            self.parent[i] = i
            
    # 可以用while的循环
    def find(self, p):
        while p != self.parent[p]:
            p = self.parent[p]
        return p

    def connected(self, p, q):
        return self.find(p) == self.find(q)

    def union(self, p, q):
        if self.connected(p, q): return 
        leader_p = self.find(p)
        leader_q = self.find(q)
        self.parent[leader_q] = leader_p
# 在构造函数中，初始化一个数组parent，
# parent[i]表示的含义为，索引为i的节点，
# 它的直接父节点为parent[i]。初始化时各个节点都不相连，
# 因此初始化parent[i]=i，让自己成为自己的父节点，从而实现各节点不互连。        
    def __init__(self, n):
        self.parent = list(range(n))
        
    def get_root(self, i):
        while i != self.parent[i]:
            i = self.parent[i]

        return i
 
# 当前每次执行get_root时，需要一层一层的找到自己的父节点，很费时。
# 由于根节点没有父节点，并且文章开始处提到过
# 如果一个节点没有父节点，那么它的父节点就是自己，
# 因此可以说只有根节点的父节点是自己本身。
# 现在我们加上一个判断，判断当前节点的父节点是否为根节点，
# 如果不为根节点，就递归地将自己的父节点设置为根节点，
# 最后返回自己的父节点。
    def get_root(self, i):
        if self.parent[i] != self.parent[self.parent[i]]:
            self.parent[i] = self.get_root(self.parent[i])
        return self.parent[i]
    
    def is_connected(self, i, j):
        return self.get_root(i) == self.get_root(j)
# 当要连通两个节点时，我们要将其中一个节点的根节点的parent，
# 设置为另一个节点的根节点。
# 注意，连通两个节点并非仅仅让两节点自身相连，
# 实际上是让它们所属的集合实现合并。  
    def union(self, i, j):
        i_root = self.get_root(i)
        j_root = self.get_root(j)
        self.parent[i_root] = j_root
# 因此我们需要在union时，尽可能的减小合并后的树的高度。
# 在构造函数中新建一个数组rank，rank[i]表示节点i所在的集合的树的高度。
# 因此，当合并树时，分别获得节点i和节点j的root i_root和j_root之后，
# 我们通过访问rank[i_root]和rank[j_root]来比较两棵树的高度，
# 将高度较小的那棵连到高度较高的那棵上。
# 如果高度相等，则可以随便，并将rank值加一。        
    def union(self, i, j):
        i_root = self.get_root(i)
        j_root = self.get_root(j)

        if self.rank[i_root] == self.rank[j_root]:
            self.parent[i_root] = j_root
            self.rank[j_root] += 1
        elif self.rank[i_root] > self.rank[j_root]:
            self.parent[j_root] = i_root
        else:
            self.parent[i_root] = j_root