## flink CEP的感性认识

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4ytsfdg32a40.png)

复杂事件处理（CEP, Complex Event Processing）

他就像是一个正则表达式一样，从一串串流动的数据中，按照规则提取所需的数据进行加工处理。

CEP其实就是一个`规则引擎`，把符合规则的所有数据都拉出来。

实时逻辑判断

## flink CEP的构成（模式pattern + 模式类型.如next）

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1m7mio7f9ksg.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6ne2t74y8sc0.png)

需要注意：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4qlt1o3atlk0.png)

## 如何使用flink CEP?

### 第一步. 定义事件模式(Pattern就是我们定义的正则表达式)

定义模式主要有如下 5 个部分组成：

pattern：前一个模式

next/followedBy/...：开始一个新的模式

start：模式名称

where：模式的内容

filter：核心处理逻辑

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4i9osde3y7e0.png)


#### optional和greedy的用法：

```java
//触发2、3、4次,尽可能重复执行
start.times(2, 4).greedy();

//触发0、2、3、4次,尽可能重复执行
start.times(2, 4).optional().greedy();
```

#### 定义条件where()、or()、until()

where：
- .where(_.getCallType == "success") 
- .where(_.callType=="success")

or：
- .or(_.duration>10) 

until：
- 如果程序中使用了 oneOrMore 或者 oneOrMore().optional()方法，则必须指定终止条件，否则模式中的规则会一直循环下去，如下终止条件通过 until()方法指定
- .oneOrMore.until(_.callOut.startsWith("186")) 

迭代条件:

该条件基于先前接收的事件的属性或子集的统计信息来接收后续事件。

```scala

// 迭代条件可以很强大，尤其是与循环模式相结合,例如：oneOrMore();
middle.oneOrMore()
   .subtype(classOf[SubEvent])
   .where(
       (value, ctx) => {
           lazy val sum = ctx.getEventsForPattern("middle").map(_.getPrice).sum
	   // 如果名称以”foo”开头
           // 同时,如果该模式的先前接收的事件的价格总和+当前事件的价格 < 5.0，
           // 则迭代条件接收名为”middle”的模式的下一个事件：
           value.getName.startsWith("foo") && sum + value.getPrice < 5.0
       }
   )
```

#### 指定时间约束
- .within(Time.seconds(10)); 

#### 超时事件的处理

通过 within 方法，我们的 parttern 规则将匹配的事件限定在一定的窗口范围内。当有超过窗口时间之后到达的 event，我们可以通过在 select 或 flatSelect 中，实现 PatternTimeoutFunction 和 PatternFlatTimeoutFunction 来处理这种情况。

#### 超时触发机制扩展

原生 Flink CEP 中超时触发的功能可以通过 within+outputtag 结合来实现。

但是在复杂的场景下处理存在问题，如下图所示，在下单事件后还有一个预付款事件，想要得到下单并且预付款后超时未被接单的订单，该如何表示呢？ 

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.41jaadegusc0.png)

这种做法的计算结果是会存在脏数据的

因为这个规则不仅匹配到了下单并且预付款后超时未被接单的订单（想要的结果）

同样还匹配到了只有下单行为后超时未被接单的订单（脏数据，没有预付款）

原因是：

- 因为`超时 within` 是控制在`整个规则`上，而不是`某一个状态节点`上，所以不论当前的状态是处在哪个状态节点，`超时`后都会被`旁路输出`。

那么就需要考虑能否通过`时间`来`直接`对`状态转移`做到`精确`的控制，

而不是通过`规则超时`这种曲线救国的方式。

于是乎，在通过消息触发状态的转移之外，需要增加通过`时间触发状态`的转移支持。

要实现此功能，需要在`原来的状态`以及`状态转移`中，增加`时间属性`的概念。

如下图所示，通过 `wait 算子`来得到 `waiting 状态`，然后在 `waiting 状态`上设置一个`十秒`的时间属性以定义一个`十秒`的`时间窗口`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.7usyxu7fahk.png)

#### wait 算子:

对应 NFA 中的 `ignore 状态`，将在没有到达`时间窗口`结束时间时`自旋`，在 ComputationState 中记录 `wait` 的开始时间，在 NFA 的 doProcess 中，将到来的数据与waiting 状态处理，如果到了 waiting 的结束时间，则进行状态转移。

