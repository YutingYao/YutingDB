import socket
import json
import pandas as pd
from sklearn.model_selection import train_test_split
import numpy as np
from pyspark.sql.functions import concat_ws
from sklearn.metrics import accuracy_score
from pyspark.ml.feature import StopWordsRemover
from sklearn.feature_extraction.text import HashingVectorizer
from pyspark.sql import SparkSession
from sklearn.naive_bayes import MultinomialNB
from sklearn import linear_model
from sklearn.datasets import load_digits
from sklearn.linear_model import Perceptron
import warnings
warnings.filterwarnings("ignore")


def prepro(df):
    for i in range(0,len(df['feature1'])):
        clean=list(df["feature1"][i].split(" "))
        #print(clean)
        for j in clean:
            if(j.startswith("@")):
                clean.remove(j)
                continue
            if(j.startswith("#")):
                clean.remove(j)
                continue
            if(j.startswith("http://")):
                clean.remove(j)
                continue
        
            if(any(map(str.isdigit, j))):
                clean.remove(j)
                continue
        df['feature1'][i] = clean
        


TCP_IP = "localhost"
TCP_PORT = 6100
soc=socket.socket(socket.AF_INET, socket.SOCK_STREAM)

try:
    soc.connect((TCP_IP, TCP_PORT))
    while True:
        json_recv=soc.recv(2048).decode()
        a_json=json.loads(json_recv)
        df=pd.DataFrame.from_dict(a_json,orient="index")
        prepro(df)
        spark = SparkSession.builder.appName("pandas to spark").getOrCreate()
        df_spark = spark.createDataFrame(df)
        
        without_stop = StopWordsRemover(inputCol="feature1", outputCol="filtered")
        df_without = without_stop.transform(df_spark)
        
        h2=np.array(df_without.withColumn("filtered",concat_ws(" ","filtered")).collect(),dtype="object").tolist()
        h22=[k[2] for k in h2 ]
        
        hv_2=HashingVectorizer(alternate_sign=False)
        hv_22=hv_2.fit_transform(h22).toarray()
        
        x_train,x_test,y_train,y_test=train_test_split(hv_22,df['feature0'],test_size=0.3)
        
        
        # MULTINOMIAL NAIVE BAYEES
        mnd = MultinomialNB()
        mnd.partial_fit(x_train,y_train,classes=np.unique(y_train))
        print("accuracy NB=",mnd.score(x_test,y_test))
        
        #SGD classifier
        clf=linear_model.SGDClassifier()
        clf.fit(x_train,y_train)
        print("accuracy SGD=", clf.score(x_test,y_test))
        
        
        #PERCEPTRON
        cl=Perceptron(tol=1e-3,random_state=0)
        cl.fit(x_train,y_train)
        print("Accuracy perceptron=",cl.score(x_test,y_test))
        
        
        
        
        print('.......................................................................................................')
        
        
        

except Exception as e:
    print(e)