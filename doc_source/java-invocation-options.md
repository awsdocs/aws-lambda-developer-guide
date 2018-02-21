# Invocation Types<a name="java-invocation-options"></a>

AWS Lambda supports synchronous and asynchronous invocation of a Lambda function\. You can control the invocation type only when you invoke a Lambda function \(referred to as *on\-demand invocation*\)\. The following examples illustrate on\-demand invocations:

+ Your custom application invokes a Lambda function\.

   

+ You manually invoke a Lambda function \(for example, using the AWS CLI\) for testing purposes\.

In both cases, you invoke your Lambda function using the [Invoke](API_Invoke.md) operation, and you can specify the invocation type as synchronous or asynchronous\. 

However, when you are using AWS services as event sources, the invocation type is predetermined for each of these services\. You don't have any control over the invocation type that these event sources use when they invoke your Lambda function\. For example, Amazon S3 always invokes a Lambda function asynchronously and Amazon Cognito always invokes a Lambda function synchronously\. For stream\-based AWS services \(Amazon Kinesis Streams and Amazon DynamoDB Streams\), AWS Lambda polls the stream and invokes your Lambda function synchronously\.

## Event Source Mapping<a name="intro-invocation-modes"></a>

In AWS Lambda, Lambda functions and event sources are the core components in AWS Lambda\.  An event source is the entity that publishes events, and a Lambda function is the custom code that processes the events\. Supported event sources refer to those AWS services that can be preconfigured to work with AWS Lambda\. The configuration is referred to as *event source mapping*, which maps an event source to a Lambda function\. It enables automatic invocation of your Lambda function when events occur\. 

Each event source mapping identifies the type of events to publish and the Lambda function to invoke when events occur\. The specific Lambda function then receives the event information as a parameter, your Lambda function code can then process the event\. 

Note the following about the event sources\. These event sources can be any of the following:

+ **AWS services** – These are the supported AWS services that can be preconfigured to work with AWS Lambda\. You can group these services as regular AWS services or stream\-based services\. Amazon Kinesis Data Streams and Amazon DynamoDB Streams are stream\-based event sources, all others AWS services do not use stream\-based event sources\. Where you maintain the event source mapping and how the Lambda function is invoked depends on whether or not you're using a stream\-based event source\.

+ **Custom applications** – You can have your custom applications publish events and invoke a Lambda function\.

You may be wondering—where do I keep the event mapping information? Do I keep it within the event source or within AWS Lambda? The following sections explain event source mapping for each of these event source categories\. These sections also explain how the Lambda function is invoked and how you manage permissions to allow invocation of your Lambda function\. 


