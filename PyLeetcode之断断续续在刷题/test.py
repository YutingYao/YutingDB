class Solution:
    def removeDuplicateLetters(self, s: str) -> str:
        res = ["0"] # 初值不要为空，不然index为-1会报错
        for idx, char in enumerate(s):
            print("idx, char:",idx, char,"result: ",res)
            print("s[idx:]",s[idx:],"数量: ",s[idx:].count(res[-1]),"需要大于0,表示要把c删掉，必须后面还有c")
            if char not in res:
                while char < res[-1] and s[idx:].count(res[-1]) > 0:
                    res.pop(-1) # result 删除最后一位
                res.append(char)
        return "".join(res[1:])