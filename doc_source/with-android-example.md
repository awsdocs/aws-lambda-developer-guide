# Tutorial: Using AWS Lambda with the Mobile SDK for Android<a name="with-android-example"></a>

In this tutorial, you create a simple Android mobile application that uses Amazon Cognito to get credentials and invokes a Lambda function\.

The mobile application retrieves AWS credentials from an Amazon Cognito identity pool and uses them to invoke a Lambda function with an event that contains request data\. The function processes the request and returns a response to the front\-end\.

## Prerequisites<a name="with-android-prepare"></a>

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

## Create the execution role<a name="with-android-create-execution-role"></a>

Create the [execution role](lambda-intro-execution-role.md) that gives your function permission to access AWS resources\.

**To create an execution role**

1. Open the [roles page](https://console.aws.amazon.com/iam/home#/roles) in the IAM console\.

1. Choose **Create role**\.

1. Create a role with the following properties\.
   + **Trusted entity** – **AWS Lambda**\.
   + **Permissions** – **AWSLambdaBasicExecutionRole**\.
   + **Role name** – **lambda\-android\-role**\.

The **AWSLambdaBasicExecutionRole** policy has the permissions that the function needs to write logs to CloudWatch Logs\.

## Create the function<a name="with-android-example-create-function"></a>

The following example uses data to generate a string response\.

**Note**  
For sample code in other languages, see [Sample function code](with-android-create-package.md)\.

**Example index\.js**  

```
exports.handler = function(event, context, callback) {
   console.log("Received event: ", event);
   var data = {
       "greetings": "Hello, " + event.firstName + " " + event.lastName + "."
   };
   callback(null, data);
}
```

**To create the function**

1. Copy the sample code into a file named `index.js`\.

1. Create a deployment package\.

   ```
   zip function.zip index.js
   ```

1. Create a Lambda function with the `create-function` command\.

   ```
   aws lambda create-function --function-name AndroidBackendLambdaFunction \
   --zip-file fileb://function.zip --handler index.handler --runtime nodejs12.x \
   --role arn:aws:iam::123456789012:role/lambda-android-role
   ```

## Test the Lambda function<a name="walkthrough-on-demand-custom-android-events-adminuser-create-test-function-upload-zip-test-manual-invoke"></a>

Invoke the function manually using the sample event data\.

**To test the Lambda function \(AWS CLI\)**

1.  Save the following sample event JSON in a file, `input.txt`\. 

   ```
   {   "firstName": "first-name",   "lastName": "last-name" }
   ```

1.  Run the following `invoke` command:

   ```
   aws lambda  invoke --function-name AndroidBackendLambdaFunction \
   --payload file://file-path/input.txt outputfile.txt
   ```

   The cli\-binary\-format option is required if you're using AWS CLI version 2\. To make this the default setting, run `aws configure set cli-binary-format raw-in-base64-out`\. For more information, see [AWS CLI supported global command line options](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-options.html#cli-configure-options-list)\.

## Create an Amazon Cognito identity pool<a name="with-on-demand-custom-android-create-cognito-pool"></a>

In this section, you create an Amazon Cognito identity pool\. The identity pool has two IAM roles\. You update the IAM role for unauthenticated users and grant permissions to run the `AndroidBackendLambdaFunction` Lambda function\. 

For more information about IAM roles, see [IAM roles](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html) in the *IAM User Guide*\. For more information about Amazon Cognito services, see the [Amazon Cognito](https://aws.amazon.com/cognito/) product detail page\. 

**To create an identity pool**

1. Open the [Amazon Cognito console](https://console.aws.amazon.com/cognito)\. 

1. Create a new identity pool called `JavaFunctionAndroidEventHandlerPool`\. Before you follow the procedure to create an identity pool, note the following:
   + The identity pool you are creating must allow access to unauthenticated identities because our example mobile application does not require a user log in\. Therefore, make sure to select the **Enable access to unauthenticated identities** option\.
   + Add the following statement to the permission policy associated with the unauthenticated identities\. 

     ```
     {
             "Effect": "Allow",
             "Action": [
                 "lambda:InvokeFunction"
             ],
             "Resource": [
                "arn:aws:lambda:us-east-1:123456789012:function:AndroidBackendLambdaFunction"
             ]
     }
     ```

     The resulting policy will be as follows:

     ```
     {
        "Version":"2012-10-17",
        "Statement":[
           {
              "Effect":"Allow",
              "Action":[
                 "mobileanalytics:PutEvents",
                 "cognito-sync:*"
              ],
              "Resource":[
                 "*"
              ]
           },
           {
              "Effect":"Allow",
              "Action":[
                 "lambda:invokefunction"
              ],
              "Resource":[
                 "arn:aws:lambda:us-east-1:account-id:function:AndroidBackendLambdaFunction"
              ]
           }
        ]
     }
     ```

   For instructions about how to create an identity pool, log in to the [Amazon Cognito console](https://console.aws.amazon.com/cognito/home) and follow the **New Identity Pool** wizard\.

1. Note the identity pool ID\. You specify this ID in your mobile application you create in the next section\. The app uses this ID when it sends request to Amazon Cognito to request for temporary security credentials\.

## Create an Android application<a name="with-ondemand-android-mobile-create-app"></a>

Create a simple Android mobile application that generates events and invokes Lambda functions by passing the event data as parameters\. 

The following instructions have been verified using Android studio\.

1. Create a new Android project called `AndroidEventGenerator` using the following configuration:
   + Select the **Phone and Tablet** platform\.
   + Choose **Blank Activity**\.

1. In the build\.gradle \(`Module:app`\) file, add the following in the `dependencies` section:

   ```
   compile 'com.amazonaws:aws-android-sdk-core:2.2.+'
   compile 'com.amazonaws:aws-android-sdk-lambda:2.2.+'
   ```

1. Build the project so that the required dependencies are downloaded, as needed\.

1. In the Android application manifest \(`AndroidManifest.xml`\), add the following permissions so that your application can connect to the Internet\. You can add them just before the `</manifest>` end tag\.

   ```
   <uses-permission android:name="android.permission.INTERNET" />
   <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
   ```

1. In `MainActivity`, add the following imports:

   ```
   import com.amazonaws.mobileconnectors.lambdainvoker.*;
   import com.amazonaws.auth.CognitoCachingCredentialsProvider;
   import com.amazonaws.regions.Regions;
   ```

1. In the `package` section, add the following two classes \(`RequestClass` and `ResponseClass`\)\. Note that the POJO is same as the POJO you created in your Lambda function in the preceding section\.
   + `RequestClass`\. The instances of this class act as the POJO \(Plain Old Java Object\) for event data which consists of first and last name\. If you are using Java example for your Lambda function you created in the preceding section, this POJO is same as the POJO you created in your Lambda function code\.

     ```
     package com.example....lambdaeventgenerator;
     public class RequestClass {
         String firstName;
         String lastName;
     
         public String getFirstName() {
             return firstName;
         }
     
         public void setFirstName(String firstName) {
             this.firstName = firstName;
         }
     
         public String getLastName() {
             return lastName;
         }
     
         public void setLastName(String lastName) {
             this.lastName = lastName;
         }
     
         public RequestClass(String firstName, String lastName) {
             this.firstName = firstName;
             this.lastName = lastName;
         }
     
         public RequestClass() {
         }
     }
     ```
   + `ResponseClass`

     ```
     package com.example....lambdaeventgenerator;
     public class ResponseClass {
         String greetings;
     
         public String getGreetings() {
             return greetings;
         }
     
         public void setGreetings(String greetings) {
             this.greetings = greetings;
         }
     
         public ResponseClass(String greetings) {
             this.greetings = greetings;
         }
     
         public ResponseClass() {
         }
     }
     ```

1. In the same package, create interface called `MyInterface` for invoking the `AndroidBackendLambdaFunction` Lambda function\. 

   ```
   package com.example.....lambdaeventgenerator;
   import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;
   public interface MyInterface {
   
       /**
        * Invoke the Lambda function "AndroidBackendLambdaFunction".
        * The function name is the method name.
        */
       @LambdaFunction
        ResponseClass AndroidBackendLambdaFunction(RequestClass request);
   
   }
   ```

   The `@LambdaFunction` annotation in the code maps the specific client method to the same\-name Lambda function\.

1. To keep the application simple, we are going to add code to invoke the Lambda function in the `onCreate()` event handler\. In `MainActivity`, add the following code toward the end of the `onCreate()` code\.

   ```
   // Create an instance of CognitoCachingCredentialsProvider
   CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
           this.getApplicationContext(), "identity-pool-id", Regions.US_WEST_2);
   
   // Create LambdaInvokerFactory, to be used to instantiate the Lambda proxy.
   LambdaInvokerFactory factory = new LambdaInvokerFactory(this.getApplicationContext(),
           Regions.US_WEST_2, cognitoProvider);
   
   // Create the Lambda proxy object with a default Json data binder.
   // You can provide your own data binder by implementing
   // LambdaDataBinder.
   final MyInterface myInterface = factory.build(MyInterface.class);
   
   RequestClass request = new RequestClass("John", "Doe");
   // The Lambda function invocation results in a network call.
   // Make sure it is not called from the main thread.
   new AsyncTask<RequestClass, Void, ResponseClass>() {
       @Override
       protected ResponseClass doInBackground(RequestClass... params) {
           // invoke "echo" method. In case it fails, it will throw a
           // LambdaFunctionException.
           try {
               return myInterface.AndroidBackendLambdaFunction(params[0]);
           } catch (LambdaFunctionException lfe) {
               Log.e("Tag", "Failed to invoke echo", lfe);
               return null;
           }
       }
   
       @Override
       protected void onPostExecute(ResponseClass result) {
           if (result == null) {
               return;
           }
   
           // Do a toast
           Toast.makeText(MainActivity.this, result.getGreetings(), Toast.LENGTH_LONG).show();
       }
   }.execute(request);
   ```

1. Run the code and verify it as follows:
   + The `Toast.makeText()` displays the response returned\.
   + Verify that CloudWatch Logs shows the log created by the Lambda function\. It should show the event data \(first name and last name\)\. You can also verify this in the AWS Lambda console\.