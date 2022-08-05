## 泛型中extends和super的区别

`< ? extends T >` 表示包括 T 在内的任何T的子类
`< ? super T >` 表示包括 T 在内的任何T的父类

## 泛型类

既保证了**通用性**，又保证了**独特性**

原本繁杂的代码：

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

使用了`泛型`以后就变得简单，用`符号`代替`具体的类型`，从而只定义了一个 ArrayList

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
- 将`运行`时的错误，转到了`编译时期`，以免造成`严重后果`
- 在获取`元素`时，就不需要`类型转换`了，获取到的`元素`就是`指定类型`

在书写泛型类时，通常做以下的约定：

`E`表示Element，通常用在`集合`中；

`ID`用于表示`对象的唯一标识符类型`

`T`表示Type(类型)，通常指`代类`；

`K`表示Key(键),通常用于`Map`中；

`V`表示Value(值),通常用于`Map`中，与K结对出现；

`N`表示Number,通常用于表示`数值类型`；

`？`表示`不确定的Java类型`；

`X`用于表示`异常`；

`U,S`表示任意的类型。

下面时泛型类的书写示例：

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

- `泛型类`中的 `泛型参数` - 需要在【实例化】该类时，指定【具体类型】
- `泛型类`中的 `泛型参数` - 在运行时被擦除，也就是说，没有 `ArrayList<String>`, `ArrayList<Integer>`。只有 `ArrayList`。
- `泛型类`中的 `泛型参数` - 静态方法：无法访问。if 要使用泛型，必须定义成【泛型方法】
- `泛型类`中的 `泛型参数` - 使用【基本类型】时 - 会自动装箱成【包装类】
  
```java
public class ArrayList<E> {
    静态方法：无法访问`泛型类`的`泛型参数`
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
        System.out.println("我的名字是%s, 今年%d岁", name, age);
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
        打印结果为：我的名字是张三, 今年16岁。我是一名学生。
    }

}
```

## 引用拷贝、浅拷贝、深拷贝

指的是【对象】的拷贝

| 引用拷贝  | 浅拷贝  | 深拷贝  |
|---|---|---|
| only复制【地址】  | 创建【新的对象】，但仍然复制【引用类型】的【地址】  | 创建totally【新的对象】  |
|    |    | 对new对象的修改，不会影响old对象的值  |

需要通过 `cloneable接口` 和 `clone()方法`，然后，我们可以在`clone()方法`里面，可以实现`浅拷贝`和`深拷贝`的一个逻辑。

```java
不建议直接使用 clone(), 容易抛出异常，可以自己编写其他的方法来实现。

public class Person implements Cloneable {
    public int age;
    public int[] arr = {1,2,3}; // 调用引用类型的clone

    public Person(int age) {
        this.age = age;
    }

    @Override
    public Person clone() {
        try {
            Person person = (Person) super.clone();
            person.arr = this.arr.clone(); // 调用引用类型的clone
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

## 深拷贝 & 浅拷贝的代码实现

```java
浅拷贝: board 仍然指向原来的【地址】
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
深拷贝: 创建一个全新的对象
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

## 实现`深拷贝`的方法

1. 序列化。把一个对象先`序列化`，再`反序列化`回来。
2. 在`clone()方法`里面，重写`clone的逻辑`，对克隆对象的`内部引用变量`，再进行一次克隆。

```java
person.arr = this.arr.clone()
```

## Java有几种文件拷贝方式，哪一种效率最高？

1. 使用 java.io 包里面的库，使用 FileInputStream 读取，使用 FileOutputStream 写出。
2. 利用 java.nio 包下面的库，使用 TransferTo 或 TransferFrom 实现
3. 使用 java 标准类库 本身提供的 Files.copy

其中 nio 里面提供的 TransferTo 或 TransferFrom，可以实现【零拷贝】。【零拷贝】可以利用【操作系统底层】，避免不必要的 【copy】和【上下文切换】，因此，在性能上表现比较好。

## IO 和 NIO 的区别?

关于这个问题，我会从下面几个方面来回答：

1. IO 指的是, 实现【数据】从【`磁盘`】中【读取 or 写入】。
除了【`磁盘`】以外，【`内存 or 网络`】都可以作为【IO 流】的【来源 or 目的地】。

2. 当【程序】面向的是【网络】进行数据IO操作时，Java 里面通过【socker】来实现【网络通信】

