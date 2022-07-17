from sklearn.naive_bayes import MultinomialNB
from sklearn import linear_model
from sklearn.neural_network import MLPClassifier
import pickle

class myModel:
    def __init__(self,name,weights):
        self.name = name
        self.weights = weights

def fetchNewModel(model):
    if(model[:3] == 'MNB'):
        return myModel(model,MultinomialNB())
    elif(model[:3] == 'SGD'):
        return myModel(model,linear_model.SGDClassifier(alpha=0.0001,random_state=3,loss='squared_hinge'))
    elif(model[:3] == 'PAC'):
        return myModel(model,linear_model.PassiveAggressiveClassifier(C=0.000001,random_state=3))
    elif(model[:3] == 'MLP'):
        return myModel(model,MLPClassifier(activation='logistic',random_state=3))

def fetchTrainedModel(model):
    if(model[:3] == 'MNB'):
        return myModel(model,pickle.load(open('MNB.sav', 'rb')))
    elif(model[:3] == 'SGD'):
        return myModel(model,pickle.load(open('SGD.sav', 'rb')))
    elif(model[:3] == 'PAC'):
        return myModel(model,pickle.load(open('PAC.sav', 'rb')))
    elif(model[:3] == 'MLP'):
        return myModel(model,pickle.load(open('MLP.sav', 'rb')))
