# Tutorial: Using an Amazon S3 trigger to create thumbnail images<a name="with-s3-tutorial"></a>

In this tutorial, you create a Lambda function and configure a trigger for Amazon Simple Storage Service \(Amazon S3\)\. Amazon S3 invokes the `CreateThumbnail` function for each image file that is uploaded to an S3 bucket\. The function reads the image object from the source S3 bucket and creates a thumbnail image to save in a target S3 bucket\.

**Note**  
This tutorial requires a moderate level of AWS and Lambda domain knowledge\. We recommend that you first try [Tutorial: Using an Amazon S3 trigger to invoke a Lambda function](with-s3-example.md)\.

 In this tutorial, you use the AWS Command Line Interface \(AWS CLI\) to create the following AWS resources:

**Lambda resources**
+ A Lambda function\. You can choose Node\.js or Python for the function code\.
+ A \.zip file archive deployment package for the function\.
+ An access policy that grants Amazon S3 permission to invoke the function\.

**AWS Identity and Access Management \(IAM\) resources**
+ An execution role with an associated permissions policy to grant permissions that your function needs\.

**Amazon S3 resources**
+ A source S3 bucket with a notification configuration that invokes the function\.
+ A target S3 bucket where the function saves the resized images\.

**Topics**
+ [Prerequisites](#with-s3-tutorial-prepare)
+ [Step 1\. Create S3 buckets and upload a sample object](#with-s3-tutorial-prepare-create-buckets)
+ [Step 2\. Create the IAM policy](#with-s3-tutorial-create-policy)
+ [Step 3\. Create the execution role](#with-s3-tutorial-create-execution-role)
+ [Step 4\. Create the deployment package](#with-s3-tutorial-create-function-package)
+ [Step 5\. Create the Lambda function](#with-s3-tutorial-create-function-createfunction)
+ [Step 6\. Test the Lambda function](#s3-tutorial-events-adminuser-create-test-function-upload-zip-test-manual-invoke)
+ [Step 7\. Configure Amazon S3 to publish events](#with-s3-tutorial-configure-event-source)
+ [Step 8\. Test using the Amazon S3 trigger](#with-s3-tutorial-configure-event-source-test-end-to-end)
+ [Step 9\. Clean up your resources](#s3-tutorial-cleanup)

## Prerequisites<a name="with-s3-tutorial-prepare"></a>
+ [Install the AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html) and [configure it with your AWS credentials](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html)\.
+ Install the language support tools and a package manager for the language that you want to use \(Node\.js or Python\)\. For suggested tools, see [Code authoring tools](lambda-settingup.md#lambda-settingup-author)\.

## Step 1\. Create S3 buckets and upload a sample object<a name="with-s3-tutorial-prepare-create-buckets"></a>

Follow these steps to create S3 buckets and upload an object\.

1. Open the [Amazon S3 console](https://console.aws.amazon.com/s3)\.

1. [Create two S3 buckets](https://docs.aws.amazon.com/AmazonS3/latest/userguide/create-bucket-overview.html)\. The target bucket must be named ***source*\-resized**, where *source* is the name of the source bucket\. For example, a source bucket named `sourcebucket` and a target bucket named `sourcebucket-resized`\.
**Note**  
Make sure that you create the buckets in the same AWS Region that you plan to use for the Lambda function\.

1. In the source bucket, [upload](https://docs.aws.amazon.com/AmazonS3/latest/userguide/upload-objects.html) a \.jpg or \.png object, for example, `HappyFace.jpg`\.

   You must create this sample object before you test your Lambda function\. When you test the function manually in step 6, you pass sample event data to the function that specifies the source bucket name and image file name\.

## Step 2\. Create the IAM policy<a name="with-s3-tutorial-create-policy"></a>

Create an IAM policy that defines the permissions for the Lambda function\. The function must have permissions to:
+ Get the object from the source S3 bucket\.
+ Put the resized object into the target S3 bucket\.
+ Write logs to Amazon CloudWatch Logs\.

**To create an IAM policy**

1. Open the [Policies page](https://console.aws.amazon.com/iam/home#/policies) in the IAM console\.

1. Choose **Create policy**\.

1. Choose the **JSON** tab, and then paste the following policy\. Be sure to replace *sourcebucket* with the name of the source bucket that you created previously\.

   ```
   {
       "Version": "2012-10-17",
       "Statement": [
           {
               "Effect": "Allow",
               "Action": [
                   "logs:PutLogEvents",
                   "logs:CreateLogGroup",
                   "logs:CreateLogStream"
               ],
               "Resource": "arn:aws:logs:*:*:*"
           },
           {
               "Effect": "Allow",
               "Action": [
                   "s3:GetObject"
               ],
               "Resource": "arn:aws:s3:::sourcebucket/*"
           },
           {
               "Effect": "Allow",
               "Action": [
                   "s3:PutObject"
               ],
               "Resource": "arn:aws:s3:::sourcebucket-resized/*"
           }
       ]
   }
   ```

1. Choose **Next: Tags**\.

1. Choose **Next: Review**\.

1. Under **Review policy**, for **Name**, enter **AWSLambdaS3Policy**\.

1. Choose **Create policy**\.

## Step 3\. Create the execution role<a name="with-s3-tutorial-create-execution-role"></a>

Create the [execution role](lambda-intro-execution-role.md) that gives your Lambda function permission to access AWS resources\.

**To create an execution role**

1. Open the [Roles page](https://console.aws.amazon.com/iam/home#/roles) in the IAM console\.

1. Choose **Create role**\.

1. Create a role with the following properties:
   + **Trusted entity** – **Lambda**
   + **Permissions policy** – **AWSLambdaS3Policy**
   + **Role name** – **lambda\-s3\-role**

## Step 4\. Create the deployment package<a name="with-s3-tutorial-create-function-package"></a>

The deployment package is a [\.zip file archive](gettingstarted-package.md#gettingstarted-package-zip) containing your Lambda function code and its dependencies\.

------
#### [ Node\.js ]

1. Open a command line terminal or shell in a Linux environment\. Ensure that the Node\.js version in your local environment matches the Node\.js version of your function\.

1. Create a a directory named `lambda-s3`\.

   ```
   mkdir lambda-s3
   ```

1. Save the function code as `index.js`\.

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
                   
   // Use the sharp module to resize the image and save in a buffer.
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

1. In the `lambda-s3` directory, create a `node_modules` directory\.

   ```
   mkdir node_modules
   cd node_modules
   ```

1. In the `node_modules` directory, install the sharp library with npm\.

   ```
   npm install sharp
   ```

   After this step, you have the following directory structure:

   ```
   lambda-s3
   |- index.js
   |- /node_modules/...
   └ /node_modules/sharp
   ```

1. Return to the `lambda-s3` directory\.

   ```
   cd lambda-s3
   ```

1. Create a deployment package with the function code and its dependencies\. Set the \-r \(recursive\) option for the zip command to compress the subfolders\. 

   ```
   zip -r function.zip .
   ```

------
#### [ Python ]

The Python deployment package for this tutorial uses the [Pillow \(PIL\)](https://pypi.org/project/Pillow/) library\. You can't use the AWS CLI to upload a deployment package that contains a C or C\+\+ library, such as Pillow\. Instead, use the AWS Serverless Application Model \(AWS SAM\) CLI [sam build](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-using-build.html) command with the `--use-container` option to create your deployment package\. Using the AWS SAM CLI with this option creates a Docker container with a Lambda\-like environment that is compatible with Lambda\.

**Prerequisites**
+ [AWS SAM CLI version 1\.39 or later](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html)

**To create a deployment package using the AWS SAM**

1. Open a command prompt and create a `lambda-s3` project directory\.

   ```
   mkdir lambda-s3
   ```

1. Navigate to the `lambda-s3` project directory\.

   ```
   cd lambda-s3
   ```

1. Copy the contents of the following sample Python code and save it in a new file named `lambda_function.py`:

   ```
   import boto3
   import os
   import sys
   import uuid
   from urllib.parse import unquote_plus
   from PIL import Image
   import PIL.Image
               
   s3_client = boto3.client('s3')
               
   def resize_image(image_path, resized_path):
     with Image.open(image_path) as image:
       image.thumbnail(tuple(x / 2 for x in image.size))
       image.save(resized_path)
               
   def lambda_handler(event, context):
     for record in event['Records']:
       bucket = record['s3']['bucket']['name']
       key = unquote_plus(record['s3']['object']['key'])
       tmpkey = key.replace('/', '')
       download_path = '/tmp/{}{}'.format(uuid.uuid4(), tmpkey)
       upload_path = '/tmp/resized-{}'.format(tmpkey)
       s3_client.download_file(bucket, key, download_path)
       resize_image(download_path, upload_path)
       s3_client.upload_file(upload_path, '{}-resized'.format(bucket), 'resized-{}'.format(key))
   ```

1. Install the Pillow library to a new `package` directory\.

   ```
   pip install --target ./package pillow
   ```

1. In the `lambda-s3` directory, create a new file called `template.yaml`\. This is [the AWS SAM template\.](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-specification-template-anatomy.html)

   ```
   AWSTemplateFormatVersion: '2010-09-09'
   Transform: AWS::Serverless-2016-10-31
   Resources:
     CreateThumbnail:
       Type: AWS::Serverless::Function
       Properties:
         Handler: lambda_function.lambda_handler
         Runtime: python3.9
         Timeout: 10
         Policies: AWSLambdaExecute
         Events:
           CreateThumbnailEvent:
             Type: S3
             Properties:
               Bucket: !Ref SrcBucket
               Events: s3:ObjectCreated:*
   
     SrcBucket:
       Type: AWS::S3::Bucket
   ```

1. Create a file called `requirements.txt` and add the following content\. This is the manifest file that specifies your dependencies\. If you installed a different version of Pillow, change the version number\.

   ```
   Pillow == 9.2.0
   ```

1. Build the deployment package\. The `--use-container` flag is required\. This flag locally compiles your functions in a Docker container that behaves like a Lambda environment, so they're in the right format when you deploy them\.

   ```
   sam build --use-container
   ```

------

## Step 5\. Create the Lambda function<a name="with-s3-tutorial-create-function-createfunction"></a>

------
#### [ Node\.js ]

```
aws lambda create-function --function-name CreateThumbnail \
--zip-file fileb://function.zip --handler index.handler --runtime nodejs16.x \
--timeout 10 --memory-size 1024 \
--role arn:aws:iam::123456789012:role/lambda-s3-role
```

For the `role` parameter, replace *123456789012* with your [AWS account ID](https://docs.aws.amazon.com/general/latest/gr/acct-identifiers.html)\.

The create\-function command specifies the function handler as `index.handler`\. This handler name reflects the function name as `handler`, and the name of the file where the handler code is stored as `index.js`\. For more information, see [AWS Lambda function handler in Node\.js](nodejs-handler.md)\. The command specifies a runtime of `nodejs16.x`\. For more information, see [Lambda runtimes](lambda-runtimes.md)\.

------
#### [ Python ]

Run the following AWS SAM CLI command to deploy the package and create the Lambda function\. Follow the on\-screen prompts\. To accept the default options provided in the interactive experience, respond with `Enter`\.

```
sam deploy --guided
```

------

The function configuration includes a 10\-second timeout value\. Depending on the size of objects that you upload, you might need to increase the timeout value using the following AWS CLI command:

```
aws lambda update-function-configuration --function-name CreateThumbnail --timeout 30
```

## Step 6\. Test the Lambda function<a name="s3-tutorial-events-adminuser-create-test-function-upload-zip-test-manual-invoke"></a>

Invoke the Lambda function manually using sample Amazon S3 event data\.

**To test the Lambda function**

1. In the project directory that you created earlier, save the following Amazon S3 sample event data in a file named `inputFile.txt`\. Be sure to replace the following values:
   + *us\-west\-2* – The AWS Region where you created the Amazon S3 bucket and the Lambda function\.
   + *sourcebucket* – The Amazon S3 source bucket that you created in step 1\.
   + *HappyFace\.jpg* – The object key of the \.jpg or \.png image that you uploaded to the source bucket\.

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

1. Invoke the function with the following invoke command\. Note that the command requests asynchronous execution \(`--invocation-type Event`\)\. Optionally, you can invoke the function synchronously by specifying `RequestResponse` as the `invocation-type` parameter value\.

   ```
   aws lambda invoke --function-name CreateThumbnail \
     --cli-binary-format raw-in-base64-out \
     --invocation-type Event \
     --payload file://inputFile.txt outputfile.txt
   ```
   + The cli\-binary\-format option is required if you're using AWS CLI version 2\. To make this the default setting, run `aws configure set cli-binary-format raw-in-base64-out`\. For more information, see [AWS CLI supported global command line options](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-options.html#cli-configure-options-list)\.
   + If you get the error "Error parsing parameter '\-\-payload': Unable to load paramfile file://inputFile\.txt", make sure that you're in the directory where the `inputFile.txt` is saved\.

1. Verify that the thumbnail is created in the target S3 bucket\.

## Step 7\. Configure Amazon S3 to publish events<a name="with-s3-tutorial-configure-event-source"></a>

Complete the configuration so that Amazon S3 can publish object\-created events to Lambda and invoke your Lambda function\. In this step, you do the following:
+ Add permissions to the function access policy to allow Amazon S3 to invoke the function\.
+ Add a notification configuration to your source S3 bucket\. In the notification configuration, you provide the following:
  + The event type for which you want Amazon S3 to publish events\. For this tutorial, specify the `s3:ObjectCreated:*` event type so that Amazon S3 publishes events when objects are created\.
  + The function to invoke\.

**To add permissions to the function policy**

1. Run the following add\-permission command to grant Amazon S3 service principal \(`s3.amazonaws.com`\) permissions to perform the `lambda:InvokeFunction` action\. Note that Amazon S3 is granted permission to invoke the function only if the following conditions are met:
   + An object\-created event is detected on a specific S3 bucket\.
   + The S3 bucket is owned by your AWS account\. If you delete a bucket, it is possible for another AWS account to create a bucket with the same Amazon Resource Name \(ARN\)\.

   ```
   aws lambda add-permission --function-name CreateThumbnail --principal s3.amazonaws.com \
   --statement-id s3invoke --action "lambda:InvokeFunction" \
   --source-arn arn:aws:s3:::sourcebucket \
   --source-account account-id
   ```

1. Verify the function's access policy by running the get\-policy command\.

   ```
   aws lambda get-policy --function-name CreateThumbnail
   ```

To have Amazon S3 publish object\-created events to Lambda, add a notification configuration on the source S3 bucket\.

**Important**  
This procedure configures the S3 bucket to invoke your function every time that an object is created in the bucket\. Be sure to configure this option only on the source bucket\. Do not have your function create objects in the source bucket, or your function could be [invoked continuously in a loop](with-s3.md#services-s3-runaway)\.

**To configure notifications**

1. Open the [Amazon S3 console](https://console.aws.amazon.com/s3)\.

1. Choose the name of the source S3 bucket\.

1. Choose the **Properties** tab\.

1. Under **Event notifications**, choose **Create event notification** to configure a notification with the following settings:
   + **Event name** – **lambda\-trigger**
   + **Event types** – **All object create events**
   + **Destination** – **Lambda function**
   + **Lambda function** – **CreateThumbnail**\.

For more information on event configuration, see [Enabling and configuring event notifications using the Amazon S3 console](https://docs.aws.amazon.com/AmazonS3/latest/userguide/enable-event-notifications.html) in the *Amazon Simple Storage Service User Guide*\.

## Step 8\. Test using the Amazon S3 trigger<a name="with-s3-tutorial-configure-event-source-test-end-to-end"></a>

Test the setup as follows:

1. Upload \.jpg or \.png objects to the source S3 bucket using the [Amazon S3 console](https://console.aws.amazon.com/s3)\.

1. Verify for each image object that a thumbnail is created in the target S3 bucket using the `CreateThumbnail` Lambda function\.

1. View logs in the [CloudWatch console](https://console.aws.amazon.com/cloudwatch)\.

## Step 9\. Clean up your resources<a name="s3-tutorial-cleanup"></a>

You can now delete the resources that you created for this tutorial, unless you want to retain them\. By deleting AWS resources that you're no longer using, you prevent unnecessary charges to your AWS account\.

**To delete the Lambda function**

1. Open the [Functions page](https://console.aws.amazon.com/lambda/home#/functions) of the Lambda console\.

1. Select the function that you created\.

1. Choose **Actions**, then choose **Delete**\.

1. Choose **Delete**\.

**To delete the policy that you created**

1. Open the [Policies page](https://console.aws.amazon.com/iam/home#/policies) of the IAM console\.

1. Select the policy that you created \(**AWSLambdaS3Policy**\)\.

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