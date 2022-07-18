## referenced Harvard CS205 Course Material Lecture C3
## https://harvard-iacs.github.io/2021-CS205/lectures/C3/
from pyspark import SparkConf,SparkContext
from pyspark.streaming import StreamingContext
from pyspark.sql import Row,SQLContext

import sys, os, requests, time, math
import datetime as dt
import yfinance as yf
import numpy as np
import pandas as pd

import tensorflow as tf

tf.compat.v1.logging.set_verbosity(tf.compat.v1.logging.ERROR)


# this method reads in the ticker codes for the S&P 500 companies
def read_tickers(which=None):
    """Reads ticker list"""
    if which != "all":
        return which.split()
    url = 'https://pkgstore.datahub.io/core/s-and-p-500-companies/'+\
        'constituents_json/data/87cab5b5abab6c61eafa6dfdfa068a42/constituents_json.json'
    files = os.listdir()
    if 'ticker_list' not in files:
        t = requests.get(url).json()
        tickers = [d['Symbol'] for d in t if '.' not in d['Symbol']]
        with open('ticker_list', 'w') as f:
            for ticker in tickers:
                f.write(ticker + '\n')
            f.close()
    else:
        tickers = []
        f = open('ticker_list', 'r')
        for line in f:
            tickers.append(line.strip())

    return tickers

# this method gets the previous day data of the stocks, as we need this to construct the data sequence for the LSTM model prediction
def get_prev_day_stocks(tickers, start_date, target_min=5, seq_len=60, feats=['Close', 'Volume']):
    data_all = yf.download(tickers, interval='1m', start=start_date, progress=False, group_by='ticker')
    
    # this is to handle the NaN of the past data
    data_all.dropna(inplace=True)

    # gets the last 60min of the previous day. Additional minutes taken as we are taking several minutes ahead
    last_60min = data_all.iloc[-67:-1,:]
    
    # initialize xs as empty dictionary
    xs = {}

    for t in tickers:
        if len(tickers) > 1:
            data_sub = last_60min[t]
        else:
            data_sub = last_60min

        # generate the past day's data as a data sequence in the same dimensions required by the trained LSTM model
        x_seq = generate_sequences(data_sub, target_min=target_min, seq_len=seq_len, feats=feats)
        
        x_seq = x_seq.tolist()
        
        xs[t] = x_seq
        
    # save the python dict of xs as a txt file
    geeky_file = open('xs_dict.txt', 'wt')
    data = str(xs)
    geeky_file.write(data)
    geeky_file.close()
    
    return xs

# this method generates the data sequence in the dimensions needed by the LSTM model
def generate_sequences(data, target_min=5, seq_len=60, feats=['Close', 'Volume']):
    """Given a subset of data for a particular ticker and date, 
    will return sequences of the appropriate length
    
    Args
        data: subset dataframe
        target_min (optional): target minutes ahead of end of sequence
        seq_len (optional): sequence length to consider
        feats (optional): list of features to keep
    Returns
        seqs: numpy array of sequences (n_seqs, seq_len, len(feats))
    """
    if ((len(data)-seq_len) - target_min ) > 0:
        for i, v in enumerate(range(target_min, len(data)-seq_len)):
            if i == 0:
                x = np.expand_dims(data[feats].values[i:i+seq_len, :], axis=0)
            else:
                z = np.expand_dims(data[feats].values[i:i+seq_len, :], axis=0)
                x = np.concatenate((x, z), axis=0)
    else:
        x = np.ones((1, seq_len, len(feats)))
    
    #print(x.shape)
    return x


# this method gets the spark context
def get_sql_context_instance(spark_context):
    if ('sqlContextSingletonInstance' not in globals()):
        globals()['sqlContextSingletonInstance'] = SQLContext(spark_context)
    return globals()['sqlContextSingletonInstance']


