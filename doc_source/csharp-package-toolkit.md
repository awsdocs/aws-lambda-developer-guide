# AWS Toolkit for Visual Studio<a name="csharp-package-toolkit"></a>

You can build \.NET\-based Lambda applications using the Lambda plugin to the [AWS Toolkit for Visual Studio](https://aws.amazon.com/visualstudio/)\. The toolkit is available as a [Visual Studio extension](https://marketplace.visualstudio.com/items?itemName=AmazonWebServices.AWSToolkitforVisualStudio2017)\.

1. Launch Microsoft Visual Studio and choose **New project**\. 

   1. From the **File** menu, choose **New**, and then choose **Project**\. 

   1. In the **New Project** window, choose **AWS Lambda Project \(\.NET Core\)** and then choose **OK**\.

   1. In the **Select Blueprint** window, you will be presented with the option of selecting from a list of sample applications that will provide you with sample code to get started with creating a \.NET\-based Lambda application\. 

   1. To create a Lambda application from scratch, choose **Empty Function** and then choose **Finish**\. 

1. Examine the `aws-lambda-tools-defaults.json` file, which is created as part of your project\. You can set the options in this file, which is read by the Lambda tooling by default\. The project templates created in Visual Studio set many of these fields with default values\. Note the following fields:
   + **profile** – The name of a profile in your [AWS SDK for \.NET credentials file](https://docs.aws.amazon.com/sdk-for-net/v3/developer-guide/net-dg-config-creds.html)\.
   + **function\-handler** – This is where the `function handler` is specified, which is why you don't have to set it in the wizard\. However, whenever you rename the *Assembly*, *Namespace*, *Class* or *Function* in your function code, you will need to update the corresponding fields in the `aws-lambda-tools-defaults.json file`\.

     ```
     {
       "profile":"default",
       "region" : "us-east-2",
       "configuration" : "Release",
       "framework" : "netcoreapp2.1",
       "function-runtime":"dotnetcore3.1",
       "function-memory-size" : 256,
       "function-timeout" : 30,
       "function-handler" : "Assembly::Namespace.Class::Function" 
     }
     ```

1. Open the **Function\.cs** file\. You will be provided with a template to implement your Lambda function handler code\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/lambda-function.png)

1. Once you have written the code that represents your Lambda function, you can upload it by right\-clicking the **Project** node in your application and then choosing **Publish to AWS Lambda**\.

1. In the **Upload Lambda Function** window, type a name for the function or select a previously published function to republish\. Then choose **Next**

1. In the **Advanced Function Details** window, configure the following options:
   + **Role Name** \(required\) – The [IAM role](lambda-intro-execution-role.md) that AWS Lambda assumes when it executes your function\.
   + **Environment** – Key\-value pairs that Lambda sets in the execution environment\. [ Use environment variables](configuration-envvars.md) to extend your function's configuration outside of code\.
   + **Memory** – The amount of memory available to the function during execution\. Choose an amount [between 128 MB and 3,008 MB](gettingstarted-limits.md) in 64\-MB increments\.
   + **Timeout** – The amount of time that Lambda allows a function to run before stopping it\. The default is 3 seconds\. The maximum allowed value is 900 seconds\.
   + **VPC** – If your function needs network access to resources that are not available over the internet, [configure it to connect to a VPC](configuration-vpc.md)\.
   + **DLQ** – If your function is invoked asynchronously, [choose a queue or topic](invocation-async.md#dlq) to receive failed invocations\.
   + **Enable active tracing** – Sample incoming requests and [trace sampled requests with AWS X\-Ray](services-xray.md)\.

1. Choose **Next** and then choose **Upload** to deploy your application\.

For more information, see [Deploying an AWS Lambda Project with the \.NET Core CLI](https://docs.aws.amazon.com/toolkit-for-visual-studio/latest/user-guide/lambda-cli-publish.html)\.