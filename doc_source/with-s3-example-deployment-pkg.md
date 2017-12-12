# Step 2\.1: Create a Deployment Package<a name="with-s3-example-deployment-pkg"></a>

From the **Filter View** list, choose the language you want to use for your Lambda function\. The appropriate section appears with code and specific instructions for creating a deployment package\.

## Node\.js<a name="with-s3-example-deployment-pkg-nodejs"></a>

The deployment package is a \.zip file containing your Lambda function code and dependencies\. 

1. Create a folder \(`examplefolder`\), and then create a subfolder \(`node_modules`\)\. 

1. Install the Node\.js platform\. For more information, see the [Node\.js](https://nodejs.org/) website\.

1. Install dependencies\. The code examples use the following libraries:

   + AWS SDK for JavaScript in Node\.js

   + gm, GraphicsMagick for node\.js

   + Async utility module

   The AWS Lambda runtime already has the AWS SDK for JavaScript in Node\.js, so you only need to install the other libraries\. Open a command prompt, navigate to the `examplefolder`, and install the libraries using the `npm` command, which is part of Node\.js\.

   ```
   npm install async gm
   ```

1. Open a text editor, and then copy the following code\.

   ```
   // dependencies
   var async = require('async');
   var AWS = require('aws-sdk');
   var gm = require('gm')
               .subClass({ imageMagick: true }); // Enable ImageMagick integration.
   var util = require('util');
   
   // constants
   var MAX_WIDTH  = 100;
   var MAX_HEIGHT = 100;
   
   // get reference to S3 client 
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
       var imageType = typeMatch[1];
       if (imageType != "jpg" && imageType != "png") {
           callback('Unsupported image type: ${imageType}');
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
**Note**  
The code sample is compliant with the Node\.js runtimes v6\.10 or v4\.3\. For more information, see [Programming Model \(Node\.js\)](programming-model.md)

1. Review the preceding code and note the following:

   + The function knows the source bucket name and the key name of the object from the event data it receives as parameters\. If the object is a \.jpg, the code creates a thumbnail and saves it to the target bucket\. 

   + The code assumes that the destination bucket exists and its name is a concatenation of the source bucket name followed by the string `resized`\. For example, if the source bucket identified in the event data is `examplebucket`, the code assumes you have an `examplebucketresized` destination bucket\.

   + For the thumbnail it creates, the code derives its key name as the concatenation of the string `resized-` followed by the source object key name\. For example, if the source object key is `sample.jpg`, the code creates a thumbnail object that has the key `resized-sample.jpg`\.

1. Save the file as `CreateThumbnail.js` in `examplefolder`\. After you complete this step, you will have the following folder structure:

   ```
   CreateThumbnail.js
   /node_modules/gm
   /node_modules/async
   ```

1. Zip the CreateThumbnail\.js file and the node\_modules folder as `CreateThumbnail.zip`\.

   This is your Lambda function deployment package\. 

### Next Step<a name="with-s3-example-deployment-pkg-node-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-s3-example-create-iam-role.md) 

## Java<a name="with-s3-example-deployment-pkg-java"></a>

The following is example Java code that reads incoming Amazon S3 events and creates a thumbnail\. Note that it implements the `RequestHandler` interface provided in the `aws-lambda-java-core` library\. Therefore, at the time you create a Lambda function you specify the class as the handler \(that is, `example.S3EventProcessorCreateThumbnail`\)\. For more information about using interfaces to provide a handler, see [Leveraging Predefined Interfaces for Creating Handler \(Java\)](java-handler-using-predefined-interfaces.md)\.

The `S3Event` type that the handler uses as the input type is one of the predefined classes in the `aws-lambda-java-events`  library that provides methods for you to easily read information from the incoming Amazon S3 event\. The handler returns a string as output\.

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
            // If you want higher quality algorithms, check this link:
            // https://today.java.net/pub/a/today/2007/04/03/perils-of-image-getscaledinstance.html
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

Using the preceding code \(in a file named `S3EventProcessorCreateThumbnail.java`\), create a deployment package\. Make sure that you add the following dependencies: 

+ `aws-lambda-java-core `

+ `aws-lambda-java-events` 

These can be found at [aws\-lambda\-java\-libs](https://github.com/aws/aws-lambda-java-libs)\.

For more information, see [Programming Model for Authoring Lambda Functions in Java](java-programming-model.md)\.

Your deployment package can be a \.zip file or a standalone \.jar\. You can use any build and packaging tool you are familiar with to create a deployment package\. For examples of how to use the Maven build tool to create a standalone \.jar, see [Creating a \.jar Deployment Package Using Maven without any IDE \(Java\)](java-create-jar-pkg-maven-no-ide.md) and [Creating a \.jar Deployment Package Using Maven and Eclipse IDE \(Java\)](java-create-jar-pkg-maven-and-eclipse.md)\. For an example of how to use the Gradle build tool to create a \.zip file, see [Creating a \.zip Deployment Package \(Java\)](create-deployment-pkg-zip-java.md)\.

After you verify that your deployment package is created, go to the next step to create an IAM role \(execution role\)\. You specify this role at the time you create your Lambda function\. 

### Next Step<a name="with-s3-example-deployment-pkg-java-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-s3-example-create-iam-role.md) 

## Python<a name="with-s3-example-deployment-pkg-python"></a>

In this section, you create an example Python function and install dependencies\. The code sample is compliant with Python runtime versions 3\.6 or 2\.7\. The steps assume the 3\.6 runtime but you can use either one\. 

1. Open a text editor, and copy the following code\. The code uploads the resized image to a different bucket with the same image name, as shown following:

   `source-bucket/image.png` \-> `source-bucketresized/image.png` 
**Note**  
The `from __future__` statement enables you to write code that is compatible with Python 2 or 3\. If you are using runtime version 3\.6, it is not necessary to include it\.

   ```
   from __future__ import print_function
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

1. Save the file as `CreateThumbnail.py`\.

1. If your source code is on a local host, copy it over\.

   `scp -i key.pem /path/to/my_code.py ec2-user@public-ip-address:~/CreateThumbnail.py`

1. Connect to a 64\-bit Amazon Linux instance via SSH\.

   `ssh -i key.pem ec2-user@public-ip-address `

1. Install Python 3\.6 and virtualenv using the following steps:

   1. `sudo yum install -y gcc zlib zlib-devel openssl openssl-devel`

   1. `wget [https://www\.python\.org/ftp/python/3\.6\.1/Python\-3\.6\.1\.tgz](https://www.python.org/ftp/python/3.6.1/Python-3.6.1.tgz)`

   1. `tar -xzvf Python-3.6.1.tgz`

   1. `cd Python-3.6.1 && ./configure && make`

   1. `sudo make install`

   1. `sudo /usr/local/bin/pip3 install virtualenv`

1. Choose the virtual environment that was installed via pip3

   `/usr/local/bin/virtualenv ~/shrink_venv`

   `source ~/shrink_venv/bin/activate `

1. Install libraries in the virtual environment

   `pip install Pillow `

   `pip install boto3 `
**Note**  
AWS Lambda includes the AWS SDK for Python \(Boto 3\), so you don't need to include it in your deployment package, but you can optionally include it for local testing\.

1. Add the contents of `lib` and `lib64` site\-packages to your \.zip file\. Note that the following steps assume you used Python runtime version 3\.6\. If you used version 2\.7 you will need to update accordingly\.

   `cd $VIRTUAL_ENV/lib/python3.6/site-packages`

   `zip -r9 ~/CreateThumbnail.zip * `
**Note**  
To include all hidden files, use the following option: `zip -r9 ~/CreateThumbnail.zip .`

1. Add your python code to the \.zip file

   cd \~

   `zip -g CreateThumbnail.zip CreateThumbnail.py `

### Next Step<a name="with-s3-example-deployment-pkg-python-next-step"></a>

 [Step 2\.2: Create the Execution Role \(IAM Role\)](with-s3-example-create-iam-role.md) 