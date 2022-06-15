To `split` `changes` made to the same file between different `commits`, in the Commit tool window `Alt+0` click `Diff`. Select the `checkbox` next to each `chunk` of modified or new code that you want to commit and click `Commit`.

要在不同的`提交`之间`分割`对同一文件所做的`更改`，在提交工具窗口`Alt+0`单击`Diff`，选择每个要提交的`修改`或`新代码`块旁边的`复选框`，然后单击`Commit`。

Unselected changes will stay in the current `changelist`.

未选中的更改将保留在当前`更改列表`中。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.17ohi703nojg.png)

The `Type-Matching code completion` analyzes the `expected type` of the whole `expression` and helps to find `methods` and `variables` that are applicable in the current context.

`类型匹配代码补全`分析整个`表达式`的`预期类型`，并帮助查找适用于当前上下文中的`方法`和`变量`。

It works after the `return keyword`, in an `assignment`, in the `argument list` of a `method call`, and `other places`.

它可以在`return关键字`之后、在`赋值`中、在`方法调用`的`参数列表`中以及`其他地方`工作。

Press `Ctrl+Shift+空格` (Code | Code Completion | Type-Matching) to get the completion `list filtered`.

按`Ctrl + Shift +空格`(代码|代码完成|类型匹配的)来完成`过滤列表`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.531f9rmj86o0.png)

Invoking `Type-Matching code completion` (`Ctrl+Shift+空格`) twice when a `collection type` is expected will search for `arrays` with the same component type and suggest converting them using the `Arrays.asList()` call.

调用`类型匹配的代码完成`(`Ctrl + Shift +空格`)两次当一个`集合类型`预计将搜索`数组`相同的组件类型,建议把他们使用`arrays.aslist()`调用。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4b2fky8p7560.png)

Invoking `Type-Matching code completion` (`Ctrl+Shift+空格`) `twice` when an `array type` is expected will search for collections with the same component type and suggest converting them using the `toArray()` call.

调用`类型匹配的代码完成`(`Ctrl + Shift +空格`)`两次`当`数组类型`预计将搜索集合使用相同的组件类型,建议把他们使用`toArray()`调用。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1xiobpw60zc0.png)

When using `code completion`, you can accept the `current selection` in the suggestions list with `Ctrl+Shift+Enter`.

当使用`代码完成`时，您可以使用`Ctrl+Shift+Enter`接受建议列表中的`当前选择`。

IntelliJ IDEA will not only insert the `selected string`, but also turn the `current code construct` into a `syntactically correct one` (balance parentheses, add missing braces and semicolons, and so on).

IntelliJ IDEA不仅会插入`选定的字符串`，还会将当前的`代码构造`转换为语法`正确的代码构造`(平衡括号，添加缺少的大括号和分号，等等)。

--------------------------------------------------------------------------

You can generate `boilerplate` code such as getters and setters and implement interface methods using `code completion`.

您可以生成诸如getter和setter之类的`样板代码`，并使用代码完成来实现接口方法。

IntelliJ IDEA can generate `getter and setter methods` for `fields` in your `class`.

IntelliJ IDEA可以为`类`中的`字段`生成`getter和setter方法`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6bylhq1cmi00.png)

With the caret inside the `class`, press `Alt+Insert` (Code | Generate ).

在`类`中使用插入符号，按`Alt+Insert`(代码| Generate)。

Just start typing the would-be `name of a method`, for example, `gn` to generate `getName()` or `ct` to implement `compareTo()`.

只需输入`方法的名称`，例如，输入`gn`来生成`getName()`或`ct`来实现`compareTo()`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1rpmx2japhts.png)

If a `method signature` has changed, IntelliJ IDEA highlights the documentation `comment tags` that `ran out of sync` and suggests a `quick-fix`:

如果一个`方法签名`发生了变化，IntelliJ IDEA会突出显示`文档注释标签`的`不同步`，并建议一个`快速修复方法`:

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.617qcl6xy9c0.png)

To add data from the `CSV file`, drag the `file` to the `tables node` of a data source or to the table.

若要从`CSV文件`中添加数据，请将`文件`拖到数据源的`表节点`或表中。

You can view `CSV` and `TSV` files as `text` or as a `table`.

您可以以`文本`或`表`的形式查看`CSV`和`TSV`文件。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.594b5cn5xv80.png)

You can access `Spring bean documentation` directly from the `editor`.

您可以直接从`编辑器`访问`Spring bean文档`。

Place the `caret` at the `bean’s definition` in the `Spring configuration file`, press `Alt+F1`, and select `Spring Bean`.

在`Spring配置文件`中的`bean定义`处放置插入`符号`，按`Alt+F1`，并选择`Spring bean`。

The `Spring tool window` opens showing you all `available information`.

`Spring工具窗口`将打开，显示所有`可用信息`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1g9rf5th9ecg.png)

