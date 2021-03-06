<!-- vscode-markdown-toc -->
* 1. [ð Flink 1.10 çæ¬](#Flink1.10)
* 2. [ð Flink 1.11 çæ¬](#Flink1.11)
* 3. [ðFlink 1.12 çæ¬](#Flink1.12)
* 4. [Flink 1.13 çæ¬](#Flink1.13)

<!-- vscode-markdown-toc-config
	numbering=true
	autoSave=true
	/vscode-markdown-toc-config -->
<!-- /vscode-markdown-toc -->##  1. <a name='Flink1.10'></a>ð Flink 1.10 çæ¬

PyFlink: æ¯æåç`ç¨æ·èªå®ä¹å½æ°ï¼UDFï¼`

è®©ç¨æ·å¨ Table API/SQL ä¸­æ³¨åå¹¶ä½¿ç¨èªå®ä¹å½æ°ï¼UDFï¼å¦ UDTF / UDAF è§åä¸­ï¼

è¿äºæ°æ®ç»æä¸ºæ¯æ Pandas ä»¥åä»åå° PyFlink å¼å¥å° DataStream API å¥ å®äºåºç¡ã

ä» Flink 1.10 å¼å§ï¼ç¨æ·åªè¦æ§è¡ä»¥ä¸å½ä»¤å°±å¯ä»¥è½»æ¾å°éè¿ pip å®è£ PyFlinkï¼

```s
pip install apache-flink
```

##  2. <a name='Flink1.11'></a>ð Flink 1.11 çæ¬

ä¹åæ®éç Python UDF æ¯æ¬¡è°ç¨åªè½å¤ç`ä¸æ¡æ°æ®`ï¼èä¸å¨ Java ç«¯å Python ç«¯é½éè¦åºåå/ååºååï¼å¼éå¾å¤§ã

ð¥ð¥ð¥ð¥ð¥ð¥ð¥ð¥ð¥ð¥ð¥ð¥ð¥ð¥ð¥ð¥

PyFlink çå¤é¡¹æ§è½ä¼åï¼åæ¬:

- å¨`Table & SQL`ä¸­`èªå®ä¹`åä½¿ç¨`åéå Python UDF`
  
- ä¼å `Python UDF` çæ§è½ï¼å¯¹æ¯ 1.10.0 `å¯ä»¥æå 30 å`ã
  
- Python UDF ä¸­ç¨æ·`èªå®ä¹ metric`ï¼æ¹ä¾¿`çæ§åè°è¯` UDF çæ§è¡
  
- å¨`Table & SQL`ä¸­å¯ä»¥å®ä¹åä½¿ç¨ `Python UDTF`
  
- æ¹ä¾¿ Python ç¨æ·åºäº Numpy å Pandas ç­æ°æ®åæé¢åå¸¸ç¨ç Python åºï¼å¼åé«æ§è½ç Python UDFã
  
- `PyFlink table` å `Pandas DataFrame` ä¹é´æ ç¼åæ¢
  
- `SQL DDL/Client` çéæ
  
- ç¨æ·åªéè¦å¨ UDF ä¿®é¥°ä¸­é¢å¤å¢å ä¸ä¸ªåæ° `udf_type=âpandasâ` å³å¯

- æ¯æ¬¡è°ç¨å¯ä»¥å¤ç `N æ¡æ°æ®`ã

- æ°æ®æ ¼å¼åºäº `Apache Arrow`ï¼å¤§å¤§éä½äº JavaãPython è¿ç¨ä¹é´ç`åºåå/ååºåå`å¼éã

##  3. <a name='Flink1.12'></a>ðFlink 1.12 çæ¬

PyFlink çå¤é¡¹æ§è½ä¼åï¼åæ¬:

- PyFlink ä¸­æ·»å äºå¯¹äº `DataStream API` çæ¯æ

- æ¯æäº`æ ç¶æç±»å`çæä½ï¼ä¾å¦ `Mapï¼FlatMapï¼Filterï¼KeyBy` ç­ï¼

- å° PyFlink æ©å±å°äº`æ´å¤æçåºæ¯`ï¼æ¯å¦éè¦å¯¹`ç¶æ`æè`å®æ¶å¨ timer` è¿è¡`ç»ç²åº¦æ§å¶`çåºæ¯ã

- ç°å¨åçæ¯æå° PyFlink ä½ä¸é¨ç½²å° Kubernetesä¸ãææ°çææ¡£ä¸­è¯¦ç»æè¿°äºå¦ä½å¨ Kubernetes ä¸å¯å¨ `session` æ `application éç¾¤`ã

- ç¨æ·èªå®ä¹`èåå½æ° (UDAFs)`ãæ®éç UDFï¼`æ éå½æ°`ï¼æ¯æ¬¡åªè½å¤ç`ä¸è¡æ°æ®`ï¼è `UDAFï¼èåå½æ°ï¼`åå¯ä»¥å¤ç`å¤è¡æ°æ®`ï¼ç¨äºè®¡ç®`å¤è¡æ°æ®`ç`èåå¼`ãä¹å¯ä»¥ä½¿ç¨ `Pandas UDAF`ï¼æ¥è¿è¡åéåè®¡ç®ï¼éå¸¸æ¥è¯´ï¼æ¯`æ®é Python UDAF` å¿«10åä»¥ä¸ï¼ãæ³¨æ: æ®é Python UDAFï¼å½åä»æ¯æå¨ `group aggregations` ä»¥å`æµæ¨¡å¼`ä¸ä½¿ç¨ãå¦æéè¦å¨`æ¹æ¨¡å¼`æè`çªå£èå`ä¸­ä½¿ç¨ï¼å»ºè®®ä½¿ç¨ `Pandas UDAF`ã

ð¥ð¥ð¥ð¥ð¥ð¥ð¥ð¥ð¥ð¥ð¥ð¥ð¥ð¥ð¥ð¥

```py
from pyflink.common.typeinfo import Types
from pyflink.datastream import MapFunction, StreamExecutionEnvironment
class MyMapFunction(MapFunction):
    def map(self, value):
        return value + 1
env = StreamExecutionEnvironment.get_execution_environment()
data_stream = env.from_collection([1, 2, 3, 4, 5], type_info=Types.INT())
mapped_stream = data_stream.map(MyMapFunction(), output_type=Types.INT())
mapped_stream.print()
env.execute("datastream job")
```

##  4. <a name='Flink1.13'></a>Flink 1.13 çæ¬

å¨ä»¥åççæ¬ä¸­ï¼è¿äº `udafå½æ°` åªæ¯ææ çç `Group-byèå`ã

è®©Python DataStream APIåTable APIå¨ç¹æ§ä¸æ´æ¥è¿Java/Scala APIã

- PyFlink DataStream APIå¢å äºå¯¹`ç¨æ·å®ä¹ççªå£`çæ¯æã Flinkç¨åºç°å¨å¯ä»¥å¨`æ åçªå£å®ä¹`ä¹å¤ä½¿ç¨çªå£ãå ä¸º`çªå£`æ¯ææå¤ç`æ éæµ`çç¨åºçæ ¸å¿(éè¿å°`æ éæµ`åå²ææå¤§å°çâæ¡¶â)ï¼è¿å¤§å¤§æé«äºAPIçè¡¨è¾¾è½åãPyFlink DataStream APIç°å¨ä¹æ¯æ`æçæµ`çæ¹å¤çæ§è¡æ¨¡å¼ï¼è¿æ¯å¨Flink 1.12ä¸­ä¸º`Java DataStream API`å¼å¥çãæ¹å¤çæ§è¡æ¨¡å¼éè¿å©ç¨`æçæµ`çç¹æ§ï¼ç»è¿ç¶æåç«¯åæ£æ¥ç¹ï¼ç®åäº`æçæµ`ä¸çæä½å¹¶æé«äºç¨åºçæ§è½ã

- Python Table APIç°å¨æ¯æåºäº`è¡`çæä½ï¼ä¾å¦ï¼èªå®ä¹ç`è¡è½¬æ¢å½æ°`ã è¿äºå½æ°æ¯å¨åç½®å½æ°ä¹å¤å¯¹è¡¨åºç¨æ°æ®è½¬æ¢çä¸ç§ç®åæ¹æ³ãé¤äº`map()`ä¹å¤ï¼è¯¥APIè¿æ¯æ`flat_map()`ã`aggregate()`ã`flat_aggregate()`åå¶ä»åºäº`è¡`æä½çå½æ°ã è¿ä½¿å¾Python Table APIä¸Java Table APIå¨ç¹æ§ä¸æ´ç¸è¿äºãæ¯æç¨æ·å¨Group Windows`èªå®ä¹èåå½æ°`ãPyFlinkçTable APIä¸­çç»çªå£ç°å¨åæ¶æ¯æä¸è¬ç`Pythonç¨æ·å®ä¹èåå½æ°(udaf)`å`Pandas udaf`ã è¿äºåè½å¯¹äºè®¸å¤åæåMLè®­ç»ç¨åºæ¯è³å³éè¦çã

```py
@udf(result_type=DataTypes.ROW(
  [DataTypes.FIELD("c1", DataTypes.BIGINT()),
   DataTypes.FIELD("c2", DataTypes.STRING())]))
def increment_column(r: Row) -> Row:
  return Row(r[0] + 1, r[1])

table = ...  # type: Table
mapped_result = table.map(increment_column)
```