+ [Event Source Mapping for AWS Services](#non-streaming-event-source-mapping)
+ [Event Source Mapping for AWS Stream\-Based Services](#streaming-event-source-mapping)
+ [Event Source Mapping for Custom Applications](#custom-app-event-source-mapping)

### Event Source Mapping for AWS Services<a name="non-streaming-event-source-mapping"></a>

Except for the stream\-based AWS services \(Amazon Kinesis Data Streams and DynamoDB streams\), other supported AWS services publish events and can also invoke your Lambda function \(referred to as the *push model*\)\. In the push model, note the following:

+ Event source mappings are maintained within the event source\. Relevant API support in the event sources enables you to create and manage event source mappings\. For example, Amazon S3 provides the bucket notification configuration API\. Using this API, you can configure an event source mapping that identifies the bucket events to publish and the Lambda function to invoke\.

+ Because the event sources invoke your Lambda function, you need to grant the event source the necessary permissions using a resource\-based policy \(referred to as the *Lambda function policy*\)\. For more information, see [AWS Lambda Permissions Model](intro-permission-model.md)\.

The following example illustrates how this model works\.

**Example – Amazon S3 Pushes Events and Invokes a Lambda Function**  
Suppose that you want your AWS Lambda function invoked for each *object created* bucket event\. You add the necessary event source mapping in the bucket notification configuration\.   

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/push-s3-example-10.png)
The diagram illustrates the flow:   

1. The user creates an object in a bucket\.

1. Amazon S3 detects the object created event\.

1. Amazon S3 invokes your Lambda function according to the event source mapping described in the bucket notification configuration\. 

1. AWS Lambda verifies the permissions policy attached to the Lambda function to ensure that Amazon S3 has the necessary permissions\. For more information on permissions policies, see [Authentication and Access Control for AWS Lambda](lambda-auth-and-access-control.md)

1. Once AWS Lambda verifies the attached permissions policy, it executes the Lambda function\. Remember that your Lambda function receives the event as a parameter\.

### Event Source Mapping for AWS Stream\-Based Services<a name="streaming-event-source-mapping"></a>

The Amazon Kinesis Data Streams and DynamoDB streams are the stream\-based services that you can preconfigure to use with AWS Lambda\. After you do the necessary event source mapping, AWS Lambda polls the streams and invokes your Lambda function \(referred to as the *pull model*\)\. In the pull model, note the following:

+ The event source mappings are maintained within the AWS Lambda\. AWS Lambda provides the relevant APIs to create and manage event source mappings\. For more information, see [CreateEventSourceMapping](API_CreateEventSourceMapping.md)\. 

+ AWS Lambda needs your permission to poll the stream and read records\. You grant these permissions via the execution role, using the permissions policy associated with role that you specify when you create your Lambda function\. AWS Lambda does not need any permissions to invoke your Lambda function\. 

The following example illustrates how this model works\.

**Example – AWS Lambda Pulls Events from an Kinesis Stream and Invokes a Lambda Function**  
The following diagram shows a custom application that writes records to an Kinesis stream and how AWS Lambda polls the stream\. When AWS Lambda detects a new record on the stream, it invokes your Lambda function\.  
Suppose you have a custom application that writes records to an Kinesis stream\. You want to invoke a Lambda function when new records are detected on the stream\. You create a Lambda function and the necessary event source mapping in AWS Lambda\.   

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/kinesis-pull-10.png)
The diagram illustrates the following sequence:  

1. The custom application writes records to an Kinesis stream\.

1. AWS Lambda continuously polls the stream, and invokes the Lambda function when the service detects new records on the stream\. AWS Lambda knows which stream to poll and which Lambda function to invoke based on the event source mapping you create in AWS Lambda\. 

1. Assuming the attached permission policy, which allows AWS Lambda to poll the stream, is verified, AWS Lambda then executes the Lambda function\. For more information on permissions policies, see [Authentication and Access Control for AWS Lambda](lambda-auth-and-access-control.md)
The example uses a Kinesis stream but the same applies when working with a DynamoDB stream\.

### Event Source Mapping for Custom Applications<a name="custom-app-event-source-mapping"></a>

If you have custom applications that publish and process events, you can create a Lambda function to process these events\. In this case, there is no preconfiguration required—you don't have to set up an event source mapping\. Instead, the event source uses the AWS Lambda `Invoke` API\. If the application and Lambda function are owned by different AWS accounts, the AWS account that owns the Lambda function must allow cross\-account permissions in the permissions policy associated with the Lambda function\.

The following example illustrates how this works\.

**Example – Custom Application Publishes Events and Invokes a Lambda Function**  
The following diagram shows how a custom application in your account can invoke a Lambda function\. In this example, the custom application is using the same account credentials as the account that owns the Lambda function, and, therefore, does not require additional permissions to invoke the function\.  

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/push-user-app-example-10.png)
In the following example, the user application and Lambda function are owned by different AWS accounts\. In this case, the AWS account that owns the Lambda function must have cross\-account permissions in the permissions policy associated with the Lambda function\. For more information, see [AWS Lambda Permissions Model](intro-permission-model.md)\.   

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/push-user-cross-account-app-example-10.png)