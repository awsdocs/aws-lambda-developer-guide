# Step 3: Set Up Cross\-Account Permissions<a name="with-sns-create-x-account-permissions"></a>

In this section, you use CLI commands to set permissions across the Lambda function account and the Amazon SNS topic account and then test the subscription\. 

1. From account A, create the Amazon SNS topic:

   ```
   aws sns create-topic \
       --name lambda-x-account
   ```

   Note the topic arn that is returned by the command\. You will need it when you add permissions to the Lambda function to subscribe to the topic\.

1. From account B, create the Lambda function\. For the runtime parameter, select `nodejs8.10`, `python3.6`, `go1.x` or `java8`, depending on the code sample you selected when you created your deployment package\.

   ```
   aws lambda create-function \
       --function-name SNS-X-Account \
       --runtime runtime \
       --role role arn \
       --handler handler-name \
       --description "SNS X Account Test Function" \
       --timeout 60 \
       --memory-size 128 \
       --zip-file fileb://path/LambdaWithSNS.zip
   ```

   Note the function arn that is returned by the command\. You will need it when you add permissions to allow Amazon SNS to invoke your function\.

1. From account A add permission to account B to subscribe to the topic:

   ```
   aws sns add-permission \
       --region us-east-1 \
       --topic-arn Amazon SNS topic arn \
       --label lambda-access \
       --aws-account-id B \
       --action-name Subscribe ListSubscriptionsByTopic Receive
   ```

1. From account B add the Lambda permission to allow invocation from Amazon SNS:

   ```
   aws lambda add-permission \
       --function-name SNS-X-Account \
       --statement-id sns-x-account \
       --action "lambda:InvokeFunction" \
       --principal sns.amazonaws.com \
       --source-arn Amazon SNS topic arn
   ```

   In response, Lambda returns the following JSON code\. The Statement value is a JSON string version of the statement added to the Lambda function policy: 

   ```
   {
       "Statement": "{\"Condition\":{\"ArnLike\":{\"AWS:SourceArn\":\"arn:aws:lambda:us-east-1:B:function:SNS-X-Account\"}},\"Action\":[\"lambda:InvokeFunction\"],\"Resource\":\"arn:aws:lambda:us-east-1:A:function:SNS-X-Account\",\"Effect\":\"Allow\",\"Principal\":{\"Service\":\"sns.amazonaws.com\"},\"Sid\":\"sns-x-account1\"}"
   }
   ```
**Note**  
Do not use the \-\-source\-account parameter to add a source account to the Lambda policy when adding the policy\. Source account is not supported for Amazon SNS event sources and will result in access being denied\. This has no security impact as the source account is included in the source ARN\. 

1. From account B subscribe the Lambda function to the topic: 

   ```
   aws sns subscribe \
       --topic-arn Amazon SNS topic arn \
       --protocol lambda \
       --notification-endpoint arn:aws:lambda:us-east-1:B:function:SNS-X-Account
   ```

   You should see JSON output similar to the following: 

   ```
   {
       "SubscriptionArn": "arn:aws:sns:us-east-1:A:lambda-x-account:5d906xxxx-7c8x-45dx-a9dx-0484e31c98xx"
   }
   ```

1. From account A you can now test the subscription\. Type "Hello World" into a text file and save it as message\.txt\. Then run the following command: 

   ```
   aws sns publish \
       --topic-arn arn:aws:sns:us-east-1:A:lambda-x-account \
       --message file://message.txt \
       --subject Test
   ```

   This will return a message id with a unique identifier, indicating the message has been accepted by the Amazon SNS service\. Amazon SNS will then attempt to deliver it to the topic's subscribers\. 

**Note**  
Alternatively, you could supply a JSON string directly to the `message` parameter, but using a text file allows for line breaks in the message\.

For more information on Amazon SNS, see [What is Amazon Simple Notification Service?](https://docs.aws.amazon.com/sns/latest/dg/)