# Python<a name="with-ddb-example-deployment-pkg-python"></a>

1. Open a text editor, and then copy the following code\. 
**Note**  
The `from __future__` statement enables you to write code that is compatible with Python 2 or 3\. If you are using runtime version 3\.6, it is not necessary to include it\.

   ```
   from __future__ import print_function
   
   def lambda_handler(event, context):
       for record in event['Records']:
           print(record['eventID'])
           print(record['eventName'])       
       print('Successfully processed %s records.' % str(len(event['Records'])))
   ```

1. Save the file as ` ProcessDynamoDBStream.py`\.

1. Zip the ` ProcessDynamoDBStream.py` file as ` ProcessDynamoDBStream.zip`\. 

## Next Step<a name="ddb-create-deployment-pkg-python-next-step"></a>

 [Create the Execution Role \(IAM Role\)](with-dynamodb-create-execution-role.md) 