#### 规则动态注入

线上运行的 CEP 中肯定经常遇到规则变更的情况，如果每次变更时都将任务重启、重新发布是非常不优雅的。尤其在营销或者风控这种对实时性要求比较高的场景，如果规则窗口过长（一两个星期），状态过大，就会导致重启时间延长，期间就会造成一些想要处理的异常行为不能及时发现。

> 那么要怎么样做到[规则的动态更新](https://mp.weixin.qq.com/s/4dQYr-RXKBRdrhu6Y5dZdw)和加载呢？

梳理一下整体架构，Flink CEP 是运行在 Flink Job 里的，而规则库是放在外部存储中的。首先，需要在运行的 Job 中能及时发现外部存储中规则的变化，即需要在 Job 中提供访问外部库的能力。其次，需要将规则库中变更的规则动态加载到 CEP 中，即把外部规则的描述解析成 Flink CEP 所能识别的 pattern 结构体。最后，把生成的 pattern 转化成 NFA，替换历史 NFA，这样对新到来的消息，就会使用新的规则进行匹配。

scala:

```scala
val patternStream: PatternStream[Event] = CEP.pattern(input, pattern)
val outputTag = OutputTag[String]("side-output")
val result: SingleOutputStreamOperator[ComplexEvent] = patternStream.select
(outputTag){
 (pattern: Map[String, Iterable[Event]], timestamp: Long) => TimeoutEvent
()
} {
 pattern: Map[String, Iterable[Event]] => ComplexEvent()
}
val timeoutResult: DataStream<TimeoutEvent> = result.getSideOutput(outputTag)
```

### 第二步. 绑定DataStream(DataStream就是正则表达式中待匹配的字符串)

调用 CEP.pattern()，给定输入流和模式，就能得到一个 PatternStream 

scala:

```scala
val input = ...
val pattern = ...
val patternStream = CEP.pattern(input, pattern)
val patternStream = CEP.pattern(loginEventStream.keyBy(_.userId), loginFailPattern)
```

```scala
//cep 做模式检测
val patternStream=CEP.pattern[EventLog(dataStream.keyBy(_.id),pattern)
```

flink 通过DataStream 和 自定义的Pattern进行匹配，生成一个经过过滤之后的DataStream.

### 第三步. 匹配结果处理

提供 `select` 和 `flatSelect` 两种方法从 PatternStream 提取事件结果事件。 

`select`以`(Map<String,List<Result>>)`来接收匹配到的事件序列

scala:

```scala
val loginFailDataStream = patternStream
 .select((pattern: Map[String, Iterable[LoginEvent]]) => {
 val first = pattern.getOrElse("begin", null).iterator.next()
 val second = pattern.getOrElse("next", null).iterator.next()
 Warning(first.userId, first.eventTime, second.eventTime, "warning")
 })
```

flatSelect 方法:

可以返回多条记录，它通过一个 `Collector[OUT]` 类型的参数来将要输出的数据传递到下游。

### 一个示例流程

```scala
val input: DataStream[Event] =  ...
val pattern = Pattern.begin[Event]("start名称").where(_.getId == 42)
  .next("middle名称").subtype(classOf[SubEvent]).where(_.getTemp >= 10.0)
  .followedBy("end名称").where(_.getName == "end")

val patternStream = CEP.pattern(inputDataStream, pattern)

val result: DataStream[Alert] = patternStream.select(createAlert(_))
```

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.17d9eqe2dukg.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1bko7chgpxj.png)

### 为了使用 Flink CEP，我们需要导入依赖

```xml
<dependency>
 <groupId>org.apache.flink</groupId>
 <artifactId>flink-cep_${scala.binary.version}</artifactId>
 <version>${flink.version}</version>
</dependency>
```

### Dewey 计数法

当一个事件到来时，如果这个事件同时符合多个输出的结果集，那么这个事件是如何保存的？

Flink CEP 通过`Dewey 计数法`在多个`结果集`中共享`同一个事件副本`，以实现对事件副本进行资源共享。 

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5dur150wma40.png)

