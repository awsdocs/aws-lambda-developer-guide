# Basic AWS Lambda Function Configuration<a name="resource-model"></a>

A Lambda function consists of code and any associated dependencies\. In addition, a Lambda function also has configuration information associated with it\. Initially, you specify the configuration information when you create a Lambda function\. Lambda provides an API for you to update some of the configuration data\.

**To configure function settings**

1. Open the AWS Lambda console at [https://console\.aws\.amazon\.com/lambda/](https://console.aws.amazon.com/lambda/)\.

1. Choose a function\.

1. Settings are on the first page shown, beneath the **Function code** editor\. If you choose other resources in the **Designer** panel, choose the function node to return to function settings\.

1. Configure any of the available options and then choose **Save**\.

**Settings**
+ **Memory** – Specify the amount of memory you want to allocate for your Lambda function\. AWS Lambda allocates CPU power proportional to the memory by using the same ratio as a general purpose Amazon EC2 instance type, such as an M3 type\. For example, if you allocate 256 MB memory, your Lambda function will receive twice the CPU share than if you allocated only 128 MB\. 

  You can update the configuration and request additional memory in 64 MB increments from 128MB to 3008 MB\. For information about relevant limits, see [AWS Lambda Limits](limits.md)\.

  You can also update the memory size of your functions using the following AWS CLI command\.

  ```
  $ aws lambda update-function-configuration --function-name myfunction --memory-size 256
  ```
+ **Timeout** – You pay for the AWS resources that are used to run your Lambda function\. To prevent your Lambda function from running indefinitely, you specify a timeout\. When the specified timeout is reached, AWS Lambda terminates execution of your Lambda function\. We recommend you set this value based on your expected execution time\. The default timeout is 3 seconds\.
**Note**  
You can invoke a Lambda function synchronously either by calling the `Invoke` operation or by using an AWS SDK in your preferred runtime\. If you anticipate a long\-running Lambda function, your client may time out before function execution completes\. To avoid this, update the client timeout or your SDK configuration\. For more information, see [Invoke](API_Invoke.md)\. 
+ **Execution role** – The role that AWS Lambda assumes when it executes the Lambda function\. For more information, see [AWS Lambda Execution Role](lambda-intro-execution-role.md)\.
+ **Handler name** – The handler method is the entry point that executes your Lambda function code and any event source dependencies that you have included as part of your Lambda function\. This setting appears in the function code editor\.