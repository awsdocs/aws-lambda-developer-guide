# Step 2: Create a Lambda Function and Invoke It Manually<a name="with-userapp-walkthrough-custom-events-create-test-function"></a>

In this section, you do the following:

+ Create a deployment package\. A deployment package is a \.zip file that contains your code and any dependencies\. For this tutorial there are no dependencies, you only have a simple example code\.

+ Create an IAM role \(execution role\)\. At the time you upload the deployment package, you need to specify an IAM role \(execution role\) that Lambda can assume to execute the function on your behalf\. 

  You also grant this role the permissions that your Lambda function needs\. The code in this tutorial writes logs to Amazon CloudWatch Logs\. So you need to grant permissions for CloudWatch actions\. For more information, see [AWS Lambda Watch Logs](https://console.aws.amazon.com/cloudwatch/home?region=us-east-1#logs:)\.

+ Create a Lambda function \(`HelloWorld`\) using the `create-function` CLI command\. For more information about the underlying API and related parameters, see [CreateFunction](API_CreateFunction.md)\.


+ [Step 2\.1: Create a Lambda Function Deployment Package](with-userapp-walkthrough-custom-events-create-nodejs-function.md)
+ [Step 2\.2: Create the Execution Role \(IAM Role\)](with-userapp-walkthrough-custom-events-create-iam-role.md)
+ [Step 2\.3: Create a Lambda Function](with-userapp-walkthrough-custom-events-upload.md)
+ [Next Step](#with-userapp-walkthrough-custom-events-upload-next-step)

## Next Step<a name="with-userapp-walkthrough-custom-events-upload-next-step"></a>

 [Step 3: Invoke the Lambda Function \(AWS CLI\)](with-userapp-walkthrough-custom-events-invoke.md)