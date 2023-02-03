# Tutorial: Creating a Lambda function with a function URL<a name="urls-tutorial"></a>

In this tutorial, you create a Lambda function defined as a \.zip file archive with a **public** function URL endpoint that returns the product of two numbers\. For more information about configuring function URLs, see [Creating and managing function URLs](urls-configuration.md)\.

## Prerequisites<a name="lambda-url-prepare"></a>

This tutorial assumes that you have some knowledge of basic Lambda operations and the Lambda console\. If you haven't already, follow the instructions in [Create a Lambda function with the console](getting-started.md#getting-started-create-function) to create your first Lambda function\.

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

## Create an execution role<a name="lambda-url-create-iam-role"></a>

Create the [execution role](lambda-intro-execution-role.md) that gives your Lambda function permission to access AWS resources\.

**To create an execution role**

1. Open the [Roles page](https://console.aws.amazon.com/iam/home#/roles) of the AWS Identity and Access Management \(IAM\) console\.

1. Choose **Create role**\.

1. Create a role with the following properties\.
   + **Trusted entity** – **AWS Lambda**\.
   + **Permissions** – **AWSLambdaBasicExecutionRole**\.
   + **Role name** – **lambda\-url\-role**\.

The **AWSLambdaBasicExecutionRole** policy has the permissions that the function needs to write logs to Amazon CloudWatch Logs\.

## Create a Lambda function with a function URL \(\.zip file archive\)<a name="lambda-url-tutorial-create-function"></a>

Create a Lambda function with a function URL endpoint using a \.zip file archive\.

**To create the function**

1. Copy the following code example into a file named `index.js`\.  
**Example index\.js**  

   ```
   exports.handler = async (event) => {
       let body = JSON.parse(event.body);
       const product = body.num1 * body.num2;
       const response = {
           statusCode: 200,
           body: "The product of " + body.num1 + " and " + body.num2 + " is " + product,
       };
       return response;
   };
   ```

1. Create a deployment package\.

   ```
   zip function.zip index.js
   ```

1. Create a Lambda function with the `create-function` command\.

   ```
   aws lambda create-function \
       --function-name my-url-function \
       --runtime nodejs14.x \
       --zip-file fileb://function.zip \
       --handler index.handler \
       --role arn:aws:iam::123456789012:role/lambda-url-role
   ```

1. Add a resource\-based policy to your function granting permissions to allow public access to your function URL\.

   ```
   aws lambda add-permission
       --function-name my-url-function \
       --action lambda:InvokeFunctionUrl \
       --principal "*" \
       --function-url-auth-type "NONE" \
       --statement-id url
   ```

1. Create a URL endpoint for the function with the `create-function-url-config` command\.

   ```
   aws lambda create-function-url-config \
       --function-name my-url-function \
       --auth-type NONE
   ```

## Test the function URL endpoint<a name="lambda-url-tutorial-test"></a>

Invoke your Lambda function by calling your function URL endpoint using an HTTP client such as curl or Postman\.

```
curl -X POST \
    'https://abcdefg.lambda-url.us-east-1.on.aws/' \
    -H 'Content-Type: application/json' \
    -d '{"num1": "10", "num2": "10"}'
```

You should see the following output:

```
The product of 10 and 10 is 100
```

## Create a Lambda function with a function URL \(CloudFormation\)<a name="lambda-url-tutorial-cfn"></a>

You can also create a Lambda function with a function URL endpoint using the AWS CloudFormation type `AWS::Lambda::Url`\.

```
Resources:
  MyUrlFunction:
    Type: AWS::Lambda::Function
    Properties:
      Handler: index.handler
      Runtime: nodejs14.x
      Role: arn:aws:iam::123456789012:role/lambda-url-role
      Code:
        ZipFile: |
          exports.handler = async (event) => {
              let body = JSON.parse(event.body);
              const product = body.num1 * body.num2;
              const response = {
                  statusCode: 200,
                  body: "The product of " + body.num1 + " and " + body.num2 + " is " + product,
              };
              return response;
          };
      Description: Create a function with a URL.
  MyUrlFunctionPermissions:
    Type: AWS::Lambda::Permission
    Properties:
      FunctionName: !Ref MyUrlFunction
      Action: lambda:InvokeFunctionUrl
      Principal: "*"
      FunctionUrlAuthType: NONE
  MyFunctionUrl:
    Type: AWS::Lambda::Url
    Properties:
      TargetFunctionArn: !Ref MyUrlFunction
      AuthType: NONE
```

## Create a Lambda function with a function URL \(AWS SAM\)<a name="lambda-url-tutorial-sam"></a>

You can also create a Lambda function configured with a function URL using AWS Serverless Application Model \(AWS SAM\)\.

```
ProductFunction:
  Type: AWS::Serverless::Function
  Properties:
    CodeUri: function/.
    Handler: index.handler
    Runtime: nodejs14.x
    AutoPublishAlias: live
    FunctionUrlConfig:
      AuthType: NONE
```

## Clean up your resources<a name="cleanup"></a>

You can now delete the resources that you created for this tutorial, unless you want to retain them\. By deleting AWS resources that you're no longer using, you prevent unnecessary charges to your AWS account\.

**To delete the execution role**

1. Open the [Roles page](https://console.aws.amazon.com/iam/home#/roles) of the IAM console\.

1. Select the execution role that you created\.

1. Choose **Delete role**\.

1. Choose **Yes, delete**\.

**To delete the Lambda function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Select the function that you created\.

1. Choose **Actions**, then choose **Delete**\.

1. Choose **Delete**\.
