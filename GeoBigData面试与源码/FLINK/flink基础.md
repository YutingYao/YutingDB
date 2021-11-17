<!-- vscode-markdown-toc -->
* 1. [面试](#)
* 2. [入门](#-1)
	* 2.1. [flink SQL](#flinkSQL)
	* 2.2. [官方文档](#-1)
	* 2.3. [其他](#-1)
* 3. [进阶](#-1)
* 4. [源码](#-1)
* 5. [实战与应用](#-1)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->
##  1. <a name=''></a>面试

[终于有人把云计算与数据库的关系讲明白了](https://mp.weixin.qq.com/s/RIf69WqvJ4xh0Qjx94nL0g)

[当场打脸面试官，Flink流处理任务到底啥时候释放？](https://mp.weixin.qq.com/s/Mm8kvjtVuLu5dc522Z3dLw)

[Flink高频面试题，附答案解析](https://mp.weixin.qq.com/s/9BbHr5kwcxu6ml0izFbwTQ)

[Flink 面试指南 | 终于要跟大家见面了！（附思维导图）](https://mp.weixin.qq.com/s/BnyC02BEVeFh7HU3JxCXgA)

[Flink面试题大全(建议收藏)](https://mp.weixin.qq.com/s/UkqBln6F_zSKtPDjnGkSKg)

[Flink的处理背压​原理及问题-面试必备](https://mp.weixin.qq.com/s/0rrruAIVYRPNpynHbZF41g)

[Flink吐血总结，学习与面试收藏这一篇就够了！！！](https://mp.weixin.qq.com/s/44G_siAfCLINOR0bBrun3g)

[深度解读！新一代大数据引擎Flink厉害在哪？（附实现原理细节）](https://mp.weixin.qq.com/s/3rQURkPEUjdzc8mxVorXOA)

[史上最全干货！Flink面试大全总结（全文6万字、110个知识点、160张图）](https://mp.weixin.qq.com/s/gv1_pAVI-b1DDV6921sr_w)

[深度解读！新一代大数据引擎Flink厉害在哪？（附实现原理细节）](https://mp.weixin.qq.com/s/3rQURkPEUjdzc8mxVorXOA)

##  2. <a name='-1'></a>入门

###  2.1. <a name='flinkSQL'></a>flink SQL

[flink sql 知其所以然（四）| sql api 类型系统](https://mp.weixin.qq.com/s/aqDRWgr3Kim7lblx10JvtA)

[33张图解flink sql应用提交（建议收藏！）](https://mp.weixin.qq.com/s/ak9s2gUw6On7WwoiduEhYQ)

[FlinkSQL窗口函数篇：易理解与实战案例](https://mp.weixin.qq.com/s/ZIYOEvCMsHlV8pZyt1qLyA)

[FlinkSQL窗口，让你眼前一亮，是否可以大吃一惊呢](https://mp.weixin.qq.com/s/WjGFNrnRUJv8qrvQq70QlQ)

### 指标

[官方指标大全](https://nightlies.apache.org/flink/flink-docs-release-1.14/zh/docs/ops/metrics/#end-to-end-latency-tracking)

[如何实时监控 Flink 集群和作业？](https://mp.weixin.qq.com/s/bYm0jkEmFg3uCnFDgaT0iw)

[Flink深入浅出：指标与监控大屏实战](https://mp.weixin.qq.com/s/AGx9Ll-p3KMzyoJayIIKqw)

[使用Prometheus+Grafana监控Flink on YARN作业](https://mp.weixin.qq.com/s/aHI-XG3wpM6mWwYSHszH2Q)

[基于 Prometheus 与 Grafana 打造企业级 Flink 监控系统](https://mp.weixin.qq.com/s/kdD45pBjENzNwA8RH4PjQQ)

[基于Prometheus+Grafana打造企业级Flink监控系统](https://mp.weixin.qq.com/s/TLbPyPWONKhTyo2l6FnJQw)

[Flink 任务实时监控最佳实践](https://mp.weixin.qq.com/s/D83oZ2Tdcq2DRoOl1p_cmA)

[Flink Metrics原理与监控系统搭建(实践篇)](https://mp.weixin.qq.com/s/iF8drds1lM7hSAyasXfAGA)

[一口气搞懂「Flink Metrics」监控指标和性能优化，全靠这33张图和7千字（建议收藏）](https://mp.weixin.qq.com/s/-52EbT3ViuvCqkgN6x5v4g)

[Flink Task Metrics源码解读](https://mp.weixin.qq.com/s/cPiesA4YfAzQCMircyfLMg)

[Flink Metrics监控与 RestApi](https://mp.weixin.qq.com/s/F8qfd3hS1ArTzPXcPLeD8w)

[Flink 指标（一）](https://www.jianshu.com/p/e50586fff515)

[详解 Flink 指标、监控与告警](https://mp.weixin.qq.com/s/wfV1SfOKa1D9_ZF2rdO2kg)

[Flink 实时 metrics](https://mp.weixin.qq.com/s/updGN1pL6O1ZzCU0TtloBg)

[从 0 到 1 搭建一套 Flink 的监控系统](https://mp.weixin.qq.com/s/nSpJksVmlnA_7x59okLOGg)

[深入理解Flink网络栈:监控、指标及反压(下)](https://mp.weixin.qq.com/s/ywpmYUCJRPimkwiWQbL-1w)

### 生产问题

[Flink经典的生产问题和解决方案](https://mp.weixin.qq.com/s/j-6V3ctP-1xXUri-0zvsoA)

[Flink企业级优化全面总结（3万字长文，15张图）](https://mp.weixin.qq.com/s/lgvSG8N6Kz9wU49weHMWVg)

[Flink重点难点：Flink任务综合调优(Checkpoint/反压/内存)](https://mp.weixin.qq.com/s/klSOQU6ag7T812TAQ6DzIg)

[背感压力，Flink背压你了解多少？](https://mp.weixin.qq.com/s/MDpNfo8VOoeNYrhSBePaqQ)

[【Flink 精选】如何分析及处理反压?](https://mp.weixin.qq.com/s/Mjsq4gVJlGMRH8iEyY2EXA)

[Flink 数据传输及反压详解](https://www.jianshu.com/p/efb1313ba52a)

[一文搞懂 Flink 网络流控与反压机制](https://www.cnblogs.com/felixzh/p/11635063.html)

[Flink 实践 | 有赞 Flink 实时任务资源优化探索与实践](https://mp.weixin.qq.com/s/hKkOSPYinr59QYzJE-GAUA)

[揭秘！带你看懂Flink在袋鼠云应用中的前世今生](https://mp.weixin.qq.com/s/t60G8Ni9Qb0b4hjx9ceQow)

[Flink 在 58 同城的应用与实践](https://mp.weixin.qq.com/s/PU73U9sKrzWsuaSaFGT-Fg)

[Flink在大搜车流计算的探索与实践](https://mp.weixin.qq.com/s/KVmS0ZToSxlrpdTtclOj4A)

[学不会去当产品吧？Flink实战任务调优](https://mp.weixin.qq.com/s/-JI6dl7LPJLNj7r2FN59GQ)

[Flink在唯品会的实践](https://mp.weixin.qq.com/s/gQRyD4igKHUCqEh4gRvZjw)

[滴滴是如何从零构建集中式实时计算平台的？| 技术头条](https://mp.weixin.qq.com/s/7inLGzALrDZT_cwzLG04DQ)

###  2.2. <a name='-1'></a>官方文档

[FLINK官方中文文档](https://nightlies.apache.org/flink/flink-docs-release-1.14/zh/docs/learn-flink/overview/)

[pyflink官方文档](https://nightlies.apache.org/flink/flink-docs-release-1.14/api/python/)

[官方配置参数](https://nightlies.apache.org/flink/flink-docs-release-1.14/zh/docs/deployment/config/#memory-configuration)

## kafka

[Flink 1.14.0 全新的 Kafka Connector](https://mp.weixin.qq.com/s/TT7WY9G2g2-jvZE9Paozpw)

###  2.3. <a name='-1'></a>其他

[Flink CDC 2.1 正式发布，稳定性大幅提升，新增 Oracle，MongoDB 支持](https://mp.weixin.qq.com/s/ImCATHNpI_hmWDwh13EvhQ)

[Flink内部Exactly Once三板斧:状态、状态后端与检查点](https://mp.weixin.qq.com/s/iy64wfWDdzTvBH8CaC9KnQ)

[Flink完全分布式集群安装](https://www.zhihu.com/tardis/sogou/art/131592261)

[搭建Flink集群环境](https://mp.weixin.qq.com/s/kxfsJXB2PIjgfmkO2wUXOw)

[使用Flink集群环境进行数据处理](https://mp.weixin.qq.com/s/dSeWar9YL2GIF1ljQyb71A)

[实时计算框架：Flink集群搭建与运行机制](https://mp.weixin.qq.com/s/ze97u_hoZY0CfZwrkwDkjw)

[Flink的设计与实现：集群资源管理](https://mp.weixin.qq.com/s/UxiLIj4hQQhqx35Z9Iz56Q)

[流处理开源框架Flink原理简介和使用](https://mp.weixin.qq.com/s/Ybfl2QHnV-1fAyJ7xE4Crg)

[Flink部署、使用、原理简介](https://mp.weixin.qq.com/s/Fiso69Qi4HTs1ejCNgMYbQ)

[Flink用法介绍](https://mp.weixin.qq.com/s/rTS4LStZoWMe0o61zQD5Cw)

[Flink流式处理百万数据量CSV文件](https://mp.weixin.qq.com/s/YBWbK0NVFSQrzU8_xjoNWA)

[Flink on Zeppelin 系列之：Yarn Application 模式支持](https://mp.weixin.qq.com/s/Uw8rUpDtzokIcQmbSmF8Lg)

[初学Tips - 为啥Flink的Java模块需要Scala的版本后缀](https://mp.weixin.qq.com/s/LFPwQ3tW5zwmWFoTFjMJRg)

[Flink从1.7到1.14版本升级汇总](https://mp.weixin.qq.com/s/gPhLlZFUwqWqLG8oWXcIFw)

[102万行代码，1270 个问题，Flink 新版发布了什么？](https://mp.weixin.qq.com/s/JKJ1UMw5gUNyiBstEQepDw)

[重磅！《Apache Flink 十大技术难点实战》发布，帮你从容应对生产环境中的技术难题](https://mp.weixin.qq.com/s/9ipBG7DxF-fO6QkKUoZWcw)

##  3. <a name='-1'></a>进阶

[你在被窝里刷手机岁月静好，一个名叫 Flink 的 ​“神秘引擎” 却在远方和时间赛跑](https://mp.weixin.qq.com/s/sCGXpBKltvJTAi9e4DnX6g)

[flink on k8s jobmanager HA 完全部署](https://mp.weixin.qq.com/s/-_zhbZ1RVGe0oNJlzD2vPg)

[Flink进阶教程：以flatMap为例，如何进行算子自定义](https://mp.weixin.qq.com/s/I6-ibigdB9TPs38cbYgU3A)

[实时数仓 | Flink实时维表join方法总结（附项目源码）](https://mp.weixin.qq.com/s/X3YYm9psakwF-HamjCvKBg)

[如何解决生产环境 Flink 应用的技术难题？](https://mp.weixin.qq.com/s/LJzJ8o9kl-NCnk1LtmNsKg)

[修炼内功，一文梳理分布式事务及相关算法，剖析 Flink 端到端的一致性](https://mp.weixin.qq.com/s/uTT-f8V8YsKef9zJWnu4bw)

[【学习笔记】-从浅入深理解流式计算框架Flink](https://mp.weixin.qq.com/s/qELKTMY9mudj5QVN4OJ_QQ)

##  4. <a name='-1'></a>源码

[JobManager & TaskManager启动流程分析](https://www.jianshu.com/p/8d0947069977)

[Flink JobManager 和 TaskManager 原理](https://mp.weixin.qq.com/s/GoDOjdAsloaJ5BSduvgN3g)

[JobManager](https://help.aliyun.com/document_detail/62486.html)

[Flink源码解读系列 | JobManager启动](https://mp.weixin.qq.com/s/Cbqb7jtYrgUAA8GZumpmNw)

[Flink内核原理与实现](https://mp.weixin.qq.com/s/NrgKJQit8WdO5-095cBzVw)

[「Flink」工具人Flink之组件通信](https://mp.weixin.qq.com/s/bmQa1xTyMDYKHnz8pYxPJQ)

[【源码解析】flink 任务提交解析](https://mp.weixin.qq.com/s/Qz79PQ9GvbBOXmvsrfnh_w)

[【Flink】第二十六篇：源码角度分析Task执行过程](https://mp.weixin.qq.com/s/BOxSh3YltFrrT_IupQAB6Q)

[手把手教你阅读Flink 源码](https://mp.weixin.qq.com/s/aiU7JapDHHGhbSGkForc3A)

[【源码】Flink 三层图结构——StreamGraph 生成前准备 Transformation](https://mp.weixin.qq.com/s/5I8nXgUr0U0hpQfsED7UIQ)

[大咖分享 | 通过制作一个迷你版Flink来学习Flink源码](https://mp.weixin.qq.com/s/hHz_7oFOfH6lu3bwbo2zsw)

##  5. <a name='-1'></a>实战与应用

[Flink学习篇二 Scala API实现简单词频统计](https://mp.weixin.qq.com/s/rLF9DPP7ECOBHoQLR0kaig)

[盘点Flink实战踩过的坑](https://mp.weixin.qq.com/s/op2bjLmouggirVt-HtPnrQ)

[Apache Flink 集成 Apache Hudi 快速入门指南](https://mp.weixin.qq.com/s/lw4RJFHmiitFm0_lpRtzgA)

[Alink：基于Flink的机器学习平台](https://mp.weixin.qq.com/s/wBuE76WAY6dgVTQfTBxpRg)

[Flink已经足够强大了吗？阿里巴巴说：还不够](https://mp.weixin.qq.com/s/hike1xQcykFyXpNb6E11tw)

[基于 Flink 实现的商品实时推荐系统(附源码)](https://mp.weixin.qq.com/s/apz64lDGc-AZfSwBwpnNMA)

[Flink + Iceberg + 对象存储，构建数据湖方案](https://mp.weixin.qq.com/s/nljOMhKf1P5b63mlfonBEw)

[实时计算框架 Flink 新方向：打造「大数据+AI」 未来更多可能](https://mp.weixin.qq.com/s/kcCmppbAsXHyIdBToLquTA)

[Flink + TiDB，体验实时数仓之美](https://mp.weixin.qq.com/s/MhiTglQH3R_8aNjYkDDllg)

[PyFlink 在聚美优品的应用实践](https://mp.weixin.qq.com/s/zVsBIs1ZEFe4atYUYtZpRg)
