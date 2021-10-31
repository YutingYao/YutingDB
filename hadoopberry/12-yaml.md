## 键值对

我们知道`单个变量`可以用`键值对`，

使用`冒号结构`表示 `key: value`，

注意`冒号`后面要加一个`空格`。

可以使用`缩进层级`的键值对表示一个对象，如下所示：

```yml
person:
  name: 陈皮
  age: 18
  man: true
```

当然也可以使用 `key:{key1: value1, key2: value2, ...}` 的形式，如下：

```yml
person: {name: 陈皮, age: 18, man: true}
```

然后在程序对这几个属性进行`赋值`到`Person对象`中，

注意: `Person类`要加`get/set方法`，不然属性会无法正确取到`配置文件的值`。

使用`@ConfigurationProperties`注入对象，

`@value`不能很好的解析复杂对象。

```java
package com.nobody;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author Mr.nobody
 * @Date 2021/4/13
 * @Version 1.0.0
 */
@Configuration
@ConfigurationProperties(prefix = "my.person")
@Getter
@Setter
public class Person {
    private String name;
    private int age;
    private boolean man;
}
```

## 数组

用`短横杆`加`空格` - 开头的行组成`数组`的每一个元素

```yml
person:
  name: 陈皮
  age: 18
  man: true
  address:
    - 深圳
    - 北京
    - 广州
```

也可以使用`中括号`进行`行内显示`形式，如下：

```yml
person:
  name: 陈皮
  age: 18
  man: true
  address: [深圳, 北京, 广州]

```

在代码中引入方式如下：

```java
package com.nobody;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Description
 * @Author Mr.nobody
 * @Date 2021/4/13
 * @Version 1.0.0
 */
@Configuration
@ConfigurationProperties(prefix = "person")
@Getter
@Setter
@ToString
public class Person {
    private String name;
    private int age;
    private boolean man;
    private List<String> address;
}
```

如果`数组`字段的成员也是一个数组，可以使用`嵌套`的形式，如下：

```yml
person:
  name: 陈皮
  age: 18
  man: true
  address: [深圳, 北京, 广州]
  twoArr:
    -
      - 2
      - 3
      - 1
    -
      - 10
      - 12
      - 30
```

```java
package com.nobody;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "person")
@Getter
@Setter
@ToString
public class Person {
    private String name;
    private int age;
    private boolean man;java
    private List<String> address;
    private List<List<Integer>> twoArr;
}
```

如果数组成员是一个对象，则用如下两种形式形式：

```yml
childs:
  -
    name: 小红
    age: 10
  -
    name: 小王
    age: 15
```

```yml
childs: [{name: 小红, age: 10}, {name: 小王, age: 15}]
```


## 文本块

如果你想引入`多行`的`文本块`，可以使用`|`符号，注意在冒号:和 |符号之间要有`空格`。

```yml
person:
  name: |
    Hello Java!!
    I am fine!
    Thanks! GoodBye!
```

它和加`双引号`的效果一样，双引号能转义特殊字符：

```yml
person:
  name: "Hello Java!!\nI am fine!\nThanks! GoodBye!"
```


## 显示指定类型

有时我们需要显示指定某些值的类型，可以使用` !（感叹号）`显式指定类型。`!单叹号`通常是`自定义类型`，`!!双叹号`是`内置类型`，例如：

```yml
# 指定为字符串
string.value: !!str HelloWorld!
# !!timestamp指定为日期时间类型
datetime.value: !!timestamp 2021-04-13T02:31:00+08:00
```

内置的类型如下：

```c
!!int：整数类型
!!float：浮点类型
!!bool：布尔类型
!!str：字符串类型
!!binary：二进制类型
!!timestamp：日期时间类型
!!null：空值
!!set：集合类型
!!omap，!!pairs：键值列表或对象列表
!!seq：序列
!!map：散列表类型
```


## 引用

引用会用到 `&锚点符合`和 `星号符号`，

`&`用来建立锚点，

`<<` 表示合并到当前数据，用来引用锚点。

```yml
xiaohong: &xiaohong
  name: 小红
  age: 20

dept:
  id: D15D8E4F6D68A4E88E
  <<: *xiaohong
```

上面最终相当于如下：

```yml
xiaohong:
  name: 小红
  age: 20

dept:
  id: D15D8E4F6D68A4E88E
  name: 小红
  age: 20
```

还有一种文件内引用，引用已经定义好的变量，如下：

```yml
base.host: https://chenpi.com
add.person.url: ${base.host}/person/add
```


## 单文件多配置

可以在同一个文件中，实现`多文档分区`，即`多配置`。

在一个yml文件中，通过 `—` 分隔多个不同配置，

根据`spring.profiles.active 的值`来决定启用哪个配置

```yml
#公共配置
spring:
  profiles:
    active: pro # 指定使用哪个文档块
---
#开发环境配置
spring:
  profiles: dev # profiles属性代表配置的名称

server:
  port: 8080
---
#生产环境配置
spring:
  profiles: pro

server:
  port: 8081
```