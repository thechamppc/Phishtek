import numpy as np
from flask import Flask, request, jsonify, render_template
import pickle
#importing the inputScript file used to analyze the URL
import inputScript 
import requests
import json
# NOTE: you must manually set API_KEY below using information retrieved from your IBM Cloud account.
API_KEY = "1JxDGTOsrik4nh3ssatCfpuv42QA2nKp40LqQDwcvGJd"
token_response = requests.post('https://iam.cloud.ibm.com/identity/token', data={"apikey":
API_KEY, "grant_type": 'urn:ibm:params:oauth:grant-type:apikey'})
mltoken = token_response.json()["access_token"]
header = {'Content-Type': 'application/json', 'Authorization': 'Bearer ' + mltoken}


#load model
app = Flask(__name__)
#model = pickle.load(open('Phishing_Website.pkl', 'rb'))

@app.route('/')
def abcd():
    return render_template("fake.html")


#Redirects to the page to give the user input URL.
@app.route('/predict')
def predict():
    return render_template('final.html')

ans = ""   #@akshit
bns = ""   #@ojas

#Fetches the URL given by the URL and passes to inputScript
@app.route('/y_predict',methods=['POST'])
def y_predict():
    '''
    For rendering results on HTML GUI
    '''
    url = request.form['URL']
    checkprediction = inputScript.main(url)
    print(checkprediction)
    payload_scoring = {
        "input_data": [
                {
                        "fields": [
                                "UsingIP",
                                "LongURL",
                                "ShortURL",
                                "Symbol@",
                                "Redirecting//",
                                "PrefixSuffix-",
                                "SubDomains",
                                "HTTPS",
                                "DomainRegLen",
                                "Favicon",
                                "NonStdPort",
                                "HTTPSDomainURL",
                                "RequestURL",
                                "AnchorURL",
                                "LinksInScriptTags",
                                "ServerFormHandler",
                                "InfoEmail",
                                "AbnormalURL",
                                "WebsiteForwarding",
                                "StatusBarCust",
                                "DisableRightClick",
                                "UsingPopupWindow",
                                "IframeRedirection",
                                "AgeofDomain",
                                "DNSRecording",
                                "WebsiteTraffic",
                                "PageRank",
                                "GoogleIndex",
                                "LinksPointingToPage",
                                "StatsReport"
                        ],
                        "values": checkprediction
                }
        ]
}

    response_scoring = requests.post('https://us-south.ml.cloud.ibm.com/ml/v4/deployments/3257146e-830a-4445-89f5-036895fbdc8a/predictions?version=2023-03-31', json=payload_scoring,
    headers={'Authorization': 'Bearer ' + mltoken})
    print("Scoring response")
    print(response_scoring.json())
    print("omg")

    pred = response_scoring.json()
    print(pred)
    
    output = pred['predictions'][0]['values'][0][0]
    print(output)
    
   
    # prediction = model.predict(checkprediction)
    #print(predictions)
    #output=predictions
    if(output==-1):
        pred="You are safe!!  This is a legitimate website."
        return render_template('final.html',bns=pred)
    
    else:
        pred="This is a phishing website. Be cautious!!"        
        return render_template('final.html',ans=pred)
    return output

#Takes the input parameters fetched from the URL by inputScript and returns the predictions
@app.route('/predict_api',methods=['POST'])
def predict_api():
    '''
    For direct API calls trought request
    '''
    data = request.get_json(force=True)
    prediction = model.y_predict([np.array(list(data.values()))])

    output = prediction[0]
    return jsonify(output)

if __name__ == "__main__":
    app.run(debug=False)

