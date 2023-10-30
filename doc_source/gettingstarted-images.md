# Deploying Lambda functions as container images<a name="gettingstarted-images"></a>

When you create a Lambda function, you package your function code into a deployment package\. Lambda supports two types of deployment packages: [container images](gettingstarted-package.md#gettingstarted-package-images) and [\.zip file archives](gettingstarted-package.md#gettingstarted-package-zip)\. The workflow to create a function is different depending on the deployment package type\. To configure a function defined as a \.zip file archive, see [Deploying Lambda functions as \.zip file archives](configuration-function-zip.md)\.

You can use the Lambda console and the Lambda API to create a function defined as a container image, update and test the image code, and configure other function settings\.

**Note**  
You cannot convert an existing container image function to use a \.zip file archive\. You must create a new function\.

When you select an image using an image tag, Lambda translates the tag to the underlying image digest\. To retrieve the digest for your image, use the [GetFunctionConfiguration](API_GetFunctionConfiguration.md) API operation\. To update the function to a newer image version, you must use the Lambda console to [update the function code](#configuration-images-update), or use the [UpdateFunctionCode](API_UpdateFunctionCode.md) API operation\. Configuration operations such as [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md) do not update the function's container image\.

**Topics**
- [Deploying Lambda functions as container images](#deploying-lambda-functions-as-container-images)
  - [Prerequisites](#prerequisites)
  - [Permissions](#permissions)
    - [Amazon ECR permissions](#amazon-ecr-permissions)
      - [Amazon ECR cross-account permissions](#amazon-ecr-cross-account-permissions)
  - [Creating the function](#creating-the-function)
  - [Testing the function](#testing-the-function)
  - [Overriding container settings](#overriding-container-settings)
  - [Updating function code](#updating-function-code)
    - [Function version $LATEST](#function-version-latest)
  - [Using the Lambda API](#using-the-lambda-api)
  - [AWS CloudFormation](#aws-cloudformation)

## Prerequisites<a name="gettingstarted-images-prereq"></a>

To complete the following steps, you need a command line terminal or shell to run commands\. Commands and the expected output are listed in separate blocks:

```
aws --version
```

You should see the following output:

```
aws-cli/2.0.57 Python/3.7.4 Darwin/19.6.0 exe/x86_64
```

For long commands, an escape character \(`\`\) is used to split a command over multiple lines\.

On Linux and macOS, use your preferred shell and package manager\.

**Note**  
On Windows, some Bash CLI commands that you commonly use with Lambda \(such as `zip`\) are not supported by the operating system's built\-in terminals\. To get a Windows\-integrated version of Ubuntu and Bash, [install the Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10)\. 

Before you create the function, you must [create a container image and upload it to Amazon ECR](images-create.md)\.

## Permissions<a name="gettingstarted-images-permissions"></a>

Make sure that the permissions for the IAM user or role that creates the function contain the AWS managed policies `GetRepositoryPolicy` and `SetRepositoryPolicy`\.

For example, use the IAM console to create a role with the following policy:

```
{
"Version": "2012-10-17",
"Statement": [
  {
  "Sid": "VisualEditor0",
  "Effect": "Allow",
  "Action": ["ecr:SetRepositoryPolicy","ecr:GetRepositoryPolicy"],
  "Resource": "arn:aws:ecr:<region>:<account>:repository/<repo-name>/"
  }
]
}
```

### Amazon ECR permissions<a name="configuration-images-permissions"></a>

For a function in the same account as the container image in Amazon ECR, you can add `ecr:BatchGetImage` and `ecr:GetDownloadUrlForLayer` permissions to your Amazon ECR repository\. The following example shows the minimum policy:

```
{
        "Sid": "LambdaECRImageRetrievalPolicy",
        "Effect": "Allow",
        "Principal": {
          "Service": "lambda.amazonaws.com"
        },
        "Action": [
          "ecr:BatchGetImage",
          "ecr:GetDownloadUrlForLayer"
        ]
    }
```

For more information about Amazon ECR repository permissions, see [Repository policies](https://docs.aws.amazon.com/AmazonECR/latest/userguide/repository-policies.html) in the *Amazon Elastic Container Registry User Guide*\.

If the Amazon ECR repository does not include these permissions, Lambda adds `ecr:BatchGetImage` and `ecr:GetDownloadUrlForLayer` to the container image repository permissions\. Lambda can add these permissions only if the Principal calling Lambda has `ecr:getRepositoryPolicy` and `ecr:setRepositoryPolicy` permissions\. 

To view or edit your Amazon ECR repository permissions, follow the directions in [Setting a repository policy statement](https://docs.aws.amazon.com/AmazonECR/latest/userguide/set-repository-policy.html) in the *Amazon Elastic Container Registry User Guide*\.

#### Amazon ECR cross\-account permissions<a name="configuration-images-xaccount-permissions"></a>

A different account in the same region can create a function that uses a container image owned by your account\. In the following example, your Amazon ECR repository permissions policy needs the following statements to grant access to account number 123456789012\.
+ **CrossAccountPermission** – Allows account 123456789012 to create and update Lambda functions that use images from this ECR repository\.
+ **LambdaECRImageCrossAccountRetrievalPolicy** – Lambda will eventually set a function's state to inactive if it is not invoked for an extended period\. This statement is required so that Lambda can retrieve the container image for optimization and caching on behalf of the function owned by 123456789012\. 

**Example Add cross\-account permission to your repository**  

```
{"Version": "2012-10-17",
    "Statement": [
      {
        "Sid": "CrossAccountPermission",
        "Effect": "Allow",
        "Action": [
          "ecr:BatchGetImage",
          "ecr:GetDownloadUrlForLayer"
        ],
        "Principal": {
          "AWS": "arn:aws:iam::<account>:root"
        } 
      },
      {
        "Sid": "LambdaECRImageCrossAccountRetrievalPolicy",
        "Effect": "Allow",
        "Action": [
          "ecr:BatchGetImage",
          "ecr:GetDownloadUrlForLayer"
        ],
        "Principal": {
            "Service": "lambda.amazonaws.com"
        },
        "Condition": {
          "StringLike": {
            "aws:sourceARN":
              "arn:aws:lambda:<region>:<account>:function:*"
          } 
        }
      }
    ]
    }
```

To give access to multiple accounts, you add the account IDs to the Principal list in the `CrossAccountPermission` policy and to the Condition evaluation list in the `LambdaECRImageCrossAccountRetrievalPolicy`\.

If you are working with multiple accounts in an AWS Organization, we recommend that you enumerate each account ID in the ECR permissions policy\. This approach aligns with the AWS security best practice of setting narrow permissions in IAM policies\.

## Creating the function<a name="configuration-images-create"></a>

To create a function defined as a container image, you must first [create the image](images-create.md) and then store the image in the Amazon ECR repository\.

**To create the function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose **Create function**\.

1. Choose the **Container image** option\.

1. Under **Basic information**, do the following:

   1. For **Function name**, enter the function name\.

   1. For **Container image URI**, provide a container image that is compatible with the instruction set architecture that you want for your function code\. 

      You can enter the Amazon ECR image URI or browse for the Amazon ECR image\.
      + Enter the Amazon ECR image URI\.
      + Or, to browse an Amazon ECR repository for the image, choose **Browse images**\. Select the Amazon ECR repository from the dropdown list, and then select the image\.

1. \(Optional\) To override configuration settings that are included in the Dockerfile, expand **Container image overrides**\. You can override any of the following settings:
   + For **Entrypoint**, enter the full path of the runtime executable\. The following example shows an entrypoint for a Node\.js function:

     ```
     "/usr/bin/npx", "aws-lambda-ric"
     ```
   + For **Command**, enter additional parameters to pass in to the image with **Entrypoint**\. The following example shows a command for a Node\.js function:

     ```
     "app.handler"
     ```
   + For **Working directory**, enter the full path of the working directory for the function\. The following example shows the working directory for an AWS base image for Lambda:

     ```
     "/var/task"
     ```
**Note**  
For the override settings, make sure that you enclose each string in quotation marks \(" "\)\.

1. \(Optional\) For **Architecture**, choose the instruction set architecture for the function\. The default architecture is x86\_64\. Note: when you build the container image for your function, make sure that it is compatible with this [instruction set architecture](foundation-arch.md)\.

1. \(Optional\) Under **Permissions**, expand **Change default execution role**\. Then, choose to create a new **Execution role**, or to use an existing role\.

1. Choose **Create function**\.

Lambda creates your function and an [execution role](lambda-intro-execution-role.md) that grants the function permission to upload logs\. Lambda assumes the execution role when you invoke your function, and uses the execution role to create credentials for the AWS SDK and to read data from event sources\.

When you deploy code as a container image to a Lambda function, the image undergoes an optimization process for running on Lambda\. This process can take a few seconds, during which the function is in pending state\. When the optimization process completes, the function enters the active state\.

## Testing the function<a name="get-started-invoke-function"></a>

Invoke your Lambda function using the sample event data provided in the console\.

**To invoke a function**

1. After selecting your function, choose the **Test** tab\.

1. In the **Test event** section, choose **New event**\. In **Template**, leave the default **hello\-world** option\. Enter a **Name** for this test and note the following sample event template:

   ```
   {
       "key1": "value1",
       "key2": "value2",
       "key3": "value3"
     }
   ```

1. Choose **Save changes**, and then choose **Test**\. Each user can create up to 10 test events per function\. Those test events are not available to other users\.

   Lambda runs your function on your behalf\. The function handler receives and then processes the sample event\.

1. Upon successful completion, view the results in the console\.
   + The **Execution result** shows the execution status as **succeeded**\. To view the function execution results, expand **Details**\. Note that the **logs** link opens the **Log groups** page in the CloudWatch console\.
   + The **Summary** section shows the key information reported in the **Log output** section \(the *REPORT* line in the execution log\)\.
   + The **Log output** section shows the log that Lambda generates for each invocation\. The function writes these logs to CloudWatch\. The Lambda console shows these logs for your convenience\. Choose **Click here** to add logs to the CloudWatch log group and open the **Log groups** page in the CloudWatch console\.

1. Run the function \(choose **Test**\) a few more times to gather some metrics that you can view in the next step\.

1. Choose the **Monitor** tab\. This page shows graphs for the metrics that Lambda sends to CloudWatch\.  
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/metrics-functions-list.png)

   For more information on these graphs, see [Monitoring functions on the Lambda console](monitoring-functions-access-metrics.md)\.

## Overriding container settings<a name="configuration-images-settings"></a>

You can use the Lambda console or the Lambda API to override the following container image settings:
+ ENTRYPOINT – Specifies the absolute path of the entry point to the application\.
+ CMD – Specifies parameters that you want to pass in with ENTRYPOINT\.
+ WORKDIR – Specifies the absolute path of the working directory\.
+ ENV – Specifies an environment variable for the Lambda function\.

Any values that you provide in the Lambda console or the Lambda API override the values [in the Dockerfile](images-create.md#images-parms)\.

**To override the configuration values in the container image**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the function to update\.

1. Under **Image configuration**, choose **Edit**\.

1. Enter new values for any of the override settings, and then choose **Save**\.

1. \(Optional\) To add or override environment variables, under **Environment variables**, choose **Edit**\.

   For more information, see [Using AWS Lambda environment variables](configuration-envvars.md)\.

## Updating function code<a name="configuration-images-update"></a>

After you deploy a container image to a function, the image is read\-only\. To update the function code, you must first deploy a new image version\. [Create a new image version](images-create.md), and then store the image in the Amazon ECR repository\.

**To configure the function to use an updated container image**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the function to update\.

1. Under **Image**, choose **Deploy new image**\.

1. Choose **Browse images**\.

1. In the **Select container image** dialog box, select the Amazon ECR repository from the dropdown list, and then select the new image version\.

1. Choose **Save**\.

### Function version $LATEST<a name="configuration-images-latest"></a>

When you publish a function version, the code and most of the configuration settings are locked to maintain a consistent experience for users of that version\. You can change the code and many configuration settings only on the unpublished version of the function\. By default, the console displays configuration information for the unpublished version of the function\. To view the versions of a function, choose **Qualifiers**\. The unpublished version is named **$LATEST**\. 

Note that Amazon Elastic Container Registry \(Amazon ECR\) also uses a *latest* tag to denote the latest version of the container image\. Be careful not to confuse this tag with the **$LATEST** function version\.

For more information about managing versions, see [Lambda function versions](configuration-versions.md)\.

## Using the Lambda API<a name="configuration-images-api"></a>

To manage functions defined as container images, use the following API operations: 
+ [CreateFunction](API_CreateFunction.md)
+ [UpdateFunctionCode](API_UpdateFunctionCode.md)
+ [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)

To create a function defined as container image, use the `create-function` command\. Set the `package-type` to `Image` and specify your container image URI using the `code` parameter\.

When you create the function, you can specify the instruction set architecture\. The default architecture is `x86-64`\. Make sure that the code in your container image is compatible with the architecture\.

 You can create the function from the same account as the container registry or from a different account in the same region as the container registry in Amazon ECR\. For cross\-account access, adjust the [Amazon ECR permissions](#configuration-images-xaccount-permissions) for the image\.

```
aws lambda create-function --region <region> --function-name my-function \
    --package-type Image  \
    --code ImageUri=<ECR Image URI>   \
    --role arn:aws:iam::<account>:role/lambda-ex
```

To update the function code, use the `update-function-code` command\. Specify the container image location using the `image-uri` parameter\.

**Note**  
You cannot change the `package-type` of a function\.

```
aws lambda update-function-code --region <region> --function-name my-function \
    --image-uri <ECR Image URI>   \
```

To update the function parameters, use the `update-function-configuration` operation\. Specify `EntryPoint` and `Command` as arrays of strings, and `WorkingDirectory` as a string\.

```
aws lambda update-function-configuration  --function-name my-function \
--image-config '{"EntryPoint": ["/usr/bin/npx", "aws-lambda-ric"],  \
                 "Command":   ["app.handler"] ,          \
                  "WorkingDirectory": "/var/task"}'
```

## AWS CloudFormation<a name="configuration-images-cloudformation"></a>

You can use AWS CloudFormation to create Lambda functions defined as container images\. In your AWS CloudFormation template, the `AWS::Lambda::Function` resource specifies the Lambda function\. For descriptions of the properties in the `AWS::Lambda::Function` resource, see [AWS::Lambda::Function](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-lambda-function.html) in the *AWS CloudFormation User Guide*\.

In the `AWS::Lambda::Function` resource, set the following properties to create a function defined as a container image:
+ AWS::Lambda::Function
  + PackageType – Set to `Image`\.
  + Code – Enter your container image URI in the `ImageUri` field\.
  + ImageConfig – \(Optional\) Override the container image configuration properties\.

The `ImageConfig` property in `AWS::Lambda::Function` contains the following fields:
+ Command – Specifies parameters that you want to pass in with `EntryPoint`\.
+ EntryPoint – Specifies the entry point to the application\.
+ WorkingDirectory – Specifies the working directory\.

**Note**  
If you declare an `ImageConfig` property in your AWS CloudFormation template, you must provide values for all three of the `ImageConfig` properties\.

For more information, see [ImageConfig](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-lambda-function.html#cfn-lambda-function-imageconfig) in the *AWS CloudFormation User Guide*\.