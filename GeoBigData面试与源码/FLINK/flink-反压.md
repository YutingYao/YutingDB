

## 监控反压

反压：生产数据的速率 > 下游 task 消费数据的速率

- 闲置的 tasks 为蓝色
- 完全被反压的 tasks 为黑色
- 完全繁忙的 tasks 被标记为红色
- 中间的所有值都表示为这三种颜色之间的过渡色。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.7sqj59ribgg.png)

OK: 0% <= 反压比例 <= 10%

LOW: 10% < 反压比例 <= 50%

HIGH: 50% < 反压比例 <= 100%

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3thsuvygk1w0.png)