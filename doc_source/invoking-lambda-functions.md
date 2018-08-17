# Invoking Lambda Functions<a name="invoking-lambda-functions"></a>

 When building applications on AWS Lambda, including [serverless](https://aws.amazon.com/serverless) applications, the core components are Lambda functions and event sources\. An *event source* is the AWS service or custom application that publishes events, and a *Lambda function* is the custom code that processes the events\. To illustrate, consider the following scenarios:
+ **File processing** – Suppose you have a photo sharing application\. People use your application to upload photos, and the application stores these user photos in an Amazon S3 bucket\. Then, your application creates a thumbnail version of each user's photos and displays them on the user's profile page\. In this scenario, you may choose to create a Lambda function that creates a thumbnail automatically\. Amazon S3 is one of the supported AWS event sources that can publish *object\-created events* and invoke your Lambda function\. Your Lambda function code can read the photo object from the S3 bucket, create a thumbnail version, and then save it in another S3 bucket\. 
+ **Data and analytics** – Suppose you are building an analytics application and storing raw data in a DynamoDB table\. When you write, update, or delete items in a table, DynamoDB streams can publish item update events to a stream associated with the table\. In this case, the event data provides the item key, event name \(such as insert, update, and delete\), and other relevant details\. You can write a Lambda function to generate custom metrics by aggregating raw data\.
+ **Websites** – Suppose you are creating a website and you want to host the backend logic on Lambda\. You can invoke your Lambda function over HTTP using Amazon API Gateway as the HTTP endpoint\. Now, your web client can invoke the API, and then API Gateway can route the request to Lambda\.
+ **Mobile applications** – Suppose you have a custom mobile application that produces events\. You can create a Lambda function to process events published by your custom application\. For example, in this scenario you can configure a Lambda function to process the clicks within your custom mobile application\. 

Each of these event sources uses a specific format for the event data\. For more information, see [Sample Events Published by Event Sources](eventsources.md)\. When a Lambda function is invoked, it receives the event as a parameter for the Lambda function\. 

AWS Lambda supports many AWS services as event sources\. For more information, see [Supported Event Sources](invoking-lambda-function.md)\. When you configure these event sources to trigger a Lambda function, the Lambda function is invoked automatically when events occur\. You define *event source mapping*, which is how you identify what events to track and which Lambda function to invoke\. 

In addition to the supported AWS services, user applications can also generate events—you can build your own custom event sources\. Custom event sources invoke a Lambda function using the AWS Lambda [Invoke](API_Invoke.md) operation\. User applications, such as client, mobile, or web applications, can publish events and invoke Lambda functions on demand using the AWS SDKs or AWS Mobile SDKs, such as the AWS Mobile SDK for Android\. 

The following are introductory examples of event sources and how the end\-to\-end experience works\.

## Example 1: Amazon S3 Pushes Events and Invokes a Lambda Function<a name="example-lambda-pushes-events-invokes-function"></a>

Amazon S3 can publish events of different types, such as PUT, POST, COPY, and DELETE object events on a bucket\. Using the bucket notification feature, you can configure an event source mapping that directs Amazon S3 to invoke a Lambda function when a specific type of event occurs, as shown in the following illustration\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/push-s3-example-10.png)

The diagram illustrates the following sequence:

1. The user creates an object in a bucket\.

1. Amazon S3 detects the object created event\.

1. Amazon S3 invokes your Lambda function using the permissions provided by the execution role\. For more information on execution roles, see [Authentication and Access Control for AWS Lambda](lambda-auth-and-access-control.md)\. Amazon S3 knows which Lambda function to invoke based on the event source mapping that is stored in the bucket notification configuration\. 

1. AWS Lambda executes the Lambda function, specifying the event as a parameter\.

Note the following:
+ The event source mapping is maintained within the event source service, Amazon S3 in this scenario\. This is true for all supported AWS event sources except the poll\-based sources \(Kinesis and DynamoDB streams or Amazon SQS queues\)\. The next example explains poll\-based event sources\.
+ The event source \(Amazon S3\) invokes the Lambda function \(referred to as the *push model*\)\. Again, this is true for all supported AWS services except the poll\-based event sources\.
+ In order for the event source \(Amazon S3\) to invoke your Lambda function, you must grant permissions using the permissions policy attached to the Lambda function\.

## Example 2: AWS Lambda Pulls Events from a Kinesis Stream and Invokes a Lambda Function<a name="example-lambda-pulls-kinesis-streams-events-invokes-function"></a>

For poll\-based event sources, AWS Lambda polls the source and then invokes the Lambda function when records are detected on that source\. For poll\-based sources, event source mapping information is stored in AWS Lambda\. AWS Lambda provides an API for you to create and manage these event source mappings\. 
+ [CreateEventSourceMapping](API_CreateEventSourceMapping.md)
+ [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md)

The following diagram shows how a custom application writes records to a Kinesis stream\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/kinesis-pull-10.png)

The diagram illustrates the following sequence:

1. The custom application writes records to a Kinesis stream\.

1. AWS Lambda continuously polls the stream, and invokes the Lambda function when the service detects new records on the stream\. AWS Lambda knows which stream to poll and which Lambda function to invoke based on the event source mapping you create in Lambda\.

1. The Lambda function is invoked with the incoming event\.

Note the following:
+ When working with stream\-based event sources, the following is true:
  + You create event source mappings in AWS Lambda\.
  + AWS Lambda invokes the Lambda function synchronously \(referred to as the *pull model*\)\.
+ AWS Lambda does not need permission to invoke your Lambda function, therefore you don't need to add any permissions to the permissions policy attached to your Lambda function\. 
+ Your Lambda role needs permission to read from the stream\.

## Example 3: AWS Lambda Pulls Events from an Amazon SQS Queue and Invokes a Lambda Function<a name="example-lambda-pulls-sqs-events-invokes-function"></a>

For poll\-based event sources, AWS Lambda polls the source and then invokes the Lambda function when records are detected on that source\. For poll\-based sources, event source mapping information is stored in AWS Lambda\. AWS Lambda provides an API for you to create and manage these event source mappings\. 
+ [CreateEventSourceMapping](API_CreateEventSourceMapping.md)
+ [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md)

The following diagram shows how a custom application writes records to an Amazon SQS queue: 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/sqs-queue.png)

The diagram illustrates the following sequence:

1. The custom application writes records to an Amazon SQS queue\.

1. AWS Lambda continuously polls the queue, and invokes the Lambda function when the service detects new records\. AWS Lambda knows which queue to poll and which Lambda function to invoke based on the event source mapping you create in Lambda\.

1. The Lambda function is invoked with the incoming event\.

Note the following:
+ When working with poll\-based events, the following is true:
  + You create event source mappings in AWS Lambda\.
  + AWS Lambda invokes the Lambda function synchronously \(referred to as the *pull model*\)\.
+ AWS Lambda does not need permission to invoke your Lambda function, therefore you don't need to add any permissions to the permissions policy attached to your Lambda function\. 
+ Your Lambda role needs permission to read from the queue and queue attributes\.