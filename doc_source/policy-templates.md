# Policy Templates<a name="policy-templates"></a>

When you create an AWS Lambda function in the console using one of the blueprints, Lambda allows you to create a role for your function from a list of Lambda policy templates\. By selecting one of these templates, your Lambda function automatically creates the role with the requisite permissions attached to that policy\. 

The following lists the permissions that are applied to each policy template in the **Policy templates** list\. The policy templates are named after the blueprints to which they correspond\. Lambda will automatically populate the placeholder items \(such as *region* and *accountID*\) with the appropriate information\. For more information on creating a Lambda function using policy templates, see [Create a Simple Lambda Function](get-started-create-function.md)\.

The following templates are automatically applied depending upon the type of Lambda function you are creating:

## Basic: 'Basic Lambda Permissions'<a name="basic-execution"></a>

```
{
   "Version":"2012-10-17",
   "Statement":[
      {
         "Effect":"Allow",
         "Action":"logs:CreateLogGroup",
         "Resource":"arn:aws:logs:region:accountId:*"
      },
      {
         "Effect":"Allow",
         "Action":[
            "logs:CreateLogStream",
            "logs:PutLogEvents"
         ],
         "Resource":[
            "arn:aws:logs:region:accountId:log-group:[[logGroups]]:*"
         ]
      }
   ]
}
```

## VPCAccess: 'Lambda VPC Access Permissions'<a name="LambdaVPCAccessExecutionRole"></a>

```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "ec2:CreateNetworkInterface",
        "ec2:DeleteNetworkInterface",
        "ec2:DescribeNetworkInterfaces"
      ],
      "Resource": "*"
    }
  ]
}
```

## Kinesis: 'Lambda Kinesis stream poller permissions'<a name="KinesisExecutionRole"></a>

```
{
   "Version":"2012-10-17",
   "Statement":[
      {
         "Effect":"Allow",
         "Action":"lambda:InvokeFunction",
         "Resource":"arn:aws:lambda:region:accountId:function:functionName*"
      },
      {
         "Effect":"Allow",
         "Action":"kinesis:ListStreams",
         "Resource":"arn:aws:kinesis:region:accountId:stream/*"
      },
      {
         "Effect":"Allow",
         "Action":[
            "kinesis:DescribeStream",
            "kinesis:GetRecords",
            "kinesis:GetShardIterator"
         ],
         "Resource":"arn:aws:kinesis:region:accountId:
stream/streamName"
      }
   ]
}
```

## DynamoDB: 'Lambda DynamoDB stream poller permissions'<a name="DynamoDBExecutionRole"></a>

```
{
   "Version":"2012-10-17",
   "Statement":[
      {
         "Effect":"Allow",
         "Action":"lambda:InvokeFunction",
         "Resource":"arn:aws:lambda:region:accountId:function:functionName*"
      },
      {
         "Effect":"Allow",
         "Action":[
            "dynamodb:DescribeStream",
            "dynamodb:GetRecords",
            "dynamodb:GetShardIterator",
            "dynamodb:ListStreams"
         ],
         "Resource":"arn:aws:dynamodb:region:accountId:table/tableName/stream/*"
      }
   ]
}
```

## Edge: 'Basic Edge Lambda permissions'<a name="EdgeExecutionRole"></a>

```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ],
      "Resource": [
        "arn:aws:logs:*:*:*"
      ]
    }
  ]
}
```

## RedrivePolicySNS: ‘Dead letter queue SNS permissions’<a name="RedriveSNSExecutionRole"></a>

```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "sns:Publish"
      ],
      "Resource": "arn:aws:sns:region:accountId:topicName"
    }
  ]
}
```

## RedrivePolicySQS: 'Dead letter queue SQS permissions'<a name="RedriveSQSExecutionRole"></a>

```
{
  "Version": "2012-10-17",
  "Statement": [
  {
    "Effect": "Allow",
    "Action": [
       "sqs:SendMessage"
     ],
    "Resource": "arn:aws:sqs:region:accountId:queueName"
  }
 ]
}
```

## <a name="w4ab1c67c11c24c21"></a>

The following templates are selected depending upon which blueprint you choose\. You can also select them from the dropdown to add extra permissions:

