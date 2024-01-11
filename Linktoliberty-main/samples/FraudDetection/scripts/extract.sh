#!/bin/env python
import json
import requests
import sys

WORKSPACE = sys.argv[2]
PREV_WORKSPACE = sys.argv[3]

with open(PREV_WORKSPACE + '/token.json') as f:
    data = json.load(f)

token = data['token']

with open(PREV_WORKSPACE +'/extracted_userData.json') as f:
    data = json.load(f)

MainframeIP = data['MainframeIP']
Username = data['username']
print("MainframeIP:",MainframeIP)

header = {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token,
            'ML-Instance-Id' : Username
}

model_version = int(sys.argv[1]) -1
target_model_name = "Cobol-deploy-v" + str(model_version)

response = requests.get("https://" + MainframeIP + ":11442/v3/deployments", headers=header,verify=False)

data = json.loads(response.text)

for index, resource in enumerate(data["resources"]):
    model_name = resource["entity"]["name"]
    if model_name == target_model_name:
        Index = index
        break

#artifact_version = (json.loads(response.text)['resources'][Index]['entity']['latest_version']['url'][27:])

deploy_id = (json.loads(response.text)['resources'][Index]['metadata']['guid'])

#deployment_urls = (json.loads(response.text)['resources'][Index]['entity']['deployments']['url'])

#model_version = (json.loads(response.text)['resources'][Index]['entity']['versionSeq'])

#extracted_val = {"artifact_version": artifact_version, "model_url": deployment_urls, "model_id": deploy_id, "model_name": target_model_name, "model_version": model_version}
extracted_val = {"model_id": deploy_id}

with open(WORKSPACE + '/samples/FraudDetection/scripts/extracted_data_model.json', 'w') as f:
    json.dump(extracted_val, f)

