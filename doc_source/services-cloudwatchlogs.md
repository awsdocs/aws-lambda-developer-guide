# Using Lambda with CloudWatch Logs<a name="services-cloudwatchlogs"></a>

You can use a Lambda function to monitor and analyze logs from an Amazon CloudWatch Logs log stream\. Create [subscriptions](https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/Subscriptions.html) for one or more log streams to invoke a function when logs are created or match an optional pattern\. Use the function to send a notification or persist the log to a database or storage\.

CloudWatch Logs invokes your function asynchronously with an event that contains log data\. The value of the data field is a Base64\-encoded \.gzip file archive\.

**Example CloudWatch Logs message event**  

```
{
  "awslogs": {
    "data": "ewogICAgIm1lc3NhZ2VUeXBlIjogIkRBVEFfTUVTU0FHRSIsCiAgICAib3duZXIiOiAiMTIzNDU2Nzg5MDEyIiwKICAgICJsb2dHcm91cCI6I..."
  }
}
```

When decoded and decompressed, the log data is a JSON document with the following structure:

**Example CloudWatch Logs message data \(decoded\)**  

```
{
    "messageType": "DATA_MESSAGE",
    "owner": "123456789012",
    "logGroup": "/aws/lambda/echo-nodejs",
    "logStream": "2019/03/13/[$LATEST]94fa867e5374431291a7fc14e2f56ae7",
    "subscriptionFilters": [
        "LambdaStream_cloudwatchlogs-node"
    ],
    "logEvents": [
        {
            "id": "34622316099697884706540976068822859012661220141643892546",
            "timestamp": 1552518348220,
            "message": "REPORT RequestId: 6234bffe-149a-b642-81ff-2e8e376d8aff\tDuration: 46.84 ms\tBilled Duration: 47 ms \tMemory Size: 192 MB\tMax Memory Used: 72 MB\t\n"
        }
    ]
}
```

For a sample application that uses CloudWatch Logs as a trigger, see [Error processor sample application for AWS Lambda](samples-errorprocessor.md)\.