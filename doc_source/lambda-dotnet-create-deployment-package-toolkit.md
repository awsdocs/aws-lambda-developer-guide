# AWS Toolkit for Visual Studio<a name="lambda-dotnet-create-deployment-package-toolkit"></a>

You can build \.NET\-based Lambda applications using the Lambda plugin to the [AWS Toolkit for Visual Studio](https://aws.amazon.com/visualstudio/)\. The plugin is available as part of a [Nuget](https://www.nuget.org/packages/Amazon.Lambda/) package\.

## Step 1: Create and Build a Project<a name="dotnet-vs-create-project"></a>

1. Launch Microsoft Visual Studio and choose **New project**\. 

   1. From the **File** menu, choose **New**, and then choose **Project**\. 

   1. In the **New Project** window, choose **AWS Lambda Project \(\.NET Core\)** and then choose **OK**\.

   1. In the **Select Blueprint** window, you will be presented with the option of selecting from a list of sample applications that will provide you with sample code to get started with creating a \.NET\-based Lambda application\. 

   1. To create a Lambda application from scratch, choose **Blank Function** and then choose **Finish**\. 

1. Examine the `aws-lambda-tools-defaults.json` file, which is created as part of your project\. You can set the options in this file, which is read by the Lambda tooling by default\. The project templates created in Visual Studio set many of these fields with default values\. Note the following fields:
   + **profile** – Your [execution role](lambda-intro-execution-role.md)
   + **function\-handler** – This is where the `function handler` is specified, which is why you don't have to set it in the wizard\. However, whenever you rename the *Assembly*, *Namespace*, *Class* or *Function* in your function code, you will need to update the corresponding fields in the `aws-lambda-tools-defaults.json file`\.

     ```
     {
       "profile":"iam-execution-profile"",
       "region" : "region",
       "configuration" : "Release",
       "framework" : "netcoreapp2.1",
       "function-runtime":"dotnetcore2.1",
       "function-memory-size" : 256,
       "function-timeout" : 30,
       "function-handler" : "Assembly::Namespace.Class::Function" 
     }
     ```

1. Open the **Function\.cs** file\. You will be provided with a template to implement your Lambda function handler code\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/lambda-function.png)

1. Once you have written the code that represents your Lambda function, you can upload it by right\-clicking the **Project** node in your application and then choosing **Publish to AWS Lambda**\.

1. In the **Upload Lambda Function** window, type a name for the function or select a previously published function to republish\. Then choose **Next**

1. In the **Advanced Function Details** window, do the following: 
   + Specify the **Role Name:**, the IAM role mentioned previously\.
   + \(Optional\) In **Environment::** specify any environment variables you wish to use\. For more information, see [AWS Lambda Environment Variables](env_variables.md)\.
   + \(Optional\) Specify the **Memory \(MB\):** or **Timeout \(Secs\):** configurations\.
   + \(Optional\) Specify any **VPC:** configurations if your Lambda function needs to access resources running inside a VPC\. For more information, see [Configuring a Lambda Function to Access Resources in an Amazon VPC](vpc.md)\.
   + Choose **Next** and then choose **Upload** to deploy your application\.

For more information, see [Deploying an AWS Lambda Project with the \.NET Core CLI](https://docs.aws.amazon.com/toolkit-for-visual-studio/latest/user-guide/lambda-cli-publish.html)\.