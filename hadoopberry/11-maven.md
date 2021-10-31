## 安装maven

```sh
sudo apt-get install maven
```

可以为Maven配置更换国内源加速依赖文件下载，通过新增文件

```sh
sudo vim /etc/maven/setting.xml
```

添加如下内容后

```xml
<mirror>
    <id>alimaven</id>
    <name>aliyun maven</name>
    <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
    <mirrorOf>central</mirrorOf>
</mirror>
```

添加的格式参考下方：

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <mirrors>
        <!-- 阿里云 -->
        <mirror>
            <id>alimaven</id>
            <mirrorOf>central</mirrorOf>
            <name>aliyun maven</name>
            <url>http://maven.aliyun.com/nexus/content/repositories/central/</url>
        </mirror>


        <!-- 中央仓库1 -->
        <mirror>
            <id>repo1</id>
            <mirrorOf>central</mirrorOf>
            <name>Human Readable Name for this Mirror.</name>
            <url>http://repo1.maven.org/maven2/</url>
        </mirror>


        <!--中央仓库2 -->
        <mirror>
            <id>repo2</id>
            <mirrorOf>central</mirrorOf>
            <name>Human Readable Name for this Mirror.</name>
            <url>http://repo2.maven.org/maven2/</url>
        </mirror>
    </mirrors> 

</settings>
```

执行

```sh
mvn --version
```

查看是否正常

## 三套的生命周期

分别是`clean`，`default`，`site`，

分别有着不同的职责：`清理`，`构建`，`建立站点`

## scope

 | scope的分类 | 讲解  |
 |---|---|
 | compile | 默认情况下就是compile类型，意味着该依赖既要参与编译又要参与后期的测试等环节 |
 | test | 表示该依赖仅仅参加和测试有关的工作 | 
 | provided | 可以参与编译，测试，运行等周期，但是在打包的时候会进行exclude的相应操作，其他方面和compile差异不大 | 
 | runntime | 在编译环节不会参与进来，个人感觉和compile差异不大 | 
 | system | 通常是指不从仓库读取依赖，而是通过本地路径来读取依赖，因此常与systemPath标签结合使用 | 

## dependency

```xml
    <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.47</version>
        </dependency>****
```

## properties

基于properties的依赖管理，在properties里面我们可以进行整个项目的统一字符集编码管理或者对于一些依赖jar包统一版本号的管理：

```xml
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <lombok.version>1.16.6</lombok.version>
        <aspectj.version>1.8.11</aspectj.version>
        <common.version>3.4</common.version>
        <codec.version>1.10</codec.version>
        <swagger2.version>2.7.0</swagger2.version>
        <mybatis.version>3.3.0</mybatis.version>
        <mybatis-spring-boot.version>1.3.1</mybatis-spring-boot.version>
        <pagehelper.version>1.2.3</pagehelper.version>
        <springfox-swagger-ui.version>2.7.0</springfox-swagger-ui.version>
        <mysql.version>5.1.6</mysql.version>
        <mongodb.version>3.2.2</mongodb.version>
    </properties>
```

## mirrors

```xml
<mirror>
    <id>alimaven</id>
     <name>aliyun maven</name>
     <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
     <mirrorOf>central</mirrorOf>
</mirror> 
```

## profile文件

```xml
<profiles>
    <profile>
        <!-- 本地开发环境 -->
        <id>dev</id>
        <properties>
            <profiles.active>dev</profiles.active>
        </properties>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
    </profile>
    <profile>
        <!-- 测试环境 -->
        <id>test</id>
        <properties>
            <profiles.active>test</profiles.active>
        </properties>
    </profile>
    <profile>
        <!-- 生产环境 -->
        <id>pro</id>
        <properties>
            <profiles.active>pro</profiles.active>
        </properties>
    </profile>
</profiles>
```

## 常用的命令

```s
mvn archetype:create ：创建 Maven 项目 

mvn compile ：编译源代码 

mvn test-compile ：编译测试代码 

mvn test ： 运行应用程序中的单元测试 

mvn site ： 生成项目相关信息的网站 

mvn clean ：清除目标目录中的生成结果 

mvn package ： 依据项目生成 jar 文件 

mvn install ：在本地 Repository 中安装 jar
```

发布的时候可以通过命令编译jar包

```s
mvn clean package -Dmaven.test.skip=true -P prod
```

## 真实的项目都应该是分模块的

每个模块都对应着一个`pom.xml`。它们之间通过`继承和聚合`（也称作多模块，multi-module）相互关联。

对应的，在一个项目中，我们会看到一些包名：

```s
org.myorg.app.dao

org.myorg.app.service

org.myorg.app.web

