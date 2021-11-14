## Flink 1.11 版本

实际生产环境中，用户经常遭遇 checkpoint 超时失败、长时间不能完成带来的困扰。

在`反压`场景下，整个数据链路堆积了大量 `buffer`，导致 `checkpoint barrier` 排在数据 `buffer` 后面，不能被 `task` 及时处理对齐，也就导致了 `checkpoint` 长时间不能执行。

🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉

Checkpoint & Savepoint 优化:

- 首先，增加了 `Checkpoint Coordinator` 通知 `task` 取消 checkpoint 的机制
  - 这样避免 `task 端`还在执行已经取消的 `checkpoint` 而对系统带来不必要的压力。
  - 同时 `task 端`放弃已经取消的 `checkpoint`，可以更快的参与执行 coordinator 新触发的 `checkpoint`，某种程度上也可以避免`新 checkpoint` 再次执行超时而失败。
  - 对后面默认开启 `local recovery` 提供了便利，`task 端`可以及时清理`失效 checkpoint` 的资源。
- 尝试减少数据链路中的 `buffer 总量`，这样 checkpoint `barrier` 可以尽快被处理对齐。
- 实现了全新的 `unaligned checkpoint` 机制，从根本上解决了反压场景下 checkpoint `barrier` 对齐的问题。
  - aligned 机制保证的是 barrier 前面所有数据必须被处理完,状态实时体现到 operator state 中
  - unaligned 机制把 barrier 前面的未处理数据所反映的 operator state 延后到 failover restart 时通过 channel state 回放进行体现，从状态恢复的角度来说最终都是一致的。
  - Unaligned checkpoint 在反压严重的场景下可以明显加速 checkpoint 的完成时间，因为它不再依赖于整体的计算吞吐能力，而和系统的存储性能更加相关，相当于计算和存储的解耦。
  - 但是它的使用也有一定的局限性，它会增加整体 state 的大小，对存储 IO 带来额外的开销，因此在 IO 已经是瓶颈的场景下就不太适合使用 unaligned checkpoint 机制。
  - unaligned checkpoint 还没有作为默认模式，需要用户手动配置来开启，并且只在 exactly-once 模式下生效。

## Flink 1.12 版本

当 checkpoint 的`间隔`比较小时，这会成为一个很大的问题，因为会创建大量的`小文件`。

File Sink 增加了`小文件合并`功能，从而使得即使作业 checkpoint `间隔`比较小时，也不会产生大量的文件。

## Flink 1.13 版本

以前版本的Flink只有在检查点成功的情况下才会报告指标(例如，持久化数据的大小、触发时间)。

🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉

`unaligned checkpoints`可以在生产中使用了。

如果你想在背压状态下看到程序的问题，鼓励使用`unaligned checkpoints`.

🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉🍉

下面这些改变使`unaligned checkpoints`更容易使用：

- 在可以从`unaligned checkpoints`重新调整应用程序。如果您的应用程序由于无法(负担不起)创建`Savepoints`而需要从`checkpoints`进行扩展，那么这将非常方便

- 对于没有back-pressured的应用程序，启用`unaligned checkpoints`成本更低。`unaligned checkpoints`现在可以通过`超时自适应`地触发，这意味着一个checkpoint作为一个`对齐`的checkpoint开始(不存储任何飞行中的事件)，并回落到一个`unaligned checkpoints`(存储一些飞行中的事件)，如果`对齐`阶段花费的时间超过了一定的时间.

- 更好的报告失败的checkpoints的`异常或者失败的原因`。Flink现在提供了失败或被中止的`检查点的统计信息`，以便更容易地确定`失败原因`，而不必分析日志。

## Flink1.14版本

在流的执行模式下的 `Checkpoint 机制`：

- 对于`无限流`，它的 `Checkpoint` 是由所有的 `source 节点`进行触发的，由 `source 节点`发送 `Checkpoint Barrier` ，当 `Checkpoint Barrier` 流过整个作业时候，同时会存储当前作业所有的 `state` 状态。

- 而在`有限流`的 `Checkpoint 机制`中，Task 是有可能`提早结束`的。上游的 Task 有可能先处理完任务提早退出了，但下游的 Task 却还在执行中。在同一个 `stage` 不同并发下，有可能因为数据量不一致导致部分任务提早完成了。
  
  - 这种情况下，在后续的执行作业中，如何进行 Checkpoint？
  - 在 1.14 中，`JobManager` 动态根据当前任务的执行情况，去明确 `Checkpoint Barrier` 是从哪里开始触发。同时在部分任务结束后，`后续的 Checkpoin`t 只会保存仍在运行 Task 所对应的 stage，通过这种方式能够让`任务执行完成后`，还可以继续做 `Checkpoint` ，在`有限流`执行中提供更好的容错保障。
  