# this method predicts the prices of the stocks with each RDD
def predict_prices(time_in, rdd):
    print("----------- %s -----------" % str(time_in))

    time_start = time.time()

    #try:
    # Get spark sql singleton context from the current context
    sql_context = get_sql_context_instance(rdd.context)
    print('Processing RDD and Predicting Stock Price')

    x_dict = rdd.collectAsMap()

    
    # open text file of dict and store as python dict
    file = open("xs_dict.txt", "r")
    contents = file.read()
    past_data_seq = eval(contents)
    file.close()

    
    for key, values in x_dict.items():
        for main_key, main_values in past_data_seq.items():
            if key == main_key:
                #print(key)
                
                # convert the latest minute's update value from string to dict type
                x_dict_value_toDict = eval(x_dict[key])

                
                # repack as a list item to append to past_data_seq of the ticker
                new_dict_value_list = [x_dict_value_toDict['Close'], x_dict_value_toDict['Volume']]

               
                # drop the oldest element of the ticker's past_data_seq
                new_one_ticker_past_data_seq = past_data_seq[main_key][0][1:]
   
    
                # append the newest minute update from spark rdd stream into the ticker's past_data_seq
                new_one_ticker_past_data_seq.append(new_dict_value_list)
 
    
                past_data_seq[main_key][0] = new_one_ticker_past_data_seq

                
                # change the type of the single ticker sequence into a numpy array
                new_one_ticker_past_data_seq = (np.array(new_one_ticker_past_data_seq) - x_min)/(x_max - x_min)
                new_one_ticker_past_data_seq = np.reshape(new_one_ticker_past_data_seq, (1,60,2))

                pred_price = (model.predict(new_one_ticker_past_data_seq))*(y_max - y_min) + y_min
                

                print('The predicted price of '+  key+ ' is '+ str(pred_price[0][0]))
                
                
    # save the python dict of xs as a txt file
    geeky_file = open('xs_dict.txt', 'wt')
    data = str(past_data_seq)
    geeky_file.write(data)
    geeky_file.close()
    
    time_end = time.time()
    time_taken = time_end - time_start
    print("Time taken to process RDD & Predict Stock is " + str(time_taken) + "s")


# this method finds the min and max that was used to normalize the training data. Need this to convert back to correct price prediction
def find_min_max(data):
    """Loads in training and testing data, finds minimum and maximum for transformation back
    to correct price prediction"""
    arr = np.load(data)
    x_train = arr['x_train']
    y_train = arr['y_train']

    # Subset data to be non-missing
    xmask = np.max(np.isnan(x_train).astype(int), axis=(1,2)) == 0
    x_train = x_train[xmask]
    y_train = y_train[xmask]

    ymask = np.isnan(y_train) == False
    x_train = x_train[ymask]
    y_train = y_train[ymask]

    # Standardize data to improve fit
    x_train_min = x_train.min(axis=0)
    x_train_max = x_train.max(axis=0)
    y_train_min = y_train.min()
    y_train_max = y_train.max()

    return y_train_max, y_train_min, x_train_max, x_train_min



if __name__ == '__main__':
    tickers = read_tickers('all')

    tickers = tickers[0:10]
    #tickers = ['AAPL', 'AMD', 'GOOG']
    date_today = dt.date.today() # - dt.timedelta(days=1)
    prev_day = date_today - dt.timedelta(days=1)
    xs = get_prev_day_stocks(tickers, prev_day, target_min=5, seq_len=60, feats=['Close', 'Volume'])

    # Load in model globally
    model = tf.keras.models.load_model("trained_lstm_mod.h5")

    # Find minimum and maximum for conversion
    y_max, y_min, x_max, x_min = find_min_max("training_data.npz")

    # create spark configuration
    conf = SparkConf()
    conf.setAppName("StockStreamApp")
    # create spark instance with the above configuration
    sc = SparkContext(conf=conf)
    sc.setLogLevel("ERROR")
    # creat the Streaming Context from the above spark context with window size n seconds
    ssc = StreamingContext(sc, 10)#30)
    # read datastream from socket
    dataStream = ssc.socketTextStream("localhost",9009)

    stocks = dataStream.map(lambda line: (line.split(">")[0], line.split(">")[1]))

    # # print in the period
    print("datastream RDD received: ")
    stocks.pprint(10)

    # # do processing for each RDD generated in each interval
    stocks.foreachRDD(predict_prices)

    # start the streaming computation
    ssc.start()
    # wait for the streaming to finish
    ssc.awaitTermination()