You can inject `Spring entities`, such as `bean names` and `resource paths`.

您可以注入`Spring实体`，比如`bean名称`和`资源路径`。

Press `Alt+Enter` and select `Inject language or reference` | `Spring Bean Name`.

按“`Alt+Enter`”，选择“`Inject language or reference` | `Spring Bean Name`”。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5beq2dky2ys0.png)

When some words are missing in the `pre-defined dictionaries`, you can create your own.

当一些单词在`预定义的词典`中缺失时，您可以创建自己的词典。

A `custom dictionary` is a `.dic` text file containing each word on a new line.

`自定义字典`是一个`.dic`文本文件，在新行中包含每个单词。

All you have to do is add the directories where your dictionaries are stored in `Settings/Preferences` | `Editor` | `Proofreading` | `Spelling`.

所有你需要做的是添加目录，你的字典存储在`设置/首选项`|`编辑`|`校对`|`拼写`。

Database Plugin warns you if you use the equals sign in the clause where IS NULL seems more appropriate.

Database Plugin会警告你，如果你在子句中使用等号，那么IS NULL似乎更合适。

To `fix` this, press `Alt+Enter` and select `Use IS NULL` operator.

要`解决`这个问题，请按`Alt+Enter`并选择`Use IS NULL`操作符。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.7769nma91ek0.png)

If you see no `objects` below the `schema level`, cannot find changes in `objects`, experience `broken tables` or any other `visualization problems`, try to `clear the cache` and `sync the data source` again.

如果您没有看到`模式级别`以下的`对象`，无法找到`对象`中的更改，遇到`表损坏`或任何其他`可视化问题`，请尝试`清除缓存`并再次`同步数据源`。

To `clear the cache`, `right-click` a `data source` and navigate to `Database Tools` | `Forget Cached Schemas`.

要`清除缓存`，`右键`单击`数据源`并导航到`Database Tools` | `Forget Cached Schemas`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3ft6pb06pxy0.png)

If you want to `log program state` `during debugging`, use `non-suspending breakpoints`.

如果要在`调试期间``记录程序状态`，请使用`非挂起断点`。

Select the `expression` that you want to `log`, hold `Shift`, and click the `gutter` at the line where the expression should be logged.

选择要`记录`的`表达式`，按住`Shift键`，然后`单击`应该记录表达式的行。

In the example, `sent.size()` will be `logged` upon reaching line 24.

在本例中，当到达第24行时，`sent.size()`将被`记录`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5j7jk8peh700.png)

Press `Ctrl+E` (View | Recent Files) to view the list of `recently opened files`.

按`Ctrl+E` (View | Recent Files)查看`最近打开的文件`列表。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.74c9cbzkr8c0.png)

You can also `bring up` the results of the `recently performed` `usage searches`.

您还可以`调出``最近执行`的`使用情况搜索`的结果。

For this, go to Edit | Find | `Recent Find Usages` or select `Recent Find Usages` from the `context menu` of the Find tool window:

为此，去`编辑`|`查找`|`最近查找用法` OR 从查找工具窗口的`上下文菜单`中选择`最近查找用法`: 

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.728xn6i615g0.png)

Enter "/" in the search field of the `Search Everywhere` (`Shift twice`) window to search for a list of `settings`, their `options`, and `plugins`.

在`search Everywhere` (`Shift两次`)窗口的搜索字段中输入"`/`"，搜索`设置`、`选项`和`插件`列表。

You can also search for the `URL mappings` entering "`/`" before the part of the `URL mapping` you are searching for.

您还可以在正在搜索的`URL映射`部分之前输入“`/`”来搜索`URL映射`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1hb6epq1fbgg.png)

Press `Shift twice` to open the `Search Everywhere` popup.

按`Shift键两次`打开`Search Everywhere`弹出窗口。

It lets you find any element of your `source code`, `a file`, `an action`, a `UI element`, or even a `Git commit`.

它允许您查找`源代码`、`文件`、`操作`、`UI元素`甚至`Git提交`中的任何元素。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1frg3tm63q68.png)

Use camel case in the `Search Everywhere popup` (`double Shift`) to filter the `list` of results when searching for a `class`, `file`, or `symbol`.

在`Search Everywhere弹出框`中使用驼峰大小写(`双Shift`)来过滤搜索`类`、`文件`或`符号`时的结果列表。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.gwfb1yvi040.png)

Press `Shift twice` to search for `files`, `actions`, `symbols`, `UI elements`, `Git branches` and `comments` across your project.

按`Shift两次`，搜索`文件`，`动作`，`符号`，`UI元素`，`Git分支`和`注释`。

Pressing `double Shift` again, will extend the search to `non-project items`.

再次按下`双Shift`，将扩展搜索到`非项目项目`。

