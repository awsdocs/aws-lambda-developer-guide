# Transforming S3 Objects with S3 Object Lambda<a name="services-s3-object-lambda"></a>

With S3 Object Lambda you can add your own code to Amazon S3 GET, HEAD, and LIST requests to modify and process data before it is returned to an application\. You can use custom code to modify the data returned by standard S3 GET, HEAD, or LIST requests to filter rows, dynamically resize images, redact confidential data, and more\. Powered by AWS Lambda functions, your code runs on infrastructure that is fully managed by AWS, eliminating the need to create and store derivative copies of your data or to run proxies, all with no changes required to applications\.

For more information, see [Transforming objects with S3 Object Lambda](https://docs.aws.amazon.com/AmazonS3/latest/userguide/transforming-objects.html)\.

**Tutorials**
+ [Transforming data for your application with Amazon S3 Object Lambda](https://docs.aws.amazon.com/AmazonS3/latest/userguide/tutorial-s3-object-lambda-uppercase.html)
+ [Detecting and redacting PII data with Amazon S3 Object Lambda and Amazon Comprehend](https://docs.aws.amazon.com/AmazonS3/latest/userguide/tutorial-s3-object-lambda-redact-pii.html)