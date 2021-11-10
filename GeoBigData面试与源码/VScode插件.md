## data preview

文件预览： `Ctrl` + `Shift` + `D`

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3888s6ryt0e0.png)

## arepl

```python
""" 当你输入时，AREPL会自动实时计算python代码。  
 
首先，确保已经安装了python 3.7或更高版本。  

或者通过命令搜索来运行AREPL: control-shift-p  
 
或者使用快捷键:control-shift-a(当前文档)/ control-shift-q(新文档)  
 
变量显示:本地变量的最终状态以可折叠的JSON格式显示。  
 
错误显示:当您出错时，将显示堆栈跟踪的错误。  

如果你想转储局部变量或在程序的特定点转储变量，你可以使用dump函数:   

from arepl_dump import dump

和

dump函数

在非arepl环境下会报错

"""

from arepl_dump import dump


# Simple List

x = [1,2,3]
y = [num*2 for num in x]
print(y)

# Dumping

def milesToKilometers(miles):
    kilometers = miles*1.60934
    dump() # dumps all the vars in your function

    # or dump when function is called for a second time
    dump(None,1) 

milesToKilometers(2*2)
milesToKilometers(3*3)

for char in ['a','b','c']:
    dump(char,2) # dump a var at a specific iteration

a=1
dump(a) # dump specific vars at any point in your program
a=2

# Turtle

import turtle

# window in right hand side of screen
turtle.setup(500,500,-1,0)

# you can comment this out to keep state inbetween runs
turtle.reset()

turtle.forward(100)
turtle.left(90)

# Web call

import requests
import datetime as dt

r = requests.get("https://api.github.com")

#$save
# #$save saves state so request is not re-executed when modifying below

now = dt.datetime.now()
if r.status_code == 200:
    print("API up at " + str(now))
```

## tabnine

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.3612u9rmnm00.png)

## vscode map preview

CSV files (as of 0.5.0)

GPX

GeoJSON

IGC

KML

TopoJSON

WFS

GML

GML2

GML3

WKT

