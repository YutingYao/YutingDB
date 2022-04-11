nums = [64, 25, 12, 22, 11] 
  
for i in range(len(nums)): 
      
    minpos = i 
    for j in range(i + 1, len(nums)): 
        if nums[j] < nums[minpos]: 
            minpos = j 
                
    nums[i], nums[minpos] = nums[minpos], nums[i] 