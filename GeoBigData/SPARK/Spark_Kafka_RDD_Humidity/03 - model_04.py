from sklearn.model_selection import GridSearchCV

def SGD_tuning(model,X,Y):
    params = {
        "loss" : ["hinge", "log", "squared_hinge", "modified_huber"],
        "alpha" : [0.0001, 0.001, 0.01, 0.1],
        "penalty" : ["l2", "l1", "none"],
    }

    clf = GridSearchCV(model, param_grid=params)

    clf.fit(X, Y)
    print(clf.best_score_)
    print(clf.best_estimator_)

def PAC_tuning(model,X,Y):
    params = {
        "C" : [1,0.1,0.01,0.001,0.00001,0.000001]
    }

    clf = GridSearchCV(model,param_grid=params)

    clf.fit(X,Y)
    print(clf.best_score_)
    print(clf.best_estimator_)

def MLP_tuning(model,X,Y):
    params = {
        "activation" : ["logistic","relu","tanh"],
        "alpha" : [0.001,0.0001,0.00001],
        "solver" : ["adam","lbfgs","sgd"],
        "learning_rate" :["constant","adaptive"]
    }

    clf = GridSearchCV(model,param_grid=params)

    clf.fit(X,Y)
    print(clf.best_score_)
    print(clf.best_estimator_)
