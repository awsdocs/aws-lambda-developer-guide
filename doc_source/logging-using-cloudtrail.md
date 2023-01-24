# Logging Lambda API calls with CloudTrail<a name="logging-using-cloudtrail"></a>

Lambda is integrated with AWS CloudTrail, a service that provides a record of actions taken by a user, role, or an AWS service in Lambda\. CloudTrail captures API calls for Lambda as events\. The calls captured include calls from the Lambda console and code calls to the Lambda API operations\. If you create a trail, you can enable continuous delivery of CloudTrail events to an Amazon Simple Storage Service \(Amazon S3\) bucket, including events for Lambda\. If you don't configure a trail, you can still view the most recent events in the CloudTrail console in **Event history**\. Using the information collected by CloudTrail, you can determine the request that was made to Lambda, the IP address from which the request was made, who made the request, when it was made, and additional details\.

For more information about CloudTrail, including how to configure and enable it, see the [AWS CloudTrail User Guide](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/)\.

## Lambda information in CloudTrail<a name="service-name-info-in-cloudtrail"></a>

CloudTrail is enabled on your AWS account when you create the account\. When supported event activity occurs in Lambda, that activity is recorded in a CloudTrail event along with other AWS service events in **Event history**\. You can view, search, and download recent events in your AWS account\. For more information, see [Viewing events with CloudTrail event history](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/view-cloudtrail-events.html) in the *AWS CloudTrail User Guide*\. 

For an ongoing record of events in your AWS account, including events for Lambda, you create a *trail*\. A trail enables CloudTrail to deliver log files to an Amazon S3 bucket\. By default, when you create a trail in the console, the trail applies to all AWS Regions\. The trail logs events from all Regions in the AWS partition and delivers the log files to the S3 bucket that you specify\. Additionally, you can configure other AWS services to further analyze and act upon the event data collected in CloudTrail logs\. 

