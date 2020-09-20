# Tutorial: Using AWS Lambda with Amazon S3<a name="with-s3-example"></a>

Suppose you want to create a thumbnail for each image file that is uploaded to a bucket\. You can create a Lambda function \(`CreateThumbnail`\) that Amazon S3 can invoke when objects are created\. Then, the Lambda function can read the image object from the source bucket and create a thumbnail image target bucket\.

Upon completing this tutorial, you will have the following Amazon S3, Lambda, and IAM resources in your account: 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/s3-admin-iser-walkthrough-10.png)

**Lambda resources**
+ A Lambda function\.
+ An access policy associated with your Lambda function that grants Amazon S3 permission to invoke the Lambda function\.

**IAM resources**
+ An execution role that grants permissions that your Lambda function needs through the permissions policy associated with this role\.

**Amazon S3 resources**
+ A source bucket with a notification configuration that invokes the Lambda function\.
+ A target bucket where the function saves resized images\.

## Prerequisites<a name="with-s3-prepare"></a>

This tutorial assumes that you have some knowledge of basic Lambda operations and the Lambda console\. If you haven't already, follow the instructions in [Getting started with AWS Lambda](getting-started.md) to create your first Lambda function\.

To follow the procedures in this guide, you will need a command line terminal or shell to run commands\. Commands are shown in listings preceded by a prompt symbol \($\) and the name of the current directory, when appropriate:

```
~/lambda-project$ this is a command
this is output
```

