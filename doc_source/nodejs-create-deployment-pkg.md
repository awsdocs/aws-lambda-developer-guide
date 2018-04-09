# Creating a Deployment Package \(Node\.js\)<a name="nodejs-create-deployment-pkg"></a>

To create a Lambda function you first create a Lambda function deployment package, a \.zip file consisting of your code and any dependencies\. As noted previously, you need to set the appropriate security permissions for the zip package\. For more information, see [Authentication and Access Control for AWS Lambda](lambda-auth-and-access-control.md) policies\.

You can create a deployment package yourself or write your code directly in the Lambda console, in which case the console creates the deployment package for you and uploads it, creating your Lambda function\. Note the following to determine if you can use the console to create your Lambda function:
+ **Simple scenario** – If your custom code requires only the AWS SDK library, then you can use the inline editor in the AWS Lambda console\. Using the console, you can edit and upload your code to AWS Lambda\. The console will zip up your code with the relevant configuration information into a deployment package that the Lambda service can run\. 

  You can also test your code in the console by manually invoking it using sample event data\. 
**Note**  
The Lambda service has preinstalled the AWS SDK for Node\.js\.
+ **Advanced scenario** – If you are writing code that uses other resources, such as a graphics library for image processing, or you want to use the AWS CLI instead of the console, you need to first create the Lambda function deployment package, and then use the console or the CLI to upload the package\.

**Note**  
After you create a deployment package, you may either upload it directly or upload the \.zip file first to an Amazon S3 bucket in the same AWS region where you want to create the Lambda function, and then specify the bucket name and object key name when you create the Lambda function using the console or the AWS CLI\.

The following is an example procedure to create a deployment package \(outside the console\)\. Suppose you want to create a deployment package that includes a `filename.js` code file and your code uses the `async` library\. 

1. Open a text editor, and write your code\. Save the file \(for example, `filename.js`\)\.

   You will use the file name to specify the handler at the time of creating the Lambda function\.

1. In the same directory, use npm to install the libraries that your code depends on\. For example, if your code uses the `async` library, use the following npm command\.

   ```
   npm install async
   ```

1. Your directory will then have the following structure:

   ```
   filename.js
   node_modules/async
   node_modules/async/lib
   node_modules/async/lib/async.js
   node_modules/async/package.json
   ```

1. Zip the content of the folder, that is your deployment package \(for example, `sample.zip`\)\.

Then, specify the \.zip file name as your deployment package at the time you create your Lambda function\.

If you want to include your own binaries, including native ones, just package them in the Zip file you upload and then reference them \(including the relative path within the Zip file you created\) when you call them from Node\.js or from other processes that you’ve previously started\. Ensure that you include the following at the start of your function code: `process.env[‘PATH’] = process.env[‘PATH’] + ‘:’ + process.env[‘LAMBDA_TASK_ROOT’]`

For more information on including native binaries in your Lambda function package, see [Running Executables in AWS Lambda](https://aws.amazon.com/blogs/compute/running-executables-in-aws-lambda/)\. 