Use `tabs` or direct shortcuts `Ctrl+N` for `classes`, `Ctrl+Shift+N` for `files`, `Ctrl+Alt+Shift+N` for `symbols`, and `Ctrl+Shift+A` for `actions` to narrow your search results.

使用`标签`或直接快捷键`Ctrl+N`用于`类`，`Ctrl+Shift+N`用于`文件`，`Ctrl+Alt+Shift+N`用于`符号`，`Ctrl+Shift+A`用于`操作`，以缩小搜索结果。

------------------------------------------------------------------------------

You can copy text from the `editor` as `rich text` to paste it into any other editor that recognizes `RTF`.

您可以将`编辑器`中的文本复制为`富文本`，将其粘贴到识别`RTF`的任何其他编辑器中。

Make sure the Copy as `rich text` `checkbox` is selected on the Editor | General page of the `Settings`/`Preferences` dialog `Ctrl+Alt+S`.

确保在`设置`/`首选项`对话框的编辑器|常规页面上选择了“复制为`富文本`”`复选框`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.325gg7q41ya0.png)

Use `Emmet` to speed up the `HTML`, `XML`, or `CSS` development.

使用`Emmet`来加速`HTML`、`XML`或`CSS`的开发。

In the `Settings`/`Preferences` dialog `Ctrl+Alt+S`, go to `Editor` | `Emmet` and select the `Enable Emmet checkbox` on the `Emmet | HTML`, `Emmet | CSS`, or `Emmet | JSX` page.

在`设置`/`首选项`对话框中`Ctrl+Alt+S`，进入`编辑器`| `Emmet`，并选择`启用Emmet`在`Emmet | HTML`, `Emmet | CSS`或`Emmet | JSX`页面上的复选框。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5rymm5rfjug0.png)

`Git annotations` show detailed information on the `origin` of each line of code (`right-click` the gutter and select `Annotate with Git Blame` ).

`Git注释`显示了每一行代码的`起源`的详细信息(`右键单击`gutter并选择`Annotate with Git Blame`)。

`Right-click` an `annotation` and choose `Show Diff` to review the `differences` between the `current` and the `previous` version of the file.

`右键单击``注释`并选择`Show Diff`来查看文件的`当前`版本和`以前`版本之间的`差异`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3kqpsynqv3s0.png)

Your IDE has a `differences viewer` for `database` objects.

您的IDE有一个`数据库`对象的`差异查看器`。

For example, in the `Database tool window`, you can select `two tables`, `right-click` the selection, and choose `Compare`.

例如，在`Database工具窗口`中，您可以选择`两个表`，`右键`单击所选内容，然后选择`Compare`。

The diff between these tables will be available in the separate dialog of the `differences viewer`.

这些表之间的差异将在`差异查看器`的单独对话框中可用。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.49cp1w0889k0.png)

If you want to `process data` from a `Collection` using a `Stream`, start `typing the Stream method` right on a `Collection instance`.

如果您想使用`Stream`处理来自`集合`的`数据`，请在`集合实例`上开始`键入Stream方法`。

IntelliJ IDEA will get the `Stream instance` for you.

IntelliJ IDEA将为您获取`Stream实例`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.62erdvhx2ys0.png)

One can easily convert any `Java class` to the `Kotlin` one with the same `semantics`.

可以很容易地将任何`Java类`转换为具有相同`语义`的`Kotlin类`。

To do that, just choose `Code` | Convert `Java File` to` Kotlin File` on the main menu:

要做到这一点，只需要在主菜单中选择 `Code` | Convert `Java File` To `Kotlin File`:

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.7c5rngo5t440.png)

You do not need to open a file in the editor to change its `line separator` style.

不需要在编辑器中打开文件来更改其行分隔符样式。

Use the `Project tool window` instead: select one or more files or folders, select `File` | `File Properties`| `Line Separators` from the main menu, and then choose the desired l`ine ending style`.

使用`项目工具窗口`:选择一个或多个文件或文件夹，从主菜单中选择`文件`|`文件属性`|`行分隔符`，然后选择所需的`行结束样式`。

For a directory, new `line separator` applies `recursively`.

对于目录，新`行分隔符``递归`应用。

-----------------------------------------------------------

`Database consoles` are `SQL files` that are already attached to a `data source`.

`数据库控制台`是已经附加到`数据源`的`SQL文件`。

When you create a `data source`, a `query console` is created `automatically`.

当您创建`数据源`时，会`自动`创建一个`查询控制台`。

To open a `console`, right-click the `data source` and select New | `Query Console`.
要打开`控制台`，右键单击`数据源`并选择New | `Query console`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2llng1hayxa0.png)

The Code | `Move Statement Up/Down actions` are useful for `reorganizing code lines`, for example for bringing a `variable declaration` closer to the variable usage.

------------------------------------------------------------------------------

代码| `Move Statement Up/Down操作`对于`重新组织代码行`很有用，例如使`变量声明`更接近于变量的使用。