一个案例[上](https://mp.weixin.qq.com/s/v7eSlrdB_F7y954XD4To3g)和[下](https://mp.weixin.qq.com/s/ysp_09-zoz0gyvZfcGNSQw)：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.36yghuft5ak0.png)

## CEP规则引擎案例-简单的报警-http状态码为非200的数量

### 第0步. 创建DataStream. 自定义的source

```java
 public static class MySource implements SourceFunction<Tuple4<String,Long,Integer,Integer>>{

  static int status[] = {200, 404, 500, 501, 301};

  @Override
  public void run(SourceContext<Tuple4<String,Long,Integer,Integer>> sourceContext) throws Exception{
   while (true){
    Thread.sleep((int) (Math.random() * 100));
    // traceid,timestamp,status,response time

    Tuple4 log = Tuple4.of(
      UUID.randomUUID().toString(),
      System.currentTimeMillis(),
      status[(int) (Math.random() * 4)],
      (int) (Math.random() * 100));

    sourceContext.collect(log);
   }
  }

  @Override
  public void cancel(){

  }
 }
```

### 第0步. 创建DataStream. 每秒http状态码为非200的比例-sql语法

```java
// 定义一个sql，每秒http状态码为非200的比例。大于0.7的时候触发报警。
  String sql = "select pv,errorcount,round(CAST(errorcount AS DOUBLE)/pv,2) as errorRate," +
               "(starttime + interval '8' hour ) as stime," +
               "(endtime + interval '8' hour ) as etime  " +
               "from (select count(*) as pv," +
               "sum(case when status = 200 then 0 else 1 end) as errorcount, " +
               "TUMBLE_START(proctime,INTERVAL '1' SECOND)  as starttime," +
               "TUMBLE_END(proctime,INTERVAL '1' SECOND)  as endtime  " +
               "from log  group by TUMBLE(proctime,INTERVAL '1' SECOND) )";
  //通过执行sql，我们获取到了一个Result对象的DataStream,
  Table table = tenv.sqlQuery(sql);
  DataStream<Result> ds1 = tenv.toAppendStream(table, Result.class);
```

### 第一步. 定义事件模式

times()与模式组：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1pl887vynedc.png)

```java
  Pattern pattern = Pattern.<Result>begin("alert").where(new IterativeCondition<Result>(){
    // "alert": Pattern的名称
    
   @Override
   public boolean filter(Result i, Context<Result> context) throws Exception{
    return i.getErrorRate() > 0.7D;
   }
  }).times(3).consecutive().followedBy("recovery").where(new IterativeCondition<Result>(){
    // times(3):    表示要匹配三次，也就是要三次大于0.7.
    模式属性:

    1. 匹配固定次数，times

    2. 匹配1次以上，oneOrMore

    3. 匹配发送多次以上，timesOrMore
    // consecutive: 表示上述的三次匹配要是连续的，比如0.75、0.8、0.78，只有类似这样的数据才能被匹配到，中间不能有不符合的数据出现。
    // followedBy:  是宽松匹配，也就是两个模式之间可以有其他的数据
    // followedBy:  表示该alert pattern的下面要跟着一个recovery pattern
   @Override
   public boolean filter(Result i, Context<Result> context) throws Exception{
    // filter + throws Exception + return 
    return i.getErrorRate() <= 0.7D;
   }
  }).optional();
    //optional:     指定要么不触发要么触发指定的次数。
    //greedy：      在 Pattern 匹配成功的前提下，会尽可能多地触发。
```

### 第二步. 绑定DataStream + 第三步. 匹配结果处理

```java
DataStream<Map<String,List<Result>>> alertStream = org.apache.flink.cep.CEP.pattern(ds1,pattern).
// pattern(ds1,pattern): 绑定DataStream
  select(new PatternSelectFunction<Result,Map<String,List<Result>>>(){
   @Override
   public Map<String,List<Result>> select(Map<String,List<Result>> map) throws Exception{
// ----------------调用报警接口进行处理----------------这只是简单的打印出来信息。
    List<Result> alertList = map.get("alert");
    List<Result> recoveryList = map.get("recovery");

    if (recoveryList != null){
     System.out.print("接受到了报警恢复的信息，报警信息如下：");
     System.out.print(alertList);
     System.out.print("  对应的恢复信息：");
     System.out.println(recoveryList);
    } else {
     System.out.print("收到了报警信息 ");
     System.out.print(alertList);
    }

    return map;
// ----------------调用报警接口进行处理----------------这只是简单的打印出来信息。
   }
  });
```

