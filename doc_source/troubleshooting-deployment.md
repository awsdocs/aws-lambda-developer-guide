# Troubleshoot Deployment Issues in AWS Lambda<a name="troubleshooting-deployment"></a>

When you update your function, Lambda deploys the change by launching new instances of the function with the updated code or settings\. Deployment errors prevent the new version from being used and can arise from issues with your deployment package, code, permissions, or tools\.

When you deploy updates to your function directly with the Lambda API or with a client such as the AWS CLI, you can see errors from Lambda directly in the output\. If you use services like AWS CloudFormation, AWS CodeDeploy, or AWS CodePipeline, look for the response from Lambda in that services logs or event stream\.

**Error:** *EACCES: permission denied, open '/var/task/index\.js'*

**Error:** *cannot load such file \-\- function*

**Error:** *\[Errno 13\] Permission denied: '/var/task/function\.py'*

The Lambda runtime needs permission to read the files in your deployment package\. You can use the `chmod` command to change the file mode\. The following example commands make all files and folders in the current directory readable by any user\.

```
my-function$ chmod 644 $(find . -type f)
my-function$ chmod 755 $(find . -type d)
```

**Error:** *An error occurred \(RequestEntityTooLargeException\) when calling the UpdateFunctionCode operation*

When you upload a deployment package or layer archive directly to Lambda, the size of the ZIP file is limited to 50 MB\. To upload a larger file, store it in Amazon S3 and use the [S3Bucket and S3Key](API_UpdateFunctionCode.md#SSS-UpdateFunctionCode-request-S3Bucket) parameters\.

**Note**  
When you upload a file directly with the AWS CLI, AWS SDK, or otherwise, the binary ZIP file is converted to Base64, increasing its size by about 30%\. To allow for this, and the size of other parameters in the request, the actual request size limit that Lambda applies is larger\. Due to this, the 50 MB limit is approximate\.

**Error:** *Error occurred while GetObject\. S3 Error Code: PermanentRedirect\. S3 Error Message: The bucket is in this region: us\-east\-2\. Please use this region to retry the request*

When you upload a function's deployment package from an Amazon S3 bucket, the bucket must be in the same Region as the function\. This issue can occur when you specify an Amazon S3 object in a call to [UpdateFunctionCode](API_UpdateFunctionCode.md), or use the package and deploy commands in the AWS CLI or AWS SAM CLI\. Create a deployment artifact bucket for each Region where you develop applications\.