Select a `code fragment` and press `Ctrl+Shift+向上箭头` or `Ctrl+Shift+向下箭头`.

选择一个`代码片段`,按`Ctrl + Shift +向上箭头`或`Ctrl + Shift +向下箭头`。

When nothing is selected in the editor, the line at the `caret position` will be moved.

当编辑器中没有选择任何内容时，`插入符号位置`的行将被移动。

------------------------------------------------------------------------------

Press `Shift+Enter` to execute the current cell in a Jupyter notebook and select the next one: `Shift+Enter`.

按`Shift+Enter键`在木星笔记本中执行当前单元格，并选择下一个:`Shift+Enter`。

------------------------------------------------------------------------------

You can configure the set of `widgets` that appear in the `status bar`.

您可以配置出现在`状态栏`中的一组`小部件`。

To do this, `right-click` a `widget` and select the items you want to see.

为此，`右键`单击`小部件`并选择您想要看到的项目。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6mv2ol8rlaw0.png)

`Speed search` is available in all `tree views`: start typing and you'll quickly `locate` the necessary item.

`速度搜索`在所有`树视图`可用: 开始键入，你会很快`找到`必要的项目。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5wdueh7nrtw0.png)

Press `Ctrl+Alt+U` to open a `UML class diagram` in a popup window.

按`Ctrl+Alt+U`在弹出窗口中打开`UML类图`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4gxxlu0lk0e0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.7inpv9zjjow0.png)

If your project is under `version control`, you can build a `UML diagram` that reflects your local changes and visualizes relationships between the modified components.

如果您的项目处于`版本控制`之下，您可以构建一个`UML图`来反映您的`本地更改`并可视化修改组件之间的关系。

Press `Ctrl+Alt+Shift+D` and select the `necessary changelist` to build the `diagram`.

按`Ctrl+Alt+Shift+D`并选择`必要的变更列表`来构建`图表`。

`Double-click` a `node` on the diagram to see the change in the `diff dialog`.

`双击`关系图上的一个`节点`，在`diff对话框`中查看更改。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5csjophrzgw0.png)

To protect your database from `accidental modifications`, you can turn on `read-only mode` for a connection.

为了保护数据库不受`意外修改`的影响，可以为连接打开`只读模式`。

To enable `read-only mode` for a connection, click `File` | `Data sources` and select the necessary data source in the `Data Sources` list.

要为连接启用`只读模式`，请单击`File` | `Data sources`并在`Data sources`列表中选择必要的数据源。

On the `Options tab`, select the `Read-only checkbox`.

在`“选项”选项卡`上，选择`“只读”复选框`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.52139x1qqog0.png)

You can view `multiple files` `side by side` in the `editor`.

您可以在`编辑器`中`并排`查看`多个文件`。

`Right-click` the desired editor tab and select how you want to `split the editor` (`Split Right` or `Split Down` ).

`右键单击`所需的`编辑器选项`卡，并选择要如何`拆分编辑器`(`右拆分`或`向下拆分`)。

Alternatively, `drag a tab` to any area of the editor to activate `split-screen mode`.
或者，`拖拽一个标签`到编辑器的任何区域来激活`分屏模式`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4o8jbyvityw0.png)

To navigate to the implementations of an `abstract method`, position the `caret` at its `usage` or its name in the `declaration` and press `Ctrl+Alt+B`.

要导航到`抽象方法`的实现，请将`插入符号`定位在其`用法`或`声明`中的名称处，并按`Ctrl+Alt+B`。

------------------------------------------------------------------------------

To scroll a file `horizontally`, turn the `mouse wheel` while keeping `Shift pressed`.

要`水平`滚动文件，请转动`鼠标滚轮`，同时`按住Shift键`。

------------------------------------------------------------------------------

To select `multiple text fragments` and modify them, press and hold `Shift+Alt` (on Windows and Linux) and `drag your mouse` across the text:

要选择`多个文本片段`并修改它们，按住`Shift+Alt`(在Windows和Linux上)，并在文本上`拖动鼠标`:

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4ndbnsza1r40.png)

Press `Ctrl twice` to get quick access to numerous actions, such as `opening a project`, launching a `run/debug configuration`, running a `command-line utility`, and so on.

按`两次Ctrl`可以快速访问许多操作，例如`打开项目`、`启动运行/调试配置`、运行`命令行实用程序`等等。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.41hwnu5edlm0.png)

Press `Ctrl+Y` in the editor to `delete` the `whole line` at the caret.

在编辑器中按`Ctrl+Y` `删除`插入符处的`整行`。

------------------------------------------------------------------------------

To create a `backup copy` of a `table`, `drag` the table to the `tables node` of the `same data source` in the `database tree view`.

若要创建`表`的`备份副本`，请将表`拖`到`数据库树视图`中`相同数据源`的`表节点`。

