class Solution:
    def findRepeatedDnaSequences(self, s: str) -> List[str]:
        if len(s) < 11: return []

        bin = { "A":  0,
              "C" : 1,
              "G":  2,
              "T":  3 }

        ans = []
        cntDIC = {} # record the appearance time

        x = 0
        for i in range(10): # use former 10 chars to init
            x += bin[s[i]] << (i * 2)
        cntDIC[x] = 1
        
        for i in range(10, len(s)):
            x >>= 2  # remove the left char
            x += bin[s[i]] << 18 # add the right char

            cntDIC[x] = cntDIC.get(x, 0) + 1
            if cntDIC[x] == 2:
                ans.append(s[i - 9:i + 1]) # find the result

        return ans