from pyspark.sql import SparkSession
# spark = SparkSession.builder.master("local[*]").getOrCreate()
from __future__ import print_function 
from pyspark.context import SparkContext
from pyspark.ml.feature import VectorAssembler
from pyspark.ml.classification import RandomForestClassifier
from pyspark.sql.session import SparkSession
from pyspark.ml import Pipeline
from pyspark.ml.feature import StringIndexer,PCA
#import packages to perform cross validation
from pyspark.ml.evaluation import MulticlassClassificationEvaluator
from pyspark.ml.tuning import CrossValidator, ParamGridBuilder
from pyspark.sql.functions import split,udf,col,regexp_replace,pandas_udf, PandasUDFType
from pyspark.sql.types import IntegerType,StringType,DoubleType
import pyspark.sql.functions as f
from collections import Counter
import pandas as pd
import numpy as np
import datetime 
import pickle
from xgboost import XGBClassifier
from sklearn.model_selection import train_test_split
from matplotlib import pyplot as plt

df = spark.read.option("header",True).csv("gs://ch16b024/park_violations.csv")
df.show(5)

#Data preprocessing pipeline
df=df.dropDuplicates(subset=['Summons Number'])
columns_to_drop = ['Plate ID','Issuer Code','Time First Observed','Vehicle Expiration Date','House Number','Street Name','Intersecting Street','Date First Observed','Law Section','Sub Division','Violation Legal Code','Days Parking In Effect','From Hours In Effect','To Hours In Effect','Vehicle Color','Unregistered Vehicle?','Vehicle Year','Meter Number','Feet From Curb','Violation Post Code','Violation Description',"No Standing or Stopping Violation","Hydrant Violation","Double Parking Violation","Latitude","Longitude","Community Board","Community Council","Census Tract","BIN","BBL","NTA"]
df = df.drop(*columns_to_drop)
#UDFS
#Extracting information from date
def day_finder(x):
    return datetime.datetime.strptime(x, '%m/%d/%Y').weekday()
#Bucketizing violation time
def time_bucket(x):
  #Bucketizing the time into 8 buckets
  if x is None:
    return 3
  if x[-1]!='P' and x[-1]!='A':
    return 3
  try:
    time = int(x[:-1])
  except:
    return 3
  if x[-1]=='P':
    time = 1200+time
  for i in range(8):
    if time>=300*i and time<300*(i+1):
      return i
time_udf = udf(lambda x: time_bucket(x), IntegerType())
day_udf = udf(lambda x: day_finder(x), IntegerType())

###################################################################### PREPROCESSING ######################################################################################
#Splitting the issue date into month,year,day
df_new = df.withColumn('Month',split('Issue Date','/')[0]).withColumn('Year',split('Issue Date','/')[2]).withColumn('Day',day_udf(col('Issue Date'))).withColumn('Time',time_udf(col('Violation Time')))
#converting the columns into integers
df_new = df_new.withColumn("Year",df_new["Year"].cast(IntegerType())).withColumn("Month",df_new["Month"].cast(DoubleType())).withColumn("Day",df_new["Day"].cast(DoubleType())).withColumn("Time",df_new["Time"].cast(DoubleType()))
#Removing outliers and some filtering
df_new = df_new.where(f.col("Year")>2012)
#Dropping columns
df_new =df_new.drop(*['Issue Date','Violation Time','Year','Issuer Squad'])
#Filling na
df_new = df_new.fillna({'Time':3})
#Removing na locaions of violation location and violation count
df_new = df_new.dropna(how='any',subset=['Violation Location','Violation County'])
#Fill na of these columns using  respective max values 
# cols = ['Vehicle Body Type','Vehicle Make','Violation County','Violation In Front Of Or Opposite']
# agg_expr = [mode(f.collect_list(col)).alias(col) for col in cols]
# max_vals = df_new.agg(*agg_expr).collect()[0]
# df_new = df_new.fillna({'Vehicle Body Type':max_vals['Vehicle Body Type'],'Vehicle Make':max_vals['Vehicle Make'],'Violation County':max_vals['Violation County'],'Violation In Front Of Or Opposite':max_vals['Violation In Front Of Or Opposite']})
df_new=df_new.dropna(how='any')
#Renaming columns
names = df_new.schema.names
for name in names:
  df_new = df_new.withColumnRenamed(name, name.replace(" ","_"))
#Mapping violation location
df_new = df_new.withColumn('Violation_Location', regexp_replace('Violation_Location', 'KINGS', 'K'))\
.withColumn('Violation_Location', regexp_replace('Violation_Location', 'KING', 'K'))\
.withColumn('Violation_Location', regexp_replace('Violation_Location', 'QUEEN', 'Q'))\
.withColumn('Violation_Location', regexp_replace('Violation_Location', 'QU', 'Q'))\
.withColumn('Violation_Location', regexp_replace('Violation_Location', 'NEWY', 'NY'))\
.withColumn('Violation_Location', regexp_replace('Violation_Location', 'NEW Y', 'NY'))\
.withColumn('Violation_Location', regexp_replace('Violation_Location', 'MAN', 'NY'))\
.withColumn('Violation_Location', regexp_replace('Violation_Location', 'MH', 'NY'))\
.withColumn('Violation_Location', regexp_replace('Violation_Location', 'BRONX', 'BX'))

