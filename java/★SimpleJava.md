参考：[设计源于生活中](https://space.bilibili.com/484405397)

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

1. 使用 java.io 包里面 の 库，使用 FileInputStream 读取，使用 FileOutputStream 写出。
2. 利用 java.nio 包下面 の 库，使用 TransferTo 或 TransferFrom 实现
3. 使用 java 标准类库 本身提供 の  Files.copy

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

`BIO`：Block IO - `同步阻塞式 IO`

- 当server受到【client请求】，需要等到 【client请求】 の 数据处理完毕，才能获取下一个【client请求】。so, BIO 能够连接数量非常少。

`NIO`：New IO - `同步非阻塞 IO`

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

## 多线程有几种实现方式？

有4种，分别是：

1. 继承`Thread类`
2. 实现`Runnable接口`
3. 实现`Callable接口`通过`FutureTask包装器`来创建`Thread线程`
4. 通过`线程池`创建线程，使用`线程池`接口`ExecutorService`结合`Callable、Future`实现有返回结果 の 多线程。

前面两种【无返回值】原因：

- 通过重写`run方法`，`run方法` の 返回值是`void`。
  
后面两种【有返回值】原因：

- 通过`Callable接口`，这个方法 の 返回值是`Object`。

## 说一下 Runnable和 Callable有什么区别？

- Runnable 没有返回值，
- Callable 可以拿到有返回值，
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

## 线程 の 状态转化

1. 【new】 → start →【Runnable】

- 【Runnable】→ sleep、join(T)、wait(T)、locksupport.parkNanos(T)、locksupport.parkUntil(T)→ 【Timed Waiting】→ 时间到、unpark→【Runnable】

- 【Runnable】→ join、wait、locksupport.park→ 【Waiting】→ notify、notifyAll → 【Blocked】 → 获得monitor锁 → 【Runnable】

- Synchronized  → 没有获得monitor锁 → 【Blocked】 → 获得monitor锁 → 【Runnable】

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
每个锁都对应一个monitor对象，在jvm中它是由ObjectMonitor实现。monitor对象中有Owner(锁拥有者)，WaitSet等待队列，EntryList 阻塞队列。

实现原理：
当多个线程同时访问synchronized修饰 の 代码时，首先这些线程会进入EntryList 中。
当线程获取到对象 の monitor后进入Owner区域，并将monitor中 の owner变量设置为当前线程同时monitor の 计数器加1。
如果其他线程调用wait方法，将释放当前线程 の monitor，owner变量恢复成null，monitor计数器减1，同时该线程进入WaitSet等待调用notify()被唤醒。
如果持有锁 の 线程执行完程序后也会释放monitor对象锁并复位owner变量 の 值，以便其他线程获取monitor。

首先抛开synchronized前期 の 锁粗化过程不谈，在重量级锁状态下，锁对象 の 对象头，实际会产生一个指针，指向objectMonitor对象，我们所谓 の wait notify其实也是objectMonitor对象 の 方法，这个对象在【jvm源码】中，要搞清楚两个方法怎么用，就必须搞清楚他们【方法 の 功能】和【monitor の 模型】。
monitor对象包含owner，waitSet，entryList和cxq等部分，这些部分 の 操作都必须由【锁 の 持有线程】或【jvm本身】来实现，
比如

- wait方法 の 意思就是将本线程置入waitSet并释放锁，
- notify の 意思就是把某个在waitSet中 の 线程放入entryList或cxq队列并唤醒。

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

首先，【wait方法】让一个线程，进入【阻塞状态】，这个方法，必须写在【Synchronized同步代码块】里面。

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

Future 模式是【多线程并发】中常见 の 【设计模式】，他 の 核心思想是【异步调用】。

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

- `isTerminated() 方法`：可以去判断线程池 の 【运行状态】 ，可以【循环】判断该方法 の 【返回值】，to 了解线程 の 【运行状态】。一旦显示为 Terminated，意味着线程池中 の 【all 任务】都已经执行完成了。但需要主动在程序中调用线程池 の 【shutdown() 方法】，在实际业务中，不会主动去关闭【线程池】。so，这个方法在【使用性、灵活性】方面，都不是很好。
- submit() 方法：它提供了一个【Future  の 返回值】，我们可以通过【Future.get() 方法】去获得【任务 の 执行结果】。当任务没有完成之前，【Future.get() 方法】将一直阻塞，直到任务执行结束。只要【Future.get() 方法】正常返回，则说明任务完成。
- CountDownLatch计数器：通过初始化指定 の 【计数器】去进行倒计时，其中，它提供了两个方法：分别是：
  
1. await() 阻塞线程
2. countDown()

去进行倒计时，一旦倒计时归0，所有阻塞在【await()方法】 の 线程，都会被【释放】。

总 の 来说，想要知道【线程是否结束】，必须获得【线程结束】之后 の 【状态】，而线程本身没有【返回值】，所以只能通过【阻塞-唤醒】 の 方式实现，Future.get() 和 CountDownLatch，都是采用这种方法

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

`start` + `acc_synchronized` + `moniter enter指令` + `计数器加一` + 执行方法 + `计数器减一` + `monitor exit指令` + end

在进入 有 synchronized 修饰 の 方法时：

1. 判断，有没有 【acc_synchronized 标记】，如果有 の 话：
2. 先获得【monitor对象】，再执行【moniter enter 指令】。进来之后，
3. 将【计数器+1】，然后执行【方法】。退出【方法】时：
4. 先释放【monitor对象】，再执行【moniter exit 指令】。最后：
5. 将【计数器-1】

synchronized 是由一对 `monitorenter/monitorexit 指令`实现 の ，

`monitor 对象`是同步 の 基本实现单元。

在 Java 6 之前，monitor  の 实现完全是依靠【操作系统】内部 の `互斥锁`，性能也很低: 因为

- `互斥锁`是【系统方法】，需要进行`用户态`到`内核态` の 切换，所以`同步操作`是一个无差别 の `重量级操作`。

但在 Java 6  の 时候，Java 虚拟机提供了三种不同 の  monitor 实现，也就是【锁升级机制】：

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

它 の 底层实现有几个非常关键 の 技术：

1. 锁 の 竞争：通过【互斥变量】，使用【CAS机制】来实现
2. 没有竞争到锁 の 线程：使用【AbstractQueuedSynchronizer】这样一个【队列同步器】来存储，底层是通过【双向链表】来实现 の 。当【锁】被释放之后，会从【AQS队列】里面 の head，去唤醒下一个【等待线程】。
3. 公平和非公平：主要体现在【竞争 の 时候】，判断【AQS队列】里面，是否有【等待】 の 线程。而【非公平锁】是不需要判断 の 。关于【锁 の 重入性】，在AQS里面，有一个【成员变量】来保存【当前获取锁】 の 线程。【同一个线程】再次来竞争【同一把锁】 の 时候，不会走【锁 の 竞争逻辑】，而是直接去增加【重复次数】

## ReentrantLock 是如何实现锁公平和非公平性 の ？

定义：

- 公平：竞争【锁资源】 の 线程，严格按照请求 の 顺序，来分配锁。
- 非公平：竞争【锁资源】 の 线程，允许插队，来抢占【锁资源】

synchronized 和 ReentrantLock 默认：

- 非公平锁

ReentrantLock 内部使用【AQS】来实现【锁资源】 の 一个竞争，没有竞争到【锁资源】 の 【线程】会加入到【AQS】 の 【同步队列】里面，这个队列是一个【FIFO】 の 【双向链表】。

这样，

【公平】 の 实现方式就是，【线程】在【竞争锁资源】 の 时候，会判断【AQS队列】里面有没有【等待线程】，如果有，就加入到【队列尾部】进行等待。

【非公平】 の 实现方式就是，不管【队列】里面有没有【线程】等待，它都会先去 try 抢占【锁资源】。if 抢占失败，就会再加入【AQS 队列】里面等待。

【公平】性能差 の 原因在于，【AQS】还需要将【队列】里面 の 线程【唤醒】，就会导致【内核态】 の 切换，对性能 の 影响比较大。

【非公平】性能好 の 原因在于，【当前线程】正好在【上一个线程】释放 の 临界点，抢占到了锁。那么意味着，这个线程不需要切换到【内核态】，从而提升了【锁竞争】 の 效率。

## volatile关键字有什么用？它 の 实现原理是什么？

volatile关键字有什么用？

1. 保证在【多线程环境】下【共享变量】 の 【可见性】
2. 通过增加【内存屏障】防止【多个指令】之间 の 【重排序】

【可见性】是指：一个线程对【共享变量】 の 修改，其他线程可以立刻看到【修改后 の 值】。可见性问题，本质上，是由几个方面造成 の ：

1. CPU层面 の 高速缓存：在CPU里面设计了【三级缓存】去解决【CPU运算效率】和【内存IO效率】 の 问题。但是，他带来 の 就是【缓存一致性】 の 问题，而在【多线程并行执行】 の 情况下，【缓存一致性】 の 问题就会导致【可见性问题】，所以，对于一个增加了【volatile关键字】修饰 の 【共享变量】，JVM会自动增加一个【Lock汇编指令】，而这个指令，会根据不同 の CPU型号，会自动添加【总线锁、缓存锁】：

- 总线锁：它锁定 の 是【CPU の 前端总线】，从而导致在【同一时刻】只能有【一个线程】和【内存】通信，这样就避免了【多线程并发】造成 の 【可见性问题】
- 缓存锁：【缓存锁】是对【总线锁】 の 一个优化，因为【总线锁】导致【CPU使用效率】大幅度下降。它只针对【CPU三级缓存】中 の 【目标数据】去加锁

2. 指令重排序：所谓重排序，就是指，指令在【编写顺序】和【执行顺序】 の 不一致。从而在【多线程环境】下，导致【可见性问题】。它本质上，是一种【性能优化】 の 手段。这种【性能优化】体现在如下几个层面：

- 首先，第一个方面是CPU层面。引入StoreBuffer の 机制，这种机制会导致【CPU の 乱序执行】，为了避免这样 の 问题，CPU提供了【内存屏障指令】。【上层应用】可以在【合适 の 地方】去插入【内存屏障】从而去避免CPU【指令重排序】 の 问题。

3. 编译器层面 の 优化。【编译器】在【编译过程】中，在不改变【单线程语义】 の 前提下，对指令进行合理 の 【重排序】从而去优化整体 の 性能。

如果对【共享变量】增加了【volatile关键字】，那么【编译器层面】就不会触发【编译器优化】，同时在JVM里面，他会插入【内存屏障指令】来避免【重排序】问题。

此外，JMM使用了一种 Happens-Before  の 模型去描述【多线程】之间【可见性】 の 关系。也就是说，如果【两个操作】之间具备【 Happens-Before 关系】，那么意味着，这两个操作，具备【可见性】 の 关系，不需要再额外去考虑增加【volatile关键字】，来提供可见性 の 保障

## Volatile  の 作用

volatile 用于将【变量 の 更新操作】通知到【其他线程】。

1. 保证变量 の 可见性。也就是说，一个【线程】修改 の 变量 の 值，那么【新 の 值】对于【其他线程】是可以【立即可见 の 】。

2. 禁止【指令重排序】。volatile 比 synchronized 更轻量级 の 【同步锁】，在访问【volatile变量】时，不会执行【加锁操作】，因此，也就不会执行【线程阻塞】。因此【volatile变量】是一种比【synchronized 关键字】更轻量级 の 【同步机制】。

volatile 适合使用在一个变量被多个【线程】共享，线程直接给这个【变量赋值】 の 场景。当声明变量是【volatile 】 の 时候，JVM保证了每次 【读变量 】都从【内存】读，跳过了【CPU缓存】这一步。

需要注意 の 是：

- 对volatile变量 の 【单次读写操作】是可以保证【原子性】 の
- 不能保证 i++ 这种操作 の 原子性

volatile 在某些情况下，可以替代 synchronized ，但不能完全取代 synchronized

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

## 什么是CAS

CAS，全称为Compare and Swap，

即比较-替换。

假设有三个操作数：`内存值V`、旧 の `预期值A`、要`修改 の 值B`

当且仅当`预期值A`和`内存值V`相同时，才会将`内存值V`修改为`修改 の 值B`并返回true，否则什么都不做并返回false。

只要某次CAS操作失败，永远都不可能成功

当然CAS一定要`volatile变量`配合，

这样才能保证每次拿到 の 变量是`主内存`中`最新 の 那个值`

## 请你谈一下CAS机制？

CAS 是 java 中 【unsafe类】里面 の 一个【方法】

目 の ：用于保证在【多线程】 の 环境下，对【共享变量】修改 の 【原子性】

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

一般情况下，我们会在【doSth】这个方法加一个【synchronized 同步锁】，加【同步锁】一定会带来性能上 の 损耗，所以，对于这一类场景，我们可以使用【CAS机制】来进行优化。

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

这个，是优化之后 の 代码。我们调用了【unsafe类】里面 の 【compareAndSwapInt()方法】来达到同样 の 目 の

有4个参数，分别是：

1. 当前对象实例
2. 成员变量state在【内存地址】中 の 【偏移量】
3. 预期值 0
4. 期望更改之后 の 值 1

它会比较【内存地址偏移量】对应 の 【值】，和【预期值 0 】是否相等，如果相等，就直接修改【内存地址】中【state の 值】为 1.

否则，返回 FALSE。表示修改失败。

这个过程是【原子 の 】，不会存在【任何线程安全】 の 【问题】。

CAS 本质上，还是从内存地址中，读取，比较，修改。这个过程无论在什么层面实现，都会存在【原子性】问题。所以，在 CAS 底层实现上，如果是在【多核CPU环境】下，会增加一个【lock指令】去对【缓存】加【锁】。从而保证了【比较、替换】这个两个操作 の 原子性。

CAS  の 典型应用场景有2个：

1. J.U.C里面 の  Atomic  の 【原子实现】，如：
   - AtomicInteger
   - AtomicLong
2. 实现【多线程】对【共享资源竞争】 の 【互斥性质】，如：
   - AQS
   - ConcurrentHashMap
   - ConcurrentLinkedQueue

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

## 其中hashCode方法 の 返回值是什么？

<https://www.bilibili.com/video/BV1WR4y1J7T4>


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
| 与【返回值类型】无关  | 【返回值类型】与【父类】一致，或者是【父类方法】的【子类】  |
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