Type a `new name` and click Run.

输入`新名称`并单击Run。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1hdxnry28lmo.png)

You can move `method parameters` in both declaration and invocation with `Ctrl+Alt+Shift+向左箭头` and `Ctrl+Alt+Shift+向右箭头`.

你可以移动在声明和调用`方法参数`按`Ctrl + Alt + Shift +向左箭头`并按`Ctrl + Alt + Shift +向右箭头`。

Moreover, you can propagate such move in a method declaration to the method invocation: press `Alt+Enter` after the move and choose `Update usages to reflect signature change`.

此外，您可以在方法声明中传播这种移动到方法调用:在移动之后按`Alt+Enter`并选择`Update usage来反映签名的变化`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4qaabddhdii0.png)

To `compare` two `.jar files` or even files inside a `.jar archive`, select them in the `Project tool window` and press `Ctrl+D`.

要`比较`两个`.jar文件`，甚至是一个`.jar存档`中的文件，请在`Project工具窗口`中选择它们并按`Ctrl+D`。

The `Compare Archives feature` is integrated with the `Java bytecode decompiler` and allows you to see what exactly has changed between two different versions of a library. 

`Compare Archives特性`与`Java字节码反编译器`集成在一起，允许您查看一个库的两个不同版本之间到底发生了什么变化。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4ykj839wu7k0.png)

You can create `code constructs` using `statement completion`.

可以使用`语句完成`创建`代码构造`。

Start typing a `method declaration`, a `method call` or a `statement` such as `if`, `do -while`, `try -catch`, or `return`.

开始键入`方法声明`、`方法调用`或`if`、`do -while`、`try -catch`或`return`等`语句`。

Press `Ctrl+Shift+Enter` to complete the `statement` into a `syntactically correct construct`. 

按`Ctrl+Shift+Enter`将`语句`完成成`语法正确的结构`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2neu1fhx2zk0.png)

To `preview code` without actually `scrolling` to it, `hover` your mouse pointer over a `warning`, `error stripe`, or just some section of source code on the `scrollbar`, and you will see a `lens`: 

要`预览代码`而不实际`滚动`到它，将鼠标指针`悬停`在`警告`，`错误条`，或`滚动条`上的源代码的一些部分，你会看到一个`镜头`:

To disable the `lens`, `right-click` the `code analysis marker` at the top of the `scrollbar` and clear the `Show code lens on scrollbar hover` checkbox.

要禁用镜头，`右键`单击`滚动条`顶部的`代码分析标记`，并清除“`在滚动条上悬停显示代码镜头`”复选框。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.5ul1zixznkg0.png)

You can jump to a file located in a `deeply nested directory` by pressing` Ctrl+Shift+N` and typing several characters of the enclosing directories and `filename`.

通过按`Ctrl+Shift+N`并键入外围目录和文件名的几个字符，可以跳转到位于`深度嵌套目录`中的`文件`。

Use either a `slash` or a `backslash` as a `delimiter`.

使用`斜杠`或`反斜杠`作为`分隔符`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2wwi5vfi4gc0.png)

To view all `methods` of the implemented interfaces in a `class`, place the caret at the implements keyword in the `class declaration` and press `Ctrl+Shift+F7`:

要查看`类`中已实现接口的所有`方法`，请在`类`声明中的`implements关键字`处插入插入符号，并按`Ctrl+Shift+F7`:

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6zbyzm42v4g0.png)

Press `Ctrl+Shift+F7` (Edit | Find Usages | `Highlight` Usages in File) to quickly highlight usages of a `certain variable` in the current file.

按`Ctrl+Shift+F7`(编辑|查找用法|`突出显示`文件中的用法)可以快速突出当前文件中`某个变量`的用法。

Press `F3` and `Shift+F3` to `navigate through` the `highlighted usages`.

按`F3`和`Shift+F3`来浏览`突出显示`的用法。

Press `Esc` to remove `highlighting`.

按`Esc`删除`突出显示`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4yo9x8v67cg0.png)

You can view all `statements` within the method where certain `exceptions` can be `thrown`.

您可以查看方法中可以`抛出`某些`异常`的所有`语句`。

Place the `caret` at the `throws` statement and press `Ctrl+Shift+F7`.

在`抛出`语句处放置`插入符号`并按`Ctrl+Shift+F7`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.aqhjkwyxcug.png)

To view all `exit points` of a `method`, place the caret at one of them, for example the `return statement`, and press `Ctrl+Shift+F7`:

要查看一个`方法`的所有`退出点`，将插入符号放在其中一个`退出点`，例如`返回语句`，并按`Ctrl+Shift+F7`:

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2zfs3qn57ns0.png)

Press `Alt+F7` to quickly `locate` all occurrences of `code referencing the symbol` at the `caret`, no matter if the symbol is a part of a `class`, `method`, `field`, `parameter`, or another statement.

