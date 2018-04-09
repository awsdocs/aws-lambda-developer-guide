# Logging AWS Lambda API Calls By Using AWS CloudTrail<a name="logging-using-cloudtrail"></a>

AWS Lambda is integrated with AWS CloudTrail, a service that captures API calls made by or on behalf of AWS Lambda in your AWS account and delivers the log files to an Amazon S3 bucket that you specify\. CloudTrail captures API calls made from the AWS Lambda console or from the AWS Lambda API\. Using the information collected by CloudTrail, you can determine what request was made to AWS Lambda, the source IP address from which the request was made, who made the request, when it was made, and so on\. To learn more about CloudTrail, including how to configure and enable it, see the [http://docs.aws.amazon.com/awscloudtrail/latest/userguide/](http://docs.aws.amazon.com/awscloudtrail/latest/userguide/)\.

## AWS Lambda Information in CloudTrail<a name="service-name-info-in-cloudtrail"></a>

When CloudTrail logging is enabled in your AWS account, API calls made to AWS Lambda actions are tracked in log files\. AWS Lambda records are written together with other AWS service records in a log file\. CloudTrail determines when to create and write to a new file based on a time period and file size\.

The following actions are supported:
+ [AddPermission](API_AddPermission.md)
+ [CreateEventSourceMapping](API_CreateEventSourceMapping.md)
+ [CreateFunction](API_CreateFunction.md)

  \(The `ZipFile` parameter is omitted from the CloudTrail logs for `CreateFunction`\.\)
+ [DeleteEventSourceMapping](API_DeleteEventSourceMapping.md)
+ [DeleteFunction](API_DeleteFunction.md)
+ [GetEventSourceMapping](API_GetEventSourceMapping.md)
+ [GetFunction](API_GetFunction.md)
+ [GetFunctionConfiguration](API_GetFunctionConfiguration.md)
+ [GetPolicy](API_GetPolicy.md)
+ [ListEventSourceMappings](API_ListEventSourceMappings.md)
+ [ListFunctions](API_ListFunctions.md)
+ [RemovePermission](API_RemovePermission.md)
+ [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md)
+ [UpdateFunctionCode](API_UpdateFunctionCode.md)

  \(The `ZipFile` parameter is omitted from the CloudTrail logs for `UpdateFunctionCode`\.\)
+ [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)

Every log entry contains information about who generated the request\. The user identity information in the log helps you determine whether the request was made with root or IAM user credentials, with temporary security credentials for a role or federated user, or by another AWS service\. For more information, see the **userIdentity** field in the [CloudTrail Event Reference](http://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-event-reference.html)\.

You can store your log files in your bucket for as long as you want, but you can also define Amazon S3 lifecycle rules to archive or delete log files automatically\. By default, your log files are encrypted by using Amazon S3 server\-side encryption \(SSE\)\.

You can choose to have CloudTrail publish Amazon SNS notifications when new log files are delivered if you want to take quick action upon log file delivery\. For more information, see [Configuring Amazon SNS Notifications for CloudTrail](http://docs.aws.amazon.com/awscloudtrail/latest/userguide/configure-sns-notifications-for-cloudtrail.html)\.

You can also aggregate AWS Lambda log files from multiple AWS regions and multiple AWS accounts into a single S3 bucket\. For more information, see [Working with CloudTrail Log Files](http://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-working-with-log-files.html)\.

## Understanding AWS Lambda Log File Entries<a name="understanding-service-name-entries"></a>

CloudTrail log files contain one or more log entries where each entry is made up of multiple JSON\-formatted events\. A log entry represents a single request from any source and includes information about the requested action, any parameters, the date and time of the action, and so on\. The log entries are not guaranteed to be in any particular order\. That is, they are not an ordered stack trace of the public API calls\.

The following example shows CloudTrail log entries for the `GetFunction` and `DeleteFunction` actions\.

```
{
  "Records": [
    {
      "eventVersion": "1.03",
      "userIdentity": {
        "type": "IAMUser",
        "principalId": "A1B2C3D4E5F6G7EXAMPLE",
        "arn": "arn:aws:iam::999999999999:user/myUserName",
        "accountId": "999999999999",
        "accessKeyId": "AKIAIOSFODNN7EXAMPLE",
        "userName": "myUserName"
      },
      "eventTime": "2015-03-18T19:03:36Z",
      "eventSource": "lambda.amazonaws.com",
      "eventName": "GetFunction",
      "awsRegion": "us-east-1",
      "sourceIPAddress": "127.0.0.1",
      "userAgent": "Python-httplib2/0.8 (gzip)",
      "errorCode": "AccessDenied",
      "errorMessage": "User: arn:aws:iam::999999999999:user/myUserName" is not authorized to perform: lambda:GetFunction on resource: arn:aws:lambda:us-west-2:999999999999:function:other-acct-function",
      "requestParameters": null,
      "responseElements": null,
      "requestID": "7aebcd0f-cda1-11e4-aaa2-e356da31e4ff",
      "eventID": "e92a3e85-8ecd-4d23-8074-843aabfe89bf",
      "eventType": "AwsApiCall",
      "recipientAccountId": "999999999999"
    },
    {
      "eventVersion": "1.03",
      "userIdentity": {
        "type": "IAMUser",
        "principalId": "A1B2C3D4E5F6G7EXAMPLE",
        "arn": "arn:aws:iam::999999999999:user/myUserName",
        "accountId": "999999999999",
        "accessKeyId": "AKIAIOSFODNN7EXAMPLE",
        "userName": "myUserName"
      },
      "eventTime": "2015-03-18T19:04:42Z",
      "eventSource": "lambda.amazonaws.com",
      "eventName": "DeleteFunction",
      "awsRegion": "us-east-1",
      "sourceIPAddress": "127.0.0.1",
      "userAgent": "Python-httplib2/0.8 (gzip)",
      "requestParameters": {
        "functionName": "basic-node-task"
      },
      "responseElements": null,
      "requestID": "a2198ecc-cda1-11e4-aaa2-e356da31e4ff",
      "eventID": "20b84ce5-730f-482e-b2b2-e8fcc87ceb22",
      "eventType": "AwsApiCall",
      "recipientAccountId": "999999999999"
    }
  ]
}
```

**Note**  
The `eventName` may include date and version information, such as `"GetFunction20150331"`, but it is still referring to the same public API\.

## Using CloudTrail to Track Function Invocations<a name="tracking-function-invocations"></a>

CloudTrail also logs data events\. You can turn on data event logging so that you log an event every time Lambda functions are invoked\. This helps you understand what identities are invoking the functions and the frequency of their invocations\. This feature is not enabled by default and incurs additional charges if enabled\. You can do this using the AWS CloudTrail console or [Invoke](API_Invoke.md) CLI operation\. For more information on this option, see [ Logging Data and Management Events for Trails](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/logging-management-and-data-events-with-cloudtrail.html)\.