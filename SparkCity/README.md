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

## 代码设计

### SparkCity/application/src/main/scala/edu/gmu/stc/analysis/

edu.gmu.stc.analysis

#### BuildShapeFileIndex.scala 

#### ComputeLST_LowAccuracy.scala 更好地支持读写tiff

#### ComputeSpectralIndex.scala 重构计算谱指数

#### MaskBandsRandGandNIR.scala 建筑指数小幅修订

#### RelationMining.scala 关系挖掘.scala 优化数据管道

#### SpatialJoin.scala

#### BuildShapeFileIndex.scala


### SparkCity/application/src/main/scala/edu/gmu/stc/hdfs/

#### HdfsUtils.scala


### SparkCity/application/src/main/scala/edu/gmu/stc/raster/io/

#### GeoTiffReaderHelper.scala


### SparkCity/application/src/main/scala/edu/gmu/stc/vector/io/

#### ShapeFileReaderHelper.scala


### SparkCity/application/src/main/scala/edu/gmu/stc/raster/landsat/

#### Calculations.scala

#### ComputeLSTPolygons.scala

#### CreateLSTPng.scala

#### CreateNDVIPng.scala

#### CreateNDWIPng.scala

#### IngestImage.scala

#### Serve.scala


### SparkCity/application/src/main/scala/edu/gmu/vector/landscape/

#### ComputeLandscape.scala

### SparkCity/application/src/main/scala/edu/gmu/stc/vector/

#### VectorUtil.scala


### SparkCity/application/src/main/scala/edu/gmu/stc/raster/operation/

#### RasterOperation.scala


### SparkCity/application/src/main/scala/edu/gmu/stc/vector/operation/

#### OperationUtil.scala

#### Overlap.scala



### SparkCity/application/src/main/scala/edu/gmu/stc/vector/osm/

#### FeatureClass.scala


### SparkCity/application/src/main/scala/edu/gmu/stc/vector/rdd/

#### index/IndexOperator.scala

#### GeometryRDD.scala
 
#### ShapeFileMetaRDD.scala

### SparkCity/application/src/main/scala/edu/gmu/stc/vector/serde/

#### HadoopConfigurationSerde.scala
 
#### VectorKryoRegistrator.scala

### SparkCity/application/src/main/scala/edu/gmu/stc/vector/sparkshell/

#### Application.scala
 
#### GeoSpark_OverlapTest.scala
 
#### OverlapShellExample.scala

#### STC_BuildIndexTest.scala
 
#### STC_OverlapTest_V1.scala
 
#### STC_OverlapTest_v2.scala
 
#### STC_OverlapTest_v3.scala
 
#### SpatialJoin.scala




### SparkCity/application/src/main/scala/edu/gmu/stc/vector/examples/

#### GeoSparkExample.scala
 
#### OverlapExample.scala
 
#### ShapeFileMetaTest.scala


### SparkCity/viz/src/main/scala/org/datasyslab/geosparkviz/showcase/

#### ScalaExample.scala


### SparkCity/sql/src/main/scala/org/apache/spark/sql/geosparksql/strategy/join/

#### DistanceJoinExec.scala
 
#### JoinQueryDetector.scala
 
#### RangeJoinExec.scala
 
#### TraitJoinQueryExec.scala


### SparkCity/sql/src/main/scala/org/apache/spark/sql/geosparksql/expressions/

#### AggregateFunctions.scala
 
#### Constructors.scala
 
#### Functions.scala
 
#### Predicates.scala


### SparkCity/sql/src/main/scala/org/apache/spark/sql/geosparksql/UDT/

#### GeometryUDT.scala
 
#### IndexUDT.scala
 
#### UdtRegistratorWrapper.scala



### SparkCity/sql/src/main/scala/org/datasyslab/geosparksql/UDF/

#### UdfRegistrator.scala


### SparkCity/sql/src/main/scala/org/datasyslab/geosparksql/UDT/

#### UdtRegistrator.scala


### SparkCity/sql/src/main/scala/org/datasyslab/geosparksql/utils/
 
#### Adapter.scala
 
#### GeoSparkSQLRegistrator.scala
 
#### GeometrySerializer.scala
 
#### IndexSerializer.scala


### SparkCity/core/src/main/scala/org/datasyslab/geospark/monitoring/

#### GeoSparkListener.scala
 
#### GeoSparkMetric.scala
 
#### GeoSparkMetrics.scala

### SparkCity/core/src/main/scala/org/datasyslab/geospark/showcase/

#### ScalaEarthdataMapperRunnableExample.scala
 
#### ScalaExample.scala
 
#### SpatialJoinShp.scala


### SparkCity/python/analysis/


#### __init__.py
 
#### basic_statistics.py
 
#### correlation_block_level.py
 
#### correlation_poi_level.py
 
#### correlation_test.py
 
#### csv_util.py
 
#### linear_regression.py



## 代码执行

### SparkCity/shell/

#### landsat_aws.txt
 
#### landsat_download.sh
 
#### landsat_hdfs_fullpath
 
#### landsat_hdfs_prefix

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
 
 


## 资源

 * [FRAGSTATS：分类地图的空间模式分析程序](https://www.umass.edu/landeco/research/fragstats/fragstats.html)
 * [大气校正参数计算器](https://atmcorr.gsfc.nasa.gov/)


## 计算




最高温度区域的位置 Locations for highest-temperature area
 