# Tutorial: Using AWS Lambda with Amazon Simple Notification Service<a name="with-sns-example"></a>

You can use a Lambda function in one AWS account to subscribe to an Amazon SNS topic in a separate AWS account\. In this tutorial, you use the AWS Command Line Interface to perform AWS Lambda operations such as creating a Lambda function, creating an Amazon SNS topic and granting permissions to allow these two resources to access each other\. 

## Prerequisites<a name="with-sns-prepare"></a>

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

In the tutorial, you use two accounts\. The AWS CLI commands illustrate this by using two [named profiles](https://docs.aws.amazon.com/cli/latest/userguide/cli-multiple-profiles.html), each configured for use with a different account\. If you use profiles with different names, or the default profile and one named profile, modify the commands as needed\.

## Create an Amazon SNS topic \(account A\)<a name="with-sns-create-topic"></a>

In **Account A**, create the source Amazon SNS topic\.

```
aws sns create-topic --name sns-topic-for-lambda --profile accountA
```

After creating the topic, record its Amazon Resource Name \(ARN\)\. You need it later when you add permissions to the Lambda function to subscribe to the topic\.

## Create the execution role \(account B\)<a name="with-sns-example-create-iam-role"></a>

In **Account B**, create the [execution role](lambda-intro-execution-role.md) that gives your function permission to access AWS resources\.

**To create an execution role**

1. Open the [roles page](https://console.aws.amazon.com/iam/home#/roles) in the IAM console\.

1. Choose **Create role**\.

1. Create a role with the following properties\.
   + **Trusted entity** – **AWS Lambda**\.
   + **Permissions** – **AWSLambdaBasicExecutionRole**\.
   + **Role name** – **lambda\-sns\-role**\.

The **AWSLambdaBasicExecutionRole** policy has the permissions that the function needs to write logs to CloudWatch Logs\.

## Create a Lambda function \(account B\)<a name="with-sns-example-create-test-function"></a>

In **Account B**, create the function that processes events from Amazon SNS\. The following example code receives an Amazon SNS event input and processes the messages that it contains\. For illustration, the code writes some of the incoming event data to CloudWatch Logs\.

**Note**  
For sample code in other languages, see [Sample function code](with-sns-create-package.md)\.

**Example index\.js**  

```
console.log('Loading function');

exports.handler = function(event, context, callback) {
// console.log('Received event:', JSON.stringify(event, null, 4));

    var message = event.Records[0].Sns.Message;
    console.log('Message received from SNS:', message);
    callback(null, "Success");
};
```

**To create the function**

1. Copy the sample code into a file named `index.js`\.

1. Create a deployment package\.

   ```
   zip function.zip index.js
   ```

1. Create a Lambda function with the `create-function` command\.

   ```
   aws lambda create-function --function-name Function-With-SNS \
   --zip-file fileb://function.zip --handler index.handler --runtime nodejs12.x \
   --role arn:aws:iam::<AccountB_ID>:role/lambda-sns-role  \
   --timeout 60 --profile accountB
   ```

After creating the function, record its function ARN\. You need it later when you add permissions to allow Amazon SNS to invoke your function\.

## Set up cross\-account permissions \(account A and B\)<a name="with-sns-create-x-account-permissions"></a>

In **Account A**, grant permission to **Account B** to subscribe to the topic:

```
aws sns add-permission --label lambda-access --aws-account-id <AccountB_ID> \
--topic-arn arn:aws:sns:us-east-2:<AccountA_ID>:sns-topic-for-lambda \
--action-name Subscribe ListSubscriptionsByTopic --profile accountA
```

In **Account B**, add the Lambda permission to allow invocation from Amazon SNS\.

```
aws lambda add-permission --function-name Function-With-SNS \
--source-arn arn:aws:sns:us-east-2:<AccountA_ID>:sns-topic-for-lambda \
--statement-id function-with-sns --action "lambda:InvokeFunction" \
--principal sns.amazonaws.com --profile accountB
```

You should see the following output:

```
{
    "Statement": "{\"Condition\":{\"ArnLike\":{\"AWS:SourceArn\":
      \"arn:aws:sns:us-east-2:<AccountA_ID>:sns-topic-for-lambda\"}},
      \"Action\":[\"lambda:InvokeFunction\"],
      \"Resource\":\"arn:aws:lambda:us-east-2:<AccountB_ID>:function:Function-With-SNS\",
      \"Effect\":\"Allow\",\"Principal\":{\"Service\":\"sns.amazonaws.com\"},
      \"Sid\":\"function-with-sns1\"}"
}
```

Do not use the `--source-account` parameter to add a source account to the Lambda policy when adding the policy\. Source account is not supported for Amazon SNS event sources and will result in access being denied\.

**Note**  
If the account with the SNS topic is hosted in an [opt\-in region](https://docs.aws.amazon.com/general/latest/gr/rande-manage.html#rande-manage-enable), you need to specify the region in the principal\. For example, if you're working with an SNS topic in the Asia Pacific \(Hong Kong\) region, you need to specify `sns.ap-east-1.amazonaws.com` instead of `sns.amazonaws.com` for the principal\. 

## Create a subscription \(account B\)<a name="with-sns-create-subscription"></a>

In **Account B**, subscribe the Lambda function to the topic\. When a message is sent to the `sns-topic-for-lambda` topic in **Account A**, Amazon SNS invokes the `Function-With-SNS` function in **Account B**\.

```
aws sns subscribe --protocol lambda \
--topic-arn arn:aws:sns:us-east-2:<AccountA_ID>:sns-topic-for-lambda \
--notification-endpoint arn:aws:lambda:us-east-2:<AccountB_ID>:function:Function-With-SNS \
--profile accountB
```

You should see the following output:

```
{
    "SubscriptionArn": "arn:aws:sns:us-east-2:<AccountA_ID>:sns-topic-for-lambda:5d906xxxx-7c8x-45dx-a9dx-0484e31c98xx"
}
```

The output contains the ARN of the topic subscription\.

## Test subscription \(account A\)<a name="with-sns-create-test"></a>

In **Account A**, test the subscription\. Type `Hello World` into a text file and save it as `message.txt`\. Then run the following command: 

```
aws sns publish --message file://message.txt --subject Test \
--topic-arn arn:aws:sns:us-east-2:<AccountA_ID>:sns-topic-for-lambda \
--profile accountA
```

This will return a message id with a unique identifier, indicating the message has been accepted by the Amazon SNS service\. Amazon SNS will then attempt to deliver it to the topic's subscribers\. Alternatively, you could supply a JSON string directly to the `message` parameter, but using a text file allows for line breaks in the message\.

To learn more about Amazon SNS, see [What is Amazon Simple Notification Service](https://docs.aws.amazon.com/sns/latest/dg/)\.

## Clean up your resources<a name="cleanup"></a>

You can now delete the resources that you created for this tutorial, unless you want to retain them\. By deleting AWS resources that you're no longer using, you prevent unnecessary charges to your AWS account\.

In **Account A**, clean up your Amazon SNS topic\.

**To delete the Amazon SNS topic**

1. Open the [Topics page](https://console.aws.amazon.com/sns/home#topics:) of the Amazon SNS console\.

1. Select the topic you created\.

1. Choose **Delete**\.

1. Enter **delete me** in the text box\.

1. Choose **Delete**\.

In **Account B**, clean up your execution role, Lambda function, and Amazon SNS subscription\.

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

**To delete the Amazon SNS subscription**

1. Open the [Subscriptions page](https://console.aws.amazon.com/sns/home#subscriptions:) of the Amazon SNS console\.

1. Select the subscription you created\.

1. Choose **Delete**, **Delete**\.