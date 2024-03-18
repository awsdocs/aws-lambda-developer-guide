import json
import numpy as np

def lambda_handler(event, context):
    
    x = np.arange(15, dtype=np.int64).reshape(3, 5)
    print(x)
    
    return {
        'statusCode': 200,
        'body': json.dumps('Hello from Lambda!')
    }
