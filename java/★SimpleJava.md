参考：[设计源于生活中](https://space.bilibili.com/484405397)

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

`初始(NEW)`：新创建了一个【线程对象】，但还没有调用`start()方法`。

`运行(RUNNABLE)`：这个状态下，线程可能【就绪（ready）】或者【运行中（running）】

- 【线程对象】创建后，该状态的线程位于【可运行线程池】中，等待被【线程调度】选中，从而获取【CPU的使用权】，此时处于【就绪状态（ready）】。

- 【就绪状态】的线程在获得【CPU时间片】后变为运行中【状态（running）】。

`阻塞(BLOCKED)`：【线程】阻塞于【锁】，处于锁等待状态。

`等待(WAITING)`：需要等待【其他线程】触发条件后【唤醒】，如 wait & notify。

`超时等待(TIMED_WAITING)`：该状态不同于`WAITING`的是，它可以在指定的时间后【自行返回】。

`终止(TERMINATED)`：表示该线程已经【执行完毕】。

## 线程的状态转化

1. 【new】 → start →【Runnable】

- 【Runnable】→ sleep、join(T)、wait(T)、locksupport.parkNanos(T)、locksupport.parkUntil(T)→ 【Timed Waiting】→ 时间到、unpark→【Runnable】

- 【Runnable】→ join、wait、locksupport.park→ 【Waiting】→ notify、notifyAll → 【Blocked】 → 获得monitor锁 → 【Runnable】

- Synchronized  → 没有获得monitor锁 → 【Blocked】 → 获得monitor锁 → 【Runnable】

2. 【Runnable】 → 【terminated】


## wait和notify 为什么要在synchronized代码块中

<https://www.bilibili.com/video/BV1xr4y1p7w6>

wait 和 notify 用于【多个线程】之间的【协调】，
wait 表示让【线程】进入【阻塞状态】，
notify 表示让【阻塞的线程】被【唤醒】
两种通常同时出现。

在jvm中对象被分成：对象头，实例数据(存放类的属性数据信息)，对齐填充(要求对象起始地址必须是8字节的整数倍，对齐填充仅仅是为了使字节对齐)。 实例数据和对齐填充与synchronized无关。

对象头主要结构时Mark Word 和 类元指针 组成，Mark Word存储了对象的hashCode，锁信息，gc分代年龄，gc标志等。
Class Metadata Address，JVM通过这个类元指针确定对象是哪个类的实例。
每个锁都对应一个monitor对象，在jvm中它是由ObjectMonitor实现。monitor对象中有Owner(锁拥有者)，WaitSet等待队列，EntryList 阻塞队列。

实现原理：
当多个线程同时访问synchronized修饰的代码时，首先这些线程会进入EntryList 中。
当线程获取到对象的monitor后进入Owner区域，并将monitor中的owner变量设置为当前线程同时monitor的计数器加1。
如果其他线程调用wait方法，将释放当前线程的monitor，owner变量恢复成null，monitor计数器减1，同时该线程进入WaitSet等待调用notify()被唤醒。 
如果持有锁的线程执行完程序后也会释放monitor对象锁并复位owner变量的值，以便其他线程获取monitor。

首先抛开synchronized前期的锁粗化过程不谈，在重量级锁状态下，锁对象的对象头，实际会产生一个指针，指向objectMonitor对象，我们所谓的wait notify其实也是objectMonitor对象的方法，这个对象在【jvm源码】中，要搞清楚两个方法怎么用，就必须搞清楚他们【方法的功能】和【monitor的模型】。
monitor对象包含owner，waitSet，entryList和cxq等部分，这些部分的操作都必须由【锁的持有线程】或【jvm本身】来实现，
比如

- wait方法的意思就是将本线程置入waitSet并释放锁，
- notify的意思就是把某个在waitSet中的线程放入entryList或cxq队列并唤醒。

不管是哪个方法，都要求执行的线程为【锁的持有线程】。基于此种功能，在开发环境下，如果不把两个方法写在同步代码中，会在编译期就提示错误。

最关键的点，是为了避免资源竞争和发生“幻读”一样的问题。等待线程的写法都是
while (!condition) {wait()}。假设此时有多个线程都在等待。再假设使用notify/wait不需要锁。那么当一个线程唤醒了当前等待的所有线程，所有的等待检查了condition都认为满足了可以继续执行，那么所有线程都会执行下一步。但是如果其中一个线程执行完之后把condition又变成了false，那么其他被唤醒的线程不会再次检查，因为已经检查过了。就出现类似幻读一样的情况。

举个例子就是消息队列。如果不需要锁，等待消息的所有消费者都会被唤醒然后都会去检查是否有消息。刚好所有消费者都检查完，认为可以执行。一个消费者把队列里唯一的消息拿走了，顺利执行。但是别的消费者因为之前检查过了，所以并不会再次检查，就会出现问题。

还有一点是基本不会用notify()去唤醒, 推荐使用notifyAll(),因为notify是唤醒某个指定线程，你不知道这个线程到底是哪个，在多线程情况下，使用推荐使用notifyAll()方法.



## BLOCKED 和 WAITING 有什么区别？

BLOCKED 和 WAITING 都属于【线程等待】状态。

BLOCKED 是指，线程在等待【监视器锁】的时候的【阻塞状态】。也就是说，在【多个线程】去竞争【synchronized同步锁】，没有竞争到【锁】的线程会被【阻塞等待】，而这个时候，这个线程状态，叫做 blocked。在【线程】的整个生命周期里面，只有【synchronized同步锁】的等待，才会存在这个状态

WAITING 是指，线程需要等待【某一线程】的【特定操作】，才会被唤醒。我们可以使用【Object.join() Object,wait() LockSupport.park()】这样一些方法，使得【线程】进入到一个【Waiting 状态】，那么，在这个状态下，我们必须要等待【特定方法】来唤醒。比如，【Object.notify() LockSupport.unpark()】去唤醒【阻塞】的线程.

| BLOCKED  | WAITING   |
|---|---|
| 【锁竞争失败】后【被动触发】的状态   |  【人为】的【主动触发】的状态  |
| 唤醒是【自动触发】的，【获得锁的线程】在【释放锁】之后，会触发唤醒  | 通过【特定方法】【主动唤醒】 |

等待ReentrantLock的线程是waiting，

等待synchronized锁的线程是blocked

`阻塞(BLOCKED)`：表示【线程】阻塞于锁。
`等待(WAITING)`：【进入该状态的线程】需要等待【其他线程】做出一些特定动作（通知或中断）。

## sleep() 和 wait() 有什么区别？

| Object.wait()  | Thread.sleep()  |
|---|---|
| 会释放【锁资源】以及【CPU资源】  | 不会释放【锁资源】，但会释放【CPU资源】  |
| 来自 Object | 来自 Thread  |
| wait() 可以使用 notify()/notifyAll()直接唤醒  | sleep() 时间到会自动恢复  |
| Object.wait()  | Thread.sleep()  |


## wait和sleep是否会触发锁的释放以及CPU资源的释放？

首先，【wait方法】让一个线程，进入【阻塞状态】，这个方法，必须写在【Synchronized同步代码块】里面。

因为【wait & notify】是基于【共享内存】来实现【线程】与【线程】之间【通信】。所以，在调用【wait & notify】之前，它必须要【竞争锁资源】，从而去实现条件的【互斥】。所以，wait 方法 必须要要释放锁，否则就会【死锁】。

Thread.sleep()方法，只是让一个线程，单纯地进入到一个睡眠状态。这个方法，没有强制要求加【synchronized同步锁】。而且，从它的功能和语义上来说，也没有这个必要。即使在【synchronized同步代码块】里面去调用【Thread.sleep方法】也并不会触发【锁的释放】。

此外，凡是那些让线程进入【阻塞状态】的方法，操作系统层面都会去【重新调度】，从而实现【CPU时间片】的切换，从而提升【CPU】的利用率。

## notify()和 notifyAll()有什么区别？

| `notifyAll()` | `notify()`  |
|---|---|
| 唤醒所有的线程  | 唤醒一个线程  |

`notifyAll()` 调用后，会将【all线程】由`等待池`移到`锁池`，然后参与`锁`的竞争，

- if 竞争成功 then 继续执行，

- if 不成功 then 留在`锁池` then 等待`锁`被`释放`后, 再次参与竞争。

`notify()`只会唤醒一个线程，具体唤醒哪一个线程由虚拟机控制。

## 如果一个线程两次调用start()，会出现什么？

如果一个线程两次调用start()，会出现什么？

当我们第一次调用【start()方法】的时候，线程状态可能会处于【终止状态】or【非new状态】下的一个【其他状态】。

再调用一次【start()方法】的时候，相当于，让这个正在运行的【线程】重新运行一遍。

不论从【线程安全性】角度，还是从【线程本身执行逻辑】来看，它都是不合理的。

因此，为避免这样一个问题，在线程运行的时候，会先去判断当前【线程】的一个【运行状态】。

以上，就是我对这个问题的理解。

## Files线程的 run() 和 start() 有什么区别？

| start() 方法  | run() 方法  |
|---|---|
|  启动线程 | 执行线程的运行时代码  |
|  只能调用一次 | 可以重复调用  |

## 线程之间如何进行通讯

1. 线程之间可以通过【共享内存】or【网络】来进行【通信】
2. if 通过【共享内存】:
   - 需要考虑并发问题。什么时候阻塞？什么时候唤醒？如 wait() 和 notify()
3. if 通过【网络】：
   - 同样需要考虑【并发问题】

## 守护线程是什么？

是专门为【用户线程】提供服务的【线程】。

【生命周期】依赖于【用户线程】，只有【用户线程】正在运行的情况下，【守护线程】才会有【存在的意义】。

【守护线程】不会阻止【JVM的退出】，但【用户线程】会阻止【JVM的退出】。

【守护线程】创建方式，和【用户线程】是一样的，只需要调用【用户线程】的 setDaemon 方法，设置成 True 就好了。表示，这个线程是【守护线程】。

基于【守护线程】的特性，它更适合【后台】的【通用型服务】的一些场景。比如，JVM里面的【垃圾回收】。

不能用在【线程池】或者【IO场景】，因为JVM 一旦退出，【守护线程】也会直接退出，那么就会导致——【任务没有执行完 、资源没有正确释放】等问题

守护线程是运行在`后台`的一种`特殊进程`。

`周期性`地执行`某种任务`。

在 Java 中`垃圾回收线程`就是特殊的守护线程。

## Java多线程的Future模式

<https://www.bilibili.com/video/BV1ov41167vH>

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

- `isTerminated() 方法`：可以去判断线程池的【运行状态】 ，可以【循环】判断该方法的【返回值】，to 了解线程的【运行状态】。一旦显示为 Terminated，意味着线程池中的【all 任务】都已经执行完成了。但需要主动在程序中调用线程池的【shutdown() 方法】，在实际业务中，不会主动去关闭【线程池】。so，这个方法在【使用性、灵活性】方面，都不是很好。
- submit() 方法：它提供了一个【Future 的返回值】，我们可以通过【Future.get() 方法】去获得【任务的执行结果】。当任务没有完成之前，【Future.get() 方法】将一直阻塞，直到任务执行结束。只要【Future.get() 方法】正常返回，则说明任务完成。
- CountDownLatch计数器：通过初始化指定的【计数器】去进行倒计时，其中，它提供了两个方法：分别是：
  
1. await() 阻塞线程
2. countDown()

去进行倒计时，一旦倒计时归0，所有阻塞在【await()方法】的线程，都会被【释放】。

总的来说，想要知道【线程是否结束】，必须获得【线程结束】之后的【状态】，而线程本身没有【返回值】，所以只能通过【阻塞-唤醒】的方式实现，Future.get() 和 CountDownLatch，都是采用这种方法

## synchronized 和 Lock 有什么区别？

| synchronized  |  lock |
|---|---|
| 有个【锁升级】的过程  |  lock |
| 原理：锁住【对象头】  |  原理： |
| 非公平锁  | 【公平锁、非公平锁】，可选择 |
| 是【方法关键字】  |  适用于【接口】 |
| 底层是【JVM层面】的锁  |  底层是【API层面】的锁 |
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
    // Lock 连续运行多次的情况
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

synchronized 就是锁住【对象头】中【两个锁】【标志位】的【数值】

## 多线程中 synchronized 锁升级的原理是什么？

synchronized 锁升级原理：

在`锁对象`的`对象头`里面有一个 `threadid 字段`，

在第一次访问的时候 `threadid 为空`，jvm 让其持有`偏向锁`，

并将 threadid 设置为其`线程 id`，

再次进入的时候会先判断 `threadid` 是否与其`线程 id` 一致，

如果一致,则可以直接使用此对象，

如果不一致，则升级`偏向锁`为`轻量级锁`，通过`自旋`循环一定次数来获取锁，

执行一定次数之后，如果还没有正常获取到要使用的对象，

此时就会把锁从轻量级升级为`重量级锁`，此过程就构成了 synchronized 锁的升级。

锁的升级的目的：

为了减低了锁带来的`性能消耗`。

在 Java 6 之后优化 synchronized 的实现方式，

使用了`偏向锁`升级为`轻量级锁`再升级到`重量级锁`的方式，

从而减低了锁带来的性能消耗。


## synchronized 的执行流程：

`start` + `acc_synchronized` + `moniter enter指令` + `计数器加一` + 执行方法 + `计数器减一` + `monitor exit指令` + end

在进入 有 synchronized 修饰的方法时：

1. 判断，有没有 【acc_synchronized 标记】，如果有的话：
2. 先获得【monitor对象】，再执行【moniter enter 指令】。进来之后，
3. 将【计数器+1】，然后执行【方法】。退出【方法】时：
4. 先释放【monitor对象】，再执行【moniter exit 指令】。最后：
5. 将【计数器-1】

synchronized 是由一对 `monitorenter/monitorexit 指令`实现的，

`monitor 对象`是同步的基本实现单元。

在 Java 6 之前，monitor 的实现完全是依靠【操作系统】内部的`互斥锁`，性能也很低: 因为

- `互斥锁`是【系统方法】，需要进行`用户态`到`内核态`的切换，所以`同步操作`是一个无差别的`重量级操作`。

但在 Java 6 的时候，Java 虚拟机提供了三种不同的 monitor 实现，也就是【锁升级机制】：

[Synchronized锁升级的原理](https://www.bilibili.com/video/BV1wt4y147dQ)

- 偏向锁: 最轻量。线程会通过【CAS】获取一个预期的标记，在【一个线程】进行【同步代码】，减少【获取锁】的代价
  - 如果存在【多线程竞争】，就会膨胀为【轻量级锁】。 
- 轻量级锁：【多线程】【交替执行】【同步代码】，而不是【阻塞】
  - 自旋锁：线程在获取【锁】的过程中，不会去【阻塞线程】，也就无所谓【唤醒线程】。【阻塞-唤醒】是需要【操作系统】去执行的，比较【耗时】。线程会通过【CAS】获取一个预期的标记，如果没有获取到，就会循环获取，会先自己循环【50~100次】
  - 如果超过【循环次数】，就会膨胀为【重量级锁】
- 重量级锁：在【多线程竞争阻塞】时，线程处于【blocked】，处于【锁等待】状态下的线程，需要等待【获得锁】的线程【释放】锁以后，才能【触发唤醒】，会进行`用户态`到`内核态`的切换

总的来说，Synchronized锁升级，是尽可能减少`用户态`到`内核态`的切换

## Synchronized 的锁消除

在JIT阶段，如果检测出【不可能有】【资源竞争的锁】，会直接消除

## java中的锁机制

- lock
- synchronized
- 分布式锁

## synchronized 的作用

实现【同步】

## synchronized 和 ReentrantLock 区别是什么？

ReentrantLock 是一种【可重入】的【排他锁】，它主要是解决【多线程】对于【共享资源】竞争的问题，它的核心特性有：

1. 它支持【重入】： 也就是，获得【锁】的【线程】在【释放锁】之前，再次去竞争【同一把锁】的时候，不需要【加锁】，就可以【直接访问】
2. 它支持【公平】和【非公平】特性
3. 它提供了【阻塞竞争锁】和【非阻塞竞争锁】的两种方法，分别为 lock() 和 tryLock()

它的底层实现有几个非常关键的技术：

1. 锁的竞争：通过【互斥变量】，使用【CAS机制】来实现
2. 没有竞争到锁的线程：使用【AbstractQueuedSynchronizer】这样一个【队列同步器】来存储，底层是通过【双向链表】来实现的。当【锁】被释放之后，会从【AQS队列】里面的head，去唤醒下一个【等待线程】。
3. 公平和非公平：主要体现在【竞争的时候】，判断【AQS队列】里面，是否有【等待】的线程。而【非公平锁】是不需要判断的。关于【锁的重入性】，在AQS里面，有一个【成员变量】来保存【当前获取锁】的线程。【同一个线程】再次来竞争【同一把锁】的时候，不会走【锁的竞争逻辑】，而是直接去增加【重复次数】

## ReentrantLock 是如何实现锁公平和非公平性的？

定义：

- 公平：竞争【锁资源】的线程，严格按照请求的顺序，来分配锁。
- 非公平：竞争【锁资源】的线程，允许插队，来抢占【锁资源】

synchronized 和 ReentrantLock 默认：

- 非公平锁

ReentrantLock 内部使用【AQS】来实现【锁资源】的一个竞争，没有竞争到【锁资源】的【线程】会加入到【AQS】的【同步队列】里面，这个队列是一个【FIFO】的【双向链表】。

这样，

【公平】的实现方式就是，【线程】在【竞争锁资源】的时候，会判断【AQS队列】里面有没有【等待线程】，如果有，就加入到【队列尾部】进行等待。

【非公平】的实现方式就是，不管【队列】里面有没有【线程】等待，它都会先去 try 抢占【锁资源】。if 抢占失败，就会再加入【AQS 队列】里面等待。

【公平】性能差的原因在于，【AQS】还需要将【队列】里面的线程【唤醒】，就会导致【内核态】的切换，对性能的影响比较大。

【非公平】性能好的原因在于，【当前线程】正好在【上一个线程】释放的临界点，抢占到了锁。那么意味着，这个线程不需要切换到【内核态】，从而提升了【锁竞争】的效率。

## volatile关键字有什么用？它的实现原理是什么？

volatile关键字有什么用？

1. 保证在【多线程环境】下【共享变量】的【可见性】
2. 通过增加【内存屏障】防止【多个指令】之间的【重排序】

【可见性】是指：一个线程对【共享变量】的修改，其他线程可以立刻看到【修改后的值】。可见性问题，本质上，是由几个方面造成的：

1. CPU层面的高速缓存：在CPU里面设计了【三级缓存】去解决【CPU运算效率】和【内存IO效率】的问题。但是，他带来的就是【缓存一致性】的问题，而在【多线程并行执行】的情况下，【缓存一致性】的问题就会导致【可见性问题】，所以，对于一个增加了【volatile关键字】修饰的【共享变量】，JVM会自动增加一个【Lock汇编指令】，而这个指令，会根据不同的CPU型号，会自动添加【总线锁、缓存锁】：

- 总线锁：它锁定的是【CPU的前端总线】，从而导致在【同一时刻】只能有【一个线程】和【内存】通信，这样就避免了【多线程并发】造成的【可见性问题】
- 缓存锁：【缓存锁】是对【总线锁】的一个优化，因为【总线锁】导致【CPU使用效率】大幅度下降。它只针对【CPU三级缓存】中的【目标数据】去加锁

2. 指令重排序：所谓重排序，就是指，指令在【编写顺序】和【执行顺序】的不一致。从而在【多线程环境】下，导致【可见性问题】。它本质上，是一种【性能优化】的手段。这种【性能优化】体现在如下几个层面：

- 首先，第一个方面是CPU层面。引入StoreBuffer的机制，这种机制会导致【CPU的乱序执行】，为了避免这样的问题，CPU提供了【内存屏障指令】。【上层应用】可以在【合适的地方】去插入【内存屏障】从而去避免CPU【指令重排序】的问题。

3. 编译器层面的优化。【编译器】在【编译过程】中，在不改变【单线程语义】的前提下，对指令进行合理的【重排序】从而去优化整体的性能。

如果对【共享变量】增加了【volatile关键字】，那么【编译器层面】就不会触发【编译器优化】，同时在JVM里面，他会插入【内存屏障指令】来避免【重排序】问题。

此外，JMM使用了一种 Happens-Before 的模型去描述【多线程】之间【可见性】的关系。也就是说，如果【两个操作】之间具备【 Happens-Before 关系】，那么意味着，这两个操作，具备【可见性】的关系，不需要再额外去考虑增加【volatile关键字】，来提供可见性的保障

## Volatile 的作用

volatile 用于将【变量的更新操作】通知到【其他线程】。

1. 保证变量的可见性。也就是说，一个【线程】修改的变量的值，那么【新的值】对于【其他线程】是可以【立即可见的】。

2. 禁止【指令重排序】。volatile 比 synchronized 更轻量级的【同步锁】，在访问【volatile变量】时，不会执行【加锁操作】，因此，也就不会执行【线程阻塞】。因此【volatile变量】是一种比【synchronized 关键字】更轻量级的【同步机制】。

volatile 适合使用在一个变量被多个【线程】共享，线程直接给这个【变量赋值】的场景。当声明变量是【volatile 】的时候，JVM保证了每次 【读变量 】都从【内存】读，跳过了【CPU缓存】这一步。

需要注意的是：

- 对volatile变量的【单次读写操作】是可以保证【原子性】的
- 不能保证 i++ 这种操作的原子性

volatile 在某些情况下，可以替代 synchronized ，但不能完全取代 synchronized 

## volatile 必须满足哪些条件，才能保证在【并发环境】的【线程安全】？

1. 首先，对变量的【写操作】不依赖于像【i++】这样的当前值。
2. 其次，【该变量】没有包含在【具有其他变量】的【不变式】中，也就是说，不同的【volatile变量】不能【相互依赖】，只有在【状态】真正独立于程序内的其他内容的时候，才能使用 volatile。

## voliate是怎么保证可见性的

Volatile 保证可见性的原理：

如果【工作线程1】中有变量修改，会直接同步到【主内存】中；【其余工作线程】在【主内存中】有一个【监听】，当监听到【主内存】中对应的数据修改时，就会去通知【其余工作线程】【缓存内容已经失效】，此时，会从【主内存】中重新获取一份数据来更新【本地缓存】。

在【工作内存】去【监听】【主内存】中的数据，用的是【总线嗅探机制】。但如果大量使用 volatile，就会不断地去监听【总线】，引起【总线风暴】

Java 内存模型定义了八种操作，来控制【主内存】和【本地内存】 の 交互：

除了 lock 和 unlock，还有read、load、use、assign、store、write

- read、load、use 作为一个原子
- assign、store、write作为后一种原子操作

从而避免了在操作过程中，被【打断】，从而保证【工作内存】和【主内存】中的数据都是【相等的】。

应用场景：变量赋值 flag = true，而不适用于 a++

## 【指令重排】背后的思想是：

如果能确保【执行的结果】相同，那么就可通过【更改顺序】来提高性能。

## 【指令重排】有三种形式：

1. 【编译器】重排序
2. 【指令集并行】重排序：在多线程环境下，可能会【结果不同】，有了 volatile 就会有【内存屏障】，从而【阻止重排】。
   - 在读和读之间，会有【读读屏障】
   - 在读和写之间，会有【读写屏障】
   - 在写和读之间，会有【写读屏障】
   - 在写和写之间，会有【写写屏障】
3. 【内存系统】重排序

## 【DCL单例模式】设计为什么需要volatile修饰【实例变量】？

当我们使用

instance = new DCLExample() 构建一个【实例对象】的时候，new这个操作并不是【原子】的，这段代码最终会被编译成 3 条指令：

1. 第一条指令是，为了【对象】分配【内存空间】
2. 第二条指令是， 初始化对象
3. 第三条指令是，  把【instance 对象】赋值给【instance 引用】

由于这三个指令并不是【原子的】，按照重排序的规则——在不影响【单线程执行结果】的情况下，两个不存在【依赖关系】的【指令】是允许【重排序】的。也就是说，不一定会按照我们代码的【编写顺序】来执行。这样一来，就会导致其他线程，可能会拿到一个不完整的对象。

解决这个问题的办法就是：

- 在这个在【instance变量】上，增加一个 volatile 关键字进行修饰。而volatile底层，使用了一个【内存屏障机制】去避免【指令重排序】

## volatile 和 synchronized区别

| volatile关键字  |  synchronized关键字 |
|---|---|
|  轻量，无锁 |  有锁 |
|  性能好，不会发生阻塞 |  开发中使用更多，可能会发生阻塞 |
|  保证: 有序性，可见性，不能保证 原子性 ✖ |  保证: 三大性，原子性，有序性，可见性 |
|  目的: 变量 在`多个线程`之间的 `可见性` | 目的: `多个线程`之间`访问资源`的 `同步性` |
|  作用于: 变量 | 作用于: 类 + 方法 + 代码块 |

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



## 什么是死锁？

当线程 A 持有独占锁a，并尝试去获取独占锁 b 的同时，线程 B 持有独占锁 b，并尝试获取独占锁 a 的情况下，就会发生 AB 两个线程由于互相持有对方需要的锁，而发生的阻塞现象，我们称为死锁。

## 怎么防止死锁？

<https://www.bilibili.com/video/BV1U44y1V7HF>

尽量使用 tryLock(long timeout, TimeUnit unit)的方法(ReentrantLock、ReentrantReadWriteLock)，设置超时时间，超时可以退出防止死锁。
尽量使用 Java. util. concurrent 并发类代替自己手写锁。
尽量降低锁的使用粒度，尽量不要几个功能用同一把锁。
尽量减少同步的代码块。



## 说一下 atomic 的原理？

atomic 主要利用 CAS (Compare And Swap) 和 volatile 和 native 方法来保证原子操作，从而避免 synchronized 的高开销，执行效率大为提升。


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
