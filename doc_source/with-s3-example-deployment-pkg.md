# Sample Amazon S3 function code<a name="with-s3-example-deployment-pkg"></a>

Sample code is available for the following languages\.

**Topics**
+ [Node\.js 12\.x](#with-s3-example-deployment-pkg-nodejs)
+ [Java 11](#with-s3-example-deployment-pkg-java)
+ [Python 3](#with-s3-example-deployment-pkg-python)

## Node\.js 12\.x<a name="with-s3-example-deployment-pkg-nodejs"></a>

The following example code receives an Amazon S3 event input and processes the message that it contains\. It resizes an image in the source bucket and saves the output to the target bucket\.

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

The deployment package is a \.zip file containing your Lambda function code and dependencies\. 

**To create a deployment package**

1. Create a folder \(`examplefolder`\), and then create a subfolder \(`node_modules`\)\. 

1. Install dependencies\. The code examples use the following libraries:
   + AWS SDK for JavaScript in Node\.js
   + Sharp for node\.js

   The AWS Lambda runtime already has the AWS SDK for JavaScript in Node\.js, so you only need to install the Sharp library\. Open a command prompt, navigate to the `examplefolder`, and install the libraries using the `npm` command, which is part of Node\.js\. For Linux, use the following command\.

   ```
   $ npm install sharp
   ```

   For macOS, use the following command\.

   ```
   $ npm install --arch=x64 --platform=linux --target=12.13.0 sharp
   ```

1. Save the sample code to a file named index\.js\.

1. Review the preceding code and note the following:
   + The function knows the source bucket name and the key name of the object from the event data it receives as parameters\. If the object is a \.jpg, the code creates a thumbnail and saves it to the target bucket\. 
   + The code assumes that the destination bucket exists and its name is a concatenation of the source bucket name followed by the string `-resized`\. For example, if the source bucket identified in the event data is `examplebucket`, the code assumes you have an `examplebucket-resized` destination bucket\.
   + For the thumbnail it creates, the code derives its key name as the concatenation of the string `resized-` followed by the source object key name\. For example, if the source object key is `sample.jpg`, the code creates a thumbnail object that has the key `resized-sample.jpg`\.

1. Save the file as `index.js` in `examplefolder`\. After you complete this step, you will have the following folder structure:

   ```
   index.js
   /node_modules/sharp
   ```

1. Zip the index\.js file and the node\_modules folder as `CreateThumbnail.zip`\.

## Java 11<a name="with-s3-example-deployment-pkg-java"></a>

The following is example Java code that reads incoming Amazon S3 events and creates a thumbnail\. Note that it implements the `RequestHandler` interface provided in the `aws-lambda-java-core` library\. Therefore, at the time you create a Lambda function you specify the class as the handler \(that is, `example.handler`\)\. For more information about using interfaces to provide a handler, see [Handler interfaces](java-handler.md#java-handler-interfaces)\.

The `S3Event` type that the handler uses as the input type is one of the predefined classes in the `aws-lambda-java-events`  library that provides methods for you to easily read information from the incoming Amazon S3 event\. The handler returns a string as output\.

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

Amazon S3 invokes your Lambda function using the `Event` invocation type, where AWS Lambda executes the code asynchronously\. What you return does not matter\. However, in this case we are implementing an interface that requires us to specify a return type, so in this example the handler uses `String` as the return type\. 

**Dependencies**
+ `aws-lambda-java-core`
+ `aws-lambda-java-events`
+ `aws-java-sdk`

Build the code with the Lambda library dependencies to create a deployment package\. For instructions, see [AWS Lambda deployment package in Java](java-package.md)\.

## Python 3<a name="with-s3-example-deployment-pkg-python"></a>

The following example code receives an Amazon S3 event input and processes the message that it contains\. It resizes an image in the source bucket and saves the output to the target bucket\.

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

**Note**  
The image library used by this code must be installed in a Linux environment in order to create a working deployment package\.

**To create a deployment package**

1. Copy the sample code into a file named `lambda_function.py`\.

1. Create a virtual environment\.

   ```
   s3-python$ virtualenv v-env
   s3-python$ source v-env/bin/activate
   ```

1. Install libraries in the virtual environment

   ```
   (v-env) s3-python$ pip install Pillow boto3
   ```

1. Create a deployment package with the contents of the installed libraries\.

   ```
   (v-env) s3-python$ cd $VIRTUAL_ENV/lib/python3.8/site-packages
   (v-env) python-s3/v-env/lib/python3.8/site-packages$ zip -r9 ${OLDPWD}/function.zip .
   ```

1. Add the handler code to the deployment package and deactivate the virtual environment\.

   ```
   (v-env) python-s3/v-env/lib/python3.8/site-packages$  cd ${OLDPWD}
   (v-env) python-s3$ zip -g function.zip lambda_function.py
     adding: lambda_function.py (deflated 55%)
   (v-env) python-s3$ deactivate
   ```