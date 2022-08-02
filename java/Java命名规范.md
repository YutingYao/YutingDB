<!-- vscode-markdown-toc -->
* 1. [包(Package)命名规范-小写英文字母](#Package-)
* 2. [类(Class)命名规范-首字母大写-驼峰命名-no缩写](#Class---no)
* 3. [接口(Interface)命名规范-形容词\动词](#Interface-)
* 4. [抽象类(Abstract Class)命名规范-前缀Abstract/Base](#AbstractClass-AbstractBase)
* 5. [异常类(Exception Class)命名规范-后缀Exception/Error](#ExceptionClass-ExceptionError)
* 6. [方法(Method)命名规范-首字母小写-驼峰命名-动词/动词+名词](#Method---)
	* 6.1. [表述获取-前缀get](#-get)
	* 6.2. [表述查询-前缀find/query](#-findquery)
	* 6.3. [表述条件-连接符by/with](#-bywith)
* 7. [表述设置-前缀set,insert,update,delete](#-setinsertupdatedelete)
	* 7.1. [获取长度或数量-length或size](#-lengthsize)
	* 7.2. [返回值为布尔类型-前缀 is/has](#-ishas)
	* 7.3. [数据类型转换-前缀to](#-to)
* 8. [变量(Variable)命名规范-小写字母开头-驼峰命名](#Variable--)
* 9. [常量命名规范-全部大写的英文单词-“_”进行分割](#--_)
* 10. [枚举(Enum)类命名规范-首字母大写-驼峰命名法](#Enum--)
* 11. [其他](#)
	* 11.1. [数组](#-1)
	* 11.2. [列表](#-1)
	* 11.3. [表述复数或者集合-复数形式s](#-s)
	* 11.4. [Map数据-后缀Map](#Map-Map)
	* 11.5. [泛型类-复杂约定好难学](#-)
	* 11.6. [接口实现类-Impl后缀](#-Impl)
	* 11.7. [测试类和测试方法+“Test”](#Test)
* 12. [扩展：速记 Java 开发中的各种O](#JavaO)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->

##  1. <a name='Package-'></a>包(Package)命名规范-小写英文字母

作用:

- 将功能相似或相关的`类`或者`接口`进行分组管理

- 便于类的`定位和查找`

- 避免`类名的冲突`和访问控制，使代码更容易维护

通常，包命使用`小写英文字母`进行命名，并使用“.”进行分割，每个被分割的单元只能包含一个名词。

一般地，包命名常采用顶级域名作为前缀，例如com，net，org，edu，gov，cn，io等，随后紧跟公司/组织/个人名称以及功能模块名称。下面是一些包命名示例：

```scala
package org.springframework.boot.autoconfigure.cloud
package org.springframework.boot.util
package org.hibernate.action
package org.hibernate.cfg
package com.alibaba.druid
package com.alibaba.druid.filter
package com.alibaba.nacos.client.config
package com.ramostear.blog.web
```

下面是Oracle Java的一些常见包命名例子：

```scala
package java.beans
package java.io
package java.lang
package java.net
package java.util
package javax.annotation
```

##  2. <a name='Class---no'></a>类(Class)命名规范-首字母大写-驼峰命名-no缩写

类(Class)通常采用名词进行命名，且`首字母大写`，如果一个类名包含两个以上名词，建议使用`驼峰命名(Camel-Case)`法书写类名,每个名词首字母也应该大写。一般地，类名的书写尽量使其保持简单和描述的完整性，因此在书写类名时`不建议使用缩写`

>一些约定俗成的命名除外。例如 Internationalization and Localization 缩写成i18n，Uniform Resource Identifier缩写成URI，Data Access Object缩写成DAO，JSON Web Token缩写成JWT，HyperText Markup Language缩写成HTML等等。

下列是一些常见的类命名示例：

```scala
public class UserDTO{

}
class EmployeeService{

}
class StudentDAO{ 
// Data Access Object缩写成DAO
}
class OrderItemEntity{

}
public class UserServiceImpl{

}
public class OrderItemController{

}
```

下面是 Oracle Java 中的一些标准命名示例：

```scala

public class HTMLEditorKit{

}
public abstract class HttpContext{

}
public interface ImageObserver{

}
public class ArrayIndexOutOfBoundsException{

}
public class enum Thread.State{

}
```

##  3. <a name='Interface-'></a>接口(Interface)命名规范-形容词\动词

与普通类名不同的是，接口命名时通常采用`形容词`或`动词`来描述`接口的动作行为`。下列是Oracle Java中一些标准库的接口使用形容词命名示例：

```java
public interface Closeable{

}
public interface Cloneable{

}
public interface Runnable{

}
public interface Comparable<T>{

}
public interface CompletionService<V>{

}
public interface Iterable<T>{

}
public interface EventListener{

}
```

在`Spring Framework标准库`中，通常采用`名词+动词/形容词`的组合方式来命名接口，下列是`Spring Framework`中一些接口命名示例：

```java
public interface AfterAdvice{

}
public interface TargetClassAware{

}
public interface ApplicationContextAware{

}
public interface MessageSourceResolvable{

}
```

##  4. <a name='AbstractClass-AbstractBase'></a>抽象类(Abstract Class)命名规范-前缀Abstract/Base

提高抽象类的可读性，在命名抽象类时，会以`“Abstract”/“Base”`作为类命的`前缀`。下面是编程中一些常规的命名示例：

```java
public abstract class AbstractRepository<T>{

}
public abstract class AbstractController{

}
public abstract class BaseDao<T,ID>{

}
public abstract class AbstractCommonService<T>{

}
```

以下是`Spring Framework`中常见的抽象类示例：

```java
public abstract class AbstractAspectJAdvice{

}
public abstract class AbstractSingletonProxyFactoryBean{

}
public abstract class AbstractBeanFactoryPointcutAdvisor{

}
public abstract class AbstractCachingConfiguration{

}
public abstract class AbstractContextLoaderInitializer{

}
```

##  5. <a name='ExceptionClass-ExceptionError'></a>异常类(Exception Class)命名规范-后缀Exception/Error

异常类在命名时需要使用`“Exception”`作为其`后缀`。下面是常见的异常类命名示例：

```java
public class FileNotFoundException{

}
public class UserAlreadyExistException{

}
public class TransactionException{

}
public class ClassNotFoundException{

}
public class IllegalArgumentException{

}
public class IndexOutOfBoundsException{

}

```

另外，在Java中还有另外一类异常类，它们属于系统异常，这一类异常类的命名使用`“Error”`作为其后缀，以区分`Exception(编码，环境，操作等异常)`。下面是`系统异常(非检查异常)`的命名示例：

```java

public abstract class VirtualMachineError{

}
public class StackOverflowError{

}
public class OutOfMemoryError{

}
public class IllegalAccessError{

}
public class NoClassDefFoundError{

}
public class NoSuchFieldError{

}
public class NoSuchMethodError{

}
```

##  6. <a name='Method---'></a>方法(Method)命名规范-首字母小写-驼峰命名-动词/动词+名词

方法(Method)命名时,其`首字母应该小写`，如果方法签名由多个单词组成，则从第二个单词起，使用`驼峰命名`法进行书写。一般地，在对方法进行命名时，通常采用`动词/动词+名词`的组合，下面是方法命名的一些常见示例。

###  6.1. <a name='-get'></a>表述获取-前缀get

如果一个方法用于获取某个值，通常使用`“get”`作为其`前缀`，例如：

```java
public String getUserName(){

}
public List<Integer> getUserIds(){

}
public User getOne(){

}
```

###  6.2. <a name='-findquery'></a>表述查询-前缀find/query

如果方法需要通过查询或筛选的方式获取某个数据，通常使用`“find”/“query”`作为其`前缀`，例如：

```java
public List<User> findOne(Integer id){

}
public List<Integer> findAll(){

} 
public List<String> queryOrders(){

}

```

###  6.3. <a name='-bywith'></a>表述条件-连接符by/with

如果一个方法需要一些`条件参数`，则可以使用`“by”/“with”`等字符作为方法名中条件的`连接符`，例如：

```java
public User findByUsername(String username){

}
public List<Integer> getUserIdsWithState(boolean state){

}
public List<User> findAllByUsernameOrderByIdDesc(String username){

}
```

##  7. <a name='-setinsertupdatedelete'></a>表述设置-前缀set,insert,update,delete

如果一个方法是要设置，插入，修改，删除等操作，应该将对应的动词(`set,insert,update,delete`)作为其名词的`前缀`，例如：

```java
public void setName(String name){

}
public User insert(User user){

}
public void update(User user){

}
public void clearAll(){

}
```

###  7.1. <a name='-lengthsize'></a>获取长度或数量-length或size

如果一个方法用于获取某组数据的长度或数量，则该方法应该使用`length或size`命名；

```java
public long length(){

}
public int size(){

}
```

###  7.2. <a name='-ishas'></a>返回值为布尔类型-前缀 is/has

如果方法的返回值为布尔类型(Boolean)，则该方法应该使用`“is”或”has”`作为`前缀`；

```java
public boolean isOpen(){

}
public boolean isNotEmpty(){

}
public boolean hasLength(){

}
```

###  7.3. <a name='-to'></a>数据类型转换-前缀to

如果方法用于将一种类型的数据`转换`为另一种数据数类型，则可以使用`“to”`作为`前缀`。下面是综合示例：

```java
public Set<Integer> mapToSet(Map map){

}
public UserDto convertTo(User user){

}
public String toString(Object obj){

}
```

##  8. <a name='Variable--'></a>变量(Variable)命名规范-小写字母开头-驼峰命名

包括`参数名称`，`成员变量`和`局部变量`。变量命名通常以`小写字母开头`，如果变量名由多个单词构成，则从第二个单词起首字母需要大写，在变量命名过程中，`不建议使用“_”`作为前缀或者单词之间的分割符号。下面是一些常见的变量命名示例：

```java
private String nickName;
private String mobileNumber;
private Long id;
private String username;
private Long orderId;
private Long orderItemId;
```

##  9. <a name='--_'></a>常量命名规范-全部大写的英文单词-“_”进行分割

一般地，常量名称采用`全部大写的英文单词`书写，如果常量名称由多个单词组成，则单词之间统一使用`“_”进行分割`，下面是常量命名示例：

```java
public static final String LOGIN_USER_SESSION_KEY = "current_login_user";
public static final int MAX_AGE_VALUE = 120;
public static final int DEFAULT_PAGE_NO = 1;
public static final long MAX_PAGE_SIZE = 1000;
public static final boolean HAS_LICENSE = false;
public static final boolean IS_CHECKED = false;
```

##  10. <a name='Enum--'></a>枚举(Enum)类命名规范-首字母大写-驼峰命名法

`首字母大写`，采用`驼峰命名法`；枚举类中定义的值的名称遵循常量的命名规范，且`枚举值`的名称需要与类名有一定的关联性，下面是枚举的一些示例：

```java
public enum Color{
    RED,YELLOW,BLUE,GREEN,WHITE;
}
public enum PhysicalSize{
    TINY,SMALL,MEDIUM,LARGE,HUGE,GIGANTIC;
}
```

下面是Oracle Java标准库中的一个示例：

```java
public enum ElementType{
    TYPE,
    FIELD,
    METHOD,
    PARAMETER,
    CONSTRUCTOR,
    LOCAL_VARIABLE,
    ANNOTATION_TYPE,
    PACKAGE,
    TYPE_PARAMETER,
    TYPE_USE;
}
```

##  11. <a name=''></a>其他

###  11.1. <a name='-1'></a>数组

在定义数组时，为了便于阅读，尽量保持以下的书写规范：

```java
int[] array = new int[10];
int[] idArray ={1,2,3,4,5};
String[] nameArray = {"First","Yellow","Big"}
```

###  11.2. <a name='-1'></a>列表

```java
public List<String> getNameById(Integer[] ids){

}

public List<String> getNameById(Integer...ids){

}
```

###  11.3. <a name='-s'></a>表述复数或者集合-复数形式s

如果一个变量用于`描述多个数据`时，尽量使用单词的`复数形式`进行书写，例如：

```java
Collection<Order> orders;
int[] values;
List<Item> items;
```

###  11.4. <a name='Map-Map'></a>Map数据-后缀Map

另外，如果表述的是一个Map数据，则应使用`“map”`作为其`后缀`，例如：

```java
Map<String,User> userMap;
Map<String,List<Object>> listMap;
```

###  11.5. <a name='-'></a>泛型类-复杂约定好难学

既保证了**通用性**，又保证了**独特性**

原本繁杂的代码：

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

使用了`泛型`以后就变得简单，用`符号`代替`具体的类型`

```java
public class PersonArrayList<E> {
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

- 将`运行`时的错误，转到了`编译时期`，以免造成`严重后果`
- 在获取`元素`时，就不需要`类型转换`了，获取到的`元素`就是`指定类型`
- 只需要`编写一套代码`，就可以运用`所有类型`

在书写泛型类时，通常做以下的约定：

`E`表示Element，通常用在`集合`中；

`ID`用于表示`对象的唯一标识符类型`

`T`表示Type(类型)，通常指`代类`；

`K`表示Key(键),通常用于Map中；

`V`表示Value(值),通常用于Map中，与K结对出现；

`N`表示Number,通常用于表示数值类型；

`？`表示不确定的Java类型；

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

### 什么情况用`泛型类`？

当`泛型参数`需要在多个`方法`or`成员属性`间扭转，自然就要用到`泛型类`

```java
public class ArrayList<E> {
    静态方法：无法访问泛型类的泛型参数
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

泛型类中的 泛型参数：
需要在【实例化】该类时，指定【具体类型】：
ArrayList<String> strList = new ArrayList<>();
ArrayList<Integer> intList = new ArrayList<>();
ArrayList<int> intList = new ArrayList<>(); // 编译错误

在【泛型】中使用【基本类型】时，会自动装箱成【包装类】
intList.add(123);
Interger num = intList.get(0);

```

注意：泛型之类型，在运行时被擦除，也就是说，没有 `ArrayList<String>`, `ArrayList<Integer>`。只有 `ArrayList`。

## 泛型方法

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

### 泛型接口

```java
public interface Generic<T> {
    void process(T t);
}
```

### 泛型类和泛型方法

|泛型类   |泛型方法   |
|---|---|
| 需要在【实例化】该类时，指定【具体类型】  |  会根据`调用`进行`类型推导` |
| 静态方法：不能访问【泛型参数】  |  静态方法：可以访问【泛型参数】 |
| 适用于：泛型参数需要在多个`方法`or`成员属性`间扭转  | 适用于：只需作用于某个方法  |


###  11.6. <a name='-Impl'></a>接口实现类-Impl后缀

为了便于阅读，在通常情况下，

建议接口实现类使用`“Impl作为后缀”`，

不建议使用大写的“I”作为`接口前缀`，下面是接口和接口实现类的书写示例。

推荐写法：

```java
public interface OrderService{

}
public class OrderServiceImpl implements OrderService{

}
```

不建议的写法：

```java
public interface IOrderService{

}
public class OrderService implements IOrderService{

}
```

###  11.7. <a name='Test'></a>测试类和测试方法+“Test”

在项目中，测试类采用被`测试业务模块名/被测试接口/被测试类+“Test”`的方法进行书写，测试类中的`测试函数`采用`“test”+用例操作_状态`的组合方式进行书写，例如：

```java
public class UserServiceTest{ //+“Test”

    public void testFindByUsernameAndPassword(){

    }

    public void testUsernameExist_notExist(){
     // “test”+ xxxx
    }

    public void testDeleteById_isOk(){
     // “test”+ xxxx
    }
}
```

##  12. <a name='JavaO'></a>扩展：速记 Java 开发中的各种O

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5dcmt073y400.png)