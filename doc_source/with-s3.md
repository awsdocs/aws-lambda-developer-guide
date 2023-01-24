# Using AWS Lambda with Amazon S3<a name="with-s3"></a>

You can use Lambda to process [event notifications](https://docs.aws.amazon.com/AmazonS3/latest/dev/NotificationHowTo.html) from Amazon Simple Storage Service\. Amazon S3 can send an event to a Lambda function when an object is created or deleted\. You configure notification settings on a bucket, and grant Amazon S3 permission to invoke a function on the function's resource\-based permissions policy\.

**Warning**  
If your Lambda function uses the same bucket that triggers it, it could cause the function to run in a loop\. For example, if the bucket triggers a function each time an object is uploaded, and the function uploads an object to the bucket, then the function indirectly triggers itself\. To avoid this, use two buckets, or configure the trigger to only apply to a prefix used for incoming objects\.

Amazon S3 invokes your function [asynchronously](invocation-async.md) with an event that contains details about the object\. The following example shows an event that Amazon S3 sent when a deployment package was uploaded to Amazon S3\.

**Example Amazon S3 notification event**  

```
{
  "Records": [
    {
      "eventVersion": "2.1",
      "eventSource": "aws:s3",
      "awsRegion": "us-east-2",
      "eventTime": "2019-09-03T19:37:27.192Z",
      "eventName": "ObjectCreated:Put",
      "userIdentity": {
        "principalId": "AWS:AIDAINPONIXQXHT3IKHL2"
      },
      "requestParameters": {
        "sourceIPAddress": "205.255.255.255"
      },
      "responseElements": {
        "x-amz-request-id": "D82B88E5F771F645",
        "x-amz-id-2": "vlR7PnpV2Ce81l0PRw6jlUpck7Jo5ZsQjryTjKlc5aLWGVHPZLj5NeC6qMa0emYBDXOo6QBU0Wo="
      },
      "s3": {
        "s3SchemaVersion": "1.0",
        "configurationId": "828aa6fc-f7b5-4305-8584-487c791949c1",
        "bucket": {
          "name": "DOC-EXAMPLE-BUCKET",
          "ownerIdentity": {
            "principalId": "A3I5XTEXAMAI3E"
          },
          "arn": "arn:aws:s3:::lambda-artifacts-deafc19498e3f2df"
        },
        "object": {
          "key": "b21b84d653bb07b05b1e6b33684dc11b",
          "size": 1305107,
          "eTag": "b21b84d653bb07b05b1e6b33684dc11b",
          "sequencer": "0C0F6F405D6ED209E1"
        }
      }
    }
  ]
}
```

To invoke your function, Amazon S3 needs permission from the function's [resource\-based policy](access-control-resource-based.md)\. When you configure an Amazon S3 trigger in the Lambda console, the console modifies the resource\-based policy to allow Amazon S3 to invoke the function if the bucket name and account ID match\. If you configure the notification in Amazon S3, you use the Lambda API to update the policy\. You can also use the Lambda API to grant permission to another account, or restrict permission to a designated alias\.

If your function uses the AWS SDK to manage Amazon S3 resources, it also needs Amazon S3 permissions in its [execution role](lambda-intro-execution-role.md)\. 

**Topics**
+ [Tutorial: Using an Amazon S3 trigger to invoke a Lambda function](with-s3-example.md)
+ [Tutorial: Using an Amazon S3 trigger to create thumbnail images](with-s3-tutorial.md)
+ [AWS SAM template for an Amazon S3 application](with-s3-example-use-app-spec.md)