按`Alt+F7`可以快速`定位`在`插入符号`处`引用该符号的所有代码`，无论该符号是`类`、`方法`、`字段`、`参数`还是其他语句的一部分。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.7jp4utvwao80.png)

------------------------------------------------------------------------------

You can easily override methods of the base class by pressing Ctrl+O (Code | Override Methods ).

您可以通过按Ctrl+O(代码|覆盖方法)轻松覆盖基类的方法。

------------------------------------------------------------------------------

To implement methods of the interfaces (or of the `abstract base class`) that the current class implements, press `Ctrl+I` (Code | Implement Methods ).

要实现当前类实现的接口(或`抽象基类`)的方法，请按`Ctrl+I`(代码|实现方法)。

------------------------------------------------------------------------------

Press `F2` or `Shift+F2` to jump to the next or previous `error` respectively in the current file.

按`F2`或`Shift+F2`键分别跳转到当前文件中的下一个或上一个`错误`。

------------------------------------------------------------------------------

Press `Ctrl+F` to display the `search pane`.

按`Ctrl+F`显示`搜索`窗格。

Press `Ctrl+R` to `add another field` where you can type the `replace` string.

按`Ctrl+R`添加另一个`字段`，您可以在其中键入`替换`字符串。

In the Find in Files dialog, you can switch to `replace` by pressing `Ctrl+Shift+R`.

在“在文件中查找”对话框中，可以通过按`Ctrl+Shift+R`切换到`替换`。

Similarly, press `Ctrl+Shift+F` to hide the Replace with field and switch to `regular search`.

类似地，按`Ctrl+Shift+F`隐藏Replace with字段并切换到`常规搜索`。

------------------------------------------------------------------------------

Refer to a `non-existing target tag` in your `Ant build file`, and IntelliJ IDEA will suggest you to `automatically create the corresponding tag`.

在`Ant构建文件`中引用一个`不存在的目标标记`，IntelliJ IDEA会建议您`自动创建相应的标记`。

This intention action will not even make you `change your current editing location`.

这个意图动作甚至不会让您`更改当前的编辑位置`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1012yuesf7io.png)

To verify that your `regular expression` is correct, place the caret within the expression you want to check, press `Alt+Enter`, and select `Check RegExp`.

要验证`正则表达式`是否正确，请在`要检查的表达式`中放置`插入符号`，按`Alt+Enter`，并选择`check RegExp`。

In the popup, type a `sample string` that should match your `regular expression`.

在弹出窗口中，键入一个应该与`正则表达式`匹配的`示例字符串`。

The `“√” icon` shows that the `match occurred`.

图标“`√`”显示`匹配发生`。

The `“!”  icon` shows that there is `no match` or your expression contains a mistake.

图标“`!`”显示`没有匹配`或表达式包含错误。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2an09ro4qbvo.png)

To quickly find and run an `inspection`, press `Ctrl+Alt+Shift+I` and start typing the name of the `inspection` or its group.

要快速查找并运行`巡检`，请按`Ctrl+Alt+Shift+I`并开始键入`巡检`或其组的名称。

Choose an `inspection` from the `suggestion list` and specify the scope.

从`建议列表`中选择`检查`并指定范围。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.21w0rgjuaylc.png)

To preview a `referenced image` in a `popup` instead of in a `separate editor tab`, press `Ctrl+Shift+I`.

要在`弹出窗口`预览引用的图像，而不是在`单独的编辑器选项卡`中，按`Ctrl+Shift+I`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4un8koa8n2i0.png)

You can invoke the `Quick Definition Viewer` (`Ctrl+Shift+I`) for items in `code completion lists` and the `class`, `file`, or `symbol navigation commands`.

对于`代码完成列表`中的项目和`类`、`文件`或`符号导航命令`，您可以调用`Quick Definition Viewer` (`Ctrl+Shift+I`)。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2kxmjcyboci0.png)

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.63uy2tw1crg0.png)

To search for a `code pattern` or a `grammatical construct`, `select Edit | Find | Search Structurally`.

要搜索`代码模式`或`语法结构`，选择`Edit | Find | search structure`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.24n2qgr0y68w.png)

You can view the list of all usages of a class, method or variable across the whole project, and quickly navigate to the selected item.

您可以查看整个项目中一个`类`、`方法`或`变量`的`所有用法的列表`，并`快速导航到所选项`。

Place the caret at a `symbol` and press `Ctrl+Alt+F7` (`Edit | Find Usages | Show Usages` ).

把插入符号和按`Ctrl+Alt+F7`(`编辑|查找用法|显示用法`)。

To jump to a `usage`, select it from the list and press `Enter`.

要跳转到一个`用法`，从列表中选择它并按`Enter`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.268haprywov4.png)

To quickly select the `currently edited element` (a class, file, method, or field) in `another view`, press Alt+F1 or call `Navigate` | `Select In`.

