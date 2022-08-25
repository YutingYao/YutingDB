https://www.nowcoder.com/discuss/949020

https://www.nowcoder.com/discuss/1027844

https://www.nowcoder.com/discuss/1017773

https://www.nowcoder.com/discuss/460344

docker和虚拟机

docker的文件管理

）k8s容器技术

1 k8s在实际项目中用过吗（答：没用过，只了解理论。我脑抽说自己用过docker）

2 讲一下docker的底层核心技术有哪些（我***了，支吾了一会儿认怂了）

作者：一定会顺利的
链接：https://www.nowcoder.com/discuss/1020873?type=post&order=recall&pos=&page=1&ncTraceId=&channel=-1&source_id=search_post_nctrack&gio_id=880723D33458CF9E307AFD749AE80DBC-1661419894299
来源：牛客网

5、看你用过docker，说一下docker如何查看日志

A：docker log（面试官：我记得是docker logs吧？我：啊，我记不得了，可能记错了；面试官：我也不记得了，也可能是我记错了。。。这里我真的瞬间不紧张了，哈哈哈哈）

纠正：docker logs命令（事实证明，面试官没错。）

6、***用什么bash进入docker容器？

A：进入docker容器是吗，docker exec -it 容器id /bin/bash

（这里当时又没太听清，不好意思问了，直接回答了完整的进入容器的命令，看面试官表情状态，应该没错）

7、docker端口映射用的参数

A：-p

docker怎么做的压测？


面试官：会的挺多，主要问问你简历里这两个项目（提到了一个springcloud的项目和一个实时计算数仓的项目）。你对spring cloud相关技术组件有多少了解呢，你这个项目用到了哪些？

主要用的是阿里生态的spring cloud，nacos统一管理配置中心、让每个微服务能够自己注册并发现配置中心，基于spring boot来写每个微服务模块，还有统一验证网关gateway、在里面控制配置负载均衡和各类验证信息，不同微服务模块之间通过feign来调用，此外还有elastic search和通过docker来部署服务器项目等。

4. 你提到了你是用docker来部署这个项目，那你对docker容器了解有多少呢？（说实话都没复习这块）

自己项目主要是对每个微服务模块来部署，每个模块根据具体功能来分配容器数量。然后谈了一下docker里面container相关的八股文。

1. 了解k8s吗？

一个容器编排引擎，对容器化应用进行部署管理的一个框架（？）。有相关了解，但是没有具体部署和使用过。（没钱给我这样造次。。）


问docker的基本知识点

问docker的基本知识点

Kubernetes的集群部署（简历有写）



14.netty为什么性能高？

15.项目里面netty是怎么用的？

16.springcloud的用过嘛？没用过，说用过docker，后续就问了docker的问题

17.docker从镜像开始到创建容器，流程说一下\



Docker

2.4为什么要用Docker ?

3.2 容器与虚拟机总结

3.3 容器与虚拟机两者是可以共存的

4.2 容器(Container):镜像运行时的实体

4.3 仓库(Repository):集中存放镜像文件的地方

问常见的Linux命令、对云计算、虚拟化、KVM和Docker的理解。（简历上写了这些）

docker怎么处理僵尸进程

项目中对Docker、k8s怎么使用的，dockerfile写过吗

对kubernetes，docker等云原生技术有了解和使用基础；