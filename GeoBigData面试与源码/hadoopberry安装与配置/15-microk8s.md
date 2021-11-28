<!-- vscode-markdown-toc -->
* 1. [某些操作](#)
	* 1.1. [关闭Swap内存交换空间](#Swap)
	* 1.2. [开启ipv4转发（可跳过）](#ipv4)
	* 1.3. [关闭防火墙（可跳过）](#-1)
* 2. [外网操作](#-1)
	* 2.1. [安装 microk8s](#microk8s)
	* 2.2. [Ubuntu 20.04上的副本集和扩展](#Ubuntu20.04)
	* 2.3. [在Ubuntu 20.04上公开应用程序](#Ubuntu20.04-1)
	* 2.4. [Microk8s的高可用性](#Microk8s)
* 3. [国产版本](#-1)
	* 3.1. [install microk8s](#installmicrok8s)
	* 3.2. [将当前用户加入 microk8s 创建的群组](#microk8s-1)
	* 3.3. [安装 docker](#docker)
	* 3.4. [修改pod的sandbox](#podsandbox)
	* 3.5. [设置别名（可跳过）](#-1)
	* 3.6. [config配置（可跳过）](#config)
	* 3.7. [如果要修改 hostname（可跳过）](#hostname)
	* 3.8. [启用k8s组件](#k8s)
	* 3.9. [microk8s.status 和 microk8s.inspect 检查各个组件的状态](#microk8s.statusmicrok8s.inspect)
	* 3.10. [检查 pods](#pods)
	* 3.11. [使用代理](#-1)
	* 3.12. [手动下载镜像操作流程](#-1)
	* 3.13. [查看 Dashboard](#Dashboard)
	* 3.14. [使用令牌登录：获取token](#token)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->

##  1. <a name=''></a>某些操作

###  1.1. <a name='Swap'></a>关闭Swap内存交换空间

`Swap内存`通俗的来说就是机器`硬盘`上的预留分配的内存分区

机器上的`物理内存`不够用了就会在`硬盘`上申请一块区域给运行的程序使用

=> 所以说在硬盘上的`Swap内存分区`的性能肯定是比不上`物理内存`的，

Swap内存分区存在的作用：就是为了让程序运行时内存不够用而`不至于崩溃` => 现在的Linux系统默认会分配Swap内存分区提高系统稳定性。

树莓派装完系统可以先查看内存占用，会看到Swap分区有1G的空间。

```sh
free -m
```

> 为什么要关闭Swap?

错误大部分情况就是容器运行的时候内存用完了有内存溢出导致某些进程被kill从而导致服务不可用，但是容器表面上是健康的。

所以在kubelet（K8s控制命令台）在1.8版本后强制要求必须关闭Swap。

> 怎么关闭Swap?

简单粗暴 给/etc/fstab内容加上注释

```sh
sed -ri 's/.swap./#&/' /etc/fstab
```

- `sed命令`是一个可以按照脚本处理编辑文本的命令，sed全名叫stream editor

- `-r` 支持扩展表达式,表示要`启用正则`处理文字，非常强大

- `-i` 直接修改源文件内容，使用-i后不会再在终端输出文件内容

- `'s/.swap./#&/'` :
  - s后面的符号`斜杠`就是指定的分割符号，分割符号这里可以自定义，
  - 使用三个分割符号，第一个指定符号，后面两个分割出`搜索`的内容和`替换`的内容，
  - 搜索`.swap.`，`点号代表一个任意字符，这里指斜杠`，
  - 用`&`代表`匹配到的内容`，`匹配到的内容`前面加一个`#`。

- 末尾没有加g。说明只匹配一个。

👇效果如下：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.38o70x7ocdi0.png)

###  1.2. <a name='ipv4'></a>开启ipv4转发（可跳过）

```sh
sudo vim /etc/sysctl.conf
```

```sh
net.ipv4.ip_forward = 1 #注释掉这一行
```

###  1.3. <a name='-1'></a>关闭防火墙（可跳过）

```sh
sudo /usr/sbin/iptables -P FORWARD ACCEPT  #永久
```

##  2. <a name='-1'></a>外网操作

###  2.1. <a name='microk8s'></a>安装 microk8s

K8s 是云原生的事实标准

针对边缘计算场景推出了各自的 K8s 发行版，例如 K3s、microK8s、K0s

**K3s** 是一个轻量级的 Kubernetes 发行版，针对边缘计算、物联网等场景进行了高度优化。K3s 将所有 `Kubernetes control-plane 组件`的操作都封装在单个`二进制文件`和进程中，通过环境变量指定启动 `server` 或者 `agent`，最大程度减轻了`外部依赖性`，K3s 仅需要 `kernel 和 cgroup` 就可运行。但 K3s 仍依赖 `containerd`，`flannel` 等组件。同时 k3s 资源消耗低， 根据 k3s 官方文档 `1 核 512M 内存`的机器就可以运行 k3s, 而且还支持 `arm 架构`。虽然 K3s 将云原生的能力拓展到边缘计算，但是边缘计算是以云计算为中心的分布式架构，K3s 缺少了与云计算中心的协同。

MicroK8s 是 Ubuntu 官方生态提供的 K8s 轻量版，适合用于开发工作站、IoT、Edge、CI/CD。

```sh
apt update
apt install openssh-server curl
```

确保防火墙是开放的，你可以稍后使用SSH和web浏览器访问它:

```sh
ufw allow 22
ufw allow 10443
ufw enable
```

现在安装MicroK8s套件:

```sh
sudo snap install microk8s --classic
sudo microk8s enable dashboard dns registry istio
```

现在开始MicroK8s:

```sh
sudo microk8s kubectl get all --all-namespaces
```

它是运行。但您需要启动代理才能访问它。方法如下:

```sh
sudo microk8s dashboard-proxy &
```

提示:上面显示我们正在后台启动代理，但它被绑定到这个shell会话。

上面命令的输出将为您生成一个令牌。收到您将获得登录到MicroK8s仪表板的令牌。

访问仪表板并登录:

<https://example.com:10443>

提示:其中“example.com”是新的MicroK8s环境的主机名或IP地址。它会要求您使用令牌(或其他选项)登录，在这里输入令牌-您在上一步中复制的那个。

有些事情在命令行中更简单。我们将创建一个基本的Nginx pod，名为“agix-nginx”:

```sh
sudo microk8s.kubectl run agix-nginx --image=nginx:alpine --port=80
```

List your pods:

```sh
sudo microk8s.kubectl get pod
```

现在，我们可以将该pod暴露于更广泛的网络中（而不仅仅是在Kubernetes环境中）。请注意，下面指定的端口“80”是pod正在使用的端口，如上述命令所述。请参见下面的命令：

```sh
sudo microk8s kubectl expose pod agix-nginx --type=NodePort --port=80 --name=agix-nginx
sudo microk8s kubectl get services
```

我从上面的“get services”命令的输出显示:

```s
NAME         TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
kubernetes   ClusterIP   10.152.183.1            443/TCP          121m
agix-nginx   NodePort    10.152.183.79           80:30045/TCP   19s
```

现在，您可以从远程机器浏览到Kubernetes主机，并看到Nginx欢迎页面。

<https://exmaple.com:30045>

确保您的端口暴露在防火墙级别:

```sh
sudo ufw allow 30045
```

注意：运行Kubernetes可能会导致大量I/O请求和存储压力。在运行MicroK8s时，不建议将U盘用作主存储器。

在某些ARM硬件上运行MicroK8s可能会遇到困难，因为cGroup

（必需！）默认情况下不启用。这可以在Rasberry Pi上解决

通过编辑引导参数：

```sh
sudo vi /boot/firmware/cmdline.txt
```

注意:在一些树莓派Linux发行版中，启动参数在' /boot/firmware/nobtcmd.txt '中。

并添加以下内容:

```sh
cgroup_enable=memory cgroup_memory=1
```

要解决Raspberry Pi上经常出现的磁盘性能问题，请参阅故障排除部分。

```sh
sudo apt install linux-modules-extra-raspi 
```

安装linux-modules-extra-raspi并使用sudo MicroK8s stop重启MicroK8s;sudo microk8s开始。

```sh
sudo microk8s stop
sudo microk8s start
```

###  2.2. <a name='Ubuntu20.04'></a>Ubuntu 20.04上的副本集和扩展

我们可以创建一个副本集（replicateset）或简称“rs”，这样我们就可以扩展应用程序以满足需求。在Kubernetes主机上创建一个名为“my-rep-set.yaml”的文件，并使用以下内容填充该文件：

提示：这只是一个例子，但它会起作用。注意“副本：5”，这意味着我们将得到5个实例。我们以后再换。

```yaml
apiVersion: apps/v1
kind: ReplicaSet
metadata:
  name: nginx-proxy
  labels:
    app: nginx-proxy
    tier: frontend

spec:
  replicas: 5
  selector:
    matchLabels:
      tier: frontend
  template:
    metadata:
      labels:
        tier: frontend
    spec:
      containers:
      - name: nginx
        image: nginx
```

然后用这个命令创建rs:

```sh
sudo microk8s kubectl apply -f my-rep-set.yaml
```

您可以使用以下命令查看新rs的状态:

```sh
sudo microk8s kubectl get replicaset
```

```s
NAME          DESIRED   CURRENT   READY   AGE
nginx-proxy   5         5         5       2m29s
```

您可以使用以下命令将其扩展到6:

```sh
sudo microk8s kubectl scale -n default replicaset nginx-proxy --replicas=6
```

并确认:

```sh
sudo microk8s kubectl get replicaset
```

```s
NAME          DESIRED   CURRENT   READY   AGE
nginx-proxy   6         6         6       5m11s
```

提示：您可以使用此命令删除rs。将“nginx proxy”替换为您要删除的任何rs，并将“default”替换为您正在使用的名称空间：

```sh
sudo microk8s kubectl delete -n default replicaset nginx-proxy
```

现在，我们可以将我们的新rs向更广泛的网络公开：

```sh
sudo microk8s kubectl expose rs nginx-proxy --type=NodePort --port=80 --name=nginx-proxy
```

获取POD、服务和端口的摘要：

```sh
sudo microk8s kubectl get services
```

```s
NAME          TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)        AGE
kubernetes    ClusterIP   10.152.183.1            443/TCP        7h56m
agix-nginx    NodePort    10.152.183.91           80:30045/TCP   5h53m
nginx-proxy   NodePort    10.152.183.48           80:32538/TCP   80s
```

我们关心的是“nginx-proxy”，它在端口“32538”上。

允许它认为本地防火墙:

```sh
ufw allow 32538
```

现在您可以浏览到它：

http://example.com:32538

###  2.3. <a name='Ubuntu20.04-1'></a>在Ubuntu 20.04上公开应用程序

本文的目标是让应用程序在主机的IP地址和我们选择的端口上向更广泛的网络公开。IP地址将是“10.0.0.210”，我们选择的端口是“30036”。

我们将公开一个dotNet应用程序，根据它的文档，它侦听端口“8080”。

将以下内容放入一个名为“dotnet-rs.yaml”的新文件:

```yaml
apiVersion: apps/v1
kind: ReplicaSet
metadata:
  name: dotnet-hello-rs # This is what our Service will point to. 
spec:
  replicas: 5 # This is the number of instances we want for this app.
  selector:
    matchLabels:
      app: dotnet-hello-rs
  template:
    metadata:
      labels:
        app: dotnet-hello-rs
    spec:
      containers:
      - name: dotnet-hello-rs
        image: appsvctest/dotnetcore # This is our image to execute. 
        ports:
        - containerPort: 8080 # Doco for this image says it's listening on this port. 
```

将以下内容放入一个名为“dotnet-service.yaml”的新文件:

```yaml
apiVersion: v1
kind: Service
metadata: 
  name: dotnet-hello-service
spec:
  selector: 
    app: dotnet-hello-service
  type: NodePort
  ports: 
    - name: http
      protocol: TCP
      nodePort: 30036 # The port our networked devices will look on for the application. 
      port: 9090 
      targetPort: 8080 # The port of the target ReplicaSet. 
  selector:
    app: dotnet-hello-rs # This is the name or the target ReplicaSet. 
```

现在启动ReplicaSet和Service对象。

```sh
sudo microk8s kubectl apply -f dotnet-rs.yaml
sudo microk8s kubectl apply -f dotnet-service.yaml
```

验证我们的ReplicaSet:

```sh
sudo microk8s kubectl get rs -o wide
```

```s
NAME              DESIRED   CURRENT   READY   AGE   CONTAINERS        IMAGES                  SELECTOR
dotnet-hello-rs   5         5         5       15m   dotnet-hello-rs   appsvctest/dotnetcore   app=dotnet-hello-rs
```

验证我们的服务:

```sh
sudo microk8s kubectl get service -o wide
```

```s
NAME                   TYPE        CLUSTER-IP     EXTERNAL-IP   PORT(S)          AGE   SELECTOR
kubernetes             ClusterIP   10.152.183.1           443/TCP          32h   
dotnet-hello-service   NodePort    10.152.183.9           9090:30036/TCP   11m   app=dotnet-hello-rs
```

确保您的防火墙允许端口“30036”通过。

```sh
sudo ufw allow 30036
```

从Kubernetes主机，测试你可以到达Pod:

```sh
curl -v 10.152.183.9:8080
```

现在从一个联网的机器(你的局域网中的其他机器)测试:

```sh
curl - v 10.0.0.210:30036
```

###  2.4. <a name='Microk8s'></a>Microk8s的高可用性

选民voters：复制数据库，参与领导人选举

待机standby：复制数据库，不参与领导人选举

备用spare：不复制数据库，不参与领导人选举

好消息是MicroK8现在可以部署在多节点体系结构中。而且做起来很简单。这段视频 https://www.youtube.com/watch?v=dNT5uEeJBSw 这是一个很好的示范。

它在集群节点之间使用浮动IP，从而无需在集群前面使用负载平衡器。

从安装开始。在每个节点上执行此操作。我使用的是3模式群集，但这并不重要，只需在每个节点上执行相同的操作，每次执行一步：

```sh
sudo snap install microk8s --classic --channel=1.19/stable
```

现在我们需要将节点加入到集群中。我们需要选择一个节点（不管是哪个节点）并在其上运行以下命令。对需要加入的每个节点运行以下命令一次。该命令提供一些输出，您可以使用这些输出将其他节点“一个”粘贴到控制台中，从而使该节点加入集群。对每个节点执行一次。不能对两个节点使用相同的输出。

```sh
microk8s add-node
```

上述命令需要一分钟左右的时间才能完成。

```s
从您希望加入此集群的节点，运行以下命令:
microk8s join 192.168.1.230:25000/92b2db237428470dc4fcfc4ebbd9dc81/2c0cb3284b05

使用“--worker”标志以不运行控制平面的工作人员身份加入节点，例如:
microk8s join 192.168.1.230:25000/92b2db237428470dc4fcfc4ebbd9dc81/2c0cb3284b05 --worker

如果您添加的节点无法通过默认界面访问，您可以使用以下方法之一:
microk8s join 192.168.1.230:25000/92b2db237428470dc4fcfc4ebbd9dc81/2c0cb3284b05
microk8s join 10.23.209.1:25000/92b2db237428470dc4fcfc4ebbd9dc81/2c0cb3284b05
microk8s join 172.17.0.1:25000/92b2db237428470dc4fcfc4ebbd9dc81/2c0cb3284b05
```

这将输出一个带有生成令牌的命令，例如

```sh
microk8s join 10.128.63.86:25000/567a21bdfc9a64738ef4b3286b2b8a69
```

复制此命令并从下一个节点运行它。成功加入可能需要几分钟的时间。

对第三个节点和任何其他节点重复这个过程(生成令牌，从连接节点运行它)。

将一个节点加入到集群应该只需要几秒钟。后来

你应该可以看到节点已经加入:

```sh
microk8s kubectl get no
```

```sh
NAME               STATUS   ROLES    AGE   VERSION
10.22.254.79       Ready    <none>   27s   v1.15.3
ip-172-31-20-243   Ready    <none>   53s   v1.15.3
```

在其中一个节点上，运行以下命令以启用加载项：

```sh
sudo microk8s enable dashboard dns registry istio
```

现在添加HA浮动IP加载项：

```sh
microk8s enable metallb
```

注意:上面的命令将导致一个关于您希望为浮动IP使用哪个IP范围的问题。

现在一切都完成了，在其中任何一个上运行以下命令以查看当前HA状态。你还应该看到你的附加组件已经在所有的集群成员上启用:

您应该会看到这样的内容（后面有更多的文字）：

```s
microk8s is running
high-availability: yes
  datastore master nodes: 10.0.0.210:19001 10.0.0.211:19001 10.0.0.212:19001
  datastore standby nodes: none
...
```

“HA”状态应为“是”，并且您还应具有浮动IP地址。也就是说，在不可用之前只属于其中一个节点的地址，此时另一个节点将承担该角色。

要获取浮动IP和其他详细信息，请运行以下命令：

```sh
sudo microk8s.kubectl get all -A
```

要优雅地删除一个节点，首先在离开的节点上运行leave命令

```sh
microk8s leave
```

节点将在Kubernetes中标记为“NotReady”（不可访问）。要完成离开节点的移除，请在任何剩余节点上执行以下操作：

```sh
microk8s remove-node <node>
microk8s remove-node <node> --force
microk8s remove-node 10.22.254.79
```

升级现有集群

如果您有一个现有的集群，您必须刷新所有节点到至少v1.19，例如:

```sh
sudo snap refresh microk8s --channel=1.19/stable
```

然后需要在主节点上启用HA集群:

```sh
microk8s enable ha-cluster
```

任何已经是集群中节点的机器都需要退出并重新加入

为了建立医管局。

要执行此操作，请在节点之间循环以排空、移除并重新连接它们：

```sh
microk8s kubectl drain <node> --ignore-daemonsets
```

在节点机器上，强制它离开集群:

```sh
microk8s leave
```

然后使用microk8s启用HA启用HA群集：

```sh
microk8s enable ha-cluster
```

并分别使用主节点和节点上发布的microk8s添加节点

```sh
microk8s add-node
```

和microk8s连接将节点重新加入群集。

```sh
microk8s join
```

##  3. <a name='-1'></a>国产版本

###  3.1. <a name='installmicrok8s'></a>install microk8s

```sh
## 检查 hostname
##  要求不含大写字母和下划线，不然依照后文修改
hostname
```

```sh
sudo apt install snapd -y
```

```sh
snap info microk8s
```

```sh
sudo snap install microk8s --classic --channel=1.15/stable
sudo snap install microk8s --classic --channel=1.21/stable
```

```sh
sudo snap install microk8s --edge --classic
```

```sh
sudo snap install microk8s --classic，
```

###  3.2. <a name='microk8s-1'></a>将当前用户加入 microk8s 创建的群组

版本一：

```sh
## 添加用户组
sudo usermod -a -G microk8s $USER
sudo chown -f -R $USER ~/.kube
newgrp microk8s
id $USER
```

版本二：

```sh
sudo usermod -a -G microk8s $USER
sudo chown -f -R $USER ~/.kube
su - $USER
```

###  3.3. <a name='docker'></a>安装 docker

```sh
sudo apt-get install docker.io
```

添加用户到docker group， 并使用该用户:

```sh
sudo usermod -aG docker  ${USER}
su  -  ${USER}
```

###  3.4. <a name='podsandbox'></a>修改pod的sandbox

pod的sandbox 默认是 k8s.gcr.io/pause:3.1，这个镜像是无法获取的。需要将sandbox修改为国内可以获取的镜像。

方案0：

```sh
sudo vim /var/snap/microk8s/current/args/containerd-template.toml
```

```s
...
[plugins.cri.registry]
      [plugins.cri.registry.mirrors]
        [plugins.cri.registry.mirrors."docker.io"]
          endpoint = [
                "https://docker.mirrors.ustc.edu.cn",
                "https://hub-mirror.c.163.com",
                "https://mirror.ccs.tencentyun.com",
                "https://registry-1.docker.io"
          ]
```

方案一：

```sh
## 添加镜像（docker.io）
##  镜像加速器
##   https://yeasy.gitbook.io/docker_practice/install/mirror
##  还可改 args/ 里不同模板的 sandbox_image

sudo vim /var/snap/microk8s/current/args/containerd-template.toml
```

```s
  [plugins."io.containerd.grpc.v1.cri"]
    [plugins."io.containerd.grpc.v1.cri".registry]
      [plugins."io.containerd.grpc.v1.cri".registry.mirrors]
        [plugins."io.containerd.grpc.v1.cri".registry.mirrors."docker.io"]
          endpoint = ["https://x.mirror.aliyuncs.com", "https://registry-1.docker.io", ]
        [plugins."io.containerd.grpc.v1.cri".registry.mirrors."localhost:32000"]
          endpoint = ["http://localhost:32000"]
## 手动导入，见后文启用插件那
```

方案二：

```sh
sudo vim /var/snap/microk8s/current/args/kubelet
```

添加：

```s
--pod-infra-container-image=s7799653/pause:3.1
```

```sh
sudo vim /var/snap/microk8s/current/args/containerd-template.toml
```

修改：plugins -> plugins.cri -> sandbox_image 为 s7799653/pause:3.1

然后，先停止 microk8s，再启动 microk8s

```sh
## 重启服务
microk8s stop
microk8s start
```

命令完成后，你需要启动Microk8s。这通过以下命令来完成：

```sh
sudo microk8s.start 
```

该命令应报告服务已启动，pod调度功能已被启用。

###  3.5. <a name='-1'></a>设置别名（可跳过）

出于简化的目的，我们可以用 kubectl 替代 microk8s.kubectl

```sh
sudo snap alias microk8s.kubectl kubectl
```

或者

```sh
echo "alias kubectl='microk8s.kubectl'" >> ~/.bashrc
source ~/.bashrc
```

###  3.6. <a name='config'></a>config配置（可跳过）

```sh
sudo microk8s.kubectl config view --raw > $HOME/.kube/config
```

###  3.7. <a name='hostname'></a>如果要修改 hostname（可跳过）

```sh
## 改名称
sudo hostnamectl set-hostname ubuntu-vm
## 改 host
sudo vi /etc/hosts

## 云主机的话，还要改下配置
sudo vi /etc/cloud/cloud.cfg
  preserve_hostname: true
  ## 如果只修改 preserve_hostname 不生效，那就直接注释掉 set/update_hostname
  cloud_init_modules:
  ##  - set_hostname
  ##  - update_hostname

## 重启，验证生效
sudo reboot
```

###  3.8. <a name='k8s'></a>启用k8s组件

为了让Microk8大有用途，你需要安装几项额外的服务。

不妨安装基本服务：kube-dns和Microk8s仪表板。

仪表板是一个基于Web的仪表板，让你可以交互和管理Kubernetes。

Kube-dns在集群上调度DNS Pod和服务，并配置kubelete(每个节点上运行的主节点代理)，以指示各个容器将DNS服务IP地址用作DNS解析器。

想安装这两项服务，回到终端窗口，执行命令：

```sh
sudo microk8s.enable dns dashboard 
```

```sh
sudo microk8s.enable dns dashboard istio
```

你还可以启用其他服务，比如：

storage—让你可以使用主机上的存储。

Ingress—创建Ingress控制器。

gpu—启用nvidia-docker运行时环境和nvidia-device-plugin-daemonset。

istio—启用核心的Istio服务。

registry—部署私有Docker注册中心。

如果你认定需要额外服务，在启动并运行Microk8后，你可以随时回过头去执行microk8s.enable命令(带有想要添加的服务)。

```sh
sudo microk8s.enable dashboard dns ingress istio registry storage
```

如果有GPU

```sh
sudo microk8s.enable gpu
```

执行 microk8s.enable 顺利的话，你将看到类似下面的日志

```s
logentry.config.istio.io/accesslog created
logentry.config.istio.io/tcpaccesslog created
rule.config.istio.io/stdio created
rule.config.istio.io/stdiotcp created
...
...
Istio is starting
Enabling the private registry
Enabling default storage class
deployment.extensions/hostpath-provisioner created
storageclass.storage.k8s.io/microk8s-hostpath created
Storage will be available soon
Applying registry manifest
namespace/container-registry created
persistentvolumeclaim/registry-claim created
deployment.extensions/registry created
service/registry created
The registry is enabled
Enabling default storage class
deployment.extensions/hostpath-provisioner unchanged
storageclass.storage.k8s.io/microk8s-hostpath unchanged
Storage will be available soon
```

###  3.9. <a name='microk8s.statusmicrok8s.inspect'></a>microk8s.status 和 microk8s.inspect 检查各个组件的状态

使用 microk8s.status 检查各个组件的状态

```sh
microk8s.status
```

```sh
microk8s status
```

```s
microk8s is running
addons:
knative: disabled
jaeger: disabled
fluentd: disabled
gpu: enabled
cilium: disabled
storage: enabled
registry: enabled
rbac: disabled
ingress: enabled
dns: enabled
metrics-server: disabled
linkerd: disabled
prometheus: disabled
istio: enabled
dashboard: enabled
```

如果 status 不正确时，可以如下排查错误：

使用 microk8s.inspect 排查下安装部署结果

```sh
microk8s.inspect
```

```sh
microk8s inspect
grep -r error /var/snap/microk8s/2346/inspection-report
```

```s
Inspecting services
  Service snap.microk8s.daemon-containerd is running
  Service snap.microk8s.daemon-docker is running
  Service snap.microk8s.daemon-apiserver is running
  Service snap.microk8s.daemon-proxy is running
  Service snap.microk8s.daemon-kubelet is running
  Service snap.microk8s.daemon-scheduler is running
  Service snap.microk8s.daemon-controller-manager is running
  Service snap.microk8s.daemon-etcd is running
  Copy service arguments to the final report tarball
Inspecting AppArmor configuration
Gathering system info
  Copy network configuration to the final report tarball
  Copy processes list to the final report tarball
  Copy snap list to the final report tarball
  Inspect kubernetes cluster

  
 WARNING:  IPtables FORWARD policy is DROP. Consider enabling traffic forwarding with: sudo iptables -P FORWARD ACCEPT
```

执行如下命令

```sh
sudo ufw default allow routed
sudo iptables -P FORWARD ACCEPT
```

再次使用 microk8s.inspect 命令检查，会发现 WARNING消失了

```sh
microk8s.inspect
```

###  3.10. <a name='pods'></a>检查 pods

使用 microk8s.kubectl get pods --all-namespaces 查看当前 Kubernetes pods 状态

```sh
sudo microk8s.kubectl get pods --all-namespaces
```

一旦安装了仪表板，你需要找到可以访问它的地址。为此，执行命令：

```sh
sudo microk8s.kubectl get all --all-namespaces 
```

```s
NAMESPACE            NAME                                              READY   STATUS              RESTARTS   AGE
container-registry   registry-7fc4594d64-rrgs9                         0/1     Pending             0          15m
default              default-http-backend-855bc7bc45-t4st8             0/1     ContainerCreating   0          16m
default              nginx-ingress-microk8s-controller-kgjtl           0/1     ContainerCreating   0          16m
...
...
```

发现相关pod一直处于ContainerCreating状态！！！

大部分pod都没有启动起来，什么原因呢？

使用 describe 命令查看 pod

```sh
kubectl describe pod default-http-backend -n container-registry
```

```sh
### 继续查找原因
kubectl describe pod/coredns-9b8997588-dp9cx -n kube-system
```

```sh
## 查看 Pods ，确认 running
microk8s kubectl get pods --all-namespaces
## 不然，详情里看下错误原因
microk8s kubectl describe pod --all-namespaces
```

日志如下：

```s
Events:
  Type     Reason                  Age                   From                              Message
  ----     ------                  ----                  ----                              -------
  Warning  FailedCreatePodSandBox  22m (x33 over 69m)    kubelet, izwz9h8m2chowowqckbcy0z  Failed create pod sandbox: rpc error: code = Unknown desc = failed to get sandbox image "k8s.gcr.io/pause:3.1": failed to pull image "k8s.gcr.io/pause:3.1": failed to resolve image "k8s.gcr.io/pause:3.1": no available registry endpoint: failed to do request: Head https://k8s.gcr.io/v2/pause/manifests/3.1: dial tcp 108.177.97.82:443: i/o timeout
```

这是 pod 的 sandbox 镜像拉取失败。

网上查资料，k8s.gcr.io/pause:3.1 是存放在 google cloud 上的镜像，由于众所周知的原因，访问失败了。

解决的方法有：

- 科学上网

- 手动下载镜像

###  3.11. <a name='-1'></a>使用代理

如果有代理的话，也可以省去 `手动下载镜像` 的麻烦，修改

```s
${SNAP_DATA}/args/containerd-env

/var/snap/microk8s/current/args/containerd-env
```

加入两行

```sh
HTTPS_PROXY=<你的代理地址:端口>
HTTP_PROXY=<你的代理地址:端口>
```

然后

```sh
sudo microk8s stop
sudo microk8s start
```

###  3.12. <a name='-1'></a>手动下载镜像操作流程

安装 docker

```sh
sudo apt install docker-ce
```

感谢微软 azure 提供 gcr 镜像下载：地址

```sh
## 使用Docker 从其它镜像仓库下载pause:3.1 镜像：
docker pull gcr.azk8s.cn/google_containers/pause:3.1
## 下载后修改Tag：
docker tag gcr.azk8s.cn/google_containers/pause:3.1 k8s.gcr.io/pause:3.1
```

下载docker hub 上的 镜像

```sh
### 下载docker hub 上的相关镜像
sudo docker pull mirrorgooglecontainers/pause:3.1
### 将相关镜像tag为 k8s.gcr.io/pause:3.1
sudo docker tag mirrorgooglecontainers/pause:3.1 k8s.gcr.io/pause:3.1
```

v1.14 之后 microk8s 使用 containerd 代替 dockerd

手动把 docker 镜像导入到 containerd

保存镜像为 tar 文件：

```sh
### 保存镜像为本地镜像文件
## 将Puase从docker环境中导出为，pause.tar
docker save k8s.gcr.io/pause:3.1 > pause.tar
## 将导出的文件，导入microk8s 镜像列表中：
### ctr导入本地镜像
## 方案一
sudo microk8s ctr image import pause.tar
sudo microk8s.ctr image import pause.tar
## 方案二
sudo microk8s.ctr -n k8s.io image import pause.tar
```

过一下，再观察 coredns 状态，会发现已经成功的 running （如果还不成功，则继续 通过 kubectl describe pods ... 观察 pods 的状态，如果有下载不成功的镜像，再如法炮制一遍。

```sh
## 其他版本：
## 如果拉取镜像失败，可以 microk8s ctr image pull <mirror>
## 或者，docker pull 后导入 containerd

docker pull registry.cn-hangzhou.aliyuncs.com/google_containers/pause:3.1
docker tag registry.cn-hangzhou.aliyuncs.com/google_containers/pause:3.1 k8s.gcr.io/pause:3.1
docker save k8s.gcr.io/pause:3.1 > pause:3.1.tar
microk8s ctr image import pause:3.1.tar

docker pull calico/cni:v3.13.2
docker save calico/cni:v3.13.2 > cni:v3.13.2.tar
microk8s ctr image import cni:v3.13.2.tar

docker pull calico/node:v3.13.2
docker save calico/node:v3.13.2 > node:v3.13.2.tar
microk8s ctr image import node:v3.13.2.tar
```

-n 是指定 namespace。

```sh
microk8s.ctr -n k8s.io image ls
## 看到导入的镜像了：
```

k8s.gcr.io/pause:3.1                                                                             application/vnd.oci.image.manifest.v1+json                sha256:3efe4ff64c93123e1217b0ad6d23b4c87a1fc2109afeff55d2f27d70c55d8f73 728.9 KiB linux/amd64 io.cri-containerd.image=managed

其他组件如果遇到gcr.io无法访问的情况也可使用如上的方法，这里特别写了个脚本来自动下载并导入这些镜像：

```sh
#!/usr/bin/env bash

echo ""
echo "=========================================================="
echo "pull microk8s v1.15.11 images from gcr.azk8s.cn ..."
echo "=========================================================="
echo ""

gcr_imgs=(
    "gcr.azk8s.cn/google_containers/pause:3.1,k8s.gcr.io/pause:3.1"
    "gcr.azk8s.cn/google_containers/heapster-influxdb-amd64:v1.3.3,k8s.gcr.io/heapster-influxdb-amd64:v1.3.3"
    "gcr.azk8s.cn/google_containers/heapster-grafana-amd64:v4.4.3,k8s.gcr.io/heapster-grafana-amd64:v4.4.3"
    "gcr.azk8s.cn/google_containers/kubernetes-dashboard-amd64:v1.10.1,k8s.gcr.io/google_containers/kubernetes-dashboard-amd64:v1.10.1"
    "gcr.azk8s.cn/google_containers/heapster-amd64:v1.5.2,k8s.gcr.io/heapster-amd64:v1.5.2"
    "gcr.azk8s.cn/google_containers/defaultbackend-amd64:1.4,gcr.io/google_containers/defaultbackend-amd64:1.4"
)

for img in ${gcr_imgs[@]}
do
    img_array=(${img//,/ })
    ## 拉取镜像
    docker pull ${img_array[0]}
    ## 添加Tag
    docker tag ${img_array[0]} ${img_array[1]}
    ## 输出
    docker save ${img_array[1]} > ${img_array[1]##*/}.tar
    ## 输入
    microk8s.ctr -n k8s.io image import ${img_array[1]##*/}.tar
    ## 删除Tag
    docker rmi ${img_array[0]} ${img_array[1]}
done

echo ""
echo "=========================================================="
echo "pull microk8s  v1.15.11 images from gcr.azk8s.cn finished."
echo "=========================================================="
echo ""
```

使用 microk8s.kubectl get pods --all-namespaces 继续进行验证

```sh
microk8s.kubectl get pods --all-namespaces
```

```s
NAMESPACE            NAME                                                           READY   STATUS             RESTARTS   AGE
cert-manager         cert-manager-5d849b9888-8nh9j                                  1/1     Running            12         3d18h
cert-manager         cert-manager-cainjector-dccb4d7f-7rrkf                         1/1     Running            15         3d
cert-manager         cert-manager-webhook-695df7dbb-gpsqs                           1/1     Running            12         3d18h
container-registry   registry-6c99589dc-gttcq                                       1/1     Running            15         4d4h
default              default-http-backend-5d5ff5d4f5-g9h8h                          1/1     Running            15         4d4h
default              nginx-ingress-microk8s-controller-td2mz                        1/1     Running   59         3h26m
istio-system         cluster-local-gateway-7bf56777fb-rbjjn                         1/1     Running            12         3d18h
istio-system         grafana-6575997f54-j77rc                                       1/1     Running            6          3d
istio-system         istio-citadel-894d98c85-xr8qm                                  1/1     Running            12         3d19h
istio-system         istio-cleanup-secrets-1.2.2-l4djr                              0/1     Completed          0          3d19h
istio-system         istio-egressgateway-9b7866bf5-h8ltc                            1/1     Running            10         3d
istio-system         istio-galley-5b984f89b-w26n9                                   1/1     Running            0          6h43m
istio-system         istio-grafana-post-install-1.2.2-v5sfg                         0/1     Completed          0          3d19h
istio-system         istio-ingressgateway-75ddf64567-glfkm                          1/1     Running            12         3d19h
istio-system         istio-pilot-5d77c559d4-nhc7d                                   2/2     Running            14         3d
istio-system         istio-policy-86478df5d4-w2lgb                                  2/2     Running            46         3d
istio-system         istio-security-post-install-1.2.2-sczrc                        0/1     Completed          0          3d19h
istio-system         istio-sidecar-injector-7b98dd6bcc-g597g                        1/1     Running            8          3d
istio-system         istio-telemetry-786747687f-t8k6k                               2/2     Running            35         3d
istio-system         istio-tracing-555cf644d-4d9f4                                  1/1     Running            13         3d19h
istio-system         kfserving-ingressgateway-64c7bd9b76-2rcxt                      1/1     Running            12         3d18h
istio-system         kiali-6cd6f9dfb5-tlwzq                                         1/1     Running            13         3d19h
istio-system         prometheus-7d7b9f7844-swqf8                                    1/1     Running            19         3d19h
kube-system          coredns-f7867546d-wkv76                                        1/1     Running            15         4d4h
kube-system          heapster-v1.5.2-844b564688-kr9t8                               4/4     Running            60         4d4h
kube-system          hostpath-provisioner-65cfd8595b-rjlhz                          1/1     Running            5          3d
kube-system          kubernetes-dashboard-7d75c474bb-s7n2t                          1/1     Running            15         4d4h
kube-system          monitoring-influxdb-grafana-v4-6b6954958c-spcqb                2/2     Running            32         4d4h
kube-system          nvidia-device-plugin-daemonset-jv96f                           1/1     Running            14         3d23h
```

```sh
microk8s kubectl describe pod --all-namespaces
```

直到全部正常 running：

```s
$ kubectl get pods --all-namespaces
NAMESPACE     NAME                                         READY   STATUS    RESTARTS   AGE
kube-system   kubernetes-dashboard-85fd7f45cb-snqrv        1/1     Running   1          15h
kube-system   dashboard-metrics-scraper-78d7698477-tmb7k   1/1     Running   1          15h
kube-system   metrics-server-8bbfb4bdb-wlf8g               1/1     Running   1          15h
kube-system   calico-node-p97kh                            1/1     Running   1          6m18s
kube-system   coredns-7f9c69c78c-255fg                     1/1     Running   1          15h
kube-system   calico-kube-controllers-f7868dd95-st9p7      1/1     Running   1          16h
```

如果你看到的结果类似上面这样，说明 Kubernetes 是真的就绪了。

如果 calico-node CrashLoopBackOff，可能网络配置问题：

```sh
## 查具体日志
microk8s kubectl logs -f -n kube-system calico-node-l5wl2 -c calico-node
## 如果有 Unable to auto-detect an IPv4 address，那么 ip a 找出哪个网口有 IP 。修改：
sudo vi /var/snap/microk8s/current/args/cni-network/cni.yaml
  - name: IP_AUTODETECTION_METHOD
  value: "interface=wlo.*"
## 重启服务
microk8s stop; microk8s start

### 参考
## Issue: Microk8s 1.19 not working on Ubuntu 20.04.1
##  https://github.com/ubuntu/microk8s/issues/1554
## Issue: CrashLoopBackOff for calico-node pods
##  https://github.com/projectcalico/calico/issues/3094
## Changing the pods CIDR in a MicroK8s cluster
##  https://microk8s.io/docs/change-cidr
## MicroK8s IPv6 DualStack HOW-TO
##  https://discuss.kubernetes.io/t/microk8s-ipv6-dualstack-how-to/14507
```

###  3.13. <a name='Dashboard'></a>查看 Dashboard

```sh
microk8s.kubectl describe service kubernetes-dashboard -n kube-system
## 获取访问ip和端口
```

```s
Name:              kubernetes-dashboard
Namespace:         kube-system
Labels:            k8s-app=kubernetes-dashboard
Annotations:       kubectl.kubernetes.io/last-applied-configuration:
                     {"apiVersion":"v1","kind":"Service","metadata":{"annotations":{},"labels":{"k8s-app":"kubernetes-dashboard"},"name":"kubernetes-dashboard"...
Selector:          k8s-app=kubernetes-dashboard
Type:              ClusterIP
IP:                10.152.183.151
Port:              <unset>  443/TCP
```

接着访问下面的地址，就能看到我们熟悉的 Dashboard

<https://10.152.183.151/>

###  3.14. <a name='token'></a>使用令牌登录：获取token

此时，你会看到仪表板需要令牌才能获得访问权限。怎么找到那个令牌?首先你要使用以下命令让Microk8s列出所有可用的机密(secret)：

方案一：

```sh
token=$(microk8s kubectl -n kube-system get secret | grep default-token | cut -d " " -f1)
 echo $token
microk8s kubectl -n kube-system describe secret $token
```

```sh
token=$(microk8s kubectl -n kube-system get secret | grep default-token | cut -d " " -f1)
microk8s kubectl -n kube-system describe secret $token
```

现在将dashboard 服务映射到本地及端口10443

```sh
## 转发端口
microk8s kubectl port-forward -n kube-system service/kubernetes-dashboard 10443:443
## 打开网页，输入 Token 登录
xdg-open https://127.0.0.1:10443

## 更多说明 https://microk8s.io/docs/addon-dashboard
## Issue: Your connection is not private
##  https://github.com/kubernetes/dashboard/issues/3804
```

映射完成后可以通过本地Node端口100443访问dashboard.

或者使用本地安装Poxy方式，执行一下命令，在本地启动一个Proxy服务：

microk8s kubectl proxy

完成后通过本地浏览器，输入以下地址访问：

 <http://localhost:8001/api/v1/namespaces/kube-system/services/https:kubernetes-dashboard:/proxy/>

注意命名空间是kube-system.

方案二：

```sh
sudo microk8s.kubectl -n kube-system get secret 
```

想检索相应服务的秘密令牌，执行以下命令：

```sh
sudo microk8s.kubectl -n kube-system describe secret kubernetes-dashboard-token-fv247
```

确保修改fv247条目，以匹配与你安装的Kubernetes Dashboard实例关联的密钥。

上述命令将显示一长串字符。复制该字符串，然后回到Web浏览器。在仪表板令牌窗口(图D)中，选择令牌，然后将复制的令牌粘贴到Enter token文本字段。

点击“SIGN IN”，你会发现自己已在Kubernetes仪表板上(图E)。

此时，你可以从基于Web的仪表板管理Kubernetes。创建作业、pod、副本和计划任务等。点击右上角的“创建”按钮，你可以直接写入或者粘贴YAML或JSON文件的内容。比如假设你想创建NGINX部署。将以下内容复制到编辑器中(图F)，然后点击UPLOAD。

```yaml
apiVersion: v1 

kind: ReplicationController 

metadata: 

name: nginx 

spec: 

replicas: 2 

selector: 

app: nginx 

template: 

metadata: 

name: nginx 

labels: 

app: nginx 

spec: 

containers: 

- name: nginx 

image: nginx 

ports: 

- containerPort: 80 
```

上传YAML文件后，点击Workloads> Pods，你会看到它已被列为正在运行中(图G)。

这就是使用Microk8s启动并运行Kubernetes的单节点实例(以及部署简单的Pod)的全部内容。该工具应该有助于你立即开发自己的Kubernetes应用程序和服务。

输入token后就进入管理页面了

```sh

```

```sh

```
