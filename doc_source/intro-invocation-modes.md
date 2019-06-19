# AWS Lambda Event Source Mapping<a name="intro-invocation-modes"></a>

Lambda functions and event sources are the core components of AWS Lambda\. An event source is the entity that publishes events, and a Lambda function is the custom code that processes the events\. Supported event sources are the AWS services that can be preconfigured to work with AWS Lambda\. The configuration is referred to as *event source mapping*, which maps an event source to a Lambda function\. It enables automatic invocation of your Lambda function when events occur\. 

Each event source mapping identifies the type of events to publish and the Lambda function to invoke when events occur\. The specific Lambda function then receives the event information as a parameter and your Lambda function code then processes the event\. 

You can also create custom applications to include AWS resource events and invoke a Lambda function\. For more information, see [Using AWS Lambda with the AWS Command Line Interface](with-userapp.md)

You may be wondering—where do I keep the event mapping information? Do I keep it within the event source or within AWS Lambda? The following sections explain event source mapping for each of these event source categories\. These sections also explain how the Lambda function is invoked and how you manage permissions to allow invocation of your Lambda function\. 

**Topics**
+ [Event Source Mapping for AWS Services](#non-streaming-event-source-mapping)
+ [Event Source Mapping for AWS Poll\-Based Services](#streaming-event-source-mapping)
+ [Event Source Mapping for Custom Applications](#custom-app-event-source-mapping)

## Event Source Mapping for AWS Services<a name="non-streaming-event-source-mapping"></a>

Except for the poll\-based AWS services \(Amazon Kinesis Data Streams and DynamoDB streams or Amazon SQS queues\), other supported AWS services publish events and can also invoke your Lambda function \(referred to as the *push model*\)\. In the push model, note the following:
+ Event source mappings are maintained within the event source\. Relevant API support in the event sources enables you to create and manage event source mappings\. For example, Amazon S3 provides the bucket notification configuration API\. Using this API, you can configure an event source mapping that identifies the bucket events to publish and the Lambda function to invoke\.
+ Because the event sources invoke your Lambda function, you need to grant the event source the necessary permissions using a resource\-based policy\. For more information, see [Using Resource\-based Policies for AWS Lambda](access-control-resource-based.md)\.

The following example illustrates how this model works\.

**Example – Amazon S3 Pushes Events and Invokes a Lambda Function**  
Suppose that you want your AWS Lambda function invoked for each *object created* bucket event\. You add the necessary event source mapping in the bucket notification configuration\.   

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/push-s3-example-10.png)
The diagram illustrates the flow:   

1. The user creates an object in a bucket\.

1. Amazon S3 detects the object created event\.

1. Amazon S3 invokes your Lambda function according to the event source mapping described in the bucket notification configuration\. 

1. AWS Lambda verifies the permissions policy attached to the Lambda function to ensure that Amazon S3 has the necessary permissions\. For more information on permissions policies, see [AWS Lambda Permissions](lambda-permissions.md)

1. Once AWS Lambda verifies the attached permissions policy, it executes the Lambda function\. Remember that your Lambda function receives the event as a parameter\.

## Event Source Mapping for AWS Poll\-Based Services<a name="streaming-event-source-mapping"></a>

AWS Lambda supports the following poll\-based services:

**Services That Lambda Reads Events From**
+ [Amazon Kinesis](with-kinesis.md)
+ [Amazon DynamoDB](with-ddb.md)
+ [Amazon Simple Queue Service](with-sqs.md)

Once you have configured the event source mapping, AWS Lambda polls the event source and invokes your Lambda function\. The event source mappings are maintained within the AWS Lambda\. AWS Lambda provides APIs to create and manage event source mappings\. For more information, see [CreateEventSourceMapping](API_CreateEventSourceMapping.md)\. 

AWS Lambda needs your permission to poll Kinesis and DynamoDB streams or Amazon SQS queues and read records\. You grant these permissions via the [execution role](lambda-intro-execution-role.md), using the permissions policy associated with role that you specify when you create your Lambda function\. AWS Lambda does not need any permissions to invoke your Lambda function\. 

The following diagram shows a custom application that writes records to a Kinesis stream and how AWS Lambda polls the stream\. When AWS Lambda detects a new record on the stream, it invokes your Lambda function\.

Suppose you have a custom application that writes records to a Kinesis stream\. You want to invoke a Lambda function when new records are detected on the stream\. You create a Lambda function and the necessary event source mapping in AWS Lambda\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/kinesis-pull-10.png)

The diagram illustrates the following sequence:

1. The custom application writes records to an Amazon Kinesis stream\.

1. AWS Lambda continuously polls the stream and invokes the Lambda function once the service detects new records on the stream\. AWS Lambda knows which stream to poll and which Lambda function to invoke based on the event source mapping you create in AWS Lambda\. 

1. AWS Lambda executes the Lambda function\.

The example uses a Kinesis stream but the same applies when working with a DynamoDB stream\.

## Event Source Mapping for Custom Applications<a name="custom-app-event-source-mapping"></a>

If you have custom applications that publish and process events, you can create a Lambda function to process these events\. In this case, there is no preconfiguration required—you don't have to set up an event source mapping\. Instead, the event source uses the AWS Lambda `Invoke` API\. If the application and Lambda function are owned by different AWS accounts, the AWS account that owns the Lambda function must allow cross\-account permissions in the permissions policy associated with the Lambda function\.

The following diagram shows how a custom application in your account can invoke a Lambda function\. In this example, the custom application is using the same account credentials as the account that owns the Lambda function, and, therefore, does not require additional permissions to invoke the function\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/push-user-app-example-10.png)

In the following example, the user application and Lambda function are owned by different AWS accounts\. In this case, the AWS account that owns the Lambda function must have cross\-account permissions in the permissions policy associated with the Lambda function\. For more information, see [AWS Lambda Permissions](lambda-permissions.md)\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/push-user-cross-account-app-example-10.png)