org.myorg.app.util
```

### 一个简单的Maven模块结构

```s
 1---- app-parent
 2|-- pom.xml (pom)
 3|
 4|-- app-util
 5|        |-- pom.xml (jar)
 6|
 7|-- app-dao
 8|        |-- pom.xml (jar)
 9|
10|-- app-service
11|        |-- pom.xml (jar)
12|
13|-- app-web
14|-- pom.xml (war)
```

这些模块的依赖关系如下：

```s
1 app-dao      --> app-util
2 app-service  --> app-dao
3 app-web      --> app-service
```

带来如下好处：

1. 方便`重用`

2. 每个模块的配置都在各自的`pom.xml`里，不用再到一个混乱的纷繁复杂的`总的POM`中寻找自己的配置。

3. 如果你只是在app-dao上工作，你不再需要`build整个项目`，只要在`app-dao目录运行mvn命令`进行build即可，这样可以节省时间

4. 某些模块，如app-util`被所有人依赖，但你不想给所有人修改`，现在你完全可以从这个项目结构出来，做成另外一个项目，`svn`只给特定的人访问，但仍提供jar给别人使用。

5. 多模块的Maven项目结构支持一些Maven的更有趣的特性（如`DepencencyManagement`），这留作以后讨论。

### app-parent的pom.xml

```xml
 <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
 <modelVersion>4.0.0</modelVersion>
 <groupId>org.myorg.myapp</groupId>
 <artifactId>app-parent</artifactId>
 <packaging>pom</packaging>
 <version>1.0-SNAPSHOT</version>
 <modules>
     <module>app-util</module>
    <module>app-dao</module>
    <module>app-service</module>
    <module>app-web</module>
</modules>
</project>
```



Maven的坐标：

`GAV（groupId, artifactId, version）`

打包方式：

特殊的地方在于，这里的packaging为pom。所有带有子模块的项目的packaging都为pom。

`packaging`如果不进行配置，它的`默认值是jar`，代表Maven会将项目打成一个jar包。

build 顺序：

该配置重要的地方在于`modules`，例子中包含的子模块有app-util, app-dao, app-service, app-war。

在`Maven build app-parent`的时候，它会根据`子模块的相互依赖关系`整理一个`build顺序`，然后`依次build`。

### 子模块符合配置继承父模块

app-util：

继承：

```xml
<parent>
<artifactId>app-parent</artifactId>
<groupId>org.myorg.myapp</groupId>
<version>1.0-SNAPSHOT</version>
</parent>
```

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 2xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
<parent>
<artifactId>app-parent</artifactId>
<groupId>org.myorg.myapp</groupId>
<version>1.0-SNAPSHOT</version>
</parent>
<modelVersion>4.0.0</modelVersion>
<artifactId>app-util</artifactId>
<dependencies>
    <dependency>
        <groupId>commons-lang</groupId>
        <artifactId>commons-lang</artifactId>
        <version>2.4</version>
   </dependency>
</dependencies>
</project>
```

app-dao：

继承于app-parent，同时依赖于app-util：

继承：

```xml
<parent>
<artifactId>app-parent</artifactId>
<groupId>org.myorg.myapp</groupId>
<version>1.0-SNAPSHOT</version>
</parent>
```

依赖：

```xml
    <dependency>
         <groupId>org.myorg.myapp</groupId>
         <artifactId>app-util</artifactId>
         <version>${project.version}</version>
    </dependency>
```

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
<parent>
<artifactId>app-parent</artifactId>
<groupId>org.myorg.myapp</groupId>
<version>1.0-SNAPSHOT</version>
</parent>
<modelVersion>4.0.0</modelVersion>
<artifactId>app-dao</artifactId>
<dependencies>
       <dependency>
         <groupId>org.myorg.myapp</groupId>
         <artifactId>app-util</artifactId>
         <version>${project.version}</version>
    </dependency>
</dependencies>
</project>
```

这里要注意的是version的值为`${project.version}`，

这个值是一个`属性引用`，指向了POM的`project/version`的值

由于`app-dao`的version继承于`app-parent`，因此它的值就是`1.0-SNAPSHOT`。

而app-util也继承了这个值，因此在所有这些项目中，我们做到了保持版本一致。

依赖关系：

`app-dao`依赖于`app-util`，而`app-util`又依赖于`commons-lang`，根据传递性，`app-dao`也拥有了对于`commons-lang`的依赖。

### app-web：最终要部署的应用，它的packaging是war

```xml 
 <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
 <parent>
 <artifactId>app-parent</artifactId>
 <groupId>org.myorg.myapp</groupId>
 <version>1.0-SNAPSHOT</version>
 </parent>
 <modelVersion>4.0.0</modelVersion>
 <artifactId>app-web</artifactId>
 <packaging>war</packaging>
 <dependencies>
     <dependency>
         <groupId>org.myorg.myapp</groupId>
         <artifactId>app-service</artifactId>
         <version>${project.version}</version>
     </dependency>
 </dependencies>
 </project>