3. 基于【socker】的IO，属于【阻塞IO】。也就是说，当server受到【client请求】，需要等到 【client请求】的数据处理完毕，才能获取下一个【client请求】。so, 基于【socker】的IO 能够连接数量非常少。

4. NIO 是 新增的 new IO 机制。提供了【非阻塞IO】，也就是说，在【连接未就绪 or IO未就绪】的情况下，服务端不会【堵塞】当前连接，而是继续【轮询】后续的链接来处理。so, NIO可以【并行处理】的连接数量非常多。

5. NIO 相比于【传统的 old IO】，效率更高

## BIO、NIO、AIO 有什么区别？

`BIO`：Block IO - `同步阻塞式 IO`

- 当server受到【client请求】，需要等到 【client请求】的数据处理完毕，才能获取下一个【client请求】。so, BIO 能够连接数量非常少。

`NIO`：New IO - `同步非阻塞 IO`

- 在【连接未就绪 or IO未就绪】的情况下，服务端不会【堵塞】当前连接，而是继续【轮询】后续的链接来处理。so, NIO可以【并行处理】的连接数量非常多。

`AIO`：Asynchronous IO - `异步非堵塞 IO`

- 是 NIO 的升级，也叫 NIO2

## 异常的两个【子类实现】？java中异常的分类？

所有异常，都 派生自 throwable，它有两个【子类实现】：

1. 一个是 error【非受检异常】
2. 一个是 exception【非受检异常 + 受检异常】

- 1️⃣error 是【程序底层 or 硬件层面】的错误，与程序无关，比如，OOM
- 2️⃣exception 是【程序里面】的异常。包括：
    1. RuntimeException
    2. 其他

## java中异常的分类？对受检异常和非受检异常的理解？

- 1️⃣【受检异常】指：`编译异常`
  - 在【编译阶段】，需要【强制检查】。
  - 程序无法预判的异常，比如，IOException、SQLException

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
    throw new MyException("没有一键三连，程序崩溃");     // 抛出异常的同时，传递异常信息
    throw new MyException(new NullPointerException()); // 抛出异常的同时，传递其他异常的堆栈信息
    throw 只能在代码块中
}
```

## 【受检异常】的两种处理方式

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

- 导入一个包中所有的类，采用 *

```java
import java.lang.*
import java.util.ArrayList
import java.util.HashMap
```

- java中的`常量`：`const`
- java中`伴生对象`：`static关键字`
- java中的`trait`：接口`interface`

## 大 の log文件中，统计异常出现 の 次数、排序，或者指定输出多少行多少列 の 内容

```s
cat /data/logs/server.log  | grep Exception | sort | uniq -c | sort -nr | head -n 50

cat 显示文件内容

grep后仅显示包含"Exception"的行

sort后把相同的行排列到一起

uniq -c去除相同行;-c添加数量统计

sort -nr 按照数字倒序排列 这样就会把出现次数最多的异常显示在最前边.