要在`另一个视图`中快速选择`当前编辑的元素`(`类`、`文件`、`方法`或`字段`)，请按`Alt+F1`或调用`导航`| `select in`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.62aq1w3neco0.png)

You can inject `SQL` into a string literal (`Alt+Enter` | Inject language or reference | &lt;SQL dialect&gt;) and then use coding assistance for SQL.

你可以将`SQL`注入到`字符串`中(`Alt+Enter` | inject language or reference | &lt;SQL方言&gt;)，然后使用SQL的编码辅助。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.20ainll7i4cg.png)

To quickly see the documentation for a class or method at the `caret`, press `Ctrl+Q` (`View | Quick Documentation` ).

要在`插入符号`处快速查看类或方法的文档，请按`Ctrl+Q`(查`看|快速文档`)。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3hay40drhrk0.png)

To quickly wrap a `code block` in useful `constructs`, select it in the editor and press `Ctrl+Alt+T` (Code | `Surround With` ).

要用有用的`构造`快速包装`代码块`，请在编辑器中选择它并按`Ctrl+Alt+T`(代码| `Surround With`)。

The list of available options or wrappers is `context-sensitive` and depends on the language.

可用选项或包装器的列表是`上下文敏感`的，并取决于语言。

For example, you can surround `html blocks` with tags, and so on.

例如，可以用标记包围`html块`，等等。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.759qjifgykg0.png)

You can easily `rename` your classes, methods, and variables with automatic correction of all places where they are used.

您可以轻松地`重命名`类、方法和变量，并自动修正它们使用的所有位置。

Position the `caret` at the symbol you want to `rename`, and press Shift+F6 (`Refactor | Rename` ).

将插入符号放置在你想`重命名`的符号处，并按`Shift+F6`(`重构|重命名`)。

Type the new name and press Enter.

输入新的名称并按Enter。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4lthtaxwe9w0.png)

IntelliJ IDEA makes it easy to specify colors in `CSS files`.

IntelliJ IDEA使得在`CSS文件`中指定颜色变得很容易。

The `color properties` have icons of the corresponding color in the editor gutter.

`颜色属性`在编辑器的gutter中具有相应颜色的图标。

Click the icon to choose the desired color using the `color picker`.

单击图标，使用`颜色选择器`选择所需的颜色。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3y15isi7bem0.png)

You can use colors to distinguish between your `data sources` and their elements.

您可以使用`颜色`来区分`数据源`及其元素。

To set a color to a `data source` or its object, right-click the element in the `Database tool window` (View | Tool Windows | Database) and select `Color Settings`.

要为`数据源`或其对象设置`颜色`，右键单击`数据库工具窗口`中的元素(查看|工具窗口|数据库)并选择`颜色设置`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.4uz1xsoaqao0.png)

Double-click an `SQL file` to open it in the IDE.

双击`SQL文件`在IDE中打开它。

To run a query from this file, call `intention actions` (Alt+Enter for Windows and Linux) and select Run query in console.

要从这个文件运行`查询`，调用`意图操作`(Windows和Linux的Alt+Enter)并选择在`控制台`运行`查询`。

In the `Sessions list`, select existed `console` or create a new one.

在`Sessions列表`中，选择已存在的`控制台`或创建一个新的`控制台。`

Note that a new `query console` means a `new connection` to a data source.

注意，新的`查询控制台`意味着到数据源的`新连接`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.21fa5vc0e200.png)

Press `Ctrl+Shift+E` to get a list of recently `viewed or changed` code fragments.

按`Ctrl+Shift+E`获得最近`查看或更改`的代码片段的列表。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2ynht5rqe9a.png)

Use text patterns in `Search Everywhere (double Shift)` when searching for a class, file, or symbol.

在搜索类、文件或符号时，在`Search Everywhere(双Shift)`中使用文本模式。

Use `*` and `space`:

使用`*`和`空格`:

`*` stands for any number of `arbitrary characters`.

`*` 表示任意数量的`任意字符`。

`space` marks the `end` of a pattern.

`空格`表示模式的`结束`。

The preceding string is considered not just a prefix but a whole pattern.

前面的字符串不只是一个前缀，而是整个模式。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6ejubrzahto0.png)

Use shortcuts to comment and uncomment lines and blocks of code:

使用快捷键注释和取消注释行和代码块:

