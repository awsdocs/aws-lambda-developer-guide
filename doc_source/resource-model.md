# Configuring Lambda Functions<a name="resource-model"></a>

A Lambda function consists of code and any associated dependencies\. In addition, a Lambda function also has configuration information associated with it\. Initially, you specify the configuration information when you create a Lambda function\. Lambda provides an API for you to update some of the configuration data\. Lambda function configuration information includes the following key elements: 
+ **Compute resources that you need** – You only specify the amount of memory you want to allocate for your Lambda function\. AWS Lambda allocates CPU power proportional to the memory by using the same ratio as a general purpose Amazon EC2 instance type, such as an M3 type\. For example, if you allocate 256 MB memory, your Lambda function will receive twice the CPU share than if you allocated only 128 MB\. 

  You can update the configuration and request additional memory in 64 MB increments from 128MB to 3008 MB\. For information about relevant limits, see [AWS Lambda Limits](limits.md)\.

  To change the amount of memory your Lambda function requires, do the following:

  1. Sign in to the AWS Management Console and navigate to the AWS Lambda console\.

  1. Choose the function whose memory size you wish to change\.

  1. Click the **Configuration** tab and then expand **Advanced settings**\.

  1. In the **Memory \(MB\)** list, choose your desired amount\.

   

  Optionally, you can update the memory size of your functions using the following AWS CLI command \(using valid 64 MB increments\):

  ```
  $ aws lambda update-function-configuration \
     --function-name your function name  \
     --region region where your function resides \
     --memory-size memory amount \
     --profile adminuser
  ```

  For information on setting up and using the AWS CLI, see [Set Up the AWS Command Line Interface \(AWS CLI\)](setup-awscli.md)\.

   
+ **Maximum execution time \(timeout\)** – You pay for the AWS resources that are used to run your Lambda function\. To prevent your Lambda function from running indefinitely, you specify a timeout\. When the specified timeout is reached, AWS Lambda terminates your Lambda function\. 
**Note**  
The default SDK client will automatically time out any Lambda function request that exceeds 50 seconds\. To accommodate functions that require more processing time, increase the default timeout limit by modifying the SDK client configuration\. 

   
+ **IAM role \(execution role\)** – This is the role that AWS Lambda assumes when it executes the Lambda function on your behalf\. For more information, see [AWS Lambda Permissions Model](intro-permission-model.md)\.

   
+ **Handler name** – The handler refers to the method in your code where AWS Lambda begins execution\. AWS Lambda passes any event information, which triggered the invocation, as a parameter to the handler method\.