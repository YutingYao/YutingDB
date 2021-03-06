### 8. String to Integer (atoi)

题目:
<https://leetcode-cn.com/problems/string-to-integer-atoi/>


难度:
Medium


需要考虑比较多的边界条件&特殊情况
1. 首先输入可能会有空格，所以先去掉空格
2. 去掉空格后要考虑空字符串情况
3. 字符串首位可能会有正负号，要考虑
4. 开始转换成数字，题目说只要遇到非数字就可以break了
5. 结果太大或者太小超过```int```限制就要返回特定数字 ```2147483647``` 或者 ```-2147483648```
6. 根据之前的正负号结果返回对应数值


```python
class Solution(object):
	def myAtoi(self, str):
		"""
		:type str: str
		:rtype: int
		"""
		str = str.strip()
		strNum = 0
		if len(str) == 0:
			return strNum

		positive = True
		if str[0] == '+' or str[0] == '-':
			if str[0] == '-':
				positive = False
			str = str[1:]
		
		for char in str:
			if char >='0' and char <='9':
				strNum = strNum * 10 +  ord(char) - ord('0')
			if char < '0' or char > '9':
				break

		if strNum > 2147483647:
			if positive == False:
				return -2147483648
			else:
				return 2147483647
		if not positive:
			strNum = 0 - strNum
		return strNum

```
