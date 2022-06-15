<!-- vscode-markdown-toc -->
* 1. [scala](#scala)
	* 1.1. [面试](#)
	* 1.2. [类](#-1)
	* 1.3. [对象](#-1)
	* 1.4. [函数](#-1)
	* 1.5. [actor](#actor)
	* 1.6. [并发](#-1)
	* 1.7. [trait](#trait)
	* 1.8. [lazy](#lazy)
* 2. [隐式](#-1)
* 3. [泛型](#-1)
	* 3.1. [模式匹配](#-1)
	* 3.2. [语法糖](#-1)
	* 3.3. [python的对比](#python)
	* 3.4. [变量](#-1)
	* 3.5. [集合](#-1)
	* 3.6. [其他](#-1)
* 4. [java面试](#java)
	* 4.1. [线程](#-1)
	* 4.2. [hash](#hash)
	* 4.3. [序列化](#-1)
	* 4.4. [其他](#-1)
	* 4.5. [JVM面试](#JVM)
* 5. [环境搭建](#-1)
	* 5.1. [maven](#maven)
		* 5.1.1. [面试](#-1)
		* 5.1.2. [实战](#-1)
	* 5.2. [sbt](#sbt)
	* 5.3. [Makefile](#Makefile)
	* 5.4. [docker](#docker)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->

##  1. <a name='scala'></a>scala
 
###  1.1. <a name=''></a>面试

[高频面试题（三）[Scala/Spark]](https://mp.weixin.qq.com/s/ROqp3l8mSe-JPaf9wLbi-g)

[scala面试题总结](https://www.cnblogs.com/Gxiaobai/p/10460336.html)

[大数据分析工程师面试集锦2-Scala](https://mp.weixin.qq.com/s/v-gxDstXShU9uwRNJsdmyQ)

[scala面试题总结](https://mp.weixin.qq.com/s/viq_U5E0lAShLlCE2C6Ucg)

[大数据笔试真题集锦——第十七章Scala面试题](https://m.sohu.com/a/393737408_100123073)

[大数据分析工程师面试集锦2-Scala](https://mp.weixin.qq.com/s/11vry9estQtQmQnRnbTksQ)

[Scala面试题总结](https://mp.weixin.qq.com/s/5magYc5T1gQWqqH0kTouuw)

[大数据面试真题，来试试你有几斤几两（11）——Scala](https://mp.weixin.qq.com/s/bqXZQtkjxT3RBi3JcOSRHg)

[Scala精华之处就在这里，拿去，面试也不怕](https://mp.weixin.qq.com/s/DHN95LrcvUNLBGFS1QSb_A)

[字节Scala面试题 --- None跟null的区别](https://mp.weixin.qq.com/s/e6d-GjYjhWWCJ-hHs3eDvA)

[面试题|Scala-unapply与apply方法区别和应用场景](https://mp.weixin.qq.com/s/h4QXvP8J4G-cn8X7YVLxNw)

[Scala编程语言面试基础篇1](https://mp.weixin.qq.com/s/istAqWocUpuQqJvFVG8pDQ)

[scala常见笔试题(囊括了scala的精髓)](https://www.cnblogs.com/szjblog/p/11272325.html)

[scala常见笔试题(囊括了scala的精髓)](https://mp.weixin.qq.com/s/vxzcaI1eEmW4rbh1JeuyeA)

###  1.2. <a name='-1'></a>类

[scala的抽象类型](https://mp.weixin.qq.com/s/6GF8Dd253LaSeG5nhxeQjg)

[scala入门(六) 类和属性](https://mp.weixin.qq.com/s/8wSdnSinlN5ZRPKHEk6-jA)

[大数据之脚踏实地学19--Scala中类的使用](https://mp.weixin.qq.com/s/cUZOvEk0aj0prGxJH4RqAQ)

[Python和Scala的类继承关系分析](https://mp.weixin.qq.com/s/PM1LNU1EAQgbKXcAhS2EsA)

[scala别名与自身类型](https://www.jianshu.com/p/587f85219730)

###  1.3. <a name='-1'></a>对象

[java.lang包介绍以及包下类的使用](https://mp.weixin.qq.com/s/fPrNyg_9u4qw0yVO5Ur-Yw)

[Scala 入门-包对象（package object）](https://codeleading.com/article/19772745360/)

[scala包对象的使用测试和字节码分析包对象到底是什么](https://mp.weixin.qq.com/s/gdBs0Ts7fbFDJ7k7LIwwRw)

[Python和Scala的类和对象(二)](https://mp.weixin.qq.com/s/IAwQErf40R8a2IAxlPLXtw)

[Scala入门--单例对象object](https://mp.weixin.qq.com/s/yq65lrI-hnXzmPsZkSyXng)

[scala语言学习------03 面向对象](https://mp.weixin.qq.com/s/jf6JipZa52kiJxjOkQ3JYw)

[Scala中Object和Class区别(伴生类和伴生对象)](https://blog.51cto.com/u_12902538/3727118)

[scala 伴生对象有什么作用](https://blog.51cto.com/u_15295052/3720433)

[Scala快速入门-6-单例对象及伴生对象](https://mp.weixin.qq.com/s/0ZWvhmMjmq6HJ0y58-JHhQ)

[[Scala] 伴生类和伴生对象](https://mp.weixin.qq.com/s/dIbqFqn9px5fc7H8QDLMcg)

[scala伴生类和伴生对象的理解](https://mp.weixin.qq.com/s/tUnBiX9mt52Txv645FF2uQ)

[简单定义Python和Scala的类和对象](https://mp.weixin.qq.com/s/uBSYm4YP0GsKScTzIDsKnA)

[Scala 使用 import](https://m.w3cschool.cn/scaladevelopmentguide/eui61jbr.html)

###  1.4. <a name='-1'></a>函数

[Python和Scala的一等函数](https://mp.weixin.qq.com/s/x0ZCwdmmqzv72b_6VbFJPQ)

[Scala快速入门-9-高阶函数](https://mp.weixin.qq.com/s/Q-2VNqKvRiecGEAjQ_xm2Q)

[Scala函数式编程](https://mp.weixin.qq.com/s/8S0l7TgfOKMWdbk5bXfrLw)

[Scala函数](https://mp.weixin.qq.com/s/10eZSs3Trv9WyNfyL1k2jA)

[Scala系列 （二）Scala的独有特性提高开发效率----学会之后玩转函数式与OOP！！](https://mp.weixin.qq.com/s/2ZP-xAoubqNV_KGOccAzKA)

[scala高阶函数(一)](https://mp.weixin.qq.com/s/Qmq6WzsbT47Q_0mziwweYA)

[Scala快速入门-2-控制结构与函数](https://mp.weixin.qq.com/s/VMrcd9OD9p4ETj8CSHzh-A)

[Scala专题系列（二）：Scala控制结构](https://mp.weixin.qq.com/s/KJGTc0uqSGAujNC5stOlog)

###  1.5. <a name='actor'></a>actor

[和我一起学习scala （十六）：Actor入门](https://mp.weixin.qq.com/s/Sr61w6EPhTKETKdFd0AIjg)

[Scala Actors迁移指南](https://mp.weixin.qq.com/s/XoJ0iIdTQVI9RT6yGv4myA)

[Scala学习之路 （十）Scala的Actor](https://www.cnblogs.com/qingyunzong/p/8886061.html)

[Scala Actor并发编程模型详解](https://mp.weixin.qq.com/s/yfse2QAz5NDEtrNC0wJP-A)

[《Scala 语言》Scala 中的 Actor 编程](https://mp.weixin.qq.com/s/kzJ2KvPtuFTGFTG2-YmpRA)

###  1.6. <a name='-1'></a>并发

[Scala异步并发编程库](https://mp.weixin.qq.com/s/SsSpIE0vmqLhpU0lNSWwqg)

[Scala之并发编程](https://mp.weixin.qq.com/s/o4b4TuRQ0mGfzBwJ-r7Vqw)

[Scala并发编程](https://mp.weixin.qq.com/s/1IDJctw8nfZ_UKL1sgwshg)

###  1.7. <a name='trait'></a>trait

[一文解说 Scala Trait 所有用法](https://www.6aiq.com/article/1534060892937)

[类、对象、继承和trait](https://www.cnblogs.com/bigdatalearnshare/p/14104916.html)

[和我一起学习scala （十）：面向对象之Trait](https://mp.weixin.qq.com/s/F_e9aIr1PCEEi8GB7pf42Q)

[scala 面向对象 trait详解（九）](https://www.cnblogs.com/linxizhifeng/p/9262697.html)

[Scala的Class、Object、Trait](https://mp.weixin.qq.com/s/f-qGgFX50DvNzEQzwTzGPA)

###  1.8. <a name='lazy'></a>lazy

[Kotlin、Swift、Scala 的延迟求值](https://mp.weixin.qq.com/s/kZERAD4-ydroAwcqy5QsjA)

[scala lazy引发的疑问](https://www.jianshu.com/p/8ec53b97b5a6)

[Scala学习之路 （五）Scala的关键字Lazy](https://www.cnblogs.com/qingyunzong/p/8869761.html)

[和我一起学习scala （四）：函数入门之过程 lazy值和异常](https://mp.weixin.qq.com/s/-CnP24ZOolY5c8z4x6ytsA)

[scala的lazy关键字字节码分析](https://mp.weixin.qq.com/s/ugiqH1Veq_kDPLDZh-bF5Q)

##  2. <a name='-1'></a>隐式

[说下scala中的关键字 implicit](https://mp.weixin.qq.com/s/e4bBJVx_jcVZwTCpt_joOQ)

[Scala之隐式转换](https://mp.weixin.qq.com/s/2CfJHm-g4FGpg-fWxvb_FA)

[《Scala 语言》Scala 中的柯里化和隐式转换](https://mp.weixin.qq.com/s/-bHwDaEQRYWYi7uov8UEWA)

[scala中的隐式转换和隐式参数](https://mp.weixin.qq.com/s/DgX-iju_yw_bwj3aiYj9cQ)

[深入浅出Scala的隐式转换实践](https://mp.weixin.qq.com/s/Fjibqf4eFcAyNfclLz2_aQ)

[和我一起学习scala （十五）：隐式转换与隐式参数](https://mp.weixin.qq.com/s/N4UiUejH-0SI7K5biVkHZg)

[scala的隐式转换](https://mp.weixin.qq.com/s/noeqAh_mQ_KacoWU-A2rmQ)

[day09-scala的泛型、上界与下界、隐式转换](https://mp.weixin.qq.com/s/Pv95Xd87g1ZnWNtLbjB3Vw)

[Scala隐式转换的问题分析](https://mp.weixin.qq.com/s/qXBEKFMTzJyi9qf3l2BMBA)

[Scala隐式转换](https://mp.weixin.qq.com/s/Yb-lFw7shi9eHCvi31TikQ)

[从示例逐渐理解 Scala 隐式转换](https://mp.weixin.qq.com/s/X0hQl46H0f0oe3tTAqdU-w)

[Scala隐式转换](https://mp.weixin.qq.com/s/VV41P796GfJAuVxC_s_sHg)

##  3. <a name='-1'></a>泛型

[Spark(三) scala復雜語法之泛型](https://www.jianshu.com/p/e3e371b0d7ca)

[Java/Scala 泛型快速入门教程](https://www.zhihu.com/tardis/sogou/art/106724236)

###  3.1. <a name='-1'></a>模式匹配

[day08-scala的集合、模式匹配、样本类](https://mp.weixin.qq.com/s/nXKEuvGi7Qgewki2He967Q)

[和我一起学习scala （十三）：模式匹配](https://mp.weixin.qq.com/s/qD1TOT0UKxlAaEbr-wjJJw)

[【必会】Scala之模式匹配和样例类](https://mp.weixin.qq.com/s/T-I153sLEfBPkUymPhIHWg)

[12.scala的模式匹配](https://mp.weixin.qq.com/s/Wzn9ko9ozLedHWU-ZDXbWw)

[Spark零基础入门（7）：Scala模式匹配（下）](https://mp.weixin.qq.com/s/cB3Qea3_wj9OhEd9PL794w)

###  3.2. <a name='-1'></a>语法糖

[scala中:: , +:, :+, :::, +++的区别](https://www.jianshu.com/p/70ffc16f2ff7)

[一文搞懂scala中奇怪的特殊符号应用场景](https://mp.weixin.qq.com/s/JbJLqfqn8dsG6dj89oUQkA)

[Scala学习笔记(五) 抽象类以及类中的一些语法糖](https://mp.weixin.qq.com/s/5ADSeIaOmeYQCm8AlMFESg)

[【雷火UX数据挖掘】Scala的语法糖](https://mp.weixin.qq.com/s/bpfIB3dUSdjzIQIzYC5lMg)

[作为Scala语法糖的设计模式](https://mp.weixin.qq.com/s/K8snWqaOWQg_mTUKrMBvYA)

[scala语法糖(一)](https://mp.weixin.qq.com/s/xkZAePDP0vbG_cJ3TFPgbQ)

###  3.3. <a name='python'></a>python的对比

[Python和Scala里的闭包](https://mp.weixin.qq.com/s/40nnm6-_iUFr_cHeuJbQTg)

[Python和Scala的操作符](https://mp.weixin.qq.com/s/46tH3R4MsUIHwmfjNnHdVw)

[曾经以为Python中的List用法足够灵活，直至我遇到了Scala…](https://mp.weixin.qq.com/s/7ErApK6Q9_6QBF5OTyFbHg)

[Python和Scala的序列](https://mp.weixin.qq.com/s/Q1fhzQA3FFotPhHDeJp5rQ)

###  3.4. <a name='-1'></a>变量

[scala入门(二) 变量声明](https://mp.weixin.qq.com/s/cnd-oDgpEm6czgZvrcdvfA)

[从定义变量小窥Python和Scala的设计理念不同](https://mp.weixin.qq.com/s/36jkyN5rNZRsoIaBjeM2wQ)

[Python和Scala的定义变量](https://mp.weixin.qq.com/s/nO6AeW0cZXkxMQbSLDaWug)

[Python和Scala的变量作用域](https://mp.weixin.qq.com/s/FTStXFeEGhTrQEeV5EeNvw)

[Python和Scala有什么区别？有哪些区别？](http://www.srcmini.com/69692.html)

###  3.5. <a name='-1'></a>集合

[Python和Scala的集合和映射](https://mp.weixin.qq.com/s/7T_GJEw8xh06bdXBcQDYQA)

[Scala与Java集合互转摘要](https://mp.weixin.qq.com/s/A3iku4xkSVmsn_YCIfoBUA)

[scala语言---集合](https://mp.weixin.qq.com/s/G11YZqLMjrUZ5JGtvT4zSw)

[Scala性能比较之集合](https://mp.weixin.qq.com/s/KUfwpVi5zHwLutjmE9zmFg)

[Scala集合之Array(进阶)](https://mp.weixin.qq.com/s/HgtSBaWVru8sujFUjsprew)

[Scala HashSet用法详解](http://www.srcmini.com/34666.html)

###  3.6. <a name='-1'></a>其他

[Java Vs Scala](https://mp.weixin.qq.com/s/_xkKsCJoZx6kMLtRpFUpzg)

[说说选择Scala的几点缘由](https://mp.weixin.qq.com/s/3E52UWgHxijRDge7vfU8Og)

[Scala3.0最新最全特性一览！](https://mp.weixin.qq.com/s/9BFHaHMJijiVKLvATEMz0Q)

[如何在Vim上把Scala玩的飞起？](https://mp.weixin.qq.com/s/A_Nc31OkQi3waTOYASIFXQ)

[scala-从入门到精通](https://mp.weixin.qq.com/s/AEpjMkvaiS9NtaxMgHcYMQ)

[Scala教程之:深入理解协变和逆变](https://mp.weixin.qq.com/s/E39gXL6bNSeqn1z-is0I2A)

[动态代理与Scala反射/Java反射在Java、Scala、Kotlin中的使用](https://mp.weixin.qq.com/s/E9LMJFuypfbi7yXe6NOG-g)

[Spark实战，第一部分：使用Scala语言开发Spark应用程序](https://mp.weixin.qq.com/s/eL_lUKUqvbJMycMPNuOQ8A)

[Scala学习一](https://mp.weixin.qq.com/s/hhWxGBaA9cs2doSFaXNglQ)

[Scala学习笔记（三）](https://mp.weixin.qq.com/s/LKZpOR7GfxVZZJHUt7RXag)

[Tour of Scala分章要点笔记（上）](https://mp.weixin.qq.com/s/OmW70nC8BPBg3lAY7H70Pw)

[Scala教程之:Enumeration](https://mp.weixin.qq.com/s/rZxRnUdyArAwyeF1hKK6ag)

[Scala笔记(1) - Debug之旅的开始](https://mp.weixin.qq.com/s/9fdU-Na4DgI9vxbLblYFeQ)

[Scala笔记(2) - 玄学Debug](https://mp.weixin.qq.com/s/NRbCGaJmmn3bTZti1L0BWA)

[想学习Scala，看这篇就够了](https://mp.weixin.qq.com/s/zhzWxvZd0R_18msOz9Kr-Q)

[深入浅出讲Scala](https://mp.weixin.qq.com/s/zwlhLNsuc2r1WVVoRV-aOw)

[Scala 中为什么不建议用return](https://www.jianshu.com/p/45efdbce8b99)

[CSV等类型文件与Map互相转换（Scala）](https://mp.weixin.qq.com/s/LOi5GvZMfOR28hihmnbzZQ)

[Scala教程之:PartialFunction](https://mp.weixin.qq.com/s/luvHYuwTOoWlQT-MhOqPtA)

[Scala Future实践踩坑了~](https://mp.weixin.qq.com/s/m-l7RxAprWYSkdqEkIKKAQ)

##  4. <a name='java'></a>java面试

###  4.1. <a name='-1'></a>线程

[Java线程面试题合集](https://mp.weixin.qq.com/s/qP76G1CE1dTMGg-3tPPVIg)

[12 道常见的 Java 多线程 面试题](https://mp.weixin.qq.com/s/zbHw_-qbYNsu34LAF2_z4Q)

[106道Java并发和多线程基础面试题大集合（2w字），这波面试稳了~](https://mp.weixin.qq.com/s/UYNhVlya-99o6MQ0T_9gGA)

[99 道 Java 多线程面试题](https://mp.weixin.qq.com/s/r6fqTgpzT19u2wN67lqVdA)

[99 道 Java 多线程面试题，看完我跪了！](https://mp.weixin.qq.com/s/dRqLZG7eev87hda9ohlJrA)

[Java并发多线程高频面试题](https://mp.weixin.qq.com/s/RJfyRw4DUjQBLzF_vZA9rQ)

[Java多线程面试题（下）](https://mp.weixin.qq.com/s/85nXtx8T8noq3YOmjsiTBA)

[JAVA宝典-面试题-多线程篇(含答案)](https://mp.weixin.qq.com/s/xsjQa8RDlGHnSRZvTwdnPA)

[史上最全 Java 多线程面试题及答案](https://mp.weixin.qq.com/s/0CI9od4DIxRrmOGFJw0SuQ)

###  4.2. <a name='hash'></a>hash

[线程安全原理简析及HashMap多线程并发5种场景异常分析](https://mp.weixin.qq.com/s/u_YT0iQMTFn13AVA2nqjNg)

[面试官：说说hashCode() 和 equals() 之间的关系？](https://mp.weixin.qq.com/s/SaI_h0CdAPN0DTiUEaGORg)

[面试：equals方法和hashCode方法](https://mp.weixin.qq.com/s/rGw8WOKxGgtzNuihiWYgHA)

[面试题分享：说说hashCode() 和 equals() 之间的关系？](https://mp.weixin.qq.com/s/Ud-3rNXJNd4ID5r1rIiWxQ)

[面试宝典:数据结构-HashSet](https://mp.weixin.qq.com/s/P3iUkJo4w2d4alcG1z4Bbg)

[面试题：HashSet是如何保证元素不重复的](https://mp.weixin.qq.com/s/A_RxNIhtcontoEzhWYmTmw)

###  4.3. <a name='-1'></a>序列化

[知乎面试官：为什么不建议使用 Java 自带的序列化？](https://mp.weixin.qq.com/s/ZMaohEi8t5ByArAqQJJdFA)

[【面试题】什么是java 序列化，如何实现java 序列化？](https://mp.weixin.qq.com/s/VhDJcEN4sO-3FPcEJjOBtg)

[Java序列化面试题-整理](https://www.cnblogs.com/huallx0510/p/13049984.html)

[Java序列化面试题：就这？就这呀？](https://mp.weixin.qq.com/s/NZ1Sm8KzGyKOjrjHDj5jCw)

[【大厂面试题】java 序列化](https://mp.weixin.qq.com/s/dBiJAl6PsBWWXdCL-FkyTg)

[面试必问基础-Java对象序列化](https://mp.weixin.qq.com/s/i1CCrsTQlsQWtQGXkh9Nyg)

[一语点破Java中的静态static关键字](https://mp.weixin.qq.com/s/rfW_rT6P2_XmZ1GT0KpM1A)

[Java序列化技术](https://mp.weixin.qq.com/s/xnnWTPAawkVNHsP82zblrw)

[你不知道的java对象序列化的秘密](https://mp.weixin.qq.com/s/kl6MysvvbXB9RHdT_did3A)

[Java 序列化的这三个坑千万要小心](https://mp.weixin.qq.com/s/1yhUmoUqOlTCIQwkzOIHJw)

[几种Java常用序列化框架的选型与对比](https://mp.weixin.qq.com/s/DiphH8bvR8TtLyQmnTkpuw)

###  4.4. <a name='-1'></a>其他

[2019最新整理JAVA面试题附答案](https://mp.weixin.qq.com/s/FuPCxlDx6x7JUfSpeX9Q7g)

[Java程序员必会的工具库，让你的代码量减少90%！](https://mp.weixin.qq.com/s/99lYUZfT78h4ovYP8TmKTg)

###  4.5. <a name='JVM'></a>JVM面试

[JVM 基础面试题总结](https://mp.weixin.qq.com/s/G9uW6vRxQE4JvbprS4kmDA)

[《八股文》19道JVM面试题](https://mp.weixin.qq.com/s/NJG4v-WhBaXn1f1AUxGMhw)

[五位卷王 | 总结的十道 JVM 面试真题！（建议收藏）](https://mp.weixin.qq.com/s/osAIJrhC6zlRG9ibv-ViZQ)

[《不看后悔》超赞！来一份常见 JVM 面试题+“答案”！](https://mp.weixin.qq.com/s/7ZaLMI1LYHBEL7Q2gOrGKQ)

[大厂JVM面试真题](https://mp.weixin.qq.com/s/LpOXBcKdmRrd24i1lPPkBg)

[皮皮爆肝 32 道高频 JVM 面试题（附答案）](https://mp.weixin.qq.com/s/lSIXF3ntUYg0UswFHrldjg)

[常见JVM面试题及答案整理](https://mp.weixin.qq.com/s/IoMcMGNFGHglNszK_Rm_6w)

[【每日3分钟技术干货 | 面试题+答案 | jvm篇(一)】](https://mp.weixin.qq.com/s/mNBiXgMmtJBOcjFeck_D1A)

[【每日3分钟技术干货 | 面试题+答案 | jvm篇(二)】](https://mp.weixin.qq.com/s/8GzEoYt716vf2dUicldJ0w)

[JVM面试题，看这一篇就够了](https://mp.weixin.qq.com/s/mfjqCjqjYaUJAJ7akhYk2Q)

[JVM面试题总结及答案](https://mp.weixin.qq.com/s/SsFSg8tulNP0W9CkPJY8nA)

[贡献一道超高套路JVM面试题（二）](https://mp.weixin.qq.com/s/7iLhMmcSjZQksDfTD6PnZg)

[2020年最新78道JVM面试题总结（含答案解析和思维导图）](https://www.cnblogs.com/zhuifeng523/p/13172080.html)

[《不看后悔》超赞！来一份常见 JVM 面试题+“答案”！](https://mp.weixin.qq.com/s/QABF-UNPGgmooEm6Kct3pw)

[【JVM面试题】Java中的静态方法为什么不能调用非静态方法](https://mp.weixin.qq.com/s/4kdqo4jyBSmkWNHPG37RxQ)

[JVM 面试题解答(40道全)](https://mp.weixin.qq.com/s/XKZepljPtp9NFkh0oLesAg)

[JVM面试题汇总](https://mp.weixin.qq.com/s/I-mO3zsaNaXFRQIMlAFSSw)

[10个经典又容易被人忽视的JVM面试题](https://mp.weixin.qq.com/s/NvR2pXh6Euhtuln1OIfdpA)

[2021大厂90%会被问到的JVM面试题](https://mp.weixin.qq.com/s/VnUsUHj30uBAxleU0RiLhQ)

[这波 JVM 面试题解答，绝对给你面试加分](https://mp.weixin.qq.com/s/Wv9FDXs7D_pyB159z-1KBA)

[面试系列二：精选大数据面试真题JVM专项-附答案详细解析](https://mp.weixin.qq.com/s/0auWlqdL8dK1Yo1uwHzjmQ)

[总结的JVM面试题](https://mp.weixin.qq.com/s/t3whubIsUV5Xg08u0-thrg)

##  5. <a name='-1'></a>环境搭建

###  5.1. <a name='maven'></a>maven

####  5.1.1. <a name='-1'></a>面试

[Maven详解【面试+工作】](https://mp.weixin.qq.com/s/WL5-CuDJs8iyA_-phP47fQ)

[面试问你：maven的生命周期你知道吗？](https://mp.weixin.qq.com/s/tM47bGP6mVHKm_YoDzi7KQ)

[Maven面试题及答案（18题）](https://mp.weixin.qq.com/s/JK58sISMw-sHcILInTc35g)

[面试：Maven 的这 7 个问题你思考过没有？](https://mp.weixin.qq.com/s/Etb5rMU4C-2Yh3j0mHgLKA)

[Maven笔记之面试题整理](https://mp.weixin.qq.com/s/2Mcwk3KUx8Vg80qDYFwOIw)

[Maven从入门到精通](https://mp.weixin.qq.com/s/5otPSpHK69YiolUTx9y2bw)

[Maven入门教程(二）](https://mp.weixin.qq.com/s/gN8VJhcrpFrs9CcMaktVrQ)

####  5.1.2. <a name='-1'></a>实战

[大佬对Maven进行深度讲解：什么是Maven？POM.XML如何解读？](https://mp.weixin.qq.com/s/xpe9cLw7uvsWt5TRVv3PNA)

[各种主流SCM及maven相关插件](https://cloud.tencent.com/developer/article/1415961)

[Maven自动部署-SCM](http://www.360doc6.net/wxarticlenew/825347219.html)

[Maven架构选型：单模块还是多模块？网友：多此一举~](https://mp.weixin.qq.com/s/IySquKLJL5AZ9Vk7fJYHwQ)

[面试官：说说 Maven 的依赖管理！](https://mp.weixin.qq.com/s/tyPZeXHR3u2aavBVisp-VQ)

[Apache Flink利用Maven对Scala与Java进行混编](https://mp.weixin.qq.com/s/4R80PSOTk1JbX0NiE7LMBg)

###  5.2. <a name='sbt'></a>sbt

[SBT和Scala之间不得不说的那些事儿](https://mp.weixin.qq.com/s/SA8CgeZUIPguKE0ZHhz-Lg)

[内网环境idea使用sbt构建scala项目](https://mp.weixin.qq.com/s/2eDAlYa53maXLPn0JlS_Bw)

[【读书笔记】Testing in scala （一）sbt工具使用](https://mp.weixin.qq.com/s/_t-LNJ_2eHMl3KlwI5OEWQ)

###  5.3. <a name='Makefile'></a>Makefile

[make命令和makefile文件](https://mp.weixin.qq.com/s/zfRwUW_SdwzgLxkDPtrE_A)

[Makefile 使用（1）](https://mp.weixin.qq.com/s/CGAWqpX599tqOS-6ZYNjww)

[Makefile 使用（2）：基础知识整理](https://mp.weixin.qq.com/s/AOQZ-Oad-w0PJnJFVAsb1w)

###  5.4. <a name='docker'></a>docker

[史上最详细的docker学习手册，请查收！](https://mp.weixin.qq.com/s/yxA5Hf5UkwJ6seiYaqZDQg)

[教你用 Docker 搭建网站](https://mp.weixin.qq.com/s/NcbyTWGYh021nWJkTliydA)

[Docker 从入门到精通（建议收藏的教程）](https://mp.weixin.qq.com/s/q00XXglhGggrfeFHTuJ9oQ)

[Docker最常用的命令总结](https://mp.weixin.qq.com/s/VLBFbqhiIIWzD864Xp4WwA)
