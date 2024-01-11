#!/bin/env python
import json
import sys

WORKSPACE = sys.argv[1]
PREV_WORKSPACE = sys.argv[2]

with open(PREV_WORKSPACE + '/scoringURL.json') as f:
    data = json.load(f)

SCORING_URL = data["scoring_url"]

with open(WORKSPACE + '/samples/FraudDetection/scripts/oldScoringURL.json') as f:
    oldData = json.load(f)

DEPLOY_ID = oldData["old_depID"]
#print(DEPLOY_ID)

filename = WORKSPACE + "/samples/FraudDetection/cobol/FRAUDMOD.cbl"
new_value = SCORING_URL.split("/")[-1]
old_value = DEPLOY_ID

print("Old scoring end point:",old_value)
print("New scoring end point:",new_value)

with open(filename, "r") as file:
    content = file.read()

content = content.replace(old_value, new_value)
print(content)

with open(filename, "w") as file:
    file.write(content)

print(file)

data = {}
data["old_depID"] = new_value
with open(WORKSPACE + '/samples/FraudDetection/scripts/oldScoringURL.json','w') as f:
    json.dump(data,f)