head -n 50 只取头50行
```

## abstract class 抽象类

注意事项：

1、`抽象类`的修饰符必须为`public`或者`protected`, 不能是private, 因为抽象类需要`其子类去实现抽象方法`，private修饰就不能被子类继承，因此子类就不能实现改方法。
2、`抽象类`不能直接`实例化`，需要通过`普通子类`进行`实例化`。
3、如果子类`只实现了抽象父类中的一些方法`，那么该子类`任然是抽象类`（不能被实例化）。

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

2️⃣抽象类：当需要让`子类`继承`成员变量`，or 需要控制`子类的实例化`时

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

## Thread 和 Runnable 的区别

| Thread  | Runnable  |
|---|---|
| 类  | 接口  |
| 支持【单一继承】  | 可以支持【多继承】  |
|   | 已经存在【继承关系】的【类】  |
| 才是真正的【处理线程】  | 相当于是【任务】 |
| 真正处理【任务】  | 定义具体【任务】  |
| 【规范、标准】的实现 | 代表【规范、标准】|

## 多线程有几种实现方式？

有4种，分别是：

1. 继承`Thread类`
2. 实现`Runnable接口`
3. 实现`Callable接口`通过`FutureTask包装器`来创建`Thread线程`
4. 通过`线程池`创建线程，使用`线程池`接口`ExecutorService`结合`Callable、Future`实现有返回结果的多线程。

前面两种【无返回值】原因：

- 通过重写`run方法`，`run方法`的返回值是`void`。
  
后面两种【有返回值】原因：

- 通过`Callable接口`，这个方法的返回值是`Object`。

## 说一下 Runnable和 Callable有什么区别？

- Runnable 没有返回值，
- Callable 可以拿到有返回值，
- Callable 可以看作是 Runnable 的补充。

## 线程有哪些状态？

线程的6种状态：

`初始(NEW)`：新创建了一个【线程对象】，但还没有调用start()方法。

`运行(RUNNABLE)`：Java线程中将【就绪（ready）】和【运行中（running）】两种状态笼统的称为“运行”。

【线程对象】创建后，其他线程(比如main线程）调用了【该对象的start()方法】。

该状态的线程位于【可运行线程池】中，等待被【线程调度】选中，获取【CPU的使用权】，此时处于【就绪状态（ready）】。

【就绪状态】的线程在获得【CPU时间片】后变为运行中【状态（running）】。

`阻塞(BLOCKED)`：表示【线程】阻塞于锁。

`等待(WAITING)`：【进入该状态的线程】需要等待【其他线程】做出一些特定动作（通知或中断）。

`超时等待(TIMED_WAITING)`：该状态不同于WAITING，它可以在指定的时间后【自行返回】。

`终止(TERMINATED)`：表示该线程已经【执行完毕】。

## 线程 & 进程 の 区别

| 进程  | 线程  |
|---|---|
| 在【内存】中存在多个【程序】  | 每个【进程】有多个【线程】  |
| CPU采用【时间片轮转】的方式运行  |   |
| 实现【操作系统】的并发  | 实现【进程】的并发    |
| 通信较【复杂】  | 通信较【简单】，通过【共享资源】   |
| 重量级  |  轻量级  |
| 独立的运行环境  | 非独立，是【进程】中的一个【任务】|
| 单独占有【地址空间 or 系统资源】| 共享  |
|  进程之间相互隔离，可靠性高 | 一个【线程崩溃】，可能影响程序的稳定性  |
| 创建和销毁，开销大  |  开销小 |

时间片：CPU为每个【进程】分配一个【时间段】。

上下文切换：如果在【时间片】结束时，进程还在运行，则暂停进程的运行，给CPU分配另一个进程

## 线程池是如何实现线程复用的？

采用【生产者】和【消费者】的模式，去实现【线程】的复用。
采用一个【中间容器】去解构【生产者】和【消费者】

【生产者】不断地产生任务，保存到【容器】里面
【消费者】不断从【容器】消费任务

在【线程池】里面，需要保证【工作线程】的重复使用，所以使用了【阻塞队列】。

生产者线程：指 交【任务】到【线程池】的线程。保存到【阻塞队列】。然后，线程池里面的【工作线程】不断得从【阻塞队列】获取任务执行。

如果【阻塞队列】没有任何任务的时候，那么这些工作线程就会【阻塞等待】，直到又有【新的任务】进来，那么这些【工作线程】又再次被唤醒

## 线程池 の 工作原理

1. 【线程池】是一种池化技术，【池化技术】是一种【资源复用】的设计思想，常见的【池化技术】有：

- 连接池
- 内存池
- 对象池

2. 【线程池】复用的是【线程资源】。它的核心设计目的是：

- 减少【线程】的频繁【创建、销毁】带来的【性能开销】
- 【线程池】本身可以通过【参数】来控制【线程数量】，从而保护资源

3. 线程池的【线程复用技术】：【线程本身】并不【受控】，【线程的生命周期】由【任务的运行状态】决定，无法人为控制。为了实现【线程的复用】，线程池里面用到了【阻塞队列】。简单来说：

- 线程池内的【工作线程】会处于【一直运行】状态。
- 【工作线程】从【阻塞队列】里面，获取【待执行的任务】，一旦队列空了，那么，这个【工作线程】就会被阻塞，直到有【new的任务】进来
- 线程池里面的【资源限制】是通过几个【关键参数】来控制的

分别是：

1. 核心线程数
2. 最大线程数量

【核心线程数】是【默认长期存在】的【工作线程】
【最大线程数】是根据【任务情况】来【动态】创建的线程

以上，就是我对这个问题的理解


## Java官方提供了哪几种线程池

一共5种：

1. `new Cached Thread Pool`，是一种可以缓存的【线程池】，它可以用来处理【大量短期】的【突发流量】

特点：

- 最大线程数 = integer.MaxValue
- 存活时间 =  60 秒
- 阻塞队列 = Synchronous Queue 

2. `new Fixed Thread Pool`：是一种可以【固定线程数量】的【线程池】

特点：

- 任何时候最多有 n 个【工作线程】是活动的。
- 如果任务比较多，就会加到【阻塞队列】里面等待；

3. `new Single Thread Executor`：【工作线程】数目被限制为 1，无法动态修改

特点：

- 保证了所有任务的都是被【顺序执行】
- 最多会有一个任务处于活动状态；

4. `new Scheduled Thread Pool`：可以进行【定时 or 周期性】的工作调度；

5. `new Work Stealing Pool`：Java 8 才加入这个创建方法，

特点：

- 其内部会构建ForkJoinPool，
- 利用【Work-Stealing算法】，【并行】地处理任务，不保证顺序；

线程池的核心是：

ThreadPoolExecutor()：上面创建方式都是对ThreadPoolExecutor的封装。

## 当任务数超过【线程池】的【核心线程数】时，如何让它不进入队列？

当提交一个任务到【线程池】，它的【工作原理】可以分为【4个步骤】：

1. 预热【核心线程】
2. 把任务添加到【阻塞队列】
3. 如果添加到【阻塞队列】失败，则创建【非核心线程】增加【处理效率】
4. 如果【非核心线程数】达到了【阈值】，就触发【拒绝策略】

如果，我们希望【任务】不进入【阻塞队列】，我们只需要影响【第二步】的【执行逻辑】即可:

- 在Java 的【线程池】的【构造方法】的【一个参数】，可以修改【阻塞队列】的类型：

- 比如，Synchronous Queue 。是一个不能存储任何元素的阻塞队列。它的特性在于，每次【生产】一个任务，就必须及时【消费】这个任务。

- 从而避免【任务】进入到【阻塞队列】，而是直接去【启动】【最大线程数量】去处理

## 如果你提交任务时，线程池队列已满，这时会发生

当提交一个任务到【线程池】，它的【工作原理】可以分为【4个步骤】：

1. 预热【核心线程】
2. 把任务添加到【阻塞队列】
   - 如果是【无界队列】，可以继续提交
   - 如果是【有界队列】，且【队列满了】，且【非核心线程数】没有达到【阈值】
3. 则创建【非核心线程】增加【处理效率】
4. 如果【非核心线程数】达到了【阈值】，就触发【拒绝策略】

## 线程池如何知道一个线程的任务已经执行完成？

线程池如何知道一个线程的任务已经执行完成？

我会从 2 个方面，来回答这个问题：

1. 从线程池的内部：

当我们把一个任务交给【线程池】执行时，【线程池】会调度【工作线程】来执行这个任务的【run方法】，当【run方法】正常执行结束以后，也就意味着，这个任务完成了。

so，线程池的【工作线程】通过同步调用任务的【run方法】，并且等待【run方法】返回后，再去统计【任务的完成数量】

2. 从线程池的外部：

- isTerminated() 方法：可以去判断线程池的【运行状态】 ，可以【循环】判断该方法的【返回值】，to 了解线程的【运行状态】。一旦显示为 Terminated，意味着线程池中的【all 任务】都已经执行完成了。但需要主动在程序中调用线程池的【shutdown() 方法】，在实际业务中，不会主动去关闭【线程池】。so，这个方法在【使用性、灵活性】方面，都不是很好。
- submit() 方法：它提供了一个【Future 的返回值】，我们可以通过【Future.get() 方法】去获得【任务的执行结果】。当任务没有完成之前，【Future.get() 方法】将一直阻塞，直到任务执行结束。只要【Future.get() 方法】正常返回，则说明任务完成。
- CountDownLatch计数器：通过初始化指定的【计数器】去进行倒计时，其中，它提供了两个方法：分别是：
  
1. await() 阻塞线程
2. countDown()

去进行倒计时，一旦倒计时归0，所有阻塞在【await()方法】的线程，都会被【释放】。

总的来说，想要知道【线程是否结束】，必须获得【线程结束】之后的【状态】，而线程本身没有【返回值】，所以只能通过【阻塞-唤醒】的方式实现，Future.get() 和 CountDownLatch，都是采用这种方法

## synchronized 关键字

<https://www.bilibili.com/video/BV1q54y1G75e>

<https://www.bilibili.com/video/BV18y4y1V79v>

## 线程的状态转化

<https://www.bilibili.com/video/BV1G44y117rH>

## JVM中如何查看线程死锁

<https://www.bilibili.com/video/BV1Cb4y1p7dn>

<https://www.bilibili.com/video/BV15q4y1Y7Bo>

## 什么是【死锁】？

<https://www.bilibili.com/video/BV1C54y1r7HM>

【死锁】是一组【互相竞争资源】的线程，因为互相等待，导致【永久阻塞】

## 发生死锁的原因？

原因有4个：

1. ① 互斥条件。共享资源X和Y，只能被【一个线程】占用。
2. ② 占有且等待。【线程1】已经取得了【共享资源X】，在等待【共享资源Y】时，不释放【共享资源X】
3. ③ 不可抢占。其他线程不能抢占【线程1】占有的资源
4. ④ 循环等待。【线程1】等待【线程2】占有的资源，【线程2】等待【线程1】占有的资源

## 如何去避免死锁？

只要打破，上述任一条件，就能避免死锁。

而在这4个条件中，

1. ① 互斥条件。是无法被破坏的。因为【锁】本身就是通过【互斥】来解决【线程安全性】问题。所以对于剩下3个，
2. ② 占有且等待。我们可以一次性申请【all资源】，这样就不存在等待了
3. ③ 不可抢占。占有【部分资源】的线程，进一步申请其他资源时，如果申请不到，可以主动释放它占有的资源。这样【不可抢占】这个条件，就被破坏掉了。
4. ④ 循环等待。可以按照【顺序】申请资源，来进行预防。也就是说，资源是【线性顺序】的，申请时，先申请【资源序号】小的，再申请【资源序号】大的。

## 死锁的发生原因和怎么避免，两分钟讲的明明白白

<https://www.bilibili.com/video/BV1VY4y1q7tA>

## 在 Java 程序中怎么保证多线程的运行安全？

方法一：使用安全类，比如 Java. util. concurrent 下的类。
方法二：使用自动锁 synchronized。
方法三：使用手动锁 Lock。
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

## 多线程中 synchronized 锁升级的原理是什么？

synchronized 锁升级原理：在锁对象的对象头里面有一个 threadid 字段，在第一次访问的时候 threadid 为空，jvm 让其持有偏向锁，并将 threadid 设置为其线程 id，再次进入的时候会先判断 threadid 是否与其线程 id 一致，如果一致则可以直接使用此对象，如果不一致，则升级偏向锁为轻量级锁，通过自旋循环一定次数来获取锁，执行一定次数之后，如果还没有正常获取到要使用的对象，此时就会把锁从轻量级升级为重量级锁，此过程就构成了 synchronized 锁的升级。
锁的升级的目的：锁升级是为了减低了锁带来的性能消耗。在 Java 6 之后优化 synchronized 的实现方式，使用了偏向锁升级为轻量级锁再升级到重量级锁的方式，从而减低了锁带来的性能消耗。

## 什么是死锁？

当线程 A 持有独占锁a，并尝试去获取独占锁 b 的同时，线程 B 持有独占锁 b，并尝试获取独占锁 a 的情况下，就会发生 AB 两个线程由于互相持有对方需要的锁，而发生的阻塞现象，我们称为死锁。

## 怎么防止死锁？

<https://www.bilibili.com/video/BV1U44y1V7HF>

尽量使用 tryLock(long timeout, TimeUnit unit)的方法(ReentrantLock、ReentrantReadWriteLock)，设置超时时间，超时可以退出防止死锁。
尽量使用 Java. util. concurrent 并发类代替自己手写锁。
尽量降低锁的使用粒度，尽量不要几个功能用同一把锁。
尽量减少同步的代码块。

## 说一下 synchronized 底层实现原理？

synchronized 是由一对 monitorenter/monitorexit 指令实现的，monitor 对象是同步的基本实现单元。在 Java 6 之前，monitor 的实现完全是依靠操作系统内部的互斥锁，因为需要进行用户态到内核态的切换，所以同步操作是一个无差别的重量级操作，性能也很低。但在 Java 6 的时候，Java 虚拟机 对此进行了大刀阔斧地改进，提供了三种不同的 monitor 实现，也就是常说的三种不同的锁：偏向锁（Biased Locking）、轻量级锁和重量级锁，大大改进了其性能。

## synchronized 和 volatile 的区别是什么？

volatile 是变量修饰符；synchronized 是修饰类、方法、代码段。
volatile 仅能实现变量的修改可见性，不能保证原子性；而 synchronized 则可以保证变量的修改可见性和原子性。
volatile 不会造成线程的阻塞；synchronized 可能会造成线程的阻塞。

## synchronized 和 Lock 有什么区别？

synchronized 可以给类、方法、代码块加锁；而 lock 只能给代码块加锁。
synchronized 不需要手动获取锁和释放锁，使用简单，发生异常会自动释放锁，不会造成死锁；而 lock 需要自己加锁和释放锁，如果使用不当没有 unLock()去释放锁就会造成死锁。
通过 Lock 可以知道有没有成功获取锁，而 synchronized 却无法办到。

## synchronized 和 ReentrantLock 区别是什么？

<https://www.bilibili.com/video/BV12a411Y7hn>

<https://www.bilibili.com/video/BV1kF411T7os>

synchronized 早期的实现比较低效，对比 ReentrantLock，大多数场景性能都相差较大，但是在 Java 6 中对 synchronized 进行了非常多的改进。
主要区别如下：

ReentrantLock 使用起来比较灵活，但是必须有释放锁的配合动作；
ReentrantLock 必须手动获取与释放锁，而 synchronized 不需要手动释放和开启锁；
ReentrantLock 只适用于代码块锁，而 synchronized 可用于修饰方法、代码块等。
ReentrantLock 标记的变量不会被编译器优化；synchronized 标记的变量可以被编译器优化。

## ReentrantLock 是如何实现锁公平和非公平性的？

<https://www.bilibili.com/video/BV1y5411D7Jj>

## 说一下 atomic 的原理？

atomic 主要利用 CAS (Compare And Swap) 和 volatile 和 native 方法来保证原子操作，从而避免 synchronized 的高开销，执行效率大为提升。

## wait和notify 为什么要在synchronized代码块中

<https://www.bilibili.com/video/BV1xr4y1p7w6>

## BLOCKED和WAITING有什么区别？

[](https://www.bilibili.com/video/BV14a411j7c1)

`阻塞(BLOCKED)`：表示【线程】阻塞于锁。
`等待(WAITING)`：【进入该状态的线程】需要等待【其他线程】做出一些特定动作（通知或中断）。

## sleep() 和 wait() 有什么区别？

[wait和sleep是否会触发锁的释放以及CPU资源的释放？](https://www.bilibili.com/video/BV1u3411u7fQ)

类的不同：sleep() 来自 Thread，wait() 来自 Object。
释放锁：sleep() 不释放锁；wait() 释放锁。
用法不同：sleep() 时间到会自动恢复；wait() 可以使用 notify()/notifyAll()直接唤醒。

42.notify()和 notifyAll()有什么区别？
notifyAll()会唤醒所有的线程，notify()之后唤醒一个线程。notifyAll() 调用后，会将全部线程由等待池移到锁池，然后参与锁的竞争，竞争成功则继续执行，如果不成功则留在锁池等待锁被释放后再次参与竞争。而 notify()只会唤醒一个线程，具体唤醒哪一个线程由虚拟机控制。

## 如果一个线程两次调用start()，会出现什么？

<https://www.bilibili.com/video/BV1yg411Z7qd>

## Files线程的 run() 和 start() 有什么区别？

start() 方法用于启动线程，run() 方法用于执行线程的运行时代码。run() 可以重复调用，而 start() 只能调用一次。

## 线程之间如何进行通讯

<https://www.bilibili.com/video/BV16T4y1i7VL>

## 线程和进程的区别？

一个程序下至少有一个进程，一个进程下至少有一个线程，一个进程下也可以有多个线程来增加程序的执行速度。

## 守护线程是什么？

<https://www.bilibili.com/video/BV1i34y1E7Ge>

守护线程是运行在后台的一种特殊进程。它独立于控制终端并且周期性地执行某种任务或等待处理某些发生的事件。在 Java 中垃圾回收线程就是特殊的守护线程。

## Java多线程的Future模式

<https://www.bilibili.com/video/BV1ov41167vH>

## lock与synchronized区别

[使用lock锁的坑，千万要小心死锁的问题](https://www.bilibili.com/video/BV1ch411d7Rq)

[Lock锁的使用](https://www.bilibili.com/video/BV1kK4y1s75N)

```java
lock.lock();
// ...
lock.unlock();

