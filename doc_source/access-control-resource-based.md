# Using Resource\-Based Policies for AWS Lambda \(Lambda Function Policies\)<a name="access-control-resource-based"></a>

A Lambda function is one of the resources in AWS Lambda\. You can add permissions to the policy associated with a Lambda function\. Permissions policies attached to Lambda functions are referred to as *resource\-based policies* \(or *Lambda function policies* in Lambda\)\. You use Lambda function policies to manage Lambda function invocation permissions \(see [Invoke](API_Invoke.md)\)\. 

**Important**  
Before you create resource\-based policies, we recommend that you first review the introductory topics that explain the basic concepts and options available for you to manage access to your AWS Lambda resources\. For more information, see [Overview of Managing Access Permissions to Your AWS Lambda Resources](access-control-overview.md)\.

Lambda function policies are primarily used when you are setting up an event source in AWS Lambda to grant a service or an event source permissions to invoke your Lambda function \(see [Invoke](API_Invoke.md)\)\. An exception to this is when an event source \(for example, Amazon DynamoDB or Kinesis\) uses the pull model, where permissions are managed in the Lambda function execution role instead\. For more information, see [Event Source Mapping](invocation-options.md#intro-invocation-modes)\. 

Lambda function policies also make it easy to grant cross\-account permissions to invoke your Lambda function\. Suppose you want to grant cross\-account permissions \(for example, permissions to Amazon S3\) to invoke your Lambda function\. Instead of creating an IAM role to grant cross\-account permissions, you can add the relevant permissions in a Lambda function policy\. 

**Note**  
If the custom application and the Lambda function it invokes belong to the same AWS account, you don't need to grant explicit permissions using the policy attached to the Lambda function\.

 AWS Lambda provides the following API operations to manage a permissions policy associated with a Lambda function:
+ [AddPermission](API_AddPermission.md)
+ [GetPolicy](API_GetPolicy.md)
+ [RemovePermission](API_RemovePermission.md)

**Note**  
The AWS Lambda console is the easiest way to manage event sources and their permissions in a Lambda function policy\. If the AWS service console for the event source supports configuring event source mapping, you can use that console too\. As you configure new event sources or modify existing event sources, the console automatically modifies the permissions policy associated with the Lambda function\. 

You can use the console to view your function policy under the **Configuration** tab and then choosing the **key** icon\. The console doesn't support directly modifying permissions in a function policy\. You must use either the AWS CLI or the AWS SDKs\. The following are AWS CLI examples of the API operations listed earlier in this topic:

**Topics**
+ [Example 1: Allow Amazon S3 to Invoke a Lambda Function](#access-control-resource-based-example-s3-invoke-function)
+ [Example 2: Allow Amazon API Gateway to Invoke a Lambda Function](#access-control-resource-based-example-apigateway-invoke-function)
+ [Example 3: Allow a User Application Created by Another AWS Account to Invoke a Lambda Function \(Cross\-Account Scenario\)](#access-control-resource-based-example-cross-account-scenario)
+ [Example 4: Retrieve a Lambda Function Policy](#access-control-resource-based-example-retrieve-function-policy)
+ [Example 5: Remove Permissions from a Lambda Function Policy](#access-control-resource-based-example-remove-permissions-function-policy)
+ [Example 6: Working with Lambda Function Versioning, Aliases, and Permissions](#access-control-resource-based-example-versioning-aliases-function-policy)

## Example 1: Allow Amazon S3 to Invoke a Lambda Function<a name="access-control-resource-based-example-s3-invoke-function"></a>

To grant Amazon S3 permission to invoke a Lambda function, you configure permissions as follows:
+ Specify `s3.amazonaws.com` as the `principal` value\.
+ Specify `lambda:InvokeFunction` as the `action` for which you are granting permissions\.

To ensure that the event is generated from a specific bucket that is owned by a specific AWS account, you also specify the following:
+ Specify the bucket ARN as the `source-arn` value to restrict events from a specific bucket\.
+ Specify the AWS account ID that owns the bucket, to ensure that the named bucket is owned by the account\.

The following example AWS CLI command adds a permission to the `helloworld` Lambda function policy granting Amazon S3 permissions to invoke the function\. 

```
aws lambda add-permission \
--region region \
--function-name helloworld \
--statement-id 1 \
--principal s3.amazonaws.com \
--action lambda:InvokeFunction \
--source-arn arn:aws:s3:::examplebucket \
--source-account 111111111111 \
--profile adminuser
```

The example assumes that the `adminuser` \(who has full permissions\) is adding this permission\. Therefore, the `--profile` parameter specifies the `adminuser` profile\.

In response, AWS Lambda returns the following JSON code\. The `Statement` value is a JSON string version of the statement added to the Lambda function policy\.

```
{
    "Statement": "{\"Condition\":{\"StringEquals\":{\"AWS:SourceAccount\":\"111111111111\"},
                   \"ArnLike\":{\"AWS:SourceArn\":\"arn:aws:s3:::examplebucket\"}},
                  \"Action\":[\"lambda:InvokeFunction\"],
                  \"Resource\":\"arn:aws:lambda:us-west-2:111111111111:function:helloworld\",
                  \"Effect\":\"Allow\",\"Principal\":{\"Service\":\"s3.amazonaws.com\"},
                  \"Sid\":\"1\"}"
}
```

For information about the push model, see [Event Source Mapping](invocation-options.md#intro-invocation-modes)\.

## Example 2: Allow Amazon API Gateway to Invoke a Lambda Function<a name="access-control-resource-based-example-apigateway-invoke-function"></a>

To grant permissions to allow Amazon API Gateway to invoke a Lambda function, do the following:
+ Specify `apigateway.amazonaws.com` as the `principal` value\.
+ Specify `lambda:InvokeFunction` as the action for which you are granting permissions\.
+ Specify the API Gateway endpoint ARN as the `source-arn` value\.

The following example AWS CLI command adds a permission to the `helloworld` Lambda function policy granting API Gateway permissions to invoke the function\. 

```
aws lambda add-permission \
--region region \
--function-name helloworld \
--statement-id 5 \
--principal apigateway.amazonaws.com \
--action lambda:InvokeFunction \
--source-arn arn:aws:execute-api:region:account-id:api-id/stage/method/resource-path \
--profile adminuser
```

In response, AWS Lambda returns the following JSON code\. The `Statement` value is a JSON string version of the statement added to the Lambda function policy\.

```
{
    "Statement": "{\"Condition\":{\"ArnLike\":{\"AWS:SourceArn\":\"arn:aws:apigateway:us-east-1::my-api-id:/test/petstorewalkthrough/pets\"}},
                  \"Action\":[\"lambda:InvokeFunction\"],
                  \"Resource\":\"arn:aws:lambda:us-west-2:account-id:function:helloworld\",
                  \"Effect\":\"Allow\",
                  \"Principal\":{\"Service\":\"apigateway.amazonaws.com\"},
                  \"Sid\":\"5\"}"
}
```

## Example 3: Allow a User Application Created by Another AWS Account to Invoke a Lambda Function \(Cross\-Account Scenario\)<a name="access-control-resource-based-example-cross-account-scenario"></a>

To grant permissions to another AWS account \(that is, to create a cross\-account scenario\), you specify the AWS account ID as the `principal` value as shown in the following AWS CLI command: 

```
aws lambda add-permission \
--region region \
--function-name helloworld \
--statement-id 3 \
--principal 111111111111 \
--action lambda:InvokeFunction \
--profile adminuser
```

In response, AWS Lambda returns the following JSON code\. The `Statement` value is a JSON string version of the statement added to the Lambda function policy\.

```
{
    "Statement": "{\"Action\":[\"lambda:InvokeFunction\"],
                   \"Resource\":\"arn:aws:lambda:us-west-2:account-id:function:helloworld\",
                   \"Effect\":\"Allow\",
                   \"Principal\":{\"AWS\":\"account-id\"},
                   \"Sid\":\"3\"}"
}
```

## Example 4: Retrieve a Lambda Function Policy<a name="access-control-resource-based-example-retrieve-function-policy"></a>

To retrieve your Lambda function policy, you use the `get-policy` command: 

```
aws lambda get-policy \
--function-name example \
--profile adminuser
```

## Example 5: Remove Permissions from a Lambda Function Policy<a name="access-control-resource-based-example-remove-permissions-function-policy"></a>

To remove permissions from your Lambda function policy, you use the `remove-permission` command, specifying the function name and statement ID: 

```
aws lambda remove-permission \
--function-name example \
--statement-id 1 \
--profile adminuser
```

## Example 6: Working with Lambda Function Versioning, Aliases, and Permissions<a name="access-control-resource-based-example-versioning-aliases-function-policy"></a>

For more information about permissions policies for Lambda function versions and aliases, see [Versioning, Aliases, and Resource Policies](versioning-aliases-permissions.md)\.