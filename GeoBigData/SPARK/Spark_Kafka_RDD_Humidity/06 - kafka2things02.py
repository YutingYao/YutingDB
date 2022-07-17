import pandas
import json


def toPandasDeserializer(message):
    """
    Deserialize kafka message to pandas.DataFrame
    :param message:
    :return:
    """
    message = message.decode('utf-8')
    messageList = message.split('__')
    if messageList:
        jsonList = [json.loads(x) for x in messageList]
        if type(jsonList[0]) != dict:
            jsonList = [x for sublist in jsonList for x in sublist]
        df = pandas.DataFrame([x['values'] for x in jsonList])
        df.index = [pandas.Timestamp.utcfromtimestamp(int(x['ts']) / 1000.0) for x in jsonList]
    else:
        df = pandas.DataFrame()
    return df


def toThingsboardDeserializer(message):
    """
    Deserialize kafka message to thingsboard publish message.
    :param message:
    :return:
    """
    df = toPandasDeserializer(message)
    dataToSend = []
    for timeIndex in df.index:
        ts = int(timeIndex.tz_localize('israel').timestamp() * 1000)
        dataToSend.append(dict(ts=ts, values=df.loc[timeIndex].to_dict()))
    return json.dumps(dataToSend).encode('utf-8')