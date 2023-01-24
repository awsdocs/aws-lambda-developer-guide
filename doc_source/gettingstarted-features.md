# Lambda features<a name="gettingstarted-features"></a>

Lambda provides a management console and API for managing and invoking functions\. It provides runtimes that support a standard set of features so that you can easily switch between languages and frameworks, depending on your needs\. In addition to functions, you can also create versions, aliases, layers, and custom runtimes\.

**Topics**
+ [Scaling](#gettingstarted-features-scaling)
+ [Concurrency controls](#gettingstarted-features-concurrency)
+ [Function URLs](#gettingstarted-features-urls)
+ [Asynchronous invocation](#gettingstarted-features-async)
+ [Event source mappings](#gettingstarted-features-eventsourcemapping)
+ [Destinations](#gettingstarted-features-destinations)
+ [Function blueprints](#gettingstarted-features-blueprints)
+ [Testing and deployment tools](#gettingstarted-features-tools)
+ [Application templates](#gettingstarted-features-templates)

## Scaling<a name="gettingstarted-features-scaling"></a>

Lambda manages the infrastructure that runs your code, and scales automatically in response to incoming requests\. When your function is invoked more quickly than a single instance of your function can process events, Lambda scales up by running additional instances\. When traffic subsides, inactive instances are frozen or stopped\. You pay only for the time that your function is initializing or processing events\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling.png)

For more information, see [Lambda function scaling](invocation-scaling.md)\.

## Concurrency controls<a name="gettingstarted-features-concurrency"></a>

Use concurrency settings to ensure that your production applications are highly available and highly responsive\. To prevent a function from using too much concurrency, and to reserve a portion of your account's available concurrency for a function, use *reserved concurrency*\. Reserved concurrency splits the pool of available concurrency into subsets\. A function with reserved concurrency only uses concurrency from its dedicated pool\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-concurrency-reserved.png)

To enable functions to scale without fluctuations in latency, use *provisioned concurrency*\. For functions that take a long time to initialize, or that require extremely low latency for all invocations, provisioned concurrency enables you to pre\-initialize instances of your function and keep them running at all times\. Lambda integrates with Application Auto Scaling to support autoscaling for provisioned concurrency based on utilization\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-scaling-provisioned-auto.png)

For more information, see [Managing Lambda reserved concurrency](configuration-concurrency.md)\.

## Function URLs<a name="gettingstarted-features-urls"></a>

Lambda offers built\-in HTTP\(S\) endpoint support through *function URLs*\. With function URLs, you can assign a dedicated HTTP endpoint to your Lambda function\. When your function URL is configured, you can use it to invoke your function through a web browser, curl, Postman, or any HTTP client\.

You can add a function URL to an existing function, or create a new function with a function URL\. For more information, see [Invoking Lambda function URLs](urls-invocation.md)\.

## Asynchronous invocation<a name="gettingstarted-features-async"></a>

When you invoke a function, you can choose to invoke it synchronously or asynchronously\. With [synchronous invocation](invocation-sync.md), you wait for the function to process the event and return a response\. With asynchronous invocation, Lambda queues the event for processing and returns a response immediately\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-async.png)

For asynchronous invocations, Lambda handles retries if the function returns an error or is throttled\. To customize this behavior, you can configure error handling settings on a function, version, or alias\. You can also configure Lambda to send events that failed processing to a dead\-letter queue, or to send a record of any invocation to a [destination](#gettingstarted-features-destinations)\.

For more information, see [Asynchronous invocation](invocation-async.md)\.

## Event source mappings<a name="gettingstarted-features-eventsourcemapping"></a>

To process items from a stream or queue, you can create an *event source mapping*\. An event source mapping is a resource in Lambda that reads items from an Amazon Simple Queue Service \(Amazon SQS\) queue, an Amazon Kinesis stream, or an Amazon DynamoDB stream, and sends the items to your function in batches\. Each event that your function processes can contain hundreds or thousands of items\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-eventsourcemapping.png)

Event source mappings maintain a local queue of unprocessed items and handle retries if the function returns an error or is throttled\. You can configure an event source mapping to customize batching behavior and error handling, or to send a record of items that fail processing to a destination\.

For more information, see [Lambda event source mappings](invocation-eventsourcemapping.md)\.

## Destinations<a name="gettingstarted-features-destinations"></a>

A destination is an AWS resource that receives invocation records for a function\. For [asynchronous invocation](#gettingstarted-features-async), you can configure Lambda to send invocation records to a queue, topic, function, or event bus\. You can configure separate destinations for successful invocations and events that failed processing\. The invocation record contains details about the event, the function's response, and the reason that the record was sent\.

![\[\]](http://docs.aws.amazon.com/lambda/latest/dg/images/features-destinations.png)

For [event source mappings](#gettingstarted-features-eventsourcemapping) that read from streams, you can configure Lambda to send a record of batches that failed processing to a queue or topic\. A failure record for an event source mapping contains metadata about the batch, and it points to the items in the stream\.

For more information, see [Configuring destinations for asynchronous invocation](invocation-async.md#invocation-async-destinations) and the error handling sections of [Using AWS Lambda with Amazon DynamoDB](with-ddb.md) and [Using AWS Lambda with Amazon Kinesis](with-kinesis.md)\.

## Function blueprints<a name="gettingstarted-features-blueprints"></a>

When you create a function in the Lambda console, you can choose to start from scratch, use a blueprint, use a [container image](gettingstarted-package.md#gettingstarted-package-images), or deploy an application from the [AWS Serverless Application Repository](https://docs.aws.amazon.com/serverlessrepo/latest/devguide/what-is-serverlessrepo.html)\. A blueprint provides sample code that shows how to use Lambda with an AWS service or a popular third\-party application\. Blueprints include sample code and function configuration presets for Node\.js and Python runtimes\.

Blueprints are provided for use under the [Amazon Software License](http://aws.amazon.com/asl/)\. They are available only in the Lambda console\.

## Testing and deployment tools<a name="gettingstarted-features-tools"></a>

Lambda supports deploying code as is or as [container images](gettingstarted-package.md#gettingstarted-package-images)\. You can use AWS services and popular community tools like the Docker command line interface \(CLI\) to author, build, and deploy your Lambda functions\. To set up the Docker CLI, see [Get Docker](https://docs.docker.com/get-docker) on the Docker Docs website\. For an introduction to using Docker with AWS, see [Getting started with Amazon ECR using the AWS CLI](https://docs.aws.amazon.com/AmazonECR/latest/userguide/getting-started-cli.html) in the *Amazon Elastic Container Registry User Guide*\.

The [AWS CLI](lambda-settingup.md#lambda-settingup-awscli) and [SAM CLI](lambda-settingup.md#lambda-settingup-samcli) are command line tools for managing Lambda application stacks\. In addition to commands for managing application stacks with the AWS CloudFormation API, the AWS CLI supports higher\-level commands that simplify tasks like uploading deployment packages and updating templates\. The AWS SAM CLI provides additional functionality, including validating templates, testing locally, and integrating with CI/CD systems\.
+ [Installing the AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html)
+ [Testing and debugging serverless applications with AWS SAM](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-test-and-debug.html)
+ [Deploying serverless applications using CI/CD systems with AWS SAM](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-deploying.html)

## Application templates<a name="gettingstarted-features-templates"></a>

You can use the Lambda console to create an application with a continuous delivery pipeline\. Application templates in the Lambda console include code for one or more functions, an application template that defines functions and supporting AWS resources, and an infrastructure template that defines an AWS CodePipeline pipeline\. The pipeline has build and deploy stages that run every time you push changes to the included Git repository\.

Application templates are provided for use under the [MIT No Attribution](https://spdx.org/licenses/MIT-0.html) license\. They are available only in the Lambda console\.

For more information, see [Creating an application with continuous delivery in the Lambda console](applications-tutorial.md)\.