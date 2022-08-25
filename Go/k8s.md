https://www.nowcoder.com/discuss/793723

https://www.nowcoder.com/discuss/992376

作者：、烟雨
链接：https://www.nowcoder.com/discuss/983775?type=all&order=recall&pos=&page=1&ncTraceId=&channel=-1&source_id=search_all_nctrack&gio_id=880723D33458CF9E307AFD749AE80DBC-1661420495637
来源：牛客网

简述ETCD及其特点？
简述ETCD适应的场景？
简述什么是Kubernetes？
简述Kubernetes和Docker的关系？
简述Kubernetes中什么是Minikube、Kubectl、Kubelet？
简述Kubernetes常见的部署方式？
简述Kubernetes如何实现集群管理？
简述Kubernetes的优势、适应场景及其特点？
简述Kubernetes的缺点或当前的不足之处？
简述Kubernetes相关基础概念？
简述Kubernetes集群相关组件？
简述Kubernetes RC的机制？
简述kube-proxy作用？
简述kube-proxy iptables原理？
简述kube-proxy ipvs原理？
简述kube-proxy ipvs和iptables的异同？
简述Kubernetes中什么是静态Pod？
简述Kubernetes中Pod可能位于的状态？
简述Kubernetes创建一个Pod的主要流程？
简述Kubernetes中Pod的重启策略？
简述Kubernetes中Pod的健康检查方式？
简述Kubernetes Pod的LivenessProbe探针的常见方式？
简述Kubernetes Pod的常见调度方式？
简述Kubernetes初始化容器（init container）？
简述Kubernetes deployment升级过程？
简述Kubernetes deployment升级策略？
简述Kubernetes DaemonSet类型的资源特性？
简述Kubernetes自动扩容机制？
简述Kubernetes Service类型？
简述Kubernetes Service分发后端的策略？
简述Kubernetes Headless Service？
简述Kubernetes外部如何访问集群内的服务？
简述Kubernetes ingress？
简述Kubernetes镜像的下载策略？
简述Kubernetes的负载均衡器？
简述Kubernetes各模块如何与API Server通信？
简述Kubernetes Scheduler作用及实现原理？
简述Kubernetes Scheduler使用哪两种算法将Pod绑定到worker节点？
简述Kubernetes kubelet的作用？
简述Kubernetes kubelet监控Worker节点资源是使用什么组件来实现的？
简述Kubernetes如何保证集群的安全性？
简述Kubernetes准入机制？
简述Kubernetes RBAC及其特点（优势）？
简述Kubernetes Secret作用？
简述Kubernetes Secret有哪些使用方式？
简述Kubernetes PodSecurityPolicy机制？
简述Kubernetes PodSecurityPolicy机制能实现哪些安全策略？
简述Kubernetes网络模型？
简述Kubernetes CNI模型？
简述Kubernetes网络策略？
简述Kubernetes网络策略原理？
简述Kubernetes中flannel的作用？
简述Kubernetes Calico网络组件实现原理？
简述Kubernetes共享存储的作用？
简述Kubernetes数据持久化的方式有哪些？
简述Kubernetes PV和PVC？
简述Kubernetes PV生命周期内的阶段？
简述Kubernetes所支持的存储供应模式？
简述Kubernetes CSI模型？
简述Kubernetes Worker节点加入集群的过程？
简述Kubernetes Pod如何实现对节点的资源控制？
简述Kubernetes Requests和Limits如何影响Pod的调度？
简述Kubernetes Metric Service？
简述Kubernetes中，如何使用EFK实现日志的统一管理？
简述Kubernetes如何进行优雅的节点关机维护？
简述Kubernetes集群联邦？
简述Helm及其优势？

1、 k8s是什么？请说出你的了解？

2、 K8s架构的组成是什么？

3、 容器和主机部署应用的区别是什么？

4、请你说一下kubenetes针对pod资源对象的健康监测机制？

5、 如何控制滚动更新过程？

6、K8s中镜像的下载策略是什么？

7、 image的状态有哪些？

8、 pod的重启策略是什么？

9、 Service这种资源对象的作用是什么？

10、版本回滚相关的命令？

11、 标签与标签选择器的作用是什么？

12、 常用的标签分类有哪些？

13、 有几种查看标签的方式？

14、 添加、修改、删除标签的命令？

15、 DaemonSet资源对象的特性？

16、 说说你对Job这种资源对象的了解？

17、描述一下pod的生命周期有哪些状态？

18、 创建一个pod的流程是什么？

19、 删除一个Pod会发生什么事情？

20、 K8s的Service是什么？

21、 k8s是怎么进行服务注册的？

22、 k8s集群外流量怎么访问Pod？

23、 k8s数据持久化的方式有哪些？

作者：已注销
链接：https://www.nowcoder.com/discuss/974775?type=all&order=recall&pos=&page=1&ncTraceId=&channel=-1&source_id=search_all_nctrack&gio_id=880723D33458CF9E307AFD749AE80DBC-1661420478693
来源：牛客网

k8s与Docker Swarm的区别
什么是k8s

k8s与Docker之间的关系

在主机部署应用于在容器上部署应用有哪些异同点

什么是容器编排

容器编排需要什么

k8s有哪些特性

k8s如何简化容器化部署

你知道k8s集群吗

什么是Google Container Engine

Heapster是什么

什么是Minikube

什么是kubectl

什么是kubelet

你理解k8s中的节点吗

k8s中的不同组件是什么

如何理解Kube-proxy?

简要介绍一下Kubernetes主节点的工作情况?

kube-apiserver和kube-scheduler的角色是什么?

简要介绍一下Kubernetes控制器管理器

ETCD是什么?

Kubernetes中有哪些不同类型的服务

对Kubernetes中的负载均衡器有何理解

什么是Ingress Network，它是如何工作的

你对云控制器管理器有什么理解

什么是容器资源监控

replica set和replication controller有什么不同

什么是Headless Service

在使用k8s时，可以采用的最佳安全措施是什么

什么是联合集群

作者：、烟雨
链接：https://www.nowcoder.com/discuss/981018?type=all&order=recall&pos=&page=1&ncTraceId=&channel=-1&source_id=search_all_nctrack&gio_id=880723D33458CF9E307AFD749AE80DBC-1661420495637
来源：牛客网

1、 k8s是什么？请说出你的了解？
2、 K8s架构的组成是什么？

3、 容器和主机部署应用的区别是什么？

4、请你说一下kubenetes针对pod资源对象的健康监测机制？
5、 如何控制滚动更新过程？

6、K8s中镜像的下载策略是什么？

7、 image的状态有哪些？
8、 pod的重启策略是什么？
9、 Service这种资源对象的作用是什么？
10、版本回滚相关的命令？
11、 标签与标签选择器的作用是什么？
12、 常用的标签分类有哪些？
13、 有几种查看标签的方式？
14、 添加、修改、删除标签的命令？
15、 DaemonSet资源对象的特性？
16、 说说你对Job这种资源对象的了解？

17、描述一下pod的生命周期有哪些状态？
18、 创建一个pod的流程是什么？
19、 删除一个Pod会发生什么事情？

20、 K8s的Service是什么？
21、 k8s是怎么进行服务注册的？
22、 k8s集群外流量怎么访问Pod？
23、 k8s数据持久化的方式有哪些？