# AWS Toolkit for Visual Studio<a name="lambda-dotnet-create-deployment-package-toolkit"></a>

You can build \.NET\-based Lambda applications using the Lambda plugin to the AWS Toolkit for Visual Studio\. The plugin is available as part of a Nuget package\.

## Step 1: Create and Build a Project<a name="dotnet-vs-create-project"></a>

1. Launch Microsoft Visual Studio and choose **New project**\. 

   1. From the **File** menu, choose **New**, and then choose **Project**\. 

   1. In the **New Project** window, choose **AWS Lambda Project \(\.NET Core\)** and then choose **OK**\.

   1. In the **Select Blueprint** window, you will be presented with the option of selecting from a list of sample applications that will provide you with sample code to get started with creating a \.NET\-based Lambda application\. 

   1. To create a Lambda application from scratch, choose **Blank Function** and then choose **Finish**\. 

   1. Note that the libraries necessary for you to build a \.NET\-based Lambda application are provided in the **References** node of your project\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/dotnet-references-example.png)

1. Open the **Function\.cs** file\. You will be provided with a template to implement your Lambda function handler code\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/lambda-function.png)

1. Once you have written the code that represents your Lambda function, you can upload it by right\-clicking the **Project** node in your application and then choosing **Publish to AWS Lambda**\.

1. In the **Upload Lambda Function** window, do the following: 

   + Specify the **Region:**

   + Specify the **Function Name:**

   + Specify the **Assembly Name:**

   + Specify the **Type Name:**

   + Specify the **Method Name:**

     Then choose **Next**

1. In the **Advanced Function Details** window, do the following: 

   + Specify the **Role Name:**, which is the IAM role required for your Lambda function's execution\. If you have not yet created an execution role, do the following:

     1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.

     1. Follow the steps in [Creating a Role to Delegate Permissions to an AWS Service](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-service.html) in the *IAM User Guide* to create an IAM role \(execution role\)\. As you follow the steps to create a role, note the following:

        + In **Role Name**, use a name that is unique within your AWS account\. 

        + In **Select Role Type**, choose **AWS Service Roles**, and then choose a service role that grants that service permissions to assume the role\.

        + In **Attach Policy**, choose a permissions policy that is suitable to execute your Lambda function\.

   + \(Optional\) In **Environment::** specify any environment variables you wish to use\. For more information, see [Environment Variables](env_variables.md)\.

   + \(Optional\)Specify the **Memory \(MB\):** or **Timeout \(Secs\):** configurations\.

   + \(Optional\)Specify any **VPC:** configurations if your Lambda function needs to access resources running inside a private VPC\. For more information, see [Configuring a Lambda Function to Access Resources in an Amazon VPC](vpc.md)\.

   + Choose **Next** and then choose **Upload** to deploy your application\.