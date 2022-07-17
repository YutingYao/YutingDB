from pyspark.sql import SQLContext
from pyspark.ml.functions import vector_to_array
from pyspark.sql.functions import concat_ws
from sklearn.model_selection import train_test_split
from sklearn.cluster import MiniBatchKMeans
from evaluate import *
import pickle
import numpy as np

iterations = 0
avg_acc = 0
incremental_acc = []

clustering_model = MiniBatchKMeans(n_clusters=2,random_state=3,batch_size=1000)

prev_centroid = np.zeros((2,4096))

def getSqlContextInstance(sparkContext):
    if ('sqlContextSingletonInstance' not in globals()):
        globals()['sqlContextSingletonInstance'] = SQLContext(sparkContext)
    return globals()['sqlContextSingletonInstance']

def preprocessAndTrainEvalModel(time,rdd,ssc,stop_flag,data_prep_pipe,model):
    print()
    print("========= %s =========" % str(time))
    
    if(not rdd.isEmpty()):
        stop_flag[0] = 0
        sqlContext = getSqlContextInstance(rdd.context)
        df1 = sqlContext.createDataFrame(rdd)
        df = df1.select(concat_ws(' ',df1.subject,df1.body).alias("text"),"classLabel")
        preprocess = data_prep_pipe.fit(df)
        clean_data = preprocess.transform(df)
        clean_data = clean_data.select(['label','features'])
        clean_data.show(5)
        clean_data = clean_data.withColumn('features', vector_to_array('features'))
        X = clean_data.select('features').rdd.map(lambda x:x['features']).collect()
        Y = clean_data.select('label').rdd.map(lambda x:x['label']).collect()
        np.random.seed(3)
        X_train, X_test, y_train, y_test = train_test_split(X, Y, test_size=0.2, random_state=3)
        model.weights.partial_fit(X_train,y_train,classes=[0,1])
        incremental_acc.append(model.weights.score(X_test,y_test))
        print('working')
    else:
        stop_flag[0] += 1
        if(stop_flag[0]>2):
            pickle.dump(model.weights, open(model.name+'.sav', 'wb'))
            with open('incrementing_acc','a') as inc_acc:
                inc_acc.write(' '.join(map(str,incremental_acc))+'\n')
            ssc.stop()

def preprocessAndTestModel(time,rdd,ssc,stop_flag,data_prep_pipe,model):
    print()
    global iterations,avg_acc
    print("========= %s =========" % str(time))
    if(not rdd.isEmpty()):
        iterations += 1
        stop_flag[0] = 0
        sqlContext = getSqlContextInstance(rdd.context)
        df1 = sqlContext.createDataFrame(rdd)
        df = df1.select(concat_ws(' ',df1.subject,df1.body).alias("text"),"classLabel")
        preprocess = data_prep_pipe.fit(df)
        clean_data = preprocess.transform(df)
        clean_data = clean_data.select(['label','features'])
        #clean_data.show(10)
        clean_data = clean_data.withColumn('features', vector_to_array('features'))
        X = clean_data.select('features').rdd.map(lambda x:x['features']).collect()
        Y = clean_data.select('label').rdd.map(lambda x:x['label']).collect()
        np.random.seed(3)
        Y_pred = model.weights.predict(X)
        avg_acc += evaluate(Y,Y_pred)
        
    else:
        stop_flag[0] += 1
        if(stop_flag[0]>2):
            print('the avg accuracy is')
            print(avg_acc/iterations)
            with open('avg_acc','a') as acc:
                acc.write(str(avg_acc/iterations)+"\n")
            ssc.stop()

def preprocessAndTrainModel(time,rdd,ssc,stop_flag,data_prep_pipe,model):
    print()
    print("========= %s =========" % str(time))
    
    if(not rdd.isEmpty()):
        stop_flag[0] = 0
        sqlContext = getSqlContextInstance(rdd.context)
        df1 = sqlContext.createDataFrame(rdd)
        df = df1.select(concat_ws(' ',df1.subject,df1.body).alias("text"),"classLabel")
        preprocess = data_prep_pipe.fit(df)
        clean_data = preprocess.transform(df)
        clean_data = clean_data.select(['label','features'])
        clean_data.show(5)
        clean_data = clean_data.withColumn('features', vector_to_array('features'))
        X = clean_data.select('features').rdd.map(lambda x:x['features']).collect()
        Y = clean_data.select('label').rdd.map(lambda x:x['label']).collect()
        np.random.seed(3)
        model.weights.partial_fit(X,Y,classes=[0,1])
        print('working')
    else:
        stop_flag[0] += 1
        if(stop_flag[0]>2):
            pickle.dump(model.weights, open(model.name+'.sav', 'wb'))
            ssc.stop()

def preprocessAndClustering(time,rdd,ssc,stop_flag,data_prep_pipe):
    print()
    print("========= %s =========" % str(time))
    
    if(not rdd.isEmpty()):
        global prev_centroid
        stop_flag[0] = 0
        sqlContext = getSqlContextInstance(rdd.context)
        df1 = sqlContext.createDataFrame(rdd)
        df = df1.select(concat_ws(' ',df1.subject,df1.body).alias("text"),"classLabel")
        preprocess = data_prep_pipe.fit(df)
        clean_data = preprocess.transform(df)
        clean_data = clean_data.select(['label','features'])
        clean_data.show(5)
        clean_data = clean_data.withColumn('features', vector_to_array('features'))
        X = clean_data.select('features').rdd.map(lambda x:x['features']).collect()
        Y = clean_data.select('label').rdd.map(lambda x:x['label']).collect()
        np.random.seed(3)
        clustering_model.partial_fit(X)
        print(prev_centroid)
        print(clustering_model.cluster_centers_)
        if(hasConverged(clustering_model.cluster_centers_)):
            pickle.dump(clustering_model, open('MKM.sav', 'wb'))
            ssc.stop()
        prev_centroid = clustering_model.cluster_centers_.copy()

def hasConverged(curr_centroid):
    for i in range(2):
        for j in range(4096):
            if(abs(curr_centroid[i][j]-prev_centroid[i][j]) > 0.01):
                return False
    return True