`Ctrl+/`: for single line comments (//...)

`Ctrl+/`:单行注释(//…)

`Ctrl+Shift+/`: for block comments (/*...*/)

`Ctrl+Shift+/`:用于块注释(/*…*/)

------------------------------------------------------------------------

The `Extract Variable` refactoring wraps a `selected expression` into a `variable`.

提取`变量重构`将选定的表达式包装到`变量`中。

It adds a new variable declaration and uses the expression as an `initializer`.

它添加了一个新的变量声明，并使用表达式作为`初始化式`。

To invoke this `refactoring`, select the `expression` and press `Ctrl+Alt+V` (`Refactor | Extract/Introduce | Variable` ):

要调用这个`重构`，`选择表达式`并`按Ctrl+Alt+V`(`重构|提取/引入|变量`):

This will result in the following:

这将导致以下结果:

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.205asay8l4hs.png)

You can use the Extract Variable refactoring on incomplete statements.

您可以在不完整的语句上使用Extract Variable重构。

Press `Ctrl+Alt+V` and choose an expression.

按“`Ctrl+Alt+V`”选择一个表达式。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.2tf27k8a8ag0.png)

You can edit a `language injection` in your code using a `dedicated editor`.

您可以使用`专用编辑器`在代码中编辑`语言注入`。

For example, to edit a regular expression, start typing it, press `Alt+Enter` and choose `Edit RegExp Fragment`.

例如，要编辑`正则表达式`，开始键入它，按`Alt+Enter`并选择`edit RegExp Fragment`。

The `regular expression` opens in a `separate tab` in the editor, where you can type `backslashes` as is.

`正则表达式`在编辑器中的一个`单独选项卡`中打开，您可以按原样输入`反斜杠`。

All changes are synchronized with the original `regular expression`, and `escape characters` are presented automatically.

所有更改都与原始`正则表达式`同步，并自动显示`转义字符`。

When ready, press `Esc` to close the `regular expression` editor.

准备好后，按`Esc`关闭`正则表达式`编辑器。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1gt5q7bwl9z4.png)

Use `live templates` to insert `frequent code constructs`.

使用`活动模板`插入`频繁的代码构造`。

For example, type `psvm` and press `Tab` to insert `the main() method declaration` template, then type `sout` to insert a `print statement`.

例如，键入`psvm`并按`Tab键`插入`main()方法声明`模板，然后键入`sout`插入`打印语句`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.ytnr4dpp8gw.png)

Use the `Edit | Copy | Copy Path/Reference` action to insert a reference to a `field/method/class/file` into the current position in the editor.

使用`编辑|复制|复制路径/引用操作`将`字段/方法/类/文件`的引用插入到编辑器的当前位置。

Position the `caret` within the `myMethod` method name and press `Ctrl+Alt+Shift+C`:

将`插入符号`放置在`myMethod`方法名称中，并按`Ctrl+Alt+Shift+C`:

To paste the reference, press `Ctrl+V`:

要粘贴引用，按`Ctrl+V`:

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3qrtvktmtji0.png)

You can use 

Ctrl+Q (View | Quick Documentation ), 

Ctrl+P (View | Parameter Info ), 

Ctrl+B (Navigate | Declaration ), 

and similar shortcuts not only in the editor but also in the suggestions list while using code completion.

您可以使用

`Ctrl+Q`(查看|快速文档)，

`Ctrl+P`(查看|参数信息)，

`Ctrl+B`(浏览|声明)，

以及类似的快捷键，不仅在编辑器中，而且在建议列表中使用代码完成。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3y9f9z6gb5k0.png)

If you position the caret at a symbol and press `Ctrl+Alt+Shift+T`, the list of `refactorings` that applicable to the current context will open.

如果将插入符号放置在符号处并按`Ctrl+Alt+Shift+T`，将打开适用于当前`上下文的重构`列表。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.6v2yioo4s6w0.png)

You can quickly navigate in the currently edited file with Ctrl+F12 (Navigate | File Structure ).

您可以快速导航在当前编辑的文件与`Ctrl+F12`(导航|文件结构)。

File structure shows the list of members of the current class.

文件结构显示当前类的成员列表。

To `navigate` to an element, select the element and press `Enter` or F4.

要`导航`到一个元素，选择元素并按`Enter`或F4。

To easily locate an item in the list, start typing its name.

要方便地在列表中找到一个项目，可以开始键入它的名称。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.w5ur8377yzk.png)

To see the `inheritance hierarchy` for the selected class, press `Ctrl+H` (Navigate | Type Hierarchy ).

要查看所选类的`继承层次`结构，请按`Ctrl+H`(导航|类型层次结构)。

You can also invoke the `hierarchy view` directly from the editor to see the `hierarchy` for the currently edited class.

还可以直接从编辑器调用`层次结构视图`，以查看当前编辑的类的`层次结构`。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.49s20y48mw80.png)

To quickly complete a `method call` of a `static method` located anywhere in your project, a library or a JDK, enter a `prefix` and press `Ctrl+空格` twice.

快速完成一个`方法调用`一个`静态方法`的任何位置在你的项目中,一个库或者一个JDK,进入一个`前缀`和按下`Ctrl +空格`两次。

You can press Alt+Enter to import the selected method.

按“Alt+Enter”可导入所选方法。

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.57zg3gzj7lc0.png)