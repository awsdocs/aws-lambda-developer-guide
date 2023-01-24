# Using AWS Lambda with AWS CloudTrail<a name="with-cloudtrail"></a>

AWS CloudTrail is a service that provides a record of actions taken by a user, role, or an AWS service\. CloudTrail captures API calls as events\. For an ongoing record of events in your AWS account, you create a trail\. A trail enables CloudTrail to deliver log files of events to an Amazon S3 bucket\.

You can take advantage of Amazon S3's bucket notification feature and direct Amazon S3 to publish object\-created events to AWS Lambda\. Whenever CloudTrail writes logs to your S3 bucket, Amazon S3 can then invoke your Lambda function by passing the Amazon S3 object\-created event as a parameter\. The S3 event provides information, including the bucket name and key name of the log object that CloudTrail created\. Your Lambda function code can read the log object and process the access records logged by CloudTrail\. For example, you might write Lambda function code to notify you if specific API call was made in your account\. 

In this scenario, CloudTrail writes access logs to your S3 bucket\. As for AWS Lambda, Amazon S3 is the event source so Amazon S3 publishes events to AWS Lambda and invokes your Lambda function\. 

**Example CloudTrail log**  

```
{  
   "Records":[  
      {  
         "eventVersion":"1.02",
         "userIdentity":{  
            "type":"Root",
            "principalId":"123456789012",
            "arn":"arn:aws:iam::123456789012:root",
            "accountId":"123456789012",
            "accessKeyId":"access-key-id",
            "sessionContext":{  
               "attributes":{  
                  "mfaAuthenticated":"false",
                  "creationDate":"2015-01-24T22:41:54Z"
               }
            }
         },
         "eventTime":"2015-01-24T23:26:50Z",
         "eventSource":"sns.amazonaws.com",
         "eventName":"CreateTopic",
         "awsRegion":"us-east-2",
         "sourceIPAddress":"205.251.233.176",
         "userAgent":"console.amazonaws.com",
         "requestParameters":{  
            "name":"dropmeplease"
         },
         "responseElements":{  
            "topicArn":"arn:aws:sns:us-east-2:123456789012:exampletopic"
         },
         "requestID":"3fdb7834-9079-557e-8ef2-350abc03536b",
         "eventID":"17b46459-dada-4278-b8e2-5a4ca9ff1a9c",
         "eventType":"AwsApiCall",
         "recipientAccountId":"123456789012"
      },
      {  
         "eventVersion":"1.02",
         "userIdentity":{  
            "type":"Root",
            "principalId":"123456789012",
            "arn":"arn:aws:iam::123456789012:root",
            "accountId":"123456789012",
            "accessKeyId": "AKIAIOSFODNN7EXAMPLE",
            "sessionContext":{  
               "attributes":{  
                  "mfaAuthenticated":"false",
                  "creationDate":"2015-01-24T22:41:54Z"
               }
            }
         },
         "eventTime":"2015-01-24T23:27:02Z",
         "eventSource":"sns.amazonaws.com",
         "eventName":"GetTopicAttributes",
         "awsRegion":"us-east-2",
         "sourceIPAddress":"205.251.233.176",
         "userAgent":"console.amazonaws.com",
         "requestParameters":{  
            "topicArn":"arn:aws:sns:us-east-2:123456789012:exampletopic"
         },
         "responseElements":null,
         "requestID":"4a0388f7-a0af-5df9-9587-c5c98c29cbec",
         "eventID":"ec5bb073-8fa1-4d45-b03c-f07b9fc9ea18",
         "eventType":"AwsApiCall",
         "recipientAccountId":"123456789012"
      }
   ]
}
```

For detailed information about how to configure Amazon S3 as the event source, see [Using AWS Lambda with Amazon S3](with-s3.md)\.

**Topics**
+ [Logging Lambda API calls with CloudTrail](logging-using-cloudtrail.md)
+ [Sample function code](with-cloudtrail-create-package.md)