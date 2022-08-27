
# 精选

### 2. 下段代码输出什么?

```go
func Test2(t *testing.T) {
 slice := []int{0, 1, 2, 3}
 m := make(map[int]*int)

 for key, val := range slice {
  m[key] = &val
 }

 for k, v := range m {
  fmt.Printf("key: %d, value: %d \n", k, *v)
 }
}
```

#### 输出

> key: 0, value: 3
>
> key: 1, value: 3
>
> key: 2, value: 3
>
> key: 3, value: 3
>
#### 解析

>for range 循环的时候会创建每个元素的副本，而不是元素的引用，
>所以 m[key] = &val 取的都是变量 val 的地址，所以最后 map 中的所有元素的值都是变量 val 的地址，
>因为最后 val 被赋值为3，所有输出都是3.

### 10. 关于iota，下面代码输出什么?

```go
func Test10(t *testing.T) {
 const (
  x = iota
  _
  y
  z = "pi"
  k
  p = iota
  q
 )
 fmt.Println(x, y, z, k, p, q)
}
```

#### 输出

> 0 2 pi pi 5 6

### 16. 下面代码输出什么？

```go
func Test16(t *testing.T) {
 a := [2]int{5, 6}
 b := [3]int{5, 6}
 if a == b {
  fmt.Println("equal")
 } else {
  fmt.Println("not equal")
 }
}
```

A. compilation error  

B. equal  

C. not equal  

#### 答案

> A 编译错误
>
> 对于数组而言，一个数组是由数组中的值和数组的长度两部分组成的，如果两个数组长度不同，那么两个数组是属于
> 不同类型的，是不能进行比较的



### 23. 下列代码输出什么？

```go
func Test23(t *testing.T) {
 str := "hello"
 str[0] = 'x'
 fmt.Println(str)
}
```

A. hello

B. xello

C. compilation error

#### 答案

> C 编译错误
>
> Go 语言中的字符串是只读的

### 30. 函数 f1(),f2(),f3()分别返回什么？

```go
func f1() (r int) {
 defer func() {
  r++
 }()
 return 0
}

func f2() (r int) {
 t := 5
 defer func() {
  t = t + 5
 }()
 return t
}

func f3() (r int) {
 defer func(r int) {
  r = r + 5
 }(r)
 return 1
}

func Test30(t *testing.T) {
 fmt.Println(f1())
 fmt.Println(f2())
 fmt.Println(f3())
}
```

#### 输出

> 1 5 1



### 37. 下列代码输出什么？

```go
func Test37(t *testing.T) {
 m := map[int]string{0: "zero", 1: "one"}
 for k, v := range m {
  fmt.Println(k, v)
 }
}
```

#### 答案

> 0 zero
>
> 1 one
>
> 或者
>
> 1 one
>
> 0 zero
>
> map输出是无序的

### 39. 下面代码输出什么？

```go
const (
 a = iota
 b = iota
)
const (
 name = "name"
 c    = iota
 d    = iota
)

func Test39(t *testing.T) {
 fmt.Println(a)
 fmt.Println(b)
 fmt.Println(c)
 fmt.Println(d)
}
```

#### 答案

> 0 1 1 2
>
> iota 在 const 关键字出现时将被重置为0，const中每新增一行常量声明将使 iota 计数一次。

### 42. 下列代码是否可以编译通过？

```go
type Square struct {
 x, y int
}

var m = map[string]Square{
 "foo": Square{2, 3},
}

func Test42(t *testing.T) {
 m["foo"].x = 1
 fmt.Println(m["foo"].x)
}
```

#### 答案

> 编译失败， m["foo"].x = 4 报错
>
> 对于类似 X = Y的赋值操作，必须知道 X 的地址，才能够将 Y 的值赋给 X，
> 但 go 中的 map 的 value 本身是不可寻址的

#### 正确写法

有两种解决方法：

第一种：

```go
square := m["foo"]
square.x = 1
```

第二种：

```go
var m = map[string]*Math{
    "foo": &Math{2, 3},
}
m["foo"].x = 1
```

### 46. 下面代码输出什么？

```go
func f46(n int) (r int) {
 defer func() {
  r += n
  recover()
 }()

 var f func()

 defer f()
 f = func() {
  r += 2
 }
 return n + 1
}

func Test46(t *testing.T) {
 fmt.Println(f46(3))
}
```

#### 答案

> 7

### 50. 关于协程，下列说法正确的有？

A. 协程和线程都可以实现程序的并发执行；

B. 线程比协程更轻量级；

C. 协程不存在死锁问题；

D. 通过 channel 来进行协程间的通信；

#### 答案

> A D

### 53. .关于switch语句，下面说法正确的有?

A. 条件表达式必须为常量或者整数；

B. 单个case中，可以出现多个结果选项；

C. 需要用break来明确退出一个case；

D. 只有在case中明确添加fallthrough关键字，才会继续执行紧跟的下一个case；

#### 答案

> B D

### 60. 关于channel的特性，下面说法正确的是？

A. 给一个 nil channel 发送数据，造成永远阻塞

B. 从一个 nil channel 接收数据，造成永远阻塞

C. 给一个已经关闭的 channel 发送数据，引起 panic

D. 从一个已经关闭的 channel 接收数据，如果缓冲区中为空，则返回一个零值

#### 答案

> A B C D

### 65. 关于select机制，下面说法正确的是?

A. select机制用来处理异步IO问题；

B. select机制最大的一条限制就是每个case语句里必须是一个IO操作；

C. golang在语言级别支持select关键字；

D. select关键字的用法与switch语句非常类似，后面要带判断条件；

#### 答案

> A B C

### 66. 下列代码有什么问题？

```go
func Stop(stop <-chan bool) {
     close(stop)
}
```

#### 答案

> 有方向的 channel 不可以被关闭

# Go面试题及详解



### 2. 下段代码输出什么?

```go
func Test2(t *testing.T) {
 slice := []int{0, 1, 2, 3}
 m := make(map[int]*int)

 for key, val := range slice {
  m[key] = &val
 }

 for k, v := range m {
  fmt.Printf("key: %d, value: %d \n", k, *v)
 }
}
```

#### 输出

> key: 0, value: 3
>
> key: 1, value: 3
>
> key: 2, value: 3
>
> key: 3, value: 3
>
#### 解析

>for range 循环的时候会创建每个元素的副本，而不是元素的引用，
>所以 m[key] = &val 取的都是变量 val 的地址，所以最后 map 中的所有元素的值都是变量 val 的地址，
>因为最后 val 被赋值为3，所有输出都是3.

### 3. 下面代码输出什么?

```go
func Test3(t *testing.T) {
 i := make([]int, 5)
 i = append(i, 1, 2, 3)
 fmt.Println(i)

 j := make([]int, 0)
 j = append(j, 1, 2, 3, 4)
 fmt.Println(j)
}
```

#### 输出

> [0 0 0 0 0 1 2 3]
>
> [1 2 3 4]

#### 解析

> make如果输入值，会默认给其初始化默认值

### 4. 下面这段代码有什么错误吗？

```go
func funcMui(x,y int)(sum int,error){
    return x+y,nil
}
```

#### 解析

> 第二个返回值没有命名,在函数有多个返回值时，只要有一个返回值有命名，
>其他的也必须命名。如果有多个返回值必须加上括号()；
>如果只有一个返回值且命名也必须加上括号()。
>这里的第一个返回值有命名 sum，第二个没有命名，所以错误。

### 5. new() 与 make() 的区别

> new只初始化并返回指针，而make不仅仅要做初始化，还需要设置一些数组的长度、容量等

### 6. 下面几段代码能否通过编译，如果能，输出什么?

```go
func main() {
 list := new([]int)
    // 编译错误
    // new([]int) 之后的 list 是一个未设置长度的 *[]int 类型的指针
    // 不能对未设置长度的指针执行 append 操作。
 list = append(list, 1)
 fmt.Println(list)

 s1 := []int{1, 2, 3}
 s2 := []int{4, 5}
    // 编译错误，s2需要展开
 s1 = append(s1, s2)
 fmt.Println(s1)
}
```

### 7. 下面能否通过编译?

```go
func Test7(t *testing.T) {
 sn1 := struct {
  age  int
  name string
 }{age: 11, name: "qq"}
 sn2 := struct {
  age  int
  name string
 }{age: 11, name: "qq"}
    // true
 if sn1 == sn2 {
  fmt.Println("sn1 == sn2")
 }

 sm1 := struct {
  age int
  m   map[string]string
 }{age: 11, m: map[string]string{"a": "1"}}
 sm2 := struct {
  age int
  m   map[string]string
 }{age: 11, m: map[string]string{"a": "1"}}
    // 编译错误，含有map、slice类型的struct不能进行比较
 if sm1 == sm2 {
  fmt.Println("sm1 == sm2")
 }
}
```