## CEP规则引擎案例-在10秒钟之内连续两个event的温度超过阈值

### 第一步. 定义事件模式pattern(warningPattern)

```java
// Warning pattern: Two consecutive temperature events whose temperature is higher than the given threshold
// appearing within a time interval of 10 seconds
Pattern<MonitoringEvent, ?> warningPattern = Pattern.<MonitoringEvent>begin("first")
// "first": 名称
 .subtype(TemperatureEvent.class)
// subtype: 来限制 event 的子类型：
 .where(new IterativeCondition<TemperatureEvent>() {
  private static final long serialVersionUID = -6301755149429716724L;

  @Override
  public boolean filter(TemperatureEvent value, Context<TemperatureEvent> ctx) throws Exception {
  // filter + throws Exception + return 
  return value.getTemperature() >= TEMPERATURE_THRESHOLD;
  }
 })
 .next("second")  
 //紧接着上一个事件
 
 .subtype(TemperatureEvent.class)
 .where(new IterativeCondition<TemperatureEvent>() {
  private static final long serialVersionUID = 2392863109523984059L;

  @Override
  public boolean filter(TemperatureEvent value, Context<TemperatureEvent> ctx) throws Exception {
  // filter + throws Exception + return 
  return value.getTemperature() >= TEMPERATURE_THRESHOLD;
  }
 })
 .within(Time.seconds(10));
```

### 第二步. warningPattern绑定DataStream(inputEventStream.keyBy("rackID")) -> tempPatternStream

使用报警模式和输入流生成模式流

```java
// Create a pattern stream from our warning pattern
PatternStream<MonitoringEvent> tempPatternStream = CEP.pattern(
 inputEventStream.keyBy("rackID"),
 warningPattern);
```

### 第三步. 匹配结果处理 tempPatternStream -> warnings

使用`select方法`为每个匹配的报警模式生成相应的`报警`。其中

`返回值`是一个`map`，`key`是我们定义的`模式`，`value`是匹配的`事件列表`。

我们最后生成了相应的用于警告的DataStream类型的数据流warnings

```java
DataStream<TemperatureWarning> warnings = tempPatternStream.select(
 (Map<String, List<MonitoringEvent>> pattern) -> {
  TemperatureEvent first = (TemperatureEvent) pattern.get("first").get(0);
  TemperatureEvent second = (TemperatureEvent) pattern.get("second").get(0);

  return new TemperatureWarning(first.getRackID(), (first.getTemperature() + second.getTemperature()) / 2);
  }
);
```

### 第一步. 定义事件模式-警报模式(alertPattern)

```java
// 警报模式：在 20 秒的时间间隔内连续出现两次温度警告
Pattern<TemperatureWarning, ?> alertPattern = Pattern.<TemperatureWarning>begin("first")
 .next("second")
 .within(Time.seconds(20));
```

### 第二步. alertPattern绑定DataStream(warnings.keyBy("rackID")) -> alertPatternStream

然后通过上面的`报警模式alertPattern`和`警告流warnings`生成我们的报警流`alertPatternStream`。

```java
// 从我们的警报模式创建模式流
PatternStream<TemperatureWarning> alertPatternStream = CEP.pattern(
 warnings.keyBy("rackID"),
 alertPattern);
```

### 第三步. 匹配结果处理 alertPatternStream -> alerts

最后当收集到的两次警告中，第一次警告的平均温度<第二次的时候，生成报警，封装TemperatureAlert信息返回。

```java
// 仅当第二次温度警告的平均温度>第一次警告的温度时,才生成温度警报
DataStream<TemperatureAlert> alerts = alertPatternStream.flatSelect(
 (Map<String, List<TemperatureWarning>> pattern, Collector<TemperatureAlert> out) -> {
  TemperatureWarning first = pattern.get("first").get(0);
  TemperatureWarning second = pattern.get("second").get(0);

  if (first.getAverageTemperature() < second.getAverageTemperature()) {
  out.collect(new TemperatureAlert(first.getRackID()));
  }
 },
 TypeInformation.of(TemperatureAlert.class));
```

最后我们将报警流和警告流输出，当然我们也可以对这两个流做其他的操作，比如发到报警系统等。

```java
   // 将警告和警报事件打印到标准输出
        warnings.print();
        alerts.print();
```