For more information, see the following topics in the *AWS CloudTrail User Guide*: 
+ [Overview for creating a trail](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-create-and-update-a-trail.html)
+ [CloudTrail supported services and integrations](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-aws-service-specific-topics.html#cloudtrail-aws-service-specific-topics-integrations)
+ [Configuring Amazon SNS notifications for CloudTrail](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/getting_notifications_top_level.html)
+ [Receiving CloudTrail log files from multiple regions](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/receive-cloudtrail-log-files-from-multiple-regions.html) and [Receiving CloudTrail log files from multiple accounts](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-receive-logs-from-multiple-accounts.html)

Every log entry contains information about who generated the request\. The user identity information in the log helps you determine whether the request was made with root or AWS Identity and Access Management \(IAM\) user credentials, with temporary security credentials for a role or federated user, or by another AWS service\. For more information, see the **userIdentity** field in the [CloudTrail event reference](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-event-reference.html)\.

You can store your log files in your bucket for as long as you want, but you can also define Amazon S3 lifecycle rules to archive or delete log files automatically\. By default, your log files are encrypted by using Amazon S3 server\-side encryption \(SSE\)\.

You can choose to have CloudTrail publish Amazon Simple Notification Service \(Amazon SNS\) notifications when new log files are delivered if you want to take quick action upon log file delivery\. For more information, see [Configuring Amazon SNS notifications for CloudTrail](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/configure-sns-notifications-for-cloudtrail.html)\.

You can also aggregate Lambda log files from multiple Regions and multiple AWS accounts into a single S3 bucket\. For more information, see [Working with CloudTrail log files](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/cloudtrail-working-with-log-files.html)\.

### List of supported Lambda API actions<a name="list-of-supported-actions"></a>

Lambda supports logging the following actions as events in CloudTrail log files\.

**Note**  
In the CloudTrail log file, the `eventName` might include date and version information, but it is still referring to the same public API\. For example the, `GetFunction` action might appear as `"GetFunction20150331"`\. To see the `eventName` for a particular action, view a log file entry in your event history\. For more information, see [Viewing events with CloudTrail event history](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/view-cloudtrail-events.html) in the *AWS CloudTrail User Guide*\. 
+ [AddLayerVersionPermission](API_AddLayerVersionPermission.md)
+ [AddPermission](API_AddPermission.md)
+ [CreateEventSourceMapping](API_CreateEventSourceMapping.md)
+ [CreateFunction](API_CreateFunction.md)

  \(The `Environment` and `ZipFile` parameters are omitted from the CloudTrail logs for `CreateFunction`\.\)
+ [CreateFunctionUrlConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_CreateFunctionUrlConfig.html)
+ [DeleteEventSourceMapping](API_DeleteEventSourceMapping.md)
+ [DeleteFunction](API_DeleteFunction.md)
+ [DeleteFunctionUrlConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_DeleteFunctionUrlConfig.html)
+ [GetEventSourceMapping](API_GetEventSourceMapping.md)
+ [GetFunction](API_GetFunction.md)
+ [GetFunctionUrlConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_GetFunctionUrlConfig.html)
+ [GetFunctionConfiguration](API_GetFunctionConfiguration.md)
+ [GetLayerVersionPolicy](API_GetLayerVersionPolicy.md)
+ [GetPolicy](API_GetPolicy.md)
+ [ListEventSourceMappings](API_ListEventSourceMappings.md)
+ [ListFunctions](API_ListFunctions.md)
+ [ListFunctionUrlConfigs](https://docs.aws.amazon.com/lambda/latest/dg/API_ListFunctionUrlConfigs.html)
+ [PublishLayerVersion](API_PublishLayerVersion.md)

  \(The `ZipFile` parameter is omitted from the CloudTrail logs for `PublishLayerVersion`\.\)
+ [RemovePermission](API_RemovePermission.md)
+ [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md)
+ [UpdateFunctionCode](API_UpdateFunctionCode.md)

  \(The `ZipFile` parameter is omitted from the CloudTrail logs for `UpdateFunctionCode`\.\)
+ [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)

  \(The `Environment` parameter is omitted from the CloudTrail logs for `UpdateFunctionConfiguration`\.\)
+ [UpdateFunctionUrlConfig](https://docs.aws.amazon.com/lambda/latest/dg/API_UpdateFunctionUrlConfig.html)

## Understanding Lambda log file entries<a name="understanding-service-name-entries"></a>

CloudTrail log files contain one or more log entries where each entry is made up of multiple JSON\-formatted events\. A log entry represents a single request from any source and includes information about the requested action, any parameters, the date and time of the action, and so on\. The log entries are not guaranteed to be in any particular order\. That is, they are not an ordered stack trace of the public API calls\.

The following example shows CloudTrail log entries for the `GetFunction` and `DeleteFunction` actions\.

**Note**  
The `eventName` might include date and version information, such as `"GetFunction20150331"`, but it is still referring to the same public API\. 

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
      "errorMessage": "User: arn:aws:iam::999999999999:user/myUserName is not authorized to perform: lambda:GetFunction on resource: arn:aws:lambda:us-west-2:999999999999:function:other-acct-function",
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

## Using CloudTrail to track function invocations<a name="tracking-function-invocations"></a>

CloudTrail also logs data events\. You can turn on data event logging so that you log an event every time Lambda functions are invoked\. This helps you understand what identities are invoking the functions and the frequency of their invocations\. For more information on this option, see [Logging data events for trails](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/logging-data-events-with-cloudtrail.html)\.

**Note**  
CloudTrail logs only authenticated and authorized requests\. CloudTrail does not log requests that fail authentication \(credentials are missing or the provided credentials are not valid\) or requests with credentials that are not authorized to invoke the function\.

### Using CloudTrail to troubleshoot disabled event sources<a name="tracking-function-invocations-disabled"></a>

One data event that can be encountered is a `LambdaESMDisabled` event\. There are five general categories of error that are associated with this event:

**`RESOURCE_NOT_FOUND`**  
The resource specified in the request does not exist\.

**`FUNCTION_NOT_FOUND`**  
The function attached to the event source does not exist\.

**`REGION_NAME_NOT_VALID`**  
A Region name provided to the event source or function is invalid\.

**`AUTHORIZATION_ERROR`**  
Permissions have not been set, or are misconfigured\.

**`FUNCTION_IN_FAILED_STATE`**  
The function code does not compile, has encountered an unrecoverable exception, or a bad deployment has occurred\.

These errors are included in the CloudTrail event message within the `serviceEventDetails` entity\.

**Example `serviceEventDetails` entity**  

```
 "serviceEventDetails":{
    "ESMDisableReason":"Lambda Function not found"
}
```