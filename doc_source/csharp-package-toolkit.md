# AWS Toolkit for Visual Studio<a name="csharp-package-toolkit"></a>

You can build \.NET\-based Lambda applications using the Lambda plugin for the [AWS Toolkit for Visual Studio](http://aws.amazon.com/visualstudio/)\. The toolkit is available as a [Visual Studio extension](https://marketplace.visualstudio.com/items?itemName=AmazonWebServices.AWSToolkitforVisualStudio2017)\.

1. Launch Microsoft Visual Studio and choose **New project**\.

   1. From the **File** menu, choose **New**, and then choose **Project**\.

   1. In the **New Project** window, choose **Lambda Project \(\.NET Core\)**, and then choose **OK**\.

   1. In the **Select Blueprint** window, select from the list of sample applications with sample code to help you get started with creating a \.NET\-based Lambda application\.

   1. To create a Lambda application from scratch, choose **Empty Function**, and then choose **Finish**\.

1. Review the `aws-lambda-tools-defaults.json` file, which is created as part of your project\. You can set the options in this file, which the Lambda tooling reads by default\. The project templates created in Visual Studio set many of these fields with default values\. Note the following fields:
   + **profile** – The name of a profile in your [AWS SDK for \.NET credentials file](https://docs.aws.amazon.com/sdk-for-net/v3/developer-guide/net-dg-config-creds.html)
   + **function\-handler** – The field where you specify the `function handler`\. \(This is why you don't have to set it in the wizard\.\) However, whenever you rename the *Assembly*, *Namespace*, *Class*, or *Function* in your function code, you must update the corresponding fields in the `aws-lambda-tools-defaults.json` file\.

     ```
     {
       "profile":"default",
       "region" : "us-east-2",
       "configuration" : "Release",
       "function-runtime":"dotnet6",
       "function-memory-size" : 256,
       "function-timeout" : 30,
       "function-handler" : "Assembly::Namespace.Class::Function" 
     }
     ```

1. Open the **Function\.cs** file\. You are provided with a template to implement your Lambda function handler code\.

1. After writing the code that represents your Lambda function, upload it by opening the context \(right\-click\) menu for the **Project** node in your application and then choosing **Publish to AWS Lambda**\.

1. In the **Upload Lambda Function** window, enter a name for the function, or select a previously published function to republish\. Then choose **Next**\.

1. In the **Advanced Function Details** window, configure the following options:
   + **Role Name** \(required\) – The [AWS Identity and Access Management \(IAM\) role](lambda-intro-execution-role.md) that Lambda assumes when it runs your function\.
   + **Environment** – Key\-value pairs that Lambda sets in the execution environment\. To extend your function's configuration outside of code, [use environment variables](configuration-envvars.md)\.
   + **Memory** – The amount of memory available to the function at runtime\. Choose an amount [between 128 MB and 10,240 MB](gettingstarted-limits.md) in 1\-MB increments\.
   + **Timeout** – The amount of time that Lambda allows a function to run before stopping it\. The default is three seconds\. The maximum allowed value is 900 seconds\.
   + **VPC** – If your function needs network access to resources that are not available over the internet, [configure it to connect to a virtual private cloud \(VPC\)](configuration-vpc.md)\.
   + **DLQ** – If your function is invoked asynchronously, [choose a dead\-letter queue](invocation-async.md#invocation-dlq) to receive failed invocations\.
   + **Enable active tracing** – Sample incoming requests and [trace sampled requests with AWS X\-Ray](services-xray.md)\.

1. Choose **Next**, and then choose **Upload** to deploy your application\.

For more information, see [Deploying an AWS Lambda Project with the \.NET Core CLI](https://docs.aws.amazon.com/toolkit-for-visual-studio/latest/user-guide/lambda-cli-publish.html)\.