|参数|值|功能|
|---|---|---|
|	map.preview.coordinateDisplay.projection	|	EPSG:4326	|	要使用的投影(默认:EPSG:4326)  	|
|	map.preview.coordinateDisplay.format	|	Lat: {y}, Lng: {x}	|	描述如何格式化坐标的格式字符串。 格式字符串必须包含{x}和{y}坐标占位符标记  	|
|	map.preview.style.line.stroke.color	|	rgba(49, 159, 211, 1)	|	用于线描的默认颜色。 这是一个rgba(r, g, b, a)表达式。 注意:如果KML的特性配置了内联样式，则不会影响KML  	|
|	map.preview.style.line.stroke.width	|	2	|	线笔画的默认粗细。 注意:如果KML的特性配置了内联样式，则不会影响KML  	|
|	map.preview.style.polygon.stroke.color	|	rgba(49, 159, 211, 1)	|	用于多边形边界的默认颜色。 这是一个rgba(r, g, b, a)表达式。 注意:如果KML的特性配置了内联样式，则不会影响KML  	|
|	map.preview.style.polygon.stroke.width	|	2	|	多边形边界的默认厚度。 注意:如果KML的特性配置了内联样式，则不会影响KML  	|
|	map.preview.style.polygon.fill.color	|	rgba(49, 159, 211, 0.1)	|	用于多边形填充的默认颜色。 这是一个rgba(r, g, b, a)表达式。 注意:如果KML的特性配置了内联样式，则不会影响KML  	|
|	map.preview.style.vertex.enabled	|	FALSE	|	控制是否设置线和多边形特征顶点的样式  	|
|	map.preview.style.vertex.radius	|	3	|	屏幕空间中的默认顶点半径。 用于在线条和多边形图层中设置顶点的样式。 如果map.preview.style.vertex。 Enabled '为false，此设置无效。 注意:如果KML的特性配置了内联样式，则不会影响KML  	|
|	map.preview.style.vertex.fill.color	|	rgba(49, 159, 211, 1)	|	默认的顶点颜色。 用于在线条和多边形图层中设置顶点的样式。 如果map.preview.style.vertex。 Enabled '为false，此设置无效。 注意:如果KML的特性配置了内联样式，则不会影响KML  	|
|	map.preview.style.point.radius	|	5	|	屏幕空间中的默认点半径。 注意:如果KML的特性配置了内联样式，则不会影响KML  	|
|	map.preview.style.point.stroke.color	|	rgba(49, 159, 211, 1)	|	点边界的默认颜色。 这是一个rgba(r, g, b, a)表达式。 注意:如果KML的特性配置了内联样式，则不会影响KML  	|
|	map.preview.style.point.stroke.width	|	2	|	点边界的默认厚度。 注意:如果KML的特性配置了内联样式，则不会影响KML  	|
|	map.preview.style.point.fill.color	|	rgba(49, 159, 211, 0.2)	|	点填充的默认颜色。 这是一个rgba(r, g, b, a)表达式。 注意:如果KML的特性配置了内联样式，则不会影响KML  	|
|	map.preview.selectionStyle.line.stroke.color	|	rgba(255, 0, 0, 1)	|	为选定的特征使用的线描的默认颜色。 这是一个rgba(r, g, b, a)表达式。 注意:如果KML的特性配置了内联样式，则不会影响KML  	|
|	map.preview.selectionStyle.line.stroke.width	|	2	|	所选特征的线笔画的默认厚度。 注意:如果KML的特性配置了内联样式，则不会影响KML  	|
|	map.preview.selectionStyle.polygon.stroke.color	|	rgba(255, 0, 0, 1)	|	为所选特征的多边形边界使用的默认颜色。 这是一个rgba(r, g, b, a)表达式。 注意:如果KML的特性配置了内联样式，则不会影响KML  	|
|	map.preview.selectionStyle.polygon.stroke.width	|	2	|	所选特征的默认多边形边界厚度。 注意:如果KML的特性配置了内联样式，则不会影响KML  	|
|	map.preview.selectionStyle.polygon.fill.color	|	rgba(255, 0, 0, 0.1)	|	为所选特征的多边形填充使用的默认颜色。 这是一个rgba(r, g, b, a)表达式。 注意:如果KML的特性配置了内联样式，则不会影响KML  	|
|	map.preview.selectionStyle.point.stroke.color	|	rgba(255, 0, 0, 1)	|	为所选特性的点边界使用的默认颜色。 这是一个rgba(r, g, b, a)表达式。 注意:如果KML的特性配置了内联样式，则不会影响KML  	|
|	map.preview.selectionStyle.point.stroke.width	|	2	|	所选特性的点边界的默认厚度。 注意:如果KML的特性配置了内联样式，则不会影响KML  	|
|	map.preview.selectionStyle.point.fill.color	|	rgba(255, 0, 0, 0.2)	|	为选定的特征点填充使用的默认颜色。 这是一个rgba(r, g, b, a)表达式。 注意:如果KML的特性配置了内联样式，则不会影响KML  	|
|	map.preview.projections	|		|	要注册到这个扩展的其他地图投影的列表。 这样的投影可以与“地图预览(带有投影)”命令一起使用  	|
|	map.preview.csvColumnAliases	|	[object Object],[object Object],[object Object],[object Object],[object Object],[object Object]	|	在尝试预览给定CSV文件时查找X/Y坐标的不区分大小写的列名对列表  	|
|	map.preview.declutterLabels	|	FALSE	|	表示是否对特性标签进行整理。 在预览标签较多的数据(如点KML文件)时很有用  	|

命令：

![image](https://raw.githubusercontent.com/YutingYao/DailyJupyter/main/imageSever/image.1qsct3xwru0w.png)
