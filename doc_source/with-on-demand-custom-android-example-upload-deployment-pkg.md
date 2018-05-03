# Step 2\.3: Create the Lambda Function and Invoke It Manually \(Using Sample Event Data\)<a name="with-on-demand-custom-android-example-upload-deployment-pkg"></a>

In this section, you do the following:
+ Create a Lambda function, by uploading the deployment package\. 
+ Test the Lambda function by invoking it manually\. Instead of creating an event source, you use sample event data\. In the next section, you create an Android mobile app and test the end\-to\-end experience\.

## Step 2\.3\.1: Create a Lambda Function \(Upload the Deployment Package\)<a name="walkthrough-on-demand-custom-android-events-adminuser-create-test-function-upload-zip-test-upload"></a>

In this step, you upload the deployment package using the AWS CLI\.

At the command prompt, run the following Lambda CLI `create-function` command using the *adminuser* `profile`\. 

You need to update the command by providing the \.zip file path and the execution role ARN\. The `--runtime` parameter value can be `nodejs8.10`, `nodejs6.10`, or `java8`, depending on the language you chose to author your code\.

```
$ aws lambda create-function \
--region us-east-1 \
--function-name AndroidBackendLambdaFunction  \
--zip-file fileb://file-path-to-jar-or-zip-deployment-package \
--role execution-role-arn  \
--handler handler-name \
--runtime runtime-value \
--profile adminuser
```

Optionally, you can upload the \.zip file to an Amazon S3 bucket in the same AWS region, and then specify the bucket and object name in the preceding command\. You need to replace the `--zip-file` parameter by the `--code` parameter, as shown following:

```
--code S3Bucket=bucket-name,S3Key=zip-file-object-key
```

**Note**  
You can create the Lambda function using the AWS Lambda console, in which case note the value of the `create-function` AWS CLI command parameters\. You provide the same values in the console UI\.

## Step 2\.3\.2: Test the Lambda Function \(Invoke Manually\)<a name="walkthrough-on-demand-custom-android-events-adminuser-create-test-function-upload-zip-test-manual-invoke"></a>

Invoke the function manually using the sample event data\. We recommend that you invoke the function using the console because the console UI provides a user\-friendly interface for reviewing the execution results, including the execution summary, logs written by your code, and the results returned by the function \(because the console always performs synchronous execution—invokes the Lambda function using the `RequestResponse` invocation type\)\.

**To test the Lambda function \(AWS Management Console\)**

1. Follow the steps in the Getting Started exercise to create and invoke the Lambda function at [Invoke the Lambda Function Manually and Verify Results, Logs, and Metrics](get-started-create-function.md#get-started-invoke-manually)\. After you choose the Lambda function, choose **Configure test event** from the **Actions** menu to specify the following sample event data:

   ```
   {   "firstName": "first-name",   "lastName": "last-name" }
   ```

1. Verify the results in the console\.
   + **Execution result** should be `Succeeded` with the following return value:

     ```
     {
       "greetings": "Hello first-name, last-name."
     }
     ```
   + Review the **Summary** and the **Log output** sections\.

**To test the Lambda function \(AWS CLI\)**

1.  Save the following sample event JSON in a file, `input.txt`\. 

   ```
   {   "firstName": "first-name",   "lastName": "last-name" }
   ```

1.  Execute the following `invoke` command:

   ```
   $ aws lambda  invoke \
   --invocation-type Event \
   --function-name AndroidBackendLambdaFunction \
   --region us-east-1 \
   --payload file://file-path/input.txt \
   --profile adminuser 
   outputfile.txt
   ```
**Note**  
In this tutorial example, the message is saved in the `outputfile.txt` file\. If you request synchronous execution \(`RequestResponse` as the invocation type\), the function returns the string message in the response body\.   
For Node\.js, it could be one of the following \(whatever one you specify in the code\):  
`context.succeed("message")`  
`context.fail("message")`  
`context.done(null, "message)`  
For Java, it is the message in the return statement:  
`return "message"`

## Next Step<a name="with-on-demand-custom-android-example-upload-deployment-pkg-next-step"></a>

 [Step 3: Create an Amazon Cognito Identity Pool ](with-on-demand-custom-android-create-cognito-pool.md) 