<!-- vscode-markdown-toc -->
* 1. [学习链接](#)
	* 1.1. [面试](#-1)
	* 1.2. [基础](#-1)
		* 1.2.1. [RDD](#RDD)
		* 1.2.2. [分区器](#-1)
		* 1.2.3. [DAG](#DAG)
		* 1.2.4. [序列化](#-1)
		* 1.2.5. [RPC](#RPC)
		* 1.2.6. [运行机制](#-1)
		* 1.2.7. [安装与配置](#-1)
		* 1.2.8. [优化](#-1)
		* 1.2.9. [算法](#-1)
		* 1.2.10. [源码](#-1)
	* 1.3. [应用与实战](#-1)
		* 1.3.1. [json](#json)
		* 1.3.2. [streaming](#streaming)
		* 1.3.3. [其他](#-1)
* 2. [面试题 - via Yuting](#-viaYuting)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->
##  1. <a name=''></a>学习链接

###  1.1. <a name='-1'></a>面试

[五万字 | Spark吐血整理，学习与面试收藏这篇就够了！](https://mp.weixin.qq.com/s/aohvYfKWwtIUi63qII5jYw)

[面试专题：Spark系列-数据本地性（data locality）](https://mp.weixin.qq.com/s/cmN6C03uJNP4XgrihWCfiw)

[一篇并不起眼的Spark面试题](https://mp.weixin.qq.com/s/GWEhQOCPBAMTofu5YQgx5w)

[大数据Spark理论实操面试题整理](https://mp.weixin.qq.com/s/HEBh1CIbP8F1pui4BohP9A)

[面试|十分钟聊透Spark(附录综合案例)](https://mp.weixin.qq.com/s/HivV3YeGkfzZkottqBmhrQ)

[Spark面试干货总结](https://mp.weixin.qq.com/s/JYfm7NIY8QyjAjV63LCd_Q)

[高频面试题（三）[Scala/Spark]](https://mp.weixin.qq.com/s/ROqp3l8mSe-JPaf9wLbi-g)

[常见的Spark面试题](https://mp.weixin.qq.com/s/SILEzkEOgJ9ys0ukFbKrvQ)

[独孤九剑-Spark面试80连击(上)](https://mp.weixin.qq.com/s/t9exZ-U8PjGMprENHqO8Wg)

[【硬刚大数据】从零到大数据专家面试篇之SparkSQL篇](https://mp.weixin.qq.com/s/w9StfmI-1h4nAkpp3d-SZw)

[大数据面试题总结（附答案）](https://mp.weixin.qq.com/s/kpBmsfJ3D_SP8pUadGvgig)

###  1.2. <a name='-1'></a>基础

####  1.2.1. <a name='RDD'></a>RDD

[Spark原理-RDD及共享变量](https://mp.weixin.qq.com/s/RkujENo3x9cKhv5ek2xPBw)

[【干货】Spark入门 | RDD原理与基本操作](https://mp.weixin.qq.com/s/4ffL-mK3t155wyxHuT6FjQ)

[Spark-RDD的持久化和缓存](https://mp.weixin.qq.com/s/6F1IVikeWyilcpNBCXIYRw)

[SparkCore剖析](https://mp.weixin.qq.com/s/8i5eVTUFWyB-m_znKXmXZg)

[RDD的理解](https://mp.weixin.qq.com/s/8PiijwnJfdAZvUL291wUeA)

[RDD任务切分之Stage任务划分(图解和源码)](https://mp.weixin.qq.com/s/fOIuZwUbS-rVStNYIf4dvA)

[Spark学习记录|RDD分区的那些事](https://mp.weixin.qq.com/s/GGII-Dzs1m_3sZ6J5cTYDQ)

[大数据Spark RDD运行设计底层原理](https://mp.weixin.qq.com/s/IvZVKlNVI0lnQtnr-xzG_w)

[经典篇 | 大数据之谜Spark基础篇，Spark RDD内幕详解](https://mp.weixin.qq.com/s/jbfHr5xXdJHb8K836ZXopQ)

[了解Spark RDD](https://mp.weixin.qq.com/s/LiUYx_GvUL6h9ixUl7YCyQ)

[RDD序列化与依赖关系](https://mp.weixin.qq.com/s/oBxgYLcMV8fcZI08gQCg_A)

[spark--RDD和shuffle机制](https://mp.weixin.qq.com/s/U1tEicrC0tIEFlKNGJZn8Q)

[大数据技术，Spark之RDD，这些就够了，RDD超详细讲解（一）](https://mp.weixin.qq.com/s/vdwvpw2iUupx_EpKYCHYaw)

[Spark入门必读：核心概念介绍及常用RDD操作](https://mp.weixin.qq.com/s/xRRHr3QlgSIcRnhV_LK3_w)

[Spark原理与实战(四)-- Spark核心数据抽象RDD](https://mp.weixin.qq.com/s/Yocet_KvenxEybqFevKX0A)

[生产常用Spark累加器剖析之一](https://mp.weixin.qq.com/s/yZLTaHaWcL7u7q71x7Exrg)

####  1.2.2. <a name='-1'></a>分区器

[Spark源码分析之分区器的作用](https://mp.weixin.qq.com/s/4D51wdJ9ynJpfu3wzjgo5A)

####  1.2.3. <a name='DAG'></a>DAG

[Spark任务调度-Stage的划分与提交](https://mp.weixin.qq.com/s/qQOmt-ZWTI-k_JXD27vxfA)

[一文讲解Spark RDD 的依赖关系以及DAG划分](https://mp.weixin.qq.com/s/O4J7bQEqSuzkBAH9IvnsgA)

[有向无环图（DAG）的温故知新](https://mp.weixin.qq.com/s/13qrmJFyIP9w2vicx6NBaA)

[图解Spark系列：简要介绍DAG划分算法](https://mp.weixin.qq.com/s/7tRWj21xZCILfIGMIlyv6w)

[Spark任务调度 | Spark，从入门到精通](https://mp.weixin.qq.com/s/VrRJOjosGUOacCxks0mwgA)

[Spark-RDD宽窄依赖、pipeline计算模式、Stage](https://www.pianshen.com/article/5484261723/)

####  1.2.4. <a name='-1'></a>序列化

[关于Spark的序列化问题](https://mp.weixin.qq.com/s/Fw3fhhEtFy4lYt8u7_aOmg)

[spark-序列化](https://mp.weixin.qq.com/s/0WUsn3i34Gqu-N7-Ot87Jw)

[关于spark的rdd序列化](https://www.jianshu.com/p/65e6c8463511)

[spark程序的序列化问题及解决方法](https://www.cnblogs.com/jimmy888/p/13551707.html)

####  1.2.5. <a name='RPC'></a>RPC

[Spark 通信篇 | spark RPC 模块篇](https://mp.weixin.qq.com/s/_Zwknq_yd5SbZ85RnXX9xQ)

[Spark原理图解：Rpc通信](https://mp.weixin.qq.com/s/p9JrdtB834KCYbI3acXqiw)

[Spark调度系统之“权力的游戏”](https://mp.weixin.qq.com/s/8vNw3e_aAtui9zZKSV6DXw)

[Spark Rpc 三剑客的理解](https://mp.weixin.qq.com/s/Fc_Et_j90OOESQGQXwGy2g)

[spark 源码分析之七--Spark RPC剖析之RpcEndPoint和RpcEndPointRef剖析](https://www.cnblogs.com/johnny666888/p/11135539.html)

[Spark 源码（1） - 通信基石之 Spark Rpc 的发展历程](https://mp.weixin.qq.com/s/WYtxvp1IiRdK-Yy6SoB0qQ)

####  1.2.6. <a name='-1'></a>运行机制
 
[Spark on YARN-Cluster和YARN-Client的区别](https://mp.weixin.qq.com/s/tzO0iJT2aauRh0T3R6Oxnw)

[Yarn的WEB UI的详细说明](https://www.jianshu.com/p/87bafb2d8828)

[2分38秒了解Spark运行结构](https://mp.weixin.qq.com/s/XSLpytFr10iHfSf5-RCvdw)

####  1.2.7. <a name='-1'></a>安装与配置

[Spark集群安装配置预备知识](https://mp.weixin.qq.com/s/0J2jexQ3VhkF7vTrV_Iz4g)

[spark快速入门（一）-------spark概述及安装配置](https://mp.weixin.qq.com/s/IXUan9_ITT82pZSxqsSbUA)

[Spark编程基础(五)-Spark集群安装配置详细过程](https://mp.weixin.qq.com/s/UyR6QotcnDPaepGCVi9gWw)

[Spark部署一spark安装](https://mp.weixin.qq.com/s/W-ahWOgeaAX5-1E8jw-3kw)

####  1.2.8. <a name='-1'></a>优化

[四万字长文 | Spark性能优化实战手册（建议收藏）](https://mp.weixin.qq.com/s/2WdKZs2_ijTIyW_ECdqvHg)

[Spark性能优化实战手册.pdf](https://mp.weixin.qq.com/s/85J8CuIBzJ2Ai9Y7LeHGQA)

[sparksql的调优参数 maxPartitionBytes openCostInBytes 切分文件](https://mp.weixin.qq.com/s/E7QDdUdg00pC_jI4WKFjUw)

[【技术难点】Spark性能调优-RDD算子调优篇](https://mp.weixin.qq.com/s/tr3wUUTToQ67qZ4DL8qwow)

####  1.2.9. <a name='-1'></a>算法

[干货分享 | 史上最全Spark高级RDD函数讲解](https://mp.weixin.qq.com/s/zal7_Zw1dymBtpbBEGZ1jQ)

[Spark常用算子内部原理剖析](https://mp.weixin.qq.com/s/HYD5zIoGZjZqne2zWwN1bQ)

[一文详解 Spark Shuffle](https://mp.weixin.qq.com/s/VdwOwmxmOpQC3NIaxqqbmw)

[Spark排序算法系列之ALS算法实现分析](https://mp.weixin.qq.com/s/3mXF5R-Cs8g7KKCdJhW1xg)

[多分类实现方式介绍和在Spark上实现多分类逻辑回归](https://mp.weixin.qq.com/s/cMuift2-svr0_i0qtUsG6A)

[Pandas vs Spark：获取指定列的N种方式](https://mp.weixin.qq.com/s/sFzXX5-UvrTCLteDIdE9EA)

[Spark中如何使用矩阵运算间接实现i2i](https://mp.weixin.qq.com/s/tX6jA2zDXwg567N0qnhqBQ)

[Spark中MLib机器学习测试](https://mp.weixin.qq.com/s/TZBjqADqdZRmxmP0y1K8hg)

[社区分享 | Spark 玩转 TensorFlow 2.0](https://mp.weixin.qq.com/s/wu4gxpzVRkXHw59gabv8bg)

####  1.2.10. <a name='-1'></a>源码

[Spark源码系列（十）spark源码解析大全](https://www.cnblogs.com/huanghanyu/p/12989067.html)

[Spark 常用算子源码](https://www.zhihu.com/tardis/sogou/art/365995989)

[spark源码系列—内核篇shuffle模块](https://mp.weixin.qq.com/s/PO5rvjbX65aR5MPOigsLaw)

[Master 启动之持久化引擎和选举代理](https://mp.weixin.qq.com/s/6f0rHzpyD3R26cKN-ICPUg)

[Spark 源码阅读：从 Spark-Submit 到 Driver 启动](https://mp.weixin.qq.com/s/8Qa4CKUc4LYnSNIid7iSlg)

[spark源码系列—sparkStreaming容错机制](https://mp.weixin.qq.com/s/eVi1DOsXDpL2D2FN9wlkBA)

[Spark如何协调完成整个Job的执行](https://mp.weixin.qq.com/s/lJd9-U7wUolt0yYr1KSJSw)

[通过扩展 Spark SQL ，打造自己的大数据分析引擎](https://mp.weixin.qq.com/s/LoOLdTl6_FWITynCEx9Bpg)

[从毛片打码到看Spark源码，我经历了什么](https://mp.weixin.qq.com/s/obj_nvNrmX90JHgqZE1IkA)

[scala的lazy关键字字节码分析](https://mp.weixin.qq.com/s/ugiqH1Veq_kDPLDZh-bF5Q)

[Spark 源码（5） - 从 SparkSubmit 开始看任务提交](https://mp.weixin.qq.com/s/PrpcfPjhLq6wRDEN-jgdAw)

[Spark 源码阅读：从 Spark-Submit 到 Driver 启动](https://mp.weixin.qq.com/s/Q30A31mqXXO9qQXpAkuplg)

[Spark 源码（6） - Master 通知 Worker 启动 Driver](https://mp.weixin.qq.com/s/ufe5o5VMJsUd9rS5-Y0sng)

[Spark 源码（7） - Driver 启动之 SparkContext 初始化](https://mp.weixin.qq.com/s/ygyJe_ZbWYIX2C-2jKMDIA)

###  1.3. <a name='-1'></a>应用与实战

####  1.3.1. <a name='json'></a>json

[spark的json数据的读取和保存](https://www.cnblogs.com/markecc121/p/11642194.html)

[json_tuple一定比 get_json_object更高效吗？（源码剖析）](https://mp.weixin.qq.com/s/7CRxI3mfFxYRpjJ6J2maaQ)

[使用spark解析json格式文件中的不同类型数据并操作](https://mp.weixin.qq.com/s/c1PyZQrxU4FvcM1bnjgHFA)

[Spark使用explode展开嵌套的JSON数据](https://mp.weixin.qq.com/s/C609LAMFr8yvSpCwd10vqA)

[用户访问session分析(十三) --json 数据格式](https://mp.weixin.qq.com/s/Cycxf6A8K8szlp5eWYuEpA)

[诡异 | Spark使用get_json_object函数](https://mp.weixin.qq.com/s/UG8tYeFIxuiZg-db9pB3xA)

[JSON 之 Jackson](https://mp.weixin.qq.com/s/3hK7sAp_y2Uh9Sb2e9ioHA)

####  1.3.2. <a name='streaming'></a>streaming

[Spark之Spark Streaming（初级篇）](https://mp.weixin.qq.com/s/qFcvfqgj7VFJO-GTnB_d7Q)

[流式数据处理在百度数据工厂的应用与实践](https://mp.weixin.qq.com/s/_tcUMJQaHzE6YqduhEshCA)

[一文详解 SparkStreaming 如何整合 Kafka ](https://mp.weixin.qq.com/s/9DMQRL20Ol8ZrbKC2NC2Lg)

[专为流式数据设计的另一种缓存：流式缓存技术解读](https://mp.weixin.qq.com/s/6vbCXVGrT0OLlq-lDA_Lmw)

[spark + kafka + sparkstreaming 搭建教程（Python）](https://mp.weixin.qq.com/s/bmanoNpmzsTNaP1yK_g7WA)

[spark源码系列—sparkStreaming状态操作](https://mp.weixin.qq.com/s/CB5sO3Q5vb3J76QtD25oNQ)

[spark源码系列—sparkStreaming数据接收模块](https://mp.weixin.qq.com/s/FGKE9TiUPl0vmcUeu1igAA)

[Spark Structured Streaming系列-执行过程](https://mp.weixin.qq.com/s/Fvbfday1By3Z__-VsGAY2Q)

[spark 开发中的那些事1-之编程模型](https://mp.weixin.qq.com/s/FJr8SDCYifjI6xSFXxLmhA)

[Spark 源码（4） - Worker 启动流程](https://mp.weixin.qq.com/s/GCDCai_Co9p90y2Rqg7QEw)

[还不收藏？Spark动态内存管理源码解析！](https://mp.weixin.qq.com/s/L6J66_EQNEbjr35qaifXRQ)

[spark源码系列—sparkStreaming窗口](https://mp.weixin.qq.com/s/Xn52biLV4Of7PjBqqBY2ag)

[spark源码系列—sparkStreaming中driver容错](https://mp.weixin.qq.com/s/XuJwRzO2tYKQTgQH8lujEg)

[spark源码系列—sparkStreaming背压](https://mp.weixin.qq.com/s/t3BnAG0Y3nlznwEYYxgJEQ)

####  1.3.3. <a name='-1'></a>其他

[优化一下 Spark 读 Kafka 的UI](https://mp.weixin.qq.com/s/4-Z4zSePTNHkov6eNJ3g2g)

[Spark On Zeppelin Yarn模式总结(CDH5.16.2)](https://mp.weixin.qq.com/s/c4h94_b_zIxcy90iHfYOuQ)

[如何用Spark 来实现一个通用大数据计算引擎？](https://mp.weixin.qq.com/s/CUEnmubFmnqIak6nzpEs7Q)

[Apache Spark 3.2 正式发布，新特性详解](https://mp.weixin.qq.com/s/hWFDUHG0CBU1iJbUDfwaGQ)

[Spark 作业资源调度源码解析 | 运维进阶](https://mp.weixin.qq.com/s/ifonPLQ0BkMCFq_6eIfh7w)

[大厂实践案例 0-1学习OLAP技术](https://mp.weixin.qq.com/s/Kc5p3-NkA8K9sfHv9Vh8Hg)

[想用Spark实现配置化的计算平台？先参考这篇硬核方案](https://mp.weixin.qq.com/s/lYECVCYdKsfcL64xhWEqPg)

[开源项目如何挣钱？ Spark 商业化公司创始人曝光心路历程](https://mp.weixin.qq.com/s/UFsKu-qKDYmQgHDFR4YhLw)

[CarbonData：大数据融合数仓新一代引擎](https://www.cnblogs.com/2020-zhy-jzoj/p/13165513.html)

[CarbonData是什么](https://www.huaweicloud.com/zhishi/mrs7.html)

[CarbonData实践(一)](https://www.jianshu.com/p/017d021cd846)

##  2. <a name='-viaYuting'></a>面试题 - via Yuting

![Spark-YutingDB](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/Spark-YutingDB.42t9nxeqco60.png)