# Common Lambda application types and use cases<a name="applications-usecases"></a>

Lambda functions and triggers are the core components of building applications on AWS Lambda\. A Lambda function is the code and runtime that process events, while a trigger is the AWS service or application that invokes the function\. To illustrate, consider the following scenarios:
+ **File processing** – Suppose you have a photo sharing application\. People use your application to upload photos, and the application stores these user photos in an Amazon S3 bucket\. Then, your application creates a thumbnail version of each user's photos and displays them on the user's profile page\. In this scenario, you may choose to create a Lambda function that creates a thumbnail automatically\. Amazon S3 is one of the supported AWS event sources that can publish *object\-created events* and invoke your Lambda function\. Your Lambda function code can read the photo object from the S3 bucket, create a thumbnail version, and then save it in another S3 bucket\. 
+ **Data and analytics** – Suppose you are building an analytics application and storing raw data in a DynamoDB table\. When you write, update, or delete items in a table, DynamoDB streams can publish item update events to a stream associated with the table\. In this case, the event data provides the item key, event name \(such as insert, update, and delete\), and other relevant details\. You can write a Lambda function to generate custom metrics by aggregating raw data\.
+ **Websites** – Suppose you are creating a website and you want to host the backend logic on Lambda\. You can invoke your Lambda function over HTTP using Amazon API Gateway as the HTTP endpoint\. Now, your web client can invoke the API, and then API Gateway can route the request to Lambda\.
+ **Mobile applications** – Suppose you have a custom mobile application that produces events\. You can create a Lambda function to process events published by your custom application\. For example, you can configure a Lambda function to process the clicks within your custom mobile application\.

AWS Lambda supports many AWS services as event sources\. For more information, see [Using AWS Lambda with other services](lambda-services.md)\. When you configure these event sources to trigger a Lambda function, the Lambda function is invoked automatically when events occur\. You define *event source mapping*, which is how you identify what events to track and which Lambda function to invoke\. 

The following are introductory examples of event sources and how the end\-to\-end experience works\.

## Example 1: Amazon S3 pushes events and invokes a Lambda function<a name="example-lambda-pushes-events-invokes-function"></a>

Amazon S3 can publish events of different types, such as PUT, POST, COPY, and DELETE object events on a bucket\. Using the bucket notification feature, you can configure an event source mapping that directs Amazon S3 to invoke a Lambda function when a specific type of event occurs\. 

The following is a typical sequence:

1. The user creates an object in a bucket\.

1. Amazon S3 detects the object created event\.

1. Amazon S3 invokes your Lambda function using the permissions provided by the [execution role](lambda-intro-execution-role.md)\.

1. AWS Lambda runs the Lambda function, specifying the event as a parameter\.

You configure Amazon S3 to invoke your function as a bucket notification action\. To grant Amazon S3 permission to invoke the function, update the function's [resource\-based policy](access-control-resource-based.md)\.

## Example 2: AWS Lambda pulls events from a Kinesis stream and invokes a Lambda function<a name="example-lambda-pulls-kinesis-streams-events-invokes-function"></a>

For poll\-based event sources, AWS Lambda polls the source and then invokes the Lambda function when records are detected on that source\.
+ [CreateEventSourceMapping](API_CreateEventSourceMapping.md)
+ [UpdateEventSourceMapping](API_UpdateEventSourceMapping.md)

The following steps describe how a custom application writes records to a Kinesis stream:

1. The custom application writes records to a Kinesis stream\.

1. AWS Lambda continuously polls the stream, and invokes the Lambda function when the service detects new records on the stream\. AWS Lambda knows which stream to poll and which Lambda function to invoke based on the event source mapping you create in Lambda\.

1. The Lambda function is invoked with the incoming event\.

When working with stream\-based event sources, you create event source mappings in AWS Lambda\. Lambda reads items from the stream and invokes the function synchronously\. You don't need to grant Lambda permission to invoke the function, but it does need permission to read from the stream\.