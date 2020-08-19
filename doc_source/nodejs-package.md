# AWS Lambda deployment package in Node\.js<a name="nodejs-package"></a>

A deployment package is a ZIP archive that contains your function code and dependencies\. You need to create a deployment package if you use the Lambda API to manage functions, or if you need to include libraries and dependencies other than the AWS SDK\. You can upload the package directly to Lambda, or you can use an Amazon S3 bucket, and then upload it to Lambda\. If the deployment package is larger than 50 MB, you must use Amazon S3\.

If you use the Lambda [console editor](code-editor.md) to author your function, the console manages the deployment package\. You can use this method as long as you don't need to add any libraries\. You can also use it to update a function that already has libraries in the deployment package, as long as the total size doesn't exceed 3 MB\.

**Note**  
To keep your deployment package size small, package your function's dependencies in layers\. Layers let you manage your dependencies independently, can be used by multiple functions, and can be shared with other accounts\. For details, see [AWS Lambda layers](configuration-layers.md)\.

**Topics**
+ [Updating a function with no dependencies](#nodejs-package-codeonly)
+ [Updating a function with additional dependencies](#nodejs-package-dependencies)

## Updating a function with no dependencies<a name="nodejs-package-codeonly"></a>

To update a function by using the Lambda API, use the [UpdateFunctionCode](API_UpdateFunctionCode.md) operation\. Create an archive that contains your function code, and upload it using the AWS CLI\.

**To update a Node\.js function with no dependencies**

1. Create a ZIP archive\.

   ```
   ~/my-function$ zip function.zip index.js
   ```

1. Use the `update-function-code` command to upload the package\.

   ```
   ~/my-function$ aws lambda update-function-code --function-name my-function --zip-file fileb://function.zip
   {
       "FunctionName": "my-function",
       "FunctionArn": "arn:aws:lambda:us-west-2:123456789012:function:my-function",
       "Runtime": "nodejs12.x",
       "Role": "arn:aws:iam::123456789012:role/lambda-role",
       "Handler": "index.handler",
       "CodeSha256": "Qf0hMc1I2di6YFMi9aXm3JtGTmcDbjniEuiYonYptAk=",
       "Version": "$LATEST",
       "TracingConfig": {
           "Mode": "Active"
       },
       "RevisionId": "983ed1e3-ca8e-434b-8dc1-7d72ebadd83d",
       ...
   }
   ```

## Updating a function with additional dependencies<a name="nodejs-package-dependencies"></a>

If your function depends on libraries other than the SDK for JavaScript, use [npm](https://www.npmjs.com/) to include them in your deployment package\. Ensure that the Node\.js version in your local environment matches the Node\.js version of your function\. If any of the libraries use native code, use an [Amazon Linux environment ](https://aws.amazon.com/blogs/compute/nodejs-packages-in-lambda/) to create the deployment package\.

You can add the SDK for JavaScript to the deployment package if you need a newer version than the one [included on the runtime](lambda-nodejs.md), or to ensure that the version doesn't change in the future\. 

**To update a Node\.js function with dependencies**

1. Open a command line terminal or shell\. Ensure that the Node\.js version in your local environment matches the Node\.js version of your function\.

1. Install libraries in the node\_modules directory with the `npm install` command\.

   ```
   ~/my-function$ npm install aws-xray-sdk
   ```

   This creates a folder structure that's similar to the following\.

   ```
   ~/my-function
   ├── index.js
   └── node_modules
       ├── async
       ├── async-listener
       ├── atomic-batcher
       ├── aws-sdk
       ├── aws-xray-sdk
       ├── aws-xray-sdk-core
   ```

1. Create a ZIP file that contains the contents of your project folder\.

   ```
   ~/my-function$ zip -r function.zip .
   ```

1. Use the `update-function-code` command to upload the package\.

   ```
   ~/my-function$ aws lambda update-function-code --function-name my-function --zip-file fileb://function.zip
   {
       "FunctionName": "my-function",
       "FunctionArn": "arn:aws:lambda:us-east-2:123456789012:function:my-function",
       "Runtime": "nodejs12.x",
       "Role": "arn:aws:iam::123456789012:role/lambda-role",
       "Handler": "index.handler",
       "CodeSha256": "Qf0hMc1I2di6YFMi9aXm3JtGTmcDbjniEuiYonYptAk=",
       "Version": "$LATEST",
       "TracingConfig": {
           "Mode": "Active"
       },
       "RevisionId": "983ed1e3-ca8e-434b-8dc1-7d72ebadd83d",
       ...
   }
   ```

In addition to code and libraries, your deployment package can also contain executable files and other resources\. For more information, see the following:
+ [Running executables in AWS Lambda](https://aws.amazon.com/blogs/compute/running-executables-in-aws-lambda/)
+ [Using packages and native nodejs modules in AWS Lambda ](https://aws.amazon.com/blogs/compute/nodejs-packages-in-lambda/)