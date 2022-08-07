# java

[经典鸡翅](https://space.bilibili.com/386498238) 这些视频比较高级，没有工作经验，看不太懂

[设计源于生活中](https://space.bilibili.com/484405397) 代码题

## 强平衡⼆叉树和弱平衡⼆叉树有什么区别

https://www.bilibili.com/video/BV1oF411M78u

## Maven中Package和Install的区别

https://www.bilibili.com/video/BV1PT4y1a7A1

## K8S

https://www.bilibili.com/video/BV1vP4y1J7EQ

## 系统保护机制

https://www.bilibili.com/video/BV1UY411b7W8

## ES的理解

https://www.bilibili.com/video/BV16h41147jy

## Docker

https://www.bilibili.com/video/BV1xR4y1W7Nh

## ArrayBlockQueue

https://www.bilibili.com/video/BV17A4y197em

## 说一下你熟悉的设计模式？

创、结、行

按照模式的【应用目标】分类：

1. 创建型：对【对象创建过程】的各种问题 and 解决方案 の 一个总结。
2. 结构型：对【软件设计结构】的总结。重点关注【类、对象继承、组合方式】的实践经验的总结
3. 行为型：【从类】or【对象】之间的【交互】，【职责划分】等角度，总结的模式

## 在学习【框架】或【中间件】底层源码遇到的设计模式？

[【23种设计模式全解析】终于有人用一个项目将23中设计模式全部讲清楚了](https://www.bilibili.com/video/BV19g411N7yx)

1. 创建型：
      1. 工厂模式
      2. 单例模式
      3. 建造者模式
      4. 原型模式
2. 结构型：
      1. 适配器模式
      2. 桥接模式
      3. 过滤器模式
      4. 组合模式
      5. 装饰器模式
      6. 外观模式
      7. 享元模式
      8. 代理模式
3. 行为型：
      1. 责任链模式
      2. 命令模式
      3. 解释器模式
      4. 迭代器模式
      5. 中介者模式
      6. 备忘录模式
      7. 观察者模式
      8. 状态模式
      9. 空对象模式
      10. 策略模式
      11. 模板模式
      12. 访问者模式

https://www.bilibili.com/video/BV1M44y137oe

https://www.bilibili.com/video/BV1UF411u7pm

https://www.bilibili.com/video/BV1zg411Z7AH

1. `单例模式`：保证被创建一次，节省系统开销。
2. `工厂模式`（简单工厂、抽象工厂）：解耦代码。
3. `观察者模式`：定义了对象之间的一对多的依赖，这样一来，当一个对象改变时，它的所有的依赖者都会收到通知并自动更新。
4. `外观模式`：提供一个统一的接口，用来访问子系统中的一群接口，外观定义了一个高层的接口，让子系统更容易使用。
5. `模版方法模式`：定义了一个算法的骨架，而将一些步骤延迟到`子类`中，模版方法使得`子类`可以在不改变算法结构的情况下，重新定义算法的步骤。
6. `状态模式`：允许对象在内部状态改变时改变它的行为，对象看起来好像修改了它的类。

## 时间轮

https://www.bilibili.com/video/BV1uv4y1u7Xm

## 对网络四元组的理解

https://www.bilibili.com/video/BV1GT4y1i7T2

## 什么是服务网格？

https://www.bilibili.com/video/BV1nS4y1N7Ax

## 你的项目中有什么亮点？

https://www.bilibili.com/video/BV1NZ4y1e7ux



## && 和 & 的区别

https://www.bilibili.com/video/BV1sQ4y1B74C

`A && B` 为短路运算，只要 A 为 false，那么 B 就不要算了，所以效率更高



## BigDecimal

用于：金融场景，防止精度丢失

参考：
https://www.bilibili.com/video/BV1RS4y1P7f5






## main 方法可以被其它方法调用吗

of course！

```java
class Main {
    public static void main(String[] args) {
        A.main(args);
        B.main(args);
    }
}

class A {
    public static void main(String[] args) {
        System.out.println("A");
    }
}

class B {
    public static void main(String[] args) {
        System.out.println("B");
    }
}

```





## final 关键字

```java
final 指向【引用对象】：

final User user = new User(); 不能变更对象指向的对象
❌ user = new User();         不能变更对象指向的对象
⭕ user.id = 1;               对象的【成员属性】是可以修改的
```

```java
final 指向【基本类型】：

final int num = 0; 
❌ num = 1;
```

```java
final 指向【类】：

final class Father {}
❌ class Son extends Father {}
```

```java
final 指向【方法】,该【方法】不能被子类override：

class Father {
    public final void foo() {
        // ...
    }
}
class Son extends Father {
    ❌ @Override
    ❌ public final void foo() {
        // ...
    ❌ }
}
```




## SimpleDateFormat 是线程安全的吗

https://www.bilibili.com/video/BV1zS4y1x7qD

## java 8 改进了之前的 DATE 的烂设计

```java
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
```

## java 的 字符串 拼接

```java
String[] names = {"A", "B", "C", "D"};
StringJoiner sj = new StringJoiner(",", "[", "]");
for (String name : names) {
    sj.add(name);
}
System.out.println(sj); // 输出：[A,B,C,D]
```

## 数组是不是对象？

yes！

```java
引用后，变量也会同步改变。
int[] arr1 = {1, 2, 3, 4, 5};
int[] arr2 = arr1;
arr2[0] = 5;
System.out.println(arr1[0]); 
// 输出5
System.out.println(arr1 instanceof Object); 
// 输出true
```

## Java到底是值传递还是引用传递？

Java 只有`值传递`

```java
public static void main(String[] args) {
    Person p =  new Person("张三");
    fun(p);
    System.out.println("实参：" + p);
}

public static void fun(Person p) {
    p =  new Person("李四");
    System.out.println("形参：" + p);
}

打印结果：
形参：Person{name='李四'}
实参：Person{name='张三'}
```

```java

public static void main(String[] args) {
    Person p =  new Person("张三");
    fun(p);
    System.out.println("实参：" + p);
}

public static void fun(Person p) {
    p.name("李四");
    System.out.println("形参：" + p);
}

打印结果：
形参：Person{name='李四'}
实参：Person{name='李四'}
```

## string 是不可变的

```java
// Person 是可变的
Person P = new Person(18);
p.setAge(20);

// String 是不可变的, 所以是【线程安全的】
String s = "RudeCrab";
```

```java
String s = "一键三连";
HashSet<String> set = new HashSet<>();
set.add(s);

假设可以修改，那么此时，set中的“一键三连”就找不到了
s.value = "点赞也行";
```

## 集合

Collection:

- List (ArrayList)
- Queue (LinkedList)(ArrayDeque)
- Set (HashSet)

Map:

- HashMap (LinkedHashMap)

[java中LinkedHashMap和TreeMap是如何保证顺序的？](https://www.bilibili.com/video/BV1e44y1x7GS)


## instanceof

```java
public class Main {
    public static void main(String[] args) {
        int[] array = {2,5,-2};

        if (array instanceof int[]){
            System.out.println("这个对象是 int[] ");
        } else {
            System.out.println("这个对象不是 int[] ");
        }
    }
}
```

## 数组 排序

```java
import java.util.Arrays;
public class Main {
    public static void main(String[] args) {
        int[] array = {2,5,-2};
        Arrays.sort(array);

        for (int i = 0; i < array.length; i++){
            System.out.print(array[i] + "");
        }
    }
}
```


## 数组 删除

```java
import java.util.Arrays;
public class Main {
    public static void main(String[] args) {
        int[] array = {2,5,-2};
        int[] newArray = new int[array.length - 1];
        int deleteIdx = 2 

        for (int i = 0; i < newArray.length; i++) {
            if (i < deleteIdx) {
                newArray[i] = array[i];
            } else {
                newArray[i] = array[i + 1];
            }
        }

        System.out.println(array);
        System.out.println(newArray);
    }
}
```

## 项目中如何规划常量？

https://www.bilibili.com/video/BV1ua4y1a7CT

## 字符串 常量池

https://www.bilibili.com/video/BV1VW411y72d

使得`字符串资源`能够复用，减少资源的浪费

```java
String s1 = "abc";
String s2 = "abc";
System.out.println(s1 == s2);//✌true
```

```java
public static final Person PERSON = new Person(18);

public static void main(String[] args) {
    Person p1 = PERSON;
    Person p2 = PERSON;
    System.out.println(p1 == p2);
}
```

## 字符串 劈开

```java
public class Main {
    public static void main(string[] args) {
        String str = "www-java-com";
        String[] tmp = str.split("-");
        for (int i = 0; i < tmp.length; i++) {
            System.out.println(tmp[i])
        }
    }
}
```

## 字符串 删除

```java
public class Main {
    public static void main(string[] args) {
        String str = "www-java-com";
        System.out.println(str.substring(0,3) + " " + str.substring(4))

    }
}
```

## 字符串 查找

```java
public class Main {
    public static void main(string[] args) {
        String str = "www-java-com";
        int idx = str.indexOf("j")
        if (idx == -1){
            System.out.println("No Find")
        } else {
            System.out.println("Index: " + idx)
        }
    }
}
```



## private 封装

`封装`的好处：

- 它可以对`成员`进行 更精准的控制
- 让 `对象` & `调用者` 解耦

```java
public class User {
    private Long id;
    public Long getID() {
        return id;
    }
    public void setID(Long id) {
        this.id = id;
    }


    private String phone;
    public String getPhone() {
        if (phone == null){
            return ""
        }
        return phone.substring(0,3) + "****" + phone.substring(7,11);
    }
    public void setPhone(String phone) {
        if (phone == null || phone.length() != 11){
            System.err.println("手机号必须为11位")
        } else {
            this.phone = phone;
        }
    }
}

主程序中：
User user = new User();
user.setPhone("123");         // 打印错误提示
user.setPhone("13700001234"); // 设置成功
System.out.println(user.getPhone()); // 打印 137****1234
```

## public

如果a包下的`A类`是`public`的，它的字段和方法都是private的。

→ 在`b包`下的`B类`可以创建`A类`的对象，但是无法访问`A类对象的字段和方法`。

如果a包下的`A类``没有修饰符`，它的字段和方法都是private的。

→ 在`a包`下的B类可以创建`A类`的对象，但无法访问A类对象的字段和方法。

→ 在`b包`下的B类无法创建A类的对象。





## enum 和 switch 语句使用

enum枚举的优势：

- 能够在 `编译阶段`，就检查 `每个值的合理性`，并且
- 可以用于 `switch判断`

```java
举个椰子🥥：

public enum Season {
    SPRING,
    SUMMER,
    AUTUMN,
    WINTER
}

public static void fun(Season season){
    switch (season) {
        case SPRING:
            break;
        case SUMMER: 
            break;
        case AUTUMN: 
            break;
        case WINTER: 
            break;
        default:
            break;
    }
}

public static void main(String[] args){
    fun(Season.SPRING);
    fun(Season.WINTER);
    fun(1); // 编译出错
    fun("SUMMER"); // 编译出错
}
```

```java
public enum Season {
    SPRING,
    SUMMER,
    AUTUMN,
    WINTER
}

-- 反编译后代码大体如下：

public final class Season extends Enum {
    public static final Season SPRING = new Season();
    public static final Season SUMMER = new Season();
    public static final Season AUTUMN = new Season();
    public static final Season WINTER = new Season();
    private Season() {} // 防止外部实例化
}
```



## void 

表示`method`不返回任何值。

## non-void 的 method

```java
public static void main(String[] args) {
    double c1 = 6, c2 = 18, c3 = 32;
    double res1 = convert(c1);
    double res2 = convert(c2);
    double res3 = convert(c3);

    System.out.println(res1);
    System.out.println(res2);
    System.out.println(res3);
}

static double convert(double c){
    double res;
    res = 1.8 * c + 32;
    return res;
}
```

## Class中的method - class名称.method名

```java
定义一个 class：
public class Keng{
    static double convert(double c){
        return 1.8 * c + 32;
    }
    static void printTwo(){
        System.out.println(2);
    }
}

Main 部分：
public class Main{
    public static void main(string args[]){
        double f = Keng.convert(40);
        System.out.println(f);
        Keng.printTwo();

        有两种写法：
        printThree();
        Main.printThree();
    }

    static void printThree(){
        System.out.println(3);
    }

}
```

```java
public class Main{
    public static void main(string args[]){
        System.out.println(getTwo());
        int two = getTwo();
        System.out.println(two);

        int attack1 = getAttack(50, 10, 1);
        int attack20 = getAttack(50, 10, 20);
        System.out.println(attack1);
        System.out.println(attack20);
    }
    static int getAttack(int level, int attackGrowth, int initialAttack){
        return level * attackGrowth + initialAttack;
    }
    static int getTwo(){
        return 2;
    }
}
```

## 为什么Java中的main方法必须是 public static void？

必须通过`main方法`才能启动java虚拟机

`main方法`没有被`实例化`过，这时候必须使用`静态方法`，才能被`调用`

## static

https://www.bilibili.com/video/BV1nW41167o1

https://www.bilibili.com/video/BV1zL411s7h9

静态修饰符，代表这个类`固有的`，在这个类里面共享，不需要`new一个实例`

`non-static method 非静态方法` = `instance method 实例方法` = new一个实例

## i++ 和 ++i 的区别

j = i++ - 2 是先算 j 再算 i
j = ++i - 2 是先算 i 再算 j

## 引用类型

自定的class 

数组

## Java 中都有哪些引用类型？

https://www.bilibili.com/video/BV1ST411J7Bk

强引用：发生 gc 的时候不会被回收。
软引用：有用但不是必须的对象，在发生内存溢出之前会被回收。
弱引用：有用但不是必须的对象，在下一次GC时会被回收。
虚引用（幽灵引用/幻影引用）：无法通过虚引用获得对象，用 PhantomReference 现虚引用，虚引用的用途是在 gc 时返回一个通知。

## constructor 是一种特殊的 method, static变量是class内部的共享变量

constructor 没有返回

```java
class ListNode {
    int val;
    ListNode next;
    static int cnt; 不属于instance，属于整个class共享
    ListNode(int x) {
        val = x;
        next = null;
        cnt ++
    }
}

public class Solution {
    public boolean hasCycle(ListNode head) {

        ListNode node1 = new ListNode(0)
        ListNode node2 = new ListNode(0)
        
    }
}
```

```py
class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None

class Solution:
    def hasCycle(self, head: Optional[ListNode]) -> bool:
```

## `instance方法` 中 可以直接调用 `instance变量`

```java
public class Dish {
    instance变量：
    double salt;

    constructor：
    Dish(double inputSalt){
        salt = inputSalt;
    }

    instance方法：
    void taste(double inputTolerance) {
        if (salt > inputTolerance) {
            System.out.println("太咸了")
        } else if (salt < inputTolerance) {
            System.out.println("太淡了")
        } else {
            System.out.println("太好吃了")
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Dish d1 = new Dish(0.1);
        Dish d2 = new Dish(0.4);
        Dish d3 = new Dish(0.8);
        d3.taste(0.5);
    }

}
```

## 字符类型转换 - 小写转大写

```java
public class Main {
    public static void main(string arg[]){
        char c = 'a'
        int encoding = (int) c;
        char capital = (char) (encoding - 32);
        System. out. println(capital)
    }
}

public class Main {
    public static void main(String[] args) {
        char[] charArray = {'a','b','c','e'};
        for (int i = 0; i < charArray.length; i++) {
            int encoding = (int) charArray[i];
            if (encoding >= 97 && encoding < 110) {
                encoding = encoding + 13;
            } else if (encoding >= 110 && encoding < 122) {
                encoding = encoding - 13;
            }
            char secret = (char) encoding;
            charArray[i] = secret;
        }
        System.out.println(charArray);
    }
}
```

## java的特点：

- java要求`文件名`和`公共类名`必须要求一致

```java
比如：`文件名` test.java -> `公共类名` public class test{}
```

- java的参数声明放在前面

```java
public String test(String param){
}
```

- 在传递多参数的时候，java用...

```java
public test(String args...){
  
}
```


## String、StringBuffer 与 StringBuilder 之间区别

https://www.bilibili.com/video/BV1b3411G7gr

https://www.bilibili.com/video/BV1KQ4y1z76p

https://www.bilibili.com/video/BV1G3411c7cV

`String`:

- 不可变
- 操作少量数据，或者不操作数据时使用

`StringBuilder`:(优先选择)

- 可变
- 线程不安全

`StringBuffer`:

- 可变
- 线程安全
- 性能较低





## 线程的创建方式




## 序列化和反序列化的理解?

https://www.bilibili.com/video/BV1Ma411A7n2

https://www.bilibili.com/video/BV1f54y1W7Js

https://www.bilibili.com/video/BV1q5411X7DG

## 什么是 Java 序列化？什么情况下需要序列化？

Java 序列化是为了保存各种对象在内存中的状态，并且可以把保存的对象状态再读出来。
以下情况需要使用 Java 序列化：

想把的内存中的对象状态保存到一个文件中或者数据库中时候；
想用套接字在网络上传送对象的时候；
想通过RMI（远程方法调用）传输对象的时候。

## 什么是序列化？

•序列化：序列化是将对象转化为字节流。

•反序列化：反序列化是将字节流转化为对象。

## 序列化的用途？

•序列化可以将对象的字节序列持久化-保存在内存、文件、数据库中。

•在网络上传送对象的字节序列。

•RMI(远程方法调用)

## 序列化和反序列化

•序列化：java.io.ObjectOutputStream 类的 writeObject() 方法可以实现序列化

•反序列化：java.io.ObjectInputStream 类的 readObject() 方法用于实现反序列化。

## 什么是java序列化，如何实现java序列化？

序列化就是一种用来处理对象流的机制，所谓对象流也就是将对象的内容进行流化。可以对流化后的对象进行读写操作，也可将流化后的对象传输于网络之间。序列化是为了解决在对对象流进行读写操作时所引发的问题。序列化的实现：将需要被序列化的类实现Serializable接口，该接口没有需要实现的方法，implements Serializable只是为了标注该对象是可被序列化的，然后使用一个输出流(如：FileOutputStream)来构造一个ObjectOutputStream(对象流)对象，接着，使用ObjectOutputStream对象的writeObject(Object obj)方法就可以将参数为obj的对象写出(即保存其状态)，要恢复的话则用输入流。

## Java中的序列化和反序列化

https://www.bilibili.com/video/BV1wL4y1q7z5



## 连接池

https://www.bilibili.com/video/BV1yR4y1K7Z7

## Netty

https://www.bilibili.com/video/BV1GN4y1g7Wi

https://www.bilibili.com/video/BV1xq4y1g73f

## 请说一下对象的创建过程？

https://www.bilibili.com/video/BV1c44y1P7Xx

## SPI

https://www.bilibili.com/video/BV1MB4y1X7Dy

## seata

https://www.bilibili.com/video/BV18Y411g7kP

## Kafka如何保证消息不丢失？

https://www.bilibili.com/video/BV1W34y1x7tY

## Kafka 怎么避免重复消费？

https://www.bilibili.com/video/BV1XS4y1B7Mk

## kafka的零拷贝原理

https://www.bilibili.com/video/BV1tu411B7p9


## 接口的幂等性？什么是幂等、如何解决幂等性问题？

https://www.bilibili.com/video/BV14a411a7SH

## zk の watch机制实现原理

https://www.bilibili.com/video/BV1sY4y1Y71P

https://www.bilibili.com/video/BV1D3411w7wK

## 简述zk の 命名服务、配置管理、集群管理

https://www.bilibili.com/video/BV1r3411F7id

## zookeeper的应用场景

https://www.bilibili.com/video/BV1Uq4y137Vh

1. 分布式锁
2. master选举
3. 集群管理

## 阻塞队列被异步消费怎么保持顺序吗？

https://www.bilibili.com/video/BV1PN4y1L7cd

## 生产环境服务器变慢，如何诊断处理？

https://www.bilibili.com/video/BV1Xt4y1t7Vw





## 负载均衡的背景？

在早期，都是【单体架构】，【单体架构】容易出现【单点故障】的问题。之后，引入了集群化的部署架构，把一个【软件应用】同时部署在【多台服务器】上。

## 集群部署带来的两个问题？

1. 【客户请求】如何均匀的分发到【多台目标服务器】上
2. 如何【检测】目标服务器的【健康状态】，使得【客户端请求】不向已经宕机的服务器【发送请求】

## 负载均衡的核心目的？

如何让【client的请求】合理地、均匀地、分发到多台【目标服务器】上。由于【请求】被多个 node 分发，使得【服务器】的性能得到了有效的提升。

## 如何实现【负载均衡】？

1. 负载均衡

只需要在【DNS服务器上】，针对某个【域名】做多个【IP映射】即可。当client通过【域名】向某个网站访问的时候，会先通过【DNS服务器】进行【域名解析】，得到【IP地址】，【DNS服务器】会随机分配一个【IP地址】，这样就能实现【请求的分发】。此外，DNS还能实现【就近访问】。

优点：配置简单，维护成本低
缺点：修改DNS配置时。会因为【缓存】导致【IP变更】不及时

2. 基于 【硬件】 实现负载均衡

基于交换机

优点：支持多种负载均衡算法，有防火墙功能
缺点：硬件负载设备，价格比较贵

3. 基于 【软件】 实现负载均衡

如Nginx。互联网企业采用

优点：免费、开源、灵活性高

## 什么是Java虚拟机为什么要使用？

https://www.bilibili.com/video/BV1Fg411278C

## Jvm垃圾回收器:serial

https://www.bilibili.com/video/BV1rK411u7bk

## JVM 的理解

谈谈你对JVM的理解：

全称 Java虚拟机。

有两个作用：
① 运行并管理【java源码文件】所生成的【class文件】
② 在不同的操作系统安装不同的 JVM，从而实现【跨平台】的保障

对于开发者而言，即使不熟悉JVM的运行机制，也不影响代码的开发。但当程序运行过程中出现了问题，而这个问题发生在JVM层面时，我们就需要去熟悉【JVM的运行机制】，才能迅速排查，并解决JVM的性能问题

JVM的运行流程：

它把一个class文件，通过【class loader subsystem 类加载机制】，装载到JVM里面，然后放到不同的【运行时数据区】，通过【编译器】来编译。
类加载机制 分为：装载、链接、初始化。它主要针对【类】进行【查找、验证】，以及【分配相关内存空间、赋值】

第三个部分：Runtime data areas。也就是我们通常所说的【运行时数据区】。他解决的问题是【class文件】进入【内存】之后，该如何进行【存储】不同的【数据】，以及【数据如何扭转】。

比如：

1. 方法区：存储由【class文件常量池】所对应的【运行时常量池】，【字段】和【方法】的【元数据信息】和【类的模板信息】
2. heap：是存储【实例】
3. Java thread 是通过【线程】已【stack】的方式去运行，加载各个【方法】
4. Native Internal Threads：。。。。。
5. Program Counter Registers： 保存每个线程【执行方法】的【实时地址】，

这样通过【运行时数据区】的【5个part】就能很好地【运行】起来

第四个部分：就是【垃圾回收器】：就是对【运行时数据区】的数据，进行【管理】和【回收】。

可以基于不同的垃圾收集器，比如说，serial、parallel、CMS、G1，可以针对不同的业务场景，去选择不同的收集器。只需要通过【JVM参数】设置即可。

这些收集器，其实就是对于不同的【垃圾收集算法】的实现，核心的算法有3个：

1. 标记 - 清除
2. 标记 - 整理
3. 复制

第五个是 JIT Compiler 和 Interpreter。通俗理解就是【编译器】。
【class字节码指令】通过【JIT Compiler 和 Interpreter】翻译成【对应操作系统】的【CPU指令】，可以选择【解释执行 or 编译执行】。在hotspot编译器中，默认采用这两种方式的组合。

第六种是 JNI技术。如果，我们想找到Java中的某个Native方法，如何通过C或者C++实现。就可以通过Native method interface来进行【查找】

在实际的操作过程中，我们可以借助一些JVM参数，比如:

1. InitialHeapSize
2. NewRatio
3. UseG1GC
4. MaxHeapSize
5. ConcGCThreads

和一些常见的JDK常见命令：

- jps
- jinfo
- jstat
- jhat
- jstack
- jmap

从而优雅地分析JVM出现的常见问题，并对其进行优雅地调优


## JVM性能优化 - 存在哪些问题？

- GC 频繁
- 死锁
- oom
- 线程池不够用
- CPU负载过高

## CPU飙高系统反应慢怎么排查？

https://www.bilibili.com/video/BV1nF41147yu

## JVM性能优化 - 如何排查问题？

1. 打印出 `GC log`，查看 minor GC 和 major GC
2. `jstack` 查看【堆栈信息】
3. 应用 `jps，jinfo，jstat，jmap`等命令

## JVM性能优化 - 如何解决问题？

1. 适当增加【堆内存大小】
2. 选择合适的【垃圾收集器】
3. 使用 zk，redis 实现【分布式锁】
4. 利用 kafka 实现【异步消息】
5. 代码优化，及时释放【资源】
6. 增加集群节点数量

## 分布式锁的理解和实现？

https://www.bilibili.com/video/BV1nS4y1K7n4

https://www.bilibili.com/video/BV1GF411373W

## 栈和栈桢

https://www.bilibili.com/video/BV1YA411J7wE

## JVM中，哪些是共享区，哪些可以作为gc root

https://www.bilibili.com/video/BV1Uq4y1e7Kq

## jvm内存结构介绍

https://www.bilibili.com/video/BV1jY4y1b7n1

Java の 内存分为5个部分：

- 线程共享：
  - 方法区(method area) 🔸 常量池 + 静态变量 + 类
  - 堆(heap) 🔸 GC 对象，即【实例对象】，内存最大 の 区域


- 每个线程一份【线程隔离】：
  - 虚拟机栈(VM stack)→(heap) 🔸 每个java被调用时，都会创建【栈帧】
    - `栈帧`内包含：
      - `局部变量表`
      - `操作数栈`
      - `动态链接`
      - `返回出口`
  - 本地方法栈 🔸 native语言
  - 程序计数器(pc register) 🔸【当前线程】执行到 の 【字节码行号】

[了解JVM内存区域吗？](https://www.bilibili.com/video/BV1Vt4y1V7cz)

[2分钟学会对象和引用如何存储在JVM内存](https://www.bilibili.com/video/BV1S5411Q78u)

## 【pass💦】Java 中有多少种引用类型？它们 の 都有什么使用场景？

https://www.bilibili.com/video/BV1e94y1D7ug

## java内存模型

java存在`线程间如何通信？` の 问题

对于每个【线程】，stack是私有 の ，而heap是共享 の 

也就是说 → 

stack中 の 变量，不会在【线程】间【共享】，也就不会有【内存可见性】 の 问题。也就不会受到【内存模型】 の 影响，

而 → 

heap中 の 变量，是【共享变量】，就会有【内存可见性】 の 问题。

-----------------------------------------------

java【线程】之间 の 通信由JMM控制：

【线程】之间 の 【共享变量】存储在【主内存】中

每个【线程】都有一个私有 の 【本地内存】 → 存储了该【线程】读写【共享变量】 の 副本。

-----------------------------------------------

如果【线程A】要与【线程B】通信：

必须经历下面2个步骤：

【线程A】将【本地内存a】中更新过 の 【共享变量】刷新到【主内存】中，

【线程B】在【本地内存】中，找到这个【共享变量】，发现这个【共享变量】已经被更新了，然后到【主内存】读取【线程A】更新过 の 【共享变量】，并拷贝到【本地内存中】

也就是说 →

线程间通信必须经过【主内存】

【线程】对【共享变量】 の 操作，必须在自己 の 【本地内存】中进行，不能直接从【主内存】中读取。

java内存模型，通过控制【主内存】和【本地内存】之间 の 交互，来提供内存 の 可见性保证。

------------------------------------------------------------

为了更精准控制【主内存】和【本地内存】间 の 交互，JMM 还定义了八种操作：lock, unlock, read, load,use,assign, store, write。

## Java 内存模型定义了八种操作来实现

Java 内存模型定义了八种操作来实现

## CompletableFuture

https://www.bilibili.com/video/BV1hA4y1d7gU

## 【pass💦】为了更好 の 控制【主内存】和【本地内存】 の 交互，Java 内存模型定义了八种操作来实现：

lock：锁定。 主内存 の 变量，把一个变量标识为一条线程独占状态。
unlock：解锁。 主内存变量，把一个处于锁定状态 の 变量释放出来，释放后 の 变量才可以被其他线程锁定。
read：读取。 主内存变量，把一个变量值从主内存传输到线程 の 【工作内存】中，以便随后 の load动作使用
load：载入。 【工作内存】 の 变量，它把read操作从主内存中得到 の 变量值放入【工作内存】 の 变量副本中。
use：使用。 【工作内存】 の 变量，把【工作内存】中 の 一个变量值传递给【执行引擎】，每当虚拟机遇到一个需要使用变量 の 值 の 字节码指令时将会执行这个操作。
assign：赋值。 【工作内存】 の 变量，它把一个从【执行引擎】接收到 の 值赋值给【工作内存】 の 变量，每当虚拟机遇到一个给变量赋值 の 字节码指令时执行这个操作。
store：存储。 【工作内存】 の 变量，把【工作内存】中 の 一个变量 の 值传送到主内存中，以便随后 の write の 操作。
write：写入。 主内存 の 变量，它把store操作从【工作内存】中一个变量 の 值传送到主内存 の 变量中。

## jvm内存中，堆和栈 の 区别？堆和栈 の 关系

https://www.bilibili.com/video/BV1RW411C7yb

|   |  stack |  heap |
|---|---|---|
| 存储  | 局部变量、引用  | instance 对象  |
| 速度  |  fast |  slow |
| 线程共享  | Thread Stack  | 共享 heap  |
| GC |   | GC 对象，占用内存最大  |
| 指向关系 | 出  | 入  |

## 堆内内存

年轻代【Eden、From、To】 + 老年代 + 持久代

## 堆外内存【定义】：

堆外内存 = 物理机内存

【java虚拟机堆】以外的内存，这个区域是受【操作系统】管理，而不是jvm。

## 堆外内存优点

减少jvm垃圾回收

加快数据复制的速度

## Linux查看内存、CPU等状态？Linux查看进程 の 内存消耗和CPU消耗？

查看内存使用情况：`free`

显示进程信息（包括CPU、内存使用等信息）：`top、ps`

## 什么是【虚拟内存】和【物理内存】

物理内存：【内存条】存储容量的大小

虚拟内存：是【计算机内存管理技术】，它让程序认为它具有【连续可用】的【4GB 内存】，而实际上，映射到多个【物理内存碎片】

## HashMap 和 HashTable の 区别

https://www.bilibili.com/video/BV1Dh411J72Y

| HashMap  |  HashTable |
|---|---|
| 非同步  | 线程同步，线程安全，有关键字 synchronized  |
| 允许  | 不允许 k-v 有 null值  |
| hash数组的默认大小是 16 | hash数组的默认大小是 11 |
| hash算法不同：增长方式是 2的指数  | hash算法不同：增长方式是 2*old + 1 |
| 继承 AbstractMap类  | 继承 Dictionary类，Dictionary类 已经被废弃  |

## HashMap的Put方法

https://www.bilibili.com/video/BV1MR4y1F7Jf



## hashmap 是如何解决hash冲突的？

https://www.bilibili.com/video/BV1Y341137uj

## HashTable是如何保证线程安全的？

HashTable 给整张表添加一把【大锁】，把整张表锁起来，大幅度降低了效率。

为了提高效率，引入 ConcurrentHashMap

## 谈谈你对线程安全的理解？

https://www.bilibili.com/video/BV1ei4y1U7HW



## 什么是【分段锁】？

就是将数据【分段】，每段加一把锁








## Hashmap和Treemap の 区别




## 追问：HashMap在哪个jdk版本使用红黑树，之前 の 实现方法是什么？

## hashmap put过程

## JVM如何判断一个对象可以被回收？

https://www.bilibili.com/video/BV1RB4y117ne

## 如何确定一个对象是【垃圾】？

引用计数法：只要【程序】中持有【该对象的引用】，就说明【该对象不是垃圾】

如果一个对象，没有【指针】对其引用，它就是垃圾

- 注意：如果AB互相引用，则会导致【永远不能被回收】，导致【内存溢出】

## JVM分代年龄为什么是15次？

在JVM的【heap内存】里面，分为【Eden Space、Survivor Space、Old generation】，当我们在java里面，去使用【new关键词】去创建一个【对象】的时候，java会在【Eden Space】分配一块内存，去存储这个对象。

当【Eden Space】的内存空间不足时，会触发【Young GC】进行对象的回收，而那些因为存在【引用关系】而无法回收的对象，JVM会把它转移到【Survivor Space】。

【Survivor Space】内部存在【From 区】和【To 区】。那么从【Eden 区】转移过来的对象，会分配到【From 区】.

每当触发一次【Young GC】，那些没有办法被回收的对象，就会在【From 区】和【To 区】来回移动。每移动一次,【GC年龄】就会+1，默认情况下,【GC年龄】达到15的时候，这些对象如果还没有办法【回收】，那么JVM会把这些对象移动到【Old Generation】里面。

一个【对象】的【GC年龄】是存储在【对象头】里面的，而一个【Java对象】在【JVM的内存】布局，由3个部分组成：

1. 对象头
2. 实例数据
3. 对齐填充

而在对象头里面，有【4个bit】来存储【GC年龄】，而4个bit位能够存储的最大数值是15。所以，从这个角度来说，【JVM分代年龄】之所以设置为15，因为，它最大能存储的数值是15。虽然，JVM提供了参数，来去设置【分代年龄】的大小，但是这个大小不能超过15。

此外，JVM还引入了【动态对象年龄】的判断方式，来决定把对象转移到【old generation】，也就是说，不管这个对象的【gc年龄】是否达到了15次，只要满足【动态年龄】的判断依据，也会把这个对象转移到【old generation】。

## 为什么 Eden:S0:S1 是 8:1:1 ?



## 新生代、老年代

Eden From To 老年代

## Minor GC 和 Full GC 有什么不同？

Minor GC / Young GC: 指发生新生代 の 垃圾收集动作
Major GC / Full GC: 指发生新生代 の 垃圾收集动作

## JVM是如何避免Minor GC时扫描全堆的?

https://www.bilibili.com/video/BV1h64y1x7SF

## java GC算法，标记-清楚、标记-复制、标记-整理

## 4.g1回收器、cms の 回收过程，场景

[CMS比较严重 の 问题并发收集阶段再次触发Full gc怎么处理](https://www.bilibili.com/video/BV1BA4y1R7W7)

[G1垃圾收集器最大停顿时间是如何实现 の ](https://www.bilibili.com/video/BV1TB4y1s7rh)

## G1垃圾收集器的【内存布局】

与【其他收集器】有很大的差别

它将【heap】划分成多个大小相等的Region，虽然仍然划分为【Eden、Survivor、Old、Humongous、Empty】

每个 Region 大小，都在 1M ~ 32M 之间，必须是 2的N 次幂

特点：

1. 仍然保留了【分代】的概念
2. 不会导致【空间碎片】
3. 可以明确规定在【M毫秒的时间段】，消耗在【垃圾收集】的时间。不得超过N毫秒。

## 什么情况下使用G1垃圾收集器？

1. 50%以上的heap被live对象占用
2. 【对象分配】和【晋升的速度】变化非常大
3. 垃圾回收【时间】比较长

## 这几个分区在gc里都有什么处理，不同分区 の gc策略

[GC是什么时候都能做吗？知道GC安全点与安全区域是怎么回事吗？](https://www.bilibili.com/video/BV1oZ4y1q7tu)

## spark那些外部资源 还有第三方jar包之类 の 都放在哪（应该是这么问 の ，不太会，说了下内存结构，告诉我是java classloader相关 の 机制）

## java掌握到什么程度







## 内存溢出

https://www.bilibili.com/video/BV1gW411r7By



## 一个空Object对象的占多大空间

https://www.bilibili.com/video/BV1SG411h7ju



##  ArrayList 和 LinkedList 的区别是什么？

https://www.bilibili.com/video/BV1Vu411z7ws

数据结构实现：ArrayList 是动态数组的数据结构实现，而 LinkedList 是双向链表的数据结构实现。
随机访问效率：ArrayList 比 LinkedList 在随机访问的时候效率要高，因为 LinkedList 是线性的数据存储方式，所以需要移动指针从前往后依次查找。
增加和删除效率：在非首尾的增加和删除操作，LinkedList 要比 ArrayList 效率要高，因为 ArrayList 增删操作要影响数组内的其他数据的下标。
综合来说，在需要频繁读取集合中的元素时，更推荐使用 ArrayList，而在插入和删除操作较多时，更推荐使用 LinkedList。

https://www.bilibili.com/video/BV1uA411J7gK

|  ArrayList | LinkedList   |
|---|---|
| 底层是【数组】  | 底层是【链表】  |
| 查询，速度更快  | 增删，速度更快  |
|   | 更占内存  |


https://www.bilibili.com/video/BV1QW41167Gn



## 线程 の 创建方式？

## 多线程了解吗？（当时脑子一抽，我以为要写多线程代码，回答了不熟悉，他就没多问了）



## Objects类中有哪些方法？

## 排序算法和时间复杂度

https://www.bilibili.com/video/BV1Sg411M7Cr



## 类加载机制

就是：把【Class文件】加载到【内存】，进行【验证、准备、解析】，然后初始化，可以形成JVM可以直接使用的java.lang.Class

- 装载
- 链接 (验证、准备、解析)
- 初始化
- 使用
- 卸载

## 【Java面试题】类加载器

|   |   |   |
|---|---|---|
| 4️⃣ Bootstrap ClassLoader  | 【启动】类加载器  | 加载`\jre\lib`的类库  |
| 3️⃣ Ext ClassLoader  | 【拓展】 类加载器 | 加载`\jre\lib\ext`的类库  |
| 2️⃣ App ClassLoader  | 【系统】 类加载器 | 加载`classpath`的类库  |
| 1️⃣ Custom ClassLoader  | 【自定义】 类加载器 | 加载`用户`类库  |

https://www.bilibili.com/video/BV1JW411r758

## 双亲委派机制：

[请介绍类加载过程，什么是双亲委派？](https://www.bilibili.com/video/BV1g94y1d787)

https://www.bilibili.com/video/BV19F411g7B1

https://www.bilibili.com/video/BV1vf4y1B7eQ

自底向上地查看，是否加载过这个类

如果没有，再自顶向下，尝试去加载这个类

[类加载机制和类加载器 双亲委派](https://www.bilibili.com/video/BV17M4y1G7W7)

[类加载机制](https://www.bilibili.com/video/BV1e34y177YY)

https://www.bilibili.com/video/BV1ja411L7k1


## 我们自己定义 の java.lang.String类是否可以被类加载器加载

https://www.bilibili.com/video/BV1MF411e7zY

## String类是否可以被继承

https://www.bilibili.com/video/BV1ev411g7Xk


## 如何创建一个线程？

## java执行 の 过程

## jvm存在 の 意义


## arraylist 和linkedlist 区别，为什么arrylist查询快

https://www.bilibili.com/video/BV1xe4y1973f


## 怎么实现多线程顺序输出下面这，ABC是三个不同 の 线程

## final 值 方法 类有什么区别，容器被final修饰会怎样

[2分钟记住final の 使用](https://www.bilibili.com/video/BV19A4y1o7ET)

## 线程安全 の 单例模式

java集合

介绍一下jvm の 区域

ClassA a=new Class(1) 在jvm中怎么存储



## java方法是值传递还是对象传递

https://www.bilibili.com/video/BV1xL4y1w7jy


## 说一下你对多线程 の 理解

threadlocal oom是为什么

java sout（0.1+0.2）输出什么，为什么

## Java面向对象三大特性

https://www.bilibili.com/video/BV1rf4y1E7u9

## 面向对象 の 【三大特性】

封装、继承、多态

## 说下对象完整创建流程

https://www.bilibili.com/video/BV1J3411G7wA

## new String("abc")到底创建了几个对象？

https://www.bilibili.com/video/BV1MS4y1b75Q

## 什么时候用多态

https://www.bilibili.com/video/BV18W411C7TC

## Java中 の 反射（不了解，没问）

https://www.bilibili.com/video/BV1L34y1S7a1

## 隐含 の 强制类型转换

https://www.bilibili.com/video/BV1Fb4y1Y7wX



Java集合 の 框架体系图



5.遇到oom怎么排查（有jvm提供 の 工具查看堆栈使用情况，具体不太了解只在跑spark遇到过、分享了下spark里 の 排查经验和参数调整）

12.用过多线程吗（了解，没具体使用过，就不问了？？八股我都准备输出了）

## java中类和对象 の 关系

https://www.bilibili.com/video/BV1YZ4y1G7JR

https://www.bilibili.com/video/BV1GR4y1p7qw

## 访问修饰符

https://www.bilibili.com/video/BV1bf4y1c7Pu

## 常用哪些数据集合，介绍hashset

[HashSet内部是如何工作 の ](https://www.bilibili.com/video/BV1sq4y1971k)

## ThreadLocal 是什么？有哪些使用场景？

https://www.bilibili.com/video/BV1Yb4y1s7RG

ThreadLocal 为每个使用该变量的线程提供独立的变量副本，所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
ThreadLocal 的经典使用场景是数据库连接和 session 管理等。

## threadlocal原理，应用场景

https://www.bilibili.com/video/BV1iZ4y127wm


## 垃圾收集器分类

串行收集器：只有一个【垃圾回收线程】，【用户线程】暂停

并行收集器(吞吐量优先)：多个【垃圾回收线程】，【用户线程】暂停

并发收集器(停顿时间优先)(CMS、G1)：【垃圾回收线程】、【用户线程】同时

## CMS 垃圾收集器

全称 Concurrent Mark Sweep

目标：获取【最短回收停顿时间】

特点：

- 采用【标记-清除】算法
- 【内存回收过程】与【用户线程】并发执行

## 垃圾回收

https://www.bilibili.com/video/BV1aW41167rS

https://www.bilibili.com/video/BV1xK4y197SH

[什么样 の 对象会被老年代回收](https://www.bilibili.com/video/BV13K4y1G7Bs)

## 什么时候才会进行垃圾回收？

1. GC 是由 JVM 【自动】完成的。
   1. 当【Eden区 or S区】不够用
   2. 【老年代】不够用
   3. 【方法区】不够用
2. 也可以通过 `System.gc()方法` 【手动】垃圾回收

但【上述两种方法】无法控制【具体的回收时间】




## java の 锁原理

## 手写单例模式

https://www.bilibili.com/video/BV1834y1m7Eq

https://www.bilibili.com/video/BV1Fo4y127zN

https://www.bilibili.com/video/BV1iW411S76k



浏览器从url输入到返回 の 流程

1T文件，每行是一个数字，机器128G内存，求top10数字

java基本数据类型



##  spark executor内 の task是怎么彼此隔离 の （从线程池 の 角度，还有切分stage）

 进程和线程区别，线程和进程切换过程

 线程进程

 线程和进程



进程 の 地址空间

说一下进程和线程

进程之间通信 の 方式

进程切换 の 过程

为什么我们一般都用线程来接收请求（好像是这样问 の ）

## 如何保证线程安全

[List の 线程安全实现有哪些](https://www.bilibili.com/video/BV1ag41177nn)

如何进行etl链路优化

如果链路慢，但是没有sla风险，怎么优化（类似上一道题）

死锁是什么

如何破坏死锁

怎么数据链运行慢定位问题，对任务进行优化

死锁 の 产生条件还有如何避免

1.1 数据库锁表 の 相关处理

## 索引失效场景

https://www.bilibili.com/video/BV1yr4y1E7Xu

https://www.bilibili.com/video/BV1pr4y1p7Ak

## Mysql锁有哪些，如何理解

https://www.bilibili.com/video/BV1ff4y1Z7VQ

## 并发编程三要素？

https://www.bilibili.com/video/BV1tS4y1e7Yr

## 面试被问到并发编程中，如何中断一个正在运行中的线程？

https://www.bilibili.com/video/BV1554y1Z7w5

## 高并发下如何做到安全 の 修改同一行数据，乐观锁和悲观锁是什么，INNODB の 行级锁有哪2种，解释其含义

[并发编程没你想得那么难学，并发学习指南|附学习资料](https://www.bilibili.com/video/BV1br4y1C72X)

##  如何保证MySQL数据库的高可用性？ 

https://www.bilibili.com/video/BV1aL4y1G7jT

1.4 数据库会死锁吗，举一个死锁 の 例子，mysql怎么解决死锁








1.1 hash算法 の 有哪几种，优缺点，使用场景

## 什么是一致性hash

https://www.bilibili.com/video/BV193411u7Lq

Hash表是怎么实现 の 。

去重如果不用 set 还有多少种方式，有没有更高效 の 方式？

怎么解决幻读 具体在sql是怎么实现 の 

## mvcc知道吗

全称 Multi-Version Concurrency Control

多版本并发控制

指的是：

维持一个数据的【多个版本】，使得没有【读写冲突】

目的：

- 提高【数据库】的【并发访问】性能
- 处理【读写冲突】。即使有【读写冲突】，也能【不加锁】，【非阻塞、并发读】

## 【数据库】的三种并发场景：

第一种：【读的并发】，就是两个线程A和B同时进行读操作，这种情况不会产生任何并发问题。

第二种：【读写并发】，就是两个【线程】，A和B同时进行【读写操作】，就会造成如下问题：

1. 事务隔离性问题。
2. 出现【脏读、幻读、不可重复读】的问题

第三种：【写的并发】，就是两个线程A和B同时进行写操作，就会造成如下问题：

1. 数据更新的丢失。

MVCC 就是为了解决【事务操作】过程中【并发安全问题】的【无锁并发控制技术】

## MVCC 实现原理？

通过【数据库记录】中的【隐式字段、Undo日志、Read View】来实现的。

## MVCC主要解决3个问题？

1. 读写并发阻塞的问题。
2. 采用【乐观锁】的方式实现，降低了【死锁】的概率
3. 解决了【一致性】读的问题

## 什么是【一致性】读？

事务启动的时候，根据【某个条件】去【读取数据】，直到【事务结束】再去执行【相同的条件】，还是读到【同一份数据】，不会发生变化

## MVCC 、乐观锁、悲观锁 的区别是什么？

【MVCC】 用来解决【读写冲突】
【乐观锁 or 悲观锁】用来解决【写的冲突】

## 1.1 维度建模有什么好处？如果业务需求增加一个维度，后续需要做哪些工作？


1.2 怎么判断一个需求能不能实现，你们 の 判断标准是什么？需求变更要做什么？

1.3 增加一个维度后发现查询 の 速度变得非常慢，是什么原因导致 の ？


## linux下 の 调查问题思路：内存、CPU、句柄数、过滤、查找、模拟POST和GET请求等等场景

1.6 分表之后想让一个id多个表是自增 の ，效率实现


1.7 事物 の 四个特性，以及各自 の 特点（原子、隔离）等等，项目怎么解决这些问题

## 一条sql语句运行慢，如何做。怎么处理MySQL的慢查询？

（慢查询，看索引，看orderby和groupby）

1. 开启【慢查询】日志，准确定位到【某个sql语句】
2. 分析【sql语句】，看看是否load了【不需要的列】
3. 分析【执行计划】，看其【索引】的使用情况，然后修改【语句or索引】，使得【语句】尽可能命中【索引】
4. 如果上述优化，还是不够。看看表中【数据量】是否过大，可以【横向or纵向】分表

https://www.bilibili.com/video/BV1N34y17715

## 索引 の 设计原则

在进行索引设计的时候，应该：

1. 经常用于 where 或 join 的列
2. 小表没必要建立索引
3. 建立索引的列没必要太多
4. 频繁更新的字段不要有索引

https://www.bilibili.com/video/BV1WB4y1s7kD


## 事务 の 特性，隔离 の 级别。

Mysql事务的实现原理：

MySQL里面的事务是满足【ACID特性】的。
在于InnoDB，是如何保证ACID的特性的。

A ： 需要保证多个【DML操作】的【原子性】，要么都成功，要么都失败。如果失败，那么，对原本执行成功的数据，需要进行【回滚】，所以，在innoDB里面，设计了一个【UNDO_LOG表】。

在【事务】执行的过程中，把【修改之前】的【数据快照】保存到【UNDO_LOG表】里面，一旦出现错误，就直接从【UNDO_LOG表】里面读取数据，进行【反向操作】就行了。

C：就是数据的【完整性约束】没有被破坏。而数据库本身，也提供了像【主键的唯一约束】【字段长度】【类型】的保障

I：表示事务的【隔离性】，多个【并行事务】对同一个数据的操作，互不干扰，从而避免【数据混乱】。InnoDB里面提供了4种隔离级别的实现：

1. RU (未提交读)
2. RC (已提交读)
3. RR (可重复读)（InnoDB默认）
4. Serializable (串行化)

还使用了 【MVCC机制】，解决了【脏读】和【不可重复读】的一个问题，

并且使用了【行锁、表锁】的方式，解决了【幻读】的问题

D：持久性。也就是说，只要事务【提交成功】，那么，对于这个数据的结果的影响，一定是永久的。不能因为【数据库宕机】or 其他原因，导致【数据变更】的失效。

数据发生变更后，先更新【buffer pool】，然后在【合适的时间】，持久化到【磁盘】。

在这个过程中，有可能出现【宕机】，导致数据丢失，无法满足【持久性】。因此，InnoDB引入了【Redo_log】，当我们通过【事务】进行【数据更改】时，除了修改【buffer pool】的数据，还会把修改值追加到【Redo_log】。即使宕机，重启以后，可以直接使用【redo_log】的重新日志，重写一遍，从而保证持久性。

也就是说，MySQL用过【MVCC、行锁、表锁、UNDO_LOG、REDO_LOG】保证了这个特性

## 分布式session中用 の 是什么数据结构

## 简述 tcp 和 udp的区别？

tcp 和 udp 是 OSI 模型中的运输层中的协议。tcp 提供可靠的通信传输，而 udp 则常被用于让广播和细节控制交给应用的通信传输。
两者的区别大致如下：

tcp 面向连接，udp 面向非连接即发送数据前不需要建立链接；
tcp 提供可靠的服务（数据传输），udp 无法保证；
tcp 面向字节流，udp 面向报文；
tcp 数据传输慢，udp 数据传输快；

## TCP的三次握手和四次挥手

https://www.bilibili.com/video/BV1GT4y1r74W

## tcp 为什么要三次握手，两次不行吗？为什么？

https://www.bilibili.com/video/BV1xL4y1F7wL

　我们假设A和B是通信的双方。我理解的握手实际上就是通信，发一次信息就是进行一次握手。

第一次握手：A给B打电话说，你可以听到我说话吗？
第二次握手：B收到了A的信息，然后对A说：我可以听得到你说话啊，你能听得到我说话吗？
第三次握手：A收到了B的信息，然后说可以的，我要给你发信息啦！
在三次握手之后，A和B都能确定这么一件事：我说的话，你能听到；你说的话，我也能听到。这样，就可以开始正常通信了。
注意：HTTP是基于TCP协议的，所以每次都是客户端发送请求，服务器应答，但是TCP还可以给其他应用层提供服务，即可能A、B在建立链接之后，谁都可能先开始通信。

如果采用两次握手，那么只要服务器发出确认数据包就会建立连接，但由于客户端此时并未响应服务器端的请求，那此时服务器端就会一直在等待客户端，这样服务器端就白白浪费了一定的资源。若采用三次握手，服务器端没有收到来自客户端的再此确认，则就会知道客户端并没有要求建立请求，就不会浪费服务器的资源。