sychronized{
  // ...
}
```

| lock  | synchronized  |
|---|---|
| 接口  | 关键字  |
| 【手动】获得锁，释放锁  | 【自动】获得锁，释放锁     |
| 适用于：线程方程多   |  适用于：线程少  |

## 接口`method名称`冲突

`类`的优先级比`接口`高

`子类`的优先级比`父类`高

一言以蔽之！！！就是越具体的越优先

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

[java中的数据类型](https://www.bilibili.com/video/BV1Gr4y127G4)

[八种基本类型包装类的常量池是如何实现的](https://www.bilibili.com/video/BV1d94y127YA)

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

## equals和==区别？

<https://www.bilibili.com/video/BV1cW411Z7XB>

<https://www.bilibili.com/video/BV13q4y1d7kg>

## 为什么两个 Integer对象 不能用 == 来判断？

<https://www.bilibili.com/video/BV1x54y1q7q6>

Integer 是一个【封装类型】。它对应的是一个【int类型】的包装，在java里面，之所以要提供 Interger，是因为 Java 是一个【面向对象】的语言，而【基本类型】不具备【对象】的特征，所以在【基本类型】之上，做了一层【对象】的包装，从而能够完成【基本类型】的一些操作。

在【封装类型】里面，除了【int类型的操作】以外，还包括【享元模式】的设计，对于【-128 到 127之间】的数据做了一层缓存。

也就是说，Integer 在【-128 到 127之间】，就直接在缓存里面获取【Integer 对象的实例】并且返回。

否则，会创建一个【new的integer对象】，从而减少【频繁创建】带来的内存消耗，从而提升性能。

如果，两个Integer 对象，都在【-128 到 127之间】，并且，我们使用【==】判断，返回的结果必然是TRUE。因为，这两个Integer 对象，指向的【内存地址】是同一个。

注意：在【测试环境】数据是有限的，正好在【Integer 的缓存区间】，导致测试通过。但在【生存环境】，数据量超出了【缓存区间】，所以会导致【生产事故】

## hashCode() + equals()

| hashCode()  | equals()  |
|---|---|
| 用于确定对象在`hash表`中的`索引位置` | 用于比较出现`哈希冲突`的两个值  |
| 用于`快速比较数值`，容易出现`哈希冲突` | 用于比较出现`哈希冲突`的两个值  |

## 重写hashCode或equals方法需要注意什么？ 为什么重写equals要重写hashcode？hashcode()方法原理

<https://www.bilibili.com/video/BV1o34y127Pm>

## int和integer有什么区别

<https://www.bilibili.com/video/BV1ZP4y187Tk>

## 数组 比较

```java
import java.util.Arrays;
public class Main {
    public static void main(String[] args) {
        int[] array = {2,5,-2};
        int[] array1 = {2,5,-2};
        int[] array2 = {2,5,-3};
        System.out.println(Arrays.equals(array,array1));
        System.out.println(Arrays.equals(array,array2));
    }
}
```

## `==` 和 `equals`

```java
`==`比较的是【值】：
int a = 8;
int b = 8;
System.out.println(a == b); // ✌true

