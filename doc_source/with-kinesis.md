# Using AWS Lambda with Kinesis<a name="with-kinesis"></a>

You can create a Kinesis stream to continuously capture and store terabytes of data per hour from hundreds of thousands of sources such as website click streams, financial transactions, social media feeds, IT logs, and location\-tracking events\. For more information, see [Kinesis](https://aws.amazon.com/kinesis/)\. 

You can subscribe Lambda functions to automatically read batches of records off your Kinesis stream and process them if records are detected on the stream\. AWS Lambda then polls the stream periodically \(once per second\) for new records\.

Note the following about how the Kinesis and AWS Lambda integration works:
+ **stream\-based model** – This is a model \(see [Event Source Mapping](invocation-options.md#intro-invocation-modes)\), where AWS Lambda polls the stream and, when it detects new records, invokes your Lambda function by passing the new records as a parameter\. 

  In a stream\-based model, you maintain event source mapping in AWS Lambda\. The event source mapping describes which stream maps to which Lambda function\. AWS Lambda provides an API \([CreateEventSourceMapping](API_CreateEventSourceMapping.md)\) that you can use to create the mapping\. You can also use the AWS Lambda console to create event source mappings\. 
+ **Synchronous invocation** – AWS Lambda invokes a Lambda function using the `RequestResponse` invocation type \(synchronous invocation\) by polling the Kinesis Stream\. For more information about invocation types, see [Invocation Types](invocation-options.md)\. 
+ **Event structure** – The event your Lambda function receives is a collection of records AWS Lambda reads from your stream\. When you configure event source mapping, the batch size you specify is the maximum number of records that you want your Lambda function to receive per invocation\.

While AWS Lambda will only poll for records once per second, it can be invoked multiple times per second provided that:
+ There were more records retrieved during the poll than the batch size will allow for a single invocation\.
+ The processing time for the function allowed it to complete before 1 second had elapsed\.

This means that the limiting factor on how many records your function can process is defined by how quickly it can process records, not how often Lambda polls for new records\.

Regardless of what invokes a Lambda function, AWS Lambda always executes a Lambda function on your behalf\. If your Lambda function needs to access any AWS resources, you need to grant the relevant permissions to access those resources\. You also need to grant AWS Lambda permissions to poll your Kinesis stream\. You grant all of these permissions to an IAM role \(execution role\) that AWS Lambda can assume to poll the stream and execute the Lambda function on your behalf\. You create this role first and then enable it at the time you create the Lambda function\. For more information, see [Manage Permissions: Using an IAM Role \(Execution Role\)](intro-permission-model.md#lambda-intro-execution-role)\.

The following diagram illustrates the application flow:

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/kinesis-pull-10.png)

1. Custom app writes records to the stream\.

1. AWS Lambda polls the stream and, when it detects new records in the stream, invokes your Lambda function\.

1. AWS Lambda executes the Lambda function by assuming the execution role you specified at the time you created the Lambda function\.

For a tutorial that walks you through an example setup, see [Tutorial: Using AWS Lambda with Kinesis](with-kinesis-example.md)\.