### 8. 通过指针变量 p 访问其成员变量 name，有哪几种方式？

A. p.name

B. (&p).name

C. (*p).name

D. p->name

#### 答案

> AC

### 9. 关于字符串连接，下面语法正确的是？

A. str := 'abc' + '123'

B. str := "abc" + "123"

C. str := '123' + "abc"

D. fmt.Sprintf("abc%d", 123)

### 答案

> BD
>
> golang单引号''中的内容表示单个字符（rune）,反引号``中的内容表示不可转义的字符串

### 10. 关于iota，下面代码输出什么?

```go
func Test10(t *testing.T) {
 const (
  x = iota
  _
  y
  z = "pi"
  k
  p = iota
  q
 )
 fmt.Println(x, y, z, k, p, q)
}
```

#### 输出

> 0 2 pi pi 5 6

### 11. 下面赋值正确的是?

A. var x = nil

B. var x interface{} = nil

C. var x string = nil

D. var x error = nil

#### 答案

> BD

### 12. 关于channel，下面语法正确的是?

A. var ch chan int

B. ch := make(chan int)

C. <- ch

D. ch <-

#### 答案

> ABC
>
> 写 chan 时，<- 右端必须要有值

### 13. 下面代码输出什么？

```go
func hello(num ...int) {
 num[0] = 18
}

func Test13(t *testing.T) {
 i := []int{5, 6, 7}
 hello(i...)
 fmt.Println(i[0])
}
```

A.18

B.5

C.Compilation error  

#### 答案

> A
>
> 可变参数是指针传递

### 14. 下面选择哪个？

```go
func main() {  
    a := 5
    b := 8.1
    fmt.Println(a + b)
}
```

A.13.1  

B.13

C.compilation error  

#### 答案

> C
>
> 整形与浮点形不能相加

### 15. 下面代码输出什么？

```go
func Test15(t *testing.T) {
 a := [5]int{1, 2, 3, 4, 5}
 s := a[3:4:4]
 fmt.Println(s[0])
}
```

A.3

B.4

C.compilation error  

#### 答案

> B
>
> a[3:4][0] = 4 切片的长度是1，容量是2
>
> a[4:4][0] 报越界错误
>
> a[3:4:4][0] = 4 切片的长度是1，容量是1，最后一个4表示切片容量的最大坐标(不含)

### 16. 下面代码输出什么？

```go
func Test16(t *testing.T) {
 a := [2]int{5, 6}
 b := [3]int{5, 6}
 if a == b {
  fmt.Println("equal")
 } else {
  fmt.Println("not equal")
 }
}
```

A. compilation error  

B. equal  

C. not equal  

#### 答案

> A 编译错误
>
> 对于数组而言，一个数组是由数组中的值和数组的长度两部分组成的，如果两个数组长度不同，那么两个数组是属于
> 不同类型的，是不能进行比较的

### 17. 下列哪个类型可以使用 cap()函数？

A. array

B. slice

C. map

D. channel

#### 答案

> A B D
> array 返回数组的元素个数；
> slice 返回 slice 的最大容量；
> channel 返回 channel 的容量；

### 18. 下面代码输出什么？

```go
func Test18(t *testing.T) {
 var i interface{}
 if i == nil {
  fmt.Println("nil")
  return
 }
 fmt.Println("not nil")
}
```

A. nil

B. not nil

C. compilation error  

#### 答案

> A
>
> 当且仅当接口的动态值和动态类型都为 nil 时，接口类型值才为 nil。

### 19. 下面代码输出什么？

```go
func Test19(t *testing.T) {
 s := make(map[string]int)
 delete(s, "h")
 fmt.Println(s["h"])
}
```

A. runtime panic

B. 0

C. compilation error

#### 答案

> B
>
> 删除 map 不存在的键值对时，不会报错，相当于没有任何作用；
> 获取不存在的键值对时，返回值类型对应的零值，所以返回 0
>
> 可以使用if v, ok := s["h"]; ok {}的方式判断键值对是否存在

### 20. 下面代码输出什么？

```go
func Test20(t *testing.T) {
 i := -5
 j := +5
 fmt.Printf("%+d %+d", i, j)
}
```

A. -5 +5

B. +5 +5

C. 0  0

#### 答案

> A
>
> %+d 是带符号输出

### 21. 定义一个全局字符串变量，下列正确的是？

A. var str string

B. str := ""

C. str = ""

D. var str = ""

#### 答案

> A D
>
> B 只支持局部变量声明；C 是赋值，str 必须在这之前已经声明；



### 23. 下列代码输出什么？

```go
func Test23(t *testing.T) {
 str := "hello"
 str[0] = 'x'
 fmt.Println(str)
}
```

A. hello

B. xello

C. compilation error

#### 答案

> C 编译错误
>
> Go 语言中的字符串是只读的

### 24. 下面代码输出什么？

```go
func inc(p *int) int {
 *p++
 return *p
}

func Test24(t *testing.T) {
 p := 1
 inc(&p)
 fmt.Println(p)
}
```

A. 1

B. 2

C. 3

#### 答案

> B

### 25. 关于可变参数的函数调用正确的是？

```go
func add(args ...int) int {

    sum := 0
    for _, arg := range args {
        sum += arg
    }
    return sum
}
```

A. add(1, 2)

B. add(1, 3, 7)

C. add([]int{1, 2})

D. add([]int{1, 3, 7}…)

#### 答案

> A B D

### 26. 下列代码中下划线处可填入哪个变量会打印"yes nil"？

```go
func Test26(t *testing.T) {
 var s1 []int
 var s2 = []int{}
 if ___ == nil {
  fmt.Println("yes nil")
 } else {
  fmt.Println("no nil")
 }
}
```

A. s1

B. s2

C. s1、s2 都可以

#### 答案

> A
>
> nil 切片和空切片。nil 切片和 nil 相等，一般用来表示一个不存在的切片；
> 空切片和 nil 不相等，表示一个空的集合

### 27. 下面代码输出什么？

```go
func Test27(t *testing.T) {
 i := 65
 fmt.Println(string(i))
}
```

A. A

B. 65

C. compilation error

#### 答案

> A
>
> UTF-8 编码中，十进制数字 65 对应的符号是 A

### 28. 切片a,b,c的容量分别是多少？

```go
func Test28(t *testing.T) {
 s := [3]int{1, 2, 3}
 a := s[:0]
 b := s[:2]
 c := s[1:2:cap(s)]
 fmt.Println(cap(a))
 fmt.Println(cap(b))
 fmt.Println(cap(c))
}
```

#### 输出

> 3 3 2
>
> 操作符 [i:j:k]，k 主要是用来限制切片的容量，
> 但是不能大于数组的长度 ，截取得到的切片长度和容量计算方法是 j-i、k-i

### 29. 下面代码输出什么？

```go
func increaseA() int {
 var i int
 defer func() {
  i++
 }()
 return i
}

func increaseB() (r int) {
 defer func() {
  r++
 }()
 return r
}

func Test29(t *testing.T) {
 fmt.Println(increaseA())
 fmt.Println(increaseB())
}
```

#### 输出

> 0 1

### 30. 函数 f1(),f2(),f3()分别返回什么？

```go
func f1() (r int) {
 defer func() {
  r++
 }()
 return 0
}

func f2() (r int) {
 t := 5
 defer func() {
  t = t + 5
 }()
 return t
}

func f3() (r int) {
 defer func(r int) {
  r = r + 5
 }(r)
 return 1
}

func Test30(t *testing.T) {
 fmt.Println(f1())
 fmt.Println(f2())
 fmt.Println(f3())
}
```

#### 输出

> 1 5 1


### 32. 下面的两个切片声明中有什么区别？哪个更可取？

A. var a []int

B. a := []int{}

#### 答案

> A
>
> A 声明的是 nil 切片；B 声明的是长度和容量都为 0 的空切片。
> A的声明不会分配内存，优先选择

### 33. A,B，C，D那个有语法错误？

```go
type S struct {
}

func m(x interface{}) {
}

func g(x *interface{}) {
}

func Test33(t *testing.T) {
 s := S{}
 p := &s
 m(s) //A
 g(s) //B
 m(p) //C
 g(p) //D
}
```

#### 答案

> B D 会编译错误
>
> 函数参数为 interface{} 时可以接收任何类型的参数，包括用户自定义类型等，
> 即使是接收指针类型也用 interface{}，而不是使用 *interface{}。
> 永远不要使用一个指针指向一个接口类型，因为它已经是一个指针。

### 34. 下面代码输出什么？