Long a = 8L;
System.out.println(a.equals(8)); // ❌false
System.out.println(a == 8);      // ✌true


-------------------------------------
`==`比较的是【对象的内存地址】：
`equals`比较的是【对象的内容】：
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
如果【缓存池】没有，在【堆空间】创建新的对象

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

## 其中hashCode方法 の 返回值是什么？

<https://www.bilibili.com/video/BV1WR4y1J7T4>

## 为什么 Java 中 “1000==1000” 为 false，而 ”100==100“ 为 true

在包装类  Integer 中存数字时
  如果数字在一个字符的范围之内(－128~127) == 比较的是数字大小
  如果超过这个范围，== 比较的就是这两个对象的地址，包装类创建两个不同的对象地址自然不同。所以就导致
   Integer i1=1000;
   Integer i2=1000;
i1==i2.sout 的结果是false
   包装类的目的就是把基本数据类型包装成对象。

## is-a 和 has-a 继承

- is-a：判断【父子类】的关系
- has-a：判断【类】与【成员】的关系

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

## 多态

多态的必要条件：

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

<https://www.bilibili.com/video/BV1V3411C7MA>

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
        tmp.info("重载的：");
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
【可变参数】必须放在【固定参数】的后面：
public static void printNames(String name1, String... names) {
    //...
}
```

【可变参数】 与 【数组】 的区别：

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

作用的范围不同：

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
