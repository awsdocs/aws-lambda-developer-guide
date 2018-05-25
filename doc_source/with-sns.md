# Using AWS Lambda with Amazon SNS from Different Accounts<a name="with-sns"></a>

In order to perform cross account Amazon SNS deliveries to Lambda, you need to authorize your Lambda function to be invoked from Amazon SNS\. In turn, Amazon SNS needs to allow the Lambda account to subscribe to the Amazon SNS topic\. For example, if the Amazon SNS topic is in account A and the Lambda function is in account B, both accounts must grant permissions to the other to access their respective resources\. Since not all the options for setting up cross\-account permissions are available from the AWS console, you use the AWS CLI to set up the entire process\. 

For a tutorial that walks you through an example setup, see [Tutorial: Using AWS Lambda with Amazon SNS](with-sns-example.md)\.