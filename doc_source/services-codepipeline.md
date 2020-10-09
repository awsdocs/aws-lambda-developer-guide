# Using AWS Lambda with AWS CodePipeline<a name="services-codepipeline"></a>

AWS CodePipeline is a service that enables you to create continuous delivery pipelines for applications that run on AWS\. You can create a pipeline to deploy your Lambda application\. You can also configure a pipeline to invoke a Lambda function to perform a task when the pipeline runs\. When you [create a Lambda application](deploying-lambda-apps.md) in the Lambda console, Lambda creates a pipeline that includes source, build, and deploy stages\.

CodePipeline invokes your function asynchronously with an event that contains details about the job\. The following example shows an event from a pipeline that invoked a function named `my-function`\.

**Example CodePipeline event**  

```
{
    "CodePipeline.job": {
        "id": "c0d76431-b0e7-xmpl-97e3-e8ee786eb6f6",
        "accountId": "123456789012",
        "data": {
            "actionConfiguration": {
                "configuration": {
                    "FunctionName": "my-function",
                    "UserParameters": "{\"KEY\": \"VALUE\"}"
                }
            },
            "inputArtifacts": [
                {
                    "name": "my-pipeline-SourceArtifact",
                    "revision": "e0c7xmpl2308ca3071aa7bab414de234ab52eea",
                    "location": {
                        "type": "S3",
                        "s3Location": {
                            "bucketName": "us-west-2-123456789012-my-pipeline",
                            "objectKey": "my-pipeline/test-api-2/TdOSFRV"
                        }
                    }
                }
            ],
            "outputArtifacts": [
                {
                    "name": "invokeOutput",
                    "revision": null,
                    "location": {
                        "type": "S3",
                        "s3Location": {
                            "bucketName": "us-west-2-123456789012-my-pipeline",
                            "objectKey": "my-pipeline/invokeOutp/D0YHsJn"
                        }
                    }
                }
            ],
            "artifactCredentials": {
                "accessKeyId": "AKIAIOSFODNN7EXAMPLE",
                "secretAccessKey": "6CGtmAa3lzWtV7a...",
                "sessionToken": "IQoJb3JpZ2luX2VjEA...",
                "expirationTime": 1575493418000
            }
        }
    }
}
```

To complete the job, the function must call the CodePipeline API to signal success or failure\. The following example Node\.js function uses the `PutJobSuccessResult` operation to signal success\. It gets the job ID for the API call from the event object\.

**Example index\.js**  

```
var AWS = require('aws-sdk')
var codepipeline = new AWS.CodePipeline()

exports.handler = async (event) => {
    console.log(JSON.stringify(event, null, 2))
    var jobId = event["CodePipeline.job"].id
    var params = {
        jobId: jobId
    }
    return codepipeline.putJobSuccessResult(params).promise()
}
```

For asynchronous invocation, Lambda queues the message and [retries](invocation-retries.md) if your function returns an error\. Configure your function with a [destination](invocation-async.md#invocation-async-destinations) to retain events that your function could not process\.

For a tutorial on how to configure a pipeline to invoke a Lambda function, see [Invoke an AWS Lambda function in a pipeline](https://docs.aws.amazon.com/codepipeline/latest/userguide/actions-invoke-lambda-function.html) in the *AWS CodePipeline User Guide*\.

You can use AWS CodePipeline to create a continuous delivery pipeline for your Lambda application\. CodePipeline combines source control, build, and deployment resources to create a pipeline that runs whenever you make a change to your application's source code\.

For an alternate method of creating a pipeline with AWS Serverless Application Model and AWS CloudFormation, watch [Automate your serverless application deployments](https://www.youtube.com/watch?v=0o3urdBeoII) on the Amazon Web Services YouTube channel\.

## Permissions<a name="services-codepipeline-permissions"></a>

To invoke a function, a CodePipeline pipeline needs permission to use the following API operations:
+ [ListFunctions](API_ListFunctions.md)
+ [InvokeFunction](API_Invoke.md)

The default [pipeline service role](https://docs.aws.amazon.com/codepipeline/latest/userguide/how-to-custom-role.html) includes these permissions\.

To complete a job, the function needs the following permissions in its [execution role](lambda-intro-execution-role.md)\.
+ `codepipeline:PutJobSuccessResult`
+ `codepipeline:PutJobFailureResult`

These permissions are included in the [AWSCodePipelineCustomActionAccess](https://console.aws.amazon.com/iam/home#/policies/arn:aws:iam::aws:policy/AWSCodePipelineCustomActionAccess) managed policy\.