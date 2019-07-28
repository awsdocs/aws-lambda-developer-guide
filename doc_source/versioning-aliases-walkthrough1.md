# Tutorial: Using AWS Lambda Aliases<a name="versioning-aliases-walkthrough1"></a>

This AWS CLI\-based tutorial creates Lambda function versions and aliases that point to it as described in the [Example: Using Aliases to Manage Lambda Function Versions](aliases-intro.md#aliases-intro-example)\.

This example uses the us\-west\-2 \(US West Oregon\) region to create the Lambda function and aliases\.

1. Create a deployment package that you can upload to create your Lambda function: 

   1. Open a text editor, and then copy the following code\. 

      ```
      console.log('Loading function');
      
      exports.handler = function(event, context, callback) {
          console.log('value1 =', event.key1);
          console.log('value2 =', event.key2);
          console.log('value3 =', event.key3);
          callback(null, "message");  
         
      };
      ```

   1. Save the file as `helloworld.js`\.

   1. Zip the `helloworld.js` file as `helloworld.zip`\. 

1. Create an AWS Identity and Access Management \(IAM\) role \(execution role\) that you can specify at the time you create your Lambda function: 

   1. Sign in to the AWS Management Console and open the IAM console at [https://console\.aws\.amazon\.com/iam/](https://console.aws.amazon.com/iam/)\.

   1. Follow the steps in [IAM Roles](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html) in the *IAM User Guide* to create an IAM role \(execution role\)\. As you follow the steps to create a role, note the following: 
      + For **Select Role Type**, choose **AWS Service Roles**, and then choose **AWS Lambda**\.
      + For **Attach Policy**, choose the policy named **AWSLambdaBasicExecutionRole**\. 

   1. Write down the Amazon Resource Name \(ARN\) of the IAM role\. You need this value when you create your Lambda function in the next step\.

1. Create a Lambda function \(`helloworld`\)\. 

   ```
   aws lambda create-function --function-name helloworld --runtime nodejs10.x \
   --zip-file fileb://helloworld.zip --handler helloworld.handler \
   --role arn:aws:iam::123456789012:role/lambda-role
   ```

   The response returns the configuration information showing `$LATEST` as the function version as shown in the following example response\.

   ```
   {
       "CodeSha256": "OjRFuuHKizEE8tHFIMsI+iHR6BPAfJ5S0rW31Mh6jKg=",
       "FunctionName": "helloworld",
       "CodeSize": 287,
       "MemorySize": 128,
       "FunctionArn": "arn:aws:lambda:us-west-2:account-id:function:helloworld",
       "Version": "$LATEST",
       "Role": "arn:aws:iam::account-id:role/lambda_basic_execution",
       "Timeout": 3,
       "LastModified": "2015-09-30T18:39:53.873+0000",
       "Handler": "helloworld.handler",
       "Runtime": "nodejs10.x",
       "Description": ""
   }
   ```

1. Create an alias \(`DEV`\) that points to the `$LATEST` version of the `helloworld` Lambda function\.

   ```
   aws lambda create-alias --function-name helloworld --name DEV \
   --description "sample alias" --function-version "\$LATEST"
   ```

   The response returns the alias information, including the function version it points to and the alias ARN\. The ARN is the same as the function ARN with an alias name suffix\. The following is an example response\.

   ```
   {
       "AliasArn": "arn:aws:lambda:us-west-2:account-id:function:helloworld:DEV",
       "FunctionVersion": "$LATEST",
       "Name": "DEV",
       "Description": "sample alias"
   }
   ```

1. Publish a version of the `helloworld` Lambda function\.

   ```
   aws lambda publish-version --function-name helloworld
   ```

   The response returns configuration information of the function version, including the version number, and the function ARN with the version suffix\. The following is an example response\.

   ```
   {
       "CodeSha256": "OjRFuuHKizEE8tHFIMsI+iHR6BPAfJ5S0rW31Mh6jKg=",
       "FunctionName": "helloworld",
       "CodeSize": 287,
       "MemorySize": 128,
       "FunctionArn": "arn:aws:lambda:us-west-2:account-id:function:helloworld:1",
       "Version": "1",
       "Role": "arn:aws:iam::account-id:role/lambda_basic_execution",
       "Timeout": 3,
       "LastModified": "2015-10-03T00:48:00.435+0000",
       "Handler": "helloworld.handler",
       "Runtime": "nodejs10.x",
       "Description": ""
   }
   ```

1. Create an alias named `BETA` for the `helloworld` Lambda function version 1\.

   ```
   aws lambda create-alias --function-name helloworld \
   --description "sample alias" --function-version 1 --name BETA
   ```

   Now you have two aliases for the `helloworld` function\. The `DEV` alias points to the `$LATEST` function version, and the `BETA` alias points to version 1 of the Lambda function\.

1. Suppose that you want to put the version 1 of the `helloworld` function in production\. Create another alias \(`PROD`\) that points to version 1\.

   ```
   aws lambda create-alias --function-name helloworld \
   --description "sample alias" --function-version 1 --name PROD
   ```

   At this time, you have both the `BETA` and `PROD` aliases pointing to version 1 of the Lambda function\.

1. You can now publish a newer version \(for example, version 2\), but first you need to update your code and upload a modified deployment package\. If the `$LATEST` version is not changed, you cannot publish more than one version of it\. Assuming you updated the deployment package, uploaded it, and published version 2, you can now change the `BETA` alias to point to version 2 of the Lambda function\. 

   ```
   aws lambda update-alias --function-name helloworld \
   --function-version 2 --name BETA
   ```

   Now you have three aliases pointing to a different version of the Lambda function \(`DEV` alias points to the `$LATEST` version, `BETA` alias points to version 2, and the `PROD` alias points to version 1 of the Lambda function\.