```

你可以在 `app-web/target` 目录下找到文件 `app-web-1.0-SNAPSHOT.war` ，

打开这个war包，在 `/WEB-INF/lib` 目录看到了` commons-lang-2.4.jar`，以及对应的`app-util`, `app-dao`, `app-service` 的jar包。

Maven`自动`帮你处理了打包的事情，并且根据你的依赖配置帮你引入了相应的jar文件。

## 面试官：如何发布maven项目到中央仓库？我：没有弄过

### 注册Sonatype OSSRH

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.60s9twqm07k0.png)

### 修改pom.xml

账号注册完成，同时也创建了project后，就可以修改pom.xml了。

首先加入`name`，`description`，`scm`，`developers`等信息

```xml
<name>${project.groupId}:${project.artifactId}</name>
<url>https://github.com/0604hx/nerve-tools</url>
<description></description>

<licenses>
    <license>
        <name>The Apache License, Version 2.0</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
    </license>
</licenses>
<developers>
    <developer>
        <name>0604hx</name>
        <email>zxingming@foxmail.com</email>
        <roles>
            <role>developer</role>
        </roles>
        <timezone>+8</timezone>
    </developer>
</developers>
<scm>
    <connection>scm:git:https://github.com/0604hx/nerve-tools.git</connection>
    <developerConnection>scm:git:https://github.com/0604hx/nerve-tools.git</developerConnection>
    <url>https://github.com/0604hx/nerve-tools</url>
    <tag>v${project.version}</tag>
</scm>
```

然后添加distributionManagement

```xml
<distributionManagement>
    <snapshotRepository>
        <id>ossrh</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    </snapshotRepository>
    <repository>
        <id>ossrh</id>
        <name>Maven Central Staging Repository</name>
        <url>https://oss.sonatype.org/service/local/staging/deploy/maven2/</url>
    </repository>
</distributionManagement>
```

接着添加plugins

```xml
<plugins>
    <plugin>
        <groupId>org.sonatype.plugins</groupId>
        <artifactId>nexus-staging-maven-plugin</artifactId>
        <version>1.6.3</version>
        <extensions>true</extensions>
        <configuration>
            <serverId>ossrh</serverId>
            <nexusUrl>https://oss.sonatype.org/</nexusUrl>
            <autoReleaseAfterClose>true</autoReleaseAfterClose>
        </configuration>
    </plugin>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <version>2.10.3</version>
        <executions>
            <execution>
                <id>attach-javadocs</id>
                <goals>
                    <goal>jar</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-source-plugin</artifactId>
        <executions>
            <execution>
                <id>attach-sources</id>
                <goals>
                    <goal>jar-no-fork</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-gpg-plugin</artifactId>
        <version>1.6</version>
        <executions>
            <execution>
                <id>sign-artifacts</id>
                <phase>verify</phase>
                <goals>
                    <goal>sign</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
</plugins>
```

### 修改maven的setting.xml

增加你的帐密信息

```xml
<server>
    <id>ossrh</id>
    <username>用户名</username>
    <password>密码</password>
</server>
```

然后设置gpg的profile

```xml
<profile>
    <id>ossrh</id>
    <activation>
        <activeByDefault>true</activeByDefault>
    </activation>
    <properties>
        <gpg.executable>gpg2</gpg.executable>
        <gpg.passphrase>密码/gpg.passphrase>
    </properties>
</profile>
```

此时要先安装gpg，详细的说明请看：http://central.sonatype.org/pages/working-with-pgp-signatures.html

GPG为您提供生成签名、管理密钥和验证签名的能力。这个页面记录了GPG与中央存储库的关系。简而言之，你必须这么做

创建您自己的密钥对

并将其分发到密钥服务器，以便用户验证它

### 执行mvn deploy

执行以下命令

```s
mvn deploy -Dmaven.test.skip=true -e
```

如果deploy时出现以下错误
 
> gpg: no default secret key: No secret key
 
则表示你没有创建secret key，此时创建key即可。
 
如果出现这样的错误：
 
> No public key:Key with id
 
就说明你没有上传keys到公共服务器，上传就行了：

### 发布项目资源

上一步完成后，其实这是将我们的资源上传到oss.sonatype而已，

我们还没有将其真正地发布出去。步骤如下：

1. 登录到oss.sonatype.com。点击 Staging Repositories

2. 可以看到我们刚刚上传的资源，选中相应的资源（处于open状态）

3. 然后点击上方的“release”按钮，会弹出确认框，直接确认即可。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.63uy2tgeaic0.png)

此时去https://issues.sonatype.org（就是你一开始创建的issue）中留言，

告诉管理员你已经release了。

等审核通过后，就可以在中央仓库中搜索出你的项目

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.34ix948wo420.png)