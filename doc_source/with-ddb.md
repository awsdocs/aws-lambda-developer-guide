# Using AWS Lambda with Amazon DynamoDB<a name="with-ddb"></a>

 You can use Lambda functions as triggers for your Amazon DynamoDB table\. Triggers are custom actions you take in response to updates made to the DynamoDB table\. To create a trigger, first you enable Amazon DynamoDB Streams for your table\. Then, you write a Lambda function to process the updates published to the stream\. 

Note the following about how the Amazon DynamoDB and AWS Lambda integration works:
+ **Stream\-based model** – This is a model \(see [Event Source Mapping](invocation-options.md#intro-invocation-modes)\), where AWS Lambda polls the stream at a rate of 4 times per second and, when it detects new records, invokes your Lambda function by passing the update event as parameter\. 

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

For a tutorial that walks you through an example setup, see [Tutorial: Using AWS Lambda with Amazon DynamoDB](with-ddb-example.md)\.