## CEP规则引擎案例-连续三次登录失败（next方法为连续事件）

```java
import com.alibaba.fastjson.JSONObject;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;
import java.util.Map;

public class LoginFailWithCep {
    private static final JSONObject jsonLoginEvent = new JSONObject();
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        // 1. 从文件中读取数据
        /**
         * 5402,83.149.11.115,success,1558430815
         * 23064,66.249.3.15,fail,1558430826
         * 5692,80.149.25.29,fail,1558430833
         * 7233,86.226.15.75,success,1558430832
         */
        DataStream<JSONObject> loginEventStream = env.readTextFile("/opt/study/Data/LoginLog2.csv")
                .map(line -> {
                    String[] fields = line.split(",");
                    jsonLoginEvent.put("userId", fields[0]);
                    jsonLoginEvent.put("ip", fields[1]);
                    jsonLoginEvent.put("loginState", fields[2]);
                    jsonLoginEvent.put("timestamp", fields[3]);
                    return jsonLoginEvent;
                })
                .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<JSONObject>(Time.seconds(3)) {
                    @Override
                    public long extractTimestamp(JSONObject element) {
                        return new Long(element.getString("timestamp")) * 1000;
                    }
                });

        //1、定义一个匹配模式
        Pattern.<JSONObject>begin("firstFail").where(new SimpleCondition<JSONObject>() {
            @Override
            public boolean filter(JSONObject jsonObject) throws Exception {
                return "fail".equals(jsonObject.getString("loginState"));
            }
        }).next("secondFail").where(new SimpleCondition<JSONObject>() {
            @Override
            public boolean filter(JSONObject jsonObject) throws Exception {
                return "fail".equals(jsonObject.getString("loginState"));
            }
        }).next("thirdFail").where(new SimpleCondition<JSONObject>() {
            @Override
            public boolean filter(JSONObject jsonObject) throws Exception {
                return "fail".equals(jsonObject.getString("loginState"));
            }
        }).within(Time.seconds(3));

        Pattern<JSONObject, JSONObject> loginFailPattern = Pattern.<JSONObject>begin("failEvents").where(new SimpleCondition<JSONObject>() {
            @Override
            public boolean filter(JSONObject jsonObject) throws Exception {
                return "fail".equals(jsonObject.getString("loginState"));
            }
        }).times(3).consecutive()
                .within(Time.seconds(5));

        //2、将匹配模式应用到数据流上，得到一个pattern stream
        PatternStream<JSONObject> patternStream = CEP.pattern(loginEventStream.keyBy(json -> json.getString("userId")), loginFailPattern);

        //3、检出符合匹配条件的复杂事件，进行转换处理，得到报警信息
        SingleOutputStreamOperator<JSONObject> warningStream = patternStream.select(new LoginFailMatchDetectWaring());

        warningStream.print();
        env.execute("login fail detect with cep job");

    }
    //实现自定义的LoginFailMatchDetectWaring
    //public interface PatternSelectFunction<IN, OUT> extends Function, Serializable {
    public static class LoginFailMatchDetectWaring implements PatternSelectFunction<JSONObject, JSONObject>{

        @Override
        public JSONObject select(Map<String, List<JSONObject>> pattern) throws Exception {
            JSONObject firstFailEvent = pattern.get("failEvents").get(0);
            JSONObject lastFailEvent = pattern.get("failEvents").get(pattern.get("failEvents").size() - 1);
            jsonLoginEvent.put("userId2", firstFailEvent.getString("userId"));
            jsonLoginEvent.put("firstFailEvent", firstFailEvent.getString("timestamp"));
            jsonLoginEvent.put("lastFailEvent", lastFailEvent.getString("timestamp"));
            jsonLoginEvent.put("info2", "login fail " + pattern.get("failEvents").size() + " times");
            return jsonLoginEvent;
        }
    }
}
```

## CEP规则引擎案例-订单超时15分钟（followedBy方法为间隔事件）

