### 353.  Design Snake Game



题目:

<https://leetcode-cn.com/problems/design-snake-game/>

难度： Medium

思路：

纯正单纯方式

在TLE边缘AC

AC代码



```py
class SnakeGame(object):

    def __init__(self, width,height,food):
        """
        Initialize your data structure here.
        @param width - screen width
        @param height - screen height 
        @param food - A list of food positions
        E.g food = [[1,1], [1,0]] means the first food is positioned at [1,1], the second is at [1,0].
        :type width: int
        :type height: int
        :type food: List[List[int]]
        """
        self.width = width
        self.height = height
        self.food = food
        self.snake = [[0,0]]
        self.score = 0
        

    def move(self, direction):
        """
        Moves the snake.
        @param direction - 'U' = Up, 'L' = Left, 'R' = Right, 'D' = Down 
        @return The game's score after the move. Return -1 if game over. 
        Game over when snake crosses the screen boundary or bites its body.
        :type direction: str
        :rtype: int
        """

        nextx, nexty = self.snake[0]

        if direction == 'U':
            nextx -= 1
        if direction == 'L':
            nexty -=1
        if direction == 'R':
            nexty +=1
        if direction == 'D':
            nextx +=1

        # nextx, nexty has food
        if self.food and [nextx,nexty] == self.food[0]:
            self.snake.insert(0,[nextx,nexty])
            self.food = self.food[1:]
            self.score += 1
        # netx, nety outside boundary
        else:
            self.snake = self.snake[:-1]
            self.snake.insert(0,[nextx,nexty])
            if nextx < 0 or nextx > self.height - 1 or nexty < 0 or nexty > self.width - 1:
                    return -1
            noDupes = []

            for snakePt in self.snake:
                # print snakePt,
                if snakePt not in noDupes:
                    noDupes.append(snakePt)
            # print 'snake', self.snake
            # print 'noDpues', noDupes
            if len(noDupes) < len(self.snake):
                return -1
        return self.score

```



