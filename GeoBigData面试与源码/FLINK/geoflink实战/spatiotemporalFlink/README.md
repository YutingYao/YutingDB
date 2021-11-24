# stFlink library
Support for spatio-temporal queries over data streams using the Apache Flink Streaming API, Table API and SQL.

# Apache Flink 1.4.1 build instructions

Base Apache Flink version - latest stable release 1.4.1
* Source[http://www.apache.org/dyn/closer.lua/flink/flink-1.4.1/flink-1.4.1-src.tgz]
* Binary[http://www.apache.org/dyn/closer.lua/flink/flink-1.4.1/flink-1.4.1-bin-scala_2.11.tgz]

Official documentation:
[https://ci.apache.org/projects/flink/flink-docs-release-1.4/start/building.html]

Following instructions have been tested and are proven to work well on Ubuntu 16.04 Xenial 64bit linux distribution.

## Build Apache Flink 1.4.1 for Scala 2.11

**Prerequisites:**
* Maven 3.3.x
* Scala 2.11.x
* Java JDK 8

**Note:**
* For Maven 3.3.x build has to be done in two steps: First in the base directory, then in the distribution project.

**Build Steps:** 

* Start build in the base direktory:

`flink-1.4.1> mvn clean install -DskipTests`

* Separately build the distribution project:

`flink-1.4.1> cd flink-dist`

`flink-1.4.1/flink-dist> mvn clean install`

# stFlink Library build instructions

## Code structure

Source code is divided in several packages:

* **hr.fer.stflink.core** - spatio-temporal data types, discrete model implementation
* **hr.fer.stflink.queries** - example queries (Q1-Q5) implemented over different Apache Flink APIs
  * **hr.fer.stflink.queries.streaming_api** - implementation using the Apache Flink Streaming API
  * **hr.fer.stflink.queries.table_api** - implementation using the Apache Flink Table API
  * **hr.fer.stflink.queries.sql** - implementation using the Apache Flink SQL

## Running the examples

### 1. Update stFlink library's pom.xml file 

Update stFlink library's pom.xml file and set its mainClass property to the desired query - e.g. to run query Q1 implemented over the Table API:

```
<transformers>
   <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
      <mainClass>hr.fer.stflink.queries.table_api.Q1</mainClass>
   </transformer>
</transformers>
```

### 2. Build stFlink library source

Navigate to stFlink's library root folder (`<repo root>/stFlink`) and run the following command:

`mvn clean package -Pbuild-jar`

Resulting .jar file (`stFlink-1.0-SNAPSHOT.jar`) can be found in the `<stFlink root folder>/target` folder

### 3. Run selected query over the GeoLife dataset

Download the dataset (bigdata.txt) from [here](https://drive.google.com/open?id=0B5iQrw8ThlP0MjBVcHhmUUw5YTA) and store it somewhere locally.

**Console 1: Run Apache Flink local instance and wait for the output**

* Navigate to your Apache Flink 1.4.1 installation *bin* folder (`<apache flink 1.4.1 source folder>/build-target/bin`)
* Run Apache Flink 1.4.1 local instance:

`./start-local.sh`

* Query output is logged in Flink's log file, so use the same console to wait for the output:

`tail -f <apache flink 1.4.1 source folder>/build-target/log/flink-<username>-taskmanager-0-ubuntu.out`

**Console 2: Read GeoLife dataset**

* Read GeoLife dataset and redirect it's output to the local socket (127.0.0.1:9999):

`cat bigdata.txt | (sleep 7; while true; do read buf; echo $buf; sleep 0.01; done) | nc -lk 9999'`

We use delay of 7 seconds to give some time for Apache Flink to start the query.

**Console 3: Run selected query**

* Navigate to Apache Flink bin folder (`<apache flink 1.4.1 source folder>/build-target/bin`) and run previously built .jar file:

`flink run <stFlink root folder>/target/stFlink-1.0-SNAPSHOT.jar`

Query results can be vieved in real-time in Console 1.


















