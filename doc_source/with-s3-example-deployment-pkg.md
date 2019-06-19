# Sample Amazon Simple Storage Service Function Code<a name="with-s3-example-deployment-pkg"></a>

Sample code is available for the following languages\.

**Topics**
+ [Node\.js 8](#with-s3-example-deployment-pkg-nodejs)
+ [Java 8](#with-s3-example-deployment-pkg-java)
+ [Python 3](#with-s3-example-deployment-pkg-python)

## Node\.js 8<a name="with-s3-example-deployment-pkg-nodejs"></a>

The following example code receives an Amazon S3 event input and processes the message that it contains\. It resizes an image in the source bucket and saves the output to the target bucket\.

**Example index\.js**  

```
var async = require('async');
var AWS = require('aws-sdk');
var gm = require('gm')
            .subClass({ imageMagick: true }); // Enable ImageMagick integration.
var util = require('util');

var MAX_WIDTH  = 100;
var MAX_HEIGHT = 100;

var s3 = new AWS.S3();
 
exports.handler = function(event, context, callback) {
    // Read options from the event.
    console.log("Reading options from event:\n", util.inspect(event, {depth: 5}));
    var srcBucket = event.Records[0].s3.bucket.name;
    // Object key may have spaces or unicode non-ASCII characters.
    var srcKey    =
    decodeURIComponent(event.Records[0].s3.object.key.replace(/\+/g, " "));  
    var dstBucket = srcBucket + "resized";
    var dstKey    = "resized-" + srcKey;

    // Sanity check: validate that source and destination are different buckets.
    if (srcBucket == dstBucket) {
        callback("Source and destination buckets are the same.");
        return;
    }

    // Infer the image type.
    var typeMatch = srcKey.match(/\.([^.]*)$/);
    if (!typeMatch) {
        callback("Could not determine the image type.");
        return;
    }
    var imageType = typeMatch[1].toLowerCase();
    if (imageType != "jpg" && imageType != "png") {
        callback(`Unsupported image type: ${imageType}`);
        return;
    }

    // Download the image from S3, transform, and upload to a different S3 bucket.
    async.waterfall([
        function download(next) {
            // Download the image from S3 into a buffer.
            s3.getObject({
                    Bucket: srcBucket,
                    Key: srcKey
                },
                next);
            },
        function transform(response, next) {
            gm(response.Body).size(function(err, size) {
                // Infer the scaling factor to avoid stretching the image unnaturally.
                var scalingFactor = Math.min(
                    MAX_WIDTH / size.width,
                    MAX_HEIGHT / size.height
                );
                var width  = scalingFactor * size.width;
                var height = scalingFactor * size.height;

                // Transform the image buffer in memory.
                this.resize(width, height)
                    .toBuffer(imageType, function(err, buffer) {
                        if (err) {
                            next(err);
                        } else {
                            next(null, response.ContentType, buffer);
                        }
                    });
            });
        },
        function upload(contentType, data, next) {
            // Stream the transformed image to a different S3 bucket.
            s3.putObject({
                    Bucket: dstBucket,
                    Key: dstKey,
                    Body: data,
                    ContentType: contentType
                },
                next);
            }
        ], function (err) {
            if (err) {
                console.error(
                    'Unable to resize ' + srcBucket + '/' + srcKey +
                    ' and upload to ' + dstBucket + '/' + dstKey +
                    ' due to an error: ' + err
                );
            } else {
                console.log(
                    'Successfully resized ' + srcBucket + '/' + srcKey +
                    ' and uploaded to ' + dstBucket + '/' + dstKey
                );
            }

            callback(null, "message");
        }
    );
};
```

The deployment package is a \.zip file containing your Lambda function code and dependencies\. 

**To create a deployment package**

1. Create a folder \(`examplefolder`\), and then create a subfolder \(`node_modules`\)\. 

1. Install the Node\.js platform\. For more information, see the [Node\.js](https://nodejs.org/) website\.

1. Install dependencies\. The code examples use the following libraries:
   + AWS SDK for JavaScript in Node\.js
   + gm, GraphicsMagick for node\.js
   + Async utility module

   The AWS Lambda runtime already has the AWS SDK for JavaScript in Node\.js, so you only need to install the other libraries\. Open a command prompt, navigate to the `examplefolder`, and install the libraries using the `npm` command, which is part of Node\.js\.

   ```
   $ npm install async gm
   ```

1. Save the sample code to a file named index\.js\.

1. Review the preceding code and note the following:
   + The function knows the source bucket name and the key name of the object from the event data it receives as parameters\. If the object is a \.jpg, the code creates a thumbnail and saves it to the target bucket\. 
   + The code assumes that the destination bucket exists and its name is a concatenation of the source bucket name followed by the string `resized`\. For example, if the source bucket identified in the event data is `examplebucket`, the code assumes you have an `examplebucketresized` destination bucket\.
   + For the thumbnail it creates, the code derives its key name as the concatenation of the string `resized-` followed by the source object key name\. For example, if the source object key is `sample.jpg`, the code creates a thumbnail object that has the key `resized-sample.jpg`\.

1. Save the file as `index.js` in `examplefolder`\. After you complete this step, you will have the following folder structure:

   ```
   index.js
   /node_modules/gm
   /node_modules/async
   ```

1. Zip the index\.js file and the node\_modules folder as `CreateThumbnail.zip`\.

## Java 8<a name="with-s3-example-deployment-pkg-java"></a>

The following is example Java code that reads incoming Amazon S3 events and creates a thumbnail\. Note that it implements the `RequestHandler` interface provided in the `aws-lambda-java-core` library\. Therefore, at the time you create a Lambda function you specify the class as the handler \(that is, `example.S3EventProcessorCreateThumbnail`\)\. For more information about using interfaces to provide a handler, see [Leveraging Predefined Interfaces for Creating Handler \(Java\)](java-handler-using-predefined-interfaces.md)\.

The `S3Event` type that the handler uses as the input type is one of the predefined classes in the `aws-lambda-java-events`  library that provides methods for you to easily read information from the incoming Amazon S3 event\. The handler returns a string as output\.

**Example S3EventProcessorCreateThumbnail\.java**  

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
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

public class S3EventProcessorCreateThumbnail implements
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
            String srcKey = record.getS3().getObject().getKey()
                    .replace('+', ' ');
            srcKey = URLDecoder.decode(srcKey, "UTF-8");

            String dstBucket = srcBucket + "resized";
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
            AmazonS3 s3Client = new AmazonS3Client();
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
            s3Client.putObject(dstBucket, dstKey, is, meta);
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
+ `aws-java-sdk-s3`

Build the code with the Lambda library dependencies to create a deployment package\. For instructions, see [AWS Lambda Deployment Package in Java](lambda-java-how-to-create-deployment-package.md)\.

## Python 3<a name="with-s3-example-deployment-pkg-python"></a>

The following example code receives an Amazon S3 event input and processes the message that it contains\. It resizes an image in the source bucket and saves the output to the target bucket\.

**Example CreateThumbnail\.py**  

```
import boto3
import os
import sys
import uuid
from PIL import Image
import PIL.Image
     
s3_client = boto3.client('s3')
     
def resize_image(image_path, resized_path):
    with Image.open(image_path) as image:
        image.thumbnail(tuple(x / 2 for x in image.size))
        image.save(resized_path)
     
def handler(event, context):
    for record in event['Records']:
        bucket = record['s3']['bucket']['name']
        key = record['s3']['object']['key'] 
        download_path = '/tmp/{}{}'.format(uuid.uuid4(), key)
        upload_path = '/tmp/resized-{}'.format(key)
        
        s3_client.download_file(bucket, key, download_path)
        resize_image(download_path, upload_path)
        s3_client.upload_file(upload_path, '{}resized'.format(bucket), key)
```

**To create a deployment package**

1. Copy the sample code into a file named `CreateThumbnail.py`\.

1. Create a virtual environment\.

   `$ virtualenv ~/shrink_venv`

   `$ source ~/shrink_venv/bin/activate `

1. Install libraries in the virtual environment

   `$ pip install Pillow`

   `$ pip install boto3`

1. Add the contents of `lib` and `lib64` site\-packages to your \.zip file\.

   `$ cd $VIRTUAL_ENV/lib/python3.7/site-packages`

   `$ zip -r ~/CreateThumbnail.zip . `

1. Add your python code to the \.zip file

   `$ cd ~`

   `$ zip -g CreateThumbnail.zip CreateThumbnail.py `