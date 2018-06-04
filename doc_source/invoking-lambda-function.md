# Supported Event Sources<a name="invoking-lambda-function"></a>

This topic lists the supported AWS services that you can configure as event sources for AWS Lambda functions\. After you preconfigure the event source mapping, your Lambda function gets invoked automatically when these event sources detect events\. For more information about invocation modes, see [Event Source Mapping](invocation-options.md#intro-invocation-modes)\.

For all of the event sources listed in this topic, note the following:
+ Event sources maintain the event source mapping, except for the stream\-based services \(Amazon Kinesis Data Streams and Amazon DynamoDB Streams\)\. For the stream\-based services, AWS Lambda maintains the event source mapping\. AWS Lambda provides the [CreateEventSourceMapping](API_CreateEventSourceMapping.md) operation for you to create and manage the event source mapping\. For more information, see [Event Source Mapping](invocation-options.md#intro-invocation-modes)\.

   
+ The invocation type that these event sources use when invoking a Lambda function is also preconfigured\. For example, Amazon S3 always invokes a Lambda function asynchronously and Amazon Cognito invokes a Lambda function synchronously\. The only time you can control the invocation type is when you are invoking the Lambda function yourself using the [Invoke](API_Invoke.md) operation \(for example, invoking a Lambda function on demand from your custom application\)\.

   
+ In order to process AWS events, your Lambda functions may need to include additional libraries, depending on the programming language used to create the function\. Functions written in Node\.js or Python do not require any additional libraries\. For C\#, you need to include [AWS Lambda for \.NET Core](https://github.com/aws/aws-lambda-dotnet)\. For Java, you need to include [aws\-lambda\-java\-libs](https://github.com/aws/aws-lambda-java-libs)\.
**Important**  
Each of the included packages should be used without modification\. Removing dependencies, adding conflicting dependencies, or selectively including classes from the packages can result in unexpected behavior\. 

You can also invoke a Lambda function on demand\. For details, see [Other Event Sources: Invoking a Lambda Function On Demand](#api-gateway-with-lambda)\.

For examples of events that are published by these event sources, see [Sample Events Published by Event Sources](eventsources.md)\.

**Topics**
+ [Amazon S3](#supported-event-source-s3)
+ [Amazon DynamoDB](#supported-event-source-dynamo-db)
+ [Amazon Kinesis Data Streams](#supported-event-source-kinesis-streams)
+ [Amazon Simple Notification Service](#supported-event-source-sns)
+ [Amazon Simple Email Service](#supported-event-source-ses)
+ [Amazon Cognito](#supported-event-source-cognito)
+ [AWS CloudFormation](#supported-event-source-cloudformation)
+ [Amazon CloudWatch Logs](#supported-event-source-cloudwatch-logs)
+ [Amazon CloudWatch Events](#supported-event-source-cloudwatch-events)
+ [AWS CodeCommit](#supported-event-source-codecommit)
+ [Scheduled Events \(powered by Amazon CloudWatch Events\)](#supported-event-source-scheduled-events)
+ [AWS Config](#supported-event-source-config)
+ [Amazon Alexa](#supported-event-source-echo)
+ [Amazon Lex](#supported-event-source-lex)
+ [Amazon API Gateway](#supported-event-source-api-gateway)
+ [AWS IoT Button](#supported-event-source-iot-button)
+ [Amazon CloudFront](#supported-event-source-cloudfront)
+ [Amazon Kinesis Data Firehose](#supported-event-source-kinesis-firehose)
+ [Other Event Sources: Invoking a Lambda Function On Demand](#api-gateway-with-lambda)
+ [Sample Events Published by Event Sources](eventsources.md)

## Amazon S3<a name="supported-event-source-s3"></a>

You can write Lambda functions to process S3 bucket events, such as the object\-created or object\-deleted events\. For example, when a user uploads a photo to a bucket, you might want Amazon S3 to invoke your Lambda function so that it reads the image and creates a thumbnail for the photo\.

You can use the bucket notification configuration feature in Amazon S3 to configure the event source mapping, identifying the bucket events that you want Amazon S3 to publish and which Lambda function to invoke\. 

For an example Amazon S3 event, see [Event Message Structure](http://docs.aws.amazon.com/AmazonS3/latest/dev/notification-content-structure.html), [Amazon S3 Put Sample Event](eventsources.md#eventsources-s3-put), and [Amazon S3 Delete Sample Event](eventsources.md#eventsources-s3-delete)\. For an example use case, see [Using AWS Lambda with Amazon S3](with-s3.md)\.

Error handling for a given event source depends on how Lambda is invoked\. Amazon S3 invokes your Lambda function asynchronously\. For more information on how errors are retried, see [Understanding Retry Behavior](retries-on-errors.md)\.

## Amazon DynamoDB<a name="supported-event-source-dynamo-db"></a>

You can use Lambda functions as triggers for your Amazon DynamoDB table\. Triggers are custom actions you take in response to updates made to the DynamoDB table\. To create a trigger, first you enable Amazon DynamoDB Streams for your table\. AWS Lambda polls the stream and your Lambda function processes any updates published to the stream\.

This is a stream\-based event source\. For stream\-based service, you create event source mapping in AWS Lambda, identifying the stream to poll and which Lambda function to invoke\.

For an example DynamoDB event, see [Step 2\.3\.2: Test the Lambda Function \(Invoke Manually\)](with-dynamodb-create-function.md#with-dbb-invoke-manually) and [Amazon DynamoDB Update Sample Event](eventsources.md#eventsources-ddb-update)\. For general format, see [GetRecord](http://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_Operations_Amazon_DynamoDB_Streams.htmlAPI_GetRecords.html) in the *Amazon DynamoDB API Reference*\. For an example use case, see [Using AWS Lambda with Amazon DynamoDB](with-ddb.md)\.

Error handling for a given event source depends on how Lambda is invoked\. DynamoDB is a stream\-based event source\. For more information on how errors are retried, see [Understanding Retry Behavior](retries-on-errors.md)\.

## Amazon Kinesis Data Streams<a name="supported-event-source-kinesis-streams"></a>

You can configure AWS Lambda to automatically poll your stream and process any new records such as website click streams, financial transactions, social media feeds, IT logs, and location\-tracking events\. Then, AWS Lambda polls the stream periodically \(once per second\) for new records\.

For stream\-based service, you create event source mapping in AWS Lambda, identifying the stream to poll and which Lambda function to invoke\.

For an example event, see [Step 2\.3: Create the Lambda Function and Test It Manually](with-kinesis-example-upload-deployment-pkg.md) and [Amazon Kinesis Data Streams Sample Event](eventsources.md#eventsources-kinesis-streams)\. For an example use case, see [Using AWS Lambda with Kinesis](with-kinesis.md)\.

Error handling for a given event source depends on how Lambda is invoked\. Amazon Kinesis Data Streams is a stream\-based event source\. For more information on how errors are retried, see [Understanding Retry Behavior](retries-on-errors.md)\.

## Amazon Simple Notification Service<a name="supported-event-source-sns"></a>

You can write Lambda functions to process Amazon Simple Notification Service notifications\. When a message is published to an Amazon SNS topic, the service can invoke your Lambda function by passing the message payload as parameter\. Your Lambda function code can then process the event, for example publish the message to other Amazon SNS topics, or send the message to other AWS services\. 

This also enables you to trigger a Lambda function in response to Amazon CloudWatch alarms and other AWS services that use Amazon SNS\.

You configure the event source mapping in Amazon SNS via topic subscription configuration\. For more information, see [Invoking Lambda functions using Amazon SNS notifications](http://docs.aws.amazon.com/sns/latest/dg/sns-lambda.html) in the *Amazon Simple Notification Service Developer Guide*\. 

For an example event, see [Appendix: Message and JSON Formats](http://docs.aws.amazon.com/sns/latest/dg/json-formats.html) and [Amazon SNS Sample Event](eventsources.md#eventsources-sns)\. For an example use case, see [Using AWS Lambda with Amazon SNS from Different Accounts](with-sns.md)\.

When a user calls the SNS Publish API on a topic that your Lambda function is subscribed to, Amazon SNS will call Lambda to invoke your function asynchronously\. Lambda will then return a delivery status\. If there was an error calling Lambda, Amazon SNS will retry invoking the Lambda function up to three times\. After three tries, if Amazon SNS still could not successfully invoke the Lambda function, then Amazon SNS will send a delivery status failure message to CloudWatch\.

Error handling for a given event source depends on how Lambda is invoked\. Amazon SNS invokes your Lambda function asynchronously\. For more information on how errors are retried, see [Understanding Retry Behavior](retries-on-errors.md)\.

## Amazon Simple Email Service<a name="supported-event-source-ses"></a>

Amazon Simple Email Service \(Amazon SES\) is a cost\-effective email service\. With Amazon SES, in addition to sending emails, you can also use the service to receive messages\. For more information about Amazon SES, see [Amazon Simple Email Service](https://aws.amazon.com/ses/)\. When you use Amazon SES to receive messages, you can configure Amazon SES to call your Lambda function when messages arrive\. The service can then invoke your Lambda function by passing in the incoming email event, which in reality is an Amazon SES message in an Amazon SNS event, as a parameter\. For example scenarios, see [Considering Your Use Case for Amazon SES Email Receiving](http://docs.aws.amazon.com/ses/latest/DeveloperGuide/receiving-email-consider-use-case.html)\.

You configure event source mapping using the rule configuration in Amazon SES\. The following topics provide additional information in the *Amazon Simple Email Service Developer Guide*:
+ For sample events, see [Lambda Action](http://docs.aws.amazon.com/ses/latest/DeveloperGuide/receiving-email-action-lambda.html) and [Amazon SES Email Receiving Sample Event](eventsources.md#eventsources-ses-email-receiving)\.
+ For Lambda function examples, see [Lambda Function Examples](http://docs.aws.amazon.com/ses/latest/DeveloperGuide/receiving-email-action-lambda-example-functions.html)\.

Error handling for a given event source depends on how Lambda is invoked\. Amazon SES invokes your Lambda function asynchronously\. For more information on how errors are retried, see [Understanding Retry Behavior](retries-on-errors.md)\.

## Amazon Cognito<a name="supported-event-source-cognito"></a>

The Amazon Cognito Events feature enables you to run Lambda function in response to events in Amazon Cognito\. For example, you can invoke a Lambda function for the Sync Trigger events, that is published each time a dataset is synchronized\. To learn more and walk through an example, see [Introducing Amazon Cognito Events: Sync Triggers](http://mobile.awsblog.com/post/Tx31I7KB4XXQUL3/Introducing-Amazon-Cognito-Events-Sync-Triggers) in the Mobile Development blog\. 

You configure event source mapping using Amazon Cognito event subscription configuration\. For information about event source mapping and a sample event, see [Amazon Cognito Events](http://docs.aws.amazon.com/cognito/devguide/sync/cognito-events/) in the *Amazon Cognito Developer Guide*\. For another example event, see [Amazon Cognito Sync Trigger Sample Event](eventsources.md#eventsources-cognito-sync-trigger)

Error handling for a given event source depends on how Lambda is invoked\. Amazon Cognito is configured to invoke a Lambda function synchronously\. For more information on how errors are retried, see [Understanding Retry Behavior](retries-on-errors.md)\.

## AWS CloudFormation<a name="supported-event-source-cloudformation"></a>

As part of deploying AWS CloudFormation stacks, you can specify a Lambda function as a custom resource to execute any custom commands\. Associating a Lambda function with a custom resource enables you to invoke your Lambda function whenever you create, update, or delete AWS CloudFormation stacks\.

You configure event source mapping in AWS CloudFormation using stack definition\. For more information, see [AWS Lambda\-backed Custom Resources](http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/template-custom-resources-lambda.html) in the *AWS CloudFormation User Guide*\. 

For an example event, see [AWS CloudFormation Create Request Sample Event](eventsources.md#eventsources-cloudformation-create-request)\. Note that this event is actually a AWS CloudFormation message in an Amazon SNS event\.

Error handling for a given event source depends on how Lambda is invoked\. AWS CloudFormation invokes your Lambda function synchronously\. For more information on how errors are retried, see [Understanding Retry Behavior](retries-on-errors.md)\.

## Amazon CloudWatch Logs<a name="supported-event-source-cloudwatch-logs"></a>

 You can use AWS Lambda functions to perform custom analysis on Amazon CloudWatch Logs using CloudWatch Logs subscriptions\. CloudWatch Logs subscriptions provide access to a real\-time feed of log events from CloudWatch Logs and deliver it to your AWS Lambda function for custom processing, analysis, or loading to other systems\. For more information about CloudWatch Logs, see [Monitoring Log Files](http://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/WhatIsCloudWatchLogs.html)\.

You maintain event source mapping in Amazon CloudWatch Logs using the log subscription configuration\. For more information, see [Real\-time Processing of Log Data with Subscriptions \(Example 2: AWS Lambda\)](http://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/SubscriptionFilters.html#LambdaFunctionExample) in the *Amazon CloudWatch User Guide*\.

For an example event, see [Amazon CloudWatch Logs Sample Event](eventsources.md#eventsources-cloudwatch-logs)\.

Error handling for a given event source depends on how Lambda is invoked\. Amazon CloudWatch Logs invokes your Lambda function asynchronously \(invoking a Lambda function does not block write operation into the logs\)\. For more information on how errors are retried, see [Understanding Retry Behavior](retries-on-errors.md)\.

## Amazon CloudWatch Events<a name="supported-event-source-cloudwatch-events"></a>

[Amazon CloudWatch Events](http://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/WhatIsCloudWatchEvents.html) help you to respond to state changes in your AWS resources\. When your resources change state, they automatically send events into an event stream\. You can create rules that match selected events in the stream and route them to your AWS Lambda function to take action\. For example, you can automatically invoke an AWS Lambda function to log the state of an [EC2 instance](http://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/LogEC2InstanceState.html) or [AutoScaling Group](http://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/LogASGroupState.html)\. 

You maintain event source mapping in Amazon CloudWatch Events by using a rule target definition\. For more information, see the [PutTargets](http://docs.aws.amazon.com/AmazonCloudWatchEvents/latest/APIReference/API_PutTargets.html) operation in the *Amazon CloudWatch Events API Reference*\.

For sample events, see [Supported Event Types](http://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/EventTypes.html) in the *Amazon CloudWatch User Guide*\.

Error handling for a given event source depends on how Lambda is invoked\. Amazon CloudWatch Events invokes your Lambda function asynchronously\. For more information on how errors are retried, see [Understanding Retry Behavior](retries-on-errors.md)\.

## AWS CodeCommit<a name="supported-event-source-codecommit"></a>

You can create a trigger for an AWS CodeCommit repository so that events in the repository will invoke a Lambda function\. For example, you can invoke a Lambda function when a branch or tag is created or when a push is made to an existing branch\. For more information, see [Manage Triggers for an AWS CodeCommit Repository](http://docs.aws.amazon.com/codecommit/latest/userguide/how-to-notify.html)\.

You maintain the event source mapping in AWS CodeCommit by using a repository trigger\. For more information, see the [PutRepositoryTriggers](http://docs.aws.amazon.com/codecommit/latest/APIReference/API_PutRepositoryTriggers.html) operation\.

Error handling for a given event source depends on how Lambda is invoked\. AWS CodeCommit invokes your Lambda function asynchronously\. For more information on how errors are retried, see [Understanding Retry Behavior](retries-on-errors.md)\.

## Scheduled Events \(powered by Amazon CloudWatch Events\)<a name="supported-event-source-scheduled-events"></a>

You can also set up AWS Lambda to invoke your code on a regular, scheduled basis using the schedule event capability in Amazon CloudWatch Events\. To set a schedule you can specify a fixed rate \(number of hours, days, or weeks\) or specify a cron expression \(see [Schedule Expression Syntax for Rules](http://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/ScheduledEvents.html) in the *Amazon CloudWatch User Guide*\)\.

You maintain event source mapping in Amazon CloudWatch Events by using a rule target definition\. For more information, see the [PutTargets](http://docs.aws.amazon.com/AmazonCloudWatchEvents/latest/APIReference/API_PutTargets.html) operation in the *Amazon CloudWatch Events API Reference*\.

For an example use case, see [Using AWS Lambda with Scheduled Events](with-scheduled-events.md)\.

For an example event, see [Scheduled Event Sample Event](eventsources.md#eventsources-scheduled-event)\.

Error handling for a given event source depends on how Lambda is invoked\. Amazon CloudWatch Events is configured to invoke a Lambda function asynchronously\. For more information on how errors are retried, see [Understanding Retry Behavior](retries-on-errors.md)\.

## AWS Config<a name="supported-event-source-config"></a>

You can use AWS Lambda functions to evaluate whether your AWS resource configurations comply with your custom Config rules\. As resources are created, deleted, or changed, AWS Config records these changes and sends the information to your Lambda functions\. Your Lambda functions then evaluate the changes and report results to AWS Config\. You can then use AWS Config to assess overall resource compliance: you can learn which resources are noncompliant and which configuration attributes are the cause of noncompliance\. 

You maintain event source mapping in AWS Config by using a rule target definition\. For more information, see the [PutConfigRule](http://docs.aws.amazon.com/config/latest/APIReference/API_PutConfigRule.html) operation in the *AWS Config API reference*\.

For more information, see [Evaluating Resources With AWS Config Rules](http://docs.aws.amazon.com/config/latest/developerguide/evaluate-config.html)\. For an example of setting a custom rule, see [Developing a Custom Rule for AWS Config](http://docs.aws.amazon.com/config/latest/developerguide/evaluate-config_develop-rules_nodejs.html)\. For example Lambda functions, see [Example AWS Lambda Functions for AWS Config Rules \(Node\.js\)](http://docs.aws.amazon.com/config/latest/developerguide/evaluate-config_develop-rules_nodejs-sample.html)\. 

Error handling for a given event source depends on how Lambda is invoked\. AWS Config is configured to invoke a Lambda function asynchronously\. For more information on how errors are retried, see [Understanding Retry Behavior](retries-on-errors.md)\.

## Amazon Alexa<a name="supported-event-source-echo"></a>

You can use Lambda functions to build services that give new skills to Alexa, the Voice assistant on Amazon Echo\. The Alexa Skills Kit provides the APIs, tools, and documentation to create these new skills, powered by your own services running as Lambda functions\. Amazon Echo users can access these new skills by asking Alexa questions or making requests\. For more information, see:
+ [Getting Started with Alexa Skills Kit](https://developer.amazon.com/appsandservices/solutions/alexa/alexa-skills-kit/getting-started-guide)\.
+ [alexa\-skills\-kit\-sdk\-for\-nodejs ](https://github.com/alexa/alexa-skills-kit-sdk-for-nodejs)
+ [alexa\-skills\-kit\-java ](https://github.com/amzn/alexa-skills-kit-java)

Error handling for a given event source depends on how Lambda is invoked\. Amazon Echo is configured to invoke a Lambda function synchronously\. For more information on how errors are retried, see [Understanding Retry Behavior](retries-on-errors.md)\.

## Amazon Lex<a name="supported-event-source-lex"></a>

Amazon Lex is an AWS service for building conversational interfaces into applications using voice and text\. Amazon Lex provides pre\-build integration with AWS Lambda, allowing you to create Lambda functions for use as code hook with your Amazon Lex bot\. In your intent configuration, you can identify your Lambda function to perform initialization/validation, fulfillment, or both\. 

For more information, see [Using Lambda Functions](http://docs.aws.amazon.com/lex/latest/dg//using-lambda.html)\. For an example use case, see [Exercise 1: Create Amazon Lex Bot Using a Blueprint](http://docs.aws.amazon.com/lex/latest/dg/gs-bp.html)\.

Error handling for a given event source depends on how Lambda is invoked\. Amazon Lex is configured to invoke a Lambda function synchronously\. For more information on how errors are retried, see [Understanding Retry Behavior](retries-on-errors.md)\.

## Amazon API Gateway<a name="supported-event-source-api-gateway"></a>

You can invoke a Lambda function over HTTPS\. You can do this by defining a custom REST API endpoint using Amazon API Gateway\. You map individual API operations, such as `GET` and `PUT`, to specific Lambda functions\. When you send an HTTPS request to the API endpoint, the Amazon API Gateway service invokes the corresponding Lambda function\.

For more information, see [Make Synchronous Calls to Lambda Functions](http://docs.aws.amazon.com/apigateway/latest/developerguide/getting-started.html)\. For an example use case, see [Using AWS Lambda with Amazon API Gateway \(On\-Demand Over HTTPS\)](with-on-demand-https.md)\.

Error handling for a given event source depends on how Lambda is invoked\. Amazon API Gateway is configured to invoke a Lambda function synchronously\. For more information on how errors are retried, see [Understanding Retry Behavior](retries-on-errors.md)\.

In addition, you can also use Lambda functions with other AWS services that publish data to one of the supported AWS event sources listed in this topic\. For example, you can:
+ Trigger Lambda functions in response to CloudTrail updates because it records all API access events to an Amazon S3 bucket\.
+ Trigger Lambda functions in response to CloudWatch alarms because it publishes alarm events to an Amazon SNS topic\.

## AWS IoT Button<a name="supported-event-source-iot-button"></a>

The AWS IoT button is a programmable button based on the Amazon Dash Button hardware\. This simple Wi\-Fi device is easy to configure and designed for developers to get started with AWS Lambda, among many other AWS services, without writing device\-specific code\.

You can code the button's logic in the cloud to configure button clicks to count or track items, call or alert someone, start or stop something, order services, or even provide feedback\. For example, you can click the button to unlock or start a car, open your garage door, call a cab, call your spouse or a customer service representative, track the use of common household chores, medications or products, or remotely control your home appliances\.

Error handling for a given event source depends on how Lambda is invoked\. AWS IoT is configured to invoke a Lambda function asynchronously\. For more information on how errors are retried, see [Understanding Retry Behavior](retries-on-errors.md)\.

## Amazon CloudFront<a name="supported-event-source-cloudfront"></a>

Lambda@Edge lets you run Lambda functions at AWS Regions and Amazon CloudFront edge locations in response to CloudFront events, without provisioning or managing servers\. You can use Lambda functions to change CloudFront requests and responses at the following points:
+ After CloudFront receives a request from a viewer \(viewer request\)
+ Before CloudFront forwards the request to the origin \(origin request\)
+ After CloudFront receives the response from the origin \(origin response\)
+ Before CloudFront forwards the response to the viewer \(viewer response\)

For more information, see [AWS Lambda@Edge](lambda-edge.md)

Error handling for a given event source depends on how Lambda is invoked\. CloudFront is configured to invoke a Lambda function synchronously\. For more information on how errors are retried, see [Understanding Retry Behavior](retries-on-errors.md)\.

## Amazon Kinesis Data Firehose<a name="supported-event-source-kinesis-firehose"></a>

Amazon Kinesis Data Firehose is the easiest way to load streaming data into AWS\. It can capture, transform, and load streaming data into downstream services such as Kinesis Data Analytics or Amazon S3, enabling near real\-time analytics with existing business intelligence tools and dashboards you’re already using today\. You can write Lambda functions to request additional, customized processing of the data before it is sent downstream\.

Error handling for a given event source depends on how Lambda is invoked\. Kinesis Data Firehose is configured to invoke a Lambda function synchronously\. For more information on how errors are retried, see [Understanding Retry Behavior](retries-on-errors.md)\.

## Other Event Sources: Invoking a Lambda Function On Demand<a name="api-gateway-with-lambda"></a>

In addition to invoking Lambda functions using event sources, you can also invoke your Lambda function on demand\. You don't need to preconfigure any event source mapping in this case\. However, make sure that the custom application has the necessary permissions to invoke your Lambda function\.

 For example, user applications can also generate events \(build your own custom event sources\)\. User applications such as client, mobile, or web applications can publish events and invoke Lambda functions using the AWS SDKs or AWS Mobile SDKs such as the AWS Mobile SDK for Android\. 

For more information, see [Tools for Amazon Web Services](https://aws.amazon.com/tools/)\. For an example tutorial, see [Using AWS Lambda with Amazon API Gateway \(On\-Demand Over HTTPS\)](with-on-demand-https.md)\.