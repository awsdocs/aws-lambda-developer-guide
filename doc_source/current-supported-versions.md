# Lambda Execution Environment and Available Libraries<a name="current-supported-versions"></a>

The underlying AWS Lambda execution environment includes the following software and libraries\.
+ Operating system – Amazon Linux
+ AMI – [amzn\-ami\-hvm\-2017\.03\.1\.20170812\-x86\_64\-gp2](https://console.aws.amazon.com/ec2/v2/home#Images:visibility=public-images;search=amzn-ami-hvm-2017.03.1.20170812-x86_64-gp2)
+ Linux kernel – 4\.14\.62\-84\.118\.amzn2\.x86\_64
+ AWS SDK for JavaScript – 2\.290\.0
+ SDK for Python \(Boto 3\) – 3\-1\.7\.74 botocore\-1\.10\.74

The execution environment uses NTP to synchronize the system clock\. If you're using any native binaries in your code, make sure that they're compiled against the package and library versions from this AMI and kernel\. Only 64\-bit binaries are supported, and that the specific CPU make and model is subject to change\.

AWS Lambda supports the following runtimes\.
+ Node\.js – Node\.js 6\.10, Node\.js 8\.10
+ Java – Java 8 \(java\-1\.8\.0\-openjdk\)
+ Python – Python 2\.7, Python 3\.6
+ C\# – \.NET Core 1\.0\.1, \.NET Core 2\.0, and \.NET Core 2\.1
+ PowerShell – \.NET Core 2\.1
+ Go – Go 1\.x

Not all runtime languages are available on the public Amazon Linux AMI version or its yum repositories\. You might need to download and install them manually from their respective public sites\.

## Environment Variables Available to Lambda Functions<a name="lambda-environment-variables"></a>

The following is a list of environment variables that are part of the AWS Lambda execution environment and made available to Lambda functions\. The table below indicates which ones are reserved by AWS Lambda and can't be changed, as well as which ones you can set when creating your Lambda function\. For more information on using environment variables with your Lambda function, see [Environment Variables](env_variables.md)\. 


**Lambda Environment Variables**  

| Key | Reserved | Value | 
| --- | --- | --- | 
| LAMBDA\_TASK\_ROOT | Yes | Contains the path to your Lambda function code\. | 
| AWS\_EXECUTION\_ENV | Yes | The environment variable is set to one of the following options, depending on the runtime of the Lambda function: [\[See the AWS documentation website for more details\]](http://docs.aws.amazon.com/lambda/latest/dg/current-supported-versions.html)  | 
| LAMBDA\_RUNTIME\_DIR | Yes | Restricted to Lambda runtime\-related artifacts\. For example, the aws\-sdk for Node\.js and boto3 for Python can be found under this path\. | 
| AWS\_REGION | Yes | The AWS Region where the Lambda function is executed\. | 
| AWS\_DEFAULT\_REGION | Yes | The AWS Region where the Lambda function is executed\. | 
| AWS\_LAMBDA\_LOG\_GROUP\_NAME | Yes | The name of Amazon CloudWatch Logs group where log streams that contain your Lambda function logs are created\. | 
| AWS\_LAMBDA\_LOG\_STREAM\_NAME | Yes | The Amazon CloudWatch Logs streams that contain your Lambda function logs\. | 
| AWS\_LAMBDA\_FUNCTION\_NAME | Yes | The name of the Lambda function\. | 
| AWS\_LAMBDA\_FUNCTION\_MEMORY\_SIZE | Yes | The size of the Lambda function in MB\. | 
| AWS\_LAMBDA\_FUNCTION\_VERSION | Yes | The version of the Lambda function\. | 
| AWS\_ACCESS\_KEY AWS\_ACCESS\_KEY\_ID AWS\_SECRET\_KEY AWS\_SECRET\_ACCESS\_KEY AWS\_SESSION\_TOKEN AWS\_SECURITY\_TOKEN  | Yes | The security credentials that are required to execute the Lambda function, depending on which runtime is used\. Different runtimes use a subset of these keys\. They are generated via an IAM execution role that's specified for the function\. | 
| PATH | No | Contains /var/lang/bin, /usr/local/bin, /usr/bin and /bin for running executables\. | 
| LANG | No | Set to en\_US\.UTF\-8\. This is the locale of the runtime\.  | 
| LD\_LIBRARY\_PATH | No | Contains /lib64, /usr/lib64, LAMBDA\_TASK\_ROOT, LAMBDA\_TASK\_ROOT/lib\. Used to store helper libraries and function code\. | 
| NODE\_PATH | No | Set for the Node\.js runtime\. It contains LAMBDA\_RUNTIME\_DIR, LAMBDA\_RUNTIME\_DIR/node\_modules, LAMBDA\_TASK\_ROOT\. | 
| PYTHONPATH | No | Set for the Python runtime\. It contains LAMBDA\_RUNTIME\_DIR\. | 
| TZ | Yes | The current local time\. Defaults to [UTC](https://www.timeanddate.com/worldclock/timezone/utc)\. | 