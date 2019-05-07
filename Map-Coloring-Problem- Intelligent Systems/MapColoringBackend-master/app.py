from flask import Flask, request

import ForwardChecking
import BackTracking
import SingletonFC
import Singeton_LCV_AUS
import Singleton_MRV_AUS
import SingletonFC_LCV
import SingletonFC_MRV
import ForwardChecking_MRV
import ForwardChecking_LCV
import ForwardChecking_LCV_AUS
import ForwardChecking_MRV_AUS
from dump_json import *
from flask_cors import CORS
import importlib

app = Flask(__name__)
CORS(app)


@app.route('/forward-check', methods=['POST', 'GET'])
def forward_checking1():
    importlib.reload(ForwardChecking)
    json_obj = ForwardChecking.main()
    json_obj = json_dump(json_obj)
    return json_obj


@app.route('/backtracking')
def backtracking1():
    importlib.reload(BackTracking)
    json_obj = BackTracking.main()
    json_obj = json_dump(json_obj)
    return json_obj


@app.route('/singleton')
def singleton():
    importlib.reload(SingletonFC)
    json_obj = SingletonFC.main()
    json_obj = json_dump(json_obj)
    return json_obj


@app.route('/forward-check-mrv')
def forward_checking1_mrv():
    importlib.reload(ForwardChecking_MRV)
    json_obj = ForwardChecking_MRV.main()
    importlib.reload(ForwardChecking_MRV)
    json_obj = json_dump(json_obj)
    return json_obj


@app.route('/forward-check-lcv')
def forward_checking1_lcv():
    importlib.reload(ForwardChecking_LCV)
    json_obj = ForwardChecking_LCV.main()
    json_obj = json_dump(json_obj)
    return json_obj


@app.route('/forward-check/aus')
def forward_check_aus():
    importlib.reload(ForwardChecking)
    json_obj = ForwardChecking.main("aus")
    json_obj = json_dump(json_obj)
    return json_obj


@app.route('/backtracking/aus')
def backtracking_aus():
    importlib.reload(BackTracking)
    json_obj = BackTracking.main("aus")
    json_obj = json_dump(json_obj)
    return json_obj


@app.route('/singleton/aus')
def singleton_aus():
    importlib.reload(SingletonFC)
    json_obj = SingletonFC.main("aus")
    json_obj = json_dump(json_obj)
    return json_obj


@app.route('/singleton-mrv')
def singleton_mrv():
    importlib.reload(SingletonFC_MRV)
    json_obj = SingletonFC_MRV.main()
    json_obj = json_dump(json_obj)
    return json_obj


@app.route('/singleton-lcv')
def singleton_lcv():
    importlib.reload(SingletonFC_LCV)
    json_obj = SingletonFC_LCV.main()
    json_obj = json_dump(json_obj)
    return json_obj


@app.route('/singleton-mrv/aus')
def singleton_aus_mrv():
    importlib.reload(Singleton_MRV_AUS)
    json_obj = Singleton_MRV_AUS.main("aus")
    json_obj = json_dump(json_obj)
    return json_obj


@app.route('/singleton-lcv/aus')
def singleton_aus_lcv():
    importlib.reload(Singeton_LCV_AUS)
    json_obj = Singeton_LCV_AUS.main("aus")
    json_obj = json_dump(json_obj)
    return json_obj


@app.route('/forward-check-mrv/aus')
def forward_checking1_mrv_aus():
    importlib.reload(ForwardChecking_MRV_AUS)
    json_obj = ForwardChecking_MRV_AUS.main("aus")
    importlib.reload(ForwardChecking_MRV_AUS)
    json_obj = json_dump(json_obj)
    return json_obj


@app.route('/forward-check-lcv/aus')
def forward_checking1_lcv_aus():
    importlib.reload(ForwardChecking_LCV_AUS)
    json_obj = ForwardChecking_LCV_AUS.main("aus")
    json_obj = json_dump(json_obj)
    return json_obj


if __name__ == "__main__":
    app.run(host='0.0.0.0', port='80')
