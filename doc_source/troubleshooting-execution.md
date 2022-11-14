# Troubleshoot execution issues in Lambda<a name="troubleshooting-execution"></a>

When the Lambda runtime runs your function code, the event might be processed on an instance of the function that's been processing events for some time, or it might require a new instance to be initialized\. Errors can occur during function initialization, when your handler code processes the event, or when your function returns \(or fails to return\) a response\.

Function execution errors can be caused by issues with your code, function configuration, downstream resources, or permissions\. If you invoke your function directly, you see function errors in the response from Lambda\. If you invoke your function asynchronously, with an event source mapping, or through another service, you might find errors in logs, a dead\-letter queue, or an on\-failure destination\. Error handling options and retry behavior vary depending on how you invoke your function and on the type of error\.

When your function code or the Lambda runtime return an error, the status code in the response from Lambda is 200 OK\. The presence of an error in the response is indicated by a header named `X-Amz-Function-Error`\. 400 and 500\-series status codes are reserved for [invocation errors](troubleshooting-invocation.md)\.

## Lambda: Execution takes too long<a name="troubleshooting-execution-toolong"></a>

**Issue:** *Function execution takes too long\.*

If your code takes much longer to run in Lambda than on your local machine, it may be constrained by the memory or processing power available to the function\. [Configure the function with additional memory](configuration-function-common.md) to increase both memory and CPU\.

## Lambda: Logs or traces don't appear<a name="troubleshooting-execution-logstraces"></a>

**Issue:** *Logs don't appear in CloudWatch Logs\.*

**Issue:** *Traces don't appear in AWS X\-Ray\.*

Your function needs permission to call CloudWatch Logs and X\-Ray\. Update its [execution role](lambda-intro-execution-role.md) to grant it permission\. Add the following managed policies to enable logs and tracing\.
+ **AWSLambdaBasicExecutionRole**
+ **AWSXRayDaemonWriteAccess**

When you add permissions to your function, update its code or configuration as well\. This forces running instances of your function, which have outdated credentials, to stop and be replaced\.

**Note**  
It may take 5 to 10 minutes for logs to show up after a function invocation\.

## Lambda: The function returns before execution finishes<a name="troubleshooting-execution-unfinished"></a>

**Issue: \(Node\.js\)** *Function returns before code finishes executing*

Many libraries, including the AWS SDK, operate asynchronously\. When you make a network call or perform another operation that requires waiting for a response, libraries return an object called a promise that tracks the progress of the operation in the background\.

To wait for the promise to resolve into a response, use the `await` keyword\. This blocks your handler code from executing until the promise is resolved into an object that contains the response\. If you don't need to use the data from the response in your code, you can return the promise directly to the runtime\.

Some libraries don't return promises but can be wrapped in code that does\. For more information, see [AWS Lambda function handler in Node\.js](nodejs-handler.md)\.

## AWS SDK: Versions and updates<a name="troubleshooting-execution-versions"></a>

**Issue:** *The AWS SDK included on the runtime is not the latest version*

**Issue:** *The AWS SDK included on the runtime updates automatically*

Runtimes for scripting languages include the AWS SDK and are periodically updated to the latest version\. The current version for each runtime is listed on [runtimes page](lambda-runtimes.md)\. To use a newer version of the AWS SDK, or to lock your functions to a specific version, you can bundle the library with your function code, or [create a Lambda layer](configuration-layers.md)\. For details on creating a deployment package with dependencies, see the following topics:

------
#### [ Node\.js ]

[Deploy Node\.js Lambda functions with \.zip file archives](nodejs-package.md) 

------
#### [ Python ]

 [Deploy Python Lambda functions with \.zip file archives](python-package.md) 

------
#### [ Ruby ]

 [Deploy Ruby Lambda functions with \.zip file archives](ruby-package.md) 

------
#### [ Java ]

 [Deploy Java Lambda functions with \.zip or JAR file archives](java-package.md) 

------
#### [ Go ]

 [Deploy Go Lambda functions with \.zip file archives](golang-package.md) 

------
#### [ C\# ]

 [Deploy C\# Lambda functions with \.zip file archives](csharp-package.md) 

------
#### [ PowerShell ]

 [Deploy PowerShell Lambda functions with \.zip file archives](powershell-package.md) 

------

## Python: Libraries load incorrectly<a name="troubleshooting-execution-libraries"></a>

**Issue:** \(Python\) *Some libraries don't load correctly from the deployment package*

Libraries with extension modules written in C or C\+\+ must be compiled in an environment with the same processor architecture as Lambda \(Amazon Linux\)\. For more information, see [Deploy Python Lambda functions with \.zip file archives](python-package.md)\.