############################################################################## TRAINING PIPELINE ####################################################################################33
#Label encoding pipeline
#Split the data
train, test = df_new.randomSplit([0.93,0.07])
indexers = [StringIndexer(inputCol=column, outputCol=column+"_index",handleInvalid='keep').fit(df_new) for column in list(set(df_new.columns)-set(['Month','Day','Time','Violation_Location','Summons_Number']))]
target_indexer = StringIndexer(inputCol="Violation_Location", outputCol="label",handleInvalid='keep').fit(df_new)
assembler = VectorAssembler(inputCols=['Registration_State_index','Plate_Type_index','Violation_Code_index','Vehicle_Body_Type_index','Vehicle_Make_index','Issuing_Agency_index','Street_Code1_index','Street_Code2_index','Street_Code3_index','Issuer_Precinct_index','Issuer_Command_index','Violation_In_Front_Of_Or_Opposite_index','Violation_County_index','Month','Day','Time'],outputCol='features')
rf = RandomForestClassifier(numTrees=500,maxBins=10000,labelCol="label", featuresCol="features")
pipeline = Pipeline(stages=indexers+[target_indexer,assembler,rf])

model = pipeline.fit(train)
#Transforming train data
df_r1 = model.transform(train)

A = {c.name: c.metadata["ml_attr"]["vals"] for c in df_r1.schema.fields if c.name.endswith("label")}
print("String Indexed labels:{}".format(A))
# #Saving model
model.save("gs://ch16b024/model_finalproject_v1")
############################################################################ XGBOOST TRAINING #######################################
#Training XGBOOST only on first few data, since training happens outside pyspark
df_pandas = df_r1.limit(100000).toPandas()
column_names = ['Registration_State_index','Plate_Type_index','Violation_Code_index','Vehicle_Body_Type_index','Vehicle_Make_index','Issuing_Agency_index','Street_Code1_index','Street_Code2_index','Street_Code3_index','Issuer_Precinct_index','Issuer_Command_index','Violation_In_Front_Of_Or_Opposite_index','Violation_County_index','Month','Day','Time']
X_pandas = df_pandas[column_names].values
y_pandas = df_pandas['label'].values
X_train, X_test, y_train, y_test = train_test_split(X_pandas, y_pandas, test_size = 0.2)
xgboost_model = XGBClassifier(max_depth=7, n_estimators=100, objective='multi:softprob')
xgboost_model.fit(X_train, y_train)
#Testing the model
y_pred_train = xgboost_model.predict(X_train)
print("For train\nAccuracy score:{},Balanced accuracy score:{},f1 score:{}".format(accuracy_score(y_train,y_pred_train),balanced_accuracy_score(y_train,y_pred_train),f1_score(y_train,y_pred_train, average='weighted')))
y_pred_test = xgboost_model.predict(X_test)
print("For test\nAccuracy score:{},Balanced accuracy score:{},f1 score:{}".format(accuracy_score(y_test,y_pred_test),balanced_accuracy_score(y_test,y_pred_test),f1_score(y_test,y_pred_test, average = 'weighted')))
#For saving model
filename = 'gs://ch16b024/XGB_final_model_v1.pkl'
pickle.dump(xgboost_model, open(filename, 'wb'))

#Defining XGBOOST prediction UDF
@f.pandas_udf(returnType=DoubleType())
def predict_pandas_udf(*cols):
    # cols will be a tuple of pandas.Series here.
    X = pd.concat(cols, axis=1)
    return pd.Series(xgboost_model.predict(np.array(X)))
#Get the transformed data
df_full = model.transform(df_new)
#Apply XGBOOST on it
df_r3 = df_full.withColumn('prediction_xgb',predict_pandas_udf(*column_names))


############################################################################## TESTING ###########################################
#Test set for random forest
df_test = model.transform(test)
#Create an evaluation object for the model using the R^2 metric
lr_evaluator = MulticlassClassificationEvaluator(predictionCol = "prediction", labelCol="label",metricName="accuracy")
#Print the Evaluation Result
print("Test accuracy for Random forest:{}\n".format(lr_evaluator.evaluate(df_test)))

#Test set for XGBOOST
#Create an evaluation object for the model using the R^2 metric
lr_evaluator = MulticlassClassificationEvaluator(predictionCol = "prediction_xgb", labelCol="label",metricName="accuracy")
#Print the Evaluation Result
print("Test accuracy for XGBOOST:{}\n".format(lr_evaluator.evaluate(df_r3)))