For long commands, an escape character \(`\`\) is used to split a command over multiple lines\.

On Linux and macOS, use your preferred shell and package manager\. On Windows 10, you can [install the Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows\-integrated version of Ubuntu and Bash\.

Install npm to manage the function's dependencies\.

The tutorial uses AWS CLI commands to create and invoke the Lambda function\. Install the [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html) and [configure it with your AWS credentials](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html) 

## Create the execution role<a name="with-s3-create-execution-role"></a>

Create the [execution role](lambda-intro-execution-role.md) that gives your function permission to access AWS resources\.

**To create an execution role**

1. Open the [roles page](https://console.aws.amazon.com/iam/home#/roles) in the IAM console\.

1. Choose **Create role**\.

1. Create a role with the following properties\.
   + **Trusted entity** – **AWS Lambda**\.
   + **Permissions** – **AWSLambdaExecute**\.
   + **Role name** – **lambda\-s3\-role**\.

The **AWSLambdaExecute** policy has the permissions that the function needs to manage objects in Amazon S3 and write logs to CloudWatch Logs\.

## Create buckets and upload a sample object<a name="with-s3-example-prepare-create-buckets"></a>

Follow the steps to create buckets and upload an object\.

1. Open the [Amazon S3 console](https://console.aws.amazon.com/s3)\.

1. Create two buckets\. The target bucket name must be *source* followed by **\-resized**, where *source* is the name of the bucket you want to use for the source\. For example, `mybucket` and `mybucket-resized`\.

1. In the source bucket, upload a \.jpg object, `HappyFace.jpg`\. 

   When you invoke the Lambda function manually before you connect to Amazon S3, you pass sample event data to the function that specifies the source bucket and `HappyFace.jpg` as the newly created object so you need to create this sample object first\.

## Create the function<a name="with-s3-example-create-function"></a>

The following example code receives an Amazon S3 event input and processes the message that it contains\. It resizes an image in the source bucket and saves the output to the target bucket\.

**Note**  
For sample code in other languages, see [Sample Amazon S3 function code](with-s3-example-deployment-pkg.md)\.

**Example index\.js**  

```
// dependencies
const AWS = require('aws-sdk');
const util = require('util');
const sharp = require('sharp');

// get reference to S3 client
const s3 = new AWS.S3();

exports.handler = async (event, context, callback) => {

    // Read options from the event parameter.
    console.log("Reading options from event:\n", util.inspect(event, {depth: 5}));
    const srcBucket = event.Records[0].s3.bucket.name;
    // Object key may have spaces or unicode non-ASCII characters.
    const srcKey    = decodeURIComponent(event.Records[0].s3.object.key.replace(/\+/g, " "));
    const dstBucket = srcBucket + "-resized";
    const dstKey    = "resized-" + srcKey;

    // Infer the image type from the file suffix.
    const typeMatch = srcKey.match(/\.([^.]*)$/);
    if (!typeMatch) {
        console.log("Could not determine the image type.");
        return;
    }

    // Check that the image type is supported  
    const imageType = typeMatch[1].toLowerCase();
    if (imageType != "jpg" && imageType != "png") {
        console.log(`Unsupported image type: ${imageType}`);
        return;
    }

    // Download the image from the S3 source bucket. 

    try {
        const params = {
            Bucket: srcBucket,
            Key: srcKey
        };
        var origimage = await s3.getObject(params).promise();

    } catch (error) {
        console.log(error);
        return;
    }  

    // set thumbnail width. Resize will set the height automatically to maintain aspect ratio.
    const width  = 200;

    // Use the Sharp module to resize the image and save in a buffer.
    try { 
        var buffer = await sharp(origimage.Body).resize(width).toBuffer();
            
    } catch (error) {
        console.log(error);
        return;
    } 

    // Upload the thumbnail image to the destination bucket
    try {
        const destparams = {
            Bucket: dstBucket,
            Key: dstKey,
            Body: buffer,
            ContentType: "image"
        };

        const putResult = await s3.putObject(destparams).promise(); 
        
    } catch (error) {
        console.log(error);
        return;
    } 
        
    console.log('Successfully resized ' + srcBucket + '/' + srcKey +
        ' and uploaded to ' + dstBucket + '/' + dstKey); 
};
```

Review the preceding code and note the following:
+ The function knows the source bucket name and the key name of the object from the event data it receives as parameters\. If the object is a \.jpg or a \.png, the code creates a thumbnail and saves it to the target bucket\. 
+ The code assumes that the destination bucket exists and its name is a concatenation of the source bucket name followed by the string `-resized`\. For example, if the source bucket identified in the event data is `examplebucket`, the code assumes you have an `examplebucket-resized` destination bucket\.
+ For the thumbnail it creates, the code derives its key name as the concatenation of the string `resized-` followed by the source object key name\. For example, if the source object key is `sample.jpg`, the code creates a thumbnail object that has the key `resized-sample.jpg`\.

The deployment package is a \.zip file containing your Lambda function code and dependencies\. 

**To create a deployment package**

1. Open a command line terminal or shell in a Linux environment\. Ensure that the Node\.js version in your local environment matches the Node\.js version of your function\. 

1. Save the function code as `index.js` in a folder named `lambda-s3`\.

1. Install the Sharp library with npm\. For Linux, use the following command\.

   ```
   lambda-s3$ npm install sharp
   ```

   For macOS, use the following command\.

   ```
   lambda-s3$ npm install --arch=x64 --platform=linux --target=12.13.0  sharp
   ```

   After you complete this step, you will have the following folder structure:

   ```
   lambda-s3
   |- index.js
   |- /node_modules/sharp
   └ /node_modules/...
   ```

1. Create a deployment package with the function code and dependencies\.

   ```
   lambda-s3$ zip -r function.zip .
   ```

**To create the function**
+ Create a Lambda function with the `create-function` command\.

  ```
  $ aws lambda create-function --function-name CreateThumbnail \
  --zip-file fileb://function.zip --handler index.handler --runtime nodejs12.x \
  --timeout 10 --memory-size 1024 \
  --role arn:aws:iam::123456789012:role/lambda-s3-role
  ```

For the role parameter, replace the number sequence with your AWS account ID\. The preceding example command specifies a 10\-second timeout value as the function configuration\. Depending on the size of objects you upload, you might need to increase the timeout value using the following AWS CLI command\.

```
$ aws lambda update-function-configuration --function-name CreateThumbnail --timeout 30
```

## Test the Lambda function<a name="walkthrough-s3-events-adminuser-create-test-function-upload-zip-test-manual-invoke"></a>

In this step, you invoke the Lambda function manually using sample Amazon S3 event data\.

**To test the Lambda function**

1. Save the following Amazon S3 sample event data in a file and save it as `inputFile.txt`\. You need to update the JSON by providing your *sourcebucket* name and a \.jpg object key\.

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

1. Run the following Lambda CLI `invoke` command to invoke the function\. Note that the command requests asynchronous execution\. You can optionally invoke it synchronously by specifying `RequestResponse` as the `invocation-type` parameter value\.

   ```
   $ aws lambda invoke --function-name CreateThumbnail --invocation-type Event \
   --payload file://inputFile.txt outputfile.txt
   ```

1. Verify that the thumbnail was created in the target bucket\.

## Configure Amazon S3 to publish events<a name="with-s3-example-configure-event-source"></a>

In this step, you add the remaining configuration so that Amazon S3 can publish object\-created events to AWS Lambda and invoke your Lambda function\. You do the following in this step:
+ Add permissions to the Lambda function access policy to allow Amazon S3 to invoke the function\.
+ Add notification configuration to your source bucket\. In the notification configuration, you provide the following:
  + Event type for which you want Amazon S3 to publish events\. For this tutorial, you specify the `s3:ObjectCreated:*` event type so that Amazon S3 publishes events when objects are created\.
  + Lambda function to invoke\.

**To add permissions to the function policy**

1. Run the following Lambda CLI `add-permission` command to grant Amazon S3 service principal \(`s3.amazonaws.com`\) permissions to perform the `lambda:InvokeFunction` action\. Note that permission is granted to Amazon S3 to invoke the function only if the following conditions are met:
   + An object\-created event is detected on a specific bucket\.
   + The bucket is owned by your account\. If you delete a bucket, it is possible for another account to create a bucket with the same ARN\.

   ```
   $ aws lambda add-permission --function-name CreateThumbnail --principal s3.amazonaws.com \
   --statement-id s3invoke --action "lambda:InvokeFunction" \
   --source-arn arn:aws:s3:::sourcebucket \
   --source-account account-id
   ```

1. Verify the function's access policy by running the AWS CLI `get-policy` command\.

   ```
   $ aws lambda get-policy --function-name CreateThumbnail
   ```

Add notification configuration on the source bucket to request Amazon S3 to publish object\-created events to Lambda\.

**Important**  
This procedure configures the bucket to invoke your function every time an object is created in it\. Ensure that you configure this option only on the source bucket and do not create objects in the source bucket from the function that is triggered\. Otherwise, your function could cause itself to be [invoked continuously in a loop](with-s3.md#services-s3-runaway)\.

**To configure notifications**

1. Open the [Amazon S3 console](https://console.aws.amazon.com/s3)\.

1. Choose the source bucket\.

1. Choose **Properties**\.

1. Under **Events**, configure a notification with the following settings\.
   + **Name** – **lambda\-trigger**\.
   + **Events** – **All object create events**\.
   + **Send to** – **Lambda function**\.
   + **Lambda** – **CreateThumbnail**\.

For more information on event configuration, see [Enabling event notifications](https://docs.aws.amazon.com/AmazonS3/latest/user-guide/enable-event-notifications.html) in the *Amazon Simple Storage Service Console User Guide*\.

## Test the setup<a name="with-s3-example-configure-event-source-test-end-to-end"></a>

Now you can test the setup as follows:

1. Upload \.jpg or \.png objects to the source bucket using the Amazon S3 console\.

1. Verify that the thumbnail was created in the target bucket using the `CreateThumbnail` function\.

1. View logs in the CloudWatch console\. 

## Clean up your resources<a name="cleanup"></a>

You can now delete the resources that you created for this tutorial, unless you want to retain them\. By deleting AWS resources that you are no longer using, you prevent unnecessary charges to your AWS account\.

**To delete the Lambda function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Select the function that you created\.

1. Choose **Actions**, **Delete**\.

1. Choose **Delete**\.

**To delete the execution role**

1. Open the [Roles page](https://console.aws.amazon.com/iam/home#/roles) of the IAM console\.

1. Select the execution role that you created\.

1. Choose **Delete role**\.

1. Choose **Yes, delete**\.

**To delete the S3 buckets**

1. Open the [Amazon S3 console\.](https://console.aws.amazon.com/s3/home#)

1. Select the source bucket you created\.

1. Choose **Delete**\.

1. Enter the name of the source bucket in the text box\.

1. Choose **Confirm**\.