- 在 Checkpoint 过程中，每个`算子`只会进行`准备提交`的操作。比如数据会提交到外部的临时存储目录下，所有任务都完成这次 Checkpoint 后会收到一个`信号`，之后才会执行`正式的 commit`，把所有分布式的临时文件一次性以事务的方式提交到外部系统。这种算法在当前`有限流`的情况下，作业结束后并不能保证有 `Checkpoint`，那么最后一部分数据如何提交？
  - 在 1.14 中，这个问题得到了解决。`Task 处理完所有数据`之后，必须`等待 Checkpoint 完成`后才可以正式的退出，这是流批一体方面针对有限流任务结束的一些改进。

1. 现有 Checkpoint 机制痛点
   - 目前 Flink 触发 Checkpoint 是依靠 barrier 在算子间进行流通
   - barrier 随着算子一直往下游进行发送，当算子下游遇到 barrier 的时候就会进行快照操作
   - 然后再把 barrier 往下游继续发送
   - 对于多路的情况我们会把 barrier 进行对齐，把先到 barrier 的这一路数据暂时性的 block，等到两路 barrier 都到了之后再做快照
2. 现有的 Checkpoint 机制存在以下问题：
   - 反压时无法做出 Checkpoint ：
     - 在反压时候 barrier 无法随着数据往下游流动，造成反压的时候无法做出 Checkpoint。但是其实在发生反压情况的时候，我们更加需要去做出对数据的 Checkpoint，因为这个时候性能遇到了瓶颈，是更加容易出问题的阶段；
   - Barrier 对齐阻塞数据处理 ：
     - 阻塞对齐对于性能上存在一定的影响；
   - 恢复性能受限于 Checkpoint 间隔 ：
     - 在做恢复的时候，延迟受到多大的影响很多时候是取决于 Checkpoint 的间隔，间隔越大，需要 replay 的数据就会越多，从而造成中断的影响也就会越大。但是目前 Checkpoint 间隔受制于持久化操作的时间，所以没办法做的很快。
3. 针对这些痛点，Flink 在最近几个版本一直在持续的优化
   - `Unaligned Checkpoint` 就是其中一个机制。
     - `barrier 算子`在到达 input buffer 最前面的时候，就会开始触发 Checkpoint 操作。它会立刻把 `barrier` 传到算子的 OutPut Buffer 的最前面，相当于它会立刻被下游的算子所读取到。通过这种方式可以使得 `barrier` 不受到数据阻塞，解决反压时候无法进行 Checkpoint 的问题。
     - 当我们把 `barrier` 发下去后，需要做一个短暂的暂停，暂停的时候会把算子的 State 和 input output buffer 中的数据进行一个标记，以方便后续随时准备上传。对于多路情况会一直等到另外一路 `barrier` 到达之前数据，全部进行标注。
     - 通过这种方式整个在做 Checkpoint 的时候，也不需要对 `barrier` 进行对齐，唯一需要做的停顿就是在整个过程中对所有 buffer 和 `state` 标注。这种方式可以很好的解决反压时无法做出 Checkpoint ，和 `Barrier` 对齐阻塞数据影响性能处理的问题。
   - `Generalized Incremental Checkpoint` 主要是用于减少 `Checkpoint 间隔`.
     - 在 Incremental Checkpoint 当中，先让算子写入 `state` 的 changelog。写完后才把变化真正的数据写入到 `StateTable` 上。`state` 的 changelog 不断向外部进行持久的存储化。在这个过程中我们其实无需等待整个 `StateTable` 去做一个持久化操作，我们只需要保证对应的 Checkpoint 这一部分的 changelog 能够持久化完成，就可以开始做下一次 Checkpoint。`StateTable` 是以一个周期性的方式，独立的去对外做持续化的一个过程。
   - 这`两个过程`进行`拆分`后，就有了从之前的需要做`全量持久化 (Per Checkpoint)` 变成 `增量持久化 (Per Checkpoint)` + 后台`周期性全量持久化`，从而达到`同样容错`的效果。在这个过程中，每一次 Checkpoint `需要做持久化的数据量减少`了，从而使得做 Checkpoint 的`间隔能够大幅度减少`。