```java


import com.alibaba.fastjson.JSONObject;
import org.apache.flink.cep.CEP;
// 处理匹配的结果主要有四个接口：PatternFlatSelectFunction，PatternSelectFunction，PatternFlatTimeoutFunction 和 PatternTimeoutFunction。
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.PatternTimeoutFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.OutputTag;

import java.util.List;
import java.util.Map;


public class OrderPayTimeout {
    private static final JSONObject jsonOrderEvent = new JSONObject();
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);

        // 读取数据并转换成POJO类型
        /**
         * 34763,create,,1558430936
         * 34764,create,,1558430937
         * 34763,pay,aaaaaa,1558431136
         * 34764,pay,,1558432936
         */
        DataStream<JSONObject> orderEventStream = env.readTextFile("/opt/study/Data/OrderLog2.csv")
                .map( line -> {
                    String[] fields = line.split(",");
                    jsonOrderEvent.put("userId", fields[0]);
                    jsonOrderEvent.put("orderState", fields[1]);
                    jsonOrderEvent.put("orderId", fields[2]);
                    jsonOrderEvent.put("timestamp", fields[3]);
                    return jsonOrderEvent;
                } )
                .assignTimestampsAndWatermarks(new AscendingTimestampExtractor<JSONObject>() {
                    @Override
                    public long extractAscendingTimestamp(JSONObject element) {
                        return new Long(element.getString("timestamp")) * 1000;
                    }
                });
        //1、定义一个带时间限制的模式
        Pattern<JSONObject, JSONObject> orderPayPattern = Pattern
                .<JSONObject>begin("create").where(new SimpleCondition<JSONObject>() {
            @Override
            public boolean filter(JSONObject jsonObject) throws Exception {
                return "create".equals(jsonObject.getString("orderState"));
            }
        })
                .followedBy("pay").where(new SimpleCondition<JSONObject>() {
                    @Override
                    public boolean filter(JSONObject jsonObject) throws Exception {
                        return "pay".equals(jsonObject.getString("orderState"));
                    }
                }).within(Time.minutes(15));


        //2.定义侧输出流标签，用来表示超时事件
        OutputTag<JSONObject> orderTimeoutTag = new OutputTag<JSONObject>("order-timeout") {};

        //3.将pattern应用到输入数据流上，得到pattern stream
        PatternStream<JSONObject> patternStream = CEP.pattern(orderEventStream.keyBy(json -> json.getString("orderId")), orderPayPattern);

        //4.调用select方法，实现对匹配复杂事件和超时复杂事件的提取和处理
        SingleOutputStreamOperator<JSONObject> resultStream = patternStream.select(orderTimeoutTag, new OrderTimeoutSelect(), new OrderPaySelect());

        resultStream.print("payed normally");
        resultStream.getSideOutput(orderTimeoutTag).print("timeout");
        env.execute("order timeout detect job");
    }
    //实现自定义的超时事件处理函数
    public static class OrderTimeoutSelect implements PatternTimeoutFunction<JSONObject, JSONObject>{

        @Override
        public JSONObject timeout(Map<String, List<JSONObject>> pattern, long timeoutTimestamp) throws Exception {

            String timeoutOrderId = pattern.get("create").iterator().next().getString("orderId");
            jsonOrderEvent.put("createtimeoutOrderId", timeoutOrderId);
            jsonOrderEvent.put("createtime", "timeout " + timeoutOrderId);
            return jsonOrderEvent;
        }
    }
    //实现自定义的正常匹配事件处理函数
    public static class OrderPaySelect implements PatternSelectFunction<JSONObject, JSONObject>{

        @Override
        public JSONObject select(Map<String, List<JSONObject>> pattern) throws Exception {
            String payedOrderId = pattern.get("pay").iterator().next().getString("orderId");
            jsonOrderEvent.put("paytimeoutOrderId", payedOrderId);
            jsonOrderEvent.put("paytime", "payed");
            return jsonOrderEvent;
        }
    }
}
```

## CEP规则引擎案例-网络安全

`CEP规则引擎`可分为`实时`和`离线`两种分析模式

需要经历如下步骤：

- 转化成`事件处理语言（EPL）`
- 将`EPL`集成到`CEP规则引擎`中并执行，`EPL`将`事件检测逻辑`定义出来，然后将`EPL`加载到`CEP规则引擎`中

基于Flink和Siddhi开发`CEP规则引擎`，是实时事件处理的典型案例，其事件处理流程的架构如图:

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5mizadwto280.png)

### Flink CEP

是基于Flink计算框架衍生出的一个计算算子，是实时复杂事件处理的解决方案，它利用`NFA（非确定有限自动机）`对象进行状态管理；

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1x4795cm4jts.png)

