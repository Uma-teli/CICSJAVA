import sys
import json

WORKSPACE = sys.argv[2]
with open(WORKSPACE +'/extracted_data.json', 'r') as f:
    data = json.load(f)

print(data[sys.argv[1]])
