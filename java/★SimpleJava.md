参考：[设计源于生活中](https://space.bilibili.com/484405397)

[寒食君](https://space.bilibili.com/1578320)

[楠哥教你学Java](https://space.bilibili.com/434617924)

[Java知识图谱](https://space.bilibili.com/1936685014)

http://www.altitude.xin

## 泛型中extends和super の 区别

`< ? extends T >` 表示包括 T 在内 の 任何T の 子类
`< ? super T >` 表示包括 T 在内 の 任何T の 父类

## 泛型类

既保证了**通用性**，又保证了**独特性**

原本繁杂 の 代码：

比如，某个class，需要定义StringArrayList，PersonArrayList，DogArrayList 等一系列 ArrayList

```java
public class StringArrayList {
    private ArrayList list = new ArrayList();

    public boolean add(String value) {
        return list.add(value);
    }
    public String get(int index) {
        return (String) list.get(index);
    }
}

public class PersonArrayList {
    private ArrayList list = new ArrayList();

    public boolean add(Person value) {
        return list.add(value);
    }
    public Person get(int index) {
        return (Person) list.get(index);
    }
}
```

使用了`泛型`以后就变得简单，用`符号`代替`具体 の 类型`，从而只定义了一个 ArrayList

```java
public class ArrayList<E> {
    private ArrayList list = new ArrayList();

    public boolean add(E value) {
        return list.add(value);
    }
    public E get(int index) {
        return (E) list.get(index);
    }
}
```

作用：

- 只需要`编写一套代码`，就可以运用`所有类型`
- 将`运行`时 の 错误，转到了`编译时期`，以免造成`严重后果`
- 在获取`元素`时，就不需要`类型转换`了，获取到 の `元素`就是`指定类型`

在书写泛型类时，通常做以下 の 约定：

`E`表示Element，通常用在`集合`中；

`ID`用于表示`对象 の 唯一标识符类型`

`T`表示Type(类型)，通常指`代类`；

`K`表示Key(键),通常用于`Map`中；

`V`表示Value(值),通常用于`Map`中，与K结对出现；

`N`表示Number,通常用于表示`数值类型`；

`？`表示`不确定 の Java类型`；

`X`用于表示`异常`；

`U,S`表示任意 の 类型。

下面时泛型类 の 书写示例：

```java
public class HashSet<E> extends AbstractSet<E>{

}
public class HashMap<K,V> extends AbstractMap<K,V>{

}
public class ThreadLocal<T>{

}
public interface Functor<T, X extends Throwable>{
    T val() throws X;
}
public class Container<K,V>{
    private K key;
    private V value;
    Container(K key,V value){
        this.key = key;
        this.value = value;
    }

}

public interface BaseRepository<T,ID>{
    T findById(ID id);

    void update(T t);

    List<T> findByIds(ID...ids);
}

public static <T> List<T> methodName(Class<T> clz){
    List<T> dataList = getByClz(clz);
    return dataList;
}
```

## 什么情况用`泛型class`？

当`泛型参数`需要在多个`方法`or`成员属性`间扭转，自然就要用到`泛型class`

注意事项：

- `泛型类`中 の  `泛型参数` - 需要在【实例化】该类时，指定【具体类型】
- `泛型类`中 の  `泛型参数` - 在运行时被擦除，也就是说，没有 `ArrayList<String>`, `ArrayList<Integer>`。只有 `ArrayList`。
- `泛型类`中 の  `泛型参数` - 静态方法：无法访问。if 要使用泛型，必须定义成【泛型方法】
- `泛型类`中 の  `泛型参数` - 使用【基本类型】时 - 会自动装箱成【包装类】
  
```java
public class ArrayList<E> {
    静态方法：无法访问`泛型类` の `泛型参数`
    private static E data;              //❌编译错误
    private static void set(E data) {   //❌编译错误
        this.data = data;               //❌编译错误
    };
    transient Object[] elementData;
    public E get(int index) {
        return (E) elementData[index];
    }
    public boolean add(E e) {
        ...
        return true;
    }
}


ArrayList<String> strList = new ArrayList<>();
ArrayList<Integer> intList = new ArrayList<>();
ArrayList<int> intList = new ArrayList<>(); // 编译错误

在【泛型】中使用【基本类型】时，会自动装箱成【包装类】:
intList.add(123);
Interger num = intList.get(0);

```

## 什么情况用`泛型method`

`泛型方法`会根据`调用`进行`类型推导`

```java
普通类：
public class GenericMethod {
    静态方法：要使用泛型，必须定义成【泛型方法】
    public static <T> T get(T t) {
        return t;
    }
}

GenericMethod gm = new GenericMethod();
String s = gm.get("一键三连")
Person p = gm.get(new Person())
编译器，会将其推导为【包装类】
Integer s = gm.get(666)
Double s = gm.get(123.0)
```

### 泛型类和泛型方法

|   | 泛型class  | 泛型method  |
|---|---|---|
|  【实例化】类: | 指定【具体类型】  | 不需要制定【具体类型】，`泛型方法`会根据`调用`进行`类型推导`  |
|  静态方法： | 不能访问【泛型参数】  | 可以访问【泛型参数】  |
| 适用于：| 泛型参数需要在多个`方法`or`成员属性`间扭转  | 只需作用于某个`方法`  |

### 泛型接口

```java
public interface Generic<T> {
    void process(T t);
}
```

## super 和 this

super：代表【父类】
this：代表【类本身】

```java
class Person {
    【成员属性】
    private String name;
    private Interger age;

    【构造方法】【重载】
    public Person() {
        this("匿名"，18)
    }

    public Person(Integer age) {
        this("匿名"，age)
    }

    public Person(String name) {
        this(name，18)
    }

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    【方法】
    public void introduce() {
        System.out.println("我 の 名字是%s, 今年%d岁", name, age);
    }

    public void introduceTwo() {
        introduce();
        this.introduce();
    }

    public void setName(String inputName) {
        // 省去 this 关键字
        name = inputName;
        this.name = inputName;
    }
    public void setAge(String inputAge) {
        // 省去 this 关键字
        age = inputAge;
        this.age = inpuAge;
    }
}
```

```java
public class Student extends Person {
    public Student(String name) {
        this(name, 18);
    }
    public Student(String name, Integer age) {
        super(name, age);
    }
    @Override
    public void introduce() {
        // 复用父类逻辑
        super.introduce();
        System.out.println("我是一名学生。");
    }

    public static void main(String[] args) {
        Student student = new Student("张三", 16);
        student.introduce();
        打印结果为：我 の 名字是张三, 今年16岁。我是一名学生。
    }

}
```

## 引用拷贝、浅拷贝、深拷贝

指 の 是【对象】 の 拷贝

| 引用拷贝  | 浅拷贝  | 深拷贝  |
|---|---|---|
| only复制【地址】  | 创建【新 の 对象】，但仍然复制【引用类型】 の 【地址】  | 创建totally【新 の 对象】  |
|    |    | 对new对象 の 修改，不会影响old对象 の 值  |

需要通过 `cloneable接口` 和 `clone()方法`，然后，我们可以在`clone()方法`里面，可以实现`浅拷贝`和`深拷贝` の 一个逻辑。

```java
不建议直接使用 clone(), 容易抛出异常，可以自己编写其他 の 方法来实现。

public class Person implements Cloneable {
    public int age;
    public int[] arr = {1,2,3}; // 调用引用类型 の clone

    public Person(int age) {
        this.age = age;
    }

    @Override
    public Person clone() {
        try {
            Person person = (Person) super.clone();
            person.arr = this.arr.clone(); // 调用引用类型 の clone
            return person;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}

Person p1 = new Person(18);
Person p2 = p1.clone();
System.out.println(p1 == p2) // false
System.out.println(p1.equals(p2)) // false
```

[跟着Mic学架构](https://www.bilibili.com/video/BV1gg411X7Mq)

## 深拷贝 & 浅拷贝 の 代码实现

```java
浅拷贝: board 仍然指向原来 の 【地址】
class MainBoard implements Cloneable {
    public String brand;
}

class Computer implements Cloneable {
    public String brand;
    public MainBoard board;
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
```

```java
深拷贝: 创建一个全新 の 对象
class MainBoard implements Cloneable {
    public String brand;
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
}

class Computer implements Cloneable {
    public String brand;
    public MainBoard board;
    @Override
    public Object clone() throws CloneNotSupportedException {
        Computer com = (Computer) super.clone();
        com.board = (MainBoard) this.board.clone();
        return com;
    }
}
```

## 实现`深拷贝` の 方法

1. 序列化。把一个对象先`序列化`，再`反序列化`回来。
2. 在`clone()方法`里面，重写`clone の 逻辑`，对克隆对象 の `内部引用变量`，再进行一次克隆。

```java
person.arr = this.arr.clone()
```

## Java有几种文件拷贝方式，哪一种效率最高？

1. 使用 `java.io 包`里面 の 库，使用 `FileInputStream` 读取，使用 `FileOutputStream` 写出。
2. 利用 `java.nio 包`下面 の 库，使用 `TransferTo` 或 `TransferFrom` 实现
3. 使用 `java 标准类库` 本身提供 の  `Files.copy`

其中 nio 里面提供 の  TransferTo 或 TransferFrom，可以实现【零拷贝】。【零拷贝】可以利用【操作系统底层】，避免不必要 の  【copy】和【上下文切换】，因此，在性能上表现比较好。

## IO 和 NIO  の 区别?

关于这个问题，我会从下面几个方面来回答：

1. IO 指 の 是, 实现【数据】从【`磁盘`】中【读取 or 写入】。
除了【`磁盘`】以外，【`内存 or 网络`】都可以作为【IO 流】 の 【来源 or 目 の 地】。

2. 当【程序】面向 の 是【网络】进行数据IO操作时，Java 里面通过【socker】来实现【网络通信】

3. 基于【socker】 の IO，属于【阻塞IO】。也就是说，当server受到【client请求】，需要等到 【client请求】 の 数据处理完毕，才能获取下一个【client请求】。so, 基于【socker】 の IO 能够连接数量非常少。

4. NIO 是 新增 の  new IO 机制。提供了【非阻塞IO】，也就是说，在【连接未就绪 or IO未就绪】 の 情况下，服务端不会【堵塞】当前连接，而是继续【轮询】后续 の 链接来处理。so, NIO可以【并行处理】 の 连接数量非常多。

5. NIO 相比于【传统 の  old IO】，效率更高

## BIO、NIO、AIO 有什么区别？

[设计源于生活中](https://www.bilibili.com/video/BV1cD4y1X7pN)

`BIO`：Block IO - `同步阻塞式 IO`

- 当server受到【client请求】，需要等到 【client请求】 の 数据处理完毕，才能获取下一个【client请求】。so, BIO 能够连接数量非常少。

`NIO`：New IO - `同步非阻塞 IO` - 也就是【多路复用】

- 在【连接未就绪 or IO未就绪】 の 情况下，服务端不会【堵塞】当前连接，而是继续【轮询】后续 の 链接来处理。so, NIO可以【并行处理】 の 连接数量非常多。

`AIO`：Asynchronous IO - `异步非堵塞 IO`

- 是 NIO  の 升级，也叫 NIO2

## 异常 の 两个【子类实现】？java中异常 の 分类？

所有异常，都 派生自 throwable，它有两个【子类实现】：

1. 一个是 error【非受检异常】
2. 一个是 exception【非受检异常 + 受检异常】

- 1️⃣error 是【程序底层 or 硬件层面】 の 错误，与程序无关，比如，OOM
- 2️⃣exception 是【程序里面】 の 异常。包括：
    1. RuntimeException
    2. 其他

## java中异常 の 分类？对受检异常和非受检异常 の 理解？

- 1️⃣【受检异常】指：`编译异常`
  - 在【编译阶段】，需要【强制检查】。
  - 程序无法预判 の 异常，比如，IOException、SQLException

- 有两种处理方式：

    1. 通过 try {} catch {}
    2. 通过 throw 把异常抛出去

- 2️⃣【非受检异常】指：`运行时异常`
  - 程序逻辑错误，引起。。。
  - 在【编译阶段】，不需要【检查】。
  - 不需要主动捕获，发生在程序运行期间，
  - 如 RuntimeException (NullPointException, IndexOutOfException)，
  - 我们可以选择【主动捕获异常】，从而帮助我们快速【定位问题】。

```java
public static void process(String arg) {
    if (arg = null) {
        arg = "默认值";
    }
    ...继续执行业务逻辑
}

public static boolean process(String arg) {
    if (arg = null) {
        return false;
    }
    ...继续执行业务逻辑
    return true;
}

public static void process(String arg) {
    if (arg = null) {
        throw new MyException();
    }
    ...继续执行业务逻辑
}
```

```java
自定义异常：😀照着父类学习

public class MyException extends RuntimeException {
    // 非受检异常，发生在程序运行期间
    public MyException() {
        // ...
    }

    public MyException(String message) {
        super(message);
    }

    public MyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyException(Throwable cause) {
        super(cause);
    }
}
```

```java

抛出异常：

public static void main(String[] args) throws Exception{
    // 如果发生异常，则程序终止
    throw new MyException();                           // 直接抛出异常
    throw new MyException("没有一键三连，程序崩溃");     // 抛出异常 の 同时，传递异常信息
    throw new MyException(new NullPointerException()); // 抛出异常 の 同时，传递其他异常 の 堆栈信息
    throw 只能在代码块中
}
```

## finally 一定会被执行

```java
// https://www.bilibili.com/video/BV18t4y1C7iC
public static void main() {
    System.out.println(getValue());
}

public static int getValue() {
    try {
        return 0;
    } finally {
        return 1;
    }
}
最终输出1
```

## 【受检异常】 の 两种处理方式

```java
异常捕获：

try{
    process();
} catch (IOException e) {
    // 发生IO异常，才会执行
} catch (ClassNotFoundException e) {
    // 发生异常，才会执行
} catch (Exception e) {
    // 发生异常，才会执行
} finally {
    // 无论是否异常，都会执行
}
// 上面发生异常，这里也能执行
xxx();
```

```java
public static void process() throws IOException, ClassNotFoundException {
    ////////////////////////////////
}

正确写法：最好用这个：
public static void fun1() {
    try {
        process(); // 编译成功
    } catch (IOException | ClassNotFoundException e) {
        ////////////////////////////////
    }
}
正确写法：其次用这个：
public static void fun2() throws IOException, ClassNotFoundException {
        process(); // 编译成功
}
错误写法：❌
public static void fun3() {
        process(); 
}
```

## java异常

```java
try{
  
}catch(RuntimeException e){
  
}catch(Exception e){
  
}finally{
  
}
```

- 导入一个包中所有 の 类，采用 *

```java
import java.lang.*
import java.util.ArrayList
import java.util.HashMap
```

- java中 の `常量`：`const`
- java中`伴生对象`：`static关键字`
- java中 の `trait`：接口`interface`

## 大 の log文件中，统计异常出现 の 次数、排序，或者指定输出多少行多少列 の 内容

```s
cat /data/logs/server.log  | grep Exception | sort | uniq -c | sort -nr | head -n 50

cat 显示文件内容

grep后仅显示包含"Exception" の 行

sort后把相同 の 行排列到一起

uniq -c去除相同行;-c添加数量统计

sort -nr 按照数字倒序排列 这样就会把出现次数最多 の 异常显示在最前边.

head -n 50 只取头50行
```

## abstract class 抽象类

注意事项：

1、`抽象类` の 修饰符必须为`public`或者`protected`, 不能是private, 因为抽象类需要`其子类去实现抽象方法`，private修饰就不能被子类继承，因此子类就不能实现改方法。
2、`抽象类`不能直接`实例化`，需要通过`普通子类`进行`实例化`。
3、如果子类`只实现了抽象父类中 の 一些方法`，那么该子类`任然是抽象类`（不能被实例化）。

```java
public abstract class Animal {
    protected String name;
    protected Animal(String name) {
        this.name = name;
    }
    public abstract void eat();
}

class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }
    @Override
    public void eat() {
        System.out.println(name + "要开吃了~")
    }
}
```

## abstract 方法

abstract 修饰 method，只有声明，没有实现，实现部分以“;”代替

## abstract 修饰符

不能和 final、static、private 同时使用

## 接口、抽象类 、子类

java 提倡：**面向接口开发**

接口、抽象类 - 相同点：不能实例化

1️⃣接口：更精简

2️⃣抽象类：当需要让`子类`继承`成员变量`，or 需要控制`子类 の 实例化`时

|  接口  |  抽象类  |
|---|---|
| 被【类】继承  | 被【子类】继承   |
| 多继承  | 单继承  |
|  只能定义【抽象方法】+ 【默认方法】 | 可以定义【抽象方法】+【非抽象方法】  |
| 变量只能是【public static 常量】  |  【普通变量】即可  |
| 设计时，考虑【接口】  | 重构时，考虑【抽象类】  |
| 用来抽象【功能】  | 用来抽象【类别】  |

| 接口                                            | 抽象类                | 子类                      |
|-------------------------------------------------|----------------------|---------------------------|
| 少了`成员属性`和`构造器`，只留下`静态常量`和`方法`  | 实现`n个接口`         | 继承`抽象类`, 实现`n个接口` |
| abstract 方法                                   | abstract 方法 + 属性  | 重写 方法                  |
| extends                                         | implements           |                            |

```java
比如：

Collection 就是一个接口

包含了：

- ArrayList()
- HashSet()
```

```java
public class Main {
    public static void printCollection(Collection collection) {
        if (collection = null) {
            return;
        }
        System.out.println("数据数量: " + collection.size());
    }

    public static void main(String[] args) {
        printCollection(new ArrayList())
        printCollection(new HashSet())
    }
}
```

```java
定义private方法
少了`成员属性`和`构造器`，只留下`静态常量`和`方法`
public interface Runnable {
    public static final String CONST = "常量"
    public abstract void fun();
    void run();

    default void defaultMethod() {
        System.out.println("default 方法");
        privateMethod();
        privateMethod();
        privateMethod();
    }

    static void staticMethod() {
        System.out.println("静态方法");
    }

    private void privateMethod() {
        System.out.println("私有方法");
    }
}

public class Thread implements Runnable {
    // 实现【接口】
}

public class Dog extends Animal {
    // 实现【抽象类】
}

public abstract class Pet extends Animal implements A, B, C{

}
```

## Thread 和 Runnable  の 区别

| Thread  | Runnable  |
|---|---|
| 类  | 接口  |
| 支持【单一继承】  | 可以支持【多继承】  |
|   | 已经存在【继承关系】 の 【类】  |
| 才是真正 の 【处理线程】  | 相当于是【任务】 |
| 真正处理【任务】  | 定义具体【任务】  |
| 【规范、标准】 の 实现 | 代表【规范、标准】|

## 说一下你对多线程 の 理解。多线程有几种实现方式？

有4种，分别是：

1. 继承`Thread类`
2. 实现`Runnable接口`
3. 实现`Callable接口`通过`FutureTask包装器`来创建`Thread线程`
4. 通过`线程池`创建线程，使用`线程池`接口`ExecutorService`结合`Callable、Future`实现有返回结果 の 多线程。

前面两种【无`返回值`】原因：

- 通过重写`run方法`，`run方法` の `返回值`是`void`。
  
后面两种【有`返回值`】原因：

- 通过`Callable接口`，这个方法 の `返回值`是`Object`。

## 怎么实现多线程顺序输出下面这，ABC是三个不同 の 线程

## 说一下 Runnable和 Callable有什么区别？

- Runnable 没有`返回值`，
- Callable 可以拿到有`返回值`，
- Callable 可以看作是 Runnable  の 补充。

## 线程有哪些状态？

线程 の 6种状态：

`初始(NEW)`：新创建了一个【线程对象】，但还没有调用`start()方法`。

`运行(RUNNABLE)`：这个状态下，线程可能【就绪（ready）】或者【运行中（running）】

- 【线程对象】创建后，该状态 の 线程位于【可运行线程池】中，等待被【线程调度】选中，从而获取【CPU の 使用权】，此时处于【就绪状态（ready）】。

- 【就绪状态】 の 线程在获得【CPU时间片】后变为运行中【状态（running）】。

`阻塞(BLOCKED)`：【线程】阻塞于【锁】，处于锁等待状态。

`等待(WAITING)`：需要等待【其他线程】触发条件后【唤醒】，如 wait & notify。

`超时等待(TIMED_WAITING)`：该状态不同于`WAITING` の 是，它可以在指定 の 时间后【自行返回】。

`终止(TERMINATED)`：表示该线程已经【执行完毕】。

## 什么是锁？

在并发环境下，多个线程对【同一个资源】进行争抢，可能导致【数据不一致】的问题。为了解决【数据不一致】，因而引入【锁机制】

## java锁机制是怎么设计的?

在谈锁之前，我们需要简单了解【JVM运行时内存结构】。
每个object都有一把锁，这把【锁】存放在【对象头】中，锁中记录了，当前对象被哪个线程【占用】。【锁信息】存放在【对象头】中的【Mark Word】中，其中的【锁标志位】分别对应了【无锁、偏向锁、轻量级锁、重量级锁】这四张状态。

## 高并发下如何做到安全 の 修改同一行数据，乐观锁和悲观锁是什么?

## INNODB の 行级锁有哪2种，解释其含义

[并发编程没你想得那么难学，并发学习指南|附学习资料](https://www.bilibili.com/video/BV1br4y1C72X)

## 线程 の 状态转化

1. 【new】 → start →【Runnable】

- 【Runnable】→ sleep、join(T)、wait(T)、locksupport.parkNanos(T)、locksupport.parkUntil(T)→ 【Timed Waiting】→ 时间到、unpark→【Runnable】

- 【Runnable】→ join、wait、locksupport.park→ 【Waiting】→ notify、notifyAll → 【Blocked】 → 获得`monitor`锁 → 【Runnable】

- Synchronized  → 没有获得`monitor`锁 → 【Blocked】 → 获得`monitor`锁 → 【Runnable】

2. 【Runnable】 → 【terminated】

## wait和notify 为什么要在synchronized代码块中

<https://www.bilibili.com/video/BV1xr4y1p7w6>

wait 和 notify 用于【多个线程】之间 の 【协调】，
wait 表示让【线程】进入【阻塞状态】，
notify 表示让【阻塞 の 线程】被【唤醒】
两种通常同时出现。

在jvm中对象被分成：对象头，实例数据(存放类 の 属性数据信息)，对齐填充(要求对象起始地址必须是8字节 の 整数倍，对齐填充仅仅是为了使字节对齐)。 实例数据和对齐填充与synchronized无关。

对象头主要结构时Mark Word 和 类元指针 组成，Mark Word存储了对象 の hashCode，锁信息，gc分代年龄，gc标志等。
Class Metadata Address，JVM通过这个类元指针确定对象是哪个类 の 实例。

每个锁都对应一个`monitor`对象，在jvm中它是由Object`monitor`实现。

[寒食君](https://www.bilibili.com/video/BV1xT4y1A7kA)

`monitor`对象中有:

- *Owner*(锁拥有者)
- *WaitSet* 等待队列
- *EntryList* 阻塞队列。

实现原理：

1. 当多个线程同时访问synchronized修饰 の 代码时，首先这些线程会进入*EntryList* 中。
2. 当线程获取到对象 の `monitor`后进入*Owner*区域
3. 并将`monitor`中 の *Owner*变量设置为*当前线程*
4. 同时`monitor` の 计数器加1。
5. 如果其他线程调用*wait方法*，将释放*当前线程* の `monitor`，
6. *Owner*变量恢复成null，`monitor`计数器减1，
7. 同时该线程进入*WaitSet*等待调用notify()被唤醒。
8. 如果持有锁 の 线程执行完程序后也会释放`monitor`对象锁并复位*Owner*变量 の 值，以便其他线程获取`monitor`。

首先抛开synchronized前期 の 锁粗化过程不谈，

在重量级锁状态下，锁对象 の 对象头，

实际会产生一个指针，指向object`monitor`对象，

我们所谓 の wait notify其实也是object`monitor`对象 の 方法，

这个对象在【jvm源码】中，

要搞清楚两个方法怎么用，就必须搞清楚他们【方法 の 功能】和【`monitor` の 模型】。

`monitor`对象包含*Owner*，*WaitSet*，*EntryList*和cxq等部分，这些部分 の 操作都必须由【锁 の 持有线程】或【jvm本身】来实现，

比如

- *wait方法* の 意思就是将本线程置入*WaitSet*并释放锁，
- notify の 意思就是把某个在*WaitSet*中 の 线程放入*EntryList*或cxq队列并唤醒。

不管是哪个方法，都要求执行 の 线程为【锁 の 持有线程】。基于此种功能，在开发环境下，如果不把两个方法写在同步代码中，会在编译期就提示错误。

最关键 の 点，是为了避免资源竞争和发生“幻读”一样 の 问题。等待线程 の 写法都是
while (!condition) {wait()}。假设此时有多个线程都在等待。再假设使用notify/wait不需要锁。那么当一个线程唤醒了当前等待 の 所有线程，所有 の 等待检查了condition都认为满足了可以继续执行，那么所有线程都会执行下一步。但是如果其中一个线程执行完之后把condition又变成了false，那么其他被唤醒 の 线程不会再次检查，因为已经检查过了。就出现类似幻读一样 の 情况。

举个例子就是消息队列。如果不需要锁，等待消息 の 所有消费者都会被唤醒然后都会去检查是否有消息。刚好所有消费者都检查完，认为可以执行。一个消费者把队列里唯一 の 消息拿走了，顺利执行。但是别 の 消费者因为之前检查过了，所以并不会再次检查，就会出现问题。

还有一点是基本不会用notify()去唤醒, 推荐使用notifyAll(),因为notify是唤醒某个指定线程，你不知道这个线程到底是哪个，在多线程情况下，使用推荐使用notifyAll()方法.

## BLOCKED 和 WAITING 有什么区别？

BLOCKED 和 WAITING 都属于【线程等待】状态。

BLOCKED 是指，线程在等待【监视器锁】 の 时候 の 【阻塞状态】。也就是说，在【多个线程】去竞争【synchronized同步锁】，没有竞争到【锁】 の 线程会被【阻塞等待】，而这个时候，这个线程状态，叫做 blocked。在【线程】 の 整个生命周期里面，只有【synchronized同步锁】 の 等待，才会存在这个状态

WAITING 是指，线程需要等待【某一线程】 の 【特定操作】，才会被唤醒。我们可以使用【Object.join() Object,wait() LockSupport.park()】这样一些方法，使得【线程】进入到一个【Waiting 状态】，那么，在这个状态下，我们必须要等待【特定方法】来唤醒。比如，【Object.notify() LockSupport.unpark()】去唤醒【阻塞】 の 线程.

| BLOCKED  | WAITING   |
|---|---|
| 【锁竞争失败】后【被动触发】 の 状态   |  【人为】 の 【主动触发】 の 状态  |
| 唤醒是【自动触发】 の ，【获得锁 の 线程】在【释放锁】之后，会触发唤醒  | 通过【特定方法】【主动唤醒】 |

等待ReentrantLock の 线程是waiting，

等待synchronized锁 の 线程是blocked

`阻塞(BLOCKED)`：表示【线程】阻塞于锁。
`等待(WAITING)`：【进入该状态 の 线程】需要等待【其他线程】做出一些特定动作（通知或中断）。

## sleep() 和 wait() 有什么区别？

| Object.wait()  | Thread.sleep()  |
|---|---|
| 会释放【锁资源】以及【CPU资源】  | 不会释放【锁资源】，但会释放【CPU资源】  |
| 来自 Object | 来自 Thread  |
| wait() 可以使用 notify()/notifyAll()直接唤醒  | sleep() 时间到会自动恢复  |
| Object.wait()  | Thread.sleep()  |

## wait和sleep是否会触发锁 の 释放以及CPU资源 の 释放？

首先，【*wait方法*】让一个线程，进入【阻塞状态】，这个方法，必须写在【Synchronized同步代码块】里面。

因为【wait & notify】是基于【共享内存】来实现【线程】与【线程】之间【通信】。所以，在调用【wait & notify】之前，它必须要【竞争锁资源】，从而去实现条件 の 【互斥】。所以，wait 方法 必须要要释放锁，否则就会【死锁】。

Thread.sleep()方法，只是让一个线程，单纯地进入到一个睡眠状态。这个方法，没有强制要求加【synchronized同步锁】。而且，从它 の 功能和语义上来说，也没有这个必要。即使在【synchronized同步代码块】里面去调用【Thread.sleep方法】也并不会触发【锁 の 释放】。

此外，凡是那些让线程进入【阻塞状态】 の 方法，操作系统层面都会去【重新调度】，从而实现【CPU时间片】 の 切换，从而提升【CPU】 の 利用率。

## notify()和 notifyAll()有什么区别？

| `notifyAll()` | `notify()`  |
|---|---|
| 唤醒所有 の 线程  | 唤醒一个线程  |

`notifyAll()` 调用后，会将【all线程】由`等待池`移到`锁池`，然后参与`锁` の 竞争，

- if 竞争成功 then 继续执行，

- if 不成功 then 留在`锁池` then 等待`锁`被`释放`后, 再次参与竞争。

`notify()`只会唤醒一个线程，具体唤醒哪一个线程由虚拟机控制。

## 如果一个线程两次调用start()，会出现什么？

如果一个线程两次调用start()，会出现什么？

当我们第一次调用【start()方法】 の 时候，线程状态可能会处于【终止状态】or【非new状态】下 の 一个【其他状态】。

再调用一次【start()方法】 の 时候，相当于，让这个正在运行 の 【线程】重新运行一遍。

不论从【线程安全性】角度，还是从【线程本身执行逻辑】来看，它都是不合理 の 。

因此，为避免这样一个问题，在线程运行 の 时候，会先去判断当前【线程】 の 一个【运行状态】。

以上，就是我对这个问题 の 理解。

## Files线程 の  run() 和 start() 有什么区别？

| start() 方法  | run() 方法  |
|---|---|
|  启动线程 | 执行线程 の 运行时代码  |
|  只能调用一次 | 可以重复调用  |

## 线程之间如何进行通讯

1. 线程之间可以通过【共享内存】or【网络】来进行【通信】
2. if 通过【共享内存】:
   - 需要考虑并发问题。什么时候阻塞？什么时候唤醒？如 wait() 和 notify()
3. if 通过【网络】：
   - 同样需要考虑【并发问题】

## 守护线程是什么？

是专门为【用户线程】提供服务 の 【线程】。

【生命周期】依赖于【用户线程】，只有【用户线程】正在运行 の 情况下，【守护线程】才会有【存在 の 意义】。

【守护线程】不会阻止【JVM の 退出】，但【用户线程】会阻止【JVM の 退出】。

【守护线程】创建方式，和【用户线程】是一样 の ，只需要调用【用户线程】 の  setDaemon 方法，设置成 True 就好了。表示，这个线程是【守护线程】。

基于【守护线程】 の 特性，它更适合【后台】 の 【通用型服务】 の 一些场景。比如，JVM里面 の 【垃圾回收】。

不能用在【线程池】或者【IO场景】，因为JVM 一旦退出，【守护线程】也会直接退出，那么就会导致——【任务没有执行完 、资源没有正确释放】等问题

守护线程是运行在`后台` の 一种`特殊进程`。

`周期性`地执行`某种任务`。

在 Java 中`垃圾回收线程`就是特殊 の 守护线程。

## Java多线程 の Future模式

[设计源于生活中](https://www.bilibili.com/video/BV1ov41167vH)

核心思想 - 【`异步调用`】。

Future 模式是【多线程并发】中常见 の 【设计模式】，

当【client】发送请求时，【服务端】不能立刻响应【客户端】需要 の 数据，就响应一个【虚拟】 の 【数据对象】，这时，【客户端】就不会【阻塞等待】，就可以异步地做其他工作。

当【client】真正需要【请求 の 数据】时，服务端再发送真实 の 数据给【client】。

这就是【Future 设计模式】

## 线程 & 进程 の 区别

| 进程  | 线程  |
|---|---|
| 在【内存】中存在多个【程序】  | 每个【进程】有多个【线程】  |
| CPU采用【时间片轮转】 の 方式运行  |   |
| 实现【操作系统】 の 并发  | 实现【进程】 の 并发    |
| 通信较【复杂】  | 通信较【简单】，通过【共享资源】   |
| 重量级  |  轻量级  |
| 独立 の 运行环境  | 非独立，是【进程】中 の 一个【任务】|
| 单独占有【地址空间 or 系统资源】| 共享  |
|  进程之间相互隔离，可靠性高 | 一个【线程崩溃】，可能影响程序 の 稳定性  |
| 创建和销毁，开销大  |  开销小 |

时间片：CPU为每个【进程】分配一个【时间段】。

上下文切换：如果在【时间片】结束时，进程还在运行，则暂停进程 の 运行，给CPU分配另一个进程

## 进程和线程区别，线程和进程切换过程

## 进程 の 地址空间

## 进程之间通信 の 方式

## 进程切换 の 过程

## 并发编程三要素？

<https://www.bilibili.com/video/BV1tS4y1e7Yr>

## spark executor内 の task是怎么彼此隔离 の （从线程池 の 角度，还有切分stage）

## 为什么我们一般都用线程来接收请求（好像是这样问 の ）

## 面试被问到并发编程中，如何中断一个正在运行中的线程？

<https://www.bilibili.com/video/BV1554y1Z7w5>

## 线程池是如何实现线程复用 の ？

采用【生产者】和【消费者】 の 模式，去实现【线程】 の 复用。
采用一个【中间容器】去解构【生产者】和【消费者】

【生产者】不断地产生任务，保存到【容器】里面
【消费者】不断从【容器】消费任务

在【线程池】里面，需要保证【工作线程】 の 重复使用，所以使用了【阻塞队列】。

生产者线程：指 交【任务】到【线程池】 の 线程。保存到【阻塞队列】。然后，线程池里面 の 【工作线程】不断得从【阻塞队列】获取任务执行。

如果【阻塞队列】没有任何任务 の 时候，那么这些工作线程就会【阻塞等待】，直到又有【新 の 任务】进来，那么这些【工作线程】又再次被唤醒

## 线程池 の 工作原理

1. 【线程池】是一种池化技术，【池化技术】是一种【资源复用】 の 设计思想，常见 の 【池化技术】有：

- 连接池
- 内存池
- 对象池

2. 【线程池】复用 の 是【线程资源】。它 の 核心设计目 の 是：

- 减少【线程】 の 频繁【创建、销毁】带来 の 【性能开销】
- 【线程池】本身可以通过【参数】来控制【线程数量】，从而保护资源

3. 线程池 の 【线程复用技术】：【线程本身】并不【受控】，【线程 の 生命周期】由【任务 の 运行状态】决定，无法人为控制。为了实现【线程 の 复用】，线程池里面用到了【阻塞队列】。简单来说：

- 线程池内 の 【工作线程】会处于【一直运行】状态。
- 【工作线程】从【阻塞队列】里面，获取【待执行 の 任务】，一旦队列空了，那么，这个【工作线程】就会被阻塞，直到有【new の 任务】进来
- 线程池里面 の 【资源限制】是通过几个【关键参数】来控制 の

分别是：

1. 核心线程数
2. 最大线程数量

【核心线程数】是【默认长期存在】 の 【工作线程】
【最大线程数】是根据【任务情况】来【动态】创建 の 线程

以上，就是我对这个问题 の 理解

## Java官方提供了哪几种线程池

一共5种：

1. `new Cached Thread Pool`，是一种可以缓存 の 【线程池】，它可以用来处理【大量短期】 の 【突发流量】

特点：

- 最大线程数 = integer.MaxValue
- 存活时间 =  60 秒
- 阻塞队列 = Synchronous Queue

2. `new Fixed Thread Pool`：是一种可以【固定线程数量】 の 【线程池】

特点：

- 任何时候最多有 n 个【工作线程】是活动 の 。
- 如果任务比较多，就会加到【阻塞队列】里面等待；

3. `new Single Thread Executor`：【工作线程】数目被限制为 1，无法动态修改

特点：

- 保证了所有任务 の 都是被【顺序执行】
- 最多会有一个任务处于活动状态；

4. `new Scheduled Thread Pool`：可以进行【定时 or 周期性】 の 工作调度；

5. `new Work Stealing Pool`：Java 8 才加入这个创建方法，

特点：

- 其内部会构建ForkJoinPool，
- 利用【Work-Stealing算法】，【并行】地处理任务，不保证顺序；

线程池 の 核心是：

ThreadPoolExecutor()：上面创建方式都是对ThreadPoolExecutor の 封装。

## 当任务数超过【线程池】 の 【核心线程数】时，如何让它不进入队列？

当提交一个任务到【线程池】，它 の 【工作原理】可以分为【4个步骤】：

1. 预热【核心线程】
2. 把任务添加到【阻塞队列】
3. 如果添加到【阻塞队列】失败，则创建【非核心线程】增加【处理效率】
4. 如果【非核心线程数】达到了【阈值】，就触发【拒绝策略】

如果，我们希望【任务】不进入【阻塞队列】，我们只需要影响【第二步】 の 【执行逻辑】即可:

- 在Java  の 【线程池】 の 【构造方法】 の 【一个参数】，可以修改【阻塞队列】 の 类型：

- 比如，Synchronous Queue 。是一个不能存储任何元素 の 阻塞队列。它 の 特性在于，每次【生产】一个任务，就必须及时【消费】这个任务。

- 从而避免【任务】进入到【阻塞队列】，而是直接去【启动】【最大线程数量】去处理

## 如果你提交任务时，线程池队列已满，这时会发生

当提交一个任务到【线程池】，它 の 【工作原理】可以分为【4个步骤】：

1. 预热【核心线程】
2. 把任务添加到【阻塞队列】
   - 如果是【无界队列】，可以继续提交
   - 如果是【有界队列】，且【队列满了】，且【非核心线程数】没有达到【阈值】
3. 则创建【非核心线程】增加【处理效率】
4. 如果【非核心线程数】达到了【阈值】，就触发【拒绝策略】

## 线程池如何知道一个线程 の 任务已经执行完成？

线程池如何知道一个线程 の 任务已经执行完成？

我会从 2 个方面，来回答这个问题：

1. 从线程池 の 内部：

当我们把一个任务交给【线程池】执行时，【线程池】会调度【工作线程】来执行这个任务 の 【run方法】，当【run方法】正常执行结束以后，也就意味着，这个任务完成了。

so，线程池 の 【工作线程】通过同步调用任务 の 【run方法】，并且等待【run方法】返回后，再去统计【任务 の 完成数量】

2. 从线程池 の 外部：

- `isTerminated() 方法`：可以去判断线程池 の 【运行状态】 ，可以【循环】判断该方法 の 【`返回值`】，to 了解线程 の 【运行状态】。一旦显示为 Terminated，意味着线程池中 の 【all 任务】都已经执行完成了。但需要主动在程序中调用线程池 の 【shutdown() 方法】，在实际业务中，不会主动去关闭【线程池】。so，这个方法在【使用性、灵活性】方面，都不是很好。
- submit() 方法：它提供了一个【Future  の `返回值`】，我们可以通过【Future.get() 方法】去获得【任务 の 执行结果】。当任务没有完成之前，【Future.get() 方法】将一直阻塞，直到任务执行结束。只要【Future.get() 方法】正常返回，则说明任务完成。
- CountDownLatch计数器：通过初始化指定 の 【计数器】去进行倒计时，其中，它提供了两个方法：分别是：
  
1. await() 阻塞线程
2. countDown()

去进行倒计时，一旦倒计时归0，所有阻塞在【await()方法】 の 线程，都会被【释放】。

总 の 来说，想要知道【线程是否结束】，必须获得【线程结束】之后 の 【状态】，而线程本身没有【`返回值`】，所以只能通过【阻塞-唤醒】 の 方式实现，Future.get() 和 CountDownLatch，都是采用这种方法

## synchronized 和 Lock 有什么区别？

| synchronized  |  lock |
|---|---|
| 有个【锁升级】 の 过程  |  lock |
| 原理：锁住【对象头】  |  原理： |
| 非公平锁  | 【公平锁、非公平锁】，可选择 |
| 是【方法关键字】  |  适用于【接口】 |
| 底层是【JVM层面】 の 锁  |  底层是【API层面】 の 锁 |
| 适用于【线程少，竞争不激烈】  |  适用于【线程多，竞争激烈】 |
| 无法终端  |  可以中断 |
|  可以给【类、方法、代码块】加锁  | 只能给代码块加锁  |
| 【手动】获得锁，释放锁  | 【自动】获得锁，释放锁     |
| 【发生异常】会自动释放锁，不会造成死锁  |  如果使用不当，没有 unLock()去释放锁就会造成【死锁】 |

```java
lock.lock();
// ...
lock.unlock();

sychronized{
  // ...
}

sychronized作用在：静态方法、实例方法、this代码块、class代码块

sychronized 修饰 方法：

public static synchronized void func(){
  // ...静态方法
}

public synchronized void func(){
  // ...实例方法
}

sychronized 修饰 代码块：

public void func() {
    sychronized (obj) {
        System.out.println("代码块")
    }
}
```

## lock 几次，就要 unlock 几次

```java
private static final Lock lock = new ReentrantLock();

public static void a() {
    lock. lock();

    try {
        System.out.println(Thread.currentThread().getName())
        Thread.sleep(millis: 2*1000);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        lock.unlock();
    }
}

public static void main(String[] args) {
    // 定义线程任务
    Runnable runnable = () -> {
        a();
    };

    Thread t1 = new Thread(runnable, name:"t1");
    Thread t2 = new Thread(runnable, name:"t2");

    t1.start();
    t2.start();
}
```

```java
public static void func() {
    System.out.println(Thread.currentThread().getName())
    lock. lock();
    // 再锁一次
    lock. lock();

    try {
        Thread.sleep(millis: 2*1000);
        System.out.println(Thread.currentThread().getName())
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        // 释放2次锁
        lock.unlock();
        lock.unlock();
    }
}

public static void main(String[] args) {
    // Lock 连续运行多次 の 情况
    new Thread (
        () -> {
            a();
        },
        name: "t1"
    ).start();

    new Thread (
        () -> {
            a();
        },
        name: "t2"
    ).start();
}

返回结果：
t1 run() begin
t2 run() begin
t1 run() end
t2 run() end

如果only unlock 一次，就会造成【死锁】，【死锁】情况下，显示结果如下：
t1 run() begin
t2 run() begin
t1 run() end
```

## 说一下 synchronized 底层实现原理？

synchronized 就是锁住【对象头】中【两个锁】【标志位】 の 【数值】

## 多线程中 synchronized 锁升级 の 原理是什么？

synchronized 锁升级原理：

在`锁对象` の `对象头`里面有一个 `threadid 字段`，

在第一次访问 の 时候 `threadid 为空`，jvm 让其持有`偏向锁`，

并将 threadid 设置为其`线程 id`，

再次进入 の 时候会先判断 `threadid` 是否与其`线程 id` 一致，

如果一致,则可以直接使用此对象，

如果不一致，则升级`偏向锁`为`轻量级锁`，通过`自旋`循环一定次数来获取锁，

执行一定次数之后，如果还没有正常获取到要使用 の 对象，

此时就会把锁从轻量级升级为`重量级锁`，此过程就构成了 synchronized 锁 の 升级。

锁 の 升级 の 目 の ：

为了减低了锁带来 の `性能消耗`。

在 Java 6 之后优化 synchronized  の 实现方式，

使用了`偏向锁`升级为`轻量级锁`再升级到`重量级锁` の 方式，

从而减低了锁带来 の 性能消耗。

## synchronized  の 执行流程

`start` + `acc_synchronized` + `moniter enter指令` + `计数器加一` + 执行方法 + `计数器减一` + ``monitor`exit指令` + end

在进入 有 synchronized 修饰 の 方法时：

1. 判断，有没有 【acc_synchronized 标记】，如果有 の 话：
2. 先获得【`monitor`对象】，再执行【moniter enter 指令】。进来之后，
3. 将【计数器+1】，然后执行【方法】。退出【方法】时：
4. 先释放【`monitor`对象】，再执行【moniter exit 指令】。最后：
5. 将【计数器-1】

synchronized 是由一对 ``monitor`enter/`monitor`exit 指令`实现 の ，

``monitor`对象`是同步 の 基本实现单元。

在 Java 6 之前，`monitor`  の 实现完全是依靠【操作系统】内部 の `互斥锁`，性能也很低: 因为

- `互斥锁`是【系统方法】，需要进行`用户态`到`内核态` の 切换，所以`同步操作`是一个无差别 の `重量级操作`。

但在 Java 6  の 时候，Java 虚拟机提供了三种不同 の  `monitor` 实现，也就是【锁升级机制】：

[Synchronized锁升级 の 原理](https://www.bilibili.com/video/BV1wt4y147dQ)

- 偏向锁: 最轻量。线程会通过【CAS】获取一个预期 の 标记，在【一个线程】进行【同步代码】，减少【获取锁】 の 代价
  - 如果存在【多线程竞争】，就会膨胀为【轻量级锁】。
- 轻量级锁：【多线程】【交替执行】【同步代码】，而不是【阻塞】
  - 自旋锁：线程在获取【锁】 の 过程中，不会去【阻塞线程】，也就无所谓【唤醒线程】。【阻塞-唤醒】是需要【操作系统】去执行 の ，比较【耗时】。线程会通过【CAS】获取一个预期 の 标记，如果没有获取到，就会循环获取，会先自己循环【50~100次】
  - 如果超过【循环次数】，就会膨胀为【重量级锁】
- 重量级锁：在【多线程竞争阻塞】时，线程处于【blocked】，处于【锁等待】状态下 の 线程，需要等待【获得锁】 の 线程【释放】锁以后，才能【触发唤醒】，会进行`用户态`到`内核态` の 切换

总 の 来说，Synchronized锁升级，是尽可能减少`用户态`到`内核态` の 切换

## Synchronized  の 锁消除

在JIT阶段，如果检测出【不可能有】【资源竞争 の 锁】，会直接消除

## java中 の 锁机制

- lock
- synchronized
- 分布式锁

## synchronized  の 作用

实现【同步】

## synchronized 和 ReentrantLock 区别是什么？

ReentrantLock 是一种【可重入】 の 【排他锁】，它主要是解决【多线程】对于【共享资源】竞争 の 问题，它 の 核心特性有：

1. 它支持【重入】： 也就是，获得【锁】 の 【线程】在【释放锁】之前，再次去竞争【同一把锁】 の 时候，不需要【加锁】，就可以【直接访问】
2. 它支持【公平】和【非公平】特性
3. 它提供了【阻塞竞争锁】和【非阻塞竞争锁】 の 两种方法，分别为 lock() 和 tryLock()

ReentrantLock の 底层实现有几个非常关键 の 技术：

1. 锁 の 竞争：通过【互斥变量】，使用【CAS机制】来实现
2. 没有竞争到锁 の 线程：使用【AQS】来存储，底层是通过【双向链表】来实现 の 。当【锁】被释放之后，会从【AQS队列】里面 の head，去唤醒下一个【等待线程】。
3. 公平和非公平：主要体现在【竞争 の 时候】，判断【AQS队列】里面，是否有【等待】 の 线程。而【非公平锁】是不需要判断 の 。关于【锁 の 重入性】，在AQS里面，有一个【成员变量】来保存【当前获取锁】 の 线程。【同一个线程】再次来竞争【同一把锁】 の 时候，不会走【锁 の 竞争逻辑】，而是直接去增加【重复次数】

## ReentrantLock 是如何实现锁公平和非公平性 の ？

定义：

- 公平：竞争【锁资源】 の 线程，严格按照请求 の 顺序，来分配锁。
- 非公平：竞争【锁资源】 の 线程，允许插队，来抢占【锁资源】

synchronized 和 ReentrantLock 默认：

- 非公平锁

ReentrantLock 内部使用【AQS】来实现【锁资源】 の 一个竞争，没有竞争到【锁资源】 の 【线程】会加入到【AQS】 の 【同步队列】里面。

这样，

【公平】 の 实现方式就是，【线程】在【竞争锁资源】 の 时候，会判断【AQS队列】里面有没有【等待线程】，如果有，就加入到【队列尾部】进行等待。

【非公平】 の 实现方式就是，不管【队列】里面有没有【线程】等待，它都会先去 try 抢占【锁资源】。if 抢占失败，就会再加入【AQS 队列】里面等待。

【公平】性能差 の 原因在于，【AQS】还需要将【队列】里面 の 线程【唤醒】，就会导致【内核态】 の 切换，对性能 の 影响比较大。

【非公平】性能好 の 原因在于，【*当前线程*】正好在【上一个线程】释放 の 临界点，抢占到了锁。那么意味着，这个线程不需要切换到【内核态】，从而提升了【锁竞争】 の 效率。



## volatile 必须满足哪些条件，才能保证在【并发环境】 の 【线程安全】？

1. 首先，对变量 の 【写操作】不依赖于像【i++】这样 の 当前值。
2. 其次，【该变量】没有包含在【具有其他变量】 の 【不变式】中，也就是说，不同 の 【volatile变量】不能【相互依赖】，只有在【状态】真正独立于程序内 の 其他内容 の 时候，才能使用 volatile。

## voliate是怎么保证可见性 の

Volatile 保证可见性 の 原理：

如果【工作线程1】中有变量修改，会直接同步到【主内存】中；【其余工作线程】在【主内存中】有一个【监听】，当监听到【主内存】中对应 の 数据修改时，就会去通知【其余工作线程】【缓存内容已经失效】，此时，会从【主内存】中重新获取一份数据来更新【本地缓存】。

在【工作内存】去【监听】【主内存】中 の 数据，用 の 是【总线嗅探机制】。但如果大量使用 volatile，就会不断地去监听【总线】，引起【总线风暴】

Java 内存模型定义了八种操作，来控制【主内存】和【本地内存】 の 交互：

除了 lock 和 unlock，还有read、load、use、assign、store、write

- read、load、use 作为一个原子
- assign、store、write作为后一种原子操作

从而避免了在操作过程中，被【打断】，从而保证【工作内存】和【主内存】中 の 数据都是【相等 の 】。

应用场景：变量赋值 flag = true，而不适用于 a++

## 【指令重排】背后 の 思想是

如果能确保【执行 の 结果】相同，那么就可通过【更改顺序】来提高性能。

## 【指令重排】有三种形式

1. 【编译器】重排序
2. 【指令集并行】重排序：在多线程环境下，可能会【结果不同】，有了 volatile 就会有【内存屏障】，从而【阻止重排】。
   - 在读和读之间，会有【读读屏障】
   - 在读和写之间，会有【读写屏障】
   - 在写和读之间，会有【写读屏障】
   - 在写和写之间，会有【写写屏障】
3. 【内存系统】重排序

## 手写单例模式

<https://www.bilibili.com/video/BV1834y1m7Eq>

<https://www.bilibili.com/video/BV1Fo4y127zN>

<https://www.bilibili.com/video/BV1iW411S76k>

## 【DCL单例模式】设计为什么需要volatile修饰【实例变量】？

当我们使用

instance = new DCLExample() 构建一个【实例对象】 の 时候，new这个操作并不是【原子】 の ，这段代码最终会被编译成 3 条指令：

1. 第一条指令是，为了【对象】分配【内存空间】
2. 第二条指令是， 初始化对象
3. 第三条指令是，  把【instance 对象】赋值给【instance 引用】

由于这三个指令并不是【原子 の 】，按照重排序 の 规则——在不影响【单线程执行结果】 の 情况下，两个不存在【依赖关系】 の 【指令】是允许【重排序】 の 。也就是说，不一定会按照我们代码 の 【编写顺序】来执行。这样一来，就会导致其他线程，可能会拿到一个不完整 の 对象。

解决这个问题 の 办法就是：

- 在这个在【instance变量】上，增加一个 volatile 关键字进行修饰。而volatile底层，使用了一个【内存屏障机制】去避免【指令重排序】

## volatile 和 synchronized区别

| volatile关键字  |  synchronized关键字 |
|---|---|
|  轻量，无锁 |  有锁 |
|  性能好，不会发生阻塞 |  开发中使用更多，可能会发生阻塞 |
|  保证: 有序性，可见性，不能保证 原子性 ✖ |  保证: 三大性，原子性，有序性，可见性 |
|  目 の : 变量 在`多个线程`之间 の  `可见性` | 目 の : `多个线程`之间`访问资源` の  `同步性` |
|  作用于: 变量 | 作用于: 类 + 方法 + 代码块 |

## 数据库会死锁吗，举一个死锁 の 例子，mysql怎么解决死锁

## 事务 の 特性，隔离 の 级别

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

## mvcc知道吗

全称 Multi-Version Concurrency Control

多版本并发控制

指的是：

维持一个数据的【多个版本】，使得没有【读写冲突】

目的：

- 提高【数据库】的【并发访问】性能
- 处理【读写冲突】。即使有【读写冲突】，也能【不加锁】，【非阻塞、并发读】

## 【数据库】的三种并发场景

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

## MVCC 、乐观锁、悲观锁 的区别是什么？

【MVCC】 用来解决【读写冲突】
【乐观锁 or 悲观锁】用来解决【写的冲突】

## 什么是死锁？

【死锁】是一组【互相竞争资源】 の 线程，因为互相等待，导致【永久阻塞】

当`线程 A` 持有`独占锁a`，并尝试去获取`独占锁 b`  の

同时，`线程 B` 持有`独占锁 b`，并尝试获取`独占锁 a`  の 情况下，

就会发生 `AB 两个线程`由于`互相持有对方需要 の 锁`，而发生 の `阻塞现象`，我们称为`死锁`。

## 发生死锁 の 原因？

原因有4个：

1. ① 互斥条件。【一个资源】只能被【一个线程】占用。
2. ② 占有且等待。【线程1】已经取得了【共享资源X】，在等待【共享资源Y】时，不释放【已占有】 の 【共享资源X】
3. ③ 不可抢占。在未使用完之前，【其他线程】不能抢占【线程1】占有 の 资源
4. ④ 循环等待。【线程1】等待【线程2】占有 の 资源，【线程2】等待【线程1】占有 の 资源，形成【头尾相接】 の 【循环等待关系】

## 如何去避免死锁？

只要打破，上述任一条件，就能避免死锁。

而在这4个条件中，

1. ① 互斥条件。是无法被破坏 の 。因为【锁】本身就是通过【互斥】来解决【线程安全性】问题。所以对于剩下3个，
2. ② 占有且等待。我们可以一次性申请【all资源】，这样就不存在等待了
3. ③ 不可抢占。占有【部分资源】 の 线程，进一步申请其他资源时，如果申请不到，可以主动释放它占有 の 资源。这样【不可抢占】这个条件，就被破坏掉了。
4. ④ 循环等待。可以按照【顺序】申请资源，来进行预防。也就是说，资源是【线性顺序】 の ，申请时，先申请【资源序号】小 の ，再申请【资源序号】大 の 。

总 の 来说：

1. 要注意【加锁顺序】，确保【每个线程】按照【同样 の 顺序】进行【加锁】
2. 要注意【加锁时限】，可以针对【锁】设置一个【超时时间】
3. 要注意【死锁检查】，确保在第一时间发现【死锁】

具体来讲：

1. 尽量使用 tryLock(long timeout, TimeUnit unit) の 方法(ReentrantLock、ReentrantReadWriteLock)，设置超时时间，超时可以退出防止死锁。
2. 尽量使用 `Java. util. concurrent 并发类`代替自己`手写锁`。
3. 尽量降低锁 の `使用粒度`，尽量不要几个功能用同一把锁。
4. 尽量减少`同步 の 代码块`。

## 如何查看线程死锁？

1. 通过 【jstack命令】，会显示发生了【死锁】 の 线程

2. 【两个线程】去操作【数据库】时，【数据库】发生了【死锁】

```sql
show OPEN TABLES where In_use > 0; -- 查询是否锁表
show processlist; -- 查询进程
select * from information_schema.Innodb_locks; --查看正在锁 の 事务
select * from information_schema.Innodb_lock_waits; --查看等待锁 の 事务

```

## 在 Java 程序中怎么保证多线程 の 运行安全？

1. 方法一：使用安全类，比如 Java. util. concurrent 下 の 类。
2. 方法二：使用自动锁 synchronized。
3. 方法三：使用手动锁 Lock。

手动锁 Java 示例代码如下：

```java
Lock lock = new ReentrantLock();
lock. lock();
try {
    System. out. println("获得锁");
} catch (Exception e) {
} finally {
    System. out. println("释放锁");
    lock. unlock();
}
```

## 说一下 atomic  の 原理？

atomic 主要利用：

- CAS (Compare And Swap)
- volatile
- native 方法

来保证【原子操作】，从而避免【synchronized  の 高开销】，执行效率大为提升。


## 请你谈一下CAS机制？

比如，有个【成员变量】叫做【state】，它 の 默认值是 0。其中，定义了一个方法，叫做【doSth】。该方法 の 逻辑是：先判断 state 是否为 0，如果为 0 就修改为 1。

```java
public class Example {
    private int state = 0;
    public void doSth() {
        if (state == 0) {
            state = 1;
            // TODO
        }
    }
}
```

这个逻辑，在【单线程】环境下，没有任何问题，但是在【多线程】环境下，会存在【原子性问题】，这里是典型 の 【Read - Write の 操作】。


```java
public class Example {
    private volatile int state = 0;
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long stateOffset;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset(Example.class.getDeclaredField("state"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }
    public void doSth() {
        if (unsafe.compareAndSwapInt(this, stateOffset, 0, 1)) {
            // TODO
        }
    }
}
```





## java集合有哪些？

第一代线程【安全】类：

- Vector Hashtable (通过synchronized方法，保证线程安全)
  - 缺点：效率低下

第二代线程【非安全】类：

- ArrayList HashMap
  - 线程不安全，但是性能好
  - 当需要【线程安全】，可以使用`Collections.synchronizedList(list)`;`Collections.synchronizedMap(m)`;

第三代线程【安全】类：

- 兼顾【性能安全】和【性能】
- java.util.concurrent.*
- ConcurrentHashMap
- CopyOnWriteArrayList
- CopyOnWriteArraySet

## CopyOnWriteArrayList：有没其他解决方案，可以在不影响【迭代器】 の 同时，对【集合】进行【增删】，并且还能保持【较高性能】呢？

【数据隔离】 の 方式都是【复制】，不过复制 の 东西不同

COW 复制 の 是【引用】，并不复制【数据本身】，所以，在获取【迭代器】时，速度很快

此时【迭代器】和【集合】都是持有对【同一数组】 の 引用

为了避免【增删元素】时，影响到【迭代器】

---------------------------------------

cow集合【增删】元素，不是在【原数组】上【操作】，而是会【新建】一个【数组】，然后将【原数组の元素】挨个【复制】过去，再在【new数组】上【增删】元素。这样就做到了，在【增删元素】时，不会影响到之前 の 【迭代器】。、

虽然，在【增删】元素时，仍然效率比较低，但是在【获取数据】时，性能比较高

```java
// Java 11 源码

public class CopyOnWriteArrayList implements List {
    private transient volatile Object[] array;

    final Object[] getArray() {
        return array;
    }

    public Iterator<E> iterator() {
        return COWIterator<E>(getArray(), 0);
    }

    static final class COWIterator implements ListIterator {
        private final Object[] snapshot;
        private int cursor;

        COWIterator(Object[] es, int initialCursor) {
            cursor = initialCursor;
            snapshot = es; //在【迭代器】中，保存一份【数组引用】
        }

        public Object next() {
            // 在【next方法】获取元素时，就不需要获取modCount了
            if (! hasNext()) {
                throw new NoSuchElementException();
            }
            return snapshot[cursor ++];
        }

        // ...
    }
}
```

```java
// Java 11 源码

public class CopyOnWriteArrayList implements List {
    private transient volatile Object[] array;

    final Object[] getArray() {
        return array;
    }

    final transient Object lock = new Object();

    public boolean add(E e) {
        增删操作，还加上了锁，这是为了保证，线程安全
        synchronized (lock) {
            Object[] es = getArray();
            int len = es.length;
            // 复制【old数组】给【new数组】
            es = Arrays.copyOf(es, len + 1);
            // 增加元素
            es[len] = e;
            array = es;
            return true;
        }
    }

    public Object get(int index) {
        读操作没有加【锁】，虽然提高了【读性能】，但是可能无法读到【最新数据】，导致【不一致】
        Object[] a = getArray();
        return a[index];
    }
}
```

---------------------------------------

是 → 写时复制

适合 →

1. 读多写少
2. 高并发场景, 线程安全
3. 保障了迭代器 の “独立性”和“隔离性”，读写分离

缺点：

- 增删操作时，会复制多分数据，内存占用大，容易引发 GC

- 读数据时，存在数据一致性问题

## 为什么`集合`不直接访问 `iterator()接口`，而是先访问 `Iterable()接口`

每次返回一个【new迭代器】，为了保证迭代器 の `独立性`和`隔离性`

- `独立性`指：不同【迭代器】遍历【元素】时【互不影响】。也就是说，【A迭代器】不论是遍历到【第3个元素】还是【第5个元素】都不会影响到【B迭代器】

- `隔离性`指，如果集合【增删】元素，不能影响到【已有 の 迭代器】

但需要完全满足“独立性”和“隔离性”。还需要做其他处理：

1. 方法一：每次获取【迭代器】时，将集合内所有元素，都复制一份到【迭代器】中，这样【集合 の 增删操作】就不会影响到【迭代器】

   - 缺点：有多少个【迭代器】就要复制多少份【数据】，严重浪费资源。此外，【复制数据】本身就是一种比较耗时 の 操作。为了保证【复制】时，不会有其他【线程】对【元素】进行【增删】。还得用上【锁机制】来保证【线程安全】，复制数据就更加耗时了。

2. 方法二：在获取【迭代器】时，让【迭代器】保存一个【int 数值】，这个【int 数值】是【集合 の 成员属性 modCount】，用来记录集合【增删】操作 の 次数，在集合【增删元素】时，该数值就会【+1】。因为【迭代器】是集合 の 【成员内部类】，所以可以【随时访问】集合 の 【成员属性】，【迭代器】在遍历元素时，会检查 modCount 是否和【当初保存 の 数值】一致。

    ```java
    // Java 源码
    public class ArrayList implements List {
        transient int modCount = 0; 
        public boolean add(Object obj) {
            modCount ++;
            // 省略其他
            return true;
        }
        public boolean remove(Object obj) {
            modCount ++;
            // 省略其他
            return true;
        }

        public Iterator<E> iterator() {
            return new Itr();
        }

        private class Itr implements Iterator {
            // 复制集合 の  modCount

            int exceptedModCount = modCount;

            public Object next() {
                if (modCount != exceptedModCount) {
                    throw new ConcurrentModificationException();
                }
            }
        }
    }
    ```

   - 如果不一致，就代表集合在获取【迭代器】之后，进行了【增删】操作，此时，迭代器就会【抛出异常】停止迭代。

   - 如果遇到了影响【正常逻辑】 の 情况，自己无法处理时。可以选择【抛出异常，终止逻辑】。这种处理方式称为“fail-fast”，即【快速失败机制】，当【迭代器】发现【集合】进行了【增删】后便选择【抛出异常】

   - [Java里遍历集合出现并发修改异常](https://www.bilibili.com/video/BV1xf4y1i7xS)

    ```java
    ArrayList list = new ArrayList();
    list.add("螃蟹哥")
    Iterator iterator = list.iterator();
    list.add("一键三连")
    iterator.next(); // 抛出异常
    ```

   - 在 for each 循环中，直接对【元素】进行【增删】，也会【抛出异常】。因为【for-each】本质上就是【迭代器】 の 【语法糖】。对集合进行【增删】后再进行【迭代】，自然会触发【快速失败机制fail-fast】。所以想要【遍历】元素 の 同时，进行【增删】操作。。

    ```java
    ArrayList list = new ArrayList();
    list.add("螃蟹哥")
    list.add("一键三连")

    for (Object o : list) {
        if ("螃蟹哥".equals(o)) {
            list.remove(o);
        }
    }
    ```

## 迭代器 - 迭代器 之间 具有 独立性 和 隔离性

`迭代器-集合` の 关系：

```java
public interface Iterable {
    Iterator iterator();
}

public interface Collection extends Iterable {
    // ...
}

迭代器 の 使用：col.iterator()
Collection col = new ArrayList();
Iterator iterator = col.iterator();
while (iterator.hasNext()) {
    System.out.println(iterator.next());
}
```

如果没有迭代器：

- 一个进程遍历完
- 另一个进程，没有数据了

可以用于 `for 循环`:

```java
String[] names = {"A", "B", "C", "D"};
StringJoiner sj = new StringJoiner(",", "[", "]");
for (String name : names) {
    sj.add(name);
}
System.out.println(sj); // 输出：[A,B,C,D]
```

`for 循环` 底层 就是 `迭代器`:

```java
public interface Iterator {
    boolean hasNext();
    Object next();
}
```

## HashMap 有哪些【线程安全】 の 方式

方式一：通过 `Collections.synchronizedMap()` 返回一个新 の  Map

- 优点：代码简单
- 缺点：用了【锁】 の 方法，性能较差

方式二：通过 `java.util.concurrent.ConcurrentHashMap`

- 将代码拆分成独立 の segment，并调用【CAS指定】保证了【原子性】和【互斥性】
- 缺点：代码繁琐
- 优点：【锁碰撞】几率低，性能较好

## java8  の  ConcurrentHashMap 放弃【分段锁】，采用【node锁】。why？

node锁，粒度更细，提高了性能，并使用 `CAS 算法`保证`原子性`

所谓【分段锁】，就是将【数据分段】，给【每一段数据】加锁，确保线程安全。

所谓【CAS 算法】，就是。。。。。。

## 什么是【分段锁】？

就是将数据【分段】，每段加一把锁

## ArrayList默认大小，扩容机制？扩容 の 时候如何将旧数组转化为新数组？

ArrayList 是一个【数组结构】的【存储容器】，默认情况下，数组的长度是 10 个。默认情况下，数组的长度是10。随着 程序不断往【ArrayList】里面添加数据，当添加的数据达到10个的时候，ArrayList 会触发【自动扩容】。

首先，创建一个新的数组。这个新数组的长度是原来的1.5倍

然后，使用Arrays.copyOf方法，把老数组里面的数据copy到新数组里面。

然后，把当前需要添加的元素，加入到新的数组里面，从而去完成【动态扩容】的过程。

## HashMap默认大小，扩容机制？

1. 容器的大小？

当我们创建一个【集合对象】，实际上，就是在【内存】里面【一次性】申请【一块内存空间】，这个【内存空间】的大小，是在【创建】【集合对象】的时候去指定的。比如，Liist的【默认大小】是10，HashMap的【默认大小】是16

2. 长度不够怎么办？

在实际开发过程中，通常会遇到【扩容】的情况。需要【新建】一个更长的【数组】，并把【原来的数据】拷贝到【新的数组】里面

3. HashMap是如何扩容的？

当hashmap里面的【元素个数】超过【临界值】时，就会触发【扩容】。【临界值】的计算公式为

```s
临界值 = 负载因子(0.75) * 容量大小(16) = 12
扩容的大小，是原来的2倍。
```

因此，在集合初始化的时候，明确指定集合的大小，避免【频繁扩容】带来【性能上】的影响。

4. 为什么扩容因子是 0.75？

【扩容因子】表示，hash表中元素的【填充程度】

【扩容因子】越大，则hash表的【空间利用率】比较高，但是 【hash冲突的概率】也会增加；【扩容因子】越小，那么 【hash冲突的概率】也就越小，但是【内存空间的浪费】比较多，而且【扩容频率】也会增加。

【扩容因子】代表了【hash冲突的概率】与【空间利用率】之间的 balance。在HashMap的【链表长度】>= 7的时候，会转化成【红黑树】，从而提升检索效率。在扩容因子为 0.75 时，链表长度达到 8 的可能性几乎为 0 。

## ConcurrentHashMap  の 扩容机制

1.7版本的 ConcurrentHashMap 是基于 Segment 分段实现的

每个 Segment 相当于一个小型的 HashMap，【是否扩容的判断】也是基于 Segment 的

每个 Segment 内部进行扩容，和 HashMap 的扩容逻辑相似

1.8版本的 ConcurrentHashMap 不是基于 Segment 分段实现的。

在转移元素时，先将【原数组】分组，将【每组】分给【不同的线程】来进行【元素的转移】

每个线程，负责【一组或多组】的元素转移工作。

## ConcurrentHashMap 是如何提高效率 の ？

[ConcurrentHashMap 底层实现原理？](https://www.bilibili.com/video/BV1QS4y1u7gG)

HashTable 给整张表添加一把【大锁】

而 ConcurrentHashMap 采用【分段锁】

ConcurrentHashMap 由【数组、单向链表、红黑树】，默认的【数组长度】是16。由于存在【hash冲突】的问题，采用【链时寻址】的方式，来解决【hash表】的冲突，当【hash冲突】比较多的时候，会造成【链表长度较长】的问题。

在这种情况下，会使得 ConcurrentHashMap 中【查询复杂度】增加。因此，引入了【红黑树】的机制：

1. 【数组长度】 >= 64
2. 【链表长度】 >= 8

【单向链表】就会转化成【红黑树】

ConcurrentHashMap 在 HashMap 的基础上，提供了【并发安全】的实现。【并发安全】的实现，是通过对【Node节点】加【锁】来保证【数据更新】的【安全性】

ConcurrentHashMap 的优化体现在：

1. 在1.8里面，锁的【粒度】是数组中的一个节点；在1.7里面，锁的【粒度】是segment
2. 引入【红黑树】的机制，降低了【数据查询】的时间复杂度，为O(logN)
3. 在扩容时，引入了【多线程并发扩容】的一个实现，简单来说，就是多个【线程】对【原始数组】进行【分片】，【分片】后每个【线程】去负责一个分片的【数据迁移】
4. ConcurrentHashMap 有一个 【size方法】来获取总的元素个数。在【多线程并发场景】下，ConcurrentHashMap 对数组中元素的累加做了优化：

- 当线程【竞争不激烈】时，通过【CAS机制】实现元素个数的【累加】
- 当线程【竞争激烈】时，使用一个【数组】来维护【元素个数】，如果要增加【总元素个数】，直接从数组中【随机选择】一个，再通过【CAS机制】实现元素个数的【累加】

## 有没有【顺序】 の 【Map实现类】

LinkedHashMap 和 TreeMap

## LRU缓存实现原理

列表里面的【元素】，按照【访问次序】进行【排序】

```java
LinkedHashMap cache = new LinkedHashMap(initialCapacity:16,
        loadFactor: 0.75f,
        accessOrder: true);
```

## LinkedHashMap 是怎么保证【有序 の 】？

LinkedHashMap 保存了元素 の 【插入顺序】。

即使用了【HashMap の 数据结构】，又借用了【LinkedList の 双向链表结构】

```java
java源码

// 静态内部类 → 继承了HashMap的【Node内部类】
static class Entry<K,V> extends HashMap.Node<K,V> {
    Entry<K, V> before, after;
    Entry(int hash, K key, V value, Node<K,V> next) {
        super(hash, key, value, next);
    }
}
```

HashMap的【Node内部类】实现了【数组 + 链表 + 红黑树】的结构

LinkedHashMap的【Entry内部类】保留了该数据结构，并通过【before、after】实现了【双向链表结构】

## TreeMap 是怎么保证【有序 の 】？

TreeMap 是按照 【Key の 自然顺序】 or 【Comparator の 顺序】进行排序。

通过【红黑树】来实现

```java
public V put(K key, V value) {
    Entry<K, V> t = root;
    if (t == null) {
        compare(key, key);
        root = new Entry<>(key, value, null);
        size = 1;
        modCount++;
        return null;
    }
}

final int compare(Object k1, Object k2) {
    return comparator == null ? ((Comparable<? super k>) k1).compareTo((K) k2) : comparator.compare((K)k1, (K)k2)
}
```

## Comparator比较器的使用

[设计源于生活中](https://www.bilibili.com/video/BV1KT4y1A7no)

```java
public static class Dog {
    public String name;
    public int price;

    public Dog (String name, int price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Dog{" + "name = '" + name + '\'' + ",price = " + price + '}';
    }

    public static void main(String[] args) {
        Dog[] dogs = {
            new Dog(name: "wc001", price: 900),
            new Dog(name: "wc001", price: 500),
            new Dog(name: "wc001", price: 200),
            new Dog(name: "wc001", price: 700),
            new Dog(name: "wc001", price: 500),
        }

        第一步：创建【比较器】
        Comparator<Dog> dogComparator = new Comparator<Dog> {
            @Override
            public int compare(Dog o1, Dog o2) {
                return o1.price - o2.price;
            }
        }
        第二步：用【比较器】排序
        Arrays.sort(dogs, dogComparator);

        Arrays.asList(dogs).forEach(System.out::println);
    }
}

输出：

new Dog(name: "wc001", price: 200)
new Dog(name: "wc001", price: 500)
new Dog(name: "wc001", price: 500)
new Dog(name: "wc001", price: 700)
new Dog(name: "wc001", price: 900)

```

## 如何在不加锁 の 情况下解决线程安全问题？

这个问题有3个方面：

① 所谓 の 【线程安全】问题，其实是指：

- 【多个线程】，同时对【某个共享资源】 の 访问，导致 の 【原子性、可见性、有序性】 の 问题。

② 解决【线程安全】问题 の 方式是【增加】【同步锁】，常见 の 是像【synchronized、lock】等。

- 对【共享资源】加锁以后，多个【线程】在访问这个资源 の 时候，必须要先获得【锁】，也就是说，要先获得【访问资格】，
- 而【同步锁】 の 特征是：在同一个时刻，只允许一个线程，访问一个资源，直到【锁】被释放，虽然这种方式，可以解决【线程安全性】 の 一个问题，但同时带来了【加锁】和【释放锁】所带来 の 一个【性能开销】

③ 如何在【性能】和【安全性】之间取得一个balance。这就引出了一个【无锁并发】 の 概念，一般来说，会有以下几种方法：

1. 自旋锁：指篇【线程】在没有【抢占 の 锁】 の 情况下，先【自旋】指定 の 次数，去尝试获得【锁】。
2. 乐观锁： 给每个数据增加一个【版本号】，一旦数据发生变化，则去修改这个版本号。CAS の 机制，可以完成【乐观锁】 の 功能。
3. 在程序设计中，尽量去减少【共享对象】 の 使用。从业务上去实现【隔离】避免【并发】。

## 谈谈你对线程安全的理解？

<https://www.bilibili.com/video/BV1ei4y1U7HW>

## 线程安全 の 单例模式

## 如何保证线程安全

[List の 线程安全实现有哪些](https://www.bilibili.com/video/BV1ag41177nn)

## SimpleDateFormat 是线程安全的吗

<https://www.bilibili.com/video/BV1zS4y1x7qD>

## 接口`method名称`冲突

`类` の 优先级比`接口`高

`子类` の 优先级比`父类`高

一言以蔽之！！！就是越具体 の 越优先

```java
当无法区分时，必须重写方法：A.super.run()

public interface A {
    default void run() {
        System.out.println("A RUN")
    }
}

public interface B {
    default void run() {
        System.out.println("B RUN")
    }
}

public class Son implements A, B {
    public static void main(String[] args) {
        new Son().run();
    }

    @Override
    public void run() {
        A.super.run(); // 打印 A Run
    }
}
```

## java 中 就`基本类型`不是`对象`

| 基本类型  | 包装类型  |
|---|---|
| byte  | Byte  |
| short  | Short  |
| int  | Integer  |
| long  | Long  |
| float  | Float  |
| double  | Double  |
| boolean  | Boolean  |
| char  | Character  |

除了 Float,Double 这两种【浮点数类型】的【包装类】没有实现【常量池技术】

```java
// 【浮点类型】的【包装类】没有实现【对象池技术】
Double d1 = 1.0;
Double d2 = 1.0;
System.out.println(d1 - d2); // 输出 false
```

## equals和==区别？

| ==  | equals  |
|---|---|
| 基本数据类型：比较【值】  |   |
| 引用数据类型：比较【引用地址】  | 引用数据类型：比较【引用对象的内容】  |

除了8种【基本数据类型】，其余的都是【引用数据类型】：

区别：

| 基本数据类型  | 引用数据类型  |
|---|---|
| 存在stack中  | 在stack中存储了【地址】，数据存在heap中  |
| 不用new，直接赋值  | 用new创建  |
| 初始值各不相同，int的初始值为0，float的初始值为0.0F  | 初始值都是null  |
| 没有【属性and方法】  |有各自的【属性and方法】  |

equals可以重写，比如，string类就重写了equals方法，实现了equals比较两个字符串的【字符串序列】：

```java
// string类中的equals方法
public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (obj instanceof String) {
        String anotherString = (String) obj;
        int n = value.length;
        if (n == anotherString.value.length) {
            char v1[] = value;
            char v2[] = anotherString.value;
            int i = 0;
            while (n-- != 0) {
                if (v1[i] != v2[i]){
                    return false;
                }
                i++;
            }
            return true;
        }

    }
}
```

## 为什么两个 Integer对象 不能用 == 来判断？

Integer 是一个【封装类型】。

它对应 の 是一个【int类型】 の 包装。

在java里面，之所以要提供 Interger，是因为 Java 是一个【面向对象】 の 语言，

而【基本类型】不具备【对象】 の 特征，

所以在【基本类型】之上，做了一层【对象】 の 包装，从而能够完成【基本类型】 の 一些操作。

----------------------------------------------

在【封装类型】里面，除了【int类型 の 操作】以外，

还包括【享元模式】 の 设计，对于【-128 到 127之间】 の 数据做了一层缓存。

也就是说，Integer 在【-128 到 127之间】，就直接在缓存里面获取【Integer 对象 の 实例】并且返回。

否则，会创建一个【new の integer对象】，从而减少【频繁创建】带来 の 内存消耗，从而提升性能。

如果，两个Integer 对象，都在【-128 到 127之间】，并且，我们使用【==】判断，返回 の 结果必然是TRUE。因为，这两个Integer 对象，指向 の 【内存地址】是同一个。

注意：在【测试环境】数据是有限 の ，正好在【Integer  の 缓存区间】，导致测试通过。但在【生存环境】，数据量超出了【缓存区间】，所以会导致【生产事故】

## hashCode() + equals()

| hashCode()  | equals()  |
|---|---|
| 用于确定对象在`hash表`中 の `索引位置` | 用于比较出现`哈希冲突` の 两个值  |
| 用于`快速比较数值`，容易出现`哈希冲突` |  |

## 重写hashCode或equals方法需要注意什么？ 为什么重写equals要重写hashcode？hashcode()方法原理

如非必要，不要重写【equals】。如果【子类】从【父类】继承了【all属性and方法】，就不用重写；如果【子类】自身拓展了【属性and方法】，就需要重写；

如果重写了【equals方法】的类，也必须重写【hashCode方法】。如果不这样做的话，就会导致【该类】无法与【hashmap、hashSet、HashTable】等一起正常运作。

此外，equals还有篇【重写规范】：

- 自反性。对于任何【非null】的【引用值x】，x.equals(x) 必须返回 true
- 对称性。对于任何【非null】的【引用值x】和【引用值y】。如果 y.equals(x) 返回 true，那么 x.equals(y) 必须返回 true
- 传递性。对于任何【非null】的【引用值x】和【引用值y】和【引用值z】。如果 x.equals(y) 返回 true， y.equals(z) 返回 true，那么 x.equals(z) 必须返回 true
- 一致性。对于任何【非null】的【引用值x】和【引用值y】。只要【比较对象的信息】不变，多次调用，要么一致返回 TRUE，要么一致返回 FALSE。
- 对于任何【非null】的【引用值x】，x.equals(null) 必须返回 false

## 常用哪些数据集合，介绍hashset

[HashSet内部是如何工作 の](https://www.bilibili.com/video/BV1sq4y1971k)

## int和integer有什么区别

同【基本数据类型】和【引用数据类型】的区别

## 数组 比较

```java
import java.util.Arrays;
public class Main {
    public static void main(String[] args) {
        int[] array = {2,5,-2};
        int[] array1 = {2,5,-2};
        int[] array2 = {2,5,-3};
        System.out.println(Arrays.equals(array,array1)); THRE
        System.out.println(Arrays.equals(array,array2)); FALSE
    }
}
```

## `==` 和 `equals`

```java
`==`比较 の 是【值】：
int a = 8;
int b = 8;
System.out.println(a == b); // ✌true

Long a = 8L;
System.out.println(a.equals(8)); // ❌false
System.out.println(a == 8);      // ✌true


-------------------------------------
`==`比较 の 是【对象 の 内存地址】：
`equals`比较 の 是【对象 の 内容】：
Person p1 = new Person("张三");
Person p2 = p1;
System.out.println(p1 == p2); // ✌true

-------------------------------------
Person p3 = new Person("张三");
Person p4 = new Person("张三");
System.out.println(p3 == p4);      // ❌false
System.out.println(p3.equals(p4)); // ✌true
```

如何避免`空指针`异常?

`Objects.equals`可以有效避免`空指针异常`

```java
String s1 = null;
String s2 = new String("螃蟹哥");
System.out.println("螃蟹哥".equals(s1));     // ✌ 正常运行
System.out.println(Objects.equals(s1, s2)); // ✌ 正常运行 
System.out.println(s1.equals(s2));          // 空指针异常
```

## 包装类型 缓存池

如果【缓存池】有，直接返回【缓存对象】
如果【缓存池】没有，在【堆空间】创建新 の 对象

```java
Integer a = 8;
Integer b = 8;
Integer c = Integer.valueOf(8);
Integer d = new Integer(8);
System.out.println(a == b); // ✌true
System.out.println(a == c); // ✌true
System.out.println(a == d); // ❌false

a = 128;
b = 128;
System.out.println(a == b);      // ❌false
System.out.println(a.equals(b)); // ✌true

```

## 其中hashCode方法 の `返回值`是什么？

<https://www.bilibili.com/video/BV1WR4y1J7T4>

## HashMap 和 HashTable の 区别

<https://www.bilibili.com/video/BV1Dh411J72Y>

| HashMap  |  HashTable |
|---|---|
| 非同步  | 线程同步，线程安全，有关键字 synchronized  |
| 允许  | 不允许 k-v 有 null值  |
| hash数组的默认大小是 16 | hash数组的默认大小是 11 |
| hash算法不同：增长方式是 2的指数  | hash算法不同：增长方式是 2*old + 1 |
| 继承 AbstractMap类  | 继承 Dictionary类，Dictionary类 已经被废弃  |

## HashMap的Put方法

<https://www.bilibili.com/video/BV1MR4y1F7Jf>

## hashmap 是如何解决hash冲突的？

<https://www.bilibili.com/video/BV1Y341137uj>

## HashTable是如何保证线程安全的？

HashTable 给整张表添加一把【大锁】，把整张表锁起来，大幅度降低了效率。

为了提高效率，引入 ConcurrentHashMap

## Hashmap和Treemap の 区别

## linkedlist

```java
public static void main(String[] args) {
    用链表实现队列
    LinkedList queue = new LinkedList(); 
    queue.add(5);
    queue.add(7);
    queue.add(3);

    FIFO 先进先出
    System.out.println(queue.removeFirst());
    System.out.println(queue.removeFirst());
    System.out.println(queue.removeFirst());
    打印出 573

    FILO 先进后出
    System.out.println(queue.removeLast());
    System.out.println(queue.removeLast());
    System.out.println(queue.removeLast());
    打印出 375
}
```

## arraylist 和linkedlist 区别，为什么arrylist查询快

<https://www.bilibili.com/video/BV1xe4y1973f>

## ArrayList 和 LinkedList 的区别是什么？

<https://www.bilibili.com/video/BV1Vu411z7ws>

数据结构实现：ArrayList 是动态数组的数据结构实现，而 LinkedList 是双向链表的数据结构实现。
随机访问效率：ArrayList 比 LinkedList 在随机访问的时候效率要高，因为 LinkedList 是线性的数据存储方式，所以需要移动指针从前往后依次查找。
增加和删除效率：在非首尾的增加和删除操作，LinkedList 要比 ArrayList 效率要高，因为 ArrayList 增删操作要影响数组内的其他数据的下标。
综合来说，在需要频繁读取集合中的元素时，更推荐使用 ArrayList，而在插入和删除操作较多时，更推荐使用 LinkedList。

<https://www.bilibili.com/video/BV1uA411J7gK>

|  ArrayList | LinkedList   |
|---|---|
| 底层是【数组】  | 底层是【链表】  |
| 查询，速度更快  | 增删，速度更快  |
|   | 更占内存  |

<https://www.bilibili.com/video/BV1QW41167Gn>

## 追问：HashMap在哪个jdk版本使用红黑树，之前 の 实现方法是什么？

## hashmap put过程

## 集合

Collection:

- List (ArrayList)
- Queue (LinkedList)(ArrayDeque)
- Set (HashSet)

Map:

- HashMap (LinkedHashMap)

[java中LinkedHashMap和TreeMap是如何保证顺序的？](https://www.bilibili.com/video/BV1e44y1x7GS)

## hash算法 の 有哪几种，优缺点，使用场景

## 什么是一致性hash

<https://www.bilibili.com/video/BV193411u7Lq>

## Hash表是怎么实现 の

## is-a 和 has-a 继承

- is-a：判断【父子类】 の 关系
- has-a：判断【类】与【成员】 の 关系

```java
定义一个【成员变量】为【翅膀】：
public class Bird extends Animal {
    private String wings;
}
```

```java
如何判断是否是继承关系？

Dog is a Animal? ( •̀ ω •́ )yes
Cat is a Animal? ( •̀ ω •́ )yes

Dog dog = new Dog(); //✌ 成功
Cat cat = new Cat(); //✌ 成功
Animal animal1 = new Dog(); //✌ 成功
Animal animal2 = new Cat(); //✌ 成功
Dog dog =  new  animal; // ❌ 【编译】报错
Dog dog =  new  animal; // ❌ 【编译】报错

Animal animal = new Dog();
Dog dog = animal; //❌ 【编译】报错

Animal animal = new Dog();
Dog dog = (Dog) animal; //✌ 成功

Animal animal = new Dog();
Cat cat = (Cat) animal; //❌ 【运行】报错

public static void shower(Animal animal) {
    // ...
}

public static void main(String[] args) {
    shower(new Dog());
    shower(new Cat());
}
```

```java
java14 在 instanceof 之后，直接声明变量:


Animal animal = new Dog();
// Java 14 之前
if (animal instance of Dog) {
    Dog dog = (Dog) animal; //✌ 成功
}

// Java 14 直接声明变量:
if (animal instance of Dog dog) {
}
```

## Java中 の 反射（不了解，没问）

<https://www.bilibili.com/video/BV1L34y1S7a1>

## 什么时候用多态

<https://www.bilibili.com/video/BV18W411C7TC>

## 多态

多态 の 必要条件：

1. 要有引用
2. `父类引用`指向`子类对象`

```java
class Animal {
    public void eat() {
        System.out.println("宠物吃东西啦~");
    }
}

class 哈士奇 extends Animal {
    @Override
    public void eat() {
        System.out.println("哈士奇吃东西啦~");
    }
}

class 东北虎 extends Animal {
    @Override
    public void eat() {
        System.out.println("东北虎吃东西啦~");
    }
}

class 加菲猫 extends Animal {
    @Override
    public void eat() {
        System.out.println("加菲猫吃东西啦~");
    }
}

public void helpEat(Animal a) {
    a.eat();
}

helpEat(哈士奇);
helpEat(东北虎);
helpEat(加菲猫);
```

## 方法重载和方法重写

| 重载  | 重写  |
|---|---|
| 【方法名】相同  | 【方法名】相同  |
| 【参数列表】不同  | 【参数列表】相同  |
| 与【`返回值`类型】无关  | 【`返回值`类型】与【父类】一致，或者是【父类方法】的【子类】  |
| 可以重载【当前类】的method，也可以重载【父类】的method  | 在【子类】中定义和【父类】完全一致的方法  |

【参数列表】不同分【3种】情况：

- 参数个数
- 参数类型
- 参数顺序

## method overload 方法重载

`method名字`可以一致，但`参数列表`必须不一致

```java
class DoubleClass{
    int height;
    DoubleClass() {
        System.out.println("No parameter constructor");
        height = 444;
    }
    DoubleClass(int i) {
        System.out.println("No parameter is " + i);
        height = i;
    }
    void info() {
        System.out.println("No parameter is " + height);
    }
    void info(string s) {
        System.out.println(s + "No parameter is " + height);
    }
}
public class Main {
    public static void main(string[] args){
        这里是non static，所以必须new一个
        DoubleClass tmp = new DoubleClass(3)
        new DoubleClass()

        tmp.info();
        tmp.info("重载 の ：");
    }
}
```

## 可变参数

```java
【方法重载】时，首先，匹配【固定参数】：

public static void printNames(String name1) {
    System.out.println("方法 1");
}

public static void printNames(String name1, String name2) {
    System.out.println("方法 2");
}

public static void printNames(String... names) {
    System.out.println("方法 3");
}

public static void main(String[] args) {
    printNames("张三"); // 方法 1
    printNames("张三", "李四"); // 方法 2
    printNames("张三", "李四", "王五"); // 方法 3
}
```

```java
【可变参数】必须放在【固定参数】 の 后面：
public static void printNames(String name1, String... names) {
    //...
}
```

【可变参数】 与 【数组】  の 区别：

```java
【可变参数】：

public static void printNames(String... names) {
    for (String name : names) {
        System.out.println(name + "");
    }
}

public static void main(String[] args) {
    printNames(); // 可以不传参
    printNames("张三"); // 传参 1 个
    printNames("张三", "李四"); // 传参 n 个
}

【数组】：

public static void printNames(String[] names) {
    for (String name : names) {
        System.out.println(name + "");
    }
}

public static void main(String[] args) {
    printNames(null); // 可以传 【null】
    printNames({"张三"}); // 需要构建 【数组】
    printNames({"张三", "李四"}); 
    printNames(new String[] {"张三"}); // 需要构建 【数组】
    printNames(new String[] {"张三", "李四"}); 
}
```

## 内部类（静态、成员、局部、匿名）

作用 の 范围不同：

- 程序（public class）
- 包  （class）
- 静态（静态内部类）
- 成员（成员内部类）
- 局部（局部内部类、匿名内部类）

```java
静态内部类：不需要实例化
public class Outer {
    // 要有static final
    private static final String a = "static final 静态属性";
    public static class StaticClass {
        public void fun() {
            System.out.println(b);
        }
    }
    public static void main(String[] args) {
        Outer outer = new Outer();
        // 访问成员内部类
        outer.StaticClass().fun()
    }
}
```

```java
成员内部类：需要实例化
public class Outer {
    private String b = "成员属性";
    public class MemberClass {
        public void fun() {
            System.out.println(b);
        }
    }
    public static void main(String[] args) {
        Outer outer = new Outer();
        // 访问成员内部类
        outer.new MemberClass().fun()
    }
}
```

```java
局部内部类：
public static void fun() {
    String c = "局部变量"
    class LocalClass{
        public void run() {
            System.out.println(c);
        }
    }
    new LocalClass().run();
}

public static void fun() {
    String c = "局部变量"
    class LocalClass implements Runnable {
        @Override
        public void run() {
            System.out.println(c);
        }
    }
    LocalClass localClass = new LocalClass();
    localClass.run();
}

完全等价于 →
```

```java
匿名内部类：
public static void fun() {
    String c = "局部变量"
    Runnable anonymousClass = new Runnable() {
        @Override
        public void run() {
            System.out.println(c);
        }
    }
    anonymousClass.run();
}
完全等价于 →
```

```java
lambda 表达式：
public static void fun() {
    String c = "局部变量"
    Runnable anonymousClass = () -> System.out.println(c);
    anonymousClass.run();
}
```

## && 和 & 的区别

<https://www.bilibili.com/video/BV1sQ4y1B74C>

`A && B` 为短路运算，只要 A 为 false，那么 B 就不要算了，所以效率更高

```java
// https://www.bilibili.com/video/BV1dz411v7Cr

private static int j = 0;
private static Boolean methodB (int k) {
    j += k;
    return true;
}

public static void methodA (int i) {
    boolean b = methodB (i);
    b = i < 10 | methodB (4);
    b = i < 10 || methodB (8);
}

public static void main (String[] args) {
    methodA(0);
    System.out.println(j);
}

最终输出，4
```

## i++ 和 ++i 的区别

j = i++ - 2 是先算 j赋值 再算 i
j = ++i - 2 是先算 i 再算 j赋值

```java
public static void main(String[] args) {
    int a = 1;
    for (int i = 0; i < 10; ++i) {
        a = a++
    }
}
a最终仍然是1
        // 相当于
        int tmp = a++; 
        // tmp先赋值为1，a变成2
        a = tmp; 
        // a变成1
```

## 引用类型

自定的class

数组

## Java 中都有哪些引用类型？

<https://www.bilibili.com/video/BV1ST411J7Bk>

强引用：发生 gc 的时候不会被回收。
软引用：有用但不是必须的对象，在发生内存溢出之前会被回收。
弱引用：有用但不是必须的对象，在下一次GC时会被回收。
虚引用（幽灵引用/幻影引用）：无法通过虚引用获得对象，用 PhantomReference 现虚引用，虚引用的用途是在 gc 时返回一个通知。

## String、StringBuffer 与 StringBuilder 之间区别

<https://www.bilibili.com/video/BV1b3411G7gr>

<https://www.bilibili.com/video/BV1KQ4y1z76p>

<https://www.bilibili.com/video/BV1G3411c7cV>

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

## 线程 の 创建方式？

## 多线程了解吗？（当时脑子一抽，我以为要写多线程代码，回答了不熟悉，他就没多问了）

## ThreadLocal 是什么？有哪些使用场景？

<https://www.bilibili.com/video/BV1Yb4y1s7RG>

ThreadLocal 为每个使用该变量的线程提供独立的变量副本，所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
ThreadLocal 的经典使用场景是数据库连接和 session 管理等。

## threadlocal原理，应用场景

<https://www.bilibili.com/video/BV1iZ4y127wm>

## threadlocal oom是为什么

## 线程的创建方式

## 请说一下对象的创建过程？

<https://www.bilibili.com/video/BV1c44y1P7Xx>

## ClassA a = new Class(1) 在jvm中怎么存储

## java方法是值传递还是对象传递

<https://www.bilibili.com/video/BV1xL4y1w7jy>

## 序列化和反序列化的理解?

序列化和反序列化的提出，是为了解决——如何把一个【对象】从一个【JVM进程】传输到【another 进程】

- 序列化，就是我们为了方便【对象】的【传输、保存】，把【对象】转化为【其他形式】，比如【字节】

- 反序列化，就是【其他形式】转化为【对象】的过程

序列化的前提，是为了保证【通信双方】对于对象的【可识别性】，所以，我们会把对象转化为【通用的解析格式】，比如【JSON、Xml】，从而实现【跨平台、跨语言】的【可识别性】。

## 序列化如何选择？

市面上，开源的【序列化技术】非常多：

- JDK序列化
- protobuf
- hession
- xml
- JSON
- Kyro

实际应用中，哪种【序列化】更合适？要看以下几点：

1. 【序列化】以后的【数据大小】，因为【数据大小】会影响【传输性能】
2. 【序列化】的【性能】，【序列化耗时】较长会影响【业务性能】
3. 是否支持【跨平台、跨语言】
4. 技术【成熟度】，越【成熟】的方案使用的公司越【多】，也就越【稳定】

## Java对象的序列化操作方式

[设计源于生活中](https://www.bilibili.com/video/BV1f54y1W7Js)

```java
Serializable不可以删除
public class Dog implements Serializable {
    public Dog() {
        System.out.println("Dog()");
    }
}
public static void main(String[] args) throws Exception{
    ObjectOutputStream oos = new ObjectOutputStream(System.out, true);
    oos.writeObject(new Dog());
}
```

## Java如何使用json序列化？

[设计源于生活中](https://www.bilibili.com/video/BV1iK411u7YF)

1. 导入阿里巴巴的`fastjson包`中的`JSON对象`

```java
import com.alibaba.fastjson.JSON;
```

2. 用`JSON对象`的`toJSONString()方法`将它序列化

```java
String s = JSON.toJSONString(dog);
```

3. 需要注意的是，`DOG对象`如果没有`get方法`，则无法完成序列化

```java
Serializable可以删除
public class DOG implements Serializable {
    private int price;
    private String dagName;

    必须有get方法：
    public int getPrice() {
        return price;
    }

    public String getDagName() {
        return dogName;
    }

    public Dog() {
        System.out.println("Dog()");
    }
    public Dog(String dagName, int price) {
        this.dagName = dagName;
        this.price = price;
    }
}
```

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

<https://www.bilibili.com/video/BV1wL4y1q7z5>

•序列化：java.io.ObjectOutputStream 类的 writeObject() 方法可以实现序列化

•反序列化：java.io.ObjectInputStream 类的 readObject() 方法用于实现反序列化。

序列化就是一种用来处理对象流的机制，所谓对象流也就是将对象的内容进行流化。可以对流化后的对象进行读写操作，也可将流化后的对象传输于网络之间。序列化是为了解决在对对象流进行读写操作时所引发的问题。序列化的实现：将需要被序列化的类实现Serializable接口，该接口没有需要实现的方法，implements Serializable只是为了标注该对象是可被序列化的，然后使用一个输出流(如：FileOutputStream)来构造一个ObjectOutputStream(对象流)对象，接着，使用ObjectOutputStream对象的writeObject(Object obj)方法就可以将参数为obj的对象写出(即保存其状态)，要恢复的话则用输入流。

## Java面向对象三大特性

<https://www.bilibili.com/video/BV1rf4y1E7u9>

## 面向对象 の 【三大特性】

封装、继承、多态

## 说下对象完整创建流程

<https://www.bilibili.com/video/BV1J3411G7wA>

## new String("abc")到底创建了几个对象？

<https://www.bilibili.com/video/BV1MS4y1b75Q>

## 连接池

<https://www.bilibili.com/video/BV1yR4y1K7Z7>

## java中类和对象 の 关系

| 类  | 对象  |
|---|---|
| 一组【相同or相似】的【属性、method】的事物的【抽象描述】  | 这类事物的一个【具体实例】  |
| 【规定】了一种【数据类型】的【属性、method】，是创建对象的【模板】  | 根据【类的规定】，在【内存】中【开辟】了一块【具体空间】，这块空间的【属性数据、method】和【类的规定】是一致的。我们可以在内存中开辟多个相同结构的【空间】  |

## 什么是Java虚拟机为什么要使用？

<https://www.bilibili.com/video/BV1Fg411278C>

## Jvm垃圾回收器:serial

<https://www.bilibili.com/video/BV1rK411u7bk>

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

<https://www.bilibili.com/video/BV1aW41167rS>

<https://www.bilibili.com/video/BV1xK4y197SH>

[什么样 の 对象会被老年代回收](https://www.bilibili.com/video/BV13K4y1G7Bs)

## 什么时候才会进行垃圾回收？

1. GC 是由 JVM 【自动】完成的。
   1. 当【Eden区 or S区】不够用
   2. 【老年代】不够用
   3. 【方法区】不够用
2. 也可以通过 `System.gc()方法` 【手动】垃圾回收

但【上述两种方法】无法控制【具体的回收时间】

## Java如何获取字节码对象

```java
public static void main(String[] args) {
    // 方案一：
    Class clazzDog = Dog.class;
    // 方案二：
    Dog dog = new Dog();
    Class clazzDog2 = Dog.getClass();
    System.out.println(clazzDog == clazzDog2);

    返回true，说明两者指向同一个地址
}
```

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

<https://www.bilibili.com/video/BV1JW411r758>

## 双亲委派机制

[请介绍类加载过程，什么是双亲委派？](https://www.bilibili.com/video/BV1g94y1d787)

<https://www.bilibili.com/video/BV19F411g7B1>

<https://www.bilibili.com/video/BV1vf4y1B7eQ>

自底向上地查看，是否加载过这个类

如果没有，再自顶向下，尝试去加载这个类

[类加载机制和类加载器 双亲委派](https://www.bilibili.com/video/BV17M4y1G7W7)

[类加载机制](https://www.bilibili.com/video/BV1e34y177YY)

<https://www.bilibili.com/video/BV1ja411L7k1>

## 我们自己定义 の java.lang.String类是否可以被类加载器加载

<https://www.bilibili.com/video/BV1MF411e7zY>

## String类是否可以被继承

<https://www.bilibili.com/video/BV1ev411g7Xk>

## JVM性能优化 - 存在哪些问题？

- GC 频繁
- 死锁
- oom
- 线程池不够用
- CPU负载过高

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

<https://www.bilibili.com/video/BV1nS4y1K7n4>

<https://www.bilibili.com/video/BV1GF411373W>

## 栈和栈桢

[设计源于生活中](https://www.bilibili.com/video/BV1YA411J7wE)

JVM中的【方法stack】是【线程私有】。每一个method的调用，都会在【方法stack】中压入一个【栈桢】。

如果启动main方法，stack中，压入main方法的栈桢。

执行methodA方法，stack中，压入methodA方法的栈桢。

执行methodB方法，stack中，压入methodB方法的栈桢。

然后methodB执行结束，methodB出栈

然后methodA执行结束，methodA出栈

然后main执行结束，main出栈

## JVM中，哪些是共享区，哪些可以作为gc root

<https://www.bilibili.com/video/BV1Uq4y1e7Kq>

## jvm内存结构介绍

<https://www.bilibili.com/video/BV1jY4y1b7n1>

Java の 内存分为5个部分：

- 线程共享：
  - 方法区(method area) 🔸 常量池 + 静态变量 + 类
  - 堆(heap) 🔸 GC 对象，即【实例对象】，内存最大 の 区域

- 每个线程一份【线程隔离】：
  - 虚拟机栈(VM stack)→(heap) 🔸 每个java被调用时，都会创建【栈帧】
    - `栈帧`内包含：
      - `局部变量表`：存储【局部变量】：变量dog、cat等
      - `操作数栈`：线程执行时，使用到的【数据存储空间】
      - `动态链接`：method区的引用，包括【类信息、常量、静态变量】
      - `返回地址`：存储这个method被【调用的位置】，因为method结束以后，需要【返回】到被【调用的位置】

  - 本地方法栈 🔸 native语言
  - 程序计数器(pc register) 🔸【*当前线程*】执行到 の 【字节码行号】

[了解JVM内存区域吗？](https://www.bilibili.com/video/BV1Vt4y1V7cz)

[2分钟学会对象和引用如何存储在JVM内存](https://www.bilibili.com/video/BV1S5411Q78u)

## 项目中如何规划常量？

通常放到 `Constants类` 中

```java
public final class Constants {
    public static final String ApplicationContextXml = "xxxx"
    public static final String ApplicationWebXml = "xxxx"
    public static final String WebXml = "xxxx"
    。。。。。。。。。。。。。。。。。。
}
```

## 【pass💦】Java 中有多少种引用类型？它们 の 都有什么使用场景？

<https://www.bilibili.com/video/BV1e94y1D7ug>

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

<https://www.bilibili.com/video/BV1hA4y1d7gU>

## 【pass💦】为了更好 の 控制【主内存】和【本地内存】 の 交互，Java 内存模型定义了八种操作来实现

lock：锁定。 主内存 の 变量，把一个变量标识为一条线程独占状态。
unlock：解锁。 主内存变量，把一个处于锁定状态 の 变量释放出来，释放后 の 变量才可以被其他线程锁定。
read：读取。 主内存变量，把一个变量值从主内存传输到线程 の 【工作内存】中，以便随后 の load动作使用
load：载入。 【工作内存】 の 变量，它把read操作从主内存中得到 の 变量值放入【工作内存】 の 变量副本中。
use：使用。 【工作内存】 の 变量，把【工作内存】中 の 一个变量值传递给【执行引擎】，每当虚拟机遇到一个需要使用变量 の 值 の 字节码指令时将会执行这个操作。
assign：赋值。 【工作内存】 の 变量，它把一个从【执行引擎】接收到 の 值赋值给【工作内存】 の 变量，每当虚拟机遇到一个给变量赋值 の 字节码指令时执行这个操作。
store：存储。 【工作内存】 の 变量，把【工作内存】中 の 一个变量 の 值传送到主内存中，以便随后 の write の 操作。
write：写入。 主内存 の 变量，它把store操作从【工作内存】中一个变量 の 值传送到主内存 の 变量中。

## jvm内存中，堆和栈 の 区别？堆和栈 の 关系

<https://www.bilibili.com/video/BV1RW411C7yb>

|   |  stack |  heap |
|---|---|---|
| 存储  | 局部变量、引用  | instance 对象  |
| 速度  |  fast |  slow |
| 线程共享  | Thread Stack  | 共享 heap  |
| GC |   | GC 对象，占用内存最大  |
| 指向关系 | 出  | 入  |

## 堆内内存

年轻代【Eden、From、To】 + 老年代 + 持久代

## 堆外内存【定义】

堆外内存 = 物理机内存

【java虚拟机堆】以外的内存，这个区域是受【操作系统】管理，而不是jvm。

## 堆外内存优点

减少jvm垃圾回收

加快数据复制的速度

## JVM如何判断一个对象可以被回收？

<https://www.bilibili.com/video/BV1RB4y117ne>

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

<https://www.bilibili.com/video/BV1h64y1x7SF>

## java GC算法，标记-清楚、标记-复制、标记-整理

## 4.g1回收器、cms の 回收过程，场景

[CMS比较严重 の 问题并发收集阶段再次触发Full gc怎么处理](https://www.bilibili.com/video/BV1BA4y1R7W7)

[G1垃圾收集器最大停顿时间是如何实现 の](https://www.bilibili.com/video/BV1TB4y1s7rh)

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

## 内存溢出

<https://www.bilibili.com/video/BV1gW411r7By>

## Objects类中有哪些方法？

## 一个空Object对象的占多大空间

<https://www.bilibili.com/video/BV1SG411h7ju>

## static

<https://www.bilibili.com/video/BV1nW41167o1>

<https://www.bilibili.com/video/BV1zL411s7h9>

静态修饰符，代表这个类`固有的`，在这个类里面共享，不需要`new一个实例`

`non-static method 非静态方法` = `instance method 实例方法` = new一个实例

## public

如果a包下的`A类`是`public`的，它的字段和方法都是private的。

→ 在`b包`下的`B类`可以创建`A类`的对象，但是无法访问`A类对象的字段和方法`。

如果a包下的`A类``没有修饰符`，它的字段和方法都是private的。

→ 在`a包`下的B类可以创建`A类`的对象，但无法访问A类对象的字段和方法。

→ 在`b包`下的B类无法创建A类的对象。

## 访问修饰符

<https://www.bilibili.com/video/BV1bf4y1c7Pu>

## final 值 方法 类有什么区别，容器被final修饰会怎样

[2分钟记住final の 使用](https://www.bilibili.com/video/BV19A4y1o7ET)

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

## 先调用【继承の构造器】，再调用【实例の构造器】

看这个视频

[设计源于生活](https://www.bilibili.com/video/BV1zv411B7g2)

```java
public class Demo {
    class Super {
        int flag = 1;
        Super() {
            test();
        }
        void test() {
            System.out.println("Super.test() flag = " + flag);
        }
    }
    class Sub extends Super {
        Sub(int i) {
            flag = i;
            System.out.println("Sub.sub() flag = " + flag);
        }
        void test() {
            System.out.println("Sub.test() flag = " + flag);
        }
    }

    public static void main(String[] args) {
        new Demo().new Sub(5);
    }

}

输出

Sub.test() flag = 1
Sub.sub() flag = 5
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




## Netty

https://www.bilibili.com/video/BV1GN4y1g7Wi

https://www.bilibili.com/video/BV1xq4y1g73f



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



## CPU飙高系统反应慢怎么排查？

https://www.bilibili.com/video/BV1nF41147yu



## Linux查看内存、CPU等状态？Linux查看进程 の 内存消耗和CPU消耗？

查看内存使用情况：`free`

显示进程信息（包括CPU、内存使用等信息）：`top、ps`

## 什么是【虚拟内存】和【物理内存】

物理内存：【内存条】存储容量的大小

虚拟内存：是【计算机内存管理技术】，它让程序认为它具有【连续可用】的【4GB 内存】，而实际上，映射到多个【物理内存碎片】













## spark那些外部资源 还有第三方jar包之类 の 都放在哪（应该是这么问 の ，不太会，说了下内存结构，告诉我是java classloader相关 の 机制）


## 排序算法和时间复杂度

https://www.bilibili.com/video/BV1Sg411M7Cr



## java执行 の 过程







## java集合

## 介绍一下jvm の 区域






## java sout（0.1+0.2）输出什么，为什么






## 隐含 の 强制类型转换

https://www.bilibili.com/video/BV1Fb4y1Y7wX



## Java集合 の 框架体系图



## 遇到oom怎么排查（有jvm提供 の 工具查看堆栈使用情况，具体不太了解只在跑spark遇到过、分享了下spark里 の 排查经验和参数调整）













## 浏览器从url输入到返回 の 流程

## 1T文件，每行是一个数字，机器128G内存，求top10数字





## 如何进行etl链路优化

## 如果链路慢，但是没有sla风险，怎么优化（类似上一道题）



## 怎么数据链运行慢定位问题，对任务进行优化



## 索引失效场景

https://www.bilibili.com/video/BV1yr4y1E7Xu

https://www.bilibili.com/video/BV1pr4y1p7Ak



##  如何保证MySQL数据库的高可用性？ 

https://www.bilibili.com/video/BV1aL4y1G7jT










## 去重如果不用 set 还有多少种方式，有没有更高效 の 方式？

## 怎么解决幻读 具体在sql是怎么实现 の 



## 什么是【一致性】读？

事务启动的时候，根据【某个条件】去【读取数据】，直到【事务结束】再去执行【相同的条件】，还是读到【同一份数据】，不会发生变化



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