点分为起始状态、中间状态、最终状态三种，边分为 take、ignore、proceed 三种。 

take：
- 必须存在一个条件判断，当到来的消息满足 take 边条件判断时，把这个消息放入结果集，将状态转移到下一状态。

ignore：
- 当消息到来时，可以忽略这个消息，将状态自旋在当前不变，是一个自己到自己的状态转移。 

proceed：
- 又叫做状态的空转移，当前状态可以不依赖于消息到来而直接转移到下一状态。举个例子，当用户购买商品时，如果购买前有一个咨询客服的行为，需要把咨询客服行为和购买行为两个消息一起放到结果集中向下游输出；如果购买前没有咨询客服的行为，只需把购买行为放到结果集中向下游输出就可以了。 也就是说，如果有咨询客服的行为，就存在咨询客服状态的上的消息保存，如果没有咨询客服的行为，就不存在咨询客服状态的上的消息保存，咨询客服状态是由一条 proceed 边和下游的购买状态相连。
- 😁我猜测，proceed和optional()有点像。

使用`CEP规则引擎`内置的算子来表示:->数据安全威胁检测场景

- 文件传输日志：短时间内下载大量文件。
- Web访问日志：某设备突然登陆若干不同的账号，偏离以往日常行为；

等等。。。。。。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2m39y49fqb60.png)

### [Siddhi](https://github.com/siddhi-io/siddhi)

是一种轻巧、易集成的开源CEP引擎，它识别事件流中需要关注的特征事件，并进行实时分析处理，Siddhi与流框架Flink能友好集成，并获得不错的处理性能。

Siddhi可以作为嵌入式Java和`Python`库运行，可以作为裸机、VM和Docker上的微型服务运行，也可以在Kubernetes中运行。

