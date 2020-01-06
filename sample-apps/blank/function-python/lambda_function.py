import json
import os
region = os.environ['AWS_REGION']

def lambda_handler(event, context):
    print('Region: ' + region)
    print('All environment variables: ')
    print(os.environ)
    return {
        'status': 'success!'
    }
