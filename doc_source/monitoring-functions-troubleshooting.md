# AWS Lambda Troubleshooting Scenarios<a name="monitoring-functions-troubleshooting"></a>

 This section describes examples of how to monitor and troubleshoot your Lambda functions using the logging and monitoring capabilities of CloudWatch\. 

## Troubleshooting Scenario 1: Lambda Function Not Working as Expected<a name="monitoring-functions-troubleshooting-one"></a>

 In this scenario, you have just finished [Tutorial: Using AWS Lambda with Amazon S3](with-s3-example.md)\. However, the Lambda function you created to upload a thumbnail image to Amazon S3 when you create an S3 object is not working as expected\. When you upload objects to Amazon S3, you see that the thumbnail images are not being uploaded\. You can troubleshoot this issue in the following ways\. 

**To determine why your Lambda function is not working as expected**

1. Check your code and verify that it is working correctly\. An increased error rate would indicate that it is not\.

   You can test your code locally as you would any other Node\.js function, or you can test it within the Lambda console using the console's test invoke functionality, or you can use the AWS CLI `Invoke` command\. Each time the code is executed in response to an event, it writes a log entry into the log group associated with a Lambda function, which is /aws/lambda/*<function name>*\. 

   Following are some examples of errors that might show up in the logs:
   + If you see a stack trace in your log, there is probably an error in your code\. Review your code and debug the error that the stack trace refers to\.
   + If you see a `permissions denied` error in the log, the IAM role you have provided as an execution role may not have the necessary permissions\. Check the IAM role and verify that it has all of the necessary permissions to access any AWS resources that your code references\. To ensure that you have correctly set up the execution role, see [AWS Lambda Execution Role](lambda-intro-execution-role.md)\.
   +  If you see a `timeout exceeded` error in the log, your function was terminated because it did not return prior to the configured timeout\. This may be because the timeout is too low, or the code is taking too long to execute\.
   +  If you see a `memory exceeded` error in the log, your memory setting is too low\. Set it to a higher value\. For information about memory size limits, see [CreateFunction](API_CreateFunction.md)\.

1. Check your Lambda function and verify that it is receiving requests\. 

    Even if your function code is working as expected and responding correctly to test invokes, the function may not be receiving requests from Amazon S3\. If Amazon S3 is able to invoke the function, you should see an increase in your CloudWatch requests metrics\. If you do not see an increase in your CloudWatch requests, check the access permissions policy associated with the function\.

## Troubleshooting Scenario 2: Increased Duration in Lambda Function Execution<a name="monitoring-functions-troubleshooting-two"></a>

 In this scenario, you have just finished [Tutorial: Using AWS Lambda with Amazon S3](with-s3-example.md)\. However, the Lambda function you created to upload a thumbnail image to Amazon S3 when you create an S3 object is not working as expected\. When you upload objects to Amazon S3, you can see that the thumbnail images are being uploaded, but your code is taking much longer to execute than expected\. You can troubleshoot this issue in a couple of different ways\. For example, you could monitor the duration metric for the Lambda function to see if the execution time is increasing\. Or you could see an increase in the CloudWatch errors metric for the Lambda function, which might be due to timeout errors\.

**To determine why there is increased duration in the execution of a Lambda function**

1. Test your code with different memory settings\.

   If your code is taking too long to execute, it could be that it does not have enough compute resources to execute its logic\. Try increasing the memory allocated to your function and testing the code again, using the Lambda console's test invoke functionality\. You can see the memory used, code execution time, and memory allocated in the function log entries\. Changing the memory setting can change how you are charged for execution time\. For information about pricing, see [AWS Lambda](https://aws.amazon.com/lambda/)\.

1. Use logs to investigate the source of the execution bottleneck

   You can test your code locally, as you would with any other Node\.js function, or you can test it within Lambda using the test invoke capability on the Lambda console, or using the `asyncInvoke` command by using AWS CLI\. Each time the code is executed in response to an event, it writes a log entry into the log group associated with a Lambda function, which is named aws/lambda/*<function name>*\. Add logging statements around various parts of your code, such as callouts to other services, to see how much time it takes to execute different parts of your code\. 