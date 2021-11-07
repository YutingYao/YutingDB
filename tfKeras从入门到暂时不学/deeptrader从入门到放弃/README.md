# Projects

★keras★[NNResearchProject](https://github.com/YutingYao/NNResearchProject)

★keras★[Reinforcement-Learning-Stock-Trader](https://github.com/YutingYao/Reinforcement-Learning-Stock-Trader)

★keras★[rl-master](https://github.com/YutingYao/rl)

★momentum★[algo_trading_and_quant_strategies](https://github.com/YutingYao/algo_trading_and_quant_strategies)

★momentum★keras★★[iex-datareader★stock-analysis-engine](https://github.com/YutingYao/stock-analysis-engine)

★momentum★[pymc4](https://github.com/YutingYao/pymc4)

高频交易策略发展的深度学习框架 [PyStrategies](https://github.com/YutingYao/PyStrategies)

# Books

★★book★momentum★iex-datareader★[Hands-On-Machine-Learning-for-Algorithmic-Trading-sample](https://github.com/YutingYao/Hands-On-Machine-Learning-for-Algorithmic-Trading-sample)

★★book★momentum★keras★embedding★iex-datareader★[machine-learning-for-trading](https://github.com/YutingYao/machine-learning-for-trading)

★★book★momentum★keras★[Stock-Prediction-Models](https://github.com/YutingYao/Stock-Prediction-Models)

★★book★momentum★[trading_evolved](https://github.com/YutingYao/trading_evolved)

# Keys

## pylint 代码分析工具
## pymc 贝叶斯分析
## arviz 贝叶斯
## pytest 自动化测试框架
## pytest.fixture
## testfixtures

[testfixture](https://github.com/gdlg/testfixtures)是一组在用Python编写自动化测试时非常有用的助手和模拟对象。

## PyLimitBook

[PyLimitBook](https://github.com/danielktaylor/PyLimitBook)是Python实现的快速限价订单簿。

## unittest python测试
## mock.patch
## celery task 异步任务
## efficientdet 目标检测

## LSTM 长短期记忆
## DCGAN 深度卷积生成对抗网络
## NLTK 自然语言处理
## spacy NLP实体名称识别
## gensim NLP神器
## bert
## glove
## fasttext
## word2vec
## skip-gram
## psenet 文本检测算法
## TSNE 降维算法
## lle 降维算法
## lightgbm 高速梯度算法
## ndims
## XLA tensorflow的加速线性代数
## Ipyvolume 三维可视化
## plotly 可视化
## seaborn 可视化

sns.set_style('whitegrid')
iex.close.plot(figsize=(14, 5))
sns.despine()

## spylunking 查看日志的沙箱
## colourized logger
## colourlovers 配色方案

## pandas_datareader.data
Pandas库提供了专门从财经网站获取金融数据的API接口，可作为量化交易股票数据获取的另一种途径，该接口在urllib3库基础上实现了以客户端身份访问网站的股票数据。

查看Pandas的操作文档可以发现:

第一个参数为股票代码，苹果公司的代码为"AAPL"，国内股市采用的输入方式“股票代码”+“对应股市”，上证股票在股票代码后面加上“.SS”，深圳股票在股票代码后面加上“.SZ”。
DataReader可从多个金融网站获取到股票数据，如“Yahoo! Finance” 、“Google Finance”等，这里以Yahoo为例。第三、四个参数为股票数据的起始时间断。返回的数据格式为DataFrame。

```python
import pandas_datareader.data as web

start = datetime.datetime(2017,1,1)#获取数据的时间段-起始时间
end = datetime.date.today()#获取数据的时间段-结束时间
stock = web.DataReader("600797.SS", "yahoo", start, end)#获取浙大网新2017年1月1日至今的股票数据
```

```python
import pandas_datareader.data as web

start = datetime.datetime(2017,1,1)#获取数据的时间段-起始时间
end = datetime.date.today()#获取数据的时间段-结束时间
yahoo= web.DataReader('FB', 'yahoo', start=start, end=end)
```

```python
start = '2014'
end = datetime(2017, 5, 24)

yahoo= web.DataReader('FB', 'yahoo', start=start, end=end)
yahoo.info()
```

```python
import pandas_datareader.data as web

start = datetime(2015, 2, 9)
# end = datetime(2017, 5, 24)
iex = web.DataReader('FB', 'iex', start)


iex.info()
iex.tail()
```

```python
symbol = 'FB.US'
quandl = web.DataReader(symbol, 'quandl', '2015-01-01')
quandl.info()
```

```python
start = datetime(2010, 1, 1)
end = datetime(2013, 1, 27)
gdp = web.DataReader('GDP', 'fred', start, end)

gdp.info()
```

```python
inflation = web.DataReader(['CPIAUCSL', 'CPILFESL'], 'fred', start, end)
inflation.info()
```

```python
from pandas_datareader.famafrench import get_available_datasets
get_available_datasets()

ds = web.DataReader('5_Industry_Portfolios', 'famafrench')
print(ds['DESCR'])
```

```python
from pandas_datareader.nasdaq_trader import get_nasdaq_symbols
symbols = get_nasdaq_symbols()
symbols.info()
```

```python
df = web.get_data_tiingo('GOOG', api_key=os.getenv('TIINGO_API_KEY'))
df.info()
```

```python
book = web.get_iex_book('AAPL')
list(book.keys())

orders = pd.concat([pd.DataFrame(book[side]).assign(side=side) for side in ['bids', 'asks']])
orders.head()

for key in book.keys():
    try:
        print(f'\n{key}')
        print(pd.DataFrame(book[key]))
    except:
        print(book[key])

pd.DataFrame(book['trades']).head()
```

```python
from pandas_datareader import wb
gdp_variables = wb.search('gdp.*capita.*const')
gdp_variables.head()

wb_data = wb.download(indicator='NY.GDP.PCAP.KD', 
                      country=['US', 'CA', 'MX'], 
                      start=1990, 
                      end=2019)
wb_data.head()
```

## mplfinance
安装mplfinance库要求pandas和matplotlib

```python
import mplfinance as mpf
mpf.plot(yahoo.drop('Adj Close', axis=1), type='candle')
plt.tight_layout()
```

## sklearn.datasets.fetch_openml
from sklearn.datasets import fetch_openml

## yahoo
## investopedia
## ae
## iex
## redis
## webhook
## Boto3 AWS针对python的SDK
## AGSI python的web服务网关接口
## Django web框架
## spearmanr 相关系数
## sec filings 美国证监会文件
## trading performance
## vertical bull spread 垂直看涨买卖权
``TRADE SHARES``
``TRADE VERTICAL BULL SPREAD``
``TRADE VERTICAL BEAR SPREAD`` 
## horizontal spreads 水平套利
## straddle 套利
## butterfly spread 蝶式套利

## pytz 时区
## strftime 根据区域格式化本地时间
## ACF 时间序列前后的相关性
## statsmodels
statsmodels是一个Python模块,它提供对许多不同统计模型估计的类和函数,并且可以进行统计测试和统计数据的探索。

### statsmodels.tsa.seasonal.seasonal_decompose

所谓分解就是将时序数据分离成不同的成分，分解有：长期趋势Trend、季节性seasonality和随机残差residuals

statsmodels使用的X-11分解过程，它主要将时序数据分离成长期趋势、季节趋势和随机成分。 


```python
statsmodels.tsa.seasonal.seasonal_decompose（x，model = 'additive'，filt = None，period = None，two_side = True，extrapolate_trend = 0）
```

参数：

**x**：array_like，被分解的数据

**model**：{“**additive**”, “**multiplicative**”}, optional，"additive"（加法模型）和"multiplicative"（乘法模型）

**filt**：array_like, optional，用于滤除季节性成分的滤除系数。滤波中使用的具体移动平均法由two_side确定

**period**：int, optional，系列的时期。如果x不是pandas对象或x的索引没有频率，则必须使用。如果x是具有时间序列索引的pandas对象，则覆盖x的默认周期性。

**two_sided**：bool, optional，滤波中使用的移动平均法。如果为True（默认），则使用filt计算居中的移动平均线。如果为False，则滤波器系数仅用于过去的值

**extrapolate_trend**：int or ‘freq’, optional，如果设置为> 0，则考虑到许多（+1）最接近的点，由卷积产生的趋势将在两端外推线性最小二乘法（如果two_side为False，则为单一个最小二乘）。如果设置为“频率”，请使用频率最近点。设置此参数将导致趋势或残油成分中没有NaN值。


```python
res = sm.tsa.seasonal_decompose(s)
plt.plot(res.trend)
```
### statsmodels.api.tsa.x13_arima_analysis
**X-13-ARIMA-SEAT**是由USCensus发布的一个**季节性调整程序**。其基础是X-11，X-11是一系列的**中心化移动平均**。由于是中心化的移动平均，因此X-11在处理**序列两端的数据**存在困难。X-13-ARIMA-SEAT，引入带有**回归自变量**的ARIMA（regARIMA）来对序列进行**预测扩展**，从而部分解决了**最新数据的移动平均**。另外，regARIMA还可以**识别异常值**、日历效应、移动假期等。

因此，在使用X-13-ARIMA-SEAT的时候，一般是四个步骤：

1、**熟悉**了解要研究的**时间序列**；

2、选择合适的自变量，**拟合**回归方程；

3、对残差拟合ARIMA模型，对样本区间进行预测扩展；

4、使用X-11进行移动平均的季节性调整，提取季节性成分。


```python
import statsmodels.api as sm
```
```python
res = sm.tsa.x13_arima_analysis(s,maxorder=(4, 2), maxdiff=(2, 0))
res.plot()
```

### statsmodels.stats.stattools.jarque_bera

在统计学中，Jarque–Bera检验是对样本数据是否具有符合正态分布的偏度和峰度的拟合优度的检验。其统计测试结果总是非负的。如果结果远大于零，则表示数据不具有正态分布。

```python
from statsmodels.stats.stattools import jarque_bera

pd.DataFrame({'Jarque-Bera p-value':fx_returns.apply(lambda x: jarque_bera(x)[1])})
```
![image](https://raw.githubusercontent.com/YutingYao/image-hosting/master/deeptrader/image.3rb4mxzqct00.png)



```python
from statsmodels.tsa.stattools import acf, pacf, adfuller
```

```python
from statsmodels.graphics.tsaplots import plot_acf,plot_pacf
```

```python
from statsmodels.graphics.gofplots import qqplot
```

### statsmodels.tsa.arima_model.ARIMA


```python
from statsmodels.tsa.arima_model import ARIMA
```

ARIMA模型（英语：Autoregressive Integrated Moving Average model），差分整合移动平均**自回归模型**，又称整合移动平均自回归模型（移动也可称作滑动），是**时间序列预测**分析方法之一。**ARIMA(p，d，q)**中，AR是“自回归”，p为**自回归项数**；MA为“**滑动平均**”，q为**滑动平均项数**，d为使之成为平稳序列所做的差分次数（阶数）。“差分”一词虽未出现在ARIMA的英文名称中，却是关键步骤。

```python
results = ARIMA(fx_returns.GBPINR,(0,0,3)).fit()
results.summary()
```

ARMA 模型结果
部变量：GBPINR 编号观察：1001
模型：ARMA(0, 3) 对数似然 3406.586
方法：css-mle S.D.创新 0.008
日期：2021 年 2 月 4 日星期四 AIC -6803.171
时间：14:02:35 BIC -6778.627
样本：0 HQIC -6793.843
coef std err z P>|z| [0.025 0.975]
常量 -0.0002 0.000 -0.978 0.328 -0.001 0.000
ma.L1.GBPINR -0.0201 0.032 -0.637 0.524 -0.082 0.042
ma.L2.GBPINR -0.0452 0.031 -1.448 0.147 -0.106 0.016
ma.L3.GBPINR -0.0978 0.032 -3.077 0.002 -0.160 -0.035
根
实虚模频率
MA.1 1.9977 -0.0000j 1.9977 -0.0000
MA.2 -1.2301 -1.8992j 2.2628 -0.3415
MA.3 -1.2301 +1.8992j 2.2628 0.3415

```python
model = ARIMA(logv_train,(1,1,1))
results = model.fit()
results.summary()
```
ARIMA 模型结果
部变量：D.Volume No. 观察：695
模型：ARIMA(1, 1, 1) 对数似然 -136.600
方法：css-mle S.D.创新 0.293
日期：2020 年 10 月 28 日，星期三 AIC 281.200
时间：16:22:28 BIC 299.375
样本：1 HQIC 288.228
coef std err z P>|z| [0.025 0.975]
const -0.0013 0.000 -10.688 0.000 -0.002 -0.001
ar.L1.D.Volume 0.5511 0.032 17.370 0.000 0.489 0.613
ma.L1.D.Volume -1.0000 0.005 -202.173 0.000 -1.010 -0.990
根
实虚模频率
AR.1 1.8145 +0.0000j 1.8145 0.0000
MA.1 1.0000 +0.0000j 1.0000 0.0000

```python
model = ARIMA(pt,(0,0,1))
results = model.fit()
results.summary()
```

ARMA 模型结果
部变量：AdjClose 编号。观察：756
模型：ARMA(0, 1) 对数似然 712.217
方法：css-mle S.D.创新 0.094
日期：2020 年 10 月 28 日，星期三 AIC -1418.435
时间：16:33:58 BIC -1404.551
样本：0 HQIC -1413.087
coef std err z P>|z| [0.025 0.975]
常量 4.7732 0.007 712.745 0.000 4.760 4.786
ma.L1.AdjClose 0.9566 0.008 125.516 0.000 0.942 0.972
根
实虚模频率
MA.1 -1.0454 +0.0000j 1.0454 0.5000

The MA(1) model is then: 

$$p_t = 4.8 + a_t + 0.96a_{t-1}, \quad a_t \sim N(0,0.094^2)$$



### statsmodels.graphics.tsaplots.plot_acf

```python
from statsmodels.graphics.tsaplots import plot_acf

fig, (ax1,ax2) = plt.subplots(2,1,figsize=(12,16))
_ = five_min_agg.NumTrades.plot(ax=ax1, marker='o')
_ = ax1.set_title('Number of Trades during Continuous Time',fontsize=16)
_ = ax1.set_xlabel('Time')
_ = ax1.set_ylabel('Trades')
_ = plot_acf(five_min_agg.NumTrades,ax=ax2)

_ = ax2.set_title('ACF for Number of Trades',fontsize=16)
_ = ax2.set_xlabel('Lags')
_ = ax2.set_ylabel('ACF')

plt.show()
```

![output](https://raw.githubusercontent.com/YutingYao/image-hosting/master/deeptrader/output.7jiqg5ml83w0.png)

###  statsmodels.tsa.api.acf

acf使用总体序列样本均值和样本方差来确定相关系数。

我们编写了一个函数来测试 acf（20 个滞后）并查看它是否显着（在 95% 处），然后将其应用于每个工具。
数据看起来确实像随机游走。

```python
import statsmodels.tsa.api as ts

def test_for_random(series):
    (acf,alpha) = ts.acf(series,nlags=20,alpha=0.05)
    return pd.Series({'IsRandom': not np.array([acf < alpha[:,0], acf > alpha[:,1]]).any()})
eq_returns.stack().groupby(level=1).apply(test_for_random)
```


### statsmodels.multivariate.manova.MANOVA

多元方差分析（ multivariate analysis of variance ，MANOVA），亦称为多变量方差分析，即表示多元数据的方差分析，是一元方差分析的推广。作为一个多变量过程，多元方差分析在有两个或多个因变量时使用，并且通常后面是分别涉及各个因变量的显着性检验。

```python
from statsmodels.multivariate.manova import 

model = MANOVA(excess_ret,X,hasconst=True)
test = model.mv_test().summary_frame
#Reformat the output table
test.index = test.index.set_levels(X.columns,level=0)
test.loc[pd.IndexSlice[:,'Hotelling-Lawley trace'],:]
```

```python
model = MANOVA(excess_ret.loc[:mid_point_date],X.loc[:mid_point_date],hasconst=True)
test = model.mv_test().summary_frame
#Reformat the output table
test.index = test.index.set_levels(X.columns,level=0)
test.loc[pd.IndexSlice[:,'Hotelling-Lawley trace'],:]
```

```python
model = MANOVA(excess_ret.loc[mid_point_date:],X.loc[mid_point_date:],hasconst=True)
test = model.mv_test().summary_frame
#Reformat the output table
test.index = test.index.set_levels(X.columns,level=0)
test.loc[pd.IndexSlice[:,'Hotelling-Lawley trace'],:]
```

![image](https://raw.githubusercontent.com/YutingYao/image-hosting/master/deeptrader/image.778j6kg3d7k0.png)

### statsmodels.tsa.vector_ar.vecm.select_coint_rank

为了测试是否以及有多少整合系列，我们使用来自 stats 模型 VECM 包的 select_coint_rank 方法。在幕后，这使用了也可以直接使用的 coint_johansen 方法。 select_coint_rank 有一个汇总方法，可以更轻松地分析结果。我们查看 5% 的显着性水平并使用 Max Eigenvalue 方法（迹值方法给出了类似的结果）

```python
from statsmodels.tsa.vector_ar.vecm import select_coint_rank
res = select_coint_rank(excess_ret,det_order=-1,k_ar_diff=0,method='maxeig',signif=0.05)
res.summary()
```

使用具有 5% 显着性水平的最大特征值检验统计量的Johansen协整检验
r_0 r_1 检验统计 临界值
0	1	928.9	36.63
1	2	719.3	30.44
2	3	651.7	24.16
3	4	624.4	17.80
4	5	579.0	11.22
5	6	481.0	4.130

[Johansen协整检验(Johansen cointegration test)](https://zhuanlan.zhihu.com/p/404295128)

johansen协整检验的目的是检验序列间的协整关系，是vecm模型的基础

johansen协整检验的结果方程，就是最终vecm模型的方程的一部分

python中使用的是 statsmodels.tsa.vector_ar下vecm包中的vecm.coint_johansen()，但是这个命令我怎么也没有找到怎么输出一个summary的结果

所以只能像下面代码里一样一个一个地输出各种结果....

.lr1 .cvt 分别是迹检验的统计结果和Critical values（各置信度下的检验统计量)

***除此之外还有另一种检验标准：最大特征值检验

![image](https://raw.githubusercontent.com/YutingYao/image-hosting/master/deeptrader/image.1znh6y1nmjvk.png)


## cvxopt

我们将在本练习中利用优秀的 CVSOPT 包，特别是 qp 求解器。请参阅用户指南 [此处](https://cvxopt.org/userguide/coneprog.html#quadratic-programming)

Python中支持[Convex Optimization（凸规划）](https://www.jb51.net/article/166762.htm)的模块为[CVXOPT](https://blog.csdn.net/qq_41185868/article/details/111026294)

```python
import cvxopt

from cvxopt.solvers import qp

from cvxopt import matrix

#We'll need it for other parts of the exercise so let's create a function to run the optimization. 
#We add suport for no short selling constraints

def run_markowits(mu,sigma,min_return,short_restricted=False):
    n = mu.shape[0]
    n_eq = 1
    
    n_iq = n+1 if short_restricted else 1
    
    P = matrix(np.asmatrix(sigma))
    # Our formulation only minimizes risk so q is 0
    q = matrix(np.zeros((n,1)))

    #Equality Constraint sum of weights needs to be 1
    A = matrix(1.0,(n_eq,n))
    b = matrix(1.0,(n_eq,1))

    #Inequality Constraint. Min return needs to be at least R. Inequalities are expressed as upper bounds so need to be flipped for lower bounds.
    G = -matrix(0.0,(n_iq,n))
    h = -matrix(0.0,(n_iq,1))

    G[0,:] = -np.asmatrix(mu)
    h[0,:] = -min_return        
    
    if short_restricted:
        #If shorts not allowed we add additional n constraints that each weight is > 0. The bottom part of the G matrix is diagonal -1
        G[1:n+1,:] = -np.diag(np.ones(n))
    
    
    res = qp(P,q,G,h,A,b)
    
    w = np.array(res['x'])
    ret = mu.T.dot(w)[0]
    vol = np.sqrt(w.T.dot(sigma.dot(w))[0][0])
    return (w,ret,vol)
```




```python
rho = 10e-9

n = mu.shape[0]
n_eq = 1
n_iq = 2*n +1
P = matrix(0.0,(2*n,2*n))
P[0:n,0:n] = np.asmatrix(sigma)
P[0:n,n:2*n] = -np.asmatrix(sigma)
P[n:2*n,0:n] = -np.asmatrix(sigma)
P[n:2*n,n:2*n] = np.asmatrix(sigma)

# Our formulation only minimizes risk so q is 0
q = matrix(rho,(2*n,1))

#Equality Constraint sum of weights needs to be 1
A = matrix(0.0,(n_eq,2*n))
A[0,0:n] = np.ones(n)
A[0,n:2*n]= -np.ones(n)

b = matrix(1.0,(n_eq,1))

#Inequality Constraint. Min return needs to be at least R. Inequalities are expressed as upper bounds so need to be flipped for lower bounds.
G = -matrix(0.0,(n_iq,2*n))

G[1:,:] = -np.diag(np.ones(2*n))
h = matrix(0.0,(n_iq,1))

G[0,0:n] = -np.asmatrix(mu)
G[0,n:2*n] = np.asmatrix(mu)
h[0,:] = -R                    

res = qp(P,q,G,h,A,b)
```
```python
rho = 10e-4
q = matrix(rho,(2*n,1))
res = qp(P,q,G,h,A,b)

w = np.array(res['x']).reshape(2,n)
print(f'W+:{np.round(w[0],2)}\nW-:{np.round(w[1],2)}')
w_total = w[0]-w[1]
print(f'W:{np.round(w_total,2)}')

print(f'Expected Return: {mu.dot(w_total).round(5)}')
print(f'Sum of W: {w_total.sum()}')
```



## pmdarima.pm

[pm.auto_arima](https://www.jianshu.com/p/54826718be4f)可以自动搜索出arima模型中的(q, d, p)参数

p--代表预测模型中采用的时序数据本身的滞后数(lags) ,也叫做AR/Auto-Regressive项

d--代表时序数据需要进行几阶差分化，才是稳定的，也叫Integrated项

q--代表预测模型中采用的预测误差的滞后数(lags)，也叫做MA/Moving Average项


```python
import pmdarima as pm
model = pm.auto_arima(fxr, start_p=1, start_q=1,max_p=3, max_q=3,error_action='ignore', suppress_warnings=True,trace=False)
```

## arch.arch_model

我们将使用 arch 包来拟合时间序列.

ARCH 模型是一种流行的波动率建模方法，其主要使用收益率或残差的观测值作为波动率参考方式。

最简单的模型构建方法是使用模型构建方法 arch.arch_model，它能表示多数通用模型。最简单的arch调用将会使用均值为常数模型的GARCH（1，1）波动过程且误差服从正态分布。

ARCH模型（Autoregressive conditional heteroskedasticity model）全称“自回归条件异方差模型”，解决了传统的计量经济学对时间序列变量的第二个假设（方差恒定）所引起的问题。这个模型是获得2003年诺贝尔经济学奖的计量经济学成果之一。

是一种用来处理时间序列的模型。在股票中，ARCH可以用来预测股票的波动率，从而控制风险。（在金融领域，波动率与风险直接挂钩，一个资产波动越大，风险越大，而获得更高收益的可能也更大）

ARCH模型广泛应用于波动性有关广泛研究领域。包括政策研究、理论命题检验、季节性分析等方面。

要了解这是一种怎样的模型，我们可以从这个名字入手：自回归、条件异方差。

ARCH模型的英文直译是：自回归条件异方差模型。

粗略地说，该模型将当前一切可利用信息作为条件，并采用某种自回归形式来刻画方差的变异，对于一个时间序列而言，在不同时刻可利用的信息不同，而相应的条件方差也不同，利用ARCH模型，可以刻画出随时间而变异的条件方差。

在这里，异方差变成了条件异方差，其本质是一样的，只是约束条件变成了：一切可利用信息。

ARCH模型的基本思想是指在以前信息集下，某一时刻一个扰动项的发生是服从正态分布。该正态分布的均值为零，方差是一个随时间变化的量（即为条件异方差）。并且这个随时间变化的方差是过去有限项噪声值平方的线性组合（即为自回归）。这样就构成了自回归条件异方差。

但这个模型不能解释为什么存在异方差，只是描述了条件异方差的行为。

```python
from arch import arch_model

am = arch_model(fx_returns.USDINR,mean='Constant',p=1,q=1)
res_usd = am.fit()
print(res_usd.summary())
```

```python
am = arch_model(fx_returns.GBPINR,mean='Constant',p=1,q=1)
res_gbp = am.fit()
print(res_gbp.summary())
```

```python
am = arch_model(fx_returns.EURINR,mean='constant',p=1,q=1)
res_eur = am.fit()
print(res_eur.summary())
```

为了测试 ARCH 效应，我们使用 __Lagrage Multiplier Test__，如公式 2.54 中指定的

```python
arch_effects = []

for cur in currencies:

    fxr2 = fx_returns.loc[:,cur]**2
    arch_lags = pd.concat([fxr2.shift(i) for i in range(1,6)],axis=1).dropna()
    #arch_lags.columns = [f'L_{i}' for i in range(1,6)]
    #arch_lags =  sm.add_constant(arch_lags)
    fxr2 = fxr2.loc[arch_lags.index]
    regr = linear_model.LinearRegression()
    _ = regr.fit(arch_lags,fxr2)
    pred = regr.predict(arch_lags,)
    r2 = r2_score(fxr2,pred)
    p_value = chi2.pdf(r2*arch_lags.shape[0],5)
    
    arch_effects.append({'Currency': fxr2.name,'r2':r2.round(3),'p_value': p_value.round(3)})

arch_effects = pd.DataFrame(arch_effects).set_index('Currency')
arch_effects
```

从这个测试中我们可以看到，对于大多数货币，我们确认对波动率有一定的影响。唯一的例外似乎是：CNY、CZK、DDK

```python
def collect_results_garch(cur,m_id, model):
    out = {}
    out['Currency'] = cur
    out['ID'] = m_id
    out['Converged'] = model.convergence_flag == 0
    out['AIC'] = model.aic.round()
    params = model.params.round(4)
    t_values = model.tvalues.round(4)
    out['omega'] = f"{params['omega']} ({t_values['omega']})"
    out['alpha'] = f"{params['alpha[1]']} ({t_values['alpha[1]']})"
    out['beta'] = f"{params['beta[1]']} ({t_values['beta[1]']})"
    
    return out

garch_models = []

for cur in currencies:
    
    fxr = fx_returns.loc[:,cur]
    model_N= arch_model(fxr,mean='Constant',vol='GARCH',p=1,q=1,dist='normal',rescale=True).fit(disp='off')
    garch_models.append(collect_results_garch(cur,'GARCH11_N',model_N))
    model_T = arch_model(fxr,mean='Constant',vol='GARCH',p=1,q=1,dist='studentst',rescale=True).fit(disp='off')
    garch_models.append(collect_results_garch(cur,'GARCH11_T',model_T))

garch_models = pd.DataFrame(garch_models).set_index(['Currency','ID'])
garch_models
```
```python
def collect_results_igarch(cur,m_id, model):
    out = {}
    out['Currency'] = cur
    out['ID'] = m_id
    out['Converged'] = model.convergence_flag == 0
    out['AIC'] = model.aic.round()
    params = model.params.round(4)
    t_values = model.tvalues.round(4)
    out['omega'] = f"{params['omega']} ({t_values['omega']})"
    out['phi'] = f"{params['phi']} ({t_values['phi']})"
    out['d'] = f"{params['d']} ({t_values['d']})"
    out['beta'] = f"{params['beta']} ({t_values['beta']})"
    
    return out

garch_models = []

for cur in currencies:
    
    fxr = fx_returns.loc[:,cur]
    model_IN= arch_model(fxr,mean='Constant',vol='FIGARCH',p=1,q=1,dist='normal',rescale=True).fit(disp='off')
    garch_models.append(collect_results_igarch(cur,'GARCH11_IN',model_IN))
    model_IT = arch_model(fxr,mean='Constant',vol='FIGARCH',p=1,q=1,dist='studentst',rescale=True).fit(disp='off')
    garch_models.append(collect_results_igarch(cur,'GARCH11_IT',model_IT))
garch_models = pd.DataFrame(garch_models).set_index(['Currency','ID'])
garch_models
```
## scipy
### scipy.stats.chi2

[python scipy 卡方分布](https://blog.csdn.net/weixin_41102672/article/details/106570093)

scipy.stats.chi2（* args，** kwds ）= <scipy.stats._continuous_distns.chi2_gen object>

卡方分布连续随机变量。

![image](https://raw.githubusercontent.com/YutingYao/image-hosting/master/deeptrader/image.47ehocudk600.png)

```python
from scipy.stats import chi2
p_value = chi2.pdf(r2*arch_lags.shape[0],5)
```

### scipy.stats.ttest_1samp
用于t检验
当我们想将样本均值与总体均值（我们已经知道）进行比较时，使用1样本t检验。英国人的平均身高为175.3厘米。一项调查记录了10名英国男性的身高，我们想知道样本的平均值是否与总体平均值不同。

[Ljung-Box检验](https://www.cnblogs.com/travelcat/p/11400307.html)即LB检验，是时间序列分析中**检验序列自相关性**的方法。LB检验可同时用于时间序列以及时序模型的残差是否存在自相关性（是否为白噪声）。

```python
from scipy.stats import ttest_1samp
from statsmodels.stats.diagnostic import acorr_ljungbox
from statsmodels.stats.stattools import jarque_bera

pd.DataFrame({'T-test p-value':fx_weekly_returns.apply(lambda x: ttest_1samp(x,0)[1])})
pd.DataFrame({'LJung-Box p-value':fx_weekly_returns.apply(lambda x: acorr_ljungbox(x,lags=1)[1][0])})
pd.DataFrame({'Jarque-Bera p-value':fx_weekly_returns.apply(lambda x: jarque_bera(x)[1])})
```

![image](https://raw.githubusercontent.com/YutingYao/image-hosting/master/deeptrader/image.3rb4mxzqct00.png)


## minute
## intraday
## expire
## momentum indicators
## volatility indicators
## cycle indicators

## third party
## tflite_model_maker.config.QuantizationConfig
## backtest
## algotraders
## ticker 每笔交易的唯一编号



## momentum
## one-hot
## pb
## statsmodel 类似SPSS
## scipy.signal 滤波器
## cdist 计算两个矩阵对之间的向量距离
## gaussian process 高斯分布+随机过程
## tensorflow_probability.distribution 
## tensorflow_probability
## tf.eable_eager_excution 类似于pytorch的动态图模式


## NMS 非极大值抑制
## hparams
## slack
## assert
## hasattr 判断对象是否有对应属性
## turbulence
## turbulence index
## self.trades
## self.tech
## self.debug
## yield 暂停，在while循环中，与next配合，而return是停止
## gfile
## mock.patch
## governing permissions
## keras
## verbose
## argparse
## parser
## absl
## tokenizer
## slack
## algo
## def
## event
## url
## tensorspec
## shape
## model

