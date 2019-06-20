# Using AWS Lambda with Amazon SNS<a name="with-sns"></a>

You can write Lambda functions to process Amazon Simple Notification Service notifications\. When a message is published to an Amazon SNS topic, the service can invoke your Lambda function by passing the message payload as a parameter\. Your Lambda function code can then process the event, for example publish the message to other Amazon SNS topics, or send the message to other AWS services\.

When a user calls the SNS Publish API on a topic that your Lambda function is subscribed to, Amazon SNS will call Lambda to invoke your function asynchronously\. Lambda will then return a delivery status\. If there was an error calling Lambda, Amazon SNS will retry invoking the Lambda function up to three times\. After three tries, if Amazon SNS still could not successfully invoke the Lambda function, then Amazon SNS will send a delivery status failure message to CloudWatch\.

In order to perform cross account Amazon SNS deliveries to Lambda, you need to authorize your Lambda function to be invoked from Amazon SNS\. In turn, Amazon SNS needs to allow the Lambda account to subscribe to the Amazon SNS topic\. For example, if the Amazon SNS topic is in account A and the Lambda function is in account B, both accounts must grant permissions to the other to access their respective resources\. Since not all the options for setting up cross\-account permissions are available from the AWS console, you use the AWS CLI to set up the entire process\.

**Example Amazon SNS Message Event**  

```
{
  "Records": [
    {
      "EventVersion": "1.0",
      "EventSubscriptionArn": "arn:aws:sns:us-east-2:123456789012:sns-lambda:21be56ed-a058-49f5-8c98-aedd2564c486",
      "EventSource": "aws:sns",
      "Sns": {
        "SignatureVersion": "1",
        "Timestamp": "2019-01-02T12:45:07.000Z",
        "Signature": "tcc6faL2yUC6dgZdmrwh1Y4cGa/ebXEkAi6RibDsvpi+tE/1+82j...65r==",
        "SigningCertUrl": "https://sns.us-east-2.amazonaws.com/SimpleNotificationService-ac565b8b1a6c5d002d285f9598aa1d9b.pem",
        "MessageId": "95df01b4-ee98-5cb9-9903-4c221d41eb5e",
        "Message": "Hello from SNS!",
        "MessageAttributes": {
          "Test": {
            "Type": "String",
            "Value": "TestString"
          },
          "TestBinary": {
            "Type": "Binary",
            "Value": "TestBinary"
          }
        },
        "Type": "Notification",
        "UnsubscribeUrl": "https://sns.us-east-2.amazonaws.com/?Action=Unsubscribe&amp;SubscriptionArn=arn:aws:sns:us-east-2:123456789012:test-lambda:21be56ed-a058-49f5-8c98-aedd2564c486",
        "TopicArn":"arn:aws:sns:us-east-2:123456789012:sns-lambda",
        "Subject": "TestInvoke"
      }
    }
  ]
}
```

You configure the event source mapping in Amazon SNS via topic subscription configuration\. For more information, see [Invoking Lambda functions using Amazon SNS notifications](https://docs.aws.amazon.com/sns/latest/dg/sns-lambda.html) in the *Amazon Simple Notification Service Developer Guide*\. 

**Topics**
+ [Tutorial: Using AWS Lambda with Amazon Simple Notification Service](with-sns-example.md)
+ [Sample Function Code](with-sns-create-package.md)