# Using AWS Lambda with Amazon DynamoDB<a name="with-ddb"></a>

 You can use Lambda functions as triggers for your Amazon DynamoDB table\. Triggers are custom actions you take in response to updates made to the DynamoDB table\. To create a trigger, first you enable Amazon DynamoDB Streams for your table\. Then, you write a Lambda function to process the updates published to the stream\. 

Note the following about how the Amazon DynamoDB and AWS Lambda integration works:
+ **stream\-based model** – This is a model \(see [Event Source Mapping](invocation-options.md#intro-invocation-modes)\), where AWS Lambda polls the stream at a rate of 4 times per second and, when it detects new records, invokes your Lambda function by passing the update event as parameter\. 

  In a stream\-based model, you maintain event source mapping in AWS Lambda\. The event source mapping describes which stream maps to which Lambda function\. AWS Lambda provides an API \([CreateEventSourceMapping](API_CreateEventSourceMapping.md)\) for you to create the mapping\. You can also use the AWS Lambda console to create event source mappings\. 
+ **Synchronous invocation** – AWS Lambda invokes a Lambda function using the `RequestResponse` invocation type \(synchronous invocation\)\. For more information about invocation types, see [Invocation Types](invocation-options.md)\. 
+ **Event structure** – The event your Lambda function receives is the table update information AWS Lambda reads from your stream\. When you configure event source mapping, the batch size you specify is the maximum number of records that you want your Lambda function to receive per invocation\.

Regardless of what invokes a Lambda function, AWS Lambda always executes a Lambda function on your behalf\. If your Lambda function needs to access any AWS resources, you need to grant the relevant permissions to access those resources\. You also need to grant AWS Lambda permissions to poll your DynamoDB stream\. You grant all of these permissions to an IAM role \(execution role\) that AWS Lambda can assume to poll the stream and execute the Lambda function on your behalf\. You create this role first and then enable it at the time you create the Lambda function\. For more information, see [Manage Permissions: Using an IAM Role \(Execution Role\)](intro-permission-model.md#lambda-intro-execution-role)\.

The following diagram illustrates the application flow:

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/ddb-pull-model-10.png)

1. Custom app updates the DynamoDB table\. 

1. Amazon DynamoDB publishes item updates to the stream\. 

1. AWS Lambda polls the stream and invokes your Lambda function when it detects new records in the stream\.

1. AWS Lambda executes the Lambda function by assuming the execution role you specified at the time you created the Lambda function\.

## Options for Creating the Application \(Using AWS CLI and AWS SAM\)<a name="with-ddb-two-options"></a>

The following topcis provide step\-by\-step instructions using both the AWS CLI and AWS SAM\. 
+ **Using AWS CLI** – you setup the example application using a series of AWS CLI commands\. Each CLI command makes API calls to specific AWS service\. This provides an instructive way to discover and learn about the underlying APIs\.

  For instructions to setup AWS CLI, see [Installing the AWS Command Line Interface](https://docs.aws.amazon.com/cli/latest/userguide/installing.html) in the AWS Command Line Interface User Guide\.

   
+ **Using AWS SAM** – Instead of running a series of AWS CLI commands to setup your application, you can create a configuration file describing your application\. You can then deploy the application in one or two commands\. This helps in a production environment, where you want to quickly make application configuration changes and quickly re\-deploy application updates, because you make configuration changes only in one file\.