根据[Siddhi快速入门指南](https://siddhi.io/en/v5.1/docs/quick-start/)，在几分钟内开始Siddhi。

有关使用Siddhi的更多信息，请参阅[Siddhi文档](https://siddhi.io/en/v5.1/docs/)。

## 一个Flink-Cep使用案例+Groovy+Aviator 来实现一个物联网监控规则

### 技术背景简介

Flink-Cep 

- 是flink中的高级library，用于进行复杂事件处理，例如某一类事件连续出现三次就触发告警，可以类比Siddhi、Esper；

Groovy 

- 是一种`动态脚本语言`，可以让用户输入代码变成后台`可执行代码`，像刷题网站leetcode 应该就是用了这么类似的一个东西；

Aviator 

- 用于执行`求值表达式`，例如求1>2的值，得到true，为什么用这个东西，也跟后续`动态规则变更`相关，接下来的案例也会具体介绍。

### 案例分析

物联网通常都是设备数据，比喻说设备的温度、耗电量等等，会有对设备的监控，

例如求：

设备连续三个点的值 > 10

且

三个点的求和值 > 100，

要求将这三个点发送到下游进行处理，

首先看一下直接使用Flink-Cep api的实现：pattern

```scala
case class  DpData(dpId:String,value:Double)
val pattern=Pattern.begin("start",AfterMatchSkipStrategy.skipPastLastEvent()).where(new SimpleCondition[DpData] {
      override def filter(value: DpData): Boolean = value.value>10
    }).times(2).consecutive()
      // 先使用start的Pattern通过times(2) 与 consecutive 来限定连续两个点的值大于10
      .next("next").where(new IterativeCondition[DpData] {
      // 然后在使用一个next的Pattern
      override def filter(value: DpData, ctx: IterativeCondition.Context[DpData]): Boolean =      {
        if(value.value>10) {
          // 限定输入数值大于10
          val sum=value.value+ctx.getEventsForPattern("start").map(_.value).sum
	  // 求得满足start-Pattern的数据值与当前点数值的和大于100
          return sum>100
        }
        return false
      }
    })
```

但是在实际中，特别是在面向`C端用户`或者是`监控类`的每个业务都有自己的`监控阈值`，

因此`规则`会是一个不断`动态变更`的过程，通常会定义一个`规则`模板，`模板`里面的`条件`是`可动态变更`的。

`用户定义的Pattern`在flink里面会被解析成为NFA(代表了一个匹配的流程)，`NFA生成`是`不可更改`的，所以要想NFA可变，就要求`Pattern可动态生成`，然后去`替换程序里面的NFA`，所以我们就需要`Groovy`这样的`脚本语言`能够`动态生成Pattern对象`，

对于规则里面的条件`value.value>10`， 对于规则配置来说就是一个`条件表达式`，要是`条件表达式`可执行可使用`Aviator`。

### 实现-定义 规则模板-Pattern模板通过Groovy定义

```scala

val groovyScript=
      """
import cep.FilterCondition
import cep.SumIterativeCondition
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.cep.nfa.aftermatch.AfterMatchSkipStrategy
where1=new FilterCondition("_script_","_fieldName_") 
where2=new SumIterativeCondition(_sum_,"_script_","_fieldName_")
def getPattern(){
return Pattern.begin("start",AfterMatchSkipStrategy.skipPastLastEvent()).where(where1).times(2).consecutive().next("next").where(where2)
}
      """.stripMargin
```

在这里面的 `_script_`、`_fieldName_`、`_sum_` 全部都是参数，需要做变量替换:

FilterCondition:

```scala
where1=new FilterCondition("_script_","_fieldName_")
```

替换成为了:

```scala

where1=new FilterCondition("getValue(data)>10","value")
```

表示从流数据里面value字段要求其值大于10。

解析这个groovy脚本，执行其 getPattern 方法获取我们需要的规则定义对象：

groovyScript:

```scala
val factory = new ScriptEngineManager();
val engine =  factory.getEngineByName("groovy");
engine.eval(groovyScript);
val p = engine.asInstanceOf[Invocable].invokeFunction("getPattern").asInstanceOf[Pattern[DpData,DpData]]
```

现在重点看一下FilterCondition 定义，表示的一个自定义继承SimpleCondition的实现：

```java

public class FilterCondition extends SimpleCondition<Map<String,Object>> {

    private String script;
    private String fieldName;
    public FilterCondition(String script,String fieldName){
        this.script=script;
        this.fieldName=fieldName;
        //加载自定义的函数:ParseValueFunction
        AviatorEvaluator.addFunction(new ParseValueFunction(this.fieldName));
    }
    //filter 方法表示的是条件判断
    @Override public boolean filter(Map<String,Object> value) throws Exception {
        Map<String,Object> params=new HashMap<>();
        params.put("data",value);
        return (Boolean) AviatorEvaluator.execute(script,params);
    }
}
```

ParseValueFunction 表示的是一个`Aviator自定义函数`，就是上述提到的`getValue函数`，

它的目的是:

解析`流数据`里面的`具体字段数值`，这里面就是解析`value字段`的值：

ParseValueFunction:解析fieldName

```java

class ParseValueFunction extends AbstractFunction{

    private String fieldName; //value
    public ParseValueFunction(String fieldName){
        this.fieldName=fieldName;
    }
    @Override public String getName() {
        return "getValue"; //定义函数名称
    }
    //env 就是上述的params 入参，arg1表示的就是 data参数
    @Override public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {

        Map<String,Object> map= (Map<String,Object>)FunctionUtils.getJavaObject(arg1,env);
        Double value=Double.valueOf((String)map.get(fieldName));
        return AviatorDouble.valueOf(value);
    }
}
```

理解了这些之后，在看第二个Pattern条件where2实现就比较清楚了:

```java

public class SumIterativeCondition extends IterativeCondition<HashMap<String,Object>> {

    private double sum;
    private String script;
    private String fieldName;

    public SumIterativeCondition(double sum,String scrpit,String fieldName){
       this.sum=sum;
       this.script=scrpit;
       this.fieldName=fieldName;
    }

    @Override public boolean filter(HashMap<String,Object> value, Context<HashMap<String,Object>> ctx) throws Exception {

        Map<String,Object> params=new HashMap<>();
        params.put("data",value);

        if((Boolean) AviatorEvaluator.execute(script,params)){
            double sumNow= Double.valueOf((String)value.get(fieldName))+ StreamSupport.stream(ctx.getEventsForPattern("start").spliterator(),false)
                    .map(x->Double.valueOf((String)value.get(fieldName))).reduce((acc,item)->{
                        return acc+item;
                    }).orElse(0.0);
            return sumNow>sum;
        }
        return false;
    }
}
```

至此一个简单的Flink-cep+Groovy+Aviator实现已经完成。