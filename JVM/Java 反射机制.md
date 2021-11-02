<!-- vscode-markdown-toc -->
* 1. [Java反射是什么](#Java)
	* 1.1. [除了使用new创建对象之外，还可以用什么方法创建对象？](#new)
	* 1.2. [Java反射创建对象效率高还是通过new创建对象的效率高？](#Javanew)
	* 1.3. [作用](#)
	* 1.4. [哪里会用到反射机制？](#-1)
	* 1.5. [通过直接new的方式或反射的方式都可以调用公共的结构，开发中到底用哪个？](#new-1)
	* 1.6. [什么时候会用到反射呢？](#-1)
	* 1.7. [反射机制与面向对象中的封装性是不是矛盾的？如何看待两个技术？](#-1)
* 2. [class反射的实现方式：](#class)
	* 2.1. [第一种：通过Object类的`getClass方法` .getClass()](#ObjectgetClass.getClass)
	* 2.2. [第二种：通过`对象实例方法`获取对象 .class](#.class)
	* 2.3. [第三种：通过`Class.forName方式` .forName(“xx.xx.Foo”)](#Class.forName.forNamexx.xx.Foo)
	* 2.4. [第四种：可以直接用`字节码文件`获取实例： .newInstance()](#.newInstance)
	* 2.5. [调用invoke方法：](#invoke)
* 3. [Class对象的功能](#Class)
	* 3.1. [获取成员变量](#-1)
	* 3.2. [获取构造方法](#-1)
	* 3.3. [获取成员方法](#-1)
	* 3.4. [实例二](#-1)
* 4. [Field对象的功能](#Field)
* 5. [Method对象的功能](#Method)
* 6. [实战应用](#-1)
	* 6.1. [有一组已经解析完整的数据](#-1)
	* 6.2. [自定义一个字段索引注解](#-1)
	* 6.3. [定义一个工具方法](#-1)
	* 6.4. [测试](#-1)
* 7. [一个实例：APP.java](#APP.java)
	* 7.1. [获取类](#-1)
	* 7.2. [调用公共属性（public）](#public)
	* 7.3. [调用私有属性](#-1)
	* 7.4. [调用公共方法 （public）](#public-1)
	* 7.5. [调用私有方法](#-1)
* 8. [反射机制的优缺点：](#-1)
	* 8.1. [优点：](#-1)
	* 8.2. [缺点：](#-1)
	* 8.3. [解决方案：](#-1)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->

##  1. <a name='Java'></a>Java反射是什么

[参考链接](https://mp.weixin.qq.com/s/9rhXiJ3T9Sy6LvIyNQSfAQ)

[参考链接](https://mp.weixin.qq.com/s/S8nQN5Ywnzc5BEkMVjexAQ)

[参考链接](https://mp.weixin.qq.com/s/ON1lognpoMl_tIngn3d9NA)

###  1.1. <a name='new'></a>除了使用new创建对象之外，还可以用什么方法创建对象？

使用Java反射可以创建对象!

###  1.2. <a name='Javanew'></a>Java反射创建对象效率高还是通过new创建对象的效率高？

通过`new`创建对象的效率比较高。

通过`反射`时，先找查找`类资源`，使用`类加载器`创建，过程比较繁琐，所以效率较低

###  1.3. <a name=''></a>作用

反射机制是在运行时，

对于任意一个类，都能够知道这个类的所有属性和方法；

对于任意个对象，都能够调用它的任意一个方法。

在java 中，只要给定类的名字，就可以通过反射机制来获得类的所有信息。

这种动态获取的信息以及动态调用对象的方法的功能称为Java语言的反射机制。

1）在运行时`判断`任意一个`对象`所属的`类`。

2）在运行时`构造`任意一个`类`的`对象`。

3）在运行时`判断`任意一个`类`所具有的`成员变量和方法`。

4）在运行时`调用`任意一个`对象`的`方法`。

简单来讲，通过反射，类对我们是完全透明的，想要获取任何东西都可以。

###  1.4. <a name='-1'></a>哪里会用到反射机制？

Java中的对象有两种类型：

`编译时类型`和`运行时类型`。

`编译时类型`是指在编写代码时所声明的类型，

`运行时类型`指为对象赋值所采用的类型。

jdbc就是典型的反射

```java
Class.forName(‘com.mysql.jdbc.Driver.class’);//加载MySQL的驱动类
```

这就是反射。如hibernate，struts等框架使用反射实现的。

###  1.5. <a name='new-1'></a>通过直接new的方式或反射的方式都可以调用公共的结构，开发中到底用哪个？

解析：建议直接new的方式

###  1.6. <a name='-1'></a>什么时候会用到反射呢？

反射的特性：动态性

###  1.7. <a name='-1'></a>反射机制与面向对象中的封装性是不是矛盾的？如何看待两个技术？

解析：不矛盾

封装：

是事先将一些属性或者方法`私有化`，并不想被外界访问，

可能私有的方法是内部所使用的方法。

> 如果想访问的话，直接访问公有的public属性或者方法就行。

反射：

是能调用类中的私有属性或者私有方法，

> 是能调用，但是一般不建议调用。

##  2. <a name='class'></a>class反射的实现方式：

在面向对象的世界里，万物皆对象。类是对象，类是java.lang.Class类的实例对象。另外class类只有java虚拟机才能new出来。任何一个类都是Class 类的实例对象。这实例对象有四种表达方式：

在如下的代码中，

person在`编译时类型`为Person,

`运行时类型`为Student,

因此无法在编译是获取在Student类中的定义方法：

```java
Person person = new Student();
```

```java
Foo foo = new Foo();
```

因此在`编译期间`无法预知该对象和类的真实信息，

只能通过`运行时`信息来发现该对象和类的真实信息，

而其真实信息（对象的属性和方法），通常通过`反射机制`来获取，

这便是Java语言中反射机制的核心功能。

###  2.1. <a name='ObjectgetClass.getClass'></a>第一种：通过Object类的`getClass方法` .getClass()

```java
Class cla = foo.getClass();
```

```java
Person p=new Person();
Class clazz=p.getClass();
```

###  2.2. <a name='.class'></a>第二种：通过`对象实例方法`获取对象 .class

```java
Class cla = foo.class;
```

```java
Class clazz=Person.class;
```

###  2.3. <a name='Class.forName.forNamexx.xx.Foo'></a>第三种：通过`Class.forName方式` .forName(“xx.xx.Foo”)

Class.forName(类的全称);

该方法不仅表示了类的类型，还代表了`动态加载类`。

编译时刻加载类是`静态加载`、运行时刻加载类是`动态加载类`。

```java
Class cla = Class.forName(“xx.xx.Foo”);
```

```java
Class clazz=Class.forName(“类的全路径”); (最常用)
```

###  2.4. <a name='.newInstance'></a>第四种：可以直接用`字节码文件`获取实例： .newInstance()

对于有空`构造函数`的类：

Class 对象的 newInstance()方法

要求该 Class 对象对应的类有默认的`空构造器`

```java
Object o = clazz.newInstance();　　// 会调用空参构造器 如果没有则会报错
```

```java
//获取 Person 类的 Class 对象
Class clazz=Class.forName("reflection.Person");
//使用.newInstane 方法创建对象
Person p=(Person) clazz.newInstance();
```

对于没有空的`构造函数`的类则需要先获取到他的`构造对象` 在通过该构造方法类获取实例：

先使用 Class 对象获取指定的 Constructor 对象，

再调用 Constructor 对象的 newInstance() 方法

```java
Constroctor constroctor = clazz.getConstructor(String.class,int.class); // 获取构造函数
Object obj = constroctor.newInstance(“jack”, 18); // 通过构造器对象的newInstance方法进行对象的初始化
```

```java
Class clazz = Person.class();

//获取构造方法并创建对象
Constructor c=clazz.getDeclaredConstructor(String.class,String.class,int.class);

//创建对象并设置属性13/04/2018
Person p1=(Person) c.newInstance("李四","男",20);
```

###  2.5. <a name='invoke'></a>调用invoke方法：

指通过调用Method对象的invoke方法来动态执行函数。invoke方法的具体使用代表如下：

```java
//1 获取xx类的Class对象
Class clazz = Class.forName("xx.Person")

//2 获取Class对象中的setName方法
Method method = clazz.getMethod("setName",String.class)

//3 获取Constructor对象
Constructor constructor = clazz.getConstructor();

//4 根据Constructor定义对象
Object object = constructor.newInstance();

//5 调用method的invoke方法，这里的method表示setName方法
//因此，相当于动态的调用object对象的setName方法并传入alex参数
method.invoke(object,"alex");
```

##  3. <a name='Class'></a>Class对象的功能

> 反射 API 用来生成 JVM 中的类、接口或者对象的信息。

Class 类：

- 反射的核心类，可以获取类的`属性`，`方法`等信息。

- 表示正在运行的Java应用程序中的类和接口

- 注意：所有获取对象的信息都需要Class类来实现。

Constructor 类：

- `Java.lang.reﬂec` 包中的类，表示类的`构造方法`。

- 提供关于类的单个构造方法的信息以及它的`访问权限`

当我们获得了想要操作的类的 Class 对象后，可以通过 Class 类中的方法获取并查看该类中的方法和属性。

###  3.1. <a name='-1'></a>获取成员变量

```java
//获取所有public修饰的成员变量
Field[] getFields() 
//获取指定名称的public修饰的成员变量         
Field getField(String name)  
//获取所有的成员变量，不考虑修饰符
Field[] getDeclaredFields() 
//获取指定的成员变量，不考虑修饰符 
Field getDeclaredField(String name)  
```

###  3.2. <a name='-1'></a>获取构造方法

```java
//获取所有public修饰的构造函数
Constructor<?>[] getConstructors()
//获取指定的public修饰的构造函数
Constructor<T> getConstructor(类<?>... parameterTypes)
//获取所有的构造函数，不考虑修饰符
Constructor<?>[] getDeclaredConstructors() 
//获取指定的构造函数，不考虑修饰符 
Constructor<T> getDeclaredConstructor(类<?>... parameterTypes)  //获取指定的构造函数，不考虑修饰符
```

###  3.3. <a name='-1'></a>获取成员方法

```java
//获取所有public修饰的成员方法
Method[] getMethods()   
//获取指定名称的public修饰的成员方法        
Method getMethod(String name, 类<?>... parameterTypes) 
//获取所有的成员方法，不考虑修饰符
Method[] getDeclaredMethods()  
//获取指定名称的成员方法，不考虑修饰符
Method getDeclaredMethod(String name, 类<?>... parameterTypes) 
```

###  3.4. <a name='-1'></a>实例二

```java
//获取 Person 类的 Class 对象
Class clazz=Class.forName("reflection.Person");


//1.获取 Person 类的所有方法信息
Method[] method=clazz.getDeclaredMethods(); 
for(Method m:method){
	System.out.println(m.toString());
}
//2.获取 Person 类的所有成员属性信息
Field[] field=clazz.getDeclaredFields();
for(Field f:field){
	System.out.println(f.toString());
}
//3.获取 Person 类的所有构造方法信息
Constructor[] constructor=clazz.getDeclaredConstructors(); 
for(Constructor c:constructor){
	System.out.println(c.toString());
}
```

##  4. <a name='Field'></a>Field对象的功能

Field 类：

- `Java.lang.reﬂec` 包中的类，表示类的`成员变量`，可以用来`获取`和`设置`类之中的`属性值`。

- 提供有关类和接口的属性信息，以及对它的`动态访问权限`。

```java
@Test
public void modifyField() throws Exception {
    ExampleClass exampleClass = new ExampleClass();
    Class<? extends ExampleClass> classClass = exampleClass.getClass();
    Field publicField = classClass.getField("publicField");
    Object publicFieldValue = publicField.get(exampleClass);
    System.out.println(publicFieldValue);

    Field privateField = classClass.getDeclaredField("privateField");
    // // 忽略访问权限的检查，暴力反射
    privateField.setAccessible(true);
    // 获取值
    Object privateFieldVale = privateField.get(exampleClass);
    System.out.println(privateFieldVale);
    // 赋新值
    privateField.set(exampleClass,"newPrivateValue");
    Object newValue = privateField.get(exampleClass);
    System.out.println(newValue);
}
```

##  5. <a name='Method'></a>Method对象的功能

Method 类：

- `Java.lang.reﬂec` 包中的类，表示类的`方法`，
  
- 它可以用来`获取`类中的`方法信息`或者`执行方法`。

执行方法 

```java
Object invoke(Object obj,Object... args);
```

获取参数 

```java
getParameters()
```

```java
@Test
public void methodFun() throws Exception {
    ExampleClass exampleClass = new ExampleClass();
    Class<? extends ExampleClass> classClass = exampleClass.getClass();
    Method noParamsMethod = classClass.getMethod("noParamsMethod");
    // 执行方法
    noParamsMethod.invoke(exampleClass);

    Method paramsMethod = classClass.getMethod("paramsMethod", String.class);
    // 获取参数
    Parameter[] parameters = paramsMethod.getParameters();
    System.out.println(Arrays.toString(parameters));
    // 执行方法
    paramsMethod.invoke(exampleClass,"这是参数");
}
```

##  6. <a name='-1'></a>实战应用

###  6.1. <a name='-1'></a>有一组已经解析完整的数据

（通过excel读取或者xml方式等等）

> 我们有几组统计数据，一组是学校的学生信息数据，另一组是学校的教师信息的数据，已经通过解析工具从三方表格中解析出来并封装到了一个数组集合中，数组索引所对应的数据已知，如index=0的代表姓名

我们先将数据准备好

```java
List<List<Object>> teachers = new ArrayList<>();
List<List<Object>> students = new ArrayList<>();

@BeforeEach
public void data(){
    List<Object> teacher1 = new ArrayList<>();
    teacher1.add("张老师");
    teacher1.add(20);
    teacher1.add("13011111111");
    teacher1.add("语文");

    List<Object> teacher2 = new ArrayList<>();
    teacher2.add("李老师");
    teacher2.add(40);
    teacher2.add("13811111111");
    teacher2.add("数学");

    teachers.add(teacher1);
    teachers.add(teacher2);

    List<Object> student1 = new ArrayList<>();
    student1.add("李华");
    student1.add(10);
    student1.add("三年级一班");

    List<Object> student2 = new ArrayList<>();
    student2.add("赵铁柱");
    student2.add(11);
    student2.add("四年级二班");

    students.add(student1);
    students.add(student2);
}
```

```s
[[张老师, 20, 13011111111, 语文], 
[李老师, 40, 13811111111, 数学]]
[[李华, 10, 三年级一班], 
[赵铁柱, 11, 四年级二班]]
```

现在有两个类，分别是Student和Teacher类

```java
public class Student {
    /**
     * 学生姓名
     */
    private String studentName;
    /**
     * 班级名称
     */
    private String className;
    /**
     * 年龄
     */
    private Integer age;
		
		// setter getter ...
}

public class Teacher {
    private String name;
    private String mobile;
    private String subject;
    private Integer age;
		// setter getter ...
}
```

下面我们将通过反射的方式将上面的两组输入放入到对应的对象中，

并准确对应在各自的属性中。

###  6.2. <a name='-1'></a>自定义一个字段索引注解

我们可以通过自定义注解的方式，

将注解内容作用到每一个字段上，

并标记好该字段在数组中的具体索引值，

然后赋值给该字段即可。

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {FIELD})
public @interface FieldIndex {
    /**
     * 对应的索引
     * @return
     */
    int index();
}
改造Student 和Teacher类

java
public class Student {
    /**
     * 学生姓名
     */
    @FieldIndex(index = 0)
    private String studentName;
    /**
     * 班级名称
     */
    @FieldIndex(index = 2)
    private String className;
    /**
     * 年龄
     */
    @FieldIndex(index = 1)
    private Integer age;
}

public class Teacher {
    @FieldIndex(index = 0)
    private String name;
    @FieldIndex(index = 2)
    private String mobile;
    @FieldIndex(index = 3)
    private String subject;
    @FieldIndex(index = 1)
    private Integer age;
}
```

###  6.3. <a name='-1'></a>定义一个工具方法

组装到对应的对象中，进行后期的应用

```java
/**
 *
 * @param t 需要绑定的类
 * @param data 处理的数据
 * @param <T>
 * @return
 */
public <T> void bind(T t,List<Object> data){
    Class<?> clazz = t.getClass();
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
        // 获取字段的注解
        FieldIndex annotation = field.getAnnotation(FieldIndex.class);
        // 有效注解
        if (annotation != null && annotation.index() >= 0){
            // 获取注解中的索引值
            int index = annotation.index();
            // 获取data中的数据
            Object value = data.get(index);
            // 忽略检查
            field.setAccessible(true);
            // 复制给该字段
            try {
                field.set(t,value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
```

###  6.4. <a name='-1'></a>测试

```java
@Test
public void dataBind(){
    List<Teacher> teacherList = new ArrayList<>();
    for (List<Object> teacherArr : teachers) {
        Teacher teacher = new Teacher();
        bind(teacher,teacherArr);
        teacherList.add(teacher);
    }
    List<Student> studentList = new ArrayList<>();
    for (List<Object> studentArr : students) {
        Student student = new Student();
        bind(student,studentArr);
        studentList.add(student);
    }
    System.out.println(teacherList);
    System.out.println(studentList);

}
```

这样我们就完成了一个简单的`数据绑定`了，

类似功能的扩展还有很多，比如进行数据的`校验`。

在我们的实际开发过程中也有很多这样的处理方式，

利用反射机制能大大方便我们进行开发。

##  7. <a name='APP.java'></a>一个实例：APP.java

```java
public class APP {    
   private Integer id;    
   private String username;        
   public String password;    
   //get、set方法省略
   public String toString(){        
      System.out.print("toString");    
   }
   private String toString_2(){   
        System.out.print("toString_2");    
   }
}
```

###  7.1. <a name='-1'></a>获取类

Class.forName(param)方法

param：指定类的全路径，比如：com.test.APP，

代码如下：

```java
public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class aClass = Class.forName("com.test.APP");
        APP app = (APP) aClass.newInstance();

    }
```

这里的newInstance()：实例化Class，生成对象，等同于new。

###  7.2. <a name='public'></a>调用公共属性（public）

getField(param)：获取指定的公共属性，

param：指定的属性名称。

getFields()：获取全部的公共属性，返回值Field[]数组。

代码如下：

```java

public static void main(String[] args) {
        try {
            Class aClass = Class.forName("com.test.APP");
            // APP app = (APP) aClass.newInstance();
            Field password = aClass.getField("password");
            
            //打印结果：public java.lang.String com.test.APP.password
            System.out.println(password);

            Field[] fields = aClass.getFields();
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }
```

###  7.3. <a name='-1'></a>调用私有属性

getDeclaredField(param)：获取指定的私有属性，param：指定的属性名称。

getDeclaredFields()：获取全部的私有属性。

代码如下：

```java
public static void main(String[] args) {
        try {
            Class aClass = Class.forName("com.test.APP");

            Field username = aClass.getDeclaredField("username");
            // 强制获得私有变量的访问权限
            username.setAccessible(true);
            //打印结果：public java.lang.String com.test.APP.username
            System.out.println(username);
            Field[] fields = aClass.getDeclaredFields();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
```

这里还涉及到一个方法，就是setAccessible(true)，其作用是获得访问权限，否则无法获取这个属性。

###  7.4. <a name='public-1'></a>调用公共方法 （public）

getMethod(param)：获取指定的公共方法，

param：指定的方法名称

getMethods()：获取全部公共方法

```java
    public static void main(String[] args) {
        try {
            Class aClass = Class.forName("com.test.APP");
            Method method = aClass.getMethod("toString");
            Method[] methods = aClass.getMethods();
            //执行toString()方法
            Object app = aClass.newInstance();
            method.invoke(app);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
```

这里还有一个知识点，就是如何执行获取到Method方法，在代码中也有体现，可以自行尝试一下。

###  7.5. <a name='-1'></a>调用私有方法

getDeclaredMethod(param)：获取指定的私有方法，

param:指定的私有方法名称。

getDeclaredMethods()：获取全部的私有方法。

```java
    public static void main(String[] args) {
        try {
            Class aClass = Class.forName("com.test.APP");
            Method method = aClass.getDeclaredMethod("toString_2");
            method.setAccessible(true);
            Method[] methods = aClass.getDeclaredMethods();
            //执行toString()方法
            Object app = aClass.newInstance();
            method.invoke(app);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
```

##  8. <a name='-1'></a>反射机制的优缺点：

###  8.1. <a name='-1'></a>优点：

1. 能够运行时动态获取类的实例，提高灵活性；

2. 与动态编译结合

###  8.2. <a name='-1'></a>缺点：

1. 使用反射性能较低，需要解析字节码，将内存中的对象进行解析。

###  8.3. <a name='-1'></a>解决方案：

1. 通过setAccessible(true)关闭JDK的安全检查来提升反射速度；

2. 多次创建一个类的实例时，有缓存会快很多

3. ReﬂﬂectASM工具类，通过字节码生成的方式加快反射速度

4. 相对不安全，破坏了封装性（因为通过反射可以获得私有方法和属性）

