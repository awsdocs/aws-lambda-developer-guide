# Step 2: Create the Lambda Function and Invoke It Manually \(Using Sample Event Data\)<a name="with-on-demand-custom-android-example-create-test-manually"></a>

In this section, you do the following:
+ Create a Lambda function deployment package using the sample code provided\. The sample Lambda function code to process your mobile application events is provided in various languages\. Select one of the languages and follow the corresponding instructions to create a deployment package\.
**Note**  
To see more examples of using other AWS services within your function, including calling other Lambda functions, see [AWS SDK for JavaScript](http://docs.aws.amazon.com/AWSJavaScriptSDK/latest/frames.html)
+ Create an IAM role \(execution role\)\. At the time you upload the deployment package, you need to specify an IAM role \(execution role\)\. This is the role that AWS Lambda assumes to invoke your Lambda function on your behalf\. 
+ Create the Lambda function by uploading the deployment package, and then test it by invoking it manually using sample event data\. 

**Topics**
+ [Step 2\.1: Create a Deployment Package](with-on-demand-custom-android-example-deployment-pkg.md)
+ [Step 2\.2: Create the Execution Role \(IAM Role\)](with-on-demand-custom-android-example-create-iam-role.md)
+ [Step 2\.3: Create the Lambda Function and Invoke It Manually \(Using Sample Event Data\)](with-on-demand-custom-android-example-upload-deployment-pkg.md)