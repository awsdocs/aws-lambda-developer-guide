# Configuring functions defined as container images<a name="configuration-images"></a>

You can use the Lambda console and the Lambda API to create a function defined as a container image, update and test the image code, and configure other function settings\.

**Note**  
You cannot convert an existing \.zip file archive function to use a container image\. You must create a new function\.

When you select an image using an image tag, Lambda translates the tag to the underlying image digest\. To retrieve the digest for your image, use the [GetFunctionConfiguration](API_GetFunctionConfiguration.md) API operation\. To update the function to a newer image version, you must use the Lambda console to [update the function code](#configuration-images-update), or use the [UpdateFunctionCode](API_UpdateFunctionCode.md) API operation\. Configuration operations such as [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md) do not update the function's container image\.

**Note**  
In Amazon ECR, if you reassign the image tag to another image, Lambda does not update the image version\.

**Topics**
+ [Function version $LATEST](#configuration-images-latest)
+ [Container image deployment](#configuration-images-optimization)
+ [Update the user permissions](#configuration-images-permissions)
+ [Override the container settings](#configuration-images-settings)
+ [Creating a function \(console\)](#configuration-images-create)
+ [Updating the function code \(console\)](#configuration-images-update)
+ [Overriding the image parameters \(console\)](#configuration-images-parms)
+ [Using the Lambda API](#configuration-images-api)
+ [AWS CloudFormation](#configuration-images-cloudformation)

## Function version $LATEST<a name="configuration-images-latest"></a>

When you publish a function version, the code and most of the configuration settings are locked to maintain a consistent experience for users of that version\. You can change the code and many configuration settings only on the unpublished version of the function\. By default, the console displays configuration information for the unpublished version of the function\. To view the versions of a function, choose **Qualifiers**\. The unpublished version is named **$LATEST**\. 

Note that Amazon Elastic Container Registry \(Amazon ECR\) also uses a *latest* tag to denote the latest version of the container image\. Be careful not to confuse this tag with the **$LATEST** function version\.

For more information about managing versions, see [Lambda function versions](configuration-versions.md)\.

## Container image deployment<a name="configuration-images-optimization"></a>

When you deploy code as a container image to a Lambda function, the image undergoes an optimization process for running on Lambda\. This process can take a few seconds, during which the function is in pending state\. When the optimization process completes, the function enters the active state\.

## Update the user permissions<a name="configuration-images-permissions"></a>

Make sure that the permissions for the AWS Identity and Access Management \(IAM\) user or role that creates the function contain the AWS managed policies `GetRepositoryPolicy`, `SetRepositoryPolicy`, and `InitiateLayerUpload`\.

For example, use the IAM console to create a role with the following policy:

```
{
    "Version": "2012-10-17",
    "Statement":  [
        {
            "Sid": "VisualEditor0",
            "Effect": "Allow",
            "Action": [
                "ecr:SetRepositoryPolicy",
                "ecr:GetRepositoryPolicy", 
                "ecr:InitiateLayerUpload"
            ],
            "Resource": "arn:aws:ecr:<region>:<account>:repository/<repo name>/"
        }
    ]
}
```

## Override the container settings<a name="configuration-images-settings"></a>

You can use the Lambda console or the Lambda API to override the following container image settings:
+ ENTRYPOINT – Specifies the absolute path of the entry point to the application\.
+ CMD – Specifies parameters that you want to pass in with ENTRYPOINT\.
+ WORKDIR – Specifies the absolute path of the working directory\.
+ ENV – Specifies an environment variable for the Lambda function\.

Any values that you provide in the Lambda console or the Lambda API override the values [in the Dockerfile](images-create.md#images-parms)\.

## Creating a function \(console\)<a name="configuration-images-create"></a>

To create a function defined as a container image, you must first [create the image](images-create.md) and then store the image in the Amazon ECR repository\.

**To create the function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) on the Lambda console\.

1. Choose **Create function**\.

1. Choose the **Container image** option\.

1. Under **Basic information**, do the following:

   1. For **Function name**, enter the function name\.

   1. For **Container image URI**, enter the Amazon ECR image URI\.
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

1. \(Optional\) Under **Permissions**, expand **Change default execution role**\. Then, choose to create a new **Execution role**, or to use an existing role\.

1. Choose **Create function**\.

## Updating the function code \(console\)<a name="configuration-images-update"></a>

After you deploy a container image to a function, the image is read\-only\. To update the function code, you must first deploy a new image version\. [Create a new image version](images-create.md), and then store the image in the Amazon ECR repository\.

**To configure the function to use an updated container image**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) on the Lambda console\.

1. Choose the function to update\.

1. Under **Image**, choose **Deploy new image**\.

1. Choose **Browse images**\.

1. In the **Select container image** dialog box, select the Amazon ECR repository from the dropdown list, and then select the new image version\.

1. Choose **Save**\.

## Overriding the image parameters \(console\)<a name="configuration-images-parms"></a>

You can use the Lambda console to override the configuration values in the container image\.

**To override the configuration values in the container image**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) on the Lambda console\.

1. Choose the function to update\.

1. Under **Image configuration**, choose **Edit**\.

1. Enter new values for any of the override settings, and then choose **Save**\.

1. \(Optional\) To add or override environment variables, under **Environment variables**, choose **Edit**\.

   For more information, see [Using AWS Lambda environment variables](configuration-envvars.md)\.

## Using the Lambda API<a name="configuration-images-api"></a>

To manage functions defined as container images, use the following API operations: 
+ [CreateFunction](API_CreateFunction.md)
+ [UpdateFunctionCode](API_UpdateFunctionCode.md)
+ [UpdateFunctionConfiguration](API_UpdateFunctionConfiguration.md)

To create a function defined as container image, use the `create-function` command\. Set the `package-type` to `Image` and specify your container image URI using the `code` parameter\. Note that you must create the function from the same account as the container registry in Amazon EFS\.

```
aws lambda create-function --region sa-east-1 --function-name my-function \
    --package-type Image  \
    --code ImageUri=<ECR Image URI>   \
    --role arn:aws:iam::123456789012:role/lambda-ex
```

To update the function code, use the `update-function-code` command\. Specify the container image location using the `image-uri` parameter\.

**Note**  
You cannot change the `package-type` of a function\.

```
aws lambda update-function-code --region sa-east-1 --function-name my-function \
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