<!-- vscode-markdown-toc -->
* 1. [安装 Prometheus, InfluxDB 和 Grafana](#PrometheusInfluxDBGrafana)
	* 1.1. [安装 Prometheus](#Prometheus)
	* 1.2. [安装 InfluxDB](#InfluxDB)
		* 1.2.1. [设置初始用户](#)
		* 1.2.2. [监控 Raspberry Pi](#RaspberryPi)
		* 1.2.3. [利用 InfluxDBReporter 获取监控数据](#InfluxDBReporter)
	* 1.3. [安装 Grafana](#Grafana)
		* 1.3.1. [登录 grafana](#grafana)
		* 1.3.2. [配置 Grafana 展示监控数据](#Grafana-1)
		* 1.3.3. [效果展示](#-1)
	* 1.4. [Prometheus + Grafana + NodeManager + Pushgateway 打造企业级 Flink 平台监控系统](#PrometheusGrafanaNodeManagerPushgatewayFlink)
		* 1.4.1. [利用 PrometheusReporter 获取监控数据](#PrometheusReporter)
		* 1.4.2. [利用 PrometheusPushGatewayReporter 获取监控数据](#PrometheusPushGatewayReporter)
		* 1.4.3. [提交 Flink 任务](#Flink)
* 2. [监控指标](#-1)
	* 2.1. [Flink Metrics 简介](#FlinkMetrics)
	* 2.2. [监控 JobManager](#JobManager)
		* 2.2.1. [基础指标](#-1)
		* 2.2.2. [Checkpoint 指标](#Checkpoint)
		* 2.2.3. [重要的指标](#-1)
	* 2.3. [监控 TaskManager](#TaskManager)
	* 2.4. [监控 Flink 作业 job](#Flinkjob)
	* 2.5. [最关心的性能指标](#-1)
		* 2.5.1. [系统指标](#-1)
		* 2.5.2. [自定义指标](#-1)
	* 2.6. [指标的聚合方式](#-1)
	* 2.7. [作业异常报警](#-1)
	* 2.8. [What to do with Backpressure?](#WhattodowithBackpressure)
* 3. [实战案例 - 金融风控场景](#-)
	* 3.1. [要求](#-1)
	* 3.2. [全链路时延计算方式](#-1)
	* 3.3. [提交任务到Flink on Yarn集群](#FlinkonYarn)
	* 3.4. [打开 Prometheus 在对话框输入全链路时延计算公式](#Prometheus-1)
	* 3.5. [问题分析](#-1)
	* 3.6. [并行度问题](#-1)
	* 3.7. [Buffer 问题](#Buffer)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->##  1. <a name='PrometheusInfluxDBGrafana'></a>安装 Prometheus, InfluxDB 和 Grafana

###  1.1. <a name='Prometheus'></a>安装 Prometheus

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.58v1y39ymtw0.png)

[Prometheus](https://grafana.com/docs/grafana/latest/datasources/prometheus/) 作为生态圈 Cloud Native Computing Foundation（简称：CNCF）中的重要一员，其活跃度仅次于 Kubernetes，现已广泛用于 Kubernetes 集群的监控系统中。

许多公司和组织都采用了 Prometheus 作为监控告警工具。

Prometheus 被用在微服务系统的监控上，Prometheus 天生为监控而生。

Prometheus 提供有数据查询语言 PromQL。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5gk4qssv4tw0.png)

[官网](https://prometheus.io/download/)下载 Prometheus 的安装包

raspberry pi 3 和 4 是 armv7 架构

下载以后直接启动

```sh
tar xvfz prometheus-2.31.1.linux-armv7.tar.gz
cd prometheus-*

./prometheus --version
./prometheus --config.file=prometheus.yml
```

访问本地的 <http://localhost:9090/> 即可以看到 Prometheus 的 Graph 页面。

###  1.2. <a name='InfluxDB'></a>安装 InfluxDB

InfluxDB 是一款时序数据库，使用它作为监控数据存储的公司也有很多

可以根据 [InfluxDB 官网](https://docs.influxdata.com/influxdb/v2.1/) 的安装步骤来操作。

```sh
# arm
tar xvzf path/to/influxdb2-2.1.1-linux-arm64.tar.gz
```

```sh
# arm
sudo cp influxdb2-2.1.1-linux-arm64/influxd /usr/local/bin/
```

启停 InfluxDB。

```sh
# //启动 influxdb 命令
systemctl start influxdb
# //重启 influxdb 命令
systemctl restart influxd
# //停止 influxdb 命令
systemctl stop influxd
# //设置开机自启动
systemctl enable influxdb
```

```sh
influxd
```

通过UI设置

<http://localhost:8086/>

点击 Get Started

####  1.2.1. <a name=''></a>设置初始用户

为初始用户输入用户名。

输入用户的“密码”和“确认密码”。

输入初始的组织名称。

输入初始桶名。

单击Continue。

也可以，✌使用下面命令来创建用户：

```sql
CREATE USER zhisheng WITH PASSWORD '123456' WITH ALL PRIVILEGES
```

然后执行命令，查看创建的用户：

```s
show users;
```

对 InfluxDB 开启身份验证，编辑 InfluxDB 配置文件 /etc/influxdb/influxdb.conf

将 auth-enabled 设置为 true

然后重启 InfluxDB

这时需要使用下面命令的命令才能够登录：

influx -username  yaoyuting -password yyt123456

重新登录就能查询到用户和数据了

```s
show users;
```

```s
show databases;
```

```s
create database YutingDB2;
```

```s
show databases;
```

```s
use YutingDB2;
```

```s
show measurements;
```

####  1.2.2. <a name='RaspberryPi'></a>监控 Raspberry Pi

[树莓派系统模板](https://hub.fastgit.org/influxdata/community-templates/tree/master/raspberry-pi)

```sh
influx apply -u https://raw.githubusercontent.com/influxdata/community-templates/master/raspberry-pi/raspberry-pi-system.yml
```

设置说明

在树莓派设备上安装Telegraf。

```sh
sudo usermod -a -G video telegraf
sudo -u telegraf vcgencmd measure_temp
```

您可能需要注销/重新登录才能使其生效。如果在树莓派400上运行Ubuntu，可能需要重新启动。

您可能需要修改telegraf配置以反映vcgencmd的位置。目前，telegraf配置是为Ubuntu (Groovy Gorilla)设置的:

```sh
[[inputs.exec]]
  commands = ["/usr/bin/vcgencmd measure_temp"]
  name_override = "temperature_gpu"
  data_format = "grok"
  grok_patterns = ["%{NUMBER:value:float}"]
```

您可以使用以下方法检查Raspberry Pi上vcgencmd的位置：

```sh
which vcgencmd
```

通常在Raspberry Pi OS上，命令行应为：

```sh
commands = ["/opt/vc/bin/vcgencmd measure_temp"]
```

命令行设置:

```sh
export INFLUX_HOST=host
export INFLUX_TOKEN=token
export INFLUX_ORG=my_org
```

SystemD设置:

为systemd电报创建环境变量。在/etc/default/telegraf文件中定义变量flux_token、flux_org和flux_host。

```sh
# write this with values to the /etc/default/telegraf file
INFLUX_HOST=host
INFLUX_TOKEN=token
INFLUX_ORG=my_org
```

####  1.2.3. <a name='InfluxDBReporter'></a>利用 InfluxDBReporter 获取监控数据

Flink 里面提供了 InfluxDBReporter 支持将 Flink 的 metrics 数据直接存储到 InfluxDB 中

在源码中该模块是通过 MetricMapper 类将 MeasurementInfo（这个类是 metric 的数据结构，里面含有两个字段 name 和 tags） 和 Gauge、Counter、Histogram、Meter 组装成 InfluxDB 中的 Point 数据。

Point 结构如下（主要就是构造 metric name、fields、tags 和 timestamp）：

```java
private String measurement;
private Map<String, String> tags;
private Long time;
private TimeUnit precision;
private Map<String, Object> fields;
```

然后在 InfluxdbReporter 类中将 metric 数据导入 InfluxDB，该类继承自 AbstractReporter 抽象类，实现了 Scheduled 接口，有下面 3 个属性：

```java
private String database;
private String retentionPolicy;
private InfluxDB influxDB;
```

在 open 方法中获取配置文件中的 InfluxDB 设置，然后初始化 InfluxDB 相关的配置，构造 InfluxDB 客户端：

```java
public void open(MetricConfig config) {
    //获取到 host 和 port
 String host = getString(config, HOST);
 int port = getInteger(config, PORT);
 //判断 host 和 port 是否合法
 if (!isValidHost(host) || !isValidPort(port)) {
  throw new IllegalArgumentException("Invalid host/port configuration. Host: " + host + " Port: " + port);
 }
 //获取到 InfluxDB database
 String database = getString(config, DB);
 if (database == null) {
  throw new IllegalArgumentException("'" + DB.key() + "' configuration option is not set");
 }
 String url = String.format("http://%s:%d", host, port);
 //获取到 InfluxDB username 和 password
 String username = getString(config, USERNAME);
 String password = getString(config, PASSWORD);

 this.database = database;
 //InfluxDB 保留政策
 this.retentionPolicy = getString(config, RETENTION_POLICY);
 if (username != null && password != null) {
     //如果有用户名和密码，根据 url 和 用户名密码来创建连接
  influxDB = InfluxDBFactory.connect(url, username, password);
 } else {
     //否则就根据 url 连接
  influxDB = InfluxDBFactory.connect(url);
 }

 log.info("Configured InfluxDBReporter with {host:{}, port:{}, db:{}, and retentionPolicy:{}}", host, port, database, retentionPolicy);
}
```

然后在 report 方法中调用一个内部 buildReport 方法来构造 BatchPoints，将一批 Point 放在该对象中，BatchPoints 对象的属性如下：

```java
private String database;
private String retentionPolicy;
private Map<String, String> tags;
private List<Point> points;
private ConsistencyLevel consistency;
private TimeUnit precision;
```

通过 buildReport 方法返回的 BatchPoints 如果不为空，则会通过 write 方法将 BatchPoints 写入 InfluxDB：

```java
if (report != null) {
 influxDB.write(report);
}
```

在使用 InfluxDBReporter 时需要注意：

1. 必须复制 Flink 安装目录下的 /opt/flink-metrics-influxdb-1.9.0.jar 到 flink 的 lib 目录下，否则运行起来会报 ClassNotFoundException 错误，详细错误如下图所示：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3p1ej9o2r3m0.png)

2. 如下所示，在 flink-conf.yaml 中添加 InfluxDB 相关的配置。

flink1.10之后采用

```yaml
metrics.reporter.influxdb.factory.class: org.apache.flink.metrics.influxdb.InfluxdbReporterFactory
metrics.reporter.influxdb.host: localhost
metrics.reporter.influxdb.port: 8086
metrics.reporter.influxdb.db: flink
metrics.reporter.influxdb.consistency: ANY
metrics.reporter.influxdb.connectTimeout: 60000
metrics.reporter.influxdb.writeTimeout: 60000
metrics.reporter.influxdb.interval: 30 SECONDS
```

flink1.10之前

```yaml
metrics.reporters: influxdb
metrics.reporter.influxdb.class: org.apache.flink.metrics.influxdb.InfluxdbReporter
metrics.reporter.influxdb.host: localhost
metrics.reporter.influxdb.port: 8086
metrics.reporter.influxdb.db: flink_monitor
metrics.reporter.influxdb.username: flink-metrics
metrics.reporter.influxdb.password: 123
```

```yaml
metrics.reporter.influxdb.class: org.apache.flink.metrics.influxdb.InfluxdbReporter
metrics.reporter.influxdb.host: ubuntu01  # InfluxDB服务器主机
metrics.reporter.influxdb.port: 8086   # 可选）InfluxDB 服务器端口，默认为 8086
metrics.reporter.influxdb.db: YutingDB # 用于存储指标的 InfluxDB 数据库  
metrics.reporter.influxdb.username: yaoyuting # （可选）用于身份验证的 InfluxDB 用户名
metrics.reporter.influxdb.password: yyt123456 # （可选）InfluxDB 用户名用于身份验证的密码
metrics.reporter.influxdb.retentionPolicy: one_hour #（可选）InfluxDB 数据保留策略，默认为服务器上数据库定义的保留策略
```

注意事项：收集flinkSQL任务的监控指标，如果用户书写的sql语句 insert into 或者insert overwrite 中单引号带有换行符，写入influxdb会报错

查看influxdb收集到监控信息，发现会自动给我生成数据库和measurement，所有的指标都存储在了具体的measurement中。

```s
show measurements
```

###  1.3. <a name='Grafana'></a>安装 Grafana

Grafana 是一款优秀的图表可视化组件，它拥有超多酷炫的图表

Prometheus 完美支持 Grafana，可以通过 PromQL 语法结合 Grafana，快速实现监控图的展示。为了和运维平台关联，通过 url 传参的方式，实现了运维平台直接打开指定集群和指定实例的监控图。

```sh
wget https://dl.grafana.com/oss/release/grafana-8.2.5.linux-amd64.tar.gz
tar -zxvf grafana-8.2.5.linux-amd64.tar.gz

cd grafana-8.0.6
nohup ./grafana-server start &
./bin/grafana-server web &
service grafana-server start
sudo systemctl start grafana-server
```

启动grafana服务./grafana-server 。访问本地的<http://localhost:3000/> 可以看到grafana页面。

```sh
tar -zxvf prometheus-2.28.1.linux-amd64.tar.gz
mv prometheus-2.28.1.linux-amd64 prometheus-2.28.1
cd prometheus-2.28.1
vi prometheus.yml
```

```sh
tar -zxvf grafana-8.0.6.linux-amd64.tar.gz
cd grafana-8.0.6
nohup ./grafana-server start &
```

```s
//启动 Grafana
systemctl start grafana-server
//停止 Grafana
systemctl stop grafana-server
//重启 Grafana
systemctl restart grafana-server
//设置开机自启动
systemctl enable grafana-server
```

```sh
docker run -p 3000:3000 --name=grafana grafana/grafana:7.4.2
```

访问 <http://localhost:3000/> 可以看到 Grafana 界面。

####  1.3.1. <a name='grafana'></a>登录 grafana

登录用户名和密码都是 admin

会提示修改密码。

[grafana 配置中文教程](https://grafana.com/docs/grafana/latest/datasources/prometheus/)

####  1.3.2. <a name='Grafana-1'></a>配置 Grafana 展示监控数据

登录 Grafana 后，需要配置数据源，Grafana 支持的数据源有很多

比如 InfluxDB、Prometheus 等，选择不同的数据源都可以绘制出很酷炫的图表

这里演示就选择 InfluxDB，然后填写 InfluxDB 的地址和用户名密码，操作步骤如下图所示。

settings - HTTPS - URL

```s
http://localhost:8086/
```

settings - InfluxDB Detials - Database

```S
YutingDB
```

settings - InfluxDB Detials - User

```s
yaoyuting
```

settings - InfluxDB Detials - Password

```s
yyt123456
```

出现：Data source is working

点击：Save & Test

####  1.3.3. <a name='-1'></a>效果展示

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3ltbbufx3r80.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.71dtin6t0p40.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6nttyphyc4c0.png)

警告⚠配置：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.309ws7vhy1k0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.11huxh20rbq8.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3d9t7ui055y0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1povds1j9z1c.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.fvi1ke2d3ug.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2jldmso5f6k0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.197mjg5bagv4.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2rzuypuekkq0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1atbqizemeg0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6eritbo4u480.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.opfw5lw2rmo.png)

###  1.4. <a name='PrometheusGrafanaNodeManagerPushgatewayFlink'></a>Prometheus + Grafana + NodeManager + Pushgateway 打造企业级 Flink 平台监控系统

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6lelctotyfc0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5noflxpwiss0.png)

flink.yaml 文件的配置

```yaml
metrics.reporter.promgateway.class: org.apache.flink.metrics.prometheus.PrometheusPushGatewayReporter

metrics.reporter.promgateway.host: 192.168.244.129
metrics.reporter.promgateway.port: 9091
metrics.reporter.promgateway.jobName: myJob
metrics.reporter.promgateway.randomJobNameSuffix: true
metrics.reporter.promgateway.deleteOnShutdown: false
metrics.reporter.promgateway.groupingKey: k1=v1;k2=v2
metrics.reporter.promgateway.interval: 60 SECONDS
```

```yaml
metrics.reporters: promgateway
metrics.reporter.promgateway.class: org.apache.flink.metrics.prometheus.PrometheusPushGatewayReporter
metrics.reporter.promgateway.host: datanode01
metrics.reporter.promgateway.port: 9100
metrics.reporter.promgateway.jobName: flink-metrics
```

```yaml
metrics.reporter.promgateway.class: org.apache.flink.metrics.prometheus.PrometheusPushGatewayReporter
# 这里写PushGateway的主机名与端口号
metrics.reporter.promgateway.host: ubuntu01
metrics.reporter.promgateway.port: 9091
# Flink metric在前端展示的标签（前缀）与随机后缀
metrics.reporter.promgateway.jobName: flinkjobs
metrics.reporter.promgateway.randomJobNameSuffix: false
metrics.reporter.promgateway.deleteOnShutdown: true
```

```yaml
metrics.reporter.promgateway.class: org.apache.flink.metrics.prometheus.PrometheusPushGatewayReporter
metrics.reporter.promgateway.host: localhost
metrics.reporter.promgateway.port: 9091
metrics.reporter.promgateway.jobName: zhisheng
metrics.reporter.promgateway.randomJobNameSuffix: true
metrics.reporter.promgateway.deleteOnShutdown: false
```

prometheus.yml 中的配置：

```yml
scrape_configs:
  - job_name: 'prometheus'
    static_configs:
      - targets: ['192.168.244.129:9090']
        labels:
          instance: 'prometheus'
  - job_name: 'linux'
    static_configs:
      - targets: ['192.168.244.129:9100']
        labels:
          instance: 'localhost'
  - job_name: 'pushgateway'
    static_configs:
      - targets: ['192.168.244.129:9091']
        labels:
          instance: 'pushgateway'
```

```yml
scrape_configs:
  - job_name: 'prometheus'
    static_configs:
      - targets: ['localhost:9090']
        labels:
          instance: 'prometheus'
  - job_name: 'linux'
    static_configs:
      - targets: ['localhost:9100']
        labels:
          instance: 'localhost'
  - job_name: 'pushgateway'
    static_configs:
      - targets: ['localhost:9091']
        labels:
          instance: 'pushgateway'
```

主要添加 PushGateway 和 PushGateway 的监控配置,这里还修改了 prometheus 抓取数据的间隔,可以不用改.

```yml
# my global config
global:
  scrape_interval:     15s # Set the scrape interval to every 15 seconds. Default is every 1 minute.
  evaluation_interval: 15s # Evaluate rules every 15 seconds. The default is every 1 minute.
  # scrape_timeout is set to the global default (10s).

# Alertmanager configuration
alerting:
  alertmanagers:
  - static_configs:
    - targets:
      # - alertmanager:9093

# Load rules once and periodically evaluate them according to the global 'evaluation_interval'.
rule_files:
  # - "first_rules.yml"
  # - "second_rules.yml"

# A scrape configuration containing exactly one endpoint to scrape:
# Here it's Prometheus itself.

scrape_configs:
  - job_name: 'prometheus'
    static_configs:
      - targets: ['storm1:9090']
        labels:
          instance: 'prometheus'
  - job_name: 'pushgateway'
    static_configs:
      - targets: ['storm1:9091']
        labels:
          instance: 'pushgateway'
```

启动：

```sh
./prometheus --config.file=prometheus.yml
```

```sh
nohup ./prometheus --config.file=prometheus.yml --storage.tsdb.retention=1d &
nohup ./pushgateway &
```

启动完后，可以通过 ps 查看一下端口

```sh
ps aux|grep prometheus
```

界面如下：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.35t79jifvl80.png)

然后访问一下 prometheus 的 <http://storm1:9090/targets> 页面,如下图所示如果 state 显示为 up 就说明配置成功了.

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4nb5of0lo1e0.png)

pushgateway 不需要任何配置,直接启动即可,访问 <http://storm1:9091/># 可以看到已经收到了 Flink Metric Reporter 推送过来的数据.

然后我们把 Flink 集群、nodeManager、pushGateway、Prometheus、Grafana 分别启动起来。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4pbto47mbwq0.png)

####  1.4.1. <a name='PrometheusReporter'></a>利用 PrometheusReporter 获取监控数据

要使用该 reporter 的话，需要将 opt 目录下的 flink-metrics-prometheus-1.9.0.jar 依赖放到 lib 目录下

可以配置的参数有：

- port：该参数为可选项，Prometheus 监听的端口，默认是 9249，和上面使用 JMXReporter 一样，如果是在一台服务器上既运行了 JobManager，又运行了 TaskManager，则使用端口范围，比如 9249-9259。

- filterLabelValueCharacters：该参数为可选项，表示指定是否过滤标签值字符，如果开启，则删除所有不匹配 [a-zA-Z0-9:_] 的字符，否则不会删除任何字符。

在 flink-conf.yaml 中配置的：

```yaml
metrics.reporters: prom
metrics.reporter.prom.class: org.apache.flink.metrics.prometheus.PrometheusReporter
metrics.reporter.prom.port: 9999
```

####  1.4.2. <a name='PrometheusPushGatewayReporter'></a>利用 PrometheusPushGatewayReporter 获取监控数据

PushGateway 是 Prometheus 生态中一个重要工具，使用它的原因主要是：

- Prometheus 采用 pull 模式，如果让 Prometheus Server 去每个节点拉数据，那么监控服务的压力就会很大，我们是在监控几千个实例的情况下做到 10s 的采集间隔。

- Prometheus是从pushgateway拉取数据的，但是flink on yarn作业的每一个任务对应的集群不一样，地址不一样，那么对于Prometheus这样一个主动拉取的角色，就必须借助一个固定地址的数据中转站来进行数据的获取，pushgateway就具有类似的功能

- 那么使用 PushGateway 的话，该 reporter 会定时将 metrics 数据 push 到 PushGateway，然后再由 Prometheus Server 去 pull 这些 metrics 数据。

- 这样在 Prometheus Server 在写入性能满足的情况下，单台机器就可以承载整个系统的监控数据。

- 考虑到跨机房采集监控数据的问题，可以在每个机房都部署 Pushgateway 节点，同时还能缓解单个 Pushgateway 的压力。

Prometheus Server定义：

- Prometheus Server 去 Pushgateway 上面拉数据的时间间隔设置为 10s。
- 多个 Pushgateway 的情况下，就配置多个组即可。
- 为了确保 Prometheus Server 的高可用，可以再加一个 Prometheus Server 放到异地容灾机房，配置和前面的 Prometheus Server 一样。
- 如果监控需要保留时间长的话，也可以配置一个采集间隔时间较大的 Prometheus Server，比如 5 分钟一次，数据保留 1 年。

下载pushGateway的安装包

```sh
wget https://github.com/prometheus/pushgateway/releases/download/v1.4.1/pushgateway-1.4.1.darwin-amd64.tar.gz

tar zxvf  pushgateway-1.4.1.linux-amd64.tar.gz
```

进入解压后的目录并且启动pushgateway：

```sh
./pushgateway &
```

查看是否在后台启动成功：

```sh
ps aux|grep pushgateway
```

启动服务：./node_exporter &

将node_exporter添加到Prometheus服务器，我们请求一下本地的<http://localhost:9100/metrics> 可以看到当前机器的一些指标

如果使用 PrometheusPushGatewayReporter 收集数据的话，也是需要将 opt 目录下的 flink-metrics-prometheus-1.9.0.jar 依赖放到 lib 目录下的。

首先我们根据flink版本，获取对应的flink-metrics包。

```sh
wget https://repo1.maven.org/maven2/org/apache/flink/flink-metrics-prometheus_2.11/1.13.1/flink-metrics-prometheus_2.11-1.13.1.jar
mv flink-metrics-prometheus_2.11-1.13.1.jar flink-1.13.1/lib
```

pushgateway启动

```sh

nohup ./pushgateway --web.listen-address :9091 &
```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.42ixgyy12mc0.png)

可配置的参数有：

- deleteOnShutdown：默认值是 true，表示是否在关闭时从 PushGateway 删除指标。
- filterLabelValueCharacters：默认值是 true，表示是否过滤标签值字符，如果开启，则不符合 [a-zA-Z0-9:_] 的字符都将被删除。
- host：无默认值，配置 PushGateway 服务所在的机器 IP。
- jobName：无默认值，要上报 Metrics 的 Job 名称。
- port：默认值是 -1，这里配置 PushGateway 服务的端口。
- randomJobNameSuffix：默认值是 true，指定是否将随机后缀名附加到作业名。

####  1.4.3. <a name='Flink'></a>提交 Flink 任务

```sh
flink run -d -yqu flink -m yarn-cluster \
-nm test10 \
-p 4 \
-yD metrics.reporter.promgateway.groupingKey="jobname=test10" \
-yD metrics.reporter.promgateway.jobName=test10 \
-c flink.streaming.FlinkStreamingFlatMapDemoNew \
/home/jason/bigdata/jar/flink-1.13.0-1.0-SNAPSHOT.jar
```

启动Flink集群

```sh
./start-cluster.sh
```

执行一下JPS，我们看到集群已经启动起来了。

启动Flink sql 客户端

```sh
./sql-client.sh embedded
```

定义Source

```sql
CREATE TABLE prometheusdatagen (
        f_sequence INT,
        f_random INT,
        f_random_str STRING,
        ts AS localtimestamp,
        WATERMARK FOR ts AS ts
) WITH (
        'connector' = 'datagen',
        'rows-per-second'='5',
        'fields.f_sequence.kind'='sequence',
        'fields.f_sequence.start'='1',
        'fields.f_sequence.end'='100000',
        'fields.f_random.min'='1',
        'fields.f_random.max'='1000',
        'fields.f_random_str.length'='10'
        );
```

执行：

```sql
select * from prometheusdatagen。
```

进入Flink UI可以看到，这个JOB已经启动起来了。

访问<http://localhost:9091/metrics，可以看到Flink已经成功将Metrics推送到了pushgateway>。

从FlinkUI 的Task Managers的logs可以看到Flink已经获取reporter的配置信息，并且启动了TaskManager

##  2. <a name='-1'></a>监控指标

[官方指标](https://nightlies.apache.org/flink/flink-docs-release-1.14/zh/docs/ops/metrics/#end-to-end-latency-tracking)

[官方配置参数](https://nightlies.apache.org/flink/flink-docs-release-1.14/zh/docs/deployment/config/#metrics-latency-interval)

###  2.1. <a name='FlinkMetrics'></a>Flink Metrics 简介

http://localhost:8081/overview

四种监控指标

分别为 Counter、Gauge、Histogram、Meter。

> Counter 累加值

对一个计数器进行累加

如🥝：Flink 算子的`接收记录总数 (numRecordsIn)` 和`发送记录总数 (numRecordsOut)` 属于 Counter 类型。

可以使用 `inc()/inc(long n)` 或 `dec()/dec(long n)` 更新（`增加`或`减少`）计数器。

```scala
      .counter("myCounter")
```

```scala
class MyMapper extends RichMapFunction[String,String] {
  @transient private var counter: Counter = _

  override def open(parameters: Configuration): Unit = {
      // 使用默认 Counter 实现
    counter = getRuntimeContext()
      .getMetricGroup()
      .counter("myCounter")
  }

  override def map(value: String): String = {
    counter.inc()
    value
  }
}
```

也可以使用自己的 Counter 实现：

```scala
class MyMapper extends RichMapFunction[String,String] {
  @transient private var counter: Counter = _

  override def open(parameters: Configuration): Unit = {
      // 使用自定义 Counter 实现
    counter = getRuntimeContext()
      .getMetricGroup()
      .counter("myCustomCounter", new CustomCounter())
  }

  override def map(value: String): String = {
    counter.inc()
    value
  }
}
```

> Gauge 瞬时值

Gauge 是最简单的 Metrics ，它反映一个指标的瞬时值

如🥝：TaskManager 的 JVM heap 内存用了多少，就可以每次实时的暴露一个 Gauge

首先创建一个实现 org.apache.flink.metrics.Gauge 接口的类

```scala
      .gauge("MyGauge", new Gauge<Integer>() {}
```

```scala
new class MyMapper extends RichMapFunction[String,String] {
  @transient private var valueToExpose = 0

  override def open(parameters: Configuration): Unit = {
    getRuntimeContext()
      .getMetricGroup()
      .gauge[Int, ScalaGauge[Int]]("MyGauge", ScalaGauge[Int]( () => valueToExpose ) )
  }

  override def map(value: String): String = {
    valueToExpose += 1
    value
  }
}
```

> Meter 平均值

一个指标在某个时间段内的平均值

如🥝：Task 算子中的 numRecordsInPerSecond,记录此 Task 或者算子每秒接收的记录数

```scala
      .meter("myMeter", new MyMeter())
```

```scala
class MyMapper extends RichMapFunction[Long,Long] {
  @transient private var meter: Meter = _

  override def open(config: Configuration): Unit = {
    meter = getRuntimeContext()
      .getMetricGroup()
      .meter("myMeter", new MyMeter())
  }

  override def map(value: Long): Long = {
    meter.markEvent()
    value
  }
}
```

Flink 提供了一个允许使用 Codahale / DropWizard 表的 Wrapper，添加以下依赖项：

```scala
<dependency>
      <groupId>org.apache.flink</groupId>
      <artifactId>flink-metrics-dropwizard</artifactId>
      <version>1.6.1</version>
</dependency>
```

代码如下：

```scala
class MyMapper extends RichMapFunction[Long,Long] {
  @transient private var meter: Meter = _

  override def open(config: Configuration): Unit = {
    com.codahale.metrics.Meter dropwizardMeter = new com.codahale.metrics.Meter()

    meter = getRuntimeContext()
      .getMetricGroup()
      .meter("myMeter", new DropwizardMeterWrapper(dropwizardMeter))
  }

  override def map(value: Long): Long = {
    meter.markEvent()
    value
  }
}
```

> Histogram 直方图

Histogram 用于统计一些数据的分布

如🥝：Quantile、Mean、StdDev、Max、Min 等，其中最重要一个是统计算子的延迟。

此项指标会记录数据处理的延迟信息，对任务监控起到很重要的作用。

```scala
      .histogram("myHistogram", new MyHistogram())
```

```scala
class MyMapper extends RichMapFunction[Long,Long] {
  @transient private var histogram: Histogram = _

  override def open(parameters: Configuration): Unit = {
    histogram = getRuntimeContext()
      .getMetricGroup()
      .histogram("myHistogram", new MyHistogram())
  }

  override def map(value: Long): Long = {
    histogram.update(value)
    value
  }
}
```

Flink 没有提供默认 Histogram 实现 ，但提供了一个允许使用 Codahale / DropWizard 直方图的包装类（Wrapper），添加以下依赖项：

```xml
<dependency>
      <groupId>org.apache.flink</groupId>
      <artifactId>flink-metrics-dropwizard</artifactId>
      <version>1.6.1</version>
</dependency>
```

代码如下：

```scala
class MyMapper extends RichMapFunction[Long, Long] {
  @transient private var histogram: Histogram = _

  override def open(config: Configuration): Unit = {
    com.codahale.metrics.Histogram dropwizardHistogram =
      new com.codahale.metrics.Histogram(new SlidingWindowReservoir(500))

    histogram = getRuntimeContext()
      .getMetricGroup()
      .histogram("myHistogram", new DropwizardHistogramWrapper(dropwizardHistogram))
  }

  override def map(value: Long): Long = {
    histogram.update(value)
    value
  }
}
```

> 以算子的指标组结构为例，其默认为：

```s
 <host> .taskmanager.<tm_id> .<job_name>.<operator_name>.<subtask_index>
 hlinkui.taskmanager.1234    .wordcount .flatmap        .0.numRecordsIn
```

###  2.2. <a name='JobManager'></a>监控 JobManager

http://localhost:8081/#/job-manager/config 页面可以看到可 JobManager 的配置信息

http://localhost:8081/jobmanager/log 页面可以查看 JobManager 的日志详情

这几个指标可以重点关注:

- TaskManager 个数：如果出现 `TaskManager 突然减少`，可能是因为有 TaskManager 挂掉重启，一旦该 TaskManager 之前运行了很多作业，那么重启带来的影响必然是巨大的。

- Slot 个数：取决于 TaskManager 的个数，决定了能运行作业的最大并行度，`如果资源不够，及时扩容。`

- 作业运行时间：根据`作业的运行时间`来判断作业`是否存活`，中途是否掉线过。

- Checkpoint 情况：Checkpoint 是 JobManager 发起的，并且关乎到作业的状态是否可以完整的保存。


####  2.2.1. <a name='-1'></a>基础指标

> 内存：

内存又分堆内存和非堆内存，在 Flink 中还有 Direct 内存，每种内存又有初始值、使用值、最大值等指标，因为在 JobManager 中的工作其实相当于 TaskManager 来说比较少，也不存储事件数据，所以通常 `JobManager 占用的内存不会很多`，在 Flink JobManager 中自带的内存 Metrics 指标有：

```s
jobmanager_Status_JVM_Memory_Direct_Count
jobmanager_Status_JVM_Memory_Direct_MemoryUsed
jobmanager_Status_JVM_Memory_Direct_TotalCapacity
jobmanager_Status_JVM_Memory_Heap_Committed
jobmanager_Status_JVM_Memory_Heap_Max
jobmanager_Status_JVM_Memory_Heap_Used
jobmanager_Status_JVM_Memory_Mapped_Count
jobmanager_Status_JVM_Memory_Mapped_MemoryUsed
jobmanager_Status_JVM_Memory_Mapped_TotalCapacity
jobmanager_Status_JVM_Memory_NonHeap_Committed
jobmanager_Status_JVM_Memory_NonHeap_Max
jobmanager_Status_JVM_Memory_NonHeap_Used
```

> CPU：

JobManager 分配的 CPU 使用情况，如果使用类似 K8S 等资源调度系统，则需要对每个容器进行设置资源，比如 CPU 限制不能超过多少，在 Flink JobManager 中自带的 CPU 指标有：

```s
jobmanager_Status_JVM_CPU_Load
jobmanager_Status_JVM_CPU_Time
```

> GC：

GC 信息对于 Java 应用来说是避免不了的，每种 GC 都有时间和次数的指标可以供参考

```s

jobmanager_Status_JVM_GarbageCollector_PS_MarkSweep_Count
jobmanager_Status_JVM_GarbageCollector_PS_MarkSweep_Time
jobmanager_Status_JVM_GarbageCollector_PS_Scavenge_Count
jobmanager_Status_JVM_GarbageCollector_PS_Scavenge_Time
```

####  2.2.2. <a name='Checkpoint'></a>Checkpoint 指标

因为 JobManager 负责了作业的 Checkpoint 的协调和发起功能，所以 Checkpoint 相关的指标就有表示:

- Checkpoint 执行的时间、
- Checkpoint 的时间长短、
- 完成的 Checkpoint 的次数、
- Checkpoint 失败的次数、
- Checkpoint 正在执行 Checkpoint 的个数
  
  其对应的指标如下：

```s
jobmanager_job_lastCheckpointAlignmentBuffered
jobmanager_job_lastCheckpointDuration
jobmanager_job_lastCheckpointExternalPath
jobmanager_job_lastCheckpointRestoreTimestamp
jobmanager_job_lastCheckpointSize
jobmanager_job_numberOfCompletedCheckpoints
jobmanager_job_numberOfFailedCheckpoints
jobmanager_job_numberOfInProgressCheckpoints
jobmanager_job_totalNumberOfCheckpoints
```

####  2.2.3. <a name='-1'></a>重要的指标

另外还有比较重要的指标就是 Flink UI 上也提供的，类似于:

- Slot 总共个数、
- Slot 可使用的个数、
- TaskManager 的个数（通过查看该值可以知道是否有 TaskManager 发生异常重启）、
- 正在运行的作业数量、
- 作业运行的时间和完成的时间、
- 作业的重启次数
  
  对应的指标如下：

```s

jobmanager_job_uptime
jobmanager_numRegisteredTaskManagers
jobmanager_numRunningJobs
jobmanager_taskSlotsAvailable
jobmanager_taskSlotsTotal
jobmanager_job_downtime
jobmanager_job_fullRestarts
jobmanager_job_restartingTime
```

###  2.3. <a name='TaskManager'></a>监控 TaskManager

http://localhost:8081/taskmanagers

http://localhost:8081/taskmanagers/tm_id 页面查看 TaskManager 的具体情况（这里的 tm_id 是个随机的 UUID 值）。可以查看:

- JVM（堆和非堆）、Direct 内存、
  - 很多时候 TaskManager 频繁重启的原因就是 JVM 内存设置得不合理，导致频繁的 GC，最后使得 OOM 崩溃，不得不重启。
- 网络、
- GC 次数和
- 时间

http://localhost:8081/taskmanagers/tm_id/log 就可以查看该 TaskManager 的日志

- 😁如果你的 Job 在多个 TaskManager 上运行，那么日志就会在多个 TaskManager 中打印出来。
- 😁如果一个 TaskManager 中运行了多个 Job，那么它里面的日志就会很混乱，查看日志时会发现它为什么既有这个 Job 打出来的日志，又有那个 Job 打出来的日志

😁所以，有人😁希望日志可以是 Job 与 Job 之间的隔离，这样日志更方便采集和查看，对于排查问题也会更快。对此国内有公司也对这一部分做了改进
  
🥝重点关注：

- 内存使用率：部分作业的算子会将所有的 State 数据存储在内存中，这样就会导致 TaskManager 的内存使用率会上升，还有就是可以根据该指标看作业的利用率，从而最后来重新划分资源的配置。
- GC 情况：分时间和次数，一旦 TaskManager 的内存率很高的时候，必定伴随着`频繁的 GC`，如果在 GC 的时候没有得到及时的预警，那么将面临 OOM 风险。

TaskManager 在 Flink 集群中也是一个个的进程实例，它的数量代表着能够运行作业个数的能力，所有的 Flink 作业最终其实是会在 TaskManager 上运行的。

包括了：

- Task 的启动销毁、
- 内存管理、
- 磁盘 IO、
- 网络传输管理

就会消耗很大的资源，所以通常来说 TaskManager 要比 JobManager 消耗的资源要多

一旦 TaskManager 因为各种问题导致崩溃重启的话，运行在它上面的 Task 也都会失败，JobManager 与它的通信也会丢失。因为作业出现 failover，所以在重启这段时间它是不会去消费数据的，所以必然就会出现数据消费延迟的问题。

TaskManager Metrics 指标如下：

```s
taskmanager_Status_JVM_CPU_Load
taskmanager_Status_JVM_CPU_Time
taskmanager_Status_JVM_ClassLoader_ClassesLoaded
taskmanager_Status_JVM_ClassLoader_ClassesUnloaded
taskmanager_Status_JVM_GarbageCollector_G1_Old_Generation_Count
taskmanager_Status_JVM_GarbageCollector_G1_Old_Generation_Time
taskmanager_Status_JVM_GarbageCollector_G1_Young_Generation_Count
taskmanager_Status_JVM_GarbageCollector_G1_Young_Generation_Time
taskmanager_Status_JVM_Memory_Direct_Count
taskmanager_Status_JVM_Memory_Direct_MemoryUsed
taskmanager_Status_JVM_Memory_Direct_TotalCapacity
taskmanager_Status_JVM_Memory_Heap_Committed
taskmanager_Status_JVM_Memory_Heap_Max
taskmanager_Status_JVM_Memory_Heap_Used
taskmanager_Status_JVM_Memory_Mapped_Count
taskmanager_Status_JVM_Memory_Mapped_MemoryUsed
taskmanager_Status_JVM_Memory_Mapped_TotalCapacity
taskmanager_Status_JVM_Memory_NonHeap_Committed
taskmanager_Status_JVM_Memory_NonHeap_Max
taskmanager_Status_JVM_Memory_NonHeap_Used
taskmanager_Status_JVM_Threads_Count
taskmanager_Status_Network_AvailableMemorySegments
taskmanager_Status_Network_TotalMemorySegments
taskmanager_Status_Shuffle_Netty_AvailableMemorySegments
taskmanager_Status_Shuffle_Netty_TotalMemorySegments
```

###  2.4. <a name='Flinkjob'></a>监控 Flink 作业 job

http://localhost:8081/jobs/job_id 页面可以查看 Job 的监控数据，包括了：

- Task 数据、
- Operator 数据、
- Exception 数据、
- Checkpoint 数据等

Flink UI 上也是有提供的查看对应的信息的：

- 状态、
- Bytes Received（接收到记录的容量大小）、
- Records Received（接收到记录的条数）、
- Bytes Sent（发出去的记录的容量大小）、
- Records Sent（发出去记录的条数）、
- 异常信息、
- timeline（作业运行状态的时间线）、
- Checkpoint 信息

```s
taskmanager_job_task_Shuffle_Netty_Input_Buffers_outPoolUsage
taskmanager_job_task_Shuffle_Netty_Input_Buffers_outputQueueLength
taskmanager_job_task_Shuffle_Netty_Output_Buffers_inPoolUsage
taskmanager_job_task_Shuffle_Netty_Output_Buffers_inputExclusiveBuffersUsage
taskmanager_job_task_Shuffle_Netty_Output_Buffers_inputFloatingBuffersUsage
taskmanager_job_task_Shuffle_Netty_Output_Buffers_inputQueueLength
taskmanager_job_task_Shuffle_Netty_Output_numBuffersInLocal
taskmanager_job_task_Shuffle_Netty_Output_numBuffersInLocalPerSecond
taskmanager_job_task_Shuffle_Netty_Output_numBuffersInRemote
taskmanager_job_task_Shuffle_Netty_Output_numBuffersInRemotePerSecond
taskmanager_job_task_Shuffle_Netty_Output_numBytesInLocal
taskmanager_job_task_Shuffle_Netty_Output_numBytesInLocalPerSecond
taskmanager_job_task_Shuffle_Netty_Output_numBytesInRemote
taskmanager_job_task_Shuffle_Netty_Output_numBytesInRemotePerSecond
taskmanager_job_task_buffers_inPoolUsage
taskmanager_job_task_buffers_inputExclusiveBuffersUsage
taskmanager_job_task_buffers_inputFloatingBuffersUsage
taskmanager_job_task_buffers_inputQueueLength
taskmanager_job_task_buffers_outPoolUsage
taskmanager_job_task_buffers_outputQueueLength
taskmanager_job_task_checkpointAlignmentTime
taskmanager_job_task_currentInputWatermark
taskmanager_job_task_numBuffersInLocal
taskmanager_job_task_numBuffersInLocalPerSecond
taskmanager_job_task_numBuffersInRemote
taskmanager_job_task_numBuffersInRemotePerSecond
taskmanager_job_task_numBuffersOut
taskmanager_job_task_numBuffersOutPerSecond
taskmanager_job_task_numBytesIn
taskmanager_job_task_numBytesInLocal
taskmanager_job_task_numBytesInLocalPerSecond
taskmanager_job_task_numBytesInPerSecond
taskmanager_job_task_numBytesInRemote
taskmanager_job_task_numBytesInRemotePerSecond
taskmanager_job_task_numBytesOut
taskmanager_job_task_numBytesOutPerSecond
taskmanager_job_task_numRecordsIn
taskmanager_job_task_numRecordsInPerSecond
taskmanager_job_task_numRecordsOut
taskmanager_job_task_numRecordsOutPerSecond
taskmanager_job_task_operator_currentInputWatermark
taskmanager_job_task_operator_currentOutputWatermark
taskmanager_job_task_operator_numLateRecordsDropped
taskmanager_job_task_operator_numRecordsIn
taskmanager_job_task_operator_numRecordsInPerSecond
taskmanager_job_task_operator_numRecordsOut
taskmanager_job_task_operator_numRecordsOutPerSecond
```

作业的状态：在 UI 上是可以看到作业的状态信息，常见的状态变更信息如下图所示: 

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.20h0lu1wmuqo.png)

- Task 的状态：其实导致作业的状态发生变化的原因通常是由于 Task 的运行状态出现导致，所以也需要对 Task 的运行状态进行监控，Task 的运行状态如下图所示。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2z1p7mcz65g0.png)

- 作业异常日志：导致 Task 出现状态异常的根因通常是`作业中的代码出现各种各样的异常日志`，最后可能还会导致作业无限重启，所以作业的异常日志也是需要及时关注。

- 作业重启次数：当 Task 状态和作业的状态发生变化的时候，如果作业中配置了重启策略或者开启了 Checkpoint 则会进行作业重启的，`重启作业的带来的影响也会很多`，并且会伴随着一些不确定的因素，最终导致作业一直重启，这样既不能解决问题，还一直在占用着资源的消耗。

- 算子的消费速度：代表了作业的消费能力，还可以知道`作业是否发生延迟`，可以包含`算子接收的数据量`和`发出去数据量`，从而可以知道在算子处是否有发生数据的丢失。


###  2.5. <a name='-1'></a>最关心的性能指标

| 指标名称 | 指标说明 |
|---|---|
| numRecordsInPerSecond | 每秒输入记录数 |
| numRecordsOutPerSecond | 每秒输出记录数 |
| numBytesInPerSecond | 每秒输入字节数 |
| numBytesOutPerSecond | 每秒输出字节数 |
| numBuffersInPerSecond | 每秒输入缓冲区数 |
| numBuffersOutPerSecond | 每秒输出缓冲区数 |
| currentInputWatermark | 当前输入水位线 |
| currentOutputWatermark | 当前输出水位线 |
| numRunningJobs | 正在运行的作业数 |
| taskSlotsAvailable | 可用的任务槽数 |
| taskSlotsTotal | 总共的任务槽数 |
| numRegisteredTaskManagers | TaskManager注册数 |
| numRestarts | 当前指标所在作业重启次数 |
| fullRestarts | 作业重启次数 |
| Used | 已使用的内存 |
| Committed | 已提交的内存 |
| Max | 最大内存数 |
| isBackPressured | 是否存在分压 |
| checkpointAlignmentTime | 检查点对齐时间 |
| checkpointStartDelayNanos | 检查点启动延迟纳秒数 |

####  2.5.1. <a name='-1'></a>系统指标

> 作业的可用性

- uptime (作业持续运行的时间)
- fullRestarts (作业重启的次数)

作用：

- 常驻 job 数量的监控
- 及时发现 job 运行过程中的重启，失败问题

> 作业的流量

- 算子消息处理的 numRecordsIn、numBytesInLocal 等相关指标
- 作业每天处理的消息数目、作业每天处理的消息数目
- numRecordsOut，numRecordsIn 每个度量标准都带有两个范围：一个范围为运算符，另一个范围为子任务。对于网络监控，子任务范围的度量标准是相关的，并显示它已发送/接收的记录总数。
- 您可能需要进一步查看这些数字以提取特定时间跨度内的记录数或使用等效的... PerSecond指标。
- numBytesOut，numBytesInLocal，numBytesInRemote 此子任务从本地/远程源发出或读取的总字节数。这些也可以通过... PerSecond指标作为meters。
- numBuffersOut，numBuffersInLocal，numBuffersInRemote 与numBytes类似...但计算网络缓冲区的数量。

作用：

- 线图趋势掌握任务处理的负载量
- 及时发现job资源分配是否合理，尽量避免消息波动带来的系统延迟增高

> CPU

- CPU.Load
- 内存（如：Heap.Used ）
- GC (如：GarbageCollector.Count、GarbageCollector.Time)
- 网络 ( inputQueueLength、outputQueueLength)
- outputQueueLength和inputQueueLength分别显示发送方子任务的输出队列和接收方子任务的输入队列中的缓冲区数量。
- 🥝!!!总的来说，我们不鼓励使用outputQueueLength和inputQueueLength，因为它们的解释很大程度上取决于运算符的当前并行性以及配置的独占缓冲区和浮动缓冲区的数量。
- 🥝!!!相反，我们建议使用各种 `PoolUsage 指标`，这些指标甚至可以揭示更详细的见解。
- 🥝!!!如果`inPoolUsage持续100％`左右，这是上行背压的强有力指标。
- taskmanager 的内存，GC 状态的线图波动。
- 及时发现系统中资源的利用率，合理分配集群资源。

> checkpoint

- 最近完成的 checkpoint 的时长（ lastCheckpointDuration ）
- 最近完成的 checkpoint 的大小（ lastCheckpointSize ）
- 作业失败后恢复的能力（ lastCheckpointRestoreTimestamp ）
- 成功和失败的 checkpoint 数目（ numberOfCompletedCheckpoints、numberOfFailedCheckpoints ）
- 在 Exactly once 模式下 barrier 对齐时间（ checkpointAlignmentTime ）

> connector 的指标

- 常用的 Kafka connector ，Kafka 自身提供了一些指标，可以帮助我们了解到作业最新消费的消息的状况、作业是否有延迟等。
- 通过监控 kafka consumer group 的 lagOffset 来发现flow 的数据消费能力是否有降低。
- 可以结合 `Kafka 的监控`去查看对应消费的 Topic 的 Group 的 `Lag 信息`，如果 Lag 很大就表明有数据堆积了

> 消息延迟监控

- Flink 算子之间消息传递的最大，最小，平均延迟。
- 及时发现任务消息的处理效率波动

> 缓冲区

- outPoolUsage，inPoolUsage，floatingBuffersUsage，exclusiveBuffersUsage 估计使用的缓冲区与各个本地缓冲池中可用的缓冲区的比率
- inPoolUsage = floatingBuffersUsage + exclusiveBuffersUsage

####  2.5.2. <a name='-1'></a>自定义指标

!!!🚩注意：指标这个东西本身不应该影响你作业本身的处理逻辑，监控应该是一个比较外围的东西。

自定义指标是指用户可以在自己的作业逻辑中进行埋点，这样可以对自己的业务逻辑进行监控。

- 比如处理`逻辑耗时打点`，可以通过在`逻辑前后进行打点`，这样可以查看每条消息处理完这个逻辑的耗时。

- 另一块是`外部服务调用`的性能，（如 Redis ）, 可以通过`打点`来查看`请求的耗时`、`请求的成功率`等。

- 还有是`缓存命中率`，有时候由于数据集过大，我们只访问`热数据`，此时会在`内存中缓存一部分信息`，我们可以监控`缓存命中率`，如果`缓存命中率`非常高说明缓存有效，如果`缓存命中率`非常低，一直在访问外部存储，就需要考虑缓存设计的是否合理。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2ushnh1fj0o0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2eij48ga8pq8.png)

举个栗子🌰

- Sink 端我们自己实现了 clickhouse，hbase，hive，kafka 等多端输出，为了避免 Flink 的流式处理对 Sink 终端造成过大的写入压力，我们抽象了一个批次的 `buffer cache`。
- 当数据的`批次达到了阀值`，或者 `buffer cache 一定的时间间隔`，就将 buffer cache 内的数据一次性 `doFlush 到各端存储`, 各个 sink实例 只需实现`BucketBufferedSink.doFlush` 方法

由于 Sink 过程中，可能面临部分 buffer cache 中的数据在 flush 过程中因为某种原因失败而导致`数据丢失`，所以必须要及时发现数据不一致，以便重跑任务恢复数据。我们在 BucketBufferedSink 之上抽象了 `SinkMetric`，并在 `BucketBufferedSink.addBuffer()` 做了 `sinkPushCounter.inc 埋点计数`, BucketBufferedSink.flush() 做了  `sinkFlushCounter.inc()`

- sinkPushCounter 统计`进入到 buffercache` 的数据条数

- sinkFlushCounter 统计 `buffercache flush 出去`的数据条数 

###  2.6. <a name='-1'></a>指标的聚合方式

> 作业的聚合维度:

- 细粒度的如 Task、Operator 维度 => 要做性能测试则需要细粒度的查询，如 task 粒度。
- 大点的粒度如 Job、机器、集群或者是业务维度（如每个区域）=> 如果想看全局的现状则需要比较粗的粒度。

查问题时从大的粒度着手，向细粒度进行排查。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6grsml4ook80.png)

> 聚合的方式:

1. 总和、均值、最大值、最小值、变化率:

- 消除`统计误差`，对数据取`移动平均`或者`固定时间`的平均来使曲线变得更加平滑。

2. 差值:

- `上游数据量`与`下游数据量`的差值
- `最新 offset` 与`消费的 offset` 的差值

3. 衡量 xx 率、xx 耗时可以使用 99 线

关注作业的指标收集是否正常:

- 监测是否存在指标丢失?
- 是单个指标丢失还是整个作业的指标丢失?

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4vu9lz7upaw0.png)

###  2.7. <a name='-1'></a>作业异常报警

!!!🚩注意：报警系统本身的稳定性，放到第1位

`作业状态异常`：包括作业任务的异常状态如 `failing`，也包括 `uptime` 等指标的异常。

作业`无指标上报`：作业`无指标上报`会给作业的负责人发报警；当上报的作业`多到一定程度`了，达到预值的时候会直接给平台的管理员发报警。

指标达到`阈值`：是大家最常用的报警项。比如：

- 处理量跌0
- 消费延迟（`落后一定数量`、`持续一定时间`）
- `失败率`、`丢失率`等

个性化：实时计算平台中有很多类任务，不同的任务它会有不同的特性。比如：

- 报警时段：不同的时间段报警，可能需要有`不同的域值`，或者不同的报警方式（公司通讯软件报警、电话报警等）

- 聚合方式：不同的业务可能会有`不同的报警`的聚合的方式，这个也是需要尽量的兼容的。

错误日志、关键词日志：当`错误日志`到达一定量或者日志出现某关键词时，`触发报警`。

###  2.8. <a name='WhattodowithBackpressure'></a>What to do with Backpressure?

假设您确定了背压源（瓶颈）的位置，下一步就是分析为什么会发生这种情况。下面，我们列出了从更基本到更复杂的背压的一些潜在原因。我们建议首先检查基本原因，然后再深入研究更复杂的原因，并可能得出错误的结论。

还请回想一下，背压可能是暂时的，是由于数据积压等待处理而导致负载高峰，检查点或作业重启的结果。如果背压是暂时的，你应该忽略它。或者，请记住，分析和解决问题的过程可能会受到瓶颈间歇性的影响。话虽如此，这里有几件事需要检查。

系统资源:

- 首先，您应该检查被控制机器的基本资源使用情况，如CPU，网络或磁盘I / O. 如果某些资源被完全或大量使用，您可以执行以下操作之一：

  - 尝试优化您的代码。代码分析器在这种情况下很有用。

  - 针对特定的资源调优Flink。

  - 通过增加并行度和/或增加群集中的计算机数量来扩展。

垃圾收集:

- 通常，长时间GC暂停会导致性能问题。您可以通过打印调试GC日志（通过-XX：+ PrintGCDetails）或使用某些内存/ GC分析器来验证您是否处于这种情况。由于处理GC问题是高度依赖于应用程序并且独立于Flink，因此我们不会在此详细介绍（Oracle的垃圾收集调整指南或Plumbr的Java垃圾收集手册似乎是一个好的开始）。

CPU /线程瓶颈:

- 如果一个或几个线程导致CPU瓶颈而整个机器的CPU使用率仍然相对较低，则有时可能无法看到CPU瓶颈。例如，48核计算机上的单个CPU瓶颈线程将导致仅使用2％的CPU。考虑使用代码分析器，因为它们可以通过显示每个线程的CPU使用情况来识别热线程。

线程争用:

- 与上面的CPU /线程瓶颈问题类似，由于共享资源上的高线程争用，子任务可能会导致瓶颈。再次，CPU分析器是你最好的朋友！考虑在用户代码中查找同步开销/锁争用 - 尽管应该避免在用户代码中添加同步，甚至可能是危险的！还要考虑调查共享系统资源。例如，默认JVM的SSL实现可以在共享/ dev / urandom资源周围变得满足。

负载不平衡:

- 如果您的瓶颈是由数据倾斜引起的，您可以尝试通过将数据分区更改为单独的重键或通过实现本地/预聚合来删除它或减轻其影响。这份清单远非详尽无遗。通常，为了减少瓶颈并因此减少背压，首先分析它发生的位置，然后找出原因。开始推理“为什么”的最佳位置是检查哪些资源被充分利用。

##  3. <a name='-'></a>实战案例 - 金融风控场景

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.47iq3n541ym0.png)

###  3.1. <a name='-1'></a>要求

推理数据：3000w，推理字段 495 个，机器学习 Xgboost 模型字段：495，

推理时延 < 20ms

全链路耗时 < 50ms => 应该使用 Flink Metrics 的 Latency Marker 进行计算

吞吐量 > 每秒 1.2w 条  => 全链路吞吐 = 单位时间处理数据数量 / 单位时间

###  3.2. <a name='-1'></a>全链路时延计算方式

算子内处理逻辑时间 + 算子间数据传递时间 + 缓冲区内等待时间

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1jmobgl1v28w.png)

由于使用到 Lateny marker,所有需要在 flink-conf.yaml 配置参数

```yaml
metrics.latency.interval: 60
# 设置每 60ms flink 自动上报算子之间的延迟信息。
```

```yaml
metrics.latency.granularity: single
# 每个operator子任务的一个直方图
metrics.latency.granularity: operator
# （默认值）：源任务和operator子任务的每个组合的一个直方图
metrics.latency.granularity: subtask
# 源子任务和operator子任务的每个组合的一个直方图（并行度中的二次方！）
```

###  3.3. <a name='FlinkonYarn'></a>提交任务到Flink on Yarn集群

```sh

# -m jobmanager 的地址
# -yjm 1024 指定 jobmanager 的内存信息
# -ytm 1024 指定 taskmanager 的内存信息
bin/flink run \
-t yarn-per-job  \
-yjm 4096  \
-ytm  8800  \
-s 96  \
--detached  -c com.threeknowbigdata.datastream.XgboostModelPrediction \
examples/batch/WordCount.jar  \
```

提交完成后，我们通过 Flink WEBUI 可以看到 job 运行的任务结果如下：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1va4raeylda8.png)

###  3.4. <a name='Prometheus-1'></a>打开 Prometheus 在对话框输入全链路时延计算公式

```s
# 计算公式：
avg(flink_taskmanager_job_latency_source_id_
operator_id _operator_subtask_index_latency{
source_id="cbc357ccb763df2852fee8c4fc7d55f2",
operator_id="c9c0ca46716e76f6b700eddf4366d243",quantile="0.999"})
```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.43gsul4pk880.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4ywg4u8sxzs0.png)

时延指标：加`并行度`，`吞吐量`也跟随高，但是`全链路时延`大幅增长

（ 1并行至32并行，时延从 110ms 增加至 3287ms ）

###  3.5. <a name='-1'></a>问题分析

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.iuj4xgh4n20.png)

内存：对吞吐和时延没什么影响， 并行度与吞吐成正相关

- 增大 `kafka 分区`,吞吐增加
- 增大 `source`、维表 `source` 并行度
- 增大 `flatmap` 推理并行度

###  3.6. <a name='-1'></a>并行度问题

反压现象：在 Flink WEB-UI 上，可以看到应用存在着非常严重的反压，这说明链路中存在较为`耗时的算子`，阻塞了整个链路；

`数据处理`慢于拉取数据：数据源消费数据的速度，大于`下游数据处理速度`；

解决方法：

- 增加计算并行度：所以在接下来的测试中会调大推理`算子并行度`，相当于提高`下游数据处理能力`；

在代码中设置:

```scala
// 调大 SOURCE 与 COFLATMAP 的并行度比例，全链路时延可进一步降低
setParellism
```

###  3.7. <a name='Buffer'></a>Buffer 问题

> Buffer 超时越短、个数越少、时延越低。

Buffer 超时: Flink 虽是纯流式框架，但默认开启了缓存机制（上游累积部分数据再发送到下游）缓存机制可以提高应用的吞吐量，但是也增大了时延

解决方法：

- 为获取最好的时延指标，第二轮测试`超时时间`置 0，记录吞吐量

在代码中设置:

```scala
senv.setBufferTimeout(0);
```

Buffer 数量: Buffer 数量越多，能缓存的数据也就越多

解决方法：

- 减小 Flink 的 `Buffer 数量`来优化时延指标

修改flink-conf.yaml

```yaml
memory.buffers-per-channel: 2
memory.float-buffers-per-gate: 2
memory.max-buffers-per-channel: 2
```

经过修改配置后，将任务再次提交到集群后，经过全链路时延计算公式、吞吐时延计算公式，最后得到优化后的结果:

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5ucaib8c5os0.png)

时延指标：并行度提升，时延也会增加，但幅度很小（可接受）。
