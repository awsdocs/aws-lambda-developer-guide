# Tutorial: Using an Amazon S3 trigger to create thumbnail images<a name="with-s3-tutorial"></a>

In this tutorial, you create a Lambda function and configure a trigger for Amazon Simple Storage Service \(Amazon S3\)\. Amazon S3 invokes the `CreateThumbnail` function for each image file that is uploaded to an S3 bucket\. The function reads the image object from the source S3 bucket and creates a thumbnail image to save in a target S3 bucket\.

**Note**  
This tutorial requires a moderate level of AWS and Lambda domain knowledge\. We recommend that you first try [Tutorial: Using an Amazon S3 trigger to invoke a Lambda function](with-s3-example.md)\.

 In this tutorial, you use the AWS Command Line Interface \(AWS CLI\) to create the following AWS resources:

**Lambda resources**
+ A Lambda function\. You can choose Node\.js, Python, or Java for the function code\.
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
+ [Step 4\. Create the function code](#with-s3-tutorial-create-function-code)
+ [Step 5\. Create the deployment package](#with-s3-tutorial-create-function-package)
+ [Step 6\. Create the Lambda function](#with-s3-tutorial-create-function-createfunction)
+ [Step 7\. Test the Lambda function](#s3-tutorial-events-adminuser-create-test-function-upload-zip-test-manual-invoke)
+ [Step 8\. Configure Amazon S3 to publish events](#with-s3-tutorial-configure-event-source)
+ [Step 9\. Test using the S3 trigger](#with-s3-tutorial-configure-event-source-test-end-to-end)
+ [Step 10\. Clean up your resources](#s3-tutorial-cleanup)

## Prerequisites<a name="with-s3-tutorial-prepare"></a>
+ AWS account

  To use Lambda and other AWS services, you need an AWS account\. If you do not have an account, visit [aws\.amazon\.com](https://aws.amazon.com/) and choose **Create an AWS Account**\. For instructions, see [How do I create and activate a new AWS account?](http://aws.amazon.com/premiumsupport/knowledge-center/create-and-activate-aws-account/)
+ Command line

  To complete the following steps, you need a command line terminal or shell to run commands\. Commands and the expected output are listed in separate blocks:

  ```
  aws --version
  ```

  You should see the following output:

  ```
  aws-cli/2.0.57 Python/3.7.4 Darwin/19.6.0 exe/x86_64
  ```

  For long commands, an escape character \(`\`\) is used to split a command over multiple lines\.

  On Linux and macOS, use your preferred shell and package manager\. On Windows 10, you can [install the Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows\-integrated version of Ubuntu and Bash\.
+ AWS CLI

  In this tutorial, you use AWS CLI commands to create and invoke the Lambda function\. [Install the AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html) and [configure it with your AWS credentials](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html)\.
+ Language tools

  Install the language support tools and a package manager for the language that you want to use: Node\.js, Python, or Java\. For suggested tools, see [Code authoring tools](lambda-settingup.md#lambda-settingup-author)\.

## Step 1\. Create S3 buckets and upload a sample object<a name="with-s3-tutorial-prepare-create-buckets"></a>

Follow these steps to create S3 buckets and upload an object\.

1. Open the [Amazon S3 console](https://console.aws.amazon.com/s3)\.

1. [Create two S3 buckets](https://docs.aws.amazon.com/AmazonS3/latest/userguide/create-bucket-overview.html)\. The target bucket must be named ***source*\-resized**, where *source* is the name of the source bucket\. For example, a source bucket named `mybucket` and a target bucket named `mybucket-resized`\.

1. In the source bucket, [upload](https://docs.aws.amazon.com/AmazonS3/latest/userguide/upload-objects.html) a \.jpg object, for example, `HappyFace.jpg`\.

   You must create this sample object before you test your Lambda function\. When you test the function manually using the Lambda invoke command, you pass sample event data to the function that specifies the source bucket name and `HappyFace.jpg` as the newly created object\.

## Step 2\. Create the IAM policy<a name="with-s3-tutorial-create-policy"></a>

Create an IAM policy that defines the permissions for the Lambda function\. The function must have permissions to:
+ Get the object from the source S3 bucket\.
+ Put the resized object into the target S3 bucket\.
+ Write logs to Amazon CloudWatch Logs\.

**To create an IAM policy**

1. Open the [Policies page](https://console.aws.amazon.com/iam/home#/policies) in the IAM console\.

1. Choose **Create policy**\.

1. Choose the **JSON** tab, and then paste the following policy\. Be sure to replace *mybucket* with the name of the source bucket that you created previously\.

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
               "Resource": "arn:aws:s3:::mybucket/*"
           },
           {
               "Effect": "Allow",
               "Action": [
                   "s3:PutObject"
               ],
               "Resource": "arn:aws:s3:::mybucket-resized/*"
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

## Step 4\. Create the function code<a name="with-s3-tutorial-create-function-code"></a>

In the following code examples, the Amazon S3 event contains the source S3 bucket name and the object key name\. If the object is a \.jpg or a \.png image file, it reads the image from the source bucket, generates a thumbnail image, and then saves the thumbnail to the target S3 bucket\.

Note the following:
+ The code assumes that the target bucket exists and that its name is a concatenation of the source bucket name and `-resized`\.
+ For each thumbnail file created, the Lambda function code derives the object key name as a concatenation of `resized-` and the source object key name\. For example, if the source object key name is `sample.jpg`, the code creates a thumbnail object that has the key `resized-sample.jpg`\.

------
#### [ Node\.js ]

Copy the following code example into a file named `index.js`\.

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

------
#### [ Python ]

Copy the following code example into a file named `lambda_function.py`\.

**Example lambda\_function\.py**  

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
      s3_client.upload_file(upload_path, '{}-resized'.format(bucket), key)
```

------
#### [ Java ]

The Java code implements the `RequestHandler` interface provided in the `aws-lambda-java-core` library\. When you create a Lambda function, you specify the class as the handler \(in this code example, `example.handler`\)\. For more information about using interfaces to provide a handler, see [Handler interfaces](java-handler.md#java-handler-interfaces)\.

The handler uses `S3Event` as the input type, which provides convenient methods for your function code to read information from the incoming Amazon S3 event\. Amazon S3 invokes your Lambda function asynchronously\. Because you are implementing an interface that requires you to specify a return type, the handler uses `String` as the return type\.

Copy the following code example into a file named `Handler.java`\.

**Example Handler\.java**  

```
package example;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class Handler implements
      RequestHandler<S3Event, String> {
  private static final float MAX_WIDTH = 100;
  private static final float MAX_HEIGHT = 100;
  private final String JPG_TYPE = (String) "jpg";
  private final String JPG_MIME = (String) "image/jpeg";
  private final String PNG_TYPE = (String) "png";
  private final String PNG_MIME = (String) "image/png";

  public String handleRequest(S3Event s3event, Context context) {
      try {
          S3EventNotificationRecord record = s3event.getRecords().get(0);

          String srcBucket = record.getS3().getBucket().getName();

          // Object key may have spaces or unicode non-ASCII characters.
          String srcKey = record.getS3().getObject().getUrlDecodedKey();

          String dstBucket = srcBucket + "-resized";
          String dstKey = "resized-" + srcKey;

          // Sanity check: validate that source and destination are different
          // buckets.
          if (srcBucket.equals(dstBucket)) {
              System.out
                      .println("Destination bucket must not match source bucket.");
              return "";
          }

          // Infer the image type.
          Matcher matcher = Pattern.compile(".*\\.([^\\.]*)").matcher(srcKey);
          if (!matcher.matches()) {
              System.out.println("Unable to infer image type for key "
                      + srcKey);
              return "";
          }
          String imageType = matcher.group(1);
          if (!(JPG_TYPE.equals(imageType)) && !(PNG_TYPE.equals(imageType))) {
              System.out.println("Skipping non-image " + srcKey);
              return "";
          }

          // Download the image from S3 into a stream
          AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
          S3Object s3Object = s3Client.getObject(new GetObjectRequest(
                  srcBucket, srcKey));
          InputStream objectData = s3Object.getObjectContent();

          // Read the source image
          BufferedImage srcImage = ImageIO.read(objectData);
          int srcHeight = srcImage.getHeight();
          int srcWidth = srcImage.getWidth();
          // Infer the scaling factor to avoid stretching the image
          // unnaturally
          float scalingFactor = Math.min(MAX_WIDTH / srcWidth, MAX_HEIGHT
                  / srcHeight);
          int width = (int) (scalingFactor * srcWidth);
          int height = (int) (scalingFactor * srcHeight);

          BufferedImage resizedImage = new BufferedImage(width, height,
                  BufferedImage.TYPE_INT_RGB);
          Graphics2D g = resizedImage.createGraphics();
          // Fill with white before applying semi-transparent (alpha) images
          g.setPaint(Color.white);
          g.fillRect(0, 0, width, height);
          // Simple bilinear resize
          g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                  RenderingHints.VALUE_INTERPOLATION_BILINEAR);
          g.drawImage(srcImage, 0, 0, width, height, null);
          g.dispose();

          // Re-encode image to target format
          ByteArrayOutputStream os = new ByteArrayOutputStream();
          ImageIO.write(resizedImage, imageType, os);
          InputStream is = new ByteArrayInputStream(os.toByteArray());
          // Set Content-Length and Content-Type
          ObjectMetadata meta = new ObjectMetadata();
          meta.setContentLength(os.size());
          if (JPG_TYPE.equals(imageType)) {
              meta.setContentType(JPG_MIME);
          }
          if (PNG_TYPE.equals(imageType)) {
              meta.setContentType(PNG_MIME);
          }

          // Uploading to S3 destination bucket
          System.out.println("Writing to: " + dstBucket + "/" + dstKey);
          try {
              s3Client.putObject(dstBucket, dstKey, is, meta);
          }
          catch(AmazonServiceException e)
          {
              System.err.println(e.getErrorMessage());
              System.exit(1);
          }
          System.out.println("Successfully resized " + srcBucket + "/"
                  + srcKey + " and uploaded to " + dstBucket + "/" + dstKey);
          return "Ok";
      } catch (IOException e) {
          throw new RuntimeException(e);
      }
  }
}
```

------

## Step 5\. Create the deployment package<a name="with-s3-tutorial-create-function-package"></a>

The deployment package is a [\.zip file archive](gettingstarted-package.md#gettingstarted-package-zip) containing your Lambda function code and its dependencies\.

------
#### [ Node\.js ]

The sample function must include the sharp module in the deployment package\. 

**To create a deployment package**

1. Open a command line terminal or shell in a Linux environment\. Ensure that the Node\.js version in your local environment matches the Node\.js version of your function\.

1. Save the function code as `index.js` in a directory named `lambda-s3`\.

1. Install the sharp library with npm\. For Linux, use the following command:

   ```
   npm install sharp
   ```

   After this step, you have the following directory structure:

   ```
   lambda-s3
   |- index.js
   |- /node_modules/sharp
   └ /node_modules/...
   ```

1. Create a deployment package with the function code and its dependencies\. Set the \-r \(recursive\) option for the zip command to compress the subfolders\. 

   ```
   zip -r function.zip .
   ```

------
#### [ Python ]

**Dependencies**
+ [Pillow](https://pypi.org/project/Pillow/)

**To create a deployment package**
+ We recommend using the AWS SAM CLI [sam build](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-using-build.html) command with the `--use-container` option to create deployment packages that contain libraries written in C or C\+\+, such as the [Pillow \(PIL\)](https://pypi.org/project/Pillow/) library\.

------
#### [ Java ]

**Dependencies**
+ `aws-lambda-java-core`
+ `aws-lambda-java-events`
+ `aws-java-sdk`

**To create a deployment package**
+ Build the code with the Lambda library dependencies to create a deployment package\. For instructions, see [Deploy Java Lambda functions with \.zip or JAR file archives](java-package.md)\.

------

## Step 6\. Create the Lambda function<a name="with-s3-tutorial-create-function-createfunction"></a>

**To create the function**
+ Create a Lambda function with the create\-function command\.

------
#### [ Node\.js ]

  ```
  aws lambda create-function --function-name CreateThumbnail \
  --zip-file fileb://function.zip --handler index.handler --runtime nodejs12.x \
  --timeout 10 --memory-size 1024 \
  --role arn:aws:iam::123456789012:role/lambda-s3-role
  ```

  The cli\-binary\-format option is required if you are using AWS CLI version 2\. You can also configure this option in your [ AWS CLI config file](https://docs.aws.amazon.com/cli/latest/userguide/cliv2-migration.html#cliv2-migration-binaryparam)\.

  The create\-function command specifies the function handler as `index.handler`\. This handler name reflects the function name as `handler`, and the name of the file where the handler code is stored as `index.js`\. For more information, see [AWS Lambda function handler in Node\.js](nodejs-handler.md)\. The command specifies a runtime of `nodejs12.x`\. For more information, see [Lambda runtimes](lambda-runtimes.md)\.

------
#### [ Python ]

  ```
  aws lambda create-function --function-name CreateThumbnail \
  --zip-file fileb://function.zip --handler lambda_function.lambda_handler --runtime python3.8 \
  --timeout 10 --memory-size 1024 \
  --role arn:aws:iam::123456789012:role/lambda-s3-role
  ```

  The cli\-binary\-format option is required if you are using AWS CLI version 2\. You can also configure this option in your [ AWS CLI config file](https://docs.aws.amazon.com/cli/latest/userguide/cliv2-migration.html#cliv2-migration-binaryparam)\.

  The create\-function command specifies the function handler as `lambda_function.lambda_handler`\. This handler name reflects the function name as `lambda_handler`, and the name of the file where the handler code is stored as `lambda_function.py`\. For more information, see [Lambda function handler in Python](python-handler.md)\. The command specifies a runtime of `python3.8`\. For more information, see [Lambda runtimes](lambda-runtimes.md)\.

------
#### [ Java ]

  ```
  aws lambda create-function --function-name CreateThumbnail \
  --zip-file fileb://function.zip --handler example.handler --runtime java11 \
  --timeout 10 --memory-size 1024 \
  --role arn:aws:iam::123456789012:role/lambda-s3-role
  ```

  The cli\-binary\-format option is required if you are using AWS CLI version 2\. You can also configure this option in your [ AWS CLI config file](https://docs.aws.amazon.com/cli/latest/userguide/cliv2-migration.html#cliv2-migration-binaryparam)\.

  The create\-function command specifies the function handler as `example.handler`\. The function can use the abbreviated handler format of `package.Class` because the function implements a handler interface\. For more information, see [AWS Lambda function handler in Java](java-handler.md)\. The command specifies a runtime of `java11`\. For more information, see [Lambda runtimes](lambda-runtimes.md)\.

------

For the role parameter, replace *123456789012* with your [AWS account ID](https://docs.aws.amazon.com/general/latest/gr/acct-identifiers.html)\. The preceding example command specifies a 10\-second timeout value as the function configuration\. Depending on the size of objects that you upload, you might need to increase the timeout value using the following AWS CLI command:

```
aws lambda update-function-configuration --function-name CreateThumbnail --timeout 30
```

## Step 7\. Test the Lambda function<a name="s3-tutorial-events-adminuser-create-test-function-upload-zip-test-manual-invoke"></a>

Invoke the Lambda function manually using sample Amazon S3 event data\.

**To test the Lambda function**

1. Save the following Amazon S3 sample event data in a file named `inputFile.txt`\. Be sure to replace *sourcebucket* and *HappyFace\.jpg* with your source S3 bucket name and a \.jpg object key, respectively\.

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
   aws lambda invoke
     --function-name CreateThumbnail \
       --invocation-type Event \
         --payload file://inputFile.txt outputfile.txt
   ```

   The cli\-binary\-format option is required if you are using AWS CLI version 2\. You can also configure this option in your [ AWS CLI config file](https://docs.aws.amazon.com/cli/latest/userguide/cliv2-migration.html#cliv2-migration-binaryparam)\.

1. Verify that the thumbnail is created in the target S3 bucket\.

## Step 8\. Configure Amazon S3 to publish events<a name="with-s3-tutorial-configure-event-source"></a>

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
This procedure configures the S3 bucket to invoke your function every time that an object is created in the bucket\. Be sure to configure this option only on the source bucket\. Do not have your function create objects in the source bucket, or your function could cause itself to be [invoked continuously in a loop](with-s3.md#services-s3-runaway)\.

**To configure notifications**

1. Open the [Amazon S3 console](https://console.aws.amazon.com/s3)\.

1. Choose the name of the source S3 bucket\.

1. Choose the **Properties** tab\.

1. Under **Event notifications**, choose **Create event notification** to configure a notification with the following settings:
   + **Event name** – **lambda\-trigger**
   + **Event types** – **All object create events**
   + **Destination** – **Lambda function**
   + **Lambda function** – **CreateThumbnail**

For more information on event configuration, see [Enabling and configuring event notifications using the Amazon S3 console](https://docs.aws.amazon.com/AmazonS3/latest/userguide/enable-event-notifications.html) in the *Amazon Simple Storage Service User Guide*\.

## Step 9\. Test using the S3 trigger<a name="with-s3-tutorial-configure-event-source-test-end-to-end"></a>

Test the setup as follows:

1. Upload \.jpg or \.png objects to the source S3 bucket using the [Amazon S3 console](https://console.aws.amazon.com/s3)\.

1. Verify for each image object that a thumbnail is created in the target S3 bucket using the `CreateThumbnail` Lambda function\.

1. View logs in the [CloudWatch console](https://console.aws.amazon.com/cloudwatch)\.

## Step 10\. Clean up your resources<a name="s3-tutorial-cleanup"></a>

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