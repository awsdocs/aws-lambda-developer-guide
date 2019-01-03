# Instrumenting Python Code in AWS Lambda<a name="python-tracing"></a>

In Python, you can have Lambda emit subsegments to X\-Ray to show you information about downstream calls to other AWS services made by your function\. To do so, you first need to include the [the AWS X\-Ray SDK for Python](https://docs.aws.amazon.com/xray/latest/devguide/xray-sdk-python.html) in your deployment package\. In addition, you can patch the `boto3` \(or `botocore` if you are using sessions\), so any client you create to access other AWS services will automatically be traced by X\-Ray\.

```
import boto3
from aws_xray_sdk.core import xray_recorder
from aws_xray_sdk.core import patch

patch(['boto3'])
```

Once you've patched the module you are using to create clients, you can use it to create your traced clients, in the case below Amazon S3:

```
s3_client = boto3.client('s3')
```

The X\-Ray SDK for Python creates a subsegment for the call and records information from the request and response\. You can use the `aws_xray_sdk_sdk.core.xray_recorder` to create subsegments automatically by decorating your Lambda functions or manually by calling `xray_recorder.begin_subsegment()` and `xray_recorder.end_subsegment()` inside the function, as shown in the following Lambda function\.

```
import boto3
from aws_xray_sdk.core import xray_recorder
from aws_xray_sdk.core import patch

patch(['boto3'])

s3_client = boto3.client('s3')

def lambda_handler(event, context):
    bucket_name = event['bucket_name']
    bucket_key = event['bucket_key']
    body = event['body']

    put_object_into_s3(bucket_name, bucket_key, body)
    get_object_from_s3(bucket_name, bucket_key)

# Define subsegments manually
def put_object_into_s3(bucket_name, bucket_key, body):
    try:
        xray_recorder.begin_subsegment('put_object')
        response = s3_client.put_object(Bucket=bucket_name, Key=bucket_key, Body=body)
        status_code = response['ResponseMetadata']['HTTPStatusCode']
        xray_recorder.current_subsegment().put_annotation('put_response', status_code)
    finally:
        xray_recorder.end_subsegment()

# Use decorators to automatically set the subsegments
@xray_recorder.capture('get_object')
def get_object_from_s3(bucket_name, bucket_key):
    response = s3_client.get_object(Bucket=bucket_name, Key=bucket_key)
    status_code = response['ResponseMetadata']['HTTPStatusCode']
    xray_recorder.current_subsegment().put_annotation('get_response', status_code)
```

**Note**  
The X\-Ray SDK for Python allows you to patch the following modules:  
botocore
boto3
requests
sqlite3
mysql
You can use `patch_all()` to patch all of them at once\. 

Following is what a trace emitted by the code preceding looks like \(synchronous invocation\): 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/AWS_X_Ray_Python.png)