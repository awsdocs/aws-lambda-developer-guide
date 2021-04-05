# What is AWS Lambda?<a name="welcome"></a>

AWS Lambda is a compute service that lets you run code without provisioning or managing servers\. Lambda runs your code only when needed and scales automatically, from a few requests per day to thousands per second\. You pay only for the compute time that you consume—there is no charge when your code is not running\. With Lambda, you can run code for virtually any type of application or backend service, all with zero administration\. Lambda runs your code on a high\-availability compute infrastructure and performs all of the administration of the compute resources, including server and operating system maintenance, capacity provisioning and automatic scaling, code monitoring and logging\. All you need to do is supply your code in one of the [languages that Lambda supports](lambda-runtimes.md)\.

You can use Lambda to run your code in response to events, such as changes to data in an Amazon Simple Storage Service \(Amazon S3\) bucket or an Amazon DynamoDB table; to run your code in response to HTTP requests using Amazon API Gateway; or to invoke your code using API calls made using AWS SDKs\. With these capabilities, you can use Lambda to build data processing triggers for AWS services such as Amazon S3 and DynamoDB, process streaming data stored in Amazon Kinesis, or create your own backend that operates at AWS scale, performance, and security\.

You can also build serverless applications composed of functions that are triggered by events, and automatically deploy them using AWS CodePipeline and AWS CodeBuild\. For more information, see [AWS Lambda applications](deploying-lambda-apps.md)\.

For more information about designing, operating, and troubleshooting Lambda\-based applications, see [Introduction](https://docs.aws.amazon.com/lambda/latest/operatorguide/intro.html) in the *Lambda operator guide*\.

**Topics**
+ [When should I use AWS Lambda?](#when-to-use-cloud-functions)
+ [Are you a first\-time user of AWS Lambda?](#welcome-first-time-user)

## When should I use AWS Lambda?<a name="when-to-use-cloud-functions"></a>

AWS Lambda is an ideal compute service for many application scenarios, provided that you can run your application code using the Lambda standard runtime environment and within the resources that Lambda provides\.

When using Lambda, you are responsible only for your code\. Lambda manages the compute fleet that offers a balance of memory, CPU, network, and other resources\. This is in exchange for flexibility, which means you cannot log in to compute instances, or customize the operating system on provided runtimes\. These constraints enable Lambda to perform operational and administrative activities on your behalf, including provisioning capacity, monitoring fleet health, applying security patches, deploying your code, and monitoring and logging your Lambda functions\.

If you need to manage your own compute resources, AWS also offers other compute services to meet your needs\. For example:
+ Amazon Elastic Compute Cloud \(Amazon EC2\) offers flexibility and a wide range of EC2 instance types to choose from\. It gives you the option to customize operating systems, network and security settings, and the entire software stack\. You are responsible for provisioning capacity, monitoring fleet health and performance, and using Availability Zones for fault tolerance\.
+ AWS Elastic Beanstalk enables you to deploy and scale applications onto Amazon EC2\. You retain ownership and full control over the underlying EC2 instances\.

Lambda is a highly available service\. For more information, see the [AWS Lambda Service Level Agreement](http://aws.amazon.com/lambda/sla/)\.

## Are you a first\-time user of AWS Lambda?<a name="welcome-first-time-user"></a>

If you are a first\-time user of AWS Lambda, we recommend that you review the following in order:

1. **Read the product overview and watch the introductory video on the [AWS Lambda webpage](http://aws.amazon.com/lambda/)\.**

1. **Try the console\-based getting started exercise\.** This exercise guides you through creating and testing a Lambda function using the AWS Management Console\. You also learn about the programming model and other Lambda concepts\. For instructions, see [Getting started with Lambda](getting-started.md)\.

1. **Try the getting started exercise for container images\.** This exercise guides you through creating and testing a Lambda function defined as a container image\. For instructions, see [Create a function defined as a container image](getting-started-create-function.md#gettingstarted-images)\.

1. **Read about deploying applications with Lambda\.** The [AWS Lambda applications](deploying-lambda-apps.md) section of this guide introduces various Lambda components that you work with to create an end\-to\-end experience\.

Beyond the getting started exercises, you can also explore the various use cases, each of which includes a tutorial that walks you through an example scenario\. Depending on your application needs \(for example, whether you want event\-driven Lambda function invocation or on\-demand invocation\), you can follow tutorials that meet your specific needs\. For more information on the various use cases, see [Using AWS Lambda with other services](lambda-services.md)\.