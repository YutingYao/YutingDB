###93. Restore IP Addresses

题目:

<https://leetcode-cn.com/problems/restore-ip-addresses/>


难度:

Medium


基本思路已达到，等待AC之路

结果AC之路还是比较漫长的，因为不允许前缀为0困扰了一下

```py
class Solution(object):
    def restoreIpAddresses(self, s):
        """
        :type s: str
        :rtype: List[str]
        """
        self.res = []
        self.singgleAddresses([],s,4)
        for i in range(len(self.res)):
            self.res[i] = '.'.join(str(j) for j in self.res[i])
        return self.res
            
        
        
    def singgleAddresses(self, curRes, s, k):
        """
        :type s: str
        :rtype: List[str]
        """
        if len(s) == 0 and k == 0:
            if curRes not in self.res:
                self.res.append(curRes)
        if len(s) == 0 or k < 0:
            return
        else:
            if self.between0And255(s[:1]):
                self.singgleAddresses(curRes + [int(s[:1])], s[1:], k-1) 
            if self.between0And255(s[:2]):
                self.singgleAddresses(curRes + [int(s[:2])], s[2:], k-1)  
            if self.between0And255(s[:3]):
                self.singgleAddresses(curRes + [int(s[:3])], s[3:], k-1) 
            
    def between0And255(self,s):
        #前缀不允许为0
        if int(s) == 0 :
            if len(s) == 1 :
                return True
            else:
                return False
                
        if int(s) > 0 and s[0] == '0':
            return False
        if int(s) > 0 and int(s) <= 255:
            return True
        return False
                
```


