# Using AWS Lambda with Amazon S3<a name="with-s3"></a>

Amazon S3 can publish events \(for example, when an object is created in a bucket\) to AWS Lambda and invoke your Lambda function by passing the event data as a parameter\. This integration enables you to write Lambda functions that process Amazon S3 events\. In Amazon S3, you add bucket notification configuration that identifies the type of event that you want Amazon S3 to publish and the Lambda function that you want to invoke\. 

**Important**  
If your Lambda function uses the same bucket that triggers it, it could cause the function to execute in a loop\. For example, if the bucket triggers a function each time an object is uploaded, and the function uploads an object to the bucket, then the function indirectly triggers itself\. To avoid this, use two buckets, or configure the trigger to only apply to a prefix used for incoming objects\.

**Example Amazon S3 Message Event**  

```
{
  "Records":[  
    {  
      "eventVersion":"2.0",
      "eventSource":"aws:s3",
      "awsRegion":"us-west-2",
      "eventTime":"1970-01-01T00:00:00.000Z",
      "eventName":"ObjectCreated:Put",
      "userIdentity":{  
        "principalId":"AIDAJDPLRKLG7UEXAMPLE"
      },
      "requestParameters":{  
        "sourceIPAddress":"127.0.0.1"
      },
      "responseElements":{  
        "x-amz-request-id":"C3D13FE58DE4C810",
        "x-amz-id-2":"FMyUVURIY8/IgAtTv8xRjskZQpcIZ9KG4V5Wp6S7S/JRWeUWerMUE5JgHvANOjpD"
      },
      "s3":{  
        "s3SchemaVersion":"1.0",
        "configurationId":"testConfigRule",
        "bucket":{  
          "name":"sourcebucket",
          "ownerIdentity":{  
            "principalId":"A3NL1KOZZKExample"
          },
          "arn":"arn:aws:s3:::sourcebucket"
        },
        "object":{  
          "key":"HappyFace.jpg",
          "size":1024,
          "eTag":"d41d8cd98f00b204e9800998ecf8427e",
          "versionId":"096fKKXTRTtl3on89fVO.nfljtsv6qko"
        }
      }
    }
  ]
}
```

Note the following about how the Amazon S3 and AWS Lambda integration works:
+ **Non\-stream based \(async\) model** – This is a model \(see [AWS Lambda Event Source Mapping](intro-invocation-modes.md)\), where Amazon S3 monitors a bucket and invokes the Lambda function by passing the event data as a parameter\. In a push model, you maintain event source mapping within Amazon S3 using the bucket notification configuration\. In the configuration, you specify the event types that you want Amazon S3 to monitor and which AWS Lambda function you want Amazon S3 to invoke\. For more information, see [Configuring Amazon S3 Event Notifications](https://docs.aws.amazon.com/AmazonS3/latest/dev/NotificationHowTo.html) in the *Amazon Simple Storage Service Developer Guide*\.
+ **Asynchronous invocation** – AWS Lambda invokes a Lambda function using the `Event` invocation type \(asynchronous invocation\)\. For more information about invocation types, see [Invocation Types](invocation-options.md)\. 
+ **Event structure** – The event your Lambda function receives is for a single object and it provides information, such as the bucket name and object key name\. 

Note that there are two types of permissions policies that you work with when you set up the end\-to\-end experience:
+ **Permissions for your Lambda function** – Regardless of what invokes a Lambda function, AWS Lambda executes the function by assuming the IAM role \(execution role\) that you specify at the time you create the Lambda function\. Using the permissions policy associated with this role, you grant your Lambda function the permissions that it needs\. For example, if your Lambda function needs to read an object, you grant permissions for the relevant Amazon S3 actions in the permissions policy\. For more information, see [AWS Lambda Execution Role](lambda-intro-execution-role.md)\.
+ **Permissions for Amazon S3 to invoke your Lambda function** – Amazon S3 cannot invoke your Lambda function without your permission\. You grant this permission via the permissions policy associated with the Lambda function\.

The following diagram summarizes the flow: 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/push-s3-example-10.png)

1. User uploads an object to an S3 bucket \(object\-created event\)\.

1. Amazon S3 detects the object\-created event\. 

1. Amazon S3 invokes a Lambda function that is specified in the bucket notification configuration\. 

1. AWS Lambda executes the Lambda function by assuming the execution role that you specified at the time you created the Lambda function\.

1. The Lambda function executes\.

**Topics**
+ [Tutorial: Using AWS Lambda with Amazon S3](with-s3-example.md)
+ [Sample Amazon Simple Storage Service Function Code](with-s3-example-deployment-pkg.md)
+ [AWS SAM Template for an Amazon S3 Application](with-s3-example-use-app-spec.md)