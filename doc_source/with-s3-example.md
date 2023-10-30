# Tutorial: Using an Amazon S3 trigger to invoke a Lambda function<a name="with-s3-example"></a>

In this tutorial, you use the console to create a Lambda function and configure a trigger for Amazon Simple Storage Service \(Amazon S3\)\. The trigger invokes your function every time that you add an object to your Amazon S3 bucket\.

We recommend that you complete this console\-based tutorial before you try the [tutorial to create thumbnail images](with-s3-tutorial.md)\.

## Prerequisites<a name="with-s3-example-prepare"></a>

To use Lambda and other AWS services, you need an AWS account\. If you do not have an account, visit [aws\.amazon\.com](https://aws.amazon.com/) and choose **Create an AWS Account**\. For instructions, see [How do I create and activate a new AWS account?](http://aws.amazon.com/premiumsupport/knowledge-center/create-and-activate-aws-account/)

This tutorial assumes that you have some knowledge of basic Lambda operations and the Lambda console\. If you haven't already, follow the instructions in [Create a Lambda function with the console](getting-started.md#getting-started-create-function) to create your first Lambda function\.

## Create a bucket and upload a sample object<a name="with-s3-example-prepare-create-buckets"></a>

Create an Amazon S3 bucket and upload a test file to your new bucket\. Your Lambda function retrieves information about this file when you test the function from the console\.

**To create an Amazon S3 bucket using the console**

1. Open the [Amazon S3 console](https://console.aws.amazon.com/s3)\.

1. Choose **Create bucket**\.

1. Under **General configuration**, do the following:

   1. For **Bucket name**, enter a unique name\.

   1. For **AWS Region**, choose a Region\. Note that you must create your Lambda function in the same Region\.

1. Choose **Create bucket**\.

After creating the bucket, Amazon S3 opens the **Buckets** page, which displays a list of all buckets in your account in the current Region\.

**To upload a test object using the Amazon S3 console**

1. On the [Buckets page](https://console.aws.amazon.com/s3/home) of the Amazon S3 console, choose the name of the bucket that you created\.

1. On the **Objects** tab, choose **Upload**\.

1. Drag a test file from your local machine to the **Upload **page\.

1. Choose **Upload**\.

## Create the Lambda function<a name="with-s3-example-create-function"></a>

Use a [function blueprint](gettingstarted-features.md#gettingstarted-features-blueprints) to create the Lambda function\. A blueprint provides a sample function that demonstrates how to use Lambda with other AWS services\. Also, a blueprint includes sample code and function configuration presets for a certain runtime\. For this tutorial, you can choose the blueprint for the Node\.js or Python runtime\.

**To create a Lambda function from a blueprint in the console**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose **Create function**\.

1. On the **Create function** page, choose **Use a blueprint**\.

1. Under **Blueprints**, enter **s3** in the search box\.

1. In the search results, do one of the following:
   + For a Node\.js function, choose **s3\-get\-object**\.
   + For a Python function, choose **s3\-get\-object\-python**\.

1. Choose **Configure**\.

1. Under **Basic information**, do the following:

   1. For **Function name**, enter **my\-s3\-function**\.

   1. For **Execution role**, choose **Create a new role from AWS policy templates**\.

   1. For **Role name**, enter **my\-s3\-function\-role**\.

1. Under **S3 trigger**, choose the S3 bucket that you created previously\.

   When you configure an S3 trigger using the Lambda console, the console modifies your function's [resource\-based policy](access-control-resource-based.md) to allow Amazon S3 to invoke the function\.

1. Choose **Create function**\.

## Review the function code<a name="with-s3-example-create-function-code"></a>

The Lambda function retrieves the source S3 bucket name and the key name of the uploaded object from the event parameter that it receives\. The function uses the Amazon S3 `getObject` API to retrieve the content type of the object\.

While viewing your function in the [Lambda console](https://console.aws.amazon.com/lambda), you can review the function code on the **Code** tab, under **Code source**\. The code looks like the following:

------
#### [ Node\.js ]

**Example index\.js**  

```
console.log('Loading function');
        
const aws = require('aws-sdk');

const s3 = new aws.S3({ apiVersion: '2006-03-01' });

exports.handler = async (event, context) => {
    //console.log('Received event:', JSON.stringify(event, null, 2));

    // Get the object from the event and show its content type
    const bucket = event.Records[0].s3.bucket.name;
    const key = decodeURIComponent(event.Records[0].s3.object.key.replace(/\+/g, ' '));
    const params = {
        Bucket: bucket,
        Key: key,
    }; 
    try {
        const { ContentType } = await s3.getObject(params).promise();
        console.log('CONTENT TYPE:', ContentType);
        return ContentType;
    } catch (err) {
        console.log(err);
        const message = `Error getting object ${key} from bucket ${bucket}. Make sure they exist and your bucket is in the same region as this function.`;
        console.log(message);
        throw new Error(message);
    }
};
```

------
#### [ Python ]

**Example lambda\-function\.py**  

```
import json
import urllib.parse
import boto3

print('Loading function')

s3 = boto3.client('s3')


def lambda_handler(event, context):
    #print("Received event: " + json.dumps(event, indent=2))

    # Get the object from the event and show its content type
    bucket = event['Records'][0]['s3']['bucket']['name']
    key = urllib.parse.unquote_plus(event['Records'][0]['s3']['object']['key'], encoding='utf-8')
    try:
        response = s3.get_object(Bucket=bucket, Key=key)
        print("CONTENT TYPE: " + response['ContentType'])
        return response['ContentType']
    except Exception as e:
        print(e)
        print('Error getting object {} from bucket {}. Make sure they exist and your bucket is in the same region as this function.'.format(key, bucket))
        raise e
```

------

## Test in the console<a name="test-manual-invoke"></a>

Invoke the Lambda function manually using sample Amazon S3 event data\.

**To test the Lambda function using the console**

1. On the **Code** tab, under **Code source**, choose the arrow next to **Test**, and then choose **Configure test events** from the dropdown list\.

1. In the **Configure test event** window, do the following:

   1. Choose **Create new test event**\.

   1. For **Event template**, choose **Amazon S3 Put** \(**s3\-put**\)\.

   1. For **Event name**, enter a name for the test event\. For example, **mys3testevent**\.

   1. In the test event JSON, replace the S3 bucket name \(`example-bucket`\) and object key \(`test%2Fkey`\) with your bucket name and test file name (that you uploaded in your bucket)\. Your test event should look similar to the following:

      ```
      {
        "Records": [
          {
            "eventVersion": "2.0",
            "eventSource": "aws:s3",
            "awsRegion": "us-west-2",
            "eventTime": "1970-01-01T00:00:00.000Z",
            "eventName": "ObjectCreated:Put",
            "userIdentity": {
              "principalId": "EXAMPLE"
            },
            "requestParameters": {
              "sourceIPAddress": "127.0.0.1"
            },
            "responseElements": {
              "x-amz-request-id": "EXAMPLE123456789",
              "x-amz-id-2": "EXAMPLE123/5678abcdefghijklambdaisawesome/mnopqrstuvwxyzABCDEFGH"
            },
            "s3": {
              "s3SchemaVersion": "1.0",
              "configurationId": "testConfigRule",
              "bucket": {
                "name": "my-s3-bucket",
                "ownerIdentity": {
                  "principalId": "EXAMPLE"
                },
                "arn": "arn:aws:s3:::my-s3-bucket"
              },
              "object": {
                "key": "HappyFace.jpg",
                "size": 1024,
                "eTag": "0123456789abcdef0123456789abcdef",
                "sequencer": "0A1B2C3D4E5F678901"
              }
            }
          }
        ]
      }
      ```

   1. Choose **Create**\.

1. To invoke the function with your test event, under **Code source**, choose **Test**\.

   The **Execution results** tab displays the response, function logs, and request ID, similar to the following:

   ```
   Response
   "image/jpeg"
   
   Function Logs
   START RequestId: 12b3cae7-5f4e-415e-93e6-416b8f8b66e6 Version: $LATEST
   2021-02-18T21:40:59.280Z	12b3cae7-5f4e-415e-93e6-416b8f8b66e6	INFO	INPUT BUCKET AND KEY:  { Bucket: 'my-s3-bucket', Key: 'HappyFace.jpg' }
   2021-02-18T21:41:00.215Z	12b3cae7-5f4e-415e-93e6-416b8f8b66e6	INFO	CONTENT TYPE: image/jpeg
   END RequestId: 12b3cae7-5f4e-415e-93e6-416b8f8b66e6
   REPORT RequestId: 12b3cae7-5f4e-415e-93e6-416b8f8b66e6	Duration: 976.25 ms	Billed Duration: 977 ms	Memory Size: 128 MB	Max Memory Used: 90 MB	Init Duration: 430.47 ms        
   
   Request ID
   12b3cae7-5f4e-415e-93e6-416b8f8b66e6
   ```

## Test with the S3 trigger<a name="with-s3-example-configure-event-source-test-end-to-end"></a>

Invoke your function when you upload a file to the Amazon S3 source bucket\.

**To test the Lambda function using the S3 trigger**

1. On the [Buckets page](https://console.aws.amazon.com/s3/home) of the Amazon S3 console, choose the name of the source bucket that you created earlier\.

1. On the **Upload** page, upload a few \.jpg or \.png image files to the bucket\.

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Choose the name of your function \(**my\-s3\-function**\)\.

1. To verify that the function ran once for each file that you uploaded, choose the **Monitor** tab\. This page shows graphs for the metrics that Lambda sends to CloudWatch\. The count in the **Invocations** graph should match the number of files that you uploaded to the Amazon S3 bucket\.   
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/metrics-functions-list.png)

   For more information on these graphs, see [Monitoring functions on the Lambda console](monitoring-functions-access-metrics.md)\.

1. \(Optional\) To view the logs in the CloudWatch console, choose **View logs in CloudWatch**\. Choose a log stream to view the logs output for one of the function invocations\.

## Clean up your resources<a name="cleanup"></a>

You can now delete the resources that you created for this tutorial, unless you want to retain them\. By deleting AWS resources that you're no longer using, you prevent unnecessary charges to your AWS account\.

**To delete the Lambda function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Select the function that you created\.

1. Choose **Actions**, then choose **Delete**\.

1. Choose **Delete**\.

**To delete the IAM policy**

1. Open the [Policies page](https://console.aws.amazon.com/iam/home#/policies) of the AWS Identity and Access Management \(IAM\) console\.

1. Select the policy that Lambda created for you\. The policy name begins with **AWSLambdaS3ExecutionRole\-**\.

1. Choose **Policy actions**, **Delete**\.

1. Choose **Delete**\.

**To delete the execution role**

1. Open the [Roles page](https://console.aws.amazon.com/iam/home#/roles) of the IAM console\.

1. Select the execution role that you created\.

1. Choose **Delete role**\.

1. Choose **Yes, delete**\.

**To delete the S3 bucket**

1. Open the [Amazon S3 console\.](https://console.aws.amazon.com/s3/home#)

1. Select the bucket you created\.

1. Choose **Delete**\.

1. Enter the name of the bucket in the text box\.

1. Choose **Confirm**\.

## Next steps<a name="next-steps"></a>

Try the more advanced tutorial\. In this tutorial, the S3 trigger invokes a function to [create a thumbnail image](with-s3-tutorial.md) for each image file that is uploaded to your S3 bucket\. This tutorial requires a moderate level of AWS and Lambda domain knowledge\. You use the AWS Command Line Interface \(AWS CLI\) to create resources, and you create a \.zip file archive deployment package for your function and its dependencies\.
