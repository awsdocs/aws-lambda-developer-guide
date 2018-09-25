# Step 2: Create a Lambda Function and Invoke It Manually \(Using Sample Event Data\)<a name="with-ddb-create-test-function"></a>

In this section, you do the following:
+ Create a Lambda function deployment package using the sample code provided\. The sample Lambda function code that you'll use to process DynamoDB events is provided in various languages\. Select one of the languages and follow the corresponding instructions to create a deployment package\.
**Note**  
To see more examples of using other AWS services within your function, including calling other Lambda functions, see [AWS SDK for JavaScript](https://docs.aws.amazon.com/AWSJavaScriptSDK/latest/frames.html)
+ Create an IAM role \(execution role\)\. At the time you upload the deployment package, you need to specify an IAM role \(execution role\) that Lambda can assume to execute the function on your behalf\. For example, AWS Lambda needs permissions for DynamoDB actions so it can poll the stream and read records from the stream\. In the *pull* model you must also grant AWS Lambda permissions to invoke your Lambda function\. The example Lambda function writes some of the event data to CloudWatch, so it needs permissions for necessary CloudWatch actions\. 
+ Create the Lambda function by uploading the deployment package, and then test it by invoking it manually using sample DynamoDB event data\. You provide both the deployment package and the IAM role at the time of creating a Lambda function\. You can also specify other configuration information, such as the function name, memory size, runtime environment to use, and the handler\. For more information about these parameters, see [CreateFunction](API_CreateFunction.md)\. After creating the Lambda function, you invoke it using sample Amazon DynamoDB event data\. 

**Topics**
+ [Step 2\.1: Create a Lambda Function Deployment Package](with-dynamodb-create-package.md)
+ [Step 2\.2: Create the Execution Role \(IAM Role\)](with-dynamodb-create-execution-role.md)
+ [Step 2\.3: Create the Lambda Function and Test It Manually](with-dynamodb-create-function.md)