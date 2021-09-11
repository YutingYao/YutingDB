# Projects

★keras★[NNResearchProject](https://github.com/YutingYao/NNResearchProject)

★keras★[Reinforcement-Learning-Stock-Trader](https://github.com/YutingYao/Reinforcement-Learning-Stock-Trader)

★keras★[rl-master](https://github.com/YutingYao/rl)

★momentum★[algo_trading_and_quant_strategies](https://github.com/YutingYao/algo_trading_and_quant_strategies)

★momentum★keras★★[iex-datareader★stock-analysis-engine](https://github.com/YutingYao/stock-analysis-engine)

★momentum★[pymc4](https://github.com/YutingYao/pymc4)

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

```python
import statsmodels.api as sm
```

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

**X-13-ARIMA-SEAT**是由USCensus发布的一个**季节性调整程序**。其基础是X-11，X-11是一系列的**中心化移动平均**。由于是中心化的移动平均，因此X-11在处理**序列两端的数据**存在困难。X-13-ARIMA-SEAT，引入带有**回归自变量**的ARIMA（regARIMA）来对序列进行**预测扩展**，从而部分解决了**最新数据的移动平均**。另外，regARIMA还可以**识别异常值**、日历效应、移动假期等。

因此，在使用X-13-ARIMA-SEAT的时候，一般是四个步骤：

1、**熟悉**了解要研究的**时间序列**；

2、选择合适的自变量，**拟合**回归方程；

3、对残差拟合ARIMA模型，对样本区间进行预测扩展；

4、使用X-11进行移动平均的季节性调整，提取季节性成分。


```python
res = sm.tsa.x13_arima_analysis(s,maxorder=(4, 2), maxdiff=(2, 0))
res.plot()
```

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



```python
from statsmodels.tsa.arima_model import ARIMA
```


## chi2
from scipy.stats import chi2

## pmdarima.pm
import pmdarima as pm

## arch.arch_model
from arch import arch_model

## scipy.stats.ttest_1samp

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

