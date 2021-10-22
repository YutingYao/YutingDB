# 可能不需要安装ssh-可以跳过

A、B分别安装ssh：这一步可能不需要。

```sh
sudo apt-get install ssh
```

Ubuntu默认安装SSH server，这一步可能不需要。

```bash
sudo apt-get install openssh-server
```

Ubuntu默认安装SSH Client，这一步可能不需要。

```bash
sudo apt-get install openssh-client
```

开启Openssh服务：

```bash
sudo service ssh start
```

查看SSH服务运行状态：（也可以不查看）

```bash
service ssh status
```

# 配置 /etc/hosts 配置主机名

```sh
ifconfig
```

```s
sudo vim /etc/hosts
```

192.168.31.219 node01
192.168.31.6 node02
192.168.31.189 ubuntu01

```s
ssh-keygen -t rsa
```

输入命令，提示直接按enter即可

如果重置ssh-key，则需要按 y

```s
sudo vim /etc/ssh/sshd_config
```

主要找到下面的几行，修改成下面的样子:

```s
PermitRootLogin prohibit-password 改为yes。
RSAAuthentication yes #这个貌似没找到
PubkeyAuthentication yes
AuthorizedKeysFile %h/.ssh/authorized_keys
```

配置完成后重启：

```sh
 sudo service ssh restart
```

这些命令不知道有没有用：

sudo find / -name ssh*config
sudo vim /etc/ssh/sshd_config
sudo vim /etc/ssh/ssh_config
sudo vim /usr/share/openssh/sshd_config

# 用u盘复制秘钥

 ubuntu系统只有在root用户下才能ssh成功

```s
cd ~/.ssh
```

有以下几个文件:

* authorized_keys:存放远程免密登录的公钥,主要通过这个文件记录多台机器的公钥

* id_rsa : 生成的私钥文件

* id_rsa.pub ： 生成的公钥文件

* know_hosts : 已知的主机公钥清单


```s
scp ~/.ssh/id_rsa.pub /media/yaoyuting03/47D7-58D1/ubuntu01/id_rsa.pub
scp ~/.ssh/id_rsa.pub /media/pi/47D7-58D11/node01/id_rsa.pub
scp ~/.ssh/id_rsa.pub /media/pi/47D7-58D11/node02/id_rsa.pub 
```

在master上

```s
cat /media/yaoyuting03/47D7-58D1/node01/id_rsa.pub >> ~/.ssh/authorized_keys
cat /media/yaoyuting03/47D7-58D1/node02/id_rsa.pub >> ~/.ssh/authorized_keys
cat /media/yaoyuting03/47D7-58D1/ubuntu01/id_rsa.pub >> ~/.ssh/authorized_keys
```

在node上
```s
cat /media/pi/47D7-58D12/node01/id_rsa.pub >> ~/.ssh/authorized_keys
cat /media/pi/47D7-58D12/node02/id_rsa.pub >> ~/.ssh/authorized_keys
cat /media/pi/47D7-58D12/ubuntu01/id_rsa.pub >> ~/.ssh/authorized_keys
```

```s
chmod 600 ~/.ssh/authorized_keys 
```

也就是是说，700更加高级耶

* -rw------- (600)    只有拥有者有读写权限。
* -rw-r--r-- (644)    只有拥有者有读写权限；而属组用户和其他用户只有读权限。
* -rwx------ (700)    只有拥有者有读、写、执行权限。

```s
sudo service ssh start
sudo service ssh restart
sudo service sshd restart
```

无需密码即可登录则成功。

```bash
ssh node01
```

```bash
ssh node02
```

输入命令 exit 可退出ssh当前登录

```bash
exit
```