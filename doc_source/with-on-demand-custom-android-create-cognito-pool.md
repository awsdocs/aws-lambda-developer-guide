# Step 3: Create an Amazon Cognito Identity Pool<a name="with-on-demand-custom-android-create-cognito-pool"></a>

In this section, you create an Amazon Cognito identity pool\. The identity pool has two IAM roles\. You update the IAM role for unauthenticated users and grant permissions to execute the `AndroidBackendLambdaFunction` Lambda function\. 

For more information about IAM roles, see [IAM Roles](http://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles.html) in the *IAM User Guide*\. For more information about Amazon Cognito services, see the [Amazon Cognito](https://aws.amazon.com/cognito/) product detail page\. 

**To create an identity pool**

1. Using the IAM User Sign\-In URL, sign in to the Amazon Cognito console as **adminuser**\. 

1. Create a new identity pool called `JavaFunctionAndroidEventHandlerPool`\. Before you follow the procedure to create an identity pool, note the following:

   + The identity pool you are creating must allow access to unauthenticated identities because our example mobile application does not require a user log in \(the application users are unauthenticated\)\. Therefore, make sure to select the **Enable access to unauthenticated identities** option\.

   + The unauthenticated application users need permission to invoke the Lambda function\. To enable this, you will add the following statement to the permission policy associated with the unauthenticated identities \(it allows permission for the for the `lambda:InvokeFunction` action on the specific Lambda function \(you must update the resource ARN by providing your account ID\)\. 

     ```
     {
             "Effect": "Allow",
             "Action": [
                 "lambda:InvokeFunction"
             ],
             "Resource": [
                "arn:aws:lambda:us-east-1:account-id:function:AndroidBackendLambdaFunction"
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
**Note**  
 You can update policy at the time of creating the identity pool\. You can also update the policy after you create the identity pool, in which case make sure you write down the IAM role name for the unauthenticated users from the Amazon Cognito console\. Then, go to the IAM console and search for the specific role and edit the access permissions policy\. 

   For instructions about how to create an identity pool, log in to the [Amazon Cognito console](https://console.aws.amazon.com/cognito/home) and follow the **New Identity Pool** wizard\.

1. Note down the identity pool ID\. You specify this ID in your mobile application you create in the next section\. The app uses this ID when it sends request to Amazon Cognito to request for temporary security credentials\.

## Next Step<a name="with-ondemand-android-mobile-cognito-pool-next-step"></a>

[Step 4: Create a Mobile Application for Android](with-ondemand-android-mobile-create-app.md)