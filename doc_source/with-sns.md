# Using AWS Lambda with Amazon SNS<a name="with-sns"></a>

You can use a Lambda function to process Amazon Simple Notification Service \(Amazon SNS\) notifications\. Amazon SNS supports Lambda functions as a target for messages sent to a topic\. You can subscribe your function to topics in the same account or in other AWS accounts\.

Amazon SNS invokes your function [asynchronously](invocation-async.md) with an event that contains a message and metadata\.

**Example Amazon SNS message event**  

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

For asynchronous invocation, Lambda queues the message and handles retries\. If Amazon SNS can't reach Lambda or the message is rejected, Amazon SNS retries at increasing intervals over several hours\. For details, see [Reliability](http://aws.amazon.com/sns/faqs/#Reliability) in the Amazon SNS FAQs\.

To perform cross\-account Amazon SNS deliveries to Lambda, you must authorize Amazon SNS to invoke your Lambda function\. In turn, Amazon SNS must allow the AWS account with the Lambda function to subscribe to the Amazon SNS topic\. For example, if the Amazon SNS topic is in account A and the Lambda function is in account B, both accounts must grant permissions to the other to access their respective resources\. Since not all the options for setting up cross\-account permissions are available from the AWS Management Console, you must use the AWS Command Line Interface \(AWS CLI\) for setup\.

For more information, see [Fanout to Lambda functions](https://docs.aws.amazon.com/sns/latest/dg/sns-lambda-as-subscriber.html) in the *Amazon Simple Notification Service Developer Guide*\.

**Input types for Amazon SNS events**

For input type examples for Amazon SNS events in Java, \.NET, and Go, see the following on the AWS GitHub repository:
+ [SNSEvent\.java](https://github.com/aws/aws-lambda-java-libs/blob/master/aws-lambda-java-events/src/main/java/com/amazonaws/services/lambda/runtime/events/SNSEvent.java)
+ [SNSEvent\.cs](https://github.com/aws/aws-lambda-dotnet/blob/master/Libraries/src/Amazon.Lambda.SNSEvents/SNSEvent.cs)
+ [sns\.go](https://github.com/aws/aws-lambda-go/blob/master/events/sns.go)

**Topics**
+ [Tutorial: Using AWS Lambda with Amazon Simple Notification Service](with-sns-example.md)
+ [Sample function code](with-sns-create-package.md)