```go
func Test34(t *testing.T) {
 s1 := []int{1, 2, 3}
 s2 := s1[1:]
 s2[1] = 4
 fmt.Println(s1)
 s2 = append(s2, 5, 6, 7)
 fmt.Println(s1)
}
```

#### 答案

> [1 2 4]
>
> [1 2 4]
>
> golang 中切片底层的数据结构是数组。当使用 s1[1:] 获得切片 s2，和 s1 共享同一个底层数组
> 这会导致 s2[1] = 4 语句影响 s1。
> 而 append 操作会导致底层数组扩容，生成新的数组，因此追加数据后的 s2 不会影响 s1

### 35. 下列代码输出什么？

```go
func Test35(t *testing.T) {
 if a := 1; false {
 } else if b := 2; false {
 } else {
  println(a, b)
 }
}
```

#### 答案

> 1 2

### 36. 下列代码输出什么？

```go
func Test36(t *testing.T) {
 a := 1
 b := 2
 defer calc("A", a, calc("10", a, b))
 a = 0
 defer calc("B", a, calc("20", a, b))
 b = 1
}

func calc(index string, a, b int) int {
 ret := a + b
 fmt.Println(index, a, b, ret)
 return ret
}
```

#### 答案

> 10 1 2 3
>
> 20 0 2 2
>  
> B 0 2 2
>
> A 1 3 4

### 37. 下列代码输出什么？

```go
func Test37(t *testing.T) {
 m := map[int]string{0: "zero", 1: "one"}
 for k, v := range m {
  fmt.Println(k, v)
 }
}
```

#### 答案

> 0 zero
>
> 1 one
>
> 或者
>
> 1 one
>
> 0 zero
>
> map输出是无序的

### 38. 下面代码是否可以编译通过？

```go
type People interface {
    Speak(string) string
}

type Student struct{}

func (stu *Student) Speak(think string) {
    fmt.Println(think)
}

func main() {
    var peo People = Student{}
    think := "speak"
    fmt.Println(peo.Speak(think))
}
```

#### 答案

> 不能编译通过，因为是 *Student 实现了Speak，并不是 值类型的Student，
但是如果是 Student 类型实现了Speak方法，那么用 值类型的`Student{}` 或是指针类型的`&Student{}`都可以访问到该方法

### 39. 下面代码输出什么？

```go
const (
 a = iota
 b = iota
)
const (
 name = "name"
 c    = iota
 d    = iota
)

func Test39(t *testing.T) {
 fmt.Println(a)
 fmt.Println(b)
 fmt.Println(c)
 fmt.Println(d)
}
```

#### 答案

> 0 1 1 2
>
> iota 在 const 关键字出现时将被重置为0，const中每新增一行常量声明将使 iota 计数一次。

### 39. 下面代码输出什么？

```go
type People interface {
 Show()
}

type Student struct{}

func (stu *Student) Show() {

}

func Test40(t *testing.T) {
 var s *Student
 if s == nil {
  fmt.Println("s is nil")
 } else {
  fmt.Println("s is not nil")
 }
 var p People = s
 if p == nil {
  fmt.Println("p is nil")
 } else {
  fmt.Println("p is not nil")
 }
}
```

#### 答案

> s is nil
>
> p is not nil
>
> 当且仅当动态值和动态类型都为 nil 时，接口类型值才为 nil。上面的代码，给变量 p 赋值之后，
> p 的动态值是 nil，但是动态类型却是 *Student，是一个 nil 指针，所以相等条件不成立。

### 41. 下列代码输出什么？

```go
type Direction int

const (
 North Direction = iota
 East
 South
 West
)

func (d Direction) String() string {
 return [...]string{"North", "East", "South", "West"}[d]
}

func Test41(t *testing.T) {
 fmt.Println(South)
}
```

#### 答案

> South

### 42. 下列代码是否可以编译通过？

```go
type Square struct {
 x, y int
}

var m = map[string]Square{
 "foo": Square{2, 3},
}

func Test42(t *testing.T) {
 m["foo"].x = 1
 fmt.Println(m["foo"].x)
}
```

#### 答案

> 编译失败， m["foo"].x = 4 报错
>
> 对于类似 X = Y的赋值操作，必须知道 X 的地址，才能够将 Y 的值赋给 X，
> 但 go 中的 map 的 value 本身是不可寻址的

#### 正确写法

有两种解决方法：

第一种：

```go
square := m["foo"]
square.x = 1
m["foo"] = square
```

第二种：

```go
var m = map[string]*Math{
    "foo": &Math{2, 3},
}
m["foo"].x = 1
```

### 43. 下面代码输出什么？

```go
var p *int

func foo() (*int, error) {
 var i int = 5
 return &i, nil
}

func bar() {
 //use p
 fmt.Println(*p)
}

func Test43(t *testing.T) {
 p, err := foo()
 if err != nil {
  fmt.Println(err)
  return
 }
 bar()
 fmt.Println(*p)
}
```

#### 答案

> bar 函数会发生panic，空指针异常
>
> 因为 err 前面没有声明，所以 p, err := foo() 中的 p 是重新声明的局部变量，而不是我们在前面声明的全局变量 p

### 44. 下面代码输出什么？

```go
func Test44(t *testing.T) {
 v := []int{1, 2, 3}
 for i := range v {
  v = append(v, i)
  fmt.Println(v)
 }
}
```

#### 答案

> [1 2 3 0]
>
> [1 2 3 0 1]
>
> [1 2 3 0 1 2]

### 45. 下面代码输出什么？

```go
func Test45(t *testing.T) {
 var m = [...]int{1, 2, 3}

 for i, v := range m {
  go func() {
   fmt.Println(i, v)
  }()
 }

 time.Sleep(time.Second * 1)
}
```

#### 答案

> 2 3
>
> 2 3
>
> 2 3
>
> for range 使用短变量声明(:=)的形式迭代变量，
> 需要注意的是，变量 i、v 在每次循环体中都会被重用，而不是重新声明。

#### 解决方案

> 有两种解决方式

第一种(推荐)

```go
for i, v := range m {
    go func(i,v int) {
        fmt.Println(i, v)
    }(i,v)
}
```

第二种

```go
for i, v := range m {
    i := i           // 这里的 := 会重新声明变量，而不是重用
    v := v
    go func() {
        fmt.Println(i, v)
    }()
}
```

### 46. 下面代码输出什么？

```go
func f46(n int) (r int) {
 defer func() {
  r += n
  recover()
 }()

 var f func()

 defer f()
 f = func() {
  r += 2
 }
 return n + 1
}

func Test46(t *testing.T) {
 fmt.Println(f46(3))
}
```

#### 答案

> 7

### 47. 下列代码输出什么？

```go
func Test47(t *testing.T) {
 var a = [5]int{1, 2, 3, 4, 5}
 var r [5]int

 for i, v := range a {
  if i == 0 {
   a[1] = 12
   a[2] = 13
  }
  r[i] = v
 }
 fmt.Println("r = ", r)
 fmt.Println("a = ", a)
}
```

#### 答案

> r =  [1 2 3 4 5]
>
> a =  [1 12 13 4 5]

### 48. 下面代码输出什么？

```go
func change(s ...int) {
 s = append(s, 3)
}

func Test48(t *testing.T) {
 slice := make([]int, 5, 5)
 slice[0] = 1
 slice[1] = 2
 // 1
 change(slice...)
 fmt.Println(slice)
 // 2
 change(slice[0:2]...)
 fmt.Println(slice)
}
```

#### 答案

> [1 2 0 0 0]
>
> [1 2 3 0 0]
>
> 1.change函数内部append时超出了s的容量，生成了新的底层数组的切片，
> 未对change函数外的切片产生影响。
>
> 2.change函数收到的切片长度小于容量，append没有重新生成底层数组，
> 直接修改了底层数组对应位置的值，影响到了change函数外的切片。

### 49. 下列代码输出什么？

```go
func Test49(t *testing.T) {
 var m = map[string]int{
  "A": 21,
  "B": 22,
  "C": 23,
 }
 counter := 0
 for k, v := range m {
  if counter == 0 {
   delete(m, "A")
  }
  counter++
  fmt.Println(k, v)
 }
 fmt.Println("counter is ", counter)
}
```

#### 答案

> counter is 2 或者 counter is 3
>
> for range map 是无序的，若先遍历到A则counter是3，否则是2

### 50. 关于协程，下列说法正确的有？

A. 协程和线程都可以实现程序的并发执行；

B. 线程比协程更轻量级；

C. 协程不存在死锁问题；

D. 通过 channel 来进行协程间的通信；

#### 答案

> A D

### 51.关于循环语句，下面说法正确的有？