## CloudFormation: 'CloudFormation stack read\-only permissions'<a name="CloudFormationExecutionRole"></a>

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "cloudformation:DescribeStacks"
            ],
            "Resource": "*"
        }
    ]
}
```

## AMI: 'AMI read\-only permissions'<a name="AMIExecutionRole"></a>

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "ec2:DescribeImages"
            ],
            "Resource": "*"
        }
    ]
}
```

## KMS: 'KMS decryption permissions'<a name="KMSExecutionRole"></a>

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "kms:Decrypt"
            ],
            "Resource": "*"
        }
    ]
}
```

## S3: 'S3 object read\-only permissions'<a name="S3ExecutionRole"></a>

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "s3:GetObject"
            ],
            "Resource": "arn:aws:s3:::*"
        }
    ]
}
```

## Elasticsearch: 'Elasticsearch permissions'<a name="ElasticsearchExecutionRole"></a>

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "es:ESHttpPost"
            ],
            "Resource": "*"
        }
    ]
}
```

## SES: 'SES bounce permissions'<a name="SESExecutionRole"></a>

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "ses:SendBounce"
            ],
            "Resource": "*"
        }
    ]
}
```

## TestHarness: 'Test Harness permissions'<a name="TestHarnessExecutionRole"></a>

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "dynamodb:PutItem"
            ],
            "Resource": "arn:aws:dynamodb:region:accountId:table/*"
        },
        {
            "Effect": "Allow",
            "Action": [
                "lambda:InvokeFunction"
            ],
            "Resource": "arn:aws:lambda:region:accountId:function:*"
        }
    ]
}
```

## Microservice: 'Simple Microservice permissions'<a name="MicroServiceExecutionRole"></a>

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "dynamodb:DeleteItem",
                "dynamodb:GetItem",
                "dynamodb:PutItem",
                "dynamodb:Scan",
                "dynamodb:UpdateItem"
            ],
            "Resource": "arn:aws:dynamodb:region:accountId:table/*"
        }
    ]
}
```

## VPN: 'VPN Connection Monitor permissions'<a name="VPNExecutionRole"></a>

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "cloudwatch:PutMetricData"
            ],
            "Resource": "*"
        },
        {
            "Effect": "Allow",
            "Action": [
                "ec2:DescribeRegions",
                "ec2:DescribeVpnConnections"
            ],
            "Resource": "*"
        }
    ]
}
```

## SQS: 'SQS Poller permissions'<a name="SQSExecutionRole"></a>

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "sqs:DeleteMessage",
                "sqs:ReceiveMessage"
            ],
            "Resource": "arn:aws:sqs:*"
        },
        {
            "Effect": "Allow",
            "Action": [
                "lambda:InvokeFunction"
            ],
            "Resource": "arn:aws:lambda:region:accountId:function:functionName*"
        }
    ]
}
```

## IoTButton: 'AWS IoT Button permissions'<a name="IOTExecutionRole"></a>

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "sns:ListSubscriptionsByTopic",
                "sns:CreateTopic",
                "sns:SetTopicAttributes",
                "sns:Subscribe",
                "sns:Publish"
            ],
            "Resource": "*"
        }
    ]
}
```

## RekognitionNoDataAccess:'Amazon Rekognition no data permissions'<a name="RekognitionNoDataAccessExecutionRole"></a>

```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "rekognition:CompareFaces",
        "rekognition:DetectFaces",
        "rekognition:DetectLabels"
      ],
      "Resource": "*"
    }
  ]
}
```

## RekognitionReadOnlyAccess: 'Amazon Rekognition read\-only permissions'<a name="RekognitionReadOnlyAccessExecutionRole"></a>

```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "rekognition:ListCollections",
        "rekognition:ListFaces",
        "rekognition:SearchFaces",
        "rekognition:SearchFacesByImage"
      ],
      "Resource": "*"
    }
  ]
}
```

## RekognitionWriteOnlyAccess: 'Amazon Rekognition write\-only permissions'<a name="RekognitionWriteOnlyAccessExecutionRole"></a>

```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "rekognition:CreateCollection",
        "rekognition:IndexFaces"
      ],
      "Resource": "*"
    }
  ]
}
```