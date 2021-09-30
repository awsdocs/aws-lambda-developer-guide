# Using AWS Lambda with Amazon RDS<a name="services-rds"></a>

You can use AWS Lambda to process event notifications from an Amazon Relational Database Service \(Amazon RDS\) database\. Amazon RDS sends notifications to an Amazon Simple Notification Service \(Amazon SNS\) topic, which you can configure to invoke a Lambda function\. Amazon SNS wraps the message from Amazon RDS in its own event document and sends it to your function\.

**Example Amazon RDS message in an Amazon SNS event**  

```
{
      "Records": [
        {
          "EventVersion": "1.0",
          "EventSubscriptionArn": "arn:aws:sns:us-east-2:123456789012:rds-lambda:21be56ed-a058-49f5-8c98-aedd2564c486",
          "EventSource": "aws:sns",
          "Sns": {
            "SignatureVersion": "1",
            "Timestamp": "2019-01-02T12:45:07.000Z",
            "Signature": "tcc6faL2yUC6dgZdmrwh1Y4cGa/ebXEkAi6RibDsvpi+tE/1+82j...65r==",
            "SigningCertUrl": "https://sns.us-east-2.amazonaws.com/SimpleNotificationService-ac565b8b1a6c5d002d285f9598aa1d9b.pem",
            "MessageId": "95df01b4-ee98-5cb9-9903-4c221d41eb5e",
            "Message": "{\"Event Source\":\"db-instance\",\"Event Time\":\"2019-01-02 12:45:06.000\",\"Identifier Link\":\"https://console.aws.amazon.com/rds/home?region=eu-west-1#dbinstance:id=dbinstanceid\",\"Source ID\":\"dbinstanceid\",\"Event ID\":\"http://docs.amazonwebservices.com/AmazonRDS/latest/UserGuide/USER_Events.html#RDS-EVENT-0002\",\"Event Message\":\"Finished DB Instance backup\"}",
            "MessageAttributes": {},
            "Type": "Notification",
            "UnsubscribeUrl": "https://sns.us-east-2.amazonaws.com/?Action=Unsubscribe&amp;SubscriptionArn=arn:aws:sns:us-east-2:123456789012:test-lambda:21be56ed-a058-49f5-8c98-aedd2564c486",
            "TopicArn":"arn:aws:sns:us-east-2:123456789012:sns-lambda",
            "Subject": "RDS Notification Message"
          }
        }
      ]
    }
```

**Topics**
+ [Tutorial: Configuring a Lambda function to access Amazon RDS in an Amazon VPC](services-rds-tutorial.md)
+ [Configuring the function](#configuration)

## Configuring the function<a name="configuration"></a>

The following section shows additional configurations and topics we recommend as part of this tutorial\.
+ If too many function instances run concurrently, one or more instances may fail to obtain a database connection\. You can use reserved concurrency to limit the maximum concurrency of the function\. Set the reserved concurrency to be less than the number of database connections\. Reserved concurrency also reserves those instances for this function, which may not be ideal\. If you are invoking the Lambda functions from your application, we recommend you write code that limits the number of concurrent instances\. For more information, see [Managing concurrency for a Lambda function](https://docs.aws.amazon.com/lambda/latest/dg/configuration-concurrency.html)\.
+ For more information on configuring an Amazon RDS database to send notifications, see [Using Amazon RDS event notifications](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/USER_Events.html)\. 
+ For more information on using Amazon SNS as trigger, see [Using AWS Lambda with Amazon SNS](with-sns.md)\.