A. 循环语句既支持 for 关键字，也支持 while 和 do-while；

B. 关键字 for 的基本使用方法与 C/C++ 中没有任何差异；

C. for 循环支持 continue 和 break 来控制循环，但是它提供了一个更高级的 break，可以选择中断哪一个循环；

D. for 循环不支持以逗号为间隔的多个赋值语句，必须使用平行赋值的方式来初始化多个变量；

#### 答案

> C D

### 52. 下列代码输出什么？

```go
func Test52(t *testing.T) {
 i := 1
 s := []string{"A", "B", "C"}
 i, s[i-1] = 2, "Z"
 fmt.Printf("s: %v \n", s)
}
```

#### 答案

> s: [Z B C]

### 53. .关于switch语句，下面说法正确的有?

A. 条件表达式必须为常量或者整数；

B. 单个case中，可以出现多个结果选项；

C. 需要用break来明确退出一个case；

D. 只有在case中明确添加fallthrough关键字，才会继续执行紧跟的下一个case；

#### 答案

> B D

### 54. 下列Add函数定义正确的是？

```go
func Test54(t *testing.T) {
 var a Integer = 1
 var b Integer = 2
 var i interface{} = &a
 sum := i.(*Integer).Add(b)
 fmt.Println(sum)
}
```

```
A.
type Integer int
func (a Integer) Add(b Integer) Integer {
        return a + b
}

B.
type Integer int
func (a Integer) Add(b *Integer) Integer {
        return a + *b
}

C.
type Integer int
func (a *Integer) Add(b Integer) Integer {
        return *a + b
}

D.
type Integer int
func (a *Integer) Add(b *Integer) Integer {
        return *a + *b
}
```

#### 答案

> A C

### 55. 关于 bool 变量 b 的赋值，下面错误的用法是？

A. b = true

B. b = 1

C. b = bool(1)

D. b = (1 == 2)

#### 答案

> B C

### 56. 关于变量的自增和自减操作，下面语句正确的是？

A.

```go
i := 1
i++
```

B.

```go
i := 1
j = i++
```

C.

```go
i := 1
++i
```

D.

```go
i := 1
i--
```

#### 答案

> A D
>
> go 里面没有 ++i 和 --i

### 56. 关于GetPodAction定义，下面赋值正确的是

```go
type Fragment interface {
        Exec(transInfo *TransInfo) error
}
type GetPodAction struct {
}
func (g GetPodAction) Exec(transInfo *TransInfo) error {
        ...
        return nil
}
```

A. var fragment Fragment = new(GetPodAction)

B. var fragment Fragment = GetPodAction

C. var fragment Fragment = &GetPodAction{}

D. var fragment Fragment = GetPodAction{}

#### 答案

> A C D

### 58. 关于整型切片的初始化，下面正确的是？

A. s := make([]int)

B. s := make([]int, 0)

C. s := make([]int, 5, 10)

D. s := []int{1, 2, 3, 4, 5}

#### 答案

> B C D

### 59. 下列代码是否会触发异常？

```go
func Test59(t *testing.T) {
 runtime.GOMAXPROCS(1)
 intChan := make(chan int, 1)
 stringChan := make(chan string, 1)
 intChan <- 1
 stringChan <- "hello"
 select {
 case value := <-intChan:
  fmt.Println(value)
 case value := <-stringChan:
  panic(value)
 }
}
```

#### 答案

> 不一定，当两个chan同时有值时，select 会随机选择一个可用通道做收发操作

### 60. 关于channel的特性，下面说法正确的是？

A. 给一个 nil channel 发送数据，造成永远阻塞

B. 从一个 nil channel 接收数据，造成永远阻塞

C. 给一个已经关闭的 channel 发送数据，引起 panic

D. 从一个已经关闭的 channel 接收数据，如果缓冲区中为空，则返回一个零值

#### 答案

> A B C D

### 61. 下列代码有什么问题？

```go
const i = 100
var j = 123

func main() {
    fmt.Println(&j, j)
    fmt.Println(&i, i)
}
```

#### 答案

> Go语言中，常量无法寻址, 是不能进行取指针操作的

### 62. 下列代码输出什么？

```go
func Test62(t *testing.T) {
 x := []string{"a", "b", "c"}
 for v := range x {
  fmt.Print(v)
 }
}
```

#### 答案

> 012
>
> range 一个返回值时，这个值是下标，两个值时，第一个是下标，第二个是值，当 x 为 map时，第一个是key，第二个是value

### 63. 关于无缓冲和有冲突的channel，下面说法正确的是？

A. 无缓冲的channel是默认的缓冲为1的channel；

B. 无缓冲的channel和有缓冲的channel都是同步的；

C. 无缓冲的channel和有缓冲的channel都是非同步的；

D. 无缓冲的channel是同步的，而有缓冲的channel是非同步的；

#### 答案

> D

### 64. 下列代码输出什么？

```go
func Foo(x interface{}) {
 if x == nil {
  fmt.Println("empty interface")
  return
 }
 fmt.Println("non-empty interface")
}
func Test64(t *testing.T) {
 var x *int = nil
 Foo(x)
}
```

#### 答案

> non-empty interface
>
> 接口除了有静态类型，还有动态类型和动态值，
> 当且仅当动态值和动态类型都为 nil 时，接口类型值才为 nil。
> 这里的 x 的动态类型是 *int，所以 x 不为 nil

### 65. 关于select机制，下面说法正确的是?

A. select机制用来处理异步IO问题；

B. select机制最大的一条限制就是每个case语句里必须是一个IO操作；

C. golang在语言级别支持select关键字；

D. select关键字的用法与switch语句非常类似，后面要带判断条件；

#### 答案

> A B C

### 66. 下列代码有什么问题？

```go
func Stop(stop <-chan bool) {
     close(stop)
}
```

#### 答案

> 只可以接收数据的 channel 不可以被关闭

### 67. 下列代码输出什么？

```go
func Test67(t *testing.T) {
 var x = []int{2: 2, 3, 0: 1}
 fmt.Println(x)
}
```

#### 答案

> [1 0 2 3]

### 68. 下列代码输出什么？

```go
func incr(p *int) int {
 *p++
 return *p
}
func Test68(t *testing.T) {
 v := 1
 incr(&v)
 fmt.Println(v)
}
```

#### 答案

> 2

### 69. 下列代码输出什么？

```go
func Test69(t *testing.T) {
 var a = []int{1, 2, 3, 4, 5}
 var r = make([]int, 0)

 for i, v := range a {
  if i == 0 {
   a = append(a, 6, 7)
  }
  r = append(r, v)
 }
 fmt.Println(r)
}
```

#### 答案

> [1 2 3 4 5]
>
> a 在 for range 过程中增加了两个元素
> len 由 5 增加到 7，但 for range 时会使用 a 的副本 a' 参与循环，副本的 len 依旧是 5，
> 因此 for range 只会循环 5 次，也就只获取 a 对应的底层数组的前 5 个元素。

### 70. 下列代码有什么问题？

```go
func main() {
    var s []int
    s = append(s,1)

    var m map[string]int
    m["one"] = 1 
}
```

#### 答案

> 切片可以开箱即用，但 map 需要用 make函数 进行初始化之后才能赋值

### 71. 下列函数能否正确输出?

```go
func main() {
    var fn1 = func() {}
    var fn2 = func() {}

    if fn1 != fn2 {
        println("fn1 not equal fn2")
    }
}
```

#### 答案

> 编译错误，func 只能与 nil 做比较

### 72. 下列代码是否正确?

```go
type T struct {
    n int
}

func main() {
    m := make(map[int]T)
    m[0].n = 1
    fmt.Println(m[0].n)
}
```

#### 答案

> 编译错误, map[key]struct 中 struct 是不可寻址的，所以无法直接赋值。

#### 修复

```go
func main() {
 m := make(map[int]T)
 t := T{1}
 m[0] = t
 fmt.Println(m[0].n)
}
```

### 73. 下列代码有什么问题?

```go
type X struct {}
func (x *X) test()  {
 println(x)
}

func main() {
   var a *X
   a.test()
   X{}.test()
}
```

#### 答案

> X{} 是不可寻址的，不能直接调用方法

#### 修复

```go
func main() {
   var a *X
   a.test()
   // 为其定义一个变量，让其可寻址
   var x = X{}
   x.test()
}
```

### 74. 关于 channel 下面描述正确的是？

A. 向已关闭的通道发送数据会引发 panic；

B. 从已关闭的缓冲通道接收数据，返回已缓冲数据或者零值；

C. 无论接收还是接收，nil 通道都会阻塞；

D. close() 可以用于只接收通道；

E. 单向通道可以转换为双向通道；

