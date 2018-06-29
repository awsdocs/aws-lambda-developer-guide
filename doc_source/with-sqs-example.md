# Tutorial: Using AWS Lambda with Amazon Simple Queue Service<a name="with-sqs-example"></a>

 In this tutorial, you create a Lambda function to consume messages from an [Amazon SQS](https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/Welcome.html) queue\. 

 The tutorial is divided into two main sections: 
+  First, you perform the necessary setup to create a Lambda function and then you test it by invoking it manually using sample event data\. 
+ Second, you create an Amazon SQS queue and add event source mapping in AWS Lambda to associate the queue with your Lambda function\. AWS Lambda starts polling the queue\. Then, you test the end\-to\-end setup\. AWS Lambda detects the new messages as it polls the queue and executes your Lambda function\. 

## Next Step<a name="wt-sqs-next-step"></a>

[Step 1: Prepare](with-sqs-prepare.md)