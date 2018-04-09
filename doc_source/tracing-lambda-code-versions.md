# Tracking Lambda Function Code Versions<a name="tracing-lambda-code-versions"></a>

When you create or update a Lambda function and deploy it to an Amazon S3 bucket, Lambda will save the original Amazon S3 location and version of the code as metadata on your function\.

This metadata could be used to help with code auditing, compliance programs, or for integration with build and deployment systems\.
+ x\-amz\-meta\-lambda\-source\-arn: arn:aws:s3:::*BucketName/ObjectName*
+ x\-amz\-meta\-lambda\-source\-version\-id: *original\-s3\-version\-id*

This metadata is returned as HTTP response headers when making a `GET` request to the pre\-signed code `URI` for your function\. For example: 

```
$ aws lambda get-function --function-name myfunction
{
 "Code": {
        "RepositoryType": "S3",
        "Location": "https://awslambda-region-tasks.s3.region.amazonaws.com/..."
  }
  ...

$ curl -I -X GET "https://awslambda-region-tasks.s3.region.amazonaws.com/..."
x-amz-meta-lambda-source-arn: arn:aws:s3:::MyCodeBucket/MyCodePackage-1.2.zip
x-amz-version-id: original-s3-version-id
...
```

The original Amazon S3 location and version is also logged in CloudTrail for all [CreateFunction](API_CreateFunction.md) and [UpdateFunctionCode](API_UpdateFunctionCode.md) requests\. For example: 

```
   "requestParameters": {
        "functionName": "myfunction",
        "code": {
            "s3Key": " MyCodePackage-1.2.zip",
            "s3Bucket": "MyCodeBucket ",
            "s3Version" "original-s3-version-id"
        }
```