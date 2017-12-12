# Step 4: Create a Mobile Application for Android<a name="with-ondemand-android-mobile-create-app"></a>

Now you can create a simple Android mobile application that generates events and invokes Lambda functions by passing the event data as parameters\. 

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
**Note**  
The `@LambdaFunction` annotation in the code maps the specific client method to the same\-name Lambda function\. For more information about this annotation, see [AWS Lambda](http://docs.aws.amazon.com/mobile/sdkforandroid/developerguide/lambda.html) in the *AWS Mobile SDK for Android Developer Guide*\.

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