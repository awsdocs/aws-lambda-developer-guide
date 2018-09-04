# Lambda Execution Environment and Available Libraries<a name="current-supported-versions"></a>

The underlying AWS Lambda execution environment is based on the following:
+ Public Amazon Linux AMI version \(AMI name: amzn\-ami\-hvm\-2017\.03\.1\.20170812\-x86\_64\-gp2\) which can be accessed [ here](https://console.aws.amazon.com/ec2/v2/home#Images:visibility=public-images;search=amzn-ami-hvm-2017.03.1.20170812-x86_64-gp2)\.  

  For information about using an AMI, see [Amazon Machine Images \(AMI\)](http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/AMIs.html) in the *Amazon EC2 User Guide for Linux Instances*\.
+  Linux kernel version – 4\.9\.119\-44\.140\.amzn1\.x86\_64 

 If you are using any native binaries in your code, make sure they are compiled against the package and library versions from this AMI and kernel\. Note that only 64\-bit binaries are supported on AWS Lambda and that the specific CPU make and model is subject to continual updates\.

AWS Lambda supports the following runtime versions:
+ Node\.js – v8\.10, v6\.10 or v4\.3
**Note**  
v4\.3 has been deprecated\. For more information on AWS Lambda's policy on deprecated runtimes, see [Runtime Support Policy](runtime-support-policy.md)\.
+ Java – Java 8
+ Python – Python 3\.6 and 2\.7
+ \.NET Core – \.NET Core 1\.0\.1, \.NET Core 2\.0 and \.NET Core 2\.1
+ Go – Go 1\.x

**Note**  
Not all runtimes are available on the Public Amazon Linux AMI version or its yum repositories\. You may need to download and install them manually from their respective public sites\.

The following libraries are available in the AWS Lambda execution environment, regardless of the supported runtime you use, so you don't need to include them:
+  AWS SDK – [AWS SDK for JavaScript](http://docs.aws.amazon.com/AWSJavaScriptSDK/guide/) version 2\.249\.1 
+ AWS SDK for Python 2\.7 \(Boto 3\) version 3\-1\.7\.30 botocore\-1\.10\.30

  AWS SDK for Python 3\.6 \(Boto 3\) version 3\-1\.7\.30 botocore\-1\.10\.30
+ Amazon Linux build of `java-1.8.0-openjdk` for Java\.

For an example of using the `boto` libraries in your Lambda function, see [Accessing Resources from a Lambda Function](accessing-resources.md)\.

## Environment Variables Available to Lambda Functions<a name="lambda-environment-variables"></a>

The following is a list of environment variables that are part of the AWS Lambda execution environment and made available to Lambda functions\. The table below indicates which ones are reserved by AWS Lambda and cannot be changed as well as which ones you can set when creating your Lambda function\. For more information on using environment variables with your Lambda function, see [Environment Variables](env_variables.md)\. 


**Lambda Environment Variables**  

| Key | Reserved | Value | 
| --- | --- | --- | 
| LAMBDA\_TASK\_ROOT | Yes | Contains the path to your Lambda function code\. | 
| AWS\_EXECUTION\_ENV | Yes | The environment variable is set to one of the following options, depending on the runtime of the Lambda function: [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/current-supported-versions.html)  | 
| LAMBDA\_RUNTIME\_DIR | Yes | Restricted to Lambda runtime\-related artifacts\. For example the aws\-sdk for Node\.js and boto3 for Python can be found under this path\. | 
| AWS\_REGION | Yes | The AWS region where the Lambda function is executed\. | 
| AWS\_DEFAULT\_REGION | Yes | The AWS region where the Lambda function is executed\. | 
| AWS\_LAMBDA\_LOG\_GROUP\_NAME | Yes | The name of Amazon CloudWatch Logs group where log streams containing your Lambda function logs are created\. | 
| AWS\_LAMBDA\_LOG\_STREAM\_NAME | Yes | The Amazon CloudWatch Logs streams containing your Lambda function logs\. | 
| AWS\_LAMBDA\_FUNCTION\_NAME | Yes | The name of the Lambda function\. | 
| AWS\_LAMBDA\_FUNCTION\_MEMORY\_SIZE | Yes | The size of the Lambda function in MB\. | 
| AWS\_LAMBDA\_FUNCTION\_VERSION | Yes | The version of the Lambda function\. | 
| AWS\_ACCESS\_KEY AWS\_ACCESS\_KEY\_ID AWS\_SECRET\_KEY AWS\_SECRET\_ACCESS\_KEY AWS\_SESSION\_TOKEN AWS\_SECURITY\_TOKEN  | Yes | The security credentials required to execute the Lambda function, depending on which runtime is used\. Different runtimes use a subset of these keys\. They are generated via an IAM execution role specified for the function\. | 
| PATH | No | Contains /usr/local/bin, /usr/bin or /bin for running executables\. | 
| LANG | No | Set to en\_US\.UTF\-8\. This is the Locale of the runtime\.  | 
| LD\_LIBRARY\_PATH | No | Contains /lib64, /usr/lib64, LAMBDA\_TASK\_ROOT, LAMBDA\_TASK\_ROOT/lib\. Used to store helper libraries and function code\. | 
| NODE\_PATH | No | Set for the Node\.js runtime\. It contains LAMBDA\_RUNTIME\_DIR, LAMBDA\_RUNTIME\_DIR/node\_modules, LAMBDA\_TASK\_ROOT\. | 
| PYTHONPATH | No | Set for the Python runtime\. It contains LAMBDA\_RUNTIME\_DIR\. | 
| TZ | Yes | The current local time\. Defaults to [UTC](https://www.timeanddate.com/worldclock/timezone/utc)\. | 