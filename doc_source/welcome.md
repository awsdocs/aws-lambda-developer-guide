# What is AWS Lambda?<a name="welcome"></a>

 AWS Lambda is a compute service that lets you run code without provisioning or managing servers\. AWS Lambda executes your code only when needed and scales automatically, from a few requests per day to thousands per second\. You pay only for the compute time you consume \- there is no charge when your code is not running\. With AWS Lambda, you can run code for virtually any type of application or backend service \- all with zero administration\. AWS Lambda runs your code on a high\-availability compute infrastructure and performs all of the administration of the compute resources, including server and operating system maintenance, capacity provisioning and automatic scaling, code monitoring and logging\. All you need to do is supply your code in one of the [languages that AWS Lambda supports](lambda-runtimes.md)\. 

You can use AWS Lambda to run your code in response to events, such as changes to data in an Amazon S3 bucket or an Amazon DynamoDB table; to run your code in response to HTTP requests using Amazon API Gateway; or invoke your code using API calls made using AWS SDKs\. With these capabilities, you can use Lambda to easily build data processing triggers for AWS services like Amazon S3 and Amazon DynamoDB, process streaming data stored in Kinesis, or create your own back end that operates at AWS scale, performance, and security\.

You can also build serverless applications composed of functions that are triggered by events and automatically deploy them using CodePipeline and AWS CodeBuild\. For more information, see [AWS Lambda applications](deploying-lambda-apps.md)\.

## When should I use AWS Lambda?<a name="when-to-use-cloud-functions"></a>

AWS Lambda is an ideal compute platform for many application scenarios, provided that you can write your application code in languages supported by AWS Lambda, and run within the AWS Lambda standard runtime environment and resources provided by Lambda\. 

When using AWS Lambda, you are responsible only for your code\. AWS Lambda manages the compute fleet that offers a balance of memory, CPU, network, and other resources\. This is in exchange for flexibility, which means you cannot log in to compute instances, or customize the operating system on provided runtimes\. These constraints enable AWS Lambda to perform operational and administrative activities on your behalf, including provisioning capacity, monitoring fleet health, applying security patches, deploying your code, and monitoring and logging your Lambda functions\.

If you need to manage your own compute resources, Amazon Web Services also offers other compute services to meet your needs\. 
+ Amazon Elastic Compute Cloud \(Amazon EC2\) service offers flexibility and a wide range of EC2 instance types to choose from\. It gives you the option to customize operating systems, network and security settings, and the entire software stack, but you are responsible for provisioning capacity, monitoring fleet health and performance, and using Availability Zones for fault tolerance\.
+ Elastic Beanstalk offers an easy\-to\-use service for deploying and scaling applications onto Amazon EC2 in which you retain ownership and full control over the underlying EC2 instances\.

Lambda is a highly available service\. For more information, see the [AWS Lambda service level agreement](https://aws.amazon.com/lambda/sla/)\.

## Are you a first\-time user of AWS Lambda?<a name="welcome-first-time-user"></a>

If you are a first\-time user of AWS Lambda, we recommend that you read the following sections in order:

1. **Read the product overview and watch the introductory video to understand sample use cases\.** These resources are available on the [AWS Lambda webpage](https://aws.amazon.com/lambda/)\.

1. **Try the console\-based getting started exercise\.** The exercise provides instructions for you to create and test your first Lambda function using the console\. You also learn about the programming model and other Lambda concepts\. For more information, see [Getting started with AWS Lambda](getting-started.md)\.

1. **Read the [deploying applications with AWS Lambda](deploying-lambda-apps.md) section of this guide\. **This section introduces various AWS Lambda components you work with to create an end\-to\-end experience\.

Beyond the Getting Started exercise, you can explore the various use cases, each of which is provided with a tutorial that walks you through an example scenario\. Depending on your application needs \(for example, whether you want event driven Lambda function invocation or on\-demand invocation\), you can follow specific tutorials that meet your specific needs\. For more information, see [Using AWS Lambda with other services](lambda-services.md)\.