F. 不能在单向通道上做逆向操作（例如：只发送通道用于接收）；

#### 答案

> A B C F

### 75. 下列代码输出什么?

```go
func Test75(t *testing.T) {
 s := make([]int, 3, 9)
 fmt.Println(len(s))
 s2 := s[4:8]
 fmt.Println(len(s2))
}
```

#### 答案

> 3 4

### 76. 下列哪一行会panic?

```go
func Test76(t *testing.T) {
 var x interface{}
 var y interface{} = []int{3, 5}
 _ = x == x
 _ = x == y
 _ = y == y
}
```

#### 答案

> _ = y == y 会发生panic, 因为两个比较值的动态类型为同一个不可比较类型

### 77. 下列哪行代码会panic?

```go
func Test77(t *testing.T) {
 x := make([]int, 2, 10)
 _ = x[6:10]
 _ = x[6:]
 _ = x[2:]
}
```

#### 答案

> _ = x[6:] 这一行会发生panic, 截取符号 [i:j]，
>如果 j 省略，默认是原切片或者数组的长度，x 的长度是 2，小于起始下标 6 ，所以 panic

### 78. 下列代码有什么问题?

```go
type data struct {
 sync.Mutex
}

func (d data) test(s string) {
 d.Lock()
 defer d.Unlock()

 for i := 0; i < 5; i++ {
  fmt.Println(s, i)
  time.Sleep(time.Second)
 }
}

func Test78(t *testing.T) {
 var wg sync.WaitGroup
 wg.Add(2)
 var d data

 go func() {
  defer wg.Done()
  d.test("read")
 }()

 go func() {
  defer wg.Done()
  d.test("write")
 }()

 wg.Wait()
}
```

#### 答案

> 锁失效。将 Mutex 作为匿名字段时，相关的方法必须使用指针接收者，否则会导致锁机制失效。

#### 修复

```go
// 指针接收者
func (d *data) test(s string)  {     
   d.Lock()
   defer d.Unlock()

   for i:=0;i<5 ;i++  {
      fmt.Println(s,i)
      time.Sleep(time.Second)
   }
}
```

### 79. 下列代码输出什么?

```go
func Test79(t *testing.T) {
 var k = 1
 var s = []int{1, 2}
 k, s[k] = 0, 3
 fmt.Println(s[0] + s[1])
}
```

#### 答案

> 4

### 80. 下列那行代码会panic?

```go
func Test80(t *testing.T) {
 nil := 123
 fmt.Println(nil)
 var _ map[string]int = nil
}
```

#### 答案

> var _ map[string]int = nil 会编译错误， 当前作用域中，
> 预定义的 nil 被覆盖，此时 nil 是 int 类型值，不能赋值给 map 类型。

### 81. 下列代码输出什么?

```go
func Test81(t *testing.T) {
 var x int8 = -128
 var y = x / -1
 fmt.Println(y)
}
```

#### 答案

> -128, 因为溢出 int8为 -128 ~ 127 之间



### 83. 关于字符串拼接,下列正确的是?

A. str := 'abc' + '123'

B. str := "abc" + "123"

C. str ：= '123' + "abc"

D. fmt.Sprintf("abc%d", 123)

#### 答案

> B D 双引号用来表示字符串 string，其实质是一个 byte 类型的数组，单引号表示 rune 类型。

### 84. 下列代码有什么问题?

```go
func main() {
     runtime.GOMAXPROCS(1)
     go func() {
         for i:=0;i<10 ;i++  {
             fmt.Println(i)
         }
     }()
 
    for {}
}
```

#### 答案

> for{} 独占 CPU 资源导致其他 Goroutine 饿死

#### 修复

```go
func main() {
     runtime.GOMAXPROCS(1)
     go func() {
         for i:=0;i<10 ;i++  {
             fmt.Println(i)
         }
     }()
 
    select {}
}
```





### 87. 下列代码输出什么?

```go
func printI(num ...int) {
 num[0] = 18
}

func Test87(t *testing.T) {
 i := []int{5, 6, 7}
 printI(i...)
 fmt.Println(i[0])
}
```

#### 答案

> 18, 可变参数是指针传递

### 88. 下列代码输出什么?

```go
func alwaysFalse() bool {
 return false
}

func Test88(t *testing.T) {
 switch alwaysFalse(); {
 case true:
  println(true)
 case false:
  println(false)
 }
}
```

#### 答案

> true, Go代码断行规则

### 89. 下列代码有什么问题?

```go
type ConfigOne struct {
 Daemon string
}

 func (c *ConfigOne) String() string {
    return fmt.Sprintf("print: %v", c)
 }
 
 func main() {
    c := &ConfigOne{}
    c.String()
}
```

#### 答案

> 无限循环，栈溢出, 如果结构体类型定义了 String() 方法，
> 使用 Printf()、Print() 、 Println() 、 Sprintf() 等格式化输出时会自动使用 String() 方法。



### 91. 下列代码输出什么?

```go
func main() {
     a := [3]int{0, 1, 2}
     s := a[1:2]
 
     s[0] = 11
     s = append(s, 12)
     s = append(s, 13)
     s[0] = 21
 
    fmt.Println(a)
    fmt.Println(s)
}
```

#### 答案

> [0 11 12]
>
> [21 12 13]

### 92. 下列代码输出什么?

```go
func Test92(t *testing.T) {
 fmt.Println(strings.TrimRight("ABBA", "BA"))
}
```

#### 答案

> 输出空字符串, TrimRight() 会将第二个参数字符串里面所有的字符拿出来处理，
> 只要与其中任何一个字符相等，便会将其删除。想正确地截取字符串，可以参考 TrimSuffix() 函数。

### 93. 下列代码输出什么?

```go
func Test93(t *testing.T) {
 var src, dst []int
 src = []int{1, 2, 3}
 copy(dst, src)
 fmt.Println(dst)
}
```

#### 答案

> 输出 [], 如果想要将 src 完全拷贝至 dst，必须给 dst 分配足够的内存空间。

#### 修复

```go
func Test93(t *testing.T) {
 var src, dst []int
 src = []int{1, 2, 3}
    dst = make([]int, len(src))
 copy(dst, src)
 fmt.Println(dst)
}
```

或者直接使用`append`

```go
func Test93(t *testing.T) {
 var src, dst []int
 src = []int{1, 2, 3}
 dst = append(dst, src...)
 fmt.Println(dst)
}
```

### 94. 下列代码是否可以编译通过?

```go
type User struct {
   Name string
}
 
func (u *User) SetName(name string) {
    u.Name = name
    fmt.Println(u.Name)
}

type Employee User

func main() {
    employee := new(Employee)
    employee.SetName("Jack")
}
```

#### 答案

> 编译不通过, 当使用 type 声明一个新类型，它不会继承原有类型的方法集。

### 95. 关于map，下面说法正确的是？

A. map 反序列化时 json.unmarshal() 的入参必须为 map 的地址；

B. 在函数调用中传递 map，则子函数中对 map 元素的增加不会导致父函数中 map 的修改；

C. 在函数调用中传递 map，则子函数中对 map 元素的修改不会导致父函数中 map 的修改；

D. 不能使用内置函数 delete() 删除 map 的元素

#### 答案

> A

### 96. 关于同步锁，下面说法正确的是？

A. 当一个 goroutine 获得了 Mutex 后，其他 goroutine 就只能乖乖的等待，除非该 goroutine 释放这个 Mutex；

B. RWMutex 在读锁占用的情况下，会阻止写，但不阻止读；

C. RWMutex 在写锁占用情况下，会阻止任何其他 goroutine（无论读和写）进来，整个锁相当于由该 goroutine 独占；

D. Lock() 操作需要保证有 Unlock() 或 RUnlock() 调用与之对应；

#### 答案

> A B C , 106

