# Using AWS Lambda with other services<a name="lambda-services"></a>

AWS Lambda integrates with other AWS services to invoke functions or take other actions\. These are some common use cases:
+ Invoke a function in response to resource lifecycle events, such as with Amazon Simple Storage Service \(Amazon S3\)\. For more information, see [Using AWS Lambda with Amazon S3](with-s3.md)\.
+ Respond to incoming HTTP requests\. For more information, see [Tutorial: Using Lambda with API Gateway](services-apigateway-tutorial.md)\.
+ Consume events from a queue\. For more information, see [Using Lambda with Amazon SQS](with-sqs.md)\.
+ Run a function on a schedule\. For more information, see [Using AWS Lambda with Amazon EventBridge \(CloudWatch Events\)](services-cloudwatchevents.md)\.

Depending on which service you're using with Lambda, the invocation generally works in one of two ways\. An event drives the invocation or Lambda polls a queue or data stream and invokes the function in response to activity in the queue or data stream\. Lambda integrates with Amazon Elastic File System and AWS X\-Ray in a way that doesn't involve invoking functions\. 

For more information, see [Event\-driven invocation](#event-driven-invocation) and [Lambda polling](#lambda-polling)\. Or, look up the service that you want to work with in the following section to find a link to information about using that service with Lambda\.

## Listing of services and links to more information<a name="listing-of-services-and-links-to-more-information"></a>

Find the service that you want to work with in the following table, to determine which method of invocation you should use\. Follow the link from the service name to find information about how to set up the integration between the services\. These topics also include example events that you can use to test your function\.

**Tip**  
Entries in this table are alphabetical by service name, excluding the "Amazon" or "AWS" prefix\. You can also use your browser's search functionality to find your service in the list\.


****  

| Service | Method of invocation | 
| --- | --- | 
|  [Amazon Alexa](services-alexa.md)  |  Event\-driven; synchronous invocation  | 
|  [Amazon Managed Streaming for Apache Kafka](with-msk.md)  |  Lambda polling  | 
|  [Self\-managed Apache Kafka](with-kafka.md)  |  Lambda polling  | 
|  [Amazon API Gateway](services-apigateway.md)  |  Event\-driven; synchronous invocation  | 
|  [AWS CloudFormation](services-cloudformation.md)  |  Event\-driven; asynchronous invocation  | 
|  [Amazon CloudFront \(Lambda@Edge\)](lambda-edge.md)  |  Event\-driven; synchronous invocation  | 
|  [Amazon EventBridge \(CloudWatch Events\)](services-cloudwatchevents.md)  |  Event\-driven; asynchronous invocation  | 
|  [Amazon CloudWatch Logs](services-cloudwatchlogs.md)  |  Event\-driven; asynchronous invocation  | 
|  [AWS CodeCommit](services-codecommit.md)  |  Event\-driven; asynchronous invocation  | 
|  [AWS CodePipeline](services-codepipeline.md)  |  Event\-driven; asynchronous invocation  | 
|  [Amazon Cognito](services-cognito.md)  |  Event\-driven; synchronous invocation  | 
|  [AWS Config](services-config.md)  |  Event\-driven; asynchronous invocation  | 
|  [Amazon Connect](services-connect.md)  |  Event\-driven; synchronous invocation  | 
|  [Amazon DynamoDB](with-ddb.md)  |  Lambda polling  | 
|  [Amazon Elastic File System](services-efs.md)  |  Special integration  | 
|  [Elastic Load Balancing \(Application Load Balancer\)](services-alb.md)  |  Event\-driven; synchronous invocation  | 
|  [AWS IoT](services-iot.md)  |  Event\-driven; asynchronous invocation  | 
|  [AWS IoT Events](services-iotevents.md)  |  Event\-driven; asynchronous invocation  | 
|  [Amazon Kinesis](with-kinesis.md)  |  Lambda polling  | 
|  [Amazon Kinesis Data Firehose](services-kinesisfirehose.md)  |  Event\-driven; synchronous invocation  | 
|  [Amazon Lex](services-lex.md)  |  Event\-driven; synchronous invocation  | 
|  [Amazon MQ](with-mq.md)  |  Lambda polling  | 
|  [Amazon Simple Email Service](services-ses.md)  |  Event\-driven; asynchronous invocation  | 
|  [Amazon Simple Notification Service](with-sns.md)  |  Event\-driven; asynchronous invocation  | 
|  [Amazon Simple Queue Service](with-sqs.md)  |  Lambda polling  | 
|  [Amazon Simple Storage Service \(Amazon S3\)](with-s3.md)  |  Event\-driven; asynchronous invocation  | 
|  [Amazon Simple Storage Service Batch](services-s3-batch.md)  |  Event\-driven; synchronous invocation  | 
|  [Secrets Manager](with-secrets-manager.md)  |  Event\-driven; synchronous invocation  | 
|  [AWS X\-Ray](services-xray.md)  |  Special integration  | 

## Event\-driven invocation<a name="event-driven-invocation"></a>

Some services generate events that can invoke your Lambda function\. For more information about designing these types of architectures , see [Event driven architectures](https://docs.aws.amazon.com/lambda/latest/operatorguide/event-driven-architectures.html) in the *Lambda operator guide*\.

When you implement an event\-driven architecture, you grant the event\-generating service permission to invoke your function in the function's [resource\-based policy](access-control-resource-based.md)\. Then you configure that service to generate events that invoke your function\.

The events are data structured in JSON format\. The JSON structure varies depending on the service that generates it and the event type, but they all contain the data that the function needs to process the event\.

Lambda converts the event document into an object and passes it to your [function handler](gettingstarted-concepts.md)\. For compiled languages, Lambda provides definitions for event types in a library\. For more information, see the topic about building functions with your language: [Building Lambda functions with C\#](lambda-csharp.md), [Building Lambda functions with Go](lambda-golang.md), [Building Lambda functions with Java](lambda-java.md), or  [Building Lambda functions with PowerShell](lambda-powershell.md)\.

Depending on the service, the event\-driven invocation can be synchronous or asynchronous\.
+ For synchronous invocation, the service that generates the event waits for the response from your function\. That service defines the data that the function needs to return in the response\. The service controls the error strategy, such as whether to retry on errors\. For more information, see [Synchronous invocation](invocation-sync.md)\.
+ For asynchronous invocation, Lambda queues the event before passing it to your function\. When Lambda queues the event, it immediately sends a success response to the service that generated the event\. After the function processes the event, Lambda doesn’t return a response to the event\-generating service\. For more information, see [Asynchronous invocation](invocation-async.md)\.

For more information about how Lambda manages error handling for synchronously and asychronously invoked functions, see [Error handling and automatic retries in AWS Lambda](invocation-retries.md)\.

## Lambda polling<a name="lambda-polling"></a>

For services that generate a queue or data stream, you set up an [event source mapping](invocation-eventsourcemapping.md) in Lambda to have Lambda poll the queue or a data stream\.

When you implement a Lambda polling architecture, you grant Lambda permission to access the other service in the function's [execution role](lambda-intro-execution-role.md)\. Lambda reads data from the other service, creates an event, and invokes your function\.