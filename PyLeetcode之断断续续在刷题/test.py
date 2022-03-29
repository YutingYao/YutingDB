def bubble_sort(nums):
    n = len(nums)

    for i in range(n):
        for j in range(1, n - i):
            if nums[j - 1] > nums[j]:
                nums[j - 1], nums[j] = nums[j], nums[j - 1]
    return nums