> 目录
>
> * [1\. 下面这段代码输出的内容：](#1-%E4%B8%8B%E9%9D%A2%E8%BF%99%E6%AE%B5%E4%BB%A3%E7%A0%81%E8%BE%93%E5%87%BA%E7%9A%84%E5%86%85%E5%AE%B9)
> * [2\. 下面这段代码输出什么，说明原因。](#2-%E4%B8%8B%E9%9D%A2%E8%BF%99%E6%AE%B5%E4%BB%A3%E7%A0%81%E8%BE%93%E5%87%BA%E4%BB%80%E4%B9%88%E8%AF%B4%E6%98%8E%E5%8E%9F%E5%9B%A0)
> * [3\. 下面两段代码输出什么？](#3-%E4%B8%8B%E9%9D%A2%E4%B8%A4%E6%AE%B5%E4%BB%A3%E7%A0%81%E8%BE%93%E5%87%BA%E4%BB%80%E4%B9%88)
> * [4\. 下面这段代码有什么缺陷？](#4-%E4%B8%8B%E9%9D%A2%E8%BF%99%E6%AE%B5%E4%BB%A3%E7%A0%81%E6%9C%89%E4%BB%80%E4%B9%88%E7%BC%BA%E9%99%B7)
> * [5\. new() 与 make() 的区别](#5-new-%E4%B8%8E-make-%E7%9A%84%E5%8C%BA%E5%88%AB)
> * [6\. 下面这段代码能否通过编译，不能的话原因是什么；如果能，输出什么？](#6-%E4%B8%8B%E9%9D%A2%E8%BF%99%E6%AE%B5%E4%BB%A3%E7%A0%81%E8%83%BD%E5%90%A6%E9%80%9A%E8%BF%87%E7%BC%96%E8%AF%91%E4%B8%8D%E8%83%BD%E7%9A%84%E8%AF%9D%E5%8E%9F%E5%9B%A0%E6%98%AF%E4%BB%80%E4%B9%88%E5%A6%82%E6%9E%9C%E8%83%BD%E8%BE%93%E5%87%BA%E4%BB%80%E4%B9%88)
> * [7\. 下面这段代码能否通过编译，不能的话原因是什么；如果可以，输出什么？](#7%E4%B8%8B%E9%9D%A2%E8%BF%99%E6%AE%B5%E4%BB%A3%E7%A0%81%E8%83%BD%E5%90%A6%E9%80%9A%E8%BF%87%E7%BC%96%E8%AF%91%E4%B8%8D%E8%83%BD%E7%9A%84%E8%AF%9D%E5%8E%9F%E5%9B%A0%E6%98%AF%E4%BB%80%E4%B9%88%E5%A6%82%E6%9E%9C%E5%8F%AF%E4%BB%A5%E8%BE%93%E5%87%BA%E4%BB%80%E4%B9%88)
> * [8\. 下面这段代码能否通过编译，如果可以，输出什么？](#8-%E4%B8%8B%E9%9D%A2%E8%BF%99%E6%AE%B5%E4%BB%A3%E7%A0%81%E8%83%BD%E5%90%A6%E9%80%9A%E8%BF%87%E7%BC%96%E8%AF%91%E5%A6%82%E6%9E%9C%E5%8F%AF%E4%BB%A5%E8%BE%93%E5%87%BA%E4%BB%80%E4%B9%88)
> * [9\. 下面这段代码能否通过编译？不能的话，原因是什么？如果通过，输出什么？](#9%E4%B8%8B%E9%9D%A2%E8%BF%99%E6%AE%B5%E4%BB%A3%E7%A0%81%E8%83%BD%E5%90%A6%E9%80%9A%E8%BF%87%E7%BC%96%E8%AF%91%E4%B8%8D%E8%83%BD%E7%9A%84%E8%AF%9D%E5%8E%9F%E5%9B%A0%E6%98%AF%E4%BB%80%E4%B9%88%E5%A6%82%E6%9E%9C%E9%80%9A%E8%BF%87%E8%BE%93%E5%87%BA%E4%BB%80%E4%B9%88)
> * [10\. 通过指针变量p访问其成员变量name,有哪几种方式？](#10-%E9%80%9A%E8%BF%87%E6%8C%87%E9%92%88%E5%8F%98%E9%87%8Fp%E8%AE%BF%E9%97%AE%E5%85%B6%E6%88%90%E5%91%98%E5%8F%98%E9%87%8Fname%E6%9C%89%E5%93%AA%E5%87%A0%E7%A7%8D%E6%96%B9%E5%BC%8F)
> * [11\. 下面这段代码能否通过编译？如果通过，输出什么？](#11-%E4%B8%8B%E9%9D%A2%E8%BF%99%E6%AE%B5%E4%BB%A3%E7%A0%81%E8%83%BD%E5%90%A6%E9%80%9A%E8%BF%87%E7%BC%96%E8%AF%91%E5%A6%82%E6%9E%9C%E9%80%9A%E8%BF%87%E8%BE%93%E5%87%BA%E4%BB%80%E4%B9%88)
> * [12\. 以下代码输出什么？](#12-%E4%BB%A5%E4%B8%8B%E4%BB%A3%E7%A0%81%E8%BE%93%E5%87%BA%E4%BB%80%E4%B9%88)
> * [13\. 关于字符串连接，下面语法正确的是？](#13-%E5%85%B3%E4%BA%8E%E5%AD%97%E7%AC%A6%E4%B8%B2%E8%BF%9E%E6%8E%A5%E4%B8%8B%E9%9D%A2%E8%AF%AD%E6%B3%95%E6%AD%A3%E7%A1%AE%E7%9A%84%E6%98%AF)
> * [14\. 下面这段代码能否编译通过？如果可以，输出什么？](#14-%E4%B8%8B%E9%9D%A2%E8%BF%99%E6%AE%B5%E4%BB%A3%E7%A0%81%E8%83%BD%E5%90%A6%E7%BC%96%E8%AF%91%E9%80%9A%E8%BF%87%E5%A6%82%E6%9E%9C%E5%8F%AF%E4%BB%A5%E8%BE%93%E5%87%BA%E4%BB%80%E4%B9%88)
> * [15\. 下面赋值正确的是（）](#15-%E4%B8%8B%E9%9D%A2%E8%B5%8B%E5%80%BC%E6%AD%A3%E7%A1%AE%E7%9A%84%E6%98%AF)
> * [16\. 关于init函数，下面说法正确的是（）](#16-%E5%85%B3%E4%BA%8Einit%E5%87%BD%E6%95%B0%E4%B8%8B%E9%9D%A2%E8%AF%B4%E6%B3%95%E6%AD%A3%E7%A1%AE%E7%9A%84%E6%98%AF)
> * [17\. 下面这段代码输出什么以及原因？](#17-%E4%B8%8B%E9%9D%A2%E8%BF%99%E6%AE%B5%E4%BB%A3%E7%A0%81%E8%BE%93%E5%87%BA%E4%BB%80%E4%B9%88%E4%BB%A5%E5%8F%8A%E5%8E%9F%E5%9B%A0)
> * [18\. 下面这段代码能否编译通过？如果可以，输出什么？](#18-%E4%B8%8B%E9%9D%A2%E8%BF%99%E6%AE%B5%E4%BB%A3%E7%A0%81%E8%83%BD%E5%90%A6%E7%BC%96%E8%AF%91%E9%80%9A%E8%BF%87%E5%A6%82%E6%9E%9C%E5%8F%AF%E4%BB%A5%E8%BE%93%E5%87%BA%E4%BB%80%E4%B9%88)
> * [19\. 关于channel，下面语法正确的是（）](#19-%E5%85%B3%E4%BA%8Echannel%E4%B8%8B%E9%9D%A2%E8%AF%AD%E6%B3%95%E6%AD%A3%E7%A1%AE%E7%9A%84%E6%98%AF)
> * [20\. 下面这段代码输出什么？](#20-%E4%B8%8B%E9%9D%A2%E8%BF%99%E6%AE%B5%E4%BB%A3%E7%A0%81%E8%BE%93%E5%87%BA%E4%BB%80%E4%B9%88)
> * [21\. 下面这段代码输出什么？](#21-%E4%B8%8B%E9%9D%A2%E8%BF%99%E6%AE%B5%E4%BB%A3%E7%A0%81%E8%BE%93%E5%87%BA%E4%BB%80%E4%B9%88)
> * [22\. 下面这段代码输出什么？](#22-%E4%B8%8B%E9%9D%A2%E8%BF%99%E6%AE%B5%E4%BB%A3%E7%A0%81%E8%BE%93%E5%87%BA%E4%BB%80%E4%B9%88)
> * [23\. 下面这段代码输出什么？](#23-%E4%B8%8B%E9%9D%A2%E8%BF%99%E6%AE%B5%E4%BB%A3%E7%A0%81%E8%BE%93%E5%87%BA%E4%BB%80%E4%B9%88)
> * [24\. 下面这段代码输出什么？](#24-%E4%B8%8B%E9%9D%A2%E8%BF%99%E6%AE%B5%E4%BB%A3%E7%A0%81%E8%BE%93%E5%87%BA%E4%BB%80%E4%B9%88)
> * [25\. 关于 cap() 函数的适用类型，下面说法正确的是()](#25-%E5%85%B3%E4%BA%8E-cap-%E5%87%BD%E6%95%B0%E7%9A%84%E9%80%82%E7%94%A8%E7%B1%BB%E5%9E%8B%E4%B8%8B%E9%9D%A2%E8%AF%B4%E6%B3%95%E6%AD%A3%E7%A1%AE%E7%9A%84%E6%98%AF)
> * [26\. 下面这段代码输出什么？](#26-%E4%B8%8B%E9%9D%A2%E8%BF%99%E6%AE%B5%E4%BB%A3%E7%A0%81%E8%BE%93%E5%87%BA%E4%BB%80%E4%B9%88)
> * [27\. 下面这段代码输出什么？](#27-%E4%B8%8B%E9%9D%A2%E8%BF%99%E6%AE%B5%E4%BB%A3%E7%A0%81%E8%BE%93%E5%87%BA%E4%BB%80%E4%B9%88)
> * [28\. 下面属于关键字的是（）](#28-%E4%B8%8B%E9%9D%A2%E5%B1%9E%E4%BA%8E%E5%85%B3%E9%94%AE%E5%AD%97%E7%9A%84%E6%98%AF)
> * [29\. 下面这段代码输出什么？](#29-%E4%B8%8B%E9%9D%A2%E8%BF%99%E6%AE%B5%E4%BB%A3%E7%A0%81%E8%BE%93%E5%87%BA%E4%BB%80%E4%B9%88)
> * [30\. 下面这段代码输出什么？](#30-%E4%B8%8B%E9%9D%A2%E8%BF%99%E6%AE%B5%E4%BB%A3%E7%A0%81%E8%BE%93%E5%87%BA%E4%BB%80%E4%B9%88)
> * [31\. 31-60题](https://github.com/yqchilde/Golang-Interview/blob/master/31-60.md)



## 2. 下面这段代码输出什么，说明原因

```go
package main

import "fmt"

func main() {
    slice := []int{0, 1, 2, 3}
    m := make(map[int]*int)

    for key, val := range slice {
        m[key] = &val
    }

    for k, v := range m {
    fmt.Println(k, "->", *v)
    }
}
```

**答：输出内容为：**

```shell
// 注：key的顺序无法确定
0 -> 3
1 -> 3
2 -> 3
3 -> 3
```

**解析：**

`for range` 循环的时候会创建每个元素的副本，而不是每个元素的引用，所以 `m[key] = &val` 取的都是变量val的地址，所以最后 `map` 中的所有元素的值都是变量 `val` 的地址，因为最后 `val` 被赋值为3，所有输出的都是3。

## 3. 下面两段代码输出什么？

```go
// 1.
func main() {
    s := make([]int, 5)
    s = append(s, 1, 2, 3)
    fmt.Println(s)
}

// 2.
func main() {
    s := make([]int, 0)
    s = append(s, 1, 2, 3, 4)
    fmt.Println(s)
}
```

**答：输出内容为：**

```shell
// 1.
[0 0 0 0 0 1 2 3]

// 2.
[1 2 3 4]
```

**解析：**

使用 `append` 向 `slice` 中添加元素，第一题中slice容量为5，所以补5个0，第二题为0，所以不需要。

## 4. 下面这段代码有什么缺陷？

```go
func funcMui(x, y int) (sum int, error) {
    return x + y, nil
}
```

**答：第二个返回值没有命名**

**解析：**

在函数有多个返回值时，只要有一个返回值有命名，其他的也必须命名。如果有多个返回值必须加上括号();如果只有一个返回值且命名也需要加上括号()。这里的第一个返回值有命名sum，第二个没有命名，所以错误。

## 5. new() 与 make() 的区别

**解析：**

* `new(T)`  和 `make(T, args)`  是Go语言内建函数，用来分配内存，但适用的类型不用。
* `new(T)` 会为了 `T` 类型的新值分配已置零的内存空间，并返回地址（指针），即类型为 `*T` 的值。换句话说就是，返回一个指针，该指针指向新分配的、类型为 `T` 的零值。适用于值类型，如 `数组` 、 `结构体` 等。
* `make(T, args)` 返回初始化之后的T类型的值，也不是指针 `*T` ，是经过初始化之后的T的引用。 `make()` 只适用于 `slice` 、 `map` 和 `channel` 。

## 6. 下面这段代码能否通过编译，不能的话原因是什么；如果能，输出什么？

```go
func main() {
    list := new([]int)
    list = append(list, 1)
    fmt.Println(list)
}
```

**答：不能通过**

**解析：**

不能通过编译， `new([]int)` 之后的 `list` 是一个 `*int[]` 类型的指针，不能对指针执行 `append` 操作。可以使用 `make()` 初始化之后再用。同样的， `map` 和 `channel` 建议使用 `make()` 或字面量的方式初始化，不要用 `new` 。

## 7. 下面这段代码能否通过编译，不能的话原因是什么；如果可以，输出什么？

```go
func main() {
    s1 := []int{1, 2, 3}
    s2 := []int{4, 5}
    s1 = append(s1, s2)
    fmt.Println(s1)
}
```

**答：不能通过**

**解析：**

`append()` 的第二个参数不能直接使用 `slice` ，需使用 `...` 操作符，将一个切片追加到另一个切片上： `append(s1, s2...)` 。或者直接跟上元素，形如： `append(s1, 1, 2, 3)`  。

## 8. 下面这段代码能否通过编译，如果可以，输出什么？

```go
var (
    size := 1024
    max_size = size * 2
)

func main() {
    fmt.Println(size, max_size)
}
```

**答：不能通过**

**解析：**

这道题的主要知识点是变量的简短模式，形如：x := 100 。但这种声明方式有限制：

1. 必须使用显示初始化；
1. 不能提供数据类型，编译器会自动推导；
1. 只能在函数内部使用简短模式；

## 9. 下面这段代码能否通过编译？不能的话，原因是什么？如果通过，输出什么？

```go
func main() {
    sn1 := struct {
        age  int
        name string
 }{age: 11, name: "qq"}
 sn2 := struct {
        age  int
        name string
 }{age: 11, name: "11"}

    if sn1 == sn2 {
        fmt.Println("sn1 == sn2")
    }

    sm1 := struct {
        age int
        m   map[string]string
    }{age: 11, m: map[string]string{"a": "1"}}
    sm2 := struct {
        age int
        m   map[string]string
    }{age: 11, m: map[string]string{"a": "1"}}

    if sm1 == sm2 {
        fmt.Println("sm1 == sm2")
    }
}
```

**答：不能通过,invalid operation: sm1 == sm2**

**解析：**

考点是结构体的比较，有几个需要注意的地方：

1. 结构体只能比较是否相等，但是不能比较大小；
1. 想同类型的结构体才能进行比较，结构体是否相同不但与属性类型有关，还与属性顺序相关；
1. 如果struct的所有成员都可以比较，则该struct就可以通过==或!=进行比较是否相同，比较时逐个项进行比较，如果每一项都相等，则两个结构体才相等，否则不相等；

**那有什么是可以比较的呢？**

* 常见的有bool、数值型、字符、指针、数组等

**不能比较的有**

* slice、map、函数

## 10. 通过指针变量p访问其成员变量name,有哪几种方式？

* A. p.name
* B. (&p).name
* C. (*p).name
* D. p->name

**答：A C**

**解析：**

`&` 取址运算符， `*` 指针解引用

## 11. 下面这段代码能否通过编译？如果通过，输出什么？

```go
package main

import "fmt"

type MyInt1 int
type MyInt2 = int

func main() {
    var i int = 0
    var i1 MyInt1 = i
    var i2 MyInt2 = i
    fmt.Println(i1, i2)
}
```

**答：不能通过**

**解析：**

这道题考的是 `类型别名` 与 `类型定义` 的区别
第5行代码是基于类型 `int` 创建了新类型 `MyInt1` ，第6行代码是创建了int的类型别名 `MyInt2` ，注意类型别名的定义是 `=` 。所以，第10行代码相当于是将int类型的变量赋值给MyInt1类型的变量，Go是强类型语言，编译当然不通过；而MyInt2只是int的别名，本质上还是int，可以赋值。
第10行代码的赋值可以使用强制类型转换 `var i1 MyInt1 = MyInt1(i)`

## 12. 以下代码输出什么？

```go
func main() {
    a := []int{7, 8, 9}
    fmt.Printf("%+v\n", a)
    ap(a)
    fmt.Printf("%+v\n", a)
    app(a)
    fmt.Printf("%+v\n", a)
}

func ap(a []int) {
    a = append(a, 10)
}

func app(a []int) {
    a[0] = 1
}
```

**答：输出内容为：**

```shell
[7 8 9]
[7 8 9]
[1 8 9]
```

**解析：**

因为append导致底层数组重新分配内存了，append中的a这个alice的底层数组和外面不是一个，并没有改变外面的。

## 13. 关于字符串连接，下面语法正确的是？

* A. str := 'abc' + '123'
* B. str := "abc" + "123"
* C. str := '123' + "abc"
* D. fmt.Sprintf("abc%d", 123)

**答：B、D**

**解析：**

在Golang中字符串用双引号，字符用单引号
字符串连接除了以上两种连接方式，还有 `strings.Join()` 、 `buffer.WriteString()` 等

## 14. 下面这段代码能否编译通过？如果可以，输出什么？

```go
const (
    x = iota
    _
    y
    z = "zz"
    k
    p = iota
)

func main() {
    fmt.Println(x, y, z, k, p)
}
```

**答：编译通过，输出：**`**0 2 zz zz 5**`

**解析：**

iota初始值为0，所以x为0，_表示不赋值，但是iota是从上往下加1的，所以y是2，z是“zz”,k和上面一个同值也是“zz”,p是iota,从上0开始数他是5

## 15. 下面赋值正确的是（）

* A. var x = nil
* B. var x interface{} = nil
* C. var x string = nil
* D. var x error = nil

**答：B、D**

**解析：**

A错在没有写类型，C错在字符串的空值是 `""` 而不是nil。
知识点：nil只能赋值给指针、chan、func、interface、map、或slice、类型的变量。

## 16. 关于init函数，下面说法正确的是（）

* A. 一个包中，可以包含多个init函数；
* B. 程序编译时，先执行依赖包的init函数，再执行main包内的init函数；
* C. main包中，不能有init函数；
* D. init函数可以被其他函数调用；

**答：A、B**

**解析：**

1. init()函数是用于程序执行前做包的初始化的函数，比如初始化包里的变量等；
1. 一个包可以出现多个init()函数，一个源文件也可以包含多个init()函数；
1. 同一个包中多个init()函数的执行顺序没有明确的定义，但是不同包的init函数是根据包导入的依赖关系决定的；
1. init函数在代码中不能被显示调用、不能被引用（赋值给函数变量），否则出现编译失败；
1. 一个包被引用多次，如A import B，C import B，A import C，B被引用多次，但B包只会初始化一次；
1. 引入包，不可出现死循环。即A import B，B import A，这种情况下编译失败；

![image.png](https://cdn.nlark.com/yuque/0/2019/png/517869/1574040068413-cfcc17c2-6f8b-4c1c-b09b-d6d297c9f745.png#align=left&display=inline&height=330&name=image.png&originHeight=419&originWidth=948&size=156149&status=done&width=746)

## 17. 下面这段代码输出什么以及原因？

```go
func hello() []string {
    return nil
}

func main() {
    h := hello
    if h == nil {
        fmt.Println("nil")
    } else {
        fmt.Println("not nil")
    }
}
```

* A. nil
* B. not nil
* C. compilation error

**答：B**

**解析：**

这道题里面，是将 `hello()` 赋值给变量h，而不是函数的返回值，所以输出 `not nil`

## 18. 下面这段代码能否编译通过？如果可以，输出什么？

```go
func GetValue() int {
    return 1
}

func main() {
    i := GetValue()
    switch i.(type) {
    case int:
        fmt.Println("int")
    case string:
        fmt.Println("string")
    case interface{}:
        fmt.Println("interface")
    default:
        fmt.Println("unknown")
    }
}
```

**答：编译失败**

**解析：**

只有接口类型才能使用类型选择
类型选择的语法形如：i.(type)，其中i是接口，type是固定关键字，需要注意的是，只有接口类型才可以使用类型选择。

## 19. 关于channel，下面语法正确的是（）

* A. var ch chan int
* B. ch := make(chan int)
* C. <-ch
* D. ch<-

**答：A、B、C**

**解析：**

A、B都是申明channel；C读取channel；写channel是必须带上值，所以D错误。

## 20. 下面这段代码输出什么？

* A. 0
* B. 1
* C. Compilation error

```go
type person struct {
    name string
}

func main() {
    var m map[person]int
    p := person{"make"}
    fmt.Println(m[p])
}
```

**答：A**

**解析：**

打印一个map中不存在的值时，返回元素类型的零值。这个例子中，m的类型是map[person]int，因为m中 不存在p，所以打印int类型的零值，即0。

## 21. 下面这段代码输出什么？

* A. 18
* B. 5
* C. Compilation error

```go
func hello(num ...int) {
    num[0] = 18
}

func main() {
    i := []int{5, 6, 7}
    hello(i...)
    fmt.Println(i[0])
}
```

**答：18**

**解析：**

可变参数传递过去，改变了第一个值。

## 22. 下面这段代码输出什么？

```go
func main() {  
    a := 5
    b := 8.1
    fmt.Println(a + b)
}
```

* A. 13.1  
* B. 13
* C. compilation error  

**答：C**

**解析：**

`a` 的类型是`int` ，`b` 的类型是`float` ，两个不同类型的数值不能相加，编译报错。

## 23. 下面这段代码输出什么？

```go
package main

import (  
    "fmt"
)

func main() {  
    a := [5]int{1, 2, 3, 4, 5}
    t := a[3:4:4]
    fmt.Println(t[0])
}
```

* A. 3
* B. 4
* C. compilation error  

**答：B**

**解析：**

* 知识点：操作符 `[i, j]`。基于数组（切片）可以使用操作符 `[i, j]`创建新的切片，从索引 `i` ，到索引 `i` ，到索引 `j` 结束，截取已有数组（切片）的任意部分，返回新的切片，新切片的值包含原数组（切片）的 `i` 索引的值，但是不包含 `j` 索引的值。`i` 、`j` 都是可选的，`i` 如果省略，默认是0，`j` 如果省略，默认是原数组（切片）的长度。`i` 、`j` 都不能超过这个长度值。

* 假如底层数组的大小为 k，截取之后获得的切片的长度和容量的计算方法：**长度：j-i，容量：k-i**。

  截取操作符还可以有第三个参数，形如 [i,j,k]，第三个参数 k 用来限制新切片的容量，但不能超过原数组（切片）的底层数组大小。截取获得的切片的长度和容量分别是：**j-i、k-i**。

  所以例子中，切片 t 为 [4]，长度和容量都是 1。

## 24. 下面这段代码输出什么？

```go
func main() {
    a := [2]int{5, 6}
    b := [3]int{5, 6}
    if a == b {
        fmt.Println("equal")
    } else {
        fmt.Println("not equal")
    }
}
```

* A. compilation error  
* B. equal  
* C. not equal

**答：A**

**解析：**

Go中的数组是值类型，可比较，另外一方面，数组的长度也是数组类型的组成部分，所以 `a` 和 `b` 是不同的类型，是不能比较的，所以编译错误。

## 25. 关于 cap() 函数的适用类型，下面说法正确的是()

* A. array
* B. slice
* C. map
* D. channel

**答：A、B、D**

**解析：**

cap()，cap() 函数不适用 map

## 26. 下面这段代码输出什么？

```go
func main() {  
    var i interface{}
    if i == nil {
        fmt.Println("nil")
        return
    }
    fmt.Println("not nil")
}
```

* A. nil
* B. not nil
* C. compilation error  

**答：A**

**解析：**

当且仅当接口的动态值和动态类型都为 nil 时，接口类型值才为 nil

## 27. 下面这段代码输出什么？

```go
func main() {  
    s := make(map[string]int)
    delete(s, "h")
    fmt.Println(s["h"])
}
```

* A. runtime panic
* B. 0
* C. compilation error

**答：B**

**解析：**

删除 map 不存在的键值对时，不会报错，相当于没有任何作用；获取不存在的减值对时，返回值类型对应的零值，所以返回 0。



## 29. 下面这段代码输出什么？

```go
func main() {  
    i := -5
    j := +5
    fmt.Printf("%+d %+d", i, j)
}
```

* A. -5 +5
* B. +5 +5
* C. 0  0

**答：A**

**解析：**

`%d`表示输出十进制数字，`+`表示输出数值的符号。这里不表示取反。

## 30. 下面这段代码输出什么？

```go
type People struct{}

func (p *People) ShowA() {
    fmt.Println("showA")
    p.ShowB()
}
func (p *People) ShowB() {
    fmt.Println("showB")
}

type Teacher struct {
    People
}

func (t *Teacher) ShowB() {
    fmt.Println("teacher showB")
}

func main() {
    t := Teacher{}
    t.ShowB()
}
```

**答：teacher showB**

**解析：**

知识点：结构体嵌套。

在嵌套结构体中，People 称为内部类型，Teacher 称为外部类型；通过嵌套，内部类型的属性、方法，可以为外部类型所有，就好像是外部类型自己的一样。此外，外部类型还可以定义自己的属性和方法，甚至可以定义与内部相同的方法，这样内部类型的方法就会被“屏蔽”。这个例子中的 ShowB() 就是同名方法。
