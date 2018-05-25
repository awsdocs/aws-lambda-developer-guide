# Permissions Required to Use the AWS Lambda Console<a name="console-specific-permissions"></a>

To take advantage of the integrated experience provided by the AWS Lambda console, a user must often have more permissions than the API\-specific permissions described in the references table, depending on what you want the user to be able to do\. For more information about Lambda API operations, see [Lambda API Permissions: Actions, Resources, and Conditions Reference](lambda-api-permissions-ref.md)\. 

For example, suppose you allow an IAM user in your account permissions to create a Lambda function to process Amazon S3 object\-created events\. To enable the user to configure Amazon S3 as the event source, the console drop\-down list will display a list of your buckets\. However, the console can show the bucket list only if the signed\-in user has permissions for the relevant Amazon S3 actions\.

The following sections describe required additional permissions for different integration points\. 

If you are new to managing permissions, we recommend that you start with the example walkthrough where you create an IAM user, grant the user incremental permissions, and verify the permissions work using the AWS Lambda console \(see [Customer Managed Policy Examples](access-control-identity-based.md#access-policy-examples-for-sdk-cli)\)\. 

**Topics**
+ [Amazon API Gateway](#console-permissions-api-gateway)
+ [Amazon CloudWatch Events](#console-permissions-cloudwatch-events)
+ [Amazon CloudWatch Logs](#console-permissions-cloudwatch-logs)
+ [Amazon Cognito](#console-permissions-cognito)
+ [Amazon DynamoDB](#console-permissions-dynamodb)
+ [Amazon Kinesis Data Streams](#console-permissions-kinesis)
+ [Amazon S3](#console-permissions-s3)
+ [Amazon SNS](#console-permissions-sns)
+ [AWS IoT](#console-permissions-iot)

**Note**  
All of these permissions policies grant the specific AWS services permissions to invoke a Lambda function\. The user who is configuring this integration must have permissions to invoke the Lambda function\. Otherwise, the user can't set the configuration\. You can attach the `AWSLambdaRole` AWS managed \(predefined\) permissions policy to the user to provide these permissions\.

## Amazon API Gateway<a name="console-permissions-api-gateway"></a>

When you configure an API endpoint in the console, the console makes several API Gateway API calls\. These calls require permissions for the `apigateway:*` action, as shown following:

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "ApiGatewayPermissions",
            "Effect": "Allow",
            "Action": [
                "apigateway:*"
            ],
            "Resource": "*"
        },
        {
            "Sid": "AddPermissionToFunctionPolicy",
            "Effect": "Allow",
            "Action": [
                "lambda:AddPermission",
                "lambda:RemovePermission",
                "lambda:GetPolicy"
            ],
            "Resource": "arn:aws:lambda:region:account-id:function:*"
        },
        {
            "Sid": "ListEventSourcePerm",
            "Effect": "Allow",
            "Action": [
                "lambda:ListEventSourceMappings"
            ],
            "Resource": "*"
        }
    ]
}
```

## Amazon CloudWatch Events<a name="console-permissions-cloudwatch-events"></a>

You can schedule when to invoke a Lambda function\. After you select an existing CloudWatch Events rule \(or create a new one\), AWS Lambda creates a new target in CloudWatch that invokes your Lambda function\. For target creation to work, you need to grant the following additional permissions:

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "EventPerms",
            "Effect": "Allow",
            "Action": [
                "events:PutRule",
                "events:ListRules",
                "events:ListRuleNamesByTarget",
                "events:PutTargets",
                "events:RemoveTargets",
                "events:DescribeRule",
                "events:TestEventPattern",
                "events:ListTargetsByRule",
                "events:DeleteRule"

              ],
            "Resource": "arn:aws:events:region:account-id:*"
        },
        {
            "Sid": "AddPermissionToFunctionPolicy",
            "Effect": "Allow",
            "Action": [
                "lambda:AddPermission",
                "lambda:RemovePermission",
                "lambda:GetPolicy"
              ],
            "Resource": "arn:aws:lambda:region:account-id:function:*"
        }
    ]
}
```

## Amazon CloudWatch Logs<a name="console-permissions-cloudwatch-logs"></a>

You can have the Amazon CloudWatch Logs service publish events and invoke your Lambda function\. When you configure this service as an event source, the console lists log groups in your account\. For this listing to occur, you need to grant the `logs:DescribeLogGroups` permissions, as shown following: 

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "CloudWatchLogsPerms",
            "Effect": "Allow",
            "Action": [
                "logs:FilterLogEvents",
                "logs:DescribeLogGroups",
                "logs:PutSubscriptionFilter",
                "logs:DescribeSubscriptionFilters",
                "logs:DeleteSubscriptionFilter",
                "logs:TestMetricFilter"
            ],
            "Resource": "arn:aws:logs:region:account-id:*"
        },
        {
            "Sid": "AddPermissionToFunctionPolicy",
            "Effect": "Allow",
            "Action": [
                "lambda:AddPermission",
                "lambda:RemovePermission",
                "lambda:GetPolicy"
            ],
            "Resource": "arn:aws:lambda:region:account-id:function:*"
        },
        {
            "Sid": "ListEventSourceMappingsPerms",
            "Effect": "Allow",
            "Action": [
                "lambda:ListEventSourceMappings"
            ],
            "Resource": "*"
        }
    ]
}
```

**Note**  
The additional permissions shown are required for managing subscription filters\.

## Amazon Cognito<a name="console-permissions-cognito"></a>

The console lists identity pools in your account\. After you select a pool, you can configure the pool to have the **Cognito sync trigger** as the event source type\. To do this, you need to grant the following additional permissions:

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "CognitoPerms1",
            "Effect": "Allow",
            "Action": [
                "cognito-identity:ListIdentityPools"
            ],
            "Resource": [
                "arn:aws:cognito-identity:region:account-id:*"
            ]
        },
        {
            "Sid": "CognitoPerms2",
            "Effect": "Allow",
            "Action": [
                "cognito-sync:GetCognitoEvents",
                "cognito-sync:SetCognitoEvents"
            ],
            "Resource": [
                "arn:aws:cognito-sync:region:account-id:*"
            ]
        },
        {
            "Sid": "AddPermissionToFunctionPolicy",
            "Effect": "Allow",
            "Action": [
                "lambda:AddPermission",
                "lambda:RemovePermission",
                "lambda:GetPolicy"
            ],
            "Resource": "arn:aws:lambda:region:account-id:function:*"
        },
        {
            "Sid": "ListEventSourcePerms",
            "Effect": "Allow",
            "Action": [
                "lambda:ListEventSourceMappings"
            ],
            "Resource": "*"
        }
    ]
}
```

## Amazon DynamoDB<a name="console-permissions-dynamodb"></a>

The console lists all of the tables in your account\. After you select a table, the console checks to see if a DynamoDB stream exists for that table\. If not, it creates the stream\. If you want the user to be able to configure a DynamoDB stream as an event source for a Lambda function, you need to grant the following additional permissions:

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "DDBpermissions1",
            "Effect": "Allow",
            "Action": [
                "dynamodb:DescribeStream",
                "dynamodb:DescribeTable",
                "dynamodb:UpdateTable"
            ],
            "Resource": "arn:aws:dynamodb:region:account-id:table/*"
        },
        {
            "Sid": "DDBpermissions2",
            "Effect": "Allow",
            "Action": [
                "dynamodb:ListStreams",
                "dynamodb:ListTables"
            ],
            "Resource": "*"
        },
        {
            "Sid": "LambdaGetPolicyPerm",
            "Effect": "Allow",
            "Action": [
                "lambda:GetPolicy"
            ],
            "Resource": "arn:aws:lambda:region:account-id:function:*"
        },
        {
            "Sid": "LambdaEventSourcePerms",
            "Effect": "Allow",
            "Action": [
                "lambda:CreateEventSourceMapping",
                "lambda:DeleteEventSourceMapping",
                "lambda:GetEventSourceMapping",
                "lambda:ListEventSourceMappings",
                "lambda:UpdateEventSourceMapping"
            ],
            "Resource": "*"
        }
    ]
}
```

**Important**  
For a Lambda function to read from a DynamoDB stream, the execution role associated with the Lambda function must have the correct permissions\. Therefore, the user must also have the same permissions before you can grant the permissions to the execution role\. You can grant these permissions by attaching the `AWSLambdaDynamoDBExecutionRole` predefined policy, first to the user and then to the execution role\.

## Amazon Kinesis Data Streams<a name="console-permissions-kinesis"></a>

The console lists all Kinesis streams in your account\. After you select a stream, the console creates event source mappings in AWS Lambda\. For this to work, you need to grant the following additional permissions:

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "PermissionForDescribeStream",
            "Effect": "Allow",
            "Action": [
                "kinesis:DescribeStream"
            ],
            "Resource": "arn:aws:kinesis:region:account-id:stream/*"
        },
        {
            "Sid": "PermissionForListStreams",
            "Effect": "Allow",
            "Action": [
                "kinesis:ListStreams"
            ],
            "Resource": "*"
        },
        {
            "Sid": "PermissionForGetFunctionPolicy",
            "Effect": "Allow",
            "Action": [
                "lambda:GetPolicy"
            ],
            "Resource": "arn:aws:lambda:region:account-id:function:*"
        },
        {
            "Sid": "LambdaEventSourcePerms",
            "Effect": "Allow",
            "Action": [
                "lambda:CreateEventSourceMapping",
                "lambda:DeleteEventSourceMapping",
                "lambda:GetEventSourceMapping",
                "lambda:ListEventSourceMappings",
                "lambda:UpdateEventSourceMapping"
            ],
            "Resource": "*"
        }
    ]
}
```

## Amazon S3<a name="console-permissions-s3"></a>

The console prepopulates the list of buckets in the AWS account and finds the bucket location for each bucket\. When you configure Amazon S3 as an event source, the console updates the bucket notification configuration\. For this to work, you need to grant the following additional permissions:

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "S3Permissions",
            "Effect": "Allow",
            "Action": [
                "s3:GetBucketLocation",
                "s3:GetBucketNotification",
                "s3:PutBucketNotification",
                "s3:ListAllMyBuckets"
            ],
            "Resource": "arn:aws:s3:::*"
        },
        {
            "Sid": "AddPermissionToFunctionPolicy",
            "Effect": "Allow",
            "Action": [
                "lambda:AddPermission",
                "lambda:RemovePermission"
            ],
            "Resource": "arn:aws:lambda:region:account-id:function:*"
        }
    ]
}
```

## Amazon SNS<a name="console-permissions-sns"></a>

The console lists Amazon Simple Notification Service \(Amazon SNS\) topics in your account\. After you select a topic, AWS Lambda subscribes your Lambda function to that Amazon SNS topic\. For this work, you need to grant the following additional permissions:

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "SNSPerms",
            "Effect": "Allow",
            "Action": [
                "sns:ListSubscriptions",
                "sns:ListSubscriptionsByTopic",
                "sns:ListTopics",
                "sns:Subscribe",
                "sns:Unsubscribe"
            ],
            "Resource": "arn:aws:sns:region:account-id:*"
        },
        {
            "Sid": "AddPermissionToFunctionPolicy",
            "Effect": "Allow",
            "Action": [
                "lambda:AddPermission",
                "lambda:RemovePermission",
                "lambda:GetPolicy"
            ],
            "Resource": "arn:aws:lambda:region:account-id:function:*"
        },
        {
            "Sid": "LambdaListESMappingsPerms",
            "Effect": "Allow",
            "Action": [
                "lambda:ListEventSourceMappings"
            ],
            "Resource": "*"
        }
    ]
}
```

## AWS IoT<a name="console-permissions-iot"></a>

The console lists all of the AWS IoT rules\. After you select a rule, the console populates the rest of the information associated with that rule in the user interface\. If you select an existing rule, the console updates it with information so that events are sent to AWS Lambda\. You can also create a new rule\. To do these things, the user must have the following additional permissions:

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "IoTperms",
            "Effect": "Allow",
            "Action": [
                "iot:GetTopicRule",
                "iot:CreateTopicRule",
                "iot:ReplaceTopicRule"
            ],
            "Resource": "arn:aws:iot:region:account-id:*"
        },
        {
            "Sid": "IoTlistTopicRulePerms",
            "Effect": "Allow",
            "Action": [
                "iot:ListTopicRules"
            ],
            "Resource": "*"
        },
        {
            "Sid": "LambdaPerms",
            "Effect": "Allow",
            "Action": [
                "lambda:AddPermission",
                "lambda:RemovePermission",
                "lambda:GetPolicy"
            ],
            "Resource": "arn:aws:lambda:region:account-id:function:*"
        }
    ]
}
```