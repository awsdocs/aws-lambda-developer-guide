# AWS Lambda Deployment Package in Node\.js<a name="nodejs-create-deployment-pkg"></a>

A deployment package is a ZIP archive that contains your function code and dependencies\. You need to create a deployment package if you use the Lambda API to manage functions, or if you need to include libraries and dependencies other than the AWS SDK\. You can upload the package directly to Lambda, or you can use an Amazon S3 bucket, and then upload it to Lambda\. If the deployment package is larger than 50 MB, you must use Amazon S3\.

If you use the Lambda [console editor](code-editor.md) to author your function, the console manages the deployment package\. You can use this method as long as you don't need to add any libraries\. You can also use it to update a function that already has libraries in the deployment package, as long as the total size doesn't exceed 3 MB\.

**Note**  
To keep your deployment package size low, package your function's dependencies in layers\. Layers let you manage your dependencies independently, can be used by multiple functions, and can be shared with other accounts\. For details, see [AWS Lambda Layers](configuration-layers.md)\.

 Files in your deployment package must have an appropriate file mode to run on Lambda\. For more information, see [Permissions Policies on Lambda Deployment Packages](deployment-package-v2.md#lambda-zip-package-permission-policies)\.

**Topics**
+ [Updating a Function with No Dependencies](#nodejs-package-codeonly)
+ [Updating a Function with Additional Dependencies](#nodejs-package-dependencies)

## Updating a Function with No Dependencies<a name="nodejs-package-codeonly"></a>

To create or update a function by using the Lambda API, create an archive that contains your function code, and upload it using the AWS CLI\.

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
       "Runtime": "nodejs10.x",
       "Role": "arn:aws:iam::123456789012:role/lambda-role",
       "Handler": "index.handler",
       "CodeSize": 300,
       "Description": "",
       "Timeout": 3,
       "MemorySize": 128,
       "LastModified": "2018-11-23T21:00:10.248+0000",
       "CodeSha256": "Qf0hMc1I2di6YFMi9aXm3JtGTmcDbjniEuiYonYptAk=",
       "Version": "$LATEST",
       "TracingConfig": {
           "Mode": "Active"
       },
       "RevisionId": "983ed1e3-ca8e-434b-8dc1-7d72ebadd83d"
   }
   ```

## Updating a Function with Additional Dependencies<a name="nodejs-package-dependencies"></a>

If your function depends on libraries other than the SDK for JavaScript, install them to a local directory with [NPM](https://www.npmjs.com/), and include them in your deployment package\. You can also include the SDK for JavaScript if you need a newer version than the one [included on the runtime](programming-model.md), or to ensure that the version doesn't change in the future\.

**To update a Node\.js function with dependencies**

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
       "FunctionArn": "arn:aws:lambda:us-west-2:123456789012:function:my-function",
       "Runtime": "nodejs10.x",
       "Role": "arn:aws:iam::123456789012:role/lambda-role",
       "Handler": "index.handler",
       "CodeSize": 300,
       "Description": "My function",
       "Timeout": 3,
       "MemorySize": 128,
       "LastModified": "2018-11-23T21:00:10.248+0000",
       "CodeSha256": "Qf0hMc1I2di6YFMi9aXm3JtGTmcDbjniEuiYonYptAk=",
       "Version": "$LATEST",
       "TracingConfig": {
           "Mode": "Active"
       },
       "RevisionId": "983ed1e3-ca8e-434b-8dc1-7d72ebadd83d"
   }
   ```

In addition to code and libraries, your deployment package can also contain executable files and other resources\. For more information, see the following:
+ [Running Executables in AWS Lambda](https://aws.amazon.com/blogs/compute/running-executables-in-aws-lambda/)
+ [Using Packages and Native nodejs Modules in AWS Lambda ](https://aws.amazon.com/blogs/compute/nodejs-packages-in-lambda/)