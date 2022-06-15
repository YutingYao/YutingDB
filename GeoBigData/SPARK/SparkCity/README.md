# 基于 Spark 的[智慧城市框架](https://github.com/feihugis/SparkCity)

这个存储库正在开发一个基于 GeoSpark 的框架，以更好地了解我们的城市。通过利用多资源数据集，例如卫星图像、地理空间矢量数据和社会
媒体数据。

## 环境依赖

### leaflet

> SparkCity/web/

Leaflet是领先的开源JavaScript库，用于移动友好的交互式地图。它只有39 KB的JS大小，拥有大多数开发人员所需要的所有映射特性。

```html

<!DOCTYPE html>
<html>
<head>

    <title>Layers Control Tutorial - Leaflet</title>

    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="shortcut icon" type="image/x-icon" href="docs/images/favicon.ico" />

    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.3.1/dist/leaflet.css" />
    <script src="https://unpkg.com/leaflet@1.3.1/dist/leaflet.js"></script>


    <style>
        html, body {
            height: 100%;
            margin: 0;
        }
        #map {
            width: 100%;
            height: 100%;
        }
    </style>


</head>
<body>

<div id='map'></div>

<script>
    var cities = L.layerGroup();

    var dc_lst = {
        block: L.tileLayer.wms('http://localhost:8080/geoserver/LST/wms?',
                               {
                                   layers: 'LST:dc_lst_block',
                                   transparency: 'true',
                                   format: 'image/png',
                                   opacity: 0.5
        }),
        pois: L.tileLayer.wms('http://localhost:8080/geoserver/LST/wms?', {
            layers: 'LST:dc_lst_pois',
            transparency: 'true',
            format: 'image/png',
            opacity: 0.5
        }),
        buildings: L.tileLayer.wms('http://localhost:8080/geoserver/LST/wms?',
                               {
                                   layers: 'LST:dc_lst_buildings',
                                   transparency: 'true',
                                   format: 'image/png',
                                   opacity: 0.5
                               }),
        landuse: L.tileLayer.wms('http://localhost:8080/geoserver/LST/wms?', {
            layers: 'LST:dc_lst_landuse',
            transparency: 'true',
            format: 'image/png',
            opacity: 0.5
        }),
        parking: L.tileLayer.wms('http://localhost:8080/geoserver/LST/wms?', {
            layers: 'LST:dc_lst_traffic',
            transparency: 'true',
            format: 'image/png',
            opacity: 0.5
        }),
        water: L.tileLayer.wms('http://localhost:8080/geoserver/LST/wms?', {
            layers: 'LST:dc_lst_water',
            transparency: 'true',
            format: 'image/png',
            opacity: 0.5
        })

    }

    var va_lst = {
        block: L.tileLayer.wms('http://localhost:8080/geoserver/LST/wms?',
                               {
                                   layers: 'LST:va_lst_block',
                                   transparency: 'true',
                                   format: 'image/png',
                                   opacity: 0.5
                               }),
        pois: L.tileLayer.wms('http://localhost:8080/geoserver/LST/wms?', {
            layers: 'LST:va_lst_pois',
            transparency: 'true',
            format: 'image/png',
            opacity: 0.1
        }),
        buildings: L.tileLayer.wms('http://localhost:8080/geoserver/LST/wms?',
                                   {
                                       layers: 'LST:va_lst_buildings',
                                       transparency: 'true',
                                       format: 'image/png',
                                       opacity: 0.5
                                   }),
        landuse: L.tileLayer.wms('http://localhost:8080/geoserver/LST/wms?', {
            layers: 'LST:va_lst_landuse',
            transparency: 'true',
            format: 'image/png',
            opacity: 0.1
        }),
        parking: L.tileLayer.wms('http://localhost:8080/geoserver/LST/wms?', {
            layers: 'LST:va_lst_traffic',
            transparency: 'true',
            format: 'image/png',
            opacity: 0.1
        }),
        water: L.tileLayer.wms('http://localhost:8080/geoserver/LST/wms?', {
            layers: 'LST:va_lst_water',
            transparency: 'true',
            format: 'image/png',
            opacity: 0.1
        })
    }

    var md_lst = {
        block: L.tileLayer.wms('http://localhost:8080/geoserver/LST/wms?',
                               {
                                   layers: 'LST:md_lst_block',
                                   transparency: 'true',
                                   format: 'image/png',
                                   opacity: 0.5
                               }),
        pois: L.tileLayer.wms('http://localhost:8080/geoserver/LST/wms?', {
            layers: 'LST:md_lst_pois',
            transparency: 'true',
            format: 'image/png',
            opacity: 0.5
        }),
        buildings: L.tileLayer.wms('http://localhost:8080/geoserver/LST/wms?',
                                   {
                                       layers: 'LST:md_lst_buildings',
                                       transparency: 'true',
                                       format: 'image/png',
                                       opacity: 0.5
                                   }),
        landuse: L.tileLayer.wms('http://localhost:8080/geoserver/LST/wms?', {
            layers: 'LST:md_lst_landuse',
            transparency: 'true',
            format: 'image/png',
            opacity: 0.5
        }),
        parking: L.tileLayer.wms('http://localhost:8080/geoserver/LST/wms?', {
            layers: 'LST:md_lst_traffic',
            transparency: 'true',
            format: 'image/png',
            opacity: 0.5
        }),
        water: L.tileLayer.wms('http://localhost:8080/geoserver/LST/wms?', {
            layers: 'LST:md_lst_water',
            transparency: 'true',
            format: 'image/png',
            opacity: 0.5
        })
    }


    var mbAttr = 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
                 '<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
                 'Imagery © <a href="http://mapbox.com">Mapbox</a>',
        mbUrl = 'https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw';

    var grayscale   = L.tileLayer(mbUrl, {id: 'mapbox.light', attribution: mbAttr}),
        streets  = L.tileLayer(mbUrl, {id: 'mapbox.streets',   attribution: mbAttr});

    var map = L.map('map', {
        center: [38.79164499999541, -77.11975900000722,],
        zoom: 10,
        layers: [streets, dc_lst.block, va_lst.block, md_lst.block]
    });

    var baseLayers = {
        "Streets": streets,
        "Grayscale": grayscale
    };

    var overlays = {
        "DC_Block": dc_lst.block,
        "DC_POI": dc_lst.pois,
        "DC_Building": dc_lst.buildings,
        "DC_Parking": dc_lst.parking,
        "DC_Landuse": dc_lst.landuse,
        "DC_Water": dc_lst.water,
        "VA_Block": va_lst.block,
        "VA_POI": va_lst.pois,
        "VA_Building": va_lst.buildings,
        "VA_Parking": va_lst.parking,
        "VA_Landuse": va_lst.landuse,
        "VA_Water": va_lst.water,
        "MD_Block": md_lst.block,
        "MD_POI": md_lst.pois,
        "MD_Building": md_lst.buildings,
        "MD_Parking": md_lst.parking,
        "MD_Landuse": md_lst.landuse,
        "MD_Water": md_lst.water
    };


    //http://localhost:8080/geoserver/LST/wms?service=WMS&version=1.1.0&request=GetMap&layers=LST:dc_lst_block&styles=&bbox=-77.11975900000722,38.79164499999541,-76.90939500000415,38.99510999999534&width=768&height=742&srs=EPSG:4269&format=application/openlayers

    L.control.layers(baseLayers, overlays).addTo(map);
</script>



</body>
</html>
```

### Maven 

scope值的`provider`和`compile`（默认）的区别

```xml
<scope>provided</scope>
<scope>compile</scope>
```

#### pom.xml

坐标：

```xml
	<groupId>org.smartcity</groupId>
	<artifactId>sparkcity-parent</artifactId>
	<version>1.0.0</version>
```

打包方式：

```xml
	<packaging>pom</packaging>
```

模块，build
```xml
  <modules>
    <module>core</module>
    <module>sql</module>    
    <module>viz</module>
    <module>application</module>
  </modules>
```



```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>org.smartcity</groupId>
	<artifactId>sparkcity-parent</artifactId>
	<version>1.0.0</version>
	<packaging>pom</packaging>
	<name>aicity-parent</name>
	<url>http://maven.apache.org</url>
  <modules>
    <module>core</module>
    <module>sql</module>    
    <module>viz</module>
    <module>application</module>
  </modules>
</project>
```

#### _config.yml

```yml
theme: jekyll-theme-cayman
```

#### .travis.yml

```yml
os:
  - linux

language: java
jdk:
  - oraclejdk8
sudo: required
git:
  submodules: false

## ----------------------------------------------------------------------
## Build tools
## ----------------------------------------------------------------------


## ----------------------------------------------------------------------
## Perform build:
## ----------------------------------------------------------------------

script:

  - mvn clean install

after_success:
  - bash <(curl -s https://codecov.io/bash)

## ----------------------------------------------------------------------
## Notification
## ----------------------------------------------------------------------
notifications:
  email:
    recipients:
      - jiayu2@asu.edu
    on_success: always
    on_failure: always
```

#### application/pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.smartcity</groupId>
    <artifactId>sparkcity-application</artifactId>
    <version>1.1.0-SNAPSHOT</version>

    <name>${project.groupId}:${project.artifactId}</name>
    <description>Application of aicity</description>
    <url>http://feihugis.com/</url>
    <packaging>jar</packaging>

    <licenses>
        <license>
            <name>MIT license</name>
            <url>https://opensource.org/licenses/MIT</url>
        </license>
    </licenses>

    <developers>
        <developer>
            <name>Fei Hu</name>
            <email>hufei68@gmail.com</email>
            <organization>NSF Spatiotemporal Innovation Center</organization>
            <organizationUrl>http://www.stcenter.net/</organizationUrl>
        </developer>
    </developers>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <scala.version>2.11.8</scala.version>
        <scala.compat.version>2.11</scala.compat.version>
        <spark.version>2.3.0</spark.version>
        <geotools.version>17.0</geotools.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.datasyslab</groupId>
            <artifactId>geospark</artifactId>
            <version>${project.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.datasyslab</groupId>
            <artifactId>geospark-sql</artifactId>
            <version>${project.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.datasyslab</groupId>
            <artifactId>geospark-viz</artifactId>
            <version>${project.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-core_${scala.compat.version}</artifactId>
            <version>${spark.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-sql_${scala.compat.version}</artifactId>
            <version>${spark.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-mllib_2.11</artifactId>
            <version>${spark.version}</version>
        </dependency>

        <dependency>
            <groupId>org.scala-lang</groupId>
            <artifactId>scala-library</artifactId>
            <version>${scala.version}</version>
        </dependency>

        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.2.0</version>
        </dependency>

        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>5.2.12.Final</version>
        </dependency>

        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-annotations</artifactId>
            <version>3.5.6-Final</version>
        </dependency>

        <dependency>
            <groupId>ch.ethz.ganymed</groupId>
            <artifactId>ganymed-ssh2</artifactId>
            <version>build210</version>
        </dependency>

        <dependency>
            <groupId>it.geosolutions</groupId>
            <artifactId>geoserver-manager</artifactId>
            <version>1.7.0</version>
        </dependency>

        <dependency>
            <groupId>org.locationtech.geotrellis</groupId>
            <artifactId>geotrellis-spark_2.11</artifactId>
            <version>1.2.1</version>
        </dependency>

        <dependency>
            <groupId>com.typesafe.akka</groupId>
            <artifactId>akka-actor_2.12</artifactId>
            <version>2.5.11</version>
        </dependency>

        <dependency>
            <groupId>com.typesafe.akka</groupId>
            <artifactId>akka-http_2.12</artifactId>
            <version>10.1.0</version>
        </dependency>

        <dependency>
            <groupId>com.typesafe.akka</groupId>
            <artifactId>akka-stream_2.12</artifactId>
            <version>2.5.11</version>
        </dependency>



        <!-- Test -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.specs2</groupId>
            <artifactId>specs2-core_${scala.compat.version}</artifactId>
            <version>2.4.16</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.scalatest</groupId>
            <artifactId>scalatest_${scala.compat.version}</artifactId>
            <version>2.2.4</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.specs2</groupId>
            <artifactId>specs2-junit_${scala.compat.version}</artifactId>
            <version>2.4.16</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <repositories>
        <repository>
            <id>maven2-repository.dev.java.net</id>
            <name>Java.net repository</name>
            <url>http://download.java.net/maven/2</url>
        </repository>
        <repository>
            <id>osgeo</id>
            <name>Open Source Geospatial Foundation Repository</name>
            <url>http://download.osgeo.org/webdav/geotools/</url>
        </repository>
        <repository>
            <id>GeoSolutions</id>
            <url>http://maven.geo-solutions.it/</url>
        </repository>
    </repositories>
    <build>
        <sourceDirectory>src/main/java</sourceDirectory>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>

            <plugin>
                <!-- see http://davidb.github.com/scala-maven-plugin -->
                <groupId>net.alchim31.maven</groupId>
                <artifactId>scala-maven-plugin</artifactId>
                <version>3.2.1</version>
                <executions>
                    <execution>
                        <id>scala-compile-first</id>
                        <phase>process-resources</phase>
                        <goals>
                            <goal>add-source</goal>
                            <goal>compile</goal>
                        </goals>
                        <configuration>
                            <args>
                                <arg>-dependencyfile</arg>
                                <arg>${project.build.directory}/.scala_dependencies</arg>
                            </args>
                        </configuration>
                    </execution>
                    <execution>
                        <id>scala-test-compile</id>
                        <phase>process-test-resources</phase>
                        <goals>
                            <goal>testCompile</goal>
                        </goals>
                        <configuration>
                            <args>
                                <arg>-dependencyfile</arg>
                                <arg>${project.build.directory}/.scala_dependencies</arg>
                            </args>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>2.1</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <transformers>
                                <!--  use transformer to handle merge of META-INF/services - see http://java.net/jira/browse/JERSEY-440?focusedCommentId=14822&page=com.atlassian.jira.plugin.system.issuetabpanels%3Acomment-tabpanel#action_14822 -->
                                <transformer
                                        implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer" />
                                <transformer
                                        implementation="org.apache.maven.plugins.shade.resource.AppendingTransformer">
                                    <resource>reference.conf</resource>
                                </transformer>
                            </transformers>
                            <filters>
                                <!--  filter to address "Invalid signature file" issue - see http://stackoverflow.com/a/6743609/589215-->
                                <filter>
                                    <artifact>*:*</artifact>
                                    <excludes>
                                        <exclude>META-INF/*.SF</exclude>
                                        <exclude>META-INF/*.DSA</exclude>
                                        <exclude>META-INF/*.RSA</exclude>
                                    </excludes>
                                </filter>
                            </filters>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.18.1</version>
            </plugin>
            <plugin>
                <groupId>org.scalatest</groupId>
                <artifactId>scalatest-maven-plugin</artifactId>
                <version>1.0</version>
                <configuration>
                    <reportsDirectory>${project.build.directory}/surefire-reports</reportsDirectory>
                    <junitxml>.</junitxml>
                    <filereports>TestSuiteReport.txt</filereports>
                </configuration>
                <executions>
                    <execution>
                        <id>test</id>
                        <goals>
                            <goal>test</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
        <resources>
            <resource>
                <directory>src/resource</directory>
            </resource>
        </resources>
    </build>
    <profiles>
        <profile>
            <id>release-sign-artifacts</id>
            <activation>
                <property>
                    <name>performRelease</name>
                    <value>true</value>
                </property>
            </activation>
            <distributionManagement>
                <snapshotRepository>
                    <id>ossrh</id>
                    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
                </snapshotRepository>
                <repository>
                    <id>ossrh</id>
                    <url>https://oss.sonatype.org/service/local/staging/deploy/maven2/</url>
                </repository>
            </distributionManagement>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.sonatype.plugins</groupId>
                        <artifactId>nexus-staging-maven-plugin</artifactId>
                        <version>1.6.7</version>
                        <extensions>true</extensions>
                        <configuration>
                            <serverId>ossrh</serverId>
                            <nexusUrl>https://oss.sonatype.org/</nexusUrl>
                            <stagingProfileId>21756750b51471</stagingProfileId>
                            <autoReleaseAfterClose>true</autoReleaseAfterClose>
                        </configuration>
                    </plugin>
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-gpg-plugin</artifactId>
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
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-source-plugin</artifactId>
                        <version>3.0.1</version>
                        <executions>
                            <execution>
                                <id>attach-sources</id>
                                <goals>
                                    <goal>jar</goal>
                                </goals>
                            </execution>
                        </executions>
                    </plugin>

                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-javadoc-plugin</artifactId>
                        <version>2.10.4</version>
                        <executions>
                            <execution>
                                <id>attach-javadocs</id>
                                <goals>
                                    <goal>jar</goal>
                                </goals>
                            </execution>
                        </executions>
                        <configuration>
                            <additionalparam>-Xdoclint:none</additionalparam>
                        </configuration>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>
    
</project>
```

#### viz/pom.xml

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>org.datasyslab</groupId>
	<artifactId>geospark-viz</artifactId>
	<version>1.1.0-SNAPSHOT</version>
	
	<name>${project.groupId}:${project.artifactId}</name>
	<description>Geospatial visualization extension of GeoSpark</description>
	<url>http://geospark.datasyslab.org/</url>
	<packaging>jar</packaging>
 
    <licenses>
        <license>
            <name>MIT license</name>
            <url>https://opensource.org/licenses/MIT</url>
        </license>
    </licenses>

    <developers>
        <developer>
            <name>Jia Yu</name>
            <email>jiayu2@asu.edu</email>
            <organization>Arizona State University Data Systems Lab</organization>
            <organizationUrl>http://www.datasyslab.org/</organizationUrl>
        </developer>
        <developer>
            <name>Mohamed Sarwat</name>
            <email>msarwat@asu.edu</email>
            <organization>Arizona State University Data Systems Lab</organization>
            <organizationUrl>http://www.datasyslab.org/</organizationUrl>
        </developer>
    </developers>
	<scm>
        <connection>scm:git:git@github.com:DataSystemsLab/GeoSpark.git</connection>
        <developerConnection>scm:git:git@github.com:DataSystemsLab/GeoSpark.git</developerConnection>
        <url>git@github.com:DataSystemsLab/GeoSpark.git</url>
    </scm>
    
	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <scala.version>2.11.8</scala.version>
        <scala.compat.version>2.11</scala.compat.version>
        <spark.version>2.2.1</spark.version>
	</properties>
	
	<dependencies>
		<dependency>
			<groupId>org.apache.spark</groupId>
			<artifactId>spark-core_${scala.compat.version}</artifactId>
			<version>${spark.version}</version>
			<scope>provided</scope>
		</dependency>
		
		<dependency>
			<groupId>org.datasyslab</groupId>
			<artifactId>geospark</artifactId>
			<version>${project.version}</version>
			<scope>provided</scope>
		</dependency>		
        
		<dependency>
  			<groupId>org.datasyslab</groupId>
  			<artifactId>JFreeSVGplus</artifactId>
  			<version>0.1.0</version>
		</dependency>

        <dependency>
            <groupId>org.datasyslab</groupId>
            <artifactId>sernetcdf</artifactId>
            <version>0.1.0</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>com.amazonaws</groupId>
            <artifactId>aws-java-sdk-core</artifactId>
            <version>1.11.160</version>
        </dependency>
        <!-- Test -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.specs2</groupId>
            <artifactId>specs2-core_${scala.compat.version}</artifactId>
            <version>2.4.16</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.scalatest</groupId>
            <artifactId>scalatest_${scala.compat.version}</artifactId>
            <version>2.2.4</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.specs2</groupId>
            <artifactId>specs2-junit_${scala.compat.version}</artifactId>
            <version>2.4.16</version>
            <scope>test</scope>
        </dependency>
	</dependencies>
	
	<build>
        <sourceDirectory>src/main/java</sourceDirectory>
		<plugins>

			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.1</version>
				<configuration>
					<source>1.7</source>
					<target>1.7</target>
				</configuration>
			</plugin>

            <plugin>
                <!-- see http://davidb.github.com/scala-maven-plugin -->
                <groupId>net.alchim31.maven</groupId>
                <artifactId>scala-maven-plugin</artifactId>
                <version>3.2.1</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>compile</goal>
                            <goal>testCompile</goal>
                        </goals>
                        <configuration>
                            <args>
                                <arg>-dependencyfile</arg>
                                <arg>${project.build.directory}/.scala_dependencies</arg>
                            </args>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

			<plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>2.1</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
                        <transformers>
                        <!--  use transformer to handle merge of META-INF/services - see http://java.net/jira/browse/JERSEY-440?focusedCommentId=14822&page=com.atlassian.jira.plugin.system.issuetabpanels%3Acomment-tabpanel#action_14822 -->
                            <transformer
                                implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer" />
        					<transformer
       							 implementation="org.apache.maven.plugins.shade.resource.AppendingTransformer">
       							 <resource>reference.conf</resource>
       						</transformer>
                        </transformers> 
                        <filters>
                            <!--  filter to address "Invalid signature file" issue - see http://stackoverflow.com/a/6743609/589215-->
                            <filter>
                                <artifact>*:*</artifact>
                                <excludes>
                                    <exclude>META-INF/*.SF</exclude>
                                    <exclude>META-INF/*.DSA</exclude>
                                    <exclude>META-INF/*.RSA</exclude>
                                </excludes>
                            </filter>
                        </filters>
                    </configuration>
                </execution>
            </executions>
        </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.18.1</version>
            </plugin>
            <plugin>
                <groupId>org.scalatest</groupId>
                <artifactId>scalatest-maven-plugin</artifactId>
                <version>1.0</version>
                <configuration>
                    <reportsDirectory>${project.build.directory}/surefire-reports</reportsDirectory>
                    <junitxml>.</junitxml>
                    <filereports>TestSuiteReport.txt</filereports>
                </configuration>
                <executions>
                    <execution>
                        <id>test</id>
                        <goals>
                            <goal>test</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
		</plugins>
		<resources>
			<resource>
				<directory>src/resource</directory>
			</resource>
		</resources>
	</build>
	<profiles>
        <profile>
            <id>release-sign-artifacts</id>
            <activation>
                <property>
                    <name>performRelease</name>
                    <value>true</value>
                </property>
            </activation>
            <distributionManagement>
                <snapshotRepository>
                    <id>ossrh</id>
                    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
                </snapshotRepository>
                <repository>
                    <id>ossrh</id>
                    <url>https://oss.sonatype.org/service/local/staging/deploy/maven2/</url>
                </repository>
            </distributionManagement>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.sonatype.plugins</groupId>
                        <artifactId>nexus-staging-maven-plugin</artifactId>
                        <version>1.6.7</version>
                        <extensions>true</extensions>
                        <configuration>
                            <serverId>ossrh</serverId>
                            <nexusUrl>https://oss.sonatype.org/</nexusUrl>
                            <stagingProfileId>21756750b51471</stagingProfileId>
                            <autoReleaseAfterClose>true</autoReleaseAfterClose>
                        </configuration>
                    </plugin>
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-gpg-plugin</artifactId>
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
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-source-plugin</artifactId>
                        <version>3.0.1</version>
                        <executions>
                            <execution>
                                <id>attach-sources</id>
                                <goals>
                                    <goal>jar</goal>
                                </goals>
                            </execution>
                        </executions>
                    </plugin>

                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-javadoc-plugin</artifactId>
                        <version>2.10.4</version>
                        <executions>
                            <execution>
                                <id>attach-javadocs</id>
                                <goals>
                                    <goal>jar</goal>
                                </goals>
                            </execution>
                        </executions>
                        <configuration>
                            <additionalparam>-Xdoclint:none</additionalparam>
                        </configuration>
                    </plugin>
                </plugins>
            </build>
        </profile>
        <profile>
            <id>allow-snapshots</id>
            <activation><activeByDefault>true</activeByDefault></activation>
            <repositories>
                <repository>
                    <id>snapshots-repo</id>
                    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
                    <releases><enabled>false</enabled></releases>
                    <snapshots><enabled>true</enabled></snapshots>
                </repository>
            </repositories>
        </profile>
    </profiles>
</project>
  
```

#### sql/pom.xml

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>org.datasyslab</groupId>
	<artifactId>geospark-sql</artifactId>
	<version>1.1.0-SNAPSHOT</version>

	<name>${project.groupId}:${project.artifactId}</name>
	<description>SQL Extension of GeoSpark</description>
	<url>http://geospark.datasyslab.org/</url>
	<packaging>jar</packaging>

    <licenses>
        <license>
            <name>MIT license</name>
            <url>https://opensource.org/licenses/MIT</url>
        </license>
    </licenses>

    <developers>
        <developer>
            <name>Jia Yu</name>
            <email>jiayu2@asu.edu</email>
            <organization>Arizona State University Data Systems Lab</organization>
            <organizationUrl>http://www.datasyslab.org/</organizationUrl>
        </developer>
        <developer>
            <name>Mohamed Sarwat</name>
            <email>msarwat@asu.edu</email>
            <organization>Arizona State University Data Systems Lab</organization>
            <organizationUrl>http://www.datasyslab.org/</organizationUrl>
        </developer>
    </developers>
	<scm>
        <connection>scm:git:git@github.com:DataSystemsLab/GeoSpark.git</connection>
        <developerConnection>scm:git:git@github.com:DataSystemsLab/GeoSpark.git</developerConnection>
        <url>git@github.com:DataSystemsLab/GeoSpark.git</url>
    </scm>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <scala.version>2.11.8</scala.version>
        <scala.compat.version>2.11</scala.compat.version>
        <spark.version>2.2.1</spark.version>
        <geotools.version>17.0</geotools.version>
	</properties>

    <dependencies>
        <dependency>
            <groupId>org.datasyslab</groupId>
            <artifactId>geospark</artifactId>
            <version>${project.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-core_${scala.compat.version}</artifactId>
            <version>${spark.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-sql_${scala.compat.version}</artifactId>
            <version>${spark.version}</version>
            <scope>provided</scope>
        </dependency>

        <!-- Test -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.specs2</groupId>
            <artifactId>specs2-core_${scala.compat.version}</artifactId>
            <version>2.4.16</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.scalatest</groupId>
            <artifactId>scalatest_${scala.compat.version}</artifactId>
            <version>2.2.4</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.specs2</groupId>
            <artifactId>specs2-junit_${scala.compat.version}</artifactId>
            <version>2.4.16</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <repositories>
        <repository>
            <id>maven2-repository.dev.java.net</id>
            <name>Java.net repository</name>
            <url>http://download.java.net/maven/2</url>
        </repository>
        <repository>
            <id>osgeo</id>
            <name>Open Source Geospatial Foundation Repository</name>
            <url>http://download.osgeo.org/webdav/geotools/</url>
        </repository>
    </repositories>
	<build>
        <sourceDirectory>src/main/scala</sourceDirectory>
		<plugins>
            <plugin>
                <groupId>org.scalastyle</groupId>
                <artifactId>scalastyle-maven-plugin</artifactId>
                <version>1.0.0</version>
                <configuration>
                    <verbose>false</verbose>
                    <failOnViolation>true</failOnViolation>
                    <includeTestSourceDirectory>true</includeTestSourceDirectory>
                    <failOnWarning>false</failOnWarning>
                    <sourceDirectory>${project.basedir}/src/main/scala</sourceDirectory>
                    <testSourceDirectory>${project.basedir}/src/test/scala</testSourceDirectory>
                    <configLocation>${project.basedir}/src/test/resources/scalastyle_config.xml</configLocation>
                    <outputFile>${project.basedir}/target/scalastyle-output.xml</outputFile>
                    <outputEncoding>UTF-8</outputEncoding>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>check</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.1</version>
				<configuration>
					<source>1.7</source>
					<target>1.7</target>
				</configuration>
			</plugin>
            <plugin>
                <!-- see http://davidb.github.com/scala-maven-plugin -->
                <groupId>net.alchim31.maven</groupId>
                <artifactId>scala-maven-plugin</artifactId>
                <version>3.2.1</version>
                <executions>
                    <execution>
                        <id>scala-compile-first</id>
                        <phase>process-resources</phase>
                        <goals>
                            <goal>add-source</goal>
                            <goal>compile</goal>
                        </goals>
                        <configuration>
                            <args>
                                <arg>-dependencyfile</arg>
                                <arg>${project.build.directory}/.scala_dependencies</arg>
                            </args>
                        </configuration>
                    </execution>
                    <execution>
                        <id>scala-test-compile</id>
                        <phase>process-test-resources</phase>
                        <goals>
                            <goal>testCompile</goal>
                        </goals>
                        <configuration>
                            <args>
                                <arg>-dependencyfile</arg>
                                <arg>${project.build.directory}/.scala_dependencies</arg>
                            </args>
                        </configuration>
                    </execution>
                    <execution>
                        <id>attach-javadocs</id>
                        <goals>
                            <goal>doc-jar</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

			<plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>2.1</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
                        <transformers>
                        <!--  use transformer to handle merge of META-INF/services - see http://java.net/jira/browse/JERSEY-440?focusedCommentId=14822&page=com.atlassian.jira.plugin.system.issuetabpanels%3Acomment-tabpanel#action_14822 -->
                            <transformer
                                implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer" />
        					<transformer
       							 implementation="org.apache.maven.plugins.shade.resource.AppendingTransformer">
       							 <resource>reference.conf</resource>
       						</transformer>
                        </transformers>
                        <filters>
                            <!--  filter to address "Invalid signature file" issue - see http://stackoverflow.com/a/6743609/589215-->
                            <filter>
                                <artifact>*:*</artifact>
                                <excludes>
                                    <exclude>META-INF/*.SF</exclude>
                                    <exclude>META-INF/*.DSA</exclude>
                                    <exclude>META-INF/*.RSA</exclude>
                                </excludes>
                            </filter>
                        </filters>
                    </configuration>
                </execution>
            </executions>
        </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.18.1</version>
            </plugin>
            <plugin>
                <groupId>org.scalatest</groupId>
                <artifactId>scalatest-maven-plugin</artifactId>
                <version>1.0</version>
                <configuration>
                    <reportsDirectory>${project.build.directory}/surefire-reports</reportsDirectory>
                    <junitxml>.</junitxml>
                    <filereports>TestSuiteReport.txt</filereports>
                </configuration>
                <executions>
                    <execution>
                        <id>test</id>
                        <goals>
                            <goal>test</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
		</plugins>
		<resources>
			<resource>
				<directory>src/resource</directory>
			</resource>
		</resources>
	</build>
	<profiles>
    <profile>
        <id>release-sign-artifacts</id>
        <activation>
            <property>
                <name>performRelease</name>
                <value>true</value>
            </property>
        </activation>
        <distributionManagement>
            <snapshotRepository>
                <id>ossrh</id>
                <url>https://oss.sonatype.org/content/repositories/snapshots</url>
            </snapshotRepository>
            <repository>
                <id>ossrh</id>
                <url>https://oss.sonatype.org/service/local/staging/deploy/maven2/</url>
            </repository>
        </distributionManagement>
        <build>
            <plugins>
            <plugin>
                <groupId>org.sonatype.plugins</groupId>
                <artifactId>nexus-staging-maven-plugin</artifactId>
                <version>1.6.7</version>
                <extensions>true</extensions>
                <configuration>
                    <serverId>ossrh</serverId>
                    <nexusUrl>https://oss.sonatype.org/</nexusUrl>
                    <stagingProfileId>21756750b51471</stagingProfileId>
                    <autoReleaseAfterClose>true</autoReleaseAfterClose>
                </configuration>
            	</plugin>
				<plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-gpg-plugin</artifactId>
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
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-source-plugin</artifactId>
                <version>3.0.1</version>
                <executions>
                    <execution>
                        <id>attach-sources</id>
                        <goals>
                            <goal>jar</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-javadoc-plugin</artifactId>
                <version>2.10.4</version>
                <executions>
                    <execution>
                        <id>attach-javadocs</id>
                        <goals>
                            <goal>jar</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <additionalparam>-Xdoclint:none</additionalparam>
                </configuration>
            </plugin>

            </plugins>
        </build>
    </profile>
    <profile>
        <id>allow-snapshots</id>
        <activation><activeByDefault>true</activeByDefault></activation>
        <repositories>
            <repository>
                <id>snapshots-repo</id>
                <url>https://oss.sonatype.org/content/repositories/snapshots</url>
                <releases><enabled>false</enabled></releases>
                <snapshots><enabled>true</enabled></snapshots>
            </repository>
        </repositories>
    </profile>
</profiles>
</project>
  
```

#### core/pom.xml

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>org.datasyslab</groupId>
	<artifactId>geospark</artifactId>
	<version>1.1.0-SNAPSHOT</version>

	<name>${project.groupId}:${project.artifactId}</name>
	<description>Geospatial extension for Apache Spark</description>
	<url>http://geospark.datasyslab.org/</url>
	<packaging>jar</packaging>

    <licenses>
        <license>
            <name>MIT license</name>
            <url>https://opensource.org/licenses/MIT</url>
        </license>
    </licenses>

    <developers>
        <developer>
            <name>Jia Yu</name>
            <email>jiayu2@asu.edu</email>
            <organization>Arizona State University Data Systems Lab</organization>
            <organizationUrl>http://www.datasyslab.org/</organizationUrl>
        </developer>
        <developer>
            <name>Mohamed Sarwat</name>
            <email>msarwat@asu.edu</email>
            <organization>Arizona State University Data Systems Lab</organization>
            <organizationUrl>http://www.datasyslab.org/</organizationUrl>
        </developer>
    </developers>
	<scm>
        <connection>scm:git:git@github.com:DataSystemsLab/GeoSpark.git</connection>
        <developerConnection>scm:git:git@github.com:DataSystemsLab/GeoSpark.git</developerConnection>
        <url>git@github.com:DataSystemsLab/GeoSpark.git</url>
    </scm>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <scala.version>2.11.8</scala.version>
        <scala.compat.version>2.11</scala.compat.version>
        <geotools.version>17.0</geotools.version>
        <spark.version>2.2.1</spark.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-core_${scala.compat.version}</artifactId>
            <version>${spark.version}</version>
            <scope>provided</scope>
        </dependency>

        <dependency>
            <groupId>org.datasyslab</groupId>
            <artifactId>JTSplus</artifactId>
            <version>0.1.4</version>
        </dependency>

        <dependency>
            <groupId>org.wololo</groupId>
            <artifactId>jts2geojson</artifactId>
            <version>0.10.0</version>
            <exclusions>
                <exclusion>
                    <groupId>com.vividsolutions</groupId>
                    <artifactId>jts-core</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.datasyslab</groupId>
            <artifactId>sernetcdf</artifactId>
            <version>0.1.0</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.geotools</groupId>
            <artifactId>gt-main</artifactId>
            <version>${geotools.version}</version>
            <exclusions>
                <exclusion>
                    <groupId>com.vividsolutions</groupId>
                    <artifactId>jts</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.geotools</groupId>
            <artifactId>gt-referencing</artifactId>
            <version>${geotools.version}</version>
        </dependency>
        <dependency>
            <groupId>org.geotools</groupId>
            <artifactId>gt-epsg-hsql</artifactId>
            <version>${geotools.version}</version>
        </dependency>
        <dependency>
            <groupId>org.geotools</groupId>
            <artifactId>gt-epsg-extension</artifactId>
            <version>${geotools.version}</version>
        </dependency>
        <dependency>
            <groupId>org.geotools</groupId>
            <artifactId>gt-shapefile</artifactId>
            <version>${geotools.version}</version>
            <exclusions>
                <exclusion>
                    <groupId>com.vividsolutions</groupId>
                    <artifactId>jts</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <!-- Test -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.specs2</groupId>
            <artifactId>specs2-core_${scala.compat.version}</artifactId>
            <version>2.4.16</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.scalatest</groupId>
            <artifactId>scalatest_${scala.compat.version}</artifactId>
            <version>2.2.4</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.specs2</groupId>
            <artifactId>specs2-junit_${scala.compat.version}</artifactId>
            <version>2.4.16</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <repositories>
        <repository>
            <id>maven2-repository.dev.java.net</id>
            <name>Java.net repository</name>
            <url>http://download.java.net/maven/2</url>
        </repository>
        <repository>
            <id>osgeo</id>
            <name>Open Source Geospatial Foundation Repository</name>
            <url>http://download.osgeo.org/webdav/geotools/</url>
        </repository>
    </repositories>
	<build>
        <sourceDirectory>src/main/java</sourceDirectory>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.1</version>
				<configuration>
					<source>1.7</source>
					<target>1.7</target>
				</configuration>
			</plugin>

            <plugin>
                <!-- see http://davidb.github.com/scala-maven-plugin -->
                <groupId>net.alchim31.maven</groupId>
                <artifactId>scala-maven-plugin</artifactId>
                <version>3.2.1</version>
                <executions>
                    <execution>
                        <id>scala-compile-first</id>
                        <phase>process-resources</phase>
                        <goals>
                            <goal>add-source</goal>
                            <goal>compile</goal>
                        </goals>
                        <configuration>
                            <args>
                                <arg>-dependencyfile</arg>
                                <arg>${project.build.directory}/.scala_dependencies</arg>
                            </args>
                        </configuration>
                    </execution>
                    <execution>
                        <id>scala-test-compile</id>
                        <phase>process-test-resources</phase>
                        <goals>
                            <goal>testCompile</goal>
                        </goals>
                        <configuration>
                            <args>
                                <arg>-dependencyfile</arg>
                                <arg>${project.build.directory}/.scala_dependencies</arg>
                            </args>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

			<plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>2.1</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
                        <transformers>
                        <!--  use transformer to handle merge of META-INF/services - see http://java.net/jira/browse/JERSEY-440?focusedCommentId=14822&page=com.atlassian.jira.plugin.system.issuetabpanels%3Acomment-tabpanel#action_14822 -->
                            <transformer
                                implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer" />
        					<transformer
       							 implementation="org.apache.maven.plugins.shade.resource.AppendingTransformer">
       							 <resource>reference.conf</resource>
       						</transformer>
                        </transformers>
                        <filters>
                            <!--  filter to address "Invalid signature file" issue - see http://stackoverflow.com/a/6743609/589215-->
                            <filter>
                                <artifact>*:*</artifact>
                                <excludes>
                                    <exclude>META-INF/*.SF</exclude>
                                    <exclude>META-INF/*.DSA</exclude>
                                    <exclude>META-INF/*.RSA</exclude>
                                </excludes>
                            </filter>
                        </filters>
                    </configuration>
                </execution>
            </executions>
        </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.18.1</version>
            </plugin>
            <plugin>
                <groupId>org.scalatest</groupId>
                <artifactId>scalatest-maven-plugin</artifactId>
                <version>1.0</version>
                <configuration>
                    <reportsDirectory>${project.build.directory}/surefire-reports</reportsDirectory>
                    <junitxml>.</junitxml>
                    <filereports>TestSuiteReport.txt</filereports>
                </configuration>
                <executions>
                    <execution>
                        <id>test</id>
                        <goals>
                            <goal>test</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
		</plugins>
		<resources>
			<resource>
				<directory>src/resource</directory>
			</resource>
		</resources>
	</build>
	<profiles>
    <profile>
        <id>release-sign-artifacts</id>
        <activation>
            <property>
                <name>performRelease</name>
                <value>true</value>
            </property>
        </activation>
        <distributionManagement>
            <snapshotRepository>
                <id>ossrh</id>
                <url>https://oss.sonatype.org/content/repositories/snapshots</url>
            </snapshotRepository>
            <repository>
                <id>ossrh</id>
                <url>https://oss.sonatype.org/service/local/staging/deploy/maven2/</url>
            </repository>
        </distributionManagement>
        <build>
            <plugins>
            <plugin>
                <groupId>org.sonatype.plugins</groupId>
                <artifactId>nexus-staging-maven-plugin</artifactId>
                <version>1.6.7</version>
                <extensions>true</extensions>
                <configuration>
                    <serverId>ossrh</serverId>
                    <nexusUrl>https://oss.sonatype.org/</nexusUrl>
                    <stagingProfileId>21756750b51471</stagingProfileId>
                    <autoReleaseAfterClose>true</autoReleaseAfterClose>
                </configuration>
            	</plugin>
				<plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-gpg-plugin</artifactId>
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
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-source-plugin</artifactId>
                <version>3.0.1</version>
                <executions>
                    <execution>
                        <id>attach-sources</id>
                        <goals>
                            <goal>jar</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-javadoc-plugin</artifactId>
                <version>2.10.4</version>
                <executions>
                    <execution>
                        <id>attach-javadocs</id>
                        <goals>
                            <goal>jar</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <additionalparam>-Xdoclint:none</additionalparam>
                </configuration>
            </plugin>
            </plugins>
        </build>
    </profile>
</profiles>
</project>
  
```

#### 编译 jars: mvn install 将模块安装到本地

```s
mvn clean install -DskipTests=true
```

在使用`mvn package`进行编译、打包时，Maven会执行`src/test/java`中的`JUnit测试用例`，

有时为了`跳过测试`，会使用参数`-DskipTests`和`-Dmaven.test.skip=true`，这两个参数的主要区别是：

- DskipTests，不执行`测试用例`，但编译`测试用例类`生成相应的`class文件`至`target/test-classes`下。

- Dmaven.test.skip=true，不执行`测试用例`，也不编译`测试用例类`。

举个栗子例子🤞：

```s
mvn -v
mvn clean (清理项目，通常和其他命令一块用)
mvn clean install (先清理再安装到本地仓库)
mvn package (直接打包)
mvn package -Dmaven.test.skip=true ( 跳过测试代码的编译)
```

### PostgreSQL

[下载安装](hadoopberry\10-postgreSQL.md)

连接 PostgreSQL：

```s
psql -h 10.192.21.133 -p 5432 -U postgres -W
```

### hadoop

需要安装哟！

### Spark-shell

#### 运行 Spark-shell

```s
spark-shell \
--master yarn \
--deploy-mode client \
--jars /root/AiCity/AiCity-application-1.1.0-SNAPSHOT.jar \
--num-executors 5 \
--driver-memory 12g \
--executor-memory 10g \
--executor-cores 22
```

#### 提交 Spark 作业

可用网格类型GridType：

`EQUALGRID`, `HILBERT`, `RTREE`, `VORONOI`, `QUADTREE`, `KDBTREE`

可用索引类型IndexType：

`RTREE`, `QUADTREE`

GeoSpark:

```s
spark-submit \
--master yarn \
--deploy-mode client \
--num-executors 5 \
--driver-memory 12g \
--executor-memory 10g \
--executor-cores 22 \
--class edu.gmu.stc.vector.sparkshell.OverlapPerformanceTest \
/root/geospark/geospark-application-1.1.0-SNAPSHOT.jar \
/user/root/data0119/ \
Impervious_Surface_2015_DC \
Soil_Type_by_Slope_DC Partition_Num \
GridType \
IndexType 
```

STCSpark_构建_索引 STCSpark_Build_Index:

```s
spark-shell \
--master yarn \
--deploy-mode client \
--num-executors 16 \
--driver-memory 12g \
--executor-memory 10g \
--executor-cores 24 \
--class edu.gmu.stc.vector.sparkshell.STC_BuildIndexTest \
--jars application/target/sparkcity-application-1.1.0-SNAPSHOT.jar \
BuildIndex \
config/conf.xml
```

STCSpark_重叠_v1 STCSpark_Overlap_v1:

```s
spark-shell \
--master yarn \
--deploy-mode client \
--num-executors 16 \
--driver-memory 12g \
--executor-memory 10g \
--executor-cores 24 \
--class edu.gmu.stc.vector.sparkshell.STC_OverlapTest \
--jars application/target/sparkcity-application-1.1.0-SNAPSHOT.jar \
config/conf.xml \
Partition_Num \
GridType \
IndexType \
/test.geojson
```

STCSpark_重叠_v2 STCSpark_Overlap_v2:

```s
spark-shell \
--master yarn \
--deploy-mode client \
--num-executors 5 \
--driver-memory 12g \
--executor-memory 10g \
--executor-cores 24 \
--class edu.gmu.stc.vector.sparkshell.STC_OverlapTest_v2 \
--jars application/target/sparkcity-application-1.1.0-SNAPSHOT.jar \
conf.xml \
Partition_Num \
GridType \
IndexType \
/test.geojson
```

STCSpark_重叠_v3 STCSpark_Overlap_v3:

```s
spark-shell \
--master yarn \
--deploy-mode client \
--num-executors 5 \
--driver-memory 12g \
--executor-memory 10g \
--executor-cores 24 \
--class edu.gmu.stc.vector.sparkshell.STC_OverlapTest_v3 \
--jars application/target/sparkcity-application-1.1.0-SNAPSHOT.jar \
config/conf.xml \
Partition_Num \
GridType \
IndexType \
/test.geojson
```

空间连接 SpatialJoin：

```s
spark-shell \
--master yarn \
--deploy-mode client \
--num-executors 16 \
--driver-memory 12g \
--executor-memory 10g \
--executor-cores 24 \
--class edu.gmu.stc.vector.sparkshell.SpatialJoin \
--jars application/target/sparkcity-application-1.1.0-SNAPSHOT.jar \
config/conf.xml \
Partition_Num \
GridType \
IndexType
```

启动 Spark-shell：

 ```s
  ~/spark-2.3.0-bin-hadoop2.6/bin//spark-shell \
  --master yarn \
  --deploy-mode client \
  --num-executors 10 \
  --driver-memory 12g \
  --executor-memory 10g \
  --executor-cores 1 \
  --jars ~/SparkCity/application/target/sparkcity-application-1.1.0-SNAPSHOT.jar
 ```

##  spark源码

### spark.internal.Logging

 ```scala
 /*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.internal

import scala.collection.JavaConverters._

import org.apache.log4j._
import org.apache.log4j.spi.{Filter, LoggingEvent}
import org.slf4j.{Logger, LoggerFactory}
import org.slf4j.impl.StaticLoggerBinder

import org.apache.spark.util.Utils

/**
 * 用于希望记录数据的 classes 的实用工具特性。 
 * 为 class 创建一个 SLF4J logger ，
 * 并允许在不同 levels 上使用
 * 仅在启用 log level 时 
 * 评估员 parameters lazily 的 methods 来记录 messages。  
 */
trait Logging {

  // 使日志字段瞬态化，以便具有 Logging 的对象可以序列化并在另一台机器上使用
  @transient private var log_ : Logger = null

  // 获取此对象的记录器名称的方法
  protected def logName = {
    // 忽略 Scala 对象的类名中的尾随 $
    this.getClass.getName.stripSuffix("$")
  }

  // 获取或创建此对象的记录器的方法
  protected def log: Logger = {
    if (log_ == null) {
      initializeLogIfNecessary(false)
      log_ = LoggerFactory.getLogger(logName)
    }
    log_
  }

  // 仅采用字符串的日志方法
  protected def logInfo(msg: => String): Unit = {
    if (log.isInfoEnabled) log.info(msg)
  }

  protected def logDebug(msg: => String): Unit = {
    if (log.isDebugEnabled) log.debug(msg)
  }

  protected def logTrace(msg: => String): Unit = {
    if (log.isTraceEnabled) log.trace(msg)
  }

  protected def logWarning(msg: => String): Unit = {
    if (log.isWarnEnabled) log.warn(msg)
  }

  protected def logError(msg: => String): Unit = {
    if (log.isErrorEnabled) log.error(msg)
  }

  // 也采用 Throwables（异常/错误）的日志方法
  protected def logInfo(msg: => String, throwable: Throwable): Unit = {
    if (log.isInfoEnabled) log.info(msg, throwable)
  }

  protected def logDebug(msg: => String, throwable: Throwable): Unit = {
    if (log.isDebugEnabled) log.debug(msg, throwable)
  }

  protected def logTrace(msg: => String, throwable: Throwable): Unit = {
    if (log.isTraceEnabled) log.trace(msg, throwable)
  }

  protected def logWarning(msg: => String, throwable: Throwable): Unit = {
    if (log.isWarnEnabled) log.warn(msg, throwable)
  }

  protected def logError(msg: => String, throwable: Throwable): Unit = {
    if (log.isErrorEnabled) log.error(msg, throwable)
  }

  protected def isTraceEnabled(): Boolean = {
    log.isTraceEnabled
  }

  protected def initializeLogIfNecessary(isInterpreter: Boolean): Unit = {
    initializeLogIfNecessary(isInterpreter, silent = false)
  }

  protected def initializeLogIfNecessary(
      isInterpreter: Boolean,
      silent: Boolean = false): Boolean = {
    if (!Logging.initialized) {
      Logging.initLock.synchronized {
        if (!Logging.initialized) {
          initializeLogging(isInterpreter, silent)
          return true
        }
      }
    }
    false
  }

  // 用于检测
  private[spark] def initializeForcefully(isInterpreter: Boolean, silent: Boolean): Unit = {
    initializeLogging(isInterpreter, silent)
  }

  private def initializeLogging(isInterpreter: Boolean, silent: Boolean): Unit = {
    // 不要在这里使用记录器，因为这本身是在记录器初始化期间发生的
    // 如果正在使用 Log4j 1.2，但未初始化，则加载默认属性文件
    if (Logging.isLog4j12()) {
      val log4j12Initialized = LogManager.getRootLogger.getAllAppenders.hasMoreElements
      // scalastyle:off println
      if (!log4j12Initialized) {
        Logging.defaultSparkLog4jConfig = true
        val defaultLogProps = "org/apache/spark/log4j-defaults.properties"
        Option(Utils.getSparkClassLoader.getResource(defaultLogProps)) match {
          case Some(url) =>
            PropertyConfigurator.configure(url)
            if (!silent) {
              System.err.println(s"Using Spark's default log4j profile: $defaultLogProps")
            }
          case None =>
            System.err.println(s"Spark was unable to load $defaultLogProps")
        }
      }

      val rootLogger = LogManager.getRootLogger()
      if (Logging.defaultRootLevel == null) {
        Logging.defaultRootLevel = rootLogger.getLevel()
      }

      if (isInterpreter) {
        // Use the repl's main class to 定义运行 shell 时的默认日志级别，
        // 如果它们不同，则覆盖根记录器的配置。
        val replLogger = LogManager.getLogger(logName)
        val replLevel = Option(replLogger.getLevel()).getOrElse(Level.WARN)
        // 将 consoleAppender 阈值更新为 replLevel
        if (replLevel != rootLogger.getEffectiveLevel()) {
          if (!silent) {
            System.err.printf("Setting default log level to \"%s\".\n", replLevel)
            System.err.println("To adjust logging level use sc.setLogLevel(newLevel). " +
              "For SparkR, use setLogLevel(newLevel).")
          }
          Logging.sparkShellThresholdLevel = replLevel
          rootLogger.getAllAppenders().asScala.foreach {
            case ca: ConsoleAppender =>
              ca.addFilter(new SparkShellLoggingFilter())
            case _ => // no-op
          }
        }
      }
      // scalastyle:on println
    }
    Logging.initialized = true

    // 强制调用 slf4j 来初始化它。避免从多个线程发生这种情况
    // and triggering this: http://mailman.qos.ch/pipermail/slf4j-dev/2010-April/002956.html
    log
  }
}

private[spark] object Logging {
  @volatile private var initialized = false
  @volatile private var defaultRootLevel: Level = null
  @volatile private var defaultSparkLog4jConfig = false
  @volatile private[spark] var sparkShellThresholdLevel: Level = null

  val initLock = new Object()
  try {
    // 我们在这里使用反射来处理用户删除 slf4j-to-jul 桥接命令以将其日志路由到 JUL 的情况。
    val bridgeClass = Utils.classForName("org.slf4j.bridge.SLF4JBridgeHandler")
    bridgeClass.getMethod("removeHandlersForRootLogger").invoke(null)
    val installed = bridgeClass.getMethod("isInstalled").invoke(null).asInstanceOf[Boolean]
    if (!installed) {
      bridgeClass.getMethod("install").invoke(null)
    }
  } catch {
    case e: ClassNotFoundException => // 还不能记录任何东西，所以只是默默地失败
  }

  /**
   * 将日志系统标记为未初始化。这尽最大努力将日志系统重置为其初始状态，以便下一个使用日志记录的类再次触发初始化。
   */
  def uninitialize(): Unit = initLock.synchronized {
    if (isLog4j12()) {
      if (defaultSparkLog4jConfig) {
        defaultSparkLog4jConfig = false
        LogManager.resetConfiguration()
      } else {
        val rootLogger = LogManager.getRootLogger()
        rootLogger.setLevel(defaultRootLevel)
        sparkShellThresholdLevel = null
      }
    }
    this.initialized = false
  }

  private def isLog4j12(): Boolean = {
    // 这将 log4j 1.2 绑定（当前为 org.slf4j.impl.Log4jLoggerFactory）与 log4j 2.0 绑定（当前为 org.apache.logging.slf4j.Log4jLoggerFactory）区分开来
    val binderClass = StaticLoggerBinder.getSingleton.getLoggerFactoryClassStr
    "org.slf4j.impl.Log4jLoggerFactory".equals(binderClass)
  }
}

private class SparkShellLoggingFilter extends Filter {

  /**
   * 如果未定义 sparkShellThresholdLevel，则此过滤器为空操作。
   * 如果事件的日志级别不等于根级别，则允许该事件。除此以外，
   * 根据日志是来自 root 还是某些自定义配置做出决定
   * @param loggingEvent
   * @return decision for accept/deny log event
   */
  def decide(loggingEvent: LoggingEvent): Int = {
    if (Logging.sparkShellThresholdLevel == null) {
      Filter.NEUTRAL
    } else if (loggingEvent.getLevel.isGreaterOrEqual(Logging.sparkShellThresholdLevel)) {
      Filter.NEUTRAL
    } else {
      var logger = loggingEvent.getLogger()
      while (logger.getParent() != null) {
        if (logger.getLevel != null || logger.getAllAppenders.hasMoreElements) {
          return Filter.NEUTRAL
        }
        logger = logger.getParent()
      }
      Filter.DENY
    }
  }
}

 ```

### Strategy

```scala
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.mllib.tree.configuration

import scala.beans.BeanProperty
import scala.collection.JavaConverters._

import org.apache.spark.annotation.Since
import org.apache.spark.mllib.tree.configuration.Algo._
import org.apache.spark.mllib.tree.configuration.QuantileStrategy._
import org.apache.spark.mllib.tree.impurity.{Entropy, Gini, Impurity, Variance}

/**
 * Stores all the configuration options for tree construction
 * @param algo  Learning goal.  Supported:
 *              `org.apache.spark.mllib.tree.configuration.Algo.Classification`,
 *              `org.apache.spark.mllib.tree.configuration.Algo.Regression`
 * @param impurity Criterion used for information gain calculation.
 *                 Supported for Classification: [[org.apache.spark.mllib.tree.impurity.Gini]],
 *                  [[org.apache.spark.mllib.tree.impurity.Entropy]].
 *                 Supported for Regression: [[org.apache.spark.mllib.tree.impurity.Variance]].
 * @param maxDepth Maximum depth of the tree (e.g. depth 0 means 1 leaf node, depth 1 means
 *                 1 internal node + 2 leaf nodes).
 * @param numClasses Number of classes for classification.
 *                                    (Ignored for regression.)
 *                                    Default value is 2 (binary classification).
 * @param maxBins Maximum number of bins used for discretizing continuous features and
 *                for choosing how to split on features at each node.
 *                More bins give higher granularity.
 * @param quantileCalculationStrategy Algorithm for calculating quantiles.  Supported:
 *                             `org.apache.spark.mllib.tree.configuration.QuantileStrategy.Sort`
 * @param categoricalFeaturesInfo A map storing information about the categorical variables and the
 *                                number of discrete values they take. An entry (n to k)
 *                                indicates that feature n is categorical with k categories
 *                                indexed from 0: {0, 1, ..., k-1}.
 * @param minInstancesPerNode Minimum number of instances each child must have after split.
 *                            Default value is 1. If a split cause left or right child
 *                            to have less than minInstancesPerNode,
 *                            this split will not be considered as a valid split.
 * @param minInfoGain Minimum information gain a split must get. Default value is 0.0.
 *                    If a split has less information gain than minInfoGain,
 *                    this split will not be considered as a valid split.
 * @param maxMemoryInMB Maximum memory in MB allocated to histogram aggregation. Default value is
 *                      256 MB.  If too small, then 1 node will be split per iteration, and
 *                      its aggregates may exceed this size.
 * @param subsamplingRate Fraction of the training data used for learning decision tree.
 * @param useNodeIdCache If this is true, instead of passing trees to executors, the algorithm will
 *                       maintain a separate RDD of node Id cache for each row.
 * @param checkpointInterval How often to checkpoint when the node Id cache gets updated.
 *                           E.g. 10 means that the cache will get checkpointed every 10 updates. If
 *                           the checkpoint directory is not set in
 *                           [[org.apache.spark.SparkContext]], this setting is ignored.
 */
@Since("1.0.0")
class Strategy @Since("1.3.0") (
    @Since("1.0.0") @BeanProperty var algo: Algo,
    @Since("1.0.0") @BeanProperty var impurity: Impurity,
    @Since("1.0.0") @BeanProperty var maxDepth: Int,
    @Since("1.2.0") @BeanProperty var numClasses: Int = 2,
    @Since("1.0.0") @BeanProperty var maxBins: Int = 32,
    @Since("1.0.0") @BeanProperty var quantileCalculationStrategy: QuantileStrategy = Sort,
    @Since("1.0.0") @BeanProperty var categoricalFeaturesInfo: Map[Int, Int] = Map[Int, Int](),
    @Since("1.2.0") @BeanProperty var minInstancesPerNode: Int = 1,
    @Since("1.2.0") @BeanProperty var minInfoGain: Double = 0.0,
    @Since("1.0.0") @BeanProperty var maxMemoryInMB: Int = 256,
    @Since("1.2.0") @BeanProperty var subsamplingRate: Double = 1,
    @Since("1.2.0") @BeanProperty var useNodeIdCache: Boolean = false,
    @Since("1.2.0") @BeanProperty var checkpointInterval: Int = 10,
    @Since("3.0.0") @BeanProperty var minWeightFractionPerNode: Double = 0.0,
    @BeanProperty private[spark] var bootstrap: Boolean = false) extends Serializable {

  /**
   */
  @Since("1.2.0")
  def isMulticlassClassification: Boolean = {
    algo == Classification && numClasses > 2
  }

  /**
   */
  @Since("1.2.0")
  def isMulticlassWithCategoricalFeatures: Boolean = {
    isMulticlassClassification && (categoricalFeaturesInfo.size > 0)
  }

  // scalastyle:off argcount
  /**
   * Backwards compatible constructor for [[org.apache.spark.mllib.tree.configuration.Strategy]]
   */
  @Since("1.0.0")
  def this(
      algo: Algo,
      impurity: Impurity,
      maxDepth: Int,
      numClasses: Int,
      maxBins: Int,
      quantileCalculationStrategy: QuantileStrategy,
      categoricalFeaturesInfo: Map[Int, Int],
      minInstancesPerNode: Int,
      minInfoGain: Double,
      maxMemoryInMB: Int,
      subsamplingRate: Double,
      useNodeIdCache: Boolean,
      checkpointInterval: Int) = {
    this(algo, impurity, maxDepth, numClasses, maxBins, quantileCalculationStrategy,
      categoricalFeaturesInfo, minInstancesPerNode, minInfoGain, maxMemoryInMB,
      subsamplingRate, useNodeIdCache, checkpointInterval, 0.0)
  }
  // scalastyle:on argcount

  /**
   * Java-friendly constructor for [[org.apache.spark.mllib.tree.configuration.Strategy]]
   */
  @Since("1.1.0")
  def this(
      algo: Algo,
      impurity: Impurity,
      maxDepth: Int,
      numClasses: Int,
      maxBins: Int,
      categoricalFeaturesInfo: java.util.Map[java.lang.Integer, java.lang.Integer]) = {
    this(algo, impurity, maxDepth, numClasses, maxBins, Sort,
      categoricalFeaturesInfo.asInstanceOf[java.util.Map[Int, Int]].asScala.toMap,
      minWeightFractionPerNode = 0.0)
  }

  /**
   * Sets Algorithm using a String.
   */
  @Since("1.2.0")
  def setAlgo(algo: String): Unit = algo match {
    case "Classification" => setAlgo(Classification)
    case "Regression" => setAlgo(Regression)
  }

  /**
   * Sets categoricalFeaturesInfo using a Java Map.
   */
  @Since("1.2.0")
  def setCategoricalFeaturesInfo(
      categoricalFeaturesInfo: java.util.Map[java.lang.Integer, java.lang.Integer]): Unit = {
    this.categoricalFeaturesInfo =
      categoricalFeaturesInfo.asInstanceOf[java.util.Map[Int, Int]].asScala.toMap
  }

  /**
   * Check validity of parameters.
   * Throws exception if invalid.
   */
  private[spark] def assertValid(): Unit = {
    algo match {
      case Classification =>
        require(numClasses >= 2,
          s"DecisionTree Strategy for Classification must have numClasses >= 2," +
          s" but numClasses = $numClasses.")
        require(Set(Gini, Entropy).contains(impurity),
          s"DecisionTree Strategy given invalid impurity for Classification: $impurity." +
          s"  Valid settings: Gini, Entropy")
      case Regression =>
        require(impurity == Variance,
          s"DecisionTree Strategy given invalid impurity for Regression: $impurity." +
          s"  Valid settings: Variance")
      case _ =>
        throw new IllegalArgumentException(
          s"DecisionTree Strategy given invalid algo parameter: $algo." +
          s"  Valid settings are: Classification, Regression.")
    }
    require(maxDepth >= 0, s"DecisionTree Strategy given invalid maxDepth parameter: $maxDepth." +
      s"  Valid values are integers >= 0.")
    require(maxBins >= 2, s"DecisionTree Strategy given invalid maxBins parameter: $maxBins." +
      s"  Valid values are integers >= 2.")
    require(minInstancesPerNode >= 1,
      s"DecisionTree Strategy requires minInstancesPerNode >= 1 but was given $minInstancesPerNode")
    require(maxMemoryInMB <= 10240,
      s"DecisionTree Strategy requires maxMemoryInMB <= 10240, but was given $maxMemoryInMB")
    require(subsamplingRate > 0 && subsamplingRate <= 1,
      s"DecisionTree Strategy requires subsamplingRate <=1 and >0, but was given " +
      s"$subsamplingRate")
  }

  /**
   * Returns a shallow copy of this instance.
   */
  @Since("1.2.0")
  def copy: Strategy = {
    new Strategy(algo, impurity, maxDepth, numClasses, maxBins,
      quantileCalculationStrategy, categoricalFeaturesInfo, minInstancesPerNode,
      minInfoGain, maxMemoryInMB, subsamplingRate, useNodeIdCache,
      checkpointInterval, minWeightFractionPerNode)
  }
}

@Since("1.2.0")
object Strategy {

  /**
   * Construct a default set of parameters for [[org.apache.spark.mllib.tree.DecisionTree]]
   * @param algo  "Classification" or "Regression"
   */
  @Since("1.2.0")
  def defaultStrategy(algo: String): Strategy = {
    defaultStrategy(Algo.fromString(algo))
  }

  /**
   * Construct a default set of parameters for [[org.apache.spark.mllib.tree.DecisionTree]]
   * @param algo Algo.Classification or Algo.Regression
   */
  @Since("1.3.0")
  def defaultStrategy(algo: Algo): Strategy = algo match {
    case Algo.Classification =>
      new Strategy(algo = Classification, impurity = Gini, maxDepth = 10,
        numClasses = 2)
    case Algo.Regression =>
      new Strategy(algo = Regression, impurity = Variance, maxDepth = 10,
        numClasses = 0)
  }

}

```

## 代码设计

### SparkCity/application/src/main/scala/edu/gmu/stc/analysis/

#### BuildShapeFileIndex.scala 

extends spark.internal.Logging

`serde`是一个序列化与反序列化框架。

Serde自身并未提供具体的序列化与反序列化实现，需要`结合社区提供的其他模块`，实现对具体数据结构的操作。

大部分语言一般是通过`反射`实现序列化操作，`性能开销`普遍较大。

而serde是基于`rust的trait系统`来实现序列化&反序列化，

每种数据结构通过实现serde的Serialize和Deserialize接口来实现序列化功能，

并且`rust编译器`可以在很多场景下对序列化进行高度优化，因此这种实现方案的性能特别高。

serde是开箱即用的，可以序列化和反序列化`任何一种常见的Rust数据类型`，

包括`String, &str, usize,Vec<T>, HashMap<K,V>`等，

以及由这些基础数据类型构造的`其他复杂数据类型`。

社区支持的数据结构:

JSON https://github.com/serde-rs/json

Bincode https://github.com/servo/bincode

CBOR https://github.com/pyfisch/cbor

YAML https://github.com/dtolnay/serde-yaml

MessagePack https://github.com/3Hren/msgpack-rust

TOML https://github.com/alexcrichton/toml-rs

Pickle https://github.com/birkenfeld/serde-pickle

RON https://github.com/ron-rs/ron

BSON https://github.com/mongodb/bson-rust

Avro https://github.com/flavray/avro-rs

JSON5 https://github.com/callum-oakley/json5-rs

Postcard https://github.com/jamesmunns/postcard

URL https://docs.rs/serde_qs

Envy https://github.com/softprops/envy

Envy Store https://github.com/softprops/envy-store

S-expressions https://github.com/rotty/lexpr-rs

D-Bus https://docs.rs/zvariant/2.5.0/zvariant/

FlexBuffers https://github.com/google/flatbuffers/tree/master/rust/flexbuffers

Bencode https://github.com/P3KI/bendy

DynamoDB Items https://docs.rs/serde_dynamo/2.0.0/serde_dynamo/



```scala
package edu.gmu.stc.analysis

import edu.gmu.stc.vector.rdd.ShapeFileMetaRDD
import edu.gmu.stc.vector.serde.VectorKryoRegistrator
import edu.gmu.stc.vector.sparkshell.STC_BuildIndexTest.logError
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.mapred.FileInputFormat
import org.apache.spark.internal.Logging
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Fei Hu on 3/22/18.
  */
object BuildShapeFileIndex extends Logging{

  def buildIndex(sc: SparkContext, hConf: Configuration): Unit = {
    sc.hadoopConfiguration.addResource(hConf)

    val shapeFileMetaRDD = new ShapeFileMetaRDD(sc, hConf)
    shapeFileMetaRDD.initializeShapeFileMetaRDD(sc, hConf)
    shapeFileMetaRDD.saveShapeFileMetaToDB()
  }

  def buildIndex_VA(sc: SparkContext): Unit = {
    val confPath = "/Users/feihu/Documents/GitHub/SparkCity/config/conf_build_index.xml"
    val hConf = new Configuration()
    hConf.addResource(new Path(confPath))
    hConf.set("mapred.input.dir", "/Users/feihu/Documents/GitHub/SparkCity/data/va")
    buildIndex(sc, hConf)
  }

  def buildIndex_DC(sc: SparkContext): Unit = {
    val confPath = "/Users/feihu/Documents/GitHub/SparkCity/config/conf_build_index.xml"
    val hConf = new Configuration()
    hConf.addResource(new Path(confPath))
    hConf.set("mapred.input.dir", "/Users/feihu/Documents/GitHub/SparkCity/data/dc")
    buildIndex(sc, hConf)
  }

  def buildIndex_MD(sc: SparkContext): Unit = {
    val confPath = "/Users/feihu/Documents/GitHub/SparkCity/config/conf_build_index.xml"
    val hConf = new Configuration()
    hConf.addResource(new Path(confPath))
    hConf.set("mapred.input.dir", "/Users/feihu/Documents/GitHub/SparkCity/data/md")
    buildIndex(sc, hConf)
  }

  def buildIndex(sc: SparkContext, confPath: String, shapeFileDir: String): Unit = {
    val hConf = new Configuration()
    hConf.addResource(new Path(confPath))
    hConf.set("mapred.input.dir", shapeFileDir)
    buildIndex(sc, hConf)
  }

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .setAppName("BuildShapefileIndex")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.kryo.registrator", classOf[VectorKryoRegistrator].getName)

    if (System.getProperty("os.name").equals("Mac OS X")) {
      sparkConf.setMaster("local[6]")
    }

    val sc = new SparkContext(sparkConf)
    //buildIndex_DC(sc)
    //buildIndex_VA(sc)
    //buildIndex_MD(sc)
    val inputDir = "/Users/feihu/Documents/GitHub/SparkCity/data/20170416/"

    buildIndex(sc,
      "/Users/feihu/Documents/GitHub/SparkCity/config/conf_lst_va.xml",
      f"${inputDir}/va"
    )

    buildIndex(sc,
      "/Users/feihu/Documents/GitHub/SparkCity/config/conf_lst_va.xml",
      f"${inputDir}/md"
    )

    buildIndex(sc,
      "/Users/feihu/Documents/GitHub/SparkCity/config/conf_lst_va.xml",
      f"${inputDir}/dc"
    )
  }

}

```

#### ComputeLST_LowAccuracy.scala 更好地支持读写tiff

```scala
package edu.gmu.stc.analysis

import com.vividsolutions.jts.geom._
import edu.gmu.stc.raster.io.GeoTiffReaderHelper
import edu.gmu.stc.raster.landsat.Calculations
import edu.gmu.stc.raster.operation.RasterOperation
import edu.gmu.stc.vector.VectorUtil
import edu.gmu.stc.vector.io.ShapeFileReaderHelper
import edu.gmu.stc.vector.shapefile.reader.GeometryReaderUtil
import edu.gmu.stc.vector.sourcedata.OSMAttributeUtil
import geotrellis.raster.{DoubleConstantNoDataCellType, Tile}
import geotrellis.vector.Extent
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.spark.internal.Logging
import org.geotools.geometry.jts.JTS

import scala.collection.JavaConverters._

/**
  * Created by Fei Hu on 3/21/18.
  */
object ComputeLST_LowAccuracy extends Logging{

  def computeLST(hConf: Configuration,
                 landsatMulBandFilePath: Path): (Extent, Tile) = {
    val geotiff = GeoTiffReaderHelper.readMultiBand(landsatMulBandFilePath, hConf)
    val tile = geotiff.tile.convert(DoubleConstantNoDataCellType)
    val (ndvi_min, ndvi_max) = tile.combineDouble(MaskBandsRandGandNIR.R_BAND,
      MaskBandsRandGandNIR.NIR_BAND,
      MaskBandsRandGandNIR.TIRS1_BAND) {
      (r: Double, ir: Double, tirs: Double) => Calculations.ndvi(r, ir);
    }.findMinMaxDouble


    val lstTile = tile.combineDouble(MaskBandsRandGandNIR.R_BAND,
      MaskBandsRandGandNIR.NIR_BAND,
      MaskBandsRandGandNIR.TIRS1_BAND) {
      (r: Double, ir: Double, tirs: Double) => Calculations.lst(r, ir, tirs, ndvi_min, ndvi_max);
    }

    (geotiff.extent, lstTile)
  }

  def extractLSTMeanValueFromTileByGeometry(hConfFile: String,
                                            lstTile: Tile,
                                            rasterExtent: Extent,
                                            rasterCRS: String,
                                            longitudeFirst: Boolean,
                                            vectorIndexTableName: String, vectorCRS: String
                                           ): List[Geometry] = {
    val hConf = new Configuration()
    hConf.addResource(new Path(hConfFile))

    val bbox = if (rasterCRS.equalsIgnoreCase(vectorCRS)) {
      new Envelope(rasterExtent.xmin, rasterExtent.xmax, rasterExtent.ymin, rasterExtent.ymax)
    } else {
      val crsTransform = VectorUtil.getCRSTransform(rasterCRS, longitudeFirst, vectorCRS, longitudeFirst)
      val envelope = new Envelope(rasterExtent.xmin, rasterExtent.xmax, rasterExtent.ymin, rasterExtent.ymax)
      JTS.transform(envelope, crsTransform)
    }

    val polygons = ShapeFileReaderHelper.read(
      hConf,
      vectorIndexTableName,
      bbox.getMinX,
      bbox.getMinY,
      bbox.getMaxX,
      bbox.getMaxY,
      true)

    val polygonsWithLST = RasterOperation
      .clipToPolygons(bbox, Array(lstTile), polygons)
      .filter(g => !g.getUserData.toString.contains("NaN"))

    logInfo("*********** Number of Polygons: " + polygonsWithLST.size)
    logInfo("*********** Number of Input Polygons: " + polygons.size)

    polygonsWithLST
  }

  def extractLSTMeanValueFromTileByGeometry(hConfFile: String,
                                         rasterFile: String, rasterCRS: String,
                                         longitudeFirst: Boolean,
                                         vectorIndexTableName: String, vectorCRS: String
                                        ): List[Geometry] = {
    val hConf = new Configuration()
    hConf.addResource(new Path(hConfFile))

    val (rasterExtent, lstTile) = computeLST(hConf, new Path(rasterFile))

    val bbox = if (rasterCRS.equalsIgnoreCase(vectorCRS)) {
      new Envelope(rasterExtent.xmin, rasterExtent.xmax, rasterExtent.ymin, rasterExtent.ymax)
    } else {
      val crsTransform = VectorUtil.getCRSTransform(rasterCRS, longitudeFirst, vectorCRS, longitudeFirst)
      val envelope = new Envelope(rasterExtent.xmin, rasterExtent.xmax, rasterExtent.ymin, rasterExtent.ymax)
      JTS.transform(envelope, crsTransform)
    }

    val polygons = ShapeFileReaderHelper.read(
      hConf,
      vectorIndexTableName,
      bbox.getMinX,
      bbox.getMinY,
      bbox.getMaxX,
      bbox.getMaxY,
      true)

    val polygonsWithLST = RasterOperation
      .clipToPolygons(bbox, Array(lstTile), polygons)
      .filter(g => !g.getUserData.toString.contains("NaN"))

    logInfo("*********** Number of Polygons: " + polygonsWithLST.size)
    logInfo("*********** Number of Input Polygons: " + polygons.size)

    polygonsWithLST
  }

  case class ComputeLSTConfig(hConfFile: String,
                              rasterFile: String,
                              rasterCRS: String,
                              longitudeFirst: Boolean,
                              vectorIndexTableName: String,
                              vectorCRS: String,
                              osmLayerName: String,
                              outputShpPath: String)

  //TODO: 优化输入参数。例如，支持可重复使用的tile的输入
  // 通过其他 OSM 层
  def addLSTToOSMLayer(computeLSTConfig: ComputeLSTConfig): Unit = {
    val polygonsWithLST = extractLSTMeanValueFromTileByGeometry(computeLSTConfig.hConfFile,
      computeLSTConfig.rasterFile,
      computeLSTConfig.rasterCRS,
      computeLSTConfig.longitudeFirst,
      computeLSTConfig.vectorIndexTableName,
      computeLSTConfig.vectorCRS)

    val attributeSchema = OSMAttributeUtil.getLayerAtrributes(computeLSTConfig.osmLayerName)

    GeometryReaderUtil.saveAsShapefile(computeLSTConfig.outputShpPath,
      computeLSTConfig.vectorCRS,
      classOf[Polygon],
      polygonsWithLST.asJava,
      attributeSchema)
  }

  def main(args: Array[String]): Unit = {

    val buildingsConfig = ComputeLSTConfig(
      hConfFile = "/Users/feihu/Documents/GitHub/SparkCity/config/conf_lst_va.xml",
      rasterFile = "/Users/feihu/Documents/GitHub/SparkCity/data/r-g-nir.tif",
      rasterCRS = "epsg:32618",
      longitudeFirst = true,
      vectorIndexTableName = "gis_osm_buildings_a_free_1",
      vectorCRS = "epsg:4326",
      osmLayerName = "buildings_a",
      outputShpPath = "/Users/feihu/Documents/GitHub/SparkCity/data/lst_buildings_va/lst_buildings_va_lowAcc.shp"
    )

    val landuseConfig = ComputeLSTConfig(
      hConfFile = "/Users/feihu/Documents/GitHub/SparkCity/config/conf_lst_va.xml",
      rasterFile = "/Users/feihu/Documents/GitHub/SparkCity/data/r-g-nir.tif",
      rasterCRS = "epsg:32618",
      longitudeFirst = true,
      vectorIndexTableName = "gis_osm_landuse_a_free_1",
      vectorCRS = "epsg:4326",
      osmLayerName = "landuse_a",
      outputShpPath = "/Users/feihu/Documents/GitHub/SparkCity/data/lst_landuse_va/lst_landuse_va_lowAcc.shp"
    )

    val poisConfig = ComputeLSTConfig(
      hConfFile = "/Users/feihu/Documents/GitHub/SparkCity/config/conf_lst_va.xml",
      rasterFile = "/Users/feihu/Documents/GitHub/SparkCity/data/r-g-nir.tif",
      rasterCRS = "epsg:32618",
      longitudeFirst = true,
      vectorIndexTableName = "gis_osm_pois_a_free_1",
      vectorCRS = "epsg:4326",
      osmLayerName = "pois_a",
      outputShpPath = "/Users/feihu/Documents/GitHub/SparkCity/data/lst_pois_va/lst_pois_va_lowAcc.shp"
    )

    val trafficConfig = ComputeLSTConfig(
      hConfFile = "/Users/feihu/Documents/GitHub/SparkCity/config/conf_lst_va.xml",
      rasterFile = "/Users/feihu/Documents/GitHub/SparkCity/data/r-g-nir.tif",
      rasterCRS = "epsg:32618",
      longitudeFirst = true,
      vectorIndexTableName = "gis_osm_traffic_a_free_1",
      vectorCRS = "epsg:4326",
      osmLayerName = "traffic_a",
      outputShpPath = "/Users/feihu/Documents/GitHub/SparkCity/data/lst_traffic_va/lst_traffic_va_lowAcc.shp"
    )

    val waterConfig = ComputeLSTConfig(
      hConfFile = "/Users/feihu/Documents/GitHub/SparkCity/config/conf_lst_va.xml",
      rasterFile = "/Users/feihu/Documents/GitHub/SparkCity/data/r-g-nir.tif",
      rasterCRS = "epsg:32618",
      longitudeFirst = true,
      vectorIndexTableName = "gis_osm_water_a_free_1",
      vectorCRS = "epsg:4326",
      osmLayerName = "water_a",
      outputShpPath = "/Users/feihu/Documents/GitHub/SparkCity/data/lst_water_va/lst_water_va_lowAcc.shp"
    )

    val blockConfig = ComputeLSTConfig(
      hConfFile = "/Users/feihu/Documents/GitHub/SparkCity/config/conf_lst_va.xml",
      rasterFile = "/Users/feihu/Documents/GitHub/SparkCity/data/r-g-nir.tif",
      rasterCRS = "epsg:32618",
      longitudeFirst = true,
      vectorIndexTableName = "cb_2016_51_bg_500k",
      vectorCRS = "epsg:4269",
      osmLayerName = "block_a",
      outputShpPath = "/Users/feihu/Documents/GitHub/SparkCity/data/lst_block_va/lst_block_va_lowAcc.shp"
    )

    val configs = Array(buildingsConfig, landuseConfig, poisConfig, trafficConfig, waterConfig, blockConfig)
    configs.foreach(config => {
      addLSTToOSMLayer(config)
      logInfo("Finished the processing of " + config.vectorIndexTableName)
    })
  }
}

```

#### ComputeSpectralIndex.scala 重构计算谱指数

```scala
package edu.gmu.stc.analysis

import com.vividsolutions.jts.geom._
import edu.gmu.stc.hdfs.HdfsUtils
import edu.gmu.stc.raster.io.GeoTiffReaderHelper
import edu.gmu.stc.raster.landsat.Calculations
import edu.gmu.stc.raster.operation.RasterOperation
import edu.gmu.stc.vector.VectorUtil
import edu.gmu.stc.vector.io.ShapeFileReaderHelper
import edu.gmu.stc.vector.serde.VectorKryoRegistrator
import edu.gmu.stc.vector.shapefile.reader.GeometryReaderUtil
import edu.gmu.stc.vector.sourcedata.OSMAttributeUtil
import geotrellis.proj4.CRS
import geotrellis.raster.io.geotiff.GeoTiff
import geotrellis.raster.{DoubleConstantNoDataCellType, Tile}
import geotrellis.vector.Extent
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.internal.Logging
import org.geotools.geometry.jts.JTS

import scala.collection.JavaConverters._

/**
  * Created by Fei Hu on 3/21/18.
  */
object ComputeSpectralIndex extends Logging{

  case class ComputeSpectralIndexConfig(hConfFile: String,
                                        rasterFile: String,
                                        rasterCRS: String,
                                        rasterLongitudeFirst: Boolean,
                                        vectorIndexTableName: String,
                                        vectorCRS: String,
                                        vectorLongitudeFirst: Boolean,
                                        osmLayerName: String,
                                        outputShpPath: String)

  //TODO: optimize the input parameters. For example, support the input of tile which can be reused
  // by other OSM layers
  def addSpectralIndexToOSMLayer(computeSpectralIndexConfig: ComputeSpectralIndexConfig,
                                 spectralIndexNames: Array[String]): Unit = {
    val polygonsWithSpectralIndex = RasterOperation.extractMeanValueFromTileByGeometry(
      computeSpectralIndexConfig.hConfFile,
      computeSpectralIndexConfig.rasterFile,
      computeSpectralIndexConfig.rasterCRS,
      computeSpectralIndexConfig.rasterLongitudeFirst,
      computeSpectralIndexConfig.vectorIndexTableName,
      computeSpectralIndexConfig.vectorCRS,
      computeSpectralIndexConfig.vectorLongitudeFirst,
      spectralIndexNames)

    val attributeSchema = OSMAttributeUtil.getLayerAtrributes(computeSpectralIndexConfig.osmLayerName)

    GeometryReaderUtil.saveAsShapefile(
      computeSpectralIndexConfig.outputShpPath,
      computeSpectralIndexConfig.vectorCRS,
      classOf[Polygon],
      polygonsWithSpectralIndex.asJava,
      attributeSchema)

    GeometryReaderUtil.saveDbfAsCSV(
      polygonsWithSpectralIndex.asJava,
      attributeSchema,
      computeSpectralIndexConfig.outputShpPath.replace(".shp", ".csv"))
  }

  def addSpectralIndexToOSMLayer(landsatTiffPath: String,
                                 hConf: Configuration,
                                 computeSpectralIndexConfigs: Array[ComputeSpectralIndexConfig],
                                 spectralIndexNames: Array[String]): Unit = {
    val (tiffExtent, multiBandTiffTiles) = RasterOperation.readMultiBandGeoTiff(hConf, new Path(landsatTiffPath))
    val indexTiles = RasterOperation.computeSpectralIndex(multiBandTiffTiles, spectralIndexNames)

    computeSpectralIndexConfigs.foreach(computeSpectralIndexConfig => {
      val polygonsWithSpectralIndex = RasterOperation
        .extractMeanValueFromTileByGeometry(
          hConf,
          tiffExtent,
          indexTiles,
          computeSpectralIndexConfig.rasterCRS,
          computeSpectralIndexConfig.rasterLongitudeFirst,
          computeSpectralIndexConfig.vectorIndexTableName,
          computeSpectralIndexConfig.vectorCRS,
          computeSpectralIndexConfig.vectorLongitudeFirst
        )

      val attributeSchema = OSMAttributeUtil.getLayerAtrributes(computeSpectralIndexConfig.osmLayerName)

      GeometryReaderUtil.saveAsShapefile(
        computeSpectralIndexConfig.outputShpPath,
        computeSpectralIndexConfig.vectorCRS,
        classOf[Polygon],
        polygonsWithSpectralIndex.asJava,
        attributeSchema)

      GeometryReaderUtil.saveDbfAsCSV(
        polygonsWithSpectralIndex.asJava,
        attributeSchema,
        computeSpectralIndexConfig.outputShpPath.replace(".shp", ".csv"))
    })
  }

  def computeSpectralIndex(stateName: String, stateID: String,
                           landsatTiff: String, outputDir: String,
                           time: String,
                           hConfFile: String): Unit = {
    //val hConfFile = "config/conf_lst_va.xml"

    val buildingsConfig = ComputeSpectralIndexConfig(
      hConfFile = hConfFile,
      rasterFile = landsatTiff,
      rasterCRS = "epsg:32618",
      rasterLongitudeFirst = true,
      vectorIndexTableName = s"${stateName}_osm_buildings_a_free_1",
      vectorCRS = "epsg:4326",
      vectorLongitudeFirst = true,
      osmLayerName = "buildings_a",
      outputShpPath = s"${outputDir}/${stateName}/lst/${stateName}_lst_buildings_${time}.shp"
    )

    val landuseConfig = ComputeSpectralIndexConfig(
      hConfFile = hConfFile,
      rasterFile = landsatTiff,
      rasterCRS = "epsg:32618",
      rasterLongitudeFirst = true,
      vectorIndexTableName = s"${stateName}_osm_landuse_a_free_1",
      vectorCRS = "epsg:4326",
      vectorLongitudeFirst = true,
      osmLayerName = "landuse_a",
      outputShpPath = s"${outputDir}/${stateName}/lst/${stateName}_lst_landuse_${time}.shp"
    )

    val poisConfig = ComputeSpectralIndexConfig(
      hConfFile = hConfFile,
      rasterFile = landsatTiff,
      rasterCRS = "epsg:32618",
      rasterLongitudeFirst = true,
      vectorIndexTableName = s"${stateName}_osm_pois_a_free_1",
      vectorCRS = "epsg:4326",
      vectorLongitudeFirst = true,
      osmLayerName = "pois_a",
      outputShpPath = s"${outputDir}/${stateName}/lst/${stateName}_lst_pois_${time}.shp"
    )

    val trafficConfig = ComputeSpectralIndexConfig(
      hConfFile = hConfFile,
      rasterFile = landsatTiff,
      rasterCRS = "epsg:32618",
      rasterLongitudeFirst = true,
      vectorIndexTableName = s"${stateName}_osm_traffic_a_free_1",
      vectorCRS = "epsg:4326",
      vectorLongitudeFirst = true,
      osmLayerName = "traffic_a",
      outputShpPath = s"${outputDir}/${stateName}/lst/${stateName}_lst_traffic_${time}.shp"
    )

    val waterConfig = ComputeSpectralIndexConfig(
      hConfFile = hConfFile,
      rasterFile = landsatTiff,
      rasterCRS = "epsg:32618",
      rasterLongitudeFirst = true,
      vectorIndexTableName = s"${stateName}_osm_water_a_free_1",
      vectorCRS = "epsg:4326",
      vectorLongitudeFirst = true,
      osmLayerName = "water_a",
      outputShpPath = s"${outputDir}/${stateName}/lst/${stateName}_lst_water_${time}.shp"
    )

    val blockConfig = ComputeSpectralIndexConfig(
      hConfFile = hConfFile,
      rasterFile = landsatTiff,
      rasterCRS = "epsg:32618",
      rasterLongitudeFirst = true,
      vectorIndexTableName = s"${stateName}_cb_2016_${stateID}_bg_500k",
      vectorCRS = "epsg:4269",
      vectorLongitudeFirst = true,
      osmLayerName = "block_a",
      outputShpPath = s"${outputDir}/${stateName}/lst/${stateName}_lst_block_${time}.shp"
    )

    val spectralIndexNames = Array("lst", "ndvi", "ndwi", "ndbi", "ndii", "mndwi", "ndisi")
    val configs = Array(landuseConfig, poisConfig, trafficConfig, waterConfig, blockConfig/*, buildingsConfig*/)

    configs.foreach(config => {
      addSpectralIndexToOSMLayer(config, spectralIndexNames)
      logInfo("Finished the processing of " + config.vectorIndexTableName)
    })
  }

  def getSpectralIndexConfig(stateName: String, stateID: String,
                            landsatTiff: String, outputDir: String,
                            time: String,
                            hConfFile: String): Array[ComputeSpectralIndexConfig] = {
    //val hConfFile = "config/conf_lst_va.xml"

    val buildingsConfig = ComputeSpectralIndexConfig(
      hConfFile = hConfFile,
      rasterFile = landsatTiff,
      rasterCRS = "epsg:32618",
      rasterLongitudeFirst = true,
      vectorIndexTableName = s"${stateName}_osm_buildings_a_free_1",
      vectorCRS = "epsg:4326",
      vectorLongitudeFirst = true,
      osmLayerName = "buildings_a",
      outputShpPath = s"${outputDir}/${stateName}/lst/${stateName}_lst_buildings_${time}.shp"
    )

    val landuseConfig = ComputeSpectralIndexConfig(
      hConfFile = hConfFile,
      rasterFile = landsatTiff,
      rasterCRS = "epsg:32618",
      rasterLongitudeFirst = true,
      vectorIndexTableName = s"${stateName}_osm_landuse_a_free_1",
      vectorCRS = "epsg:4326",
      vectorLongitudeFirst = true,
      osmLayerName = "landuse_a",
      outputShpPath = s"${outputDir}/${stateName}/lst/${stateName}_lst_landuse_${time}.shp"
    )

    val poisConfig = ComputeSpectralIndexConfig(
      hConfFile = hConfFile,
      rasterFile = landsatTiff,
      rasterCRS = "epsg:32618",
      rasterLongitudeFirst = true,
      vectorIndexTableName = s"${stateName}_osm_pois_a_free_1",
      vectorCRS = "epsg:4326",
      vectorLongitudeFirst = true,
      osmLayerName = "pois_a",
      outputShpPath = s"${outputDir}/${stateName}/lst/${stateName}_lst_pois_${time}.shp"
    )

    val trafficConfig = ComputeSpectralIndexConfig(
      hConfFile = hConfFile,
      rasterFile = landsatTiff,
      rasterCRS = "epsg:32618",
      rasterLongitudeFirst = true,
      vectorIndexTableName = s"${stateName}_osm_traffic_a_free_1",
      vectorCRS = "epsg:4326",
      vectorLongitudeFirst = true,
      osmLayerName = "traffic_a",
      outputShpPath = s"${outputDir}/${stateName}/lst/${stateName}_lst_traffic_${time}.shp"
    )

    val waterConfig = ComputeSpectralIndexConfig(
      hConfFile = hConfFile,
      rasterFile = landsatTiff,
      rasterCRS = "epsg:32618",
      rasterLongitudeFirst = true,
      vectorIndexTableName = s"${stateName}_osm_water_a_free_1",
      vectorCRS = "epsg:4326",
      vectorLongitudeFirst = true,
      osmLayerName = "water_a",
      outputShpPath = s"${outputDir}/${stateName}/lst/${stateName}_lst_water_${time}.shp"
    )

    val blockConfig = ComputeSpectralIndexConfig(
      hConfFile = hConfFile,
      rasterFile = landsatTiff,
      rasterCRS = "epsg:32618",
      rasterLongitudeFirst = true,
      vectorIndexTableName = s"${stateName}_cb_2016_${stateID}_bg_500k",
      vectorCRS = "epsg:4269",
      vectorLongitudeFirst = true,
      osmLayerName = "block_a",
      outputShpPath = s"${outputDir}/${stateName}/lst/${stateName}_lst_block_${time}.shp"
    )

    val configs = Array(landuseConfig, poisConfig, trafficConfig, waterConfig, blockConfig/*, buildingsConfig*/)
    configs
  }

  def getSpectralIndexConfigCases(landsatTiff: String, time: String, outputDir: String, hConfFile: String): Array[ComputeSpectralIndexConfig] = {
    val configs1 = getSpectralIndexConfig("va", "51", landsatTiff, outputDir, time, hConfFile)
    val configs2 = getSpectralIndexConfig("md", "24", landsatTiff, outputDir, time, hConfFile)
    val configs3 = getSpectralIndexConfig("dc", "11", landsatTiff, outputDir, time, hConfFile)

    val configs = configs1 ++ configs2 ++ configs3
    configs
    /*val fs = FileSystem.get(sc.hadoopConfiguration)
    val hosts = HdfsUtils.getDataLocation(fs, new Path(landsatTiff))
    configs.map(config => (config, hosts))
*/

    /*sc.parallelize(configs).foreach(config => {
      addSpectralIndexToOSMLayer(config, spectralIndexNames)
      logInfo("Finished the processing of " + config.vectorIndexTableName)
    })*/
  }

  def computeSpectralIndexInParallel(sc: SparkContext,
                                     hConfFile: String,
                                     landsatTiffHDFSTxt: String, outputDir: String): Unit = {
    val spectralIndexNames = Array("lst", "ndvi", "ndwi", "ndbi", "ndii", "mndwi", "ndisi")
    val fs = FileSystem.get(sc.hadoopConfiguration)

    val landsatFilesWithDataLocation = sc.textFile(landsatTiffHDFSTxt).collect()
      .map(landsatFilePath => {
        val hosts = HdfsUtils.getDataLocation(fs, new Path(landsatFilePath))
        (landsatFilePath, hosts)
      })

    sc.makeRDD(landsatFilesWithDataLocation).foreach({
      case (landsatFilePath, hosts) => {
        val time = landsatFilePath.split("_")(3)
        val configs = getSpectralIndexConfigCases(landsatFilePath, time, outputDir, hConfFile)
        val hConf = new Configuration()
        HdfsUtils.addConfigXmlFromHDFS(hConf, hConfFile)
        addSpectralIndexToOSMLayer(landsatFilePath, hConf, configs, spectralIndexNames)
        logInfo("Finished the processing of " + landsatFilePath)
      }
    })



    /*val configCases = landsatFiles.flatMap(landsatFilePath => {
      val time = landsatFilePath.split("_")(3)
      getSpectralIndexConfigCases(sc, landsatFilePath, time, outputDir, hConfFile)
    })

    sc.makeRDD(configCases).foreach(config => {
      addSpectralIndexToOSMLayer(config._1, spectralIndexNames)
      logInfo("Finished the processing of " + config._1.vectorIndexTableName)
    })*/
  }


  def main(args: Array[String]): Unit = {

    // va 51
    // md 24
    // dc 11

    val landsatTiff = "data/landsat8_dc/LC08_L1TP_015033_20170416_20170501_01_T1/r-g-nir-tirs1-swir1.tif"
    val outputDir = "/Users/feihu/Documents/GitHub/SparkCity/data/20170416/"
    val hConfFile: String = "config/conf_lst_va.xml"
    val time = "20170416"


    val inputParameters = Array(
      ("va", "51", landsatTiff, outputDir),
      ("md", "24", landsatTiff, outputDir),
      ("dc", "11", landsatTiff, outputDir)
    )

    inputParameters.foreach({
      case (stateName: String, stateID: String, landsatTiff: String, outputDir: String) => {
        computeSpectralIndex(stateName, stateID, landsatTiff, outputDir, time, hConfFile)
      }
    })
  }
}

```

#### MaskBandsRandGandNIR.scala 建筑指数小幅修订

```scala
package edu.gmu.stc.analysis

import edu.gmu.stc.analysis.MaskBandsRandGandNIR.bandPath
import edu.gmu.stc.hdfs.HdfsUtils
import edu.gmu.stc.raster.io.GeoTiffReaderHelper
import geotrellis.raster._
import geotrellis.raster.io.geotiff._
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.spark.SparkContext

object MaskBandsRandGandNIR {
  val maskedPath = "data/landsat8_dc/LC08_L1TP_015033_20170416_20170501_01_T1/r-g-nir-tirs1-swir1-test.tif"
  // Path to our landsat band geotiffs.
  def bandPath(b: String) = s"data/landsat8_dc/LC08_L1TP_015033_20170416_20170501_01_T1/LC08_L1TP_015033_20170416_20170501_01_T1_${b}.TIF"
  //constants to differentiate which bands to use
  val R_BAND = 0
  val G_BAND = 1
  val NIR_BAND = 2
  val TIRS1_BAND = 3
  val SWIR1_BAND = 4

  def combineBands(bandPathPrefix: String, output: String): Unit = {
    def bandPath(b: String) = bandPathPrefix + s"_${b}.TIF"
    val conf = new Configuration()
    // Read in the red band
    println("Reading in the red band...")
    val rGeoTiff = GeoTiffReaderHelper.readSingleBand(new Path(bandPath("B4")), conf)

    // Read in the green band
    println("Reading in green band...")
    val gGeoTiff = GeoTiffReaderHelper.readSingleBand(new Path(bandPath("B3")), conf)

    // Read in the near infrared band (NIR)
    println("Reading in the NIR band...")
    val nirGeoTiff = GeoTiffReaderHelper.readSingleBand(new Path(bandPath("B5")), conf)

    // Read in the TIRS1 (band10) band
    println("Reading in the TIRS band/band10")
    val tirs1GeoTiff = GeoTiffReaderHelper.readSingleBand(new Path(bandPath("B10")), conf)

    // Read in the TIRS1 (band10) band
    println("Reading in the SWIR1")
    val swir1GeoTiff = GeoTiffReaderHelper.readSingleBand(new Path(bandPath("B6")), conf)

    // Read in the QA band
    println("Reading in the QA band...")
    val qaGeoTiff = GeoTiffReaderHelper.readSingleBand(new Path(bandPath("BQA")), conf)

    // GeoTiffs have more information we need; just grab the Tile out of them.
    val (rTile, gTile, nirTile, qaTile, tirs1Tile, swir1Tile) = (rGeoTiff.tile, gGeoTiff.tile,
      nirGeoTiff.tile, qaGeoTiff.tile, tirs1GeoTiff.tile, swir1GeoTiff.tile)

    // This function will set anything that is potentially a cloud to NODATA
    def maskClouds(tile: Tile): Tile =
      tile.combine(qaTile) { (v: Int, qa: Int) =>
        val isCloud = qa & 0x8000
        val isCirrus = qa & 0x2000
        if(isCloud > 0 || isCirrus > 0) { NODATA }
        else { v }
      }

    // Mask our red, green and near infrared bands using the qa band
    println("Masking clouds in the red band...")
    val rMasked = maskClouds(rTile)
    println("Masking clouds in the green band...")
    val gMasked = maskClouds(gTile)
    println("Masking clouds in the NIR band...")
    val nirMasked = maskClouds(nirTile)
    println("Masking clouds in the TIRS band...")
    val tirs1Masked = maskClouds(tirs1Tile)
    println("Masking clouds in the SWIR1 band...")
    val swir1Masked = maskClouds(swir1Tile)

    // Create a multiband tile with our two masked red and infrared bands.
    val mb = ArrayMultibandTile(rMasked, gMasked, nirMasked, tirs1Masked, swir1Masked).convert(IntConstantNoDataCellType)

    // Create a multiband geotiff from our tile, using the same extent and CRS as the original geotiffs.
    println("Writing out the multiband R + G + NIR + TIRS1 + SWIR1 tile...")
    // MultibandGeoTiff(mb, rGeoTiff.extent, rGeoTiff.crs).write(maskedPath)
    val byteArray = MultibandGeoTiff(mb, rGeoTiff.extent, rGeoTiff.crs).toByteArray
    HdfsUtils.write(new Path(output), new Configuration(), byteArray)
  }

  def combineBands(sc: SparkContext, landsatTxtPath: String): Unit = {
    val paths = sc.textFile(landsatTxtPath)
    val pathRDD = paths.repartition(paths.count().toInt)

    val suffix = "_r-g-nir-tirs1-swir1.tif"
    pathRDD.foreach(landsatPre => combineBands(landsatPre, landsatPre + suffix))
  }

  def main(args: Array[String]): Unit = {
    // Read in the red band
    println("Reading in the red band...")
    val rGeoTiff = SinglebandGeoTiff(bandPath("B4"))

    // Read in the green band
    println("Reading in green band...")
    val gGeoTiff = SinglebandGeoTiff(bandPath("B3"))

    // Read in the near infrared band (NIR)
    println("Reading in the NIR band...")
    val nirGeoTiff = SinglebandGeoTiff(bandPath("B5"))

    // Read in the TIRS1 (band10) band
    println("Reading in the TIRS band/band10")
    val tirs1GeoTiff = SinglebandGeoTiff(bandPath("B10"))

    // Read in the TIRS1 (band10) band
    println("Reading in the SWIR1")
    val swir1GeoTiff = SinglebandGeoTiff(bandPath("B6"))

    // Read in the QA band
    println("Reading in the QA band...")
    val qaGeoTiff = SinglebandGeoTiff(bandPath("BQA"))

    // GeoTiffs have more information we need; just grab the Tile out of them.
    val (rTile, gTile, nirTile, qaTile, tirs1Tile, swir1Tile) = (rGeoTiff.tile, gGeoTiff.tile,
      nirGeoTiff.tile, qaGeoTiff.tile, tirs1GeoTiff.tile, swir1GeoTiff.tile)

    // This function will set anything that is potentially a cloud to NODATA
    def maskClouds(tile: Tile): Tile =
      tile.combine(qaTile) { (v: Int, qa: Int) =>
        val isCloud = qa & 0x8000
        val isCirrus = qa & 0x2000
        if(isCloud > 0 || isCirrus > 0) { NODATA }
        else { v }
      }

    // Mask our red, green and near infrared bands using the qa band
    println("Masking clouds in the red band...")
    val rMasked = maskClouds(rTile)
    println("Masking clouds in the green band...")
    val gMasked = maskClouds(gTile)
    println("Masking clouds in the NIR band...")
    val nirMasked = maskClouds(nirTile)
    println("Masking clouds in the TIRS band...")
    val tirs1Masked = maskClouds(tirs1Tile)
    println("Masking clouds in the SWIR1 band...")
    val swir1Masked = maskClouds(swir1Tile)

    // Create a multiband tile with our two masked red and infrared bands.
    val mb = ArrayMultibandTile(rMasked, gMasked, nirMasked, tirs1Masked, swir1Masked).convert(IntConstantNoDataCellType)

    // Create a multiband geotiff from our tile, using the same extent and CRS as the original geotiffs.
    println("Writing out the multiband R + G + NIR + TIRS1 + SWIR1 tile...")
    // MultibandGeoTiff(mb, rGeoTiff.extent, rGeoTiff.crs).write(maskedPath)
    val byteArray = MultibandGeoTiff(mb, rGeoTiff.extent, rGeoTiff.crs).toByteArray
    HdfsUtils.write(new Path(maskedPath), new Configuration(), byteArray)
  }
}

```

#### RelationMining.scala 关系挖掘.scala 优化数据管道

```scala
package edu.gmu.stc.analysis


import org.apache.spark.ml.feature.Normalizer
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.SparkSession

/**
  * Created by Fei Hu on 3/22/18.
  */
object RelationMining {

  def linearRegressionWithElasticNet(): Unit = {
    val spark = SparkSession
      .builder
      .master("local[6]")
      .appName(s"LinearRegression")
      .getOrCreate()

    // Load training data
    val training = spark.read.format("libsvm")
      .load("/Users/feihu/Documents/GitHub/SparkCity/data/lst_va_libsvm/part-00000-2b65712e-d9dd-4b6c-8f35-c2ffc8f874a1-c000.libsvm").limit(200)
      //.load("/Users/feihu/Documents/GitHub/SparkCity/data/sample_linear_regression_data.txt")

    /*val normalizer = new Normalizer()
      .setInputCol("features")
      .setOutputCol("normFeatures")
      .setP(1.0)

    val training = normalizer.transform(training_input)*/

    training.show()

    val lr = new LinearRegression()
      .setMaxIter(1000)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)

    // Fit the model
    val lrModel = lr.fit(training)

    // Print the coefficients and intercept for linear regression
    println(s"Coefficients: ${lrModel.coefficients} Intercept: ${lrModel.intercept}")

    // Summarize the model over the training set and print out some metrics
    val trainingSummary = lrModel.summary
    println(s"numIterations: ${trainingSummary.totalIterations}")
    println(s"objectiveHistory: [${trainingSummary.objectiveHistory.mkString(",")}]")
    trainingSummary.residuals.show()
    println(s"RMSE: ${trainingSummary.rootMeanSquaredError}")
    println(s"r2: ${trainingSummary.r2}")

    spark.stop()
  }

  def main(args: Array[String]): Unit = {
    linearRegressionWithElasticNet()
  }

}

```

#### SpatialJoin.scala

```scala
package edu.gmu.stc.analysis

import java.io.{File, PrintWriter}

import com.vividsolutions.jts.geom.Envelope
import edu.gmu.stc.vector.VectorUtil
import edu.gmu.stc.vector.rdd.GeometryRDD
import edu.gmu.stc.vector.serde.VectorKryoRegistrator
import edu.gmu.stc.vector.shapefile.reader.GeometryReaderUtil
import edu.gmu.stc.vector.sourcedata.{Attribute, OSMAttributeUtil}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.spark.{SparkConf, SparkContext}
import edu.gmu.vector.landscape.ComputeLandscape._
import org.apache.spark.ml.feature.LabeledPoint
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.SQLContext
import org.opengis.metadata.extent.Extent
//import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.ml.regression.LinearRegression
import scala.collection.JavaConverters._

/**
  * Created by Fei Hu on 3/22/18.
  */
object SpatialJoin {

  case class SpatialJoinConfig(confFilePath: String,
                               envelope: Envelope,
                               gridType: String,
                               indexType: String,
                               partitionNum: Int,
                               indexTable1: String,
                               crs1: String,
                               longitudeFirst1: Boolean,
                               indexTable2: String,
                               crs2: String,
                               longitudeFirst2: Boolean,
                               indexArray: Array[String],
                               rawAttributes: Array[Attribute],
                               outputFilePath: String)

  def spatialJoin(sc: SparkContext, spatialJoinConfig: SpatialJoinConfig): Unit = {
    spatialJoin(
      sc,
      spatialJoinConfig.confFilePath,
      spatialJoinConfig.envelope,
      spatialJoinConfig.gridType,
      spatialJoinConfig.indexType,
      spatialJoinConfig.partitionNum,
      spatialJoinConfig.indexTable1,
      spatialJoinConfig.crs1,
      spatialJoinConfig.longitudeFirst1,
      spatialJoinConfig.indexTable2,
      spatialJoinConfig.crs2,
      spatialJoinConfig.longitudeFirst2,
      spatialJoinConfig.indexArray,
      spatialJoinConfig.rawAttributes,
      spatialJoinConfig.outputFilePath
    )
  }

  def spatialJoin(sc: SparkContext, confFilePath: String,
                  envelope: Envelope,
                  gridType: String = "KDBTREE",
                  indexType: String = "RTREE",
                  partitionNum: Int,
                  indexTable1: String,
                  crs1: String,
                  longitudeFirst1: Boolean = true,
                  indexTable2: String,
                  crs2: String,
                  longitudeFirst2: Boolean = true,
                  indexArray: Array[String],
                  rawAttributes: Array[Attribute],
                  outputFilePath: String): Unit = {

    val hConf = new Configuration()
    hConf.addResource(new Path(confFilePath))
    sc.hadoopConfiguration.addResource(hConf)

    val minX = envelope.getMinX
    val minY = envelope.getMinY
    val maxX = envelope.getMaxX
    val maxY = envelope.getMaxY

    val baseLayerRDD = GeometryRDD(
      sc, hConf, indexTable1,
      gridType, indexType, partitionNum,
      minX, minY, maxX, maxY,
      readAttributes = true, isCache = true)

    val overlayedEnvelope = VectorUtil.transformCRS(
      envelope,
      sourceCRSStr = crs1,
      sourceLongitudeFirst = longitudeFirst1,
      targetCRSStr = crs2,
      targetLongitudeFirst = longitudeFirst2
    )

    val overlayedRDD = GeometryRDD(
      sc, hConf, indexTable2,
      partitionNum,
      baseLayerRDD.getPartitioner,
      overlayedEnvelope.getMinX,
      overlayedEnvelope.getMinY,
      overlayedEnvelope.getMaxX,
      overlayedEnvelope.getMaxY,
      readAttributes = true,
      isCache = true)

    if (!crs1.equalsIgnoreCase(crs2)) {
      overlayedRDD.transforCRS(crs2, longitudeFirst2, crs1, longitudeFirst1)
    }

    val joinedRDD = baseLayerRDD.spatialJoin(overlayedRDD).cache()

    val featureRDD = joinedRDD.map {
      case (geometry1, geometries2) => {
        val indices = indexArray.map {
          case "CP" => computeCoverPercent(geometry1, geometries2)
          case "FN" => countFeatureNum(geometry1, geometries2)
          case "MPS" => computeMeanPatchSize(geometry1, geometries2)
          case "MSI" => computeMeanShapeIndex(geometry1, geometries2)
          case "MNND" => computeMeanNearestNeighborDistance(geometry1, geometries2)
          case "PCI" => computePatchCohesionIndex(geometry1, geometries2)
          case "RP" => computeRoadPercent(geometry1, geometries2)
          case "TP" => computeCoverPercent(geometry1, geometries2)
          case "WP" => computeCoverPercent(geometry1, geometries2)  //water percent
        }

        geometry1
          .getUserData
          .toString
          .split("\t")
          .map(str => str.replace(",", " "))
          .++(indices).mkString(",")
      }
    }

    val result = featureRDD.collect()
    val col_names = rawAttributes
          .slice(0, result.head.split(",").length - indexArray.length)
          .map(attribute => attribute.getName).++(indexArray)

    GeometryReaderUtil.saveRows2CSV(col_names.mkString(","), result.toList.asJava, outputFilePath)
  }

  def spatialJoinEntrance(sc: SparkContext,
                  stateName: String,
                  stateID: String,
                  dataDir: String = "/Users/feihu/Documents/GitHub/SparkCity/data/20180318/",
                  gridType: String = "KDBTREE",
                  indexType: String = "RTREE",
                  confFilePath: String = "/Users/feihu/Documents/GitHub/SparkCity/config/conf_lst_va.xml"): Unit = {
    // Change this part for configuration
    val (minX, maxX, minY, maxY) = (-180.0, 180.0, -180.0, 180.0)
    ///////////////////////////////////////////

    val bbox = new Envelope(minX, maxX, minY, maxY)
    val baseIndexTable = s"${stateName}_lst_block"
    val crs1 = "epsg:4269"
    val crs2 = "epsg:4326"
    val partitionNum = 36

    val spatialJoinConfig_0 = SpatialJoinConfig(
      confFilePath = confFilePath,
      envelope = bbox,
      gridType = gridType,
      indexType = indexType,
      partitionNum = partitionNum,
      indexTable1 = baseIndexTable,
      crs1 = crs1,
      longitudeFirst1 = true,
      indexTable2 = baseIndexTable,
      crs2 = crs2,
      longitudeFirst2 = true,
      indexArray = Array(),
      rawAttributes = OSMAttributeUtil.getLayerAtrributes("block_a").asScala.toArray,
      outputFilePath = s"${dataDir}/${stateName}/result/${stateName}_cb.csv"
    )

    val spatialJoinConfig_1 = SpatialJoinConfig(
      confFilePath = confFilePath,
      envelope = bbox,
      gridType = gridType,
      indexType = indexType,
      partitionNum = partitionNum,
      indexTable1 = baseIndexTable,
      crs1 = crs1,
      longitudeFirst1 = true,
      indexTable2 =  s"${stateName}_osm_buildings_a_free_1",
      crs2 = crs2,
      longitudeFirst2 = true,
      indexArray = Array("CP", "MPS", "MSI","MNND", "PCI", "FN"),
      rawAttributes = OSMAttributeUtil.getLayerAtrributes("block_a").asScala.toArray,
      outputFilePath = s"${dataDir}/${stateName}/result/${stateName}_buildings.csv".toString
    )

    val spatialJoinConfig_2 = SpatialJoinConfig(
      confFilePath = confFilePath,
      envelope = bbox,
      gridType = gridType,
      indexType = indexType,
      partitionNum = partitionNum,
      indexTable1 = baseIndexTable,
      crs1 = crs1,
      longitudeFirst1 = true,
      indexTable2 = s"${stateName}_osm_roads_free_1",
      crs2 = crs2,
      longitudeFirst2 = true,
      indexArray = Array("RP"),
      rawAttributes = OSMAttributeUtil.getLayerAtrributes("block_a").asScala.toArray,
      outputFilePath = s"${dataDir}/${stateName}/result/${stateName}_roads.csv"
    )

    val spatialJoinConfig_3 = SpatialJoinConfig(
      confFilePath = confFilePath,
      envelope = bbox,
      gridType = gridType,
      indexType = indexType,
      partitionNum = partitionNum,
      indexTable1 = baseIndexTable,
      crs1 = crs1,
      longitudeFirst1 = true,
      indexTable2 = s"${stateName}_lst_traffic",
      crs2 = crs2,
      longitudeFirst2 = true,
      indexArray = Array("TP"),
      rawAttributes = OSMAttributeUtil.getLayerAtrributes("block_a").asScala.toArray,
      outputFilePath = s"${dataDir}/${stateName}/result/${stateName}_parkings.csv"
    )

    val spatialJoinConfig_4 = SpatialJoinConfig(
      confFilePath = confFilePath,
      envelope = bbox,
      gridType = gridType,
      indexType = indexType,
      partitionNum = partitionNum,
      indexTable1 = baseIndexTable,
      crs1 = crs1,
      longitudeFirst1 = true,
      indexTable2 = s"${stateName}_lst_water",
      crs2 = crs2,
      longitudeFirst2 = true,
      indexArray = Array("WP"),
      rawAttributes = OSMAttributeUtil.getLayerAtrributes("block_a").asScala.toArray,
      outputFilePath = s"${dataDir}/${stateName}/result/${stateName}_water.csv"
    )

    spatialJoin(sc, spatialJoinConfig_0)
    spatialJoin(sc, spatialJoinConfig_1)
    spatialJoin(sc, spatialJoinConfig_2)
    spatialJoin(sc, spatialJoinConfig_3)
    spatialJoin(sc, spatialJoinConfig_4)

  }

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("SpatialJoin")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.kryo.registrator", classOf[VectorKryoRegistrator].getName)

    if (System.getProperty("os.name").equals("Mac OS X")) {
      sparkConf.setMaster("local[6]")
    }

    val sc = new SparkContext(sparkConf)

    val va_bbox = (-77.622, -76.995, 38.658, 39.105)
    val va_statename = "va"
    val va_stateID = 51

    val dc_bbox = (-77.1512, -76.8916, 38.7882, 38.9980)
    val dc_statename = "dc"
    val dc_stateID = 11

    val md_bbox = (-76.7119, -76.5121, 39.2327, 39.3669)
    val md_statename = "md"
    val md_stateID = 24

    val dataDir = "/Users/feihu/Documents/GitHub/SparkCity/data/20170416/"
    val gridType: String = "KDBTREE"
    val indexType: String = "RTREE"
    val confFilePath: String = "/Users/feihu/Documents/GitHub/SparkCity/config/conf_lst_va.xml"

    val spatialJoinParameters = Array((va_statename, va_stateID), (dc_statename, dc_stateID), (md_statename, md_stateID))

    spatialJoinParameters.foreach({
      case (stateName, stateID) => {
        spatialJoinEntrance(sc, stateName, stateID.toString, dataDir, gridType, indexType, confFilePath)
      }
    })
  }

}

```


### SparkCity/application/src/main/scala/edu/gmu/stc/hdfs/

#### HdfsUtils.scala

```scala
package edu.gmu.stc.hdfs

import java.io.IOException

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileStatus, FileSystem, FileUtil, Path}
import org.apache.spark.internal.Logging

import scala.collection.mutable.ListBuffer

/**
  * Created by Fei Hu on 4/16/18.
  */
object HdfsUtils extends Logging{
  def pathExists(path: Path, conf: Configuration): Boolean =
    path.getFileSystem(conf).exists(path)

  def renamePath(from: Path, to: Path, conf: Configuration): Unit = {
    val fs = from.getFileSystem(conf)
    fs.rename(from, to)
  }

  def copyPath(from: Path, to: Path, conf: Configuration): Unit = {
    val fsFrom = from.getFileSystem(conf)
    val fsTo = to.getFileSystem(conf)
    FileUtil.copy(fsFrom, from, fsTo, to, false, conf)
  }

  def ensurePathExists(path: Path, conf: Configuration): Unit = {
    val fs = path.getFileSystem(conf)
    if(!fs.exists(path))
      fs.mkdirs(path)
    else
    if(!fs.isDirectory(path)) logError(s"Directory $path does not exist on ${fs.getUri}")
  }

  def getDataLocation(fs: FileSystem, path: Path): Array[String] = {
    val blockLocations = fs.getFileBlockLocations(path, 0, fs.getFileStatus(path).getLen)
    blockLocations.flatMap(blockLocation => blockLocation.getHosts)
  }

  /*
   * Recursively descend into a directory and and get list of file paths
   * The input path can have glob patterns
   *    e.g. /geotrellis/images/ne*.tif
   * to only return those files that match "ne*.tif"
   */
  def listFiles(path: Path, conf: Configuration): List[Path] = {
    val fs = path.getFileSystem(conf)
    val files = new ListBuffer[Path]

    def addFiles(fileStatuses: Array[FileStatus]): Unit = {
      for (fst <- fileStatuses) {
        if (fst.isDirectory())
          addFiles(fs.listStatus(fst.getPath()))
        else
          files += fst.getPath()
      }
    }

    val globStatus = fs.globStatus(path)
    if (globStatus == null)
      throw new IOException(s"No matching file(s) for path: $path")

    addFiles(globStatus)
    files.toList
  }

  def write(path: Path, conf: Configuration, bytes: Array[Byte]): Unit = {
    val fs = path.getFileSystem(conf)
    if (pathExists(path, conf)) return

    val out = fs.create(path, false, bytes.length, 3, bytes.length - (bytes.length%512) + 512)
    out.write(bytes)
    out.flush()
    out.close()
  }

  def addConfigXmlFromHDFS(hConf: Configuration, configFilePath: String): Unit = {
    val fs = FileSystem.get(hConf)
    val inputStream = fs.open(new Path(configFilePath))
    hConf.addResource(inputStream)
  }
}

```

### SparkCity/application/src/main/scala/edu/gmu/stc/raster/io/

#### GeoTiffReaderHelper.scala

```scala
package edu.gmu.stc.raster.io

import geotrellis.raster.io.geotiff.{MultibandGeoTiff, SinglebandGeoTiff}
import geotrellis.raster.io.geotiff.reader.GeoTiffReader
import geotrellis.spark.io.hadoop.HdfsRangeReader
import geotrellis.util.StreamingByteReader
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path

/**
  * Created by Fei Hu on 3/21/18.
  */
object GeoTiffReaderHelper {

  def readMultiBand(path: Path, hConf: Configuration): MultibandGeoTiff = {
    GeoTiffReader.readMultiband(
      StreamingByteReader(HdfsRangeReader(path, hConf))
    )
  }

  def readSingleBand(path: Path, hConf: Configuration): SinglebandGeoTiff = {
    GeoTiffReader.readSingleband(
      StreamingByteReader(HdfsRangeReader(path, hConf))
    )
  }



}

```


### SparkCity/application/src/main/scala/edu/gmu/stc/vector/io/

#### ShapeFileReaderHelper.scala

```scala
package edu.gmu.stc.vector.io

import com.vividsolutions.jts.geom.Geometry
import edu.gmu.stc.hibernate.{DAOImpl, HibernateUtil, PhysicalNameStrategyImpl}
import edu.gmu.stc.vector.shapefile.meta.ShapeFileMeta
import edu.gmu.stc.vector.shapefile.reader.GeometryReaderUtil
import org.apache.hadoop.conf.Configuration
import org.apache.spark.internal.Logging

import scala.collection.JavaConverters._

/**
  * Created by Fei Hu on 3/21/18.
  */
object ShapeFileReaderHelper extends Logging{

  def queryShapeMetaDatas(hconf: Configuration,
           tableName: String,
           minX: Double, minY: Double,
           maxX: Double, maxY: Double,
           hasAttribute: Boolean): List[ShapeFileMeta] = {
    val physicalNameStrategy = new PhysicalNameStrategyImpl(tableName)
    val hibernateUtil = new HibernateUtil
    hibernateUtil.createSessionFactoryWithPhysicalNamingStrategy(hconf,
      physicalNameStrategy,
      classOf[ShapeFileMeta])
    val session = hibernateUtil.getSession
    val dao = new DAOImpl[ShapeFileMeta]()
    dao.setSession(session)
    val hql = ShapeFileMeta.getSQLForOverlappedRows(tableName, minX, minY, maxX, maxY)
    logInfo(hql)

    val shapeFileMetaList = dao.findByQuery(hql, classOf[ShapeFileMeta]).asScala.toList
    session.close()
    hibernateUtil.closeSessionFactory()
    shapeFileMetaList
  }

  def read(hconf: Configuration,
           tableName: String,
           minX: Double, minY: Double,
           maxX: Double, maxY: Double,
           hasAttribute: Boolean): List[Geometry] = {
    val shapeFileMetaList = queryShapeMetaDatas(hconf, tableName, minX, minY, maxX, maxY, hasAttribute)

    if (hasAttribute) {
      GeometryReaderUtil.readGeometriesWithAttributes(shapeFileMetaList.asJava).asScala.toList
    } else {
      GeometryReaderUtil.readGeometries(shapeFileMetaList.asJava).asScala.toList
    }
  }



}

```


### SparkCity/application/src/main/scala/edu/gmu/stc/raster/landsat/

#### Calculations.scala

```scala
package edu.gmu.stc.raster.landsat

import geotrellis.raster._

import scala.math._

/**
  * Object that can calculate ndvi and ndwi
  */
object Calculations {
  /** Calculates the normalized difference vegetation index
    * @param r value of red band
    * @param nir value of infra-red band
    */
  def ndvi (r: Double, nir: Double) : Double = {
    if (isData(r) && isData(nir)) {
        (nir - r) / (nir + r)
    } else {
      Double.NaN
    }
  }

  /** Calculates the normalized difference water index
    * @param g value of green band
    * @param nir value of infra-red band
    */
  def ndwi (g: Double, nir: Double) : Double = {
    if (isData(g) && isData(nir)) {
      (g - nir) / (g + nir)
    } else {
      Double.NaN
    }
  }

  /**
    * Calculate the normalized difference built-up index
    * @param swir1 Band 6 - Shortwave Infrared (SWIR) 1
    * @param nir Band 5 - Near Infrared (NIR)
    * @return
    */
  def ndbi(swir1: Double, nir: Double) : Double = {
    if (isData(swir1) && isData(nir)) {
      (swir1 - nir) / (swir1 + nir)
    } else {
      Double.NaN
    }
  }

  /**
    * Calculate the normalized difference imperious index
    * @param vis visible band (e.g. r band), such as band 2, 3, 4; We the choose the red (4) band
    *            here based on this paper, Wang, Z., Gang, C., Li, X., Chen, Y. and Li, J., 2015.
    *            Application of a normalized difference impervious index (NDII) to extract urban
    *            impervious surface features based on Landsat TM images. International Journal of
    *            Remote Sensing, 36(4), pp.1055-1069.
    * @param tir1 Band 10 - Thermal Infrared (TIRS) 1
    * @return
    */
  def ndii (vis: Double, tir1: Double): Double = {
    if (isData(vis) && isData(tir1)) {
      (vis - tir1) / (vis + tir1)
    } else {
      Double.NaN
    }
  }


  //Reference: Garg, A., Pal, D., Singh, H. and Pandey, D.C., 2016, November. A comparative study of
  // NDBI, NDISI and NDII for extraction of urban impervious surface of Dehradun [Uttarakhand, India]
  // using Landsat 8 imagery. In Emerging Trends in Communication Technologies (ETCT), International
  // Conference on (pp. 1-5). IEEE.

  /**
    * Calculate the water index
    * @param g Band 3 - Green
    * @param swir1 Band 6 - Shortwave Infrared (SWIR) 1
    * @return
    */
  def mndwi(g: Double, swir1: Double): Double = {
    if (isData(g) && isData(swir1)) {
      (g - swir1) / (g + swir1)
    } else {
      Double.NaN
    }
  }

  /**
    * Calculate the normalized difference impervious surface index
    * @param tir1 Band 10 - Thermal Infrared (TIRS) 1
    * @param g Band 3 - Green
    * @param nir Band 5 - Near Infrared (NIR)
    * @param swir1 Band 6 - Shortwave Infrared (SWIR) 1
    * @return
    */
  def ndisi(tir1: Double, g: Double, nir: Double, swir1: Double): Double = {
    if (isData(tir1) && isData(g) && isData(nir) && isData(swir1) && isData(swir1)) {
      val mndwi = (g - swir1) / (g + swir1)
      (tir1 - (mndwi + nir + swir1)/3) / (tir1 + (mndwi + nir + swir1)/3)
    } else {
      Double.NaN
    }
  }


  val Qmax = 65535.0
  val Qmin = 1.0
  val Lmax = 22.00180
  val Lmin = 0.10033

  def lst(r: Double, ir: Double, tirs: Double, ndvi_min: Double, ndvi_max: Double): Double = {
    if (tirs < Qmin) {
      return Double.NaN
    }

    if (isData(r) && isData(ir) && isData(tirs)) {
      val toa_val = toa(tirs)
      val brightnessTemp = brightnesstemperature(toa_val)

      val lamda = 11.5
      val p = 0.014388 * 1000000 //6.626 * 2.998 / 1.38 / pow(10.0, 3.0)
      val lse = landSurfaceEmissivity(r, ir, ndvi_min, ndvi_max)
      val land_surface_temperature = brightnessTemp/(1 + lamda * brightnessTemp / p * log(lse))

     /* if (land_surface_temperature.toString.equals("NaN")) {
        println(r, ir, tirs, toa_val, brightnessTemp, lse, land_surface_temperature)
      }*/

      land_surface_temperature.toInt.toDouble
    } else {
      Double.NaN
    }
  }

  def toa(tirs: Double): Double = {
    (Lmax - Lmin) / (Qmax - Qmin) * (tirs - Qmin) + Lmin
  }

  def brightnesstemperature(toa: Double): Double = {
    val k1 = 607.76
    val k2 = 1260.56

    k2/log(k1/toa + 1)
  }

  def landSurfaceEmissivity(r: Double, ir: Double, ndvi_min: Double, ndvi_max: Double): Double = {
    val ndvi_val = ndvi(r, ir)
    val pv = pow((ndvi_val - ndvi_min) / (ndvi_max - ndvi_min), 2.0)

    val tse = if (ndvi_val < 0.2) {
      0.979 - 0.035*r/100000.0
    } else if (ndvi_val <= 0.5) {
      0.986 + 0.004 * pv
    } else {
      0.99
    }

    tse
  }


}

```

#### ComputeLSTPolygons.scala

```scala
package edu.gmu.stc.raster.landsat

import java.io.{BufferedWriter, File, FileWriter}

import com.typesafe.config.ConfigFactory
import com.vividsolutions.jts.geom.Geometry
import edu.gmu.stc.analysis.MaskBandsRandGandNIR
import geotrellis.raster.{DoubleConstantNoDataCellType, _}
import geotrellis.raster.io.geotiff.MultibandGeoTiff
import geotrellis.raster.render.{ColorMap, ColorRamp}
import geotrellis.vector.{Extent, _}
import org.wololo.geojson.{Feature, FeatureCollection}
import org.wololo.jts2geojson.GeoJSONWriter

import scala.collection.JavaConverters._

/**
  * Created by Fei Hu on 3/18/18.
  */
object ComputeLSTPolygons {
  val maskedPath = "data/r-g-nir.tif"
  val lstPath = "data/lst-dc-2.png"

  def vectorize = {
    val threshold = 3101111.0
    val lst = {
      val tile = MultibandGeoTiff(maskedPath).tile.convert(DoubleConstantNoDataCellType)
      println(tile.getClass)
      val (ndvi_min, ndvi_max) = tile.combineDouble(MaskBandsRandGandNIR.R_BAND, MaskBandsRandGandNIR.NIR_BAND, MaskBandsRandGandNIR.TIRS1_BAND) { (r: Double, ir: Double, tirs: Double) =>
        Calculations.ndvi(r, ir);
      }.findMinMaxDouble

      tile.combineDouble(MaskBandsRandGandNIR.R_BAND, MaskBandsRandGandNIR.NIR_BAND, MaskBandsRandGandNIR.TIRS1_BAND) { (r: Double, ir: Double, tirs: Double) =>
        Calculations.lst(r, ir, tirs, ndvi_min, ndvi_max)
      }
    }//.>(threshold)

    println(lst.getClass)

    val (min, max) = lst.findMinMaxDouble

    println(min, "*******", max)

    //val lst_new = lst.normalize(min, max, 0.0, 1.0).>(0.6)

    println(lst.dimensions)

    val vector = lst.toVector(new Extent(0, 0, 7831, 7951))
      .map(feature => feature.reproject((x, y) => (220800 + x*30, 4188000 + y*30)))
    //val vector = lst_new.toVector(new Extent(220800, 4188000, 455700, 4426500))



    println(vector.size)

    val vector_filered = vector.map(feature => {
      val polygon = feature.jtsGeom
      polygon.setUserData(feature.data)
      (feature.data, polygon)
    })
      .filter(pair => pair._1 > 300)
      .map(pair => pair._2)

    println(vector_filered.size)

    //val geojsonContent = vector.map(feature => feature.geom).toGeoJson
    val geojsonContent = toGeoJSON(vector_filered.toIterator)



    val file = new File(threshold + "_vectorize_categorized.geojson")
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(geojsonContent)
    bw.close()
  }

  def toGeoJSON(iterator: Iterator[Geometry]): String = {
    val geoJSONWriter = new GeoJSONWriter
    val featureList = iterator.map(geometry => {
      if (geometry.getUserData != null) {
        val userData = Map("UserData" -> geometry.getUserData)
        val feature = new Feature(geoJSONWriter.write(geometry), userData.asJava)
        feature.setType("FeatureCollection")
        feature
      } else {
        val feature = new Feature(geoJSONWriter.write(geometry), null)
        feature.setType("FeatureCollection")
        feature
      }
    }).toList


    val featureCollection = new FeatureCollection(featureList.toArray[Feature])
    featureCollection.toString
  }

  def createLSTPng(args: Array[String]): Unit = {
    val lst = {
      // Convert the tile to type double values,
      // because we will be performing an operation that
      // produces floating point values.
      println("Reading in multiband image...")
      val tile = MultibandGeoTiff(maskedPath).tile.convert(DoubleConstantNoDataCellType)

      // Use the combineDouble method to map over the red and infrared values
      // and perform the NDVI calculation.
      println("Performing LST calculation...")
      val (ndvi_min, ndvi_max) = tile.combineDouble(MaskBandsRandGandNIR.R_BAND, MaskBandsRandGandNIR.NIR_BAND, MaskBandsRandGandNIR.TIRS1_BAND) { (r: Double, ir: Double, tirs: Double) =>
        Calculations.ndvi(r, ir);
      }.findMinMaxDouble


      tile.combineDouble(MaskBandsRandGandNIR.R_BAND, MaskBandsRandGandNIR.NIR_BAND, MaskBandsRandGandNIR.TIRS1_BAND) { (r: Double, ir: Double, tirs: Double) =>
        Calculations.lst(r, ir, tirs, ndvi_min, ndvi_max);
      }
    }


    //lst.foreachDouble((x, y, v) => println(x, y, v))


    val (min, max) = lst.findMinMaxDouble

    println(min, "*******", max)

    val lst_new = lst.normalize(min, max, 0.0, 1.0).>(0.6)

    // Get color map from the application.conf settings file.
    val colorMap = ColorMap.fromStringDouble(ConfigFactory.load().getString("tutorial.lstColormap")).get
    val colors: Vector[Int] = Vector(0x0000FF, 0xFF0000)
    val ramp: ColorRamp = ColorRamp(colors)



    // Render this NDVI using the color breaks as a PNG,
    // and write the PNG to disk.
    println("Rendering PNG and saving to disk...")
    val stops = 10
    //val breaks = for (i <- 1 to stops) yield i.toDouble / stops
    val breaks = for (i <- 0 to 2 * (max - min + 1).toInt) yield i*0.5 + min
    //lst_new.renderPng(ColorRamps.HeatmapBlueToYellowToRedSpectrum.toColorMap(breaks.toArray)).write(lstPath)
    //lst_new.renderPng(ramp.stops(stops).toColorMap(breaks.toArray)).write(lstPath)
    //lst.renderPng(ColorRamps.HeatmapBlueToYellowToRedSpectrum.toColorMap(breaks.toArray)).write(lstPath)
    lst_new.renderPng(colorMap).write(lstPath)
  }

  def main(args: Array[String]): Unit = {
    //createLSTPng(args)
    vectorize
  }

}

```

#### CreateLSTPng.scala

```scala
package edu.gmu.stc.raster.landsat

import java.io.{BufferedWriter, File, FileWriter}

import com.typesafe.config.ConfigFactory
import com.vividsolutions.jts.geom.Geometry
import edu.gmu.stc.analysis.MaskBandsRandGandNIR
import geotrellis.raster.DoubleConstantNoDataCellType
import geotrellis.raster.io.geotiff.MultibandGeoTiff
import geotrellis.raster.io.geotiff.reader.GeoTiffReader
import geotrellis.raster.render.{ColorMap, ColorRamp, ColorRamps}
import geotrellis.spark.io.hadoop.HdfsRangeReader
import geotrellis.util.StreamingByteReader
import geotrellis.vector.{Extent, Polygon}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.wololo.geojson.{Feature, FeatureCollection}
import org.wololo.jts2geojson.GeoJSONWriter

import scala.collection.JavaConverters._
import geotrellis.raster.ArrayMultibandTile
import geotrellis.raster.io.geotiff.MultibandGeoTiff


/**
  * Created by Fei Hu on 3/21/18.
  */
object CreateLSTPng {

  val maskedPath = "data/r-g-nir.tif"
  val lstPath = "data/lst-dc-2.png"

  def testHDFS = {
    val lst = {
      val geotiff = GeoTiffReader.readMultiband(
        StreamingByteReader(HdfsRangeReader(new Path(maskedPath),
          new Configuration())))


      val tile = geotiff.tile.convert(DoubleConstantNoDataCellType)


      println(geotiff.getClass)
      println(tile.getClass)

      val (ndvi_min, ndvi_max) = tile.combineDouble(MaskBandsRandGandNIR.R_BAND,
        MaskBandsRandGandNIR.NIR_BAND,
        MaskBandsRandGandNIR.TIRS1_BAND) {
        (r: Double, ir: Double, tirs: Double) => Calculations.ndvi(r, ir);
      }.findMinMaxDouble

      tile.combineDouble(MaskBandsRandGandNIR.R_BAND,
        MaskBandsRandGandNIR.NIR_BAND,
        MaskBandsRandGandNIR.TIRS1_BAND) {
        (r: Double, ir: Double, tirs: Double) => Calculations.lst(r, ir, tirs, ndvi_min, ndvi_max);
      }
    }

    println(lst.getClass)

    val (min, max) = lst.findMinMaxDouble

    //val lst_new = lst.normalize(min, max, 0.0, 1.0).>(0.6)

    // Get color map from the application.conf settings file.
    //val colorMap = ColorMap.fromStringDouble(ConfigFactory.load().getString("tutorial.lstColormap")).get
    //val colors: Vector[Int] = Vector(0x0000FF, 0xFF0000)
    //val ramp: ColorRamp = ColorRamp(colors)

    println("Rendering PNG and saving to disk...")
    val stops = 10
    //val breaks = for (i <- 1 to stops) yield i.toDouble / stops
    val breaks = for (i <- 0 to 2 * (max - min + 1).toInt) yield i*0.5 + min
    lst.renderPng(ColorRamps.HeatmapBlueToYellowToRedSpectrum.toColorMap(breaks.toArray)).write(lstPath)
    //lst_new.renderPng(ramp.stops(stops).toColorMap(breaks.toArray)).write(lstPath)
    //lst.renderPng(ColorRamps.HeatmapBlueToYellowToRedSpectrum.toColorMap(breaks.toArray)).write(lstPath)
    //lst_new.renderPng(colorMap).write(lstPath)
  }

  def vectorize = {
    val threshold = 3101111.0
    val lst = {
      val tile = MultibandGeoTiff(maskedPath).tile.convert(DoubleConstantNoDataCellType)
      println(tile.getClass)
      val (ndvi_min, ndvi_max) = tile.combineDouble(MaskBandsRandGandNIR.R_BAND, MaskBandsRandGandNIR.NIR_BAND, MaskBandsRandGandNIR.TIRS1_BAND) { (r: Double, ir: Double, tirs: Double) =>
        Calculations.ndvi(r, ir);
      }.findMinMaxDouble

      tile.combineDouble(MaskBandsRandGandNIR.R_BAND, MaskBandsRandGandNIR.NIR_BAND, MaskBandsRandGandNIR.TIRS1_BAND) { (r: Double, ir: Double, tirs: Double) =>
        Calculations.lst(r, ir, tirs, ndvi_min, ndvi_max)
      }
    }//.>(threshold)

    println(lst.getClass)

    val (min, max) = lst.findMinMaxDouble

    println(min, "*******", max)

    //val lst_new = lst.normalize(min, max, 0.0, 1.0).>(0.6)

    println(lst.dimensions)

    val vector = lst.toVector(new Extent(0, 0, 7831, 7951))
      .map(feature => feature.reproject((x, y) => (220800 + x*30, 4188000 + y*30)))
    //val vector = lst_new.toVector(new Extent(220800, 4188000, 455700, 4426500))


    println(vector.size)

    val vector_filered = vector.map(feature => {
      val polygon = feature.jtsGeom
      polygon.setUserData(feature.data)
      (feature.data, polygon)
    })
      .filter(pair => pair._1 > 300)
      .map(pair => pair._2)

    println(vector_filered.size)

    //val geojsonContent = vector.map(feature => feature.geom).toGeoJson
    val geojsonContent = toGeoJSON(vector_filered.toIterator)



    val file = new File(threshold + "_vectorize_categorized.geojson")
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(geojsonContent)
    bw.close()
  }

  def toGeoJSON(iterator: Iterator[Geometry]): String = {
    val geoJSONWriter = new GeoJSONWriter
    val featureList = iterator.map(geometry => {
      if (geometry.getUserData != null) {
        val userData = Map("UserData" -> geometry.getUserData)
        val feature = new Feature(geoJSONWriter.write(geometry), userData.asJava)
        feature.setType("FeatureCollection")
        feature
      } else {
        val feature = new Feature(geoJSONWriter.write(geometry), null)
        feature.setType("FeatureCollection")
        feature
      }
    }).toList


    val featureCollection = new FeatureCollection(featureList.toArray[Feature])
    featureCollection.toString
  }



  def createLSTPng(args: Array[String]): Unit = {
    val lst = {
      // Convert the tile to type double values,
      // because we will be performing an operation that
      // produces floating point values.
      println("Reading in multiband image...")
      val tile = MultibandGeoTiff(maskedPath).tile.convert(DoubleConstantNoDataCellType)

      // Use the combineDouble method to map over the red and infrared values
      // and perform the NDVI calculation.
      println("Performing LST calculation...")
      val (ndvi_min, ndvi_max) = tile.combineDouble(MaskBandsRandGandNIR.R_BAND, MaskBandsRandGandNIR.NIR_BAND, MaskBandsRandGandNIR.TIRS1_BAND) { (r: Double, ir: Double, tirs: Double) =>
        Calculations.ndvi(r, ir);
      }.findMinMaxDouble


      tile.combineDouble(MaskBandsRandGandNIR.R_BAND, MaskBandsRandGandNIR.NIR_BAND, MaskBandsRandGandNIR.TIRS1_BAND) { (r: Double, ir: Double, tirs: Double) =>
        Calculations.lst(r, ir, tirs, ndvi_min, ndvi_max);
      }
    }


    //lst.foreachDouble((x, y, v) => println(x, y, v))


    val (min, max) = lst.findMinMaxDouble

    println(min, "*******", max)

    val lst_new = lst.normalize(min, max, 0.0, 1.0).>(0.6)

    // Get color map from the application.conf settings file.
    val colorMap = ColorMap.fromStringDouble(ConfigFactory.load().getString("tutorial.lstColormap")).get
    val colors: Vector[Int] = Vector(0x0000FF, 0xFF0000)
    val ramp: ColorRamp = ColorRamp(colors)



    // Render this NDVI using the color breaks as a PNG,
    // and write the PNG to disk.
    println("Rendering PNG and saving to disk...")
    val stops = 10
    //val breaks = for (i <- 1 to stops) yield i.toDouble / stops
    val breaks = for (i <- 0 to 2 * (max - min + 1).toInt) yield i*0.5 + min
    //lst_new.renderPng(ColorRamps.HeatmapBlueToYellowToRedSpectrum.toColorMap(breaks.toArray)).write(lstPath)
    //lst_new.renderPng(ramp.stops(stops).toColorMap(breaks.toArray)).write(lstPath)
    //lst.renderPng(ColorRamps.HeatmapBlueToYellowToRedSpectrum.toColorMap(breaks.toArray)).write(lstPath)
    lst_new.renderPng(colorMap).write(lstPath)
  }

  def main(args: Array[String]): Unit = {
    //createLSTPng(args)
    //vectorize
    testHDFS
  }

}

```

#### CreateNDVIPng.scala

```scala
package edu.gmu.stc.raster.landsat

import com.typesafe.config.ConfigFactory
import edu.gmu.stc.analysis.MaskBandsRandGandNIR
import geotrellis.raster._
import geotrellis.raster.io.geotiff._
import geotrellis.raster.render._

object CreateNDVIPng {
  val maskedPath = "data/r-g-nir.tif"
  val ndviPath = "data/ndvi-dc.png"

  def main(args: Array[String]): Unit = {
    val ndvi = {
      // Convert the tile to type double values,
      // because we will be performing an operation that
      // produces floating point values.
      println("Reading in multiband image...")
      val tile = MultibandGeoTiff(maskedPath).tile.convert(DoubleConstantNoDataCellType)

      // Use the combineDouble method to map over the red and infrared values
      // and perform the NDVI calculation.
      println("Performing NDVI calculation...")
      tile.combineDouble(MaskBandsRandGandNIR.R_BAND, MaskBandsRandGandNIR.NIR_BAND) { (r: Double, ir: Double) =>
        Calculations.ndvi(r, ir);
      }
    }

    val (min, max) = ndvi.findMinMaxDouble

    println(min, "*******", max)

    // Get color map from the application.conf settings file.
    val colorMap = ColorMap.fromStringDouble(ConfigFactory.load().getString("tutorial.ndviColormap")).get

    // Render this NDVI using the color breaks as a PNG,
    // and write the PNG to disk.
    println("Rendering PNG and saving to disk...")
    ndvi.renderPng(colorMap).write(ndviPath)
  }
}

```

#### CreateNDWIPng.scala

```scala
package edu.gmu.stc.raster.landsat

import com.typesafe.config.ConfigFactory
import edu.gmu.stc.analysis.MaskBandsRandGandNIR
import geotrellis.raster._
import geotrellis.raster.io.geotiff._
import geotrellis.raster.render._


/**
  * Created by FroehlingGallier on 10/13/16.
  */
object CreateNDWIPng {

  val maskedPath = "data/r-g-nir.tif"
  val ndwiPath = "data/ndwi.png"

  def main(args: Array[String]): Unit = {
    val ndwi = {
      // Convert the tile to type double values,
      // because we will be performing an operation that
      // produces floating point values.
      println("Reading in multiband image...")
      val tile = MultibandGeoTiff(maskedPath).tile.convert(DoubleConstantNoDataCellType)

      // Use the combineDouble method to map over the red and infrared values
      // and perform the NDVI calculation.
      println("Performing NDWI calculation...")
      tile.combineDouble(MaskBandsRandGandNIR.G_BAND, MaskBandsRandGandNIR.NIR_BAND) { (g: Double, ir: Double) =>
        Calculations.ndwi(g, ir);
      }
    }

    // Get color map from the application.conf settings file.
    val colorMap = ColorMap.fromStringDouble(ConfigFactory.load().getString("tutorial.ndwiColormap")).get

    // Render this NDVI using the color breaks as a PNG,
    // and write the PNG to disk.
    println("Rendering PNG and saving to disk...")
    ndwi.renderPng(colorMap).write(ndwiPath)
  }
}

```

#### IngestImage.scala

```scala
package edu.gmu.stc.raster.landsat

import java.io.File

import geotrellis.proj4._
import geotrellis.raster._
import geotrellis.raster.resample._
import geotrellis.spark._
import geotrellis.spark.io._
import geotrellis.spark.io.file._
import geotrellis.spark.io.hadoop._
import geotrellis.spark.io.index._
import geotrellis.spark.pyramid._
import geotrellis.spark.tiling._
import geotrellis.vector._
import org.apache.spark._
import org.apache.spark.rdd._

import scala.io.StdIn

object IngestImage {
  val inputPath = "file://" + new File("data/r-g-nir.tif").getAbsolutePath
  val outputPath = "data/catalog"
  def main(args: Array[String]): Unit = {
    // Setup Spark to use Kryo serializer.
    val conf =
      new SparkConf()
        .setMaster("local[*]")
        .setAppName("Spark Tiler")
        .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        .set("spark.kryo.registrator", "geotrellis.spark.io.kryo.KryoRegistrator")

    val sc = new SparkContext(conf)
    try {
      run(sc)
      // Pause to wait to close the spark context,
      // so that you can check out the UI at http://localhost:4040
      println("Hit enter to exit.")
      StdIn.readLine()
    } finally {
      sc.stop()
    }
  }

  def fullPath(path: String) = new java.io.File(path).getAbsolutePath

  def run(implicit sc: SparkContext) = {
    // Read the geotiff in as a single image RDD,
    // using a method implicitly added to SparkContext by
    // an implicit class available via the
    // "import geotrellis.spark.io.hadoop._ " statement.
    val inputRdd: RDD[(ProjectedExtent, MultibandTile)] =
      sc.hadoopMultibandGeoTiffRDD(inputPath)

    // Use the "TileLayerMetadata.fromRdd" call to find the zoom
    // level that the closest match to the resolution of our source image,
    // and derive information such as the full bounding box and data type.
    val (_, rasterMetaData) =
      TileLayerMetadata.fromRdd(inputRdd, FloatingLayoutScheme(512))

    // Use the Tiler to cut our tiles into tiles that are index to a floating layout scheme.
    // We'll repartition it so that there are more partitions to work with, since spark
    // likes to work with more, smaller partitions (to a point) over few and large partitions.
    val tiled: RDD[(SpatialKey, MultibandTile)] =
      inputRdd
        .tileToLayout(rasterMetaData.cellType, rasterMetaData.layout, Bilinear)
        .repartition(100)

    // We'll be tiling the images using a zoomed layout scheme
    // in the web mercator format (which fits the slippy map tile specification).
    // We'll be creating 256 x 256 tiles.
    val layoutScheme = ZoomedLayoutScheme(WebMercator, tileSize = 256)

    // We need to reproject the tiles to WebMercator
    val (zoom, reprojected): (Int, RDD[(SpatialKey, MultibandTile)] with Metadata[TileLayerMetadata[SpatialKey]]) =
      MultibandTileLayerRDD(tiled, rasterMetaData)
        .reproject(WebMercator, layoutScheme, Bilinear)

    // Create the attributes store that will tell us information about our catalog.
    val attributeStore = FileAttributeStore(outputPath)

    // Create the writer that we will use to store the tiles in the local catalog.
    val writer = FileLayerWriter(attributeStore)

    // Pyramiding up the zoom levels, write our tiles out to the local file system.
    Pyramid.upLevels(reprojected, layoutScheme, zoom, Bilinear) { (rdd, z) =>
      val layerId = LayerId("landsat", z)
      // If the layer exists already, delete it out before writing
      if(attributeStore.layerExists(layerId)) {
        new FileLayerManager(attributeStore).delete(layerId)
      }
      writer.write(layerId, rdd, ZCurveKeyIndexMethod)
    }
  }
}

```

#### Serve.scala

extends App with Service

启动一个Scala项目，有两种方法：

利用App特质：

```scala
object Application extends App {
   println("Hello World")
}
```

使用main：

```scala
object Application {
    def main(args: Array[String]): Unit = {
        println("Hello World");
    }
}
```

那么，这两种启动项目的方式有什么不同呢？

`App`是scala语言内置的一个`特质`，使用它，则把对应object内部的整体作为scala main的一部分，有`延迟启动`的特性。

同时，`命令行参数args`也作为App特质的一部分，可以被开发者直接使用。

而main函数则是scala的默认启动方式。

所以，这两种启动方式`没有对错之分`。

不过，使用App特质时，可能导致extends App的object内部`有错误时整个项目启动失败`。

所以，`用main的方式启动项目更加稳妥`。
 

```scala
package edu.gmu.stc.raster.landsat

import geotrellis.raster._
import geotrellis.raster.render._
import geotrellis.spark._
import geotrellis.spark.io._
import geotrellis.spark.io.file._

import akka.actor._
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.{ActorMaterializer, Materializer}

import scala.concurrent._
import com.typesafe.config.ConfigFactory
import edu.gmu.stc.analysis.MaskBandsRandGandNIR.{R_BAND, G_BAND, NIR_BAND, TIRS1_BAND}

object Serve extends App with Service {
  val catalogPath = new java.io.File("data/catalog").getAbsolutePath
  // Create a reader that will read in the indexed tiles we produced in IngestImage.
  val fileValueReader = FileValueReader(catalogPath)
  def reader(layerId: LayerId) = fileValueReader.reader[SpatialKey, MultibandTile](layerId)
  val ndviColorMap =
    ColorMap.fromStringDouble(ConfigFactory.load().getString("tutorial.ndviColormap")).get
  val ndwiColorMap =
    ColorMap.fromStringDouble(ConfigFactory.load().getString("tutorial.ndwiColormap")).get
  val lstColorMap =
    ColorMap.fromStringDouble(ConfigFactory.load().getString("tutorial.lstColormap")).get

  override implicit val system = ActorSystem("tutorial-system")
  override implicit val executor = system.dispatcher
  override implicit val materializer = ActorMaterializer()
  override val logger = Logging(system, getClass)

  Http().bindAndHandle(root, "0.0.0.0", 8080)
}

trait Service {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: Materializer
  val logger: LoggingAdapter

  def pngAsHttpResponse(png: Png): HttpResponse =
    HttpResponse(entity = HttpEntity(ContentType(MediaTypes.`image/png`), png.bytes))

  def root =
    pathPrefix(Segment / IntNumber / IntNumber / IntNumber) { (render, zoom, x, y) =>
      complete {
        Future {
          // Read in the tile at the given z/x/y coordinates.
          val tileOpt: Option[MultibandTile] =
            try {
              Some(Serve.reader(LayerId("landsat", zoom)).read(x, y))
            } catch {
              case _: ValueNotFoundError =>
                None
            }
          render match {
            case "ndvi" =>
              tileOpt.map { tile =>
                // Compute the NDVI
                val ndvi =
                  tile.convert(DoubleConstantNoDataCellType).combineDouble(R_BAND, NIR_BAND) { (r, ir) =>
                    Calculations.ndvi(r, ir);
                  }
                // Render as a PNG
                val png = ndvi.renderPng(Serve.ndviColorMap)
                pngAsHttpResponse(png)
              }
            case "ndwi" =>
              tileOpt.map { tile =>
                // Compute the NDWI
                val ndwi =
                  tile.convert(DoubleConstantNoDataCellType).combineDouble(G_BAND, NIR_BAND) { (g, ir) =>
                    Calculations.ndwi(g, ir)
                  }
                // Render as a PNG
                val png = ndwi.renderPng(Serve.ndwiColorMap)
                pngAsHttpResponse(png)
              }
            case "lst" =>
              tileOpt.map { tile =>
                // Compute the LST
                val ndvi =
                  tile.convert(DoubleConstantNoDataCellType).combineDouble(R_BAND, NIR_BAND) { (r, ir) =>
                    Calculations.ndvi(r, ir);
                  }

                val (ndvi_min, ndvi_max) = ndvi.findMinMaxDouble
                val lst =
                  tile.convert(DoubleConstantNoDataCellType).combineDouble(G_BAND, NIR_BAND, TIRS1_BAND) { (g, ir, tirs) =>
                    Calculations.lst(g, ir, tirs, ndvi_min, ndvi_max)
                  }

                val (min, max) = lst.findMinMaxDouble
                println(min, "***********", max)

                // Render as a PNG
                val lst_normalize = lst.normalize(292.0, 323.0, 0.0, 1.0)
                val png = lst_normalize.renderPng(Serve.lstColorMap)
                pngAsHttpResponse(png)
              }
          }
        }
      }
    } ~
      pathEndOrSingleSlash {
        getFromFile("static/index.html")
      } ~
      pathPrefix("") {
        getFromDirectory("static")
      }
}

```


### SparkCity/application/src/main/scala/edu/gmu/vector/landscape/

#### ComputeLandscape.scala

```scala
package edu.gmu.vector.landscape

import com.vividsolutions.jts.geom
import com.vividsolutions.jts.geom.{Geometry, LineString, MultiPolygon, Polygon}
import org.apache.spark.internal.Logging

import scala.math._

/**
  * Created by Fei Hu on 3/20/18.
  * Reference
  * 1. Kim, J.H., Gu, D., Sohn, W., Kil, S.H., Kim, H. and Lee, D.K., 2016. Neighborhood landscape
  *    spatial patterns and land surface temperature: an empirical study on single-family residential
  *    areas in Austin, Texas. International journal of environmental research and public health,
  *    13(9), p.880.
  */
object ComputeLandscape extends Logging{

  def computeCoverPercent(geoCover: Geometry, geoFeatureList: Iterable[Geometry]): Double = {
    val featureSum = geoFeatureList.foldLeft[Double](0.0)
      {case (sum, geoFeature) => {
        geoFeature match {
          case polygon: Polygon => sum + polygon.getArea
          case mulPolygon: MultiPolygon => sum + mulPolygon.getArea
          case lineString: LineString => sum + lineString.getLength
          case _ => {
            logError("Do not support this kind of geometry type: " + geoFeature.getClass)
            0.0
          }
        }
      }}

    if (geoFeatureList.head.isInstanceOf[LineString]) {
      featureSum / geoCover.getLength
    } else {
      featureSum / geoCover.getArea
    }

  }

  def countFeatureNum(geoCover: Geometry, geoFeatureList: Iterable[Geometry]): Int = geoFeatureList.size

  def computeMeanPatchSize(geoCover: Geometry, geoFeatureList: Iterable[Geometry]): Double = {
    val areaSum = geoFeatureList.foldLeft[Double](0.0)((sum, geoFeature) => sum + geoFeature.getArea)
    areaSum / geoFeatureList.size
  }

  def computeMeanShapeIndex(geoCover: Geometry, geoFeatureList: Iterable[Geometry]): Double = {
    val shapeIndexSum = geoFeatureList.foldLeft[Double](0.0){
      case (sum, geoFeature) => {
        val shapeIndex = 0.25 * geoFeature.getLength / sqrt(geoFeature.getArea)
        sum + shapeIndex
      }
    }
    shapeIndexSum / geoFeatureList.size
  }

  def computeMeanNearestNeighborDistance(geoCover: Geometry, geoFeatureList: Iterable[Geometry]): Double = {
    val features = geoFeatureList.toArray
    val max_val = 9999999.999
    val distances: Array[Double] = Array.fill[Double](features.length)(max_val)

    if (features.length == 0) {
      Double.NaN
    }
    else if (features.length == 1) {
      features.head.getCentroid.distance(geoCover)
    }
    else {
      for (i <- features.indices) {
        for (j <- i + 1 until features.length) {
          val dist = features(i).distance(features(j))
          if (distances(i) > dist) distances(i) = dist
          if (distances(j) > dist) distances(j) = dist
        }
      }
      distances.sum/distances.length
    }
  }

  def computePatchCohesionIndex(geoCover: Geometry, geoFeatureList: Iterable[Geometry]): Double = {

    val (areaSum, perimeterSum, perimeterAreaSum) = geoFeatureList.foldLeft[(Double, Double, Double)]((0.0, 0.0, 0.0)) {
      case ((areaSum_, perimeterSum_, perimeterAreaSum_), geoFeature) => {
        val perimeter = geoFeature.getLength
        val area = geoFeature.getArea

        (areaSum_ + area, perimeterSum_ + perimeter, perimeterAreaSum_ + perimeter * sqrt(area))
      }
    }

    //println("perimeterSum: ", perimeterSum, " perimeterAreaSum: " + perimeterAreaSum)

    (1 - perimeterSum / perimeterAreaSum) / (1 - 1/sqrt(geoCover.getArea))
  }

  def computeRoadPercent(geoCover: Geometry, geoFeatureList: Iterable[Geometry]): Double = {
    val featureSum = geoFeatureList.foldLeft[Double](0.0)
      {case (sum, geoFeature) => {
        sum + geoFeature.getLength
      }}

    featureSum / geoCover.getLength
  }
}

```

### SparkCity/application/src/main/scala/edu/gmu/stc/vector/

#### VectorUtil.scala

```scala
package edu.gmu.stc.vector

import com.vividsolutions.jts.geom.{Envelope, Geometry}
import org.geotools.geometry.jts.JTS
import org.geotools.referencing.CRS
import org.opengis.referencing.crs.CoordinateReferenceSystem
import org.opengis.referencing.operation.MathTransform

/**
  * Created by Fei Hu on 3/24/18.
  */
object VectorUtil {

  def getCRSTransform(sourceCRSStr: String,
                      sourceLongitudeFirst: Boolean,
                      targetCRSStr: String,
                      targetLongitudeFirst: Boolean): MathTransform = {

    val sourceCRS = CRS.decode(sourceCRSStr, sourceLongitudeFirst)
    val targetCRS = CRS.decode(targetCRSStr, targetLongitudeFirst)
    CRS.findMathTransform(sourceCRS, targetCRS)
  }

  def transformCRS(inputGeometry: Geometry,
                   sourceCRSStr: String,
                   sourceLongitudeFirst: Boolean,
                   targetCRSStr: String,
                   targetLongitudeFirst: Boolean): Geometry = {
    val transform = getCRSTransform(sourceCRSStr, sourceLongitudeFirst, targetCRSStr, targetLongitudeFirst)
    JTS.transform(inputGeometry, transform)
  }

  def transformCRS(inputGeometry: Envelope,
                   sourceCRSStr: String,
                   sourceLongitudeFirst: Boolean,
                   targetCRSStr: String,
                   targetLongitudeFirst: Boolean): Envelope = {
    val transform = getCRSTransform(sourceCRSStr, sourceLongitudeFirst, targetCRSStr, targetLongitudeFirst)
    JTS.transform(inputGeometry, transform)
  }


}

```


### SparkCity/application/src/main/scala/edu/gmu/stc/raster/operation/

#### RasterOperation.scala

```scala
package edu.gmu.stc.raster.operation

import com.vividsolutions.jts.geom._
import edu.gmu.stc.analysis.ComputeSpectralIndex.logInfo
import edu.gmu.stc.analysis.MaskBandsRandGandNIR
import edu.gmu.stc.raster.io.GeoTiffReaderHelper
import edu.gmu.stc.raster.landsat.Calculations
import edu.gmu.stc.vector.VectorUtil
import edu.gmu.stc.vector.io.ShapeFileReaderHelper
import geotrellis.raster.{DoubleConstantNoDataCellType, MultibandTile, Tile}
import geotrellis.raster.io.geotiff.MultibandGeoTiff
import geotrellis.vector.Extent
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.geotools.geometry.jts.JTS
import org.apache.spark.internal.Logging

/**
  * Created by Fei Hu on 3/21/18.
  */
object RasterOperation extends  Logging {

  def clipToPolygons(extent: Extent,
                     tiles: Array[Tile],
                     geometries: List[Geometry]): List[Geometry] = {

    if (tiles.exists(tile => tile.rows != tiles.head.rows || tile.cols != tiles.head.cols)) {
      throw new AssertionError("There are two input tiles with different size")
    }

    val cellWidth = (extent.xmax - extent.xmin) / tiles.head.cols
    val cellHeight = (extent.ymax - extent.ymin) / tiles.head.rows

    geometries.map {

      case point: Point => {
        val meanValues = tiles.map(
          tile => getPixelValueFromTile(point, tile, extent, cellWidth, cellHeight)
        )

        meanValues.foreach(meanValue => {
          point.setUserData(point.getUserData + "\t" + meanValue)
        })

        point
      }

      case polygon: Polygon => {
        val tileMeanValuePairs = tiles.map(tile => (tile, tile.polygonalMean(extent, polygon)))

        tileMeanValuePairs.foreach{
          case (tile, meanValue) => {
            if (meanValue.isNaN) {
              val point = polygon.getCentroid
              val value = getPixelValueFromTile(point, tile, extent, cellWidth, cellHeight)
              polygon.setUserData(polygon.getUserData + "\t" + value)
            } else {
              polygon.setUserData(polygon.getUserData + "\t" + meanValue)
            }
          }
        }

        polygon
      }

      case multiPolygon: MultiPolygon => {

        val tileMeanValuePairs = tiles.map(tile => (tile, tile.polygonalMean(extent, multiPolygon)))

        tileMeanValuePairs.foreach{
          case (tile, meanValue) => {
            if (meanValue.isNaN) {
              val point = multiPolygon.getCentroid
              val value = getPixelValueFromTile(point, tile, extent, cellWidth, cellHeight)
              multiPolygon.setUserData(multiPolygon.getUserData + "\t" + value)
            } else {
              multiPolygon.setUserData(multiPolygon.getUserData + "\t" + meanValue)
            }
          }
        }

        multiPolygon
      }

      case s: Any => {
        throw new AssertionError("Unconsistent geometry: " + s.getClass)
      }
    }
  }

  def getPixelValueFromTile(point: Point,
                            tile: Tile,
                            extent: Extent,
                            cellWidth: Double, cellHeight: Double): Double = {
    if (!extent.contains(point)) {
      Double.NaN
    } else {
      val x = (point.getX - extent.xmin) / cellWidth
      val y = (point.getY - extent.ymin) / cellHeight

      tile.getDouble(x.toInt, y.toInt)
    }
  }

  def getPixelValueFromTile(point: Point, tile: Tile, extent: Extent): Double = {
    if (!extent.contains(point)) {
      Double.NaN
    } else {
      val cellWidth = (extent.xmax - extent.xmin) / tile.cols
      val cellHeight = (extent.ymax - extent.ymin) / tile.rows

      getPixelValueFromTile(point, tile, extent, cellWidth, cellHeight)
    }
  }

  def readMultiBandGeoTiff(hConf: Configuration,
                           landsatMulBandFilePath: Path): (Extent, MultibandTile) = {
    val geoTiff = GeoTiffReaderHelper.readMultiBand(landsatMulBandFilePath, hConf)
    val tile = geoTiff.tile.convert(DoubleConstantNoDataCellType)
    (geoTiff.extent, tile)
  }

  def computeSpectralIndex(multiBandTile: MultibandTile,
                           indexNames: Array[String]): Array[Tile] = {
    val tiles = indexNames.map(indexName => {
      indexName.toLowerCase match {
        case "lst" => {
          val ndviTile = multiBandTile.combineDouble(
            MaskBandsRandGandNIR.R_BAND,
            MaskBandsRandGandNIR.NIR_BAND) {
            (r: Double, ir: Double) => Calculations.ndvi(r, ir)
          }

          val (ndvi_min, ndvi_max) = ndviTile.findMinMaxDouble

          multiBandTile.combineDouble(
            MaskBandsRandGandNIR.R_BAND,
            MaskBandsRandGandNIR.NIR_BAND,
            MaskBandsRandGandNIR.TIRS1_BAND) {
            (r: Double, nir: Double, tirs1: Double) => Calculations.lst(r, nir, tirs1, ndvi_min, ndvi_max)
          }
        }

        case "ndvi" => {
          multiBandTile.combineDouble(
            MaskBandsRandGandNIR.R_BAND,
            MaskBandsRandGandNIR.NIR_BAND) {
            (r: Double, ir: Double) => Calculations.ndvi(r, ir)
          }
        }

        case "ndwi" => {
          multiBandTile.combineDouble(
            MaskBandsRandGandNIR.G_BAND,
            MaskBandsRandGandNIR.NIR_BAND) {
            (g: Double, nir: Double) => Calculations.ndwi(g, nir)
          }
        }

        case "mndwi" => {
          multiBandTile.combineDouble(
            MaskBandsRandGandNIR.G_BAND,
            MaskBandsRandGandNIR.SWIR1_BAND) {
            (g: Double, swir1: Double) => Calculations.mndwi(g, swir1)
          }
        }

        case "ndbi" => {
          multiBandTile.combineDouble(
            MaskBandsRandGandNIR.SWIR1_BAND,
            MaskBandsRandGandNIR.NIR_BAND) {
            (swir1: Double, nir: Double) => Calculations.ndbi(swir1, nir)
          }
        }

        case "ndii" => {
          multiBandTile.combineDouble(
            MaskBandsRandGandNIR.R_BAND,
            MaskBandsRandGandNIR.TIRS1_BAND) {
            (r: Double, tirs1: Double) => Calculations.ndii(r, tirs1)
          }
        }

        case "ndisi" => {
          multiBandTile.combineDouble(
            MaskBandsRandGandNIR.TIRS1_BAND,
            MaskBandsRandGandNIR.G_BAND,
            MaskBandsRandGandNIR.NIR_BAND,
            MaskBandsRandGandNIR.SWIR1_BAND) {
            (tirs1: Double, g: Double, nir: Double, swir1: Double) => Calculations.ndisi(tirs1, g, nir, swir1)
          }
        }

        case default => {
          throw new AssertionError("Do not support this index: " + default)
        }
      }
    })

    tiles
  }



  def computeSpectralIndex(hConf: Configuration,
                           landsatMulBandFilePath: Path,
                           indexNames: Array[String]): (Extent, Array[Tile]) = {
    /*val geotiff = GeoTiffReaderHelper.readMultiBand(landsatMulBandFilePath, hConf)
    val tile = geotiff.tile.convert(DoubleConstantNoDataCellType)*/

    val  (extent, tile) = readMultiBandGeoTiff(hConf, landsatMulBandFilePath)

    val tiles = indexNames.map(indexName => {
      indexName.toLowerCase match {
        case "lst" => {
          val ndviTile = tile.combineDouble(
            MaskBandsRandGandNIR.R_BAND,
            MaskBandsRandGandNIR.NIR_BAND) {
            (r: Double, ir: Double) => Calculations.ndvi(r, ir)
          }

          val (ndvi_min, ndvi_max) = ndviTile.findMinMaxDouble

          tile.combineDouble(
            MaskBandsRandGandNIR.R_BAND,
            MaskBandsRandGandNIR.NIR_BAND,
            MaskBandsRandGandNIR.TIRS1_BAND) {
            (r: Double, nir: Double, tirs1: Double) => Calculations.lst(r, nir, tirs1, ndvi_min, ndvi_max)
          }
        }

        case "ndvi" => {
          tile.combineDouble(
            MaskBandsRandGandNIR.R_BAND,
            MaskBandsRandGandNIR.NIR_BAND) {
            (r: Double, ir: Double) => Calculations.ndvi(r, ir)
          }
        }

        case "ndwi" => {
          tile.combineDouble(
            MaskBandsRandGandNIR.G_BAND,
            MaskBandsRandGandNIR.NIR_BAND) {
            (g: Double, nir: Double) => Calculations.ndwi(g, nir)
          }
        }

        case "mndwi" => {
          tile.combineDouble(
            MaskBandsRandGandNIR.G_BAND,
            MaskBandsRandGandNIR.SWIR1_BAND) {
            (g: Double, swir1: Double) => Calculations.mndwi(g, swir1)
          }
        }

        case "ndbi" => {
          tile.combineDouble(
            MaskBandsRandGandNIR.SWIR1_BAND,
            MaskBandsRandGandNIR.NIR_BAND) {
            (swir1: Double, nir: Double) => Calculations.ndbi(swir1, nir)
          }
        }

        case "ndii" => {
          tile.combineDouble(
            MaskBandsRandGandNIR.R_BAND,
            MaskBandsRandGandNIR.TIRS1_BAND) {
            (r: Double, tirs1: Double) => Calculations.ndii(r, tirs1)
          }
        }

        case "ndisi" => {
          tile.combineDouble(
            MaskBandsRandGandNIR.TIRS1_BAND,
            MaskBandsRandGandNIR.G_BAND,
            MaskBandsRandGandNIR.NIR_BAND,
            MaskBandsRandGandNIR.SWIR1_BAND) {
            (tirs1: Double, g: Double, nir: Double, swir1: Double) => Calculations.ndisi(tirs1, g, nir, swir1)
          }
        }

        case default => {
          throw new AssertionError("Do not support this index: " + default)
        }
      }
    })

    (extent, tiles)
  }

  def computeLSTAndNDVI(hConf: Configuration,
                        landsatMulBandFilePath: Path): (Extent, Tile, Tile) = {
    val geotiff = GeoTiffReaderHelper.readMultiBand(landsatMulBandFilePath, hConf)
    val tile = geotiff.tile.convert(DoubleConstantNoDataCellType)
    val ndviTile = tile.combineDouble(MaskBandsRandGandNIR.R_BAND,
      MaskBandsRandGandNIR.NIR_BAND,
      MaskBandsRandGandNIR.TIRS1_BAND) {
      (r: Double, ir: Double, tirs: Double) => Calculations.ndvi(r, ir);
    }

    val (ndvi_min, ndvi_max) = ndviTile.findMinMaxDouble


    val lstTile = tile.combineDouble(MaskBandsRandGandNIR.R_BAND,
      MaskBandsRandGandNIR.NIR_BAND,
      MaskBandsRandGandNIR.TIRS1_BAND) {
      (r: Double, ir: Double, tirs: Double) => Calculations.lst(r, ir, tirs, ndvi_min, ndvi_max);
    }

    //GeoTiff(lstTile, geotiff.extent, CRS.fromName("epsg:32618")).write("lst_test.tif")
    (geotiff.extent, lstTile, ndviTile)
  }

  def extractMeanValueFromTileByGeometry(hConfFile: String,
                                         rasterFile: String,
                                         rasterCRS: String,
                                         rasterLongitudeFirst: Boolean,
                                         vectorIndexTableName: String,
                                         vectorCRS: String,
                                         vectorLongitudeFirst: Boolean,
                                         indexNames: Array[String]
                                        ): List[Geometry] = {
    val hConf = new Configuration()
    val fs = FileSystem.get(hConf)
    val inputStream = fs.open(new Path(hConfFile))
    hConf.addResource(inputStream)

    val (rasterExtent, indexTiles) = RasterOperation.computeSpectralIndex(hConf, new Path(rasterFile), indexNames)

    val raster2osm_CRSTransform = VectorUtil.getCRSTransform(
      rasterCRS, rasterLongitudeFirst, vectorCRS, vectorLongitudeFirst
    )
    val osm2raster_CRSTransform = VectorUtil.getCRSTransform(
      vectorCRS, rasterLongitudeFirst, rasterCRS, vectorLongitudeFirst
    )

    val bbox = if (rasterCRS.equalsIgnoreCase(vectorCRS)) {
      new Envelope(rasterExtent.xmin, rasterExtent.xmax, rasterExtent.ymin, rasterExtent.ymax)
    } else {
      val envelope = new Envelope(rasterExtent.xmin, rasterExtent.xmax, rasterExtent.ymin, rasterExtent.ymax)
      JTS.transform(envelope, raster2osm_CRSTransform)
    }

    val polygons = ShapeFileReaderHelper.read(
      hConf,
      vectorIndexTableName,
      bbox.getMinX,
      bbox.getMinY,
      bbox.getMaxX,
      bbox.getMaxY,
      hasAttribute = true)
      .map(geometry => JTS.transform(geometry, osm2raster_CRSTransform))

    val polygonsWithIndices = RasterOperation
      .clipToPolygons(rasterExtent, indexTiles, polygons)
      .filter(g => !g.getUserData.toString.contains("NaN"))
      .map(geometry => JTS.transform(geometry, raster2osm_CRSTransform))

    logInfo("*********** Number of Polygons: " + polygonsWithIndices.size)
    logInfo("*********** Number of Input Polygons: " + polygons.size)

    polygonsWithIndices
  }

  def extractMeanValueFromTileByGeometry(hConf: Configuration,
                                         rasterExtent: Extent,
                                         indexTiles: Array[Tile],
                                         rasterCRS: String,
                                         rasterLongitudeFirst: Boolean,
                                         vectorIndexTableName: String,
                                         vectorCRS: String,
                                         vectorLongitudeFirst: Boolean
                                        ): List[Geometry] = {
    val raster2osm_CRSTransform = VectorUtil.getCRSTransform(
      rasterCRS, rasterLongitudeFirst, vectorCRS, vectorLongitudeFirst
    )
    val osm2raster_CRSTransform = VectorUtil.getCRSTransform(
      vectorCRS, rasterLongitudeFirst, rasterCRS, vectorLongitudeFirst
    )

    val bbox = if (rasterCRS.equalsIgnoreCase(vectorCRS)) {
      new Envelope(rasterExtent.xmin, rasterExtent.xmax, rasterExtent.ymin, rasterExtent.ymax)
    } else {
      val envelope = new Envelope(rasterExtent.xmin, rasterExtent.xmax, rasterExtent.ymin, rasterExtent.ymax)
      JTS.transform(envelope, raster2osm_CRSTransform)
    }

    val polygons = ShapeFileReaderHelper.read(
      hConf,
      vectorIndexTableName,
      bbox.getMinX,
      bbox.getMinY,
      bbox.getMaxX,
      bbox.getMaxY,
      hasAttribute = true)
      .map(geometry => JTS.transform(geometry, osm2raster_CRSTransform))

    val polygonsWithIndices = RasterOperation
      .clipToPolygons(rasterExtent, indexTiles, polygons)
      .filter(g => !g.getUserData.toString.contains("NaN"))
      .map(geometry => JTS.transform(geometry, raster2osm_CRSTransform))

    logInfo("*********** Number of Polygons: " + polygonsWithIndices.size)
    logInfo("*********** Number of Input Polygons: " + polygons.size)

    polygonsWithIndices
  }
}

```


### SparkCity/application/src/main/scala/edu/gmu/stc/vector/operation/

#### OperationUtil.scala

```scala
package edu.gmu.stc.vector.operation

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileStatus, FileSystem, Path, PathFilter}
import org.apache.hadoop.hdfs.web.WebHdfsFileSystem
import org.apache.spark.rdd.RDD

import scala.collection.mutable
import scala.collection.mutable.HashMap
import scala.util.Random

/**
  * Created by Fei Hu on 1/23/18.
  */
object OperationUtil {

  val random = Random

  val WEBHDFS_GET = "http://%s:50075/webhdfs/v1%s?op=OPEN&namenoderpcaddress=%s:8020&offset=0"

  val hostIPMap: HashMap[String, String] = HashMap("svr-A7-C-U12" -> "10.192.21.139",
    "svr-A7-C-U13" -> "10.192.21.140",
    "svr-A7-C-U14" -> "10.192.21.141",
    "svr-A7-C-U15" -> "10.192.21.142",
    "svr-A7-C-U16" -> "10.192.21.143",
    "svr-A7-C-U17" -> "10.192.21.144",
    "svr-A7-C-U18" -> "10.192.21.145",
    "svr-A7-C-U19" -> "10.192.21.146",
    "svr-A7-C-U20" -> "10.192.21.147",
    "svr-A7-C-U21" -> "10.192.21.148",
    "svr-A7-C-U6" -> "10.192.21.133",
    "localhost" -> "127.0.0.1")

  val HDFS_MASTER_NODE = "10.192.21.148"

  /**
    * monitor the runtime for the task/function
    *
    * @param proc
    * @tparam T
    * @return
    */
  def show_timing[T](proc: => T): Long = {
    val start=System.nanoTime()
    val res = proc // call the code
    val end = System.nanoTime()
    val runtime = (end-start)/1000000000  //seconds
    runtime
  }

  def show_partitionInfo[T](rdd: RDD[T]): Unit = {
    rdd.mapPartitions(parition => List(parition.size).toIterator)
      .collect()
      .foreach(println)
  }

  def getUniqID(id1: Long, id2: Long): Long = {
    (id1 << 32) + id2
  }



  def hdfsToLocal(hdfsPath: String, localPath: String) = {
    val conf = new Configuration()
    val fs = FileSystem.get(conf)
    val localFs = FileSystem.getLocal(conf)

    val src = new Path(hdfsPath)
    val dest = new Path(localPath)

    if (localFs.exists(dest)) {
      localFs.delete(dest, true)
    }

    fs.copyToLocalFile(src, dest)
  }

  def getFileNum(dirPath: String, pathFilter: String = "part-"): Int = {
    val conf = new Configuration()
    val fs = FileSystem.get(conf)
    val fileStatuses = listFileStatus(dirPath, pathFilter, fs)

    fileStatuses.length
  }

  def listFileStatus(dirPath: String, pathFilter: String = "part-", fs: FileSystem): Array[FileStatus] = {
    val fileStatuses = fs.listStatus(new Path(dirPath), new PathFilter {
      override def accept(path: Path): Boolean = path.toString.contains(pathFilter)
    })

    fileStatuses
  }

  def getWebHDFSUrl(path: String, pathFilter: String = "part-"): String = {
    val conf = new Configuration()
    val fs = FileSystem.get(conf)
    val fileStatusArray = listFileStatus(path, pathFilter, fs)

    val hdfsUrls = fileStatusArray.indices.map(i => {
      val path = fileStatusArray(i).getPath
      val locations = fs.getFileBlockLocations(path, 0, fileStatusArray(i).getLen)
      val hosts = locations(random.nextInt(locations.size)).getHosts
      val randomHost = hosts(random.nextInt(hosts.size))
      val hostIP = hostIPMap.getOrElse(randomHost, "127.0.0.1")

      WEBHDFS_GET.format(hostIP, path.toUri.getRawPath, HDFS_MASTER_NODE)
    })

    hdfsUrls.mkString(",")
  }

  def main(args: Array[String]): Unit = {
    OperationUtil.hdfsToLocal("/Users/feihu/Documents/GitHub/GeoSpark/config", "/Users/feihu/Desktop/111/111/111/")
    println(OperationUtil.getWebHDFSUrl("/Users/feihu/Desktop", "Screen Shot"))
  }

}

```

#### Overlap.scala

```scala
package edu.gmu.stc.vector.operation

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{SQLContext, SparkSession}
import org.datasyslab.geospark.enums.{GridType, IndexType}
import org.datasyslab.geospark.formatMapper.shapefileParser.ShapefileReader
import org.datasyslab.geospark.spatialOperator.JoinQuery
import org.datasyslab.geospark.spatialRDD.PolygonRDD
import org.datasyslab.geosparksql.utils.GeoSparkSQLRegistrator


/**
  * Created by Fei Hu
  */
object Overlap {

  val isDebug: Boolean = System.getProperty("spark.isDebug", "false").toBoolean

  val LOG: Logger = Logger.getLogger(Overlap.getClass)

  def intersect(sparkSession: SparkSession,
                gridType: String,
                indexType: String,
                shpFile1: String, shpFile2: String,
                numPartitions: Int,
                outputFile: String): Unit = {

    val plg1RDD = ShapefileReader.readToPolygonRDD(sparkSession.sparkContext, shpFile1)
    val plg2RDD = ShapefileReader.readToPolygonRDD(sparkSession.sparkContext, shpFile2)

    // get the boundary of RDD
    plg1RDD.analyze()
    plg2RDD.analyze()

    // partition RDD and buildIndex for each partition
    val PARTITION_TYPE = GridType.getGridType(gridType)
    val INDEX_TYPE = IndexType.getIndexType(indexType)

    plg1RDD.spatialPartitioning(PARTITION_TYPE, numPartitions)
    plg1RDD.buildIndex(INDEX_TYPE, true)
    plg1RDD.indexedRDD = plg1RDD.indexedRDD.cache()


    plg2RDD.spatialPartitioning(plg1RDD.getPartitioner)
    plg2RDD.buildIndex(INDEX_TYPE, true)
    plg2RDD.indexedRDD = plg2RDD.indexedRDD.cache()

    // overlap operation
    val intersectRDD = JoinQuery.SpatialIntersectQuery(plg1RDD, plg2RDD, true, true)
    val intersectedResult = new PolygonRDD(intersectRDD)


    if (isDebug) {
      val numOfPolygons = intersectedResult.countWithoutDuplicates()
      LOG.info(s"****** Number of polygons: $numOfPolygons")
    }

    intersectedResult.saveAsGeoJSON(outputFile)
  }
}

```



### SparkCity/application/src/main/scala/edu/gmu/stc/vector/osm/

#### FeatureClass.scala

```scala
package edu.gmu.stc.vector.osm

import scala.collection.immutable.HashSet

/**
  * Created by Fei Hu on 3/23/18.
  */
object FeatureClass {

  val landuse_RedFeatures: HashSet[String] = HashSet("residential", "industrial", "commercial",
    "recreation_ground", "retail", "military", "quarry", "heath", "brownfield", "construction",
    "railway")

  val landuse_GreenFeatures: HashSet[String] = HashSet("forest", "park", "cemetery", "allotments",
    "meadow", "nature_reserve", "orchard", "vineyard", "scrub", "grass", "national_park", "basin",
    "village_green", "plant_nursery", "greenfield", "farm", "farmland", "farmyard")

  def isRedFeature(feature: String): Boolean = landuse_RedFeatures.contains(feature.toLowerCase())
}

```


### SparkCity/application/src/main/scala/edu/gmu/stc/vector/rdd/

#### index/IndexOperator.scala

extends Serializable, Serializable是一个空接口，目的只是`简单的标识一个类的对象可以被序列化`

​> 分布式应用中，就得实现`序列化`，如果不是分布式应用，就`没必要实现序列化`，因为

1. 将对象的状态保存在`存储媒体`中以便可以在以后重新创建出完全相同的副本

2. 按值将对象从`一个应用程序域发送`至`另一个应用程序域`

​ 
​> 需要序列化的情况:

1. 当你想把`内存`中的对象写入到硬盘的时候

2. 有时候`传输`某一类的对象，有时候就要实现Seriailzable接口，最常见的`传输一个字符串`，是JDK里面的类，也实现了`Serializable接口`，所以可以传输

3. 当你想通过`RMI传输对象`的时候

```scala
package edu.gmu.stc.vector.rdd.index

import com.vividsolutions.jts.geom.{Envelope, Geometry, GeometryFactory}
import com.vividsolutions.jts.index.SpatialIndex
import com.vividsolutions.jts.index.quadtree.Quadtree
import com.vividsolutions.jts.index.strtree.STRtree
import edu.gmu.stc.vector.shapefile.meta.ShapeFileMeta
import edu.gmu.stc.vector.shapefile.reader.GeometryReaderUtil
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.spark.internal.Logging
import org.datasyslab.geospark.enums.IndexType

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.HashMap
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

/**
  * Created by Fei Hu on 1/27/18.
  */
class IndexOperator(indexType: String) extends Serializable {

  def buildIndex(iterator: Iterator[Geometry]): Iterator[SpatialIndex] = {
    val spatialIndex: SpatialIndex = if (IndexType.getIndexType(indexType) == IndexType.RTREE) {
                                          new STRtree
                                        } else {
                                          new Quadtree
                                        }

    while (iterator.hasNext) {
      val shapeFileMeta = iterator.next()
      spatialIndex.insert(shapeFileMeta.getEnvelopeInternal, shapeFileMeta)
    }

    spatialIndex.query(new Envelope(0.0, 0.0, 0.0, 0.0))

    List(spatialIndex).toIterator
  }


}

object IndexOperator extends Logging{

  def spatialJoin[T <: Geometry](iterator1: Iterator[SpatialIndex],
                  iterator2: Iterator[T])
  : Iterator[(T, T)] = {
    if (iterator1.isEmpty || iterator2.isEmpty) {
      return List[(T, T)]().iterator
    }

    iterator1.map(spatialIndex => {
      iterator2.flatMap(shapeMeta => {
        val overlapped = spatialIndex.query(shapeMeta.getEnvelopeInternal).asScala
          //.asInstanceOf[Iterator[ShapeFileMeta]]
        overlapped.map(shapeMeta1 => (shapeMeta1.asInstanceOf[T], shapeMeta))
      })
    }).foldLeft(Iterator[(T, T)]())(_ ++ _)
  }

  def geoSpatialIntersection[T <: Geometry](iterator1: Iterator[SpatialIndex],
                                            iterator2: Iterator[T])
  : Iterator[Geometry] = {
    if (iterator1.isEmpty || iterator2.isEmpty) {
      return List[Geometry]().iterator
    }

    iterator1.map(spatialIndex => {
      iterator2.flatMap(g2 => {
        val overlapped = spatialIndex.query(g2.getEnvelopeInternal).asScala
        //.asInstanceOf[Iterator[ShapeFileMeta]]
        overlapped.filter(g1 => g1.asInstanceOf[T].intersects(g2))
          .map(g1 => g1.asInstanceOf[T].intersection(g2))
      })
    }).foldLeft(Iterator[Geometry]())(_ ++ _)
  }

  def geoSpatialJoin[T <: Geometry](iterator1: Iterator[SpatialIndex],
                                            iterator2: Iterator[T])
  : Iterator[(T, T)] = {
    if (iterator1.isEmpty || iterator2.isEmpty) {
      return List[(T, T)]().iterator
    }

    iterator1.map(spatialIndex => {
      iterator2.flatMap(g2 => {
        val overlapped = spatialIndex.query(g2.getEnvelopeInternal).asScala
        overlapped.filter(g1 => g1.asInstanceOf[T].intersects(g2))
          .map(g1 => (g1.asInstanceOf[T], g2))
      })
    }).foldLeft(Iterator[(T, T)]())(_ ++ _)
  }



  def spatialJoinV2(iterator1: Iterator[SpatialIndex],
                  iterator2: Iterator[ShapeFileMeta])
  : Iterator[(Long, Long)] = {
    if (iterator1.isEmpty || iterator2.isEmpty) {
      return List[(Long, Long)]().iterator
    }

    iterator1.map(spatialIndex => {
      iterator2.flatMap(shapeMeta => {
        val overlapped = spatialIndex.query(shapeMeta.getEnvelopeInternal).asScala
        //.asInstanceOf[Iterator[ShapeFileMeta]]
        overlapped.map(shapeMeta1 => (shapeMeta1.asInstanceOf[ShapeFileMeta].getIndex.toLong, shapeMeta.getIndex.toLong))
      })
    }).foldLeft(Iterator[(Long, Long)]())(_ ++ _)
  }

  def spatialIntersect(iterator1: Iterator[SpatialIndex],
                  iterator2: Iterator[ShapeFileMeta])
  : Iterator[Geometry] = {
    val pairList = iterator1.map(spatialIndex => {
      iterator2.flatMap(shapeMeta => {
        val overlapped = spatialIndex.query(shapeMeta.getEnvelopeInternal).asScala
        overlapped.map(shapeMeta1 => (shapeMeta1.asInstanceOf[ShapeFileMeta], shapeMeta))
      })
    }).foldLeft(Iterator[(ShapeFileMeta, ShapeFileMeta)]())(_ ++ _).toList

    val shpPath1 = new Path(pairList.head._1.getFilePath + GeometryReaderUtil.SHP_SUFFIX)
    val shpPath2 = new Path(pairList.head._2.getFilePath + GeometryReaderUtil.SHP_SUFFIX)
    val fs = shpPath1.getFileSystem(new Configuration())
    val shpInputStream1 = fs.open(shpPath1)
    val shpInputStream2 = fs.open(shpPath2)
    val geometryFactory = new GeometryFactory()

    val hashMap1 = new HashMap[Long, Geometry]()
    val hashMap2 = new HashMap[Long, Geometry]()

    val time1 = System.currentTimeMillis()

    pairList.foreach(tuple => {
      if (!hashMap1.contains(tuple._1.getIndex)) {
        val geometry:Geometry = GeometryReaderUtil.readGeometry(shpInputStream1, tuple._1, geometryFactory)
        hashMap1 += (tuple._1.getIndex.toLong -> geometry)
      }

      if (!hashMap2.contains(tuple._2.getIndex)) {
        val geometry:Geometry = GeometryReaderUtil.readGeometry(shpInputStream2, tuple._2, geometryFactory)
        hashMap2 += (tuple._2.getIndex.toLong -> geometry)
      }
    })

    val time2 = System.currentTimeMillis()

    logInfo("********* Reading the number of geometries (%d, %d) took: %d seconds"
      .format(hashMap1.size, hashMap2.size, (time2 - time1)/1000))


    val result = pairList.map(tuple => {
      val geometry1 = hashMap1.getOrElse(tuple._1.getIndex.toLong, None)
      val geometry2 = hashMap2.getOrElse(tuple._2.getIndex.toLong, None)

      if (geometry1 != None && geometry2 != None) {
        geometry1.asInstanceOf[Geometry].intersection(geometry2.asInstanceOf[Geometry])
      } else {
        geometry1.asInstanceOf[Geometry]
      }
    }).toIterator

    val time3 = System.currentTimeMillis()

    logInfo("********* Intersecting %d pairs of geometries takes about : %d seconds"
      .format(pairList.size, (time3 - time2)/1000))

    result
  }

  def spatialIntersect(iterator: Iterator[(ShapeFileMeta, ShapeFileMeta)])
  : Iterator[Geometry] = {
    val pairList = iterator.toList

    val shpPath1 = new Path(pairList.head._1.getFilePath + GeometryReaderUtil.SHP_SUFFIX)
    val shpPath2 = new Path(pairList.head._2.getFilePath + GeometryReaderUtil.SHP_SUFFIX)
    val fs = shpPath1.getFileSystem(new Configuration())
    val shpInputStream1 = fs.open(shpPath1)
    val shpInputStream2 = fs.open(shpPath2)
    val geometryFactory = new GeometryFactory()

    val hashMap1 = new HashMap[Long, Geometry]()
    val hashMap2 = new HashMap[Long, Geometry]()

    val time1 = System.currentTimeMillis()

    pairList.foreach(tuple => {
      if (!hashMap1.contains(tuple._1.getIndex)) {
        val geometry:Geometry = GeometryReaderUtil.readGeometry(shpInputStream1, tuple._1, geometryFactory)
        hashMap1 += (tuple._1.getIndex.toLong -> geometry)
      }

      if (!hashMap2.contains(tuple._2.getIndex)) {
        val geometry:Geometry = GeometryReaderUtil.readGeometry(shpInputStream2, tuple._2, geometryFactory)
        hashMap2 += (tuple._2.getIndex.toLong -> geometry)
      }
    })

    val time2 = System.currentTimeMillis()

    logInfo("********* Reading the number of geometries (%d, %d) took: %d seconds"
      .format(hashMap1.size, hashMap2.size, (time2 - time1)/1000))


    val result = pairList.map(tuple => {
      val geometry1 = hashMap1.getOrElse(tuple._1.getIndex.toLong, None)
      val geometry2 = hashMap2.getOrElse(tuple._2.getIndex.toLong, None)

      geometry1.asInstanceOf[Geometry]

      val intersection = if (geometry1 != None && geometry2 != None && geometry1.asInstanceOf[Geometry].intersects(geometry2.asInstanceOf[Geometry])) {
        geometry1.asInstanceOf[Geometry].intersection(geometry2.asInstanceOf[Geometry])
      } else {
        None
      }
/*
      if (intersection.isEmpty && tuple._1.getEnvelopeInternal.intersects(tuple._2.getEnvelopeInternal)) {
        //logError("******** No overlapping " + tuple._1.getEnvelopeInternal.toString + ", " + tuple._2.getEnvelopeInternal.toString)
      }*/

      intersection
    }).filter(geometry => geometry != None).map(geometry => geometry.asInstanceOf[Geometry]).toIterator

    val time3 = System.currentTimeMillis()

    logInfo("********* Intersecting %d pairs of geometries takes about : %d seconds"
      .format(pairList.size, (time3 - time2)/1000))

    result
  }
}

```

#### GeometryRDD.scala

```scala
package edu.gmu.stc.vector.rdd

import com.vividsolutions.jts.geom.{Geometry, GeometryFactory, MultiPolygon}
import com.vividsolutions.jts.index.SpatialIndex
import edu.gmu.stc.vector.rdd.index.IndexOperator
import edu.gmu.stc.vector.shapefile.meta.ShapeFileMeta
import edu.gmu.stc.vector.shapefile.reader.GeometryReaderUtil
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FSDataInputStream, Path}
import org.apache.spark.SparkContext
import org.apache.spark.internal.Logging
import org.apache.spark.rdd.RDD
import org.datasyslab.geospark.enums.{GridType, IndexType}
import org.datasyslab.geospark.spatialPartitioning.SpatialPartitioner
import org.geotools.geometry.jts.JTS
import org.geotools.referencing.CRS
import org.wololo.geojson.{Feature, FeatureCollection}
import org.wololo.jts2geojson.GeoJSONWriter

import scala.collection.JavaConverters._
import scala.reflect.ClassTag

/**
  * Created by Fei Hu on 1/26/18.
  */
class GeometryRDD extends Logging{
  private var geometryRDD: RDD[Geometry] = _
  private var indexedGeometryRDD: RDD[SpatialIndex] = _
  private var partitioner: SpatialPartitioner = _

  def initialize(shapeFileMetaRDD: ShapeFileMetaRDD, hasAttribute: Boolean = false): Unit = {
    this.geometryRDD = shapeFileMetaRDD.getShapeFileMetaRDD.mapPartitions(itor => {
      val shapeFileMetaList = itor.toList
      if (hasAttribute) {
        GeometryReaderUtil.readGeometriesWithAttributes(shapeFileMetaList.asJava).asScala.toIterator
      } else {
        GeometryReaderUtil.readGeometries(shapeFileMetaList.asJava).asScala.toIterator
      }
    })

    this.partitioner = shapeFileMetaRDD.getPartitioner
  }

  def transforCRS(sourceEpsgCRSCode: String,
                  sourceLongitudeFirst: Boolean = true,
                  targetEpsgCRSCode: String,
                  targetLongitudeFirst: Boolean = true): Unit = {
    val sourceCRS = CRS.decode(sourceEpsgCRSCode, sourceLongitudeFirst)
    val targetCRS = CRS.decode(targetEpsgCRSCode, targetLongitudeFirst)
    val transform = CRS.findMathTransform(sourceCRS, targetCRS)
    this.geometryRDD = this.geometryRDD.map(geometry => JTS.transform(geometry, transform))
  }

  def partition(partition: SpatialPartitioner): Unit = {
    this.partitioner = partition
    this.geometryRDD = this.geometryRDD.flatMap(geometry => partition.placeObject(geometry).asScala)
      .partitionBy(partition).map(_._2)
  }

  def intersect(shapeFileMetaRDD1: ShapeFileMetaRDD, shapeFileMetaRDD2: ShapeFileMetaRDD, partitionNum: Int): Unit = {
    var joinRDD: RDD[(ShapeFileMeta, ShapeFileMeta)] = shapeFileMetaRDD1.spatialJoin(shapeFileMetaRDD2, partitionNum)
      .sortBy({case (shapeFileMeta1, shapeFileMeta2) => shapeFileMeta1.getShp_offset})
      .repartition(partitionNum)

    joinRDD = joinRDD.cache()

    this.geometryRDD = joinRDD.mapPartitions(IndexOperator.spatialIntersect)
  }

  def getGeometryRDD: RDD[Geometry] = this.geometryRDD

  def indexPartition(indexType: IndexType) = {
    val indexBuilder = new IndexOperator(indexType.toString)
    this.indexedGeometryRDD = this.geometryRDD.mapPartitions(indexBuilder.buildIndex)
  }

  def intersect(other: GeometryRDD): GeometryRDD = {
    val geometryRDD = new GeometryRDD
    geometryRDD.geometryRDD = this.indexedGeometryRDD.zipPartitions(other.geometryRDD)(IndexOperator.geoSpatialIntersection)
    geometryRDD.geometryRDD = geometryRDD.geometryRDD.filter(geometry => !geometry.isEmpty)
    geometryRDD
  }

  def spatialJoin(other: GeometryRDD): RDD[(Geometry, Iterable[Geometry])] = {
    val pairedRDD= this.indexedGeometryRDD
      .zipPartitions(other.geometryRDD)(IndexOperator.geoSpatialJoin)
      .map{
        case (g1, g2) => {
          (g1.hashCode() + "" + g2.hashCode(), (g1, g2))
        }
      }.reduceByKey({
      case (tuple1, tuple2) => tuple1
    }).map(tuple => tuple._2)

    //TODO: is there any other efficient way 还有其他有效的办法吗
    pairedRDD.groupByKey()
  }

  def intersectV2(other: GeometryRDD, partitionNum: Int): GeometryRDD = {
    val geometryRDD = new GeometryRDD
    val pairedRDD= this.indexedGeometryRDD.zipPartitions(other.geometryRDD)(IndexOperator.geoSpatialJoin)

    geometryRDD.geometryRDD = pairedRDD
      .map({case (g1, g2) => {
        (g1.hashCode() + "_" + g2.hashCode(), (g1, g2))
      }})
      .reduceByKey((v1, v2) => v1)
        .map(tuple => tuple._2)
      .repartition(partitionNum)
      .map({case(g1, g2) => {
        g1.intersection(g2)
      }})
      .filter(g => !g.isEmpty)
      .map(g => (g.hashCode(), g))
      .reduceByKey({
        case (g1, g2) => g1
      }).map(_._2)

    geometryRDD
  }

  def cache(): Unit = {
    this.geometryRDD = this.geometryRDD.cache()
  }

  def uncache(blocking: Boolean = true): Unit = {
    this.geometryRDD.unpersist(blocking)
  }

  def saveAsGeoJSON(outputLocation: String): Unit = {
    this.geometryRDD.mapPartitions(iterator => {
      val geoJSONWriter = new GeoJSONWriter
      val featureList = iterator.map(geometry => {
        if (geometry.getUserData != null) {
          val userData = Map("UserData" -> geometry.getUserData)
          new Feature(geoJSONWriter.write(geometry), userData.asJava)
        } else {
          new Feature(geoJSONWriter.write(geometry), null)
        }
      }).toList

      val featureCollection = new FeatureCollection(featureList.toArray[Feature])
      List[String](featureCollection.toString).toIterator
    }).saveAsTextFile(outputLocation)
  }

  def getPartitioner: SpatialPartitioner = this.partitioner

  def saveAsShapefile(filepath: String, crs: String): Unit = {
    val geometries = this.geometryRDD.collect().toList.asJava
    GeometryReaderUtil.saveAsShapefile(filepath, geometries, crs)
  }
}

object GeometryRDD {
  def apply(sc: SparkContext, hadoopConfig: Configuration,
            tableName: String,
            gridTypeString: String, indexTypeString: String,
            partitionNum: Int,
            minX: Double,
            minY: Double,
            maxX: Double,
            maxY: Double,
            readAttributes: Boolean,
            isCache: Boolean): GeometryRDD = {

    val gridType = GridType.getGridType(gridTypeString)
    val indexType = IndexType.getIndexType(indexTypeString)

    val shapeFileMetaRDD = new ShapeFileMetaRDD(sc, hadoopConfig)
    shapeFileMetaRDD.initializeShapeFileMetaRDDAndPartitioner(sc, tableName,
      gridType, partitionNum, minX, minY, maxX, maxY)
    val geometryRDD = new GeometryRDD
    geometryRDD.initialize(shapeFileMetaRDD, readAttributes)
    geometryRDD.partition(shapeFileMetaRDD.getPartitioner)
    geometryRDD.indexPartition(indexType)

    if (isCache) {
      geometryRDD.cache()
    }

    geometryRDD
  }

  // use the partitioner from another geometryRDD to partition RDD
  def apply(sc: SparkContext, hadoopConfig: Configuration,
            tableName: String,
            partitionNum: Int,
            spatialPartitioner: SpatialPartitioner,
            minX: Double,
            minY: Double,
            maxX: Double,
            maxY: Double,
            readAttributes: Boolean,
            isCache: Boolean): GeometryRDD = {
    val shapeFileMetaRDD = new ShapeFileMetaRDD(sc, hadoopConfig)
    shapeFileMetaRDD.initializeShapeFileMetaRDDWithoutPartition(sc, tableName,
      partitionNum, minX, minY, maxX, maxY)
    val geometryRDD = new GeometryRDD
    geometryRDD.initialize(shapeFileMetaRDD, readAttributes)
    geometryRDD.partition(spatialPartitioner)
    if (isCache) {
      geometryRDD.cache()
    }

    geometryRDD
  }
}

```
 
#### ShapeFileMetaRDD.scala

extends Serializable with Logging

```scala
package edu.gmu.stc.vector.rdd

import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.index.SpatialIndex
import edu.gmu.stc.hibernate.{DAOImpl, HibernateUtil, PhysicalNameStrategyImpl}
import edu.gmu.stc.vector.operation.OperationUtil
import edu.gmu.stc.vector.parition.PartitionUtil
import edu.gmu.stc.vector.rdd.index.IndexOperator
import edu.gmu.stc.vector.shapefile.meta.ShapeFileMeta
import edu.gmu.stc.vector.shapefile.meta.index.ShapeFileMetaIndexInputFormat
import org.apache.commons.io.FilenameUtils
import org.apache.spark.{Partition, SerializableWritable, SparkContext, TaskContext}
import org.apache.spark.rdd.RDD
import org.apache.hadoop.conf.Configuration
import org.apache.spark.rdd.NewHadoopRDD
import org.apache.hadoop.mapreduce.InputFormat
import org.apache.log4j.Logger
import org.apache.spark.internal.Logging
import org.datasyslab.geospark.enums.{GridType, IndexType}
import org.datasyslab.geospark.formatMapper.shapefileParser.shapes.ShapeKey
import org.datasyslab.geospark.spatialPartitioning.SpatialPartitioner
import org.hibernate.Session

import scala.collection.JavaConverters._



/**
  * Created by Fei Hu on 1/24/18.
  */

class ShapeFileMetaRDD (sc: SparkContext, @transient conf: Configuration) extends Serializable with Logging {
  private var shapeFileMetaRDD: RDD[ShapeFileMeta] = _

  private var indexedShapeFileMetaRDD: RDD[SpatialIndex] = _

  private var partitioner: SpatialPartitioner = _

  private val confBroadcast = sc.broadcast(new SerializableWritable(conf))

  def getConf: Configuration = {
    val conf: Configuration = confBroadcast.value.value
    conf
  }

  def initializeShapeFileMetaRDD(sc: SparkContext, conf: Configuration): Unit = {
    shapeFileMetaRDD = new NewHadoopRDD[ShapeKey, ShapeFileMeta](sc,
      classOf[ShapeFileMetaIndexInputFormat].asInstanceOf[Class[F] forSome {type F <: InputFormat[ShapeKey, ShapeFileMeta]}],
      classOf[org.datasyslab.geospark.formatMapper.shapefileParser.shapes.ShapeKey],
      classOf[edu.gmu.stc.vector.shapefile.meta.ShapeFileMeta],
      conf).map( element => element._2)
  }

  def initializeShapeFileMetaRDD(sc: SparkContext,
                                 tableName: String,
                                 gridType: GridType,
                                 partitionNum: Int, minX: Double, minY: Double,
                                 maxX: Double, maxY: Double): Unit = {
    val physicalNameStrategy = new PhysicalNameStrategyImpl(tableName)
    val hibernateUtil = new HibernateUtil
    hibernateUtil
      .createSessionFactoryWithPhysicalNamingStrategy(sc.hadoopConfiguration, physicalNameStrategy,
        classOf[ShapeFileMeta])
    val session = hibernateUtil.getSession
    val dao = new DAOImpl[ShapeFileMeta]()
    dao.setSession(session)
    val hql = ShapeFileMeta.getSQLForOverlappedRows(tableName, minX, minY, maxX, maxY)

    val shapeFileMetaList = dao.findByQuery(hql, classOf[ShapeFileMeta]).asScala
    val envelopes = shapeFileMetaList.map(shapeFileMeta => shapeFileMeta.getEnvelopeInternal)

    logInfo("Number of queried shapefile metas is : " + envelopes.size)

    session.close()
    hibernateUtil.closeSessionFactory()

    //initialize the partitioner
    this.partitioner = PartitionUtil.spatialPartitioning(gridType, partitionNum, envelopes.asJava)

    shapeFileMetaRDD = sc.parallelize(shapeFileMetaList, partitionNum)
      .flatMap(shapefileMeta => partitioner.placeObject(shapefileMeta).asScala)
      .partitionBy(partitioner)
      .map(tuple => tuple._2)
  }

  def initializeShapeFileMetaRDDAndPartitioner(sc: SparkContext,
                                               tableName: String,
                                               gridType: GridType,
                                               partitionNum: Int, minX: Double, minY: Double,
                                               maxX: Double, maxY: Double): Unit = {
    val physicalNameStrategy = new PhysicalNameStrategyImpl(tableName)
    val hibernateUtil = new HibernateUtil
    hibernateUtil
      .createSessionFactoryWithPhysicalNamingStrategy(sc.hadoopConfiguration, physicalNameStrategy,
        classOf[ShapeFileMeta])
    val session = hibernateUtil.getSession
    val dao = new DAOImpl[ShapeFileMeta]()
    dao.setSession(session)
    val hql = ShapeFileMeta.getSQLForOverlappedRows(tableName, minX, minY, maxX, maxY)

    val shapeFileMetaList = dao.findByQuery(hql, classOf[ShapeFileMeta]).asScala
    logInfo("**** Number of shapeFile meta is %d".format(shapeFileMetaList.size))
    val envelopes = shapeFileMetaList.map(shapeFileMeta => shapeFileMeta.getEnvelopeInternal)

    logInfo("Number of queried shapefile metas is : " + envelopes.size)

    session.close()
    hibernateUtil.closeSessionFactory()

    //initialize the partitioner
    this.partitioner = PartitionUtil.spatialPartitioning(gridType, partitionNum, envelopes.asJava)

    session.close()
    hibernateUtil.closeSessionFactory()

    this.shapeFileMetaRDD = sc.parallelize(shapeFileMetaList, partitionNum)
  }

  def initializeShapeFileMetaRDDWithoutPartition(sc: SparkContext,
                                                 tableName: String,
                                                 partitionNum: Int, minX: Double, minY: Double,
                                                 maxX: Double, maxY: Double): Unit = {
    val physicalNameStrategy = new PhysicalNameStrategyImpl(tableName)
    val hibernateUtil = new HibernateUtil
    hibernateUtil
      .createSessionFactoryWithPhysicalNamingStrategy(sc.hadoopConfiguration, physicalNameStrategy,
        classOf[ShapeFileMeta])
    val session = hibernateUtil.getSession
    val dao = new DAOImpl[ShapeFileMeta]()
    dao.setSession(session)
    val hql = ShapeFileMeta.getSQLForOverlappedRows(tableName, minX, minY, maxX, maxY)

    val shapeFileMetaList = dao.findByQuery(hql, classOf[ShapeFileMeta]).asScala

    logInfo("Number of queried shapefile metas is : " + shapeFileMetaList.size)

    session.close()
    hibernateUtil.closeSessionFactory()

    session.close()
    hibernateUtil.closeSessionFactory()

    this.shapeFileMetaRDD = sc.parallelize(shapeFileMetaList, partitionNum)
  }

  def initializeShapeFileMetaRDD(sc: SparkContext, partitioner: SpatialPartitioner,
                                 tableName: String, partitionNum: Int,
                                 minX: Double, minY: Double, maxX: Double, maxY: Double) = {
    val physicalNameStrategy = new PhysicalNameStrategyImpl(tableName)
    val hibernateUtil = new HibernateUtil
    hibernateUtil.createSessionFactoryWithPhysicalNamingStrategy(sc.hadoopConfiguration, physicalNameStrategy,
        classOf[ShapeFileMeta])
    val session = hibernateUtil.getSession
    val dao = new DAOImpl[ShapeFileMeta]()
    dao.setSession(session)
    val hql = ShapeFileMeta.getSQLForOverlappedRows(tableName, minX, minY, maxX, maxY)

    val shapeFileMetaList = dao.findByQuery(hql, classOf[ShapeFileMeta]).asScala.toList
    session.close()
    hibernateUtil.closeSessionFactory()

    this.partitioner = partitioner

    shapeFileMetaRDD = sc.parallelize(shapeFileMetaList, partitionNum)
      .flatMap(shapefileMeta => this.partitioner.placeObject(shapefileMeta).asScala)
      .partitionBy(this.partitioner)
      .map(tuple => tuple._2)
  }

  def saveShapeFileMetaToDB(conf: Configuration, tableName: String): Unit = {
    shapeFileMetaRDD.foreachPartition(itor => {
      val physicalNameStrategy = new PhysicalNameStrategyImpl(tableName)
      val hibernateUtil = new HibernateUtil
      hibernateUtil.createSessionFactoryWithPhysicalNamingStrategy(conf, physicalNameStrategy,
                                                        classOf[ShapeFileMeta])
      val session = hibernateUtil.getSession
      val dao = new DAOImpl[ShapeFileMeta]()
      dao.setSession(session)
      dao.insertDynamicTableObjectList(tableName, itor.asJava)
      session.close()
      hibernateUtil.closeSessionFactory()
    })
  }

  def saveShapeFileMetaToDB(): Unit = {
    shapeFileMetaRDD.foreachPartition(itor => {
      val shapeFileMetaList = itor.toList
      //TODO: make sure the table name is right 确保表名正确
      //val tableName = shapeFileMetaList.head.getFilePath.split("/").last.toLowerCase
      val tableName = FilenameUtils.getBaseName(shapeFileMetaList.head.getFilePath)

      logInfo("******* Save into the table [%s]".format(tableName))

      val physicalNameStrategy = new PhysicalNameStrategyImpl(tableName)
      val hibernateUtil = new HibernateUtil
      hibernateUtil.createSessionFactoryWithPhysicalNamingStrategy(getConf, physicalNameStrategy, classOf[ShapeFileMeta])

      val session = hibernateUtil.getSession
      val dao = new DAOImpl[ShapeFileMeta]()
      dao.setSession(session)
      dao.insertDynamicTableObjectList(tableName, shapeFileMetaList.asJava.iterator())
      hibernateUtil.closeSession()
      hibernateUtil.closeSessionFactory()
    })

  }

  def partition(partitioner: SpatialPartitioner): Unit = {
    this.shapeFileMetaRDD = this.shapeFileMetaRDD
      .flatMap(shapefileMeta => this.partitioner.placeObject(shapefileMeta).asScala)
      .partitionBy(this.partitioner).map( tuple => tuple._2).distinct()
  }

  def indexPartition(indexType: IndexType) = {
    val indexBuilder = new IndexOperator(indexType.toString)
    this.indexedShapeFileMetaRDD = this.shapeFileMetaRDD.mapPartitions(indexBuilder.buildIndex)
  }

  def spatialJoin(shapeFileMetaRDD2: ShapeFileMetaRDD, partitionNum: Int): RDD[(ShapeFileMeta, ShapeFileMeta)] = {
    this.indexedShapeFileMetaRDD
      .zipPartitions(shapeFileMetaRDD2.getShapeFileMetaRDD)(IndexOperator.spatialJoin)
      .map(tuple => (OperationUtil.getUniqID(tuple._1.getIndex, tuple._2.getIndex), tuple))
      .reduceByKey((tuple1:(ShapeFileMeta, ShapeFileMeta), tuple2: (ShapeFileMeta, ShapeFileMeta)) => tuple1)
      //.sortByKey(ascending = true, partitionNum)
      .map(tuple => tuple._2)
  }

  def spatialJoinV2(shapeFileMetaRDD2: ShapeFileMetaRDD, partitionNum: Int): RDD[(Long, Set[Long])] = {
    this.indexedShapeFileMetaRDD
      .zipPartitions(shapeFileMetaRDD2.getShapeFileMetaRDD)(IndexOperator.spatialJoinV2)
      .distinct()
      .groupByKey()
      .map(tuple => (tuple._1, tuple._2.toSet[Long]))
  }

  def spatialIntersect(shapeFileMetaRDD2: ShapeFileMetaRDD): RDD[Geometry] = {
    this.indexedShapeFileMetaRDD
      .zipPartitions(shapeFileMetaRDD2.getShapeFileMetaRDD, preservesPartitioning = true)(IndexOperator.spatialIntersect)
  }

  def getShapeFileMetaRDD: RDD[ShapeFileMeta] = this.shapeFileMetaRDD

  def getPartitioner: SpatialPartitioner = this.partitioner

  def getIndexedShapeFileMetaRDD: RDD[SpatialIndex] = this.indexedShapeFileMetaRDD
}
```

### SparkCity/application/src/main/scala/edu/gmu/stc/vector/serde/

#### HadoopConfigurationSerde.scala

```scala
package edu.gmu.stc.vector.serde

import com.esotericsoftware.kryo.io.{Input, Output}
import com.esotericsoftware.kryo.{Kryo, Serializer}
import edu.gmu.stc.config.ConfigParameter
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io.Writable
import org.apache.spark.internal.Logging

/**
  * Created by Fei Hu on 1/29/18.
  */
class HadoopConfigurationSerde extends Serializer with Logging{
  override def read(kryo: Kryo, input: Input, aClass: Class[Nothing]): Nothing = {
    val configuration = new Configuration()
    configuration.set(ConfigParameter.HIBERNATE_DRIEVER, input.readString())
    configuration.set(ConfigParameter.HIBERNATE_URL, input.readString())
    configuration.set(ConfigParameter.HIBERNATE_USER, input.readString())
    configuration.set(ConfigParameter.HIBERNATE_PASS, input.readString())
    configuration.set(ConfigParameter.HIBERNATE_DIALECT, input.readString())
    configuration.set(ConfigParameter.HIBERNATE_HBM2DDL_AUTO, input.readString())

    configuration.set(ConfigParameter.INPUT_DIR_PATH, input.readString())

    configuration.asInstanceOf[Nothing]
  }

  override def write(kryo: Kryo, output: Output, t: Nothing): Unit = {
    if (t.isInstanceOf[Configuration]) {
      val config = t.asInstanceOf[Configuration]
      output.writeString(config.get(ConfigParameter.HIBERNATE_DRIEVER))
      output.writeString(config.get(ConfigParameter.HIBERNATE_URL))
      output.writeString(config.get(ConfigParameter.HIBERNATE_USER))
      output.writeString(config.get(ConfigParameter.HIBERNATE_PASS))
      output.writeString(config.get(ConfigParameter.HIBERNATE_DIALECT))
      output.writeString(config.get(ConfigParameter.HIBERNATE_HBM2DDL_AUTO))

      output.writeString(config.get(ConfigParameter.INPUT_DIR_PATH))
    } else {
      logError("Does not support the serialization for this class " + t.getClass.toString)
    }
  }
}

```
 
#### VectorKryoRegistrator.scala

extends KryoRegistrator

```scala
package edu.gmu.stc.vector.serde

import com.esotericsoftware.kryo.Kryo
import com.vividsolutions.jts.geom._
import com.vividsolutions.jts.index.SpatialIndex
import com.vividsolutions.jts.index.quadtree.Quadtree
import com.vividsolutions.jts.index.strtree.STRtree
import edu.gmu.stc.vector.shapefile.meta.ShapeFileMeta
import org.apache.spark.serializer.KryoRegistrator
import org.datasyslab.geospark.geometryObjects.{Circle, GeometrySerde, SpatialIndexSerde}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io.Text

/**
  * Created by Fei Hu on 1/26/18.
  */
class VectorKryoRegistrator extends KryoRegistrator{
  override def registerClasses(kryo: Kryo): Unit = {
    val serializer = new GeometrySerde
    val indexSerializer = new SpatialIndexSerde(serializer)

    kryo.register(classOf[ShapeFileMeta])

    kryo.register(classOf[Point], serializer)
    kryo.register(classOf[LineString], serializer)
    kryo.register(classOf[Polygon], serializer)
    kryo.register(classOf[MultiPoint], serializer)
    kryo.register(classOf[MultiLineString], serializer)
    kryo.register(classOf[MultiPolygon], serializer)
    kryo.register(classOf[GeometryCollection], serializer)
    kryo.register(classOf[Circle], serializer)
    kryo.register(classOf[Envelope], serializer)
    // TODO: 用默认空间索引序列化器替换默认序列化器
    kryo.register(classOf[Quadtree], indexSerializer)
    kryo.register(classOf[STRtree], indexSerializer)
    kryo.register(classOf[SpatialIndex], indexSerializer)
    kryo.register(classOf[Tuple2[Polygon, Polygon]])

    //kryo.register(classOf[Configuration], new HadoopConfigurationSerde)
  }
}

```

### SparkCity/application/src/main/scala/edu/gmu/stc/vector/sparkshell/

#### Application.scala

```scala
package edu.gmu.stc.vector.sparkshell

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.internal.Logging
import org.apache.spark.sql.{SQLContext, SparkSession}

/**
  * Created by Fei Hu on 2/22/18.
  */
object Application extends Logging{

  def spatialOperation(args: Array[String], sc: SparkContext, sparkSession: SparkSession): String = {
    if (args.length < 1) {
      logError("Please input the arguments")
    }
    val operationType = args(0)

    operationType match {
      case "GeoSpark_Overlap" => {
        GeoSpark_OverlapTest.overlap(args.slice(1, args.length), sc, sparkSession)
      }

      case "STC_OverlapTest_V1" => {
        STC_OverlapTest_V1.overlap(args.slice(1, args.length), sc, sparkSession)
      }

      case "STC_OverlapTest_V2" => {
        STC_OverlapTest_v2.overlap(args.slice(1, args.length), sc, sparkSession)
      }

      case _ => {
        logError("Please input the right arguments for operations")
        "Please input the right arguments for operations"
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("Application")

    if (System.getProperty("os.name").equals("Mac OS X")) {
      sparkConf.setMaster("local[6]")
    }

    val sc = new SparkContext(sparkConf)
    val sqlContext = new SQLContext(sc)
    val sparkSession: SparkSession = sqlContext.sparkSession

    val args_new = Array("STC_OverlapTest_V1", "/Users/feihu/Documents/GitHub/GeoSpark/config/conf_dc.xml", "240", "KDBTREE", "RTREE", "/Users/feihu/Documents/GitHub/GeoSpark/shp_dc_test.shp")
    val args_1 = Array("GeoSpark_Overlap", "/Users/feihu/Documents/GitHub/GeoSpark/application/src/main/resources/data/Washington_DC", "Impervious_Surface_2015", "Soil_Type_by_Slope", "240", "KDBTREE", "RTREE")

    val output = Application.spatialOperation(args_1, sc, sparkSession)
    println(output)
  }

}

```
 
#### GeoSpark_OverlapTest.scala

```scala
package edu.gmu.stc.vector.sparkshell

import edu.gmu.stc.vector.operation.Overlap.intersect
import edu.gmu.stc.vector.operation.OperationUtil.show_timing
import edu.gmu.stc.vector.sparkshell.OverlapShellExample.sc
import edu.gmu.stc.vector.sparkshell.STC_OverlapTest_V1.logError
import org.apache.spark.internal.Logging
import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}
import org.datasyslab.geosparksql.utils.GeoSparkSQLRegistrator

/**
  * Created by Fei Hu.
  */
object GeoSpark_OverlapTest extends Logging{

  def overlap(args: Array[String], sc: SparkContext, spark: SparkSession): String = {
    if (args.length != 6) {
      logError("Please input 6 arguments: " +
        "\n \t 1) input dir path: this directory path for the shapefiles" +
        "\n \t 2) shapefile layer 1 folder name: the name of the first shapefile" +
        "\n \t 3) shapefile layer 2 folder name: the name of the second shapefile" +
        "\n \t 4) number of partitions: the int number of partitions" +
        "\n \t 5) partition type: it can be EQUALGRID, HILBERT, RTREE, VORONOI, QUADTREE, KDBTREE") +
        "\n \t 6) indexType: the index type for each partition, e.g. QUADTREE, RTREE"

      return "Please input the right arguments"
    }

    GeoSparkSQLRegistrator.registerAll(spark.sqlContext)

    val resourceFolder = args(0) //"/user/root/data0119/"
    val shpFile1: String = resourceFolder + "/" + args(1) //"Impervious_Surface_2015_DC"
    val shpFile2: String = resourceFolder + "/" + args(2) //"Soil_Type_by_Slope_DC"

    val numPartition: Int = args(3).toInt
    val outputFile: String = resourceFolder + "%s_%s_overlap_%s_".format(args(1), args(2), args(4)) + System.currentTimeMillis() + ".geojson"

    intersect(spark, args(4), args(5), shpFile1, shpFile2, numPartition, outputFile)
    outputFile
  }

  def main(args: Array[String]): Unit = {
    if (args.length != 6) {
      logError("Please input 6 arguments: " +
        "\n \t 1) input dir path: this directory path for the shapefiles" +
        "\n \t 2) shapefile layer 1 folder name: the name of the first shapefile" +
        "\n \t 3) shapefile layer 2 folder name: the name of the second shapefile" +
        "\n \t 4) number of partitions: the int number of partitions" +
        "\n \t 5) partition type: it can be EQUALGRID, HILBERT, RTREE, VORONOI, QUADTREE, KDBTREE") +
        "\n \t 6) indexType: the index type for each partition, e.g. QUADTREE, RTREE"

      return
    }

    val sparkConf = new SparkConf().setAppName("GeoSpark_OverlapTest" + "_%s_%s_%s".format(args(3), args(4), args(5)))

    if (System.getProperty("os.name").equals("Mac OS X")) {
      sparkConf.setMaster("local[6]")
    }

    val sc = new SparkContext(sparkConf)
    val sqlContext = new SQLContext(sc)
    val sparkSession: SparkSession = sqlContext.sparkSession

    GeoSparkSQLRegistrator.registerAll(sparkSession.sqlContext)

    val resourceFolder = args(0) //"/user/root/data0119/"
    val shpFile1: String = resourceFolder + "/" + args(1) //"Impervious_Surface_2015_DC"
    val shpFile2: String = resourceFolder + "/" + args(2) //"Soil_Type_by_Slope_DC"

    val numPartition: Int = args(3).toInt
    val outputFile: String = resourceFolder + "%s_%s_overlap_%s".format(args(1), args(2), args(4)) + ".geojson"


    val runtime: Long = show_timing(intersect(sparkSession, args(4), args(5), shpFile1, shpFile2, numPartition, outputFile))
    println(s"Runtime is : $runtime seconds")

    sparkSession.stop()
  }
}

```
 
#### OverlapShellExample.scala

```scala
package edu.gmu.stc.vector.sparkshell

import edu.gmu.stc.vector.operation.Overlap.intersect
import edu.gmu.stc.vector.operation.OperationUtil.show_timing
import org.apache.spark.SparkContext
import org.apache.spark.sql.{SQLContext, SparkSession}
import org.datasyslab.geosparksql.utils.GeoSparkSQLRegistrator

/**
  * Created by Fei Hu on 1/23/18.
  */
object OverlapShellExample {

  val sc = new SparkContext()
  val sqlContext = new SQLContext(sc)
  val sparkSession: SparkSession = sqlContext.sparkSession

  GeoSparkSQLRegistrator.registerAll(sparkSession.sqlContext)

  val resourceFolder = "/user/root/data0119/"
  val shpFile1: String = resourceFolder + "Impervious_Surface_2015_DC"
  val shpFile2: String = resourceFolder + "Soil_Type_by_Slope_DC"

  val outputFile: String = resourceFolder + "dc_overlayMap_intersect_" + System.currentTimeMillis() + ".geojson"
  val numPartition = 24
  val runtime: Long = show_timing(intersect(sparkSession, "KDBTREE", "QUADTREE", shpFile1, shpFile2, numPartition, outputFile))
}

```

#### STC_BuildIndexTest.scala

```scala
package edu.gmu.stc.vector.sparkshell

import edu.gmu.stc.config.ConfigParameter
import edu.gmu.stc.hibernate.HibernateUtil
import edu.gmu.stc.vector.examples.ShapeFileMetaTest.logInfo
import edu.gmu.stc.vector.rdd.{GeometryRDD, ShapeFileMetaRDD}
import edu.gmu.stc.vector.serde.VectorKryoRegistrator
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.spark.internal.Logging
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
import org.datasyslab.geospark.enums.IndexType
import org.apache.hadoop.conf.Configuration


/**
  * Created by Fei Hu on 1/29/18.
  */
object STC_BuildIndexTest extends Logging{

  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      logError("Please input two arguments: " +
        "\n \t 1) appName: the name of the application " +
        "\n \t 2) configFilePath: this file path for the configuration file path")
      return
    }

    val sparkConf = new SparkConf().setAppName(args(0))//.setMaster("local[6]")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.kryo.registrator", classOf[VectorKryoRegistrator].getName)

    if (System.getProperty("os.name").equals("Mac OS X")) {
      sparkConf.setMaster("local[6]")
    }

    val sc = new SparkContext(sparkConf)

    val configFilePath = args(1)   //"/Users/feihu/Documents/GitHub/GeoSpark/config/conf.xml"
    val hConf = new Configuration()
    hConf.addResource(new Path(configFilePath))
    sc.hadoopConfiguration.addResource(hConf)

    val shapeFileMetaRDD = new ShapeFileMetaRDD(sc, hConf)
    shapeFileMetaRDD.initializeShapeFileMetaRDD(sc, hConf)
    shapeFileMetaRDD.saveShapeFileMetaToDB()
  }
}

```
 
#### STC_OverlapTest_V1.scala

```scala
package edu.gmu.stc.vector.sparkshell

import edu.gmu.stc.config.ConfigParameter
import edu.gmu.stc.vector.examples.ShapeFileMetaTest._
import edu.gmu.stc.vector.rdd.{GeometryRDD, ShapeFileMetaRDD}
import edu.gmu.stc.vector.serde.VectorKryoRegistrator
import edu.gmu.stc.vector.sparkshell.STC_BuildIndexTest.logError
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.internal.Logging
import org.apache.spark.sql.SparkSession
import org.datasyslab.geospark.enums.{GridType, IndexType}

/**
  * Created by Fei Hu on 1/29/18.
  */
object STC_OverlapTest_V1 extends Logging{

  def overlap(args: Array[String], sc: SparkContext, spark: SparkSession): String = {
    if (args.length != 6) {
      logError("Please input four arguments: " +
        "\n \t 1)configFilePath: this file path for the configuration file path" +
        "\n \t 2) numPartition: the number of partitions" +
        "\n \t 3) gridType: the type of the partition, e.g. EQUALGRID, HILBERT, RTREE, VORONOI, QUADTREE, KDBTREE" +
        "\n \t 4) indexType: the index type for each partition, e.g. QUADTREE, RTREE" +
        "\n \t 5) output file path: the file path for geojson output" +
        "\n \t 6) crs: coordinate reference system")

      return "Please input the right arguments"
    }

    sc.getConf
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.kryo.registrator", classOf[VectorKryoRegistrator].getName)

    val configFilePath = args(0)   //"/Users/feihu/Documents/GitHub/GeoSpark/config/conf.xml"
    val hConf = new Configuration()
    hConf.addResource(new Path(configFilePath))
    sc.hadoopConfiguration.addResource(hConf)

    val tableNames = hConf.get(ConfigParameter.SHAPEFILE_INDEX_TABLES).split(",").map(s => s.toLowerCase().trim)

    val partitionNum = args(1).toInt  //24
    val minX = -180
    val minY = -180
    val maxX = 180
    val maxY = 180

    val gridType = GridType.getGridType(args(2)) //EQUALGRID, HILBERT, RTREE, VORONOI, QUADTREE, KDBTREE
    val indexType = IndexType.getIndexType(args(3))  //RTREE, QUADTREE

    val shapeFileMetaRDD1 = new ShapeFileMetaRDD(sc, hConf)
    val table1 = tableNames(0)
    shapeFileMetaRDD1.initializeShapeFileMetaRDD(sc, table1, gridType, partitionNum, minX, minY, maxX, maxY)
    shapeFileMetaRDD1.indexPartition(indexType)
    shapeFileMetaRDD1.getIndexedShapeFileMetaRDD.cache()

    val shapeFileMetaRDD2 = new ShapeFileMetaRDD(sc, hConf)
    val table2 = tableNames(1)
    shapeFileMetaRDD2.initializeShapeFileMetaRDD(sc, shapeFileMetaRDD1.getPartitioner, table2,
      partitionNum, minX, minY, maxX, maxY)

    shapeFileMetaRDD2.getShapeFileMetaRDD.cache()

    val geometryRDD = new GeometryRDD
    geometryRDD.intersect(shapeFileMetaRDD1, shapeFileMetaRDD2, partitionNum)
    geometryRDD.cache()

    val filePath = args(4)
    val crs = args(5)
    if (filePath.endsWith("shp")) {
      geometryRDD.saveAsShapefile(filePath, crs)
    } else {
      geometryRDD.saveAsGeoJSON(filePath)
    }

    filePath
  }

  def main(args: Array[String]): Unit = {

    if (args.length != 5) {
      logError("Please input four arguments: " +
        "\n \t 1)configFilePath: this file path for the configuration file path" +
        "\n \t 2) numPartition: the number of partitions" +
        "\n \t 3) gridType: the type of the partition, e.g. EQUALGRID, HILBERT, RTREE, VORONOI, QUADTREE, KDBTREE" +
        "\n \t 4) indexType: the index type for each partition, e.g. QUADTREE, RTREE" +
        "\n \t 5) output file path: the file path for geojson output" +
        "\n \t 6) crs: coordinate reference system")

      return
    }

    val sparkConf = new SparkConf().setAppName("%s_%s_%s_%s".format("STC_OverlapTest_v1", args(1), args(2), args(3)))
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.kryo.registrator", classOf[VectorKryoRegistrator].getName)

    if (System.getProperty("os.name").equals("Mac OS X")) {
      sparkConf.setMaster("local[6]")
    }

    val sc = new SparkContext(sparkConf)

    val configFilePath = args(0)   //"/Users/feihu/Documents/GitHub/GeoSpark/config/conf.xml"
    val hConf = new Configuration()
    hConf.addResource(new Path(configFilePath))
    sc.hadoopConfiguration.addResource(hConf)

    val tableNames = hConf.get(ConfigParameter.SHAPEFILE_INDEX_TABLES).split(",").map(s => s.toLowerCase().trim)

    val partitionNum = args(1).toInt  //24
    val minX = -180
    val minY = -180
    val maxX = 180
    val maxY = 180

    val gridType = GridType.getGridType(args(2)) //EQUALGRID, HILBERT, RTREE, VORONOI, QUADTREE, KDBTREE
    val indexType = IndexType.getIndexType(args(3))  //RTREE, QUADTREE

    val shapeFileMetaRDD1 = new ShapeFileMetaRDD(sc, hConf)
    val table1 = tableNames(0)
    shapeFileMetaRDD1.initializeShapeFileMetaRDD(sc, table1, gridType, partitionNum, minX, minY, maxX, maxY)
    shapeFileMetaRDD1.indexPartition(indexType)
    shapeFileMetaRDD1.getIndexedShapeFileMetaRDD.cache()
    println("******shapeFileMetaRDD1****************", shapeFileMetaRDD1.getShapeFileMetaRDD.count())

    val shapeFileMetaRDD2 = new ShapeFileMetaRDD(sc, hConf)
    val table2 = tableNames(1)
    shapeFileMetaRDD2.initializeShapeFileMetaRDD(sc, shapeFileMetaRDD1.getPartitioner, table2,
      partitionNum, minX, minY, maxX, maxY)

    shapeFileMetaRDD2.getShapeFileMetaRDD.cache()

    println("******shapeFileMetaRDD2****************", shapeFileMetaRDD2.getShapeFileMetaRDD.count())

    println(shapeFileMetaRDD1.getShapeFileMetaRDD.partitions.size, "**********************",
      shapeFileMetaRDD2.getShapeFileMetaRDD.partitions.size)

    val geometryRDD = new GeometryRDD
    geometryRDD.intersect(shapeFileMetaRDD1, shapeFileMetaRDD2, partitionNum)
    geometryRDD.cache()

    val filePath = args(4)
    if (filePath.endsWith("shp")) {
      geometryRDD.saveAsShapefile(filePath, "epsg:4326")
    } else {
      geometryRDD.saveAsGeoJSON(filePath)
    }
    println("******** Number of intersected polygons: %d".format(geometryRDD.getGeometryRDD.count()))
  }

}

```
 
#### STC_OverlapTest_v2.scala

```scala
package edu.gmu.stc.vector.sparkshell

import edu.gmu.stc.config.ConfigParameter
import edu.gmu.stc.vector.operation.OperationUtil
import edu.gmu.stc.vector.rdd.{GeometryRDD, ShapeFileMetaRDD}
import edu.gmu.stc.vector.serde.VectorKryoRegistrator
import edu.gmu.stc.vector.sparkshell.STC_OverlapTest_V1.{logError, logInfo}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.spark.internal.Logging
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}
import org.datasyslab.geospark.enums.{GridType, IndexType}

/**
  * Created by Fei Hu on 1/31/18.
  */
object STC_OverlapTest_v2 extends Logging{

  def overlap(args: Array[String], sc: SparkContext, spark: SparkSession): String = {
    if (args.length != 5) {
      logError("You input "+ args.length + "arguments: " + args.mkString(" ") + ", but it requires 5 arguments: " +
        "\n \t 1)configFilePath: this file path for the configuration file path" +
        "\n \t 2) numPartition: the number of partitions" +
        "\n \t 3) gridType: the type of the partition, e.g. EQUALGRID, HILBERT, RTREE, VORONOI, QUADTREE, KDBTREE" +
        "\n \t 4) indexType: the index type for each partition, e.g. QUADTREE, RTREE" +
        "\n \t 5) output file path: the file path for geojson output")

      return ""
    }

    sc.getConf
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.kryo.registrator", classOf[VectorKryoRegistrator].getName)

    val configFilePath = args(0)   //"/Users/feihu/Documents/GitHub/GeoSpark/config/conf.xml"
    val hConf = new Configuration()
    hConf.addResource(new Path(configFilePath))
    sc.hadoopConfiguration.addResource(hConf)

    val tableNames = hConf.get(ConfigParameter.SHAPEFILE_INDEX_TABLES).split(",").map(s => s.toLowerCase().trim)

    val partitionNum = args(1).toInt  //24
    val minX = -180
    val minY = -180
    val maxX = 180
    val maxY = 180

    val gridType = GridType.getGridType(args(2)) //EQUALGRID, HILBERT, RTREE, VORONOI, QUADTREE, KDBTREE
    val indexType = IndexType.getIndexType(args(3))  //RTREE, QUADTREE

    val shapeFileMetaRDD1 = new ShapeFileMetaRDD(sc, hConf)
    val table1 = tableNames(0)
    shapeFileMetaRDD1.initializeShapeFileMetaRDDAndPartitioner(sc, table1, gridType, partitionNum, minX, minY, maxX, maxY)
    val geometryRDD1 = new GeometryRDD
    geometryRDD1.initialize(shapeFileMetaRDD1, hasAttribute = false)
    geometryRDD1.partition(shapeFileMetaRDD1.getPartitioner)
    geometryRDD1.indexPartition(indexType)
    geometryRDD1.cache()

    val partitionNum1 = geometryRDD1.getGeometryRDD.mapPartitionsWithIndex({
      case (index, itor) => {
        List((index, itor.size)).toIterator
      }
    }).collect()

    partitionNum1.foreach(println)

    val shapeFileMetaRDD2 = new ShapeFileMetaRDD(sc, hConf)
    val table2 = tableNames(1)
    shapeFileMetaRDD2.initializeShapeFileMetaRDDWithoutPartition(sc, table2,
      partitionNum, minX, minY, maxX, maxY)

    val geometryRDD2 = new GeometryRDD
    geometryRDD2.initialize(shapeFileMetaRDD2, hasAttribute = false)
    geometryRDD2.partition(shapeFileMetaRDD1.getPartitioner)
    geometryRDD2.cache()

    val partitionNum2 = geometryRDD2.getGeometryRDD.mapPartitionsWithIndex({
      case (index, itor) => {
        List((index, itor.size)).toIterator
      }
    }).collect()

    val geometryRDD = geometryRDD1.intersect(geometryRDD2)
    geometryRDD.cache()

    val outputFilePath = args(4)
    if (outputFilePath.endsWith("shp")) {
      geometryRDD.saveAsShapefile(outputFilePath, "epsg:4326")
    } else {
      geometryRDD.saveAsGeoJSON(outputFilePath)
    }

    outputFilePath
  }

  def main(args: Array[String]): Unit = {

    if (args.length != 5) {
      logError("You input "+ args.length + "arguments: " + args.mkString(" ") + ", but it requires 5 arguments: " +
        "\n \t 1)configFilePath: this file path for the configuration file path" +
        "\n \t 2) numPartition: the number of partitions" +
        "\n \t 3) gridType: the type of the partition, e.g. EQUALGRID, HILBERT, RTREE, VORONOI, QUADTREE, KDBTREE" +
        "\n \t 4) indexType: the index type for each partition, e.g. QUADTREE, RTREE" +
        "\n \t 5) output file path: the file path for geojson output")

      return
    }

    val t = System.currentTimeMillis()

    val sparkConf = new SparkConf().setAppName("%s_%s_%s_%s".format("STC_OverlapTest_v2", args(1), args(2), args(3)))
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.kryo.registrator", classOf[VectorKryoRegistrator].getName)
      .set("spark.kryoserializer.buffer.max", "1550m")

    if (System.getProperty("os.name").equals("Mac OS X")) {
      sparkConf.setMaster("local[6]")
    }

    val sc = new SparkContext(sparkConf)

    val configFilePath = args(0)   //"/Users/feihu/Documents/GitHub/GeoSpark/config/conf.xml"
    val hConf = new Configuration()
    hConf.addResource(new Path(configFilePath))
    sc.hadoopConfiguration.addResource(hConf)

    val tableNames = hConf.get(ConfigParameter.SHAPEFILE_INDEX_TABLES).split(",").map(s => s.toLowerCase().trim)

    val partitionNum = args(1).toInt  //24
    val minX = -180
    val minY = -180
    val maxX = 180
    val maxY = 180

    val gridType = GridType.getGridType(args(2)) //EQUALGRID, HILBERT, RTREE, VORONOI, QUADTREE, KDBTREE
    val indexType = IndexType.getIndexType(args(3))  //RTREE, QUADTREE

    val shapeFileMetaRDD1 = new ShapeFileMetaRDD(sc, hConf)
    val table1 = tableNames(0)
    shapeFileMetaRDD1.initializeShapeFileMetaRDDAndPartitioner(sc, table1, gridType, partitionNum, minX, minY, maxX, maxY)
    val geometryRDD1 = new GeometryRDD
    geometryRDD1.initialize(shapeFileMetaRDD1, hasAttribute = false)
    geometryRDD1.partition(shapeFileMetaRDD1.getPartitioner)
    geometryRDD1.indexPartition(indexType)
    geometryRDD1.cache()

    println("*************Counting GeometryRDD1 Time: " + OperationUtil.show_timing(geometryRDD1.getGeometryRDD.count()))

    val partitionNum1 = geometryRDD1.getGeometryRDD.mapPartitionsWithIndex({
      case (index, itor) => {
        List((index, itor.size)).toIterator
      }
    }).collect()

    println("********geometryRDD1*************\n")
    partitionNum1.foreach(println)
    println("********geometryRDD1*************\n")
    println("******geometryRDD1****************" + geometryRDD1.getGeometryRDD.count())

    val shapeFileMetaRDD2 = new ShapeFileMetaRDD(sc, hConf)
    val table2 = tableNames(1)
    shapeFileMetaRDD2.initializeShapeFileMetaRDDWithoutPartition(sc, table2,
      partitionNum, minX, minY, maxX, maxY)

    val geometryRDD2 = new GeometryRDD
    geometryRDD2.initialize(shapeFileMetaRDD2, hasAttribute = false)
    geometryRDD2.partition(shapeFileMetaRDD1.getPartitioner)
    geometryRDD2.cache()

    println("*************Counting GeometryRDD2 Time: " + OperationUtil.show_timing(geometryRDD2.getGeometryRDD.count()))


    val partitionNum2 = geometryRDD2.getGeometryRDD.mapPartitionsWithIndex({
      case (index, itor) => {
        List((index, itor.size)).toIterator
      }
    }).collect()

    println("*********geometryRDD2************\n")
    partitionNum2.foreach(println)
    println("*********geometryRDD2************\n")

    println("******geometryRDD2****************" + geometryRDD2.getGeometryRDD.count())

    println(geometryRDD1.getGeometryRDD.partitions.length
      + "**********************"
      + geometryRDD2.getGeometryRDD.partitions.length)

    val geometryRDD = geometryRDD1.intersect(geometryRDD2)
    geometryRDD.cache()

    val filePath = args(4)
    if (filePath.endsWith("shp")) {
      geometryRDD.saveAsShapefile(filePath, "epsg:4326")
    } else {
      geometryRDD.saveAsGeoJSON(filePath)
    }

    println("******** Number of intersected polygons: %d".format(geometryRDD.getGeometryRDD.count()))
    println("************** Total time: " + (System.currentTimeMillis() - t)/1000000)
  }

}

```
 
#### STC_OverlapTest_v3.scala

```scala
package edu.gmu.stc.vector.sparkshell

import edu.gmu.stc.config.ConfigParameter
import edu.gmu.stc.vector.operation.OperationUtil
import edu.gmu.stc.vector.rdd.{GeometryRDD, ShapeFileMetaRDD}
import edu.gmu.stc.vector.serde.VectorKryoRegistrator
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.spark.internal.Logging
import org.apache.spark.{SparkConf, SparkContext}
import org.datasyslab.geospark.enums.{GridType, IndexType}

/**
  * Created by Fei Hu.
  */
object STC_OverlapTest_v3 extends Logging{
  def main(args: Array[String]): Unit = {

    if (args.length != 5) {
      logError("You input "+ args.length + "arguments: " + args.mkString(" ") + ", but it requires 5 arguments: " +
        "\n \t 1)configFilePath: this file path for the configuration file path" +
        "\n \t 2) numPartition: the number of partitions" +
        "\n \t 3) gridType: the type of the partition, e.g. EQUALGRID, HILBERT, RTREE, VORONOI, QUADTREE, KDBTREE" +
        "\n \t 4) indexType: the index type for each partition, e.g. QUADTREE, RTREE" +
        "\n \t 5) output file path: the file path for geojson output")

      return
    }

    val t = System.currentTimeMillis()

    val sparkConf = new SparkConf().setAppName("%s_%s_%s_%s".format("STC_OverlapTest_v2", args(1), args(2), args(3)))
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.kryo.registrator", classOf[VectorKryoRegistrator].getName)

    if (System.getProperty("os.name").equals("Mac OS X")) {
      sparkConf.setMaster("local[6]")
    }

    val sc = new SparkContext(sparkConf)

    val configFilePath = args(0)   //"/Users/feihu/Documents/GitHub/GeoSpark/config/conf.xml"
    val hConf = new Configuration()
    hConf.addResource(new Path(configFilePath))
    sc.hadoopConfiguration.addResource(hConf)

    val tableNames = hConf.get(ConfigParameter.SHAPEFILE_INDEX_TABLES).split(",").map(s => s.toLowerCase().trim)

    val partitionNum = args(1).toInt  //24
    val minX = -180
    val minY = -180
    val maxX = 180
    val maxY = 180

    val gridType = GridType.getGridType(args(2)) //EQUALGRID, HILBERT, RTREE, VORONOI, QUADTREE, KDBTREE
    val indexType = IndexType.getIndexType(args(3))  //RTREE, QUADTREE

    val shapeFileMetaRDD1 = new ShapeFileMetaRDD(sc, hConf)
    val table1 = tableNames(0)
    shapeFileMetaRDD1.initializeShapeFileMetaRDDAndPartitioner(sc, table1, gridType, partitionNum, minX, minY, maxX, maxY)
    val geometryRDD1 = new GeometryRDD
    geometryRDD1.initialize(shapeFileMetaRDD1, hasAttribute = false)
    geometryRDD1.partition(shapeFileMetaRDD1.getPartitioner)
    geometryRDD1.indexPartition(indexType)
    geometryRDD1.cache()

    logInfo("*************Counting GeometryRDD1 Time: " + OperationUtil.show_timing(geometryRDD1.getGeometryRDD.count()))

    val partitionNum1 = geometryRDD1.getGeometryRDD.mapPartitionsWithIndex({
      case (index, itor) => {
        List((index, itor.size)).toIterator
      }
    }).collect()

    logDebug("********geometryRDD1*************\n")
    partitionNum1.foreach(println)
    logDebug("********geometryRDD1*************\n")
    logDebug("******geometryRDD1****************" + geometryRDD1.getGeometryRDD.count())

    val shapeFileMetaRDD2 = new ShapeFileMetaRDD(sc, hConf)
    val table2 = tableNames(1)
    shapeFileMetaRDD2.initializeShapeFileMetaRDDWithoutPartition(sc, table2,
      partitionNum, minX, minY, maxX, maxY)

    val geometryRDD2 = new GeometryRDD
    geometryRDD2.initialize(shapeFileMetaRDD2, hasAttribute = false)
    geometryRDD2.partition(shapeFileMetaRDD1.getPartitioner)
    geometryRDD2.cache()

    logDebug("*************Counting GeometryRDD2 Time: " + OperationUtil.show_timing(geometryRDD2.getGeometryRDD.count()))


    val partitionNum2 = geometryRDD2.getGeometryRDD.mapPartitionsWithIndex({
      case (index, itor) => {
        List((index, itor.size)).toIterator
      }
    }).collect()

    logDebug("*********geometryRDD2************\n")
    partitionNum2.foreach(println)
    logDebug("*********geometryRDD2************\n")

    logDebug("******geometryRDD2****************" + geometryRDD2.getGeometryRDD.count())

    logDebug(geometryRDD1.getGeometryRDD.partitions.length
      + "**********************"
      + geometryRDD2.getGeometryRDD.partitions.length)

    val startTime = System.currentTimeMillis()
    val geometryRDD = geometryRDD1.intersectV2(geometryRDD2, partitionNum)
    geometryRDD.cache()
    val endTime = System.currentTimeMillis()
    logDebug("******** Intersection time: " + (endTime - startTime)/1000000)

    val filePath = args(4)
    if (filePath.endsWith("shp")) {
      geometryRDD.saveAsShapefile(filePath, "epsg:4326")
    } else {
      geometryRDD.saveAsGeoJSON(filePath)
    }

    logDebug("******** Number of intersected polygons: %d".format(geometryRDD.getGeometryRDD.count()))

    logDebug("************** Total time: " + (System.currentTimeMillis() - t)/1000000)
  }

}

```
 
#### SpatialJoin.scala

```scala
package edu.gmu.stc.vector.sparkshell

import com.vividsolutions.jts.geom.Envelope
import edu.gmu.stc.config.ConfigParameter
import edu.gmu.stc.vector.operation.OperationUtil
import edu.gmu.stc.vector.rdd.{GeometryRDD, ShapeFileMetaRDD}
import edu.gmu.stc.vector.serde.VectorKryoRegistrator
import edu.gmu.stc.vector.sparkshell.STC_OverlapTest_v3.{logDebug, logError, logInfo}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.internal.Logging
import org.datasyslab.geospark.enums.{GridType, IndexType}
import org.geotools.geometry.jts.JTS
import org.geotools.referencing.CRS
import edu.gmu.vector.landscape.ComputeLandscape._
import org.apache.spark.sql.SQLContext

/**
  * Created by Fei Hu on 3/11/18.
  */
object SpatialJoin extends Logging{

  def main(args: Array[String]): Unit = {

    if (args.length != 4) {
      logError("You input "+ args.length + "arguments: " + args.mkString(" ") + ", but it requires 5 arguments: " +
        "\n \t 1)configFilePath: this file path for the configuration file path" +
        "\n \t 2) numPartition: the number of partitions" +
        "\n \t 3) gridType: the type of the partition, e.g. EQUALGRID, HILBERT, RTREE, VORONOI, QUADTREE, KDBTREE" +
        "\n \t 4) indexType: the index type for each partition, e.g. QUADTREE, RTREE")

      return
    }

    val sparkConf = new SparkConf().setAppName("%s_%s_%s_%s".format("Spatial_Join", args(1), args(2), args(3)))
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.kryo.registrator", classOf[VectorKryoRegistrator].getName)

    if (System.getProperty("os.name").equals("Mac OS X")) {
      sparkConf.setMaster("local[6]")
    }

    val sc = new SparkContext(sparkConf)


    val configFilePath = args(0)   //"/Users/feihu/Documents/GitHub/GeoSpark/config/conf.xml"
    val hConf = new Configuration()
    hConf.addResource(new Path(configFilePath))
    sc.hadoopConfiguration.addResource(hConf)

    val tableNames = hConf.get(ConfigParameter.SHAPEFILE_INDEX_TABLES).split(",").map(s => s.toLowerCase().trim)

    val partitionNum = args(1).toInt  //24

    // Bounding box for querying shapefiles
    val minX = -77.786
    val minY = 38.352
    val maxX = -76.914
    val maxY = 39.151

    /*val minX = -76.6517
    val minY = 39.2622
    val maxX = -76.5579
    val maxY = 39.3212*/

    //Transform query bbox projection
    /*val sourceCRS = CRS.decode("epsg:4326")
    val targetCRS = CRS.decode("epsg:32618")
    val transform = CRS.findMathTransform(sourceCRS, targetCRS)
    val envelope = new Envelope(minX, minY, maxX, maxY)
    val envelope2D = JTS.getEnvelope2D(envelope, sourceCRS)
    val bbox = JTS.transform(envelope, transform)*/


    val gridType = args(2) //EQUALGRID, HILBERT, RTREE, VORONOI, QUADTREE, KDBTREE
    val indexType = args(3)  //RTREE, QUADTREE
    val table1 = tableNames(0)
    val table2 = tableNames(1)
    val table3 = tableNames(2)

    val geometryRDD1 = GeometryRDD(sc, hConf, table1, gridType, indexType, partitionNum, minX, minY, maxX, maxY, true, true)

    logDebug("********geometryRDD1*************\n")
    OperationUtil.show_partitionInfo(geometryRDD1.getGeometryRDD)
    logInfo("******Geometry Num****************" + geometryRDD1.getGeometryRDD.count())

    val geometryRDD2 = GeometryRDD(sc, hConf, table2, partitionNum, geometryRDD1.getPartitioner, minX, minY, maxX, maxY, true, true)

    logDebug("*************Counting GeometryRDD2 Time: " + OperationUtil.show_timing(geometryRDD2.getGeometryRDD.count()))
    logDebug("********geometryRDD2*************\n")
    OperationUtil.show_partitionInfo(geometryRDD2.getGeometryRDD)
    logInfo("******Geometry Num****************" + geometryRDD2.getGeometryRDD.count())

    geometryRDD2.getGeometryRDD.foreach(geometry => println(geometry.getUserData))

    val joinedGeometryRDD12 = geometryRDD1.spatialJoin(geometryRDD2)
    joinedGeometryRDD12.cache()

    val landscapeMetricRDD = joinedGeometryRDD12.map {
      case (geoCover, geoFeatureList) => {
        (computeCoverPercent(geoCover, geoFeatureList),
        computeMeanNearestNeighborDistance(geoCover, geoFeatureList),
        computePatchCohesionIndex(geoCover, geoFeatureList))
      }
    }

    val sqlContext = new SQLContext(sc)
    import sqlContext.implicits._

    val df = landscapeMetricRDD.toDF()
    df.show(100)
    //joinedGeometryRDD12.foreach(tuple => println(tuple._2.foldLeft[Double](0.0)((area, g) => area + g.getArea)))

    /*val geometryRDD3 = GeometryRDD(sc, hConf, table3, partitionNum, geometryRDD1.getPartitioner, minX, minY, maxX, maxY, true, true)
    val joinedGeometryRDD13 = geometryRDD1.spatialJoin(geometryRDD3)

    joinedGeometryRDD13.foreach {
      case(geoBlock, geoItor) => {
        val (t, area) = geoItor.foldLeft[(Double, Double)]((0.0, 0.0))((tuple, geo) => {
          val area = geo.getArea
          val t = geo.getUserData.asInstanceOf[String].toDouble * area

          (tuple._1 + t, tuple._2 + area)
        })

        println(t/area + " : temperature")
      }
    }*/
  }

}

```




### SparkCity/application/src/main/scala/edu/gmu/stc/vector/examples/

#### GeoSparkExample.scala

```scala
package edu.gmu.stc.vector.examples

import java.awt.Color

import com.vividsolutions.jts.geom._
import org.apache.log4j.{Level, Logger}
import org.apache.spark.serializer.KryoSerializer
import org.apache.spark.sql.SparkSession
import org.datasyslab.geospark.enums.{GridType, IndexType}
import org.datasyslab.geospark.formatMapper.shapefileParser.ShapefileReader
import org.datasyslab.geospark.serde.GeoSparkKryoRegistrator
import org.datasyslab.geospark.spatialOperator.JoinQuery
import org.datasyslab.geospark.spatialRDD.{CircleRDD, SpatialRDD}
import org.datasyslab.geosparksql.utils.{Adapter, GeoSparkSQLRegistrator}
import org.datasyslab.geosparkviz.core.{ImageGenerator, RasterOverlayOperator}
import org.datasyslab.geosparkviz.extension.visualizationEffect.{HeatMap, ScatterPlot}
import org.datasyslab.geosparkviz.utils.ImageType

/**
  * Created by Fei Hu
  */
object GeoSparkExample {
  def main(args: Array[String]): Unit = {
    val resourceFolder = System.getProperty("user.dir") + "/application/src/main/resources/data/"

    // Data link (in shapefile): https://geo.nyu.edu/catalog/nyu_2451_34514
    val nycArealandmarkShapefileLocation = resourceFolder + "nyc-area-landmark-shapefile"

    // Data link (in CSV): http://www.nyc.gov/html/tlc/html/about/trip_record_data.shtml
    val nyctripCSVLocation = resourceFolder + "yellow_tripdata_2009-01-subset.csv"

    val colocationMapLocation = resourceFolder + "colocationMap"

    visualizeSpatialColocation()
    calculateSpatialColocation()

    System.out.println("Finished GeoSpark Spatial Analysis Example")


    def visualizeSpatialColocation(): Unit = {
      val sparkSession: SparkSession = SparkSession.builder()
        .master("local[*]").appName("GeoSpark-Analysis").getOrCreate()
      Logger.getLogger("org").setLevel(Level.WARN)
      Logger.getLogger("akka").setLevel(Level.WARN)

      GeoSparkSQLRegistrator.registerAll(sparkSession.sqlContext)

      // 准备纽约地区地标，包括机场、博物馆、大学、医院
      val arealmRDD = ShapefileReader.readToPolygonRDD(sparkSession.sparkContext,
                                                       nycArealandmarkShapefileLocation)

      // 准备纽约出租车之旅。仅使用出租车行程的接送点
      val tripDf = sparkSession.read.format("csv")
                                    .option("delimiter", ",")
                                    .option("header", "false")
                                    .load(nyctripCSVLocation)
      // 从 DataFrame 转换为 RDD。这也可以直接通过 GeoSpark RDD API 完成。
      tripDf.createOrReplaceTempView("tripdf")
      val tripRDD = new SpatialRDD[Geometry]
      tripRDD.rawSpatialRDD = Adapter.toRdd(sparkSession.sql(
        "select ST_Point(cast(tripdf._c0 as Decimal(24, 14)), " +
          "cast(tripdf._c1 as Decimal(24, 14))) from tripdf"))

      // 将坐标参考系从基于度的转换为基于米的。
      // 这将返回准确的距离计算。
      arealmRDD.CRSTransform("epsg:4326", "epsg:3857")
      tripRDD.CRSTransform("epsg:4326", "epsg:3857")

      // !!!NOTE!!!: 如果您知道数据集的矩形边界和近似总数，则可以避免分析 RDD 步骤。
      arealmRDD.analyze()
      tripRDD.analyze()

      val imageResolutionX = 1000
      val imageResolutionY = 1000

      val frontImage = new ScatterPlot(imageResolutionX, imageResolutionY,
                                       arealmRDD.boundaryEnvelope, true)
      frontImage.CustomizeColor(0, 0, 0, 255, Color.GREEN, true)
      frontImage.Visualize(sparkSession.sparkContext, arealmRDD)

      val backImage = new HeatMap(imageResolutionX, imageResolutionY,
                                  arealmRDD.boundaryEnvelope, true, 1)
      backImage.Visualize(sparkSession.sparkContext, tripRDD)

      val overlayOperator = new RasterOverlayOperator(backImage.rasterImage)
      overlayOperator.JoinImage(frontImage.rasterImage)

      val imageGenerator = new ImageGenerator
      imageGenerator.SaveRasterImageAsLocalFile(overlayOperator.backRasterImage,
                                                colocationMapLocation, ImageType.PNG)

      sparkSession.stop()
    }

    def calculateSpatialColocation(): Unit = {
      val sparkSession: SparkSession = SparkSession.builder()
        .config("spark.serializer", classOf[KryoSerializer].getName)
        .config("spark.kryo.registrator", classOf[GeoSparkKryoRegistrator].getName)
        .master("local[*]")
        .appName("GeoSpark-Analysis")
        .getOrCreate()

      Logger.getLogger("org").setLevel(Level.WARN)
      Logger.getLogger("akka").setLevel(Level.WARN)

      GeoSparkSQLRegistrator.registerAll(sparkSession.sqlContext)


      // 准备纽约地区地标，包括机场、博物馆、大学、医院
      val arealmRDD = new SpatialRDD[Geometry]()
      arealmRDD.rawSpatialRDD = ShapefileReader.readToGeometryRDD(sparkSession.sparkContext,
                                                                  nycArealandmarkShapefileLocation)
      // 使用区域地标的中心点检查并置。这是 Ripley 的 K 函数所要求的。
      arealmRDD.rawSpatialRDD = arealmRDD.rawSpatialRDD.rdd.map[Geometry](f => f.getCentroid)

      // 以下两行是可选的。目的是显示shapefile的结构。
      val arealmDf = Adapter.toDf(arealmRDD, sparkSession)
      arealmDf.show()

      // 准备纽约出租车之旅。仅使用出租车行程的接送点
      val tripDf = sparkSession.read.format("csv")
        .option("delimiter", ",")
        .option("header", "false")
        .load(nyctripCSVLocation)

      tripDf.show() // Optional
      // 从 DataFrame 转换为 RDD。这也可以直接通过 GeoSpark RDD API 完成。
      tripDf.createOrReplaceTempView("tripdf")
      val tripRDD = new SpatialRDD[Geometry]
      tripRDD.rawSpatialRDD = Adapter.toRdd(
        sparkSession.sql("select ST_Point(cast(tripdf._c0 as Decimal(24, 14)), " +
          "cast(tripdf._c1 as Decimal(24, 14))) from tripdf"))

      // 将坐标参考系从基于度的转换为基于米的。这将返回准确的距离计算。
      arealmRDD.CRSTransform("epsg:4326", "epsg:3857")
      tripRDD.CRSTransform("epsg:4326", "epsg:3857")

      // !!!NOTE!!!: 如果您知道数据集的矩形边界和近似总数，则可以避免分析 RDD 步骤。
      arealmRDD.analyze()
      tripRDD.analyze()

      // 缓存索引 NYC 出租车行程 rdd 以提高迭代性能
      tripRDD.spatialPartitioning(GridType.KDBTREE)
      tripRDD.buildIndex(IndexType.QUADTREE, true)
      tripRDD.indexedRDD = tripRDD.indexedRDD.cache()

      // 参数设置。检查 Ripley's K 函数的定义。
      val area = tripRDD.boundaryEnvelope.getArea
      val maxDistance = 0.01 * Math.max(tripRDD.boundaryEnvelope.getHeight,
                                        tripRDD.boundaryEnvelope.getWidth)
      val iterationTimes = 10
      val distanceIncrement = maxDistance / iterationTimes
      val beginDistance = 0.0
      var currentDistance = 0.0

      // 开始迭代
      println("distance(meter),observedL,difference,coLocationStatus")
      for (i <- 1 to iterationTimes) {
        currentDistance = beginDistance + i * distanceIncrement

        val bufferedArealmRDD = new CircleRDD(arealmRDD, currentDistance)
        bufferedArealmRDD.spatialPartitioning(tripRDD.getPartitioner)
        // 运行 GeoSpark 距离 Join Query
        val adjacentMatrix = JoinQuery.DistanceJoinQueryFlat(tripRDD, bufferedArealmRDD, true, true)

        // 如果您想查看 SparkSQL 中连接结果的样子，请取消对以下两行的注释
        // var adjacentMatrixDf = Adapter.toDf(adjacentMatrix, sparkSession)
        // adjacentMatrixDf.show()

        val observedK = adjacentMatrix.count() * area * 1.0 / (arealmRDD.approximateTotalCount *
          tripRDD.approximateTotalCount)

        val observedL = Math.sqrt(observedK / Math.PI)
        val expectedL = currentDistance
        val colocationDifference = observedL - expectedL
        val colocationStatus = {
          if (colocationDifference > 0) "Co-located" else "Dispersed"
        }

        println(s"""$currentDistance,$observedL,$colocationDifference,$colocationStatus""")
      }
      sparkSession.stop()
    }

  }


}

```
 
#### OverlapExample.scala

extends App

```scala
package edu.gmu.stc.vector.examples

import edu.gmu.stc.vector.operation.Overlap.intersect
import edu.gmu.stc.vector.operation.OperationUtil.show_timing
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.datasyslab.geospark.formatMapper.shapefileParser.ShapefileReader
import org.datasyslab.geosparksql.utils.GeoSparkSQLRegistrator

/**
  * Created by Fei Hu.
  */
object OverlapExample extends App {
  val LOG = Logger.getLogger("OverlapExample")

  Logger.getLogger("org").setLevel(Level.WARN)
  Logger.getLogger("akka").setLevel(Level.WARN)

  LOG.info("Start GeoSpark Overlap Spatial Analysis Example")

  val sparkSession: SparkSession = SparkSession.builder()
    .master("local[6]")
    .appName("GeoSpark-Analysis")
    .getOrCreate()

  //val sqlContext = new SQLContext(sc)
  //val sparkSession: SparkSession = sqlContext.sparkSession
  GeoSparkSQLRegistrator.registerAll(sparkSession.sqlContext)

  val resourceFolder = "/Users/feihu/Documents/GitHub/GeoSpark/application/src/main/resources/data/Washington_DC/"
  //val resourceFolder = "/user/root/data0119/"
  val shpFile1 = resourceFolder + "Impervious_Surface_2015_DC"
  val shpFile2 = resourceFolder + "Soil_Type_by_Slope_DC"
  val outputFile = resourceFolder + "dc_overlayMap_intersect_" + System.currentTimeMillis() + ".geojson"

  val numPartition = 24

  /*val plg1RDD = ShapefileReader.readToPolygonRDD(sparkSession.sparkContext, shpFile1)
  println(plg1RDD.rawSpatialRDD.count())*/

  val runtime = show_timing(intersect(sparkSession, "RTREE", "QUADTREE", shpFile1, shpFile2, numPartition, outputFile))
  LOG.info(s"*** Runtime is : $runtime")
  LOG.info("Finish GeoSpark Overlap Spatial Analysis Example")

  sparkSession.stop()
}

```
 
#### ShapeFileMetaTest.scala

```scala
package edu.gmu.stc.vector.examples

import edu.gmu.stc.vector.operation.Overlap.intersect
import edu.gmu.stc.vector.operation.OperationUtil.show_timing
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.datasyslab.geospark.formatMapper.shapefileParser.ShapefileReader
import org.datasyslab.geosparksql.utils.GeoSparkSQLRegistrator

/**
  * Created by Fei Hu.
  */
object OverlapExample extends App {
  val LOG = Logger.getLogger("OverlapExample")

  Logger.getLogger("org").setLevel(Level.WARN)
  Logger.getLogger("akka").setLevel(Level.WARN)

  LOG.info("Start GeoSpark Overlap Spatial Analysis Example")

  val sparkSession: SparkSession = SparkSession.builder()
    .master("local[6]")
    .appName("GeoSpark-Analysis")
    .getOrCreate()

  //val sqlContext = new SQLContext(sc)
  //val sparkSession: SparkSession = sqlContext.sparkSession
  GeoSparkSQLRegistrator.registerAll(sparkSession.sqlContext)

  val resourceFolder = "/Users/feihu/Documents/GitHub/GeoSpark/application/src/main/resources/data/Washington_DC/"
  //val resourceFolder = "/user/root/data0119/"
  val shpFile1 = resourceFolder + "Impervious_Surface_2015_DC"
  val shpFile2 = resourceFolder + "Soil_Type_by_Slope_DC"
  val outputFile = resourceFolder + "dc_overlayMap_intersect_" + System.currentTimeMillis() + ".geojson"

  val numPartition = 24

  /*val plg1RDD = ShapefileReader.readToPolygonRDD(sparkSession.sparkContext, shpFile1)
  println(plg1RDD.rawSpatialRDD.count())*/

  val runtime = show_timing(intersect(sparkSession, "RTREE", "QUADTREE", shpFile1, shpFile2, numPartition, outputFile))
  LOG.info(s"*** Runtime is : $runtime")
  LOG.info("Finish GeoSpark Overlap Spatial Analysis Example")

  sparkSession.stop()
}

```


### SparkCity/viz/src/main/scala/org/datasyslab/geosparkviz/showcase/

#### ScalaExample.scala

```scala
/**
	* FILE: ScalaExample.java
	* PATH: org.datasyslab.geospark.showcase.ScalaExample.scala
	* Copyright (c) 2017 Arizona State University Data Systems Lab
	* All rights reserved.
	*/
package org.datasyslab.geosparkviz.showcase

import java.awt.Color
import java.io.FileInputStream
import java.util.Properties

import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.storage.StorageLevel
import org.datasyslab.geosparkviz.core.{ImageGenerator, RasterOverlayOperator}
import org.datasyslab.geosparkviz.extension.visualizationEffect.ChoroplethMap
import org.datasyslab.geosparkviz.extension.visualizationEffect.HeatMap
import org.datasyslab.geosparkviz.extension.visualizationEffect.ScatterPlot
import org.datasyslab.geosparkviz.utils.{ColorizeOption, ImageType}
import com.vividsolutions.jts.geom.Envelope
import org.datasyslab.geospark.enums.{FileDataSplitter, GridType, IndexType}
import org.datasyslab.geospark.formatMapper.EarthdataHDFPointMapper
import org.datasyslab.geospark.spatialOperator.JoinQuery
import org.datasyslab.geospark.spatialRDD.{PointRDD, PolygonRDD, RectangleRDD}


/**
	* The Class ScalaExample.
	*/
object ScalaExample extends App{
	val sparkConf = new SparkConf().setAppName("GeoSparkVizDemo").setMaster("local[4]")
	val sparkContext = new SparkContext(sparkConf)
	Logger.getLogger("org").setLevel(Level.WARN)
	Logger.getLogger("akka").setLevel(Level.WARN)
	val prop = new Properties()
	val resourcePath = "src/test/resources/"
	val demoOutputPath = "target/demo"
	var ConfFile = new FileInputStream(resourcePath + "babylon.point.properties")
	prop.load(ConfFile)
	val scatterPlotOutputPath = System.getProperty("user.dir") + "/" + demoOutputPath + "/scatterplot"
	val heatMapOutputPath = System.getProperty("user.dir") + "/" + demoOutputPath + "/heatmap"
	val choroplethMapOutputPath = System.getProperty("user.dir") + "/" + demoOutputPath + "/choroplethmap"
	val parallelFilterRenderStitchOutputPath = System.getProperty("user.dir") + "/" + demoOutputPath + "/parallelfilterrenderstitchheatmap"
	val earthdataScatterPlotOutputPath = System.getProperty("user.dir") + "/" + demoOutputPath + "/earthdatascatterplot"
	val PointInputLocation = "file://" + System.getProperty("user.dir") + "/" + resourcePath + prop.getProperty("inputLocation")
	val PointOffset = prop.getProperty("offset").toInt
	val PointSplitter = FileDataSplitter.getFileDataSplitter(prop.getProperty("splitter"))
	val PointNumPartitions = prop.getProperty("numPartitions").toInt
	ConfFile = new FileInputStream(resourcePath + "babylon.rectangle.properties")
	prop.load(ConfFile)
	val RectangleInputLocation = "file://" + System.getProperty("user.dir") + "/" + resourcePath + prop.getProperty("inputLocation")
	val RectangleOffset = prop.getProperty("offset").toInt
	val RectangleSplitter = FileDataSplitter.getFileDataSplitter(prop.getProperty("splitter"))
	val RectangleNumPartitions = prop.getProperty("numPartitions").toInt
	ConfFile = new FileInputStream(resourcePath + "babylon.polygon.properties")
	prop.load(ConfFile)
	val PolygonInputLocation = "file://" + System.getProperty("user.dir") + "/" + resourcePath + prop.getProperty("inputLocation")
	val PolygonOffset = prop.getProperty("offset").toInt
	val PolygonSplitter = FileDataSplitter.getFileDataSplitter(prop.getProperty("splitter"))
	val PolygonNumPartitions = prop.getProperty("numPartitions").toInt
	ConfFile = new FileInputStream(resourcePath + "babylon.linestring.properties")
	prop.load(ConfFile)
	val LineStringInputLocation = "file://" + System.getProperty("user.dir") + "/" + resourcePath + prop.getProperty("inputLocation")
	val LineStringOffset = prop.getProperty("offset").toInt
	val LineStringSplitter = FileDataSplitter.getFileDataSplitter(prop.getProperty("splitter"))
	val LineStringNumPartitions = prop.getProperty("numPartitions").toInt
	val USMainLandBoundary = new Envelope(-126.790180, -64.630926, 24.863836, 50.000)
	val earthdataInputLocation = System.getProperty("user.dir") + "/src/test/resources/modis/modis.csv"
	val earthdataNumPartitions = 5
	val HDFIncrement = 5
	val HDFOffset = 2
	val HDFRootGroupName = "MOD_Swath_LST"
	val HDFDataVariableName = "LST"
	val HDFDataVariableList = Array("LST", "QC", "Error_LST", "Emis_31", "Emis_32")
	val HDFswitchXY = true
	val urlPrefix = System.getProperty("user.dir") + "/src/test/resources/modis/"

	if (buildScatterPlot(scatterPlotOutputPath) && buildHeatMap(heatMapOutputPath)
		&& buildChoroplethMap(choroplethMapOutputPath) && parallelFilterRenderStitch(parallelFilterRenderStitchOutputPath + "-stitched")
		&& parallelFilterRenderNoStitch(parallelFilterRenderStitchOutputPath) && earthdataVisualization(earthdataScatterPlotOutputPath))
		System.out.println("All 5 GeoSparkViz Demos have passed.")
	else System.out.println("GeoSparkViz Demos failed.")

	/**
		* Builds the scatter plot.
		*
		* @param outputPath the output path
		* @return true, if successful
		*/
	def buildScatterPlot(outputPath: String): Boolean = {
		val spatialRDD = new PolygonRDD(sparkContext, PolygonInputLocation, PolygonSplitter, false, PolygonNumPartitions)
		var visualizationOperator = new ScatterPlot(1000, 600, USMainLandBoundary, false)
		visualizationOperator.CustomizeColor(255, 255, 255, 255, Color.GREEN, true)
		visualizationOperator.Visualize(sparkContext, spatialRDD)
		var imageGenerator = new ImageGenerator
		imageGenerator.SaveRasterImageAsLocalFile(visualizationOperator.rasterImage, outputPath, ImageType.PNG)
		visualizationOperator = new ScatterPlot(1000, 600, USMainLandBoundary, false, -1, -1, false, true)
		visualizationOperator.CustomizeColor(255, 255, 255, 255, Color.GREEN, true)
		visualizationOperator.Visualize(sparkContext, spatialRDD)
		imageGenerator = new ImageGenerator
		imageGenerator.SaveVectorImageAsLocalFile(visualizationOperator.vectorImage, outputPath, ImageType.SVG)
		visualizationOperator = new ScatterPlot(1000, 600, USMainLandBoundary, false, -1, -1, true, true)
		visualizationOperator.CustomizeColor(255, 255, 255, 255, Color.GREEN, true)
		visualizationOperator.Visualize(sparkContext, spatialRDD)
		imageGenerator = new ImageGenerator
		imageGenerator.SaveVectorImageAsLocalFile(visualizationOperator.distributedVectorImage, "file://" + outputPath + "-distributed", ImageType.SVG)
		true
	}

	/**
		* Builds the heat map.
		*
		* @param outputPath the output path
		* @return true, if successful
		*/
	def buildHeatMap(outputPath: String): Boolean = {
		val spatialRDD = new RectangleRDD(sparkContext, RectangleInputLocation, RectangleSplitter, false, RectangleNumPartitions, StorageLevel.MEMORY_ONLY)
		val visualizationOperator = new HeatMap(1000, 600, USMainLandBoundary, false, 2)
		visualizationOperator.Visualize(sparkContext, spatialRDD)
		val imageGenerator = new ImageGenerator
		imageGenerator.SaveRasterImageAsLocalFile(visualizationOperator.rasterImage, outputPath, ImageType.PNG)
		true
	}

	/**
		* Builds the choropleth map.
		*
		* @param outputPath the output path
		* @return true, if successful
		*/
	def buildChoroplethMap(outputPath: String): Boolean = {
		val spatialRDD = new PointRDD(sparkContext, PointInputLocation, PointOffset, PointSplitter, false, PointNumPartitions, StorageLevel.MEMORY_ONLY)
		val queryRDD = new PolygonRDD(sparkContext, PolygonInputLocation, PolygonSplitter, false, PolygonNumPartitions, StorageLevel.MEMORY_ONLY)
		spatialRDD.spatialPartitioning(GridType.RTREE)
		queryRDD.spatialPartitioning(spatialRDD.grids)
		spatialRDD.buildIndex(IndexType.RTREE, true)
		val joinResult = JoinQuery.SpatialJoinQueryCountByKey(spatialRDD, queryRDD, true, false)
		val visualizationOperator = new ChoroplethMap(1000, 600, USMainLandBoundary, false)
		visualizationOperator.CustomizeColor(255, 255, 255, 255, Color.RED, true)
		visualizationOperator.Visualize(sparkContext, joinResult)
		val frontImage = new ScatterPlot(1000, 600, USMainLandBoundary, false)
		frontImage.CustomizeColor(0, 0, 0, 255, Color.GREEN, true)
		frontImage.Visualize(sparkContext, queryRDD)
		val overlayOperator = new RasterOverlayOperator(visualizationOperator.rasterImage)
		overlayOperator.JoinImage(frontImage.rasterImage)
		val imageGenerator = new ImageGenerator
		imageGenerator.SaveRasterImageAsLocalFile(overlayOperator.backRasterImage, outputPath, ImageType.PNG)
		true
	}

	/**
		* Parallel filter render stitch.
		*
		* @param outputPath the output path
		* @return true, if successful
		*/
	def parallelFilterRenderNoStitch(outputPath: String): Boolean = {
		val spatialRDD = new RectangleRDD(sparkContext, RectangleInputLocation, RectangleSplitter, false, RectangleNumPartitions, StorageLevel.MEMORY_ONLY)
		val visualizationOperator = new HeatMap(1000, 600, USMainLandBoundary, false, 2, 4, 4, true, true)
		visualizationOperator.Visualize(sparkContext, spatialRDD)
		val imageGenerator = new ImageGenerator
		imageGenerator.SaveRasterImageAsLocalFile(visualizationOperator.distributedRasterImage, outputPath, ImageType.PNG,0,4,4)
		true
	}

	/**
		* Parallel filter render stitch.
		*
		* @param outputPath the output path
		* @return true, if successful
		*/
	def parallelFilterRenderStitch(outputPath: String): Boolean = {
		val spatialRDD = new RectangleRDD(sparkContext, RectangleInputLocation, RectangleSplitter, false, RectangleNumPartitions, StorageLevel.MEMORY_ONLY)
		val visualizationOperator = new HeatMap(1000, 600, USMainLandBoundary, false, 2, 4, 4, true, true)
		visualizationOperator.Visualize(sparkContext, spatialRDD)
		val imageGenerator = new ImageGenerator
		imageGenerator.SaveRasterImageAsLocalFile(visualizationOperator.rasterImage, outputPath, ImageType.PNG)
		true
	}

	def earthdataVisualization(outputPath: String): Boolean = {
		val earthdataHDFPoint = new EarthdataHDFPointMapper(HDFIncrement, HDFOffset, HDFRootGroupName,
			HDFDataVariableList, HDFDataVariableName, HDFswitchXY, urlPrefix)
		val spatialRDD = new PointRDD(sparkContext, earthdataInputLocation, earthdataNumPartitions, earthdataHDFPoint, StorageLevel.MEMORY_ONLY)
		val visualizationOperator = new ScatterPlot(1000, 600, spatialRDD.boundaryEnvelope, ColorizeOption.EARTHOBSERVATION, false, false)
		visualizationOperator.CustomizeColor(255, 255, 255, 255, Color.BLUE, true)
		visualizationOperator.Visualize(sparkContext, spatialRDD)
		val imageGenerator = new ImageGenerator
		imageGenerator.SaveRasterImageAsLocalFile(visualizationOperator.rasterImage, outputPath, ImageType.PNG)
		true
	}


}
```


### SparkCity/sql/src/main/scala/org/apache/spark/sql/geosparksql/strategy/join/

#### DistanceJoinExec.scala

```scala
/**
  * FILE: DistanceJoinExec
  * PATH: org.apache.spark.sql.geosparksql.strategy.join.DistanceJoinExec
  * Copyright (c) GeoSpark Development Team
  *
  * MIT License
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */
package org.apache.spark.sql.geosparksql.strategy.join

import com.vividsolutions.jts.geom.Geometry
import org.apache.spark.internal.Logging
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.expressions.{BindReferences, Expression, UnsafeRow}
import org.apache.spark.sql.catalyst.util.ArrayData
import org.apache.spark.sql.execution.{BinaryExecNode, SparkPlan}
import org.datasyslab.geospark.geometryObjects.Circle
import org.datasyslab.geospark.spatialRDD.SpatialRDD
import org.datasyslab.geosparksql.utils.GeometrySerializer

// ST_Distance（左，右）<= 半径
// 半径可以是文字或对“左”的计算
case class DistanceJoinExec(left: SparkPlan,
                            right: SparkPlan,
                            leftShape: Expression,
                            rightShape: Expression,
                            radius: Expression,
                            intersects: Boolean,
                            extraCondition: Option[Expression] = None)
    extends BinaryExecNode
    with TraitJoinQueryExec
    with Logging {

  private val boundRadius = BindReferences.bindReference(radius, left.output)
  override def toSpatialRddPair(
                              buildRdd: RDD[UnsafeRow],
                              buildExpr: Expression,
                              streamedRdd: RDD[UnsafeRow],
                              streamedExpr: Expression): (SpatialRDD[Geometry], SpatialRDD[Geometry]) =
    (toCircleRDD(buildRdd, buildExpr), toSpatialRdd(streamedRdd, streamedExpr))

  private def toCircleRDD(rdd: RDD[UnsafeRow], shapeExpression: Expression): SpatialRDD[Geometry] = {
    val spatialRdd = new SpatialRDD[Geometry]
    spatialRdd.setRawSpatialRDD(
      rdd
        .map { x =>
          {
            val shape = GeometrySerializer.deserialize(shapeExpression.eval(x).asInstanceOf[ArrayData])
            val circle = new Circle(shape, boundRadius.eval(x).asInstanceOf[Double])
            circle.setUserData(x.copy)
            circle.asInstanceOf[Geometry]
          }
        }
        .toJavaRDD())
    spatialRdd
  }

}

```
 
#### JoinQueryDetector.scala

 extends spark.sql.Strategy

```scala
/**
  * FILE: JoinQueryDetector
  * PATH: org.apache.spark.sql.geosparksql.strategy.join.JoinQueryDetector
  * Copyright (c) GeoSpark Development Team
  *
  * MIT License
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */
package org.apache.spark.sql.geosparksql.strategy.join

import org.apache.spark.sql.Strategy
import org.apache.spark.sql.catalyst.expressions.{Expression, LessThan, LessThanOrEqual}
import org.apache.spark.sql.catalyst.plans.Inner
import org.apache.spark.sql.catalyst.plans.logical.{Join, LogicalPlan}
import org.apache.spark.sql.execution.SparkPlan
import org.apache.spark.sql.geosparksql.expressions.{ST_Contains, ST_Distance, ST_Intersects, ST_Within}

/**
  * Plans `RangeJoinExec` for inner joins on spatial relationships ST_Contains(a, b)
  * and ST_Intersects(a, b).
  *
  * Plans `DistanceJoinExec` for inner joins on spatial relationship ST_Distance(a, b) < r.
  */
object JoinQueryDetector extends Strategy {

  /**
    * Returns true if specified expression has at least one reference and all its references
    * map to the output of the specified plan.
    */
  private def matches(expr: Expression, plan: LogicalPlan): Boolean =
    expr.references.find(plan.outputSet.contains(_)).isDefined &&
      expr.references.find(!plan.outputSet.contains(_)).isEmpty

  private def matchExpressionsToPlans(exprA: Expression,
                                      exprB: Expression,
                                      planA: LogicalPlan,
                                      planB: LogicalPlan): Option[(LogicalPlan, LogicalPlan)] =
    if (matches(exprA, planA) && matches(exprB, planB)) {
      Some((planA, planB))
    } else if (matches(exprA, planB) && matches(exprB, planA)) {
      Some((planB, planA))
    } else {
      None
    }


  def apply(plan: LogicalPlan): Seq[SparkPlan] = plan match {

    // ST_Contains(a, b) - a contains b
    case Join(left, right, Inner, Some(ST_Contains(Seq(leftShape, rightShape)))) =>
      planSpatialJoin(left, right, Seq(leftShape,rightShape), false)

    // ST_Intersects(a, b) - a intersects b
    case Join(left, right, Inner, Some(ST_Intersects(Seq(leftShape, rightShape)))) =>
      planSpatialJoin(left, right, Seq(leftShape,rightShape), true)

    // ST_WITHIN(a, b) - a is within b
    case Join(left, right, Inner, Some(ST_Within(Seq(leftShape, rightShape)))) =>
      planSpatialJoin(right, left, Seq(rightShape,leftShape), false)

    // ST_Distance(a, b) <= radius consider boundary intersection
    case Join(left, right, Inner, Some(LessThanOrEqual(ST_Distance(Seq(leftShape, rightShape)), radius))) =>
      planDistanceJoin(left, right, Seq(leftShape,rightShape), radius, true)

    // ST_Distance(a, b) < radius don't consider boundary intersection
    case Join(left, right, Inner, Some(LessThan(ST_Distance(Seq(leftShape, rightShape)), radius))) =>
      planDistanceJoin(left, right, Seq(leftShape,rightShape), radius, false)
    case _ =>
      Nil
  }

  private def planSpatialJoin(left: LogicalPlan,
                              right: LogicalPlan,
                              children: Seq[Expression],
                              intersects: Boolean,
                              extraCondition: Option[Expression] = None): Seq[SparkPlan] = {
    val a = children.head
    val b = children.tail.head

    val relationship = if (intersects) "ST_Intersects" else "ST_Contains";

    matchExpressionsToPlans(a, b, left, right) match {
      case Some((planA, planB)) =>
        logInfo(s"Planning spatial join for $relationship relationship")
        RangeJoinExec(planLater(planA), planLater(planB), a, b, intersects, extraCondition) :: Nil
      case None =>
        logInfo(
          s"Spatial join for $relationship with arguments not aligned " +
            "with join relations is not supported")
        Nil
    }
  }

  private def planDistanceJoin(left: LogicalPlan,
                               right: LogicalPlan,
                               children: Seq[Expression],
                               radius: Expression,
                               intersects: Boolean,
                               extraCondition: Option[Expression] = None): Seq[SparkPlan] = {
    val a = children.head
    val b = children.tail.head

    val relationship = if (intersects) "ST_Distance <=" else "ST_Distance <";

    matchExpressionsToPlans(a, b, left, right) match {
      case Some((planA, planB)) =>
        if (radius.references.isEmpty || matches(radius, planA)) {
          logInfo("Planning spatial distance join")
          DistanceJoinExec(planLater(planA), planLater(planB), a, b, radius, intersects, extraCondition) :: Nil
        } else if (matches(radius, planB)) {
          logInfo("Planning spatial distance join")
          DistanceJoinExec(planLater(planB), planLater(planA), b, a, radius, intersects, extraCondition) :: Nil
        } else {
          logInfo(
            "Spatial distance join for ST_Distance with non-scalar radius " +
              "that is not a computation over just one side of the join is not supported")
          Nil
        }
      case None =>
        logInfo(
          "Spatial distance join for ST_Distance with arguments not " +
            "aligned with join relations is not supported")
        Nil
    }
  }
}

```
 
#### RangeJoinExec.scala

extends spark.sql.execution.BinaryExecNode

```scala
/**
  * FILE: RangeJoinExec
  * PATH: org.apache.spark.sql.geosparksql.strategy.join.RangeJoinExec
  * Copyright (c) GeoSpark Development Team
  *
  * MIT License
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */
package org.apache.spark.sql.geosparksql.strategy.join

import org.apache.spark.internal.Logging
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.execution.{BinaryExecNode, SparkPlan}

/**
  *  ST_Contains(left, right) - left contains right
  *  or
  *  ST_Intersects(left, right) - left and right intersect
  *
  * @param left left side of the join
  * @param right right side of the join
  * @param leftShape expression for the first argument of ST_Contains or ST_Intersects
  * @param rightShape expression for the second argument of ST_Contains or ST_Intersects
  * @param intersects boolean indicating whether spatial relationship is 'intersects' (true)
  *                   or 'contains' (false)
  */
case class RangeJoinExec(left: SparkPlan,
                         right: SparkPlan,
                         leftShape: Expression,
                         rightShape: Expression,
                         intersects: Boolean,
                         extraCondition: Option[Expression] = None)
    extends BinaryExecNode
    with TraitJoinQueryExec
    with Logging {}

```
 
#### TraitJoinQueryExec.scala

```scala
/**
  * FILE: TraitJoinQueryExec
  * PATH: org.apache.spark.sql.geosparksql.strategy.join.TraitJoinQueryExec
  * Copyright (c) GeoSpark Development Team
  *
  * MIT License
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */
package org.apache.spark.sql.geosparksql.strategy.join

import com.vividsolutions.jts.geom.Geometry
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.codegen.GenerateUnsafeRowJoiner
import org.apache.spark.sql.catalyst.expressions.{Attribute, BindReferences, Expression, UnsafeRow}
import org.apache.spark.sql.catalyst.util.ArrayData
import org.apache.spark.sql.execution.SparkPlan
import org.datasyslab.geospark.enums.JoinSparitionDominantSide
import org.datasyslab.geospark.spatialOperator.JoinQuery
import org.datasyslab.geospark.spatialOperator.JoinQuery.JoinParams
import org.datasyslab.geospark.spatialRDD.SpatialRDD
import org.datasyslab.geospark.utils.GeoSparkConf
import org.datasyslab.geosparksql.utils.GeometrySerializer

trait TraitJoinQueryExec { self: SparkPlan =>

  val left: SparkPlan
  val right: SparkPlan
  val leftShape: Expression
  val rightShape: Expression
  val intersects: Boolean
  val extraCondition: Option[Expression]

  // Using lazy val to avoid serialization
  @transient private lazy val boundCondition: (InternalRow => Boolean) = {
    if (extraCondition.isDefined) {
      newPredicate(extraCondition.get, left.output ++ right.output).eval _
    } else { (r: InternalRow) =>
      true
    }
  }

  override def output: Seq[Attribute] = left.output ++ right.output

  override protected def doExecute(): RDD[InternalRow] = {
    val boundLeftShape = BindReferences.bindReference(leftShape, left.output)
    val boundRightShape = BindReferences.bindReference(rightShape, right.output)

    val leftResultsRaw = left.execute().asInstanceOf[RDD[UnsafeRow]]
    val rightResultsRaw = right.execute().asInstanceOf[RDD[UnsafeRow]]

    var geosparkConf = new GeoSparkConf(sparkContext.conf)

    logDebug("Number of partitions on the left: " + leftResultsRaw.partitions.size)
    logDebug("Number of partitions on the right: " + rightResultsRaw.partitions.size)

    val (leftShapes, rightShapes) =
      toSpatialRddPair(leftResultsRaw, boundLeftShape, rightResultsRaw, boundRightShape)

    // Only do SpatialRDD analyze when the user doesn't know approximate total count of the spatial partitioning
    // dominant side rdd
    if (geosparkConf.getJoinApproximateTotalCount == -1)
    {
      if (geosparkConf.getJoinSparitionDominantSide == JoinSparitionDominantSide.LEFT)
      {
        leftShapes.analyze()
        geosparkConf.setJoinApproximateTotalCount(leftShapes.approximateTotalCount)
        geosparkConf.setDatasetBoundary(leftShapes.boundaryEnvelope)
      }
      else
      {
        rightShapes.analyze()
        geosparkConf.setJoinApproximateTotalCount(rightShapes.approximateTotalCount)
        geosparkConf.setDatasetBoundary(rightShapes.boundaryEnvelope)
      }
    }
    logDebug(
        s"Found ${geosparkConf.getJoinApproximateTotalCount} objects")
    var numPartitions = -1
    try {
      if (geosparkConf.getJoinSparitionDominantSide == JoinSparitionDominantSide.LEFT) {
        if(geosparkConf.getFallbackPartitionNum != -1)
        {
          numPartitions = geosparkConf.getFallbackPartitionNum
        }
        else
        {
          numPartitions = leftShapes.rawSpatialRDD.partitions.size()
        }
        doSpatialPartitioning(leftShapes, rightShapes, numPartitions, geosparkConf)
      }
      else
      {
          if (geosparkConf.getFallbackPartitionNum != -1) {
            numPartitions = geosparkConf.getFallbackPartitionNum
          }
          else {
            numPartitions = rightShapes.rawSpatialRDD.partitions.size()
          }
          doSpatialPartitioning(rightShapes, leftShapes, numPartitions, geosparkConf)
      }
    }
    catch
    {
      case e: IllegalArgumentException => {
        // Partition number are not qualified
        // Use fallback num partitions specified in GeoSparkConf
        if (geosparkConf.getJoinSparitionDominantSide == JoinSparitionDominantSide.LEFT) {
          numPartitions = geosparkConf.getFallbackPartitionNum
          doSpatialPartitioning(leftShapes,rightShapes,numPartitions,geosparkConf)
        }
        else {
          numPartitions = geosparkConf.getFallbackPartitionNum
          doSpatialPartitioning(rightShapes,leftShapes,numPartitions,geosparkConf)
        }
      }
    }


    val joinParams = new JoinParams(intersects, geosparkConf.getIndexType, geosparkConf.getJoinBuildSide)

    //logInfo(s"leftShape count ${leftShapes.spatialPartitionedRDD.count()}")
    //logInfo(s"rightShape count ${rightShapes.spatialPartitionedRDD.count()}")

    val matches = JoinQuery.spatialJoin(leftShapes, rightShapes, joinParams)

    logDebug(s"Join result has ${matches.count()} rows")

      matches.rdd.mapPartitions { iter =>
        val filtered =
          if (extraCondition.isDefined) {
            val boundCondition = newPredicate(extraCondition.get, left.output ++ right.output)
            iter.filter {
              case (l, r) =>
                val leftRow = l.getUserData.asInstanceOf[UnsafeRow]
                val rightRow = r.getUserData.asInstanceOf[UnsafeRow]
                var joiner = GenerateUnsafeRowJoiner.create(left.schema, right.schema)
                boundCondition.eval(joiner.join(leftRow,rightRow))
            }
          } else {
            iter
          }

        filtered.map {
          case (l, r) =>
            val leftRow = l.getUserData.asInstanceOf[UnsafeRow]
            val rightRow = r.getUserData.asInstanceOf[UnsafeRow]
            var joiner = GenerateUnsafeRowJoiner.create(left.schema, right.schema)
          joiner.join(leftRow, rightRow)
        }
      }
  }

  protected def toSpatialRdd(rdd: RDD[UnsafeRow],
                             shapeExpression: Expression): SpatialRDD[Geometry] = {

    val spatialRdd = new SpatialRDD[Geometry]
    spatialRdd.setRawSpatialRDD(
      rdd
        .map { x =>
        {
          val shape = GeometrySerializer.deserialize(shapeExpression.eval(x).asInstanceOf[ArrayData])
          //logInfo(shape.toString)
          shape.setUserData(x.copy)
          shape
        }
        }
        .toJavaRDD())
    spatialRdd
  }

  def toSpatialRddPair(buildRdd: RDD[UnsafeRow],
                    buildExpr: Expression,
                    streamedRdd: RDD[UnsafeRow],
                    streamedExpr: Expression): (SpatialRDD[Geometry], SpatialRDD[Geometry]) =
    (toSpatialRdd(buildRdd, buildExpr), toSpatialRdd(streamedRdd, streamedExpr))

  def doSpatialPartitioning(dominantShapes:SpatialRDD[Geometry], followerShapes:SpatialRDD[Geometry],
                            numPartitions: Integer, geosparkConf: GeoSparkConf): Unit =
  {
    dominantShapes.spatialPartitioning(geosparkConf.getJoinGridType, numPartitions)
    followerShapes.spatialPartitioning(dominantShapes.getPartitioner)
  }
}

```


### SparkCity/sql/src/main/scala/org/apache/spark/sql/geosparksql/expressions/

#### AggregateFunctions.scala

extends spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}

```scala
/**
  * FILE: AggregateFunctions
  * PATH: org.apache.spark.sql.geosparksql.expressions.AggregateFunctions
  * Copyright (c) GeoSpark Development Team
  *
  * MIT License
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */
package org.apache.spark.sql.geosparksql.expressions

import com.vividsolutions.jts.geom.{Coordinate, Geometry, GeometryFactory, Polygon}
import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.geosparksql.UDT.GeometryUDT
import org.apache.spark.sql.types.{DataType, StructField, StructType}

/**
  * Return the polygon union of all Polygon in the given column
  */

class ST_Union_Aggr extends UserDefinedAggregateFunction {
  override def inputSchema: StructType =  StructType(StructField("Union", new GeometryUDT) :: Nil)

  override def bufferSchema: StructType = StructType(
    StructField("Union", new GeometryUDT) :: Nil
  )

  override def dataType: DataType = new GeometryUDT

  override def deterministic: Boolean = true

  override def initialize(buffer: MutableAggregationBuffer): Unit =
  {
    val coordinates:Array[Coordinate] = new Array[Coordinate](5)
    coordinates(0) = new Coordinate(-999999999,-999999999)
    coordinates(1) = new Coordinate(-999999999,-999999999)
    coordinates(2) = new Coordinate(-999999999,-999999999)
    coordinates(3) = new Coordinate(-999999999,-999999999)
    coordinates(4) = coordinates(0)
    val geometryFactory = new GeometryFactory()
    buffer(0) = geometryFactory.createPolygon(coordinates)
  }

  override def update(buffer: MutableAggregationBuffer, input: Row): Unit =
  {
    val accumulateUnion = buffer.getAs[Polygon](0)
    val newPolygon = input.getAs[Polygon](0)
    if (accumulateUnion.getArea ==0) buffer(0)=newPolygon
    else buffer(0) = accumulateUnion.union(newPolygon)
  }

  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit =
  {
    val leftPolygon = buffer1.getAs[Polygon](0)
    val rightPolygon = buffer2.getAs[Polygon](0)
    if (leftPolygon.getCoordinates()(0).x == -999999999) buffer1(0)=rightPolygon
    else if(rightPolygon.getCoordinates()(0).x == -999999999) buffer1(0)=leftPolygon
    else buffer1(0) = leftPolygon.union(rightPolygon)
  }

  override def evaluate(buffer: Row): Any =
  {
    return buffer.getAs[Geometry](0)
  }
}

/**
  * Return the envelope boundary of the entire column
  */
class ST_Envelope_Aggr extends UserDefinedAggregateFunction {
  // This is the input fields for your aggregate function.
  override def inputSchema: org.apache.spark.sql.types.StructType =
    StructType(StructField("Envelope", new GeometryUDT) :: Nil)

  // This is the internal fields you keep for computing your aggregate.
  override def bufferSchema: StructType = StructType(
      StructField("Envelope", new GeometryUDT) :: Nil
  )

  // This is the output type of your aggregatation function.
  override def dataType: DataType = new GeometryUDT

  override def deterministic: Boolean = true

  // This is the initial value for your buffer schema.
  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    val coordinates:Array[Coordinate] = new Array[Coordinate](5)
    coordinates(0) = new Coordinate(-999999999,-999999999)
    coordinates(1) = new Coordinate(-999999999,-999999999)
    coordinates(2) = new Coordinate(-999999999,-999999999)
    coordinates(3) = new Coordinate(-999999999,-999999999)
    coordinates(4) = new Coordinate(-999999999,-999999999)
    val geometryFactory = new GeometryFactory()
    buffer(0) = geometryFactory.createPolygon(coordinates)
    //buffer(0) = new GenericArrayData(GeometrySerializer.serialize(geometryFactory.createPolygon(coordinates)))
  }

  // This is how to update your buffer schema given an input.
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    val accumulateEnvelope = buffer.getAs[Geometry](0).getEnvelopeInternal
    val newEnvelope = input.getAs[Geometry](0).getEnvelopeInternal
    val coordinates:Array[Coordinate] = new Array[Coordinate](5)
    var minX = 0.0
    var minY = 0.0
    var maxX = 0.0
    var maxY = 0.0
    if (accumulateEnvelope.getMinX == -999999999)
    {
      // Found the accumulateEnvelope is the initial value
      minX = newEnvelope.getMinX
      minY = newEnvelope.getMinY
      maxX = newEnvelope.getMaxX
      maxY = newEnvelope.getMaxY
    }
    else
    {
      minX = Math.min(accumulateEnvelope.getMinX, newEnvelope.getMinX)
      minY = Math.min(accumulateEnvelope.getMinY, newEnvelope.getMinY)
      maxX = Math.max(accumulateEnvelope.getMaxX, newEnvelope.getMaxX)
      maxY = Math.max(accumulateEnvelope.getMaxY, newEnvelope.getMaxY)
    }
    coordinates(0) = new Coordinate(minX,minY)
    coordinates(1) = new Coordinate(minX,maxY)
    coordinates(2) = new Coordinate(maxX,maxY)
    coordinates(3) = new Coordinate(maxX,minY)
    coordinates(4) = coordinates(0)
    val geometryFactory = new GeometryFactory()
    buffer(0) = geometryFactory.createPolygon(coordinates)
  }

  // This is how to merge two objects with the bufferSchema type.
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    val leftEnvelope = buffer1.getAs[Geometry](0).getEnvelopeInternal
    val rightEnvelope = buffer2.getAs[Geometry](0).getEnvelopeInternal
    val coordinates:Array[Coordinate] = new Array[Coordinate](5)
    var minX = 0.0
    var minY = 0.0
    var maxX = 0.0
    var maxY = 0.0
    if (leftEnvelope.getMinX == -999999999)
    {
      // Found the leftEnvelope is the initial value
      minX = rightEnvelope.getMinX
      minY = rightEnvelope.getMinY
      maxX = rightEnvelope.getMaxX
      maxY = rightEnvelope.getMaxY
    }
    else if (rightEnvelope.getMinX == -999999999)
    {
      // Found the rightEnvelope is the initial value
      minX = leftEnvelope.getMinX
      minY = leftEnvelope.getMinY
      maxX = leftEnvelope.getMaxX
      maxY = leftEnvelope.getMaxY
    }
    else
    {
      minX = Math.min(leftEnvelope.getMinX, rightEnvelope.getMinX)
      minY = Math.min(leftEnvelope.getMinY, rightEnvelope.getMinY)
      maxX = Math.max(leftEnvelope.getMaxX, rightEnvelope.getMaxX)
      maxY = Math.max(leftEnvelope.getMaxY, rightEnvelope.getMaxY)
    }
    coordinates(0) = new Coordinate(minX,minY)
    coordinates(1) = new Coordinate(minX,maxY)
    coordinates(2) = new Coordinate(maxX,maxY)
    coordinates(3) = new Coordinate(maxX,minY)
    coordinates(4) = coordinates(0)
    val geometryFactory = new GeometryFactory()
    buffer1(0) = geometryFactory.createPolygon(coordinates)
  }

  // This is where you output the final value, given the final value of your bufferSchema.
  override def evaluate(buffer: Row): Any = {
    return buffer.getAs[Geometry](0)
  }
}

```
 
#### Constructors.scala

extends spark.sql.catalyst.expressions.Expression

```scala
/**
  * FILE: Constructors
  * PATH: org.apache.spark.sql.geosparksql.expressions.Constructors
  * Copyright (c) GeoSpark Development Team
  *
  * MIT License
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */
package org.apache.spark.sql.geosparksql.expressions

import com.vividsolutions.jts.geom.{Coordinate, GeometryFactory}
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.catalyst.expressions.codegen.CodegenFallback
import org.apache.spark.sql.catalyst.util.{ArrayData, GenericArrayData}
import org.apache.spark.sql.geosparksql.UDT.GeometryUDT
import org.apache.spark.sql.types.{DataType, Decimal}
import org.apache.spark.unsafe.types.UTF8String
import org.datasyslab.geospark.enums.{FileDataSplitter, GeometryType}
import org.datasyslab.geospark.formatMapper.FormatMapper
import org.datasyslab.geospark.geometryObjects.Circle
import org.datasyslab.geosparksql.utils.GeometrySerializer

/**
  * Return a point from a string. The string must be plain string and each coordinate must be separated by a delimiter.
  * @param inputExpressions This function takes 2 parameters. The first parameter is the input geometry
  *                         string, the second parameter is the delimiter. String format should be similar to CSV/TSV
  */
case class ST_PointFromText(inputExpressions: Seq[Expression])
  extends Expression with CodegenFallback with UserDataGeneratator{
  override def nullable: Boolean = false

  override def eval(inputRow: InternalRow): Any = {
    // This is an expression which takes two input expressions.
    val minInputLength = 2
    assert(inputExpressions.length>=minInputLength)
    val geomString = inputExpressions(0).eval(inputRow).asInstanceOf[UTF8String].toString
    val geomFormat = inputExpressions(1).eval(inputRow).asInstanceOf[UTF8String].toString

    var fileDataSplitter = FileDataSplitter.getFileDataSplitter(geomFormat)
    var formatMapper = new FormatMapper(fileDataSplitter, false, GeometryType.POINT)
    var geometry = formatMapper.readGeometry(geomString)
    // If the user specify a bunch of attributes to go with each geometry, we need to store all of them in this geometry
    if(inputExpressions.length>minInputLength)
    {
      geometry.setUserData(generateUserData(minInputLength,inputExpressions, inputRow))
    }

    return new GenericArrayData(GeometrySerializer.serialize(geometry))
  }

  override def dataType:DataType = new GeometryUDT()

  override def children: Seq[Expression] = inputExpressions
}

/**
  * Return a polygon from a string. The string must be plain string and each coordinate must be separated by a delimiter.
  * @param inputExpressions
  */
case class ST_PolygonFromText(inputExpressions: Seq[Expression])
  extends Expression with CodegenFallback with UserDataGeneratator{
  override def nullable: Boolean = false

  override def eval(inputRow: InternalRow): Any = {
    // This is an expression which takes two input expressions.
    val minInputLength = 2
    assert(inputExpressions.length>=minInputLength)
    val geomString = inputExpressions(0).eval(inputRow).asInstanceOf[UTF8String].toString
    val geomFormat = inputExpressions(1).eval(inputRow).asInstanceOf[UTF8String].toString

    var fileDataSplitter = FileDataSplitter.getFileDataSplitter(geomFormat)
    var formatMapper = new FormatMapper(fileDataSplitter, false, GeometryType.POLYGON)
    var geometry = formatMapper.readGeometry(geomString)
    // If the user specify a bunch of attributes to go with each geometry, we need to store all of them in this geometry
    if(inputExpressions.length>minInputLength)
    {
      geometry.setUserData(generateUserData(minInputLength,inputExpressions, inputRow))
    }

    return new GenericArrayData(GeometrySerializer.serialize(geometry))
  }

  override def dataType:DataType = new GeometryUDT()

  override def children: Seq[Expression] = inputExpressions
}

/**
  * Return a linestring from a string. The string must be plain string and each coordinate must be separated by a delimiter.
  * @param inputExpressions
  */
case class ST_LineStringFromText(inputExpressions: Seq[Expression])
  extends Expression with CodegenFallback with UserDataGeneratator{
  override def nullable: Boolean = false

  override def eval(inputRow: InternalRow): Any = {
    // This is an expression which takes two input expressions.
    val minInputLength = 2
    assert(inputExpressions.length>=minInputLength)
    val geomString = inputExpressions(0).eval(inputRow).asInstanceOf[UTF8String].toString
    val geomFormat = inputExpressions(1).eval(inputRow).asInstanceOf[UTF8String].toString

    var fileDataSplitter = FileDataSplitter.getFileDataSplitter(geomFormat)
    var formatMapper = new FormatMapper(fileDataSplitter, false, GeometryType.LINESTRING)
    var geometry = formatMapper.readGeometry(geomString)
    // If the user specify a bunch of attributes to go with each geometry, we need to store all of them in this geometry
    if(inputExpressions.length>minInputLength)
    {
      geometry.setUserData(generateUserData(minInputLength,inputExpressions, inputRow))
    }

    return new GenericArrayData(GeometrySerializer.serialize(geometry))
  }

  override def dataType:DataType = new GeometryUDT()

  override def children: Seq[Expression] = inputExpressions
}


/**
  * Return a Geometry from a WKT string
  * @param inputExpressions This function takes 1 parameter which is the geometry string. The string format must be WKT.
  */
case class ST_GeomFromWKT(inputExpressions: Seq[Expression])
  extends Expression with CodegenFallback with UserDataGeneratator {
  override def nullable: Boolean = false

  override def eval(inputRow: InternalRow): Any = {
    // This is an expression which takes one input expressions
    val minInputLength = 1
    assert(inputExpressions.length>=minInputLength)

    val geomString = inputExpressions(0).eval(inputRow).asInstanceOf[UTF8String].toString

    var fileDataSplitter = FileDataSplitter.WKT
    var formatMapper = new FormatMapper(fileDataSplitter, false)
    var geometry = formatMapper.readGeometry(geomString)
    // If the user specify a bunch of attributes to go with each geometry, we need to store all of them in this geometry
    if(inputExpressions.length>minInputLength)
    {
      geometry.setUserData(generateUserData(minInputLength,inputExpressions, inputRow))
    }
    return new GenericArrayData(GeometrySerializer.serialize(geometry))
  }

  override def dataType:DataType = new GeometryUDT()

  override def children: Seq[Expression] = inputExpressions
}

  /**
    * Return a Geometry from a GeoJSON string
    * @param inputExpressions This function takes 1 parameter which is the geometry string. The string format must be GeoJson.
    */
  case class ST_GeomFromGeoJSON(inputExpressions: Seq[Expression])
    extends Expression with CodegenFallback with UserDataGeneratator {
    override def nullable: Boolean = false

    override def eval(inputRow: InternalRow): Any = {
      // This is an expression which takes one input expressions
      val minInputLength = 1
      assert(inputExpressions.length>=minInputLength)

      val geomString = inputExpressions(0).eval(inputRow).asInstanceOf[UTF8String].toString

      var fileDataSplitter = FileDataSplitter.GEOJSON
      var formatMapper = new FormatMapper(fileDataSplitter, false)
      var geometry = formatMapper.readGeometry(geomString)
      // If the user specify a bunch of attributes to go with each geometry, we need to store all of them in this geometry
      if(inputExpressions.length>1)
      {
        geometry.setUserData(generateUserData(minInputLength,inputExpressions, inputRow))
      }
      return new GenericArrayData(GeometrySerializer.serialize(geometry))
    }

  override def dataType:DataType = new GeometryUDT()

  override def children: Seq[Expression] = inputExpressions
}

/**
  * Return a Point from X and Y
  * @param inputExpressions This function takes 2 parameter which are point x and y.
  */
case class ST_Point(inputExpressions: Seq[Expression])
  extends Expression with CodegenFallback with UserDataGeneratator {
  override def nullable: Boolean = false

  override def eval(inputRow: InternalRow): Any = {
    val minInputLength = 2
    assert(inputExpressions.length>=minInputLength)

    val x = inputExpressions(0).eval(inputRow).asInstanceOf[Decimal].toDouble
    val y = inputExpressions(1).eval(inputRow).asInstanceOf[Decimal].toDouble
    var geometryFactory = new GeometryFactory()
    var geometry = geometryFactory.createPoint(new Coordinate(x,y))
    // If the user specify a bunch of attributes to go with each geometry, we need to store all of them in this geometry
    if(inputExpressions.length>minInputLength)
    {
      geometry.setUserData(generateUserData(minInputLength,inputExpressions, inputRow))
    }
    return new GenericArrayData(GeometrySerializer.serialize(geometry))
  }

  override def dataType: DataType = new GeometryUDT()

  override def children: Seq[Expression] = inputExpressions
}


/**
  * Return a Circle from a Geometry and a radius
  * @param inputExpressions This function takes two parameters, a geometry column and a radius, and outputs a circle type
  */
case class ST_Circle(inputExpressions: Seq[Expression])
  extends Expression with CodegenFallback with UserDataGeneratator {
  override def nullable: Boolean = false

  override def eval(inputRow: InternalRow): Any = {
    assert(inputExpressions.length==2)
    val geometry = GeometrySerializer.deserialize(inputExpressions(0).eval(inputRow).asInstanceOf[ArrayData])
    val circle = new Circle(geometry, inputExpressions(1).eval(inputRow).asInstanceOf[Decimal].toDouble)
    return new GenericArrayData(GeometrySerializer.serialize(circle))
  }

  override def dataType: DataType = new GeometryUDT()

  override def children: Seq[Expression] = inputExpressions
}

/**
  * Return a polygon given minX,minY,maxX,maxY
  * @param inputExpressions
  */
case class ST_PolygonFromEnvelope(inputExpressions: Seq[Expression]) extends Expression with CodegenFallback with UserDataGeneratator {
  override def nullable: Boolean = false

  override def eval(input: InternalRow): Any =
  {
    val minInputLength = 4
    assert(inputExpressions.length>=minInputLength)
    val minX = inputExpressions(0).eval(input).asInstanceOf[Decimal].toDouble
    val minY = inputExpressions(1).eval(input).asInstanceOf[Decimal].toDouble
    val maxX = inputExpressions(2).eval(input).asInstanceOf[Decimal].toDouble
    val maxY = inputExpressions(3).eval(input).asInstanceOf[Decimal].toDouble
    var coordinates = new Array[Coordinate](5)
    coordinates(0) = new Coordinate(minX,minY)
    coordinates(1) = new Coordinate(minX,maxY)
    coordinates(2) = new Coordinate(maxX,maxY)
    coordinates(3) = new Coordinate(maxX,minY)
    coordinates(4) = coordinates(0)
    val geometryFactory = new GeometryFactory()
    val polygon = geometryFactory.createPolygon(coordinates)
    if(inputExpressions.length>minInputLength)
    {
      polygon.setUserData(generateUserData(minInputLength,inputExpressions,input))
    }
    new GenericArrayData(GeometrySerializer.serialize(polygon))
  }

  override def dataType: DataType = new GeometryUDT()

  override def children: Seq[Expression] = inputExpressions
}

trait UserDataGeneratator
{
  def generateUserData(minInputLength: Integer, inputExpressions: Seq[Expression], inputRow: InternalRow): String =
  {
    var userData = inputExpressions(minInputLength).eval(inputRow).asInstanceOf[UTF8String].toString

    for (i <- minInputLength+1 to inputExpressions.length-1)
    {
      userData = userData+"\t"+inputExpressions(i).eval(inputRow).asInstanceOf[UTF8String].toString
    }
    return userData
  }
}


```
 
#### Functions.scala

```scala
/**
  * FILE: Functions
  * PATH: org.apache.spark.sql.geosparksql.expressions.Functions
  * Copyright (c) GeoSpark Development Team
  *
  * MIT License
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */
package org.apache.spark.sql.geosparksql.expressions

import com.vividsolutions.jts.geom.Polygon
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.catalyst.expressions.codegen.CodegenFallback
import org.apache.spark.sql.catalyst.util.{ArrayData, GenericArrayData}
import org.apache.spark.sql.geosparksql.UDT.GeometryUDT
import org.apache.spark.sql.types.{DataType, DoubleType}
import org.apache.spark.unsafe.types.UTF8String
import org.datasyslab.geosparksql.utils.GeometrySerializer
import org.geotools.geometry.jts.JTS
import org.geotools.referencing.CRS

/**
  * Return the distance between two geometries.
  * @param inputExpressions This function takes two geometries and calculates the distance between two objects.
  */
case class ST_Distance(inputExpressions: Seq[Expression])
  extends Expression with CodegenFallback{

  // This is a binary expression
  assert(inputExpressions.length == 2)

  override def nullable: Boolean = false

  override def toString: String = s" **${ST_Distance.getClass.getName}**  "

  override def children: Seq[Expression] = inputExpressions

  override def eval(inputRow: InternalRow): Any = {
    assert(inputExpressions.length==2)

    val leftArray = inputExpressions(0).eval(inputRow).asInstanceOf[ArrayData]
    val rightArray = inputExpressions(1).eval(inputRow).asInstanceOf[ArrayData]

    val leftGeometry = GeometrySerializer.deserialize(leftArray)

    val rightGeometry = GeometrySerializer.deserialize(rightArray)

    return leftGeometry.distance(rightGeometry)
  }
  override def dataType = DoubleType
}

/**
  * Return the convex hull of a Geometry.
  * @param inputExpressions
  */
case class ST_ConvexHull(inputExpressions: Seq[Expression])
  extends Expression with CodegenFallback
{
  override def nullable: Boolean = false

  override def eval(input: InternalRow): Any =
  {
    assert(inputExpressions.length==1)
    val geometry = GeometrySerializer.deserialize(inputExpressions(0).eval(input).asInstanceOf[ArrayData]).asInstanceOf[Polygon]
    new GenericArrayData(GeometrySerializer.serialize(geometry.convexHull()))
  }

  override def dataType: DataType = new GeometryUDT()

  override def children: Seq[Expression] = inputExpressions
}

/**
  * Return the bounding rectangle for a Geometry
  * @param inputExpressions
  */
case class ST_Envelope(inputExpressions: Seq[Expression])
  extends Expression with CodegenFallback
{
  override def nullable: Boolean = false

  override def eval(input: InternalRow): Any =
  {
    assert(inputExpressions.length==1)
    val geometry = GeometrySerializer.deserialize(inputExpressions(0).eval(input).asInstanceOf[ArrayData])
    new GenericArrayData(GeometrySerializer.serialize(geometry.getEnvelope()))
  }

  override def dataType: DataType = new GeometryUDT()

  override def children: Seq[Expression] = inputExpressions
}

/**
  * Return the length measurement of a Geometry
  * @param inputExpressions
  */
case class ST_Length(inputExpressions: Seq[Expression])
  extends Expression with CodegenFallback
{
  override def nullable: Boolean = false

  override def eval(input: InternalRow): Any =
  {
    assert(inputExpressions.length==1)
    val geometry = GeometrySerializer.deserialize(inputExpressions(0).eval(input).asInstanceOf[ArrayData])
    return geometry.getLength
  }

  override def dataType: DataType = DoubleType

  override def children: Seq[Expression] = inputExpressions
}

/**
  * Return the area measurement of a Geometry.
  * @param inputExpressions
  */
case class ST_Area(inputExpressions: Seq[Expression])
  extends Expression with CodegenFallback
{
  override def nullable: Boolean = false

  override def eval(input: InternalRow): Any =
  {
    assert(inputExpressions.length==1)
    val geometry = GeometrySerializer.deserialize(inputExpressions(0).eval(input).asInstanceOf[ArrayData])
    return geometry.getArea
  }

  override def dataType: DataType = DoubleType

  override def children: Seq[Expression] = inputExpressions
}

/**
  * Return mathematical centroid of a geometry.
  * @param inputExpressions
  */
case class ST_Centroid(inputExpressions: Seq[Expression])
  extends Expression with CodegenFallback
{
  override def nullable: Boolean = false

  override def eval(input: InternalRow): Any =
  {
    assert(inputExpressions.length==1)
    val geometry = GeometrySerializer.deserialize(inputExpressions(0).eval(input).asInstanceOf[ArrayData])
    new GenericArrayData(GeometrySerializer.serialize(geometry.getCentroid()))
}

override def dataType: DataType = new GeometryUDT()

  override def children: Seq[Expression] = inputExpressions
}

/**
  * Given a geometry, sourceEPSGcode, and targetEPSGcode, convert the geometry's Spatial Reference System / Coordinate Reference System.
  * @param inputExpressions
  */
case class ST_Transform(inputExpressions: Seq[Expression])
  extends Expression with CodegenFallback
{
  override def nullable: Boolean = false

  override def eval(input: InternalRow): Any =
  {
    assert(inputExpressions.length==3||inputExpressions.length==4)
    System.setProperty("org.geotools.referencing.forceXY", "true")
    if (inputExpressions.length==4)
    {
      System.setProperty("org.geotools.referencing.forceXY", inputExpressions(3).eval(input).asInstanceOf[Boolean].toString)
    }
    val originalGeometry = GeometrySerializer.deserialize(inputExpressions(0).eval(input).asInstanceOf[ArrayData])
    val sourceCRScode = CRS.decode(inputExpressions(1).eval(input).asInstanceOf[UTF8String].toString)
    val targetCRScode = CRS.decode(inputExpressions(2).eval(input).asInstanceOf[UTF8String].toString)
    val transform = CRS.findMathTransform(sourceCRScode, targetCRScode, false)
    new GenericArrayData(GeometrySerializer.serialize(JTS.transform(originalGeometry, transform)))

  }

  override def dataType: DataType = new GeometryUDT()

  override def children: Seq[Expression] = inputExpressions
}
```
 
#### Predicates.scala

```scala
/**
  * FILE: Predicates
  * PATH: org.apache.spark.sql.geosparksql.expressions.Predicates
  * Copyright (c) GeoSpark Development Team
  *
  * MIT License
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */
package org.apache.spark.sql.geosparksql.expressions
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.catalyst.expressions.codegen.CodegenFallback
import org.apache.spark.sql.catalyst.util.ArrayData
import org.apache.spark.sql.types.BooleanType
import org.datasyslab.geosparksql.utils.GeometrySerializer

/**
  * Test if leftGeometry full contains rightGeometry
  * @param inputExpressions
  */
case class ST_Contains(inputExpressions: Seq[Expression])
  extends Expression with CodegenFallback{

  // This is a binary expression
  assert(inputExpressions.length == 2)

  override def nullable: Boolean = false

  override def toString: String = s" **${ST_Contains.getClass.getName}**  "

  override def children: Seq[Expression] = inputExpressions

  override def eval(inputRow: InternalRow): Any = {
    val leftArray = inputExpressions(0).eval(inputRow).asInstanceOf[ArrayData]
    val rightArray = inputExpressions(1).eval(inputRow).asInstanceOf[ArrayData]

    val leftGeometry = GeometrySerializer.deserialize(leftArray)

    val rightGeometry = GeometrySerializer.deserialize(rightArray)

    return leftGeometry.covers(rightGeometry)
  }
  override def dataType = BooleanType
}

/**
  * Test if leftGeometry full intersects rightGeometry
  * @param inputExpressions
  */
case class ST_Intersects(inputExpressions: Seq[Expression])
  extends Expression with CodegenFallback{
  override def nullable: Boolean = false

  // This is a binary expression
  assert(inputExpressions.length == 2)

  override def toString: String = s" **${ST_Intersects.getClass.getName}**  "

  override def children: Seq[Expression] = inputExpressions

  override def eval(inputRow: InternalRow): Any = {
    val leftArray = inputExpressions(0).eval(inputRow).asInstanceOf[ArrayData]
    val rightArray = inputExpressions(1).eval(inputRow).asInstanceOf[ArrayData]

    val leftGeometry = GeometrySerializer.deserialize(leftArray)

    val rightGeometry = GeometrySerializer.deserialize(rightArray)

    return leftGeometry.intersects(rightGeometry)
  }
  override def dataType = BooleanType
}

/**
  * Test if leftGeometry is full within rightGeometry
  * @param inputExpressions
  */
case class ST_Within(inputExpressions: Seq[Expression])
  extends Expression with CodegenFallback{
  override def nullable: Boolean = false

  // This is a binary expression
  assert(inputExpressions.length == 2)

  override def toString: String = s" **${ST_Intersects.getClass.getName}**  "

  override def children: Seq[Expression] = inputExpressions

  override def eval(inputRow: InternalRow): Any = {
    val leftArray = inputExpressions(0).eval(inputRow).asInstanceOf[ArrayData]
    val rightArray = inputExpressions(1).eval(inputRow).asInstanceOf[ArrayData]

    val leftGeometry = GeometrySerializer.deserialize(leftArray)

    val rightGeometry = GeometrySerializer.deserialize(rightArray)

    return leftGeometry.coveredBy(rightGeometry)
  }
  override def dataType = BooleanType
}
```


### SparkCity/sql/src/main/scala/org/apache/spark/sql/geosparksql/UDT/

#### GeometryUDT.scala

```scala
/**
  * FILE: GeometryUDT
  * PATH: org.apache.spark.sql.geosparksql.UDT.GeometryUDT
  * Copyright (c) GeoSpark Development Team
  *
  * MIT License
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */
package org.apache.spark.sql.geosparksql.UDT

import com.vividsolutions.jts.geom.Geometry
import org.apache.spark.sql.catalyst.util.{ArrayData, GenericArrayData}
import org.apache.spark.sql.types._
import org.datasyslab.geosparksql.utils.GeometrySerializer


private[sql] class GeometryUDT extends UserDefinedType[Geometry]{
  override def sqlType: DataType = ArrayType(ByteType, containsNull = false)

  override def userClass: Class[Geometry] = classOf[Geometry]

  override def serialize(obj: Geometry): GenericArrayData =
  {
    new GenericArrayData(GeometrySerializer.serialize(obj))
  }

  override def deserialize(datum: Any): Geometry =
  {
    datum match
    {
      case values: ArrayData => {
        return GeometrySerializer.deserialize(values)
      }
    }
  }
  case object GeometryUDT extends GeometryUDT
}

```
 
#### IndexUDT.scala

```scala
/**
  * FILE: IndexUDT
  * PATH: org.apache.spark.sql.geosparksql.UDT.IndexUDT
  * Copyright (c) GeoSpark Development Team
  *
  * MIT License
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */
package org.apache.spark.sql.geosparksql.UDT

import com.vividsolutions.jts.index.SpatialIndex
import org.apache.spark.sql.catalyst.util.{ArrayData, GenericArrayData}
import org.apache.spark.sql.types.{ArrayType, ByteType, DataType, UserDefinedType}
import org.datasyslab.geosparksql.utils.IndexSerializer

class IndexUDT extends UserDefinedType[SpatialIndex]{
  override def sqlType: DataType = ArrayType(ByteType, containsNull = false)

  override def serialize(obj: SpatialIndex): GenericArrayData =
  {
    return new GenericArrayData(IndexSerializer.serialize(obj))
  }

  override def deserialize(datum: Any): SpatialIndex =
  {
    datum match
    {
      case values: ArrayData => {
        return IndexSerializer.deserialize(values)
      }
    }
  }
  override def userClass: Class[SpatialIndex] = classOf[SpatialIndex]
}

```
 
#### UdtRegistratorWrapper.scala

```scala
/**
  * FILE: UdtRegistratorWrapper
  * PATH: org.apache.spark.sql.geosparksql.UDT.UdtRegistratorWrapper
  * Copyright (c) GeoSpark Development Team
  *
  * MIT License
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */
package org.apache.spark.sql.geosparksql.UDT

import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.index.SpatialIndex
import org.apache.spark.sql.types.UDTRegistration

object UdtRegistratorWrapper {
  def registerAll(): Unit =
  {
    UDTRegistration.register(classOf[Geometry].getName, classOf[GeometryUDT].getName)
    UDTRegistration.register(classOf[SpatialIndex].getName, classOf[IndexUDT].getName)
  }
}

```



### SparkCity/sql/src/main/scala/org/datasyslab/geosparksql/UDF/

#### UdfRegistrator.scala

```scala
/**
  * FILE: UdfRegistrator
  * PATH: org.datasyslab.geosparksql.UDF.UdfRegistrator
  * Copyright (c) GeoSpark Development Team
  *
  * MIT License
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */
package org.datasyslab.geosparksql.UDF

import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.sql.geosparksql.expressions._

object UdfRegistrator {
  def resigterConstructors(sparkSession: SparkSession): Unit =
  {
    sparkSession.sessionState.functionRegistry.registerFunction("ST_PointFromText", ST_PointFromText)
    sparkSession.sessionState.functionRegistry.registerFunction("ST_PolygonFromText", ST_PolygonFromText)
    sparkSession.sessionState.functionRegistry.registerFunction("ST_LineStringFromText", ST_LineStringFromText)
    sparkSession.sessionState.functionRegistry.registerFunction("ST_GeomFromWKT", ST_GeomFromWKT)
    sparkSession.sessionState.functionRegistry.registerFunction("ST_GeomFromGeoJSON", ST_GeomFromGeoJSON)
    sparkSession.sessionState.functionRegistry.registerFunction("ST_Circle", ST_Circle)
    sparkSession.sessionState.functionRegistry.registerFunction("ST_Point", ST_Point)
    sparkSession.sessionState.functionRegistry.registerFunction("ST_PolygonFromEnvelope", ST_PolygonFromEnvelope)
  }

  def registerPredicates(sparkSession: SparkSession): Unit =
  {
    sparkSession.sessionState.functionRegistry.registerFunction("ST_Contains", ST_Contains)
    sparkSession.sessionState.functionRegistry.registerFunction("ST_Intersects", ST_Intersects)
    sparkSession.sessionState.functionRegistry.registerFunction("ST_Within", ST_Within)
  }

  def registerFunctions(sparkSession: SparkSession): Unit =
  {
    sparkSession.sessionState.functionRegistry.registerFunction("ST_Distance", ST_Distance)
    sparkSession.sessionState.functionRegistry.registerFunction("ST_ConvexHull", ST_ConvexHull)
    sparkSession.sessionState.functionRegistry.registerFunction("ST_Envelope", ST_Envelope)
    sparkSession.sessionState.functionRegistry.registerFunction("ST_Length", ST_Length)
    sparkSession.sessionState.functionRegistry.registerFunction("ST_Area", ST_Area)
    sparkSession.sessionState.functionRegistry.registerFunction("ST_Centroid", ST_Centroid)
    sparkSession.sessionState.functionRegistry.registerFunction("ST_Transform", ST_Transform)
  }

  def registerAggregateFunctions(sparkSession: SparkSession): Unit =
  {
    sparkSession.udf.register("ST_Envelope_Aggr", new ST_Envelope_Aggr)
    sparkSession.udf.register("ST_Union_Aggr", new ST_Union_Aggr)
  }

  def dropConstructors(sparkSession: SparkSession): Unit =
  {
    sparkSession.sessionState.functionRegistry.dropFunction("ST_PointFromText")
    sparkSession.sessionState.functionRegistry.dropFunction("ST_PolygonFromText")
    sparkSession.sessionState.functionRegistry.dropFunction("ST_LineStringFromText")
    sparkSession.sessionState.functionRegistry.dropFunction("ST_GeomFromWKT")
    sparkSession.sessionState.functionRegistry.dropFunction("ST_GeomFromGeoJSON")
    sparkSession.sessionState.functionRegistry.dropFunction("ST_Circle")
    sparkSession.sessionState.functionRegistry.dropFunction("ST_Point")
    sparkSession.sessionState.functionRegistry.dropFunction("ST_PolygonFromEnvelope")
  }

  def dropPredicates(sparkSession: SparkSession): Unit =
  {
    sparkSession.sessionState.functionRegistry.dropFunction("ST_Contains")
    sparkSession.sessionState.functionRegistry.dropFunction("ST_Intersects")
    sparkSession.sessionState.functionRegistry.dropFunction("ST_Within")
  }

  def dropFunctions(sparkSession: SparkSession): Unit =
  {
    sparkSession.sessionState.functionRegistry.dropFunction("ST_Distance")
    sparkSession.sessionState.functionRegistry.dropFunction("ST_ConvexHull")
    sparkSession.sessionState.functionRegistry.dropFunction("ST_Envelope")
    sparkSession.sessionState.functionRegistry.dropFunction("ST_Length")
    sparkSession.sessionState.functionRegistry.dropFunction("ST_Area")
    sparkSession.sessionState.functionRegistry.dropFunction("ST_Centroid")
    sparkSession.sessionState.functionRegistry.dropFunction("ST_Transform")
  }

  def dropAggregateFunctions(sparkSession: SparkSession): Unit =
  {
    sparkSession.sessionState.functionRegistry.dropFunction("ST_Envelope_Aggr")
    sparkSession.sessionState.functionRegistry.dropFunction("ST_Union_Aggr")
  }

  def registerAll(sqlContext: SQLContext): Unit =
  {
    resigterConstructors(sqlContext.sparkSession)
    registerPredicates(sqlContext.sparkSession)
    registerFunctions(sqlContext.sparkSession)
    registerAggregateFunctions(sqlContext.sparkSession)
  }

  def registerAll(sparkSession: SparkSession): Unit =
  {
    resigterConstructors(sparkSession)
    registerPredicates(sparkSession)
    registerFunctions(sparkSession)
    registerAggregateFunctions(sparkSession)
  }

  def dropAll(sparkSession: SparkSession): Unit =
  {
    dropConstructors(sparkSession)
    dropPredicates(sparkSession)
    dropFunctions(sparkSession)
    dropAggregateFunctions(sparkSession)
  }
}

```


### SparkCity/sql/src/main/scala/org/datasyslab/geosparksql/UDT/

#### UdtRegistrator.scala

```scala
/**
  * FILE: UdtRegistrator
  * PATH: org.datasyslab.geosparksql.UDT.UdtRegistrator
  * Copyright (c) GeoSpark Development Team
  *
  * MIT License
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */
package org.datasyslab.geosparksql.UDT

import org.apache.spark.sql.geosparksql.UDT.UdtRegistratorWrapper

object UdtRegistrator {

  def registerAll(): Unit =
  {
    UdtRegistratorWrapper.registerAll()
  }

}

```


### SparkCity/sql/src/main/scala/org/datasyslab/geosparksql/utils/
 
#### Adapter.scala

```scala
/**
  * 文件: Adapter
  * 路径: org.datasyslab.geosparksql.utils.Adapter
  * Copyright (c) GeoSpark Development Team
  *
  * MIT License
  *
  * 特此向任何获得副本的人免费授予许可
  * 本软件及相关文档文件（“软件”），以处理
  * 在软件中不受限制，包括但不限于权利
  * 使用、复制、修改、合并、发布、分发、再许可和/或出售
  * 软件的副本，并允许软件的使用人
  * 提供这样做，受以下条件的约束：
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */
package org.datasyslab.geosparksql.utils


import com.vividsolutions.jts.geom.Geometry
import org.apache.spark.api.java.{JavaPairRDD, JavaRDD}
import org.apache.spark.rdd.RDD
//import org.apache.spark.sql.geosparksql.GeometryWrapper
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.datasyslab.geospark.spatialRDD.SpatialRDD

object Adapter {

  def toJavaRdd(dataFrame: DataFrame): JavaRDD[Geometry] =
  {
    return toRdd(dataFrame).toJavaRDD()
  }

  def toRdd(dataFrame: DataFrame): RDD[Geometry] =
  {
    return dataFrame.rdd.map[Geometry](f => f.get(0).asInstanceOf[Geometry])
  }

  def toDf(spatialRDD: SpatialRDD[Geometry], sparkSession: SparkSession): DataFrame =
  {
    val rowRdd = spatialRDD.rawSpatialRDD.rdd.map[Row](f => Row.fromSeq(f.toString.split("\t").toSeq))
    var fieldArray = new Array[StructField](rowRdd.take(1)(0).size)
    fieldArray(0) = StructField("rddshape", StringType)
    for (i <- 1 to fieldArray.length-1) fieldArray(i) = StructField("_c"+i, StringType)
    val schema = StructType(fieldArray)
    return sparkSession.createDataFrame(rowRdd, schema)
  }

  def toDf(spatialPairRDD: JavaPairRDD[Geometry, Geometry], sparkSession: SparkSession): DataFrame =
  {
    val rowRdd = spatialPairRDD.rdd.map[Row](f =>
    {
      val seq1 = f._1.toString.split("\t").toSeq
      val seq2 = f._2.toString.split("\t").toSeq
      val result = seq1++seq2
      Row.fromSeq(result)
    })
    var fieldArray = new Array[StructField](rowRdd.take(1)(0).size)
    fieldArray(0) = StructField("windowrddshape", StringType)
    for (i <- 1 to fieldArray.length-1) fieldArray(i) = StructField("_c"+i, StringType)
    val schema = StructType(fieldArray)
    return sparkSession.createDataFrame(rowRdd, schema)
  }

  /*
   * 由于 UserDefinedType 对用户隐藏。我们不能直接将spatialRDD 返回给spatialDf。
   * 让我们等待Spark方面的变化
   */
  /*
  def toSpatialDf(spatialRDD: SpatialRDD[Geometry], sparkSession: SparkSession): DataFrame =
  {
    val rowRdd = spatialRDD.rawSpatialRDD.rdd.map[Row](f =>
      {
        var seq = Seq(new GeometryWrapper(f))
        var otherFields = f.getUserData.asInstanceOf[String].split("\t").toSeq
        seq :+ otherFields
        Row.fromSeq(seq)
      }
      )
    var fieldArray = new Array[StructField](rowRdd.take(1)(0).size)
    fieldArray(0) = StructField("rddshape", ArrayType(ByteType, false))
    for (i <- 1 to fieldArray.length-1) fieldArray(i) = StructField("_c"+i, StringType)
    val schema = StructType(fieldArray)
    return sparkSession.createDataFrame(rowRdd, schema)
  }
  */
}

```
 
#### GeoSparkSQLRegistrator.scala

```scala
/**
  * FILE: GeoSparkSQLRegistrator
  * PATH: org.datasyslab.geosparksql.utils.GeoSparkSQLRegistrator
  * Copyright (c) GeoSpark Development Team
  *
  * MIT License
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */
package org.datasyslab.geosparksql.utils

import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.sql.geosparksql.strategy.join.JoinQueryDetector
import org.datasyslab.geosparksql.UDF.UdfRegistrator
import org.datasyslab.geosparksql.UDT.UdtRegistrator

object GeoSparkSQLRegistrator {
  def registerAll(sqlContext:SQLContext): Unit =
  {
    sqlContext.experimental.extraStrategies = JoinQueryDetector :: Nil
    UdtRegistrator.registerAll()
    UdfRegistrator.registerAll(sqlContext)
  }
  def dropAll(sparkSession: SparkSession): Unit =
  {
    UdfRegistrator.dropAll(sparkSession)
  }
}

```
 
#### GeometrySerializer.scala

```scala
/**
  * FILE: GeometrySerializer
  * PATH: org.datasyslab.geosparksql.utils.GeometrySerializer
  * Copyright (c) GeoSpark Development Team
  *
  * MIT License
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */
package org.datasyslab.geosparksql.utils

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}
import com.vividsolutions.jts.geom.Geometry
import org.apache.spark.sql.catalyst.util.ArrayData
import org.datasyslab.geospark.geometryObjects.GeometrySerde

// This is a wrapper of GeoSpark core kryo serializer
object GeometrySerializer {

  def serialize(geometry: Geometry): Array[Byte] = {
    val out = new ByteArrayOutputStream()
    val kryo = new Kryo()
    val geometrySerde = new GeometrySerde()
    val output = new Output(out)
    geometrySerde.write(kryo,output,geometry)
    output.close()
    return out.toByteArray
  }

  def deserialize(values: ArrayData): Geometry = {
    val in = new ByteArrayInputStream(values.toByteArray())
    val kryo = new Kryo()
    val geometrySerde = new GeometrySerde()
    val input = new Input(in)
    val geometry = geometrySerde.read(kryo, input,classOf[Geometry])
    input.close()
    return geometry.asInstanceOf[Geometry]
  }
}

```
 
#### IndexSerializer.scala


```scala
/**
  * FILE: IndexSerializer
  * PATH: org.datasyslab.geosparksql.utils.IndexSerializer
  * Copyright (c) GeoSpark Development Team
  *
  * MIT License
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */
package org.datasyslab.geosparksql.utils

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}
import com.vividsolutions.jts.index.SpatialIndex
import org.apache.spark.sql.catalyst.util.ArrayData

// This is a wrapper of GeoSpark core kryo serializer
object IndexSerializer {
  def serialize(index: SpatialIndex): Array[Byte] = {
    val out = new ByteArrayOutputStream()
    val kryo = new Kryo()
    val output = new Output(out)
    kryo.writeObject(output,index)
    output.close()
    return out.toByteArray
  }

  def deserialize(values: ArrayData): SpatialIndex = {
    val in = new ByteArrayInputStream(values.toByteArray())
    val kryo = new Kryo()
    val input = new Input(in)
    val spatialIndex = kryo.readObject(input, classOf[SpatialIndex])
    input.close()
    return spatialIndex.asInstanceOf[SpatialIndex]
  }

}

```


### SparkCity/core/src/main/scala/org/datasyslab/geospark/monitoring/

 extends  spark.scheduler.SparkListener

#### GeoSparkListener.scala

```scala
package org.datasyslab.geospark.monitoring

import org.apache.spark.scheduler.{AccumulableInfo, SparkListener, SparkListenerStageCompleted, SparkListenerTaskEnd}

import scala.collection.mutable

class GeoSparkListener extends SparkListener {

  private val counterNames = Seq("buildCount", "streamCount", "candidateCount", "resultCount")

  private val taskCpuTime: mutable.Map[(Integer, Integer), Long] = mutable.Map()

  override def onTaskEnd(taskEnd: SparkListenerTaskEnd): Unit = {
    if (taskEnd.reason == org.apache.spark.Success) {
      val cpuTime = taskEnd.taskMetrics.executorCpuTime
      // Task ID is a string made of two integers separated with period:
      //  <partition-id>.<attempt-id>
      val partitionId = Integer.parseInt(taskEnd.taskInfo.id.split('.')(0))

      taskCpuTime((taskEnd.stageId, partitionId)) = cpuTime
    }
  }

  override def onStageCompleted(stageCompleted: SparkListenerStageCompleted) = {
    val accumulables = stageCompleted.stageInfo.accumulables

    def getCounterOption(name: String) = {
      accumulables.find { case (k, v) => v.name == Some("geospark.spatialjoin." + name) }
    }

    def getCounter(name: String) = {
      getCounterOption(name).get._2.value.get
    }

    if (getCounterOption("buildCount").isDefined) {

      val stageId = stageCompleted.stageInfo.stageId

      val buildCounts: Map[Int, Long] = getCounter("buildCount").asInstanceOf[Map[Int, Long]]
      val streamCounts: Map[Int, Long] = getCounter("streamCount").asInstanceOf[Map[Int, Long]]
      val candidateCounts: Map[Int, Long] = getCounter("candidateCount").asInstanceOf[Map[Int, Long]]
      val resultCounts: Map[Int, Long] = getCounter("resultCount").asInstanceOf[Map[Int, Long]]

      val stats: List[(Int, Long, Long, Long, Long, Long)] =
        buildCounts.map {
          case (partitionId, buildCount) => {
            val streamCount: Long = streamCounts.getOrElse(partitionId, -1)
            val candidateCount: Long = candidateCounts.getOrElse(partitionId, -1)
            val resultCount: Long = resultCounts.getOrElse(partitionId, -1)
            val cpuTime: Long = taskCpuTime.getOrElse((stageId, partitionId), -1)
            (partitionId, buildCount, streamCount, candidateCount, resultCount, cpuTime)
          }
        }.toList.sortBy {
          case (_, _, _, _, _, cpuTime) => cpuTime
        }

      Console.out.println("Spatial join is complete. Execution statistics:")
      Console.out.println("Partition\t CPU Time (s)\tBuild ##\tStream ##\tCandidates ##\tResults ##")
      stats.foreach {
        case (partitionId, buildCount, streamCount, candidateCount, resultCount, cpuTime) =>
          Console.out.println(f"$partitionId% 10d\t${cpuTime / 1000}% 10d" +
            f"$buildCount% 10d\t$streamCount% 10d\t$candidateCount% 10d\t$resultCount% 10d")
      }
    }
  }
}

```
 
#### GeoSparkMetric.scala

extends spark.util.AccumulatorV2

```scala
package org.datasyslab.geospark.monitoring

import org.apache.spark.{SparkEnv, TaskContext}
import org.apache.spark.util.AccumulatorV2

import scala.collection.mutable

/**
  * An accumulator to collect custom per-task metrics into a map
  * keyed by partition ID processed by the task.
  */
case class GeoSparkMetric(initialValue: Map[Int, Long] = Map()) extends AccumulatorV2[Long, Map[Int, Long]] {
  private var _counts: mutable.Map[Int, Long] = mutable.Map[Int, Long]() ++ initialValue

  override def isZero: Boolean = _counts.isEmpty

  override def copy(): AccumulatorV2[Long, Map[Int, Long]] = new GeoSparkMetric(_counts.toMap)

  override def reset(): Unit = _counts.clear()

  override def add(v: Long): Unit = add(TaskContext.getPartitionId, v)

  private def add(partitionId: Int, value: Long) = {
    _counts(partitionId) = value + _counts.getOrElse(partitionId, 0L)
  }

  override def merge(other: AccumulatorV2[Long, Map[Int, Long]]): Unit = {
    other.asInstanceOf[GeoSparkMetric]._counts.foreach {
      case (partitionId, value) => add(partitionId, value)
    }
  }

  override def value: Map[Int, Long] = _counts.toMap
}

```
 
#### GeoSparkMetrics.scala

```scala
package org.datasyslab.geospark.monitoring

import org.apache.spark.SparkContext

object GeoSparkMetrics {
  def createMetric(sc: SparkContext, name: String): GeoSparkMetric = {
    val acc = new GeoSparkMetric()
    sc.register(acc, "geospark.spatialjoin." + name)
    acc
  }
}

```

### SparkCity/core/src/main/scala/org/datasyslab/geospark/showcase/

#### ScalaEarthdataMapperRunnableExample.scala

```scala
/**
  * FILE: ScalaEarthdataMapperRunnableExample.java
  * PATH: org.datasyslab.geospark.showcase.ScalaEarthdataMapperRunnableExample.java
  * Copyright (c) 2017 Arizona State University Data Systems Lab
  * All rights reserved.
  */
package org.datasyslab.geospark.showcase

import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.storage.StorageLevel
import org.datasyslab.geospark.enums.FileDataSplitter
import org.datasyslab.geospark.enums.IndexType
import org.datasyslab.geospark.formatMapper.EarthdataHDFPointMapper
import org.datasyslab.geospark.spatialOperator.RangeQuery
import org.datasyslab.geospark.spatialRDD.PointRDD
import com.vividsolutions.jts.geom.Envelope

  object ScalaEarthdataMapperRunnableExample extends App {
    val conf = new SparkConf().setAppName("EarthdataMapperRunnableExample").setMaster("local[2]")
    val sc = new SparkContext(conf)
    Logger.getLogger("org").setLevel(Level.WARN)
    Logger.getLogger("akka").setLevel(Level.WARN)
    val InputLocation = System.getProperty("user.dir") + "/src/test/resources/modis/modis.csv"
    val splitter = FileDataSplitter.CSV
    val indexType = IndexType.RTREE
    val queryEnvelope = new Envelope(-90.01, -80.01, 30.01, 40.01)
    val numPartitions = 5
    val loopTimes = 1
    val HDFIncrement = 5
    val HDFOffset = 2
    val HDFRootGroupName = "MOD_Swath_LST"
    val HDFDataVariableName = "LST"
    val urlPrefix = System.getProperty("user.dir") + "/src/test/resources/modis/"
    val HDFDataVariableList = Array("LST", "QC", "Error_LST", "Emis_31", "Emis_32")
    testSpatialRangeQuery()
    testSpatialRangeQueryUsingIndex()
    sc.stop()
    System.out.println("All GeoSpark Earthdata DEMOs passed!")

    /**
      * Test spatial range query.
      */
    def testSpatialRangeQuery() {
      val earthdataHDFPoint = new EarthdataHDFPointMapper(HDFIncrement, HDFOffset, HDFRootGroupName, HDFDataVariableList, HDFDataVariableName, urlPrefix)
      val spatialRDD = new PointRDD(sc, InputLocation, numPartitions, earthdataHDFPoint, StorageLevel.MEMORY_ONLY)
      var i = 0
      while (i < loopTimes) {
          var resultSize = 0L
          resultSize = RangeQuery.SpatialRangeQuery(spatialRDD, queryEnvelope, false, false).count
          i=i+1
      }
    }

    /**
      * Test spatial range query using index.
      */
    def testSpatialRangeQueryUsingIndex() {
      val earthdataHDFPoint = new EarthdataHDFPointMapper(HDFIncrement, HDFOffset, HDFRootGroupName, HDFDataVariableList, HDFDataVariableName, urlPrefix)
      val spatialRDD = new PointRDD(sc, InputLocation, numPartitions, earthdataHDFPoint, StorageLevel.MEMORY_ONLY)
        spatialRDD.buildIndex(IndexType.RTREE, false)
      var i = 0
      while (i < loopTimes) {
          var resultSize = 0L
          resultSize = RangeQuery.SpatialRangeQuery(spatialRDD, queryEnvelope, false, true).count
          i=i+1
      }
      }
}

```
 
#### ScalaExample.scala

```scala
/**
	* FILE: ScalaExample.java
	* PATH: org.datasyslab.geospark.showcase.ScalaExample.scala
	* Copyright (c) 2017 Arizona State University Data Systems Lab
	* All rights reserved.
	*/
package org.datasyslab.geospark.showcase

import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.storage.StorageLevel
import org.datasyslab.geospark.enums.FileDataSplitter
import org.datasyslab.geospark.enums.GridType
import org.datasyslab.geospark.enums.IndexType
import org.datasyslab.geospark.spatialOperator.JoinQuery
import org.datasyslab.geospark.spatialOperator.KNNQuery
import org.datasyslab.geospark.spatialOperator.RangeQuery
import org.datasyslab.geospark.spatialRDD.CircleRDD
import org.datasyslab.geospark.spatialRDD.PointRDD
import org.datasyslab.geospark.spatialRDD.PolygonRDD
import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.Envelope
import com.vividsolutions.jts.geom.GeometryFactory
import org.apache.spark.serializer.KryoSerializer
import org.datasyslab.geospark.formatMapper.shapefileParser.ShapefileRDD
import org.datasyslab.geospark.serde.GeoSparkKryoRegistrator


/**
	* The Class ScalaExample.
	*/
object ScalaExample extends App{

	val conf = new SparkConf().setAppName("GeoSparkRunnableExample").setMaster("local[2]")
	conf.set("spark.serializer", classOf[KryoSerializer].getName)
	conf.set("spark.kryo.registrator", classOf[GeoSparkKryoRegistrator].getName)

	val sc = new SparkContext(conf)
	Logger.getLogger("org").setLevel(Level.WARN)
	Logger.getLogger("akka").setLevel(Level.WARN)

	val resourceFolder = System.getProperty("user.dir")+"/src/test/resources/"

	val PointRDDInputLocation = resourceFolder+"arealm-small.csv"
	val PointRDDSplitter = FileDataSplitter.CSV
	val PointRDDIndexType = IndexType.RTREE
	val PointRDDNumPartitions = 5
	val PointRDDOffset = 0

	val PolygonRDDInputLocation = resourceFolder + "primaryroads-polygon.csv"
	val PolygonRDDSplitter = FileDataSplitter.CSV
	val PolygonRDDNumPartitions = 5
	val PolygonRDDStartOffset = 0
	val PolygonRDDEndOffset = 8

	val geometryFactory=new GeometryFactory()
	val kNNQueryPoint=geometryFactory.createPoint(new Coordinate(-84.01, 34.01))
	val rangeQueryWindow=new Envelope (-90.01,-80.01,30.01,40.01)
	val joinQueryPartitioningType = GridType.QUADTREE
	val eachQueryLoopTimes=5

	var ShapeFileInputLocation = resourceFolder+"shapefiles/polygon"

	testSpatialRangeQuery()
	testSpatialRangeQueryUsingIndex()
	testSpatialKnnQuery()
	testSpatialKnnQueryUsingIndex()
	testSpatialJoinQuery()
	testSpatialJoinQueryUsingIndex()
	testDistanceJoinQuery()
	testDistanceJoinQueryUsingIndex()
	testCRSTransformationSpatialRangeQuery()
	testCRSTransformationSpatialRangeQueryUsingIndex()
	sc.stop()
	System.out.println("All GeoSpark DEMOs passed!")


	/**
		* Test spatial range query.
		*
		* @throws Exception the exception
		*/
	def testSpatialRangeQuery() {
	val objectRDD = new PointRDD(sc, PointRDDInputLocation, PointRDDOffset, PointRDDSplitter, true, StorageLevel.MEMORY_ONLY)
	objectRDD.rawSpatialRDD.persist(StorageLevel.MEMORY_ONLY)
	for(i <- 1 to eachQueryLoopTimes)
	{
		val resultSize = RangeQuery.SpatialRangeQuery(objectRDD, rangeQueryWindow, false,false).count
	}
	}



	/**
		* Test spatial range query using index.
		*
		* @throws Exception the exception
		*/
	def testSpatialRangeQueryUsingIndex() {
	val objectRDD = new PointRDD(sc, PointRDDInputLocation, PointRDDOffset, PointRDDSplitter, true, StorageLevel.MEMORY_ONLY)
	objectRDD.buildIndex(PointRDDIndexType,false)
	objectRDD.indexedRawRDD.persist(StorageLevel.MEMORY_ONLY)
	for(i <- 1 to eachQueryLoopTimes)
	{
		val resultSize = RangeQuery.SpatialRangeQuery(objectRDD, rangeQueryWindow, false,true).count
	}

	}

	/**
		* Test spatial knn query.
		*
		* @throws Exception the exception
		*/
	def testSpatialKnnQuery() {
	val objectRDD = new PointRDD(sc, PointRDDInputLocation, PointRDDOffset, PointRDDSplitter, true, StorageLevel.MEMORY_ONLY)
	objectRDD.rawSpatialRDD.persist(StorageLevel.MEMORY_ONLY)
	for(i <- 1 to eachQueryLoopTimes)
	{
		val result = KNNQuery.SpatialKnnQuery(objectRDD, kNNQueryPoint, 1000,false)
	}
	}

	/**
		* Test spatial knn query using index.
		*
		* @throws Exception the exception
		*/
	def testSpatialKnnQueryUsingIndex() {
	val objectRDD = new PointRDD(sc, PointRDDInputLocation, PointRDDOffset, PointRDDSplitter, true, StorageLevel.MEMORY_ONLY)
	objectRDD.buildIndex(PointRDDIndexType,false)
	objectRDD.indexedRawRDD.persist(StorageLevel.MEMORY_ONLY)
	for(i <- 1 to eachQueryLoopTimes)
	{
		val result = KNNQuery.SpatialKnnQuery(objectRDD, kNNQueryPoint, 1000, true)
	}
	}

	/**
		* Test spatial join query.
		*
		* @throws Exception the exception
		*/
	def testSpatialJoinQuery() {
	val queryWindowRDD = new PolygonRDD(sc, PolygonRDDInputLocation, PolygonRDDStartOffset, PolygonRDDEndOffset, PolygonRDDSplitter, true)
	val objectRDD = new PointRDD(sc, PointRDDInputLocation, PointRDDOffset, PointRDDSplitter, true, StorageLevel.MEMORY_ONLY)

	objectRDD.spatialPartitioning(joinQueryPartitioningType)
	queryWindowRDD.spatialPartitioning(objectRDD.getPartitioner)

	objectRDD.spatialPartitionedRDD.persist(StorageLevel.MEMORY_ONLY)
	queryWindowRDD.spatialPartitionedRDD.persist(StorageLevel.MEMORY_ONLY)
	for(i <- 1 to eachQueryLoopTimes)
	{
		val resultSize = JoinQuery.SpatialJoinQuery(objectRDD,queryWindowRDD,false,true).count
	}
	}

	/**
		* Test spatial join query using index.
		*
		* @throws Exception the exception
		*/
	def testSpatialJoinQueryUsingIndex() {
	val queryWindowRDD = new PolygonRDD(sc, PolygonRDDInputLocation, PolygonRDDStartOffset, PolygonRDDEndOffset, PolygonRDDSplitter, true)
	val objectRDD = new PointRDD(sc, PointRDDInputLocation, PointRDDOffset, PointRDDSplitter, true, StorageLevel.MEMORY_ONLY)

	objectRDD.spatialPartitioning(joinQueryPartitioningType)
	queryWindowRDD.spatialPartitioning(objectRDD.getPartitioner)

	objectRDD.buildIndex(PointRDDIndexType,true)

	objectRDD.indexedRDD.persist(StorageLevel.MEMORY_ONLY)
	queryWindowRDD.spatialPartitionedRDD.persist(StorageLevel.MEMORY_ONLY)

	for(i <- 1 to eachQueryLoopTimes)
	{
		val resultSize = JoinQuery.SpatialJoinQuery(objectRDD,queryWindowRDD,true,false).count()
	}
	}

	/**
		* Test spatial join query.
		*
		* @throws Exception the exception
		*/
	def testDistanceJoinQuery() {
	val objectRDD = new PointRDD(sc, PointRDDInputLocation, PointRDDOffset, PointRDDSplitter, true, StorageLevel.MEMORY_ONLY)
	val queryWindowRDD = new CircleRDD(objectRDD,0.1)

	objectRDD.spatialPartitioning(GridType.QUADTREE)
	queryWindowRDD.spatialPartitioning(objectRDD.getPartitioner)

	objectRDD.spatialPartitionedRDD.persist(StorageLevel.MEMORY_ONLY)
	queryWindowRDD.spatialPartitionedRDD.persist(StorageLevel.MEMORY_ONLY)

	for(i <- 1 to eachQueryLoopTimes)
	{
		val resultSize = JoinQuery.DistanceJoinQuery(objectRDD,queryWindowRDD,false,true).count()
	}
	}

	/**
		* Test spatial join query using index.
		*
		* @throws Exception the exception
		*/
	def testDistanceJoinQueryUsingIndex() {
	val objectRDD = new PointRDD(sc, PointRDDInputLocation, PointRDDOffset, PointRDDSplitter, true, StorageLevel.MEMORY_ONLY)
	val queryWindowRDD = new CircleRDD(objectRDD,0.1)

	objectRDD.spatialPartitioning(GridType.QUADTREE)
	queryWindowRDD.spatialPartitioning(objectRDD.getPartitioner)

	objectRDD.buildIndex(IndexType.RTREE,true)

	objectRDD.indexedRDD.persist(StorageLevel.MEMORY_ONLY)
	queryWindowRDD.spatialPartitionedRDD.persist(StorageLevel.MEMORY_ONLY)

	for(i <- 1 to eachQueryLoopTimes)
	{
		val resultSize = JoinQuery.DistanceJoinQuery(objectRDD,queryWindowRDD,true,true).count
	}
	}

	@throws[Exception]
	def testCRSTransformationSpatialRangeQuery(): Unit = {
		val objectRDD = new PointRDD(sc, PointRDDInputLocation, PointRDDOffset, PointRDDSplitter, true, StorageLevel.MEMORY_ONLY, "epsg:4326", "epsg:3005")
		objectRDD.rawSpatialRDD.persist(StorageLevel.MEMORY_ONLY)
		var i = 0
		while ( {
			i < eachQueryLoopTimes
		}) {
			val resultSize = RangeQuery.SpatialRangeQuery(objectRDD, rangeQueryWindow, false, false).count
			assert(resultSize > -1)

			{
				i += 1; i - 1
			}
		}
	}


	@throws[Exception]
	def testCRSTransformationSpatialRangeQueryUsingIndex(): Unit = {
		val objectRDD = new PointRDD(sc, PointRDDInputLocation, PointRDDOffset, PointRDDSplitter, true, StorageLevel.MEMORY_ONLY, "epsg:4326", "epsg:3005")
		objectRDD.buildIndex(PointRDDIndexType, false)
		objectRDD.indexedRawRDD.persist(StorageLevel.MEMORY_ONLY)
		var i = 0
		while ( {
			i < eachQueryLoopTimes
		}) {
			val resultSize = RangeQuery.SpatialRangeQuery(objectRDD, rangeQueryWindow, false, true).count
			assert(resultSize > -1)

			{
				i += 1; i - 1
			}
		}
	}

	@throws[Exception]
	def testLoadShapefileIntoPolygonRDD(): Unit = {
		val shapefileRDD = new ShapefileRDD(sc, ShapeFileInputLocation)
		val spatialRDD = new PolygonRDD(shapefileRDD.getPolygonRDD)
		try
			RangeQuery.SpatialRangeQuery(spatialRDD, new Envelope(-180, 180, -90, 90), false, false).count
		catch {
			case e: Exception =>
				// TODO Auto-generated catch block 自动生成的catch块
				e.printStackTrace()
		}
	}

}
```
 
#### SpatialJoinShp.scala

```scala
package org.datasyslab.geospark.showcase

import com.vividsolutions.jts.geom.Polygon
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.storage.StorageLevel
import org.datasyslab.geospark.formatMapper.shapefileParser.ShapefileRDD
import org.datasyslab.geospark.spatialRDD.PolygonRDD

object SpatialJoinShp extends App {

  def loadShapefile(path: String, numPartitions: Int = 20): PolygonRDD = {
    val shp = new ShapefileRDD(sc, path)
    val polygon = new PolygonRDD(shp.getPolygonRDD, StorageLevel.MEMORY_ONLY)
    //polygon.rawSpatialRDD = polygon.rawSpatialRDD.repartition(numPartitions)
    //polygon.analyze()
    polygon
  }



  Logger.getLogger("org").setLevel(Level.WARN)
  Logger.getLogger("akka").setLevel(Level.WARN)

  val conf = new SparkConf().setAppName("SpatialJoinSpeciesPA").setMaster("local[4]")
  val sc = new SparkContext(conf)

  val shp1 = new ShapefileRDD(sc, "/Users/jiayu/Downloads/spark4geo_subset/wdpa")
  val wdpa = new PolygonRDD(shp1.getPolygonRDD, StorageLevel.MEMORY_ONLY)

  val shp2 = new ShapefileRDD(sc, "/Users/jiayu/Downloads/spark4geo_subset/amphib")
  val species = new PolygonRDD(shp2.getPolygonRDD, StorageLevel.MEMORY_ONLY)

  //wdpa.spatialPartitioning(GridType.QUADTREE)
  //species.spatialPartitioning(wdpa.partitionTree)


  val result = shp2.getShapeRDD.collect();

  for( a <- 1 until result.size()){
    println( "print..."+result.get(a).getUserData+" END");
  }

  //val query = JoinQuery.SpatialJoinQuery(wdpa, species, false, false)

  println("polygon is "+shp2.getPolygonRDD.take(100).get(55))
  println("userdata is "+wdpa.rawSpatialRDD.take(100).get(55).asInstanceOf[Polygon].getUserData)
  println(species.rawSpatialRDD.count())


  //val user_data_sample = JoinQuery.SpatialJoinQuery(wdpa, species, false, false).first()._1.getUserData
  //if (user_data_sample.toString.isEmpty) println("UserData is empty") else println(user_data_sample)

//  val join_result = query.rdd.map((tuple: (Polygon, util.HashSet[Polygon])) => (tuple._1, tuple._2.asScala.map(tuple._1.intersection(_).getArea)) )
//  val intersections = join_result.collect()
}

```


### SparkCity/python/analysis/


#### __init__.py

空的
 
#### basic_statistics.py

```py
from analysis.csv_util import load_data
import matplotlib.pyplot as plt

import pandas as pd
import numpy as np
import os

from matplotlib import rcParams
rcParams.update({'figure.autolayout': True})


def describe_data(df, col_names):
    # density plot
    df.plot(kind='density', subplots=True, layout=(len(col_names), 1), sharex=False, figsize=(10, 10))
    correlations = df.corr()
    plt.show()

    # plot correlation matrix
    fig = plt.figure(figsize=(10, 10))
    ax = fig.add_subplot(111)
    cax = ax.matshow(correlations, vmin=-1, vmax=1)
    fig.colorbar(cax)
    ticks = np.arange(0, len(col_names), 1)
    ax.set_xticks(ticks)
    ax.set_yticks(ticks)
    ax.set_xticklabels(col_names)
    ax.set_yticklabels(col_names)
    plt.show()

    # Scatterplot Matrix
    pd.scatter_matrix(df, figsize=(10, 10))
    plt.show()


def top_poi(statename, key='lst'):
    csvfile = f"data/{statename}/lst/{statename}_lst_pois.csv"
    schema = "osm_id,code,fclass,name,lst,ndvi,ndwi,ndbi,ndii,mndwi,ndisi,longitude,latitude,area".split(",")

    df_raw = load_data(csvfile, hasheader=True)
    df_raw = filter_by_area(df_raw)
    # va (-77.622, -76.995, 38.658, 39.105)
    # df_raw = df_raw[(df_raw['longitude'] > -77.622)
    #                 & (df_raw['longitude'] < -76.995)
    #                 & (df_raw['latitude'] > 38.658)
    #                 & (df_raw['latitude'] < 39.105)]

    df_sort = df_raw.sort_values(by=[key], ascending=False)

    row_num = len(df_raw)

    percent = 0.1

    df_top = df_sort.head(int(row_num*percent))
    df_tail = df_sort.tail(int(row_num*percent))

    dir_path = f"data/{statename}/result/poi/"
    os.makedirs(dir_path, exist_ok=True)

    df_top.sort_values(by=['fclass']).to_csv(f"data/{statename}/result/poi/poi_top_{percent}.csv", header=True)
    df_tail.sort_values(by=['fclass']).to_csv(f"data/{statename}/result/poi/poi_tail_{percent}.csv", header=True)

    df_top[df_top['fclass']=='pitch'].to_csv(f"data/{statename}/result/poi/top_pitch_{percent}.csv", header=True)
    df_tail[df_tail['fclass']=='pitch'].to_csv(f"data/{statename}/result/poi/tail_pitch_{percent}.csv", header=True)

    top_count = df_top['fclass'].value_counts()
    tail_count = df_tail['fclass'].value_counts()

    top_count.to_csv(f"data/{statename}/result/poi/top_count.csv", header=True)
    tail_count.to_csv(f"data/{statename}/result/poi/tail_count.csv", header=True)

    #plt.tight_layout()
    fig = plt.figure(figsize=(12, 9))
    ax = top_count.head(15).plot(kind='bar', title="Top 10%", fontsize=15)
    ax.title.set_fontsize(20)
    fig.show()
    fig.savefig(f"data/{statename}/result/poi/top_count_{percent}.png")

    fig = plt.figure(figsize=(12, 9))
    ax = tail_count.head(15).plot(kind='bar', title="Bottom 10%", fontsize=15)
    ax.title.set_fontsize(20)
    fig.show()
    fig.savefig(f"data/{statename}/result/poi/Bottom_count_{percent}.png")

def filter_by_area(df):
    df = df[(df["area"]*111000*111000 >= 900)]
    return df

def descriptive_statistics(statename, layer, label_fontsize=10, title_fontsize=20):
    csvfile = f"data/{statename}/lst/{statename}_lst_{layer.lower()}.csv"
    schema = "fclass,lst".split(",")

    df = load_data(csvfile, hasheader=True)
    print(len(df))
    df = filter_by_area(df)
    print("---------------------")
    print(len(df))
    #print(df.groupby("fclass")["lst"].describe())
    sorted_fclass = df.groupby("fclass")["lst"].median().sort_values(ascending=False).index

    plot = df\
        .pivot_table(index="osm_id", columns="fclass")["lst"][sorted_fclass]\
        .plot(kind='box', figsize=[16,8],
              title=f"Descriptive Statistics of LST by {layer} Type ({statename.upper()})",
              rot=270,
              fontsize=label_fontsize)
    plot.title.set_fontsize(title_fontsize)
    fig = plot.get_figure()

    plt.show()

    dir_path = f"data/{statename}/result/descriptive_statistics/"
    os.makedirs(dir_path, exist_ok=True)
    fig.savefig(f"data/{statename}/result/descriptive_statistics/{statename}_{layer}_ds.png")

    plt.show()


if __name__ == '__main__':
    # top_poi("va")
    descriptive_statistics(statename="va", layer="POIs", label_fontsize=10, title_fontsize=20)
    # descriptive_statistics(statename="dc", layer="POIs", label_fontsize=10, title_fontsize=20)
    # descriptive_statistics(statename="md", layer="POIs", label_fontsize=10, title_fontsize=20)
    #
    # descriptive_statistics(statename="va", layer="Landuse", label_fontsize=15, title_fontsize=20)
    # descriptive_statistics(statename="dc", layer="Landuse", label_fontsize=15, title_fontsize=20)
    # descriptive_statistics(statename="md", layer="Landuse", label_fontsize=15, title_fontsize=20)
```
 
#### correlation_block_level.py

```py

from analysis.linear_regression import load_data, train_test_split, linear_regression, ridge_regression, lasso_regression, stepwise_selection, randomforest_regression


def regression_analysis(csv_file, feature_columns, target_column, test_percent = 0.3):
    df = load_data(csv_file, hasheader=True)
    #df = df[(df["CP"] != 0) & (df["TP"] != 0)]
    num_raw_df = len(df)
    #df = df[(df["CP"] != 0) | (df["MPS"] != 0) | (df["MSI"] != 0) |	(df["MNND"] != 0) | (df["PCI"] != 0) | (df["FN"] != 0) | (df["TP"] != 0) | (df["RP"] != 0) | (df["WP"] != 0)]
    df = df[df["FN"] > 3]
    num_filtered_df = len(df)

    print("*************************************************** ", num_filtered_df, "/", num_raw_df)

    #describe_data(df[feature_columns + target_column], feature_columns + target_column)

    #result = correlation_test(df[feature_columns + target_column])
    #print(result)

    X_train, y_train, X_test, y_test = train_test_split(df,
                                                        feature_columns,
                                                        target_column,
                                                        test_percent,
                                                        isNormalize=False,
                                                        isStandardize=True)

    linear_regression(X_train, y_train, X_test, y_test, normalize=False)
    lasso_regression(X_train, y_train, X_test, y_test, normalize=False)
    ridge_regression(X_train, y_train, X_test, y_test, normalize=False)
    randomforest_regression(X_train, y_train, X_test, y_test)

    # result = stepwise_selection(X_train, y_train,
    #                             initial_list=[],
    #                             threshold_in=0.01,
    #                             threshold_out=0.05,
    #                             verbose=True)

    #print('resulting features:')
    #print(result)


def main():
    statename = "dc"
    input_dir = "/Users/feihu/Documents/GitHub/SparkCity/data/20171228/"
    csv_file = f"{input_dir}/{statename}/result/join_feature.csv"
    # CP : building percentage
    feature_columns = "ndvi,ndwi,ndbi,ndii,mndwi,ndisi,CP,WP,MPS,MSI,MNND,PCI,FN,TP,RP,population,income".split(",")
    feature_columns = "CP,RP,TP,WP,MPS,MSI,MNND,PCI,population,income".split(",")
    target_column = ["lst"]
    test_percent = 0.2
    regression_analysis(csv_file, feature_columns, target_column, test_percent)


if __name__ == '__main__':
    main()

```
 
#### correlation_poi_level.py

```py

from analysis.linear_regression import load_data, train_test_split, linear_regression, ridge_regression, lasso_regression, stepwise_selection, randomforest_regression
from analysis.basic_statistics import filter_by_area


def regression_analysis(csv_file, feature_columns, target_column, fclass="", test_percent=0.3):
    df = load_data(csv_file, hasheader=True)
    df = filter_by_area(df)

    if len(fclass) > 0:
        df = df[(df["fclass"] == fclass)]
    print(len(df))

    #describe_data(df[feature_columns + target_column], feature_columns + target_column)

    X_train, y_train, X_test, y_test = train_test_split(df,
                                                        feature_columns,
                                                        target_column,
                                                        test_percent,
                                                        isNormalize=False,
                                                        isStandardize=True)

    linear_regression(X_train, y_train, X_test, y_test, normalize=False)
    lasso_regression(X_train, y_train, X_test, y_test, normalize=False)
    ridge_regression(X_train, y_train, X_test, y_test, normalize=False)
    randomforest_regression(X_train, y_train, X_test, y_test, max_depth=6)

    # result = stepwise_selection(X_train, y_train,
    #                             initial_list=[],
    #                             threshold_in=0.01,
    #                             threshold_out=0.05,
    #                             verbose=True)
    #
    # print('resulting features:')
    # print(result)


def main():
    statename = "md"
    input_dir = "/Users/feihu/Documents/GitHub/SparkCity/data/"
    csv_file = f"{input_dir}/{statename}/lst/{statename}_lst_pois.csv"
    feature_columns = "ndvi,ndwi,ndbi,ndii,ndisi".split(",")
    target_column = ["lst"]
    test_percent = 0.3
    regression_analysis(csv_file, feature_columns, target_column, "", test_percent)


if __name__ == '__main__':
    main()

```
 
#### correlation_test.py

```py
from analysis.csv_util import load_data
import matplotlib.pyplot as plt

import pandas as pd
import numpy as np
import os
from string import ascii_letters
import seaborn as sns

from matplotlib import rcParams
rcParams.update({'figure.autolayout': True})


def correlation_test(df, method='pearson'):
    return df.corr(method=method)


def visualize_corr(corr):
    # Generate a mask for the upper triangle
    mask = np.zeros_like(corr, dtype=np.bool)
    mask[np.triu_indices_from(mask)] = True

    # Set up the matplotlib figure
    f, ax = plt.subplots(figsize=(11, 9))

    # Generate a custom diverging colormap
    cmap = sns.diverging_palette(220, 10, as_cmap=True)

    # Draw the heatmap with the mask and correct aspect ratio
    # sns.heatmap(corr, mask=mask, cmap=cmap, vmax=.3, center=0,
    #             square=True, linewidths=.5, cbar_kws={"shrink": .5})

    sns.set(font_scale=2.4)
    sns.heatmap(corr, mask=np.zeros_like(corr, dtype=np.bool),
                cmap=sns.diverging_palette(220, 10, as_cmap=True),
                cbar_kws={"shrink": .5},
                square=True, ax=ax, linewidths=.5,
                annot_kws={"size": 120})

    plt.yticks(rotation=0, fontsize=18)
    plt.xticks(rotation=270, fontsize=18)

    plt.show()


if __name__ == '__main__':
    csv_file = "/Users/feihu/Documents/GitHub/SparkCity/data/md/result/join_feature.csv"
    df = load_data(csv_file, hasheader=True)
    columns = "lst,CP,MPS,MSI,MNND,PCI,TP,RP,WP,population,income".split(",")
    df = df[columns]
    df.columns = "LST,PBA,MPS,MSI,MNND,PCI,PPA,PRA,PWA,Population,Income".split(",")
    corr = correlation_test(df)
    print(corr)
    visualize_corr(corr)

    plt.show()

```
 
#### csv_util.py

```py
import pandas as pd


def load_data(file_path, col_names=[], hasheader=False):
    if hasheader:
        x = pd.read_csv(file_path, header=0)
    else:
        x = pd.read_csv(file_path, names=col_names)

    return x


def join_tables(df_left, df_right, how='left', on=["osm_id"]):
    return pd.merge(df_left, df_right, how=how, on=on)


def join_block_table(statename="va", input_dir = "/Users/feihu/Documents/GitHub/SparkCity/data/20180318/"):

    block_table_base = f"{input_dir}/{statename}/result/{statename}_cb.csv"
    block_table_base_col = "STATEFP,COUNTYFP,TRACTCE,BLKGRPCE,AFFGEOID,GEOID,NAME,LSAD,ALAND,AWATER," \
                           "lst,ndvi,ndwi,ndbi,ndii,mndwi,ndisi".split(",")

    block_table = f"{input_dir}/{statename}/lst/{statename}_lst_block.csv"
    block_table_columns = "AFFGEOID,area".split(",")

    buildings_table = f"{input_dir}/{statename}/result/{statename}_buildings.csv"
    buildings_table_columns = "AFFGEOID,CP,MPS,MSI,MNND,PCI,FN".split(",")

    parkings_table = f"{input_dir}/{statename}/result/{statename}_parkings.csv"
    parkings_table_columns = "AFFGEOID,TP".split(",")

    roads_table = f"{input_dir}/{statename}/result/{statename}_roads.csv"
    roads_table_columns = "AFFGEOID,RP".split(",")

    water_table = f"{input_dir}/{statename}/result/{statename}_water.csv"
    water_table_columns = "AFFGEOID,WP".split(",")

    race_table = f"{input_dir}/{statename}/social/{statename}_race.csv"
    race_table_columns = "GEOID,B02001e1".split(",")

    income_table = f"{input_dir}/{statename}/social/{statename}_income.csv"
    income_table_columns = "GEOID,B19001e1".split(",")

    df_block_base = load_data(block_table_base, hasheader=True)[block_table_base_col]
    df_block = load_data(block_table, hasheader=True)[block_table_columns]
    df_buildings = load_data(buildings_table, hasheader=True)[buildings_table_columns]
    df_parkings = load_data(parkings_table, hasheader=True)[parkings_table_columns]
    df_roads = load_data(roads_table, hasheader=True)[roads_table_columns]
    df_water = load_data(water_table, hasheader=True)[water_table_columns]

    df_race = load_data(race_table, hasheader=True)[race_table_columns]
    df_race.columns = ['AFFGEOID', 'population']
    df_race['AFFGEOID'] = df_race['AFFGEOID'].apply(lambda id: id.replace("15000", "1500000"))

    df_income = load_data(income_table, hasheader=True)[income_table_columns]
    df_income.columns = ['AFFGEOID', 'income']
    df_income['AFFGEOID'] = df_income['AFFGEOID'].apply(lambda id: id.replace("15000", "1500000"))

    # B19001e1 : house hold income
    # B02001e1: total population

    for df_right in [df_block, df_buildings, df_parkings, df_roads, df_water, df_race, df_income]:
        df_block_base = join_tables(df_block_base, df_right, how='left', on=["AFFGEOID"])

    df_block_base["population"] = df_block_base["population"] / df_block_base["area"]

    df_block_base.fillna(0).to_csv(f"{input_dir}/{statename}/result/join_feature.csv", index=False)


def join_landuse_table(args=None):
    landuse = "data/result/va/landuse/landuse.csv"
    landuse_cols = "osm_id	code	fclass	name	lst	ndvi	ndwi	ndbi	ndii	mndwi	" \
                   "ndisi".split("\t")
    buildings = "data/result/va/landuse/buildings.csv"
    buildings_cols = "osm_id	CP	MPS	MSI	MNND	PCI".split("\t")
    parkings = "data/result/va/landuse/parkings.csv"
    parkings_cols = "osm_id	TP".split("\t")
    roads = "data/result/va/landuse/roads.csv"
    roads_cols = "osm_id	RP".split("\t")

    df_landuse = load_data(landuse, hasheader=True)[landuse_cols]
    df_buildings = load_data(buildings, hasheader=True)[buildings_cols]
    df_parkings = load_data(parkings, hasheader=True)[parkings_cols]
    df_roads = load_data(roads, hasheader=True)[roads_cols]

    for df_right in [df_buildings, df_parkings, df_roads]:
        df_landuse = join_tables(df_landuse, df_right, how='left', on=["osm_id"])

    df_landuse.fillna(0).to_csv("data/result/va/landuse/join_features.csv", index=False)


if __name__ == '__main__':
    # 20180318
    # 20171228
    # 20170416
    input_dir = "/Users/feihu/Documents/GitHub/SparkCity/data/20170416/"
    join_block_table(statename="md", input_dir=input_dir)
    join_block_table(statename="va", input_dir=input_dir)
    join_block_table(statename="dc", input_dir=input_dir)

```
 
#### linear_regression.py

pd.DataFrame.from_records

```py
import matplotlib.pyplot as plt
import numpy as np
from sklearn import datasets, linear_model
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_squared_error, r2_score

import pandas as pd

from analysis.csv_util import load_data

import numpy as np
from sklearn.linear_model import Ridge, RidgeCV

import statsmodels.api as sm
from scipy.stats import pearsonr

from sklearn.model_selection import train_test_split as sklearn_train_test_split

pd.set_option('display.width', 1000)

POLYGON_ID = "id"
LST = "lst"
NDVI = "ndvi"
NDWI = "ndwi"
NDBI = "ndbi"
NDII = "ndii"
MNDWI = "mndwi"
NDISI = "ndisi"
CP = "CP"
MNND = "MNND"
PCI = "PCI"

CSV_COLUMNS = "osm_id	code	fclass	name	lst	ndvi	ndwi	ndbi	ndii	mndwi	ndisi	CP	MPS	MSI	MNND	PCI	TP	RP".split("\t")
FEATURE_COLUMNS = "ndvi	ndwi	ndbi	ndii	mndwi	ndisi	CP	MPS	MSI	MNND	PCI	TP	RP".split("\t")
LABEL_COLUMN = [LST]
csvfile = "data/result/va/result/join_features.csv"


def normalize(df):
    return (df - df.min()) / (df.max() - df.min())


def standardize(df):
    return (df - df.mean()) / df.std()


def train_test_split(df, x_cols, y_col, test_percent, isStandardize=False, isNormalize=False):
    # print(df.head(5))
    df_X = df[x_cols]
    df_y = df[y_col]

    X_train, X_test, y_train, y_test = sklearn_train_test_split(df_X,
                                                                df_y,
                                                                test_size=test_percent,
                                                                random_state=None)

    if isStandardize:
        X_train = standardize(X_train)
        X_test = standardize(X_test)
        return X_train, y_train, X_test, y_test

    if isNormalize:
        X_train = normalize(X_train)
        X_test= normalize(X_test)
        return X_train, y_train, X_test, y_test

    return X_train, y_train, X_test, y_test


def randomforest_regression(X_train, y_train, X_test, y_test, max_depth=6):
    print("-------------------------- RandomForest Regression")
    clf = RandomForestRegressor(max_depth=max_depth, random_state=0)
    clf.fit(X_train, y_train)

    y_pred = clf.predict(X_test)

    # 均方误差
    print("Mean squared error: %.2f"
          % mean_squared_error(y_test, y_pred))
    # 解释方差得分：1 是完美预测
    print('Coefficient of determination(R^2): %.2f' % r2_score(y_test, y_pred))
    # 系数
    cols = X_train.columns.tolist()
    coef = clf.feature_importances_
    coef = list(zip(cols, coef))
    df_coef = pd.DataFrame.from_records(coef)
    print('Coefficients: \n', df_coef.T)
    df_coef.T.to_csv("randomforest_regression.csv")
    return clf


def lasso_regression(X_train, y_train, X_test, y_test, normalize=False):
    print("-------------------------- Lasso Regression")
    clf = linear_model.LassoCV(alphas=np.arange(0.1, 2, 0.1), max_iter=5000)
    clf.fit(X_train, y_train)

    # 使用测试集进行预测
    y_pred = clf.predict(X_test)

    # 拦截
    print("Intercept: %.4f" % clf.intercept_)
    # 均方误差
    print("Mean squared error: %.2f"
          % mean_squared_error(y_test, y_pred))
    # 解释方差得分：1 是完美预测
    print('Coefficient of determination(R^2): %.2f' % r2_score(y_test, y_pred))
    # 系数
    cols = X_train.columns.tolist()
    coef = clf.coef_.tolist()
    coef = list(zip(cols, coef))
    df_coef = pd.DataFrame.from_records(coef)
    print('Coefficients: \n', df_coef.T)
    print('Alpha: \n', clf.alpha_)

    return clf


def ridge_regression(X_train, y_train, X_test, y_test, normalize=False):
    print("-------------------------- Ridge Regression")
    #clf = Ridge(alpha=1.50, max_iter=5000)
    clf = RidgeCV(alphas=np.arange(0.1, 2, 0.1))
    clf.fit(X_train, y_train)

    # Make predictions using the testing set
    y_pred = clf.predict(X_test)

    # The intercept
    print("Intercept: %.4f" % clf.intercept_)
    # The mean squared error
    print("Mean squared error: %.2f"
          % mean_squared_error(y_test, y_pred))
    # Explained variance score: 1 is perfect prediction
    print('Coefficient of determination(R^2): %.2f' % r2_score(y_test, y_pred))
    # The coefficients
    cols = X_train.columns.tolist()
    coef = clf.coef_.tolist()[0]
    coef = list(zip(cols, coef))
    df_coef = pd.DataFrame.from_records(coef)
    print('Coefficients: \n', df_coef.T)
    print('Alpha: \n', clf.alpha_)
    df_coef.T.to_csv("ridge_regression.csv")

    return clf


def linear_regression(X_train, y_train, X_test, y_test, normalize=False):
    print("-------------------------- Linear Regression")
    # Create linear regression object
    regr = linear_model.LinearRegression(normalize=normalize)

    # Train the model using the training sets
    regr.fit(X_train, y_train)

    # Make predictions using the testing set
    y_pred = regr.predict(X_test)

    # The intercept
    print("Intercept: %.4f" % regr.intercept_)
    # The mean squared error
    print("Mean squared error: %.2f"
          % mean_squared_error(y_test, y_pred))
    # Explained variance score: 1 is perfect prediction
    print('Coefficient of determination(R^2): %.2f' % r2_score(y_test, y_pred))
    # The coefficients
    cols = X_train.columns.tolist()
    coef = regr.coef_.tolist()[0]
    coef = list(zip(cols, coef))
    df_coef = pd.DataFrame.from_records(coef)
    print('Coefficients: \n', df_coef.T)
    df_coef.T.to_csv("linear_regression.csv")

    return regr


    # Plot outputs
    # plt.scatter(X_test, y_test, color='black')
    # plt.plot(X_test, y_pred, color='blue', linewidth=3)
    #
    # plt.xticks(())
    # plt.yticks(())
    #
    # plt.show()

def stepwise_selection(X, y,
                       initial_list=[],
                       threshold_in=0.01,
                       threshold_out = 0.05,
                       verbose=True):
    """ 根据 statsmodels.api.OLS 中的 p 值执行前向后向特征选择
    参数：
        X - pandas.DataFrame 具有候选特征
        y - 与目标类似的列表
        initial_list - 开始的功能列表（X 的列名）
        threshold_in - 包含一个特征，如果它的 p 值 < threshold_in
        threshold_out - 如果 p 值 > threshold_out，则排除特征
        verbose - 是否打印包含和排除的顺序
    返回：选定功能列表
    始终设置 threshold_in < threshold_out 以避免无限循环。
    See https://en.wikipedia.org/wiki/Stepwise_regression for the details
    """
    included = list(initial_list)
    while True:
        changed=False
        # 向前迈进
        excluded = list(set(X.columns)-set(included))
        new_pval = pd.Series(index=excluded)
        for new_column in excluded:
            model = sm.OLS(y, sm.add_constant(pd.DataFrame(X[included+[new_column]]))).fit()
            new_pval[new_column] = model.pvalues[new_column]
        best_pval = new_pval.min()
        if best_pval < threshold_in:
            best_feature = new_pval.argmin()
            included.append(best_feature)
            changed=True
            if verbose:
                print('Add  {:30} with p-value {:.6}'.format(best_feature, best_pval))

        # 倒退一步
        model = sm.OLS(y, sm.add_constant(pd.DataFrame(X[included]))).fit()
        # 使用除截距以外的所有系数
        pvalues = model.pvalues.iloc[1:]
        worst_pval = pvalues.max() # 如果 pvalues 为空，则为 null
        if worst_pval > threshold_out:
            changed=True
            worst_feature = pvalues.argmax()
            included.remove(worst_feature)
            if verbose:
                print('Drop {:30} with p-value {:.6}'.format(worst_feature, worst_pval))
        if not changed:
            break
    return included


def main(args=None):
    df = load_data(csvfile, hasheader=True)
    df = df[(df["CP"] != 0) & (df["TP"] != 0)]
    print(len(df))

    # describe_data(df[CSV_COLUMNS], CSV_COLUMNS)

    #result = correlation_test(df[CSV_COLUMNS])
    #print(result)


if __name__ == '__main__':
    main()

```



## 代码执行

### SparkCity/shell/

#### landsat_aws.txt

```scala
LC08_L1TP_015033_20180318_20180403_01_T1
LC08_L1TP_015033_20180318_20180318_01_RT
LC08_L1TP_015033_20171228_20180103_01_T1
LC08_L1TP_015033_20171228_20171228_01_RT
LC08_L1TP_015033_20171126_20171206_01_T1
LC08_L1TP_015033_20171126_20171126_01_RT
LC08_L1TP_015033_20171025_20171107_01_T1
LC08_L1TP_015033_20171025_20171025_01_RT
LC08_L1TP_015033_20170923_20171013_01_T1
LC08_L1TP_015033_20170923_20170923_01_RT
LC08_L1TP_015033_20170907_20170926_01_T1
LC08_L1TP_015033_20170907_20170907_01_RT
LC08_L1TP_015033_20170822_20170912_01_T1
LC08_L1TP_015033_20170822_20170822_01_RT
LC08_L1TP_015033_20170518_20170525_01_T1
LC08_L1TP_015033_20170518_20170518_01_RT
LC08_L1TP_015033_20170502_20170515_01_T1
LC08_L1TP_015033_20170416_20170501_01_T1
```
 
#### landsat_download.sh

```sh
#! /bin/bash

#LANDSAT_NAME="LC08_L1TP_015033_20170822_20170822_01_RT"
#OUTPUT_DIR="/var/lib/hadoop-hdfs/SparkCity/data/$LANDSAT_NAME"
#mkdir -p $OUTPUT_DIR

download_landsat()
{
    LANDSAT_NAME=$1
    OUTPUT_DIR="$2/$1"
    mkdir -p $OUTPUT_DIR

    wget http://landsat-pds.s3.amazonaws.com/c1/L8/015/033/$LANDSAT_NAME/"$LANDSAT_NAME"_B1.TIF -P $OUTPUT_DIR
    wget http://landsat-pds.s3.amazonaws.com/c1/L8/015/033/$LANDSAT_NAME/"$LANDSAT_NAME"_B2.TIF -P $OUTPUT_DIR
    wget http://landsat-pds.s3.amazonaws.com/c1/L8/015/033/$LANDSAT_NAME/"$LANDSAT_NAME"_B3.TIF -P $OUTPUT_DIR
    wget http://landsat-pds.s3.amazonaws.com/c1/L8/015/033/$LANDSAT_NAME/"$LANDSAT_NAME"_B4.TIF -P $OUTPUT_DIR
    wget http://landsat-pds.s3.amazonaws.com/c1/L8/015/033/$LANDSAT_NAME/"$LANDSAT_NAME"_B5.TIF -P $OUTPUT_DIR
    wget http://landsat-pds.s3.amazonaws.com/c1/L8/015/033/$LANDSAT_NAME/"$LANDSAT_NAME"_B6.TIF -P $OUTPUT_DIR
    wget http://landsat-pds.s3.amazonaws.com/c1/L8/015/033/$LANDSAT_NAME/"$LANDSAT_NAME"_B7.TIF -P $OUTPUT_DIR
    wget http://landsat-pds.s3.amazonaws.com/c1/L8/015/033/$LANDSAT_NAME/"$LANDSAT_NAME"_B8.TIF -P $OUTPUT_DIR
    wget http://landsat-pds.s3.amazonaws.com/c1/L8/015/033/$LANDSAT_NAME/"$LANDSAT_NAME"_B9.TIF -P $OUTPUT_DIR
    wget http://landsat-pds.s3.amazonaws.com/c1/L8/015/033/$LANDSAT_NAME/"$LANDSAT_NAME"_B10.TIF -P $OUTPUT_DIR
    wget http://landsat-pds.s3.amazonaws.com/c1/L8/015/033/$LANDSAT_NAME/"$LANDSAT_NAME"_B11.TIF -P $OUTPUT_DIR
    wget http://landsat-pds.s3.amazonaws.com/c1/L8/015/033/$LANDSAT_NAME/"$LANDSAT_NAME"_BQA.TIF -P $OUTPUT_DIR

    hadoop fs -put $OUTPUT_DIR /SparkCity/data/
}

while read p; do
    echo $p
    download_landsat $p ../data/
done < "landsat_files.txt"




```
 
#### landsat_hdfs_fullpath

```scala
/SparkCity/data/LC08_L1TP_015033_20180318_20180403_01_T1/LC08_L1TP_015033_20180318_20180403_01_T1_r-g-nir-tirs1-swir1.tif
/SparkCity/data/LC08_L1TP_015033_20171228_20180103_01_T1/LC08_L1TP_015033_20171228_20180103_01_T1_r-g-nir-tirs1-swir1.tif
/SparkCity/data/LC08_L1TP_015033_20171126_20171206_01_T1/LC08_L1TP_015033_20171126_20171206_01_T1_r-g-nir-tirs1-swir1.tif
/SparkCity/data/LC08_L1TP_015033_20171025_20171107_01_T1/LC08_L1TP_015033_20171025_20171107_01_T1_r-g-nir-tirs1-swir1.tif
/SparkCity/data/LC08_L1TP_015033_20170923_20171013_01_T1/LC08_L1TP_015033_20170923_20171013_01_T1_r-g-nir-tirs1-swir1.tif
/SparkCity/data/LC08_L1TP_015033_20170907_20170926_01_T1/LC08_L1TP_015033_20170907_20170926_01_T1_r-g-nir-tirs1-swir1.tif
/SparkCity/data/LC08_L1TP_015033_20170822_20170912_01_T1/LC08_L1TP_015033_20170822_20170912_01_T1_r-g-nir-tirs1-swir1.tif
/SparkCity/data/LC08_L1TP_015033_20170518_20170525_01_T1/LC08_L1TP_015033_20170518_20170525_01_T1_r-g-nir-tirs1-swir1.tif
/SparkCity/data/LC08_L1TP_015033_20170502_20170515_01_T1/LC08_L1TP_015033_20170502_20170515_01_T1_r-g-nir-tirs1-swir1.tif
/SparkCity/data/LC08_L1TP_015033_20170416_20170501_01_T1/LC08_L1TP_015033_20170416_20170501_01_T1_r-g-nir-tirs1-swir1.tif
```
 
#### landsat_hdfs_prefix

```scala
/SparkCity/data/LC08_L1TP_015033_20180318_20180403_01_T1/LC08_L1TP_015033_20180318_20180403_01_T1
/SparkCity/data/LC08_L1TP_015033_20171228_20180103_01_T1/LC08_L1TP_015033_20171228_20180103_01_T1
/SparkCity/data/LC08_L1TP_015033_20171126_20171206_01_T1/LC08_L1TP_015033_20171126_20171206_01_T1
/SparkCity/data/LC08_L1TP_015033_20171025_20171107_01_T1/LC08_L1TP_015033_20171025_20171107_01_T1
/SparkCity/data/LC08_L1TP_015033_20170923_20171013_01_T1/LC08_L1TP_015033_20170923_20171013_01_T1
/SparkCity/data/LC08_L1TP_015033_20170907_20170926_01_T1/LC08_L1TP_015033_20170907_20170926_01_T1
/SparkCity/data/LC08_L1TP_015033_20170822_20170912_01_T1/LC08_L1TP_015033_20170822_20170912_01_T1
/SparkCity/data/LC08_L1TP_015033_20170518_20170525_01_T1/LC08_L1TP_015033_20170518_20170525_01_T1
/SparkCity/data/LC08_L1TP_015033_20170502_20170515_01_T1/LC08_L1TP_015033_20170502_20170515_01_T1
/SparkCity/data/LC08_L1TP_015033_20170416_20170501_01_T1/LC08_L1TP_015033_20170416_20170501_01_T1
```

### 组合多个波段


 ```scala
 import edu.gmu.stc.analysis.MaskBandsRandGandNIR
 val input = "/SparkCity/data/LC08_L1TP_015033_20170822_20170822_01_RT/LC08_L1TP_015033_20170822_20170822_01_RT"
 val output = "/SparkCity/data/LC08_L1TP_015033_20170822_20170822_01_RT/LC08_L1TP_015033_20170822_20170822_01_RT_r-g-nir-tirs1-swir1-test.tif"
 MaskBandsRandGandNIR.combineBands(input, output)
 ```
 
 ```scala
 import edu.gmu.stc.analysis.MaskBandsRandGandNIR
 val landsatTxtPath = landsat_hdfs_prefix
 MaskBandsRandGandNIR.combineBands(sc, landsatTxtPath)
 ```
 
### 建立索引

 ```scala
 import edu.gmu.stc.analysis.BuildShapeFileIndex
 val hconf = "/var/lib/hadoop-hdfs/SparkCity/config/conf_lst_cluster.xml"
 val inputDir = "/SparkCity/data/"
 BuildShapeFileIndex.buildIndex(sc, hconf, inputDir + "/dc")
 BuildShapeFileIndex.buildIndex(sc, hconf, inputDir + "/va")
 BuildShapeFileIndex.buildIndex(sc, hconf, inputDir + "/md")
 ```

### 计算光谱指数

```scala 
import edu.gmu.stc.analysis.ComputeSpectralIndex
val landsatTiff = "/SparkCity/data/LC08_L1TP_015033_20170416_20170501_01_T1/LC08_L1TP_015033_20170416_20170501_01_T1_r-g-nir-tirs1-swir1.tif"
val time = "20170416"
val outputDir = "/SparkCity/data"
val hConfFile = "hdfs://svr-A3-A-U2:8020/SparkCity/config/conf_lst_cluster.xml"
ComputeSpectralIndex.computeInCluster(sc, landsatTiff, time, outputDir, hConfFile)

ComputeSpectralIndex.computeSpectralIndexInParallel(sc, "/SparkCity/landsat_hdfs_fullpath", "/SparkCity/data", "hdfs://svr-A3-A-U2:8020/SparkCity/config/conf_lst_cluster.xml")
```

### 配置文件，没找到：

不同土地利用类型的地表温度 LST for different landuse type

```scala
val hconf = "/var/lib/hadoop-hdfs/SparkCity/config/conf_lst_cluster.xml"

val hConfFile = "hdfs://svr-A3-A-U2:8020/SparkCity/config/conf_lst_cluster.xml"
```
 
 


## 其他资源

 * [FRAGSTATS：分类地图的空间模式分析程序](https://www.umass.edu/landeco/research/fragstats/fragstats.html)
 * [大气校正参数计算器](https://atmcorr.gsfc.nasa.gov/)

